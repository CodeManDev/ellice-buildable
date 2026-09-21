package dev.felix.ellice.feature.scaffold;

public final class ScaffoldObserveService {
   private ScaffoldObserveService.Phase phase2 = ScaffoldObserveService.Phase.RUN_UP;
   private int count;
   private int count2;
   private long timestamp = Long.MIN_VALUE;

   public ScaffoldObserveService.Phase observe(long longValue, boolean enabled, boolean currentEnabled, boolean nextEnabled, int value, boolean previousEnabled) {
      if (longValue <= this.timestamp || this.timestamp != Long.MIN_VALUE && longValue != this.timestamp + 1L) {
         this.reset();
      }

      this.timestamp = longValue;
      if (enabled) {
         this.count = 0;
         this.count2++;
         this.phase2 = ScaffoldObserveService.Phase.RUN_UP;
      } else {
         this.count2 = 0;
         this.count++;
         this.phase2 = this.count <= value
            ? ScaffoldObserveService.Phase.STRAIGHT
            : (previousEnabled && currentEnabled && nextEnabled ? ScaffoldObserveService.Phase.RETURN : ScaffoldObserveService.Phase.BUILD);
      }

      return this.phase2;
   }

   public boolean mayJump(
      boolean enabled, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled, boolean sourceEnabled, boolean targetEnabled, boolean inputEnabled, double doubleValue, ScaffoldMovementProfile scaffoldMovementProfile
   ) {
      return this.phase2 == ScaffoldObserveService.Phase.RUN_UP
         && this.count2 >= Math.max(1, scaffoldMovementProfile.groundTicks())
         && enabled
         && !currentEnabled
         && nextEnabled
         && previousEnabled
         && sourceEnabled
         && targetEnabled
         && inputEnabled
         && doubleValue >= scaffoldMovementProfile.minimumSpeed();
   }

   public ScaffoldObserveService.Phase phase() {
      return this.phase2;
   }

   public int airTicks() {
      return this.count;
   }

   public void reset() {
      this.phase2 = ScaffoldObserveService.Phase.RUN_UP;
      this.count = this.count2 = 0;
      this.timestamp = Long.MIN_VALUE;
   }

   public enum Phase {
      RUN_UP,
      STRAIGHT,
      BUILD,
      RETURN;


      private static ScaffoldObserveService.Phase[] $values() {
         return new ScaffoldObserveService.Phase[]{RUN_UP, STRAIGHT, BUILD, RETURN};
      }
   }
}
