package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.text.TextMode;
import java.util.function.Consumer;
import java.util.function.Function;

public class ControlGetAnimPropertyService extends ScenePctService<ControlGetAnimPropertyService> {
  private int fblhtbnm9trh;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private float value;
  private float finuxaglzsz9;
  private boolean fbrjruucfhts;
  private boolean enabled5;
  private boolean f7t7uipxc2h;
  private static final MotionFiniteService ff9i1ag5hhkx;
  private static final MotionFiniteService fhhnrdwz9i77;
  private String fhnudul1y2f;
  private int f6uswfhi9ezd;
  private int count3;
  private int f4yg70t7813b;
  private float f5w2ocxla71e;
  private float fehkfxtewt6o;
  public Consumer<Integer> onChange;
  private Function<Integer, String> text3;
  private Runnable runnable;

  @Override
  public float getAnimProperty(final String s) {
    return switch (s) {
      case "materialKeyPress" -> this.value;
      case "materialKeyListening" -> this.finuxaglzsz9;
      default -> super.getAnimProperty(s);
    };
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    switch (s) {
      case "materialKeyPress":
        {
          this.value = n;
          break;
        }
      case "materialKeyListening":
        {
          this.finuxaglzsz9 = n;
          break;
        }
      default:
        {
          super.setAnimProperty(s, n);
          break;
        }
    }
  }

  public ControlGetAnimPropertyService material(final boolean enabled2) {
    this.enabled2 = enabled2;
    return this;
  }

  public void materialFocus(final boolean enabled3) {
    this.enabled3 = enabled3;
    this.invalidate();
  }

  public void activate() {
    this.handleClick();
  }

  public ControlGetAnimPropertyService chooser(final Runnable runnable) {
    this.runnable = runnable;
    return this;
  }

  public ControlGetAnimPropertyService() {
    this.fblhtbnm9trh = -1;
    this.fhnudul1y2f = "None";
    this.f6uswfhi9ezd = 553648127;
    this.count3 = 1090479718;
    this.f4yg70t7813b = -3355444;
    this.f5w2ocxla71e = Float.intBitsToFloat(1090519040);
    this.fehkfxtewt6o = Float.intBitsToFloat(1082130432);
    this.interactive = true;
  }

  public ControlGetAnimPropertyService keyCode(final int fblhtbnm9trh) {
    if (this.fblhtbnm9trh == fblhtbnm9trh) {
      return this;
    }
    this.fblhtbnm9trh = fblhtbnm9trh;
    this.updateState();
    this.invalidateLayout();
    return this;
  }

  public ControlGetAnimPropertyService bgColor(final int f6uswfhi9ezd) {
    this.f6uswfhi9ezd = f6uswfhi9ezd;
    return this;
  }

  public ControlGetAnimPropertyService textColor(final int f4yg70t7813b) {
    this.f4yg70t7813b = f4yg70t7813b;
    return this;
  }

  public ControlGetAnimPropertyService fontSize(final float n) {
    if (Float.floatToIntBits(this.f5w2ocxla71e) == Float.floatToIntBits(n)) {
      return this;
    }
    this.f5w2ocxla71e = n;
    this.invalidateLayout();
    return this;
  }

  public ControlGetAnimPropertyService cornerRadius(final float fehkfxtewt6o) {
    this.fehkfxtewt6o = fehkfxtewt6o;
    return this;
  }

  public ControlGetAnimPropertyService onChange(final Consumer<Integer> onChange) {
    this.onChange = onChange;
    return this;
  }

  public ControlGetAnimPropertyService keyNameFn(final Function<Integer, String> text3) {
    if (this.text3 == text3) {
      return this;
    }
    this.text3 = text3;
    this.updateState();
    this.invalidateLayout();
    return this;
  }

  public int keyCode() {
    return this.fblhtbnm9trh;
  }

  public boolean isListening() {
    return this.enabled;
  }

  private void updateState() {
    if (this.text3 != null) {
      this.fhnudul1y2f = this.text3.apply(this.fblhtbnm9trh);
    } else if (this.fblhtbnm9trh == -1) {
      this.fhnudul1y2f = "None";
    } else {
      this.fhnudul1y2f = "Key" + this.fblhtbnm9trh;
    }
  }

  @Override
  protected void handleClick() {
    if (this.runnable != null) {
      this.enabled = false;
      this.runnable.run();
      return;
    }
    this.enabled = !this.enabled;
    if (this.onClick != null) {
      this.onClick.run();
    }
  }

  @Override
  protected void handleRightClick() {
    this.fblhtbnm9trh = -1;
    this.enabled = false;
    this.updateState();
    this.invalidateLayout();
    if (this.onChange != null) {
      this.onChange.accept(-1);
    }
    if (this.onRightClick != null) {
      this.onRightClick.run();
    }
  }

  public void acceptKey(final int fblhtbnm9trh) {
    if (fblhtbnm9trh == 256) {
      this.enabled = false;
      return;
    }
    this.fblhtbnm9trh = fblhtbnm9trh;
    this.enabled = false;
    this.updateState();
    this.invalidateLayout();
    if (this.onChange != null) {
      this.onChange.accept(this.fblhtbnm9trh);
    }
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    if (this.enabled2) {
      return Math.max(
          Float.intBitsToFloat(1111490560),
          compositorPushPresentationScaleService.textWidth(
                  this.fhnudul1y2f,
                  Float.intBitsToFloat(1096810496),
                  TextMode.REGULAR,
                  "material-roboto-medium",
                  Float.intBitsToFloat(1036831949))
              + Float.intBitsToFloat(1107296256));
    }
    return Math.max(
        Float.intBitsToFloat(1108344832),
        compositorPushPresentationScaleService.textWidth(this.fhnudul1y2f, this.f5w2ocxla71e)
            + Float.intBitsToFloat(1094713344));
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return this.enabled2
        ? Float.intBitsToFloat(1111490560)
        : (compositorPushPresentationScaleService.textLineHeight(this.f5w2ocxla71e)
            + Float.intBitsToFloat(1086324736));
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    final float effectiveOpacity = this.effectiveOpacity;
    if (this.enabled2) {
      if (this.pressed != this.fbrjruucfhts) {
        this.fbrjruucfhts = this.pressed;
        MotionAnimateService.animate(
            this,
            ControlGetAnimPropertyService.ff9i1ag5hhkx,
            this.pressed ? 1.0f : 0.0f,
            MaterialIsLightService.FAST_SPATIAL);
      }
      if (this.hovered != this.enabled5) {
        this.enabled5 = this.hovered;
        MotionAnimateService.animate(
            this,
            MotionColorsContainer.Floats.HOVER_PROGRESS,
            this.hovered ? 1.0f : 0.0f,
            MaterialIsLightService.FAST_EFFECTS);
      }
      if (this.enabled != this.f7t7uipxc2h) {
        this.f7t7uipxc2h = this.enabled;
        MotionAnimateService.animate(
            this,
            ControlGetAnimPropertyService.fhhnrdwz9i77,
            this.enabled ? 1.0f : 0.0f,
            MaterialIsLightService.FAST_EFFECTS);
      }
      final float max = Math.max(0.0f, Math.min(1.0f, this.value));
      final float max2 = Math.max(0.0f, Math.min(1.0f, this.finuxaglzsz9));
      int n =
          MaterialIsLightService.layer(
              MaterialIsLightService.ON_SECONDARY_CONTAINER,
              MaterialIsLightService.ON_PRIMARY_CONTAINER,
              max2);
      final int layer =
          MaterialIsLightService.layer(
              MaterialIsLightService.SECONDARY_CONTAINER,
              MaterialIsLightService.PRIMARY_CONTAINER,
              max2);
      int n2;
      if (this.interactive) {
        n2 =
            MaterialIsLightService.layer(
                layer,
                n,
                Float.intBitsToFloat(1034147594) * this.getAnimProperty("hoverProgress")
                    + Float.intBitsToFloat(1017370378) * max);
      } else {
        n2 =
            MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER,
                MaterialIsLightService.ON_SURFACE,
                Float.intBitsToFloat(1039516303));
        n =
            MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER,
                MaterialIsLightService.ON_SURFACE,
                Float.intBitsToFloat(1052938076));
      }
      final float n3 = Float.intBitsToFloat(1101004800) - Float.intBitsToFloat(1094713344) * max;
      compositorPushPresentationScaleService.roundedRect(
          this.cx,
          this.cy + Float.intBitsToFloat(1082130432),
          this.cw,
          this.ch - Float.intBitsToFloat(1090519040),
          n3,
          ScenePctService.mulAlpha(n2, effectiveOpacity));
      if (this.enabled3 && this.interactive) {
        compositorPushPresentationScaleService.roundedRect(
            this.cx - Float.intBitsToFloat(1077936128),
            this.cy + 1.0f,
            this.cw + Float.intBitsToFloat(1086324736),
            this.ch - 2.0f,
            n3 + Float.intBitsToFloat(1077936128),
            n3 + Float.intBitsToFloat(1077936128),
            n3 + Float.intBitsToFloat(1077936128),
            n3 + Float.intBitsToFloat(1077936128),
            0,
            0.0f,
            0.0f,
            0,
            2.0f,
            ScenePctService.mulAlpha(MaterialIsLightService.PRIMARY, effectiveOpacity),
            1.0f,
            Float.intBitsToFloat(1058642330));
      }
      String s = this.enabled ? "Press a key · Esc to cancel" : this.fhnudul1y2f;
      float n4 =
          compositorPushPresentationScaleService.textWidth(
              s,
              Float.intBitsToFloat(1096810496),
              TextMode.REGULAR,
              "material-roboto-medium",
              Float.intBitsToFloat(1036831949));
      if (this.enabled && n4 > this.cw - Float.intBitsToFloat(1107296256)) {
        s = "Press a key · Esc cancels";
        n4 =
            compositorPushPresentationScaleService.textWidth(
                s,
                Float.intBitsToFloat(1096810496),
                TextMode.REGULAR,
                "material-roboto-medium",
                Float.intBitsToFloat(1036831949));
      }
      if (this.enabled && n4 > this.cw - Float.intBitsToFloat(1107296256)) {
        s = "Press a key";
        n4 =
            compositorPushPresentationScaleService.textWidth(
                s,
                Float.intBitsToFloat(1096810496),
                TextMode.REGULAR,
                "material-roboto-medium",
                Float.intBitsToFloat(1036831949));
      }
      compositorPushPresentationScaleService.pushClip(
          this.cx + Float.intBitsToFloat(1098907648),
          this.cy,
          this.cw - Float.intBitsToFloat(1107296256),
          this.ch);
      compositorPushPresentationScaleService.text(
          this.cx + (this.cw - n4) / 2.0f,
          this.cy
              + (this.ch
                      - compositorPushPresentationScaleService.textLineHeight(
                          Float.intBitsToFloat(1096810496),
                          TextMode.REGULAR,
                          "material-roboto-medium"))
                  / 2.0f,
          s,
          Float.intBitsToFloat(1096810496),
          ScenePctService.mulAlpha(n, effectiveOpacity),
          MaterialIsLightService.LABEL.withLetterSpacing(Float.intBitsToFloat(1036831949)));
      compositorPushPresentationScaleService.popClip();
      return;
    }
    final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
    final int n5 = this.enabled ? this.count3 : (this.hovered ? 822083583 : this.f6uswfhi9ezd);
    final float fehkfxtewt6o = this.fehkfxtewt6o;
    compositorPushPresentationScaleService.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        fehkfxtewt6o,
        fehkfxtewt6o,
        fehkfxtewt6o,
        fehkfxtewt6o,
        ScenePctService.mulAlpha(n5, effectiveOpacity),
        0.0f,
        0.0f,
        0,
        0.0f,
        0,
        1.0f,
        effectiveEdgeSoftness);
    final String s2 = this.enabled ? "..." : this.fhnudul1y2f;
    compositorPushPresentationScaleService.text(
        this.cx
            + (this.cw - compositorPushPresentationScaleService.textWidth(s2, this.f5w2ocxla71e))
                / 2.0f,
        this.cy + (this.ch - this.f5w2ocxla71e) / 2.0f,
        s2,
        this.f5w2ocxla71e,
        ScenePctService.mulAlpha(this.f4yg70t7813b, effectiveOpacity));
  }

  static {
    ff9i1ag5hhkx =
        MotionFiniteService.finite(
            "materialKeyPress",
            scenePctService -> scenePctService instanceof ControlGetAnimPropertyService);
    fhhnrdwz9i77 =
        MotionFiniteService.finite(
            "materialKeyListening",
            scenePctService2 -> scenePctService2 instanceof ControlGetAnimPropertyService);
  }
}
