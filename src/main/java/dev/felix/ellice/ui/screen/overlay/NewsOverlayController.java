package dev.felix.ellice.ui.screen.overlay;

import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.text.TextMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;

public final class NewsOverlayController {
   private final LayoutContainerNode sceneComponent4 = new LayoutContainerNode()
      .position(0.0F, 0.0F)
      .size(ScenePctService.pct(100.0F), ScenePctService.pct(100.0F))
      .direction(ScenePctService.Direction.ROW)
      .align(ScenePctService.Align.END)
      .justify(ScenePctService.Justify.END)
      .padding(0.0F, 14.0F, 14.0F, 0.0F)
      .layerBreak(true)
      .visible(false);
   private final SceneCornerRadiusService sceneCornerRadiusService;
   private final SceneCornerRadiusService sceneCornerRadiusService2;
   private final SceneTextService sceneTextService;
   private Runnable runnable;
   private boolean enabled;

   public NewsOverlayController(SceneDtService sceneDt, String text) {
      this.sceneComponent4.id("client.versionBadge");
      this.sceneCornerRadiusService = new SceneCornerRadiusService()
         .size(-1.0F, 30.0F)
         .cornerRadius(15.0F)
         .backgroundColor(-1831610142)
         .gradientEnd(-1968656966)
         .glass(true)
         .blur(26.0F)
         .glassSaturation(1.25F)
         .glassBrightness(0.025F)
         .glassContrast(1.06F)
         .glassRefraction(7.0F)
         .glassNoise(0.0025F)
         .border(0.8F, 1895825407)
         .secondaryBorderColor(671088640)
         .shadow(10.0F)
         .shadowColor(1610612736)
         .padding(0.0F, 12.0F, 0.0F, 12.0F)
         .gap(7.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(this::updateState);
      this.sceneCornerRadiusService
         .onHoverChange(
            item -> this.sceneCornerRadiusService.animate("scale", item ? 1.035F : 1.0F, SceneEaseHandler.Spring.SNAPPY)
         );
      this.sceneCornerRadiusService
         .addChild(
            new SceneTextService("ellice v" + (text == null ? "0.0.0" : text), 7.5F, -870836190)
               .fontVariant(TextMode.BOLD)
         );
      this.sceneCornerRadiusService2 = new SceneCornerRadiusService()
         .size(6.0F, 6.0F)
         .cornerRadius(3.0F)
         .backgroundColor(-10262799)
         .visible(false);
      this.sceneTextService = new SceneTextService("What's New", 7.0F, -1139271646)
         .fontVariant(TextMode.BOLD)
         .visible(false);
      this.sceneCornerRadiusService.addChild(this.sceneCornerRadiusService2);
      this.sceneCornerRadiusService.addChild(this.sceneTextService);
      this.sceneComponent4.addChild(this.sceneCornerRadiusService);
      sceneDt.root().addChild(this.sceneComponent4);
   }

   public void onActivate(Runnable currentRunnable) {
      this.runnable = currentRunnable;
   }

   public boolean isVisible() {
      return this.sceneComponent4.visible;
   }

   public boolean hasNews() {
      return this.enabled;
   }

   public void setHasNews(boolean currentEnabled) {
      this.enabled = currentEnabled;
      this.sceneCornerRadiusService2.visible(currentEnabled);
      this.sceneTextService.visible(currentEnabled);
      this.sceneCornerRadiusService2.cancelAnimation("opacity");
      if (currentEnabled) {
         this.sceneCornerRadiusService2
            .loop("opacity", 0.35F, 1.0F, SceneEaseHandler.Tween.ease(0.75F), true);
      } else {
         this.sceneCornerRadiusService2.opacity(1.0F);
      }
   }

   public void update() {
      Screen currentScreen = Minecraft.getInstance().screen;
      this.sceneComponent4.visible(currentScreen instanceof TitleScreen || currentScreen instanceof JoinMultiplayerScreen || currentScreen instanceof SelectWorldScreen);
   }

   private void updateState() {
      if (this.runnable != null) {
         this.runnable.run();
      }
   }
}
