package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.lighting.DirectionalLight;
import org.joml.Vector3f;

public final class TerrainSunDirectionService {
  public static final Vector3f DEFAULT_SUN = new Vector3f(-0.55F, 0.55F, -0.35F).normalize();
  private static final float value = 0.25F;
  private static final double value2 = 0.12;
  private static final double value3 = 10.0;
  private static final float value4 = 2.2F;

  private TerrainSunDirectionService() {}

  public static Vector3f sunDirection(long longValue) {
    double y = (longValue % 24000L + 24000L) % 24000L / 24000.0;
    double width = y * 3.141592653589793 * 2.0;
    return new Vector3f((float) Math.cos(width), (float) Math.sin(width), 0.25F).normalize();
  }

  public static float daylight(float value) {
    return !Float.isFinite(value) ? 1.0F : Math.clamp(1.0F - value, 0.0F, 1.0F);
  }

  public static DirectionalLight mapSun(Vector3f vector3f, float value) {
    Vector3f currentVector3f = createVector3f(vector3f);
    float currentValue = calculateValue(value, 1.0F);
    if (!(currentValue <= 0.02F) && !(currentVector3f.y < -0.02F)) {
      float nextValue = Math.clamp(currentVector3f.y, 0.0F, 1.0F);
      float previousValue =
          Math.clamp(1.0F - nextValue * 1.6F, 0.0F, 1.0F)
              * Math.clamp(currentValue * 2.0F, 0.0F, 1.0F);
      Vector3f nextVector3f =
          new Vector3f(1.0F, 0.96F - 0.41F * previousValue, 0.9F - 0.6F * previousValue);
      return new DirectionalLight(currentVector3f, nextVector3f, 0.5F + 1.9F * currentValue);
    } else {
      Vector3f previousVector3f =
          new Vector3f(
                  -currentVector3f.x,
                  Math.clamp(-currentVector3f.y, 0.25F, 1.0F),
                  -currentVector3f.z)
              .normalize();
      return new DirectionalLight(previousVector3f, new Vector3f(0.55F, 0.68F, 0.95F), 0.5F);
    }
  }

  public static Vector3f mapAmbientColor(float value) {
    float currentValue = calculateValue(value, 1.0F);
    return new Vector3f(
        0.35F + 0.2F * currentValue, 0.42F + 0.2F * currentValue, 0.6F + 0.15F * currentValue);
  }

  public static float mapAmbientIntensity(float value) {
    return 0.16F + 0.19F * calculateValue(value, 1.0F);
  }

  public static Vector3f gradeTint(float value, float currentValue) {
    float nextValue = calculateValue(value, 1.0F);
    float previousValue =
        Float.isFinite(currentValue) ? Math.clamp(currentValue, 0.0F, 1.0F) : 0.65F;
    float sourceValue =
        Math.clamp(1.0F - previousValue * 1.6F, 0.0F, 1.0F)
            * Math.clamp(nextValue * 2.0F, 0.0F, 1.0F);
    return new Vector3f(
        0.94F + 0.11F * nextValue + 0.06F * sourceValue,
        0.97F + 0.03F * nextValue - 0.02F * sourceValue,
        1.08F - 0.13F * nextValue - 0.06F * sourceValue);
  }

  public static float shadowHeightScale(double doubleValue) {
    return Double.isFinite(doubleValue) && !(doubleValue <= 0.0)
        ? Math.min(2.2F, (float) (1.0 + doubleValue * 0.12))
        : 1.0F;
  }

  public static float shadowHeightFade(double doubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue <= 0.0) {
      return 1.0F;
    } else {
      return doubleValue >= 10.0 ? 0.0F : (float) (1.0 - doubleValue / 10.0);
    }
  }

  private static Vector3f createVector3f(Vector3f vector3f) {
    return vector3f != null && vector3f.isFinite() && vector3f.lengthSquared() > 1.0E-8F
        ? new Vector3f(vector3f).normalize()
        : new Vector3f(DEFAULT_SUN);
  }

  private static float calculateValue(float value, float currentValue) {
    return !Float.isFinite(value) ? currentValue : Math.clamp(value, 0.0F, 1.0F);
  }

  public static Vector3f cinematicSunDirection(double x, double y) {
    double width = Double.isFinite(y) && y > 1.0 ? y : 20.0;
    double height = Double.isFinite(x) ? Math.clamp(x / width, 0.0, 1.0) : 0.0;
    double doubleValue = -0.45 + 0.1 * (height * height * (3.0 - 2.0 * height));
    double currentDoubleValue = 0.36 - 0.06 * Math.sin(3.141592653589793 * height);
    double nextDoubleValue = Math.cos(currentDoubleValue);
    return new Vector3f(
            (float) (Math.sin(doubleValue) * nextDoubleValue),
            (float) Math.sin(currentDoubleValue),
            (float) (Math.cos(doubleValue) * nextDoubleValue))
        .normalize();
  }

  public static float cinematicDaylight() {
    return 1.0F;
  }

  public static CompatLoadedHandler.SkyState cinematicSky(
      double doubleValue, double currentDoubleValue) {
    return new CompatLoadedHandler.SkyState(
        cinematicSunDirection(doubleValue, currentDoubleValue), cinematicDaylight());
  }
}
