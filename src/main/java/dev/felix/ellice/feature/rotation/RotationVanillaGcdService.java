package dev.felix.ellice.feature.rotation;

public final class RotationVanillaGcdService {
  private static final double value = 1.0E-6;

  public double vanillaGcd(double doubleValue) {
    return calculateValue(doubleValue) * 0.15;
  }

  private static double calculateValue(double doubleValue) {
    if (Double.isFinite(doubleValue) && !(doubleValue < 0.0) && !(doubleValue > 1.0)) {
      double currentDoubleValue = doubleValue * 0.6 + 0.2;
      return currentDoubleValue * currentDoubleValue * currentDoubleValue * 8.0;
    } else {
      throw new IllegalArgumentException("Sensitivity must be in [0, 1]");
    }
  }

  public RotationVanillaGcdService.QuantizedRotation quantize(
      RotationData rotationData,
      RotationData currentRotationData,
      double doubleValue,
      double currentDoubleValue) {
    if (!Double.isFinite(currentDoubleValue)) {
      throw new IllegalArgumentException("GCD offset must be finite");
    }

    double nextDoubleValue = this.vanillaGcd(doubleValue);
    double previousDoubleValue = Math.max(1.0E-6, nextDoubleValue + currentDoubleValue);
    int value = currentDoubleValue == 0.0 ? 1 : 0;
    RotationData nextRotationData =
        value != 0
            ? new RotationData((float) rotationData.yaw(), (float) rotationData.pitch())
            : rotationData;
    long longValue =
        Math.round(
            RotationData.yawDelta(nextRotationData.yaw(), currentRotationData.yaw())
                / previousDoubleValue);
    long currentLongValue =
        Math.round((currentRotationData.pitch() - nextRotationData.pitch()) / previousDoubleValue);
    RotationData previousRotationData;
    if (value != 0) {
      double sourceDoubleValue = calculateValue(doubleValue);
      float currentValue = (float) (longValue * sourceDoubleValue) * 0.15F;
      float nextValue = (float) (currentLongValue * sourceDoubleValue) * 0.15F;
      float previousValue = (float) nextRotationData.yaw() + currentValue;
      float sourceValue =
          Math.max(-90.0F, Math.min(90.0F, (float) nextRotationData.pitch() + nextValue));
      previousRotationData = new RotationData(previousValue, sourceValue);
    } else {
      currentLongValue =
          calculateValue2(nextRotationData.pitch(), currentLongValue, previousDoubleValue);
      previousRotationData =
          new RotationData(
              nextRotationData.yaw() + longValue * previousDoubleValue,
              nextRotationData.pitch() + currentLongValue * previousDoubleValue);
    }

    return new RotationVanillaGcdService.QuantizedRotation(
        previousRotationData, nextDoubleValue, previousDoubleValue, longValue, currentLongValue);
  }

  private static long calculateValue2(
      double doubleValue, long longValue, double currentDoubleValue) {
    double nextDoubleValue = doubleValue + longValue * currentDoubleValue;
    if (nextDoubleValue > 90.0) {
      return (long) Math.floor((90.0 - doubleValue) / currentDoubleValue);
    } else {
      return nextDoubleValue < -90.0
          ? (long) Math.ceil((-90.0 - doubleValue) / currentDoubleValue)
          : longValue;
    }
  }

  public record QuantizedRotation(
      RotationData rotation,
      double vanillaGcd,
      double effectiveGcd,
      long yawMouseCounts,
      long pitchMouseCounts) {}
}
