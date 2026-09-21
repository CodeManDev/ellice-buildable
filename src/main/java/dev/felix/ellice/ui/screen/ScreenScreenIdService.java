package dev.felix.ellice.ui.screen;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;

public final class ScreenScreenIdService {
   private final ScreenDelayQueue screenDelayQueue;
   private final ThemeIsSetService values;
   private final String text2;
   private float value = 0.016666668F;

   ScreenScreenIdService(ScreenDelayQueue currentScreenDelayQueue, ThemeIsSetService themeIsSet, String text) {
      this.screenDelayQueue = currentScreenDelayQueue;
      this.values = themeIsSet;
      this.text2 = text;
   }

   public String screenId() {
      return this.text2;
   }

   public ThemeIsSetService theme() {
      return this.values;
   }

   public float deltaTime() {
      return this.value;
   }

   void beginFrame(float currentValue) {
      if (!Float.isFinite(currentValue) || currentValue < 0.0F) {
         currentValue = 0.0F;
      }

      this.value = Math.min(currentValue, 0.1F);
   }

   public void close() {
      this.screenDelayQueue.close();
   }

   public void open(String text) {
      this.screenDelayQueue.open(text);
   }

   public SceneTextService text(String currentText, float value, int currentValue) {
      return new SceneTextService().text(currentText).fontSize(value).color(currentValue);
   }

   public SceneCornerRadiusService button(String currentText, Runnable runnable) {
      SceneCornerRadiusService currentSize = new SceneCornerRadiusService()
         .size(-1.0F, 38.0F)
         .cornerRadius(10.0F)
         .backgroundColor(-266856414)
         .hoverBackground(-265672138)
         .pressBackground(-267448554)
         .border(0.5F, 905969663)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(runnable);
      currentSize.addChild(this.text(currentText, 9.0F, -117440513));
      return currentSize;
   }
}
