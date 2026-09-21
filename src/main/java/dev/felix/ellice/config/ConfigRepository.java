package dev.felix.ellice.config;

import com.google.gson.Gson;
import dev.felix.ellice.ui.material.MaterialData;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.theme.ThemeBlurQualityData;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class ConfigRepository {
  public static final List<ConfigRepository.EffectsPreset> EFFECT_PRESETS =
      List.of(
          new ConfigRepository.EffectsPreset(
              "toaster",
              "Toaster",
              "No blur or shadows. Simple glass and a static menu.",
              new ThemeBlurQualityData(
                  0.0F, ThemeBlurQualityData.BlurQuality.LOW, 0.0F, false, false)),
          new ConfigRepository.EffectsPreset(
              "balanced",
              "Balanced",
              "Lighter blur, softer shadows and simple glass.",
              new ThemeBlurQualityData(
                  0.65F, ThemeBlurQualityData.BlurQuality.MEDIUM, 0.5F, false, true)),
          new ConfigRepository.EffectsPreset(
              "quality",
              "Quality",
              "Full-resolution blur, glass refraction and shadows.",
              ThemeBlurQualityData.defaults()));
  public static final List<ConfigRepository.Preset> PRESETS =
      List.of(
          new ConfigRepository.Preset(
              "amethyst",
              "Amethyst",
              "ellice's original violet",
              ConfigRepository.Appearance.defaults()),
          new ConfigRepository.Preset(
              "arctic",
              "Arctic",
              "Ice blue and cool slate",
              new ConfigRepository.Appearance(-9254410, -14469557, false)),
          new ConfigRepository.Preset(
              "mint",
              "Mint",
              "Fresh green, soft charcoal",
              new ConfigRepository.Appearance(-9840211, -14469329, false)),
          new ConfigRepository.Preset(
              "rose",
              "Rose",
              "Warm pink and plum",
              new ConfigRepository.Appearance(-942150, -12835277, false)),
          new ConfigRepository.Preset(
              "ember",
              "Ember",
              "Amber on warm graphite",
              new ConfigRepository.Appearance(-741267, -12833758, false)),
          new ConfigRepository.Preset(
              "graphite",
              "Graphite",
              "Quiet, neutral contrast",
              new ConfigRepository.Appearance(-3487030, -14277082, false)),
          new ConfigRepository.Preset(
              "paper",
              "Paper",
              "Clean light surfaces",
              new ConfigRepository.Appearance(-9084737, -1514002, true)),
          new ConfigRepository.Preset(
              "linen",
              "Linen",
              "Warm light with olive",
              new ConfigRepository.Appearance(-10128324, -1119779, true)),
          new ConfigRepository.Preset(
              "midnight",
              "Midnight",
              "Electric blue on deep navy",
              new ConfigRepository.Appearance(-8479489, -14668720, false)),
          new ConfigRepository.Preset(
              "lagoon",
              "Lagoon",
              "Turquoise and deep ocean tones",
              new ConfigRepository.Appearance(-11478832, -14665152, false)),
          new ConfigRepository.Preset(
              "forest",
              "Forest",
              "Soft sage on evergreen",
              new ConfigRepository.Appearance(-5978742, -14076118, false)),
          new ConfigRepository.Preset(
              "matcha",
              "Matcha",
              "Bright lime and smoky olive",
              new ConfigRepository.Appearance(-3089290, -13354974, false)),
          new ConfigRepository.Preset(
              "espresso",
              "Espresso",
              "Caramel and roasted coffee",
              new ConfigRepository.Appearance(-3627130, -12964826, false)),
          new ConfigRepository.Preset(
              "cherry",
              "Cherry",
              "Coral red with burgundy surfaces",
              new ConfigRepository.Appearance(-30825, -12442063, false)),
          new ConfigRepository.Preset(
              "orchid",
              "Orchid",
              "Luminous magenta and muted plum",
              new ConfigRepository.Appearance(-1663761, -13097923, false)),
          new ConfigRepository.Preset(
              "cloud",
              "Cloud",
              "Clear blue on cool white",
              new ConfigRepository.Appearance(-12490046, -1840139, true)),
          new ConfigRepository.Preset(
              "blossom",
              "Blossom",
              "Rose ink on soft pink porcelain",
              new ConfigRepository.Appearance(-4632710, -859672, true)),
          new ConfigRepository.Preset(
              "sand",
              "Sand",
              "Terracotta and sunlit cream",
              new ConfigRepository.Appearance(-4626365, -989480, true)),
          new ConfigRepository.Preset(
              "glacier",
              "Glacier",
              "Deep teal on misty white",
              new ConfigRepository.Appearance(-14254462, -2035475, true)),
          new ConfigRepository.Preset(
              "lavender",
              "Lavender",
              "Soft violet and lilac mist",
              new ConfigRepository.Appearance(-6856272, -1121293, true)));
  private final Path path;
  private final ThemeIsSetService values;
  private ConfigRepository.Appearance appearance2 = ConfigRepository.Appearance.defaults();
  private ThemeBlurQualityData themeBlurQualityData = ThemeBlurQualityData.defaults();
  private ThemeCornerData themeCornerData = ThemeCornerData.defaults();
  private boolean enabled;
  private String text2 = "";
  private Consumer<ThemeCornerData> consumer = item -> {};
  private static final Gson gson2 = new Gson();

  public ConfigRepository(ThemeIsSetService themeIsSet) {
    this(null, themeIsSet);
  }

  public ConfigRepository(Path currentPath, ThemeIsSetService themeIsSet) {
    this.path = currentPath;
    this.values = Objects.requireNonNull(themeIsSet);
    if (currentPath != null) {
      try {
        if (Files.size(currentPath) <= 16384L) {
          ConfigRepository.FileState fileState =
              (ConfigRepository.FileState)
                  gson2.fromJson(Files.readString(currentPath), ConfigRepository.FileState.class);
          if (fileState != null
              && fileState.version() >= 1
              && fileState.version() <= 3
              && fileState.appearance() != null) {
            this.appearance2 = fileState.appearance();
            if (fileState.version() >= 2 && fileState.effects() != null) {
              this.themeBlurQualityData = fileState.effects();
            }

            if (fileState.version() >= 2 && fileState.preferences() != null) {
              ThemeCornerData currentThemeCornerData = fileState.preferences();
              this.themeCornerData =
                  fileState.version() == 2
                      ? new ThemeCornerData(
                          currentThemeCornerData.notifications(),
                          currentThemeCornerData.notificationDuration(),
                          currentThemeCornerData.notificationCorner(),
                          currentThemeCornerData.notificationLimit(),
                          currentThemeCornerData.tooltips(),
                          currentThemeCornerData.tooltipDelay(),
                          currentThemeCornerData.scrollSpeed(),
                          currentThemeCornerData.rememberClickGui(),
                          currentThemeCornerData.focusSearch(),
                          currentThemeCornerData.dimBackground(),
                          true)
                      : currentThemeCornerData;
            }
          }
        }
      } catch (IOException | RuntimeException iOExceptionRuntimeException) {
      }
    }

    this.updateState();
    ThemeBlurQualityData.apply(this.themeBlurQualityData);
    ThemeCornerData.apply(this.themeCornerData);
  }

  public ConfigRepository.Appearance appearance() {
    return this.appearance2;
  }

  public ThemeIsSetService theme() {
    return this.values;
  }

  public ThemeBlurQualityData effects() {
    return this.themeBlurQualityData;
  }

  public ThemeCornerData preferences() {
    return this.themeCornerData;
  }

  public void onPreferencesChanged(Consumer<ThemeCornerData> currentConsumer) {
    this.consumer = Objects.requireNonNull(currentConsumer);
    this.consumer.accept(this.themeCornerData);
  }

  public void preferences(ThemeCornerData currentThemeCornerData) {
    if (!this.themeCornerData.equals(currentThemeCornerData)) {
      this.themeCornerData = Objects.requireNonNull(currentThemeCornerData);
      this.enabled = true;
      this.text2 = "";
      ThemeCornerData.apply(currentThemeCornerData);
      this.consumer.accept(currentThemeCornerData);
      this.values.updateColors(Map.of());
    }
  }

  public String effectsPresetName() {
    return EFFECT_PRESETS.stream()
        .filter(item -> item.effects().equals(this.themeBlurQualityData))
        .map(ConfigRepository.EffectsPreset::name)
        .findFirst()
        .orElse("Custom");
  }

  public void effects(ThemeBlurQualityData currentThemeBlurQualityData) {
    if (!this.themeBlurQualityData.equals(currentThemeBlurQualityData)) {
      this.themeBlurQualityData = Objects.requireNonNull(currentThemeBlurQualityData);
      this.enabled = true;
      this.text2 = "";
      ThemeBlurQualityData.apply(currentThemeBlurQualityData);
      this.values.updateColors(Map.of());
    }
  }

  public String error() {
    return this.text2;
  }

  public String presetName() {
    return PRESETS.stream()
        .filter(item -> item.appearance().equals(this.appearance2))
        .map(ConfigRepository.Preset::name)
        .findFirst()
        .orElse("Custom");
  }

  public void appearance(ConfigRepository.Appearance currentAppearance) {
    if (!this.appearance2.equals(currentAppearance)) {
      this.appearance2 = Objects.requireNonNull(currentAppearance);
      this.enabled = true;
      this.text2 = "";
      this.updateState();
    }
  }

  private void updateState() {
    MaterialIsLightService.apply(this.appearance2.scheme());
    LinkedHashMap linkedHashMap = new LinkedHashMap();
    linkedHashMap.put("accent", MaterialIsLightService.PRIMARY);
    linkedHashMap.put("accent-hover", MaterialIsLightService.PRIMARY);
    linkedHashMap.put("text", MaterialIsLightService.ON_SURFACE);
    linkedHashMap.put("text-dim", MaterialIsLightService.ON_SURFACE_VARIANT);
    linkedHashMap.put("text-muted", MaterialIsLightService.OUTLINE);
    linkedHashMap.put("surface-solid", MaterialIsLightService.SURFACE);
    linkedHashMap.put("slider.fill", MaterialIsLightService.PRIMARY);
    linkedHashMap.put("toggle.active", MaterialIsLightService.PRIMARY);
    linkedHashMap.put("textinput.selection", MaterialIsLightService.SELECTION);
    linkedHashMap.put("textinput.border-focus", MaterialIsLightService.PRIMARY);
    this.values.updateColors(linkedHashMap);
  }

  public void save() {
    if (this.enabled) {
      if (this.path == null) {
        this.enabled = false;
      } else {
        try {
          Path currentPath = this.path.toAbsolutePath().getParent();
          Files.createDirectories(currentPath);
          Path nextPath = Files.createTempFile(currentPath, "client-settings-", ".tmp");

          try {
            Files.writeString(
                nextPath,
                gson2.toJson(
                    new ConfigRepository.FileState(
                        3, this.appearance2, this.themeBlurQualityData, this.themeCornerData)));

            try {
              Files.move(
                  nextPath,
                  this.path,
                  StandardCopyOption.REPLACE_EXISTING,
                  StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
              Files.move(nextPath, this.path, StandardCopyOption.REPLACE_EXISTING);
            }
          } finally {
            Files.deleteIfExists(nextPath);
          }

          this.enabled = false;
          this.text2 = "";
        } catch (IOException iOException) {
          this.text2 = "Could not save client settings. Your preview is still active.";
        }
      }
    }
  }

  public record Appearance(int accent, int surfaceTint, boolean light) {
    public Appearance(int accent, int surfaceTint, boolean light) {
      accent |= -16777216;
      surfaceTint |= -16777216;
      this.accent = accent;
      this.surfaceTint = surfaceTint;
      this.light = light;
    }

    public static ConfigRepository.Appearance defaults() {
      return new ConfigRepository.Appearance(-3097345, -15461864, false);
    }

    public MaterialData scheme() {
      return MaterialData.create(this.accent, this.surfaceTint, this.light);
    }
  }

  public record EffectsPreset(
      String id, String name, String description, ThemeBlurQualityData effects) {}

  private record FileState(
      int version,
      ConfigRepository.Appearance appearance,
      ThemeBlurQualityData effects,
      ThemeCornerData preferences) {}

  public record Preset(
      String id, String name, String description, ConfigRepository.Appearance appearance) {}
}
