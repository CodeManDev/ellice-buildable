package dev.felix.ellice.feature.antibot;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayDeque;

final class AntibotComponent {
   private final ArrayDeque<AntibotComponent.Sample> items = new ArrayDeque<>();

   void clear() {
      this.items.clear();
   }

   String observe(long longValue, RotationVector rotationVector, RotationVector currentRotationVector, double doubleValue, boolean enabled) {
      AntibotComponent.Sample sample = this.items.peekLast();
      if (enabled && finite(rotationVector) && finite(currentRotationVector) && Double.isFinite(doubleValue)) {
         if (sample != null
            && (
               longValue - sample.time > 250L
                  || longValue <= sample.time
                  || rotationVector.subtract(sample.self).length() > 8.0
                  || currentRotationVector.subtract(sample.other).length() > 8.0
            )) {
            this.clear();
         }

         this.items.addLast(new AntibotComponent.Sample(longValue, rotationVector, currentRotationVector, doubleValue));

         while (this.items.size() > 64 || longValue - this.items.getFirst().time > 2400L) {
            this.items.removeFirst();
         }

         if (this.items.size() >= 24 && longValue - this.items.getFirst().time >= 1200L) {
            RotationVector nextRotationVector = currentRotationVector.subtract(rotationVector);
            if (!(nextRotationVector.length() < 0.5)
               && !(nextRotationVector.length() > 8.0)) {
               RotationVector previousRotationVector = new RotationVector(0.0, 0.0, 0.0);
               RotationVector sourceRotationVector = previousRotationVector;
               double currentDoubleValue = 0.0;
               double nextDoubleValue = 0.0;
               double previousDoubleValue = 0.0;
               double sourceDoubleValue = 0.0;
               RotationVector targetRotationVector = null;
               AntibotComponent.Sample currentSample = null;

               for (AntibotComponent.Sample nextSample : this.items) {
                  previousRotationVector = previousRotationVector.add(nextSample.other.subtract(nextSample.self));
                  sourceRotationVector = sourceRotationVector.add(createRotationData7(nextSample));
                  if (currentSample != null) {
                     RotationVector inputRotationVector = nextSample.self.subtract(currentSample.self);
                     double currentX = Math.hypot(inputRotationVector.x(), inputRotationVector.z());
                     currentDoubleValue += currentX;
                     nextDoubleValue += nextSample.other.subtract(currentSample.other).length();
                     sourceDoubleValue += Math.abs(RotationData.yawDelta(currentSample.yaw, nextSample.yaw));
                     if (currentX > 0.04) {
                        RotationVector nextX = new RotationVector(inputRotationVector.x() / currentX, 0.0, inputRotationVector.z() / currentX);
                        if (targetRotationVector != null) {
                           previousDoubleValue += Math.toDegrees(
                              Math.acos(Math.max(-1.0, Math.min(1.0, nextX.dot(targetRotationVector))))
                           );
                        }

                        targetRotationVector = nextX;
                     }
                  }

                  currentSample = nextSample;
               }

               previousRotationVector = previousRotationVector.multiply(1.0 / this.items.size());
               sourceRotationVector = sourceRotationVector.multiply(1.0 / this.items.size());
               double targetDoubleValue = 0.0;
               double inputDoubleValue = 0.0;

               for (AntibotComponent.Sample previousSample : this.items) {
                  targetDoubleValue = Math.max(targetDoubleValue, previousSample.other.subtract(previousSample.self).subtract(previousRotationVector).length());
                  inputDoubleValue = Math.max(inputDoubleValue, createRotationData7(previousSample).subtract(sourceRotationVector).length());
               }

               if (currentDoubleValue >= 2.5
                  && previousDoubleValue >= 100.0
                  && targetDoubleValue < 0.08) {
                  return "Fixed offset through movement turns";
               } else {
                  return sourceDoubleValue >= 100.0
                        && nextDoubleValue >= 1.5
                        && Math.hypot(sourceRotationVector.x(), sourceRotationVector.z()) >= 0.65
                        && inputDoubleValue < 0.1
                     ? "Follows the camera through turns"
                     : "";
               }
            } else {
               return "";
            }
         } else {
            return "";
         }
      } else {
         this.clear();
         return "";
      }
   }

   private static RotationVector createRotationData7(AntibotComponent.Sample sample) {
      RotationVector rotationVector = sample.other.subtract(sample.self);
      double doubleValue = Math.toRadians(sample.yaw);
      double currentDoubleValue = Math.cos(doubleValue);
      double nextDoubleValue = Math.sin(doubleValue);
      return new RotationVector(rotationVector.x() * currentDoubleValue + rotationVector.z() * nextDoubleValue, rotationVector.y(), -rotationVector.x() * nextDoubleValue + rotationVector.z() * currentDoubleValue);
   }

   static boolean finite(RotationVector rotationVector) {
      return rotationVector != null && Double.isFinite(rotationVector.x()) && Double.isFinite(rotationVector.y()) && Double.isFinite(rotationVector.z());
   }

   private record Sample(long time, RotationVector self, RotationVector other, double yaw) {
   }
}
