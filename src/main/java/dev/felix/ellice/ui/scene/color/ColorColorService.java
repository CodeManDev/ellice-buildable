package dev.felix.ellice.ui.scene.color;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ColorColorService extends ScenePctService<ColorColorService> {
   private int count = -65536;
   private float value2;
   private float value3 = 1.0F;
   private float value4 = 1.0F;
   private float value5 = 1.0F;
   public Consumer<Integer> onChange;
   private boolean enabled;
   private static final List<Integer> items = new ArrayList<>();
   private static final int count2 = 12;
   private ScenePctService<?> scenePctService;
   private SceneCornerRadiusService sceneCornerRadiusService;
   private SaturationBrightnessPicker colorGetAnimPropertyService2;
   private ColorHueService colorHueService;
   private HueTrianglePicker colorHueService3;
   private ColorGetAnimPropertyService colorGetAnimPropertyService;
   private SceneCornerRadiusService sceneCornerRadiusService2;
   private int count3;
   private String text2 = "Color";
   private static final int[] int2 = new int[]{
      -48060, -29696, -10496, -11870592, -14494738, -10262799, -5745161, -1292135, -1, -7829368, -12303292, -16777216
   };
   private int count4;

   public ColorColorService() {
      this.interactive = true;
      this.updateState();
   }

   public ColorColorService color(int currentColor) {
      this.count = currentColor;
      this.updateState();
      return this;
   }

   public ColorColorService onChange(Consumer<Integer> consumer) {
      this.onChange = consumer;
      return this;
   }

   public ColorColorService popupParent(ScenePctService<?> scenePct) {
      this.scenePctService = scenePct;
      return this;
   }

   public ColorColorService settingName(String name) {
      this.text2 = name;
      return this;
   }

   public int color() {
      return this.count;
   }

   private void updateState() {
      this.value5 = (this.count >> 24 & 0xFF) / 255.0F;
      int currentCount = this.count >> 16 & 0xFF;
      int nextCount = this.count >> 8 & 0xFF;
      int previousCount = this.count & 0xFF;
      float value = currentCount / 255.0F;
      float currentValue = nextCount / 255.0F;
      float nextValue = previousCount / 255.0F;
      float previousValue = Math.max(value, Math.max(currentValue, nextValue));
      float sourceValue = Math.min(value, Math.min(currentValue, nextValue));
      float targetValue = previousValue - sourceValue;
      this.value4 = previousValue;
      this.value3 = previousValue == 0.0F ? 0.0F : targetValue / previousValue;
      if (targetValue == 0.0F) {
         this.value2 = 0.0F;
      } else if (previousValue == value) {
         this.value2 = (currentValue - nextValue) / targetValue % 6.0F / 6.0F;
      } else if (previousValue == currentValue) {
         this.value2 = ((nextValue - value) / targetValue + 2.0F) / 6.0F;
      } else {
         this.value2 = ((value - currentValue) / targetValue + 4.0F) / 6.0F;
      }

      if (this.value2 < 0.0F) {
         this.value2++;
      }
   }

   private void updateState2() {
      int value = (int)(this.value5 * 255.0F) & 0xFF;
      this.count = value << 24 | hsbToRgb(this.value2, this.value3, this.value4);
      if (this.onChange != null) {
         this.onChange.accept(this.count);
      }
   }

   @Override
   protected void handleClick() {
      if (this.sceneCornerRadiusService != null) {
         this.closePopup();
      } else {
         this.updateState3();
      }
   }

   public void eyedropperPick(int value) {
      this.count = value;
      this.updateState();
      this.value5 = 1.0F;
      this.enabled = false;
      if (this.colorGetAnimPropertyService2 != null) {
         this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorHueService != null) {
         this.colorHueService.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorHueService3 != null) {
         this.colorHueService3.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorGetAnimPropertyService != null) {
         this.colorGetAnimPropertyService.hue(this.value2);
      }

      this.updateState7();
      this.updateState8("alpha", 100.0F);
      this.updateState8("bri", this.value4 * 100.0F);
      if (this.sceneCornerRadiusService != null) {
         this.sceneCornerRadiusService2.backgroundColor(this.count);
         if (this.sceneCornerRadiusService.findById("hex") instanceof ControlLetterSpacingService controlLetterSpacing) {
            controlLetterSpacing.text(this.createText());
            this.updateState9(controlLetterSpacing);
         }
      }

      updateState13(this.count);
      if (this.onChange != null) {
         this.onChange.accept(this.count);
      }
   }

   public boolean isEyedropperActive() {
      return this.enabled;
   }

   private void updateState3() {
      if (this.scenePctService != null) {
         this.count4 = this.count;
         this.updateState4();
      }
   }

   private void updateState4() {
      float currentValue = this.cx + this.cw + 6.0F;
      float nextValue = this.cy;
      if (this.sceneCornerRadiusService != null) {
         currentValue = this.sceneCornerRadiusService.x;
         nextValue = this.sceneCornerRadiusService.y;
         if (this.scenePctService != null) {
            this.scenePctService.removeChild(this.sceneCornerRadiusService);
         }
      }

      this.colorGetAnimPropertyService2 = null;
      this.colorHueService = null;
      this.colorHueService3 = null;
      this.colorGetAnimPropertyService = null;
      this.sceneCornerRadiusService = new SceneCornerRadiusService();
      this.sceneCornerRadiusService
         .size(165.0F, -1.0F)
         .position(currentValue, nextValue)
         .cornerRadius(8.0F)
         .backgroundColor(-534765520)
         .border(1.0F, 822083583)
         .shadow(8.0F)
         .blur(12.0F)
         .draggable(true)
         .direction(ScenePctService.Direction.COLUMN)
         .padding(8.0F)
         .gap(5.0F);
      LayoutContainerNode layoutContainerNode = new LayoutContainerNode();
      layoutContainerNode.direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.SPACE_BETWEEN);
      SceneTextService sceneText = new SceneTextService(this.text2, 8.0F, 1895825407);
      layoutContainerNode.addChild(sceneText);
      SceneCornerRadiusService sceneCornerRadius = new SceneCornerRadiusService();
      sceneCornerRadius.size(16.0F, 16.0F)
         .cornerRadius(4.0F)
         .backgroundColor(369098751)
         .hoverBackground(1090519039)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER);
      sceneCornerRadius.addChild(new SceneTextService("x", 7.0F, -1426063361));
      sceneCornerRadius.onClick(this::closePopup);
      layoutContainerNode.addChild(sceneCornerRadius);
      this.sceneCornerRadiusService.addChild(layoutContainerNode);
      LayoutContainerNode currentLayoutContainerNode = new LayoutContainerNode();
      currentLayoutContainerNode.direction(ScenePctService.Direction.ROW).gap(2.0F);
      currentLayoutContainerNode.addChild(this.createSceneCornerRadiusService("Gradient", 0));
      currentLayoutContainerNode.addChild(this.createSceneCornerRadiusService("Triangle", 2));
      this.sceneCornerRadiusService.addChild(currentLayoutContainerNode);
      LayoutContainerNode nextLayoutContainerNode = new LayoutContainerNode();
      nextLayoutContainerNode.direction(ScenePctService.Direction.ROW).gap(2.0F);
      SceneCornerRadiusService currentSceneCornerRadius = new SceneCornerRadiusService();
      currentSceneCornerRadius.size(-1.0F, 14.0F)
         .flex(1.0F)
         .cornerRadius(3.0F)
         .backgroundColor(this.count4);
      nextLayoutContainerNode.addChild(currentSceneCornerRadius);
      this.sceneCornerRadiusService2 = new SceneCornerRadiusService();
      this.sceneCornerRadiusService2
         .size(-1.0F, 14.0F)
         .flex(1.0F)
         .cornerRadius(3.0F)
         .backgroundColor(this.count);
      nextLayoutContainerNode.addChild(this.sceneCornerRadiusService2);
      this.sceneCornerRadiusService.addChild(nextLayoutContainerNode);
      ControlLetterSpacingService controlLetterSpacing = new ControlLetterSpacingService();
      controlLetterSpacing.id("hex")
         .text(this.createText())
         .fontSize(8.0F)
         .textColor(-855638017)
         .bgColor(369098751)
         .maxLength(9)
         .size(-1.0F, 16.0F)
         .onChanged(this::updateState12)
         .onSubmit(this::updateState12);
      this.sceneCornerRadiusService.addChild(controlLetterSpacing);
      if (this.count3 == 2) {
         this.colorHueService3 = new HueTrianglePicker();
         this.colorHueService3
            .hue(this.value2)
            .sat(this.value3)
            .bri(this.value4)
            .size(-1.0F, 105.0F)
            .onHueChange(item -> {
               this.value2 = item;
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            })
            .onSBChange((item, currentItem) -> {
               this.value3 = item;
               this.value4 = currentItem;
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            });
         this.sceneCornerRadiusService.addChild(this.colorHueService3);
      } else if (this.count3 == 1) {
         this.colorHueService = new ColorHueService();
         this.colorHueService
            .hue(this.value2)
            .sat(this.value3)
            .bri(this.value4)
            .size(-1.0F, 100.0F)
            .onChange((item, currentItem) -> {
               this.value2 = item;
               this.value3 = currentItem;
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            });
         this.sceneCornerRadiusService.addChild(this.colorHueService);
         SliderControl sliderControl = new SliderControl();
         sliderControl.id("bri")
            .value(this.value4 * 100.0F)
            .range(0.0F, 100.0F)
            .step(1.0F)
            .fillColor(0xFF000000 | hsbToRgb(this.value2, this.value3, 1.0F))
            .trackColor(553648127)
            .format("B: %.0f%%")
            .textColor(-2130706433)
            .size(-1.0F, 16.0F)
            .onChange(item -> {
               this.value4 = item / 100.0F;
               this.colorHueService.bri(this.value4);
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            });
         this.sceneCornerRadiusService.addChild(sliderControl);
      } else {
         this.colorGetAnimPropertyService2 = new SaturationBrightnessPicker();
         this.colorGetAnimPropertyService2
            .hue(this.value2)
            .sat(this.value3)
            .bri(this.value4)
            .cornerRadius(4.0F)
            .size(-1.0F, 95.0F)
            .onChange((item, currentItem) -> {
               this.value3 = item;
               this.value4 = currentItem;
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            });
         this.sceneCornerRadiusService.addChild(this.colorGetAnimPropertyService2);
         this.colorGetAnimPropertyService = new ColorGetAnimPropertyService();
         this.colorGetAnimPropertyService
            .hue(this.value2)
            .cornerRadius(3.0F)
            .size(-1.0F, 12.0F)
            .onChange(item -> {
               this.value2 = item;
               this.colorGetAnimPropertyService2.hue(item);
               this.updateState2();
               this.updateState9(controlLetterSpacing);
               this.updateState7();
            });
         this.sceneCornerRadiusService.addChild(this.colorGetAnimPropertyService);
      }

      SliderControl currentSliderControl = new SliderControl();
      currentSliderControl.id("alpha")
         .value(this.value5 * 100.0F)
         .range(0.0F, 100.0F)
         .step(1.0F)
         .fillColor(this.count | 0xFF000000)
         .trackColor(553648127)
         .format("A: %.0f%%")
         .textColor(-2130706433)
         .size(-1.0F, 16.0F)
         .onChange(item -> {
            this.value5 = item / 100.0F;
            this.updateState2();
            this.updateState9(controlLetterSpacing);
         });
      this.sceneCornerRadiusService.addChild(currentSliderControl);
      this.sceneCornerRadiusService.addChild(this.createControlCompactService2("R", this.count >> 16 & 0xFF, -1096636, controlLetterSpacing));
      this.sceneCornerRadiusService.addChild(this.createControlCompactService2("G", this.count >> 8 & 0xFF, -11870592, controlLetterSpacing));
      this.sceneCornerRadiusService.addChild(this.createControlCompactService2("B", this.count & 0xFF, -10443270, controlLetterSpacing));
      SceneCornerRadiusService nextSceneCornerRadius = new SceneCornerRadiusService();
      nextSceneCornerRadius.size(-1.0F, 1.0F).backgroundColor(369098751);
      this.sceneCornerRadiusService.addChild(nextSceneCornerRadius);
      SceneTextService currentSceneText = new SceneTextService("Harmonies", 7.0F, 1627389951);
      this.sceneCornerRadiusService.addChild(currentSceneText);
      LayoutContainerNode previousLayoutContainerNode = new LayoutContainerNode();
      previousLayoutContainerNode.id("harmonies").direction(ScenePctService.Direction.ROW).gap(3.0F);
      this.updateState5(previousLayoutContainerNode, controlLetterSpacing);
      this.sceneCornerRadiusService.addChild(previousLayoutContainerNode);
      SceneCornerRadiusService previousSceneCornerRadius = new SceneCornerRadiusService();
      previousSceneCornerRadius.size(-1.0F, 1.0F).backgroundColor(369098751);
      this.sceneCornerRadiusService.addChild(previousSceneCornerRadius);
      SceneTextService nextSceneText = new SceneTextService("Presets", 7.0F, 1627389951);
      this.sceneCornerRadiusService.addChild(nextSceneText);
      LayoutContainerNode sourceLayoutContainerNode = new LayoutContainerNode();
      sourceLayoutContainerNode.direction(ScenePctService.Direction.ROW).gap(3.0F);

      for (int previousValue : int2) {
         sourceLayoutContainerNode.addChild(this.createSceneCornerRadiusService2(previousValue, 10.0F, controlLetterSpacing));
      }

      this.sceneCornerRadiusService.addChild(sourceLayoutContainerNode);
      if (!items.isEmpty()) {
         SceneTextService previousSceneText = new SceneTextService("Recent", 7.0F, 1627389951);
         this.sceneCornerRadiusService.addChild(previousSceneText);
         LayoutContainerNode targetLayoutContainerNode = new LayoutContainerNode();
         targetLayoutContainerNode.id("history").direction(ScenePctService.Direction.ROW).gap(3.0F);

         for (int sourceValue : items) {
            targetLayoutContainerNode.addChild(this.createSceneCornerRadiusService2(sourceValue, 10.0F, controlLetterSpacing));
         }

         this.sceneCornerRadiusService.addChild(targetLayoutContainerNode);
      }

      SceneCornerRadiusService sourceSceneCornerRadius = new SceneCornerRadiusService();
      sourceSceneCornerRadius.size(-1.0F, 1.0F).backgroundColor(369098751);
      this.sceneCornerRadiusService.addChild(sourceSceneCornerRadius);
      SceneTextService sourceSceneText = new SceneTextService("", 7.0F, 1627389951);
      sourceSceneText.id("fmt-rgb");
      this.updateState10(sourceSceneText);
      this.sceneCornerRadiusService.addChild(sourceSceneText);
      SceneTextService targetSceneText = new SceneTextService("", 7.0F, 1627389951);
      targetSceneText.id("fmt-hsl");
      this.updateState11(targetSceneText);
      this.sceneCornerRadiusService.addChild(targetSceneText);
      LayoutContainerNode inputLayoutContainerNode = new LayoutContainerNode();
      inputLayoutContainerNode.direction(ScenePctService.Direction.ROW).gap(3.0F);
      SceneCornerRadiusService targetSceneCornerRadius = new SceneCornerRadiusService();
      targetSceneCornerRadius.direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .flex(1.0F)
         .padding(3.0F)
         .cornerRadius(4.0F)
         .backgroundColor(369098751)
         .hoverBackground(637534207);
      targetSceneCornerRadius.addChild(new SceneTextService("Eyedropper", 7.0F, -1426063361));
      targetSceneCornerRadius.onClick(() -> this.enabled = true);
      inputLayoutContainerNode.addChild(targetSceneCornerRadius);
      SceneCornerRadiusService inputSceneCornerRadius = new SceneCornerRadiusService();
      inputSceneCornerRadius.direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .flex(1.0F)
         .padding(3.0F)
         .cornerRadius(4.0F)
         .backgroundColor(369098751)
         .hoverBackground(637534207);
      inputSceneCornerRadius.addChild(new SceneTextService("Random", 7.0F, -1426063361));
      inputSceneCornerRadius.onClick(() -> {
         this.value2 = (float)Math.random();
         this.value3 = 0.5F + (float)Math.random() * 0.5F;
         this.value4 = 0.5F + (float)Math.random() * 0.5F;
         this.updateState2();
         if (this.colorGetAnimPropertyService2 != null) {
            this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
         }

         if (this.colorHueService != null) {
            this.colorHueService.hue(this.value2).sat(this.value3).bri(this.value4);
         }

         if (this.colorHueService3 != null) {
            this.colorHueService3.hue(this.value2).sat(this.value3).bri(this.value4);
         }

         if (this.colorGetAnimPropertyService != null) {
            this.colorGetAnimPropertyService.hue(this.value2);
         }

         this.updateState9(controlLetterSpacing);
         this.updateState7();
         this.updateState8("bri", this.value4 * 100.0F);
      });
      inputLayoutContainerNode.addChild(inputSceneCornerRadius);
      this.sceneCornerRadiusService.addChild(inputLayoutContainerNode);
      this.scenePctService.addChild(this.sceneCornerRadiusService);
   }

   private SceneCornerRadiusService createSceneCornerRadiusService(String text, int value) {
      int currentValue = this.count3 == value ? 1 : 0;
      SceneCornerRadiusService sceneCornerRadius = new SceneCornerRadiusService();
      sceneCornerRadius.direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .flex(1.0F)
         .padding(3.0F)
         .cornerRadius(4.0F)
         .backgroundColor(currentValue != 0 ? 822083583 : 0)
         .hoverBackground(553648127);
      SceneTextService sceneText = new SceneTextService(text, 7.0F, currentValue != 0 ? -1 : -2130706433);
      sceneCornerRadius.addChild(sceneText);
      sceneCornerRadius.onClick(() -> {
         this.count3 = value;
         this.updateState4();
      });
      return sceneCornerRadius;
   }

   private SceneCornerRadiusService createSceneCornerRadiusService2(int value, float currentValue, ControlLetterSpacingService controlLetterSpacing) {
      SceneCornerRadiusService sceneCornerRadius = new SceneCornerRadiusService();
      sceneCornerRadius.size(currentValue, currentValue)
         .cornerRadius(2.0F)
         .backgroundColor(value)
         .hoverBackground(value)
         .interactive(true)
         .onClick(() -> this.updateState6(value, controlLetterSpacing));
      return sceneCornerRadius;
   }

   private void updateState5(LayoutContainerNode layoutContainerNode, ControlLetterSpacingService controlLetterSpacing) {
      layoutContainerNode.clearChildren();
      int currentCount = this.count | 0xFF000000;
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 + 0.5F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 + 0.083333336F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 - 0.083333336F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 + 0.33333334F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 - 0.33333334F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 + 0.41666666F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
      layoutContainerNode.addChild(
         this.createSceneCornerRadiusService2(
            0xFF000000 | hsbToRgb(this.value2 - 0.41666666F, this.value3, this.value4),
            12.0F,
            controlLetterSpacing
         )
      );
   }

   private void updateState6(int value, ControlLetterSpacingService controlLetterSpacing) {
      this.count = value;
      this.updateState();
      this.value5 = (value >> 24 & 0xFF) / 255.0F;
      if (this.colorGetAnimPropertyService2 != null) {
         this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorHueService != null) {
         this.colorHueService.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorHueService3 != null) {
         this.colorHueService3.hue(this.value2).sat(this.value3).bri(this.value4);
      }

      if (this.colorGetAnimPropertyService != null) {
         this.colorGetAnimPropertyService.hue(this.value2);
      }

      this.updateState9(controlLetterSpacing);
      this.updateState7();
      this.updateState8("alpha", this.value5 * 100.0F);
      this.updateState8("bri", this.value4 * 100.0F);
      if (this.onChange != null) {
         this.onChange.accept(this.count);
      }
   }

   private SliderControl createControlCompactService2(String channelName, int channelValue, int fillColor, ControlLetterSpacingService controlLetterSpacing) {
      SliderControl sliderControl = new SliderControl();
      sliderControl.id("rgb-" + channelName)
         .value(channelValue)
         .range(0.0F, 255.0F)
         .step(1.0F)
         .fillColor(fillColor)
         .trackColor(553648127)
         .format(channelName + ": %.0f")
         .textColor(-2130706433)
         .fontSize(7.0F)
         .size(-1.0F, 14.0F)
         .onChange(item -> {
            int red = this.calculateValue("R");
            int green = this.calculateValue("G");
            int blue = this.calculateValue("B");
            int alpha = (int)(this.value5 * 255.0F) & 0xFF;
            this.count = alpha << 24 | red << 16 | green << 8 | blue;
            this.updateState();
            this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
            this.colorGetAnimPropertyService.hue(this.value2);
            this.updateState9(controlLetterSpacing);
         });
      return sliderControl;
   }

   private int calculateValue(String text) {
      return this.sceneCornerRadiusService.findById("rgb-" + text) instanceof SliderControl sliderControl ? (int)sliderControl.value() : 0;
   }

   private void updateState7() {
      this.updateState8("rgb-R", this.count >> 16 & 0xFF);
      this.updateState8("rgb-G", this.count >> 8 & 0xFF);
      this.updateState8("rgb-B", this.count & 0xFF);
   }

   private void updateState8(String text, float currentValue) {
      if (this.sceneCornerRadiusService.findById(text) instanceof SliderControl sliderControl) {
         sliderControl.value(currentValue);
      }
   }

   private void updateState9(ControlLetterSpacingService controlLetterSpacing) {
      this.sceneCornerRadiusService2.backgroundColor(this.count);
      if (controlLetterSpacing != null && !controlLetterSpacing.focused()) {
         controlLetterSpacing.text(this.createText());
      }

      if (this.sceneCornerRadiusService.findById("alpha") instanceof SliderControl sliderControl) {
         sliderControl.fillColor(this.count | 0xFF000000);
      }

      if (this.sceneCornerRadiusService.findById("harmonies") instanceof LayoutContainerNode layoutContainerNode) {
         this.updateState5(layoutContainerNode, controlLetterSpacing);
      }

      if (this.sceneCornerRadiusService.findById("fmt-rgb") instanceof SceneTextService sceneText) {
         this.updateState10(sceneText);
      }

      if (this.sceneCornerRadiusService.findById("fmt-hsl") instanceof SceneTextService currentSceneText) {
         this.updateState11(currentSceneText);
      }
   }

   private void updateState10(SceneTextService sceneText) {
      int currentCount = this.count >> 16 & 0xFF;
      int nextCount = this.count >> 8 & 0xFF;
      int previousCount = this.count & 0xFF;
      sceneText.text(String.format(Locale.ROOT, "rgb(%d, %d, %d)", currentCount, nextCount, previousCount));
   }

   private void updateState11(SceneTextService sceneText) {
      float value = this.value4 * (1.0F - this.value3 / 2.0F);
      float currentValue = value != 0.0F && value != 1.0F ? (this.value4 - value) / Math.min(value, 1.0F - value) : 0.0F;
      sceneText.text(
         String.format(
            Locale.ROOT,
            "hsl(%.0f, %.0f%%, %.0f%%)",
            this.value2 * 360.0F,
            currentValue * 100.0F,
            value * 100.0F
         )
      );
   }

   private String createText() {
      return this.value5 >= 0.999F
         ? String.format(Locale.ROOT, "#%06X", this.count & 16777215)
         : String.format(Locale.ROOT, "#%08X", this.count);
   }

   private void updateState12(String text) {
      if (text != null && !text.isEmpty()) {
         try {
            String currentText = text.startsWith("#") ? text.substring(1) : text;
            int value;
            if (currentText.length() == 6) {
               value = 0xFF000000 | Integer.parseUnsignedInt(currentText, 16);
            } else {
               if (currentText.length() != 8) {
                  return;
               }

               value = (int)Long.parseUnsignedLong(currentText, 16);
            }

            this.count = value;
            this.updateState();
            this.value5 = (this.count >> 24 & 0xFF) / 255.0F;
            if (this.colorGetAnimPropertyService2 != null) {
               this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
            }

            if (this.colorHueService != null) {
               this.colorHueService.hue(this.value2).sat(this.value3).bri(this.value4);
            }

            if (this.colorHueService3 != null) {
               this.colorHueService3.hue(this.value2).sat(this.value3).bri(this.value4);
            }

            if (this.colorGetAnimPropertyService != null) {
               this.colorGetAnimPropertyService.hue(this.value2);
            }

            this.sceneCornerRadiusService2.backgroundColor(this.count);
            this.updateState7();
            this.updateState8("alpha", this.value5 * 100.0F);
            this.updateState8("bri", this.value4 * 100.0F);
            if (this.onChange != null) {
               this.onChange.accept(this.count);
            }

            if (this.sceneCornerRadiusService.findById("harmonies") instanceof LayoutContainerNode layoutContainerNode) {
               ControlLetterSpacingService controlLetterSpacing = this.sceneCornerRadiusService.findById("hex") instanceof ControlLetterSpacingService currentControlLetterSpacing ? currentControlLetterSpacing : null;
               this.updateState5(layoutContainerNode, controlLetterSpacing);
            }
         } catch (NumberFormatException numberFormatException) {
         }
      }
   }

   public void closePopup() {
      if (this.sceneCornerRadiusService != null && this.scenePctService != null) {
         updateState13(this.count);
         this.scenePctService.removeChild(this.sceneCornerRadiusService);
         this.sceneCornerRadiusService = null;
         this.colorGetAnimPropertyService2 = null;
         this.colorHueService = null;
         this.colorHueService3 = null;
         this.colorGetAnimPropertyService = null;
         this.sceneCornerRadiusService2 = null;
      }
   }

   private static void updateState13(int value) {
      items.remove(Integer.valueOf(value));
      items.add(0, value);

      while (items.size() > 12) {
         items.remove(items.size() - 1);
      }
   }

   public boolean isPopupOpen() {
      return this.sceneCornerRadiusService != null;
   }

   @Override
   public float intrinsicWidth(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return 24.0F;
   }

   @Override
   public float intrinsicHeight(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      return 14.0F;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = this.effectiveOpacity;
      float width = this.effectiveEdgeSoftness;
      float height = 3.0F;
      int value = this.hovered ? -2130706433 : 822083583;
      compositorPushPresentationScale.roundedRect(
         this.cx,
         this.cy,
         this.cw,
         this.ch,
         height,
         height,
         height,
         height,
         mulAlpha(this.count, y),
         0.0F,
         0.0F,
         0,
         1.0F,
         mulAlpha(value, y),
         1.0F,
         width
      );
   }

   static int hsbToRgb(float value, float currentValue, float nextValue) {
      value -= (float)Math.floor(value);
      currentValue = Math.max(0.0F, Math.min(1.0F, currentValue));
      nextValue = Math.max(0.0F, Math.min(1.0F, nextValue));
      float previousValue = nextValue * currentValue;
      float sourceValue = previousValue * (1.0F - Math.abs(value * 6.0F % 2.0F - 1.0F));
      float targetValue = nextValue - previousValue;
      int inputValue = (int)(value * 6.0F);
      if (inputValue > 5) {
         inputValue = 5;
      }

      float outputValue;
      float resultValue;
      float candidateValue;
      switch (inputValue) {
         case 0:
            outputValue = previousValue;
            resultValue = sourceValue;
            candidateValue = 0.0F;
            break;
         case 1:
            outputValue = sourceValue;
            resultValue = previousValue;
            candidateValue = 0.0F;
            break;
         case 2:
            outputValue = 0.0F;
            resultValue = previousValue;
            candidateValue = sourceValue;
            break;
         case 3:
            outputValue = 0.0F;
            resultValue = sourceValue;
            candidateValue = previousValue;
            break;
         case 4:
            outputValue = sourceValue;
            resultValue = 0.0F;
            candidateValue = previousValue;
            break;
         default:
            outputValue = previousValue;
            resultValue = 0.0F;
            candidateValue = sourceValue;
      }

      return (int)((outputValue + targetValue) * 255.0F) << 16
         | (int)((resultValue + targetValue) * 255.0F) << 8
         | (int)((candidateValue + targetValue) * 255.0F);
   }
}

