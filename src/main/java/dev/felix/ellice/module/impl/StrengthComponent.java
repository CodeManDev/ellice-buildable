package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorRenderer;

public final class StrengthComponent extends ModuleSettingsService {
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number("Strength", 0.55F, 0.0F, 1.0F, 0.01F)
         .description("Controls how much frame history is blended into the current image; 0 disables visible blur.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number("Persistence", 0.75F, 0.0F, 1.0F, 0.01F)
         .description("Controls how long previous frames remain visible; high values produce longer ghost trails.")
   );
   private final ModuleSetting.Mode moduleMode = this.setting(
      new ModuleSetting.Mode("Mode", new String[]{"Smooth", "Smear", "Trail", "Gamma"}, "Smooth")
         .description("Selects the history blend: even Smooth, heavier Smear, long Trail, or highlight-preserving Gamma.")
   );

   public StrengthComponent() {
      super(
         ModuleBuilderData.builder("Motion Blur")
            .description("Blends recent frames into configurable motion blur and trail effects.")
            .category(ModuleFeatureType.VISUALS)
            .build()
      );
      this.moduleNumber.onChange(item -> this.updateState());
      this.moduleNumber2.onChange(item -> this.updateState());
      this.moduleMode.onChange(item -> this.updateState());
   }

   private int calculateValue() {
      return switch ((String)this.moduleMode.get()) {
         case "Smear" -> 1;
         case "Trail" -> 2;
         case "Gamma" -> 3;
         default -> 0;
      };
   }

   private void updateState() {
      CompositorRenderer compositorRenderer = CoreIsInitializedHandler.get().compositor().postFX();
      if (compositorRenderer != null) {
         compositorRenderer.setMotionBlur(this.isEnabled(), (Float)this.moduleNumber.get(), (Float)this.moduleNumber2.get(), this.calculateValue());
      }
   }

   @Override
   protected void onEnable() {
      this.updateState();
   }

   @Override
   protected void onDisable() {
      CompositorRenderer compositorRenderer = CoreIsInitializedHandler.get().compositor().postFX();
      if (compositorRenderer != null) {
         compositorRenderer.setMotionBlur(false, 0.0F, 0.0F, 0);
      }
   }
}
