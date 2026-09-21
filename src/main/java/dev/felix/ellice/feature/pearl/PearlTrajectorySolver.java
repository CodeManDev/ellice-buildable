package dev.felix.ellice.feature.pearl;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.List;

public final class PearlTrajectorySolver {
   public static final double SPEED = 1.5;
   public static final double GRAVITY = 0.03;
   public static final double DRAG = 0.99F;
   public static final double SPREAD = 0.02584125;
   public static final int HORIZON = 80;
   private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);

   private PearlTrajectorySolver() {
   }

   public static PearlTrajectorySolver.Launch launch(RotationVector rotationVector, RotationVector currentRotationVector, RotationData rotationData) {
      return new PearlTrajectorySolver.Launch(
         rotationVector.add(new RotationVector(0.0, -0.10000000149011612, 0.0)),
         rotationData.direction().multiply(1.5).add(currentRotationVector)
      );
   }

   public static RotationVector position(PearlTrajectorySolver.Launch launch, double y) {
      if (Double.isFinite(y) && !(y < 0.0) && !(y > 81.0)) {
         double width = calculateValue2(y);
         return launch.origin()
            .add(launch.velocity().multiply(width))
            .add(new RotationVector(0.0, -2.970002861025678 * (y - width), 0.0));
      } else {
         throw new IllegalArgumentException("Flight time");
      }
   }

   public static List<PearlTrajectorySolver.Solution> solve(RotationVector rotationVector, RotationVector currentRotationVector, RotationVector nextRotationVector, double doubleValue) {
      ArrayList arrayList = new ArrayList(2);
      RotationVector previousRotationVector = launch(rotationVector, currentRotationVector, new RotationData(0.0, 0.0)).origin();
      double currentDoubleValue = 0.05;
      double nextDoubleValue = calculateValue(previousRotationVector, currentRotationVector, nextRotationVector, currentDoubleValue);

      for (double previousDoubleValue = 0.25;
         previousDoubleValue <= Math.min(80.0, doubleValue);
         previousDoubleValue += 0.25
      ) {
         double sourceDoubleValue = calculateValue(previousRotationVector, currentRotationVector, nextRotationVector, previousDoubleValue);
         if (Math.signum(nextDoubleValue) != Math.signum(sourceDoubleValue)) {
            double targetDoubleValue = currentDoubleValue;
            double inputDoubleValue = previousDoubleValue;
            int value = nextDoubleValue > 0.0 ? 1 : 0;

            for (int index = 0; index < 25; index++) {
               double outputDoubleValue = (targetDoubleValue + inputDoubleValue) * 0.5;
               if ((calculateValue(previousRotationVector, currentRotationVector, nextRotationVector, outputDoubleValue) > 0.0 ? 1 : 0) == value) {
                  targetDoubleValue = outputDoubleValue;
               } else {
                  inputDoubleValue = outputDoubleValue;
               }
            }

            double resultDoubleValue = (targetDoubleValue + inputDoubleValue) * 0.5;
            arrayList.add(new PearlTrajectorySolver.Solution(RotationData.lookAt(rotationData7, createRotationData7(previousRotationVector, currentRotationVector, nextRotationVector, resultDoubleValue)), resultDoubleValue));
            if (arrayList.size() == 2) {
               break;
            }
         }

         currentDoubleValue = previousDoubleValue;
         nextDoubleValue = sourceDoubleValue;
      }

      return List.copyOf(arrayList);
   }

   private static double calculateValue(RotationVector rotationVector, RotationVector currentRotationVector, RotationVector nextRotationVector, double doubleValue) {
      return createRotationData7(rotationVector, currentRotationVector, nextRotationVector, doubleValue).length() - 1.5;
   }

   private static RotationVector createRotationData7(RotationVector rotationVector, RotationVector currentRotationVector, RotationVector nextRotationVector, double doubleValue) {
      double currentDoubleValue = calculateValue2(doubleValue);
      return nextRotationVector.subtract(rotationVector)
         .add(new RotationVector(0.0, 2.970002861025678 * (doubleValue - currentDoubleValue), 0.0))
         .multiply(1.0 / currentDoubleValue)
         .subtract(currentRotationVector);
   }

   private static double calculateValue2(double doubleValue) {
      int value = (int)doubleValue;
      double currentDoubleValue = Math.pow(0.9900000095367432, value);
      return 0.9900000095367432
         * ((1.0 - currentDoubleValue) / 0.009999990463256836 + (doubleValue - value) * currentDoubleValue);
   }

   public record Launch(RotationVector origin, RotationVector velocity) {
   }

   public record Solution(RotationData rotation, double ticks) {
   }
}
