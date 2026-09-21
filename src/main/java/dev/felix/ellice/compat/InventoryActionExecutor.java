package dev.felix.ellice.compat;

import dev.felix.ellice.feature.inventory.InventoryActionTracker;
import dev.felix.ellice.feature.inventory.InventoryPlanService;
import dev.felix.ellice.feature.inventory.InventoryCellData;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

public final class InventoryActionExecutor implements InventoryActionTracker.Environment {
   @Override
   public Optional<InventoryCellData> capture() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null
         && minecraft.level != null
         && minecraft.gameMode != null
         && minecraft.getConnection() != null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && minecraft.screen instanceof InventoryScreen inventoryScreen
         && inventoryScreen.getMenu() == minecraft.player.inventoryMenu
         && minecraft.player.containerMenu == minecraft.player.inventoryMenu) {
         InventoryMenu currentInventoryMenu = minecraft.player.inventoryMenu;
         ArrayList arrayList = new ArrayList();

         for (int index = 5; index <= 45; index++) {
            Slot slot = currentInventoryMenu.getSlot(index);
            arrayList.add(
               new InventoryCellData.Cell(
                  index, slot.hasItem() ? ItemStackDecoder.read(slot.getItem()) : null, slot.isActive() && slot.mayPickup(minecraft.player)
               )
            );
         }

         return Optional.of(
            new InventoryCellData(
               currentInventoryMenu,
               arrayList,
               CompatRememberInteractionService.paused(minecraft, CompatAdapterService.windowHandle(minecraft)) || minecraft.player.isUsingItem(),
               currentInventoryMenu.getCarried().isEmpty()
            )
         );
      } else {
         return Optional.empty();
      }
   }

   @Override
   public boolean execute(InventoryCellData inventoryCellData, InventoryPlanService.Action action) {
      InventoryCellData currentInventoryCellData = this.capture().orElse(null);
      if (currentInventoryCellData != null
         && currentInventoryCellData.menu() == inventoryCellData.menu()
         && !currentInventoryCellData.paused()
         && currentInventoryCellData.cursorEmpty()
         && currentInventoryCellData.cells().equals(inventoryCellData.cells())
         && action.matches(currentInventoryCellData)) {
         Minecraft minecraft = Minecraft.getInstance();
         InventoryMenu currentInventoryMenu = minecraft.player.inventoryMenu;
         Slot slot = currentInventoryMenu.getSlot(action.source());
         if (action.input() != InventoryPlanService.Input.THROW) {
            Slot currentSlot = currentInventoryMenu.getSlot(action.destination());
            if (!slot.hasItem() || slot.mayPickup(minecraft.player) && currentSlot.mayPlace(slot.getItem())) {
               if (!currentSlot.hasItem() || currentSlot.mayPickup(minecraft.player) && slot.mayPlace(currentSlot.getItem())) {
                  if (action.input() == InventoryPlanService.Input.QUICK_MOVE) {
                     if (currentSlot.hasItem() || !slot.hasItem()) {
                        return false;
                     }

                     ContainerInputActions.quickMove(minecraft, currentInventoryMenu.containerId, action.source());
                  } else {
                     if (action.destination() < 36 || action.destination() > 44) {
                        return false;
                     }

                     ContainerInputActions.swap(minecraft, currentInventoryMenu.containerId, action.source(), action.destination() - 36);
                  }

                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else if (action.source() >= 9 && action.source() <= 44 && slot.hasItem() && slot.isActive() && slot.mayPickup(minecraft.player)) {
            ContainerInputActions.throwStack(minecraft, currentInventoryMenu.containerId, action.source());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
