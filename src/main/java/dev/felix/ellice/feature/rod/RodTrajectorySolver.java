package dev.felix.ellice.feature.rod;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Optional;
import java.util.function.DoubleFunction;

public final class RodTrajectorySolver {
  private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
  public static final int HORIZON = 24;

  private RodTrajectorySolver() {}

  public static RodTrajectorySolver.Launch launch(
      RodTrajectorySolver.Kind kind,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationData rotationData) {
    double doubleValue = Math.toRadians(rotationData.yaw());
    double currentDoubleValue = Math.toRadians(rotationData.pitch());
    if (kind == RodTrajectorySolver.Kind.ROD) {
      RotationVector nextRotationVector =
          new RotationVector(
              -Math.sin(doubleValue),
              Math.clamp(-Math.tan(currentDoubleValue), -5.0, 5.0),
              Math.cos(doubleValue));
      return new RodTrajectorySolver.Launch(
          rotationVector.add(
              new RotationVector(-Math.sin(doubleValue) * 0.3, 0.0, Math.cos(doubleValue) * 0.3)),
          nextRotationVector.multiply(0.6 / nextRotationVector.length() + 0.5));
    } else {
      return new RodTrajectorySolver.Launch(
          rotationVector.add(new RotationVector(0.0, -0.10000000149011612, 0.0)),
          rotationData.direction().multiply(1.5).add(currentRotationVector));
    }
  }

  public static RotationVector position(
      RodTrajectorySolver.Kind kind, RodTrajectorySolver.Launch launch, double width) {
    if (Double.isFinite(width) && !(width < 0.0) && !(width > 26.0)) {
      RotationVector rotationVector = launch.origin();
      RotationVector currentRotationVector = launch.velocity();

      for (int index = 0; index < Math.ceil(width); index++) {
        currentRotationVector = currentRotationVector.add(new RotationVector(0.0, -0.03, 0.0));
        if (kind != RodTrajectorySolver.Kind.ROD) {
          currentRotationVector = currentRotationVector.multiply(0.9900000095367432);
        }

        rotationVector =
            rotationVector.add(currentRotationVector.multiply(Math.min(1.0, width - index)));
        if (kind == RodTrajectorySolver.Kind.ROD) {
          currentRotationVector = currentRotationVector.multiply(0.92);
        }
      }

      return rotationVector;
    } else {
      throw new IllegalArgumentException("Flight time");
    }
  }

  public static Optional<RodTrajectorySolver.Solution> solve(
      RodTrajectorySolver.Kind kind,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      DoubleFunction<RotationVector> doubleFunction) {
    double doubleValue = 0.05;
    double currentDoubleValue =
        calculateValue(
            kind,
            rotationVector,
            currentRotationVector,
            (RotationVector) doubleFunction.apply(0.05),
            0.05);

    for (double nextDoubleValue = 0.25; nextDoubleValue <= 24.0; nextDoubleValue += 0.25) {
      double previousDoubleValue =
          calculateValue(
              kind,
              rotationVector,
              currentRotationVector,
              (RotationVector) doubleFunction.apply(nextDoubleValue),
              nextDoubleValue);
      if (currentDoubleValue > 0.0 && previousDoubleValue <= 0.0) {
        double sourceDoubleValue = doubleValue;
        double targetDoubleValue = nextDoubleValue;

        for (int index = 0; index < 24; index++) {
          double inputDoubleValue = (sourceDoubleValue + targetDoubleValue) * 0.5;
          if (calculateValue(
                  kind,
                  rotationVector,
                  currentRotationVector,
                  (RotationVector) doubleFunction.apply(inputDoubleValue),
                  inputDoubleValue)
              > 0.0) {
            sourceDoubleValue = inputDoubleValue;
          } else {
            targetDoubleValue = inputDoubleValue;
          }
        }

        double outputDoubleValue = (sourceDoubleValue + targetDoubleValue) * 0.5;
        RotationVector nextRotationVector =
            createRotationData7(
                kind,
                rotationVector,
                currentRotationVector,
                (RotationVector) doubleFunction.apply(outputDoubleValue),
                outputDoubleValue);
        RotationData rotationData = RotationData.lookAt(rotationData7, nextRotationVector);
        if (kind == RodTrajectorySolver.Kind.ROD && Math.abs(rotationData.pitch()) > 78.0) {
          return Optional.empty();
        }

        return Optional.of(new RodTrajectorySolver.Solution(rotationData, outputDoubleValue));
      }

      doubleValue = nextDoubleValue;
      currentDoubleValue = previousDoubleValue;
    }

    return Optional.empty();
  }

  private static double calculateValue(
      RodTrajectorySolver.Kind kind,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue) {
    RotationVector previousRotationVector =
        createRotationData7(
            kind, rotationVector, currentRotationVector, nextRotationVector, doubleValue);
    double currentX = Math.hypot(previousRotationVector.x(), previousRotationVector.z());
    double currentLength =
        kind == RodTrajectorySolver.Kind.ROD
            ? 0.6 + 0.5 * previousRotationVector.length() / Math.max(currentX, 1.0E-9)
            : 1.5;
    return previousRotationVector.length() - currentLength;
  }

  private static RotationVector createRotationData7(
      RodTrajectorySolver.Kind kind,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      RotationVector nextRotationVector,
      double doubleValue) {
    RotationVector previousRotationVector;
    if (kind == RodTrajectorySolver.Kind.ROD) {
      RotationVector sourceRotationVector = nextRotationVector.subtract(rotationVector);
      double currentX = Math.hypot(sourceRotationVector.x(), sourceRotationVector.z());
      previousRotationVector =
          rotationVector.add(
              new RotationVector(sourceRotationVector.x(), 0.0, sourceRotationVector.z())
                  .multiply(0.3 / Math.max(currentX, 1.0E-9)));
    } else {
      previousRotationVector =
          rotationVector.add(new RotationVector(0.0, -0.10000000149011612, 0.0));
    }

    double currentDoubleValue = kind == RodTrajectorySolver.Kind.ROD ? 0.92 : 0.9900000095367432;
    int value = (int) doubleValue;
    double nextDoubleValue =
        (1.0 - Math.pow(currentDoubleValue, value)) / (1.0 - currentDoubleValue)
            + (doubleValue - value) * Math.pow(currentDoubleValue, value);
    if (kind != RodTrajectorySolver.Kind.ROD) {
      nextDoubleValue *= currentDoubleValue;
    }

    double currentY =
        position(kind, new RodTrajectorySolver.Launch(rotationData7, rotationData7), doubleValue)
            .y();
    RotationVector targetRotationVector =
        nextRotationVector
            .subtract(previousRotationVector)
            .subtract(new RotationVector(0.0, currentY, 0.0))
            .multiply(1.0 / nextDoubleValue);
    return kind == RodTrajectorySolver.Kind.ROD
        ? targetRotationVector
        : targetRotationVector.subtract(currentRotationVector);
  }

  public enum Kind {
    ROD,
    SNOWBALL,
    EGG;

    private static RodTrajectorySolver.Kind[] $values() {
      return new RodTrajectorySolver.Kind[] {ROD, SNOWBALL, EGG};
    }
  }

  public record Launch(RotationVector origin, RotationVector velocity) {}

  public record Solution(RotationData rotation, double ticks) {}
}
