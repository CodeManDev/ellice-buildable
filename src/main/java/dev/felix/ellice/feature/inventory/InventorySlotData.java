package dev.felix.ellice.feature.inventory;

import java.util.List;

public record InventorySlotData(
    Object menu,
    List<InventorySlotData.Slot> slots,
    List<InventoryKindData> owned,
    boolean paused,
    boolean carriedItem,
    boolean activationHeld) {
  public InventorySlotData(
      Object menu,
      List<InventorySlotData.Slot> slots,
      List<InventoryKindData> owned,
      boolean paused,
      boolean carriedItem,
      boolean activationHeld) {
    slots = List.copyOf(slots);
    owned = List.copyOf(owned);
    this.menu = menu;
    this.slots = slots;
    this.owned = owned;
    this.paused = paused;
    this.carriedItem = carriedItem;
    this.activationHeld = activationHeld;
  }

  public int ownedCount(InventoryKindData inventoryKindData) {
    return this.owned.stream()
        .filter(inventoryKindData::stacksWith)
        .mapToInt(InventoryKindData::count)
        .sum();
  }

  public record Slot(
      int index,
      int x,
      int y,
      InventoryKindData item,
      boolean mayTake,
      int transferCount,
      int freeSlotsBefore,
      int freeSlotsAfter) {}
}
