


package dev.felix.ellice.plugin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.plugin.PluginRepository;
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
    private static final Set<String> f3y7ffnbbycz = Set.of("tabgui", "keybinds", "home", "clickgui", "marketplace", "chat");
    private final Path fd8j0f9hsj7k;
    private final List<PluginRepository> f219o4jdv67i = new ArrayList<PluginRepository>();
    private WatchService f9sun0b3t1io;

    public PluginLoadAllService(Path path) {
        this.fd8j0f9hsj7k = path.resolve("ellice-plugins");
    }

    public void loadAll() {
        try {
            Files.createDirectories(this.fd8j0f9hsj7k, new FileAttribute[0]);
        }
        catch (IOException iOException) {
            CoreIsInitializedHandler.LOGGER.error("Failed to create plugin directory", (Throwable)iOException);
            return;
        }
        this.mcn4g8seo5s6(this.fd8j0f9hsj7k);
        CoreIsInitializedHandler.LOGGER.info("Loaded {} non-visual plugin(s)", (Object)this.f219o4jdv67i.size());
        this.m6yc29k9petr();
    }

    private void mcn4g8seo5s6(Path path2) {
        if (!Files.isDirectory(path2, new LinkOption[0])) {
            return;
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path2, path -> Files.isDirectory(path, new LinkOption[0]));){
            for (Path path3 : directoryStream) {
                PluginRepository pluginRepository = PluginRepository.fromDirectory(path3);
                if (pluginRepository == null || PluginLoadAllService.mion8duv81hp(pluginRepository)) continue;
                pluginRepository.load();
                this.f219o4jdv67i.add(pluginRepository);
                pluginRepository.enable();
                CoreIsInitializedHandler.LOGGER.info("Loaded user plugin: {} v{}", (Object)pluginRepository.meta().name(), (Object)pluginRepository.meta().version());
            }
        }
        catch (IOException iOException) {
            CoreIsInitializedHandler.LOGGER.error("Failed to scan plugin directory: {}", (Object)path2, (Object)iOException);
        }
    }

    public void onKey(int n, int n2) {
        for (PluginRepository pluginRepository : this.f219o4jdv67i) {
            if (!pluginRepository.isEnabled()) continue;
            pluginRepository.onKey(n, n2);
        }
    }

    public void fireEvent(String string) {
        for (PluginRepository pluginRepository : this.f219o4jdv67i) {
            if (!pluginRepository.isEnabled()) continue;
            pluginRepository.fireEvent(string);
        }
    }

    public void pollHotReload() {
        WatchKey watchKey;
        if (this.f9sun0b3t1io == null) {
            return;
        }
        while ((watchKey = this.f9sun0b3t1io.poll()) != null) {
            Path path = (Path)watchKey.watchable();
            int n = 0;
            for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                Path path2;
                if (watchEvent.kind() == StandardWatchEventKinds.OVERFLOW || !(path2 = (Path)watchEvent.context()).getFileName().toString().endsWith(".lua")) continue;
                n = 1;
            }
            if (n != 0) {
                for (PluginRepository pluginRepository : new ArrayList<PluginRepository>(this.f219o4jdv67i)) {
                    if (!pluginRepository.directory().equals(path)) continue;
                    CoreIsInitializedHandler.LOGGER.info("Hot-reload (Lua automation) \u2192 {}", (Object)pluginRepository.meta().id());
                    this.mgty3fbsoj04(pluginRepository);
                    break;
                }
            }
            if (watchKey.reset()) continue;
            CoreIsInitializedHandler.LOGGER.debug("Plugin watcher stopped for {}", (Object)path);
        }
    }

    private void mgty3fbsoj04(PluginRepository pluginRepository) {
        try {
            pluginRepository.disable();
            pluginRepository.unload();
            pluginRepository.load();
            pluginRepository.enable();
            CoreIsInitializedHandler.LOGGER.info("Plugin reloaded: {}", (Object)pluginRepository.meta().id());
        }
        catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.error("Failed to reload plugin: {}", (Object)pluginRepository.meta().id(), (Object)exception);
        }
    }

    private void m6yc29k9petr() {
        try {
            this.f9sun0b3t1io = FileSystems.getDefault().newWatchService();
            for (PluginRepository pluginRepository : this.f219o4jdv67i) {
                this.mevyyizddy9x(pluginRepository.directory());
            }
            CoreIsInitializedHandler.LOGGER.info("Plugin hot-reload watcher started (watching {} Lua automation dirs)", (Object)this.f219o4jdv67i.size());
        }
        catch (IOException iOException) {
            CoreIsInitializedHandler.LOGGER.warn("Failed to start plugin hot-reload watcher", (Throwable)iOException);
        }
    }

    private void mevyyizddy9x(Path path) throws IOException {
        path.register(this.f9sun0b3t1io, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
    }

    public boolean loadNewPlugin(String string) {
        Path path = this.fd8j0f9hsj7k.resolve(string);
        if (!Files.isDirectory(path, new LinkOption[0])) {
            return false;
        }
        for (PluginRepository pluginRepository : this.f219o4jdv67i) {
            if (!pluginRepository.meta().id().equals(string)) continue;
            return true;
        }
        PluginRepository pluginRepository = PluginRepository.fromDirectory(path);
        if (pluginRepository == null || PluginLoadAllService.mion8duv81hp(pluginRepository)) {
            return false;
        }
        pluginRepository.load();
        this.f219o4jdv67i.add(pluginRepository);
        pluginRepository.enable();
        CoreIsInitializedHandler.LOGGER.info("Hot-loaded plugin: {} v{}", (Object)pluginRepository.meta().name(), (Object)pluginRepository.meta().version());
        if (this.f9sun0b3t1io != null) {
            try {
                this.mevyyizddy9x(path);
            }
            catch (IOException iOException) {
                CoreIsInitializedHandler.LOGGER.warn("Failed to watch hot-loaded plugin: {}", (Object)string, (Object)iOException);
            }
        }
        return true;
    }

    public boolean unloadPlugin(String string) {
        PluginRepository pluginRepository = null;
        for (PluginRepository object : this.f219o4jdv67i) {
            if (!object.meta().id().equals(string)) continue;
            pluginRepository = object;
            break;
        }
        if (pluginRepository == null) {
            return false;
        }
        pluginRepository.disable();
        pluginRepository.unload();
        this.f219o4jdv67i.remove(pluginRepository);
        Path path2 = pluginRepository.directory();
        try (Stream<Path> iOException = Files.walk(path2, new FileVisitOption[0]);){
            iOException.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                }
                catch (Exception exception) {
                    
                }
            });
        }
        catch (IOException iOException2) {
            CoreIsInitializedHandler.LOGGER.warn("Failed to delete plugin dir: {}", (Object)string);
        }
        CoreIsInitializedHandler.LOGGER.info("Uninstalled plugin: {}", (Object)string);
        return true;
    }

    public List<PluginRepository> plugins() {
        return Collections.unmodifiableList(this.f219o4jdv67i);
    }

    private static boolean mion8duv81hp(PluginRepository pluginRepository) {
        if (!f3y7ffnbbycz.contains(pluginRepository.meta().id())) {
            return false;
        }
        CoreIsInitializedHandler.LOGGER.info("Skipping legacy UI plugin: {}", (Object)pluginRepository.meta().id());
        return true;
    }
}

