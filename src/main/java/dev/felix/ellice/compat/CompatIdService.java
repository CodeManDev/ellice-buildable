package dev.felix.ellice.compat;

import java.util.List;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.item.ItemStack;

public final class CompatIdService {
  private CompatIdService() {}

  public static int id(ClientboundContainerSetContentPacket clientboundContainerSetContentPacket) {
    return clientboundContainerSetContentPacket.containerId();
  }

  public static List<ItemStack> items(
      ClientboundContainerSetContentPacket clientboundContainerSetContentPacket) {
    return clientboundContainerSetContentPacket.items();
  }
}
