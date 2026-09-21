package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.speed.SpeedActivateService;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SpeedLivingMixin {
  @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
  private void ellice$speedBeforeJump(CallbackInfo ci) {
    if (SpeedActivateService.beforeJump(this)) {
      ci.cancel();
    }
  }

  @Inject(method = "getJumpPower()F", at = @At("RETURN"), cancellable = true)
  private void ellice$speedJumpPower(CallbackInfoReturnable<Float> cir) {
    cir.setReturnValue(SpeedActivateService.jumpPower(this, (Float) cir.getReturnValue()));
  }

  @Inject(method = "jumpFromGround", at = @At("RETURN"))
  private void ellice$speedAfterJump(CallbackInfo ci) {
    SpeedActivateService.afterJump(this);
  }
}
