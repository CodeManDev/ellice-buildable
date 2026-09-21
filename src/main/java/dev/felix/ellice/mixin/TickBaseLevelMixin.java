package dev.felix.ellice.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.felix.ellice.compat.CompatEnableService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLevel.class)
public abstract class TickBaseLevelMixin {
   @WrapMethod(method = "tickNonPassenger")
   private void ellice$schedulePlayerTick(Entity entity, Operation<Void> original) {
      boolean local = entity == Minecraft.getInstance().player;
      if (local && CompatEnableService.beforeTick()) {
         entity.setOldPosAndRot();
      } else {
         original.call(new Object[]{entity});
         if (local && entity == Minecraft.getInstance().player) {
            CompatEnableService.afterTick();
         }
      }
   }
}
