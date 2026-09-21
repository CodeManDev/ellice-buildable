package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.esp.EspPrepareService;
import dev.felix.ellice.feature.nametags.NametagsPrepareService;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NametagMixin {
   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void ellice$replaceNametag(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
      if (NametagsPrepareService.suppressesVanilla() || NametagsPrepareService.replaces(entity.getUUID()) || EspPrepareService.replaces(entity.getUUID())) {
         state.nameTag = null;
      }
   }
}
