package dev.felix.ellice.feature.nametags;

import dev.felix.ellice.compat.TextureUvRegion;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class NametagsItemService extends ScenePctService<NametagsItemService> {
   private TextureUvRegion compatData2;
   private int count;

   public NametagsItemService item(TextureUvRegion textureUvRegion, int value) {
      this.compatData2 = textureUvRegion;
      this.count = value;
      return this;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.compatData2 != null && this.compatData2.textureId() > 0) {
         compositorPushPresentationScale.drawTextureRegion(
            new RhiBlendStateService.TextureHandle(this.compatData2.textureId()),
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.effectiveOpacity,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0,
            0.0F,
            0.0F,
            this.compatData2.u0(),
            this.compatData2.v0(),
            this.compatData2.u1(),
            this.compatData2.v1(),
            true
         );
      } else {
         float y = Math.min(this.cw, this.ch) * 0.45F;
         compositorPushPresentationScale.roundedRect(
            this.cx + (this.cw - y) / 2.0F,
            this.cy + (this.ch - y) / 2.0F,
            y,
            y,
            y / 2.0F,
            mulAlpha(this.count, this.effectiveOpacity)
         );
      }
   }
}
