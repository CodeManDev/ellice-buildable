package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.ScaffoldBlockHit;
import dev.felix.ellice.feature.scaffold.ScaffoldComponent;
import dev.felix.ellice.feature.scaffold.ScaffoldPlacementHeadingService;
import dev.felix.ellice.feature.scaffold.ScaffoldPlayerSnapshot;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.mixin.MinecraftAttackAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CompatPrepareService {
  private static BlockHitResult blockHitResult;
  private static LocalPlayer localPlayer;

  private CompatPrepareService() {}

  public static void clear() {
    blockHitResult = null;
    localPlayer = null;
  }

  public static RotationData prepare(
      Minecraft minecraft,
      ScaffoldCompatibility scaffoldCompatibility,
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.Input input,
      RotationData rotationData,
      double doubleValue,
      double currentDoubleValue) {
    clear();
    if (scaffoldPlayerSnapshot.onGround()
        && input.hasHorizontalIntent()
        && !minecraft.options.keyAttack.isDown()
        && !ScaffoldComponent.manualItemUse(minecraft)
        && !minecraft.player.isHandsBusy()) {
      ScaffoldRemapService.WorldVector currentWorldVector =
          ScaffoldRemapService.worldVector(
              scaffoldPlayerSnapshot.playerRotation().yaw(), input.forwardAxis(), input.leftAxis());
      AABB currentX =
          minecraft
              .player
              .getBoundingBox()
              .expandTowards(currentWorldVector.x() * 0.9, 0.0, currentWorldVector.z() * 0.9);
      BlockPos blockPos = null;
      RotationVector rotationVector = null;
      double nextDoubleValue = Double.POSITIVE_INFINITY;

      for (BlockPos currentBlockPos :
          BlockPos.betweenClosed(
              (int) Math.floor(currentX.minX),
              (int) Math.floor(scaffoldPlayerSnapshot.feetPosition().y()),
              (int) Math.floor(currentX.minZ),
              (int) Math.floor(currentX.maxX),
              (int) Math.floor(currentX.maxY - 1.0E-4),
              (int) Math.floor(currentX.maxZ))) {
        BlockState blockState = minecraft.level.getBlockState(currentBlockPos);
        if (blockState.getBlock() instanceof SnowLayerBlock
            || blockState.is(Blocks.SNOW_BLOCK)
            || blockState.canBeReplaced() && blockState.getFluidState().isEmpty()) {
          VoxelShape voxelShape = blockState.getShape(minecraft.level, currentBlockPos);
          if (!voxelShape.isEmpty()) {
            Vec3 nextX =
                voxelShape
                    .bounds()
                    .getCenter()
                    .add(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ());
            double previousDoubleValue = nextX.distanceToSqr(minecraft.player.getEyePosition());
            if (previousDoubleValue < nextDoubleValue) {
              blockPos = currentBlockPos.immutable();
              rotationVector = new RotationVector(nextX.x, nextX.y, nextX.z());
              nextDoubleValue = previousDoubleValue;
            }
          }
        }
      }

      if (blockPos != null && !(nextDoubleValue > doubleValue * doubleValue)) {
        RotationData currentRotationData =
            RotationObserveService.current().orElse(scaffoldPlayerSnapshot.playerRotation());
        RotationData nextRotationData = rotationData == null ? currentRotationData : rotationData;
        RotationObserveService.Snapshot currentSnapshot =
            RotationObserveService.snapshot().orElse(null);
        ScaffoldBlockHit scaffoldBlockHit =
            scaffoldCompatibility
                .raycastBlockFromEye(
                    minecraft,
                    currentRotationData,
                    doubleValue,
                    scaffoldPlayerSnapshot.eyePosition())
                .orElse(null);
        ScaffoldBlockHit currentScaffoldBlockHit =
            currentSnapshot == null
                ? null
                : scaffoldCompatibility
                    .raycastBlockFromEye(
                        minecraft,
                        currentRotationData,
                        doubleValue,
                        currentSnapshot.wireEyePosition())
                    .orElse(null);
        BlockCoordinates previousX =
            new BlockCoordinates(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (scaffoldBlockHit != null
            && !scaffoldBlockHit.inside()
            && scaffoldBlockHit.blockPosition().equals(previousX)
            && currentScaffoldBlockHit != null
            && !currentScaffoldBlockHit.inside()
            && currentScaffoldBlockHit.blockPosition().equals(previousX)) {
          localPlayer = minecraft.player;
          blockHitResult =
              new BlockHitResult(
                  new Vec3(
                      scaffoldBlockHit.hitPoint().x(),
                      scaffoldBlockHit.hitPoint().y(),
                      scaffoldBlockHit.hitPoint().z()),
                  Direction.valueOf(scaffoldBlockHit.face().name()),
                  blockPos,
                  false);
          return currentRotationData;
        } else {
          return ScaffoldPlacementHeadingService.turnToward(
              nextRotationData,
              RotationData.lookAt(scaffoldPlayerSnapshot.eyePosition(), rotationVector),
              scaffoldPlayerSnapshot.mouseSensitivity(),
              currentDoubleValue);
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public static void continueAttack(Minecraft minecraft, boolean enabled) {
    BlockHitResult currentBlockHitResult = blockHitResult;
    if (currentBlockHitResult != null
        && localPlayer == minecraft.player
        && CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)
        && minecraft.screen == null
        && !minecraft.player.isUsingItem()
        && !enabled) {
      HitResult currentHitResult = minecraft.hitResult;

      try {
        minecraft.hitResult = currentBlockHitResult;
        ((MinecraftAttackAccess) minecraft).ellice$continueAttack(true);
      } finally {
        minecraft.hitResult = currentHitResult;
      }
    } else {
      ((MinecraftAttackAccess) minecraft).ellice$continueAttack(enabled);
    }
  }
}
