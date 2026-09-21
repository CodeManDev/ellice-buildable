package dev.felix.ellice.account;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

public final class AccountFetchHeadService {
  private static final HttpClient client =
      HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(8L))
          .followRedirects(Redirect.NORMAL)
          .build();
  private static final String text = "https://sessionserver.mojang.com/session/minecraft/profile/";
  private static final int count = 64;

  private AccountFetchHeadService() {}

  public static CompletableFuture<BufferedImage> fetchHead(UUID uUID) {
    if (uUID == null) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("uuid is null"));
    }

    String profileUrl =
        "https://sessionserver.mojang.com/session/minecraft/profile/"
            + uUID.toString().replace("-", "");
    HttpRequest httpRequest =
        HttpRequest.newBuilder(URI.create(profileUrl))
            .timeout(Duration.ofSeconds(10L))
            .header("Accept", "application/json")
            .GET()
            .build();
    return client
        .sendAsync(httpRequest, BodyHandlers.ofString())
        .thenCompose(
            profileResponse -> {
              if (profileResponse.statusCode() != 200) {
                return CompletableFuture.failedFuture(
                    new IOException("profile lookup failed: HTTP " + profileResponse.statusCode()));
              }

              String skinUrl = createText(profileResponse.body());
              return skinUrl == null
                  ? CompletableFuture.failedFuture(
                      new IOException("no SKIN texture in profile properties"))
                  : client.sendAsync(
                      HttpRequest.newBuilder(URI.create(skinUrl))
                          .timeout(Duration.ofSeconds(10L))
                          .GET()
                          .build(),
                      BodyHandlers.ofByteArray());
            })
        .thenApply(
            skinResponse -> {
              if (skinResponse.statusCode() != 200) {
                throw new RuntimeException(
                    "skin download failed: HTTP " + skinResponse.statusCode());
              }

              try {
                BufferedImage bufferedImage =
                    ImageIO.read(new ByteArrayInputStream((byte[]) skinResponse.body()));
                if (bufferedImage == null) {
                  throw new IOException("skin PNG decode returned null");
                } else {
                  return createBufferedImage(bufferedImage);
                }
              } catch (IOException iOException) {
                throw new RuntimeException("skin decode failed", iOException);
              }
            });
  }

  private static String createText(String text) {
    try {
      JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();
      if (!jsonObject.has("properties")) {
        return null;
      }

      for (JsonElement jsonElement : jsonObject.getAsJsonArray("properties")) {
        JsonObject currentJsonObject = jsonElement.getAsJsonObject();
        if (currentJsonObject.has("name")
            && "textures".equals(currentJsonObject.get("name").getAsString())) {
          String currentText = currentJsonObject.get("value").getAsString();
          String nextText =
              new String(Base64.getDecoder().decode(currentText), StandardCharsets.UTF_8);
          JsonObject nextJsonObject = JsonParser.parseString(nextText).getAsJsonObject();
          if (!nextJsonObject.has("textures")) {
            return null;
          }

          JsonObject previousJsonObject = nextJsonObject.getAsJsonObject("textures");
          if (!previousJsonObject.has("SKIN")) {
            return null;
          }

          JsonObject sourceJsonObject = previousJsonObject.getAsJsonObject("SKIN");
          if (!sourceJsonObject.has("url")) {
            return null;
          }

          return sourceJsonObject.get("url").getAsString();
        }
      }
    } catch (Exception exception) {
    }

    return null;
  }

  private static BufferedImage createBufferedImage(BufferedImage bufferedImage) {
    BufferedImage currentBufferedImage = new BufferedImage(64, 64, 2);
    Graphics2D graphics2D = currentBufferedImage.createGraphics();
    graphics2D.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    graphics2D.drawImage(bufferedImage, 0, 0, 64, 64, 8, 8, 16, 16, null);
    if (bufferedImage.getHeight() >= 64) {
      graphics2D.drawImage(bufferedImage, 0, 0, 64, 64, 40, 8, 48, 16, null);
    }

    graphics2D.dispose();
    return currentBufferedImage;
  }
}
