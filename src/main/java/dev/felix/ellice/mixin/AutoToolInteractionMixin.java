package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CompatProfileTracker;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class AutoToolInteractionMixin {
  @WrapMethod(method = "ensureHasSentCarriedItem")
  private void ellice$holdServerTool(Operation<Void> original) {
    CompatProfileTracker.synchronize(
        () -> CompatConfigureService.synchronize(() -> original.call(new Object[0])));
  }

  @WrapMethod(method = "stopDestroyBlock")
  private void ellice$abortBeforeRestore(Operation<Void> original) {
    CompatConfigureService.stop(() -> original.call(new Object[0]));
  }

  @Inject(
      method = {
        "attack",
        "releaseUsingItem",
        "handlePlaceRecipe",
        "handleCreativeModeItemAdd",
        "handleCreativeModeItemDrop"
      },
      at = @At("HEAD"))
  private void ellice$releaseHand(CallbackInfo ci) {
    CompatProfileTracker.beforeInteraction();
    CompatConfigureService.beforeInteraction();
    CompatOptionsTracker.beforeInteraction();
  }

  @Inject(
      method = {"useItem", "useItemOn", "interact"},
      at = @At("HEAD"))
  private void ellice$releaseUse(CallbackInfoReturnable<?> cir) {
    CompatProfileTracker.beforeInteraction();
    CompatConfigureService.beforeInteraction();
    CompatOptionsTracker.beforeInteraction();
  }
}
