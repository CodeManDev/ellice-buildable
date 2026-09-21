package dev.felix.ellice.feature.cape;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class CapeHttpsService {
  private static final HttpClient client =
      HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(8L))
          .followRedirects(Redirect.NORMAL)
          .build();

  private CapeHttpsService() {}

  public static URI https(String text) throws IOException {
    try {
      URI uRI = URI.create(text.strip());
      if ("https".equalsIgnoreCase(uRI.getScheme())
          && uRI.getHost() != null
          && uRI.getUserInfo() == null) {
        return uRI;
      } else {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new IOException("Paste a complete HTTPS image link.");
    }
  }

  public static byte[] get(URI uRI, int value) throws IOException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder(uRI)
            .timeout(Duration.ofSeconds(20L))
            .header("User-Agent", "ElliceClient-CapeStudio/1.0 (desktop GIF and cape viewer)")
            .GET()
            .build();
    CompletableFuture completableFuture =
        client.sendAsync(httpRequest, item -> new CapeHttpsService.LimitedBody(value));

    try {
      HttpResponse httpResponse = (HttpResponse) completableFuture.get(25L, TimeUnit.SECONDS);
      if (httpResponse.statusCode() == 429) {
        throw new IOException("The service is busy. Try again in a minute.");
      } else if (httpResponse.statusCode() == 401 || httpResponse.statusCode() == 403) {
        throw new IOException("Access denied. Check the API key or try a direct image link.");
      } else if (httpResponse.statusCode() != 200) {
        throw new IOException("The image service returned HTTP " + httpResponse.statusCode() + ".");
      } else {
        return (byte[]) httpResponse.body();
      }
    } catch (InterruptedException interruptedException) {
      completableFuture.cancel(true);
      Thread.currentThread().interrupt();
      throw new IOException("Download cancelled.");
    } catch (ExecutionException | TimeoutException executionExceptionTimeoutException) {
      completableFuture.cancel(true);
      throw new IOException(
          "The download failed or timed out. Try again.", executionExceptionTimeoutException);
    }
  }

  private static final class LimitedBody implements BodySubscriber<byte[]> {
    private final int count;
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final CompletableFuture<byte[]> completableFuture = new CompletableFuture<>();
    private Subscription subscription;

    LimitedBody(int value) {
      this.count = value;
    }

    @Override
    public CompletionStage<byte[]> getBody() {
      return this.completableFuture;
    }

    @Override
    public void onSubscribe(Subscription currentSubscription) {
      this.subscription = currentSubscription;
      currentSubscription.request(1L);
    }

    public void onNext(List<ByteBuffer> items) {
      for (ByteBuffer byteBuffer : items) {
        if (byteBuffer.remaining() > this.count - this.byteArrayOutputStream.size()) {
          this.subscription.cancel();
          this.completableFuture.completeExceptionally(new IOException("Download is too large."));
          return;
        }

        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        this.byteArrayOutputStream.writeBytes(bytes);
      }

      this.subscription.request(1L);
    }

    @Override
    public void onError(Throwable exception) {
      this.completableFuture.completeExceptionally(exception);
    }

    @Override
    public void onComplete() {
      this.completableFuture.complete(this.byteArrayOutputStream.toByteArray());
    }
  }
}
