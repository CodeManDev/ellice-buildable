package dev.felix.ellice.compat;

import dev.felix.ellice.feature.notebot.NotebotPositionData;
import dev.felix.ellice.feature.notebot.NotebotAvailableHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class CompatContextService implements NotebotAvailableHandler {
   @Override
   public NotebotAvailableHandler.Context context() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null && minecraft.player != null && minecraft.gameMode != null && minecraft.getConnection() != null) {
         String text = "";
         if (!minecraft.player.isAlive()) {
            text = "Respawn before playing.";
         } else if (minecraft.gameMode.getPlayerMode() != GameType.SURVIVAL) {
            text = "Switch to Survival. Creative attacks would break note blocks.";
         } else if (!minecraft.player.getMainHandItem().isEmpty()) {
            text = "Select an empty hotbar slot.";
         } else if (minecraft.player.isShiftKeyDown() || minecraft.player.isHandsBusy()) {
            text = "Release sneak and item use before playing.";
         } else if (minecraft.gameMode.isDestroying()) {
            text = "Release the attack button before playing.";
         }

         return new NotebotAvailableHandler.Context(minecraft.level, text, minecraft.isPaused());
      } else {
         return new NotebotAvailableHandler.Context(null, "Join a world to find note blocks.", false);
      }
   }

   @Override
   public NotebotAvailableHandler.Scan scan() {
      Minecraft minecraft = Minecraft.getInstance();
      NotebotAvailableHandler.Context currentContext = this.context();
      if (minecraft.player != null && minecraft.level != null) {
         int value = (int)Math.ceil(Math.min(6.0, minecraft.player.blockInteractionRange()));
         BlockPos blockPos = minecraft.player.blockPosition();
         ArrayList<NotebotPositionData> arrayList = new ArrayList<>();
         int index = 0;
         int currentIndex = 0;

         for (BlockPos currentBlockPos : BlockPos.betweenClosed(blockPos.offset(-value, -value, -value), blockPos.offset(value, value, value))) {
            if (minecraft.level.hasChunkAt(currentBlockPos)) {
               BlockState blockState = minecraft.level.getBlockState(currentBlockPos);
               if (blockState.is(Blocks.NOTE_BLOCK)) {
                  NotebotPositionData notebotPositionData = createNotebotPositionData(currentBlockPos);
                  if (!notebotPositionData.melodic()) {
                     currentIndex++;
                  } else if (minecraft.level.getBlockState(currentBlockPos.above()).isAir() && !findResult(currentBlockPos).isEmpty()) {
                     arrayList.add(notebotPositionData);
                  } else {
                     index++;
                  }
               }
            }
         }

         Vec3 vec3 = minecraft.player.getEyePosition();
         arrayList.sort(Comparator.comparingDouble(item -> Vec3.atCenterOf(createBlockPos(item.position())).distanceToSqr(vec3)));
         return new NotebotAvailableHandler.Scan(currentContext, arrayList, index, currentIndex);
      } else {
         return new NotebotAvailableHandler.Scan(currentContext, List.of(), 0, 0);
      }
   }

   @Override
   public Optional<NotebotPositionData> inspect(NotebotPositionData.Position position) {
      Minecraft minecraft = Minecraft.getInstance();
      BlockPos blockPos = createBlockPos(position);
      if (minecraft.level != null
         && minecraft.player != null
         && minecraft.level.hasChunkAt(blockPos)
         && minecraft.level.getBlockState(blockPos).is(Blocks.NOTE_BLOCK)
         && minecraft.level.getBlockState(blockPos.above()).isAir()
         && !findResult(blockPos).isEmpty()) {
         NotebotPositionData notebotPositionData = createNotebotPositionData(blockPos);
         return notebotPositionData.melodic() ? Optional.of(notebotPositionData) : Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   @Override
   public boolean tune(NotebotPositionData notebotPositionData) {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.context().available() && !this.inspect(notebotPositionData.position()).filter(notebotPositionData::equals).isEmpty()) {
         Optional result = findResult(createBlockPos(notebotPositionData.position()));
         int value = result.isPresent()
               && minecraft.gameMode.useItemOn(minecraft.player, InteractionHand.MAIN_HAND, (BlockHitResult)result.get()).consumesAction()
            ? 1
            : 0;
         if (value != 0) {
            minecraft.player.swing(InteractionHand.MAIN_HAND);
         }

         return (value != 0);
      } else {
         return false;
      }
   }

   @Override
   public boolean play(NotebotPositionData notebotPositionData) {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.context().available() && !this.inspect(notebotPositionData.position()).filter(notebotPositionData::equals).isEmpty()) {
         BlockPos blockPos = createBlockPos(notebotPositionData.position());
         if (minecraft.level.getBlockState(blockPos).getDestroyProgress(minecraft.player, minecraft.level, blockPos) >= 1.0F) {
            return false;
         }

         Optional result = findResult(blockPos);
         if (result.isEmpty()) {
            return false;
         }

         try {
            boolean enabled = minecraft.gameMode.startDestroyBlock(blockPos, ((BlockHitResult)result.get()).getDirection());
            if (enabled) {
               minecraft.player.swing(InteractionHand.MAIN_HAND);
            }

            return enabled;
         } finally {
            minecraft.gameMode.stopDestroyBlock();
         }
      } else {
         return false;
      }
   }

   private static NotebotPositionData createNotebotPositionData(BlockPos blockPos) {
      BlockState blockState = Minecraft.getInstance().level.getBlockState(blockPos);
      return new NotebotPositionData(
         new NotebotPositionData.Position(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
         ((NoteBlockInstrument)blockState.getValue(NoteBlock.INSTRUMENT)).getSerializedName(),
         (Integer)blockState.getValue(NoteBlock.NOTE)
      );
   }

   private static Optional<BlockHitResult> findResult(BlockPos blockPos) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null) {
         Vec3 vec3 = minecraft.player.getEyePosition();
         double doubleValue = minecraft.player.blockInteractionRange();
         Vec3 currentVec3 = Vec3.atCenterOf(blockPos);

         for (Direction direction : Direction.values()) {
            Vec3 nextVec3 = currentVec3.add(
               direction.getStepX() * 0.499,
               direction.getStepY() * 0.499,
               direction.getStepZ() * 0.499
            );
            if (!(vec3.distanceToSqr(nextVec3) > doubleValue * doubleValue)) {
               BlockHitResult blockHitResult = minecraft.level.clip(new ClipContext(vec3, nextVec3, Block.OUTLINE, Fluid.NONE, minecraft.player));
               if (blockHitResult.getType() == Type.BLOCK && blockHitResult.getBlockPos().equals(blockPos)) {
                  return Optional.of(blockHitResult);
               }
            }
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   private static BlockPos createBlockPos(NotebotPositionData.Position position) {
      return new BlockPos(position.x(), position.y(), position.z());
   }
}
