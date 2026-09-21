package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.ImplIsActiveService;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class NoHurtCamMixin {
   @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
   private void ellice$noHurtCam(CallbackInfo ci) {
      if (ImplIsActiveService.isActive()) {
         ci.cancel();
      }
   }
}
