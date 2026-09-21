package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.antibot.AntibotFeatureType;
import dev.felix.ellice.feature.backtrack.BacktrackDelayQueue;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.module.impl.ImplDecisionTracker;
import dev.felix.ellice.module.impl.BacktrackModule;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.StreamSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CompatEnableHandler {
   private static final BacktrackDelayQueue<CompatEnableHandler.Pending> backtrackDelayQueue = new BacktrackDelayQueue<>(256);
   private static final VecDeltaCodec vecDeltaCodec = new VecDeltaCodec();
   private static final double value = 1.0;
   private static final int count = 2;
   private static final Random random = new Random();
   private static BacktrackModule implProfileService4;
   private static ClientLevel client2;
   private static PacketListener items;
   private static LivingEntity livingEntity;
   private static UUID uUID;
   private static long timestamp;
   private static long timestamp2;
   private static int count2;
   private static int count3;
   private static int count4 = Integer.MIN_VALUE;
   private static UUID uUID2;
   private static String text;
   private static CompatEnableHandler.Profile profile2;

   private CompatEnableHandler() {
   }

   public static void enable(BacktrackModule backtrackModule) {
      reset();
      implProfileService4 = backtrackModule;
      client2 = CoreIsInitializedHandler.mc().level;
      items = CoreIsInitializedHandler.mc().getConnection();
      uUID = null;
      timestamp2 = System.nanoTime();
      count4 = Integer.MIN_VALUE;
      profile2 = backtrackModule.profile();
   }

   public static void disable(BacktrackModule backtrackModule) {
      if (implProfileService4 == backtrackModule) {
         reset();
         implProfileService4 = null;
         uUID = null;
      }
   }

   public static void worldChanged() {
      backtrackDelayQueue.clear();
      livingEntity = null;
      uUID = null;
      client2 = null;
      items = null;
      timestamp2 = System.nanoTime();
      count3 = 0;
      count4 = Integer.MIN_VALUE;
   }

   public static void reset() {
      updateState();
      livingEntity = null;
   }

   public static void attackPacket(Packet<?> packet) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (implProfileService4 != null && minecraft.isSameThread() && minecraft.level != null) {
         int value = STABComponent.attackedEntityId(packet, minecraft);
         if (minecraft.level.getEntity(value) instanceof LivingEntity livingEntity && checkCondition2(minecraft, livingEntity, implProfileService4.profile())) {
            uUID = livingEntity.getUUID();
            timestamp = System.nanoTime();
         }
      }
   }

   public static void tick() {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (implProfileService4 != null) {
         long longValue = System.nanoTime();
         if (!checkCondition(minecraft, longValue)) {
            updateState();
         } else {
            backtrackDelayQueue.releaseDue(longValue, CompatEnableHandler::updateState2);
         }
      }
   }

   public static boolean intercept(Packet<?> packet, PacketListener packetListener) {
      CompatStatusTracker.beforeIncoming(packet, packetListener);
      CompatEnableService.beforeIncoming(packet, packetListener);
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null && minecraft.isSameThread() && implProfileService4 != null && packetListener == minecraft.getConnection()) {
         long longValue = System.nanoTime();
         if (!checkCondition(minecraft, longValue)) {
            updateState();
            return false;
         }

         backtrackDelayQueue.releaseDue(longValue, CompatEnableHandler::updateState2);
         int value = calculateValue(packet, minecraft);
         if (value < 0) {
            return false;
         }

         if (value == 0) {
            updateState();
            return false;
         }

         CompatEnableHandler.Profile currentProfile = implProfileService4.profile();
         Vec3 vec3 = createVec3(minecraft);
         double doubleValue = calculateValue3(vec3, createAABB());
         boolean enabled = checkCondition3(packet, minecraft);
         double currentDoubleValue = calculateValue3(vec3, createAABB());
         double nextDoubleValue = calculateValue3(vec3, livingEntity.getBoundingBox());
         double previousDoubleValue = vecDeltaCodec.getBase().distanceTo(livingEntity.position());
         if (!(currentDoubleValue > currentProfile.maximumRange() + 1.5) && !(previousDoubleValue > 1.0)) {
            if (enabled) {
               if (currentDoubleValue + 0.025 < doubleValue) {
                  if (currentDoubleValue <= nextDoubleValue + 0.025 || ++count3 >= 2) {
                     updateState();
                     return false;
                  }
               } else if (currentDoubleValue > doubleValue + 0.001
                  && currentDoubleValue > nextDoubleValue + 0.025) {
                  count3 = 0;
               }
            }

            if (backtrackDelayQueue.isEmpty()) {
               if (longValue - timestamp2 < 0L
                  || currentProfile.maximumDelayMillis() == 0
                  || currentProfile.retreatOnly()
                     && (
                        !enabled
                           || currentDoubleValue <= doubleValue + 0.001
                           || currentDoubleValue <= nextDoubleValue + 0.025
                     )) {
                  return false;
               }

               count2 = currentProfile.minimumDelayMillis() + random.nextInt(currentProfile.maximumDelayMillis() - currentProfile.minimumDelayMillis() + 1);
               if (count2 == 0) {
                  return false;
               }
            }

            if (!backtrackDelayQueue.offer(new CompatEnableHandler.Pending(packet, packetListener, minecraft.level), longValue, count2, value)) {
               updateState();
               return false;
            } else {
               return true;
            }
         } else {
            updateState();
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean checkCondition(Minecraft minecraft, long longValue) {
      if (minecraft.level != client2 || minecraft.getConnection() != items) {
         worldChanged();
         client2 = minecraft.level;
         items = minecraft.getConnection();
      }

      if (minecraft.player != null
         && minecraft.level != null
         && minecraft.getConnection() != null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && minecraft.player.tickCount > 10
         && minecraft.screen == null
         && minecraft.getOverlay() == null
         && minecraft.isWindowActive()) {
         CompatEnableHandler.Profile currentProfile = implProfileService4.profile();
         if (!currentProfile.equals(profile2)) {
            updateState();
            profile2 = currentProfile;
            count4 = Integer.MIN_VALUE;
         }
         UUID currentUUID = switch (currentProfile.mode()) {
            case "Attack" -> longValue - timestamp <= currentProfile.attackWindowMillis() * 1000000L ? uUID : null;
            case "KillAura" -> (UUID)CoreIsInitializedHandler.get().modules().get(ImplDecisionTracker.class).flatMap(ImplDecisionTracker::targetUuid).orElse(null);
            default -> null;
         };
         LivingEntity currentLivingEntity = livingEntity;
         int value = count4 == minecraft.player.tickCount
               && Objects.equals(currentUUID, uUID2)
               && currentProfile.mode().equals(text)
               && (livingEntity == null || minecraft.level.getEntity(livingEntity.getId()) == livingEntity)
            ? 0
            : 1;
         Vec3 vec3 = createVec3(minecraft);
         if (value != 0 && currentProfile.mode().equals("Nearest")) {
            currentLivingEntity = StreamSupport.<Entity>stream(minecraft.level.entitiesForRendering().spliterator(), false)
               .filter(item -> item instanceof LivingEntity)
               .map(item -> (LivingEntity)item)
               .filter(item -> checkCondition2(minecraft, item, currentProfile))
               .filter(item -> calculateValue3(vec3, item.getBoundingBox()) <= currentProfile.maximumRange())
               .min(Comparator.comparingDouble(item -> calculateValue3(vec3, item.getBoundingBox())))
               .orElse(null);
         } else if (value != 0) {
            currentLivingEntity = currentUUID == null
               ? null
               : StreamSupport.<Entity>stream(minecraft.level.entitiesForRendering().spliterator(), false)
                  .filter(item -> item instanceof LivingEntity && item.getUUID().equals(currentUUID))
                  .map(item -> (LivingEntity)item)
                  .findFirst()
                  .orElse(null);
         }

         count4 = minecraft.player.tickCount;
         uUID2 = currentUUID;
         text = currentProfile.mode();
         if (currentLivingEntity != null
            && (
               !currentLivingEntity.isAlive()
                  || FriendSyncController.excluded(FriendsMode.BACKTRACK, currentLivingEntity)
                  || CompatDecisionTracker.excluded(AntibotFeatureType.BACKTRACK, currentLivingEntity)
                  || value != 0 && !checkCondition2(minecraft, currentLivingEntity, currentProfile)
            )) {
            currentLivingEntity = null;
         }

         if (currentLivingEntity != livingEntity) {
            updateState();
            livingEntity = currentLivingEntity;
            if (livingEntity != null) {
               vecDeltaCodec.setBase(livingEntity.getPositionCodec().getBase());
            }
         } else if (livingEntity != null && backtrackDelayQueue.isEmpty()) {
            vecDeltaCodec.setBase(livingEntity.getPositionCodec().getBase());
         }

         if (livingEntity != null && !CompatEnableService.coordinatesBacktrack(livingEntity.getUUID())) {
            double doubleValue = calculateValue3(vec3, livingEntity.getBoundingBox());
            int currentValue = !(doubleValue <= currentProfile.maximumRange()) || !(doubleValue >= currentProfile.minimumRange()) && backtrackDelayQueue.isEmpty() ? 0 : 1;
            return currentValue != 0 && currentProfile.maximumDelayMillis() > 0 && (!currentProfile.pauseHurt() || minecraft.player.hurtTime == 0);
         } else {
            return false;
         }
      } else {
         reset();
         return false;
      }
   }

   private static boolean checkCondition2(Minecraft minecraft, LivingEntity livingEntity, CompatEnableHandler.Profile profile) {
      if (FriendSyncController.excluded(FriendsMode.BACKTRACK, livingEntity)) {
         return false;
      } else {
         return CompatDecisionTracker.excluded(AntibotFeatureType.BACKTRACK, livingEntity)
            ? false
            : livingEntity != minecraft.player
               && livingEntity.isAlive()
               && !livingEntity.isSpectator()
               && livingEntity.isAttackable()
               && livingEntity.isPickable()
               && !livingEntity.isInvisibleTo(minecraft.player)
               && (livingEntity instanceof Player ? profile.players() && minecraft.player.canHarmPlayer((Player)livingEntity) : livingEntity instanceof Mob && profile.mobs())
               && !minecraft.player.isAlliedTo(livingEntity)
               && !livingEntity.isAlliedTo(minecraft.player)
               && minecraft.player.hasLineOfSight(livingEntity);
      }
   }

   private static int calculateValue(Packet<?> packet, Minecraft minecraft) {
      if (!(packet instanceof BundlePacket bundlePacket)) {
         if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
            return clientboundMoveEntityPacket.getEntity(minecraft.level) == livingEntity ? 1 : (clientboundMoveEntityPacket.getEntity(minecraft.level) == minecraft.player ? 0 : -2);
         } else if (packet instanceof ClientboundRotateHeadPacket clientboundRotateHeadPacket) {
            return clientboundRotateHeadPacket.getEntity(minecraft.level) == livingEntity ? 1 : (clientboundRotateHeadPacket.getEntity(minecraft.level) == minecraft.player ? 0 : -2);
         } else if (packet instanceof ClientboundAnimatePacket clientboundAnimatePacket) {
            return clientboundAnimatePacket.getId() == livingEntity.getId() ? -1 : (clientboundAnimatePacket.getId() == minecraft.player.getId() ? 0 : -2);
         } else if (packet instanceof ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
            return calculateValue2(clientboundSetEntityDataPacket.id(), minecraft);
         } else if (packet instanceof ClientboundSetEquipmentPacket clientboundSetEquipmentPacket) {
            return calculateValue2(clientboundSetEquipmentPacket.getEntity(), minecraft);
         } else if (packet instanceof ClientboundUpdateAttributesPacket clientboundUpdateAttributesPacket) {
            return calculateValue2(clientboundUpdateAttributesPacket.getEntityId(), minecraft);
         } else if (packet instanceof ClientboundAnimatePacket currentClientboundAnimatePacket) {
            return calculateValue2(currentClientboundAnimatePacket.getId(), minecraft);
         } else {
            return !(packet instanceof ClientboundSetTimePacket)
                  && !(packet instanceof ClientboundSoundPacket)
                  && !(packet instanceof ClientboundSoundEntityPacket)
                  && !(packet instanceof ClientboundSystemChatPacket)
                  && !(packet instanceof ClientboundPlayerChatPacket)
                  && !(packet instanceof ClientboundTabListPacket)
                  && !(packet instanceof ClientboundSetActionBarTextPacket)
                  && !(packet instanceof ClientboundLevelParticlesPacket)
                  && !(packet instanceof ClientboundSetScorePacket)
                  && !(packet instanceof ClientboundResetScorePacket)
                  && !(packet instanceof ClientboundSetObjectivePacket)
                  && !(packet instanceof ClientboundSetDisplayObjectivePacket)
                  && !(packet instanceof ClientboundBossEventPacket)
               ? 0
               : -1;
         }
      } else {
         int value = 0;
         int byteValue = 0;

         for (Packet currentPacket : (Iterable<Packet>) (Iterable<?>) (bundlePacket.subPackets())) {
            int currentValue = calculateValue(currentPacket, minecraft);
            if (currentValue == 0) {
               return 0;
            }

            if (currentValue == -2) {
               byteValue = 1;
            } else if (currentValue > 0) {
               value += currentValue;
            }

            if (value > 256 || byteValue != 0 && value > 0) {
               return 0;
            }
         }

         return value > 0 ? value : -1;
      }
   }

   private static int calculateValue2(int value, Minecraft minecraft) {
      return value != livingEntity.getId() && value != minecraft.player.getId() ? -2 : 0;
   }

   private static boolean checkCondition3(Packet<?> packet, Minecraft minecraft) {
      if (!(packet instanceof BundlePacket bundlePacket)) {
         if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket && clientboundMoveEntityPacket.hasPosition() && clientboundMoveEntityPacket.getEntity(minecraft.level) == livingEntity) {
            vecDeltaCodec.setBase(vecDeltaCodec.decode(clientboundMoveEntityPacket.getXa(), clientboundMoveEntityPacket.getYa(), clientboundMoveEntityPacket.getZa()));
            return true;
         } else {
            return false;
         }
      } else {
         boolean matched = false;

         for (Packet currentPacket : (Iterable<Packet>) (Iterable<?>) (bundlePacket.subPackets())) {
            matched |= checkCondition3(currentPacket, minecraft);
         }

         return matched;
      }
   }

   private static void updateState() {
      int value = !backtrackDelayQueue.isEmpty() ? 1 : 0;
      count3 = 0;
      backtrackDelayQueue.releaseAll(CompatEnableHandler::updateState2);
      if (value != 0 && implProfileService4 != null) {
         timestamp2 = System.nanoTime() + implProfileService4.profile().intervalMillis() * 1000000L;
      }
   }

   private static void updateState2(CompatEnableHandler.Pending pending) {
      Minecraft minecraft = CoreIsInitializedHandler.mc();
      if (pending.listener() == minecraft.getConnection() && pending.world() == minecraft.level && pending.listener().shouldHandleMessage(pending.packet())) {
         try {
            handle(pending.packet(), pending.listener());
         } catch (Exception exception) {
            pending.listener().onPacketError(pending.packet(), exception);
         }
      }
   }

   public static void handle(Packet packet, PacketListener packetListener) {
      packet.handle(packetListener);
   }

   public static CompatEnableHandler.Status status() {
      return new CompatEnableHandler.Status(
         livingEntity == null ? null : livingEntity.getUUID(),
         backtrackDelayQueue.size(),
         backtrackDelayQueue.oldestAgeMillis(System.nanoTime()),
         count2,
         livingEntity == null ? Vec3.ZERO : vecDeltaCodec.getBase(),
         livingEntity == null ? 0.0 : vecDeltaCodec.getBase().distanceTo(livingEntity.position())
      );
   }

   public static Optional<AABB> receivedBox() {
      return livingEntity == null ? Optional.empty() : Optional.of(createAABB());
   }

   public static Optional<AABB> receivedBox(UUID uUID) {
      return implProfileService4 != null && livingEntity != null && livingEntity.getUUID().equals(uUID) ? receivedBox() : Optional.empty();
   }

   public static void flushForTickShift(UUID uUID) {
      if (implProfileService4 != null && livingEntity != null && livingEntity.getUUID().equals(uUID)) {
         updateState();
      }
   }

   private static AABB createAABB() {
      return livingEntity.getBoundingBox().move(vecDeltaCodec.getBase().subtract(livingEntity.position()));
   }

   private static Vec3 createVec3(Minecraft minecraft) {
      Optional result = CompatStatusTracker.sentBox();
      if (result.isPresent()) {
         AABB aABB = (AABB)result.get();
         return new Vec3(
            (aABB.minX + aABB.maxX) * 0.5,
            aABB.minY + minecraft.player.getEyeHeight(),
            (aABB.minZ + aABB.maxZ) * 0.5
         );
      } else {
         return minecraft.player.getEyePosition();
      }
   }

   private static double calculateValue3(Vec3 vec3, AABB aABB) {
      double doubleValue = Math.max(aABB.minX, Math.min(aABB.maxX, vec3.x));
      double currentDoubleValue = Math.max(aABB.minY, Math.min(aABB.maxY, vec3.y));
      double nextDoubleValue = Math.max(aABB.minZ, Math.min(aABB.maxZ, vec3.z));
      return vec3.distanceTo(new Vec3(doubleValue, currentDoubleValue, nextDoubleValue));
   }

   private record Pending(Packet<?> packet, PacketListener listener, ClientLevel world) {
   }

   public record Profile(
      String mode,
      boolean players,
      boolean mobs,
      float minimumRange,
      float maximumRange,
      int attackWindowMillis,
      int minimumDelayMillis,
      int maximumDelayMillis,
      int intervalMillis,
      boolean retreatOnly,
      boolean pauseHurt
   ) {
   }

   public record Status(UUID target, int packets, long oldestMillis, int delayMillis, Vec3 receivedPosition, double divergence) {
   }
}
