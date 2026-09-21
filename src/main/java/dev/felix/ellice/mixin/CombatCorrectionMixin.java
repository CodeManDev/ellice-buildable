package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class CombatCorrectionMixin {
   @Inject(method = {"handleMovePlayer", "handleRotatePlayer"}, at = @At("RETURN"))
   private void ellice$afterAppliedCorrection(CallbackInfo ci) {
      CombatOwnsService.reset();
      RotationObserveService.invalidateVelocity();
      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get().bus().post(EventAttackInputService.PLAYER_POSITION_CORRECTED, EventAttackInputService.PlayerPositionCorrected.INSTANCE);
      }
   }
}
