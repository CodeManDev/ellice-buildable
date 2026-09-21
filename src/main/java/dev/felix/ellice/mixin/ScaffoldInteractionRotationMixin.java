package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.scaffold.ScaffoldForActorService;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ScaffoldInteractionRotationMixin {
  @Inject(method = "getYRot()F", at = @At("HEAD"), cancellable = true, require = 1)
  private void ellice$interactionYaw(CallbackInfoReturnable<Float> callback) {
    RotationData rotation = ScaffoldForActorService.forActor(this);
    if (rotation != null) {
      callback.setReturnValue((float) rotation.yaw());
    }
  }

  @Inject(method = "getXRot()F", at = @At("HEAD"), cancellable = true, require = 1)
  private void ellice$interactionPitch(CallbackInfoReturnable<Float> callback) {
    RotationData rotation = ScaffoldForActorService.forActor(this);
    if (rotation != null) {
      callback.setReturnValue((float) rotation.pitch());
    }
  }
}
