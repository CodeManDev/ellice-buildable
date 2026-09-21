package dev.felix.ellice.changelog;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ChangelogRepository {
   private static final String text2 = "/assets/ellice/changelog.json";
   private final List<ChangelogRelease> items;

   public ChangelogRepository(List<ChangelogRelease> currentItems) {
      this.items = currentItems == null ? List.of() : List.copyOf(currentItems);
   }

   public List<ChangelogRelease> releases() {
      return this.items;
   }

   public ChangelogRelease latest() {
      return this.items.isEmpty() ? null : this.items.getFirst();
   }

   public ChangelogRelease forVersion(String text) {
      if (text == null) {
         return null;
      }

      for (ChangelogRelease changelogRelease : this.items) {
         if (text.equals(changelogRelease.version())) {
            return changelogRelease;
         }
      }

      return null;
   }

   public static ChangelogRepository load() {
      try (InputStream inputStream = ChangelogRepository.class.getResourceAsStream("/assets/ellice/changelog.json")) {
         if (inputStream == null) {
            CoreIsInitializedHandler.LOGGER.warn("Bundled changelog is missing: {}", "/assets/ellice/changelog.json");
            return new ChangelogRepository(List.of());
         }

         JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         JsonArray jsonArray;
         if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
         } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonArray = jsonObject.has("releases") ? jsonObject.getAsJsonArray("releases") : new JsonArray();
         }

         ArrayList currentSize = new ArrayList(jsonArray.size());

         for (JsonElement currentJsonElement : jsonArray) {
            if (currentJsonElement.isJsonObject()) {
               currentSize.add(createChangelogData2(currentJsonElement.getAsJsonObject()));
            }
         }

         return new ChangelogRepository(currentSize);
      } catch (Exception exception) {
         CoreIsInitializedHandler.LOGGER.warn("Could not load bundled changelog", exception);
         return new ChangelogRepository(List.of());
      }
   }

   private static ChangelogRelease createChangelogData2(JsonObject jsonObject) {
      ArrayList arrayList = new ArrayList();
      if (jsonObject.has("highlights") && jsonObject.get("highlights").isJsonArray()) {
         for (JsonElement jsonElement : jsonObject.getAsJsonArray("highlights")) {
            if (jsonElement.isJsonPrimitive()) {
               arrayList.add(jsonElement.getAsString());
            }
         }
      }

      ArrayList currentArrayList = new ArrayList();
      if (jsonObject.has("entries") && jsonObject.get("entries").isJsonArray()) {
         for (JsonElement currentJsonElement : jsonObject.getAsJsonArray("entries")) {
            if (currentJsonElement.isJsonPrimitive()) {
               currentArrayList.add(new ChangelogData(ChangelogFeatureType.CHANGED, currentJsonElement.getAsString()));
            } else if (currentJsonElement.isJsonObject()) {
               JsonObject currentJsonObject = currentJsonElement.getAsJsonObject();
               currentArrayList.add(new ChangelogData(ChangelogFeatureType.from(createText(currentJsonObject, "type")), createText(currentJsonObject, "text")));
            }
         }
      }

      return new ChangelogRelease(createText(jsonObject, "version"), createText(jsonObject, "date"), createText(jsonObject, "title"), arrayList, currentArrayList);
   }

   private static String createText(JsonObject jsonObject, String text) {
      try {
         return jsonObject.has(text) && !jsonObject.get(text).isJsonNull() ? jsonObject.get(text).getAsString() : "";
      } catch (Exception exception) {
         return "";
      }
   }
}
