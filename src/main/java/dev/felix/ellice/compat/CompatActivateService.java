package dev.felix.ellice.compat;

import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.feature.survival.SurvivalAllService;
import dev.felix.ellice.feature.survival.SurvivalEnvironmentTracker;
import dev.felix.ellice.feature.survival.SurvivalItemData;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class CompatActivateService implements SurvivalEnvironmentTracker.Environment {
  private static CompatActivateService compatActivateService;
  private InventoryMenu inventoryMenu2;
  private int count = -1;
  private int count2 = -1;
  private ItemStack itemStack = ItemStack.EMPTY;
  private boolean enabled;

  public void activate() {
    compatActivateService = this;
    this.inventoryMenu2 = null;
    this.updateState();
  }

  public void deactivate() {
    if (compatActivateService == this) {
      compatActivateService = null;
    }

    this.inventoryMenu2 = null;
    this.updateState();
  }

  private void updateState() {
    this.count = this.count2 = -1;
    this.itemStack = ItemStack.EMPTY;
  }

  private void updateState2(InventoryMenu inventoryMenu) {
    if (this.inventoryMenu2 != inventoryMenu) {
      if (this.inventoryMenu2 != null) {
        this.updateState();
      }

      this.inventoryMenu2 = inventoryMenu;
    }
  }

  private boolean checkCondition() {
    Minecraft minecraft = Minecraft.getInstance();
    return this.inventoryMenu2 != null
        && minecraft.player != null
        && minecraft.player.inventoryMenu == this.inventoryMenu2;
  }

  @Override
  public SurvivalItemData capture() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.getConnection() != null
        && !CompatReleaseTracker.reserved()
        && !PearlThrowController.reserved()
        && !SoupInventoryBridge.handBusy()
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()
        && !minecraft.player.getAbilities().instabuild
        && !minecraft.player.isPassenger()
        && !minecraft.gameMode.isServerControlledInventory()
        && minecraft.player.containerMenu == minecraft.player.inventoryMenu
        && minecraft.screen == null) {
      InventoryMenu currentInventoryMenu = minecraft.player.inventoryMenu;
      if (currentInventoryMenu.slots.size() != 46) {
        return null;
      }

      this.updateState2(currentInventoryMenu);
      int value = InventoryInputActions.selected(minecraft);
      int currentCount =
          (this.count2 < 36 || this.count2 > 44 || value == this.count2 - 36)
                  && (this.count2 != 45 || value == this.count)
              ? 0
              : 1;
      int currentValue =
          currentCount == 0 && (minecraft.screen != null || !minecraft.options.keyUse.isDown())
              ? 0
              : 1;
      ArrayList arrayList = new ArrayList(9);

      for (int index = 0; index < 9; index++) {
        arrayList.add(read(currentInventoryMenu.getSlot(36 + index).getItem()));
      }

      ArrayList currentArrayList = new ArrayList(27);

      for (int currentIndex = 0; currentIndex < 27; currentIndex++) {
        currentArrayList.add(read(currentInventoryMenu.getSlot(9 + currentIndex).getItem()));
      }

      LocalPlayer localPlayer = minecraft.player;
      return new SurvivalItemData(
          currentInventoryMenu,
          arrayList,
          read(currentInventoryMenu.getSlot(45).getItem()),
          currentArrayList,
          value,
          localPlayer.getFoodData().getFoodLevel(),
          localPlayer.getHealth(),
          localPlayer.getMaxHealth(),
          localPlayer.getAbsorptionAmount(),
          localPlayer.getFoodData().getSaturationLevel(),
          localPlayer.getAirSupply(),
          localPlayer.getMaxAirSupply(),
          localPlayer.getRemainingFireTicks(),
          localPlayer.fallDistance,
          localPlayer.onGround(),
          localPlayer.isInWater(),
          localPlayer.isEyeInFluid(FluidTags.WATER),
          true,
          localPlayer.isUsingItem(),
          (currentValue != 0),
          minecraft.isPaused() || !minecraft.isWindowActive() || minecraft.getOverlay() != null);
    } else {
      return null;
    }
  }

  private boolean checkCondition2(SurvivalItemData survivalItemData) {
    SurvivalItemData currentSurvivalItemData = this.capture();
    return currentSurvivalItemData != null
        && currentSurvivalItemData.session() == survivalItemData.session()
        && currentSurvivalItemData.hotbar().equals(survivalItemData.hotbar())
        && currentSurvivalItemData.offhand().equals(survivalItemData.offhand())
        && currentSurvivalItemData.storage().equals(survivalItemData.storage())
        && currentSurvivalItemData.selected() == survivalItemData.selected();
  }

  @Override
  public boolean use(SurvivalItemData survivalItemData, int value) {
    if (this.checkCondition2(survivalItemData)
        && !survivalItemData.usingItem()
        && value >= 36
        && value <= 45) {
      if (SurvivalAllService.isKnownFood(survivalItemData.readySlot(value).id())
          && survivalItemData.food() < 20) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack currentItemStack = minecraft.player.inventoryMenu.getSlot(value).getItem();
        if (!currentItemStack.isEmpty() && !minecraft.gameMode.isDestroying()) {
          this.count = survivalItemData.selected();
          this.count2 = value;
          this.itemStack = currentItemStack.copy();
          this.enabled = true;

          try {
            if (value != 45) {
              InventoryInputActions.select(minecraft, value - 36);
            }

            minecraft.gameMode.useItem(
                minecraft.player,
                value == 45 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            return true;
          } finally {
            this.enabled = false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public void finishUse(boolean enabled) {
    if (this.count2 >= 0) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null
          && minecraft.player.inventoryMenu == this.inventoryMenu2
          && minecraft.gameMode != null) {
        int value =
            this.count2 != 45 && InventoryInputActions.selected(minecraft) != this.count2 - 36
                ? 0
                : 1;
        if (value != 0
            && minecraft.player.isUsingItem()
            && minecraft.player.getUsedItemHand()
                == (this.count2 == 45 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND)
            && ItemStack.isSameItemSameComponents(minecraft.player.getUseItem(), this.itemStack)) {
          minecraft.gameMode.releaseUsingItem(minecraft.player);
        }

        if (enabled && value != 0 && this.count2 != 45 && this.count >= 0) {
          InventoryInputActions.select(minecraft, this.count);
          minecraft.gameMode.ensureHasSentCarriedItem();
        }
      }

      this.count = this.count2 = -1;
      this.itemStack = ItemStack.EMPTY;
    }
  }

  @Override
  public boolean swap(SurvivalItemData survivalItemData, int value, int currentValue) {
    if (value < 9 || value > 35 || currentValue < 0 || currentValue > 8) {
      return false;
    }

    if (!this.checkCondition2(survivalItemData)) {
      return false;
    }

    Minecraft minecraft = Minecraft.getInstance();
    if (!minecraft.player.inventoryMenu.getCarried().isEmpty()) {
      return false;
    }

    Slot slot = minecraft.player.inventoryMenu.getSlot(value);
    Slot currentSlot = minecraft.player.inventoryMenu.getSlot(36 + currentValue);
    ItemStack itemStack = slot.getItem();
    if (!itemStack.isEmpty() && SurvivalAllService.isKnownFood(createText(itemStack))) {
      ItemStack currentItemStack = currentSlot.getItem().copy();
      if (slot.mayPickup(minecraft.player)
          && currentSlot.mayPlace(itemStack)
          && slot.mayPlace(currentItemStack)) {
        this.enabled = true;

        try {
          InventoryInputActions.click(minecraft, value, currentValue, SoupClickTracker.Click.SWAP);
          return ItemStack.matches(
                  minecraft.player.inventoryMenu.getSlot(value).getItem(), currentItemStack)
              && ItemStack.isSameItemSameComponents(
                  minecraft.player.inventoryMenu.getSlot(36 + currentValue).getItem(), itemStack);
        } finally {
          this.enabled = false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public static boolean holdUse() {
    if (compatActivateService != null && compatActivateService.count2 >= 0) {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.player != null
          && minecraft.player.inventoryMenu == compatActivateService.inventoryMenu2
          && minecraft.screen == null
          && minecraft.player.isUsingItem()
          && ItemStack.isSameItemSameComponents(
              minecraft.player.getUseItem(), compatActivateService.itemStack)
          && minecraft.player.getUsedItemHand()
              == (compatActivateService.count2 == 45
                  ? InteractionHand.OFF_HAND
                  : InteractionHand.MAIN_HAND)
          && (compatActivateService.count2 == 45
              || InventoryInputActions.selected(minecraft) == compatActivateService.count2 - 36);
    } else {
      return false;
    }
  }

  public static boolean handBusy() {
    if (compatActivateService != null
        && compatActivateService.count2 >= 0
        && !compatActivateService.enabled) {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.player != null
          && minecraft.player.inventoryMenu == compatActivateService.inventoryMenu2
          && (compatActivateService.count2 == 45
              || InventoryInputActions.selected(minecraft) == compatActivateService.count2 - 36);
    } else {
      return false;
    }
  }

  static SurvivalItemData.Item read(ItemStack itemStack) {
    return itemStack.isEmpty()
        ? SurvivalItemData.Item.EMPTY
        : new SurvivalItemData.Item(createText(itemStack), itemStack.getCount());
  }

  private static String createText(ItemStack itemStack) {
    return BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
  }
}
