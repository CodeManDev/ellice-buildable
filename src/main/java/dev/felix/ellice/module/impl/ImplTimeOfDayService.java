package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class ImplTimeOfDayService extends ModuleSettingsService {
   public final ModuleSetting.Mode mode = this.setting(
      new ModuleSetting.Mode("Time", new String[]{"Day", "Sunset", "Night", "Midnight", "Custom"}, "Day")
   );
   public final ModuleSetting.Number custom = this.setting(
      new ModuleSetting.Number("Custom time", 6000.0F, 0.0F, 23999.0F, 1.0F)
   );
   private static ImplTimeOfDayService implTimeOfDayService;

   public ImplTimeOfDayService() {
      super(
         ModuleBuilderData.builder("TimeChanger")
            .category(ModuleFeatureType.VISUALS)
            .description("Overrides the displayed time while keeping server time updates.")
            .build()
      );
      this.custom.visibleWhen(this.mode, "Custom"::equals);
   }

   @Override
   protected void onEnable() {
      implTimeOfDayService = this;
   }

   @Override
   protected void onDisable() {
      if (implTimeOfDayService == this) {
         implTimeOfDayService = null;
      }
   }

   public long timeOfDay() {
      return switch ((String)this.mode.get()) {
         case "Sunset" -> 12000L;
         case "Night" -> 14000L;
         case "Midnight" -> 18000L;
         case "Custom" -> ((Float)this.custom.get()).longValue();
         default -> 6000L;
      };
   }

   public static long displayedTime(long longValue) {
      return implTimeOfDayService == null ? longValue : longValue - Math.floorMod(longValue, 24000L) + implTimeOfDayService.timeOfDay();
   }
}
