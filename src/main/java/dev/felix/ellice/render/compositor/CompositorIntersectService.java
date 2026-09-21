package dev.felix.ellice.render.compositor;

import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import java.util.Arrays;

public final class CompositorIntersectService {
  public static final int MAX_ROUNDED_MASKS = 8;
  private static final int count = 0;
  private static final int count2 = 1;
  private static final int count3 = 2;
  private static final int count4 = 3;
  private static final int count5 = 4;
  private static final int count6 = 5;
  private static final int count7 = 8;
  private static final int count8 = 69;

  private CompositorIntersectService() {}

  public static float[] intersect(
      float[] floats,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue) {
    value = calculateValue(value);
    currentValue = calculateValue(currentValue);
    nextValue = calculateValue2(nextValue);
    previousValue = calculateValue2(previousValue);
    float[] currentFloats = new float[69];
    if (floats == null) {
      currentFloats[0] = value;
      currentFloats[1] = currentValue;
      currentFloats[2] = nextValue;
      currentFloats[3] = previousValue;
    } else {
      float resultValue = Math.max(value, floats[0]);
      float candidateValue = Math.max(currentValue, floats[1]);
      float selectedValue = Math.min(value + nextValue, floats[0] + floats[2]);
      float defaultValue = Math.min(currentValue + previousValue, floats[1] + floats[3]);
      currentFloats[0] = resultValue;
      currentFloats[1] = candidateValue;
      currentFloats[2] = Math.max(0.0F, selectedValue - resultValue);
      currentFloats[3] = Math.max(0.0F, defaultValue - candidateValue);
      int initialValue = roundedMaskCount(floats);
      currentFloats[4] = initialValue;
      if (initialValue > 0) {
        System.arraycopy(floats, 5, currentFloats, 5, initialValue * 8);
      }
    }

    float resolvedValue = calculateValue2(sourceValue);
    float computedValue = calculateValue2(targetValue);
    float cachedValue = calculateValue2(inputValue);
    float pendingValue = calculateValue2(outputValue);
    if (nextValue > 0.0F
        && previousValue > 0.0F
        && Math.max(Math.max(resolvedValue, computedValue), Math.max(cachedValue, pendingValue))
            > 0.0F) {
      updateState(
          currentFloats,
          value,
          currentValue,
          nextValue,
          previousValue,
          resolvedValue,
          computedValue,
          cachedValue,
          pendingValue);
    }

    return currentFloats;
  }

  public static void apply(
      RhiCommandBuffer rhiCommandBuffer, float[] floats, float value, int currentValue) {
    float nextValue = Float.isFinite(value) ? Math.max(0.0F, value) : 0.0F;
    if (floats == null) {
      rhiCommandBuffer.pushInt("uClipCount", 0);
    } else {
      int previousValue = calculateValue3(floats[0] * nextValue);
      int sourceValue = calculateValue4((floats[0] + floats[2]) * nextValue);
      int targetValue = calculateValue3(floats[1] * nextValue);
      int inputValue = calculateValue4((floats[1] + floats[3]) * nextValue);
      rhiCommandBuffer.setScissor(
          previousValue,
          currentValue - inputValue,
          Math.max(0, sourceValue - previousValue),
          Math.max(0, inputValue - targetValue));
      int outputValue = roundedMaskCount(floats);
      rhiCommandBuffer.pushInt("uClipCount", outputValue);

      for (int index = 0; index < outputValue; index++) {
        int currentIndex = 5 + index * 8;
        rhiCommandBuffer.pushVec4(
            "uClipRects[" + index + "]",
            floats[currentIndex] * nextValue,
            floats[currentIndex + 1] * nextValue,
            floats[currentIndex + 2] * nextValue,
            floats[currentIndex + 3] * nextValue);
        rhiCommandBuffer.pushVec4(
            "uClipRadii[" + index + "]",
            floats[currentIndex + 4] * nextValue,
            floats[currentIndex + 5] * nextValue,
            floats[currentIndex + 6] * nextValue,
            floats[currentIndex + 7] * nextValue);
      }
    }
  }

  public static void clear(RhiCommandBuffer rhiCommandBuffer, float[] floats) {
    if (floats != null) {
      rhiCommandBuffer.clearScissor();
    }
  }

  public static boolean same(float[] floats, float[] currentFloats) {
    return floats == currentFloats
        || floats != null && currentFloats != null && Arrays.equals(floats, currentFloats);
  }

  public static int roundedMaskCount(float[] floats) {
    return floats != null && floats.length > 4
        ? Math.max(0, Math.min(8, Math.round(floats[4])))
        : 0;
  }

  static float scissorX(float[] floats) {
    return floats[0];
  }

  static float scissorY(float[] floats) {
    return floats[1];
  }

  static float scissorWidth(float[] floats) {
    return floats[2];
  }

  static float scissorHeight(float[] floats) {
    return floats[3];
  }

  static float maskValue(float[] floats, int value, int currentValue) {
    return floats[5 + value * 8 + currentValue];
  }

  private static void updateState(
      float[] floats,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue) {
    int resultValue = roundedMaskCount(floats);

    for (int index = 0; index < resultValue; index++) {
      int currentIndex = 5 + index * 8;
      if (floats[currentIndex] == value
          && floats[currentIndex + 1] == currentValue
          && floats[currentIndex + 2] == nextValue
          && floats[currentIndex + 3] == previousValue
          && floats[currentIndex + 4] == sourceValue
          && floats[currentIndex + 5] == targetValue
          && floats[currentIndex + 6] == inputValue
          && floats[currentIndex + 7] == outputValue) {
        return;
      }
    }

    int candidateValue = resultValue < 8 ? resultValue : 7;
    int nextIndex = 5 + candidateValue * 8;
    floats[nextIndex] = value;
    floats[nextIndex + 1] = currentValue;
    floats[nextIndex + 2] = nextValue;
    floats[nextIndex + 3] = previousValue;
    floats[nextIndex + 4] = sourceValue;
    floats[nextIndex + 5] = targetValue;
    floats[nextIndex + 6] = inputValue;
    floats[nextIndex + 7] = outputValue;
    floats[4] = Math.min(8, resultValue + 1);
  }

  private static float calculateValue(float value) {
    return Float.isFinite(value) ? value : 0.0F;
  }

  private static float calculateValue2(float value) {
    return Float.isFinite(value) ? Math.max(0.0F, value) : 0.0F;
  }

  private static int calculateValue3(float value) {
    return !Float.isFinite(value) ? 0 : (int) Math.floor(value);
  }

  private static int calculateValue4(float value) {
    return !Float.isFinite(value) ? 0 : (int) Math.ceil(value);
  }
}
