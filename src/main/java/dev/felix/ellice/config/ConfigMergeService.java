package dev.felix.ellice.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;

public final class ConfigMergeService {
  private ConfigMergeService() {}

  public static void merge(JsonObject jsonObject) {
    if (!jsonObject.has("ESP") && (jsonObject.has("2D ESP") || jsonObject.has("Player Fire ESP"))) {
      JsonObject currentJsonObject = createJsonObject(jsonObject, "2D ESP");
      JsonObject nextJsonObject = createJsonObject(jsonObject, "Player Fire ESP");
      boolean currentEnabled = checkCondition(currentJsonObject);
      boolean nextEnabled = checkCondition(nextJsonObject);
      JsonObject previousJsonObject = new JsonObject();
      JsonObject sourceJsonObject = new JsonObject();
      JsonObject targetJsonObject = createJsonObject(nextJsonObject, "settings");
      targetJsonObject
          .entrySet()
          .forEach(
              entry ->
                  sourceJsonObject.add(
                      (String) entry.getKey(), ((JsonElement) entry.getValue()).deepCopy()));
      Set values = Set.of("Range", "Max Targets", "Include Self");
      createJsonObject(currentJsonObject, "settings")
          .entrySet()
          .forEach(
              entry -> {
                String text =
                    switch ((String) entry.getKey()) {
                      case "Self in Third Person" -> "Include Self";
                      case "Max Boxes" -> "Max Targets";
                      case "Through Walls" -> "Boxes Through Walls";
                      default -> (String) entry.getKey();
                    };
                if (!nextEnabled || !values.contains(text)) {
                  sourceJsonObject.add(text, ((JsonElement) entry.getValue()).deepCopy());
                }
              });
      sourceJsonObject.addProperty("2D Boxes", currentEnabled);
      if (!nextEnabled && currentEnabled || !jsonObject.has("Player Fire ESP")) {
        for (String text : new String[] {"Fire", "Outline", "Kawase Bloom", "Interference"}) {
          sourceJsonObject.addProperty(text, false);
        }
      }

      if (nextEnabled) {
        sourceJsonObject.addProperty("Players", true);
      }

      previousJsonObject.add("settings", sourceJsonObject);
      previousJsonObject.addProperty("enabled", currentEnabled || nextEnabled);
      jsonObject.add("ESP", previousJsonObject);
    }
  }

  private static JsonObject createJsonObject(JsonObject jsonObject, String text) {
    return jsonObject.has(text) && jsonObject.get(text).isJsonObject()
        ? jsonObject.getAsJsonObject(text)
        : new JsonObject();
  }

  private static boolean checkCondition(JsonObject jsonObject) {
    return jsonObject.has("enabled") && jsonObject.get("enabled").getAsBoolean();
  }
}
