package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class SmallTotemModule extends ModuleSettingsService {
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number(
            "Scale",
            0.42F,
            0.1F,
            0.8F,
            0.01F
         )
         .description("Sets the screen scale of the Totem of Undying activation animation; vanilla uses 0.80.")
   );
   private static volatile boolean enabled;
   private static volatile float value = 0.42F;

   public SmallTotemModule() {
      super(
         ModuleBuilderData.builder("Small Totem")
            .description("Reduces the totem activation animation without changing gameplay.")
            .category(ModuleFeatureType.VISUALS)
            .build()
      );
      this.moduleNumber.onChange(item -> value = item);
   }

   @Override
   protected void onEnable() {
      enabled = true;
      value = (Float)this.moduleNumber.get();
   }

   @Override
   protected void onDisable() {
      enabled = false;
   }

   public static boolean isActive() {
      return enabled;
   }

   public static float totemActivationScale(float scale) {
      return enabled ? value : scale;
   }
}

