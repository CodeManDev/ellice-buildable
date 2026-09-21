package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.ui.component.ComponentUnmountService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.awt.Color;
import java.util.Locale;
import java.util.function.Consumer;

final class ColourComponent {
   private final ScenePctService<?> scenePctService;
   private final Consumer<Integer> consumer;
   private final LayoutContainerNode sceneComponent4;
   private final SceneCornerRadiusService sceneCornerRadiusService;
   private final ControlLetterSpacingService controlLetterSpacingService;
   private LayoutContainerNode sceneComponent42;
   private SaturationBrightnessPicker colorGetAnimPropertyService2;
   private ColorGetAnimPropertyService colorGetAnimPropertyService;
   private AlphaSliderControl colorGetAnimPropertyService3;
   private int count = -1;
   private float value;
   private float value2;
   private float value3 = 1.0F;
   private float value4 = 1.0F;
   private boolean enabled;

   ColourComponent(ScenePctService<?> scenePct, Consumer<Integer> currentConsumer) {
      this.scenePctService = scenePct;
      this.consumer = currentConsumer;
      this.sceneComponent4 = new LayoutContainerNode()
         .size(ScenePctService.pct(100.0F), 28.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .gap(8.0F);
      this.sceneCornerRadiusService = new SceneCornerRadiusService()
         .size(28.0F, 28.0F)
         .cornerRadius(14.0F)
         .backgroundColor(this.count)
         .border(1.0F, 1358954495)
         .shadow(4.0F)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(this::updateState);
      this.controlLetterSpacingService = new ControlLetterSpacingService()
         .text(createText(this.count))
         .fontSize(8.5F)
         .textColor(-385875969)
         .bgColor(452984831)
         .focusBorder(-10262799)
         .cornerRadius(7.0F)
         .maxLength(9)
         .onChanged(this::updateState3)
         .onSubmit(this::updateState3);
      this.controlLetterSpacingService.size(-1.0F, 26.0F).flex(1.0F);
      this.sceneComponent4.addChild(this.sceneCornerRadiusService);
      this.sceneComponent4.addChild(this.controlLetterSpacingService);
      this.updateState5();
   }

   LayoutContainerNode node() {
      return this.sceneComponent4;
   }

   void setColor(int color) {
      this.count = color;
      this.updateState5();
      this.updateState6();
   }

   void close() {
      if (this.sceneComponent42 != null) {
         ComponentUnmountService.unmount(this.scenePctService, this.sceneComponent42);
         this.sceneComponent42 = null;
         this.colorGetAnimPropertyService2 = null;
         this.colorGetAnimPropertyService = null;
         this.colorGetAnimPropertyService3 = null;
      }
   }

   private void updateState() {
      if (this.sceneComponent42 != null) {
         this.close();
      } else {
         this.updateState2();
      }
   }

   private void updateState2() {
      SceneCornerRadiusService currentSize = new SceneCornerRadiusService()
         .size(LayoutOperationHandler.px(214.0F), LayoutOperationHandler.px(178.0F))
         .maxWidth(LayoutOperationHandler.percent(100.0F))
         .maxHeight(LayoutOperationHandler.percent(100.0F))
         .cornerRadius(13.0F)
         .backgroundColor(-266856412)
         .gradientEnd(-267645932)
         .border(1.0F, 905969663)
         .shadow(18.0F)
         .blur(24.0F)
         .padding(12.0F)
         .gap(8.0F)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH);
      LayoutContainerNode nextSize = new LayoutContainerNode()
         .size(ScenePctService.pct(100.0F), 18.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN);
      nextSize.addChild(new SceneTextService("Colour", 9.0F, -385875969));
      SceneCornerRadiusService previousSize = new SceneCornerRadiusService()
         .size(38.0F, 18.0F)
         .cornerRadius(7.0F)
         .backgroundColor(553648127)
         .hoverBackground(905969663)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .onClick(this::close);
      previousSize.addChild(new SceneTextService("Done", 7.0F, -805306369));
      nextSize.addChild(previousSize);
      currentSize.addChild(nextSize);
      this.colorGetAnimPropertyService2 = new SaturationBrightnessPicker()
         .hue(this.value)
         .sat(this.value2)
         .bri(this.value3)
         .cornerRadius(7.0F)
         .onChange((item, currentItem) -> {
            this.value2 = item;
            this.value3 = currentItem;
            this.updateState4();
         });
      this.colorGetAnimPropertyService2.size(ScenePctService.pct(100.0F), 86.0F);
      currentSize.addChild(this.colorGetAnimPropertyService2);
      this.colorGetAnimPropertyService = new ColorGetAnimPropertyService().hue(this.value).cornerRadius(4.0F).onChange(item -> {
         this.value = item;
         if (this.colorGetAnimPropertyService2 != null) {
            this.colorGetAnimPropertyService2.hue(item);
         }

         this.updateState4();
      });
      this.colorGetAnimPropertyService.size(ScenePctService.pct(100.0F), 10.0F);
      currentSize.addChild(this.colorGetAnimPropertyService);
      this.colorGetAnimPropertyService3 = new AlphaSliderControl()
         .hue(this.value)
         .sat(this.value2)
         .bri(this.value3)
         .alpha(this.value4)
         .cornerRadius(4.0F)
         .onChange(item -> {
            this.value4 = item;
            this.updateState4();
         });
      this.colorGetAnimPropertyService3.size(ScenePctService.pct(100.0F), 10.0F);
      currentSize.addChild(this.colorGetAnimPropertyService3);
      this.sceneComponent42 = ComponentUnmountService.mountCentered(this.scenePctService, currentSize, 12.0F, this::close);
      currentSize.opacity(0.0F).scale(0.96F);
      MotionAnimateService.animate(currentSize, MotionColorsContainer.Floats.OPACITY, 1.0F, SceneEaseHandler.Tween.ease(0.16F));
      MotionAnimateService.animate(currentSize, MotionColorsContainer.Floats.SCALE, 1.0F, SceneEaseHandler.Spring.SNAPPY);
   }

   private void updateState3(String text) {
      if (!this.enabled) {
         String currentText = text == null ? "" : text.trim();
         if (currentText.startsWith("#")) {
            currentText = currentText.substring(1);
         }

         try {
            long offset;
            if (currentText.length() == 6) {
               offset = 4278190080L | Long.parseLong(currentText, 16);
            } else {
               if (currentText.length() != 8) {
                  return;
               }

               offset = Long.parseLong(currentText, 16);
            }

            this.count = (int)offset;
            this.updateState5();
            this.sceneCornerRadiusService.backgroundColor(this.count);
            this.updateState7();
            if (this.consumer != null) {
               this.consumer.accept(this.count);
            }
         } catch (NumberFormatException numberFormatException) {
         }
      }
   }

   private void updateState4() {
      int currentValue = Color.HSBtoRGB(this.value, this.value2, this.value3) & 16777215;
      this.count = Math.round(this.value4 * 255.0F) << 24 | currentValue;
      this.sceneCornerRadiusService.backgroundColor(this.count);
      this.enabled = true;
      this.controlLetterSpacingService.text(createText(this.count));
      this.enabled = false;
      if (this.colorGetAnimPropertyService3 != null) {
         this.colorGetAnimPropertyService3.hue(this.value).sat(this.value2).bri(this.value3).alpha(this.value4);
      }

      if (this.consumer != null) {
         this.consumer.accept(this.count);
      }
   }

   private void updateState5() {
      this.value4 = (this.count >>> 24 & 0xFF) / 255.0F;
      float[] floats = Color.RGBtoHSB(this.count >>> 16 & 0xFF, this.count >>> 8 & 0xFF, this.count & 0xFF, null);
      this.value = floats[0];
      this.value2 = floats[1];
      this.value3 = floats[2];
   }

   private void updateState6() {
      this.sceneCornerRadiusService.backgroundColor(this.count);
      this.enabled = true;
      this.controlLetterSpacingService.text(createText(this.count));
      this.enabled = false;
      this.updateState7();
   }

   private void updateState7() {
      if (this.colorGetAnimPropertyService2 != null) {
         this.colorGetAnimPropertyService2.hue(this.value).sat(this.value2).bri(this.value3);
      }

      if (this.colorGetAnimPropertyService != null) {
         this.colorGetAnimPropertyService.hue(this.value);
      }

      if (this.colorGetAnimPropertyService3 != null) {
         this.colorGetAnimPropertyService3.hue(this.value).sat(this.value2).bri(this.value3).alpha(this.value4);
      }
   }

   private static String createText(int value) {
      return String.format(Locale.ROOT, "#%08X", value);
   }
}

