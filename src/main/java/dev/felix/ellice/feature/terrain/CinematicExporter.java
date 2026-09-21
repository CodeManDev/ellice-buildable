package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.media.MediaReleaseTracker;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public final class CinematicExporter implements AutoCloseable {
  private int fcetdxsu6qqn = 2560;
  private int count2 = 1440;
  private int count3 = 200;
  private double value = Double.longBitsToDouble(0x4034000000000000L);
  private double value2;
  private double value3;
  private double f9qmimnjyxxd;
  private int ff3vzzhzdghh;
  private final CompatLoadedHandler compatLoadedHandler;
  private final Render3dSceneService renderer;
  private final Path path;
  private final Consumer<String> text;
  private final LongSupplier longSupplier;
  private State feuieoaf4pwu = State.EMPTY;
  private TerrainAddService terrainAddService;
  private FrameCaptureBuffer terrainCloseService5;
  private CompatLoadedHandler.Textures textures2;
  private UUID uUID;
  private Object object;
  private Object object2;
  private String text2;
  private long timestamp;
  private long timestamp2;
  private TerrainMapRenderer ftvewxbr51x;
  private TerrainMapRenderer.Target ffpohaogpe2o;
  private TerrainCloseService terrainCloseService;
  private PlayerShadowRenderer f8n9322wwsz;
  private TerrainFarForService terrainFarForService;
  private SkyTextureRenderer fjmpsvhz5yhu;
  private MediaReleaseTracker fgjcjxehpjfl;
  private Map<TerrainData, TerrainLayerData> entries = Map.of();
  private int f5o1tb3mcl5;
  private String text3 = "";
  private Path path2;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private double value5 = Double.longBitsToDouble(9221120237041090560L);
  private double value6 = Double.longBitsToDouble(9221120237041090560L);
  public static final int THUMBNAIL_COUNT = 20;
  public static final int THUMBNAIL_WIDTH = 160;
  public static final int THUMBNAIL_HEIGHT = 90;
  private final byte[][] byte2 = new byte[20][];
  private int count6;
  static final double SHUTTER_ANGLE = 0.5;

  static ExportRange exportRange(double d, double d2, double d3) {
    if (!Double.isFinite(d)
        || !Double.isFinite(d2)
        || !Double.isFinite(d3)
        || d <= 0.0
        || d2 < 0.0
        || d3 < 0.0) {
      throw new IllegalArgumentException("Invalid clip timing");
    }
    double d4 = Math.min(d2, d);
    double d5 = d - d4 - d3;
    if (d5 < Double.longBitsToDouble(4586165620538955093L)) {
      throw new IllegalArgumentException("Trim removes the whole clip");
    }
    return new ExportRange(
        d4, d5, (int) Math.ceil(d5 * Double.longBitsToDouble(4627448617123184640L)));
  }

  public void duration(double d) {
    if (this.feuieoaf4pwu == State.RECORDING
        || this.feuieoaf4pwu == State.EXPORTING
        || this.feuieoaf4pwu == State.FINISHING) {
      return;
    }
    if (!Double.isFinite(d) || d < 1.0 || d > Double.longBitsToDouble(0x404E000000000000L)) {
      throw new IllegalArgumentException("Unsupported recording duration");
    }
    this.value = d;
  }

  public void trim(double d, double d2) {
    if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
      return;
    }
    if (!Double.isFinite(d) || !Double.isFinite(d2) || d < 0.0 || d2 < 0.0) {
      throw new IllegalArgumentException("Invalid clip trim");
    }
    this.value2 = d;
    this.value3 = d2;
  }

  public void bitrateMbps(int n) {
    if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
      return;
    }
    if (n < 25 || n > 400) {
      throw new IllegalArgumentException("Unsupported video bitrate");
    }
    this.count3 = n;
  }

  public void resolution(int n) {
    if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
      return;
    }
    if (n != 1080 && n != 1440 && n != 2160) {
      throw new IllegalArgumentException("Unsupported clip resolution");
    }
    this.count2 = n;
    this.fcetdxsu6qqn = n * 16 / 9;
  }

  public CinematicExporter(
      CompatLoadedHandler compatLoadedHandler,
      Render3dSceneService render3dSceneService,
      Path path,
      Consumer<String> consumer) {
    this(compatLoadedHandler, render3dSceneService, path, consumer, System::nanoTime);
  }

  CinematicExporter(
      CompatLoadedHandler compatLoadedHandler,
      Render3dSceneService render3dSceneService,
      Path path,
      Consumer<String> consumer,
      LongSupplier longSupplier) {
    this.compatLoadedHandler = compatLoadedHandler;
    this.renderer = render3dSceneService;
    this.path = path;
    this.text = consumer;
    this.longSupplier = longSupplier;
  }

  public State state() {
    return this.feuieoaf4pwu;
  }

  public boolean recording() {
    return this.feuieoaf4pwu == State.RECORDING;
  }

  public double takeDuration() {
    return this.terrainAddService == null || !this.terrainAddService.finished()
        ? 0.0
        : this.terrainAddService.duration();
  }

  public String status() {
    return switch (this.feuieoaf4pwu.ordinal()) {
      case 1 ->
          String.format(Locale.ROOT, "Recording %.1f / %.0f s", this.mfneikkq5ezo(), this.value);
      case 2 ->
          String.format(
              Locale.ROOT,
              "3D clip ready \u00b7 %.1f s recorded \u00b7 Render Clip to export",
              this.terrainAddService.duration());
      case 3 ->
          "Rendering clip \u00b7 " + this.f5o1tb3mcl5 * 100 / Math.max(1, this.ff3vzzhzdghh) + "%";
      case 4 -> "Finalizing MP4\u2026";
      default -> this.text3;
    };
  }

  public Path lastOutput() {
    return this.path2;
  }

  public RhiBlendStateService.TextureHandle previewTexture() {
    return this.textureHandle;
  }

  public byte[] thumbnailPixels(int n) {
    return this.byte2[n];
  }

  public void clearPreview() {
    if (this.feuieoaf4pwu == State.READY && this.ftvewxbr51x != null && this.fgjcjxehpjfl == null) {
      this.updateState6();
    }
  }

  public void requestPreview(double d) {
    if (this.feuieoaf4pwu == State.READY && Double.isFinite(d)) {
      this.value5 = Math.clamp(d, 0.0, this.terrainAddService.duration());
    }
  }

  public void renderPreview() {
    if (this.feuieoaf4pwu != State.READY
        || !Double.isFinite(this.value5)
        || this.value5 == this.value6 && this.count6 == 20) {
      return;
    }
    try {
      if (this.ftvewxbr51x == null) {
        this.updateState2(960, 540);
      }
      if (this.count6 < 20) {
        double d =
            this.terrainAddService.duration()
                * ((double) this.count6 + Double.longBitsToDouble(4602678819172646912L))
                / Double.longBitsToDouble(0x4034000000000000L);
        RhiBlendStateService.TextureHandle textureHandle = this.createTextureHandle(d);
        RhiBlendStateService.TextureDownload textureDownload =
            RhiDeviceService.device().downloadTexture(textureHandle);
        if (textureDownload != null && textureDownload.complete()) {
          try {
            this.byte2[this.count6] =
                CinematicExporter.downsampleTopDown(
                    textureDownload.pixels(),
                    textureDownload.width(),
                    textureDownload.height(),
                    160,
                    90);
          } finally {
            MemoryUtil.memFree((ByteBuffer) textureDownload.pixels());
          }
        }
        ++this.count6;
      }
      this.textureHandle = this.createTextureHandle(this.value5);
      this.value6 = this.value5;
    } catch (RuntimeException runtimeException) {
      this.updateState6();
      this.mwwk80sgtio("Preview failed: " + runtimeException.getMessage());
    }
  }

  static byte[] downsampleTopDown(ByteBuffer byteBuffer, int n, int n2, int n3, int n4) {
    if (n <= 0 || n2 <= 0 || n3 <= 0 || n4 <= 0) {
      throw new IllegalArgumentException("Invalid thumbnail size");
    }
    byte[] byArray = new byte[n3 * n4 * 4];
    for (int i = 0; i < n4; ++i) {
      int n5 = n2 - 1 - Math.min(n2 - 1, i * n2 / n4);
      for (int j = 0; j < n3; ++j) {
        int n6 = (n5 * n + Math.min(n - 1, j * n / n3)) * 4;
        int n7 = (i * n3 + j) * 4;
        for (int k = 0; k < 4; ++k) {
          byArray[n7 + k] = byteBuffer.get(n6 + k);
        }
      }
    }
    return byArray;
  }

  static void copyTopDown(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int n, int n2) {
    int n3 = n * 4;
    if (n <= 0
        || n2 <= 0
        || (long) byteBuffer.remaining() < (long) n3 * (long) n2
        || (long) byteBuffer2.remaining() < (long) n3 * (long) n2) {
      throw new IllegalArgumentException("Invalid frame buffers");
    }
    for (int i = 0; i < n2; ++i) {
      byteBuffer2.put(byteBuffer.slice((n2 - 1 - i) * n3, n3));
    }
    byteBuffer2.flip();
  }

  private double mfneikkq5ezo() {
    return Math.min(
        this.value,
        Math.max(
            0.0,
            (double) (this.longSupplier.getAsLong() - this.timestamp)
                / Double.longBitsToDouble(4741671816366391296L)));
  }

  public void record(UUID uUID) {
    if (this.recording()) {
      this.mfcfhvo1uqw();
      return;
    }
    if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
      this.mwwk80sgtio("Export is still running");
      return;
    }
    if (this.feuieoaf4pwu == State.READY) {
      this.mwwk80sgtio("Render the current clip first, or use Discard Clip");
      return;
    }
    CompatLoadedHandler.World world = this.compatLoadedHandler.world();
    if (world == null || uUID == null) {
      this.mwwk80sgtio("Join a world before recording");
      return;
    }
    this.updateState7();
    try {
      this.terrainCloseService5 = new FrameCaptureBuffer();
      this.textures2 = this.terrainCloseService5.capture(this.compatLoadedHandler.textures());
      this.terrainAddService = new TerrainAddService(this.value);
      this.uUID = uUID;
      this.object = this.compatLoadedHandler.instanceIdentity();
      this.object2 = this.compatLoadedHandler.resourceIdentity();
      this.text2 = world.dimension();
      this.timestamp = this.longSupplier.getAsLong();
      this.timestamp2 = 0L;
      this.feuieoaf4pwu = State.RECORDING;
      this.mwwk80sgtio(
          String.format(
              Locale.ROOT,
              "Recording up to %.0f seconds of 3D action. Press Record Clip again to stop early.",
              this.value));
    } catch (RuntimeException runtimeException) {
      this.m3unp8jsw1qi(runtimeException);
    }
  }

  public void capture(TerrainStateController terrainStateController) {
    if (!this.recording()) {
      return;
    }
    CompatLoadedHandler.World world = this.compatLoadedHandler.world();
    if (!(world != null
        && Objects.equals(this.object, this.compatLoadedHandler.instanceIdentity())
        && Objects.equals(this.object2, this.compatLoadedHandler.resourceIdentity())
        && this.text2.equals(world.dimension()))) {
      this.mfcfhvo1uqw();
      return;
    }
    long l = this.longSupplier.getAsLong();
    double d = this.mfneikkq5ezo();
    if (this.timestamp2 != 0L && l - this.timestamp2 < 41666666L && d < this.value) {
      return;
    }
    try {
      HashMap<TerrainData, TerrainLayerData> hashMap = new HashMap<TerrainData, TerrainLayerData>();
      for (TerrainLayerData object2 : terrainStateController.meshes()) {
        double d4;
        double iterator = (double) (object2.key().blockX() + 8) - world.player().x;
        if (!(iterator * iterator
                + (d4 = (double) (object2.key().blockZ() + 8) - world.player().z) * d4
            < Double.longBitsToDouble(4668121751257874432L))) continue;
        hashMap.put(object2.key(), object2);
      }
      List<CompatLoadedHandler.MapPlayer> list =
          this.compatLoadedHandler.mapPlayers().stream()
              .filter(
                  mapPlayer ->
                      world.player().distanceSquared(mapPlayer.x(), mapPlayer.y(), mapPlayer.z())
                          <= Double.longBitsToDouble(4657284964654514176L))
              .toList();
      HashMap<UUID, Double> hashMap2 = new HashMap<UUID, Double>();
      Iterator iterator = list.iterator();
      while (iterator.hasNext()) {
        CompatLoadedHandler.MapPlayer mapPlayer2 = (CompatLoadedHandler.MapPlayer) iterator.next();
        Double d2 =
            this.compatLoadedHandler.shadowGround(mapPlayer2.x(), mapPlayer2.z(), mapPlayer2.y());
        if (d2 == null) continue;
        hashMap2.put(mapPlayer2.id(), d2);
      }
      this.terrainAddService.add(
          new TerrainAddService.Sample(
              this.terrainAddService.size() == 0 ? 0.0 : d,
              world.player(),
              this.terrainCloseService5.capture(list),
              hashMap,
              "minecraft:overworld".equals(this.text2) ? this.compatLoadedHandler.skyState() : null,
              hashMap2));
      this.timestamp2 = l;
      if (d >= this.value) {
        this.mfcfhvo1uqw();
      }
    } catch (RuntimeException runtimeException) {
      this.m3unp8jsw1qi(runtimeException);
    }
  }

  public void stopRecording() {
    if (this.recording()) {
      this.mfcfhvo1uqw();
    }
  }

  private void mfcfhvo1uqw() {
    if (this.terrainAddService == null || this.terrainAddService.size() == 0) {
      this.updateState7();
      this.feuieoaf4pwu = State.EMPTY;
      this.mwwk80sgtio("No 3D frames recorded");
      return;
    }
    this.terrainAddService.finish(this.mfneikkq5ezo());
    this.feuieoaf4pwu = State.READY;
    this.mwwk80sgtio("3D clip ready. Use Render Clip to create the cinematic MP4.");
  }

  public void export() {
    ExportRange exportRange;
    if (this.feuieoaf4pwu != State.READY) {
      this.mwwk80sgtio("Record a clip before exporting");
      return;
    }
    try {
      exportRange =
          CinematicExporter.exportRange(
              this.terrainAddService.duration(), this.value2, this.value3);
    } catch (IllegalArgumentException illegalArgumentException) {
      this.mwwk80sgtio("Trim removes the whole clip. Reduce Trim Start or Trim End.");
      return;
    }
    this.f9qmimnjyxxd = exportRange.start();
    this.ff3vzzhzdghh = exportRange.frames();
    try {
      this.updateState6();
      this.updateState2(this.fcetdxsu6qqn, this.count2);
      this.f5o1tb3mcl5 = 0;
      Path path =
          this.path.resolve(
              "ellice/clips/clip-"
                  + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                  + "-"
                  + UUID.randomUUID().toString().substring(0, 8)
                  + ".mp4");
      this.fgjcjxehpjfl =
          new MediaReleaseTracker(path, this.fcetdxsu6qqn, this.count2, 24, this.count3 * 1000000);
      this.feuieoaf4pwu = State.EXPORTING;
      this.mwwk80sgtio("Rendering cinematic clip at " + this.count2 + "p / 24 FPS");
    } catch (RuntimeException runtimeException) {
      this.mh891h08v4vc(runtimeException);
    }
  }

  private void updateState2(int n, int n2) {
    this.ftvewxbr51x = new TerrainMapRenderer(this.renderer);
    this.ffpohaogpe2o = this.ftvewxbr51x.createTarget();
    this.ftvewxbr51x.setShadowSize(4096);
    this.ffpohaogpe2o.view().resizeForExport(n, n2);
    this.terrainCloseService =
        new TerrainCloseService(this.renderer, this.ffpohaogpe2o.view().scene());
    this.f8n9322wwsz = new PlayerShadowRenderer(this.renderer, this.ffpohaogpe2o.view().scene());
    this.terrainFarForService = new TerrainFarForService(this.terrainAddService, this.uUID);
    this.entries = Map.of();
    this.fjmpsvhz5yhu = new SkyTextureRenderer(this.renderer);
  }

  public void render() {
    if (this.feuieoaf4pwu != State.EXPORTING && this.feuieoaf4pwu != State.FINISHING) {
      return;
    }
    try {
      ByteBuffer byteBuffer;
      block12:
      {
        if (this.fgjcjxehpjfl.result().isDone()) {
          this.path2 = this.fgjcjxehpjfl.result().join();
          this.updateState6();
          this.updateState7();
          this.feuieoaf4pwu = State.EMPTY;
          this.mwwk80sgtio("Video saved: " + String.valueOf(this.path2));
          return;
        }
        if (this.feuieoaf4pwu == State.FINISHING || !this.fgjcjxehpjfl.ready()) {
          return;
        }
        double d =
            this.f9qmimnjyxxd
                + (double) this.f5o1tb3mcl5 / Double.longBitsToDouble(4627448617123184640L);
        double d2 = Double.longBitsToDouble(4572654821656843605L);
        ByteBuffer byteBuffer2 = null;
        byteBuffer = null;
        try {
          byteBuffer2 = this.m66c2g912vw2(Math.max(this.f9qmimnjyxxd, d - d2));
          byteBuffer = this.m66c2g912vw2(Math.min(this.terrainAddService.duration(), d + d2));
          CinematicExporter.averageFrames(byteBuffer2, byteBuffer);
          if (this.fgjcjxehpjfl.offer(byteBuffer2)) {
            byteBuffer2 = null;
            ++this.f5o1tb3mcl5;
          }
          if (byteBuffer2 == null) break block12;
        } catch (Throwable throwable) {
          if (byteBuffer2 != null) {
            MemoryUtil.memFree(byteBuffer2);
          }
          if (byteBuffer != null) {
            MemoryUtil.memFree(byteBuffer);
          }
          throw throwable;
        }
        MemoryUtil.memFree((ByteBuffer) byteBuffer2);
      }
      if (byteBuffer != null) {
        MemoryUtil.memFree((ByteBuffer) byteBuffer);
      }
      if (this.f5o1tb3mcl5 == this.ff3vzzhzdghh) {
        this.fgjcjxehpjfl.finish();
        this.feuieoaf4pwu = State.FINISHING;
      }
    } catch (RuntimeException runtimeException) {
      this.mh891h08v4vc(runtimeException);
    }
  }

  static void averageFrames(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
    int n = byteBuffer.remaining();
    for (int i = 0; i < n; ++i) {
      byteBuffer.put(
          i,
          (byte)
              (Byte.toUnsignedInt(byteBuffer.get(i)) + Byte.toUnsignedInt(byteBuffer2.get(i)) + 1
                  >>> 1));
    }
  }

  private ByteBuffer m66c2g912vw2(double d) {
    RhiBlendStateService.TextureHandle textureHandle = this.createTextureHandle(d);
    RhiBlendStateService.TextureDownload textureDownload =
        RhiDeviceService.device().downloadTexture(textureHandle);
    if (textureDownload == null || !textureDownload.complete()) {
      throw new IllegalStateException("Video frame readback failed");
    }
    try {
      if (textureDownload.width() != this.fcetdxsu6qqn || textureDownload.height() != this.count2) {
        throw new IllegalStateException("Video render target has an unexpected resolution");
      }
      ByteBuffer byteBuffer = MemoryUtil.memAlloc((int) (this.fcetdxsu6qqn * this.count2 * 4));
      CinematicExporter.copyTopDown(
          textureDownload.pixels(), byteBuffer, this.fcetdxsu6qqn, this.count2);
      ByteBuffer byteBuffer2 = byteBuffer;
      return byteBuffer2;
    } finally {
      MemoryUtil.memFree((ByteBuffer) textureDownload.pixels());
    }
  }

  private RhiBlendStateService.TextureHandle createTextureHandle(double d) {
    TerrainAddService.Sample sample = this.terrainAddService.at(d);
    this.ftvewxbr51x.recordedTerrain(this.entries, sample.terrain());
    this.entries = sample.terrain();
    int n = this.ffpohaogpe2o.view().width();
    int n2 = this.ffpohaogpe2o.view().height();
    TerrainViewportService.Frame frame = this.terrainFarForService.frame(d, n, n2);
    boolean bl = "minecraft:overworld".equals(this.text2);
    CompatLoadedHandler.SkyState skyState =
        bl
            ? TerrainSunDirectionService.cinematicSky(d, this.terrainAddService.duration())
            : sample.sky();
    Vector3f vector3f =
        skyState == null ? TerrainSunDirectionService.DEFAULT_SUN : skyState.sunDirection();
    float f = skyState == null ? 1.0f : skyState.daylight();
    this.ffpohaogpe2o.view().scene().sun(TerrainSunDirectionService.mapSun(vector3f, f));
    this.ffpohaogpe2o
        .view()
        .scene()
        .ambient(
            TerrainSunDirectionService.mapAmbientColor(f),
            TerrainSunDirectionService.mapAmbientIntensity(f));
    this.terrainCloseService.updateRecorded(
        sample.players(),
        frame,
        1L + Math.round(d * Double.longBitsToDouble(4741671816366391296L)));
    this.f8n9322wwsz.update(
        sample.players(),
        this.ffpohaogpe2o.view().scene().sun().direction(),
        frame,
        this.terrainCloseService,
        sample.ground());
    this.ftvewxbr51x.render(
        this.ffpohaogpe2o,
        frame,
        this.textures2,
        skyState,
        this.terrainCloseService.casters(),
        "minecraft:overworld".equals(this.text2),
        d);
    RhiBlendStateService.TextureHandle textureHandle = this.ffpohaogpe2o.texture();
    if (this.fjmpsvhz5yhu != null) {
      textureHandle =
          this.fjmpsvhz5yhu.render(
              textureHandle,
              this.ffpohaogpe2o.view().depth(),
              n,
              n2,
              Float.intBitsToFloat(1050253722),
              TerrainFarForService.farFor(frame.distance()),
              (float) frame.distance(),
              d);
    }
    return textureHandle;
  }

  public void discard() {
    this.updateState6();
    this.updateState7();
    this.feuieoaf4pwu = State.EMPTY;
    this.mwwk80sgtio("Clip discarded");
  }

  private void mwwk80sgtio(String string) {
    this.text3 = string;
    this.text.accept(string);
  }

  private void m3unp8jsw1qi(RuntimeException runtimeException) {
    this.updateState6();
    this.updateState7();
    this.feuieoaf4pwu = State.EMPTY;
    this.mwwk80sgtio("Clip failed: " + runtimeException.getMessage());
  }

  private void mh891h08v4vc(RuntimeException runtimeException) {
    this.updateState6();
    this.feuieoaf4pwu = State.READY;
    this.mwwk80sgtio("Export failed; recording kept for retry: " + runtimeException.getMessage());
  }

  private void updateState6() {
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.value5 = this.value6 = Double.longBitsToDouble(9221120237041090560L);
    Arrays.fill((Object[]) this.byte2, null);
    this.count6 = 0;
    if (this.fgjcjxehpjfl != null) {
      this.fgjcjxehpjfl.close();
    }
    this.fgjcjxehpjfl = null;
    if (this.terrainCloseService != null) {
      this.terrainCloseService.close();
    }
    this.terrainCloseService = null;
    if (this.f8n9322wwsz != null) {
      this.f8n9322wwsz.close();
    }
    this.f8n9322wwsz = null;
    if (this.ftvewxbr51x != null) {
      if (this.ffpohaogpe2o != null) {
        this.ftvewxbr51x.destroyTarget(this.ffpohaogpe2o);
      }
      this.ftvewxbr51x.close();
    }
    this.ftvewxbr51x = null;
    this.ffpohaogpe2o = null;
    this.terrainFarForService = null;
    this.entries = Map.of();
    if (this.fjmpsvhz5yhu != null) {
      this.fjmpsvhz5yhu.close();
    }
    this.fjmpsvhz5yhu = null;
  }

  private void updateState7() {
    if (this.terrainCloseService5 != null) {
      this.terrainCloseService5.close();
    }
    this.terrainCloseService5 = null;
    this.terrainAddService = null;
    this.textures2 = null;
  }

  @Override
  public void close() {
    this.updateState6();
    this.updateState7();
    this.feuieoaf4pwu = State.EMPTY;
  }

  record ExportRange(double start, double duration, int frames) {}

  public static enum State {
    EMPTY,
    RECORDING,
    READY,
    EXPORTING,
    FINISHING;
  }
}
