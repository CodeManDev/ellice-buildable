package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatActivateService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.survival.SurvivalAssessService;
import dev.felix.ellice.feature.survival.SurvivalEnvironmentTracker;
import dev.felix.ellice.feature.survival.SurvivalItemData;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;

public final class AutoEatModule extends ModuleSettingsService {
  private final ModuleNameService moduleNameService =
      this.settingCategory("Consumption")
          .description(
              "Eats the best stored food by saturation. Healing bites enable health regen when"
                  + " critically hurt.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Inventory & timing")
          .description(
              "Moves stored food into the hotbar with a predicted swap, without opening any screen."
                  + " Manual input takes priority.");
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Eat below (food)", 14.0F, 0.0F, 19.0F, 1.0F)
              .description(
                  "Vanilla food points, from 0 to 20. Eats the highest-saturation food"
                      + " available."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Eat to heal", true)
              .description(
                  "Eats below the critical health threshold even when not hungry, so health can"
                      + " regenerate."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Number("Critical below (hearts)", 5.0F, 0.5F, 10.0F, 0.5F)
              .description(
                  "Healing bites trigger at or below this health, with room left to regenerate."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Refill hotbar", true)
              .description(
                  "Swaps the best stored food into the first empty hotbar slot, otherwise the"
                      + " selected one."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Bool("Restore selected slot", true)
              .description(
                  "Returns to your previous hotbar selection after eating, unless you changed it"
                      + " yourself."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Action interval (ms)", 100.0F, 50.0F, 500.0F, 25.0F)
              .description(
                  "Minimum spacing for eating actions. At most one is sent per client tick,"
                      + " including after lag."));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Number("Server wait (ms)", 2000.0F, 500.0F, 6000.0F, 100.0F)
              .description(
                  "Waits for the server to confirm the bite before stopping. Eating itself gets at"
                      + " least 3 seconds."));
  private final SurvivalEnvironmentTracker survivalEnvironmentTracker =
      new SurvivalEnvironmentTracker();
  private final CompatActivateService compatActivateService = new CompatActivateService();

  public AutoEatModule() {
    super(
        ModuleBuilderData.builder("AutoEat")
            .category(ModuleFeatureType.PLAYER)
            .description(
                "Eats the best food by saturation and refills the hotbar through Vanilla inventory"
                    + " interactions.")
            .build());
    this.moduleNumber2.visibleWhen(this.moduleBool);
  }

  @Override
  protected void onEnable() {
    this.survivalEnvironmentTracker.reset();
    this.compatActivateService.activate();
    this.on(EventAttackInputService.TICK)
        .run(
            item ->
                this.survivalEnvironmentTracker.tick(
                    System.nanoTime() / 1000000L, this.compatActivateService, this.policy()));
    this.on(EventAttackInputService.WORLD)
        .run(
            item -> {
              this.survivalEnvironmentTracker.stop(
                  this.compatActivateService, (Boolean) this.moduleBool3.get());
              this.compatActivateService.activate();
            });
  }

  @Override
  protected void onDisable() {
    this.survivalEnvironmentTracker.stop(
        this.compatActivateService, (Boolean) this.moduleBool3.get());
    this.compatActivateService.deactivate();
  }

  public String status() {
    SurvivalItemData survivalItemData = this.compatActivateService.capture();
    String text = "";
    if (survivalItemData != null) {
      SurvivalAssessService.Advice advice =
          SurvivalAssessService.assess(
              survivalItemData.vitals(),
              new SurvivalAssessService.Policy(
                  (Float) this.moduleNumber2.get(),
                  ((Float) this.moduleNumber.get()).intValue(),
                  60,
                  3.5));
      if (advice.threat() != SurvivalAssessService.Threat.NONE) {
        text = advice.reason() + " · ";
      }
    }

    return text + this.survivalEnvironmentTracker.status();
  }

  public SurvivalEnvironmentTracker.Policy policy() {
    return new SurvivalEnvironmentTracker.Policy(
        ((Float) this.moduleNumber.get()).intValue(),
        this.moduleBool.get() ? (Float) this.moduleNumber2.get() : 0.0F,
        (Boolean) this.moduleBool2.get(),
        ((Float) this.moduleNumber3.get()).intValue(),
        ((Float) this.moduleNumber4.get()).intValue(),
        (Boolean) this.moduleBool3.get());
  }
}
