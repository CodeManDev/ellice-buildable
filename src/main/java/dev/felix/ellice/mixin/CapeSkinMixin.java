package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatApplyService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class CapeSkinMixin {
  @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true, require = 1)
  private void ellice$localAnimatedCape(CallbackInfoReturnable<PlayerSkin> cir) {
    if ((Object) this == Minecraft.getInstance().player) {
      cir.setReturnValue(CompatApplyService.apply((PlayerSkin) cir.getReturnValue()));
    }
  }
}
