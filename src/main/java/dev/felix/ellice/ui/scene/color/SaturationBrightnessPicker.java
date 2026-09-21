package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import java.util.function.BiConsumer;

public class SaturationBrightnessPicker extends ScenePctService<SaturationBrightnessPicker> {
   private float value;
   private float value2 = 1.0F;
   private float value3 = 1.0F;
   private float value4 = 4.0F;
   private BiConsumer<Float, Float> biConsumer;
   private BiConsumer<Float, Float> biConsumer2;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private float value5;
   private float value6;
   private float value7;
   private float value8;
   private float value9;
   private boolean enabled5;
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("materialColorPress", item -> item instanceof SaturationBrightnessPicker);
   private static final MotionFiniteService motionFiniteService2 = MotionFiniteService.finite("materialMarkerSat", item -> item instanceof SaturationBrightnessPicker);
   private static final MotionFiniteService motionFiniteService3 = MotionFiniteService.finite("materialMarkerBri", item -> item instanceof SaturationBrightnessPicker);

   @Override
   protected boolean handlesContinuousPointer() {
      return true;
   }

   public SaturationBrightnessPicker material(boolean enabled) {
      this.enabled2 = enabled;
      return this;
   }

   public void materialFocus(boolean enabled) {
      this.enabled3 = enabled;
      this.invalidate();
   }

   @Override
   public float getAnimProperty(String text) {
      return switch (text) {
         case "materialColorPress" -> this.value5;
         case "materialMarkerSat" -> this.value6;
         case "materialMarkerBri" -> this.value7;
         default -> super.getAnimProperty(text);
      };
   }

   @Override
   public void setAnimProperty(String text, float value) {
      switch (text) {
         case "materialColorPress":
            this.value5 = value;
            break;
         case "materialMarkerSat":
            this.value6 = value;
            break;
         case "materialMarkerBri":
            this.value7 = value;
            break;
         default:
            super.setAnimProperty(text, value);
      }
   }

   public SaturationBrightnessPicker() {
      this.interactive = true;
   }

   public SaturationBrightnessPicker hue(float currentValue) {
      if (Float.isFinite(currentValue) && !checkCondition(this.value, currentValue)) {
         this.value = currentValue;
         this.invalidate();
         return this;
      } else {
         return this;
      }
   }

   public SaturationBrightnessPicker sat(float value) {
      float currentValue = calculateValue(value);
      if (checkCondition(this.value2, currentValue)) {
         return this;
      }

      this.value2 = currentValue;
      this.invalidate();
      return this;
   }

   public SaturationBrightnessPicker bri(float value) {
      float currentValue = calculateValue(value);
      if (checkCondition(this.value3, currentValue)) {
         return this;
      }

      this.value3 = currentValue;
      this.invalidate();
      return this;
   }

   public SaturationBrightnessPicker cornerRadius(float value) {
      float currentValue = Float.isFinite(value) ? Math.max(0.0F, value) : this.value4;
      if (checkCondition(this.value4, currentValue)) {
         return this;
      }

      this.value4 = currentValue;
      this.invalidate();
      return this;
   }

   public SaturationBrightnessPicker onChange(BiConsumer<Float, Float> currentBiConsumer) {
      this.biConsumer = currentBiConsumer;
      return this;
   }

   public SaturationBrightnessPicker onCommit(BiConsumer<Float, Float> biConsumer) {
      this.biConsumer2 = biConsumer;
      return this;
   }

   public float hue() {
      return this.value;
   }

   public float sat() {
      return this.value2;
   }

   public float bri() {
      return this.value3;
   }

   @Override
   protected void handleClick() {
      if (this.onClick != null) {
         this.onClick.run();
      }
   }

   @Override
   protected void updateWhilePressed(float value, float currentValue) {
      if (!(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
         float nextValue = calculateValue((value - this.cx) / this.cw);
         float previousValue = calculateValue(1.0F - (currentValue - this.cy) / this.ch);
         if (!checkCondition(this.value2, nextValue) || !checkCondition(this.value3, previousValue)) {
            this.value2 = nextValue;
            this.value3 = previousValue;
            this.enabled = true;
            this.invalidate();
            if (this.biConsumer != null) {
               this.biConsumer.accept(this.value2, this.value3);
            }
         }
      }
   }

   @Override
   protected void onRelease(boolean currentEnabled) {
      if (this.enabled) {
         this.enabled = false;
         if (this.biConsumer2 != null) {
            this.biConsumer2.accept(this.value2, this.value3);
         }
      }
   }

   @Override
   protected void onDetached() {
      this.enabled = false;
      this.biConsumer = null;
      this.biConsumer2 = null;
   }

   @Override
   public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return 80.0F;
   }

   @Override
   public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return 80.0F;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (!(this.effectiveOpacity < 0.003F)) {
         compositorPushPresentationScale.colorField(this.cx, this.cy, this.cw, this.ch, this.value, this.value4, this.effectiveOpacity);
         if (this.enabled2 && this.pressed != this.enabled4) {
            this.enabled4 = this.pressed;
            MotionAnimateService.animate(this, motionFiniteService, this.pressed ? 1.0F : 0.0F, this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
         }

         if (this.enabled2) {
            if (!this.enabled5 || this.pressed) {
               this.enabled5 = true;
               MotionAnimateService.cancel(this, motionFiniteService2);
               MotionAnimateService.cancel(this, motionFiniteService3);
               this.value6 = this.value2;
               this.value7 = this.value3;
            } else if (this.value2 != this.value8 || this.value3 != this.value9) {
               MotionAnimateService.animate(this, motionFiniteService2, this.value2, MaterialEnterService.PAGE);
               MotionAnimateService.animate(this, motionFiniteService3, this.value3, MaterialEnterService.PAGE);
            }

            this.value8 = this.value2;
            this.value9 = this.value3;
         }

         float y = this.enabled2
            ? 6.0F
               + 4.0F * Math.max(0.0F, Math.min(1.06F, this.value5))
            : 5.0F;
         float width = (this.enabled2 ? 11.0F : y) + this.value4 * 0.42F;
         if (width > this.cw * 0.5F) {
            width = this.cw * 0.5F;
         }

         if (width > this.ch * 0.5F) {
            width = this.ch * 0.5F;
         }

         float height = this.cx
            + Math.max(width, Math.min(this.cw - width, (this.enabled2 ? this.value6 : this.value2) * this.cw));
         float currentValue = this.cy
            + Math.max(width, Math.min(this.ch - width, (1.0F - (this.enabled2 ? this.value7 : this.value3)) * this.ch));
         compositorPushPresentationScale.overlayRect(
            height - y,
            currentValue - y,
            y * 2.0F,
            y * 2.0F,
            y,
            y,
            y,
            y,
            0,
            0.0F,
            0.0F,
            0,
            2.0F,
            mulAlpha(-587202560, this.effectiveOpacity)
         );
         float nextValue = y - 1.5F;
         compositorPushPresentationScale.overlayRect(
            height - nextValue,
            currentValue - nextValue,
            nextValue * 2.0F,
            nextValue * 2.0F,
            nextValue,
            nextValue,
            nextValue,
            nextValue,
            0,
            0.0F,
            0.0F,
            0,
            1.2F,
            mulAlpha(-1, this.effectiveOpacity)
         );
         if (this.enabled2 && this.enabled3) {
            compositorPushPresentationScale.overlayRect(
               this.cx + 2.0F,
               this.cy + 2.0F,
               this.cw - 4.0F,
               this.ch - 4.0F,
               this.value4,
               this.value4,
               this.value4,
               this.value4,
               0,
               0.0F,
               0.0F,
               0,
               2.0F,
               mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity)
            );
         }
      }
   }

   private static float calculateValue(float value) {
      return !Float.isFinite(value) ? 0.0F : Math.max(0.0F, Math.min(1.0F, value));
   }

   private static boolean checkCondition(float value, float currentValue) {
      return Float.floatToIntBits(value) == Float.floatToIntBits(currentValue);
   }
}

