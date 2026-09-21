package dev.felix.ellice.ui.util;

import dev.felix.ellice.ui.scene.SceneEaseHandler;

public final class UtilIsPrimedService {
   private float value;
   private float value2;
   private boolean enabled;

   public boolean isPrimed() {
      return this.enabled;
   }

   public float position() {
      return this.value;
   }

   public float velocity() {
      return this.value2;
   }

   public void snap(float currentValue) {
      this.value = currentValue;
      this.value2 = 0.0F;
      this.enabled = true;
   }

   public void reset() {
      this.enabled = false;
      this.value2 = 0.0F;
   }

   public float step(float currentValue, float nextValue, SceneEaseHandler.Spring spring) {
      if (!this.enabled) {
         this.snap(currentValue);
         return this.value;
      }

      if (nextValue <= 0.0F) {
         return this.value;
      }

      byte byteValue = 8;
      float previousValue = nextValue / byteValue;
      float sourceValue = this.value;
      float targetValue = this.value2;

      for (int index = 0; index < byteValue; index++) {
         float inputValue = sourceValue - currentValue;
         float outputValue = -spring.stiffness() * inputValue - spring.damping() * targetValue;
         targetValue += outputValue * previousValue;
         sourceValue += targetValue * previousValue;
      }

      this.value = sourceValue;
      this.value2 = targetValue;
      return this.value;
   }
}

