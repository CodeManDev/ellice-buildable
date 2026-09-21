package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class ScaffoldEntityMovementMixin {
  @Redirect(
      method = "moveRelative",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getYRot()F"),
      require = 1)
  private float ellice$scaffoldMovementYaw(Entity entity) {
    return entity instanceof LocalPlayer
        ? LocalPhysicsFrameBus.yaw().orElseGet(entity::getYRot)
        : entity.getYRot();
  }
}
