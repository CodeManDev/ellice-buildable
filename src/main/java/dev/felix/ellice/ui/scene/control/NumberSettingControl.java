



package dev.felix.ellice.ui.scene.control;

import java.math.BigDecimal;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.function.Consumer;
import dev.felix.ellice.module.ModuleSetting;

public final class NumberSettingControl extends SliderControl
{
    public static final float DEFAULT_WIDTH = 112.0f;
    public static final float DEFAULT_HEIGHT = 18.0f;
    private static final int fpmkv4m4kag = 738197503;
    private static final int f3nmaaotbibv = -8530948;
    private static final int f6ujyd07s0y8 = -1;
    private static final int f2n1xl9wzehh = 1073741824;
    private static final int fxzgh38ier2 = -905969665;
    private final ModuleSetting.Number ferpamy2spy8;
    private final AutoCloseable fdd1dob7geje;
    private Consumer<Float> f62j8djy5ok3;
    private Consumer<Float> fgtjsrvy772f;
    private boolean ff01q8flhxol;
    private boolean f8po858kxf6k;
    private boolean fjl8oqrlp12f;
    private boolean fdi39tv0gche;
    private boolean fx9dabrmkuz;
    private boolean fgpz4nff4d7h;
    
    public NumberSettingControl(final ModuleSetting.Number obj) {
        this.ff01q8flhxol = true;
        this.f8po858kxf6k = true;
        this.ferpamy2spy8 = Objects.requireNonNull(obj, "setting");
        super.range(obj.min(), obj.max());
        super.step(obj.step());
        super.value(obj.get());
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
        super.format(mc00wjbdqihl(obj.step()));
        super.size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1099956224));
        super.cursorStyle(CursorStyle.POINTER);
        super.onChange(n -> {
            this.fx9dabrmkuz = true;
            if (this.fgtjsrvy772f != null) {
                this.fgtjsrvy772f.accept(n);
            }
            return;
        });
        this.refreshState();
        this.fdd1dob7geje = obj.onStateChanged(this::refreshState);
    }
    
    public ModuleSetting.Number setting() {
        return this.ferpamy2spy8;
    }
    
    public NumberSettingControl onPreview(final Consumer<Float> fgtjsrvy772f) {
        this.fgtjsrvy772f = fgtjsrvy772f;
        return this;
    }
    
    @Override
    public NumberSettingControl value(final float n) {
        this.trySetValue(n);
        return this;
    }
    
    @Override
    public NumberSettingControl range(final float f1, final float f2) {
        if (Float.compare(f1, this.ferpamy2spy8.min()) != 0 || Float.compare(f2, this.ferpamy2spy8.max()) != 0) {
            throw new UnsupportedOperationException("a bound slider's range is owned by its Setting.Number");
        }
        return this;
    }
    
    @Override
    public NumberSettingControl step(final float f1) {
        if (Float.compare(f1, this.ferpamy2spy8.step()) != 0) {
            throw new UnsupportedOperationException("a bound slider's step is owned by its Setting.Number");
        }
        return this;
    }
    
    public boolean dependencyVisible() {
        return this.fjl8oqrlp12f;
    }
    
    public boolean dependencyActive() {
        return this.fdi39tv0gche;
    }
    
    public boolean available() {
        return this.fjl8oqrlp12f && this.fdi39tv0gche && this.ff01q8flhxol && this.f8po858kxf6k;
    }
    
    @Override
    public NumberSettingControl visible(final boolean ff01q8flhxol) {
        this.ff01q8flhxol = ff01q8flhxol;
        this.refreshState();
        return this;
    }
    
    public NumberSettingControl enabled(final boolean f8po858kxf6k) {
        this.f8po858kxf6k = f8po858kxf6k;
        this.refreshState();
        return this;
    }
    
    @Override
    public NumberSettingControl onChange(final Consumer<Float> f62j8djy5ok3) {
        this.f62j8djy5ok3 = f62j8djy5ok3;
        return this;
    }
    
    public boolean trySetValue(final float n) {
        this.refreshState();
        if (!this.available() || this.fgpz4nff4d7h || !Float.isFinite(n)) {
            return false;
        }
        final float floatValue = this.ferpamy2spy8.get();
        this.ferpamy2spy8.set(n);
        final float floatValue2 = this.ferpamy2spy8.get();
        this.mabkvye09yfn(floatValue2);
        final boolean b = Float.compare(floatValue, floatValue2) != 0;
        if (b && this.f62j8djy5ok3 != null) {
            this.f62j8djy5ok3.accept(floatValue2);
        }
        return b;
    }
    
    public void refreshState() {
        if (this.fgpz4nff4d7h) {
            return;
        }
        this.fjl8oqrlp12f = this.ferpamy2spy8.isVisible();
        this.fdi39tv0gche = this.ferpamy2spy8.isActive();
        final boolean available = this.available();
        final boolean visible = this.materialStyle ? (this.ff01q8flhxol && this.fjl8oqrlp12f) : available;
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
        this.mabkvye09yfn(this.ferpamy2spy8.get());
    }
    
    @Override
    protected void onRelease(final boolean b) {
        if (!this.fx9dabrmkuz) {
            return;
        }
        final float value = super.value();
        this.fx9dabrmkuz = false;
        if (!this.available() || this.fgpz4nff4d7h) {
            this.mabkvye09yfn(this.ferpamy2spy8.get());
            return;
        }
        final float floatValue = this.ferpamy2spy8.get();
        this.ferpamy2spy8.set(value);
        final float floatValue2 = this.ferpamy2spy8.get();
        this.mabkvye09yfn(floatValue2);
        if (Float.compare(floatValue, floatValue2) != 0 && this.f62j8djy5ok3 != null) {
            this.f62j8djy5ok3.accept(floatValue2);
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
        this.fgtjsrvy772f = null;
        this.dispose();
    }
    
    public void dispose() {
        if (this.fgpz4nff4d7h) {
            return;
        }
        this.fgpz4nff4d7h = true;
        this.fx9dabrmkuz = false;
        this.interactive = false;
        this.pointerEvents = false;
        try {
            this.fdd1dob7geje.close();
        }
        catch (final Exception ex) {}
    }
    
    private void mabkvye09yfn(final float n) {
        this.fx9dabrmkuz = false;
        super.value(n);
    }
    
    private static String mc00wjbdqihl(final float n) {
        if (n <= 0.0f || !Float.isFinite(n)) {
            return "%.2f";
        }
        return "%." + Math.max(0, Math.min(4, new BigDecimal(Float.toString(n)).stripTrailingZeros().scale()));
    }
}
