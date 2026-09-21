package dev.felix.ellice.module;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public final class ModuleRegisterService {
  private final Map<Class<? extends Module>, Module> entries = new LinkedHashMap<>();
  private final Map<String, Module> text = new LinkedHashMap<>();

  public ModuleRegisterService register(Module module) {
    String currentText = module.name().toLowerCase(Locale.ROOT);
    if (this.text.containsKey(currentText)) {
      throw new IllegalArgumentException("Module already registered: " + module.name());
    }

    this.entries.putIfAbsent((Class<? extends Module>) module.getClass(), module);
    this.text.put(currentText, module);
    return this;
  }

  public ModuleRegisterService register(Module... modules) {
    for (Module module : modules) {
      this.register(module);
    }

    return this;
  }

  @SafeVarargs
  public final void discoverAll(String text, Class<? extends Module>... classValues) {
    Set values = Set.of(classValues);
    String currentText = text.replace('.', '/');
    ClassLoader classLoader = this.getClass().getClassLoader();

    try {
      Enumeration enumeration = classLoader.getResources(currentText);

      while (enumeration.hasMoreElements()) {
        URL uRL = (URL) enumeration.nextElement();
        Path path = createPath(uRL, currentText);
        Path currentPath = path.resolve(currentText);
        if (Files.isDirectory(currentPath)) {
          try (Stream<Path> stream = Files.walk(currentPath)) {
            stream
                .filter(item -> item.toString().endsWith(".class"))
                .sorted()
                .forEach(item -> this.updateState(path, item, values));
          }
        }
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Module discovery failed for {}", text, exception);
    }
  }

  private static Path createPath(URL uRL, String path) throws Exception {
    URI uRI = uRL.toURI();
    if ("jar".equals(uRI.getScheme())) {
      String currentPath = uRI.toString().split("!")[0];

      FileSystem fileSystem;
      try {
        fileSystem = FileSystems.getFileSystem(URI.create(currentPath));
      } catch (FileSystemNotFoundException fileSystemNotFoundException) {
        fileSystem = FileSystems.newFileSystem(URI.create(currentPath), Map.of());
      }

      return fileSystem.getPath("/");
    } else {
      Path nextPath = Path.of(uRI);
      return nextPath
          .getRoot()
          .resolve(nextPath.subpath(0, nextPath.getNameCount() - Path.of(path).getNameCount()));
    }
  }

  private void updateState(Path path, Path currentPath, Set<Class<? extends Module>> values) {
    String text =
        path.relativize(currentPath)
            .toString()
            .replace('/', '.')
            .replace('\\', '.')
            .replaceAll("\\.class$", "");

    try {
      Class classValue = Class.forName(text, true, this.getClass().getClassLoader());
      if (values.contains(classValue)) {
        return;
      }

      if (!ModuleSettingsService.class.isAssignableFrom(classValue)) {
        return;
      }

      if (Modifier.isAbstract(classValue.getModifiers())) {
        return;
      }

      this.register((Module) classValue.getDeclaredConstructor().newInstance());
    } catch (ClassNotFoundException classNotFoundException) {
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Failed to instantiate module: {}", text, exception);
    }
  }

  public <T extends Module> Optional<T> get(Class<T> classValue) {
    return Optional.ofNullable((T) this.entries.get(classValue));
  }

  public Optional<Module> get(String currentText) {
    return Optional.ofNullable(this.text.get(currentText.toLowerCase(Locale.ROOT)));
  }

  public boolean unregister(Module module) {
    String currentText = module.name().toLowerCase(Locale.ROOT);
    if (this.text.get(currentText) != module) {
      return false;
    }

    if (module.isEnabled()) {
      module.disable();
    }

    this.text.remove(currentText);
    if (this.entries.get(module.getClass()) == module) {
      this.entries.remove(module.getClass());
      this.text.values().stream()
          .filter(item -> item.getClass() == module.getClass())
          .findFirst()
          .ifPresent(item -> this.entries.put((Class<? extends Module>) module.getClass(), item));
    }

    return true;
  }

  public Stream<Module> stream() {
    return this.text.values().stream();
  }

  public Stream<Module> byCategory(ModuleFeatureType moduleFeatureType) {
    return this.stream().filter(item -> item.category() == moduleFeatureType);
  }

  public Stream<Module> enabled() {
    return this.stream().filter(Module::isEnabled);
  }

  public Collection<Module> all() {
    return Collections.unmodifiableCollection(this.text.values());
  }

  public int size() {
    return this.text.size();
  }

  public void disableAll() {
    this.enabled().forEach(Module::disable);
  }
}
