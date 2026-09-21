package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class MaterialEnterService {
   public static final SceneEaseHandler.Spring PRESS = createSpring(0.88F, 1800.0F);
   public static final SceneEaseHandler.Spring RELEASE = createSpring(0.46F, 600.0F);
   public static final SceneEaseHandler.Spring PAGE = createSpring(0.62F, 420.0F);
   public static final SceneEaseHandler.Spring LEADING_EDGE = createSpring(0.72F, 720.0F);
   public static final SceneEaseHandler.Spring TRAILING_EDGE = createSpring(0.58F, 420.0F);

   private MaterialEnterService() {
   }

   private static SceneEaseHandler.Spring createSpring(float value, float currentValue) {
      return new SceneEaseHandler.Spring(2.0F * value * (float)Math.sqrt(currentValue), currentValue);
   }

   public static void enter(ScenePctService<?> scenePct, float value, float currentValue) {
      enter(scenePct, value, currentValue, 0.0F);
   }

   public static void enter(ScenePctService<?> scenePct, float value, float currentValue, float nextValue) {
      if (scenePct != null) {
         MotionAnimateService.cancel(scenePct, MotionColorsContainer.Floats.TRANSLATE_X);
         MotionAnimateService.cancel(scenePct, MotionColorsContainer.Floats.TRANSLATE_Y);
         scenePct.translate(value, currentValue);
         if (value != 0.0F) {
            MotionAnimateService.animate(scenePct, MotionColorsContainer.Floats.TRANSLATE_X, 0.0F, PAGE, nextValue);
         }

         if (currentValue != 0.0F) {
            MotionAnimateService.animate(scenePct, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0F, PAGE, nextValue);
         }
      }
   }

   public static void reveal(ScenePctService<?> scenePct, float value, float currentValue) {
      if (scenePct != null) {
         enter(scenePct, value, currentValue);
         MotionAnimateService.cancel(scenePct, MotionColorsContainer.Floats.OPACITY);
         scenePct.opacity(0.0F);
         MotionAnimateService.animate(scenePct, MotionColorsContainer.Floats.OPACITY, 1.0F, SceneEaseHandler.Tween.ease(0.26F));
      }
   }
}
