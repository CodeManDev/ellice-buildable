package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
  @Inject(method = "init", at = @At("HEAD"), cancellable = true)
  private void ellice$mainMenu(CallbackInfo ci) {
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().showMainMenu()) {
      ci.cancel();
    }
  }
}
