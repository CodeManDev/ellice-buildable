



package dev.felix.ellice.diagnostics;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.LinkedHashMap;
import com.google.gson.JsonPrimitive;
import dev.felix.ellice.module.Module;
import java.nio.file.DirectoryStream;
import com.google.gson.JsonParser;
import java.nio.file.LinkOption;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.JsonArray;
import java.util.Iterator;
import net.minecraft.client.multiplayer.ClientPacketListener;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.compat.CompatAdapterService;
import net.minecraft.client.Minecraft;
import java.nio.file.OpenOption;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.temporal.TemporalAccessor;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import org.lwjgl.opengl.GL11;
import java.util.Map;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.nio.file.Path;

public final class DiagnosticsIsRecordingService
{
    public static final int DEFAULT_TARGET = 240;
    private final DiagnosticsAddFrameListenerService f1ifu6iv8lvc;
    private final Path fe42r0caub0k;
    private final Consumer<DiagnosticsAddFrameListenerService.Frame> f5x2swngnu64;
    private final List<DiagnosticsAddFrameListenerService.Frame> f4gtofwqtulu;
    private final List<DiagnosticsAddFrameListenerService.Frame> f92vgj8xt88e;
    private boolean f3n8n86ho2c1;
    private int fcqfs0xw0nhe;
    private long f6xsp0dmp1jy;
    private String femao9d9qzlh;
    private String fdp1ylm53lr0;
    private String f1ocl71aevy5;
    private boolean fdnml06dtobi;
    
    public DiagnosticsIsRecordingService(final DiagnosticsAddFrameListenerService f1ifu6iv8lvc, final Path path) {
        this.f5x2swngnu64 = this::mccm2duw3zx3;
        this.f4gtofwqtulu = new ArrayList<DiagnosticsAddFrameListenerService.Frame>();
        this.f92vgj8xt88e = new ArrayList<DiagnosticsAddFrameListenerService.Frame>();
        this.fcqfs0xw0nhe = 240;
        this.femao9d9qzlh = "?";
        this.fdp1ylm53lr0 = "?";
        this.f1ocl71aevy5 = "?";
        this.f1ifu6iv8lvc = f1ifu6iv8lvc;
        this.fe42r0caub0k = path.resolve("ellice-reports");
    }
    
    public boolean isRecording() {
        return this.f3n8n86ho2c1;
    }
    
    public int framesCaptured() {
        return this.f4gtofwqtulu.size();
    }
    
    public int framesTarget() {
        return this.fcqfs0xw0nhe;
    }
    
    public boolean start() {
        return this.start(240);
    }
    
    public boolean start(final int b) {
        if (this.f3n8n86ho2c1) {
            return false;
        }
        this.fcqfs0xw0nhe = Math.max(60, b);
        this.f4gtofwqtulu.clear();
        this.f92vgj8xt88e.clear();
        final int filled = this.f1ifu6iv8lvc.filled();
        final int head = this.f1ifu6iv8lvc.head();
        final DiagnosticsAddFrameListenerService.Frame[] history = this.f1ifu6iv8lvc.history();
        for (int i = filled; i > 0; --i) {
            this.f92vgj8xt88e.add(maz1fuwzp59q(history[(head - i + 240) % 240]));
        }
        this.f6xsp0dmp1jy = System.nanoTime();
        this.f3n8n86ho2c1 = true;
        this.f1ifu6iv8lvc.addFrameListener(this.f5x2swngnu64);
        CoreIsInitializedHandler.LOGGER.info("PerfRecorder: started ({} pre-buffer, capturing {} frames)", (Object)this.f92vgj8xt88e.size(), (Object)this.fcqfs0xw0nhe);
        return true;
    }
    
    public void cancel() {
        if (!this.f3n8n86ho2c1) {
            return;
        }
        this.f3n8n86ho2c1 = false;
        this.f1ifu6iv8lvc.removeFrameListener(this.f5x2swngnu64);
        this.f4gtofwqtulu.clear();
        this.f92vgj8xt88e.clear();
    }
    
    private void mccm2duw3zx3(final DiagnosticsAddFrameListenerService.Frame frame) {
        if (!this.f3n8n86ho2c1) {
            return;
        }
        this.f4gtofwqtulu.add(maz1fuwzp59q(frame));
        if (this.f4gtofwqtulu.size() >= this.fcqfs0xw0nhe) {
            this.f3n8n86ho2c1 = false;
            this.f1ifu6iv8lvc.removeFrameListener(this.f5x2swngnu64);
            try {
                final Path mbo5xml9lu4c = this.mbo5xml9lu4c();
                mbcr23zafr5e("Perf report saved", mbo5xml9lu4c.getFileName().toString());
                CoreIsInitializedHandler.LOGGER.info("PerfRecorder: wrote {}", (Object)mbo5xml9lu4c.toAbsolutePath());
            }
            catch (final Exception ex) {
                CoreIsInitializedHandler.LOGGER.error("PerfRecorder: write failed", (Throwable)ex);
                mbcr23zafr5e("Perf report failed", ex.getClass().getSimpleName());
            }
        }
    }
    
    private static DiagnosticsAddFrameListenerService.Frame maz1fuwzp59q(final DiagnosticsAddFrameListenerService.Frame frame) {
        final DiagnosticsAddFrameListenerService.Frame frame2 = new DiagnosticsAddFrameListenerService.Frame();
        frame2.totalNanos = frame.totalNanos;
        frame2.sections.putAll(frame.sections);
        return frame2;
    }
    
    public void captureGlInfoIfNeeded() {
        if (this.fdnml06dtobi) {
            return;
        }
        try {
            this.femao9d9qzlh = GL11.glGetString(7936);
            this.fdp1ylm53lr0 = GL11.glGetString(7937);
            this.f1ocl71aevy5 = GL11.glGetString(7938);
            this.fdnml06dtobi = true;
        }
        catch (final Throwable t) {}
    }
    
    private Path mbo5xml9lu4c() throws Exception {
        Files.createDirectories(this.fe42r0caub0k, (FileAttribute<?>[])new FileAttribute[0]);
        final Path resolve = this.fe42r0caub0k.resolve("perf-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.systemDefault()).format(Instant.now()) + ".json");
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("format_version", (Number)1);
        jsonObject.addProperty("captured_at", Instant.now().toString());
        jsonObject.addProperty("capture_duration_ns", (Number)(System.nanoTime() - this.f6xsp0dmp1jy));
        jsonObject.add("system", (JsonElement)this.myswltaxagj());
        jsonObject.add("context", (JsonElement)this.mhm95yhr0nwg());
        jsonObject.add("plugins", (JsonElement)this.md1yh80j1bif());
        jsonObject.add("modules", (JsonElement)this.me78gkdfzzgm());
        jsonObject.add("sections", (JsonElement)this.m39fy1as2qw6());
        jsonObject.add("pre_buffer", (JsonElement)this.mfkp1w118drt(this.f92vgj8xt88e));
        jsonObject.add("frames", (JsonElement)this.mfkp1w118drt(this.f4gtofwqtulu));
        jsonObject.add("summary", (JsonElement)this.metrgunebqlq());
        Files.writeString(resolve, jsonObject.toString(), new OpenOption[0]);
        return resolve;
    }
    
    private JsonObject myswltaxagj() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("os_name", System.getProperty("os.name", "?"));
        jsonObject.addProperty("os_version", System.getProperty("os.version", "?"));
        jsonObject.addProperty("os_arch", System.getProperty("os.arch", "?"));
        jsonObject.addProperty("jvm_name", System.getProperty("java.vm.name", "?"));
        jsonObject.addProperty("jvm_version", System.getProperty("java.version", "?"));
        jsonObject.addProperty("cpu_cores", (Number)Runtime.getRuntime().availableProcessors());
        jsonObject.addProperty("heap_max_mib", (Number)(Runtime.getRuntime().maxMemory() / 1048576L));
        jsonObject.addProperty("heap_used_mib", (Number)((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L));
        jsonObject.addProperty("gl_vendor", this.femao9d9qzlh);
        jsonObject.addProperty("gl_renderer", this.fdp1ylm53lr0);
        jsonObject.addProperty("gl_version", this.f1ocl71aevy5);
        try {
            final Minecraft instance = Minecraft.getInstance();
            final FramebufferInfo mainFramebuffer = CompatAdapterService.mainFramebuffer(instance);
            jsonObject.addProperty("mc_version", instance.getLaunchedVersion());
            jsonObject.addProperty("fb_width", (Number)mainFramebuffer.width());
            jsonObject.addProperty("fb_height", (Number)mainFramebuffer.height());
            jsonObject.addProperty("gui_scale", (Number)instance.getWindow().getGuiScale());
            jsonObject.addProperty("fps_limit", (Number)instance.options.framerateLimit().get());
            jsonObject.addProperty("vsync", (Boolean)instance.options.enableVsync().get());
        }
        catch (final Throwable t) {}
        jsonObject.addProperty("ellice_version", CoreIsInitializedHandler.VERSION);
        return jsonObject;
    }
    
    private JsonObject mhm95yhr0nwg() {
        final JsonObject jsonObject = new JsonObject();
        try {
            final Minecraft instance = Minecraft.getInstance();
            jsonObject.addProperty("current_screen", (instance.screen == null) ? "none" : instance.screen.getClass().getSimpleName());
            jsonObject.addProperty("in_game", Boolean.valueOf(instance.level != null));
            try {
                final ClientPacketListener connection = instance.getConnection();
                jsonObject.addProperty("server_brand", (connection != null) ? connection.serverBrand() : "?");
            }
            catch (final Throwable t) {
                jsonObject.addProperty("server_brand", "?");
            }
            jsonObject.addProperty("scene_node_count", (Number)this.mp9lorw7tku(CoreIsInitializedHandler.get().scene().root()));
        }
        catch (final Throwable t2) {}
        return jsonObject;
    }
    
    private int mp9lorw7tku(final ScenePctService<?> scenePctService) {
        if (scenePctService == null) {
            return 0;
        }
        int n = 1;
        final Iterator<ScenePctService<?>> iterator = scenePctService.children().iterator();
        while (iterator.hasNext()) {
            n += this.mp9lorw7tku(iterator.next());
        }
        return n;
    }
    
    private JsonArray md1yh80j1bif() {
        final JsonArray jsonArray = new JsonArray();
        try {
            CoreIsInitializedHandler.get().pluginLoader();
            final Path resolve = FabricLoader.getInstance().getGameDir().resolve("ellice-plugins");
            if (Files.isDirectory(resolve, new LinkOption[0])) {
                try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resolve, path -> Files.isDirectory(path, new LinkOption[0]))) {
                    final Iterator<Path> iterator = directoryStream.iterator();
                    while (iterator.hasNext()) {
                        final Path resolve2 = iterator.next().resolve("plugin.json");
                        if (!Files.exists(resolve2, new LinkOption[0])) {
                            continue;
                        }
                        jsonArray.add((JsonElement)JsonParser.parseString(Files.readString(resolve2)).getAsJsonObject());
                    }
                }
            }
        }
        catch (final Throwable t2) {}
        return jsonArray;
    }
    
    private JsonArray me78gkdfzzgm() {
        final JsonArray jsonArray = new JsonArray();
        try {
            for (final Module module : CoreIsInitializedHandler.get().modules().all()) {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", module.name());
                jsonObject.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
                jsonArray.add((JsonElement)jsonObject);
            }
        }
        catch (final Throwable t) {}
        return jsonArray;
    }
    
    private JsonArray m39fy1as2qw6() {
        final JsonArray jsonArray = new JsonArray();
        final String[] sections = DiagnosticsAddFrameListenerService.SECTIONS;
        for (int length = sections.length, i = 0; i < length; ++i) {
            jsonArray.add((JsonElement)new JsonPrimitive(sections[i]));
        }
        final String[] sections_COMPOSITOR = DiagnosticsAddFrameListenerService.SECTIONS_COMPOSITOR;
        for (int length2 = sections_COMPOSITOR.length, j = 0; j < length2; ++j) {
            jsonArray.add((JsonElement)new JsonPrimitive(sections_COMPOSITOR[j]));
        }
        return jsonArray;
    }
    
    private JsonArray mfkp1w118drt(final List<DiagnosticsAddFrameListenerService.Frame> list) {
        final JsonArray jsonArray = new JsonArray();
        for (final DiagnosticsAddFrameListenerService.Frame frame : list) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("t_ns", (Number)frame.totalNanos);
            final JsonObject jsonObject2 = new JsonObject();
            for (final Map.Entry entry : frame.sections.entrySet()) {
                jsonObject2.addProperty((String)entry.getKey(), (Number)entry.getValue());
            }
            jsonObject.add("sections", (JsonElement)jsonObject2);
            jsonArray.add((JsonElement)jsonObject);
        }
        return jsonArray;
    }
    
    private JsonObject metrgunebqlq() {
        final JsonObject jsonObject = new JsonObject();
        if (this.f4gtofwqtulu.isEmpty()) {
            return jsonObject;
        }
        long n = 0L;
        final LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<>();
        long totalNanos = Long.MAX_VALUE;
        long totalNanos2 = 0L;
        for (final DiagnosticsAddFrameListenerService.Frame frame : this.f4gtofwqtulu) {
            n += frame.totalNanos;
            if (frame.totalNanos < totalNanos) {
                totalNanos = frame.totalNanos;
            }
            if (frame.totalNanos > totalNanos2) {
                totalNanos2 = frame.totalNanos;
            }
            for (final Map.Entry<String, Long> entry : frame.sections.entrySet()) {
                linkedHashMap.merge(entry.getKey(), entry.getValue(), Long::sum);
            }
        }
        final int size = this.f4gtofwqtulu.size();
        final double n2 = n / (double)size;
        jsonObject.addProperty("frame_count", (Number)size);
        jsonObject.addProperty("avg_ms", (Number)(n2 / Double.longBitsToDouble(4696837146684686336L)));
        jsonObject.addProperty("avg_fps", (Number)(Double.longBitsToDouble(4741671816366391296L) / n2));
        jsonObject.addProperty("min_ms", (Number)(totalNanos / Double.longBitsToDouble(4696837146684686336L)));
        jsonObject.addProperty("max_ms", (Number)(totalNanos2 / Double.longBitsToDouble(4696837146684686336L)));
        final JsonObject jsonObject2 = new JsonObject();
        for (final Map.Entry entry2 : (Iterable<Map.Entry>) (Iterable<?>) (linkedHashMap.entrySet())) {
            jsonObject2.addProperty((String)entry2.getKey(), (Number)((long)entry2.getValue() / (double)size / Double.longBitsToDouble(4696837146684686336L)));
        }
        jsonObject.add("avg_section_ms", (JsonElement)jsonObject2);
        return jsonObject;
    }
    
    private static void mbcr23zafr5e(final String s, final String s2) {
        try {
            CoreIsInitializedHandler.get().scene().toasts().show(s, s2, Float.intBitsToFloat(1082130432));
        }
        catch (final Throwable t) {}
    }
}
