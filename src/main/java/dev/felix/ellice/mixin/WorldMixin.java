package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationObserveService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class WorldMixin {
  @Inject(method = "updateLevelInEngines", at = @At("HEAD"))
  private void onWorldChange(ClientLevel world, CallbackInfo ci) {
    RotationObserveService.clear();
    CombatOwnsService.reset();
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get()
          .bus()
          .post(EventAttackInputService.WORLD, new EventAttackInputService.World(world));
    }
  }
}
