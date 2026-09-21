package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatRememberInteractionService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.inventory.InventoryData;
import dev.felix.ellice.feature.inventory.InventoryEnvironmentTracker;
import dev.felix.ellice.feature.inventory.InventoryKindData;
import dev.felix.ellice.feature.inventory.InventoryLootPlanner;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.phys.BlockHitResult;

public final class ImplRememberInteractionService extends ModuleSettingsService {
  private final ModuleNameService moduleNameService =
      this.settingCategory("Selection")
          .description(
              "Compares chest contents with equipped armor, the hotbar, inventory, and offhand.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Stock targets")
          .description(
              "Refills using whole stacks. The final stack may exceed a target; nothing is"
                  + " discarded.");
  private final ModuleNameService moduleNameService3 =
      this.settingCategory("Pacing")
          .description(
              "One normal inventory action at a time, with reading and inspection pauses.");
  private final ModuleNameService moduleNameService4 =
      this.settingCategory("Controls")
          .description(
              "Controls activation and containers. Your own slot input always pauses collection.");
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Mode("Loot mode", new String[] {"Smart", "Everything"}, "Smart")
              .description(
                  "Smart takes equipment upgrades and useful supplies. Everything collects all"
                      + " categories; exclusions still apply."));
  private final ModuleSetting.MultiSelect moduleMultiSelect =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.MultiSelect(
                  "Take items",
                  new String[] {
                    "Armor", "Weapons", "Tools", "Food", "Blocks", "Supplies", "Valuables", "Other"
                  },
                  "Armor",
                  "Weapons",
                  "Tools",
                  "Food",
                  "Blocks",
                  "Supplies",
                  "Valuables")
              .description(
                  "Selects item groups for Smart mode. Silk Touch tools are compared separately"
                      + " from ordinary and Fortune tools."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Skip cursed gear", true)
              .description(
                  "Leaves items with Curse of Binding or Curse of Vanishing in the container."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Minimum durability (%)", 10.0F, 0.0F, 100.0F, 1.0F)
              .description(
                  "Leaves equipment below this remaining durability. Armor protection, toughness,"
                      + " enchantments and wear all affect upgrade ranking."));
  private final ModuleSetting.Text moduleText =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Text("Ignore items", "", 1024)
              .description(
                  "Comma-separated item IDs to leave behind, such as minecraft:cobblestone,"
                      + " minecraft:rotten_flesh. Applies in both modes."));
  private final ModuleSetting.Number moduleNumber2 = this.createNumber("Block target", 192, 512);
  private final ModuleSetting.Number moduleNumber3 = this.createNumber("Food target", 32, 128);
  private final ModuleSetting.Number moduleNumber4 = this.createNumber("Arrow target", 64, 256);
  private final ModuleSetting.Number moduleNumber5 = this.createNumber("Pearl target", 16, 64);
  private final ModuleSetting.Number moduleNumber6 = this.createNumber("Healing target", 8, 64);
  private final ModuleSetting.Number moduleNumber7 = this.createNumber("Utility target", 16, 128);
  private final ModuleSetting.Number moduleNumber8 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Keep free slots", 0.0F, 0.0F, 9.0F, 1.0F)
              .description(
                  "Keeps this many inventory slots empty. Matching stacks can still merge without"
                      + " using another slot."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Thoughtful pacing", true)
              .description(
                  "Adds time to inspect equipment and reach distant slots, with a varied tempo and"
                      + " occasional short pauses. Does not guarantee human behavior."));
  private final ModuleSetting.Range moduleRange =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Range("Click delay (ms)", 180.0F, 320.0F, 50.0F, 1000.0F, 10.0F)
              .description(
                  "Base delay between transfers. Inspection pauses can add time; with Thoughtful"
                      + " pacing off, the lower value is used."));
  private final ModuleSetting.Range moduleRange2 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Range("Read container (ms)", 300.0F, 500.0F, 100.0F, 1500.0F, 10.0F)
              .description(
                  "Waits after opening a container before selecting its contents. No backlog of"
                      + " clicks is sent after a slow frame."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Close when done", true)
              .description(
                  "Closes after no useful items remain and the closing delay has elapsed. A full"
                      + " inventory or failed transfer keeps the container open."));
  private final ModuleSetting.Number moduleNumber9 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Closing delay (ms)", 600.0F, 250.0F, 2000.0F, 50.0F)
              .description(
                  "Waits after the final inventory change before the normal close action; newly"
                      + " arriving loot cancels closing."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Bool("Only storage blocks", true)
              .description(
                  "Requires opening a chest, barrel, ender chest or shulker block. Turning away"
                      + " while the menu loads is fine. Turn off for virtual chest menus or chest"
                      + " minecarts."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Bool("Include shulker boxes", true)
              .description(
                  "Also collects from open shulker boxes. Crafting tables, furnaces, merchants and"
                      + " player inventories are excluded."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Bool("Hold to collect", false)
              .description(
                  "Collects only while the activation key is held; releasing it pauses pending"
                      + " selections."));
  private final ModuleSetting.Keybind moduleKeybind =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Keybind("Collection key", 342)
              .description(
                  "Key held for Hold to collect. Choose it using the rendered keyboard in"
                      + " ClickGUI."));
  private final InventoryEnvironmentTracker inventoryEnvironmentTracker =
      new InventoryEnvironmentTracker(new Random());
  private final CompatRememberInteractionService compatRememberInteractionService =
      new CompatRememberInteractionService(
          this.moduleBool4::get,
          this.moduleBool5::get,
          this.moduleBool6::get,
          () ->
              ModuleBindService.isKeyboardKey((Integer) this.moduleKeybind.get())
                  ? (Integer) this.moduleKeybind.get()
                  : -1);

  public ImplRememberInteractionService() {
    super(
        ModuleBuilderData.builder("ChestStealer")
            .description(
                "Selects useful chest loot, compares equipment, and refills supplies with paced"
                    + " inventory clicks.")
            .category(ModuleFeatureType.PLAYER)
            .build());
    this.moduleMultiSelect.visibleWhen(this.moduleMode, "Smart"::equals);

    for (ModuleSetting.Number number :
        List.of(
            this.moduleNumber2,
            this.moduleNumber3,
            this.moduleNumber4,
            this.moduleNumber5,
            this.moduleNumber6,
            this.moduleNumber7)) {
      number.visibleWhen(this.moduleMode, "Smart"::equals);
    }

    this.moduleNumber9.visibleWhen(this.moduleBool3);
    this.moduleKeybind.visibleWhen(this.moduleBool6);
  }

  private ModuleSetting.Number createNumber(String text, int value, int currentValue) {
    return this.setting(
        this.moduleNameService2,
        new ModuleSetting.Number(text, value, 0.0F, currentValue, 1.0F)
            .description(
                "Collects whole stacks until this item group's carried count reaches the target."
                    + " Zero skips the group; the last stack can exceed the target."));
  }

  @Override
  protected void onEnable() {
    this.inventoryEnvironmentTracker.reset();
    this.compatRememberInteractionService.reset();
    this.on(EventAttackInputService.TICK)
        .run(
            item ->
                this.inventoryEnvironmentTracker.tick(
                    System.nanoTime() / 1000000L,
                    this.compatRememberInteractionService,
                    this.policy(),
                    this.createTiming()));
    this.on(EventAttackInputService.WORLD)
        .run(
            item -> {
              this.inventoryEnvironmentTracker.reset();
              this.compatRememberInteractionService.reset();
            });
  }

  @Override
  protected void onDisable() {
    this.inventoryEnvironmentTracker.reset();
    this.compatRememberInteractionService.reset();
  }

  public void rememberInteraction(BlockHitResult blockHitResult, boolean enabled) {
    if (this.isEnabled()) {
      this.compatRememberInteractionService.rememberInteraction(blockHitResult, enabled);
    }
  }

  public String status() {
    return this.inventoryEnvironmentTracker.status();
  }

  public InventoryLootPlanner.Plan lastPlan() {
    return this.inventoryEnvironmentTracker.lastPlan();
  }

  public InventoryData policy() {
    EnumSet enumSet = EnumSet.noneOf(InventoryKindData.Kind.class);

    for (InventoryKindData.Kind kind : InventoryKindData.Kind.values()) {
      String text;
      if (kind.armor()) {
        text = "Armor";
      } else if (kind.tool()) {
        text = "Tools";
      } else if (kind.equipment()) {
        text = "Weapons";
      } else {
        switch (kind) {
          case FOOD:
            text = "Food";
            break;
          case BLOCK:
            text = "Blocks";
            break;
          case ARROW:
          case PEARL:
          case HEALING:
          case UTILITY:
            text = "Supplies";
            break;
          case VALUABLE:
            text = "Valuables";
            break;
          default:
            text = "Other";
        }
      }

      String currentText = text;
      if (((Set) this.moduleMultiSelect.get()).contains(currentText)) {
        enumSet.add(kind);
      }
    }

    Set currentValues =
        Arrays.stream(((String) this.moduleText.get()).toLowerCase(Locale.ROOT).split("[,\\s]+"))
            .map(String::trim)
            .filter(item -> !item.isEmpty())
            .map(item -> (String) (item.contains(":") ? item : "minecraft:" + item))
            .collect(Collectors.toSet());
    return new InventoryData(
        ((String) this.moduleMode.get()).equals("Smart"),
        enumSet,
        Map.of(
            InventoryKindData.Kind.BLOCK,
            ((Float) this.moduleNumber2.get()).intValue(),
            InventoryKindData.Kind.FOOD,
            ((Float) this.moduleNumber3.get()).intValue(),
            InventoryKindData.Kind.ARROW,
            ((Float) this.moduleNumber4.get()).intValue(),
            InventoryKindData.Kind.PEARL,
            ((Float) this.moduleNumber5.get()).intValue(),
            InventoryKindData.Kind.HEALING,
            ((Float) this.moduleNumber6.get()).intValue(),
            InventoryKindData.Kind.UTILITY,
            ((Float) this.moduleNumber7.get()).intValue()),
        (Boolean) this.moduleBool.get(),
        (Float) this.moduleNumber.get() / 100.0F,
        ((Float) this.moduleNumber8.get()).intValue(),
        currentValues);
  }

  private InventoryEnvironmentTracker.Timing createTiming() {
    return new InventoryEnvironmentTracker.Timing(
        (int) this.moduleRange2.low(),
        (int) this.moduleRange2.high(),
        (int) this.moduleRange.low(),
        (int) this.moduleRange.high(),
        (Boolean) this.moduleBool2.get(),
        (Boolean) this.moduleBool3.get(),
        ((Float) this.moduleNumber9.get()).intValue());
  }
}
