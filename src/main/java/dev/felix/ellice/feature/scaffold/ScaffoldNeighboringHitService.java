package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import java.util.Objects;
import java.util.Optional;

public final class ScaffoldNeighboringHitService {
  private final RotationVanillaGcdService rotationVanillaGcdService =
      new RotationVanillaGcdService();

  public Optional<ScaffoldNeighboringHitService.Selection> neighboringHit(
      RotationData rotationData,
      RotationData currentRotationData,
      double doubleValue,
      double currentDoubleValue,
      ScaffoldNeighboringHitService.Probe probe) {
    return this.neighboringHit(
        rotationData, currentRotationData, doubleValue, currentDoubleValue, 1, probe);
  }

  public Optional<ScaffoldNeighboringHitService.Selection> neighboringHit(
      RotationData rotationData,
      RotationData currentRotationData,
      double doubleValue,
      double currentDoubleValue,
      int value,
      ScaffoldNeighboringHitService.Probe currentProbe) {
    Objects.requireNonNull(rotationData, "current");
    Objects.requireNonNull(currentRotationData, "ideal");
    Objects.requireNonNull(currentProbe, "probe");
    if (Double.isFinite(doubleValue)
        && !(doubleValue < 0.0)
        && !(doubleValue > 1.0)
        && Double.isFinite(currentDoubleValue)
        && !(currentDoubleValue < 0.0)
        && !(currentDoubleValue > 0.5)
        && value >= 0
        && value <= 8) {
      double nextDoubleValue = this.rotationVanillaGcdService.vanillaGcd(doubleValue);
      long longValue =
          Math.round(
              RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw())
                  / nextDoubleValue);
      long currentLongValue =
          Math.round((currentRotationData.pitch() - rotationData.pitch()) / nextDoubleValue);
      ScaffoldNeighboringHitService.Selection selection = null;
      double previousDoubleValue = Double.POSITIVE_INFINITY;

      for (long index = longValue - value; index <= longValue + value; index++) {
        for (long currentIndex = currentLongValue - value;
            currentIndex <= currentLongValue + value;
            currentIndex++) {
          RotationData nextRotationData =
              ScaffoldNextService.applyMouseCounts(rotationData, index, currentIndex, doubleValue);
          double sourceDoubleValue = currentProbe.faceMargin(nextRotationData);
          if (Double.isFinite(sourceDoubleValue)
              && !(sourceDoubleValue < currentDoubleValue)
              && !(sourceDoubleValue > 0.5)) {
            double targetDoubleValue =
                RotationData.distance(nextRotationData, currentRotationData)
                    - sourceDoubleValue * 8.0;
            if (targetDoubleValue < previousDoubleValue) {
              previousDoubleValue = targetDoubleValue;
              selection =
                  new ScaffoldNeighboringHitService.Selection(
                      nextRotationData, index, currentIndex, sourceDoubleValue);
            }
          }
        }
      }

      return Optional.ofNullable(selection);
    } else {
      throw new IllegalArgumentException("Invalid quantized-ray search");
    }
  }

  @FunctionalInterface
  public interface Probe {
    double faceMargin(RotationData rotationData);
  }

  public record Selection(
      RotationData rotation, long yawMouseCounts, long pitchMouseCounts, double faceMargin) {}
}
