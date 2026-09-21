package dev.felix.ellice.feature.rotation;

public record RotationData(double yaw, double pitch) {
  public RotationData(double yaw, double pitch) {
    if (Double.isFinite(yaw) && Double.isFinite(pitch)) {
      this.yaw = yaw;
      this.pitch = pitch;
    } else {
      throw new IllegalArgumentException("Rotation components must be finite");
    }
  }

  public static RotationData lookAt(
      RotationVector rotationVector, RotationVector currentRotationVector) {
    RotationVector nextRotationVector = currentRotationVector.subtract(rotationVector);
    double currentX = Math.hypot(nextRotationVector.x(), nextRotationVector.z());
    if (currentX < 1.0E-12 && Math.abs(nextRotationVector.y()) < 1.0E-12) {
      throw new IllegalArgumentException("Eye and target point must differ");
    }

    double nextX =
        Math.toDegrees(Math.atan2(nextRotationVector.z(), nextRotationVector.x())) - 90.0;
    double currentY = -Math.toDegrees(Math.atan2(nextRotationVector.y(), currentX));
    return new RotationData(nextX, clampPitch(currentY));
  }

  public RotationVector direction() {
    double x = Math.toRadians(this.yaw);
    double y = Math.toRadians(this.pitch);
    double width = Math.cos(y);
    return new RotationVector(-Math.sin(x) * width, -Math.sin(y), Math.cos(x) * width);
  }

  public RotationData withPitchClamped() {
    return new RotationData(this.yaw, clampPitch(this.pitch));
  }

  public static RotationData interpolate(
      RotationData rotationData, RotationData currentRotationData, double doubleValue) {
    double currentDoubleValue = Math.max(0.0, Math.min(1.0, doubleValue));
    return new RotationData(
        rotationData.yaw + yawDelta(rotationData.yaw, currentRotationData.yaw) * currentDoubleValue,
        rotationData.pitch + (currentRotationData.pitch - rotationData.pitch) * currentDoubleValue);
  }

  public static double distance(RotationData rotationData, RotationData currentRotationData) {
    return Math.hypot(
        yawDelta(rotationData.yaw, currentRotationData.yaw),
        currentRotationData.pitch - rotationData.pitch);
  }

  public static double yawDelta(double doubleValue, double currentDoubleValue) {
    return wrapDegrees(currentDoubleValue - doubleValue);
  }

  public static double wrapDegrees(double doubleValue) {
    double currentDoubleValue = doubleValue % 360.0;
    if (currentDoubleValue >= 180.0) {
      currentDoubleValue -= 360.0;
    }

    if (currentDoubleValue < -180.0) {
      currentDoubleValue += 360.0;
    }

    return currentDoubleValue;
  }

  public static double clampPitch(double doubleValue) {
    return Math.max(-90.0, Math.min(90.0, doubleValue));
  }
}
