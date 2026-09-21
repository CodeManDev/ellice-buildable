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
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
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
  private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9._-]{1,128}$");
  private static final long timestamp = 52428800L;
  private static final long timestamp2 = 209715200L;
  private static final int count = 2000;
  private final HttpClient fcivsbyymmlp =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();
  private final Path path;
  private final Path path2;
  private final Path path3;
  private String text2 = mbmeohi9efid();

  public MarketplaceLuaApi() {
    Path var1 = FabricLoader.getInstance().getGameDir();
    this.path = var1.resolve("ellice-plugins");
    this.path2 = var1.resolve("ellice-setups");
    this.path3 = var1.resolve("ellice-themes");
  }

  @Override
  public void register(Globals var1) {
    LuaTable var2 = new LuaTable();
    var2.set(
        "baseUrl",
        new ZeroArgFunction() {
          public LuaValue call() {
            return LuaValue.valueOf(MarketplaceLuaApi.this.text2);
          }
        });
    var2.set(
        "elliceVersion",
        new ZeroArgFunction() {
          public LuaValue call() {
            return LuaValue.valueOf(CoreIsInitializedHandler.VERSION);
          }
        });
    var2.set(
        "featuredSetups",
        new OneArgFunction() {
          public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.updateState8("/v1/gallery/featured", var1);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "trendingSetups",
        new OneArgFunction() {
          public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.updateState8("/v1/gallery/trending", var1);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "listSetups",
        new VarArgFunction() {
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

            MarketplaceLuaApi.this.updateState8(var6, var5);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "listPlugins",
        new VarArgFunction() {
          public Varargs invoke(Varargs var1) {
            String var2 = var1.arg(1).isnil() ? "" : var1.arg(1).tojstring();
            int var3 = var1.arg(2).isnil() ? 0 : Math.max(0, var1.arg(2).toint());
            LuaValue var4 = var1.arg(3);
            String var5 = "/v1/plugins?page=" + var3 + "&size=30";
            if (!var2.isBlank()) {
              var5 = var5 + "&query=" + MarketplaceLuaApi.mce9cvktfdfy(var2);
            }

            MarketplaceLuaApi.this.updateState8(var5, var4);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "listThemes",
        new VarArgFunction() {
          public Varargs invoke(Varargs var1) {
            String var2 = var1.arg(1).isnil() ? "" : var1.arg(1).tojstring();
            int var3 = var1.arg(2).isnil() ? 0 : Math.max(0, var1.arg(2).toint());
            LuaValue var4 = var1.arg(3);
            String var5 = "/v1/themes?page=" + var3 + "&size=30";
            if (!var2.isBlank()) {
              var5 = var5 + "&query=" + MarketplaceLuaApi.mce9cvktfdfy(var2);
            }

            MarketplaceLuaApi.this.updateState8(var5, var4);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "featuredThemes",
        new OneArgFunction() {
          public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.updateState8("/v1/themes/featured", var1);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "detail",
        new VarArgFunction() {
          public Varargs invoke(Varargs var1) {
            String var2 = MarketplaceLuaApi.createText3(var1.arg(1).tojstring());
            String var3 = var1.arg(2).tojstring();
            LuaValue var4 = var1.arg(3);
            MarketplaceLuaApi.this.updateState8(
                "/v1/"
                    + MarketplaceLuaApi.createText4(var2)
                    + "/"
                    + MarketplaceLuaApi.createText8(var3),
                var4);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "versions",
        new VarArgFunction() {
          public Varargs invoke(Varargs var1) {
            String var2 = MarketplaceLuaApi.createText3(var1.arg(1).tojstring());
            String var3 = var1.arg(2).tojstring();
            LuaValue var4 = var1.arg(3);
            MarketplaceLuaApi.this.updateState8(
                "/v1/"
                    + MarketplaceLuaApi.createText4(var2)
                    + "/"
                    + MarketplaceLuaApi.createText8(var3)
                    + "/versions",
                var4);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "installSetup",
        new TwoArgFunction() {
          public LuaValue call(LuaValue var1, LuaValue var2x) {
            MarketplaceLuaApi.this.updateState3(var1.tojstring(), var2x);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "installPlugin",
        new TwoArgFunction() {
          public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.updateState(var1.tojstring(), var2);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "installTheme",
        new TwoArgFunction() {
          public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.updateState2(var1.tojstring(), var2);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "installed",
        new ZeroArgFunction() {
          public LuaValue call() {
            return MarketplaceLuaApi.this.createLuaTable();
          }
        });
    var2.set(
        "uninstall",
        new TwoArgFunction() {
          public LuaValue call(LuaValue var1, LuaValue var2) {
            String var3 = var1.tojstring();
            Minecraft.getInstance()
                .execute(
                    () -> {
                      boolean var2x =
                          CoreIsInitializedHandler.get().pluginLoader().unloadPlugin(var3);
                      LuaTable var3x =
                          MarketplaceLuaApi.createLuaTable2(
                              var2x, var2x ? "Plugin removed" : "Plugin not found");
                      var3x.set("id", LuaValue.valueOf(var3));
                      MarketplaceLuaApi.mehnyczwqliw(var2, var3x);
                    });
            return LuaValue.NIL;
          }
        });
    var2.set(
        "list",
        new OneArgFunction() {
          public LuaValue call(LuaValue var1) {
            MarketplaceLuaApi.this.updateState8("/v1/plugins?page=0&size=30", var1);
            return LuaValue.NIL;
          }
        });
    var2.set(
        "search",
        new TwoArgFunction() {
          public LuaValue call(LuaValue var1, LuaValue var2) {
            MarketplaceLuaApi.this.updateState8(
                "/v1/plugins?query="
                    + MarketplaceLuaApi.mce9cvktfdfy(var1.tojstring())
                    + "&page=0&size=30",
                var2);
            return LuaValue.NIL;
          }
        });
    var1.set("marketplace", var2);
  }

  private void updateState(String var1, LuaValue var2) {
    if (!checkCondition(var1)) {
      mb30ktnd7j3(var2, createLuaTable2(false, "Invalid plugin id"));
    } else {
      CompletableFuture.runAsync(
          () -> {
            try {
              JsonObject var3 =
                  this.createJsonObject(
                      "/v1/plugins/"
                          + createText8(var1)
                          + "/versions/latest?elliceVersion="
                          + mce9cvktfdfy(CoreIsInitializedHandler.VERSION));
              String var4 = createText6(var3, "version", "?");
              List var5 = createText(var3.get("permissions"));
              this.updateState4(var1, var3);
              LuaTable var6 = createLuaTable2(true, "Installed " + var1 + " " + var4);
              var6.set("slug", LuaValue.valueOf(var1));
              var6.set("version", LuaValue.valueOf(var4));
              var6.set("permissions", createLuaTable3(var5));
              this.mdculhvdaxrc(List.of(var1), var2, var6);
            } catch (Exception var7) {
              CoreIsInitializedHandler.LOGGER.warn(
                  "Marketplace: plugin install failed for {}", var1, var7);
              mb30ktnd7j3(var2, createLuaTable2(false, createText5(var7)));
            }
          });
    }
  }

  private void updateState2(String var1, LuaValue var2) {
    if (!checkCondition(var1)) {
      mb30ktnd7j3(var2, createLuaTable2(false, "Invalid theme id"));
    } else {
      CompletableFuture.runAsync(
          () -> {
            try {
              JsonObject var3 =
                  this.createJsonObject("/v1/themes/" + createText8(var1) + "/versions/latest");
              JsonObject var4 = new JsonObject();
              var4.addProperty("url", createText6(var3, "fileUrl", ""));
              var4.addProperty("sha256", createText6(var3, "sha256", ""));
              var4.addProperty("size", mh0ixvlh7qj(var3, "fileSize", 0L));
              this.updateState6(var1, var4, this.path3.resolve(var1), true);
              LuaTable var5 = createLuaTable2(true, "Downloaded theme " + var1);
              var5.set("slug", LuaValue.valueOf(var1));
              var5.set("version", LuaValue.valueOf(createText6(var3, "version", "?")));
              mb30ktnd7j3(var2, var5);
            } catch (Exception var6) {
              CoreIsInitializedHandler.LOGGER.warn(
                  "Marketplace: theme install failed for {}", var1, var6);
              mb30ktnd7j3(var2, createLuaTable2(false, createText5(var6)));
            }
          });
    }
  }

  private void updateState3(String var1, LuaValue var2) {
    if (!checkCondition(var1)) {
      mb30ktnd7j3(var2, createLuaTable2(false, "Invalid setup id"));
    } else {
      CompletableFuture.runAsync(
          () -> {
            try {
              JsonObject var3 =
                  this.createJsonObject(
                      "/v1/setups/"
                          + createText8(var1)
                          + "/install?elliceVersion="
                          + mce9cvktfdfy(CoreIsInitializedHandler.VERSION));
              String var4 = createText6(var3, "name", var1);
              JsonObject var5 = createJsonObject2(var3, "file");
              if (var5 != null && !createText6(var5, "url", "").isBlank()) {
                Path var6 = this.path2.resolve(var1);
                this.updateState6(var1, var5, var6, true);
                Files.writeString(
                    var6.resolve("install-manifest.json"),
                    var3.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
              }

              ArrayList var18 = new ArrayList();
              List var7 = createText(var3.get("warnings"));
              int var8 = 0;
              int var9 = 0;
              JsonArray var10 = createJsonArray(var3, "plugins");
              if (var10 != null) {
                for (JsonElement var12 : var10) {
                  if (var12.isJsonObject()) {
                    JsonObject var13 = var12.getAsJsonObject();
                    String var14 = createText6(var13, "id", "");
                    boolean var15 = checkCondition2(var13, "skipped", false);
                    if (var15) {
                      var9++;
                      String var16 = createText6(var13, "reason", "Skipped");
                      if (!var14.isBlank()) {
                        var7.add(var14 + ": " + var16);
                      }
                    } else if (!var14.isBlank()) {
                      if (!checkCondition(var14)) {
                        var7.add(var14 + ": invalid id, skipped");
                        var9++;
                      } else {
                        this.updateState5(var14, var13);
                        var18.add(var14);
                        var8++;
                      }
                    }
                  }
                }
              }

              LuaTable var19 = createLuaTable2(true, "Installed setup " + var4);
              var19.set("slug", LuaValue.valueOf(var1));
              var19.set("name", LuaValue.valueOf(var4));
              var19.set("version", LuaValue.valueOf(createText6(var3, "version", "?")));
              var19.set("installedPlugins", LuaValue.valueOf(var8));
              var19.set("skippedPlugins", LuaValue.valueOf(var9));
              var19.set("warnings", createLuaTable3(var7));
              this.mdculhvdaxrc(var18, var2, var19);
            } catch (Exception var17) {
              CoreIsInitializedHandler.LOGGER.warn(
                  "Marketplace: setup install failed for {}", var1, var17);
              mb30ktnd7j3(var2, createLuaTable2(false, createText5(var17)));
            }
          });
    }
  }

  private void updateState4(String var1, JsonObject var2) throws Exception {
    this.updateState5(var1, var2);
  }

  private void updateState5(String var1, JsonObject var2) throws Exception {
    JsonObject var3 = new JsonObject();
    var3.addProperty("url", createText6(var2, "url", createText6(var2, "fileUrl", "")));
    var3.addProperty("sha256", createText6(var2, "sha256", ""));
    var3.addProperty("size", mh0ixvlh7qj(var2, "size", mh0ixvlh7qj(var2, "fileSize", 0L)));
    this.updateState6(var1, var3, this.path.resolve(var1), true);
  }

  private void updateState6(String var1, JsonObject var2, Path var3, boolean var4)
      throws Exception {
    String var5 = createText6(var2, "url", "");
    if (var5.isBlank()) {
      throw new IllegalArgumentException("missing download URL for " + var1);
    }

    String var6 = createText6(var2, "sha256", "");
    if (!var6.isBlank() && var6.length() >= 64) {
      byte[] var7 = this.createByte(var5);
      String var8 = createText2(var7);
      if (!var8.equalsIgnoreCase(var6)) {
        throw new IllegalStateException("SHA256 mismatch for " + var1);
      }

      if (var4 && Files.isDirectory(var3)) {
        updateState10(var3);
      }

      updateState9(var7, var3);
    } else {
      throw new IllegalStateException("missing SHA256 for " + var1);
    }
  }

  private void mdculhvdaxrc(List<String> var1, LuaValue var2, LuaTable var3) {
    Minecraft.getInstance()
        .execute(
            () -> {
              for (String var4 : var1) {
                try {
                  CoreIsInitializedHandler.get().pluginLoader().loadNewPlugin(var4);
                } catch (Exception var6) {
                  CoreIsInitializedHandler.LOGGER.warn(
                      "Marketplace: failed to hot-load plugin {}", var4, var6);
                }
              }

              mehnyczwqliw(var2, var3);
            });
  }

  private LuaTable createLuaTable() {
    LuaTable var1 = new LuaTable();

    try {
      if (!Files.isDirectory(this.path)) {
        return var1;
      }

      int var2 = 1;

      try (DirectoryStream var3 =
          Files.newDirectoryStream(this.path, var0 -> Files.isDirectory(var0))) {
        for (Path var5 : (Iterable<Path>) (Iterable<?>) (var3)) {
          Path var6 = var5.resolve("plugin.json");
          if (Files.exists(var6)) {
            try {
              JsonObject var7 = JsonParser.parseString(Files.readString(var6)).getAsJsonObject();
              LuaTable var8 = new LuaTable();
              String var9 = createText6(var7, "id", var5.getFileName().toString());
              var8.set("id", LuaValue.valueOf(var9));
              var8.set("slug", LuaValue.valueOf(var5.getFileName().toString()));
              var8.set("name", LuaValue.valueOf(createText6(var7, "name", var9)));
              var8.set("version", LuaValue.valueOf(createText6(var7, "version", "?")));
              var8.set("author", LuaValue.valueOf(createText6(var7, "author", "")));
              var8.set("description", LuaValue.valueOf(createText6(var7, "description", "")));
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

  private void updateState8(String var1, LuaValue var2) {
    CompletableFuture.runAsync(
        () -> {
          try {
            mb30ktnd7j3(var2, createLuaValue(this.createJsonElement(var1)));
          } catch (Exception var4) {
            CoreIsInitializedHandler.LOGGER.debug("Marketplace: fetch failed for {}", var1, var4);
            mb30ktnd7j3(var2, LuaValue.NIL);
          }
        });
  }

  private JsonElement createJsonElement(String var1) throws Exception {
    HttpResponse var2 =
        this.fcivsbyymmlp.send(this.createBuilder(var1).GET().build(), BodyHandlers.ofString());
    if (var2.statusCode() >= 400) {
      throw this.createRuntimeException(var2);
    } else {
      return JsonParser.parseString((String) var2.body());
    }
  }

  private JsonObject createJsonObject(String var1) throws Exception {
    JsonElement var2 = this.createJsonElement(var1);
    if (!var2.isJsonObject()) {
      throw new IllegalStateException("Expected JSON object from " + var1);
    } else {
      return var2.getAsJsonObject();
    }
  }

  private byte[] createByte(String var1) throws Exception {
    HttpRequest var2 =
        HttpRequest.newBuilder(this.createURI2(var1))
            .timeout(Duration.ofSeconds(45L))
            .GET()
            .build();
    HttpResponse var3 = this.fcivsbyymmlp.send(var2, BodyHandlers.ofInputStream());
    if (var3.statusCode() >= 400) {
      InputStream var15 = (InputStream) var3.body();
      if (var15 != null) {
        var15.close();
      }

      throw new RuntimeException("HTTP " + var3.statusCode());
    } else {
      try (InputStream var4 = (InputStream) var3.body();
          ByteArrayOutputStream var5 = new ByteArrayOutputStream(); ) {
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

  private Builder createBuilder(String var1) {
    return HttpRequest.newBuilder(this.createURI(var1))
        .timeout(Duration.ofSeconds(15L))
        .header("Accept", "application/json");
  }

  private URI createURI(String var1) {
    return URI.create(this.text2 + var1);
  }

  private URI createURI2(String var1) {
    if (var1.startsWith("https://")) {
      return URI.create(var1);
    } else if (var1.startsWith("http://")) {
      throw new SecurityException("plain http:// download refused: " + var1);
    } else {
      return URI.create(this.text2 + (var1.startsWith("/") ? var1 : "/" + var1));
    }
  }

  private RuntimeException createRuntimeException(HttpResponse<String> var1) {
    try {
      JsonObject var2 = JsonParser.parseString((String) var1.body()).getAsJsonObject();
      String var3 = createText6(var2, "code", "HTTP_" + var1.statusCode());
      String var4 = createText6(var2, "message", "HTTP " + var1.statusCode());
      return new RuntimeException(var3 + ": " + var4);
    } catch (Exception var5) {
      return new RuntimeException("HTTP " + var1.statusCode());
    }
  }

  private static void updateState9(byte[] var0, Path var1) throws Exception {
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
          try (OutputStream var11 =
              Files.newOutputStream(
                  var10, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
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

  private static void updateState10(Path var0) throws Exception {
    try (Stream<Path> var1 = Files.walk(var0)) {
      var1.sorted(Comparator.reverseOrder())
          .forEach(
              var0x -> {
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

  private static LuaTable createLuaTable2(boolean var0, String var1) {
    LuaTable var2 = new LuaTable();
    var2.set("ok", LuaValue.valueOf(var0));
    var2.set("message", LuaValue.valueOf(var1 == null ? "" : var1));
    return var2;
  }

  private static LuaValue createLuaValue(JsonElement var0) {
    if (var0 == null || var0.isJsonNull()) {
      return LuaValue.NIL;
    }

    if (var0.isJsonPrimitive()) {
      JsonPrimitive var6 = var0.getAsJsonPrimitive();
      if (var6.isBoolean()) {
        return LuaValue.valueOf(var6.getAsBoolean());
      } else {
        return (LuaValue)
            (var6.isNumber()
                ? LuaValue.valueOf(var6.getAsDouble())
                : LuaValue.valueOf(var6.getAsString()));
      }
    } else if (var0.isJsonArray()) {
      JsonArray var5 = var0.getAsJsonArray();
      LuaTable var7 = new LuaTable();

      for (int var8 = 0; var8 < var5.size(); var8++) {
        var7.set(var8 + 1, createLuaValue(var5.get(var8)));
      }

      return var7;
    } else {
      if (!var0.isJsonObject()) {
        return LuaValue.NIL;
      }

      JsonObject var1 = var0.getAsJsonObject();
      LuaTable var2 = new LuaTable();

      for (Entry var4 : var1.entrySet()) {
        var2.set((String) var4.getKey(), createLuaValue((JsonElement) var4.getValue()));
      }

      return var2;
    }
  }

  private static LuaTable createLuaTable3(List<String> var0) {
    LuaTable var1 = new LuaTable();

    for (int var2 = 0; var2 < var0.size(); var2++) {
      var1.set(var2 + 1, LuaValue.valueOf((String) var0.get(var2)));
    }

    return var1;
  }

  private static List<String> createText(JsonElement var0) {
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

  private static String createText2(byte[] var0) throws Exception {
    byte[] var1 = MessageDigest.getInstance("SHA-256").digest(var0);
    return HexFormat.of().formatHex(var1);
  }

  private static boolean checkCondition(String var0) {
    return var0 != null && pattern.matcher(var0).matches();
  }

  private static String createText3(String var0) {
    String var1 = var0 == null ? "" : var0.toLowerCase().trim();
    if (var1.startsWith("theme")) {
      return "theme";
    } else {
      return !var1.startsWith("setup") && !var1.startsWith("gallery") ? "plugin" : "setup";
    }
  }

  private static String createText4(String var0) {
    return switch (var0) {
      case "theme" -> "themes";
      case "setup" -> "setups";
      default -> "plugins";
    };
  }

  private static String createText5(Throwable var0) {
    String var1 = var0.getMessage();
    if (var1 != null && !var1.isBlank()) {
      return var1.length() > 180 ? var1.substring(0, 180) : var1;
    } else {
      return "Marketplace request failed";
    }
  }

  private static JsonObject createJsonObject2(JsonObject var0, String var1) {
    return var0.has(var1) && var0.get(var1).isJsonObject() ? var0.getAsJsonObject(var1) : null;
  }

  private static JsonArray createJsonArray(JsonObject var0, String var1) {
    return var0.has(var1) && var0.get(var1).isJsonArray() ? var0.getAsJsonArray(var1) : null;
  }

  private static String createText6(JsonObject var0, String var1, String var2) {
    return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsString() : var2;
  }

  private static boolean checkCondition2(JsonObject var0, String var1, boolean var2) {
    return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsBoolean() : var2;
  }

  private static long mh0ixvlh7qj(JsonObject var0, String var1, long var2) {
    return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsLong() : var2;
  }

  private static String mce9cvktfdfy(String var0) {
    return URLEncoder.encode(var0 == null ? "" : var0, StandardCharsets.UTF_8);
  }

  private static String createText8(String var0) {
    return mce9cvktfdfy(var0).replace("+", "%20");
  }

  private static String mbmeohi9efid() {
    String var0 = System.getProperty("ellice.api.baseUrl");
    if (var0 != null && !var0.isBlank()) {
      return createText10(var0);
    }

    String var1 = System.getenv("ELLICE_API_BASE_URL");
    return var1 != null && !var1.isBlank() ? createText10(var1) : "https://api.pyrra.net";
  }

  private static String createText10(String var0) {
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
