package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.SoupInventoryBridge;
import dev.felix.ellice.compat.CompatRememberInteractionService;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryInteractionMixin {
   @Inject(method = "slotClicked", at = @At("HEAD"))
   private void ellice$manualInventoryInput(CallbackInfo ci) {
      CompatRememberInteractionService.manualInput();
      SoupInventoryBridge.manualInput();
   }
}
