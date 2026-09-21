package dev.felix.ellice.hud.layout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.UUID;

public final class LayoutRepository {
   private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
   private static final String text = "layout.json";

   private LayoutRepository() {
   }

   public static Path dirFor(Path path, String text) {
      if (text == null || text.isBlank()) {
         text = "active";
      }

      return path.resolve("ellice-hud-layouts").resolve(text);
   }

   public static Path fileFor(Path path, String currentPath) {
      return dirFor(path, currentPath).resolve("layout.json");
   }

   public static LayoutFindSelector load(Path path) {
      if (path != null && Files.exists(path)) {
         try {
            JsonElement jsonElement = JsonParser.parseString(Files.readString(path));
            return !jsonElement.isJsonObject() ? null : fromJson(jsonElement.getAsJsonObject());
         } catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.warn("Failed to load HUD layout from {}", path, exception);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void save(Path path, LayoutFindSelector layoutFindSelector) {
      if (layoutFindSelector != null) {
         try {
            Files.createDirectories(path.getParent());
            Path currentPath = path.resolveSibling(path.getFileName() + ".tmp");
            Files.writeString(currentPath, gson2.toJson(toJson(layoutFindSelector)), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(currentPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.warn("Failed to save HUD layout to {}", path, exception);
         }
      }
   }

   public static JsonObject toJson(LayoutFindSelector layoutFindSelector) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("version", layoutFindSelector.version);
      if (layoutFindSelector.name != null) {
         jsonObject.addProperty("name", layoutFindSelector.name);
      }

      if (layoutFindSelector.author != null) {
         jsonObject.addProperty("author", layoutFindSelector.author);
      }

      if (layoutFindSelector.description != null) {
         jsonObject.addProperty("description", layoutFindSelector.description);
      }

      JsonArray jsonArray = new JsonArray();

      for (LayoutIsContainerService layoutIsContainer : layoutFindSelector.elements) {
         jsonArray.add(createJsonObject2(layoutIsContainer));
      }

      jsonObject.add("elements", jsonArray);
      return jsonObject;
   }

   public static LayoutFindSelector fromJson(JsonObject jsonObject) {
      if (jsonObject == null) {
         return null;
      }

      jsonObject = createJsonObject(jsonObject);
      LayoutFindSelector layoutFindSelector = new LayoutFindSelector();
      if (jsonObject.has("version")) {
         layoutFindSelector.version = jsonObject.get("version").getAsInt();
      }

      if (jsonObject.has("name")) {
         layoutFindSelector.name = jsonObject.get("name").getAsString();
      }

      if (jsonObject.has("author")) {
         layoutFindSelector.author = jsonObject.get("author").getAsString();
      }

      if (jsonObject.has("description")) {
         layoutFindSelector.description = jsonObject.get("description").getAsString();
      }

      JsonElement jsonElement = jsonObject.get("elements");
      if (jsonElement != null && jsonElement.isJsonArray()) {
         for (JsonElement currentJsonElement : jsonElement.getAsJsonArray()) {
            if (currentJsonElement.isJsonObject()) {
               LayoutIsContainerService layoutIsContainer = createLayoutIsContainerService(currentJsonElement.getAsJsonObject());
               if (layoutIsContainer != null) {
                  layoutFindSelector.elements.add(layoutIsContainer);
               }
            }
         }
      }

      return layoutFindSelector;
   }

   private static JsonObject createJsonObject(JsonObject jsonObject) {
      if (!jsonObject.has("version")) {
         jsonObject.addProperty("version", 1);
      }

      return jsonObject;
   }

   private static JsonObject createJsonObject2(LayoutIsContainerService layoutIsContainer) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("id", layoutIsContainer.id != null ? layoutIsContainer.id : "");
      jsonObject.addProperty("type", layoutIsContainer.type != null ? layoutIsContainer.type : "rect");
      jsonObject.addProperty("anchorH", layoutIsContainer.anchor.h.name().toLowerCase());
      jsonObject.addProperty("anchorV", layoutIsContainer.anchor.v.name().toLowerCase());
      jsonObject.addProperty("offsetX", layoutIsContainer.offsetX);
      jsonObject.addProperty("offsetY", layoutIsContainer.offsetY);
      jsonObject.addProperty("width", layoutIsContainer.width);
      jsonObject.addProperty("height", layoutIsContainer.height);
      if (layoutIsContainer.rotation != 0.0F) {
         jsonObject.addProperty("rotation", layoutIsContainer.rotation);
      }

      if (layoutIsContainer.opacity != 1.0F) {
         jsonObject.addProperty("opacity", layoutIsContainer.opacity);
      }

      if (!layoutIsContainer.visible) {
         jsonObject.addProperty("visible", false);
      }

      if (layoutIsContainer.locked) {
         jsonObject.addProperty("locked", true);
      }

      if (layoutIsContainer.props != null && layoutIsContainer.props.size() > 0) {
         jsonObject.add("props", layoutIsContainer.props.deepCopy());
      }

      if (layoutIsContainer.children != null && !layoutIsContainer.children.isEmpty()) {
         JsonArray jsonArray = new JsonArray();

         for (LayoutIsContainerService currentLayoutIsContainer : layoutIsContainer.children) {
            jsonArray.add(createJsonObject2(currentLayoutIsContainer));
         }

         jsonObject.add("children", jsonArray);
      }

      return jsonObject;
   }

   private static LayoutIsContainerService createLayoutIsContainerService(JsonObject jsonObject) {
      if (jsonObject == null) {
         return null;
      }

      LayoutIsContainerService layoutIsContainer = new LayoutIsContainerService();
      layoutIsContainer.id = jsonObject.has("id") ? jsonObject.get("id").getAsString() : UUID.randomUUID().toString();
      layoutIsContainer.type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "rect";
      layoutIsContainer.anchor = LayoutCodec.parse(
         jsonObject.has("anchorH") ? jsonObject.get("anchorH").getAsString() : null, jsonObject.has("anchorV") ? jsonObject.get("anchorV").getAsString() : null
      );
      if (jsonObject.has("offsetX")) {
         layoutIsContainer.offsetX = jsonObject.get("offsetX").getAsFloat();
      }

      if (jsonObject.has("offsetY")) {
         layoutIsContainer.offsetY = jsonObject.get("offsetY").getAsFloat();
      }

      if (jsonObject.has("width")) {
         layoutIsContainer.width = jsonObject.get("width").getAsFloat();
      }

      if (jsonObject.has("height")) {
         layoutIsContainer.height = jsonObject.get("height").getAsFloat();
      }

      if (jsonObject.has("rotation")) {
         layoutIsContainer.rotation = jsonObject.get("rotation").getAsFloat();
      }

      if (jsonObject.has("opacity")) {
         layoutIsContainer.opacity = jsonObject.get("opacity").getAsFloat();
      }

      if (jsonObject.has("visible")) {
         layoutIsContainer.visible = jsonObject.get("visible").getAsBoolean();
      }

      if (jsonObject.has("locked")) {
         layoutIsContainer.locked = jsonObject.get("locked").getAsBoolean();
      }

      if (jsonObject.has("props") && jsonObject.get("props").isJsonObject()) {
         layoutIsContainer.props = jsonObject.getAsJsonObject("props").deepCopy();
      }

      if (jsonObject.has("children") && jsonObject.get("children").isJsonArray()) {
         layoutIsContainer.children = new ArrayList<>();

         for (JsonElement jsonElement : jsonObject.getAsJsonArray("children")) {
            if (jsonElement.isJsonObject()) {
               LayoutIsContainerService currentLayoutIsContainer = createLayoutIsContainerService(jsonElement.getAsJsonObject());
               if (currentLayoutIsContainer != null) {
                  layoutIsContainer.children.add(currentLayoutIsContainer);
               }
            }
         }
      }

      return layoutIsContainer;
   }
}
