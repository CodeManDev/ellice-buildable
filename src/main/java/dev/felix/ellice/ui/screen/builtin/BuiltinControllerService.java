package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.feature.cape.CapeRevisionService;
import dev.felix.ellice.feature.cape.CapeRenderer;
import dev.felix.ellice.feature.cape.CapePreviewRenderer;
import dev.felix.ellice.feature.cape.CapeSizeService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.awt.image.BufferedImage;

public final class BuiltinControllerService extends ScenePctService<BuiltinControllerService> implements AutoCloseable {
   private final CapeRenderer renderer = new CapeRenderer();
   private CapeRevisionService capeRevisionService;
   private CapeSizeService capeSizeService;
   private CapePreviewRenderer.Style renderer2;
   private BufferedImage bufferedImage;
   private int count = -1;
   private int count2 = -1;
   private double value = -24.0;
   private double value2;
   private double value3 = 1.0;
   private float value4;
   private float value5;
   private boolean enabled;
   private long timestamp = System.nanoTime();

   public BuiltinControllerService() {
      this.onDrag(this::updateState);
      this.onScrollEvent(
         item -> this.value3 = Math.clamp(
            this.value3 + item.floatValue() * 0.06,
            0.7,
            1.45
         )
      );
      this.cursorStyle(ScenePctService.CursorStyle.POINTER);
   }

   public BuiltinControllerService controller(CapeRevisionService capeRevision) {
      this.capeRevisionService = capeRevision;
      return this;
   }

   public BuiltinControllerService view(int currentValue, boolean currentEnabled) {
      if (this.count2 != currentValue) {
         this.count2 = currentValue;
         this.value = currentValue % 2 == 0 ? -24.0 : 156.0;
         this.value2 = 0.0;
         this.value3 = 1.0;
      }

      this.enabled = currentEnabled;
      return this;
   }

   @Override
   protected void onPress(float value, float currentValue) {
      this.value4 = value;
      this.value5 = currentValue;
   }

   private void updateState(float currentValue, float nextValue) {
      this.value = this.value + (currentValue - this.value4) * 0.65;
      this.value2 = Math.clamp(
         this.value2 + (nextValue - this.value5) * 0.35,
         -25.0,
         25.0
      );
      this.value4 = currentValue;
      this.value5 = nextValue;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.capeRevisionService != null
         && this.capeRevisionService.selected() != null
         && !(this.cw < 10.0F)
         && !(this.ch < 10.0F)) {
         long longValue = System.nanoTime();
         double width = Math.min(
            0.05, (longValue - this.timestamp) / 1.0E9
         );
         this.timestamp = longValue;
         if (this.enabled) {
            this.value = this.value + width * 16.0;
         }

         CapeSizeService capeSize = this.capeRevisionService.selected().animation();
         CapePreviewRenderer.Style currentStyle = this.capeRevisionService.style();
         int currentValue = capeSize.frameAt(this.capeRevisionService.playback().position(longValue));
         if (capeSize != this.capeSizeService || !currentStyle.equals(this.renderer2) || this.count != currentValue) {
            this.capeSizeService = capeSize;
            this.renderer2 = currentStyle;
            this.count = currentValue;
            this.bufferedImage = CapePreviewRenderer.render(capeSize.frame(currentValue), currentStyle);
         }

         RhiBlendStateService.TextureHandle textureHandle = this.renderer
            .render(
               Math.clamp((int)(this.cw * 1.5), 64, 1024),
               Math.clamp((int)(this.ch * 1.5), 64, 1024),
               this.capeRevisionService.skin(),
               this.capeRevisionService.slim(),
               this.bufferedImage,
               this.value,
               this.value2,
               this.value3,
               longValue / 1.0E9
            );
         compositorPushPresentationScale.drawTextureRegion(
            textureHandle, this.cx, this.cy, this.cw, this.ch, this.effectiveOpacity, 0.0F, 0.0F, 0.0F, 0.0F, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F
         );
      }
   }

   @Override
   public void close() {
      this.renderer.close();
      this.bufferedImage = null;
   }
}
