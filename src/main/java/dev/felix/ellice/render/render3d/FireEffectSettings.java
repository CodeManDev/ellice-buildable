package dev.felix.ellice.render.render3d;

import java.util.Objects;

public record FireEffectSettings(
    int coreColor,
    int edgeColor,
    float intensity,
    float flameHeight,
    float flameWidth,
    float bloomStrength,
    float blurRadiusPixels,
    float backdropBlur,
    float heatDistortionPixels,
    float visibleOpacity,
    float throughWallsOpacity,
    boolean perPlayerColors,
    boolean fireEnabled,
    OutlineEffectSettings outline,
    KawaseBloomSettings kawase,
    InterferenceEffectSettings interference,
    Render3dData glow,
    int damageColor,
    ElliceEffectSettings ellice) {
  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      boolean enabled,
      boolean currentEnabled,
      OutlineEffectSettings outlineEffectSettings,
      KawaseBloomSettings kawaseBloomSettings,
      InterferenceEffectSettings interferenceEffectSettings,
      Render3dData render3dData,
      int defaultValue) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        enabled,
        currentEnabled,
        outlineEffectSettings,
        kawaseBloomSettings,
        interferenceEffectSettings,
        render3dData,
        defaultValue,
        ElliceEffectSettings.NONE);
  }

  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      boolean enabled,
      boolean currentEnabled,
      OutlineEffectSettings outlineEffectSettings,
      KawaseBloomSettings kawaseBloomSettings,
      InterferenceEffectSettings interferenceEffectSettings,
      Render3dData render3dData) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        enabled,
        currentEnabled,
        outlineEffectSettings,
        kawaseBloomSettings,
        interferenceEffectSettings,
        render3dData,
        0);
  }

  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      boolean enabled,
      boolean currentEnabled,
      OutlineEffectSettings outlineEffectSettings,
      KawaseBloomSettings kawaseBloomSettings,
      InterferenceEffectSettings interferenceEffectSettings) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        enabled,
        currentEnabled,
        outlineEffectSettings,
        kawaseBloomSettings,
        interferenceEffectSettings,
        Render3dData.NONE);
  }

  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      boolean enabled,
      boolean currentEnabled,
      OutlineEffectSettings outlineEffectSettings,
      KawaseBloomSettings kawaseBloomSettings) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        enabled,
        currentEnabled,
        outlineEffectSettings,
        kawaseBloomSettings,
        InterferenceEffectSettings.NONE);
  }

  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      boolean enabled) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        enabled,
        true,
        OutlineEffectSettings.NONE,
        KawaseBloomSettings.NONE);
  }

  public FireEffectSettings(
      int value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        targetValue,
        inputValue,
        outputValue,
        resultValue,
        candidateValue,
        selectedValue,
        false);
  }

  public FireEffectSettings(
      int coreColor,
      int edgeColor,
      float intensity,
      float flameHeight,
      float flameWidth,
      float bloomStrength,
      float blurRadiusPixels,
      float backdropBlur,
      float heatDistortionPixels,
      float visibleOpacity,
      float throughWallsOpacity,
      boolean perPlayerColors,
      boolean fireEnabled,
      OutlineEffectSettings outline,
      KawaseBloomSettings kawase,
      InterferenceEffectSettings interference,
      Render3dData glow,
      int damageColor,
      ElliceEffectSettings ellice) {
    Objects.requireNonNull(outline, "outline");
    Objects.requireNonNull(kawase, "kawase");
    Objects.requireNonNull(interference, "interference");
    Objects.requireNonNull(glow, "glow");
    Objects.requireNonNull(ellice, "ellice");
    updateState2("intensity", intensity);
    updateState("flameHeight", flameHeight);
    updateState("flameWidth", flameWidth);
    updateState2("bloomStrength", bloomStrength);
    updateState2("blurRadiusPixels", blurRadiusPixels);
    updateState3("backdropBlur", backdropBlur);
    updateState2("heatDistortionPixels", heatDistortionPixels);
    updateState3("visibleOpacity", visibleOpacity);
    updateState3("throughWallsOpacity", throughWallsOpacity);
    this.coreColor = coreColor;
    this.edgeColor = edgeColor;
    this.intensity = intensity;
    this.flameHeight = flameHeight;
    this.flameWidth = flameWidth;
    this.bloomStrength = bloomStrength;
    this.blurRadiusPixels = blurRadiusPixels;
    this.backdropBlur = backdropBlur;
    this.heatDistortionPixels = heatDistortionPixels;
    this.visibleOpacity = visibleOpacity;
    this.throughWallsOpacity = throughWallsOpacity;
    this.perPlayerColors = perPlayerColors;
    this.fireEnabled = fireEnabled;
    this.outline = outline;
    this.kawase = kawase;
    this.interference = interference;
    this.glow = glow;
    this.damageColor = damageColor;
    this.ellice = ellice;
  }

  public boolean hasFire() {
    return this.fireEnabled
        && this.intensity > 0.0F
        && (this.visibleOpacity > 0.0F || this.throughWallsOpacity > 0.0F);
  }

  public boolean hasOutline() {
    return this.outline.renderable();
  }

  public boolean hasKawase() {
    return this.kawase.renderable();
  }

  public boolean hasInterference() {
    return this.interference.renderable();
  }

  public boolean hasEllice() {
    return this.ellice.renderable();
  }

  public boolean hasGlow() {
    return this.glow.renderable();
  }

  public boolean renderable() {
    return this.hasFire()
        || this.hasOutline()
        || this.hasKawase()
        || this.hasInterference()
        || this.hasGlow()
        || this.hasEllice();
  }

  private static void updateState(String text, float value) {
    updateState4(text, value);
    if (value <= 0.0F) {
      throw new IllegalArgumentException(text + " must be positive");
    }
  }

  private static void updateState2(String text, float value) {
    updateState4(text, value);
    if (value < 0.0F) {
      throw new IllegalArgumentException(text + " must not be negative");
    }
  }

  private static void updateState3(String text, float value) {
    updateState4(text, value);
    if (value < 0.0F || value > 1.0F) {
      throw new IllegalArgumentException(text + " must be in [0, 1]");
    }
  }

  private static void updateState4(String text, float value) {
    if (!Float.isFinite(value)) {
      throw new IllegalArgumentException(text + " must be finite");
    }
  }
}
