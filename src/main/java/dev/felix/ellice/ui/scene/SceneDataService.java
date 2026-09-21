package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.compositor.CompositorEmptyService;

public final class SceneDataService extends ScenePctService<SceneDataService> {
   private CompositorEmptyService compositorEmptyService = CompositorEmptyService.empty();
   private int count = -11310662;

   public SceneDataService data(CompositorEmptyService compositorEmpty) {
      this.compositorEmptyService = compositorEmpty;
      return this;
   }

   public SceneDataService color(int currentColor) {
      this.count = currentColor;
      return this;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      compositorPushPresentationScale.timeSeries(this.cx, this.cy, this.cw, this.ch, this.compositorEmptyService, this.count, this.effectiveOpacity);
   }
}
