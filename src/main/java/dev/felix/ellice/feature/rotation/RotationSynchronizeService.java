package dev.felix.ellice.feature.rotation;

public final class RotationSynchronizeService {
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private double value;
   private double value2;

   public void synchronize(RotationData rotationData, RotationData currentRotationData) {
      this.value = RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw());
      this.value2 = currentRotationData.pitch() - rotationData.pitch();
   }

   public RotationVanillaGcdService.QuantizedRotation next(RotationData rotationData, RotationData currentRotationData, double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return this.next(rotationData, currentRotationData, doubleValue, currentDoubleValue, nextDoubleValue, true);
   }

   public RotationVanillaGcdService.QuantizedRotation next(RotationData rotationData, RotationData currentRotationData, double doubleValue, double currentDoubleValue, double nextDoubleValue, boolean enabled) {
      double previousDoubleValue = accelerationLimit(nextDoubleValue);
      double sourceDoubleValue = calculateValue(this.value, RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw()), previousDoubleValue, nextDoubleValue, enabled);
      double targetDoubleValue = calculateValue(this.value2, currentRotationData.pitch() - rotationData.pitch(), previousDoubleValue, nextDoubleValue, enabled);
      RotationVanillaGcdService.QuantizedRotation quantizedRotation = this.rotationVanillaGcdService
         .quantize(rotationData, new RotationData(rotationData.yaw() + sourceDoubleValue, rotationData.pitch() + targetDoubleValue).withPitchClamped(), doubleValue, currentDoubleValue);
      this.synchronize(rotationData, quantizedRotation.rotation());
      return quantizedRotation;
   }

   public static double accelerationLimit(double doubleValue) {
      return Math.max(0.25, doubleValue * 0.25);
   }

   public boolean moving() {
      return this.value != 0.0 || this.value2 != 0.0;
   }

   public void reset() {
      this.value = this.value2 = 0.0;
   }

   private static double calculateValue(double doubleValue, double currentDoubleValue, double nextDoubleValue, double previousDoubleValue, boolean enabled) {
      double sourceDoubleValue = nextDoubleValue;
      double targetDoubleValue = Math.clamp(currentDoubleValue, -previousDoubleValue, previousDoubleValue);
      if (enabled) {
         double inputDoubleValue = (Math.sqrt(sourceDoubleValue * sourceDoubleValue + 8.0 * sourceDoubleValue * Math.abs(currentDoubleValue)) - sourceDoubleValue)
            * 0.5;
         targetDoubleValue = Math.copySign(Math.min(Math.abs(targetDoubleValue), inputDoubleValue), targetDoubleValue);
      }

      double outputDoubleValue = doubleValue + Math.clamp(targetDoubleValue - doubleValue, -sourceDoubleValue, sourceDoubleValue);
      double resultDoubleValue = Math.min(Math.abs(currentDoubleValue), previousDoubleValue);
      return currentDoubleValue >= 0.0 ? Math.clamp(outputDoubleValue, 0.0, resultDoubleValue) : Math.clamp(outputDoubleValue, -resultDoubleValue, 0.0);
   }
}
