package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CompatBeginTickService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.rod.RodStateService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class AutoRodModule extends ModuleSettingsService {
   private final ModuleNameService moduleNameService = this.settingCategory("Items")
      .description("Hotbar and offhand support items; your previous slot returns after use.");
   private final ModuleNameService moduleNameService2 = this.settingCategory("Combat timing")
      .description("Shares KillAura's opponent and gives ready melee attacks priority.");
   private final ModuleNameService moduleNameService3 = this.settingCategory("Aim")
      .description("Moving-target prediction, collision checks and corrected movement.");
   private final ModuleSetting.Bool moduleBool = this.setting(this.moduleNameService, new ModuleSetting.Bool("Fishing rod", true));
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Save rod durability", true)
         .description(
            "Resets the hook by switching away instead of pulling. Keep rods out of the offhand. Off enables active reeling, which costs durability."
         )
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(this.moduleNameService, new ModuleSetting.Bool("Snowballs", true));
   private final ModuleSetting.Bool moduleBool4 = this.setting(this.moduleNameService, new ModuleSetting.Bool("Eggs", false));
   private final ModuleSetting.Bool moduleBool5 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Require KillAura target", false)
         .description("Otherwise also chooses nearby opponents when KillAura has no target. Friends and teammates are excluded.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
         "Rod range",
         7.0F,
         3.0F,
         9.0F,
         0.25F
      )
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
         "Throw range",
         10.0F,
         3.0F,
         16.0F,
         0.5F
      )
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
         "Rod interval (ms)",
         700.0F,
         400.0F,
         2500.0F,
         50.0F
      )
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
            "Throw interval (ms)",
            200.0F,
            200.0F,
            1000.0F,
            50.0F
         )
         .description("At least four game ticks between throws, also respecting the native item cooldown.")
   );
   private final ModuleSetting.Number moduleNumber5 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number("Throws per burst", 3.0F, 1.0F, 8.0F, 1.0F)
   );
   private final ModuleSetting.Number moduleNumber6 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
         "Burst pause (ms)",
         400.0F,
         200.0F,
         2000.0F,
         50.0F
      )
   );
   private final ModuleSetting.Number moduleNumber7 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
         "Turn speed", 65.0F, 10.0F, 180.0F, 1.0F
      )
   );
   private final ModuleSetting.Number moduleNumber8 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
            "Prediction coverage",
            60.0F,
            40.0F,
            90.0F,
            5.0F
         )
         .description("Required coverage of learned motion forecasts. Higher waits for clearer shots; it is not a guaranteed hit rate.")
   );
   private final RodStateService rodStateService = new RodStateService();
   private final CompatOptionsTracker compatOptionsTracker = new CompatOptionsTracker();
   private long timestamp;

   public AutoRodModule() {
      super(
         ModuleBuilderData.builder("AutoRod")
            .category(ModuleFeatureType.COMBAT)
            .description("Times fishing-rod casts and aimed snowball/egg bursts around KillAura's melee windows.")
            .build()
      );
      this.moduleNumber.visibleWhen(this.moduleBool);
      this.moduleNumber3.visibleWhen(this.moduleBool);
      this.moduleBool2.visibleWhen(this.moduleBool);
   }

   private RodStateService.Policy createPolicy() {
      return new RodStateService.Policy(
         Math.round((Float)this.moduleNumber3.get() / 50.0F),
         Math.round((Float)this.moduleNumber4.get() / 50.0F),
         Math.round((Float)this.moduleNumber5.get()),
         Math.round((Float)this.moduleNumber6.get() / 50.0F),
         30,
         (Boolean)this.moduleBool2.get()
      );
   }

   @Override
   protected void onEnable() {
      this.rodStateService.reset(this.compatOptionsTracker);
      this.compatOptionsTracker.clearTracking();
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT)
         .priority(EventIsAfterHandler.Priority.FIRST)
         .run(
            item -> {
               this.compatOptionsTracker
                  .update(
                     ++this.timestamp,
                     new CompatOptionsTracker.Options(
                        (Boolean)this.moduleBool.get(),
                        (Boolean)this.moduleBool3.get(),
                        (Boolean)this.moduleBool4.get(),
                        (Boolean)this.moduleBool5.get(),
                        ((Float)this.moduleNumber.get()).floatValue(),
                        ((Float)this.moduleNumber2.get()).floatValue(),
                        ((Float)this.moduleNumber7.get()).floatValue(),
                        ((Float)this.moduleNumber8.get()).floatValue() / 100.0,
                        (Boolean)this.moduleBool2.get()
                     )
                  );
               this.rodStateService.before(this.timestamp, this.compatOptionsTracker, this.createPolicy());
            }
         );
      this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE)
         .priority(EventIsAfterHandler.Priority.EARLY)
         .run(item -> this.compatOptionsTracker.reconcile(this.timestamp));
      this.on(EventAttackInputService.ATTACK_INPUT).priority(EventIsAfterHandler.Priority.EARLY).run(item -> {
         if (CompatBeginTickService.canAct()) {
            this.rodStateService.after(this.timestamp, this.compatOptionsTracker, this.createPolicy());
         }
      });
      this.on(EventAttackInputService.TICK).priority(EventIsAfterHandler.Priority.EARLY).run(item -> this.compatOptionsTracker.watchdog());
      this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED).run(item -> this.compatOptionsTracker.accepted(item.packet()));
      this.on(EventAttackInputService.WORLD).run(item -> {
         this.rodStateService.reset(this.compatOptionsTracker);
         this.compatOptionsTracker.clearTracking();
      });
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> {
         this.rodStateService.stop(this.compatOptionsTracker);
         this.compatOptionsTracker.clearTracking();
      });
   }

   @Override
   protected void onDisable() {
      this.rodStateService.reset(this.compatOptionsTracker);
      this.compatOptionsTracker.clearTracking();
   }

   public String status() {
      return this.rodStateService.status();
   }
}
