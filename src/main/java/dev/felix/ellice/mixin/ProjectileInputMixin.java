package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatBeginTickService;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class ProjectileInputMixin {
   @Inject(method = {"startUseItem", "continueAttack"}, at = @At("HEAD"), cancellable = true)
   private void ellice$reserveProjectileTick(CallbackInfo ci) {
      if (CompatBeginTickService.blocking()) {
         ci.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void ellice$reserveProjectileAttack(CallbackInfoReturnable<Boolean> cir) {
      if (CompatBeginTickService.blocking()) {
         cir.setReturnValue(false);
      }
   }
}
