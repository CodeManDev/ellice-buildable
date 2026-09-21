package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public final class ContinuousSupportChecker {
   private ContinuousSupportChecker() {
   }

   public static boolean hasContinuousSupport(RotationVector rotationVector, ScaffoldSweepsService scaffoldSweeps, int value, ContinuousSupportChecker.Query query) {
      Objects.requireNonNull(scaffoldSweeps, "envelope");

      for (ScaffoldSweepsService.Sweep sweep : scaffoldSweeps.sweeps()) {
         if (!hasContinuousSupport(rotationVector, sweep.direction().x(), sweep.direction().z(), sweep.distance(), value, query)) {
            return false;
         }
      }

      return true;
   }

   public static boolean hasContinuousSupport(RotationVector rotationVector, double doubleValue, double currentDoubleValue, double nextDoubleValue, int value, ContinuousSupportChecker.Query currentQuery) {
      Objects.requireNonNull(rotationVector, "feet");
      Objects.requireNonNull(currentQuery, "query");
      double previousDoubleValue = Math.hypot(doubleValue, currentDoubleValue);
      if (Double.isFinite(previousDoubleValue) && !(previousDoubleValue < 1.0E-6) && Double.isFinite(nextDoubleValue) && !(nextDoubleValue < 0.0)) {
         double sourceDoubleValue = doubleValue / previousDoubleValue;
         double targetDoubleValue = currentDoubleValue / previousDoubleValue;
         double inputDoubleValue = -targetDoubleValue;
         double outputDoubleValue = sourceDoubleValue;
         int currentValue = Math.max(1, (int)Math.ceil(nextDoubleValue / 0.16));

         for (int index = 0; index <= currentValue; index++) {
            double resultDoubleValue = nextDoubleValue * index / currentValue;
            byte byteValue = 0;

            for (double candidateDoubleValue : new double[]{
               -0.24, 0.0, 0.24
            }) {
               int currentX = (int)Math.floor(rotationVector.x() + sourceDoubleValue * resultDoubleValue + inputDoubleValue * candidateDoubleValue);
               int nextValue = (int)Math.floor(rotationVector.z() + targetDoubleValue * resultDoubleValue + outputDoubleValue * candidateDoubleValue);
               if (currentQuery.hasSupport(new BlockCoordinates(currentX, value, nextValue))) {
                  byteValue = 1;
                  break;
               }
            }

            if (byteValue == 0) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   @FunctionalInterface
   public interface Query {
      boolean hasSupport(BlockCoordinates blockCoordinates);
   }
}
