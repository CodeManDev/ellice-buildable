package dev.felix.ellice.feature.notebot;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.DoubleConsumer;
import java.util.function.LongSupplier;

public final class NotebotDecoderTracker implements AutoCloseable {
  private final NotebotAvailableHandler notebotAvailableHandler;
  private final NotebotRepository notebotRepository;
  private final Executor executor;
  private final NotebotDecoderTracker.Decoder decoder;
  private final List<Runnable> items = new CopyOnWriteArrayList<>();
  private ExecutorService executor2;
  private Future<?> future;
  private int count;
  private int count2;
  private boolean enabled;
  private double value;
  private String text = "";
  private boolean enabled2 = true;
  private int count3 = 2;
  private double value2 = 1.0;
  private NotebotAvailableHandler.Scan scan2;
  private String text2 = "";

  public NotebotDecoderTracker(NotebotAvailableHandler notebotAvailable, Executor executor) {
    this(notebotAvailable, executor, new NotebotCodec()::decode);
  }

  public NotebotDecoderTracker(
      NotebotAvailableHandler notebotAvailable,
      Executor executor,
      NotebotDecoderTracker.Decoder decoder) {
    this(notebotAvailable, executor, decoder, System::nanoTime);
  }

  public NotebotDecoderTracker(
      NotebotAvailableHandler notebotAvailable,
      Executor currentExecutor,
      NotebotDecoderTracker.Decoder currentDecoder,
      LongSupplier longSupplier) {
    this.notebotAvailableHandler = notebotAvailable;
    this.notebotRepository = new NotebotRepository(notebotAvailable, longSupplier);
    this.executor = currentExecutor;
    this.decoder = currentDecoder;
    this.scan2 =
        new NotebotAvailableHandler.Scan(
            new NotebotAvailableHandler.Context(null, "Scan the blocks around you.", false),
            List.of(),
            0,
            0);
  }

  public NotebotRepository playback() {
    return this.notebotRepository;
  }

  public NotebotAvailableHandler.Scan scan() {
    return this.scan2;
  }

  public boolean importing() {
    return this.enabled;
  }

  public double importProgress() {
    return this.value;
  }

  public String message() {
    return !this.text.isEmpty() ? this.text : this.notebotRepository.message();
  }

  public boolean autoTune() {
    return this.enabled2;
  }

  public int voices() {
    return this.count3;
  }

  public double speed() {
    return this.value2;
  }

  public boolean performing() {
    return this.notebotRepository.phase() == NotebotRepository.Phase.PLAYING
        || this.notebotRepository.phase() == NotebotRepository.Phase.TUNING;
  }

  public boolean arrangementLocked() {
    return this.performing() || this.notebotRepository.phase() == NotebotRepository.Phase.PAUSED;
  }

  public void dispatch(Runnable runnable) {
    this.executor.execute(runnable);
  }

  public AutoCloseable listen(Runnable runnable) {
    this.items.add(runnable);
    return () -> this.items.remove(runnable);
  }

  public void autoTune(boolean enabled) {
    if (!this.arrangementLocked()) {
      this.enabled2 = enabled;
      this.updateState();
    }
  }

  public void voices(int value) {
    if (!this.arrangementLocked()) {
      this.count3 = Math.max(1, Math.min(3, value));
      this.updateState();
    }
  }

  public void speed(double doubleValue) {
    if (Double.isFinite(doubleValue)) {
      this.value2 = Math.max(0.5, Math.min(1.5, doubleValue));
      this.notebotRepository.speed(this.value2);
      this.updateState();
    }
  }

  public void loop(boolean enabled) {
    this.notebotRepository.loop(enabled);
    this.updateState();
  }

  public void seek(double doubleValue) {
    if (!this.enabled && this.notebotRepository.song() != null && Double.isFinite(doubleValue)) {
      this.notebotRepository.seek(
          Math.max(0.0, Math.min(1.0, doubleValue))
              * this.notebotRepository.song().durationTicks());
      this.updateState();
    }
  }

  public void importSong(Path path) {
    this.cancelImport();
    this.notebotRepository.stop();
    int currentCount = ++this.count;
    this.enabled = true;
    this.value = 0.0;
    this.text = "Listening for melody and chords...";
    this.updateState();
    if (this.executor2 == null || this.executor2.isShutdown()) {
      this.executor2 =
          Executors.newSingleThreadExecutor(
              item -> {
                Thread thread = new Thread(item, "ellice-note-analysis");
                thread.setDaemon(true);
                return thread;
              });
    }

    this.future =
        this.executor2.submit(
            () -> {
              try {
                NotebotTitleService notebotTitle =
                    this.decoder.decode(
                        path,
                        item ->
                            this.dispatch(
                                () -> {
                                  if (currentCount == this.count && this.enabled) {
                                    this.value = Math.max(0.0, Math.min(1.0, item));
                                    this.updateState();
                                  }
                                }));
                this.dispatch(
                    () -> {
                      if (currentCount == this.count && this.enabled) {
                        this.enabled = false;
                        this.value = 1.0;
                        this.text = "";
                        this.notebotRepository.load(notebotTitle);
                        this.rescan();
                      }
                    });
              } catch (CancellationException cancellationException) {
              } catch (Exception | LinkageError exceptionLinkageError) {
                this.dispatch(
                    () -> {
                      if (currentCount == this.count && this.enabled) {
                        this.enabled = false;
                        String currentText = exceptionLinkageError.getMessage();
                        this.text =
                            currentText != null && !currentText.isBlank()
                                ? currentText
                                : "The audio could not be decoded. Try another file.";
                        this.updateState();
                      }
                    });
              }
            });
  }

  public void cancelImport() {
    this.count++;
    if (this.future != null) {
      this.future.cancel(true);
    }

    this.future = null;
    this.enabled = false;
    this.text = "";
    this.updateState();
  }

  public void rescan() {
    this.scan2 = this.notebotAvailableHandler.scan();
    this.updateState();
  }

  public void playPause() {
    if (!this.enabled && this.notebotRepository.song() != null) {
      this.text = "";
      if (this.performing()) {
        this.notebotRepository.pause();
      } else if (this.notebotRepository.phase() == NotebotRepository.Phase.PAUSED) {
        this.notebotRepository.resume();
      } else {
        this.rescan();
        this.notebotRepository.start(this.scan2, this.enabled2, this.count3);
      }

      this.updateState();
    }
  }

  public void stop() {
    this.cancelImport();
    this.notebotRepository.stop();
    this.updateState();
  }

  public void disconnect() {
    this.notebotRepository.disconnect();
    this.rescan();
  }

  public void tick() {
    this.notebotRepository.tick();
    if (++this.count2 % 20 == 0 && !this.items.isEmpty() && !this.performing()) {
      this.scan2 = this.notebotAvailableHandler.scan();
    }

    String text =
        this.notebotRepository.phase()
            + ":"
            + this.notebotRepository.message()
            + ":"
            + (int) (this.notebotRepository.position() / 20.0)
            + ":"
            + this.notebotRepository.tunedClicks()
            + ":"
            + this.notebotRepository.skippedNotes()
            + ":"
            + this.scan2.hashCode();
    if (!this.text2.equals(text)) {
      this.text2 = text;
      this.updateState();
    }
  }

  private void updateState() {
    this.items.forEach(Runnable::run);
  }

  @Override
  public void close() {
    this.stop();
    if (this.executor2 != null) {
      this.executor2.shutdownNow();
    }

    this.executor2 = null;
  }

  @FunctionalInterface
  public interface Decoder {
    NotebotTitleService decode(Path path, DoubleConsumer doubleConsumer) throws Exception;
  }
}
