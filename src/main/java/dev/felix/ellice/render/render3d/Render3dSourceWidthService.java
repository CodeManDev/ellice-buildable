package dev.felix.ellice.render.render3d;

import java.util.ArrayList;
import java.util.List;

public final class Render3dSourceWidthService {
  public static final int MIN_LEVELS = 2;
  public static final int MAX_LEVELS = 5;
  public static final float MIN_RADIUS_PIXELS = 0.0F;
  public static final float MAX_RADIUS_PIXELS = 96.0F;
  private final int count;
  private final int count2;
  private final float value;
  private final int count3;
  private final float value2;
  private final float value3;
  private final List<Render3dSourceWidthService.MipLevel> items;

  private Render3dSourceWidthService(
      int currentValue,
      int nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      float inputValue,
      List<Render3dSourceWidthService.MipLevel> currentItems) {
    this.count = currentValue;
    this.count2 = nextValue;
    this.value = previousValue;
    this.count3 = sourceValue;
    this.value2 = targetValue;
    this.value3 = inputValue;
    this.items = List.copyOf(currentItems);
  }

  public static Render3dSourceWidthService create(
      int value, int currentValue, float nextValue, int previousValue) {
    if (value > 0 && currentValue > 0) {
      updateState(nextValue);
      int sourceValue = clampLevels(previousValue);
      ArrayList arrayList = new ArrayList(sourceValue);
      int targetValue = value;
      int inputValue = currentValue;

      for (int index = 0; index < sourceValue; index++) {
        targetValue = calculateValue(targetValue);
        inputValue = calculateValue(inputValue);
        arrayList.add(new Render3dSourceWidthService.MipLevel(index, targetValue, inputValue));
      }

      float outputValue = supportForLevels(sourceValue);
      float resultValue = nextValue / outputValue;
      return new Render3dSourceWidthService(
          value, currentValue, nextValue, sourceValue, outputValue, resultValue, arrayList);
    } else {
      throw new IllegalArgumentException("source dimensions must be positive");
    }
  }

  public int sourceWidth() {
    return this.count;
  }

  public int sourceHeight() {
    return this.count2;
  }

  public float radiusPixels() {
    return this.value;
  }

  public int levels() {
    return this.count3;
  }

  public float supportPixels() {
    return this.value2;
  }

  public float offsetScale() {
    return this.value3;
  }

  public List<Render3dSourceWidthService.MipLevel> mips() {
    return this.items;
  }

  public Render3dSourceWidthService.MipLevel mip(int value) {
    return this.items.get(value);
  }

  public int downsamplePassCount() {
    return this.count3;
  }

  public int upsamplePassCount() {
    return this.count3 - 1;
  }

  public int passCount() {
    return 2 * this.count3 - 1;
  }

  public static int clampLevels(int value) {
    return Math.max(2, Math.min(5, value));
  }

  public static float supportForLevels(int value) {
    int currentValue = clampLevels(value);
    int nextValue = 1 << currentValue;
    float previousValue = 0.5F * (nextValue - 1);
    float sourceValue = 2.0F * nextValue - 4.0F;
    return previousValue + sourceValue;
  }

  private static int calculateValue(int value) {
    return value / 2 + value % 2;
  }

  private static void updateState(float value) {
    if (!Float.isFinite(value) || value < 0.0F || value > 96.0F) {
      throw new IllegalArgumentException("radiusPixels must be finite and in [0.0, 96.0]");
    }
  }

  public record MipLevel(int index, int width, int height) {
    public MipLevel(int index, int width, int height) {
      if (index < 0) {
        throw new IllegalArgumentException("index must not be negative");
      }

      if (width > 0 && height > 0) {
        this.index = index;
        this.width = width;
        this.height = height;
      } else {
        throw new IllegalArgumentException("mip dimensions must be positive");
      }
    }
  }
}
