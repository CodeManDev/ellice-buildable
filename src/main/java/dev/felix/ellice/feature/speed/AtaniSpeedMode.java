package dev.felix.ellice.feature.speed;

final class AtaniSpeedMode extends SpeedEnableService {
   private final AtaniSpeedMode.Kind kind;
   private boolean enabled;

   AtaniSpeedMode(String text, SpeedOperationHandler speedOperation) {
      super(speedOperation);
      this.kind = AtaniSpeedMode.Kind.valueOf(text);
   }

   @Override
   protected void reset(SpeedMovingService speedMoving) {
      this.enabled = false;
   }

   private static void updateState(SpeedMovingService speedMoving, double doubleValue) {
      double currentDoubleValue = Math.signum(speedMoving.forward);
      double nextDoubleValue = Math.signum(speedMoving.sideways);
      double previousDoubleValue = Math.toRadians(speedMoving.yaw);
      if (currentDoubleValue != 0.0 && nextDoubleValue != 0.0) {
         currentDoubleValue *= Math.sin(0.6398355709958845);
         nextDoubleValue *= Math.cos(0.6398355709958845);
      }

      speedMoving.mx = doubleValue * (-currentDoubleValue * Math.sin(previousDoubleValue) + nextDoubleValue * Math.cos(previousDoubleValue));
      speedMoving.mz = doubleValue * (currentDoubleValue * Math.cos(previousDoubleValue) + nextDoubleValue * Math.sin(previousDoubleValue));
   }

   @Override
   public void update(SpeedMovingService speedMoving) {
      this.updateState2(speedMoving);
   }

   @Override
   public void afterMotion(SpeedMovingService speedMoving) {
      this.updateState2(speedMoving);
   }

   @Override
   public boolean autoJump(SpeedMovingService speedMoving) {
      return (this.kind == AtaniSpeedMode.Kind.IntaveGroundStrafe || this.kind == AtaniSpeedMode.Kind.IncognitoNormal)
         && speedMoving.ground
         && speedMoving.moving();
   }

   private void updateState2(SpeedMovingService speedMoving) {
      if (this.kind == AtaniSpeedMode.Kind.IntaveGroundStrafe && !speedMoving.obstructed()) {
         if (speedMoving.ground && speedMoving.moving()) {
            speedMoving.timer(1.0700000524520874);
            if (this.enabled && speedMoving.speed() < 0.15306319260371434) {
               updateState(speedMoving, 0.15306319260371434);
            }

            this.enabled = true;
         } else {
            speedMoving.airSpeed(
               0.02 + speedMoving.actions.random() / 2000.0
            );
            speedMoving.timer(1.0 + speedMoving.actions.random() / 500.0);
            if (!speedMoving.moving()) {
               this.enabled = false;
            }
         }
      } else if (this.kind == AtaniSpeedMode.Kind.IncognitoNormal) {
         double doubleValue = Math.max(
            0.0,
            0.272 * (1.0 + 0.2 * speedMoving.speedLevel())
               - 0.2874999940395355
         );
         if (!speedMoving.moving()) {
            speedMoving.stop();
         } else {
            updateState(
               speedMoving,
               speedMoving.ground
                  ? 0.36
                     + speedMoving.actions.random() / 70.0
                     + doubleValue
                  : speedMoving.speed()
                     - (speedMoving.actions.random() - 0.5)
                        / 70.0
            );
         }

         if (speedMoving.moving() && speedMoving.speed() < 0.25) {
            updateState(speedMoving, speedMoving.speed() + 0.02);
         }
      }
   }

   @Override
   public void motion(SpeedMovingService speedMoving) {
      this.updateState2(speedMoving);
      switch (this.kind) {
         case Karhu:
            if (speedMoving.jumpKey || !speedMoving.moving()) {
               return;
            }

            speedMoving.sprinting = true;
            if (speedMoving.ground) {
               speedMoving.timer(1.0);
               speedMoving.jump();
               speedMoving.my = speedMoving.my * 0.55;
            } else {
               speedMoving.timer(
                  1.0
                     + (speedMoving.actions.random() - 0.5)
                        / 100.0
               );
            }
         case IntaveGroundStrafe:
         case IncognitoNormal:
         default:
            break;
         case NCPHop:
            if (!speedMoving.moving()) {
               return;
            }

            if (speedMoving.ground) {
               speedMoving.jump();
            }

            if (speedMoving.fallDistance > 0.9) {
               speedMoving.timer(1.1299999952316284);
            } else if (!speedMoving.ground) {
               speedMoving.timer(1.0);
            }

            updateState(speedMoving, speedMoving.speed());
            break;
         case MineMenClub:
            speedMoving.sprinting = speedMoving.moving();
            if (speedMoving.ground && speedMoving.moving()) {
               speedMoving.jump();
            } else if (speedMoving.hurtTime <= 6) {
               updateState(speedMoving, speedMoving.speed());
            }
      }
   }

   @Override
   public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
      if (this.kind == AtaniSpeedMode.Kind.BHop) {
         currentMove.strafe(speedMoving, this.values.number("Boost"));
         if (speedMoving.moving() && speedMoving.ground) {
            currentMove.y = speedMoving.my = this.values.number("Jump height");
         } else if (!speedMoving.moving()) {
            speedMoving.stop();
         }
      }
   }

   enum Kind {
      BHop,
      Karhu,
      IntaveGroundStrafe,
      IncognitoNormal,
      NCPHop,
      MineMenClub;


      private static AtaniSpeedMode.Kind[] $values() {
         return new AtaniSpeedMode.Kind[]{BHop, Karhu, IntaveGroundStrafe, IncognitoNormal, NCPHop, MineMenClub};
      }
   }
}

