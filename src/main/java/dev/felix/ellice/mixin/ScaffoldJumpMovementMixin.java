package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class ScaffoldJumpMovementMixin {
  @Redirect(
      method = "jumpFromGround",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"),
      require = 1)
  private float ellice$scaffoldJumpYaw(LivingEntity entity) {
    return entity instanceof LocalPlayer
        ? LocalPhysicsFrameBus.yaw().orElseGet(entity::getYRot)
        : entity.getYRot();
  }
}
