package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.feature.studio.StudioCanvasRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class StudioShowService extends ScenePctService<StudioShowService> {
   private String text2 = "";
   private float value;
   private float value2;
   private int count;

   public void show(String text, int currentValue, float nextValue, float previousValue) {
      this.text2 = text;
      this.count = currentValue;
      this.value = nextValue;
      this.value2 = previousValue;
   }

   public void clear() {
      this.text2 = "";
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (!this.text2.isEmpty()) {
         StudioCanvasRenderer.fonts(compositorPushPresentationScale);
         compositorPushPresentationScale.nextLayer();
         float y = 166.0F;
         float width = Math.max(
            this.cx + 4.0F,
            Math.min(this.cx + this.cw - y - 4.0F, this.value + 14.0F)
         );
         float height = Math.max(
            this.cy + 4.0F,
            Math.min(this.cy + this.ch - 52.0F, this.value2 + 14.0F)
         );
         compositorPushPresentationScale.studioPrimitive(
            width,
            height,
            y,
            44.0F,
            12.0F,
            -13945781,
            this.count,
            1.0F,
            new CompositorPushPresentationScaleService.StudioPaint(5, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F)
         );
         compositorPushPresentationScale.text(
            width + 13.0F,
            height + 12.0F,
            StudioCanvasRenderer.fit(compositorPushPresentationScale, this.text2, 12.0F, y - 26.0F),
            12.0F,
            this.count,
            MaterialIsLightService.LABEL
         );
      }
   }
}
