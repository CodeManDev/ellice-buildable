package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.svg.SvgQueueService;
import java.util.ArrayList;
import java.util.List;

public final class SceneModelNode extends ScenePctService<SceneModelNode> {
  private static final List<Face> items = SceneModelNode.collectValues2();
  private static final List<Face> ffxgt4pqnhwk = SceneModelNode.m4vcwccvpovh();
  private boolean enabled;

  public SceneModelNode multiplayer(boolean bl) {
    this.enabled = bl;
    return this;
  }

  @Override
  protected void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    float f =
        Math.min(
            this.cw / Float.intBitsToFloat(1134559232), this.ch / Float.intBitsToFloat(1132068864));
    float f2 =
        this.cx
            + (this.cw - Float.intBitsToFloat(1134559232) * f) * Float.intBitsToFloat(0x3F000000);
    float f3 =
        this.cy
            + (this.ch - Float.intBitsToFloat(1132068864) * f) * Float.intBitsToFloat(0x3F000000);
    for (int i = 0; i < 3; ++i) {
      compositorPushPresentationScaleService.roundedRect(
          f2 + (float) (50 + i * 17) * f,
          f3 + (float) (224 + i * 2) * f,
          (float) (220 - i * 34) * f,
          (float) (10 - i * 2) * f,
          5 - i,
          SceneModelNode.mulAlpha(269226007, this.effectiveOpacity));
    }
    SvgQueueService svgQueueService = compositorPushPresentationScaleService.vectorRenderer();
    if (svgQueueService == null) {
      return;
    }
    for (Face face : this.enabled ? items : ffxgt4pqnhwk) {
      float f4 =
          (float) (face.color >>> 24) / Float.intBitsToFloat(1132396544) * this.effectiveOpacity;
      svgQueueService.queue(
          new SvgQueueService.VectorCmd(
              face.vertices,
              face.vertices.length / 3,
              f2,
              f3,
              f,
              f,
              0.0f,
              0.0f,
              0.0f,
              (float) (face.color >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544) * f4,
              (float) (face.color >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544) * f4,
              (float) (face.color & 0xFF) / Float.intBitsToFloat(1132396544) * f4,
              f4,
              0.0f,
              0.0f,
              0.0f,
              compositorPushPresentationScaleService.currentClip()));
    }
    compositorPushPresentationScaleService.nextLayer();
  }

  private static List<Face> m4vcwccvpovh() {
    int n;
    Model model = new Model();
    SceneModelNode.updateState(model, false);
    model.box(
        Float.intBitsToFloat(-1039663104),
        Float.intBitsToFloat(-1039925248),
        0.0f,
        Float.intBitsToFloat(1103101952),
        Float.intBitsToFloat(1104674816),
        Float.intBitsToFloat(0x41000000),
        -7228261);
    model.box(
        Float.intBitsToFloat(-1040187392),
        Float.intBitsToFloat(-1040711680),
        Float.intBitsToFloat(0x41000000),
        Float.intBitsToFloat(1101004800),
        Float.intBitsToFloat(1102577664),
        2.0f,
        -4338016);
    SceneModelNode.updateState4(
        model,
        Float.intBitsToFloat(-1044905984),
        Float.intBitsToFloat(-1044905984),
        Float.intBitsToFloat(1092616192),
        Float.intBitsToFloat(1105723392));
    SceneModelNode.updateState4(
        model,
        Float.intBitsToFloat(-1039400960),
        Float.intBitsToFloat(-1059061760),
        0.0f,
        Float.intBitsToFloat(1103626240));
    SceneModelNode.updateState2(
        model, Float.intBitsToFloat(0x40800000), Float.intBitsToFloat(-1051721728), 0.0f, false);
    for (n = 0; n < 5; ++n) {
      model.box(
          Float.intBitsToFloat(1092616192) + (float) n * Float.intBitsToFloat(1067030938),
          17 + n * 4,
          Float.intBitsToFloat(0x3F000000),
          Float.intBitsToFloat(0x40C00000),
          Float.intBitsToFloat(1076677837),
          Float.intBitsToFloat(1061997773),
          -1845832);
    }
    SceneModelNode.updateState5(
        model, Float.intBitsToFloat(-1044381696), Float.intBitsToFloat(1092616192), 0.0f);
    model.box(
        Float.intBitsToFloat(-1065353216),
        Float.intBitsToFloat(1107820544),
        Float.intBitsToFloat(0x3F333333),
        Float.intBitsToFloat(1102053376),
        Float.intBitsToFloat(0x40400000),
        Float.intBitsToFloat(1070386381),
        -4351620);
    for (n = 0; n < 5; ++n) {
      model.box(
          Float.intBitsToFloat(-1069547520) + (float) n * Float.intBitsToFloat(1082340147),
          Float.intBitsToFloat(1105199104),
          Float.intBitsToFloat(0x3F666666),
          Float.intBitsToFloat(1079613850),
          Float.intBitsToFloat(1095761920),
          Float.intBitsToFloat(1067869798),
          -2048880);
    }
    model.box(
        Float.intBitsToFloat(-1063256064),
        Float.intBitsToFloat(1105199104),
        1.0f,
        Float.intBitsToFloat(1067869798),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(0x40A00000),
        -3430271);
    model.box(
        Float.intBitsToFloat(1099956224),
        Float.intBitsToFloat(1105199104),
        1.0f,
        Float.intBitsToFloat(1067869798),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(0x40A00000),
        -3430271);
    model.box(
        Float.intBitsToFloat(-1056964608),
        Float.intBitsToFloat(1110966272),
        Float.intBitsToFloat(-1047527424),
        Float.intBitsToFloat(1096810496),
        1.0f,
        Float.intBitsToFloat(1100113510),
        -8995380);
    for (n = 0; n < 4; ++n) {
      model.box(
          Float.intBitsToFloat(-1059061760) + (float) n * Float.intBitsToFloat(0x40666666),
          Float.intBitsToFloat(1111254630),
          -17 + n % 2 * 3,
          Float.intBitsToFloat(1059481190),
          Float.intBitsToFloat(1039516303),
          16 - n % 2 * 3,
          -1075776795);
    }
    model.box(
        Float.intBitsToFloat(-1055916032),
        Float.intBitsToFloat(1110966272),
        Float.intBitsToFloat(1045220557),
        Float.intBitsToFloat(1098907648),
        Float.intBitsToFloat(1067869798),
        Float.intBitsToFloat(0x3F000000),
        -2035491);
    SceneModelNode.updateState7(
        model, Float.intBitsToFloat(1104674816), Float.intBitsToFloat(1103101952), -1457268);
    SceneModelNode.updateState7(
        model, Float.intBitsToFloat(-1052770304), Float.intBitsToFloat(-1051721728), -996139);
    SceneModelNode.updateState8(
        model,
        Float.intBitsToFloat(1107820544),
        Float.intBitsToFloat(1098907648),
        Float.intBitsToFloat(0x40800000));
    SceneModelNode.updateState8(
        model,
        Float.intBitsToFloat(-1041235968),
        Float.intBitsToFloat(1108344832),
        Float.intBitsToFloat(0x40400000));
    return model.finish();
  }

  private static List<Face> collectValues2() {
    Model model = new Model();
    SceneModelNode.updateState(model, true);
    model.box(
        Float.intBitsToFloat(-1047527424),
        Float.intBitsToFloat(-1056964608),
        Float.intBitsToFloat(0x3DCCCCCD),
        Float.intBitsToFloat(1109131264),
        Float.intBitsToFloat(0x42200000),
        Float.intBitsToFloat(0x3F000000),
        -3753540);
    for (int i = 0; i < 7; ++i) {
      model.box(
          Float.intBitsToFloat(-1048051712),
          Float.intBitsToFloat(-1059061760) + (float) i * Float.intBitsToFloat(1085485875),
          Float.intBitsToFloat(1059481190),
          Float.intBitsToFloat(1108344832),
          Float.intBitsToFloat(0x3EB33333),
          Float.intBitsToFloat(0x3DCCCCCD),
          -5595480);
      model.box(
          Float.intBitsToFloat(-1048051712) + (float) i * Float.intBitsToFloat(1085485875),
          Float.intBitsToFloat(-1059061760),
          Float.intBitsToFloat(1059648963),
          Float.intBitsToFloat(0x3EB33333),
          Float.intBitsToFloat(1108606976),
          Float.intBitsToFloat(0x3DCCCCCD),
          -5595480);
    }
    SceneModelNode.updateState2(
        model, Float.intBitsToFloat(-1040187392), Float.intBitsToFloat(-1040711680), 0.0f, true);
    SceneModelNode.updateState2(
        model, Float.intBitsToFloat(1097859072), Float.intBitsToFloat(-1044381696), 0.0f, false);
    model.box(
        Float.intBitsToFloat(-1065353216),
        Float.intBitsToFloat(-1039925248),
        0.0f,
        Float.intBitsToFloat(1096810496),
        Float.intBitsToFloat(1096810496),
        Float.intBitsToFloat(1108869120),
        -3356209);
    model.box(
        Float.intBitsToFloat(-1062207488),
        Float.intBitsToFloat(-1039532032),
        Float.intBitsToFloat(1108606976),
        Float.intBitsToFloat(1099431936),
        Float.intBitsToFloat(1099431936),
        2.0f,
        -1450781);
    model.roof(
        Float.intBitsToFloat(-1059061760),
        Float.intBitsToFloat(-1039138816),
        Float.intBitsToFloat(1109131264),
        Float.intBitsToFloat(1101004800),
        Float.intBitsToFloat(1101004800),
        Float.intBitsToFloat(1097859072),
        -7029586);
    model.box(
        Float.intBitsToFloat(0x40200000),
        Float.intBitsToFloat(-1042284544),
        Float.intBitsToFloat(1113063424),
        Float.intBitsToFloat(0x3F666666),
        Float.intBitsToFloat(0x3F666666),
        Float.intBitsToFloat(1093664768),
        -2569025);
    model.face(
        -1261361,
        SceneModelNode.createPoint(
            Float.intBitsToFloat(1079613850),
            Float.intBitsToFloat(1115815936),
            Float.intBitsToFloat(-1042284544)),
        SceneModelNode.createPoint(
            Float.intBitsToFloat(0x41400000),
            Float.intBitsToFloat(1115291648),
            Float.intBitsToFloat(-1042284544)),
        SceneModelNode.createPoint(
            Float.intBitsToFloat(1079613850),
            Float.intBitsToFloat(1114636288),
            Float.intBitsToFloat(-1042284544)));
    model.box(
        Float.intBitsToFloat(1092668621),
        Float.intBitsToFloat(-1041760256),
        Float.intBitsToFloat(1103101952),
        Float.intBitsToFloat(1041865114),
        Float.intBitsToFloat(0x40A00000),
        Float.intBitsToFloat(0x41000000),
        -8950135);
    model.box(
        Float.intBitsToFloat(1092878336),
        Float.intBitsToFloat(-1042022400),
        Float.intBitsToFloat(1103626240),
        Float.intBitsToFloat(1041865114),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40C00000),
        -927835);
    model.box(
        Float.intBitsToFloat(-1056964608),
        Float.intBitsToFloat(0x40A00000),
        Float.intBitsToFloat(0x3F333333),
        Float.intBitsToFloat(1099431936),
        Float.intBitsToFloat(1099431936),
        Float.intBitsToFloat(0x40400000),
        -2436388);
    model.box(
        Float.intBitsToFloat(-1061158912),
        Float.intBitsToFloat(0x40E00000),
        Float.intBitsToFloat(0x40700000),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(0x3EE66666),
        -7618612);
    model.box(
        Float.intBitsToFloat(-1076258406),
        Float.intBitsToFloat(1093979341),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x41000000),
        -3945003);
    model.box(
        Float.intBitsToFloat(-1065353216),
        Float.intBitsToFloat(0x41100000),
        Float.intBitsToFloat(1092616192),
        Float.intBitsToFloat(0x41100000),
        Float.intBitsToFloat(0x41100000),
        Float.intBitsToFloat(1067030938),
        -5056811);
    model.box(
        Float.intBitsToFloat(-1070386381),
        Float.intBitsToFloat(1092825907),
        Float.intBitsToFloat(1093979341),
        Float.intBitsToFloat(1087583027),
        Float.intBitsToFloat(1087583027),
        Float.intBitsToFloat(1050253722),
        -3019803);
    SceneModelNode.updateState5(
        model, Float.intBitsToFloat(-1041235968), Float.intBitsToFloat(1096810496), 0.0f);
    SceneModelNode.updateState4(
        model,
        Float.intBitsToFloat(1106771968),
        Float.intBitsToFloat(1102577664),
        0.0f,
        Float.intBitsToFloat(1106771968));
    SceneModelNode.updateState6(
        model, Float.intBitsToFloat(-1051721728), Float.intBitsToFloat(1105723392));
    SceneModelNode.updateState6(
        model, Float.intBitsToFloat(1099431936), Float.intBitsToFloat(0x40A00000));
    model.box(
        Float.intBitsToFloat(1092616192),
        Float.intBitsToFloat(1102577664),
        Float.intBitsToFloat(0x40400000),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(1067869798),
        -2707816);
    model.box(
        Float.intBitsToFloat(1092616192),
        Float.intBitsToFloat(1104412672),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(1095761920),
        1.0f,
        Float.intBitsToFloat(0x40800000),
        -1787486);
    model.box(
        Float.intBitsToFloat(1093664768),
        Float.intBitsToFloat(1102734950),
        Float.intBitsToFloat(0x3F333333),
        1.0f,
        Float.intBitsToFloat(0x40400000),
        Float.intBitsToFloat(0x40400000),
        -8620150);
    model.box(
        Float.intBitsToFloat(1101529088),
        Float.intBitsToFloat(1102734950),
        Float.intBitsToFloat(0x3F333333),
        1.0f,
        Float.intBitsToFloat(0x40400000),
        Float.intBitsToFloat(0x40400000),
        -8620150);
    SceneModelNode.updateState7(
        model, Float.intBitsToFloat(1103626240), Float.intBitsToFloat(0x40800000), -998188);
    SceneModelNode.updateState7(
        model, Float.intBitsToFloat(-1048051712), Float.intBitsToFloat(1107820544), -1715564);
    return model.finish();
  }

  private static void updateState(Model model, boolean bl) {
    for (int i = -4; i < 4; ++i) {
      for (int j = -4; j < 4; ++j) {
        if (Math.abs((float) i + Float.intBitsToFloat(0x3F000000))
                + Math.abs((float) j + Float.intBitsToFloat(0x3F000000))
            > Float.intBitsToFloat(0x40C00000)) continue;
        float f = i * 12;
        float f2 = j * 12;
        int n = Math.floorMod(i * 17 + j * 31, 5);
        float f3 = Float.intBitsToFloat(-1048051712) - (float) n * Float.intBitsToFloat(1070386381);
        int n2 = !bl && j >= 2 && i >= -2 && i <= 1 ? 1 : 0;
        model.box(
            f,
            f2,
            f3,
            Float.intBitsToFloat(0x41400000),
            Float.intBitsToFloat(0x41400000),
            -f3 - Float.intBitsToFloat(0x40800000),
            SceneModelNode.mgbkgmmnl6hd(
                -7832445,
                Float.intBitsToFloat(1064178811) + (float) n * Float.intBitsToFloat(0x3CCCCCCD)));
        model.box(
            f,
            f2,
            Float.intBitsToFloat(-1065353216),
            Float.intBitsToFloat(0x41400000),
            Float.intBitsToFloat(0x41400000),
            Float.intBitsToFloat(0x40400000),
            bl ? -5532004 : -5400189);
        model.box(
            f,
            f2,
            Float.intBitsToFloat(-1082130432),
            Float.intBitsToFloat(0x41400000),
            Float.intBitsToFloat(0x41400000),
            n2 != 0 ? Float.intBitsToFloat(1059481190) : 1.0f,
            n2 != 0
                ? SceneModelNode.mgbkgmmnl6hd(
                    -8143670,
                    Float.intBitsToFloat(0x3F733333) + (float) n * Float.intBitsToFloat(1016296636))
                : SceneModelNode.mgbkgmmnl6hd(
                    bl ? -4667987 : -5125214,
                    Float.intBitsToFloat(1064682127)
                        + (float) n * Float.intBitsToFloat(1012202996)));
        if (n2 != 0 && n % 2 == 0) {
          model.box(
              f + 2.0f,
              f2 + Float.intBitsToFloat(0x40800000),
              Float.intBitsToFloat(-1097901015),
              Float.intBitsToFloat(0x40C00000),
              Float.intBitsToFloat(1053609165),
              Float.intBitsToFloat(1034147594),
              -3217185);
        }
        if (i != 3 && j != 3 || n <= 1) continue;
        model.box(
            f + Float.intBitsToFloat(0x40400000),
            f2 + Float.intBitsToFloat(0x40400000),
            f3 - Float.intBitsToFloat(0x40400000),
            Float.intBitsToFloat(0x40C00000),
            Float.intBitsToFloat(0x40C00000),
            Float.intBitsToFloat(0x40400000),
            -9081218);
      }
    }
  }

  private static void updateState2(Model model, float f, float f2, float f3, boolean bl) {
    float f4 = bl ? Float.intBitsToFloat(1102577664) : Float.intBitsToFloat(1104674816);
    float f5 = bl ? Float.intBitsToFloat(1100480512) : Float.intBitsToFloat(1102577664);
    float f6 = bl ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1102577664);
    model.box(f - 1.0f, f2 - 1.0f, f3, f4 + 2.0f, f5 + 2.0f, 2.0f, -5856864);
    model.box(f, f2, f3 + 2.0f, f4, f5, f6, bl ? -1980220 : -1714761);
    for (float f7 : new float[] {0.0f, f4 - Float.intBitsToFloat(1069547520)}) {
      model.box(
          f + f7,
          f2 + f5 - 1.0f,
          f3 + 2.0f,
          Float.intBitsToFloat(1069547520),
          Float.intBitsToFloat(1067030938),
          f6,
          -6452865);
    }
    model.box(
        f + f4 * Float.intBitsToFloat(1054280253),
        f2 + f5 + Float.intBitsToFloat(1039516303),
        f3 + 2.0f,
        Float.intBitsToFloat(0x40B00000),
        Float.intBitsToFloat(1050253722),
        Float.intBitsToFloat(0x41400000),
        -7636615);
    model.box(
        f + f4 * Float.intBitsToFloat(1054280253) + Float.intBitsToFloat(0x40800000),
        f2 + f5 + Float.intBitsToFloat(0x3EE66666),
        f3 + Float.intBitsToFloat(0x40E00000),
        Float.intBitsToFloat(1059481190),
        Float.intBitsToFloat(1048576000),
        Float.intBitsToFloat(1059481190),
        -863330);
    SceneModelNode.updateState3(
        model,
        f + Float.intBitsToFloat(0x40400000),
        f2 + f5 + Float.intBitsToFloat(1041865114),
        f3 + Float.intBitsToFloat(1093664768),
        Float.intBitsToFloat(0x40A00000),
        Float.intBitsToFloat(0x40C00000));
    model.box(
        f + f4 + Float.intBitsToFloat(0x3DCCCCCD),
        f2 + Float.intBitsToFloat(0x40C00000),
        f3 + Float.intBitsToFloat(1093664768),
        Float.intBitsToFloat(1045220557),
        Float.intBitsToFloat(0x41000000),
        Float.intBitsToFloat(0x40E00000),
        -7176326);
    model.box(
        f + f4 + Float.intBitsToFloat(1050253722),
        f2 + Float.intBitsToFloat(1087792742),
        f3 + Float.intBitsToFloat(1094398771),
        Float.intBitsToFloat(1041865114),
        Float.intBitsToFloat(1087583027),
        Float.intBitsToFloat(1085485875),
        -730976);
    model.box(
        f + f4 + Float.intBitsToFloat(0x3F000000),
        f2 + Float.intBitsToFloat(1092249190),
        f3 + Float.intBitsToFloat(1094398771),
        Float.intBitsToFloat(1041865114),
        Float.intBitsToFloat(0x3F333333),
        Float.intBitsToFloat(1085485875),
        -5203321);
    model.roof(
        f - Float.intBitsToFloat(0x40400000),
        f2 - Float.intBitsToFloat(0x40400000),
        f3 + f6 + 2.0f,
        f4 + Float.intBitsToFloat(0x40C00000),
        f5 + Float.intBitsToFloat(0x40C00000),
        Float.intBitsToFloat(1095761920),
        bl ? -3566164 : -7624293);
    for (int i = 1; i < 6; ++i) {
      float f8 =
          f
              - Float.intBitsToFloat(0x40400000)
              + (f4 + Float.intBitsToFloat(0x40C00000))
                  * (float) i
                  / Float.intBitsToFloat(0x40C00000);
      model.face(
          898933916,
          SceneModelNode.createPoint(
              f8,
              f3 + f6 + Float.intBitsToFloat(1074496799),
              f2 + f5 + Float.intBitsToFloat(0x40400000)),
          SceneModelNode.createPoint(
              f8 + Float.intBitsToFloat(0x3EE66666),
              f3 + f6 + Float.intBitsToFloat(1074496799),
              f2 + f5 + Float.intBitsToFloat(0x40400000)),
          SceneModelNode.createPoint(
              f8 + Float.intBitsToFloat(0x3EE66666),
              f3 + f6 + Float.intBitsToFloat(1098047816),
              f2 + f5 / 2.0f),
          SceneModelNode.createPoint(
              f8, f3 + f6 + Float.intBitsToFloat(1098047816), f2 + f5 / 2.0f));
    }
    model.box(
        f + Float.intBitsToFloat(0x40800000),
        f2 + 2.0f,
        f3 + f6 + Float.intBitsToFloat(0x40E00000),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(1092616192),
        -4872799);
    model.box(
        f + Float.intBitsToFloat(1079613850),
        f2 + Float.intBitsToFloat(0x3FB33333),
        f3 + f6 + Float.intBitsToFloat(1099431936),
        Float.intBitsToFloat(1084647014),
        Float.intBitsToFloat(1084647014),
        Float.intBitsToFloat(1067030938),
        -2767174);
    model.box(
        f + Float.intBitsToFloat(1082340147),
        f2 + Float.intBitsToFloat(0x40066666),
        f3 + f6 + Float.intBitsToFloat(1100113510),
        Float.intBitsToFloat(1081291571),
        Float.intBitsToFloat(1081291571),
        Float.intBitsToFloat(1041865114),
        -9408903);
  }

  private static void updateState3(Model model, float f, float f2, float f3, float f4, float f5) {
    model.box(f, f2, f3, f4, Float.intBitsToFloat(1048576000), f5, -6651524);
    model.box(
        f + Float.intBitsToFloat(1058642330),
        f2 + Float.intBitsToFloat(1050253722),
        f3 + Float.intBitsToFloat(1058642330),
        f4 - Float.intBitsToFloat(1067030938),
        Float.intBitsToFloat(1041865114),
        f5 - Float.intBitsToFloat(1067030938),
        -665951);
    model.box(
        f + f4 / 2.0f - Float.intBitsToFloat(1048576000),
        f2 + Float.intBitsToFloat(0x3F000000),
        f3 + Float.intBitsToFloat(1058642330),
        Float.intBitsToFloat(0x3F000000),
        Float.intBitsToFloat(0x3DCCCCCD),
        f5 - Float.intBitsToFloat(1067030938),
        -4350331);
    model.box(
        f - Float.intBitsToFloat(0x3F333333),
        f2 - Float.intBitsToFloat(1041865114),
        f3 - Float.intBitsToFloat(1058642330),
        f4 + Float.intBitsToFloat(0x3FB33333),
        Float.intBitsToFloat(1069547520),
        Float.intBitsToFloat(1061997773),
        -2569810);
  }

  private static void updateState4(Model model, float f, float f2, float f3, float f4) {
    model.box(
        f - Float.intBitsToFloat(1067869798),
        f2 - Float.intBitsToFloat(1067869798),
        f3,
        Float.intBitsToFloat(1076258406),
        Float.intBitsToFloat(1076258406),
        f4 * Float.intBitsToFloat(1059481190),
        -5402246);
    for (int i = 0; i < 3; ++i) {
      float f5 =
          f4 * (Float.intBitsToFloat(1058810102) - (float) i * Float.intBitsToFloat(1040522936));
      model.pyramid(
          f - f5 / 2.0f,
          f2 - f5 / 2.0f,
          f3 + Float.intBitsToFloat(0x41000000) + (float) i * f4 * Float.intBitsToFloat(1045220557),
          f5,
          f4 * Float.intBitsToFloat(1056629064),
          i == 2 ? -5125726 : (i == 1 ? -7358051 : -9265264));
    }
  }

  private static void updateState5(Model model, float f, float f2, float f3) {
    model.box(
        f - Float.intBitsToFloat(1072064102),
        f2 - Float.intBitsToFloat(1072064102),
        f3,
        Float.intBitsToFloat(0x40666666),
        Float.intBitsToFloat(0x40666666),
        Float.intBitsToFloat(1103101952),
        -5204341);
    model.box(
        f - Float.intBitsToFloat(0x40E00000),
        f2 - 1.0f,
        f3 + Float.intBitsToFloat(1099956224),
        Float.intBitsToFloat(0x41000000),
        Float.intBitsToFloat(0x40200000),
        Float.intBitsToFloat(0x40200000),
        -4810097);
    model.box(
        f - Float.intBitsToFloat(1096810496),
        f2 - Float.intBitsToFloat(1092616192),
        f3 + Float.intBitsToFloat(1102053376),
        Float.intBitsToFloat(1104150528),
        Float.intBitsToFloat(1101529088),
        Float.intBitsToFloat(0x41000000),
        -2512195);
    model.box(
        f - Float.intBitsToFloat(1092616192),
        f2 - Float.intBitsToFloat(0x41400000),
        f3 + Float.intBitsToFloat(1106247680),
        Float.intBitsToFloat(1102053376),
        Float.intBitsToFloat(1101004800),
        Float.intBitsToFloat(0x40E00000),
        -1458741);
    model.box(
        f - Float.intBitsToFloat(0x40A00000),
        f2 - Float.intBitsToFloat(0x40E00000),
        f3 + Float.intBitsToFloat(1108606976),
        Float.intBitsToFloat(1095761920),
        Float.intBitsToFloat(0x41400000),
        Float.intBitsToFloat(0x40800000),
        -996141);
    model.box(
        f - Float.intBitsToFloat(1097859072),
        f2 - Float.intBitsToFloat(0x40400000),
        f3 + Float.intBitsToFloat(1105199104),
        Float.intBitsToFloat(0x40E00000),
        Float.intBitsToFloat(0x41400000),
        Float.intBitsToFloat(0x40A00000),
        -1261106);
    for (int i = 0; i < 5; ++i) {
      model.box(
          f - Float.intBitsToFloat(0x41000000) + (float) (i * 4),
          f2 + Float.intBitsToFloat(0x41000000) + (float) (i % 2 * 4),
          Float.intBitsToFloat(1045220557),
          Float.intBitsToFloat(1069547520),
          1.0f,
          Float.intBitsToFloat(1041865114),
          -1392175);
    }
  }

  private static void updateState6(Model model, float f, float f2) {
    model.box(
        f - Float.intBitsToFloat(1069547520),
        f2 - Float.intBitsToFloat(1069547520),
        Float.intBitsToFloat(0x3F333333),
        Float.intBitsToFloat(0x40400000),
        Float.intBitsToFloat(0x40400000),
        1.0f,
        -7765359);
    model.box(
        f - Float.intBitsToFloat(1058642330),
        f2 - Float.intBitsToFloat(1058642330),
        Float.intBitsToFloat(1071225242),
        Float.intBitsToFloat(1067030938),
        Float.intBitsToFloat(1067030938),
        Float.intBitsToFloat(1095761920),
        -7569006);
    model.box(
        f - 2.0f,
        f2 - 2.0f,
        Float.intBitsToFloat(1096810496),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40800000),
        Float.intBitsToFloat(0x40800000),
        -994656);
    model.box(
        f - Float.intBitsToFloat(0x40200000),
        f2 - Float.intBitsToFloat(0x40200000),
        Float.intBitsToFloat(1099956224),
        Float.intBitsToFloat(0x40A00000),
        Float.intBitsToFloat(0x40A00000),
        1.0f,
        -6319707);
  }

  private static void updateState7(Model model, float f, float f2, int n) {
    model.box(
        f - 1.0f,
        f2 - 1.0f,
        Float.intBitsToFloat(1050253722),
        Float.intBitsToFloat(0x41000000),
        Float.intBitsToFloat(0x40C00000),
        Float.intBitsToFloat(1069547520),
        -7689592);
    for (int i = 0; i < 4; ++i) {
      float f3 = f + (float) (i % 2 * 4);
      float f4 = f2 + (float) (i / 2 * 3);
      model.box(
          f3,
          f4,
          Float.intBitsToFloat(1072064102),
          Float.intBitsToFloat(1058642330),
          Float.intBitsToFloat(1058642330),
          Float.intBitsToFloat(0x40333333),
          -8873089);
      model.box(
          f3 - Float.intBitsToFloat(0x3F333333),
          f4 - Float.intBitsToFloat(0x3F333333),
          Float.intBitsToFloat(0x40800000),
          2.0f,
          2.0f,
          1.0f,
          n);
    }
  }

  private static void updateState8(Model model, float f, float f2, float f3) {
    model.box(
        f,
        f2,
        Float.intBitsToFloat(0x3DCCCCCD),
        f3 * Float.intBitsToFloat(1069547520),
        f3,
        f3,
        -5065041);
    model.box(
        f + 1.0f,
        f2 + Float.intBitsToFloat(1058642330),
        f3,
        f3,
        f3 * Float.intBitsToFloat(1059481190),
        1.0f,
        -3354433);
  }

  private static Point createPoint(float f, float f2, float f3) {
    return new Point(f, f2, f3);
  }

  private static int mgbkgmmnl6hd(int n, float f) {
    return n & 0xFF000000
        | Math.min(255, Math.round((float) (n >> 16 & 0xFF) * f)) << 16
        | Math.min(255, Math.round((float) (n >> 8 & 0xFF) * f)) << 8
        | Math.min(255, Math.round((float) (n & 0xFF) * f));
  }

  private record Face(float[] vertices, int color, float depth) {}

  private static final class Model {
    private final List<Polygon> items3 = new ArrayList<Polygon>();

    private Model() {}

    void box(float f, float f2, float f3, float f4, float f5, float f6, int n) {
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1061494456)),
          SceneModelNode.createPoint(f + f4, f3, f2),
          SceneModelNode.createPoint(f + f4, f3, f2 + f5),
          SceneModelNode.createPoint(f + f4, f3 + f6, f2 + f5),
          SceneModelNode.createPoint(f + f4, f3 + f6, f2));
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1063507722)),
          SceneModelNode.createPoint(f, f3, f2 + f5),
          SceneModelNode.createPoint(f + f4, f3, f2 + f5),
          SceneModelNode.createPoint(f + f4, f3 + f6, f2 + f5),
          SceneModelNode.createPoint(f, f3 + f6, f2 + f5));
      this.face(
          n,
          SceneModelNode.createPoint(f, f3 + f6, f2),
          SceneModelNode.createPoint(f + f4, f3 + f6, f2),
          SceneModelNode.createPoint(f + f4, f3 + f6, f2 + f5),
          SceneModelNode.createPoint(f, f3 + f6, f2 + f5));
    }

    void roof(float f, float f2, float f3, float f4, float f5, float f6, int n) {
      Point point = SceneModelNode.createPoint(f, f3, f2);
      Point point2 = SceneModelNode.createPoint(f + f4, f3, f2);
      Point point3 = SceneModelNode.createPoint(f + f4, f3, f2 + f5);
      Point point4 = SceneModelNode.createPoint(f, f3, f2 + f5);
      Point point5 = SceneModelNode.createPoint(f, f3 + f6, f2 + f5 / 2.0f);
      Point point6 = SceneModelNode.createPoint(f + f4, f3 + f6, f2 + f5 / 2.0f);
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1062165545)), point2, point3, point6);
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1064346583)),
          point,
          point2,
          point6,
          point5);
      this.face(n, point4, point3, point6, point5);
    }

    void pyramid(float f, float f2, float f3, float f4, float f5, int n) {
      Point point = SceneModelNode.createPoint(f + f4 / 2.0f, f3 + f5, f2 + f4 / 2.0f);
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1064178811)),
          SceneModelNode.createPoint(f, f3, f2),
          SceneModelNode.createPoint(f + f4, f3, f2),
          point);
      this.face(
          SceneModelNode.mgbkgmmnl6hd(n, Float.intBitsToFloat(1061326684)),
          SceneModelNode.createPoint(f + f4, f3, f2),
          SceneModelNode.createPoint(f + f4, f3, f2 + f4),
          point);
      this.face(
          n,
          SceneModelNode.createPoint(f, f3, f2 + f4),
          SceneModelNode.createPoint(f + f4, f3, f2 + f4),
          point);
    }

    void face(int n, Point... pointArray) {
      this.items3.add(new Polygon(pointArray, n));
    }

    private static Face md3h4lxa90wm(Polygon polygon) {
      Point[] pointArray = polygon.points;
      float[] fArray = new float[(pointArray.length - 2) * 9];
      int n = 0;
      for (int i = 1; i < pointArray.length - 1; ++i) {
        for (Point point : new Point[] {pointArray[0], pointArray[i], pointArray[i + 1]}) {
          fArray[n++] =
              Float.intBitsToFloat(1126170624)
                  + (point.x - point.z) * Float.intBitsToFloat(1068624773);
          fArray[n++] =
              Float.intBitsToFloat(1125515264)
                  + (point.x + point.z) * Float.intBitsToFloat(1059313418)
                  - point.y * Float.intBitsToFloat(1068540887);
          fArray[n++] = 0.0f;
        }
      }
      float f = 0.0f;
      for (Point point : pointArray) {
        f += point.x + point.z + point.y * Float.intBitsToFloat(1064178811);
      }
      return new Face(fArray, polygon.color, f / (float) pointArray.length);
    }

    List<Face> finish() {
      ArrayList<Polygon> arrayList = new ArrayList<Polygon>();
      new Bsp(this.items3).append(arrayList);
      return arrayList.stream().map(Model::md3h4lxa90wm).toList();
    }
  }

  private record Point(float x, float y, float z) {}

  private static final class Bsp {
    private final List<Polygon> f19hblh0lx3g = new ArrayList<Polygon>();
    private final Plane fc1mbzcrvgf4;
    private final Bsp fee6n8j6mt3y;
    private final Bsp fc0z89k5udf6;

    Bsp(List<Polygon> list) {
      Plane object;
      Plane object2 = null;
      int n = Integer.MAX_VALUE;
      for (int i = 0; i < list.size(); i += Math.max(1, list.size() / 12)) {
        object = Plane.of(list.get(i));
        if (object == null) continue;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (Polygon polygon : list) {
          int n5 = ((Plane) object).side(polygon);
          if (n5 == 1) {
            ++n2;
            continue;
          }
          if (n5 == 2) {
            ++n3;
            continue;
          }
          if (n5 != 3) continue;
          ++n4;
        }
        int n6 = n4 * 8 + Math.abs(n2 - n3);
        if (n6 >= n) continue;
        n = n6;
        object2 = object;
      }
      this.fc1mbzcrvgf4 = object2;
      if (this.fc1mbzcrvgf4 == null) {
        this.f19hblh0lx3g.addAll(list);
        this.fc0z89k5udf6 = null;
        this.fee6n8j6mt3y = null;
        return;
      }
      ArrayList<Polygon> arrayList = new ArrayList<Polygon>();
      ArrayList<Polygon> backPolygons = new ArrayList<>();
      block7:
      for (Polygon polygon : list) {
        switch (this.fc1mbzcrvgf4.side(polygon)) {
          case 0:
            {
              this.f19hblh0lx3g.add(polygon);
              continue block7;
            }
          case 1:
            {
              arrayList.add(polygon);
              continue block7;
            }
          case 2:
            {
              backPolygons.add(polygon);
              continue block7;
            }
        }
        Bsp.Plane(polygon, this.fc1mbzcrvgf4, arrayList, backPolygons);
      }
      this.fee6n8j6mt3y = arrayList.isEmpty() ? null : new Bsp(arrayList);
      this.fc0z89k5udf6 = backPolygons.isEmpty() ? null : new Bsp(backPolygons);
    }

    void append(List<Polygon> list) {
      Bsp bsp;
      int n =
          this.fc1mbzcrvgf4 == null
                  || this.fc1mbzcrvgf4.distance(
                          SceneModelNode.createPoint(
                              Float.intBitsToFloat(1176256512),
                              Float.intBitsToFloat(1175514112),
                              Float.intBitsToFloat(1176256512)))
                      > 0.0f
              ? 1
              : 0;
      Bsp bsp2 = n != 0 ? this.fc0z89k5udf6 : this.fee6n8j6mt3y;
      Bsp bsp3 = bsp = n != 0 ? this.fee6n8j6mt3y : this.fc0z89k5udf6;
      if (bsp2 != null) {
        bsp2.append(list);
      }
      list.addAll(this.f19hblh0lx3g);
      if (bsp != null) {
        bsp.append(list);
      }
    }

    private static void Plane(
        Polygon polygon, Plane plane, List<Polygon> list, List<Polygon> list2) {
      ArrayList<Point> arrayList = new ArrayList<Point>();
      ArrayList<Point> arrayList2 = new ArrayList<Point>();
      Point[] pointArray = polygon.points;
      for (int i = 0; i < pointArray.length; ++i) {
        Point point = pointArray[i];
        Point point2 = pointArray[(i + 1) % pointArray.length];
        float f = plane.distance(point);
        float f2 = plane.distance(point2);
        if (f >= Float.intBitsToFloat(-1165815185)) {
          arrayList.add(point);
        }
        if (f <= Float.intBitsToFloat(981668463)) {
          arrayList2.add(point);
        }
        if (!(f > Float.intBitsToFloat(981668463) && f2 < Float.intBitsToFloat(-1165815185))
            && (!(f < Float.intBitsToFloat(-1165815185))
                || !(f2 > Float.intBitsToFloat(981668463)))) continue;
        float f3 = f / (f - f2);
        Point point3 =
            SceneModelNode.createPoint(
                point.x + (point2.x - point.x) * f3,
                point.y + (point2.y - point.y) * f3,
                point.z + (point2.z - point.z) * f3);
        arrayList.add(point3);
        arrayList2.add(point3);
      }
      if (arrayList.size() >= 3) {
        list.add(new Polygon((Point[]) arrayList.toArray(Point[]::new), polygon.color));
      }
      if (arrayList2.size() >= 3) {
        list2.add(new Polygon((Point[]) arrayList2.toArray(Point[]::new), polygon.color));
      }
    }
  }

  private record Plane(float nx, float ny, float nz, float d) {
    static Plane of(Polygon polygon) {
      Point point = polygon.points[0];
      Point point2 = polygon.points[1];
      Point point3 = polygon.points[2];
      float f = point2.y - point.y;
      float f2 = point3.z - point.z;
      float f3 = point2.z - point.z;
      float f4 = point3.y - point.y;
      float f5 = f * f2 - f3 * f4;
      float f6 = point3.x - point.x;
      float f7 = point2.x - point.x;
      float f8 = f3 * f6 - f7 * f2;
      float f9 = f7 * f4 - f * f6;
      float f10 = (float) Math.sqrt(f5 * f5 + f8 * f8 + f9 * f9);
      if (f10 < Float.intBitsToFloat(897988541)) {
        return null;
      }
      return new Plane(
          f5 /= f10, f8 /= f10, f9 /= f10, -(f5 * point.x + f8 * point.y + f9 * point.z));
    }

    float distance(Point point) {
      return this.nx * point.x + this.ny * point.y + this.nz * point.z + this.d;
    }

    int side(Polygon polygon) {
      int n = 0;
      for (Point point : polygon.points) {
        float f = this.distance(point);
        if (f > Float.intBitsToFloat(981668463)) {
          n |= 1;
        }
        if (!(f < Float.intBitsToFloat(-1165815185))) continue;
        n |= 2;
      }
      return n;
    }
  }

  private record Polygon(Point[] points, int color) {}
}
