package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.bucket.BucketStageService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ImplStatusService extends ModuleSettingsService {
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Lava only", false).description("When off, also extinguishes fire after leaving lava or taking fire damage.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Use powder snow", true)
         .description("Uses a powder-snow bucket if water cannot be placed, including in the Nether. Fire can consume the snow.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      new ModuleSetting.Bool("Recover bucket", true)
         .description("Picks up only the placed source after you stop burning and nearby lava no longer needs the water.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
         "Retry interval (ms)",
         500.0F,
         250.0F,
         3000.0F,
         50.0F
      )
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
         "Server wait (ms)",
         2000.0F,
         500.0F,
         6000.0F,
         100.0F
      )
   );
   private final BucketStageService bucketStageService = new BucketStageService();
   private final CompatReleaseTracker values = new CompatReleaseTracker(CompatReleaseTracker.Purpose.EXTINGUISH);

   public ImplStatusService() {
      super(
         ModuleBuilderData.builder("AutoExtinguish")
            .category(ModuleFeatureType.PLAYER)
            .description("Automatically puts out fire with a hotbar/offhand bucket, then restores your slot and view.")
            .build()
      );
   }

   private BucketStageService.Policy createPolicy() {
      return new BucketStageService.Policy(
         (Boolean)this.moduleBool3.get(), ((Float)this.moduleNumber.get()).longValue(), ((Float)this.moduleNumber2.get()).longValue(), 250L
      );
   }

   private static long calculateValue() {
      return System.nanoTime() / 1000000L;
   }

   @Override
   protected void onEnable() {
      this.bucketStageService.stop(this.values);
      this.on(EventAttackInputService.TICK)
         .priority(EventIsAfterHandler.Priority.EARLY)
         .run(item -> this.bucketStageService.watchdog(calculateValue(), this.values, this.createPolicy()));
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT)
         .priority(EventIsAfterHandler.Priority.FIRST)
         .run(
            item -> {
               this.values
                  .options(
                     new CompatReleaseTracker.Options(
                        (Boolean)this.moduleBool.get(),
                        (Boolean)this.moduleBool2.get(),
                        false,
                        false,
                        4.5,
                        ""
                     )
                  );
               this.bucketStageService.prepare(calculateValue(), this.values, this.createPolicy());
            }
         );
      this.on(EventAttackInputService.POST_MOVEMENT_PACKET)
         .priority(EventIsAfterHandler.Priority.EARLY)
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
