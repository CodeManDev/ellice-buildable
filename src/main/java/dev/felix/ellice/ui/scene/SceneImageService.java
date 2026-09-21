package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextData;
import java.awt.image.BufferedImage;
import java.util.Locale;

public final class SceneImageService extends ScenePctService<SceneImageService> {
   private BufferedImage bufferedImage;
   private BufferedImage bufferedImage2;
   private RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
   private CompositorPushPresentationScaleService compositorPushPresentationScaleService;
   private String text2 = "S";
   private int count = -9338450;
   private boolean enabled;

   public SceneImageService material(boolean currentEnabled) {
      this.enabled = currentEnabled;
      return this;
   }

   public SceneImageService image(BufferedImage currentBufferedImage) {
      this.bufferedImage = currentBufferedImage;
      return this;
   }

   public SceneImageService identity(String id, String currentId) {
      String nextId = currentId != null && !currentId.isBlank() ? currentId : id;
      this.text2 = nextId.substring(0, nextId.offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT);
      int[] ints = new int[]{-10126680, -9270145, -6586983, -5339035};
      this.count = ints[Math.floorMod(id.hashCode(), ints.length)];
      return this;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      this.compositorPushPresentationScaleService = compositorPushPresentationScale;
      if (this.bufferedImage != null && (this.bufferedImage2 != this.bufferedImage || !this.textureHandle.valid())) {
         RhiBlendStateService.TextureHandle currentTextureHandle = compositorPushPresentationScale.uploadImageTexture(RhiBlendStateService.TextureHandle.NONE, this.bufferedImage);
         if (currentTextureHandle.valid()) {
            if (this.textureHandle.valid()) {
               compositorPushPresentationScale.destroyTexture(this.textureHandle);
            }

            this.textureHandle = currentTextureHandle;
            this.bufferedImage2 = this.bufferedImage;
         }
      }

      float width = Math.min(this.cw, this.ch) * 0.24F;
      if (this.textureHandle.valid()) {
         compositorPushPresentationScale.drawTexture(this.textureHandle, this.cx, this.cy, this.cw, this.ch, this.effectiveOpacity, width);
      } else {
         int currentCount = this.enabled ? MaterialIsLightService.PRIMARY_CONTAINER : this.count;
         int value = Math.round((currentCount >>> 24 & 0xFF) * this.effectiveOpacity);
         compositorPushPresentationScale.roundedRect(this.cx, this.cy, this.cw, this.ch, width, currentCount & 16777215 | value << 24);
         float currentValue = Math.min(this.cw, this.ch) * 0.38F;
         if (this.enabled) {
            TextData textData = MaterialIsLightService.LABEL;
            float nextValue = compositorPushPresentationScale.textWidth(this.text2, currentValue, TextMode.REGULAR, textData.fontFamily());
            compositorPushPresentationScale.text(
               this.cx + (this.cw - nextValue) / 2.0F,
               this.cy + (this.ch - currentValue) / 2.0F,
               this.text2,
               currentValue,
               mulAlpha(MaterialIsLightService.ON_PRIMARY_CONTAINER, this.effectiveOpacity),
               textData
            );
         } else {
            compositorPushPresentationScale.text(
               this.cx + (this.cw - compositorPushPresentationScale.textWidth(this.text2, currentValue)) / 2.0F,
               this.cy + (this.ch - currentValue) / 2.0F,
               this.text2,
               currentValue,
               16777215 | value << 24
            );
         }
      }
   }

   @Override
   protected void onDetached() {
      if (this.compositorPushPresentationScaleService != null && this.textureHandle.valid()) {
         this.compositorPushPresentationScaleService.destroyTexture(this.textureHandle);
      }

      this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
      this.bufferedImage2 = null;
      this.compositorPushPresentationScaleService = null;
   }
}
