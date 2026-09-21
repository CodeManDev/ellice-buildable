package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class MaterialResponsiveService extends SceneCornerRadiusService {
   private boolean enabled;
   private int count;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private int count2;
   private float value;
   private float value2;
   private float value3;
   private static final MotionFiniteService motionFiniteService = MotionFiniteService.finite("materialSurfaceActivity", item -> item instanceof MaterialResponsiveService);
   private static final MotionFiniteService motionFiniteService2 = MotionFiniteService.finite("materialSurfaceEngagement", item -> item instanceof MaterialResponsiveService);
   private static final MotionFiniteService motionFiniteService3 = MotionFiniteService.finite("materialSurfaceSelection", item -> item instanceof MaterialResponsiveService);

   public MaterialResponsiveService responsive(boolean enabled) {
      this.enabled2 = enabled;
      return this;
   }

   public void keyboardActive(boolean enabled) {
      this.enabled3 = enabled;
      this.invalidate();
   }

   public MaterialResponsiveService selected(boolean enabled) {
      if (!this.enabled4) {
         this.enabled4 = true;
         this.value3 = enabled ? 1.0F : 0.0F;
      } else if (this.enabled5 != enabled) {
         MotionAnimateService.animate(this, motionFiniteService3, enabled ? 1.0F : 0.0F, MaterialEnterService.RELEASE);
      }

      this.enabled5 = enabled;
      return this;
   }

   public MaterialResponsiveService surface(int value) {
      if (!this.enabled) {
         this.enabled = true;
         this.backgroundColor(value);
      } else if (this.count != value) {
         MotionAnimateService.animate(this, MotionColorsContainer.Colors.BACKGROUND, value, MaterialIsLightService.FAST_EFFECTS);
      }

      this.count = value;
      return this;
   }

   @Override
   public float getAnimProperty(String text) {
      return switch (text) {
         case "materialSurfaceActivity" -> this.value;
         case "materialSurfaceEngagement" -> this.value2;
         case "materialSurfaceSelection" -> this.value3;
         default -> super.getAnimProperty(text);
      };
   }

   @Override
   public void setAnimProperty(String text, float currentValue) {
      switch (text) {
         case "materialSurfaceActivity":
            this.value = currentValue;
            break;
         case "materialSurfaceEngagement":
            this.value2 = currentValue;
            break;
         case "materialSurfaceSelection":
            this.value3 = currentValue;
            break;
         default:
            super.setAnimProperty(text, currentValue);
      }
   }

   private static int calculateValue(ScenePctService<?> scenePct) {
      if (scenePct.visible && scenePct.pointerEvents()) {
         int value = !scenePct.isPressed() && !(scenePct instanceof ControlLetterSpacingService controlLetterSpacing && controlLetterSpacing.focused()) ? (scenePct.isHovered() ? 1 : 0) : 3;

         for (ScenePctService currentScenePct : scenePct.children()) {
            value |= calculateValue(currentScenePct);
            if (value == 3) {
               break;
            }
         }

         return value;
      } else {
         return 0;
      }
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      super.draw(compositorPushPresentationScale);
      int y = this.enabled2 ? (this.enabled3 ? 3 : calculateValue(this)) : 0;
      if (y != this.count2) {
         MotionAnimateService.animate(this, motionFiniteService, (y & 1) != 0 ? 1.0F : 0.0F, MaterialIsLightService.FAST_EFFECTS);
         MotionAnimateService.animate(this, motionFiniteService2, (y & 2) != 0 ? 1.0F : 0.0F, (y & 2) != 0 ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
         this.count2 = y;
      }

      float width = Math.max(0.0F, Math.min(1.0F, this.value));
      float height = Math.max(0.0F, Math.min(1.0F, this.value2));
      float currentValue = this.cornerTL >= 0.0F ? this.cornerTL : this.cornerRadius;
      float nextValue = this.cornerTR >= 0.0F ? this.cornerTR : this.cornerRadius;
      float previousValue = this.cornerBR >= 0.0F ? this.cornerBR : this.cornerRadius;
      float sourceValue = this.cornerBL >= 0.0F ? this.cornerBL : this.cornerRadius;
      if (width + height > 0.001F) {
         compositorPushPresentationScale.roundedRect(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            currentValue,
            nextValue,
            previousValue,
            sourceValue,
            mulAlpha(
               MaterialIsLightService.PRIMARY,
               this.effectiveOpacity * (0.045F * width + 0.035F * height)
            ),
            0.0F,
            0.0F,
            0,
            0.0F,
            0,
            1.0F,
            this.effectiveEdgeSoftness
         );
      }

      float targetValue = Math.max(
         Math.max(0.0F, Math.min(1.08F, this.value3)),
         Math.max(0.0F, Math.min(1.06F, this.value2))
      );
      if (targetValue > 0.001F) {
         float inputValue = Math.min(
               20.0F * this.presentationScale,
               this.ch - 12.0F * this.presentationScale
            )
            * targetValue;
         compositorPushPresentationScale.roundedRect(
            this.cx + 2.0F * this.presentationScale,
            this.cy + (this.ch - inputValue) / 2.0F,
            2.0F * this.presentationScale,
            inputValue,
            1.0F,
            mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity * Math.min(1.0F, targetValue))
         );
      }
   }
}

