package dev.felix.ellice.mixin;

import dev.felix.ellice.compat.CompatHiddenService;
import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraEyeHeightMixin {
  @Shadow private float eyeHeight;
  @Shadow private float eyeHeightOld;
  @Unique private boolean ellice$eyeHeightSpringPrimed;
  @Unique private float ellice$lastSpringEyeHeight;

  @Redirect(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F"),
      require = 1)
  private float ellice$visualEyeHeight(Entity entity) {
    return CompatHiddenService.eyeHeight(entity);
  }

  @Inject(method = "tick", at = @At("TAIL"))
  private void ellice$springEyeHeight(CallbackInfo ci) {
    if (!CoreAutoSprintRequestedClient.smoothSneakActive()) {
      this.ellice$eyeHeightSpringPrimed = false;
    } else {
      Entity focused = this.ellice$focusedEntity();
      if (focused == null) {
        this.ellice$eyeHeightSpringPrimed = false;
      } else {
        float previous =
            this.ellice$eyeHeightSpringPrimed ? this.ellice$lastSpringEyeHeight : this.eyeHeight;
        float smoothed =
            CoreAutoSprintRequestedClient.advanceEyeHeightSpring(
                this.eyeHeight, CompatHiddenService.eyeHeight(focused));
        this.eyeHeightOld = previous;
        this.eyeHeight = smoothed;
        this.ellice$lastSpringEyeHeight = smoothed;
        this.ellice$eyeHeightSpringPrimed = true;
      }
    }
  }

  @Unique
  private Entity ellice$focusedEntity() {
    Object self = this;

    for (String methodName : new String[] {"entity", "getEntity"}) {
      try {
        if (self.getClass().getMethod(methodName).invoke(self) instanceof Entity entity) {
          return entity;
        }
      } catch (ReflectiveOperationException reflectiveOperationException) {
      }
    }

    return null;
  }
}
