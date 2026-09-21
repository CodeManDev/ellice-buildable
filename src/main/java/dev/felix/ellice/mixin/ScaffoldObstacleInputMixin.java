package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatExecuteService;
import dev.felix.ellice.compat.CompatPrepareService;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class ScaffoldObstacleInputMixin {
  @Redirect(
      method = "handleKeybinds",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"),
      require = 1)
  private void ellice$clearScaffoldObstacle(Minecraft minecraft, boolean attacking) {
    if (!CompatExecuteService.ownsMiningInput()) {
      CompatPrepareService.continueAttack(minecraft, attacking);
    }
  }
}
