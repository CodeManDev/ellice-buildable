package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.DoubleFunction;

public final class BowTrajectorySolver {
   public static final double DRAG = 0.99F;
   public static final double GRAVITY = 0.05;
   public static final double EYE_OFFSET = 0.1F;
   public static final int MAX_FLIGHT = 80;

   private BowTrajectorySolver() {
   }

   public static double speed(int value) {
      float currentValue = Math.max(0, value) / 20.0F;
      currentValue = Math.min(1.0F, (currentValue * currentValue + currentValue * 2.0F) / 3.0F);
      return currentValue * 3.0F;
   }

   public static Optional<BowTrajectorySolver.Solution> solve(RotationVector rotationVector, RotationVector currentRotationVector, double doubleValue, DoubleFunction<RotationVector> doubleFunction) {
      if (Double.isFinite(doubleValue)
         && !(doubleValue < 0.3)
         && !(doubleValue > 3.0)) {
         double currentDoubleValue = 0.05;
         double currentLength = createRotationData7(rotationVector, currentRotationVector, (RotationVector)doubleFunction.apply(currentDoubleValue), currentDoubleValue).length() - doubleValue;

         for (double nextDoubleValue = 0.25;
            nextDoubleValue <= 80.0;
            nextDoubleValue += 0.25
         ) {
            double nextLength = createRotationData7(rotationVector, currentRotationVector, (RotationVector)doubleFunction.apply(nextDoubleValue), nextDoubleValue).length() - doubleValue;
            if (currentLength >= 0.0 && nextLength <= 0.0) {
               double previousDoubleValue = currentDoubleValue;
               double sourceDoubleValue = nextDoubleValue;

               for (int index = 0; index < 28; index++) {
                  double targetDoubleValue = (previousDoubleValue + sourceDoubleValue) * 0.5;
                  if (createRotationData7(rotationVector, currentRotationVector, (RotationVector)doubleFunction.apply(targetDoubleValue), targetDoubleValue).length() > doubleValue) {
                     previousDoubleValue = targetDoubleValue;
                  } else {
                     sourceDoubleValue = targetDoubleValue;
                  }
               }

               double inputDoubleValue = (previousDoubleValue + sourceDoubleValue) * 0.5;
               RotationVector nextRotationVector = (RotationVector)doubleFunction.apply(inputDoubleValue);
               RotationVector previousRotationVector = createRotationData7(rotationVector, currentRotationVector, nextRotationVector, inputDoubleValue);
               return Optional.of(new BowTrajectorySolver.Solution(RotationData.lookAt(new RotationVector(0.0, 0.0, 0.0), previousRotationVector), inputDoubleValue, nextRotationVector));
            }

            currentDoubleValue = nextDoubleValue;
            currentLength = nextLength;
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public static RotationVector position(RotationVector rotationVector, RotationVector currentRotationVector, double width) {
      if (Double.isFinite(width) && !(width < 0.0) && !(width > 85.0)) {
         double height = calculateValue(width);
         return rotationVector.add(currentRotationVector.multiply(height)).add(new RotationVector(0.0, -calculateValue2(width), 0.0));
      } else {
         throw new IllegalArgumentException("Invalid flight time");
      }
   }

   public static OptionalDouble hitFraction(RotationVector rotationVector, RotationVector currentRotationVector, RotationBoundingBox rotationBoundingBox, RotationVector nextRotationVector, RotationVector previousRotationVector) {
      RotationVector sourceRotationVector = rotationVector.subtract(nextRotationVector);
      RotationVector targetRotationVector = currentRotationVector.subtract(previousRotationVector);
      RotationVector inputRotationVector = targetRotationVector.subtract(sourceRotationVector);
      double doubleValue = 0.0;
      double currentDoubleValue = 1.0;

      for (int index = 0; index < 3; index++) {
         double currentX = switch (index) {
            case 0 -> sourceRotationVector.x();
            case 1 -> sourceRotationVector.y();
            default -> sourceRotationVector.z();
         };

         double nextX = switch (index) {
            case 0 -> inputRotationVector.x();
            case 1 -> inputRotationVector.y();
            default -> inputRotationVector.z();
         };

         double nextDoubleValue = switch (index) {
            case 0 -> rotationBoundingBox.minX();
            case 1 -> rotationBoundingBox.minY();
            default -> rotationBoundingBox.minZ();
         };

         double previousDoubleValue = switch (index) {
            case 0 -> rotationBoundingBox.maxX();
            case 1 -> rotationBoundingBox.maxY();
            default -> rotationBoundingBox.maxZ();
         };
         if (Math.abs(nextX) < 1.0E-12) {
            if (currentX < nextDoubleValue || currentX > previousDoubleValue) {
               return OptionalDouble.empty();
            }
         } else {
            double sourceDoubleValue = (nextDoubleValue - currentX) / nextX;
            double targetDoubleValue = (previousDoubleValue - currentX) / nextX;
            doubleValue = Math.max(doubleValue, Math.min(sourceDoubleValue, targetDoubleValue));
            currentDoubleValue = Math.min(currentDoubleValue, Math.max(sourceDoubleValue, targetDoubleValue));
            if (doubleValue > currentDoubleValue) {
               return OptionalDouble.empty();
            }
         }
      }

      return OptionalDouble.of(doubleValue);
   }

   private static RotationVector createRotationData7(RotationVector rotationVector, RotationVector currentRotationVector, RotationVector nextRotationVector, double doubleValue) {
      return nextRotationVector.subtract(rotationVector).add(new RotationVector(0.0, calculateValue2(doubleValue), 0.0)).multiply(1.0 / calculateValue(doubleValue)).subtract(currentRotationVector);
   }

   private static double calculateValue(double doubleValue) {
      int value = (int)doubleValue;
      double currentDoubleValue = Math.pow(0.9900000095367432, value);
      return (1.0 - currentDoubleValue) / 0.009999990463256836 + (doubleValue - value) * currentDoubleValue;
   }

   private static double calculateValue2(double doubleValue) {
      return 0.05 * (doubleValue - calculateValue(doubleValue)) / 0.009999990463256836;
   }

   public record Solution(RotationData rotation, double flightTicks, RotationVector intercept) {
   }
}
