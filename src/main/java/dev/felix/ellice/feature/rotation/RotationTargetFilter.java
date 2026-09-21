package dev.felix.ellice.feature.rotation;

public record RotationTargetFilter(boolean players, boolean mobs, double maxRange, boolean includeInvisible) {
   public RotationTargetFilter(boolean enabled, boolean currentEnabled, double doubleValue) {
      this(enabled, currentEnabled, doubleValue, false);
   }

   public RotationTargetFilter(boolean players, boolean mobs, double maxRange, boolean includeInvisible) {
      if (Double.isFinite(maxRange) && !(maxRange <= 0.0)) {
         this.players = players;
         this.mobs = mobs;
         this.maxRange = maxRange;
         this.includeInvisible = includeInvisible;
      } else {
         throw new IllegalArgumentException("maxRange must be finite and positive");
      }
   }
}
