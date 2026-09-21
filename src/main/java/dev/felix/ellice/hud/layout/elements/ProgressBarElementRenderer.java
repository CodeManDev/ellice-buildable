package dev.felix.ellice.hud.layout.elements;

import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public final class ProgressBarElementRenderer implements HudElementRenderer {
   @Override
   public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale, LayoutIsContainerService layoutIsContainer, float width, float height, float value, float currentValue, LayoutOperationHandler layoutOperation, float nextValue, float previousValue
   ) {
      String text = LayoutStringService.string(layoutIsContainer.props, "fillFrom", "player.health");
      float sourceValue = LayoutStringService.number(layoutIsContainer.props, "min", 0.0F);
      float targetValue = LayoutStringService.number(layoutIsContainer.props, "max", Float.NaN);
      float inputValue = layoutOperation.number(text);
      float outputValue = Float.isNaN(targetValue) ? layoutOperation.max(text) : targetValue;
      if (outputValue <= sourceValue) {
         outputValue = sourceValue + 1.0F;
      }

      float resultValue = (inputValue - sourceValue) / (outputValue - sourceValue);
      if (resultValue < 0.0F) {
         resultValue = 0.0F;
      }

      if (resultValue > 1.0F) {
         resultValue = 1.0F;
      }

      boolean enabled = "vertical".equalsIgnoreCase(LayoutStringService.string(layoutIsContainer.props, "orientation", "horizontal"));
      float candidateValue = layoutIsContainer.props.has("cornerRadius")
         ? LayoutStringService.number(layoutIsContainer.props, "cornerRadius", 0.0F) * previousValue
         : Math.min(value, currentValue) * 0.5F;
      int selectedValue = LayoutStringService.withOpacity(LayoutStringService.color(layoutIsContainer.props, "trackColor", 1090519039), nextValue);
      int defaultValue = LayoutStringService.withOpacity(LayoutStringService.color(layoutIsContainer.props, "fillColor", -14494823), nextValue);
      float initialValue = LayoutStringService.number(layoutIsContainer.props, "borderWidth", 0.0F) * previousValue;
      int resolvedValue = LayoutStringService.withOpacity(LayoutStringService.color(layoutIsContainer.props, "borderColor", 0), nextValue);
      compositorPushPresentationScale.roundedRect(width, height, value, currentValue, candidateValue, candidateValue, candidateValue, candidateValue, selectedValue, 0.0F, 0.0F, 0, initialValue, resolvedValue, nextValue, 0.0F);
      float computedValue = enabled ? value : Math.max(0.0F, value * resultValue);
      float cachedValue = enabled ? Math.max(0.0F, currentValue * resultValue) : currentValue;
      float pendingValue = width;
      float activeValue = enabled ? height + currentValue - cachedValue : height;
      if (computedValue > 0.5F && cachedValue > 0.5F) {
         compositorPushPresentationScale.roundedRect(pendingValue, activeValue, computedValue, cachedValue, candidateValue, candidateValue, candidateValue, candidateValue, defaultValue, 0.0F, 0.0F, 0, 0.0F, 0, nextValue, 0.0F);
      }
   }
}
