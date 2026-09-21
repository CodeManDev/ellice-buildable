package dev.felix.ellice.feature.inventory;

import java.util.Map;
import java.util.Objects;

public record InventoryKindData(
   String id, String name, Object components, int count, int maxCount, InventoryKindData.Kind kind, InventoryKindData.Stats stats
) {
   public InventoryKindData(String id, String name, Object components, int count, int maxCount, InventoryKindData.Kind kind, InventoryKindData.Stats stats) {
      Objects.requireNonNull(id);
      Objects.requireNonNull(name);
      Objects.requireNonNull(components);
      Objects.requireNonNull(kind);
      Objects.requireNonNull(stats);
      if (count > 0 && maxCount > 0) {
         this.id = id;
         this.name = name;
         this.components = components;
         this.count = count;
         this.maxCount = maxCount;
         this.kind = kind;
         this.stats = stats;
      } else {
         throw new IllegalArgumentException("A loot item must be nonempty");
      }
   }

   public boolean stacksWith(InventoryKindData inventoryKindData) {
      return inventoryKindData != null && this.id.equals(inventoryKindData.id) && this.components.equals(inventoryKindData.components);
   }

   public String equipmentGroup() {
      return this.kind.name() + (this.kind.tool() && this.stats.enchantment("silk_touch") > 0 ? ":silk" : "");
   }

   public enum Kind {
      HELMET,
      CHESTPLATE,
      LEGGINGS,
      BOOTS,
      SWORD,
      AXE,
      PICKAXE,
      SHOVEL,
      HOE,
      BOW,
      CROSSBOW,
      TRIDENT,
      MACE,
      SHIELD,
      ELYTRA,
      FOOD,
      BLOCK,
      ARROW,
      PEARL,
      HEALING,
      UTILITY,
      VALUABLE,
      OTHER;

      public boolean armor() {
         return this.ordinal() <= BOOTS.ordinal();
      }

      public boolean tool() {
         return this == AXE || this == PICKAXE || this == SHOVEL || this == HOE;
      }

      public boolean equipment() {
         return this.ordinal() <= ELYTRA.ordinal();
      }


      private static InventoryKindData.Kind[] $values() {
         return new InventoryKindData.Kind[]{
            HELMET,
            CHESTPLATE,
            LEGGINGS,
            BOOTS,
            SWORD,
            AXE,
            PICKAXE,
            SHOVEL,
            HOE,
            BOW,
            CROSSBOW,
            TRIDENT,
            MACE,
            SHIELD,
            ELYTRA,
            FOOD,
            BLOCK,
            ARROW,
            PEARL,
            HEALING,
            UTILITY,
            VALUABLE,
            OTHER
         };
      }
   }

   public record Stats(
      double armor,
      double toughness,
      double knockbackResistance,
      double attackDamage,
      double attackSpeed,
      double miningSpeed,
      double foodValue,
      int remainingDurability,
      int maxDurability,
      int harvestTier,
      Map<String, Integer> enchantments
   ) {
      public static final InventoryKindData.Stats NONE = new InventoryKindData.Stats(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, Map.of());

      public Stats(
         double armor,
         double toughness,
         double knockbackResistance,
         double attackDamage,
         double attackSpeed,
         double miningSpeed,
         double foodValue,
         int remainingDurability,
         int maxDurability,
         int harvestTier,
         Map<String, Integer> enchantments
      ) {
         enchantments = Map.copyOf(enchantments);
         this.armor = armor;
         this.toughness = toughness;
         this.knockbackResistance = knockbackResistance;
         this.attackDamage = attackDamage;
         this.attackSpeed = attackSpeed;
         this.miningSpeed = miningSpeed;
         this.foodValue = foodValue;
         this.remainingDurability = remainingDurability;
         this.maxDurability = maxDurability;
         this.harvestTier = harvestTier;
         this.enchantments = enchantments;
      }

      public int enchantment(String text) {
         return Math.max(0, Math.min(255, this.enchantments.getOrDefault(text, 0)));
      }

      public double durability() {
         return this.maxDurability <= 0 ? 1.0 : Math.clamp((double)this.remainingDurability / this.maxDurability, 0.0, 1.0);
      }

      public boolean cursed() {
         return this.enchantment("binding_curse") > 0 || this.enchantment("vanishing_curse") > 0;
      }
   }
}
