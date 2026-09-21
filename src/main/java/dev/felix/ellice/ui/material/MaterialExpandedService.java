package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class MaterialExpandedService extends SceneSrcService {
   private boolean enabled;
   private boolean enabled2;

   public MaterialExpandedService() {
      this.src(MaterialTextService.iconPath("chevron_right"));
      this.pointerEvents(false);
   }

   public MaterialExpandedService expanded(boolean currentEnabled) {
      if (!this.enabled) {
         this.enabled = true;
         MotionColorsContainer.Floats.ROTATION.set(this, currentEnabled ? 90.0F : 0.0F);
      } else if (this.enabled2 != currentEnabled) {
         MotionAnimateService.animate(this, MotionColorsContainer.Floats.ROTATION, currentEnabled ? 90.0F : 0.0F, MaterialEnterService.RELEASE);
      }

      this.enabled2 = currentEnabled;
      return this;
   }
}

