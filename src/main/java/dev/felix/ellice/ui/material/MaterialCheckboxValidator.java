package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;

public final class MaterialCheckboxValidator extends ScenePctService<MaterialCheckboxValidator> {
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("materialSelection", item -> item instanceof MaterialCheckboxValidator);
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private float value;

   public MaterialCheckboxValidator() {
      this.pointerEvents(false);
      this.size(24.0F, 24.0F);
      this.flexShrink(0.0F);
   }

   public MaterialCheckboxValidator checkbox(boolean currentEnabled) {
      this.enabled = currentEnabled;
      return this;
   }

   public MaterialCheckboxValidator selected(boolean enabled) {
      if (!this.enabled3) {
         this.enabled3 = true;
         this.value = enabled ? 1.0F : 0.0F;
      } else if (this.enabled2 != enabled) {
         MotionAnimateService.animate(this, motionFiniteService, enabled ? 1.0F : 0.0F, MaterialIsLightService.FAST_SPATIAL);
      }

      this.enabled2 = enabled;
      return this;
   }

   @Override
   public float getAnimProperty(String text) {
      return "materialSelection".equals(text) ? this.value : super.getAnimProperty(text);
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      if ("materialSelection".equals(text)) {
         this.value = currentValue;
      } else {
         super.setAnimProperty(text, currentValue);
      }
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = Math.max(0.0F, Math.min(1.0F, this.value));
      float width = this.presentationScale;
      float height = 20.0F * width;
      float currentValue = this.cx + (this.cw - height) / 2.0F;
      float nextValue = this.cy + (this.ch - height) / 2.0F;
      float previousValue = (this.enabled ? 2 : 10) * width;
      int sourceValue = MaterialIsLightService.layer(MaterialIsLightService.ON_SURFACE_VARIANT, MaterialIsLightService.PRIMARY, y);
      compositorPushPresentationScale.roundedRect(
         currentValue + width,
         nextValue + width,
         height - 2.0F * width,
         height - 2.0F * width,
         previousValue - width,
         previousValue - width,
         previousValue - width,
         previousValue - width,
         this.enabled ? mulAlpha(MaterialIsLightService.PRIMARY, y * this.effectiveOpacity) : 0,
         0.0F,
         0.0F,
         0,
         2.0F * width,
         mulAlpha(sourceValue, this.effectiveOpacity),
         1.0F,
         0.0F
      );
      if (this.enabled && y > 0.001F && compositorPushPresentationScale.svgRenderer() != null) {
         RhiBlendStateService.TextureHandle textureHandle = compositorPushPresentationScale.svgRenderer().getSdfTexture(MaterialTextService.iconPath("checkbox_check"));
         compositorPushPresentationScale.pushClip(currentValue, nextValue, height * y, height);
         compositorPushPresentationScale.drawSdfTexture(
            textureHandle, currentValue, nextValue, height, height, this.effectiveOpacity, MaterialIsLightService.ON_PRIMARY, 0.0F, 12.0F
         );
         compositorPushPresentationScale.popClip();
      } else if (!this.enabled && y > 0.001F) {
         float targetValue = 10.0F
            * width
            * Math.max(0.0F, Math.min(1.06F, this.value));
         compositorPushPresentationScale.roundedRect(
            this.cx + (this.cw - targetValue) / 2.0F,
            this.cy + (this.ch - targetValue) / 2.0F,
            targetValue,
            targetValue,
            targetValue / 2.0F,
            mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity)
         );
      }
   }
}

