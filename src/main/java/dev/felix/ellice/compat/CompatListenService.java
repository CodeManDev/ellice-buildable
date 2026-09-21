package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class CompatListenService {
   private static final AtomicReference<CompatListenService.Listener> items = new AtomicReference<>();

   private CompatListenService() {
   }

   public static CompatListenService.Registration listen(CompatListenService.Listener currentListener) {
      Objects.requireNonNull(currentListener, "listener");
      if (!items.compareAndSet(null, currentListener)) {
         throw new IllegalStateException("A player-model capture listener is already installed");
      } else {
         return () -> items.compareAndSet(currentListener, null);
      }
   }

   public static CompatListenService.Ticket request(UUID uUID, int value) {
      Objects.requireNonNull(uUID, "playerId");
      CompatListenService.Listener listener = items.get();
      if (listener == null) {
         return null;
      }

      try {
         return listener.wantsCapture(uUID, value) ? new CompatListenService.Ticket(uUID, value, listener) : null;
      } catch (RuntimeException exception) {
         updateState(listener, "requesting player-model capture", exception);
         return null;
      }
   }

   private static void updateState(CompatListenService.Listener currentListener, String text, RuntimeException exception) {
      items.compareAndSet(currentListener, null);
      CoreIsInitializedHandler.LOGGER.error("Disabled player-model capture listener after failure while {}", text, exception);
   }

   public interface Listener {
      boolean wantsCapture(UUID uUID, int value);

      void onCapture(CompatPlayerIdService compatPlayerId);
   }

   @FunctionalInterface
   public interface Registration extends AutoCloseable {
      @Override
      void close();
   }

   public static final class Ticket {
      private final UUID uUID;
      private final int count;
      private final CompatListenService.Listener items2;
      private boolean enabled;

      private Ticket(UUID currentUUID, int value, CompatListenService.Listener listener) {
         this.uUID = currentUUID;
         this.count = value;
         this.items2 = listener;
      }

      public void publish(float[] floats, float[] currentFloats) {
         if (this.enabled) {
            throw new IllegalStateException("Capture ticket already published");
         }

         this.enabled = true;
         if (CompatListenService.items.get() == this.items2) {
            try {
               CompatPlayerIdService compatPlayerId = new CompatPlayerIdService(this.uUID, this.count, floats, currentFloats);
               if (compatPlayerId.hasGeometry()) {
                  this.items2.onCapture(compatPlayerId);
               }
            } catch (RuntimeException exception) {
               CompatListenService.updateState(this.items2, "publishing player-model capture", exception);
            }
         }
      }
   }
}

