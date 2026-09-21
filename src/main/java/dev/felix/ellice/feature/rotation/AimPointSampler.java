package dev.felix.ellice.feature.rotation;

import java.util.Random;

public final class AimPointSampler {
  private final Random random;
  private Integer integer;
  private RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
  private RotationVector rotationData72 = this.rotationData7;
  private int count;
  private int count2;

  public AimPointSampler(long longValue) {
    this.random = new Random(longValue);
  }

  public RotationVector next(
      RotationBoundingBox rotationBoundingBox,
      RotationVector rotationVector,
      int value,
      double doubleValue) {
    if (rotationBoundingBox.contains(rotationVector)
        && Double.isFinite(doubleValue)
        && !(doubleValue < 0.0)
        && !(doubleValue > 1.0)) {
      if (this.integer == null || this.integer != value) {
        this.reset();
        this.integer = value;
      }

      if (doubleValue == 0.0) {
        this.reset();
        return rotationVector;
      }

      if (this.count >= this.count2) {
        this.rotationData7 = this.rotationData72;
        this.rotationData72 =
            new RotationVector(this.calculateValue(), this.calculateValue(), this.calculateValue());
        this.count = 0;
        this.count2 = 8 + this.random.nextInt(13);
      }

      double currentDoubleValue = (double) (++this.count) / this.count2;
      double nextDoubleValue =
          currentDoubleValue
              * currentDoubleValue
              * currentDoubleValue
              * (currentDoubleValue * (currentDoubleValue * 6.0 - 15.0) + 10.0);
      RotationVector currentRotationVector =
          this.rotationData7.add(
              this.rotationData72.subtract(this.rotationData7).multiply(nextDoubleValue));
      double previousDoubleValue = Math.sqrt(doubleValue);
      return new RotationVector(
          calculateValue2(
              rotationVector.x(),
              currentRotationVector.x() * previousDoubleValue * 0.16,
              rotationBoundingBox.minX(),
              rotationBoundingBox.maxX()),
          calculateValue2(
              rotationVector.y(),
              currentRotationVector.y() * previousDoubleValue * 0.08,
              rotationBoundingBox.minY(),
              rotationBoundingBox.maxY()),
          calculateValue2(
              rotationVector.z(),
              currentRotationVector.z() * previousDoubleValue * 0.16,
              rotationBoundingBox.minZ(),
              rotationBoundingBox.maxZ()));
    } else {
      throw new IllegalArgumentException("Expected an in-box anchor and intensity in [0, 1]");
    }
  }

  public void reset() {
    this.integer = null;
    this.rotationData7 = new RotationVector(0.0, 0.0, 0.0);
    this.rotationData72 = this.rotationData7;
    this.count = 0;
    this.count2 = 0;
  }

  private double calculateValue() {
    return Math.max(-1.0, Math.min(1.0, this.random.nextGaussian() * 0.55));
  }

  private static double calculateValue2(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue) {
    double sourceDoubleValue =
        currentDoubleValue >= 0.0
            ? previousDoubleValue - doubleValue
            : doubleValue - nextDoubleValue;
    double targetDoubleValue =
        currentDoubleValue
            * Math.min(previousDoubleValue - nextDoubleValue, sourceDoubleValue / 0.2);
    return Math.max(
        nextDoubleValue, Math.min(previousDoubleValue, doubleValue + targetDoubleValue));
  }
}
