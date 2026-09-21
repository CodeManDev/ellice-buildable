package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.ImplFireOverlayYService;
import dev.felix.ellice.module.impl.SmallTotemModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
  @Shadow private ItemStack itemActivationItem;

  @ModifyConstant(method = "renderFire", constant = @Constant(floatValue = -0.3F))
  private static float ellice$lowFireY(float vanilla) {
    return ImplFireOverlayYService.fireOverlayY(vanilla);
  }

  @ModifyConstant(method = "renderFire", constant = @Constant(floatValue = 0.9F))
  private static float ellice$lowFireAlpha(float vanilla) {
    return ImplFireOverlayYService.fireOverlayAlpha(vanilla);
  }

  @ModifyConstant(method = "renderItemActivationAnimation", constant = @Constant(floatValue = 0.8F))
  private float ellice$smallTotem(float vanilla) {
    if (!SmallTotemModule.isActive()) {
      return vanilla;
    } else {
      return this.itemActivationItem != null && this.itemActivationItem.is(Items.TOTEM_OF_UNDYING)
          ? SmallTotemModule.totemActivationScale(vanilla)
          : vanilla;
    }
  }
}
