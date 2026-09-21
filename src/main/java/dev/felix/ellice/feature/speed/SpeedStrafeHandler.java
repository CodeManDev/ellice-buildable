package dev.felix.ellice.feature.speed;

public interface SpeedStrafeHandler {
   default void enable(SpeedMovingService speedMoving) {
   }

   default void disable(SpeedMovingService speedMoving) {
   }

   default void update(SpeedMovingService speedMoving) {
   }

   default void motion(SpeedMovingService speedMoving) {
   }

   default void afterMotion(SpeedMovingService speedMoving) {
   }

   default void strafe(SpeedMovingService speedMoving) {
   }

   default void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
   }

   default boolean autoJump(SpeedMovingService speedMoving) {
      return false;
   }

   default boolean cancelJump(SpeedMovingService speedMoving) {
      return false;
   }

   default float jumpPower(SpeedMovingService speedMoving, float value) {
      return value;
   }

   default void beforeJump(SpeedMovingService speedMoving) {
   }

   default void afterJump(SpeedMovingService speedMoving) {
   }

   default void correction(SpeedMovingService speedMoving) {
   }

   default void velocity(SpeedMovingService speedMoving, boolean enabled) {
   }

   default void packet(SpeedMovingService speedMoving, SpeedStrafeHandler.Packet currentPacket) {
   }

   final class Move {
      public double x;
      public double y;
      public double z;

      public Move(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
         this.x = doubleValue;
         this.y = currentDoubleValue;
         this.z = nextDoubleValue;
      }

      public void strafe(SpeedMovingService speedMoving, double doubleValue) {
         if (!speedMoving.moving()) {
            this.x = this.z = 0.0;
         } else {
            this.x = -Math.sin(speedMoving.direction()) * doubleValue;
            this.z = Math.cos(speedMoving.direction()) * doubleValue;
         }
      }
   }

   final class Packet {
      public double y;
      public boolean ground;
      public final boolean hasPosition;

      public Packet(double doubleValue, boolean enabled, boolean currentEnabled) {
         this.y = doubleValue;
         this.ground = enabled;
         this.hasPosition = currentEnabled;
      }
   }
}
