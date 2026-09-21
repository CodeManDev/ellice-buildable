package dev.felix.ellice.event;

public sealed interface EventCancelHandler permits EventCancelHandler.Active, EventCancelHandler.Cancelled {
   void cancel();

   boolean isActive();

   record Active(Runnable onCancel) implements EventCancelHandler {
      @Override
      public void cancel() {
         this.onCancel.run();
      }

      @Override
      public boolean isActive() {
         return true;
      }
   }

   record Cancelled() implements EventCancelHandler {
      public static final EventCancelHandler.Cancelled INSTANCE = new EventCancelHandler.Cancelled();

      @Override
      public void cancel() {
      }

      @Override
      public boolean isActive() {
         return false;
      }
   }
}
