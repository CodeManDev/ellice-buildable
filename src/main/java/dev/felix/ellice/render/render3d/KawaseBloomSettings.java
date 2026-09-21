package dev.felix.ellice.render.render3d;

import java.util.Objects;

public record KawaseBloomSettings(
    boolean enabled,
    int visibleColor,
    int occludedColor,
    float strength,
    float radiusPixels,
    int levels,
    float coreOpacity,
    Render3dFeatureType visibility) {
  public static final float MIN_STRENGTH = 0.0F;
  public static final float MAX_STRENGTH = 4.0F;
  public static final float MIN_RADIUS_PIXELS = 0.0F;
  public static final float MAX_RADIUS_PIXELS = 96.0F;
  public static final int MIN_LEVELS = 2;
  public static final int MAX_LEVELS = 5;
  public static final float MIN_CORE_OPACITY = 0.0F;
  public static final float MAX_CORE_OPACITY = 1.0F;
  public static final KawaseBloomSettings NONE =
      new KawaseBloomSettings(false, 0, 0, 0.0F, 0.0F, 2, 0.0F, Render3dFeatureType.BOTH);

  public KawaseBloomSettings(
      boolean enabled,
      int visibleColor,
      int occludedColor,
      float strength,
      float radiusPixels,
      int levels,
      float coreOpacity,
      Render3dFeatureType visibility) {
    Objects.requireNonNull(visibility, "visibility");
    updateState("strength", strength, 0.0F, 4.0F);
    updateState("radiusPixels", radiusPixels, 0.0F, 96.0F);
    if (levels >= 2 && levels <= 5) {
      updateState("coreOpacity", coreOpacity, 0.0F, 1.0F);
      this.enabled = enabled;
      this.visibleColor = visibleColor;
      this.occludedColor = occludedColor;
      this.strength = strength;
      this.radiusPixels = radiusPixels;
      this.levels = levels;
      this.coreOpacity = coreOpacity;
      this.visibility = visibility;
    } else {
      throw new IllegalArgumentException("levels must be in [2, 5]");
    }
  }

  public KawaseBloomSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      int sourceValue,
      float targetValue,
      Render3dFeatureType render3dFeatureType) {
    this(
        true,
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        render3dFeatureType);
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

  public boolean hasVisibleBloom() {
    return this.checkCondition() && this.hasVisible() && checkCondition2(this.visibleColor);
  }

  public boolean hasOccludedBloom() {
    return this.checkCondition() && this.hasOccluded() && checkCondition2(this.occludedColor);
  }

  public boolean bloomRenderable() {
    return this.hasVisibleBloom() || this.hasOccludedBloom();
  }

  public boolean hasVisibleCore() {
    return this.coreOpacity > 0.0F && this.hasVisible();
  }

  public boolean hasOccludedCore() {
    return this.coreOpacity > 0.0F && this.hasOccluded();
  }

  public boolean coreRenderable() {
    return this.hasVisibleCore() || this.hasOccludedCore();
  }

  public boolean renderable() {
    return this.bloomRenderable() || this.coreRenderable();
  }

  public float visibleAlpha() {
    return calculateValue2(this.visibleColor);
  }

  public float occludedAlpha() {
    return calculateValue2(this.occludedColor);
  }

  private boolean checkCondition() {
    return this.strength > 0.0F && this.radiusPixels > 0.0F;
  }

  private static boolean checkCondition2(int value) {
    return (value & 16777215) != 0;
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
