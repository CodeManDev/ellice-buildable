package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.impl.ImplRememberInteractionService;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class ChestStealerInteractionMixin {
   @Inject(method = "useItemOn", at = @At("RETURN"))
   private void ellice$rememberStorageUse(
      LocalPlayer player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir
   ) {
      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get()
            .modules()
            .get(ImplRememberInteractionService.class)
            .ifPresent(module -> module.rememberInteraction(hit, ((InteractionResult)cir.getReturnValue()).consumesAction()));
      }
   }
}
