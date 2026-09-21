package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public class TooltipBubbleNode extends ScenePctService<TooltipBubbleNode> {
  private float value = 8.0F;
  private float value2 = 14.0F;
  private float value3 = 7.0F;
  private float value4 = 0.5F;
  private float value5 = 4.0F;
  private int count = -266330064;
  private int count2;
  private float value6 = 8.0F;
  private int count3 = 1610612736;
  private float value7 = 1.0F;
  private int count4 = 822083583;
  private float value8;
  private float value9 = 1.0F;
  private boolean enabled = true;
  private float value10;

  public TooltipBubbleNode cornerRadius(float currentValue) {
    this.value = Math.max(0.0F, currentValue);
    return this;
  }

  public TooltipBubbleNode tailWidth(float value) {
    this.value2 = Math.max(0.0F, value);
    return this;
  }

  public TooltipBubbleNode tailHeight(float value) {
    this.value3 = Math.max(0.0F, value);
    return this;
  }

  public TooltipBubbleNode tailX(float value) {
    this.value4 = value;
    return this;
  }

  public TooltipBubbleNode smoothK(float value) {
    this.value5 = Math.max(0.5F, value);
    return this;
  }

  public TooltipBubbleNode color(int currentColor) {
    this.count = currentColor;
    return this;
  }

  public TooltipBubbleNode gradientEnd(int value) {
    this.count2 = value;
    return this;
  }

  public TooltipBubbleNode shadow(float value) {
    this.value6 = Math.max(0.0F, value);
    return this;
  }

  public TooltipBubbleNode shadowColor(int color) {
    this.count3 = color;
    return this;
  }

  public TooltipBubbleNode borderWidth(float value) {
    this.value7 = Math.max(0.0F, value);
    return this;
  }

  public TooltipBubbleNode borderColor(int color) {
    this.count4 = color;
    return this;
  }

  public TooltipBubbleNode innerHighlight(float value) {
    this.value8 = calculateValue(value);
    return this;
  }

  public TooltipBubbleNode innerHighlightSize(float value) {
    this.value9 = Math.max(0.0F, value);
    return this;
  }

  public TooltipBubbleNode blur(boolean currentEnabled) {
    this.enabled = currentEnabled;
    return this;
  }

  public TooltipBubbleNode edgeSoftness(float value) {
    this.value10 = Math.max(0.0F, value);
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "cornerRadius" -> this.value;
      case "tailWidth", "tailW" -> this.value2;
      case "tailHeight", "tailH" -> this.value3;
      case "tailX", "tailOffset" -> this.value4;
      case "smoothK", "smooth" -> this.value5;
      case "shadow" -> this.value6;
      case "borderWidth", "border" -> this.value7;
      case "innerHighlight" -> this.value8;
      case "innerHighlightSize" -> this.value9;
      case "edgeSoftness" -> this.value10;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float value) {
    switch (text) {
      case "cornerRadius":
        this.cornerRadius(value);
        break;
      case "tailWidth":
      case "tailW":
        this.tailWidth(value);
        break;
      case "tailHeight":
      case "tailH":
        this.tailHeight(value);
        break;
      case "tailX":
      case "tailOffset":
        this.tailX(value);
        break;
      case "smoothK":
      case "smooth":
        this.smoothK(value);
        break;
      case "shadow":
        this.shadow(value);
        break;
      case "borderWidth":
      case "border":
        this.borderWidth(value);
        break;
      case "innerHighlight":
        this.innerHighlight(value);
        break;
      case "innerHighlightSize":
        this.innerHighlightSize(value);
        break;
      case "edgeSoftness":
        this.edgeSoftness(value);
        break;
      default:
        super.setAnimProperty(text, value);
    }

    this.invalidate();
  }

  @Override
  public int getColorProperty(String text) {
    return switch (text) {
      case "color", "background", "backgroundColor", "fill" -> this.count;
      case "gradientEnd" -> this.count2;
      case "shadowColor" -> this.count3;
      case "borderColor" -> this.count4;
      default -> super.getColorProperty(text);
    };
  }

  @Override
  public void setColorProperty(String text, int currentColor) {
    switch (text) {
      case "color":
      case "background":
      case "backgroundColor":
      case "fill":
        this.count = currentColor;
        break;
      case "gradientEnd":
        this.count2 = currentColor;
        break;
      case "shadowColor":
        this.count3 = currentColor;
        break;
      case "borderColor":
        this.count4 = currentColor;
        break;
      default:
        super.setColorProperty(text, currentColor);
    }

    this.invalidate();
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (!(this.effectiveOpacity <= 0.001F) && !(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
      float y = this.value4 >= 0.0F && this.value4 <= 1.0F ? this.value4 * this.cw : this.value4;
      compositorPushPresentationScale.addTooltipBubble(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          this.value,
          y,
          this.value2,
          this.value3,
          this.value5,
          this.count,
          this.count2,
          this.value6,
          this.count3,
          this.value7,
          this.count4,
          this.value8,
          this.value9,
          this.enabled,
          this.effectiveOpacity,
          this.value10);
    }
  }

  private static float calculateValue(float value) {
    return Math.max(0.0F, Math.min(1.0F, value));
  }
}
