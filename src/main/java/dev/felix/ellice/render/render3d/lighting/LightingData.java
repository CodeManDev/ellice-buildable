package dev.felix.ellice.render.render3d.lighting;

import org.joml.Vector3d;
import org.joml.Vector3f;

public record LightingData(Vector3d position, Vector3f color, float intensity, float radius) {
  public LightingData(Vector3d position, Vector3f color, float intensity, float radius) {
    if (position == null || color == null || !checkCondition(position) || !checkCondition2(color)) {
      throw new IllegalArgumentException("Position and color must be finite");
    }

    if (!Float.isFinite(intensity)) {
      throw new IllegalArgumentException("Point-light intensity must be finite");
    }

    if (Float.isFinite(radius) && radius > 0.0F) {
      position = new Vector3d(position);
      color = new Vector3f(color);
      intensity = Math.max(0.0F, intensity);
      this.position = position;
      this.color = color;
      this.intensity = intensity;
      this.radius = radius;
    } else {
      throw new IllegalArgumentException("Point-light radius must be finite and positive");
    }
  }

  public Vector3d position() {
    return new Vector3d(this.position);
  }

  public Vector3f color() {
    return new Vector3f(this.color);
  }

  private static boolean checkCondition(Vector3d vector3d) {
    return Double.isFinite(vector3d.x)
        && Double.isFinite(vector3d.y)
        && Double.isFinite(vector3d.z);
  }

  private static boolean checkCondition2(Vector3f vector3f) {
    return Float.isFinite(vector3f.x) && Float.isFinite(vector3f.y) && Float.isFinite(vector3f.z);
  }
}
