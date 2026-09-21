package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.felix.ellice.feature.velocity.VelocityActivateService;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class VelocityPushMixin {
   @WrapOperation(
      method = "push(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;push(DDD)V")
   )
   private void ellice$collisionPush(Entity entity, double x, double y, double z, Operation<Void> original) {
      double scale = VelocityActivateService.pushScale(entity);
      if (scale != 0.0) {
         original.call(new Object[]{entity, x * scale, y * scale, z * scale});
      }
   }
}
