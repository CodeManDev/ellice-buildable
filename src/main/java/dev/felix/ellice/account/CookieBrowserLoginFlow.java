package dev.felix.ellice.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

final class CookieBrowserLoginFlow {
   static final URI LOGIN = URI.create(
      "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254&as=1"
   );
   private static final Duration duration = Duration.ofMinutes(5L);

   private CookieBrowserLoginFlow() {
   }

   static String login(AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<String> consumer) {
      try (CookieBrowserLoginFlow.Session currentSession = open(booleanSupplier, false)) {
         consumer.accept("Opening Minecraft.net with your cookie session…");
         return loginInSession(currentSession, accountCodec, booleanSupplier, consumer, duration);
      } catch (AccountCodeService accountCode) {
         throw accountCode;
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
      } catch (Exception exception) {
         throw failure();
      }
   }

   static CookieBrowserLoginFlow.Session open(BooleanSupplier booleanSupplier, boolean enabled) throws Exception {
      MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
      Optional result = AccountImportCookiesService.findBrowser();
      if (result.isPresent()) {
         return AccountImportCookiesService.open((Path)result.get(), booleanSupplier, enabled);
      }

      Path path = AccountVisitFileService.findBrowser()
         .orElseThrow(
            () -> new AccountCodeService(
               "COOKIE_BROWSER_UNAVAILABLE", "Install Firefox, Chrome or Edge to use cookie login, or use Microsoft sign-in."
            )
         );
      return new CookieBrowserLoginFlow.ChromiumSession(AccountVisitFileService.BrowserSession.open(path, booleanSupplier, enabled));
   }

   static String loginInSession(CookieBrowserLoginFlow.Session session, AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<String> consumer, Duration duration) throws Exception {
      MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
      session.importCookies(accountCodec);
      MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
      session.navigate(LOGIN);
      long longValue = System.nanoTime() + duration.toNanos();
      byte byteValue = 0;

      while (true) {
         MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
         session.checkAlive();
         String text = tokenFromCookies(session.cookies());
         MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
         if (text != null) {
            return text;
         }

         String currentText = session.location();
         if (currentText.startsWith("about:neterror") || currentText.startsWith("chrome-error:")) {
            throw new AccountCodeService(
               "COOKIE_BROWSER_NETWORK", "The sign-in window couldn't load Minecraft.net. Check your connection and try again."
            );
         }

         if (byteValue == 0 && System.nanoTime() > longValue - duration.toNanos() + Duration.ofSeconds(10L).toNanos()) {
            consumer.accept(
               "Waiting for Minecraft.net. If Microsoft asks you to verify the account, complete the prompt in the sign-in window."
            );
            byteValue = 1;
         }

         if (System.nanoTime() >= longValue) {
            throw new AccountCodeService(
               "COOKIE_BROWSER_TIMEOUT",
               "Minecraft.net did not finish signing in. Complete any Microsoft prompts, or import a fresh cookie export."
            );
         }

         Thread.sleep(200L);
      }
   }

   static String tokenFromCookies(JsonArray jsonArray) {
      String text = null;

      for (JsonElement jsonElement : jsonArray) {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         String currentText = jsonObject.get("domain").getAsString();
         if ((
               currentText.equalsIgnoreCase(".minecraft.net")
                  || currentText.equalsIgnoreCase("minecraft.net")
                  || currentText.equalsIgnoreCase("www.minecraft.net")
            )
            && jsonObject.has("secure")
            && jsonObject.get("secure").getAsBoolean()
            && (!jsonObject.has("path") || jsonObject.get("path").getAsString().equals("/"))) {
            String nextText = jsonObject.has("expiry") ? "expiry" : "expires";
            if (jsonObject.has(nextText)) {
               double doubleValue = jsonObject.get(nextText).getAsDouble();
               if (doubleValue > 0.0 && doubleValue <= Instant.now().getEpochSecond()) {
                  continue;
               }
            }

            String previousText = jsonObject.get("name").getAsString();
            if (previousText.equals("access_token") || previousText.equals("bearer_token")) {
               JsonElement currentJsonElement = jsonObject.get("value");
               String sourceText = currentJsonElement.isJsonObject() ? currentJsonElement.getAsJsonObject().get("value").getAsString() : currentJsonElement.getAsString();
               if (sourceText.length() > 65536) {
                  throw failure();
               }

               sourceText = URLDecoder.decode(sourceText.replace("+", "%2B"), StandardCharsets.UTF_8);
               if (previousText.equals("access_token")) {
                  JsonObject currentJsonObject = JsonParser.parseString(sourceText).getAsJsonObject();
                  if (currentJsonObject.has("accessToken") && !currentJsonObject.get("accessToken").isJsonNull()) {
                     String targetText = currentJsonObject.get("accessToken").getAsString();
                     if (!targetText.isBlank()) {
                        return targetText;
                     }
                  }
               } else if (!sourceText.isBlank()) {
                  text = sourceText;
               }
            }
         }
      }

      return text;
   }

   static AccountCodeService failure() {
      return new AccountCodeService(
         "COOKIE_BROWSER_FAILED", "Couldn't complete the Minecraft.net cookie login. Retry the cookie or use Microsoft sign-in."
      );
   }

   static AccountCodeService closed() {
      return new AccountCodeService("COOKIE_BROWSER_CLOSED", "The sign-in window was closed. Retry the cookie to continue.");
   }

   private static final class ChromiumSession implements CookieBrowserLoginFlow.Session {
      private final AccountVisitFileService.BrowserSession browserSession;
      private CompletableFuture<JsonObject> completableFuture;

      private ChromiumSession(AccountVisitFileService.BrowserSession currentBrowserSession) {
         this.browserSession = currentBrowserSession;
      }

      @Override
      public void importCookies(AccountCodec accountCodec) throws Exception {
         this.browserSession.pageCommand("Network.setCookies", AccountVisitFileService.cookieParams(accountCodec));
      }

      @Override
      public void navigate(URI uRI) {
         this.completableFuture = this.browserSession.pageAsync("Page.navigate", AccountVisitFileService.object("url", uRI.toString()));
      }

      @Override
      public JsonArray cookies() throws Exception {
         JsonArray jsonArray = new JsonArray();
         jsonArray.add("https://www.minecraft.net/en-us/login");
         JsonObject jsonObject = new JsonObject();
         jsonObject.add("urls", jsonArray);
         return this.browserSession.pageCommand("Network.getCookies", jsonObject).getAsJsonArray("cookies");
      }

      @Override
      public String location() throws Exception {
         return this.browserSession
            .pageCommand("Page.getFrameTree", new JsonObject())
            .getAsJsonObject("frameTree")
            .getAsJsonObject("frame")
            .get("url")
            .getAsString();
      }

      @Override
      public void checkAlive() {
         this.browserSession.checkAlive();
         if (this.completableFuture != null && this.completableFuture.isDone() && this.completableFuture.join().has("errorText")) {
            throw new AccountCodeService(
               "COOKIE_BROWSER_NETWORK", "The sign-in window couldn't load Minecraft.net. Try Firefox or check your connection."
            );
         }
      }

      @Override
      public void close() {
         this.browserSession.close();
      }
   }

   interface Session extends AutoCloseable {
      void importCookies(AccountCodec accountCodec) throws Exception;

      void navigate(URI uRI) throws Exception;

      JsonArray cookies() throws Exception;

      String location() throws Exception;

      void checkAlive();

      @Override
      void close();
   }
}
