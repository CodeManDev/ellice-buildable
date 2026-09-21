package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatActiveService;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class CombatCameraInterpolationMixin {
  @Inject(method = "setOldPosAndRot()V", at = @At("RETURN"))
  private void ellice$preserveCombatCameraInterpolation(CallbackInfo ci) {
    CompatActiveService.preserveInterpolation((Entity) (Object) this);
  }

  @Inject(method = "setOldRot()V", at = @At("RETURN"))
  private void ellice$preserveCombatCameraOldRot(CallbackInfo ci) {
    CompatActiveService.preserveInterpolation((Entity) (Object) this);
  }

  @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
  private void ellice$blockCombatCameraMouse(double yaw, double pitch, CallbackInfo ci) {
    if (CompatActiveService.blocksMouse((Entity) (Object) this)) {
      ci.cancel();
    }
  }
}
