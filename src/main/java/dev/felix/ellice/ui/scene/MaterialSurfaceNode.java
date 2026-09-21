package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import java.util.Iterator;

public class MaterialSurfaceNode extends LayoutContainerNode {
  private static int f51vfdpkz43e;
  private final int f6b8waj8zs0s;
  private float fh41zlzpxng4;
  private float fcfizzf4a801;
  private float f19hso0oq35h;
  private float f57xhv4nfrbw;
  private float fctdx880k132;
  private float fhhzyl9d2dwc;
  private float f8yncn0wzoi2;
  private float fihicp8ssnan;
  private float felp8gm02hoa;
  private float f25nw8f70jw3;
  private float f20tgdlhmu0k;
  private int f3xjzqa5j64i;
  private MaskShape f9kc950746tn;
  private boolean f68qpeq7slwd;
  private boolean fr1pv49hkei;
  private Material f4cdt7ij1hs7;
  private float fiidemq6d4oo;
  private float fwnvh6ql5fq;
  private float faz5i39520ck;
  private float fesr2ocbeo22;
  private float fewdqj8hx8gp;
  private int fa6igwldey2;

  public MaterialSurfaceNode() {
    this.f6b8waj8zs0s = MaterialSurfaceNode.f51vfdpkz43e++;
    this.fcfizzf4a801 = Float.intBitsToFloat(-1082130432);
    this.f19hso0oq35h = Float.intBitsToFloat(-1082130432);
    this.f57xhv4nfrbw = Float.intBitsToFloat(-1082130432);
    this.fctdx880k132 = Float.intBitsToFloat(-1082130432);
    this.fihicp8ssnan = 1.0f;
    this.f25nw8f70jw3 = 1.0f;
    this.f9kc950746tn = MaskShape.ROUNDED_RECT;
    this.f4cdt7ij1hs7 = Material.NONE;
    this.fiidemq6d4oo = 1.0f;
    this.fwnvh6ql5fq = 1.0f;
    this.faz5i39520ck = 1.0f;
    this.fesr2ocbeo22 = 1.0f;
    this.fewdqj8hx8gp = 0.0f;
    this.fa6igwldey2 = -9050369;
  }

  public MaterialSurfaceNode cornerRadius(final float fh41zlzpxng4) {
    this.fh41zlzpxng4 = fh41zlzpxng4;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode cornerRadiusTL(final float fcfizzf4a801) {
    this.fcfizzf4a801 = fcfizzf4a801;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode cornerRadiusTR(final float f19hso0oq35h) {
    this.f19hso0oq35h = f19hso0oq35h;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode cornerRadiusBR(final float f57xhv4nfrbw) {
    this.f57xhv4nfrbw = f57xhv4nfrbw;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode cornerRadiusBL(final float fctdx880k132) {
    this.fctdx880k132 = fctdx880k132;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode edgeSoftness(final float fhhzyl9d2dwc) {
    this.fhhzyl9d2dwc = fhhzyl9d2dwc;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode blur(final float b) {
    this.f8yncn0wzoi2 = Math.max(0.0f, b);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode saturation(final float fihicp8ssnan) {
    this.fihicp8ssnan = fihicp8ssnan;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode brightness(final float felp8gm02hoa) {
    this.felp8gm02hoa = felp8gm02hoa;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode contrast(final float f25nw8f70jw3) {
    this.f25nw8f70jw3 = f25nw8f70jw3;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode grayscale(final float f20tgdlhmu0k) {
    this.f20tgdlhmu0k = f20tgdlhmu0k;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode tintColor(final int f3xjzqa5j64i) {
    this.f3xjzqa5j64i = f3xjzqa5j64i;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode maskShape(final MaskShape maskShape) {
    this.f9kc950746tn = ((maskShape != null) ? maskShape : MaskShape.ROUNDED_RECT);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode maskInvert(final boolean f68qpeq7slwd) {
    this.f68qpeq7slwd = f68qpeq7slwd;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode cache(final boolean fr1pv49hkei) {
    this.fr1pv49hkei = fr1pv49hkei;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode material(final String s) {
    this.f4cdt7ij1hs7 = Material.parse(s);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode material(final Material material) {
    this.f4cdt7ij1hs7 = ((material != null) ? material : Material.NONE);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialIntensity(final float b) {
    this.fiidemq6d4oo = Math.max(0.0f, b);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialScale(final float b) {
    this.fwnvh6ql5fq = Math.max(Float.intBitsToFloat(981668463), b);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialSpeed(final float faz5i39520ck) {
    this.faz5i39520ck = faz5i39520ck;
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialDistortion(final float b) {
    this.fesr2ocbeo22 = Math.max(0.0f, b);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialGlow(final float b) {
    this.fewdqj8hx8gp = Math.max(0.0f, b);
    this.invalidate();
    return this;
  }

  public MaterialSurfaceNode materialColor(final int fa6igwldey2) {
    this.fa6igwldey2 = fa6igwldey2;
    this.invalidate();
    return this;
  }

  @Override
  public float getAnimProperty(final String s) {
    return switch (s) {
      case "cornerRadius" -> this.fh41zlzpxng4;
      case "cornerRadiusTL", "cornerRadiusTopLeft" ->
          (this.fcfizzf4a801 >= 0.0f) ? this.fcfizzf4a801 : this.fh41zlzpxng4;
      case "cornerRadiusTR", "cornerRadiusTopRight" ->
          (this.f19hso0oq35h >= 0.0f) ? this.f19hso0oq35h : this.fh41zlzpxng4;
      case "cornerRadiusBR", "cornerRadiusBottomRight" ->
          (this.f57xhv4nfrbw >= 0.0f) ? this.f57xhv4nfrbw : this.fh41zlzpxng4;
      case "cornerRadiusBL", "cornerRadiusBottomLeft" ->
          (this.fctdx880k132 >= 0.0f) ? this.fctdx880k132 : this.fh41zlzpxng4;
      case "edgeSoftness" -> this.fhhzyl9d2dwc;
      case "blur" -> this.f8yncn0wzoi2;
      case "saturation" -> this.fihicp8ssnan;
      case "brightness" -> this.felp8gm02hoa;
      case "contrast" -> this.f25nw8f70jw3;
      case "grayscale" -> this.f20tgdlhmu0k;
      case "maskInvert" -> this.f68qpeq7slwd ? 1.0f : 0.0f;
      case "cache", "cached" -> this.fr1pv49hkei ? 1.0f : 0.0f;
      case "materialIntensity" -> this.fiidemq6d4oo;
      case "materialScale" -> this.fwnvh6ql5fq;
      case "materialSpeed" -> this.faz5i39520ck;
      case "materialDistortion" -> this.fesr2ocbeo22;
      case "materialGlow" -> this.fewdqj8hx8gp;
      default -> super.getAnimProperty(s);
    };
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    switch (s) {
      case "cornerRadius":
        {
          this.fh41zlzpxng4 = n;
          break;
        }
      case "cornerRadiusTL":
      case "cornerRadiusTopLeft":
        {
          this.fcfizzf4a801 = n;
          break;
        }
      case "cornerRadiusTR":
      case "cornerRadiusTopRight":
        {
          this.f19hso0oq35h = n;
          break;
        }
      case "cornerRadiusBR":
      case "cornerRadiusBottomRight":
        {
          this.f57xhv4nfrbw = n;
          break;
        }
      case "cornerRadiusBL":
      case "cornerRadiusBottomLeft":
        {
          this.fctdx880k132 = n;
          break;
        }
      case "edgeSoftness":
        {
          this.fhhzyl9d2dwc = n;
          break;
        }
      case "blur":
        {
          this.f8yncn0wzoi2 = Math.max(0.0f, n);
          break;
        }
      case "saturation":
        {
          this.fihicp8ssnan = n;
          break;
        }
      case "brightness":
        {
          this.felp8gm02hoa = n;
          break;
        }
      case "contrast":
        {
          this.f25nw8f70jw3 = n;
          break;
        }
      case "grayscale":
        {
          this.f20tgdlhmu0k = n;
          break;
        }
      case "maskInvert":
        {
          this.f68qpeq7slwd = (n >= Float.intBitsToFloat(1056964608));
          break;
        }
      case "cache":
      case "cached":
        {
          this.fr1pv49hkei = (n >= Float.intBitsToFloat(1056964608));
          break;
        }
      case "materialIntensity":
        {
          this.fiidemq6d4oo = Math.max(0.0f, n);
          break;
        }
      case "materialScale":
        {
          this.fwnvh6ql5fq = Math.max(Float.intBitsToFloat(981668463), n);
          break;
        }
      case "materialSpeed":
        {
          this.faz5i39520ck = n;
          break;
        }
      case "materialDistortion":
        {
          this.fesr2ocbeo22 = Math.max(0.0f, n);
          break;
        }
      case "materialGlow":
        {
          this.fewdqj8hx8gp = Math.max(0.0f, n);
          break;
        }
      default:
        {
          super.setAnimProperty(s, n);
          break;
        }
    }
    this.invalidate();
  }

  @Override
  public int getColorProperty(final String anObject) {
    if ("materialColor".equals(anObject)) {
      return this.fa6igwldey2;
    }
    return ("color".equals(anObject) || "tintColor".equals(anObject))
        ? this.f3xjzqa5j64i
        : super.getColorProperty(anObject);
  }

  @Override
  public void setColorProperty(final String anObject, final int n) {
    if ("materialColor".equals(anObject)) {
      this.fa6igwldey2 = n;
      this.invalidate();
    } else if ("color".equals(anObject) || "tintColor".equals(anObject)) {
      this.f3xjzqa5j64i = n;
      this.invalidate();
    } else {
      super.setColorProperty(anObject, n);
    }
  }

  @Override
  public void drawTree(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final float n) {
    this.drawTree(compositorPushPresentationScaleService, n, PresentationTransform.IDENTITY);
  }

  @Override
  void drawTree(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final float n,
      final PresentationTransform presentationTransform) {
    if (!this.visible) {
      return;
    }
    final float max = Math.max(0.0f, this.opacity);
    this.effectiveOpacity = max * n;
    if (this.effectiveOpacity < Float.intBitsToFloat(1000593162)
        || this.cw <= 0.0f
        || this.ch <= 0.0f) {
      return;
    }
    final PresentationTransform presentationTransform2 =
        this.presentationTransform(presentationTransform);
    final float cx = this.cx;
    final float cy = this.cy;
    final float cw = this.cw;
    final float ch = this.ch;
    final float presentationScale = this.presentationScale;
    this.cx = presentationTransform2.mapX(cx);
    this.cy = presentationTransform2.mapY(cy);
    this.cw = cw * presentationTransform2.scale();
    this.ch = ch * presentationTransform2.scale();
    this.presentationScale = presentationTransform2.scale();
    compositorPushPresentationScaleService.pushPresentationScale(presentationTransform2.scale());
    try {
      this.effectiveEdgeSoftness =
          Math.min(
              Math.max(
                  (this.parent != null) ? this.parent.effectiveEdgeSoftness : 0.0f,
                  this.fhhzyl9d2dwc),
              Math.min(cw, ch) * Float.intBitsToFloat(1053609165));
      final CompositorPushPresentationScaleService.LayerEffect layerEffect =
          new CompositorPushPresentationScaleService.LayerEffect(
              this.cx,
              this.cy,
              this.cw,
              this.ch,
              ((this.fcfizzf4a801 >= 0.0f) ? this.fcfizzf4a801 : this.fh41zlzpxng4)
                  * presentationTransform2.scale(),
              ((this.f19hso0oq35h >= 0.0f) ? this.f19hso0oq35h : this.fh41zlzpxng4)
                  * presentationTransform2.scale(),
              ((this.f57xhv4nfrbw >= 0.0f) ? this.f57xhv4nfrbw : this.fh41zlzpxng4)
                  * presentationTransform2.scale(),
              ((this.fctdx880k132 >= 0.0f) ? this.fctdx880k132 : this.fh41zlzpxng4)
                  * presentationTransform2.scale(),
              max,
              this.f8yncn0wzoi2 * presentationTransform2.scale(),
              this.fihicp8ssnan,
              this.felp8gm02hoa,
              this.f25nw8f70jw3,
              this.f20tgdlhmu0k,
              this.f3xjzqa5j64i,
              this.effectiveEdgeSoftness * presentationTransform2.scale(),
              this.f9kc950746tn.shaderId,
              this.f68qpeq7slwd,
              this.fr1pv49hkei ? this.f6b8waj8zs0s : 0,
              this.subtreeVersion(),
              this.f4cdt7ij1hs7.shaderId,
              this.fiidemq6d4oo,
              this.fwnvh6ql5fq,
              this.faz5i39520ck,
              this.fesr2ocbeo22,
              this.fewdqj8hx8gp,
              this.fa6igwldey2,
              this.hoverProgress,
              this.pressProgress);
      if (this.fr1pv49hkei
          && compositorPushPresentationScaleService.isLayerCacheValid(
              this.f6b8waj8zs0s, this.subtreeVersion(), this.cx, this.cy, this.cw, this.ch)) {
        compositorPushPresentationScaleService.nextLayer(layerEffect);
        compositorPushPresentationScaleService.nextLayer();
        return;
      }
      compositorPushPresentationScaleService.nextLayer(layerEffect);
      compositorPushPresentationScaleService.pushClip(
          this.cx, this.cy, this.cw, this.ch, this.fh41zlzpxng4);
      try {
        final Iterator<ScenePctService<?>> iterator = this.children().iterator();
        while (iterator.hasNext()) {
          iterator
              .next()
              .drawTree(compositorPushPresentationScaleService, n, presentationTransform2);
        }
      } finally {
        compositorPushPresentationScaleService.popClip();
      }
      compositorPushPresentationScaleService.nextLayer();
    } finally {
      compositorPushPresentationScaleService.popPresentationScale();
      this.cx = cx;
      this.cy = cy;
      this.cw = cw;
      this.ch = ch;
      this.presentationScale = presentationScale;
    }
  }

  static {
    MaterialSurfaceNode.f51vfdpkz43e = 1;
  }

  public enum MaskShape {
    ROUNDED_RECT(0),
    ELLIPSE(1),
    CIRCLE(2);

    final int shaderId;

    private MaskShape(final int shaderId) {
      this.shaderId = shaderId;
    }
  }

  public enum Material {
    NONE(0),
    HOLO_GLASS(1),
    LIQUID_GLASS(2),
    ENERGY(3),
    PORTAL(4),
    CHROMA(5);

    final int shaderId;

    private Material(final int shaderId) {
      this.shaderId = shaderId;
    }

    static Material parse(final String s) {
      if (s == null || s.isBlank()) {
        return Material.NONE;
      }
      final String replace =
          s.trim().toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
      return switch (replace) {
        case "holo", "hologlass", "holographic", "hologram" -> Material.HOLO_GLASS;
        case "liquid", "liquidglass", "glassliquid" -> Material.LIQUID_GLASS;
        case "energy", "neon", "electric", "plasma" -> Material.ENERGY;
        case "portal", "vortex", "warp" -> Material.PORTAL;
        case "chroma", "chromatic", "iridescent", "ellice" -> Material.CHROMA;
        default -> Material.NONE;
      };
    }
  }
}
