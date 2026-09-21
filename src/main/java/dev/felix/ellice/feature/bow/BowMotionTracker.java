package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.UUID;

public final class BowMotionTracker {
   private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
   private final BowObserveService bowObserveService = new BowObserveService();
   private UUID uUID;
   private RotationVector rotationData72;
   private RotationVector rotationData73 = rotationData7;
   private RotationVector rotationData74 = rotationData7;
   private long timestamp;
   private long timestamp2;
   private int count;
   private double value = 1.0;
   private double value2;

   public BowMotionTracker.Motion observe(long longValue, UUID currentUUID, RotationVector rotationVector, boolean enabled) {
      if (!currentUUID.equals(this.uUID)
         || this.rotationData72 == null
         || longValue - this.timestamp > 3L
         || longValue <= this.timestamp
         || rotationVector.subtract(this.rotationData72).length() > 1.5 * (longValue - this.timestamp)) {
         this.clear();
         this.uUID = currentUUID;
         this.timestamp2 = longValue;
         this.bowObserveService.observe(longValue, rotationVector);
      } else if (rotationVector.subtract(this.rotationData72).lengthSquared() > 1.0E-8) {
         long currentLongValue = longValue - this.timestamp2;
         if (currentLongValue <= 8L) {
            double doubleValue = Math.abs(currentLongValue - this.value);
            this.value = this.value + (currentLongValue - this.value) * 0.35;
            this.value2 = this.value2 + (doubleValue - this.value2) * 0.3;
         }

         double currentDoubleValue = Math.max(1L, longValue - this.bowObserveService.estimate().tick());
         RotationVector currentRotationVector = rotationVector.subtract(this.rotationData72).multiply(1.0 / currentDoubleValue);
         BowObserveService.Estimate currentEstimate = this.bowObserveService.observe(longValue, rotationVector);
         RotationVector currentX = new RotationVector(
            currentEstimate.velocity().x(),
            enabled ? 0.0 : currentRotationVector.y() - 0.04 * (currentDoubleValue - 1.0),
            currentEstimate.velocity().z()
         );
         this.rotationData74 = createRotationData7(
            new RotationVector(currentX.x() - this.rotationData73.x(), 0.0, currentX.z() - this.rotationData73.z()),
            0.045
         );
         this.rotationData73 = createRotationData7(currentX, 1.0);
         this.count++;
         this.timestamp2 = longValue;
      } else {
         int currentValue = Math.max(
            3, Math.min(8, (int)Math.ceil(this.value + this.value2 * 2.0 + 1.0))
         );
         if (longValue - this.timestamp2 >= currentValue) {
            this.bowObserveService.observe(longValue, rotationVector);
            this.rotationData73 = this.rotationData73.multiply(0.55);
         }

         if (enabled) {
            this.rotationData73 = new RotationVector(this.rotationData73.x(), 0.0, this.rotationData73.z());
         }

         this.rotationData74 = rotationData7;
         this.count++;
      }

      this.rotationData72 = rotationVector;
      this.timestamp = longValue;
      BowObserveService.Estimate nextEstimate = this.bowObserveService.estimate();
      double nextDoubleValue = Math.min(1.0, this.count / 6.0) * nextEstimate.confidence();
      int nextValue = (int)Math.min(4L, Math.max(0L, longValue - nextEstimate.tick()));
      return new BowMotionTracker.Motion(this.rotationData73, this.rotationData74, nextDoubleValue, enabled, nextValue, nextEstimate);
   }

   public void clear() {
      this.uUID = null;
      this.rotationData72 = null;
      this.rotationData73 = this.rotationData74 = rotationData7;
      this.count = 0;
      this.value = 1.0;
      this.value2 = 0.0;
      this.bowObserveService.clear();
   }

   private static RotationVector createRotationData7(RotationVector rotationVector, double doubleValue) {
      return rotationVector.length() > doubleValue ? rotationVector.multiply(doubleValue / rotationVector.length()) : rotationVector;
   }

   public record Motion(
      RotationVector velocity, RotationVector acceleration, double confidence, boolean grounded, int ageTicks, BowObserveService.Estimate ensemble
   ) {
   }
}
