package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.feature.survival.SurvivalAllService;
import dev.felix.ellice.feature.task.TaskAction;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class LootScreenController {
   private Screen screen2;
   private AbstractContainerMenu abstractContainerMenu;
   private LootScreenController.BlockPos3Key blockPos3Key;
   private long timestamp;
   private long timestamp2;
   private boolean enabled;
   private boolean enabled2;

   public String tick(TaskAction.Loot loot) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null && minecraft.gameMode != null && minecraft.getConnection() != null) {
         LootScreenController.BlockPos3Key currentX = new LootScreenController.BlockPos3Key(loot.pos().x(), loot.pos().y(), loot.pos().z());
         if (!currentX.equals(this.blockPos3Key)) {
            this.closeIfOwned();
            this.blockPos3Key = currentX;
            this.abstractContainerMenu = null;
            this.enabled2 = false;
         }

         if (this.screen2 != null && minecraft.screen != this.screen2) {
            this.closeIfOwned();
            return "Screen closed";
         }

         if (this.screen2 == null
            && this.enabled2
            && minecraft.screen instanceof AbstractContainerScreen abstractContainerScreen
            && !(abstractContainerScreen instanceof InventoryScreen)
            && !(abstractContainerScreen instanceof CraftingScreen)) {
            this.screen2 = abstractContainerScreen;
            this.abstractContainerMenu = minecraft.player.containerMenu;
            this.enabled2 = false;
         }

         if (minecraft.screen != null && minecraft.screen != this.screen2) {
            return "Waiting for screen";
         } else {
            long longValue = System.currentTimeMillis();
            if (longValue < this.timestamp2) {
               return "Looting food";
            } else {
               return this.screen2 == null ? this.createText(minecraft, currentX) : this.createText2(minecraft, longValue);
            }
         }
      } else {
         return "Waiting for gameplay";
      }
   }

   public void closeIfOwned() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.screen2 == null) {
         this.blockPos3Key = null;
      } else {
         if (minecraft.screen == this.screen2) {
            this.screen2.onClose();
         }

         if (minecraft.screen != this.screen2) {
            this.screen2 = null;
            this.abstractContainerMenu = null;
            this.enabled2 = false;
            this.blockPos3Key = null;
         }
      }
   }

   public void stop() {
      this.closeIfOwned();
      this.blockPos3Key = null;
      this.abstractContainerMenu = null;
      this.enabled2 = false;
      this.timestamp = this.timestamp2 = 0L;
   }

   private String createText(Minecraft minecraft, LootScreenController.BlockPos3Key blockPos3Key) {
      BlockPos currentX = new BlockPos(blockPos3Key.x(), blockPos3Key.y(), blockPos3Key.z());
      Optional result = CompatExecuteService.aim(minecraft, currentX);
      Vec3 vec3 = minecraft.player.getEyePosition();
      Vec3 currentVec3 = Vec3.atCenterOf(currentX);
      CombatOwnsService.claim(CombatOwnsService.Owner.TRAVEL);
      RotationPublishService.publish(
         new RotationData(minecraft.player.getYRot(), minecraft.player.getXRot()),
         RotationData.lookAt(new RotationVector(vec3.x, vec3.y, vec3.z), new RotationVector(currentVec3.x, currentVec3.y, currentVec3.z))
      );
      if (result.isEmpty()) {
         return "Aiming at loot";
      }

      this.enabled = true;

      try {
         if (minecraft.gameMode.useItemOn(minecraft.player, InteractionHand.MAIN_HAND, (BlockHitResult)result.get()).consumesAction()) {
            minecraft.player.swing(InteractionHand.MAIN_HAND);
         }
      } finally {
         this.enabled = false;
      }

      this.enabled2 = true;
      this.timestamp = System.currentTimeMillis() + 5000L;
      this.timestamp2 = System.currentTimeMillis() + 300L;
      return "Opening loot";
   }

   private String createText2(Minecraft minecraft, long currentSize) {
      this.abstractContainerMenu = minecraft.player.containerMenu;
      if (this.abstractContainerMenu != null && this.abstractContainerMenu.slots.size() > 36) {
         if (currentSize >= this.timestamp) {
            this.closeIfOwned();
            return "";
         }

         int nextSize = this.abstractContainerMenu.slots.size() - 36;

         for (int index = 0; index < nextSize; index++) {
            ItemStack itemStack = this.abstractContainerMenu.getSlot(index).getItem();
            if (!itemStack.isEmpty() && SurvivalAllService.isKnownFood(createText3(itemStack))) {
               Slot slot = this.abstractContainerMenu.getSlot(index);
               if (slot.isActive() && slot.mayPickup(minecraft.player)) {
                  if (!this.abstractContainerMenu.getCarried().isEmpty()) {
                     return "Cursor occupied";
                  }

                  InventoryInputActions.clickMenu(minecraft, this.abstractContainerMenu.containerId, index, 0, SoupClickTracker.Click.QUICK_MOVE);
                  this.timestamp2 = System.currentTimeMillis() + 150L;
                  return "Taking " + createText4(createText3(itemStack));
               }
            }
         }

         this.closeIfOwned();
         return "";
      } else {
         this.closeIfOwned();
         return "";
      }
   }

   private static String createText3(ItemStack itemStack) {
      return itemStack.isEmpty() ? "" : BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
   }

   private static String createText4(String text) {
      return text.startsWith("minecraft:") ? text.substring("minecraft:".length()) : text;
   }

   private record BlockPos3Key(int x, int y, int z) {
   }
}

