package dev.felix.ellice.feature.speed;

public interface SpeedOperationHandler {
   boolean bool(String text);

   double number(String text);

   String choice(String text);

   default int integer(String text) {
      return (int)Math.round(this.number(text));
   }
}
