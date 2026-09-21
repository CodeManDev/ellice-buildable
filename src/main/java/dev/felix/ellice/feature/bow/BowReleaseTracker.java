package dev.felix.ellice.feature.bow;

import java.util.UUID;

public final class BowReleaseTracker {
   private UUID uUID;
   private long timestamp = Long.MIN_VALUE;
   private int count;

   public boolean ready(long longValue, UUID currentUUID, int value, int currentValue, double doubleValue, boolean enabled) {
      if (enabled && !(doubleValue < 0.65)) {
         if (!currentUUID.equals(this.uUID) || longValue != this.timestamp + 1L) {
            this.count = 0;
         }

         this.uUID = currentUUID;
         this.timestamp = longValue;
         this.count++;
         return this.count >= 3 && value >= currentValue;
      } else {
         this.resetAim();
         return false;
      }
   }

   public void released(long longValue) {
      this.resetAim();
   }

   public void clear() {
      this.resetAim();
   }

   public void resetAim() {
      this.uUID = null;
      this.count = 0;
      this.timestamp = Long.MIN_VALUE;
   }
}
