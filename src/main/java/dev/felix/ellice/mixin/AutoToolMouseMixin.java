package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.PearlThrowController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class AutoToolMouseMixin {
   @Inject(method = "onScroll", at = @At("HEAD"))
   private void ellice$scrollHand(long window, double horizontal, double vertical, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.screen == null && mc.mouseHandler.isMouseGrabbed() && (horizontal != 0.0 || vertical != 0.0)) {
         CompatProfileTracker.manualInput();
         CompatConfigureService.beforeInteraction();
         CompatOptionsTracker.beforeInteraction();
         PearlThrowController.manualInput();
      }
   }
}
