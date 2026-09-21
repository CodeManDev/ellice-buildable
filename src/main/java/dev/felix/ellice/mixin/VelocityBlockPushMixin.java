package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.velocity.VelocityActivateService;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class VelocityBlockPushMixin {
  @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
  private void ellice$blockPush(double x, double z, CallbackInfo ci) {
    if (VelocityActivateService.cancelBlockPush((LocalPlayer) (Object) this)) {
      ci.cancel();
    }
  }
}
