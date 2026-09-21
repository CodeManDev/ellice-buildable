package dev.felix.ellice.plugin;

import com.google.gson.Gson;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import org.luaj.vm2.LuaValue;

public class PluginRepository {
  private final PluginRepository.Meta meta2;
  private final Path path;
  private PluginCallService pluginCallService;
  private boolean enabled2;

  public PluginRepository(PluginRepository.Meta meta, Path currentPath) {
    this.meta2 = meta;
    this.path = currentPath;
  }

  public void load() {
    this.pluginCallService = new PluginCallService(this);
    Path currentPath = this.path.resolve("init.lua");
    if (Files.exists(currentPath)) {
      this.pluginCallService.executeFile(currentPath);
    }
  }

  public void unload() {
    this.pluginCallService = null;
  }

  public void enable() {
    this.enabled2 = true;
    if (this.pluginCallService != null) {
      this.pluginCallService.callIfExists("onEnable");
    }

    CoreIsInitializedHandler.LOGGER.info("Plugin enabled: {}", this.meta2.id);
  }

  public void disable() {
    this.enabled2 = false;
    if (this.pluginCallService != null) {
      this.pluginCallService.callIfExists("onDisable");
    }
  }

  public void onKey(int value, int currentValue) {
    if (this.pluginCallService != null && currentValue == 1) {
      this.pluginCallService.fireEvent("key.press", LuaValue.valueOf(value));

      String text =
          switch (value) {
            case 262 -> "key.right";
            case 263 -> "key.left";
            case 264 -> "key.down";
            case 265 -> "key.up";
            default -> "key." + value;
          };
      this.pluginCallService.fireEvent(text);
    }
  }

  public void fireEvent(String text) {
    if (this.pluginCallService != null) {
      this.pluginCallService.fireEvent(text);
    }
  }

  public void fireEvent(String text, LuaValue luaValue) {
    if (this.pluginCallService != null) {
      this.pluginCallService.fireEvent(text, luaValue);
    }
  }

  public PluginRepository.Meta meta() {
    return this.meta2;
  }

  public Path directory() {
    return this.path;
  }

  public boolean isEnabled() {
    return this.enabled2;
  }

  static PluginRepository fromDirectory(Path path) {
    Path currentPath = path.resolve("plugin.json");
    if (!Files.exists(currentPath)) {
      return null;
    }

    try {
      String text = Files.readString(currentPath);
      PluginRepository.Meta meta =
          (PluginRepository.Meta) new Gson().fromJson(text, PluginRepository.Meta.class);
      return meta != null && meta.id != null ? new PluginRepository(meta, path) : null;
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Failed to load plugin.json from {}", path, exception);
      return null;
    }
  }

  public record Meta(
      String id,
      String name,
      String version,
      String author,
      String description,
      String[] permissions,
      String keybind) {}
}
