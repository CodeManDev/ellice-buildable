package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.speed.SpeedActivateService;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class SpeedEntityMixin {
   @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
   private Vec3 ellice$speedMove(Vec3 movement, MoverType type, Vec3 original) {
      return SpeedActivateService.move(this, type, movement);
   }

   @ModifyVariable(method = "moveRelative", at = @At("HEAD"), argsOnly = true)
   private float ellice$speedStrafe(float speed) {
      return SpeedActivateService.strafe(this, speed);
   }
}
