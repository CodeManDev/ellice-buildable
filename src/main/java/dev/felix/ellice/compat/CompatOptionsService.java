package dev.felix.ellice.compat;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;

public final class CompatOptionsService {
   private CompatOptionsService() {
   }

   public static void options() {
      Minecraft minecraft = Minecraft.getInstance();
      CoreIsInitializedHandler.get().screens().closeForNavigation();
      minecraft.setScreen(new OptionsScreen(new TitleScreen(), minecraft.options, false));
   }

   public static void createWorld() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         CoreIsInitializedHandler.get().screens().closeForNavigation();
         CreateWorldScreen.openFresh(minecraft, () -> minecraft.setScreen(new TitleScreen()));
      }
   }

   public static void quit() {
      Minecraft.getInstance().stop();
   }
}
