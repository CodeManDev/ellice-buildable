package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class AnimatedSelectionIndicator extends LayoutContainerNode {
  public static final float TAB_WIDTH = 112.0F;
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite(
          "materialIndicatorLeft", item -> item instanceof AnimatedSelectionIndicator);
  private static final MotionFiniteService motionFiniteService2 =
      MotionFiniteService.finite(
          "materialIndicatorRight", item -> item instanceof AnimatedSelectionIndicator);
  private float value;
  private float value2;
  private float value3;
  private float value4;
  private float value5 = -1.0F;
  private float value6;
  private float value7;
  private int count = -1;
  private boolean enabled;
  private boolean enabled2;

  public AnimatedSelectionIndicator() {
    this.direction(ScenePctService.Direction.ROW);
    this.onLayout(
        () -> {
          this.value6 = this.computedX();
          this.value7 = this.computedW();
        });
  }

  public AnimatedSelectionIndicator selection(int value) {
    if (this.count == value) {
      return this;
    }

    this.count = value;
    this.enabled2 = true;
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "materialIndicatorLeft" -> this.value;
      case "materialIndicatorRight" -> this.value2;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    switch (text) {
      case "materialIndicatorLeft":
        this.value = currentValue;
        break;
      case "materialIndicatorRight":
        this.value2 = currentValue;
        break;
      default:
        super.setAnimProperty(text, currentValue);
    }
  }

  private void updateState(float currentValue, float nextValue) {
    if (!this.enabled) {
      this.enabled = true;
      this.value = this.value3 = currentValue;
      this.value2 = this.value4 = nextValue;
    } else if (this.value3 != currentValue || this.value4 != nextValue) {
      int previousValue = currentValue + nextValue >= this.value3 + this.value4 ? 1 : 0;
      this.value3 = currentValue;
      this.value4 = nextValue;
      MotionAnimateService.animate(
          this,
          motionFiniteService,
          currentValue,
          previousValue != 0
              ? MaterialEnterService.TRAILING_EDGE
              : MaterialEnterService.LEADING_EDGE);
      MotionAnimateService.animate(
          this,
          motionFiniteService2,
          nextValue,
          previousValue != 0
              ? MaterialEnterService.LEADING_EDGE
              : MaterialEnterService.TRAILING_EDGE);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = this.presentationScale;
    float width = this.value7;
    if (this.count >= 0 && this.count < this.children().size()) {
      ScenePctService scenePct = this.children().get(this.count);
      float currentValue = scenePct.computedX() - this.value6 - this.scrollY;
      float nextValue = scenePct.computedW();
      if (this.enabled2 || width != this.value5) {
        this.enabled2 = false;
        this.value5 = width;
        float previousValue = this.scrollTarget;
        if (currentValue + previousValue < this.paddingLeft) {
          previousValue = this.paddingLeft - currentValue;
        } else if (currentValue + nextValue + previousValue > width - this.paddingRight) {
          previousValue = width - this.paddingRight - currentValue - nextValue;
        }

        previousValue = Math.max(this.scrollMin, Math.min(0.0F, previousValue));
        this.scrollTarget(previousValue);
        MotionAnimateService.animate(
            this, MotionColorsContainer.Floats.SCROLL_Y, previousValue, MaterialEnterService.PAGE);
      }

      float sourceValue = 24.0F;
      if (!scenePct.children().isEmpty()
          && scenePct.children().getFirst() instanceof SceneTextService sceneText) {
        compositorPushPresentationScale.pushPresentationScale(1.0F);

        try {
          sourceValue =
              Math.max(
                  24.0F,
                  Math.min(
                      nextValue - 24.0F,
                      sceneText.intrinsicWidth(compositorPushPresentationScale)));
        } finally {
          compositorPushPresentationScale.popPresentationScale();
        }
      }

      float targetValue = currentValue + nextValue / 2.0F;
      this.updateState(targetValue - sourceValue / 2.0F, targetValue + sourceValue / 2.0F);
      float inputValue = Math.max(12.0F, Math.min(sourceValue + 40.0F, this.value2 - this.value));
      float outputValue = (this.value + this.value2) / 2.0F;
      compositorPushPresentationScale.roundedRect(
          this.cx + (this.scrollY + outputValue - inputValue / 2.0F) * y,
          this.cy + this.ch - 3.0F * y,
          inputValue * y,
          3.0F * y,
          3.0F,
          mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity));
    }
  }
}
