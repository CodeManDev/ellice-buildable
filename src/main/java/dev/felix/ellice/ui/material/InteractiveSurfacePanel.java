package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class InteractiveSurfacePanel extends SceneCornerRadiusService {
   private int count = MaterialIsLightService.SURFACE_HIGH;

   public InteractiveSurfacePanel() {
      this.backgroundColor(MaterialIsLightService.SURFACE_HIGH);
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      int y = !this.isHovered()
            && !this.children().stream().anyMatch(item -> item.isHovered() || item instanceof ControlLetterSpacingService controlLetterSpacing && controlLetterSpacing.focused())
         ? 0
         : 1;
      int width = MaterialIsLightService.layer(MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.ON_SURFACE, y != 0 ? 0.08F : 0.0F);
      if (width != this.count) {
         this.count = width;
         MotionAnimateService.animate(this, MotionColorsContainer.Colors.BACKGROUND, width, MaterialIsLightService.FAST_EFFECTS);
      }

      super.draw(compositorPushPresentationScale);
   }
}
