package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public record PlacementCandidate(
   BlockCoordinates targetPosition, BlockCoordinates supportPosition, ScaffoldMode clickedFace, RotationVector hitPoint, boolean inside
) {
   public PlacementCandidate(BlockCoordinates blockCoordinates, BlockCoordinates currentBlockCoordinates, ScaffoldMode scaffoldMode, RotationVector rotationVector) {
      this(blockCoordinates, currentBlockCoordinates, scaffoldMode, rotationVector, false);
   }

   public PlacementCandidate(
      BlockCoordinates targetPosition, BlockCoordinates supportPosition, ScaffoldMode clickedFace, RotationVector hitPoint, boolean inside
   ) {
      Objects.requireNonNull(targetPosition, "targetPosition");
      Objects.requireNonNull(supportPosition, "supportPosition");
      Objects.requireNonNull(clickedFace, "clickedFace");
      Objects.requireNonNull(hitPoint, "hitPoint");
      if (!supportPosition.equals(targetPosition) && !supportPosition.offset(clickedFace).equals(targetPosition)) {
         throw new IllegalArgumentException("Clicked support face must point at the placement target");
      }

      this.targetPosition = targetPosition;
      this.supportPosition = supportPosition;
      this.clickedFace = clickedFace;
      this.hitPoint = hitPoint;
      this.inside = inside;
   }

   public PlacementCandidate withSurfacePoint(double x, double y) {
      return new PlacementCandidate(
         this.targetPosition,
         this.supportPosition,
         this.clickedFace,
         this.supportPosition.facePoint(this.clickedFace, x, y),
         this.inside
      );
   }

   public double faceEdgeMargin() {
      double currentX = this.hitPoint.x() - this.supportPosition.x();
      double currentY = this.hitPoint.y() - this.supportPosition.y();
      double doubleValue = this.hitPoint.z() - this.supportPosition.z();
      double currentDoubleValue;
      double nextDoubleValue;
      switch (this.clickedFace) {
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
            throw new IllegalStateException("Unknown block face " + this.clickedFace);
      }

      return calculateValue(Math.min(Math.min(currentDoubleValue, 1.0 - currentDoubleValue), Math.min(nextDoubleValue, 1.0 - nextDoubleValue)));
   }

   private static double calculateValue(double doubleValue) {
      return Math.max(0.0, Math.min(0.5, doubleValue));
   }
}
