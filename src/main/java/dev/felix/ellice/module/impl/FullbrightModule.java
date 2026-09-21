package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;

public final class FullbrightModule extends ModuleSettingsService {
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number("Strength", 1.0F, 0.0F, 1.0F, 0.01F)
              .description(
                  "Sets the minimum client lightmap brightness; 0 preserves vanilla lighting and 1"
                      + " is fully bright."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("Reduce Darkness", true)
              .description(
                  "Also suppresses Darkness-effect and boss-overlay dimming in proportion to"
                      + " Strength."));
  private static volatile boolean enabled;
  private static volatile float value = 1.0F;
  private static volatile boolean enabled2 = true;

  public FullbrightModule() {
    super(
        ModuleBuilderData.builder("Fullbright")
            .description("Brightens the client lightmap for clearer caves and nights.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.moduleNumber.onChange(item -> value = item);
    this.moduleBool.onChange(item -> enabled2 = item);
  }

  @Override
  protected void onEnable() {
    enabled = true;
    value = (Float) this.moduleNumber.get();
    enabled2 = (Boolean) this.moduleBool.get();
  }

  @Override
  protected void onDisable() {
    enabled = false;
  }

  public static boolean isActive() {
    return enabled;
  }

  public static float brightness() {
    return value;
  }

  public static boolean reducesDarkness() {
    return enabled2;
  }
}
