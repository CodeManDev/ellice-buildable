package dev.felix.ellice.mixin;

import dev.felix.ellice.feature.speed.SpeedActivateService;
import dev.felix.ellice.feature.timer.TimerStateController;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.client.DeltaTracker.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Timer.class)
public abstract class SpeedTimerMixin {
  @Redirect(
      method = "advanceGameTime",
      at =
          @At(
              value = "INVOKE",
              target = "Lit/unimi/dsi/fastutil/floats/FloatUnaryOperator;apply(F)F"))
  private float ellice$speedMspt(FloatUnaryOperator provider, float base) {
    return provider.apply(base)
        / TimerStateController.combine(SpeedActivateService.timerMultiplier());
  }
}
