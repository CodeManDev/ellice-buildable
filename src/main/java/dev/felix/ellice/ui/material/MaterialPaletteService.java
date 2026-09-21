package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class MaterialPaletteService extends ScenePctService<MaterialPaletteService> {
  private static final MotionFiniteService fid45qpc5767;
  private static final MotionFiniteService fi73q3e9j7in;
  private int[] int2;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private float value;
  private float fesgqrflrwqy;

  public MaterialPaletteService() {
    this.int2 = new int[0];
    this.pointerEvents(false);
  }

  public MaterialPaletteService palette(final int[] int2, final boolean enabled2) {
    this.int2 = int2;
    if (!this.enabled) {
      this.enabled = true;
      this.value = (enabled2 ? 1.0f : 0.0f);
    } else if (this.enabled2 != enabled2) {
      MotionAnimateService.animate(
          this,
          MaterialPaletteService.fid45qpc5767,
          enabled2 ? 1.0f : 0.0f,
          MaterialEnterService.RELEASE);
    }
    this.enabled2 = enabled2;
    return this;
  }

  @Override
  public float getAnimProperty(final String s) {
    return switch (s) {
      case "materialPaletteForm" -> this.value;
      case "materialPaletteLift" -> this.fesgqrflrwqy;
      default -> super.getAnimProperty(s);
    };
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    switch (s) {
      case "materialPaletteForm":
        {
          this.value = n;
          break;
        }
      case "materialPaletteLift":
        {
          this.fesgqrflrwqy = n;
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
    if (this.int2.length == 0) {
      return;
    }
    final boolean enabled3 = this.parent() != null && this.parent().isHovered();
    if (this.enabled3 != enabled3) {
      this.enabled3 = enabled3;
      MotionAnimateService.animate(
          this,
          MaterialPaletteService.fi73q3e9j7in,
          enabled3 ? 1.0f : 0.0f,
          MaterialEnterService.RELEASE);
    }
    final float presentationScale = this.presentationScale;
    final float max =
        Math.max(
            Float.intBitsToFloat(-1113336054),
            Math.min(Float.intBitsToFloat(1066024305), this.value));
    final float n = 2.0f * presentationScale;
    final float max2 = Math.max(0.0f, this.cw - n * (this.int2.length - 1));
    float cx = this.cx;
    final float n2 =
        (this.int2.length > 1)
            ? (Math.min(
                    max2 * Float.intBitsToFloat(1036831949),
                    Float.intBitsToFloat(1086324736) * presentationScale)
                * max)
            : 0.0f;
    for (int i = 0; i < this.int2.length; ++i) {
      final float n3 =
          max2 / this.int2.length + ((i == 0) ? n2 : (-n2 / Math.max(1, this.int2.length - 1)));
      final float n4 =
          this.cy
              + Math.max(
                      Float.intBitsToFloat(-1105618534),
                      Math.min(Float.intBitsToFloat(1066192077), this.fesgqrflrwqy))
                  * ((i % 2 == 0) ? -1 : 1)
                  * presentationScale;
      final float n5 = this.ch / presentationScale * Float.intBitsToFloat(1056964608);
      final float n6 = 2.0f + max * 2.0f;
      compositorPushPresentationScaleService.roundedRect(
          cx,
          n4,
          n3,
          this.ch,
          (i % 2 == 0) ? n5 : n6,
          (i % 2 == 0) ? n6 : n5,
          (i % 2 == 0) ? n5 : n6,
          (i % 2 == 0) ? n6 : n5,
          ScenePctService.mulAlpha(this.int2[i], this.effectiveOpacity),
          0.0f,
          0.0f,
          0,
          Float.intBitsToFloat(1056964608),
          ScenePctService.mulAlpha(
              MaterialIsLightService.ON_SURFACE,
              this.effectiveOpacity * Float.intBitsToFloat(1039516303)),
          1.0f,
          this.effectiveEdgeSoftness);
      cx += n3 + n;
    }
  }

  static {
    fid45qpc5767 =
        MotionFiniteService.finite(
            "materialPaletteForm",
            scenePctService -> scenePctService instanceof MaterialPaletteService);
    fi73q3e9j7in =
        MotionFiniteService.finite(
            "materialPaletteLift",
            scenePctService2 -> scenePctService2 instanceof MaterialPaletteService);
  }
}
