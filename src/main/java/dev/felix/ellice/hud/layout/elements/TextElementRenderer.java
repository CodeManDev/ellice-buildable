package dev.felix.ellice.hud.layout.elements;

import dev.felix.ellice.hud.layout.HudElementRenderer;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextMode;
import java.util.Locale;

public class TextElementRenderer implements HudElementRenderer {
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
    String currentText = LayoutStringService.string(layoutIsContainer.props, "text", "");
    if (!currentText.isEmpty()) {
      String nextText = layoutOperation.interpolate(currentText);
      if (!nextText.isEmpty()) {
        float sourceValue =
            LayoutStringService.number(layoutIsContainer.props, "fontSize", 9.0F) * previousValue;
        int targetValue =
            LayoutStringService.withOpacity(
                LayoutStringService.color(layoutIsContainer.props, "color", -1), nextValue);
        TextMode textMode =
            this.createTextMode(
                LayoutStringService.string(layoutIsContainer.props, "variant", "regular"));
        TextData textData =
            TextData.NONE.withVariant(textMode).withFontFamily(createText(layoutIsContainer));
        float inputValue =
            LayoutStringService.number(layoutIsContainer.props, "shadowX", 0.0F) * previousValue;
        float outputValue =
            LayoutStringService.number(layoutIsContainer.props, "shadowY", 0.0F) * previousValue;
        int resultValue =
            LayoutStringService.color(layoutIsContainer.props, "shadowColor", Integer.MIN_VALUE);
        if (inputValue != 0.0F || outputValue != 0.0F) {
          textData =
              textData.withShadow(
                  inputValue, outputValue, LayoutStringService.withOpacity(resultValue, nextValue));
        }

        float candidateValue =
            LayoutStringService.number(layoutIsContainer.props, "outlineWidth", 0.0F)
                * previousValue;
        if (candidateValue > 0.0F) {
          int selectedValue =
              LayoutStringService.color(layoutIsContainer.props, "outlineColor", -16777216);
          textData =
              textData.withOutline(
                  candidateValue, LayoutStringService.withOpacity(selectedValue, nextValue));
        }

        compositorPushPresentationScale.text(
            width, height, nextText, sourceValue, targetValue, textData);
        if (LayoutStringService.bool(layoutIsContainer.props, "underline", false)) {
          float defaultValue =
              compositorPushPresentationScale.textWidth(
                  nextText, sourceValue, textMode, createText(layoutIsContainer));
          float initialValue = Math.max(1.0F, sourceValue * 0.07F);
          compositorPushPresentationScale.roundedRect(
              width, height + sourceValue * 0.92F, defaultValue, initialValue, 0.0F, targetValue);
        }
      }
    }
  }

  @Override
  public HudElementRenderer.Size measure(
      LayoutIsContainerService layoutIsContainer,
      LayoutOperationHandler layoutOperation,
      float value,
      float currentValue,
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    float nextValue = layoutIsContainer.width;
    float previousValue = layoutIsContainer.height;
    if (nextValue < 0.0F || previousValue < 0.0F) {
      float sourceValue = LayoutStringService.number(layoutIsContainer.props, "fontSize", 9.0F);
      if (nextValue < 0.0F) {
        String currentText =
            layoutOperation.interpolate(
                LayoutStringService.string(layoutIsContainer.props, "text", ""));
        TextMode textMode =
            this.createTextMode(
                LayoutStringService.string(layoutIsContainer.props, "variant", "regular"));
        nextValue =
            compositorPushPresentationScale != null
                ? Math.max(
                    1.0F,
                    compositorPushPresentationScale.textWidth(
                        currentText, sourceValue, textMode, createText(layoutIsContainer)))
                : Math.max(1.0F, currentText.length() * sourceValue * 0.55F);
      }

      if (previousValue < 0.0F) {
        previousValue = sourceValue * 1.2F;
      }
    }

    return new HudElementRenderer.Size(nextValue, previousValue);
  }

  @Override
  public HudElementRenderer.Size measure(
      LayoutIsContainerService layoutIsContainer,
      LayoutOperationHandler layoutOperation,
      float value,
      float currentValue) {
    float nextValue = LayoutStringService.number(layoutIsContainer.props, "fontSize", 9.0F);
    TextMode textMode =
        this.createTextMode(
            LayoutStringService.string(layoutIsContainer.props, "variant", "regular"));
    float previousValue = layoutIsContainer.width;
    float sourceValue = layoutIsContainer.height;
    if (previousValue < 0.0F) {
      String currentText = LayoutStringService.string(layoutIsContainer.props, "text", "");
      String nextText = layoutOperation.interpolate(currentText);
      previousValue = Math.max(1.0F, nextText.length() * nextValue * 0.55F);
    }

    if (sourceValue < 0.0F) {
      sourceValue = nextValue * 1.2F;
    }

    return new HudElementRenderer.Size(previousValue, sourceValue);
  }

  private static String createText(LayoutIsContainerService layoutIsContainer) {
    String text = LayoutStringService.string(layoutIsContainer.props, "font", "");
    return text != null && !text.isEmpty() && !"client".equalsIgnoreCase(text)
        ? text.toLowerCase(Locale.ROOT)
        : null;
  }

  private TextMode createTextMode(String text) {
    if (text == null) {
      return TextMode.REGULAR;
    }

    return switch (text.toLowerCase()) {
      case "bold" -> TextMode.BOLD;
      case "italic" -> TextMode.ITALIC;
      case "bolditalic", "bold-italic" -> TextMode.BOLD_ITALIC;
      default -> TextMode.REGULAR;
    };
  }
}
