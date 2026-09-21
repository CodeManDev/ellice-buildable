package dev.felix.ellice.event;

public interface EventIsCancelledHandler {
   boolean isCancelled();

   void cancel();

   final class State implements EventIsCancelledHandler {
      private boolean enabled;

      @Override
      public boolean isCancelled() {
         return this.enabled;
      }

      @Override
      public void cancel() {
         this.enabled = true;
      }

      public void setCancelled(boolean currentEnabled) {
         this.enabled = currentEnabled;
      }
   }
}

