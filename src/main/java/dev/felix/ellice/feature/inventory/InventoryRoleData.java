package dev.felix.ellice.feature.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record InventoryRoleData(List<InventoryRoleData.Role> slots) {
  public static final InventoryRoleData BALANCED =
      of(
          InventoryRoleData.Role.SWORD,
          InventoryRoleData.Role.PICKAXE,
          InventoryRoleData.Role.AXE,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.FOOD,
          InventoryRoleData.Role.HEALING,
          InventoryRoleData.Role.PEARLS,
          InventoryRoleData.Role.UTILITY);
  public static final InventoryRoleData COMBAT =
      of(
          InventoryRoleData.Role.SWORD,
          InventoryRoleData.Role.RANGED,
          InventoryRoleData.Role.PEARLS,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.HEALING,
          InventoryRoleData.Role.FOOD,
          InventoryRoleData.Role.SHIELD,
          InventoryRoleData.Role.TOTEM,
          InventoryRoleData.Role.UTILITY);
  public static final InventoryRoleData BUILDING =
      of(
          InventoryRoleData.Role.PICKAXE,
          InventoryRoleData.Role.AXE,
          InventoryRoleData.Role.SHOVEL,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.BLOCKS,
          InventoryRoleData.Role.FOOD,
          InventoryRoleData.Role.UTILITY);

  public InventoryRoleData(List<InventoryRoleData.Role> slots) {
    slots = List.copyOf(slots);
    if (slots.size() != 9) {
      throw new IllegalArgumentException("A hotbar has exactly nine slots");
    }

    this.slots = slots;
  }

  public static InventoryRoleData of(InventoryRoleData.Role... roles) {
    return new InventoryRoleData(Arrays.asList(roles));
  }

  public static InventoryRoleData parse(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Missing hotbar layout");
    } else {
      return new InventoryRoleData(
          Arrays.stream(text.split(",", -1))
              .map(String::trim)
              .map(InventoryRoleData.Role::valueOf)
              .toList());
    }
  }

  public String encode() {
    return this.slots.stream().map(Enum::name).collect(Collectors.joining(","));
  }

  public InventoryRoleData with(int value, InventoryRoleData.Role role) {
    ArrayList arrayList = new ArrayList<>(this.slots);
    arrayList.set(value, role);
    return new InventoryRoleData(arrayList);
  }

  public InventoryRoleData swap(int value, int currentValue) {
    ArrayList arrayList = new ArrayList<>(this.slots);
    Collections.swap(arrayList, value, currentValue);
    return new InventoryRoleData(arrayList);
  }

  public boolean locked(int value) {
    return value >= 36
        && value <= 44
        && this.slots.get(value - 36) == InventoryRoleData.Role.LOCKED;
  }

  public enum Role {
    LOCKED(
        "Locked", "lock", "Keep this slot exactly as it is; its item is never used as a source."),
    SWORD("Sword", "swords", "Best sword by damage, speed, enchantments and durability."),
    PICKAXE(
        "Pickaxe", "construction", "Best available pickaxe; the current item wins equal scores."),
    AXE("Axe", "construction", "Best axe for damage and harvesting."),
    SHOVEL("Shovel", "construction", "Best shovel, including Efficiency and remaining durability."),
    HOE("Hoe", "construction", "Best hoe for harvesting."),
    RANGED("Ranged", "target", "A bow or crossbow, ranked by its enchantments and durability."),
    BLOCKS(
        "Blocks",
        "deployed_code",
        "A large stack of solid building blocks; multiple slots use distinct stacks."),
    FOOD("Food", "restaurant", "Nutritious food first; harmful food is excluded."),
    HEALING("Healing", "favorite", "Golden apples or beneficial potions."),
    PEARLS("Pearls", "trip_origin", "Ender pearls, preferring the largest stack."),
    SHIELD("Shield", "shield", "Best shield by enchantments and durability."),
    UTILITY("Utility", "handyman", "Buckets, fireworks, torches and other utility items."),
    TOTEM("Totem", "shield", "A Totem of Undying."),
    SILK_TOUCH("Silk Touch", "diamond", "A pickaxe with Silk Touch; kept separate from Fortune."),
    FORTUNE("Fortune", "diamond", "A pickaxe with Fortune.");

    private final String text;
    private final String text2;
    private final String text3;

    Role(String currentText, String nextText, String previousText) {
      this.text = currentText;
      this.text2 = nextText;
      this.text3 = previousText;
    }

    public String title() {
      return this.text;
    }

    public String icon() {
      return this.text2;
    }

    public String help() {
      return this.text3;
    }

    public boolean accepts(InventoryKindData inventoryKindData) {
      if (inventoryKindData == null) {
        return false;
      }

      return (boolean)
          (switch (this) {
            case LOCKED -> 0;
            case SWORD -> inventoryKindData.kind() == InventoryKindData.Kind.SWORD ? 1 : 0;
            case PICKAXE -> inventoryKindData.kind() == InventoryKindData.Kind.PICKAXE ? 1 : 0;
            case AXE -> inventoryKindData.kind() == InventoryKindData.Kind.AXE ? 1 : 0;
            case SHOVEL -> inventoryKindData.kind() == InventoryKindData.Kind.SHOVEL ? 1 : 0;
            case HOE -> inventoryKindData.kind() == InventoryKindData.Kind.HOE ? 1 : 0;
            case RANGED ->
                inventoryKindData.kind() != InventoryKindData.Kind.BOW
                        && inventoryKindData.kind() != InventoryKindData.Kind.CROSSBOW
                    ? 0
                    : 1;
            case BLOCKS -> inventoryKindData.kind() == InventoryKindData.Kind.BLOCK ? 1 : 0;
            case FOOD -> inventoryKindData.kind() == InventoryKindData.Kind.FOOD ? 1 : 0;
            case HEALING -> inventoryKindData.kind() == InventoryKindData.Kind.HEALING ? 1 : 0;
            case PEARLS -> inventoryKindData.kind() == InventoryKindData.Kind.PEARL ? 1 : 0;
            case SHIELD -> inventoryKindData.kind() == InventoryKindData.Kind.SHIELD ? 1 : 0;
            case UTILITY -> inventoryKindData.kind() == InventoryKindData.Kind.UTILITY ? 1 : 0;
            case TOTEM -> inventoryKindData.id().equals("minecraft:totem_of_undying");
            case SILK_TOUCH ->
                inventoryKindData.kind() == InventoryKindData.Kind.PICKAXE
                        && inventoryKindData.stats().enchantment("silk_touch") > 0
                    ? 1
                    : 0;
            case FORTUNE ->
                inventoryKindData.kind() == InventoryKindData.Kind.PICKAXE
                        && inventoryKindData.stats().enchantment("fortune") > 0
                    ? 1
                    : 0;
          });
    }

    private static InventoryRoleData.Role[] $values() {
      return new InventoryRoleData.Role[] {
        LOCKED,
        SWORD,
        PICKAXE,
        AXE,
        SHOVEL,
        HOE,
        RANGED,
        BLOCKS,
        FOOD,
        HEALING,
        PEARLS,
        SHIELD,
        UTILITY,
        TOTEM,
        SILK_TOUCH,
        FORTUNE
      };
    }
  }
}
