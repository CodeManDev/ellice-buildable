



package dev.felix.ellice.ui.material;

import java.util.HashMap;
import java.util.Optional;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import dev.felix.ellice.ui.svg.SvgGetTextureService;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.nio.file.Path;
import java.util.Map;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class MaterialLabelService extends ScenePctService<MaterialLabelService>
{
    private static final float f9imr9oymer2 = 64.0f;
    private static final Map<String, Path> f8s8zx7rfw4s;
    private static final MotionFiniteService ffu8yapt69xt;
    private static final MotionFiniteService fajkjmgix7bh;
    private String fia9yp40q4zq;
    private boolean f8teoig6282g;
    private boolean fhni3v3jj2b1;
    private boolean fikxfxf6g6ni;
    private float f59tbpobyb6n;
    private float f7l5t8c0ppkv;
    private int fji5u0u9c2ig;
    private boolean f4pd7h3tryq9;
    private String f9fz3io2g8tm;
    
    public MaterialLabelService label(final String s) {
        this.f9fz3io2g8tm = ((s == null) ? "" : s);
        return this;
    }
    
    public MaterialLabelService() {
        this.fia9yp40q4zq = "cookie9";
        this.fji5u0u9c2ig = MaterialIsLightService.PRIMARY;
        this.f9fz3io2g8tm = "";
        this.pointerEvents(false);
    }
    
    public MaterialLabelService shape(final String fia9yp40q4zq) {
        this.fia9yp40q4zq = fia9yp40q4zq;
        return this;
    }
    
    public MaterialLabelService tint(final int fji5u0u9c2ig) {
        this.fji5u0u9c2ig = fji5u0u9c2ig;
        return this;
    }
    
    public MaterialLabelService active(final boolean f8teoig6282g) {
        this.f8teoig6282g = f8teoig6282g;
        return this;
    }
    
    public MaterialLabelService reactToParent(final boolean f4pd7h3tryq9) {
        this.f4pd7h3tryq9 = f4pd7h3tryq9;
        return this;
    }
    
    @Override
    public float getAnimProperty(final String s) {
        return switch (s) {
            case "materialMorph" -> this.f59tbpobyb6n;
            case "materialTurn" -> this.f7l5t8c0ppkv;
            default -> super.getAnimProperty(s);
        };
    }
    
    @Override
    public void setAnimProperty(final String s, final float n) {
        switch (s) {
            case "materialMorph": {
                this.f59tbpobyb6n = n;
                break;
            }
            case "materialTurn": {
                this.f7l5t8c0ppkv = n;
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
        final boolean fhni3v3jj2b1 = this.f8teoig6282g || (this.f4pd7h3tryq9 && this.parent() != null && (this.parent().isHovered() || this.parent().isPressed()));
        if (!this.fikxfxf6g6ni || fhni3v3jj2b1 != this.fhni3v3jj2b1) {
            this.fikxfxf6g6ni = true;
            this.fhni3v3jj2b1 = fhni3v3jj2b1;
            MotionAnimateService.animate(this, MaterialLabelService.ffu8yapt69xt, fhni3v3jj2b1 ? 1.0f : 0.0f, MaterialIsLightService.SPATIAL);
            MotionAnimateService.animate(this, MaterialLabelService.fajkjmgix7bh, fhni3v3jj2b1 ? Float.intBitsToFloat(1110704128) : 0.0f, MaterialIsLightService.SPATIAL);
        }
        final SvgGetTextureService svgRenderer = compositorPushPresentationScaleService.svgRenderer();
        if (svgRenderer == null || !svgRenderer.isInitialized()) {
            return;
        }
        compositorPushPresentationScaleService.drawMorphSdfTexture(svgRenderer.getSdfTexture(m2776m24r1zb("circle"), Float.intBitsToFloat(1115684864)), svgRenderer.getSdfTexture(m2776m24r1zb(this.fia9yp40q4zq), Float.intBitsToFloat(1115684864)), this.f59tbpobyb6n, this.cx, this.cy, this.cw, this.ch, this.effectiveOpacity, this.fji5u0u9c2ig, this.f7l5t8c0ppkv, Float.intBitsToFloat(1115684864));
        if (!this.f9fz3io2g8tm.isEmpty()) {
            compositorPushPresentationScaleService.nextLayer();
            final float n = Math.min(Float.intBitsToFloat(1101004800), Math.min(this.cw, this.ch) / this.presentationScale * Float.intBitsToFloat(1057803469)) * this.presentationScale;
            compositorPushPresentationScaleService.text(this.cx + (this.cw - compositorPushPresentationScaleService.textWidth(this.f9fz3io2g8tm, n, TextMode.REGULAR, "material-roboto-medium")) / 2.0f, this.cy + (this.ch - compositorPushPresentationScaleService.textLineHeight(n, TextMode.REGULAR, "material-roboto-medium")) / 2.0f, this.f9fz3io2g8tm, n, ScenePctService.mulAlpha(MaterialIsLightService.ON_PRIMARY, this.effectiveOpacity), MaterialIsLightService.LABEL);
        }
    }
    
    private static Path m2776m24r1zb(final String key) {
        return MaterialLabelService.f8s8zx7rfw4s.computeIfAbsent(key, shapeName -> {
            String resourcePath = "assets/ellice/components/shapes/material/" + shapeName + ".svg";
            return FabricLoader.getInstance().getModContainer("ellice").flatMap(modContainer -> modContainer.findPath(resourcePath)).orElse(Path.of("src/main/resources", resourcePath));
        });
    }
    
    static {
        f8s8zx7rfw4s = new HashMap<String, Path>();
        ffu8yapt69xt = MotionFiniteService.finite("materialMorph", scenePctService -> scenePctService instanceof MaterialLabelService);
        fajkjmgix7bh = MotionFiniteService.finite("materialTurn", scenePctService2 -> scenePctService2 instanceof MaterialLabelService);
    }
}
