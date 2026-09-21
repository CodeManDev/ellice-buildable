package dev.felix.ellice.hud.layout;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class LayoutStringService {
  private LayoutStringService() {}

  public static String string(JsonObject jsonObject, String text, String currentText) {
    if (jsonObject == null) {
      return currentText;
    }

    JsonElement jsonElement = jsonObject.get(text);
    if (jsonElement != null && !jsonElement.isJsonNull()) {
      try {
        return jsonElement.getAsString();
      } catch (Exception exception) {
        return currentText;
      }
    } else {
      return currentText;
    }
  }

  public static float number(JsonObject jsonObject, String text, float value) {
    if (jsonObject == null) {
      return value;
    }

    JsonElement jsonElement = jsonObject.get(text);
    if (jsonElement != null && !jsonElement.isJsonNull()) {
      try {
        return jsonElement.getAsFloat();
      } catch (Exception exception) {
        return value;
      }
    } else {
      return value;
    }
  }

  public static int integer(JsonObject jsonObject, String text, int value) {
    if (jsonObject == null) {
      return value;
    }

    JsonElement jsonElement = jsonObject.get(text);
    if (jsonElement != null && !jsonElement.isJsonNull()) {
      try {
        return jsonElement.getAsInt();
      } catch (Exception exception) {
        return value;
      }
    } else {
      return value;
    }
  }

  public static boolean bool(JsonObject jsonObject, String text, boolean enabled) {
    if (jsonObject == null) {
      return enabled;
    }

    JsonElement jsonElement = jsonObject.get(text);
    if (jsonElement != null && !jsonElement.isJsonNull()) {
      try {
        return jsonElement.getAsBoolean();
      } catch (Exception exception) {
        return enabled;
      }
    } else {
      return enabled;
    }
  }

  public static int color(JsonObject jsonObject, String text, int currentColor) {
    if (jsonObject == null) {
      return currentColor;
    }

    JsonElement jsonElement = jsonObject.get(text);
    if (jsonElement != null && !jsonElement.isJsonNull()) {
      try {
        if (jsonElement.getAsJsonPrimitive().isNumber()) {
          return jsonElement.getAsInt();
        }

        String currentText = jsonElement.getAsString();
        if (currentText != null && !currentText.isEmpty()) {
          if (currentText.charAt(0) == '#') {
            currentText = currentText.substring(1);
          }

          long longValue = Long.parseUnsignedLong(currentText, 16);
          if (currentText.length() <= 6) {
            longValue |= 4278190080L;
          }

          return (int) longValue;
        } else {
          return currentColor;
        }
      } catch (Exception exception) {
        return currentColor;
      }
    } else {
      return currentColor;
    }
  }

  public static int withOpacity(int value, float opacity) {
    if (opacity >= 1.0F) {
      return value;
    }

    if (opacity <= 0.0F) {
      return value & 16777215;
    }

    int currentValue = value >>> 24 & 0xFF;
    currentValue = Math.max(0, Math.min(255, Math.round(currentValue * opacity)));
    return currentValue << 24 | value & 16777215;
  }
}
