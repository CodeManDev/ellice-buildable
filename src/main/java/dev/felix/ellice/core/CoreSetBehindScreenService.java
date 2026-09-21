package dev.felix.ellice.core;

import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.screen.ScreenDelayQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class CoreSetBehindScreenService extends Screen {
   private Screen screen2;

   public CoreSetBehindScreenService() {
      super(Component.empty());
   }

   public void setBehindScreen(Screen screen) {
      this.screen2 = screen;
      if (screen != null) {
         Minecraft minecraft = Minecraft.getInstance();

         try {
            screen.init(minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
         } catch (Throwable exception) {
         }
      }
   }

   public Screen getBehindScreen() {
      return this.screen2;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, int y, int width, float height) {
      if (this.screen2 != null) {
         try {
            this.screen2.extractRenderState(guiGraphicsExtractor, -9999, -9999, height);
         } catch (Throwable exception) {
         }
      }
   }

   public void resize(int value, int currentValue) {
      super.resize(value, currentValue);
      if (this.screen2 != null) {
         try {
            this.screen2.resize(value, currentValue);
         } catch (Throwable exception) {
         }
      }
   }

   public boolean keyPressed(KeyEvent keyEvent) {
      int value = keyEvent.key();
      int currentValue = keyEvent.modifiers();
      if (!CoreIsInitializedHandler.isReady()) {
         return false;
      }

      CoreIsInitializedHandler coreIsInitialized = CoreIsInitializedHandler.get();
      SceneDtService sceneDt = coreIsInitialized.scene();
      ScreenDelayQueue screenDelayQueue = coreIsInitialized.screens();
      if (screenDelayQueue == null || !screenDelayQueue.isActive() || value != 256 && value != 344) {
         if (screenDelayQueue != null && screenDelayQueue.navigationKeyPressed(value, currentValue)) {
            return true;
         }

         if (sceneDt.hasFocusedInput()) {
            int nextValue = (currentValue & 2) == 0 && (currentValue & 8) == 0 ? 0 : 1;
            if (nextValue != 0) {
               switch (value) {
                  case 65:
                     sceneDt.selectAll();
                     return true;
                  case 67:
                     String text = sceneDt.copy();
                     if (text != null) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(text);
                     }

                     return true;
                  case 86:
                     sceneDt.paste(Minecraft.getInstance().keyboardHandler.getClipboard());
                     return true;
                  case 88:
                     String currentText = sceneDt.copy();
                     if (currentText != null) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(currentText);
                     }

                     sceneDt.cut();
                     return true;
               }
            }

            if (sceneDt.keyPressed(value, currentValue)) {
               return true;
            }
         }

         if (screenDelayQueue != null && screenDelayQueue.isActive()) {
            return screenDelayQueue.keyPressed(value, currentValue);
         } else if (value != 256 && value != 344) {
            coreIsInitialized.pluginLoader().onKey(value, 1);
            return true;
         } else {
            this.onClose();
            return true;
         }
      } else {
         return screenDelayQueue.keyPressed(value, currentValue);
      }
   }

   public boolean charTyped(CharacterEvent characterEvent) {
      if (CoreIsInitializedHandler.isReady()) {
         int value = characterEvent.codepoint();
         if (value <= 65535) {
            CoreIsInitializedHandler.get().scene().charTyped((char)value);
         }
      }

      return true;
   }

   public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean enabled) {
      return true;
   }

   public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
      return true;
   }

   public boolean mouseScrolled(double x, double y, double width, double height) {
      if (CoreIsInitializedHandler.isReady()) {
         CoreIsInitializedHandler.get().scene().mouseScroll((float)height);
      }

      return true;
   }
}
