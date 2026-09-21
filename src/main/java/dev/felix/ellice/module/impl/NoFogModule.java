package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;

public final class NoFogModule extends ModuleSettingsService {
  private static final float value = 1000000.0F;
  private static final float value2 = 2000000.0F;
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("Affect Liquids", true)
              .description(
                  "Also removes underwater and lava fog; disable it to preserve liquid depth"
                      + " cues."));
  private static volatile boolean enabled;
  private static volatile boolean enabled2 = true;

  public NoFogModule() {
    super(
        ModuleBuilderData.builder("No Fog")
            .description("Removes terrain fog, with optional support for water and lava.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.moduleBool.onChange(item -> enabled2 = item);
  }

  @Override
  protected void onEnable() {
    enabled = true;
    enabled2 = (Boolean) this.moduleBool.get();
  }

  @Override
  protected void onDisable() {
    enabled = false;
  }

  public static boolean isActive() {
    return enabled;
  }

  public static boolean affectsLiquids() {
    return enabled2;
  }

  public static float fogStart() {
    return 1000000.0F;
  }

  public static float fogEnd() {
    return 2000000.0F;
  }
}
