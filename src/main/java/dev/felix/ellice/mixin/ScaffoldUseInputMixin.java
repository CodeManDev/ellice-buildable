package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ScaffoldUseInputMixin {
  @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true, require = 1)
  private void ellice$bridgeBlockUse(CallbackInfo callback) {
    if (CoreAutoSprintRequestedClient.scaffoldOwnsBlockUse()) {
      callback.cancel();
    }
  }
}
