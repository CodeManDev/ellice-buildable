








package dev.felix.ellice.module.impl.playerlookup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.impl.playerlookup.PlayerlookupData;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

public final class PlayerlookupLooksValidValidator {
    private static final String fik438ijak0a = "https://playerdb.co/api/player/minecraft/";
    private static final Pattern fem7uazg2qyg = Pattern.compile("^[A-Za-z0-9_]{1,16}$");
    private static final Pattern f1g9y5na63cf = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern f1m27ukr70z0 = Pattern.compile("^[0-9a-fA-F]{32}$");
    private final HttpClient fequnvrvi92l = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(6L)).followRedirects(HttpClient.Redirect.NORMAL).build();
    private final Map<String, PlayerlookupData> f6pvkpvahl6u = new LinkedHashMap<String, PlayerlookupData>();
    private final Map<String, CompletableFuture<PlayerlookupData>> fcht1pvfdd9n = new LinkedHashMap<String, CompletableFuture<PlayerlookupData>>();
    private final Deque<String> f1xb7a0dt914 = new ArrayDeque<String>();
    private static final int f3q65ac74qx2 = 8;

    public static boolean looksValid(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.trim();
        if (string2.isEmpty()) {
            return false;
        }
        return (fem7uazg2qyg.matcher(string2).matches() || f1g9y5na63cf.matcher(string2).matches() || f1m27ukr70z0.matcher(string2).matches() ? 1 : 0) != 0;
    }

    public List<String> recent() {
        return new ArrayList<String>(this.f1xb7a0dt914);
    }

    public void clearRecent() {
        this.f1xb7a0dt914.clear();
    }

    private void m5bbkegwssgr(String string) {
        if (string == null || string.isBlank()) {
            return;
        }
        this.f1xb7a0dt914.removeIf(string2 -> string2.equalsIgnoreCase(string));
        this.f1xb7a0dt914.addFirst(string);
        while (this.f1xb7a0dt914.size() > 8) {
            this.f1xb7a0dt914.removeLast();
        }
    }

    public PlayerlookupData peek(String string) {
        if (string == null) {
            return null;
        }
        return this.f6pvkpvahl6u.get(string.trim().toLowerCase(Locale.ROOT));
    }

    public CompletableFuture<PlayerlookupData> lookup(String string) {
        String string2;
        String string3 = string2 = string == null ? "" : string.trim();
        if (!PlayerlookupLooksValidValidator.looksValid(string2)) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Not a valid username or UUID"));
        }
        String string4 = string2.toLowerCase(Locale.ROOT);
        this.m5bbkegwssgr(string2);
        PlayerlookupData playerlookupData2 = this.f6pvkpvahl6u.get(string4);
        if (playerlookupData2 != null) {
            return CompletableFuture.completedFuture(playerlookupData2);
        }
        CompletableFuture<PlayerlookupData> completableFuture = this.fcht1pvfdd9n.get(string4);
        if (completableFuture != null) {
            return completableFuture;
        }
        CompletionStage completionStage = this.m9mf4qa1sif9(string2).whenComplete((playerlookupData, throwable) -> {
            PlayerlookupLooksValidValidator playerlookupLooksValidValidator = this;
            synchronized (playerlookupLooksValidValidator) {
                this.fcht1pvfdd9n.remove(string4);
                if (playerlookupData != null) {
                    this.f6pvkpvahl6u.put(string4, (PlayerlookupData)playerlookupData);
                    this.f6pvkpvahl6u.put(playerlookupData.dashedUuid().toLowerCase(Locale.ROOT), (PlayerlookupData)playerlookupData);
                    this.f6pvkpvahl6u.put(playerlookupData.trimmedUuid().toLowerCase(Locale.ROOT), (PlayerlookupData)playerlookupData);
                    if (playerlookupData.username() != null) {
                        this.f6pvkpvahl6u.put(playerlookupData.username().toLowerCase(Locale.ROOT), (PlayerlookupData)playerlookupData);
                    }
                }
            }
        });
        this.fcht1pvfdd9n.put(string4, (CompletableFuture<PlayerlookupData>)completionStage);
        return completionStage.toCompletableFuture();
    }

    private CompletableFuture<PlayerlookupData> m9mf4qa1sif9(String string) {
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create(fik438ijak0a + URLEncoder.encode(string, StandardCharsets.UTF_8))).timeout(Duration.ofSeconds(10L)).header("Accept", "application/json").header("User-Agent", "ElliceClient/PlayerLookup").build();
        return this.fequnvrvi92l.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(this::mcugjzkoxegp);
    }

    private PlayerlookupData mcugjzkoxegp(HttpResponse<String> httpResponse) {
        if (httpResponse.statusCode() == 404) {
            throw new RuntimeException("No Minecraft account matches that.");
        }
        if (httpResponse.statusCode() == 429) {
            throw new RuntimeException("Rate limited \u2014 wait a few seconds and try again.");
        }
        if (httpResponse.statusCode() != 200) {
            throw new RuntimeException("Lookup failed (HTTP " + httpResponse.statusCode() + ")");
        }
        try {
            JsonObject jsonObject = JsonParser.parseString((String)httpResponse.body()).getAsJsonObject();
            if (!jsonObject.has("data") || jsonObject.get("data").isJsonNull()) {
                String string = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "No profile in response.";
                throw new RuntimeException(string);
            }
            JsonObject jsonObject2 = jsonObject.getAsJsonObject("data").getAsJsonObject("player");
            String string = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject2, "id");
            if (string == null) {
                throw new RuntimeException("No UUID in response.");
            }
            UUID uUID = UUID.fromString(string);
            String string2 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject2, "username");
            String string3 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject2, "avatar");
            String string4 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject2, "skin_texture");
            String string5 = null;
            boolean bl = false;
            Instant instant = null;
            if (jsonObject2.has("properties") && jsonObject2.get("properties").isJsonArray()) {
                JsonArray jsonArray = jsonObject2.getAsJsonArray("properties");
                for (int i = 0; i < jsonArray.size(); ++i) {
                    String string6;
                    JsonObject jsonObject3;
                    if (!jsonArray.get(i).isJsonObject() || !"textures".equals(PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject3 = jsonArray.get(i).getAsJsonObject(), "name")) || (string6 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject3, "value")) == null) continue;
                    try {
                        String string7 = new String(Base64.getDecoder().decode(string6), StandardCharsets.UTF_8);
                        JsonObject jsonObject4 = JsonParser.parseString((String)string7).getAsJsonObject();
                        if (jsonObject4.has("timestamp") && !jsonObject4.get("timestamp").isJsonNull()) {
                            instant = Instant.ofEpochMilli(jsonObject4.get("timestamp").getAsLong());
                        }
                        if (!jsonObject4.has("textures") || !jsonObject4.get("textures").isJsonObject()) continue;
                        JsonObject jsonObject5 = jsonObject4.getAsJsonObject("textures");
                        if (jsonObject5.has("SKIN") && jsonObject5.get("SKIN").isJsonObject()) {
                            JsonObject jsonObject6 = jsonObject5.getAsJsonObject("SKIN");
                            String string8 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject6, "url");
                            if (string8 != null) {
                                string4 = string8;
                            }
                            if (jsonObject6.has("metadata") && jsonObject6.get("metadata").isJsonObject()) {
                                String string9 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject6.getAsJsonObject("metadata"), "model");
                                bl = "slim".equalsIgnoreCase(string9);
                            }
                        }
                        if (!jsonObject5.has("CAPE") || !jsonObject5.get("CAPE").isJsonObject()) continue;
                        string5 = PlayerlookupLooksValidValidator.m9l5ktrsodth(jsonObject5.getAsJsonObject("CAPE"), "url");
                        continue;
                    }
                    catch (Exception exception) {
                        CoreIsInitializedHandler.LOGGER.debug("PlayerLookup texture decode failed: {}", (Object)exception.toString());
                    }
                }
            }
            return new PlayerlookupData(uUID, string2, string4, string5, bl, instant, string3);
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.warn("PlayerLookup parse failed: {}", (Object)exception.toString());
            throw new RuntimeException("Couldn't read response from PlayerDB.");
        }
    }

    private static String m9l5ktrsodth(JsonObject jsonObject, String string) {
        if (jsonObject == null) {
            return null;
        }
        JsonElement jsonElement = jsonObject.get(string);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString();
    }
}
