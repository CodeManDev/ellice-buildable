package dev.felix.ellice.feature.inventory;

import java.util.Map;
import java.util.Set;

public record InventoryData(
   boolean smart,
   Set<InventoryKindData.Kind> kinds,
   Map<InventoryKindData.Kind, Integer> stockTargets,
   boolean skipCurses,
   double minimumDurability,
   int reserveSlots,
   Set<String> ignoredIds
) {
   public InventoryData(
      boolean smart,
      Set<InventoryKindData.Kind> kinds,
      Map<InventoryKindData.Kind, Integer> stockTargets,
      boolean skipCurses,
      double minimumDurability,
      int reserveSlots,
      Set<String> ignoredIds
   ) {
      kinds = Set.copyOf(kinds);
      stockTargets = Map.copyOf(stockTargets);
      ignoredIds = Set.copyOf(ignoredIds);
      minimumDurability = Math.clamp(minimumDurability, 0.0, 1.0);
      reserveSlots = Math.clamp(reserveSlots, 0, 36);
      this.smart = smart;
      this.kinds = kinds;
      this.stockTargets = stockTargets;
      this.skipCurses = skipCurses;
      this.minimumDurability = minimumDurability;
      this.reserveSlots = reserveSlots;
      this.ignoredIds = ignoredIds;
   }

   public boolean accepts(InventoryKindData inventoryKindData) {
      return !this.ignoredIds.contains(inventoryKindData.id())
         && (!this.smart || this.kinds.contains(inventoryKindData.kind()))
         && (!this.skipCurses || !inventoryKindData.stats().cursed())
         && (!inventoryKindData.kind().equipment() || inventoryKindData.stats().durability() >= this.minimumDurability);
   }
}
