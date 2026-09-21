package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CompatProfileTracker;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class AutoToolInventoryMixin {
  @Inject(
      method = {"handleContainerInput", "piercingAttack"},
      at = @At("HEAD"))
  private void ellice$releaseForInventory(CallbackInfo ci) {
    CompatProfileTracker.beforeInteraction();
    CompatConfigureService.beforeInteraction();
    CompatOptionsTracker.beforeInteraction();
  }
}
