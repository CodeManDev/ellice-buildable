package dev.felix.ellice.feature.terrain;

import org.joml.Vector3f;

public final class TerrainFogRangeService {
   private TerrainFogRangeService() {
   }

   public static Vector3f fogRange(double doubleValue, boolean enabled) {
      double currentDoubleValue = Double.isFinite(doubleValue) ? Math.max(0.0, doubleValue) : 220.0;
      float value = (float)Math.max(32.0, currentDoubleValue * 0.8);
      float currentValue = (float)Math.max(value + 64.0F, currentDoubleValue * 2.5);
      return new Vector3f(value, currentValue, enabled ? 0.05F : 0.0F);
   }

   public static Vector3f horizon(float value) {
      float currentValue = Float.isFinite(value) ? Math.clamp(value, 0.0F, 1.0F) : 1.0F;
      return new Vector3f(0.045F, 0.062F, 0.105F)
         .lerp(new Vector3f(0.6F, 0.72F, 0.85F), currentValue);
   }

   public static int[] targetSize(float value, float currentValue, float nextValue, boolean enabled) {
      double doubleValue = Math.max(0.01, nextValue)
         * (enabled ? 1.25 : 1.0);
      doubleValue *= Math.min(1.0, 2048.0 / (Math.max(1.0F, Math.max(value, currentValue)) * doubleValue));
      return new int[]{Math.max(32, (int)Math.round(value * doubleValue)), Math.max(32, (int)Math.round(currentValue * doubleValue))};
   }
}
