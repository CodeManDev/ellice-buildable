package dev.felix.ellice.feature.notebot;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.function.DoubleConsumer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

public final class NotebotCodec {
  public static final int MAX_SECONDS = 1200;

  public static boolean supports(Path path) {
    if (path != null && path.getFileName() != null) {
      String text = path.getFileName().toString().toLowerCase(Locale.ROOT);
      return text.endsWith(".mp3")
          || text.endsWith(".wav")
          || text.endsWith(".flac")
          || text.endsWith(".ogg")
          || text.endsWith(".m4a");
    } else {
      return false;
    }
  }

  public NotebotTitleService decode(Path path, DoubleConsumer doubleConsumer) throws Exception {
    Path currentPath = path.toRealPath();
    if (Files.isRegularFile(currentPath) && supports(currentPath)) {
      if (Files.size(currentPath) > 536870912L) {
        throw new IOException("Choose an audio file smaller than 512 MB.");
      }

      NotebotFinishService notebotFinish = new NotebotFinishService();
      FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(currentPath.toFile());

      try {
        fFmpegFrameGrabber.setAudioChannels(1);
        fFmpegFrameGrabber.setSampleRate(22050);
        fFmpegFrameGrabber.setSampleFormat(3);
        fFmpegFrameGrabber.setOption("protocol_whitelist", "file,pipe");
        fFmpegFrameGrabber.start();
        if (fFmpegFrameGrabber.getAudioStream() < 0) {
          throw new IOException("This file has no audio stream.");
        }

        double doubleValue = fFmpegFrameGrabber.getLengthInTime() / 1000000.0;
        if (doubleValue > 1201.0) {
          throw new IOException("Choose a track up to 20 minutes long.");
        }

        long longValue = 0L;
        int value = -1;

        Frame frame;
        while ((frame = fFmpegFrameGrabber.grabSamples()) != null) {
          if (Thread.currentThread().isInterrupted()) {
            throw new CancellationException();
          }

          if (frame.samples != null && frame.samples.length != 0) {
            if (!(frame.samples[0] instanceof FloatBuffer floatBuffer)) {
              throw new IOException("The audio decoder returned an unsupported sample format.");
            }

            FloatBuffer currentFloatBuffer = floatBuffer.duplicate();

            while (currentFloatBuffer.hasRemaining()) {
              notebotFinish.accept(currentFloatBuffer.get());
              if (++longValue > 26460000L) {
                throw new IOException("Choose a track up to 20 minutes long.");
              }
            }

            int currentValue =
                doubleValue > 0.0
                    ? (int) Math.min(99.0, longValue * 100.0 / 22050.0 / doubleValue)
                    : 0;
            if (currentValue != value) {
              value = currentValue;
              doubleConsumer.accept(currentValue / 100.0);
            }
          }
        }

        if (longValue == 0L) {
          throw new IOException("The file contains no decodable audio.");
        }
      } catch (Throwable exception) {
        try {
          fFmpegFrameGrabber.close();
        } catch (Throwable currentException) {
          exception.addSuppressed(currentException);
        }

        throw exception;
      }

      fFmpegFrameGrabber.close();
      NotebotTitleService notebotTitle = notebotFinish.finish(currentPath.getFileName().toString());
      if (notebotTitle.notes().isEmpty()) {
        throw new IOException(
            "No clear melody found. Try a piano, instrumental or isolated melody track.");
      }

      doubleConsumer.accept(1.0);
      return notebotTitle;
    } else {
      throw new IOException("Choose an MP3, WAV, FLAC, OGG or M4A audio file.");
    }
  }
}
