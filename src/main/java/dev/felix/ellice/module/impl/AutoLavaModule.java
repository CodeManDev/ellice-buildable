package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.bucket.BucketStageService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class AutoLavaModule extends ModuleSettingsService {
   private final ModuleSetting.Bool moduleBool = this.setting(new ModuleSetting.Bool("Target players", true));
   private final ModuleSetting.Bool moduleBool2 = this.setting(new ModuleSetting.Bool("Target mobs", false));
   private final ModuleSetting.Text moduleText = this.setting(
      new ModuleSetting.Text("Excluded players", "", 256)
         .description("Friend names or UUIDs separated by commas. Scoreboard teammates are always excluded.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
            "Range", 4.0F, 2.0F, 4.5F, 0.1F
         )
         .description("Also limited by Vanilla block reach. Tracks moving opponents and places below short jumps when ground is reachable.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      new ModuleSetting.Bool("Recover lava", true)
         .description("Tries to recover only its own lava source; stops if the source or bucket has changed.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
         "Lava duration (ms)",
         500.0F,
         250.0F,
         2000.0F,
         50.0F
      )
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      new ModuleSetting.Number(
         "Placement interval (ms)",
         1500.0F,
         500.0F,
         6000.0F,
         100.0F
      )
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      new ModuleSetting.Number(
         "Server wait (ms)",
         2000.0F,
         500.0F,
         6000.0F,
         100.0F
      )
   );
   private final BucketStageService bucketStageService = new BucketStageService();
   private final CompatReleaseTracker values = new CompatReleaseTracker(CompatReleaseTracker.Purpose.LAVA);

   public AutoLavaModule() {
      super(
         ModuleBuilderData.builder("AutoLava")
            .category(ModuleFeatureType.COMBAT)
            .description("Places a hotbar/offhand lava bucket at an opponent's feet when reach, terrain and self-distance allow it.")
            .build()
      );
      this.moduleNumber2.visibleWhen(this.moduleBool3);
   }

   private BucketStageService.Policy createPolicy() {
      return new BucketStageService.Policy(
         (Boolean)this.moduleBool3.get(),
         ((Float)this.moduleNumber3.get()).longValue(),
         ((Float)this.moduleNumber4.get()).longValue(),
         ((Float)this.moduleNumber2.get()).longValue()
      );
   }

   private static long calculateValue() {
      return System.nanoTime() / 1000000L;
   }

   @Override
   protected void onEnable() {
      this.bucketStageService.stop(this.values);
      this.on(EventAttackInputService.TICK).run(item -> this.bucketStageService.watchdog(calculateValue(), this.values, this.createPolicy()));
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT)
         .priority(EventIsAfterHandler.Priority.EARLY)
         .run(
            item -> {
               this.values
                  .options(
                     new CompatReleaseTracker.Options(
                        false,
                        false,
                        (Boolean)this.moduleBool.get(),
                        (Boolean)this.moduleBool2.get(),
                        ((Float)this.moduleNumber.get()).floatValue(),
                        (String)this.moduleText.get()
                     )
                  );
               this.bucketStageService.prepare(calculateValue(), this.values, this.createPolicy());
               this.values.beforeMovement();
            }
         );
      this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE)
         .priority(EventIsAfterHandler.Priority.EARLY)
         .run(item -> this.values.reconcileMovement());
      this.on(EventAttackInputService.POST_MOVEMENT_PACKET)
         .run(item -> this.bucketStageService.afterMovement(calculateValue(), this.values, this.createPolicy()));
      this.on(EventAttackInputService.WORLD).run(item -> this.bucketStageService.stop(this.values));
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> this.bucketStageService.stop(this.values));
   }

   @Override
   protected void onDisable() {
      this.bucketStageService.stop(this.values);
   }

   public String status() {
      return this.bucketStageService.status();
   }
}
