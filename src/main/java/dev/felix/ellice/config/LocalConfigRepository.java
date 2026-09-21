package dev.felix.ellice.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.hud.HudItemsService;
import dev.felix.ellice.hud.layout.LayoutFindSelector;
import dev.felix.ellice.hud.layout.LayoutRepository;
import dev.felix.ellice.module.HudStyleSettings;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleRegisterService;
import dev.felix.ellice.module.ModuleSetting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class LocalConfigRepository {
  private static final String text = "default";
  public static final int CONFIG_SCHEMA_VERSION = 2;
  private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
  private static final Pattern pattern = Pattern.compile("[\\p{L}\\p{N}_. \\-]{1,128}");
  private static volatile boolean enabled2;
  private final Path path;
  private final ModuleRegisterService moduleRegisterService;
  private final HudStyleSettings values;
  private final HudItemsService hudItemsService;
  private String text2 = "default";
  private LayoutFindSelector layoutFindSelector;
  private Path path2;
  private final Map<String, float[]> text3 = new LinkedHashMap<>();

  public static boolean isSavesSuppressed() {
    return enabled2;
  }

  public static void runWithoutSaving(Runnable runnable) {
    if (runnable == null) {
      throw new IllegalArgumentException("action is required");
    }

    boolean enabled = enabled2;
    enabled2 = true;

    try {
      runnable.run();
    } finally {
      enabled2 = enabled;
    }
  }

  public static String normalizeConfigName(String currentName) {
    String nextName = currentName == null ? "" : currentName.trim();
    if (pattern.matcher(nextName).matches()
        && !nextName.equals(".")
        && !nextName.equals("..")
        && !nextName.endsWith(".")) {
      return nextName;
    } else {
      throw new IllegalArgumentException(
          "Use letters, numbers, spaces, - or _. The name cannot end with a dot.");
    }
  }

  public LocalConfigRepository(
      Path currentPath,
      ModuleRegisterService moduleRegister,
      HudStyleSettings hudStyleSettings,
      HudItemsService hudItems) {
    this.path = currentPath.resolve("ellice-config");
    this.moduleRegisterService = moduleRegister;
    this.values = hudStyleSettings;
    this.hudItemsService = hudItems;

    try {
      Files.createDirectories(this.path);
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error("Failed to create config dir", iOException);
    }
  }

  public void save() {
    if (!enabled2) {
      this.saveAs(this.text2);
    }
  }

  public void saveAs(String text) {
    String currentText = normalizeConfigName(text);

    try {
      this.saveProfile(currentText);
      CoreIsInitializedHandler.LOGGER.debug("Config saved: {}", currentText);
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error("Failed to save config: {}", currentText, iOException);
    }
  }

  public JsonObject exportActiveConfig() {
    return this.exportConfig(this.text2);
  }

  public JsonObject exportConfig(String text) {
    String currentText = normalizeConfigName(text);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("version", 2);
    jsonObject.addProperty("name", currentText);
    JsonObject currentJsonObject = new JsonObject();

    for (Module module : this.moduleRegisterService.all()) {
      JsonObject nextJsonObject = new JsonObject();
      nextJsonObject.addProperty("enabled", module.isEnabled());
      JsonObject previousJsonObject = new JsonObject();

      for (ModuleSetting moduleSetting : module.settings()) {
        previousJsonObject.add(moduleSetting.name(), this.createJsonElement(moduleSetting));
      }

      if (!module.settings().isEmpty()) {
        nextJsonObject.add("settings", previousJsonObject);
      }

      currentJsonObject.add(module.name(), nextJsonObject);
    }

    jsonObject.add("modules", currentJsonObject);
    JsonObject sourceJsonObject = new JsonObject();

    for (ModuleSetting currentModuleSetting : this.values.settings()) {
      sourceJsonObject.add(
          currentModuleSetting.name(), this.createJsonElement(currentModuleSetting));
    }

    jsonObject.add("hudStyle", sourceJsonObject);
    jsonObject.add("hudItems", this.hudItemsService.toJson());
    if (CoreIsInitializedHandler.isReady()) {
      jsonObject.add("keybinds", CoreIsInitializedHandler.get().keybinds().toJson());
    }

    JsonObject targetJsonObject = new JsonObject();

    for (Entry entry : this.text3.entrySet()) {
      JsonArray jsonArray = new JsonArray();
      jsonArray.add(((float[]) entry.getValue())[0]);
      jsonArray.add(((float[]) entry.getValue())[1]);
      targetJsonObject.add((String) entry.getKey(), jsonArray);
    }

    jsonObject.add("positions", targetJsonObject);
    if (this.layoutFindSelector != null) {
      jsonObject.add("hudLayout", LayoutRepository.toJson(this.layoutFindSelector));
    }

    return jsonObject;
  }

  public void load() {
    Path currentPath = this.path.resolve(".active-profile");
    if (Files.isRegularFile(currentPath)) {
      try {
        String text =
            normalizeConfigName(
                JsonParser.parseString(Files.readString(currentPath))
                    .getAsJsonObject()
                    .get("name")
                    .getAsString());
        this.loadProfile(text);
        return;
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.warn("Could not restore the active config", exception);
      }
    }

    if (Files.isRegularFile(this.createPath("default"))) {
      try {
        this.loadProfile("default");
        return;
      } catch (IOException iOException) {
        CoreIsInitializedHandler.LOGGER.warn(
            "Could not load the default config; keeping the unreadable file", iOException);
      }
    }

    this.hudItemsService.ensureDefaults();
    String currentText = "default";

    for (int index = 1; Files.exists(this.createPath(currentText)); index++) {
      currentText = "Recovered config " + index;
    }

    try {
      this.createProfile(currentText);
    } catch (IOException currentIOException) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to create the initial config", currentIOException);
    }
  }

  public void loadConfig(String text) {
    String currentText = normalizeConfigName(text);
    Path path = this.createPath(currentText);
    if (!Files.exists(path)) {
      CoreIsInitializedHandler.LOGGER.info("No config found: {}, using defaults", currentText);
      this.text2 = currentText;
      this.hudItemsService.ensureDefaults();
      this.saveAs(currentText);
    } else {
      try {
        this.loadProfile(currentText);
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.error("Failed to load config: {}", currentText, exception);
      }
    }
  }

  public void importConfig(String text, JsonObject jsonObject) {
    this.importConfig(text, jsonObject, true);
  }

  public void importConfig(String text, JsonObject jsonObject, boolean enabled) {
    String currentText = normalizeConfigName(text);
    if (jsonObject == null) {
      throw new IllegalArgumentException("Config JSON is required");
    }

    boolean currentEnabled = enabled2;
    enabled2 = true;

    try {
      this.updateState3(jsonObject);
      this.text2 = currentText;
      CoreIsInitializedHandler.LOGGER.info("Config loaded: {}", currentText);
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Failed to import config: {}", currentText, exception);
      throw new IllegalStateException("Failed to import config: " + currentText, exception);
    } finally {
      enabled2 = currentEnabled;
    }

    if (enabled) {
      this.saveAs(currentText);

      try {
        this.updateState2(currentText);
      } catch (IOException iOException) {
        CoreIsInitializedHandler.LOGGER.error("Failed to remember active config", iOException);
      }
    }
  }

  public List<String> listConfigs() {
    ArrayList<String> arrayList = new ArrayList<>();

    try (Stream<Path> stream = Files.list(this.path)) {
      stream
          .filter(item -> Files.isRegularFile(item) && item.toString().endsWith(".json"))
          .forEach(
              item -> {
                String text = item.getFileName().toString();
                text = text.substring(0, text.length() - 5);
                if (pattern.matcher(text).matches()) {
                  arrayList.add(text);
                }
              });
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error("Failed to list configs", iOException);
    }

    arrayList.sort(String::compareToIgnoreCase);
    return arrayList;
  }

  public boolean deleteConfig(String text) {
    String currentText = normalizeConfigName(text);

    try {
      this.removeProfile(currentText);
      return true;
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to delete config: {}", currentText, iOException);
      return false;
    }
  }

  public String activeConfig() {
    return this.text2;
  }

  public void bindHudLayout(LayoutFindSelector currentLayoutFindSelector, Path path) {
    this.layoutFindSelector = currentLayoutFindSelector;
    this.path2 = path;
  }

  public void saveProfile(String path) throws IOException {
    String currentPath = normalizeConfigName(path);
    this.updateState4(this.createPath(currentPath), this.exportConfig(currentPath));
  }

  public void createProfile(String path) throws IOException {
    String currentPath = this.createText(path);
    this.saveProfile(currentPath);

    try {
      this.updateState2(currentPath);
    } catch (IOException iOException) {
      Files.deleteIfExists(this.createPath(currentPath));
      throw iOException;
    }

    this.text2 = currentPath;
  }

  public String importProfile(Path path, String currentPath) throws IOException {
    String nextPath = this.createText(currentPath);
    JsonObject jsonObject = this.createJsonObject2(path);
    jsonObject.addProperty("name", nextPath);
    this.updateState(this.createPath(nextPath), jsonObject);
    return nextPath;
  }

  public void exportProfile(String path, Path currentPath) throws IOException {
    String nextPath = normalizeConfigName(path);
    JsonObject jsonObject =
        nextPath.equals(this.text2) ? this.exportConfig(nextPath) : this.createJsonObject(nextPath);
    jsonObject.addProperty("name", nextPath);
    this.updateState(currentPath, jsonObject);
  }

  private void updateState(Path path, JsonObject jsonObject) throws IOException {
    Path currentPath = path.toAbsolutePath().normalize();
    if (Files.exists(currentPath)) {
      throw new IOException("This file already exists. Choose a different name.");
    }

    Path nextPath = Files.createTempFile(currentPath.getParent(), ".ellice-export-", ".tmp");

    try {
      Files.writeString(nextPath, gson2.toJson(jsonObject), StandardCharsets.UTF_8);
      Files.move(nextPath, currentPath);
    } finally {
      Files.deleteIfExists(nextPath);
    }
  }

  public void loadProfile(String path) throws IOException {
    String currentPath = normalizeConfigName(path);
    JsonObject jsonObject = this.createJsonObject(currentPath);
    JsonObject currentJsonObject = this.exportActiveConfig();

    try {
      runWithoutSaving(() -> this.updateState3(jsonObject));
      this.updateState2(currentPath);
      this.text2 = currentPath;
    } catch (Exception exception) {
      try {
        runWithoutSaving(() -> this.updateState3(currentJsonObject));
      } catch (Exception currentException) {
        exception.addSuppressed(currentException);
      }

      throw new IOException(
          "This config could not be loaded. Your previous settings were kept.", exception);
    }
  }

  public void renameProfile(String path, String currentPath) throws IOException {
    String nextPath = normalizeConfigName(path);
    if ("default".equalsIgnoreCase(nextPath)) {
      throw new IOException("The default config cannot be renamed.");
    }

    String previousPath = normalizeConfigName(currentPath);
    if (!nextPath.equals(previousPath)) {
      previousPath = this.createText(previousPath);
      Path sourcePath = this.createPath(nextPath);
      Path targetPath = this.createPath(previousPath);
      Files.move(sourcePath, targetPath);
      if (this.text2.equals(nextPath)) {
        try {
          this.updateState2(previousPath);
          this.text2 = previousPath;
        } catch (IOException iOException) {
          Files.move(targetPath, sourcePath);
          throw iOException;
        }
      }
    }
  }

  public void removeProfile(String path) throws IOException {
    String currentPath = normalizeConfigName(path);
    if ("default".equalsIgnoreCase(currentPath)) {
      throw new IOException("The default config cannot be deleted.");
    }

    if (this.text2.equalsIgnoreCase(currentPath)) {
      throw new IOException("Load another config before deleting the active one.");
    }

    Files.delete(this.createPath(currentPath));
  }

  public List<LocalConfigRepository.Profile> profiles() {
    ArrayList arrayList = new ArrayList();

    for (String path : this.listConfigs()) {
      long longValue = 0L;

      try {
        longValue = Files.getLastModifiedTime(this.createPath(path)).toMillis();
        arrayList.add(this.createProfile2(path, longValue, this.createJsonObject(path)));
      } catch (Exception exception) {
        arrayList.add(
            new LocalConfigRepository.Profile(
                path, longValue, 0, 0, 0, 0, 0, "This file could not be read as a ellice config."));
      }
    }

    return List.copyOf(arrayList);
  }

  public LocalConfigRepository.Profile previewProfile(Path path) throws IOException {
    JsonObject jsonObject = this.createJsonObject2(path);
    String currentPath = path.getFileName().toString().replaceFirst("(?i)\\.json$", "");
    if (jsonObject.has("name")
        && jsonObject.get("name").isJsonPrimitive()
        && jsonObject.getAsJsonPrimitive("name").isString()) {
      currentPath = jsonObject.get("name").getAsString();
    }

    currentPath = currentPath.replaceAll("[^\\p{L}\\p{N}_. \\-]", "_");
    if (currentPath.length() > 110) {
      currentPath = currentPath.substring(0, 110);
    }

    currentPath = currentPath.trim().replaceFirst("[. ]+$", "");
    if (currentPath.isBlank()) {
      currentPath = "Imported config";
    }

    String nextPath = currentPath;
    List items = this.listConfigs();

    for (int index = 2; checkCondition(items, currentPath); index++) {
      currentPath = nextPath + " " + index;
    }

    try {
      return this.createProfile2(
          currentPath, Files.getLastModifiedTime(path).toMillis(), jsonObject);
    } catch (RuntimeException exception) {
      throw new IOException("This file is not a valid config.", exception);
    }
  }

  private static boolean checkCondition(List<String> items, String text) {
    return items.stream().anyMatch(text::equalsIgnoreCase);
  }

  private LocalConfigRepository.Profile createProfile2(
      String path, long currentSize, JsonObject jsonObject) {
    JsonObject currentJsonObject = jsonObject.getAsJsonObject("modules");
    int index = 0;
    int value = 0;

    for (Entry entry : currentJsonObject.entrySet()) {
      JsonObject nextJsonObject = ((JsonElement) entry.getValue()).getAsJsonObject();
      if (nextJsonObject.has("enabled") && nextJsonObject.get("enabled").getAsBoolean()) {
        index++;
      }

      if (nextJsonObject.has("settings")) {
        value += nextJsonObject.getAsJsonObject("settings").size();
      }
    }

    int nextSize = jsonObject.has("keybinds") ? jsonObject.getAsJsonObject("keybinds").size() : 0;
    int previousSize =
        jsonObject.has("hudLayout")
            ? jsonObject.getAsJsonObject("hudLayout").getAsJsonArray("elements").size()
            : 0;
    return new LocalConfigRepository.Profile(
        path, currentSize, currentJsonObject.size(), index, value, nextSize, previousSize, "");
  }

  public Path exportToDirectory(String text, Path path) throws IOException {
    String currentText = normalizeConfigName(text);
    Files.createDirectories(path);
    Path currentPath = path.resolve(currentText + ".json");

    for (int index = 2; Files.exists(currentPath); index++) {
      currentPath = path.resolve(currentText + " " + index + ".json");
    }

    this.exportProfile(currentText, currentPath);
    return currentPath;
  }

  public String importProfile(Path path, String currentPath, boolean enabled) throws IOException {
    String nextPath = this.importProfile(path, currentPath);
    if (enabled) {
      try {
        this.loadProfile(nextPath);
      } catch (IOException iOException) {
        Files.deleteIfExists(this.createPath(nextPath));
        throw iOException;
      }
    }

    return nextPath;
  }

  private String createText(String text) throws IOException {
    String currentText = normalizeConfigName(text);
    if (!Files.exists(this.createPath(currentText))
        && !this.listConfigs().stream().anyMatch(currentText::equalsIgnoreCase)) {
      return currentText;
    } else {
      throw new IOException("A config with this name already exists.");
    }
  }

  private void updateState2(String text) throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", text);
    this.updateState4(this.path.resolve(".active-profile"), jsonObject);
  }

  private JsonObject createJsonObject(String text) throws IOException {
    return this.createJsonObject2(this.createPath(text));
  }

  private JsonObject createJsonObject2(Path path) throws IOException {
    try {
      JsonObject jsonObject =
          JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8)).getAsJsonObject();
      if (jsonObject.has("modules") && jsonObject.get("modules").isJsonObject()) {
        if (jsonObject.has("version") && jsonObject.get("version").getAsInt() > 2) {
          throw new IllegalArgumentException("Newer config schema");
        }

        for (String text : new String[] {"hudStyle", "keybinds", "positions", "hudLayout"}) {
          if (jsonObject.has(text) && !jsonObject.get(text).isJsonObject()) {
            throw new IllegalArgumentException(text);
          }
        }

        for (Entry entry : jsonObject.getAsJsonObject("modules").entrySet()) {
          JsonObject currentJsonObject = ((JsonElement) entry.getValue()).getAsJsonObject();
          if (currentJsonObject.has("enabled")
              && (!currentJsonObject.get("enabled").isJsonPrimitive()
                  || !currentJsonObject.getAsJsonPrimitive("enabled").isBoolean())) {
            throw new IllegalArgumentException("enabled");
          }

          if (currentJsonObject.has("settings")
              && !currentJsonObject.get("settings").isJsonObject()) {
            throw new IllegalArgumentException("settings");
          }
        }

        if (jsonObject.has("hudLayout")) {
          LayoutRepository.fromJson(jsonObject.getAsJsonObject("hudLayout"));
        }

        return jsonObject;
      } else {
        throw new IllegalArgumentException("Missing modules");
      }
    } catch (Exception exception) {
      throw new IOException(
          "This file is missing, damaged or belongs to a newer ellice version.", exception);
    }
  }

  public void setPosition(
      String text,
      float y,
      float width,
      float height,
      float value,
      float currentValue,
      float nextValue) {
    float previousValue = y / Math.max(1.0F, height - currentValue);
    float sourceValue = width / Math.max(1.0F, value - nextValue);
    previousValue = Math.max(0.0F, Math.min(1.0F, previousValue));
    sourceValue = Math.max(0.0F, Math.min(1.0F, sourceValue));
    this.text3.put(text, new float[] {previousValue, sourceValue});
  }

  public void setPosition(String text, float y, float width) {
    this.text3.put(text, new float[] {y, width});
  }

  public float[] getPosition(String text, float y, float width, float height, float value) {
    float[] floats = this.text3.get(text);
    if (floats == null) {
      return null;
    }

    float currentValue = floats[0] * (y - height);
    float nextValue = floats[1] * (width - value);
    return new float[] {currentValue, nextValue};
  }

  public float[] getPositionRaw(String text) {
    return this.text3.get(text);
  }

  private void updateState3(JsonObject jsonObject) {
    if (this.layoutFindSelector != null && jsonObject.has("hudLayout")) {
      LayoutFindSelector currentLayoutFindSelector =
          LayoutRepository.fromJson(jsonObject.getAsJsonObject("hudLayout"));
      this.layoutFindSelector.name = currentLayoutFindSelector.name;
      this.layoutFindSelector.author = currentLayoutFindSelector.author;
      this.layoutFindSelector.description = currentLayoutFindSelector.description;
      this.layoutFindSelector.version = currentLayoutFindSelector.version;
      this.layoutFindSelector.elements.clear();
      this.layoutFindSelector.elements.addAll(currentLayoutFindSelector.elements);
      if (this.path2 != null) {
        LayoutRepository.save(this.path2, this.layoutFindSelector);
      }
    }

    if (jsonObject.has("modules")) {
      JsonObject currentJsonObject = jsonObject.getAsJsonObject("modules");
      ConfigMergeService.merge(currentJsonObject);

      for (Module module : this.moduleRegisterService.all()) {
        String text = module.name();
        if (!currentJsonObject.has(text)
            && text.equals("KillAura")
            && currentJsonObject.has("Rotation Lab")) {
          text = "Rotation Lab";
        }

        if (!currentJsonObject.has(text)
            && text.equals("ScaffoldWalk")
            && currentJsonObject.has("Scaffold Walk Lab")) {
          text = "Scaffold Walk Lab";
        }

        if (currentJsonObject.has(text)) {
          JsonObject nextJsonObject = currentJsonObject.getAsJsonObject(text);
          if (nextJsonObject.has("settings")) {
            JsonObject previousJsonObject = nextJsonObject.getAsJsonObject("settings");

            for (ModuleSetting moduleSetting : module.settings()) {
              if (previousJsonObject.has(moduleSetting.name())) {
                this.updateState5(moduleSetting, previousJsonObject.get(moduleSetting.name()));
              }
            }
          }

          if (nextJsonObject.has("enabled")) {
            boolean currentEnabled = nextJsonObject.get("enabled").getAsBoolean();
            if (currentEnabled && !module.isEnabled()) {
              module.enable();
            } else if (!currentEnabled && module.isEnabled()) {
              module.disable();
            }
          }
        }
      }
    }

    if (jsonObject.has("keybinds") && CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get().keybinds().fromJson(jsonObject.getAsJsonObject("keybinds"));
    }

    if (jsonObject.has("hudStyle")) {
      JsonObject sourceJsonObject = jsonObject.getAsJsonObject("hudStyle");

      for (ModuleSetting currentModuleSetting : this.values.settings()) {
        if (sourceJsonObject.has(currentModuleSetting.name())) {
          this.updateState5(
              currentModuleSetting, sourceJsonObject.get(currentModuleSetting.name()));
        }
      }
    }

    if (jsonObject.has("hudItems") && jsonObject.get("hudItems").isJsonArray()) {
      this.hudItemsService.fromJson(jsonObject.getAsJsonArray("hudItems"));
    } else {
      this.hudItemsService.ensureDefaults();
    }

    this.text3.clear();
    if (jsonObject.has("positions")) {
      JsonObject targetJsonObject = jsonObject.getAsJsonObject("positions");

      for (String currentText : targetJsonObject.keySet()) {
        JsonArray jsonArray = targetJsonObject.getAsJsonArray(currentText);
        if (jsonArray.size() >= 2) {
          this.text3.put(
              currentText,
              new float[] {jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat()});
        }
      }
    }
  }

  private Path createPath(String currentPath) {
    return this.path.resolve(currentPath + ".json");
  }

  private void updateState4(Path currentPath, JsonObject jsonObject) throws IOException {
    Files.createDirectories(this.path);
    Path nextPath = currentPath.resolveSibling(currentPath.getFileName() + ".tmp");
    Files.writeString(nextPath, gson2.toJson(jsonObject), StandardCharsets.UTF_8);

    try {
      Files.move(
          nextPath,
          currentPath,
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.ATOMIC_MOVE);
    } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
      Files.move(nextPath, currentPath, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private JsonElement createJsonElement(ModuleSetting<?> moduleSetting) {
    if (moduleSetting instanceof ModuleSetting.Bool bool) {
      return new JsonPrimitive((Boolean) bool.get());
    } else if (moduleSetting instanceof ModuleSetting.Number number) {
      return new JsonPrimitive((Number) number.get());
    } else if (moduleSetting instanceof ModuleSetting.Mode mode) {
      return new JsonPrimitive((String) mode.get());
    } else if (moduleSetting instanceof ModuleSetting.Color color) {
      return new JsonPrimitive((Number) color.get());
    } else if (moduleSetting instanceof ModuleSetting.Curve curve) {
      ModuleSetting.CurveValue curveValue = (ModuleSetting.CurveValue) curve.get();
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("type", createText3(curveValue.type()));
      if (curveValue.type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
        jsonObject.addProperty("x1", curveValue.x1());
        jsonObject.addProperty("y1", curveValue.y1());
        jsonObject.addProperty("x2", curveValue.x2());
        jsonObject.addProperty("y2", curveValue.y2());
      } else if (curveValue.type() == ModuleSetting.CurveType.STEPS) {
        jsonObject.addProperty("count", curveValue.steps());
        jsonObject.addProperty("jump", createText4(curveValue.stepMode()));
      }

      return jsonObject;
    } else if (moduleSetting instanceof ModuleSetting.Keybind keybind) {
      return new JsonPrimitive((Number) keybind.get());
    } else if (moduleSetting instanceof ModuleSetting.Text text) {
      return new JsonPrimitive((String) text.get());
    } else if (moduleSetting instanceof ModuleSetting.Range range) {
      JsonArray jsonArray = new JsonArray();
      jsonArray.add(range.low());
      jsonArray.add(range.high());
      return jsonArray;
    } else if (moduleSetting instanceof ModuleSetting.MultiSelect multiSelect) {
      JsonArray currentJsonArray = new JsonArray();
      ((Set<?>) multiSelect.get()).forEach(value -> currentJsonArray.add(String.valueOf(value)));
      return currentJsonArray;
    } else {
      return JsonNull.INSTANCE;
    }
  }

  private void updateState5(ModuleSetting<?> moduleSetting, JsonElement jsonElement) {
    try {
      if (moduleSetting instanceof ModuleSetting.Bool bool && jsonElement.isJsonPrimitive()) {
        bool.set(jsonElement.getAsBoolean());
      } else if (moduleSetting instanceof ModuleSetting.Number number
          && jsonElement.isJsonPrimitive()) {
        number.set(jsonElement.getAsFloat());
      } else if (moduleSetting instanceof ModuleSetting.Mode mode
          && jsonElement.isJsonPrimitive()) {
        mode.set(jsonElement.getAsString());
      } else if (moduleSetting instanceof ModuleSetting.Color color
          && jsonElement.isJsonPrimitive()) {
        color.set(jsonElement.getAsInt());
      } else if (moduleSetting instanceof ModuleSetting.Curve curve && jsonElement.isJsonObject()) {
        curve.set(createCurveValue(jsonElement.getAsJsonObject()));
      } else if (moduleSetting instanceof ModuleSetting.Keybind keybind
          && jsonElement.isJsonPrimitive()) {
        keybind.set(jsonElement.getAsInt());
      } else if (moduleSetting instanceof ModuleSetting.Text text
          && jsonElement.isJsonPrimitive()) {
        text.set(jsonElement.getAsString());
      } else if (moduleSetting instanceof ModuleSetting.Range range && jsonElement.isJsonArray()) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.size() >= 2) {
          range.set(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
        }
      } else if (moduleSetting instanceof ModuleSetting.MultiSelect multiSelect
          && jsonElement.isJsonArray()) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        jsonElement.getAsJsonArray().forEach(item -> linkedHashSet.add(item.getAsString()));
        multiSelect.set(linkedHashSet);
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Failed to deserialize setting '{}': {}", moduleSetting.name(), exception.getMessage());
    }
  }

  private static ModuleSetting.CurveValue createCurveValue(JsonObject jsonObject) {
    ModuleSetting.CurveType curveType = createCurveType(createText5(jsonObject, "type"));

    return switch (curveType) {
      case LINEAR -> ModuleSetting.CurveValue.linear();
      case CUBIC_BEZIER ->
          ModuleSetting.CurveValue.cubicBezier(
              calculateValue(jsonObject, "x1"),
              calculateValue(jsonObject, "y1"),
              calculateValue(jsonObject, "x2"),
              calculateValue(jsonObject, "y2"));
      case STEPS ->
          ModuleSetting.CurveValue.steps(
              calculateValue2(jsonObject, "count"),
              createStepMode(createText5(jsonObject, "jump")));
    };
  }

  private static ModuleSetting.CurveType createCurveType(String text) {
    String currentText = createText2(text);
    if ("BEZIER".equals(currentText)) {
      currentText = "CUBIC_BEZIER";
    }

    return ModuleSetting.CurveType.valueOf(currentText);
  }

  private static ModuleSetting.StepMode createStepMode(String text) {
    String currentText = createText2(text);
    if ("START".equals(currentText) || "STEP_START".equals(currentText)) {
      currentText = "JUMP_START";
    } else if ("END".equals(currentText) || "STEP_END".equals(currentText)) {
      currentText = "JUMP_END";
    }

    return ModuleSetting.StepMode.valueOf(currentText);
  }

  private static String createText2(String text) {
    if (text != null && !text.isBlank()) {
      return text.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    } else {
      throw new IllegalArgumentException("Curve enum value must not be blank");
    }
  }

  private static String createText3(ModuleSetting.CurveType curveType) {
    return curveType.name().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  private static String createText4(ModuleSetting.StepMode stepMode) {
    return stepMode.name().toLowerCase(Locale.ROOT).replace('_', '-');
  }

  private static float calculateValue(JsonObject jsonObject, String text) {
    if (jsonObject.has(text)
        && jsonObject.get(text).isJsonPrimitive()
        && jsonObject.getAsJsonPrimitive(text).isNumber()) {
      float value = jsonObject.get(text).getAsFloat();
      if (!Float.isFinite(value)) {
        throw new IllegalArgumentException("Curve property '" + text + "' must be finite");
      } else {
        return value;
      }
    } else {
      throw new IllegalArgumentException("Curve property '" + text + "' is required");
    }
  }

  private static int calculateValue2(JsonObject jsonObject, String text) {
    if (jsonObject.has(text)
        && jsonObject.get(text).isJsonPrimitive()
        && jsonObject.getAsJsonPrimitive(text).isNumber()) {
      double doubleValue = jsonObject.get(text).getAsDouble();
      if (Double.isFinite(doubleValue)
          && doubleValue == Math.rint(doubleValue)
          && !(doubleValue < -2.147483648E9)
          && !(doubleValue > 2.147483647E9)) {
        return (int) doubleValue;
      } else {
        throw new IllegalArgumentException("Curve property '" + text + "' must be an integer");
      }
    } else {
      throw new IllegalArgumentException("Curve property '" + text + "' is required");
    }
  }

  private static String createText5(JsonObject jsonObject, String text) {
    if (jsonObject.has(text)
        && jsonObject.get(text).isJsonPrimitive()
        && jsonObject.getAsJsonPrimitive(text).isString()) {
      return jsonObject.get(text).getAsString();
    } else {
      throw new IllegalArgumentException("Curve property '" + text + "' is required");
    }
  }

  public record Profile(
      String name,
      long modified,
      int modules,
      int enabled,
      int settings,
      int keybinds,
      int hudElements,
      String problem) {
    public boolean valid() {
      return this.problem.isEmpty();
    }
  }
}
