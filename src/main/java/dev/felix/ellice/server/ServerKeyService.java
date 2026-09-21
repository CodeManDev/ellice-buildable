package dev.felix.ellice.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.LoggerFactory;

public final class ServerKeyService {
  private final Path path;
  private final Map<String, String> text;
  private final Map<UUID, Map<String, Usage>> text2;
  private boolean enabled;
  private boolean enabled2;

  public ServerKeyService(final Path path) {
    this.text = new HashMap<String, String>();
    this.text2 = new HashMap<UUID, Map<String, Usage>>();
    this.enabled = true;
    this.path = path.resolve("ellice/server-library.json");
    if (!Files.exists(this.path, new LinkOption[0])) {
      return;
    }
    try {
      if (Files.size(this.path) > 4194304L) {
        throw new IOException("Server library too large");
      }
      final JsonObject asJsonObject =
          JsonParser.parseString(Files.readString(this.path)).getAsJsonObject();
      if (asJsonObject.has("servers")) {
        asJsonObject
            .getAsJsonObject("servers")
            .entrySet()
            .forEach(
                entry -> {
                  try {
                    this.text.put(
                        key(entry.getKey()),
                        ServerQueryService.clean(entry.getValue().getAsString(), 100));
                  } catch (final RuntimeException ex2) {
                  }
                  return;
                });
      }
      if (asJsonObject.has("profiles")) {
        asJsonObject
            .getAsJsonObject("profiles")
            .entrySet()
            .forEach(
                entry2 -> {
                  try {
                    UUID profileId = UUID.fromString(entry2.getKey());
                    final HashMap<String, Usage> map = new HashMap<>();
                    entry2
                        .getValue()
                        .getAsJsonObject()
                        .entrySet()
                        .forEach(
                            entry3 -> {
                              try {
                                final JsonObject jsonObject = entry3.getValue().getAsJsonObject();
                                map.put(
                                    key(entry3.getKey()),
                                    new Usage(
                                        jsonObject.get("favorite").getAsBoolean(),
                                        Math.max(0, jsonObject.get("joins").getAsInt()),
                                        Math.max(0L, jsonObject.get("playedMillis").getAsLong()),
                                        Math.max(0L, jsonObject.get("lastJoined").getAsLong())));
                              } catch (final RuntimeException ex4) {
                              }
                              return;
                            });
                    this.text2.put(profileId, map);
                  } catch (final RuntimeException ex3) {
                  }
                });
      }
    } catch (final Exception ex) {
      this.enabled = false;
      LoggerFactory.getLogger((Class) ServerKeyService.class)
          .warn("Could not read server library", (Throwable) ex);
    }
  }

  public static String key(final String s) {
    return ParsedServerEndpoint.parse(s).address();
  }

  public String name(final String defaultValue) {
    return this.text.getOrDefault(key(defaultValue), defaultValue);
  }

  public Map<String, Usage> history(final UUID key) {
    return Map.copyOf(
        (Map<? extends String, ? extends Usage>) this.text2.getOrDefault(key, Map.of()));
  }

  public Usage usage(final UUID key, final String s) {
    return this.text2.getOrDefault(key, Map.of()).getOrDefault(key(s), Usage.EMPTY);
  }

  public boolean writable() {
    return this.enabled;
  }

  public boolean dirty() {
    return this.enabled2;
  }

  public void remember(final String s, final String s2) {
    final String key = key(s);
    final String s3 = (s2 == null || s2.isBlank()) ? key : ServerQueryService.clean(s2, 100);
    if (!s3.equals(this.text.put(key, s3))) {
      this.enabled2 = true;
    }
  }

  public void toggleFavorite(final UUID uuid, final String s) {
    if (uuid == null) {
      return;
    }
    final Usage usage = this.usage(uuid, s);
    this.updateState(
        uuid,
        s,
        new Usage(!usage.favorite(), usage.joins(), usage.playedMillis(), usage.lastJoined()));
  }

  public void joined(final UUID uuid, final String s, final String s2, final long n) {
    if (uuid == null) {
      return;
    }
    this.remember(s, s2);
    final Usage usage = this.usage(uuid, s);
    this.updateState(
        uuid, s, new Usage(usage.favorite(), usage.joins() + 1, usage.playedMillis(), n));
  }

  public void played(final UUID uuid, final String s, final long n) {
    if (uuid == null || n <= 0L) {
      return;
    }
    final Usage usage = this.usage(uuid, s);
    this.updateState(
        uuid,
        s,
        new Usage(usage.favorite(), usage.joins(), usage.playedMillis() + n, usage.lastJoined()));
  }

  private void updateState(final UUID key, final String s, final Usage usage) {
    this.text2.computeIfAbsent(key, p0 -> new HashMap()).put(key(s), usage);
    this.enabled2 = true;
  }

  public String snapshot() {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("version", (Number) 1);
    final JsonObject jsonObject2 = new JsonObject();
    for (final Map.Entry entry : this.text.entrySet()) {
      jsonObject2.addProperty((String) entry.getKey(), (String) entry.getValue());
    }
    jsonObject.add("servers", (JsonElement) jsonObject2);
    final JsonObject jsonObject3 = new JsonObject();
    for (final Map.Entry entry2 : this.text2.entrySet()) {
      final JsonObject jsonObject4 = new JsonObject();
      for (final Map.Entry entry3 :
          (Iterable<Map.Entry>) (Iterable<?>) (((Map) entry2.getValue()).entrySet())) {
        final Usage usage = (Usage) entry3.getValue();
        final JsonObject jsonObject5 = new JsonObject();
        jsonObject5.addProperty("favorite", Boolean.valueOf(usage.favorite()));
        jsonObject5.addProperty("joins", (Number) usage.joins());
        jsonObject5.addProperty("playedMillis", (Number) usage.playedMillis());
        jsonObject5.addProperty("lastJoined", (Number) usage.lastJoined());
        jsonObject4.add((String) entry3.getKey(), (JsonElement) jsonObject5);
      }
      jsonObject3.add(((UUID) entry2.getKey()).toString(), (JsonElement) jsonObject4);
    }
    jsonObject.add("profiles", (JsonElement) jsonObject3);
    this.enabled2 = false;
    return new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement) jsonObject);
  }

  public void writeSnapshot(final String csq) throws IOException {
    if (!this.enabled) {
      throw new IOException("Existing server library could not be read");
    }
    Files.createDirectories(this.path.getParent(), (FileAttribute<?>[]) new FileAttribute[0]);
    final Path tempFile =
        Files.createTempFile(
            this.path.getParent(),
            "server-library-",
            ".tmp",
            (FileAttribute<?>[]) new FileAttribute[0]);
    try {
      Files.writeString(tempFile, csq, new OpenOption[0]);
      try {
        Files.move(
            tempFile,
            this.path,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE);
      } catch (final AtomicMoveNotSupportedException ex) {
        Files.move(tempFile, this.path, StandardCopyOption.REPLACE_EXISTING);
      }
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  public record Usage(boolean favorite, int joins, long playedMillis, long lastJoined) {
    public static final Usage EMPTY;

    static {
      EMPTY = new Usage(false, 0, 0L, 0L);
    }
  }
}
