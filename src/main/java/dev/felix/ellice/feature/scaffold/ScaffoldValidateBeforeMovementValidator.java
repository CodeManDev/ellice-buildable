package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public final class ScaffoldValidateBeforeMovementValidator {
   private ScaffoldValidateBeforeMovementValidator() {
   }

   public static ScaffoldValidateBeforeMovementValidator.PrepareCheck validateBeforeMovement(
      ScaffoldValidateBeforeMovementValidator.Arm currentArm, long longValue, long currentLongValue, ScaffoldDecisionTracker.TargetKey targetKey, RotationData rotationData, ScaffoldCorridorService.Evaluation evaluation
   ) {
      Objects.requireNonNull(currentArm, "arm");
      if (longValue != currentArm.tick()) {
         return createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure.STALE_TICK);
      } else if (currentLongValue != currentArm.precedingMovementOrdinal()) {
         return createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure.MOVEMENT_RACED_USE);
      } else if (!currentArm.target().equals(targetKey)) {
         return createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure.TARGET_CHANGED);
      } else if (!sameSentRotation(currentArm.wireRotation(), rotationData)) {
         return createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure.ROTATION_CHANGED);
      } else {
         return evaluation != null && evaluation.ready()
            ? new ScaffoldValidateBeforeMovementValidator.PrepareCheck(true, ScaffoldValidateBeforeMovementValidator.Failure.NONE)
            : createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure.UNSAFE_RAY_ENVELOPE);
      }
   }

   public static ScaffoldValidateBeforeMovementValidator.UseDisposition classifyUse(boolean enabled, int value) {
      if (value >= 0) {
         return ScaffoldValidateBeforeMovementValidator.UseDisposition.ISSUED;
      } else {
         return enabled ? ScaffoldValidateBeforeMovementValidator.UseDisposition.AMBIGUOUS_NATIVE_RESULT : ScaffoldValidateBeforeMovementValidator.UseDisposition.NOT_ISSUED;
      }
   }

   public static boolean followingMovementMatches(ScaffoldValidateBeforeMovementValidator.Arm currentArm, long longValue, RotationData rotationData) {
      Objects.requireNonNull(currentArm, "arm");
      return longValue == currentArm.precedingMovementOrdinal() + 1L && sameSentRotation(currentArm.wireRotation(), rotationData);
   }

   public static boolean sameSentRotation(RotationData rotationData, RotationData currentRotationData) {
      return rotationData != null && currentRotationData != null
         ? Float.floatToIntBits((float)rotationData.yaw()) == Float.floatToIntBits((float)currentRotationData.yaw())
            && Float.floatToIntBits((float)rotationData.pitch()) == Float.floatToIntBits((float)currentRotationData.pitch())
         : false;
   }

   private static ScaffoldValidateBeforeMovementValidator.PrepareCheck createPrepareCheck(ScaffoldValidateBeforeMovementValidator.Failure failure) {
      return new ScaffoldValidateBeforeMovementValidator.PrepareCheck(false, failure);
   }

   public record Arm(
      long tick,
      long precedingMovementOrdinal,
      ScaffoldDecisionTracker.TargetKey target,
      RotationData wireRotation,
      RotationVector previousWireEye,
      long attemptToken
   ) {
      public Arm(
         long tick,
         long precedingMovementOrdinal,
         ScaffoldDecisionTracker.TargetKey target,
         RotationData wireRotation,
         RotationVector previousWireEye,
         long attemptToken
      ) {
         if (tick >= 0L && precedingMovementOrdinal >= 0L && attemptToken > 0L) {
            Objects.requireNonNull(target, "target");
            Objects.requireNonNull(wireRotation, "wireRotation");
            Objects.requireNonNull(previousWireEye, "previousWireEye");
            this.tick = tick;
            this.precedingMovementOrdinal = precedingMovementOrdinal;
            this.target = target;
            this.wireRotation = wireRotation;
            this.previousWireEye = previousWireEye;
            this.attemptToken = attemptToken;
         } else {
            throw new IllegalArgumentException("Invalid placement arm");
         }
      }
   }

   public enum Failure {
      NONE,
      STALE_TICK,
      MOVEMENT_RACED_USE,
      TARGET_CHANGED,
      ROTATION_CHANGED,
      UNSAFE_RAY_ENVELOPE;


      private static ScaffoldValidateBeforeMovementValidator.Failure[] $values() {
         return new ScaffoldValidateBeforeMovementValidator.Failure[]{NONE, STALE_TICK, MOVEMENT_RACED_USE, TARGET_CHANGED, ROTATION_CHANGED, UNSAFE_RAY_ENVELOPE};
      }
   }

   public record PrepareCheck(boolean ready, ScaffoldValidateBeforeMovementValidator.Failure failure) {
      public PrepareCheck(boolean ready, ScaffoldValidateBeforeMovementValidator.Failure failure) {
         Objects.requireNonNull(failure, "failure");
         if (ready != (failure == ScaffoldValidateBeforeMovementValidator.Failure.NONE)) {
            throw new IllegalArgumentException("Ready state and failure disagree");
         }

         this.ready = (boolean)ready;
         this.failure = failure;
      }
   }

   public enum UseDisposition {
      ISSUED,
      NOT_ISSUED,
      AMBIGUOUS_NATIVE_RESULT;


      private static ScaffoldValidateBeforeMovementValidator.UseDisposition[] $values() {
         return new ScaffoldValidateBeforeMovementValidator.UseDisposition[]{ISSUED, NOT_ISSUED, AMBIGUOUS_NATIVE_RESULT};
      }
   }
}
