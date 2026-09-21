package dev.felix.ellice.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felix.ellice.compat.CompatBlockingService;
import dev.felix.ellice.render.RenderMatrixService;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class SwordBlockAnimationMixin {
  @Shadow
  public abstract void renderItem(
      LivingEntity livingEntity,
      ItemStack itemStack,
      ItemDisplayContext itemDisplayContext,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int value);

  @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
  private void ellice$legacySword(
      AbstractClientPlayer player,
      float partialTick,
      float pitch,
      InteractionHand hand,
      float swing,
      ItemStack item,
      float equip,
      PoseStack pose,
      SubmitNodeCollector collector,
      int light,
      CallbackInfo ci) {
    if (CompatBlockingService.hideShield(player, hand, item)) {
      ci.cancel();
    } else if (CompatBlockingService.sword(player, hand, item)) {
      boolean right = player.getMainArm() == HumanoidArm.RIGHT;
      pose.pushPose();

      try {
        RenderMatrixService.apply(pose, player.getMainArm(), equip, swing);
        this.renderItem(
            player,
            item,
            right
                ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
            pose,
            collector,
            light);
      } finally {
        pose.popPose();
      }

      ci.cancel();
    }
  }
}
