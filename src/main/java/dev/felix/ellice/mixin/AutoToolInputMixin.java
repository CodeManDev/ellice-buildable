package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.compat.CompatConfigureService;
import dev.felix.ellice.compat.CompatOptionsTracker;
import dev.felix.ellice.compat.CombatRotationOverride;
import dev.felix.ellice.compat.CompatBeginTickService;
import dev.felix.ellice.compat.PearlThrowController;
import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class AutoToolInputMixin {
   @Inject(method = "startAttack", at = @At("HEAD"))
   private void ellice$releaseForAttack(CallbackInfoReturnable<Boolean> cir) {
      if (!CompatBeginTickService.blocking()) {
         CompatProfileTracker.beforeInteraction();
         CompatConfigureService.beforeAttack();
         CompatOptionsTracker.beforeInteraction();
      }
   }

   @WrapOperation(
      method = "startAttack",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
      )
   )
   private boolean ellice$startMining(MultiPlayerGameMode gameMode, BlockPos pos, Direction face, Operation<Boolean> original) {
      return CompatConfigureService.mine(pos, () -> (Boolean)original.call(new Object[]{gameMode, pos, face}));
   }

   @WrapOperation(
      method = "continueAttack",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
      )
   )
   private boolean ellice$continueMining(MultiPlayerGameMode gameMode, BlockPos pos, Direction face, Operation<Boolean> original) {
      CompatOptionsTracker.beforeInteraction();
      return CompatConfigureService.mine(pos, () -> (Boolean)original.call(new Object[]{gameMode, pos, face}));
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean ellice$manualHandAction(KeyMapping key, Operation<Boolean> original) {
      CombatRotationOverride.cancelForManualInteraction(key);
      boolean consumed = (Boolean)original.call(new Object[]{key});
      CompatConfigureService.consumed(key, consumed);
      CompatProfileTracker.consumed(key, consumed);
      CompatOptionsTracker.consumed(key, consumed);
      PearlThrowController.consumed(key, consumed);
      return consumed;
   }

   @Inject(method = "setScreen", at = @At("HEAD"))
   private void ellice$releaseForScreen(Screen screen, CallbackInfo ci) {
      if (screen != null) {
         CompatProfileTracker.beforeInteraction();
         CompatConfigureService.beforeInteraction();
         CompatOptionsTracker.beforeInteraction();
         PearlThrowController.manualInput();
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"))
   private void ellice$releaseForUse(CallbackInfo ci) {
      if (!CoreAutoSprintRequestedClient.scaffoldOwnsBlockUse() && !CompatBeginTickService.blocking()) {
         CompatProfileTracker.beforeInteraction();
         CompatConfigureService.beforeInteraction();
         CompatOptionsTracker.beforeInteraction();
      }
   }
}
