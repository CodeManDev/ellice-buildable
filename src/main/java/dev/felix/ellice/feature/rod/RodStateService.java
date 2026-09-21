package dev.felix.ellice.feature.rod;

public final class RodStateService {
   private RodStateService.State state2 = RodStateService.State.IDLE;
   private RodTrajectorySolver.Kind kind;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private long timestamp4;
   private long timestamp5;
   private long timestamp6 = Long.MIN_VALUE;
   private int count;
   private String text = "Ready";

   public RodStateService.State state() {
      return this.state2;
   }

   public String status() {
      return this.text;
   }

   public boolean active() {
      return this.state2 != RodStateService.State.IDLE;
   }

   public void before(long longValue, RodStateService.Environment environment, RodStateService.Policy policy) {
      try {
         this.updateState(longValue, environment, policy);
      } catch (RuntimeException | Error runtimeExceptionError) {
         this.updateState3(environment, runtimeExceptionError);
         throw runtimeExceptionError;
      }
   }

   private void updateState(long offset, RodStateService.Environment environment, RodStateService.Policy policy) {
      if (!environment.context()) {
         this.stop(environment);
         this.text = "Waiting for free hands";
      } else if (this.active() && !environment.valid()) {
         this.stop(environment);
         this.text = "Hand released";
      } else {
         if (this.state2 == RodStateService.State.IDLE) {
            if (environment.meleeReady()) {
               this.text = "Melee has priority";
               return;
            }

            this.kind = environment.acquire(offset >= this.timestamp4, offset >= this.timestamp5);
            if (this.kind == null) {
               this.text = "Waiting for an opening";
               return;
            }

            this.state2 = RodStateService.State.AIM;
            this.timestamp = offset;
         }

         if (this.state2 == RodStateService.State.AIM) {
            if (offset - this.timestamp > 6L || environment.targetLost() || environment.meleeReady()) {
               this.stop(environment);
               return;
            }

            this.text = "Aiming";
         } else {
            RodStateService.Hook currentHook = environment.hook();
            if (this.state2 == RodStateService.State.WAIT_RESET) {
               if (currentHook == RodStateService.Hook.NONE) {
                  this.stop(environment);
                  this.text = "Rod reset without reeling";
                  return;
               }

               if (offset - this.timestamp < policy.serverWait() && currentHook != RodStateService.Hook.OTHER) {
                  environment.aim(true);
                  this.text = "Waiting for rod reset";
                  return;
               }

               this.stop(environment);
               this.text = "Rod reset ended";
               return;
            }

            if (currentHook == RodStateService.Hook.OTHER) {
               if (policy.saveRod() && environment.resetRod()) {
                  this.state2 = RodStateService.State.WAIT_RESET;
                  this.timestamp = offset;
                  return;
               }

               this.stop(environment);
               this.text = "Foreign hook target";
               return;
            }

            if (this.state2 == RodStateService.State.WAIT_HOOK) {
               if (environment.meleeReady()) {
                  this.stop(environment);
                  this.text = "Melee released while waiting for hook";
                  return;
               }

               if (currentHook != RodStateService.Hook.NONE) {
                  this.state2 = RodStateService.State.FLIGHT;
                  this.timestamp3 = offset;
               } else if (offset - this.timestamp2 >= policy.serverWait()) {
                  this.stop(environment);
                  this.timestamp4 = offset + policy.rodInterval();
                  this.text = "Hook not confirmed";
                  return;
               }

               this.text = "Waiting for hook";
            }

            if (this.state2 == RodStateService.State.FLIGHT) {
               if (currentHook == RodStateService.Hook.NONE) {
                  this.stop(environment);
                  return;
               }

               if (currentHook == RodStateService.Hook.TARGET
                  || currentHook == RodStateService.Hook.BLOCKED
                  || environment.targetLost()
                  || environment.meleeReady()
                  || offset - this.timestamp3 >= Math.min(16, environment.expectedFlight() + 3)) {
                  this.state2 = RodStateService.State.REEL;
                  this.timestamp = offset;
               }

               this.text = "Hook in flight";
            }

            if (this.state2 == RodStateService.State.REEL) {
               if (currentHook == RodStateService.Hook.NONE || offset - this.timestamp > 6L) {
                  this.stop(environment);
                  return;
               }

               this.text = "Reeling in";
            }
         }

         environment.aim(this.state2 == RodStateService.State.REEL);
      }
   }

   public void after(long longValue, RodStateService.Environment environment, RodStateService.Policy policy) {
      try {
         this.updateState2(longValue, environment, policy);
      } catch (RuntimeException | Error runtimeExceptionError) {
         this.updateState3(environment, runtimeExceptionError);
         throw runtimeExceptionError;
      }
   }

   private void updateState2(long offset, RodStateService.Environment environment, RodStateService.Policy policy) {
      if (this.active() && this.timestamp6 != offset) {
         if (!environment.context() || !environment.valid()) {
            this.stop(environment);
         } else if (this.state2 == RodStateService.State.AIM || this.state2 == RodStateService.State.REEL) {
            int value = this.state2 == RodStateService.State.REEL ? 1 : 0;
            if (value != 0 && policy.saveRod()) {
               this.timestamp6 = offset;
               if (environment.resetRod()) {
                  this.state2 = RodStateService.State.WAIT_RESET;
                  this.timestamp = offset;
                  this.text = "Resetting rod without reeling";
               } else {
                  this.stop(environment);
                  this.text = "Rod reset unavailable";
               }
            } else if (environment.aligned((value != 0))) {
               if (value == 0 || environment.hook() != RodStateService.Hook.NONE && environment.hook() != RodStateService.Hook.OTHER) {
                  if (value != 0 || !environment.targetLost() && !environment.meleeReady()) {
                     this.timestamp6 = offset;
                     if (!environment.use((value != 0))) {
                        this.stop(environment);
                        this.timestamp4 = Math.max(this.timestamp4, offset + 8L);
                        this.timestamp5 = Math.max(this.timestamp5, offset + 8L);
                        this.text = "Use was not sent";
                     } else if (value != 0) {
                        this.stop(environment);
                        this.timestamp4 = Math.max(this.timestamp4, offset + 4L);
                        this.text = "Melee released";
                     } else {
                        if (this.kind == RodTrajectorySolver.Kind.ROD) {
                           this.state2 = RodStateService.State.WAIT_HOOK;
                           this.timestamp2 = offset;
                           this.timestamp4 = offset + policy.rodInterval();
                           this.text = "Rod cast";
                        } else {
                           this.timestamp5 = offset + policy.throwInterval();
                           if (++this.count >= policy.burstSize()) {
                              this.count = 0;
                              this.timestamp5 = this.timestamp5 + policy.burstPause();
                           }

                           this.stop(environment);
                           this.text = "Projectile thrown";
                        }
                     }
                  } else {
                     this.stop(environment);
                  }
               } else {
                  this.stop(environment);
               }
            }
         }
      }
   }

   private void updateState3(RodStateService.Environment environment, Throwable exception) {
      try {
         this.stop(environment);
      } catch (RuntimeException | Error runtimeExceptionError) {
         if (runtimeExceptionError != exception) {
            exception.addSuppressed(runtimeExceptionError);
         }
      }
   }

   public void stop(RodStateService.Environment environment) {
      this.state2 = RodStateService.State.IDLE;
      this.kind = null;
      environment.release();
   }

   public void reset(RodStateService.Environment environment) {
      this.stop(environment);
      this.timestamp4 = this.timestamp5 = 0L;
      this.count = 0;
      this.timestamp6 = Long.MIN_VALUE;
      this.text = "Ready";
   }

   public interface Environment {
      boolean context();

      boolean meleeReady();

      RodTrajectorySolver.Kind acquire(boolean enabled, boolean currentEnabled);

      boolean valid();

      void aim(boolean enabled);

      boolean aligned(boolean enabled);

      boolean use(boolean enabled);

      boolean resetRod();

      RodStateService.Hook hook();

      boolean targetLost();

      int expectedFlight();

      void release();
   }

   public enum Hook {
      NONE,
      FLYING,
      TARGET,
      OTHER,
      BLOCKED;


      private static RodStateService.Hook[] $values() {
         return new RodStateService.Hook[]{NONE, FLYING, TARGET, OTHER, BLOCKED};
      }
   }

   public record Policy(int rodInterval, int throwInterval, int burstSize, int burstPause, int serverWait, boolean saveRod) {
      public Policy(int value, int currentValue, int nextValue, int previousValue, int sourceValue) {
         this(value, currentValue, nextValue, previousValue, sourceValue, false);
      }

      public Policy(int rodInterval, int throwInterval, int burstSize, int burstPause, int serverWait, boolean saveRod) {
         rodInterval = Math.max(8, rodInterval);
         throwInterval = Math.max(4, throwInterval);
         burstSize = Math.max(1, burstSize);
         burstPause = Math.max(4, burstPause);
         serverWait = Math.max(10, serverWait);
         this.rodInterval = rodInterval;
         this.throwInterval = throwInterval;
         this.burstSize = burstSize;
         this.burstPause = burstPause;
         this.serverWait = serverWait;
         this.saveRod = saveRod;
      }
   }

   public enum State {
      IDLE,
      AIM,
      WAIT_HOOK,
      FLIGHT,
      REEL,
      WAIT_RESET;


      private static RodStateService.State[] $values() {
         return new RodStateService.State[]{IDLE, AIM, WAIT_HOOK, FLIGHT, REEL, WAIT_RESET};
      }
   }
}
