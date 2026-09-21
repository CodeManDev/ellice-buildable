package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class MaterialSelectedSelector extends SceneSrcService {
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite("rotation", item -> item instanceof SceneSrcService);
  private boolean enabled;
  private boolean enabled2;

  public MaterialSelectedSelector selected(boolean currentEnabled) {
    if (this.enabled && this.enabled2 != currentEnabled) {
      this.scale(currentEnabled ? 1.16F : 0.94F);
      this.rotation(currentEnabled ? -12.0F : 6.0F);
      MotionAnimateService.animate(
          this, MotionColorsContainer.Floats.SCALE, 1.0F, MaterialEnterService.RELEASE);
      MotionAnimateService.animate(this, motionFiniteService, 0.0F, MaterialEnterService.RELEASE);
    }

    this.enabled = true;
    this.enabled2 = currentEnabled;
    return this;
  }
}
