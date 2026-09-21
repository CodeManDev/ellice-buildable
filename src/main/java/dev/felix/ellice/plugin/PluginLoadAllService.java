package dev.felix.ellice.plugin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class PluginLoadAllService {
  private static final Set<String> text =
      Set.of("tabgui", "keybinds", "home", "clickgui", "marketplace", "chat");
  private final Path path;
  private final List<PluginRepository> items = new ArrayList<PluginRepository>();
  private WatchService watchService;

  public PluginLoadAllService(Path path) {
    this.path = path.resolve("ellice-plugins");
  }

  public void loadAll() {
    try {
      Files.createDirectories(this.path, new FileAttribute[0]);
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to create plugin directory", (Throwable) iOException);
      return;
    }
    this.updateState(this.path);
    CoreIsInitializedHandler.LOGGER.info(
        "Loaded {} non-visual plugin(s)", (Object) this.items.size());
    this.updateState3();
  }

  private void updateState(Path path2) {
    if (!Files.isDirectory(path2, new LinkOption[0])) {
      return;
    }
    try (DirectoryStream<Path> directoryStream =
        Files.newDirectoryStream(path2, path -> Files.isDirectory(path, new LinkOption[0])); ) {
      for (Path path3 : directoryStream) {
        PluginRepository pluginRepository = PluginRepository.fromDirectory(path3);
        if (pluginRepository == null || PluginLoadAllService.checkCondition(pluginRepository))
          continue;
        pluginRepository.load();
        this.items.add(pluginRepository);
        pluginRepository.enable();
        CoreIsInitializedHandler.LOGGER.info(
            "Loaded user plugin: {} v{}",
            (Object) pluginRepository.meta().name(),
            (Object) pluginRepository.meta().version());
      }
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to scan plugin directory: {}", (Object) path2, (Object) iOException);
    }
  }

  public void onKey(int n, int n2) {
    for (PluginRepository pluginRepository : this.items) {
      if (!pluginRepository.isEnabled()) continue;
      pluginRepository.onKey(n, n2);
    }
  }

  public void fireEvent(String string) {
    for (PluginRepository pluginRepository : this.items) {
      if (!pluginRepository.isEnabled()) continue;
      pluginRepository.fireEvent(string);
    }
  }

  public void pollHotReload() {
    WatchKey watchKey;
    if (this.watchService == null) {
      return;
    }
    while ((watchKey = this.watchService.poll()) != null) {
      Path path = (Path) watchKey.watchable();
      int n = 0;
      for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
        Path path2;
        if (watchEvent.kind() == StandardWatchEventKinds.OVERFLOW
            || !(path2 = (Path) watchEvent.context()).getFileName().toString().endsWith(".lua"))
          continue;
        n = 1;
      }
      if (n != 0) {
        for (PluginRepository pluginRepository : new ArrayList<PluginRepository>(this.items)) {
          if (!pluginRepository.directory().equals(path)) continue;
          CoreIsInitializedHandler.LOGGER.info(
              "Hot-reload (Lua automation) \u2192 {}", (Object) pluginRepository.meta().id());
          this.updateState2(pluginRepository);
          break;
        }
      }
      if (watchKey.reset()) continue;
      CoreIsInitializedHandler.LOGGER.debug("Plugin watcher stopped for {}", (Object) path);
    }
  }

  private void updateState2(PluginRepository pluginRepository) {
    try {
      pluginRepository.disable();
      pluginRepository.unload();
      pluginRepository.load();
      pluginRepository.enable();
      CoreIsInitializedHandler.LOGGER.info(
          "Plugin reloaded: {}", (Object) pluginRepository.meta().id());
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to reload plugin: {}", (Object) pluginRepository.meta().id(), (Object) exception);
    }
  }

  private void updateState3() {
    try {
      this.watchService = FileSystems.getDefault().newWatchService();
      for (PluginRepository pluginRepository : this.items) {
        this.mevyyizddy9x(pluginRepository.directory());
      }
      CoreIsInitializedHandler.LOGGER.info(
          "Plugin hot-reload watcher started (watching {} Lua automation dirs)",
          (Object) this.items.size());
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Failed to start plugin hot-reload watcher", (Throwable) iOException);
    }
  }

  private void mevyyizddy9x(Path path) throws IOException {
    path.register(
        this.watchService,
        StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_CREATE);
  }

  public boolean loadNewPlugin(String string) {
    Path path = this.path.resolve(string);
    if (!Files.isDirectory(path, new LinkOption[0])) {
      return false;
    }
    for (PluginRepository pluginRepository : this.items) {
      if (!pluginRepository.meta().id().equals(string)) continue;
      return true;
    }
    PluginRepository pluginRepository = PluginRepository.fromDirectory(path);
    if (pluginRepository == null || PluginLoadAllService.checkCondition(pluginRepository)) {
      return false;
    }
    pluginRepository.load();
    this.items.add(pluginRepository);
    pluginRepository.enable();
    CoreIsInitializedHandler.LOGGER.info(
        "Hot-loaded plugin: {} v{}",
        (Object) pluginRepository.meta().name(),
        (Object) pluginRepository.meta().version());
    if (this.watchService != null) {
      try {
        this.mevyyizddy9x(path);
      } catch (IOException iOException) {
        CoreIsInitializedHandler.LOGGER.warn(
            "Failed to watch hot-loaded plugin: {}", (Object) string, (Object) iOException);
      }
    }
    return true;
  }

  public boolean unloadPlugin(String string) {
    PluginRepository pluginRepository = null;
    for (PluginRepository object : this.items) {
      if (!object.meta().id().equals(string)) continue;
      pluginRepository = object;
      break;
    }
    if (pluginRepository == null) {
      return false;
    }
    pluginRepository.disable();
    pluginRepository.unload();
    this.items.remove(pluginRepository);
    Path path2 = pluginRepository.directory();
    try (Stream<Path> iOException = Files.walk(path2, new FileVisitOption[0]); ) {
      iOException
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (Exception exception) {

                }
              });
    } catch (IOException iOException2) {
      CoreIsInitializedHandler.LOGGER.warn("Failed to delete plugin dir: {}", (Object) string);
    }
    CoreIsInitializedHandler.LOGGER.info("Uninstalled plugin: {}", (Object) string);
    return true;
  }

  public List<PluginRepository> plugins() {
    return Collections.unmodifiableList(this.items);
  }

  private static boolean checkCondition(PluginRepository pluginRepository) {
    if (!text.contains(pluginRepository.meta().id())) {
      return false;
    }
    CoreIsInitializedHandler.LOGGER.info(
        "Skipping legacy UI plugin: {}", (Object) pluginRepository.meta().id());
    return true;
  }
}
