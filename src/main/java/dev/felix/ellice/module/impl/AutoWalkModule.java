package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSettingsService;

public final class AutoWalkModule extends ModuleSettingsService {
  private static boolean enabled;

  public AutoWalkModule() {
    super(
        ModuleBuilderData.builder("AutoWalk")
            .category(ModuleFeatureType.MOVEMENT)
            .description("Automatically walks forward; holding back or sneak pauses it.")
            .build());
  }

  @Override
  protected void onEnable() {
    enabled = true;
  }

  @Override
  protected void onDisable() {
    enabled = false;
  }

  public static boolean isActive() {
    return enabled;
  }
}
