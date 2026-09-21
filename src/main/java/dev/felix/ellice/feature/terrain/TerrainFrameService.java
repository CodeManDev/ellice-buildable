package dev.felix.ellice.feature.terrain;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public final class TerrainFrameService {
  public static final int SHADOW_SIZE = 2048;
  public static final int EXPORT_SHADOW_SIZE = 4096;
  public static final float MIN_RADIUS = 48.0F;
  public static final float MAX_RADIUS = 512.0F;
  public static final float RELIEF = 350.0F;
  public static final float RADIUS_STEP = 16.0F;

  private TerrainFrameService() {}

  public static TerrainFrameService.ShadowFrame frame(Vector3f vector3f, float value) {
    return frame(vector3f, value, new Vector3d());
  }

  public static TerrainFrameService.ShadowFrame frame(
      Vector3f vector3f, float value, Vector3d vector3d) {
    return frame(vector3f, value, vector3d, 2048);
  }

  public static int clampShadowSize(int value) {
    return value >= 1024 && value <= 8192 ? Integer.highestOneBit(value - 1) << 1 : 2048;
  }

  public static TerrainFrameService.ShadowFrame frame(
      Vector3f vector3f, float value, Vector3d vector3d, int currentValue) {
    Vector3f currentVector3f = new Vector3f(vector3f);
    if (!currentVector3f.isFinite() || currentVector3f.lengthSquared() < 1.0E-8F) {
      currentVector3f.set(TerrainSunDirectionService.DEFAULT_SUN);
    }

    currentVector3f.normalize();
    float nextValue = Math.clamp(value, 48.0F, 512.0F);
    float previousValue = nextValue + 500.0F;
    Vector3f nextVector3f = new Vector3f(currentVector3f).mul(previousValue);
    Vector3f previousVector3f =
        Math.abs(currentVector3f.y) > 0.99F
            ? new Vector3f(0.0F, 0.0F, 1.0F)
            : new Vector3f(0.0F, 1.0F, 0.0F);
    Matrix4f matrix4f =
        new Matrix4f().lookAt(nextVector3f, new Vector3f(0.0F, 0.0F, 0.0F), previousVector3f);
    float sourceValue = 150.0F;
    float targetValue = 2.0F * nextValue + 850.0F;
    Matrix4f currentMatrix4f =
        new Matrix4f()
            .ortho(-nextValue, nextValue, -nextValue, nextValue, sourceValue, targetValue, false);
    float inputValue = 2.0F * nextValue / clampShadowSize(currentValue);
    double doubleValue =
        matrix4f.m00() * vector3d.x + matrix4f.m10() * vector3d.y + matrix4f.m20() * vector3d.z;
    double currentDoubleValue =
        matrix4f.m01() * vector3d.x + matrix4f.m11() * vector3d.y + matrix4f.m21() * vector3d.z;
    Vector3f sourceVector3f =
        new Vector3f(
            (float)
                (doubleValue
                    - Math.floor(doubleValue / inputValue) * inputValue
                    + 0.5 * inputValue),
            (float)
                (currentDoubleValue
                    - Math.floor(currentDoubleValue / inputValue) * inputValue
                    + 0.5 * inputValue),
            0.0F);
    Matrix4f nextMatrix4f =
        new Matrix4f(currentMatrix4f).mul(new Matrix4f().translation(sourceVector3f)).mul(matrix4f);
    return new TerrainFrameService.ShadowFrame(
        matrix4f, nextMatrix4f, nextValue, sourceValue, targetValue);
  }

  public static float radiusFor(double doubleValue) {
    if (!Double.isFinite(doubleValue)) {
      return 256.0F;
    }

    float value = (float) (doubleValue * 0.8);
    return Math.clamp((float) (Math.ceil(value / 16.0F) * 16.0), 48.0F, 512.0F);
  }

  public static float radiusFor(double doubleValue, double currentDoubleValue) {
    if (!Double.isFinite(doubleValue)) {
      return 256.0F;
    }

    double nextDoubleValue =
        Double.isFinite(currentDoubleValue)
            ? Math.clamp(currentDoubleValue, 0.2, 1.5707963267948966)
            : 1.0;
    double previousDoubleValue =
        Math.max(0.0, doubleValue) * (0.8 + 1.3 / Math.tan(nextDoubleValue));
    return Math.clamp(
        (float) (Math.ceil(previousDoubleValue / 16.0 - 1.0E-9) * 16.0), 48.0F, 512.0F);
  }

  public record ShadowFrame(
      Matrix4f view, Matrix4f viewProjection, float radius, float near, float far) {
    public ShadowFrame(
        Matrix4f view, Matrix4f viewProjection, float radius, float near, float far) {
      view = new Matrix4f(view);
      viewProjection = new Matrix4f(viewProjection);
      this.view = view;
      this.viewProjection = viewProjection;
      this.radius = radius;
      this.near = near;
      this.far = far;
    }
  }
}
