package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatCanStartService;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ShieldUseInputMixin {
   @Inject(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0),
      require = 1
   )
   private void ellice$maintainOwnedShield(CallbackInfo ci) {
      CompatCanStartService.maintain((Minecraft)(Object)this);
   }
}
