package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.ImplHideVignetteService;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiOverlayMixin {
   @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
   private void ellice$clearVignette(GuiGraphicsExtractor context, Entity entity, CallbackInfo ci) {
      if (ImplHideVignetteService.hideVignette()) {
         ci.cancel();
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void ellice$clearTextureOverlay(GuiGraphicsExtractor context, Identifier texture, float alpha, CallbackInfo ci) {
      if (ImplHideVignetteService.hideTextureOverlay(texture)) {
         ci.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void ellice$clearPortal(GuiGraphicsExtractor context, float alpha, CallbackInfo ci) {
      if (ImplHideVignetteService.hidePortal()) {
         ci.cancel();
      }
   }

   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void ellice$clearNausea(GuiGraphicsExtractor context, float alpha, CallbackInfo ci) {
      if (ImplHideVignetteService.hideNausea()) {
         ci.cancel();
      }
   }

   @Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
   private void ellice$clearSpyglass(GuiGraphicsExtractor context, float scale, CallbackInfo ci) {
      if (ImplHideVignetteService.hideSpyglass()) {
         ci.cancel();
      }
   }
}
