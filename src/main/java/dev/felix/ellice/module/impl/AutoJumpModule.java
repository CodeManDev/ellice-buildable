package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;

public final class AutoJumpModule extends ModuleSettingsService {
   private static boolean enabled;

   public AutoJumpModule() {
      super(
         ModuleBuilderData.builder("AutoJump")
            .category(ModuleFeatureType.MOVEMENT)
            .description("Automatically jumps on the ground while moving.")
            .build()
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

