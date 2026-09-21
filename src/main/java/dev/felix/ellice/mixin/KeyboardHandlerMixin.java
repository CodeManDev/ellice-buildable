package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.fatal.FatalShowingService;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void ellice$keyInput(long window, int action, KeyEvent event, CallbackInfo ci) {
      if (FatalShowingService.consumeKey(window, event.key(), action)) {
         ci.cancel();
      } else {
         if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().handleKeyInput(window, event.key(), event.scancode(), action)) {
            ci.cancel();
         }
      }
   }
}
