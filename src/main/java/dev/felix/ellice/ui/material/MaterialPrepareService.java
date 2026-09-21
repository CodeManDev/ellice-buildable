package dev.felix.ellice.ui.material;

import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.text.TextMode;
import java.util.ArrayList;
import java.util.List;

public final class MaterialPrepareService extends ScenePctService<MaterialPrepareService> {
  private String text2 = "";
  private String text3 = "";
  private List<String> text4 = List.of();
  private float value;
  private float value2;
  private float value3;
  private boolean enabled;
  private float value4 = -1.0F;
  private long timestamp = -1L;

  public MaterialPrepareService() {
    this.pointerEvents(false);
  }

  public void prepare(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      String text,
      float currentValue) {
    text = PrivacyRevisionService.replace(text);
    if (!text.equals(this.text2)
        || this.value4 != currentValue
        || this.timestamp != compositorPushPresentationScale.fontMetricsRevision()) {
      if (!text.equals(this.text2)) {
        this.text2 = text;
        this.opacity(0.0F);
        this.translateY(5.0F);
        MotionAnimateService.animate(
            this, MotionColorsContainer.Floats.OPACITY, 1.0F, SceneEaseHandler.Tween.ease(0.16F));
        MotionAnimateService.animate(
            this, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0F, MaterialIsLightService.SPATIAL);
      }

      this.value4 = currentValue;
      this.timestamp = compositorPushPresentationScale.fontMetricsRevision();
      float nextValue = Math.max(24.0F, Math.min(286.0F, currentValue - 28.0F));
      String[] strings = text.split("\\n", 2);
      this.text3 =
          createText(
              compositorPushPresentationScale,
              strings[0],
              13.0F,
              "material-roboto-medium",
              nextValue);
      ArrayList arrayList = new ArrayList();
      String currentLength = strings.length > 1 ? strings[1] : "";

      for (String currentText : currentLength.split("\\n")) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String nextText : currentText.split("\\s+")) {
          String previousText = stringBuilder.isEmpty() ? nextText : stringBuilder + " " + nextText;
          if (!stringBuilder.isEmpty()
              && compositorPushPresentationScale.textWidthLiteral(
                      previousText, 11.0F, TextMode.REGULAR, "material-roboto", 0.0F)
                  > nextValue) {
            arrayList.add(stringBuilder.toString());
            stringBuilder.setLength(0);
          }

          if (compositorPushPresentationScale.textWidthLiteral(
                  nextText, 11.0F, TextMode.REGULAR, "material-roboto", 0.0F)
              > nextValue) {
            for (int previousValue : nextText.codePoints().toArray()) {
              String sourceText = new String(Character.toChars(previousValue));
              if (!stringBuilder.isEmpty()
                  && compositorPushPresentationScale.textWidthLiteral(
                          stringBuilder.toString() + sourceText,
                          11.0F,
                          TextMode.REGULAR,
                          "material-roboto",
                          0.0F)
                      > nextValue) {
                arrayList.add(stringBuilder.toString());
                stringBuilder.setLength(0);
              }

              stringBuilder.append(sourceText);
            }
          } else {
            if (!stringBuilder.isEmpty()) {
              stringBuilder.append(' ');
            }

            stringBuilder.append(nextText);
          }
        }

        if (!stringBuilder.isEmpty()) {
          arrayList.add(stringBuilder.toString());
        }
      }

      if (arrayList.size() > 4) {
        arrayList.subList(4, arrayList.size()).clear();
        arrayList.set(
            3,
            createText(
                compositorPushPresentationScale,
                (String) arrayList.get(3) + "…",
                11.0F,
                "material-roboto",
                nextValue));
      }

      this.text4 = List.copyOf(arrayList);
      float sourceValue =
          compositorPushPresentationScale.textWidthLiteral(
              this.text3, 13.0F, TextMode.REGULAR, "material-roboto-medium", 0.0F);

      for (String targetText : this.text4) {
        sourceValue =
            Math.max(
                sourceValue,
                compositorPushPresentationScale.textWidthLiteral(
                    targetText, 11.0F, TextMode.REGULAR, "material-roboto", 0.0F));
      }

      this.value = Math.min(currentValue, Math.max(64.0F, sourceValue + 28.0F));
      this.value2 = this.text4.isEmpty() ? 38.0F : 43 + this.text4.size() * 16;
    }
  }

  public float bubbleWidth() {
    return this.value;
  }

  public float bubbleHeight() {
    return this.value2;
  }

  public void anchor(float value, boolean currentEnabled) {
    this.value3 = value;
    this.enabled = currentEnabled;
  }

  private static String createText(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      String text,
      float value,
      String currentText,
      float currentValue) {
    if (compositorPushPresentationScale.textWidthLiteral(
            text, value, TextMode.REGULAR, currentText, 0.0F)
        <= currentValue) {
      return text;
    }

    String nextText = text;

    while (!nextText.isEmpty()
        && compositorPushPresentationScale.textWidthLiteral(
                nextText + "…", value, TextMode.REGULAR, currentText, 0.0F)
            > currentValue) {
      nextText = nextText.substring(0, nextText.offsetByCodePoints(nextText.length(), -1));
    }

    return nextText + "…";
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.enabled) {
      compositorPushPresentationScale.addTooltipBubble(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          14.0F,
          Math.max(18.0F, Math.min(this.cw - 18.0F, this.value3)),
          12.0F,
          5.0F,
          2.0F,
          MaterialIsLightService.SURFACE_CONTAINER,
          0,
          8.0F,
          1342177280,
          0.65F,
          MaterialIsLightService.OUTLINE_VARIANT,
          0.0F,
          0.0F,
          false,
          this.effectiveOpacity,
          0.65F);
    } else {
      compositorPushPresentationScale.roundedRect(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          14.0F,
          14.0F,
          14.0F,
          14.0F,
          mulAlpha(MaterialIsLightService.SURFACE_CONTAINER, this.effectiveOpacity),
          0.0F,
          8.0F,
          mulAlpha(1342177280, this.effectiveOpacity),
          0.65F,
          mulAlpha(MaterialIsLightService.OUTLINE_VARIANT, this.effectiveOpacity),
          1.0F);
    }

    compositorPushPresentationScale.textLiteral(
        this.cx + 14.0F,
        this.cy + 11.0F,
        this.text3,
        13.0F,
        mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity),
        MaterialIsLightService.LABEL);

    for (int index = 0; index < this.text4.size(); index++) {
      compositorPushPresentationScale.textLiteral(
          this.cx + 14.0F,
          this.cy + 32.0F + index * 16,
          this.text4.get(index),
          11.0F,
          mulAlpha(MaterialIsLightService.ON_SURFACE_VARIANT, this.effectiveOpacity),
          MaterialIsLightService.BODY);
    }
  }
}
