



package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class MaterialPaletteService extends ScenePctService<MaterialPaletteService>
{
    private static final MotionFiniteService fid45qpc5767;
    private static final MotionFiniteService fi73q3e9j7in;
    private int[] f4zrvvjm3emp;
    private boolean fc410sjjnhpu;
    private boolean f25e5j268429;
    private boolean fho08vybuchj;
    private float fibfmb5zbv2m;
    private float fesgqrflrwqy;
    
    public MaterialPaletteService() {
        this.f4zrvvjm3emp = new int[0];
        this.pointerEvents(false);
    }
    
    public MaterialPaletteService palette(final int[] f4zrvvjm3emp, final boolean f25e5j268429) {
        this.f4zrvvjm3emp = f4zrvvjm3emp;
        if (!this.fc410sjjnhpu) {
            this.fc410sjjnhpu = true;
            this.fibfmb5zbv2m = (f25e5j268429 ? 1.0f : 0.0f);
        }
        else if (this.f25e5j268429 != f25e5j268429) {
            MotionAnimateService.animate(this, MaterialPaletteService.fid45qpc5767, f25e5j268429 ? 1.0f : 0.0f, MaterialEnterService.RELEASE);
        }
        this.f25e5j268429 = f25e5j268429;
        return this;
    }
    
    @Override
    public float getAnimProperty(final String s) {
        return switch (s) {
            case "materialPaletteForm" -> this.fibfmb5zbv2m;
            case "materialPaletteLift" -> this.fesgqrflrwqy;
            default -> super.getAnimProperty(s);
        };
    }
    
    @Override
    public void setAnimProperty(final String s, final float n) {
        switch (s) {
            case "materialPaletteForm": {
                this.fibfmb5zbv2m = n;
                break;
            }
            case "materialPaletteLift": {
                this.fesgqrflrwqy = n;
                break;
            }
            default: {
                super.setAnimProperty(s, n);
                break;
            }
        }
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        if (this.f4zrvvjm3emp.length == 0) {
            return;
        }
        final boolean fho08vybuchj = this.parent() != null && this.parent().isHovered();
        if (this.fho08vybuchj != fho08vybuchj) {
            this.fho08vybuchj = fho08vybuchj;
            MotionAnimateService.animate(this, MaterialPaletteService.fi73q3e9j7in, fho08vybuchj ? 1.0f : 0.0f, MaterialEnterService.RELEASE);
        }
        final float presentationScale = this.presentationScale;
        final float max = Math.max(Float.intBitsToFloat(-1113336054), Math.min(Float.intBitsToFloat(1066024305), this.fibfmb5zbv2m));
        final float n = 2.0f * presentationScale;
        final float max2 = Math.max(0.0f, this.cw - n * (this.f4zrvvjm3emp.length - 1));
        float cx = this.cx;
        final float n2 = (this.f4zrvvjm3emp.length > 1) ? (Math.min(max2 * Float.intBitsToFloat(1036831949), Float.intBitsToFloat(1086324736) * presentationScale) * max) : 0.0f;
        for (int i = 0; i < this.f4zrvvjm3emp.length; ++i) {
            final float n3 = max2 / this.f4zrvvjm3emp.length + ((i == 0) ? n2 : (-n2 / Math.max(1, this.f4zrvvjm3emp.length - 1)));
            final float n4 = this.cy + Math.max(Float.intBitsToFloat(-1105618534), Math.min(Float.intBitsToFloat(1066192077), this.fesgqrflrwqy)) * ((i % 2 == 0) ? -1 : 1) * presentationScale;
            final float n5 = this.ch / presentationScale * Float.intBitsToFloat(1056964608);
            final float n6 = 2.0f + max * 2.0f;
            compositorPushPresentationScaleService.roundedRect(cx, n4, n3, this.ch, (i % 2 == 0) ? n5 : n6, (i % 2 == 0) ? n6 : n5, (i % 2 == 0) ? n5 : n6, (i % 2 == 0) ? n6 : n5, ScenePctService.mulAlpha(this.f4zrvvjm3emp[i], this.effectiveOpacity), 0.0f, 0.0f, 0, Float.intBitsToFloat(1056964608), ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, this.effectiveOpacity * Float.intBitsToFloat(1039516303)), 1.0f, this.effectiveEdgeSoftness);
            cx += n3 + n;
        }
    }
    
    static {
        fid45qpc5767 = MotionFiniteService.finite("materialPaletteForm", scenePctService -> scenePctService instanceof MaterialPaletteService);
        fi73q3e9j7in = MotionFiniteService.finite("materialPaletteLift", scenePctService2 -> scenePctService2 instanceof MaterialPaletteService);
    }
}
