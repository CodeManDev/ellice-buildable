package dev.felix.ellice.feature.speed;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.mixin.SpeedLivingAccess;
import dev.felix.ellice.mixin.SpeedMovePacketAccess;
import dev.felix.ellice.module.impl.ImplBoolService;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class SpeedActivateService {
  private static volatile ImplBoolService implBoolService;
  private static SpeedStrafeHandler speedStrafeHandler;
  private static LocalPlayer localPlayer;
  private static Object object;
  private static String text;
  private static int count;
  private static int count2;
  private static int count3;
  private static double value = 1.0;
  private static double value2 = Double.NaN;
  private static volatile long timestamp;
  private static final ConcurrentLinkedQueue<SpeedActivateService.Incoming> concurrentLinkedQueue =
      new ConcurrentLinkedQueue<>();
  private static final ArrayDeque<SpeedActivateService.DelayedPacket> items = new ArrayDeque<>();
  private static boolean enabled;
  private static double value3;
  private static double value4;
  private static double value5;
  private static final SpeedMovingService.Actions actions = new SpeedActivateService.Actions();

  private SpeedActivateService() {}

  public static void activate(ImplBoolService implBool) {
    implBoolService = implBool;
    updateState(false);
  }

  public static void deactivate() {
    updateState(true);
    implBoolService = null;
  }

  public static void modeChanged() {
    updateState(true);
  }

  public static void worldChanged() {
    updateState(false);
  }

  private static void updateState(boolean enabled) {
    Minecraft minecraft = Minecraft.getInstance();
    if (enabled
        && speedStrafeHandler != null
        && localPlayer != null
        && minecraft.player == localPlayer
        && minecraft.level == object) {
      SpeedMovingService speedMoving = createSpeedMovingService();
      speedStrafeHandler.disable(speedMoving);
      updateState4(speedMoving);
    }

    updateState2(true);
    SpeedStateController.reset();
    timestamp++;
    concurrentLinkedQueue.clear();
    speedStrafeHandler = null;
    localPlayer = null;
    object = null;
    text = null;
    count3 = 0;
    count2 = 0;
    count = 0;
    value = 1.0;
    value2 = Double.NaN;
  }

  private static boolean checkCondition() {
    ImplBoolService implBool = implBoolService;
    if (implBool == null) {
      return false;
    }

    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer localPlayer = minecraft.player;
    return localPlayer != null
        && minecraft.level != null
        && minecraft.screen == null
        && minecraft.getOverlay() == null
        && !minecraft.isPaused()
        && minecraft.isWindowActive()
        && localPlayer.isAlive()
        && !localPlayer.isSpectator()
        && !localPlayer.isPassenger()
        && !localPlayer.isFallFlying()
        && !localPlayer.getAbilities().flying
        && !localPlayer.isInWater()
        && !localPlayer.isInLava()
        && !localPlayer.onClimbable()
        && !localPlayer.isShiftKeyDown()
        && !minecraft.options.keyShift.isDown()
        && (!(Boolean) implBool.pauseScaffold.get() || !checkCondition2());
  }

  private static boolean checkCondition2() {
    return CoreIsInitializedHandler.isReady()
        && CoreIsInitializedHandler.get()
            .modules()
            .get("ScaffoldWalk")
            .map(item -> item.isEnabled())
            .orElse(false);
  }

  private static boolean checkCondition3() {
    Minecraft minecraft = Minecraft.getInstance();
    return speedStrafeHandler != null
        && localPlayer == minecraft.player
        && object == minecraft.level
        && implBoolService != null
        && text.equals(implBoolService.mode.get())
        && checkCondition();
  }

  public static void clientTick() {
    updateState2(false);
    if (!checkCondition()) {
      if (speedStrafeHandler != null) {
        updateState(true);
      }
    } else {
      if (count3 > 0 && --count3 == 0) {
        value = 1.0;
      }

      SpeedStateController.tick();
    }
  }

  public static void beforePhysics() {
    if (!checkCondition()) {
      if (speedStrafeHandler != null) {
        updateState(true);
      }
    } else {
      Minecraft minecraft = Minecraft.getInstance();
      if (!checkCondition3()) {
        updateState(true);
        localPlayer = minecraft.player;
        object = minecraft.level;
        text = implBoolService.mode.get();
        value3 = localPlayer.getX();
        value4 = localPlayer.getZ();
        speedStrafeHandler = SpeedDefinitionService.get(text).create(implBoolService.values(text));
        SpeedMovingService speedMoving = createSpeedMovingService();
        speedStrafeHandler.enable(speedMoving);
        updateState4(speedMoving);
      }

      value5 = Math.hypot(localPlayer.getX() - value3, localPlayer.getZ() - value4);
      value3 = localPlayer.getX();
      value4 = localPlayer.getZ();
      if (localPlayer.onGround()) {
        count = 0;
        count2++;
      } else {
        count++;
        count2 = 0;
      }

      SpeedActivateService.Incoming incoming;
      while ((incoming = concurrentLinkedQueue.poll()) != null) {
        if (incoming.generation() == timestamp) {
          SpeedMovingService currentSpeedMoving = createSpeedMovingService();
          if (incoming.packet() instanceof ClientboundPlayerPositionPacket) {
            speedStrafeHandler.correction(currentSpeedMoving);
          } else if (incoming.packet()
                  instanceof ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket
              && SpeedStateController.entityId(clientboundSetEntityMotionPacket)
                  == localPlayer.getId()) {
            speedStrafeHandler.velocity(
                currentSpeedMoving,
                SpeedStateController.fallDamage(clientboundSetEntityMotionPacket));
          }

          updateState4(currentSpeedMoving);
        }
      }

      SpeedMovingService nextSpeedMoving = createSpeedMovingService();
      if ((Boolean) implBoolService.autoSprint.get()
          && nextSpeedMoving.moving()
          && !text.equals("legacy/Legit")) {
        nextSpeedMoving.sprinting = true;
      }

      if (text.equals("nextgen/BlocksMC") && nextSpeedMoving.moving()) {
        nextSpeedMoving.sprinting = true;
      }

      speedStrafeHandler.update(nextSpeedMoving);
      updateState4(nextSpeedMoving);
      SpeedMovingService previousSpeedMoving = createSpeedMovingService();
      if (speedStrafeHandler.autoJump(previousSpeedMoving)
          && !speedStrafeHandler.cancelJump(previousSpeedMoving)) {
        previousSpeedMoving.jump();
        updateState4(previousSpeedMoving);
      }
    }
  }

  public static void motion(boolean enabled) {
    if (checkCondition3()) {
      if (!enabled) {
        value5 = Math.hypot(localPlayer.getX() - value3, localPlayer.getZ() - value4);
      }

      updateState3(
          item -> {
            if (enabled) {
              speedStrafeHandler.afterMotion(item);
            } else {
              speedStrafeHandler.motion(item);
            }
          });
    }
  }

  public static float strafe(Object value, float currentValue) {
    if (value == localPlayer && checkCondition3()) {
      SpeedMovingService speedMoving = createSpeedMovingService();
      speedStrafeHandler.strafe(speedMoving);
      updateState4(speedMoving);
      if (Double.isFinite(speedMoving.strafeAcceleration)
          && speedMoving.strafeAcceleration >= 0.0) {
        return (float) speedMoving.strafeAcceleration;
      } else {
        return !localPlayer.onGround() && Double.isFinite(value2) ? (float) value2 : currentValue;
      }
    } else {
      return currentValue;
    }
  }

  public static Vec3 move(Object value, MoverType moverType, Vec3 vec3) {
    if (value == localPlayer && moverType == MoverType.SELF && checkCondition3()) {
      SpeedMovingService speedMoving = createSpeedMovingService();
      SpeedStrafeHandler.Move currentMove = new SpeedStrafeHandler.Move(vec3.x, vec3.y, vec3.z);
      speedStrafeHandler.move(speedMoving, currentMove);
      updateState4(speedMoving);
      return checkCondition4(currentMove.x, currentMove.y, currentMove.z)
          ? new Vec3(currentMove.x, currentMove.y, currentMove.z)
          : vec3;
    } else {
      return vec3;
    }
  }

  public static boolean beforeJump(Object value) {
    if (value == localPlayer && checkCondition3()) {
      SpeedMovingService speedMoving = createSpeedMovingService();
      if (speedStrafeHandler.cancelJump(speedMoving)) {
        return true;
      }

      speedStrafeHandler.beforeJump(speedMoving);
      updateState4(speedMoving);
      return false;
    } else {
      return false;
    }
  }

  public static float jumpPower(Object value, float currentValue) {
    return value == localPlayer && checkCondition3()
        ? speedStrafeHandler.jumpPower(createSpeedMovingService(), currentValue)
        : currentValue;
  }

  public static void afterJump(Object value) {
    if (value == localPlayer && checkCondition3()) {
      updateState3(item -> speedStrafeHandler.afterJump(item));
    }
  }

  public static float timerMultiplier() {
    return checkCondition3() && !Minecraft.getInstance().level.tickRateManager().isFrozen()
        ? (float) value
        : 1.0F;
  }

  public static void packet(EventAttackInputService.Packet currentPacket) {
    if (!currentPacket.isCancelled() && implBoolService != null) {
      Packet nextPacket = currentPacket.packet();
      if (currentPacket.isIncoming()) {
        if (nextPacket instanceof ClientboundPlayerPositionPacket
            || nextPacket instanceof ClientboundSetEntityMotionPacket) {
          concurrentLinkedQueue.add(new SpeedActivateService.Incoming(timestamp, nextPacket));
        }
      } else {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isSameThread() && checkCondition3()) {
          if (enabled
              || !text.equals("nextgen/SentinelDamage")
              || minecraft.hasSingleplayerServer()
              || !(nextPacket instanceof ServerboundKeepAlivePacket)
                  && !(nextPacket instanceof ServerboundPongPacket)) {
            if (nextPacket instanceof ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
              SpeedStrafeHandler.Packet currentY =
                  new SpeedStrafeHandler.Packet(
                      serverboundMovePlayerPacket.getY(localPlayer.getY()),
                      serverboundMovePlayerPacket.isOnGround(),
                      serverboundMovePlayerPacket.hasPosition());
              speedStrafeHandler.packet(createSpeedMovingService(), currentY);
              SpeedMovePacketAccess speedMovePacketAccess =
                  (SpeedMovePacketAccess) serverboundMovePlayerPacket;
              if (currentY.hasPosition && Double.isFinite(currentY.y)) {
                speedMovePacketAccess.ellice$speedY(currentY.y);
              }

              speedMovePacketAccess.ellice$speedGround(currentY.ground);
            }
          } else {
            int value = implBoolService.values(text).integer("Ping delay");
            if (value > 0) {
              items.add(
                  new SpeedActivateService.DelayedPacket(
                      nextPacket, System.currentTimeMillis() + value, minecraft.getConnection()));
              currentPacket.cancel();
            }
          }
        }
      }
    }
  }

  private static void updateState2(boolean currentEnabled) {
    ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
    long offset = System.currentTimeMillis();
    enabled = true;

    try {
      while (!items.isEmpty() && (currentEnabled || items.peek().due() <= offset)) {
        SpeedActivateService.DelayedPacket delayedPacket = items.remove();
        if (clientPacketListener != null && clientPacketListener == delayedPacket.connection()) {
          clientPacketListener.send(delayedPacket.packet());
        }
      }
    } finally {
      enabled = false;
    }
  }

  private static void updateState3(Consumer<SpeedMovingService> consumer) {
    SpeedMovingService speedMoving = createSpeedMovingService();
    consumer.accept(speedMoving);
    updateState4(speedMoving);
  }

  private static SpeedMovingService createSpeedMovingService() {
    LocalPlayer currentLocalPlayer = localPlayer;
    Minecraft minecraft = Minecraft.getInstance();
    SpeedMovingService speedMoving = new SpeedMovingService(actions);
    Vec3 vec3 = currentLocalPlayer.getDeltaMovement();
    speedMoving.x = currentLocalPlayer.getX();
    speedMoving.y = currentLocalPlayer.getY();
    speedMoving.z = currentLocalPlayer.getZ();
    speedMoving.mx = vec3.x;
    speedMoving.my = vec3.y;
    speedMoving.mz = vec3.z;
    speedMoving.yaw = LocalPhysicsFrameBus.yaw().orElseGet(currentLocalPlayer::getYRot);
    speedMoving.pitch = currentLocalPlayer.getXRot();
    Input currentInput = currentLocalPlayer.input.keyPresses;
    speedMoving.forward = (currentInput.forward() ? 1 : 0) - (currentInput.backward() ? 1 : 0);
    speedMoving.sideways = (currentInput.left() ? 1 : 0) - (currentInput.right() ? 1 : 0);
    speedMoving.jumpKey = minecraft.options.keyJump.isDown();
    speedMoving.ground = currentLocalPlayer.onGround();
    speedMoving.sprinting = currentLocalPlayer.isSprinting();
    speedMoving.tick = currentLocalPlayer.tickCount;
    speedMoving.airTicks = count;
    speedMoving.groundTicks = count2;
    speedMoving.hurtTime = currentLocalPlayer.hurtTime;
    speedMoving.fallDistance = currentLocalPlayer.fallDistance;
    speedMoving.flyDistance = currentLocalPlayer.flyDist;
    speedMoving.lastDistance = value5;
    SpeedStateController.captureEffects(currentLocalPlayer, speedMoving);
    speedMoving.verticalCollision = currentLocalPlayer.verticalCollision;
    speedMoving.horizontalCollision = currentLocalPlayer.horizontalCollision;
    speedMoving.liquid = !(!currentLocalPlayer.isInWater() && !currentLocalPlayer.isInLava());
    speedMoving.climbing = currentLocalPlayer.onClimbable();
    speedMoving.web =
        minecraft.level.getBlockState(currentLocalPlayer.blockPosition()).is(Blocks.COBWEB);
    speedMoving.carpet =
        minecraft.level.getBlockState(currentLocalPlayer.blockPosition()).getBlock()
            instanceof CarpetBlock;
    speedMoving.usingItem = currentLocalPlayer.isUsingItem();
    speedMoving.consuming =
        currentLocalPlayer.isUsingItem()
            && SpeedStateController.consuming(currentLocalPlayer.getUseItem());
    speedMoving.leatherBoots =
        currentLocalPlayer.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
    speedMoving.scaffold = checkCondition2();
    return speedMoving;
  }

  private static void updateState4(SpeedMovingService speedMoving) {
    if (localPlayer != null) {
      if (checkCondition4(speedMoving.mx, speedMoving.my, speedMoving.mz)) {
        localPlayer.setDeltaMovement(speedMoving.mx, speedMoving.my, speedMoving.mz);
      }

      if (checkCondition4(speedMoving.x, speedMoving.y, speedMoving.z)
          && (localPlayer.getX() != speedMoving.x
              || localPlayer.getY() != speedMoving.y
              || localPlayer.getZ() != speedMoving.z)) {
        localPlayer.setPos(speedMoving.x, speedMoving.y, speedMoving.z);
      }

      if (localPlayer.isSprinting() != speedMoving.sprinting) {
        localPlayer.setSprinting(speedMoving.sprinting);
      }
    }
  }

  private static boolean checkCondition4(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return Double.isFinite(doubleValue)
        && Double.isFinite(currentDoubleValue)
        && Double.isFinite(nextDoubleValue);
  }

  private static final class Actions implements SpeedMovingService.Actions {
    @Override
    public void jump(SpeedMovingService speedMoving) {
      SpeedActivateService.updateState4(speedMoving);
      SpeedActivateService.localPlayer.jumpFromGround();
      ((SpeedLivingAccess) SpeedActivateService.localPlayer).ellice$speedJumpDelay(10);
      Vec3 vec3 = SpeedActivateService.localPlayer.getDeltaMovement();
      speedMoving.mx = vec3.x;
      speedMoving.my = vec3.y;
      speedMoving.mz = vec3.z;
    }

    @Override
    public void timer(double doubleValue) {
      if (Double.isFinite(doubleValue) && !(doubleValue <= 0.0)) {
        SpeedActivateService.value = doubleValue;
        SpeedActivateService.count3 =
            SpeedActivateService.text != null && SpeedActivateService.text.startsWith("nextgen/")
                ? 2
                : 0;
      }
    }

    @Override
    public void airSpeed(double doubleValue) {
      SpeedActivateService.value2 = doubleValue;
    }

    @Override
    public boolean collision(
        double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return Minecraft.getInstance()
          .level
          .getBlockCollisions(
              SpeedActivateService.localPlayer,
              SpeedActivateService.localPlayer
                  .getBoundingBox()
                  .move(doubleValue, currentDoubleValue, nextDoubleValue))
          .iterator()
          .hasNext();
    }

    @Override
    public boolean blockAtFeet(
        double doubleValue, double currentDoubleValue, double nextDoubleValue) {
      return !Minecraft.getInstance()
          .level
          .getBlockState(
              BlockPos.containing(
                  SpeedActivateService.localPlayer.getX() + doubleValue,
                  SpeedActivateService.localPlayer.getY() + currentDoubleValue,
                  SpeedActivateService.localPlayer.getZ() + nextDoubleValue))
          .isAir();
    }

    @Override
    public boolean hasLanding() {
      Vec3 vec3 = SpeedActivateService.localPlayer.position();
      Vec3 currentVec3 = SpeedActivateService.localPlayer.getDeltaMovement();
      AABB aABB = SpeedActivateService.localPlayer.getBoundingBox();
      ClientLevel clientLevel = Minecraft.getInstance().level;

      for (int index = 0; index < 500 && vec3.y >= clientLevel.getMinY() - 2; index++) {
        AABB currentAABB = aABB.move(currentVec3);
        if (currentVec3.y < 0.0
            && clientLevel
                .getBlockCollisions(
                    SpeedActivateService.localPlayer, aABB.expandTowards(currentVec3))
                .iterator()
                .hasNext()) {
          return true;
        }

        vec3 = vec3.add(currentVec3);
        aABB = currentAABB;
        currentVec3 =
            new Vec3(currentVec3.x * 0.91, (currentVec3.y - 0.08) * 0.98, currentVec3.z * 0.91);
      }

      return false;
    }

    @Override
    public int nearbyEntities(double doubleValue) {
      return Minecraft.getInstance()
          .level
          .getEntities(
              SpeedActivateService.localPlayer,
              SpeedActivateService.localPlayer.getBoundingBox().inflate(doubleValue),
              item -> item instanceof LivingEntity && !(item instanceof ArmorStand))
          .size();
    }

    @Override
    public long millis() {
      return System.currentTimeMillis();
    }

    @Override
    public double random() {
      return SpeedActivateService.localPlayer.getRandom().nextDouble();
    }

    @Override
    public void positionPacket(SpeedMovingService speedMoving, double currentY, boolean enabled) {
      SpeedActivateService.localPlayer.connection.send(
          new Pos(
              speedMoving.x,
              speedMoving.y + currentY,
              speedMoving.z,
              enabled,
              speedMoving.horizontalCollision));
    }

    @Override
    public void fullPacket(SpeedMovingService speedMoving, boolean enabled) {
      SpeedActivateService.localPlayer.connection.send(
          new PosRot(
              speedMoving.x,
              speedMoving.y,
              speedMoving.z,
              speedMoving.yaw,
              speedMoving.pitch,
              enabled,
              speedMoving.horizontalCollision));
    }

    @Override
    public void piercingAttack(
        int value,
        int currentValue,
        boolean enabled,
        boolean currentEnabled,
        boolean nextEnabled,
        int nextValue,
        String text) {
      SpeedStateController.piercingAttack(
          value, currentValue, enabled, currentEnabled, nextEnabled, nextValue, text);
    }
  }

  private record DelayedPacket(Packet<?> packet, long due, Object connection) {}

  private record Incoming(long generation, Packet<?> packet) {}
}
