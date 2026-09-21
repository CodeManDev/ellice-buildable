package dev.felix.ellice.ui.scene.motion;

import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.function.Predicate;

public final class MotionFiniteService {
   private final String text;
   private final Predicate<ScenePctService<?>> predicate;
   private final MotionFiniteService.ValueConstraint valueConstraint;
   private final String text2;

   private MotionFiniteService(String currentText, Predicate<ScenePctService<?>> currentPredicate, MotionFiniteService.ValueConstraint currentValueConstraint, String nextText) {
      if (currentText != null && !currentText.isBlank()) {
         this.text = currentText;
         this.predicate = Objects.requireNonNull(currentPredicate, "supportedBy");
         this.valueConstraint = Objects.requireNonNull(currentValueConstraint, "constraint");
         this.text2 = Objects.requireNonNull(nextText, "constraintDescription");
      } else {
         throw new IllegalArgumentException("Property name must not be blank");
      }
   }

   public static MotionFiniteService of(String text, Predicate<ScenePctService<?>> predicate, MotionFiniteService.ValueConstraint valueConstraint, String currentText) {
      return new MotionFiniteService(text, predicate, valueConstraint, currentText);
   }

   public static MotionFiniteService finite(String text, Predicate<ScenePctService<?>> predicate) {
      return of(text, predicate, Float::isFinite, "a finite number");
   }

   public String compatibilityName() {
      return this.text;
   }

   public boolean supports(ScenePctService<?> scenePct) {
      return scenePct != null && this.predicate.test(scenePct);
   }

   public float get(ScenePctService<?> scenePct) {
      this.requireSupported(scenePct);
      return scenePct.getAnimProperty(this.text);
   }

   public void set(ScenePctService<?> scenePct, float value) {
      this.requireSupported(scenePct);
      this.validate(value);
      scenePct.setAnimProperty(this.text, value);
      scenePct.invalidate();
   }

   void requireSupported(ScenePctService<?> scenePct) {
      Objects.requireNonNull(scenePct, "node");
      if (!this.predicate.test(scenePct)) {
         throw new IllegalArgumentException("Property '" + this.text + "' is not supported by " + scenePct.getClass().getSimpleName());
      }
   }

   void validate(float value) {
      if (!this.valueConstraint.accepts(value)) {
         throw new IllegalArgumentException("Property '" + this.text + "' requires " + this.text2 + ", got " + value);
      }
   }

   @Override
   public String toString() {
      return "FloatProperty[" + this.text + "]";
   }

   @FunctionalInterface
   public interface ValueConstraint {
      boolean accepts(float value);
   }
}
