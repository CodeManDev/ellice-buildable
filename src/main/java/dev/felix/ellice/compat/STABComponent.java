package dev.felix.ellice.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.phys.EntityHitResult;

final class STABComponent {
   private STABComponent() {
   }

   static int attackedEntityId(Packet<?> packet, Minecraft minecraft) {
      if (packet instanceof ServerboundAttackPacket serverboundAttackPacket) {
         return serverboundAttackPacket.entityId();
      } else {
         return packet instanceof ServerboundPlayerActionPacket serverboundPlayerActionPacket
               && serverboundPlayerActionPacket.getAction().name().equals("STAB")
               && minecraft.hitResult instanceof EntityHitResult entityHitResult
            ? entityHitResult.getEntity().getId()
            : Integer.MIN_VALUE;
      }
   }
}
