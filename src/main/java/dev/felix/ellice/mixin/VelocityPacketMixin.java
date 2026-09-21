package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.velocity.VelocityActivateService;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class VelocityPacketMixin {
   @Redirect(
      method = "handleSetEntityMotion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(Lnet/minecraft/world/phys/Vec3;)V")
   )
   private void ellice$velocity(Entity entity, Vec3 incoming) {
      Vec3 value = VelocityActivateService.impulse(entity, incoming, false);
      if (value != null) {
         entity.lerpMotion(value);
      }
   }
}
