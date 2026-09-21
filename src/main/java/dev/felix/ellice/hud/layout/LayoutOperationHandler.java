package dev.felix.ellice.hud.layout;

public interface LayoutOperationHandler {
  String string(String text);

  float number(String text);

  float max(String text);

  default String interpolate(String text) {
    if (text != null && !text.isEmpty() && text.indexOf(123) >= 0) {
      StringBuilder currentLength = new StringBuilder(text.length());
      int index = 0;

      while (index < text.length()) {
        int value = text.indexOf(123, index);
        if (value < 0) {
          currentLength.append(text, index, text.length());
          break;
        }

        currentLength.append(text, index, value);
        int currentValue = text.indexOf(125, value + 1);
        if (currentValue < 0) {
          currentLength.append(text, value, text.length());
          break;
        }

        String currentText = text.substring(value + 1, currentValue);
        String nextText = this.string(currentText);
        if (nextText != null && !nextText.isEmpty()) {
          currentLength.append(nextText);
        } else {
          currentLength.append('{').append(currentText).append('}');
        }

        index = currentValue + 1;
      }

      return currentLength.toString();
    } else {
      return text == null ? "" : text;
    }
  }
}
