package dev.felix.ellice.render.render3d.geometry;

public final class GeometryCubeService {
  public static final float DEFAULT_UNIT_SIZE = 1.0F;
  public static final int DEFAULT_SPHERE_SEGMENTS = 32;
  public static final int DEFAULT_SPHERE_RINGS = 16;
  public static final float DEFAULT_HEART_DEPTH = 0.45F;
  public static final int DEFAULT_HEART_SEGMENTS = 48;
  public static final int DEFAULT_HEART_RINGS = 16;
  private static final double value = 0.8;

  private GeometryCubeService() {}

  public static GeometryVertexCountService cube() {
    return cube(1.0F);
  }

  public static GeometryVertexCountService cube(float value) {
    updateState(value, "size");
    float currentValue = value * 0.5F;
    if (Float.isFinite(currentValue) && currentValue != 0.0F) {
      float[] floats = new float[288];
      int nextValue = 0;
      nextValue =
          calculateValue2(
              floats,
              nextValue,
              -currentValue,
              -currentValue,
              currentValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              currentValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              0.0F,
              0.0F,
              1.0F,
              1.0F,
              0.0F,
              0.0F,
              1.0F);
      nextValue =
          calculateValue2(
              floats,
              nextValue,
              currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              -currentValue,
              0.0F,
              0.0F,
              -1.0F,
              -1.0F,
              0.0F,
              0.0F,
              1.0F);
      nextValue =
          calculateValue2(
              floats,
              nextValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              -currentValue,
              -currentValue,
              currentValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              currentValue,
              1.0F,
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              -1.0F,
              1.0F);
      nextValue =
          calculateValue2(
              floats,
              nextValue,
              -currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              -currentValue,
              currentValue,
              -currentValue,
              currentValue,
              currentValue,
              -currentValue,
              currentValue,
              -currentValue,
              -1.0F,
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1.0F,
              1.0F);
      nextValue =
          calculateValue2(
              floats,
              nextValue,
              -currentValue,
              currentValue,
              currentValue,
              currentValue,
              currentValue,
              currentValue,
              currentValue,
              currentValue,
              -currentValue,
              -currentValue,
              currentValue,
              -currentValue,
              0.0F,
              1.0F,
              0.0F,
              1.0F,
              0.0F,
              0.0F,
              1.0F);
      calculateValue2(
          floats,
          nextValue,
          -currentValue,
          -currentValue,
          -currentValue,
          currentValue,
          -currentValue,
          -currentValue,
          currentValue,
          -currentValue,
          currentValue,
          -currentValue,
          -currentValue,
          currentValue,
          0.0F,
          -1.0F,
          0.0F,
          1.0F,
          0.0F,
          0.0F,
          1.0F);
      int[] ints = new int[36];
      int index = 0;

      for (int currentIndex = 0; currentIndex < 6; currentIndex++) {
        int previousValue = currentIndex * 4;
        ints[index++] = previousValue;
        ints[index++] = previousValue + 1;
        ints[index++] = previousValue + 2;
        ints[index++] = previousValue;
        ints[index++] = previousValue + 2;
        ints[index++] = previousValue + 3;
      }

      return new GeometryVertexCountService(floats, ints);
    } else {
      throw new IllegalArgumentException("size is outside the usable float range");
    }
  }

  public static GeometryVertexCountService plane() {
    return plane(1.0F, 1.0F);
  }

  public static GeometryVertexCountService plane(float value) {
    return plane(value, value);
  }

  public static GeometryVertexCountService plane(float value, float currentValue) {
    updateState(value, "width");
    updateState(currentValue, "depth");
    float nextValue = value * 0.5F;
    float previousValue = currentValue * 0.5F;
    if (Float.isFinite(nextValue)
        && nextValue != 0.0F
        && Float.isFinite(previousValue)
        && previousValue != 0.0F) {
      float[] floats = new float[48];
      int sourceValue = 0;
      sourceValue =
          calculateValue3(
              floats,
              sourceValue,
              -nextValue,
              0.0F,
              -previousValue,
              0.0F,
              1.0F,
              0.0F,
              0.0F,
              0.0F,
              1.0F,
              0.0F,
              0.0F,
              -1.0F);
      sourceValue =
          calculateValue3(
              floats,
              sourceValue,
              -nextValue,
              0.0F,
              previousValue,
              0.0F,
              1.0F,
              0.0F,
              0.0F,
              1.0F,
              1.0F,
              0.0F,
              0.0F,
              -1.0F);
      sourceValue =
          calculateValue3(
              floats,
              sourceValue,
              nextValue,
              0.0F,
              previousValue,
              0.0F,
              1.0F,
              0.0F,
              1.0F,
              1.0F,
              1.0F,
              0.0F,
              0.0F,
              -1.0F);
      calculateValue3(
          floats,
          sourceValue,
          nextValue,
          0.0F,
          -previousValue,
          0.0F,
          1.0F,
          0.0F,
          1.0F,
          0.0F,
          1.0F,
          0.0F,
          0.0F,
          -1.0F);
      return new GeometryVertexCountService(floats, new int[] {0, 1, 2, 0, 2, 3});
    } else {
      throw new IllegalArgumentException("plane dimensions are outside the usable float range");
    }
  }

  public static GeometryVertexCountService uvSphere() {
    return uvSphere(0.5F, 32, 16);
  }

  public static GeometryVertexCountService uvSphere(int value, int currentValue) {
    return uvSphere(0.5F, value, currentValue);
  }

  public static GeometryVertexCountService uvSphere(float value, int currentValue, int nextValue) {
    updateState(value, "radius");
    if (currentValue < 3) {
      throw new IllegalArgumentException("segments must be at least 3");
    }

    if (nextValue < 2) {
      throw new IllegalArgumentException("rings must be at least 2");
    }

    long longValue = (nextValue - 1) * (currentValue + 1L);
    long currentLongValue = 2L * currentValue + longValue;
    long nextLongValue = 6L * currentValue * (nextValue - 1L);
    if (currentLongValue <= 178956970L && nextLongValue <= 2147483647L) {
      int previousValue = (int) currentLongValue;
      float[] floats = new float[previousValue * 12];
      int[] ints = new int[(int) nextLongValue];
      int sourceValue = 0;

      for (int index = 0; index < currentValue; index++) {
        double doubleValue = (index + 0.5) / currentValue;
        double currentDoubleValue = doubleValue * 3.141592653589793 * 2.0;
        sourceValue =
            calculateValue3(
                floats,
                sourceValue,
                0.0F,
                value,
                0.0F,
                0.0F,
                1.0F,
                0.0F,
                (float) doubleValue,
                0.0F,
                (float) (-Math.sin(currentDoubleValue)),
                0.0F,
                (float) Math.cos(currentDoubleValue),
                1.0F);
      }

      int targetValue = currentValue;

      for (int currentIndex = 1; currentIndex < nextValue; currentIndex++) {
        double nextDoubleValue = (double) currentIndex / nextValue;
        double previousDoubleValue = nextDoubleValue * 3.141592653589793;
        double sourceDoubleValue = Math.sin(previousDoubleValue);
        double targetDoubleValue = Math.cos(previousDoubleValue);

        for (int nextIndex = 0; nextIndex <= currentValue; nextIndex++) {
          double inputDoubleValue = (double) nextIndex / currentValue;
          double outputDoubleValue = inputDoubleValue * 3.141592653589793 * 2.0;
          float inputValue = (float) (sourceDoubleValue * Math.cos(outputDoubleValue));
          float outputValue = (float) targetDoubleValue;
          float resultValue = (float) (sourceDoubleValue * Math.sin(outputDoubleValue));
          sourceValue =
              calculateValue3(
                  floats,
                  sourceValue,
                  inputValue * value,
                  outputValue * value,
                  resultValue * value,
                  inputValue,
                  outputValue,
                  resultValue,
                  (float) inputDoubleValue,
                  (float) nextDoubleValue,
                  (float) (-Math.sin(outputDoubleValue)),
                  0.0F,
                  (float) Math.cos(outputDoubleValue),
                  1.0F);
        }
      }

      int candidateValue = targetValue + (nextValue - 1) * (currentValue + 1);

      for (int previousIndex = 0; previousIndex < currentValue; previousIndex++) {
        double resultDoubleValue = (previousIndex + 0.5) / currentValue;
        double candidateDoubleValue = resultDoubleValue * 3.141592653589793 * 2.0;
        sourceValue =
            calculateValue3(
                floats,
                sourceValue,
                0.0F,
                -value,
                0.0F,
                0.0F,
                -1.0F,
                0.0F,
                (float) resultDoubleValue,
                1.0F,
                (float) (-Math.sin(candidateDoubleValue)),
                0.0F,
                (float) Math.cos(candidateDoubleValue),
                1.0F);
      }

      int sourceIndex = 0;

      for (int targetIndex = 0; targetIndex < currentValue; targetIndex++) {
        ints[sourceIndex++] = targetIndex;
        ints[sourceIndex++] = targetValue + targetIndex + 1;
        ints[sourceIndex++] = targetValue + targetIndex;
      }

      for (int inputIndex = 0; inputIndex < nextValue - 2; inputIndex++) {
        int selectedValue = targetValue + inputIndex * (currentValue + 1);
        int defaultValue = selectedValue + currentValue + 1;

        for (int outputIndex = 0; outputIndex < currentValue; outputIndex++) {
          int initialValue = selectedValue + outputIndex;
          int resolvedValue = initialValue + 1;
          int computedValue = defaultValue + outputIndex;
          int cachedValue = computedValue + 1;
          ints[sourceIndex++] = initialValue;
          ints[sourceIndex++] = resolvedValue;
          ints[sourceIndex++] = computedValue;
          ints[sourceIndex++] = resolvedValue;
          ints[sourceIndex++] = cachedValue;
          ints[sourceIndex++] = computedValue;
        }
      }

      int pendingValue = targetValue + (nextValue - 2) * (currentValue + 1);

      for (int resultIndex = 0; resultIndex < currentValue; resultIndex++) {
        ints[sourceIndex++] = candidateValue + resultIndex;
        ints[sourceIndex++] = pendingValue + resultIndex;
        ints[sourceIndex++] = pendingValue + resultIndex + 1;
      }

      if (sourceValue == floats.length && sourceIndex == ints.length) {
        return new GeometryVertexCountService(floats, ints);
      } else {
        throw new IllegalStateException("internal sphere tessellation count mismatch");
      }
    } else {
      throw new IllegalArgumentException("sphere tessellation is too large");
    }
  }

  public static GeometryVertexCountService heart() {
    return heart(1.0F, 1.0F, 0.45F, 48, 16);
  }

  public static GeometryVertexCountService heart(float value, float currentValue, float nextValue) {
    return heart(value, currentValue, nextValue, 48, 16);
  }

  public static GeometryVertexCountService heart(int value, int currentValue) {
    return heart(1.0F, 1.0F, 0.45F, value, currentValue);
  }

  public static GeometryVertexCountService heart(
      float value, float currentValue, float nextValue, int previousValue, int sourceValue) {
    updateState(value, "width");
    updateState(currentValue, "height");
    updateState(nextValue, "depth");
    if (previousValue < 8) {
      throw new IllegalArgumentException("segments must be at least 8");
    }

    if (sourceValue < 2) {
      throw new IllegalArgumentException("rings must be at least 2");
    }

    float targetValue = value * 0.5F;
    float inputValue = currentValue * 0.5F;
    float outputValue = nextValue * 0.5F;
    if (Float.isFinite(targetValue)
        && targetValue != 0.0F
        && Float.isFinite(inputValue)
        && inputValue != 0.0F
        && Float.isFinite(outputValue)
        && outputValue != 0.0F) {
      long longValue = (sourceValue - 1) * (previousValue + 1L);
      long currentLongValue = 2L * previousValue + longValue;
      long nextLongValue = 6L * previousValue * (sourceValue - 1L);
      if (currentLongValue <= 178956970L && nextLongValue <= 2147483647L) {
        double[] doubles = new double[previousValue + 1];
        double[] currentDoubles = new double[previousValue + 1];
        double[] nextDoubles = new double[previousValue + 1];
        double[] previousDoubles = new double[previousValue + 1];
        double doubleValue = Double.POSITIVE_INFINITY;
        double currentDoubleValue = Double.POSITIVE_INFINITY;
        double nextDoubleValue = Double.NEGATIVE_INFINITY;
        double previousDoubleValue = Double.NEGATIVE_INFINITY;

        for (int index = 0; index < previousValue; index++) {
          double sourceDoubleValue = -6.283185307179586 * index / previousValue;
          double targetDoubleValue = Math.sin(sourceDoubleValue);
          double inputDoubleValue = Math.cos(sourceDoubleValue);
          double outputDoubleValue =
              16.0 * targetDoubleValue * targetDoubleValue * targetDoubleValue
                  + 0.8 * targetDoubleValue;
          double resultDoubleValue =
              13.0 * inputDoubleValue
                  - 5.0 * Math.cos(2.0 * sourceDoubleValue)
                  - 2.0 * Math.cos(3.0 * sourceDoubleValue)
                  - Math.cos(4.0 * sourceDoubleValue);
          doubles[index] = outputDoubleValue;
          currentDoubles[index] = resultDoubleValue;
          doubleValue = Math.min(doubleValue, outputDoubleValue);
          currentDoubleValue = Math.min(currentDoubleValue, resultDoubleValue);
          nextDoubleValue = Math.max(nextDoubleValue, outputDoubleValue);
          previousDoubleValue = Math.max(previousDoubleValue, resultDoubleValue);
        }

        double candidateDoubleValue = value / (nextDoubleValue - doubleValue);
        double selectedDoubleValue = currentValue / (previousDoubleValue - currentDoubleValue);
        double defaultDoubleValue = (doubleValue + nextDoubleValue) * 0.5;
        double initialDoubleValue = (currentDoubleValue + previousDoubleValue) * 0.5;

        for (int currentIndex = 0; currentIndex <= previousValue; currentIndex++) {
          byte byteValue = (byte) (currentIndex == previousValue ? 0 : currentIndex);
          double resolvedDoubleValue = -6.283185307179586 * byteValue / previousValue;
          double computedDoubleValue = Math.sin(resolvedDoubleValue);
          double cachedDoubleValue = Math.cos(resolvedDoubleValue);
          double pendingDoubleValue =
              16.0 * computedDoubleValue * computedDoubleValue * computedDoubleValue
                  + 0.8 * computedDoubleValue;
          double activeDoubleValue =
              13.0 * cachedDoubleValue
                  - 5.0 * Math.cos(2.0 * resolvedDoubleValue)
                  - 2.0 * Math.cos(3.0 * resolvedDoubleValue)
                  - Math.cos(4.0 * resolvedDoubleValue);
          doubles[currentIndex] = (pendingDoubleValue - defaultDoubleValue) * candidateDoubleValue;
          currentDoubles[currentIndex] =
              (activeDoubleValue - initialDoubleValue) * selectedDoubleValue;
          nextDoubles[currentIndex] =
              -(48.0 * computedDoubleValue * computedDoubleValue + 0.8)
                  * cachedDoubleValue
                  * candidateDoubleValue;
          previousDoubles[currentIndex] =
              -(-13.0 * computedDoubleValue
                      + 10.0 * Math.sin(2.0 * resolvedDoubleValue)
                      + 6.0 * Math.sin(3.0 * resolvedDoubleValue)
                      + 4.0 * Math.sin(4.0 * resolvedDoubleValue))
                  * selectedDoubleValue;
        }

        int resultValue = (int) currentLongValue;
        float[] floats = new float[resultValue * 12];
        int[] ints = new int[(int) nextLongValue];
        int candidateValue = 0;

        for (int nextIndex = 0; nextIndex < previousValue; nextIndex++) {
          double fallbackDoubleValue = (nextIndex + 0.5) / previousValue;
          double[] sourceDoubles =
              createDouble(fallbackDoubleValue, candidateDoubleValue, selectedDoubleValue);
          candidateValue =
              calculateValue3(
                  floats,
                  candidateValue,
                  0.0F,
                  0.0F,
                  outputValue,
                  0.0F,
                  0.0F,
                  1.0F,
                  (float) fallbackDoubleValue,
                  0.0F,
                  (float) sourceDoubles[0],
                  (float) sourceDoubles[1],
                  0.0F,
                  -1.0F);
        }

        int selectedValue = previousValue;
        double primaryDoubleValue =
            Math.sin(3.141592653589793 * Math.floor(sourceValue * 0.5) / sourceValue);
        double secondaryDoubleValue = 1.0 / primaryDoubleValue;

        for (int previousIndex = 1; previousIndex < sourceValue; previousIndex++) {
          double tertiaryDoubleValue = (double) previousIndex / sourceValue;
          double temporaryDoubleValue = tertiaryDoubleValue * 3.141592653589793;
          double requestedDoubleValue = Math.sin(temporaryDoubleValue);
          double actualDoubleValue = Math.cos(temporaryDoubleValue);

          for (int sourceIndex = 0; sourceIndex <= previousValue; sourceIndex++) {
            double expectedDoubleValue = doubles[sourceIndex];
            double minimumDoubleValue = currentDoubles[sourceIndex];
            double maximumDoubleValue = nextDoubles[sourceIndex];
            double startDoubleValue = previousDoubles[sourceIndex];
            double endDoubleValue = secondaryDoubleValue * requestedDoubleValue;
            double localDoubleValue =
                outputValue
                    * secondaryDoubleValue
                    * requestedDoubleValue
                    * requestedDoubleValue
                    * startDoubleValue;
            double storedDoubleValue =
                -outputValue
                    * secondaryDoubleValue
                    * requestedDoubleValue
                    * requestedDoubleValue
                    * maximumDoubleValue;
            double createdDoubleValue =
                secondaryDoubleValue
                    * secondaryDoubleValue
                    * requestedDoubleValue
                    * actualDoubleValue
                    * (expectedDoubleValue * startDoubleValue
                        - minimumDoubleValue * maximumDoubleValue);
            double updatedDoubleValue =
                calculateValue(localDoubleValue, storedDoubleValue, createdDoubleValue);
            double removedDoubleValue = calculateValue(maximumDoubleValue, startDoubleValue, 0.0);
            candidateValue =
                calculateValue3(
                    floats,
                    candidateValue,
                    (float) (endDoubleValue * expectedDoubleValue),
                    (float) (endDoubleValue * minimumDoubleValue),
                    (float) (outputValue * actualDoubleValue),
                    (float) (localDoubleValue * updatedDoubleValue),
                    (float) (storedDoubleValue * updatedDoubleValue),
                    (float) (createdDoubleValue * updatedDoubleValue),
                    (float) sourceIndex / previousValue,
                    (float) tertiaryDoubleValue,
                    (float) (maximumDoubleValue * removedDoubleValue),
                    (float) (startDoubleValue * removedDoubleValue),
                    0.0F,
                    -1.0F);
          }
        }

        int defaultValue = selectedValue + (sourceValue - 1) * (previousValue + 1);

        for (int targetIndex = 0; targetIndex < previousValue; targetIndex++) {
          double firstDoubleValue = (targetIndex + 0.5) / previousValue;
          double[] targetDoubles =
              createDouble(firstDoubleValue, candidateDoubleValue, selectedDoubleValue);
          candidateValue =
              calculateValue3(
                  floats,
                  candidateValue,
                  0.0F,
                  0.0F,
                  -outputValue,
                  0.0F,
                  0.0F,
                  -1.0F,
                  (float) firstDoubleValue,
                  1.0F,
                  (float) targetDoubles[0],
                  (float) targetDoubles[1],
                  0.0F,
                  -1.0F);
        }

        int inputIndex = 0;

        for (int outputIndex = 0; outputIndex < previousValue; outputIndex++) {
          ints[inputIndex++] = outputIndex;
          ints[inputIndex++] = selectedValue + outputIndex;
          ints[inputIndex++] = selectedValue + outputIndex + 1;
        }

        for (int resultIndex = 0; resultIndex < sourceValue - 2; resultIndex++) {
          int initialValue = selectedValue + resultIndex * (previousValue + 1);
          int resolvedValue = initialValue + previousValue + 1;

          for (int candidateIndex = 0; candidateIndex < previousValue; candidateIndex++) {
            int computedValue = initialValue + candidateIndex;
            int cachedValue = computedValue + 1;
            int pendingValue = resolvedValue + candidateIndex;
            int activeValue = pendingValue + 1;
            ints[inputIndex++] = computedValue;
            ints[inputIndex++] = pendingValue;
            ints[inputIndex++] = cachedValue;
            ints[inputIndex++] = cachedValue;
            ints[inputIndex++] = pendingValue;
            ints[inputIndex++] = activeValue;
          }
        }

        int fallbackValue = selectedValue + (sourceValue - 2) * (previousValue + 1);

        for (int selectedIndex = 0; selectedIndex < previousValue; selectedIndex++) {
          ints[inputIndex++] = defaultValue + selectedIndex;
          ints[inputIndex++] = fallbackValue + selectedIndex + 1;
          ints[inputIndex++] = fallbackValue + selectedIndex;
        }

        if (candidateValue == floats.length && inputIndex == ints.length) {
          return new GeometryVertexCountService(floats, ints);
        } else {
          throw new IllegalStateException("internal heart tessellation count mismatch");
        }
      } else {
        throw new IllegalArgumentException("heart tessellation is too large");
      }
    } else {
      throw new IllegalArgumentException("heart dimensions are outside the usable float range");
    }
  }

  private static double[] createDouble(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    double previousDoubleValue = -6.283185307179586 * doubleValue;
    double sourceDoubleValue = Math.sin(previousDoubleValue);
    double targetDoubleValue = Math.cos(previousDoubleValue);
    double inputDoubleValue =
        -(48.0 * sourceDoubleValue * sourceDoubleValue + 0.8)
            * targetDoubleValue
            * currentDoubleValue;
    double outputDoubleValue =
        -(-13.0 * sourceDoubleValue
                + 10.0 * Math.sin(2.0 * previousDoubleValue)
                + 6.0 * Math.sin(3.0 * previousDoubleValue)
                + 4.0 * Math.sin(4.0 * previousDoubleValue))
            * nextDoubleValue;
    double resultDoubleValue = calculateValue(inputDoubleValue, outputDoubleValue, 0.0);
    return new double[] {
      inputDoubleValue * resultDoubleValue, outputDoubleValue * resultDoubleValue
    };
  }

  private static double calculateValue(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    double previousDoubleValue =
        doubleValue * doubleValue
            + currentDoubleValue * currentDoubleValue
            + nextDoubleValue * nextDoubleValue;
    if (previousDoubleValue > 0.0 && Double.isFinite(previousDoubleValue)) {
      return 1.0 / Math.sqrt(previousDoubleValue);
    } else {
      throw new IllegalArgumentException("heart dimensions produce an invalid vertex frame");
    }
  }

  private static int calculateValue2(
      float[] floats,
      int value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue,
      float initialValue,
      float resolvedValue,
      float computedValue,
      float cachedValue,
      float pendingValue,
      float activeValue,
      float fallbackValue,
      float primaryValue) {
    value =
        calculateValue3(
            floats,
            value,
            currentValue,
            nextValue,
            previousValue,
            resolvedValue,
            computedValue,
            cachedValue,
            0.0F,
            0.0F,
            pendingValue,
            activeValue,
            fallbackValue,
            primaryValue);
    value =
        calculateValue3(
            floats,
            value,
            sourceValue,
            targetValue,
            inputValue,
            resolvedValue,
            computedValue,
            cachedValue,
            1.0F,
            0.0F,
            pendingValue,
            activeValue,
            fallbackValue,
            primaryValue);
    value =
        calculateValue3(
            floats,
            value,
            outputValue,
            resultValue,
            candidateValue,
            resolvedValue,
            computedValue,
            cachedValue,
            1.0F,
            1.0F,
            pendingValue,
            activeValue,
            fallbackValue,
            primaryValue);
    return calculateValue3(
        floats,
        value,
        selectedValue,
        defaultValue,
        initialValue,
        resolvedValue,
        computedValue,
        cachedValue,
        0.0F,
        1.0F,
        pendingValue,
        activeValue,
        fallbackValue,
        primaryValue);
  }

  private static int calculateValue3(
      float[] floats,
      int index,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue,
      float outputValue,
      float resultValue,
      float candidateValue,
      float selectedValue,
      float defaultValue) {
    floats[index++] = value;
    floats[index++] = currentValue;
    floats[index++] = nextValue;
    floats[index++] = previousValue;
    floats[index++] = sourceValue;
    floats[index++] = targetValue;
    floats[index++] = inputValue;
    floats[index++] = outputValue;
    floats[index++] = resultValue;
    floats[index++] = candidateValue;
    floats[index++] = selectedValue;
    floats[index++] = defaultValue;
    return index;
  }

  private static void updateState(float value, String text) {
    if (!Float.isFinite(value) || value <= 0.0F) {
      throw new IllegalArgumentException(text + " must be finite and greater than zero");
    }
  }
}
