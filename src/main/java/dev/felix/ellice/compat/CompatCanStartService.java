package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventCancelHandler;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CompatCanStartService {
   private static CompatCanStartService.Lease lease;
   private static LocalPlayer localPlayer;
   private static ClientPacketListener items;
   private static int count = Integer.MIN_VALUE;

   private CompatCanStartService() {
   }

   public static boolean canStart(Minecraft minecraft) {
      return lease == null
         && CoreIsInitializedHandler.isReady()
         && checkCondition2(minecraft)
         && !checkCondition(minecraft)
         && !minecraft.player.isUsingItem()
         && !checkCondition4(minecraft)
         && minecraft.player.getOffhandItem().is(Items.SHIELD)
         && minecraft.player.getOffhandItem().isItemEnabled(minecraft.level.enabledFeatures())
         && !minecraft.player.getCooldowns().isOnCooldown(minecraft.player.getOffhandItem())
         && RotationObserveService.snapshot().isPresent();
   }

   public static boolean start(Minecraft minecraft) {
      if (!canStart(minecraft)) {
         return false;
      }

      RotationObserveService.Snapshot currentSnapshot = RotationObserveService.snapshot().orElseThrow();
      CompatCanStartService.Lease currentLease = new CompatCanStartService.Lease(minecraft);
      boolean[] booleans = new boolean[]{false};
      EventCancelHandler eventCancel = CoreIsInitializedHandler.get().bus().subscribe(EventAttackInputService.OUTGOING_PACKET_ACCEPTED, item -> {
         if (item.packet() instanceof ServerboundUseItemPacket serverboundUseItemPacket && serverboundUseItemPacket.getHand() == InteractionHand.OFF_HAND) {
            booleans[0] = true;
         }
      });

      try {
         ScaffoldForActorService.withRotation(currentLease.player, currentSnapshot.rotation(), () -> minecraft.gameMode.useItem(currentLease.player, InteractionHand.OFF_HAND));
      } finally {
         eventCancel.cancel();
         if (currentLease.matches(minecraft)) {
            if (booleans[0]) {
               lease = currentLease;
               currentLease.pendingRelease = !checkCondition3(currentLease);
            } else if (checkCondition3(currentLease)) {
               currentLease.player.stopUsingItem();
            }
         }
      }

      return holding(minecraft);
   }

   public static boolean holding(Minecraft minecraft) {
      return lease != null && lease.matches(minecraft) && !lease.pendingRelease && checkCondition3(lease);
   }

   public static boolean pendingRelease(Minecraft minecraft) {
      return lease != null && lease.matches(minecraft) && lease.pendingRelease;
   }

   public static boolean release(Minecraft minecraft) {
      CompatCanStartService.Lease currentLease = lease;
      if (currentLease == null) {
         return false;
      }

      if (!currentLease.matches(minecraft)) {
         abandon();
         return false;
      }

      if (checkCondition3(currentLease) || currentLease.pendingRelease && !currentLease.player.isUsingItem()) {
         if (checkCondition3(currentLease) && checkCondition2(minecraft) && minecraft.options.keyUse.isDown()) {
            lease = null;
            return false;
         }

         if (!checkCondition(minecraft) && minecraft.gameMode != null && CoreIsInitializedHandler.isReady()) {
            localPlayer = currentLease.player;
            items = currentLease.connection;
            count = currentLease.player.tickCount;
            currentLease.pendingRelease = true;
            boolean[] booleans = new boolean[]{false};
            EventCancelHandler eventCancel = CoreIsInitializedHandler.get().bus().subscribe(EventAttackInputService.OUTGOING_PACKET_ACCEPTED, item -> {
               if (item.packet() instanceof ServerboundPlayerActionPacket serverboundPlayerActionPacket && serverboundPlayerActionPacket.getAction() == Action.RELEASE_USE_ITEM) {
                  booleans[0] = true;
               }
            });

            try {
               minecraft.gameMode.releaseUsingItem(currentLease.player);
            } finally {
               eventCancel.cancel();
               if (booleans[0] && lease == currentLease) {
                  lease = null;
               }
            }

            return booleans[0];
         } else {
            return false;
         }
      } else {
         lease = null;
         return false;
      }
   }

   public static boolean maintain(Minecraft minecraft) {
      if (lease != null && !lease.matches(minecraft)) {
         abandon();
      }

      if (checkCondition(minecraft)) {
         return true;
      }

      CompatCanStartService.Lease currentLease = lease;
      if (currentLease == null) {
         return false;
      }

      if (checkCondition3(currentLease) && checkCondition2(minecraft) && minecraft.options.keyUse.isDown()) {
         lease = null;
         return false;
      }

      if (currentLease.pendingRelease) {
         release(minecraft);
         return true;
      }

      if (!checkCondition3(currentLease)) {
         lease = null;
         return false;
      }

      if (checkCondition2(minecraft)
         && !checkCondition4(minecraft)
         && minecraft.player.getOffhandItem() == currentLease.shield
         && !minecraft.player.getCooldowns().isOnCooldown(minecraft.player.getOffhandItem())) {
         return false;
      }

      release(minecraft);
      return true;
   }

   public static boolean holdUse() {
      Minecraft minecraft = Minecraft.getInstance();
      return holding(minecraft) && checkCondition2(minecraft) && !checkCondition4(minecraft);
   }

   public static void abandon() {
      lease = null;
      localPlayer = null;
      items = null;
      count = Integer.MIN_VALUE;
   }

   private static boolean checkCondition(Minecraft minecraft) {
      return minecraft != null
         && minecraft.player != null
         && minecraft.player == localPlayer
         && minecraft.getConnection() == items
         && minecraft.player.tickCount == count;
   }

   private static boolean checkCondition2(Minecraft minecraft) {
      if (minecraft != null
         && minecraft.player != null
         && minecraft.level != null
         && minecraft.gameMode != null
         && minecraft.getConnection() != null
         && minecraft.screen == null
         && minecraft.getOverlay() == null
         && !minecraft.isPaused()
         && minecraft.isWindowActive()) {
         LocalPlayer localPlayer = minecraft.player;
         return localPlayer.isAlive()
            && !localPlayer.isSpectator()
            && !localPlayer.isSleeping()
            && !localPlayer.isHandsBusy()
            && !SoupInventoryBridge.handBusy()
            && !CompatReleaseTracker.handBusy()
            && !CompatActivateService.handBusy()
            && !minecraft.gameMode.isDestroying();
      } else {
         return false;
      }
   }

   private static boolean checkCondition3(CompatCanStartService.Lease lease) {
      return lease.player.isUsingItem()
         && lease.player.getUsedItemHand() == InteractionHand.OFF_HAND
         && lease.player.getUseItem() == lease.shield
         && lease.shield.is(Items.SHIELD);
   }

   private static boolean checkCondition4(Minecraft minecraft) {
      return minecraft.options.keyUse.isDown()
         || minecraft.options.keyAttack.isDown()
         || ((ScaffoldKeyMappingAccess)minecraft.options.keyUse).ellice$pendingClicks() > 0
         || ((ScaffoldKeyMappingAccess)minecraft.options.keyAttack).ellice$pendingClicks() > 0;
   }

   private static final class Lease {
      final LocalPlayer player;
      final ClientLevel level;
      final ClientPacketListener connection;
      final ItemStack shield;
      boolean pendingRelease;

      Lease(Minecraft minecraft) {
         this.player = minecraft.player;
         this.level = minecraft.level;
         this.connection = minecraft.getConnection();
         this.shield = minecraft.player.getOffhandItem();
      }

      boolean matches(Minecraft minecraft) {
         return minecraft != null && minecraft.player == this.player && minecraft.level == this.level && minecraft.getConnection() == this.connection;
      }
   }
}

