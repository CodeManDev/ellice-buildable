package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MultiplayerMenuMixin {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void ellice$serverMenu(Screen screen, CallbackInfo ci) {
      if (screen instanceof JoinMultiplayerScreen && CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().showServerMenu()) {
         ci.cancel();
      }
   }
}
