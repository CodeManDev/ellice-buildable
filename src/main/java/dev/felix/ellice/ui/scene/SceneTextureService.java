package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;

public class SceneTextureService extends ScenePctService<SceneTextureService> {
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private String text;
  private float value;
  private float value2 = -1.0F;
  private float value3 = -1.0F;
  private float value4 = -1.0F;
  private float value5 = -1.0F;
  private float value6;
  private int count;
  private boolean enabled;

  public SceneTextureService texture(RhiBlendStateService.TextureHandle currentTextureHandle) {
    this.textureHandle = currentTextureHandle;
    return this;
  }

  public SceneTextureService textureName(String name) {
    this.text = name;
    return this;
  }

  public SceneTextureService cornerRadius(float currentValue) {
    this.value = currentValue;
    return this;
  }

  public SceneTextureService cornerRadiusTL(float value) {
    this.value2 = value;
    return this;
  }

  public SceneTextureService cornerRadiusTR(float value) {
    this.value3 = value;
    return this;
  }

  public SceneTextureService cornerRadiusBR(float value) {
    this.value4 = value;
    return this;
  }

  public SceneTextureService cornerRadiusBL(float value) {
    this.value5 = value;
    return this;
  }

  public SceneTextureService edgeSoftness(float value) {
    this.value6 = value;
    return this;
  }

  public SceneTextureService tintColor(int color) {
    this.count = color;
    return this;
  }

  public SceneTextureService flipY(boolean currentEnabled) {
    this.enabled = currentEnabled;
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "cornerRadius" -> this.value;
      case "cornerRadiusTL", "cornerRadiusTopLeft" ->
          this.value2 >= 0.0F ? this.value2 : this.value;
      case "cornerRadiusTR", "cornerRadiusTopRight" ->
          this.value3 >= 0.0F ? this.value3 : this.value;
      case "cornerRadiusBR", "cornerRadiusBottomRight" ->
          this.value4 >= 0.0F ? this.value4 : this.value;
      case "cornerRadiusBL", "cornerRadiusBottomLeft" ->
          this.value5 >= 0.0F ? this.value5 : this.value;
      case "edgeSoftness" -> this.value6;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    switch (text) {
      case "cornerRadius":
        this.value = currentValue;
        break;
      case "cornerRadiusTL":
      case "cornerRadiusTopLeft":
        this.value2 = currentValue;
        break;
      case "cornerRadiusTR":
      case "cornerRadiusTopRight":
        this.value3 = currentValue;
        break;
      case "cornerRadiusBR":
      case "cornerRadiusBottomRight":
        this.value4 = currentValue;
        break;
      case "cornerRadiusBL":
      case "cornerRadiusBottomLeft":
        this.value5 = currentValue;
        break;
      case "edgeSoftness":
        this.value6 = currentValue;
        break;
      default:
        super.setAnimProperty(text, currentValue);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    RhiBlendStateService.TextureHandle currentTextureHandle = this.textureHandle;
    if (this.text != null) {
      RhiBlendStateService.TextureHandle nextTextureHandle =
          compositorPushPresentationScale.getNamedTexture(this.text);
      if (nextTextureHandle != null && nextTextureHandle.valid()) {
        currentTextureHandle = nextTextureHandle;
      }
    }

    if (currentTextureHandle != null && currentTextureHandle.valid()) {
      float height = this.value2 >= 0.0F ? this.value2 : this.value;
      float currentValue = this.value3 >= 0.0F ? this.value3 : this.value;
      float nextValue = this.value4 >= 0.0F ? this.value4 : this.value;
      float previousValue = this.value5 >= 0.0F ? this.value5 : this.value;
      float sourceValue = Math.max(this.value6, this.effectiveEdgeSoftness);
      if (this.enabled) {
        compositorPushPresentationScale.drawTextureRegion(
            currentTextureHandle,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.effectiveOpacity,
            height,
            currentValue,
            nextValue,
            previousValue,
            this.count,
            0.0F,
            sourceValue,
            0.0F,
            1.0F,
            1.0F,
            0.0F);
      } else {
        compositorPushPresentationScale.drawTexture(
            currentTextureHandle,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.effectiveOpacity,
            height,
            currentValue,
            nextValue,
            previousValue,
            this.count,
            0.0F,
            sourceValue);
      }
    }
  }
}
