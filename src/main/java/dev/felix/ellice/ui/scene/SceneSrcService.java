package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.svg.SvgRepository;
import dev.felix.ellice.ui.svg.SvgGetTextureService;
import java.nio.file.Path;
import java.util.Objects;

public class SceneSrcService extends ScenePctService<SceneSrcService> {
   private Path path;
   private SvgRepository svgRepository;
   private int count;
   private float value;
   private RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.TextureHandle textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
   private boolean enabled;
   private boolean enabled2;
   private int count2;
   private int count3;

   public SceneSrcService src(Path currentPath) {
      if (Objects.equals(this.path, currentPath)) {
         return this;
      }

      this.path = currentPath;
      this.enabled = false;
      this.enabled2 = false;
      this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
      this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
      this.count2 = this.count3 = 0;
      this.invalidate();
      return this;
   }

   public SceneSrcService document(SvgRepository currentSvgRepository) {
      if (this.svgRepository == currentSvgRepository) {
         return this;
      }

      this.svgRepository = currentSvgRepository;
      this.invalidateLayout();
      return this;
   }

   public SceneSrcService tintColor(int color) {
      this.count = color;
      return this;
   }

   public SceneSrcService rotation(float currentValue) {
      this.value = currentValue;
      return this;
   }

   public Path srcPath() {
      return this.path;
   }

   public SvgRepository document() {
      return this.svgRepository;
   }

   public float rotation() {
      return this.value;
   }

   @Override
   public float getAnimProperty(String text) {
      return "rotation".equals(text) ? this.value : super.getAnimProperty(text);
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      if ("rotation".equals(text)) {
         this.value = currentValue;
      } else {
         super.setAnimProperty(text, currentValue);
      }
   }

   @Override
   public int getColorProperty(String text) {
      return !"color".equals(text) && !"tintColor".equals(text) ? super.getColorProperty(text) : this.count;
   }

   @Override
   public void setColorProperty(String text, int currentColor) {
      if (!"color".equals(text) && !"tintColor".equals(text)) {
         super.setColorProperty(text, currentColor);
      } else {
         this.count = currentColor;
      }
   }

   @Override
   public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return this.svgRepository != null ? this.svgRepository.viewBoxW() : 16.0F;
   }

   @Override
   public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return this.svgRepository != null ? this.svgRepository.viewBoxH() : 16.0F;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.path != null || this.svgRepository != null) {
         float y = this.effectiveOpacity;
         if (!(y < 0.005F)) {
            SvgGetTextureService svgGetTexture = compositorPushPresentationScale.svgRenderer();
            if (svgGetTexture != null && svgGetTexture.isInitialized()) {
               if (this.count >>> 24 != 0 && this.path != null) {
                  if (!this.enabled2) {
                     this.textureHandle2 = svgGetTexture.getSdfTexture(this.path);
                     this.enabled2 = true;
                  }

                  if (this.textureHandle2.valid()) {
                     compositorPushPresentationScale.drawSdfTexture(
                        this.textureHandle2,
                        this.cx,
                        this.cy,
                        this.cw,
                        this.ch,
                        y,
                        this.count,
                        this.value,
                        12.0F
                     );
                     return;
                  }
               }

               int height = Math.max(1, (int)Math.ceil(this.cw));
               int currentValue = Math.max(1, (int)Math.ceil(this.ch));
               if ((!this.enabled || height != this.count2 || currentValue != this.count3) && this.path != null) {
                  this.textureHandle = svgGetTexture.getTexture(this.path, height, currentValue);
                  this.enabled = true;
                  this.count2 = height;
                  this.count3 = currentValue;
               }

               if (this.textureHandle.valid()) {
                  compositorPushPresentationScale.drawTexture(this.textureHandle, this.cx, this.cy, this.cw, this.ch, y, 0.0F, this.count, this.value);
               }
            }
         }
      }
   }
}

