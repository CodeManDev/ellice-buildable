



package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.function.Function;
import java.util.function.Consumer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.ScenePctService;

public class ControlGetAnimPropertyService extends ScenePctService<ControlGetAnimPropertyService>
{
    private int fblhtbnm9trh;
    private boolean f1ndx5hjiyjd;
    private boolean f4t1t1yv3cjp;
    private boolean fjd250tu0rcy;
    private float f88puk79u5it;
    private float finuxaglzsz9;
    private boolean fbrjruucfhts;
    private boolean f4uoz4scc60v;
    private boolean f7t7uipxc2h;
    private static final MotionFiniteService ff9i1ag5hhkx;
    private static final MotionFiniteService fhhnrdwz9i77;
    private String fhnudul1y2f;
    private int f6uswfhi9ezd;
    private int f787mavw6kro;
    private int f4yg70t7813b;
    private float f5w2ocxla71e;
    private float fehkfxtewt6o;
    public Consumer<Integer> onChange;
    private Function<Integer, String> f941vbjzz86e;
    private Runnable f2pn841lo8x8;
    
    @Override
    public float getAnimProperty(final String s) {
        return switch (s) {
            case "materialKeyPress" -> this.f88puk79u5it;
            case "materialKeyListening" -> this.finuxaglzsz9;
            default -> super.getAnimProperty(s);
        };
    }
    
    @Override
    public void setAnimProperty(final String s, final float n) {
        switch (s) {
            case "materialKeyPress": {
                this.f88puk79u5it = n;
                break;
            }
            case "materialKeyListening": {
                this.finuxaglzsz9 = n;
                break;
            }
            default: {
                super.setAnimProperty(s, n);
                break;
            }
        }
    }
    
    public ControlGetAnimPropertyService material(final boolean f4t1t1yv3cjp) {
        this.f4t1t1yv3cjp = f4t1t1yv3cjp;
        return this;
    }
    
    public void materialFocus(final boolean fjd250tu0rcy) {
        this.fjd250tu0rcy = fjd250tu0rcy;
        this.invalidate();
    }
    
    public void activate() {
        this.handleClick();
    }
    
    public ControlGetAnimPropertyService chooser(final Runnable f2pn841lo8x8) {
        this.f2pn841lo8x8 = f2pn841lo8x8;
        return this;
    }
    
    public ControlGetAnimPropertyService() {
        this.fblhtbnm9trh = -1;
        this.fhnudul1y2f = "None";
        this.f6uswfhi9ezd = 553648127;
        this.f787mavw6kro = 1090479718;
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
        this.mc6t376a1i0c();
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
    
    public ControlGetAnimPropertyService keyNameFn(final Function<Integer, String> f941vbjzz86e) {
        if (this.f941vbjzz86e == f941vbjzz86e) {
            return this;
        }
        this.f941vbjzz86e = f941vbjzz86e;
        this.mc6t376a1i0c();
        this.invalidateLayout();
        return this;
    }
    
    public int keyCode() {
        return this.fblhtbnm9trh;
    }
    
    public boolean isListening() {
        return this.f1ndx5hjiyjd;
    }
    
    private void mc6t376a1i0c() {
        if (this.f941vbjzz86e != null) {
            this.fhnudul1y2f = this.f941vbjzz86e.apply(this.fblhtbnm9trh);
        }
        else if (this.fblhtbnm9trh == -1) {
            this.fhnudul1y2f = "None";
        }
        else {
            this.fhnudul1y2f = "Key" + this.fblhtbnm9trh;
        }
    }
    
    @Override
    protected void handleClick() {
        if (this.f2pn841lo8x8 != null) {
            this.f1ndx5hjiyjd = false;
            this.f2pn841lo8x8.run();
            return;
        }
        this.f1ndx5hjiyjd = !this.f1ndx5hjiyjd;
        if (this.onClick != null) {
            this.onClick.run();
        }
    }
    
    @Override
    protected void handleRightClick() {
        this.fblhtbnm9trh = -1;
        this.f1ndx5hjiyjd = false;
        this.mc6t376a1i0c();
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
            this.f1ndx5hjiyjd = false;
            return;
        }
        this.fblhtbnm9trh = fblhtbnm9trh;
        this.f1ndx5hjiyjd = false;
        this.mc6t376a1i0c();
        this.invalidateLayout();
        if (this.onChange != null) {
            this.onChange.accept(this.fblhtbnm9trh);
        }
    }
    
    @Override
    public float intrinsicWidth(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        if (this.f4t1t1yv3cjp) {
            return Math.max(Float.intBitsToFloat(1111490560), compositorPushPresentationScaleService.textWidth(this.fhnudul1y2f, Float.intBitsToFloat(1096810496), TextMode.REGULAR, "material-roboto-medium", Float.intBitsToFloat(1036831949)) + Float.intBitsToFloat(1107296256));
        }
        return Math.max(Float.intBitsToFloat(1108344832), compositorPushPresentationScaleService.textWidth(this.fhnudul1y2f, this.f5w2ocxla71e) + Float.intBitsToFloat(1094713344));
    }
    
    @Override
    public float intrinsicHeight(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        return this.f4t1t1yv3cjp ? Float.intBitsToFloat(1111490560) : (compositorPushPresentationScaleService.textLineHeight(this.f5w2ocxla71e) + Float.intBitsToFloat(1086324736));
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        final float effectiveOpacity = this.effectiveOpacity;
        if (this.f4t1t1yv3cjp) {
            if (this.pressed != this.fbrjruucfhts) {
                this.fbrjruucfhts = this.pressed;
                MotionAnimateService.animate(this, ControlGetAnimPropertyService.ff9i1ag5hhkx, this.pressed ? 1.0f : 0.0f, MaterialIsLightService.FAST_SPATIAL);
            }
            if (this.hovered != this.f4uoz4scc60v) {
                this.f4uoz4scc60v = this.hovered;
                MotionAnimateService.animate(this, MotionColorsContainer.Floats.HOVER_PROGRESS, this.hovered ? 1.0f : 0.0f, MaterialIsLightService.FAST_EFFECTS);
            }
            if (this.f1ndx5hjiyjd != this.f7t7uipxc2h) {
                this.f7t7uipxc2h = this.f1ndx5hjiyjd;
                MotionAnimateService.animate(this, ControlGetAnimPropertyService.fhhnrdwz9i77, this.f1ndx5hjiyjd ? 1.0f : 0.0f, MaterialIsLightService.FAST_EFFECTS);
            }
            final float max = Math.max(0.0f, Math.min(1.0f, this.f88puk79u5it));
            final float max2 = Math.max(0.0f, Math.min(1.0f, this.finuxaglzsz9));
            int n = MaterialIsLightService.layer(MaterialIsLightService.ON_SECONDARY_CONTAINER, MaterialIsLightService.ON_PRIMARY_CONTAINER, max2);
            final int layer = MaterialIsLightService.layer(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.PRIMARY_CONTAINER, max2);
            int n2;
            if (this.interactive) {
                n2 = MaterialIsLightService.layer(layer, n, Float.intBitsToFloat(1034147594) * this.getAnimProperty("hoverProgress") + Float.intBitsToFloat(1017370378) * max);
            }
            else {
                n2 = MaterialIsLightService.layer(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1039516303));
                n = MaterialIsLightService.layer(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1052938076));
            }
            final float n3 = Float.intBitsToFloat(1101004800) - Float.intBitsToFloat(1094713344) * max;
            compositorPushPresentationScaleService.roundedRect(this.cx, this.cy + Float.intBitsToFloat(1082130432), this.cw, this.ch - Float.intBitsToFloat(1090519040), n3, ScenePctService.mulAlpha(n2, effectiveOpacity));
            if (this.fjd250tu0rcy && this.interactive) {
                compositorPushPresentationScaleService.roundedRect(this.cx - Float.intBitsToFloat(1077936128), this.cy + 1.0f, this.cw + Float.intBitsToFloat(1086324736), this.ch - 2.0f, n3 + Float.intBitsToFloat(1077936128), n3 + Float.intBitsToFloat(1077936128), n3 + Float.intBitsToFloat(1077936128), n3 + Float.intBitsToFloat(1077936128), 0, 0.0f, 0.0f, 0, 2.0f, ScenePctService.mulAlpha(MaterialIsLightService.PRIMARY, effectiveOpacity), 1.0f, Float.intBitsToFloat(1058642330));
            }
            String s = this.f1ndx5hjiyjd ? "Press a key · Esc to cancel" : this.fhnudul1y2f;
            float n4 = compositorPushPresentationScaleService.textWidth(s, Float.intBitsToFloat(1096810496), TextMode.REGULAR, "material-roboto-medium", Float.intBitsToFloat(1036831949));
            if (this.f1ndx5hjiyjd && n4 > this.cw - Float.intBitsToFloat(1107296256)) {
                s = "Press a key · Esc cancels";
                n4 = compositorPushPresentationScaleService.textWidth(s, Float.intBitsToFloat(1096810496), TextMode.REGULAR, "material-roboto-medium", Float.intBitsToFloat(1036831949));
            }
            if (this.f1ndx5hjiyjd && n4 > this.cw - Float.intBitsToFloat(1107296256)) {
                s = "Press a key";
                n4 = compositorPushPresentationScaleService.textWidth(s, Float.intBitsToFloat(1096810496), TextMode.REGULAR, "material-roboto-medium", Float.intBitsToFloat(1036831949));
            }
            compositorPushPresentationScaleService.pushClip(this.cx + Float.intBitsToFloat(1098907648), this.cy, this.cw - Float.intBitsToFloat(1107296256), this.ch);
            compositorPushPresentationScaleService.text(this.cx + (this.cw - n4) / 2.0f, this.cy + (this.ch - compositorPushPresentationScaleService.textLineHeight(Float.intBitsToFloat(1096810496), TextMode.REGULAR, "material-roboto-medium")) / 2.0f, s, Float.intBitsToFloat(1096810496), ScenePctService.mulAlpha(n, effectiveOpacity), MaterialIsLightService.LABEL.withLetterSpacing(Float.intBitsToFloat(1036831949)));
            compositorPushPresentationScaleService.popClip();
            return;
        }
        final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
        final int n5 = this.f1ndx5hjiyjd ? this.f787mavw6kro : (this.hovered ? 822083583 : this.f6uswfhi9ezd);
        final float fehkfxtewt6o = this.fehkfxtewt6o;
        compositorPushPresentationScaleService.roundedRect(this.cx, this.cy, this.cw, this.ch, fehkfxtewt6o, fehkfxtewt6o, fehkfxtewt6o, fehkfxtewt6o, ScenePctService.mulAlpha(n5, effectiveOpacity), 0.0f, 0.0f, 0, 0.0f, 0, 1.0f, effectiveEdgeSoftness);
        final String s2 = this.f1ndx5hjiyjd ? "..." : this.fhnudul1y2f;
        compositorPushPresentationScaleService.text(this.cx + (this.cw - compositorPushPresentationScaleService.textWidth(s2, this.f5w2ocxla71e)) / 2.0f, this.cy + (this.ch - this.f5w2ocxla71e) / 2.0f, s2, this.f5w2ocxla71e, ScenePctService.mulAlpha(this.f4yg70t7813b, effectiveOpacity));
    }
    
    static {
        ff9i1ag5hhkx = MotionFiniteService.finite("materialKeyPress", scenePctService -> scenePctService instanceof ControlGetAnimPropertyService);
        fhhnrdwz9i77 = MotionFiniteService.finite("materialKeyListening", scenePctService2 -> scenePctService2 instanceof ControlGetAnimPropertyService);
    }
}
