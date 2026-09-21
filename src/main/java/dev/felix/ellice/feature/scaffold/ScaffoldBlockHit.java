package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public record ScaffoldBlockHit(
    BlockCoordinates blockPosition, ScaffoldMode face, RotationVector hitPoint, boolean inside) {
  public ScaffoldBlockHit(
      BlockCoordinates blockPosition, ScaffoldMode face, RotationVector hitPoint, boolean inside) {
    Objects.requireNonNull(blockPosition, "blockPosition");
    Objects.requireNonNull(face, "face");
    Objects.requireNonNull(hitPoint, "hitPoint");
    this.blockPosition = blockPosition;
    this.face = face;
    this.hitPoint = hitPoint;
    this.inside = inside;
  }

  public double faceEdgeMargin() {
    double currentX = this.hitPoint.x() - this.blockPosition.x();
    double currentY = this.hitPoint.y() - this.blockPosition.y();
    double doubleValue = this.hitPoint.z() - this.blockPosition.z();
    double currentDoubleValue;
    double nextDoubleValue;
    switch (this.face) {
      case UP:
      case DOWN:
        currentDoubleValue = currentX;
        nextDoubleValue = doubleValue;
        break;
      case NORTH:
      case SOUTH:
        currentDoubleValue = currentX;
        nextDoubleValue = currentY;
        break;
      case WEST:
      case EAST:
        currentDoubleValue = doubleValue;
        nextDoubleValue = currentY;
        break;
      default:
        throw new IllegalStateException("Unknown block face " + this.face);
    }

    double previousDoubleValue =
        Math.min(
            Math.min(currentDoubleValue, 1.0 - currentDoubleValue),
            Math.min(nextDoubleValue, 1.0 - nextDoubleValue));
    return Math.max(0.0, Math.min(0.5, previousDoubleValue));
  }

  public static ScaffoldBlockHit from(PlacementCandidate placementCandidate) {
    Objects.requireNonNull(placementCandidate, "placement");
    return new ScaffoldBlockHit(
        placementCandidate.supportPosition(),
        placementCandidate.clickedFace(),
        placementCandidate.hitPoint(),
        placementCandidate.inside());
  }
}
