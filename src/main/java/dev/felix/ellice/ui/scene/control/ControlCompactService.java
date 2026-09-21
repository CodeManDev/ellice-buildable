



package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.ui.material.MaterialCompactToggleService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import java.util.function.Consumer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.ScenePctService;

public class ControlCompactService extends ScenePctService<ControlCompactService>
{
    protected boolean materialStyle;
    private boolean f6bn6vdtdlrt;
    private boolean f8cq29hyd6xg;
    private float fe1guwcmhnsj;
    private boolean fb2791njv96k;
    private static final MotionFiniteService f588u4dkp372;
    private boolean faxakfn32t9h;
    private float f5kva17wewm2;
    int trackColor;
    int activeColor;
    int knobColor;
    public Consumer<Boolean> onToggle;
    
    public ControlCompactService compact(final boolean f6bn6vdtdlrt) {
        this.f6bn6vdtdlrt = f6bn6vdtdlrt;
        return this;
    }
    
    public ControlCompactService material(final boolean materialStyle) {
        this.materialStyle = materialStyle;
        this.invalidate();
        return this;
    }
    
    public void materialFocus(final boolean f8cq29hyd6xg) {
        this.f8cq29hyd6xg = f8cq29hyd6xg;
        this.invalidate();
    }
    
    private void m7tyob70nebn() {
        if (this.pressed != this.fb2791njv96k) {
            this.fb2791njv96k = this.pressed;
            MotionAnimateService.animate(this, ControlCompactService.f588u4dkp372, this.pressed ? 1.0f : 0.0f, this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
        }
    }
    
    public ControlCompactService() {
        this.trackColor = Integer.MIN_VALUE;
        this.activeColor = Integer.MIN_VALUE;
        this.knobColor = Integer.MIN_VALUE;
        this.interactive = true;
    }
    
    public ControlCompactService value(final boolean faxakfn32t9h) {
        this.faxakfn32t9h = faxakfn32t9h;
        this.f5kva17wewm2 = (faxakfn32t9h ? 1.0f : 0.0f);
        return this;
    }
    
    public ControlCompactService trackColor(final int trackColor) {
        this.trackColor = trackColor;
        return this;
    }
    
    public ControlCompactService activeColor(final int activeColor) {
        this.activeColor = activeColor;
        return this;
    }
    
    public ControlCompactService knobColor(final int knobColor) {
        this.knobColor = knobColor;
        return this;
    }
    
    public ControlCompactService onToggle(final Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
    
    public boolean value() {
        return this.faxakfn32t9h;
    }
    
    public void toggle() {
        this.handleClick();
    }
    
    @Override
    protected void handleClick() {
        this.faxakfn32t9h = !this.faxakfn32t9h;
        MotionAnimateService.animate(this, MotionFiniteService.finite("knobPosition", scenePctService -> scenePctService instanceof ControlCompactService), this.faxakfn32t9h ? 1.0f : 0.0f, this.materialStyle ? MaterialIsLightService.FAST_SPATIAL : SceneEaseHandler.Spring.SNAPPY);
        if (this.onToggle != null) {
            this.onToggle.accept(this.faxakfn32t9h);
        }
        if (this.onClick != null) {
            this.onClick.run();
        }
    }
    
    @Override
    public float getAnimProperty(final String s) {
        if ("materialPress".equals(s)) {
            return this.fe1guwcmhnsj;
        }
        if ("knobPosition".equals(s)) {
            return this.f5kva17wewm2;
        }
        return super.getAnimProperty(s);
    }
    
    @Override
    public void setAnimProperty(final String s, final float n) {
        if ("materialPress".equals(s)) {
            this.fe1guwcmhnsj = n;
            return;
        }
        if ("knobPosition".equals(s)) {
            this.f5kva17wewm2 = n;
            return;
        }
        super.setAnimProperty(s, n);
    }
    
    private ThemeIsSetService m209z5wnzz3z() {
        return CoreIsInitializedHandler.get().theme();
    }
    
    private int m29do8uszx0p(final int n, final String s) {
        return ThemeIsSetService.isSet(n) ? n : this.m209z5wnzz3z().color(s);
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1107296256);
    }
    
    @Override
    public float intrinsicHeight(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return Float.intBitsToFloat(1099956224);
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        if (this.materialStyle) {
            this.m7tyob70nebn();
            if (this.f6bn6vdtdlrt) {
                MaterialCompactToggleService.compactToggle(compositorPushPresentationScaleService, this.cx, this.cy, this.cw, this.ch, this.f5kva17wewm2, this.fe1guwcmhnsj, this.hovered, this.interactive, this.f8cq29hyd6xg, this.effectiveOpacity);
            }
            else {
                MaterialCompactToggleService.toggle(compositorPushPresentationScaleService, this.cx, this.cy, this.cw, this.ch, this.f5kva17wewm2, this.fe1guwcmhnsj, this.hovered, this.interactive, this.f8cq29hyd6xg, this.effectiveOpacity);
            }
            return;
        }
        final float effectiveOpacity = this.effectiveOpacity;
        final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
        final float max = Math.max(0.0f, Math.min(1.0f, this.f5kva17wewm2));
        final int m29do8uszx0p = this.m29do8uszx0p(this.trackColor, "toggle.track");
        final int m29do8uszx0p2 = this.m29do8uszx0p(this.activeColor, "toggle.active");
        final int m29do8uszx0p3 = this.m29do8uszx0p(this.knobColor, "toggle.knob");
        final int lerpColor = lerpColor(m29do8uszx0p, m29do8uszx0p2, max);
        final float n = this.ch / 2.0f;
        compositorPushPresentationScaleService.roundedRect(this.cx, this.cy, this.cw, this.ch, n, n, n, n, ScenePctService.mulAlpha(lerpColor, effectiveOpacity), 0.0f, 0.0f, 0, 0.0f, 0, 1.0f, effectiveEdgeSoftness);
        final float n2 = 2.0f;
        final float n3 = this.ch - n2 * 2.0f;
        final float n4 = this.cx + n2 + max * (this.cw - n3 - n2 * 2.0f);
        final float n5 = n3 / 2.0f;
        compositorPushPresentationScaleService.roundedRect(n4, this.cy + n2, n3, n3, n5, n5, n5, n5, ScenePctService.mulAlpha(m29do8uszx0p3, effectiveOpacity), 0.0f, (effectiveOpacity > Float.intBitsToFloat(1041865114)) ? 2.0f : 0.0f, 805306368, 0.0f, 0, 1.0f, effectiveEdgeSoftness);
    }
    
    static int lerpColor(final int n, final int n2, float max) {
        max = Math.max(0.0f, Math.min(1.0f, max));
        return m8j78iuhzgid(n >> 24 & 0xFF, n2 >> 24 & 0xFF, max) << 24 | m8j78iuhzgid(n >> 16 & 0xFF, n2 >> 16 & 0xFF, max) << 16 | m8j78iuhzgid(n >> 8 & 0xFF, n2 >> 8 & 0xFF, max) << 8 | m8j78iuhzgid(n & 0xFF, n2 & 0xFF, max);
    }
    
    private static int m8j78iuhzgid(final int n, final int n2, final float n3) {
        return (int)(n + (n2 - n) * n3);
    }
    
    static {
        f588u4dkp372 = MotionFiniteService.of("materialPress", scenePctService -> scenePctService instanceof ControlCompactService, Float::isFinite, "a finite value");
    }
}
