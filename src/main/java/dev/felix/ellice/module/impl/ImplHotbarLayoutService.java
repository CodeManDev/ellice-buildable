package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.InventoryActionExecutor;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.inventory.InventoryActionTracker;
import dev.felix.ellice.feature.inventory.InventoryPlanService;
import dev.felix.ellice.feature.inventory.InventoryRoleData;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleLayoutService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import java.util.Random;

public final class ImplHotbarLayoutService extends ModuleSettingsService {
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("AutoArmor", true)
              .description(
                  "Equips better armor while your inventory is open. Compares protection,"
                      + " toughness, enchantments and durability; replaced armor is kept unless"
                      + " duplicate cleanup is enabled."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          new ModuleSetting.Bool("Organize hotbar", true)
              .description(
                  "Places the best matching items into your chosen hotbar slots while your"
                      + " inventory is open."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          new ModuleSetting.Bool("Drop junk", true)
              .description(
                  "Throws out stacks of rotten flesh, poisonous potatoes and dead bushes while your"
                      + " inventory is open. Other materials and supplies are kept."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          new ModuleSetting.Bool("Drop inferior equipment", true)
              .description(
                  "Throws out worse or equal duplicate equipment after equipping and sorting. Keeps"
                      + " the best usable item per type, Silk Touch and Riptide variants, equipped"
                      + " items and matching hotbar slots. Locked slots are never dropped."));
  private final ModuleLayoutService moduleLayoutService =
      this.setting(
          new ModuleLayoutService("Hotbar layout", InventoryRoleData.BALANCED)
              .description(
                  "Design all nine hotbar slots in the visual editor. Locked slots are never"
                      + " changed or used as a source. Missing roles leave their slots alone."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          new ModuleSetting.Bool("Keep equipped elytra", true)
              .description(
                  "Preserves an equipped elytra instead of replacing it with a chestplate."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          new ModuleSetting.Bool("Skip cursed equipment", true)
              .description(
                  "Avoids gear with Binding or Vanishing. Equipped Curse of Binding armor is always"
                      + " left in place."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number("Minimum durability (%)", 10.0F, 0.0F, 100.0F, 1.0F)
              .description(
                  "Rejects almost-broken replacements and hotbar tools below this remaining"
                      + " durability."));
  private final ModuleSetting.Range moduleRange =
      this.setting(
          new ModuleSetting.Range("Action delay (ms)", 160.0F, 260.0F, 50.0F, 1000.0F, 10.0F)
              .description(
                  "Delay between individual native inventory actions. Mouse input, cursor items,"
                      + " closing the inventory and container changes cancel queued work."));
  private final InventoryActionTracker inventoryEnvironmentTracker2 =
      new InventoryActionTracker(new Random());
  private final InventoryActionExecutor compatCaptureService5 = new InventoryActionExecutor();

  public ImplHotbarLayoutService() {
    super(
        ModuleBuilderData.builder("InventoryManager")
            .category(ModuleFeatureType.PLAYER)
            .description(
                "Equips better armor, organizes your hotbar and drops junk and inferior equipment"
                    + " while your inventory is open.")
            .build());
    this.moduleBool5.visibleWhen(this.moduleBool);
  }

  @Override
  protected void onEnable() {
    this.inventoryEnvironmentTracker2.reset();
    this.on(EventAttackInputService.TICK)
        .run(
            item ->
                this.inventoryEnvironmentTracker2.tick(
                    System.nanoTime() / 1000000L,
                    this.compatCaptureService5,
                    this.policy(),
                    (int) this.moduleRange.low(),
                    (int) this.moduleRange.high()));
    this.on(EventAttackInputService.WORLD).run(item -> this.inventoryEnvironmentTracker2.reset());
  }

  @Override
  protected void onDisable() {
    this.inventoryEnvironmentTracker2.reset();
  }

  public ModuleLayoutService hotbarLayout() {
    return this.moduleLayoutService;
  }

  public String status() {
    return this.inventoryEnvironmentTracker2.status();
  }

  public InventoryPlanService.Policy policy() {
    return new InventoryPlanService.Policy(
        this.moduleLayoutService.layout(),
        (Boolean) this.moduleBool.get(),
        (Boolean) this.moduleBool2.get(),
        (Boolean) this.moduleBool5.get(),
        (Boolean) this.moduleBool6.get(),
        (Float) this.moduleNumber.get() / 100.0F,
        (Boolean) this.moduleBool3.get(),
        (Boolean) this.moduleBool4.get());
  }
}
