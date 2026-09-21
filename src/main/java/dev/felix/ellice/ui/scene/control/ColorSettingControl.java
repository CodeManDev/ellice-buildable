package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneContentService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.text.TextMode;
import java.awt.Color;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class ColorSettingControl extends LayoutContainerNode {
  public static final float DEFAULT_WIDTH = 112.0f;
  public static final float TRIGGER_HEIGHT = 19.0f;
  public static final float FIELD_HEIGHT = 68.0f;
  public static final float BAR_HEIGHT = 9.0f;
  private static final int count = 572070949;
  private static final int count2 = 942288442;
  private static final int count3 = 1244804932;
  private static final int count4 = -1458432996;
  private static final int ffqkvub2kffp = -805306369;
  private static final int count6 = 1493172223;
  private final ModuleSetting.Color moduleColor;
  private final SceneCornerRadiusService sceneCornerRadiusService;
  private final SceneTextService sceneTextService;
  private final SceneCornerRadiusService sceneCornerRadiusService2;
  private final SceneContentService fhkayuexdwn2;
  private final AutoCloseable autoCloseable;
  private SceneCornerRadiusService sceneCornerRadiusService3;
  private SaturationBrightnessPicker colorGetAnimPropertyService2;
  private ColorGetAnimPropertyService colorGetAnimPropertyService;
  private AlphaSliderControl colorGetAnimPropertyService3;
  private Consumer<Integer> fsrkr2z4mmk;
  private Runnable runnable;
  private int value;
  private float value2;
  private float value3;
  private float value4;
  private float value5;
  private boolean f83h3r6js8m0;
  private boolean f5fgq10g09e4;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;
  private boolean enabled7;

  public ColorSettingControl(final ModuleSetting.Color obj) {
    this.f83h3r6js8m0 = true;
    this.f5fgq10g09e4 = true;
    this.moduleColor = Objects.requireNonNull(obj, "setting");
    final Integer n = obj.get();
    this.mbj6zpxdxsgt(this.value = ((n != null) ? n : -1));
    this.direction(Direction.COLUMN);
    this.align(Align.STRETCH);
    this.justify(Justify.START);
    this.gap(Float.intBitsToFloat(1077936128));
    this.size(
        LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
        LayoutOperationHandler.auto());
    this.flexShrink(0.0f);
    this.sceneCornerRadiusService =
        new SceneCornerRadiusService() {
          @Override
          protected void onPress(final float n, final float n2) {
            ColorSettingControl.this.updateState6();
          }
        };
    this.sceneCornerRadiusService
        .size(
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            LayoutOperationHandler.px(Float.intBitsToFloat(1100480512)))
        .flexShrink(0.0f)
        .cornerRadius(Float.intBitsToFloat(1084227584))
        .backgroundColor(572070949)
        .hoverBackground(942288442)
        .pressBackground(1244804932)
        .gradientEnd(0)
        .glass(false)
        .blur(0.0f)
        .direction(Direction.ROW)
        .align(Align.CENTER)
        .justify(Justify.SPACE_BETWEEN)
        .padding(0.0f, Float.intBitsToFloat(1084227584))
        .gap(Float.intBitsToFloat(1084227584))
        .cursorStyle(CursorStyle.POINTER)
        .onClick(this::toggle);
    this.sceneTextService =
        new SceneTextService(createText(this.value), Float.intBitsToFloat(1088421888), -805306369)
            .fontVariant(TextMode.REGULAR)
            .overflow(SceneTextService.Overflow.ELLIPSIS)
            .inheritEdgeSoftness(false)
            .flex(1.0f);
    this.sceneCornerRadiusService2 =
        new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1093664768))
            .flexShrink(0.0f)
            .cornerRadius(Float.intBitsToFloat(1077936128))
            .backgroundColor(this.value)
            .gradientEnd(0)
            .glass(false)
            .blur(0.0f)
            .border(Float.intBitsToFloat(1061158912), 1493172223)
            .pointerEvents(false);
    this.sceneCornerRadiusService.addChild(this.sceneTextService);
    this.sceneCornerRadiusService.addChild(this.sceneCornerRadiusService2);
    (this.fhkayuexdwn2 = new SceneContentService())
        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.addChild(this.sceneCornerRadiusService);
    this.addChild(this.fhkayuexdwn2);
    this.refreshState();
    this.autoCloseable = obj.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Color setting() {
    return this.moduleColor;
  }

  public int value() {
    return this.value;
  }

  public boolean isExpanded() {
    return this.fhkayuexdwn2.isExpanded();
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.enabled5;
  }

  public boolean available() {
    return this.enabled4 && this.enabled5 && this.f83h3r6js8m0 && this.f5fgq10g09e4;
  }

  @Override
  public ColorSettingControl visible(final boolean f83h3r6js8m0) {
    this.f83h3r6js8m0 = f83h3r6js8m0;
    this.refreshState();
    return this;
  }

  public ColorSettingControl enabled(final boolean f5fgq10g09e4) {
    this.f5fgq10g09e4 = f5fgq10g09e4;
    this.refreshState();
    return this;
  }

  @Override
  public ColorSettingControl id(final String s) {
    super.id(s);
    this.updateState7();
    return this;
  }

  public ColorSettingControl onChange(final Consumer<Integer> fsrkr2z4mmk) {
    this.fsrkr2z4mmk = fsrkr2z4mmk;
    return this;
  }

  public ColorSettingControl onInteraction(final Runnable runnable) {
    this.runnable = runnable;
    return this;
  }

  public ColorSettingControl toggle() {
    return this.setExpanded(!this.isExpanded(), true);
  }

  public ColorSettingControl setExpanded(final boolean b) {
    return this.setExpanded(b, true);
  }

  public ColorSettingControl setExpanded(final boolean b, final boolean b2) {
    if (this.enabled7 || (b && !this.available())) {
      return this;
    }
    if (b) {
      this.updateState();
    }
    this.fhkayuexdwn2.setExpanded(b, b2);
    return this;
  }

  public boolean trySetColor(final int i) {
    this.refreshState();
    if (!this.available() || this.enabled7) {
      return false;
    }
    final Integer n = this.moduleColor.get();
    if (n == null) {
      return false;
    }
    final int intValue = n;
    this.moduleColor.set(i);
    final Integer n2 = this.moduleColor.get();
    if (n2 == null) {
      this.mjmmuxhtycv5(intValue, true);
      return false;
    }
    final int intValue2 = n2;
    this.mjmmuxhtycv5(intValue2, true);
    final boolean b = intValue != intValue2;
    if (b && this.fsrkr2z4mmk != null) {
      this.fsrkr2z4mmk.accept(intValue2);
    }
    return b;
  }

  public void refreshState() {
    if (this.enabled7) {
      return;
    }
    this.enabled4 = this.moduleColor.isVisible();
    this.enabled5 = this.moduleColor.isActive();
    final boolean available = this.available();
    if (this.visible != available) {
      this.visible = available;
      this.invalidateLayout();
    }
    if (this.pointerEvents != available) {
      this.pointerEvents = available;
      this.invalidate();
    }
    if (this.interactive != available) {
      this.interactive = available;
      this.invalidate();
    }
    this.cursorStyle(available ? CursorStyle.POINTER : CursorStyle.DEFAULT);
    if (!available && this.fhkayuexdwn2.isExpanded()) {
      this.fhkayuexdwn2.setExpanded(false, false);
    }
    final Integer n = this.moduleColor.get();
    if (n != null) {
      this.mjmmuxhtycv5(n, true);
    }
  }

  @Override
  protected boolean handleScroll(final float n) {
    return false;
  }

  @Override
  protected void onDetached() {
    this.dispose();
  }

  public void dispose() {
    if (this.enabled7) {
      return;
    }
    this.enabled7 = true;
    this.enabled6 = false;
    this.fhkayuexdwn2.setExpanded(false, false);
    this.visible = false;
    this.pointerEvents = false;
    this.interactive = false;
    this.fsrkr2z4mmk = null;
    this.runnable = null;
    try {
      this.autoCloseable.close();
    } catch (final Exception ex) {
    }
  }

  private void updateState() {
    if (this.sceneCornerRadiusService3 != null) {
      return;
    }
    this.sceneCornerRadiusService3 =
        new SceneCornerRadiusService()
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.auto())
            .flexShrink(0.0f)
            .cornerRadius(Float.intBitsToFloat(1086324736))
            .backgroundColor(-1458432996)
            .gradientEnd(0)
            .glass(false)
            .blur(0.0f)
            .border(0.0f, 0)
            .padding(Float.intBitsToFloat(1084227584))
            .gap(Float.intBitsToFloat(1082130432))
            .direction(Direction.COLUMN)
            .align(Align.STRETCH)
            .justify(Justify.START)
            .clip(true);
    this.colorGetAnimPropertyService2 =
        new SaturationBrightnessPicker() {
          @Override
          protected void onPress(final float n, final float n2) {
            ColorSettingControl.this.updateState6();
          }
        };
    this.colorGetAnimPropertyService2
        .hue(this.value2)
        .sat(this.value3)
        .bri(this.value4)
        .cornerRadius(Float.intBitsToFloat(1084227584))
        .size(
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            LayoutOperationHandler.px(Float.intBitsToFloat(1116209152)))
        .flexShrink(0.0f)
        .cursorStyle(CursorStyle.POINTER)
        .onChange(
            (n, n2) -> {
              this.value3 = n;
              this.value4 = n2;
              this.updateState2();
              return;
            })
        .onCommit((p0, p1) -> this.colorGetAnimPropertyService3());
    this.colorGetAnimPropertyService =
        new ColorGetAnimPropertyService() {
          @Override
          protected void onPress(final float n, final float n2) {
            ColorSettingControl.this.updateState6();
          }
        };
    this.colorGetAnimPropertyService
        .hue(this.value2)
        .cornerRadius(Float.intBitsToFloat(1082130432))
        .size(
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            LayoutOperationHandler.px(Float.intBitsToFloat(1091567616)))
        .flexShrink(0.0f)
        .cursorStyle(CursorStyle.POINTER)
        .onChange(
            n3 -> {
              this.value2 = n3;
              if (this.colorGetAnimPropertyService2 != null) {
                this.colorGetAnimPropertyService2.hue(this.value2);
              }
              this.updateState2();
              return;
            })
        .onCommit(p0 -> this.colorGetAnimPropertyService3());
    this.colorGetAnimPropertyService3 =
        new AlphaSliderControl() {
          @Override
          protected void onPress(final float n, final float n2) {
            ColorSettingControl.this.updateState6();
          }
        };
    this.colorGetAnimPropertyService3
        .hue(this.value2)
        .sat(this.value3)
        .bri(this.value4)
        .alpha(this.value5)
        .cornerRadius(Float.intBitsToFloat(1082130432))
        .size(
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            LayoutOperationHandler.px(Float.intBitsToFloat(1091567616)))
        .flexShrink(0.0f)
        .cursorStyle(CursorStyle.POINTER)
        .onChange(
            n4 -> {
              this.value5 = n4;
              this.updateState2();
              return;
            })
        .onCommit(p0 -> this.colorGetAnimPropertyService3());
    this.sceneCornerRadiusService3.addChild(this.colorGetAnimPropertyService2);
    this.sceneCornerRadiusService3.addChild(this.colorGetAnimPropertyService);
    this.sceneCornerRadiusService3.addChild(this.colorGetAnimPropertyService3);
    this.fhkayuexdwn2.content(this.sceneCornerRadiusService3);
    this.updateState7();
  }

  private void updateState2() {
    final int value =
        Math.round(this.value5 * Float.intBitsToFloat(1132396544)) << 24
            | (Color.HSBtoRGB(this.value2, this.value3, this.value4) & 0xFFFFFF);
    if (this.value == value) {
      return;
    }
    this.value = value;
    final Integer n = this.moduleColor.get();
    this.enabled6 = (n == null || n != this.value);
    this.sceneCornerRadiusService2.backgroundColor(this.value);
    this.sceneCornerRadiusService2.invalidate();
    if (this.colorGetAnimPropertyService3 != null) {
      this.colorGetAnimPropertyService3
          .hue(this.value2)
          .sat(this.value3)
          .bri(this.value4)
          .alpha(this.value5);
    }
  }

  private void colorGetAnimPropertyService3() {
    if (!this.enabled6) {
      return;
    }
    final int value = this.value;
    this.enabled6 = false;
    if (!this.available() || this.enabled7) {
      final Integer n = this.moduleColor.get();
      if (n != null) {
        this.mjmmuxhtycv5(n, true);
      }
      return;
    }
    final Integer n2 = this.moduleColor.get();
    if (n2 == null) {
      return;
    }
    final int intValue = n2;
    this.moduleColor.set(value);
    final Integer n3 = this.moduleColor.get();
    if (n3 == null) {
      this.mjmmuxhtycv5(intValue, true);
      return;
    }
    final int intValue2 = n3;
    this.mjmmuxhtycv5(intValue2, true);
    if (intValue != intValue2 && this.fsrkr2z4mmk != null) {
      this.fsrkr2z4mmk.accept(intValue2);
    }
  }

  private void mjmmuxhtycv5(final int value, final boolean b) {
    this.value = value;
    this.enabled6 = false;
    this.mbj6zpxdxsgt(value);
    this.sceneCornerRadiusService2.backgroundColor(value);
    this.sceneCornerRadiusService2.invalidate();
    if (b) {
      this.sceneTextService.text(createText(value));
    }
    if (this.colorGetAnimPropertyService2 != null) {
      this.colorGetAnimPropertyService2.hue(this.value2).sat(this.value3).bri(this.value4);
    }
    if (this.colorGetAnimPropertyService != null) {
      this.colorGetAnimPropertyService.hue(this.value2);
    }
    if (this.colorGetAnimPropertyService3 != null) {
      this.colorGetAnimPropertyService3
          .hue(this.value2)
          .sat(this.value3)
          .bri(this.value4)
          .alpha(this.value5);
    }
  }

  private void mbj6zpxdxsgt(final int n) {
    this.value5 = (n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
    final float[] rgBtoHSB = Color.RGBtoHSB(n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, null);
    this.value2 = rgBtoHSB[0];
    this.value3 = rgBtoHSB[1];
    this.value4 = rgBtoHSB[2];
  }

  private void updateState6() {
    if (!this.enabled7 && this.available() && this.runnable != null) {
      this.runnable.run();
    }
  }

  private void updateState7() {
    final String id = this.getId();
    if (id == null || id.isBlank()) {
      return;
    }
    this.sceneCornerRadiusService.id(id + ".trigger");
    this.sceneTextService.id(id + ".value");
    this.sceneCornerRadiusService2.id(id + ".swatch");
    this.fhkayuexdwn2.id(id + ".reveal");
    if (this.sceneCornerRadiusService3 != null) {
      this.sceneCornerRadiusService3.id(id + ".editor");
    }
    if (this.colorGetAnimPropertyService2 != null) {
      this.colorGetAnimPropertyService2.id(id + ".field");
    }
    if (this.colorGetAnimPropertyService != null) {
      this.colorGetAnimPropertyService.id(id + ".hue");
    }
    if (this.colorGetAnimPropertyService3 != null) {
      this.colorGetAnimPropertyService3.id(id + ".alpha");
    }
  }

  private static String createText(final int i) {
    return String.format(Locale.ROOT, "#%08X", i);
  }
}
