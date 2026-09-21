package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.mixin.MinecraftAttackAccess;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public final class CompatAvailableService {
  private CompatAvailableService() {}

  public static boolean available(Minecraft minecraft) {
    return availableForPlanning(minecraft)
        && !minecraft.player.isUsingItem()
        && !minecraft.player.isHandsBusy();
  }

  public static boolean availableForPlanning(Minecraft minecraft) {
    if (minecraft != null
        && minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && !minecraft.isPaused()
        && minecraft.isWindowActive()) {
      LocalPlayer localPlayer = minecraft.player;
      boolean enabled = CompatCanStartService.holding(minecraft);
      return localPlayer.isAlive()
          && !localPlayer.isSpectator()
          && !localPlayer.isSleeping()
          && (enabled || !localPlayer.isUsingItem() && !localPlayer.isHandsBusy())
          && !CompatCanStartService.pendingRelease(minecraft)
          && !SoupInventoryBridge.handBusy()
          && !CompatReleaseTracker.handBusy()
          && !CompatOptionsTracker.handBusy()
          && !CompatActivateService.handBusy()
          && !PearlThrowController.handBusy()
          && !minecraft.gameMode.isDestroying()
          && !minecraft.options.keyUse.isDown()
          && !minecraft.options.keyAttack.isDown()
          && ((ScaffoldKeyMappingAccess) minecraft.options.keyUse).ellice$pendingClicks() == 0
          && ((ScaffoldKeyMappingAccess) minecraft.options.keyAttack).ellice$pendingClicks() == 0;
    } else {
      return false;
    }
  }

  public static Optional<EntityHitResult> pick(
      Minecraft minecraft,
      RotationVector rotationVector,
      RotationData rotationData,
      double doubleValue) {
    if (minecraft.player != null
        && minecraft.level != null
        && rotationVector != null
        && rotationData != null) {
      LocalPlayer localPlayer = minecraft.player;
      double currentDoubleValue = Math.min(doubleValue, localPlayer.entityInteractionRange());
      if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue <= 0.0)) {
        Vec3 currentX = new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z());
        Vec3 vec3 =
            Vec3.directionFromRotation((float) rotationData.pitch(), (float) rotationData.yaw());
        Vec3 currentVec3 = currentX.add(vec3.scale(currentDoubleValue));
        BlockHitResult blockHitResult =
            minecraft.level.clip(
                new ClipContext(currentX, currentVec3, Block.OUTLINE, Fluid.NONE, localPlayer));
        double nextDoubleValue = currentDoubleValue * currentDoubleValue;
        if (blockHitResult.getType() != Type.MISS) {
          nextDoubleValue = currentX.distanceToSqr(blockHitResult.getLocation());
        }

        AABB aABB =
            localPlayer
                .getBoundingBox()
                .move(currentX.subtract(localPlayer.getEyePosition()))
                .expandTowards(currentVec3.subtract(currentX))
                .inflate(1.0);
        EntityHitResult entityHitResult =
            ProjectileUtil.getEntityHitResult(
                localPlayer,
                currentX,
                currentVec3,
                aABB,
                item -> !item.isSpectator() && item.isPickable(),
                nextDoubleValue);
        return entityHitResult != null
                && currentX.distanceToSqr(entityHitResult.getLocation()) < nextDoubleValue
            ? Optional.of(entityHitResult)
            : Optional.empty();
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  public static boolean attack(
      Minecraft minecraft, EntityHitResult entityHitResult, RotationData rotationData) {
    return attack(minecraft, entityHitResult, rotationData, 1.0F);
  }

  public static boolean attack(
      Minecraft minecraft,
      EntityHitResult entityHitResult,
      RotationData rotationData,
      float value) {
    if (entityHitResult != null && rotationData != null && available(minecraft)) {
      if (CompatDecisionTracker.excluded(
          AntibotFeatureType.KILL_AURA, entityHitResult.getEntity())) {
        return false;
      }

      LocalPlayer localPlayer = minecraft.player;
      float currentValue = localPlayer.getAttackStrengthScale(0.0F);
      float nextValue = Float.isFinite(value) ? Math.max(0.0F, Math.min(1.0F, value)) : 1.0F;
      if (Float.isFinite(currentValue) && !(currentValue < nextValue)) {
        HitResult currentHitResult = minecraft.hitResult;
        minecraft.hitResult = entityHitResult;

        try {
          return ScaffoldForActorService.withRotation(
              localPlayer,
              rotationData,
              () -> {
                ((MinecraftAttackAccess) minecraft).ellice$startAttack();
                return localPlayer.getAttackStrengthScale(0.0F) < currentValue;
              });
        } finally {
          minecraft.hitResult = currentHitResult;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
