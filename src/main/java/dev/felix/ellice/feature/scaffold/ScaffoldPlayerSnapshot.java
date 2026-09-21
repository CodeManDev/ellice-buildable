package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public record ScaffoldPlayerSnapshot(
    RotationVector eyePosition,
    RotationVector feetPosition,
    RotationVector velocity,
    RotationData playerRotation,
    double mouseSensitivity,
    boolean onGround) {
  public ScaffoldPlayerSnapshot(
      RotationVector eyePosition,
      RotationVector feetPosition,
      RotationVector velocity,
      RotationData playerRotation,
      double mouseSensitivity,
      boolean onGround) {
    Objects.requireNonNull(eyePosition, "eyePosition");
    Objects.requireNonNull(feetPosition, "feetPosition");
    Objects.requireNonNull(velocity, "velocity");
    Objects.requireNonNull(playerRotation, "playerRotation");
    if (Double.isFinite(mouseSensitivity)
        && !(mouseSensitivity < 0.0)
        && !(mouseSensitivity > 1.0)) {
      this.eyePosition = eyePosition;
      this.feetPosition = feetPosition;
      this.velocity = velocity;
      this.playerRotation = playerRotation;
      this.mouseSensitivity = mouseSensitivity;
      this.onGround = onGround;
    } else {
      throw new IllegalArgumentException("Mouse sensitivity must be in [0, 1]");
    }
  }

  public double horizontalSpeed() {
    return Math.hypot(this.velocity.x(), this.velocity.z());
  }
}
