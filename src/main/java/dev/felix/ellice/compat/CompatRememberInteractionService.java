package dev.felix.ellice.compat;

import dev.felix.ellice.feature.inventory.InventoryEnvironmentTracker;
import dev.felix.ellice.feature.inventory.InventoryRecordService;
import dev.felix.ellice.feature.inventory.InventorySlotData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public final class CompatRememberInteractionService
    implements InventoryEnvironmentTracker.Environment {
  private static long timestamp = Long.MIN_VALUE;
  private final BooleanSupplier booleanSupplier;
  private final BooleanSupplier booleanSupplier2;
  private final BooleanSupplier booleanSupplier3;
  private final IntSupplier intSupplier;
  private AbstractContainerMenu abstractContainerMenu;
  private boolean enabled;
  private final InventoryRecordService inventoryRecordService = new InventoryRecordService();
  private static long timestamp2;

  public CompatRememberInteractionService(
      BooleanSupplier currentBooleanSupplier,
      BooleanSupplier nextBooleanSupplier,
      BooleanSupplier previousBooleanSupplier,
      IntSupplier currentIntSupplier) {
    this.booleanSupplier = currentBooleanSupplier;
    this.booleanSupplier2 = nextBooleanSupplier;
    this.booleanSupplier3 = previousBooleanSupplier;
    this.intSupplier = currentIntSupplier;
  }

  public static void manualInput() {
    timestamp = System.nanoTime() / 1000000L;
  }

  public void reset() {
    this.abstractContainerMenu = null;
    this.enabled = false;
    this.inventoryRecordService.reset();
  }

  public void rememberInteraction(BlockHitResult blockHitResult, boolean enabled) {
    Minecraft minecraft = Minecraft.getInstance();
    if (enabled && minecraft.level != null && minecraft.player != null) {
      this.inventoryRecordService.record(
          minecraft.level,
          minecraft.player.containerMenu,
          createKind(minecraft.level.getBlockState(blockHitResult.getBlockPos()).getBlock()),
          System.nanoTime() / 1000000L);
    } else {
      this.inventoryRecordService.reset();
    }
  }

  private static InventoryRecordService.Kind createKind(Block block) {
    if (block instanceof ShulkerBoxBlock) {
      return InventoryRecordService.Kind.SHULKER;
    } else {
      return !(block instanceof ChestBlock)
              && !(block instanceof BarrelBlock)
              && !(block instanceof EnderChestBlock)
          ? null
          : InventoryRecordService.Kind.CHEST;
    }
  }

  @Override
  public Optional<InventorySlotData> capture() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.getConnection() != null
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()
        && minecraft.screen instanceof AbstractContainerScreen abstractContainerScreen) {
      AbstractContainerMenu currentAbstractContainerMenu = abstractContainerScreen.getMenu();
      int value =
          currentAbstractContainerMenu instanceof ChestMenu chestMenu
              ? chestMenu.getRowCount() * 9
              : (currentAbstractContainerMenu instanceof ShulkerBoxMenu
                      && this.booleanSupplier2.getAsBoolean()
                  ? 27
                  : 0);
      if (value != 0
          && currentAbstractContainerMenu == minecraft.player.containerMenu
          && currentAbstractContainerMenu.slots.size() == value + 36
          && currentAbstractContainerMenu.stillValid(minecraft.player)) {
        if (this.abstractContainerMenu != currentAbstractContainerMenu) {
          this.abstractContainerMenu = currentAbstractContainerMenu;
          InventoryRecordService.Kind kind =
              currentAbstractContainerMenu instanceof ShulkerBoxMenu
                  ? InventoryRecordService.Kind.SHULKER
                  : InventoryRecordService.Kind.CHEST;
          this.enabled =
              this.inventoryRecordService.consume(
                  minecraft.level,
                  currentAbstractContainerMenu,
                  kind,
                  System.nanoTime() / 1000000L);
          if (!this.enabled && minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            this.enabled =
                createKind(minecraft.level.getBlockState(blockHitResult.getBlockPos()).getBlock())
                    == kind;
          }
        }

        if (this.booleanSupplier.getAsBoolean() && !this.enabled) {
          return Optional.empty();
        }

        List<Slot> currentSize =
            currentAbstractContainerMenu.slots.subList(
                value, currentAbstractContainerMenu.slots.size());
        if (currentSize.stream()
            .anyMatch(item -> item.container != minecraft.player.getInventory())) {
          return Optional.empty();
        }

        ArrayList arrayList = new ArrayList();

        for (Slot slot : (Iterable<Slot>) (Iterable<?>) (currentSize)) {
          if (slot.hasItem()) {
            arrayList.add(ItemStackDecoder.read(slot.getItem()));
          }
        }

        for (EquipmentSlot equipmentSlot :
            List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET,
                EquipmentSlot.OFFHAND)) {
          ItemStack itemStack = minecraft.player.getItemBySlot(equipmentSlot);
          if (!itemStack.isEmpty()) {
            arrayList.add(ItemStackDecoder.read(itemStack));
          }
        }

        ArrayList currentArrayList = new ArrayList();
        int currentCount = (int) currentSize.stream().filter(item -> !item.hasItem()).count();

        for (int index = 0; index < value; index++) {
          Slot currentSlot = (Slot) currentAbstractContainerMenu.slots.get(index);
          if (currentSlot.hasItem()) {
            int[] ints = capacity(currentSlot.getItem(), currentSize);
            currentArrayList.add(
                new InventorySlotData.Slot(
                    index,
                    currentSlot.x,
                    currentSlot.y,
                    ItemStackDecoder.read(currentSlot.getItem()),
                    currentSlot.isActive() && currentSlot.mayPickup(minecraft.player),
                    ints[0],
                    currentCount,
                    ints[1]));
          }
        }

        long longValue = CompatAdapterService.windowHandle(minecraft);
        int currentValue =
            this.booleanSupplier3.getAsBoolean()
                    && (this.intSupplier.getAsInt() < 0
                        || GLFW.glfwGetKey(longValue, this.intSupplier.getAsInt()) != 1)
                ? 0
                : 1;
        return Optional.of(
            new InventorySlotData(
                currentAbstractContainerMenu,
                currentArrayList,
                arrayList,
                paused(minecraft, longValue),
                !currentAbstractContainerMenu.getCarried().isEmpty(),
                (currentValue != 0)));
      } else {
        return Optional.empty();
      }
    } else {
      this.abstractContainerMenu = null;
      return Optional.empty();
    }
  }

  @Override
  public boolean quickMove(InventorySlotData inventorySlotData, InventorySlotData.Slot slot) {
    Minecraft minecraft = Minecraft.getInstance();
    InventorySlotData currentInventorySlotData = this.capture().orElse(null);
    if (currentInventorySlotData != null
        && currentInventorySlotData.menu() == inventorySlotData.menu()
        && !currentInventorySlotData.paused()
        && !currentInventorySlotData.carriedItem()
        && currentInventorySlotData.activationHeld()
        && currentInventorySlotData.owned().equals(inventorySlotData.owned())
        && !currentInventorySlotData.slots().stream().noneMatch(slot::equals)) {
      ContainerInputActions.quickMove(
          minecraft, ((AbstractContainerMenu) inventorySlotData.menu()).containerId, slot.index());
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void close(InventorySlotData inventorySlotData) {
    InventorySlotData currentInventorySlotData = this.capture().orElse(null);
    if (currentInventorySlotData != null
        && currentInventorySlotData.menu() == inventorySlotData.menu()
        && !currentInventorySlotData.paused()
        && !currentInventorySlotData.carriedItem()
        && currentInventorySlotData.activationHeld()
        && currentInventorySlotData.slots().equals(inventorySlotData.slots())
        && currentInventorySlotData.owned().equals(inventorySlotData.owned())) {
      Minecraft.getInstance().player.closeContainer();
    }
  }

  static boolean paused(Minecraft minecraft, long longValue) {
    long currentLongValue = System.nanoTime() / 1000000L;

    for (int index = 0; index <= 2; index++) {
      if (GLFW.glfwGetMouseButton(longValue, index) == 1) {
        timestamp2 = currentLongValue + 350L;
      }
    }

    return minecraft.isPaused()
        || !minecraft.isWindowActive()
        || minecraft.getOverlay() != null
        || currentLongValue < timestamp2
        || timestamp != Long.MIN_VALUE && currentLongValue - timestamp < 350L;
  }

  static int[] capacity(ItemStack itemStack, List<Slot> items) {
    int value = itemStack.getCount();
    int currentCount = (int) items.stream().filter(item -> !item.hasItem()).count();
    if (itemStack.isStackable()) {
      for (int currentSize = items.size() - 1; currentSize >= 0 && value > 0; currentSize += -1) {
        Slot slot = (Slot) items.get(currentSize);
        if (ItemStack.isSameItemSameComponents(itemStack, slot.getItem())) {
          value -=
              Math.min(
                  value,
                  Math.max(0, slot.getMaxStackSize(slot.getItem()) - slot.getItem().getCount()));
        }
      }
    }

    if (value > 0) {
      for (int nextSize = items.size() - 1; nextSize >= 0 && value > 0; nextSize += -1) {
        Slot currentSlot = (Slot) items.get(nextSize);
        if (!currentSlot.hasItem()
            && currentSlot.mayPlace(itemStack)
            && currentSlot.getMaxStackSize(itemStack) > 0) {
          value -= Math.min(value, currentSlot.getMaxStackSize(itemStack));
          currentCount += -1;
        }
      }
    }

    return new int[] {itemStack.getCount() - value, currentCount};
  }
}
