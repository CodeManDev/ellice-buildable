package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreAutoSprintRequestedClient;
import dev.felix.ellice.feature.movement.MovementAvailableService;
import dev.felix.ellice.feature.scaffold.ScaffoldRemapService;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KeyboardInput.class)
public abstract class ScaffoldKeyboardInputMixin {
   @Shadow
   @Final
   private Options options;

   @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"), require = 7, expect = 7)
   private boolean ellice$scaffoldInput(KeyMapping mapping) {
      if (mapping == this.options.keySprint && CoreAutoSprintRequestedClient.suppressCombatSprint()) {
         return false;
      }

      boolean requested = MovementAvailableService.requested(mapping) || CoreAutoSprintRequestedClient.autoSprintRequested(mapping);
      PlacementOverrideBus.Override override = PlacementOverrideBus.current().orElse(null);
      if (override == null) {
         return requested;
      }

      ScaffoldRemapService.Input movement = override.movement();
      if (override.overrideDirections()) {
         if (mapping == this.options.keyUp) {
            return movement.forward();
         }

         if (mapping == this.options.keyDown) {
            return movement.backward();
         }

         if (mapping == this.options.keyLeft) {
            return movement.left();
         }

         if (mapping == this.options.keyRight) {
            return movement.right();
         }
      }

      if (mapping == this.options.keyShift) {
         return override.effectiveSneak();
      } else if (mapping == this.options.keyJump && override.overrideJump()) {
         return movement.jump();
      } else if (mapping == this.options.keySprint && override.suppressSprint()) {
         return false;
      } else {
         return mapping == this.options.keySprint && override.overrideSprint() ? movement.sprint() : requested;
      }
   }
}
