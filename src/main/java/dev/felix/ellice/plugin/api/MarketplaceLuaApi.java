package dev.felix.ellice.plugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public final class MarketplaceLuaApi implements ApiOperationHandler {
   private static final String ffojrsyxok2h = "https://api.pyrra.net";
   private static final Pattern ffr68lxtrzyy = Pattern.compile("^[A-Za-z0-9._-]{1,128}$");
   private static final long fiy758xyxc2q = 52428800L;
   private static final long fhhcclwbh43r = 209715200L;
   private static final int f2acz45ao1ik = 2000;
   private final HttpClient fcivsbyymmlp = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
   private final Path ffxvlg5wygh2;
   private final Path f3ianjseji5f;
   private final Path f1yf1dk5hoey;
   private String f8y2skafl1f5 = mbmeohi9efid();

   public MarketplaceLuaApi() {
      Path var1 = FabricLoader.getInstance().getGameDir();
      this.ffxvlg5wygh2 = var1.resolve("ellice-plugins");
      this.f3ianjseji5f = var1.resolve("ellice-setups");
      this.f1yf1dk5hoey = var1.resolve("ellice-themes");
   }

   @Override
   public void register(Globals var1) {
      LuaTable var2 = new LuaTable();
      var2.set("baseUrl", new ZeroArgFunction() {
         public LuaValue call() {
            return LuaValue.valueOf(MarketplaceLuaApi.this.f8y2skafl1f5);
         }
      });
      var2.set("elliceVersion", new ZeroArgFunction() {
         public LuaValue call() {
            return LuaValue.valueOf(CoreIsInitializedHandler.VERSION);
         }
      });
      var2.set("featuredSetups", new OneArgFunction() {
         public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/gallery/featured", var1);
            return LuaValue.NIL;
         }
      });
      var2.set("trendingSetups", new OneArgFunction() {
         public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/gallery/trending", var1);
            return LuaValue.NIL;
         }
      });
      var2.set("listSetups", new VarArgFunction() {
         public Varargs invoke(Varargs var1) {
            String var2x = var1.arg(1).isnil() ? "" : var1.arg(1).tojstring();
            String var3 = var1.arg(2).isnil() ? "" : var1.arg(2).tojstring();
            int var4 = var1.arg(3).isnil() ? 0 : Math.max(0, var1.arg(3).toint());
            LuaValue var5 = var1.arg(4);
            String var6 = "/v1/setups?page=" + var4 + "&size=24";
            if (!var2x.isBlank()) {
               var6 = var6 + "&query=" + MarketplaceLuaApi.mce9cvktfdfy(var2x);
            }

            if (!var3.isBlank() && !"ALL".equalsIgnoreCase(var3)) {
               var6 = var6 + "&category=" + MarketplaceLuaApi.mce9cvktfdfy(var3);
            }

            MarketplaceLuaApi.this.mbepvu93ovdb(var6, var5);
            return LuaValue.NIL;
         }
      });
      var2.set("listPlugins", new VarArgFunction() {
         public Varargs invoke(Varargs var1) {
            String var2 = var1.arg(1).isnil() ? "" : var1.arg(1).tojstring();
            int var3 = var1.arg(2).isnil() ? 0 : Math.max(0, var1.arg(2).toint());
            LuaValue var4 = var1.arg(3);
            String var5 = "/v1/plugins?page=" + var3 + "&size=30";
            if (!var2.isBlank()) {
               var5 = var5 + "&query=" + MarketplaceLuaApi.mce9cvktfdfy(var2);
            }

            MarketplaceLuaApi.this.mbepvu93ovdb(var5, var4);
            return LuaValue.NIL;
         }
      });
      var2.set("listThemes", new VarArgFunction() {
         public Varargs invoke(Varargs var1) {
            String var2 = var1.arg(1).isnil() ? "" : var1.arg(1).tojstring();
            int var3 = var1.arg(2).isnil() ? 0 : Math.max(0, var1.arg(2).toint());
            LuaValue var4 = var1.arg(3);
            String var5 = "/v1/themes?page=" + var3 + "&size=30";
            if (!var2.isBlank()) {
               var5 = var5 + "&query=" + MarketplaceLuaApi.mce9cvktfdfy(var2);
            }

            MarketplaceLuaApi.this.mbepvu93ovdb(var5, var4);
            return LuaValue.NIL;
         }
      });
      var2.set("featuredThemes", new OneArgFunction() {
         public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/themes/featured", var1);
            return LuaValue.NIL;
         }
      });
      var2.set("detail", new VarArgFunction() {
         public Varargs invoke(Varargs var1) {
            String var2 = MarketplaceLuaApi.m7d17bne92xv(var1.arg(1).tojstring());
            String var3 = var1.arg(2).tojstring();
            LuaValue var4 = var1.arg(3);
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/" + MarketplaceLuaApi.ma0gu4a02rkl(var2) + "/" + MarketplaceLuaApi.m7ovh0uxyniz(var3), var4);
            return LuaValue.NIL;
         }
      });
      var2.set("versions", new VarArgFunction() {
         public Varargs invoke(Varargs var1) {
            String var2 = MarketplaceLuaApi.m7d17bne92xv(var1.arg(1).tojstring());
            String var3 = var1.arg(2).tojstring();
            LuaValue var4 = var1.arg(3);
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/" + MarketplaceLuaApi.ma0gu4a02rkl(var2) + "/" + MarketplaceLuaApi.m7ovh0uxyniz(var3) + "/versions", var4);
            return LuaValue.NIL;
         }
      });
      var2.set("installSetup", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2x) {
            MarketplaceLuaApi.this.mhui3x4r0z0n(var1.tojstring(), var2x);
            return LuaValue.NIL;
         }
      });
      var2.set("installPlugin", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.mcqurg73a65p(var1.tojstring(), var2);
            return LuaValue.NIL;
         }
      });
      var2.set("installTheme", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.m53yqykuwiwy(var1.tojstring(), var2);
            return LuaValue.NIL;
         }
      });
      var2.set("installed", new ZeroArgFunction() {
         public LuaValue call() {
            return MarketplaceLuaApi.this.mdy3pcgvy500();
         }
      });
      var2.set("uninstall", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2) {
            String var3 = var1.tojstring();
            Minecraft.getInstance().execute(() -> {
               boolean var2x = CoreIsInitializedHandler.get().pluginLoader().unloadPlugin(var3);
               LuaTable var3x = MarketplaceLuaApi.metzaa184qay(var2x, var2x ? "Plugin removed" : "Plugin not found");
               var3x.set("id", LuaValue.valueOf(var3));
               MarketplaceLuaApi.mehnyczwqliw(var2, var3x);
            });
            return LuaValue.NIL;
         }
      });
      var2.set("list", new OneArgFunction() {
         public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/plugins?page=0&size=30", var1);
            return LuaValue.NIL;
         }
      });
      var2.set("search", new TwoArgFunction() {
         public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.mbepvu93ovdb("/v1/plugins?query=" + MarketplaceLuaApi.mce9cvktfdfy(var1.tojstring()) + "&page=0&size=30", var2);
            return LuaValue.NIL;
         }
      });
      var1.set("marketplace", var2);
   }

   private void mcqurg73a65p(String var1, LuaValue var2) {
      if (!m5eer6plk4dw(var1)) {
         mb30ktnd7j3(var2, metzaa184qay(false, "Invalid plugin id"));
      } else {
         CompletableFuture.runAsync(
            () -> {
               try {
                  JsonObject var3 = this.m1b7nuixshf3(
                     "/v1/plugins/" + m7ovh0uxyniz(var1) + "/versions/latest?elliceVersion=" + mce9cvktfdfy(CoreIsInitializedHandler.VERSION)
                  );
                  String var4 = m5sc6xah6s39(var3, "version", "?");
                  List var5 = m9ycv2cmgt63(var3.get("permissions"));
                  this.m9u6zrbynhcm(var1, var3);
                  LuaTable var6 = metzaa184qay(true, "Installed " + var1 + " " + var4);
                  var6.set("slug", LuaValue.valueOf(var1));
                  var6.set("version", LuaValue.valueOf(var4));
                  var6.set("permissions", mikt91uziinb(var5));
                  this.mdculhvdaxrc(List.of(var1), var2, var6);
               } catch (Exception var7) {
                  CoreIsInitializedHandler.LOGGER.warn("Marketplace: plugin install failed for {}", var1, var7);
                  mb30ktnd7j3(var2, metzaa184qay(false, m182g4oji4mn(var7)));
               }
            }
         );
      }
   }

   private void m53yqykuwiwy(String var1, LuaValue var2) {
      if (!m5eer6plk4dw(var1)) {
         mb30ktnd7j3(var2, metzaa184qay(false, "Invalid theme id"));
      } else {
         CompletableFuture.runAsync(() -> {
            try {
               JsonObject var3 = this.m1b7nuixshf3("/v1/themes/" + m7ovh0uxyniz(var1) + "/versions/latest");
               JsonObject var4 = new JsonObject();
               var4.addProperty("url", m5sc6xah6s39(var3, "fileUrl", ""));
               var4.addProperty("sha256", m5sc6xah6s39(var3, "sha256", ""));
               var4.addProperty("size", mh0ixvlh7qj(var3, "fileSize", 0L));
               this.mejs0ddyfc9w(var1, var4, this.f1yf1dk5hoey.resolve(var1), true);
               LuaTable var5 = metzaa184qay(true, "Downloaded theme " + var1);
               var5.set("slug", LuaValue.valueOf(var1));
               var5.set("version", LuaValue.valueOf(m5sc6xah6s39(var3, "version", "?")));
               mb30ktnd7j3(var2, var5);
            } catch (Exception var6) {
               CoreIsInitializedHandler.LOGGER.warn("Marketplace: theme install failed for {}", var1, var6);
               mb30ktnd7j3(var2, metzaa184qay(false, m182g4oji4mn(var6)));
            }
         });
      }
   }

   private void mhui3x4r0z0n(String var1, LuaValue var2) {
      if (!m5eer6plk4dw(var1)) {
         mb30ktnd7j3(var2, metzaa184qay(false, "Invalid setup id"));
      } else {
         CompletableFuture.runAsync(
            () -> {
               try {
                  JsonObject var3 = this.m1b7nuixshf3(
                     "/v1/setups/" + m7ovh0uxyniz(var1) + "/install?elliceVersion=" + mce9cvktfdfy(CoreIsInitializedHandler.VERSION)
                  );
                  String var4 = m5sc6xah6s39(var3, "name", var1);
                  JsonObject var5 = mb7xf7k8x6m7(var3, "file");
                  if (var5 != null && !m5sc6xah6s39(var5, "url", "").isBlank()) {
                     Path var6 = this.f3ianjseji5f.resolve(var1);
                     this.mejs0ddyfc9w(var1, var5, var6, true);
                     Files.writeString(
                        var6.resolve("install-manifest.json"),
                        var3.toString(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                     );
                  }

                  ArrayList var18 = new ArrayList();
                  List var7 = m9ycv2cmgt63(var3.get("warnings"));
                  int var8 = 0;
                  int var9 = 0;
                  JsonArray var10 = m285l92p8wug(var3, "plugins");
                  if (var10 != null) {
                     for (JsonElement var12 : var10) {
                        if (var12.isJsonObject()) {
                           JsonObject var13 = var12.getAsJsonObject();
                           String var14 = m5sc6xah6s39(var13, "id", "");
                           boolean var15 = m8ijzeiasl0j(var13, "skipped", false);
                           if (var15) {
                              var9++;
                              String var16 = m5sc6xah6s39(var13, "reason", "Skipped");
                              if (!var14.isBlank()) {
                                 var7.add(var14 + ": " + var16);
                              }
                           } else if (!var14.isBlank()) {
                              if (!m5eer6plk4dw(var14)) {
                                 var7.add(var14 + ": invalid id, skipped");
                                 var9++;
                              } else {
                                 this.mcpw69bj69jb(var14, var13);
                                 var18.add(var14);
                                 var8++;
                              }
                           }
                        }
                     }
                  }

                  LuaTable var19 = metzaa184qay(true, "Installed setup " + var4);
                  var19.set("slug", LuaValue.valueOf(var1));
                  var19.set("name", LuaValue.valueOf(var4));
                  var19.set("version", LuaValue.valueOf(m5sc6xah6s39(var3, "version", "?")));
                  var19.set("installedPlugins", LuaValue.valueOf(var8));
                  var19.set("skippedPlugins", LuaValue.valueOf(var9));
                  var19.set("warnings", mikt91uziinb(var7));
                  this.mdculhvdaxrc(var18, var2, var19);
               } catch (Exception var17) {
                  CoreIsInitializedHandler.LOGGER.warn("Marketplace: setup install failed for {}", var1, var17);
                  mb30ktnd7j3(var2, metzaa184qay(false, m182g4oji4mn(var17)));
               }
            }
         );
      }
   }

   private void m9u6zrbynhcm(String var1, JsonObject var2) throws Exception {
      this.mcpw69bj69jb(var1, var2);
   }

   private void mcpw69bj69jb(String var1, JsonObject var2) throws Exception {
      JsonObject var3 = new JsonObject();
      var3.addProperty("url", m5sc6xah6s39(var2, "url", m5sc6xah6s39(var2, "fileUrl", "")));
      var3.addProperty("sha256", m5sc6xah6s39(var2, "sha256", ""));
      var3.addProperty("size", mh0ixvlh7qj(var2, "size", mh0ixvlh7qj(var2, "fileSize", 0L)));
      this.mejs0ddyfc9w(var1, var3, this.ffxvlg5wygh2.resolve(var1), true);
   }

   private void mejs0ddyfc9w(String var1, JsonObject var2, Path var3, boolean var4) throws Exception {
      String var5 = m5sc6xah6s39(var2, "url", "");
      if (var5.isBlank()) {
         throw new IllegalArgumentException("missing download URL for " + var1);
      }

      String var6 = m5sc6xah6s39(var2, "sha256", "");
      if (!var6.isBlank() && var6.length() >= 64) {
         byte[] var7 = this.mdv31ftwd5sy(var5);
         String var8 = m1ep6ras9rsu(var7);
         if (!var8.equalsIgnoreCase(var6)) {
            throw new IllegalStateException("SHA256 mismatch for " + var1);
         }

         if (var4 && Files.isDirectory(var3)) {
            m29sj0dq1m0h(var3);
         }

         mcql2icbw45c(var7, var3);
      } else {
         throw new IllegalStateException("missing SHA256 for " + var1);
      }
   }

   private void mdculhvdaxrc(List<String> var1, LuaValue var2, LuaTable var3) {
      Minecraft.getInstance().execute(() -> {
         for (String var4 : var1) {
            try {
               CoreIsInitializedHandler.get().pluginLoader().loadNewPlugin(var4);
            } catch (Exception var6) {
               CoreIsInitializedHandler.LOGGER.warn("Marketplace: failed to hot-load plugin {}", var4, var6);
            }
         }

         mehnyczwqliw(var2, var3);
      });
   }

   private LuaTable mdy3pcgvy500() {
      LuaTable var1 = new LuaTable();

      try {
         if (!Files.isDirectory(this.ffxvlg5wygh2)) {
            return var1;
         }

         int var2 = 1;

         try (DirectoryStream var3 = Files.newDirectoryStream(this.ffxvlg5wygh2, var0 -> Files.isDirectory(var0))) {
            for (Path var5 : (Iterable<Path>) (Iterable<?>) (var3)) {
               Path var6 = var5.resolve("plugin.json");
               if (Files.exists(var6)) {
                  try {
                     JsonObject var7 = JsonParser.parseString(Files.readString(var6)).getAsJsonObject();
                     LuaTable var8 = new LuaTable();
                     String var9 = m5sc6xah6s39(var7, "id", var5.getFileName().toString());
                     var8.set("id", LuaValue.valueOf(var9));
                     var8.set("slug", LuaValue.valueOf(var5.getFileName().toString()));
                     var8.set("name", LuaValue.valueOf(m5sc6xah6s39(var7, "name", var9)));
                     var8.set("version", LuaValue.valueOf(m5sc6xah6s39(var7, "version", "?")));
                     var8.set("author", LuaValue.valueOf(m5sc6xah6s39(var7, "author", "")));
                     var8.set("description", LuaValue.valueOf(m5sc6xah6s39(var7, "description", "")));
                     var1.set(var2++, var8);
                  } catch (Exception var11) {
                  }
               }
            }
         }
      } catch (Exception var13) {
         CoreIsInitializedHandler.LOGGER.debug("Marketplace: failed to list installed plugins", var13);
      }

      return var1;
   }

   private void mbepvu93ovdb(String var1, LuaValue var2) {
      CompletableFuture.runAsync(() -> {
         try {
            mb30ktnd7j3(var2, m3zi7vbb7av8(this.m7m3bobotu9q(var1)));
         } catch (Exception var4) {
            CoreIsInitializedHandler.LOGGER.debug("Marketplace: fetch failed for {}", var1, var4);
            mb30ktnd7j3(var2, LuaValue.NIL);
         }
      });
   }

   private JsonElement m7m3bobotu9q(String var1) throws Exception {
      HttpResponse var2 = this.fcivsbyymmlp.send(this.mfcu9pf6quvm(var1).GET().build(), BodyHandlers.ofString());
      if (var2.statusCode() >= 400) {
         throw this.mhnt2sz9fvjf(var2);
      } else {
         return JsonParser.parseString((String)var2.body());
      }
   }

   private JsonObject m1b7nuixshf3(String var1) throws Exception {
      JsonElement var2 = this.m7m3bobotu9q(var1);
      if (!var2.isJsonObject()) {
         throw new IllegalStateException("Expected JSON object from " + var1);
      } else {
         return var2.getAsJsonObject();
      }
   }

   private byte[] mdv31ftwd5sy(String var1) throws Exception {
      HttpRequest var2 = HttpRequest.newBuilder(this.mcyug02ui9ny(var1)).timeout(Duration.ofSeconds(45L)).GET().build();
      HttpResponse var3 = this.fcivsbyymmlp.send(var2, BodyHandlers.ofInputStream());
      if (var3.statusCode() >= 400) {
         InputStream var15 = (InputStream)var3.body();
         if (var15 != null) {
            var15.close();
         }

         throw new RuntimeException("HTTP " + var3.statusCode());
      } else {
         try (
            InputStream var4 = (InputStream)var3.body();
            ByteArrayOutputStream var5 = new ByteArrayOutputStream();
         ) {
            byte[] var6 = new byte[8192];
            long var7 = 0L;

            int var9;
            while ((var9 = var4.read(var6)) > 0) {
               var7 += var9;
               if (var7 > 52428800L) {
                  throw new IllegalStateException("download exceeds 52428800 bytes");
               }

               var5.write(var6, 0, var9);
            }

            return var5.toByteArray();
         }
      }
   }

   private Builder mfcu9pf6quvm(String var1) {
      return HttpRequest.newBuilder(this.md0yng6nb3g6(var1)).timeout(Duration.ofSeconds(15L)).header("Accept", "application/json");
   }

   private URI md0yng6nb3g6(String var1) {
      return URI.create(this.f8y2skafl1f5 + var1);
   }

   private URI mcyug02ui9ny(String var1) {
      if (var1.startsWith("https://")) {
         return URI.create(var1);
      } else if (var1.startsWith("http://")) {
         throw new SecurityException("plain http:// download refused: " + var1);
      } else {
         return URI.create(this.f8y2skafl1f5 + (var1.startsWith("/") ? var1 : "/" + var1));
      }
   }

   private RuntimeException mhnt2sz9fvjf(HttpResponse<String> var1) {
      try {
         JsonObject var2 = JsonParser.parseString((String)var1.body()).getAsJsonObject();
         String var3 = m5sc6xah6s39(var2, "code", "HTTP_" + var1.statusCode());
         String var4 = m5sc6xah6s39(var2, "message", "HTTP " + var1.statusCode());
         return new RuntimeException(var3 + ": " + var4);
      } catch (Exception var5) {
         return new RuntimeException("HTTP " + var1.statusCode());
      }
   }

   private static void mcql2icbw45c(byte[] var0, Path var1) throws Exception {
      Files.createDirectories(var1);
      Path var2 = var1.toAbsolutePath().normalize();
      long var3 = 0L;
      int var5 = 0;

      try (ZipInputStream var6 = new ZipInputStream(new ByteArrayInputStream(var0))) {
         byte[] var8 = new byte[8192];

         ZipEntry var7;
         while ((var7 = var6.getNextEntry()) != null) {
            if (++var5 > 2000) {
               throw new SecurityException("Zip has too many entries (>2000)");
            }

            String var9 = var7.getName();
            if (var9.indexOf(92) >= 0 || var9.indexOf(0) >= 0) {
               throw new SecurityException("Invalid zip entry name: " + var9);
            }

            Path var10 = var1.resolve(var9).toAbsolutePath().normalize();
            if (!var10.startsWith(var2)) {
               throw new SecurityException("Zip slip: " + var9);
            }

            if (var7.isDirectory()) {
               Files.createDirectories(var10);
            } else {
               Files.createDirectories(var10.getParent());

               int var12;
               try (OutputStream var11 = Files.newOutputStream(var10, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                  while ((var12 = var6.read(var8)) > 0) {
                     var3 += var12;
                     if (var3 > 209715200L) {
                        throw new SecurityException("Extracted size exceeds 209715200 bytes");
                     }

                     var11.write(var8, 0, var12);
                  }
               }
            }
         }
      }
   }

   private static void m29sj0dq1m0h(Path var0) throws Exception {
       try (Stream<Path> var1 = Files.walk(var0)) {
         var1.sorted(Comparator.reverseOrder()).forEach(var0x -> {
            try {
               Files.deleteIfExists(var0x);
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         });
      }
   }

   private static void mb30ktnd7j3(LuaValue var0, LuaValue var1) {
      if (!var0.isnil() && var0.isfunction()) {
         Minecraft.getInstance().execute(() -> mehnyczwqliw(var0, var1));
      }
   }

   private static void mehnyczwqliw(LuaValue var0, LuaValue var1) {
      if (!var0.isnil() && var0.isfunction()) {
         try {
            var0.call(var1);
         } catch (LuaError var3) {
            CoreIsInitializedHandler.LOGGER.error("Marketplace callback error: {}", var3.getMessage());
         }
      }
   }

   private static LuaTable metzaa184qay(boolean var0, String var1) {
      LuaTable var2 = new LuaTable();
      var2.set("ok", LuaValue.valueOf(var0));
      var2.set("message", LuaValue.valueOf(var1 == null ? "" : var1));
      return var2;
   }

   private static LuaValue m3zi7vbb7av8(JsonElement var0) {
      if (var0 == null || var0.isJsonNull()) {
         return LuaValue.NIL;
      }

      if (var0.isJsonPrimitive()) {
         JsonPrimitive var6 = var0.getAsJsonPrimitive();
         if (var6.isBoolean()) {
            return LuaValue.valueOf(var6.getAsBoolean());
         } else {
            return (LuaValue)(var6.isNumber() ? LuaValue.valueOf(var6.getAsDouble()) : LuaValue.valueOf(var6.getAsString()));
         }
      } else if (var0.isJsonArray()) {
         JsonArray var5 = var0.getAsJsonArray();
         LuaTable var7 = new LuaTable();

         for (int var8 = 0; var8 < var5.size(); var8++) {
            var7.set(var8 + 1, m3zi7vbb7av8(var5.get(var8)));
         }

         return var7;
      } else {
         if (!var0.isJsonObject()) {
            return LuaValue.NIL;
         }

         JsonObject var1 = var0.getAsJsonObject();
         LuaTable var2 = new LuaTable();

         for (Entry var4 : var1.entrySet()) {
            var2.set((String)var4.getKey(), m3zi7vbb7av8((JsonElement)var4.getValue()));
         }

         return var2;
      }
   }

   private static LuaTable mikt91uziinb(List<String> var0) {
      LuaTable var1 = new LuaTable();

      for (int var2 = 0; var2 < var0.size(); var2++) {
         var1.set(var2 + 1, LuaValue.valueOf((String)var0.get(var2)));
      }

      return var1;
   }

   private static List<String> m9ycv2cmgt63(JsonElement var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null && var0.isJsonArray()) {
         for (JsonElement var3 : var0.getAsJsonArray()) {
            if (!var3.isJsonNull()) {
               var1.add(var3.getAsString());
            }
         }
      }

      return var1;
   }

   private static String m1ep6ras9rsu(byte[] var0) throws Exception {
      byte[] var1 = MessageDigest.getInstance("SHA-256").digest(var0);
      return HexFormat.of().formatHex(var1);
   }

   private static boolean m5eer6plk4dw(String var0) {
      return var0 != null && ffr68lxtrzyy.matcher(var0).matches();
   }

   private static String m7d17bne92xv(String var0) {
      String var1 = var0 == null ? "" : var0.toLowerCase().trim();
      if (var1.startsWith("theme")) {
         return "theme";
      } else {
         return !var1.startsWith("setup") && !var1.startsWith("gallery") ? "plugin" : "setup";
      }
   }

   private static String ma0gu4a02rkl(String var0) {
      return switch (var0) {
         case "theme" -> "themes";
         case "setup" -> "setups";
         default -> "plugins";
      };
   }

   private static String m182g4oji4mn(Throwable var0) {
      String var1 = var0.getMessage();
      if (var1 != null && !var1.isBlank()) {
         return var1.length() > 180 ? var1.substring(0, 180) : var1;
      } else {
         return "Marketplace request failed";
      }
   }

   private static JsonObject mb7xf7k8x6m7(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonObject() ? var0.getAsJsonObject(var1) : null;
   }

   private static JsonArray m285l92p8wug(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonArray() ? var0.getAsJsonArray(var1) : null;
   }

   private static String m5sc6xah6s39(JsonObject var0, String var1, String var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsString() : var2;
   }

   private static boolean m8ijzeiasl0j(JsonObject var0, String var1, boolean var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsBoolean() : var2;
   }

   private static long mh0ixvlh7qj(JsonObject var0, String var1, long var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsLong() : var2;
   }

   private static String mce9cvktfdfy(String var0) {
      return URLEncoder.encode(var0 == null ? "" : var0, StandardCharsets.UTF_8);
   }

   private static String m7ovh0uxyniz(String var0) {
      return mce9cvktfdfy(var0).replace("+", "%20");
   }

   private static String mbmeohi9efid() {
      String var0 = System.getProperty("ellice.api.baseUrl");
      if (var0 != null && !var0.isBlank()) {
         return m7pcwvb23lkq(var0);
      }

      String var1 = System.getenv("ELLICE_API_BASE_URL");
      return var1 != null && !var1.isBlank() ? m7pcwvb23lkq(var1) : "https://api.pyrra.net";
   }

   private static String m7pcwvb23lkq(String var0) {
      String var1 = var0 != null && !var0.isBlank() ? var0.trim() : "https://api.pyrra.net";

      while (var1.endsWith("/")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      if (var1.endsWith("/api")) {
         var1 = var1.substring(0, var1.length() - 4);
      }

      return var1;
   }
}
