package dev.felix.ellice.anticheat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class AnticheatRepository implements AutoCloseable {
  private final Path path;
  private final Source source;
  private final ExecutorService fg4pzrq7d1u;
  private final ExecutorService executor2 =
      Executors.newFixedThreadPool(3, Thread.ofVirtual().name("anticheat-status-", 0L).factory());
  private volatile View view2 = new View(List.of(), false, null, "");
  private volatile boolean enabled;
  private long timestamp;

  public AnticheatRepository(Path path) {
    this(
        path.resolve("ellice-cache/anticheats.json"),
        new WebSource(),
        Executors.newSingleThreadExecutor(Thread.ofVirtual().name("anticheat-catalog").factory()));
  }

  public AnticheatRepository(Path path, Source source, ExecutorService executorService) {
    this.path = path;
    this.source = source;
    this.fg4pzrq7d1u = executorService;
  }

  public View view() {
    return this.view2;
  }

  public synchronized void load(boolean bl) {
    if (this.enabled || this.view2.loading()) {
      return;
    }
    if (!bl && AnticheatRepository.mhqquda6hrkh(this.view2.updated())) {
      return;
    }
    this.view2 = new View(this.view2.entries(), true, this.view2.updated(), "");
    long l = ++this.timestamp;
    this.fg4pzrq7d1u.execute(() -> this.mnv1g2hslez(l, bl));
  }

  private void mnv1g2hslez(long l, boolean bl) {
    try {
      if (this.view2.entries().isEmpty()) {
        this.updateState3(l);
      }
      if (!bl && AnticheatRepository.mhqquda6hrkh(this.view2.updated())) {
        this.meaqezzedr1y(l, this.view2.entries(), false, this.view2.updated(), "");
        return;
      }
      List<AnticheatCodec.Entry> list = AnticheatCodec.parse(this.source.load());
      Instant instant = Instant.now();
      this.meaqezzedr1y(l, list, true, instant, "");
      ConcurrentHashMap<String, AnticheatCodec.Status> concurrentHashMap =
          new ConcurrentHashMap<String, AnticheatCodec.Status>();
      List<Callable<Void>> list2 =
          list.stream()
              .filter(entry -> entry.status() == AnticheatCodec.Status.CHECKING)
              .map(
                  entry ->
                      (Callable<Void>)
                          () -> {
                            AnticheatCodec.Status status;
                            try {
                              status = this.source.status((AnticheatCodec.Entry) entry);
                            } catch (Exception exception) {
                              status = AnticheatCodec.Status.UNKNOWN;
                            }
                            concurrentHashMap.put(
                                entry.name(),
                                status == null ? AnticheatCodec.Status.UNKNOWN : status);
                            AnticheatRepository anticheatRepository = this;
                            synchronized (anticheatRepository) {
                              this.meaqezzedr1y(
                                  l,
                                  AnticheatRepository.collectValues(list, concurrentHashMap),
                                  true,
                                  instant,
                                  "");
                            }
                            return null;
                          })
              .toList();
      this.executor2.invokeAll(list2, 30L, TimeUnit.SECONDS);
      AnticheatRepository anticheatRepository = this;
      synchronized (anticheatRepository) {
        List<AnticheatCodec.Entry> list3 =
            AnticheatRepository.collectValues(list, concurrentHashMap).stream()
                .map(
                    entry ->
                        entry.status() == AnticheatCodec.Status.CHECKING
                            ? entry.withStatus(AnticheatCodec.Status.UNKNOWN)
                            : entry)
                .toList();
        this.meaqezzedr1y(l, list3, false, instant, "");
        if (!this.enabled && l == this.timestamp) {
          ++this.timestamp;
          this.updateState4(list3, instant);
        }
      }
    } catch (Exception exception) {
      if (exception instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      this.meaqezzedr1y(
          l,
          this.view2.entries(),
          false,
          this.view2.updated(),
          this.view2.entries().isEmpty()
              ? "The list couldn't be loaded. Check your connection and retry."
              : "Offline \u00b7 showing the saved list");
    }
  }

  private static List<AnticheatCodec.Entry> collectValues(
      List<AnticheatCodec.Entry> list, Map<String, AnticheatCodec.Status> map) {
    return list.stream()
        .map(entry -> entry.withStatus(map.getOrDefault(entry.name(), entry.status())))
        .toList();
  }

  private synchronized void meaqezzedr1y(
      long l, List<AnticheatCodec.Entry> list, boolean bl, Instant instant, String string) {
    if (!this.enabled && l == this.timestamp) {
      this.view2 = new View(list, bl, instant, string);
    }
  }

  private static boolean mhqquda6hrkh(Instant instant) {
    return (instant != null && instant.isAfter(Instant.now().minus(Duration.ofHours(6L))) ? 1 : 0)
        != 0;
  }

  private void updateState3(long l) {
    if (this.path == null) {
      return;
    }
    try {
      if (Files.size(this.path) > 0x100000L) {
        return;
      }
      JsonObject jsonObject =
          JsonParser.parseString((String) Files.readString(this.path)).getAsJsonObject();
      this.meaqezzedr1y(
          l,
          AnticheatCodec.parse(jsonObject.get("entries").toString()),
          true,
          Instant.parse(jsonObject.get("updated").getAsString()),
          "");
    } catch (Exception exception) {

    }
  }

  private void updateState4(List<AnticheatCodec.Entry> list, Instant instant) {
    if (this.path == null) {
      return;
    }
    try {
      JsonArray jsonArray = new JsonArray();
      for (AnticheatCodec.Entry object2 : list) {
        JsonObject atomicMoveNotSupportedException = new JsonObject();
        atomicMoveNotSupportedException.addProperty("name", object2.name());
        atomicMoveNotSupportedException.addProperty("status", object2.status().name());
        atomicMoveNotSupportedException.addProperty("versions", object2.versions());
        atomicMoveNotSupportedException.addProperty("price", object2.price());
        JsonArray jsonArray2 = new JsonArray();
        object2.platforms().forEach(arg_0 -> ((JsonArray) jsonArray2).add(arg_0));
        atomicMoveNotSupportedException.add("platform", (JsonElement) jsonArray2);
        JsonArray jsonArray3 = new JsonArray();
        for (AnticheatCodec.Link link : object2.links()) {
          JsonObject jsonObject = new JsonObject();
          jsonObject.addProperty("name", link.name());
          jsonObject.addProperty("url", link.uri().toString());
          jsonArray3.add((JsonElement) jsonObject);
        }
        atomicMoveNotSupportedException.add("links", (JsonElement) jsonArray3);
        jsonArray.add((JsonElement) atomicMoveNotSupportedException);
      }
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("updated", instant.toString());
      jsonObject.add("entries", (JsonElement) jsonArray);
      Files.createDirectories(this.path.getParent(), new FileAttribute[0]);
      Path path =
          Files.createTempFile(this.path.getParent(), "anticheats-", ".tmp", new FileAttribute[0]);
      try {
        Files.writeString(path, (CharSequence) jsonObject.toString(), new OpenOption[0]);
        try {
          Files.move(
              path, this.path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
          Files.move(path, this.path, StandardCopyOption.REPLACE_EXISTING);
        }
      } finally {
        Files.deleteIfExists(path);
      }
    } catch (IOException iOException) {

    }
  }

  @Override
  public synchronized void close() {
    this.enabled = true;
    ++this.timestamp;
    this.fg4pzrq7d1u.shutdownNow();
    this.executor2.shutdownNow();
  }

  private static final class WebSource implements Source {
    private final HttpClient client =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5L))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final AtomicBoolean f2qitz60o7m = new AtomicBoolean();

    private WebSource() {}

    @Override
    public String load() throws Exception {
      this.f2qitz60o7m.set(false);
      return this.createText(
          URI.create("https://maninmyvan.github.io/Minecraft-Anticheat-List/anticheats.json"));
    }

    @Override
    public AnticheatCodec.Status status(AnticheatCodec.Entry entry) throws Exception {
      JsonObject jsonObject;
      Instant instant = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(4L).toInstant();
      int n = 0;
      if (entry.spigot() > 0) {
        try {
          jsonObject =
              JsonParser.parseString(
                      (String)
                          this.createText(
                              URI.create("https://api.spiget.org/v2/resources/" + entry.spigot())))
                  .getAsJsonObject();
          if (jsonObject.has("updateDate")
              && Instant.ofEpochSecond(jsonObject.get("updateDate").getAsLong()).isAfter(instant)) {
            return AnticheatCodec.Status.ACTIVE;
          }
          n = jsonObject.has("updateDate") ? 1 : 0;
        } catch (Missing missing) {
          if (entry.github().isEmpty()) {
            return AnticheatCodec.Status.UNAVAILABLE;
          }
        } catch (IOException iOException) {

        }
      }
      if (entry.github().isEmpty()) {
        return n != 0 ? AnticheatCodec.Status.OLD : AnticheatCodec.Status.UNKNOWN;
      }
      if (this.f2qitz60o7m.get()) {
        return AnticheatCodec.Status.UNKNOWN;
      }
      try {
        jsonObject =
            JsonParser.parseString(
                    (String)
                        this.createText(
                            URI.create("https://api.github.com/repos/" + entry.github())))
                .getAsJsonObject();
        if (jsonObject.has("archived") && jsonObject.get("archived").getAsBoolean()) {
          return AnticheatCodec.Status.DISCONTINUED;
        }
        return Instant.parse(jsonObject.get("pushed_at").getAsString()).isAfter(instant)
            ? AnticheatCodec.Status.ACTIVE
            : AnticheatCodec.Status.OLD;
      } catch (Missing missing) {
        return AnticheatCodec.Status.UNAVAILABLE;
      }
    }

    private String createText(URI uRI) throws Exception {
      HttpRequest httpRequest =
          HttpRequest.newBuilder(uRI)
              .timeout(Duration.ofSeconds(6L))
              .header("User-Agent", "ElliceClient-AnticheatList")
              .header("Accept", "application/json")
              .GET()
              .build();
      HttpResponse<InputStream> httpResponse =
          this.client.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
      try (InputStream inputStream = httpResponse.body(); ) {
        if (httpResponse.statusCode() == 404) {
          throw new Missing();
        }
        if ((httpResponse.statusCode() == 403 || httpResponse.statusCode() == 429)
            && "api.github.com".equals(uRI.getHost())) {
          this.f2qitz60o7m.set(true);
        }
        if (httpResponse.statusCode() != 200) {
          throw new IOException("Catalog request failed");
        }
        byte[] byArray = inputStream.readNBytes(0x100001);
        if (byArray.length > 0x100000) {
          throw new IOException("Catalog response too large");
        }
        String string = new String(byArray, StandardCharsets.UTF_8);
        return string;
      }
    }

    private static final class Missing extends IOException {
      private Missing() {}
    }
  }

  public static interface Source {
    public String load() throws Exception;

    public AnticheatCodec.Status status(AnticheatCodec.Entry var1) throws Exception;
  }

  public record View(
      List<AnticheatCodec.Entry> entries, boolean loading, Instant updated, String notice) {
    public View {
      entries = List.copyOf(entries);
    }
  }
}
