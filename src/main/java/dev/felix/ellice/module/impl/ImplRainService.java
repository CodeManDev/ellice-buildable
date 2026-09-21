package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ImplRainService extends ModuleSettingsService {
   public final ModuleSetting.Mode mode = this.setting(new ModuleSetting.Mode("Weather", new String[]{"Clear", "Rain", "Thunder"}, "Clear"));
   public final ModuleSetting.Number strength = this.setting(
      new ModuleSetting.Number("Strength (%)", 100.0F, 0.0F, 100.0F, 1.0F)
   );
   private static ImplRainService implRainService;

   public ImplRainService() {
      super(
         ModuleBuilderData.builder("WeatherChanger")
            .category(ModuleFeatureType.VISUALS)
            .description("Changes local rain and thunder intensity without changing server weather.")
            .build()
      );
      this.strength.visibleWhen(this.mode, item -> !item.equals("Clear"));
   }

   @Override
   protected void onEnable() {
      implRainService = this;
   }

   @Override
   protected void onDisable() {
      if (implRainService == this) {
         implRainService = null;
      }
   }

   public static float rain(float value) {
      return implRainService == null
         ? value
         : (
            ((String)implRainService.mode.get()).equals("Clear")
               ? 0.0F
               : (Float)implRainService.strength.get() / 100.0F
         );
   }

   public static float thunder(float value) {
      return implRainService == null
         ? value
         : (
            ((String)implRainService.mode.get()).equals("Thunder")
               ? (Float)implRainService.strength.get() / 100.0F
               : 0.0F
         );
   }
}
