package dev.felix.ellice.diagnostics.fatal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class FatalReportLayout {
  private static final int BACKGROUND_COLOR = -15987180;
  private static final int PANEL_BORDER_COLOR = -15460576;
  private static final int DIVIDER_COLOR = -14078662;
  private static final int PRIMARY_TEXT_COLOR = -855048;
  private static final int SECONDARY_TEXT_COLOR = -6511432;
  private static final int ACCENT_TEXT_COLOR = -25464;
  private final FatalScreenCanvas canvas;
  private final int uiScale;

  private FatalReportLayout(int width, int height) {
    this.canvas = new FatalScreenCanvas(width, height);
    this.uiScale = Math.max(1, Math.min(4, Math.min(width / 760, height / 440)));
  }

  static FatalReportLayout.Frame paint(
      FatalCaptureService fatalCapture,
      int value,
      int currentValue,
      int nextValue,
      boolean enabled) {
    return new FatalReportLayout(value, currentValue).createFrame(fatalCapture, nextValue, enabled);
  }

  private FatalReportLayout.Frame createFrame(
      FatalCaptureService fatalCapture, int value, boolean enabled) {
    int currentValue = this.canvas.w / this.uiScale;
    int nextValue = this.canvas.h / this.uiScale;
    this.canvas.clear(BACKGROUND_COLOR);

    for (int index = 0; index < this.canvas.h; index++) {
      for (int currentIndex = 0; currentIndex < this.canvas.w; currentIndex++) {
        double doubleValue =
            Math.hypot(
                ((double) currentIndex / this.uiScale - currentValue * 0.75)
                    / Math.max(1, currentValue),
                (double) index / this.uiScale / Math.max(1, nextValue));
        int previousValue = (int) (Math.max(0.0, 1.0 - doubleValue) * 36.0);
        this.canvas.px[index * this.canvas.w + currentIndex] =
            FatalComponent.blend(BACKGROUND_COLOR, -9084780, previousValue);
      }
    }

    int sourceValue = currentValue < 600 ? 16 : 32;
    int targetValue = Math.min(1040, currentValue - sourceValue * 2);
    int inputValue = (currentValue - targetValue) / 2;
    int outputValue = nextValue < 400 ? 1 : 0;
    int resultValue = outputValue != 0 ? 12 : Math.max(18, (nextValue - 570) / 2);
    int candidateValue = nextValue - 24;
    this.drawText(inputValue, resultValue, 14, PRIMARY_TEXT_COLOR, "ellice");
    String text = "REPORT / " + fatalCapture.id().toUpperCase(Locale.ROOT);
    this.drawText(
        inputValue + targetValue - this.measureTextWidth(text, 12),
        resultValue + 2,
        12,
        SECONDARY_TEXT_COLOR,
        text);
    this.drawDivider(inputValue, resultValue + 32, targetValue, DIVIDER_COLOR);
    int selectedValue = resultValue + (outputValue != 0 ? 38 : 50);
    int defaultValue = this.measureTextWidth(fatalCapture.title(), 28) <= targetValue ? 28 : 14;
    this.drawText(
        inputValue, selectedValue, defaultValue, PRIMARY_TEXT_COLOR, fatalCapture.title());
    this.drawWrappedText(
        inputValue,
        selectedValue + (outputValue != 0 ? 38 : 43),
        targetValue,
        14,
        21,
        SECONDARY_TEXT_COLOR,
        fatalCapture.guidance(),
        outputValue != 0 ? 1 : 2);
    ArrayList arrayList = new ArrayList();
    if (fatalCapture.allowsContinue()) {
      arrayList.add(0);
    }

    arrayList.add(1);
    if (fatalCapture.dumpFile() != null) {
      arrayList.add(2);
    }

    arrayList.add(3);
    int currentSize = targetValue >= 570 ? arrayList.size() : Math.min(2, arrayList.size());
    int nextSize = (arrayList.size() + currentSize - 1) / currentSize;
    int initialValue = candidateValue - 24 - nextSize * 42 - (nextSize - 1) * 8;
    int resolvedValue = selectedValue + (outputValue != 0 ? 68 : 90);
    int computedValue = initialValue - resolvedValue - (outputValue != 0 ? 12 : 20);
    int cachedValue = targetValue >= 700 && computedValue >= 176 ? 1 : 0;
    int pendingValue = cachedValue != 0 ? 190 : 0;
    int activeValue = targetValue - (cachedValue != 0 ? pendingValue + 16 : 0);
    if (computedValue >= 28) {
      this.drawPanel(inputValue, resolvedValue, activeValue, computedValue);
      int fallbackValue = inputValue + 20;
      int primaryValue = activeValue - 40;
      if (computedValue >= 76) {
        this.drawText(
            fallbackValue,
            resolvedValue + 15,
            12,
            SECONDARY_TEXT_COLOR,
            fatalCapture.kind() == FatalCaptureService.Kind.LICENSE
                ? "WHAT TO DO"
                : "WHAT HAPPENED");
      }

      int secondaryValue = resolvedValue + (computedValue >= 76 ? 38 : 6);
      int tertiaryValue = Math.max(1, Math.min(4, (computedValue - 65) / 21));
      int temporaryValue =
          this.drawWrappedText(
              fallbackValue,
              secondaryValue,
              primaryValue,
              14,
              21,
              PRIMARY_TEXT_COLOR,
              fatalCapture.message(),
              tertiaryValue);
      if (temporaryValue + 34 < resolvedValue + computedValue) {
        this.drawText(
            fallbackValue,
            temporaryValue + 8,
            12,
            ACCENT_TEXT_COLOR,
            this.truncateText(fatalCapture.exceptionType(), primaryValue, 12));
      }

      int requestedValue = temporaryValue + 44;
      if (requestedValue + 37 < resolvedValue + computedValue) {
        this.drawDivider(fallbackValue, requestedValue - 8, primaryValue, DIVIDER_COLOR);
        String[] strings = fatalCapture.screenLines();

        for (int nextIndex = 3;
            nextIndex < strings.length && requestedValue + 50 < resolvedValue + computedValue;
            nextIndex++) {
          this.drawText(
              fallbackValue,
              requestedValue,
              12,
              SECONDARY_TEXT_COLOR,
              this.truncateText(strings[nextIndex], primaryValue, 12));
          requestedValue += 18;
        }

        this.drawText(
            fallbackValue,
            resolvedValue + computedValue - 27,
            12,
            SECONDARY_TEXT_COLOR,
            this.truncateText("Full details are included in the report.", primaryValue, 12));
      }
    }

    if (cachedValue != 0) {
      int actualValue = inputValue + targetValue - pendingValue;
      this.drawPanel(actualValue, resolvedValue, pendingValue, computedValue);
      this.drawText(
          actualValue + 20, resolvedValue + 15, 12, SECONDARY_TEXT_COLOR, "TAKE THE REPORT");
      int expectedValue = Math.min(128, computedValue - 64);

      try {
        boolean[][] booleans = FatalCodec.encode(fatalCapture.qrPayload());
        this.canvas.qr(
            booleans,
            (actualValue + (pendingValue - expectedValue) / 2) * this.uiScale,
            (resolvedValue + 33) * this.uiScale,
            expectedValue * this.uiScale);
      } catch (Throwable exception) {
        this.drawText(
            actualValue + 20, resolvedValue + 60, 14, SECONDARY_TEXT_COLOR, "Use Copy report");
      }

      String currentText = "Scan to save details";
      this.drawText(
          actualValue + (pendingValue - this.measureTextWidth(currentText, 12)) / 2,
          resolvedValue + 39 + expectedValue,
          12,
          SECONDARY_TEXT_COLOR,
          currentText);
    }

    ArrayList currentArrayList = new ArrayList();
    int minimumValue = (targetValue - (currentSize - 1) * 8) / currentSize;

    for (int previousIndex = 0; previousIndex < arrayList.size(); previousIndex++) {
      int maximumValue = (Integer) arrayList.get(previousIndex);
      int startValue = inputValue + previousIndex % currentSize * (minimumValue + 8);
      int endValue = initialValue + previousIndex / currentSize * 50;
      int localValue = previousIndex == 0 ? 1 : 0;
      int storedValue =
          localValue != 0
              ? (value == maximumValue ? -1 : -1578253)
              : (value == maximumValue ? -13617595 : -14802386);
      this.drawRoundedRect(startValue, endValue, minimumValue, 42, 8, storedValue);

      String nextText =
          switch (maximumValue) {
            case 0 -> "Continue without ellice";
            case 1 -> enabled ? "Report copied" : "Copy report";
            case 2 -> "Open report folder";
            default -> "Quit game";
          };
      int createdValue = this.measureTextWidth(nextText, 14) <= minimumValue - 18 ? 14 : 12;
      nextText = this.truncateText(nextText, minimumValue - 18, createdValue);
      this.drawText(
          startValue + (minimumValue - this.measureTextWidth(nextText, createdValue)) / 2,
          endValue + (createdValue == 14 ? 12 : 13),
          createdValue,
          localValue != 0 ? -15394783 : PRIMARY_TEXT_COLOR,
          nextText);
      currentArrayList.add(
          new FatalReportLayout.Action(
              maximumValue,
              startValue * this.uiScale,
              endValue * this.uiScale,
              minimumValue * this.uiScale,
              42 * this.uiScale));
    }

    String previousText =
        (fatalCapture.allowsContinue() ? "Esc  Continue     " : "")
            + "C  Copy report     "
            + (fatalCapture.dumpFile() != null ? "O  Open folder     " : "")
            + "Q  Quit";
    this.drawText(
        inputValue,
        candidateValue - 9,
        12,
        SECONDARY_TEXT_COLOR,
        this.truncateText(previousText, targetValue, 12));
    return new FatalReportLayout.Frame(this.canvas, List.copyOf(currentArrayList));
  }

  private void drawPanel(int value, int currentValue, int nextValue, int previousValue) {
    this.drawRoundedRect(value, currentValue, nextValue, previousValue, 12, DIVIDER_COLOR);
    this.drawRoundedRect(
        value + 1, currentValue + 1, nextValue - 2, previousValue - 2, 11, PANEL_BORDER_COLOR);
  }

  private void drawDivider(int value, int currentValue, int nextValue, int previousValue) {
    this.canvas.fill(
        value * this.uiScale,
        currentValue * this.uiScale,
        nextValue * this.uiScale,
        this.uiScale,
        previousValue);
  }

  private void drawRoundedRect(
      int value,
      int currentValue,
      int nextValue,
      int previousValue,
      int sourceValue,
      int targetValue) {
    int inputValue =
        Math.max(0, Math.min(sourceValue, Math.min(nextValue, previousValue) / 2)) * this.uiScale;
    int outputValue = nextValue * this.uiScale;
    int resultValue = previousValue * this.uiScale;

    for (int index = 0; index < resultValue; index++) {
      int candidateValue =
          index < inputValue
              ? inputValue - index - 1
              : (index >= resultValue - inputValue ? index - (resultValue - inputValue) : 0);
      int selectedValue =
          candidateValue == 0
              ? 0
              : inputValue
                  - (int)
                      Math.sqrt(
                          (double) inputValue * inputValue
                              - (double) candidateValue * candidateValue);
      this.canvas.fill(
          value * this.uiScale + selectedValue,
          currentValue * this.uiScale + index,
          outputValue - selectedValue * 2,
          1,
          targetValue);
    }
  }

  private int measureTextWidth(String text, int value) {
    return FatalComponent.available(value * 10 + this.uiScale)
        ? (FatalComponent.width(text, value * 10 + this.uiScale) + this.uiScale - 1) / this.uiScale
        : this.canvas.textWidth(text, Math.max(1, value / 9));
  }

  private void drawText(
      int value, int currentValue, int nextValue, int previousValue, String currentText) {
    if (FatalComponent.available(nextValue * 10 + this.uiScale)) {
      FatalComponent.draw(
          this.canvas,
          value * this.uiScale,
          currentValue * this.uiScale,
          currentText,
          nextValue * 10 + this.uiScale,
          previousValue);
    } else {
      this.canvas.text(
          value * this.uiScale,
          (currentValue + 3) * this.uiScale,
          Math.max(1, nextValue / 9) * this.uiScale,
          previousValue,
          currentText);
    }
  }

  private String truncateText(String text, int value, int currentValue) {
    if (this.measureTextWidth(text, currentValue) <= value) {
      return text;
    }

    int currentLength = text.length();

    while (currentLength > 0
        && this.measureTextWidth(text.substring(0, currentLength) + "...", currentValue) > value) {
      currentLength += -1;
    }

    return currentLength == 0 ? "" : text.substring(0, currentLength).stripTrailing() + "...";
  }

  private int drawWrappedText(
      int value,
      int currentValue,
      int nextValue,
      int previousValue,
      int sourceValue,
      int targetValue,
      String text,
      int inputValue) {
    String currentText = text.replaceAll("\\s+", " ").trim();

    for (int index = 0; index < inputValue && !currentText.isEmpty(); index++) {
      if (index == inputValue - 1
          || this.measureTextWidth(currentText, previousValue) <= nextValue) {
        this.drawText(
            value,
            currentValue,
            previousValue,
            targetValue,
            this.truncateText(currentText, nextValue, previousValue));
        return currentValue + sourceValue;
      }

      int currentLength = currentText.length();

      while (currentLength > 1
          && this.measureTextWidth(currentText.substring(0, currentLength), previousValue)
              > nextValue) {
        currentLength += -1;
      }

      int outputValue = currentText.lastIndexOf(32, currentLength);
      if (outputValue > 0) {
        currentLength = outputValue;
      }

      this.drawText(
          value, currentValue, previousValue, targetValue, currentText.substring(0, currentLength));
      currentText = currentText.substring(currentLength).trim();
      currentValue += sourceValue;
    }

    return currentValue;
  }

  record Action(int id, int x, int y, int w, int h) {}

  record Frame(FatalScreenCanvas canvas, List<FatalReportLayout.Action> actions) {}
}
