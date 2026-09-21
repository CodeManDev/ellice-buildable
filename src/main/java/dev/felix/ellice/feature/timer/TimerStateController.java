package dev.felix.ellice.feature.timer;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.impl.ImplProfileService;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public final class TimerStateController {
  private static final TimerModeTracker timerModeTracker = new TimerModeTracker();
  private static ImplProfileService implProfileService;
  private static TimerModeTracker.Profile profile2;

  private TimerStateController() {}

  public static void activate(ImplProfileService implProfile) {
    implProfileService = implProfile;
    updateState(true);
  }

  public static void deactivate(ImplProfileService implProfile) {
    if (implProfileService == implProfile) {
      implProfileService = null;
      profile2 = null;
    }
  }

  public static void tick() {
    updateState(false);
  }

  public static void worldChanged() {
    if (implProfileService != null) {
      updateState(true);
    }
  }

  public static void accepted(Packet<?> packet) {
    if (implProfileService != null && packet instanceof ServerboundMovePlayerPacket) {
      timerModeTracker.movementPacketAccepted();
    }
  }

  public static float combine(float value) {
    float currentValue = Float.isFinite(value) && value > 0.0F ? value : 1.0F;
    return !checkCondition()
        ? currentValue
        : Math.clamp(currentValue * timerModeTracker.multiplier(), 0.05F, 10.0F);
  }

  public static float multiplier() {
    return checkCondition() ? timerModeTracker.multiplier() : 1.0F;
  }

  public static String status() {
    ImplProfileService implProfile = implProfileService;
    if (implProfile == null) {
      return "Inactive";
    }

    if (!checkCondition()) {
      return "Waiting for world";
    }

    String text =
        profile2 != null
                && (profile2.mode() == TimerModeTracker.Mode.PULSE
                    || profile2.mode() == TimerModeTracker.Mode.PACKET_PULSE)
            ? (timerModeTracker.boostPhase() ? "boost" : "brake")
            : "active";
    return implProfile.modeName()
        + " · "
        + createText(timerModeTracker.multiplier())
        + "x · "
        + text;
  }

  private static void updateState(boolean enabled) {
    ImplProfileService implProfile = implProfileService;
    if (implProfile != null) {
      TimerModeTracker.Profile currentProfile = implProfile.profile();
      if (!enabled && currentProfile.equals(profile2)) {
        timerModeTracker.tick();
      } else {
        profile2 = currentProfile;
        timerModeTracker.configure(currentProfile);
      }
    }
  }

  private static boolean checkCondition() {
    if (implProfileService != null && profile2 != null && CoreIsInitializedHandler.isReady()) {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.player != null
          && minecraft.level != null
          && !minecraft.isPaused()
          && !minecraft.level.tickRateManager().isFrozen();
    } else {
      return false;
    }
  }

  private static String createText(float value) {
    return String.format(Locale.ROOT, "%.2f", value);
  }
}
