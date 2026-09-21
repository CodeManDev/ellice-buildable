package dev.felix.ellice.render.render3d.lighting;

import org.joml.Vector3f;

public record DirectionalLight(Vector3f direction, Vector3f color, float intensity) {
  public DirectionalLight(Vector3f direction, Vector3f color, float intensity) {
    if (direction == null
        || !checkCondition(direction)
        || !Float.isFinite(direction.lengthSquared())
        || direction.lengthSquared() < 1.0E-8F) {
      throw new IllegalArgumentException("Light direction must be non-zero");
    }

    if (color == null || !checkCondition(color)) {
      throw new IllegalArgumentException("Light color must be finite");
    }

    if (!Float.isFinite(intensity)) {
      throw new IllegalArgumentException("Light intensity must be finite");
    }

    direction = new Vector3f(direction).normalize();
    color = new Vector3f(color);
    intensity = Math.max(0.0F, intensity);
    this.direction = direction;
    this.color = color;
    this.intensity = intensity;
  }

  public Vector3f direction() {
    return new Vector3f(this.direction);
  }

  public Vector3f color() {
    return new Vector3f(this.color);
  }

  private static boolean checkCondition(Vector3f vector3f) {
    return Float.isFinite(vector3f.x) && Float.isFinite(vector3f.y) && Float.isFinite(vector3f.z);
  }

  public static DirectionalLight daylight() {
    return new DirectionalLight(
        new Vector3f(-0.36F, 0.82F, -0.44F), new Vector3f(1.0F, 0.95F, 0.86F), 3.2F);
  }
}
