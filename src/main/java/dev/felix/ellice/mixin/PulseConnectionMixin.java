package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatStatusTracker;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class PulseConnectionMixin {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"))
   private void ellice$pulseSendBarrier(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
      CompatStatusTracker.directSend((Connection)(Object)this, packet);
   }
}
