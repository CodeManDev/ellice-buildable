package dev.felix.ellice.mixin;

import com.mojang.blaze3d.opengl.DirectStateAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlDevice")
public interface GlDeviceAccessor {
   @Invoker("directStateAccess")
   DirectStateAccess ellice$directStateAccess();
}
