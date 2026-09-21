package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.combat.CombatAllowsService;
import dev.felix.ellice.feature.combat.CombatDecisionTracker;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationTarget;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class RotationObservationCapture {
  private RotationObservationCapture() {}

  public static Optional<RotationObservationCapture.Observation> capture(
      Minecraft minecraft,
      long longValue,
      RotationTarget rotationTarget,
      RotationData rotationData,
      double doubleValue) {
    if (rotationTarget != null
        && rotationData != null
        && minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && minecraft.isWindowActive()
        && !minecraft.isPaused()
        && !SoupInventoryBridge.handBusy()
        && !CompatReleaseTracker.handBusy()
        && !CompatActivateService.handBusy()
        && !minecraft.options.keyUse.isDown()
        && !minecraft.gameMode.isDestroying()) {
      LocalPlayer localPlayer = minecraft.player;
      if (localPlayer.isAlive()
          && !localPlayer.isSpectator()
          && !localPlayer.isSleeping()
          && !localPlayer.isUsingItem()
          && localPlayer.onGround()
          && !localPlayer.onClimbable()
          && !localPlayer.isPassenger()
          && !localPlayer.isInWater()
          && !localPlayer.isInLava()
          && !localPlayer.isSwimming()
          && !localPlayer.isFallFlying()
          && !localPlayer.isAutoSpinAttack()
          && !localPlayer.getAbilities().flying
          && !(Math.abs(localPlayer.getDeltaMovement().y) > 0.1)) {
        if (minecraft.level.getEntity(rotationTarget.entityId())
                instanceof LivingEntity livingEntity
            && livingEntity.isAlive()
            && livingEntity.getUUID().equals(rotationTarget.uuid())
            && !livingEntity.isSpectator()
            && !CompatDecisionTracker.excluded(AntibotFeatureType.KILL_AURA, livingEntity)) {
          ScaffoldCompatibility scaffoldCompatibility =
              CompatAdapterService.scaffoldEnvironment().orElse(null);
          if (scaffoldCompatibility == null) {
            return Optional.empty();
          }

          double currentDoubleValue =
              scaffoldCompatibility.groundInputAcceleration(minecraft, false);
          if (localPlayer.isSprinting()) {
            currentDoubleValue /= 1.300000011920929;
          }

          double nextDoubleValue = scaffoldCompatibility.groundInputAcceleration(minecraft, true);
          double previousDoubleValue = scaffoldCompatibility.groundFriction(minecraft) * 0.91;
          if (Double.isFinite(currentDoubleValue)
              && Double.isFinite(nextDoubleValue)
              && Double.isFinite(previousDoubleValue)
              && !(currentDoubleValue <= 0.0)
              && !(nextDoubleValue < currentDoubleValue)
              && !(previousDoubleValue < 0.0)
              && !(previousDoubleValue >= 0.99)) {
            double sourceDoubleValue = Math.min(doubleValue, localPlayer.entityInteractionRange());
            if (Double.isFinite(sourceDoubleValue) && !(sourceDoubleValue <= 0.0)) {
              AABB aABB = livingEntity.getBoundingBox();
              Vec3 vec3 = localPlayer.getDeltaMovement();
              CombatDecisionTracker.Fighter currentX =
                  new CombatDecisionTracker.Fighter(
                      new RotationVector(
                          localPlayer.getX(), localPlayer.getY(), localPlayer.getZ()),
                      new RotationVector(vec3.x, vec3.y, vec3.z),
                      localPlayer.getEyeHeight(),
                      (localPlayer.getHealth() + localPlayer.getAbsorptionAmount())
                          / Math.max(1.0F, localPlayer.getMaxHealth()),
                      localPlayer.getAttackStrengthScale(0.0F),
                      localPlayer.getCurrentItemAttackStrengthDelay(),
                      Math.max(0, localPlayer.hurtTime));
              boolean enabled =
                  CompatBlocksAttackFromService.blocksAttackFrom(
                      livingEntity, localPlayer.position());
              CombatDecisionTracker.Opponent opponent =
                  new CombatDecisionTracker.Opponent(
                      livingEntity.getUUID(),
                      new RotationBoundingBox(
                          aABB.minX, aABB.minY, aABB.minZ, aABB.maxX, aABB.maxY, aABB.maxZ),
                      enabled ? livingEntity.getYHeadRot() : livingEntity.getYRot(),
                      (livingEntity.getHealth() + livingEntity.getAbsorptionAmount())
                          / Math.max(1.0F, livingEntity.getMaxHealth()),
                      Math.max(0, livingEntity.hurtTime),
                      enabled);
              CompatEnableHandler.Status currentStatus = CompatEnableHandler.status();
              int value =
                  CompatEnableService.replaying()
                          || livingEntity.getUUID().equals(currentStatus.target())
                              && currentStatus.packets() > 0
                      ? 0
                      : 1;
              CombatDecisionTracker.Frame frame =
                  new CombatDecisionTracker.Frame(
                      longValue,
                      currentX,
                      opponent,
                      localPlayer.getYRot(),
                      (float) rotationData.yaw(),
                      sourceDoubleValue,
                      new CombatDecisionTracker.Physics(
                          currentDoubleValue, nextDoubleValue, previousDoubleValue),
                      (value != 0));
              return Optional.of(
                  new RotationObservationCapture.Observation(
                      frame, new RotationObservationCapture.Terrain(minecraft)));
            } else {
              return Optional.empty();
            }
          } else {
            return Optional.empty();
          }
        } else {
          return Optional.empty();
        }
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  public record Observation(
      CombatDecisionTracker.Frame frame, CombatAllowsService.Terrain terrain) {}

  private static final class Terrain implements CombatAllowsService.Terrain {
    private final Minecraft minecraft2;
    private final AABB aABB;
    private final double value;
    private final Map<RotationObservationCapture.Terrain.Point, Boolean> entries = new HashMap<>();

    private Terrain(Minecraft minecraft) {
      this.minecraft2 = minecraft;
      this.aABB = minecraft.player.getBoundingBox().deflate(1.0E-5, 0.0, 1.0E-5);
      this.value = Math.min(0.6, minecraft.player.maxUpStep());
    }

    @Override
    public boolean safeAt(double doubleValue, double currentDoubleValue) {
      return this.entries.computeIfAbsent(
          new RotationObservationCapture.Terrain.Point(doubleValue, currentDoubleValue),
          item -> this.checkCondition(doubleValue, currentDoubleValue));
    }

    private boolean checkCondition(double doubleValue, double currentDoubleValue) {
      if (Math.hypot(doubleValue, currentDoubleValue) > 3.0) {
        return false;
      } else {
        AABB currentAABB = this.aABB.move(doubleValue, 0.0, currentDoubleValue);
        if (this.checkCondition2(currentAABB)) {
          return true;
        } else {
          return this.value >= 0.5 && this.checkCondition2(currentAABB.move(0.0, 0.5, 0.0))
              ? true
              : this.value > 0.5 && this.checkCondition2(currentAABB.move(0.0, this.value, 0.0));
        }
      }
    }

    private boolean checkCondition2(AABB aABB) {
      if (!this.minecraft2.level.getWorldBorder().isWithinBounds(aABB)) {
        return false;
      }

      for (BlockPos blockPos :
          BlockPos.betweenClosed(
              BlockPos.containing(aABB.minX, aABB.minY - 0.55, aABB.minZ),
              BlockPos.containing(aABB.maxX, aABB.maxY, aABB.maxZ))) {
        if (!this.minecraft2.level.hasChunkAt(blockPos)) {
          return false;
        }

        BlockState blockState = this.minecraft2.level.getBlockState(blockPos);
        if (!blockState.getFluidState().isEmpty()
            || blockState.is(Blocks.FIRE)
            || blockState.is(Blocks.SOUL_FIRE)
            || blockState.is(Blocks.CACTUS)
            || blockState.is(Blocks.MAGMA_BLOCK)
            || blockState.is(Blocks.CAMPFIRE)
            || blockState.is(Blocks.SOUL_CAMPFIRE)
            || blockState.is(Blocks.SWEET_BERRY_BUSH)
            || blockState.is(Blocks.WITHER_ROSE)
            || blockState.is(Blocks.POWDER_SNOW)) {
          return false;
        }
      }

      if (!this.minecraft2.level.noCollision(
          this.minecraft2.player, aABB.deflate(0.0, 1.0E-5, 0.0))) {
        return false;
      }

      double doubleValue = (aABB.minX + aABB.maxX) * 0.5;
      double currentDoubleValue = (aABB.minZ + aABB.maxZ) * 0.5;
      AABB currentAABB =
          new AABB(
              doubleValue - 0.1,
              aABB.minY - 0.55,
              currentDoubleValue - 0.1,
              doubleValue + 0.1,
              aABB.minY - 1.0E-5,
              currentDoubleValue + 0.1);

      for (VoxelShape voxelShape :
          this.minecraft2.level.getBlockCollisions(this.minecraft2.player, currentAABB)) {
        if (!voxelShape.isEmpty()) {
          return true;
        }
      }

      return false;
    }

    private record Point(double x, double z) {}
  }
}
