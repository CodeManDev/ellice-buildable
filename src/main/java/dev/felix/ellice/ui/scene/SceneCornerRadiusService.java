package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;

public class SceneCornerRadiusService extends ScenePctService<SceneCornerRadiusService> {
  protected float cornerRadius;
  protected float cornerTL;
  protected float cornerTR;
  protected float cornerBR;
  protected float cornerBL;
  protected float blur;
  protected float shadow;
  protected int shadowColor;
  public float borderWidth;
  public int borderColor;
  public int secondaryBorderColor;
  protected int backgroundColor;
  protected int hoverBackground;
  protected int pressBackground;
  protected float edgeSoftness;
  protected int gradientEnd;
  protected boolean insetShadow;
  protected float innerHighlight;
  protected float innerHighlightSize;
  protected boolean glass;
  protected float glassSaturation;
  protected float glassBrightness;
  protected float glassContrast;
  protected float glassRefraction;
  protected float glassNoise;
  protected float glassChromatic;
  protected int glassHighlightColor;
  protected float edgeWobble;
  protected float edgeWobbleRadius;
  private float f9l80ymct3u4;
  private float fdyk71mweq49;
  private float fpegc9vd0i2;
  private float f3pppfacncsz;
  private float fggo4jo1cxh9;

  public SceneCornerRadiusService() {
    this.cornerTL = Float.intBitsToFloat(-1082130432);
    this.cornerTR = Float.intBitsToFloat(-1082130432);
    this.cornerBR = Float.intBitsToFloat(-1082130432);
    this.cornerBL = Float.intBitsToFloat(-1082130432);
    this.shadowColor = 1073741824;
    this.backgroundColor = 0;
    this.glassSaturation = Float.intBitsToFloat(1067702026);
    this.glassBrightness = Float.intBitsToFloat(1024416809);
    this.glassContrast = Float.intBitsToFloat(1065856532);
    this.glassRefraction = Float.intBitsToFloat(1090519040);
    this.glassNoise = Float.intBitsToFloat(992204554);
    this.glassChromatic = 1.0f;
    this.glassHighlightColor = 956301311;
    this.edgeWobbleRadius = Float.intBitsToFloat(1116733440);
    this.f9l80ymct3u4 = Float.intBitsToFloat(-1082130432);
    this.fggo4jo1cxh9 = 1.0f;
  }

  public SceneCornerRadiusService cornerRadius(final float cornerRadius) {
    this.cornerRadius = cornerRadius;
    return this;
  }

  public SceneCornerRadiusService cornerRadiusTL(final float cornerTL) {
    this.cornerTL = cornerTL;
    return this;
  }

  public SceneCornerRadiusService cornerRadiusTR(final float cornerTR) {
    this.cornerTR = cornerTR;
    return this;
  }

  public SceneCornerRadiusService cornerRadiusBR(final float cornerBR) {
    this.cornerBR = cornerBR;
    return this;
  }

  public SceneCornerRadiusService cornerRadiusBL(final float cornerBL) {
    this.cornerBL = cornerBL;
    return this;
  }

  public SceneCornerRadiusService blur(final float blur) {
    this.blur = blur;
    return this;
  }

  public SceneCornerRadiusService shadow(final float shadow) {
    this.shadow = shadow;
    return this;
  }

  public SceneCornerRadiusService shadowColor(final int shadowColor) {
    this.shadowColor = shadowColor;
    return this;
  }

  public SceneCornerRadiusService border(final float borderWidth, final int borderColor) {
    this.borderWidth = borderWidth;
    this.borderColor = borderColor;
    return this;
  }

  public SceneCornerRadiusService secondaryBorderColor(final int secondaryBorderColor) {
    this.secondaryBorderColor = secondaryBorderColor;
    return this;
  }

  public SceneCornerRadiusService backgroundColor(final int backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public SceneCornerRadiusService hoverBackground(final int hoverBackground) {
    this.hoverBackground = hoverBackground;
    return this;
  }

  public SceneCornerRadiusService pressBackground(final int pressBackground) {
    this.pressBackground = pressBackground;
    return this;
  }

  public SceneCornerRadiusService edgeSoftness(final float edgeSoftness) {
    this.edgeSoftness = edgeSoftness;
    return this;
  }

  public SceneCornerRadiusService gradientEnd(final int gradientEnd) {
    this.gradientEnd = gradientEnd;
    return this;
  }

  public SceneCornerRadiusService insetShadow(final boolean insetShadow) {
    this.insetShadow = insetShadow;
    return this;
  }

  public SceneCornerRadiusService innerHighlight(final float b) {
    this.innerHighlight = Math.max(0.0f, Math.min(1.0f, b));
    return this;
  }

  public SceneCornerRadiusService innerHighlightSize(final float b) {
    this.innerHighlightSize = Math.max(0.0f, b);
    return this;
  }

  public SceneCornerRadiusService glass(final boolean glass) {
    this.glass = glass;
    return this;
  }

  public SceneCornerRadiusService glass() {
    return this.glass(true);
  }

  public SceneCornerRadiusService glassSaturation(final float glassSaturation) {
    this.glassSaturation = glassSaturation;
    return this;
  }

  public SceneCornerRadiusService glassBrightness(final float glassBrightness) {
    this.glassBrightness = glassBrightness;
    return this;
  }

  public SceneCornerRadiusService glassContrast(final float glassContrast) {
    this.glassContrast = glassContrast;
    return this;
  }

  public SceneCornerRadiusService glassRefraction(final float glassRefraction) {
    this.glassRefraction = glassRefraction;
    return this;
  }

  public SceneCornerRadiusService glassNoise(final float glassNoise) {
    this.glassNoise = glassNoise;
    return this;
  }

  public SceneCornerRadiusService glassChromatic(final float glassChromatic) {
    this.glassChromatic = glassChromatic;
    return this;
  }

  public SceneCornerRadiusService glassHighlightColor(final int glassHighlightColor) {
    this.glassHighlightColor = glassHighlightColor;
    return this;
  }

  public SceneCornerRadiusService edgeWobble(final float b) {
    this.edgeWobble = Math.max(0.0f, b);
    return this;
  }

  public SceneCornerRadiusService edgeWobbleRadius(final float b) {
    this.edgeWobbleRadius = Math.max(1.0f, b);
    return this;
  }

  @Override
  protected float ownEdgeSoftness() {
    return this.edgeSoftness;
  }

  @Override
  public float getAnimProperty(final String s) {
    return switch (s) {
      case "cornerRadius" -> this.cornerRadius;
      case "cornerRadiusTL" -> (this.cornerTL >= 0.0f) ? this.cornerTL : this.cornerRadius;
      case "cornerRadiusTR" -> (this.cornerTR >= 0.0f) ? this.cornerTR : this.cornerRadius;
      case "cornerRadiusBR" -> (this.cornerBR >= 0.0f) ? this.cornerBR : this.cornerRadius;
      case "cornerRadiusBL" -> (this.cornerBL >= 0.0f) ? this.cornerBL : this.cornerRadius;
      case "blur" -> this.blur;
      case "shadow" -> this.shadow;
      case "borderWidth" -> this.borderWidth;
      case "edgeSoftness" -> this.edgeSoftness;
      case "glass" -> this.glass ? 1.0f : 0.0f;
      case "glassSaturation" -> this.glassSaturation;
      case "glassBrightness" -> this.glassBrightness;
      case "glassContrast" -> this.glassContrast;
      case "glassRefraction" -> this.glassRefraction;
      case "glassNoise" -> this.glassNoise;
      case "glassChromatic" -> this.glassChromatic;
      case "edgeWobble" -> this.edgeWobble;
      case "edgeWobbleRadius" -> this.edgeWobbleRadius;
      case "innerHighlight" -> this.innerHighlight;
      case "innerHighlightSize" -> this.innerHighlightSize;
      default -> super.getAnimProperty(s);
    };
  }

  @Override
  public void setAnimProperty(final String s, final float b) {
    switch (s) {
      case "cornerRadius":
        {
          this.cornerRadius = b;
          break;
        }
      case "cornerRadiusTL":
        {
          this.cornerTL = b;
          break;
        }
      case "cornerRadiusTR":
        {
          this.cornerTR = b;
          break;
        }
      case "cornerRadiusBR":
        {
          this.cornerBR = b;
          break;
        }
      case "cornerRadiusBL":
        {
          this.cornerBL = b;
          break;
        }
      case "blur":
        {
          this.blur = b;
          break;
        }
      case "shadow":
        {
          this.shadow = b;
          break;
        }
      case "borderWidth":
        {
          this.borderWidth = b;
          break;
        }
      case "edgeSoftness":
        {
          this.edgeSoftness = b;
          break;
        }
      case "glass":
        {
          this.glass = (b >= Float.intBitsToFloat(1056964608));
          break;
        }
      case "glassSaturation":
        {
          this.glassSaturation = b;
          break;
        }
      case "glassBrightness":
        {
          this.glassBrightness = b;
          break;
        }
      case "glassContrast":
        {
          this.glassContrast = b;
          break;
        }
      case "glassRefraction":
        {
          this.glassRefraction = b;
          break;
        }
      case "glassNoise":
        {
          this.glassNoise = b;
          break;
        }
      case "glassChromatic":
        {
          this.glassChromatic = b;
          break;
        }
      case "edgeWobble":
        {
          this.edgeWobble = Math.max(0.0f, b);
          break;
        }
      case "edgeWobbleRadius":
        {
          this.edgeWobbleRadius = Math.max(1.0f, b);
          break;
        }
      case "innerHighlight":
        {
          this.innerHighlight = Math.max(0.0f, Math.min(1.0f, b));
          break;
        }
      case "innerHighlightSize":
        {
          this.innerHighlightSize = Math.max(0.0f, b);
          break;
        }
      default:
        {
          super.setAnimProperty(s, b);
          break;
        }
    }
  }

  @Override
  public int getColorProperty(final String s) {
    return switch (s) {
      case "backgroundColor" -> this.backgroundColor;
      case "borderColor" -> this.borderColor;
      case "borderColor2" -> this.secondaryBorderColor;
      case "shadowColor" -> this.shadowColor;
      case "hoverBackground" -> this.hoverBackground;
      case "gradientEnd" -> this.gradientEnd;
      case "glassHighlightColor" -> this.glassHighlightColor;
      default -> super.getColorProperty(s);
    };
  }

  @Override
  public void setColorProperty(final String s, final int glassHighlightColor) {
    switch (s) {
      case "backgroundColor":
        {
          this.backgroundColor = glassHighlightColor;
          break;
        }
      case "borderColor":
        {
          this.borderColor = glassHighlightColor;
          break;
        }
      case "borderColor2":
        {
          this.secondaryBorderColor = glassHighlightColor;
          break;
        }
      case "shadowColor":
        {
          this.shadowColor = glassHighlightColor;
          break;
        }
      case "hoverBackground":
        {
          this.hoverBackground = glassHighlightColor;
          break;
        }
      case "gradientEnd":
        {
          this.gradientEnd = glassHighlightColor;
          break;
        }
      case "glassHighlightColor":
        {
          this.glassHighlightColor = glassHighlightColor;
          break;
        }
      default:
        {
          super.setColorProperty(s, glassHighlightColor);
          break;
        }
    }
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    final float effectiveOpacity = this.effectiveOpacity;
    final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
    int n = this.backgroundColor;
    if (this.hoverBackground != 0) {
      n = lerpColor(n, this.hoverBackground, this.hoverProgress);
    }
    if (this.pressBackground != 0) {
      n = lerpColor(n, this.pressBackground, this.pressProgress);
    }
    final int mulAlpha = ScenePctService.mulAlpha(this.shadowColor, effectiveOpacity);
    final int mulAlpha2 = ScenePctService.mulAlpha(this.borderColor, effectiveOpacity);
    final int mulAlpha3 = ScenePctService.mulAlpha(this.secondaryBorderColor, effectiveOpacity);
    final float n2 =
        (effectiveOpacity > Float.intBitsToFloat(1036831949))
            ? (this.shadow * effectiveOpacity)
            : 0.0f;
    final float n3 = (this.cornerTL >= 0.0f) ? this.cornerTL : this.cornerRadius;
    final float n4 = (this.cornerTR >= 0.0f) ? this.cornerTR : this.cornerRadius;
    final float n5 = (this.cornerBR >= 0.0f) ? this.cornerBR : this.cornerRadius;
    final float n6 = (this.cornerBL >= 0.0f) ? this.cornerBL : this.cornerRadius;
    if (!this.glass) {
      if (this.blur > 0.0f) {
        if (n >>> 24 == 0
            && n2 <= 0.0f
            && this.borderWidth <= 0.0f
            && this.innerHighlight <= 0.0f) {
          return;
        }
        compositorPushPresentationScaleService.addRect(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            n3,
            n4,
            n5,
            n6,
            n,
            this.blur,
            n2,
            mulAlpha,
            this.borderWidth,
            mulAlpha2,
            effectiveOpacity,
            effectiveEdgeSoftness,
            this.insetShadow,
            this.gradientEnd,
            mulAlpha3,
            this.innerHighlight * effectiveOpacity,
            this.innerHighlightSize);
      } else {
        final int mulAlpha4 = ScenePctService.mulAlpha(n, effectiveOpacity);
        if (mulAlpha4 >>> 24 == 0
            && n2 <= 0.0f
            && this.borderWidth <= 0.0f
            && this.innerHighlight <= 0.0f) {
          return;
        }
        compositorPushPresentationScaleService.addRect(
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            n3,
            n4,
            n5,
            n6,
            mulAlpha4,
            0.0f,
            n2,
            mulAlpha,
            this.borderWidth,
            mulAlpha2,
            1.0f,
            effectiveEdgeSoftness,
            this.insetShadow,
            this.gradientEnd,
            mulAlpha3,
            this.innerHighlight * effectiveOpacity,
            this.innerHighlightSize);
      }
      return;
    }
    final int mulAlpha5 = ScenePctService.mulAlpha(this.glassHighlightColor, effectiveOpacity);
    final boolean b = n >>> 24 != 0 || this.gradientEnd >>> 24 != 0;
    final boolean b2 =
        this.borderWidth > 0.0f
            && (mulAlpha2 >>> 24 != 0 || mulAlpha3 >>> 24 != 0 || mulAlpha5 >>> 24 != 0);
    if (!b && n2 <= 0.0f && !b2) {
      return;
    }
    if (n2 > 0.0f) {
      compositorPushPresentationScaleService.addRect(
          this.cx,
          this.cy,
          this.cw,
          this.ch,
          n3,
          n4,
          n5,
          n6,
          0,
          0.0f,
          n2,
          mulAlpha,
          0.0f,
          0,
          1.0f,
          effectiveEdgeSoftness,
          this.insetShadow,
          0,
          0,
          0.0f,
          0.0f);
    }
    this.mebj2teccfae(compositorPushPresentationScaleService);
    compositorPushPresentationScaleService.glassRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        n3,
        n4,
        n5,
        n6,
        n,
        this.gradientEnd,
        (this.blur > 0.0f) ? this.blur : Float.intBitsToFloat(1099956224),
        effectiveOpacity,
        this.glassSaturation,
        this.glassBrightness,
        this.glassContrast,
        this.glassRefraction,
        this.glassNoise,
        this.glassChromatic,
        this.borderWidth,
        mulAlpha2,
        mulAlpha3,
        mulAlpha5,
        effectiveEdgeSoftness,
        this.edgeWobble,
        this.edgeWobbleRadius,
        this.f9l80ymct3u4,
        this.fdyk71mweq49,
        this.fpegc9vd0i2,
        this.fggo4jo1cxh9);
  }

  static int lerpColor(final int n, final int n2, final float n3) {
    if (n3 <= 0.0f) {
      return n;
    }
    if (n3 >= 1.0f) {
      return n2;
    }
    return mgfgubwgbztx(n >> 24 & 0xFF, n2 >> 24 & 0xFF, n3) << 24
        | mgfgubwgbztx(n >> 16 & 0xFF, n2 >> 16 & 0xFF, n3) << 16
        | mgfgubwgbztx(n >> 8 & 0xFF, n2 >> 8 & 0xFF, n3) << 8
        | mgfgubwgbztx(n & 0xFF, n2 & 0xFF, n3);
  }

  private static int mgfgubwgbztx(final int n, final int n2, final float n3) {
    return (int) (n + (n2 - n) * n3);
  }

  private void mebj2teccfae(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    final float min =
        Math.min(Float.intBitsToFloat(1028443341), Math.max(0.0f, SceneDtService.dt()));
    if (this.edgeWobble <= Float.intBitsToFloat(981668463)
        || this.cw <= 1.0f
        || this.ch <= 1.0f
        || min <= 0.0f) {
      this.mbbzv0tuxnv5(0.0f, min);
      return;
    }
    final float n = compositorPushPresentationScaleService.pointerX() - this.cx;
    final float n2 = compositorPushPresentationScaleService.pointerY() - this.cy;
    RubberTarget m5mou0kje00;
    final RubberTarget rubberTarget = m5mou0kje00 = this.m5mou0kje00(n, n2);
    if (this.f9l80ymct3u4 >= 0.0f) {
      final RubberTarget mjmbt0hjvedt = this.mjmbt0hjvedt((int) this.f9l80ymct3u4, n, n2);
      if (mjmbt0hjvedt.score > Float.intBitsToFloat(1014350479)
          && (mjmbt0hjvedt.score > rubberTarget.score * Float.intBitsToFloat(1054280253)
              || Math.abs(this.fpegc9vd0i2) > this.edgeWobble * Float.intBitsToFloat(1043878380))) {
        m5mou0kje00 = mjmbt0hjvedt;
      }
    }
    float amount = 0.0f;
    if (m5mou0kje00.score > Float.intBitsToFloat(1014350479)) {
      if (this.f9l80ymct3u4 < 0.0f
          || Math.abs(this.fpegc9vd0i2) < this.edgeWobble * Float.intBitsToFloat(1042536202)) {
        this.f9l80ymct3u4 = (float) m5mou0kje00.side;
        this.fdyk71mweq49 = m5mou0kje00.tangent;
        this.fggo4jo1cxh9 = m5mou0kje00.width;
      } else if ((int) this.f9l80ymct3u4 != m5mou0kje00.side
          && Math.abs(this.fpegc9vd0i2) < this.edgeWobble * Float.intBitsToFloat(1051595899)) {
        this.f9l80ymct3u4 = (float) m5mou0kje00.side;
        this.fdyk71mweq49 = m5mou0kje00.tangent;
        this.fggo4jo1cxh9 = m5mou0kje00.width;
      }
      final float n3 = 1.0f - (float) Math.exp(-min * Float.intBitsToFloat(1099956224));
      this.fdyk71mweq49 += (m5mou0kje00.tangent - this.fdyk71mweq49) * n3;
      this.fggo4jo1cxh9 += (m5mou0kje00.width - this.fggo4jo1cxh9) * n3;
      amount = m5mou0kje00.amount;
    }
    this.mbbzv0tuxnv5(amount, min);
    if (m5mou0kje00.score <= Float.intBitsToFloat(1014350479)
        && Math.abs(this.fpegc9vd0i2) < Float.intBitsToFloat(1020054733)
        && Math.abs(this.f3pppfacncsz) < Float.intBitsToFloat(1028443341)) {
      this.f9l80ymct3u4 = Float.intBitsToFloat(-1082130432);
      this.fpegc9vd0i2 = 0.0f;
      this.f3pppfacncsz = 0.0f;
    }
  }

  private void mbbzv0tuxnv5(final float n, final float n2) {
    final int n3 = 4;
    final float n4 = n2 / n3;
    for (int i = 0; i < n3; ++i) {
      this.f3pppfacncsz +=
          (Float.intBitsToFloat(-1018036224) * (this.fpegc9vd0i2 - n)
                  - Float.intBitsToFloat(1104674816) * this.f3pppfacncsz)
              * n4;
      this.fpegc9vd0i2 += this.f3pppfacncsz * n4;
    }
  }

  private RubberTarget m5mou0kje00(final float n, final float n2) {
    RubberTarget none = RubberTarget.NONE;
    for (int i = 0; i < 4; ++i) {
      final RubberTarget mjmbt0hjvedt = this.mjmbt0hjvedt(i, n, n2);
      if (mjmbt0hjvedt.score > none.score) {
        none = mjmbt0hjvedt;
      }
    }
    return none;
  }

  private RubberTarget mjmbt0hjvedt(final int n, final float n2, final float n3) {
    final float max =
        Math.max(
            Float.intBitsToFloat(1099431936), this.edgeWobble * Float.intBitsToFloat(1082130432));
    final float max2 =
        Math.max(
            Float.intBitsToFloat(1103626240), this.edgeWobble * Float.intBitsToFloat(1085695590));
    final float n4 = n2 + max * Float.intBitsToFloat(1054951342);
    final float n5 = n3 + max2 * Float.intBitsToFloat(1055622431);
    final float n6 = max * Float.intBitsToFloat(1056964608);
    final float n7 = max2 * Float.intBitsToFloat(1056964608);
    float n8 = 0.0f;
    float n9 = 0.0f;
    float n10 = 0.0f;
    float n11 = 0.0f;
    float n12 = 0.0f;
    switch (n) {
      case 0:
        {
          n8 = -n4;
          n9 = n6;
          n10 = n7;
          n11 = n5;
          n12 = this.ch;
          break;
        }
      case 1:
        {
          n8 = n4 - this.cw;
          n9 = n6;
          n10 = n7;
          n11 = n5;
          n12 = this.ch;
          break;
        }
      case 2:
        {
          n8 = -n5;
          n9 = n7;
          n10 = n6;
          n11 = n4;
          n12 = this.cw;
          break;
        }
      default:
        {
          n8 = n5 - this.ch;
          n9 = n7;
          n10 = n6;
          n11 = n4;
          n12 = this.cw;
          break;
        }
    }
    final float mayen93awqyi =
        mayen93awqyi(
            (1.0f
                    - m3cy849k25oe(
                        this.edgeWobble * Float.intBitsToFloat(1041865114),
                        this.edgeWobbleRadius * Float.intBitsToFloat(1052266988),
                        Math.abs(n8) - n9))
                * (1.0f
                    - m3cy849k25oe(
                        this.edgeWobbleRadius * Float.intBitsToFloat(1036831949),
                        this.edgeWobbleRadius * Float.intBitsToFloat(1062836634),
                        Math.max(0.0f, Math.max(-n11, n11 - n12)))));
    if (mayen93awqyi <= 0.0f) {
      return RubberTarget.NONE;
    }
    final float mayen93awqyi2 =
        mayen93awqyi(
            (n9 - Math.abs(n8) + this.edgeWobble * Float.intBitsToFloat(1066611507))
                / Math.max(1.0f, n9 + this.edgeWobble * Float.intBitsToFloat(1066611507)));
    return new RubberTarget(
        n,
        m9rtvz4w16qe(n11, 0.0f, n12),
        ((n8 >= 0.0f) ? Float.intBitsToFloat(-1082130432) : 1.0f)
            * this.edgeWobble
            * (Float.intBitsToFloat(1043878380) + Float.intBitsToFloat(1063004406) * mayen93awqyi2)
            * mayen93awqyi,
        Math.max(
            Float.intBitsToFloat(1099956224),
            n10
                + this.edgeWobbleRadius
                    * (Float.intBitsToFloat(1055622431)
                        + Float.intBitsToFloat(1043878380) * mayen93awqyi)),
        mayen93awqyi
            * (Float.intBitsToFloat(1050253722)
                + Float.intBitsToFloat(1060320051) * mayen93awqyi2));
  }

  private static float mayen93awqyi(final float b) {
    return Math.max(0.0f, Math.min(1.0f, b));
  }

  private static float m9rtvz4w16qe(final float b, final float a, final float a2) {
    return Math.max(a, Math.min(a2, b));
  }

  private static float m3cy849k25oe(final float n, final float n2, final float n3) {
    final float mayen93awqyi =
        mayen93awqyi((n3 - n) / Math.max(Float.intBitsToFloat(953267991), n2 - n));
    return mayen93awqyi * mayen93awqyi * (Float.intBitsToFloat(1077936128) - 2.0f * mayen93awqyi);
  }

  record RubberTarget(int side, float tangent, float amount, float width, float score) {
    static final RubberTarget NONE;

    static {
      NONE = new RubberTarget(-1, 0.0f, 0.0f, 1.0f, 0.0f);
    }
  }
}
