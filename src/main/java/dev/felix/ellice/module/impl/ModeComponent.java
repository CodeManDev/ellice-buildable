package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ModeComponent extends ModuleSettingsService {
   private final ModuleSetting.Mode moduleMode = this.setting(
      new ModuleSetting.Mode("Mode", new String[]{"Normal", "Silent"}, "Normal")
         .description("Silent keeps your visible hotbar slot and held item while the server uses the mining tool.")
   );
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Restore slot", true)
         .description("Returns to your previous slot after mining. Manual slot changes always take priority.")
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Protect tools", true).description("Avoids selecting tools at or below the durability reserve.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number("Durability reserve", 5.0F, 1.0F, 100.0F, 1.0F)
         .description("Remaining durability to preserve when choosing a tool.")
   );

   public ModeComponent() {
      super(
         ModuleBuilderData.builder("AutoTool")
            .category(ModuleFeatureType.PLAYER)
            .description("Uses the best hotbar tool for each block, with optional silent switching and durability protection.")
            .build()
      );
      this.moduleBool.visibleWhen(this.moduleMode, "Normal"::equals);
      this.moduleNumber.visibleWhen(this.moduleBool2);
      this.moduleMode.onChange(item -> this.updateState());
      this.moduleBool.onChange(item -> this.updateState());
      this.moduleBool2.onChange(item -> this.updateState());
      this.moduleNumber.onChange(item -> this.updateState());
   }

   private void updateState() {
      if (this.isEnabled()) {
         CompatConfigureService.configure(
            "Silent".equals(this.moduleMode.get()),
            (Boolean)this.moduleBool.get(),
            this.moduleBool2.get() ? ((Float)this.moduleNumber.get()).intValue() : -1
         );
      }
   }

   @Override
   protected void onEnable() {
      this.updateState();
      this.on(EventAttackInputService.TICK).priority(EventIsAfterHandler.Priority.EARLY).run(item -> CompatConfigureService.watchdog());
      this.on(EventAttackInputService.WORLD).run(item -> CompatConfigureService.cancel());
      this.on(EventAttackInputService.PLAYER_POSITION_CORRECTED).run(item -> CompatConfigureService.cancel());
   }

   @Override
   protected void onDisable() {
      CompatConfigureService.disable();
   }
}
