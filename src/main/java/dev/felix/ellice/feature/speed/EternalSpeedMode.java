package dev.felix.ellice.feature.speed;

final class EternalSpeedMode extends SpeedEnableService {
   private final EternalSpeedMode.Kind kind;
   private double value;
   private double value2;
   private int count;
   private boolean enabled;
   private boolean enabled2;

   EternalSpeedMode(String text, SpeedOperationHandler speedOperation) {
      super(speedOperation);
      this.kind = EternalSpeedMode.Kind.valueOf(text);
   }

   @Override
   protected void reset(SpeedMovingService speedMoving) {
      this.value = this.value2 = 0.0;
      this.count = 0;
      this.enabled = this.enabled2 = false;
   }

   private void updateState(SpeedMovingService speedMoving) {
      if (!speedMoving.moving()) {
         this.value = 0.0;
      }

      this.value = this.value + this.values.number("Speed") / 100.0;
      if (speedMoving.moving()) {
         speedMoving.strafe(Math.min(this.values.number("Speed"), this.value));
      } else {
         speedMoving.stop();
      }
   }

   @Override
   public void motion(SpeedMovingService speedMoving) {
      this.value2 = speedMoving.lastDistance;
      if (this.kind == EternalSpeedMode.Kind.Accel) {
         this.updateState(speedMoving);
      }

      if (this.kind == EternalSpeedMode.Kind.YPort) {
         this.enabled2 = true;
      }
   }

   @Override
   public void afterMotion(SpeedMovingService speedMoving) {
      if (this.kind == EternalSpeedMode.Kind.Accel) {
         this.updateState(speedMoving);
      }

      this.enabled2 = false;
   }

   @Override
   public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
      if (this.kind != EternalSpeedMode.Kind.Accel) {
         double doubleValue = base(speedMoving);
         switch (this.kind) {
            case Accel:
               throw new AssertionError();
            case AGC:
               if (speedMoving.ground && speedMoving.moving()) {
                  currentMove.y = 0.41999998688697815;
                  this.value = doubleValue + 0.625;
               } else {
                  this.value = this.value * 0.901;
               }
               break;
            case OldNCP:
               if (Math.round((speedMoving.y - (int)speedMoving.y) * 1000.0) == 138L) {
                  speedMoving.my = speedMoving.my - 0.138;
               }

               if (!speedMoving.moving()) {
                  this.count = 0;
               } else if (speedMoving.ground) {
                  if (this.count == 0) {
                     this.value = 0.4 + doubleValue;
                  } else {
                     this.count = 0;
                     this.value = this.value * 2.149;
                     currentMove.y = speedMoving.my = 0.42;
                  }
               } else {
                  this.value = this.count == 1
                     ? this.value2 + 0.67 * (doubleValue - this.value2)
                     : this.value2 * 0.99375 - 1.0E-4;
               }

               this.count++;
               this.value = Math.max(doubleValue, this.value);
               break;
            case YPort:
               if (!speedMoving.moving()) {
                  this.value = this.value2 = 0.0;
               } else if (speedMoving.ground) {
                  this.enabled = true;
                  this.value = this.value * 2.14999;
                  currentMove.y = speedMoving.my = 0.41999998688697815;
               } else if (this.enabled) {
                  currentMove.y = speedMoving.my = -1.0;
                  this.value = this.value2 + 0.66 * (doubleValue - this.value2);
                  this.enabled = false;
               } else {
                  this.value = this.value2 * 0.99375
                     - 1.0E-4;
               }

               this.value = Math.max(doubleValue, this.value);
         }

         currentMove.strafe(speedMoving, this.value);
      }
   }

   @Override
   public void packet(SpeedMovingService speedMoving, SpeedStrafeHandler.Packet currentPacket) {
      if (this.enabled2) {
         currentPacket.ground = true;
      }
   }

   enum Kind {
      Accel,
      AGC,
      OldNCP,
      YPort;


      private static EternalSpeedMode.Kind[] $values() {
         return new EternalSpeedMode.Kind[]{Accel, AGC, OldNCP, YPort};
      }
   }
}

