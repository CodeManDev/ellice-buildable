package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialCompactToggleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;

public class RangeSliderControl extends ScenePctService<RangeSliderControl> {
  protected boolean materialStyle;
  private boolean enabled;
  private boolean enabled2;
  private float value2;
  private boolean enabled3;
  private static final MotionFiniteService faijcs111pik;
  private float fdmerwyiuxhp;
  private float value4;
  private float fflpkutsgnvy;
  private float ffq2yswvtbjr;
  private float f8nsuscisrer;
  private float fibjanmz4h9h;
  private float value9;
  private int ffmgisnpy9nr;
  private int f2nru51c74ox;
  private int ffchjhmyi1ye;
  private int f1znnkghq3j4;
  private String f5r5wxypi85x;
  private int fg85ce7htpbw;
  private float f44uc3wexvw5;
  private float fg7jsmgqitxs;
  private float f5jm9o0fuqbo;
  private float fa7hwrdw80z4;
  private float fdprwlxf8efe;
  private float fg3vq97u6mab;
  private static final SceneEaseHandler.Spring f5to2t65p6sd;
  private BiConsumer<Float, Float> fikgbkllgkh5;
  private boolean enabled4;
  private boolean enabled5;

  public RangeSliderControl compact(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public RangeSliderControl material(final boolean materialStyle) {
    this.materialStyle = materialStyle;
    this.invalidate();
    return this;
  }

  public void materialFocus(final boolean enabled2) {
    this.enabled2 = enabled2;
    this.invalidate();
  }

  private void updateState() {
    if (this.pressed != this.enabled3) {
      this.enabled3 = this.pressed;
      MotionAnimateService.animate(
          this,
          RangeSliderControl.faijcs111pik,
          this.pressed ? 1.0f : 0.0f,
          this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
    }
  }

  public RangeSliderControl() {
    this.fibjanmz4h9h = 1.0f;
    this.ffmgisnpy9nr = 637534207;
    this.f2nru51c74ox = -10262799;
    this.ffchjhmyi1ye = -1;
    this.f1znnkghq3j4 = 805306368;
    this.f5r5wxypi85x = "%.1f";
    this.fg85ce7htpbw = -1426063361;
    this.f44uc3wexvw5 = Float.intBitsToFloat(1090519040);
    this.fg7jsmgqitxs = Float.intBitsToFloat(1082130432);
    this.f5jm9o0fuqbo = Float.intBitsToFloat(1077936128);
    this.fa7hwrdw80z4 = 2.0f;
    this.fdprwlxf8efe = Float.intBitsToFloat(-1082130432);
    this.fg3vq97u6mab = 1.0f;
    this.interactive = true;
  }

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  public RangeSliderControl low(final float n) {
    if (!Float.isFinite(n)) {
      return this;
    }
    this.fflpkutsgnvy = n;
    this.updateState2(false);
    return this;
  }

  public RangeSliderControl high(final float n) {
    if (!Float.isFinite(n)) {
      return this;
    }
    this.ffq2yswvtbjr = n;
    this.updateState2(false);
    return this;
  }

  public RangeSliderControl values(final float n, final float n2) {
    if (!Float.isFinite(n) || !Float.isFinite(n2)) {
      return this;
    }
    this.fflpkutsgnvy = n;
    this.ffq2yswvtbjr = n2;
    this.updateState2(false);
    return this;
  }

  public RangeSliderControl range(final float n, final float n2) {
    if (!Float.isFinite(n) || !Float.isFinite(n2) || n2 < n) {
      throw new IllegalArgumentException("range slider domain must be finite and max >= min");
    }
    if (m4lspwbaavan(this.f8nsuscisrer, n) && m4lspwbaavan(this.fibjanmz4h9h, n2)) {
      return this;
    }
    this.f8nsuscisrer = n;
    this.fibjanmz4h9h = n2;
    this.updateState2(false);
    this.invalidateLayout();
    return this;
  }

  public RangeSliderControl step(final float n) {
    if (!Float.isFinite(n) || n < 0.0f) {
      throw new IllegalArgumentException("range slider step must be finite and >= 0");
    }
    if (m4lspwbaavan(this.value9, n)) {
      return this;
    }
    this.value9 = n;
    this.updateState2(false);
    this.invalidate();
    return this;
  }

  public RangeSliderControl trackColor(final int ffmgisnpy9nr) {
    if (this.ffmgisnpy9nr != ffmgisnpy9nr) {
      this.ffmgisnpy9nr = ffmgisnpy9nr;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl fillColor(final int f2nru51c74ox) {
    if (this.f2nru51c74ox != f2nru51c74ox) {
      this.f2nru51c74ox = f2nru51c74ox;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl thumbColor(final int ffchjhmyi1ye) {
    if (this.ffchjhmyi1ye != ffchjhmyi1ye) {
      this.ffchjhmyi1ye = ffchjhmyi1ye;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl thumbShadowColor(final int f1znnkghq3j4) {
    if (this.f1znnkghq3j4 != f1znnkghq3j4) {
      this.f1znnkghq3j4 = f1znnkghq3j4;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl format(final String s) {
    if (Objects.equals(this.f5r5wxypi85x, s)) {
      return this;
    }
    this.f5r5wxypi85x = Objects.requireNonNull(s, "format");
    this.invalidateLayout();
    return this;
  }

  public RangeSliderControl textColor(final int fg85ce7htpbw) {
    if (this.fg85ce7htpbw != fg85ce7htpbw) {
      this.fg85ce7htpbw = fg85ce7htpbw;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl fontSize(final float n) {
    if (Float.floatToIntBits(this.f44uc3wexvw5) == Float.floatToIntBits(n)) {
      return this;
    }
    this.f44uc3wexvw5 = n;
    this.invalidateLayout();
    return this;
  }

  public RangeSliderControl trackHeight(final float n) {
    if (Float.floatToIntBits(this.fg7jsmgqitxs) == Float.floatToIntBits(n)) {
      return this;
    }
    this.fg7jsmgqitxs = n;
    this.invalidateLayout();
    return this;
  }

  public RangeSliderControl thumbRadius(final float f5jm9o0fuqbo) {
    if (!m4lspwbaavan(this.f5jm9o0fuqbo, f5jm9o0fuqbo)) {
      this.f5jm9o0fuqbo = f5jm9o0fuqbo;
      this.invalidateLayout();
    }
    return this;
  }

  public RangeSliderControl thumbShadow(final float fa7hwrdw80z4) {
    if (!m4lspwbaavan(this.fa7hwrdw80z4, fa7hwrdw80z4)) {
      this.fa7hwrdw80z4 = fa7hwrdw80z4;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl trackRadius(final float fdprwlxf8efe) {
    if (!m4lspwbaavan(this.fdprwlxf8efe, fdprwlxf8efe)) {
      this.fdprwlxf8efe = fdprwlxf8efe;
      this.invalidate();
    }
    return this;
  }

  public RangeSliderControl onChange(final BiConsumer<Float, Float> fikgbkllgkh5) {
    this.fikgbkllgkh5 = fikgbkllgkh5;
    return this;
  }

  public float low() {
    return this.fdmerwyiuxhp;
  }

  public float high() {
    return this.value4;
  }

  @Override
  public float getAnimProperty(final String s) {
    if ("materialPress".equals(s)) {
      return this.value2;
    }
    if ("thumbScale".equals(s)) {
      return this.fg3vq97u6mab;
    }
    return super.getAnimProperty(s);
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    if ("materialPress".equals(s)) {
      this.value2 = n;
      return;
    }
    if ("thumbScale".equals(s)) {
      this.fg3vq97u6mab = n;
      return;
    }
    super.setAnimProperty(s, n);
  }

  private float calculateValue(float max) {
    max = Math.max(this.f8nsuscisrer, Math.min(this.fibjanmz4h9h, max));
    if (this.value9 > 0.0f) {
      max =
          this.f8nsuscisrer
              + Math.round((max - this.f8nsuscisrer) / this.value9) * this.value9;
    }
    return Math.max(this.f8nsuscisrer, Math.min(this.fibjanmz4h9h, max));
  }

  private static boolean m4lspwbaavan(final float value, final float value2) {
    return Float.floatToIntBits(value) == Float.floatToIntBits(value2);
  }

  private void updateState2(final boolean b) {
    this.updateState3(
        this.calculateValue(Math.min(this.fflpkutsgnvy, this.ffq2yswvtbjr)),
        this.calculateValue(Math.max(this.fflpkutsgnvy, this.ffq2yswvtbjr)),
        b);
  }

  private void updateState3(final float fdmerwyiuxhp, final float value4, final boolean b) {
    if (m4lspwbaavan(this.fdmerwyiuxhp, fdmerwyiuxhp)
        && m4lspwbaavan(this.value4, value4)) {
      return;
    }
    this.fdmerwyiuxhp = fdmerwyiuxhp;
    this.value4 = value4;
    this.invalidate();
    if (b && this.fikgbkllgkh5 != null) {
      this.fikgbkllgkh5.accept(this.fdmerwyiuxhp, this.value4);
    }
  }

  private float calculateValue2(final float n) {
    return (this.fibjanmz4h9h > this.f8nsuscisrer)
        ? ((n - this.f8nsuscisrer) / (this.fibjanmz4h9h - this.f8nsuscisrer))
        : 0.0f;
  }

  @Override
  protected void updateWhilePressed(final float n, final float n2) {
    if (this.cw <= 0.0f || this.fibjanmz4h9h <= this.f8nsuscisrer) {
      return;
    }
    final float n3 =
        this.materialStyle
            ? 2.0f
            : (this.f5jm9o0fuqbo * Math.max(0.0f, Math.min(2.0f, this.fg3vq97u6mab)));
    final float calculateValue =
        this.calculateValue(
            this.f8nsuscisrer
                + Math.max(
                        0.0f,
                        Math.min(1.0f, (n - (this.cx + n3)) / Math.max(1.0f, this.cw - n3 * 2.0f)))
                    * (this.fibjanmz4h9h - this.f8nsuscisrer));
    if (!this.enabled5) {
      final float abs = Math.abs(calculateValue - this.fdmerwyiuxhp);
      final float abs2 = Math.abs(calculateValue - this.value4);
      this.enabled4 =
          (abs2 < abs || (m4lspwbaavan(abs2, abs) && calculateValue >= this.value4));
      this.enabled5 = true;
    }
    float fflpkutsgnvy = this.fdmerwyiuxhp;
    float ffq2yswvtbjr = this.value4;
    if (this.enabled4) {
      ffq2yswvtbjr = Math.max(this.fdmerwyiuxhp, calculateValue);
    } else {
      fflpkutsgnvy = Math.min(calculateValue, this.value4);
    }
    this.updateState3(this.fflpkutsgnvy = fflpkutsgnvy, this.ffq2yswvtbjr = ffq2yswvtbjr, true);
  }

  @Override
  protected boolean handleScroll(final float f) {
    if (!Float.isFinite(f) || this.fibjanmz4h9h <= this.f8nsuscisrer) {
      return false;
    }
    final float n =
        (this.value9 > 0.0f)
            ? this.value9
            : ((this.fibjanmz4h9h - this.f8nsuscisrer) / Float.intBitsToFloat(1112014848));
    float f8nsuscisrer = this.fdmerwyiuxhp + f * n;
    float fibjanmz4h9h = this.value4 + f * n;
    if (f8nsuscisrer < this.f8nsuscisrer) {
      fibjanmz4h9h += this.f8nsuscisrer - f8nsuscisrer;
      f8nsuscisrer = this.f8nsuscisrer;
    }
    if (fibjanmz4h9h > this.fibjanmz4h9h) {
      f8nsuscisrer -= fibjanmz4h9h - this.fibjanmz4h9h;
      fibjanmz4h9h = this.fibjanmz4h9h;
    }
    this.updateState3(
        this.fflpkutsgnvy = this.calculateValue(f8nsuscisrer),
        this.ffq2yswvtbjr = this.calculateValue(fibjanmz4h9h),
        true);
    return true;
  }

  @Override
  protected void onRelease(final boolean b) {
    this.enabled5 = false;
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1120403456);
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return this.materialStyle
        ? Float.intBitsToFloat(1111490560)
        : Math.max(
            this.fg7jsmgqitxs + 2.0f,
            this.f44uc3wexvw5 + this.fg7jsmgqitxs + Float.intBitsToFloat(1082130432));
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    if (this.materialStyle) {
      this.updateState();
      if (this.enabled) {
        MaterialCompactToggleService.compactSlider(
            compositorPushPresentationScaleService,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.calculateValue2(this.fdmerwyiuxhp),
            this.calculateValue2(this.value4),
            true,
            this.enabled4,
            this.value2,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      } else {
        MaterialCompactToggleService.slider(
            compositorPushPresentationScaleService,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.calculateValue2(this.fdmerwyiuxhp),
            this.calculateValue2(this.value4),
            true,
            this.enabled4,
            this.value2,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      }
      return;
    }
    final float effectiveOpacity = this.effectiveOpacity;
    final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
    if (!this.pressed) {
      this.enabled5 = false;
    }
    final float n = this.cy + this.ch - this.fg7jsmgqitxs - 1.0f;
    final float n2 = (this.fdprwlxf8efe >= 0.0f) ? this.fdprwlxf8efe : (this.fg7jsmgqitxs / 2.0f);
    final float calculateValue2 = this.calculateValue2(this.fdmerwyiuxhp);
    final float m25diw8gwgwt2 = this.calculateValue2(this.value4);
    final float n3 = (this.hovered || this.pressed) ? Float.intBitsToFloat(1068708659) : 1.0f;
    if (Math.abs(this.fg3vq97u6mab - n3) > Float.intBitsToFloat(1028443341)) {
      this.animate("thumbScale", n3, RangeSliderControl.f5to2t65p6sd);
    }
    compositorPushPresentationScaleService.text(
        this.cx,
        this.cy,
        String.format(
            Locale.ROOT,
            this.f5r5wxypi85x + " – " + this.f5r5wxypi85x,
            this.fdmerwyiuxhp,
            this.value4),
        this.f44uc3wexvw5,
        ScenePctService.mulAlpha(this.fg85ce7htpbw, effectiveOpacity));
    compositorPushPresentationScaleService.roundedRect(
        this.cx,
        n,
        this.cw,
        this.fg7jsmgqitxs,
        n2,
        n2,
        n2,
        n2,
        ScenePctService.mulAlpha(this.ffmgisnpy9nr, effectiveOpacity),
        0.0f,
        0.0f,
        0,
        0.0f,
        0,
        1.0f,
        effectiveEdgeSoftness);
    final float n4 = this.f5jm9o0fuqbo * 2.0f * Math.max(0.0f, Math.min(2.0f, this.fg3vq97u6mab));
    final float n5 = this.cx + n4 / 2.0f;
    final float max = Math.max(0.0f, this.cw - n4);
    final float n6 = n5 + max * calculateValue2;
    final float n7 = n5 + max * m25diw8gwgwt2;
    compositorPushPresentationScaleService.roundedRect(
        n6,
        n,
        Math.max(this.fg7jsmgqitxs, n7 - n6),
        this.fg7jsmgqitxs,
        n2,
        n2,
        n2,
        n2,
        ScenePctService.mulAlpha(this.f2nru51c74ox, effectiveOpacity),
        0.0f,
        0.0f,
        0,
        0.0f,
        0,
        1.0f,
        effectiveEdgeSoftness);
    this.updateState4(
        compositorPushPresentationScaleService, n6, n, n4, effectiveOpacity, effectiveEdgeSoftness);
    this.updateState4(
        compositorPushPresentationScaleService, n7, n, n4, effectiveOpacity, effectiveEdgeSoftness);
  }

  private void updateState4(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final float n,
      final float n2,
      final float n3,
      final float n4,
      final float n5) {
    final float n6 = n3 / 2.0f;
    compositorPushPresentationScaleService.roundedRect(
        n - n6,
        n2 + (this.fg7jsmgqitxs - n3) / 2.0f,
        n3,
        n3,
        n6,
        n6,
        n6,
        n6,
        ScenePctService.mulAlpha(this.ffchjhmyi1ye, n4),
        0.0f,
        (n4 > Float.intBitsToFloat(1041865114)) ? this.fa7hwrdw80z4 : 0.0f,
        ScenePctService.mulAlpha(this.f1znnkghq3j4, n4),
        0.0f,
        0,
        1.0f,
        n5);
  }

  static {
    faijcs111pik =
        MotionFiniteService.of(
            "materialPress",
            scenePctService -> scenePctService instanceof RangeSliderControl,
            Float::isFinite,
            "a finite value");
    f5to2t65p6sd =
        new SceneEaseHandler.Spring(
            Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1142292480));
  }
}
