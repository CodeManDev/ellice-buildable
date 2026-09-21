



package dev.felix.ellice.ui.scene.control;

import java.util.Locale;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import java.util.Objects;
import java.util.function.Consumer;
import dev.felix.ellice.module.ModuleSetting;
import java.util.List;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class CurveGraphControl extends ScenePctService<CurveGraphControl>
{
    private boolean f7u48yeoj81v;
    private float f17rha3azcml;
    private boolean fgm0z0e5657j;
    private Handle f79wn5u9j2r8;
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
    private static final int f92xsv4k7xuk = -536870913;
    private static final List<String> f3pn7wr985gf;
    private static final float[] f40zatfqczr7;
    private static final List<String> fisaqr9d4xv2;
    private static final float[] f94m5mye412l;
    private ModuleSetting.CurveValue f8hnwhofdz4f;
    private String f5slqryxd14v;
    private String f6d5gxngj6wr;
    private String fboscpcjym5x;
    private String fabkdnsbdaci;
    private ModuleSetting.CurveValue f7fm8cwj1azl;
    private Consumer<ModuleSetting.CurveValue> fa5dfuvyk1zx;
    private Consumer<ModuleSetting.CurveValue> f85hu0ru7tyc;
    private Runnable fgpxxrqyqg5g;
    private Handle f3e4pak9lfv6;
    private final Plot f8nharusau28;
    private boolean f7ep6mp40l13;
    private boolean f1exvkkb2vew;
    private boolean f2c9pd4notww;
    
    @Override
    protected boolean handlesContinuousPointer() {
        return true;
    }
    
    @Override
    public float getAnimProperty(final String anObject) {
        return "materialCurveGrab".equals(anObject) ? this.f17rha3azcml : super.getAnimProperty(anObject);
    }
    
    @Override
    public void setAnimProperty(final String anObject, final float f17rha3azcml) {
        if ("materialCurveGrab".equals(anObject)) {
            this.f17rha3azcml = f17rha3azcml;
        }
        else {
            super.setAnimProperty(anObject, f17rha3azcml);
        }
    }
    
    public CurveGraphControl material(final boolean f7u48yeoj81v) {
        this.f7u48yeoj81v = f7u48yeoj81v;
        return this;
    }
    
    public CurveGraphControl() {
        this(ModuleSetting.CurveValue.linear());
    }
    
    public CurveGraphControl(final ModuleSetting.CurveValue obj) {
        this.f79wn5u9j2r8 = Handle.NONE;
        this.f3e4pak9lfv6 = Handle.NONE;
        this.f8nharusau28 = new Plot();
        this.f1exvkkb2vew = true;
        this.f8hnwhofdz4f = Objects.requireNonNull(obj, "value");
        this.mcqdhlvlpdpn();
        this.interactive = true;
        this.cursorStyle(CursorStyle.POINTER);
        this.size(Float.intBitsToFloat(1130102784), Float.intBitsToFloat(1124728832));
        this.flexShrink(0.0f);
    }
    
    public ModuleSetting.CurveValue value() {
        return this.f8hnwhofdz4f;
    }
    
    public CurveGraphControl value(final ModuleSetting.CurveValue obj) {
        final ModuleSetting.CurveValue curveValue = Objects.requireNonNull(obj, "value");
        if (Objects.equals(this.f8hnwhofdz4f, curveValue)) {
            return this;
        }
        this.f8hnwhofdz4f = curveValue;
        this.mcqdhlvlpdpn();
        this.invalidate();
        this.f7fm8cwj1azl = null;
        this.f7ep6mp40l13 = false;
        this.f3e4pak9lfv6 = Handle.NONE;
        return this;
    }
    
    public CurveGraphControl onChange(final Consumer<ModuleSetting.CurveValue> fa5dfuvyk1zx) {
        this.fa5dfuvyk1zx = fa5dfuvyk1zx;
        return this;
    }
    
    public CurveGraphControl onCommit(final Consumer<ModuleSetting.CurveValue> f85hu0ru7tyc) {
        this.f85hu0ru7tyc = f85hu0ru7tyc;
        return this;
    }
    
    public CurveGraphControl onInteraction(final Runnable fgpxxrqyqg5g) {
        this.fgpxxrqyqg5g = fgpxxrqyqg5g;
        return this;
    }
    
    public CurveGraphControl enabled(final boolean f1exvkkb2vew) {
        if (this.f1exvkkb2vew == f1exvkkb2vew) {
            return this;
        }
        this.f1exvkkb2vew = f1exvkkb2vew;
        this.interactive = (f1exvkkb2vew && !this.f2c9pd4notww);
        this.pointerEvents = (f1exvkkb2vew && !this.f2c9pd4notww);
        this.cursorStyle((f1exvkkb2vew && !this.f2c9pd4notww) ? CursorStyle.POINTER : CursorStyle.DEFAULT);
        if (!f1exvkkb2vew) {
            this.cancelGesture();
        }
        this.invalidate();
        return this;
    }
    
    public boolean enabled() {
        return this.f1exvkkb2vew && !this.f2c9pd4notww;
    }
    
    public boolean isDragging() {
        return this.f3e4pak9lfv6 != Handle.NONE;
    }
    
    public Handle activeHandle() {
        return this.f3e4pak9lfv6;
    }
    
    public String primaryValueLabel() {
        return this.f5slqryxd14v;
    }
    
    public String secondaryValueLabel() {
        return this.f6d5gxngj6wr;
    }
    
    public List<String> xAxisLabels() {
        return CurveGraphControl.f3pn7wr985gf;
    }
    
    public List<String> yAxisLabels() {
        return CurveGraphControl.fisaqr9d4xv2;
    }
    
    public CurveGraphControl cancelGesture() {
        final boolean b = this.f3e4pak9lfv6 != Handle.NONE || this.f7fm8cwj1azl != null || this.f7ep6mp40l13;
        this.f3e4pak9lfv6 = Handle.NONE;
        this.f7fm8cwj1azl = null;
        this.f7ep6mp40l13 = false;
        if (b) {
            this.invalidate();
        }
        return this;
    }
    
    public boolean previewHandle(final Handle handle, final float f, final float f2) {
        if (!this.enabled() || this.f8hnwhofdz4f.type() != ModuleSetting.CurveType.CUBIC_BEZIER || handle == null || handle == Handle.NONE || !Float.isFinite(f) || !Float.isFinite(f2)) {
            return false;
        }
        float mflv5ik364rn = mflv5ik364rn(f, 0.0f, 1.0f);
        float mflv5ik364rn2 = mflv5ik364rn(f2, Float.intBitsToFloat(-1073741824), 2.0f);
        final float n = (handle == Handle.FIRST) ? this.f8hnwhofdz4f.x1() : this.f8hnwhofdz4f.x2();
        final float n2 = (handle == Handle.FIRST) ? this.f8hnwhofdz4f.y1() : this.f8hnwhofdz4f.y2();
        if (mgs8xrgpaj0t(mflv5ik364rn, n)) {
            mflv5ik364rn = n;
        }
        if (mgs8xrgpaj0t(mflv5ik364rn2, n2)) {
            mflv5ik364rn2 = n2;
        }
        if (this.f7fm8cwj1azl != null) {
            final float n3 = (handle == Handle.FIRST) ? this.f7fm8cwj1azl.x1() : this.f7fm8cwj1azl.x2();
            final float n4 = (handle == Handle.FIRST) ? this.f7fm8cwj1azl.y1() : this.f7fm8cwj1azl.y2();
            if (mgs8xrgpaj0t(mflv5ik364rn, n3)) {
                mflv5ik364rn = n3;
            }
            if (mgs8xrgpaj0t(mflv5ik364rn2, n4)) {
                mflv5ik364rn2 = n4;
            }
        }
        final ModuleSetting.CurveValue b = (handle == Handle.FIRST) ? ModuleSetting.CurveValue.cubicBezier(mflv5ik364rn, mflv5ik364rn2, this.f8hnwhofdz4f.x2(), this.f8hnwhofdz4f.y2()) : ModuleSetting.CurveValue.cubicBezier(this.f8hnwhofdz4f.x1(), this.f8hnwhofdz4f.y1(), mflv5ik364rn, mflv5ik364rn2);
        if (Objects.equals(b, this.f8hnwhofdz4f)) {
            return false;
        }
        this.f8hnwhofdz4f = b;
        this.mcqdhlvlpdpn();
        this.f7ep6mp40l13 = (this.f7fm8cwj1azl == null || !Objects.equals(this.f7fm8cwj1azl, b));
        this.invalidate();
        if (this.fa5dfuvyk1zx != null) {
            this.fa5dfuvyk1zx.accept(b);
        }
        return true;
    }
    
    public Point handleCenter(final Handle handle) {
        if (handle == null || handle == Handle.NONE || this.f8hnwhofdz4f.type() != ModuleSetting.CurveType.CUBIC_BEZIER) {
            return null;
        }
        final Plot mhx0zr2tf9a9 = this.mhx0zr2tf9a9(this.cx, this.cy, this.cw, this.ch, 1.0f);
        return new Point(mj6mm1rkpoae(mhx0zr2tf9a9, (handle == Handle.FIRST) ? this.f8hnwhofdz4f.x1() : this.f8hnwhofdz4f.x2()), mags5k7oczhq(mhx0zr2tf9a9, (handle == Handle.FIRST) ? this.f8hnwhofdz4f.y1() : this.f8hnwhofdz4f.y2()));
    }
    
    @Override
    protected void onPress(final float n, final float n2) {
        if (!this.enabled()) {
            return;
        }
        if (this.fgpxxrqyqg5g != null) {
            this.fgpxxrqyqg5g.run();
        }
        if (this.f8hnwhofdz4f.type() != ModuleSetting.CurveType.CUBIC_BEZIER || this.cw <= 0.0f || this.ch <= 0.0f) {
            this.f3e4pak9lfv6 = Handle.NONE;
            return;
        }
        final Point handleCenter = this.handleCenter(Handle.FIRST);
        final Point handleCenter2 = this.handleCenter(Handle.SECOND);
        final float mslulprvbkl = mslulprvbkl(n, n2, handleCenter.x(), handleCenter.y());
        final float mslulprvbkl2 = mslulprvbkl(n, n2, handleCenter2.x(), handleCenter2.y());
        if (Math.min(mslulprvbkl, mslulprvbkl2) > Float.intBitsToFloat(1125122048)) {
            this.f3e4pak9lfv6 = Handle.NONE;
            return;
        }
        this.f3e4pak9lfv6 = ((mslulprvbkl <= mslulprvbkl2) ? Handle.FIRST : Handle.SECOND);
        this.f7fm8cwj1azl = this.f8hnwhofdz4f;
        this.f7ep6mp40l13 = false;
    }
    
    @Override
    protected void updateWhilePressed(final float n, final float n2) {
        if (!this.enabled() || this.f3e4pak9lfv6 == Handle.NONE || this.cw <= 0.0f || this.ch <= 0.0f) {
            return;
        }
        final Plot mhx0zr2tf9a9 = this.mhx0zr2tf9a9(this.cx, this.cy, this.cw, this.ch, 1.0f);
        if (mhx0zr2tf9a9.faabivmzwvyv <= 0.0f || mhx0zr2tf9a9.fe1at5aux48c <= 0.0f) {
            return;
        }
        this.previewHandle(this.f3e4pak9lfv6, m8b7qwepv0xx(mhx0zr2tf9a9, n), mgbww7vox9gb(mhx0zr2tf9a9, n2));
    }
    
    @Override
    protected void onRelease(final boolean b) {
        final ModuleSetting.CurveValue f8hnwhofdz4f = this.f8hnwhofdz4f;
        final boolean b2 = this.f3e4pak9lfv6 != Handle.NONE && this.f7ep6mp40l13;
        this.f3e4pak9lfv6 = Handle.NONE;
        this.f7fm8cwj1azl = null;
        this.f7ep6mp40l13 = false;
        if (b2 && this.enabled() && this.f85hu0ru7tyc != null) {
            this.f85hu0ru7tyc.accept(f8hnwhofdz4f);
        }
    }
    
    @Override
    protected boolean handleScroll(final float n) {
        return false;
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1130102784);
    }
    
    @Override
    public float intrinsicHeight(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1124728832);
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        if (this.effectiveOpacity < Float.intBitsToFloat(994352038) || this.cw <= 0.0f || this.ch <= 0.0f) {
            return;
        }
        final Plot mhx0zr2tf9a9 = this.mhx0zr2tf9a9(this.cx, this.cy, this.cw, this.ch, this.presentationScale);
        compositorPushPresentationScaleService.curveGraph(this.cx, this.cy, this.cw, this.ch, mhx0zr2tf9a9.fevhhyt6y15d, mhx0zr2tf9a9.f5c64hupxh0r, mhx0zr2tf9a9.faabivmzwvyv, mhx0zr2tf9a9.fe1at5aux48c, this.f7u48yeoj81v ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1086324736), this.f8hnwhofdz4f, this.m2hdhj9erqga(), this.effectiveOpacity, this.effectiveEdgeSoftness, this.f7u48yeoj81v ? CurveGraphControl.f9dklpowc9bc : CompositorPushPresentationScaleService.DEFAULT_CURVE_GRAPH_STYLE);
        this.mf29zrsd0eru(compositorPushPresentationScaleService, mhx0zr2tf9a9, this.effectiveOpacity);
        if (this.f7u48yeoj81v) {
            final boolean fgm0z0e5657j = this.f3e4pak9lfv6 != Handle.NONE;
            if (fgm0z0e5657j) {
                this.f79wn5u9j2r8 = this.f3e4pak9lfv6;
            }
            if (fgm0z0e5657j != this.fgm0z0e5657j) {
                this.fgm0z0e5657j = fgm0z0e5657j;
                MotionAnimateService.animate(this, CurveGraphControl.f4jqnc2913qv, fgm0z0e5657j ? 1.0f : 0.0f, MaterialIsLightService.FAST_SPATIAL);
            }
            final Point handleCenter = this.handleCenter(this.f79wn5u9j2r8);
            final float max = Math.max(0.0f, Math.min(Float.intBitsToFloat(1065856532), this.f17rha3azcml));
            if (handleCenter != null && max > Float.intBitsToFloat(981668463)) {
                final float n = Float.intBitsToFloat(1084227584) + Float.intBitsToFloat(1084227584) * max;
                compositorPushPresentationScaleService.overlayRect(handleCenter.x - n, handleCenter.y - n, 2.0f * n, 2.0f * n, n, n, n, n, ScenePctService.mulAlpha(MaterialIsLightService.PRIMARY, Float.intBitsToFloat(1039516303) * max * this.effectiveOpacity), 0.0f, 0.0f, 0, Float.intBitsToFloat(1069547520) * max, ScenePctService.mulAlpha(MaterialIsLightService.PRIMARY, this.effectiveOpacity));
            }
        }
    }
    
    @Override
    protected void onDetached() {
        this.dispose();
    }
    
    public void dispose() {
        if (this.f2c9pd4notww) {
            return;
        }
        this.f2c9pd4notww = true;
        this.f1exvkkb2vew = false;
        this.interactive = false;
        this.pointerEvents = false;
        this.cancelGesture();
        this.fa5dfuvyk1zx = null;
        this.f85hu0ru7tyc = null;
        this.fgpxxrqyqg5g = null;
    }
    
    private void mf29zrsd0eru(final CompositorPushPresentationScaleService compositorPushPresentationScaleService, final Plot plot, final float n) {
        final float n2 = this.f7u48yeoj81v ? Float.intBitsToFloat(1094713344) : Float.intBitsToFloat(1087897600);
        final TextData textData = this.f7u48yeoj81v ? MaterialIsLightService.BODY : TextData.NONE;
        final float textLineHeight = compositorPushPresentationScaleService.textLineHeight(n2);
        final int mulAlpha = ScenePctService.mulAlpha(-1342177281, n);
        final int mulAlpha2 = ScenePctService.mulAlpha(-536870913, n);
        final float n3 = this.cx + Float.intBitsToFloat(1077936128) * this.presentationScale;
        for (int i = 0; i < CurveGraphControl.fisaqr9d4xv2.size(); ++i) {
            compositorPushPresentationScaleService.text(n3, mflv5ik364rn(mags5k7oczhq(plot, CurveGraphControl.f94m5mye412l[i]) - textLineHeight * Float.intBitsToFloat(1056964608), this.cy + 2.0f * this.presentationScale, this.cy + this.ch - textLineHeight - 2.0f * this.presentationScale), CurveGraphControl.fisaqr9d4xv2.get(i), n2, mulAlpha, textData);
        }
        final float min = Math.min(this.cy + this.ch - textLineHeight - 2.0f * this.presentationScale, plot.f5c64hupxh0r + plot.fe1at5aux48c + Float.intBitsToFloat(1077936128) * this.presentationScale);
        for (int j = 0; j < CurveGraphControl.f3pn7wr985gf.size(); ++j) {
            final String s = CurveGraphControl.f3pn7wr985gf.get(j);
            final float textWidth = compositorPushPresentationScaleService.textWidth(s, n2, TextMode.REGULAR, this.f7u48yeoj81v ? "material-roboto" : null);
            compositorPushPresentationScaleService.text(mflv5ik364rn(mj6mm1rkpoae(plot, CurveGraphControl.f40zatfqczr7[j]) - textWidth * Float.intBitsToFloat(1056964608), this.cx + 2.0f * this.presentationScale, this.cx + this.cw - textWidth - 2.0f * this.presentationScale), min, s, n2, mulAlpha, textData);
        }
        String s2 = this.f5slqryxd14v;
        String s3 = this.f6d5gxngj6wr;
        if (this.f8hnwhofdz4f.type() == ModuleSetting.CurveType.CUBIC_BEZIER && plot.faabivmzwvyv < Float.intBitsToFloat(1123680256) * this.presentationScale) {
            s2 = this.fboscpcjym5x;
            s3 = this.fabkdnsbdaci;
        }
        final float n4 = this.cy + Float.intBitsToFloat(1084227584) * this.presentationScale;
        compositorPushPresentationScaleService.text(plot.fevhhyt6y15d, n4, s2, n2, mulAlpha2, textData);
        if (!s3.isEmpty()) {
            compositorPushPresentationScaleService.text(Math.max(plot.fevhhyt6y15d, plot.fevhhyt6y15d + plot.faabivmzwvyv - compositorPushPresentationScaleService.textWidth(s3, n2, TextMode.REGULAR, this.f7u48yeoj81v ? "material-roboto" : null)), n4, s3, n2, mulAlpha2, textData);
        }
    }
    
    private void mcqdhlvlpdpn() {
        switch (this.f8hnwhofdz4f.type()) {
            case LINEAR: {
                this.f5slqryxd14v = "y = x";
                this.f6d5gxngj6wr = "";
                this.fboscpcjym5x = this.f5slqryxd14v;
                this.fabkdnsbdaci = this.f6d5gxngj6wr;
                break;
            }
            case CUBIC_BEZIER: {
                this.f5slqryxd14v = String.format(Locale.ROOT, "P1 %.2f, %.2f", this.f8hnwhofdz4f.x1(), this.f8hnwhofdz4f.y1());
                this.f6d5gxngj6wr = String.format(Locale.ROOT, "P2 %.2f, %.2f", this.f8hnwhofdz4f.x2(), this.f8hnwhofdz4f.y2());
                this.fboscpcjym5x = String.format(Locale.ROOT, "P1 %.2f/%.2f", this.f8hnwhofdz4f.x1(), this.f8hnwhofdz4f.y1());
                this.fabkdnsbdaci = String.format(Locale.ROOT, "P2 %.2f/%.2f", this.f8hnwhofdz4f.x2(), this.f8hnwhofdz4f.y2());
                break;
            }
            case STEPS: {
                this.f5slqryxd14v = this.f8hnwhofdz4f.steps() + ((this.f8hnwhofdz4f.steps() == 1) ? " step" : " steps");
                this.f6d5gxngj6wr = switch (this.f8hnwhofdz4f.stepMode()) {
                    default -> throw new MatchException(null, null);
                    case JUMP_START -> "jump start";
                    case JUMP_END -> "jump end";
                    case JUMP_NONE -> "jump none";
                    case JUMP_BOTH -> "jump both";
                };
                this.fboscpcjym5x = this.f5slqryxd14v;
                this.fabkdnsbdaci = this.f6d5gxngj6wr;
                break;
            }
        }
    }
    
    private int m2hdhj9erqga() {
        return switch (this.f3e4pak9lfv6.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
        };
    }
    
    private Plot mhx0zr2tf9a9(final float n, final float n2, final float n3, final float n4, final float n5) {
        final float n6 = Float.isFinite(n5) ? Math.max(0.0f, n5) : 1.0f;
        float min = Math.min((this.f7u48yeoj81v ? Float.intBitsToFloat(1107296256) : Float.intBitsToFloat(1103101952)) * n6, n3 * Float.intBitsToFloat(1049582633));
        float min2 = Math.min((this.f7u48yeoj81v ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1092616192)) * n6, n3 * Float.intBitsToFloat(1039516303));
        float min3 = Math.min((this.f7u48yeoj81v ? Float.intBitsToFloat(1107296256) : Float.intBitsToFloat(1101529088)) * n6, n4 * Float.intBitsToFloat(1050924810));
        float min4 = Math.min((this.f7u48yeoj81v ? Float.intBitsToFloat(1105199104) : Float.intBitsToFloat(1099956224)) * n6, n4 * Float.intBitsToFloat(1049582633));
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
        this.f8nharusau28.fevhhyt6y15d = n + min;
        this.f8nharusau28.f5c64hupxh0r = n2 + min3;
        this.f8nharusau28.faabivmzwvyv = Math.max(0.0f, n3 - min - min2);
        this.f8nharusau28.fe1at5aux48c = Math.max(0.0f, n4 - min3 - min4);
        return this.f8nharusau28;
    }
    
    private static float mj6mm1rkpoae(final Plot plot, final float n) {
        return plot.fevhhyt6y15d + mflv5ik364rn(n, 0.0f, 1.0f) * plot.faabivmzwvyv;
    }
    
    private static float m8b7qwepv0xx(final Plot plot, final float n) {
        if (plot.faabivmzwvyv <= 0.0f) {
            return 0.0f;
        }
        return mflv5ik364rn((n - plot.fevhhyt6y15d) / plot.faabivmzwvyv, 0.0f, 1.0f);
    }
    
    private static float mags5k7oczhq(final Plot plot, final float n) {
        return plot.f5c64hupxh0r + (1.0f - mflv5ik364rn(mhm7m39fuh16(mflv5ik364rn(n, Float.intBitsToFloat(-1073741824), 2.0f)), 0.0f, 1.0f)) * plot.fe1at5aux48c;
    }
    
    private static float mgbww7vox9gb(final Plot plot, final float n) {
        if (plot.fe1at5aux48c <= 0.0f) {
            return 0.0f;
        }
        final float mflv5ik364rn = mflv5ik364rn(1.0f - (n - plot.f5c64hupxh0r) / plot.fe1at5aux48c, 0.0f, 1.0f);
        float intBitsToFloat = Float.intBitsToFloat(-1073741824);
        float n2 = 2.0f;
        for (int i = 0; i < 22; ++i) {
            final float n3 = (intBitsToFloat + n2) * Float.intBitsToFloat(1056964608);
            if (mhm7m39fuh16(n3) < mflv5ik364rn) {
                intBitsToFloat = n3;
            }
            else {
                n2 = n3;
            }
        }
        return (intBitsToFloat + n2) * Float.intBitsToFloat(1056964608);
    }
    
    private static float mhm7m39fuh16(final float n) {
        if (n <= 0.0f) {
            return mel6nu9bu18j((n + 2.0f) * Float.intBitsToFloat(1056964608), 0.0f, Float.intBitsToFloat(1048576000), 0.0f, Float.intBitsToFloat(1054567863));
        }
        if (n <= 1.0f) {
            return mel6nu9bu18j(n, Float.intBitsToFloat(1048576000), Float.intBitsToFloat(1061158912), Float.intBitsToFloat(1046179255), Float.intBitsToFloat(1051372203));
        }
        return mel6nu9bu18j(n - 1.0f, Float.intBitsToFloat(1061158912), 1.0f, Float.intBitsToFloat(1051372203), Float.intBitsToFloat(1040187392));
    }
    
    private static float mel6nu9bu18j(final float n, final float n2, final float n3, final float n4, final float n5) {
        final float n6 = n * n;
        final float n7 = n6 * n;
        return (2.0f * n7 - Float.intBitsToFloat(1077936128) * n6 + 1.0f) * n2 + (n7 - 2.0f * n6 + n) * n4 + (Float.intBitsToFloat(-1073741824) * n7 + Float.intBitsToFloat(1077936128) * n6) * n3 + (n7 - n6) * n5;
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
        f4jqnc2913qv = MotionFiniteService.finite("materialCurveGrab", scenePctService -> scenePctService instanceof CurveGraphControl);
        f9dklpowc9bc = new CompositorPushPresentationScaleService.CurveGraphStyle(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.OUTLINE_VARIANT, 407455055, MaterialIsLightService.OUTLINE_VARIANT, MaterialIsLightService.PRIMARY, MaterialIsLightService.PRIMARY, 407844747, MaterialIsLightService.OUTLINE, MaterialIsLightService.PRIMARY, MaterialIsLightService.ON_PRIMARY_CONTAINER, MaterialIsLightService.ON_PRIMARY, MaterialIsLightService.ON_SURFACE, 2.0f, 2.0f);
        f3pn7wr985gf = List.of("0", ".5", "1");
        f40zatfqczr7 = new float[] { 0.0f, Float.intBitsToFloat(1056964608), 1.0f };
        fisaqr9d4xv2 = List.of("-2", "0", "1", "2");
        f94m5mye412l = new float[] { Float.intBitsToFloat(-1073741824), 0.0f, 1.0f, 2.0f };
    }
    
    public enum Handle
    {
        NONE, 
        FIRST, 
        SECOND;
    }
    
    private static final class Plot
    {
        private float fevhhyt6y15d;
        private float f5c64hupxh0r;
        private float faabivmzwvyv;
        private float fe1at5aux48c;
    }
    
    record Point(float x, float y) {}
}
