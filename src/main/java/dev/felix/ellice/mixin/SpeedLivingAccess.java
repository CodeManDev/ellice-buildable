package dev.felix.ellice.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface SpeedLivingAccess {
   @Accessor("noJumpDelay")
   void ellice$speedJumpDelay(int value);
}
