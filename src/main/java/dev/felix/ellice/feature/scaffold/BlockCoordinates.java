package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;

public record BlockCoordinates(int x, int y, int z) {
   public BlockCoordinates offset(ScaffoldMode scaffoldMode) {
      return new BlockCoordinates(this.x + scaffoldMode.x(), this.y + scaffoldMode.y(), this.z + scaffoldMode.z());
   }

   public RotationVector facePoint(ScaffoldMode scaffoldMode, double currentY, double width) {
      updateState(currentY);
      updateState(width);

      return switch (scaffoldMode) {
         case UP -> new RotationVector(this.x + currentY, this.y + 1.0, this.z + width);
         case DOWN -> new RotationVector(this.x + currentY, this.y, this.z + width);
         case NORTH -> new RotationVector(this.x + currentY, this.y + width, this.z);
         case SOUTH -> new RotationVector(this.x + currentY, this.y + width, this.z + 1.0);
         case WEST -> new RotationVector(this.x, this.y + width, this.z + currentY);
         case EAST -> new RotationVector(this.x + 1.0, this.y + width, this.z + currentY);
      };
   }

   private static void updateState(double doubleValue) {
      if (!Double.isFinite(doubleValue) || doubleValue < 0.0 || doubleValue > 1.0) {
         throw new IllegalArgumentException("Face coordinates must be in [0, 1]");
      }
   }
}
