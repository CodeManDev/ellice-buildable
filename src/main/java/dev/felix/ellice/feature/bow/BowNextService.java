package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;

public final class BowNextService {
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private double value;
   private double value2;

   public BowNextService.Step next(RotationData rotationData, RotationData currentRotationData, double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw());
      double sourceDoubleValue = RotationData.clampPitch(currentRotationData.pitch()) - rotationData.pitch();
      double targetDoubleValue = currentDoubleValue / (1.0 + nextDoubleValue * 5.0);
      this.value = calculateValue2(this.value, calculateValue(previousDoubleValue, currentDoubleValue, targetDoubleValue), targetDoubleValue);
      this.value2 = calculateValue2(this.value2, calculateValue(sourceDoubleValue, currentDoubleValue, targetDoubleValue), targetDoubleValue);
      if (Math.abs(previousDoubleValue) <= targetDoubleValue) {
         this.value = previousDoubleValue;
      }

      if (Math.abs(sourceDoubleValue) <= targetDoubleValue) {
         this.value2 = sourceDoubleValue;
      }

      double inputDoubleValue = Math.hypot(this.value, this.value2);
      if (inputDoubleValue > currentDoubleValue) {
         this.value *= currentDoubleValue / inputDoubleValue;
         this.value2 *= currentDoubleValue / inputDoubleValue;
      }

      RotationVanillaGcdService.QuantizedRotation quantizedRotation = this.rotationVanillaGcdService
         .quantize(
            rotationData, new RotationData(rotationData.yaw() + this.value, RotationData.clampPitch(rotationData.pitch() + this.value2)), doubleValue, 0.0
         );
      return new BowNextService.Step(
         quantizedRotation.rotation(),
         RotationData.distance(quantizedRotation.rotation(), currentRotationData)
            <= quantizedRotation.vanillaGcd() * 0.8 + 1.0E-6
      );
   }

   public void reset() {
      this.value = this.value2 = 0.0;
   }

   private static double calculateValue(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return Math.copySign(Math.min(currentDoubleValue, Math.sqrt(2.0 * nextDoubleValue * Math.abs(doubleValue))), doubleValue);
   }

   private static double calculateValue2(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return doubleValue + Math.max(-nextDoubleValue, Math.min(nextDoubleValue, currentDoubleValue - doubleValue));
   }

   public record Step(RotationData rotation, boolean settled) {
   }
}
