package dev.felix.ellice.module.impl;

import com.mojang.blaze3d.platform.Window;
import dev.felix.ellice.compat.CompatStatusTracker;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.world.BoxOverlayRenderer;
import java.util.List;

public final class PulseModule extends ModuleSettingsService {
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
            "Hold (ms)",
            100.0F,
            50.0F,
            150.0F,
            10.0F
         )
         .description("Groups at most three native movement packets. The first packet fixes the deadline.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
            "Interval (ms)",
            150.0F,
            100.0F,
            500.0F,
            25.0F
         )
         .description("Normal transmission between pulses. Attacks, item use and changed input release movement immediately.")
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      new ModuleSetting.Number(
            "Max separation", 0.8F, 0.3F, 1.0F, 0.05F
         )
         .description("Maximum distance in blocks from the last transmitted position before releasing the window.")
   );
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Combat only", true)
         .description("Activates with a KillAura target or held attack input. Pulse takes priority over TickBase.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Sent position", true)
         .description("Outlines your last transmitted position while movement is held. This is not a server acknowledgement.")
   );
   private final ModuleSetting.Color moduleColor = this.setting(new ModuleSetting.Color("Position color", -996161537));
   private final BoxOverlayRenderer renderer = new BoxOverlayRenderer();

   public PulseModule() {
      super(
         ModuleBuilderData.builder("Pulse")
            .category(ModuleFeatureType.COMBAT)
            .description("Releases short groups of real movement packets between interactions, without changing local tick speed.")
            .build()
      );
      this.moduleColor.visibleWhen(this.moduleBool2);
   }

   public PulseModule.Profile profile() {
      return new PulseModule.Profile(
         Math.round((Float)this.moduleNumber.get()),
         Math.round((Float)this.moduleNumber2.get()),
         ((Float)this.moduleNumber3.get()).floatValue(),
         (Boolean)this.moduleBool.get()
      );
   }

   @Override
   protected void onEnable() {
      CompatStatusTracker.enable(this);
      this.on(EventAttackInputService.TICK).priority(EventIsAfterHandler.Priority.FIRST).run(item -> CompatStatusTracker.tick());
      this.on(EventAttackInputService.GAMEPLAY_INPUT).priority(EventIsAfterHandler.Priority.FIRST).run(item -> CompatStatusTracker.tick());
      this.on(EventAttackInputService.PRE_PLAYER_MOVEMENT).priority(EventIsAfterHandler.Priority.FIRST).run(item -> CompatStatusTracker.tick());
      this.on(EventAttackInputService.RENDER).run(item -> CompatStatusTracker.tick());
      this.on(EventAttackInputService.WORLD).run(item -> CompatStatusTracker.worldChanged());
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> CompatStatusTracker.corrected());
      this.on(EventAttackInputService.WORLD_RENDER)
         .run(
            worldRender -> {
               if ((Boolean)this.moduleBool2.get()) {
                  CompatStatusTracker.sentBox()
                     .ifPresent(
                        sentBox -> {
                           Window window = CoreIsInitializedHandler.mc().getWindow();
                           int value = (Integer)this.moduleColor.get();
                           int currentValue = value & 16777215 | 402653184;
                           this.renderer
                              .render(
                                 List.of(sentBox.inflate(0.01)),
                                 worldRender,
                                 window.getWidth(),
                                 window.getHeight(),
                                 new BoxOverlayRenderer.Style(currentValue, value, 0.008F, true, true, false)
                              );
                        }
                     );
               }
            }
         );
   }

   @Override
   protected void onDisable() {
      CompatStatusTracker.disable(this);
      this.renderer.shutdown();
   }

   public String status() {
      return CompatStatusTracker.status().reason();
   }

   public record Profile(int holdMillis, int intervalMillis, double distance, boolean combatOnly) {
   }
}
