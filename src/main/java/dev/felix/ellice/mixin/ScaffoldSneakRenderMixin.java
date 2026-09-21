package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatHiddenService;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public abstract class ScaffoldSneakRenderMixin {
  @Inject(method = "extractHumanoidRenderState", at = @At("TAIL"), require = 1)
  private static void ellice$hideAutomaticSneak(
      LivingEntity entity,
      HumanoidRenderState state,
      float tickDelta,
      ItemModelResolver items,
      CallbackInfo ci) {
    if (CompatHiddenService.hidden(entity)) {
      state.isCrouching = false;
      state.pose = Pose.STANDING;
    }
  }
}
