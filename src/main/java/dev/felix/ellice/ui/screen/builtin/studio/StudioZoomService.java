



package dev.felix.ellice.ui.screen.builtin.studio;

import java.util.List;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.feature.studio.StudioMode;
import java.util.Map;
import java.util.Locale;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.Iterator;
import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.feature.studio.StudioCanvasRenderer;
import dev.felix.ellice.feature.studio.StudioValidateValidator;
import java.util.function.Supplier;
import dev.felix.ellice.feature.studio.StudioListenerService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class StudioZoomService extends ScenePctService<StudioZoomService>
{
    public static final float BLOCK_WIDTH = 172.0f;
    private static final MotionFiniteService fi13ctov9jzz;
    private final StudioListenerService fec4iwwpu8nv;
    private final Supplier<StudioValidateValidator.Frame> f2doots1g4cr;
    private final Supplier<Float> fbl888b7eoyp;
    private final StudioCanvasRenderer f8pwtav7l4v3;
    private final float[] f3xyhxr9102y;
    private final float[] fjf7z4kdghwg;
    private final float[] fa60fuj9c46m;
    private final boolean[] fhjrw2h6c6zl;
    private float fhn1xhk08hd5;
    private float f9qg8q0kh34h;
    private float fdxabdjgdi0y;
    private float fawuxza1rnnn;
    private float fjefzuzl4n7x;
    private float f4g3xtutrv9u;
    private float f3qagwlyzc2g;
    private float fcmnqcu1mxb3;
    private float fhz11h1dqw0t;
    private float f4qqw1vgaje1;
    private float f3zkxgc8ym0n;
    private float fcycttpb7dbl;
    private float f3wyf7kfkynh;
    private boolean f64kdxtkn3s1;
    private boolean fagkplloc0nd;
    private boolean fa7pux21lgw;
    private boolean f3umfsas966p;
    private boolean ffa499e5sj8b;
    private float fdjrt45rlv2z;
    private String fabme4nju3dh;
    private String f8apnx0zybvf;
    private String fbg2xmzromoa;
    private int f865uisvg50j;
    private Runnable f98bjqod9u94;
    private float fe605x17utjq;
    private float fbddpoc27w08;
    
    public StudioZoomService(final StudioListenerService fec4iwwpu8nv, final Supplier<StudioValidateValidator.Frame> f2doots1g4cr, final Supplier<Float> fbl888b7eoyp) {
        this.f8pwtav7l4v3 = new StudioCanvasRenderer();
        this.f3xyhxr9102y = new float[] { 1.0f, Float.intBitsToFloat(1062836634) };
        this.fjf7z4kdghwg = new float[] { 0.0f, 0.0f };
        this.fa60fuj9c46m = new float[] { 0.0f, 0.0f };
        this.fhjrw2h6c6zl = new boolean[] { false, false };
        this.fhn1xhk08hd5 = 1.0f;
        this.fawuxza1rnnn = Float.intBitsToFloat(-1082130432);
        this.fjefzuzl4n7x = Float.intBitsToFloat(-1082130432);
        this.fdjrt45rlv2z = 1.0f;
        this.f98bjqod9u94 = (() -> {});
        this.fec4iwwpu8nv = fec4iwwpu8nv;
        this.f2doots1g4cr = f2doots1g4cr;
        this.fbl888b7eoyp = fbl888b7eoyp;
        this.interactive(true).clip(true).stopPropagation(true).cursorStyle(CursorStyle.CROSSHAIR);
        this.onScrollEvent(n -> this.zoomBy((float)Math.pow(Double.longBitsToDouble(4607722850755301868L), n)));
    }
    
    public float zoom() {
        return this.fhn1xhk08hd5;
    }
    
    public StudioZoomService beforeSelect(final Runnable f98bjqod9u94) {
        this.f98bjqod9u94 = f98bjqod9u94;
        return this;
    }
    
    public float worldX(final float n) {
        return (n - this.computedX() - this.f9qg8q0kh34h) / this.fhn1xhk08hd5;
    }
    
    public float worldY(final float n) {
        return (n - this.computedY() - this.fdxabdjgdi0y) / this.fhn1xhk08hd5;
    }
    
    public boolean containsScene(final float n, final float n2) {
        return n >= this.computedX() && n2 >= this.computedY() && n < this.computedX() + this.computedW() && n2 < this.computedY() + this.computedH();
    }
    
    public float[] insertion() {
        return new float[] { this.worldX(this.computedX() + this.computedW() / 2.0f) - Float.intBitsToFloat(1114636288), this.worldY(this.computedY() + this.computedH() / 2.0f) - Float.intBitsToFloat(1109393408) };
    }
    
    public void zoomBy(final float n) {
        final float clamp = StudioValidateValidator.clamp(this.fhn1xhk08hd5 * n, Float.intBitsToFloat(1050253722), 2.0f);
        final float fe605x17utjq = (this.computedW() / 2.0f - this.f9qg8q0kh34h) / this.fhn1xhk08hd5;
        final float fbddpoc27w08 = (this.computedH() / 2.0f - this.fdxabdjgdi0y) / this.fhn1xhk08hd5;
        this.fe605x17utjq = fe605x17utjq;
        this.fbddpoc27w08 = fbddpoc27w08;
        MotionAnimateService.animate(this, StudioZoomService.fi13ctov9jzz, clamp, MaterialEnterService.PAGE);
    }
    
    @Override
    public float getAnimProperty(final String anObject) {
        return "studioZoom".equals(anObject) ? this.fhn1xhk08hd5 : super.getAnimProperty(anObject);
    }
    
    @Override
    public void setAnimProperty(final String anObject, final float n) {
        if ("studioZoom".equals(anObject)) {
            this.fhn1xhk08hd5 = StudioValidateValidator.clamp(n, Float.intBitsToFloat(1050253722), 2.0f);
            this.f9qg8q0kh34h = this.computedW() / 2.0f - this.fe605x17utjq * this.fhn1xhk08hd5;
            this.fdxabdjgdi0y = this.computedH() / 2.0f - this.fbddpoc27w08 * this.fhn1xhk08hd5;
        }
        else {
            super.setAnimProperty(anObject, n);
        }
    }
    
    public void fit() {
        MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
        float a = 0.0f;
        float a2 = 0.0f;
        float a3 = this.fec4iwwpu8nv.project.width;
        float a4 = this.fec4iwwpu8nv.project.height;
        if (this.fec4iwwpu8nv.logic && !this.fec4iwwpu8nv.project.blocks.isEmpty()) {
            a = Float.intBitsToFloat(2139095039);
            a2 = Float.intBitsToFloat(2139095039);
            a3 = Float.intBitsToFloat(-8388609);
            a4 = Float.intBitsToFloat(-8388609);
            for (final StudioShapeService.Block block : this.fec4iwwpu8nv.project.blocks) {
                a = Math.min(a, block.x);
                a2 = Math.min(a2, block.y);
                a3 = Math.max(a3, block.x + Float.intBitsToFloat(1126957056));
                a4 = Math.max(a4, block.y + blockHeight(block));
            }
        }
        this.fhn1xhk08hd5 = StudioValidateValidator.clamp(Math.min((this.computedW() - Float.intBitsToFloat(1115684864)) / (a3 - a), (this.computedH() - Float.intBitsToFloat(1116733440)) / (a4 - a2)), Float.intBitsToFloat(1050253722), Float.intBitsToFloat(1068708659));
        this.f9qg8q0kh34h = (this.computedW() - (a3 - a) * this.fhn1xhk08hd5) / 2.0f - a * this.fhn1xhk08hd5;
        this.fdxabdjgdi0y = (this.computedH() - (a4 - a2) * this.fhn1xhk08hd5) / 2.0f - a2 * this.fhn1xhk08hd5;
        this.fhjrw2h6c6zl[this.fec4iwwpu8nv.logic ? 1 : 0] = true;
        this.invalidate();
    }
    
    private void mfkg61dns80t() {
        this.f865uisvg50j = (this.fec4iwwpu8nv.logic ? 1 : 0);
        if (this.f64kdxtkn3s1 != this.fec4iwwpu8nv.logic) {
            final int f64kdxtkn3s1 = this.f64kdxtkn3s1 ? 1 : 0;
            this.f3xyhxr9102y[f64kdxtkn3s1] = this.fhn1xhk08hd5;
            this.fjf7z4kdghwg[f64kdxtkn3s1] = this.f9qg8q0kh34h;
            this.fa60fuj9c46m[f64kdxtkn3s1] = this.fdxabdjgdi0y;
            MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
            this.fhn1xhk08hd5 = this.f3xyhxr9102y[this.f865uisvg50j];
            this.f9qg8q0kh34h = this.fjf7z4kdghwg[this.f865uisvg50j];
            this.fdxabdjgdi0y = this.fa60fuj9c46m[this.f865uisvg50j];
            this.f64kdxtkn3s1 = this.fec4iwwpu8nv.logic;
        }
        if (!this.fhjrw2h6c6zl[this.f865uisvg50j] || Math.abs(this.fawuxza1rnnn - this.cw) > 1.0f || Math.abs(this.fjefzuzl4n7x - this.ch) > 1.0f) {
            this.fit();
        }
        this.fawuxza1rnnn = this.cw;
        this.fjefzuzl4n7x = this.ch;
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        this.mfkg61dns80t();
        final float floatValue = this.fbl888b7eoyp.get();
        StudioCanvasRenderer.fonts(compositorPushPresentationScaleService);
        compositorPushPresentationScaleService.pushClip(this.cx, this.cy, this.cw, this.ch, Float.intBitsToFloat(1101004800));
        compositorPushPresentationScaleService.studioPrimitive(this.cx, this.cy, this.cw, this.ch, Float.intBitsToFloat(1101004800), -15261389, -14343625, this.effectiveOpacity, new CompositorPushPresentationScaleService.StudioPaint(0, floatValue, this.f9qg8q0kh34h, this.fdxabdjgdi0y, this.fhn1xhk08hd5, 1.0f));
        compositorPushPresentationScaleService.nextLayer();
        if (this.fec4iwwpu8nv.logic) {
            this.mb1v0ygdva9h(compositorPushPresentationScaleService, this.f2doots1g4cr.get(), floatValue);
        }
        else {
            this.mikpqomdajra(compositorPushPresentationScaleService, this.f2doots1g4cr.get(), floatValue);
        }
        compositorPushPresentationScaleService.popClip();
    }
    
    private void mikpqomdajra(final CompositorPushPresentationScaleService compositorPushPresentationScaleService, final StudioValidateValidator.Frame frame, final float n) {
        final float n2 = this.cx + this.f9qg8q0kh34h;
        final float n3 = this.cy + this.fdxabdjgdi0y;
        final float n4 = this.fec4iwwpu8nv.project.width * this.fhn1xhk08hd5;
        compositorPushPresentationScaleService.roundedRect(n2, n3, n4, this.fec4iwwpu8nv.project.height * this.fhn1xhk08hd5, Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1090519040), 638720297, 0.0f, 0.0f, 0, 1.0f, 894327672, 1.0f, 0.0f);
        mjowiohlngpo(compositorPushPresentationScaleService, n2, n3 - Float.intBitsToFloat(1102577664), this.fec4iwwpu8nv.project.name.toUpperCase(Locale.ROOT), Float.intBitsToFloat(1092616192), -5458229, 1.0f);
        mjowiohlngpo(compositorPushPresentationScaleService, n2 + n4 - Float.intBitsToFloat(1115815936), n3 - Float.intBitsToFloat(1102577664), Math.round(this.fec4iwwpu8nv.project.width) + " × " + Math.round(this.fec4iwwpu8nv.project.height), Float.intBitsToFloat(1092616192), -8023381, 1.0f);
        this.f8pwtav7l4v3.draw(compositorPushPresentationScaleService, this.fec4iwwpu8nv.project, frame, n2, n3, this.fhn1xhk08hd5, this.effectiveOpacity, n);
        for (StudioCanvasRenderer.Placed placed : this.f8pwtav7l4v3.layout(this.fec4iwwpu8nv.project, frame, n2, n3, this.fhn1xhk08hd5, this.effectiveOpacity)) {
            if (placed.shape().id.equals(this.fec4iwwpu8nv.selected)) {
                compositorPushPresentationScaleService.roundedRect(placed.x() - Float.intBitsToFloat(1077936128), placed.y() - Float.intBitsToFloat(1077936128), placed.width() + Float.intBitsToFloat(1086324736), placed.height() + Float.intBitsToFloat(1086324736), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584), 0, 0.0f, 0.0f, 0, Float.intBitsToFloat(1067869798), -3424003, 1.0f, 0.0f);
                if (!placed.shape().locked) {
                    compositorPushPresentationScaleService.roundedRect(placed.x() + placed.width() - Float.intBitsToFloat(1077936128), placed.y() + placed.height() - Float.intBitsToFloat(1077936128), Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1077936128), -1647361);
                }
                mjowiohlngpo(compositorPushPresentationScaleService, placed.x(), placed.y() + placed.height() + Float.intBitsToFloat(1092616192), placed.shape().name + "  ·  " + Math.round(placed.shape().width) + " × " + Math.round(placed.shape().height), Float.intBitsToFloat(1092616192), -2700056, 1.0f);
            }
        }
    }
    
    private void mb1v0ygdva9h(final CompositorPushPresentationScaleService compositorPushPresentationScaleService, final StudioValidateValidator.Frame frame, final float n) {
        for (final StudioShapeService.Block block : this.fec4iwwpu8nv.project.blocks) {
            for (final Map.Entry entry : block.inputs.entrySet()) {
                final StudioShapeService.Block block2 = this.fec4iwwpu8nv.project.block((String)entry.getValue());
                final StudioMode.Port port = block.kind.port((String)entry.getKey());
                if (block2 != null) {
                    if (port == null) {
                        continue;
                    }
                    BezierConnectionRenderer.draw(compositorPushPresentationScaleService, this.m63aic1d9gg7(block2.x + Float.intBitsToFloat(1126957056)), this.mxl7sd19s3w(block2.y + Float.intBitsToFloat(1103626240)), this.m63aic1d9gg7(block.x), this.mxl7sd19s3w(block.y + Float.intBitsToFloat(1114374144) + block.kind.ports.indexOf(port) * 25), port.type().ink, this.fhn1xhk08hd5, this.effectiveOpacity * Float.intBitsToFloat(1061158912), n, this.fec4iwwpu8nv.playing);
                }
            }
        }
        if (this.fabme4nju3dh != null) {
            final StudioShapeService.Block block3 = this.fec4iwwpu8nv.project.block(this.fabme4nju3dh);
            if (block3 != null) {
                BezierConnectionRenderer.draw(compositorPushPresentationScaleService, this.m63aic1d9gg7(block3.x + Float.intBitsToFloat(1126957056)), this.mxl7sd19s3w(block3.y + Float.intBitsToFloat(1103626240)), this.fcycttpb7dbl, this.f3wyf7kfkynh, block3.kind.color(), this.fhn1xhk08hd5, this.effectiveOpacity, n, false);
            }
        }
        else if (this.f8apnx0zybvf != null) {
            final StudioShapeService.Block block4 = this.fec4iwwpu8nv.project.block(this.f8apnx0zybvf);
            if (block4 != null) {
                final StudioMode.Port port2 = block4.kind.port(this.fbg2xmzromoa);
                BezierConnectionRenderer.draw(compositorPushPresentationScaleService, this.fcycttpb7dbl, this.f3wyf7kfkynh, this.m63aic1d9gg7(block4.x), this.mxl7sd19s3w(block4.y + Float.intBitsToFloat(1114374144) + block4.kind.ports.indexOf(port2) * 25), port2.type().ink, this.fhn1xhk08hd5, this.effectiveOpacity, n, false);
            }
        }
        compositorPushPresentationScaleService.nextLayer();
        for (final StudioShapeService.Block block5 : this.fec4iwwpu8nv.project.blocks) {
            final float m63aic1d9gg7 = this.m63aic1d9gg7(block5.x);
            final float mxl7sd19s3w = this.mxl7sd19s3w(block5.y);
            final float n2 = Float.intBitsToFloat(1126957056) * this.fhn1xhk08hd5;
            final float n3 = blockHeight(block5) * this.fhn1xhk08hd5;
            final boolean equals = block5.id.equals(this.fec4iwwpu8nv.selected);
            compositorPushPresentationScaleService.studioPrimitive(m63aic1d9gg7, mxl7sd19s3w, n2, n3, Float.intBitsToFloat(1094713344) * this.fhn1xhk08hd5, StudioValidateValidator.mix(-14538181, block5.kind.color(), equals ? Float.intBitsToFloat(1040522936) : Float.intBitsToFloat(1027101164)), block5.kind.color(), this.effectiveOpacity, new CompositorPushPresentationScaleService.StudioPaint(5, n, this.fhn1xhk08hd5, equals ? 1.0f : 0.0f, 0.0f, 0.0f));
            mjowiohlngpo(compositorPushPresentationScaleService, m63aic1d9gg7 + Float.intBitsToFloat(1095761920) * this.fhn1xhk08hd5, mxl7sd19s3w + Float.intBitsToFloat(1095761920) * this.fhn1xhk08hd5, block5.kind.title, Float.intBitsToFloat(1094713344), block5.kind.color(), this.fhn1xhk08hd5);
            if (block5.kind.output != null) {
                this.ma9h2kz7b6gs(compositorPushPresentationScaleService, m63aic1d9gg7 + n2, mxl7sd19s3w + Float.intBitsToFloat(1103626240) * this.fhn1xhk08hd5, block5.kind.output.ink, true);
            }
            for (int i = 0; i < block5.kind.ports.size(); ++i) {
                final StudioMode.Port port3 = block5.kind.ports.get(i);
                final float n4 = mxl7sd19s3w + (59 + i * 25) * this.fhn1xhk08hd5;
                this.ma9h2kz7b6gs(compositorPushPresentationScaleService, m63aic1d9gg7, n4, port3.type().ink, block5.inputs.containsKey(port3.name()));
                mjowiohlngpo(compositorPushPresentationScaleService, m63aic1d9gg7 + Float.intBitsToFloat(1095761920) * this.fhn1xhk08hd5, n4 - Float.intBitsToFloat(1088421888) * this.fhn1xhk08hd5, port3.name(), Float.intBitsToFloat(1093664768), -2893852, this.fhn1xhk08hd5);
                if (!block5.inputs.containsKey(port3.name())) {
                    mjowiohlngpo(compositorPushPresentationScaleService, m63aic1d9gg7 + n2 - Float.intBitsToFloat(1112801280) * this.fhn1xhk08hd5, n4 - Float.intBitsToFloat(1088421888) * this.fhn1xhk08hd5, (port3.type() == StudioMode.Type.NUMBER) ? m7pwp0vygo3k(block5.defaults.getOrDefault(port3.name(), port3.fallback())) : ((port3.type() == StudioMode.Type.BOOLEAN) ? ((block5.defaults.getOrDefault(port3.name(), port3.fallback()) != 0.0f) ? "True" : "False") : "—"), Float.intBitsToFloat(1092616192), -7759693, this.fhn1xhk08hd5);
                }
            }
            String s;
            if (block5.kind.shapeOutput()) {
                final StudioShapeService.Shape shape = this.fec4iwwpu8nv.project.shape(block5.target);
                s = ((shape == null) ? "Choose a shape" : shape.name);
            }
            else if (block5.kind == StudioMode.MODULE_GATE) {
                s = (block5.target.isBlank() ? "Choose a module" : block5.target);
            }
            else {
                final StudioValidateValidator.Value value = frame.values().get(block5.id);
                s = ((value == null) ? "" : value.caption());
            }
            compositorPushPresentationScaleService.roundedRect(m63aic1d9gg7 + Float.intBitsToFloat(1092616192) * this.fhn1xhk08hd5, mxl7sd19s3w + n3 - Float.intBitsToFloat(1106771968) * this.fhn1xhk08hd5, n2 - Float.intBitsToFloat(1101004800) * this.fhn1xhk08hd5, Float.intBitsToFloat(1100480512) * this.fhn1xhk08hd5, Float.intBitsToFloat(1086324736) * this.fhn1xhk08hd5, 1428171324);
            mjowiohlngpo(compositorPushPresentationScaleService, m63aic1d9gg7 + Float.intBitsToFloat(1098907648) * this.fhn1xhk08hd5, mxl7sd19s3w + n3 - Float.intBitsToFloat(1105199104) * this.fhn1xhk08hd5, StudioCanvasRenderer.fit(compositorPushPresentationScaleService, s, Float.intBitsToFloat(1092616192) * this.fhn1xhk08hd5, n2 - Float.intBitsToFloat(1107296256) * this.fhn1xhk08hd5), Float.intBitsToFloat(1092616192), -2039826, this.fhn1xhk08hd5);
        }
    }
    
    private float m63aic1d9gg7(final float n) {
        return this.cx + this.f9qg8q0kh34h + n * this.fhn1xhk08hd5;
    }
    
    private float mxl7sd19s3w(final float n) {
        return this.cy + this.fdxabdjgdi0y + n * this.fhn1xhk08hd5;
    }
    
    private void ma9h2kz7b6gs(final CompositorPushPresentationScaleService compositorPushPresentationScaleService, final float n, final float n2, final int n3, final boolean b) {
        final float n4 = Float.intBitsToFloat(1084227584) * this.fhn1xhk08hd5;
        compositorPushPresentationScaleService.roundedRect(n - n4, n2 - n4, n4 * 2.0f, n4 * 2.0f, n4, n4, n4, n4, b ? n3 : -14339770, 0.0f, 0.0f, 0, Float.intBitsToFloat(1069547520) * this.fhn1xhk08hd5, n3, this.effectiveOpacity, 0.0f);
    }
    
    private static void mjowiohlngpo(final CompositorPushPresentationScaleService compositorPushPresentationScaleService, final float n, final float n2, final String s, final float n3, final int n4, final float n5) {
        compositorPushPresentationScaleService.text(n, n2, s, n3 * n5, n4, MaterialIsLightService.LABEL);
    }
    
    public static float blockHeight(final StudioShapeService.Block block) {
        return (float)(77 + block.kind.ports.size() * 25);
    }
    
    private static String m7pwp0vygo3k(final float n) {
        return String.format(Locale.ROOT, (Math.abs(n - Math.round(n)) < Float.intBitsToFloat(981668463)) ? "%.0f" : "%.2f", n);
    }
    
    private PortHit mj7jd3kmc1ma(final float n, final float n2) {
        for (final StudioShapeService.Block block : this.fec4iwwpu8nv.project.blocks.reversed()) {
            if (block.kind.output != null && Math.hypot(n - block.x - Float.intBitsToFloat(1126957056), n2 - block.y - Float.intBitsToFloat(1103626240)) < Float.intBitsToFloat(1093664768) / this.fhn1xhk08hd5) {
                return new PortHit(block.id, "", true, block.kind.output);
            }
            for (int i = 0; i < block.kind.ports.size(); ++i) {
                final StudioMode.Port port = block.kind.ports.get(i);
                if (Math.hypot(n - block.x, n2 - block.y - Float.intBitsToFloat(1114374144) - i * 25) < Float.intBitsToFloat(1093664768) / this.fhn1xhk08hd5) {
                    return new PortHit(block.id, port.name(), false, port.type());
                }
            }
        }
        return null;
    }
    
    @Override
    protected void onPress(final float n, final float n2) {
        this.f98bjqod9u94.run();
        this.ffa499e5sj8b = false;
        MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
        this.fcycttpb7dbl = n;
        this.f4g3xtutrv9u = n;
        this.f3wyf7kfkynh = n2;
        this.f3qagwlyzc2g = n2;
        final boolean fagkplloc0nd = false;
        this.f3umfsas966p = fagkplloc0nd;
        this.fa7pux21lgw = fagkplloc0nd;
        this.fagkplloc0nd = fagkplloc0nd;
        final String fabme4nju3dh = null;
        this.fbg2xmzromoa = fabme4nju3dh;
        this.f8apnx0zybvf = fabme4nju3dh;
        this.fabme4nju3dh = fabme4nju3dh;
        final float worldX = this.worldX(n);
        final float worldY = this.worldY(n2);
        if (this.fec4iwwpu8nv.logic) {
            final PortHit mj7jd3kmc1ma = this.mj7jd3kmc1ma(worldX, worldY);
            if (mj7jd3kmc1ma != null) {
                if (mj7jd3kmc1ma.output) {
                    this.fabme4nju3dh = mj7jd3kmc1ma.block;
                }
                else {
                    this.f8apnx0zybvf = mj7jd3kmc1ma.block;
                    this.fbg2xmzromoa = mj7jd3kmc1ma.port;
                }
                return;
            }
            for (final StudioShapeService.Block block : this.fec4iwwpu8nv.project.blocks.reversed()) {
                if (worldX >= block.x && worldX <= block.x + Float.intBitsToFloat(1126957056) && worldY >= block.y && worldY <= block.y + blockHeight(block)) {
                    this.fec4iwwpu8nv.select(block.id);
                    this.fec4iwwpu8nv.beginGesture();
                    this.fcmnqcu1mxb3 = block.x;
                    this.fhz11h1dqw0t = block.y;
                    return;
                }
            }
        }
        else {
            final List<StudioCanvasRenderer.Placed> layout = this.f8pwtav7l4v3.layout(this.fec4iwwpu8nv.project, this.f2doots1g4cr.get(), 0.0f, 0.0f, 1.0f, 1.0f);
            for (final StudioCanvasRenderer.Placed placed : layout.reversed()) {
                if (placed.shape().id.equals(this.fec4iwwpu8nv.selected) && !placed.shape().locked && Math.hypot(worldX - placed.x() - placed.width(), worldY - placed.y() - placed.height()) < Float.intBitsToFloat(1094713344) / this.fhn1xhk08hd5) {
                    this.fa7pux21lgw = true;
                    this.fec4iwwpu8nv.beginGesture();
                    this.f4qqw1vgaje1 = placed.shape().width;
                    this.f3zkxgc8ym0n = placed.shape().height;
                    this.fdjrt45rlv2z = placed.scale() / placed.appearance().scale * (1.0f + placed.appearance().scale) / 2.0f;
                    return;
                }
            }
            for (final StudioCanvasRenderer.Placed placed2 : layout.reversed()) {
                if (placed2.contains(worldX, worldY) && !placed2.shape().locked) {
                    this.fec4iwwpu8nv.select(placed2.shape().id);
                    this.fec4iwwpu8nv.beginGesture();
                    this.fcmnqcu1mxb3 = placed2.shape().x;
                    this.fhz11h1dqw0t = placed2.shape().y;
                    this.fdjrt45rlv2z = placed2.scale() / Math.max(Float.intBitsToFloat(1028443341), placed2.appearance().scale);
                    return;
                }
            }
        }
        this.fec4iwwpu8nv.select("");
        this.f3umfsas966p = true;
        this.fcmnqcu1mxb3 = this.f9qg8q0kh34h;
        this.fhz11h1dqw0t = this.fdxabdjgdi0y;
    }
    
    @Override
    protected void updateWhilePressed(final float fcycttpb7dbl, final float f3wyf7kfkynh) {
        if (this.ffa499e5sj8b) {
            return;
        }
        this.fcycttpb7dbl = fcycttpb7dbl;
        this.f3wyf7kfkynh = f3wyf7kfkynh;
        final float n = fcycttpb7dbl - this.f4g3xtutrv9u;
        final float n2 = f3wyf7kfkynh - this.f3qagwlyzc2g;
        if (Math.hypot(n, n2) > Double.longBitsToDouble(4613937818241073152L)) {
            this.fagkplloc0nd = true;
        }
        if (this.fabme4nju3dh != null || this.f8apnx0zybvf != null) {
            return;
        }
        if (this.f3umfsas966p) {
            this.f9qg8q0kh34h = this.fcmnqcu1mxb3 + n;
            this.fdxabdjgdi0y = this.fhz11h1dqw0t + n2;
            return;
        }
        if (!this.fagkplloc0nd) {
            return;
        }
        if (this.fec4iwwpu8nv.logic) {
            final StudioShapeService.Block block = this.fec4iwwpu8nv.block();
            if (block != null) {
                block.x = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.fcmnqcu1mxb3 + n / this.fhn1xhk08hd5), Float.intBitsToFloat(-969179136), Float.intBitsToFloat(1178304512));
                block.y = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.fhz11h1dqw0t + n2 / this.fhn1xhk08hd5), Float.intBitsToFloat(-969179136), Float.intBitsToFloat(1178304512));
            }
        }
        else {
            final StudioShapeService.Shape shape = this.fec4iwwpu8nv.shape();
            if (shape == null) {
                return;
            }
            if (this.fa7pux21lgw) {
                shape.width = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.f4qqw1vgaje1 + n / this.fhn1xhk08hd5 / this.fdjrt45rlv2z), Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1157234688));
                shape.height = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.f3zkxgc8ym0n + n2 / this.fhn1xhk08hd5 / this.fdjrt45rlv2z), Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1157234688));
            }
            else {
                shape.x = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.fcmnqcu1mxb3 + n / this.fhn1xhk08hd5 / this.fdjrt45rlv2z), Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296));
                shape.y = StudioValidateValidator.clamp(this.fec4iwwpu8nv.grid(this.fhz11h1dqw0t + n2 / this.fhn1xhk08hd5 / this.fdjrt45rlv2z), Float.intBitsToFloat(-981860352), Float.intBitsToFloat(1165623296));
            }
        }
    }
    
    @Override
    protected void onRelease(final boolean b) {
        if (this.ffa499e5sj8b) {
            return;
        }
        if (this.fabme4nju3dh != null || this.f8apnx0zybvf != null) {
            final PortHit mj7jd3kmc1ma = this.mj7jd3kmc1ma(this.worldX(this.fcycttpb7dbl), this.worldY(this.f3wyf7kfkynh));
            if (mj7jd3kmc1ma != null && this.fabme4nju3dh != null && !mj7jd3kmc1ma.output) {
                this.fec4iwwpu8nv.connect(this.fabme4nju3dh, mj7jd3kmc1ma.block, mj7jd3kmc1ma.port);
            }
            else if (mj7jd3kmc1ma != null && this.f8apnx0zybvf != null && mj7jd3kmc1ma.output) {
                this.fec4iwwpu8nv.connect(mj7jd3kmc1ma.block, this.f8apnx0zybvf, this.fbg2xmzromoa);
            }
            else {
                this.fec4iwwpu8nv.message = "Drag between matching colored ports.";
                this.fec4iwwpu8nv.notifyChanged();
            }
            final String fabme4nju3dh = null;
            this.fbg2xmzromoa = fabme4nju3dh;
            this.f8apnx0zybvf = fabme4nju3dh;
            this.fabme4nju3dh = fabme4nju3dh;
        }
        else if (!this.f3umfsas966p) {
            this.fec4iwwpu8nv.endGesture();
        }
        final boolean b2 = false;
        this.fa7pux21lgw = b2;
        this.f3umfsas966p = b2;
    }
    
    @Override
    protected void handleClick() {
    }
    
    public boolean cancelInteraction() {
        if (this.fabme4nju3dh == null && this.f8apnx0zybvf == null && !this.f3umfsas966p && !this.fec4iwwpu8nv.gestureActive()) {
            return false;
        }
        this.ffa499e5sj8b = true;
        final String fabme4nju3dh = null;
        this.fbg2xmzromoa = fabme4nju3dh;
        this.f8apnx0zybvf = fabme4nju3dh;
        this.fabme4nju3dh = fabme4nju3dh;
        if (!this.f3umfsas966p) {
            this.fec4iwwpu8nv.cancelGesture();
        }
        else {
            this.f9qg8q0kh34h = this.fcmnqcu1mxb3;
            this.fdxabdjgdi0y = this.fhz11h1dqw0t;
        }
        final boolean b = false;
        this.fa7pux21lgw = b;
        this.f3umfsas966p = b;
        return true;
    }
    
    static {
        fi13ctov9jzz = MotionFiniteService.finite("studioZoom", scenePctService -> scenePctService instanceof StudioZoomService);
    }
    
    record PortHit(String block, String port, boolean output, StudioMode.Type type) {}
}
