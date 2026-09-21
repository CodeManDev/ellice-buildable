package dev.felix.ellice.feature.task;

public record TaskBlockPosition(int x, int y, int z) {
   public double distSq(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      double previousDoubleValue = this.x + 0.5 - doubleValue;
      double sourceDoubleValue = this.y + 0.5 - currentDoubleValue;
      double targetDoubleValue = this.z + 0.5 - nextDoubleValue;
      return previousDoubleValue * previousDoubleValue + sourceDoubleValue * sourceDoubleValue + targetDoubleValue * targetDoubleValue;
   }

   @Override
   public String toString() {
      return this.x + "," + this.y + "," + this.z;
   }
}
