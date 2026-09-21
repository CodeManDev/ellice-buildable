package dev.felix.ellice.feature.velocity;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.module.impl.ImplSelectedModeSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class VelocityActivateService {
  private static final VelocityTransformConverter fh08yztkwnsf;
  private static ImplSelectedModeSelector implSelectedModeSelector;
  private static LocalPlayer localPlayer;
  private static boolean f3n21tq0izu;
  private static int count;

  private VelocityActivateService() {}

  public static void activate(final ImplSelectedModeSelector implSelectedModeSelector) {
    VelocityActivateService.implSelectedModeSelector = implSelectedModeSelector;
    reset();
  }

  public static void deactivate() {
    VelocityActivateService.implSelectedModeSelector = null;
    reset();
  }

  public static void reset() {
    VelocityActivateService.fh08yztkwnsf.reset();
    VelocityActivateService.localPlayer = null;
    VelocityActivateService.f3n21tq0izu = false;
  }

  private static boolean mbbtv2hwugcv(final Entity entity) {
    if (VelocityActivateService.implSelectedModeSelector == null) {
      return false;
    }
    final Minecraft instance = Minecraft.getInstance();
    return entity != null
        && entity == instance.player
        && instance.level != null
        && instance.player.isAlive()
        && !instance.player.isSpectator()
        && !instance.player.isPassenger();
  }

  public static Vec3 impulse(final Entity entity, final Vec3 vec3, final boolean b) {
    if (!mbbtv2hwugcv(entity) || (b && !VelocityActivateService.implSelectedModeSelector.explosions.get())) {
      return vec3;
    }
    final VelocityTransformConverter.Mode selectedMode =
        VelocityActivateService.implSelectedModeSelector.selectedMode();
    if (selectedMode == VelocityTransformConverter.Mode.PUSH
        || (VelocityActivateService.implSelectedModeSelector.onlyGround.get()
            && selectedMode != VelocityTransformConverter.Mode.JUMP
            && !entity.onGround())
        || entity.getRandom().nextFloat() * Float.intBitsToFloat(1120403456)
            >= VelocityActivateService.implSelectedModeSelector.chance.get()) {
      return vec3;
    }
    if (selectedMode == VelocityTransformConverter.Mode.CANCEL) {
      return null;
    }
    if (selectedMode == VelocityTransformConverter.Mode.JUMP) {
      if (vec3.lengthSqr() > Double.longBitsToDouble(4457293557087583675L)) {
        if (VelocityActivateService.localPlayer != entity) {
          VelocityActivateService.fh08yztkwnsf.reset();
          VelocityActivateService.localPlayer = (LocalPlayer) entity;
        }
        VelocityActivateService.fh08yztkwnsf.impulse(
            VelocityActivateService.localPlayer.tickCount,
            Math.round(VelocityActivateService.implSelectedModeSelector.jumpDelay.get()));
      }
      return vec3;
    }
    final RotationVector transform =
        VelocityTransformConverter.transform(
            selectedMode,
            createRotationData7(entity.getDeltaMovement()),
            createRotationData7(vec3),
            b,
            (selectedMode == VelocityTransformConverter.Mode.REVERSE)
                ? VelocityActivateService.implSelectedModeSelector.reverseStrength.get()
                : VelocityActivateService.implSelectedModeSelector.horizontal.get(),
            VelocityActivateService.implSelectedModeSelector.vertical.get());
    return new Vec3(transform.x(), transform.y(), transform.z());
  }

  public static double pushScale(final Entity entity) {
    return (mbbtv2hwugcv(entity)
            && VelocityActivateService.implSelectedModeSelector.selectedMode()
                == VelocityTransformConverter.Mode.PUSH)
        ? (VelocityActivateService.implSelectedModeSelector.entityPush.get() / Float.intBitsToFloat(1120403456))
        : 1.0;
  }

  public static boolean cancelBlockPush(final Entity entity) {
    return mbbtv2hwugcv(entity)
        && VelocityActivateService.implSelectedModeSelector.selectedMode()
            == VelocityTransformConverter.Mode.PUSH
        && !VelocityActivateService.implSelectedModeSelector.blockPush.get();
  }

  public static void beforeMovement() {
    VelocityActivateService.f3n21tq0izu = false;
    final Minecraft instance = Minecraft.getInstance();
    final LocalPlayer player = instance.player;
    final CombatOwnsService.Owner owner = CombatOwnsService.currentOwner().orElse(null);
    final boolean b = owner != null && owner != CombatOwnsService.Owner.KILL_AURA;
    final boolean b2 =
        CoreIsInitializedHandler.isReady()
            && CoreIsInitializedHandler.get()
                .modules()
                .get("Speed")
                .map(module -> module.isEnabled())
                .orElse(false);
    VelocityActivateService.f3n21tq0izu =
        VelocityActivateService.fh08yztkwnsf.jump(
            (player == null) ? 0L : ((long) player.tickCount),
            mbbtv2hwugcv((Entity) player)
                && VelocityActivateService.localPlayer == player
                && VelocityActivateService.implSelectedModeSelector.selectedMode()
                    == VelocityTransformConverter.Mode.JUMP
                && instance.screen == null
                && instance.getOverlay() == null
                && !instance.isPaused()
                && instance.isWindowActive()
                && !player.isInWater()
                && !player.isInLava()
                && !player.onClimbable()
                && !player.isFallFlying()
                && !player.getAbilities().flying
                && !b
                && !b2,
            player != null && player.onGround());
    if (VelocityActivateService.f3n21tq0izu) {
      VelocityActivateService.count = player.tickCount;
    }
  }

  public static boolean jumpRequested() {
    final LocalPlayer player = Minecraft.getInstance().player;
    return VelocityActivateService.f3n21tq0izu
        && mbbtv2hwugcv((Entity) player)
        && player == VelocityActivateService.localPlayer
        && player.tickCount == VelocityActivateService.count
        && !LocalPhysicsFrameBus.wasAborted(VelocityActivateService.count);
  }

  private static RotationVector createRotationData7(final Vec3 vec3) {
    return new RotationVector(vec3.x, vec3.y, vec3.z);
  }

  static {
    fh08yztkwnsf = new VelocityTransformConverter();
  }
}
