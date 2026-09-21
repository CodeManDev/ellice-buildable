package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public final class PlacementRuleEvaluator {
  private PlacementRuleEvaluator() {}

  public static boolean allows(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.WorldVector worldVector,
      int value,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      ContinuousSupportChecker.Query query) {
    Objects.requireNonNull(scaffoldPlayerSnapshot, "frame");
    Objects.requireNonNull(worldVector, "appliedDirection");
    Objects.requireNonNull(query, "confirmedSupport");
    if (Double.isFinite(currentDoubleValue)
        && !(currentDoubleValue < 1.0)
        && !(currentDoubleValue > 32.0)
        && Double.isFinite(doubleValue)
        && !(doubleValue <= 0.0)
        && !(doubleValue > 0.3)) {
      if (scaffoldPlayerSnapshot.onGround()
          && !(Math.abs(scaffoldPlayerSnapshot.feetPosition().y() - (value + 1.0)) > 1.0E-4)
          && Double.isFinite(nextDoubleValue)
          && !(nextDoubleValue < 0.0)) {
        RotationVector rotationVector = scaffoldPlayerSnapshot.feetPosition();
        RotationVector currentX =
            rotationVector.add(
                new RotationVector(
                    scaffoldPlayerSnapshot.velocity().x() * currentDoubleValue,
                    0.0,
                    scaffoldPlayerSnapshot.velocity().z() * currentDoubleValue));
        double currentLength = worldVector.length();
        if (currentLength < 1.0E-9) {
          return ScaffoldHasContinuousSupportService.hasContinuousSupport(
              rotationVector, currentX, value, doubleValue, query);
        }

        RotationVector nextX =
            rotationVector.add(
                new RotationVector(
                    (scaffoldPlayerSnapshot.velocity().x()
                            + worldVector.x() / currentLength * nextDoubleValue)
                        * currentDoubleValue,
                    0.0,
                    (scaffoldPlayerSnapshot.velocity().z()
                            + worldVector.z() / currentLength * nextDoubleValue)
                        * currentDoubleValue));
        return ScaffoldHasContinuousSupportService.hasContinuousTriangleSupport(
            rotationVector, currentX, nextX, value, doubleValue, query);
      } else {
        return false;
      }
    } else {
      throw new IllegalArgumentException("Invalid ground travel safety bounds");
    }
  }
}
