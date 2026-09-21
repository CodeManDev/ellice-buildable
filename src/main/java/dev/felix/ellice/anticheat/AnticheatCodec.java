package dev.felix.ellice.anticheat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AnticheatCodec {
  public static final String WEBSITE = "https://maninmyvan.github.io/Minecraft-Anticheat-List/";
  public static final String DATA =
      "https://maninmyvan.github.io/Minecraft-Anticheat-List/anticheats.json";

  private AnticheatCodec() {}

  public static List<AnticheatCodec.Entry> parse(String text) {
    JsonArray jsonArray = JsonParser.parseString(text).getAsJsonArray();
    if (!jsonArray.isEmpty() && jsonArray.size() <= 2000) {
      ArrayList arrayList = new ArrayList();
      HashSet hashSet = new HashSet();

      for (JsonElement jsonElement : jsonArray) {
        if (jsonElement.isJsonObject()) {
          JsonObject jsonObject = jsonElement.getAsJsonObject();
          String currentText = string(jsonObject, "name", "");
          if (!currentText.isBlank() && hashSet.add(currentText.toLowerCase(Locale.ROOT))) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            JsonElement currentJsonElement = jsonObject.get("platform");
            if (currentJsonElement != null && currentJsonElement.isJsonArray()) {
              for (JsonElement nextJsonElement : currentJsonElement.getAsJsonArray()) {
                if (nextJsonElement.isJsonPrimitive()) {
                  linkedHashSet.add(createText(nextJsonElement.getAsString()));
                }
              }
            } else if (currentJsonElement != null && currentJsonElement.isJsonPrimitive()) {
              linkedHashSet.add(createText(currentJsonElement.getAsString()));
            }

            linkedHashSet.remove("");
            if (linkedHashSet.isEmpty()) {
              linkedHashSet.add("Unknown");
            }

            ArrayList currentArrayList = new ArrayList();
            if (jsonObject.has("links") && jsonObject.get("links").isJsonArray()) {
              for (JsonElement previousJsonElement : jsonObject.getAsJsonArray("links")) {
                if (previousJsonElement.isJsonObject()) {
                  JsonObject currentJsonObject = previousJsonElement.getAsJsonObject();

                  try {
                    URI uRI = URI.create(string(currentJsonObject, "url", ""));
                    if (isWebLink(uRI)) {
                      currentArrayList.add(
                          new AnticheatCodec.Link(
                              string(currentJsonObject, "name", uRI.getHost()), uRI));
                    }
                  } catch (IllegalArgumentException illegalArgumentException) {
                  }
                }
              }
            }

            AnticheatCodec.Status currentStatus;
            try {
              currentStatus =
                  AnticheatCodec.Status.valueOf(
                      string(jsonObject, "status", "CHECKING").toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException currentIllegalArgumentException) {
              currentStatus = AnticheatCodec.Status.UNKNOWN;
            }

            String nextText = string(jsonObject, "github", "");
            if (!nextText.matches("[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+")) {
              nextText = "";
            }

            int value = 0;

            try {
              value = Math.max(0, jsonObject.get("spigot").getAsInt());
            } catch (RuntimeException exception) {
            }

            arrayList.add(
                new AnticheatCodec.Entry(
                    currentText,
                    List.copyOf(linkedHashSet),
                    currentStatus,
                    string(jsonObject, "versions", "Not listed"),
                    string(jsonObject, "price", "Not listed"),
                    currentArrayList,
                    nextText,
                    value));
          }
        }
      }

      if (arrayList.isEmpty()) {
        throw new IllegalArgumentException("The catalog contains no entries");
      }

      arrayList.sort(
          Comparator.comparing(AnticheatCodec.Entry::name, String.CASE_INSENSITIVE_ORDER));
      return List.copyOf(arrayList);
    } else {
      throw new IllegalArgumentException("Unexpected catalog size");
    }
  }

  public static boolean isWebLink(URI uRI) {
    return uRI != null
        && ("https".equalsIgnoreCase(uRI.getScheme()) || "http".equalsIgnoreCase(uRI.getScheme()))
        && uRI.getHost() != null
        && uRI.getUserInfo() == null;
  }

  static String string(JsonObject jsonObject, String text, String currentText) {
    JsonElement jsonElement = jsonObject.get(text);
    return jsonElement != null && jsonElement.isJsonPrimitive()
        ? createText(jsonElement.getAsString())
        : currentText;
  }

  private static String createText(String text) {
    return text.replaceAll("[\\p{Cntrl}§]", " ").strip();
  }

  public record Entry(
      String name,
      List<String> platforms,
      AnticheatCodec.Status status,
      String versions,
      String price,
      List<AnticheatCodec.Link> links,
      String github,
      int spigot) {
    public Entry(
        String name,
        List<String> platforms,
        AnticheatCodec.Status status,
        String versions,
        String price,
        List<AnticheatCodec.Link> links,
        String github,
        int spigot) {
      platforms = List.copyOf(platforms);
      links = List.copyOf(links);
      this.name = name;
      this.platforms = platforms;
      this.status = status;
      this.versions = versions;
      this.price = price;
      this.links = links;
      this.github = github;
      this.spigot = spigot;
    }

    public AnticheatCodec.Entry withStatus(AnticheatCodec.Status status) {
      return new AnticheatCodec.Entry(
          this.name,
          this.platforms,
          status,
          this.versions,
          this.price,
          this.links,
          this.github,
          this.spigot);
    }

    public boolean matches(String text, String currentText, boolean currentMatches) {
      if ((currentText.isEmpty() || this.platforms.contains(currentText))
          && (!currentMatches || this.price.equalsIgnoreCase("Free"))) {
        String nextText =
            (this.name
                    + " "
                    + String.join(" ", this.platforms)
                    + " "
                    + this.versions
                    + " "
                    + this.status.label
                    + " "
                    + this.price)
                .toLowerCase(Locale.ROOT);
        return Arrays.stream(text.strip().toLowerCase(Locale.ROOT).split("\\s+"))
            .allMatch(nextText::contains);
      } else {
        return false;
      }
    }

    public boolean matches(
        String text,
        String currentText,
        boolean currentMatches,
        AnticheatCodec.Status currentStatus) {
      return (currentStatus == null || this.status == currentStatus)
          && this.matches(text, currentText, currentMatches);
    }
  }

  public record Link(String name, URI uri) {}

  public enum Sort {
    NAME("Name"),
    STATUS("Status"),
    PLATFORM("Platform"),
    PRICE("Price");

    public final String label;

    Sort(String text) {
      this.label = text;
    }

    public Comparator<AnticheatCodec.Entry> comparator(boolean enabled) {
      Comparator<AnticheatCodec.Entry> currentComparator =
          switch (this) {
            case NAME ->
                Comparator.comparing(AnticheatCodec.Entry::name, String.CASE_INSENSITIVE_ORDER);
            case STATUS -> Comparator.comparingInt(item -> item.status().ordinal());
            case PLATFORM ->
                Comparator.comparing(
                    item -> String.join(", ", item.platforms()), String.CASE_INSENSITIVE_ORDER);
            case PRICE ->
                Comparator.<AnticheatCodec.Entry, String>comparing(
                        item -> createText2(item.price()))
                    .thenComparingDouble(item -> calculateValue(item.price()));
          };
      return (enabled ? currentComparator.reversed() : currentComparator)
          .thenComparing(AnticheatCodec.Entry::name, String.CASE_INSENSITIVE_ORDER);
    }

    private static String createText2(String text) {
      if (text.equalsIgnoreCase("Free")) {
        return "0";
      } else {
        return !text.matches(".*\\d.*")
            ? "2" + text
            : "1" + text.replaceAll("[0-9.,\\s]", "").toUpperCase(Locale.ROOT);
      }
    }

    private static double calculateValue(String text) {
      Matcher currentMatcher = Pattern.compile("[0-9]+(?:[.,][0-9]+)?").matcher(text);
      return currentMatcher.find()
          ? Double.parseDouble(currentMatcher.group().replace(',', '.'))
          : 0.0;
    }

    private static AnticheatCodec.Sort[] $values() {
      return new AnticheatCodec.Sort[] {NAME, STATUS, PLATFORM, PRICE};
    }
  }

  public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DISCONTINUED("Discontinued"),
    OLD("Old"),
    UNAVAILABLE("Unavailable"),
    UNKNOWN("Unknown"),
    CHECKING("Checking…");

    public final String label;

    Status(String text) {
      this.label = text;
    }

    private static AnticheatCodec.Status[] $values() {
      return new AnticheatCodec.Status[] {
        ACTIVE, INACTIVE, DISCONTINUED, OLD, UNAVAILABLE, UNKNOWN, CHECKING
      };
    }
  }
}
