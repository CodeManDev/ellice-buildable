package dev.felix.ellice.mixin;

import dev.felix.ellice.module.impl.ImplTimeOfDayService;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public abstract class TimeChangerMixin {
  @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
  private void ellice$time(Holder<WorldClock> clock, CallbackInfoReturnable<Long> ci) {
    if (clock.is(WorldClocks.OVERWORLD)) {
      ci.setReturnValue(ImplTimeOfDayService.displayedTime((Long) ci.getReturnValue()));
    }
  }
}
