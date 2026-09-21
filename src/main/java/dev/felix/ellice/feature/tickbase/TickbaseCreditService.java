package dev.felix.ellice.feature.tickbase;

public final class TickbaseCreditService {
   private int count;
   private int count2;
   private int count3;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private long timestamp4;
   private boolean enabled;
   private boolean enabled2;
   private TickbaseCreditService.Mode mode = TickbaseCreditService.Mode.PAST;

   public int credit() {
      return this.count;
   }

   public int debt() {
      return this.count3;
   }

   public long skipped() {
      return this.timestamp3;
   }

   public long executed() {
      return this.timestamp4;
   }

   public boolean replaying() {
      return this.enabled2;
   }

   public long cooldownRemaining(long longValue) {
      return Math.max(0L, this.timestamp - longValue);
   }

   public String status() {
      return this.enabled2
         ? "Shifting"
         : (
            this.count3 > 0
               ? "Repaying " + this.count3 + " ticks"
               : (this.count > 0 ? "Charging " + this.count + " ticks" : "Ready")
         );
   }

   public boolean beforeTick(long longValue, int value, TickbaseCreditService.Environment environment) {
      return this.beforeTick(longValue, value, TickbaseCreditService.Mode.PAST, environment);
   }

   public boolean beforeTick(long longValue, int value, TickbaseCreditService.Mode mode, TickbaseCreditService.Environment environment) {
      if (this.enabled2) {
         return false;
      }

      this.updateState(mode, environment);
      if (!environment.valid()) {
         this.pause(environment);
         return false;
      }

      if (this.count3 > 0) {
         this.count3--;
         this.timestamp3++;
         return true;
      }

      if (mode == TickbaseCreditService.Mode.FUTURE) {
         return false;
      }

      if (this.count > 0 && !environment.continueShift()) {
         this.pause(environment);
         this.timestamp = Math.max(this.timestamp, longValue + 100L);
         return false;
      }

      if (this.count2 == 0 && this.count > 0) {
         this.enabled = true;
         return false;
      }

      if (this.count2 == 0) {
         if (longValue < this.timestamp) {
            return false;
         }

         this.count2 = Math.clamp(environment.plan(Math.clamp(value, 1, 8)), 0, Math.clamp(value, 1, 8));
         if (this.count2 == 0) {
            return false;
         }
      }

      this.count2--;
      this.count++;
      this.timestamp3++;
      return true;
   }

   public void afterTick(long longValue, long currentLongValue, TickbaseCreditService.Environment environment) {
      if (!this.enabled2 && this.enabled) {
         long nextLongValue = this.timestamp2;
         this.timestamp = Math.max(this.timestamp, longValue + Math.max(0L, currentLongValue));
         this.enabled2 = true;

         try {
            environment.beginShift();

            while (this.count > 0 && this.timestamp2 == nextLongValue && environment.valid() && environment.continueShift()) {
               this.count--;
               this.timestamp4++;
               if (!environment.extraTick()) {
                  this.timestamp4--;
                  break;
               }
            }
         } finally {
            this.enabled2 = false;
            if (this.timestamp2 == nextLongValue) {
               this.enabled = false;
               this.count = this.count2 = 0;
               environment.forget();
            }
         }
      }
   }

   public void afterTick(long longValue, long currentLongValue, int value, TickbaseCreditService.Mode mode, TickbaseCreditService.Environment environment) {
      if (!this.enabled2) {
         this.updateState(mode, environment);
         if (mode == TickbaseCreditService.Mode.PAST) {
            this.afterTick(longValue, currentLongValue, environment);
         } else if (!this.enabled2 && this.count3 <= 0 && longValue >= this.timestamp && environment.valid()) {
            int currentValue = Math.clamp(environment.plan(Math.clamp(value, 1, 8)), 0, Math.clamp(value, 1, 8));
            if (currentValue != 0) {
               long nextLongValue = this.timestamp2;
               this.timestamp = Math.max(this.timestamp, longValue + Math.max(0L, currentLongValue));
               this.enabled2 = true;

               try {
                  environment.beginShift();

                  for (int index = 0; index < currentValue && this.timestamp2 == nextLongValue && environment.valid() && environment.continueShift(); index++) {
                     this.count3++;
                     this.timestamp4++;
                     if (!environment.extraTick()) {
                        this.timestamp4--;
                        this.count3 = Math.max(0, this.count3 - 1);
                        break;
                     }
                  }
               } finally {
                  this.enabled2 = false;
                  if (this.timestamp2 == nextLongValue) {
                     environment.forget();
                  }
               }
            }
         }
      }
   }

   private void updateState(TickbaseCreditService.Mode currentMode, TickbaseCreditService.Environment environment) {
      if (currentMode != this.mode) {
         this.mode = currentMode;
         this.count = this.count2 = 0;
         this.enabled = false;
         environment.forget();
      }
   }

   public void pause(TickbaseCreditService.Environment environment) {
      this.timestamp2++;
      this.count = this.count2 = 0;
      this.enabled = false;
      environment.forget();
   }

   public void rejectSkippedTick(boolean enabled, TickbaseCreditService.Environment environment) {
      this.timestamp3--;
      if (enabled) {
         this.count3++;
      } else {
         this.pause(environment);
      }
   }

   public void reset(TickbaseCreditService.Environment environment) {
      this.pause(environment);
      this.count3 = 0;
      this.timestamp = 0L;
   }

   public interface Environment {
      boolean valid();

      int plan(int value);

      boolean continueShift();

      default void beginShift() {
      }

      boolean extraTick();

      void forget();
   }

   public enum Mode {
      PAST,
      FUTURE;


      private static TickbaseCreditService.Mode[] $values() {
         return new TickbaseCreditService.Mode[]{PAST, FUTURE};
      }
   }
}

