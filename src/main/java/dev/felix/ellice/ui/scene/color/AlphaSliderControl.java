package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.util.function.Consumer;

public class AlphaSliderControl extends ScenePctService<AlphaSliderControl> {
  private float value;
  private float value2 = 1.0F;
  private float value3 = 1.0F;
  private float value4 = 1.0F;
  private float value5 = 3.0F;
  private Consumer<Float> consumer;
  private Consumer<Float> consumer2;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private float value6;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite("materialAlphaPress", item -> item instanceof AlphaSliderControl);

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  public AlphaSliderControl material(boolean enabled) {
    this.enabled2 = enabled;
    return this;
  }

  public void materialFocus(boolean enabled) {
    this.enabled3 = enabled;
    this.invalidate();
  }

  @Override
  public float getAnimProperty(String text) {
    return "materialAlphaPress".equals(text) ? this.value6 : super.getAnimProperty(text);
  }

  @Override
  public void setAnimProperty(String text, float value) {
    if ("materialAlphaPress".equals(text)) {
      this.value6 = value;
    } else {
      super.setAnimProperty(text, value);
    }
  }

  public AlphaSliderControl() {
    this.interactive = true;
  }

  public AlphaSliderControl hue(float currentValue) {
    float nextValue = calculateValue(currentValue);
    if (checkCondition(this.value, nextValue)) {
      return this;
    }

    this.value = nextValue;
    this.invalidate();
    return this;
  }

  public AlphaSliderControl sat(float value) {
    float currentValue = calculateValue(value);
    if (checkCondition(this.value2, currentValue)) {
      return this;
    }

    this.value2 = currentValue;
    this.invalidate();
    return this;
  }

  public AlphaSliderControl bri(float value) {
    float currentValue = calculateValue(value);
    if (checkCondition(this.value3, currentValue)) {
      return this;
    }

    this.value3 = currentValue;
    this.invalidate();
    return this;
  }

  public AlphaSliderControl alpha(float opacity) {
    float currentOpacity = calculateValue(opacity);
    if (checkCondition(this.value4, currentOpacity)) {
      return this;
    }

    this.value4 = currentOpacity;
    this.invalidate();
    return this;
  }

  public AlphaSliderControl cornerRadius(float value) {
    float currentValue = Float.isFinite(value) ? Math.max(0.0F, value) : this.value5;
    if (checkCondition(this.value5, currentValue)) {
      return this;
    }

    this.value5 = currentValue;
    this.invalidate();
    return this;
  }

  public AlphaSliderControl onChange(Consumer<Float> currentConsumer) {
    this.consumer = currentConsumer;
    return this;
  }

  public AlphaSliderControl onCommit(Consumer<Float> consumer) {
    this.consumer2 = consumer;
    return this;
  }

  public float alpha() {
    return this.value4;
  }

  @Override
  protected void updateWhilePressed(float value, float currentValue) {
    if (!(this.cw <= 0.0F)) {
      float nextValue =
          calculateValue(
              (value - this.cx - (this.enabled2 ? 2 : 0))
                  / Math.max(1.0F, this.cw - (this.enabled2 ? 4 : 0)));
      if (!checkCondition(this.value4, nextValue)) {
        this.value4 = nextValue;
        this.enabled = true;
        this.invalidate();
        if (this.consumer != null) {
          this.consumer.accept(this.value4);
        }
      }
    }
  }

  @Override
  protected void onRelease(boolean currentEnabled) {
    if (this.enabled) {
      this.enabled = false;
      if (this.consumer2 != null) {
        this.consumer2.accept(this.value4);
      }
    }
  }

  @Override
  protected void onDetached() {
    this.enabled = false;
    this.consumer = null;
    this.consumer2 = null;
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 80.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return this.enabled2 ? 48.0F : 10.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (!(this.effectiveOpacity < 0.003F)) {
      if (this.enabled2) {
        if (this.pressed != this.enabled4) {
          this.enabled4 = this.pressed;
          MotionAnimateService.animate(
              this,
              motionFiniteService,
              this.pressed ? 1.0F : 0.0F,
              this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
        }

        float y = 4.0F - 2.0F * Math.max(-0.2F, Math.min(1.0F, this.value6));
        float width = this.cx + 2.0F + this.value4 * (this.cw - 4.0F);
        float height = this.cy + (this.ch - 16.0F) / 2.0F;
        float currentValue = 6.0F + y / 2.0F;
        if (width - currentValue > this.cx) {
          this.updateState(
              compositorPushPresentationScale,
              this.cx,
              height,
              width - currentValue - this.cx,
              8.0F,
              2.0F);
        }

        if (width + currentValue < this.cx + this.cw) {
          this.updateState(
              compositorPushPresentationScale,
              width + currentValue,
              height,
              this.cx + this.cw - width - currentValue,
              2.0F,
              8.0F);
        }

        compositorPushPresentationScale.overlayRect(
            width - y / 2.0F,
            this.cy + (this.ch - 44.0F) / 2.0F,
            y,
            44.0F,
            2.0F,
            2.0F,
            2.0F,
            2.0F,
            mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
            0.0F,
            0.0F,
            0,
            0.0F,
            0);
        if (this.enabled3) {
          compositorPushPresentationScale.overlayRect(
              this.cx - 3.0F,
              this.cy,
              this.cw + 6.0F,
              this.ch,
              12.0F,
              12.0F,
              12.0F,
              12.0F,
              0,
              0.0F,
              0.0F,
              0,
              2.0F,
              mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity));
        }
      } else {
        compositorPushPresentationScale.alphaBar(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.value,
            this.value2,
            this.value3,
            this.value5,
            this.effectiveOpacity);
        float nextValue = Math.max(2.0F, this.ch * 0.5F - 1.0F);
        float previousValue =
            this.cx + Math.max(nextValue, Math.min(this.cw - nextValue, this.value4 * this.cw));
        float sourceValue = this.cy + this.ch * 0.5F;
        compositorPushPresentationScale.overlayRect(
            previousValue - nextValue,
            sourceValue - nextValue,
            nextValue * 2.0F,
            nextValue * 2.0F,
            nextValue,
            nextValue,
            nextValue,
            nextValue,
            mulAlpha(-1, this.effectiveOpacity),
            0.0F,
            2.0F * this.effectiveOpacity,
            mulAlpha(1610612736, this.effectiveOpacity),
            0.0F,
            0);
      }
    }
  }

  private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue) {
    compositorPushPresentationScale.pushClip(
        currentValue,
        nextValue,
        previousValue,
        16.0F,
        sourceValue,
        targetValue,
        targetValue,
        sourceValue);
    compositorPushPresentationScale.alphaBar(
        this.cx,
        nextValue,
        this.cw,
        16.0F,
        this.value,
        this.value2,
        this.value3,
        8.0F,
        this.effectiveOpacity);
    compositorPushPresentationScale.popClip();
  }

  private static float calculateValue(float value) {
    return !Float.isFinite(value) ? 0.0F : Math.max(0.0F, Math.min(1.0F, value));
  }

  private static boolean checkCondition(float value, float currentValue) {
    return Float.floatToIntBits(value) == Float.floatToIntBits(currentValue);
  }
}
