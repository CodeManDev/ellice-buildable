package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ImplActiveService extends ModuleSettingsService {
   private static volatile ImplActiveService implActiveService;
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Always with KillAura", false)
         .description(
            "Displays the 1.8 block pose while KillAura has a target and a sword is in your main hand, without holding Use. Stops when the target is lost."
         )
   );
   private final ModuleSetting.Bool moduleBool2 = this.setting(
      new ModuleSetting.Bool("Hide offhand shield", true)
         .description("Hides the first-person offhand shield while the sword block pose is displayed. Shield use still works normally.")
   );
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      new ModuleSetting.Bool("Swing while blocking", false)
         .description("Adds the classic sword swing to the block pose. Off keeps vanilla 1.8's steady blocking pose.")
   );

   public ImplActiveService() {
      super(
         ModuleBuilderData.builder("Block Animation")
            .description(
               "Brings back the vanilla 1.8 first-person sword blocking animation. Hold Use with a sword or enable Always with KillAura."
            )
            .category(ModuleFeatureType.VISUALS)
            .build()
      );
   }

   @Override
   protected void onEnable() {
      implActiveService = this;
   }

   @Override
   protected void onDisable() {
      if (implActiveService == this) {
         implActiveService = null;
      }
   }

   public static ImplActiveService active() {
      return implActiveService;
   }

   public boolean hideShield() {
      return (Boolean)this.moduleBool2.get();
   }

   public boolean blockSwing() {
      return (Boolean)this.moduleBool3.get();
   }

   public boolean shouldBlock(boolean enabled, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled, boolean sourceEnabled, boolean targetEnabled) {
      return this.isEnabled() && enabled && !targetEnabled && ((Boolean)this.moduleBool.get() && nextEnabled || previousEnabled || currentEnabled && !sourceEnabled);
   }
}
