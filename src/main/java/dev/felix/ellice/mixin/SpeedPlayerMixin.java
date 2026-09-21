package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.speed.SpeedActivateService;
import dev.felix.ellice.module.impl.NoJumpDelayModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class SpeedPlayerMixin {
   @Inject(
      method = "aiStep",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V", shift = Shift.BEFORE)
   )
   private void ellice$speedBeforePhysics(CallbackInfo ci) {
      SpeedActivateService.beforePhysics();
      if (NoJumpDelayModule.isActive()) {
         ((SpeedLivingAccess)(Object)this).ellice$speedJumpDelay(0);
      }
   }
}
