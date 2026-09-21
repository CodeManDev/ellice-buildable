package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import java.util.function.Consumer;

public class ColorGetAnimPropertyService extends ScenePctService<ColorGetAnimPropertyService> {
   private float value;
   private float value2 = 3.0F;
   private Consumer<Float> consumer;
   private Consumer<Float> consumer2;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private float value3;
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("materialHuePress", item -> item instanceof ColorGetAnimPropertyService);

   @Override
   protected boolean handlesContinuousPointer() {
      return true;
   }

   public ColorGetAnimPropertyService material(boolean enabled) {
      this.enabled2 = enabled;
      return this;
   }

   public void materialFocus(boolean enabled) {
      this.enabled3 = enabled;
      this.invalidate();
   }

   @Override
   public float getAnimProperty(String text) {
      return "materialHuePress".equals(text) ? this.value3 : super.getAnimProperty(text);
   }

   @Override
   public void setAnimProperty(String text, float value) {
      if ("materialHuePress".equals(text)) {
         this.value3 = value;
      } else {
         super.setAnimProperty(text, value);
      }
   }

   public ColorGetAnimPropertyService() {
      this.interactive = true;
   }

   public ColorGetAnimPropertyService hue(float currentValue) {
      float nextValue = calculateValue(currentValue);
      if (checkCondition(this.value, nextValue)) {
         return this;
      }

      this.value = nextValue;
      this.invalidate();
      return this;
   }

   public ColorGetAnimPropertyService cornerRadius(float value) {
      float currentValue = Float.isFinite(value) ? Math.max(0.0F, value) : this.value2;
      if (checkCondition(this.value2, currentValue)) {
         return this;
      }

      this.value2 = currentValue;
      this.invalidate();
      return this;
   }

   public ColorGetAnimPropertyService onChange(Consumer<Float> currentConsumer) {
      this.consumer = currentConsumer;
      return this;
   }

   public ColorGetAnimPropertyService onCommit(Consumer<Float> consumer) {
      this.consumer2 = consumer;
      return this;
   }

   public float hue() {
      return this.value;
   }

   @Override
   protected void updateWhilePressed(float currentValue, float nextValue) {
      if (!(this.cw <= 0.0F)) {
         float previousValue = calculateValue((currentValue - this.cx - (this.enabled2 ? 2 : 0)) / Math.max(1.0F, this.cw - (this.enabled2 ? 4 : 0)));
         if (!checkCondition(this.value, previousValue)) {
            this.value = previousValue;
            this.enabled = true;
            this.invalidate();
            if (this.consumer != null) {
               this.consumer.accept(this.value);
            }
         }
      }
   }

   @Override
   protected void onRelease(boolean currentEnabled) {
      if (this.enabled) {
         this.enabled = false;
         if (this.consumer2 != null) {
            this.consumer2.accept(this.value);
         }
      }
   }

   @Override
   protected void onDetached() {
      this.enabled = false;
      this.consumer = null;
      this.consumer2 = null;
   }

   @Override
   public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return 80.0F;
   }

   @Override
   public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return this.enabled2 ? 48.0F : 10.0F;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (!(this.effectiveOpacity < 0.003F)) {
         if (this.enabled2) {
            if (this.pressed != this.enabled4) {
               this.enabled4 = this.pressed;
               MotionAnimateService.animate(this, motionFiniteService, this.pressed ? 1.0F : 0.0F, this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
            }

            float y = 4.0F
               - 2.0F * Math.max(-0.2F, Math.min(1.0F, this.value3));
            float width = this.cx + 2.0F + this.value * (this.cw - 4.0F);
            float height = this.cy + (this.ch - 16.0F) / 2.0F;
            float currentValue = 6.0F + y / 2.0F;
            float nextValue = width - currentValue - this.cx;
            float previousValue = this.cx + this.cw - width - currentValue;
            if (nextValue > 0.0F) {
               this.updateState(compositorPushPresentationScale, this.cx, height, nextValue, 8.0F, 2.0F);
            }

            if (previousValue > 0.0F) {
               this.updateState(compositorPushPresentationScale, width + currentValue, height, previousValue, 2.0F, 8.0F);
            }

            compositorPushPresentationScale.overlayRect(
               width - y / 2.0F,
               this.cy + (this.ch - 44.0F) / 2.0F,
               y,
               44.0F,
               2.0F,
               2.0F,
               2.0F,
               2.0F,
               mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
               0.0F,
               0.0F,
               0,
               0.0F,
               0
            );
            if (this.enabled3) {
               compositorPushPresentationScale.overlayRect(
                  this.cx - 3.0F,
                  this.cy,
                  this.cw + 6.0F,
                  this.ch,
                  12.0F,
                  12.0F,
                  12.0F,
                  12.0F,
                  0,
                  0.0F,
                  0.0F,
                  0,
                  2.0F,
                  mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity)
               );
            }
         } else {
            compositorPushPresentationScale.hueBar(this.cx, this.cy, this.cw, this.ch, this.value2, this.effectiveOpacity);
            float sourceValue = this.ch * 0.5F - 1.0F;
            if (sourceValue < 2.0F) {
               sourceValue = 2.0F;
            }

            float targetValue = this.value2 - sourceValue;
            float inputValue = this.value2 * this.value2 - targetValue * targetValue;
            float outputValue = inputValue > 0.0F ? (float)Math.sqrt(inputValue) : 0.0F;
            float resultValue = (float)Math.ceil(sourceValue + this.value2 - outputValue);
            if (resultValue < sourceValue) {
               resultValue = sourceValue;
            }

            if (resultValue > this.cw * 0.5F) {
               resultValue = this.cw * 0.5F;
            }

            float candidateValue = this.cx + Math.max(resultValue, Math.min(this.cw - resultValue, this.value * this.cw));
            float selectedValue = this.cy + this.ch * 0.5F;
            compositorPushPresentationScale.overlayRect(
               candidateValue - sourceValue,
               selectedValue - sourceValue,
               sourceValue * 2.0F,
               sourceValue * 2.0F,
               sourceValue,
               sourceValue,
               sourceValue,
               sourceValue,
               mulAlpha(-1, this.effectiveOpacity),
               0.0F,
               2.0F * this.effectiveOpacity,
               mulAlpha(1610612736, this.effectiveOpacity),
               0.0F,
               0
            );
         }
      }
   }

   private void updateState(CompositorPushPresentationScaleService compositorPushPresentationScale, float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      compositorPushPresentationScale.pushClip(value, currentValue, nextValue, 16.0F, previousValue, sourceValue, sourceValue, previousValue);
      compositorPushPresentationScale.hueBar(this.cx, currentValue, this.cw, 16.0F, 8.0F, this.effectiveOpacity);
      compositorPushPresentationScale.popClip();
   }

   private static float calculateValue(float value) {
      return !Float.isFinite(value) ? 0.0F : Math.max(0.0F, Math.min(1.0F, value));
   }

   private static boolean checkCondition(float value, float currentValue) {
      return Float.floatToIntBits(value) == Float.floatToIntBits(currentValue);
   }
}

