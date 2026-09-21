package dev.felix.ellice.feature.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CapeSearchSelector {
   private CapeSearchSelector() {
   }

   public static CapeSearchSelector.Page search(CapeSearchSelector.Provider value, String text, int currentValue, String currentText) throws IOException {
      String nextText;
      if (value == CapeSearchSelector.Provider.GIPHY) {
         updateState(currentText);
         String previousText = text.isBlank() ? "trending" : "search";
         nextText = "https://api.giphy.com/v1/gifs/"
            + previousText
            + "?api_key="
            + createText2(currentText)
            + "&limit=12&offset="
            + currentValue
            + "&rating=pg-13&lang=en";
         if (!text.isBlank()) {
            nextText = nextText + "&q=" + createText2(text);
         }
      } else {
         nextText = "https://commons.wikimedia.org/w/api.php?action=query&format=json&formatversion=2&generator=search&gsrnamespace=6&gsrlimit=12&gsroffset="
            + currentValue
            + "&gsrsearch="
            + createText2((text.isBlank() ? "animation" : text) + " filemime:image/gif")
            + "&prop=imageinfo&iiprop=url%7Cextmetadata&iiurlwidth=180&iiextmetadatafilter=Artist%7CLicenseShortName";
      }

      JsonObject jsonObject = createJsonObject(CapeHttpsService.get(URI.create(nextText), 2097152));
      return parse(value, jsonObject, currentValue);
   }

   public static CapeSearchSelector.Entry giphy(String text, String currentText) throws IOException {
      updateState(currentText);
      if (!text.matches("[A-Za-z0-9]{1,100}")) {
         throw new IOException("Invalid GIPHY selection.");
      }

      JsonObject jsonObject = createJsonObject(
         CapeHttpsService.get(URI.create("https://api.giphy.com/v1/gifs/" + text + "?api_key=" + createText2(currentText)), 524288)
      );

      try {
         return createEntry(jsonObject.getAsJsonObject("data"));
      } catch (RuntimeException exception) {
         throw new IOException("This GIPHY item is unavailable.");
      }
   }

   static CapeSearchSelector.Page parse(CapeSearchSelector.Provider value, JsonObject jsonObject, int currentValue) throws IOException {
      try {
         ArrayList arrayList = new ArrayList();
         int nextValue = -1;
         if (jsonObject.has("error")) {
            throw new IOException("The search service could not complete this request.");
         }

         if (value == CapeSearchSelector.Provider.GIPHY) {
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("data")) {
               arrayList.add(createEntry(jsonElement.getAsJsonObject()));
            }

            JsonObject currentJsonObject = jsonObject.getAsJsonObject("pagination");
            if (currentJsonObject != null && currentValue + arrayList.size() < currentJsonObject.get("total_count").getAsInt()) {
               nextValue = currentValue + arrayList.size();
            }
         } else if (jsonObject.has("query")) {
            ArrayList<JsonObject> currentArrayList = new ArrayList<>();

            for (JsonElement currentJsonElement : jsonObject.getAsJsonObject("query").getAsJsonArray("pages")) {
               currentArrayList.add(currentJsonElement.getAsJsonObject());
            }

            currentArrayList.sort(Comparator.comparingInt(item -> item.has("index") ? item.get("index").getAsInt() : 0));

            for (JsonObject nextJsonObject : (Iterable<JsonObject>) (Iterable<?>) (currentArrayList)) {
               if (nextJsonObject.has("imageinfo")) {
                  JsonObject previousJsonObject = nextJsonObject.getAsJsonArray("imageinfo").get(0).getAsJsonObject();
                  JsonObject sourceJsonObject = previousJsonObject.getAsJsonObject("extmetadata");
                  String text = str(nextJsonObject, "title").replaceFirst("^File:", "").replaceFirst("(?i)\\.gif$", "");
                  String currentText = createText(sourceJsonObject, "Artist") + " · " + createText(sourceJsonObject, "LicenseShortName");
                  arrayList.add(
                     new CapeSearchSelector.Entry(
                        str(nextJsonObject, "pageid"),
                        text,
                        str(previousJsonObject, "url"),
                        previousJsonObject.has("thumburl") ? str(previousJsonObject, "thumburl") : str(previousJsonObject, "url"),
                        str(previousJsonObject, "descriptionurl"),
                        currentText,
                        value
                     )
                  );
               }
            }

            if (jsonObject.has("continue")) {
               nextValue = jsonObject.getAsJsonObject("continue").get("gsroffset").getAsInt();
            }
         }

         return new CapeSearchSelector.Page(List.copyOf(arrayList), nextValue);
      } catch (RuntimeException exception) {
         throw new IOException("Unexpected response from the GIF service.", exception);
      }
   }

   private static CapeSearchSelector.Entry createEntry(JsonObject jsonObject) {
      JsonObject currentJsonObject = jsonObject.getAsJsonObject("images");
      JsonObject nextJsonObject = currentJsonObject.has("downsized") ? currentJsonObject.getAsJsonObject("downsized") : currentJsonObject.getAsJsonObject("original");
      JsonObject previousJsonObject = currentJsonObject.has("fixed_width_still") ? currentJsonObject.getAsJsonObject("fixed_width_still") : nextJsonObject;
      String text = jsonObject.has("username") && !str(jsonObject, "username").isBlank() ? "@" + str(jsonObject, "username") : "GIPHY";
      return new CapeSearchSelector.Entry(
         str(jsonObject, "id"), str(jsonObject, "title"), str(nextJsonObject, "url"), str(previousJsonObject, "url"), str(jsonObject, "url"), text, CapeSearchSelector.Provider.GIPHY
      );
   }

   private static String createText(JsonObject jsonObject, String text) {
      return jsonObject != null && jsonObject.has(text) ? str(jsonObject.getAsJsonObject(text), "value").replaceAll("<[^>]*>", "").replace("&amp;", "&") : "";
   }

   static String str(JsonObject jsonObject, String text) {
      return jsonObject != null && jsonObject.has(text) && !jsonObject.get(text).isJsonNull() ? jsonObject.get(text).getAsString() : "";
   }

   private static String createText2(String text) {
      return URLEncoder.encode(text, StandardCharsets.UTF_8);
   }

   private static void updateState(String text) throws IOException {
      if (text == null || text.isBlank()) {
         throw new IOException("Add your GIPHY API key in Connections, or use Commons without a key.");
      }
   }

   private static JsonObject createJsonObject(byte[] bytes) throws IOException {
      try {
         return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
      } catch (RuntimeException exception) {
         throw new IOException("The service returned an invalid response.", exception);
      }
   }

   public record Entry(String id, String title, String media, String thumbnail, String page, String credit, CapeSearchSelector.Provider provider) {
   }

   public record Page(List<CapeSearchSelector.Entry> entries, int nextOffset) {
   }

   public enum Provider {
      COMMONS,
      GIPHY;


      private static CapeSearchSelector.Provider[] $values() {
         return new CapeSearchSelector.Provider[]{COMMONS, GIPHY};
      }
   }
}
