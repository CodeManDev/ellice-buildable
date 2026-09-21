package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatContextReadyService;
import dev.felix.ellice.feature.rotation.RotationData;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MultiPlayerGameMode.class)
public abstract class BowUseRotationMixin {
   @ModifyArgs(
      method = "lambda$useItem$0",
      remap = false,
      at = @At(
         value = "INVOKE",
         remap = true,
         target = "Lnet/minecraft/network/protocol/game/ServerboundUseItemPacket;<init>(Lnet/minecraft/world/InteractionHand;IFF)V"
      ),
      require = 1
   )
   @Dynamic("Vanilla item-use prediction lambda; name differs between mapped versions")
   private void ellice$bowUseRotation(Args args) {
      RotationData rotation = CompatContextReadyService.useRotation();
      if (rotation != null) {
         args.set(2, (float)rotation.yaw());
         args.set(3, (float)rotation.pitch());
      }
   }
}
