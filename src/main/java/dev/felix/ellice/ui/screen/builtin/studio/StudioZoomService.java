package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.feature.studio.StudioCanvasRenderer;
import dev.felix.ellice.feature.studio.StudioListenerService;
import dev.felix.ellice.feature.studio.StudioMode;
import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.feature.studio.StudioValidateValidator;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public final class StudioZoomService extends ScenePctService<StudioZoomService> {
  public static final float BLOCK_WIDTH = 172.0f;
  private static final MotionFiniteService fi13ctov9jzz;
  private final StudioListenerService items;
  private final Supplier<StudioValidateValidator.Frame> supplier;
  private final Supplier<Float> nextSupplier;
  private final StudioCanvasRenderer f8pwtav7l4v3;
  private final float[] f3xyhxr9102y;
  private final float[] fjf7z4kdghwg;
  private final float[] fa60fuj9c46m;
  private final boolean[] fhjrw2h6c6zl;
  private float value;
  private float value2;
  private float fdxabdjgdi0y;
  private float fawuxza1rnnn;
  private float fjefzuzl4n7x;
  private float value6;
  private float value7;
  private float value8;
  private float value9;
  private float value10;
  private float value11;
  private float fcycttpb7dbl;
  private float value13;
  private boolean enabled;
  private boolean fagkplloc0nd;
  private boolean fa7pux21lgw;
  private boolean enabled4;
  private boolean enabled5;
  private float fdjrt45rlv2z;
  private String text2;
  private String text3;
  private String fbg2xmzromoa;
  private int count;
  private Runnable currentRunnable;
  private float value15;
  private float value16;

  public StudioZoomService(
      final StudioListenerService items,
      final Supplier<StudioValidateValidator.Frame> supplier,
      final Supplier<Float> nextSupplier) {
    this.f8pwtav7l4v3 = new StudioCanvasRenderer();
    this.f3xyhxr9102y = new float[] {1.0f, Float.intBitsToFloat(1062836634)};
    this.fjf7z4kdghwg = new float[] {0.0f, 0.0f};
    this.fa60fuj9c46m = new float[] {0.0f, 0.0f};
    this.fhjrw2h6c6zl = new boolean[] {false, false};
    this.value = 1.0f;
    this.fawuxza1rnnn = Float.intBitsToFloat(-1082130432);
    this.fjefzuzl4n7x = Float.intBitsToFloat(-1082130432);
    this.fdjrt45rlv2z = 1.0f;
    this.currentRunnable = (() -> {});
    this.items = items;
    this.supplier = supplier;
    this.nextSupplier = nextSupplier;
    this.interactive(true).clip(true).stopPropagation(true).cursorStyle(CursorStyle.CROSSHAIR);
    this.onScrollEvent(
        n -> this.zoomBy((float) Math.pow(Double.longBitsToDouble(4607722850755301868L), n)));
  }

  public float zoom() {
    return this.value;
  }

  public StudioZoomService beforeSelect(final Runnable currentRunnable) {
    this.currentRunnable = currentRunnable;
    return this;
  }

  public float worldX(final float n) {
    return (n - this.computedX() - this.value2) / this.value;
  }

  public float worldY(final float n) {
    return (n - this.computedY() - this.fdxabdjgdi0y) / this.value;
  }

  public boolean containsScene(final float n, final float n2) {
    return n >= this.computedX()
        && n2 >= this.computedY()
        && n < this.computedX() + this.computedW()
        && n2 < this.computedY() + this.computedH();
  }

  public float[] insertion() {
    return new float[] {
      this.worldX(this.computedX() + this.computedW() / 2.0f) - Float.intBitsToFloat(1114636288),
      this.worldY(this.computedY() + this.computedH() / 2.0f) - Float.intBitsToFloat(1109393408)
    };
  }

  public void zoomBy(final float n) {
    final float clamp =
        StudioValidateValidator.clamp(this.value * n, Float.intBitsToFloat(1050253722), 2.0f);
    final float value15 = (this.computedW() / 2.0f - this.value2) / this.value;
    final float value16 = (this.computedH() / 2.0f - this.fdxabdjgdi0y) / this.value;
    this.value15 = value15;
    this.value16 = value16;
    MotionAnimateService.animate(
        this, StudioZoomService.fi13ctov9jzz, clamp, MaterialEnterService.PAGE);
  }

  @Override
  public float getAnimProperty(final String anObject) {
    return "studioZoom".equals(anObject) ? this.value : super.getAnimProperty(anObject);
  }

  @Override
  public void setAnimProperty(final String anObject, final float n) {
    if ("studioZoom".equals(anObject)) {
      this.value = StudioValidateValidator.clamp(n, Float.intBitsToFloat(1050253722), 2.0f);
      this.value2 = this.computedW() / 2.0f - this.value15 * this.value;
      this.fdxabdjgdi0y = this.computedH() / 2.0f - this.value16 * this.value;
    } else {
      super.setAnimProperty(anObject, n);
    }
  }

  public void fit() {
    MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
    float a = 0.0f;
    float a2 = 0.0f;
    float a3 = this.items.project.width;
    float a4 = this.items.project.height;
    if (this.items.logic && !this.items.project.blocks.isEmpty()) {
      a = Float.intBitsToFloat(2139095039);
      a2 = Float.intBitsToFloat(2139095039);
      a3 = Float.intBitsToFloat(-8388609);
      a4 = Float.intBitsToFloat(-8388609);
      for (final StudioShapeService.Block block : this.items.project.blocks) {
        a = Math.min(a, block.x);
        a2 = Math.min(a2, block.y);
        a3 = Math.max(a3, block.x + Float.intBitsToFloat(1126957056));
        a4 = Math.max(a4, block.y + blockHeight(block));
      }
    }
    this.value =
        StudioValidateValidator.clamp(
            Math.min(
                (this.computedW() - Float.intBitsToFloat(1115684864)) / (a3 - a),
                (this.computedH() - Float.intBitsToFloat(1116733440)) / (a4 - a2)),
            Float.intBitsToFloat(1050253722),
            Float.intBitsToFloat(1068708659));
    this.value2 = (this.computedW() - (a3 - a) * this.value) / 2.0f - a * this.value;
    this.fdxabdjgdi0y = (this.computedH() - (a4 - a2) * this.value) / 2.0f - a2 * this.value;
    this.fhjrw2h6c6zl[this.items.logic ? 1 : 0] = true;
    this.invalidate();
  }

  private void mfkg61dns80t() {
    this.count = (this.items.logic ? 1 : 0);
    if (this.enabled != this.items.logic) {
      final int enabled = this.enabled ? 1 : 0;
      this.f3xyhxr9102y[enabled] = this.value;
      this.fjf7z4kdghwg[enabled] = this.value2;
      this.fa60fuj9c46m[enabled] = this.fdxabdjgdi0y;
      MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
      this.value = this.f3xyhxr9102y[this.count];
      this.value2 = this.fjf7z4kdghwg[this.count];
      this.fdxabdjgdi0y = this.fa60fuj9c46m[this.count];
      this.enabled = this.items.logic;
    }
    if (!this.fhjrw2h6c6zl[this.count]
        || Math.abs(this.fawuxza1rnnn - this.cw) > 1.0f
        || Math.abs(this.fjefzuzl4n7x - this.ch) > 1.0f) {
      this.fit();
    }
    this.fawuxza1rnnn = this.cw;
    this.fjefzuzl4n7x = this.ch;
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    this.mfkg61dns80t();
    final float floatValue = this.nextSupplier.get();
    StudioCanvasRenderer.fonts(compositorPushPresentationScaleService);
    compositorPushPresentationScaleService.pushClip(
        this.cx, this.cy, this.cw, this.ch, Float.intBitsToFloat(1101004800));
    compositorPushPresentationScaleService.studioPrimitive(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        Float.intBitsToFloat(1101004800),
        -15261389,
        -14343625,
        this.effectiveOpacity,
        new CompositorPushPresentationScaleService.StudioPaint(
            0, floatValue, this.value2, this.fdxabdjgdi0y, this.value, 1.0f));
    compositorPushPresentationScaleService.nextLayer();
    if (this.items.logic) {
      this.mb1v0ygdva9h(compositorPushPresentationScaleService, this.supplier.get(), floatValue);
    } else {
      this.mikpqomdajra(compositorPushPresentationScaleService, this.supplier.get(), floatValue);
    }
    compositorPushPresentationScaleService.popClip();
  }

  private void mikpqomdajra(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final StudioValidateValidator.Frame frame,
      final float n) {
    final float n2 = this.cx + this.value2;
    final float n3 = this.cy + this.fdxabdjgdi0y;
    final float n4 = this.items.project.width * this.value;
    compositorPushPresentationScaleService.roundedRect(
        n2,
        n3,
        n4,
        this.items.project.height * this.value,
        Float.intBitsToFloat(1090519040),
        Float.intBitsToFloat(1090519040),
        Float.intBitsToFloat(1090519040),
        Float.intBitsToFloat(1090519040),
        638720297,
        0.0f,
        0.0f,
        0,
        1.0f,
        894327672,
        1.0f,
        0.0f);
    mjowiohlngpo(
        compositorPushPresentationScaleService,
        n2,
        n3 - Float.intBitsToFloat(1102577664),
        this.items.project.name.toUpperCase(Locale.ROOT),
        Float.intBitsToFloat(1092616192),
        -5458229,
        1.0f);
    mjowiohlngpo(
        compositorPushPresentationScaleService,
        n2 + n4 - Float.intBitsToFloat(1115815936),
        n3 - Float.intBitsToFloat(1102577664),
        Math.round(this.items.project.width) + " × " + Math.round(this.items.project.height),
        Float.intBitsToFloat(1092616192),
        -8023381,
        1.0f);
    this.f8pwtav7l4v3.draw(
        compositorPushPresentationScaleService,
        this.items.project,
        frame,
        n2,
        n3,
        this.value,
        this.effectiveOpacity,
        n);
    for (StudioCanvasRenderer.Placed placed :
        this.f8pwtav7l4v3.layout(
            this.items.project, frame, n2, n3, this.value, this.effectiveOpacity)) {
      if (placed.shape().id.equals(this.items.selected)) {
        compositorPushPresentationScaleService.roundedRect(
            placed.x() - Float.intBitsToFloat(1077936128),
            placed.y() - Float.intBitsToFloat(1077936128),
            placed.width() + Float.intBitsToFloat(1086324736),
            placed.height() + Float.intBitsToFloat(1086324736),
            Float.intBitsToFloat(1084227584),
            Float.intBitsToFloat(1084227584),
            Float.intBitsToFloat(1084227584),
            Float.intBitsToFloat(1084227584),
            0,
            0.0f,
            0.0f,
            0,
            Float.intBitsToFloat(1067869798),
            -3424003,
            1.0f,
            0.0f);
        if (!placed.shape().locked) {
          compositorPushPresentationScaleService.roundedRect(
              placed.x() + placed.width() - Float.intBitsToFloat(1077936128),
              placed.y() + placed.height() - Float.intBitsToFloat(1077936128),
              Float.intBitsToFloat(1090519040),
              Float.intBitsToFloat(1090519040),
              Float.intBitsToFloat(1077936128),
              -1647361);
        }
        mjowiohlngpo(
            compositorPushPresentationScaleService,
            placed.x(),
            placed.y() + placed.height() + Float.intBitsToFloat(1092616192),
            placed.shape().name
                + "  ·  "
                + Math.round(placed.shape().width)
                + " × "
                + Math.round(placed.shape().height),
            Float.intBitsToFloat(1092616192),
            -2700056,
            1.0f);
      }
    }
  }

  private void mb1v0ygdva9h(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final StudioValidateValidator.Frame frame,
      final float n) {
    for (final StudioShapeService.Block block : this.items.project.blocks) {
      for (final Map.Entry entry : block.inputs.entrySet()) {
        final StudioShapeService.Block block2 = this.items.project.block((String) entry.getValue());
        final StudioMode.Port port = block.kind.port((String) entry.getKey());
        if (block2 != null) {
          if (port == null) {
            continue;
          }
          BezierConnectionRenderer.draw(
              compositorPushPresentationScaleService,
              this.m63aic1d9gg7(block2.x + Float.intBitsToFloat(1126957056)),
              this.mxl7sd19s3w(block2.y + Float.intBitsToFloat(1103626240)),
              this.m63aic1d9gg7(block.x),
              this.mxl7sd19s3w(
                  block.y + Float.intBitsToFloat(1114374144) + block.kind.ports.indexOf(port) * 25),
              port.type().ink,
              this.value,
              this.effectiveOpacity * Float.intBitsToFloat(1061158912),
              n,
              this.items.playing);
        }
      }
    }
    if (this.text2 != null) {
      final StudioShapeService.Block block3 = this.items.project.block(this.text2);
      if (block3 != null) {
        BezierConnectionRenderer.draw(
            compositorPushPresentationScaleService,
            this.m63aic1d9gg7(block3.x + Float.intBitsToFloat(1126957056)),
            this.mxl7sd19s3w(block3.y + Float.intBitsToFloat(1103626240)),
            this.fcycttpb7dbl,
            this.value13,
            block3.kind.color(),
            this.value,
            this.effectiveOpacity,
            n,
            false);
      }
    } else if (this.text3 != null) {
      final StudioShapeService.Block block4 = this.items.project.block(this.text3);
      if (block4 != null) {
        final StudioMode.Port port2 = block4.kind.port(this.fbg2xmzromoa);
        BezierConnectionRenderer.draw(
            compositorPushPresentationScaleService,
            this.fcycttpb7dbl,
            this.value13,
            this.m63aic1d9gg7(block4.x),
            this.mxl7sd19s3w(
                block4.y
                    + Float.intBitsToFloat(1114374144)
                    + block4.kind.ports.indexOf(port2) * 25),
            port2.type().ink,
            this.value,
            this.effectiveOpacity,
            n,
            false);
      }
    }
    compositorPushPresentationScaleService.nextLayer();
    for (final StudioShapeService.Block block5 : this.items.project.blocks) {
      final float m63aic1d9gg7 = this.m63aic1d9gg7(block5.x);
      final float mxl7sd19s3w = this.mxl7sd19s3w(block5.y);
      final float n2 = Float.intBitsToFloat(1126957056) * this.value;
      final float n3 = blockHeight(block5) * this.value;
      final boolean equals = block5.id.equals(this.items.selected);
      compositorPushPresentationScaleService.studioPrimitive(
          m63aic1d9gg7,
          mxl7sd19s3w,
          n2,
          n3,
          Float.intBitsToFloat(1094713344) * this.value,
          StudioValidateValidator.mix(
              -14538181,
              block5.kind.color(),
              equals ? Float.intBitsToFloat(1040522936) : Float.intBitsToFloat(1027101164)),
          block5.kind.color(),
          this.effectiveOpacity,
          new CompositorPushPresentationScaleService.StudioPaint(
              5, n, this.value, equals ? 1.0f : 0.0f, 0.0f, 0.0f));
      mjowiohlngpo(
          compositorPushPresentationScaleService,
          m63aic1d9gg7 + Float.intBitsToFloat(1095761920) * this.value,
          mxl7sd19s3w + Float.intBitsToFloat(1095761920) * this.value,
          block5.kind.title,
          Float.intBitsToFloat(1094713344),
          block5.kind.color(),
          this.value);
      if (block5.kind.output != null) {
        this.ma9h2kz7b6gs(
            compositorPushPresentationScaleService,
            m63aic1d9gg7 + n2,
            mxl7sd19s3w + Float.intBitsToFloat(1103626240) * this.value,
            block5.kind.output.ink,
            true);
      }
      for (int i = 0; i < block5.kind.ports.size(); ++i) {
        final StudioMode.Port port3 = block5.kind.ports.get(i);
        final float n4 = mxl7sd19s3w + (59 + i * 25) * this.value;
        this.ma9h2kz7b6gs(
            compositorPushPresentationScaleService,
            m63aic1d9gg7,
            n4,
            port3.type().ink,
            block5.inputs.containsKey(port3.name()));
        mjowiohlngpo(
            compositorPushPresentationScaleService,
            m63aic1d9gg7 + Float.intBitsToFloat(1095761920) * this.value,
            n4 - Float.intBitsToFloat(1088421888) * this.value,
            port3.name(),
            Float.intBitsToFloat(1093664768),
            -2893852,
            this.value);
        if (!block5.inputs.containsKey(port3.name())) {
          mjowiohlngpo(
              compositorPushPresentationScaleService,
              m63aic1d9gg7 + n2 - Float.intBitsToFloat(1112801280) * this.value,
              n4 - Float.intBitsToFloat(1088421888) * this.value,
              (port3.type() == StudioMode.Type.NUMBER)
                  ? m7pwp0vygo3k(block5.defaults.getOrDefault(port3.name(), port3.fallback()))
                  : ((port3.type() == StudioMode.Type.BOOLEAN)
                      ? ((block5.defaults.getOrDefault(port3.name(), port3.fallback()) != 0.0f)
                          ? "True"
                          : "False")
                      : "—"),
              Float.intBitsToFloat(1092616192),
              -7759693,
              this.value);
        }
      }
      String s;
      if (block5.kind.shapeOutput()) {
        final StudioShapeService.Shape shape = this.items.project.shape(block5.target);
        s = ((shape == null) ? "Choose a shape" : shape.name);
      } else if (block5.kind == StudioMode.MODULE_GATE) {
        s = (block5.target.isBlank() ? "Choose a module" : block5.target);
      } else {
        final StudioValidateValidator.Value value = frame.values().get(block5.id);
        s = ((value == null) ? "" : value.caption());
      }
      compositorPushPresentationScaleService.roundedRect(
          m63aic1d9gg7 + Float.intBitsToFloat(1092616192) * this.value,
          mxl7sd19s3w + n3 - Float.intBitsToFloat(1106771968) * this.value,
          n2 - Float.intBitsToFloat(1101004800) * this.value,
          Float.intBitsToFloat(1100480512) * this.value,
          Float.intBitsToFloat(1086324736) * this.value,
          1428171324);
      mjowiohlngpo(
          compositorPushPresentationScaleService,
          m63aic1d9gg7 + Float.intBitsToFloat(1098907648) * this.value,
          mxl7sd19s3w + n3 - Float.intBitsToFloat(1105199104) * this.value,
          StudioCanvasRenderer.fit(
              compositorPushPresentationScaleService,
              s,
              Float.intBitsToFloat(1092616192) * this.value,
              n2 - Float.intBitsToFloat(1107296256) * this.value),
          Float.intBitsToFloat(1092616192),
          -2039826,
          this.value);
    }
  }

  private float m63aic1d9gg7(final float n) {
    return this.cx + this.value2 + n * this.value;
  }

  private float mxl7sd19s3w(final float n) {
    return this.cy + this.fdxabdjgdi0y + n * this.value;
  }

  private void ma9h2kz7b6gs(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final float n,
      final float n2,
      final int n3,
      final boolean b) {
    final float n4 = Float.intBitsToFloat(1084227584) * this.value;
    compositorPushPresentationScaleService.roundedRect(
        n - n4,
        n2 - n4,
        n4 * 2.0f,
        n4 * 2.0f,
        n4,
        n4,
        n4,
        n4,
        b ? n3 : -14339770,
        0.0f,
        0.0f,
        0,
        Float.intBitsToFloat(1069547520) * this.value,
        n3,
        this.effectiveOpacity,
        0.0f);
  }

  private static void mjowiohlngpo(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService,
      final float n,
      final float n2,
      final String s,
      final float n3,
      final int n4,
      final float n5) {
    compositorPushPresentationScaleService.text(
        n, n2, s, n3 * n5, n4, MaterialIsLightService.LABEL);
  }

  public static float blockHeight(final StudioShapeService.Block block) {
    return (float) (77 + block.kind.ports.size() * 25);
  }

  private static String m7pwp0vygo3k(final float n) {
    return String.format(
        Locale.ROOT,
        (Math.abs(n - Math.round(n)) < Float.intBitsToFloat(981668463)) ? "%.0f" : "%.2f",
        n);
  }

  private PortHit portHit(final float n, final float n2) {
    for (final StudioShapeService.Block block : this.items.project.blocks.reversed()) {
      if (block.kind.output != null
          && Math.hypot(
                  n - block.x - Float.intBitsToFloat(1126957056),
                  n2 - block.y - Float.intBitsToFloat(1103626240))
              < Float.intBitsToFloat(1093664768) / this.value) {
        return new PortHit(block.id, "", true, block.kind.output);
      }
      for (int i = 0; i < block.kind.ports.size(); ++i) {
        final StudioMode.Port port = block.kind.ports.get(i);
        if (Math.hypot(n - block.x, n2 - block.y - Float.intBitsToFloat(1114374144) - i * 25)
            < Float.intBitsToFloat(1093664768) / this.value) {
          return new PortHit(block.id, port.name(), false, port.type());
        }
      }
    }
    return null;
  }

  @Override
  protected void onPress(final float n, final float n2) {
    this.currentRunnable.run();
    this.enabled5 = false;
    MotionAnimateService.cancel(this, StudioZoomService.fi13ctov9jzz);
    this.fcycttpb7dbl = n;
    this.value6 = n;
    this.value13 = n2;
    this.value7 = n2;
    final boolean fagkplloc0nd = false;
    this.enabled4 = fagkplloc0nd;
    this.fa7pux21lgw = fagkplloc0nd;
    this.fagkplloc0nd = fagkplloc0nd;
    final String text2 = null;
    this.fbg2xmzromoa = text2;
    this.text3 = text2;
    this.text2 = text2;
    final float worldX = this.worldX(n);
    final float worldY = this.worldY(n2);
    if (this.items.logic) {
      final PortHit portHit = this.portHit(worldX, worldY);
      if (portHit != null) {
        if (portHit.output) {
          this.text2 = portHit.block;
        } else {
          this.text3 = portHit.block;
          this.fbg2xmzromoa = portHit.port;
        }
        return;
      }
      for (final StudioShapeService.Block block : this.items.project.blocks.reversed()) {
        if (worldX >= block.x
            && worldX <= block.x + Float.intBitsToFloat(1126957056)
            && worldY >= block.y
            && worldY <= block.y + blockHeight(block)) {
          this.items.select(block.id);
          this.items.beginGesture();
          this.value8 = block.x;
          this.value9 = block.y;
          return;
        }
      }
    } else {
      final List<StudioCanvasRenderer.Placed> layout =
          this.f8pwtav7l4v3.layout(this.items.project, this.supplier.get(), 0.0f, 0.0f, 1.0f, 1.0f);
      for (final StudioCanvasRenderer.Placed placed : layout.reversed()) {
        if (placed.shape().id.equals(this.items.selected)
            && !placed.shape().locked
            && Math.hypot(
                    worldX - placed.x() - placed.width(), worldY - placed.y() - placed.height())
                < Float.intBitsToFloat(1094713344) / this.value) {
          this.fa7pux21lgw = true;
          this.items.beginGesture();
          this.value10 = placed.shape().width;
          this.value11 = placed.shape().height;
          this.fdjrt45rlv2z =
              placed.scale()
                  / placed.appearance().scale
                  * (1.0f + placed.appearance().scale)
                  / 2.0f;
          return;
        }
      }
      for (final StudioCanvasRenderer.Placed placed2 : layout.reversed()) {
        if (placed2.contains(worldX, worldY) && !placed2.shape().locked) {
          this.items.select(placed2.shape().id);
          this.items.beginGesture();
          this.value8 = placed2.shape().x;
          this.value9 = placed2.shape().y;
          this.fdjrt45rlv2z =
              placed2.scale()
                  / Math.max(Float.intBitsToFloat(1028443341), placed2.appearance().scale);
          return;
        }
      }
    }
    this.items.select("");
    this.enabled4 = true;
    this.value8 = this.value2;
    this.value9 = this.fdxabdjgdi0y;
  }

  @Override
  protected void updateWhilePressed(final float fcycttpb7dbl, final float value13) {
    if (this.enabled5) {
      return;
    }
    this.fcycttpb7dbl = fcycttpb7dbl;
    this.value13 = value13;
    final float n = fcycttpb7dbl - this.value6;
    final float n2 = value13 - this.value7;
    if (Math.hypot(n, n2) > Double.longBitsToDouble(4613937818241073152L)) {
      this.fagkplloc0nd = true;
    }
    if (this.text2 != null || this.text3 != null) {
      return;
    }
    if (this.enabled4) {
      this.value2 = this.value8 + n;
      this.fdxabdjgdi0y = this.value9 + n2;
      return;
    }
    if (!this.fagkplloc0nd) {
      return;
    }
    if (this.items.logic) {
      final StudioShapeService.Block block = this.items.block();
      if (block != null) {
        block.x =
            StudioValidateValidator.clamp(
                this.items.grid(this.value8 + n / this.value),
                Float.intBitsToFloat(-969179136),
                Float.intBitsToFloat(1178304512));
        block.y =
            StudioValidateValidator.clamp(
                this.items.grid(this.value9 + n2 / this.value),
                Float.intBitsToFloat(-969179136),
                Float.intBitsToFloat(1178304512));
      }
    } else {
      final StudioShapeService.Shape shape = this.items.shape();
      if (shape == null) {
        return;
      }
      if (this.fa7pux21lgw) {
        shape.width =
            StudioValidateValidator.clamp(
                this.items.grid(this.value10 + n / this.value / this.fdjrt45rlv2z),
                Float.intBitsToFloat(1082130432),
                Float.intBitsToFloat(1157234688));
        shape.height =
            StudioValidateValidator.clamp(
                this.items.grid(this.value11 + n2 / this.value / this.fdjrt45rlv2z),
                Float.intBitsToFloat(1082130432),
                Float.intBitsToFloat(1157234688));
      } else {
        shape.x =
            StudioValidateValidator.clamp(
                this.items.grid(this.value8 + n / this.value / this.fdjrt45rlv2z),
                Float.intBitsToFloat(-981860352),
                Float.intBitsToFloat(1165623296));
        shape.y =
            StudioValidateValidator.clamp(
                this.items.grid(this.value9 + n2 / this.value / this.fdjrt45rlv2z),
                Float.intBitsToFloat(-981860352),
                Float.intBitsToFloat(1165623296));
      }
    }
  }

  @Override
  protected void onRelease(final boolean b) {
    if (this.enabled5) {
      return;
    }
    if (this.text2 != null || this.text3 != null) {
      final PortHit portHit =
          this.portHit(this.worldX(this.fcycttpb7dbl), this.worldY(this.value13));
      if (portHit != null && this.text2 != null && !portHit.output) {
        this.items.connect(this.text2, portHit.block, portHit.port);
      } else if (portHit != null && this.text3 != null && portHit.output) {
        this.items.connect(portHit.block, this.text3, this.fbg2xmzromoa);
      } else {
        this.items.message = "Drag between matching colored ports.";
        this.items.notifyChanged();
      }
      final String text2 = null;
      this.fbg2xmzromoa = text2;
      this.text3 = text2;
      this.text2 = text2;
    } else if (!this.enabled4) {
      this.items.endGesture();
    }
    final boolean b2 = false;
    this.fa7pux21lgw = b2;
    this.enabled4 = b2;
  }

  @Override
  protected void handleClick() {}

  public boolean cancelInteraction() {
    if (this.text2 == null && this.text3 == null && !this.enabled4 && !this.items.gestureActive()) {
      return false;
    }
    this.enabled5 = true;
    final String text2 = null;
    this.fbg2xmzromoa = text2;
    this.text3 = text2;
    this.text2 = text2;
    if (!this.enabled4) {
      this.items.cancelGesture();
    } else {
      this.value2 = this.value8;
      this.fdxabdjgdi0y = this.value9;
    }
    final boolean b = false;
    this.fa7pux21lgw = b;
    this.enabled4 = b;
    return true;
  }

  static {
    fi13ctov9jzz =
        MotionFiniteService.finite(
            "studioZoom", scenePctService -> scenePctService instanceof StudioZoomService);
  }

  record PortHit(String block, String port, boolean output, StudioMode.Type type) {}
}
