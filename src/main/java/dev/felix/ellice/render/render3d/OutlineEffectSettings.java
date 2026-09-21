package dev.felix.ellice.render.render3d;

import java.util.Objects;

public record OutlineEffectSettings(
    boolean enabled,
    int visibleColor,
    int occludedColor,
    float thicknessPixels,
    float softnessPixels,
    Render3dFeatureType visibility) {
  public static final float MIN_THICKNESS_PIXELS = 0.5F;
  public static final float MAX_THICKNESS_PIXELS = 16.0F;
  public static final float MIN_SOFTNESS_PIXELS = 0.0F;
  public static final float MAX_SOFTNESS_PIXELS = 3.0F;
  public static final OutlineEffectSettings NONE =
      new OutlineEffectSettings(false, 0, 0, 0.5F, 0.0F, Render3dFeatureType.BOTH);

  public OutlineEffectSettings(
      boolean enabled,
      int visibleColor,
      int occludedColor,
      float thicknessPixels,
      float softnessPixels,
      Render3dFeatureType visibility) {
    Objects.requireNonNull(visibility, "visibility");
    updateState("thicknessPixels", thicknessPixels, 0.5F, 16.0F);
    updateState("softnessPixels", softnessPixels, 0.0F, 3.0F);
    this.enabled = enabled;
    this.visibleColor = visibleColor;
    this.occludedColor = occludedColor;
    this.thicknessPixels = thicknessPixels;
    this.softnessPixels = softnessPixels;
    this.visibility = visibility;
  }

  public OutlineEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      Render3dFeatureType render3dFeatureType) {
    this(true, value, currentValue, nextValue, previousValue, render3dFeatureType);
  }

  public boolean hasVisible() {
    return this.enabled
        && this.visibility.includesVisible()
        && calculateValue(this.visibleColor) != 0;
  }

  public boolean hasOccluded() {
    return this.enabled
        && this.visibility.includesOccluded()
        && calculateValue(this.occludedColor) != 0;
  }

  public boolean renderable() {
    return this.hasVisible() || this.hasOccluded();
  }

  public float visibleAlpha() {
    return calculateValue2(this.visibleColor);
  }

  public float occludedAlpha() {
    return calculateValue2(this.occludedColor);
  }

  private static int calculateValue(int value) {
    return value >>> 24 & 0xFF;
  }

  private static float calculateValue2(int value) {
    return calculateValue(value) / 255.0F;
  }

  private static void updateState(String text, float value, float currentValue, float nextValue) {
    if (!Float.isFinite(value) || value < currentValue || value > nextValue) {
      throw new IllegalArgumentException(
          text + " must be finite and in [" + currentValue + ", " + nextValue + "]");
    }
  }
}
