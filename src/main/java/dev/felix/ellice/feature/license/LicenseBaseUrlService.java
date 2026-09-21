package dev.felix.ellice.feature.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class LicenseBaseUrlService {
  private static final HttpClient client =
      HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(10L))
          .followRedirects(Redirect.NORMAL)
          .build();

  private LicenseBaseUrlService() {}

  public static String baseUrl() {
    String text = System.getProperty("ellice.supabase.url", "");
    return !text.isBlank()
        ? text.trim().replaceAll("/+$", "")
        : "https://hnnzpailbyjzieknkyvm.supabase.co";
  }

  public static String anonKey() {
    String text = System.getProperty("ellice.supabase.anon", "");
    return !text.isBlank() ? text.trim() : "sb_publishable_2SCv_reVWeVmNdfU5dO4ag_-0F3Zprh";
  }

  public static Boolean freeRelease() {
    try {
      String text = createText("/rest/v1/rpc/my_entitlement", "{}", anonKey(), anonKey());
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      return jsonObject.has("free") && jsonObject.get("free").getAsBoolean();
    } catch (Exception exception) {
      return null;
    }
  }

  public static LicenseBaseUrlService.Heartbeat heartbeat(
      String text, String currentText, String nextText, String previousText) {
    try {
      String sourceText =
          "{\"hwid\":"
              + createText7(currentText)
              + ",\"build\":"
              + createText7(nextText)
              + ",\"nonce\":"
              + createText7(previousText == null ? "" : previousText)
              + "}";
      HttpResponse httpResponse =
          createText3("/functions/v1/heartbeat", sourceText, text, anonKey());
      int value = httpResponse.statusCode();
      String targetText = (String) httpResponse.body();
      if (value == 200) {
        return new LicenseBaseUrlService.Heartbeat(
            "", LicenseBaseUrlService.HeartbeatResult.OK, "");
      } else if (value == 426) {
        return new LicenseBaseUrlService.Heartbeat(
            createText5(targetText),
            LicenseBaseUrlService.HeartbeatResult.UPDATE_REQUIRED,
            "update required");
      } else if (value == 423) {
        return new LicenseBaseUrlService.Heartbeat(
            "", LicenseBaseUrlService.HeartbeatResult.DEVICE_LOCKED, createText6(targetText));
      } else {
        return value != 401 && value != 403
            ? new LicenseBaseUrlService.Heartbeat(
                "", LicenseBaseUrlService.HeartbeatResult.NETWORK_ERROR, "HTTP " + value)
            : new LicenseBaseUrlService.Heartbeat(
                "", LicenseBaseUrlService.HeartbeatResult.DENIED, createText6(targetText));
      }
    } catch (Exception exception) {
      return new LicenseBaseUrlService.Heartbeat(
          "", LicenseBaseUrlService.HeartbeatResult.NETWORK_ERROR, "network");
    }
  }

  private static String createText(
      String text, String currentText, String nextText, String previousText) throws Exception {
    return createText4(text, currentText, previousText, nextText).body();
  }

  private static HttpResponse<String> createText2(String text, String currentText, String nextText)
      throws Exception {
    return createText4(text, currentText, nextText, anonKey());
  }

  private static HttpResponse<String> createText3(
      String text, String currentText, String nextText, String previousText) throws Exception {
    HttpRequest httpRequest =
        HttpRequest.newBuilder(URI.create(baseUrl() + text))
            .timeout(Duration.ofSeconds(10L))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("apikey", previousText)
            .header("Authorization", "Bearer " + nextText)
            .header("User-Agent", "ellice-Mod")
            .POST(BodyPublishers.ofString(currentText, StandardCharsets.UTF_8))
            .build();
    return client.send(httpRequest, BodyHandlers.ofString(StandardCharsets.UTF_8));
  }

  private static HttpResponse<String> createText4(
      String text, String currentText, String nextText, String previousText) throws Exception {
    HttpResponse httpResponse = createText3(text, currentText, nextText, previousText);
    if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() <= 299) {
      return httpResponse;
    } else {
      throw new LicenseBaseUrlService.HttpStatus(
          httpResponse.statusCode(), (String) httpResponse.body());
    }
  }

  private static String createText5(String text) {
    try {
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      return jsonObject.has("min") ? jsonObject.get("min").getAsString() : "";
    } catch (Exception exception) {
      return "";
    }
  }

  private static String createText6(String text) {
    try {
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      if (jsonObject.has("error")) {
        return jsonObject.get("error").getAsString();
      }
    } catch (Exception exception) {
    }

    return text.length() > 120 ? text.substring(0, 120) : text;
  }

  private static String createText7(String text) {
    return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
  }

  public record Entitlement(boolean beta, boolean free) {
    boolean allowed() {
      return this.beta || this.free;
    }
  }

  public record Heartbeat(
      String minVersion, LicenseBaseUrlService.HeartbeatResult result, String error) {}

  public enum HeartbeatResult {
    OK,
    UPDATE_REQUIRED,
    DEVICE_LOCKED,
    DENIED,
    NETWORK_ERROR;

    private static LicenseBaseUrlService.HeartbeatResult[] $values() {
      return new LicenseBaseUrlService.HeartbeatResult[] {
        OK, UPDATE_REQUIRED, DEVICE_LOCKED, DENIED, NETWORK_ERROR
      };
    }
  }

  private static final class HttpStatus extends Exception {
    final int code;
    final String body;

    HttpStatus(int value, String text) {
      super("HTTP " + value);
      this.code = value;
      this.body = text;
    }
  }
}
