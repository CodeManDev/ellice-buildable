package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatEnableService;
import dev.felix.ellice.compat.CompatBeginTickService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class TickMixin {
   @Inject(method = "tick", at = @At("HEAD"))
   private void onTick(CallbackInfo ci) {
      if (CoreIsInitializedHandler.isReady()) {
         CompatBeginTickService.beginTick();
         CompatEnableService.beginClientTick();
         CoreIsInitializedHandler.get().bus().post(EventAttackInputService.TICK, EventAttackInputService.Tick.INSTANCE);
      }
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;handleKeybinds()V", shift = Shift.BEFORE))
   private void beforeGameplayInput(CallbackInfo ci) {
      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get().bus().post(EventAttackInputService.GAMEPLAY_INPUT, EventAttackInputService.GameplayInput.INSTANCE);
      }
   }

   @Inject(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0, shift = Shift.BEFORE)
   )
   private void beforeAttackInput(CallbackInfo ci) {
      if (CoreIsInitializedHandler.isReady()) {
         CompatBeginTickService.beginInput();
         CoreIsInitializedHandler.get().bus().post(EventAttackInputService.ATTACK_INPUT, EventAttackInputService.AttackInput.INSTANCE);
      }
   }

   @Inject(method = "handleKeybinds", at = @At("RETURN"))
   private void afterGameplayInput(CallbackInfo ci) {
      CompatBeginTickService.endInput();
   }
}
