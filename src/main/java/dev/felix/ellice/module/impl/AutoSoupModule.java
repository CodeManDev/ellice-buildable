package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.SoupInventoryBridge;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.soup.SoupClickTracker;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class AutoSoupModule extends ModuleSettingsService {
   private final ModuleNameService moduleNameService = this.settingCategory("Consumption")
      .description("Soup PvP expects server-side instant healing. Vanilla keeps the normal eating duration and hunger rules.");
   private final ModuleNameService moduleNameService2 = this.settingCategory("Supplies")
      .description("Refills the hotbar and reuses bowls with red and brown mushrooms. Other items are kept.");
   private final ModuleNameService moduleNameService3 = this.settingCategory("Inventory & timing")
      .description("Uses the real inventory screen and Vanilla's predicted clicks. Manual input takes priority.");
   private final ModuleSetting.Mode moduleMode = this.setting(
      this.moduleNameService, new ModuleSetting.Mode("Soup mode", new String[]{"Soup PvP", "Vanilla"}, "Soup PvP")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number(
            "Use below (hearts)",
            6.5F,
            0.5F,
            20.0F,
            0.5F
         )
         .description("Uses one soup at or below this health, then waits for the server to consume it before reassessing.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number("Use below (food)", 14.0F, 0.0F, 19.0F, 1.0F)
         .description("Vanilla food points, from 0 to 20. Mushroom stew restores 6 food points.")
   );
   private final ModuleSetting.Bool moduleBool = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Restore selected slot", true)
         .description("Returns to your previous hotbar selection after use, unless you changed it yourself.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(this.moduleNameService2, new ModuleSetting.Bool("Refill hotbar", true));
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Soup slot", 9.0F, 1.0F, 9.0F, 1.0F)
         .description("Prefers this hotbar slot, then empty slots and bowls. A displaced item is swapped into the soup's inventory slot.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Auto craft", true)
         .description("Uses a bowl, red mushroom and brown mushroom in the 2×2 inventory grid. Takes only server-supplied mushroom stew.")
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Soup stock", 8.0F, 1.0F, 32.0F, 1.0F)
         .description("Crafts one soup at a time until this carried stock is reached or ingredients/space run out.")
   );
   private final ModuleSetting.Bool moduleBool4 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Recraft when safe", true)
         .description(
            "Maintains stock above 80% health and after 1.5 seconds without damage. If healing is needed and no soup remains, crafts one immediately."
         )
   );
   private final ModuleSetting.Mode moduleMode2 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Mode("Inventory access", new String[]{"Open automatically", "Only while open"}, "Open automatically")
         .description(
            "Automatic opens the actual inventory and closes only screens opened by AutoSoup. Existing menus, cursor items and crafting are respected."
         )
   );
   private final ModuleSetting.Number moduleNumber5 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
            "Action interval (ms)",
            100.0F,
            50.0F,
            500.0F,
            25.0F
         )
         .description("Minimum spacing for inventory actions. At most one is sent per client tick, including after lag.")
   );
   private final ModuleSetting.Number moduleNumber6 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
            "Server wait (ms)",
            2000.0F,
            500.0F,
            6000.0F,
            100.0F
         )
         .description(
            "Waits for recipe/results before stopping and returning ingredients. Item use gets at least 3 seconds for normal eating."
         )
   );
   private final SoupClickTracker soupClickTracker = new SoupClickTracker();
   private final SoupInventoryBridge compatActivateService2 = new SoupInventoryBridge();

   public AutoSoupModule() {
      super(
         ModuleBuilderData.builder("AutoSoup")
            .category(ModuleFeatureType.PLAYER)
            .description("Uses healing soup, refills the hotbar and recrafts through Vanilla inventory interactions.")
            .build()
      );
      this.moduleNumber.visibleWhen(this.moduleMode, "Soup PvP"::equals);
      this.moduleNumber2.visibleWhen(this.moduleMode, "Vanilla"::equals);
      this.moduleNumber3.visibleWhen(this.moduleBool2);
      this.moduleNumber4.visibleWhen(this.moduleBool3);
      this.moduleBool4.visibleWhen(this.moduleBool3);
   }

   @Override
   protected void onEnable() {
      this.soupClickTracker.reset();
      this.compatActivateService2.activate();
      this.on(EventAttackInputService.TICK).run(item -> this.soupClickTracker.tick(System.nanoTime() / 1000000L, this.compatActivateService2, this.policy()));
      this.on(EventAttackInputService.WORLD).run(item -> {
         this.soupClickTracker.stop(this.compatActivateService2);
         this.compatActivateService2.activate();
      });
   }

   @Override
   protected void onDisable() {
      this.soupClickTracker.stop(this.compatActivateService2);
      this.compatActivateService2.deactivate();
   }

   public String status() {
      return this.soupClickTracker.status();
   }

   public SoupClickTracker.Policy policy() {
      return new SoupClickTracker.Policy(
         ((String)this.moduleMode.get()).equals("Soup PvP") ? SoupClickTracker.Mode.SOUP_PVP : SoupClickTracker.Mode.VANILLA,
         (Float)this.moduleNumber.get(),
         ((Float)this.moduleNumber2.get()).intValue(),
         (Boolean)this.moduleBool2.get(),
         ((Float)this.moduleNumber3.get()).intValue() - 1,
         (Boolean)this.moduleBool3.get(),
         ((Float)this.moduleNumber4.get()).intValue(),
         ((String)this.moduleMode2.get()).equals("Open automatically"),
         (Boolean)this.moduleBool4.get(),
         ((Float)this.moduleNumber5.get()).intValue(),
         ((Float)this.moduleNumber6.get()).intValue(),
         (Boolean)this.moduleBool.get()
      );
   }
}
