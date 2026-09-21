package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.NoFogModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
   @Inject(method = "setupFog", at = @At("RETURN"))
   private void ellice$noFog(
      Camera camera,
      int renderDistance,
      DeltaTracker deltaTracker,
      float darkenWorldAmount,
      ClientLevel level,
      CallbackInfoReturnable<FogData> cir
   ) {
      FogData fog = (FogData)cir.getReturnValue();
      if (NoFogModule.isActive() && fog != null) {
         if (NoFogModule.affectsLiquids() || !ellice$isLiquidFog(camera.getFluidInCamera())) {
            fog.environmentalStart = NoFogModule.fogStart();
            fog.environmentalEnd = NoFogModule.fogEnd();
            fog.renderDistanceStart = NoFogModule.fogStart();
            fog.renderDistanceEnd = NoFogModule.fogEnd();
            fog.skyEnd = NoFogModule.fogEnd();
            fog.cloudEnd = NoFogModule.fogEnd();
         }
      }
   }

   private static boolean ellice$isLiquidFog(FogType type) {
      return type == FogType.WATER || type == FogType.LAVA || type == FogType.POWDER_SNOW;
   }
}
