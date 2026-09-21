package dev.felix.ellice.feature.travel;

public final class TravelFallCostService {
   public static final double COST_INF = 1000000.0;
   public static final double MIN_IMPROVEMENT = 0.01;
   public static final double WALK_ONE_BLOCK = 4.63284688441047;
   public static final double SPRINT_ONE_BLOCK = 3.563791874554526;
   public static final double SNEAK_ONE_BLOCK = 15.384615384615383;
   public static final double WALK_DIAGONAL = 4.63284688441047
      * Math.sqrt(2.0);
   public static final double SPRINT_DIAGONAL = 3.563791874554526
      * Math.sqrt(2.0);
   public static final double SWIM_ONE_BLOCK = 9.091;
   public static final double LADDER_UP_ONE = 5.0;
   public static final double LADDER_DOWN_ONE = 1.43;
   public static final double JUMP_PENALTY = 2.0;
   public static final double PARKOUR_AIR_PER_BLOCK = 2.2;
   public static final double PLACE_ONE_BLOCK = 20.0;
   public static final double BREAK_ONE_BLOCK = 10.0;

   private TravelFallCostService() {
   }

   public static double fallCost(double doubleValue) {
      return doubleValue <= 0.0
         ? 0.0
         : Math.sqrt(2.0 * doubleValue / 0.08)
            + 2.0;
   }

   public static double walkCost(double doubleValue, boolean enabled) {
      return doubleValue * (enabled ? 3.563791874554526 : 4.63284688441047);
   }
}
