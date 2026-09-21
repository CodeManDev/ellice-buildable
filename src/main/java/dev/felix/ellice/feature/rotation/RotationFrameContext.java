package dev.felix.ellice.feature.rotation;

import java.util.List;
import java.util.Objects;

public record RotationFrameContext(RotationVector eyePosition, RotationData playerRotation, double mouseSensitivity, List<RotationTarget> entities) {
   public RotationFrameContext(RotationVector eyePosition, RotationData playerRotation, double mouseSensitivity, List<RotationTarget> entities) {
      Objects.requireNonNull(eyePosition, "eyePosition");
      Objects.requireNonNull(playerRotation, "playerRotation");
      if (Double.isFinite(mouseSensitivity) && !(mouseSensitivity < 0.0) && !(mouseSensitivity > 1.0)) {
         entities = List.copyOf(entities);
         this.eyePosition = eyePosition;
         this.playerRotation = playerRotation;
         this.mouseSensitivity = mouseSensitivity;
         this.entities = entities;
      } else {
         throw new IllegalArgumentException("Mouse sensitivity must be in [0, 1]");
      }
   }
}
