package dev.felix.ellice.mixin;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundInteractPacket.class)
public interface BacktrackInteractAccess {
   @Accessor("entityId")
   int ellice$entityId();
}
