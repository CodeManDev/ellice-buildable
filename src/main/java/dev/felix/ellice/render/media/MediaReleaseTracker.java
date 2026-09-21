package dev.felix.ellice.render.media;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.lwjgl.system.MemoryUtil;

public final class MediaReleaseTracker implements AutoCloseable {
   private final ArrayBlockingQueue<ByteBuffer> arrayBlockingQueue = new ArrayBlockingQueue<>(3);
   private final CompletableFuture<Path> path = new CompletableFuture<>();
   private final int count;
   private final int count2;
   private final int count3;
   private final int count4;
   private final Path path2;
   private volatile boolean enabled;
   private volatile boolean enabled2;

   public MediaReleaseTracker(Path path, int value, int currentValue, int nextValue) {
      this(path, value, currentValue, nextValue, 200000000);
   }

   public MediaReleaseTracker(Path path, int value, int currentValue, int nextValue, int previousValue) {
      if (previousValue >= 1000000 && previousValue <= 400000000) {
         this.count4 = previousValue;
         if (value > 0 && currentValue > 0 && (value & 1) == 0 && (currentValue & 1) == 0 && nextValue > 0) {
            this.path2 = path;
            this.count = value;
            this.count2 = currentValue;
            this.count3 = nextValue;
            Thread thread = new Thread(this::updateState, "ellice clip encoder");
            thread.setDaemon(true);
            thread.start();
         } else {
            throw new IllegalArgumentException("Video dimensions must be positive and even");
         }
      } else {
         throw new IllegalArgumentException("Invalid video bitrate");
      }
   }

   public boolean ready() {
      return !this.enabled && !this.enabled2 && !this.path.isDone() && this.arrayBlockingQueue.remainingCapacity() > 0;
   }

   public synchronized boolean offer(ByteBuffer byteBuffer) {
      if (byteBuffer.remaining() != (long)this.count * this.count2 * 4L) {
         throw new IllegalArgumentException("Invalid video frame size");
      } else {
         return this.ready() && this.arrayBlockingQueue.offer(byteBuffer);
      }
   }

   public void finish() {
      this.enabled = true;
   }

   public CompletableFuture<Path> result() {
      return this.path;
   }

   private void updateState() {
      Path currentPath = null;

      try {
         Files.createDirectories(this.path2.toAbsolutePath().getParent());
         currentPath = Files.createTempFile(this.path2.toAbsolutePath().getParent(), ".clip-", ".mp4");
         String[] strings = new String[]{"h264_videotoolbox", "libopenh264"};
         FFmpegFrameRecorder fFmpegFrameRecorder = null;
         String text = null;
         Exception exception = null;
         String[] currentStrings = strings;
         int currentLength = currentStrings.length;
         int index = 0;

         label333: {
            String currentText;
            FFmpegFrameRecorder currentFFmpegFrameRecorder;
            while (true) {
               if (index >= currentLength) {
                  break label333;
               }

               currentText = currentStrings[index];

               try {
                  Files.deleteIfExists(currentPath);
               } catch (IOException iOException) {
               }

               currentFFmpegFrameRecorder = new FFmpegFrameRecorder(currentPath.toFile(), this.count, this.count2, 0);
               currentFFmpegFrameRecorder.setFormat("mp4");
               currentFFmpegFrameRecorder.setVideoCodecName(currentText);
               currentFFmpegFrameRecorder.setVideoCodec(27);
               currentFFmpegFrameRecorder.setPixelFormat(0);
               currentFFmpegFrameRecorder.setFrameRate(this.count3);
               currentFFmpegFrameRecorder.setVideoBitrate(this.count4);
               currentFFmpegFrameRecorder.setGopSize(1);
               currentFFmpegFrameRecorder.setOption("movflags", "+faststart");
               if ("h264_videotoolbox".equals(currentText)) {
                  currentFFmpegFrameRecorder.setVideoOption("allow_sw", "1");
               } else {
                  currentFFmpegFrameRecorder.setVideoOption("rc_mode", "bitrate");
                  currentFFmpegFrameRecorder.setVideoOption("allow_skip_frames", "1");
                  currentFFmpegFrameRecorder.setVideoOption("cabac", "1");
               }

               try {
                  currentFFmpegFrameRecorder.start();
                  break;
               } catch (Exception currentException) {
                  try {
                     currentFFmpegFrameRecorder.release();
                  } catch (java.lang.Exception nextException) {
                  }

                  exception = currentException;
                  index++;
               }
            }

            fFmpegFrameRecorder = currentFFmpegFrameRecorder;
            text = currentText;
         }

         if (fFmpegFrameRecorder == null) {
            throw new IllegalStateException("No H.264 encoder available (tried VideoToolbox + OpenH264)", exception);
         }

         try {
            int currentIndex = 0;

            while (!this.enabled2) {
               ByteBuffer byteBuffer = this.arrayBlockingQueue.poll(100L, TimeUnit.MILLISECONDS);
               if (byteBuffer == null) {
                  if (this.enabled) {
                     break;
                  }
               } else {
                  try {
                     fFmpegFrameRecorder.recordImage(this.count, this.count2, 8, 4, this.count * 4, 26, new Buffer[]{byteBuffer});
                     currentIndex++;
                  } finally {
                     MemoryUtil.memFree(byteBuffer);
                  }
               }
            }

            if (this.enabled2) {
               throw new CancellationException("Video export cancelled");
            }

            if (currentIndex == 0) {
               throw new IllegalStateException("No video frames rendered (" + text + ")");
            }

            fFmpegFrameRecorder.stop();
            fFmpegFrameRecorder.release();
         } catch (Throwable previousException) {
            try {
               fFmpegFrameRecorder.release();
            } catch (java.lang.Exception sourceException) {
            }

            throw previousException;
         }

         if (this.enabled2) {
            throw new CancellationException("Video export cancelled");
         }

         if (!Files.exists(currentPath) || Files.size(currentPath) < 10000L) {
            throw new IllegalStateException("Encoder " + text + " produced no output");
         }

         Files.move(currentPath, this.path2);
         this.path.complete(this.path2);
      } catch (Throwable targetException) {
         this.path.completeExceptionally(targetException);
      } finally {
         synchronized (this) {
            this.enabled2 = true;

            ByteBuffer currentByteBuffer;
            while ((currentByteBuffer = this.arrayBlockingQueue.poll()) != null) {
               MemoryUtil.memFree(currentByteBuffer);
            }
         }

         if (currentPath != null) {
            try {
               Files.deleteIfExists(currentPath);
            } catch (IOException currentIOException) {
            }
         }
      }
   }

   @Override
   public void close() {
      this.enabled2 = true;
   }
}

