package dev.felix.ellice.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerInput;

final class ContainerInputActions {
  private ContainerInputActions() {}

  static void throwStack(Minecraft minecraft, int value, int currentValue) {
    minecraft.gameMode.handleContainerInput(
        value, currentValue, 1, ContainerInput.THROW, minecraft.player);
  }

  static void quickMove(Minecraft minecraft, int value, int currentValue) {
    minecraft.gameMode.handleContainerInput(
        value, currentValue, 0, ContainerInput.QUICK_MOVE, minecraft.player);
  }

  static void swap(Minecraft minecraft, int value, int currentValue, int nextValue) {
    minecraft.gameMode.handleContainerInput(
        value, currentValue, nextValue, ContainerInput.SWAP, minecraft.player);
  }
}
