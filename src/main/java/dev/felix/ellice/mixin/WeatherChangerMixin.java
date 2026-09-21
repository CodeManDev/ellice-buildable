package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.ImplRainService;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class WeatherChangerMixin {
   @Inject(method = "getRainLevel", at = @At("RETURN"), cancellable = true)
   private void ellice$rain(float partialTick, CallbackInfoReturnable<Float> ci) {
      if ((Object)this instanceof ClientLevel) {
         ci.setReturnValue(ImplRainService.rain((Float)ci.getReturnValue()));
      }
   }

   @Inject(method = "getThunderLevel", at = @At("RETURN"), cancellable = true)
   private void ellice$thunder(float partialTick, CallbackInfoReturnable<Float> ci) {
      if ((Object)this instanceof ClientLevel) {
         ci.setReturnValue(ImplRainService.thunder((Float)ci.getReturnValue()));
      }
   }
}
