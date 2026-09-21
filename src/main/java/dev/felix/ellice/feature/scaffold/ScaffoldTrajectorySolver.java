package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ScaffoldTrajectorySolver {
   private final ScaffoldTrajectorySolver.SelectionPolicy selectionPolicy;
   public static final int MAX_PROBE_EVALUATIONS = 512;
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();

   public ScaffoldTrajectorySolver() {
      this(ScaffoldTrajectorySolver.SelectionPolicy.STABLE_MARGIN);
   }

   public ScaffoldTrajectorySolver(ScaffoldTrajectorySolver.SelectionPolicy currentSelectionPolicy) {
      this.selectionPolicy = Objects.requireNonNull(currentSelectionPolicy, "policy");
   }

   public Optional<ScaffoldTrajectorySolver.Solution> solveKeepingYaw(
      RotationData rotationData, RotationVector rotationVector, PlacementCandidate placementCandidate, double doubleValue, double currentDoubleValue, int value, ScaffoldTrajectorySolver.Probe probe
   ) {
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN && value >= 1) {
         RotationVector currentRotationVector = new RotationData(rotationData.yaw(), 0.0).direction();
         double currentX = currentRotationVector.x() * scaffoldMode.x() + currentRotationVector.z() * scaffoldMode.z();
         double nextX = (rotationVector.x() - placementCandidate.hitPoint().x()) * scaffoldMode.x() + (rotationVector.z() - placementCandidate.hitPoint().z()) * scaffoldMode.z();
         if (!(nextX <= 1.0E-6) && !(currentX >= -1.0E-6)) {
            double nextDoubleValue = -nextX / currentX;
            RotationVector nextRotationVector = rotationVector.add(currentRotationVector.multiply(nextDoubleValue));
            double previousX = scaffoldMode != ScaffoldMode.EAST && scaffoldMode != ScaffoldMode.WEST
               ? nextRotationVector.x() - placementCandidate.supportPosition().x()
               : nextRotationVector.z() - placementCandidate.supportPosition().z();
            if (!(previousX < currentDoubleValue) && !(previousX > 1.0 - currentDoubleValue)) {
               double previousDoubleValue = this.rotationVanillaGcdService.vanillaGcd(doubleValue);
               double currentY = Math.toDegrees(Math.atan2(rotationVector.y() - (placementCandidate.supportPosition().y() + 1 - currentDoubleValue), nextDoubleValue));
               double nextY = Math.toDegrees(Math.atan2(rotationVector.y() - (placementCandidate.supportPosition().y() + currentDoubleValue), nextDoubleValue));
               long longValue = (long)Math.floor((currentY - rotationData.pitch()) / previousDoubleValue) - 1L;
               long currentLongValue = (long)Math.ceil((nextY - rotationData.pitch()) / previousDoubleValue) + 1L;
               long nextLongValue = Math.max(longValue, Math.min(currentLongValue, 0L));
               int index = 0;

               for (long currentIndex = 0L; currentIndex <= currentLongValue - longValue && index < value; currentIndex++) {
                  for (int currentValue : currentIndex == 0L ? new int[]{1} : new int[]{-1, 1}) {
                     long previousLongValue = nextLongValue + currentIndex * currentValue;
                     if (previousLongValue >= longValue && previousLongValue <= currentLongValue) {
                        RotationData currentRotationData = ScaffoldNextService.applyMouseCounts(rotationData, 0L, previousLongValue, doubleValue);
                        double previousY = rotationVector.y() - Math.tan(Math.toRadians(currentRotationData.pitch())) * nextDoubleValue - placementCandidate.supportPosition().y();
                        if (!(previousY < currentDoubleValue) && !(previousY > 1.0 - currentDoubleValue)) {
                           PlacementCandidate currentPlacementCandidate = placementCandidate.withSurfacePoint(previousX, previousY);
                           index++;
                           Optional result = probe.raycast(currentPlacementCandidate, currentRotationData)
                              .filter(item -> checkCondition(placementCandidate, item) && !item.inside() && item.faceEdgeMargin() >= currentDoubleValue);
                           if (result.isPresent()) {
                              return Optional.of(
                                 new ScaffoldTrajectorySolver.Solution(
                                    currentRotationData, currentPlacementCandidate, (PlacementCandidate)result.get(), 0L, previousLongValue, -Math.abs(currentRotationData.pitch() - rotationData.pitch())
                                 )
                              );
                           }

                           if (index >= value) {
                              break;
                           }
                        }
                     }
                  }
               }

               return Optional.empty();
            } else {
               return Optional.empty();
            }
         } else {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   public Optional<ScaffoldTrajectorySolver.Solution> solveNearbyYaw(
      RotationData rotationData, RotationVector rotationVector, PlacementCandidate placementCandidate, double doubleValue, double currentDoubleValue, int value, int currentValue, ScaffoldTrajectorySolver.Probe probe
   ) {
      double nextDoubleValue = this.rotationVanillaGcdService.vanillaGcd(doubleValue);
      int nextValue = Math.min(value, (int)Math.floor(2.0 / nextDoubleValue));
      int[] ints = new int[]{0};
      ScaffoldTrajectorySolver.Probe currentProbe = (item, currentItem) -> {
         if (ints[0] >= currentValue) {
            return Optional.empty();
         }

         ints[0]++;
         return probe.raycast(item, currentItem);
      };

      for (int index = 1; index <= nextValue && ints[0] < currentValue; index++) {
         for (int previousValue : new int[]{-1, 1}) {
            int sourceValue = index * previousValue;
            RotationData currentRotationData = ScaffoldNextService.applyMouseCounts(rotationData, sourceValue, 0L, doubleValue);
            Optional result = this.solveKeepingYaw(currentRotationData, rotationVector, placementCandidate, doubleValue, currentDoubleValue, Math.min(32, currentValue - ints[0]), currentProbe);
            if (result.isPresent()) {
               ScaffoldTrajectorySolver.Solution solution = (ScaffoldTrajectorySolver.Solution)result.get();
               return Optional.of(
                  new ScaffoldTrajectorySolver.Solution(
                     solution.rotation(),
                     solution.anchor(),
                     solution.exactRay(),
                     sourceValue,
                     solution.pitchMouseCounts(),
                     -RotationData.distance(rotationData, solution.rotation())
                  )
               );
            }
         }
      }

      return Optional.empty();
   }

   public Optional<ScaffoldTrajectorySolver.Solution> solve(
      RotationData rotationData, RotationVector rotationVector, PlacementCandidate placementCandidate, double doubleValue, double currentDoubleValue, int value, int currentValue, ScaffoldTrajectorySolver.Probe probe
   ) {
      return this.solve(rotationData, rotationVector, placementCandidate, doubleValue, currentDoubleValue, value, currentValue, 512, probe);
   }

   public Optional<ScaffoldTrajectorySolver.Solution> solve(
      RotationData rotationData,
      RotationVector rotationVector,
      PlacementCandidate placementCandidate,
      double doubleValue,
      double currentDoubleValue,
      int value,
      int currentValue,
      int nextValue,
      ScaffoldTrajectorySolver.Probe currentProbe
   ) {
      Objects.requireNonNull(rotationData, "current");
      Objects.requireNonNull(rotationVector, "eye");
      Objects.requireNonNull(placementCandidate, "placement");
      Objects.requireNonNull(currentProbe, "probe");
      if (value >= 1
         && value <= 9
         && value % 2 != 0
         && currentValue >= 0
         && currentValue <= 8
         && nextValue >= 1
         && nextValue <= 512
         && Double.isFinite(currentDoubleValue)
         && !(currentDoubleValue < 0.0)
         && !(currentDoubleValue > 0.45)) {
         double nextDoubleValue = Math.max(
            0.08,
            Math.min(0.32, currentDoubleValue + 0.035)
         );
         ScaffoldTrajectorySolver.SurfacePoint surfacePoint = createSurfacePoint(placementCandidate);
         LinkedHashSet linkedHashSet = collectValues(rotationVector, placementCandidate, surfacePoint, currentDoubleValue);
         LinkedHashSet currentLinkedHashSet = new LinkedHashSet();
         currentLinkedHashSet.add(surfacePoint);
         currentLinkedHashSet.addAll(linkedHashSet);
         currentLinkedHashSet.add(
            new ScaffoldTrajectorySolver.SurfacePoint(0.5, 0.5)
         );
         if (value > 1) {
            for (int index = 0; index < value; index++) {
               for (int currentIndex = 0; currentIndex < value; currentIndex++) {
                  currentLinkedHashSet.add(
                     new ScaffoldTrajectorySolver.SurfacePoint(
                        calculateValue5(nextDoubleValue, 1.0 - nextDoubleValue, index, value), calculateValue5(nextDoubleValue, 1.0 - nextDoubleValue, currentIndex, value)
                     )
                  );
               }
            }
         }

         ArrayList arrayList = new ArrayList(currentLinkedHashSet);
         arrayList.sort(
            Comparator.<ScaffoldTrajectorySolver.SurfacePoint>comparingInt(item -> item.equals(surfacePoint) ? 0 : (linkedHashSet.contains(item) ? 1 : 2))
               .thenComparingDouble(item -> calculateValue2(item, surfacePoint))
               .thenComparingDouble(ScaffoldTrajectorySolver::calculateValue)
               .thenComparingDouble(ScaffoldTrajectorySolver.SurfacePoint::first)
               .thenComparingDouble(ScaffoldTrajectorySolver.SurfacePoint::second)
         );
         double previousDoubleValue = this.rotationVanillaGcdService.vanillaGcd(doubleValue);
         ArrayList currentSize = new ArrayList(arrayList.size());

         for (ScaffoldTrajectorySolver.SurfacePoint currentSurfacePoint : (Iterable<ScaffoldTrajectorySolver.SurfacePoint>) (Iterable<?>) (arrayList)) {
            PlacementCandidate currentPlacementCandidate = placementCandidate.withSurfacePoint(currentSurfacePoint.first(), currentSurfacePoint.second());

            RotationData currentRotationData;
            try {
               currentRotationData = RotationData.lookAt(rotationVector, currentPlacementCandidate.hitPoint());
            } catch (IllegalArgumentException illegalArgumentException) {
               continue;
            }

            currentSize.add(
               new ScaffoldTrajectorySolver.AimPoint(
                  currentSurfacePoint,
                  currentPlacementCandidate,
                  Math.round(RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw()) / previousDoubleValue),
                  Math.round((currentRotationData.pitch() - rotationData.pitch()) / previousDoubleValue)
               )
            );
         }

         ScaffoldTrajectorySolver.CachedProbe cachedProbe = new ScaffoldTrajectorySolver.CachedProbe(currentProbe, nextValue);

         for (int nextIndex = 0; nextIndex <= currentValue; nextIndex++) {
            ScaffoldTrajectorySolver.Solution solution = null;

            for (ScaffoldTrajectorySolver.CountOffset countOffset : collectValues2(nextIndex)) {
               for (ScaffoldTrajectorySolver.AimPoint aimPoint : (Iterable<ScaffoldTrajectorySolver.AimPoint>) (Iterable<?>) (currentSize)) {
                  long longValue = aimPoint.yawCenter() + countOffset.yaw();
                  long currentLongValue = aimPoint.pitchCenter() + countOffset.pitch();
                  RotationData nextRotationData = ScaffoldNextService.applyMouseCounts(rotationData, longValue, currentLongValue, doubleValue);
                  Optional result = cachedProbe.findResult(aimPoint.anchor(), nextRotationData)
                     .filter(item -> checkCondition(aimPoint.anchor(), item))
                     .filter(item -> !item.inside())
                     .filter(item -> item.faceEdgeMargin() >= currentDoubleValue);
                  if (result.isPresent()) {
                     PlacementCandidate nextPlacementCandidate = (PlacementCandidate)result.get();
                     double sourceDoubleValue = Math.sqrt(calculateValue2(aimPoint.point(), surfacePoint));
                     double targetDoubleValue = Math.min(nextPlacementCandidate.faceEdgeMargin(), currentDoubleValue + 0.16);
                     double inputDoubleValue = aimPoint.point().equals(surfacePoint) ? 0.85 : 0.0;
                     double outputDoubleValue = targetDoubleValue * 10.0
                        - RotationData.distance(rotationData, nextRotationData) * 0.025
                        - sourceDoubleValue * 2.5
                        - Math.sqrt(calculateValue(aimPoint.point())) * 0.08
                        + inputDoubleValue;
                     if (this.selectionPolicy == ScaffoldTrajectorySolver.SelectionPolicy.MINIMUM_ROTATION) {
                        outputDoubleValue = -RotationData.distance(rotationData, nextRotationData)
                           + targetDoubleValue * 1.0E-6
                           - sourceDoubleValue * 1.0E-8;
                     }

                     ScaffoldTrajectorySolver.Solution currentSolution = new ScaffoldTrajectorySolver.Solution(nextRotationData, aimPoint.anchor(), nextPlacementCandidate, longValue, currentLongValue, outputDoubleValue);
                     if (solution == null || currentSolution.score() > solution.score()) {
                        solution = currentSolution;
                     }
                  }

                  if (cachedProbe.checkCondition2()) {
                     return Optional.ofNullable(solution);
                  }
               }
            }

            if (solution != null) {
               return Optional.of(solution);
            }
         }

         return Optional.empty();
      } else {
         throw new IllegalArgumentException("Invalid face-target search");
      }
   }

   private static boolean checkCondition(PlacementCandidate placementCandidate, PlacementCandidate currentPlacementCandidate) {
      return placementCandidate.targetPosition().equals(currentPlacementCandidate.targetPosition())
         && placementCandidate.supportPosition().equals(currentPlacementCandidate.supportPosition())
         && placementCandidate.clickedFace() == currentPlacementCandidate.clickedFace();
   }

   private static double calculateValue(ScaffoldTrajectorySolver.SurfacePoint surfacePoint) {
      double doubleValue = surfacePoint.first() - 0.5;
      double currentDoubleValue = surfacePoint.second() - 0.5;
      return doubleValue * doubleValue + currentDoubleValue * currentDoubleValue;
   }

   private static double calculateValue2(ScaffoldTrajectorySolver.SurfacePoint surfacePoint, ScaffoldTrajectorySolver.SurfacePoint currentSurfacePoint) {
      double doubleValue = surfacePoint.first() - currentSurfacePoint.first();
      double currentDoubleValue = surfacePoint.second() - currentSurfacePoint.second();
      return doubleValue * doubleValue + currentDoubleValue * currentDoubleValue;
   }

   private static LinkedHashSet<ScaffoldTrajectorySolver.SurfacePoint> collectValues(
      RotationVector rotationVector, PlacementCandidate placementCandidate, ScaffoldTrajectorySolver.SurfacePoint surfacePoint, double doubleValue
   ) {
      LinkedHashSet linkedHashSet = new LinkedHashSet();
      ScaffoldMode scaffoldMode = placementCandidate.clickedFace();
      if (scaffoldMode != ScaffoldMode.UP && scaffoldMode != ScaffoldMode.DOWN) {
         RotationVector currentRotationVector = placementCandidate.hitPoint();
         double currentX = (rotationVector.x() - currentRotationVector.x()) * scaffoldMode.x() + (rotationVector.z() - currentRotationVector.z()) * scaffoldMode.z();
         if (!(currentX <= 1.0E-6) && !(currentX >= 0.1)) {
            double currentDoubleValue = Math.min(0.49, doubleValue + 0.005);
            double nextX = scaffoldMode != ScaffoldMode.EAST && scaffoldMode != ScaffoldMode.WEST
               ? rotationVector.x() - placementCandidate.supportPosition().x()
               : rotationVector.z() - placementCandidate.supportPosition().z();
            nextX = calculateValue3(nextX, currentDoubleValue);
            double nextDoubleValue = calculateValue3(surfacePoint.first(), currentDoubleValue);
            linkedHashSet.add(new ScaffoldTrajectorySolver.SurfacePoint(nextX, surfacePoint.second()));
            linkedHashSet.add(new ScaffoldTrajectorySolver.SurfacePoint(nextX, 0.5));

            for (double previousDoubleValue : new double[]{nextDoubleValue, nextX}) {
               for (double sourceDoubleValue : new double[]{
                  0.25, 0.5, 1.0
               }) {
                  double targetDoubleValue = currentX * sourceDoubleValue;
                  linkedHashSet.add(new ScaffoldTrajectorySolver.SurfacePoint(calculateValue3(previousDoubleValue - targetDoubleValue, currentDoubleValue), surfacePoint.second()));
                  linkedHashSet.add(new ScaffoldTrajectorySolver.SurfacePoint(calculateValue3(previousDoubleValue + targetDoubleValue, currentDoubleValue), surfacePoint.second()));
               }
            }

            return linkedHashSet;
         } else {
            return linkedHashSet;
         }
      } else {
         return linkedHashSet;
      }
   }

   private static double calculateValue3(double doubleValue, double currentDoubleValue) {
      return Math.max(currentDoubleValue, Math.min(1.0 - currentDoubleValue, doubleValue));
   }

   private static ArrayList<ScaffoldTrajectorySolver.CountOffset> collectValues2(int value) {
      ArrayList arrayList = new ArrayList();
      if (value == 0) {
         arrayList.add(new ScaffoldTrajectorySolver.CountOffset(0, 0));
         return arrayList;
      }

      for (int index = -value; index <= value; index++) {
         for (int currentIndex = -value; currentIndex <= value; currentIndex++) {
            if (Math.max(Math.abs(index), Math.abs(currentIndex)) == value) {
               arrayList.add(new ScaffoldTrajectorySolver.CountOffset(index, currentIndex));
            }
         }
      }

      arrayList.sort(
         Comparator.comparingInt(ScaffoldTrajectorySolver::calculateValue4)
            .thenComparingInt(ScaffoldTrajectorySolver.CountOffset::yaw)
            .thenComparingInt(ScaffoldTrajectorySolver.CountOffset::pitch)
      );
      return arrayList;
   }

   private static int calculateValue4(ScaffoldTrajectorySolver.CountOffset countOffset) {
      return countOffset.yaw() * countOffset.yaw() + countOffset.pitch() * countOffset.pitch();
   }

   private static ScaffoldTrajectorySolver.SurfacePoint createSurfacePoint(PlacementCandidate placementCandidate) {
      double currentX = placementCandidate.hitPoint().x() - placementCandidate.supportPosition().x();
      double currentY = placementCandidate.hitPoint().y() - placementCandidate.supportPosition().y();
      double height = placementCandidate.hitPoint().z() - placementCandidate.supportPosition().z();

      return switch (placementCandidate.clickedFace()) {
         case UP, DOWN -> new ScaffoldTrajectorySolver.SurfacePoint(calculateValue6(currentX), calculateValue6(height));
         case NORTH, SOUTH -> new ScaffoldTrajectorySolver.SurfacePoint(calculateValue6(currentX), calculateValue6(currentY));
         case WEST, EAST -> new ScaffoldTrajectorySolver.SurfacePoint(calculateValue6(height), calculateValue6(currentY));
      };
   }

   private static double calculateValue5(double doubleValue, double currentDoubleValue, int value, int currentValue) {
      return currentValue <= 1 ? (doubleValue + currentDoubleValue) * 0.5 : doubleValue + (currentDoubleValue - doubleValue) * value / (currentValue - 1.0);
   }

   private static double calculateValue6(double doubleValue) {
      return Math.max(0.0, Math.min(1.0, doubleValue));
   }

   private record AimPoint(ScaffoldTrajectorySolver.SurfacePoint point, PlacementCandidate anchor, long yawCenter, long pitchCenter) {
   }

   private static final class CachedProbe {
      private final ScaffoldTrajectorySolver.Probe probe2;
      private final int count;
      private final Map<ScaffoldTrajectorySolver.RotationKey, Optional<PlacementCandidate>> entries = new HashMap<>();
      private int count2;

      private CachedProbe(ScaffoldTrajectorySolver.Probe probe, int value) {
         this.probe2 = probe;
         this.count = value;
      }

      private Optional<PlacementCandidate> findResult(PlacementCandidate placementCandidate, RotationData rotationData) {
         RotationData currentRotationData = new RotationData((float)rotationData.yaw(), (float)rotationData.pitch());
         ScaffoldTrajectorySolver.RotationKey rotationKey = ScaffoldTrajectorySolver.RotationKey.createRotationKey(currentRotationData);
         Optional result = this.entries.get(rotationKey);
         if (result != null || this.entries.containsKey(rotationKey)) {
            return result;
         }

         if (this.count2 >= this.count) {
            return Optional.empty();
         }

         this.count2++;
         Optional currentResult = Objects.requireNonNull(this.probe2.raycast(placementCandidate, currentRotationData), "Native ray probe returned null");
         this.entries.put(rotationKey, currentResult);
         return currentResult;
      }

      private boolean checkCondition2() {
         return this.count2 >= this.count;
      }
   }

   private record CountOffset(int yaw, int pitch) {
   }

   @FunctionalInterface
   public interface Probe {
      Optional<PlacementCandidate> raycast(PlacementCandidate placementCandidate, RotationData rotationData);
   }

   private record RotationKey(int yawBits, int pitchBits) {
      private static ScaffoldTrajectorySolver.RotationKey createRotationKey(RotationData rotationData) {
         return new ScaffoldTrajectorySolver.RotationKey(Float.floatToIntBits((float)rotationData.yaw()), Float.floatToIntBits((float)rotationData.pitch()));
      }
   }

   public enum SelectionPolicy {
      STABLE_MARGIN,
      MINIMUM_ROTATION;


      private static ScaffoldTrajectorySolver.SelectionPolicy[] $values() {
         return new ScaffoldTrajectorySolver.SelectionPolicy[]{STABLE_MARGIN, MINIMUM_ROTATION};
      }
   }

   public record Solution(
      RotationData rotation, PlacementCandidate anchor, PlacementCandidate exactRay, long yawMouseCounts, long pitchMouseCounts, double score
   ) {
   }

   private record SurfacePoint(double first, double second) {
   }
}
