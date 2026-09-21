package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSettingsService;
import net.minecraft.client.Minecraft;

public final class ImplRequestsSprintClient extends ModuleSettingsService {
  private static boolean enabled;

  public ImplRequestsSprintClient() {
    super(
        ModuleBuilderData.builder("AutoSprint")
            .description("Automatically sprints while moving forward.")
            .category(ModuleFeatureType.MOVEMENT)
            .build());
  }

  @Override
  protected void onEnable() {
    enabled = true;
  }

  @Override
  protected void onDisable() {
    enabled = false;
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft != null && minecraft.player != null && !minecraft.options.keySprint.isDown()) {
      minecraft.player.setSprinting(false);
    }
  }

  public static boolean requestsSprint(Minecraft minecraft) {
    return enabled
        && minecraft != null
        && minecraft.player != null
        && minecraft.level != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && minecraft.isWindowActive()
        && !minecraft.isPaused()
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()
        && !minecraft.player.isPassenger();
  }
}
