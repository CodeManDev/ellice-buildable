package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.feature.task.TaskAction;
import dev.felix.ellice.feature.task.TaskResolveService;
import dev.felix.ellice.feature.task.TaskData;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class CompatStateController {
   private Screen screen2;
   private AbstractContainerMenu abstractContainerMenu;
   private TaskResolveService.Station station;
   private String text = "";
   private CompatStateController.Phase phase = CompatStateController.Phase.OPEN;
   private boolean enabled;
   private int count2;
   private long timestamp;
   private long timestamp2;
   private boolean enabled2;

   public String tick(TaskAction.Craft craft, TaskResolveService.Station currentStation, TaskData taskData) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null && minecraft.gameMode != null && minecraft.getConnection() != null) {
         if (!craft.outputId().equals(this.text)) {
            this.closeIfOwned();
            this.updateState();
            this.text = craft.outputId();
            this.station = currentStation;
         }

         if (this.screen2 != null && minecraft.screen != this.screen2) {
            this.updateState();
            return "Screen closed";
         }

         if (this.screen2 == null && this.enabled && minecraft.screen instanceof CraftingScreen craftingScreen) {
            this.screen2 = craftingScreen;
            this.abstractContainerMenu = minecraft.player.containerMenu;
            this.enabled = false;
            this.phase = CompatStateController.Phase.OPEN;
         }

         if (minecraft.screen != null && minecraft.screen != this.screen2) {
            return "Waiting for screen";
         }

         long longValue = System.currentTimeMillis();
         if (longValue < this.timestamp2) {
            return this.createText();
         }

         if (!this.checkCondition2(minecraft)) {
            return this.createText2(minecraft);
         }

         if (this.phase == CompatStateController.Phase.OPEN && !this.checkCondition3(minecraft)) {
            return "Clear the crafting grid";
         }

         return switch (this.phase) {
            case OPEN -> this.createText3(minecraft, taskData, longValue);
            case PLACE -> this.createText4(minecraft, taskData, longValue);
            case TAKE -> this.createText5(minecraft, taskData, longValue);
            case CONFIRM -> this.createText6(taskData, longValue);
         };
      } else {
         return "Waiting for gameplay";
      }
   }

   public Optional<TaskAction.Craft> pendingCommand() {
      return this.screen2 != null
            && Minecraft.getInstance().screen == this.screen2
            && this.phase != CompatStateController.Phase.OPEN
            && !this.text.isEmpty()
         ? Optional.of(new TaskAction.Craft(this.text))
         : Optional.empty();
   }

   public void closeIfOwned() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.screen2 != null) {
         if (minecraft.screen == this.screen2) {
            this.screen2.onClose();
         }

         if (minecraft.screen != this.screen2) {
            this.screen2 = null;
            this.abstractContainerMenu = null;
            this.enabled = false;
            this.phase = CompatStateController.Phase.OPEN;
         }
      }
   }

   public void stop() {
      this.closeIfOwned();
      this.updateState();
   }

   private void updateState() {
      this.screen2 = null;
      this.abstractContainerMenu = null;
      this.text = "";
      this.phase = CompatStateController.Phase.OPEN;
      this.enabled = false;
      this.count2 = 0;
      this.timestamp = this.timestamp2 = 0L;
   }

   private String createText() {
      return switch (this.phase) {
         case OPEN -> "Opening crafting";
         case PLACE -> "Placing recipe";
         case TAKE -> "Taking result";
         case CONFIRM -> "Confirming result";
      };
   }

   private boolean checkCondition(Screen screen) {
      return this.station == TaskResolveService.Station.INVENTORY ? screen instanceof InventoryScreen : screen instanceof CraftingScreen;
   }

   private boolean checkCondition2(Minecraft minecraft) {
      return this.screen2 != null && minecraft.screen == this.screen2 && this.checkCondition(this.screen2);
   }

   private String createText2(Minecraft minecraft) {
      if (this.screen2 != null) {
         this.closeIfOwned();
         return "Switching screen";
      }

      if (this.station == TaskResolveService.Station.INVENTORY) {
         if (!minecraft.player.isUsingItem() && minecraft.player.inventoryMenu.getCarried().isEmpty()) {
            this.screen2 = new InventoryScreen(minecraft.player);
            minecraft.setScreen(this.screen2);
            this.abstractContainerMenu = minecraft.player.containerMenu;
            this.phase = CompatStateController.Phase.OPEN;
            this.timestamp = System.currentTimeMillis() + 3000L;
            this.timestamp2 = System.currentTimeMillis() + 150L;
            return "Opening inventory";
         } else {
            return "Waiting for current item use";
         }
      } else {
         BlockPos blockPos = createBlockPos(minecraft);
         if (blockPos == null) {
            return "Need a placed table nearby";
         }

         Optional result = CompatExecuteService.aim(minecraft, blockPos);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 currentVec3 = Vec3.atCenterOf(blockPos);
         CombatOwnsService.claim(CombatOwnsService.Owner.TRAVEL);
         RotationPublishService.publish(
            new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()),
            RotationData.lookAt(new RotationVector(vec3.x, vec3.y, vec3.z), new RotationVector(currentVec3.x, currentVec3.y, currentVec3.z))
         );
         if (result.isEmpty()) {
            return "Aiming at table";
         }

         this.enabled2 = true;

         try {
            if (minecraft.gameMode.useItemOn(minecraft.player, InteractionHand.MAIN_HAND, (BlockHitResult)result.get()).consumesAction()) {
               minecraft.player.swing(InteractionHand.MAIN_HAND);
            }
         } finally {
            this.enabled2 = false;
         }

         this.enabled = true;
         this.timestamp = System.currentTimeMillis() + 3000L;
         this.timestamp2 = System.currentTimeMillis() + 200L;
         return "Opening table";
      }
   }

   private boolean checkCondition3(Minecraft minecraft) {
      this.abstractContainerMenu = minecraft.player.containerMenu;
      if (this.abstractContainerMenu != null && this.abstractContainerMenu.slots.size() == 46) {
         if (!this.abstractContainerMenu.getCarried().isEmpty()) {
            return false;
         }

         int value = this.station == TaskResolveService.Station.INVENTORY ? 4 : 9;

         for (int index = 1; index <= value; index++) {
            if (!this.abstractContainerMenu.getSlot(index).getItem().isEmpty()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private String createText3(Minecraft minecraft, TaskData taskData, long currentSize) {
      if (this.abstractContainerMenu != null
         && this.abstractContainerMenu.slots.size() == 46
         && createText7(this.abstractContainerMenu.getSlot(0).getItem()).equals(this.text)) {
         this.phase = CompatStateController.Phase.TAKE;
         return this.createText5(minecraft, taskData, currentSize);
      }

      if (!this.checkCondition4(minecraft)) {
         return "Recipe locked — craft it manually once";
      }

      this.phase = CompatStateController.Phase.PLACE;
      this.timestamp = System.currentTimeMillis() + 3000L;
      this.timestamp2 = System.currentTimeMillis() + 200L;
      return "Placing recipe";
   }

   private String createText4(Minecraft minecraft, TaskData taskData, long currentSize) {
      this.abstractContainerMenu = minecraft.player.containerMenu;
      if (this.abstractContainerMenu != null
         && this.abstractContainerMenu.slots.size() == 46
         && createText7(this.abstractContainerMenu.getSlot(0).getItem()).equals(this.text)) {
         this.phase = CompatStateController.Phase.TAKE;
         return this.createText5(minecraft, taskData, currentSize);
      } else if (currentSize >= this.timestamp) {
         this.stop();
         return "Server did not supply a recipe";
      } else {
         return "Waiting for result";
      }
   }

   private String createText5(Minecraft minecraft, TaskData taskData, long currentSize) {
      this.abstractContainerMenu = minecraft.player.containerMenu;
      if (this.abstractContainerMenu != null
         && this.abstractContainerMenu.slots.size() == 46
         && createText7(this.abstractContainerMenu.getSlot(0).getItem()).equals(this.text)) {
         Slot slot = this.abstractContainerMenu.getSlot(0);
         if (slot.isActive() && (!slot.hasItem() || slot.mayPickup(minecraft.player))) {
            this.count2 = taskData.count(this.text);
            InventoryInputActions.clickMenu(minecraft, this.abstractContainerMenu.containerId, 0, 0, SoupClickTracker.Click.QUICK_MOVE);
            this.phase = CompatStateController.Phase.CONFIRM;
            this.timestamp = System.currentTimeMillis() + 3000L;
            this.timestamp2 = System.currentTimeMillis() + 200L;
            return "Taking result";
         } else {
            return "Result blocked";
         }
      } else {
         return this.createText4(minecraft, taskData, currentSize);
      }
   }

   private String createText6(TaskData taskData, long size) {
      if (taskData.count(this.text) > this.count2) {
         this.closeIfOwned();
         this.updateState();
         return "";
      } else if (size >= this.timestamp) {
         this.stop();
         return "Craft was not confirmed";
      } else {
         return "Confirming result";
      }
   }

   private boolean checkCondition4(Minecraft minecraft) {
      StackedItemContents stackedItemContents = new StackedItemContents();
      minecraft.player.getInventory().fillStackedContents(stackedItemContents);
      ContextMap contextMap = SlotDisplayContext.fromLevel(minecraft.level);

      for (RecipeCollection recipeCollection : minecraft.player.getRecipeBook().getCollections()) {
         for (RecipeDisplayEntry recipeDisplayEntry : recipeCollection.getRecipes()) {
            if (recipeDisplayEntry.canCraft(stackedItemContents)) {
               List items = recipeDisplayEntry.resultItems(contextMap);
               if (items.size() == 1 && createText7((ItemStack)items.getFirst()).equals(this.text)) {
                  this.enabled2 = true;

                  try {
                     minecraft.gameMode.handlePlaceRecipe(this.abstractContainerMenu.containerId, recipeDisplayEntry.id(), false);
                  } finally {
                     this.enabled2 = false;
                  }

                  return true;
               }
            }
         }
      }

      return false;
   }

   private static BlockPos createBlockPos(Minecraft minecraft) {
      BlockPos blockPos = minecraft.player.blockPosition();
      BlockPos currentBlockPos = null;
      double doubleValue = 25.0;

      for (int index = blockPos.getX() - 5; index <= blockPos.getX() + 5; index++) {
         for (int currentIndex = blockPos.getY() - 3; currentIndex <= blockPos.getY() + 3; currentIndex++) {
            for (int nextIndex = blockPos.getZ() - 5; nextIndex <= blockPos.getZ() + 5; nextIndex++) {
               BlockPos nextBlockPos = new BlockPos(index, currentIndex, nextIndex);
               if (minecraft.level.hasChunkAt(nextBlockPos) && minecraft.level.getBlockState(nextBlockPos).is(Blocks.CRAFTING_TABLE)) {
                  double currentDoubleValue = minecraft.player
                     .distanceToSqr(
                        index + 0.5,
                        currentIndex + 0.5,
                        nextIndex + 0.5
                     );
                  if (currentDoubleValue < doubleValue) {
                     doubleValue = currentDoubleValue;
                     currentBlockPos = nextBlockPos.immutable();
                  }
               }
            }
         }
      }

      return currentBlockPos;
   }

   private static String createText7(ItemStack itemStack) {
      return itemStack.isEmpty() ? "" : BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
   }

   private enum Phase {
      OPEN,
      PLACE,
      TAKE,
      CONFIRM;


      private static CompatStateController.Phase[] $values() {
         return new CompatStateController.Phase[]{OPEN, PLACE, TAKE, CONFIRM};
      }
   }
}

