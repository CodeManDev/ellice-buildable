package dev.felix.ellice.menu;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MenuLoaderTracker implements AutoCloseable {
   private final MenuLoaderTracker.Loader loader;
   private final ExecutorService executor = Executors.newSingleThreadExecutor(item -> {
      Thread thread = new Thread(item, "ellice-world-library");
      thread.setDaemon(true);
      return thread;
   });
   private final ConcurrentLinkedQueue<MenuLoaderTracker.Result> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
   private List<MenuLoaderTracker.World> items = List.of();
   private boolean enabled;
   private boolean enabled2;
   private String text = "";
   private long timestamp;

   public MenuLoaderTracker(MenuLoaderTracker.Loader currentLoader) {
      this.loader = Objects.requireNonNull(currentLoader);
   }

   public List<MenuLoaderTracker.World> worlds() {
      return this.items;
   }

   public boolean loading() {
      return this.enabled;
   }

   public String error() {
      return this.text;
   }

   public long revision() {
      return this.timestamp;
   }

   public void reload() {
      if (!this.enabled2 && !this.enabled) {
         this.enabled = true;
         this.text = "";
         this.timestamp++;
         this.executor
            .submit(
               () -> {
                  try {
                     List items = this.loader
                        .load()
                        .stream()
                        .sorted(Comparator.comparingLong(MenuLoaderTracker.World::lastPlayed).reversed().thenComparing(MenuLoaderTracker.World::id))
                        .toList();
                     this.concurrentLinkedQueue.add(new MenuLoaderTracker.Result(items, ""));
                  } catch (Exception exception) {
                     CoreIsInitializedHandler.LOGGER.warn("Could not load the world library", exception);
                     this.concurrentLinkedQueue
                        .add(new MenuLoaderTracker.Result(null, "Could not read your worlds. Check the saves folder and try again."));
                  }
               }
            );
      }
   }

   public void tick() {
      if (!this.enabled2) {
         MenuLoaderTracker.Result result;
         while ((result = this.concurrentLinkedQueue.poll()) != null) {
            if (result.worlds != null) {
               this.items = result.worlds;
            }

            this.text = result.error;
            this.enabled = false;
            this.timestamp++;
         }
      }
   }

   @Override
   public void close() {
      this.enabled2 = true;
      this.executor.shutdownNow();
      this.concurrentLinkedQueue.clear();
   }

   @FunctionalInterface
   public interface Loader {
      List<MenuLoaderTracker.World> load() throws Exception;
   }

   private record Result(List<MenuLoaderTracker.World> worlds, String error) {
   }

   public record World(
      String id,
      String name,
      long lastPlayed,
      String mode,
      String version,
      String info,
      boolean hardcore,
      boolean commands,
      boolean experimental,
      boolean canPlay,
      String playLabel,
      boolean canEdit,
      BufferedImage icon
   ) {
      public World(
         String id,
         String name,
         long lastPlayed,
         String mode,
         String version,
         String info,
         boolean hardcore,
         boolean commands,
         boolean experimental,
         boolean canPlay,
         String playLabel,
         boolean canEdit,
         BufferedImage icon
      ) {
         Objects.requireNonNull(id);
         name = name != null && !name.isBlank() ? name : id;
         mode = Objects.requireNonNullElse(mode, "Unknown");
         version = Objects.requireNonNullElse(version, "Unknown");
         info = Objects.requireNonNullElse(info, "");
         playLabel = Objects.requireNonNullElse(playLabel, "Play world");
         this.id = id;
         this.name = name;
         this.lastPlayed = lastPlayed;
         this.mode = mode;
         this.version = version;
         this.info = info;
         this.hardcore = hardcore;
         this.commands = commands;
         this.experimental = experimental;
         this.canPlay = canPlay;
         this.playLabel = playLabel;
         this.canEdit = canEdit;
         this.icon = icon;
      }
   }
}

