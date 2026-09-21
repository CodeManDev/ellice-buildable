package dev.felix.ellice.compat;

import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.feature.soup.SoupConfirmedData;
import dev.felix.ellice.feature.soup.SoupKindData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

public final class SoupInventoryBridge implements SoupClickTracker.Environment {
  private static SoupInventoryBridge compatActivateService2;
  private static long timestamp = Long.MIN_VALUE;
  private final SoupConfirmedData.Confirmed[] confirmed = new SoupConfirmedData.Confirmed[46];
  private InventoryMenu inventoryMenu2;
  private InventoryScreen inventoryScreen;
  private long timestamp2;
  private long timestamp3;
  private int count = -1;
  private int count2 = -1;
  private boolean enabled;

  public void activate() {
    compatActivateService2 = this;
    this.inventoryMenu2 = null;
    this.updateState();
  }

  public void deactivate() {
    if (compatActivateService2 == this) {
      compatActivateService2 = null;
    }

    this.inventoryMenu2 = null;
    this.updateState();
  }

  private void updateState() {
    this.count = this.count2 = -1;
    this.inventoryScreen = null;
  }

  public static void manualInput() {
    timestamp = calculateValue();
  }

  public static void recipeInput() {
    if (compatActivateService2 != null && !compatActivateService2.enabled) {
      manualInput();
    }
  }

  private static long calculateValue() {
    return System.nanoTime() / 1000000L;
  }

  public static void serverSlot(int value, int currentValue, ItemStack itemStack) {
    if (compatActivateService2 != null) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
        if (value == -2) {
          currentValue = SoupConfirmedData.playerSlotToMenu(currentValue);
        } else if (value != 0) {
          return;
        }

        compatActivateService2.updateState2(
            minecraft.player.inventoryMenu, currentValue, itemStack);
      }
    }
  }

  public static void serverInventorySlot(int value, ItemStack itemStack) {
    serverSlot(0, SoupConfirmedData.playerSlotToMenu(value), itemStack);
  }

  public static void serverContent(int value, List<ItemStack> items) {
    if (value == 0) {
      for (int index = 0; index < Math.min(46, items.size()); index++) {
        serverSlot(0, index, (ItemStack) items.get(index));
      }
    }
  }

  private void updateState2(InventoryMenu inventoryMenu, int index, ItemStack itemStack) {
    this.updateState3(inventoryMenu);
    if (index >= 0 && index < 46) {
      this.confirmed[index] = new SoupConfirmedData.Confirmed(++this.timestamp2, read(itemStack));
    }
  }

  private void updateState3(InventoryMenu inventoryMenu) {
    if (this.inventoryMenu2 != inventoryMenu) {
      if (this.inventoryMenu2 != null) {
        this.updateState();
      }

      this.inventoryMenu2 = inventoryMenu;
      this.timestamp2 = 0L;
      Arrays.fill(this.confirmed, new SoupConfirmedData.Confirmed(0L, SoupKindData.EMPTY));
    }
  }

  @Override
  public SoupConfirmedData capture() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.getConnection() != null
        && !CompatReleaseTracker.reserved()
        && !PearlThrowController.reserved()
        && !CompatActivateService.handBusy()
        && minecraft.player.isAlive()
        && !minecraft.player.isSpectator()
        && !minecraft.player.getAbilities().instabuild
        && !minecraft.player.isPassenger()
        && !minecraft.gameMode.isServerControlledInventory()
        && minecraft.player.containerMenu == minecraft.player.inventoryMenu
        && (minecraft.screen == null || minecraft.screen instanceof InventoryScreen)) {
      InventoryMenu currentInventoryMenu = minecraft.player.inventoryMenu;
      if (currentInventoryMenu.slots.size() != 46) {
        return null;
      }

      this.updateState3(currentInventoryMenu);
      if (this.inventoryScreen != null && minecraft.screen != this.inventoryScreen) {
        this.inventoryScreen = null;
        this.timestamp3 = calculateValue() + 600L;
      }

      int value = InventoryInputActions.selected(minecraft);
      int currentCount =
          (this.count2 < 36 || this.count2 > 44 || value == this.count2 - 36)
                  && (this.count2 != 45 || value == this.count)
              ? 0
              : 1;
      int currentValue =
          calculateValue() >= this.timestamp3
                  && (timestamp == Long.MIN_VALUE || calculateValue() - timestamp >= 350L)
                  && currentCount == 0
                  && (minecraft.screen != null || !minecraft.options.keyUse.isDown())
              ? 0
              : 1;
      ArrayList arrayList = new ArrayList(46);

      for (Slot slot : currentInventoryMenu.slots) {
        arrayList.add(read(slot.getItem()));
      }

      return new SoupConfirmedData(
          currentInventoryMenu,
          arrayList,
          read(currentInventoryMenu.getCarried()),
          List.of((SoupConfirmedData.Confirmed[]) this.confirmed.clone()),
          value,
          minecraft.player.getHealth(),
          minecraft.player.getMaxHealth(),
          minecraft.player.getFoodData().getFoodLevel(),
          minecraft.screen instanceof InventoryScreen,
          minecraft.screen == this.inventoryScreen && this.inventoryScreen != null,
          minecraft.isPaused() || !minecraft.isWindowActive() || minecraft.getOverlay() != null,
          (currentValue != 0),
          minecraft.player.isUsingItem());
    } else {
      return null;
    }
  }

  private boolean checkCondition(SoupConfirmedData soupConfirmedData) {
    SoupConfirmedData currentSoupConfirmedData = this.capture();
    return currentSoupConfirmedData != null
        && currentSoupConfirmedData.session() == soupConfirmedData.session()
        && !currentSoupConfirmedData.paused()
        && !currentSoupConfirmedData.manualInput()
        && currentSoupConfirmedData.slots().equals(soupConfirmedData.slots())
        && currentSoupConfirmedData.cursor().equals(soupConfirmedData.cursor())
        && currentSoupConfirmedData.selected() == soupConfirmedData.selected()
        && currentSoupConfirmedData.inventoryOpen() == soupConfirmedData.inventoryOpen();
  }

  @Override
  public boolean openInventory(SoupConfirmedData soupConfirmedData) {
    if (!soupConfirmedData.inventoryOpen()
        && !soupConfirmedData.usingItem()
        && soupConfirmedData.cursor().empty()
        && this.checkCondition(soupConfirmedData)) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.getTutorial().onOpenInventory();
      this.inventoryScreen = new InventoryScreen(minecraft.player);
      minecraft.setScreen(this.inventoryScreen);
      return minecraft.screen == this.inventoryScreen;
    } else {
      return false;
    }
  }

  @Override
  public boolean closeInventory(SoupConfirmedData soupConfirmedData) {
    if (soupConfirmedData.ownedInventory()
        && soupConfirmedData.canReturnCraftingItems()
        && this.checkCondition(soupConfirmedData)) {
      InventoryScreen currentInventoryScreen = this.inventoryScreen;
      this.inventoryScreen = null;
      currentInventoryScreen.onClose();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void yieldInventory() {
    this.inventoryScreen = null;
  }

  @Override
  public boolean click(
      SoupConfirmedData soupConfirmedData,
      int value,
      int currentValue,
      SoupClickTracker.Click currentClick) {
    if (soupConfirmedData.inventoryOpen()
        && this.checkCondition(soupConfirmedData)
        && value >= 0
        && value < 46) {
      Minecraft minecraft = Minecraft.getInstance();
      Slot slot = minecraft.player.inventoryMenu.getSlot(value);
      if (slot.isActive() && (!slot.hasItem() || slot.mayPickup(minecraft.player))) {
        if (currentClick == SoupClickTracker.Click.PICKUP
            && !minecraft.player.inventoryMenu.getCarried().isEmpty()
            && !slot.mayPlace(minecraft.player.inventoryMenu.getCarried())) {
          return false;
        }

        InventoryInputActions.click(minecraft, value, currentValue, currentClick);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public boolean placeRecipe(SoupConfirmedData soupConfirmedData) {
    if (soupConfirmedData.inventoryOpen()
        && soupConfirmedData.emptyGrid()
        && soupConfirmedData.cursor().empty()
        && this.checkCondition(soupConfirmedData)) {
      Minecraft minecraft = Minecraft.getInstance();

      for (int index = 9; index <= 44; index++) {
        SoupKindData soupKindData = soupConfirmedData.slot(index);
        if (Set.of(
                    SoupKindData.Kind.BOWL,
                    SoupKindData.Kind.RED_MUSHROOM,
                    SoupKindData.Kind.BROWN_MUSHROOM)
                .contains(soupKindData.kind())
            && !soupKindData.ingredient()) {
          return false;
        }
      }

      StackedItemContents stackedItemContents = new StackedItemContents();
      minecraft.player.getInventory().fillStackedContents(stackedItemContents);
      ContextMap contextMap = SlotDisplayContext.fromLevel(minecraft.level);

      for (RecipeCollection recipeCollection : minecraft.player.getRecipeBook().getCollections()) {
        for (RecipeDisplayEntry recipeDisplayEntry : recipeCollection.getRecipes()) {
          if (recipeDisplayEntry.display()
                  instanceof ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay
              && shapelessCraftingRecipeDisplay.ingredients().size() == 3
              && recipeDisplayEntry.canCraft(stackedItemContents)) {
            List items = recipeDisplayEntry.resultItems(contextMap);
            if (items.size() == 1
                && ((ItemStack) items.getFirst()).is(Items.MUSHROOM_STEW)
                && ((ItemStack) items.getFirst()).getCount() == 1) {
              HashSet hashSet = new HashSet();
              byte byteValue = 1;

              for (SlotDisplay slotDisplay : shapelessCraftingRecipeDisplay.ingredients()) {
                List currentItems = slotDisplay.resolveForStacks(contextMap);
                if (currentItems.size() != 1) {
                  byteValue = 0;
                  break;
                }

                hashSet.add(read((ItemStack) currentItems.getFirst()).kind());
              }

              if (byteValue != 0
                  && hashSet.equals(
                      Set.of(
                          SoupKindData.Kind.BOWL,
                          SoupKindData.Kind.RED_MUSHROOM,
                          SoupKindData.Kind.BROWN_MUSHROOM))) {
                this.enabled = true;

                try {
                  minecraft.gameMode.handlePlaceRecipe(0, recipeDisplayEntry.id(), false);
                } finally {
                  this.enabled = false;
                }

                return true;
              }
            }
          }
        }
      }

      return false;
    } else {
      return false;
    }
  }

  @Override
  public boolean use(SoupConfirmedData soupConfirmedData, int value) {
    if (!soupConfirmedData.inventoryOpen()
        && !soupConfirmedData.usingItem()
        && soupConfirmedData.cursor().empty()
        && this.checkCondition(soupConfirmedData)
        && value >= 36
        && value <= 45
        && soupConfirmedData.slot(value).kind() == SoupKindData.Kind.SOUP) {
      Minecraft minecraft = Minecraft.getInstance();
      ItemStack itemStack = minecraft.player.inventoryMenu.getSlot(value).getItem();
      if (!minecraft.player.getCooldowns().isOnCooldown(itemStack)
          && !minecraft.gameMode.isDestroying()) {
        this.count = soupConfirmedData.selected();
        this.count2 = value;
        this.enabled = true;

        try {
          if (value != 45) {
            InventoryInputActions.select(minecraft, value - 36);
          }

          minecraft.gameMode.useItem(
              minecraft.player, value == 45 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
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
            && minecraft.player.getUseItem().is(Items.MUSHROOM_STEW)) {
          minecraft.gameMode.releaseUsingItem(minecraft.player);
        }

        if (enabled && value != 0 && this.count2 != 45 && this.count >= 0) {
          InventoryInputActions.select(minecraft, this.count);
          minecraft.gameMode.ensureHasSentCarriedItem();
        }
      }

      this.count = this.count2 = -1;
    }
  }

  public static boolean holdUse() {
    if (compatActivateService2 != null && compatActivateService2.count2 >= 0) {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.player != null
          && minecraft.player.inventoryMenu == compatActivateService2.inventoryMenu2
          && minecraft.screen == null
          && minecraft.player.isUsingItem()
          && minecraft.player.getUseItem().is(Items.MUSHROOM_STEW)
          && minecraft.player.getUsedItemHand()
              == (compatActivateService2.count2 == 45
                  ? InteractionHand.OFF_HAND
                  : InteractionHand.MAIN_HAND)
          && (compatActivateService2.count2 == 45
              || InventoryInputActions.selected(minecraft) == compatActivateService2.count2 - 36);
    } else {
      return false;
    }
  }

  public static boolean handBusy() {
    if (compatActivateService2 != null
        && compatActivateService2.count2 >= 0
        && !compatActivateService2.enabled) {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft.player != null
          && minecraft.player.inventoryMenu == compatActivateService2.inventoryMenu2
          && (compatActivateService2.count2 == 45
              || InventoryInputActions.selected(minecraft) == compatActivateService2.count2 - 36);
    } else {
      return false;
    }
  }

  static SoupKindData read(ItemStack itemStack) {
    if (itemStack.isEmpty()) {
      return SoupKindData.EMPTY;
    }

    SoupKindData.Kind kind =
        itemStack.is(Items.MUSHROOM_STEW)
            ? SoupKindData.Kind.SOUP
            : (itemStack.is(Items.BOWL)
                ? SoupKindData.Kind.BOWL
                : (itemStack.is(Items.RED_MUSHROOM)
                    ? SoupKindData.Kind.RED_MUSHROOM
                    : (itemStack.is(Items.BROWN_MUSHROOM)
                        ? SoupKindData.Kind.BROWN_MUSHROOM
                        : SoupKindData.Kind.OTHER)));
    int value =
        Set.of(
                        SoupKindData.Kind.BOWL,
                        SoupKindData.Kind.RED_MUSHROOM,
                        SoupKindData.Kind.BROWN_MUSHROOM)
                    .contains(kind)
                && !itemStack.has(DataComponents.CUSTOM_NAME)
                && !itemStack.has(DataComponents.CUSTOM_DATA)
            ? 1
            : 0;
    return new SoupKindData(
        kind,
        itemStack.getCount(),
        itemStack.getMaxStackSize(),
        new SoupInventoryBridge.Identity(
            BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString(),
            itemStack.getComponentsPatch()),
        (value != 0));
  }

  private record Identity(Object item, Object components) {}
}
