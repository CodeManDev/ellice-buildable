package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatIdService;
import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.compat.SoupInventoryBridge;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class SoupInventoryUpdatesMixin {
  @Inject(method = "handleContainerSetSlot", at = @At("RETURN"))
  private void ellice$soupSlot(ClientboundContainerSetSlotPacket packet, CallbackInfo ci) {
    SoupInventoryBridge.serverSlot(packet.getContainerId(), packet.getSlot(), packet.getItem());
    CompatReleaseTracker.serverSlot(packet.getContainerId(), packet.getSlot(), packet.getItem());
  }

  @Inject(method = "handleContainerContent", at = @At("RETURN"))
  private void ellice$soupContent(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
    SoupInventoryBridge.serverContent(CompatIdService.id(packet), CompatIdService.items(packet));
    CompatReleaseTracker.serverContent(CompatIdService.id(packet), CompatIdService.items(packet));
  }

  @Inject(method = "handleSetPlayerInventory", at = @At("RETURN"))
  private void ellice$soupInventory(ClientboundSetPlayerInventoryPacket packet, CallbackInfo ci) {
    SoupInventoryBridge.serverInventorySlot(packet.slot(), packet.contents());
    CompatReleaseTracker.serverInventorySlot(packet.slot(), packet.contents());
  }
}
