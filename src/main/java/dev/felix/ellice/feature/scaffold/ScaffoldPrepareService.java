package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.CompatPrepareService;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.LinkedHashMap;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Input;

public final class ScaffoldPrepareService implements ScaffoldOperationHandler {
   private final ScaffoldRemapService entries = new ScaffoldRemapService();
   private final ScaffoldPlanService scaffoldPlanService = new ScaffoldPlanService();
   private final ScaffoldShouldActivateService scaffoldShouldActivateService = new ScaffoldShouldActivateService();
   private final ScaffoldTrajectorySolver scaffoldTrajectorySolver = new ScaffoldTrajectorySolver(ScaffoldTrajectorySolver.SelectionPolicy.MINIMUM_ROTATION);
   private final RotationVanillaGcdService rotationVanillaGcdService = new RotationVanillaGcdService();
   private final ScaffoldSneakService scaffoldSneakService = new ScaffoldSneakService();
   private final ScaffoldReleaseTracker values = new ScaffoldReleaseTracker();
   private final ScaffoldBudgetService scaffoldBudgetService = new ScaffoldBudgetService();
   private RotationData rotationData;
   private RotationData rotationData2;
   private double value;
   private long timestamp;
   private long timestamp2;
   private int count;
   private boolean enabled;
   private double value2 = Double.NaN;
   private double value3 = Double.NaN;
   private String text;
   private long timestamp3;
   private ScaffoldRemapService.Result entries2;
   private PlacementCandidate scaffoldData6;
   private boolean enabled2;
   private PlacementCandidate scaffoldData62;
   private PlacementCandidate scaffoldData63;
   private int count2 = -1;
   private boolean enabled3;
   private boolean enabled4;
   private boolean enabled5;
   private boolean enabled6;
   private boolean enabled7;
   private boolean enabled8;
   private PlacementCandidate scaffoldData64;
   private PlacementCandidate scaffoldData65;
   private int count3;
   private long timestamp4 = Long.MIN_VALUE;
   private double value4;
   private RotationData rotationData3;
   private double value5;
   private boolean enabled9;
   private String text2 = "Waiting for movement";
   private long timestamp5 = Long.MAX_VALUE;
   private int count4 = -1;
   private final LinkedHashMap<Integer, PlacementCandidate> entries3 = new LinkedHashMap<>();

   @Override
   public void prepare(
      final Minecraft minecraft, final ScaffoldCompatibility scaffoldCompatibility, CombatCompatibility combatCompatibility, ScaffoldRemapService.Input currentInput, ScaffoldSettings scaffoldSettings, Runnable runnable
   ) {
      this.timestamp++;
      this.enabled9 = false;
      this.text2 = "Finding a face";
      CompatPrepareService.clear();
      this.enabled3 = scaffoldSettings.diagnostics();
      this.enabled5 = scaffoldSettings.silentSneak();
      ScaffoldData scaffoldData = scaffoldSettings.tuning();
      double doubleValue = scaffoldSettings.turnSpeed();
      double currentDoubleValue = scaffoldSettings.pitch();
      double nextDoubleValue = scaffoldSettings.reach();
      if (this.timestamp5 < this.timestamp) {
         this.entries3
            .entrySet()
            .removeIf(
               entry -> {
                  if (entry.getKey() > this.count4) {
                     return false;
                  }

                  if (this.enabled3) {
                     CoreIsInitializedHandler.LOGGER
                        .info(
                           "[Scaffold] ack sequence={} target={} supporting={}",
                           new Object[]{
                              entry.getKey(),
                              entry.getValue().targetPosition(),
                              scaffoldCompatibility.isPlayerSupporting(minecraft, entry.getValue().targetPosition())
                           }
                        );
                  }

                  return true;
               }
            );
         this.timestamp5 = Long.MAX_VALUE;
      }

      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot = scaffoldCompatibility.capture(minecraft).orElse(null);
      RotationData currentRotationData = RotationObserveService.current().orElse(null);
      this.value5 = currentRotationData != null && this.rotationData3 != null
         ? RotationData.distance(this.rotationData3, currentRotationData)
         : Double.POSITIVE_INFINITY;
      this.rotationData3 = currentRotationData;
      if (scaffoldPlayerSnapshot != null
         && minecraft.screen == null
         && minecraft.getOverlay() == null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && !minecraft.player.isPassenger()
         && !minecraft.player.getAbilities().flying
         && !minecraft.player.isFallFlying()
         && !minecraft.player.isAutoSpinAttack()
         && !minecraft.player.isSwimming()
         && !minecraft.player.isInWater()
         && !minecraft.player.isInLava()
         && (scaffoldPlayerSnapshot.onGround() || (!scaffoldSettings.onlyOnGround() || this.enabled) && scaffoldSettings.jumpScaffold())) {
         if (scaffoldData.activated(minecraft.options.keyUse.isDown(), minecraft.options.keyShift.isDown())
            && !ScaffoldComponent.manualItemUse(minecraft)
            && !ScaffoldComponent.manualAttack(minecraft)
            && !CompatProfileTracker.paused()) {
            RotationData nextRotationData = scaffoldSettings.clearObstacles()
               ? CompatPrepareService.prepare(minecraft, scaffoldCompatibility, scaffoldPlayerSnapshot, currentInput, this.rotationData, nextDoubleValue, doubleValue)
               : null;
            if (nextRotationData != null) {
               CompatProfileTracker.release();
               this.text2 = "Clearing obstacle";
               runnable.run();
               this.rotationData = nextRotationData;
               this.rotationData2 = null;
               this.scaffoldData62 = null;
               this.enabled6 = false;
               this.enabled4 = false;
               this.scaffoldSneakService.restart();
               ScaffoldRemapService.Input nextInput = new ScaffoldRemapService.Input(false, false, false, false, currentInput.jump(), currentInput.sneak(), false);
               this.entries2 = this.entries.remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), nextInput, false, true);
               this.updateState(minecraft, combatCompatibility);
            } else {
               boolean currentEnabled = this.enabled8;
               this.enabled8 = ScaffoldShouldActivateService.shouldActivate(
                  scaffoldSettings.towerMode(), true, currentInput, scaffoldPlayerSnapshot.horizontalSpeed(), scaffoldSettings.towerMaximumDrift()
               );
               this.scaffoldData64 = this.enabled8 ? this.scaffoldShouldActivateService.plan(scaffoldPlayerSnapshot, new ScaffoldShouldActivateService.Query() {
                  @Override
                  public boolean isReplaceable(BlockCoordinates blockCoordinates) {
                     return scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates);
                  }

                  @Override
                  public boolean isFaceSturdy(BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
                     return scaffoldCompatibility.isFaceSturdy(minecraft, blockCoordinates, scaffoldMode);
                  }
               }, nextDoubleValue, scaffoldSettings.towerFootMargin(), 4).orElse(null) : null;
               int currentValue = !this.enabled8
                     && !currentInput.hasHorizontalIntent()
                     && scaffoldPlayerSnapshot.onGround()
                     && scaffoldPlayerSnapshot.horizontalSpeed() < 0.01
                  ? 1
                  : 0;
               if (!currentInput.hasHorizontalIntent()) {
                  this.scaffoldSneakService.restart();
                  this.enabled6 = false;
               }

               if (currentValue != 0) {
                  this.scaffoldSneakService.restart();
                  if (!scaffoldSettings.keepTargetWhenStopped() || this.rotationData == null) {
                     this.suspend(minecraft, scaffoldSettings.returnSpeed(), scaffoldSettings.returnSmoothness());
                     this.text2 = "Waiting for movement";
                     return;
                  }
               }

               runnable.run();
               minecraft.player.setSprinting(false);
               this.enabled4 = false;
               if (scaffoldPlayerSnapshot.onGround() || !this.enabled || !scaffoldSettings.lockBridgeHeight()) {
                  this.count = (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().y() - 1.0E-4);
                  if (scaffoldPlayerSnapshot.onGround()) {
                     int currentX = (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().x());
                     int nextValue = (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().z());

                     for (int currentCount = this.count; currentCount >= this.count - 1; currentCount += -1) {
                        if (scaffoldCompatibility.isPlayerSupporting(minecraft, new BlockCoordinates(currentX, currentCount, nextValue))) {
                           this.count = currentCount;
                           break;
                        }
                     }
                  }

                  if (!scaffoldPlayerSnapshot.onGround()) {
                     int nextX = (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().x());
                     int previousValue = (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().z());

                     label701:
                     for (int nextCount = this.count; nextCount >= this.count - scaffoldData.rescueDepth(); nextCount += -1) {
                        for (int index = nextX - 1; index <= nextX + 1; index++) {
                           for (int currentIndex = previousValue - 1; currentIndex <= previousValue + 1; currentIndex++) {
                              if (scaffoldCompatibility.isPlayerSupporting(minecraft, new BlockCoordinates(index, nextCount, currentIndex))) {
                                 this.count = nextCount;
                                 break label701;
                              }
                           }
                        }
                     }
                  }

                  this.enabled = true;
               }

               ScaffoldRemapService.WorldVector currentWorldVector = ScaffoldRemapService.worldVector(scaffoldPlayerSnapshot.playerRotation().yaw(), currentInput.forwardAxis(), currentInput.leftAxis());
               boolean currentHasHorizontalIntent = currentInput.hasHorizontalIntent();
               this.value4 = 0.0;
               int sourceValue = this.timestamp >= this.timestamp3
                     && this.values.attempt(this.timestamp, scaffoldData.clickRhythm(), scaffoldSettings.useInterval())
                  ? 1
                  : 0;
               int targetValue = this.rotationData != null
                     && !minecraft.player.isUsingItem()
                     && (!scaffoldData.cameraAim() || ScaffoldValidateBeforeMovementValidator.sameSentRotation(this.rotationData, scaffoldPlayerSnapshot.playerRotation()))
                     && scaffoldCompatibility.canStartUseItem(minecraft)
                     && RotationObserveService.current().filter(item -> ScaffoldValidateBeforeMovementValidator.sameSentRotation(item, this.rotationData)).isPresent()
                     && this.checkCondition2(minecraft, scaffoldCompatibility, scaffoldPlayerSnapshot, scaffoldSettings, (sourceValue != 0))
                  ? 1
                  : 0;
               RotationData previousRotationData = this.rotationData;
               double previousDoubleValue = scaffoldPlayerSnapshot.onGround()
                  ? scaffoldCompatibility.groundInputAccelerationEstimate(minecraft, currentInput.forwardAxis(), currentInput.leftAxis(), false)
                  : scaffoldCompatibility.airInputAccelerationEstimate(minecraft, currentInput.forwardAxis(), currentInput.leftAxis());
               double sourceDoubleValue = currentHasHorizontalIntent ? ScaffoldPlacementHeadingService.placementHeading(scaffoldPlayerSnapshot, currentWorldVector, previousDoubleValue) : this.value;
               double targetDoubleValue = this.rotationVanillaGcdService.vanillaGcd(scaffoldPlayerSnapshot.mouseSensitivity());
               if (this.enabled8 && !scaffoldData.cameraAim()) {
                  this.enabled7 = false;
                  if (this.rotationData == null) {
                     this.rotationData = RotationObserveService.current().orElse(scaffoldPlayerSnapshot.playerRotation());
                  }

                  this.rotationData2 = ScaffoldNextService.applyMouseCounts(
                     this.rotationData,
                     0L,
                     Math.round((90.0 - this.rotationData.pitch()) / targetDoubleValue),
                     scaffoldPlayerSnapshot.mouseSensitivity()
                  );
                  this.timestamp3 = this.timestamp;
               } else if (scaffoldData.cameraAim()) {
                  this.rotationData = this.rotationData2 = scaffoldPlayerSnapshot.playerRotation();
                  this.value = sourceDoubleValue;
                  this.timestamp3 = this.timestamp;
                  this.text = scaffoldData.acquisitionProfile();
               } else if (this.rotationData2 == null
                  || currentEnabled
                  || currentHasHorizontalIntent && !this.enabled7
                  || Math.abs(RotationData.yawDelta(this.value, sourceDoubleValue)) > scaffoldData.headingTolerance()
                  || currentDoubleValue != this.value2
                  || scaffoldPlayerSnapshot.mouseSensitivity() != this.value3
                  || !scaffoldData.acquisitionProfile().equals(this.text)) {
                  int inputValue = previousRotationData != null && !currentEnabled && currentDoubleValue == this.value2 ? 0 : 1;
                  this.text = scaffoldData.acquisitionProfile();
                  this.value2 = currentDoubleValue;
                  this.value3 = scaffoldPlayerSnapshot.mouseSensitivity();
                  this.value = sourceDoubleValue;
                  this.enabled7 |= currentHasHorizontalIntent;
                  if (previousRotationData == null) {
                     this.enabled2 = false;
                  }

                  if (this.rotationData == null) {
                     this.rotationData = RotationObserveService.current().orElse(scaffoldPlayerSnapshot.playerRotation());
                  }

                  double inputDoubleValue = Math.abs(Math.sin(Math.toRadians(this.value * 2.0)))
                        > 0.99
                     ? targetDoubleValue
                     : 0.0;
                  long longValue = inputValue != 0 ? Math.round((currentDoubleValue - this.rotationData.pitch()) / targetDoubleValue) : 0L;
                  if (scaffoldData.autoPitch()) {
                     long currentLongValue = (long)Math.ceil((scaffoldData.minimumPitch() - this.rotationData.pitch()) / targetDoubleValue);
                     long nextLongValue = (long)Math.floor((scaffoldData.maximumPitch() - this.rotationData.pitch()) / targetDoubleValue);
                     if (currentLongValue <= nextLongValue) {
                        longValue = Math.max(currentLongValue, Math.min(nextLongValue, longValue));
                     }
                  }

                  this.rotationData2 = currentHasHorizontalIntent
                     ? ScaffoldNextService.applyMouseCounts(
                        this.rotationData,
                        Math.round(
                           RotationData.yawDelta(
                                 this.rotationData.yaw(),
                                 this.value
                                    + 180.0
                                    + inputDoubleValue
                                    + scaffoldData.yawOffset(this.value, scaffoldPlayerSnapshot.feetPosition())
                              )
                              / targetDoubleValue
                        ),
                        longValue,
                        scaffoldPlayerSnapshot.mouseSensitivity()
                     )
                     : this.rotationData;
                  if (previousRotationData == null) {
                     this.timestamp3 = this.timestamp + scaffoldData.startDelay();
                     this.scaffoldBudgetService.reset();
                     this.values.reset();
                     this.scaffoldSneakService.restart();
                     this.enabled6 = false;
                  }
               } else if (currentHasHorizontalIntent && Math.abs(RotationData.yawDelta(this.value, sourceDoubleValue)) > 1.0E-6
                  )
                {
                  double outputDoubleValue = RotationData.yawDelta(this.value, sourceDoubleValue);
                  this.rotationData2 = new RotationData(this.rotationData2.yaw() + outputDoubleValue, this.rotationData2.pitch());
                  this.value = sourceDoubleValue;
               }

               RotationData sourceRotationData = scaffoldData.cameraAim() && previousRotationData != null ? previousRotationData : this.rotationData;
               double resultDoubleValue = this.scaffoldBudgetService
                  .budget(
                     scaffoldData.turnStyle(),
                     this.enabled6 ? Math.min(doubleValue, scaffoldSettings.maximumClickSpeed()) : doubleValue,
                     scaffoldData.smoothness(),
                     RotationData.distance(this.rotationData, this.rotationData2),
                     targetDoubleValue
                  );
               if (targetValue == 0 && this.timestamp >= this.timestamp3 && !scaffoldData.cameraAim()) {
                  this.rotationData = ScaffoldPlacementHeadingService.turnToward(this.rotationData, this.rotationData2, scaffoldPlayerSnapshot.mouseSensitivity(), resultDoubleValue);
               }

               this.value4 = RotationData.distance(sourceRotationData, this.rotationData);
               int outputValue = this.timestamp >= this.timestamp3 && RotationData.distance(this.rotationData, this.rotationData2) <= targetDoubleValue
                  ? 1
                  : 0;
               if (scaffoldSettings.stopWhenEmpty() && scaffoldCompatibility.findPlaceableHotbarSlot(minecraft) < 0) {
                  ScaffoldRemapService.Input previousInput = new ScaffoldRemapService.Input(false, false, false, false, currentInput.jump(), currentInput.sneak(), false);
                  this.entries2 = this.entries.remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), previousInput, false, true);
                  this.enabled9 = true;
                  this.text2 = CompatProfileTracker.stock(minecraft).reason();
                  this.updateState(minecraft, combatCompatibility);
               } else {
                  ScaffoldRemapService.Result result = this.entries
                     .remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), currentInput, false, true);
                  RotationVector previousX = scaffoldPlayerSnapshot.feetPosition()
                     .add(
                        new RotationVector(
                           result.serverWorldVector().x() * scaffoldSettings.edgeSneakDistance() + scaffoldPlayerSnapshot.velocity().x() * scaffoldSettings.sneakLeadTicks(),
                           0.0,
                           result.serverWorldVector().z() * scaffoldSettings.edgeSneakDistance() + scaffoldPlayerSnapshot.velocity().z() * scaffoldSettings.sneakLeadTicks()
                        )
                     );
                  int previousCount = scaffoldPlayerSnapshot.onGround()
                        && !ScaffoldHasContinuousSupportService.hasContinuousSupport(
                           scaffoldPlayerSnapshot.feetPosition(),
                           previousX,
                           this.count,
                           0.299,
                           item -> scaffoldCompatibility.isPlayerSupporting(minecraft, item)
                        )
                     ? 1
                     : 0;
                  int resultValue = previousCount == 0 || "Eagle".equals(scaffoldSettings.sneakMode()) && !this.scaffoldSneakService.eagleDue(scaffoldData.eagleInterval()) ? 0 : 1;
                  this.enabled4 = this.scaffoldSneakService
                     .sneak(
                        this.timestamp,
                        scaffoldSettings.sneakMode(),
                        (resultValue != 0),
                        "Start".equals(scaffoldSettings.sneakMode()) ? !this.enabled6 : outputValue == 0,
                        scaffoldSettings.sneakWhileFalling()
                           && !scaffoldPlayerSnapshot.onGround()
                           && scaffoldPlayerSnapshot.velocity().y() < -0.03,
                        scaffoldSettings.sneakOnInputError() && result.angularErrorDegrees() > scaffoldSettings.maximumInputError(),
                        scaffoldSettings.sneakReleaseDelay()
                     );
                  int candidateValue = !currentInput.sneak() && !this.enabled4 ? 0 : 1;
                  if (!this.enabled8 && outputValue != 0 && !this.enabled2 && candidateValue == 0 && scaffoldData.diagonalAlignment() && !scaffoldData.cameraAim()
                     )
                   {
                     this.enabled2 = true;
                     double sourceX = scaffoldPlayerSnapshot.feetPosition().x() * Math.signum(currentWorldVector.z()) - scaffoldPlayerSnapshot.feetPosition().z() * Math.signum(currentWorldVector.x());
                     double candidateDoubleValue = sourceX - Math.floor(sourceX);
                     if (scaffoldPlayerSnapshot.onGround()
                        && Math.abs(Math.sin(Math.toRadians(this.value * 2.0)))
                           > 0.99
                        && (candidateDoubleValue < 0.2 || candidateDoubleValue > 0.8)
                        )
                      {
                        ScaffoldRemapService.Input targetX = this.entries.steerToward(scaffoldPlayerSnapshot.playerRotation().yaw(), currentWorldVector.z(), -currentWorldVector.x(), currentInput);
                        this.entries2 = this.entries
                           .remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), targetX, false, true);
                        this.updateState(minecraft, combatCompatibility);
                        return;
                     }
                  }

                  double selectedDoubleValue = scaffoldPlayerSnapshot.onGround()
                     ? scaffoldCompatibility.groundInputAccelerationEstimate(minecraft, result.serverInput().forwardAxis(), result.serverInput().leftAxis(), false)
                     : scaffoldCompatibility.airInputAccelerationEstimate(minecraft, result.serverInput().forwardAxis(), result.serverInput().leftAxis());
                  if (Double.isFinite(selectedDoubleValue) && !(selectedDoubleValue < 0.0)) {
                     double inputX = scaffoldPlayerSnapshot.feetPosition().x() + scaffoldPlayerSnapshot.velocity().x() + result.serverWorldVector().x() * selectedDoubleValue;
                     double defaultDoubleValue = scaffoldPlayerSnapshot.feetPosition().z() + scaffoldPlayerSnapshot.velocity().z() + result.serverWorldVector().z() * selectedDoubleValue;
                     RotationVector currentY = new RotationVector(inputX, scaffoldPlayerSnapshot.feetPosition().y(), defaultDoubleValue);
                     boolean currentHasContinuousSupport = ScaffoldHasContinuousSupportService.hasContinuousSupport(
                        currentY, currentY, this.count, scaffoldSettings.minimumFootOverlap(), item -> scaffoldCompatibility.isPlayerSupporting(minecraft, item)
                     );
                     RotationVector outputX = scaffoldPlayerSnapshot.eyePosition()
                        .add(new RotationVector(inputX - scaffoldPlayerSnapshot.feetPosition().x(), 0.0, defaultDoubleValue - scaffoldPlayerSnapshot.feetPosition().z()));
                     boolean nextEnabled = this.checkCondition(minecraft, scaffoldCompatibility, scaffoldSettings, outputX);
                     int selectedValue = "Edge Window".equals(scaffoldData.jumpTiming()) && (currentHasContinuousSupport || nextEnabled) ? 0 : 1;
                     boolean previousEnabled = this.scaffoldSneakService
                        .jump(
                           currentInput.jump(),
                           currentInput.sneak() || this.enabled4,
                           scaffoldPlayerSnapshot.onGround(),
                           outputValue != 0 && selectedValue != 0,
                           currentHasHorizontalIntent,
                           scaffoldSettings.autoJump(),
                           scaffoldSettings.jumpEvery()
                        );
                     int defaultValue = Math.abs(Math.sin(Math.toRadians(this.value * 2.0)))
                           > 0.1
                        ? 1
                        : 0;
                     int nextY = scaffoldPlayerSnapshot.feetPosition().y() < this.count + 1 - 0.02 ? 1 : 0;
                     int initialValue = !scaffoldPlayerSnapshot.onGround() ? 1 : 0;
                     int resolvedValue = !scaffoldPlayerSnapshot.onGround() || previousCount == 0 && currentHasContinuousSupport && candidateValue == 0 && scaffoldSettings.lockRotation() ? 0 : 1;
                     int sourceCount = scaffoldSettings.safetyBrake()
                           && scaffoldPlayerSnapshot.onGround()
                           && !previousEnabled
                           && candidateValue == 0
                           && (!this.enabled6 || !nextEnabled)
                           && !PlacementRuleEvaluator.allows(
                              scaffoldPlayerSnapshot,
                              result.serverWorldVector(),
                              this.count,
                              scaffoldSettings.minimumFootOverlap(),
                              ScaffoldCoastTicksService.coastTicks(scaffoldCompatibility.groundFriction(minecraft)),
                              selectedDoubleValue,
                              item -> scaffoldCompatibility.isPlayerSupporting(minecraft, item)
                           )
                        ? 1
                        : 0;
                     if (!this.enabled8
                        && targetValue == 0
                        && currentValue == 0
                        && !scaffoldData.cameraAim()
                        && scaffoldData.autoPitch()
                        && (defaultValue != 0 || nextY != 0 || initialValue != 0 || resolvedValue != 0 || sourceCount != 0)) {
                        double initialDoubleValue = scaffoldCompatibility.jumpVelocityEstimate(minecraft);
                        if (!Double.isFinite(initialDoubleValue) || initialDoubleValue < 0.0) {
                           initialDoubleValue = 0.0;
                        }

                        ScaffoldPlayerSnapshot currentScaffoldPlayerSnapshot = ScaffoldPlacementHeadingService.clipLanding(
                           scaffoldPlayerSnapshot,
                           ScaffoldPlacementHeadingService.nextFrame(scaffoldPlayerSnapshot, result.serverWorldVector(), selectedDoubleValue, previousEnabled, initialDoubleValue),
                           this.count,
                           item -> scaffoldCompatibility.isPlayerSupporting(minecraft, item)
                        );
                        RotationVector resultX = sourceCount != 0
                           ? scaffoldPlayerSnapshot.eyePosition().add(new RotationVector(scaffoldPlayerSnapshot.velocity().x(), 0.0, scaffoldPlayerSnapshot.velocity().z()))
                           : currentScaffoldPlayerSnapshot.eyePosition();
                        double previousY = currentScaffoldPlayerSnapshot.feetPosition().y();
                        ScaffoldPlanService.Query query = new ScaffoldPlanService.Query() {
                           @Override
                           public boolean isReplaceable(BlockCoordinates blockCoordinates) {
                              return scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates);
                           }

                           @Override
                           public boolean isFaceSturdy(BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode) {
                              return scaffoldCompatibility.isFaceSturdy(minecraft, blockCoordinates, scaffoldMode);
                           }

                           @Override
                           public double groundFriction() {
                              return scaffoldCompatibility.groundFriction(minecraft);
                           }
                        };
                        int targetCount = Math.min(this.count, (int)Math.floor(previousY - 1.0));
                        PlacementCandidate placementCandidate = this.scaffoldPlanService
                           .plan(currentScaffoldPlayerSnapshot, query, scaffoldSettings.lookAhead(), nextDoubleValue, this.rotationData, targetCount, result.serverWorldVector(), item -> true)
                           .or(
                              () -> this.scaffoldPlanService
                                 .plan(
                                    scaffoldPlayerSnapshot,
                                    query,
                                    scaffoldSettings.lookAhead(),
                                    nextDoubleValue,
                                    this.rotationData,
                                    targetCount,
                                    result.serverWorldVector(),
                                    item -> true
                                 )
                           )
                           .orElse(null);
                        if (placementCandidate == null
                           && this.scaffoldData62 != null
                           && this.scaffoldData62.targetPosition().y() == targetCount
                           && scaffoldCompatibility.isReplaceable(minecraft, this.scaffoldData62.targetPosition())
                           && scaffoldCompatibility.isFaceSturdy(minecraft, this.scaffoldData62.supportPosition(), this.scaffoldData62.clickedFace())
                           && ScaffoldPlanService.canReachFace(currentScaffoldPlayerSnapshot, this.scaffoldData62, result.serverWorldVector(), scaffoldCompatibility.groundFriction(minecraft))) {
                           placementCandidate = this.scaffoldData62;
                        }

                        if (placementCandidate != null) {
                           this.scaffoldData62 = placementCandidate;
                        }

                        if (placementCandidate != null
                           && scaffoldCompatibility.raycastPlacementFromEye(minecraft, placementCandidate, this.rotationData, nextDoubleValue, resultX)
                              .filter(item -> item.faceEdgeMargin() >= scaffoldSettings.faceMargin())
                              .isEmpty()) {
                           int[] ints = new int[]{0};
                           ScaffoldTrajectorySolver.Probe probe = (item, currentItem) -> {
                              ints[0]++;
                              return !scaffoldData.acceptsPitch(currentItem.pitch())
                                 ? Optional.empty()
                                 : scaffoldCompatibility.raycastPlacementFromEye(minecraft, item, currentItem, nextDoubleValue, resultX);
                           };
                           Optional currentResult = this.scaffoldTrajectorySolver
                              .solveKeepingYaw(
                                 this.rotationData,
                                 resultX,
                                 placementCandidate,
                                 scaffoldPlayerSnapshot.mouseSensitivity(),
                                 scaffoldSettings.faceMargin(),
                                 Math.min(32, scaffoldSettings.raycastBudget()),
                                 probe
                              );
                           int computedValue = scaffoldSettings.raycastBudget() - ints[0];
                           if (currentResult.isEmpty() && computedValue > 0) {
                              currentResult = this.scaffoldTrajectorySolver
                                 .solveNearbyYaw(
                                    this.rotationData,
                                    resultX,
                                    placementCandidate,
                                    scaffoldPlayerSnapshot.mouseSensitivity(),
                                    scaffoldSettings.faceMargin(),
                                    scaffoldSettings.searchRadius(),
                                    computedValue,
                                    probe
                                 );
                              computedValue = scaffoldSettings.raycastBudget() - ints[0];
                           }

                           if (currentResult.isEmpty() && computedValue > 0 && (!scaffoldSettings.lockRotation() || scaffoldData.rescueTurns() && (!currentHasHorizontalIntent || nextY != 0))) {
                              currentResult = this.scaffoldTrajectorySolver
                                 .solve(
                                    this.rotationData,
                                    resultX,
                                    placementCandidate,
                                    scaffoldPlayerSnapshot.mouseSensitivity(),
                                    scaffoldSettings.faceMargin(),
                                    scaffoldSettings.searchGrid(),
                                    scaffoldSettings.searchRadius(),
                                    computedValue,
                                    probe
                                 );
                           }

                           if (currentResult.isPresent()) {
                              RotationData targetRotationData = ((ScaffoldTrajectorySolver.Solution)currentResult.get()).rotation();
                              if (!scaffoldPlayerSnapshot.onGround() || targetRotationData.pitch() < 89.9) {
                                 this.rotationData2 = outputValue != 0 ? targetRotationData : new RotationData(this.rotationData2.yaw(), targetRotationData.pitch());
                                 if (resultDoubleValue > this.value4) {
                                    this.rotationData = ScaffoldPlacementHeadingService.turnToward(
                                       this.rotationData, this.rotationData2, scaffoldPlayerSnapshot.mouseSensitivity(), resultDoubleValue - this.value4
                                    );
                                 }

                                 this.value4 = RotationData.distance(sourceRotationData, this.rotationData);
                              }
                           }
                        }
                     }

                     if (!this.enabled8
                        && !scaffoldData.cameraAim()
                        && scaffoldData.autoPitch()
                        && !scaffoldSettings.lockRotation()
                        && defaultValue == 0
                        && candidateValue == 0
                        && sourceCount == 0
                        && outputValue != 0
                        && targetValue == 0
                        && scaffoldPlayerSnapshot.onGround()
                        && currentHasContinuousSupport
                        && scaffoldCompatibility.isPlayerSupporting(
                           minecraft,
                           new BlockCoordinates(
                              (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().x()), this.count, (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().z())
                           )
                        )
                        && Math.abs(this.rotationData.pitch() - currentDoubleValue) > targetDoubleValue) {
                        RotationData inputRotationData = ScaffoldNextService.applyMouseCounts(
                           this.rotationData, 0L, Math.round((currentDoubleValue - this.rotationData.pitch()) / targetDoubleValue), scaffoldPlayerSnapshot.mouseSensitivity()
                        );
                        if (scaffoldData.acceptsPitch(inputRotationData.pitch())) {
                           this.rotationData2 = inputRotationData;
                        }

                        if (resultDoubleValue > this.value4) {
                           this.rotationData = ScaffoldPlacementHeadingService.turnToward(
                              this.rotationData, this.rotationData2, scaffoldPlayerSnapshot.mouseSensitivity(), resultDoubleValue - this.value4
                           );
                        }
                     }

                     if (scaffoldSettings.safetyBrake() && scaffoldPlayerSnapshot.onGround() && !previousEnabled && candidateValue == 0) {
                        ScaffoldRemapService.Result nextResult = this.entries
                           .remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), currentInput, false, true);
                        double resolvedDoubleValue = scaffoldCompatibility.groundInputAccelerationEstimate(
                           minecraft, nextResult.serverInput().forwardAxis(), nextResult.serverInput().leftAxis(), false
                        );
                        if (!Double.isFinite(resolvedDoubleValue) || resolvedDoubleValue < 0.0) {
                           resolvedDoubleValue = 0.0;
                        }

                        RotationVector candidateX = scaffoldPlayerSnapshot.eyePosition()
                           .add(
                              new RotationVector(
                                 scaffoldPlayerSnapshot.velocity().x() + nextResult.serverWorldVector().x() * resolvedDoubleValue,
                                 0.0,
                                 scaffoldPlayerSnapshot.velocity().z() + nextResult.serverWorldVector().z() * resolvedDoubleValue
                              )
                           );
                        sourceCount |= (!this.enabled6 || !this.checkCondition(minecraft, scaffoldCompatibility, scaffoldSettings, candidateX))
                              && !PlacementRuleEvaluator.allows(
                                 scaffoldPlayerSnapshot,
                                 nextResult.serverWorldVector(),
                                 this.count,
                                 scaffoldSettings.minimumFootOverlap(),
                                 ScaffoldCoastTicksService.coastTicks(scaffoldCompatibility.groundFriction(minecraft)),
                                 resolvedDoubleValue,
                                 item -> scaffoldCompatibility.isPlayerSupporting(minecraft, item)
                              )
                           ? 1
                           : 0;
                     }

                     this.enabled9 = (sourceCount != 0);
                     int cachedValue = (outputValue != 0 || this.enabled6) && sourceCount == 0 ? 1 : 0;
                     if (currentValue != 0) {
                        this.text2 = "Waiting for movement";
                     } else if (this.timestamp < this.timestamp3) {
                        this.text2 = "Starting";
                     } else if (outputValue == 0 && !this.enabled6) {
                        this.text2 = "Aligning view";
                     } else if (sourceCount != 0) {
                        if ("Finding a face".equals(this.text2)) {
                           this.text2 = "Waiting for a reachable face";
                        }
                     } else if (this.enabled8) {
                        this.text2 = "Towering";
                     } else if (targetValue != 0 || cachedValue != 0) {
                        this.text2 = "Building";
                     }

                     ScaffoldRemapService.Input sourceInput = new ScaffoldRemapService.Input(
                        cachedValue != 0 && currentInput.forward(),
                        cachedValue != 0 && currentInput.backward(),
                        cachedValue != 0 && currentInput.left(),
                        cachedValue != 0 && currentInput.right(),
                        previousEnabled,
                        currentInput.sneak(),
                        false
                     );
                     this.entries2 = this.entries
                        .remap(scaffoldPlayerSnapshot.playerRotation().yaw(), this.rotationData.yaw(), sourceInput, this.enabled4, true);
                     this.updateState(minecraft, combatCompatibility);
                  } else {
                     this.entries2 = result;
                     this.updateState(minecraft, combatCompatibility);
                  }
               }
            }
         } else {
            this.suspend(minecraft, scaffoldSettings.returnSpeed(), scaffoldSettings.returnSmoothness());
            this.text2 = !scaffoldData.activated(minecraft.options.keyUse.isDown(), minecraft.options.keyShift.isDown())
               ? "Hold " + ("Hold Use".equals(scaffoldData.activation()) ? "use" : "sneak") + " to build"
               : "Manual input";
         }
      } else {
         this.release();
         this.text2 = "Movement unavailable";
      }
   }

   private void updateState(Minecraft minecraft, CombatCompatibility combatCompatibility) {
      combatCompatibility.applyServerRotation(minecraft, this.rotationData, CombatOwnsService.Owner.SCAFFOLD);
      PlacementOverrideBus.Override override = new PlacementOverrideBus.Override(true, this.entries2.serverInput(), this.enabled4, true, true)
         .withSilentSneak(this.enabled5, minecraft.options.keyShift.isDown());
      PlacementOverrideBus.publish(override);
      ScaffoldPublishService.publish(this.entries2.serverInput(), true);
      LocalPhysicsFrameBus.publish(new LocalPhysicsFrameBus.Frame(this.timestamp, (float)this.rotationData.yaw(), override.localInput(), true));
      minecraft.player.setSprinting(false);
   }

   private boolean checkCondition(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, ScaffoldSettings scaffoldSettings, RotationVector rotationVector) {
      int value = scaffoldCompatibility.findPlaceableHotbarSlot(minecraft);
      if (this.timestamp + 1L >= this.timestamp2
         && this.values.ready(this.timestamp + 1L)
         && scaffoldSettings.rayDwellTicks() <= 0
         && scaffoldCompatibility.canStartUseItem(minecraft)
         && value >= 0
         && (scaffoldSettings.tuning().autoSelectBlocks() || value == scaffoldCompatibility.selectedHotbarSlot(minecraft))
         && (scaffoldSettings.tuning().cameraAim() || scaffoldSettings.tuning().acceptsPitch(this.rotationData.pitch()))
         && !RotationObserveService.current().map(item -> RotationData.distance(item, this.rotationData) > scaffoldSettings.maximumClickSpeed()).orElse(true)) {
         ScaffoldBlockHit blockHitResult = scaffoldCompatibility.raycastBlockFromEye(minecraft, this.rotationData, scaffoldSettings.reach(), rotationVector).orElse(null);
         if (blockHitResult != null
            && !blockHitResult.inside()
            && !(blockHitResult.faceEdgeMargin() < scaffoldSettings.faceMargin())
            && blockHitResult.face() != ScaffoldMode.UP
            && blockHitResult.face() != ScaffoldMode.DOWN) {
            PlacementCandidate placementCandidate = scaffoldCompatibility.placementFromRay(minecraft, blockHitResult).orElse(null);
            return placementCandidate != null
               && placementCandidate.targetPosition().y() == this.count
               && scaffoldCompatibility.isReplaceable(minecraft, placementCandidate.targetPosition())
               && (placementCandidate.targetPosition().equals(blockHitResult.blockPosition()) || scaffoldCompatibility.isFaceSturdy(minecraft, blockHitResult.blockPosition(), blockHitResult.face()));
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean checkCondition2(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, ScaffoldPlayerSnapshot scaffoldPlayerSnapshot, ScaffoldSettings scaffoldSettings, boolean enabled) {
      double doubleValue = scaffoldSettings.reach();
      ScaffoldBlockHit blockHitResult = scaffoldCompatibility.raycastBlockFromEye(minecraft, this.rotationData, doubleValue, scaffoldPlayerSnapshot.eyePosition()).orElse(null);
      if (blockHitResult != null
         && !blockHitResult.inside()
         && blockHitResult.face() != ScaffoldMode.DOWN
         && !(blockHitResult.faceEdgeMargin() < scaffoldSettings.faceMargin())
         && !(this.value5 > scaffoldSettings.maximumClickSpeed())) {
         PlacementCandidate placementCandidate = scaffoldCompatibility.placementFromRay(minecraft, blockHitResult).orElse(null);
         if (placementCandidate == null) {
            return false;
         }

         BlockCoordinates blockCoordinates = placementCandidate.targetPosition();
         int currentY = this.enabled8 && this.scaffoldData64 != null
            ? this.scaffoldData64.targetPosition().y()
            : Math.min(this.count, (int)Math.floor(scaffoldPlayerSnapshot.feetPosition().y() - 1.0 + 1.0E-6));
         if (blockCoordinates.y() == currentY
            && !(blockCoordinates.y() + 1 > scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6)
            && scaffoldCompatibility.isReplaceable(minecraft, blockCoordinates)
            && (blockCoordinates.equals(blockHitResult.blockPosition()) || scaffoldCompatibility.isFaceSturdy(minecraft, blockHitResult.blockPosition(), blockHitResult.face()))) {
            double currentX = Math.max(blockCoordinates.x() - scaffoldPlayerSnapshot.feetPosition().x(), scaffoldPlayerSnapshot.feetPosition().x() - (blockCoordinates.x() + 1));
            double currentDoubleValue = Math.max(blockCoordinates.z() - scaffoldPlayerSnapshot.feetPosition().z(), scaffoldPlayerSnapshot.feetPosition().z() - (blockCoordinates.z() + 1));
            if (!(Math.max(currentX, currentDoubleValue) > scaffoldSettings.tuning().footDistance())
               && !(scaffoldPlayerSnapshot.feetPosition().y() - (blockCoordinates.y() + 1) > scaffoldSettings.tuning().rescueDrop())
               && (this.enabled8 || scaffoldSettings.tuning().cameraAim() || scaffoldSettings.tuning().acceptsPitch(this.rotationData.pitch()))) {
               if (!this.enabled8
                  || this.scaffoldData64 != null && blockCoordinates.equals(this.scaffoldData64.targetPosition()) && blockHitResult.face() == ScaffoldMode.UP) {
                  int value = scaffoldCompatibility.findPlaceableHotbarSlot(minecraft);
                  if (value >= 0 && (scaffoldSettings.tuning().autoSelectBlocks() || value == scaffoldCompatibility.selectedHotbarSlot(minecraft))) {
                     if (this.timestamp4 != this.timestamp - 1L
                        || this.scaffoldData65 == null
                        || !this.scaffoldData65.targetPosition().equals(blockCoordinates)
                        || !this.scaffoldData65.supportPosition().equals(blockHitResult.blockPosition())
                        || this.scaffoldData65.clickedFace() != blockHitResult.face()) {
                        this.count3 = 0;
                     }

                     this.scaffoldData65 = placementCandidate;
                     this.timestamp4 = this.timestamp;
                     if (++this.count3 > scaffoldSettings.rayDwellTicks() && enabled && this.timestamp >= this.timestamp2) {
                        RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
                        if (currentSnapshot != null
                           && ScaffoldValidateBeforeMovementValidator.sameSentRotation(currentSnapshot.rotation(), this.rotationData)
                           && !scaffoldCompatibility.raycastPlacementFromEye(minecraft, placementCandidate, currentSnapshot.rotation(), doubleValue, currentSnapshot.wireEyePosition()).isEmpty()) {
                           this.scaffoldData63 = placementCandidate;
                           this.count2 = -1;

                           boolean currentEnabled;
                           try {
                              currentEnabled = CompatProfileTracker.place(minecraft, scaffoldCompatibility, value, placementCandidate);
                           } finally {
                              this.scaffoldData63 = null;
                           }

                           if (this.enabled3) {
                              CoreIsInitializedHandler.LOGGER
                                 .info(
                                    "[Scaffold] use tick={} sequence={} consumed={} target={} face={} eye={} rotation={} previous={}",
                                    new Object[]{
                                       this.timestamp,
                                       this.count2,
                                       currentEnabled,
                                       blockCoordinates,
                                       blockHitResult.face(),
                                       scaffoldPlayerSnapshot.eyePosition(),
                                       this.rotationData,
                                       RotationObserveService.snapshot().orElse(null)
                                    }
                                 );
                           }

                           if (this.count2 >= 0) {
                              this.entries3.put(this.count2, placementCandidate);
                              if (this.entries3.size() > 128) {
                                 this.entries3.remove(this.entries3.keySet().iterator().next());
                              }
                           }

                           if (currentEnabled && this.count2 >= 0) {
                              this.scaffoldData6 = placementCandidate;
                              this.scaffoldSneakService.placed();
                              this.enabled6 = true;
                              this.timestamp2 = this.timestamp
                                 + ("Held Use".equals(scaffoldSettings.tuning().clickRhythm()) ? 4 : Math.max(1, scaffoldSettings.useInterval()));
                              return true;
                           } else {
                              return false;
                           }
                        } else {
                           return false;
                        }
                     } else {
                        this.text2 = this.count3 <= scaffoldSettings.rayDwellTicks() ? "Holding aim" : "Waiting for next placement";
                        return false;
                     }
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         this.scaffoldData65 = null;
         this.count3 = 0;
         this.text2 = this.value5 > scaffoldSettings.maximumClickSpeed() ? "Settling aim" : "Finding a face";
         return false;
      }
   }

   @Override
   public void observeOutgoing(ScaffoldCompatibility scaffoldCompatibility, Object value) {
      if (this.scaffoldData63 != null) {
         int currentValue = scaffoldCompatibility.placementSequence(value, this.scaffoldData63);
         if (currentValue >= 0) {
            this.count2 = currentValue;
         }
      }
   }

   @Override
   public void observeServerPacket(ScaffoldCompatibility scaffoldCompatibility, Object value) {
      int currentValue = scaffoldCompatibility.acknowledgedPlacementSequence(value);
      if (currentValue >= 0) {
         this.count4 = Math.max(this.count4, currentValue);
         this.timestamp5 = this.timestamp;
      }
   }

   @Override
   public void reconcile(Minecraft minecraft) {
      if (CombatOwnsService.owns(CombatOwnsService.Owner.SCAFFOLD)) {
         LocalPhysicsFrameBus.Frame frame = LocalPhysicsFrameBus.current().orElse(null);
         if (frame != null && minecraft.player != null) {
            Input currentInput = minecraft.player.input.keyPresses;
            ScaffoldRemapService.Input nextInput = new ScaffoldRemapService.Input(
               currentInput.forward(), currentInput.backward(), currentInput.left(), currentInput.right(), currentInput.jump(), currentInput.shift(), false
            );
            LocalPhysicsFrameBus.publishAppliedInput(frame.tick(), nextInput, true);
            ScaffoldPublishService.publish(PlacementOverrideBus.serverInput(nextInput), true);
         }
      }
   }

   @Override
   public RotationData rotation() {
      return this.rotationData;
   }

   @Override
   public ScaffoldRemapService.Result movement() {
      return this.entries2;
   }

   @Override
   public PlacementCandidate lastPlacement() {
      return this.scaffoldData6;
   }

   @Override
   public boolean braking() {
      return this.enabled9;
   }

   @Override
   public String status() {
      return this.text2;
   }

   @Override
   public void suspend(Minecraft minecraft, double doubleValue, double currentDoubleValue) {
      if (this.rotationData != null) {
         CombatRotationOverride.start(minecraft, CombatOwnsService.Owner.SCAFFOLD, doubleValue, currentDoubleValue);
      }

      this.release();
   }

   @Override
   public void release() {
      CompatProfileTracker.release();
      CompatPrepareService.clear();
      this.rotationData = this.rotationData2 = null;
      this.entries2 = null;
      this.scaffoldData62 = null;
      this.scaffoldSneakService.reset();
      this.enabled4 = false;
      this.scaffoldData65 = null;
      this.count3 = 0;
      this.values.reset();
      this.scaffoldBudgetService.reset();
      this.text = null;
      this.timestamp2 = 0L;
      this.value4 = 0.0;
      this.enabled9 = false;
      this.rotationData3 = null;
      this.enabled6 = false;
      this.enabled7 = false;
      this.enabled8 = false;
      this.scaffoldData64 = null;
      this.timestamp4 = Long.MIN_VALUE;
      this.text2 = "Waiting for movement";
      CombatOwnsService.release(CombatOwnsService.Owner.SCAFFOLD);
   }

   @Override
   public void reset(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, boolean currentEnabled) {
      this.release();
      this.enabled = false;
      this.value2 = this.value3 = Double.NaN;
      this.timestamp = this.timestamp2 = 0L;
      this.scaffoldData6 = null;
      this.entries3.clear();
      this.scaffoldData63 = null;
      this.count2 = this.count4 = -1;
      this.timestamp5 = Long.MAX_VALUE;
   }
}

