package dev.felix.ellice.feature.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

public final class InventoryLootPlanner {
  public InventoryLootPlanner.Plan plan(
      InventorySlotData inventorySlotData,
      InventoryData inventoryData,
      int value,
      int currentValue,
      Set<Integer> values) {
    ArrayList<InventoryLootPlanner.Candidate> candidates = new ArrayList<>();
    boolean capacityBlocked = false;
    boolean retryBlocked = false;
    IdentityHashMap<InventoryKindData, Double> scores = new IdentityHashMap<>();
    ToDoubleFunction<InventoryKindData> toDoubleFunction =
        inventoryItem ->
            scores.computeIfAbsent(
                inventoryItem,
                candidate -> InventoryScoreService.score(candidate, inventorySlotData.owned()));
    HashMap<String, InventoryKindData> equippedItems = new HashMap<>();
    HashMap<String, InventorySlotData.Slot> equipmentCandidates = new HashMap<>();
    if (inventoryData.smart()) {
      for (InventoryKindData inventoryKindData : inventorySlotData.owned()) {
        if (inventoryKindData.kind().equipment()) {
          equippedItems.merge(
              inventoryKindData.equipmentGroup(),
              inventoryKindData,
              (item, currentItem) ->
                  (InventoryKindData)
                      (toDoubleFunction.applyAsDouble(currentItem)
                              > toDoubleFunction.applyAsDouble(item)
                          ? currentItem
                          : item));
        }
      }

      for (InventorySlotData.Slot currentSlot : inventorySlotData.slots()) {
        if (currentSlot.item().kind().equipment()
            && currentSlot.mayTake()
            && inventoryData.accepts(currentSlot.item())) {
          equipmentCandidates.merge(
              currentSlot.item().equipmentGroup(),
              currentSlot,
              (currentItem, nextItem) -> {
                double doubleValue =
                    toDoubleFunction.applyAsDouble(nextItem.item())
                        - toDoubleFunction.applyAsDouble(currentItem.item());
                return (InventorySlotData.Slot)
                    (!(doubleValue > 0.001)
                            && (!(Math.abs(doubleValue) <= 0.001)
                                || nextItem.index() >= currentItem.index())
                        ? currentItem
                        : nextItem);
              });
        }
      }
    }

    for (InventorySlotData.Slot nextSlot : inventorySlotData.slots()) {
      InventoryKindData currentInventoryKindData = nextSlot.item();
      if (nextSlot.mayTake() && inventoryData.accepts(currentInventoryKindData)) {
        double doubleValue = 20.0;
        String text = "Collect item";
        if (inventoryData.smart()) {
          if (currentInventoryKindData.kind().equipment()) {
            double currentDoubleValue = toDoubleFunction.applyAsDouble(currentInventoryKindData);
            InventoryKindData nextInventoryKindData =
                equippedItems.get(currentInventoryKindData.equipmentGroup());
            double nextDoubleValue =
                nextInventoryKindData == null
                    ? 0.0
                    : toDoubleFunction.applyAsDouble(nextInventoryKindData);
            if (nextInventoryKindData != null && currentDoubleValue <= nextDoubleValue + 0.25
                || equipmentCandidates.get(currentInventoryKindData.equipmentGroup()) != nextSlot) {
              continue;
            }

            doubleValue =
                (currentInventoryKindData.kind().armor() ? 110 : 90)
                    + Math.min(20.0, currentDoubleValue - nextDoubleValue);
            text =
                nextInventoryKindData == null
                    ? "Missing equipment"
                    : "Upgrade over " + nextInventoryKindData.name();
          } else {
            int currentCount =
                inventorySlotData.owned().stream()
                    .filter(item -> item.kind() == currentInventoryKindData.kind())
                    .mapToInt(InventoryKindData::count)
                    .sum();
            int nextValue =
                inventoryData
                    .stockTargets()
                    .getOrDefault(currentInventoryKindData.kind(), Integer.MAX_VALUE);
            if (currentCount >= nextValue) {
              continue;
            }
            doubleValue =
                switch (currentInventoryKindData.kind()) {
                  case HEALING -> 100.0;
                  case PEARL -> 80.0;
                  case VALUABLE -> 75.0;
                  case FOOD ->
                      (currentCount < 8 ? 95 : 55)
                          + Math.min(10.0, currentInventoryKindData.stats().foodValue() / 2.0);
                  case BLOCK -> currentCount < 32 ? 85.0 : 40.0;
                  case ARROW -> 60.0;
                  case UTILITY -> 50.0;
                  default -> 20.0;
                };
            text =
                nextValue == Integer.MAX_VALUE
                    ? "Collect valuables"
                    : "Refill: " + currentCount + " / " + nextValue;
          }
        }

        if (values.contains(nextSlot.index())) {
          retryBlocked = true;
        } else if (nextSlot.transferCount() > 0
            && nextSlot.freeSlotsAfter()
                >= Math.min(nextSlot.freeSlotsBefore(), inventoryData.reserveSlots())) {
          if (value >= 0) {
            doubleValue -=
                Math.min(
                    12.0, Math.hypot(nextSlot.x() - value, nextSlot.y() - currentValue) / 18.0);
          }

          candidates.add(new InventoryLootPlanner.Candidate(nextSlot, doubleValue, text));
        } else {
          capacityBlocked = true;
        }
      }
    }

    candidates.sort(
        Comparator.comparingDouble(InventoryLootPlanner.Candidate::priority)
            .reversed()
            .thenComparingInt(item -> item.slot().index()));
    return new InventoryLootPlanner.Plan(candidates, capacityBlocked, retryBlocked);
  }

  public record Candidate(InventorySlotData.Slot slot, double priority, String reason) {}

  public record Plan(
      List<InventoryLootPlanner.Candidate> candidates,
      boolean capacityBlocked,
      boolean retryBlocked) {
    public Plan(
        List<InventoryLootPlanner.Candidate> candidates,
        boolean capacityBlocked,
        boolean retryBlocked) {
      candidates = List.copyOf(candidates);
      this.candidates = candidates;
      this.capacityBlocked = capacityBlocked;
      this.retryBlocked = retryBlocked;
    }

    public boolean complete() {
      return this.candidates.isEmpty() && !this.capacityBlocked && !this.retryBlocked;
    }
  }
}
