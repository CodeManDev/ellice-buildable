package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.screen.ScreenDelayQueue;
import net.minecraft.client.Minecraft;

public final class CompatInterceptService {
   private static final CompatInterceptService.State state = new CompatInterceptService.State();

   private CompatInterceptService() {
   }

   public static boolean intercept(long longValue, int value, int currentValue, int nextValue) {
      if (!CoreIsInitializedHandler.isReady()) {
         return false;
      } else {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null && longValue == CompatAdapterService.windowHandle(minecraft)) {
            ScreenDelayQueue screenDelayQueue = CoreIsInitializedHandler.get().screens();
            int previousValue = screenDelayQueue != null && screenDelayQueue.capturesKeybindInput() ? 1 : 0;
            return state.event(value, currentValue, (previousValue != 0), () -> screenDelayQueue.keyPressed(value, nextValue));
         } else {
            return false;
         }
      }
   }

   static final class State {
      private final boolean[] boolean2 = new boolean[349];

      boolean event(int index, int value, boolean enabled, Runnable runnable) {
         int currentLength = index >= 0 && index < this.boolean2.length ? 1 : 0;
         if (enabled) {
            if (currentLength != 0) {
               this.boolean2[index] = value != 0;
            }

            if (value == 1) {
               runnable.run();
            }

            return true;
         } else {
            if (currentLength == 0 || !this.boolean2[index]) {
               return false;
            }

            if (value == 1) {
               this.boolean2[index] = false;
               return false;
            }

            if (value == 0) {
               this.boolean2[index] = false;
            }

            return true;
         }
      }
   }
}

