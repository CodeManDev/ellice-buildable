package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;

public final class SceneSelectionSelector extends LayoutContainerNode {
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("menuSelectionLeft", item -> item instanceof SceneSelectionSelector);
   private static final MotionFiniteService motionFiniteService2 = MotionFiniteService.finite("menuSelectionRight", item -> item instanceof SceneSelectionSelector);
   private float value;
   private float value2;
   private float value3;
   private float value4;
   private float value5;
   private int count;
   private boolean enabled;

   public SceneSelectionSelector() {
      this.onLayout(() -> this.value5 = this.computedX());
   }

   public SceneSelectionSelector selection(int value) {
      this.count = value;
      return this;
   }

   @Override
   public float getAnimProperty(String text) {
      return switch (text) {
         case "menuSelectionLeft" -> this.value;
         case "menuSelectionRight" -> this.value2;
         default -> super.getAnimProperty(text);
      };
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      switch (text) {
         case "menuSelectionLeft":
            this.value = currentValue;
            break;
         case "menuSelectionRight":
            this.value2 = currentValue;
            break;
         default:
            super.setAnimProperty(text, currentValue);
      }
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.count >= 0 && this.count < this.children().size()) {
         ScenePctService scenePct = this.children().get(this.count);
         float width = scenePct.computedX() - this.value5;
         float height = width + scenePct.computedW();
         if (!this.enabled) {
            this.enabled = true;
            this.value = this.value3 = width;
            this.value2 = this.value4 = height;
         } else if (this.value3 != width || this.value4 != height) {
            int currentValue = width + height >= this.value3 + this.value4 ? 1 : 0;
            this.value3 = width;
            this.value4 = height;
            MotionAnimateService.animate(
               this,
               motionFiniteService,
               width,
               new SceneEaseHandler.Spring(
                  currentValue != 0 ? 27.0F : 30.0F,
                  currentValue != 0 ? 210.0F : 380.0F
               )
            );
            MotionAnimateService.animate(
               this,
               motionFiniteService2,
               height,
               new SceneEaseHandler.Spring(
                  currentValue != 0 ? 30.0F : 27.0F,
                  currentValue != 0 ? 380.0F : 210.0F
               )
            );
         }

         float nextValue = this.presentationScale;
         compositorPushPresentationScale.roundedRect(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.ch * 0.5F,
            this.ch * 0.5F,
            this.ch * 0.5F,
            this.ch * 0.5F,
            mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, 0.88F),
            18.0F,
            0.0F,
            0,
            0.0F,
            0,
            this.effectiveOpacity
         );
         float previousValue = Math.max(
            24.0F,
            Math.min(scenePct.computedW() + 48.0F, this.value2 - this.value)
         );
         float sourceValue = Math.max(
            previousValue * 0.5F,
            Math.min(
               this.cw / nextValue - previousValue * 0.5F,
               (this.value + this.value2) * 0.5F
            )
         );
         compositorPushPresentationScale.roundedRect(
            this.cx + (sourceValue - previousValue * 0.5F) * nextValue,
            this.cy + 4.0F * nextValue,
            previousValue * nextValue,
            this.ch - 8.0F * nextValue,
            (this.ch - 8.0F * nextValue) * 0.5F,
            mulAlpha(MaterialIsLightService.SECONDARY_CONTAINER, this.effectiveOpacity)
         );
      }
   }
}

