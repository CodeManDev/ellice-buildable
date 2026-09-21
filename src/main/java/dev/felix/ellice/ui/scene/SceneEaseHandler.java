package dev.felix.ellice.ui.scene;

public sealed interface SceneEaseHandler permits SceneEaseHandler.Spring, SceneEaseHandler.Tween {
  enum Easing {
    LINEAR,
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
    EXPRESSIVE_OUT,
    EXPRESSIVE_IN;

    public float apply(float value) {
      float currentValue = Math.max(0.0F, Math.min(1.0F, value));

      return switch (this) {
        case LINEAR -> currentValue;
        case EASE_IN -> currentValue * currentValue * currentValue;
        case EASE_OUT -> {
          float nextValue = 1.0F - currentValue;
          yield 1.0F - nextValue * nextValue * nextValue * nextValue;
        }
        case EASE_IN_OUT ->
            currentValue < 0.5F
                ? 8.0F * currentValue * currentValue * currentValue * currentValue
                : 1.0F - (float) Math.pow(-2.0F * currentValue + 2.0F, 4.0) / 2.0F;
        case EXPRESSIVE_OUT -> calculateValue(currentValue, 0.16F, 1.0F, 0.3F, 1.0F);
        case EXPRESSIVE_IN -> calculateValue(currentValue, 0.7F, 0.0F, 0.84F, 0.0F);
      };
    }

    private static float calculateValue(
        float value, float currentValue, float nextValue, float previousValue, float sourceValue) {
      if (!(value <= 0.0F) && !(value >= 1.0F)) {
        float targetValue = 0.0F;
        float inputValue = 1.0F;
        float outputValue = value;

        for (int index = 0; index < 10; index++) {
          float resultValue = calculateValue2(outputValue, currentValue, previousValue);
          float candidateValue = resultValue - value;
          if (Math.abs(candidateValue) <= 1.0E-5F) {
            break;
          }

          if (candidateValue > 0.0F) {
            inputValue = outputValue;
          } else {
            targetValue = outputValue;
          }

          float selectedValue = calculateValue3(outputValue, currentValue, previousValue);
          float defaultValue =
              selectedValue > 1.0E-5F
                  ? outputValue - candidateValue / selectedValue
                  : (targetValue + inputValue) * 0.5F;
          outputValue =
              defaultValue > targetValue && defaultValue < inputValue
                  ? defaultValue
                  : (targetValue + inputValue) * 0.5F;
        }

        return calculateValue2(outputValue, nextValue, sourceValue);
      } else {
        return value;
      }
    }

    private static float calculateValue2(float value, float currentValue, float nextValue) {
      float previousValue = 1.0F - value;
      return 3.0F * previousValue * previousValue * value * currentValue
          + 3.0F * previousValue * value * value * nextValue
          + value * value * value;
    }

    private static float calculateValue3(float value, float currentValue, float nextValue) {
      float previousValue = 1.0F - value;
      return 3.0F * previousValue * previousValue * currentValue
          + 6.0F * previousValue * value * (nextValue - currentValue)
          + 3.0F * value * value * (1.0F - nextValue);
    }

    private static SceneEaseHandler.Easing[] $values() {
      return new SceneEaseHandler.Easing[] {
        LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, EXPRESSIVE_OUT, EXPRESSIVE_IN
      };
    }
  }

  record Spring(float damping, float stiffness) implements SceneEaseHandler {
    public static final SceneEaseHandler.Spring DEFAULT =
        new SceneEaseHandler.Spring(14.0F, 170.0F);
    public static final SceneEaseHandler.Spring SNAPPY = new SceneEaseHandler.Spring(22.0F, 400.0F);
    public static final SceneEaseHandler.Spring GENTLE = new SceneEaseHandler.Spring(18.0F, 60.0F);
    public static final SceneEaseHandler.Spring BOUNCY = new SceneEaseHandler.Spring(15.0F, 320.0F);

    SceneEaseHandler.Spring.Sample advance(
        float value, float currentValue, float nextValue, float previousValue) {
      double doubleValue = (double) value - nextValue;
      double currentDoubleValue = this.damping * 0.5;
      double nextDoubleValue = currentDoubleValue * currentDoubleValue - this.stiffness;
      double previousDoubleValue;
      double sourceDoubleValue;
      if (Math.abs(nextDoubleValue) < 1.0E-7 * Math.max(1.0F, this.stiffness)) {
        double targetDoubleValue = currentValue + currentDoubleValue * doubleValue;
        double inputDoubleValue = Math.exp(-currentDoubleValue * previousValue);
        previousDoubleValue = (doubleValue + targetDoubleValue * previousValue) * inputDoubleValue;
        sourceDoubleValue =
            (currentValue - currentDoubleValue * targetDoubleValue * previousValue)
                * inputDoubleValue;
      } else if (nextDoubleValue < 0.0) {
        double outputDoubleValue = Math.sqrt(-nextDoubleValue);
        double resultDoubleValue = outputDoubleValue * previousValue;
        double candidateDoubleValue = Math.cos(resultDoubleValue);
        double selectedDoubleValue = Math.sin(resultDoubleValue) / outputDoubleValue;
        double defaultDoubleValue = Math.exp(-currentDoubleValue * previousValue);
        previousDoubleValue =
            defaultDoubleValue
                * (doubleValue * candidateDoubleValue
                    + (currentValue + currentDoubleValue * doubleValue) * selectedDoubleValue);
        sourceDoubleValue =
            defaultDoubleValue
                * (currentValue * candidateDoubleValue
                    - (currentDoubleValue * currentValue + this.stiffness * doubleValue)
                        * selectedDoubleValue);
      } else {
        double initialDoubleValue = Math.sqrt(nextDoubleValue);
        double resolvedDoubleValue = -currentDoubleValue - initialDoubleValue;
        double computedDoubleValue = -this.stiffness / (currentDoubleValue + initialDoubleValue);
        double cachedDoubleValue =
            (currentValue - resolvedDoubleValue * doubleValue)
                / (computedDoubleValue - resolvedDoubleValue);
        double pendingDoubleValue = doubleValue - cachedDoubleValue;
        double activeDoubleValue =
            cachedDoubleValue * Math.exp(computedDoubleValue * previousValue);
        double fallbackDoubleValue =
            pendingDoubleValue * Math.exp(resolvedDoubleValue * previousValue);
        previousDoubleValue = activeDoubleValue + fallbackDoubleValue;
        sourceDoubleValue =
            computedDoubleValue * activeDoubleValue + resolvedDoubleValue * fallbackDoubleValue;
      }

      return new SceneEaseHandler.Spring.Sample(
          (float) (nextValue + previousDoubleValue), (float) sourceDoubleValue);
    }

    record Sample(float position, float velocity) {}
  }

  record Tween(float duration, SceneEaseHandler.Easing easing) implements SceneEaseHandler {
    public static SceneEaseHandler.Tween ease(float value) {
      return new SceneEaseHandler.Tween(value, SceneEaseHandler.Easing.EASE_OUT);
    }

    public static SceneEaseHandler.Tween linear(float value) {
      return new SceneEaseHandler.Tween(value, SceneEaseHandler.Easing.LINEAR);
    }

    public static SceneEaseHandler.Tween enter(float value) {
      return new SceneEaseHandler.Tween(value, SceneEaseHandler.Easing.EXPRESSIVE_OUT);
    }

    public static SceneEaseHandler.Tween exit(float value) {
      return new SceneEaseHandler.Tween(value, SceneEaseHandler.Easing.EXPRESSIVE_IN);
    }
  }
}
