package dev.felix.ellice.ui.text;

public record TextData(
    float shadowX,
    float shadowY,
    int shadowColor,
    float outlineWidth,
    int outlineColor,
    TextMode variant,
    String fontFamily,
    float edgeSoftness,
    float letterSpacing) {
  public static final TextData NONE =
      new TextData(0.0F, 0.0F, 0, 0.0F, 0, TextMode.REGULAR, null, 0.0F);

  public TextData(
      float value,
      float currentValue,
      int nextValue,
      float previousValue,
      int sourceValue,
      TextMode textMode,
      String text,
      float targetValue) {
    this(
        value,
        currentValue,
        nextValue,
        previousValue,
        sourceValue,
        textMode,
        text,
        targetValue,
        0.0F);
  }

  public static TextData shadow(float value, float currentValue, int nextValue) {
    return new TextData(value, currentValue, nextValue, 0.0F, 0, TextMode.REGULAR, null, 0.0F);
  }

  public static TextData outline(float value, int currentValue) {
    return new TextData(0.0F, 0.0F, 0, value, currentValue, TextMode.REGULAR, null, 0.0F);
  }

  public TextData withShadow(float value, float currentValue, int nextValue) {
    return new TextData(
        value,
        currentValue,
        nextValue,
        this.outlineWidth,
        this.outlineColor,
        this.variant,
        this.fontFamily,
        this.edgeSoftness,
        this.letterSpacing);
  }

  public TextData withOutline(float value, int currentValue) {
    return new TextData(
        this.shadowX,
        this.shadowY,
        this.shadowColor,
        value,
        currentValue,
        this.variant,
        this.fontFamily,
        this.edgeSoftness,
        this.letterSpacing);
  }

  public TextData withVariant(TextMode textMode) {
    return new TextData(
        this.shadowX,
        this.shadowY,
        this.shadowColor,
        this.outlineWidth,
        this.outlineColor,
        textMode,
        this.fontFamily,
        this.edgeSoftness,
        this.letterSpacing);
  }

  public TextData withFontFamily(String text) {
    return new TextData(
        this.shadowX,
        this.shadowY,
        this.shadowColor,
        this.outlineWidth,
        this.outlineColor,
        this.variant,
        text,
        this.edgeSoftness,
        this.letterSpacing);
  }

  public TextData withEdgeSoftness(float value) {
    return new TextData(
        this.shadowX,
        this.shadowY,
        this.shadowColor,
        this.outlineWidth,
        this.outlineColor,
        this.variant,
        this.fontFamily,
        value,
        this.letterSpacing);
  }

  public TextData withLetterSpacing(float value) {
    if (!Float.isFinite(value)) {
      throw new IllegalArgumentException("Non-finite letter spacing");
    } else {
      return new TextData(
          this.shadowX,
          this.shadowY,
          this.shadowColor,
          this.outlineWidth,
          this.outlineColor,
          this.variant,
          this.fontFamily,
          this.edgeSoftness,
          value);
    }
  }

  public boolean hasShadow() {
    return this.shadowColor >>> 24 > 0 && (this.shadowX != 0.0F || this.shadowY != 0.0F);
  }

  public boolean hasOutline() {
    return this.outlineWidth > 0.0F && this.outlineColor >>> 24 > 0;
  }
}
