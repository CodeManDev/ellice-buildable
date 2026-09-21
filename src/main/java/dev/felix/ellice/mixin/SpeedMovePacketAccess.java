package dev.felix.ellice.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundMovePlayerPacket.class)
public interface SpeedMovePacketAccess {
   @Mutable
   @Accessor("y")
   void ellice$speedY(double doubleValue);

   @Mutable
   @Accessor("onGround")
   void ellice$speedGround(boolean enabled);
}
