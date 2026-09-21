package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class ScaffoldFovMixin {
   @Redirect(
      method = "getFieldOfViewModifier",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/AbstractClientPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"
      ),
      require = 1,
      expect = 1
   )
   private double ellice$stableScaffoldFov(AbstractClientPlayer player, Holder<Attribute> attribute) {
      return CoreAutoSprintRequestedClient.scaffoldFovSpeed(player, attribute);
   }
}
