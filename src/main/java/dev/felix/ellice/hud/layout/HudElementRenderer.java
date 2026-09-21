package dev.felix.ellice.hud.layout;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public interface HudElementRenderer {
   void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutIsContainerService layoutIsContainer, float width, float height, float value, float currentValue, LayoutOperationHandler layoutOperation, float nextValue, float previousValue
   );

   default HudElementRenderer.Size measure(LayoutIsContainerService layoutIsContainer, LayoutOperationHandler layoutOperation, float value, float currentValue) {
      return new HudElementRenderer.Size(
         layoutIsContainer.width >= 0.0F ? layoutIsContainer.width : Math.max(0.0F, value), layoutIsContainer.height >= 0.0F ? layoutIsContainer.height : Math.max(0.0F, currentValue)
      );
   }

   default HudElementRenderer.Size measure(LayoutIsContainerService layoutIsContainer, LayoutOperationHandler layoutOperation, float value, float currentValue, CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return this.measure(layoutIsContainer, layoutOperation, value, currentValue);
   }

   record Size(float w, float h) {
   }
}
