package dev.felix.ellice.feature.scaffold;

public record ScaffoldMovementProfile(
   int straightTicks,
   int groundTicks,
   double edgeDistance,
   double minimumSpeed,
   double turnSpeed,
   double clickTurnSpeed,
   double forwardPitch,
   double landingReserve,
   boolean autoSprint,
   boolean earlyReturn
) {
   public ScaffoldMovementProfile(
      int straightTicks,
      int groundTicks,
      double edgeDistance,
      double minimumSpeed,
      double turnSpeed,
      double clickTurnSpeed,
      double forwardPitch,
      double landingReserve,
      boolean autoSprint,
      boolean earlyReturn
   ) {
      if (straightTicks >= 0
         && straightTicks <= 5
         && groundTicks >= 0
         && groundTicks <= 10
         && checkCondition(edgeDistance)
         && checkCondition(minimumSpeed)
         && checkCondition(turnSpeed)
         && checkCondition(clickTurnSpeed)
         && Double.isFinite(forwardPitch)
         && !(forwardPitch < -45.0)
         && !(forwardPitch > 60.0)
         && Double.isFinite(landingReserve)
         && !(landingReserve < 0.0)
         && !(landingReserve > 1.5)) {
         this.straightTicks = straightTicks;
         this.groundTicks = groundTicks;
         this.edgeDistance = edgeDistance;
         this.minimumSpeed = minimumSpeed;
         this.turnSpeed = turnSpeed;
         this.clickTurnSpeed = clickTurnSpeed;
         this.forwardPitch = forwardPitch;
         this.landingReserve = landingReserve;
         this.autoSprint = autoSprint;
         this.earlyReturn = earlyReturn;
      } else {
         throw new IllegalArgumentException("Invalid Telly policy");
      }
   }

   private static boolean checkCondition(double doubleValue) {
      return Double.isFinite(doubleValue) && doubleValue > 0.0;
   }
}
