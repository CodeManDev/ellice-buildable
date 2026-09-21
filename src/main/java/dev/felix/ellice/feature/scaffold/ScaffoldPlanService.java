package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class ScaffoldPlanService {
   public static final double MAXIMUM_LOOK_AHEAD_DISTANCE = 3.0;
   private static final ScaffoldMode[] scaffoldMode = new ScaffoldMode[]{
      ScaffoldMode.DOWN, ScaffoldMode.NORTH, ScaffoldMode.SOUTH, ScaffoldMode.WEST, ScaffoldMode.EAST
   };

   public Optional<PlacementCandidate> plan(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldPlanService.Query query, double doubleValue, double currentDoubleValue) {
      return this.plan(scaffoldPlayerSnapshot, query, doubleValue, currentDoubleValue, scaffoldPlayerSnapshot.playerRotation());
   }

   public Optional<PlacementCandidate> plan(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldPlanService.Query query, double doubleValue, double currentDoubleValue, RotationData rotationData) {
      int currentY = calculateValue5(scaffoldPlayerSnapshot.feetPosition().y() - 1.0);
      return this.plan(scaffoldPlayerSnapshot, query, doubleValue, currentDoubleValue, rotationData, currentY, item -> true);
   }

   public Optional<PlacementCandidate> plan(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldPlanService.Query query, double doubleValue, double currentDoubleValue, RotationData rotationData, int value) {
      return this.plan(scaffoldPlayerSnapshot, query, doubleValue, currentDoubleValue, rotationData, value, item -> true);
   }

   public Optional<PlacementCandidate> plan(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldPlanService.Query query, double doubleValue, double currentDoubleValue, RotationData rotationData, int value, Predicate<PlacementCandidate> predicate
   ) {
      double nextDoubleValue = scaffoldPlayerSnapshot.horizontalSpeed();
      ScaffoldRemapService.WorldVector currentX = nextDoubleValue < 0.01
         ? new ScaffoldRemapService.WorldVector(0.0, 0.0)
         : new ScaffoldRemapService.WorldVector(scaffoldPlayerSnapshot.velocity().x() / nextDoubleValue, scaffoldPlayerSnapshot.velocity().z() / nextDoubleValue);
      return this.plan(scaffoldPlayerSnapshot, query, doubleValue, currentDoubleValue, rotationData, value, currentX, predicate);
   }

   public Optional<PlacementCandidate> plan(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldPlanService.Query query,
      double doubleValue,
      double currentDoubleValue,
      RotationData rotationData,
      int value,
      ScaffoldRemapService.WorldVector worldVector,
      Predicate<PlacementCandidate> currentPredicate
   ) {
      if (currentPredicate == null) {
         throw new IllegalArgumentException("Allowed predicate is required");
      }

      if (worldVector == null) {
         throw new IllegalArgumentException("Committed direction is required");
      }

      if (!Double.isFinite(doubleValue) || doubleValue < 0.0 || doubleValue > 3.0) {
         throw new IllegalArgumentException("Look-ahead must be in [0, 3.0]");
      }

      if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue <= 0.0)) {
         int currentY = scaffoldPlayerSnapshot.velocity().y() < -0.02 ? 1 : 0;
         double currentLength = worldVector.length();
         if (currentLength < 1.0E-9 && currentY == 0) {
            return Optional.empty();
         }

         double currentX = currentLength < 1.0E-9 ? 0.0 : worldVector.x() / currentLength;
         double nextDoubleValue = currentLength < 1.0E-9 ? 0.0 : worldVector.z() / currentLength;
         PlacementCandidate placementCandidate = null;
         double previousDoubleValue = Double.POSITIVE_INFINITY;
         int currentValue = Integer.MAX_VALUE;
         if (value + 1.0 > scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6) {
            return Optional.empty();
         }

         for (Entry entry : createMap(scaffoldPlayerSnapshot, currentX, nextDoubleValue, doubleValue, value).entrySet()) {
            if ((Integer)entry.getValue() > currentValue) {
               break;
            }

            BlockCoordinates blockCoordinates = (BlockCoordinates)entry.getKey();
            if (query.isReplaceable(blockCoordinates)) {
               for (ScaffoldMode currentScaffoldMode : scaffoldMode) {
                  BlockCoordinates currentBlockCoordinates = blockCoordinates.offset(currentScaffoldMode);
                  ScaffoldMode nextScaffoldMode = currentScaffoldMode.opposite();
                  if (!query.isReplaceable(currentBlockCoordinates) && query.isFaceSturdy(currentBlockCoordinates, nextScaffoldMode)) {
                     RotationVector rotationVector = currentBlockCoordinates.facePoint(
                        nextScaffoldMode, 0.5, 0.5
                     );
                     double nextLength = rotationVector.subtract(scaffoldPlayerSnapshot.eyePosition()).length();
                     if (!(nextLength > currentDoubleValue)) {
                        PlacementCandidate currentPlacementCandidate = new PlacementCandidate(blockCoordinates, currentBlockCoordinates, nextScaffoldMode, rotationVector);
                        if (currentPredicate.test(currentPlacementCandidate)
                           && canReachFace(scaffoldPlayerSnapshot, currentPlacementCandidate, new ScaffoldRemapService.WorldVector(currentX, nextDoubleValue), query.groundFriction())) {
                           RotationData currentRotationData = RotationData.lookAt(ScaffoldMotionPhaseData.predictedEyeAtClick(scaffoldPlayerSnapshot, currentPlacementCandidate, worldVector), rotationVector);
                           double sourceDoubleValue = RotationData.distance(rotationData, currentRotationData) + nextLength * 0.05;
                           if ((Integer)entry.getValue() < currentValue || sourceDoubleValue < previousDoubleValue) {
                              currentValue = (Integer)entry.getValue();
                              previousDoubleValue = sourceDoubleValue;
                              placementCandidate = currentPlacementCandidate;
                           }
                        }
                     }
                  }
               }
            }
         }

         return Optional.ofNullable(placementCandidate);
      } else {
         throw new IllegalArgumentException("Reach must be positive");
      }
   }

   public static boolean canReachFace(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate, ScaffoldRemapService.WorldVector worldVector, double doubleValue) {
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN) {
         RotationVector currentX = new RotationVector(scaffoldMode.x(), scaffoldMode.y(), scaffoldMode.z());
         double currentDoubleValue = scaffoldPlayerSnapshot.eyePosition().subtract(placementCandidate.hitPoint()).dot(currentX);
         double nextX = worldVector.x() * currentX.x() + worldVector.z() * currentX.z();
         if (!(currentDoubleValue > 1.0E-4) && !(nextX > 1.0E-4)) {
            double nextDoubleValue = Double.isFinite(doubleValue) && doubleValue >= 0.0 && doubleValue <= 1.0 ? doubleValue : 1.0;
            double previousDoubleValue = Math.max(0.0, scaffoldPlayerSnapshot.velocity().dot(currentX)) * ScaffoldCoastTicksService.coastTicks(scaffoldPlayerSnapshot.onGround() ? nextDoubleValue : 1.0);
            return currentDoubleValue + previousDoubleValue > 1.0E-4;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private static LinkedHashMap<BlockCoordinates, Integer> createMap(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, double doubleValue, double currentDoubleValue, double nextDoubleValue, int value) {
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      double previousDoubleValue = -currentDoubleValue;
      double sourceDoubleValue = doubleValue;
      double targetDoubleValue = 0.3 + nextDoubleValue;
      double currentX = doubleValue * targetDoubleValue + scaffoldPlayerSnapshot.velocity().x() * 1.5;
      double inputDoubleValue = currentDoubleValue * targetDoubleValue + scaffoldPlayerSnapshot.velocity().z() * 1.5;
      int currentValue = 0;

      for (double outputDoubleValue : new double[]{0.0, -0.24, 0.24}) {
         double nextX = scaffoldPlayerSnapshot.feetPosition().x() + previousDoubleValue * outputDoubleValue;
         double resultDoubleValue = scaffoldPlayerSnapshot.feetPosition().z() + sourceDoubleValue * outputDoubleValue;
         currentValue = calculateValue3(nextX, resultDoubleValue, nextX + currentX, resultDoubleValue + inputDoubleValue, value, linkedHashMap, currentValue);
      }

      if (scaffoldPlayerSnapshot.velocity().y() < -0.02) {
         int previousX = calculateValue5(scaffoldPlayerSnapshot.feetPosition().x() - 0.3);
         int sourceX = calculateValue5(scaffoldPlayerSnapshot.feetPosition().x() + 0.3);
         int nextValue = calculateValue5(scaffoldPlayerSnapshot.feetPosition().z() - 0.3);
         int previousValue = calculateValue5(scaffoldPlayerSnapshot.feetPosition().z() + 0.3);

         for (int index = previousX; index <= sourceX; index++) {
            for (int currentIndex = nextValue; currentIndex <= previousValue; currentIndex++) {
               currentValue = calculateValue4(linkedHashMap, new BlockCoordinates(index, value, currentIndex), currentValue);
            }
         }
      }

      ArrayList arrayList = new ArrayList(linkedHashMap.keySet());
      arrayList.sort(
         Comparator.<BlockCoordinates>comparingDouble(item -> calculateValue(scaffoldPlayerSnapshot, item, doubleValue, currentDoubleValue))
            .thenComparingDouble(item -> calculateValue2(scaffoldPlayerSnapshot, item, doubleValue, currentDoubleValue))
      );
      LinkedHashMap currentLinkedHashMap = new LinkedHashMap();

      for (int nextIndex = 0; nextIndex < arrayList.size(); nextIndex++) {
         currentLinkedHashMap.put((BlockCoordinates)arrayList.get(nextIndex), nextIndex);
      }

      return currentLinkedHashMap;
   }

   private static double calculateValue(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, BlockCoordinates blockCoordinates, double doubleValue, double currentDoubleValue) {
      double currentX = blockCoordinates.x() + 0.5 - scaffoldPlayerSnapshot.feetPosition().x();
      double nextDoubleValue = blockCoordinates.z() + 0.5 - scaffoldPlayerSnapshot.feetPosition().z();
      double previousDoubleValue = currentX * doubleValue + nextDoubleValue * currentDoubleValue;
      double sourceDoubleValue = 0.5 * (Math.abs(doubleValue) + Math.abs(currentDoubleValue));
      return Math.max(0.0, previousDoubleValue - sourceDoubleValue);
   }

   private static double calculateValue2(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, BlockCoordinates blockCoordinates, double doubleValue, double currentDoubleValue) {
      double currentX = blockCoordinates.x() + 0.5 - scaffoldPlayerSnapshot.feetPosition().x();
      double nextDoubleValue = blockCoordinates.z() + 0.5 - scaffoldPlayerSnapshot.feetPosition().z();
      return Math.abs(currentX * -currentDoubleValue + nextDoubleValue * doubleValue);
   }

   private static int calculateValue3(
      double doubleValue, double currentDoubleValue, double nextDoubleValue, double previousDoubleValue, int value, LinkedHashMap<BlockCoordinates, Integer> linkedHashMap, int currentValue
   ) {
      int nextValue = calculateValue5(doubleValue);
      int previousValue = calculateValue5(currentDoubleValue);
      int sourceValue = calculateValue5(nextDoubleValue);
      int targetValue = calculateValue5(previousDoubleValue);
      currentValue = calculateValue4(linkedHashMap, new BlockCoordinates(nextValue, value, previousValue), currentValue);
      double sourceDoubleValue = nextDoubleValue - doubleValue;
      double targetDoubleValue = previousDoubleValue - currentDoubleValue;
      int inputValue = sourceDoubleValue > 0.0 ? 1 : (sourceDoubleValue < 0.0 ? -1 : 0);
      int outputValue = targetDoubleValue > 0.0 ? 1 : (targetDoubleValue < 0.0 ? -1 : 0);
      double inputDoubleValue = inputValue == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(sourceDoubleValue);
      double outputDoubleValue = outputValue == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(targetDoubleValue);
      double resultDoubleValue = inputValue > 0 ? nextValue + 1.0 : nextValue;
      double candidateDoubleValue = outputValue > 0 ? previousValue + 1.0 : previousValue;
      double selectedDoubleValue = inputValue == 0 ? Double.POSITIVE_INFINITY : Math.abs((resultDoubleValue - doubleValue) / sourceDoubleValue);
      double defaultDoubleValue = outputValue == 0 ? Double.POSITIVE_INFINITY : Math.abs((candidateDoubleValue - currentDoubleValue) / targetDoubleValue);

      for (int index = 0;
         (nextValue != sourceValue || previousValue != targetValue) && index++ < 16;
         currentValue = calculateValue4(linkedHashMap, new BlockCoordinates(nextValue, value, previousValue), currentValue)
      ) {
         int resultValue = !(selectedDoubleValue < defaultDoubleValue)
               && (!(Math.abs(selectedDoubleValue - defaultDoubleValue) < 1.0E-9) || !(Math.abs(sourceDoubleValue) >= Math.abs(targetDoubleValue)))
            ? 0
            : 1;
         if (resultValue != 0) {
            nextValue += inputValue;
            selectedDoubleValue += inputDoubleValue;
         } else {
            previousValue += outputValue;
            defaultDoubleValue += outputDoubleValue;
         }
      }

      return currentValue;
   }

   private static int calculateValue4(LinkedHashMap<BlockCoordinates, Integer> linkedHashMap, BlockCoordinates blockCoordinates, int index) {
      if (!linkedHashMap.containsKey(blockCoordinates)) {
         linkedHashMap.put(blockCoordinates, index++);
      }

      return index;
   }

   private static int calculateValue5(double doubleValue) {
      return (int)Math.floor(doubleValue);
   }

   public interface Query {
      boolean isReplaceable(BlockCoordinates blockCoordinates);

      boolean isFaceSturdy(BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode);

      default double groundFriction() {
         return 1.0;
      }
   }
}
