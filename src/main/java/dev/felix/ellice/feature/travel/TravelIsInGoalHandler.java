package dev.felix.ellice.feature.travel;

import java.util.List;
import java.util.Objects;

public interface TravelIsInGoalHandler {
  boolean isInGoal(int value, double doubleValue, int currentValue);

  double heuristic(int value, double doubleValue, int currentValue);

  record BlockGoal(int gx, int gy, int gz) implements TravelIsInGoalHandler {
    @Override
    public boolean isInGoal(int value, double doubleValue, int currentValue) {
      return value == this.gx && currentValue == this.gz && Math.abs(doubleValue - this.gy) < 0.6;
    }

    @Override
    public double heuristic(int value, double doubleValue, int currentValue) {
      double currentDoubleValue = Math.abs(value - this.gx);
      double nextDoubleValue = Math.abs(currentValue - this.gz);
      double previousDoubleValue = Math.abs(doubleValue - this.gy) * 0.5;
      return Math.max(currentDoubleValue, nextDoubleValue)
          + 0.41 * Math.min(currentDoubleValue, nextDoubleValue)
          + previousDoubleValue * 4.63284688441047 * 0.12;
    }
  }

  record CompositeGoal(List<TravelIsInGoalHandler> goals) implements TravelIsInGoalHandler {
    public CompositeGoal(List<TravelIsInGoalHandler> goals) {
      goals = List.copyOf(Objects.requireNonNull(goals, "goals"));
      this.goals = goals;
    }

    @Override
    public boolean isInGoal(int value, double doubleValue, int currentValue) {
      for (TravelIsInGoalHandler travelIsInGoal : this.goals) {
        if (travelIsInGoal.isInGoal(value, doubleValue, currentValue)) {
          return true;
        }
      }

      return false;
    }

    @Override
    public double heuristic(int value, double doubleValue, int currentValue) {
      double currentDoubleValue = Double.POSITIVE_INFINITY;

      for (TravelIsInGoalHandler travelIsInGoal : this.goals) {
        currentDoubleValue =
            Math.min(
                currentDoubleValue, travelIsInGoal.heuristic(value, doubleValue, currentValue));
      }

      return currentDoubleValue;
    }
  }

  record NearGoal(int gx, double gy, int gz, double radius) implements TravelIsInGoalHandler {
    public NearGoal(int gx, double gy, int gz, double radius) {
      if (Double.isFinite(radius) && !(radius < 0.0) && !(radius > 64.0)) {
        this.gx = gx;
        this.gy = gy;
        this.gz = gz;
        this.radius = radius;
      } else {
        throw new IllegalArgumentException("Invalid radius");
      }
    }

    @Override
    public boolean isInGoal(int value, double doubleValue, int currentValue) {
      double currentDoubleValue = value + 0.5 - (this.gx + 0.5);
      double nextDoubleValue = currentValue + 0.5 - (this.gz + 0.5);
      return Math.hypot(currentDoubleValue, nextDoubleValue) <= this.radius
          && Math.abs(doubleValue - this.gy) < 2.0;
    }

    @Override
    public double heuristic(int value, double doubleValue, int currentValue) {
      double currentDoubleValue = Math.abs(value - this.gx);
      double nextDoubleValue = Math.abs(currentValue - this.gz);
      double previousDoubleValue =
          Math.max(0.0, Math.hypot(currentDoubleValue, nextDoubleValue) - this.radius);
      return previousDoubleValue * 4.63284688441047;
    }
  }

  record XZGoal(int gx, int gz) implements TravelIsInGoalHandler {
    @Override
    public boolean isInGoal(int value, double doubleValue, int currentValue) {
      return value == this.gx && currentValue == this.gz;
    }

    @Override
    public double heuristic(int value, double doubleValue, int currentValue) {
      double currentDoubleValue = Math.abs(value - this.gx);
      double nextDoubleValue = Math.abs(currentValue - this.gz);
      return (Math.max(currentDoubleValue, nextDoubleValue)
              + 0.41 * Math.min(currentDoubleValue, nextDoubleValue))
          * 4.63284688441047;
    }
  }
}
