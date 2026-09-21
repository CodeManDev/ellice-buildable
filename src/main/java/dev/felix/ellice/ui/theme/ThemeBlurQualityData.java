package dev.felix.ellice.ui.theme;

import java.util.Objects;

public record ThemeBlurQualityData(
    float blurStrength,
    ThemeBlurQualityData.BlurQuality blurQuality,
    float shadowStrength,
    boolean glassEffects,
    boolean animatedBackground) {
  private static ThemeBlurQualityData current = defaults();
  private static long revision;

  public ThemeBlurQualityData(
      float blurStrength,
      ThemeBlurQualityData.BlurQuality blurQuality,
      float shadowStrength,
      boolean glassEffects,
      boolean animatedBackground) {
    blurStrength = calculateValue(blurStrength, 1.5F, 1.0F);
    shadowStrength = calculateValue(shadowStrength, 1.0F, 1.0F);
    if (blurQuality == null) {
      blurQuality = ThemeBlurQualityData.BlurQuality.HIGH;
    }

    this.blurStrength = blurStrength;
    this.blurQuality = blurQuality;
    this.shadowStrength = shadowStrength;
    this.glassEffects = glassEffects;
    this.animatedBackground = animatedBackground;
  }

  public static ThemeBlurQualityData defaults() {
    return new ThemeBlurQualityData(1.0F, ThemeBlurQualityData.BlurQuality.HIGH, 1.0F, true, true);
  }

  public static ThemeBlurQualityData current() {
    return current;
  }

  public static long revision() {
    return revision;
  }

  public static void apply(ThemeBlurQualityData themeBlurQualityData) {
    if (!current.equals(Objects.requireNonNull(themeBlurQualityData))) {
      current = themeBlurQualityData;
      revision++;
    }
  }

  public boolean blurEnabled() {
    return this.blurStrength > 0.001F;
  }

  public boolean advancedGlass() {
    return this.glassEffects && this.blurEnabled();
  }

  public float blur(float value) {
    return Float.isFinite(value) ? Math.max(0.0F, value) * this.blurStrength : 0.0F;
  }

  public float shadow(float value) {
    return Float.isFinite(value) ? Math.max(0.0F, value) * this.shadowStrength : 0.0F;
  }

  public int shadowColor(int color) {
    return Math.round((color >>> 24) * this.shadowStrength) << 24 | color & 16777215;
  }

  private static float calculateValue(float value, float currentValue, float nextValue) {
    return Float.isFinite(value) ? Math.clamp(value, 0.0F, currentValue) : nextValue;
  }

  public enum BlurQuality {
    LOW("Low", 4, 3),
    MEDIUM("Medium", 2, 4),
    HIGH("High", 1, 5);

    private final String text;
    private final int count;
    private final int count2;

    BlurQuality(String currentText, int value, int currentValue) {
      this.text = currentText;
      this.count = value;
      this.count2 = currentValue;
    }

    public String label() {
      return this.text;
    }

    public int divisor() {
      return this.count;
    }

    public int levels() {
      return this.count2;
    }

    private static ThemeBlurQualityData.BlurQuality[] $values() {
      return new ThemeBlurQualityData.BlurQuality[] {LOW, MEDIUM, HIGH};
    }
  }
}
