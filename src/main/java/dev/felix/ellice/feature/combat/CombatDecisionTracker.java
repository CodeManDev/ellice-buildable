package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class CombatDecisionTracker {
   private static final int[][] int2 = new int[][]{{0, 0}, {1, 0}, {1, 1}, {1, -1}, {0, 1}, {0, -1}, {-1, 0}, {-1, 1}, {-1, -1}};
   private static final RotationVector rotationData7 = new RotationVector(0.0, 0.0, 0.0);
   private static final int count = 16;
   private static final long timestamp = 1200L;
   private final LinkedHashMap<UUID, CombatDecisionTracker.Memory> entries = new LinkedHashMap<>(16, 0.75F, true);
   private CombatObserveService combatObserveService;
   private CombatObserveService.Forecast forecast;
   private RotationVector rotationData72 = rotationData7;
   private int count2;
   private long timestamp2;
   private long timestamp3;
   private UUID uUID;
   private RotationVector rotationData73;
   private RotationVector rotationData74;
   private RotationVector rotationData75 = rotationData7;
   private RotationVector rotationData76 = rotationData7;
   private ScaffoldRemapService.WorldVector entries2 = new ScaffoldRemapService.WorldVector(0.0, 0.0);
   private long timestamp4;
   private long timestamp5 = Long.MIN_VALUE;
   private long timestamp6;
   private long timestamp7;
   private int count3 = 1;
   private int count4;
   private double value;
   private CombatDecisionTracker.Decision decision;

   public Optional<CombatDecisionTracker.Decision> decide(
      CombatDecisionTracker.Frame currentFrame, CombatDecisionTracker.Profile currentProfile, ScaffoldRemapService.Input currentInput, CombatAllowsService.Terrain currentTerrain
   ) {
      Objects.requireNonNull(currentFrame, "frame");
      Objects.requireNonNull(currentProfile, "profile");
      Objects.requireNonNull(currentInput, "physical");
      Objects.requireNonNull(currentTerrain, "terrain");
      if ((!currentProfile.yieldBackward() || !currentInput.backward()) && (!currentProfile.yieldSneak() || !currentInput.sneak()) && (!currentProfile.yieldJump() || !currentInput.jump())) {
         CombatDecisionTracker.Fighter fighter = currentFrame.self();
         CombatDecisionTracker.Opponent opponent = currentFrame.enemy();
         RotationVector rotationVector = opponent.hitbox()
            .sample(0.5, 0.0, 0.5);
         double doubleValue = calculateValue(fighter.feet(), opponent.hitbox(), rotationData7);
         double currentY = Math.max(
            0.0,
            Math.max(opponent.hitbox().minY() - fighter.feet().y() - fighter.eyeHeight(), fighter.feet().y() + fighter.eyeHeight() - opponent.hitbox().maxY())
         );
         if (!(doubleValue > currentProfile.pursuitRange())
            && !(currentY >= currentFrame.attackReach())
            && !(Math.abs(rotationVector.y() - fighter.feet().y()) > 2.0)
            && !(Math.hypot(fighter.velocity().x(), fighter.velocity().z()) > 0.85)) {
            if (!this.checkCondition3(currentFrame, rotationVector)) {
               return Optional.empty();
            }

            double currentX = rotationVector.x() - fighter.feet().x();
            double currentDoubleValue = rotationVector.z() - fighter.feet().z();
            double nextDoubleValue = Math.hypot(currentX, currentDoubleValue);
            if (nextDoubleValue < 0.01) {
               return this.findResult();
            }

            ScaffoldRemapService.WorldVector currentWorldVector = new ScaffoldRemapService.WorldVector(currentX / nextDoubleValue, currentDoubleValue / nextDoubleValue);
            ScaffoldRemapService.WorldVector nextX = new ScaffoldRemapService.WorldVector(currentWorldVector.z(), -currentWorldVector.x());
            double previousDoubleValue = calculateValue2(this.rotationData75, currentWorldVector);
            double sourceDoubleValue = calculateValue2(this.rotationData75, nextX);
            CombatDecisionTracker.OpponentMotion opponentMotion = createOpponentMotion(previousDoubleValue, sourceDoubleValue);
            this.updateState(currentFrame, currentProfile, currentWorldVector, nextX);
            double targetDoubleValue = 0.75
               + this.value * 1.75;
            RotationVector currentRotationVector = this.createRotationData7(targetDoubleValue, currentWorldVector, nextX, true);
            RotationVector nextRotationVector = this.createRotationData72(
               this.rotationData75,
               this.forecast == null ? rotationData7 : createRotationData73(this.forecast.offset(targetDoubleValue).multiply(1.0 / targetDoubleValue), currentWorldVector, nextX)
            );
            double inputDoubleValue = calculateValue2(nextRotationVector, currentWorldVector);
            double outputDoubleValue = calculateValue2(nextRotationVector, nextX);
            this.updateState2(currentFrame, currentInput, nextX, outputDoubleValue);
            double resultDoubleValue = Math.sqrt(currentFrame.attackReach() * currentFrame.attackReach() - currentY * currentY);
            double candidateDoubleValue = Math.max(
               0.45,
               Math.min(currentProfile.distance(), resultDoubleValue - 0.35)
            );
            double selectedDoubleValue = currentProfile.style() == CombatDecisionTracker.Style.DEFENSIVE
               ? 0.55
               : 0.32;
            int currentValue = fighter.healthRatio() < selectedDoubleValue
                  && fighter.healthRatio() + 0.12 < opponent.healthRatio()
               ? 1
               : 0;
            double defaultDoubleValue = Math.max(0.0, 1.0 - fighter.attackStrength()) * fighter.cooldownTicks();
            int nextValue = this.forecast != null
                  && this.forecast.confidence() > 0.4
                  && this.forecast.pattern() == CombatObserveService.Pattern.REENGAGE
                  && defaultDoubleValue > 2.0
               ? 1
               : 0;
            int previousValue = fighter.hurtTicks() <= 0
                  && (!(defaultDoubleValue > 4.0) || opponent.hurtTicks() != 0)
                  && nextValue == 0
               ? 0
               : 1;
            int sourceValue = opponent.hurtTicks() <= 0
                  && (
                     this.timestamp5 == Long.MIN_VALUE
                        || currentFrame.tick() - this.timestamp5 > 8L
                        || !(previousDoubleValue > 0.045)
                  )
               ? 0
               : 1;
            int targetValue = opponent.blockingAgainstUs() && doubleValue <= resultDoubleValue + 0.25 ? 1 : 0;
            CombatDecisionTracker.Tactic currentTactic;
            if (currentValue != 0) {
               currentTactic = CombatDecisionTracker.Tactic.EVADE;
               candidateDoubleValue = Math.min(currentProfile.pursuitRange(), resultDoubleValue + 0.35);
            } else if (targetValue != 0) {
               currentTactic = currentProfile.strafe() ? CombatDecisionTracker.Tactic.FLANK : CombatDecisionTracker.Tactic.SPACE;
               candidateDoubleValue = Math.max(
                  0.45,
                  Math.min(
                     resultDoubleValue - 0.12,
                     Math.max(candidateDoubleValue, resultDoubleValue - 0.35)
                  )
               );
            } else if (previousValue != 0
               && doubleValue < candidateDoubleValue + 0.45
               && currentProfile.style() != CombatDecisionTracker.Style.PRESSURE) {
               currentTactic = CombatDecisionTracker.Tactic.SPACE;
               candidateDoubleValue = Math.min(
                  resultDoubleValue - 0.1, candidateDoubleValue + 0.35
               );
            } else if (sourceValue != 0 && defaultDoubleValue <= 4.0) {
               currentTactic = CombatDecisionTracker.Tactic.PRESSURE;
               candidateDoubleValue = Math.max(0.65, candidateDoubleValue - 0.16);
            } else if (doubleValue
               > resultDoubleValue
                  - (
                     this.decision != null && this.decision.tactic() == CombatDecisionTracker.Tactic.PURSUE
                        ? 0.3
                        : 0.15
                  )) {
               currentTactic = CombatDecisionTracker.Tactic.PURSUE;
            } else {
               ScaffoldRemapService.WorldVector nextWorldVector = ScaffoldRemapService.worldVector(opponent.yaw(), 1, 0);
               double initialDoubleValue = this.decision != null && this.decision.tactic() == CombatDecisionTracker.Tactic.FLANK
                  ? -0.25
                  : -0.4;
               currentTactic = currentProfile.strafe() && nextWorldVector.dot(currentWorldVector) < initialDoubleValue ? CombatDecisionTracker.Tactic.FLANK : CombatDecisionTracker.Tactic.SPACE;
            }

            if (currentProfile.style() == CombatDecisionTracker.Style.PRESSURE && currentValue == 0 && targetValue == 0) {
               candidateDoubleValue = Math.max(0.65, candidateDoubleValue - 0.12);
            }

            double resolvedDoubleValue = calculateValue(fighter.feet(), opponent.hitbox(), currentRotationVector);
            double computedDoubleValue = calculateValue3(resolvedDoubleValue - candidateDoubleValue, 0.12);
            double cachedDoubleValue = Math.min(
               0.38, currentFrame.physics().walkAcceleration() / (1.0 - currentFrame.physics().drag())
            );
            int inputValue = currentInput.sprint()
                  && currentValue == 0
                  && (currentTactic == CombatDecisionTracker.Tactic.PURSUE || currentTactic == CombatDecisionTracker.Tactic.PRESSURE)
                  && computedDoubleValue > 0.2
               ? 1
               : 0;
            double pendingDoubleValue = inputValue != 0
               ? Math.min(
                  0.48, currentFrame.physics().sprintAcceleration() / (1.0 - currentFrame.physics().drag())
               )
               : cachedDoubleValue;
            double activeDoubleValue = calculateValue5(
               computedDoubleValue * 0.42 + inputDoubleValue * 0.7, -cachedDoubleValue, pendingDoubleValue
            );
            double fallbackDoubleValue;
            if (!currentProfile.strafe()) {
               fallbackDoubleValue = 0.0;
            } else {
               fallbackDoubleValue = cachedDoubleValue * switch (currentTactic) {
                  case FLANK -> targetValue != 0 ? 0.9 : 0.68;
                  case SPACE, EVADE -> 0.4;
                  default -> 0.15;
               };
            }

            double primaryDoubleValue = fallbackDoubleValue;
            double secondaryDoubleValue = calculateValue5(outputDoubleValue * 0.85 + this.count3 * primaryDoubleValue, -cachedDoubleValue, cachedDoubleValue);
            if (doubleValue > resultDoubleValue) {
               secondaryDoubleValue *= 0.45;
            }

            double previousX = currentWorldVector.x() * activeDoubleValue + nextX.x() * secondaryDoubleValue;
            double tertiaryDoubleValue = currentWorldVector.z() * activeDoubleValue + nextX.z() * secondaryDoubleValue;
            double temporaryDoubleValue = Math.hypot(previousX, tertiaryDoubleValue);
            if (temporaryDoubleValue > pendingDoubleValue) {
               previousX *= pendingDoubleValue / temporaryDoubleValue;
               tertiaryDoubleValue *= pendingDoubleValue / temporaryDoubleValue;
            }

            ArrayList arrayList = new ArrayList(9);

            for (int[] ints : int2) {
               ScaffoldRemapService.WorldVector previousWorldVector = ScaffoldRemapService.worldVector((float)currentFrame.serverYaw(), ints[0], ints[1]);
               int outputValue = inputValue != 0 && ints[0] > 0 && previousWorldVector.dot(currentWorldVector) > 0.65 ? 1 : 0;
               double requestedDoubleValue = (outputValue != 0 ? currentFrame.physics().sprintAcceleration() : currentFrame.physics().walkAcceleration())
                  * 0.98;
               double sourceX = fighter.velocity().x() + previousWorldVector.x() * requestedDoubleValue;
               double actualDoubleValue = fighter.velocity().z() + previousWorldVector.z() * requestedDoubleValue;
               double expectedDoubleValue = calculateValue4(sourceX - previousX) + calculateValue4(actualDoubleValue - tertiaryDoubleValue);
               double minimumDoubleValue = 1.0 + currentFrame.physics().drag();
               RotationVector previousRotationVector = fighter.feet().add(new RotationVector(sourceX * minimumDoubleValue, 0.0, actualDoubleValue * minimumDoubleValue));
               double maximumDoubleValue = calculateValue(previousRotationVector, opponent.hitbox(), this.createRotationData7(minimumDoubleValue, currentWorldVector, nextX, false));
               expectedDoubleValue += calculateValue4(calculateValue3(maximumDoubleValue - candidateDoubleValue, 0.18))
                  * 0.018;
               expectedDoubleValue += calculateValue4(Math.max(0.0, 0.75 - maximumDoubleValue))
                  * 0.3;
               expectedDoubleValue -= this.entries2.dot(previousWorldVector) * 8.0E-4;
               arrayList.add(
                  new CombatDecisionTracker.Candidate(
                     new ScaffoldRemapService.Input(ints[0] > 0, ints[0] < 0, ints[1] > 0, ints[1] < 0, false, false, (outputValue != 0)),
                     previousWorldVector,
                     outputValue == 0,
                     requestedDoubleValue,
                     expectedDoubleValue
                  )
               );
            }

            arrayList.sort(Comparator.comparingDouble(CombatDecisionTracker.Candidate::score));

            for (CombatDecisionTracker.Candidate candidate : (Iterable<CombatDecisionTracker.Candidate>) (Iterable<?>) (arrayList)) {
               if (checkCondition2(candidate, currentFrame, currentTerrain)) {
                  candidate = this.createCandidate(candidate, arrayList, currentFrame, currentInput, currentTerrain, currentTactic, doubleValue);
                  if (this.decision == null || !checkCondition(this.entries2, candidate.direction())) {
                     this.timestamp7 = currentFrame.tick();
                  }

                  this.entries2 = candidate.direction();
                  if (currentProfile.strafe() && Math.abs(candidate.direction().dot(nextX)) > 0.5) {
                     int resultValue = candidate.direction().dot(nextX) > 0.0 ? 1 : -1;
                     if (resultValue != this.count3) {
                        this.count3 = resultValue;
                        this.timestamp6 = currentFrame.tick() + 8L;
                     }
                  }

                  this.decision = new CombatDecisionTracker.Decision(
                     candidate.input(), candidate.suppressSprint(), currentTactic, opponentMotion, this.value, candidateDoubleValue
                  );
                  return Optional.of(this.decision);
               }
            }

            this.entries2 = new ScaffoldRemapService.WorldVector(0.0, 0.0);
            this.timestamp7 = currentFrame.tick();
            this.decision = new CombatDecisionTracker.Decision(
               new ScaffoldRemapService.Input(false, false, false, false, false, false, false),
               true,
               CombatDecisionTracker.Tactic.BLOCKED,
               opponentMotion,
               this.value,
               candidateDoubleValue
            );
            return Optional.of(this.decision);
         } else {
            return this.findResult();
         }
      } else {
         return this.findResult();
      }
   }

   public void attacked(long longValue, UUID currentUUID) {
      if (currentUUID.equals(this.uUID)) {
         this.timestamp5 = longValue;
      }
   }

   public Optional<CombatDecisionTracker.Decision> lastDecision() {
      return Optional.ofNullable(this.decision);
   }

   public Optional<CombatObserveService.Forecast> learning() {
      return Optional.ofNullable(this.forecast);
   }

   int rememberedOpponents() {
      return this.entries.size();
   }

   private CombatDecisionTracker.Candidate createCandidate(
      CombatDecisionTracker.Candidate candidate,
      ArrayList<CombatDecisionTracker.Candidate> arrayList,
      CombatDecisionTracker.Frame frame,
      ScaffoldRemapService.Input input,
      CombatAllowsService.Terrain terrain,
      CombatDecisionTracker.Tactic currentTactic,
      double doubleValue
   ) {
      if (this.decision != null
         && this.decision.tactic() == currentTactic
         && currentTactic != CombatDecisionTracker.Tactic.EVADE
         && input.leftAxis() == 0
         && frame.self().hurtTicks() <= 0
         && !(doubleValue < 1.2)) {
         CombatDecisionTracker.Candidate currentX = arrayList.stream()
            .min(
               Comparator.comparingDouble(
                  item -> calculateValue4(item.direction().x() - this.entries2.x())
                     + calculateValue4(item.direction().z() - this.entries2.z())
               )
            )
            .orElse(candidate);
         if (currentX == candidate) {
            return candidate;
         }

         double currentDoubleValue = frame.tick() - this.timestamp7 < 4L
            ? 0.004
            : 8.0E-4;
         if (this.entries2.length() < 0.01) {
            currentDoubleValue *= 0.45;
         }

         return currentX.score() - candidate.score() <= currentDoubleValue && checkCondition2(currentX, frame, terrain) ? currentX : candidate;
      } else {
         return candidate;
      }
   }

   private static boolean checkCondition(ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector) {
      return !(worldVector.length() < 0.01)
            && !(currentWorldVector.length() < 0.01)
         ? worldVector.dot(currentWorldVector) > 0.95
         : worldVector.length() < 0.01 && currentWorldVector.length() < 0.01;
   }

   private static boolean checkCondition2(CombatDecisionTracker.Candidate candidate, CombatDecisionTracker.Frame frame, CombatAllowsService.Terrain terrain) {
      return CombatAllowsService.allows(
         candidate.direction(),
         frame.self().velocity(),
         frame.physics().walkAcceleration() * 0.98,
         candidate.acceleration(),
         frame.physics().drag(),
         terrain
      );
   }

   public void suspend() {
      if (this.combatObserveService != null) {
         this.combatObserveService.interrupt();
      }

      this.combatObserveService = null;
      this.forecast = null;
      this.uUID = null;
      this.rotationData73 = this.rotationData74 = null;
      this.rotationData75 = this.rotationData76 = this.rotationData72 = rotationData7;
      this.entries2 = new ScaffoldRemapService.WorldVector(0.0, 0.0);
      this.timestamp5 = Long.MIN_VALUE;
      this.count4 = 0;
      this.value = 0.0;
      this.timestamp6 = this.timestamp7 = 0L;
      this.decision = null;
      this.count2 = 0;
      this.timestamp2 = 0L;
      this.timestamp3 = 0L;
   }

   public void clear() {
      this.suspend();
      this.entries.clear();
   }

   private Optional<CombatDecisionTracker.Decision> findResult() {
      this.suspend();
      return Optional.empty();
   }

   private boolean checkCondition3(CombatDecisionTracker.Frame frame, RotationVector rotationVector) {
      if (!frame.enemy().id().equals(this.uUID)) {
         this.suspend();
         this.uUID = frame.enemy().id();
         this.count3 = (this.uUID.getLeastSignificantBits() & 1L) == 0L ? 1 : -1;
      } else {
         long longValue = frame.tick() - this.timestamp4;
         RotationVector currentRotationVector = rotationVector.subtract(this.rotationData73);
         if (longValue == 0L) {
            return false;
         }

         if (longValue < 0L
            || longValue > 3L
            || currentRotationVector.length() > 1.4 * longValue
            || frame.self().feet().subtract(this.rotationData74).length() > 1.4 * longValue) {
            if (longValue < 0L
               || currentRotationVector.length() > 1.4 * Math.max(1L, longValue)
               || frame.self().feet().subtract(this.rotationData74).length()
                  > 1.4 * Math.max(1L, longValue)) {
               this.entries.remove(this.uUID);
            }

            this.suspend();
            return false;
         }

         RotationVector currentX = new RotationVector(currentRotationVector.x() / longValue, 0.0, currentRotationVector.z() / longValue);
         this.rotationData72 = currentX;
         if (longValue != 1L) {
            this.timestamp2 = frame.tick() + 3L;
         }

         double currentLength = currentX.subtract(this.rotationData75).length();
         double doubleValue = currentX.dot(this.rotationData75) < 0.0
            ? 0.85
            : 0.55;
         RotationVector nextRotationVector = this.rotationData75.multiply(1.0 - doubleValue).add(currentX.multiply(doubleValue));
         this.rotationData76 = createRotationData74(nextRotationVector.subtract(this.rotationData75), 0.035);
         this.rotationData75 = createRotationData74(nextRotationVector, 0.65);
         this.count4++;
         this.value = Math.min(1.0, this.count4 / 8.0)
            * calculateValue5(1.0 - currentLength * 2.5, 0.15, 1.0);
      }

      this.timestamp4 = frame.tick();
      this.rotationData73 = rotationVector;
      this.rotationData74 = frame.self().feet();
      return true;
   }

   private void updateState(
      CombatDecisionTracker.Frame frame, CombatDecisionTracker.Profile profile, ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector
   ) {
      this.forecast = null;
      if (!profile.learning()) {
         this.entries.clear();
         this.combatObserveService = null;
      } else {
         this.entries
            .entrySet()
            .removeIf(entry -> frame.tick() < entry.getValue().seen() || frame.tick() - entry.getValue().seen() > 1200L);
         CombatDecisionTracker.Memory memory = this.entries.get(this.uUID);
         this.combatObserveService = memory == null ? new CombatObserveService() : memory.model();
         this.entries.put(this.uUID, new CombatDecisionTracker.Memory(this.combatObserveService, frame.tick()));

         while (this.entries.size() > 16) {
            this.entries.pollFirstEntry();
         }

         if (!frame.learnable() || this.rotationData72.length() > 0.65) {
            this.timestamp2 = frame.tick() + 3L;
         }

         if (frame.enemy().hurtTicks() > this.count2) {
            this.timestamp3 = frame.tick() + 3L;
         }

         this.count2 = frame.enemy().hurtTicks();
         if (this.count4 != 0 && frame.tick() > this.timestamp2) {
            double doubleValue = 3.0;
            RotationVector rotationVector = this.rotationData75
               .multiply(doubleValue)
               .add(this.rotationData76.multiply(0.5 * doubleValue * doubleValue * this.value));
            this.forecast = this.combatObserveService
               .observe(
                  frame.tick(),
                  calculateValue2(this.rotationData72, worldVector),
                  calculateValue2(this.rotationData72, currentWorldVector),
                  frame.enemy().blockingAgainstUs(),
                  frame.enemy().hurtTicks(),
                  new RotationVector(calculateValue2(rotationVector, worldVector), 0.0, calculateValue2(rotationVector, currentWorldVector))
               );
            if (frame.tick() <= this.timestamp3) {
               this.forecast = null;
            }
         } else {
            this.combatObserveService.interrupt();
         }
      }
   }

   private RotationVector createRotationData7(double doubleValue, ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector, boolean enabled) {
      RotationVector rotationVector = this.rotationData75.multiply(doubleValue);
      if (enabled) {
         rotationVector = rotationVector.add(this.rotationData76.multiply(0.5 * doubleValue * doubleValue * this.value));
      }

      return this.createRotationData72(rotationVector, this.forecast == null ? rotationData7 : createRotationData73(this.forecast.offset(doubleValue), worldVector, currentWorldVector));
   }

   private RotationVector createRotationData72(RotationVector rotationVector, RotationVector currentRotationVector) {
      double doubleValue = this.forecast == null ? 0.0 : this.forecast.confidence() * 0.9;
      return rotationVector.multiply(1.0 - doubleValue).add(currentRotationVector.multiply(doubleValue));
   }

   private static RotationVector createRotationData73(RotationVector rotationVector, ScaffoldRemapService.WorldVector worldVector, ScaffoldRemapService.WorldVector currentWorldVector) {
      return new RotationVector(worldVector.x() * rotationVector.x() + currentWorldVector.x() * rotationVector.z(), 0.0, worldVector.z() * rotationVector.x() + currentWorldVector.z() * rotationVector.z());
   }

   private void updateState2(CombatDecisionTracker.Frame frame, ScaffoldRemapService.Input input, ScaffoldRemapService.WorldVector currentWorldVector, double doubleValue) {
      if (input.leftAxis() != 0) {
         ScaffoldRemapService.WorldVector nextWorldVector = ScaffoldRemapService.worldVector(frame.cameraYaw(), 0, input.leftAxis());
         if (Math.abs(nextWorldVector.dot(currentWorldVector)) > 0.25) {
            this.count3 = nextWorldVector.dot(currentWorldVector) > 0.0 ? 1 : -1;
            this.timestamp6 = frame.tick() + 8L;
            return;
         }
      }

      if (frame.tick() >= this.timestamp6 && frame.enemy().blockingAgainstUs()) {
         ScaffoldRemapService.WorldVector previousWorldVector = ScaffoldRemapService.worldVector(frame.enemy().yaw(), 1, 0);
         double currentDoubleValue = previousWorldVector.dot(currentWorldVector);
         if (Math.abs(currentDoubleValue) > 0.15) {
            this.count3 = currentDoubleValue > 0.0 ? -1 : 1;
            this.timestamp6 = frame.tick() + 12L;
            return;
         }
      }

      if (frame.tick() >= this.timestamp6 && Math.abs(doubleValue) > 0.055) {
         this.count3 = doubleValue > 0.0 ? 1 : -1;
         this.timestamp6 = frame.tick() + 8L;
      }
   }

   private static CombatDecisionTracker.OpponentMotion createOpponentMotion(double doubleValue, double currentDoubleValue) {
      if (Math.abs(currentDoubleValue) > 0.045 && Math.abs(currentDoubleValue) > Math.abs(doubleValue)) {
         return CombatDecisionTracker.OpponentMotion.STRAFING;
      } else if (doubleValue > 0.035) {
         return CombatDecisionTracker.OpponentMotion.RETREATING;
      } else {
         return doubleValue < -0.035
            ? CombatDecisionTracker.OpponentMotion.APPROACHING
            : CombatDecisionTracker.OpponentMotion.STILL;
      }
   }

   private static double calculateValue(RotationVector rotationVector, RotationBoundingBox rotationBoundingBox, RotationVector currentRotationVector) {
      double currentX = Math.max(0.0, Math.max(rotationBoundingBox.minX() + currentRotationVector.x() - rotationVector.x(), rotationVector.x() - rotationBoundingBox.maxX() - currentRotationVector.x()));
      double doubleValue = Math.max(0.0, Math.max(rotationBoundingBox.minZ() + currentRotationVector.z() - rotationVector.z(), rotationVector.z() - rotationBoundingBox.maxZ() - currentRotationVector.z()));
      return Math.hypot(currentX, doubleValue);
   }

   private static RotationVector createRotationData74(RotationVector rotationVector, double doubleValue) {
      double currentLength = rotationVector.length();
      return currentLength > doubleValue ? rotationVector.multiply(doubleValue / currentLength) : rotationVector;
   }

   private static double calculateValue2(RotationVector rotationVector, ScaffoldRemapService.WorldVector worldVector) {
      return rotationVector.x() * worldVector.x() + rotationVector.z() * worldVector.z();
   }

   private static double calculateValue3(double doubleValue, double currentDoubleValue) {
      return Math.copySign(Math.max(0.0, Math.abs(doubleValue) - currentDoubleValue), doubleValue);
   }

   private static double calculateValue4(double doubleValue) {
      return doubleValue * doubleValue;
   }

   private static double calculateValue5(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return Math.max(currentDoubleValue, Math.min(nextDoubleValue, doubleValue));
   }

   private static boolean checkCondition4(double... doubles) {
      for (double doubleValue : doubles) {
         if (!Double.isFinite(doubleValue)) {
            return false;
         }
      }

      return true;
   }

   private record Candidate(
      ScaffoldRemapService.Input input, ScaffoldRemapService.WorldVector direction, boolean suppressSprint, double acceleration, double score
   ) {
   }

   public record Decision(
      ScaffoldRemapService.Input input,
      boolean suppressSprint,
      CombatDecisionTracker.Tactic tactic,
      CombatDecisionTracker.OpponentMotion opponentMotion,
      double confidence,
      double desiredDistance
   ) {
   }

   public record Fighter(
      RotationVector feet,
      RotationVector velocity,
      double eyeHeight,
      double healthRatio,
      double attackStrength,
      double cooldownTicks,
      int hurtTicks
   ) {
      public Fighter(
         RotationVector feet,
         RotationVector velocity,
         double eyeHeight,
         double healthRatio,
         double attackStrength,
         double cooldownTicks,
         int hurtTicks
      ) {
         Objects.requireNonNull(feet, "feet");
         Objects.requireNonNull(velocity, "velocity");
         if (CombatDecisionTracker.checkCondition4(eyeHeight, healthRatio, attackStrength, cooldownTicks)
            && !(eyeHeight < 0.0)
            && !(healthRatio < 0.0)
            && !(attackStrength < 0.0)
            && !(cooldownTicks <= 0.0)
            && hurtTicks >= 0) {
            this.feet = feet;
            this.velocity = velocity;
            this.eyeHeight = eyeHeight;
            this.healthRatio = healthRatio;
            this.attackStrength = attackStrength;
            this.cooldownTicks = cooldownTicks;
            this.hurtTicks = hurtTicks;
         } else {
            throw new IllegalArgumentException("Invalid fighter state");
         }
      }
   }

   public record Frame(
      long tick,
      CombatDecisionTracker.Fighter self,
      CombatDecisionTracker.Opponent enemy,
      double cameraYaw,
      double serverYaw,
      double attackReach,
      CombatDecisionTracker.Physics physics,
      boolean learnable
   ) {
      public Frame(
         long longValue,
         CombatDecisionTracker.Fighter fighter,
         CombatDecisionTracker.Opponent opponent,
         double doubleValue,
         double currentDoubleValue,
         double nextDoubleValue,
         CombatDecisionTracker.Physics physics
      ) {
         this(longValue, fighter, opponent, doubleValue, currentDoubleValue, nextDoubleValue, physics, true);
      }

      public Frame(
         long tick,
         CombatDecisionTracker.Fighter self,
         CombatDecisionTracker.Opponent enemy,
         double cameraYaw,
         double serverYaw,
         double attackReach,
         CombatDecisionTracker.Physics physics,
         boolean learnable
      ) {
         Objects.requireNonNull(self, "self");
         Objects.requireNonNull(enemy, "enemy");
         Objects.requireNonNull(physics, "physics");
         if (CombatDecisionTracker.checkCondition4(cameraYaw, serverYaw, attackReach) && !(attackReach <= 0.0)) {
            this.tick = tick;
            this.self = self;
            this.enemy = enemy;
            this.cameraYaw = cameraYaw;
            this.serverYaw = serverYaw;
            this.attackReach = attackReach;
            this.physics = physics;
            this.learnable = learnable;
         } else {
            throw new IllegalArgumentException("Invalid combat frame");
         }
      }
   }

   private record Memory(CombatObserveService model, long seen) {
   }

   public record Opponent(UUID id, RotationBoundingBox hitbox, double yaw, double healthRatio, int hurtTicks, boolean blockingAgainstUs) {
      public Opponent(UUID uUID, RotationBoundingBox rotationBoundingBox, double doubleValue, double currentDoubleValue, int value) {
         this(uUID, rotationBoundingBox, doubleValue, currentDoubleValue, value, false);
      }

      public Opponent(UUID id, RotationBoundingBox hitbox, double yaw, double healthRatio, int hurtTicks, boolean blockingAgainstUs) {
         Objects.requireNonNull(id, "id");
         Objects.requireNonNull(hitbox, "hitbox");
         if (CombatDecisionTracker.checkCondition4(yaw, healthRatio) && !(healthRatio < 0.0) && hurtTicks >= 0) {
            this.id = id;
            this.hitbox = hitbox;
            this.yaw = yaw;
            this.healthRatio = healthRatio;
            this.hurtTicks = hurtTicks;
            this.blockingAgainstUs = blockingAgainstUs;
         } else {
            throw new IllegalArgumentException("Invalid opponent state");
         }
      }
   }

   public enum OpponentMotion {
      STILL,
      APPROACHING,
      RETREATING,
      STRAFING;


      private static CombatDecisionTracker.OpponentMotion[] $values() {
         return new CombatDecisionTracker.OpponentMotion[]{STILL, APPROACHING, RETREATING, STRAFING};
      }
   }

   public record Physics(double walkAcceleration, double sprintAcceleration, double drag) {
      public Physics(double walkAcceleration, double sprintAcceleration, double drag) {
         if (CombatDecisionTracker.checkCondition4(walkAcceleration, sprintAcceleration, drag)
            && !(walkAcceleration <= 0.0)
            && !(sprintAcceleration < walkAcceleration)
            && !(drag < 0.0)
            && !(drag >= 0.99)) {
            this.walkAcceleration = walkAcceleration;
            this.sprintAcceleration = sprintAcceleration;
            this.drag = drag;
         } else {
            throw new IllegalArgumentException("Invalid ground physics");
         }
      }
   }

   public record Profile(
      CombatDecisionTracker.Style style,
      double distance,
      double pursuitRange,
      boolean strafe,
      boolean learning,
      boolean yieldBackward,
      boolean yieldSneak,
      boolean yieldJump
   ) {
      public Profile(CombatDecisionTracker.Style style, double doubleValue, double currentDoubleValue, boolean enabled) {
         this(style, doubleValue, currentDoubleValue, enabled, true);
      }

      public Profile(CombatDecisionTracker.Style style, double doubleValue, double currentDoubleValue, boolean enabled, boolean currentEnabled) {
         this(style, doubleValue, currentDoubleValue, enabled, currentEnabled, true, true, true);
      }

      public Profile(
         CombatDecisionTracker.Style style,
         double distance,
         double pursuitRange,
         boolean strafe,
         boolean learning,
         boolean yieldBackward,
         boolean yieldSneak,
         boolean yieldJump
      ) {
         Objects.requireNonNull(style, "style");
         if (CombatDecisionTracker.checkCondition4(distance, pursuitRange)
            && !(distance < 0.5)
            && !(distance > 4.0)
            && !(pursuitRange < 1.0)
            && !(pursuitRange > 8.0)) {
            this.style = style;
            this.distance = distance;
            this.pursuitRange = pursuitRange;
            this.strafe = strafe;
            this.learning = learning;
            this.yieldBackward = yieldBackward;
            this.yieldSneak = yieldSneak;
            this.yieldJump = yieldJump;
         } else {
            throw new IllegalArgumentException("Invalid combat AI profile");
         }
      }
   }

   public enum Style {
      BALANCED,
      PRESSURE,
      DEFENSIVE;


      private static CombatDecisionTracker.Style[] $values() {
         return new CombatDecisionTracker.Style[]{BALANCED, PRESSURE, DEFENSIVE};
      }
   }

   public enum Tactic {
      PURSUE,
      PRESSURE,
      FLANK,
      SPACE,
      EVADE,
      BLOCKED;


      private static CombatDecisionTracker.Tactic[] $values() {
         return new CombatDecisionTracker.Tactic[]{PURSUE, PRESSURE, FLANK, SPACE, EVADE, BLOCKED};
      }
   }
}
