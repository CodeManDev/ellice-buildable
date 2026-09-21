package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatShowService;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
   @Inject(method = "init", at = @At("HEAD"), cancellable = true)
   private void ellice$worldCreation(CallbackInfo ci) {
      if (CompatShowService.show((CreateWorldScreen)(Object)this)) {
         ci.cancel();
      }
   }
}
