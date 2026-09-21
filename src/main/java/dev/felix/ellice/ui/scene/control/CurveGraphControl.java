package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class CurveGraphControl extends ScenePctService<CurveGraphControl> {
  private boolean enabled;
  private float value2;
  private boolean enabled3;
  private Handle NONE;
  private static final MotionFiniteService f4jqnc2913qv;
  private static final CompositorPushPresentationScaleService.CurveGraphStyle f9dklpowc9bc;
  public static final float DEFAULT_WIDTH = 220.0f;
  public static final float DEFAULT_HEIGHT = 138.0f;
  private static final float fbvyabyhfokl = 24.0f;
  private static final float f5g7bkc975bd = 10.0f;
  private static final float f4ydedkaxz98 = 21.0f;
  private static final float f2gwydhjbktb = 18.0f;
  private static final float fjkzemxuqu6i = 12.0f;
  private static final int fjcoxdgbvhyw = -1342177281;
  private static final int count2 = -536870913;
  private static final List<String> f3pn7wr985gf;
  private static final float[] f40zatfqczr7;
  private static final List<String> fisaqr9d4xv2;
  private static final float[] f94m5mye412l;
  private ModuleSetting.CurveValue moduleCurveValue;
  private String text4;
  private String text5;
  private String fboscpcjym5x;
  private String fabkdnsbdaci;
  private ModuleSetting.CurveValue moduleCurveValue2;
  private Consumer<ModuleSetting.CurveValue> moduleConsumer;
  private Consumer<ModuleSetting.CurveValue> consumer;
  private Runnable fgpxxrqyqg5g;
  private Handle handle2;
  private final Plot plot;
  private boolean enabled4;
  private boolean currentEnabled;
  private boolean enabled6;

  @Override
  protected boolean handlesContinuousPointer() {
    return true;
  }

  @Override
  public float getAnimProperty(final String anObject) {
    return "materialCurveGrab".equals(anObject) ? this.value2 : super.getAnimProperty(anObject);
  }

  @Override
  public void setAnimProperty(final String anObject, final float value2) {
    if ("materialCurveGrab".equals(anObject)) {
      this.value2 = value2;
    } else {
      super.setAnimProperty(anObject, value2);
    }
  }

  public CurveGraphControl material(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public CurveGraphControl() {
    this(ModuleSetting.CurveValue.linear());
  }

  public CurveGraphControl(final ModuleSetting.CurveValue obj) {
    this.NONE = Handle.NONE;
    this.handle2 = Handle.NONE;
    this.plot = new Plot();
    this.currentEnabled = true;
    this.moduleCurveValue = Objects.requireNonNull(obj, "value");
    this.mcqdhlvlpdpn();
    this.interactive = true;
    this.cursorStyle(CursorStyle.POINTER);
    this.size(Float.intBitsToFloat(1130102784), Float.intBitsToFloat(1124728832));
    this.flexShrink(0.0f);
  }

  public ModuleSetting.CurveValue value() {
    return this.moduleCurveValue;
  }

  public CurveGraphControl value(final ModuleSetting.CurveValue obj) {
    final ModuleSetting.CurveValue curveValue = Objects.requireNonNull(obj, "value");
    if (Objects.equals(this.moduleCurveValue, curveValue)) {
      return this;
    }
    this.moduleCurveValue = curveValue;
    this.mcqdhlvlpdpn();
    this.invalidate();
    this.moduleCurveValue2 = null;
    this.enabled4 = false;
    this.handle2 = Handle.NONE;
    return this;
  }

  public CurveGraphControl onChange(final Consumer<ModuleSetting.CurveValue> moduleConsumer) {
    this.moduleConsumer = moduleConsumer;
    return this;
  }

  public CurveGraphControl onCommit(final Consumer<ModuleSetting.CurveValue> consumer) {
    this.consumer = consumer;
    return this;
  }

  public CurveGraphControl onInteraction(final Runnable fgpxxrqyqg5g) {
    this.fgpxxrqyqg5g = fgpxxrqyqg5g;
    return this;
  }

  public CurveGraphControl enabled(final boolean currentEnabled) {
    if (this.currentEnabled == currentEnabled) {
      return this;
    }
    this.currentEnabled = currentEnabled;
    this.interactive = (currentEnabled && !this.enabled6);
    this.pointerEvents = (currentEnabled && !this.enabled6);
    this.cursorStyle(
        (currentEnabled && !this.enabled6) ? CursorStyle.POINTER : CursorStyle.DEFAULT);
    if (!currentEnabled) {
      this.cancelGesture();
    }
    this.invalidate();
    return this;
  }

  public boolean enabled() {
    return this.currentEnabled && !this.enabled6;
  }

  public boolean isDragging() {
    return this.handle2 != Handle.NONE;
  }

  public Handle activeHandle() {
    return this.handle2;
  }

  public String primaryValueLabel() {
    return this.text4;
  }

  public String secondaryValueLabel() {
    return this.text5;
  }

  public List<String> xAxisLabels() {
    return CurveGraphControl.f3pn7wr985gf;
  }

  public List<String> yAxisLabels() {
    return CurveGraphControl.fisaqr9d4xv2;
  }

  public CurveGraphControl cancelGesture() {
    final boolean b =
        this.handle2 != Handle.NONE || this.moduleCurveValue2 != null || this.enabled4;
    this.handle2 = Handle.NONE;
    this.moduleCurveValue2 = null;
    this.enabled4 = false;
    if (b) {
      this.invalidate();
    }
    return this;
  }

  public boolean previewHandle(final Handle handle, final float f, final float f2) {
    if (!this.enabled()
        || this.moduleCurveValue.type() != ModuleSetting.CurveType.CUBIC_BEZIER
        || handle == null
        || handle == Handle.NONE
        || !Float.isFinite(f)
        || !Float.isFinite(f2)) {
      return false;
    }
    float mflv5ik364rn = mflv5ik364rn(f, 0.0f, 1.0f);
    float mflv5ik364rn2 = mflv5ik364rn(f2, Float.intBitsToFloat(-1073741824), 2.0f);
    final float n =
        (handle == Handle.FIRST) ? this.moduleCurveValue.x1() : this.moduleCurveValue.x2();
    final float n2 =
        (handle == Handle.FIRST) ? this.moduleCurveValue.y1() : this.moduleCurveValue.y2();
    if (mgs8xrgpaj0t(mflv5ik364rn, n)) {
      mflv5ik364rn = n;
    }
    if (mgs8xrgpaj0t(mflv5ik364rn2, n2)) {
      mflv5ik364rn2 = n2;
    }
    if (this.moduleCurveValue2 != null) {
      final float n3 =
          (handle == Handle.FIRST) ? this.moduleCurveValue2.x1() : this.moduleCurveValue2.x2();
      final float n4 =
          (handle == Handle.FIRST) ? this.moduleCurveValue2.y1() : this.moduleCurveValue2.y2();
      if (mgs8xrgpaj0t(mflv5ik364rn, n3)) {
        mflv5ik364rn = n3;
      }
      if (mgs8xrgpaj0t(mflv5ik364rn2, n4)) {
        mflv5ik364rn2 = n4;
      }
    }
    final ModuleSetting.CurveValue b =
        (handle == Handle.FIRST)
            ? ModuleSetting.CurveValue.cubicBezier(
                mflv5ik364rn, mflv5ik364rn2, this.moduleCurveValue.x2(), this.moduleCurveValue.y2())
            : ModuleSetting.CurveValue.cubicBezier(
                this.moduleCurveValue.x1(),
                this.moduleCurveValue.y1(),
                mflv5ik364rn,
                mflv5ik364rn2);
    if (Objects.equals(b, this.moduleCurveValue)) {
      return false;
    }
    this.moduleCurveValue = b;
    this.mcqdhlvlpdpn();
    this.enabled4 = (this.moduleCurveValue2 == null || !Objects.equals(this.moduleCurveValue2, b));
    this.invalidate();
    if (this.moduleConsumer != null) {
      this.moduleConsumer.accept(b);
    }
    return true;
  }

  public Point handleCenter(final Handle handle) {
    if (handle == null
        || handle == Handle.NONE
        || this.moduleCurveValue.type() != ModuleSetting.CurveType.CUBIC_BEZIER) {
      return null;
    }
    final Plot createPlot = this.createPlot(this.cx, this.cy, this.cw, this.ch, 1.0f);
    return new Point(
        mj6mm1rkpoae(
            createPlot,
            (handle == Handle.FIRST) ? this.moduleCurveValue.x1() : this.moduleCurveValue.x2()),
        mags5k7oczhq(
            createPlot,
            (handle == Handle.FIRST) ? this.moduleCurveValue.y1() : this.moduleCurveValue.y2()));
  }

  @Override
  protected void onPress(final float n, final float n2) {
    if (!this.enabled()) {
      return;
    }
    if (this.fgpxxrqyqg5g != null) {
      this.fgpxxrqyqg5g.run();
    }
    if (this.moduleCurveValue.type() != ModuleSetting.CurveType.CUBIC_BEZIER
        || this.cw <= 0.0f
        || this.ch <= 0.0f) {
      this.handle2 = Handle.NONE;
      return;
    }
    final Point handleCenter = this.handleCenter(Handle.FIRST);
    final Point handleCenter2 = this.handleCenter(Handle.SECOND);
    final float mslulprvbkl = mslulprvbkl(n, n2, handleCenter.x(), handleCenter.y());
    final float mslulprvbkl2 = mslulprvbkl(n, n2, handleCenter2.x(), handleCenter2.y());
    if (Math.min(mslulprvbkl, mslulprvbkl2) > Float.intBitsToFloat(1125122048)) {
      this.handle2 = Handle.NONE;
      return;
    }
    this.handle2 = ((mslulprvbkl <= mslulprvbkl2) ? Handle.FIRST : Handle.SECOND);
    this.moduleCurveValue2 = this.moduleCurveValue;
    this.enabled4 = false;
  }

  @Override
  protected void updateWhilePressed(final float n, final float n2) {
    if (!this.enabled() || this.handle2 == Handle.NONE || this.cw <= 0.0f || this.ch <= 0.0f) {
      return;
    }
    final Plot createPlot = this.createPlot(this.cx, this.cy, this.cw, this.ch, 1.0f);
    if (createPlot.faabivmzwvyv <= 0.0f || createPlot.value11 <= 0.0f) {
      return;
    }
    this.previewHandle(this.handle2, m8b7qwepv0xx(createPlot, n), mgbww7vox9gb(createPlot, n2));
  }

  @Override
  protected void onRelease(final boolean b) {
    final ModuleSetting.CurveValue moduleCurveValue = this.moduleCurveValue;
    final boolean b2 = this.handle2 != Handle.NONE && this.enabled4;
    this.handle2 = Handle.NONE;
    this.moduleCurveValue2 = null;
    this.enabled4 = false;
    if (b2 && this.enabled() && this.consumer != null) {
      this.consumer.accept(moduleCurveValue);
    }
  }

  @Override
  protected boolean handleScroll(final float n) {
    return false;
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1130102784);
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1124728832);
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    if (this.effectiveOpacity < Float.intBitsToFloat(994352038)
        || this.cw <= 0.0f
        || this.ch <= 0.0f) {
      return;
    }
    final Plot createPlot =
        this.createPlot(this.cx, this.cy, this.cw, this.ch, this.presentationScale);
    compositorPushPresentationScaleService.curveGraph(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        createPlot.value8,
        createPlot.value9,
        createPlot.faabivmzwvyv,
        createPlot.value11,
        this.enabled ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1086324736),
        this.moduleCurveValue,
        this.calculateValue(),
        this.effectiveOpacity,
        this.effectiveEdgeSoftness,
        this.enabled
            ? CurveGraphControl.f9dklpowc9bc
            : CompositorPushPresentationScaleService.DEFAULT_CURVE_GRAPH_STYLE);
    this.mf29zrsd0eru(compositorPushPresentationScaleService, createPlot, this.effectiveOpacity);
    if (this.enabled) {
      final boolean enabled3 = this.handle2 != Handle.NONE;
      if (enabled3) {
        this.NONE = this.handle2;
      }
      if (enabled3 != this.enabled3) {
        this.enabled3 = enabled3;
        MotionAnimateService.animate(
            this,
            CurveGraphControl.f4jqnc2913qv,
            enabled3 ? 1.0f : 0.0f,
            MaterialIsLightService.FAST_SPATIAL);
      }
      final Point handleCenter = this.handleCenter(this.NONE);
      final float max = Math.max(0.0f, Math.min(Float.intBitsToFloat(1065856532), this.value2));
      if (handleCenter != null && max > Float.intBitsToFloat(981668463)) {
        final float n = Float.intBitsToFloat(1084227584) + Float.intBitsToFloat(1084227584) * max;
        compositorPushPresentationScaleService.overlayRect(
            handleCenter.x - n,
            handleCenter.y - n,
            2.0f * n,
            2.0f * n,
            n,
            n,
            n,
            n,
            ScenePctService.mulAlpha(
                MaterialIsLightService.PRIMARY,
                Float.intBitsToFloat(1039516303) * max * this.effectiveOpacity),
            0.0f,
            0.0f,
            0,
            Float.intBitsToFloat(1069547520) * max,
            ScenePctService.mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity));
      }
    }
  }

  @Override
  protected void onDetached() {
    this.dispose();
  }

  public void dispose() {
    if (this.enabled6) {
      return;
    }
    this.enabled6 = true;
    this.currentEnabled = false;
    this.interactive = false;
    this.pointerEvents = false;
    this.cancelGesture();
    this.moduleConsumer = null;
    this.consumer = null;
    this.fgpxxrqyqg5g = null;
  }

  private void mf29zrsd0eru(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final Plot plot,
      final float n) {
    final float n2 =
        this.enabled ? Float.intBitsToFloat(1094713344) : Float.intBitsToFloat(1087897600);
    final TextData textData = this.enabled ? MaterialIsLightService.BODY : TextData.NONE;
    final float textLineHeight = compositorPushPresentationScaleService.textLineHeight(n2);
    final int mulAlpha = ScenePctService.mulAlpha(-1342177281, n);
    final int mulAlpha2 = ScenePctService.mulAlpha(-536870913, n);
    final float n3 = this.cx + Float.intBitsToFloat(1077936128) * this.presentationScale;
    for (int i = 0; i < CurveGraphControl.fisaqr9d4xv2.size(); ++i) {
      compositorPushPresentationScaleService.text(
          n3,
          mflv5ik364rn(
              mags5k7oczhq(plot, CurveGraphControl.f94m5mye412l[i])
                  - textLineHeight * Float.intBitsToFloat(1056964608),
              this.cy + 2.0f * this.presentationScale,
              this.cy + this.ch - textLineHeight - 2.0f * this.presentationScale),
          CurveGraphControl.fisaqr9d4xv2.get(i),
          n2,
          mulAlpha,
          textData);
    }
    final float min =
        Math.min(
            this.cy + this.ch - textLineHeight - 2.0f * this.presentationScale,
            plot.value9 + plot.value11 + Float.intBitsToFloat(1077936128) * this.presentationScale);
    for (int j = 0; j < CurveGraphControl.f3pn7wr985gf.size(); ++j) {
      final String s = CurveGraphControl.f3pn7wr985gf.get(j);
      final float textWidth =
          compositorPushPresentationScaleService.textWidth(
              s, n2, TextMode.REGULAR, this.enabled ? "material-roboto" : null);
      compositorPushPresentationScaleService.text(
          mflv5ik364rn(
              mj6mm1rkpoae(plot, CurveGraphControl.f40zatfqczr7[j])
                  - textWidth * Float.intBitsToFloat(1056964608),
              this.cx + 2.0f * this.presentationScale,
              this.cx + this.cw - textWidth - 2.0f * this.presentationScale),
          min,
          s,
          n2,
          mulAlpha,
          textData);
    }
    String s2 = this.text4;
    String s3 = this.text5;
    if (this.moduleCurveValue.type() == ModuleSetting.CurveType.CUBIC_BEZIER
        && plot.faabivmzwvyv < Float.intBitsToFloat(1123680256) * this.presentationScale) {
      s2 = this.fboscpcjym5x;
      s3 = this.fabkdnsbdaci;
    }
    final float n4 = this.cy + Float.intBitsToFloat(1084227584) * this.presentationScale;
    compositorPushPresentationScaleService.text(plot.value8, n4, s2, n2, mulAlpha2, textData);
    if (!s3.isEmpty()) {
      compositorPushPresentationScaleService.text(
          Math.max(
              plot.value8,
              plot.value8
                  + plot.faabivmzwvyv
                  - compositorPushPresentationScaleService.textWidth(
                      s3, n2, TextMode.REGULAR, this.enabled ? "material-roboto" : null)),
          n4,
          s3,
          n2,
          mulAlpha2,
          textData);
    }
  }

  private void mcqdhlvlpdpn() {
    switch (this.moduleCurveValue.type()) {
      case LINEAR:
        {
          this.text4 = "y = x";
          this.text5 = "";
          this.fboscpcjym5x = this.text4;
          this.fabkdnsbdaci = this.text5;
          break;
        }
      case CUBIC_BEZIER:
        {
          this.text4 =
              String.format(
                  Locale.ROOT,
                  "P1 %.2f, %.2f",
                  this.moduleCurveValue.x1(),
                  this.moduleCurveValue.y1());
          this.text5 =
              String.format(
                  Locale.ROOT,
                  "P2 %.2f, %.2f",
                  this.moduleCurveValue.x2(),
                  this.moduleCurveValue.y2());
          this.fboscpcjym5x =
              String.format(
                  Locale.ROOT,
                  "P1 %.2f/%.2f",
                  this.moduleCurveValue.x1(),
                  this.moduleCurveValue.y1());
          this.fabkdnsbdaci =
              String.format(
                  Locale.ROOT,
                  "P2 %.2f/%.2f",
                  this.moduleCurveValue.x2(),
                  this.moduleCurveValue.y2());
          break;
        }
      case STEPS:
        {
          this.text4 =
              this.moduleCurveValue.steps()
                  + ((this.moduleCurveValue.steps() == 1) ? " step" : " steps");
          this.text5 =
              switch (this.moduleCurveValue.stepMode()) {
                default -> throw new MatchException(null, null);
                case JUMP_START -> "jump start";
                case JUMP_END -> "jump end";
                case JUMP_NONE -> "jump none";
                case JUMP_BOTH -> "jump both";
              };
          this.fboscpcjym5x = this.text4;
          this.fabkdnsbdaci = this.text5;
          break;
        }
    }
  }

  private int calculateValue() {
    return switch (this.handle2.ordinal()) {
      default -> throw new MatchException(null, null);
      case 0 -> 0;
      case 1 -> 1;
      case 2 -> 2;
    };
  }

  private Plot createPlot(
      final float n, final float n2, final float n3, final float n4, final float n5) {
    final float n6 = Float.isFinite(n5) ? Math.max(0.0f, n5) : 1.0f;
    float min =
        Math.min(
            (this.enabled ? Float.intBitsToFloat(1107296256) : Float.intBitsToFloat(1103101952))
                * n6,
            n3 * Float.intBitsToFloat(1049582633));
    float min2 =
        Math.min(
            (this.enabled ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1092616192))
                * n6,
            n3 * Float.intBitsToFloat(1039516303));
    float min3 =
        Math.min(
            (this.enabled ? Float.intBitsToFloat(1107296256) : Float.intBitsToFloat(1101529088))
                * n6,
            n4 * Float.intBitsToFloat(1050924810));
    float min4 =
        Math.min(
            (this.enabled ? Float.intBitsToFloat(1105199104) : Float.intBitsToFloat(1099956224))
                * n6,
            n4 * Float.intBitsToFloat(1049582633));
    final float b = min + min2;
    if (b > n3) {
      final float n7 = n3 / Math.max(Float.intBitsToFloat(953267991), b);
      min *= n7;
      min2 *= n7;
    }
    final float b2 = min3 + min4;
    if (b2 > n4) {
      final float n8 = n4 / Math.max(Float.intBitsToFloat(953267991), b2);
      min3 *= n8;
      min4 *= n8;
    }
    this.plot.value8 = n + min;
    this.plot.value9 = n2 + min3;
    this.plot.faabivmzwvyv = Math.max(0.0f, n3 - min - min2);
    this.plot.value11 = Math.max(0.0f, n4 - min3 - min4);
    return this.plot;
  }

  private static float mj6mm1rkpoae(final Plot plot, final float n) {
    return plot.value8 + mflv5ik364rn(n, 0.0f, 1.0f) * plot.faabivmzwvyv;
  }

  private static float m8b7qwepv0xx(final Plot plot, final float n) {
    if (plot.faabivmzwvyv <= 0.0f) {
      return 0.0f;
    }
    return mflv5ik364rn((n - plot.value8) / plot.faabivmzwvyv, 0.0f, 1.0f);
  }

  private static float mags5k7oczhq(final Plot plot, final float n) {
    return plot.value9
        + (1.0f
                - mflv5ik364rn(
                    mhm7m39fuh16(mflv5ik364rn(n, Float.intBitsToFloat(-1073741824), 2.0f)),
                    0.0f,
                    1.0f))
            * plot.value11;
  }

  private static float mgbww7vox9gb(final Plot plot, final float n) {
    if (plot.value11 <= 0.0f) {
      return 0.0f;
    }
    final float mflv5ik364rn = mflv5ik364rn(1.0f - (n - plot.value9) / plot.value11, 0.0f, 1.0f);
    float intBitsToFloat = Float.intBitsToFloat(-1073741824);
    float n2 = 2.0f;
    for (int i = 0; i < 22; ++i) {
      final float n3 = (intBitsToFloat + n2) * Float.intBitsToFloat(1056964608);
      if (mhm7m39fuh16(n3) < mflv5ik364rn) {
        intBitsToFloat = n3;
      } else {
        n2 = n3;
      }
    }
    return (intBitsToFloat + n2) * Float.intBitsToFloat(1056964608);
  }

  private static float mhm7m39fuh16(final float n) {
    if (n <= 0.0f) {
      return mel6nu9bu18j(
          (n + 2.0f) * Float.intBitsToFloat(1056964608),
          0.0f,
          Float.intBitsToFloat(1048576000),
          0.0f,
          Float.intBitsToFloat(1054567863));
    }
    if (n <= 1.0f) {
      return mel6nu9bu18j(
          n,
          Float.intBitsToFloat(1048576000),
          Float.intBitsToFloat(1061158912),
          Float.intBitsToFloat(1046179255),
          Float.intBitsToFloat(1051372203));
    }
    return mel6nu9bu18j(
        n - 1.0f,
        Float.intBitsToFloat(1061158912),
        1.0f,
        Float.intBitsToFloat(1051372203),
        Float.intBitsToFloat(1040187392));
  }

  private static float mel6nu9bu18j(
      final float n, final float n2, final float n3, final float n4, final float n5) {
    final float n6 = n * n;
    final float n7 = n6 * n;
    return (2.0f * n7 - Float.intBitsToFloat(1077936128) * n6 + 1.0f) * n2
        + (n7 - 2.0f * n6 + n) * n4
        + (Float.intBitsToFloat(-1073741824) * n7 + Float.intBitsToFloat(1077936128) * n6) * n3
        + (n7 - n6) * n5;
  }

  private static float mslulprvbkl(final float n, final float n2, final float n3, final float n4) {
    final float n5 = n - n3;
    final float n6 = n2 - n4;
    return n5 * n5 + n6 * n6;
  }

  private static float mflv5ik364rn(final float b, final float a, final float a2) {
    return Math.max(a, Math.min(a2, b));
  }

  private static boolean mgs8xrgpaj0t(final float n, final float n2) {
    return Math.abs(n - n2) <= Float.intBitsToFloat(925353388);
  }

  static {
    f4jqnc2913qv =
        MotionFiniteService.finite(
            "materialCurveGrab", scenePctService -> scenePctService instanceof CurveGraphControl);
    f9dklpowc9bc =
        new CompositorPushPresentationScaleService.CurveGraphStyle(
            MaterialIsLightService.SURFACE_CONTAINER,
            MaterialIsLightService.OUTLINE_VARIANT,
            407455055,
            MaterialIsLightService.OUTLINE_VARIANT,
            MaterialIsLightService.PRIMARY,
            MaterialIsLightService.PRIMARY,
            407844747,
            MaterialIsLightService.OUTLINE,
            MaterialIsLightService.PRIMARY,
            MaterialIsLightService.ON_PRIMARY_CONTAINER,
            MaterialIsLightService.ON_PRIMARY,
            MaterialIsLightService.ON_SURFACE,
            2.0f,
            2.0f);
    f3pn7wr985gf = List.of("0", ".5", "1");
    f40zatfqczr7 = new float[] {0.0f, Float.intBitsToFloat(1056964608), 1.0f};
    fisaqr9d4xv2 = List.of("-2", "0", "1", "2");
    f94m5mye412l = new float[] {Float.intBitsToFloat(-1073741824), 0.0f, 1.0f, 2.0f};
  }

  public enum Handle {
    NONE,
    FIRST,
    SECOND;
  }

  private static final class Plot {
    private float value8;
    private float value9;
    private float faabivmzwvyv;
    private float value11;
  }

  record Point(float x, float y) {}
}
