



package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.function.Consumer;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class ControlSettingService extends ControlCompactService
{
    public static final float DEFAULT_WIDTH = 36.0f;
    public static final float DEFAULT_HEIGHT = 20.0f;
    public static final MotionFiniteService TOGGLE_PROGRESS;
    private static final int f7cnz9k5c2qa = 1802731408;
    private static final int fbxlnt5a6zqc = -8530948;
    private static final int f925dru5uzp5 = -1;
    private static final SceneEaseHandler f8mtzekmsj6k;
    private static final SceneEaseHandler fq9klyrad2e;
    private final ModuleSetting.Bool f6345l6y6975;
    private final AutoCloseable fdqgwd49p4bh;
    private float fhpxltearaqs;
    private boolean fewlk0wltl7s;
    private boolean fechhr47m69h;
    private boolean f9k2iu1h2oq8;
    private boolean f11kqjzhvkfm;
    private boolean f7f45g2azbno;
    private boolean fdgedh10y4r0;
    private boolean feq3z45z8yay;
    private float fazuf6jr90s4;
    
    public ControlSettingService(final ModuleSetting.Bool obj) {
        this.fhpxltearaqs = Float.intBitsToFloat(1054951342);
        this.fewlk0wltl7s = true;
        this.fechhr47m69h = true;
        this.fazuf6jr90s4 = 1.0f;
        this.f6345l6y6975 = Objects.requireNonNull(obj, "setting");
        this.f7f45g2azbno = Boolean.TRUE.equals(obj.get());
        this.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1101004800));
        this.trackColor(1802731408);
        this.activeColor(-8530948);
        this.knobColor(-1);
        this.cursorStyle = CursorStyle.POINTER;
        super.value(this.f7f45g2azbno);
        this.m9v2hznvzu17(false);
        this.fdgedh10y4r0 = true;
        this.fdqgwd49p4bh = obj.onStateChanged(this::refreshState);
    }
    
    public ModuleSetting.Bool setting() {
        return this.f6345l6y6975;
    }
    
    @Override
    public boolean value() {
        return Boolean.TRUE.equals(this.f6345l6y6975.get());
    }
    
    @Override
    public ControlSettingService value(final boolean b) {
        if (Boolean.TRUE.equals(this.f6345l6y6975.get()) == b) {
            return this;
        }
        this.f6345l6y6975.set(b);
        this.m76s6emi7i8w(b, this.fdgedh10y4r0);
        return this;
    }
    
    public float progress() {
        return ControlSettingService.TOGGLE_PROGRESS.get(this);
    }
    
    public boolean dependencyVisible() {
        return this.f9k2iu1h2oq8;
    }
    
    public boolean dependencyActive() {
        return this.f11kqjzhvkfm;
    }
    
    @Override
    public ControlSettingService visible(final boolean fewlk0wltl7s) {
        this.fewlk0wltl7s = fewlk0wltl7s;
        this.refreshState();
        return this;
    }
    
    public ControlSettingService enabled(final boolean fechhr47m69h) {
        this.fechhr47m69h = fechhr47m69h;
        this.refreshState();
        return this;
    }
    
    public ControlSettingService disabledOpacity(final float n) {
        if (!Float.isFinite(n) || n < 0.0f || n > 1.0f) {
            throw new IllegalArgumentException("disabled opacity must be finite and in [0, 1]");
        }
        this.fhpxltearaqs = n;
        this.m7h1i8qq9ofa(this.fdgedh10y4r0);
        return this;
    }
    
    @Override
    public ControlSettingService activeColor(final int n) {
        super.activeColor(n);
        this.invalidate();
        return this;
    }
    
    @Override
    public ControlSettingService trackColor(final int n) {
        super.trackColor(n);
        this.invalidate();
        return this;
    }
    
    @Override
    public ControlSettingService knobColor(final int n) {
        super.knobColor(n);
        this.invalidate();
        return this;
    }
    
    @Override
    public ControlSettingService onToggle(final Consumer<Boolean> consumer) {
        super.onToggle(consumer);
        return this;
    }
    
    public void refreshState() {
        this.m9v2hznvzu17(this.fdgedh10y4r0);
    }
    
    public boolean tryToggle() {
        this.refreshState();
        if (!this.f9k2iu1h2oq8 || !this.f11kqjzhvkfm || this.feq3z45z8yay) {
            return false;
        }
        final boolean b = !Boolean.TRUE.equals(this.f6345l6y6975.get());
        this.f6345l6y6975.set(b);
        this.m76s6emi7i8w(b, true);
        if (this.onToggle != null) {
            this.onToggle.accept(b);
        }
        return true;
    }
    
    @Override
    public void toggle() {
        this.handleClick();
    }
    
    @Override
    protected void handleClick() {
        if (this.tryToggle() && this.onClick != null) {
            this.onClick.run();
        }
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1108344832);
    }
    
    @Override
    public float intrinsicHeight(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1101004800);
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        if (this.materialStyle) {
            super.draw(compositorPushPresentationScaleService);
            return;
        }
        final float n = this.f11kqjzhvkfm ? (this.pressed ? Float.intBitsToFloat(1064766013) : (this.hovered ? Float.intBitsToFloat(1065504211) : 1.0f)) : 1.0f;
        if (Float.compare(this.fazuf6jr90s4, n) != 0) {
            this.fazuf6jr90s4 = n;
            MotionAnimateService.animate(this, MotionColorsContainer.Floats.SCALE, n, ControlSettingService.fq9klyrad2e);
        }
        super.draw(compositorPushPresentationScaleService);
    }
    
    @Override
    protected void onDetached() {
        this.dispose();
    }
    
    public void dispose() {
        if (this.feq3z45z8yay) {
            return;
        }
        this.feq3z45z8yay = true;
        try {
            this.fdqgwd49p4bh.close();
        }
        catch (final Exception ex) {}
    }
    
    private void m9v2hznvzu17(final boolean b) {
        if (this.feq3z45z8yay) {
            return;
        }
        final boolean b2 = this.fewlk0wltl7s && this.f6345l6y6975.isVisible();
        final boolean f11kqjzhvkfm = this.fechhr47m69h && this.f6345l6y6975.isActive();
        final boolean b3 = this.f9k2iu1h2oq8 != b2;
        final boolean b4 = this.f11kqjzhvkfm != f11kqjzhvkfm;
        this.f9k2iu1h2oq8 = b2;
        this.f11kqjzhvkfm = f11kqjzhvkfm;
        if (this.visible != b2) {
            this.visible = b2;
            this.invalidateLayout();
        }
        final boolean interactive = b2 && f11kqjzhvkfm;
        if (this.interactive != interactive) {
            this.interactive = interactive;
            this.invalidate();
        }
        final CursorStyle cursorStyle = interactive ? CursorStyle.POINTER : CursorStyle.DEFAULT;
        if (this.cursorStyle != cursorStyle) {
            this.cursorStyle = cursorStyle;
            this.invalidate();
        }
        if (b4 || b3 || !this.fdgedh10y4r0) {
            this.m7h1i8qq9ofa(b);
        }
        this.m76s6emi7i8w(Boolean.TRUE.equals(this.f6345l6y6975.get()), b);
    }
    
    private void m76s6emi7i8w(final boolean f7f45g2azbno, final boolean b) {
        if (this.fdgedh10y4r0 && this.f7f45g2azbno == f7f45g2azbno) {
            return;
        }
        final float progress = this.progress();
        final float n = (this.f7f45g2azbno = f7f45g2azbno) ? 1.0f : 0.0f;
        super.value(f7f45g2azbno);
        if (b && Math.abs(progress - n) > Float.intBitsToFloat(953267991)) {
            ControlSettingService.TOGGLE_PROGRESS.set(this, progress);
            MotionAnimateService.animate(this, ControlSettingService.TOGGLE_PROGRESS, n, this.materialStyle ? MaterialIsLightService.FAST_SPATIAL : ControlSettingService.f8mtzekmsj6k);
        }
        else {
            MotionAnimateService.cancel(this, ControlSettingService.TOGGLE_PROGRESS);
            ControlSettingService.TOGGLE_PROGRESS.set(this, n);
        }
    }
    
    private void m7h1i8qq9ofa(final boolean b) {
        final float n = this.f11kqjzhvkfm ? 1.0f : this.fhpxltearaqs;
        if (b) {
            MotionAnimateService.animate(this, MotionColorsContainer.Floats.OPACITY, n, ControlSettingService.f8mtzekmsj6k);
        }
        else {
            MotionAnimateService.cancel(this, MotionColorsContainer.Floats.OPACITY);
            MotionColorsContainer.Floats.OPACITY.set(this, n);
        }
    }
    
    static {
        TOGGLE_PROGRESS = MotionFiniteService.of("knobPosition", scenePctService -> scenePctService instanceof ControlSettingService, f -> Float.isFinite(f) && f >= 0.0f && f <= 1.0f, "a finite value in [0, 1]");
        f8mtzekmsj6k = new SceneEaseHandler.Tween(Float.intBitsToFloat(1045220557), SceneEaseHandler.Easing.EASE_IN_OUT);
        fq9klyrad2e = new SceneEaseHandler.Spring(Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1139146752));
    }
}
