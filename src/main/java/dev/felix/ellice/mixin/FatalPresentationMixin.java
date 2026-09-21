package dev.felix.ellice.mixin;

import dev.felix.ellice.diagnostics.fatal.FatalShowingService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlDevice")
public abstract class FatalPresentationMixin {
   @Inject(method = "presentFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSwapBuffers(J)V", remap = false))
   private void ellice$presentCrash(CallbackInfo ci) {
      FatalShowingService.present();
   }
}
