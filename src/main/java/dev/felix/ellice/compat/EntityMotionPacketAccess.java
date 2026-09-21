package dev.felix.ellice.compat;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

final class EntityMotionPacketAccess {
   private EntityMotionPacketAccess() {
   }

   static int entityId(ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket) {
      return clientboundSetEntityMotionPacket.id();
   }
}
