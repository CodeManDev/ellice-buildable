package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatEnableHandler;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.network.PacketProcessor$ListenerAndPacket")
public abstract class BacktrackPacketMixin {
  @Redirect(
      method = "handle",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"),
      require = 1)
  private void ellice$orderedIncoming(Packet<?> packet, PacketListener listener) {
    if (!CompatEnableHandler.intercept(packet, listener)) {
      CompatEnableHandler.handle(packet, listener);
    }
  }
}
