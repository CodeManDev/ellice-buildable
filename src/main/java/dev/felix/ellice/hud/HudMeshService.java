package dev.felix.ellice.hud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HudMeshService {
  private HudMeshService() {}

  public static HudMeshService.Mesh build(
      List<HudMeshService.Row> items, float value, float currentValue) {
    List currentItems =
        items.stream()
            .filter(
                item ->
                    Float.isFinite(item.width)
                        && Float.isFinite(item.height)
                        && item.width > 0.001F
                        && item.height > 0.001F)
            .toList();
    if (currentItems.isEmpty()) {
      return new HudMeshService.Mesh(new float[0], new float[0], 0.0F, 0.0F);
    }

    ArrayList arrayList = new ArrayList();
    arrayList.add(new HudMeshService.Point(0.0F, 0.0F));
    arrayList.add(
        new HudMeshService.Point(((HudMeshService.Row) currentItems.getFirst()).width, 0.0F));
    float nextValue = 0.0F;
    float previousValue = 0.0F;

    for (int index = 0; index < currentItems.size(); index++) {
      HudMeshService.Row row = (HudMeshService.Row) currentItems.get(index);
      nextValue += row.height;
      previousValue = Math.max(previousValue, row.width);
      if (index + 1 >= currentItems.size()
          || !(Math.abs(row.width - ((HudMeshService.Row) currentItems.get(index + 1)).width)
              < 0.001F)) {
        arrayList.add(new HudMeshService.Point(row.width, nextValue));
        if (index + 1 < currentItems.size()) {
          arrayList.add(
              new HudMeshService.Point(
                  ((HudMeshService.Row) currentItems.get(index + 1)).width, nextValue));
        }
      }
    }

    arrayList.add(new HudMeshService.Point(0.0F, nextValue));
    ArrayList currentArrayList = new ArrayList();

    for (int currentIndex = 0; currentIndex < arrayList.size(); currentIndex++) {
      HudMeshService.Point currentSize =
          (HudMeshService.Point)
              arrayList.get((currentIndex + arrayList.size() - 1) % arrayList.size());
      HudMeshService.Point point = (HudMeshService.Point) arrayList.get(currentIndex);
      HudMeshService.Point nextSize =
          (HudMeshService.Point) arrayList.get((currentIndex + 1) % arrayList.size());
      float sourceValue = calculateValue2(currentSize, point);
      float targetValue = calculateValue2(point, nextSize);
      float inputValue = Math.max(0.0F, Math.min(value, Math.min(sourceValue, targetValue) * 0.5F));
      HudMeshService.Point currentPoint = createPoint(point, currentSize, inputValue / sourceValue);
      HudMeshService.Point nextPoint = createPoint(point, nextSize, inputValue / targetValue);
      updateState(currentArrayList, currentPoint);

      for (int nextIndex = 1; nextIndex <= 6; nextIndex++) {
        float outputValue = nextIndex / 6.0F;
        float resultValue = 1.0F - outputValue;
        updateState(
            currentArrayList,
            new HudMeshService.Point(
                resultValue * resultValue * currentPoint.x
                    + 2.0F * resultValue * outputValue * point.x
                    + outputValue * outputValue * nextPoint.x,
                resultValue * resultValue * currentPoint.y
                    + 2.0F * resultValue * outputValue * point.y
                    + outputValue * outputValue * nextPoint.y));
      }
    }

    if (currentArrayList.size() > 1
        && calculateValue2(
                (HudMeshService.Point) currentArrayList.getFirst(),
                (HudMeshService.Point) currentArrayList.getLast())
            <= 1.0E-5F) {
      currentArrayList.removeLast();
    }

    return new HudMeshService.Mesh(
        createFloat(currentArrayList),
        createFloat2(currentArrayList, currentValue),
        previousValue,
        nextValue);
  }

  private static float[] createFloat(List<HudMeshService.Point> items) {
    List currentItems = items.stream().map(HudMeshService.Point::y).distinct().sorted().toList();
    float[] currentSize = new float[currentItems.size() * 18];
    int value = 0;

    for (int index = 0; index + 1 < currentItems.size(); index++) {
      float currentValue = (Float) currentItems.get(index);
      float nextValue = (Float) currentItems.get(index + 1);
      double doubleValue = ((double) currentValue + nextValue) * 0.5;
      float previousValue = Float.POSITIVE_INFINITY;
      float sourceValue = 0.0F;
      float targetValue = Float.NEGATIVE_INFINITY;
      float inputValue = 0.0F;
      double currentDoubleValue = Double.POSITIVE_INFINITY;
      double nextDoubleValue = Double.NEGATIVE_INFINITY;

      for (int currentIndex = 0; currentIndex < items.size(); currentIndex++) {
        HudMeshService.Point point = (HudMeshService.Point) items.get(currentIndex);
        HudMeshService.Point nextSize =
            (HudMeshService.Point) items.get((currentIndex + 1) % items.size());
        if (!(doubleValue <= Math.min(point.y, nextSize.y))
            && !(doubleValue >= Math.max(point.y, nextSize.y))) {
          double previousDoubleValue = (double) nextSize.x - point.x;
          double sourceDoubleValue = (double) nextSize.y - point.y;
          double targetDoubleValue =
              point.x + (doubleValue - point.y) / sourceDoubleValue * previousDoubleValue;
          float outputValue =
              (float)
                  (point.x + (currentValue - point.y) / sourceDoubleValue * previousDoubleValue);
          float resultValue =
              (float) (point.x + (nextValue - point.y) / sourceDoubleValue * previousDoubleValue);
          if (targetDoubleValue < currentDoubleValue) {
            currentDoubleValue = targetDoubleValue;
            previousValue = outputValue;
            sourceValue = resultValue;
          }

          if (targetDoubleValue > nextDoubleValue) {
            nextDoubleValue = targetDoubleValue;
            targetValue = outputValue;
            inputValue = resultValue;
          }
        }
      }

      if (Float.isFinite(previousValue) && Float.isFinite(targetValue)) {
        value =
            calculateValue(
                currentSize,
                value,
                previousValue,
                currentValue,
                targetValue,
                currentValue,
                inputValue,
                nextValue);
        value =
            calculateValue(
                currentSize,
                value,
                previousValue,
                currentValue,
                inputValue,
                nextValue,
                sourceValue,
                nextValue);
      }
    }

    return Arrays.copyOf(currentSize, value);
  }

  private static int calculateValue(
      float[] floats,
      int index,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue) {
    if (Math.abs(
            (nextValue - value) * (targetValue - currentValue)
                - (previousValue - currentValue) * (sourceValue - value))
        < 1.0E-7F) {
      return index;
    }

    floats[index++] = value;
    floats[index++] = currentValue;
    floats[index++] = 0.0F;
    floats[index++] = nextValue;
    floats[index++] = previousValue;
    floats[index++] = 0.0F;
    floats[index++] = sourceValue;
    floats[index++] = targetValue;
    floats[index++] = 0.0F;
    return index;
  }

  private static float[] createFloat2(List<HudMeshService.Point> items, float value) {
    int currentSize = items.size();
    float[] floats = new float[currentSize * 2];
    float[] currentFloats = new float[currentSize * 18];

    for (int index = 0; index < currentSize; index++) {
      HudMeshService.Point point =
          (HudMeshService.Point) items.get((index + currentSize - 1) % currentSize);
      HudMeshService.Point currentPoint = (HudMeshService.Point) items.get(index);
      HudMeshService.Point nextPoint = (HudMeshService.Point) items.get((index + 1) % currentSize);
      float currentValue = calculateValue2(point, currentPoint);
      float nextValue = calculateValue2(currentPoint, nextPoint);
      float previousValue =
          (currentPoint.y - point.y) / currentValue + (nextPoint.y - currentPoint.y) / nextValue;
      float sourceValue =
          (point.x - currentPoint.x) / currentValue + (currentPoint.x - nextPoint.x) / nextValue;
      float targetValue = Math.max(0.001F, (float) Math.hypot(previousValue, sourceValue));
      previousValue /= targetValue;
      sourceValue /= targetValue;
      float inputValue =
          previousValue * (nextPoint.y - currentPoint.y) / nextValue
              + sourceValue * (currentPoint.x - nextPoint.x) / nextValue;
      float outputValue = Math.min(2.0F, 1.0F / Math.max(0.5F, inputValue)) * value * 0.5F;
      floats[index * 2] = previousValue * outputValue;
      floats[index * 2 + 1] = sourceValue * outputValue;
    }

    int[] ints = new int[] {0, 0, 1, 1, 0, 1};
    float[] nextFloats = new float[] {1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F};

    for (int currentIndex = 0; currentIndex < currentSize; currentIndex++) {
      for (int nextIndex = 0; nextIndex < 6; nextIndex++) {
        int resultValue = (currentIndex + ints[nextIndex]) % currentSize;
        int previousIndex = currentIndex * 18 + nextIndex * 3;
        currentFloats[previousIndex] =
            ((HudMeshService.Point) items.get(resultValue)).x
                + floats[resultValue * 2] * nextFloats[nextIndex];
        currentFloats[previousIndex + 1] =
            ((HudMeshService.Point) items.get(resultValue)).y
                + floats[resultValue * 2 + 1] * nextFloats[nextIndex];
        currentFloats[previousIndex + 2] = nextFloats[nextIndex];
      }
    }

    return currentFloats;
  }

  private static void updateState(List<HudMeshService.Point> items, HudMeshService.Point point) {
    if (items.isEmpty()
        || calculateValue2((HudMeshService.Point) items.getLast(), point) > 1.0E-5F) {
      items.add(point);
    }
  }

  private static float calculateValue2(
      HudMeshService.Point point, HudMeshService.Point currentPoint) {
    return (float) Math.hypot(currentPoint.x - point.x, currentPoint.y - point.y);
  }

  private static HudMeshService.Point createPoint(
      HudMeshService.Point point, HudMeshService.Point currentPoint, float width) {
    return new HudMeshService.Point(
        point.x + (currentPoint.x - point.x) * width, point.y + (currentPoint.y - point.y) * width);
  }

  public record Mesh(float[] fill, float[] edge, float width, float height) {}

  private record Point(float x, float y) {}

  public record Row(float width, float height) {}
}
