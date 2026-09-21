package dev.felix.ellice.feature.inventory;

import java.util.Comparator;
import java.util.List;

public final class InventoryScoreService {
   private InventoryScoreService() {
   }

   public static double score(InventoryKindData inventoryKindData, List<InventoryKindData> items) {
      InventoryKindData.Stats currentStats = inventoryKindData.stats();
      double doubleValue;
      if (inventoryKindData.kind().armor()) {
         double currentDoubleValue = currentStats.armor();
         double nextDoubleValue = currentStats.toughness();
         int value = currentStats.enchantment("protection");
         int currentValue = currentStats.enchantment("fire_protection");
         int nextValue = currentStats.enchantment("blast_protection");
         int previousValue = currentStats.enchantment("projectile_protection");

         for (InventoryKindData.Kind currentKind : List.of(
            InventoryKindData.Kind.HELMET, InventoryKindData.Kind.CHESTPLATE, InventoryKindData.Kind.LEGGINGS, InventoryKindData.Kind.BOOTS
         )) {
            if (currentKind != inventoryKindData.kind()) {
               InventoryKindData currentInventoryKindData = items.stream()
                  .filter(item -> item.kind() == currentKind)
                  .max(Comparator.comparingDouble(InventoryScoreService::calculateValue))
                  .orElse(null);
               if (currentInventoryKindData != null) {
                  InventoryKindData.Stats nextStats = currentInventoryKindData.stats();
                  currentDoubleValue += nextStats.armor();
                  nextDoubleValue += nextStats.toughness();
                  value += nextStats.enchantment("protection");
                  currentValue += nextStats.enchantment("fire_protection");
                  nextValue += nextStats.enchantment("blast_protection");
                  previousValue += nextStats.enchantment("projectile_protection");
               }
            }
         }

         double previousDoubleValue = 0.5 * calculateValue4(value)
            + 0.18 * calculateValue4(value + 2 * previousValue)
            + 0.16 * calculateValue4(value + 2 * currentValue)
            + 0.16 * calculateValue4(value + 2 * nextValue);
         double sourceDoubleValue = (
               0.65 * calculateValue3(currentDoubleValue, nextDoubleValue, 8.0)
                  + 0.35 * calculateValue3(currentDoubleValue, nextDoubleValue, 20.0)
            )
            * previousDoubleValue;
         doubleValue = 100.0 * (1.0 - sourceDoubleValue)
            + currentStats.knockbackResistance() * 6.0
            + currentStats.enchantment("feather_falling") * 0.7
            + currentStats.enchantment("respiration") * 0.35
            + currentStats.enchantment("aqua_affinity") * 0.4
            + currentStats.enchantment("depth_strider") * 0.3;
      } else {
         doubleValue = switch (inventoryKindData.kind()) {
            case SWORD, AXE, TRIDENT, MACE -> {
               double targetDoubleValue = currentStats.attackDamage()
                  + (
                     currentStats.enchantment("sharpness") == 0
                        ? 0.0
                        : 0.5 * currentStats.enchantment("sharpness")
                           + 0.5
                  );
               double inputDoubleValue = Math.clamp(
                  currentStats.attackSpeed(), 0.1, 4.0
               );
               yield targetDoubleValue * (1.0 + 0.25 * inputDoubleValue)
                  + currentStats.enchantment("fire_aspect") * 0.4
                  + currentStats.enchantment("looting") * 0.6
                  + currentStats.enchantment("sweeping_edge") * 0.3
                  + currentStats.enchantment("impaling") * 0.4
                  + currentStats.enchantment("loyalty") * 0.5
                  + currentStats.enchantment("riptide") * 0.5
                  + currentStats.enchantment("density") * 0.6
                  + currentStats.enchantment("breach") * 0.5
                  + currentStats.enchantment("wind_burst") * 0.6
                  + (inventoryKindData.kind().tool() ? calculateValue2(currentStats) : 0.0);
            }
            case PICKAXE, SHOVEL, HOE -> 4.0 + calculateValue2(currentStats);
            case BOW -> 8 + currentStats.enchantment("power") * 2 + currentStats.enchantment("infinity") * 3 + currentStats.enchantment("flame")
               + currentStats.enchantment("punch") * 0.5;
            case CROSSBOW -> 8 + currentStats.enchantment("quick_charge") * 2 + currentStats.enchantment("multishot") * 2
               + currentStats.enchantment("piercing") * 0.7;
            case SHIELD, ELYTRA -> 10.0;
            case FOOD -> currentStats.foodValue();
            default -> 1.0;
         };
      }

      if (inventoryKindData.kind().equipment()) {
         doubleValue += currentStats.enchantment("unbreaking") * 0.35
            + currentStats.enchantment("mending") * 1.1;
         doubleValue *= 0.78
            + 0.22 * Math.sqrt(currentStats.durability());
         if (currentStats.durability() < 0.15) {
            doubleValue *= 0.4 + 4.0 * currentStats.durability();
         }
      }

      return Double.isFinite(doubleValue) ? Math.max(0.0, doubleValue) : 0.0;
   }

   private static double calculateValue(InventoryKindData inventoryKindData) {
      InventoryKindData.Stats currentStats = inventoryKindData.stats();
      return (
            currentStats.armor()
               + currentStats.toughness() * 0.4
               + currentStats.enchantment("protection") * 1.25
         )
         * (0.78 + 0.22 * Math.sqrt(currentStats.durability()));
   }

   private static double calculateValue2(InventoryKindData.Stats stats) {
      return stats.miningSpeed() * 0.6
         + stats.harvestTier() * 4
         + Math.log1p(Math.max(0, stats.remainingDurability())) * 0.8
         + stats.enchantment("efficiency") * 1.3
         + stats.enchantment("fortune") * 1.5
         + stats.enchantment("silk_touch") * 3;
   }

   private static double calculateValue3(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = Math.clamp(
         doubleValue
            - nextDoubleValue / (2.0 + Math.max(0.0, currentDoubleValue) / 4.0),
         Math.min(20.0, Math.max(0.0, doubleValue) * 0.2),
         20.0
      );
      return 1.0 - previousDoubleValue / 25.0;
   }

   private static double calculateValue4(int value) {
      return 1.0 - Math.min(20, value) * 0.04;
   }
}
