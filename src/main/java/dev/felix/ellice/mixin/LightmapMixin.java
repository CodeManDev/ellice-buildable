package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.FullbrightModule;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lightmap.class)
public class LightmapMixin {
   @Inject(method = "render", at = @At("HEAD"))
   private void ellice$fullbright(LightmapRenderState state, CallbackInfo ci) {
      if (FullbrightModule.isActive() && state != null) {
         float amount = FullbrightModule.brightness();
         state.needsUpdate = true;
         state.blockFactor = Math.max(state.blockFactor, amount);
         state.skyFactor = Math.max(state.skyFactor, amount);
         state.brightness = Math.max(state.brightness, amount);
         state.nightVisionEffectIntensity = Math.max(state.nightVisionEffectIntensity, amount);
         if (FullbrightModule.reducesDarkness()) {
            float keep = 1.0F - amount;
            state.darknessEffectScale *= keep;
            state.bossOverlayWorldDarkening = state.bossOverlayWorldDarkening * Math.max(0.0F, 1.0F - amount * 0.85F);
         }
      }
   }
}
