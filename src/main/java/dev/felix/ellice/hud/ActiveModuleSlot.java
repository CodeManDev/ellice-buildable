package dev.felix.ellice.hud;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;

final class ActiveModuleSlot extends LayoutContainerNode {
   private float value = 1.0F;
   private float value2 = 1.0F;

   void contentSize(float currentValue, float nextValue) {
      this.value = currentValue;
      this.value2 = nextValue;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = this.ch / Math.max(0.001F, this.value2 * this.presentationScale);
      float width = this.cw / Math.max(0.001F, this.value * this.presentationScale);
      this.effectiveOpacity = this.effectiveOpacity
         * (
            smooth((y - 0.55F) / 0.4F)
               * smooth((width - 0.72F) / 0.26F)
         );
   }

   static float smooth(float value) {
      float currentValue = Math.max(0.0F, Math.min(1.0F, value));
      return currentValue
         * currentValue
         * currentValue
         * (currentValue * (currentValue * 6.0F - 15.0F) + 10.0F);
   }
}
