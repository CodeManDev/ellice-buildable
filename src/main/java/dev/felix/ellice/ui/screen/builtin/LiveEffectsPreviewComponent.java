package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;

final class LiveEffectsPreviewComponent extends ScenePctService<LiveEffectsPreviewComponent> {
  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float y = this.presentationScale;
    float width = 14.0F * y;
    compositorPushPresentationScale.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        20.0F,
        mulAlpha(MaterialIsLightService.SURFACE_LOW, this.effectiveOpacity));
    float height = this.cw / 8.0F;

    for (int index = 0; index < 8; index++) {
      compositorPushPresentationScale.roundedRect(
          this.cx + index * height + 3.0F * y,
          this.cy + 10.0F * y,
          height - 6.0F * y,
          this.ch - 20.0F * y,
          10.0F,
          mulAlpha(
              index % 3 == 0
                  ? MaterialIsLightService.PRIMARY
                  : (index % 3 == 1
                      ? MaterialIsLightService.TERTIARY
                      : MaterialIsLightService.SECONDARY_CONTAINER),
              this.effectiveOpacity * 0.8F));
    }

    compositorPushPresentationScale.nextLayer();
    float value = this.cx + width;
    float currentValue = this.cy + 28.0F * y;
    float nextValue = this.cw - width * 2.0F;
    float previousValue = this.ch - 56.0F * y;
    compositorPushPresentationScale.roundedRect(
        value,
        currentValue,
        nextValue,
        previousValue,
        16.0F,
        16.0F,
        16.0F,
        16.0F,
        0,
        0.0F,
        14.0F,
        1342177280,
        0.0F,
        0,
        this.effectiveOpacity);
    compositorPushPresentationScale.glassRect(
        value,
        currentValue,
        nextValue,
        previousValue,
        16.0F,
        16.0F,
        16.0F,
        16.0F,
        -1073741824 | MaterialIsLightService.SURFACE & 16777215,
        0,
        26.0F,
        this.effectiveOpacity,
        1.2F,
        0.015F,
        1.05F,
        6.0F,
        0.002F,
        0.6F,
        0.6F,
        MaterialIsLightService.OUTLINE_VARIANT,
        0,
        419430399,
        0.0F);
    compositorPushPresentationScale.textLiteral(
        value + 14.0F * y,
        currentValue + (previousValue - 20.0F * y) / 2.0F,
        "Live effects preview",
        14.0F,
        mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
        MaterialIsLightService.BODY);
  }
}
