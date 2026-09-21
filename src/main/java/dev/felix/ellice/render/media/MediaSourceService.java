package dev.felix.ellice.render.media;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.HexFormat;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import net.fabricmc.loader.api.FabricLoader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;
import org.bytedeco.javacv.Java2DFrameConverter;

public final class MediaSourceService implements AutoCloseable {
  private static final long timestamp2 = 20000000L;
  private static final long timestamp3 = 120000000L;
  private static final long timestamp4 = 500000000L;
  private static final int count = 24;
  private static final int count2 = 6;
  private static final int count3 = 64;
  private static final int count4 = 1347830579;
  private static final int count5 = 3;
  private static final int count6 = 32;
  private static final long timestamp5 = 1000000000L;
  private Path path;
  private volatile boolean enabled = true;
  private volatile boolean enabled2 = true;
  private volatile boolean enabled3;
  private volatile float value = 1.0F;
  private volatile float value2 = 30.0F;
  private volatile boolean enabled4;
  private volatile float value3 = 60.0F;
  private volatile int count7;
  private volatile int count8;
  private volatile MediaSourceService.ScaleMode scaleMode = MediaSourceService.ScaleMode.CONTAIN;
  private volatile boolean enabled5;
  private volatile long timestamp6;
  private volatile Thread thread;
  private volatile MediaSourceService.DecodedFrame decodedFrame;
  private volatile String text;
  private final Object object = new Object();
  private final ArrayDeque<MediaSourceService.PreparedFrame> items = new ArrayDeque<>();
  private final ArrayDeque<ByteBuffer> items2 = new ArrayDeque<>();
  private long timestamp7;
  private long timestamp8;
  private long timestamp9;
  private long timestamp10;
  private long timestamp11;
  private boolean enabled6;

  public synchronized void source(Path sourcePath) {
    Path currentSourcePath = sourcePath == null ? null : sourcePath.toAbsolutePath().normalize();
    if (!Objects.equals(this.path, currentSourcePath)) {
      this.updateState2();
      this.path = currentSourcePath;
      this.updateState14();
      this.text = null;
      this.updateState19();
      if (this.path != null && this.enabled2) {
        this.enabled3 = true;
        this.updateState();
      } else if (this.path != null && this.enabled3) {
        this.updateState();
      }
    }
  }

  public synchronized void autoplay(boolean enabled) {
    this.enabled2 = enabled;
    if (enabled && this.path != null) {
      this.enabled3 = true;
      this.updateState();
    }
  }

  public void loop(boolean currentEnabled) {
    this.enabled = currentEnabled;
  }

  public synchronized void playing(boolean enabled) {
    this.enabled3 = enabled;
    if (enabled) {
      this.updateState();
    }
  }

  public void playbackRate(float currentValue) {
    this.value = currentValue > 0.0F ? currentValue : 1.0F;
  }

  public void fallbackFps(float value) {
    if (value > 0.0F) {
      this.value2 = value;
    }
  }

  public void frameRate(float value) {
    if (value > 0.0F) {
      this.value2 = value;
      this.enabled4 = true;
    } else {
      this.enabled4 = false;
    }
  }

  public synchronized void outputSize(int value, int currentValue) {
    this.outputSize(value, currentValue, MediaSourceService.ScaleMode.CONTAIN);
  }

  public synchronized void outputSize(
      int value, int currentValue, MediaSourceService.ScaleMode currentScaleMode) {
    int nextValue = calculateValue4(value);
    int previousValue = calculateValue4(currentValue);
    MediaSourceService.ScaleMode nextScaleMode =
        currentScaleMode != null ? currentScaleMode : MediaSourceService.ScaleMode.CONTAIN;
    int sourceValue = nextValue <= this.count7 && previousValue <= this.count8 ? 0 : 1;
    int targetValue = this.scaleMode != nextScaleMode ? 1 : 0;
    if (sourceValue != 0 || targetValue != 0) {
      this.count7 = Math.max(this.count7, nextValue);
      this.count8 = Math.max(this.count8, previousValue);
      this.scaleMode = nextScaleMode;
      boolean enabled = this.enabled5;
      if (enabled) {
        this.updateState2();
      }

      this.updateState14();
      this.updateState19();
      if (enabled && this.path != null && this.enabled3) {
        this.updateState();
      }
    }
  }

  public void outputFrameRate(float value) {
    this.value3 = Math.max(0.0F, value);
  }

  public synchronized void restart() {
    this.updateState2();
    this.updateState14();
    this.text = null;
    this.updateState19();
    this.enabled3 = true;
    this.updateState();
  }

  public MediaSourceService.DecodedFrame latestFrame() {
    if (!this.enabled3) {
      return this.decodedFrame;
    }

    synchronized (this.object) {
      long longValue = System.nanoTime();
      if (this.items.isEmpty()) {
        if (!this.enabled6) {
          this.timestamp9 = 0L;
        }

        return this.decodedFrame;
      } else {
        if (this.timestamp9 == 0L) {
          if (!this.enabled6 && this.timestamp8 < 120000000L) {
            return this.decodedFrame;
          }

          this.timestamp9 = longValue;
        }

        byte byteValue;
        for (byteValue = 0; !this.items.isEmpty() && longValue >= this.timestamp9; byteValue = 1) {
          MediaSourceService.PreparedFrame preparedFrame = this.items.removeFirst();
          this.timestamp8 = Math.max(0L, this.timestamp8 - preparedFrame.durationNanos());
          this.updateState13(this.decodedFrame);
          this.decodedFrame =
              new MediaSourceService.DecodedFrame(
                  preparedFrame.rgba(),
                  preparedFrame.width(),
                  preparedFrame.height(),
                  ++this.timestamp7);
          this.timestamp9 = this.timestamp9 + preparedFrame.durationNanos();
        }

        if (byteValue != 0) {
          this.object.notifyAll();
        }

        return this.decodedFrame;
      }
    }
  }

  public String lastError() {
    return this.text;
  }

  public boolean hasSource() {
    return this.path != null;
  }

  private synchronized void updateState() {
    if (this.enabled3 && this.path != null) {
      Thread currentThread = this.thread;
      if (!this.enabled5 || currentThread == null || !currentThread.isAlive()) {
        this.enabled5 = true;
        long offset = ++this.timestamp6;
        Path currentPath = this.path;
        Thread nextThread =
            new Thread(
                () -> this.updateState3(currentPath, offset),
                "ellice MP4 Decoder - " + currentPath.getFileName());
        nextThread.setDaemon(true);
        this.thread = nextThread;
        nextThread.start();
      }
    }
  }

  private synchronized void updateState2() {
    this.enabled5 = false;
    this.timestamp6++;
    Thread currentThread = this.thread;
    this.thread = null;
    if (currentThread != null) {
      currentThread.interrupt();
    }

    synchronized (this.object) {
      this.object.notifyAll();
    }
  }

  private boolean checkCondition(long longValue) {
    return this.enabled5 && this.timestamp6 == longValue && !Thread.currentThread().isInterrupted();
  }

  private void updateState3(Path path, long offset) {
    while (this.checkCondition(offset)) {
      if (!Files.isRegularFile(path)) {
        if (this.checkCondition(offset)) {
          this.text = "MP4 file not found: " + path;
          this.enabled5 = false;
        }

        return;
      }

      try {
        this.updateState6(offset);
        if (!this.checkCondition(offset)) {
          return;
        }

        Path currentPath = this.createPath(path);
        this.updateState8(path, currentPath, offset);
        if (!this.checkCondition(offset)) {
          return;
        }

        this.updateState9(currentPath, offset);
      } catch (Throwable exception) {
        if (this.checkCondition(offset)) {
          this.text =
              exception.getMessage() != null
                  ? exception.getMessage()
                  : exception.getClass().getSimpleName();
          CoreIsInitializedHandler.LOGGER.warn("ellice MP4 decode failed for {}", path, exception);
          this.enabled5 = false;
        }

        return;
      }

      if (!this.checkCondition(offset)) {
        return;
      }

      if (!this.enabled) {
        if (this.checkCondition(offset)) {
          synchronized (this.object) {
            this.enabled6 = true;
            this.object.notifyAll();
          }

          this.enabled5 = false;
        }

        return;
      }

      this.updateState18();
    }
  }

  private void updateState4(Path path, long offset, MediaSourceService.FrameConsumer frameConsumer)
      throws IOException {
    FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(path.toFile());
    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    byte byteValue = 0;

    try {
      fFmpegFrameGrabber.setOption("threads", "0");
      fFmpegFrameGrabber.setOption("probesize", "5000000");
      fFmpegFrameGrabber.setOption("analyzeduration", "5000000");
      fFmpegFrameGrabber.setImageMode(ImageMode.COLOR);
      fFmpegFrameGrabber.start();
      byteValue = 1;
      long currentOffset = this.calculateValue(fFmpegFrameGrabber);
      MediaSourceService.FrameSnapshot frameSnapshot = null;

      while (this.checkCondition(offset)) {
        this.checkCondition5(offset);
        if (!this.checkCondition(offset)) {
          break;
        }

        Frame frame = fFmpegFrameGrabber.grabImage();
        if (frame == null) {
          break;
        }

        BufferedImage bufferedImage = java2DFrameConverter.convert(frame);
        if (bufferedImage != null) {
          bufferedImage = this.createBufferedImage(bufferedImage);
          long nextOffset =
              frame.timestamp > 0L ? frame.timestamp : fFmpegFrameGrabber.getTimestamp();
          MediaSourceService.FrameSnapshot width =
              new MediaSourceService.FrameSnapshot(
                  this.createByteBuffer3(bufferedImage),
                  bufferedImage.getWidth(),
                  bufferedImage.getHeight(),
                  nextOffset);
          if (frameSnapshot != null) {
            long previousOffset =
                this.calculateValue2(
                    frameSnapshot.timestampMicros(), width.timestampMicros(), currentOffset);
            this.updateState5(frameConsumer, frameSnapshot, previousOffset);
          }

          frameSnapshot = width;
        }
      }

      if (frameSnapshot != null && this.checkCondition(offset)) {
        this.updateState5(frameConsumer, frameSnapshot, currentOffset);
      }

      this.text = null;
    } catch (Exception exception) {
      throw new IOException("FFmpeg MP4 decode failed", exception);
    } finally {
      if (byteValue != 0) {
        try {
          fFmpegFrameGrabber.stop();
        } catch (Exception currentException) {
        }
      }

      try {
        fFmpegFrameGrabber.close();
      } catch (Exception nextException) {
      }

      java2DFrameConverter.close();
    }
  }

  private void updateState5(
      MediaSourceService.FrameConsumer frameConsumer,
      MediaSourceService.FrameSnapshot frameSnapshot,
      long offset)
      throws IOException {
    if (frameConsumer.shouldAccept(offset)) {
      frameConsumer.accept(
          frameSnapshot.rgba(), frameSnapshot.width(), frameSnapshot.height(), offset);
    } else {
      this.updateState15(frameSnapshot.rgba());
    }
  }

  private long calculateValue(FFmpegFrameGrabber fFmpegFrameGrabber) {
    double doubleValue = this.enabled4 ? this.value2 : fFmpegFrameGrabber.getFrameRate();
    if (!Double.isFinite(doubleValue) || doubleValue <= 0.0) {
      doubleValue = Math.max(1.0F, this.value2);
    }

    return this.calculateValue3(1.0 / doubleValue);
  }

  private long calculateValue2(long longValue, long currentLongValue, long nextLongValue) {
    if (!this.enabled4 && currentLongValue > longValue) {
      double doubleValue = (currentLongValue - longValue) / 1000000.0;
      if (Double.isFinite(doubleValue) && doubleValue > 0.0) {
        return this.calculateValue3(doubleValue);
      }
    }

    return nextLongValue;
  }

  private long calculateValue3(double doubleValue) {
    float currentValue = this.value > 0.0F ? this.value : 1.0F;
    return Math.max(1L, (long) (doubleValue * 1.0E9 / currentValue));
  }

  private void updateState6(long offset) {
    long currentOffset = System.nanoTime() + 1000000000L;

    while (this.checkCondition(offset)
        && (this.count7 <= 0 || this.count8 <= 0)
        && System.nanoTime() < currentOffset) {
      LockSupport.parkNanos(20000000L);
    }
  }

  private Path createPath(Path path) throws IOException {
    Path currentPath =
        FabricLoader.getInstance().getGameDir().resolve("ellice-cache").resolve("videos");
    Files.createDirectories(currentPath);
    String nextPath = this.createText(path);
    return currentPath.resolve(nextPath + ".pvc");
  }

  private String createText(Path path) throws IOException {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      updateState7(messageDigest, path.toAbsolutePath().normalize().toString());
      updateState7(messageDigest, Long.toString(Files.size(path)));
      updateState7(messageDigest, Long.toString(Files.getLastModifiedTime(path).toMillis()));
      updateState7(messageDigest, Integer.toString(Math.max(0, this.count7)));
      updateState7(messageDigest, Integer.toString(Math.max(0, this.count8)));
      updateState7(messageDigest, this.scaleMode.name());
      updateState7(messageDigest, Float.toString(this.value3));
      updateState7(messageDigest, Integer.toString(3));
      return HexFormat.of().formatHex(messageDigest.digest(), 0, 16);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new IOException("SHA-256 is unavailable", noSuchAlgorithmException);
    }
  }

  private static void updateState7(MessageDigest messageDigest, String text) {
    messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
    messageDigest.update((byte) 0);
  }

  private void updateState8(Path path, Path currentPath, long offset) throws IOException {
    if (!this.checkCondition2(currentPath)) {
      Path nextPath = currentPath.resolveSibling(currentPath.getFileName() + ".tmp");
      Files.deleteIfExists(nextPath);
      CoreIsInitializedHandler.LOGGER.info(
          "Preparing ellice FFmpeg video cache for {}", path.getFileName());
      this.updateState18();
      byte byteValue = 0;
      final MediaSourceService.CacheWriter cacheWriter =
          new MediaSourceService.CacheWriter(nextPath);

      try {
        this.updateState4(
            path,
            offset,
            new MediaSourceService.FrameConsumer() {
              @Override
              public boolean shouldAccept(long longValue) {
                return MediaSourceService.this.checkCondition4(longValue);
              }

              @Override
              public void accept(ByteBuffer byteBuffer, int value, int currentValue, long longValue)
                  throws IOException {
                try {
                  cacheWriter.updateState20(byteBuffer, value, currentValue, longValue);
                } finally {
                  MediaSourceService.this.updateState15(byteBuffer);
                }
              }
            });
        cacheWriter.updateState21();
        byteValue = 1;
      } finally {
        cacheWriter.close();
        this.updateState18();
        if (byteValue == 0) {
          Files.deleteIfExists(nextPath);
        }
      }

      if (!this.checkCondition(offset)) {
        Files.deleteIfExists(nextPath);
      } else {
        try {
          Files.move(
              nextPath,
              currentPath,
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
          Files.move(nextPath, currentPath, StandardCopyOption.REPLACE_EXISTING);
        }

        CoreIsInitializedHandler.LOGGER.info(
            "ellice FFmpeg video cache ready: {}", currentPath.getFileName());
      }
    }
  }

  private boolean checkCondition2(Path path) {
    if (!Files.isRegularFile(path)) {
      return false;
    }

    try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
      MediaSourceService.CacheHeader cacheHeader = createCacheHeader(seekableByteChannel);
      long longValue = 32L + cacheHeader.frameCount() * (8L + cacheHeader.frameBytes());
      return cacheHeader.frameCount() > 0
          && cacheHeader.width() > 0
          && cacheHeader.height() > 0
          && cacheHeader.frameBytes()
              == Math.multiplyExact(
                  Math.multiplyExact(cacheHeader.width(), cacheHeader.height()), 4)
          && Files.size(path) == longValue;
    } catch (IOException | RuntimeException iOExceptionRuntimeException) {
      return false;
    }
  }

  private void updateState9(Path path, long offset) throws IOException {
    try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)) {
      MediaSourceService.CacheHeader cacheHeader = createCacheHeader(seekableByteChannel);
      int value = cacheHeader.frameBytes();
      ByteBuffer byteBuffer = ByteBuffer.allocate(8);

      for (int index = 0;
          this.checkCondition(offset) && index < cacheHeader.frameCount();
          index++) {
        this.checkCondition5(offset);
        if (!this.checkCondition(offset)) {
          break;
        }

        this.updateState17(offset);
        if (!this.checkCondition(offset)) {
          break;
        }

        byteBuffer.clear();
        updateState11(seekableByteChannel, byteBuffer);
        byteBuffer.flip();
        long currentOffset = byteBuffer.getLong();
        ByteBuffer currentByteBuffer = this.createByteBuffer2(value);
        updateState11(seekableByteChannel, currentByteBuffer);
        currentByteBuffer.flip();
        this.updateState16(
            new MediaSourceService.PreparedFrame(
                currentByteBuffer, cacheHeader.width(), cacheHeader.height(), currentOffset));
      }
    }
  }

  private static ByteBuffer createByteBuffer(ByteBuffer byteBuffer) {
    ByteBuffer currentByteBuffer = byteBuffer.duplicate();
    currentByteBuffer.rewind();
    return currentByteBuffer;
  }

  private static MediaSourceService.CacheHeader createCacheHeader(
      ReadableByteChannel readableByteChannel) throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocate(32);
    updateState11(readableByteChannel, byteBuffer);
    byteBuffer.flip();
    int value = byteBuffer.getInt();
    int currentValue = byteBuffer.getInt();
    int nextValue = byteBuffer.getInt();
    int previousValue = byteBuffer.getInt();
    int sourceValue = byteBuffer.getInt();
    int targetValue = byteBuffer.getInt();
    byteBuffer.getLong();
    if (value != 1347830579) {
      throw new IOException("Invalid ellice video cache");
    } else if (currentValue != 3) {
      throw new IOException("Unsupported ellice video cache version");
    } else if (nextValue >= 0 && previousValue >= 0 && sourceValue >= 0 && targetValue >= 0) {
      return new MediaSourceService.CacheHeader(nextValue, previousValue, sourceValue, targetValue);
    } else {
      throw new IOException("Corrupt ellice video cache header");
    }
  }

  private static void updateState10(
      WritableByteChannel writableByteChannel, MediaSourceService.CacheHeader cacheHeader)
      throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocate(32);
    byteBuffer.putInt(1347830579);
    byteBuffer.putInt(3);
    byteBuffer.putInt(cacheHeader.width());
    byteBuffer.putInt(cacheHeader.height());
    byteBuffer.putInt(cacheHeader.frameCount());
    byteBuffer.putInt(cacheHeader.frameBytes());
    byteBuffer.putLong(0L);
    byteBuffer.flip();
    updateState12(writableByteChannel, byteBuffer);
  }

  private static void updateState11(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer)
      throws IOException {
    while (byteBuffer.hasRemaining()) {
      int value = readableByteChannel.read(byteBuffer);
      if (value < 0) {
        throw new IOException("Unexpected EOF in ellice video cache");
      }
    }
  }

  private static void updateState12(WritableByteChannel writableByteChannel, ByteBuffer byteBuffer)
      throws IOException {
    while (byteBuffer.hasRemaining()) {
      writableByteChannel.write(byteBuffer);
    }
  }

  private ByteBuffer createByteBuffer2(int bufferId) {
    synchronized (this.items2) {
      int currentSize = this.items2.size();

      for (int index = 0; index < currentSize; index++) {
        ByteBuffer byteBuffer = this.items2.removeFirst();
        if (byteBuffer.capacity() >= bufferId) {
          byteBuffer.clear();
          byteBuffer.limit(bufferId);
          return byteBuffer;
        }

        this.items2.addLast(byteBuffer);
      }
    }

    ByteBuffer currentByteBuffer = ByteBuffer.allocateDirect(bufferId);
    currentByteBuffer.limit(bufferId);
    return currentByteBuffer;
  }

  private void updateState13(MediaSourceService.DecodedFrame decodedFrame) {
    if (decodedFrame != null) {
      this.updateState15(decodedFrame.rgba());
    }
  }

  private void updateState14() {
    synchronized (this.object) {
      this.updateState13(this.decodedFrame);
      this.decodedFrame = null;
    }
  }

  private void updateState15(ByteBuffer byteBuffer) {
    if (byteBuffer != null) {
      synchronized (this.items2) {
        if (this.items2.size() < 6) {
          byteBuffer.clear();
          this.items2.addLast(byteBuffer);
        }
      }
    }
  }

  private void updateState16(MediaSourceService.PreparedFrame preparedFrame) {
    synchronized (this.object) {
      this.items.addLast(preparedFrame);
      this.timestamp8 = this.timestamp8 + preparedFrame.durationNanos();
      this.enabled6 = false;
      this.object.notifyAll();
    }
  }

  private void updateState17(long offset) {
    synchronized (this.object) {
      while (this.checkCondition(offset) && this.enabled3 && this.checkCondition3()) {
        try {
          this.object.wait(10L);
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
          return;
        }
      }
    }
  }

  private boolean checkCondition3() {
    return this.items.size() >= 24 || this.timestamp8 >= 500000000L;
  }

  private boolean checkCondition4(long longValue) {
    float value = this.value3;
    long currentLongValue = this.timestamp10;
    this.timestamp10 += longValue;
    if (value <= 0.0F) {
      return true;
    }

    long nextLongValue = (long) (1.0E9F / value);
    if (this.timestamp11 == 0L) {
      this.timestamp11 = currentLongValue + nextLongValue;
      return true;
    }

    if (currentLongValue + longValue / 2L < this.timestamp11) {
      return false;
    }

    do {
      this.timestamp11 += nextLongValue;
    } while (this.timestamp11 < currentLongValue);

    return true;
  }

  private void updateState18() {
    this.timestamp10 = 0L;
    this.timestamp11 = 0L;
  }

  private void updateState19() {
    synchronized (this.object) {
      while (!this.items.isEmpty()) {
        this.updateState15(this.items.removeFirst().rgba());
      }

      this.timestamp8 = 0L;
      this.timestamp9 = 0L;
      this.enabled6 = false;
      this.updateState18();
      this.object.notifyAll();
    }
  }

  private static int calculateValue4(int value) {
    int currentValue = Math.max(0, value);
    return currentValue == 0 ? 0 : (currentValue + 64 - 1) / 64 * 64;
  }

  private BufferedImage createBufferedImage(BufferedImage bufferedImage) {
    int offset = this.count7;
    int currentOffset = this.count8;
    if (bufferedImage != null && offset > 0 && currentOffset > 0) {
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      MediaSourceService.ScaleMode currentScaleMode = this.scaleMode;
      int nextOffset;
      int previousOffset;
      if (currentScaleMode == MediaSourceService.ScaleMode.STRETCH) {
        if (width <= offset && height <= currentOffset) {
          return bufferedImage;
        }

        nextOffset = Math.max(1, offset);
        previousOffset = Math.max(1, currentOffset);
      } else {
        float value = (float) offset / width;
        float currentValue = (float) currentOffset / height;
        float nextValue =
            currentScaleMode == MediaSourceService.ScaleMode.COVER
                ? Math.max(value, currentValue)
                : Math.min(value, currentValue);
        if (!(nextValue > 0.0F) || nextValue >= 0.995F) {
          return bufferedImage;
        }

        nextOffset = Math.max(1, Math.round(width * nextValue));
        previousOffset = Math.max(1, Math.round(height * nextValue));
      }

      if (nextOffset == width && previousOffset == height) {
        return bufferedImage;
      }

      BufferedImage currentBufferedImage = new BufferedImage(nextOffset, previousOffset, 2);
      Graphics2D graphics2D = currentBufferedImage.createGraphics();

      try {
        graphics2D.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.setRenderingHint(
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.drawImage(bufferedImage, 0, 0, nextOffset, previousOffset, null);
      } finally {
        graphics2D.dispose();
      }

      return currentBufferedImage;
    } else {
      return bufferedImage;
    }
  }

  private ByteBuffer createByteBuffer3(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    ByteBuffer byteBuffer =
        this.createByteBuffer2(Math.multiplyExact(Math.multiplyExact(width, height), 4));
    int[] ints = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

    for (int offset : ints) {
      byteBuffer.put((byte) (offset >> 16 & 0xFF));
      byteBuffer.put((byte) (offset >> 8 & 0xFF));
      byteBuffer.put((byte) (offset & 0xFF));
      byteBuffer.put((byte) (offset >> 24 & 0xFF));
    }

    byteBuffer.flip();
    return byteBuffer;
  }

  private boolean checkCondition5(long longValue) {
    byte byteValue = 0;

    while (this.checkCondition(longValue) && !this.enabled3) {
      byteValue = 1;
      LockSupport.parkNanos(20000000L);
    }

    return (byteValue != 0);
  }

  @Override
  public synchronized void close() {
    this.updateState2();
    this.path = null;
    this.enabled3 = false;
    this.updateState14();
    this.updateState19();
  }

  private record CacheHeader(int width, int height, int frameCount, int frameBytes) {}

  private final class CacheWriter implements AutoCloseable {
    private final SeekableByteChannel seekableByteChannel;
    private int count9;
    private int count10;
    private int count11;
    private int count12;

    private CacheWriter(Path path) throws IOException {
      this.seekableByteChannel =
          Files.newByteChannel(
              path,
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING,
              StandardOpenOption.WRITE);
      MediaSourceService.updateState10(
          this.seekableByteChannel, new MediaSourceService.CacheHeader(0, 0, 0, 0));
    }

    private void updateState20(ByteBuffer byteBuffer, int value, int currentValue, long offset)
        throws IOException {
      if (value > 0 && currentValue > 0) {
        int nextValue = Math.multiplyExact(Math.multiplyExact(value, currentValue), 4);
        if (this.count9 == 0) {
          this.count9 = value;
          this.count10 = currentValue;
          this.count11 = nextValue;
        } else if (this.count9 != value
            || this.count10 != currentValue
            || this.count11 != nextValue) {
          throw new IOException("Video cache frame size changed during build");
        }

        ByteBuffer currentByteBuffer = ByteBuffer.allocate(8);
        currentByteBuffer.putLong(Math.max(1L, offset));
        currentByteBuffer.flip();
        MediaSourceService.updateState12(this.seekableByteChannel, currentByteBuffer);
        MediaSourceService.updateState12(
            this.seekableByteChannel, MediaSourceService.createByteBuffer(byteBuffer));
        this.count12++;
      } else {
        throw new IOException("Invalid cached frame size");
      }
    }

    private void updateState21() throws IOException {
      if (this.count12 <= 0) {
        throw new IOException("Video cache produced no frames");
      }

      this.seekableByteChannel.position(0L);
      MediaSourceService.updateState10(
          this.seekableByteChannel,
          new MediaSourceService.CacheHeader(
              this.count9, this.count10, this.count12, this.count11));
    }

    @Override
    public void close() throws IOException {
      this.seekableByteChannel.close();
    }
  }

  public record DecodedFrame(ByteBuffer rgba, int width, int height, long version) {}

  private interface FrameConsumer {
    boolean shouldAccept(long longValue);

    void accept(ByteBuffer byteBuffer, int value, int currentValue, long longValue)
        throws IOException;
  }

  private record FrameSnapshot(ByteBuffer rgba, int width, int height, long timestampMicros) {}

  private record PreparedFrame(ByteBuffer rgba, int width, int height, long durationNanos) {}

  public enum ScaleMode {
    STRETCH,
    CONTAIN,
    COVER;

    private static MediaSourceService.ScaleMode[] $values() {
      return new MediaSourceService.ScaleMode[] {STRETCH, CONTAIN, COVER};
    }
  }
}
