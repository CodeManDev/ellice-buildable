package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.function.BiConsumer;

public class ColorHueService extends ScenePctService<ColorHueService> {
  private float value;
  private float value2 = 1.0F;
  private float value3 = 1.0F;
  BiConsumer<Float, Float> onChange;

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  public ColorHueService() {
    this.interactive = true;
  }

  public ColorHueService hue(float currentValue) {
    this.value = currentValue;
    return this;
  }

  public ColorHueService sat(float value) {
    this.value2 = value;
    return this;
  }

  public ColorHueService bri(float value) {
    this.value3 = value;
    return this;
  }

  public ColorHueService onChange(BiConsumer<Float, Float> biConsumer) {
    this.onChange = biConsumer;
    return this;
  }

  public float hue() {
    return this.value;
  }

  public float sat() {
    return this.value2;
  }

  @Override
  protected void updateWhilePressed(float currentValue, float nextValue) {
    float previousValue = this.cx + this.cw / 2.0F;
    float sourceValue = this.cy + this.ch / 2.0F;
    float targetValue = currentValue - previousValue;
    float inputValue = nextValue - sourceValue;
    float outputValue = Math.min(this.cw, this.ch) / 2.0F;
    this.value = (float) (Math.atan2(inputValue, targetValue) / 6.283185307179586) + 0.5F;
    if (this.value < 0.0F) {
      this.value++;
    }

    if (this.value > 1.0F) {
      this.value--;
    }

    this.value2 =
        Math.min(
            1.0F,
            (float) Math.sqrt(targetValue * targetValue + inputValue * inputValue) / outputValue);
    if (this.onChange != null) {
      this.onChange.accept(this.value, this.value2);
    }
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 95.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 95.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (!(this.effectiveOpacity < 0.003F)) {
      compositorPushPresentationScale.colorWheel(this.cx, this.cy, this.cw, this.ch, this.value3);
      float y = Math.min(this.cw, this.ch) / 2.0F;
      float width = (this.value - 0.5F) * 2.0F * 3.1415927F;
      float height = this.value2 * y;
      float currentValue = this.cx + this.cw / 2.0F + (float) Math.cos(width) * height;
      float nextValue = this.cy + this.ch / 2.0F + (float) Math.sin(width) * height;
      float previousValue = 5.0F;
      compositorPushPresentationScale.overlayRect(
          currentValue - previousValue,
          nextValue - previousValue,
          previousValue * 2.0F,
          previousValue * 2.0F,
          previousValue,
          previousValue,
          previousValue,
          previousValue,
          0,
          0.0F,
          0.0F,
          0,
          2.0F,
          mulAlpha(-587202560, this.effectiveOpacity));
      float sourceValue = previousValue - 1.5F;
      compositorPushPresentationScale.overlayRect(
          currentValue - sourceValue,
          nextValue - sourceValue,
          sourceValue * 2.0F,
          sourceValue * 2.0F,
          sourceValue,
          sourceValue,
          sourceValue,
          sourceValue,
          0,
          0.0F,
          0.0F,
          0,
          1.2F,
          mulAlpha(-1, this.effectiveOpacity));
    }
  }
}
