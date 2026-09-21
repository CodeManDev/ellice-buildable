package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Optional;
import java.util.OptionalDouble;

public final class CombatCommonBoxService {
  private CombatCommonBoxService() {}

  public static Optional<RotationBoundingBox> commonBox(
      RotationBoundingBox rotationBoundingBox, RotationBoundingBox currentRotationBoundingBox) {
    double doubleValue = Math.max(rotationBoundingBox.minX(), currentRotationBoundingBox.minX());
    double currentDoubleValue =
        Math.max(rotationBoundingBox.minY(), currentRotationBoundingBox.minY());
    double nextDoubleValue =
        Math.max(rotationBoundingBox.minZ(), currentRotationBoundingBox.minZ());
    double previousDoubleValue =
        Math.min(rotationBoundingBox.maxX(), currentRotationBoundingBox.maxX());
    double sourceDoubleValue =
        Math.min(rotationBoundingBox.maxY(), currentRotationBoundingBox.maxY());
    double targetDoubleValue =
        Math.min(rotationBoundingBox.maxZ(), currentRotationBoundingBox.maxZ());
    double inputDoubleValue =
        Math.min(
            0.03,
            Math.min(
                    rotationBoundingBox.maxX() - rotationBoundingBox.minX(),
                    currentRotationBoundingBox.maxX() - currentRotationBoundingBox.minX())
                * 0.05);
    double outputDoubleValue =
        Math.min(
            0.03,
            Math.min(
                    rotationBoundingBox.maxY() - rotationBoundingBox.minY(),
                    currentRotationBoundingBox.maxY() - currentRotationBoundingBox.minY())
                * 0.05);
    double resultDoubleValue =
        Math.min(
            0.03,
            Math.min(
                    rotationBoundingBox.maxZ() - rotationBoundingBox.minZ(),
                    currentRotationBoundingBox.maxZ() - currentRotationBoundingBox.minZ())
                * 0.05);
    doubleValue += inputDoubleValue;
    previousDoubleValue -= inputDoubleValue;
    currentDoubleValue += outputDoubleValue;
    sourceDoubleValue -= outputDoubleValue;
    nextDoubleValue += resultDoubleValue;
    targetDoubleValue -= resultDoubleValue;
    return !(doubleValue >= previousDoubleValue)
            && !(currentDoubleValue >= sourceDoubleValue)
            && !(nextDoubleValue >= targetDoubleValue)
        ? Optional.of(
            new RotationBoundingBox(
                doubleValue,
                currentDoubleValue,
                nextDoubleValue,
                previousDoubleValue,
                sourceDoubleValue,
                targetDoubleValue))
        : Optional.empty();
  }

  public static Optional<RotationVector> hitPoint(
      RotationVector rotationVector,
      RotationData rotationData,
      RotationBoundingBox rotationBoundingBox,
      double height,
      double doubleValue) {
    if (Double.isFinite(height)
        && !(height <= 0.0)
        && Double.isFinite(doubleValue)
        && !(doubleValue <= 0.0)) {
      RotationVector currentRotationVector = rotationData.direction();
      OptionalDouble optionalDouble =
          rotationBoundingBox.rayIntersection(
              rotationVector, currentRotationVector, Math.min(height, doubleValue));
      return optionalDouble.isEmpty()
          ? Optional.empty()
          : Optional.of(
              rotationVector.add(currentRotationVector.multiply(optionalDouble.getAsDouble())));
    } else {
      return Optional.empty();
    }
  }
}
