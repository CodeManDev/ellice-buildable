package dev.felix.ellice.feature.movement;

import dev.felix.ellice.feature.velocity.VelocityActivateService;
import dev.felix.ellice.module.impl.AutoJumpModule;
import dev.felix.ellice.module.impl.AutoWalkModule;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;

public final class MovementAvailableService {
  private MovementAvailableService() {}

  public static boolean available(Minecraft minecraft) {
    return minecraft != null
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

  public static boolean forward(Minecraft minecraft) {
    return AutoWalkModule.isActive()
        && available(minecraft)
        && !minecraft.options.keyDown.isDown()
        && !minecraft.options.keyShift.isDown();
  }

  public static boolean jump(Minecraft minecraft) {
    if (AutoJumpModule.isActive() && available(minecraft)) {
      LocalPlayer localPlayer = minecraft.player;
      Options currentOptions = minecraft.options;
      return localPlayer.onGround()
          && !localPlayer.isInWater()
          && !localPlayer.isInLava()
          && !localPlayer.onClimbable()
          && !localPlayer.getAbilities().flying
          && !currentOptions.keyShift.isDown()
          && (forward(minecraft)
              || currentOptions.keyUp.isDown()
              || currentOptions.keyDown.isDown()
              || currentOptions.keyLeft.isDown()
              || currentOptions.keyRight.isDown());
    } else {
      return false;
    }
  }

  public static boolean requested(KeyMapping keyMapping) {
    Minecraft minecraft = Minecraft.getInstance();
    if (keyMapping.isDown()) {
      return true;
    } else {
      return minecraft == null
          ? false
          : keyMapping == minecraft.options.keyUp && forward(minecraft)
              || keyMapping == minecraft.options.keyJump
                  && (jump(minecraft) || VelocityActivateService.jumpRequested());
    }
  }
}
