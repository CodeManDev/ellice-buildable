package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class MaterialJoinedService extends SceneCornerRadiusService {
  private int count = MaterialIsLightService.SECONDARY_CONTAINER;
  private int count2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
  private float value = 20.0F;
  private float value2 = 40.0F;
  private float value3;
  private boolean enabled = true;
  private Integer integer;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private int count3;
  private boolean enabled5;
  private boolean enabled6;
  private float value4 = 0.5F;
  private float value5 = 0.5F;
  private float value6;
  private boolean enabled7;
  private boolean enabled8;
  private boolean enabled9;
  private boolean enabled10;
  private float value7 = 4.0F;
  private boolean enabled11;
  private boolean enabled12;
  private boolean enabled13;
  private float value8;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite("materialDepth", item -> item instanceof MaterialJoinedService);
  private static final MotionFiniteService motionFiniteService2 =
      MotionFiniteService.finite(
          "materialInnerRadius", item -> item instanceof MaterialJoinedService);
  private static final MotionFiniteService motionFiniteService3 =
      MotionFiniteService.finite("materialRipple", item -> item instanceof MaterialJoinedService);

  public MaterialJoinedService joined(boolean enabled, boolean currentEnabled) {
    this.enabled8 = true;
    this.enabled9 = enabled;
    this.enabled10 = currentEnabled;
    return this;
  }

  public MaterialJoinedService joined(
      boolean enabled, boolean currentEnabled, boolean nextEnabled) {
    this.joined(enabled, currentEnabled);
    float value = nextEnabled ? 20.0F : 4.0F;
    if (!this.enabled11) {
      this.enabled11 = true;
      this.value7 = value;
    } else if (this.enabled12 != nextEnabled) {
      MotionAnimateService.animate(this, motionFiniteService2, value, MaterialEnterService.RELEASE);
    }

    this.enabled12 = nextEnabled;
    return this;
  }

  public MaterialJoinedService surfaceWidth(float value) {
    this.value3 = value;
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "materialRipple" -> this.value6;
      case "materialInnerRadius" -> this.value7;
      case "materialDepth" -> this.value8;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float value) {
    if ("materialDepth".equals(text)) {
      this.value8 = value;
    } else if ("materialRipple".equals(text)) {
      this.value6 = value;
    } else if ("materialInnerRadius".equals(text)) {
      this.value7 = value;
    } else {
      super.setAnimProperty(text, value);
    }
  }

  public MaterialJoinedService() {
    this.interactive(true).cursorStyle(ScenePctService.CursorStyle.POINTER).stopPropagation(true);
    this.cornerRadius(this.value);
  }

  public MaterialJoinedService colors(int color, int currentColor) {
    this.count = color;
    this.count2 = currentColor;
    if (!this.enabled4) {
      this.enabled4 = true;
      this.backgroundColor(color);
    } else if (this.count3 != color) {
      MotionAnimateService.animate(
          this,
          MotionColorsContainer.Colors.BACKGROUND,
          color,
          MaterialIsLightService.FAST_EFFECTS);
    }

    this.count3 = color;
    return this;
  }

  public MaterialJoinedService shape(float currentValue, float nextValue) {
    if (!this.enabled13) {
      this.enabled13 = true;
      this.value = currentValue;
      this.cornerRadius(currentValue);
    } else if (this.value != currentValue) {
      this.value = currentValue;
      if (!this.pressed) {
        MotionAnimateService.animate(
            this,
            MotionColorsContainer.Floats.CORNER_RADIUS,
            currentValue,
            MaterialIsLightService.FAST_SPATIAL);
      }
    }

    this.value2 = nextValue;
    return this;
  }

  public MaterialJoinedService outlined(boolean enabled) {
    this.enabled3 = enabled;
    return this;
  }

  public MaterialJoinedService available(boolean currentEnabled) {
    if (this.enabled && !currentEnabled) {
      this.enabled2 = false;
      this.enabled7 = false;
      MotionAnimateService.cancel(this, motionFiniteService);
      motionFiniteService.set(this, 0.0F);
      MotionAnimateService.cancel(this, MotionColorsContainer.Floats.CORNER_RADIUS);
      this.cornerRadius(this.value);
    }

    this.enabled = currentEnabled;
    this.interactive(currentEnabled)
        .pointerEvents(currentEnabled)
        .cursorStyle(
            currentEnabled
                ? ScenePctService.CursorStyle.POINTER
                : ScenePctService.CursorStyle.DEFAULT);
    return this;
  }

  public boolean available() {
    return this.enabled;
  }

  public MaterialJoinedService disabledSurface(int value) {
    this.integer = value;
    return this;
  }

  public void keyboardFocused(boolean enabled) {
    this.enabled2 = enabled;
    this.invalidate();
  }

  public void activate() {
    if (this.enabled && this.onClick != null) {
      motionFiniteService.set(this, 0.65F);
      MotionAnimateService.animate(this, motionFiniteService, 0.0F, MaterialEnterService.RELEASE);
      this.onClick.run();
    }
  }

  @Override
  protected void onPress(float currentValue, float nextValue) {
    if (this.enabled) {
      this.value4 =
          this.cw > 0.0F
              ? Math.max(0.0F, Math.min(1.0F, (currentValue - this.cx) / this.cw))
              : 0.5F;
      this.value5 =
          this.ch > 0.0F ? Math.max(0.0F, Math.min(1.0F, (nextValue - this.cy) / this.ch)) : 0.5F;
      this.enabled7 = true;
      motionFiniteService3.set(this, 0.0F);
      MotionAnimateService.animate(
          this, motionFiniteService3, 1.0F, MaterialIsLightService.EFFECTS);
      MotionAnimateService.animate(this, motionFiniteService, 1.0F, MaterialEnterService.PRESS);
      MotionAnimateService.animate(
          this,
          MotionColorsContainer.Floats.CORNER_RADIUS,
          Math.min(10.0F, this.value),
          MaterialEnterService.PRESS);
    }
  }

  @Override
  protected void onRelease(boolean currentEnabled) {
    if (this.enabled) {
      MotionAnimateService.animate(this, motionFiniteService, 0.0F, MaterialEnterService.RELEASE);
      MotionAnimateService.animate(
          this,
          MotionColorsContainer.Floats.CORNER_RADIUS,
          this.value,
          MaterialEnterService.RELEASE);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = Math.min(this.ch, this.value2 <= 0.0F ? this.ch : this.value2);
    float width = this.value3 > 0.0F ? Math.min(this.cw, this.value3) : this.cw;
    float height =
        this.value == 0.0F
            ? 0.0F
            : Math.max(-0.28F, Math.min(1.0F, this.value8)) * this.presentationScale;
    float currentValue = Math.max(1.0F, y - height * 3.5F);
    float nextValue = Math.max(1.0F, width - height * 2.5F);
    float previousValue = this.cx + (this.cw - nextValue) / 2.0F;
    float sourceValue = this.cy + (this.ch - currentValue) / 2.0F;
    float targetValue =
        Math.max(0.0F, Math.min(currentValue / 2.0F, this.getAnimProperty("cornerRadius")));
    int inputValue =
        this.enabled
            ? this.backgroundColor
            : (this.integer != null
                ? this.integer
                : MaterialIsLightService.layer(
                    MaterialIsLightService.SURFACE_CONTAINER,
                    MaterialIsLightService.ON_SURFACE,
                    0.12F));
    if (this.enabled5 != this.hovered || this.enabled6 != this.pressed) {
      this.enabled5 = this.hovered;
      this.enabled6 = this.pressed;
      MotionAnimateService.animate(
          this,
          MotionColorsContainer.Floats.HOVER_PROGRESS,
          this.hovered ? 1.0F : 0.0F,
          MaterialIsLightService.FAST_EFFECTS);
      MotionAnimateService.animate(
          this,
          MotionColorsContainer.Floats.PRESS_PROGRESS,
          this.pressed ? 1.0F : 0.0F,
          MaterialIsLightService.FAST_EFFECTS);
    }

    float outputValue =
        this.enabled8 && !this.enabled9
            ? Math.max(0.0F, Math.min(this.value7, targetValue))
            : targetValue;
    float resultValue =
        this.enabled8 && !this.enabled10
            ? Math.max(0.0F, Math.min(this.value7, targetValue))
            : targetValue;
    float candidateValue = this.enabled3 ? 0.5F : 0.0F;
    float selectedValue = inputValue >>> 24 > 0 ? Math.max(0.0F, this.blur) : 0.0F;
    compositorPushPresentationScale.roundedRect(
        previousValue + candidateValue,
        sourceValue + candidateValue,
        nextValue - 2.0F * candidateValue,
        currentValue - 2.0F * candidateValue,
        Math.max(0.0F, outputValue - candidateValue),
        Math.max(0.0F, resultValue - candidateValue),
        Math.max(0.0F, resultValue - candidateValue),
        Math.max(0.0F, outputValue - candidateValue),
        selectedValue > 0.0F ? inputValue : mulAlpha(inputValue, this.effectiveOpacity),
        selectedValue,
        0.0F,
        0,
        this.enabled3 ? 1.0F : 0.0F,
        mulAlpha(MaterialIsLightService.OUTLINE_VARIANT, this.effectiveOpacity),
        selectedValue > 0.0F ? this.effectiveOpacity : 1.0F,
        this.effectiveEdgeSoftness);
    if (this.enabled) {
      float defaultValue =
          this.enabled2
              ? 0.1F
              : 0.08F * this.getAnimProperty("hoverProgress")
                  + 0.02F * this.getAnimProperty("pressProgress");
      compositorPushPresentationScale.roundedRect(
          previousValue,
          sourceValue,
          nextValue,
          currentValue,
          outputValue,
          resultValue,
          resultValue,
          outputValue,
          mulAlpha(this.count2, this.effectiveOpacity * defaultValue),
          0.0F,
          0.0F,
          0,
          0.0F,
          0,
          1.0F,
          this.effectiveEdgeSoftness);
    }

    if (this.enabled7 && this.value6 < 0.995F && this.enabled) {
      compositorPushPresentationScale.pushClip(
          previousValue,
          sourceValue,
          nextValue,
          currentValue,
          outputValue,
          resultValue,
          resultValue,
          outputValue);
      float initialValue = (float) Math.hypot(this.cw, currentValue) * Math.max(0.0F, this.value6);
      float resolvedValue = this.cx + this.value4 * this.cw;
      float computedValue = this.cy + this.value5 * this.ch;
      compositorPushPresentationScale.roundedRect(
          resolvedValue - initialValue,
          computedValue - initialValue,
          initialValue * 2.0F,
          initialValue * 2.0F,
          initialValue,
          mulAlpha(this.count2, this.effectiveOpacity * 0.1F * (1.0F - this.value6)));
      compositorPushPresentationScale.popClip();
    }

    if (this.enabled2 && this.enabled) {
      float cachedValue = y >= this.ch ? -3.0F : 3.0F;
      float pendingValue = Math.max(0.0F, outputValue + cachedValue);
      float activeValue = Math.max(0.0F, resultValue + cachedValue);
      compositorPushPresentationScale.roundedRect(
          previousValue - cachedValue,
          sourceValue - cachedValue,
          nextValue + cachedValue * 2.0F,
          currentValue + cachedValue * 2.0F,
          pendingValue,
          activeValue,
          activeValue,
          pendingValue,
          0,
          0.0F,
          0.0F,
          0,
          2.0F,
          mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity),
          1.0F,
          this.effectiveEdgeSoftness);
    }
  }
}
