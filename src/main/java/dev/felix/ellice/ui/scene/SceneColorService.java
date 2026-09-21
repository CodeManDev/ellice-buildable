package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public class SceneColorService extends ScenePctService<SceneColorService> {
  private int count;
  private int count2;
  private int count3;
  private int count4;
  private int count5;
  private float value;

  public SceneColorService color(int currentColor, int nextColor) {
    switch (currentColor) {
      case 0:
        this.count = nextColor;
        break;
      case 1:
        this.count2 = nextColor;
        break;
      case 2:
        this.count3 = nextColor;
        break;
      case 3:
        this.count4 = nextColor;
        break;
      case 4:
        this.count5 = nextColor;
    }

    return this;
  }

  public SceneColorService colors(
      int color, int currentColor, int nextColor, int previousColor, int sourceColor) {
    this.count = color;
    this.count2 = currentColor;
    this.count3 = nextColor;
    this.count4 = previousColor;
    this.count5 = sourceColor;
    return this;
  }

  public SceneColorService cornerRadius(float currentValue) {
    this.value = currentValue;
    return this;
  }

  @Override
  public int getColorProperty(String text) {
    return switch (text) {
      case "color0" -> this.count;
      case "color1" -> this.count2;
      case "color2" -> this.count3;
      case "color3" -> this.count4;
      case "color4" -> this.count5;
      default -> super.getColorProperty(text);
    };
  }

  @Override
  public void setColorProperty(String text, int color) {
    switch (text) {
      case "color0":
        this.count = color;
        break;
      case "color1":
        this.count2 = color;
        break;
      case "color2":
        this.count3 = color;
        break;
      case "color3":
        this.count4 = color;
        break;
      case "color4":
        this.count5 = color;
        break;
      default:
        super.setColorProperty(text, color);
    }
  }

  @Override
  public float getAnimProperty(String text) {
    return "cornerRadius".equals(text) ? this.value : super.getAnimProperty(text);
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    if ("cornerRadius".equals(text)) {
      this.value = currentValue;
    } else {
      super.setAnimProperty(text, currentValue);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = this.effectiveOpacity;
    if (!(y <= 0.001F)) {
      compositorPushPresentationScale.addMesh(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          this.value,
          this.count,
          this.count2,
          this.count3,
          this.count4,
          this.count5,
          y);
    }
  }
}
