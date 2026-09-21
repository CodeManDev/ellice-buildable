package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class GameRendererFovMixin {
   @Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true)
   private void ellice$smoothFov(float tickDelta, CallbackInfoReturnable<Float> cir) {
      if (CoreIsInitializedHandler.isReady()) {
         Float vanilla = (Float)cir.getReturnValue();
         if (vanilla != null) {
            float fov = CoreAutoSprintRequestedClient.applyWorldFov(vanilla);
            if (fov != vanilla) {
               cir.setReturnValue(fov);
            }
         }
      }
   }
}
