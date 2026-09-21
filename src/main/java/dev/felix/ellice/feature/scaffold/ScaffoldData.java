package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;

public record ScaffoldData(
   String activation,
   String aimMode,
   String angleMode,
   String turnStyle,
   double smoothness,
   int startDelay,
   double headingTolerance,
   boolean autoPitch,
   double minimumPitch,
   double maximumPitch,
   boolean diagonalAlignment,
   String clickRhythm,
   int eagleInterval,
   String jumpTiming,
   boolean autoSelectBlocks,
   double footDistance,
   double rescueDrop,
   int rescueDepth,
   boolean rescueTurns
) {
   public boolean activated(boolean enabled, boolean currentEnabled) {
      return activated(this.activation, enabled, currentEnabled);
   }

   public static boolean activated(String text, boolean enabled, boolean currentEnabled) {
      return (boolean)(switch (text) {
         case "Hold Use" -> enabled;
         case "Hold Sneak" -> currentEnabled;
         default -> 1;
      });
   }

   public boolean cameraAim() {
      return "Camera".equals(this.aimMode);
   }

   public boolean acceptsPitch(double doubleValue) {
      return !this.autoPitch
         || doubleValue >= this.minimumPitch - 1.0E-5
            && doubleValue <= this.maximumPitch + 1.0E-5;
   }

   public String acquisitionProfile() {
      return this.aimMode + ":" + this.angleMode + ":" + this.autoPitch + ":" + this.minimumPitch + ":" + this.maximumPitch;
   }

   public double yawOffset(double doubleValue, RotationVector rotationVector) {
      return switch (this.angleMode) {
         case "Diagonal Left" -> -45.0;
         case "Diagonal Right" -> 45.0;
         case "Auto Diagonal" -> {
            if (Math.abs(Math.sin(Math.toRadians(doubleValue * 2.0)))
               > 0.1) {
               yield 0.0;
            } else {
               RotationVector currentRotationVector = new RotationData(doubleValue + 135.0, 0.0).direction();
               RotationVector nextRotationVector = new RotationData(doubleValue + 225.0, 0.0).direction();
               double currentX = Math.floor(rotationVector.x()) + 0.5 - rotationVector.x();
               double currentDoubleValue = Math.floor(rotationVector.z()) + 0.5 - rotationVector.z();
               yield currentRotationVector.x() * currentX + currentRotationVector.z() * currentDoubleValue >= nextRotationVector.x() * currentX + nextRotationVector.z() * currentDoubleValue
                  ? -45.0
                  : 45.0;
            }
         }
         default -> 0.0;
      };
   }
}
