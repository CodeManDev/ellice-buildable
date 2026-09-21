package dev.felix.ellice.feature.speed;

final class SlackSpeedMode extends SpeedEnableService {
   private final SlackSpeedMode.Kind kind;
   private double value;
   private int count;

   SlackSpeedMode(String text, SpeedOperationHandler speedOperation) {
      super(speedOperation);
      this.kind = SlackSpeedMode.Kind.valueOf(text);
   }

   @Override
   protected void reset(SpeedMovingService speedMoving) {
      this.value = 0.0;
      this.count = 0;
   }

   private static void updateState(SpeedMovingService speedMoving, double doubleValue) {
      speedMoving.mx = -Math.sin(speedMoving.direction()) * doubleValue;
      speedMoving.mz = Math.cos(speedMoving.direction()) * doubleValue;
   }

   @Override
   public void update(SpeedMovingService speedMoving) {
      switch (this.kind) {
         case HypixelBasic:
            if (speedMoving.ground) {
               if (speedMoving.moving()) {
                  speedMoving.jump();
                  speedMoving.strafe(
                     speedMoving.speedAmplifier < 0
                        ? 0.47999998927116394
                        : 0.47F + 0.024F * potion(speedMoving)
                  );
                  speedMoving.my = speedMoving.jumpHeight(0.41999998688698);
               }
            } else {
               speedMoving.multiply(1.0005);
            }
            break;
         case NCPHop:
            if (speedMoving.ground) {
               if (speedMoving.moving()) {
                  speedMoving.jump();
                  speedMoving.strafe(
                     Math.max(
                        speedMoving.speed(),
                        0.484
                           * (1.0 + 0.13 * potion(speedMoving))
                     )
                  );
               }
            } else {
               speedMoving.multiply(1.001);
               if (speedMoving.airTicks == 5) {
                  speedMoving.my = -0.0784000015258789;
               }

               updateState(speedMoving, speedMoving.speed());
            }
            break;
         case VulcanLow:
            if (speedMoving.ground && speedMoving.moving()) {
               speedMoving.jump();
               speedMoving.strafe(
                  0.54 + 0.02 * speedMoving.actions.random()
               );
            } else if (speedMoving.my > 0.2) {
               speedMoving.my = -0.0784000015258789;
            }
            break;
         case VulcanPort:
            speedMoving.airSpeed(0.02449999935925007);
            if (!speedMoving.ground && speedMoving.airTicks > 3 && speedMoving.my > 0.0) {
               speedMoving.my = -0.27;
            }

            if (!speedMoving.ground && speedMoving.speed() < 0.2150000035762787) {
               speedMoving.strafe(0.2150000035762787);
            }

            if (speedMoving.ground && speedMoving.moving()) {
               speedMoving.jump();
               speedMoving.strafe(Math.max(0.47999998927116394, speedMoving.speed()));
            } else if (!speedMoving.moving()) {
               speedMoving.stop();
            }
         case VerusHop:
      }
   }

   @Override
   public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
      if (this.kind == SlackSpeedMode.Kind.VerusHop) {
         if (speedMoving.ground) {
            if (speedMoving.moving()) {
               currentMove.y = 0.41999998688697815;
            }

            this.value = 0.6600000262260437;
            this.count = 0;
         } else if (this.count++ == 0) {
            this.value = this.value * 0.5934960376506356;
         }

         currentMove.strafe(speedMoving, this.value);
         this.value = this.value * 0.9800000190734863;
      }
   }

   enum Kind {
      HypixelBasic,
      NCPHop,
      VulcanLow,
      VulcanPort,
      VerusHop;


      private static SlackSpeedMode.Kind[] $values() {
         return new SlackSpeedMode.Kind[]{HypixelBasic, NCPHop, VulcanLow, VulcanPort, VerusHop};
      }
   }
}
