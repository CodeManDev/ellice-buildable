package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.awt.Color;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HueTrianglePicker extends ScenePctService<HueTrianglePicker> {
  private float value;
  private float value2 = 1.0F;
  private float value3 = 1.0F;
  private float value4;
  private HueTrianglePicker.Drag drag = HueTrianglePicker.Drag.NONE;
  private boolean enabled;
  private Consumer<Float> consumer;
  private BiConsumer<Float, Float> biConsumer;
  private Runnable runnable;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite("trianglePress", item -> item instanceof HueTrianglePicker);

  public HueTrianglePicker() {
    this.interactive = true;
  }

  public HueTrianglePicker hue(float currentValue) {
    float nextValue = ColorPickerMath.wrap(currentValue);
    if (this.value != nextValue) {
      this.value = nextValue;
      this.invalidate();
    }

    return this;
  }

  public HueTrianglePicker sat(float value) {
    float currentValue = calculateValue(value);
    if (this.value2 != currentValue) {
      this.value2 = currentValue;
      this.invalidate();
    }

    return this;
  }

  public HueTrianglePicker bri(float value) {
    float currentValue = calculateValue(value);
    if (this.value3 != currentValue) {
      this.value3 = currentValue;
      this.invalidate();
    }

    return this;
  }

  public HueTrianglePicker onHueChange(Consumer<Float> currentConsumer) {
    this.consumer = currentConsumer;
    return this;
  }

  public HueTrianglePicker onSBChange(BiConsumer<Float, Float> currentBiConsumer) {
    this.biConsumer = currentBiConsumer;
    return this;
  }

  public HueTrianglePicker onCommit(Runnable currentRunnable) {
    this.runnable = currentRunnable;
    return this;
  }

  public float hue() {
    return this.value;
  }

  public float sat() {
    return this.value2;
  }

  public float bri() {
    return this.value3;
  }

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  @Override
  public float getAnimProperty(String text) {
    return text.equals("trianglePress") ? this.value4 : super.getAnimProperty(text);
  }

  @Override
  public void setAnimProperty(String text, float value) {
    if (text.equals("trianglePress")) {
      this.value4 = value;
    } else {
      super.setAnimProperty(text, value);
    }
  }

  @Override
  protected void onPress(float currentValue, float nextValue) {
    float previousValue = Math.min(this.cw, this.ch);
    this.drag = HueTrianglePicker.Drag.NONE;
    this.enabled = false;
    if (!(previousValue <= 0.0F)) {
      float sourceValue = (currentValue - this.cx - this.cw / 2.0F) / previousValue;
      float targetValue = (nextValue - this.cy - this.ch / 2.0F) / previousValue;
      if (!(Math.hypot(sourceValue, targetValue) > 0.48499998450279236)) {
        this.drag =
            ColorPickerMath.contains(sourceValue, targetValue, this.value)
                ? HueTrianglePicker.Drag.TRIANGLE
                : HueTrianglePicker.Drag.HUE;
        MotionAnimateService.animate(this, motionFiniteService, 1.0F, MaterialEnterService.PRESS);
      }
    }
  }

  @Override
  protected void updateWhilePressed(float currentValue, float nextValue) {
    float previousValue = Math.min(this.cw, this.ch);
    if (this.drag != HueTrianglePicker.Drag.NONE && !(previousValue <= 0.0F)) {
      float sourceValue = (currentValue - this.cx - this.cw / 2.0F) / previousValue;
      float targetValue = (nextValue - this.cy - this.ch / 2.0F) / previousValue;
      if (this.drag == HueTrianglePicker.Drag.HUE) {
        if (Math.hypot(sourceValue, targetValue) < 0.025) {
          return;
        }

        float inputValue = ColorPickerMath.hue(sourceValue, targetValue);
        if (Math.abs(this.value - inputValue) < 1.0E-6) {
          return;
        }

        this.hue(inputValue);
        this.enabled = true;
        if (this.consumer != null) {
          this.consumer.accept(this.value);
        }
      } else {
        ColorPickerMath.SB sB = ColorPickerMath.pick(sourceValue, targetValue, this.value);
        if (Math.abs(this.value2 - sB.saturation()) < 1.0E-6
            && Math.abs(this.value3 - sB.brightness()) < 1.0E-6) {
          return;
        }

        this.sat(sB.saturation());
        this.bri(sB.brightness());
        this.enabled = true;
        if (this.biConsumer != null) {
          this.biConsumer.accept(this.value2, this.value3);
        }
      }
    }
  }

  @Override
  protected void onRelease(boolean currentEnabled) {
    this.drag = HueTrianglePicker.Drag.NONE;
    MotionAnimateService.animate(this, motionFiniteService, 0.0F, MaterialEnterService.RELEASE);
    boolean nextEnabled = this.enabled;
    this.enabled = false;
    if (nextEnabled && this.runnable != null) {
      this.runnable.run();
    }
  }

  @Override
  protected void onDetached() {
    this.drag = HueTrianglePicker.Drag.NONE;
    this.enabled = false;
    this.consumer = null;
    this.biConsumer = null;
    this.runnable = null;
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 192.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 192.0F;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (!(this.effectiveOpacity < 0.003F)) {
      compositorPushPresentationScale.colorTriangle(
          this.cx, this.cy, this.cw, this.ch, this.value, this.effectiveOpacity);
      float y = Math.min(this.cw, this.ch);
      ColorPickerMath.Point point = ColorPickerMath.marker(this.value, this.value2, this.value3);
      ColorPickerMath.Point currentPoint = ColorPickerMath.polar(this.value, 0.43F);
      float currentValue = Math.min(6.0F, y * 0.033F);
      this.updateState(
          compositorPushPresentationScale,
          point,
          y,
          currentValue + (this.drag == HueTrianglePicker.Drag.TRIANGLE ? 2.0F * this.value4 : 0.0F),
          Color.HSBtoRGB(this.value, this.value2, this.value3));
      this.updateState(
          compositorPushPresentationScale,
          currentPoint,
          y,
          currentValue + (this.drag == HueTrianglePicker.Drag.HUE ? 2.0F * this.value4 : 0.0F),
          Color.HSBtoRGB(this.value, 1.0F, 1.0F));
    }
  }

  private void updateState(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      ColorPickerMath.Point point,
      float value,
      float currentValue,
      int nextValue) {
    float currentX = this.cx + this.cw / 2.0F + point.x() * value;
    float currentY = this.cy + this.ch / 2.0F + point.y() * value;
    compositorPushPresentationScale.overlayRect(
        currentX - currentValue - 1.5F,
        currentY - currentValue - 1.5F,
        (currentValue + 1.5F) * 2.0F,
        (currentValue + 1.5F) * 2.0F,
        currentValue + 1.5F,
        currentValue + 1.5F,
        currentValue + 1.5F,
        currentValue + 1.5F,
        0,
        0.0F,
        0.0F,
        0,
        1.5F,
        mulAlpha(-1342177280, this.effectiveOpacity));
    compositorPushPresentationScale.overlayRect(
        currentX - currentValue,
        currentY - currentValue,
        currentValue * 2.0F,
        currentValue * 2.0F,
        currentValue,
        currentValue,
        currentValue,
        currentValue,
        mulAlpha(nextValue, this.effectiveOpacity),
        0.0F,
        0.0F,
        0,
        2.0F,
        mulAlpha(-1, this.effectiveOpacity));
  }

  private static float calculateValue(float value) {
    return Float.isFinite(value) ? Math.clamp(value, 0.0F, 1.0F) : 0.0F;
  }

  private enum Drag {
    NONE,
    HUE,
    TRIANGLE;

    private static HueTrianglePicker.Drag[] $values() {
      return new HueTrianglePicker.Drag[] {NONE, HUE, TRIANGLE};
    }
  }
}
