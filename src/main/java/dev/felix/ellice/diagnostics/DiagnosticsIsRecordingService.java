package dev.felix.ellice.diagnostics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.lwjgl.opengl.GL11;

public final class DiagnosticsIsRecordingService {
  public static final int DEFAULT_TARGET = 240;
  private final DiagnosticsAddFrameListenerService items;
  private final Path path;
  private final Consumer<DiagnosticsAddFrameListenerService.Frame> items2;
  private final List<DiagnosticsAddFrameListenerService.Frame> f4gtofwqtulu;
  private final List<DiagnosticsAddFrameListenerService.Frame> items4;
  private boolean enabled2;
  private int count;
  private long timestamp;
  private String text;
  private String text2;
  private String text3;
  private boolean enabled3;

  public DiagnosticsIsRecordingService(
      final DiagnosticsAddFrameListenerService items, final Path path) {
    this.items2 = this::updateState;
    this.f4gtofwqtulu = new ArrayList<DiagnosticsAddFrameListenerService.Frame>();
    this.items4 = new ArrayList<DiagnosticsAddFrameListenerService.Frame>();
    this.count = 240;
    this.text = "?";
    this.text2 = "?";
    this.text3 = "?";
    this.items = items;
    this.path = path.resolve("ellice-reports");
  }

  public boolean isRecording() {
    return this.enabled2;
  }

  public int framesCaptured() {
    return this.f4gtofwqtulu.size();
  }

  public int framesTarget() {
    return this.count;
  }

  public boolean start() {
    return this.start(240);
  }

  public boolean start(final int b) {
    if (this.enabled2) {
      return false;
    }
    this.count = Math.max(60, b);
    this.f4gtofwqtulu.clear();
    this.items4.clear();
    final int filled = this.items.filled();
    final int head = this.items.head();
    final DiagnosticsAddFrameListenerService.Frame[] history = this.items.history();
    for (int i = filled; i > 0; --i) {
      this.items4.add(collectValues(history[(head - i + 240) % 240]));
    }
    this.timestamp = System.nanoTime();
    this.enabled2 = true;
    this.items.addFrameListener(this.items2);
    CoreIsInitializedHandler.LOGGER.info(
        "PerfRecorder: started ({} pre-buffer, capturing {} frames)",
        (Object) this.items4.size(),
        (Object) this.count);
    return true;
  }

  public void cancel() {
    if (!this.enabled2) {
      return;
    }
    this.enabled2 = false;
    this.items.removeFrameListener(this.items2);
    this.f4gtofwqtulu.clear();
    this.items4.clear();
  }

  private void updateState(final DiagnosticsAddFrameListenerService.Frame frame) {
    if (!this.enabled2) {
      return;
    }
    this.f4gtofwqtulu.add(collectValues(frame));
    if (this.f4gtofwqtulu.size() >= this.count) {
      this.enabled2 = false;
      this.items.removeFrameListener(this.items2);
      try {
        final Path path = this.path();
        updateState2("Perf report saved", path.getFileName().toString());
        CoreIsInitializedHandler.LOGGER.info(
            "PerfRecorder: wrote {}", (Object) path.toAbsolutePath());
      } catch (final Exception ex) {
        CoreIsInitializedHandler.LOGGER.error("PerfRecorder: write failed", (Throwable) ex);
        updateState2("Perf report failed", ex.getClass().getSimpleName());
      }
    }
  }

  private static DiagnosticsAddFrameListenerService.Frame collectValues(
      final DiagnosticsAddFrameListenerService.Frame frame) {
    final DiagnosticsAddFrameListenerService.Frame frame2 =
        new DiagnosticsAddFrameListenerService.Frame();
    frame2.totalNanos = frame.totalNanos;
    frame2.sections.putAll(frame.sections);
    return frame2;
  }

  public void captureGlInfoIfNeeded() {
    if (this.enabled3) {
      return;
    }
    try {
      this.text = GL11.glGetString(7936);
      this.text2 = GL11.glGetString(7937);
      this.text3 = GL11.glGetString(7938);
      this.enabled3 = true;
    } catch (final Throwable t) {
    }
  }

  private Path path() throws Exception {
    Files.createDirectories(this.path, (FileAttribute<?>[]) new FileAttribute[0]);
    final Path resolve =
        this.path.resolve(
            "perf-"
                + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now())
                + ".json");
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("format_version", (Number) 1);
    jsonObject.addProperty("captured_at", Instant.now().toString());
    jsonObject.addProperty("capture_duration_ns", (Number) (System.nanoTime() - this.timestamp));
    jsonObject.add("system", (JsonElement) this.myswltaxagj());
    jsonObject.add("context", (JsonElement) this.createJsonObject2());
    jsonObject.add("plugins", (JsonElement) this.createJsonArray());
    jsonObject.add("modules", (JsonElement) this.createJsonArray2());
    jsonObject.add("sections", (JsonElement) this.createJsonArray3());
    jsonObject.add("pre_buffer", (JsonElement) this.createJsonArray4(this.items4));
    jsonObject.add("frames", (JsonElement) this.createJsonArray4(this.f4gtofwqtulu));
    jsonObject.add("summary", (JsonElement) this.metrgunebqlq());
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
    jsonObject.addProperty("cpu_cores", (Number) Runtime.getRuntime().availableProcessors());
    jsonObject.addProperty("heap_max_mib", (Number) (Runtime.getRuntime().maxMemory() / 1048576L));
    jsonObject.addProperty(
        "heap_used_mib",
        (Number)
            ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L));
    jsonObject.addProperty("gl_vendor", this.text);
    jsonObject.addProperty("gl_renderer", this.text2);
    jsonObject.addProperty("gl_version", this.text3);
    try {
      final Minecraft instance = Minecraft.getInstance();
      final FramebufferInfo mainFramebuffer = CompatAdapterService.mainFramebuffer(instance);
      jsonObject.addProperty("mc_version", instance.getLaunchedVersion());
      jsonObject.addProperty("fb_width", (Number) mainFramebuffer.width());
      jsonObject.addProperty("fb_height", (Number) mainFramebuffer.height());
      jsonObject.addProperty("gui_scale", (Number) instance.getWindow().getGuiScale());
      jsonObject.addProperty("fps_limit", (Number) instance.options.framerateLimit().get());
      jsonObject.addProperty("vsync", (Boolean) instance.options.enableVsync().get());
    } catch (final Throwable t) {
    }
    jsonObject.addProperty("ellice_version", CoreIsInitializedHandler.VERSION);
    return jsonObject;
  }

  private JsonObject createJsonObject2() {
    final JsonObject jsonObject = new JsonObject();
    try {
      final Minecraft instance = Minecraft.getInstance();
      jsonObject.addProperty(
          "current_screen",
          (instance.screen == null) ? "none" : instance.screen.getClass().getSimpleName());
      jsonObject.addProperty("in_game", Boolean.valueOf(instance.level != null));
      try {
        final ClientPacketListener connection = instance.getConnection();
        jsonObject.addProperty(
            "server_brand", (connection != null) ? connection.serverBrand() : "?");
      } catch (final Throwable t) {
        jsonObject.addProperty("server_brand", "?");
      }
      jsonObject.addProperty(
          "scene_node_count",
          (Number) this.mp9lorw7tku(CoreIsInitializedHandler.get().scene().root()));
    } catch (final Throwable t2) {
    }
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

  private JsonArray createJsonArray() {
    final JsonArray jsonArray = new JsonArray();
    try {
      CoreIsInitializedHandler.get().pluginLoader();
      final Path resolve = FabricLoader.getInstance().getGameDir().resolve("ellice-plugins");
      if (Files.isDirectory(resolve, new LinkOption[0])) {
        try (final DirectoryStream<Path> directoryStream =
            Files.newDirectoryStream(resolve, path -> Files.isDirectory(path, new LinkOption[0]))) {
          final Iterator<Path> iterator = directoryStream.iterator();
          while (iterator.hasNext()) {
            final Path resolve2 = iterator.next().resolve("plugin.json");
            if (!Files.exists(resolve2, new LinkOption[0])) {
              continue;
            }
            jsonArray.add(
                (JsonElement) JsonParser.parseString(Files.readString(resolve2)).getAsJsonObject());
          }
        }
      }
    } catch (final Throwable t2) {
    }
    return jsonArray;
  }

  private JsonArray createJsonArray2() {
    final JsonArray jsonArray = new JsonArray();
    try {
      for (final Module module : CoreIsInitializedHandler.get().modules().all()) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", module.name());
        jsonObject.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
        jsonArray.add((JsonElement) jsonObject);
      }
    } catch (final Throwable t) {
    }
    return jsonArray;
  }

  private JsonArray createJsonArray3() {
    final JsonArray jsonArray = new JsonArray();
    final String[] sections = DiagnosticsAddFrameListenerService.SECTIONS;
    for (int length = sections.length, i = 0; i < length; ++i) {
      jsonArray.add((JsonElement) new JsonPrimitive(sections[i]));
    }
    final String[] sections_COMPOSITOR = DiagnosticsAddFrameListenerService.SECTIONS_COMPOSITOR;
    for (int length2 = sections_COMPOSITOR.length, j = 0; j < length2; ++j) {
      jsonArray.add((JsonElement) new JsonPrimitive(sections_COMPOSITOR[j]));
    }
    return jsonArray;
  }

  private JsonArray createJsonArray4(final List<DiagnosticsAddFrameListenerService.Frame> list) {
    final JsonArray jsonArray = new JsonArray();
    for (final DiagnosticsAddFrameListenerService.Frame frame : list) {
      final JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("t_ns", (Number) frame.totalNanos);
      final JsonObject jsonObject2 = new JsonObject();
      for (final Map.Entry entry : frame.sections.entrySet()) {
        jsonObject2.addProperty((String) entry.getKey(), (Number) entry.getValue());
      }
      jsonObject.add("sections", (JsonElement) jsonObject2);
      jsonArray.add((JsonElement) jsonObject);
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
    final double n2 = n / (double) size;
    jsonObject.addProperty("frame_count", (Number) size);
    jsonObject.addProperty("avg_ms", (Number) (n2 / Double.longBitsToDouble(4696837146684686336L)));
    jsonObject.addProperty(
        "avg_fps", (Number) (Double.longBitsToDouble(4741671816366391296L) / n2));
    jsonObject.addProperty(
        "min_ms", (Number) (totalNanos / Double.longBitsToDouble(4696837146684686336L)));
    jsonObject.addProperty(
        "max_ms", (Number) (totalNanos2 / Double.longBitsToDouble(4696837146684686336L)));
    final JsonObject jsonObject2 = new JsonObject();
    for (final Map.Entry entry2 : (Iterable<Map.Entry>) (Iterable<?>) (linkedHashMap.entrySet())) {
      jsonObject2.addProperty(
          (String) entry2.getKey(),
          (Number)
              ((long) entry2.getValue()
                  / (double) size
                  / Double.longBitsToDouble(4696837146684686336L)));
    }
    jsonObject.add("avg_section_ms", (JsonElement) jsonObject2);
    return jsonObject;
  }

  private static void updateState2(final String s, final String s2) {
    try {
      CoreIsInitializedHandler.get().scene().toasts().show(s, s2, Float.intBitsToFloat(1082130432));
    } catch (final Throwable t) {
    }
  }
}
