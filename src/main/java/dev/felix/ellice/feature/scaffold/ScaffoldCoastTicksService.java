package dev.felix.ellice.feature.scaffold;

public final class ScaffoldCoastTicksService {
   private ScaffoldCoastTicksService() {
   }

   public static double coastTicks(double doubleValue) {
      if (Double.isFinite(doubleValue) && !(doubleValue < 0.0) && !(doubleValue > 1.0)) {
         double currentDoubleValue = Math.nextUp((double)((float)doubleValue * 0.91F));
         return 1.0 / (1.0 - currentDoubleValue);
      } else {
         throw new IllegalArgumentException("Ground friction must be in [0, 1]");
      }
   }
}
