package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;

public final class ImplIsActiveService extends ModuleSettingsService {
   private static boolean enabled;

   public ImplIsActiveService() {
      super(
         ModuleBuilderData.builder("NoHurtCam").category(ModuleFeatureType.VISUALS).description("Disables the camera tilt caused by damage.").build()
      );
   }

   @Override
   protected void onEnable() {
      enabled = true;
   }

   @Override
   protected void onDisable() {
      enabled = false;
   }

   public static boolean isActive() {
      return enabled;
   }
}

