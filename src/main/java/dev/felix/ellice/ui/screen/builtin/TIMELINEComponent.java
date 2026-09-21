package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.terrain.CinematicExporter;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.nio.ByteBuffer;
import java.util.Locale;

final class TIMELINEComponent extends ScenePctService<TIMELINEComponent> {
   private final CinematicExporter renderer;
   private final RhiBlendStateService.TextureHandle[] textureHandle = new RhiBlendStateService.TextureHandle[20];
   private final byte[][] byte2 = new byte[20][];
   private TIMELINEComponent.Change change;
   private Runnable runnable;
   private float value;
   private float value2;
   private float value3;
   private float value4;
   private float value5 = 1.0F;
   private float value6;
   private TIMELINEComponent.Drag drag2 = TIMELINEComponent.Drag.NONE;

   TIMELINEComponent(CinematicExporter cinematicExporter) {
      this.renderer = cinematicExporter;
      this.interactive(true)
         .stopPropagation(true)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .tooltip("Drag clip edges to trim; drag the white line to seek; scroll to pan when zoomed");
   }

   TIMELINEComponent state(float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      return this.state(value, currentValue, nextValue, previousValue, sourceValue, false);
   }

   TIMELINEComponent state(float currentValue, float nextValue, float previousValue, float sourceValue, float targetValue, boolean enabled) {
      this.value = Math.max(0.041666668F, currentValue);
      this.value2 = Math.clamp(nextValue, 0.0F, this.value);
      this.value3 = Math.clamp(previousValue, this.value2, this.value);
      this.value4 = Math.clamp(sourceValue, 0.0F, this.value);
      float inputValue = Math.clamp(targetValue, 1.0F, 8.0F);
      if (this.value5 != inputValue) {
         float outputValue = this.value4;
         this.value5 = inputValue;
         this.value6 = Math.clamp(
            outputValue - this.calculateValue() * 0.5F, 0.0F, this.value - this.calculateValue()
         );
      }

      if (enabled && this.value4 < this.value6) {
         this.value6 = this.value4;
      }

      if (enabled && this.value4 > this.value6 + this.calculateValue()) {
         this.value6 = this.value4 - this.calculateValue();
      }

      this.value6 = Math.clamp(this.value6, 0.0F, this.value - this.calculateValue());
      this.invalidate();
      return this;
   }

   TIMELINEComponent onChange(TIMELINEComponent.Change currentChange) {
      this.change = currentChange;
      return this;
   }

   TIMELINEComponent onCommit(Runnable currentRunnable) {
      this.runnable = currentRunnable;
      return this;
   }

   private float calculateValue() {
      return this.value / this.value5;
   }

   private float calculateValue2() {
      return this.cx + 82.0F;
   }

   private float calculateValue3() {
      return Math.max(1.0F, this.cw - 98.0F);
   }

   private float calculateValue4(float value) {
      return this.calculateValue2() + (value - this.value6) / this.calculateValue() * this.calculateValue3();
   }

   private float calculateValue5(float currentValue) {
      return Math.clamp(
         this.value6 + (currentValue - this.calculateValue2()) / this.calculateValue3() * this.calculateValue(), 0.0F, this.value
      );
   }

   private float calculateValue6(float value) {
      return Math.round(value * 24.0F) / 24.0F;
   }

   @Override
   protected boolean handlesContinuousPointer() {
      return true;
   }

   @Override
   protected void onPress(float value, float currentValue) {
      if (!(currentValue < this.cy + 28.0F) && !(currentValue > this.cy + this.ch - 24.0F)) {
         float nextValue = this.calculateValue4(this.value2);
         float previousValue = this.calculateValue4(this.value3);
         float sourceValue = this.calculateValue4(this.value4);
         if (Math.abs(value - nextValue) <= 12.0F && Math.abs(value - nextValue) <= Math.abs(value - previousValue)) {
            this.drag2 = TIMELINEComponent.Drag.IN;
         } else if (Math.abs(value - previousValue) <= 12.0F) {
            this.drag2 = TIMELINEComponent.Drag.OUT;
         } else {
            this.drag2 = TIMELINEComponent.Drag.PLAYHEAD;
         }

         if (this.drag2 == TIMELINEComponent.Drag.PLAYHEAD || Math.abs(value - sourceValue) < 8.0F) {
            this.updateWhilePressed(value, currentValue);
         }
      } else {
         this.drag2 = TIMELINEComponent.Drag.PLAYHEAD;
         this.updateWhilePressed(value, currentValue);
      }
   }

   @Override
   protected void updateWhilePressed(float currentValue, float nextValue) {
      float previousValue = this.calculateValue6(this.calculateValue5(currentValue));
      switch (this.drag2) {
         case IN:
            this.value2 = Math.min(previousValue, this.value3 - 0.041666668F);
            break;
         case OUT:
            this.value3 = Math.max(previousValue, this.value2 + 0.041666668F);
            break;
         case PLAYHEAD:
            this.value4 = previousValue;
            break;
         default:
            return;
      }

      this.value2 = Math.clamp(this.value2, 0.0F, this.value);
      this.value3 = Math.clamp(this.value3, 0.0F, this.value);
      if (this.change != null) {
         this.change.accept(this.value2, this.value3, this.value4);
      }

      this.invalidate();
   }

   @Override
   protected void onRelease(boolean enabled) {
      if ((this.drag2 == TIMELINEComponent.Drag.IN || this.drag2 == TIMELINEComponent.Drag.OUT) && this.runnable != null) {
         this.runnable.run();
      }

      this.drag2 = TIMELINEComponent.Drag.NONE;
   }

   @Override
   protected boolean handleScroll(float currentValue) {
      if (this.value5 <= 1.0F) {
         return false;
      }

      this.value6 = Math.clamp(
         this.value6 - currentValue * this.calculateValue() * 0.12F, 0.0F, this.value - this.calculateValue()
      );
      this.invalidate();
      return true;
   }

   @Override
   protected void onDetached() {
      for (int index = 0; index < this.textureHandle.length; index++) {
         if (this.textureHandle[index] != null && this.textureHandle[index].valid()) {
            CoreIsInitializedHandler.get().compositor().destroyTexture(this.textureHandle[index]);
         }

         this.textureHandle[index] = null;
         this.byte2[index] = null;
      }
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = this.calculateValue2();
      float width = this.calculateValue3();
      float height = this.cy + 36.0F;
      compositorPushPresentationScale.roundedRect(this.cx, this.cy, this.cw, this.ch, 20.0F, this.calculateValue7(MaterialIsLightService.SURFACE_HIGH));
      compositorPushPresentationScale.text(
         this.cx + 14.0F,
         this.cy + 10.0F,
         "TIMELINE",
         10.0F,
         this.calculateValue7(MaterialIsLightService.PRIMARY),
         MaterialIsLightService.LABEL
      );
      compositorPushPresentationScale.text(
         this.cx + 14.0F,
         height + 22.0F,
         "V1",
         12.0F,
         this.calculateValue7(MaterialIsLightService.ON_SURFACE),
         MaterialIsLightService.LABEL
      );
      compositorPushPresentationScale.text(
         this.cx + 14.0F,
         height + 40.0F,
         "CAMERA",
         9.0F,
         this.calculateValue7(MaterialIsLightService.ON_SURFACE_VARIANT),
         MaterialIsLightService.BODY
      );
      compositorPushPresentationScale.text(
         this.cx + 14.0F,
         height + 124.0F,
         "A1",
         12.0F,
         this.calculateValue7(MaterialIsLightService.ON_SURFACE_VARIANT),
         MaterialIsLightService.LABEL
      );
      compositorPushPresentationScale.text(
         this.cx + 14.0F,
         height + 142.0F,
         "NO AUDIO",
         9.0F,
         this.calculateValue7(MaterialIsLightService.OUTLINE),
         MaterialIsLightService.BODY
      );
      compositorPushPresentationScale.roundedRect(
         y,
         height,
         width,
         102.0F,
         12.0F,
         this.calculateValue7(MaterialIsLightService.SURFACE_LOWEST)
      );
      compositorPushPresentationScale.roundedRect(
         y,
         height + 114.0F,
         width,
         43.0F,
         8.0F,
         this.calculateValue7(MaterialIsLightService.SURFACE_CONTAINER)
      );
      compositorPushPresentationScale.text(
         y + 10.0F,
         height + 128.0F,
         "Audio is not captured by 3D Replay",
         10.0F,
         this.calculateValue7(MaterialIsLightService.OUTLINE),
         MaterialIsLightService.BODY
      );
      float currentValue = this.calculateValue();
      float nextValue = currentValue <= 3.0F
         ? 0.5F
         : (
            currentValue <= 8.0F
               ? 1.0F
               : (
                  currentValue <= 18.0F
                     ? 2.0F
                     : (currentValue <= 35.0F ? 5.0F : 10.0F)
               )
         );

      for (float previousValue = (float)Math.ceil(this.value6 / nextValue) * nextValue;
         previousValue <= this.value6 + currentValue + 0.001F;
         previousValue += nextValue
      ) {
         float sourceValue = this.calculateValue4(previousValue);
         if (!(sourceValue < y) && !(sourceValue > y + width)) {
            compositorPushPresentationScale.roundedRect(
               sourceValue,
               this.cy + 24.0F,
               1.0F,
               8.0F,
               0.5F,
               this.calculateValue7(MaterialIsLightService.OUTLINE)
            );
            compositorPushPresentationScale.text(
               Math.min(y + width - 32.0F, sourceValue + 4.0F),
               this.cy + 7.0F,
               createText(previousValue),
               10.0F,
               this.calculateValue7(MaterialIsLightService.ON_SURFACE_VARIANT),
               MaterialIsLightService.BODY
            );
         }
      }

      float targetValue = this.value / 20.0F;

      for (int index = 0; index < 20; index++) {
         float inputValue = index * targetValue;
         float outputValue = (index + 1) * targetValue;
         float resultValue = Math.max(inputValue, this.value6);
         float candidateValue = Math.min(outputValue, this.value6 + currentValue);
         if (!(candidateValue <= resultValue)) {
            byte[] bytes = this.renderer.thumbnailPixels(index);
            if (bytes != null && this.byte2[index] != bytes) {
               if (this.textureHandle[index] != null && this.textureHandle[index].valid()) {
                  compositorPushPresentationScale.destroyTexture(this.textureHandle[index]);
               }

               ByteBuffer currentLength = ByteBuffer.allocateDirect(bytes.length);
               currentLength.put(bytes).flip();
               this.textureHandle[index] = compositorPushPresentationScale.uploadRgbaTexture(RhiBlendStateService.TextureHandle.NONE, 160, 90, currentLength);
               this.byte2[index] = bytes;
            }

            float selectedValue = this.calculateValue4(resultValue);
            float defaultValue = this.calculateValue4(candidateValue);
            if (this.textureHandle[index] != null && this.textureHandle[index].valid()) {
               compositorPushPresentationScale.drawTextureRegion(
                  this.textureHandle[index],
                  selectedValue,
                  height + 4.0F,
                  defaultValue - selectedValue,
                  94.0F,
                  this.effectiveOpacity,
                  0.0F,
                  0.0F,
                  0.0F,
                  0.0F,
                  0,
                  0.0F,
                  0.0F,
                  (resultValue - inputValue) / targetValue,
                  0.0F,
                  (candidateValue - inputValue) / targetValue,
                  1.0F
               );
            } else {
               compositorPushPresentationScale.roundedRect(
                  selectedValue,
                  height + 4.0F,
                  defaultValue - selectedValue,
                  94.0F,
                  3.0F,
                  this.calculateValue7(MaterialIsLightService.SURFACE_HIGHEST)
               );
            }
         }
      }

      compositorPushPresentationScale.nextLayer();
      float initialValue = Math.clamp(this.calculateValue4(this.value2), y, y + width);
      float resolvedValue = Math.clamp(this.calculateValue4(this.value3), y, y + width);
      compositorPushPresentationScale.roundedRect(y, height, Math.max(0.0F, initialValue - y), 102.0F, 0.0F, this.calculateValue7(-1340861928));
      compositorPushPresentationScale.roundedRect(
         resolvedValue, height, Math.max(0.0F, y + width - resolvedValue), 102.0F, 0.0F, this.calculateValue7(-1340861928)
      );
      if (resolvedValue > initialValue) {
         compositorPushPresentationScale.roundedRect(initialValue, height, resolvedValue - initialValue, 3.0F, 1.0F, this.calculateValue7(MaterialIsLightService.PRIMARY));
         compositorPushPresentationScale.roundedRect(
            initialValue,
            height + 99.0F,
            resolvedValue - initialValue,
            3.0F,
            1.0F,
            this.calculateValue7(MaterialIsLightService.PRIMARY)
         );
      }

      if (this.value2 >= this.value6 && this.value2 <= this.value6 + currentValue) {
         this.updateState(compositorPushPresentationScale, initialValue, height);
      }

      if (this.value3 >= this.value6 && this.value3 <= this.value6 + currentValue) {
         this.updateState(compositorPushPresentationScale, resolvedValue, height);
      }

      float computedValue = this.calculateValue4(this.value4);
      if (computedValue >= y && computedValue <= y + width) {
         compositorPushPresentationScale.roundedRect(
            computedValue - 1.0F,
            this.cy + 22.0F,
            2.0F,
            this.ch - 30.0F,
            1.0F,
            this.calculateValue7(MaterialIsLightService.ON_SURFACE)
         );
         compositorPushPresentationScale.roundedRect(
            computedValue - 6.0F,
            this.cy + 20.0F,
            12.0F,
            12.0F,
            4.0F,
            this.calculateValue7(MaterialIsLightService.ON_SURFACE)
         );
      }
   }

   private void updateState(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue) {
      compositorPushPresentationScale.roundedRect(
         value - 5.0F,
         currentValue,
         10.0F,
         102.0F,
         4.0F,
         this.calculateValue7(MaterialIsLightService.PRIMARY)
      );
      compositorPushPresentationScale.roundedRect(
         value - 1.0F,
         currentValue + 40.0F,
         2.0F,
         22.0F,
         1.0F,
         this.calculateValue7(MaterialIsLightService.ON_PRIMARY)
      );
   }

   private int calculateValue7(int value) {
      return mulAlpha(value, this.effectiveOpacity);
   }

   private static String createText(float value) {
      return String.format(Locale.ROOT, "%d:%02d", (int)value / 60, (int)value % 60);
   }

   interface Change {
      void accept(float value, float currentValue, float nextValue);
   }

   private enum Drag {
      NONE,
      IN,
      OUT,
      PLAYHEAD;


      private static TIMELINEComponent.Drag[] $values() {
         return new TIMELINEComponent.Drag[]{NONE, IN, OUT, PLAYHEAD};
      }
   }
}
