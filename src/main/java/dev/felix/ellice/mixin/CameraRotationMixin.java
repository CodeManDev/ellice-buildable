package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatActiveService;
import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraRotationMixin {
   @Shadow
   @Final
   private Quaternionf rotation;
   @Unique
   private float[] ellice$combatPose;

   @ModifyVariable(method = "setRotation", at = @At("HEAD"), ordinal = 0, argsOnly = true)
   private float ellice$combatCameraYaw(float yaw) {
      this.ellice$combatPose = CompatActiveService.sample();
      return this.ellice$combatPose != null ? this.ellice$combatPose[0] : yaw;
   }

   @ModifyVariable(method = "setRotation", at = @At("HEAD"), ordinal = 1, argsOnly = true)
   private float ellice$combatCameraPitch(float pitch) {
      return this.ellice$combatPose != null ? this.ellice$combatPose[1] : pitch;
   }

   @Inject(method = "setRotation", at = @At("TAIL"))
   private void ellice$appendLean(float yaw, float pitch, CallbackInfo ci) {
      if (CoreAutoSprintRequestedClient.cameraLeanActive()) {
         float roll = CoreAutoSprintRequestedClient.currentLeanRollRadians();
         if (roll != 0.0F) {
            this.rotation.rotateZ(roll);
         }
      }
   }
}
