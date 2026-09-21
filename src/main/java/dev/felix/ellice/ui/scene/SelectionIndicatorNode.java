package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public class SelectionIndicatorNode extends ScenePctService<SelectionIndicatorNode> {
  private float value;
  private float value2;
  private float value3 = 1.0F;
  private float value4;
  private float value5 = 1.0F;
  private float value6;
  private float value7 = 1.0F;
  private int count = -8530948;

  public SelectionIndicatorNode cornerRadius(float currentValue) {
    this.value = Math.max(0.0F, currentValue);
    return this;
  }

  public SelectionIndicatorNode activeX(float value) {
    this.value2 = value;
    return this;
  }

  public SelectionIndicatorNode activeWidth(float value) {
    this.value3 = Math.max(1.0F, value);
    return this;
  }

  public SelectionIndicatorNode hoverX(float value) {
    this.value4 = value;
    return this;
  }

  public SelectionIndicatorNode hoverWidth(float value) {
    this.value5 = Math.max(1.0F, value);
    return this;
  }

  public SelectionIndicatorNode hoverMix(float value) {
    this.value6 = calculateValue(value);
    return this;
  }

  public SelectionIndicatorNode transition(float value) {
    this.value7 = calculateValue(value);
    return this;
  }

  public SelectionIndicatorNode accentColor(int color) {
    this.count = color;
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "cornerRadius" -> this.value;
      case "activeX" -> this.value2;
      case "activeWidth" -> this.value3;
      case "hoverX" -> this.value4;
      case "hoverWidth" -> this.value5;
      case "hoverMix" -> this.value6;
      case "transition" -> this.value7;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    switch (text) {
      case "cornerRadius":
        this.value = Math.max(0.0F, currentValue);
        break;
      case "activeX":
        this.value2 = currentValue;
        break;
      case "activeWidth":
        this.value3 = Math.max(1.0F, currentValue);
        break;
      case "hoverX":
        this.value4 = currentValue;
        break;
      case "hoverWidth":
        this.value5 = Math.max(1.0F, currentValue);
        break;
      case "hoverMix":
        this.value6 = calculateValue(currentValue);
        break;
      case "transition":
        this.value7 = calculateValue(currentValue);
        break;
      default:
        super.setAnimProperty(text, currentValue);
    }

    this.invalidate();
  }

  @Override
  public int getColorProperty(String text) {
    return !"accentColor".equals(text) && !"color".equals(text)
        ? super.getColorProperty(text)
        : this.count;
  }

  @Override
  public void setColorProperty(String text, int currentColor) {
    if (!"accentColor".equals(text) && !"color".equals(text)) {
      super.setColorProperty(text, currentColor);
    } else {
      this.count = currentColor;
      this.invalidate();
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (!(this.effectiveOpacity <= 0.001F) && !(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
      compositorPushPresentationScale.addLiquidSwitch(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          this.value > 0.0F ? this.value : this.ch * 0.5F,
          this.value2,
          this.value3,
          this.value4,
          this.value5,
          this.value6,
          this.value7,
          this.effectiveOpacity,
          this.count);
    }
  }

  private static float calculateValue(float value) {
    return Math.max(0.0F, Math.min(1.0F, value));
  }
}
