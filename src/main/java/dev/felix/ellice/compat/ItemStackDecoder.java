package dev.felix.ellice.compat;

import dev.felix.ellice.feature.inventory.InventoryKindData;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;

public final class ItemStackDecoder {
   private static final Set<String> text = Set.of(
      "diamond",
      "emerald",
      "gold_ingot",
      "gold_nugget",
      "iron_ingot",
      "netherite_ingot",
      "netherite_scrap",
      "ancient_debris",
      "enchanted_book",
      "experience_bottle",
      "nether_star",
      "dragon_egg",
      "ender_eye",
      "heart_of_the_sea",
      "nautilus_shell",
      "echo_shard",
      "heavy_core",
      "trial_key",
      "ominous_trial_key",
      "totem_of_undying"
   );
   private static final Set<String> text2 = Set.of(
      "water_bucket",
      "lava_bucket",
      "bucket",
      "milk_bucket",
      "flint_and_steel",
      "fishing_rod",
      "torch",
      "soul_torch",
      "crafting_table",
      "ender_chest",
      "firework_rocket"
   );

   private ItemStackDecoder() {
   }

   public static InventoryKindData read(ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         throw new IllegalArgumentException("Cannot evaluate an empty stack");
      }

      Identifier identifier = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
      HashMap hashMap = new HashMap();

      for (Entry entry : itemStack.getEnchantments().entrySet()) {
         ((Holder)entry.getKey()).unwrapKey().ifPresent(item -> {
            String text = ((Holder)entry.getKey()).getRegisteredName();
            hashMap.put(text.startsWith("minecraft:") ? text.substring(10) : text, entry.getIntValue());
         });
      }

      Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
      EquipmentSlot equipmentSlot = equippable == null ? EquipmentSlot.MAINHAND : equippable.slot();
      double doubleValue = calculateValue(itemStack, Attributes.ARMOR, 0.0, equipmentSlot);
      FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
      Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
      double currentDoubleValue = tool == null
         ? 0.0
         : tool.rules()
            .stream()
            .mapToDouble(item -> item.speed().orElse(tool.defaultMiningSpeed()).floatValue())
            .max()
            .orElse(tool.defaultMiningSpeed());
      int value = tool == null
         ? 0
         : (
            tool.isCorrectForDrops(Blocks.OBSIDIAN.defaultBlockState())
               ? 3
               : (
                  tool.isCorrectForDrops(Blocks.DIAMOND_ORE.defaultBlockState())
                     ? 2
                     : (tool.isCorrectForDrops(Blocks.IRON_ORE.defaultBlockState()) ? 1 : 0)
               )
         );
      InventoryKindData.Stats stats = new InventoryKindData.Stats(
         doubleValue,
         calculateValue(itemStack, Attributes.ARMOR_TOUGHNESS, 0.0, equipmentSlot),
         calculateValue(itemStack, Attributes.KNOCKBACK_RESISTANCE, 0.0, equipmentSlot),
         calculateValue(itemStack, Attributes.ATTACK_DAMAGE, 1.0, EquipmentSlot.MAINHAND),
         calculateValue(itemStack, Attributes.ATTACK_SPEED, 4.0, EquipmentSlot.MAINHAND),
         currentDoubleValue,
         foodProperties == null ? 0.0 : foodProperties.nutrition() + foodProperties.saturation(),
         Math.max(0, itemStack.getMaxDamage() - itemStack.getDamageValue()),
         itemStack.getMaxDamage(),
         value,
         hashMap
      );
      return new InventoryKindData(
         identifier.toString(),
         itemStack.getHoverName().getString(),
         itemStack.getComponentsPatch(),
         itemStack.getCount(),
         itemStack.getMaxStackSize(),
         createKind(itemStack, identifier.getPath(), equipmentSlot, doubleValue),
         stats
      );
   }

   private static InventoryKindData.Kind createKind(ItemStack itemStack, String currentText, EquipmentSlot equipmentSlot, double doubleValue) {
      if (currentText.equals("elytra")) {
         return InventoryKindData.Kind.ELYTRA;
      }

      if (doubleValue > 0.0) {
         InventoryKindData.Kind kind = switch (equipmentSlot) {
            case HEAD -> InventoryKindData.Kind.HELMET;
            case CHEST -> InventoryKindData.Kind.CHESTPLATE;
            case LEGS -> InventoryKindData.Kind.LEGGINGS;
            case FEET -> InventoryKindData.Kind.BOOTS;
            default -> null;
         };
         if (kind != null) {
            return kind;
         }
      }

      Set values = itemStack.getItem().builtInRegistryHolder().tags().map(item -> item.location().toString()).collect(Collectors.toSet());
      if (values.contains("minecraft:swords") || currentText.endsWith("_sword")) {
         return InventoryKindData.Kind.SWORD;
      }

      if (values.contains("minecraft:pickaxes") || currentText.endsWith("_pickaxe")) {
         return InventoryKindData.Kind.PICKAXE;
      }

      if (values.contains("minecraft:axes") || currentText.endsWith("_axe")) {
         return InventoryKindData.Kind.AXE;
      }

      if (values.contains("minecraft:shovels") || currentText.endsWith("_shovel")) {
         return InventoryKindData.Kind.SHOVEL;
      }

      if (!values.contains("minecraft:hoes") && !currentText.endsWith("_hoe")) {
         switch (currentText) {
            case "bow":
               return InventoryKindData.Kind.BOW;
            case "crossbow":
               return InventoryKindData.Kind.CROSSBOW;
            case "trident":
               return InventoryKindData.Kind.TRIDENT;
            case "mace":
               return InventoryKindData.Kind.MACE;
            case "shield":
               return InventoryKindData.Kind.SHIELD;
            case "arrow":
            case "spectral_arrow":
            case "tipped_arrow":
               return InventoryKindData.Kind.ARROW;
            case "ender_pearl":
               return InventoryKindData.Kind.PEARL;
            case "golden_apple":
            case "enchanted_golden_apple":
               return InventoryKindData.Kind.HEALING;
            default:
               PotionContents potionContents = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
               if (potionContents != null) {
                  byte byteValue = 0;

                  for (MobEffectInstance mobEffectInstance : potionContents.getAllEffects()) {
                     if (((MobEffect)mobEffectInstance.getEffect().value()).getCategory() == MobEffectCategory.HARMFUL) {
                        return InventoryKindData.Kind.OTHER;
                     }

                     byteValue |= ((MobEffect)mobEffectInstance.getEffect().value()).getCategory() == MobEffectCategory.BENEFICIAL ? 1 : 0;
                  }

                  return byteValue != 0 ? InventoryKindData.Kind.HEALING : InventoryKindData.Kind.OTHER;
               } else if (itemStack.has(DataComponents.FOOD)) {
                  Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
                  return consumable != null
                        && consumable.onConsumeEffects()
                           .stream()
                           .filter(ApplyStatusEffectsConsumeEffect.class::isInstance)
                           .map(ApplyStatusEffectsConsumeEffect.class::cast)
                           .anyMatch(
                              statusEffect -> statusEffect.probability() > 0.0F
                                 && statusEffect.effects()
                                    .stream()
                                    .anyMatch(effect -> ((MobEffect)effect.getEffect().value()).getCategory() == MobEffectCategory.HARMFUL)
                           )
                     ? InventoryKindData.Kind.OTHER
                     : InventoryKindData.Kind.FOOD;
               } else if (text.contains(currentText)
                  || currentText.endsWith("_shulker_box")
                  || currentText.equals("shulker_box")
                  || currentText.endsWith("_smithing_template")) {
                  return InventoryKindData.Kind.VALUABLE;
               } else if (text2.contains(currentText)) {
                  return InventoryKindData.Kind.UTILITY;
               } else {
                  if (itemStack.getItem() instanceof BlockItem blockItem) {
                     Block block = blockItem.getBlock();
                     if (!(block instanceof FallingBlock)
                        && !currentText.equals("tnt")
                        && !currentText.equals("magma_block")
                        && block.defaultBlockState().isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
                        return InventoryKindData.Kind.BLOCK;
                     }
                  }

                  return InventoryKindData.Kind.OTHER;
               }
         }
      } else {
         return InventoryKindData.Kind.HOE;
      }
   }

   private static double calculateValue(ItemStack itemStack, Holder<Attribute> holder, double doubleValue, EquipmentSlot equipmentSlot) {
      double currentDoubleValue = doubleValue;
      double nextDoubleValue = 0.0;
      double previousDoubleValue = 1.0;

      for (net.minecraft.world.item.component.ItemAttributeModifiers.Entry entry : ((ItemAttributeModifiers)itemStack.getOrDefault(
            DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY
         ))
         .modifiers()) {
         if (entry.attribute().equals(holder) && entry.slot().test(equipmentSlot)) {
            double sourceDoubleValue = entry.modifier().amount();
            if (Double.isFinite(sourceDoubleValue)) {
               switch (entry.modifier().operation()) {
                  case ADD_VALUE:
                     currentDoubleValue += sourceDoubleValue;
                     break;
                  case ADD_MULTIPLIED_BASE:
                     nextDoubleValue += sourceDoubleValue;
                     break;
                  case ADD_MULTIPLIED_TOTAL:
                     previousDoubleValue *= 1.0 + sourceDoubleValue;
               }
            }
         }
      }

      return Math.max(0.0, (currentDoubleValue + currentDoubleValue * nextDoubleValue) * previousDoubleValue);
   }
}
