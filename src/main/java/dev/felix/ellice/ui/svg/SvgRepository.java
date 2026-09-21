package dev.felix.ellice.ui.svg;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class SvgRepository {
  private final List<SvgRepository.Shape> items;
  private final float value;
  private final float value2;
  static final float[] IDENTITY = new float[] {1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};

  public SvgRepository(
      List<SvgRepository.Shape> currentItems, float currentValue, float nextValue) {
    this.items = List.copyOf(currentItems);
    this.value = currentValue;
    this.value2 = nextValue;
  }

  public List<SvgRepository.Shape> shapes() {
    return this.items;
  }

  public float viewBoxW() {
    return this.value;
  }

  public float viewBoxH() {
    return this.value2;
  }

  public static SvgRepository load(Path path) {
    try (InputStream inputStream = Files.newInputStream(path)) {
      Document document =
          DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
      Element element = document.getDocumentElement();
      float value = 24.0F;
      float currentValue = 24.0F;
      String text = element.getAttribute("viewBox");
      if (!text.isEmpty()) {
        String[] strings = text.trim().split("[\\s,]+");
        if (strings.length >= 4) {
          value = Float.parseFloat(strings[2]);
          currentValue = Float.parseFloat(strings[3]);
        }
      }

      String currentText = element.getAttribute("fill");
      String nextText = element.getAttribute("stroke");
      String previousText = element.getAttribute("stroke-linecap");
      String sourceText = element.getAttribute("stroke-linejoin");
      ArrayList arrayList = new ArrayList();
      updateState(
          element,
          arrayList,
          value,
          currentValue,
          currentText,
          nextText,
          previousText,
          sourceText,
          IDENTITY);
      return new SvgRepository(arrayList, value, currentValue);
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Failed to load SVG: {}", path, exception);
      return new SvgRepository(List.of(), 24.0F, 24.0F);
    }
  }

  private static void updateState(
      Element element,
      List<SvgRepository.Shape> items,
      float value,
      float currentValue,
      String text,
      String currentText,
      String nextText,
      String previousText,
      float[] floats) {
    NodeList nodeList = element.getChildNodes();

    for (int index = 0; index < nodeList.getLength(); index++) {
      if (nodeList.item(index) instanceof Element currentElement) {
        String sourceText = currentElement.getTagName().toLowerCase();
        float[] currentFloats = createFloat(currentElement, floats);
        if ("path".equals(sourceText)) {
          String targetText = currentElement.getAttribute("d");
          if (!targetText.isEmpty()) {
            SvgCodec svgCodec =
                SvgCodec.parse(targetText, value, currentValue).transform(currentFloats);
            updateState2(
                items, currentElement, svgCodec, text, currentText, nextText, previousText);
          }
        } else if ("circle".equals(sourceText)) {
          float nextValue = calculateValue2(currentElement.getAttribute("cx"), 0.0F);
          float previousValue = calculateValue2(currentElement.getAttribute("cy"), 0.0F);
          float sourceValue = calculateValue2(currentElement.getAttribute("r"), 0.0F);
          if (!(sourceValue <= 0.0F)) {
            SvgCodec currentSvgCodec =
                SvgCodec.parse(
                        createText(nextValue, previousValue, sourceValue, sourceValue),
                        value,
                        currentValue)
                    .transform(currentFloats);
            updateState2(
                items, currentElement, currentSvgCodec, text, currentText, nextText, previousText);
          }
        } else if ("ellipse".equals(sourceText)) {
          float targetValue = calculateValue2(currentElement.getAttribute("cx"), 0.0F);
          float inputValue = calculateValue2(currentElement.getAttribute("cy"), 0.0F);
          float outputValue = calculateValue2(currentElement.getAttribute("rx"), 0.0F);
          float resultValue = calculateValue2(currentElement.getAttribute("ry"), 0.0F);
          if (!(outputValue <= 0.0F) && !(resultValue <= 0.0F)) {
            SvgCodec nextSvgCodec =
                SvgCodec.parse(
                        createText(targetValue, inputValue, outputValue, resultValue),
                        value,
                        currentValue)
                    .transform(currentFloats);
            updateState2(
                items, currentElement, nextSvgCodec, text, currentText, nextText, previousText);
          }
        } else if ("rect".equals(sourceText)) {
          float candidateValue = calculateValue2(currentElement.getAttribute("x"), 0.0F);
          float selectedValue = calculateValue2(currentElement.getAttribute("y"), 0.0F);
          float defaultValue = calculateValue2(currentElement.getAttribute("width"), 0.0F);
          float initialValue = calculateValue2(currentElement.getAttribute("height"), 0.0F);
          if (!(defaultValue <= 0.0F) && !(initialValue <= 0.0F)) {
            float resolvedValue = calculateValue2(currentElement.getAttribute("rx"), 0.0F);
            float computedValue = calculateValue2(currentElement.getAttribute("ry"), 0.0F);
            if (resolvedValue > 0.0F && computedValue <= 0.0F) {
              computedValue = resolvedValue;
            }

            if (computedValue > 0.0F && resolvedValue <= 0.0F) {
              resolvedValue = computedValue;
            }

            String inputText =
                !(resolvedValue > 0.0F) && !(computedValue > 0.0F)
                    ? String.format(
                        Locale.US,
                        "M%f %fh%fv%fh%fZ",
                        candidateValue,
                        selectedValue,
                        defaultValue,
                        initialValue,
                        -defaultValue)
                    : createText2(
                        candidateValue,
                        selectedValue,
                        defaultValue,
                        initialValue,
                        resolvedValue,
                        computedValue);
            SvgCodec previousSvgCodec =
                SvgCodec.parse(inputText, value, currentValue).transform(currentFloats);
            updateState2(
                items, currentElement, previousSvgCodec, text, currentText, nextText, previousText);
          }
        } else if ("line".equals(sourceText)) {
          float cachedValue = calculateValue2(currentElement.getAttribute("x1"), 0.0F);
          float pendingValue = calculateValue2(currentElement.getAttribute("y1"), 0.0F);
          float activeValue = calculateValue2(currentElement.getAttribute("x2"), 0.0F);
          float fallbackValue = calculateValue2(currentElement.getAttribute("y2"), 0.0F);
          SvgCodec sourceSvgCodec =
              SvgCodec.parse(
                      String.format(
                          Locale.US,
                          "M%f %fL%f %f",
                          cachedValue,
                          pendingValue,
                          activeValue,
                          fallbackValue),
                      value,
                      currentValue)
                  .transform(currentFloats);
          updateState2(
              items, currentElement, sourceSvgCodec, "none", currentText, nextText, previousText);
        } else if ("polyline".equals(sourceText) || "polygon".equals(sourceText)) {
          String outputText = currentElement.getAttribute("points");
          if (!outputText.isEmpty()) {
            String[] strings = outputText.trim().split("[\\s,]+");
            StringBuilder stringBuilder = new StringBuilder();

            for (byte currentIndex = 0; currentIndex + 1 < strings.length; currentIndex += 2) {
              stringBuilder.append(currentIndex == 0 ? "M" : "L");
              stringBuilder
                  .append(strings[currentIndex])
                  .append(' ')
                  .append(strings[currentIndex + 1]);
            }

            if ("polygon".equals(sourceText)) {
              stringBuilder.append("Z");
            }

            String resultText = "polyline".equals(sourceText) ? "none" : text;
            SvgCodec targetSvgCodec =
                SvgCodec.parse(stringBuilder.toString(), value, currentValue)
                    .transform(currentFloats);
            updateState2(
                items,
                currentElement,
                targetSvgCodec,
                resultText,
                currentText,
                nextText,
                previousText);
          }
        } else if ("g".equals(sourceText)) {
          String candidateText = createText3(currentElement, "fill", text);
          String selectedText = createText3(currentElement, "stroke", currentText);
          String defaultText = createText3(currentElement, "stroke-linecap", nextText);
          String initialText = createText3(currentElement, "stroke-linejoin", previousText);
          updateState(
              currentElement,
              items,
              value,
              currentValue,
              candidateText,
              selectedText,
              defaultText,
              initialText,
              currentFloats);
        }
      }
    }
  }

  private static void updateState2(
      List<SvgRepository.Shape> items,
      Element element,
      SvgCodec svgCodec,
      String text,
      String currentText,
      String nextText,
      String previousText) {
    String sourceText = createText3(element, "fill", text);
    String targetText = createText3(element, "stroke", currentText);
    int value = sourceText != null && !"none".equalsIgnoreCase(sourceText) ? 1 : 0;
    int currentValue = targetText != null && !"none".equalsIgnoreCase(targetText) ? 1 : 0;
    int nextValue = value != 0 ? calculateValue(sourceText, 0) : 0;
    int previousValue = currentValue != 0 ? calculateValue(targetText, 0) : 0;
    float sourceValue = calculateValue2(element.getAttribute("stroke-width"), 2.0F);
    SvgFillTrianglesService.LineCap lineCap =
        createLineCap(createText3(element, "stroke-linecap", nextText));
    SvgFillTrianglesService.LineJoin lineJoin =
        createLineJoin(createText3(element, "stroke-linejoin", previousText));
    float[] floats = value != 0 ? SvgFillTrianglesService.fillTriangles(svgCodec) : null;
    float[] currentFloats =
        currentValue != 0
            ? SvgFillTrianglesService.strokeTriangles(svgCodec, sourceValue, lineCap, lineJoin)
            : null;
    if (floats != null || currentFloats != null) {
      items.add(
          new SvgRepository.Shape(floats, currentFloats, nextValue, previousValue, sourceValue));
    }
  }

  private static String createText(
      float value, float currentValue, float nextValue, float previousValue) {
    float sourceValue = 0.5522848F * nextValue;
    float targetValue = 0.5522848F * previousValue;
    return String.format(
        Locale.US,
        "M%f %fC%f %f %f %f %f %fC%f %f %f %f %f %fC%f %f %f %f %f %fC%f %f %f %f %f %fZ",
        value,
        currentValue - previousValue,
        value + sourceValue,
        currentValue - previousValue,
        value + nextValue,
        currentValue - targetValue,
        value + nextValue,
        currentValue,
        value + nextValue,
        currentValue + targetValue,
        value + sourceValue,
        currentValue + previousValue,
        value,
        currentValue + previousValue,
        value - sourceValue,
        currentValue + previousValue,
        value - nextValue,
        currentValue + targetValue,
        value - nextValue,
        currentValue,
        value - nextValue,
        currentValue - targetValue,
        value - sourceValue,
        currentValue - previousValue,
        value,
        currentValue - previousValue);
  }

  private static String createText2(
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue) {
    sourceValue = Math.min(sourceValue, nextValue / 2.0F);
    targetValue = Math.min(targetValue, previousValue / 2.0F);
    return String.format(
        Locale.US,
        "M%f %fH%fA%f %f 0 0 1 %f %fV%fA%f %f 0 0 1 %f %fH%fA%f %f 0 0 1 %f %fV%fA%f %f 0 0 1 %f"
            + " %fZ",
        value + sourceValue,
        currentValue,
        value + nextValue - sourceValue,
        sourceValue,
        targetValue,
        value + nextValue,
        currentValue + targetValue,
        currentValue + previousValue - targetValue,
        sourceValue,
        targetValue,
        value + nextValue - sourceValue,
        currentValue + previousValue,
        value + sourceValue,
        sourceValue,
        targetValue,
        value,
        currentValue + previousValue - targetValue,
        currentValue + targetValue,
        sourceValue,
        targetValue,
        value + sourceValue,
        currentValue);
  }

  private static float[] createFloat(Element element, float[] floats) {
    String text = element.getAttribute("transform");
    return text != null && !text.isEmpty() ? multiply(floats, parseTransform(text)) : floats;
  }

  static float[] parseTransform(String text) {
    float[] floats = new float[] {1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
    int index = 0;
    int currentLength = text.length();

    while (index < currentLength) {
      while (index < currentLength && !Character.isLetter(text.charAt(index))) {
        index++;
      }

      if (index >= currentLength) {
        break;
      }

      int value = index;

      while (index < currentLength && Character.isLetter(text.charAt(index))) {
        index++;
      }

      String currentText = text.substring(value, index);

      while (index < currentLength && text.charAt(index) != '(') {
        index++;
      }

      if (index >= currentLength) {
        break;
      }

      index++;
      ArrayList arrayList = new ArrayList();
      int[] ints = new int[] {index};

      while (true) {
        if (ints[0] < currentLength && text.charAt(ints[0]) != ')') {
          while (ints[0] < currentLength) {
            char character = text.charAt(ints[0]);
            if (character != ' ' && character != ',') {
              break;
            }

            ints[0]++;
          }

          if (ints[0] >= currentLength || text.charAt(ints[0]) != ')') {
            arrayList.add(SvgCodec.readFloat(text, ints));
            continue;
          }
        }

        if (ints[0] < currentLength) {
          ints[0]++;
        }

        index = ints[0];

        float[] currentSize =
            switch (currentText) {
              case "translate" -> {
                float currentValue = calculateValue3(arrayList, 0, 0.0F);
                float nextValue = calculateValue3(arrayList, 1, 0.0F);
                yield new float[] {1.0F, 0.0F, 0.0F, 1.0F, currentValue, nextValue};
              }
              case "scale" -> {
                float previousValue = calculateValue3(arrayList, 0, 1.0F);
                float sourceValue = calculateValue3(arrayList, 1, previousValue);
                yield new float[] {previousValue, 0.0F, 0.0F, sourceValue, 0.0F, 0.0F};
              }
              case "rotate" -> {
                float targetValue = (float) Math.toRadians(calculateValue3(arrayList, 0, 0.0F));
                float inputValue = (float) Math.cos(targetValue);
                float outputValue = (float) Math.sin(targetValue);
                if (arrayList.size() >= 3) {
                  float resultValue = (Float) arrayList.get(1);
                  float candidateValue = (Float) arrayList.get(2);
                  yield new float[] {
                    inputValue,
                    outputValue,
                    -outputValue,
                    inputValue,
                    resultValue * (1.0F - inputValue) + candidateValue * outputValue,
                    candidateValue * (1.0F - inputValue) - resultValue * outputValue
                  };
                } else {
                  yield new float[] {inputValue, outputValue, -outputValue, inputValue, 0.0F, 0.0F};
                }
              }
              case "matrix" ->
                  arrayList.size() >= 6
                      ? new float[] {
                        (Float) arrayList.get(0),
                        (Float) arrayList.get(1),
                        (Float) arrayList.get(2),
                        (Float) arrayList.get(3),
                        (Float) arrayList.get(4),
                        (Float) arrayList.get(5)
                      }
                      : null;
              case "skewX" -> {
                float selectedValue =
                    (float) Math.tan(Math.toRadians(calculateValue3(arrayList, 0, 0.0F)));
                yield new float[] {1.0F, 0.0F, selectedValue, 1.0F, 0.0F, 0.0F};
              }
              case "skewY" -> {
                float defaultValue =
                    (float) Math.tan(Math.toRadians(calculateValue3(arrayList, 0, 0.0F)));
                yield new float[] {1.0F, defaultValue, 0.0F, 1.0F, 0.0F, 0.0F};
              }
              default -> null;
            };
        if (currentSize != null) {
          floats = multiply(floats, currentSize);
        }
        break;
      }
    }

    return floats;
  }

  static float[] multiply(float[] floats, float[] currentFloats) {
    return new float[] {
      floats[0] * currentFloats[0] + floats[2] * currentFloats[1],
      floats[1] * currentFloats[0] + floats[3] * currentFloats[1],
      floats[0] * currentFloats[2] + floats[2] * currentFloats[3],
      floats[1] * currentFloats[2] + floats[3] * currentFloats[3],
      floats[0] * currentFloats[4] + floats[2] * currentFloats[5] + floats[4],
      floats[1] * currentFloats[4] + floats[3] * currentFloats[5] + floats[5]
    };
  }

  private static String createText3(Element element, String text, String currentText) {
    if (element.hasAttribute(text)) {
      String nextText = element.getAttribute(text);
      if (!nextText.isEmpty()) {
        return nextText;
      }
    }

    return currentText;
  }

  private static SvgFillTrianglesService.LineCap createLineCap(String text) {
    if (text == null) {
      return SvgFillTrianglesService.LineCap.BUTT;
    }

    return switch (text) {
      case "round" -> SvgFillTrianglesService.LineCap.ROUND;
      case "square" -> SvgFillTrianglesService.LineCap.SQUARE;
      default -> SvgFillTrianglesService.LineCap.BUTT;
    };
  }

  private static SvgFillTrianglesService.LineJoin createLineJoin(String text) {
    if (text == null) {
      return SvgFillTrianglesService.LineJoin.MITER;
    }

    return switch (text) {
      case "round" -> SvgFillTrianglesService.LineJoin.ROUND;
      case "bevel" -> SvgFillTrianglesService.LineJoin.BEVEL;
      default -> SvgFillTrianglesService.LineJoin.MITER;
    };
  }

  private static int calculateValue(String text, int value) {
    if (text == null || text.isEmpty() || "currentColor".equals(text)) {
      return value;
    }

    if (text.startsWith("#")) {
      text = text.substring(1);
      if (text.length() == 3) {
        text =
            ""
                + text.charAt(0)
                + text.charAt(0)
                + text.charAt(1)
                + text.charAt(1)
                + text.charAt(2)
                + text.charAt(2);
      }

      return (int) (4278190080L | Long.parseLong(text, 16));
    } else {
      return value;
    }
  }

  private static float calculateValue2(String text, float value) {
    if (text != null && !text.isEmpty()) {
      try {
        return Float.parseFloat(text);
      } catch (NumberFormatException numberFormatException) {
        return value;
      }
    } else {
      return value;
    }
  }

  private static float calculateValue3(List<Float> items, int index, float value) {
    return index < items.size() ? (Float) items.get(index) : value;
  }

  public record Shape(
      float[] fillVerts, float[] strokeVerts, int fillColor, int strokeColor, float strokeWidth) {}
}
