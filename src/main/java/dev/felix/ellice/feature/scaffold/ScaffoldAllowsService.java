package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public final class ScaffoldAllowsService {
  private static final double value = 0.045;

  private ScaffoldAllowsService() {}

  public static boolean allows(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      PlacementCandidate placementCandidate,
      ScaffoldRemapService.WorldVector worldVector,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      ContinuousSupportChecker.Query query) {
    Objects.requireNonNull(scaffoldPlayerSnapshot, "frame");
    Objects.requireNonNull(worldVector, "appliedDirection");
    Objects.requireNonNull(query, "confirmedSupport");
    if (!Double.isFinite(currentDoubleValue)
        || currentDoubleValue < 1.0
        || currentDoubleValue > 32.0
        || !Double.isFinite(doubleValue)
        || doubleValue <= 0.0
        || doubleValue > 0.3) {
      throw new IllegalArgumentException("Invalid approach safety bounds");
    }

    if (!scaffoldPlayerSnapshot.onGround() || placementCandidate == null) {
      return false;
    }

    if (Double.isFinite(nextDoubleValue) && !(nextDoubleValue < 0.0)) {
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN) {
        BlockCoordinates blockCoordinates = placementCandidate.supportPosition();
        if (!(Math.abs(scaffoldPlayerSnapshot.feetPosition().y() - (blockCoordinates.y() + 1.0))
                > 1.0E-4)
            && query.hasSupport(blockCoordinates)) {
          double currentLength = worldVector.length();
          if (currentLength < 1.0E-9) {
            return false;
          }

          double currentX = worldVector.x() / currentLength;
          double previousDoubleValue = worldVector.z() / currentLength;
          double nextX = currentX * scaffoldMode.x() + previousDoubleValue * scaffoldMode.z();
          if (nextX <= 1.0E-4) {
            return false;
          }

          double previousX =
              (scaffoldPlayerSnapshot.eyePosition().x() - placementCandidate.hitPoint().x())
                      * scaffoldMode.x()
                  + (scaffoldPlayerSnapshot.eyePosition().z() - placementCandidate.hitPoint().z())
                      * scaffoldMode.z();
          double sourceDoubleValue = Math.max(0.0, 0.045 - previousX) / nextX;
          if (sourceDoubleValue > 3.0) {
            return false;
          }

          RotationVector rotationVector = scaffoldPlayerSnapshot.feetPosition();
          RotationVector currentRotationVector =
              rotationVector.add(
                  new RotationVector(
                      currentX * sourceDoubleValue, 0.0, previousDoubleValue * sourceDoubleValue));
          return !ScaffoldHasContinuousSupportService.hasContinuousSupport(
                  rotationVector, currentRotationVector, blockCoordinates.y(), doubleValue, query)
              ? false
              : PlacementRuleEvaluator.allows(
                  scaffoldPlayerSnapshot,
                  worldVector,
                  blockCoordinates.y(),
                  doubleValue,
                  currentDoubleValue,
                  nextDoubleValue,
                  query);
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
