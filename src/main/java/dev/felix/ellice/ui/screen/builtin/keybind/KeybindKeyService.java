package dev.felix.ellice.ui.screen.builtin.keybind;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.text.TextMode;

public final class KeybindKeyService extends ScenePctService<KeybindKeyService> {
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("keyTravel", item -> item instanceof KeybindKeyService);
   private static final MotionFiniteService motionFiniteService2 = MotionFiniteService.finite("keySelection", item -> item instanceof KeybindKeyService);
   private static final MotionFiniteService motionFiniteService3 = MotionFiniteService.finite("keyPulse", item -> item instanceof KeybindKeyService);
   private float value;
   private float value2;
   private float value3;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private String text2 = "";
   private String text3 = "";
   private int count;
   private int count2;

   public KeybindKeyService() {
      this.interactive(true).stopPropagation(true).cursorStyle(ScenePctService.CursorStyle.POINTER);
   }

   public KeybindKeyService key(int value, String text, String currentText, int currentValue, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled) {
      this.count2 = value;
      this.text2 = text;
      this.text3 = currentText;
      this.count = currentValue;
      this.enabled4 = nextEnabled;
      this.enabled3 = previousEnabled;
      if (this.enabled != currentEnabled) {
         this.enabled = currentEnabled;
         MotionAnimateService.animate(this, motionFiniteService2, currentEnabled ? 1.0F : 0.0F, MaterialIsLightService.EFFECTS);
      }

      return this;
   }

   public int code() {
      return this.count2;
   }

   public void keyboardFocused(boolean enabled) {
      this.enabled2 = enabled;
      this.invalidate();
   }

   public void activate() {
      this.pulse();
      if (this.onClick != null) {
         this.onClick.run();
      }
   }

   public void pulse() {
      motionFiniteService3.set(this, 1.0F);
      MotionAnimateService.animate(
         this, motionFiniteService3, 0.0F, new SceneEaseHandler.Spring(14.0F, 300.0F)
      );
   }

   @Override
   protected void onPress(float value, float currentValue) {
      MotionAnimateService.animate(this, motionFiniteService, 1.0F, MaterialIsLightService.FAST_SPATIAL);
   }

   @Override
   protected void onRelease(boolean enabled) {
      MotionAnimateService.animate(
         this, motionFiniteService, 0.0F, new SceneEaseHandler.Spring(14.0F, 320.0F)
      );
      if (enabled) {
         this.pulse();
      }
   }

   @Override
   public float getAnimProperty(String text) {
      return switch (text) {
         case "keyTravel" -> this.value;
         case "keySelection" -> this.value2;
         case "keyPulse" -> this.value3;
         default -> super.getAnimProperty(text);
      };
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      switch (text) {
         case "keyTravel":
            this.value = currentValue;
            break;
         case "keySelection":
            this.value2 = currentValue;
            break;
         case "keyPulse":
            this.value3 = currentValue;
            break;
         default:
            super.setAnimProperty(text, currentValue);
      }
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.enabled5 != this.hovered) {
         this.enabled5 = this.hovered;
         MotionAnimateService.animate(this, MotionColorsContainer.Floats.HOVER_PROGRESS, this.hovered ? 1.0F : 0.0F, MaterialIsLightService.FAST_EFFECTS);
      }

      float y = this.getAnimProperty("hoverProgress");
      float width = Math.max(0.0F, Math.min(1.0F, this.value2));
      float height = Math.max(
         -0.3F, Math.min(1.0F, this.value + this.value3 * 0.35F)
      );
      float currentValue = this.cx + 2.0F;
      float nextValue = this.cy + 2.0F + height * 2.3F;
      float previousValue = Math.max(1.0F, this.cw - 4.0F);
      float sourceValue = Math.max(1.0F, this.ch - 7.0F - height * 1.1F);
      float targetValue = Math.min(9.0F, Math.min(previousValue, sourceValue) * 0.25F);
      int inputValue = this.enabled3 ? MaterialIsLightService.SURFACE_CONTAINER : MaterialIsLightService.SURFACE_HIGH;
      int outputValue = MaterialIsLightService.layer(
         MaterialIsLightService.layer(inputValue, MaterialIsLightService.PRIMARY, width * 0.94F),
         MaterialIsLightService.ON_SURFACE,
         y * 0.075F * (1.0F - width)
      );
      int resultValue = MaterialIsLightService.layer(this.enabled3 ? MaterialIsLightService.OUTLINE : MaterialIsLightService.ON_SURFACE, MaterialIsLightService.ON_PRIMARY, width);
      compositorPushPresentationScale.roundedRect(
         currentValue,
         this.cy + 5.0F,
         previousValue,
         Math.max(1.0F, this.ch - 7.0F),
         targetValue,
         mulAlpha(MaterialIsLightService.SURFACE_LOWEST, this.effectiveOpacity)
      );
      compositorPushPresentationScale.roundedRect(
         currentValue,
         nextValue,
         previousValue,
         sourceValue,
         targetValue,
         targetValue,
         targetValue,
         targetValue,
         mulAlpha(outputValue, this.effectiveOpacity),
         0.0F,
         (y * 3.0F + width * 5.0F) * this.presentationScale,
         mulAlpha(
            MaterialIsLightService.PRIMARY,
            this.effectiveOpacity * (width * 0.18F + y * 0.08F)
         ),
         0.6F,
         mulAlpha(MaterialIsLightService.layer(MaterialIsLightService.OUTLINE_VARIANT, MaterialIsLightService.PRIMARY, width), this.effectiveOpacity),
         1.0F
      );
      compositorPushPresentationScale.roundedRect(
         currentValue + 3.0F,
         nextValue + 1.0F,
         Math.max(1.0F, previousValue - 6.0F),
         0.7F,
         0.4F,
         mulAlpha(620756991, this.effectiveOpacity * (1.0F - width))
      );
      if (this.enabled2) {
         compositorPushPresentationScale.roundedRect(
            currentValue - 1.5F,
            nextValue - 1.5F,
            previousValue + 3.0F,
            sourceValue + 3.0F,
            targetValue + 1.0F,
            targetValue + 1.0F,
            targetValue + 1.0F,
            targetValue + 1.0F,
            0,
            0.0F,
            0.0F,
            0,
            1.5F,
            mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity),
            1.0F
         );
      }

      if (this.value3 > 0.01F) {
         float candidateValue = (1.0F - Math.max(0.0F, this.value3)) * 4.0F;
         compositorPushPresentationScale.roundedRect(
            currentValue - candidateValue,
            nextValue - candidateValue,
            previousValue + 2.0F * candidateValue,
            sourceValue + 2.0F * candidateValue,
            targetValue + candidateValue,
            targetValue + candidateValue,
            targetValue + candidateValue,
            targetValue + candidateValue,
            0,
            0.0F,
            0.0F,
            0,
            0.65F,
            mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity * Math.max(0.0F, this.value3) * 0.6F),
            1.0F
         );
      }

      float currentLength = this.text2.length() > 4
         ? 10.0F
         : (this.text2.length() > 1 ? 11.0F : 15.0F);
      float selectedValue = compositorPushPresentationScale.textWidth(this.text2, currentLength, TextMode.REGULAR, "material-roboto-medium");
      if (selectedValue > previousValue - 10.0F) {
         currentLength *= Math.max(0.5F, (previousValue - 10.0F) / selectedValue);
         selectedValue = compositorPushPresentationScale.textWidth(this.text2, currentLength, TextMode.REGULAR, "material-roboto-medium");
      }

      float defaultValue = sourceValue < 36.0F
         ? nextValue + (sourceValue - compositorPushPresentationScale.textLineHeight(currentLength, TextMode.REGULAR, "material-roboto-medium")) / 2.0F
         : nextValue + 7.0F;
      compositorPushPresentationScale.text(
         currentValue
            + (
               !(previousValue > 80.0F) && !(previousValue < 34.0F)
                  ? 7.0F
                  : (previousValue - selectedValue) / 2.0F
            ),
         defaultValue,
         this.text2,
         currentLength,
         mulAlpha(resultValue, this.effectiveOpacity),
         MaterialIsLightService.LABEL
      );
      if (sourceValue >= 36.0F && !this.text3.isBlank()) {
         String currentText = this.text3;
         float initialValue = 8.0F;
         if (compositorPushPresentationScale.textWidth(currentText, initialValue, TextMode.REGULAR, "material-roboto") > previousValue - 14.0F) {
            while (
               !currentText.isEmpty()
                  && compositorPushPresentationScale.textWidth(currentText + "…", initialValue, TextMode.REGULAR, "material-roboto") > previousValue - 14.0F
            ) {
               currentText = currentText.substring(0, currentText.offsetByCodePoints(currentText.length(), -1));
            }

            currentText = currentText.isEmpty() ? "" : currentText + "…";
         }

         compositorPushPresentationScale.text(
            currentValue + 7.0F,
            nextValue + sourceValue - 13.0F,
            currentText,
            initialValue,
            mulAlpha(
               MaterialIsLightService.layer(MaterialIsLightService.ON_SURFACE_VARIANT, MaterialIsLightService.ON_PRIMARY, width),
               this.effectiveOpacity * 0.82F
            ),
            MaterialIsLightService.BODY
         );
      }

      if (this.count != 0 || this.enabled4) {
         int currentCount = width > 0.5F
            ? MaterialIsLightService.ON_PRIMARY
            : (this.count != 0 ? this.count : MaterialIsLightService.PRIMARY);
         compositorPushPresentationScale.roundedRect(
            currentValue + previousValue - 8.0F,
            nextValue + 6.0F,
            3.0F,
            3.0F,
            1.5F,
            mulAlpha(currentCount, this.effectiveOpacity)
         );
      }
   }
}
