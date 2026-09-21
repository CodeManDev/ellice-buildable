package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatInterceptService;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeybindCaptureMixin {
  @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
  private void ellice$captureBinding(long window, int action, KeyEvent event, CallbackInfo ci) {
    if (CompatInterceptService.intercept(window, event.key(), action, event.modifiers())) {
      ci.cancel();
    }
  }
}
