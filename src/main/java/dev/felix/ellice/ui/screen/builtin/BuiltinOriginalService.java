package dev.felix.ellice.ui.screen.builtin;

import java.awt.Color;
import java.util.Locale;

public final class BuiltinOriginalService {
  private final int count;
  private int count2;
  private float value;
  private float value2;
  private float value3;
  private String text = "";

  public BuiltinOriginalService(int value) {
    this.count = value;
    this.argb(value);
  }

  public int original() {
    return this.count;
  }

  public int argb() {
    return this.count2;
  }

  public float hue() {
    return this.value;
  }

  public float saturation() {
    return this.value2;
  }

  public float brightness() {
    return this.value3;
  }

  public float alpha() {
    return (this.count2 >>> 24) / 255.0F;
  }

  public String error() {
    return this.text;
  }

  public String hex() {
    return String.format(Locale.ROOT, "#%08X", this.count2);
  }

  public void argb(int currentValue) {
    float[] floats =
        Color.RGBtoHSB(
            currentValue >> 16 & 0xFF, currentValue >> 8 & 0xFF, currentValue & 0xFF, null);
    if (floats[1] > 0.0F && floats[2] > 0.0F) {
      this.value = floats[0];
    }

    if (floats[2] > 0.0F) {
      this.value2 = floats[1];
    }

    this.value3 = floats[2];
    this.count2 = currentValue;
    this.text = "";
  }

  public void hue(float currentValue) {
    this.value = calculateValue(currentValue);
    this.updateState();
  }

  public void sv(float value, float currentValue) {
    this.value2 = calculateValue(value);
    this.value3 = calculateValue(currentValue);
    this.updateState();
  }

  public void alpha(float opacity) {
    this.count2 = Math.round(calculateValue(opacity) * 255.0F) << 24 | this.count2 & 16777215;
    this.text = "";
  }

  public void hex(String currentText) {
    String nextText = currentText.trim();
    if (nextText.startsWith("#")) {
      nextText = nextText.substring(1);
    }

    if (nextText.matches("[a-fA-F0-9]{8}")) {
      this.argb((int) Long.parseLong(nextText, 16));
    } else if (nextText.matches("[a-fA-F0-9]{6}")) {
      this.argb(0xFF000000 | Integer.parseInt(nextText, 16));
    } else {
      this.text = "Enter 6 RGB or 8 ARGB hex digits.";
    }
  }

  public int shade(float currentValue, float nextValue) {
    return this.count2 & 0xFF000000
        | Color.HSBtoRGB(this.value, currentValue, nextValue) & 16777215;
  }

  private void updateState() {
    this.count2 =
        this.count2 & 0xFF000000 | Color.HSBtoRGB(this.value, this.value2, this.value3) & 16777215;
    this.text = "";
  }

  private static float calculateValue(float value) {
    return Float.isFinite(value) ? Math.max(0.0F, Math.min(1.0F, value)) : 0.0F;
  }
}
