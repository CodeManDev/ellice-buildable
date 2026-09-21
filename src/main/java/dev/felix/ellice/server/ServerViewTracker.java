package dev.felix.ellice.server;

import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.render.compositor.CompositorEmptyService;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;

public final class ServerViewTracker implements AutoCloseable {
  public static final long REFRESH_MS = 30000L;
  private final ServerKeyService serverKeyService;
  private final Path path;
  private final ExecutorService executor =
      Executors.newFixedThreadPool(
          4, item -> Thread.ofPlatform().daemon().name("ellice-server-status").unstarted(item));
  private final ExecutorService executor2 =
      Executors.newSingleThreadExecutor(
          item -> Thread.ofPlatform().daemon().name("ellice-server-library").unstarted(item));
  private final ConcurrentLinkedQueue<ServerViewTracker.Result> concurrentLinkedQueue =
      new ConcurrentLinkedQueue<>();
  private final Map<String, ServerViewTracker.Entry> text = new HashMap<>();
  private final Set<String> text2 = new HashSet<>();
  private List<CompatSavedService.Saved> items = List.of();
  private CompatSavedService.Session session2;
  private long timestamp2;
  private long timestamp3;
  private long timestamp4;
  private int count;
  private volatile String text3 = "";
  private volatile boolean enabled;

  public ServerViewTracker(Path currentPath) {
    this.serverKeyService = new ServerKeyService(currentPath);
    this.path = currentPath.resolve("ellice/cache/server-icons");
    if (!this.serverKeyService.writable()) {
      this.text3 = "Server history could not be loaded. Changes are kept for this session.";
    }
  }

  public ServerKeyService library() {
    return this.serverKeyService;
  }

  public List<CompatSavedService.Saved> saved() {
    return this.items;
  }

  public long revision() {
    return this.timestamp4;
  }

  public String saveError() {
    return this.text3;
  }

  public void reloadSaved() {
    this.items = List.copyOf(CompatSavedService.saved());

    for (CompatSavedService.Saved currentSaved : this.items) {
      this.serverKeyService.remember(currentSaved.address(), currentSaved.name());
      this.seedIcon(currentSaved.address(), currentSaved.icon());
    }

    this.timestamp4++;
  }

  public void toggleFavorite(UUID uUID, String text) {
    this.serverKeyService.toggleFavorite(uUID, text);
    this.timestamp4++;
    this.updateState3();
  }

  public ServerViewTracker.View view(String currentText) {
    ServerViewTracker.Entry entry = this.text.get(currentText);
    return entry == null
        ? new ServerViewTracker.View(
            null, 0L, false, false, CompositorEmptyService.empty(), CompositorEmptyService.empty())
        : new ServerViewTracker.View(
            entry.status,
            entry.checkedAt,
            entry.reachable,
            entry.loading,
            entry.playerGraph,
            entry.pingGraph);
  }

  public BufferedImage icon(String currentText) {
    ServerViewTracker.Entry entry = this.text.get(currentText);
    return entry == null ? null : entry.icon;
  }

  public void request(String currentText, boolean currentEnabled) {
    if (!this.enabled && this.count < 4) {
      ServerViewTracker.Entry entry =
          this.text.computeIfAbsent(currentText, item -> new ServerViewTracker.Entry());
      long longValue = System.currentTimeMillis();
      if (!entry.loading && longValue - entry.checkedAt >= (currentEnabled ? 5000L : 30000L)) {
        entry.loading = true;
        this.count++;
        this.timestamp4++;
        int value = CompatSavedService.protocol();
        this.executor.execute(
            () -> {
              ServerQueryService.Status status = null;
              BufferedImage bufferedImage = null;

              try {
                ParsedServerEndpoint parsedServerEndpoint = ParsedServerEndpoint.parse(currentText);
                status =
                    new ServerQueryService()
                        .query(
                            parsedServerEndpoint,
                            CompatSavedService.resolve(parsedServerEndpoint),
                            value);
                bufferedImage = ServerCodec.decode(status.icon());
                if (bufferedImage != null) {
                  this.updateState2(currentText, status.icon());
                }
              } catch (Exception exception) {
              }

              if (!this.enabled) {
                this.concurrentLinkedQueue.add(
                    new ServerViewTracker.Result(currentText, status, bufferedImage));
              }
            });
      }
    }
  }

  public void seedIcon(String text, byte[] bytes) {
    if (!this.enabled && this.text2.add(text)) {
      this.executor2.execute(
          () -> {
            BufferedImage bufferedImage = ServerCodec.decode(bytes);
            if (bufferedImage != null) {
              this.updateState2(text, bytes);
            }

            if (bufferedImage == null) {
              Path path = this.createPath(text);

              try {
                if (Files.exists(path) && Files.size(path) <= 262144L) {
                  bufferedImage = ServerCodec.decode(Files.readAllBytes(path));
                }
              } catch (Exception exception) {
              }
            }

            if (!this.enabled && bufferedImage != null) {
              this.concurrentLinkedQueue.add(
                  new ServerViewTracker.Result(text, null, bufferedImage));
            }
          });
    }
  }

  public void tick() {
    if (!this.enabled) {
      long longValue = System.currentTimeMillis();

      ServerViewTracker.Result result;
      while ((result = this.concurrentLinkedQueue.poll()) != null) {
        ServerViewTracker.Entry entry =
            this.text.computeIfAbsent(result.address(), item -> new ServerViewTracker.Entry());
        if (result.status() != null || result.icon() == null) {
          entry.loading = false;
          this.count--;
          entry.checkedAt = longValue;
          entry.reachable = result.status() != null;
          if (result.status() != null) {
            entry.status = result.status();
          }

          if (result.icon() != null) {
            entry.icon = result.icon();
          }

          updateState(
              entry.players, longValue, result.status() == null ? -1.0 : result.status().players());
          updateState(
              entry.ping, longValue, result.status() == null ? -1.0 : result.status().latencyMs());
          entry.playerGraph = new CompositorEmptyService(entry.players);
          entry.pingGraph = new CompositorEmptyService(entry.ping);
        } else if (entry.icon == null) {
          entry.icon = result.icon();
        }

        this.timestamp4++;
      }

      this.trackSession(CompatSavedService.session(), longValue, System.nanoTime());
      if (longValue - this.timestamp3 >= 30000L) {
        this.timestamp3 = longValue;
        this.updateState3();
      }
    }
  }

  private static void updateState(
      List<CompositorEmptyService.Sample> items, long offset, double doubleValue) {
    if (!items.isEmpty()
        && offset < ((CompositorEmptyService.Sample) items.getLast()).timestamp()) {
      items.clear();
    }

    items.add(new CompositorEmptyService.Sample(offset, doubleValue));

    while (items.size() > 40) {
      items.removeFirst();
    }
  }

  void trackSession(CompatSavedService.Session session, long longValue, long currentLongValue) {
    int value =
        (this.session2 != null || session != null)
                && (this.session2 == null
                    || session == null
                    || !this.session2.account().equals(session.account())
                    || !this.session2.server().address().equals(session.server().address()))
            ? 0
            : 1;
    if (this.session2 != null && this.timestamp2 != 0L) {
      long nextLongValue =
          Math.max(0L, Math.min(5000L, (currentLongValue - this.timestamp2) / 1000000L));
      this.serverKeyService.played(
          this.session2.account(), this.session2.server().address(), nextLongValue);
    }

    if (value == 0) {
      this.session2 = session;
      if (session != null) {
        this.serverKeyService.joined(
            session.account(), session.server().address(), session.server().name(), longValue);
        this.seedIcon(session.server().address(), session.server().icon());
      }

      this.timestamp4++;
      this.updateState3();
    }

    this.timestamp2 = currentLongValue;
  }

  private Path createPath(String currentPath) {
    return this.path.resolve(
        UUID.nameUUIDFromBytes(currentPath.getBytes(StandardCharsets.UTF_8)) + ".png");
  }

  private void updateState2(String text, byte[] bytes) {
    try {
      Files.createDirectories(this.path);
      Path currentPath = Files.createTempFile(this.path, "icon-", ".tmp");

      try {
        Files.write(currentPath, bytes);

        try {
          Files.move(
              currentPath,
              this.createPath(text),
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
          Files.move(currentPath, this.createPath(text), StandardCopyOption.REPLACE_EXISTING);
        }
      } finally {
        Files.deleteIfExists(currentPath);
      }
    } catch (Exception exception) {
    }
  }

  private void updateState3() {
    if (!this.enabled
        && (this.serverKeyService.dirty() || !this.text3.isBlank())
        && this.serverKeyService.writable()) {
      String text = this.serverKeyService.snapshot();
      this.executor2.execute(
          () -> {
            try {
              this.serverKeyService.writeSnapshot(text);
              this.text3 = "";
            } catch (Exception exception) {
              this.text3 = "Server changes could not be saved to disk.";
              LoggerFactory.getLogger(ServerViewTracker.class)
                  .warn("Could not save server library", exception);
            }
          });
    }
  }

  @Override
  public void close() {
    if (!this.enabled) {
      this.trackSession(null, System.currentTimeMillis(), System.nanoTime());
      this.updateState3();
      this.enabled = true;
      this.executor.shutdownNow();
      this.executor2.shutdown();

      try {
        this.executor2.awaitTermination(3L, TimeUnit.SECONDS);
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      }

      this.concurrentLinkedQueue.clear();
    }
  }

  private static final class Entry {
    ServerQueryService.Status status;
    long checkedAt;
    boolean reachable;
    boolean loading;
    BufferedImage icon;
    final List<CompositorEmptyService.Sample> players = new ArrayList<>();
    final List<CompositorEmptyService.Sample> ping = new ArrayList<>();
    CompositorEmptyService playerGraph = CompositorEmptyService.empty();
    CompositorEmptyService pingGraph = CompositorEmptyService.empty();
  }

  private record Result(String address, ServerQueryService.Status status, BufferedImage icon) {}

  public record View(
      ServerQueryService.Status status,
      long checkedAt,
      boolean reachable,
      boolean loading,
      CompositorEmptyService players,
      CompositorEmptyService ping) {}
}
