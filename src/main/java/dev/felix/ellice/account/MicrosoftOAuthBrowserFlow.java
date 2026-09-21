package dev.felix.ellice.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.raphimc.minecraftauth.msa.model.MsaToken;

final class MicrosoftOAuthBrowserFlow {
  private static final URI uRI = URI.create("https://login.live.com/");
  private static final URI uRI2 = uRI.resolve("oauth20_desktop.srf");
  private static final URI uRI3 = uRI.resolve("oauth20_token.srf");
  private static final int count = 10;
  private static final String text2 =
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko)"
          + " Chrome/131.0.0.0 Safari/537.36";
  private final MicrosoftOAuthBrowserFlow.Transport text3;

  MicrosoftOAuthBrowserFlow(MicrosoftOAuthBrowserFlow.Transport transport) {
    this.text3 = transport;
  }

  static MsaToken login(AccountCodec accountCodec, BooleanSupplier booleanSupplier) {
    return login(accountCodec, booleanSupplier, item -> {});
  }

  static MsaToken login(
      AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<String> consumer) {
    try (HttpClient httpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10L))
            .followRedirects(Redirect.NEVER)
            .build()) {
      return new MicrosoftOAuthBrowserFlow(
              (uri, headers, requestBody) -> {
                Builder builder =
                    HttpRequest.newBuilder(uri)
                        .timeout(Duration.ofSeconds(15L))
                        .header(
                            "User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36"
                                + " (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                        .header("Accept-Language", "en-US,en;q=0.9");
                headers.forEach(
                    (headerName, headerValues) ->
                        headerValues.forEach(
                            headerValue -> builder.header(headerName, headerValue)));
                if (requestBody == null) {
                  builder
                      .header(
                          "Accept",
                          "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                      .header("Upgrade-Insecure-Requests", "1")
                      .header("Sec-Fetch-Dest", "document")
                      .header("Sec-Fetch-Mode", "navigate")
                      .header("Sec-Fetch-Site", "none")
                      .header("Sec-Fetch-User", "?1")
                      .GET();
                } else {
                  builder
                      .header("Content-Type", "application/x-www-form-urlencoded")
                      .header("Accept", "*/*")
                      .header("Sec-Fetch-Dest", "empty")
                      .header("Sec-Fetch-Mode", "cors")
                      .header("Sec-Fetch-Site", "same-origin")
                      .POST(BodyPublishers.ofString(requestBody));
                }

                HttpResponse httpResponse =
                    httpClient.send(builder.build(), BodyHandlers.ofString());
                return new MicrosoftOAuthBrowserFlow.Response(
                    httpResponse.statusCode(),
                    httpResponse.headers().map(),
                    (String) httpResponse.body());
              })
          .authenticateWithBrowser(
              accountCodec, booleanSupplier, AccountVisitFileService::authorize, consumer);
    }
  }

  MsaToken authenticateWithBrowser(
      AccountCodec accountCodec,
      BooleanSupplier booleanSupplier,
      MicrosoftOAuthBrowserFlow.BrowserLogin browserLogin,
      Consumer<String> consumer) {
    AtomicReference atomicReference = new AtomicReference<>(accountCodec);

    try {
      return this.createMsaToken(accountCodec, booleanSupplier, atomicReference::set);
    } catch (AccountCodeService accountCode) {
      if (!accountCode.code().equals("COOKIE_SIGN_IN_REQUIRED")) {
        throw accountCode;
      }

      checkCancelled(booleanSupplier);
      consumer.accept(
          "Continuing your cookie session in a sign-in window. Complete any Microsoft prompts"
              + " there…");
      MicrosoftOAuthBrowserFlow.Authorization authorization =
          new MicrosoftOAuthBrowserFlow.Authorization();
      URI uRI =
          browserLogin.authorize(
              (AccountCodec) atomicReference.get(), authorization.uri(false), booleanSupplier);
      checkCancelled(booleanSupplier);
      consumer.accept("Finishing Microsoft sign-in…");

      try {
        return this.createMsaToken2(authorization, uRI, booleanSupplier);
      } catch (AccountCodeService currentAccountCode) {
        throw currentAccountCode;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
      } catch (IOException iOException) {
        throw createAccountCodeService4();
      } catch (RuntimeException exception) {
        throw createAccountCodeService3();
      }
    }
  }

  MsaToken authenticate(AccountCodec accountCodec, BooleanSupplier booleanSupplier) {
    return this.createMsaToken(accountCodec, booleanSupplier, item -> {});
  }

  private MsaToken createMsaToken(
      AccountCodec accountCodec, BooleanSupplier booleanSupplier, Consumer<AccountCodec> consumer) {
    CookieManager cookieManager =
        new CookieManager(
            null,
            (item, currentItem) ->
                checkCondition2(item)
                    && CookiePolicy.ACCEPT_ORIGINAL_SERVER.shouldAccept(item, currentItem));
    accountCodec
        .cookies()
        .forEach(item -> cookieManager.getCookieStore().add(AccountCodec.origin(item), item));
    MicrosoftOAuthBrowserFlow.Authorization authorization =
        new MicrosoftOAuthBrowserFlow.Authorization();
    URI uRI = authorization.uri(true);

    try {
      for (int index = 0; index <= 10; index++) {
        checkCancelled(booleanSupplier);
        if (!checkCondition2(uRI)) {
          throw createAccountCodeService3();
        }

        if (isCallback(uRI)) {
          consumer.accept(AccountCodec.fromCookieJar(cookieManager.getCookieStore().getCookies()));
          cookieManager.getCookieStore().removeAll();
          return this.createMsaToken2(authorization, uRI, booleanSupplier);
        }

        MicrosoftOAuthBrowserFlow.Response response =
            this.text3.send(uRI, cookieManager.get(uRI, Map.of()), null);
        checkCancelled(booleanSupplier);
        cookieManager.put(uRI, response.headers());
        updateState(response);
        String text = response.location();
        if (response.status() == 200) {
          throw createAccountCodeService2();
        }

        if (response.status() < 300 || response.status() >= 400 || text == null) {
          throw createAccountCodeService3();
        }

        uRI = uRI.resolve(text);
        if (!checkCondition2(uRI) && checkCondition(uRI)) {
          throw createAccountCodeService2();
        }
      }

      throw new AccountCodeService(
          "COOKIE_REDIRECT_LOOP", "Microsoft sign-in redirected too many times. Try again later.");
    } catch (AccountCodeService accountCode) {
      if (accountCode.code().equals("COOKIE_SIGN_IN_REQUIRED")
          && !cookieManager.getCookieStore().getCookies().isEmpty()) {
        consumer.accept(AccountCodec.fromCookieJar(cookieManager.getCookieStore().getCookies()));
      }

      throw accountCode;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
    } catch (IOException iOException) {
      throw createAccountCodeService4();
    } catch (RuntimeException exception) {
      throw createAccountCodeService3();
    } finally {
      cookieManager.getCookieStore().removeAll();
    }
  }

  private MsaToken createMsaToken2(
      MicrosoftOAuthBrowserFlow.Authorization authorization,
      URI uRI,
      BooleanSupplier booleanSupplier)
      throws IOException, InterruptedException {
    checkCancelled(booleanSupplier);
    if (isCallback(uRI) && uRI.getRawFragment() == null) {
      Map entries = createText(uRI);
      if (!authorization.state.equals(entries.get("state"))) {
        throw createAccountCodeService3();
      }

      if (entries.containsKey("error")) {
        throw createAccountCodeService((String) entries.get("error"));
      }

      String text = (String) entries.get("code");
      if (text != null && !text.isBlank()) {
        MicrosoftOAuthBrowserFlow.Response response =
            this.text3.send(
                uRI3,
                Map.of(),
                authorization.config + "&grant_type=authorization_code&code=" + createText2(text));
        checkCancelled(booleanSupplier);
        updateState(response);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        if (response.status() != 200) {
          if (jsonObject.has("error")) {
            throw createAccountCodeService(jsonObject.get("error").getAsString());
          } else {
            throw createAccountCodeService3();
          }
        } else if (jsonObject.has("access_token")
            && jsonObject.has("refresh_token")
            && !jsonObject.get("access_token").getAsString().isBlank()
            && !jsonObject.get("refresh_token").getAsString().isBlank()) {
          long size =
              jsonObject.has("expires_in") ? jsonObject.get("expires_in").getAsLong() : 3600L;
          if (size <= 0L) {
            throw createAccountCodeService3();
          }

          long currentSize =
              Math.addExact(System.currentTimeMillis(), Math.multiplyExact(size, 1000));
          return new MsaToken(
              currentSize,
              jsonObject.get("access_token").getAsString(),
              jsonObject.get("refresh_token").getAsString());
        } else {
          throw createAccountCodeService3();
        }
      } else {
        throw createAccountCodeService3();
      }
    } else {
      throw createAccountCodeService3();
    }
  }

  static boolean isCallback(URI uRI) {
    return uRI != null && checkCondition2(uRI) && uRI2.getRawPath().equals(uRI.getRawPath());
  }

  private static boolean checkCondition(URI uRI) {
    return "https".equalsIgnoreCase(uRI.getScheme())
        && uRI.getRawUserInfo() == null
        && (uRI.getPort() == -1 || uRI.getPort() == 443)
        && uRI.getHost() != null
        && List.of(
                "account.live.com",
                "account.microsoft.com",
                "login.microsoft.com",
                "login.microsoftonline.com")
            .contains(uRI.getHost().toLowerCase(Locale.ROOT));
  }

  private static void updateState(MicrosoftOAuthBrowserFlow.Response response) {
    if (response.status() == 429) {
      throw new AccountCodeService(
          "COOKIE_RATE_LIMIT", "Microsoft is busy. Wait a moment and try again.");
    }

    if (response.status() >= 500) {
      throw new AccountCodeService(
          "COOKIE_SERVICE_UNAVAILABLE",
          "Microsoft sign-in is temporarily unavailable. Try again later.");
    }
  }

  private static AccountCodeService createAccountCodeService(String text) {
    return switch (text) {
      case "login_required",
          "interaction_required",
          "consent_required",
          "account_selection_required",
          "invalid_grant" ->
          createAccountCodeService2();
      case "access_denied" -> new AccountCodeService("DENIED", "Microsoft sign-in was declined.");
      case "server_error", "temporarily_unavailable" ->
          new AccountCodeService(
              "COOKIE_SERVICE_UNAVAILABLE",
              "Microsoft sign-in is temporarily unavailable. Try again later.");
      default -> createAccountCodeService3();
    };
  }

  private static boolean checkCondition2(URI uRI) {
    return "https".equalsIgnoreCase(uRI.getScheme())
        && "login.live.com".equalsIgnoreCase(uRI.getHost())
        && (uRI.getPort() == -1 || uRI.getPort() == 443)
        && uRI.getRawUserInfo() == null;
  }

  private static Map<String, String> createText(URI uRI) {
    LinkedHashMap linkedHashMap = new LinkedHashMap();
    if (uRI.getRawQuery() == null) {
      return linkedHashMap;
    }

    for (String text : uRI.getRawQuery().split("&")) {
      String[] strings = text.split("=", 2);
      if (strings.length == 2) {
        String currentText = URLDecoder.decode(strings[0], StandardCharsets.UTF_8);
        if (linkedHashMap.putIfAbsent(
                currentText, URLDecoder.decode(strings[1], StandardCharsets.UTF_8))
            != null) {
          throw createAccountCodeService3();
        }
      }
    }

    return linkedHashMap;
  }

  private static String createText2(String text) {
    return URLEncoder.encode(text, StandardCharsets.UTF_8);
  }

  static void checkCancelled(BooleanSupplier booleanSupplier) {
    if (booleanSupplier.getAsBoolean()) {
      throw new AccountCodeService("CANCELLED", "Sign-in cancelled.");
    }
  }

  private static AccountCodeService createAccountCodeService2() {
    return new AccountCodeService(
        "COOKIE_SIGN_IN_REQUIRED",
        "Microsoft couldn't complete this session silently. Continue in the sign-in window or use"
            + " Microsoft sign-in.");
  }

  private static AccountCodeService createAccountCodeService3() {
    return new AccountCodeService(
        "COOKIE_INVALID_RESPONSE",
        "Microsoft returned an unexpected sign-in response. Try again or use Microsoft sign-in.");
  }

  private static AccountCodeService createAccountCodeService4() {
    return new AccountCodeService(
        "COOKIE_NETWORK", "Couldn't reach Microsoft. Check your connection and try again.");
  }

  private static final class Authorization {
    final String state = UUID.randomUUID().toString();
    final String config =
        "client_id="
            + MicrosoftOAuthBrowserFlow.createText2("00000000402b5328")
            + "&scope="
            + MicrosoftOAuthBrowserFlow.createText2("service::user.auth.xboxlive.com::MBI_SSL")
            + "&redirect_uri="
            + MicrosoftOAuthBrowserFlow.createText2(MicrosoftOAuthBrowserFlow.uRI2.toString());

    URI uri(boolean enabled) {
      return MicrosoftOAuthBrowserFlow.uRI.resolve(
          "oauth20_authorize.srf?"
              + this.config
              + "&response_type=code&response_mode=query"
              + (enabled ? "&prompt=none" : "")
              + "&state="
              + this.state);
    }
  }

  @FunctionalInterface
  interface BrowserLogin {
    URI authorize(AccountCodec accountCodec, URI uRI, BooleanSupplier booleanSupplier);
  }

  record Response(int status, Map<String, List<String>> headers, String body) {
    String location() {
      return this.headers.entrySet().stream()
          .filter(entry -> entry.getKey().equalsIgnoreCase("location"))
          .flatMap(entry -> entry.getValue().stream())
          .findFirst()
          .orElse(null);
    }

    @Override
    public String toString() {
      return "Microsoft cookie authentication response [redacted]";
    }
  }

  @FunctionalInterface
  interface Transport {
    MicrosoftOAuthBrowserFlow.Response send(URI uRI, Map<String, List<String>> entries, String text)
        throws IOException, InterruptedException;
  }
}
