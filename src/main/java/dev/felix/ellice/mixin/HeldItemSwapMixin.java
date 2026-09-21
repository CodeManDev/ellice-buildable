package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemInHandRenderer.class)
public class HeldItemSwapMixin {
  @ModifyConstant(
      method = "tick",
      constant = {@Constant(floatValue = 0.4F), @Constant(floatValue = -0.4F)})
  private float ellice$smoothSwap(float orig) {
    float magnitude = CoreAutoSprintRequestedClient.itemSwapClamp();
    return orig < 0.0F ? -magnitude : magnitude;
  }
}
