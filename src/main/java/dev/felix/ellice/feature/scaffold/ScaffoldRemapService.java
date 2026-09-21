package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import java.util.Objects;

public final class ScaffoldRemapService {
  private static final int[][] int2 =
      new int[][] {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
  private static final double value = 1.0E-12;

  public ScaffoldRemapService.Result remap(
      double doubleValue, double currentDoubleValue, ScaffoldRemapService.Input input) {
    return this.remap(doubleValue, currentDoubleValue, input, false, true);
  }

  public ScaffoldRemapService.Result remap(
      double doubleValue,
      double currentDoubleValue,
      ScaffoldRemapService.Input input,
      boolean enabled) {
    return this.remap(doubleValue, currentDoubleValue, input, enabled, true);
  }

  public ScaffoldRemapService.Result remap(
      double doubleValue,
      double currentDoubleValue,
      ScaffoldRemapService.Input input,
      boolean enabled,
      boolean currentEnabled) {
    if (Double.isFinite(doubleValue) && Double.isFinite(currentDoubleValue)) {
      Objects.requireNonNull(input, "original");
      int value = input.forwardAxis();
      int currentValue = input.leftAxis();
      int nextValue = !input.sneak() && !enabled ? 0 : 1;
      if (value == 0 && currentValue == 0) {
        ScaffoldRemapService.Input currentInput =
            new ScaffoldRemapService.Input(
                false, false, false, false, input.jump(), (nextValue != 0), false);
        ScaffoldRemapService.WorldVector currentWorldVector =
            new ScaffoldRemapService.WorldVector(0.0, 0.0);
        double nextDoubleValue = RotationData.wrapDegrees(doubleValue);
        return new ScaffoldRemapService.Result(
            currentInput,
            currentWorldVector,
            currentWorldVector,
            false,
            nextDoubleValue,
            nextDoubleValue,
            0.0,
            0.0);
      }

      ScaffoldRemapService.WorldVector nextWorldVector =
          worldVector(doubleValue, value, currentValue);
      int previousValue = 0;
      int sourceValue = 0;
      ScaffoldRemapService.WorldVector previousWorldVector = null;
      double previousDoubleValue = Double.NEGATIVE_INFINITY;
      int targetValue = Integer.MAX_VALUE;

      for (int[] ints : int2) {
        int inputValue = ints[0];
        int outputValue = ints[1];
        ScaffoldRemapService.WorldVector sourceWorldVector =
            worldVector(currentDoubleValue, inputValue, outputValue);
        double sourceDoubleValue = nextWorldVector.dot(sourceWorldVector);
        int resultValue = Math.abs(inputValue) + Math.abs(outputValue);
        if (sourceDoubleValue > previousDoubleValue + 1.0E-12
            || Math.abs(sourceDoubleValue - previousDoubleValue) <= 1.0E-12
                && resultValue < targetValue) {
          previousDoubleValue = sourceDoubleValue;
          targetValue = resultValue;
          previousValue = inputValue;
          sourceValue = outputValue;
          previousWorldVector = sourceWorldVector;
        }
      }

      int candidateValue = previousValue > 0 ? 1 : 0;
      ScaffoldRemapService.Input nextInput =
          new ScaffoldRemapService.Input(
              (candidateValue != 0),
              previousValue < 0,
              sourceValue > 0,
              sourceValue < 0,
              input.jump(),
              (nextValue != 0),
              input.sprint() && (!currentEnabled || candidateValue != 0 && nextValue == 0));
      double targetDoubleValue = calculateValue(nextWorldVector);
      double inputDoubleValue = calculateValue(previousWorldVector);
      double outputDoubleValue = RotationData.wrapDegrees(inputDoubleValue - targetDoubleValue);
      return new ScaffoldRemapService.Result(
          nextInput,
          nextWorldVector,
          previousWorldVector,
          true,
          targetDoubleValue,
          inputDoubleValue,
          outputDoubleValue,
          Math.abs(outputDoubleValue));
    } else {
      throw new IllegalArgumentException("Rotation yaws must be finite");
    }
  }

  public ScaffoldRemapService.Result passthrough(
      double doubleValue,
      double currentDoubleValue,
      ScaffoldRemapService.Input input,
      boolean enabled,
      boolean currentEnabled) {
    if (Double.isFinite(doubleValue) && Double.isFinite(currentDoubleValue)) {
      Objects.requireNonNull(input, "original");
      int value = input.forwardAxis();
      int currentValue = input.leftAxis();
      int nextValue = !input.sneak() && !enabled ? 0 : 1;
      int previousValue = value == 0 && currentValue == 0 ? 0 : 1;
      int sourceValue = value > 0 && nextValue == 0 ? 1 : 0;
      ScaffoldRemapService.Input currentInput =
          new ScaffoldRemapService.Input(
              value > 0,
              value < 0,
              currentValue > 0,
              currentValue < 0,
              input.jump(),
              (nextValue != 0),
              input.sprint() && (!currentEnabled || sourceValue != 0));
      if (previousValue == 0) {
        ScaffoldRemapService.WorldVector currentWorldVector =
            new ScaffoldRemapService.WorldVector(0.0, 0.0);
        double nextDoubleValue = RotationData.wrapDegrees(doubleValue);
        return new ScaffoldRemapService.Result(
            currentInput,
            currentWorldVector,
            currentWorldVector,
            false,
            nextDoubleValue,
            nextDoubleValue,
            0.0,
            0.0);
      } else {
        ScaffoldRemapService.WorldVector nextWorldVector =
            worldVector(doubleValue, value, currentValue);
        ScaffoldRemapService.WorldVector previousWorldVector =
            worldVector(currentDoubleValue, value, currentValue);
        double previousDoubleValue = calculateValue(nextWorldVector);
        double sourceDoubleValue = calculateValue(previousWorldVector);
        double targetDoubleValue =
            RotationData.wrapDegrees(sourceDoubleValue - previousDoubleValue);
        return new ScaffoldRemapService.Result(
            currentInput,
            nextWorldVector,
            previousWorldVector,
            true,
            previousDoubleValue,
            sourceDoubleValue,
            targetDoubleValue,
            Math.abs(targetDoubleValue));
      }
    } else {
      throw new IllegalArgumentException("Rotation yaws must be finite");
    }
  }

  public ScaffoldRemapService.Input steerToward(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      ScaffoldRemapService.Input input) {
    if (Double.isFinite(doubleValue)
        && Double.isFinite(currentDoubleValue)
        && Double.isFinite(nextDoubleValue)) {
      Objects.requireNonNull(input, "original");
      double previousDoubleValue = Math.hypot(currentDoubleValue, nextDoubleValue);
      if (previousDoubleValue < 1.0E-9) {
        return new ScaffoldRemapService.Input(
            false, false, false, false, input.jump(), input.sneak(), false);
      }

      ScaffoldRemapService.WorldVector currentWorldVector =
          new ScaffoldRemapService.WorldVector(
              currentDoubleValue / previousDoubleValue, nextDoubleValue / previousDoubleValue);
      int value = 0;
      int currentValue = 0;
      double sourceDoubleValue = Double.NEGATIVE_INFINITY;
      int nextValue = Integer.MAX_VALUE;

      for (int[] ints : int2) {
        ScaffoldRemapService.WorldVector nextWorldVector =
            worldVector(doubleValue, ints[0], ints[1]);
        double targetDoubleValue = currentWorldVector.dot(nextWorldVector);
        int previousValue = Math.abs(ints[0]) + Math.abs(ints[1]);
        if (targetDoubleValue > sourceDoubleValue + 1.0E-12
            || Math.abs(targetDoubleValue - sourceDoubleValue) <= 1.0E-12
                && previousValue < nextValue) {
          sourceDoubleValue = targetDoubleValue;
          nextValue = previousValue;
          value = ints[0];
          currentValue = ints[1];
        }
      }

      return new ScaffoldRemapService.Input(
          value > 0,
          value < 0,
          currentValue > 0,
          currentValue < 0,
          input.jump(),
          input.sneak(),
          false);
    } else {
      throw new IllegalArgumentException("Steering vector must be finite");
    }
  }

  public static ScaffoldRemapService.WorldVector worldVector(
      double doubleValue, int value, int currentValue) {
    if (!Double.isFinite(doubleValue)) {
      throw new IllegalArgumentException("Yaw must be finite");
    }

    if (value >= -1 && value <= 1 && currentValue >= -1 && currentValue <= 1) {
      double currentDoubleValue = Math.hypot(value, currentValue);
      if (currentDoubleValue == 0.0) {
        return new ScaffoldRemapService.WorldVector(0.0, 0.0);
      }

      double nextDoubleValue = value / currentDoubleValue;
      double previousDoubleValue = currentValue / currentDoubleValue;
      double sourceDoubleValue = Math.toRadians(doubleValue);
      double targetDoubleValue = Math.sin(sourceDoubleValue);
      double inputDoubleValue = Math.cos(sourceDoubleValue);
      return new ScaffoldRemapService.WorldVector(
          previousDoubleValue * inputDoubleValue - nextDoubleValue * targetDoubleValue,
          nextDoubleValue * inputDoubleValue + previousDoubleValue * targetDoubleValue);
    } else {
      throw new IllegalArgumentException("Input axes must be in [-1, 1]");
    }
  }

  private static double calculateValue(ScaffoldRemapService.WorldVector worldVector) {
    return RotationData.wrapDegrees(
        Math.toDegrees(Math.atan2(worldVector.z(), worldVector.x())) - 90.0);
  }

  public record Input(
      boolean forward,
      boolean backward,
      boolean left,
      boolean right,
      boolean jump,
      boolean sneak,
      boolean sprint) {
    public int forwardAxis() {
      return (this.forward ? 1 : 0) - (this.backward ? 1 : 0);
    }

    public int leftAxis() {
      return (this.left ? 1 : 0) - (this.right ? 1 : 0);
    }

    public boolean hasHorizontalIntent() {
      return this.forwardAxis() != 0 || this.leftAxis() != 0;
    }
  }

  public record Result(
      ScaffoldRemapService.Input serverInput,
      ScaffoldRemapService.WorldVector intendedWorldVector,
      ScaffoldRemapService.WorldVector serverWorldVector,
      boolean moving,
      double intendedWorldYaw,
      double serverWorldYaw,
      double signedAngularErrorDegrees,
      double angularErrorDegrees) {
    public Result(
        ScaffoldRemapService.Input serverInput,
        ScaffoldRemapService.WorldVector intendedWorldVector,
        ScaffoldRemapService.WorldVector serverWorldVector,
        boolean moving,
        double intendedWorldYaw,
        double serverWorldYaw,
        double signedAngularErrorDegrees,
        double angularErrorDegrees) {
      Objects.requireNonNull(serverInput, "serverInput");
      Objects.requireNonNull(intendedWorldVector, "intendedWorldVector");
      Objects.requireNonNull(serverWorldVector, "serverWorldVector");
      if (Double.isFinite(intendedWorldYaw)
          && Double.isFinite(serverWorldYaw)
          && Double.isFinite(signedAngularErrorDegrees)
          && Double.isFinite(angularErrorDegrees)
          && !(angularErrorDegrees < 0.0)
          && !(angularErrorDegrees > 180.000000000001)) {
        this.serverInput = serverInput;
        this.intendedWorldVector = intendedWorldVector;
        this.serverWorldVector = serverWorldVector;
        this.moving = moving;
        this.intendedWorldYaw = intendedWorldYaw;
        this.serverWorldYaw = serverWorldYaw;
        this.signedAngularErrorDegrees = signedAngularErrorDegrees;
        this.angularErrorDegrees = angularErrorDegrees;
      } else {
        throw new IllegalArgumentException("Invalid direction metrics");
      }
    }
  }

  public record WorldVector(double x, double z) {
    public WorldVector(double x, double z) {
      if (Double.isFinite(x) && Double.isFinite(z)) {
        this.x = x;
        this.z = z;
      } else {
        throw new IllegalArgumentException("World-vector components must be finite");
      }
    }

    public double length() {
      return Math.hypot(this.x, this.z);
    }

    public double dot(ScaffoldRemapService.WorldVector worldVector) {
      Objects.requireNonNull(worldVector, "other");
      return this.x * worldVector.x + this.z * worldVector.z;
    }
  }
}
