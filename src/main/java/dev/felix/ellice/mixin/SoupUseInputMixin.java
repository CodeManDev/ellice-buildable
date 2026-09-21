package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatActivateService;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.SoupInventoryBridge;
import dev.felix.ellice.compat.CompatCanStartService;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class SoupUseInputMixin {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void ellice$soupSwing(CallbackInfoReturnable<Boolean> cir) {
      if (SoupInventoryBridge.handBusy() || CompatActivateService.handBusy()) {
         cir.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void ellice$soupBreaking(CallbackInfo ci) {
      if (SoupInventoryBridge.handBusy() || CompatActivateService.handBusy()) {
         ci.cancel();
      }
   }

   @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean ellice$holdSoup(KeyMapping key) {
      CombatRotationOverride.cancelForManualInteraction(key);
      return key.isDown()
         || key == Minecraft.getInstance().options.keyUse && (SoupInventoryBridge.holdUse() || CompatActivateService.holdUse() || CompatCanStartService.holdUse());
   }
}
