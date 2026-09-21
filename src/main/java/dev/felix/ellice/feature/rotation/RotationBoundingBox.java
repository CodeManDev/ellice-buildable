package dev.felix.ellice.feature.rotation;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public record RotationBoundingBox(
    double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
  private static final double EPSILON = 1.0E-12;
  private static final double[] HORIZONTAL_AIM_FRACTIONS =
      new double[] {0.08, 0.29, 0.5, 0.71, 0.92};
  private static final double[] VERTICAL_AIM_FRACTIONS =
      new double[] {0.06, 0.2, 0.35, 0.5, 0.65, 0.8, 0.94};

  public RotationBoundingBox(
      double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    if (Double.isFinite(minX)
        && Double.isFinite(minY)
        && Double.isFinite(minZ)
        && Double.isFinite(maxX)
        && Double.isFinite(maxY)
        && Double.isFinite(maxZ)
        && !(minX > maxX)
        && !(minY > maxY)
        && !(minZ > maxZ)) {
      this.minX = minX;
      this.minY = minY;
      this.minZ = minZ;
      this.maxX = maxX;
      this.maxY = maxY;
      this.maxZ = maxZ;
    } else {
      throw new IllegalArgumentException("Invalid hitbox bounds");
    }
  }

  public RotationVector sample(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return new RotationVector(
        calculateValue(this.minX, this.maxX, doubleValue),
        calculateValue(this.minY, this.maxY, currentDoubleValue),
        calculateValue(this.minZ, this.maxZ, nextDoubleValue));
  }

  public List<RotationVector> aimSamples() {
    ArrayList currentLength =
        new ArrayList(
            HORIZONTAL_AIM_FRACTIONS.length
                * VERTICAL_AIM_FRACTIONS.length
                * HORIZONTAL_AIM_FRACTIONS.length);

    for (double doubleValue : HORIZONTAL_AIM_FRACTIONS) {
      for (double currentDoubleValue : VERTICAL_AIM_FRACTIONS) {
        for (double nextDoubleValue : HORIZONTAL_AIM_FRACTIONS) {
          currentLength.add(this.sample(doubleValue, currentDoubleValue, nextDoubleValue));
        }
      }
    }

    return List.copyOf(currentLength);
  }

  public boolean contains(RotationVector rotationVector) {
    return rotationVector.x() >= this.minX - 1.0E-12
        && rotationVector.x() <= this.maxX + 1.0E-12
        && rotationVector.y() >= this.minY - 1.0E-12
        && rotationVector.y() <= this.maxY + 1.0E-12
        && rotationVector.z() >= this.minZ - 1.0E-12
        && rotationVector.z() <= this.maxZ + 1.0E-12;
  }

  public double visibleAngularWidth(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      List<RotationVector> items) {
    if (this.contains(currentRotationVector)
        && !items.isEmpty()
        && !items.stream().anyMatch(item -> !this.contains(item))) {
      List currentItems = this.collectValues(currentRotationVector, items);
      RotationData rotationData = RotationData.lookAt(rotationVector, currentRotationVector);
      double doubleValue = 0.0;
      double currentDoubleValue = 0.0;
      double nextDoubleValue = 0.0;
      double previousDoubleValue = 0.0;

      for (RotationVector nextRotationVector :
          (Iterable<RotationVector>) (Iterable<?>) (currentItems)) {
        RotationData currentRotationData = RotationData.lookAt(rotationVector, nextRotationVector);
        double sourceDoubleValue =
            RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw());
        double targetDoubleValue = currentRotationData.pitch() - rotationData.pitch();
        doubleValue = Math.min(doubleValue, sourceDoubleValue);
        currentDoubleValue = Math.max(currentDoubleValue, sourceDoubleValue);
        nextDoubleValue = Math.min(nextDoubleValue, targetDoubleValue);
        previousDoubleValue = Math.max(previousDoubleValue, targetDoubleValue);
      }

      RotationVector previousRotationVector = this.sample(0.5, 0.5, 0.5);
      double currentLength =
          Math.max(0.01, previousRotationVector.subtract(rotationVector).length());
      double inputDoubleValue = Math.max(this.maxX - this.minX, this.maxZ - this.minZ);
      double outputDoubleValue = this.maxY - this.minY;
      double resultDoubleValue = calculateValue2(inputDoubleValue, currentLength);
      double candidateDoubleValue = calculateValue2(outputDoubleValue, currentLength);
      double nextLength =
          currentDoubleValue - doubleValue + resultDoubleValue / HORIZONTAL_AIM_FRACTIONS.length;
      double previousLength =
          previousDoubleValue
              - nextDoubleValue
              + candidateDoubleValue / VERTICAL_AIM_FRACTIONS.length;
      double selectedDoubleValue =
          Math.min(
              Math.min(resultDoubleValue, nextLength),
              Math.min(candidateDoubleValue, previousLength));
      return Math.max(0.1, Math.min(45.0, selectedDoubleValue));
    } else {
      throw new IllegalArgumentException("Visible points must lie inside the hitbox");
    }
  }

  private List<RotationVector> collectValues(
      RotationVector rotationVector, List<RotationVector> items) {
    int index = 0;
    double doubleValue = ((RotationVector) items.get(0)).subtract(rotationVector).lengthSquared();

    for (int currentIndex = 1; currentIndex < items.size(); currentIndex++) {
      double currentDoubleValue =
          ((RotationVector) items.get(currentIndex)).subtract(rotationVector).lengthSquared();
      if (currentDoubleValue < doubleValue) {
        index = currentIndex;
        doubleValue = currentDoubleValue;
      }
    }

    boolean[] currentSize = new boolean[items.size()];
    int[] nextSize = new int[items.size()];
    int nextIndex = 0;
    int previousIndex = 0;
    currentSize[index] = true;
    nextSize[previousIndex++] = index;

    while (nextIndex < previousIndex) {
      RotationVector currentRotationVector = (RotationVector) items.get(nextSize[nextIndex++]);

      for (int sourceIndex = 0; sourceIndex < items.size(); sourceIndex++) {
        if (!currentSize[sourceIndex]
            && this.checkCondition(
                currentRotationVector, (RotationVector) items.get(sourceIndex))) {
          currentSize[sourceIndex] = true;
          nextSize[previousIndex++] = sourceIndex;
        }
      }
    }

    ArrayList arrayList = new ArrayList(previousIndex);

    for (int targetIndex = 0; targetIndex < currentSize.length; targetIndex++) {
      if (currentSize[targetIndex]) {
        arrayList.add((RotationVector) items.get(targetIndex));
      }
    }

    return arrayList;
  }

  private boolean checkCondition(
      RotationVector rotationVector, RotationVector currentRotationVector) {
    double doubleValue = Math.max(1.0E-12, this.maxX - this.minX);
    double currentDoubleValue = Math.max(1.0E-12, this.maxY - this.minY);
    double nextDoubleValue = Math.max(1.0E-12, this.maxZ - this.minZ);
    double currentX = Math.abs(rotationVector.x() - currentRotationVector.x()) / doubleValue;
    double currentY = Math.abs(rotationVector.y() - currentRotationVector.y()) / currentDoubleValue;
    double previousDoubleValue =
        Math.abs(rotationVector.z() - currentRotationVector.z()) / nextDoubleValue;
    return currentX <= 0.215
        && currentY <= 0.155
        && previousDoubleValue <= 0.215
        && currentX + currentY + previousDoubleValue > 1.0E-12;
  }

  public OptionalDouble rayIntersection(
      RotationVector rotationVector, RotationVector currentRotationVector, double doubleValue) {
    if (Double.isFinite(doubleValue) && !(doubleValue < 0.0)) {
      double currentLength = currentRotationVector.length();
      if (Math.abs(currentLength - 1.0) > 1.0E-8) {
        throw new IllegalArgumentException("Ray direction must be normalized");
      }

      double currentDoubleValue = 0.0;
      double nextDoubleValue = doubleValue;
      double[] currentX = new double[] {rotationVector.x(), rotationVector.y(), rotationVector.z()};
      double[] nextX =
          new double[] {
            currentRotationVector.x(), currentRotationVector.y(), currentRotationVector.z()
          };
      double[] doubles = new double[] {this.minX, this.minY, this.minZ};
      double[] currentDoubles = new double[] {this.maxX, this.maxY, this.maxZ};

      for (int index = 0; index < 3; index++) {
        double previousDoubleValue = nextX[index];
        if (Math.abs(previousDoubleValue) < 1.0E-12) {
          if (currentX[index] < doubles[index] || currentX[index] > currentDoubles[index]) {
            return OptionalDouble.empty();
          }
        } else {
          double sourceDoubleValue = (doubles[index] - currentX[index]) / previousDoubleValue;
          double targetDoubleValue =
              (currentDoubles[index] - currentX[index]) / previousDoubleValue;
          if (sourceDoubleValue > targetDoubleValue) {
            double inputDoubleValue = sourceDoubleValue;
            sourceDoubleValue = targetDoubleValue;
            targetDoubleValue = inputDoubleValue;
          }

          currentDoubleValue = Math.max(currentDoubleValue, sourceDoubleValue);
          nextDoubleValue = Math.min(nextDoubleValue, targetDoubleValue);
          if (currentDoubleValue > nextDoubleValue) {
            return OptionalDouble.empty();
          }
        }
      }

      return currentDoubleValue <= doubleValue
          ? OptionalDouble.of(currentDoubleValue)
          : OptionalDouble.empty();
    } else {
      throw new IllegalArgumentException("maxDistance must be finite and non-negative");
    }
  }

  private static double calculateValue(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    if (Double.isFinite(nextDoubleValue) && !(nextDoubleValue < 0.0) && !(nextDoubleValue > 1.0)) {
      return doubleValue + (currentDoubleValue - doubleValue) * nextDoubleValue;
    } else {
      throw new IllegalArgumentException("Sample fractions must be in [0, 1]");
    }
  }

  private static double calculateValue2(double doubleValue, double currentDoubleValue) {
    return Math.toDegrees(2.0 * Math.atan2(doubleValue * 0.5, currentDoubleValue));
  }
}
