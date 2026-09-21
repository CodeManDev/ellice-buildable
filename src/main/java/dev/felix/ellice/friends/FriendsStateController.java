package dev.felix.ellice.friends;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class FriendsStateController {
  private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
  private final Path path;
  private volatile List<FriendProfile> items = List.of();
  private volatile Map<UUID, FriendProfile> entries2 = Map.of();
  private volatile Map<String, FriendProfile> text = Map.of();
  private volatile long timestamp;
  private volatile String text2 = "";
  private boolean enabled;

  public FriendsStateController(Path currentPath) {
    this.path = currentPath.resolve("ellice/friends.json");
    if (Files.exists(this.path)) {
      try {
        JsonObject jsonObject =
            JsonParser.parseString(Files.readString(this.path)).getAsJsonObject();
        if (jsonObject.get("version").getAsInt() != 1) {
          throw new IOException("Unsupported friends file version");
        }

        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        HashSet currentHashSet = new HashSet();
        HashSet nextHashSet = new HashSet();

        for (JsonElement jsonElement : jsonObject.getAsJsonArray("friends")) {
          JsonObject currentJsonObject = jsonElement.getAsJsonObject();
          UUID uUID = UUID.fromString(currentJsonObject.get("id").getAsString());
          UUID currentUUID =
              currentJsonObject.has("uuid")
                  ? UUID.fromString(currentJsonObject.get("uuid").getAsString())
                  : null;
          String text = currentJsonObject.get("name").getAsString();
          String currentText = currentJsonObject.get("note").getAsString();
          if (currentUUID == null || !text.isEmpty()) {
            updateState3(text);
          }

          if (currentText.length() > 240
              || !hashSet.add(uUID)
              || (currentUUID != null
                  ? !currentHashSet.add(currentUUID)
                  : !nextHashSet.add(createText(text)))) {
            throw new IOException("Invalid or duplicate friend");
          }

          EnumSet enumSet = EnumSet.noneOf(FriendsMode.class);

          for (JsonElement currentJsonElement : currentJsonObject.getAsJsonArray("excluded")) {
            enumSet.add(
                FriendsMode.valueOf(
                    switch (currentJsonElement.getAsString()) {
                      case "ESP_2D", "PLAYER_FIRE_ESP" -> "ESP";
                      default -> currentJsonElement.getAsString();
                    }));
          }

          arrayList.add(
              new FriendProfile(
                  uUID,
                  currentUUID,
                  text,
                  currentText,
                  currentJsonObject.get("favorite").getAsBoolean(),
                  enumSet));
        }

        this.updateState2(arrayList);
      } catch (Exception exception) {
        this.enabled = true;
        this.text2 = "Could not read ellice/friends.json. The existing file has been preserved.";
      }
    }
  }

  public List<FriendProfile> entries() {
    return this.items;
  }

  public long revision() {
    return this.timestamp;
  }

  public String notice() {
    return this.text2;
  }

  public FriendProfile get(UUID uUID) {
    return this.items.stream().filter(item -> item.id().equals(uUID)).findFirst().orElse(null);
  }

  public FriendProfile find(UUID uUID, String name) {
    FriendProfile friendProfile = uUID == null ? null : this.entries2.get(uUID);
    return friendProfile != null ? friendProfile : this.text.get(createText(name));
  }

  public boolean excludes(FriendsMode friendsMode, UUID uUID, String text) {
    FriendProfile friendProfile = this.find(uUID, text);
    return friendProfile != null && friendProfile.excluded().contains(friendsMode);
  }

  public synchronized FriendProfile add(String text, List<FriendsData> items) throws IOException {
    String currentText = text.strip();
    UUID uUID = null;
    String nextText = currentText;

    try {
      uUID = UUID.fromString(currentText);
      nextText = "";
    } catch (IllegalArgumentException illegalArgumentException) {
      updateState3(currentText);
    }

    for (FriendsData friendsData : items) {
      if (uUID != null
          ? uUID.equals(friendsData.uuid())
          : currentText.equalsIgnoreCase(friendsData.name())) {
        uUID = friendsData.uuid();
        nextText = friendsData.name();
        break;
      }
    }

    return this.add(uUID, nextText);
  }

  public synchronized FriendProfile add(UUID uUID, String text) throws IOException {
    if (uUID == null || !text.isEmpty()) {
      updateState3(text);
    }

    FriendProfile friendProfile = this.find(uUID, text);
    if (friendProfile != null) {
      if (friendProfile.uuid() == null && uUID != null) {
        friendProfile = friendProfile.identity(uUID, text);
        this.update(friendProfile);
      }

      return friendProfile;
    } else {
      FriendProfile currentFriendProfile =
          new FriendProfile(UUID.randomUUID(), uUID, text, "", false, FriendsMode.defaults());
      ArrayList arrayList = new ArrayList<>(this.items);
      arrayList.add(currentFriendProfile);
      this.updateState(arrayList);
      return currentFriendProfile;
    }
  }

  public synchronized void update(FriendProfile friendProfile) throws IOException {
    FriendProfile currentFriendProfile = this.get(friendProfile.id());
    if (currentFriendProfile == null) {
      throw new IllegalArgumentException("Friend no longer exists");
    }

    if (friendProfile.note().length() > 240) {
      throw new IllegalArgumentException("Notes can contain up to 240 characters");
    }

    ArrayList<FriendProfile> arrayList = new ArrayList<>(this.items);
    arrayList.set(arrayList.indexOf(currentFriendProfile), friendProfile);
    this.updateState(arrayList);
  }

  public synchronized void remove(UUID uUID) throws IOException {
    ArrayList<FriendProfile> arrayList = new ArrayList<>(this.items);
    arrayList.removeIf(item -> item.id().equals(uUID));
    this.updateState(arrayList);
  }

  public synchronized void restore(FriendProfile friendProfile) throws IOException {
    if (this.get(friendProfile.id()) == null
        && this.find(friendProfile.uuid(), friendProfile.name()) == null) {
      ArrayList arrayList = new ArrayList<>(this.items);
      arrayList.add(friendProfile);
      this.updateState(arrayList);
    } else {
      throw new IllegalArgumentException("This friend is already in your list");
    }
  }

  public synchronized void resolve(List<FriendsData> currentItems) {
    if (!this.enabled) {
      ArrayList arrayList = new ArrayList<>(this.items);
      byte byteValue = 0;
      HashSet hashSet = new HashSet<>(this.entries2.keySet());

      for (int index = 0; index < arrayList.size(); index++) {
        FriendProfile friendProfile = (FriendProfile) arrayList.get(index);

        for (FriendsData friendsData : currentItems) {
          if (friendProfile.matches(friendsData.uuid(), friendsData.name())
              && checkCondition(friendsData.name())
              && (friendProfile.uuid() != null || !hashSet.contains(friendsData.uuid()))) {
            if (!Objects.equals(friendProfile.uuid(), friendsData.uuid())
                || !friendProfile.name().equals(friendsData.name())) {
              arrayList.set(index, friendProfile.identity(friendsData.uuid(), friendsData.name()));
              hashSet.add(friendsData.uuid());
              byteValue = 1;
            }
            break;
          }
        }
      }

      if (byteValue != 0) {
        try {
          this.updateState(arrayList);
        } catch (IOException iOException) {
          this.text2 = "Could not save updated friend identities.";
        }
      }
    }
  }

  private void updateState(List<FriendProfile> items) throws IOException {
    if (this.enabled) {
      throw new IOException(this.text2);
    }

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("version", 1);
    JsonArray jsonArray = new JsonArray();
    jsonObject.add("friends", jsonArray);

    for (FriendProfile friendProfile : items) {
      JsonObject currentJsonObject = new JsonObject();
      currentJsonObject.addProperty("id", friendProfile.id().toString());
      if (friendProfile.uuid() != null) {
        currentJsonObject.addProperty("uuid", friendProfile.uuid().toString());
      }

      currentJsonObject.addProperty("name", friendProfile.name());
      currentJsonObject.addProperty("note", friendProfile.note());
      currentJsonObject.addProperty("favorite", friendProfile.favorite());
      JsonArray currentJsonArray = new JsonArray();
      friendProfile.excluded().stream().sorted().forEach(item -> currentJsonArray.add(item.name()));
      currentJsonObject.add("excluded", currentJsonArray);
      jsonArray.add(currentJsonObject);
    }

    Files.createDirectories(this.path.getParent());
    Path currentPath = Files.createTempFile(this.path.getParent(), "friends-", ".tmp");

    try {
      Files.writeString(currentPath, gson2.toJson(jsonObject));

      try {
        Files.move(
            currentPath,
            this.path,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE);
      } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
        Files.move(currentPath, this.path, StandardCopyOption.REPLACE_EXISTING);
      }
    } finally {
      Files.deleteIfExists(currentPath);
    }

    this.updateState2(items);
    this.text2 = "";
  }

  private void updateState2(List<FriendProfile> currentItems) {
    HashMap hashMap = new HashMap();
    HashMap currentHashMap = new HashMap();

    for (FriendProfile friendProfile : currentItems) {
      if (friendProfile.uuid() != null) {
        hashMap.put(friendProfile.uuid(), friendProfile);
      } else {
        currentHashMap.put(createText(friendProfile.name()), friendProfile);
      }
    }

    this.entries2 = Map.copyOf(hashMap);
    this.text = Map.copyOf(currentHashMap);
    this.items = List.copyOf(currentItems);
    this.timestamp++;
  }

  private static String createText(String text) {
    return text == null ? "" : text.toLowerCase(Locale.ROOT);
  }

  private static boolean checkCondition(String text) {
    return text != null && text.matches("[A-Za-z0-9_.-]{1,32}");
  }

  private static void updateState3(String text) {
    if (!checkCondition(text)) {
      throw new IllegalArgumentException("Enter a player name (no spaces) or a full UUID.");
    }
  }
}
