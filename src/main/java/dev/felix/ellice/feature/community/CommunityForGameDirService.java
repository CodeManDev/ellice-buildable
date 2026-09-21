package dev.felix.ellice.feature.community;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dev.felix.ellice.feature.license.LicenseBaseUrlService;
import dev.felix.ellice.feature.license.LicenseRepository;
import dev.felix.ellice.feature.license.LicenseStableService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public final class CommunityForGameDirService {
   private static final Gson f4syu93evwgr = new Gson();
   private static final long fiydh0nhgy6e = 1048576L;
   private static final int fa43ujfq5l5s = 24;
   private final HttpClient fgmr64zfra8t = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).followRedirects(Redirect.NORMAL).build();
   private final Path f5hknee2o9oq;

   public CommunityForGameDirService(Path var1) {
      this.f5hknee2o9oq = var1;
   }

   public static CommunityForGameDirService forGameDir() {
      return new CommunityForGameDirService(FabricLoader.getInstance().getGameDir());
   }

   public String voterKey() {
      String var1 = LicenseStableService.stable();
      return !"hw2_unknown".equals(var1) && !var1.isBlank() ? var1 : this.m162f123eynt();
   }

   public String sessionJwt() {
      LicenseRepository.CachedLicense var1 = LicenseRepository.load(this.f5hknee2o9oq);
      return var1 == null ? "" : var1.jwt();
   }

   public void list(String var1, String var2, int var3, Consumer<CommunityForGameDirService.Result<CommunityForGameDirService.Page>> var4) {
      String var5 = switch (var1) {
         case "new", "downloads" -> var1;
         default -> "top";
      };
      JsonObject var8 = new JsonObject();
      var8.addProperty("action", "list");
      var8.addProperty("sort", var5);
      var8.addProperty("query", var2 == null ? "" : var2);
      var8.addProperty("limit", 24);
      var8.addProperty("offset", Math.max(0, var3));
      var8.addProperty("voter_key", this.voterKey());
      this.mf5pqh89mjp6(var8, var1x -> {
         if (!var1x.ok()) {
            var4.accept(CommunityForGameDirService.Result.error(var1x.error()));
         } else {
            var4.accept(CommunityForGameDirService.Result.ok(parsePage(var1x.value())));
         }
      });
   }

   public void upload(
      String var1,
      String var2,
      String var3,
      JsonObject var4,
      String var5,
      Consumer<CommunityForGameDirService.Result<CommunityForGameDirService.UploadResult>> var6
   ) {
      JsonObject var7 = new JsonObject();
      var7.addProperty("action", "upload");
      var7.addProperty("title", var1 == null ? "" : var1);
      var7.addProperty("description", var2 == null ? "" : var2);
      var7.addProperty("author_name", var3 != null && !var3.isBlank() ? var3 : "Anonymous");
      var7.add("config", withoutKeybinds(var4));
      var7.addProperty("ellice_version", var5 == null ? "" : var5);
      var7.addProperty("author_key", this.voterKey());
      this.mf5pqh89mjp6(var7, var2x -> {
         if (!var2x.ok()) {
            var6.accept(CommunityForGameDirService.Result.error(var2x.error()));
         } else {
            JsonObject var3x = var2x.value();
            String var4x = var3x.has("id") ? var3x.get("id").getAsString() : "";
            String var5x = var3x.has("delete_token") ? var3x.get("delete_token").getAsString() : "";
            if (!var4x.isBlank() && !var5x.isBlank()) {
               this.m5qg1o8usch0(var4x, var5x);
               var6.accept(CommunityForGameDirService.Result.ok(new CommunityForGameDirService.UploadResult(var4x, var5x)));
            } else {
               var6.accept(CommunityForGameDirService.Result.error("Upload failed. Please try again."));
            }
         }
      });
   }

   public void vote(String var1, int var2, Consumer<CommunityForGameDirService.Result<CommunityForGameDirService.VoteResult>> var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", "vote");
      var4.addProperty("id", var1);
      var4.addProperty("value", var2);
      var4.addProperty("voter_key", this.voterKey());
      this.mf5pqh89mjp6(
         var4,
         var1x -> {
            if (!var1x.ok()) {
               var3.accept(CommunityForGameDirService.Result.error(var1x.error()));
            } else {
               JsonObject var2x = var1x.value();
               var3.accept(
                  CommunityForGameDirService.Result.ok(
                     new CommunityForGameDirService.VoteResult(
                        m6scyhah6uo9(var2x, "upvotes"), m6scyhah6uo9(var2x, "downvotes"), m6scyhah6uo9(var2x, "score"), m6scyhah6uo9(var2x, "my_vote")
                     )
                  )
               );
            }
         }
      );
   }

   public void download(String var1, Consumer<CommunityForGameDirService.Result<CommunityForGameDirService.Download>> var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty("action", "download");
      var3.addProperty("id", var1);
      var3.addProperty("voter_key", this.voterKey());
      this.mf5pqh89mjp6(var3, var1x -> {
         if (!var1x.ok()) {
            var2.accept(CommunityForGameDirService.Result.error(var1x.error()));
         } else {
            JsonObject var2x = var1x.value();
            if (var2x.has("config") && var2x.get("config").isJsonObject()) {
               JsonObject var3x = withoutKeybinds(var2x.getAsJsonObject("config"));
               var2.accept(CommunityForGameDirService.Result.ok(new CommunityForGameDirService.Download(parseEntry(var2x), var3x)));
            } else {
               var2.accept(CommunityForGameDirService.Result.error("Download failed. Please try again."));
            }
         }
      });
   }

   public void report(String var1, String var2, Consumer<CommunityForGameDirService.Result<Boolean>> var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", "report");
      var4.addProperty("id", var1);
      var4.addProperty("reason", var2 == null ? "" : var2);
      var4.addProperty("reporter_key", this.voterKey());
      this.mf5pqh89mjp6(var4, var1x -> {
         if (!var1x.ok()) {
            var3.accept(CommunityForGameDirService.Result.error(var1x.error()));
         } else {
            var3.accept(CommunityForGameDirService.Result.ok(true));
         }
      });
   }

   public void delete(String var1, Consumer<CommunityForGameDirService.Result<Boolean>> var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty("action", "delete");
      var3.addProperty("id", var1);
      String var4 = this.mfb9nbtmxhsm(var1);
      var3.addProperty("delete_token", var4 == null ? "" : var4);
      this.mf5pqh89mjp6(var3, var3x -> {
         if (!var3x.ok()) {
            var2.accept(CommunityForGameDirService.Result.error(var3x.error()));
         } else {
            this.mfizcddj3lzo(var1);
            var2.accept(CommunityForGameDirService.Result.ok(true));
         }
      });
   }

   public String deleteToken(String var1) {
      return this.mfb9nbtmxhsm(var1);
   }

   static JsonObject withoutKeybinds(JsonObject var0) {
      JsonObject var1 = var0 == null ? new JsonObject() : var0.deepCopy();
      var1.remove("keybinds");
      return var1;
   }

   static CommunityForGameDirService.Page parsePage(JsonObject var0) {
      ArrayList var1 = new ArrayList();
      if (var0.has("items") && var0.get("items").isJsonArray()) {
         for (JsonElement var3 : var0.getAsJsonArray("items")) {
            if (var3.isJsonObject()) {
               var1.add(parseEntry(var3.getAsJsonObject()));
            }
         }
      }

      long var4 = var0.has("total") ? md0o0tlzjyzm(var0, "total") : var1.size();
      return new CommunityForGameDirService.Page(List.copyOf(var1), var4);
   }

   static CommunityForGameDirService.Entry parseEntry(JsonObject var0) {
      return new CommunityForGameDirService.Entry(
         milqb32l9tvp(var0, "id"),
         milqb32l9tvp(var0, "title"),
         milqb32l9tvp(var0, "description"),
         milqb32l9tvp(var0, "author_name"),
         milqb32l9tvp(var0, "ellice_version"),
         m6scyhah6uo9(var0, "upvotes"),
         m6scyhah6uo9(var0, "downvotes"),
         m6scyhah6uo9(var0, "score"),
         m6scyhah6uo9(var0, "downloads"),
         me0v3h7sb5un(milqb32l9tvp(var0, "created_at")),
         m6scyhah6uo9(var0, "my_vote"),
         m6scyhah6uo9(var0, "module_count"),
         m6scyhah6uo9(var0, "enabled_count"),
         m9uvw1hcv9ap(var0, "verified")
      );
   }

   private void mf5pqh89mjp6(JsonObject var1, Consumer<CommunityForGameDirService.Result<JsonObject>> var2) {
      String var3 = this.sessionJwt();
      String var4 = LicenseBaseUrlService.anonKey();
      CompletableFuture.runAsync(
         () -> {
            try {
               HttpRequest var5 = HttpRequest.newBuilder(URI.create(LicenseBaseUrlService.baseUrl() + "/functions/v1/community"))
                  .timeout(Duration.ofSeconds(20L))
                  .header("Content-Type", "application/json")
                  .header("Accept", "application/json")
                  .header("apikey", var4)
                  .header("Authorization", "Bearer " + (var3.isEmpty() ? var4 : var3))
                  .header("User-Agent", "ellice-Mod")
                  .POST(BodyPublishers.ofString(var1.toString(), StandardCharsets.UTF_8))
                  .build();
               HttpResponse var6 = this.fgmr64zfra8t.send(var5, BodyHandlers.ofInputStream());

               JsonObject var7;
               try (InputStream var8 = (InputStream)var6.body()) {
                  byte[] var9 = mdy5e685f86z(var8, 1048576L);
                  var7 = JsonParser.parseString(new String(var9, StandardCharsets.UTF_8)).getAsJsonObject();
               }

               if (var7.has("error") && !var7.get("error").getAsString().isBlank()) {
                  mcmrbard4o6b(var2, CommunityForGameDirService.Result.error(m5s0m7pnw506(var7.get("error").getAsString())));
                  return;
               }

               mcmrbard4o6b(var2, CommunityForGameDirService.Result.ok(var7));
            } catch (Exception var13) {
               mcmrbard4o6b(var2, CommunityForGameDirService.Result.error("Could not reach the community service. Check your connection."));
            }
         }
      );
   }

   private static <T> void mcmrbard4o6b(Consumer<CommunityForGameDirService.Result<T>> var0, CommunityForGameDirService.Result<T> var1) {
      Runnable var2 = () -> var0.accept(var1);

      try {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null) {
            var3.execute(var2);
            return;
         }
      } catch (Throwable var4) {
      }

      var2.run();
   }

   private String m162f123eynt() {
      try {
         Path var1 = this.f5hknee2o9oq.resolve("ellice").resolve(".community-id");
         if (Files.isRegularFile(var1)) {
            String var2 = Files.readString(var1, StandardCharsets.UTF_8).trim();
            if (var2.matches("[0-9a-f]{32}")) {
               return "anon_" + var2;
            }
         }

         String var4 = UUID.randomUUID().toString().replace("-", "");
         Files.createDirectories(var1.getParent());
         Files.writeString(var1, var4, StandardCharsets.UTF_8);
         return "anon_" + var4;
      } catch (Exception var3) {
         return "anon_fallback";
      }
   }

   private Map<String, String> mgknw8vpdujk() {
      try {
         Path var1 = this.mc2ok3alo5q0();
         if (!Files.isRegularFile(var1)) {
            return new LinkedHashMap<>();
         }

         if (f4syu93evwgr.fromJson(Files.readString(var1, StandardCharsets.UTF_8), (new TypeToken<Map<String, String>>() {}).getType()) instanceof Map var3) {
            LinkedHashMap var4 = new LinkedHashMap();

            for (Map.Entry var6 : (Iterable<Map.Entry>) (Iterable<?>) (var3.entrySet())) {
               if (var6.getKey() instanceof String var7 && var6.getValue() instanceof String var8) {
                  var4.put(var7, var8);
               }
            }

            return var4;
         }
      } catch (Exception var10) {
      }

      return new LinkedHashMap<>();
   }

   private void m5qg1o8usch0(String var1, String var2) {
      try {
         Map var3 = this.mgknw8vpdujk();
         var3.put(var1, var2);
         Files.createDirectories(this.mc2ok3alo5q0().getParent());
         Files.writeString(this.mc2ok3alo5q0(), f4syu93evwgr.toJson(var3), StandardCharsets.UTF_8);
      } catch (Exception var4) {
      }
   }

   private String mfb9nbtmxhsm(String var1) {
      return this.mgknw8vpdujk().get(var1);
   }

   private void mfizcddj3lzo(String var1) {
      try {
         Map var2 = this.mgknw8vpdujk();
         var2.remove(var1);
         Files.writeString(this.mc2ok3alo5q0(), f4syu93evwgr.toJson(var2), StandardCharsets.UTF_8);
      } catch (Exception var3) {
      }
   }

   private Path mc2ok3alo5q0() {
      return this.f5hknee2o9oq.resolve("ellice").resolve(".community-tokens.json");
   }

   private static byte[] mdy5e685f86z(InputStream var0, long var1) throws IOException {
      try (ByteArrayOutputStream var3 = new ByteArrayOutputStream()) {
         byte[] var4 = new byte[8192];
         long var5 = 0L;

         int var7;
         while ((var7 = var0.read(var4)) > 0) {
            var5 += var7;
            if (var5 > var1) {
               throw new IOException("response too large");
            }

            var3.write(var4, 0, var7);
         }

         return var3.toByteArray();
      }
   }

   private static String m5s0m7pnw506(String var0) {
      if (var0 != null && !var0.isBlank()) {
         String var1 = var0.trim();
         return var1.length() > 160 ? var1.substring(0, 160) : var1;
      } else {
         return "Community request failed.";
      }
   }

   private static String milqb32l9tvp(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() && var0.getAsJsonPrimitive(var1).isString() ? var0.get(var1).getAsString() : "";
   }

   private static int m6scyhah6uo9(JsonObject var0, String var1) {
      try {
         return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsInt() : 0;
      } catch (Exception var3) {
         return 0;
      }
   }

   private static long md0o0tlzjyzm(JsonObject var0, String var1) {
      try {
         return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsLong() : 0L;
      } catch (Exception var3) {
         return 0L;
      }
   }

   private static boolean m9uvw1hcv9ap(JsonObject var0, String var1) {
      try {
         return var0.has(var1) && !var0.get(var1).isJsonNull() && var0.get(var1).getAsBoolean();
      } catch (Exception var3) {
         return false;
      }
   }

   private static long me0v3h7sb5un(String var0) {
      try {
         return Instant.parse(var0).toEpochMilli();
      } catch (Exception var2) {
         return 0L;
      }
   }

   public record Download(CommunityForGameDirService.Entry meta, JsonObject config) {
   }

   public record Entry(
      String id,
      String title,
      String description,
      String author,
      String elliceVersion,
      int upvotes,
      int downvotes,
      int score,
      int downloads,
      long createdAtMs,
      int myVote,
      int moduleCount,
      int enabledCount,
      boolean verified
   ) {
   }

   public record Page(List<CommunityForGameDirService.Entry> items, long total) {
   }

   public record Result<T>(boolean ok, T value, String error) {
      public static <T> CommunityForGameDirService.Result<T> ok(T var0) {
         return new CommunityForGameDirService.Result<>(true, (T)var0, "");
      }

      public static <T> CommunityForGameDirService.Result<T> error(String var0) {
         return new CommunityForGameDirService.Result<>(false, null, var0);
      }
   }

   public record UploadResult(String id, String deleteToken) {
   }

   public record VoteResult(int upvotes, int downvotes, int score, int myVote) {
   }
}
