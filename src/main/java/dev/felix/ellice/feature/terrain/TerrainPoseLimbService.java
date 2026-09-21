package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.feature.cape.CapePlayerService;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TerrainPoseLimbService {
  public static final float MODEL_UNITS = 32.0F;
  public static final float HEIGHT_BLOCKS = 1.8F;
  public static final float WORLD_SCALE = 0.05625F;
  private static final int count = 36;

  private TerrainPoseLimbService() {}

  public static GeometryXService poseLimb(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      float value,
      float currentValue,
      float nextValue,
      TerrainPoseLimbService.Limb limb) {
    Quaternionf quaternionf =
        new Quaternionf().rotateY((float) (3.141592653589793 - Math.toRadians(value)));
    Quaternionf currentQuaternionf =
        new Quaternionf(quaternionf).mul(new Quaternionf().rotateX(currentValue));
    Vector3f vector3f =
        new Vector3f(limb.pivotX, limb.pivotY, limb.pivotZ).mul(0.05625F).rotate(quaternionf);
    return new GeometryXService(
        doubleValue + vector3f.x,
        currentDoubleValue - nextValue + vector3f.y,
        nextDoubleValue + vector3f.z,
        currentQuaternionf,
        new Vector3f(0.05625F));
  }

  public static GeometryXService poseTorsoChild(
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      TerrainPoseLimbService.Limb limb) {
    if (limb != TerrainPoseLimbService.Limb.HEAD
        && limb != TerrainPoseLimbService.Limb.ARM_LEFT
        && limb != TerrainPoseLimbService.Limb.ARM_RIGHT) {
      throw new IllegalArgumentException("Only head and arms ride the torso: " + limb);
    }

    Quaternionf quaternionf =
        new Quaternionf().rotateY((float) (3.141592653589793 - Math.toRadians(value)));
    Quaternionf currentQuaternionf = new Quaternionf().rotateX(currentValue);
    Quaternionf nextQuaternionf =
        new Quaternionf(quaternionf)
            .mul(currentQuaternionf)
            .mul(new Quaternionf().rotateX(nextValue));
    Vector3f vector3f =
        new Vector3f(
                TerrainPoseLimbService.Limb.TORSO.pivotX,
                TerrainPoseLimbService.Limb.TORSO.pivotY,
                TerrainPoseLimbService.Limb.TORSO.pivotZ)
            .mul(0.05625F);
    Vector3f currentVector3f =
        new Vector3f(
                limb.pivotX - TerrainPoseLimbService.Limb.TORSO.pivotX,
                limb.pivotY - TerrainPoseLimbService.Limb.TORSO.pivotY,
                limb.pivotZ - TerrainPoseLimbService.Limb.TORSO.pivotZ)
            .mul(0.05625F);
    Vector3f nextVector3f =
        new Vector3f(vector3f)
            .rotate(quaternionf)
            .add(
                new Vector3f(currentVector3f)
                    .rotate(new Quaternionf(quaternionf).mul(currentQuaternionf)));
    return new GeometryXService(
        doubleValue + nextVector3f.x,
        currentDoubleValue - previousValue + nextVector3f.y,
        nextDoubleValue + nextVector3f.z,
        nextQuaternionf,
        new Vector3f(0.05625F));
  }

  public static GeometryVertexCountService playerMesh(boolean enabled, boolean currentEnabled) {
    float[] floats = CapePlayerService.player(enabled, currentEnabled);
    int value = currentEnabled ? 7 : 12;
    return createGeometryVertexCountService(floats, 0, value * 36, 0.0F, 0.0F, 0.0F);
  }

  public static TerrainPoseLimbService.LimbMesh limbMesh(
      boolean enabled, boolean currentEnabled, TerrainPoseLimbService.Limb limb) {
    float[] floats = CapePlayerService.player(enabled, currentEnabled);
    int value = limb.baseBox * 36;
    float[] currentFloats = createFloat2(floats, value, 36, limb.pivotX, limb.pivotY, limb.pivotZ);
    float[] nextFloats = null;
    if (!currentEnabled || limb == TerrainPoseLimbService.Limb.HEAD) {
      int currentValue = limb.overlayBox * 36;
      if (currentValue + 36 <= floats.length / 8) {
        nextFloats = createFloat2(floats, currentValue, 36, limb.pivotX, limb.pivotY, limb.pivotZ);
      }
    }

    float[] previousFloats =
        nextFloats == null ? currentFloats : createFloat(currentFloats, nextFloats);
    int index = previousFloats.length / 12;
    int[] ints = new int[index];
    int currentIndex = 0;

    while (currentIndex < index) {
      ints[currentIndex] = currentIndex++;
    }

    return new TerrainPoseLimbService.LimbMesh(
        new GeometryVertexCountService(previousFloats, ints),
        limb.pivotX,
        limb.pivotY,
        limb.pivotZ);
  }

  private static GeometryVertexCountService createGeometryVertexCountService(
      float[] floats,
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue) {
    float[] currentFloats =
        createFloat2(floats, value, currentValue, nextValue, previousValue, sourceValue);
    int index = currentFloats.length / 12;
    int[] ints = new int[index];
    int currentIndex = 0;

    while (currentIndex < index) {
      ints[currentIndex] = currentIndex++;
    }

    return new GeometryVertexCountService(currentFloats, ints);
  }

  private static float[] createFloat(float[] floats, float[] currentFloats) {
    float[] currentLength = new float[floats.length + currentFloats.length];
    System.arraycopy(floats, 0, currentLength, 0, floats.length);
    System.arraycopy(currentFloats, 0, currentLength, floats.length, currentFloats.length);
    return currentLength;
  }

  private static float[] createFloat2(
      float[] floats,
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue) {
    byte byteValue = 8;
    float[] currentFloats = new float[currentValue * 12];

    for (int index = 0; index < currentValue; index++) {
      int targetValue = index % 3;
      int currentIndex =
          (value + index - targetValue + (targetValue == 0 ? 0 : (targetValue == 1 ? 2 : 1))) * 8;
      int nextIndex = index * 12;
      float inputValue = floats[currentIndex + 3];
      float outputValue = floats[currentIndex + 4];
      float resultValue = floats[currentIndex + 5];
      currentFloats[nextIndex] = floats[currentIndex] - nextValue;
      currentFloats[nextIndex + 1] = floats[currentIndex + 1] - previousValue;
      currentFloats[nextIndex + 2] = floats[currentIndex + 2] - sourceValue;
      currentFloats[nextIndex + 3] = inputValue;
      currentFloats[nextIndex + 4] = outputValue;
      currentFloats[nextIndex + 5] = resultValue;
      currentFloats[nextIndex + 6] = floats[currentIndex + 6];
      currentFloats[nextIndex + 7] = floats[currentIndex + 7];
      float candidateValue = -resultValue;
      float selectedValue = 0.0F;
      float defaultValue = inputValue;
      float initialValue =
          (float) Math.sqrt(candidateValue * candidateValue + defaultValue * defaultValue);
      if (initialValue < 1.0E-6F) {
        candidateValue = 1.0F;
        selectedValue = 0.0F;
        defaultValue = 0.0F;
        initialValue = 1.0F;
      }

      currentFloats[nextIndex + 8] = candidateValue / initialValue;
      currentFloats[nextIndex + 9] = selectedValue / initialValue;
      currentFloats[nextIndex + 10] = defaultValue / initialValue;
      currentFloats[nextIndex + 11] = 1.0F;
    }

    return currentFloats;
  }

  public enum Limb {
    HEAD(0, 6, 0.0F, 24.0F, 0.0F),
    TORSO(1, 7, 0.0F, 12.0F, 0.0F),
    ARM_RIGHT(2, 8, -5.0F, 22.0F, 0.0F),
    ARM_LEFT(3, 9, 5.0F, 22.0F, 0.0F),
    LEG_RIGHT(4, 10, -2.0F, 12.0F, 0.0F),
    LEG_LEFT(5, 11, 2.0F, 12.0F, 0.0F);

    final int baseBox;
    final int overlayBox;
    final float pivotX;
    final float pivotY;
    final float pivotZ;

    Limb(int value, int currentValue, float nextValue, float previousValue, float sourceValue) {
      this.baseBox = value;
      this.overlayBox = currentValue;
      this.pivotX = nextValue;
      this.pivotY = previousValue;
      this.pivotZ = sourceValue;
    }

    private static TerrainPoseLimbService.Limb[] $values() {
      return new TerrainPoseLimbService.Limb[] {
        HEAD, TORSO, ARM_RIGHT, ARM_LEFT, LEG_RIGHT, LEG_LEFT
      };
    }
  }

  public record LimbMesh(
      GeometryVertexCountService mesh, float pivotX, float pivotY, float pivotZ) {}
}
