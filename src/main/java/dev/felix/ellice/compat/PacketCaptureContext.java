package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.rotation.RotationVector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class PacketCaptureContext {
  private PacketCaptureContext() {}

  public static PacketCaptureContext.Context capture() {
    LocalPlayer localPlayer = Minecraft.getInstance().player;
    if (localPlayer == null) {
      return null;
    }

    Vec3 vec3 = localPlayer.getEyePosition();
    return new PacketCaptureContext.Context(
        new RotationVector(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ()),
        new RotationVector(vec3.x, vec3.y, vec3.z),
        RotationPublishService.current()
            .orElseGet(() -> new RotationData(localPlayer.getYRot(), localPlayer.getXRot())));
  }

  public static void accepted(Packet<?> packet, PacketCaptureContext.Context context) {
    CompatBeginTickService.observeAccepted(packet);
    CompatEnableService.accepted(packet);
    if (packet instanceof ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
      if (context == null) {
        RotationObserveService.observe(serverboundMovePlayerPacket);
      } else {
        RotationObserveService.observe(
            serverboundMovePlayerPacket, context.feet(), context.eye(), context.rotation());
      }
    } else if (packet instanceof ServerboundClientTickEndPacket) {
      RotationObserveService.endTick();
    } else if (packet instanceof ServerboundUseItemPacket serverboundUseItemPacket) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null) {
        ItemStack itemStack = minecraft.player.getItemInHand(serverboundUseItemPacket.getHand());
        if (!itemStack.isEmpty() && itemStack.isItemEnabled(minecraft.level.enabledFeatures())) {
          RotationObserveService.observe(serverboundUseItemPacket);
        }
      }
    }

    CoreIsInitializedHandler.get()
        .bus()
        .post(
            EventAttackInputService.OUTGOING_PACKET_ACCEPTED,
            new EventAttackInputService.OutgoingPacketAccepted(packet));
  }

  public record Context(RotationVector feet, RotationVector eye, RotationData rotation) {}
}
