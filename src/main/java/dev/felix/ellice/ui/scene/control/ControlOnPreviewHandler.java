



package dev.felix.ellice.ui.scene.control;

import java.math.BigDecimal;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.function.BiConsumer;
import dev.felix.ellice.module.ModuleSetting;

public final class ControlOnPreviewHandler extends RangeSliderControl
{
    public static final float DEFAULT_WIDTH = 112.0f;
    public static final float DEFAULT_HEIGHT = 18.0f;
    private static final int f4cbrov7no4f = 738197503;
    private static final int fb2iuvjx9oqx = -8530948;
    private static final int f56p0g55bgyo = -1;
    private static final int fbh6r88klill = 1073741824;
    private static final int f2f6v4kxi2i6 = -905969665;
    private final ModuleSetting.Range fi2xecokut2u;
    private final AutoCloseable f2suypzb8fzk;
    private BiConsumer<Float, Float> fa5sry0fnzxd;
    private BiConsumer<Float, Float> fj18xhv9ib5a;
    private boolean fejhr7h6hfa2;
    private boolean feukfxelseco;
    private boolean f4mvl3e7a6xu;
    private boolean fair2ejcgsbi;
    private boolean f1ce4yrikht1;
    private boolean f2c5fmlepqel;
    
    public ControlOnPreviewHandler onPreview(final BiConsumer<Float, Float> fj18xhv9ib5a) {
        this.fj18xhv9ib5a = fj18xhv9ib5a;
        return this;
    }
    
    public ControlOnPreviewHandler(final ModuleSetting.Range obj) {
        this.fejhr7h6hfa2 = true;
        this.feukfxelseco = true;
        this.fi2xecokut2u = Objects.requireNonNull(obj, "setting");
        super.range(obj.min(), obj.max());
        super.step(obj.step());
        super.values(obj.low(), obj.high());
        super.trackColor(738197503);
        super.fillColor(-8530948);
        super.thumbColor(-1);
        super.thumbShadowColor(1073741824);
        super.textColor(-905969665);
        super.fontSize(Float.intBitsToFloat(1089470464));
        super.trackHeight(Float.intBitsToFloat(1077936128));
        super.thumbRadius(Float.intBitsToFloat(1078984704));
        super.thumbShadow(Float.intBitsToFloat(1075838976));
        super.trackRadius(Float.intBitsToFloat(1069547520));
        super.format(m6drpw69syke(obj.step()));
        super.size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1099956224));
        super.cursorStyle(CursorStyle.POINTER);
        super.onChange((n, n2) -> {
            this.f1ce4yrikht1 = true;
            if (this.fj18xhv9ib5a != null) {
                this.fj18xhv9ib5a.accept(n, n2);
            }
            return;
        });
        this.refreshState();
        this.f2suypzb8fzk = obj.onStateChanged(this::refreshState);
    }
    
    public ModuleSetting.Range setting() {
        return this.fi2xecokut2u;
    }
    
    @Override
    public ControlOnPreviewHandler low(final float n) {
        if (!Float.isFinite(n)) {
            return this;
        }
        this.trySetRange(Math.min(n, this.fi2xecokut2u.high()), this.fi2xecokut2u.high());
        return this;
    }
    
    @Override
    public ControlOnPreviewHandler high(final float n) {
        if (!Float.isFinite(n)) {
            return this;
        }
        this.trySetRange(this.fi2xecokut2u.low(), Math.max(this.fi2xecokut2u.low(), n));
        return this;
    }
    
    @Override
    public ControlOnPreviewHandler values(final float n, final float n2) {
        this.trySetRange(n, n2);
        return this;
    }
    
    @Override
    public ControlOnPreviewHandler range(final float f1, final float f2) {
        if (Float.compare(f1, this.fi2xecokut2u.min()) != 0 || Float.compare(f2, this.fi2xecokut2u.max()) != 0) {
            throw new UnsupportedOperationException("a bound range slider's domain is owned by its Setting.Range");
        }
        return this;
    }
    
    @Override
    public ControlOnPreviewHandler step(final float f1) {
        if (Float.compare(f1, this.fi2xecokut2u.step()) != 0) {
            throw new UnsupportedOperationException("a bound range slider's step is owned by its Setting.Range");
        }
        return this;
    }
    
    public boolean dependencyVisible() {
        return this.f4mvl3e7a6xu;
    }
    
    public boolean dependencyActive() {
        return this.fair2ejcgsbi;
    }
    
    public boolean available() {
        return this.f4mvl3e7a6xu && this.fair2ejcgsbi && this.fejhr7h6hfa2 && this.feukfxelseco;
    }
    
    @Override
    public ControlOnPreviewHandler visible(final boolean fejhr7h6hfa2) {
        this.fejhr7h6hfa2 = fejhr7h6hfa2;
        this.refreshState();
        return this;
    }
    
    public ControlOnPreviewHandler enabled(final boolean feukfxelseco) {
        this.feukfxelseco = feukfxelseco;
        this.refreshState();
        return this;
    }
    
    @Override
    public ControlOnPreviewHandler onChange(final BiConsumer<Float, Float> fa5sry0fnzxd) {
        this.fa5sry0fnzxd = fa5sry0fnzxd;
        return this;
    }
    
    public boolean trySetRange(final float f, final float f2) {
        this.refreshState();
        if (!this.available() || this.f2c5fmlepqel || !Float.isFinite(f) || !Float.isFinite(f2)) {
            return false;
        }
        final ModuleSetting.RangeValue rangeValue = this.fi2xecokut2u.get();
        this.fi2xecokut2u.set(f, f2);
        final ModuleSetting.RangeValue rangeValue2 = this.fi2xecokut2u.get();
        this.m81aqhhwlo8s(rangeValue2);
        final boolean b = !rangeValue.equals(rangeValue2);
        if (b && this.fa5sry0fnzxd != null) {
            this.fa5sry0fnzxd.accept(rangeValue2.low(), rangeValue2.high());
        }
        return b;
    }
    
    public void refreshState() {
        if (this.f2c5fmlepqel) {
            return;
        }
        this.f4mvl3e7a6xu = this.fi2xecokut2u.isVisible();
        this.fair2ejcgsbi = this.fi2xecokut2u.isActive();
        final boolean available = this.available();
        final boolean visible = this.materialStyle ? (this.fejhr7h6hfa2 && this.f4mvl3e7a6xu) : available;
        if (this.visible != visible) {
            this.visible = visible;
            this.invalidateLayout();
        }
        if (this.interactive != available) {
            this.interactive = available;
            this.invalidate();
        }
        if (this.pointerEvents != available) {
            this.pointerEvents = available;
            this.invalidate();
        }
        final CursorStyle cursorStyle = available ? CursorStyle.POINTER : CursorStyle.DEFAULT;
        if (this.cursorStyle != cursorStyle) {
            this.cursorStyle = cursorStyle;
            this.invalidate();
        }
        this.m81aqhhwlo8s(this.fi2xecokut2u.get());
    }
    
    @Override
    protected void onRelease(final boolean b) {
        super.onRelease(b);
        if (!this.f1ce4yrikht1) {
            return;
        }
        final float low = super.low();
        final float high = super.high();
        this.f1ce4yrikht1 = false;
        if (!this.available() || this.f2c5fmlepqel) {
            this.m81aqhhwlo8s(this.fi2xecokut2u.get());
            return;
        }
        final ModuleSetting.RangeValue rangeValue = this.fi2xecokut2u.get();
        this.fi2xecokut2u.set(low, high);
        final ModuleSetting.RangeValue rangeValue2 = this.fi2xecokut2u.get();
        this.m81aqhhwlo8s(rangeValue2);
        if (!rangeValue.equals(rangeValue2) && this.fa5sry0fnzxd != null) {
            this.fa5sry0fnzxd.accept(rangeValue2.low(), rangeValue2.high());
        }
    }
    
    @Override
    protected boolean handleScroll(final float n) {
        return false;
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1121976320);
    }
    
    @Override
    public float intrinsicHeight(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1099956224);
    }
    
    @Override
    protected void onDetached() {
        this.fj18xhv9ib5a = null;
        this.dispose();
    }
    
    public void dispose() {
        if (this.f2c5fmlepqel) {
            return;
        }
        this.f2c5fmlepqel = true;
        this.f1ce4yrikht1 = false;
        this.interactive = false;
        this.pointerEvents = false;
        try {
            this.f2suypzb8fzk.close();
        }
        catch (final Exception ex) {}
    }
    
    private void m81aqhhwlo8s(final ModuleSetting.RangeValue rangeValue) {
        this.f1ce4yrikht1 = false;
        super.values(rangeValue.low(), rangeValue.high());
    }
    
    private static String m6drpw69syke(final float n) {
        if (n <= 0.0f || !Float.isFinite(n)) {
            return "%.2f";
        }
        return "%." + Math.max(0, Math.min(4, new BigDecimal(Float.toString(n)).stripTrailingZeros().scale()));
    }
}
