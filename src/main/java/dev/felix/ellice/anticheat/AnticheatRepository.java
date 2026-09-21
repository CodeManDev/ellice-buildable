








package dev.felix.ellice.anticheat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.anticheat.AnticheatCodec;
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

public final class AnticheatRepository
implements AutoCloseable {
    private final Path f3d8egnue2bz;
    private final Source fcb1a9lp1ne7;
    private final ExecutorService fg4pzrq7d1u;
    private final ExecutorService f56gjr4dz5d0 = Executors.newFixedThreadPool(3, Thread.ofVirtual().name("anticheat-status-", 0L).factory());
    private volatile View fgt1mo8mky6q = new View(List.of(), false, null, "");
    private volatile boolean f69vvzm57zj9;
    private long f9mtu2z7lyp4;

    public AnticheatRepository(Path path) {
        this(path.resolve("ellice-cache/anticheats.json"), new WebSource(), Executors.newSingleThreadExecutor(Thread.ofVirtual().name("anticheat-catalog").factory()));
    }

    public AnticheatRepository(Path path, Source source, ExecutorService executorService) {
        this.f3d8egnue2bz = path;
        this.fcb1a9lp1ne7 = source;
        this.fg4pzrq7d1u = executorService;
    }

    public View view() {
        return this.fgt1mo8mky6q;
    }

    public synchronized void load(boolean bl) {
        if (this.f69vvzm57zj9 || this.fgt1mo8mky6q.loading()) {
            return;
        }
        if (!bl && AnticheatRepository.mhqquda6hrkh(this.fgt1mo8mky6q.updated())) {
            return;
        }
        this.fgt1mo8mky6q = new View(this.fgt1mo8mky6q.entries(), true, this.fgt1mo8mky6q.updated(), "");
        long l = ++this.f9mtu2z7lyp4;
        this.fg4pzrq7d1u.execute(() -> this.mnv1g2hslez(l, bl));
    }

    


    private void mnv1g2hslez(long l, boolean bl) {
        try {
            if (this.fgt1mo8mky6q.entries().isEmpty()) {
                this.m5r54ejdkihe(l);
            }
            if (!bl && AnticheatRepository.mhqquda6hrkh(this.fgt1mo8mky6q.updated())) {
                this.meaqezzedr1y(l, this.fgt1mo8mky6q.entries(), false, this.fgt1mo8mky6q.updated(), "");
                return;
            }
            List<AnticheatCodec.Entry> list = AnticheatCodec.parse(this.fcb1a9lp1ne7.load());
            Instant instant = Instant.now();
            this.meaqezzedr1y(l, list, true, instant, "");
            ConcurrentHashMap<String, AnticheatCodec.Status> concurrentHashMap = new ConcurrentHashMap<String, AnticheatCodec.Status>();
            List<Callable<Void>> list2 = list.stream().filter(entry -> entry.status() == AnticheatCodec.Status.CHECKING).map(entry -> (Callable<Void>) () -> {
                AnticheatCodec.Status status;
                try {
                    status = this.fcb1a9lp1ne7.status((AnticheatCodec.Entry)entry);
                }
                catch (Exception exception) {
                    status = AnticheatCodec.Status.UNKNOWN;
                }
                concurrentHashMap.put(entry.name(), status == null ? AnticheatCodec.Status.UNKNOWN : status);
                AnticheatRepository anticheatRepository = this;
                synchronized (anticheatRepository) {
                    this.meaqezzedr1y(l, AnticheatRepository.m535f5oabz1n(list, concurrentHashMap), true, instant, "");
                }
                return null;
            }).toList();
            this.f56gjr4dz5d0.invokeAll(list2, 30L, TimeUnit.SECONDS);
            AnticheatRepository anticheatRepository = this;
            synchronized (anticheatRepository) {
                List<AnticheatCodec.Entry> list3 = AnticheatRepository.m535f5oabz1n(list, concurrentHashMap).stream().map(entry -> entry.status() == AnticheatCodec.Status.CHECKING ? entry.withStatus(AnticheatCodec.Status.UNKNOWN) : entry).toList();
                this.meaqezzedr1y(l, list3, false, instant, "");
                if (!this.f69vvzm57zj9 && l == this.f9mtu2z7lyp4) {
                    ++this.f9mtu2z7lyp4;
                    this.m2s9xu276q8w(list3, instant);
                }
            }
        }
        catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            this.meaqezzedr1y(l, this.fgt1mo8mky6q.entries(), false, this.fgt1mo8mky6q.updated(), this.fgt1mo8mky6q.entries().isEmpty() ? "The list couldn't be loaded. Check your connection and retry." : "Offline \u00b7 showing the saved list");
        }
    }

    private static List<AnticheatCodec.Entry> m535f5oabz1n(List<AnticheatCodec.Entry> list, Map<String, AnticheatCodec.Status> map) {
        return list.stream().map(entry -> entry.withStatus(map.getOrDefault(entry.name(), entry.status()))).toList();
    }

    private synchronized void meaqezzedr1y(long l, List<AnticheatCodec.Entry> list, boolean bl, Instant instant, String string) {
        if (!this.f69vvzm57zj9 && l == this.f9mtu2z7lyp4) {
            this.fgt1mo8mky6q = new View(list, bl, instant, string);
        }
    }

    private static boolean mhqquda6hrkh(Instant instant) {
        return (instant != null && instant.isAfter(Instant.now().minus(Duration.ofHours(6L))) ? 1 : 0) != 0;
    }

    private void m5r54ejdkihe(long l) {
        if (this.f3d8egnue2bz == null) {
            return;
        }
        try {
            if (Files.size(this.f3d8egnue2bz) > 0x100000L) {
                return;
            }
            JsonObject jsonObject = JsonParser.parseString((String)Files.readString(this.f3d8egnue2bz)).getAsJsonObject();
            this.meaqezzedr1y(l, AnticheatCodec.parse(jsonObject.get("entries").toString()), true, Instant.parse(jsonObject.get("updated").getAsString()), "");
        }
        catch (Exception exception) {
            
        }
    }

    


    private void m2s9xu276q8w(List<AnticheatCodec.Entry> list, Instant instant) {
        if (this.f3d8egnue2bz == null) {
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
                object2.platforms().forEach(arg_0 -> ((JsonArray)jsonArray2).add(arg_0));
                atomicMoveNotSupportedException.add("platform", (JsonElement)jsonArray2);
                JsonArray jsonArray3 = new JsonArray();
                for (AnticheatCodec.Link link : object2.links()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", link.name());
                    jsonObject.addProperty("url", link.uri().toString());
                    jsonArray3.add((JsonElement)jsonObject);
                }
                atomicMoveNotSupportedException.add("links", (JsonElement)jsonArray3);
                jsonArray.add((JsonElement)atomicMoveNotSupportedException);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("updated", instant.toString());
            jsonObject.add("entries", (JsonElement)jsonArray);
            Files.createDirectories(this.f3d8egnue2bz.getParent(), new FileAttribute[0]);
            Path path = Files.createTempFile(this.f3d8egnue2bz.getParent(), "anticheats-", ".tmp", new FileAttribute[0]);
            try {
                Files.writeString(path, (CharSequence)jsonObject.toString(), new OpenOption[0]);
                try {
                    Files.move(path, this.f3d8egnue2bz, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                }
                catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
                    Files.move(path, this.f3d8egnue2bz, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            finally {
                Files.deleteIfExists(path);
            }
        }
        catch (IOException iOException) {
            
        }
    }

    @Override
    public synchronized void close() {
        this.f69vvzm57zj9 = true;
        ++this.f9mtu2z7lyp4;
        this.fg4pzrq7d1u.shutdownNow();
        this.f56gjr4dz5d0.shutdownNow();
    }

    private static final class WebSource
    implements Source {
        private final HttpClient f5690sguz3xe = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).followRedirects(HttpClient.Redirect.NORMAL).build();
        private final AtomicBoolean f2qitz60o7m = new AtomicBoolean();

        private WebSource() {
        }

        @Override
        public String load() throws Exception {
            this.f2qitz60o7m.set(false);
            return this.mjg3z14dea5d(URI.create("https://maninmyvan.github.io/Minecraft-Anticheat-List/anticheats.json"));
        }

        @Override
        public AnticheatCodec.Status status(AnticheatCodec.Entry entry) throws Exception {
            JsonObject jsonObject;
            Instant instant = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(4L).toInstant();
            int n = 0;
            if (entry.spigot() > 0) {
                try {
                    jsonObject = JsonParser.parseString((String)this.mjg3z14dea5d(URI.create("https://api.spiget.org/v2/resources/" + entry.spigot()))).getAsJsonObject();
                    if (jsonObject.has("updateDate") && Instant.ofEpochSecond(jsonObject.get("updateDate").getAsLong()).isAfter(instant)) {
                        return AnticheatCodec.Status.ACTIVE;
                    }
                    n = jsonObject.has("updateDate") ? 1 : 0;
                }
                catch (Missing missing) {
                    if (entry.github().isEmpty()) {
                        return AnticheatCodec.Status.UNAVAILABLE;
                    }
                }
                catch (IOException iOException) {
                    
                }
            }
            if (entry.github().isEmpty()) {
                return n != 0 ? AnticheatCodec.Status.OLD : AnticheatCodec.Status.UNKNOWN;
            }
            if (this.f2qitz60o7m.get()) {
                return AnticheatCodec.Status.UNKNOWN;
            }
            try {
                jsonObject = JsonParser.parseString((String)this.mjg3z14dea5d(URI.create("https://api.github.com/repos/" + entry.github()))).getAsJsonObject();
                if (jsonObject.has("archived") && jsonObject.get("archived").getAsBoolean()) {
                    return AnticheatCodec.Status.DISCONTINUED;
                }
                return Instant.parse(jsonObject.get("pushed_at").getAsString()).isAfter(instant) ? AnticheatCodec.Status.ACTIVE : AnticheatCodec.Status.OLD;
            }
            catch (Missing missing) {
                return AnticheatCodec.Status.UNAVAILABLE;
            }
        }

        private String mjg3z14dea5d(URI uRI) throws Exception {
            HttpRequest httpRequest = HttpRequest.newBuilder(uRI).timeout(Duration.ofSeconds(6L)).header("User-Agent", "ElliceClient-AnticheatList").header("Accept", "application/json").GET().build();
            HttpResponse<InputStream> httpResponse = this.f5690sguz3xe.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
            try (InputStream inputStream = httpResponse.body();){
                if (httpResponse.statusCode() == 404) {
                    throw new Missing();
                }
                if ((httpResponse.statusCode() == 403 || httpResponse.statusCode() == 429) && "api.github.com".equals(uRI.getHost())) {
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

        private static final class Missing
        extends IOException {
            private Missing() {
            }
        }
    }

    public static interface Source {
        public String load() throws Exception;

        public AnticheatCodec.Status status(AnticheatCodec.Entry var1) throws Exception;
    }

    public record View(List<AnticheatCodec.Entry> entries, boolean loading, Instant updated, String notice) {
        public View {
            entries = List.copyOf(entries);
        }
    }
}

