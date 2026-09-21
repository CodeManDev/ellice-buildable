package dev.felix.ellice.feature.cape;

public final class CapePositionService {
   private long timestamp = System.nanoTime();
   private double value;
   private double value2 = 1.0;
   private boolean enabled;

   public double position(long offset) {
      return this.value
         + (
            this.enabled
               ? 0.0
               : Math.max(0L, offset - this.timestamp) / 1000000.0 * this.value2
         );
   }

   public void pause(boolean currentEnabled, long longValue) {
      this.value = this.position(longValue);
      this.timestamp = longValue;
      this.enabled = currentEnabled;
   }

   public void speed(double doubleValue, long longValue) {
      this.value = this.position(longValue);
      this.timestamp = longValue;
      this.value2 = Double.isFinite(doubleValue)
         ? Math.clamp(doubleValue, 0.25, 3.0)
         : 1.0;
   }

   public void seek(double doubleValue, long offset) {
      this.value = Math.max(0.0, Double.isFinite(doubleValue) ? doubleValue : 0.0);
      this.timestamp = offset;
   }

   public boolean paused() {
      return this.enabled;
   }
}
