package dev.felix.ellice.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;
import net.raphimc.minecraftauth.java.exception.MinecraftProfileNotFoundException;
import net.raphimc.minecraftauth.java.model.MinecraftProfile;
import net.raphimc.minecraftauth.java.model.MinecraftToken;
import net.raphimc.minecraftauth.msa.model.MsaToken;
import net.raphimc.minecraftauth.xbl.exception.XblRequestException;

public final class AccountNewThreadService {
   public static final String TITLE_CLIENT_ID = "00000000402b5328";
   private static final String text = "https://login.live.com/oauth20_connect.srf";
   private static final String text2 = "https://login.live.com/oauth20_token.srf";
   private static final String text3 = "service::user.auth.xboxlive.com::MBI_SSL";
   private static final Executor executor = Executors.newCachedThreadPool(new ThreadFactory() {
      private final AtomicInteger atomicInteger = new AtomicInteger();

      @Override
      public Thread newThread(Runnable runnable) {
         Thread thread = new Thread(runnable, "ellice-msauth-" + this.atomicInteger.incrementAndGet());
         thread.setDaemon(true);
         return thread;
      }
   });
   private final HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10L))
      .followRedirects(Redirect.NORMAL)
      .build();
   private final net.lenni0451.commons.httpclient.HttpClient client2 = MinecraftAuth.createHttpClient("ElliceClient Microsoft Login");

   public CompletableFuture<AccountNewThreadService.DeviceCode> requestDeviceCode() {
      String text = "client_id=00000000402b5328&scope="
         + createText2("service::user.auth.xboxlive.com::MBI_SSL")
         + "&response_type=device_code";
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://login.live.com/oauth20_connect.srf"))
         .timeout(Duration.ofSeconds(15L))
         .header("Content-Type", "application/x-www-form-urlencoded")
         .POST(BodyPublishers.ofString(text))
         .build();
      return this.client
         .sendAsync(httpRequest, BodyHandlers.ofString())
         .thenApply(
            item -> {
               if (item.statusCode() != 200) {
                  throw createAccountCodeService("devicecode", (HttpResponse<String>)item);
               }

               JsonObject jsonObject = JsonParser.parseString(item.body()).getAsJsonObject();
               return new AccountNewThreadService.DeviceCode(
                  jsonObject.get("user_code").getAsString(),
                  jsonObject.get("device_code").getAsString(),
                  jsonObject.get("verification_uri").getAsString(),
                  Duration.ofSeconds(jsonObject.get("expires_in").getAsLong()),
                  Duration.ofSeconds(Math.max(jsonObject.get("interval").getAsLong(), 1L))
               );
            }
         );
   }

   public CompletableFuture<AccountNewThreadService.LoginResult> completeLogin(AccountNewThreadService.DeviceCode deviceCode, BooleanSupplier booleanSupplier) {
      return this.createCompletableFuture(deviceCode, booleanSupplier).thenApplyAsync(this::createLoginResult, executor);
   }

   public CompletableFuture<AccountTypeData> completeAuth(AccountNewThreadService.DeviceCode deviceCode, BooleanSupplier booleanSupplier) {
      return this.completeLogin(deviceCode, booleanSupplier).thenApply(AccountNewThreadService.LoginResult::account);
   }

   public CompletableFuture<AccountNewThreadService.LoginResult> login(AccountTypeData accountTypeData) {
      if (accountTypeData.isSession()) {
         return !accountTypeData.hasCurrentSession()
            ? CompletableFuture.failedFuture(AccountToStringService.expired())
            : CompletableFuture.completedFuture(new AccountNewThreadService.LoginResult(accountTypeData, accountTypeData.sessionAccessToken(), accountTypeData.sessionExpiresAt()));
      } else {
         return this.createCompletableFuture2(accountTypeData.msRefreshToken())
            .thenApplyAsync(this::createLoginResult, executor)
            .thenApply(
               item -> new AccountNewThreadService.LoginResult(
                  new AccountTypeData(
                     item.account().uuid(),
                     item.account().username(),
                     item.account().msRefreshToken(),
                     accountTypeData.addedAt(),
                     accountTypeData.lastUsedAt()
                  ),
                  item.accessToken(),
                  item.expiresAt()
               )
            );
      }
   }

   public CompletableFuture<AccountTypeData> refresh(AccountTypeData accountTypeData) {
      return this.login(accountTypeData).thenApply(AccountNewThreadService.LoginResult::account);
   }

   public CompletableFuture<AccountNewThreadService.LoginResult> loginWithCookies(AccountCodec accountCodec, BooleanSupplier booleanSupplier) {
      return this.loginWithCookies(accountCodec, booleanSupplier, item -> {});
   }

   public CompletableFuture<AccountNewThreadService.LoginResult> loginWithCookies(AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<String> consumer) {
      return CompletableFuture.supplyAsync(() -> {
         MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
         AccountNewThreadService.LoginResult loginResult = AccountToStringService.login(accountCodec, booleanSupplier, consumer);
         MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
         return loginResult;
      }, executor);
   }

   private CompletableFuture<AccountNewThreadService.MsTokens> createCompletableFuture(AccountNewThreadService.DeviceCode deviceCode, BooleanSupplier booleanSupplier) {
      CompletableFuture completableFuture = new CompletableFuture();
      Instant instant = Instant.now().plus(deviceCode.expiresIn());
      long size = Math.max(deviceCode.interval().toMillis(), 1000L);
      executor.execute(() -> this.updateState(deviceCode, booleanSupplier, instant, size, completableFuture));
      return completableFuture;
   }

   private void updateState(
      AccountNewThreadService.DeviceCode currentDeviceCode, BooleanSupplier booleanSupplier, Instant instant, long offset, CompletableFuture<AccountNewThreadService.MsTokens> completableFuture
   ) {
      long currentOffset = offset;

      try {
         while (Instant.now().isBefore(instant)) {
            if (booleanSupplier.getAsBoolean()) {
               completableFuture.completeExceptionally(new AccountCodeService("CANCELLED", "User cancelled"));
               return;
            }

            Thread.sleep(currentOffset);
            if (booleanSupplier.getAsBoolean()) {
               completableFuture.completeExceptionally(new AccountCodeService("CANCELLED", "User cancelled"));
               return;
            }

            String text = "grant_type=device_code&client_id=00000000402b5328&device_code=" + createText2(currentDeviceCode.deviceCode());
            HttpResponse httpResponse = this.client
               .send(
                  HttpRequest.newBuilder(URI.create("https://login.live.com/oauth20_token.srf"))
                     .timeout(Duration.ofSeconds(15L))
                     .header("Content-Type", "application/x-www-form-urlencoded")
                     .POST(BodyPublishers.ofString(text))
                     .build(),
                  BodyHandlers.ofString()
               );
            JsonObject jsonObject = JsonParser.parseString((String)httpResponse.body()).getAsJsonObject();
            if (httpResponse.statusCode() == 200 && jsonObject.has("access_token")) {
               completableFuture.complete(createMsTokens(jsonObject));
               return;
            }

            if (!jsonObject.has("error")) {
               completableFuture.completeExceptionally(createAccountCodeService("token", httpResponse));
               return;
            }

            String currentText = jsonObject.get("error").getAsString();
            switch (currentText) {
               case "authorization_pending":
                  break;
               case "slow_down":
                  currentOffset += 5000L;
                  break;
               case "expired_token":
                  completableFuture.completeExceptionally(new AccountCodeService("EXPIRED", "Code expired before login completed"));
                  return;
               case "access_denied":
                  completableFuture.completeExceptionally(new AccountCodeService("DENIED", "User declined the Microsoft login"));
                  return;
               default:
                  completableFuture.completeExceptionally(new AccountCodeService(currentText, createText(jsonObject, currentText)));
                  return;
            }
         }

         completableFuture.completeExceptionally(new AccountCodeService("EXPIRED", "Code expired before login completed"));
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         completableFuture.completeExceptionally(new AccountCodeService("INTERRUPTED", "Login was interrupted"));
      } catch (Exception exception) {
         completableFuture.completeExceptionally(exception);
      }
   }

   private CompletableFuture<AccountNewThreadService.MsTokens> createCompletableFuture2(String refreshToken) {
      String requestBody = "grant_type=refresh_token&client_id=00000000402b5328&refresh_token="
         + createText2(refreshToken)
         + "&scope="
         + createText2("service::user.auth.xboxlive.com::MBI_SSL");
      HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://login.live.com/oauth20_token.srf"))
         .timeout(Duration.ofSeconds(15L))
         .header("Content-Type", "application/x-www-form-urlencoded")
         .POST(BodyPublishers.ofString(requestBody))
         .build();
      return this.client
         .sendAsync(httpRequest, BodyHandlers.ofString())
         .thenApply(
            response -> {
               if (response.statusCode() == 200) {
                  AccountNewThreadService.MsTokens msTokens = createMsTokens(JsonParser.parseString(response.body()).getAsJsonObject());
                  return msTokens.refreshToken() == null ? new AccountNewThreadService.MsTokens(msTokens.accessToken(), refreshToken, msTokens.expiresAt()) : msTokens;
               } else {
                  JsonObject jsonObject = createJsonObject(response.body());
                  String errorCode = jsonObject != null && jsonObject.has("error")
                     ? jsonObject.get("error").getAsString()
                     : "MS_REFRESH_HTTP_" + response.statusCode();
                  String errorMessage = "invalid_grant".equals(errorCode)
                     ? "This saved login uses the old Microsoft flow or has expired. Link the account again."
                     : (
                        jsonObject != null
                           ? createText(jsonObject, "Microsoft sign-in expired; link the account again.")
                           : "Microsoft sign-in expired; link the account again."
                     );
                  throw new AccountCodeService(errorCode, errorMessage);
               }
            }
         );
   }

   private AccountNewThreadService.LoginResult createLoginResult(AccountNewThreadService.MsTokens msTokens) {
      if (msTokens.refreshToken() != null && !msTokens.refreshToken().isBlank()) {
         try {
            MsaToken msaToken = new MsaToken(msTokens.expiresAt().toEpochMilli(), msTokens.accessToken(), msTokens.refreshToken());
            JavaAuthManager javaAuthManager = JavaAuthManager.create(this.client2).login(msaToken);
            MinecraftToken minecraftToken = (MinecraftToken)javaAuthManager.getMinecraftToken().getUpToDate();
            MinecraftProfile minecraftProfile = (MinecraftProfile)javaAuthManager.getMinecraftProfile().getUpToDate();
            Instant instant = Instant.now();
            AccountTypeData accountTypeData = new AccountTypeData(minecraftProfile.getId(), minecraftProfile.getName(), msTokens.refreshToken(), instant, instant);
            return new AccountNewThreadService.LoginResult(accountTypeData, minecraftToken.getToken(), Instant.ofEpochMilli(minecraftToken.getExpireTimeMs()));
         } catch (MinecraftProfileNotFoundException minecraftProfileNotFoundException) {
            throw new AccountCodeService("NO_MC_LICENSE", "This Microsoft account doesn't own Minecraft: Java Edition.");
         } catch (XblRequestException xblRequestException) {
            throw new AccountCodeService("XBL_" + xblRequestException.getErrorCode(), xblRequestException.getErrorMessage());
         } catch (AccountCodeService accountCode) {
            throw accountCode;
         } catch (Exception exception) {
            String text = exception.getMessage();
            throw new AccountCodeService("TITLE_AUTH_FAILED", text != null && !text.isBlank() ? text : "Microsoft/Xbox authentication failed.");
         }
      } else {
         throw new AccountCodeService("NO_MS_REFRESH_TOKEN", "Microsoft did not return a refresh token. Try signing in again.");
      }
   }

   private static AccountNewThreadService.MsTokens createMsTokens(JsonObject jsonObject) {
      return new AccountNewThreadService.MsTokens(
         jsonObject.get("access_token").getAsString(),
         jsonObject.has("refresh_token") ? jsonObject.get("refresh_token").getAsString() : null,
         jsonObject.has("expires_in") ? Instant.now().plusSeconds(jsonObject.get("expires_in").getAsLong()) : Instant.now().plusSeconds(3600L)
      );
   }

   private static AccountCodeService createAccountCodeService(String text, HttpResponse<String> httpResponse) {
      return new AccountCodeService(
         text.toUpperCase() + "_HTTP_" + httpResponse.statusCode(),
         text + " failed: HTTP " + httpResponse.statusCode() + " — " + createText3((String)httpResponse.body(), 256)
      );
   }

   private static String createText(JsonObject jsonObject, String text) {
      return jsonObject.has("error_description") ? jsonObject.get("error_description").getAsString() : text;
   }

   private static JsonObject createJsonObject(String text) {
      try {
         return JsonParser.parseString(text).getAsJsonObject();
      } catch (RuntimeException exception) {
         return null;
      }
   }

   private static String createText2(String text) {
      return URLEncoder.encode(text, StandardCharsets.UTF_8);
   }

   private static String createText3(String text, int value) {
      if (text == null) {
         return "";
      } else {
         return text.length() <= value ? text : text.substring(0, value) + "…";
      }
   }

   public record DeviceCode(String userCode, String deviceCode, String verificationUri, Duration expiresIn, Duration interval) {
   }

   public record LoginResult(AccountTypeData account, String accessToken, Instant expiresAt) {
      @Override
      public String toString() {
         return "LoginResult[account=" + this.account + ", token=redacted]";
      }
   }

   private record MsTokens(String accessToken, String refreshToken, Instant expiresAt) {
   }
}
