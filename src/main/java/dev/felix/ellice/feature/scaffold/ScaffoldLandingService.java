package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ScaffoldLandingService {
   private static final ScaffoldMode[] scaffoldMode = new ScaffoldMode[]{
      ScaffoldMode.NORTH, ScaffoldMode.SOUTH, ScaffoldMode.EAST, ScaffoldMode.WEST, ScaffoldMode.DOWN
   };

   private ScaffoldLandingService() {
   }

   public static Optional<ScaffoldLandingService.Landing> landing(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldRemapService.WorldVector worldVector, double doubleValue, double currentDoubleValue, int value
   ) {
      if (!scaffoldPlayerSnapshot.onGround() && Double.isFinite(doubleValue) && !(doubleValue < 0.0) && Double.isFinite(currentDoubleValue) && !(currentDoubleValue <= 0.0)) {
         RotationVector rotationVector = scaffoldPlayerSnapshot.feetPosition();
         RotationVector currentRotationVector = scaffoldPlayerSnapshot.velocity();
         if (rotationVector.y() < value + 1 - 0.02) {
            return Optional.empty();
         }

         for (int index = 1; index <= 40; index++) {
            RotationVector currentX = rotationVector.add(new RotationVector(currentRotationVector.x() + worldVector.x() * doubleValue, currentRotationVector.y(), currentRotationVector.z() + worldVector.z() * doubleValue));
            if (currentRotationVector.y() < 0.0 && currentX.y() <= value + 1) {
               return Optional.of(new ScaffoldLandingService.Landing(rotationVector, new RotationVector(currentX.x(), value + 1, currentX.z()), index));
            }

            currentRotationVector = new RotationVector(
               (currentRotationVector.x() + worldVector.x() * doubleValue) * 0.91,
               (currentRotationVector.y() - currentDoubleValue) * 0.98,
               (currentRotationVector.z() + worldVector.z() * doubleValue) * 0.91
            );
            rotationVector = currentX;
         }

         return Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public static List<BlockCoordinates> cells(RotationVector rotationVector, RotationVector currentRotationVector, int value) {
      if (Math.hypot(currentRotationVector.x() - rotationVector.x(), currentRotationVector.z() - rotationVector.z()) > 10.0) {
         return List.of();
      }

      int currentX = (int)Math.floor(rotationVector.x());
      int currentValue = (int)Math.floor(rotationVector.z());
      int nextX = (int)Math.floor(currentRotationVector.x());
      int nextValue = (int)Math.floor(currentRotationVector.z());
      ArrayList arrayList = new ArrayList();
      arrayList.add(new BlockCoordinates(currentX, value, currentValue));
      double previousX = currentRotationVector.x() - rotationVector.x();
      double doubleValue = currentRotationVector.z() - rotationVector.z();
      int previousValue = Double.compare(previousX, 0.0);
      int sourceValue = Double.compare(doubleValue, 0.0);
      double currentDoubleValue = previousValue == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(previousX);
      double nextDoubleValue = sourceValue == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(doubleValue);
      double sourceX = previousValue == 0 ? Double.POSITIVE_INFINITY : ((previousValue > 0 ? currentX + 1 : currentX) - rotationVector.x()) / previousX;
      double previousDoubleValue = sourceValue == 0 ? Double.POSITIVE_INFINITY : ((sourceValue > 0 ? currentValue + 1 : currentValue) - rotationVector.z()) / doubleValue;

      while ((currentX != nextX || currentValue != nextValue) && arrayList.size() < 32) {
         if (currentX == nextX || currentValue != nextValue && !(sourceX <= previousDoubleValue)) {
            currentValue += sourceValue;
            previousDoubleValue += nextDoubleValue;
         } else {
            currentX += previousValue;
            sourceX += currentDoubleValue;
         }

         arrayList.add(new BlockCoordinates(currentX, value, currentValue));
      }

      return List.copyOf(arrayList);
   }

   public static Optional<PlacementCandidate> plan(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, RotationVector rotationVector, RotationVector currentRotationVector, int value, double doubleValue, RotationData rotationData, ScaffoldPlanService.Query query
   ) {
      if (value + 1 > scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6) {
         return Optional.empty();
      }

      for (BlockCoordinates blockCoordinates : cells(rotationVector, currentRotationVector, value)) {
         if (query.isReplaceable(blockCoordinates)) {
            PlacementCandidate placementCandidate = null;
            double currentDoubleValue = Double.POSITIVE_INFINITY;

            for (ScaffoldMode currentScaffoldMode : scaffoldMode) {
               BlockCoordinates currentBlockCoordinates = blockCoordinates.offset(currentScaffoldMode);
               ScaffoldMode nextScaffoldMode = currentScaffoldMode.opposite();
               if (!query.isReplaceable(currentBlockCoordinates) && query.isFaceSturdy(currentBlockCoordinates, nextScaffoldMode)) {
                  RotationVector nextRotationVector = currentBlockCoordinates.facePoint(
                     nextScaffoldMode, 0.5, 0.5
                  );
                  if (!(nextRotationVector.subtract(scaffoldPlayerSnapshot.eyePosition()).length() > doubleValue)) {
                     double currentX = scaffoldPlayerSnapshot.eyePosition().subtract(nextRotationVector).dot(new RotationVector(nextScaffoldMode.x(), nextScaffoldMode.y(), nextScaffoldMode.z()));
                     if (!(currentX <= 1.0E-4)) {
                        double nextDoubleValue = RotationData.distance(rotationData, RotationData.lookAt(scaffoldPlayerSnapshot.eyePosition(), nextRotationVector));
                        if (nextDoubleValue < currentDoubleValue) {
                           currentDoubleValue = nextDoubleValue;
                           placementCandidate = new PlacementCandidate(blockCoordinates, currentBlockCoordinates, nextScaffoldMode, nextRotationVector);
                        }
                     }
                  }
               }
            }

            if (placementCandidate != null) {
               return Optional.of(placementCandidate);
            }

            return Optional.empty();
         }
      }

      return Optional.empty();
   }

   public record Landing(RotationVector before, RotationVector feet, int ticks) {
   }
}
