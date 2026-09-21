package dev.felix.ellice.feature.speed;

abstract class SpeedEnableService implements SpeedStrafeHandler {
   protected final SpeedOperationHandler values;

   SpeedEnableService(SpeedOperationHandler speedOperation) {
      this.values = speedOperation;
   }

   protected abstract void reset(SpeedMovingService speedMoving);

   @Override
   public final void enable(SpeedMovingService speedMoving) {
      this.reset(speedMoving);
   }

   @Override
   public final void correction(SpeedMovingService speedMoving) {
      this.updateState(speedMoving);
      this.reset(speedMoving);
   }

   @Override
   public void disable(SpeedMovingService speedMoving) {
      this.updateState(speedMoving);
      this.reset(speedMoving);
   }

   private void updateState(SpeedMovingService speedMoving) {
      speedMoving.timer(1.0);
      speedMoving.airSpeed(Double.NaN);
   }

   protected static int potion(SpeedMovingService speedMoving) {
      return Math.max(0, speedMoving.speedAmplifier + 1);
   }

   protected static double base(SpeedMovingService speedMoving) {
      return 0.2873 * (1.0 + 0.2 * potion(speedMoving));
   }

   protected static double predict(double doubleValue, int value) {
      for (int index = 0; index < value; index++) {
         doubleValue = (doubleValue - 0.08) * 0.9800000190734863;
      }

      return doubleValue;
   }
}
