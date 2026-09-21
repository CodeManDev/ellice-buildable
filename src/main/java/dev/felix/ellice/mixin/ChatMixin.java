package dev.felix.ellice.mixin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ChatMixin {
   @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
   private void onChat(String message, CallbackInfo ci) {
      if (CoreIsInitializedHandler.isReady()) {
         if (CoreIsInitializedHandler.get().commands().execute(message)) {
            ci.cancel();
         } else {
            EventAttackInputService.Chat event = new EventAttackInputService.Chat(message);
            CoreIsInitializedHandler.get().bus().post(EventAttackInputService.CHAT, event);
            if (event.isCancelled()) {
               ci.cancel();
            }
         }
      }
   }
}
