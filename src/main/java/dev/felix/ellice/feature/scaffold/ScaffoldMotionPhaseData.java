package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public record ScaffoldMotionPhaseData(
   ScaffoldMotionPhaseData.PathPattern pathPattern,
   ScaffoldMotionPhaseData.ViewRelation viewRelation,
   ScaffoldMotionPhaseData.PathRelation pathRelation,
   double movementYaw,
   RotationVector predictedEyeAtClick,
   double deadlineTicks,
   double urgency,
   double firstAnchorBias,
   double secondAnchorBias,
   double turnSpeedScale,
   double smoothnessOffset,
   double maximumClickSpeed,
   ScaffoldMotionPhaseData.MotionPhase motionPhase
) {
   private static final double STATIONARY_SPEED = 0.0125;
   private static final double FULL_HEADING_SPEED = 0.075;

   public ScaffoldMotionPhaseData(
      ScaffoldMotionPhaseData.PathPattern pathPattern,
      ScaffoldMotionPhaseData.ViewRelation viewRelation,
      ScaffoldMotionPhaseData.PathRelation pathRelation,
      double movementYaw,
      RotationVector predictedEyeAtClick,
      double deadlineTicks,
      double urgency,
      double firstAnchorBias,
      double secondAnchorBias,
      double turnSpeedScale,
      double smoothnessOffset,
      double maximumClickSpeed,
      ScaffoldMotionPhaseData.MotionPhase motionPhase
   ) {
      Objects.requireNonNull(pathPattern, "pathPattern");
      Objects.requireNonNull(viewRelation, "viewRelation");
      Objects.requireNonNull(pathRelation, "pathRelation");
      Objects.requireNonNull(predictedEyeAtClick, "predictedEyeAtClick");
      Objects.requireNonNull(motionPhase, "motionPhase");
      if (Double.isFinite(movementYaw)
         && Double.isFinite(deadlineTicks)
         && !(deadlineTicks < 0.0)
         && Double.isFinite(urgency)
         && !(urgency < 0.0)
         && !(urgency > 1.0)
         && Double.isFinite(firstAnchorBias)
         && Double.isFinite(secondAnchorBias)
         && Double.isFinite(turnSpeedScale)
         && !(turnSpeedScale <= 0.0)
         && Double.isFinite(smoothnessOffset)
         && Double.isFinite(maximumClickSpeed)
         && !(maximumClickSpeed <= 0.0)) {
         this.pathPattern = pathPattern;
         this.viewRelation = viewRelation;
         this.pathRelation = pathRelation;
         this.movementYaw = movementYaw;
         this.predictedEyeAtClick = predictedEyeAtClick;
         this.deadlineTicks = deadlineTicks;
         this.urgency = urgency;
         this.firstAnchorBias = firstAnchorBias;
         this.secondAnchorBias = secondAnchorBias;
         this.turnSpeedScale = turnSpeedScale;
         this.smoothnessOffset = smoothnessOffset;
         this.maximumClickSpeed = maximumClickSpeed;
         this.motionPhase = motionPhase;
      } else {
         throw new IllegalArgumentException("Invalid scaffold situation");
      }
   }

   public ScaffoldMotionPhaseData(
      ScaffoldMotionPhaseData.PathPattern pathPattern,
      ScaffoldMotionPhaseData.ViewRelation viewRelation,
      ScaffoldMotionPhaseData.PathRelation pathRelation,
      double doubleValue,
      RotationVector rotationVector,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue,
      double sourceDoubleValue,
      double targetDoubleValue,
      double inputDoubleValue,
      double outputDoubleValue
   ) {
      this(pathPattern, viewRelation, pathRelation, doubleValue, rotationVector, currentDoubleValue, nextDoubleValue, previousDoubleValue, sourceDoubleValue, targetDoubleValue, inputDoubleValue, outputDoubleValue, ScaffoldMotionPhaseData.MotionPhase.GROUNDED);
   }

   public boolean shouldCoastSingleRayMiss() {
      return this.motionPhase == ScaffoldMotionPhaseData.MotionPhase.STATIONARY
         || this.motionPhase == ScaffoldMotionPhaseData.MotionPhase.GROUNDED
            && this.pathRelation.isPredictableChain()
            && this.urgency < 0.72;
   }

   public boolean suppressEndpointError() {
      return this.motionPhase == ScaffoldMotionPhaseData.MotionPhase.STATIONARY
         || this.motionPhase.isAirborne()
         || this.pathRelation == ScaffoldMotionPhaseData.PathRelation.RECOVERY;
   }

   public static ScaffoldMotionPhaseData analyze(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate, PlacementCandidate currentPlacementCandidate, double doubleValue, boolean enabled) {
      return analyze(
         scaffoldPlayerSnapshot, placementCandidate, currentPlacementCandidate, doubleValue, enabled, Double.NaN, Double.NaN
      );
   }

   public static ScaffoldMotionPhaseData analyze(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate, PlacementCandidate currentPlacementCandidate, double doubleValue, boolean enabled, double currentDoubleValue, double nextDoubleValue
   ) {
      Objects.requireNonNull(scaffoldPlayerSnapshot, "frame");
      Objects.requireNonNull(placementCandidate, "placement");
      boolean currentFinite = Double.isFinite(currentDoubleValue);
      if (!currentFinite && !Double.isNaN(currentDoubleValue)) {
         throw new IllegalArgumentException("Stable intent yaw must be finite or NaN");
      }

      boolean nextFinite = Double.isFinite(nextDoubleValue);
      if ((!nextFinite || !(nextDoubleValue < 0.0)) && (nextFinite || Double.isNaN(nextDoubleValue))) {
         double previousDoubleValue = scaffoldPlayerSnapshot.horizontalSpeed();
         ScaffoldMotionPhaseData.MotionPhase motionPhase = createMotionPhase(scaffoldPlayerSnapshot, previousDoubleValue);
         double currentX = previousDoubleValue >= 1.0E-6
            ? Math.toDegrees(Math.atan2(scaffoldPlayerSnapshot.velocity().z(), scaffoldPlayerSnapshot.velocity().x())) - 90.0
            : scaffoldPlayerSnapshot.playerRotation().yaw();
         double sourceDoubleValue;
         if (currentFinite) {
            sourceDoubleValue = RotationData.wrapDegrees(currentDoubleValue);
         } else if (previousDoubleValue < 0.0125) {
            sourceDoubleValue = Double.isFinite(doubleValue) ? doubleValue : scaffoldPlayerSnapshot.playerRotation().yaw();
         } else if (Double.isFinite(doubleValue) && previousDoubleValue < 0.075) {
            double targetDoubleValue = calculateValue4(
               (previousDoubleValue - 0.0125) / 0.0625,
               0.18,
               1.0
            );
            sourceDoubleValue = doubleValue + RotationData.yawDelta(doubleValue, currentX) * targetDoubleValue;
         } else {
            sourceDoubleValue = currentX;
         }

         double inputDoubleValue = Math.toRadians(sourceDoubleValue);
         double outputDoubleValue = -Math.sin(inputDoubleValue);
         double resultDoubleValue = Math.cos(inputDoubleValue);
         double candidateDoubleValue = Double.isFinite(doubleValue) ? Math.abs(RotationData.yawDelta(doubleValue, sourceDoubleValue)) : 0.0;
         double selectedDoubleValue = Math.max(Math.abs(outputDoubleValue), Math.abs(resultDoubleValue));
         double defaultDoubleValue = Math.min(Math.abs(outputDoubleValue), Math.abs(resultDoubleValue));
         ScaffoldMotionPhaseData.PathPattern pathPattern = motionPhase == ScaffoldMotionPhaseData.MotionPhase.STATIONARY
            ? ScaffoldMotionPhaseData.PathPattern.STATIONARY
            : (
               candidateDoubleValue > 24.0
                  ? ScaffoldMotionPhaseData.PathPattern.TURNING
                  : (
                     defaultDoubleValue / Math.max(1.0E-6, selectedDoubleValue) > 0.38
                        ? ScaffoldMotionPhaseData.PathPattern.DIAGONAL
                        : ScaffoldMotionPhaseData.PathPattern.AXIS_ALIGNED
                  )
            );
         RotationVector rotationVector = predictedEyeAtClick(scaffoldPlayerSnapshot, placementCandidate, currentFinite ? new ScaffoldRemapService.WorldVector(outputDoubleValue, resultDoubleValue) : null);
         RotationData rotationData = RotationData.lookAt(rotationVector, placementCandidate.hitPoint());
         double initialDoubleValue = RotationData.yawDelta(rotationData.yaw(), sourceDoubleValue);
         double resolvedDoubleValue = Math.abs(initialDoubleValue);
         ScaffoldMotionPhaseData.ViewRelation viewRelation;
         if (resolvedDoubleValue <= 32.0) {
            viewRelation = ScaffoldMotionPhaseData.ViewRelation.FORWARD;
         } else if (resolvedDoubleValue >= 148.0) {
            viewRelation = ScaffoldMotionPhaseData.ViewRelation.BACKWARD;
         } else if (resolvedDoubleValue >= 62.0 && resolvedDoubleValue <= 118.0) {
            viewRelation = initialDoubleValue < 0.0 ? ScaffoldMotionPhaseData.ViewRelation.STRAFE_LEFT : ScaffoldMotionPhaseData.ViewRelation.STRAFE_RIGHT;
         } else {
            viewRelation = ScaffoldMotionPhaseData.ViewRelation.OBLIQUE;
         }

         ScaffoldMotionPhaseData.PathRelation pathRelation = enabled ? ScaffoldMotionPhaseData.PathRelation.RECOVERY : createPathRelation(currentPlacementCandidate, placementCandidate, pathPattern, viewRelation);
         double nextX = placementCandidate.targetPosition().x() + 0.5;
         double computedDoubleValue = placementCandidate.targetPosition().z() + 0.5;
         double previousX = Math.hypot(nextX - scaffoldPlayerSnapshot.feetPosition().x(), computedDoubleValue - scaffoldPlayerSnapshot.feetPosition().z());
         double cachedDoubleValue = nextFinite ? nextDoubleValue : previousDoubleValue;
         double pendingDoubleValue = cachedDoubleValue < 0.0125
            ? 64.0
            : Math.max(0.0, (previousX - 0.3) / cachedDoubleValue);
         double activeDoubleValue = calculateValue4(
            (5.0 - pendingDoubleValue) / 4.5, 0.0, 1.0
         );
         double fallbackDoubleValue = previousDoubleValue < 0.0125
            ? 0.0
            : 0.055 + activeDoubleValue * 0.035;
         double primaryDoubleValue;
         double secondaryDoubleValue;
         switch (placementCandidate.clickedFace()) {
            case EAST:
            case WEST:
               primaryDoubleValue = resultDoubleValue * fallbackDoubleValue;
               secondaryDoubleValue = calculateValue(viewRelation, activeDoubleValue);
               break;
            case NORTH:
            case SOUTH:
               primaryDoubleValue = outputDoubleValue * fallbackDoubleValue;
               secondaryDoubleValue = calculateValue(viewRelation, activeDoubleValue);
               break;
            case UP:
            case DOWN:
               primaryDoubleValue = outputDoubleValue * fallbackDoubleValue;
               secondaryDoubleValue = resultDoubleValue * fallbackDoubleValue;
               break;
            default:
               throw new IllegalStateException("Unknown face " + placementCandidate.clickedFace());
         }

         double tertiaryDoubleValue = 0.86 + activeDoubleValue * 0.22;
         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.STATIONARY) {
            tertiaryDoubleValue *= 0.72;
         }

         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.ASCENDING) {
            tertiaryDoubleValue *= 0.86;
         }

         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.APEX) {
            tertiaryDoubleValue *= 0.94;
         }

         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.DESCENDING) {
            tertiaryDoubleValue *= 1.08;
         }

         if (pathPattern == ScaffoldMotionPhaseData.PathPattern.TURNING) {
            tertiaryDoubleValue *= 0.78;
         }

         if (pathPattern == ScaffoldMotionPhaseData.PathPattern.DIAGONAL) {
            tertiaryDoubleValue *= 0.91;
         }

         if (viewRelation == ScaffoldMotionPhaseData.ViewRelation.STRAFE_LEFT || viewRelation == ScaffoldMotionPhaseData.ViewRelation.STRAFE_RIGHT) {
            tertiaryDoubleValue *= 0.9;
         }

         if (pathRelation == ScaffoldMotionPhaseData.PathRelation.RECOVERY) {
            tertiaryDoubleValue *= 1.12;
         }

         double temporaryDoubleValue = pathPattern == ScaffoldMotionPhaseData.PathPattern.TURNING ? 0.14 : 0.0;
         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.STATIONARY) {
            temporaryDoubleValue += 0.12;
         }

         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.ASCENDING) {
            temporaryDoubleValue += 0.06;
         }

         if (motionPhase == ScaffoldMotionPhaseData.MotionPhase.DESCENDING) {
            temporaryDoubleValue -= 0.1;
         }

         if (pathRelation.isPredictableChain()) {
            temporaryDoubleValue -= 0.08;
         }

         if (pathRelation == ScaffoldMotionPhaseData.PathRelation.RECOVERY) {
            temporaryDoubleValue -= 0.16;
         }

         temporaryDoubleValue -= activeDoubleValue * 0.07;

         double requestedDoubleValue = switch (pathRelation) {
            case STRAIGHT_CHAIN, BACKWARD_CHAIN -> 3.1;
            case DIAGONAL_CHAIN -> 2.5;
            case SIDE_SUPPORT_TRANSITION -> 1.9;
            default -> 2.15;
            case RECOVERY -> 8.0;
         };
         return new ScaffoldMotionPhaseData(pathPattern, viewRelation, pathRelation, sourceDoubleValue, rotationVector, pendingDoubleValue, activeDoubleValue, primaryDoubleValue, secondaryDoubleValue, tertiaryDoubleValue, temporaryDoubleValue, requestedDoubleValue, motionPhase);
      } else {
         throw new IllegalArgumentException("Projected closing speed must be non-negative or NaN");
      }
   }

   private static ScaffoldMotionPhaseData.MotionPhase createMotionPhase(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, double doubleValue) {
      if (scaffoldPlayerSnapshot.onGround()) {
         return doubleValue < 0.0125
            ? ScaffoldMotionPhaseData.MotionPhase.STATIONARY
            : ScaffoldMotionPhaseData.MotionPhase.GROUNDED;
      } else if (scaffoldPlayerSnapshot.velocity().y() > 0.055) {
         return ScaffoldMotionPhaseData.MotionPhase.ASCENDING;
      } else {
         return scaffoldPlayerSnapshot.velocity().y() < -0.055
            ? ScaffoldMotionPhaseData.MotionPhase.DESCENDING
            : ScaffoldMotionPhaseData.MotionPhase.APEX;
      }
   }

   private static ScaffoldMotionPhaseData.PathRelation createPathRelation(
      PlacementCandidate placementCandidate, PlacementCandidate currentPlacementCandidate, ScaffoldMotionPhaseData.PathPattern pathPattern, ScaffoldMotionPhaseData.ViewRelation viewRelation
   ) {
      if (placementCandidate == null) {
         return ScaffoldMotionPhaseData.PathRelation.FIRST;
      } else {
         BlockCoordinates blockCoordinates = placementCandidate.targetPosition();
         BlockCoordinates currentBlockCoordinates = currentPlacementCandidate.targetPosition();
         int currentX = Math.abs(blockCoordinates.x() - currentBlockCoordinates.x());
         int currentY = Math.abs(blockCoordinates.y() - currentBlockCoordinates.y());
         int value = Math.abs(blockCoordinates.z() - currentBlockCoordinates.z());
         int currentValue = currentY == 0 && currentX + value == 1 && currentPlacementCandidate.supportPosition().equals(blockCoordinates) ? 1 : 0;
         if (currentValue == 0) {
            return ScaffoldMotionPhaseData.PathRelation.DISCONTINUOUS;
         } else if (placementCandidate.clickedFace() != currentPlacementCandidate.clickedFace()) {
            return pathPattern == ScaffoldMotionPhaseData.PathPattern.DIAGONAL
               ? ScaffoldMotionPhaseData.PathRelation.DIAGONAL_CHAIN
               : ScaffoldMotionPhaseData.PathRelation.SIDE_SUPPORT_TRANSITION;
         } else {
            return viewRelation == ScaffoldMotionPhaseData.ViewRelation.BACKWARD
               ? ScaffoldMotionPhaseData.PathRelation.BACKWARD_CHAIN
               : ScaffoldMotionPhaseData.PathRelation.STRAIGHT_CHAIN;
         }
      }
   }

   private static double calculateValue(ScaffoldMotionPhaseData.ViewRelation viewRelation, double doubleValue) {
      double currentDoubleValue = switch (viewRelation) {
         case FORWARD -> 0.11;
         case BACKWARD -> 0.18;
         case STRAFE_LEFT, STRAFE_RIGHT -> 0.14;
         case OBLIQUE -> 0.15;
      };
      return currentDoubleValue + doubleValue * 0.018;
   }

   static RotationVector predictedEyeAtClick(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate) {
      return predictedEyeAtClick(scaffoldPlayerSnapshot, placementCandidate, null);
   }

   static RotationVector predictedEyeAtClick(ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, PlacementCandidate placementCandidate, ScaffoldRemapService.WorldVector worldVector) {
      RotationVector currentX = new RotationVector(placementCandidate.clickedFace().x(), placementCandidate.clickedFace().y(), placementCandidate.clickedFace().z());
      double doubleValue = scaffoldPlayerSnapshot.eyePosition().subtract(placementCandidate.hitPoint()).dot(currentX);
      if (placementCandidate.clickedFace() != ScaffoldMode.UP && placementCandidate.clickedFace() != ScaffoldMode.DOWN) {
         double currentDoubleValue = scaffoldPlayerSnapshot.velocity().dot(currentX);
         double nextX = worldVector == null ? 0.0 : worldVector.x() * currentX.x() + worldVector.z() * currentX.z();
         if (worldVector != null) {
            if (nextX <= 1.0E-4) {
               return scaffoldPlayerSnapshot.eyePosition();
            }
         } else if (currentDoubleValue <= 1.0E-4) {
            return scaffoldPlayerSnapshot.eyePosition();
         }

         double nextDoubleValue = calculateValue4(
            0.14 + Math.max(0.0, currentDoubleValue) * 0.65,
            0.14,
            0.27
         );
         if (doubleValue >= nextDoubleValue) {
            return scaffoldPlayerSnapshot.eyePosition();
         }

         double previousDoubleValue = nextDoubleValue - doubleValue;
         if (scaffoldPlayerSnapshot.onGround()) {
            double sourceDoubleValue = calculateValue2(
               (doubleValue - 0.025) / 0.019999999999999997
            );
            double targetDoubleValue = calculateValue2(
               (scaffoldPlayerSnapshot.horizontalSpeed() - 0.0125) / 0.0625
            );
            previousDoubleValue *= 1.0 - sourceDoubleValue * (1.0 - targetDoubleValue);
            if (previousDoubleValue == 0.0) {
               return scaffoldPlayerSnapshot.eyePosition();
            }
         }

         RotationVector previousX = createRotationData7(new RotationVector(scaffoldPlayerSnapshot.velocity().x(), 0.0, scaffoldPlayerSnapshot.velocity().z()), currentX, previousDoubleValue, currentDoubleValue);
         if (worldVector != null) {
            RotationVector sourceX = createRotationData7(new RotationVector(worldVector.x(), 0.0, worldVector.z()), currentX, previousDoubleValue, nextX);
            double inputDoubleValue = calculateValue4(currentDoubleValue / 0.075, 0.0, 1.0);
            previousX = sourceX.multiply(1.0 - inputDoubleValue).add(previousX.multiply(inputDoubleValue));
         }

         double outputDoubleValue = currentDoubleValue > 1.0E-4
            ? Math.min(4.0, previousDoubleValue / currentDoubleValue)
            : 0.0;
         double currentY = scaffoldPlayerSnapshot.onGround() ? 0.0 : calculateValue3(scaffoldPlayerSnapshot.velocity().y(), outputDoubleValue);
         return scaffoldPlayerSnapshot.eyePosition().add(currentX.multiply(previousDoubleValue)).add(previousX).add(new RotationVector(0.0, currentY, 0.0));
      } else {
         return scaffoldPlayerSnapshot.eyePosition();
      }
   }

   private static RotationVector createRotationData7(RotationVector rotationVector, RotationVector currentRotationVector, double doubleValue, double currentDoubleValue) {
      if (currentDoubleValue <= 1.0E-4) {
         return new RotationVector(0.0, 0.0, 0.0);
      }

      RotationVector nextRotationVector = rotationVector.subtract(currentRotationVector.multiply(currentDoubleValue)).multiply(doubleValue / currentDoubleValue);
      double currentX = Math.hypot(nextRotationVector.x(), nextRotationVector.z());
      return currentX > 0.55
         ? nextRotationVector.multiply(0.55 / currentX)
         : nextRotationVector;
   }

   private static double calculateValue2(double doubleValue) {
      double currentDoubleValue = calculateValue4(doubleValue, 0.0, 1.0);
      return currentDoubleValue * currentDoubleValue * (3.0 - 2.0 * currentDoubleValue);
   }

   private static double calculateValue3(double doubleValue, double currentDoubleValue) {
      double nextDoubleValue = doubleValue;
      double previousDoubleValue = 0.0;
      int value = (int)Math.floor(currentDoubleValue);

      for (int index = 0; index < value; index++) {
         previousDoubleValue += nextDoubleValue;
         nextDoubleValue = (nextDoubleValue - 0.08) * 0.98;
      }

      previousDoubleValue += nextDoubleValue * (currentDoubleValue - value);
      return calculateValue4(previousDoubleValue, -1.5, 1.5);
   }

   private static double calculateValue4(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return Math.max(currentDoubleValue, Math.min(nextDoubleValue, doubleValue));
   }

   public enum MotionPhase {
      STATIONARY,
      GROUNDED,
      ASCENDING,
      APEX,
      DESCENDING;

      public boolean isAirborne() {
         return this == ASCENDING || this == APEX || this == DESCENDING;
      }


      private static ScaffoldMotionPhaseData.MotionPhase[] $values() {
         return new ScaffoldMotionPhaseData.MotionPhase[]{STATIONARY, GROUNDED, ASCENDING, APEX, DESCENDING};
      }
   }

   public enum PathPattern {
      STATIONARY,
      AXIS_ALIGNED,
      DIAGONAL,
      TURNING;


      private static ScaffoldMotionPhaseData.PathPattern[] $values() {
         return new ScaffoldMotionPhaseData.PathPattern[]{STATIONARY, AXIS_ALIGNED, DIAGONAL, TURNING};
      }
   }

   public enum PathRelation {
      FIRST,
      STRAIGHT_CHAIN,
      DIAGONAL_CHAIN,
      BACKWARD_CHAIN,
      SIDE_SUPPORT_TRANSITION,
      DISCONTINUOUS,
      RECOVERY;

      public boolean isPredictableChain() {
         return this == STRAIGHT_CHAIN || this == DIAGONAL_CHAIN || this == BACKWARD_CHAIN;
      }

      public int transitionDelayTicks() {
         return this != DIAGONAL_CHAIN && this != SIDE_SUPPORT_TRANSITION ? 0 : 1;
      }


      private static ScaffoldMotionPhaseData.PathRelation[] $values() {
         return new ScaffoldMotionPhaseData.PathRelation[]{
            FIRST, STRAIGHT_CHAIN, DIAGONAL_CHAIN, BACKWARD_CHAIN, SIDE_SUPPORT_TRANSITION, DISCONTINUOUS, RECOVERY
         };
      }
   }

   public enum ViewRelation {
      FORWARD,
      BACKWARD,
      STRAFE_LEFT,
      STRAFE_RIGHT,
      OBLIQUE;


      private static ScaffoldMotionPhaseData.ViewRelation[] $values() {
         return new ScaffoldMotionPhaseData.ViewRelation[]{FORWARD, BACKWARD, STRAFE_LEFT, STRAFE_RIGHT, OBLIQUE};
      }
   }
}
