package dev.felix.ellice.feature.velocity;

import dev.felix.ellice.feature.rotation.RotationVector;

public final class VelocityTransformConverter {
   private long timestamp = Long.MIN_VALUE;

   public static RotationVector transform(
      VelocityTransformConverter.Mode mode, RotationVector rotationVector, RotationVector currentRotationVector, boolean enabled, double doubleValue, double currentDoubleValue
   ) {
      if (!Double.isFinite(doubleValue)
         || !Double.isFinite(currentDoubleValue)
         || doubleValue < 0.0
         || doubleValue > 200.0
         || currentDoubleValue < 0.0
         || currentDoubleValue > 200.0) {
         throw new IllegalArgumentException("Velocity percentages must be in [0, 200]");
      }

      if (mode != VelocityTransformConverter.Mode.PUSH && mode != VelocityTransformConverter.Mode.JUMP) {
         if (mode == VelocityTransformConverter.Mode.CANCEL || doubleValue == 0.0 && currentDoubleValue == 0.0) {
            return enabled ? new RotationVector(0.0, 0.0, 0.0) : rotationVector;
         }

         if (mode == VelocityTransformConverter.Mode.PERCENTAGE
            && doubleValue == 100.0
            && currentDoubleValue == 100.0) {
            return currentRotationVector;
         }

         RotationVector nextRotationVector = enabled ? currentRotationVector : currentRotationVector.subtract(rotationVector);
         double nextDoubleValue = doubleValue / 100.0 * (mode == VelocityTransformConverter.Mode.REVERSE ? -1 : 1);
         RotationVector currentX = new RotationVector(
            nextRotationVector.x() * nextDoubleValue, nextRotationVector.y() * currentDoubleValue / 100.0, nextRotationVector.z() * nextDoubleValue
         );
         return enabled ? currentX : rotationVector.add(currentX);
      } else {
         return currentRotationVector;
      }
   }

   public void impulse(long longValue, int value) {
      if (this.timestamp == Long.MIN_VALUE) {
         this.timestamp = longValue + Math.max(0, value);
      }
   }

   public boolean jump(long longValue, boolean enabled, boolean currentEnabled) {
      if (enabled && (this.timestamp == Long.MIN_VALUE || longValue <= this.timestamp + 3L)) {
         if (this.timestamp != Long.MIN_VALUE && longValue >= this.timestamp && currentEnabled) {
            this.reset();
            return true;
         } else {
            return false;
         }
      } else {
         this.reset();
         return false;
      }
   }

   public void reset() {
      this.timestamp = Long.MIN_VALUE;
   }

   public enum Mode {
      CANCEL,
      PERCENTAGE,
      REVERSE,
      PUSH,
      JUMP;


      private static VelocityTransformConverter.Mode[] $values() {
         return new VelocityTransformConverter.Mode[]{CANCEL, PERCENTAGE, REVERSE, PUSH, JUMP};
      }
   }
}
