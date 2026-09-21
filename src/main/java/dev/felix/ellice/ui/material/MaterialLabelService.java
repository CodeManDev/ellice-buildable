package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.svg.SvgGetTextureService;
import dev.felix.ellice.ui.text.TextMode;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;

public final class MaterialLabelService extends ScenePctService<MaterialLabelService> {
  private static final float value = 64.0f;
  private static final Map<String, Path> f8s8zx7rfw4s;
  private static final MotionFiniteService ffu8yapt69xt;
  private static final MotionFiniteService fajkjmgix7bh;
  private String fia9yp40q4zq;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private float value2;
  private float value3;
  private int fji5u0u9c2ig;
  private boolean enabled4;
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

  public MaterialLabelService active(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public MaterialLabelService reactToParent(final boolean enabled4) {
    this.enabled4 = enabled4;
    return this;
  }

  @Override
  public float getAnimProperty(final String s) {
    return switch (s) {
      case "materialMorph" -> this.value2;
      case "materialTurn" -> this.value3;
      default -> super.getAnimProperty(s);
    };
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    switch (s) {
      case "materialMorph":
        {
          this.value2 = n;
          break;
        }
      case "materialTurn":
        {
          this.value3 = n;
          break;
        }
      default:
        {
          super.setAnimProperty(s, n);
          break;
        }
    }
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    final boolean enabled2 =
        this.enabled
            || (this.enabled4
                && this.parent() != null
                && (this.parent().isHovered() || this.parent().isPressed()));
    if (!this.enabled3 || enabled2 != this.enabled2) {
      this.enabled3 = true;
      this.enabled2 = enabled2;
      MotionAnimateService.animate(
          this,
          MaterialLabelService.ffu8yapt69xt,
          enabled2 ? 1.0f : 0.0f,
          MaterialIsLightService.SPATIAL);
      MotionAnimateService.animate(
          this,
          MaterialLabelService.fajkjmgix7bh,
          enabled2 ? Float.intBitsToFloat(1110704128) : 0.0f,
          MaterialIsLightService.SPATIAL);
    }
    final SvgGetTextureService svgRenderer = compositorPushPresentationScaleService.svgRenderer();
    if (svgRenderer == null || !svgRenderer.isInitialized()) {
      return;
    }
    compositorPushPresentationScaleService.drawMorphSdfTexture(
        svgRenderer.getSdfTexture(createPath("circle"), Float.intBitsToFloat(1115684864)),
        svgRenderer.getSdfTexture(
            createPath(this.fia9yp40q4zq), Float.intBitsToFloat(1115684864)),
        this.value2,
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        this.effectiveOpacity,
        this.fji5u0u9c2ig,
        this.value3,
        Float.intBitsToFloat(1115684864));
    if (!this.f9fz3io2g8tm.isEmpty()) {
      compositorPushPresentationScaleService.nextLayer();
      final float n =
          Math.min(
                  Float.intBitsToFloat(1101004800),
                  Math.min(this.cw, this.ch)
                      / this.presentationScale
                      * Float.intBitsToFloat(1057803469))
              * this.presentationScale;
      compositorPushPresentationScaleService.text(
          this.cx
              + (this.cw
                      - compositorPushPresentationScaleService.textWidth(
                          this.f9fz3io2g8tm, n, TextMode.REGULAR, "material-roboto-medium"))
                  / 2.0f,
          this.cy
              + (this.ch
                      - compositorPushPresentationScaleService.textLineHeight(
                          n, TextMode.REGULAR, "material-roboto-medium"))
                  / 2.0f,
          this.f9fz3io2g8tm,
          n,
          ScenePctService.mulAlpha(MaterialIsLightService.ON_PRIMARY, this.effectiveOpacity),
          MaterialIsLightService.LABEL);
    }
  }

  private static Path createPath(final String key) {
    return MaterialLabelService.f8s8zx7rfw4s.computeIfAbsent(
        key,
        shapeName -> {
          String resourcePath = "assets/ellice/components/shapes/material/" + shapeName + ".svg";
          return FabricLoader.getInstance()
              .getModContainer("ellice")
              .flatMap(modContainer -> modContainer.findPath(resourcePath))
              .orElse(Path.of("src/main/resources", resourcePath));
        });
  }

  static {
    f8s8zx7rfw4s = new HashMap<String, Path>();
    ffu8yapt69xt =
        MotionFiniteService.finite(
            "materialMorph", scenePctService -> scenePctService instanceof MaterialLabelService);
    fajkjmgix7bh =
        MotionFiniteService.finite(
            "materialTurn", scenePctService2 -> scenePctService2 instanceof MaterialLabelService);
  }
}
