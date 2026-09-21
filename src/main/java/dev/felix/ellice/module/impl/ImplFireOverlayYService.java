package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ImplFireOverlayYService extends ModuleSettingsService {
   private static final float value = -0.3F;
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number("Height", 0.35F, 0.0F, 1.0F, 0.01F)
         .description("Controls the first-person flame position; lower values push the overlay farther down the screen.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number(
            "Opacity", 0.75F, 0.0F, 0.9F, 0.01F
         )
         .description("Caps the first-person fire overlay alpha; 0 makes the flames invisible.")
   );
   private static volatile boolean enabled;
   private static volatile float value2 = 0.35F;
   private static volatile float value3 = 0.75F;

   public ImplFireOverlayYService() {
      super(
         ModuleBuilderData.builder("Low Fire")
            .description("Lowers and fades the first-person fire overlay to keep your view clear.")
            .category(ModuleFeatureType.VISUALS)
            .build()
      );
      this.moduleNumber.onChange(item -> value2 = item);
      this.moduleNumber2.onChange(item -> value3 = item);
   }

   @Override
   protected void onEnable() {
      enabled = true;
      value2 = (Float)this.moduleNumber.get();
      value3 = (Float)this.moduleNumber2.get();
   }

   @Override
   protected void onDisable() {
      enabled = false;
   }

   public static float fireOverlayY(float value) {
      return !enabled ? value : -0.3F - (1.0F - value2) * 0.85F;
   }

   public static float fireOverlayAlpha(float opacity) {
      return enabled ? Math.min(opacity, value3) : opacity;
   }
}

