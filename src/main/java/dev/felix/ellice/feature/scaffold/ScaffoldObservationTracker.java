package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;

public final class ScaffoldObservationTracker {
   private static final ScaffoldRemapService.WorldVector entries = new ScaffoldRemapService.WorldVector(0.0, 0.0);
   private final ScaffoldObservationTracker.Policy policy2;
   private ScaffoldObservationTracker.Phase phase2 = ScaffoldObservationTracker.Phase.IDLE;
   private ScaffoldRemapService.WorldVector entries2 = entries;
   private ScaffoldRemapService.WorldVector entries3 = entries;
   private ScaffoldObservationTracker.InputSignature inputSignature;
   private int count;
   private int count2;
   private int count3;
   private long timestamp = Long.MIN_VALUE;
   private ScaffoldObservationTracker.Snapshot snapshot2 = new ScaffoldObservationTracker.Snapshot(
      ScaffoldObservationTracker.Phase.IDLE, entries, entries, entries, 0.0, 0, 0, false
   );

   public ScaffoldObservationTracker() {
      this(ScaffoldObservationTracker.Policy.defaults());
   }

   public ScaffoldObservationTracker(ScaffoldObservationTracker.Policy currentPolicy) {
      this.policy2 = Objects.requireNonNull(currentPolicy, "policy");
   }

   public ScaffoldObservationTracker.Snapshot update(ScaffoldObservationTracker.Observation currentObservation) {
      Objects.requireNonNull(currentObservation, "observation");
      if (currentObservation.tick() <= this.timestamp) {
         return this.snapshot2;
      }

      int value = this.timestamp != Long.MIN_VALUE && currentObservation.tick() == this.timestamp + 1L ? 1 : 0;
      this.timestamp = currentObservation.tick();
      ScaffoldRemapService.Input input = currentObservation.requestedInput();
      ScaffoldObservationTracker.InputSignature currentInputSignature = new ScaffoldObservationTracker.InputSignature(input.forwardAxis(), input.leftAxis());
      ScaffoldRemapService.WorldVector worldVector = createMap(currentObservation.cameraYaw(), currentInputSignature);
      double currentX = Math.hypot(currentObservation.velocity().x(), currentObservation.velocity().z());
      ScaffoldRemapService.WorldVector nextX = currentX < 1.0E-9
         ? entries
         : new ScaffoldRemapService.WorldVector(currentObservation.velocity().x() / currentX, currentObservation.velocity().z() / currentX);
      byte byteValue = 0;
      if (currentInputSignature.forward() == 0 && currentInputSignature.left() == 0) {
         this.count2++;
         this.updateState();
         this.phase2 = this.count2 >= this.policy2.idleDwellTicks() && currentX < this.policy2.movingSpeed()
            ? ScaffoldObservationTracker.Phase.IDLE
            : ScaffoldObservationTracker.Phase.COASTING;
         return this.createSnapshot(worldVector, nextX, currentX, false);
      }

      this.count2 = 0;
      if (!(this.entries2.length() < 1.0E-9)
         && this.phase2 != ScaffoldObservationTracker.Phase.IDLE
         && (this.phase2 != ScaffoldObservationTracker.Phase.COASTING || !(currentX < this.policy2.movingSpeed()))) {
         double doubleValue = calculateValue(this.entries2, worldVector);
         if (doubleValue <= this.policy2.turnDeadbandDegrees()) {
            this.updateState();
            if (currentX < this.policy2.movingSpeed()) {
               this.phase2 = ScaffoldObservationTracker.Phase.STARTING;
            } else if (this.checkCondition(nextX, this.entries2, currentX)) {
               this.phase2 = ScaffoldObservationTracker.Phase.REVERSING;
            } else {
               this.phase2 = ScaffoldObservationTracker.Phase.TRACKING;
            }

            return this.createSnapshot(worldVector, nextX, currentX, false);
         } else {
            if (value != 0
               && this.inputSignature != null
               && this.inputSignature.equals(currentInputSignature)
               && calculateValue(this.entries3, worldVector) <= this.policy2.candidateToleranceDegrees()) {
               this.count++;
               this.entries3 = worldVector;
            } else {
               this.inputSignature = currentInputSignature;
               this.entries3 = worldVector;
               this.count = 1;
            }

            this.phase2 = doubleValue >= this.policy2.reversalDegrees() ? ScaffoldObservationTracker.Phase.REVERSING : ScaffoldObservationTracker.Phase.TURN_PENDING;
            if (this.count >= this.policy2.structuralCommitTicks()) {
               this.entries2 = worldVector;
               this.count3++;
               byteValue = 1;
               this.updateState();
               if (currentX < this.policy2.movingSpeed()) {
                  this.phase2 = ScaffoldObservationTracker.Phase.STARTING;
               } else if (this.checkCondition(nextX, worldVector, currentX)) {
                  this.phase2 = ScaffoldObservationTracker.Phase.REVERSING;
               } else {
                  this.phase2 = ScaffoldObservationTracker.Phase.TRACKING;
               }
            }

            return this.createSnapshot(worldVector, nextX, currentX, (byteValue != 0));
         }
      } else {
         this.entries2 = worldVector;
         this.count3++;
         byteValue = 1;
         this.updateState();
         this.phase2 = currentX < this.policy2.movingSpeed()
            ? ScaffoldObservationTracker.Phase.STARTING
            : (this.checkCondition(nextX, worldVector, currentX) ? ScaffoldObservationTracker.Phase.REVERSING : ScaffoldObservationTracker.Phase.TRACKING);
         return this.createSnapshot(worldVector, nextX, currentX, (byteValue != 0));
      }
   }

   public ScaffoldObservationTracker.Snapshot current() {
      return this.snapshot2;
   }

   public void reset() {
      this.phase2 = ScaffoldObservationTracker.Phase.IDLE;
      this.entries2 = entries;
      this.updateState();
      this.count2 = 0;
      this.count3 = 0;
      this.timestamp = Long.MIN_VALUE;
      this.snapshot2 = new ScaffoldObservationTracker.Snapshot(ScaffoldObservationTracker.Phase.IDLE, entries, entries, entries, 0.0, 0, 0, false);
   }

   private ScaffoldObservationTracker.Snapshot createSnapshot(ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector, double doubleValue, boolean enabled) {
      this.snapshot2 = new ScaffoldObservationTracker.Snapshot(
         this.phase2, worldVector, this.entries2, currentWorldVector, doubleValue, this.count3, this.count, enabled
      );
      return this.snapshot2;
   }

   private void updateState() {
      this.entries3 = entries;
      this.inputSignature = null;
      this.count = 0;
   }

   private boolean checkCondition(ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector, double doubleValue) {
      return doubleValue >= this.policy2.movingSpeed() && worldVector.dot(currentWorldVector) < -0.15;
   }

   private static ScaffoldRemapService.WorldVector createMap(double doubleValue, ScaffoldObservationTracker.InputSignature inputSignature) {
      return ScaffoldRemapService.worldVector(doubleValue, inputSignature.forward(), inputSignature.left());
   }

   private static double calculateValue(ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector) {
      double currentLength = worldVector.length() * currentWorldVector.length();
      if (currentLength < 1.0E-12) {
         return 0.0;
      }

      double doubleValue = Math.max(-1.0, Math.min(1.0, worldVector.dot(currentWorldVector) / currentLength));
      return Math.toDegrees(Math.acos(doubleValue));
   }

   private record InputSignature(int forward, int left) {
   }

   public record Observation(long tick, ScaffoldRemapService.Input requestedInput, double cameraYaw, RotationVector velocity) {
      public Observation(long tick, ScaffoldRemapService.Input requestedInput, double cameraYaw, RotationVector velocity) {
         Objects.requireNonNull(requestedInput, "requestedInput");
         Objects.requireNonNull(velocity, "velocity");
         if (tick >= 0L && Double.isFinite(cameraYaw)) {
            this.tick = tick;
            this.requestedInput = requestedInput;
            this.cameraYaw = cameraYaw;
            this.velocity = velocity;
         } else {
            throw new IllegalArgumentException("Invalid intent observation");
         }
      }
   }

   public enum Phase {
      IDLE,
      STARTING,
      TRACKING,
      TURN_PENDING,
      REVERSING,
      COASTING;


      private static ScaffoldObservationTracker.Phase[] $values() {
         return new ScaffoldObservationTracker.Phase[]{IDLE, STARTING, TRACKING, TURN_PENDING, REVERSING, COASTING};
      }
   }

   public record Policy(
      double turnDeadbandDegrees,
      double candidateToleranceDegrees,
      double reversalDegrees,
      int structuralCommitTicks,
      double movingSpeed,
      int idleDwellTicks
   ) {
      public Policy(
         double turnDeadbandDegrees,
         double candidateToleranceDegrees,
         double reversalDegrees,
         int structuralCommitTicks,
         double movingSpeed,
         int idleDwellTicks
      ) {
         if (Double.isFinite(turnDeadbandDegrees)
            && !(turnDeadbandDegrees < 0.0)
            && !(turnDeadbandDegrees >= 45.0)
            && Double.isFinite(candidateToleranceDegrees)
            && !(candidateToleranceDegrees < 0.0)
            && !(candidateToleranceDegrees >= 45.0)
            && Double.isFinite(reversalDegrees)
            && !(reversalDegrees <= 90.0)
            && !(reversalDegrees > 180.0)
            && structuralCommitTicks >= 2
            && structuralCommitTicks <= 10
            && Double.isFinite(movingSpeed)
            && !(movingSpeed <= 0.0)
            && idleDwellTicks >= 1
            && idleDwellTicks <= 20) {
            this.turnDeadbandDegrees = turnDeadbandDegrees;
            this.candidateToleranceDegrees = candidateToleranceDegrees;
            this.reversalDegrees = reversalDegrees;
            this.structuralCommitTicks = structuralCommitTicks;
            this.movingSpeed = movingSpeed;
            this.idleDwellTicks = idleDwellTicks;
         } else {
            throw new IllegalArgumentException("Invalid intent policy");
         }
      }

      public static ScaffoldObservationTracker.Policy defaults() {
         return new ScaffoldObservationTracker.Policy(
            12.0,
            12.0,
            120.0,
            2,
            0.01,
            2
         );
      }
   }

   public record Snapshot(
      ScaffoldObservationTracker.Phase phase,
      ScaffoldRemapService.WorldVector requestedDirection,
      ScaffoldRemapService.WorldVector committedDirection,
      ScaffoldRemapService.WorldVector momentumDirection,
      double horizontalSpeed,
      int intentEpoch,
      int candidateTicks,
      boolean structuralCommit
   ) {
      public Snapshot(
         ScaffoldObservationTracker.Phase phase,
         ScaffoldRemapService.WorldVector requestedDirection,
         ScaffoldRemapService.WorldVector committedDirection,
         ScaffoldRemapService.WorldVector momentumDirection,
         double horizontalSpeed,
         int intentEpoch,
         int candidateTicks,
         boolean structuralCommit
      ) {
         Objects.requireNonNull(phase, "phase");
         Objects.requireNonNull(requestedDirection, "requestedDirection");
         Objects.requireNonNull(committedDirection, "committedDirection");
         Objects.requireNonNull(momentumDirection, "momentumDirection");
         if (Double.isFinite(horizontalSpeed) && !(horizontalSpeed < 0.0) && intentEpoch >= 0 && candidateTicks >= 0) {
            this.phase = phase;
            this.requestedDirection = requestedDirection;
            this.committedDirection = committedDirection;
            this.momentumDirection = momentumDirection;
            this.horizontalSpeed = horizontalSpeed;
            this.intentEpoch = intentEpoch;
            this.candidateTicks = candidateTicks;
            this.structuralCommit = structuralCommit;
         } else {
            throw new IllegalArgumentException("Invalid intent snapshot");
         }
      }

      public boolean hasRequestedDirection() {
         return this.requestedDirection.length() > 1.0E-9;
      }

      public boolean allowsStructuralPlanning() {
         return this.phase != ScaffoldObservationTracker.Phase.IDLE && this.phase != ScaffoldObservationTracker.Phase.COASTING;
      }

      public ScaffoldSweepsService pathEnvelope(double doubleValue, double currentDoubleValue) {
         return ScaffoldSweepsService.of(this.requestedDirection, doubleValue, this.momentumDirection, currentDoubleValue);
      }
   }
}
