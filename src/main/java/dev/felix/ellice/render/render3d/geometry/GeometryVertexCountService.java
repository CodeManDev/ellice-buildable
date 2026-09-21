package dev.felix.ellice.render.render3d.geometry;

import java.util.Arrays;
import java.util.Objects;
import org.joml.Vector3f;

public final class GeometryVertexCountService {
  public static final int POSITION_OFFSET_FLOATS = 0;
  public static final int NORMAL_OFFSET_FLOATS = 3;
  public static final int UV_OFFSET_FLOATS = 6;
  public static final int TANGENT_OFFSET_FLOATS = 8;
  public static final int VERTEX_STRIDE_FLOATS = 12;
  public static final int VERTEX_STRIDE_BYTES = 48;
  private static final double value = 0.002;
  private static final double value2 = 0.002;
  private static final double value3 = 1.0E-4;
  private final float[] float2;
  private final int[] int2;
  private final int count;
  private final GeometryVertexCountService.BoundingSphere boundingSphere2;

  public GeometryVertexCountService(float[] floats, int[] ints) {
    Objects.requireNonNull(floats, "vertices");
    Objects.requireNonNull(ints, "indices");
    this.float2 = (float[]) floats.clone();
    this.int2 = (int[]) ints.clone();
    this.count = calculateValue(this.float2, this.int2);
    this.boundingSphere2 = createBoundingSphere(this.float2, this.count);
  }

  public int vertexCount() {
    return this.count;
  }

  public int indexCount() {
    return this.int2.length;
  }

  public int triangleCount() {
    return this.int2.length / 3;
  }

  public int vertexStrideFloats() {
    return 12;
  }

  public int vertexStrideBytes() {
    return 48;
  }

  public float[] vertices() {
    return (float[]) this.float2.clone();
  }

  public int[] indices() {
    return (int[]) this.int2.clone();
  }

  public float[] vertexData() {
    return this.vertices();
  }

  public int[] indexData() {
    return this.indices();
  }

  public void copyVerticesTo(float[] floats, int value) {
    Objects.requireNonNull(floats, "destination");
    Objects.checkFromIndexSize(value, this.float2.length, floats.length);
    System.arraycopy(this.float2, 0, floats, value, this.float2.length);
  }

  public void copyIndicesTo(int[] ints, int value) {
    Objects.requireNonNull(ints, "destination");
    Objects.checkFromIndexSize(value, this.int2.length, ints.length);
    System.arraycopy(this.int2, 0, ints, value, this.int2.length);
  }

  public GeometryVertexCountService.BoundingSphere boundingSphere() {
    return this.boundingSphere2;
  }

  public GeometryVertexCountService.BoundingSphere bounds() {
    return this.boundingSphere2;
  }

  private static int calculateValue(float[] floats, int[] ints) {
    if (floats.length != 0 && floats.length % 12 == 0) {
      int currentIndex = floats.length / 12;
      if (currentIndex < 3) {
        throw new IllegalArgumentException("a triangle mesh requires at least 3 vertices");
      }

      if (ints.length >= 3 && ints.length % 3 == 0) {
        for (int nextIndex = 0; nextIndex < currentIndex; nextIndex++) {
          updateState(floats, nextIndex);
        }

        boolean[] booleans = new boolean[currentIndex];

        for (byte previousIndex = 0; previousIndex < ints.length; previousIndex += 3) {
          int sourceIndex = ints[previousIndex];
          int targetIndex = ints[previousIndex + 1];
          int inputIndex = ints[previousIndex + 2];
          updateState2(sourceIndex, currentIndex, previousIndex);
          updateState2(targetIndex, currentIndex, previousIndex + 1);
          updateState2(inputIndex, currentIndex, previousIndex + 2);
          if (sourceIndex == targetIndex
              || targetIndex == inputIndex
              || inputIndex == sourceIndex) {
            throw new IllegalArgumentException(
                "triangle " + previousIndex / 3 + " repeats a vertex index");
          }

          booleans[sourceIndex] = true;
          booleans[targetIndex] = true;
          booleans[inputIndex] = true;
          updateState3(floats, sourceIndex, targetIndex, inputIndex, previousIndex / 3);
        }

        for (int outputIndex = 0; outputIndex < booleans.length; outputIndex++) {
          if (!booleans[outputIndex]) {
            throw new IllegalArgumentException("vertex " + outputIndex + " is not indexed");
          }
        }

        return currentIndex;
      } else {
        throw new IllegalArgumentException(
            "index data must contain a non-zero whole number of triangles");
      }
    } else {
      throw new IllegalArgumentException(
          "vertex data must contain a non-zero whole number of 12-float vertices");
    }
  }

  private static void updateState(float[] floats, int value) {
    int currentValue = value * 12;

    for (int index = 0; index < 12; index++) {
      if (!Float.isFinite(floats[currentValue + index])) {
        throw new IllegalArgumentException(
            "vertex " + value + " component " + index + " must be finite");
      }
    }

    float nextValue = floats[currentValue + 3];
    float previousValue = floats[currentValue + 3 + 1];
    float sourceValue = floats[currentValue + 3 + 2];
    double doubleValue = calculateValue2(nextValue, previousValue, sourceValue);
    if (Math.abs(doubleValue - 1.0) > 0.002) {
      throw new IllegalArgumentException("vertex " + value + " normal must be unit length");
    }

    float targetValue = floats[currentValue + 8];
    float inputValue = floats[currentValue + 8 + 1];
    float outputValue = floats[currentValue + 8 + 2];
    float resultValue = floats[currentValue + 8 + 3];
    double currentDoubleValue = calculateValue2(targetValue, inputValue, outputValue);
    if (Math.abs(currentDoubleValue - 1.0) > 0.002) {
      throw new IllegalArgumentException("vertex " + value + " tangent must be unit length");
    }

    double nextDoubleValue =
        (double) nextValue * targetValue
            + (double) previousValue * inputValue
            + (double) sourceValue * outputValue;
    if (Math.abs(nextDoubleValue) > 0.002) {
      throw new IllegalArgumentException(
          "vertex " + value + " tangent must be orthogonal to its normal");
    }

    if (Math.abs(Math.abs(resultValue) - 1.0) > 1.0E-4) {
      throw new IllegalArgumentException(
          "vertex " + value + " tangent handedness must be -1 or +1");
    }
  }

  private static void updateState2(int value, int currentValue, int nextValue) {
    if (value < 0 || value >= currentValue) {
      throw new IllegalArgumentException(
          "index "
              + nextValue
              + " references vertex "
              + value
              + " outside [0, "
              + currentValue
              + ")");
    }
  }

  private static void updateState3(
      float[] floats, int value, int currentValue, int nextValue, int previousValue) {
    int index = value * 12;
    int currentIndex = currentValue * 12;
    int nextIndex = nextValue * 12;
    double doubleValue = floats[currentIndex] - floats[index];
    double currentDoubleValue = floats[currentIndex + 1] - floats[index + 1];
    double nextDoubleValue = floats[currentIndex + 2] - floats[index + 2];
    double previousDoubleValue = floats[nextIndex] - floats[index];
    double sourceDoubleValue = floats[nextIndex + 1] - floats[index + 1];
    double targetDoubleValue = floats[nextIndex + 2] - floats[index + 2];
    double inputDoubleValue =
        currentDoubleValue * targetDoubleValue - nextDoubleValue * sourceDoubleValue;
    double outputDoubleValue =
        nextDoubleValue * previousDoubleValue - doubleValue * targetDoubleValue;
    double resultDoubleValue =
        doubleValue * sourceDoubleValue - currentDoubleValue * previousDoubleValue;
    double candidateDoubleValue =
        inputDoubleValue * inputDoubleValue
            + outputDoubleValue * outputDoubleValue
            + resultDoubleValue * resultDoubleValue;
    if (Double.isFinite(candidateDoubleValue) && !(candidateDoubleValue <= 0.0)) {
      double selectedDoubleValue =
          floats[index + 3] + floats[currentIndex + 3] + floats[nextIndex + 3];
      double defaultDoubleValue =
          floats[index + 3 + 1] + floats[currentIndex + 3 + 1] + floats[nextIndex + 3 + 1];
      double initialDoubleValue =
          floats[index + 3 + 2] + floats[currentIndex + 3 + 2] + floats[nextIndex + 3 + 2];
      double resolvedDoubleValue =
          inputDoubleValue * selectedDoubleValue
              + outputDoubleValue * defaultDoubleValue
              + resultDoubleValue * initialDoubleValue;
      if (!(resolvedDoubleValue > 0.0)) {
        throw new IllegalArgumentException(
            "triangle " + previousValue + " winding disagrees with its vertex normals");
      }
    } else {
      throw new IllegalArgumentException("triangle " + previousValue + " has zero area");
    }
  }

  private static double calculateValue2(float value, float currentValue, float nextValue) {
    return (double) value * value
        + (double) currentValue * currentValue
        + (double) nextValue * nextValue;
  }

  private static GeometryVertexCountService.BoundingSphere createBoundingSphere(
      float[] floats, int value) {
    float currentValue = Float.POSITIVE_INFINITY;
    float nextValue = Float.POSITIVE_INFINITY;
    float previousValue = Float.POSITIVE_INFINITY;
    float sourceValue = Float.NEGATIVE_INFINITY;
    float targetValue = Float.NEGATIVE_INFINITY;
    float inputValue = Float.NEGATIVE_INFINITY;

    for (int index = 0; index < value; index++) {
      int currentIndex = index * 12;
      float outputValue = floats[currentIndex];
      float resultValue = floats[currentIndex + 1];
      float candidateValue = floats[currentIndex + 2];
      currentValue = Math.min(currentValue, outputValue);
      nextValue = Math.min(nextValue, resultValue);
      previousValue = Math.min(previousValue, candidateValue);
      sourceValue = Math.max(sourceValue, outputValue);
      targetValue = Math.max(targetValue, resultValue);
      inputValue = Math.max(inputValue, candidateValue);
    }

    float selectedValue = (float) (currentValue + ((double) sourceValue - currentValue) * 0.5);
    float defaultValue = (float) (nextValue + ((double) targetValue - nextValue) * 0.5);
    float initialValue = (float) (previousValue + ((double) inputValue - previousValue) * 0.5);
    double doubleValue = 0.0;

    for (int nextIndex = 0; nextIndex < value; nextIndex++) {
      int previousIndex = nextIndex * 12;
      double currentDoubleValue = (double) floats[previousIndex] - selectedValue;
      double nextDoubleValue = (double) floats[previousIndex + 1] - defaultValue;
      double previousDoubleValue = (double) floats[previousIndex + 2] - initialValue;
      doubleValue =
          Math.max(
              doubleValue,
              currentDoubleValue * currentDoubleValue
                  + nextDoubleValue * nextDoubleValue
                  + previousDoubleValue * previousDoubleValue);
    }

    float resolvedValue = (float) Math.sqrt(doubleValue);
    if (!Float.isFinite(resolvedValue)) {
      throw new IllegalArgumentException("mesh bounds exceed the finite float range");
    }

    if (resolvedValue > 0.0F) {
      resolvedValue = Math.nextUp(resolvedValue);
    }

    return new GeometryVertexCountService.BoundingSphere(
        selectedValue, defaultValue, initialValue, resolvedValue);
  }

  @Override
  public boolean equals(Object value) {
    if (this == value) {
      return true;
    } else {
      return !(value instanceof GeometryVertexCountService geometryVertexCount)
          ? false
          : Arrays.equals(this.float2, geometryVertexCount.float2)
              && Arrays.equals(this.int2, geometryVertexCount.int2);
    }
  }

  @Override
  public int hashCode() {
    return 31 * Arrays.hashCode(this.float2) + Arrays.hashCode(this.int2);
  }

  @Override
  public String toString() {
    return "MeshData[vertices="
        + this.count
        + ", triangles="
        + this.triangleCount()
        + ", bounds="
        + this.boundingSphere2
        + "]";
  }

  public static final class BoundingSphere {
    private final float value4;
    private final float value5;
    private final float value6;
    private final float value7;

    private BoundingSphere(float value, float currentValue, float nextValue, float previousValue) {
      this.value4 = value;
      this.value5 = currentValue;
      this.value6 = nextValue;
      this.value7 = previousValue;
    }

    public float centerX() {
      return this.value4;
    }

    public float centerY() {
      return this.value5;
    }

    public float centerZ() {
      return this.value6;
    }

    public Vector3f center() {
      return new Vector3f(this.value4, this.value5, this.value6);
    }

    public Vector3f center(Vector3f vector3f) {
      return Objects.requireNonNull(vector3f, "destination")
          .set(this.value4, this.value5, this.value6);
    }

    public float radius() {
      return this.value7;
    }

    public boolean contains(float value, float currentValue, float nextValue) {
      double doubleValue = (double) value - this.value4;
      double currentDoubleValue = (double) currentValue - this.value5;
      double nextDoubleValue = (double) nextValue - this.value6;
      return doubleValue * doubleValue
              + currentDoubleValue * currentDoubleValue
              + nextDoubleValue * nextDoubleValue
          <= (double) this.value7 * this.value7;
    }

    @Override
    public boolean equals(Object value) {
      if (this == value) {
        return true;
      } else {
        return !(value instanceof GeometryVertexCountService.BoundingSphere boundingSphere)
            ? false
            : Float.compare(this.value4, boundingSphere.value4) == 0
                && Float.compare(this.value5, boundingSphere.value5) == 0
                && Float.compare(this.value6, boundingSphere.value6) == 0
                && Float.compare(this.value7, boundingSphere.value7) == 0;
      }
    }

    @Override
    public int hashCode() {
      int value = Float.hashCode(this.value4);
      value = 31 * value + Float.hashCode(this.value5);
      value = 31 * value + Float.hashCode(this.value6);
      return 31 * value + Float.hashCode(this.value7);
    }

    @Override
    public String toString() {
      return "BoundingSphere[center=("
          + this.value4
          + ", "
          + this.value5
          + ", "
          + this.value6
          + "), radius="
          + this.value7
          + "]";
    }
  }
}
