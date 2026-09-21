package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialCompactToggleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class SliderControl extends ScenePctService<SliderControl> {
  protected boolean materialStyle;
  private boolean enabled;
  private boolean enabled2;
  private float value2;
  private boolean enabled3;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.of(
          "materialPress",
          item -> item instanceof SliderControl,
          Float::isFinite,
          "a finite value");
  private float value3;
  private float value4;
  private float value5;
  private float value6 = 1.0F;
  private float value7;
  private int count = Integer.MIN_VALUE;
  private int count2 = Integer.MIN_VALUE;
  private int count3 = Integer.MIN_VALUE;
  private int count4 = Integer.MIN_VALUE;
  private int count5 = Integer.MIN_VALUE;
  private float value8 = Float.NaN;
  private String text2 = "%.1f";
  private float value9 = Float.NaN;
  private float value10 = Float.NaN;
  private float value11 = Float.NaN;
  private float value12 = -1.0F;
  private float value13 = 1.0F;
  private static final SceneEaseHandler.Spring spring = new SceneEaseHandler.Spring(26.0F, 600.0F);
  private Consumer<Float> consumer;
  private Consumer<Float> consumer2;
  private boolean enabled4;

  public SliderControl compact(boolean currentEnabled) {
    this.enabled = currentEnabled;
    return this;
  }

  public SliderControl material(boolean enabled) {
    this.materialStyle = enabled;
    this.invalidate();
    return this;
  }

  public void materialFocus(boolean enabled) {
    this.enabled2 = enabled;
    this.invalidate();
  }

  private void updateState() {
    if (this.pressed != this.enabled3) {
      this.enabled3 = this.pressed;
      MotionAnimateService.animate(
          this,
          motionFiniteService,
          this.pressed ? 1.0F : 0.0F,
          this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
    }
  }

  public SliderControl() {
    this.interactive = true;
  }

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  public SliderControl value(float currentValue) {
    if (!Float.isFinite(currentValue)) {
      return this;
    }

    this.value4 = currentValue;
    this.updateState3(this.calculateValue4(currentValue), false);
    return this;
  }

  public SliderControl range(float value, float currentValue) {
    if (!Float.isFinite(value) || !Float.isFinite(currentValue) || currentValue < value) {
      throw new IllegalArgumentException("slider range must be finite and max >= min");
    }

    if (checkCondition(this.value5, value) && checkCondition(this.value6, currentValue)) {
      return this;
    }

    this.value5 = value;
    this.value6 = currentValue;
    this.updateState3(this.calculateValue4(this.value4), false);
    this.invalidateLayout();
    return this;
  }

  public SliderControl step(float value) {
    if (!Float.isFinite(value) || value < 0.0F) {
      throw new IllegalArgumentException("slider step must be finite and >= 0");
    }

    if (checkCondition(this.value7, value)) {
      return this;
    }

    this.value7 = value;
    this.updateState3(this.calculateValue4(this.value4), false);
    this.invalidate();
    return this;
  }

  public float value() {
    return this.value3;
  }

  public SliderControl trackColor(int color) {
    if (this.count != color) {
      this.count = color;
      this.invalidate();
    }

    return this;
  }

  public SliderControl fillColor(int color) {
    if (this.count2 != color) {
      this.count2 = color;
      this.invalidate();
    }

    return this;
  }

  public SliderControl thumbColor(int color) {
    if (this.count3 != color) {
      this.count3 = color;
      this.invalidate();
    }

    return this;
  }

  public SliderControl thumbShadowColor(int color) {
    if (this.count4 != color) {
      this.count4 = color;
      this.invalidate();
    }

    return this;
  }

  public SliderControl textColor(int color) {
    if (this.count5 != color) {
      this.count5 = color;
      this.invalidate();
    }

    return this;
  }

  public SliderControl trackHeight(float value) {
    if (Float.floatToIntBits(this.value9) == Float.floatToIntBits(value)) {
      return this;
    }

    this.value9 = value;
    this.invalidateLayout();
    return this;
  }

  public SliderControl thumbRadius(float value) {
    if (!checkCondition(this.value10, value)) {
      this.value10 = value;
      this.invalidateLayout();
    }

    return this;
  }

  public SliderControl thumbShadow(float value) {
    if (!checkCondition(this.value11, value)) {
      this.value11 = value;
      this.invalidate();
    }

    return this;
  }

  public SliderControl trackRadius(float value) {
    if (!checkCondition(this.value12, value)) {
      this.value12 = value;
      this.invalidate();
    }

    return this;
  }

  public SliderControl fontSize(float value) {
    if (Float.floatToIntBits(this.value8) == Float.floatToIntBits(value)) {
      return this;
    }

    this.value8 = value;
    this.invalidateLayout();
    return this;
  }

  public SliderControl format(String text) {
    if (Objects.equals(this.text2, text)) {
      return this;
    }

    this.text2 = Objects.requireNonNull(text, "format");
    this.invalidateLayout();
    return this;
  }

  public SliderControl onChange(Consumer<Float> currentConsumer) {
    this.consumer = currentConsumer;
    return this;
  }

  public SliderControl onCommit(Consumer<Float> consumer) {
    this.consumer2 = consumer;
    return this;
  }

  private ThemeIsSetService collectValues() {
    return CoreIsInitializedHandler.get().theme();
  }

  private int calculateValue(int value, String text) {
    return ThemeIsSetService.isSet(value) ? value : this.collectValues().color(text);
  }

  private float calculateValue2(float value, String text) {
    return ThemeIsSetService.isSet(value) ? value : this.collectValues().number(text);
  }

  @Override
  public float getAnimProperty(String text) {
    if ("materialPress".equals(text)) {
      return this.value2;
    } else {
      return "thumbScale".equals(text) ? this.value13 : super.getAnimProperty(text);
    }
  }

  @Override
  public void setAnimProperty(String text, float value) {
    if ("materialPress".equals(text)) {
      this.value2 = value;
    } else if ("thumbScale".equals(text)) {
      this.value13 = value;
    } else {
      super.setAnimProperty(text, value);
    }
  }

  private float calculateValue3() {
    return this.value6 > this.value5
        ? (this.value3 - this.value5) / (this.value6 - this.value5)
        : 0.0F;
  }

  private static boolean checkCondition(float value, float currentValue) {
    return Float.floatToIntBits(value) == Float.floatToIntBits(currentValue);
  }

  private float calculateValue4(float value) {
    value = Math.max(this.value5, Math.min(this.value6, value));
    if (this.value7 > 0.0F) {
      value = this.value5 + Math.round((value - this.value5) / this.value7) * this.value7;
    }

    return Math.max(this.value5, Math.min(this.value6, value));
  }

  private void updateState2(float value) {
    float currentValue = this.calculateValue4(value);
    if (Float.compare(currentValue, this.value3) != 0) {
      this.enabled4 = true;
    }

    this.value4 = currentValue;
    this.updateState3(currentValue, true);
  }

  private void updateState3(float value, boolean enabled) {
    if (Float.compare(value, this.value3) != 0) {
      this.value3 = value;
      this.invalidate();
      if (enabled && this.consumer != null) {
        this.consumer.accept(this.value3);
      }
    }
  }

  @Override
  protected void updateWhilePressed(float value, float currentValue) {
    if (!(this.cw <= 0.0F) && this.value6 > this.value5) {
      float nextValue =
          this.materialStyle
              ? 4.0F
              : this.calculateValue2(this.value10, "slider.thumb-radius")
                  * 2.0F
                  * Math.max(0.0F, Math.min(2.0F, this.value13));
      float previousValue = Math.max(1.0F, this.cw - nextValue);
      float sourceValue = (value - this.cx - nextValue / 2.0F) / previousValue;
      this.updateState2(
          this.value5 + Math.max(0.0F, Math.min(1.0F, sourceValue)) * (this.value6 - this.value5));
    }
  }

  @Override
  protected boolean handleScroll(float value) {
    this.updateState2(
        this.value3
            + value * (this.value7 > 0.0F ? this.value7 : (this.value6 - this.value5) / 50.0F));
    this.updateState4();
    return true;
  }

  @Override
  protected void onRelease(boolean enabled) {
    this.updateState4();
  }

  private void updateState4() {
    if (this.enabled4) {
      this.enabled4 = false;
      if (this.consumer2 != null) {
        this.consumer2.accept(this.value3);
      }
    }
  }

  @Override
  public float intrinsicWidth(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    return 100.0F;
  }

  @Override
  public float intrinsicHeight(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.materialStyle) {
      return 48.0F;
    }

    float value = this.calculateValue2(this.value9, "slider.track-height");
    float currentValue = this.calculateValue2(this.value8, "slider.font-size");
    return Math.max(value + 2.0F, currentValue + value + 4.0F);
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.materialStyle) {
      this.updateState();
      if (this.enabled) {
        MaterialCompactToggleService.compactSlider(
            compositorPushPresentationScale,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            0.0F,
            this.calculateValue3(),
            false,
            true,
            this.value2,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      } else {
        MaterialCompactToggleService.slider(
            compositorPushPresentationScale,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            0.0F,
            this.calculateValue3(),
            false,
            true,
            this.value2,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      }
    } else {
      float y = this.effectiveOpacity;
      float width = this.effectiveEdgeSoftness;
      float currentHeight = this.calculateValue3();
      int currentCount = this.calculateValue(this.count, "slider.track");
      int value = this.calculateValue(this.count2, "slider.fill");
      int currentValue = this.calculateValue(this.count3, "slider.thumb");
      int nextValue = this.calculateValue(this.count4, "slider.thumb-shadow");
      int previousValue = this.calculateValue(this.count5, "slider.text");
      float sourceValue = this.calculateValue2(this.value8, "slider.font-size");
      float targetValue = this.calculateValue2(this.value9, "slider.track-height");
      float inputValue = this.calculateValue2(this.value10, "slider.thumb-radius");
      float outputValue = this.calculateValue2(this.value11, "slider.thumb-shadow-size");
      float resultValue = this.cy + this.ch - targetValue - 1.0F;
      float candidateValue = this.value12 >= 0.0F ? this.value12 : targetValue / 2.0F;
      float selectedValue = !this.hovered && !this.pressed ? 1.0F : 1.4F;
      if (Math.abs(this.value13 - selectedValue) > 0.05F) {
        this.animate("thumbScale", selectedValue, spring);
      }

      compositorPushPresentationScale.text(
          this.cx,
          this.cy,
          String.format(Locale.ROOT, this.text2, this.value3),
          sourceValue,
          mulAlpha(previousValue, y));
      compositorPushPresentationScale.roundedRect(
          this.cx,
          resultValue,
          this.cw,
          targetValue,
          candidateValue,
          candidateValue,
          candidateValue,
          candidateValue,
          mulAlpha(currentCount, y),
          0.0F,
          0.0F,
          0,
          0.0F,
          0,
          1.0F,
          width);
      float defaultValue = inputValue * 2.0F * Math.max(0.0F, Math.min(2.0F, this.value13));
      float initialValue = Math.max(0.0F, this.cw - defaultValue);
      float resolvedValue = this.cx + initialValue * currentHeight;
      float computedValue = resolvedValue + defaultValue / 2.0F;
      float cachedValue = Math.max(targetValue, computedValue - this.cx);
      compositorPushPresentationScale.roundedRect(
          this.cx,
          resultValue,
          cachedValue,
          targetValue,
          candidateValue,
          candidateValue,
          candidateValue,
          candidateValue,
          mulAlpha(value, y),
          0.0F,
          0.0F,
          0,
          0.0F,
          0,
          1.0F,
          width);
      float pendingValue = resultValue + (targetValue - defaultValue) / 2.0F;
      float activeValue = defaultValue / 2.0F;
      float fallbackValue = y > 0.15F ? outputValue : 0.0F;
      compositorPushPresentationScale.roundedRect(
          resolvedValue,
          pendingValue,
          defaultValue,
          defaultValue,
          activeValue,
          activeValue,
          activeValue,
          activeValue,
          mulAlpha(currentValue, y),
          0.0F,
          fallbackValue,
          mulAlpha(nextValue, y),
          0.0F,
          0,
          1.0F,
          width);
    }
  }
}
