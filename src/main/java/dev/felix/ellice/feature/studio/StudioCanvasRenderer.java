package dev.felix.ellice.feature.studio;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;
import java.util.ArrayList;
import java.util.List;

public final class StudioCanvasRenderer {
  public List<StudioCanvasRenderer.Placed> layout(
      StudioShapeService studioShape,
      StudioValidateValidator.Frame frame,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    ArrayList arrayList = new ArrayList();
    this.updateState(
        studioShape, frame, "", value, currentValue, nextValue, previousValue, arrayList, 0);
    return arrayList;
  }

  private void updateState(
      StudioShapeService studioShape,
      StudioValidateValidator.Frame frame,
      String text,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      List<StudioCanvasRenderer.Placed> items,
      int sourceValue) {
    if (sourceValue <= 64) {
      for (StudioShapeService.Shape shape : studioShape.shapes) {
        if (shape.parent.equals(text)) {
          StudioValidateValidator.Appearance appearance = frame.shapes().get(shape.id);
          if (appearance != null && appearance.visible) {
            float targetValue = nextValue * appearance.scale;
            float inputValue =
                value
                    + appearance.x * nextValue
                    + shape.width * nextValue * (1.0F - appearance.scale) / 2.0F;
            float outputValue =
                currentValue
                    + appearance.y * nextValue
                    + shape.height * nextValue * (1.0F - appearance.scale) / 2.0F;
            StudioCanvasRenderer.Placed placed =
                new StudioCanvasRenderer.Placed(
                    shape,
                    appearance,
                    inputValue,
                    outputValue,
                    shape.width * targetValue,
                    shape.height * targetValue,
                    targetValue,
                    previousValue * appearance.opacity);
            items.add(placed);
            if (shape.kind == StudioShapeService.ShapeKind.GROUP) {
              this.updateState(
                  studioShape,
                  frame,
                  shape.id,
                  inputValue,
                  outputValue,
                  targetValue,
                  previousValue * appearance.opacity,
                  items,
                  sourceValue + 1);
            }
          }
        }
      }
    }
  }

  public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      StudioShapeService studioShape,
      StudioValidateValidator.Frame frame,
      float height,
      float value,
      float currentValue,
      float nextValue,
      float previousValue) {
    fonts(compositorPushPresentationScale);

    for (StudioCanvasRenderer.Placed placed :
        this.layout(studioShape, frame, height, value, currentValue, nextValue)) {
      this.draw(compositorPushPresentationScale, placed, previousValue);
    }
  }

  public void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      StudioCanvasRenderer.Placed placed,
      float currentWidth) {
    StudioShapeService.Shape currentShape = placed.shape;
    StudioValidateValidator.Appearance currentAppearance = placed.appearance;
    if (currentShape.kind != StudioShapeService.ShapeKind.GROUP && !(placed.opacity <= 0.001F)) {
      if (currentShape.kind == StudioShapeService.ShapeKind.TEXT) {
        float value = currentShape.fontSize * placed.scale;
        String currentText =
            fit(compositorPushPresentationScale, currentAppearance.text, value, placed.width);
        float currentValue =
            compositorPushPresentationScale.textLineHeight(
                value, TextMode.REGULAR, "material-roboto-medium");
        float nextValue =
            Math.max(
                0.0F,
                placed.width
                    - compositorPushPresentationScale.textWidth(
                        currentText, value, TextMode.REGULAR, "material-roboto-medium"));

        float previousValue =
            switch (currentShape.alignment) {
              case LEFT -> 0.0F;
              case CENTER -> nextValue / 2.0F;
              case RIGHT -> nextValue;
            };
        compositorPushPresentationScale.pushClip(placed.x, placed.y, placed.width, placed.height);
        compositorPushPresentationScale.text(
            placed.x + previousValue,
            placed.y + (placed.height - currentValue) / 2.0F,
            currentText,
            value,
            alpha(currentAppearance.color, placed.opacity),
            MaterialIsLightService.LABEL);
        compositorPushPresentationScale.popClip();
      } else {
        byte byteValue =
            switch (currentShape.kind) {
              case PANEL -> 1;
              case ELLIPSE -> 2;
              case RING -> 3;
              case DIAMOND -> 4;
              case BAR -> 6;
              default -> 1;
            };
        compositorPushPresentationScale.studioPrimitive(
            placed.x,
            placed.y,
            placed.width,
            placed.height,
            currentShape.radius * placed.scale,
            currentAppearance.color,
            currentShape.color2,
            placed.opacity,
            new CompositorPushPresentationScaleService.StudioPaint(
                byteValue,
                currentWidth,
                currentAppearance.progress,
                currentShape.stroke * placed.scale,
                currentShape.finish.ordinal(),
                0.0F));
      }

      compositorPushPresentationScale.nextLayer();
    }
  }

  public static void fonts(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    compositorPushPresentationScale.requestFontFamily(
        "material-roboto",
        TextTextureService.FontSource.CLASSPATH,
        "/assets/ellice/fonts/material/Roboto-Regular.ttf");
    compositorPushPresentationScale.requestFontFamily(
        "material-roboto-medium",
        TextTextureService.FontSource.CLASSPATH,
        "/assets/ellice/fonts/material/Roboto-Medium.ttf");
  }

  public static String fit(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      String text,
      float value,
      float currentValue) {
    if (compositorPushPresentationScale.textWidth(
            text, value, TextMode.REGULAR, "material-roboto-medium")
        <= currentValue) {
      return text;
    }

    String currentText = text;

    while (!currentText.isEmpty()
        && compositorPushPresentationScale.textWidth(
                currentText + "…", value, TextMode.REGULAR, "material-roboto-medium")
            > currentValue) {
      currentText =
          currentText.substring(0, currentText.offsetByCodePoints(currentText.length(), -1));
    }

    return currentText.isEmpty() ? "" : currentText + "…";
  }

  public static int alpha(int value, float opacity) {
    return value & 16777215
        | Math.round((value >>> 24 & 0xFF) * Math.max(0.0F, Math.min(1.0F, opacity))) << 24;
  }

  public record Placed(
      StudioShapeService.Shape shape,
      StudioValidateValidator.Appearance appearance,
      float x,
      float y,
      float width,
      float height,
      float scale,
      float opacity) {
    public boolean contains(float value, float currentValue) {
      return value >= this.x
          && currentValue >= this.y
          && value <= this.x + this.width
          && currentValue <= this.y + this.height;
    }
  }
}
