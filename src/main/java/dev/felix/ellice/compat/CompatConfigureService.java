package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import dev.felix.ellice.feature.tool.ToolActiveService;
import dev.felix.ellice.feature.tool.ToolValueService;
import dev.felix.ellice.feature.tool.ToolChooseService;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class CompatConfigureService {
   private static final ToolActiveService toolActiveService = new ToolActiveService();
   private static boolean enabled;
   private static boolean enabled2;
   private static boolean enabled3;
   private static int count;
   private static BlockPos blockPos;
   private static boolean enabled4;

   private CompatConfigureService() {
   }

   public static void configure(boolean currentEnabled, boolean nextEnabled, int value) {
      cancel();
      enabled = true;
      enabled2 = currentEnabled;
      enabled3 = nextEnabled;
      count = value;
   }

   public static void disable() {
      enabled = false;
      cancel();
   }

   public static void cancel() {
      toolActiveService.release(true);
      blockPos = null;
   }

   public static void beforeInteraction() {
      if (toolActiveService.active()) {
         enabled4 = true;
         cancel();
      }
   }

   public static void beforeAttack() {
      if (toolActiveService.active() && !(Minecraft.getInstance().hitResult instanceof BlockHitResult)) {
         beforeInteraction();
      }
   }

   public static void consumed(KeyMapping keyMapping, boolean enabled) {
      if (enabled && toolActiveService.active()) {
         Options currentOptions = Minecraft.getInstance().options;
         if (keyMapping != currentOptions.keyDrop
            && keyMapping != currentOptions.keySwapOffhand
            && keyMapping != currentOptions.keyPickItem
            && keyMapping != currentOptions.keyUse
            && keyMapping != currentOptions.keyInventory) {
            for (KeyMapping currentKeyMapping : currentOptions.keyHotbarSlots) {
               if (keyMapping == currentKeyMapping) {
                  beforeInteraction();
                  return;
               }
            }
         } else {
            beforeInteraction();
         }
      }
   }

   public static void watchdog() {
      enabled4 = false;
      if (toolActiveService.active()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (!checkCondition(minecraft)
            || !toolActiveService.owned()
            || !minecraft.options.keyAttack.isDown()
            || !minecraft.gameMode.isDestroying()
            || blockPos == null
            || minecraft.level.getBlockState(blockPos).isAir()
            || !(minecraft.hitResult instanceof BlockHitResult blockHitResult && blockHitResult.getBlockPos().equals(blockPos))) {
            cancel();
         }
      }
   }

   public static boolean mine(BlockPos currentBlockPos, Supplier<Boolean> supplier) {
      Minecraft minecraft = Minecraft.getInstance();
      if (!enabled) {
         return (Boolean)supplier.get();
      }

      if (enabled4 || !checkCondition(minecraft)) {
         cancel();
         return (Boolean)supplier.get();
      }

      if (toolActiveService.active() && !toolActiveService.owned()) {
         beforeInteraction();
         return (Boolean)supplier.get();
      }

      if (toolActiveService.active() && !currentBlockPos.equals(blockPos)) {
         cancel();
      }

      BlockState blockState = minecraft.level.getBlockState(currentBlockPos);
      if (!blockState.isAir() && !(blockState.getDestroySpeed(minecraft.level, currentBlockPos) < 0.0F)) {
         int value = calculateValue(minecraft, currentBlockPos);
         if (value < 0) {
            cancel();
            return false;
         }

         if (toolActiveService.active() && toolActiveService.tool() != value) {
            cancel();
         }

         if (!toolActiveService.active()) {
            if (minecraft.gameMode.isDestroying()) {
               minecraft.gameMode.stopDestroyBlock();
            }

            toolActiveService.begin(new CompatConfigureService.Hand(minecraft), value, enabled2, enabled3);
            blockPos = currentBlockPos.immutable();
         }

         boolean currentEnabled = toolActiveService.mine(supplier);
         if (!toolActiveService.active()) {
            blockPos = null;
         }

         return currentEnabled;
      } else {
         cancel();
         return (Boolean)supplier.get();
      }
   }

   public static void synchronize(Runnable runnable) {
      toolActiveService.synchronize(runnable);
   }

   public static void stop(Runnable runnable) {
      try {
         toolActiveService.stop(runnable);
      } finally {
         if (!toolActiveService.active()) {
            blockPos = null;
         }
      }
   }

   private static boolean checkCondition(Minecraft minecraft) {
      return minecraft != null
         && minecraft.player != null
         && minecraft.level != null
         && minecraft.gameMode != null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && !minecraft.player.getAbilities().instabuild
         && minecraft.screen == null
         && !minecraft.player.isUsingItem()
         && !minecraft.options.keyUse.isDown()
         && !SoupInventoryBridge.handBusy()
         && !CompatActivateService.handBusy()
         && !CompatReleaseTracker.handBusy()
         && !PearlThrowController.reserved()
         && !CoreAutoSprintRequestedClient.scaffoldOwnsBlockUse();
   }

   private static int calculateValue(Minecraft minecraft, BlockPos blockPos) {
      BlockState blockState = minecraft.level.getBlockState(blockPos);
      ArrayList arrayList = new ArrayList(9);

      for (int index = 0; index < 9; index++) {
         arrayList.add(minecraft.player.getInventory().getItem(index));
      }

      ArrayList currentArrayList = new ArrayList(9);

      for (int currentIndex = 0; currentIndex < 9; currentIndex++) {
         ItemStack itemStack = (ItemStack)arrayList.get(currentIndex);
         if (itemStack.isItemEnabled(minecraft.level.enabledFeatures())) {
            float currentValue = itemStack.getDestroySpeed(blockState);
            if (currentValue > 1.0F) {
               currentValue += (float)ToolValueService.value(minecraft.player.getAttribute(Attributes.MINING_EFFICIENCY), arrayList, itemStack);
            }

            currentValue *= (float)ToolValueService.value(minecraft.player.getAttribute(Attributes.BLOCK_BREAK_SPEED), arrayList, itemStack);
            if (minecraft.player.isEyeInFluid(FluidTags.WATER)) {
               currentValue *= (float)ToolValueService.value(minecraft.player.getAttribute(Attributes.SUBMERGED_MINING_SPEED), arrayList, itemStack);
            }

            int nextValue = blockState.requiresCorrectToolForDrops() && !itemStack.isCorrectToolForDrops(blockState) ? 0 : 1;
            currentArrayList.add(
               new ToolChooseService.Candidate(
                  currentIndex, (nextValue != 0), currentValue / (nextValue != 0 ? 30 : 100), itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.isDamageableItem()
               )
            );
         }
      }

      return ToolChooseService.choose(currentArrayList, toolActiveService.active() ? toolActiveService.tool() : InventoryInputActions.selected(minecraft), count);
   }

   private static final class Hand implements ToolActiveService.Hand {
      private final Minecraft minecraft2;
      private final LocalPlayer localPlayer;
      private final ClientLevel client2;
      private final MultiPlayerGameMode multiPlayerGameMode;

      private Hand(Minecraft minecraft) {
         this.minecraft2 = minecraft;
         this.localPlayer = minecraft.player;
         this.client2 = minecraft.level;
         this.multiPlayerGameMode = minecraft.gameMode;
      }

      @Override
      public boolean valid() {
         return this.minecraft2.player == this.localPlayer
            && this.minecraft2.level == this.client2
            && this.minecraft2.gameMode == this.multiPlayerGameMode
            && this.minecraft2.getConnection() == this.localPlayer.connection
            && this.localPlayer.connection.getConnection().isConnected();
      }

      @Override
      public int selected() {
         return InventoryInputActions.selected(this.minecraft2);
      }

      @Override
      public void select(int value) {
         InventoryInputActions.select(this.minecraft2, value);
      }

      @Override
      public void sync() {
         this.multiPlayerGameMode.ensureHasSentCarriedItem();
      }

      @Override
      public void abort() {
         this.multiPlayerGameMode.stopDestroyBlock();
      }

      @Override
      public boolean destroying() {
         return this.multiPlayerGameMode.isDestroying();
      }
   }
}

