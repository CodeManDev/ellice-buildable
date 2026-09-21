package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.PearlThrowController;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.pearl.PearlStatusService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class PearlSafeModule extends ModuleSettingsService {
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
            "Fall trigger",
            1.2F,
            0.4F,
            5.0F,
            0.1F
         )
         .description("Minimum falling distance before checking for a void rescue. Early detection preserves the upward throw window.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
            "Search radius", 24.0F, 8.0F, 40.0F, 2.0F
         )
         .description("Searches loaded landing surfaces, starting near the last grounded position. Both throw arcs are tested.")
   );
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Use inventory pearls", true)
         .description("Can temporarily swap a pearl stack from the main inventory into the hotbar using a normal inventory action.")
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      new ModuleSetting.Number("Minimum health", 6.0F, 1.0F, 20.0F, 1.0F)
         .description(
            "Health plus absorption required before throwing. A vanilla pearl can deal 5 damage; lower this only to accept that risk."
         )
   );
   private final ModuleSetting.Number moduleNumber4 = this.setting(
      new ModuleSetting.Number("Safety reserve (ticks)", 2.0F, 0.0F, 8.0F, 1.0F)
         .description("Extra time before reaching the void-damage region, in addition to the observed connection latency.")
   );
   private final PearlStatusService pearlStatusService = new PearlStatusService();
   private final PearlThrowController compatOptionsTracker2 = new PearlThrowController();
   private long timestamp;

   public PearlSafeModule() {
      super(
         ModuleBuilderData.builder("PearlSafe")
            .category(ModuleFeatureType.PLAYER)
            .description("Attempts to rescue void falls with a predicted pearl throw toward a checked safe landing.")
            .build()
      );
   }

   @Override
   protected void onEnable() {
      this.pearlStatusService.reset(this.compatOptionsTracker2);
      this.compatOptionsTracker2.clearTerrain();
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT)
         .priority(EventIsAfterHandler.Priority.FIRST)
         .run(
            item -> {
               this.compatOptionsTracker2
                  .update(
                     ++this.timestamp,
                     new PearlThrowController.Options(
                        ((Float)this.moduleNumber.get()).floatValue(),
                        ((Float)this.moduleNumber2.get()).floatValue(),
                        ((Float)this.moduleNumber3.get()).floatValue(),
                        Math.round((Float)this.moduleNumber4.get()),
                        (Boolean)this.moduleBool.get()
                     )
                  );
               this.pearlStatusService.before(this.timestamp, this.compatOptionsTracker2);
            }
         );
      this.on(EventAttackInputService.MOVEMENT_PACKET_PREPARE).priority(EventIsAfterHandler.Priority.FIRST).run(item -> this.compatOptionsTracker2.reconcile());
      this.on(EventAttackInputService.ATTACK_INPUT).priority(EventIsAfterHandler.Priority.FIRST).run(item -> this.pearlStatusService.after(this.compatOptionsTracker2));
      this.on(EventAttackInputService.OUTGOING_PACKET_ACCEPTED).run(item -> this.compatOptionsTracker2.accepted(item.packet()));
      this.on(EventAttackInputService.TICK).priority(EventIsAfterHandler.Priority.FIRST).run(item -> this.compatOptionsTracker2.watchdog());
      this.on(EventAttackInputService.WORLD).run(item -> {
         this.pearlStatusService.reset(this.compatOptionsTracker2);
         this.compatOptionsTracker2.clearTerrain();
      });
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> {
         this.pearlStatusService.stop(this.compatOptionsTracker2);
         this.compatOptionsTracker2.clearTerrain();
      });
   }

   @Override
   protected void onDisable() {
      this.pearlStatusService.stop(this.compatOptionsTracker2);
      this.compatOptionsTracker2.clearTerrain();
   }

   public String status() {
      return this.pearlStatusService.status();
   }
}
