package dev.felix.ellice.compat;

import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.bow.BowCoverageCalculator;
import dev.felix.ellice.feature.bow.BowMotionTracker;
import dev.felix.ellice.feature.bow.BowObserveService;
import dev.felix.ellice.feature.bow.BowRankCalculator;
import dev.felix.ellice.feature.bow.BowTrajectorySolver;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationBoundingBox;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.friends.FriendsMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CompatContextReadyService {
  private static boolean enabled;
  private static boolean enabled2;
  private static boolean enabled3;

  private CompatContextReadyService() {}

  public static boolean contextReady(Minecraft minecraft) {
    return trackingReady(minecraft) && (minecraft.options.keyUse.isDown() || drawing(minecraft));
  }

  public static boolean trackingReady(Minecraft minecraft) {
    if (minecraft.player != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && !minecraft.isPaused()
        && minecraft.isWindowActive()
        && !SoupInventoryBridge.handBusy()
        && !CompatReleaseTracker.handBusy()
        && !PearlThrowController.reserved()
        && !CompatActivateService.handBusy()
        && !minecraft.gameMode.isDestroying()) {
      LocalPlayer localPlayer = minecraft.player;
      return localPlayer.isAlive()
          && !localPlayer.isSpectator()
          && !localPlayer.isSleeping()
          && !localPlayer.isPassenger()
          && !localPlayer.isFallFlying()
          && !localPlayer.isAutoSpinAttack()
          && !localPlayer.isSwimming()
          && !localPlayer.isInWater()
          && !localPlayer.isInLava()
          && !localPlayer.getAbilities().flying
          && localPlayer.getMainHandItem().is(Items.BOW)
          && (!localPlayer.isUsingItem() || drawing(minecraft))
          && !localPlayer.getProjectile(localPlayer.getMainHandItem()).isEmpty()
          && !localPlayer.getCooldowns().isOnCooldown(localPlayer.getMainHandItem());
    } else {
      return false;
    }
  }

  public static Optional<LivingEntity> target(
      Minecraft minecraft,
      boolean enabled,
      boolean currentEnabled,
      double doubleValue,
      double currentDoubleValue,
      UUID uUID) {
    if (!trackingReady(minecraft)) {
      return Optional.empty();
    }

    ArrayList<LivingEntity> arrayList = new ArrayList<>();
    LocalPlayer localPlayer = minecraft.player;
    RotationData rotationData = new RotationData(localPlayer.getYRot(), localPlayer.getXRot());

    for (Entity entity : minecraft.level.entitiesForRendering()) {
      if (entity instanceof LivingEntity livingEntity
          && checkCondition4(localPlayer, livingEntity)
          && (enabled && livingEntity instanceof Player
              || currentEnabled && livingEntity instanceof Mob)
          && !(livingEntity.getBoundingBox().distanceToSqr(localPlayer.getEyePosition())
              > doubleValue * doubleValue)) {
        RotationData currentRotationData =
            RotationData.lookAt(
                vector(localPlayer.getEyePosition()),
                vector(livingEntity.getBoundingBox().getCenter()));
        if (!(currentDoubleValue < 360.0)
            || !(Math.abs(RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw()))
                > currentDoubleValue * 0.5)) {
          arrayList.add(livingEntity);
        }
      }
    }

    arrayList.sort(
        Comparator.comparingDouble(
            item -> {
              if (item.getUUID().equals(uUID)) {
                return -1.0;
              }

              double distanceSquared = item.distanceToSqr(localPlayer);
              double rotationDistance =
                  RotationData.distance(
                      rotationData,
                      RotationData.lookAt(
                          vector(localPlayer.getEyePosition()),
                          vector(item.getBoundingBox().getCenter())));
              return distanceSquared + rotationDistance * 1.5;
            }));

    for (LivingEntity currentLivingEntity :
        (Iterable<LivingEntity>) (Iterable<?>) (arrayList.stream().limit(12L).toList())) {
      AABB aABB = currentLivingEntity.getBoundingBox();

      for (double nextDoubleValue : new double[] {0.55, 0.8, 0.3}) {
        Vec3 vec3 =
            new Vec3(
                (aABB.minX + aABB.maxX) * 0.5,
                aABB.minY + (aABB.maxY - aABB.minY) * nextDoubleValue,
                (aABB.minZ + aABB.maxZ) * 0.5);
        if (checkCondition2(minecraft, vector(localPlayer.getEyePosition()), vector(vec3))) {
          return Optional.of(currentLivingEntity);
        }
      }
    }

    return Optional.empty();
  }

  public static Optional<CompatContextReadyService.Prediction> predict(
      Minecraft minecraft,
      LivingEntity livingEntity,
      BowMotionTracker.Motion motion,
      int value,
      boolean enabled) {
    ArrayList arrayList = new ArrayList();
    CompatContextReadyService.CollisionCache collisionCache =
        new CompatContextReadyService.CollisionCache(minecraft, livingEntity);

    for (BowObserveService.Hypothesis hypothesis : motion.ensemble().hypotheses()) {
      arrayList.add(createForecast(minecraft, livingEntity, motion, hypothesis, collisionCache));
    }

    double doubleValue = motion.ageTicks();
    if (enabled && minecraft.getConnection() != null && !minecraft.hasSingleplayerServer()) {
      PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID());
      if (playerInfo != null) {
        doubleValue += Math.max(0.0, Math.min(4.0, playerInfo.getLatency() / 50.0));
      }
    }

    RotationVector rotationVector = launchOrigin(minecraft);
    RotationVector currentRotationVector = inheritedVelocity(minecraft);
    double currentDoubleValue = BowTrajectorySolver.speed(value);
    RotationVector nextRotationVector =
        ((CompatContextReadyService.Forecast) arrayList.getFirst()).box().sample(0.5, 0.5, 0.5);
    double nextDoubleValue =
        BowTrajectorySolver.solve(
                rotationVector,
                currentRotationVector,
                currentDoubleValue,
                item -> nextRotationVector)
            .map(BowTrajectorySolver.Solution::flightTicks)
            .orElse(20.0);
    List items =
        collectValues(
            arrayList,
            motion.ensemble(),
            nextDoubleValue,
            doubleValue,
            new RotationVector(0.0, 0.0, 0.0));
    List currentItems =
        BowRankCalculator.rank(
            rotationVector,
            currentRotationVector,
            currentDoubleValue,
            items,
            RotationPublishService.current().orElse(null));
    CompatContextReadyService.Prediction prediction = null;
    double previousDoubleValue = 0.0;

    for (BowRankCalculator.Candidate candidate :
        (Iterable<BowRankCalculator.Candidate>)
            (Iterable<?>) (currentItems.stream().limit(4L).toList())) {
      if (!(candidate.solution().flightTicks() + doubleValue >= 88.0)) {
        CompatContextReadyService.Prediction currentPrediction =
            new CompatContextReadyService.Prediction(
                livingEntity.getId(),
                livingEntity.getUUID(),
                arrayList,
                motion.ensemble(),
                candidate.solution(),
                doubleValue,
                motion.confidence());
        List nextItems =
            collectValues2(
                minecraft,
                currentPrediction,
                candidate.solution().rotation(),
                rotationVector,
                currentRotationVector,
                currentDoubleValue);
        double sourceDoubleValue =
            BowCoverageCalculator.rankCoverage(
                rotationVector,
                createRotationData7(candidate.solution().rotation()),
                currentRotationVector,
                currentDoubleValue,
                nextItems,
                (int) Math.ceil(candidate.solution().flightTicks()) + 3);
        if (sourceDoubleValue > previousDoubleValue) {
          prediction = currentPrediction;
          previousDoubleValue = sourceDoubleValue;
        }

        if (sourceDoubleValue >= candidate.coverage() - 0.005) {
          break;
        }
      }
    }

    return Optional.ofNullable(prediction);
  }

  public static boolean verifiedShot(
      Minecraft minecraft,
      CompatContextReadyService.Prediction prediction,
      RotationData rotationData,
      RotationVector rotationVector,
      double doubleValue,
      double currentDoubleValue) {
    if (contextReady(minecraft) && drawing(minecraft)) {
      if (minecraft.level.getEntity(prediction.entityId()) instanceof LivingEntity livingEntity
          && livingEntity.getUUID().equals(prediction.uuid())
          && checkCondition4(minecraft.player, livingEntity)
          && !(livingEntity.getBoundingBox().distanceToSqr(minecraft.player.getEyePosition())
              > doubleValue * doubleValue)
          && !(rotationVector.subtract(vector(minecraft.player.getEyePosition())).length()
              > 0.08)) {
        RotationVector currentRotationVector =
            rotationVector.add(new RotationVector(0.0, -0.10000000149011612, 0.0));
        RotationVector nextRotationVector = inheritedVelocity(minecraft);
        double nextDoubleValue = BowTrajectorySolver.speed(minecraft.player.getTicksUsingItem());
        List items =
            collectValues2(
                minecraft,
                prediction,
                rotationData,
                currentRotationVector,
                nextRotationVector,
                nextDoubleValue);
        return BowCoverageCalculator.coverage(
                currentRotationVector,
                createRotationData7(rotationData),
                nextRotationVector,
                nextDoubleValue,
                items,
                (int) Math.ceil(prediction.solution().flightTicks()) + 3)
            >= currentDoubleValue;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private static List<BowCoverageCalculator.Body> collectValues(
      List<CompatContextReadyService.Forecast> items,
      BowObserveService.Estimate estimate,
      double doubleValue,
      double currentDoubleValue,
      RotationVector rotationVector) {
    ArrayList arrayList = new ArrayList();

    for (BowObserveService.Weighted weighted : estimate.at(doubleValue + currentDoubleValue)) {
      CompatContextReadyService.Forecast forecast =
          items.stream()
              .filter(item -> item.model() == weighted.hypothesis().model())
              .findFirst()
              .orElseThrow();
      arrayList.add(
          new BowCoverageCalculator.Body(
              weighted.weight(),
              forecast.box(),
              item -> forecast.offset(item + currentDoubleValue).add(rotationVector),
              weighted.hypothesis().error(doubleValue + currentDoubleValue),
              doubleValue + currentDoubleValue,
              currentDoubleValue));
    }

    return arrayList;
  }

  private static List<BowCoverageCalculator.Body> collectValues2(
      Minecraft minecraft,
      CompatContextReadyService.Prediction prediction,
      RotationData rotationData,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      double doubleValue) {
    if (doubleValue < 0.3) {
      return List.of();
    }

    Entity entity = minecraft.level.getEntity(prediction.entityId());
    if (entity != null && entity.getUUID().equals(prediction.uuid())) {
      AABB aABB = createAABB(entity);
      RotationBoundingBox rotationBoundingBox = prediction.forecasts().getFirst().box();
      RotationVector nextRotationVector =
          new RotationVector(
              (aABB.minX + aABB.maxX - rotationBoundingBox.minX() - rotationBoundingBox.maxX())
                  * 0.5,
              aABB.minY - rotationBoundingBox.minY(),
              (aABB.minZ + aABB.maxZ - rotationBoundingBox.minZ() - rotationBoundingBox.maxZ())
                  * 0.5);
      if (nextRotationVector.length() > 0.75) {
        return List.of();
      }

      List items =
          collectValues(
              prediction.forecasts(),
              prediction.ensemble(),
              prediction.solution().flightTicks(),
              prediction.lead(),
              nextRotationVector);
      ArrayList arrayList = new ArrayList();

      for (BowCoverageCalculator.Body body :
          (Iterable<BowCoverageCalculator.Body>) (Iterable<?>) (items)) {
        if (body.weight() >= 0.005
            && checkCondition(
                minecraft,
                entity,
                body,
                prediction.solution().flightTicks(),
                rotationData,
                rotationVector,
                currentRotationVector,
                doubleValue)) {
          arrayList.add(body);
        }
      }

      return arrayList;
    } else {
      return List.of();
    }
  }

  private static boolean checkCondition(
      Minecraft minecraft,
      Entity entity,
      BowCoverageCalculator.Body body,
      double doubleValue,
      RotationData rotationData,
      RotationVector rotationVector,
      RotationVector currentRotationVector,
      double currentDoubleValue) {
    RotationBoundingBox rotationBoundingBox = body.box();
    double nextDoubleValue =
        Math.min(
            0.05,
            Math.min(
                    rotationBoundingBox.maxX() - rotationBoundingBox.minX(),
                    rotationBoundingBox.maxZ() - rotationBoundingBox.minZ())
                * 0.1);
    RotationBoundingBox currentRotationBoundingBox =
        new RotationBoundingBox(
            rotationBoundingBox.minX() + nextDoubleValue,
            rotationBoundingBox.minY() + nextDoubleValue,
            rotationBoundingBox.minZ() + nextDoubleValue,
            rotationBoundingBox.maxX() - nextDoubleValue,
            rotationBoundingBox.maxY() - nextDoubleValue,
            rotationBoundingBox.maxZ() - nextDoubleValue);
    RotationVector nextRotationVector =
        createRotationData7(rotationData).multiply(currentDoubleValue).add(currentRotationVector);
    RotationVector previousRotationVector = rotationVector;
    int value = Math.min(80, (int) Math.ceil(doubleValue) + 4);

    for (int index = 1; index <= value; index++) {
      RotationVector sourceRotationVector =
          BowTrajectorySolver.position(rotationVector, nextRotationVector, index);
      OptionalDouble optionalDouble =
          BowTrajectorySolver.hitFraction(
              previousRotationVector,
              sourceRotationVector,
              currentRotationBoundingBox,
              body.offset().apply(index - 1),
              body.offset().apply(index));
      RotationVector targetRotationVector =
          optionalDouble.isPresent()
              ? previousRotationVector.add(
                  sourceRotationVector
                      .subtract(previousRotationVector)
                      .multiply(optionalDouble.getAsDouble()))
              : sourceRotationVector;
      if (!checkCondition2(minecraft, previousRotationVector, targetRotationVector)
          || checkCondition3(minecraft, entity, previousRotationVector, targetRotationVector)) {
        return false;
      }

      if (optionalDouble.isPresent()) {
        return true;
      }

      previousRotationVector = sourceRotationVector;
    }

    return false;
  }

  private static boolean checkCondition2(
      Minecraft minecraft, RotationVector rotationVector, RotationVector currentRotationVector) {
    int currentLength =
        Math.max(1, (int) Math.ceil(currentRotationVector.subtract(rotationVector).length()));

    for (int index = 0; index <= currentLength; index++) {
      RotationVector nextRotationVector =
          rotationVector.add(
              currentRotationVector
                  .subtract(rotationVector)
                  .multiply((double) index / currentLength));
      if (!minecraft.level.hasChunkAt(
          BlockPos.containing(
              nextRotationVector.x(), nextRotationVector.y(), nextRotationVector.z()))) {
        return false;
      }
    }

    return minecraft
            .level
            .clip(
                new ClipContext(
                    createVec3(rotationVector),
                    createVec3(currentRotationVector),
                    Block.COLLIDER,
                    Fluid.ANY,
                    minecraft.player))
            .getType()
        == Type.MISS;
  }

  private static boolean checkCondition3(
      Minecraft minecraft,
      Entity entity,
      RotationVector rotationVector,
      RotationVector currentRotationVector) {
    double currentLength = currentRotationVector.subtract(rotationVector).length();
    if (currentLength < 1.0E-8) {
      return false;
    }

    AABB aABB =
        new AABB(createVec3(rotationVector), createVec3(currentRotationVector)).inflate(1.0);

    for (Entity currentEntity :
        minecraft.level.getEntities(
            minecraft.player,
            aABB,
            item -> item != entity && item.isAlive() && item.isPickable() && !item.isSpectator())) {
      AABB currentAABB = currentEntity.getBoundingBox().inflate(0.3);
      if (createRotationData2(currentAABB)
          .rayIntersection(
              rotationVector,
              currentRotationVector.subtract(rotationVector).multiply(1.0 / currentLength),
              currentLength)
          .isPresent()) {
        return true;
      }
    }

    return false;
  }

  private static CompatContextReadyService.Forecast createForecast(
      Minecraft minecraft,
      LivingEntity livingEntity,
      BowMotionTracker.Motion motion,
      BowObserveService.Hypothesis hypothesis,
      CompatContextReadyService.CollisionCache collisionCache) {
    AABB aABB = createAABB(livingEntity);
    List currentY =
        CompatForecastService.forecast(
            aABB,
            hypothesis.steps(),
            motion.velocity().y(),
            motion.grounded(),
            livingEntity.maxUpStep(),
            livingEntity.hurtTime > 0,
            collisionCache::collectValues3,
            item ->
                minecraft.level.hasChunkAt(BlockPos.containing(item.minX, item.minY, item.minZ)));
    return new CompatContextReadyService.Forecast(
        createRotationData2(aABB), currentY, hypothesis.model());
  }

  public static boolean drawing(Minecraft minecraft) {
    return minecraft != null
        && minecraft.player != null
        && minecraft.player.isUsingItem()
        && minecraft.player.getUsedItemHand() == InteractionHand.MAIN_HAND
        && minecraft.player.getUseItem().is(Items.BOW)
        && minecraft.player.getMainHandItem().is(Items.BOW);
  }

  public static void active(boolean currentEnabled) {
    enabled = currentEnabled;
  }

  public static RotationData useRotation() {
    Minecraft minecraft = Minecraft.getInstance();
    return enabled
            && CombatOwnsService.owns(CombatOwnsService.Owner.BOW)
            && minecraft != null
            && minecraft.player != null
            && minecraft.player.getMainHandItem().is(Items.BOW)
            && minecraft.options.keyUse.isDown()
        ? RotationPublishService.current().orElse(null)
        : null;
  }

  public static void observeAccepted(Object value) {
    if (enabled2
        && value instanceof ServerboundPlayerActionPacket serverboundPlayerActionPacket
        && serverboundPlayerActionPacket.getAction() == Action.RELEASE_USE_ITEM) {
      enabled3 = true;
    }
  }

  public static boolean release(Minecraft minecraft) {
    if (drawing(minecraft) && CompatBeginTickService.canAct()) {
      enabled3 = false;
      enabled2 = true;

      try {
        minecraft.gameMode.releaseUsingItem(minecraft.player);
      } finally {
        enabled2 = false;
      }

      CompatBeginTickService.accepted();
      return enabled3;
    } else {
      return false;
    }
  }

  public static RotationVector observedPosition(Entity entity) {
    Vec3 vec3 = entity.getPositionCodec().getBase();
    return vector(vec3.distanceToSqr(entity.position()) <= 64.0 ? vec3 : entity.position());
  }

  private static AABB createAABB(Entity entity) {
    RotationVector rotationVector = observedPosition(entity).subtract(vector(entity.position()));
    return entity.getBoundingBox().move(rotationVector.x(), rotationVector.y(), rotationVector.z());
  }

  private static RotationVector createRotationData7(RotationData rotationData) {
    float value = (float) rotationData.yaw() * 0.017453292F;
    float currentValue = (float) rotationData.pitch() * 0.017453292F;
    return new RotationVector(
            -Mth.sin(value) * Mth.cos(currentValue),
            -Mth.sin(currentValue),
            Mth.cos(value) * Mth.cos(currentValue))
        .normalized();
  }

  private static boolean checkCondition4(Player player, LivingEntity livingEntity) {
    if (FriendSyncController.excluded(FriendsMode.BOW_AIMBOT, livingEntity)) {
      return false;
    } else {
      return CompatDecisionTracker.excluded(AntibotFeatureType.BOW_AIMBOT, livingEntity)
          ? false
          : livingEntity != player
              && livingEntity.isAlive()
              && !livingEntity.isSpectator()
              && livingEntity.isPickable()
              && livingEntity.isAttackable()
              && !livingEntity.isInvisibleTo(player)
              && !livingEntity.isAlliedTo(player)
              && !player.isAlliedTo(livingEntity)
              && !(livingEntity instanceof Player currentPlayer
                  && !player.canHarmPlayer(currentPlayer));
    }
  }

  public static RotationVector launchOrigin(Minecraft minecraft) {
    return vector(minecraft.player.getEyePosition())
        .add(new RotationVector(0.0, -0.10000000149011612, 0.0));
  }

  public static RotationVector inheritedVelocity(Minecraft minecraft) {
    Vec3 vec3 = minecraft.player.getKnownMovement();
    return new RotationVector(vec3.x, minecraft.player.onGround() ? 0.0 : vec3.y, vec3.z);
  }

  public static RotationVector vector(Vec3 vec3) {
    return new RotationVector(vec3.x, vec3.y, vec3.z);
  }

  private static Vec3 createVec3(RotationVector rotationVector) {
    return new Vec3(rotationVector.x(), rotationVector.y(), rotationVector.z());
  }

  private static RotationBoundingBox createRotationData2(AABB aABB) {
    return new RotationBoundingBox(
        aABB.minX, aABB.minY, aABB.minZ, aABB.maxX, aABB.maxY, aABB.maxZ);
  }

  private static final class CollisionCache {
    private final HashMap<CompatContextReadyService.CollisionCache.Region, List<VoxelShape>>
        entries = new HashMap<>();
    private final Minecraft minecraft2;
    private final Entity entity2;

    private CollisionCache(Minecraft minecraft, Entity entity) {
      this.minecraft2 = minecraft;
      this.entity2 = entity;
    }

    private List<VoxelShape> collectValues3(AABB aABB) {
      CompatContextReadyService.CollisionCache.Region region =
          new CompatContextReadyService.CollisionCache.Region(
              Mth.floor(aABB.minX),
              Mth.floor(aABB.minY),
              Mth.floor(aABB.minZ),
              Mth.floor(aABB.maxX) + 1,
              Mth.floor(aABB.maxY) + 1,
              Mth.floor(aABB.maxZ) + 1);
      return this.entries.computeIfAbsent(
          region,
          item -> {
            ArrayList arrayList = new ArrayList();

            for (VoxelShape voxelShape :
                this.minecraft2.level.getBlockCollisions(
                    this.entity2, new AABB(item.x0, item.y0, item.z0, item.x1, item.y1, item.z1))) {
              arrayList.add(voxelShape);
            }

            return List.copyOf(arrayList);
          });
    }

    private record Region(int x0, int y0, int z0, int x1, int y1, int z1) {}
  }

  public record Forecast(
      RotationBoundingBox box, List<RotationVector> offsets, BowObserveService.Model model) {
    public Forecast(
        RotationBoundingBox box, List<RotationVector> offsets, BowObserveService.Model model) {
      offsets = List.copyOf(offsets);
      this.box = box;
      this.offsets = offsets;
      this.model = model;
    }

    public RotationVector offset(double doubleValue) {
      double currentSize = Math.max(0.0, Math.min(this.offsets.size() - 1, doubleValue));
      int value = (int) currentSize;
      int nextSize = Math.min(this.offsets.size() - 1, value + 1);
      return this.offsets
          .get(value)
          .add(
              this.offsets
                  .get(nextSize)
                  .subtract(this.offsets.get(value))
                  .multiply(currentSize - value));
    }
  }

  public record Prediction(
      int entityId,
      UUID uuid,
      List<CompatContextReadyService.Forecast> forecasts,
      BowObserveService.Estimate ensemble,
      BowTrajectorySolver.Solution solution,
      double lead,
      double confidence) {
    public Prediction(
        int entityId,
        UUID uuid,
        List<CompatContextReadyService.Forecast> forecasts,
        BowObserveService.Estimate ensemble,
        BowTrajectorySolver.Solution solution,
        double lead,
        double confidence) {
      forecasts = List.copyOf(forecasts);
      this.entityId = entityId;
      this.uuid = uuid;
      this.forecasts = forecasts;
      this.ensemble = ensemble;
      this.solution = solution;
      this.lead = lead;
      this.confidence = confidence;
    }
  }
}
