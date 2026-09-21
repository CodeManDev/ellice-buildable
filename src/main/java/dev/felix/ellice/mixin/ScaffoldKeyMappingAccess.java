package dev.felix.ellice.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface ScaffoldKeyMappingAccess {
   @Accessor("clickCount")
   int ellice$pendingClicks();
}
