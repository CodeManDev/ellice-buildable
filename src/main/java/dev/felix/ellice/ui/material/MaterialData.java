package dev.felix.ellice.ui.material;

import java.util.LinkedHashMap;
import java.util.Map;

public record MaterialData(Map<String, Integer> colors) {
  public MaterialData(Map<String, Integer> colors) {
    colors = Map.copyOf(colors);
    this.colors = colors;
  }

  public int color(String text) {
    return this.colors.get(text);
  }

  public static MaterialData create(int value, int currentValue, boolean enabled) {
    if (!enabled && value == -3097345 && currentValue == -15461864) {
      return defaults();
    }

    LinkedHashMap linkedHashMap = new LinkedHashMap();
    double[] doubles = createDouble(currentValue);
    double[] currentDoubles = createDouble(value);
    double doubleValue = Math.min(0.16, doubles[1]);
    double currentDoubleValue = Math.min(0.85, currentDoubles[1]);
    linkedHashMap.put("SURFACE", calculateValue4(doubles[0], doubleValue, enabled ? 0.975 : 0.08));
    linkedHashMap.put(
        "SURFACE_LOWEST", calculateValue4(doubles[0], doubleValue, enabled ? 1.0 : 0.055));
    linkedHashMap.put(
        "SURFACE_LOW", calculateValue4(doubles[0], doubleValue, enabled ? 0.96 : 0.115));
    linkedHashMap.put(
        "SURFACE_CONTAINER", calculateValue4(doubles[0], doubleValue, enabled ? 0.935 : 0.14));
    linkedHashMap.put(
        "SURFACE_HIGH", calculateValue4(doubles[0], doubleValue, enabled ? 0.91 : 0.18));
    linkedHashMap.put(
        "SURFACE_HIGHEST", calculateValue4(doubles[0], doubleValue, enabled ? 0.88 : 0.225));
    linkedHashMap.put(
        "PRIMARY",
        calculateValue3(
            calculateValue4(currentDoubles[0], currentDoubleValue, enabled ? 0.38 : 0.78),
            (Integer) linkedHashMap.get("SURFACE_HIGHEST"),
            4.5));
    linkedHashMap.put(
        "ON_PRIMARY",
        calculateValue3(
            calculateValue4(currentDoubles[0], currentDoubleValue * 0.6, enabled ? 0.99 : 0.15),
            (Integer) linkedHashMap.get("PRIMARY"),
            4.5));
    linkedHashMap.put(
        "PRIMARY_CONTAINER",
        calculateValue4(currentDoubles[0], currentDoubleValue * 0.6, enabled ? 0.9 : 0.29));
    linkedHashMap.put(
        "ON_PRIMARY_CONTAINER",
        calculateValue3(
            calculateValue4(currentDoubles[0], currentDoubleValue * 0.5, enabled ? 0.16 : 0.92),
            (Integer) linkedHashMap.get("PRIMARY_CONTAINER"),
            4.5));
    linkedHashMap.put(
        "SECONDARY",
        calculateValue4(currentDoubles[0], currentDoubleValue * 0.2, enabled ? 0.38 : 0.8));
    linkedHashMap.put(
        "ON_SECONDARY",
        calculateValue3(
            calculateValue4(currentDoubles[0], currentDoubleValue * 0.2, enabled ? 0.99 : 0.16),
            (Integer) linkedHashMap.get("SECONDARY"),
            4.5));
    linkedHashMap.put(
        "SECONDARY_CONTAINER",
        calculateValue4(currentDoubles[0], currentDoubleValue * 0.22, enabled ? 0.9 : 0.28));
    linkedHashMap.put(
        "ON_SECONDARY_CONTAINER",
        calculateValue3(
            calculateValue4(currentDoubles[0], currentDoubleValue * 0.2, enabled ? 0.15 : 0.92),
            (Integer) linkedHashMap.get("SECONDARY_CONTAINER"),
            4.5));
    linkedHashMap.put(
        "TERTIARY",
        calculateValue3(
            calculateValue4(
                currentDoubles[0] + 0.12, currentDoubleValue * 0.7, enabled ? 0.38 : 0.78),
            (Integer) linkedHashMap.get("SURFACE_HIGHEST"),
            4.5));
    linkedHashMap.put(
        "ON_SURFACE",
        calculateValue3(
            calculateValue4(doubles[0], doubleValue, enabled ? 0.12 : 0.91),
            (Integer) linkedHashMap.get("SURFACE_HIGHEST"),
            7.0));
    linkedHashMap.put(
        "ON_SURFACE_VARIANT",
        calculateValue3(
            calculateValue4(doubles[0], doubleValue, enabled ? 0.34 : 0.8),
            (Integer) linkedHashMap.get("SURFACE_HIGHEST"),
            4.5));
    linkedHashMap.put(
        "OUTLINE",
        calculateValue3(
            calculateValue4(doubles[0], doubleValue, enabled ? 0.44 : 0.62),
            (Integer) linkedHashMap.get("SURFACE_HIGHEST"),
            3.0));
    linkedHashMap.put(
        "OUTLINE_VARIANT", calculateValue4(doubles[0], doubleValue, enabled ? 0.76 : 0.3));
    linkedHashMap.put(
        "ERROR",
        calculateValue3(
            enabled ? -4580838 : -870219, (Integer) linkedHashMap.get("SURFACE_HIGHEST"), 4.5));
    double[] nextDoubles = createDouble(enabled ? -4580838 : -870219);
    linkedHashMap.put(
        "ERROR_CONTAINER",
        calculateValue4(nextDoubles[0], Math.min(0.6, nextDoubles[1]), enabled ? 0.9 : 0.25));
    linkedHashMap.put(
        "ON_ERROR_CONTAINER",
        calculateValue3(
            enabled ? -12517374 : -9514, (Integer) linkedHashMap.get("ERROR_CONTAINER"), 4.5));
    linkedHashMap.put(
        "INVERSE_SURFACE", calculateValue4(doubles[0], doubleValue, enabled ? 0.2 : 0.9));
    linkedHashMap.put(
        "INVERSE_ON_SURFACE",
        calculateValue3(
            calculateValue4(doubles[0], doubleValue, enabled ? 0.95 : 0.16),
            (Integer) linkedHashMap.get("INVERSE_SURFACE"),
            7.0));
    return new MaterialData(linkedHashMap);
  }

  public static MaterialData defaults() {
    String[] strings =
        new String[] {
          "SURFACE",
          "SURFACE_LOWEST",
          "SURFACE_LOW",
          "SURFACE_CONTAINER",
          "SURFACE_HIGH",
          "SURFACE_HIGHEST",
          "PRIMARY",
          "ON_PRIMARY",
          "PRIMARY_CONTAINER",
          "ON_PRIMARY_CONTAINER",
          "SECONDARY",
          "ON_SECONDARY",
          "SECONDARY_CONTAINER",
          "ON_SECONDARY_CONTAINER",
          "TERTIARY",
          "ON_SURFACE",
          "ON_SURFACE_VARIANT",
          "OUTLINE",
          "OUTLINE_VARIANT",
          "ERROR",
          "ERROR_CONTAINER",
          "ON_ERROR_CONTAINER",
          "INVERSE_SURFACE",
          "INVERSE_ON_SURFACE"
        };
    int[] ints =
        new int[] {
          -15461864, -15790829, -14869728, -14606554, -13948624, -13224901, -3097345, -13099406,
          -11585653, -1384961, -3357988, -13423295, -11910056, -1515784, -1066808, -1646359,
          -3488560, -7106663, -11975345, -870219, -7143414, -9514, -1646359, -13488331
        };
    LinkedHashMap linkedHashMap = new LinkedHashMap();

    for (int index = 0; index < strings.length; index++) {
      linkedHashMap.put(strings[index], ints[index]);
    }

    return new MaterialData(linkedHashMap);
  }

  public static double contrast(int value, int currentValue) {
    double doubleValue = calculateValue(value);
    double currentDoubleValue = calculateValue(currentValue);
    return (Math.max(doubleValue, currentDoubleValue) + 0.05)
        / (Math.min(doubleValue, currentDoubleValue) + 0.05);
  }

  private static double calculateValue(int value) {
    return 0.2126 * calculateValue2(value >>> 16 & 0xFF)
        + 0.7152 * calculateValue2(value >>> 8 & 0xFF)
        + 0.0722 * calculateValue2(value & 0xFF);
  }

  private static double calculateValue2(int value) {
    double doubleValue = value / 255.0;
    return doubleValue <= 0.04045
        ? doubleValue / 12.92
        : Math.pow((doubleValue + 0.055) / 1.055, 2.4);
  }

  private static int calculateValue3(int value, int currentValue, double doubleValue) {
    if (contrast(value, currentValue) >= doubleValue) {
      return value;
    }

    int nextValue = contrast(-16777216, currentValue) > contrast(-1, currentValue) ? -16777216 : -1;

    for (int index = 1; index <= 100; index++) {
      int previousValue = MaterialIsLightService.layer(value, nextValue, index / 100.0F);
      if (contrast(previousValue, currentValue) >= doubleValue) {
        return previousValue;
      }
    }

    return nextValue;
  }

  private static double[] createDouble(int value) {
    double doubleValue = (value >>> 16 & 0xFF) / 255.0;
    double currentDoubleValue = (value >>> 8 & 0xFF) / 255.0;
    double nextDoubleValue = (value & 0xFF) / 255.0;
    double previousDoubleValue =
        Math.max(doubleValue, Math.max(currentDoubleValue, nextDoubleValue));
    double sourceDoubleValue = Math.min(doubleValue, Math.min(currentDoubleValue, nextDoubleValue));
    double targetDoubleValue = previousDoubleValue - sourceDoubleValue;
    double inputDoubleValue = (previousDoubleValue + sourceDoubleValue) / 2.0;
    if (targetDoubleValue < 1.0E-8) {
      return new double[] {0.0, 0.0, inputDoubleValue};
    }

    double outputDoubleValue =
        previousDoubleValue == doubleValue
            ? (currentDoubleValue - nextDoubleValue) / targetDoubleValue
                + (currentDoubleValue < nextDoubleValue ? 6 : 0)
            : (previousDoubleValue == currentDoubleValue
                ? (nextDoubleValue - doubleValue) / targetDoubleValue + 2.0
                : (doubleValue - currentDoubleValue) / targetDoubleValue + 4.0);
    return new double[] {
      outputDoubleValue / 6.0,
      targetDoubleValue / (1.0 - Math.abs(2.0 * inputDoubleValue - 1.0)),
      inputDoubleValue
    };
  }

  private static int calculateValue4(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    doubleValue -= Math.floor(doubleValue);
    double previousDoubleValue = (1.0 - Math.abs(2.0 * nextDoubleValue - 1.0)) * currentDoubleValue;
    double sourceDoubleValue =
        previousDoubleValue * (1.0 - Math.abs(doubleValue * 6.0 % 2.0 - 1.0));
    double targetDoubleValue = nextDoubleValue - previousDoubleValue / 2.0;
    double inputDoubleValue;
    double outputDoubleValue;
    double resultDoubleValue;
    if (doubleValue < 0.16666666666666666) {
      inputDoubleValue = previousDoubleValue;
      outputDoubleValue = sourceDoubleValue;
      resultDoubleValue = 0.0;
    } else if (doubleValue < 0.3333333333333333) {
      inputDoubleValue = sourceDoubleValue;
      outputDoubleValue = previousDoubleValue;
      resultDoubleValue = 0.0;
    } else if (doubleValue < 0.5) {
      inputDoubleValue = 0.0;
      outputDoubleValue = previousDoubleValue;
      resultDoubleValue = sourceDoubleValue;
    } else if (doubleValue < 0.6666666666666666) {
      inputDoubleValue = 0.0;
      outputDoubleValue = sourceDoubleValue;
      resultDoubleValue = previousDoubleValue;
    } else if (doubleValue < 0.8333333333333334) {
      inputDoubleValue = sourceDoubleValue;
      outputDoubleValue = 0.0;
      resultDoubleValue = previousDoubleValue;
    } else {
      inputDoubleValue = previousDoubleValue;
      outputDoubleValue = 0.0;
      resultDoubleValue = sourceDoubleValue;
    }

    return 0xFF000000
        | (int) Math.round((inputDoubleValue + targetDoubleValue) * 255.0) << 16
        | (int) Math.round((outputDoubleValue + targetDoubleValue) * 255.0) << 8
        | (int) Math.round((resultDoubleValue + targetDoubleValue) * 255.0);
  }
}
