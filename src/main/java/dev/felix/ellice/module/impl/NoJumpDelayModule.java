package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSettingsService;

public final class NoJumpDelayModule extends ModuleSettingsService {
  private static boolean enabled;

  public NoJumpDelayModule() {
    super(
        ModuleBuilderData.builder("NoJumpDelay")
            .category(ModuleFeatureType.MOVEMENT)
            .description("Removes the local delay between held jumps.")
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
