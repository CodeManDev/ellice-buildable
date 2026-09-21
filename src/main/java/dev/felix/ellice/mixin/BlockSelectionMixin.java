package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class BlockSelectionMixin {
   @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true, require = 1)
   private void ellice$selectionOutline(CallbackInfoReturnable<Boolean> callback) {
      if (CoreAutoSprintRequestedClient.customBlockOutline()) {
         callback.setReturnValue(false);
      }
   }
}
