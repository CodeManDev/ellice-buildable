package dev.felix.ellice.compat;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.BlockCoordinates;
import dev.felix.ellice.feature.scaffold.PlacementCandidate;
import dev.felix.ellice.feature.scaffold.ScaffoldBlockHit;
import dev.felix.ellice.feature.scaffold.ScaffoldMode;
import dev.felix.ellice.feature.scaffold.ScaffoldPlayerSnapshot;
import java.util.Optional;
import net.minecraft.client.Minecraft;

public interface ScaffoldCompatibility {
  Optional<ScaffoldPlayerSnapshot> capture(Minecraft minecraft);

  boolean isReplaceable(Minecraft minecraft, BlockCoordinates blockCoordinates);

  boolean isPlayerSupporting(Minecraft minecraft, BlockCoordinates blockCoordinates);

  default double groundFriction(Minecraft minecraft) {
    return 1.0;
  }

  default double groundInputAcceleration(Minecraft minecraft) {
    return Double.NaN;
  }

  default double groundInputAcceleration(Minecraft minecraft, boolean enabled) {
    return enabled ? Double.NaN : this.groundInputAcceleration(minecraft);
  }

  default double groundInputAccelerationEstimate(
      Minecraft minecraft, int value, int currentValue, boolean enabled) {
    if (value >= -1 && value <= 1 && currentValue >= -1 && currentValue <= 1) {
      return Double.NaN;
    } else {
      throw new IllegalArgumentException("Input axes must be in [-1, 1]");
    }
  }

  default double airInputAccelerationEstimate(Minecraft minecraft, int value, int currentValue) {
    return 0.019999999552965164
        * Math.min(1.0, Math.hypot(value, currentValue) * 0.9800000190734863);
  }

  default double jumpVelocityEstimate(Minecraft minecraft) {
    return 0.41999998688697815;
  }

  boolean isFaceSturdy(
      Minecraft minecraft, BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode);

  int findPlaceableHotbarSlot(Minecraft minecraft);

  int selectedHotbarSlot(Minecraft minecraft);

  void selectHotbarSlot(Minecraft minecraft, int value);

  boolean canStartUseItem(Minecraft minecraft);

  Optional<PlacementCandidate> raycastPlacement(
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      RotationData rotationData,
      double doubleValue);

  Optional<PlacementCandidate> raycastPlacementFromEye(
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      RotationData rotationData,
      double doubleValue,
      RotationVector rotationVector);

  Optional<ScaffoldBlockHit> raycastBlockFromEye(
      Minecraft minecraft,
      RotationData rotationData,
      double doubleValue,
      RotationVector rotationVector);

  default Optional<PlacementCandidate> placementFromRay(
      Minecraft minecraft, ScaffoldBlockHit scaffoldBlockHit) {
    return CompatCanReplaceService.resolve(
        minecraft, scaffoldBlockHit, this.findPlaceableHotbarSlot(minecraft));
  }

  default boolean rayHitsPlacement(
      Minecraft minecraft,
      PlacementCandidate placementCandidate,
      RotationData rotationData,
      double doubleValue) {
    return this.raycastPlacement(minecraft, placementCandidate, rotationData, doubleValue)
        .isPresent();
  }

  boolean place(Minecraft minecraft, PlacementCandidate placementCandidate);

  default ScaffoldCompatibility.PlacementFeedback placementFeedback(
      Object value, BlockCoordinates blockCoordinates) {
    return ScaffoldCompatibility.PlacementFeedback.NONE;
  }

  default int placementSequence(Object value, PlacementCandidate placementCandidate) {
    return -1;
  }

  default int acknowledgedPlacementSequence(Object value) {
    return -1;
  }

  void reconcilePlacementPrediction(Minecraft minecraft, int value);

  enum PlacementFeedback {
    NONE,
    ACCEPTED,
    REJECTED;

    private static ScaffoldCompatibility.PlacementFeedback[] $values() {
      return new ScaffoldCompatibility.PlacementFeedback[] {NONE, ACCEPTED, REJECTED};
    }
  }
}
