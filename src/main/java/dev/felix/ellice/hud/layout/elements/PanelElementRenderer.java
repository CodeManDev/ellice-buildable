package dev.felix.ellice.hud.layout.elements;

import com.google.gson.JsonObject;
import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public final class PanelElementRenderer implements HudElementRenderer {
  @Override
  public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      LayoutIsContainerService layoutIsContainer,
      float width,
      float height,
      float value,
      float currentValue,
      LayoutOperationHandler layoutOperation,
      float nextValue,
      float previousValue) {
    JsonObject jsonObject = layoutIsContainer.props;
    int sourceValue = LayoutStringService.color(jsonObject, "color", -14671832);
    boolean enabled = LayoutStringService.bool(jsonObject, "gradient", false);
    int targetValue =
        enabled ? LayoutStringService.color(jsonObject, "gradientEnd", sourceValue) : sourceValue;
    float inputValue = LayoutStringService.number(jsonObject, "cornerRadius", 4.0F);
    boolean currentEnabled = LayoutStringService.bool(jsonObject, "perCorner", false);
    float outputValue =
        (currentEnabled
                ? LayoutStringService.number(jsonObject, "cornerTL", inputValue)
                : inputValue)
            * previousValue;
    float resultValue =
        (currentEnabled
                ? LayoutStringService.number(jsonObject, "cornerTR", inputValue)
                : inputValue)
            * previousValue;
    float candidateValue =
        (currentEnabled
                ? LayoutStringService.number(jsonObject, "cornerBR", inputValue)
                : inputValue)
            * previousValue;
    float selectedValue =
        (currentEnabled
                ? LayoutStringService.number(jsonObject, "cornerBL", inputValue)
                : inputValue)
            * previousValue;
    float defaultValue = LayoutStringService.number(jsonObject, "blur", 0.0F) * previousValue;
    float initialValue =
        LayoutStringService.number(jsonObject, "edgeSoftness", 0.0F) * previousValue;
    float resolvedValue =
        LayoutStringService.number(jsonObject, "borderWidth", 0.0F) * previousValue;
    int computedValue =
        LayoutStringService.withOpacity(
            LayoutStringService.color(jsonObject, "borderColor", 0), nextValue);
    boolean nextEnabled = LayoutStringService.bool(jsonObject, "border2", false);
    int cachedValue =
        nextEnabled
            ? LayoutStringService.withOpacity(
                LayoutStringService.color(
                    jsonObject,
                    "borderColor2",
                    LayoutStringService.color(jsonObject, "borderColor", 0)),
                nextValue)
            : 0;
    if (LayoutStringService.bool(jsonObject, "glass", false)) {
      float pendingValue = LayoutStringService.number(jsonObject, "glassSaturation", 1.08F);
      float activeValue = LayoutStringService.number(jsonObject, "glassBrightness", -0.02F);
      float fallbackValue = LayoutStringService.number(jsonObject, "glassContrast", 1.06F);
      float primaryValue =
          LayoutStringService.number(jsonObject, "glassRefraction", 8.0F) * previousValue;
      float secondaryValue = LayoutStringService.number(jsonObject, "glassNoise", 0.0028F);
      float tertiaryValue = LayoutStringService.number(jsonObject, "glassChromatic", 0.5F);
      int temporaryValue = LayoutStringService.color(jsonObject, "glassHighlightColor", 486539263);
      compositorPushPresentationScale.glassRect(
          width,
          height,
          value,
          currentValue,
          outputValue,
          resultValue,
          candidateValue,
          selectedValue,
          LayoutStringService.withOpacity(sourceValue, nextValue),
          LayoutStringService.withOpacity(targetValue, nextValue),
          defaultValue,
          nextValue,
          pendingValue,
          activeValue,
          fallbackValue,
          primaryValue,
          secondaryValue,
          tertiaryValue,
          resolvedValue,
          computedValue,
          cachedValue,
          temporaryValue,
          initialValue);
    } else {
      float requestedValue = LayoutStringService.number(jsonObject, "shadow", 0.0F) * previousValue;
      int actualValue =
          LayoutStringService.withOpacity(
              LayoutStringService.color(jsonObject, "shadowColor", 1610612736), nextValue);
      boolean previousEnabled = LayoutStringService.bool(jsonObject, "insetShadow", false);
      float expectedValue = LayoutStringService.number(jsonObject, "innerHighlight", 0.0F);
      float minimumValue =
          LayoutStringService.number(jsonObject, "innerHighlightSize", 0.0F) * previousValue;
      compositorPushPresentationScale.addRect(
          width,
          height,
          value,
          currentValue,
          outputValue,
          resultValue,
          candidateValue,
          selectedValue,
          LayoutStringService.withOpacity(sourceValue, nextValue),
          defaultValue,
          requestedValue,
          actualValue,
          resolvedValue,
          computedValue,
          nextValue,
          initialValue,
          previousEnabled,
          LayoutStringService.withOpacity(targetValue, nextValue),
          cachedValue,
          expectedValue,
          minimumValue);
    }
  }
}
