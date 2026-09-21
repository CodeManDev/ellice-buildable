package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class MaterialSelectionSelector extends LayoutContainerNode {
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite(
          "materialChoiceLeft", item -> item instanceof MaterialSelectionSelector);
  private static final MotionFiniteService motionFiniteService2 =
      MotionFiniteService.finite(
          "materialChoiceRight", item -> item instanceof MaterialSelectionSelector);
  private String text = "";
  private float value;
  private float value2;
  private float value3;
  private float value4;
  private float value5 = -1.0F;
  private float value6;
  private float value7;
  private float value8;
  private boolean enabled;
  private boolean enabled2;

  public MaterialSelectionSelector() {
    this.direction(ScenePctService.Direction.ROW);
    this.onLayout(this::updateState);
  }

  public MaterialSelectionSelector selection(String currentText) {
    if (!this.text.equals(currentText)) {
      this.text = currentText;
      this.enabled2 = true;
    }

    return this;
  }

  private void updateState() {
    ScenePctService scenePct =
        this.children().stream()
            .filter(item -> item.visible && this.text.equals(item.getId()))
            .findFirst()
            .orElse(null);
    if (scenePct == null) {
      this.enabled = false;
    } else {
      float currentValue = scenePct.computedX() - this.computedX() - this.scrollY();
      float nextValue = currentValue + scenePct.computedW();
      this.value7 = scenePct.computedY() - this.computedY();
      this.value8 = scenePct.computedH();
      int previousValue = Math.abs(this.value5 - this.computedW()) > 0.5F ? 1 : 0;
      this.value5 = this.computedW();
      this.value6 =
          this.children().stream()
              .filter(item -> item.visible)
              .map(item -> item.computedX() - this.computedX() - this.scrollY() + item.computedW())
              .max(Float::compare)
              .orElse(nextValue);
      if (!this.enabled || previousValue != 0) {
        MotionAnimateService.cancel(this, motionFiniteService);
        MotionAnimateService.cancel(this, motionFiniteService2);
        this.value = this.value3 = currentValue;
        this.value2 = this.value4 = nextValue;
        this.enabled = true;
      } else if (currentValue != this.value3 || nextValue != this.value4) {
        int sourceValue = currentValue + nextValue >= this.value3 + this.value4 ? 1 : 0;
        this.value3 = currentValue;
        this.value4 = nextValue;
        MotionAnimateService.animate(
            this,
            motionFiniteService,
            currentValue,
            sourceValue != 0
                ? MaterialEnterService.TRAILING_EDGE
                : MaterialEnterService.LEADING_EDGE);
        MotionAnimateService.animate(
            this,
            motionFiniteService2,
            nextValue,
            sourceValue != 0
                ? MaterialEnterService.LEADING_EDGE
                : MaterialEnterService.TRAILING_EDGE);
      }

      if (this.enabled2 || previousValue != 0) {
        this.enabled2 = false;
        float targetValue = this.scrollTarget;
        if (currentValue + targetValue < this.paddingLeft) {
          targetValue = this.paddingLeft - currentValue;
        } else if (nextValue + targetValue > this.computedW() - this.paddingRight) {
          targetValue = this.computedW() - this.paddingRight - nextValue;
        }

        targetValue = Math.max(this.scrollMin(), Math.min(0.0F, targetValue));
        if (targetValue != this.scrollTarget) {
          this.scrollTarget(targetValue);
          MotionAnimateService.animate(
              this, MotionColorsContainer.Floats.SCROLL_Y, targetValue, MaterialEnterService.PAGE);
        }
      }
    }
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "materialChoiceLeft" -> this.value;
      case "materialChoiceRight" -> this.value2;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    switch (text) {
      case "materialChoiceLeft":
        this.value = currentValue;
        break;
      case "materialChoiceRight":
        this.value2 = currentValue;
        break;
      default:
        super.setAnimProperty(text, currentValue);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.enabled) {
      float y = this.presentationScale;
      compositorPushPresentationScale.pushClip(this.cx, this.cy, this.cw, this.ch);
      compositorPushPresentationScale.roundedRect(
          this.cx + (this.paddingLeft + this.scrollY()) * y,
          this.cy + this.value7 * y,
          (this.value6 - this.paddingLeft) * y,
          this.value8 * y,
          16.0F,
          mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity));
      float width =
          Math.max(24.0F, Math.min(this.value4 - this.value3 + 32.0F, this.value2 - this.value));
      float height =
          Math.max(
              this.paddingLeft + width / 2.0F,
              Math.min(this.value6 - width / 2.0F, (this.value + this.value2) / 2.0F));
      compositorPushPresentationScale.roundedRect(
          this.cx + (this.scrollY() + height - width / 2.0F) * y,
          this.cy + this.value7 * y,
          width * y,
          this.value8 * y,
          16.0F,
          mulAlpha(MaterialIsLightService.SECONDARY_CONTAINER, this.effectiveOpacity));
      compositorPushPresentationScale.popClip();
    }
  }
}
