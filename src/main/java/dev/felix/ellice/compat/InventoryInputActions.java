package dev.felix.ellice.compat;

import dev.felix.ellice.feature.soup.SoupClickTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerInput;

final class InventoryInputActions {
  private InventoryInputActions() {}

  static int selected(Minecraft minecraft) {
    return minecraft.player.getInventory().getSelectedSlot();
  }

  static void select(Minecraft minecraft, int value) {
    minecraft.player.getInventory().setSelectedSlot(value);
  }

  static void click(
      Minecraft minecraft, int value, int currentValue, SoupClickTracker.Click currentClick) {
    clickMenu(minecraft, 0, value, currentValue, currentClick);
  }

  static void clickMenu(
      Minecraft minecraft,
      int value,
      int currentValue,
      int nextValue,
      SoupClickTracker.Click click) {
    ContainerInput containerInput =
        switch (click) {
          case PICKUP -> ContainerInput.PICKUP;
          case QUICK_MOVE -> ContainerInput.QUICK_MOVE;
          case SWAP -> ContainerInput.SWAP;
        };
    minecraft.gameMode.handleContainerInput(
        value, currentValue, nextValue, containerInput, minecraft.player);
  }
}
