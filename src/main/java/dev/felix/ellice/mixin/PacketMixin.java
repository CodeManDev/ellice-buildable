package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.felix.ellice.compat.CompatEnableService;
import dev.felix.ellice.compat.CompatStatusTracker;
import dev.felix.ellice.compat.PacketCaptureContext;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class PacketMixin {
  private static Packet<?> ellice$remapServerMovementInput(Packet<?> packet) {
    if (packet instanceof ServerboundPlayerInputPacket original) {
      ScaffoldRemapService.Input mapped = ScaffoldPublishService.current().orElse(null);
      if (mapped == null) {
        return packet;
      }

      Input input =
          new Input(
              mapped.forward(),
              mapped.backward(),
              mapped.left(),
              mapped.right(),
              mapped.jump(),
              mapped.sneak(),
              mapped.sprint());
      return (Packet<?>)
          (input.equals(original.input()) ? packet : new ServerboundPlayerInputPacket(input));
    } else {
      return packet;
    }
  }

  @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
  private static void onReceive(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
    if (CoreIsInitializedHandler.isReady()) {
      EventAttackInputService.Packet event =
          new EventAttackInputService.Packet(packet, EventAttackInputService.Packet.Direction.IN);
      CoreIsInitializedHandler.get().bus().post(EventAttackInputService.PACKET, event);
      if (event.isCancelled()) {
        ci.cancel();
      }
    }
  }

  @WrapMethod(method = "send(Lnet/minecraft/network/protocol/Packet;)V")
  private void onSend(Packet<?> packet, Operation<Void> original) {
    if (!CoreIsInitializedHandler.isReady()) {
      original.call(new Object[] {packet});
    } else {
      packet = ellice$remapServerMovementInput(packet);
      EventAttackInputService.Packet event =
          new EventAttackInputService.Packet(packet, EventAttackInputService.Packet.Direction.OUT);
      CoreIsInitializedHandler.get().bus().post(EventAttackInputService.PACKET, event);
      if (!event.isCancelled()) {
        if (!CompatEnableService.suppressTickEnd((Connection) (Object) this, packet)) {
          PacketCaptureContext.Context context = PacketCaptureContext.capture();
          if (!CompatStatusTracker.intercept((Connection) (Object) this, packet, context)) {
            original.call(new Object[] {packet});
            PacketCaptureContext.accepted(packet, context);
          }
        }
      }
    }
  }
}
