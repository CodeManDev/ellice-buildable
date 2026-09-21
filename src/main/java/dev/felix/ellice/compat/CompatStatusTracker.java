package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.pulse.PulseDelayQueue;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationVector;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.PulseModule;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.AABB;

public final class CompatStatusTracker {
   private static final PulseDelayQueue<CompatStatusTracker.Pending> pulseDelayQueue = new PulseDelayQueue<>(6);
   private static PulseModule implProfileService2;
   private static ClientLevel client2;
   private static LocalPlayer localPlayer;
   private static Connection connection;
   private static PulseModule.Profile profile2;
   private static long timestamp;
   private static long timestamp2;
   private static long timestamp3;
   private static long timestamp4;
   private static long timestamp5;
   private static boolean enabled;
   private static boolean enabled2;
   private static String text = "Inactive";

   private CompatStatusTracker() {
   }

   public static void enable(PulseModule pulseModule) {
      worldChanged();
      implProfileService2 = pulseModule;
      profile2 = pulseModule.profile();
      timestamp = timestamp2 = System.nanoTime();
      timestamp5 = 0L;
      timestamp4 = 0L;
      timestamp3 = 0L;
      text = "Waiting for movement";
   }

   public static void disable(PulseModule pulseModule) {
      if (implProfileService2 == pulseModule) {
         updateState();
         implProfileService2 = null;
         text = "Inactive";
      }
   }

   public static void worldChanged() {
      timestamp5 = timestamp5 + pulseDelayQueue.size();
      pulseDelayQueue.clear();
      client2 = null;
      localPlayer = null;
      connection = null;
   }

   public static void corrected() {
      if (implProfileService2 != null) {
         timestamp5 = timestamp5 + pulseDelayQueue.size();
         pulseDelayQueue.clear();
         timestamp2 = timestamp = System.nanoTime() + 1500000000L;
         text = "Recovering after correction";
      }
   }

   public static void beforeIncoming(Packet<?> packet, PacketListener packetListener) {
      Minecraft minecraft = Minecraft.getInstance();
      if (implProfileService2 != null && minecraft.isSameThread() && packetListener == minecraft.getConnection()) {
         if (packet instanceof BundlePacket bundlePacket) {
            for (Packet currentPacket : (Iterable<Packet>) (Iterable<?>) (bundlePacket.subPackets())) {
               beforeIncoming(currentPacket, packetListener);
            }
         } else if (packet instanceof ClientboundPlayerPositionPacket
            || packet instanceof ClientboundPlayerRotationPacket
            || packet instanceof ClientboundRespawnPacket
            || packet instanceof ClientboundLoginPacket
            || packet instanceof ClientboundStartConfigurationPacket) {
            corrected();
         } else if (minecraft.player != null
            && (
               packet instanceof ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket && EntityMotionPacketAccess.entityId(clientboundSetEntityMotionPacket) == minecraft.player.getId()
                  || packet instanceof ClientboundDamageEventPacket clientboundDamageEventPacket && clientboundDamageEventPacket.entityId() == minecraft.player.getId()
                  || packet instanceof ClientboundExplodePacket
            )) {
            updateState();
            long longValue = System.nanoTime() + 250000000L;
            if (longValue - timestamp2 > 0L) {
               timestamp = longValue;
               timestamp2 = longValue;
               text = "Recovering after impulse";
            }
         }
      }
   }

   public static void tick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (implProfileService2 != null && minecraft.isSameThread()) {
         long longValue = System.nanoTime();
         if (!checkCondition(minecraft, longValue) || pulseDelayQueue.due(longValue)) {
            updateState();
         }
      }
   }

   public static boolean intercept(Connection connection, Packet<?> currentPacket, PacketCaptureContext.Context context) {
      Minecraft minecraft = Minecraft.getInstance();
      if (implProfileService2 == null || !minecraft.isSameThread() || minecraft.getConnection() == null || connection != minecraft.getConnection().getConnection()) {
         return false;
      }

      if (currentPacket instanceof ServerboundAcceptTeleportationPacket) {
         corrected();
         return false;
      }

      long longValue = System.nanoTime();
      if (!checkCondition(minecraft, longValue)) {
         updateState();
         return false;
      }

      if (!enabled && (currentPacket instanceof ServerboundMovePlayerPacket || currentPacket instanceof ServerboundClientTickEndPacket)) {
         if (pulseDelayQueue.due(longValue)) {
            updateState();
         }

         if (longValue - timestamp < 0L) {
            return false;
         }

         if (!(currentPacket instanceof ServerboundClientTickEndPacket)) {
            ServerboundMovePlayerPacket serverboundMovePlayerPacket = (ServerboundMovePlayerPacket)currentPacket;
            if (pulseDelayQueue.count(item -> item.packet() instanceof ServerboundMovePlayerPacket) >= 3) {
               text = "Movement limit";
               updateState();
               return false;
            }

            RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElse(null);
            if (currentSnapshot != null && context != null) {
               RotationVector rotationVector = currentSnapshot.wireFeetPosition();
               double currentX = serverboundMovePlayerPacket.getX(rotationVector.x()) - rotationVector.x();
               double currentY = serverboundMovePlayerPacket.getY(rotationVector.y()) - rotationVector.y();
               double doubleValue = serverboundMovePlayerPacket.getZ(rotationVector.z()) - rotationVector.z();
               if (Double.isFinite(currentX + currentY + doubleValue)
                  && !(currentX * currentX + currentY * currentY + doubleValue * doubleValue > profile2.distance() * profile2.distance())) {
                  if (!pulseDelayQueue.isEmpty()
                     || serverboundMovePlayerPacket.hasPosition()
                        && !(currentX * currentX + currentY * currentY + doubleValue * doubleValue < 1.0E-4)) {
                     boolean empty = pulseDelayQueue.isEmpty();
                     if (!pulseDelayQueue.offer(new CompatStatusTracker.Pending(serverboundMovePlayerPacket, context), longValue, profile2.holdMillis())) {
                        text = "Packet limit";
                        updateState();
                        return false;
                     }

                     if (empty) {
                        timestamp3++;
                     }

                     text = "Holding movement";
                     return true;
                  } else {
                     text = "Waiting for movement";
                     return false;
                  }
               } else {
                  text = "Distance limit";
                  updateState();
                  return false;
               }
            } else {
               text = "Waiting for initial position";
               return false;
            }
         } else {
            if (pulseDelayQueue.isEmpty()) {
               return false;
            }

            if (pulseDelayQueue.offer(new CompatStatusTracker.Pending(currentPacket, null), longValue, profile2.holdMillis())) {
               return true;
            }

            updateState();
            return false;
         }
      } else {
         updateState();
         return false;
      }
   }

   public static void directSend(Connection connection, Packet<?> packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (implProfileService2 != null
         && !enabled2
         && minecraft.isSameThread()
         && minecraft.getConnection() != null
         && connection == minecraft.getConnection().getConnection()) {
         if (packet instanceof ServerboundAcceptTeleportationPacket) {
            corrected();
         } else {
            updateState();
         }
      }
   }

   public static void beforeInteraction() {
      if (implProfileService2 != null && Minecraft.getInstance().isSameThread()) {
         updateState();
      }
   }

   private static boolean checkCondition(Minecraft minecraft, long longValue) {
      Connection currentConnection = minecraft.getConnection() == null ? null : minecraft.getConnection().getConnection();
      if (minecraft.level != client2 || minecraft.player != localPlayer || currentConnection != connection) {
         worldChanged();
         client2 = minecraft.level;
         localPlayer = minecraft.player;
         connection = currentConnection;
      }

      PulseModule.Profile currentProfile = implProfileService2.profile();
      if (!currentProfile.equals(profile2)) {
         updateState();
         profile2 = currentProfile;
      }

      if (longValue - timestamp2 < 0L) {
         text = "Recovering after correction";
         return false;
      }

      if (localPlayer != null
         && client2 != null
         && connection != null
         && connection.isConnected()
         && minecraft.gameMode != null
         && localPlayer.isAlive()
         && !localPlayer.isSpectator()
         && !localPlayer.isPassenger()
         && !localPlayer.isSleeping()
         && !localPlayer.isUsingItem()
         && !localPlayer.isHandsBusy()
         && !localPlayer.isInWater()
         && !localPlayer.isInLava()
         && !localPlayer.onClimbable()
         && !localPlayer.getAbilities().flying
         && !localPlayer.isFallFlying()
         && !localPlayer.isAutoSpinAttack()
         && localPlayer.hurtTime <= 0
         && !localPlayer.horizontalCollision
         && localPlayer.tickCount >= 20
         && minecraft.screen == null
         && minecraft.getOverlay() == null
         && !minecraft.isPaused()
         && minecraft.isWindowActive()
         && !client2.tickRateManager().isFrozen()
         && !minecraft.gameMode.isDestroying()
         && !minecraft.options.keyUse.isDown()
         && !minecraft.options.keyShift.isDown()
         && localPlayer.containerMenu == localPlayer.inventoryMenu
         && localPlayer.inventoryMenu.getCarried().isEmpty()
         && !SoupInventoryBridge.handBusy()
         && !CompatActivateService.handBusy()
         && !CompatReleaseTracker.reserved()
         && !CompatOptionsTracker.handBusy()
         && !PearlThrowController.handBusy()
         && !CompatCanStartService.holding(minecraft)
         && !CompatCanStartService.pendingRelease(minecraft)) {
         for (String currentText : new String[]{"Speed", "ScaffoldWalk", "Blink"}) {
            if (CoreIsInitializedHandler.get().modules().get(currentText).filter(item -> item.isEnabled()).isPresent()) {
               text = "Paused while " + currentText + " is active";
               return false;
            }
         }

         if (profile2.combatOnly()
            && !minecraft.options.keyAttack.isDown()
            && CoreIsInitializedHandler.get().modules().get(ImplDecisionTracker.class).flatMap(ImplDecisionTracker::targetUuid).isEmpty()) {
            text = "Waiting for combat";
            return false;
         }

         if (pulseDelayQueue.isEmpty()) {
            text = longValue - timestamp < 0L ? "Between pulses" : "Ready";
         }

         return true;
      } else {
         text = "Paused by interaction / movement";
         return false;
      }
   }

   private static boolean checkCondition2() {
      Minecraft minecraft = Minecraft.getInstance();
      return client2 != null
         && minecraft.level == client2
         && minecraft.player == localPlayer
         && minecraft.getConnection() != null
         && minecraft.getConnection().getConnection() == connection
         && connection.isConnected();
   }

   private static void updateState() {
      if (!pulseDelayQueue.isEmpty()) {
         if (!checkCondition2()) {
            timestamp5 = timestamp5 + pulseDelayQueue.size();
            pulseDelayQueue.clear();
         } else {
            timestamp = System.nanoTime() + (profile2 == null ? 150 : profile2.intervalMillis()) * 1000000L;
            boolean currentEnabled = enabled;
            enabled = true;

            try {
               pulseDelayQueue.releaseAll(item -> {
                  if (!checkCondition2()) {
                     timestamp5++;
                  } else {
                     enabled2 = true;

                     try {
                        connection.send(item.packet(), null, true);
                     } finally {
                        enabled2 = false;
                     }

                     timestamp4++;
                     PacketCaptureContext.accepted(item.packet(), item.context());
                  }
               });
            } finally {
               enabled = currentEnabled;
            }
         }
      }
   }

   public static CompatStatusTracker.Status status() {
      return new CompatStatusTracker.Status(
         pulseDelayQueue.size(), pulseDelayQueue.ageMillis(System.nanoTime()), timestamp3, timestamp4, timestamp5, text
      );
   }

   public static Optional<AABB> sentBox() {
      return !pulseDelayQueue.isEmpty() && checkCondition2()
         ? RotationObserveService.snapshot()
            .map(
               item -> {
                  RotationVector rotationVector = item.wireFeetPosition();
                  return localPlayer.getBoundingBox()
                     .move(rotationVector.x() - localPlayer.getX(), rotationVector.y() - localPlayer.getY(), rotationVector.z() - localPlayer.getZ());
               }
            )
         : Optional.empty();
   }

   private record Pending(Packet<?> packet, PacketCaptureContext.Context context) {
   }

   public record Status(int packets, long ageMillis, long windows, long released, long discarded, String reason) {
   }
}

