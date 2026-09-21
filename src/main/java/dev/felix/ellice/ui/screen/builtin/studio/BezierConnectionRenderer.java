package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.feature.studio.StudioCanvasRenderer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.svg.SvgQueueService;

final class BezierConnectionRenderer {
  static void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      float y,
      float width,
      float height,
      float value,
      int currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      boolean enabled) {
    if (compositorPushPresentationScale.vectorRenderer() != null) {
      int targetValue =
          Math.max(24, Math.min(96, (int) (Math.hypot(height - y, value - width) / 8.0)));
      float[] floats = new float[targetValue * 18];
      float[] currentFloats = new float[(targetValue + 1) * 4];
      float inputValue = Math.max(52.0F * nextValue, Math.abs(height - y) * 0.45F);
      float outputValue = 1.2F * nextValue + 0.5F;

      for (int index = 0; index <= targetValue; index++) {
        float resultValue = (float) index / targetValue;
        float candidateValue = 1.0F - resultValue;
        float[] nextFloats = createFloat(y, width, height, value, inputValue, resultValue);
        float selectedValue =
            3.0F * candidateValue * candidateValue * inputValue
                + 6.0F * candidateValue * resultValue * (height - y - 2.0F * inputValue)
                + 3.0F * resultValue * resultValue * inputValue;
        float defaultValue = 6.0F * candidateValue * resultValue * (value - width);
        float initialValue = Math.max(0.001F, (float) Math.hypot(selectedValue, defaultValue));
        int currentIndex = index * 4;
        currentFloats[currentIndex] = nextFloats[0];
        currentFloats[currentIndex + 1] = nextFloats[1];
        currentFloats[currentIndex + 2] = -defaultValue / initialValue * outputValue;
        currentFloats[currentIndex + 3] = selectedValue / initialValue * outputValue;
      }

      int[] ints = new int[] {0, 0, 1, 1, 0, 1};
      float[] previousFloats = new float[] {1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F};

      for (int nextIndex = 0; nextIndex < targetValue; nextIndex++) {
        for (int previousIndex = 0; previousIndex < 6; previousIndex++) {
          int sourceIndex = (nextIndex + ints[previousIndex]) * 4;
          int targetIndex = nextIndex * 18 + previousIndex * 3;
          floats[targetIndex] =
              currentFloats[sourceIndex]
                  + currentFloats[sourceIndex + 2] * previousFloats[previousIndex];
          floats[targetIndex + 1] =
              currentFloats[sourceIndex + 1]
                  + currentFloats[sourceIndex + 3] * previousFloats[previousIndex];
          floats[targetIndex + 2] = previousFloats[previousIndex];
        }
      }

      compositorPushPresentationScale
          .vectorRenderer()
          .queue(
              new SvgQueueService.VectorCmd(
                  floats,
                  floats.length / 3,
                  0.0F,
                  0.0F,
                  1.0F,
                  1.0F,
                  0.0F,
                  0.0F,
                  0.0F,
                  (currentValue >>> 16 & 0xFF) / 255.0F * previousValue,
                  (currentValue >>> 8 & 0xFF) / 255.0F * previousValue,
                  (currentValue & 0xFF) / 255.0F * previousValue,
                  previousValue,
                  0.0F,
                  0.0F,
                  0.0F,
                  compositorPushPresentationScale.currentClip(),
                  true));
      if (enabled) {
        float[] sourceFloats =
            createFloat(y, width, height, value, inputValue, sourceValue * 0.24F % 1.0F);
        compositorPushPresentationScale.roundedRect(
            sourceFloats[0] - 2.0F * nextValue,
            sourceFloats[1] - 2.0F * nextValue,
            4.0F * nextValue,
            4.0F * nextValue,
            2.0F * nextValue,
            StudioCanvasRenderer.alpha(currentValue, previousValue));
      }
    }
  }

  private static float[] createFloat(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue) {
    float inputValue = 1.0F - targetValue;
    return new float[] {
      inputValue * inputValue * inputValue * value
          + 3.0F * inputValue * inputValue * targetValue * (value + sourceValue)
          + 3.0F * inputValue * targetValue * targetValue * (nextValue - sourceValue)
          + targetValue * targetValue * targetValue * nextValue,
      inputValue * inputValue * inputValue * currentValue
          + 3.0F * inputValue * inputValue * targetValue * currentValue
          + 3.0F * inputValue * targetValue * targetValue * previousValue
          + targetValue * targetValue * targetValue * previousValue
    };
  }
}
