package dev.felix.ellice.feature.scaffold;

public final class ScaffoldStateController {
   private Integer integer;
   private Integer integer2;
   private int count;

   public int update(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, Integer value) {
      if (value != null) {
         this.integer = value;
         this.integer2 = null;
         this.count = 0;
         return this.integer;
      }

      if (scaffoldPlayerSnapshot.onGround()) {
         int currentY = calculateValue(scaffoldPlayerSnapshot.feetPosition().y() - 0.05);
         if (this.integer == null) {
            this.integer = currentY;
            this.integer2 = currentY;
            this.count = 1;
         } else if (currentY == this.integer) {
            this.integer2 = currentY;
            this.count = 0;
         } else if (this.integer2 != null && this.integer2 == currentY) {
            this.count++;
            if (this.count >= 2) {
               this.integer = currentY;
               this.count = 0;
            }
         } else {
            this.integer2 = currentY;
            this.count = 1;
         }
      } else {
         this.integer2 = null;
         this.count = 0;
         if (this.integer != null
            && scaffoldPlayerSnapshot.velocity().y() < 0.0
            && scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6 < this.integer.intValue() + 1.0) {
            this.integer = Math.min(this.integer, calculateValue(scaffoldPlayerSnapshot.feetPosition().y() - 1.0));
         }
      }

      if (this.integer == null) {
         this.integer = calculateValue(scaffoldPlayerSnapshot.feetPosition().y() - 1.0);
      }

      return this.integer;
   }

   public Integer current() {
      return this.integer;
   }

   public void reset() {
      this.integer = null;
      this.integer2 = null;
      this.count = 0;
   }

   private static int calculateValue(double doubleValue) {
      return (int)Math.floor(doubleValue);
   }
}
