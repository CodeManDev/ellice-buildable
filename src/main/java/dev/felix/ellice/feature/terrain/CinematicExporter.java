






package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.feature.terrain.FrameCaptureBuffer;
import dev.felix.ellice.feature.terrain.PlayerShadowRenderer;
import dev.felix.ellice.feature.terrain.SkyTextureRenderer;
import dev.felix.ellice.feature.terrain.TerrainAddService;
import dev.felix.ellice.feature.terrain.TerrainCloseService;
import dev.felix.ellice.feature.terrain.TerrainData;
import dev.felix.ellice.feature.terrain.TerrainFarForService;
import dev.felix.ellice.feature.terrain.TerrainLayerData;
import dev.felix.ellice.feature.terrain.TerrainMapRenderer;
import dev.felix.ellice.feature.terrain.TerrainStateController;
import dev.felix.ellice.feature.terrain.TerrainSunDirectionService;
import dev.felix.ellice.feature.terrain.TerrainViewportService;
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

public final class CinematicExporter
implements AutoCloseable {
    private int fcetdxsu6qqn = 2560;
    private int f51uj1duu05o = 1440;
    private int f2tqe6s63p0t = 200;
    private double fhoea53ibejr = Double.longBitsToDouble(0x4034000000000000L);
    private double fgv7j2y5u0co;
    private double fjncvrld5u8a;
    private double f9qmimnjyxxd;
    private int ff3vzzhzdghh;
    private final CompatLoadedHandler fiw4hgj6kgbd;
    private final Render3dSceneService fdpzeihy1mr4;
    private final Path f47ewo5yy99t;
    private final Consumer<String> f1jr9bodpcyl;
    private final LongSupplier f3a7xlho26i2;
    private State feuieoaf4pwu = State.EMPTY;
    private TerrainAddService f55y5ox2mhpq;
    private FrameCaptureBuffer f38mtcuxvvnc;
    private CompatLoadedHandler.Textures f3t616lfvo6v;
    private UUID fgzdi3lhxx06;
    private Object f17glsfqxzfe;
    private Object fcqhqg4erl3o;
    private String fest7x4fvc1k;
    private long f1b8wh6jqe9c;
    private long f8nqagmd96x4;
    private TerrainMapRenderer ftvewxbr51x;
    private TerrainMapRenderer.Target ffpohaogpe2o;
    private TerrainCloseService f5wseeq3gj0m;
    private PlayerShadowRenderer f8n9322wwsz;
    private TerrainFarForService f8hu18u8bqj9;
    private SkyTextureRenderer fjmpsvhz5yhu;
    private MediaReleaseTracker fgjcjxehpjfl;
    private Map<TerrainData, TerrainLayerData> f89wf9nqz5nu = Map.of();
    private int f5o1tb3mcl5;
    private String fgg78c6lhxmx = "";
    private Path f9la63rrk6ff;
    private RhiBlendStateService.TextureHandle fjl3nzy6h50w = RhiBlendStateService.TextureHandle.NONE;
    private double f979h7v231g6 = Double.longBitsToDouble(9221120237041090560L);
    private double fdd3v92yuvkx = Double.longBitsToDouble(9221120237041090560L);
    public static final int THUMBNAIL_COUNT = 20;
    public static final int THUMBNAIL_WIDTH = 160;
    public static final int THUMBNAIL_HEIGHT = 90;
    private final byte[][] f9xuon95dpas = new byte[20][];
    private int fie14n22b29g;
    static final double SHUTTER_ANGLE = 0.5;

    static ExportRange exportRange(double d, double d2, double d3) {
        if (!Double.isFinite(d) || !Double.isFinite(d2) || !Double.isFinite(d3) || d <= 0.0 || d2 < 0.0 || d3 < 0.0) {
            throw new IllegalArgumentException("Invalid clip timing");
        }
        double d4 = Math.min(d2, d);
        double d5 = d - d4 - d3;
        if (d5 < Double.longBitsToDouble(4586165620538955093L)) {
            throw new IllegalArgumentException("Trim removes the whole clip");
        }
        return new ExportRange(d4, d5, (int)Math.ceil(d5 * Double.longBitsToDouble(4627448617123184640L)));
    }

    public void duration(double d) {
        if (this.feuieoaf4pwu == State.RECORDING || this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
            return;
        }
        if (!Double.isFinite(d) || d < 1.0 || d > Double.longBitsToDouble(0x404E000000000000L)) {
            throw new IllegalArgumentException("Unsupported recording duration");
        }
        this.fhoea53ibejr = d;
    }

    public void trim(double d, double d2) {
        if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
            return;
        }
        if (!Double.isFinite(d) || !Double.isFinite(d2) || d < 0.0 || d2 < 0.0) {
            throw new IllegalArgumentException("Invalid clip trim");
        }
        this.fgv7j2y5u0co = d;
        this.fjncvrld5u8a = d2;
    }

    public void bitrateMbps(int n) {
        if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
            return;
        }
        if (n < 25 || n > 400) {
            throw new IllegalArgumentException("Unsupported video bitrate");
        }
        this.f2tqe6s63p0t = n;
    }

    public void resolution(int n) {
        if (this.feuieoaf4pwu == State.EXPORTING || this.feuieoaf4pwu == State.FINISHING) {
            return;
        }
        if (n != 1080 && n != 1440 && n != 2160) {
            throw new IllegalArgumentException("Unsupported clip resolution");
        }
        this.f51uj1duu05o = n;
        this.fcetdxsu6qqn = n * 16 / 9;
    }

    public CinematicExporter(CompatLoadedHandler compatLoadedHandler, Render3dSceneService render3dSceneService, Path path, Consumer<String> consumer) {
        this(compatLoadedHandler, render3dSceneService, path, consumer, System::nanoTime);
    }

    CinematicExporter(CompatLoadedHandler compatLoadedHandler, Render3dSceneService render3dSceneService, Path path, Consumer<String> consumer, LongSupplier longSupplier) {
        this.fiw4hgj6kgbd = compatLoadedHandler;
        this.fdpzeihy1mr4 = render3dSceneService;
        this.f47ewo5yy99t = path;
        this.f1jr9bodpcyl = consumer;
        this.f3a7xlho26i2 = longSupplier;
    }

    public State state() {
        return this.feuieoaf4pwu;
    }

    public boolean recording() {
        return this.feuieoaf4pwu == State.RECORDING;
    }

    public double takeDuration() {
        return this.f55y5ox2mhpq == null || !this.f55y5ox2mhpq.finished() ? 0.0 : this.f55y5ox2mhpq.duration();
    }

    public String status() {
        return switch (this.feuieoaf4pwu.ordinal()) {
            case 1 -> String.format(Locale.ROOT, "Recording %.1f / %.0f s", this.mfneikkq5ezo(), this.fhoea53ibejr);
            case 2 -> String.format(Locale.ROOT, "3D clip ready \u00b7 %.1f s recorded \u00b7 Render Clip to export", this.f55y5ox2mhpq.duration());
            case 3 -> "Rendering clip \u00b7 " + this.f5o1tb3mcl5 * 100 / Math.max(1, this.ff3vzzhzdghh) + "%";
            case 4 -> "Finalizing MP4\u2026";
            default -> this.fgg78c6lhxmx;
        };
    }

    public Path lastOutput() {
        return this.f9la63rrk6ff;
    }

    public RhiBlendStateService.TextureHandle previewTexture() {
        return this.fjl3nzy6h50w;
    }

    public byte[] thumbnailPixels(int n) {
        return this.f9xuon95dpas[n];
    }

    public void clearPreview() {
        if (this.feuieoaf4pwu == State.READY && this.ftvewxbr51x != null && this.fgjcjxehpjfl == null) {
            this.mi5x6nczj60y();
        }
    }

    public void requestPreview(double d) {
        if (this.feuieoaf4pwu == State.READY && Double.isFinite(d)) {
            this.f979h7v231g6 = Math.clamp(d, 0.0, this.f55y5ox2mhpq.duration());
        }
    }

    


    public void renderPreview() {
        if (this.feuieoaf4pwu != State.READY || !Double.isFinite(this.f979h7v231g6) || this.f979h7v231g6 == this.fdd3v92yuvkx && this.fie14n22b29g == 20) {
            return;
        }
        try {
            if (this.ftvewxbr51x == null) {
                this.m52soe1xo9uz(960, 540);
            }
            if (this.fie14n22b29g < 20) {
                double d = this.f55y5ox2mhpq.duration() * ((double)this.fie14n22b29g + Double.longBitsToDouble(4602678819172646912L)) / Double.longBitsToDouble(0x4034000000000000L);
                RhiBlendStateService.TextureHandle textureHandle = this.m9pgovh41om6(d);
                RhiBlendStateService.TextureDownload textureDownload = RhiDeviceService.device().downloadTexture(textureHandle);
                if (textureDownload != null && textureDownload.complete()) {
                    try {
                        this.f9xuon95dpas[this.fie14n22b29g] = CinematicExporter.downsampleTopDown(textureDownload.pixels(), textureDownload.width(), textureDownload.height(), 160, 90);
                    }
                    finally {
                        MemoryUtil.memFree((ByteBuffer)textureDownload.pixels());
                    }
                }
                ++this.fie14n22b29g;
            }
            this.fjl3nzy6h50w = this.m9pgovh41om6(this.f979h7v231g6);
            this.fdd3v92yuvkx = this.f979h7v231g6;
        }
        catch (RuntimeException runtimeException) {
            this.mi5x6nczj60y();
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
        if (n <= 0 || n2 <= 0 || (long)byteBuffer.remaining() < (long)n3 * (long)n2 || (long)byteBuffer2.remaining() < (long)n3 * (long)n2) {
            throw new IllegalArgumentException("Invalid frame buffers");
        }
        for (int i = 0; i < n2; ++i) {
            byteBuffer2.put(byteBuffer.slice((n2 - 1 - i) * n3, n3));
        }
        byteBuffer2.flip();
    }

    private double mfneikkq5ezo() {
        return Math.min(this.fhoea53ibejr, Math.max(0.0, (double)(this.f3a7xlho26i2.getAsLong() - this.f1b8wh6jqe9c) / Double.longBitsToDouble(4741671816366391296L)));
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
        CompatLoadedHandler.World world = this.fiw4hgj6kgbd.world();
        if (world == null || uUID == null) {
            this.mwwk80sgtio("Join a world before recording");
            return;
        }
        this.m5qdhw3hwpt4();
        try {
            this.f38mtcuxvvnc = new FrameCaptureBuffer();
            this.f3t616lfvo6v = this.f38mtcuxvvnc.capture(this.fiw4hgj6kgbd.textures());
            this.f55y5ox2mhpq = new TerrainAddService(this.fhoea53ibejr);
            this.fgzdi3lhxx06 = uUID;
            this.f17glsfqxzfe = this.fiw4hgj6kgbd.instanceIdentity();
            this.fcqhqg4erl3o = this.fiw4hgj6kgbd.resourceIdentity();
            this.fest7x4fvc1k = world.dimension();
            this.f1b8wh6jqe9c = this.f3a7xlho26i2.getAsLong();
            this.f8nqagmd96x4 = 0L;
            this.feuieoaf4pwu = State.RECORDING;
            this.mwwk80sgtio(String.format(Locale.ROOT, "Recording up to %.0f seconds of 3D action. Press Record Clip again to stop early.", this.fhoea53ibejr));
        }
        catch (RuntimeException runtimeException) {
            this.m3unp8jsw1qi(runtimeException);
        }
    }

    public void capture(TerrainStateController terrainStateController) {
        if (!this.recording()) {
            return;
        }
        CompatLoadedHandler.World world = this.fiw4hgj6kgbd.world();
        if (!(world != null && Objects.equals(this.f17glsfqxzfe, this.fiw4hgj6kgbd.instanceIdentity()) && Objects.equals(this.fcqhqg4erl3o, this.fiw4hgj6kgbd.resourceIdentity()) && this.fest7x4fvc1k.equals(world.dimension()))) {
            this.mfcfhvo1uqw();
            return;
        }
        long l = this.f3a7xlho26i2.getAsLong();
        double d = this.mfneikkq5ezo();
        if (this.f8nqagmd96x4 != 0L && l - this.f8nqagmd96x4 < 41666666L && d < this.fhoea53ibejr) {
            return;
        }
        try {
            HashMap<TerrainData, TerrainLayerData> hashMap = new HashMap<TerrainData, TerrainLayerData>();
            for (TerrainLayerData object2 : terrainStateController.meshes()) {
                double d4;
                double iterator = (double)(object2.key().blockX() + 8) - world.player().x;
                if (!(iterator * iterator + (d4 = (double)(object2.key().blockZ() + 8) - world.player().z) * d4 < Double.longBitsToDouble(4668121751257874432L))) continue;
                hashMap.put(object2.key(), object2);
            }
            List<CompatLoadedHandler.MapPlayer> list = this.fiw4hgj6kgbd.mapPlayers().stream().filter(mapPlayer -> world.player().distanceSquared(mapPlayer.x(), mapPlayer.y(), mapPlayer.z()) <= Double.longBitsToDouble(4657284964654514176L)).toList();
            HashMap<UUID, Double> hashMap2 = new HashMap<UUID, Double>();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                CompatLoadedHandler.MapPlayer mapPlayer2 = (CompatLoadedHandler.MapPlayer)iterator.next();
                Double d2 = this.fiw4hgj6kgbd.shadowGround(mapPlayer2.x(), mapPlayer2.z(), mapPlayer2.y());
                if (d2 == null) continue;
                hashMap2.put(mapPlayer2.id(), d2);
            }
            this.f55y5ox2mhpq.add(new TerrainAddService.Sample(this.f55y5ox2mhpq.size() == 0 ? 0.0 : d, world.player(), this.f38mtcuxvvnc.capture(list), hashMap, "minecraft:overworld".equals(this.fest7x4fvc1k) ? this.fiw4hgj6kgbd.skyState() : null, hashMap2));
            this.f8nqagmd96x4 = l;
            if (d >= this.fhoea53ibejr) {
                this.mfcfhvo1uqw();
            }
        }
        catch (RuntimeException runtimeException) {
            this.m3unp8jsw1qi(runtimeException);
        }
    }

    public void stopRecording() {
        if (this.recording()) {
            this.mfcfhvo1uqw();
        }
    }

    private void mfcfhvo1uqw() {
        if (this.f55y5ox2mhpq == null || this.f55y5ox2mhpq.size() == 0) {
            this.m5qdhw3hwpt4();
            this.feuieoaf4pwu = State.EMPTY;
            this.mwwk80sgtio("No 3D frames recorded");
            return;
        }
        this.f55y5ox2mhpq.finish(this.mfneikkq5ezo());
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
            exportRange = CinematicExporter.exportRange(this.f55y5ox2mhpq.duration(), this.fgv7j2y5u0co, this.fjncvrld5u8a);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            this.mwwk80sgtio("Trim removes the whole clip. Reduce Trim Start or Trim End.");
            return;
        }
        this.f9qmimnjyxxd = exportRange.start();
        this.ff3vzzhzdghh = exportRange.frames();
        try {
            this.mi5x6nczj60y();
            this.m52soe1xo9uz(this.fcetdxsu6qqn, this.f51uj1duu05o);
            this.f5o1tb3mcl5 = 0;
            Path path = this.f47ewo5yy99t.resolve("ellice/clips/clip-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "-" + UUID.randomUUID().toString().substring(0, 8) + ".mp4");
            this.fgjcjxehpjfl = new MediaReleaseTracker(path, this.fcetdxsu6qqn, this.f51uj1duu05o, 24, this.f2tqe6s63p0t * 1000000);
            this.feuieoaf4pwu = State.EXPORTING;
            this.mwwk80sgtio("Rendering cinematic clip at " + this.f51uj1duu05o + "p / 24 FPS");
        }
        catch (RuntimeException runtimeException) {
            this.mh891h08v4vc(runtimeException);
        }
    }

    private void m52soe1xo9uz(int n, int n2) {
        this.ftvewxbr51x = new TerrainMapRenderer(this.fdpzeihy1mr4);
        this.ffpohaogpe2o = this.ftvewxbr51x.createTarget();
        this.ftvewxbr51x.setShadowSize(4096);
        this.ffpohaogpe2o.view().resizeForExport(n, n2);
        this.f5wseeq3gj0m = new TerrainCloseService(this.fdpzeihy1mr4, this.ffpohaogpe2o.view().scene());
        this.f8n9322wwsz = new PlayerShadowRenderer(this.fdpzeihy1mr4, this.ffpohaogpe2o.view().scene());
        this.f8hu18u8bqj9 = new TerrainFarForService(this.f55y5ox2mhpq, this.fgzdi3lhxx06);
        this.f89wf9nqz5nu = Map.of();
        this.fjmpsvhz5yhu = new SkyTextureRenderer(this.fdpzeihy1mr4);
    }

    


    public void render() {
        if (this.feuieoaf4pwu != State.EXPORTING && this.feuieoaf4pwu != State.FINISHING) {
            return;
        }
        try {
            ByteBuffer byteBuffer;
            block12: {
                if (this.fgjcjxehpjfl.result().isDone()) {
                    this.f9la63rrk6ff = this.fgjcjxehpjfl.result().join();
                    this.mi5x6nczj60y();
                    this.m5qdhw3hwpt4();
                    this.feuieoaf4pwu = State.EMPTY;
                    this.mwwk80sgtio("Video saved: " + String.valueOf(this.f9la63rrk6ff));
                    return;
                }
                if (this.feuieoaf4pwu == State.FINISHING || !this.fgjcjxehpjfl.ready()) {
                    return;
                }
                double d = this.f9qmimnjyxxd + (double)this.f5o1tb3mcl5 / Double.longBitsToDouble(4627448617123184640L);
                double d2 = Double.longBitsToDouble(4572654821656843605L);
                ByteBuffer byteBuffer2 = null;
                byteBuffer = null;
                try {
                    byteBuffer2 = this.m66c2g912vw2(Math.max(this.f9qmimnjyxxd, d - d2));
                    byteBuffer = this.m66c2g912vw2(Math.min(this.f55y5ox2mhpq.duration(), d + d2));
                    CinematicExporter.averageFrames(byteBuffer2, byteBuffer);
                    if (this.fgjcjxehpjfl.offer(byteBuffer2)) {
                        byteBuffer2 = null;
                        ++this.f5o1tb3mcl5;
                    }
                    if (byteBuffer2 == null) break block12;
                }
                catch (Throwable throwable) {
                    if (byteBuffer2 != null) {
                        MemoryUtil.memFree(byteBuffer2);
                    }
                    if (byteBuffer != null) {
                        MemoryUtil.memFree(byteBuffer);
                    }
                    throw throwable;
                }
                MemoryUtil.memFree((ByteBuffer)byteBuffer2);
            }
            if (byteBuffer != null) {
                MemoryUtil.memFree((ByteBuffer)byteBuffer);
            }
            if (this.f5o1tb3mcl5 == this.ff3vzzhzdghh) {
                this.fgjcjxehpjfl.finish();
                this.feuieoaf4pwu = State.FINISHING;
            }
        }
        catch (RuntimeException runtimeException) {
            this.mh891h08v4vc(runtimeException);
        }
    }

    static void averageFrames(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        int n = byteBuffer.remaining();
        for (int i = 0; i < n; ++i) {
            byteBuffer.put(i, (byte)(Byte.toUnsignedInt(byteBuffer.get(i)) + Byte.toUnsignedInt(byteBuffer2.get(i)) + 1 >>> 1));
        }
    }

    


    private ByteBuffer m66c2g912vw2(double d) {
        RhiBlendStateService.TextureHandle textureHandle = this.m9pgovh41om6(d);
        RhiBlendStateService.TextureDownload textureDownload = RhiDeviceService.device().downloadTexture(textureHandle);
        if (textureDownload == null || !textureDownload.complete()) {
            throw new IllegalStateException("Video frame readback failed");
        }
        try {
            if (textureDownload.width() != this.fcetdxsu6qqn || textureDownload.height() != this.f51uj1duu05o) {
                throw new IllegalStateException("Video render target has an unexpected resolution");
            }
            ByteBuffer byteBuffer = MemoryUtil.memAlloc((int)(this.fcetdxsu6qqn * this.f51uj1duu05o * 4));
            CinematicExporter.copyTopDown(textureDownload.pixels(), byteBuffer, this.fcetdxsu6qqn, this.f51uj1duu05o);
            ByteBuffer byteBuffer2 = byteBuffer;
            return byteBuffer2;
        }
        finally {
            MemoryUtil.memFree((ByteBuffer)textureDownload.pixels());
        }
    }

    private RhiBlendStateService.TextureHandle m9pgovh41om6(double d) {
        TerrainAddService.Sample sample = this.f55y5ox2mhpq.at(d);
        this.ftvewxbr51x.recordedTerrain(this.f89wf9nqz5nu, sample.terrain());
        this.f89wf9nqz5nu = sample.terrain();
        int n = this.ffpohaogpe2o.view().width();
        int n2 = this.ffpohaogpe2o.view().height();
        TerrainViewportService.Frame frame = this.f8hu18u8bqj9.frame(d, n, n2);
        boolean bl = "minecraft:overworld".equals(this.fest7x4fvc1k);
        CompatLoadedHandler.SkyState skyState = bl ? TerrainSunDirectionService.cinematicSky(d, this.f55y5ox2mhpq.duration()) : sample.sky();
        Vector3f vector3f = skyState == null ? TerrainSunDirectionService.DEFAULT_SUN : skyState.sunDirection();
        float f = skyState == null ? 1.0f : skyState.daylight();
        this.ffpohaogpe2o.view().scene().sun(TerrainSunDirectionService.mapSun(vector3f, f));
        this.ffpohaogpe2o.view().scene().ambient(TerrainSunDirectionService.mapAmbientColor(f), TerrainSunDirectionService.mapAmbientIntensity(f));
        this.f5wseeq3gj0m.updateRecorded(sample.players(), frame, 1L + Math.round(d * Double.longBitsToDouble(4741671816366391296L)));
        this.f8n9322wwsz.update(sample.players(), this.ffpohaogpe2o.view().scene().sun().direction(), frame, this.f5wseeq3gj0m, sample.ground());
        this.ftvewxbr51x.render(this.ffpohaogpe2o, frame, this.f3t616lfvo6v, skyState, this.f5wseeq3gj0m.casters(), "minecraft:overworld".equals(this.fest7x4fvc1k), d);
        RhiBlendStateService.TextureHandle textureHandle = this.ffpohaogpe2o.texture();
        if (this.fjmpsvhz5yhu != null) {
            textureHandle = this.fjmpsvhz5yhu.render(textureHandle, this.ffpohaogpe2o.view().depth(), n, n2, Float.intBitsToFloat(1050253722), TerrainFarForService.farFor(frame.distance()), (float)frame.distance(), d);
        }
        return textureHandle;
    }

    public void discard() {
        this.mi5x6nczj60y();
        this.m5qdhw3hwpt4();
        this.feuieoaf4pwu = State.EMPTY;
        this.mwwk80sgtio("Clip discarded");
    }

    private void mwwk80sgtio(String string) {
        this.fgg78c6lhxmx = string;
        this.f1jr9bodpcyl.accept(string);
    }

    private void m3unp8jsw1qi(RuntimeException runtimeException) {
        this.mi5x6nczj60y();
        this.m5qdhw3hwpt4();
        this.feuieoaf4pwu = State.EMPTY;
        this.mwwk80sgtio("Clip failed: " + runtimeException.getMessage());
    }

    private void mh891h08v4vc(RuntimeException runtimeException) {
        this.mi5x6nczj60y();
        this.feuieoaf4pwu = State.READY;
        this.mwwk80sgtio("Export failed; recording kept for retry: " + runtimeException.getMessage());
    }

    private void mi5x6nczj60y() {
        this.fjl3nzy6h50w = RhiBlendStateService.TextureHandle.NONE;
        this.f979h7v231g6 = this.fdd3v92yuvkx = Double.longBitsToDouble(9221120237041090560L);
        Arrays.fill((Object[])this.f9xuon95dpas, null);
        this.fie14n22b29g = 0;
        if (this.fgjcjxehpjfl != null) {
            this.fgjcjxehpjfl.close();
        }
        this.fgjcjxehpjfl = null;
        if (this.f5wseeq3gj0m != null) {
            this.f5wseeq3gj0m.close();
        }
        this.f5wseeq3gj0m = null;
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
        this.f8hu18u8bqj9 = null;
        this.f89wf9nqz5nu = Map.of();
        if (this.fjmpsvhz5yhu != null) {
            this.fjmpsvhz5yhu.close();
        }
        this.fjmpsvhz5yhu = null;
    }

    private void m5qdhw3hwpt4() {
        if (this.f38mtcuxvvnc != null) {
            this.f38mtcuxvvnc.close();
        }
        this.f38mtcuxvvnc = null;
        this.f55y5ox2mhpq = null;
        this.f3t616lfvo6v = null;
    }

    @Override
    public void close() {
        this.mi5x6nczj60y();
        this.m5qdhw3hwpt4();
        this.feuieoaf4pwu = State.EMPTY;
    }

    record ExportRange(double start, double duration, int frames) {
    }

    public static enum State {
        EMPTY,
        RECORDING,
        READY,
        EXPORTING,
        FINISHING;

    }
}

