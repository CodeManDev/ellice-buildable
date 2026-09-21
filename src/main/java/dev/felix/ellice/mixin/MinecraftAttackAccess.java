package dev.felix.ellice.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAttackAccess {
   @Invoker("startAttack")
   boolean ellice$startAttack();

   @Invoker("continueAttack")
   void ellice$continueAttack(boolean enabled);
}
