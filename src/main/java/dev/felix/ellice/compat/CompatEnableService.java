package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.tickbase.TickbaseBeginService;
import dev.felix.ellice.feature.tickbase.TickbaseCreditService;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.mixin.MinecraftAttackAccess;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.TickBaseModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public final class CompatEnableService implements TickbaseCreditService.Environment {
  private static final CompatEnableService compatEnableService = new CompatEnableService();
  private static final TickbaseCreditService tickbaseCreditService = new TickbaseCreditService();
  private static final TickbaseBeginService tickbaseBeginService = new TickbaseBeginService();
  private static TickBaseModule implProfileService3;
  private static LocalPlayer localPlayer;
  private static Object object;
  private static Object object2;
  private static long timestamp;
  private static long timestamp2;
  private static String text = "";
  private static UUID uUID;
  private static CompatEnableService.ManualFollowup manualFollowup;
  private LocalPlayer localPlayer2;
  private Object object3;
  private Object object4;
  private LivingEntity livingEntity;
  private Vec3 vec3;
  private ImplDecisionTracker implDecisionTracker;
  private ImplDecisionTracker.TickBaseWindow tickBaseWindow2;
  private long timestamp3;
  private String text2 = "Waiting for an opportunity";
  private int count;
  private String text3 = "";
  private long timestamp4;

  private CompatEnableService() {}

  private static long calculateValue() {
    return System.nanoTime() / 1000000L;
  }

  public static void enable(TickBaseModule tickBaseModule) {
    updateState2();
    tickbaseCreditService.pause(compatEnableService);
    implProfileService3 = tickBaseModule;
    compatEnableService.text3 = "";
    compatEnableService.timestamp4 = 0L;
    compatEnableService.text2 = "Waiting for an opportunity";
  }

  public static void disable(TickBaseModule tickBaseModule) {
    if (implProfileService3 == tickBaseModule) {
      tickbaseCreditService.pause(compatEnableService);
      manualFollowup = null;
      implProfileService3 = null;
    }
  }

  public static void reset() {
    tickbaseCreditService.reset(compatEnableService);
    tickbaseBeginService.begin();
    timestamp = 0L;
    uUID = null;
    manualFollowup = null;
  }

  public static void corrected() {
    updateState(1500L, "Recovering after position correction");
  }

  private static void updateState(long offset, String currentText) {
    tickbaseCreditService.pause(compatEnableService);
    manualFollowup = null;
    if (calculateValue() + offset >= timestamp) {
      timestamp = calculateValue() + offset;
      text = currentText;
    }
  }

  private static void updateState2() {
    Minecraft minecraft = Minecraft.getInstance();
    if (localPlayer != minecraft.player
        || object != minecraft.level
        || object2 != minecraft.getConnection()) {
      reset();
      localPlayer = minecraft.player;
      object = minecraft.level;
      object2 = minecraft.getConnection();
    }
  }

  public static void beginClientTick() {
    updateState2();
    tickbaseBeginService.begin();
  }

  public static void watchdog() {
    if (implProfileService3 != null && !compatEnableService.valid()) {
      tickbaseCreditService.pause(compatEnableService);
      manualFollowup = null;
    }
  }

  public static void attackInput() {
    if (!tickbaseCreditService.replaying() && manualFollowup != null) {
      CompatEnableService.ManualFollowup currentManualFollowup = manualFollowup;
      manualFollowup = null;
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == currentManualFollowup.player()
          && minecraft.level == currentManualFollowup.world()
          && compatEnableService.valid()
          && minecraft.level.getEntity(currentManualFollowup.target().getId())
              == currentManualFollowup.target()
          && compatEnableService.checkCondition3(currentManualFollowup.target())) {
        updateState4(minecraft, currentManualFollowup.player(), currentManualFollowup.target());
      }
    }
  }

  public static void accepted(Packet<?> packet) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.isSameThread() && minecraft.getConnection() != null) {
      if (packet instanceof ServerboundClientTickEndPacket) {
        timestamp2++;
      } else if (!(packet instanceof ServerboundKeepAlivePacket)
          && !(packet instanceof ServerboundPongPacket)) {
        TickbaseBeginService.Skip skip = tickbaseBeginService.activity();
        if (skip != TickbaseBeginService.Skip.NONE) {
          tickbaseCreditService.rejectSkippedTick(
              skip == TickbaseBeginService.Skip.REPAY, compatEnableService);
        }
      }
    }
  }

  public static boolean suppressTickEnd(Connection connection, Packet<?> packet) {
    Minecraft minecraft = Minecraft.getInstance();
    return packet instanceof ServerboundClientTickEndPacket
        && minecraft.isSameThread()
        && minecraft.getConnection() != null
        && connection == minecraft.getConnection().getConnection()
        && tickbaseBeginService.suppressEnd();
  }

  public static void beforeIncoming(Packet<?> packet, PacketListener packetListener) {
    Minecraft minecraft = Minecraft.getInstance();
    if (implProfileService3 != null
        && minecraft.isSameThread()
        && packetListener == minecraft.getConnection()) {
      if (packet instanceof BundlePacket bundlePacket) {
        for (Packet currentPacket : (Iterable<Packet>) (Iterable<?>) (bundlePacket.subPackets())) {
          beforeIncoming(currentPacket, packetListener);
        }
      } else if (packet instanceof ClientboundPlayerPositionPacket
          || packet instanceof ClientboundPlayerRotationPacket) {
        corrected();
      } else if (minecraft.player != null
          && (packet instanceof ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket
                  && EntityMotionPacketAccess.entityId(clientboundSetEntityMotionPacket)
                      == minecraft.player.getId()
              || packet instanceof ClientboundDamageEventPacket clientboundDamageEventPacket
                  && clientboundDamageEventPacket.entityId() == minecraft.player.getId()
              || packet instanceof ClientboundExplodePacket)) {
        updateState(250L, "Recovering after server impulse");
      }
    }
  }

  public static String status(TickBaseModule tickBaseModule) {
    if (implProfileService3 != tickBaseModule) {
      return "Disabled";
    }

    if (calculateValue() < timestamp) {
      return text;
    }

    if (!compatEnableService.valid()) {
      return compatEnableService.text2;
    }

    if (!tickbaseCreditService.status().equals("Ready")) {
      return tickbaseCreditService.status();
    }

    long longValue = tickbaseCreditService.cooldownRemaining(calculateValue());
    return longValue > 0L
        ? "Cooldown " + (longValue + 99L) / 100L / 10.0 + "s"
        : compatEnableService.text2;
  }

  public static String lastResult() {
    return calculateValue() - compatEnableService.timestamp4 < 3000L
        ? compatEnableService.text3
        : "";
  }

  public static String compactStatus(TickBaseModule tickBaseModule) {
    String text = status(tickBaseModule);
    if (text.startsWith("Cooldown")) {
      return lastResult().isEmpty() ? text : compatEnableService.text3 + " · " + text;
    } else if (text.equals("Waiting for KillAura target / click window")) {
      return "Waiting for KillAura";
    } else if (text.startsWith("No reachable")) {
      return "Waiting for approach";
    } else if (text.equals("Paused by movement / manual interaction")) {
      return "Paused";
    } else {
      return text.startsWith("Paused while ")
          ? text.replace("Paused while ", "Paused: ").replace(" is active", "")
          : text;
    }
  }

  public static boolean coordinatesBacktrack(UUID currentUUID) {
    return implProfileService3 != null
        && currentUUID.equals(uUID)
        && (tickbaseCreditService.replaying()
            || tickbaseCreditService.debt() > 0
            || tickbaseCreditService.credit() > 0);
  }

  public static int lastBurstTicks() {
    return compatEnableService.count;
  }

  public static long skippedTicks() {
    return tickbaseCreditService.skipped();
  }

  public static long extraTicks() {
    return tickbaseCreditService.executed();
  }

  public static int repaymentTicks() {
    return tickbaseCreditService.debt();
  }

  public static boolean replaying() {
    return tickbaseCreditService.replaying();
  }

  public static boolean beforeTick() {
    if (implProfileService3 == null
        || calculateValue() < timestamp
        || tickbaseCreditService.replaying()) {
      return false;
    }

    if (!tickbaseBeginService.canSkip()) {
      if (tickbaseCreditService.credit() > 0) {
        tickbaseCreditService.pause(compatEnableService);
      }

      return false;
    } else {
      int value = tickbaseCreditService.debt() > 0 ? 1 : 0;
      if (!tickbaseCreditService.beforeTick(
          calculateValue(),
          implProfileService3.profile().ticks(),
          implProfileService3.profile().timing(),
          compatEnableService)) {
        return false;
      }

      tickbaseBeginService.skipped((value != 0));
      return true;
    }
  }

  public static void afterTick() {
    if (implProfileService3 != null
        && calculateValue() >= timestamp
        && !tickbaseCreditService.replaying()) {
      long longValue = tickbaseCreditService.executed();
      tickbaseCreditService.afterTick(
          calculateValue(),
          implProfileService3.profile().cooldown(),
          implProfileService3.profile().ticks(),
          implProfileService3.profile().timing(),
          compatEnableService);
      if (tickbaseCreditService.executed() > longValue) {
        compatEnableService.text3 =
            "Shifted " + (tickbaseCreditService.executed() - longValue) + " ticks";
        compatEnableService.timestamp4 = calculateValue();
      }
    }
  }

  @Override
  public boolean valid() {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer localPlayer = minecraft.player;

    for (String id : new String[] {"Speed", "ScaffoldWalk", "Blink", "Pulse", "AutoRod"}) {
      if (checkCondition(id)) {
        this.text2 = "Paused while " + id + " is active";
        return false;
      }
    }

    if (implProfileService3 != null
        && localPlayer != null
        && minecraft.level != null
        && minecraft.gameMode != null
        && minecraft.getConnection() != null
        && localPlayer.isAlive()
        && !localPlayer.isSpectator()
        && !localPlayer.isPassenger()
        && !localPlayer.isSleeping()
        && !localPlayer.isUsingItem()
        && !localPlayer.isInWater()
        && !localPlayer.isInLava()
        && !localPlayer.onClimbable()
        && !localPlayer.getAbilities().flying
        && !localPlayer.isFallFlying()
        && !localPlayer.isAutoSpinAttack()
        && localPlayer.hurtTime <= 0
        && calculateValue() >= timestamp
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && !minecraft.isPaused()
        && minecraft.isWindowActive()
        && !minecraft.level.tickRateManager().isFrozen()
        && !minecraft.gameMode.isDestroying()
        && !minecraft.options.keyUse.isDown()
        && !minecraft.options.keyShift.isDown()
        && localPlayer.containerMenu == localPlayer.inventoryMenu
        && localPlayer.inventoryMenu.getCarried().isEmpty()
        && !CompatReleaseTracker.reserved()
        && !SoupInventoryBridge.handBusy()
        && !CompatActivateService.handBusy()
        && !CompatOptionsTracker.handBusy()
        && !PearlThrowController.reserved()
        && !CompatCanStartService.holding(minecraft)
        && !CompatCanStartService.pendingRelease(minecraft)) {
      return this.localPlayer2 == null
          || localPlayer == this.localPlayer2
              && minecraft.level == this.object3
              && minecraft.getConnection() == this.object4;
    }

    this.text2 = "Paused by movement / manual interaction";
    return false;
  }

  private static boolean checkCondition(String text) {
    return CoreIsInitializedHandler.isReady()
        && CoreIsInitializedHandler.get()
            .modules()
            .get(text)
            .map(item -> item.isEnabled())
            .orElse(false);
  }

  private Vec3 createVec3() {
    Minecraft minecraft = Minecraft.getInstance();
    Options currentOptions = minecraft.options;
    LocalPhysicsFrameBus.Frame frame = LocalPhysicsFrameBus.current().orElse(null);
    if (implProfileService3.profile().syncAura() && frame != null) {
      ScaffoldRemapService.Input currentInput = frame.input();
      ScaffoldRemapService.WorldVector currentWorldVector =
          ScaffoldRemapService.worldVector(
              frame.yaw(), currentInput.forwardAxis(), currentInput.leftAxis());
      return new Vec3(currentWorldVector.x(), 0.0, currentWorldVector.z());
    } else {
      int value =
          (currentOptions.keyUp.isDown() ? 1 : 0) - (currentOptions.keyDown.isDown() ? 1 : 0);
      int currentValue =
          (currentOptions.keyLeft.isDown() ? 1 : 0) - (currentOptions.keyRight.isDown() ? 1 : 0);
      Vec3 vec3 = Vec3.directionFromRotation(0.0F, minecraft.player.getYRot());
      return vec3.scale(value).add(vec3.z * currentValue, 0.0, -vec3.x * currentValue).normalize();
    }
  }

  private boolean checkCondition2() {
    Minecraft minecraft = Minecraft.getInstance();
    return !implProfileService3.profile().combatOnly()
        || minecraft.options.keyAttack.isDown()
        || CoreIsInitializedHandler.get()
            .modules()
            .get(ImplDecisionTracker.class)
            .filter(Module::isEnabled)
            .flatMap(ImplDecisionTracker::targetUuid)
            .isPresent();
  }

  @Override
  public int plan(int value) {
    if (!this.valid()) {
      return 0;
    }

    this.implDecisionTracker = null;
    this.tickBaseWindow2 = null;
    if (implProfileService3.profile().syncAura()) {
      this.implDecisionTracker =
          CoreIsInitializedHandler.get()
              .modules()
              .get(ImplDecisionTracker.class)
              .filter(Module::isEnabled)
              .orElse(null);
      this.tickBaseWindow2 =
          this.implDecisionTracker == null
              ? null
              : this.implDecisionTracker.tickBaseWindow().orElse(null);
      if (this.tickBaseWindow2 == null) {
        this.text2 = "Waiting for KillAura target / click window";
        return 0;
      }
    } else if (!this.checkCondition2()) {
      this.text2 = "Waiting for combat input";
      return 0;
    }

    Minecraft minecraft = Minecraft.getInstance();
    Vec3 currentVec3 = this.createVec3();
    if (currentVec3.lengthSqr() < 0.01
        && minecraft.player.getDeltaMovement().horizontalDistance() < 0.01) {
      this.text2 = "No useful approach";
      return 0;
    }

    this.localPlayer2 = minecraft.player;
    this.object3 = minecraft.level;
    this.object4 = minecraft.getConnection();
    this.vec3 = currentVec3;
    ArrayList<LivingEntity> arrayList = new ArrayList<>();

    for (Entity entity : minecraft.level.entitiesForRendering()) {
      if (entity instanceof LivingEntity currentLivingEntity
          && this.checkCondition3(currentLivingEntity)
          && (this.tickBaseWindow2 == null
              || currentLivingEntity.getUUID().equals(this.tickBaseWindow2.target()))) {
        arrayList.add(currentLivingEntity);
      }
    }

    arrayList.sort(Comparator.comparingDouble(this.localPlayer2::distanceToSqr));
    List items = this.collectValues(value);
    Vec3 nextVec3 = this.localPlayer2.getEyePosition();
    double doubleValue =
        this.tickBaseWindow2 == null
            ? this.localPlayer2.entityInteractionRange()
            : this.tickBaseWindow2.reach();

    for (LivingEntity nextLivingEntity : (Iterable<LivingEntity>) (Iterable<?>) (arrayList)) {
      double currentDoubleValue = this.calculateValue2(nextVec3, nextLivingEntity);
      if (!(currentDoubleValue > implProfileService3.profile().range())) {
        int index = 0;

        for (int currentIndex = 0; currentIndex < items.size(); currentIndex++) {
          int currentValue = currentIndex + 1;
          CompatEnableService.Prediction prediction =
              (CompatEnableService.Prediction) items.get(currentIndex);
          double nextDoubleValue = this.calculateValue2(prediction.eye(), nextLivingEntity);
          if (!(nextDoubleValue >= currentDoubleValue - 0.04)
              && !(nextDoubleValue > doubleValue - 0.04)
              && this.checkCondition4(prediction.eye(), nextLivingEntity)) {
            index++;
            int nextValue =
                implProfileService3.profile().timing() == TickbaseCreditService.Mode.FUTURE
                    ? 0
                    : currentValue;
            if ((this.tickBaseWindow2 == null
                    || index >= this.tickBaseWindow2.stableTicks()
                        && this.implDecisionTracker.tickBaseWeaponReady(nextValue))
                && (this.tickBaseWindow2 != null
                    || !(this.localPlayer2.getAttackStrengthScale(nextValue) < 1.0F))) {
              RotationData rotationData =
                  RotationObserveService.current()
                      .orElse(
                          new RotationData(
                              this.localPlayer2.getYRot(), this.localPlayer2.getXRot()));
              Vec3 previousVec3 = nextLivingEntity.getBoundingBox().getCenter();
              RotationData currentRotationData =
                  RotationData.lookAt(
                      new RotationVector(
                          prediction.eye().x, prediction.eye().y, prediction.eye().z),
                      new RotationVector(previousVec3.x, previousVec3.y, previousVec3.z));
              if (this.tickBaseWindow2 == null
                  || !(RotationData.distance(rotationData, currentRotationData)
                      > Math.max(5.0, this.tickBaseWindow2.turnSpeed() * 0.5) * currentValue)) {
                this.livingEntity = nextLivingEntity;
                uUID = nextLivingEntity.getUUID();
                this.timestamp3 =
                    this.implDecisionTracker == null
                        ? 0L
                        : this.implDecisionTracker.issuedAttacks();
                this.count = currentValue;
                this.text2 =
                    "Planned "
                        + currentValue
                        + " ticks for "
                        + nextLivingEntity.getName().getString();
                return currentValue;
              }
            }
          } else {
            index = 0;
          }
        }
      }
    }

    this.text2 = "No reachable attack window within " + value + " ticks";
    this.forget();
    return 0;
  }

  private boolean checkCondition3(LivingEntity livingEntity) {
    if (FriendSyncController.excluded(FriendsMode.TICK_BASE, livingEntity)) {
      return false;
    } else if (CompatDecisionTracker.excluded(AntibotFeatureType.TICK_BASE, livingEntity)) {
      return false;
    } else {
      TickBaseModule.Profile currentProfile = implProfileService3.profile();
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer localPlayer = minecraft.player;
      if (livingEntity != localPlayer
          && livingEntity.isAlive()
          && livingEntity.isPickable()
          && livingEntity.isAttackable()
          && !livingEntity.isSpectator()
          && !livingEntity.isInvisibleTo(localPlayer)
          && !livingEntity.isAlliedTo(localPlayer)
          && !localPlayer.isAlliedTo(livingEntity)
          && !(livingEntity instanceof Player currentPlayer
              && (currentPlayer.getAbilities().instabuild
                  || !localPlayer.canHarmPlayer(currentPlayer)))
          && (currentProfile.syncAura()
              || currentProfile.players() && livingEntity instanceof Player
              || currentProfile.mobs() && livingEntity instanceof Mob)
          && localPlayer.hasLineOfSight(livingEntity)) {
        String text = livingEntity.getName().getString().toLowerCase(Locale.ROOT);
        String currentText = livingEntity.getUUID().toString();
        return Arrays.stream(currentProfile.excluded().toLowerCase(Locale.ROOT).split("[,;\\s]+"))
            .noneMatch(item -> !item.isEmpty() && (item.equals(text) || item.equals(currentText)));
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean continueShift() {
    if (this.valid()
        && this.livingEntity != null
        && this.checkCondition3(this.livingEntity)
        && Minecraft.getInstance().level.getEntity(this.livingEntity.getId())
            == this.livingEntity) {
      if (this.implDecisionTracker != null) {
        if (this.implDecisionTracker.issuedAttacks() != this.timestamp3
            || !this.implDecisionTracker.isEnabled()
            || this.implDecisionTracker
                .targetUuid()
                .filter(this.livingEntity.getUUID()::equals)
                .isEmpty()) {
          return false;
        }

        if (this.implDecisionTracker.tickBaseWindow().isEmpty()) {
          return false;
        }
      } else if (!this.checkCondition2() || this.createVec3().dot(this.vec3) < 0.8) {
        return false;
      }

      double doubleValue =
          this.calculateValue2(this.localPlayer2.getEyePosition(), this.livingEntity);
      if (doubleValue > implProfileService3.profile().range() || doubleValue < 0.65) {
        return false;
      }

      if (this.implDecisionTracker == null
          && doubleValue <= this.localPlayer2.entityInteractionRange()) {
        return false;
      }

      List items = this.collectValues(1);
      return !items.isEmpty()
          && this.calculateValue2(
                  ((CompatEnableService.Prediction) items.getFirst()).eye(), this.livingEntity)
              <= doubleValue + 0.03;
    } else {
      return false;
    }
  }

  private List<CompatEnableService.Prediction> collectValues(int value) {
    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel clientLevel = minecraft.level;
    AABB aABB = this.localPlayer2.getBoundingBox();
    Vec3 vec3 = this.localPlayer2.getDeltaMovement();
    Vec3 currentVec3 = this.createVec3();
    boolean onGround = this.localPlayer2.onGround();
    double doubleValue = this.localPlayer2.getEyeHeight();
    ArrayList arrayList = new ArrayList();

    for (int index = 0; index < value; index++) {
      Vec3 nextVec3 = aABB.getCenter();
      float nextValue =
          clientLevel
              .getBlockState(BlockPos.containing(nextVec3.x, aABB.minY - 0.5, nextVec3.z))
              .getBlock()
              .getFriction();
      double currentDoubleValue =
          onGround
              ? this.localPlayer2.getSpeed() * 0.21600002 / (nextValue * nextValue * nextValue)
              : (this.localPlayer2.isSprinting() ? 0.026 : 0.02);
      double nextDoubleValue = onGround ? nextValue * 0.91 : 0.91;
      if (onGround && minecraft.options.keyJump.isDown() && index == 0) {
        vec3 = new Vec3(vec3.x, 0.42 + this.localPlayer2.getJumpBoostPower(), vec3.z);
      }

      Vec3 previousVec3 = vec3.add(currentVec3.scale(currentDoubleValue * 0.98));
      Vec3 sourceVec3 =
          Entity.collideBoundingBox(this.localPlayer2, previousVec3, aABB, clientLevel, List.of());
      if (Math.abs(sourceVec3.x - previousVec3.x) > 1.0E-5
          || Math.abs(sourceVec3.z - previousVec3.z) > 1.0E-5) {
        break;
      }

      int previousValue =
          previousVec3.y < 0.0 && Math.abs(sourceVec3.y - previousVec3.y) > 1.0E-5 ? 1 : 0;
      if (onGround && previousValue == 0 && !minecraft.options.keyJump.isDown()) {
        break;
      }

      aABB = aABB.move(sourceVec3);
      onGround = previousValue != 0;
      if (!clientLevel.getWorldBorder().isWithinBounds(aABB)
          || !clientLevel.hasChunkAt(BlockPos.containing(aABB.getCenter()))) {
        break;
      }

      byte byteValue = 0;

      for (BlockPos blockPos :
          BlockPos.betweenClosed(
              BlockPos.containing(aABB.minX, aABB.minY, aABB.minZ),
              BlockPos.containing(aABB.maxX, aABB.maxY, aABB.maxZ))) {
        if (!clientLevel.getFluidState(blockPos).isEmpty()) {
          byteValue = 1;
          break;
        }
      }

      if (byteValue != 0) {
        break;
      }

      arrayList.add(
          new CompatEnableService.Prediction(
              new Vec3(aABB.getCenter().x, aABB.minY + doubleValue, aABB.getCenter().z)));
      double previousDoubleValue =
          Math.abs(sourceVec3.y - previousVec3.y) > 1.0E-5 ? 0.0 : previousVec3.y;
      vec3 =
          new Vec3(
              sourceVec3.x * nextDoubleValue,
              (previousDoubleValue - this.localPlayer2.getGravity()) * 0.98,
              sourceVec3.z * nextDoubleValue);
    }

    return arrayList;
  }

  private boolean checkCondition4(Vec3 vec3, LivingEntity livingEntity) {
    AABB aABB =
        CompatEnableHandler.receivedBox(livingEntity.getUUID())
            .orElse(livingEntity.getBoundingBox());
    return this.checkCondition5(vec3, livingEntity.getBoundingBox().getCenter())
        && this.checkCondition5(vec3, aABB.getCenter());
  }

  private boolean checkCondition5(Vec3 vec3, Vec3 currentVec3) {
    return Minecraft.getInstance()
            .level
            .clip(new ClipContext(vec3, currentVec3, Block.COLLIDER, Fluid.NONE, this.localPlayer2))
            .getType()
        == Type.MISS;
  }

  private double calculateValue2(Vec3 vec3, LivingEntity livingEntity) {
    AABB aABB =
        CompatEnableHandler.receivedBox(livingEntity.getUUID())
            .orElse(livingEntity.getBoundingBox());
    return Math.max(
        calculateValue3(vec3, livingEntity.getBoundingBox()), calculateValue3(vec3, aABB));
  }

  private static double calculateValue3(Vec3 vec3, AABB aABB) {
    double doubleValue = vec3.x - Math.clamp(vec3.x, aABB.minX, aABB.maxX);
    double currentDoubleValue = vec3.y - Math.clamp(vec3.y, aABB.minY, aABB.maxY);
    double nextDoubleValue = vec3.z - Math.clamp(vec3.z, aABB.minZ, aABB.maxZ);
    return Math.sqrt(
        doubleValue * doubleValue
            + currentDoubleValue * currentDoubleValue
            + nextDoubleValue * nextDoubleValue);
  }

  @Override
  public void beginShift() {
    CompatEnableHandler.flushForTickShift(uUID);
  }

  @Override
  public boolean extraTick() {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer localPlayer = this.localPlayer2;
    ClientLevel clientLevel = minecraft.level;
    long longValue = timestamp2;
    minecraft.getConnection().send(ServerboundClientTickEndPacket.INSTANCE);
    if (longValue == timestamp2) {
      return false;
    }

    this.updateState3(minecraft);
    if (minecraft.player == localPlayer && minecraft.level == clientLevel) {
      clientLevel.tickNonPassenger(localPlayer);
      if (this.localPlayer2 == localPlayer
          && this.implDecisionTracker == null
          && this.livingEntity != null
          && this.calculateValue2(localPlayer.getEyePosition(), this.livingEntity)
              <= localPlayer.entityInteractionRange()) {
        manualFollowup =
            new CompatEnableService.ManualFollowup(localPlayer, clientLevel, this.livingEntity);
      }

      return true;
    } else {
      return false;
    }
  }

  private void updateState3(Minecraft minecraft) {
    if (this.localPlayer2 != null && this.livingEntity != null && this.valid()) {
      if (this.implDecisionTracker != null) {
        if (this.calculateValue2(this.localPlayer2.getEyePosition(), this.livingEntity)
                < this.tickBaseWindow2.reach()
            && this.checkCondition4(this.localPlayer2.getEyePosition(), this.livingEntity)) {
          this.implDecisionTracker.tickBaseAttackPhase(this.livingEntity.getUUID());
        }
      } else {
        CoreIsInitializedHandler.get()
            .bus()
            .post(
                EventAttackInputService.ATTACK_INPUT, EventAttackInputService.AttackInput.INSTANCE);
        if (this.localPlayer2 != null && this.livingEntity != null && this.valid()) {
          updateState4(minecraft, this.localPlayer2, this.livingEntity);
        }
      }
    }
  }

  private static void updateState4(
      Minecraft minecraft, LocalPlayer localPlayer, LivingEntity livingEntity) {
    if (minecraft.options.keyAttack.isDown()
        && !(localPlayer.getAttackStrengthScale(0.0F) < 1.0F)) {
      RotationObserveService.Snapshot currentSnapshot =
          RotationObserveService.snapshot().orElse(null);
      if (currentSnapshot != null) {
        EntityHitResult entityHitResult =
            CompatAvailableService.pick(
                    minecraft,
                    currentSnapshot.wireEyePosition(),
                    currentSnapshot.rotation(),
                    localPlayer.entityInteractionRange())
                .orElse(null);
        if (entityHitResult != null && entityHitResult.getEntity() == livingEntity) {
          HitResult currentHitResult = minecraft.hitResult;
          minecraft.hitResult = entityHitResult;

          try {
            ScaffoldForActorService.withRotation(
                localPlayer,
                currentSnapshot.rotation(),
                () -> ((MinecraftAttackAccess) minecraft).ellice$startAttack());
          } finally {
            minecraft.hitResult = currentHitResult;
          }
        }
      }
    }
  }

  @Override
  public void forget() {
    this.localPlayer2 = null;
    this.object3 = this.object4 = null;
    this.livingEntity = null;
    this.vec3 = null;
    this.implDecisionTracker = null;
    this.tickBaseWindow2 = null;
  }

  private record ManualFollowup(LocalPlayer player, Object world, LivingEntity target) {}

  private record Prediction(Vec3 eye) {}
}
