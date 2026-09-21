package dev.felix.ellice.feature.pearl;

public final class PearlStatusService {
   private boolean enabled;
   private boolean enabled2;
   private long timestamp;
   private long timestamp2 = Long.MIN_VALUE;
   private long timestamp3 = Long.MIN_VALUE;
   private int count;
   private String text = "Watching for void falls";

   public String status() {
      return this.text;
   }

   public boolean thrown() {
      return this.enabled2;
   }

   public void before(long longValue, PearlStatusService.Environment environment) {
      try {
         this.updateState(longValue, environment);
      } catch (RuntimeException | Error runtimeExceptionError) {
         this.updateState3(environment, runtimeExceptionError);
         throw runtimeExceptionError;
      }
   }

   private void updateState(long offset, PearlStatusService.Environment environment) {
      if (offset != this.timestamp2) {
         this.timestamp2 = offset;
         if (!environment.context()) {
            this.stop(environment);
            this.text = "Waiting for a valid player state";
         } else if (environment.grounded()) {
            this.stop(environment);
            if (++this.count >= 2) {
               this.enabled2 = false;
            }

            this.text = "Watching for void falls";
         } else {
            this.count = 0;
            if (this.enabled2) {
               this.text = "Pearl thrown · waiting for landing";
            } else if (!environment.danger()) {
               this.stop(environment);
               this.text = "Fall has support";
            } else {
               if (!this.enabled) {
                  if (!environment.acquire()) {
                     this.text = "No reachable safe pearl landing";
                     return;
                  }

                  this.enabled = true;
                  this.timestamp = offset;
               }

               if (offset - this.timestamp <= 8L && environment.valid()) {
                  this.text = environment.aim() ? "Aiming at a checked landing" : "Recalculating falling launch";
               } else {
                  this.stop(environment);
                  this.text = "Rescue interrupted";
               }
            }
         }
      }
   }

   public void after(PearlStatusService.Environment environment) {
      try {
         this.updateState2(environment);
      } catch (RuntimeException | Error runtimeExceptionError) {
         this.updateState3(environment, runtimeExceptionError);
         throw runtimeExceptionError;
      }
   }

   private void updateState2(PearlStatusService.Environment environment) {
      if (this.enabled && this.timestamp3 != this.timestamp2) {
         this.timestamp3 = this.timestamp2;
         if (environment.context() && environment.valid() && environment.danger()) {
            if (environment.throwPearl()) {
               this.enabled2 = true;
               this.stop(environment);
               this.text = "Pearl thrown · waiting for landing";
            }
         } else {
            this.stop(environment);
         }
      }
   }

   public void stop(PearlStatusService.Environment environment) {
      this.enabled = false;
      environment.release();
   }

   private void updateState3(PearlStatusService.Environment environment, Throwable exception) {
      try {
         this.stop(environment);
      } catch (RuntimeException | Error runtimeExceptionError) {
         if (runtimeExceptionError != exception) {
            exception.addSuppressed(runtimeExceptionError);
         }
      }
   }

   public void reset(PearlStatusService.Environment environment) {
      this.stop(environment);
      this.enabled2 = false;
      this.count = 0;
      this.timestamp2 = this.timestamp3 = Long.MIN_VALUE;
      this.text = "Watching for void falls";
   }

   public interface Environment {
      boolean context();

      boolean grounded();

      boolean danger();

      boolean acquire();

      boolean aim();

      boolean valid();

      boolean throwPearl();

      void release();
   }
}

