package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.component.ComponentUnmountService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextFontDirectoryService;
import java.util.List;
import java.util.function.Consumer;

final class FONTComponent {
   private final ScenePctService<?> scenePctService;
   private final TextFontDirectoryService textFontDirectoryService;
   private final CompositorPushPresentationScaleService compositorPushPresentationScaleService;
   private final int count;
   private final Consumer<String> text2;
   private final LayoutContainerNode sceneComponent4;
   private final SceneTextService sceneTextService;
   private LayoutContainerNode sceneComponent42;
   private String text3 = "";
   private int count2 = -1;

   FONTComponent(ScenePctService<?> scenePct, TextFontDirectoryService textFontDirectory, CompositorPushPresentationScaleService compositorPushPresentationScale, int value, Consumer<String> consumer) {
      this.scenePctService = scenePct;
      this.textFontDirectoryService = textFontDirectory;
      this.compositorPushPresentationScaleService = compositorPushPresentationScale;
      this.count = value;
      this.text2 = consumer;
      this.sceneComponent4 = new LayoutContainerNode()
         .size(ScenePctService.pct(100.0F), -1.0F)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(5.0F);
      this.sceneComponent4.addChild(new SceneTextService("FONT", 7.0F, 1660944383));
      SceneCornerRadiusService currentSize = new SceneCornerRadiusService()
         .size(ScenePctService.pct(100.0F), 28.0F)
         .cornerRadius(8.0F)
         .backgroundColor(452984831)
         .gradientEnd(234881023)
         .border(0.7F, 687865855)
         .hoverBackground(654311423)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .padding(0.0F, 9.0F, 0.0F, 9.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN)
         .onClick(this::updateState);
      this.sceneTextService = new SceneTextService("Client", 8.5F, -385875969);
      currentSize.addChild(this.sceneTextService);
      currentSize.addChild(new SceneTextService("⌄", 9.0F, -2130706433));
      this.sceneComponent4.addChild(currentSize);
   }

   LayoutContainerNode node() {
      return this.sceneComponent4;
   }

   void setSelection(String currentText) {
      this.text3 = currentText != null && !"client".equals(currentText) ? currentText : "";
      TextFontDirectoryService.FontEntry fontEntry = this.textFontDirectoryService.find(this.text3);
      this.sceneTextService.text(fontEntry.display());
      this.sceneTextService.fontFamily(this.text3.isEmpty() ? null : this.text3);
      this.updateState4(fontEntry);
   }

   void tick() {
      if (this.sceneComponent42 != null && this.count2 != this.textFontDirectoryService.entries().size()) {
         this.updateState3();
      }

      TextFontDirectoryService.FontEntry fontEntry = this.textFontDirectoryService.find(this.text3);
      this.updateState4(fontEntry);
      this.sceneTextService
         .fontFamily(!this.text3.isEmpty() && this.compositorPushPresentationScaleService.isFontFamilyReady(this.text3) ? this.text3 : null);
   }

   void close() {
      if (this.sceneComponent42 != null) {
         ComponentUnmountService.unmount(this.scenePctService, this.sceneComponent42);
         this.sceneComponent42 = null;
      }
   }

   private void updateState() {
      if (this.sceneComponent42 == null) {
         this.updateState2();
      } else {
         this.close();
      }
   }

   private void updateState2() {
      this.textFontDirectoryService.scanUserFonts();
      this.updateState3();
   }

   private void updateState3() {
      if (this.sceneComponent42 != null) {
         ComponentUnmountService.unmount(this.scenePctService, this.sceneComponent42);
      }

      List items = this.textFontDirectoryService.entries();
      this.count2 = items.size();
      SceneCornerRadiusService currentSize = new SceneCornerRadiusService()
         .size(LayoutOperationHandler.px(280.0F), LayoutOperationHandler.px(310.0F))
         .maxWidth(LayoutOperationHandler.percent(100.0F))
         .maxHeight(LayoutOperationHandler.percent(100.0F))
         .cornerRadius(13.0F)
         .backgroundColor(-266856412)
         .gradientEnd(-267711726)
         .border(1.0F, 905969663)
         .shadow(20.0F)
         .blur(25.0F)
         .padding(10.0F)
         .gap(5.0F)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH);
      LayoutContainerNode nextSize = new LayoutContainerNode()
         .size(ScenePctService.pct(100.0F), 24.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN);
      nextSize.addChild(new SceneTextService("Choose font", 10.0F, -268435457));
      SceneCornerRadiusService previousSize = new SceneCornerRadiusService()
         .size(74.0F, 22.0F)
         .cornerRadius(7.0F)
         .backgroundColor(553648127)
         .hoverBackground(905969663)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .onClick(() -> CompatOpenUriService.openPath(this.textFontDirectoryService.fontDirectory()));
      previousSize.addChild(new SceneTextService("Add font…", 7.0F, -805306369));
      nextSize.addChild(previousSize);
      currentSize.addChild(nextSize);
      LayoutContainerNode sourceSize = new LayoutContainerNode()
         .flex(1.0F)
         .size(ScenePctService.pct(100.0F), -1.0F)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(2.0F)
         .scrollable(true)
         .clip(true)
         .scrollInset(4.0F);
      int targetSize = Math.min(items.size(), 160);

      for (int index = 0; index < targetSize; index++) {
         sourceSize.addChild(this.createSceneCornerRadiusService((TextFontDirectoryService.FontEntry)items.get(index)));
      }

      currentSize.addChild(sourceSize);
      this.sceneComponent42 = ComponentUnmountService.mountCentered(this.scenePctService, currentSize, 12.0F, this::close);
   }

   private SceneCornerRadiusService createSceneCornerRadiusService(TextFontDirectoryService.FontEntry fontEntry) {
      boolean enabled = fontEntry.key().equals(this.text3);
      SceneCornerRadiusService currentSize = new SceneCornerRadiusService()
         .size(ScenePctService.pct(100.0F), 25.0F)
         .cornerRadius(7.0F)
         .backgroundColor(enabled ? this.count & 16777215 | 889192448 : 150994943)
         .hoverBackground(553648127)
         .padding(0.0F, 8.0F, 0.0F, 8.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> {
            this.text3 = fontEntry.key();
            this.sceneTextService.text(fontEntry.display());
            this.updateState4(fontEntry);
            if (this.text2 != null) {
               this.text2.accept(this.text3.isEmpty() ? null : this.text3);
            }

            this.close();
         });
      SceneTextService sceneText = new SceneTextService(fontEntry.display(), 8.2F, enabled ? -1 : -788529153);
      if (fontEntry.kind() == TextFontDirectoryService.Kind.MINECRAFT) {
         sceneText.fontFamily("minecraft");
      } else if (!fontEntry.key().isEmpty() && this.compositorPushPresentationScaleService.isFontFamilyReady(fontEntry.key())) {
         sceneText.fontFamily(fontEntry.key());
      }

      currentSize.addChild(sceneText);
      currentSize.addChild(new SceneTextService(fontEntry.kind().name().toLowerCase(), 6.5F, 1442840575));
      return currentSize;
   }

   private void updateState4(TextFontDirectoryService.FontEntry fontEntry) {
      if (fontEntry != null && fontEntry.source() != null && !fontEntry.key().isEmpty()) {
         this.compositorPushPresentationScaleService.requestFontFamily(fontEntry.key(), fontEntry.source(), fontEntry.ref());
      }
   }
}
