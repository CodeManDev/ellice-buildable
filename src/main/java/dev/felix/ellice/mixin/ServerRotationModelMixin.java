package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.rotation.RotationPublishService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class ServerRotationModelMixin {
  @Unique private boolean ellice$restore;
  @Unique private float ellice$yaw;
  @Unique private float ellice$pitch;
  @Unique private float ellice$oldYaw;
  @Unique private float ellice$oldPitch;
  @Unique private float ellice$headYaw;
  @Unique private float ellice$oldHeadYaw;
  @Unique private float ellice$bodyYaw;
  @Unique private float ellice$oldBodyYaw;

  @Inject(
      method =
          "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("HEAD"))
  private void ellice$applyServerModelRotation(
      LivingEntity entity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
    this.ellice$restore = false;
    if (entity == Minecraft.getInstance().player) {
      RotationPublishService.Snapshot snapshot = RotationPublishService.snapshot().orElse(null);
      if (snapshot != null) {
        this.ellice$yaw = entity.getYRot();
        this.ellice$pitch = entity.getXRot();
        this.ellice$oldYaw = entity.yRotO;
        this.ellice$oldPitch = entity.xRotO;
        this.ellice$headYaw = entity.yHeadRot;
        this.ellice$oldHeadYaw = entity.yHeadRotO;
        this.ellice$bodyYaw = entity.yBodyRot;
        this.ellice$oldBodyYaw = entity.yBodyRotO;
        this.ellice$restore = true;
        entity.setYRot((float) snapshot.current().yaw());
        entity.setXRot((float) snapshot.current().pitch());
        entity.yRotO = (float) snapshot.previous().yaw();
        entity.xRotO = (float) snapshot.previous().pitch();
        entity.setYHeadRot((float) snapshot.current().yaw());
        entity.yHeadRotO = (float) snapshot.previous().yaw();
        entity.setYBodyRot((float) snapshot.bodyYaw());
        entity.yBodyRotO = (float) snapshot.previousBodyYaw();
      }
    }
  }

  @Inject(
      method =
          "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("RETURN"))
  private void ellice$restoreClientModelRotation(
      LivingEntity entity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
    if (this.ellice$restore) {
      this.ellice$restore = false;
      entity.setYRot(this.ellice$yaw);
      entity.setXRot(this.ellice$pitch);
      entity.yRotO = this.ellice$oldYaw;
      entity.xRotO = this.ellice$oldPitch;
      entity.setYHeadRot(this.ellice$headYaw);
      entity.yHeadRotO = this.ellice$oldHeadYaw;
      entity.setYBodyRot(this.ellice$bodyYaw);
      entity.yBodyRotO = this.ellice$oldBodyYaw;
    }
  }
}
