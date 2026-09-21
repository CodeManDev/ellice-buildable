package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class MaterialColorService extends ScenePctService<MaterialColorService> {
   private int count;
   private float value = 12.0F;
   private int count2;

   public MaterialColorService color(int currentColor) {
      this.count = currentColor;
      this.invalidate();
      return this;
   }

   public MaterialColorService radius(float currentValue) {
      this.value = currentValue;
      return this;
   }

   public MaterialColorService paired(int value) {
      this.count2 = value;
      return this;
   }

   public int color() {
      return this.count;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = Math.min(Math.min(this.cw, this.ch) / 2.0F, this.value * this.presentationScale);
      float width = this.count2 > 0 ? Math.min(y, 8.0F * this.presentationScale) : y;
      float height = this.count2 < 0 ? Math.min(y, 8.0F * this.presentationScale) : y;
      compositorPushPresentationScale.pushClip(this.cx, this.cy, this.cw, this.ch, width, height, height, width);
      if (this.count >>> 24 < 255) {
         float currentValue = 8.0F * this.presentationScale;
         compositorPushPresentationScale.roundedRect(this.cx, this.cy, this.cw, this.ch, 0.0F, mulAlpha(-11975345, this.effectiveOpacity));

         for (int index = 0; index * currentValue < this.ch; index++) {
            for (int nextValue = index % 2; nextValue * currentValue < this.cw; nextValue += 2) {
               compositorPushPresentationScale.roundedRect(
                  this.cx + nextValue * currentValue,
                  this.cy + index * currentValue,
                  Math.min(currentValue, this.cw - nextValue * currentValue),
                  Math.min(currentValue, this.ch - index * currentValue),
                  0.0F,
                  mulAlpha(-8817538, this.effectiveOpacity)
               );
            }
         }
      }

      compositorPushPresentationScale.roundedRect(this.cx, this.cy, this.cw, this.ch, 0.0F, mulAlpha(this.count, this.effectiveOpacity));
      compositorPushPresentationScale.popClip();
   }
}
