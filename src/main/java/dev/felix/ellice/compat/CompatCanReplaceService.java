package dev.felix.ellice.compat;

import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.ScaffoldBlockHit;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class CompatCanReplaceService {
  private CompatCanReplaceService() {}

  public static boolean canReplace(
      Minecraft minecraft, BlockCoordinates blockCoordinates, int value) {
    if (minecraft.level != null && minecraft.player != null) {
      BlockPos currentX =
          new BlockPos(blockCoordinates.x(), blockCoordinates.y(), blockCoordinates.z());
      BlockState blockState = minecraft.level.getBlockState(currentX);
      if (value < 0) {
        return blockState.canBeReplaced();
      }

      BlockHitResult blockHitResult =
          new BlockHitResult(Vec3.atCenterOf(currentX), Direction.UP, currentX, false);
      return blockState.canBeReplaced(
          new BlockPlaceContext(
              minecraft.player,
              InteractionHand.MAIN_HAND,
              minecraft.player.getInventory().getItem(value),
              blockHitResult));
    } else {
      return false;
    }
  }

  public static Optional<PlacementCandidate> resolve(
      Minecraft minecraft, ScaffoldBlockHit scaffoldBlockHit, int value) {
    if (minecraft.player != null
        && minecraft.level != null
        && value >= 0
        && !scaffoldBlockHit.inside()) {
      BlockCoordinates blockCoordinates = scaffoldBlockHit.blockPosition();
      BlockHitResult currentX =
          new BlockHitResult(
              new Vec3(
                  scaffoldBlockHit.hitPoint().x(),
                  scaffoldBlockHit.hitPoint().y(),
                  scaffoldBlockHit.hitPoint().z()),
              Direction.valueOf(scaffoldBlockHit.face().name()),
              new BlockPos(blockCoordinates.x(), blockCoordinates.y(), blockCoordinates.z()),
              false);
      BlockPlaceContext blockPlaceContext =
          new BlockPlaceContext(
              minecraft.player,
              InteractionHand.MAIN_HAND,
              minecraft.player.getInventory().getItem(value),
              currentX);
      if (!blockPlaceContext.canPlace()) {
        return Optional.empty();
      }

      BlockPos blockPos = blockPlaceContext.getClickedPos();
      return Optional.of(
          new PlacementCandidate(
              new BlockCoordinates(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
              blockCoordinates,
              scaffoldBlockHit.face(),
              scaffoldBlockHit.hitPoint(),
              false));
    } else {
      return Optional.empty();
    }
  }
}
