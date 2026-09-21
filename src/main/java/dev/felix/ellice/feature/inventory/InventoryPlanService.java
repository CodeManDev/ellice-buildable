package dev.felix.ellice.feature.inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class InventoryPlanService {
  private static final Set<String> text =
      Set.of("minecraft:rotten_flesh", "minecraft:poisonous_potato", "minecraft:dead_bush");

  public Plan plan(InventoryCellData inventoryCellData, Policy policy) {
    if (policy.autoArmor()) {
      Plan armorPlan = this.createPlan2(inventoryCellData, policy);
      if (!armorPlan.actions().isEmpty()) {
        return armorPlan;
      }
    }
    if (policy.sortHotbar()) {
      int n;
      Set<Integer> object = new HashSet<>();
      for (n = 36; n <= 44; ++n) {
        if (!policy.layout().locked(n)) continue;
        object.add(n);
      }
      for (n = 36; n <= 44; ++n) {
        InventoryRoleData.Role role = policy.layout().slots().get(n - 36);
        if (role == InventoryRoleData.Role.LOCKED) continue;
        InventoryCellData.Cell cell = null;
        double d = Double.longBitsToDouble(-4616189618054758400L);
        for (InventoryCellData.Cell cell2 : inventoryCellData.cells()) {
          if (!InventoryPlanService.checkCondition2(cell2, policy)
              || object.contains(cell2.index())
              || !role.accepts(cell2.item())
              || role == InventoryRoleData.Role.PICKAXE
                  && (cell2.item().stats().enchantment("silk_touch") > 0
                          && policy
                              .layout()
                              .slots()
                              .contains((Object) InventoryRoleData.Role.SILK_TOUCH)
                      || cell2.item().stats().enchantment("fortune") > 0
                          && policy
                              .layout()
                              .slots()
                              .contains((Object) InventoryRoleData.Role.FORTUNE))) continue;
          double d2 = InventoryPlanService.mgal2fcoyypo(cell2.item(), inventoryCellData.owned());
          if (cell != null
              && !(d2 > d + Double.longBitsToDouble(4562254508917369340L))
              && (!(Math.abs(d2 - d) <= Double.longBitsToDouble(4562254508917369340L))
                  || cell2.index() != n)) continue;
          cell = cell2;
          d = d2;
        }
        if (cell == null) continue;
        if (cell.index() != n && inventoryCellData.cell(n).mayTake()) {
          return new Plan(
              List.of(
                  InventoryPlanService.createAction(
                      inventoryCellData, cell.index(), n, Input.SWAP)),
              "Slot " + (n - 35) + ": " + cell.item().name());
        }
        object.add(n);
      }
    }
    return this.mfii81tdx0u1(inventoryCellData, policy);
  }

  private Plan mfii81tdx0u1(InventoryCellData inventoryCellData, Policy policy) {
    HashSet<Integer> hashSet = new HashSet<Integer>();
    HashMap<String, InventoryCellData.Cell> hashMap = new HashMap<String, InventoryCellData.Cell>();
    List<InventoryKindData> list = inventoryCellData.owned();
    for (InventoryCellData.Cell cell3 : inventoryCellData.cells()) {
      if (cell3.item() == null
          || cell3.index() >= 9
              && cell3.index() <= 44
              && cell3.mayTake()
              && !policy.layout().locked(cell3.index())
              && (cell3.index() < 36
                  || !policy.layout().slots().get(cell3.index() - 36).accepts(cell3.item())))
        continue;
      hashSet.add(cell3.index());
    }
    if (policy.dropDuplicates()) {
      for (InventoryCellData.Cell cell3 : inventoryCellData.cells()) {
        if (cell3.item() == null
            || !cell3.item().kind().equipment()
            || !InventoryPlanService.mfbnxqf13lmv(cell3.item(), policy)) continue;
        hashMap.merge(
            InventoryPlanService.mgdh4oojpo9k(cell3.item()),
            cell3,
            (cell, cell2) -> {
              double d =
                  InventoryScoreService.score(cell2.item(), list)
                      - InventoryScoreService.score(cell.item(), list);
              if (Math.abs(d) <= Double.longBitsToDouble(4562254508917369340L)) {
                if (hashSet.contains(cell2.index()) != hashSet.contains(cell.index())) {
                  return hashSet.contains(cell2.index()) ? cell2 : cell;
                }
                return cell2.index() < cell.index() ? cell2 : cell;
              }
              return d > 0.0 ? cell2 : cell;
            });
      }
      for (InventoryCellData.Cell cell3 : hashMap.values()) {
        hashSet.add(cell3.index());
      }
    }
    for (InventoryCellData.Cell cell3 : inventoryCellData.cells()) {
      int n;
      if (cell3.item() == null || hashSet.contains(cell3.index())) continue;
      int n2 = policy.dropJunk() && text.contains(cell3.item().id()) ? 1 : 0;
      int n3 =
          n =
              policy.dropDuplicates()
                      && cell3.item().kind().equipment()
                      && hashMap.containsKey(InventoryPlanService.mgdh4oojpo9k(cell3.item()))
                  ? 1
                  : 0;
      if (n2 == 0 && n == 0) continue;
      return new Plan(
          List.of(new Action(cell3.index(), -1, Input.THROW, cell3.item(), null)),
          "Drop " + cell3.item().name());
    }
    return Plan.idle("Armor, hotbar and cleanup are ready");
  }

  private static String mgdh4oojpo9k(InventoryKindData inventoryKindData) {
    return inventoryKindData.equipmentGroup()
        + (inventoryKindData.kind() == InventoryKindData.Kind.TRIDENT
                && inventoryKindData.stats().enchantment("riptide") > 0
            ? ":riptide"
            : "");
  }

  private static boolean mfbnxqf13lmv(InventoryKindData inventoryKindData, Policy policy) {
    return !(policy.skipCurses() && inventoryKindData.stats().cursed()
        || inventoryKindData.kind().equipment()
            && !(inventoryKindData.stats().durability() >= policy.minimumDurability()));
  }

  private Plan createPlan2(InventoryCellData inventoryCellData, Policy policy) {
    List<InventoryKindData.Kind> list =
        List.of(
            InventoryKindData.Kind.HELMET,
            InventoryKindData.Kind.CHESTPLATE,
            InventoryKindData.Kind.LEGGINGS,
            InventoryKindData.Kind.BOOTS);
    for (int i = 0; i < list.size(); ++i) {
      InventoryCellData.Cell cell = inventoryCellData.cell(5 + i);
      if (!cell.mayTake()
          || cell.item() != null
              && (cell.item().stats().enchantment("binding_curse") > 0
                  || policy.keepElytra() && cell.item().kind() == InventoryKindData.Kind.ELYTRA))
        continue;
      InventoryCellData.Cell cell2 = null;
      double d =
          cell.item() == null
              ? Double.longBitsToDouble(-4616189618054758400L)
              : InventoryScoreService.score(cell.item(), inventoryCellData.owned());
      double d2 = d + Double.longBitsToDouble(4598175219545276416L);
      for (InventoryCellData.Cell cell3 : inventoryCellData.cells()) {
        double d3;
        if (!InventoryPlanService.checkCondition2(cell3, policy)
            || cell3.item().kind() != list.get(i)
            || !((d3 = InventoryScoreService.score(cell3.item(), inventoryCellData.owned())) > d2))
          continue;
        cell2 = cell3;
        d2 = d3;
      }
      if (cell2 == null) continue;
      String string = "Equip " + cell2.item().name();
      if (cell.item() == null) {
        return new Plan(
            List.of(
                InventoryPlanService.createAction(
                    inventoryCellData, cell2.index(), cell.index(), Input.QUICK_MOVE)),
            string);
      }
      if (cell2.index() >= 36) {
        return new Plan(
            List.of(
                InventoryPlanService.createAction(
                    inventoryCellData, cell.index(), cell2.index(), Input.SWAP)),
            string);
      }
      int n = -1;
      for (int j = 44; j >= 36; j += -1) {
        if (policy.layout().locked(j) || !inventoryCellData.cell(j).mayTake()) continue;
        n = j;
        break;
      }
      if (n < 0) continue;
      InventoryKindData inventoryKindData = inventoryCellData.cell(n).item();
      return new Plan(
          List.of(
              InventoryPlanService.createAction(inventoryCellData, cell2.index(), n, Input.SWAP),
              new Action(cell.index(), n, Input.SWAP, cell.item(), cell2.item()),
              new Action(cell2.index(), n, Input.SWAP, inventoryKindData, cell.item())),
          string);
    }
    return Plan.idle("No armor upgrade available");
  }

  private static boolean checkCondition2(InventoryCellData.Cell cell, Policy policy) {
    return (cell.index() >= 9
                && cell.index() <= 44
                && cell.item() != null
                && cell.mayTake()
                && !policy.layout().locked(cell.index())
                && InventoryPlanService.mfbnxqf13lmv(cell.item(), policy)
            ? 1
            : 0)
        != 0;
  }

  private static double mgal2fcoyypo(
      InventoryKindData inventoryKindData, List<InventoryKindData> list) {
    if (inventoryKindData.kind().equipment()) {
      return InventoryScoreService.score(inventoryKindData, list);
    }
    if (inventoryKindData.kind() == InventoryKindData.Kind.FOOD) {
      return inventoryKindData.stats().foodValue() * Double.longBitsToDouble(4636737291354636288L)
          + (double) inventoryKindData.count();
    }
    return inventoryKindData.count();
  }

  private static Action createAction(
      InventoryCellData inventoryCellData, int n, int n2, Input input) {
    return new Action(
        n, n2, input, inventoryCellData.cell(n).item(), inventoryCellData.cell(n2).item());
  }

  public record Policy(
      InventoryRoleData layout,
      boolean autoArmor,
      boolean sortHotbar,
      boolean keepElytra,
      boolean skipCurses,
      double minimumDurability,
      boolean dropJunk,
      boolean dropDuplicates) {
    public Policy(
        InventoryRoleData inventoryRoleData,
        boolean bl,
        boolean bl2,
        boolean bl3,
        boolean bl4,
        double d) {
      this(inventoryRoleData, bl, bl2, bl3, bl4, d, false, false);
    }
  }

  public record Plan(List<Action> actions, String reason) {
    public Plan {
      actions = List.copyOf(actions);
    }

    public static Plan idle(String string) {
      return new Plan(List.of(), string);
    }
  }

  public static enum Input {
    SWAP,
    QUICK_MOVE,
    THROW;
  }

  public record Action(
      int source,
      int destination,
      Input input,
      InventoryKindData sourceItem,
      InventoryKindData destinationItem) {
    public boolean matches(InventoryCellData inventoryCellData) {
      return (Objects.equals(this.sourceItem, inventoryCellData.cell(this.source).item())
                  && (this.input == Input.THROW
                      || Objects.equals(
                          this.destinationItem, inventoryCellData.cell(this.destination).item()))
              ? 1
              : 0)
          != 0;
    }

    public boolean applied(InventoryCellData inventoryCellData) {
      if (this.input == Input.THROW) {
        return inventoryCellData.cell(this.source).item() == null;
      }
      return (Objects.equals(this.destinationItem, inventoryCellData.cell(this.source).item())
                  && Objects.equals(
                      this.sourceItem, inventoryCellData.cell(this.destination).item())
              ? 1
              : 0)
          != 0;
    }
  }
}
