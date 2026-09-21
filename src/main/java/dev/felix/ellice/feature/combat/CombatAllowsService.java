package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;

public final class CombatAllowsService {
   public static final double MAX_RESERVE = 3.0;
   private static final double value = 0.075;

   private CombatAllowsService() {
   }

   public static boolean allows(
      ScaffoldRemapService.WorldVector worldVector, RotationVector rotationVector, double doubleValue, double currentDoubleValue, double nextDoubleValue, CombatAllowsService.Terrain terrain
   ) {
      if (Double.isFinite(nextDoubleValue)
         && !(nextDoubleValue < 0.0)
         && !(nextDoubleValue >= 0.99)
         && Double.isFinite(doubleValue)
         && !(doubleValue < 0.0)
         && Double.isFinite(currentDoubleValue)
         && !(currentDoubleValue < doubleValue)) {
         for (int index = 0; index <= 4; index++) {
            double previousDoubleValue = doubleValue + (currentDoubleValue - doubleValue) * index / 4.0;
            double currentX = (rotationVector.x() + worldVector.x() * previousDoubleValue) / (1.0 - nextDoubleValue);
            double sourceDoubleValue = (rotationVector.z() + worldVector.z() * previousDoubleValue) / (1.0 - nextDoubleValue);
            if (!checkCondition(currentX, sourceDoubleValue, terrain)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean checkCondition(double doubleValue, double currentDoubleValue, CombatAllowsService.Terrain terrain) {
      double nextDoubleValue = Math.hypot(doubleValue, currentDoubleValue);
      if (Double.isFinite(nextDoubleValue) && !(nextDoubleValue > 3.0)) {
         int value = Math.max(1, (int)Math.ceil(nextDoubleValue / 0.075));

         for (int index = 0; index <= value; index++) {
            if (!terrain.safeAt(doubleValue * index / value, currentDoubleValue * index / value)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @FunctionalInterface
   public interface Terrain {
      boolean safeAt(double doubleValue, double currentDoubleValue);
   }
}
