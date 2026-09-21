package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;

public final class ImplHideVignetteService extends ModuleSettingsService {
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("Vignette", true)
              .description(
                  "Hides vanilla edge darkening, including the low-light vignette around the"
                      + " screen."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          new ModuleSetting.Bool("Helmet Overlays", true)
              .description(
                  "Hides first-person texture overlays from worn head items, such as the"
                      + " carved-pumpkin mask."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          new ModuleSetting.Bool("Powder Snow", false)
              .description(
                  "Hides the frozen-border overlay shown while the player is inside powder snow."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          new ModuleSetting.Bool("Portal", true)
              .description(
                  "Hides the purple distortion overlay while standing in a Nether portal."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          new ModuleSetting.Bool("Nausea", true)
              .description("Hides the screen distortion produced by the Nausea status effect."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          new ModuleSetting.Bool("Spyglass Scope", false)
              .description(
                  "Hides the dark scope frame while using a spyglass; the zoom itself remains"
                      + " active."));
  private static volatile boolean enabled;
  private static volatile boolean enabled2 = true;
  private static volatile boolean enabled3 = true;
  private static volatile boolean enabled4 = false;
  private static volatile boolean enabled5 = true;
  private static volatile boolean enabled6 = true;
  private static volatile boolean enabled7 = false;

  public ImplHideVignetteService() {
    super(
        ModuleBuilderData.builder("Clear GUI")
            .description(
                "Hides selected first-person overlays such as the vignette, portals, and helmets.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.moduleBool.onChange(item -> enabled2 = item);
    this.moduleBool2.onChange(item -> enabled3 = item);
    this.moduleBool3.onChange(item -> enabled4 = item);
    this.moduleBool4.onChange(item -> enabled5 = item);
    this.moduleBool5.onChange(item -> enabled6 = item);
    this.moduleBool6.onChange(item -> enabled7 = item);
  }

  @Override
  protected void onEnable() {
    enabled = true;
    enabled2 = (Boolean) this.moduleBool.get();
    enabled3 = (Boolean) this.moduleBool2.get();
    enabled4 = (Boolean) this.moduleBool3.get();
    enabled5 = (Boolean) this.moduleBool4.get();
    enabled6 = (Boolean) this.moduleBool5.get();
    enabled7 = (Boolean) this.moduleBool6.get();
  }

  @Override
  protected void onDisable() {
    enabled = false;
  }

  public static boolean hideVignette() {
    return enabled && enabled2;
  }

  public static boolean hideTextureOverlay(Object value) {
    if (enabled && value != null) {
      String id = createText(value);
      return id.contains("powder_snow_outline") ? enabled4 : enabled3;
    } else {
      return false;
    }
  }

  public static boolean hidePortal() {
    return enabled && enabled5;
  }

  public static boolean hideNausea() {
    return enabled && enabled6;
  }

  public static boolean hideSpyglass() {
    return enabled && enabled7;
  }

  private static String createText(Object value) {
    try {
      Object currentValue = value.getClass().getMethod("getPath").invoke(value);
      return String.valueOf(currentValue);
    } catch (ReflectiveOperationException reflectiveOperationException) {
      return value.toString();
    }
  }
}
