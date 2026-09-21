package dev.felix.ellice.feature.terrain;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;

public final class TerrainViewportService {
  public static final double MIN_DISTANCE = 12.0;
  public static final double MAX_DISTANCE = 8192.0;
  private final TerrainViewportService.Spring spring =
      new TerrainViewportService.Spring(Math.log(220.0));
  private final TerrainViewportService.Spring spring2 =
      new TerrainViewportService.Spring(Math.toRadians(-35.0));
  private final TerrainViewportService.Spring spring3 =
      new TerrainViewportService.Spring(Math.toRadians(58.0));
  private final TerrainViewportService.Spring spring4 = new TerrainViewportService.Spring(64.0);
  private final TerrainViewportService.Spring spring5 = new TerrainViewportService.Spring(0.0);
  private double value2;
  private double value3 = 64.0;
  private double value4;
  private double value5;
  private double value6;
  private float value7 = 1.0F;
  private float value8 = 1.0F;
  private Vector3d vector3d;
  private float value9;
  private float value10;
  private boolean enabled;
  private TerrainViewportService.Spring[] spring6;

  public void viewport(float x, float y) {
    this.value7 = Math.max(1.0F, x);
    this.value8 = Math.max(1.0F, y);
  }

  public void center(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    if (Double.isFinite(doubleValue)
        && Double.isFinite(currentDoubleValue)
        && Double.isFinite(nextDoubleValue)) {
      this.value2 = Math.clamp(doubleValue, -2.9999984E7, 2.9999984E7);
      this.value3 = Math.clamp(currentDoubleValue, -4096.0, 4096.0);
      this.spring4.snap(this.value3);
      this.value4 = Math.clamp(nextDoubleValue, -2.9999984E7, 2.9999984E7);
      this.value5 = this.value6 = 0.0;
      this.vector3d = null;
      this.spring6 = null;
    }
  }

  public void flyTo(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    if (Double.isFinite(doubleValue)
        && Double.isFinite(currentDoubleValue)
        && Double.isFinite(nextDoubleValue)) {
      this.spring6 =
          new TerrainViewportService.Spring[] {
            new TerrainViewportService.Spring(this.value2),
            new TerrainViewportService.Spring(this.value3),
            new TerrainViewportService.Spring(this.value4)
          };
      this.spring6[0].target = Math.clamp(doubleValue, -2.9999984E7, 2.9999984E7);
      this.spring6[1].target = Math.clamp(currentDoubleValue, -4096.0, 4096.0);
      this.spring6[2].target = Math.clamp(nextDoubleValue, -2.9999984E7, 2.9999984E7);
      this.vector3d = null;
      this.value5 = this.value6 = 0.0;
    }
  }

  public TerrainViewportService.Frame frame() {
    double doubleValue = Math.exp(this.spring.value);
    double currentDoubleValue = this.spring3.value;
    double nextDoubleValue = this.spring2.value;
    double previousDoubleValue = Math.cos(currentDoubleValue) * doubleValue;
    float currentValue = (float) (Math.sin(nextDoubleValue) * previousDoubleValue);
    float nextValue = (float) (Math.sin(currentDoubleValue) * doubleValue);
    float previousValue = (float) (Math.cos(nextDoubleValue) * previousDoubleValue);
    Matrix4f matrix4f =
        new Matrix4f()
            .lookAt(
                currentValue,
                nextValue,
                previousValue,
                0.0F,
                0.0F,
                0.0F,
                (float) (-Math.sin(nextDoubleValue) * Math.sin(currentDoubleValue)),
                (float) Math.cos(currentDoubleValue),
                (float) (-Math.cos(nextDoubleValue) * Math.sin(currentDoubleValue)));
    Matrix4f currentMatrix4f =
        new Matrix4f()
            .perspective(
                (float) Math.toRadians(45.0),
                this.value7 / this.value8,
                0.5F,
                (float) Math.max(2048.0, doubleValue * 6.0));
    if (this.spring5.value > 0.0) {
      float sourceValue = (float) doubleValue;
      currentMatrix4f
          .m00(currentMatrix4f.m00() / sourceValue)
          .m11(currentMatrix4f.m11() / sourceValue)
          .m22(currentMatrix4f.m22() / sourceValue)
          .m23(currentMatrix4f.m23() / sourceValue)
          .m32(currentMatrix4f.m32() / sourceValue);
      float targetValue = sourceValue * 0.41421357F;
      Matrix4f nextMatrix4f =
          new Matrix4f()
              .ortho(
                  -targetValue * this.value7 / this.value8,
                  targetValue * this.value7 / this.value8,
                  -targetValue,
                  targetValue,
                  0.5F,
                  (float) Math.max(2048.0, doubleValue * 6.0));
      currentMatrix4f.lerp(nextMatrix4f, (float) this.spring5.value);
    }

    Matrix4f previousMatrix4f = new Matrix4f(currentMatrix4f).mul(matrix4f);
    return new TerrainViewportService.Frame(
        new Vector3d(this.value2, this.value3, this.value4),
        new Vector3d(
            this.value2 + currentValue, this.value3 + nextValue, this.value4 + previousValue),
        previousMatrix4f,
        new Matrix4f(previousMatrix4f).invert(),
        this.value7,
        this.value8,
        doubleValue,
        matrix4f,
        currentMatrix4f);
  }

  public void zoom(double doubleValue, float value, float currentValue, Vector3d currentVector3d) {
    if (Double.isFinite(doubleValue)) {
      this.spring6 = null;
      this.spring.target =
          Math.clamp(this.spring.target - doubleValue * 0.16, Math.log(12.0), Math.log(8192.0));
      if (currentVector3d != null && currentVector3d.isFinite()) {
        this.spring4.target = Math.clamp(currentVector3d.y, -4096.0, 4096.0);
      }

      this.vector3d =
          currentVector3d != null
              ? new Vector3d(currentVector3d)
              : this.frame().ray(value, currentValue).onPlane(this.value3);
      this.value9 = value / this.value7;
      this.value10 = currentValue / this.value8;
      this.value5 = this.value6 = 0.0;
    }
  }

  public void beginDrag() {
    this.enabled = true;
    this.value5 = this.value6 = 0.0;
    this.vector3d = null;
    this.spring6 = null;
    this.spring.snap(this.spring.value);
    this.spring4.snap(this.value3);
  }

  public void endDrag() {
    this.enabled = false;
  }

  public void pan(
      float value, float currentValue, float nextValue, float previousValue, double doubleValue) {
    TerrainViewportService.Frame currentFrame = this.frame();
    Vector3d currentVector3d = currentFrame.ray(value, currentValue).onPlane(this.value3);
    Vector3d nextVector3d = currentFrame.ray(nextValue, previousValue).onPlane(this.value3);
    if (currentVector3d != null && nextVector3d != null) {
      double currentDoubleValue = currentVector3d.x - nextVector3d.x;
      double nextDoubleValue = currentVector3d.z - nextVector3d.z;
      this.value2 += currentDoubleValue;
      this.value4 += nextDoubleValue;
      if (doubleValue > 0.001 && doubleValue < 0.15) {
        this.value5 = Math.clamp(currentDoubleValue / doubleValue, -3000.0, 3000.0);
        this.value6 = Math.clamp(nextDoubleValue / doubleValue, -3000.0, 3000.0);
      }

      this.vector3d = null;
      this.updateState();
    }
  }

  public void orbit(double doubleValue, double currentDoubleValue) {
    if (Double.isFinite(doubleValue) && Double.isFinite(currentDoubleValue)) {
      this.spring6 = null;
      this.spring2.snap(this.spring2.value - doubleValue * 0.006);
      this.spring3.snap(
          Math.clamp(
              this.spring3.value + currentDoubleValue * 0.005,
              Math.toRadians(20.0),
              Math.toRadians(85.0)));
      this.vector3d = null;
      this.value5 = this.value6 = 0.0;
    }
  }

  public void northUp() {
    this.spring2.target =
        this.spring2.value - Math.IEEEremainder(this.spring2.value, 6.283185307179586);
    this.vector3d = null;
  }

  public boolean topDown() {
    return this.spring5.target > 0.0 || this.spring5.value > 0.001;
  }

  public void topDown(boolean enabled) {
    this.spring3.target = Math.toRadians(enabled ? 90.0 : 58.0);
    this.spring5.target = enabled ? 1.0 : 0.0;
    this.vector3d = null;
  }

  public void planView() {
    this.spring3.snap(1.5707963267948966);
    this.spring2.snap(0.0);
    this.spring5.snap(1.0);
  }

  public void distance(double doubleValue) {
    if (Double.isFinite(doubleValue)) {
      this.spring.snap(Math.log(Math.clamp(doubleValue, 12.0, 8192.0)));
    }

    this.vector3d = null;
  }

  public void zoomTo(double doubleValue) {
    if (Double.isFinite(doubleValue)) {
      this.spring.target = Math.log(Math.clamp(doubleValue, 12.0, 8192.0));
    }

    this.vector3d = null;
  }

  public void advance(double doubleValue) {
    if (Double.isFinite(doubleValue) && !(doubleValue <= 0.0)) {
      double currentDoubleValue = Math.min(doubleValue, 0.1);
      this.spring.advance(currentDoubleValue, 22.0);
      this.spring2.advance(currentDoubleValue, 16.0);
      this.spring3.advance(currentDoubleValue, 16.0);
      this.spring5.advance(currentDoubleValue, 14.0);
      if (this.spring6 != null) {
        for (TerrainViewportService.Spring currentSpring : this.spring6) {
          currentSpring.advance(currentDoubleValue, 10.0);
        }

        this.value2 = this.spring6[0].value;
        this.value3 = this.spring6[1].value;
        this.value4 = this.spring6[2].value;
        this.spring4.snap(this.value3);
        if (this.spring6[0].settled() && this.spring6[1].settled() && this.spring6[2].settled()) {
          this.spring6 = null;
        }
      } else {
        this.spring4.advance(currentDoubleValue, 14.0);
        this.value3 = this.spring4.value;
      }

      if (!this.enabled) {
        double nextDoubleValue = Math.exp(-9.0 * currentDoubleValue);
        double previousDoubleValue = (1.0 - nextDoubleValue) / 9.0;
        this.value2 = this.value2 + this.value5 * previousDoubleValue;
        this.value4 = this.value4 + this.value6 * previousDoubleValue;
        this.value5 *= nextDoubleValue;
        this.value6 *= nextDoubleValue;
        if (Math.hypot(this.value5, this.value6) < 0.01) {
          this.value5 = this.value6 = 0.0;
        }
      }

      if (this.vector3d != null) {
        Vector3d currentVector3d =
            this.frame()
                .ray(this.value9 * this.value7, this.value10 * this.value8)
                .onPlane(this.vector3d.y);
        if (currentVector3d != null) {
          this.value2 = this.value2 + (this.vector3d.x - currentVector3d.x);
          this.value4 = this.value4 + (this.vector3d.z - currentVector3d.z);
        }

        if (this.spring.settled() && this.spring4.settled()) {
          this.vector3d = null;
        }
      }

      this.updateState();
    }
  }

  public boolean moving() {
    return this.enabled
        || this.spring6 != null
        || !this.spring.settled()
        || !this.spring2.settled()
        || !this.spring3.settled()
        || !this.spring4.settled()
        || !this.spring5.settled()
        || Math.hypot(this.value5, this.value6) >= 0.01;
  }

  public double headingDegrees() {
    return Math.toDegrees(Math.IEEEremainder(this.spring2.value, 6.283185307179586));
  }

  private void updateState() {
    this.value2 = Math.clamp(this.value2, -2.9999984E7, 2.9999984E7);
    this.value4 = Math.clamp(this.value4, -2.9999984E7, 2.9999984E7);
  }

  public record Frame(
      Vector3d focus,
      Vector3d eye,
      Matrix4f viewProjection,
      Matrix4f inverse,
      float width,
      float height,
      double distance,
      Matrix4f view,
      Matrix4f projection) {
    public TerrainViewportService.Ray ray(float value, float currentValue) {
      float nextValue = 2.0F * value / this.width - 1.0F;
      float previousValue = 1.0F - 2.0F * currentValue / this.height;
      Vector4f vector4f =
          this.inverse.transform(new Vector4f(nextValue, previousValue, -1.0F, 1.0F));
      Vector4f currentVector4f =
          this.inverse.transform(new Vector4f(nextValue, previousValue, 1.0F, 1.0F));
      vector4f.div(vector4f.w);
      currentVector4f.div(currentVector4f.w);
      Vector3d vector3d = new Vector3d(vector4f.x, vector4f.y, vector4f.z).add(this.focus);
      Vector3d currentVector3d =
          new Vector3d(
                  currentVector4f.x - vector4f.x,
                  currentVector4f.y - vector4f.y,
                  currentVector4f.z - vector4f.z)
              .normalize();
      return new TerrainViewportService.Ray(vector3d, currentVector3d);
    }

    public Vector3d project(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      Vector4f vector4f =
          this.viewProjection.transform(
              new Vector4f(
                  (float) (doubleValue - this.focus.x),
                  (float) (currentDoubleValue - this.focus.y),
                  (float) (nextDoubleValue - this.focus.z),
                  1.0F));
      if (vector4f.isFinite() && !(vector4f.w <= 0.001F)) {
        vector4f.div(vector4f.w);
        return !(vector4f.x < -1.0F)
                && !(vector4f.x > 1.0F)
                && !(vector4f.y < -1.0F)
                && !(vector4f.y > 1.0F)
                && !(vector4f.z < -1.0F)
                && !(vector4f.z > 1.0F)
            ? new Vector3d(
                (vector4f.x + 1.0F) * this.width / 2.0F,
                (1.0F - vector4f.y) * this.height / 2.0F,
                vector4f.z)
            : null;
      } else {
        return null;
      }
    }
  }

  public record Ray(Vector3d origin, Vector3d direction) {
    public Vector3d at(double doubleValue) {
      return new Vector3d(this.direction).mul(doubleValue).add(this.origin);
    }

    public Vector3d onPlane(double doubleValue) {
      if (Math.abs(this.direction.y) < 1.0E-8) {
        return null;
      }

      double currentDoubleValue = (doubleValue - this.origin.y) / this.direction.y;
      return currentDoubleValue >= 0.0 ? this.at(currentDoubleValue) : null;
    }
  }

  private static final class Spring {
    double value;
    double target;
    double velocity;

    Spring(double doubleValue) {
      this.snap(doubleValue);
    }

    void snap(double doubleValue) {
      this.value = this.target = doubleValue;
      this.velocity = 0.0;
    }

    boolean settled() {
      return Math.abs(this.value - this.target) < 1.0E-6 && Math.abs(this.velocity) < 1.0E-5;
    }

    void advance(double doubleValue, double currentDoubleValue) {
      double nextDoubleValue = this.value - this.target;
      double previousDoubleValue = this.velocity + currentDoubleValue * nextDoubleValue;
      double sourceDoubleValue = Math.exp(-currentDoubleValue * doubleValue);
      this.value =
          this.target + (nextDoubleValue + previousDoubleValue * doubleValue) * sourceDoubleValue;
      this.velocity =
          (this.velocity - currentDoubleValue * previousDoubleValue * doubleValue)
              * sourceDoubleValue;
      if (this.settled()) {
        this.snap(this.target);
      }
    }
  }
}
