package dev.felix.ellice.feature.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class LicenseExpiryMsService {
  private LicenseExpiryMsService() {}

  public static long expiryMs(String text) {
    try {
      String[] strings = text.split("\\.");
      if (strings.length < 2) {
        return -1L;
      }

      String currentText =
          new String(Base64.getUrlDecoder().decode(createText(strings[1])), StandardCharsets.UTF_8);
      JsonObject jsonObject = JsonParser.parseString(currentText).getAsJsonObject();
      return !jsonObject.has("exp") ? -1L : jsonObject.get("exp").getAsLong() * 1000L;
    } catch (Exception exception) {
      return -1L;
    }
  }

  public static String subject(String text) {
    try {
      String[] strings = text.split("\\.");
      if (strings.length < 2) {
        return "";
      }

      String currentText =
          new String(Base64.getUrlDecoder().decode(createText(strings[1])), StandardCharsets.UTF_8);
      JsonObject jsonObject = JsonParser.parseString(currentText).getAsJsonObject();
      return jsonObject.has("sub") ? jsonObject.get("sub").getAsString() : "";
    } catch (Exception exception) {
      return "";
    }
  }

  public static boolean expired(String text, long longValue, long currentLongValue) {
    long nextLongValue = expiryMs(text);
    return nextLongValue < 0L || longValue + currentLongValue >= nextLongValue;
  }

  private static String createText(String text) {
    int currentLength = (4 - text.length() % 4) % 4;
    return text + "=".repeat(currentLength);
  }
}
