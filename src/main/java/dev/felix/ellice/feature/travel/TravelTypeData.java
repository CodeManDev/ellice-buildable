package dev.felix.ellice.feature.travel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record TravelTypeData(
   int destX, double destY, int destZ, double costTicks, TravelTypeData.Type type, boolean placesBlock, int placeX, int placeY, int placeZ
) {
   private static final int[][] CARDINAL = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] DIAGONAL = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public TravelTypeData(
      int destX, double destY, int destZ, double costTicks, TravelTypeData.Type type, boolean placesBlock, int placeX, int placeY, int placeZ
   ) {
      if (Double.isFinite(destY)
         && Double.isFinite(costTicks)
         && !(costTicks <= 0.0)
         && !(costTicks >= 1000000.0)) {
         Objects.requireNonNull(type, "type");
         this.destX = destX;
         this.destY = destY;
         this.destZ = destZ;
         this.costTicks = costTicks;
         this.type = type;
         this.placesBlock = placesBlock;
         this.placeX = placeX;
         this.placeY = placeY;
         this.placeZ = placeZ;
      } else {
         throw new IllegalArgumentException("Invalid move target or cost");
      }
   }

   static TravelTypeData walk(int value, double doubleValue, int currentValue, double currentDoubleValue, TravelTypeData.Type type) {
      return new TravelTypeData(value, doubleValue, currentValue, currentDoubleValue, type, false, 0, 0, 0);
   }

   static TravelTypeData place(int value, double doubleValue, int currentValue, double currentDoubleValue, TravelTypeData.Type type, int nextValue, int previousValue, int sourceValue) {
      return new TravelTypeData(value, doubleValue, currentValue, currentDoubleValue, type, true, nextValue, previousValue, sourceValue);
   }

   public static List<TravelTypeData> generate(TravelPolicy travelPolicy, TravelLoadedHandler travelLoaded, int value, double doubleValue, int currentValue) {
      ArrayList arrayList = new ArrayList(12);
      boolean currentCanSprint = travelPolicy.canSprint();
      double currentDoubleValue = currentCanSprint ? 3.563791874554526 : 4.63284688441047;
      double nextDoubleValue = currentCanSprint ? TravelFallCostService.SPRINT_DIAGONAL : TravelFallCostService.WALK_DIAGONAL;

      for (int[] ints : CARDINAL) {
         int nextValue = value + ints[0];
         int previousValue = currentValue + ints[1];
         if (travelLoaded.loaded(nextValue, previousValue)) {
            Double previousDoubleValue = travelLoaded.floor(nextValue, previousValue, doubleValue);
            if (previousDoubleValue != null) {
               updateState(travelPolicy, travelLoaded, arrayList, value, doubleValue, currentValue, nextValue, previousDoubleValue, previousValue, currentDoubleValue, false);
            } else {
               updateState2(travelPolicy, travelLoaded, arrayList, value, doubleValue, currentValue, ints[0], ints[1], currentDoubleValue);
               updateState3(travelPolicy, travelLoaded, arrayList, value, doubleValue, currentValue, nextValue, previousValue, currentDoubleValue);
            }
         }
      }

      if (travelPolicy.allowDiagonal()) {
         for (int[] currentInts : DIAGONAL) {
            int sourceValue = value + currentInts[0];
            int targetValue = currentValue + currentInts[1];
            if (travelLoaded.loaded(sourceValue, targetValue)) {
               Double sourceDoubleValue = travelLoaded.floor(value + currentInts[0], currentValue, doubleValue);
               Double targetDoubleValue = travelLoaded.floor(value, currentValue + currentInts[1], doubleValue);
               if (sourceDoubleValue != null && targetDoubleValue != null) {
                  Double inputDoubleValue = travelLoaded.floor(sourceValue, targetValue, doubleValue);
                  if (inputDoubleValue != null) {
                     updateState(travelPolicy, travelLoaded, arrayList, value, doubleValue, currentValue, sourceValue, inputDoubleValue, targetValue, nextDoubleValue, true);
                  }
               }
            }
         }
      }

      updateState5(travelLoaded, arrayList, value, doubleValue, currentValue);
      updateState4(travelPolicy, travelLoaded, arrayList, value, doubleValue, currentValue);
      return arrayList;
   }

   private static void updateState(
      TravelPolicy travelPolicy,
      TravelLoadedHandler travelLoaded,
      List<TravelTypeData> items,
      int value,
      double doubleValue,
      int currentValue,
      int nextValue,
      double currentDoubleValue,
      int previousValue,
      double nextDoubleValue,
      boolean enabled
   ) {
      if (travelLoaded.headroom(nextValue, currentDoubleValue, previousValue)) {
         TravelLoadedHandler.Hazard currentHazard = travelLoaded.hazard(nextValue, currentDoubleValue, previousValue);
         if (currentHazard != TravelLoadedHandler.Hazard.VOID && currentHazard != TravelLoadedHandler.Hazard.DANGER) {
            double previousDoubleValue = currentDoubleValue - doubleValue;
            int sourceValue = !travelLoaded.water(nextValue, currentDoubleValue, previousValue) && !travelLoaded.water(value, doubleValue, currentValue) ? 0 : 1;
            if (sourceValue != 0) {
               items.add(
                  walk(
                     nextValue,
                     currentDoubleValue,
                     previousValue,
                     9.091 + Math.abs(previousDoubleValue) * 2.0,
                     TravelTypeData.Type.SWIM
                  )
               );
            } else if (travelLoaded.climbable(nextValue, currentDoubleValue, previousValue) || travelLoaded.climbable(value, doubleValue, currentValue)) {
               double sourceDoubleValue = previousDoubleValue >= 0.0 ? 5.0 : 1.43;
               items.add(walk(nextValue, currentDoubleValue, previousValue, sourceDoubleValue + nextDoubleValue * 0.2, TravelTypeData.Type.CLIMB));
            } else if (travelLoaded.passableDoor(nextValue, currentDoubleValue, previousValue)) {
               items.add(walk(nextValue, currentDoubleValue, previousValue, nextDoubleValue + 1.0, TravelTypeData.Type.DOOR));
            } else if (!(previousDoubleValue > 1.01)) {
               if (!(previousDoubleValue > 0.01)) {
                  double targetDoubleValue = -previousDoubleValue;
                  if (!(targetDoubleValue > travelPolicy.maxFallHeight() + 0.01)) {
                     double inputDoubleValue = targetDoubleValue <= 3.01
                        ? nextDoubleValue + targetDoubleValue * 0.3
                        : nextDoubleValue + TravelFallCostService.fallCost(targetDoubleValue) * 0.4;
                     items.add(
                        walk(
                           nextValue,
                           currentDoubleValue,
                           previousValue,
                           inputDoubleValue,
                           targetDoubleValue > 0.5 ? TravelTypeData.Type.FALL : TravelTypeData.Type.TRAVERSE
                        )
                     );
                  }
               } else if (!enabled || travelPolicy.allowDiagonalAscend()) {
                  if (travelLoaded.jumpHeadroom(nextValue, currentDoubleValue, previousValue) || !(previousDoubleValue > 0.5)) {
                     items.add(
                        walk(
                           nextValue,
                           currentDoubleValue,
                           previousValue,
                           nextDoubleValue + travelPolicy.jumpPenalty() + previousDoubleValue * 1.2,
                           enabled ? TravelTypeData.Type.DIAGONAL_ASCEND : TravelTypeData.Type.ASCEND
                        )
                     );
                  }
               }
            }
         }
      }
   }

   private static void updateState2(
      TravelPolicy travelPolicy, TravelLoadedHandler travelLoaded, List<TravelTypeData> items, int value, double doubleValue, int currentValue, int nextValue, int previousValue, double currentDoubleValue
   ) {
      if (travelPolicy.allowParkour()) {
         if (travelLoaded.jumpHeadroom(value, doubleValue, currentValue)) {
            int sourceValue = travelPolicy.canSprint() ? 4 : 3;

            for (int index = 2; index <= sourceValue; index++) {
               int targetValue = value + nextValue * index;
               int inputValue = currentValue + previousValue * index;
               if (!travelLoaded.loaded(targetValue, inputValue)) {
                  break;
               }

               byte byteValue = 0;

               for (int currentIndex = 1; currentIndex < index; currentIndex++) {
                  int outputValue = value + nextValue * currentIndex;
                  int resultValue = currentValue + previousValue * currentIndex;
                  if (!travelLoaded.loaded(outputValue, resultValue)) {
                     byteValue = 1;
                     break;
                  }

                  Double nextDoubleValue = travelLoaded.floor(outputValue, resultValue, doubleValue);
                  if (nextDoubleValue != null && nextDoubleValue >= doubleValue - 0.5) {
                     byteValue = 1;
                     break;
                  }

                  if (!travelLoaded.headroom(outputValue, doubleValue + 0.5, resultValue)
                     || !travelLoaded.headroom(outputValue, doubleValue + 1.2, resultValue)) {
                     byteValue = 1;
                     break;
                  }

                  TravelLoadedHandler.Hazard currentHazard = travelLoaded.hazard(outputValue, doubleValue, resultValue);
                  if (currentHazard != TravelLoadedHandler.Hazard.DANGER && currentHazard == TravelLoadedHandler.Hazard.VOID) {
                  }
               }

               if (byteValue == 0) {
                  Double previousDoubleValue = travelLoaded.floor(targetValue, inputValue, doubleValue + (travelPolicy.allowParkourAscend() ? 1.0 : 0.0));
                  if (previousDoubleValue != null) {
                     double sourceDoubleValue = previousDoubleValue - doubleValue;
                     if (!(sourceDoubleValue > 1.01)
                        && !(sourceDoubleValue < -travelPolicy.maxFallHeight())
                        && travelLoaded.headroom(targetValue, previousDoubleValue, inputValue)
                        && travelLoaded.jumpHeadroom(targetValue, previousDoubleValue, inputValue)) {
                        TravelLoadedHandler.Hazard nextHazard = travelLoaded.hazard(targetValue, previousDoubleValue, inputValue);
                        if (nextHazard != TravelLoadedHandler.Hazard.DANGER && nextHazard != TravelLoadedHandler.Hazard.VOID) {
                           int candidateValue = sourceDoubleValue > 0.5 ? 1 : 0;
                           if (candidateValue == 0 || travelPolicy.allowParkourAscend()) {
                              double targetDoubleValue = currentDoubleValue * index
                                 + 2.2 * (index - 1)
                                 + travelPolicy.jumpPenalty()
                                 + Math.max(0.0, sourceDoubleValue) * 1.5;
                              items.add(
                                 walk(targetValue, previousDoubleValue, inputValue, targetDoubleValue, candidateValue != 0 ? TravelTypeData.Type.PARKOUR_ASCEND : TravelTypeData.Type.PARKOUR)
                              );
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void updateState3(
      TravelPolicy travelPolicy, TravelLoadedHandler travelLoaded, List<TravelTypeData> items, int value, double doubleValue, int currentValue, int nextValue, int previousValue, double currentDoubleValue
   ) {
      if (travelPolicy.allowBridge() && travelPolicy.canPlace()) {
         if (travelLoaded.placeAllowed(nextValue, previousValue)) {
            int sourceValue = (int)Math.floor(doubleValue) - 1;
            if (travelLoaded.placeable(nextValue, sourceValue, previousValue)) {
               double nextDoubleValue = sourceValue + 1.0;
               if (!(Math.abs(nextDoubleValue - doubleValue) > 1.01)) {
                  if (travelLoaded.headroom(nextValue, nextDoubleValue, previousValue)) {
                     items.add(place(nextValue, nextDoubleValue, previousValue, currentDoubleValue + travelPolicy.placePenalty(), TravelTypeData.Type.BRIDGE, nextValue, sourceValue, previousValue));
                  }
               }
            }
         }
      }
   }

   private static void updateState4(TravelPolicy travelPolicy, TravelLoadedHandler travelLoaded, List<TravelTypeData> items, int value, double doubleValue, int currentValue) {
      if (travelPolicy.allowTower() && travelPolicy.canPlace()) {
         int nextValue = (int)Math.floor(doubleValue) - 1;
         if (travelLoaded.placeable(value, nextValue, currentValue)) {
            double currentDoubleValue = doubleValue + 1.0;
            if (travelLoaded.headroom(value, currentDoubleValue, currentValue)) {
               items.add(place(value, currentDoubleValue, currentValue, travelPolicy.jumpPenalty() + travelPolicy.placePenalty(), TravelTypeData.Type.PILLAR, value, nextValue, currentValue));
            }
         }
      }
   }

   private static void updateState5(TravelLoadedHandler travelLoaded, List<TravelTypeData> items, int value, double doubleValue, int currentValue) {
      if (travelLoaded.water(value, doubleValue, currentValue)) {
         Double currentDoubleValue = travelLoaded.floor(value, currentValue, doubleValue + 1.0);
         if (currentDoubleValue != null && currentDoubleValue > doubleValue && travelLoaded.headroom(value, currentDoubleValue, currentValue)) {
            items.add(walk(value, currentDoubleValue, currentValue, 13.636499999999998, TravelTypeData.Type.SWIM));
         }
      }

      if (travelLoaded.climbable(value, doubleValue, currentValue)) {
         Double nextDoubleValue = travelLoaded.floor(value, currentValue, doubleValue + 1.0);
         if (nextDoubleValue != null && travelLoaded.headroom(value, nextDoubleValue, currentValue)) {
            items.add(walk(value, nextDoubleValue, currentValue, 5.0, TravelTypeData.Type.CLIMB));
         }
      }
   }

   public static boolean lineOfSight(TravelLoadedHandler travelLoaded, int value, double doubleValue, int currentValue, int nextValue, double currentDoubleValue, int previousValue) {
      double nextDoubleValue = nextValue - value;
      double previousDoubleValue = previousValue - currentValue;
      double sourceDoubleValue = currentDoubleValue - doubleValue;
      int sourceValue = Math.max(Math.abs(nextValue - value), Math.abs(previousValue - currentValue)) * 2 + 1;
      int index = 1;

      while (index < sourceValue) {
         double targetDoubleValue = (double)index / sourceValue;
         int targetValue = (int)Math.floor(value + 0.5 + nextDoubleValue * targetDoubleValue);
         int inputValue = (int)Math.floor(currentValue + 0.5 + previousDoubleValue * targetDoubleValue);
         double inputDoubleValue = doubleValue + sourceDoubleValue * targetDoubleValue;
         if (!travelLoaded.loaded(targetValue, inputValue)) {
            return false;
         }

         Double outputDoubleValue = travelLoaded.floor(targetValue, inputValue, inputDoubleValue);
         if (outputDoubleValue != null && !(Math.abs(outputDoubleValue - inputDoubleValue) > 1.01)) {
            if (!travelLoaded.headroom(targetValue, outputDoubleValue, inputValue)) {
               return false;
            }

            TravelLoadedHandler.Hazard currentHazard = travelLoaded.hazard(targetValue, outputDoubleValue, inputValue);
            if (currentHazard != TravelLoadedHandler.Hazard.DANGER && currentHazard != TravelLoadedHandler.Hazard.VOID) {
               index++;
               continue;
            }

            return false;
         }

         return false;
      }

      return true;
   }

   public enum Type {
      TRAVERSE,
      ASCEND,
      DESCEND,
      DIAGONAL,
      DIAGONAL_ASCEND,
      PARKOUR,
      PARKOUR_ASCEND,
      PILLAR,
      BRIDGE,
      FALL,
      SWIM,
      CLIMB,
      DOOR;


      private static TravelTypeData.Type[] $values() {
         return new TravelTypeData.Type[]{
            TRAVERSE, ASCEND, DESCEND, DIAGONAL, DIAGONAL_ASCEND, PARKOUR, PARKOUR_ASCEND, PILLAR, BRIDGE, FALL, SWIM, CLIMB, DOOR
         };
      }
   }
}
