package dev.felix.ellice.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

final class AccountToStringService {
  private static final URI uRI = URI.create("https://api.minecraftservices.com/minecraft/profile");

  private AccountToStringService() {}

  static AccountNewThreadService.LoginResult login(
      AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<String> consumer) {
    String text = CookieBrowserLoginFlow.login(accountCodec, booleanSupplier, consumer);
    MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
    consumer.accept("Verifying your Minecraft profile…");

    try (HttpClient httpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10L))
            .followRedirects(Redirect.NEVER)
            .build()) {
      return validate(
          text,
          booleanSupplier,
          item -> {
            HttpResponse httpResponse =
                httpClient.send(
                    HttpRequest.newBuilder(uRI)
                        .timeout(Duration.ofSeconds(15L))
                        .header("Authorization", "Bearer " + item)
                        .header("Accept", "application/json")
                        .GET()
                        .build(),
                    BodyHandlers.ofString());
            return new AccountToStringService.Response(
                httpResponse.statusCode(), (String) httpResponse.body());
          });
    }
  }

  static AccountNewThreadService.LoginResult validate(
      String currentId,
      BooleanSupplier booleanSupplier,
      AccountToStringService.ProfileRequest profileRequest) {
    try {
      MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
      if (currentId != null
          && currentId.length() <= 32768
          && currentId.matches("[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+")) {
        JsonObject jsonObject =
            JsonParser.parseString(
                    new String(
                        Base64.getUrlDecoder().decode(currentId.split("\\.")[1]),
                        StandardCharsets.UTF_8))
                .getAsJsonObject();
        Instant instant = Instant.ofEpochSecond(jsonObject.get("exp").getAsLong());
        if (!instant.isAfter(Instant.now())) {
          throw expired();
        }

        AccountToStringService.Response response = profileRequest.get(currentId);
        MicrosoftOAuthBrowserFlow.checkCancelled(booleanSupplier);
        switch (response.status()) {
          case 401:
          case 403:
            throw expired();
          case 404:
            throw new AccountCodeService(
                "NO_MC_LICENSE", "This Microsoft account has no Minecraft: Java Edition profile.");
          case 429:
            throw new AccountCodeService(
                "COOKIE_RATE_LIMIT", "Minecraft is busy. Wait a moment and try again.");
          default:
            if (response.status() >= 500) {
              throw new AccountCodeService(
                  "COOKIE_SERVICE_UNAVAILABLE",
                  "Minecraft sign-in is temporarily unavailable. Try again later.");
            } else if (response.status() != 200) {
              throw createAccountCodeService();
            } else {
              JsonObject currentJsonObject =
                  JsonParser.parseString(response.body()).getAsJsonObject();
              String nextId = currentJsonObject.get("id").getAsString();
              if (!nextId.matches("[0-9a-fA-F]{32}")) {
                throw createAccountCodeService();
              } else {
                UUID uUID =
                    UUID.fromString(
                        nextId.substring(0, 8)
                            + "-"
                            + nextId.substring(8, 12)
                            + "-"
                            + nextId.substring(12, 16)
                            + "-"
                            + nextId.substring(16, 20)
                            + "-"
                            + nextId.substring(20));
                String previousId = currentJsonObject.get("name").getAsString();
                if (!AccountTypeData.isValidOfflineUsername(previousId)) {
                  throw createAccountCodeService();
                } else if (!instant.isAfter(Instant.now())) {
                  throw expired();
                } else {
                  AccountTypeData accountTypeData =
                      AccountTypeData.session(uUID, previousId, currentId, instant);
                  return new AccountNewThreadService.LoginResult(
                      accountTypeData, currentId, instant);
                }
              }
            }
        }
      } else {
        throw createAccountCodeService();
      }
    } catch (AccountCodeService accountCode) {
      throw accountCode;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
    } catch (IOException iOException) {
      throw new AccountCodeService(
          "COOKIE_NETWORK", "Couldn't reach Minecraft. Check your connection and try again.");
    } catch (RuntimeException exception) {
      throw createAccountCodeService();
    }
  }

  static AccountCodeService expired() {
    return new AccountCodeService(
        "COOKIE_SESSION_EXPIRED",
        "This cookie session has expired or the client was restarted. Import the cookie file"
            + " again.");
  }

  private static AccountCodeService createAccountCodeService() {
    return new AccountCodeService(
        "COOKIE_INVALID_RESPONSE",
        "Minecraft.net returned an invalid session. Retry the cookie login.");
  }

  @FunctionalInterface
  interface ProfileRequest {
    AccountToStringService.Response get(String text) throws IOException, InterruptedException;
  }

  record Response(int status, String body) {
    @Override
    public String toString() {
      return "Minecraft profile response [redacted]";
    }
  }
}
