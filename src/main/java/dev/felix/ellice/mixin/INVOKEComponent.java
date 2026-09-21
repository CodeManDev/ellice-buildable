package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.felix.ellice.feature.velocity.VelocityActivateService;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPacketListener.class)
public abstract class INVOKEComponent {
   @ModifyExpressionValue(
      method = "handleExplosion",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;"
      )
   )
   private Optional<Vec3> ellice$explosionMotion(Optional<Vec3> incoming) {
      return incoming.flatMap(value -> Optional.ofNullable(VelocityActivateService.impulse(Minecraft.getInstance().player, value, true)));
   }
}
