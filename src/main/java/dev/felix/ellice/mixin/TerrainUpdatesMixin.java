package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.terrain.TerrainSubscribeService;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class TerrainUpdatesMixin {
   @Inject(method = "setSectionDirty(IIIZ)V", at = @At("HEAD"))
   private void ellice$dirtyTerrain(int x, int y, int z, boolean playerChanged, CallbackInfo ci) {
      TerrainSubscribeService.dirty(x, y, z);
   }

   @Inject(method = "allChanged", at = @At("HEAD"))
   private void ellice$reloadTerrain(CallbackInfo ci) {
      TerrainSubscribeService.reload();
   }
}
