package dev.felix.ellice.feature.speed;

final class EtbSpeedMode extends SpeedEnableService {
   private final EtbSpeedMode.Kind kind;
   private double value;
   private double value2;
   private int count;

   EtbSpeedMode(String text, SpeedOperationHandler speedOperation) {
      super(speedOperation);
      this.kind = EtbSpeedMode.Kind.valueOf(text);
   }

   @Override
   protected void reset(SpeedMovingService speedMoving) {
      this.value = this.value2 = 0.0;
      this.count = 0;
   }

   private boolean checkCondition() {
      return this.kind == EtbSpeedMode.Kind.OldGuardian || this.kind == EtbSpeedMode.Kind.GuardianYport;
   }

   @Override
   public void motion(SpeedMovingService speedMoving) {
      if (!this.checkCondition()) {
         this.value2 = speedMoving.lastDistance;
      } else {
         if (this.kind == EtbSpeedMode.Kind.GuardianYport) {
            speedMoving.timer(speedMoving.moving() ? 1.600000023841858 : 1.0);
         }

         if (speedMoving.ground && speedMoving.moving()) {
            speedMoving.my = this.kind == EtbSpeedMode.Kind.OldGuardian
               ? 0.4
               : 0.07999999999999999;
            speedMoving.positionPacket(1.0E-9, speedMoving.ground);
            speedMoving.strafe(
               this.kind == EtbSpeedMode.Kind.OldGuardian
                  ? 1.75
                  : 0.7
            );
         } else {
            speedMoving.strafe();
         }
      }
   }

   @Override
   public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
      if (!this.checkCondition()) {
         double doubleValue = base(speedMoving);
         if (this.kind == EtbSpeedMode.Kind.Bhop) {
            speedMoving.timer(1.0700000524520874);
         }

         if (this.kind == EtbSpeedMode.Kind.Mineplex) {
            speedMoving.timer(1.100000023841858);
         }

         int currentValue = speedMoving.ground && speedMoving.moving() ? 1 : 0;
         if (currentValue != 0 && this.count == 1) {
            this.value = switch (this.kind) {
               case Bhop -> 2.55 * doubleValue - 0.01;
               case HypixelHop -> 1.56 * doubleValue - 0.01;
               default -> 0.58;
            };
            if (this.kind == EtbSpeedMode.Kind.HypixelHop) {
               speedMoving.timer(1.149999976158142);
            }
         } else if (currentValue != 0 && this.count == 2) {
            currentMove.y = speedMoving.my = this.kind == EtbSpeedMode.Kind.Mineplex
               ? 0.3
               : 0.3999;
            this.value = this.kind == EtbSpeedMode.Kind.Mineplex
               ? 0.64
               : this.value
                  * (
                     this.kind == EtbSpeedMode.Kind.Bhop
                        ? 2.1
                        : 1.58
                  );
            if (this.kind == EtbSpeedMode.Kind.HypixelHop) {
               speedMoving.timer(1.2000000476837158);
            }
         } else if (this.count == 3) {
            this.value = this.value2 + 0.66 * (doubleValue - this.value2);
            if (this.kind == EtbSpeedMode.Kind.HypixelHop) {
               speedMoving.timer(1.100000023841858);
            }
         } else {
            if (speedMoving.collision(0.0, speedMoving.my, 0.0) || speedMoving.verticalCollision && this.count > 0) {
               this.count = speedMoving.moving() ? 1 : 0;
            }

            this.value = this.value2 * 0.9937106918238994;
         }

         this.value = Math.max(this.value, doubleValue);
         currentMove.strafe(speedMoving, this.value);
         if (speedMoving.moving()) {
            this.count++;
         }
      }
   }

   enum Kind {
      Bhop,
      HypixelHop,
      Mineplex,
      OldGuardian,
      GuardianYport;


      private static EtbSpeedMode.Kind[] $values() {
         return new EtbSpeedMode.Kind[]{Bhop, HypixelHop, Mineplex, OldGuardian, GuardianYport};
      }
   }
}
