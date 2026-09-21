package dev.felix.ellice.feature.bow;

import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.List;
import java.util.function.DoubleFunction;

public final class BowCoverageCalculator {
  public static final double SPREAD = 0.0172275;
  private static final int count = 96;
  private static final RotationVector[] rotationData7 = new RotationVector[96];
  private static final double[][] double2 = new double[96][2];

  private BowCoverageCalculator() {}

  public static double coverage(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue,
      RotationBoundingBox rotationBoundingBox,
      DoubleFunction<RotationVector> doubleFunction,
      int value) {
    return coverage(
        rotationVector,
        currentRotationVector,
        nextRotationVector,
        doubleValue,
        List.of(
            new BowCoverageCalculator.Body(
                1.0,
                rotationBoundingBox,
                doubleFunction,
                new BowObserveService.Error(0.0, 0.0, 0.0),
                1.0,
                0.0)),
        value);
  }

  public static double coverage(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue,
      List<BowCoverageCalculator.Body> items,
      int value) {
    return calculateValue(
        rotationVector, currentRotationVector, nextRotationVector, doubleValue, items, value, 96);
  }

  public static double rankCoverage(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue,
      List<BowCoverageCalculator.Body> items,
      int value) {
    return calculateValue(
        rotationVector, currentRotationVector, nextRotationVector, doubleValue, items, value, 16);
  }

  private static double calculateValue(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue,
      List<BowCoverageCalculator.Body> items,
      int value,
      int currentValue) {
    double currentDoubleValue = 0.0;

    for (BowCoverageCalculator.Body body : items) {
      if (!(body.weight < 0.005)) {
        currentDoubleValue +=
            body.weight
                * calculateValue2(
                    rotationVector,
                    currentRotationVector,
                    nextRotationVector,
                    doubleValue,
                    body,
                    value,
                    currentValue);
      }
    }

    return currentDoubleValue;
  }

  private static double calculateValue2(
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue,
      BowCoverageCalculator.Body body,
      int value,
      int currentValue) {
    int nextValue = Math.max(1, Math.min(80, value));
    RotationVector[] rotationVectors = new RotationVector[nextValue + 1];
    double[] doubles = new double[nextValue + 1];

    for (int index = 0; index <= nextValue; index++) {
      rotationVectors[index] = body.offset.apply(index);
      doubles[index] = Math.min(1.5, (index + body.age) / Math.max(1.0, body.errorHorizon));
    }

    int currentIndex = 0;

    for (int nextIndex = 0; nextIndex < currentValue; nextIndex++) {
      int previousIndex = nextIndex * 96 / currentValue;
      RotationVector previousRotationVector =
          body.error.sample(double2[previousIndex][0], double2[previousIndex][1]);
      RotationVector sourceRotationVector = rotationVector;
      RotationVector targetRotationVector =
          currentRotationVector
              .add(rotationData7[previousIndex])
              .multiply(doubleValue)
              .add(nextRotationVector);

      for (int sourceIndex = 1; sourceIndex <= nextValue; sourceIndex++) {
        RotationVector inputRotationVector = sourceRotationVector.add(targetRotationVector);
        if (BowTrajectorySolver.hitFraction(
                sourceRotationVector,
                inputRotationVector,
                body.box,
                rotationVectors[sourceIndex - 1].add(
                    previousRotationVector.multiply(doubles[sourceIndex - 1])),
                rotationVectors[sourceIndex].add(
                    previousRotationVector.multiply(doubles[sourceIndex])))
            .isPresent()) {
          currentIndex++;
          break;
        }

        sourceRotationVector = inputRotationVector;
        targetRotationVector =
            targetRotationVector
                .multiply(0.9900000095367432)
                .add(new RotationVector(0.0, -0.05, 0.0));
      }
    }

    return (double) currentIndex / currentValue;
  }

  private static double calculateValue3(int value, int currentValue) {
    double doubleValue = 0.0;
    double currentDoubleValue = 1.0;

    while (value > 0) {
      currentDoubleValue /= currentValue;
      doubleValue += currentDoubleValue * (value % currentValue);
      value /= currentValue;
    }

    return doubleValue;
  }

  static {
    for (int sampleIndex = 0; sampleIndex < 96; sampleIndex++) {
      int sequenceIndex = sampleIndex + 1;
      rotationData7[sampleIndex] =
          new RotationVector(
                  calculateValue3(sequenceIndex, 2) - calculateValue3(sequenceIndex, 3),
                  calculateValue3(sequenceIndex, 5) - calculateValue3(sequenceIndex, 7),
                  calculateValue3(sequenceIndex, 11) - calculateValue3(sequenceIndex, 13))
              .multiply(0.0172275);
      double radialDistance =
          Math.sqrt(-2.0 * Math.log(Math.max(1.0E-6, calculateValue3(sequenceIndex, 17))));
      double angle = Math.PI * 2.0 * calculateValue3(sequenceIndex, 19);
      double2[sampleIndex][0] = radialDistance * Math.cos(angle);
      double2[sampleIndex][1] = radialDistance * Math.sin(angle);
    }
  }

  public record Body(
      double weight,
      RotationBoundingBox box,
      DoubleFunction<RotationVector> offset,
      BowObserveService.Error error,
      double errorHorizon,
      double age) {}
}
