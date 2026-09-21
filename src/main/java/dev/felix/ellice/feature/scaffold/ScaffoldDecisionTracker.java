package dev.felix.ellice.feature.scaffold;

import java.util.Objects;
import java.util.Optional;

public final class ScaffoldDecisionTracker {
   public static final long NO_ATTEMPT = 0L;
   private ScaffoldDecisionTracker.CognitivePhase cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.IDLE;
   private ScaffoldDecisionTracker.AimPhase aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
   private ScaffoldDecisionTracker.HandPhase handPhase2 = ScaffoldDecisionTracker.HandPhase.UNCHANGED;
   private ScaffoldDecisionTracker.TargetKey targetKey;
   private ScaffoldDecisionTracker.TargetKey targetKey2;
   private ScaffoldDecisionTracker.TargetKey targetKey3;
   private long timestamp;
   private long timestamp2 = Long.MIN_VALUE;
   private long timestamp3;
   private long timestamp4;
   private long timestamp5;
   private long timestamp6 = Long.MIN_VALUE;
   private long timestamp7 = 1L;
   private long timestamp8 = 0L;
   private long timestamp9 = 0L;
   private int count = -1;
   private int count2;
   private int count3;
   private boolean enabled;
   private int count4 = 6;
   private int count5 = 1;
   private int count6 = 2;

   public ScaffoldDecisionTracker(long longValue) {
   }

   public void configure(int value, int currentValue, int nextValue) {
      if (value >= 1 && value <= 100 && currentValue >= 0 && currentValue <= 20 && nextValue >= 1 && nextValue <= 100) {
         this.count4 = value;
         this.count5 = currentValue;
         this.count6 = nextValue;
      } else {
         throw new IllegalArgumentException("Invalid sequencer timing policy");
      }
   }

   public ScaffoldDecisionTracker.Decision prepareTick(long longValue, ScaffoldDecisionTracker.Context context) {
      return this.createDecision(longValue, context);
   }

   private ScaffoldDecisionTracker.Decision createDecision(long size, ScaffoldDecisionTracker.Context currentContext) {
      Objects.requireNonNull(currentContext, "context");
      if (size <= this.timestamp6) {
         return this.createDecision2();
      }

      this.timestamp6 = size;
      if (this.handPhase2 == ScaffoldDecisionTracker.HandPhase.CLICK_ARMED) {
         this.timestamp8 = 0L;
         this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
         this.timestamp5 = Math.max(this.timestamp5, size);
      }

      if (!currentContext.active() && this.timestamp9 != 0L) {
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.RECONCILING;
         this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
         this.timestamp3 = Math.min(this.timestamp3, size);
         return this.createDecision3(false, false, false, 0L);
      }

      if (!currentContext.active()) {
         this.updateState3();
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.IDLE;
         return this.createDecision2();
      }

      if (this.cognitivePhase2 == ScaffoldDecisionTracker.CognitivePhase.IDLE) {
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
      }

      if (this.checkCondition2(currentContext.confirmedAttemptToken())) {
         this.updateState2(size);
      } else if (this.handPhase2 == ScaffoldDecisionTracker.HandPhase.USE_COOLDOWN) {
         if (size < this.timestamp3) {
            this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.WAIT_FEEDBACK;
            return this.createDecision3(false, this.targetKey != null, false, 0L);
         }

         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.RECONCILING;
         this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
         return this.createDecision3(false, false, false, 0L);
      }

      if (this.cognitivePhase2 == ScaffoldDecisionTracker.CognitivePhase.RECOVER && size < this.timestamp4) {
         return this.createDecision3(false, false, false, 0L);
      }

      if (this.cognitivePhase2 == ScaffoldDecisionTracker.CognitivePhase.RECOVER) {
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
      }

      boolean enabled = this.checkCondition(currentContext);
      this.updateState(size, currentContext.candidate(), currentContext.pathRelation());
      if (this.targetKey != null && currentContext.requiredSlot() >= 0) {
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.VERIFYING;
         return this.createDecision3(enabled, true, false, 0L);
      } else {
         return this.createDecision3(enabled, false, false, 0L);
      }
   }

   public Optional<ScaffoldDecisionTracker.Decision> evaluatePlaceWindow(ScaffoldDecisionTracker.PlaceWindow placeWindow) {
      Objects.requireNonNull(placeWindow, "window");
      if (placeWindow.tick() == this.timestamp6
         && this.targetKey != null
         && this.cognitivePhase2 != ScaffoldDecisionTracker.CognitivePhase.IDLE
         && this.cognitivePhase2 != ScaffoldDecisionTracker.CognitivePhase.RECOVER
         && this.cognitivePhase2 != ScaffoldDecisionTracker.CognitivePhase.WAIT_FEEDBACK
         && this.cognitivePhase2 != ScaffoldDecisionTracker.CognitivePhase.RECONCILING
         && this.handPhase2 == ScaffoldDecisionTracker.HandPhase.READY
         && placeWindow.geometryValid()
         && placeWindow.aimReady()
         && placeWindow.motorPlanId() == this.count3
         && placeWindow.target() != null
         && placeWindow.target().equals(this.targetKey)
         && placeWindow.selectedSlot() >= 0
         && placeWindow.selectedSlot() == placeWindow.requiredSlot()
         && placeWindow.requiredSlot() == this.count
         && placeWindow.useCooldownReady()
         && placeWindow.tick() >= this.timestamp5) {
         boolean currentEnabled = this.checkCondition3(placeWindow.tick(), placeWindow.ray(), placeWindow.minimumFaceMargin());
         int value = placeWindow.angularSpeed() <= placeWindow.maximumClickSpeed() ? 1 : 0;
         if (currentEnabled && value != 0) {
            if (this.timestamp2 == Long.MIN_VALUE) {
               this.timestamp2 = placeWindow.tick();
            }

            int currentValue = !this.enabled && !placeWindow.urgent() ? placeWindow.dwellTicks() : 0;
            if (placeWindow.tick() - this.timestamp2 < currentValue) {
               this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.ACQUIRED;
               return Optional.empty();
            } else {
               this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.VERIFYING;
               this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.ACQUIRED;
               this.timestamp8 = this.timestamp7++;
               this.handPhase2 = ScaffoldDecisionTracker.HandPhase.CLICK_ARMED;
               return Optional.of(this.createDecision3(false, true, true, this.timestamp8));
            }
         } else {
            this.timestamp2 = Long.MIN_VALUE;
            this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOMING;
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   public boolean cancelPreparedWindow(long longValue, long currentLongValue) {
      if (currentLongValue == this.timestamp6
         && longValue != 0L
         && longValue == this.timestamp8
         && this.timestamp9 == 0L
         && this.handPhase2 == ScaffoldDecisionTracker.HandPhase.CLICK_ARMED) {
         this.timestamp8 = 0L;
         this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
         this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOMING;
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.VERIFYING;
         this.timestamp2 = Long.MIN_VALUE;
         this.timestamp5 = Math.max(this.timestamp5, currentLongValue + 1L);
         return true;
      } else {
         return false;
      }
   }

   public void placementAttempted(long longValue, boolean enabled, long currentLongValue) {
      if (longValue != 0L && longValue == this.timestamp8 && this.handPhase2 == ScaffoldDecisionTracker.HandPhase.CLICK_ARMED) {
         this.timestamp8 = 0L;
         if (!enabled) {
            this.count2++;
            if (this.count2 > this.count5) {
               this.targetKey3 = this.targetKey;
               this.timestamp = currentLongValue + Math.max(4L, this.count6 * 2L);
               this.targetKey = null;
               this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
               this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
               this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.RECOVER;
               this.timestamp4 = currentLongValue + this.count6;
               this.timestamp5 = this.timestamp4;
               this.timestamp2 = Long.MIN_VALUE;
               this.count2 = 0;
            } else {
               this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
               this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOMING;
               this.timestamp2 = Long.MIN_VALUE;
               this.timestamp5 = Math.max(this.timestamp5, currentLongValue + 1L);
            }
         } else {
            this.timestamp9 = longValue;
            this.handPhase2 = ScaffoldDecisionTracker.HandPhase.USE_COOLDOWN;
            this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.WAIT_FEEDBACK;
            this.timestamp3 = currentLongValue + this.count4;
            this.timestamp2 = Long.MIN_VALUE;
         }
      }
   }

   public ScaffoldDecisionTracker.TargetKey lockedTarget() {
      return this.targetKey;
   }

   public long pendingAttemptToken() {
      return this.timestamp9;
   }

   public int motorPlanId() {
      return this.count3;
   }

   public ScaffoldDecisionTracker.CognitivePhase cognitivePhase() {
      return this.cognitivePhase2;
   }

   public void quarantinePendingTarget(ScaffoldDecisionTracker.TargetKey currentTargetKey, long longValue) {
      if (currentTargetKey != null
         && this.targetKey != null
         && currentTargetKey.equals(this.targetKey)
         && this.timestamp9 != 0L
         && this.handPhase2 == ScaffoldDecisionTracker.HandPhase.USE_COOLDOWN) {
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.RECONCILING;
         this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
         this.timestamp3 = Math.min(this.timestamp3, longValue);
         this.timestamp2 = Long.MIN_VALUE;
      }
   }

   public boolean retireAttempt(long longValue, ScaffoldDecisionTracker.TargetKey currentTargetKey, ScaffoldDecisionTracker.AttemptOutcome attemptOutcome, long currentLongValue) {
      Objects.requireNonNull(attemptOutcome, "outcome");
      if (longValue != 0L && longValue == this.timestamp9 && currentTargetKey != null && this.targetKey != null && currentTargetKey.equals(this.targetKey)) {
         if (attemptOutcome == ScaffoldDecisionTracker.AttemptOutcome.ACCEPTED) {
            this.targetKey2 = this.targetKey;
         }

         this.targetKey = null;
         this.timestamp9 = 0L;
         this.timestamp8 = 0L;
         this.count2 = 0;
         this.handPhase2 = this.count >= 0 ? ScaffoldDecisionTracker.HandPhase.READY : ScaffoldDecisionTracker.HandPhase.UNCHANGED;
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
         this.aimPhase2 = attemptOutcome == ScaffoldDecisionTracker.AttemptOutcome.ACCEPTED ? ScaffoldDecisionTracker.AimPhase.TRANSITION : ScaffoldDecisionTracker.AimPhase.HOLD;
         this.enabled = false;
         this.timestamp3 = 0L;
         this.timestamp4 = 0L;
         this.timestamp2 = Long.MIN_VALUE;
         this.timestamp5 = Math.max(this.timestamp5, currentLongValue);
         return true;
      } else {
         return false;
      }
   }

   public void abortTarget(ScaffoldDecisionTracker.TargetKey currentTargetKey, long longValue) {
      if (currentTargetKey != null && this.targetKey != null && currentTargetKey.equals(this.targetKey)) {
         if (this.timestamp9 != 0L) {
            this.quarantinePendingTarget(currentTargetKey, longValue);
         } else {
            this.targetKey = null;
            this.timestamp8 = 0L;
            this.timestamp9 = 0L;
            this.count2 = 0;
            this.handPhase2 = this.count >= 0 ? ScaffoldDecisionTracker.HandPhase.READY : ScaffoldDecisionTracker.HandPhase.UNCHANGED;
            this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
            this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
            this.enabled = false;
            this.timestamp3 = 0L;
            this.timestamp4 = 0L;
            this.timestamp2 = Long.MIN_VALUE;
            this.timestamp5 = longValue;
         }
      }
   }

   public ScaffoldDecisionTracker.HandPhase handPhase() {
      return this.handPhase2;
   }

   public void reset() {
      this.updateState3();
      this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.IDLE;
      this.timestamp6 = Long.MIN_VALUE;
      this.timestamp7 = 1L;
      this.count3 = 0;
   }

   private boolean checkCondition(ScaffoldDecisionTracker.Context context) {
      if (context.requiredSlot() < 0) {
         this.count = -1;
         this.handPhase2 = ScaffoldDecisionTracker.HandPhase.UNCHANGED;
         return false;
      }

      if (context.selectedSlot() == context.requiredSlot()) {
         this.count = context.requiredSlot();
         this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
         return false;
      }

      if (this.count != context.requiredSlot() || this.handPhase2 != ScaffoldDecisionTracker.HandPhase.SELECTING) {
         this.count = context.requiredSlot();
      }

      this.handPhase2 = ScaffoldDecisionTracker.HandPhase.SELECTING;
      return true;
   }

   private void updateState(long offset, ScaffoldDecisionTracker.TargetKey currentTargetKey, ScaffoldMotionPhaseData.PathRelation pathRelation) {
      if (currentTargetKey == null) {
         if (this.handPhase2 != ScaffoldDecisionTracker.HandPhase.USE_COOLDOWN) {
            this.targetKey = null;
            this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
            this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
            this.timestamp2 = Long.MIN_VALUE;
         }
      } else if (currentTargetKey.equals(this.targetKey3) && offset < this.timestamp) {
         this.targetKey = null;
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.RECOVER;
         this.timestamp4 = this.timestamp;
      } else if (!currentTargetKey.equals(this.targetKey)) {
         this.enabled = pathRelation.isPredictableChain() && checkCondition4(this.targetKey2, currentTargetKey);
         this.targetKey = currentTargetKey;
         this.count2 = 0;
         this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.COMMITTED;
         this.aimPhase2 = this.enabled ? ScaffoldDecisionTracker.AimPhase.TRANSITION : ScaffoldDecisionTracker.AimPhase.PRIMARY;
         this.timestamp2 = Long.MIN_VALUE;
         this.count3++;
      }
   }

   private boolean checkCondition2(long longValue) {
      return this.timestamp9 != 0L && longValue == this.timestamp9;
   }

   private void updateState2(long offset) {
      this.targetKey2 = this.targetKey;
      this.targetKey = null;
      this.timestamp9 = 0L;
      this.timestamp8 = 0L;
      this.count2 = 0;
      this.handPhase2 = ScaffoldDecisionTracker.HandPhase.READY;
      this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
      this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.TRANSITION;
      this.timestamp2 = Long.MIN_VALUE;
      this.timestamp5 = Math.max(this.timestamp5, offset);
   }

   private boolean checkCondition3(long longValue, ScaffoldDecisionTracker.RaySample raySample, double doubleValue) {
      return raySample != null && raySample.sampledTick() == longValue && raySample.target().equals(this.targetKey) && raySample.edgeMargin() >= doubleValue;
   }

   private static boolean checkCondition4(ScaffoldDecisionTracker.TargetKey targetKey, ScaffoldDecisionTracker.TargetKey currentTargetKey) {
      if (targetKey != null
         && currentTargetKey != null
         && currentTargetKey.supportPosition().equals(targetKey.targetPosition())
         && targetKey.targetPosition().y() == currentTargetKey.targetPosition().y()) {
         int currentX = Math.abs(targetKey.targetPosition().x() - currentTargetKey.targetPosition().x());
         int value = Math.abs(targetKey.targetPosition().z() - currentTargetKey.targetPosition().z());
         return currentX + value == 1;
      } else {
         return false;
      }
   }

   private ScaffoldDecisionTracker.Decision createDecision2() {
      return this.createDecision3(false, false, false, 0L);
   }

   private ScaffoldDecisionTracker.Decision createDecision3(boolean enabled, boolean currentEnabled, boolean nextEnabled, long size) {
      return new ScaffoldDecisionTracker.Decision(
         this.cognitivePhase2, this.aimPhase2, this.handPhase2, this.targetKey, enabled, currentEnabled, nextEnabled, size, this.count3
      );
   }

   private void updateState3() {
      this.cognitivePhase2 = ScaffoldDecisionTracker.CognitivePhase.SEARCH;
      this.aimPhase2 = ScaffoldDecisionTracker.AimPhase.HOLD;
      this.handPhase2 = ScaffoldDecisionTracker.HandPhase.UNCHANGED;
      this.targetKey = null;
      this.targetKey2 = null;
      this.targetKey3 = null;
      this.timestamp = 0L;
      this.timestamp2 = Long.MIN_VALUE;
      this.timestamp3 = 0L;
      this.timestamp4 = 0L;
      this.timestamp5 = 0L;
      this.timestamp8 = 0L;
      this.timestamp9 = 0L;
      this.count = -1;
      this.count2 = 0;
      this.enabled = false;
   }

   public enum AimPhase {
      HOLD,
      PRIMARY,
      HOMING,
      ACQUIRED,
      TRANSITION;


      private static ScaffoldDecisionTracker.AimPhase[] $values() {
         return new ScaffoldDecisionTracker.AimPhase[]{HOLD, PRIMARY, HOMING, ACQUIRED, TRANSITION};
      }
   }

   public enum AttemptOutcome {
      ACCEPTED,
      REJECTED,
      TERMINAL;


      private static ScaffoldDecisionTracker.AttemptOutcome[] $values() {
         return new ScaffoldDecisionTracker.AttemptOutcome[]{ACCEPTED, REJECTED, TERMINAL};
      }
   }

   public enum CognitivePhase {
      IDLE,
      SEARCH,
      COMMITTED,
      VERIFYING,
      WAIT_FEEDBACK,
      RECONCILING,
      RECOVER;


      private static ScaffoldDecisionTracker.CognitivePhase[] $values() {
         return new ScaffoldDecisionTracker.CognitivePhase[]{IDLE, SEARCH, COMMITTED, VERIFYING, WAIT_FEEDBACK, RECONCILING, RECOVER};
      }
   }

   public record Context(
      boolean active,
      int selectedSlot,
      int requiredSlot,
      ScaffoldDecisionTracker.TargetKey candidate,
      ScaffoldMotionPhaseData.PathRelation pathRelation,
      long confirmedAttemptToken
   ) {
      public Context(
         boolean active,
         int selectedSlot,
         int requiredSlot,
         ScaffoldDecisionTracker.TargetKey candidate,
         ScaffoldMotionPhaseData.PathRelation pathRelation,
         long confirmedAttemptToken
      ) {
         if (selectedSlot >= -1 && selectedSlot <= 8 && requiredSlot >= -1 && requiredSlot <= 8) {
            Objects.requireNonNull(pathRelation, "pathRelation");
            if (confirmedAttemptToken < 0L) {
               throw new IllegalArgumentException("Confirmed attempt token must be non-negative");
            }

            this.active = active;
            this.selectedSlot = selectedSlot;
            this.requiredSlot = requiredSlot;
            this.candidate = candidate;
            this.pathRelation = pathRelation;
            this.confirmedAttemptToken = confirmedAttemptToken;
         } else {
            throw new IllegalArgumentException("Hotbar slot must be in [-1, 8]");
         }
      }
   }

   public record Decision(
      ScaffoldDecisionTracker.CognitivePhase cognitivePhase,
      ScaffoldDecisionTracker.AimPhase aimPhase,
      ScaffoldDecisionTracker.HandPhase handPhase,
      ScaffoldDecisionTracker.TargetKey lockedTarget,
      boolean selectBlock,
      boolean rotate,
      boolean place,
      long attemptToken,
      int motorPlanId
   ) {
   }

   public enum HandPhase {
      UNCHANGED,
      SELECTING,
      SETTLING,
      READY,
      CLICK_ARMED,
      USE_COOLDOWN;


      private static ScaffoldDecisionTracker.HandPhase[] $values() {
         return new ScaffoldDecisionTracker.HandPhase[]{UNCHANGED, SELECTING, SETTLING, READY, CLICK_ARMED, USE_COOLDOWN};
      }
   }

   public record PlaceWindow(
      long tick,
      int motorPlanId,
      ScaffoldDecisionTracker.TargetKey target,
      ScaffoldDecisionTracker.RaySample ray,
      boolean geometryValid,
      int selectedSlot,
      int requiredSlot,
      boolean aimReady,
      double angularSpeed,
      double maximumClickSpeed,
      boolean useCooldownReady,
      double minimumFaceMargin,
      int dwellTicks,
      boolean urgent
   ) {
      public PlaceWindow(
         long tick,
         int motorPlanId,
         ScaffoldDecisionTracker.TargetKey target,
         ScaffoldDecisionTracker.RaySample ray,
         boolean geometryValid,
         int selectedSlot,
         int requiredSlot,
         boolean aimReady,
         double angularSpeed,
         double maximumClickSpeed,
         boolean useCooldownReady,
         double minimumFaceMargin,
         int dwellTicks,
         boolean urgent
      ) {
         if (motorPlanId >= 0
            && selectedSlot >= -1
            && selectedSlot <= 8
            && requiredSlot >= -1
            && requiredSlot <= 8
            && Double.isFinite(angularSpeed)
            && !(angularSpeed < 0.0)
            && Double.isFinite(maximumClickSpeed)
            && !(maximumClickSpeed <= 0.0)
            && Double.isFinite(minimumFaceMargin)
            && !(minimumFaceMargin < 0.0)
            && !(minimumFaceMargin > 0.5)
            && dwellTicks >= 0
            && dwellTicks <= 10) {
            this.tick = tick;
            this.motorPlanId = motorPlanId;
            this.target = target;
            this.ray = ray;
            this.geometryValid = geometryValid;
            this.selectedSlot = selectedSlot;
            this.requiredSlot = requiredSlot;
            this.aimReady = aimReady;
            this.angularSpeed = angularSpeed;
            this.maximumClickSpeed = maximumClickSpeed;
            this.useCooldownReady = useCooldownReady;
            this.minimumFaceMargin = minimumFaceMargin;
            this.dwellTicks = dwellTicks;
            this.urgent = urgent;
         } else {
            throw new IllegalArgumentException("Invalid placement window");
         }
      }
   }

   public record RaySample(ScaffoldDecisionTracker.TargetKey target, double edgeMargin, long sampledTick) {
      public RaySample(ScaffoldDecisionTracker.TargetKey target, double edgeMargin, long sampledTick) {
         Objects.requireNonNull(target, "target");
         if (Double.isFinite(edgeMargin) && !(edgeMargin < 0.0) && !(edgeMargin > 0.5)) {
            this.target = target;
            this.edgeMargin = edgeMargin;
            this.sampledTick = sampledTick;
         } else {
            throw new IllegalArgumentException("Face edge margin must be in [0, 0.5]");
         }
      }
   }

   public record TargetKey(BlockCoordinates targetPosition, BlockCoordinates supportPosition, ScaffoldMode clickedFace) {
      public TargetKey(BlockCoordinates targetPosition, BlockCoordinates supportPosition, ScaffoldMode clickedFace) {
         Objects.requireNonNull(targetPosition, "targetPosition");
         Objects.requireNonNull(supportPosition, "supportPosition");
         Objects.requireNonNull(clickedFace, "clickedFace");
         this.targetPosition = targetPosition;
         this.supportPosition = supportPosition;
         this.clickedFace = clickedFace;
      }

      public static ScaffoldDecisionTracker.TargetKey from(PlacementCandidate placementCandidate) {
         Objects.requireNonNull(placementCandidate, "placement");
         return new ScaffoldDecisionTracker.TargetKey(placementCandidate.targetPosition(), placementCandidate.supportPosition(), placementCandidate.clickedFace());
      }
   }
}

