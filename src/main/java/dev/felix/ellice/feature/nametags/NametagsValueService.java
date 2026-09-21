package dev.felix.ellice.feature.nametags;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.theme.ThemeMixService;

public final class NametagsValueService extends ScenePctService<NametagsValueService> {
  public static final MotionFiniteService HEALTH = createMotionFiniteService("tagHealth");
  public static final MotionFiniteService ABSORPTION = createMotionFiniteService("tagAbsorption");
  private float value2;
  private float value3;
  private float value4 = -1.0F;
  private float value5 = -1.0F;

  public NametagsValueService value(float currentValue, float nextValue, boolean enabled) {
    int previousValue = this.value4 < 0.0F ? 1 : 0;
    if (previousValue == 0 && enabled) {
      if (currentValue != this.value4) {
        MotionAnimateService.animate(
            this, HEALTH, currentValue, SceneEaseHandler.Tween.ease(0.18F));
      }

      if (nextValue != this.value5) {
        MotionAnimateService.animate(
            this, ABSORPTION, nextValue, SceneEaseHandler.Tween.ease(0.18F));
      }
    } else {
      MotionAnimateService.cancel(this, HEALTH);
      MotionAnimateService.cancel(this, ABSORPTION);
      HEALTH.set(this, currentValue);
      ABSORPTION.set(this, nextValue);
    }

    this.value4 = currentValue;
    this.value5 = nextValue;
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "tagHealth" -> this.value2;
      case "tagAbsorption" -> this.value3;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float value) {
    switch (text) {
      case "tagHealth":
        this.value2 = value;
        break;
      case "tagAbsorption":
        this.value3 = value;
        break;
      default:
        super.setAnimProperty(text, value);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = Math.max(1.0F, this.value2 + this.value3);
    float width = this.cw * this.value2 / y;
    float height = this.ch / 2.0F;
    int value =
        MaterialIsLightService.layer(
            MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.ON_SURFACE, 0.18F);
    compositorPushPresentationScale.roundedRect(
        this.cx, this.cy, this.cw, this.ch, height, mulAlpha(value, this.effectiveOpacity));
    float currentValue = Math.min(1.0F, this.ch * 0.25F);
    float nextValue = this.cy + currentValue;
    float previousValue = Math.max(1.0F, this.ch - currentValue * 2.0F);
    float sourceValue = previousValue / 2.0F;
    if (width > 0.1F) {
      int targetValue =
          this.value2 < 0.5F
              ? ThemeMixService.mix(MaterialIsLightService.ERROR, -14739, this.value2 * 2.0F)
              : ThemeMixService.mix(-14739, -10890342, (this.value2 - 0.5F) * 2.0F);
      compositorPushPresentationScale.roundedRect(
          this.cx,
          nextValue,
          width,
          previousValue,
          sourceValue,
          mulAlpha(targetValue, this.effectiveOpacity));
      float inputValue = Math.min(1.0F, previousValue * 0.45F);
      compositorPushPresentationScale.roundedRect(
          this.cx + sourceValue,
          nextValue,
          Math.max(0.0F, width - sourceValue),
          inputValue,
          inputValue / 2.0F,
          mulAlpha(ThemeMixService.lighten(targetValue, 0.22F), this.effectiveOpacity * 0.8F));
    }

    float outputValue = this.cw * this.value3 / y;
    if (outputValue > 0.1F) {
      int resultValue = -1454447;
      compositorPushPresentationScale.roundedRect(
          this.cx + width,
          nextValue,
          outputValue,
          previousValue,
          sourceValue,
          mulAlpha(resultValue, this.effectiveOpacity));
      float candidateValue = Math.min(1.0F, previousValue * 0.45F);
      compositorPushPresentationScale.roundedRect(
          this.cx + width + sourceValue,
          nextValue,
          Math.max(0.0F, outputValue - sourceValue),
          candidateValue,
          candidateValue / 2.0F,
          mulAlpha(ThemeMixService.lighten(resultValue, 0.18F), this.effectiveOpacity * 0.8F));
      if (width > 0.1F) {
        compositorPushPresentationScale.roundedRect(
            this.cx + width - 0.5F,
            this.cy + currentValue,
            1.0F,
            previousValue,
            0.5F,
            mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity));
      }
    }
  }

  private static MotionFiniteService createMotionFiniteService(String text) {
    return MotionFiniteService.of(
        text,
        item -> item instanceof NametagsValueService,
        item -> Float.isFinite(item) && item >= 0.0F && item <= 1.0F,
        "a fraction in [0, 1]");
  }
}
