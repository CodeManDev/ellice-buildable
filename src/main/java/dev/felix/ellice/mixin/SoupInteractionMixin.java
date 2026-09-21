package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatReleaseTracker;
import dev.felix.ellice.compat.CompatActivateService;
import dev.felix.ellice.compat.SoupInventoryBridge;
import dev.felix.ellice.compat.PearlThrowController;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class SoupInteractionMixin {
   @Inject(method = "handlePlaceRecipe", at = @At("HEAD"))
   private void ellice$soupRecipeInput(CallbackInfo ci) {
      SoupInventoryBridge.recipeInput();
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void ellice$soupAttack(CallbackInfo ci) {
      if (SoupInventoryBridge.handBusy() || CompatActivateService.handBusy() || CompatReleaseTracker.handBusy() || PearlThrowController.handBusy()) {
         ci.cancel();
      }
   }

   @Inject(method = {"useItem", "useItemOn"}, at = @At("HEAD"), cancellable = true)
   private void ellice$soupUse(CallbackInfoReturnable<InteractionResult> cir) {
      if (SoupInventoryBridge.handBusy() || CompatActivateService.handBusy() || CompatReleaseTracker.handBusy() || PearlThrowController.handBusy()) {
         cir.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void ellice$soupMining(CallbackInfoReturnable<Boolean> cir) {
      if (SoupInventoryBridge.handBusy() || CompatActivateService.handBusy() || CompatReleaseTracker.handBusy() || PearlThrowController.handBusy()) {
         cir.setReturnValue(false);
      }
   }
}
