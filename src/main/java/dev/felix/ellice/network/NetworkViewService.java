package dev.felix.ellice.network;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.LongSupplier;

public final class NetworkViewService implements AutoCloseable {
  private final ExecutorService executor;
  private final NetworkViewService.Factory factory;
  private final LongSupplier longSupplier;
  private final NetworkViewService.Sleeper sleeper;
  private final Path path;
  private volatile NetworkViewService.View view2;
  private volatile boolean enabled;

  public NetworkViewService(Path path) {
    this(
        path,
        NetworkInspectService::new,
        Executors.newSingleThreadExecutor(
            item -> {
              Thread thread = new Thread(item, "ellice-fritzbox");
              thread.setDaemon(true);
              return thread;
            }),
        () -> System.nanoTime() / 1000000L,
        Thread::sleep);
  }

  public NetworkViewService(NetworkViewService.Factory value, ExecutorService executor) {
    this(null, value, executor, () -> System.nanoTime() / 1000000L, Thread::sleep);
  }

  NetworkViewService(
      Path currentPath,
      NetworkViewService.Factory value,
      ExecutorService currentExecutor,
      LongSupplier currentLongSupplier,
      NetworkViewService.Sleeper currentSleeper) {
    this.factory = Objects.requireNonNull(value);
    this.executor = Objects.requireNonNull(currentExecutor);
    this.longSupplier = currentLongSupplier;
    this.sleeper = currentSleeper;
    this.path = currentPath == null ? null : currentPath.resolve("ellice/fritzbox-host.txt");
    String text = "fritz.box";
    if (this.path != null && Files.isRegularFile(this.path)) {
      try {
        if (Files.size(this.path) <= 512L) {
          String currentText = Files.readString(this.path).strip();
          NetworkInspectService.address(currentText);
          text = currentText;
        }
      } catch (IOException iOException) {
      }
    }

    this.view2 =
        new NetworkViewService.View(
            NetworkViewService.Phase.IDLE,
            text,
            "Ready to check your FRITZ!Box. No router password needed.",
            null,
            null,
            0L);
  }

  public NetworkViewService.View view() {
    return this.view2;
  }

  public boolean check(String text) {
    return this.checkCondition(text, false);
  }

  public boolean reconnect(String text) {
    return this.checkCondition(text, true);
  }

  private synchronized boolean checkCondition(String text, boolean currentEnabled) {
    if (!this.enabled && !this.view2.busy()) {
      String currentText = text == null ? "" : text.strip();

      try {
        NetworkInspectService.address(currentText);
      } catch (IOException iOException) {
        this.updateState3(
            NetworkViewService.Phase.ERROR, currentText, iOException.getMessage(), null, null, 0L);
        return false;
      }

      this.updateState3(
          currentEnabled
              ? NetworkViewService.Phase.RECONNECTING
              : NetworkViewService.Phase.CHECKING,
          currentText,
          currentEnabled
              ? "Checking the connection before reconnecting…"
              : "Reading your FRITZ!Box connection…",
          null,
          null,
          0L);

      try {
        this.executor.execute(() -> this.updateState(currentText, currentEnabled));
        return true;
      } catch (RejectedExecutionException rejectedExecutionException) {
        this.updateState3(
            NetworkViewService.Phase.ERROR,
            currentText,
            "The network service has stopped.",
            null,
            null,
            0L);
        return false;
      }
    } else {
      return false;
    }
  }

  private void updateState(String text, boolean currentEnabled) {
    NetworkConnectedHandler.Status currentStatus = null;

    try {
      if (this.enabled) {
        return;
      }

      NetworkConnectedHandler networkConnected = this.factory.open(text);
      currentStatus = networkConnected.inspect();
      this.updateState4(text);
      if (!currentEnabled) {
        this.updateState3(
            NetworkViewService.Phase.READY,
            text,
            currentStatus.connected()
                ? (currentStatus.canReconnect()
                    ? "Connected and ready to reconnect."
                    : "Connected. This router does not offer reconnection through UPnP.")
                : "The FRITZ!Box reports: " + currentStatus.connection() + ".",
            null,
            currentStatus,
            0L);
        return;
      }

      if (!currentStatus.canReconnect()) {
        throw new NetworkInspectService.Rejected(
            0, "This router does not offer password-free reconnection. Check its UPnP settings.");
      }

      if (!currentStatus.connected()) {
        throw new NetworkInspectService.Rejected(
            0,
            "No active internet connection was reported. Check the connection in your FRITZ!Box.");
      }

      if (this.enabled || Thread.currentThread().isInterrupted()) {
        return;
      }

      this.updateState3(
          NetworkViewService.Phase.RECONNECTING,
          text,
          "Disconnecting the internet connection…",
          currentStatus,
          currentStatus,
          0L);
      byte byteValue = 1;

      try {
        networkConnected.terminate();
      } catch (NetworkInspectService.Rejected rejected) {
        throw rejected;
      } catch (IOException iOException) {
        byteValue = 0;
      }

      this.updateState2(text, networkConnected, currentStatus, (byteValue != 0));
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } catch (IOException | RuntimeException iOExceptionRuntimeException) {
      String currentText =
          iOExceptionRuntimeException instanceof NetworkInspectService.Rejected
              ? iOExceptionRuntimeException.getMessage()
              : (iOExceptionRuntimeException instanceof IOException
                  ? "The FRITZ!Box is unreachable or returned an invalid response. Check its local"
                        + " address and UPnP status information."
                  : "The network service could not complete the request.");
      this.updateState3(
          NetworkViewService.Phase.ERROR, text, currentText, currentStatus, currentStatus, 0L);
    }
  }

  private void updateState2(
      String text,
      NetworkConnectedHandler networkConnected,
      NetworkConnectedHandler.Status currentStatus,
      boolean currentEnabled)
      throws InterruptedException {
    long offset = this.longSupplier.getAsLong();
    byte byteValue = 0;
    byte currentByteValue = 0;
    byte nextByteValue = 0;
    long currentOffset = offset;
    NetworkConnectedHandler.Status nextStatus = null;

    while (!this.enabled && this.longSupplier.getAsLong() - offset < 90000L) {
      this.updateState3(
          NetworkViewService.Phase.WAITING,
          text,
          currentEnabled
              ? "Waiting for the internet connection to return…"
              : "No response received. Checking recovery without repeating the disconnect request.",
          currentStatus,
          nextStatus,
          (this.longSupplier.getAsLong() - offset) / 1000L);
      this.sleeper.sleep(1500L);
      if (this.enabled || Thread.currentThread().isInterrupted()) {
        return;
      }

      try {
        NetworkConnectedHandler.Status previousStatus = networkConnected.inspect();
        long nextOffset = (this.longSupplier.getAsLong() - offset) / 1000L;
        currentOffset = this.longSupplier.getAsLong();
        byteValue |=
            previousStatus.connected()
                    && (currentStatus.uptime() <= 2L
                        || previousStatus.uptime() < 0L
                        || previousStatus.uptime() + 2L >= currentStatus.uptime())
                ? 0
                : 1;
        nextByteValue =
            (byte)
                (previousStatus.connected()
                        && nextStatus != null
                        && nextStatus.connected()
                        && previousStatus.hasAddress()
                        && previousStatus.sameAddresses(nextStatus)
                    ? nextByteValue + 1
                    : 0);
        nextStatus = previousStatus;
        if (previousStatus.connected()
            && nextByteValue >= 1
            && previousStatus.changedFrom(currentStatus)) {
          this.updateState3(
              NetworkViewService.Phase.CHANGED,
              text,
              "A new WAN address has been confirmed.",
              currentStatus,
              previousStatus,
              nextOffset);
          return;
        }

        if (byteValue != 0
            && previousStatus.connected()
            && nextByteValue >= 1
            && nextOffset >= 8L) {
          int value =
              !currentStatus.hasAddress()
                      || !currentStatus.ipv4().isEmpty() && previousStatus.ipv4().isEmpty()
                      || !currentStatus.ipv6().isEmpty() && previousStatus.ipv6().isEmpty()
                  ? 0
                  : 1;
          this.updateState3(
              value != 0 ? NetworkViewService.Phase.UNCHANGED : NetworkViewService.Phase.UNVERIFIED,
              text,
              value != 0
                  ? "Reconnected. Your provider assigned the same IP address again."
                  : "Reconnected. The router data cannot confirm an IP change.",
              currentStatus,
              previousStatus,
              nextOffset);
          return;
        }

        if ("Disconnected".equals(previousStatus.connection())
            && currentByteValue == 0
            && networkConnected.canRequestConnection()) {
          currentByteValue = 1;

          try {
            networkConnected.requestConnection();
          } catch (IOException iOException) {
          }
        }
      } catch (NetworkInspectService.Rejected rejected) {
        nextByteValue = 0;
        if (rejected.code() == 606) {
          this.updateState3(
              NetworkViewService.Phase.UNVERIFIED,
              text,
              "Reconnection requested, but the router refused the subsequent status check. "
                  + rejected.getMessage(),
              currentStatus,
              nextStatus,
              (this.longSupplier.getAsLong() - offset) / 1000L);
          return;
        }
      } catch (IOException currentIOException) {
        nextByteValue = 0;
      }
    }

    int currentValue =
        nextStatus != null
                && nextStatus.connected()
                && this.longSupplier.getAsLong() - currentOffset < 6000L
            ? 1
            : 0;
    this.updateState3(
        NetworkViewService.Phase.UNVERIFIED,
        text,
        currentValue != 0
            ? "The router reports a connection, but could not confirm a new IP or a completed"
                  + " reconnect."
            : "Recovery was not confirmed within 90 seconds. Check the internet status in your"
                  + " FRITZ!Box.",
        currentStatus,
        nextStatus,
        (this.longSupplier.getAsLong() - offset) / 1000L);
  }

  private void updateState3(
      NetworkViewService.Phase phase,
      String text,
      String currentText,
      NetworkConnectedHandler.Status status,
      NetworkConnectedHandler.Status currentStatus,
      long offset) {
    if (!this.enabled) {
      this.view2 =
          new NetworkViewService.View(phase, text, currentText, status, currentStatus, offset);
    }
  }

  private void updateState4(String text) {
    if (this.path != null) {
      try {
        Files.createDirectories(this.path.getParent());
        Path currentPath = Files.createTempFile(this.path.getParent(), "fritzbox-host-", ".tmp");

        try {
          Files.writeString(currentPath, text);

          try {
            Files.move(
                currentPath,
                this.path,
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING);
          } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
            Files.move(currentPath, this.path, StandardCopyOption.REPLACE_EXISTING);
          }
        } finally {
          Files.deleteIfExists(currentPath);
        }
      } catch (IOException iOException) {
      }
    }
  }

  @Override
  public synchronized void close() {
    this.enabled = true;
    this.executor.shutdownNow();
  }

  @FunctionalInterface
  public interface Factory {
    NetworkConnectedHandler open(String text) throws IOException;
  }

  public enum Phase {
    IDLE,
    CHECKING,
    READY,
    RECONNECTING,
    WAITING,
    CHANGED,
    UNCHANGED,
    UNVERIFIED,
    ERROR;

    private static NetworkViewService.Phase[] $values() {
      return new NetworkViewService.Phase[] {
        IDLE, CHECKING, READY, RECONNECTING, WAITING, CHANGED, UNCHANGED, UNVERIFIED, ERROR
      };
    }
  }

  @FunctionalInterface
  interface Sleeper {
    void sleep(long longValue) throws InterruptedException;
  }

  public record View(
      NetworkViewService.Phase phase,
      String host,
      String message,
      NetworkConnectedHandler.Status before,
      NetworkConnectedHandler.Status current,
      long elapsedSeconds) {
    public boolean busy() {
      return this.phase == NetworkViewService.Phase.CHECKING
          || this.phase == NetworkViewService.Phase.RECONNECTING
          || this.phase == NetworkViewService.Phase.WAITING;
    }
  }
}
