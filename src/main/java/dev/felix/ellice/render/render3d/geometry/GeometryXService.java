package dev.felix.ellice.render.render3d.geometry;

import java.util.Objects;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class GeometryXService {
  public static final GeometryXService IDENTITY = new GeometryXService();
  private static final double value = 1.0E-20;
  private final double value2;
  private final double value3;
  private final double value4;
  private final Quaternionf quaternionf;
  private final Vector3f vector3f;

  public GeometryXService() {
    this(0.0, 0.0, 0.0, new Quaternionf(), new Vector3f(1.0F));
  }

  public GeometryXService(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    this(doubleValue, currentDoubleValue, nextDoubleValue, new Quaternionf(), new Vector3f(1.0F));
  }

  public GeometryXService(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      Quaternionf quaternionf,
      Vector3f vector3f) {
    this(
        doubleValue,
        currentDoubleValue,
        nextDoubleValue,
        (Quaternionfc) quaternionf,
        (Vector3fc) vector3f);
  }

  public GeometryXService(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      Quaternionfc quaternionfc,
      Vector3fc vector3fc) {
    updateState(doubleValue, "x");
    updateState(currentDoubleValue, "y");
    updateState(nextDoubleValue, "z");
    Objects.requireNonNull(quaternionfc, "rotation");
    Objects.requireNonNull(vector3fc, "scale");
    updateState2(quaternionfc.x(), "rotation.x");
    updateState2(quaternionfc.y(), "rotation.y");
    updateState2(quaternionfc.z(), "rotation.z");
    updateState2(quaternionfc.w(), "rotation.w");
    double currentX =
        (double) quaternionfc.x() * quaternionfc.x()
            + (double) quaternionfc.y() * quaternionfc.y()
            + (double) quaternionfc.z() * quaternionfc.z()
            + (double) quaternionfc.w() * quaternionfc.w();
    if (currentX <= 1.0E-20) {
      throw new IllegalArgumentException("rotation must be a non-zero quaternion");
    }

    updateState3(vector3fc.x(), "scale.x");
    updateState3(vector3fc.y(), "scale.y");
    updateState3(vector3fc.z(), "scale.z");
    this.value2 = doubleValue;
    this.value3 = currentDoubleValue;
    this.value4 = nextDoubleValue;
    this.quaternionf = new Quaternionf(quaternionfc).normalize();
    this.vector3f = new Vector3f(vector3fc);
  }

  public double x() {
    return this.value2;
  }

  public double y() {
    return this.value3;
  }

  public double z() {
    return this.value4;
  }

  public Vector3d position() {
    return new Vector3d(this.value2, this.value3, this.value4);
  }

  public Vector3d position(Vector3d vector3d) {
    return Objects.requireNonNull(vector3d, "destination")
        .set(this.value2, this.value3, this.value4);
  }

  public Quaternionf rotation() {
    return new Quaternionf(this.quaternionf);
  }

  public Quaternionf rotation(Quaternionf currentQuaternionf) {
    return Objects.requireNonNull(currentQuaternionf, "destination").set(this.quaternionf);
  }

  public Vector3f scale() {
    return new Vector3f(this.vector3f);
  }

  public Vector3f scale(Vector3f currentVector3f) {
    return Objects.requireNonNull(currentVector3f, "destination").set(this.vector3f);
  }

  public GeometryXService withPosition(double x, double y, double width) {
    return new GeometryXService(x, y, width, this.quaternionf, this.vector3f);
  }

  public GeometryXService translated(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    updateState(doubleValue, "deltaX");
    updateState(currentDoubleValue, "deltaY");
    updateState(nextDoubleValue, "deltaZ");
    return this.withPosition(
        this.value2 + doubleValue, this.value3 + currentDoubleValue, this.value4 + nextDoubleValue);
  }

  public GeometryXService withRotation(Quaternionfc quaternionfc) {
    return new GeometryXService(this.value2, this.value3, this.value4, quaternionfc, this.vector3f);
  }

  public GeometryXService withScale(Vector3fc vector3fc) {
    return new GeometryXService(this.value2, this.value3, this.value4, this.quaternionf, vector3fc);
  }

  public Matrix4f modelMatrixRelativeTo(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return this.modelMatrixRelativeTo(
        doubleValue, currentDoubleValue, nextDoubleValue, new Matrix4f());
  }

  public Matrix4f modelMatrixRelativeTo(
      double doubleValue, double currentDoubleValue, double nextDoubleValue, Matrix4f matrix4f) {
    Objects.requireNonNull(matrix4f, "destination");
    float value = calculateValue(this.value2, doubleValue, "x");
    float currentValue = calculateValue(this.value3, currentDoubleValue, "y");
    float nextValue = calculateValue(this.value4, nextDoubleValue, "z");
    return matrix4f
        .identity()
        .translate(value, currentValue, nextValue)
        .rotate(this.quaternionf)
        .scale(this.vector3f);
  }

  public Matrix4f relativeModelMatrix(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return this.modelMatrixRelativeTo(doubleValue, currentDoubleValue, nextDoubleValue);
  }

  public Matrix3f normalMatrix() {
    return this.normalMatrix(new Matrix3f());
  }

  public Matrix3f normalMatrix(Matrix3f matrix3f) {
    Objects.requireNonNull(matrix3f, "destination");
    return matrix3f
        .identity()
        .rotate(this.quaternionf)
        .scale(1.0F / this.vector3f.x(), 1.0F / this.vector3f.y(), 1.0F / this.vector3f.z());
  }

  private static float calculateValue(double doubleValue, double currentDoubleValue, String text) {
    updateState(currentDoubleValue, "origin" + text.toUpperCase());
    double nextDoubleValue = doubleValue - currentDoubleValue;
    if (Double.isFinite(nextDoubleValue)
        && !(nextDoubleValue < -3.4028234663852886E38)
        && !(nextDoubleValue > 3.4028234663852886E38)) {
      return (float) nextDoubleValue;
    } else {
      throw new IllegalArgumentException(
          "relative " + text + " translation is outside the finite float range");
    }
  }

  private static void updateState(double doubleValue, String text) {
    if (!Double.isFinite(doubleValue)) {
      throw new IllegalArgumentException(text + " must be finite");
    }
  }

  private static void updateState2(float value, String text) {
    if (!Float.isFinite(value)) {
      throw new IllegalArgumentException(text + " must be finite");
    }
  }

  private static void updateState3(float value, String text) {
    updateState2(value, text);
    if (value <= 0.0F) {
      throw new IllegalArgumentException(
          text + " must be positive; mirror geometry in the mesh instead");
    }
  }

  @Override
  public boolean equals(Object value) {
    if (this == value) {
      return true;
    } else {
      return !(value instanceof GeometryXService geometryX)
          ? false
          : Double.compare(this.value2, geometryX.value2) == 0
              && Double.compare(this.value3, geometryX.value3) == 0
              && Double.compare(this.value4, geometryX.value4) == 0
              && this.quaternionf.equals(geometryX.quaternionf)
              && this.vector3f.equals(geometryX.vector3f);
    }
  }

  @Override
  public int hashCode() {
    int value = Double.hashCode(this.value2);
    value = 31 * value + Double.hashCode(this.value3);
    value = 31 * value + Double.hashCode(this.value4);
    value = 31 * value + this.quaternionf.hashCode();
    return 31 * value + this.vector3f.hashCode();
  }

  @Override
  public String toString() {
    return "Transform3D[position=("
        + this.value2
        + ", "
        + this.value3
        + ", "
        + this.value4
        + "), rotation="
        + this.quaternionf
        + ", scale="
        + this.vector3f
        + "]";
  }
}
