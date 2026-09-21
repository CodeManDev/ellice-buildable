package dev.felix.ellice.ui.screen.builtin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Window;
import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.hud.layout.LayoutCodec;
import dev.felix.ellice.hud.layout.LayoutFindSelector;
import dev.felix.ellice.hud.layout.LayoutIsContainerService;
import dev.felix.ellice.hud.layout.LayoutNewDefaultService;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.hud.layout.LayoutRenderer;
import dev.felix.ellice.hud.layout.LayoutRepository;
import dev.felix.ellice.hud.layout.LayoutStringService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneDtService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlCompactService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.text.TextFontDirectoryService;
import dev.felix.ellice.ui.text.TextMode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class HudEditorScreen implements ScreenOperationHandler {
   private static final int f96wnwr73m7l = -871559915;
   private static final int f5ftcjd8we2j = -871757295;
   private static final int fgnoj8e1aydd = 788529151;
   private static final int fgasbzd8r2aw = 285212671;
   private static final int fcyomq0ivk0q = 1879048192;
   private static final int f42zvxlsv6hh = -218103809;
   private static final int fdps1helzlom = -553648129;
   private static final int f25cwb3nl5bj = -1627389953;
   private static final int f7265t9aijyl = 1795162111;
   private static final int feknbu39fnuo = 251658239;
   private static final int fi2h2kivyvli = 452984831;
   private static final int fgnx61vxnr4i = 268435456;
   private static final int fehg6gbkd60p = 385875967;
   private static final int fa918flgmw45 = 855638016;
   private static final int f3cgbyqv2hn0 = 822083583;
   private static final int f1e7ado8dk57 = 301989888;
   private static final int f49hdcrowlcj = 419430399;
   private static final int fcpq0ei0sdcq = 788529151;
   private static final int fc19rhy6chnf = 318767103;
   private static final int f73l7kvmsarc = 167772159;
   private static final Path fbqv2qn2af8p = FabricLoader.getInstance()
      .getModContainer("ellice")
      .flatMap(var0 -> var0.findPath("assets/ellice/components/icons/x.svg"))
      .orElse(Path.of("src/main/resources/assets/ellice/components/icons/x.svg"));
   private static final Path f41taspqavp9 = FabricLoader.getInstance()
      .getModContainer("ellice")
      .flatMap(var0 -> var0.findPath("assets/ellice/components/icons/trash.svg"))
      .orElse(Path.of("src/main/resources/assets/ellice/components/icons/trash.svg"));
   private static final int f5vo9a9a85vq = 384779332;
   private static final int fi9b6ktvnibj = 250561604;
   private static final int fc2y0cxc0uqn = 753878084;
   private static final int f14bt8k5frv9 = 1190085700;
   private static final int f9d8g0m9ut3k = 1307526212;
   private static final int f6v71ilx23a6 = -25958;
   private static final int fernql01npn4 = 452984831;
   private static final int fcffx5z4jxm0 = 352321535;
   private static final int f7avzt0j9pcv = 822083583;
   private static final int fdcll87yzerz = 301989888;
   private static final int f8hafkta8x8e = -1;
   private static final int f3us7cyk0kvq = -1513234;
   private static final int fdfhx152tz44 = -434628574;
   private static final float f6laym6gk4wz = 2.0F;
   private static final float fd3t5dtr7z1c = 0.0F;
   private static final float fg05gbms46ej = 46.0F;
   private static final float fg3lea8i5j4w = 168.0F;
   private static final float fdl95by5cpg0 = 220.0F;
   private static final int[][] fdv1pwgda7wj = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
   private static final String[][] fe0y5493cdq5 = new String[][]{
      {"fps", "FPS"}, {"coords", "Coords"}, {"rect", "Rect"}, {"armorhud", "Armor HUD"}, {"keystrokes", "Keystrokes"}
   };
   private final LayoutFindSelector f2prt6jl4bc;
   private final LayoutRenderer f8rtk7cicuor;
   private final LayoutOperationHandler f7fr4h364rv;
   private int fh22dz4o6gb0 = -10262799;
   private int f3dscnabklqo = -11581723;
   private LayoutContainerNode fbp8noz2yf8w;
   private SceneCornerRadiusService f8narrhwk0c5;
   private SceneCornerRadiusService f85ebf4cgvn2;
   private HudEditorScreen.ScreenSampleNode fetqhndloi5c;
   private SceneTextService fdqwnrs1i4ii;
   private String f3qwjbmavez9 = "Fit";
   private SceneTextService f8xxf9r3gts3;
   private LayoutContainerNode fjkjf66g3q82;
   private SceneTextService fb7v7dchg6qx;
   private boolean fbkjdm8rmyhv;
   private String f9b79zzflau1 = "";
   private LayoutContainerNode f31v8ssb3qo7;
   private LayoutContainerNode fhx46gstizzs;
   private TextFontDirectoryService flgozpodfet;
   private FONTComponent f87wbk15r6mn;
   private boolean ff0i2lnbhktw;
   private SceneCornerRadiusService f1oxmbxecj6;
   private SceneCornerRadiusService fbv5aezu4d6d;
   private SceneCornerRadiusService f9ailxqrx9w5;
   private ColourComponent f6lp4depep2p;
   private LayoutIsContainerService fipp48a3nif5;
   private LayoutContainerNode fuy67uovp93;
   private final SceneCornerRadiusService[] fherocc325dv = new SceneCornerRadiusService[9];
   private ControlLetterSpacingService f8adl4jpxtnn;
   private ControlLetterSpacingService fhvosy6eh2ev;
   private CompactSliderControl fdgd7d15rksl;
   private SceneTextService fadywccw1j68;
   private String fdpi7m01kxm1 = "";
   private ControlCompactService f7q6hgnuza9v;
   private ControlCompactService f99y1vdyi106;
   private LayoutIsContainerService f73b914k7qrv;
   private String f9sg2aat5v1n = "";
   private String fbknte11kewb = "";
   private LayoutContainerNode f6y70fw856pa;
   private LayoutIsContainerService fbpg4ilk69c9;
   private boolean fb84je10o0l8;
   private final List<Runnable> f75csd7dbp6l = new ArrayList<>();
   private final List<ColourComponent> fhc6hu0todpb = new ArrayList<>();
   private final List<Runnable> fds2fqndpgdh = new ArrayList<>();
   private final List<ColourComponent> f77ef2glx8wk = new ArrayList<>();
   private List<Runnable> ff3g1h4q7tfg = this.f75csd7dbp6l;
   private List<ColourComponent> f9xxgz6cr89j = this.fhc6hu0todpb;
   private LayoutContainerNode fga9wawkp0sx;
   private LayoutIsContainerService fi06ijkdbakq;
   private boolean f8ze0ngtip07;
   private SceneCornerRadiusService f1qx0m3ot2qg;
   private SceneCornerRadiusService fexrk9yloox5;
   private final List<Runnable> f3xssi477ala = new ArrayList<>();
   private final List<ColourComponent> fyh7bfyp88z = new ArrayList<>();
   private LayoutContainerNode f6s8d99hz8qx;
   private LayoutIsContainerService fg8obau2afpd;
   private boolean f40bk6yc2zqk;
   private FONTComponent feemtwsjsd4d;
   private ControlLetterSpacingService f4gxtve49sah;
   private ControlLetterSpacingService fd18idlubdkk;
   private String fhp9h6gpevng = "";
   private String fdv6femko4bf = "";
   private CompactSliderControl f96dfdn55rhr;
   private SceneTextService fcp6k70oa7q;
   private JsonObject ftxajmi19ob;
   private float fgw3tbkd8bmp = 1.0F;
   private SceneTextService ffyp031lzdhl;
   private long f4r23sa6lvd9;
   private LayoutContainerNode fjfgtmdxhqe6;
   private LayoutContainerNode f6blsk1mokq2;
   private String f3un8o2fjumw = "";
   private LayoutIsContainerService f3dzr20rwo5a;
   private boolean fij1ukc3rqz8;
   private float f32noh3bskqo;
   private static final float fi9f9jpu1foa = 26.0F;
   private static final float f1kzdpudx42z = 3.0F;
   private static final float fg7yfexypqmk = 29.0F;
   private static final float f764x7gufv5r = 11.0F;
   private static final float f7vh8zlywxn1 = 8.0F;
   private final List<LayoutIsContainerService> f7kty3ijaokt = new ArrayList<>();
   private final Map<LayoutIsContainerService, SceneCornerRadiusService> fb12afqf5ew0 = new HashMap<>();
   private final List<HudEditorScreen.PendingAnim> fc4scvr07kj6 = new ArrayList<>();

   public HudEditorScreen(LayoutFindSelector var1, LayoutRenderer var2, LayoutOperationHandler var3) {
      this.f2prt6jl4bc = var1;
      this.f8rtk7cicuor = var2;
      this.f7fr4h364rv = var3;
   }

   @Override
   public String id() {
      return "hudeditor";
   }

   @Override
   public SceneCodec.Preset transitionPreset() {
      return SceneCodec.Preset.FADE;
   }

   @Override
   public float backgroundDesaturation() {
      return 0.0F;
   }

   @Override
   public ScenePctService<?> build(ScreenScreenIdService var1) {
      int var2 = var1.theme().color("accent");
      if (var2 != 0) {
         this.fh22dz4o6gb0 = var2;
      }

      int var3 = var1.theme().color("accent-press");
      if (var3 != 0) {
         this.f3dscnabklqo = var3;
      }

      this.fbp8noz2yf8w = new LayoutContainerNode()
         .size(
            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))
         )
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH);
      this.flgozpodfet = new TextFontDirectoryService(FabricLoader.getInstance().getGameDir());
      this.fbp8noz2yf8w
         .addChild(
            new SceneCornerRadiusService()
               .absolute()
               .inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0F))
               .size(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(), dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())
               .backgroundColor(805306368)
               .cornerRadius(0.0F)
               .interactive(true)
               .layerBreak(true)
         );
      this.f8narrhwk0c5 = this.m4rckm7i9x9h(var1);
      this.f8narrhwk0c5
         .size(
            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))
         );
      this.fbp8noz2yf8w.addChild(this.f8narrhwk0c5);
      this.f8narrhwk0c5.scale(Float.intBitsToFloat(1064849900)).opacity(0.0F);
      this.f8narrhwk0c5.animate("opacity", 1.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1046562734)));
      this.f8narrhwk0c5.animate("scale", 1.0F, SceneEaseHandler.Spring.SNAPPY);
      return this.fbp8noz2yf8w;
   }

   private SceneCornerRadiusService m4rckm7i9x9h(ScreenScreenIdService var1) {
      SceneCornerRadiusService var2 = new SceneCornerRadiusService()
         .cornerRadius(0.0F)
         .backgroundColor(-871559915)
         .gradientEnd(-871757295)
         .blur(Float.intBitsToFloat(1107820544))
         .glass(true)
         .glassSaturation(Float.intBitsToFloat(1066192077))
         .glassBrightness(Float.intBitsToFloat(-1133133169))
         .glassContrast(Float.intBitsToFloat(1065856532))
         .glassRefraction(Float.intBitsToFloat(1082130432))
         .glassNoise(Float.intBitsToFloat(991775058))
         .glassChromatic(Float.intBitsToFloat(1050253722))
         .glassHighlightColor(385875967)
         .border(Float.intBitsToFloat(1056964608), 788529151)
         .secondaryBorderColor(285212671)
         .shadow(Float.intBitsToFloat(1104150528))
         .shadowColor(1879048192)
         .clip(true)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START);
      LayoutContainerNode var3 = this.mbyfz7rnvc1a(var1);
      var3.opacity(0.0F);
      var2.addChild(var3);
      this.mfxl2apq71o0(var3, Float.intBitsToFloat(1031127695));
      var2.addChild(new SceneCornerRadiusService().size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), 1.0F).backgroundColor(318767103));
      LayoutContainerNode var4 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .flex(1.0F);
      var2.addChild(var4);
      LayoutContainerNode var5 = this.m1klpgjudor6();
      var5.opacity(0.0F);
      var4.addChild(var5);
      this.mfxl2apq71o0(var5, Float.intBitsToFloat(1039516303));
      var4.addChild(
         new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1056964608), ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .backgroundColor(167772159)
      );
      LayoutContainerNode var6 = this.mj0chqsxuf66();
      var6.opacity(0.0F);
      var4.addChild(var6);
      this.mfxl2apq71o0(var6, Float.intBitsToFloat(1043878380));
      var4.addChild(
         new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1056964608), ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .backgroundColor(167772159)
      );
      LayoutContainerNode var7 = this.mab92plz1ev5();
      var7.opacity(0.0F);
      var4.addChild(var7);
      this.mfxl2apq71o0(var7, Float.intBitsToFloat(1047904911));
      return var2;
   }

   private LayoutContainerNode mbyfz7rnvc1a(ScreenScreenIdService var1) {
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1110966272))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN)
         .padding(0.0F, Float.intBitsToFloat(1098907648));
      LayoutContainerNode var3 = new LayoutContainerNode()
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1092616192));
      var3.addChild(mejww8vrb7gl("HUD Editor", Float.intBitsToFloat(1097859072), -218103809, TextMode.BOLD));
      this.ffyp031lzdhl = mejww8vrb7gl("", Float.intBitsToFloat(1091043328), this.fh22dz4o6gb0, TextMode.BOLD);
      this.ffyp031lzdhl.opacity(0.0F);
      var3.addChild(this.ffyp031lzdhl);
      var2.addChild(var3);
      LayoutContainerNode var4 = new LayoutContainerNode()
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.END)
         .gap(Float.intBitsToFloat(1090519040));
      var4.addChild(this.mbpayroggdes());
      var4.addChild(this.m53lv5ywch9s());
      var4.addChild(this.mevc01em7wui(var1));
      var2.addChild(var4);
      return var2;
   }

   private SceneCornerRadiusService mbpayroggdes() {
      SceneCornerRadiusService var1 = new SceneCornerRadiusService()
         .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1102053376))
         .cornerRadius(Float.intBitsToFloat(1090519040))
         .backgroundColor(this.fh22dz4o6gb0 & 16777215 | 855638016)
         .gradientEnd(this.f3dscnabklqo & 16777215 | 855638016)
         .hoverBackground(this.fh22dz4o6gb0 & 16777215 | 1426063360)
         .border(Float.intBitsToFloat(1056964608), this.fh22dz4o6gb0 & 16777215 | 1879048192)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .padding(0.0F, Float.intBitsToFloat(1093664768))
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(this::mcwatwgehpgo);
      var1.addChild(mejww8vrb7gl("Save", Float.intBitsToFloat(1091043328), -218103809, TextMode.BOLD));
      return var1;
   }

   private SceneCornerRadiusService m53lv5ywch9s() {
      this.fdqwnrs1i4ii = mejww8vrb7gl("Fit", Float.intBitsToFloat(1090519040), -1627389953, TextMode.BOLD);
      SceneCornerRadiusService var1 = new SceneCornerRadiusService()
         .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1102053376))
         .cornerRadius(Float.intBitsToFloat(1090519040))
         .backgroundColor(419430399)
         .hoverBackground(385875967)
         .border(Float.intBitsToFloat(1056964608), 788529151)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .padding(0.0F, Float.intBitsToFloat(1091567616))
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> {
            if (this.fetqhndloi5c != null) {
               this.fetqhndloi5c.resetView();
            }
         });
      var1.addChild(this.fdqwnrs1i4ii);
      return var1;
   }

   private SceneCornerRadiusService mevc01em7wui(ScreenScreenIdService var1) {
      SceneCornerRadiusService var2 = new SceneCornerRadiusService()
         .size(Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1104150528))
         .cornerRadius(Float.intBitsToFloat(1091567616))
         .backgroundColor(0)
         .hoverBackground(385875967)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(var1::close);
      SceneSrcService var3 = new SceneSrcService()
         .src(fbqv2qn2af8p)
         .size(Float.intBitsToFloat(1093664768), Float.intBitsToFloat(1093664768))
         .tintColor(-1627389953);
      var2.addChild(var3);
      var2.onHoverChange(var1x -> var3.animateColor("color", var1x ? -218103809 : -1627389953, SceneEaseHandler.Spring.SNAPPY));
      return var2;
   }

   private LayoutContainerNode m1klpgjudor6() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(Float.intBitsToFloat(1126694912), ScenePctService.pct(Float.intBitsToFloat(1120403456)))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .padding(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1094713344))
         .gap(Float.intBitsToFloat(1090519040));
      var1.addChild(this.mcvucas6k1zq("ADD"));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1086324736));

      for (String[] var6 : fe0y5493cdq5) {
         var2.addChild(this.miy2iacyy9nb(var6[0], var6[1]));
      }

      var1.addChild(var2);
      var1.addChild(this.mcvucas6k1zq("LAYERS").marginTop(Float.intBitsToFloat(1082130432)));
      this.fjfgtmdxhqe6 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .flex(1.0F)
         .scrollable(true)
         .clip(true)
         .scrollbarWidth(Float.intBitsToFloat(1077936128))
         .scrollbarColor(1157627903);
      this.f6blsk1mokq2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), 0.0F)
         .direction(ScenePctService.Direction.NONE)
         .padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1093664768), Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1093664768));
      this.fjfgtmdxhqe6.addChild(this.f6blsk1mokq2);
      var1.addChild(this.fjfgtmdxhqe6);
      return var1;
   }

   private void mbxn3u3dhfxk() {
      if (this.f6blsk1mokq2 != null && this.f2prt6jl4bc != null && !this.fij1ukc3rqz8) {
         this.f6blsk1mokq2.clearChildren();
         if (this.fjfgtmdxhqe6 != null) {
            this.fjfgtmdxhqe6.scrollY(0.0F);
         }

         this.f7kty3ijaokt.clear();
         this.fb12afqf5ew0.clear();
         if (this.f2prt6jl4bc.elements.isEmpty()) {
            this.f6blsk1mokq2.size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432));
            this.f6blsk1mokq2
               .addChild(
                  mejww8vrb7gl("No elements yet.", Float.intBitsToFloat(1090519040), 1795162111, TextMode.REGULAR)
                     .marginTop(Float.intBitsToFloat(1082130432))
                     .marginLeft(2.0F)
               );
         } else {
            int var1 = this.f2prt6jl4bc.elements.size();

            for (int var2 = 0; var2 < var1; var2++) {
               LayoutIsContainerService var3 = this.f2prt6jl4bc.elements.get(var1 - 1 - var2);
               SceneCornerRadiusService var4 = this.m7vl7x44849i(var3);
               var4.position(0.0F, var2 * Float.intBitsToFloat(1105723392));
               this.f6blsk1mokq2.addChild(var4);
               this.f7kty3ijaokt.add(var3);
               this.fb12afqf5ew0.put(var3, var4);
            }

            this.f6blsk1mokq2
               .size(
                  ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                  (var1 - 1) * Float.intBitsToFloat(1105723392) + Float.intBitsToFloat(1104150528) + Float.intBitsToFloat(1098907648)
               );
         }
      }
   }

   private SceneCornerRadiusService m7vl7x44849i(LayoutIsContainerService var1) {
      int var2 = this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var1) ? 1 : 0;
      SceneCornerRadiusService var3 = new SceneCornerRadiusService()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1104150528))
         .cornerRadius(Float.intBitsToFloat(1088421888))
         .backgroundColor(var2 != 0 ? this.fh22dz4o6gb0 & 16777215 | 771751936 : 0)
         .hoverBackground(419430399)
         .border(0.0F, this.fh22dz4o6gb0 & 16777215 | -1610612736)
         .shadowColor(1711276032)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .padding(0.0F, Float.intBitsToFloat(1086324736))
         .gap(Float.intBitsToFloat(1082130432))
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> this.m7w6j6bb5vgu(var1));
      HudEditorScreen.LayerGripNode var4 = new HudEditorScreen.LayerGripNode(
         1795162111, -553648129, var2x -> this.mc9gx3ubmlnf(var1, var2x), var1x -> this.m4a5qj1m70cm(var1x)
      );
      var4.size(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1101004800));
      var3.addChild(var4);
      SceneTextService var5 = mejww8vrb7gl(
         m6z84l3mpji(var1), Float.intBitsToFloat(1091043328), var1.visible ? (var2 != 0 ? -218103809 : -553648129) : 1795162111, TextMode.REGULAR
      );
      var5.overflow(SceneTextService.Overflow.ELLIPSIS);
      var5.flex(1.0F);
      var3.addChild(var5);
      var3.onHoverChange(
         var3x -> var5.animateColor(
             "color", var3x ? -218103809 : (var1.visible ? (var2 != 0 ? -218103809 : -553648129) : 1795162111), SceneEaseHandler.Spring.SNAPPY
         )
      );
      var3.addChild(
         this.mf1bqf3axafi(
            var1.visible ? "eye.svg" : "eye-off.svg",
            Float.intBitsToFloat(1093664768),
            var1.visible ? -1627389953 : 1795162111,
            -218103809,
            () -> var1.visible = !var1.visible
         )
      );
      var3.addChild(
         this.mf1bqf3axafi(
            var1.locked ? "lock.svg" : "lock-open.svg",
            Float.intBitsToFloat(1093664768),
            var1.locked ? this.fh22dz4o6gb0 : 1795162111,
            -218103809,
            () -> var1.locked = !var1.locked
         )
      );
      return var3;
   }

   private LayoutContainerNode mf1bqf3axafi(String var1, float var2, int var3, int var4, Runnable var5) {
      SceneSrcService var6 = new SceneSrcService().src(mnciejz4hrw(var1)).size(var2, var2).tintColor(var3);
      LayoutContainerNode var7 = new LayoutContainerNode()
         .size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .stopPropagation(true)
         .onClick(var5)
         .onHoverChange(var3x -> var6.animateColor("color", var3x ? var4 : var3, SceneEaseHandler.Spring.SNAPPY));
      var7.addChild(var6);
      return var7;
   }

   private void m7w6j6bb5vgu(LayoutIsContainerService var1) {
      if (this.fetqhndloi5c != null) {
         long var2 = CompatAdapterService.windowHandle(Minecraft.getInstance());
         int var4 = var2 == 0L || GLFW.glfwGetKey(var2, 340) != 1 && GLFW.glfwGetKey(var2, 344) != 1 ? 0 : 1;
         if (var4 != 0) {
            this.fetqhndloi5c.toggleSelection(var1);
         } else {
            this.fetqhndloi5c.selectOnly(var1);
         }
      }
   }

   private void mc9gx3ubmlnf(LayoutIsContainerService var1, float var2) {
      SceneCornerRadiusService var3 = this.fb12afqf5ew0.get(var1);
      if (this.f2prt6jl4bc != null && var3 != null) {
         this.f3dzr20rwo5a = var1;
         this.f32noh3bskqo = var2;
         this.fij1ukc3rqz8 = true;
         if (this.f6blsk1mokq2 != null) {
            this.f6blsk1mokq2.bringChildToFront(var3);
         }

         var3.cancelAnimation("y");
         var3.animate("scale", Float.intBitsToFloat(1065688760), SceneEaseHandler.Spring.SNAPPY);
         var3.animate("shadow", Float.intBitsToFloat(1084227584), SceneEaseHandler.Spring.SNAPPY);
         var3.animate("borderWidth", 1.0F, SceneEaseHandler.Spring.SNAPPY);
         var3.animateColor("backgroundColor", this.fh22dz4o6gb0 & 16777215 | 1275068416, SceneEaseHandler.Spring.SNAPPY);
      }
   }

   private void m4a5qj1m70cm(float var1) {
      if (this.fij1ukc3rqz8 && this.f3dzr20rwo5a != null) {
         SceneCornerRadiusService var2 = this.fb12afqf5ew0.get(this.f3dzr20rwo5a);
         if (var2 != null) {
            int var3 = this.f7kty3ijaokt.size();
            float var4 = Math.max(0.0F, (var3 - 1) * Float.intBitsToFloat(1105723392));
            var2.y = Math.max(0.0F, Math.min(var4, var2.y + (var1 - this.f32noh3bskqo)));
            var2.invalidate();
            this.f32noh3bskqo = var1;
            int var5 = this.f7kty3ijaokt.indexOf(this.f3dzr20rwo5a);
            int var6 = Math.max(0, Math.min(var3 - 1, Math.round(var2.y / Float.intBitsToFloat(1105723392))));
            if (var6 != var5 && var5 >= 0) {
               this.f7kty3ijaokt.remove(var5);
               this.f7kty3ijaokt.add(var6, this.f3dzr20rwo5a);

               for (int var7 = 0; var7 < var3; var7++) {
                  LayoutIsContainerService var8 = this.f7kty3ijaokt.get(var7);
                  if (var8 != this.f3dzr20rwo5a) {
                     SceneCornerRadiusService var9 = this.fb12afqf5ew0.get(var8);
                     if (var9 != null) {
                        var9.animate("y", var7 * Float.intBitsToFloat(1105723392), SceneEaseHandler.Spring.SNAPPY);
                     }
                  }
               }
            }
         }
      }
   }

   private void m5zw901d7kqo() {
      if (this.fij1ukc3rqz8) {
         this.fij1ukc3rqz8 = false;
         LayoutIsContainerService var1 = this.f3dzr20rwo5a;
         this.f3dzr20rwo5a = null;
         SceneCornerRadiusService var2 = var1 == null ? null : this.fb12afqf5ew0.get(var1);
         if (var2 != null) {
            int var3 = this.f7kty3ijaokt.indexOf(var1);
            if (var3 >= 0) {
               var2.animate("y", var3 * Float.intBitsToFloat(1105723392), SceneEaseHandler.Spring.SNAPPY);
            }

            var2.animate("scale", 1.0F, SceneEaseHandler.Spring.SNAPPY);
            var2.animate("shadow", 0.0F, SceneEaseHandler.Spring.SNAPPY);
            var2.animate("borderWidth", 0.0F, SceneEaseHandler.Spring.SNAPPY);
            int var4 = this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var1) ? 1 : 0;
            var2.animateColor("backgroundColor", var4 != 0 ? this.fh22dz4o6gb0 & 16777215 | 771751936 : 0, SceneEaseHandler.Spring.SNAPPY);
         }

         if (this.f2prt6jl4bc != null && !this.f7kty3ijaokt.isEmpty()) {
            ArrayList var6 = new ArrayList();

            for (int var7 = this.f7kty3ijaokt.size() - 1; var7 >= 0; var7 += -1) {
               LayoutIsContainerService var5 = this.f7kty3ijaokt.get(var7);
               if (this.f2prt6jl4bc.elements.contains(var5)) {
                  var6.add(var5);
               }
            }

            if (var6.size() == this.f2prt6jl4bc.elements.size()) {
               this.f2prt6jl4bc.elements.clear();
               this.f2prt6jl4bc.elements.addAll(var6);
               this.f3un8o2fjumw = this.m43oysyp9t2g();
            }
         } else {
            this.f3un8o2fjumw = this.m43oysyp9t2g();
         }
      }
   }

   private static Path mnciejz4hrw(String var0) {
      return FabricLoader.getInstance()
         .getModContainer("ellice")
         .flatMap(var1 -> var1.findPath("assets/ellice/components/icons/" + var0))
         .orElse(Path.of("src/main/resources/assets/ellice/components/icons/" + var0));
   }

   private SceneCornerRadiusService miy2iacyy9nb(String var1, String var2) {
      SceneCornerRadiusService var3 = new SceneCornerRadiusService()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1107820544))
         .cornerRadius(Float.intBitsToFloat(1092616192))
         .backgroundColor(251658239)
         .hoverBackground(385875967)
         .pressBackground(251658239)
         .border(Float.intBitsToFloat(1056964608), 452984831)
         .secondaryBorderColor(268435456)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .padding(0.0F, Float.intBitsToFloat(1094713344))
         .gap(Float.intBitsToFloat(1090519040))
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> this.m8hjo123ghs2(var1));
      var3.addChild(mejww8vrb7gl(var2, Float.intBitsToFloat(1092616192), -553648129, TextMode.BOLD));
      return var3;
   }

   private void m8hjo123ghs2(String var1) {
      if (this.f2prt6jl4bc != null) {
         LayoutIsContainerService var2 = m7ojld7zqtmz(var1);
         if (var2 != null) {
            int var3 = this.f2prt6jl4bc.elements.size();
            var2.offsetX = var2.offsetX + var3 % 6 * Float.intBitsToFloat(1094713344);
            var2.offsetY = var2.offsetY + var3 % 6 * Float.intBitsToFloat(1094713344);
            this.f2prt6jl4bc.elements.add(var2);
         }
      }
   }

   private static LayoutIsContainerService m7ojld7zqtmz(String var0) {
      return switch (var0) {
         case "text" -> LayoutNewDefaultService.createText();
         case "rect" -> LayoutNewDefaultService.createRect();
         case "bar" -> LayoutNewDefaultService.createBar("player.health");
         case "armorhud" -> LayoutNewDefaultService.createArmorHud();
         case "keystrokes" -> LayoutNewDefaultService.createKeystrokes();
         case "fps" -> {
            LayoutIsContainerService var4 = LayoutNewDefaultService.createText();
            var4.type = "fps";
            var4.props = new JsonObject();
            yield var4;
         }
         case "coords" -> {
            LayoutIsContainerService var3 = LayoutNewDefaultService.createText();
            var3.type = "coords";
            var3.props = new JsonObject();
            yield var3;
         }
         default -> null;
      };
   }

   private LayoutContainerNode mj0chqsxuf66() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .flex(1.0F)
         .padding(Float.intBitsToFloat(1098907648));
      this.f85ebf4cgvn2 = new SceneCornerRadiusService()
         .flex(1.0F)
         .cornerRadius(Float.intBitsToFloat(1094713344))
         .backgroundColor(855638016)
         .border(Float.intBitsToFloat(1056964608), 822083583)
         .secondaryBorderColor(301989888)
         .clip(true)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START);
      this.fetqhndloi5c = new HudEditorScreen.ScreenSampleNode(this.f2prt6jl4bc, this.f8rtk7cicuor, this.f7fr4h364rv);
      this.fetqhndloi5c.flex(1.0F).interactive(true);
      this.f85ebf4cgvn2.addChild(this.fetqhndloi5c);
      this.f85ebf4cgvn2.addChild(new HudEditorScreen.HudPreviewNode(this.fetqhndloi5c, this.f2prt6jl4bc, this.f8rtk7cicuor, this.f7fr4h364rv));
      this.f85ebf4cgvn2.addChild(new HudEditorScreen.OverlayNode(this.fetqhndloi5c, this.fh22dz4o6gb0));
      var1.addChild(this.f85ebf4cgvn2);
      return var1;
   }

   private LayoutContainerNode mab92plz1ev5() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(Float.intBitsToFloat(1130102784), ScenePctService.pct(Float.intBitsToFloat(1120403456)))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .padding(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1094713344))
         .gap(Float.intBitsToFloat(1090519040));
      var1.addChild(this.mcvucas6k1zq("INSPECTOR"));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1090519040))
         .padding(0.0F, Float.intBitsToFloat(1092616192), 0.0F, Float.intBitsToFloat(1086324736))
         .flex(1.0F)
         .scrollable(true)
         .clip(true)
         .scrollbarWidth(Float.intBitsToFloat(1077936128))
         .scrollbarColor(1157627903);
      this.f8xxf9r3gts3 = mejww8vrb7gl("No element selected", Float.intBitsToFloat(1091567616), -1627389953, TextMode.REGULAR);
      var2.addChild(this.f8xxf9r3gts3);
      this.fjkjf66g3q82 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1094713344));
      this.fb7v7dchg6qx = mejww8vrb7gl("", Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD);
      this.fjkjf66g3q82.addChild(this.fb7v7dchg6qx);
      this.fuy67uovp93 = this.macrp7h9n1d9();
      this.fjkjf66g3q82.addChild(this.fuy67uovp93);
      this.f31v8ssb3qo7 = this.mgzdpf0x6ikt();
      this.f31v8ssb3qo7.visible(false);
      this.fjkjf66g3q82.addChild(this.f31v8ssb3qo7);
      this.f6y70fw856pa = this.mblgoju6j7dc();
      this.f6y70fw856pa.visible(false);
      this.fjkjf66g3q82.addChild(this.f6y70fw856pa);
      this.fga9wawkp0sx = this.mil7znabye0v();
      this.fga9wawkp0sx.visible(false);
      this.fjkjf66g3q82.addChild(this.fga9wawkp0sx);
      this.f6s8d99hz8qx = this.m4g9a2wkfxyf();
      this.f6s8d99hz8qx.visible(false);
      this.fjkjf66g3q82.addChild(this.f6s8d99hz8qx);
      this.fjkjf66g3q82.addChild(this.me2fl17o23av());
      this.fjkjf66g3q82.addChild(this.m2f1jbj0xjvf());
      this.fjkjf66g3q82.visible(false);
      var2.addChild(this.fjkjf66g3q82);
      var1.addChild(var2);
      return var1;
   }

   private LayoutContainerNode mbfu3iaxadx0() {
      this.f87wbk15r6mn = new FONTComponent(
         this.fbp8noz2yf8w, this.flgozpodfet, CoreIsInitializedHandler.get().compositor(), this.fh22dz4o6gb0, this::mea8ljk2rbik
      );
      return this.f87wbk15r6mn.node();
   }

   private void mea8ljk2rbik(String var1) {
      if (this.fetqhndloi5c != null) {
         LayoutIsContainerService var2 = this.fetqhndloi5c.selected();
         if (var2 != null) {
            if (var2.props == null) {
               var2.props = new JsonObject();
            }

            if (var1 == null) {
               var2.props.remove("font");
            } else {
               var2.props.addProperty("font", var1);
            }
         }
      }
   }

   private static boolean malgeiokaae(LayoutIsContainerService var0) {
      String var1 = var0.type;
      return "text".equals(var1) || "fps".equals(var1) || "coords".equals(var1);
   }

   private static boolean m7vsrvzjfs8c(LayoutIsContainerService var0) {
      return "rect".equals(var0.type);
   }

   private LayoutContainerNode mblgoju6j7dc() {
      this.ff3g1h4q7tfg = this.f75csd7dbp6l;
      this.f9xxgz6cr89j = this.fhc6hu0todpb;
      this.f75csd7dbp6l.clear();
      this.fhc6hu0todpb.clear();
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
      var1.addChild(this.m9x8q2vlh9uq());
      var1.addChild(this.mgu3yar4wwnz("FILL", "color", -14671832));
      LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var2.addChild(this.mgu3yar4wwnz("END COLOUR", "gradientEnd", -15724520));
      var1.addChild(this.mapc0ng175le("Gradient", "gradient", false, var2));
      var1.addChild(this.mbr0vcvekm60());
      var1.addChild(this.mjoqra3lvuyv());
      var1.addChild(this.mgxijwfep92z());
      LayoutContainerNode var3 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var3.addChild(this.mb1btrwgujbo("AMOUNT", "innerHighlight", 0.0F, 1.0F, Float.intBitsToFloat(1008981770), 0.0F, false));
      var3.addChild(this.mb1btrwgujbo("SIZE", "innerHighlightSize", 0.0F, Float.intBitsToFloat(1109393408), 1.0F, 0.0F, true));
      var1.addChild(this.mhx5vlavd5a3("INNER HIGHLIGHT", var3));
      var1.addChild(this.mb1btrwgujbo("FEATHER", "edgeSoftness", 0.0F, Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1056964608), 0.0F, true));
      var1.addChild(this.mb1btrwgujbo("BACKDROP BLUR", "blur", 0.0F, Float.intBitsToFloat(1109393408), 1.0F, 0.0F, true));
      LayoutContainerNode var4 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var4.addChild(this.mb1btrwgujbo("SATURATION", "glassSaturation", 0.0F, 2.0F, Float.intBitsToFloat(1008981770), Float.intBitsToFloat(1066024305), false));
      var4.addChild(
         this.m7tcnky7ip0q(
            "BRIGHTNESS",
            "glassBrightness",
            Float.intBitsToFloat(-1090519040),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1008981770),
            Float.intBitsToFloat(-1130113270),
            2
         )
      );
      var4.addChild(
         this.m7tcnky7ip0q(
            "CONTRAST", "glassContrast", Float.intBitsToFloat(1056964608), 2.0F, Float.intBitsToFloat(1008981770), Float.intBitsToFloat(1065856532), 2
         )
      );
      var4.addChild(
         this.mb1btrwgujbo(
            "REFRACTION", "glassRefraction", 0.0F, Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1056964608), Float.intBitsToFloat(1090519040), true
         )
      );
      var4.addChild(
         this.m7tcnky7ip0q("NOISE", "glassNoise", 0.0F, Float.intBitsToFloat(1017370378), Float.intBitsToFloat(973279855), Float.intBitsToFloat(993493044), 4)
      );
      var4.addChild(
         this.mb1btrwgujbo(
            "CHROMATIC", "glassChromatic", 0.0F, Float.intBitsToFloat(1077936128), Float.intBitsToFloat(1028443341), Float.intBitsToFloat(1056964608), false
         )
      );
      var4.addChild(this.mgu3yar4wwnz("HIGHLIGHT", "glassHighlightColor", 486539263));
      var1.addChild(this.m28byo25cjtk("Glass", "glass", false, var4, var1x -> {
         if (var1x) {
            LayoutIsContainerService var2x = this.m8aefs6eqlgs();
            if (var2x != null && LayoutStringService.number(var2x.props, "blur", 0.0F) <= 0.0F) {
               this.m4utfnbc8jwd("blur", Float.intBitsToFloat(1099956224));
            }
         }
      }));
      return var1;
   }

   private LayoutContainerNode m9x8q2vlh9uq() {
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("SIZE", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1090519040));
      this.f4gxtve49sah = this.marquun6sn4f(var1x -> this.mimlouseeyin(true, var1x));
      this.fd18idlubdkk = this.marquun6sn4f(var1x -> this.mimlouseeyin(false, var1x));
      var2.addChild(this.mc5uuf6rmafd("W", this.f4gxtve49sah));
      var2.addChild(this.mc5uuf6rmafd("H", this.fd18idlubdkk));
      var1.addChild(var2);
      return var1;
   }

   private void mimlouseeyin(boolean var1, String var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         try {
            float var4 = Float.parseFloat(var2.trim());
            if (Float.isFinite(var4) && var4 >= 1.0F) {
               if (var1) {
                  var3.width = var4;
               } else {
                  var3.height = var4;
               }
            }
         } catch (Exception var5) {
         }
      }
   }

   private LayoutContainerNode mbr0vcvekm60() {
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var1.addChild(mejww8vrb7gl("CORNERS", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      LayoutContainerNode var3 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN);
      var3.addChild(mejww8vrb7gl("Radius", Float.intBitsToFloat(1089470464), -1627389953, TextMode.REGULAR).marginLeft(2.0F));
      this.fcp6k70oa7q = mejww8vrb7gl("4", Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
      var3.addChild(this.fcp6k70oa7q);
      var2.addChild(var3);
      this.f96dfdn55rhr = new CompactSliderControl(
         0.0F,
         Float.intBitsToFloat(1109393408),
         Float.intBitsToFloat(1056964608),
         Float.intBitsToFloat(1082130432),
         this.fh22dz4o6gb0,
         this.f3dscnabklqo,
         var1x -> {
            this.m4utfnbc8jwd("cornerRadius", var1x);
            this.fcp6k70oa7q.text(m6614bn3b4vx(var1x, false));
         }
      );
      var2.addChild(this.f96dfdn55rhr);
      this.ff3g1h4q7tfg.add(() -> {
         float var1x = this.m5s0ch7dk74h("cornerRadius", Float.intBitsToFloat(1082130432));
         this.f96dfdn55rhr.value(var1x);
         this.fcp6k70oa7q.text(m6614bn3b4vx(var1x, false));
      });
      var1.addChild(var2);
      LayoutContainerNode var4 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      LayoutContainerNode var5 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .gap(Float.intBitsToFloat(1090519040));
      var5.addChild(this.m86hlye6nh75("TL", "cornerTL"));
      var5.addChild(this.m86hlye6nh75("TR", "cornerTR"));
      LayoutContainerNode var6 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .gap(Float.intBitsToFloat(1090519040));
      var6.addChild(this.m86hlye6nh75("BL", "cornerBL"));
      var6.addChild(this.m86hlye6nh75("BR", "cornerBR"));
      var4.addChild(var5);
      var4.addChild(var6);
      var1.addChild(this.mapc0ng175le("Per corner", "perCorner", false, var4));
      return var1;
   }

   private LayoutContainerNode mjoqra3lvuyv() {
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var1.addChild(this.mb1btrwgujbo("BORDER", "borderWidth", 0.0F, Float.intBitsToFloat(1086324736), Float.intBitsToFloat(1048576000), 0.0F, false));
      var1.addChild(this.mgu3yar4wwnz("BORDER COLOUR", "borderColor", -1));
      LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var2.addChild(this.mgu3yar4wwnz("BOTTOM COLOUR", "borderColor2", 1090519039));
      var1.addChild(this.mapc0ng175le("2-tone border", "border2", false, var2));
      return var1;
   }

   private LayoutContainerNode mgxijwfep92z() {
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var1.addChild(this.mb1btrwgujbo("SHADOW", "shadow", 0.0F, Float.intBitsToFloat(1109393408), 1.0F, 0.0F, true));
      var1.addChild(this.mgu3yar4wwnz("SHADOW COLOUR", "shadowColor", 1610612736));
      var1.addChild(this.m4ngqca316ch("Inset shadow", "insetShadow", false));
      return var1;
   }

   private LayoutContainerNode mb1btrwgujbo(String var1, String var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.m7tcnky7ip0q(var1, var2, var3, var4, var5, var6, var7 ? -1 : 2);
   }

   private LayoutContainerNode m7tcnky7ip0q(String var1, String var2, float var3, float var4, float var5, float var6, int var7) {
      LayoutContainerNode var8 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      LayoutContainerNode var9 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN);
      var9.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      SceneTextService var10 = mejww8vrb7gl(mj40fnfhfd0y(var6, var7), Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
      var9.addChild(var10);
      var8.addChild(var9);
      CompactSliderControl var11 = new CompactSliderControl(var3, var4, var5, var6, this.fh22dz4o6gb0, this.f3dscnabklqo, var4x -> {
         this.m4utfnbc8jwd(var2, var4x);
         var10.text(mj40fnfhfd0y(var4x, var7));
      });
      var8.addChild(var11);
      this.ff3g1h4q7tfg.add(() -> {
         float var6x = this.m5s0ch7dk74h(var2, var6);
         var11.value(var6x);
         var10.text(mj40fnfhfd0y(var6x, var7));
      });
      return var8;
   }

   private LayoutContainerNode mgu3yar4wwnz(String var1, String var2, int var3) {
      ColourComponent var4 = new ColourComponent(this.fbp8noz2yf8w, var2x -> this.m4hru6zgdqyy(var2, var2x));
      this.f9xxgz6cr89j.add(var4);
      this.ff3g1h4q7tfg.add(() -> var4.setColor(this.m6vslb70e5uc(var2, var3)));
      LayoutContainerNode var5 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      var5.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      var5.addChild(var4.node());
      return var5;
   }

   private LayoutContainerNode m86hlye6nh75(String var1, String var2) {
      ControlLetterSpacingService var3 = this.marquun6sn4f(var2x -> this.m6jhviv6f09r(var2, var2x));
      this.ff3g1h4q7tfg.add(() -> {
         if (!var3.focused()) {
            var3.text(mbns3mp5wpnx(this.m5s0ch7dk74h(var2, this.m5s0ch7dk74h("cornerRadius", Float.intBitsToFloat(1082130432)))));
         }
      });
      return this.mc5uuf6rmafd(var1, var3);
   }

   private LayoutContainerNode m4ngqca316ch(String var1, String var2, boolean var3) {
      ControlCompactService var4 = new ControlCompactService().value(var3).activeColor(this.fh22dz4o6gb0).onToggle(var2x -> this.m8qdwolun1w(var2, var2x));
      this.ff3g1h4q7tfg.add(() -> var4.value(this.m1igc5airhxn(var2, var3)));
      return this.m727m7468ysd(var1, var4);
   }

   private LayoutContainerNode mapc0ng175le(String var1, String var2, boolean var3, LayoutContainerNode var4) {
      return this.m28byo25cjtk(var1, var2, var3, var4, null);
   }

   private LayoutContainerNode m28byo25cjtk(String var1, String var2, boolean var3, LayoutContainerNode var4, Consumer<Boolean> var5) {
      ControlCompactService var6 = new ControlCompactService().value(var3).activeColor(this.fh22dz4o6gb0).onToggle(var4x -> {
         this.m8qdwolun1w(var2, var4x);
         var4.visible(var4x);
         if (var5 != null) {
            var5.accept(var4x);
         }
      });
      this.ff3g1h4q7tfg.add(() -> {
         boolean var5x = this.m1igc5airhxn(var2, var3);
         var6.value(var5x);
         var4.visible(var5x);
      });
      var4.visible(var3);
      LayoutContainerNode var7 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
      var7.addChild(this.m727m7468ysd(var1, var6));
      var7.addChild(var4);
      return var7;
   }

   private LayoutContainerNode mhx5vlavd5a3(String var1, LayoutContainerNode var2) {
      LayoutContainerNode var3 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      var3.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      var3.addChild(var2);
      return var3;
   }

   private static LayoutContainerNode mftjrxqx4ezo(float var0) {
      return new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(var0);
   }

   private void m25b92c3z0yx(LayoutIsContainerService var1) {
      if (var1.props == null) {
         var1.props = new JsonObject();
      }
   }

   private void m4utfnbc8jwd(String var1, float var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         this.m25b92c3z0yx(var3);
         var3.props.addProperty(var1, var2);
      }
   }

   private void m8qdwolun1w(String var1, boolean var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         this.m25b92c3z0yx(var3);
         var3.props.addProperty(var1, var2);
      }
   }

   private void m4hru6zgdqyy(String var1, int var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         this.m25b92c3z0yx(var3);
         var3.props.addProperty(var1, String.format("#%08X", var2));
      }
   }

   private void m6jhviv6f09r(String var1, String var2) {
      try {
         float var3 = Float.parseFloat(var2.trim());
         if (Float.isFinite(var3)) {
            this.m4utfnbc8jwd(var1, var3);
         }
      } catch (Exception var4) {
      }
   }

   private void m6mrtet695hg(String var1, String var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         this.m25b92c3z0yx(var3);
         var3.props.addProperty(var1, var2);
      }
   }

   private float m5s0ch7dk74h(String var1, float var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      return var3 != null ? LayoutStringService.number(var3.props, var1, var2) : var2;
   }

   private int m6vslb70e5uc(String var1, int var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      return var3 != null ? LayoutStringService.color(var3.props, var1, var2) : var2;
   }

   private boolean m1igc5airhxn(String var1, boolean var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      return var3 != null ? LayoutStringService.bool(var3.props, var1, var2) : var2;
   }

   private String mezqlymtb7ba(String var1, String var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      return var3 != null ? LayoutStringService.string(var3.props, var1, var2) : var2;
   }

   private static String m6614bn3b4vx(float var0, boolean var1) {
      return mj40fnfhfd0y(var0, var1 ? -1 : 2);
   }

   private static String mj40fnfhfd0y(float var0, int var1) {
      return var1 < 0 ? String.valueOf(Math.round(var0)) : String.format(Locale.ROOT, "%." + var1 + "f", var0);
   }

   private void mfiwpn4wknjg(LayoutIsContainerService var1) {
      for (Runnable var3 : this.f75csd7dbp6l) {
         var3.run();
      }

      this.fhp9h6gpevng = this.fdv6femko4bf = "";

      for (ColourComponent var5 : this.fhc6hu0todpb) {
         var5.close();
      }
   }

   private LayoutContainerNode mil7znabye0v() {
      this.ff3g1h4q7tfg = this.fds2fqndpgdh;
      this.f9xxgz6cr89j = this.f77ef2glx8wk;
      this.fds2fqndpgdh.clear();
      this.f77ef2glx8wk.clear();
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
      var1.addChild(this.mi8augwbkvkf());
      var1.addChild(
         this.mb1btrwgujbo(
            "CHIP SIZE", "size", Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1111490560), 1.0F, Float.intBitsToFloat(1104150528), true
         )
      );
      var1.addChild(this.mb1btrwgujbo("GAP", "gap", 0.0F, Float.intBitsToFloat(1098907648), 1.0F, Float.intBitsToFloat(1084227584), true));
      var1.addChild(this.mgu3yar4wwnz("ACCENT", "accent", -10262799));
      var1.addChild(this.m4ngqca316ch("Show empty slots", "showEmpty", false));
      var1.addChild(this.m4ngqca316ch("Durability bar", "durabilityBar", true));
      return var1;
   }

   private LayoutContainerNode mi8augwbkvkf() {
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("ORIENTATION", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1105199104))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1086324736));
      this.f1qx0m3ot2qg = this.m7ni4jvjd5ai("Horizontal", "horizontal");
      this.fexrk9yloox5 = this.m7ni4jvjd5ai("Vertical", "vertical");
      var2.addChild(this.f1qx0m3ot2qg);
      var2.addChild(this.fexrk9yloox5);
      var1.addChild(var2);
      this.ff3g1h4q7tfg.add(this::mc7ojefr1dgm);
      return var1;
   }

   private SceneCornerRadiusService m7ni4jvjd5ai(String var1, String var2) {
      SceneCornerRadiusService var3 = new SceneCornerRadiusService()
         .flex(1.0F)
         .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1105199104))
         .cornerRadius(Float.intBitsToFloat(1090519040))
         .backgroundColor(452984831)
         .gradientEnd(352321535)
         .hoverBackground(385875967)
         .border(Float.intBitsToFloat(1056964608), 452984831)
         .secondaryBorderColor(268435456)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> {
            this.m6mrtet695hg("orientation", var2);
            this.mc7ojefr1dgm();
         });
      var3.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD));
      return var3;
   }

   private void mc7ojefr1dgm() {
      if (this.f1qx0m3ot2qg != null && this.fexrk9yloox5 != null) {
         int var1 = !"vertical".equalsIgnoreCase(this.mezqlymtb7ba("orientation", "horizontal")) ? 1 : 0;
         this.f1qx0m3ot2qg.animateColor("backgroundColor", var1 != 0 ? this.fh22dz4o6gb0 : 452984831, SceneEaseHandler.Spring.SNAPPY);
         this.fexrk9yloox5.animateColor("backgroundColor", var1 != 0 ? 452984831 : this.fh22dz4o6gb0, SceneEaseHandler.Spring.SNAPPY);
      }
   }

   private void m617xfi6y4iv(LayoutIsContainerService var1) {
      for (Runnable var3 : this.fds2fqndpgdh) {
         var3.run();
      }

      for (ColourComponent var5 : this.f77ef2glx8wk) {
         var5.close();
      }
   }

   private LayoutContainerNode m4g9a2wkfxyf() {
      this.ff3g1h4q7tfg = this.f3xssi477ala;
      this.f9xxgz6cr89j = this.fyh7bfyp88z;
      this.f3xssi477ala.clear();
      this.fyh7bfyp88z.clear();
      LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
      var1.addChild(
         this.mb1btrwgujbo("KEY SIZE", "size", Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1107296256), 1.0F, Float.intBitsToFloat(1098907648), true)
      );
      var1.addChild(this.mb1btrwgujbo("GAP", "gap", 0.0F, Float.intBitsToFloat(1092616192), 1.0F, Float.intBitsToFloat(1077936128), true));
      var1.addChild(
         this.mb1btrwgujbo(
            "RADIUS", "cornerRadius", 0.0F, Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1056964608), Float.intBitsToFloat(1082130432), false
         )
      );
      var1.addChild(this.mb1btrwgujbo("BLUR", "blur", 0.0F, Float.intBitsToFloat(1106247680), 1.0F, 0.0F, true));
      this.feemtwsjsd4d = new FONTComponent(
         this.fbp8noz2yf8w, this.flgozpodfet, CoreIsInitializedHandler.get().compositor(), this.fh22dz4o6gb0, this::mea8ljk2rbik
      );
      var1.addChild(this.feemtwsjsd4d.node());
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1092616192));
      var2.addChild(this.m4ngqca316ch("Mouse", "showMouse", true));
      var2.addChild(this.m4ngqca316ch("Space", "showSpace", true));
      var1.addChild(this.mhx5vlavd5a3("KEYS", var2));
      var1.addChild(this.m4ngqca316ch("Liquid shader", "liquid", true));
      var1.addChild(this.mgu3yar4wwnz("IDLE", "idleColor", 1713381424));
      var1.addChild(this.mgu3yar4wwnz("PRESSED", "pressColor", -530882070));
      var1.addChild(this.mgu3yar4wwnz("TEXT", "textColor", -218103809));
      var1.addChild(this.mgu3yar4wwnz("ACCENT / GLOW", "accent", -10262799));
      return var1;
   }

   private void mhh5jzb2c6ut(LayoutIsContainerService var1) {
      for (Runnable var3 : this.f3xssi477ala) {
         var3.run();
      }

      for (ColourComponent var5 : this.fyh7bfyp88z) {
         var5.close();
      }
   }

   private void m5cuce8czb34(LayoutIsContainerService var1) {
      if (this.f4gxtve49sah != null && !this.f4gxtve49sah.focused()) {
         String var2 = var1.width == Float.intBitsToFloat(-1082130432) ? "auto" : mbns3mp5wpnx(var1.width);
         if (!var2.equals(this.fhp9h6gpevng)) {
            this.fhp9h6gpevng = var2;
            this.f4gxtve49sah.text(var2);
         }
      }

      if (this.fd18idlubdkk != null && !this.fd18idlubdkk.focused()) {
         String var3 = var1.height == Float.intBitsToFloat(-1082130432) ? "auto" : mbns3mp5wpnx(var1.height);
         if (!var3.equals(this.fdv6femko4bf)) {
            this.fdv6femko4bf = var3;
            this.fd18idlubdkk.text(var3);
         }
      }

      if (this.f96dfdn55rhr != null && !this.f96dfdn55rhr.isPressed()) {
         float var4 = LayoutStringService.number(var1.props, "cornerRadius", Float.intBitsToFloat(1082130432));
         if (Math.abs(var4 - this.f96dfdn55rhr.value()) > Float.intBitsToFloat(1008981770)) {
            this.f96dfdn55rhr.value(var4);
            if (this.fcp6k70oa7q != null) {
               this.fcp6k70oa7q.text(m6614bn3b4vx(var4, false));
            }
         }
      }
   }

   private LayoutContainerNode mgzdpf0x6ikt() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1094713344));
      var1.addChild(this.mbfu3iaxadx0());
      var1.addChild(this.miudbxrqxvwf());
      var1.addChild(this.m4jmtyqd5gd5());
      return var1;
   }

   private LayoutContainerNode miudbxrqxvwf() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("STYLE", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1104150528))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1086324736));
      this.f1oxmbxecj6 = this.m2fyhkjdj9cq("B", TextMode.BOLD, () -> this.mecqs0fj38q8(true, false));
      this.fbv5aezu4d6d = this.m2fyhkjdj9cq("I", TextMode.ITALIC, () -> this.mecqs0fj38q8(false, true));
      this.f9ailxqrx9w5 = this.m2fyhkjdj9cq("U", TextMode.REGULAR, this::m9015cq9c5wh);
      var2.addChild(this.f1oxmbxecj6);
      var2.addChild(this.fbv5aezu4d6d);
      var2.addChild(this.f9ailxqrx9w5);
      var1.addChild(var2);
      return var1;
   }

   private SceneCornerRadiusService m2fyhkjdj9cq(String var1, TextMode var2, Runnable var3) {
      SceneCornerRadiusService var4 = new SceneCornerRadiusService()
         .flex(1.0F)
         .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1104150528))
         .cornerRadius(Float.intBitsToFloat(1090519040))
         .backgroundColor(452984831)
         .gradientEnd(352321535)
         .hoverBackground(385875967)
         .border(Float.intBitsToFloat(1056964608), 822083583)
         .secondaryBorderColor(301989888)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(var3);
      var4.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1092616192), -553648129, var2));
      return var4;
   }

   private LayoutContainerNode m4jmtyqd5gd5() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("COLOR", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      this.f6lp4depep2p = new ColourComponent(this.fbp8noz2yf8w, this::m1h5ytvwbtif);
      var1.addChild(this.f6lp4depep2p.node());
      return var1;
   }

   private void mecqs0fj38q8(boolean var1, boolean var2) {
      LayoutIsContainerService var3 = this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
      if (var3 != null) {
         if (var3.props == null) {
            var3.props = new JsonObject();
         }

         String var4 = LayoutStringService.string(var3.props, "variant", "regular").toLowerCase();
         boolean var5 = var4.contains("bold");
         boolean var6 = var4.contains("italic");
         if (var1) {
            var5 = !var5;
         }

         if (var2) {
            var6 = !var6;
         }

         var3.props.addProperty("variant", var5 && var6 ? "bolditalic" : (var5 ? "bold" : (var6 ? "italic" : "regular")));
         this.m4830d28wyrn(var3);
      }
   }

   private void m9015cq9c5wh() {
      LayoutIsContainerService var1 = this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
      if (var1 != null) {
         if (var1.props == null) {
            var1.props = new JsonObject();
         }

         var1.props.addProperty("underline", !LayoutStringService.bool(var1.props, "underline", false));
         this.m4830d28wyrn(var1);
      }
   }

   private void m1h5ytvwbtif(int var1) {
      LayoutIsContainerService var2 = this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
      if (var2 != null) {
         if (var2.props == null) {
            var2.props = new JsonObject();
         }

         var2.props.addProperty("color", String.format("#%08X", Integer.valueOf(var1)));
      }
   }

   private void m4830d28wyrn(LayoutIsContainerService var1) {
      String var2 = LayoutStringService.string(var1.props, "variant", "regular").toLowerCase();
      this.mjo2ijh5h2ur(this.f1oxmbxecj6, var2.contains("bold"));
      this.mjo2ijh5h2ur(this.fbv5aezu4d6d, var2.contains("italic"));
      this.mjo2ijh5h2ur(this.f9ailxqrx9w5, LayoutStringService.bool(var1.props, "underline", false));
   }

   private void mjo2ijh5h2ur(SceneCornerRadiusService var1, boolean var2) {
      if (var1 != null) {
         int var3 = this.fh22dz4o6gb0 & 16777215 | 1073741824;
         var1.animateColor("backgroundColor", var2 ? var3 : 452984831, SceneEaseHandler.Spring.SNAPPY);
         var1.animateColor("gradientEnd", var2 ? var3 : 352321535, SceneEaseHandler.Spring.SNAPPY);
         var1.animateColor("borderColor", var2 ? this.fh22dz4o6gb0 : 822083583, SceneEaseHandler.Spring.SNAPPY);
      }
   }

   private LayoutContainerNode macrp7h9n1d9() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1094713344));
      var1.addChild(this.mgtjt5upsc9());
      var1.addChild(this.mcolccs039oa());
      var1.addChild(this.mfqwo6hve0a1());
      var1.addChild(this.mh3qp829m4w0());
      return var1;
   }

   private LayoutContainerNode mgtjt5upsc9() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.START)
         .gap(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("ANCHOR", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      SceneCornerRadiusService var2 = new SceneCornerRadiusService()
         .size(Float.intBitsToFloat(1115947008), Float.intBitsToFloat(1115947008))
         .cornerRadius(Float.intBitsToFloat(1090519040))
         .backgroundColor(855638016)
         .border(Float.intBitsToFloat(1056964608), 822083583)
         .secondaryBorderColor(301989888)
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .justify(ScenePctService.Justify.START);

      for (int var3 = 0; var3 < 3; var3++) {
         LayoutContainerNode var4 = new LayoutContainerNode()
            .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
            .flex(1.0F)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START);

         for (int var5 = 0; var5 < 3; var5++) {
            var4.addChild(this.meicsxqo3rkx(var5, var3));
         }

         var2.addChild(var4);
      }

      var1.addChild(var2);
      return var1;
   }

   private LayoutContainerNode meicsxqo3rkx(int var1, int var2) {
      LayoutContainerNode var3 = new LayoutContainerNode()
         .flex(1.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(() -> this.man037y32122(var1, var2));
      SceneCornerRadiusService var4 = new SceneCornerRadiusService()
         .size(Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584))
         .cornerRadius(Float.intBitsToFloat(1075838976))
         .backgroundColor(1090519039);
      this.fherocc325dv[var2 * 3 + var1] = var4;
      var3.addChild(var4);
      return var3;
   }

   private void man037y32122(int var1, int var2) {
      LayoutIsContainerService var3 = this.m8aefs6eqlgs();
      if (var3 != null) {
         LayoutCodec.H var4 = var1 == 0 ? LayoutCodec.H.LEFT : (var1 == 1 ? LayoutCodec.H.CENTER : LayoutCodec.H.RIGHT);
         LayoutCodec.V var5 = var2 == 0 ? LayoutCodec.V.TOP : (var2 == 1 ? LayoutCodec.V.CENTER : LayoutCodec.V.BOTTOM);
         LayoutCodec var6 = new LayoutCodec(var4, var5);
         if (!var6.equals(var3.anchor)) {
            var3.anchor = var6;
            var3.offsetX = 0.0F;
            var3.offsetY = 0.0F;
            this.f9sg2aat5v1n = this.fbknte11kewb = "";
         }

         this.m4c6wcyvtv9z(var3);
      }
   }

   private void m4c6wcyvtv9z(LayoutIsContainerService var1) {
      int var2 = var1.anchor.v.ordinal() * 3 + var1.anchor.h.ordinal();

      for (int var3 = 0; var3 < 9; var3++) {
         SceneCornerRadiusService var4 = this.fherocc325dv[var3];
         if (var4 != null) {
            int var5 = var3 == var2 ? 1 : 0;
            var4.animateColor("backgroundColor", var5 != 0 ? this.fh22dz4o6gb0 : 1090519039, SceneEaseHandler.Spring.SNAPPY);
            var4.animate("width", var5 != 0 ? Float.intBitsToFloat(1088421888) : Float.intBitsToFloat(1084227584), SceneEaseHandler.Spring.SNAPPY);
            var4.animate("height", var5 != 0 ? Float.intBitsToFloat(1088421888) : Float.intBitsToFloat(1084227584), SceneEaseHandler.Spring.SNAPPY);
         }
      }
   }

   private LayoutContainerNode mcolccs039oa() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(Float.intBitsToFloat(1084227584));
      var1.addChild(mejww8vrb7gl("OFFSET", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1090519040));
      this.f8adl4jpxtnn = this.marquun6sn4f(this::m1krpy0p8v4p);
      this.fhvosy6eh2ev = this.marquun6sn4f(this::m8g1qr5o0o4o);
      var2.addChild(this.mc5uuf6rmafd("X", this.f8adl4jpxtnn));
      var2.addChild(this.mc5uuf6rmafd("Y", this.fhvosy6eh2ev));
      var1.addChild(var2);
      return var1;
   }

   private ControlLetterSpacingService marquun6sn4f(Consumer<String> var1) {
      ControlLetterSpacingService var2 = new ControlLetterSpacingService()
         .fontSize(Float.intBitsToFloat(1091567616))
         .textColor(-553648129)
         .bgColor(452984831)
         .focusBorder(this.fh22dz4o6gb0)
         .cornerRadius(Float.intBitsToFloat(1088421888))
         .maxLength(8)
         .onChanged(var1);
      var2.size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1103101952)).flex(1.0F);
      return var2;
   }

   private LayoutContainerNode mc5uuf6rmafd(String var1, ControlLetterSpacingService var2) {
      LayoutContainerNode var3 = new LayoutContainerNode()
         .flex(1.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1084227584));
      var3.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1090519040), 1795162111, TextMode.BOLD));
      var3.addChild(var2);
      return var3;
   }

   private void m1krpy0p8v4p(String var1) {
      LayoutIsContainerService var2 = this.m8aefs6eqlgs();
      if (var2 != null) {
         try {
            float var3 = Float.parseFloat(var1.trim());
            if (Float.isFinite(var3)) {
               var2.offsetX = var3;
            }
         } catch (Exception var4) {
         }
      }
   }

   private void m8g1qr5o0o4o(String var1) {
      LayoutIsContainerService var2 = this.m8aefs6eqlgs();
      if (var2 != null) {
         try {
            float var3 = Float.parseFloat(var1.trim());
            if (Float.isFinite(var3)) {
               var2.offsetY = var3;
            }
         } catch (Exception var4) {
         }
      }
   }

   private void m2vtm8enxq6e(LayoutIsContainerService var1) {
      if (this.f8adl4jpxtnn != null && !this.f8adl4jpxtnn.focused()) {
         String var2 = mbns3mp5wpnx(var1.offsetX);
         if (!var2.equals(this.f9sg2aat5v1n)) {
            this.f9sg2aat5v1n = var2;
            this.f8adl4jpxtnn.text(var2);
         }
      }

      if (this.fhvosy6eh2ev != null && !this.fhvosy6eh2ev.focused()) {
         String var3 = mbns3mp5wpnx(var1.offsetY);
         if (!var3.equals(this.fbknte11kewb)) {
            this.fbknte11kewb = var3;
            this.fhvosy6eh2ev.text(var3);
         }
      }
   }

   private static String mbns3mp5wpnx(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private LayoutContainerNode mfqwo6hve0a1() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.COLUMN)
         .align(ScenePctService.Align.STRETCH)
         .gap(Float.intBitsToFloat(1084227584));
      LayoutContainerNode var2 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN);
      var2.addChild(mejww8vrb7gl("OPACITY", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD).marginLeft(2.0F));
      this.fadywccw1j68 = mejww8vrb7gl("1.00", Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
      var2.addChild(this.fadywccw1j68);
      var1.addChild(var2);
      this.fdgd7d15rksl = new CompactSliderControl(0.0F, 1.0F, Float.intBitsToFloat(1008981770), 1.0F, this.fh22dz4o6gb0, this.f3dscnabklqo, var1x -> {
         LayoutIsContainerService var2x = this.m8aefs6eqlgs();
         if (var2x != null) {
            var2x.opacity = var1x;
         }
      });
      var1.addChild(this.fdgd7d15rksl);
      return var1;
   }

   private LayoutContainerNode mh3qp829m4w0() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1092616192));
      this.f7q6hgnuza9v = new ControlCompactService().value(true).activeColor(this.fh22dz4o6gb0).onToggle(var1x -> {
         LayoutIsContainerService var2 = this.m8aefs6eqlgs();
         if (var2 != null) {
            var2.visible = var1x;
         }
      });
      this.f99y1vdyi106 = new ControlCompactService().value(false).activeColor(this.fh22dz4o6gb0).onToggle(var1x -> {
         LayoutIsContainerService var2 = this.m8aefs6eqlgs();
         if (var2 != null) {
            var2.locked = var1x;
         }
      });
      var1.addChild(this.m727m7468ysd("Visible", this.f7q6hgnuza9v));
      var1.addChild(this.m727m7468ysd("Lock", this.f99y1vdyi106));
      return var1;
   }

   private LayoutContainerNode m727m7468ysd(String var1, ControlCompactService var2) {
      var2.size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1099431936));
      LayoutContainerNode var3 = new LayoutContainerNode()
         .flex(1.0F)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.SPACE_BETWEEN)
         .gap(Float.intBitsToFloat(1086324736));
      var3.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1091043328), -553648129, TextMode.REGULAR));
      var3.addChild(var2);
      return var3;
   }

   private void m8yrcshpa2my(LayoutIsContainerService var1) {
      this.m4c6wcyvtv9z(var1);
      if (this.fdgd7d15rksl != null) {
         this.fdgd7d15rksl.value(var1.opacity);
      }

      if (this.f7q6hgnuza9v != null) {
         this.f7q6hgnuza9v.value(var1.visible);
      }

      if (this.f99y1vdyi106 != null) {
         this.f99y1vdyi106.value(var1.locked);
      }

      this.f9sg2aat5v1n = this.fbknte11kewb = "";
   }

   private LayoutIsContainerService m8aefs6eqlgs() {
      return this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
   }

   private float m5mpk037zng9() {
      Window var1 = Minecraft.getInstance().getWindow();
      return var1 != null ? var1.getGuiScaledWidth() : 0.0F;
   }

   private float miu2y8qvnfho() {
      Window var1 = Minecraft.getInstance().getWindow();
      return var1 != null ? var1.getGuiScaledHeight() : 0.0F;
   }

   private LayoutContainerNode me2fl17o23av() {
      LayoutContainerNode var1 = new LayoutContainerNode()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(-1082130432))
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.START)
         .gap(Float.intBitsToFloat(1090519040));
      var1.addChild(this.m6c5uc6vttau("Copy style", this::mfewt8wll2g7));
      var1.addChild(this.m6c5uc6vttau("Paste style", this::m97h221y6tr2));
      return var1;
   }

   private SceneCornerRadiusService m6c5uc6vttau(String var1, Runnable var2) {
      SceneCornerRadiusService var3 = new SceneCornerRadiusService()
         .flex(1.0F)
         .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1106247680))
         .cornerRadius(Float.intBitsToFloat(1091567616))
         .backgroundColor(251658239)
         .hoverBackground(385875967)
         .border(Float.intBitsToFloat(1056964608), 452984831)
         .secondaryBorderColor(268435456)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(var2);
      var3.addChild(mejww8vrb7gl(var1, Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD));
      return var3;
   }

   private SceneCornerRadiusService m2f1jbj0xjvf() {
      SceneCornerRadiusService var1 = new SceneCornerRadiusService()
         .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), Float.intBitsToFloat(1108344832))
         .cornerRadius(Float.intBitsToFloat(1092616192))
         .backgroundColor(384779332)
         .gradientEnd(250561604)
         .hoverBackground(753878084)
         .pressBackground(1190085700)
         .border(Float.intBitsToFloat(1056964608), 1307526212)
         .secondaryBorderColor(402653184)
         .direction(ScenePctService.Direction.ROW)
         .align(ScenePctService.Align.CENTER)
         .justify(ScenePctService.Justify.CENTER)
         .gap(Float.intBitsToFloat(1088421888))
         .cursorStyle(ScenePctService.CursorStyle.POINTER)
         .onClick(this::mbcinx574d3f);
      var1.addChild(new SceneSrcService().src(f41taspqavp9).size(Float.intBitsToFloat(1095761920), Float.intBitsToFloat(1095761920)).tintColor(-25958));
      var1.addChild(mejww8vrb7gl("Delete", Float.intBitsToFloat(1092091904), -25958, TextMode.BOLD));
      return var1;
   }

   private void mbcinx574d3f() {
      if (this.fetqhndloi5c != null && this.f2prt6jl4bc != null) {
         List var1 = this.fetqhndloi5c.selection();
         if (!var1.isEmpty()) {
            this.f2prt6jl4bc.elements.removeAll(var1);
            this.fetqhndloi5c.deselect();
            this.fbkjdm8rmyhv = false;
            this.ff0i2lnbhktw = false;
            this.fipp48a3nif5 = null;
            this.fb84je10o0l8 = false;
            this.fbpg4ilk69c9 = null;
            this.f8ze0ngtip07 = false;
            this.fi06ijkdbakq = null;
            this.f40bk6yc2zqk = false;
            this.fg8obau2afpd = null;
            if (this.f6lp4depep2p != null) {
               this.f6lp4depep2p.close();
            }

            if (this.f87wbk15r6mn != null) {
               this.f87wbk15r6mn.close();
            }

            if (this.feemtwsjsd4d != null) {
               this.feemtwsjsd4d.close();
            }

            for (ColourComponent var3 : this.fhc6hu0todpb) {
               var3.close();
            }

            for (ColourComponent var6 : this.f77ef2glx8wk) {
               var6.close();
            }

            for (ColourComponent var7 : this.fyh7bfyp88z) {
               var7.close();
            }

            if (this.f6y70fw856pa != null) {
               this.f6y70fw856pa.visible(false);
            }

            if (this.fga9wawkp0sx != null) {
               this.fga9wawkp0sx.visible(false);
            }

            if (this.f6s8d99hz8qx != null) {
               this.f6s8d99hz8qx.visible(false);
            }

            if (this.f8xxf9r3gts3 != null) {
               this.f8xxf9r3gts3.visible(true);
            }

            if (this.fjkjf66g3q82 != null) {
               this.fjkjf66g3q82.visible(false);
            }
         }
      }
   }

   @Override
   public boolean keyPressed(ScreenScreenIdService var1, int var2, int var3) {
      if (CoreIsInitializedHandler.get().scene().hasFocusedInput()) {
         return false;
      }

      int var4 = (var3 & 2) == 0 && (var3 & 8) == 0 ? 0 : 1;
      int var5 = (var3 & 4) != 0 ? 1 : 0;
      switch (var2) {
         case 65:
            if (var4 != 0) {
               this.m25ijys3uns9();
               return true;
            }
            break;
         case 67:
            if (var4 != 0 && var5 != 0) {
               this.mfewt8wll2g7();
               return true;
            }
            break;
         case 83:
            if (var4 != 0) {
               this.mcwatwgehpgo();
               return true;
            }
            break;
         case 86:
            if (var4 != 0 && var5 != 0) {
               this.m97h221y6tr2();
               return true;
            }
            break;
         case 259:
         case 261:
            if (this.fetqhndloi5c != null && !this.fetqhndloi5c.selection().isEmpty()) {
               this.mbcinx574d3f();
               return true;
            }
      }

      return false;
   }

   private void m25ijys3uns9() {
      if (this.fetqhndloi5c != null && this.f2prt6jl4bc != null) {
         this.fetqhndloi5c.deselect();

         for (LayoutIsContainerService var2 : this.f2prt6jl4bc.elements) {
            if (var2.visible) {
               this.fetqhndloi5c.addToSelection(var2);
            }
         }
      }
   }

   private void mcwatwgehpgo() {
      if (this.f2prt6jl4bc != null) {
         Path var1 = LayoutRepository.fileFor(FabricLoader.getInstance().getGameDir(), "active");
         LayoutRepository.save(var1, this.f2prt6jl4bc);
         if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
            CoreIsInitializedHandler.get().config().save();
         }

         this.m4lnhdub9qli("Saved");
      }
   }

   private void mfewt8wll2g7() {
      LayoutIsContainerService var1 = this.m8aefs6eqlgs();
      if (var1 != null) {
         JsonObject var2 = (var1.props != null ? var1.props : new JsonObject()).deepCopy();
         var2.remove("text");
         this.ftxajmi19ob = var2;
         this.fgw3tbkd8bmp = var1.opacity;
         this.m4lnhdub9qli("Style copied");
      }
   }

   private void m97h221y6tr2() {
      if (this.ftxajmi19ob != null && this.fetqhndloi5c != null) {
         List var1 = this.fetqhndloi5c.selection();
         if (!var1.isEmpty()) {
            for (LayoutIsContainerService var3 : (Iterable<LayoutIsContainerService>) (Iterable<?>) (var1)) {
               if (var3.props == null) {
                  var3.props = new JsonObject();
               }

               for (Entry var5 : this.ftxajmi19ob.entrySet()) {
                  var3.props.add((String)var5.getKey(), ((JsonElement)var5.getValue()).deepCopy());
               }

               var3.opacity = this.fgw3tbkd8bmp;
            }

            this.f73b914k7qrv = null;
            this.fipp48a3nif5 = null;
            this.fbpg4ilk69c9 = null;
            this.fi06ijkdbakq = null;
            this.fg8obau2afpd = null;
            this.m4lnhdub9qli("Style pasted");
         }
      }
   }

   private void m4lnhdub9qli(String var1) {
      if (this.ffyp031lzdhl != null) {
         this.ffyp031lzdhl.text(var1);
         this.ffyp031lzdhl.opacity(1.0F);
         this.f4r23sa6lvd9 = System.currentTimeMillis() + 1400L;
      }
   }

   private static String m6z84l3mpji(LayoutIsContainerService var0) {
      if (var0.type != null) {
         for (String[] var4 : fe0y5493cdq5) {
            if (var4[0].equals(var0.type)) {
               return var4[1];
            }
         }
      }

      return var0.type != null ? var0.type : "Element";
   }

   private SceneTextService mcvucas6k1zq(String var1) {
      return mejww8vrb7gl(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
         .marginLeft(Float.intBitsToFloat(1086324736))
         .marginBottom(Float.intBitsToFloat(1082130432));
   }

   @Override
   public void tick(ScreenScreenIdService var1) {
      if (!this.fc4scvr07kj6.isEmpty()) {
         float var2 = var1.deltaTime();
         Iterator var3 = this.fc4scvr07kj6.iterator();

         while (var3.hasNext()) {
            HudEditorScreen.PendingAnim var4 = (HudEditorScreen.PendingAnim)var3.next();
            var4.delay -= var2;
            if (var4.delay <= 0.0F) {
               var4.node.animate("opacity", 1.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1048911544)));
               var3.remove();
            }
         }
      }

      if (this.fdqwnrs1i4ii != null && this.fetqhndloi5c != null) {
         float var10 = this.fetqhndloi5c.zoom();
         String var14 = var10 <= Float.intBitsToFloat(1065437102) ? "Fit" : Math.round(var10 * Float.intBitsToFloat(1120403456)) + "%";
         if (!var14.equals(this.f3qwjbmavez9)) {
            this.f3qwjbmavez9 = var14;
            this.fdqwnrs1i4ii.text(var14);
         }
      }

      if (this.fetqhndloi5c != null) {
         LayoutIsContainerService var11 = this.fetqhndloi5c.selected();
         boolean var15 = var11 != null;
         if (var15 != this.fbkjdm8rmyhv) {
            this.fbkjdm8rmyhv = var15;
            if (this.f8xxf9r3gts3 != null) {
               this.f8xxf9r3gts3.visible(!var15);
            }

            if (this.fjkjf66g3q82 != null) {
               this.fjkjf66g3q82.visible(var15);
            }
         }

         if (var15 && this.fb7v7dchg6qx != null) {
            String var16 = m6z84l3mpji(var11);
            if (!var16.equals(this.f9b79zzflau1)) {
               this.f9b79zzflau1 = var16;
               this.fb7v7dchg6qx.text(var16);
            }
         }

         if (var15) {
            if (var11 != this.f73b914k7qrv) {
               this.f73b914k7qrv = var11;
               this.m8yrcshpa2my(var11);
            }

            this.m2vtm8enxq6e(var11);
            if (this.fadywccw1j68 != null && this.fdgd7d15rksl != null) {
               String var17 = String.format(Locale.ROOT, "%.2f", this.fdgd7d15rksl.value());
               if (!var17.equals(this.fdpi7m01kxm1)) {
                  this.fdpi7m01kxm1 = var17;
                  this.fadywccw1j68.text(var17);
               }
            }
         } else {
            this.f73b914k7qrv = null;
         }

         boolean var18 = var15 && malgeiokaae(var11);
         if (var18 != this.ff0i2lnbhktw) {
            this.ff0i2lnbhktw = var18;
            if (this.f31v8ssb3qo7 != null) {
               this.f31v8ssb3qo7.visible(var18);
            }

            this.fipp48a3nif5 = null;
            if (!var18) {
               if (this.f6lp4depep2p != null) {
                  this.f6lp4depep2p.close();
               }

               if (this.f87wbk15r6mn != null) {
                  this.f87wbk15r6mn.close();
               }
            }
         }

         if (var18) {
            if (var11 != this.fipp48a3nif5) {
               this.fipp48a3nif5 = var11;
               if (this.f6lp4depep2p != null) {
                  this.f6lp4depep2p.close();
                  this.f6lp4depep2p.setColor(LayoutStringService.color(var11.props, "color", -1));
               }

               if (this.f87wbk15r6mn != null) {
                  this.f87wbk15r6mn.setSelection(LayoutStringService.string(var11.props, "font", ""));
               }

               this.m4830d28wyrn(var11);
            }
         } else {
            this.fipp48a3nif5 = null;
         }

         boolean var5 = var15 && m7vsrvzjfs8c(var11);
         if (var5 != this.fb84je10o0l8) {
            this.fb84je10o0l8 = var5;
            if (this.f6y70fw856pa != null) {
               this.f6y70fw856pa.visible(var5);
            }

            if (!var5) {
               for (ColourComponent var7 : this.fhc6hu0todpb) {
                  var7.close();
               }
            }

            this.fbpg4ilk69c9 = null;
         }

         if (var5) {
            if (var11 != this.fbpg4ilk69c9) {
               this.fbpg4ilk69c9 = var11;
               this.mfiwpn4wknjg(var11);
            }

            this.m5cuce8czb34(var11);
         } else {
            this.fbpg4ilk69c9 = null;
         }

         boolean var20 = var15 && "armorhud".equals(var11.type);
         if (var20 != this.f8ze0ngtip07) {
            this.f8ze0ngtip07 = var20;
            if (this.fga9wawkp0sx != null) {
               this.fga9wawkp0sx.visible(var20);
            }

            if (!var20) {
               for (ColourComponent var8 : this.f77ef2glx8wk) {
                  var8.close();
               }
            }

            this.fi06ijkdbakq = null;
         }

         if (var20) {
            if (var11 != this.fi06ijkdbakq) {
               this.fi06ijkdbakq = var11;
               this.m617xfi6y4iv(var11);
            }
         } else {
            this.fi06ijkdbakq = null;
         }

         boolean var22 = var15 && "keystrokes".equals(var11.type);
         if (var22 != this.f40bk6yc2zqk) {
            this.f40bk6yc2zqk = var22;
            if (this.f6s8d99hz8qx != null) {
               this.f6s8d99hz8qx.visible(var22);
            }

            if (!var22) {
               for (ColourComponent var9 : this.fyh7bfyp88z) {
                  var9.close();
               }

               if (this.feemtwsjsd4d != null) {
                  this.feemtwsjsd4d.close();
               }
            }

            this.fg8obau2afpd = null;
         }

         if (var22) {
            if (var11 != this.fg8obau2afpd) {
               this.fg8obau2afpd = var11;
               this.mhh5jzb2c6ut(var11);
               if (this.feemtwsjsd4d != null) {
                  this.feemtwsjsd4d.setSelection(LayoutStringService.string(var11.props, "font", ""));
               }
            }
         } else {
            this.fg8obau2afpd = null;
         }
      }

      if (this.f87wbk15r6mn != null) {
         this.f87wbk15r6mn.tick();
      }

      if (this.feemtwsjsd4d != null) {
         this.feemtwsjsd4d.tick();
      }

      if (this.fij1ukc3rqz8) {
         long var12 = CompatAdapterService.windowHandle(Minecraft.getInstance());
         int var19 = var12 != 0L && GLFW.glfwGetMouseButton(var12, 0) == 1 ? 1 : 0;
         if (var19 == 0) {
            this.m5zw901d7kqo();
         }
      }

      String var13 = this.m43oysyp9t2g();
      if (!var13.equals(this.f3un8o2fjumw)) {
         this.f3un8o2fjumw = var13;
         this.mbxn3u3dhfxk();
      }

      if (this.ffyp031lzdhl != null && this.f4r23sa6lvd9 > 0L && System.currentTimeMillis() > this.f4r23sa6lvd9) {
         this.f4r23sa6lvd9 = 0L;
         this.ffyp031lzdhl.animate("opacity", 0.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1053609165)));
      }
   }

   private String m43oysyp9t2g() {
      if (this.f2prt6jl4bc == null) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();

      for (LayoutIsContainerService var3 : this.f2prt6jl4bc.elements) {
         var1.append(System.identityHashCode(var3))
            .append((char)(var3.visible ? '1' : '0'))
            .append((char)(var3.locked ? '1' : '0'))
            .append((char)(this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var3) ? 'S' : '.'))
            .append(';');
      }

      var1.append("|d").append(this.fij1ukc3rqz8 ? System.identityHashCode(this.f3dzr20rwo5a) : 0);
      return var1.toString();
   }

   @Override
   public void onClose(ScreenScreenIdService var1) {
      this.fc4scvr07kj6.clear();
   }

   private static SceneTextService mejww8vrb7gl(String var0, float var1, int var2, TextMode var3) {
      SceneTextService var4 = new SceneTextService().text(var0).fontSize(var1).color(var2);
      if (var3 != null && var3 != TextMode.REGULAR) {
         var4.fontVariant(var3);
      }

      return var4;
   }

   private void mfxl2apq71o0(ScenePctService<?> var1, float var2) {
      this.fc4scvr07kj6.add(new HudEditorScreen.PendingAnim(var1, var2));
   }

   record Guide(boolean vertical, float pos, float a, float b) {
   }

   static final class HudPreviewNode extends ScenePctService<HudEditorScreen.HudPreviewNode> {
      private final HudEditorScreen.ScreenSampleNode ffsqhicehemw;
      private final LayoutFindSelector f5ya2y9vtox;
      private final LayoutRenderer fcng3yma8ozs;
      private final LayoutOperationHandler feyiy0zcotls;

      HudPreviewNode(HudEditorScreen.ScreenSampleNode var1, LayoutFindSelector var2, LayoutRenderer var3, LayoutOperationHandler var4) {
         this.ffsqhicehemw = var1;
         this.f5ya2y9vtox = var2;
         this.fcng3yma8ozs = var3;
         this.feyiy0zcotls = var4;
         this.layerBreak(true);
      }

      @Override
      protected void draw(CompositorPushPresentationScaleService var1) {
         if (this.f5ya2y9vtox != null && this.fcng3yma8ozs != null) {
            float var2 = this.ffsqhicehemw.fitW;
            if (!(var2 <= 0.0F) && !(this.ffsqhicehemw.fitH <= 0.0F)) {
               Window var3 = Minecraft.getInstance().getWindow();
               if (var3 != null) {
                  float var4 = var3.getGuiScaledWidth();
                  float var5 = var3.getGuiScaledHeight();
                  if (!(var4 <= 0.0F) && !(var5 <= 0.0F)) {
                     float var6 = var2 / var4;
                     this.fcng3yma8ozs.render(var1, this.f5ya2y9vtox, this.ffsqhicehemw.fitX, this.ffsqhicehemw.fitY, var4, var5, var6, this.feyiy0zcotls);
                  }
               }
            }
         }
      }
   }

   static final class LayerGripNode extends ScenePctService<HudEditorScreen.LayerGripNode> {
      private final int f2mbcgq8ypm3;
      private final int f8ts5oay7bkx;
      private final Consumer<Float> ff4s3a4cxzp7;
      private final Consumer<Float> finac0q15tm8;

      LayerGripNode(int var1, int var2, Consumer<Float> var3, Consumer<Float> var4) {
         this.f2mbcgq8ypm3 = var1;
         this.f8ts5oay7bkx = var2;
         this.ff4s3a4cxzp7 = var3;
         this.finac0q15tm8 = var4;
         this.interactive(true);
         this.stopPropagation(true);
         this.cursorStyle(ScenePctService.CursorStyle.POINTER);
      }

      @Override
      protected void onPress(float var1, float var2) {
         if (this.ff4s3a4cxzp7 != null) {
            this.ff4s3a4cxzp7.accept(var2);
         }
      }

      @Override
      protected void updateWhilePressed(float var1, float var2) {
         if (this.finac0q15tm8 != null) {
            this.finac0q15tm8.accept(var2);
         }
      }

      @Override
      protected void draw(CompositorPushPresentationScaleService var1) {
         float var2 = Math.min(this.cw * Float.intBitsToFloat(1058642330), Float.intBitsToFloat(1088421888));
         float var3 = Float.intBitsToFloat(1068708659);
         float var4 = Float.intBitsToFloat(1076258406);
         float var5 = this.cx + (this.cw - var2) * Float.intBitsToFloat(1056964608);
         float var6 = var3 * Float.intBitsToFloat(1077936128) + var4 * 2.0F;
         float var7 = this.cy + (this.ch - var6) * Float.intBitsToFloat(1056964608);
         int var8 = m51q9frqfkfu(!this.isHovered() && !this.isPressed() ? this.f2mbcgq8ypm3 : this.f8ts5oay7bkx, this.effectiveOpacity);

         for (int var9 = 0; var9 < 3; var9++) {
            var1.roundedRect(var5, var7 + var9 * (var3 + var4), var2, var3, var3 * Float.intBitsToFloat(1056964608), var8);
         }
      }

      private static int m51q9frqfkfu(int var0, float var1) {
         int var2 = Math.round((var0 >>> 24 & 0xFF) * Math.max(0.0F, Math.min(1.0F, var1)));
         return var2 << 24 | var0 & 16777215;
      }
   }

   static final class OverlayNode extends ScenePctService<HudEditorScreen.OverlayNode> {
      private static final int fcnimi9si2el = -1292135;
      private static final int f77cfzpsnkcv = -14494738;
      private final HudEditorScreen.ScreenSampleNode f526iiui911n;
      private final int f6i8h7zx5n7z;

      OverlayNode(HudEditorScreen.ScreenSampleNode var1, int var2) {
         this.f526iiui911n = var1;
         this.f6i8h7zx5n7z = var2;
         this.layerBreak(true);
      }

      @Override
      protected void draw(CompositorPushPresentationScaleService var1) {
         for (HudEditorScreen.Guide var3 : this.f526iiui911n.guides()) {
            if (var3.vertical()) {
               var1.roundedRect(var3.pos() - Float.intBitsToFloat(1056964608), var3.a(), 1.0F, var3.b() - var3.a(), 0.0F, -1292135);
            } else {
               var1.roundedRect(var3.a(), var3.pos() - Float.intBitsToFloat(1056964608), var3.b() - var3.a(), 1.0F, 0.0F, -1292135);
            }
         }

         for (HudEditorScreen.SpacingBadge var14 : this.f526iiui911n.spacingBadges()) {
            this.mdb97mkoeqop(var1, var14);
         }

         float var13 = Float.intBitsToFloat(1069547520);

         for (LayoutRenderer.Rect var4 : this.f526iiui911n.selectionScreenRects()) {
            var1.roundedRect(var4.x(), var4.y(), var4.w(), var13, 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var4.x(), var4.y() + var4.h() - var13, var4.w(), var13, 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var4.x(), var4.y(), var13, var4.h(), 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var4.x() + var4.w() - var13, var4.y(), var13, var4.h(), 0.0F, this.f6i8h7zx5n7z);
         }

         LayoutRenderer.Rect var16 = this.f526iiui911n.selectionSize() == 1 ? this.f526iiui911n.selectedScreenRect() : null;
         if (var16 != null && var16.w() > Float.intBitsToFloat(1101529088) && var16.h() > Float.intBitsToFloat(1101529088)) {
            float var17 = Float.intBitsToFloat(1090519040);
            float var5 = Float.intBitsToFloat(1084227584);

            for (int[] var9 : HudEditorScreen.fdv1pwgda7wj) {
               float var10 = var16.x() + (var9[0] + 1) * Float.intBitsToFloat(1056964608) * var16.w();
               float var11 = var16.y() + (var9[1] + 1) * Float.intBitsToFloat(1056964608) * var16.h();
               var1.roundedRect(
                  var10 - var17 * Float.intBitsToFloat(1056964608), var11 - var17 * Float.intBitsToFloat(1056964608), var17, var17, 2.0F, this.f6i8h7zx5n7z
               );
               var1.roundedRect(
                  var10 - var5 * Float.intBitsToFloat(1056964608),
                  var11 - var5 * Float.intBitsToFloat(1056964608),
                  var5,
                  var5,
                  Float.intBitsToFloat(1069547520),
                  -1
               );
            }
         }

         float[] var18 = this.f526iiui911n.marqueeRect();
         if (var18 != null && var18[2] > 1.0F && var18[3] > 1.0F) {
            var1.roundedRect(var18[0], var18[1], var18[2], var18[3], 1.0F, this.f6i8h7zx5n7z & 16777215 | 402653184);
            var1.roundedRect(var18[0], var18[1], var18[2], 1.0F, 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var18[0], var18[1] + var18[3] - 1.0F, var18[2], 1.0F, 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var18[0], var18[1], 1.0F, var18[3], 0.0F, this.f6i8h7zx5n7z);
            var1.roundedRect(var18[0] + var18[2] - 1.0F, var18[1], 1.0F, var18[3], 0.0F, this.f6i8h7zx5n7z);
         }
      }

      private void mdb97mkoeqop(CompositorPushPresentationScaleService var1, HudEditorScreen.SpacingBadge var2) {
         float var3 = Float.intBitsToFloat(1077936128);
         String var4 = String.valueOf(var2.dist());
         if (var2.horizontal()) {
            float var5 = var2.cross();
            float var6 = Math.min(var2.a0(), var2.a1());
            float var7 = Math.max(var2.a0(), var2.a1());
            var1.roundedRect(var6, var5 - Float.intBitsToFloat(1056964608), var7 - var6, 1.0F, 0.0F, -14494738);
            var1.roundedRect(var6, var5 - var3, 1.0F, var3 * 2.0F, 0.0F, -14494738);
            var1.roundedRect(var7 - 1.0F, var5 - var3, 1.0F, var3 * 2.0F, 0.0F, -14494738);
            var1.text(
               (var6 + var7) * Float.intBitsToFloat(1056964608) - var4.length() * Float.intBitsToFloat(1072902963),
               var5 - Float.intBitsToFloat(1092616192),
               var4,
               Float.intBitsToFloat(1087373312),
               -14494738
            );
         } else {
            float var8 = var2.cross();
            float var9 = Math.min(var2.a0(), var2.a1());
            float var10 = Math.max(var2.a0(), var2.a1());
            var1.roundedRect(var8 - Float.intBitsToFloat(1056964608), var9, 1.0F, var10 - var9, 0.0F, -14494738);
            var1.roundedRect(var8 - var3, var9, var3 * 2.0F, 1.0F, 0.0F, -14494738);
            var1.roundedRect(var8 - var3, var10 - 1.0F, var3 * 2.0F, 1.0F, 0.0F, -14494738);
            var1.text(
               var8 + Float.intBitsToFloat(1082130432),
               (var9 + var10) * Float.intBitsToFloat(1056964608) - Float.intBitsToFloat(1077936128),
               var4,
               Float.intBitsToFloat(1087373312),
               -14494738
            );
         }
      }
   }

   private static final class PendingAnim {
      final ScenePctService<?> node;
      float delay;

      PendingAnim(ScenePctService<?> var1, float var2) {
         this.node = var1;
         this.delay = var2;
      }
   }

   static final class ScreenSampleNode extends ScenePctService<HudEditorScreen.ScreenSampleNode> {
      float fitX;
      float fitY;
      float fitW;
      float fitH;
      private static final float fevjy84nfebj = 1.0F;
      private static final float f8pmdtqxk4mg = 8.0F;
      private static final float f7q1w1mol2ln = 22.0F;
      private static final float fe844rhcucxj = 6.0F;
      private static final float fdbp7cx1x60v = 7.0F;
      static final float HANDLE_MIN_SIZE = 21.0F;
      private static final float fgmsl43yh6bl = 4.0F;
      private static final float fedxhdvjhf1 = 3.0F;
      private static final int fde5btqeee4c = 0;
      private static final int fii4ang8ddej = 1;
      private static final int f9t7qaep0x92 = 2;
      private static final int f56wo43rwmli = 3;
      private static final int f4rhivxcwtqw = 4;
      private final LayoutFindSelector f6vwisjhq6u4;
      private final LayoutRenderer fb0r5ksfgwey;
      private final LayoutOperationHandler f2sa5kpgfxfw;
      private float f2pvoqpy9wed = 1.0F;
      private float f1xomqxzdozf = 1.0F;
      private float f9hzds9ynyos;
      private float feps9zkj6f8f;
      private float f57f5jfo42ur;
      private float fjczf73jxnln;
      private float f9vqxr571xac;
      private float fhmljivzxxri;
      private final LinkedHashSet<LayoutIsContainerService> f86kac7j5iju = new LinkedHashSet<>();
      private LayoutIsContainerService ffrgpgq4c34i;
      private int f87okhqyy2j4 = 0;
      private final List<LayoutIsContainerService> fbx67bnx0w41 = new ArrayList<>();
      private float[] fifme2nv3uxy;
      private float[] f4nm5dzw9x43;
      private float f11x5ia98nwz;
      private float fh5gdi83bvv2;
      private boolean fh2uz78r5nwt;
      private float fi6nm83jr75p;
      private float f95kvdxhy2zz;
      private float f2oiu6q5u1jn;
      private float fbwrn0fnh61l;
      private final LinkedHashSet<LayoutIsContainerService> f5ti9xthufjw = new LinkedHashSet<>();
      private final List<HudEditorScreen.Guide> fhmbzyzryl6c = new ArrayList<>();
      private final List<HudEditorScreen.SpacingBadge> f59fmxf7tn5k = new ArrayList<>();
      private int f5ros71ee6j4;
      private int f8ybl7c49ize;
      private float fdo7pt0uxf3;
      private float fc8uf86bf9a2;
      private float f5sv6cgpsx2d;
      private float f4hkexf84zu9;
      private float fb76wqc9913;
      private float f8slatb7zchf;
      private float f26n3h05ov89;
      private float f9ulsggmm0kc;
      private float f1ax835qkn9j;
      private float fhzvwpeozl9c;
      private boolean f5yxn2ejht3t;
      private boolean fczr8nr4xg4c;

      ScreenSampleNode(LayoutFindSelector var1, LayoutRenderer var2, LayoutOperationHandler var3) {
         this.f6vwisjhq6u4 = var1;
         this.fb0r5ksfgwey = var2;
         this.f2sa5kpgfxfw = var3;
      }

      float zoom() {
         return this.f2pvoqpy9wed;
      }

      LayoutIsContainerService selected() {
         return this.ffrgpgq4c34i;
      }

      List<LayoutIsContainerService> selection() {
         return new ArrayList<>(this.f86kac7j5iju);
      }

      int selectionSize() {
         return this.f86kac7j5iju.size();
      }

      boolean isSelected(LayoutIsContainerService var1) {
         return this.f86kac7j5iju.contains(var1);
      }

      List<HudEditorScreen.Guide> guides() {
         return this.fhmbzyzryl6c;
      }

      List<HudEditorScreen.SpacingBadge> spacingBadges() {
         return this.f59fmxf7tn5k;
      }

      void resetView() {
         this.f1xomqxzdozf = 1.0F;
         this.f57f5jfo42ur = 0.0F;
         this.fjczf73jxnln = 0.0F;
      }

      void deselect() {
         this.f86kac7j5iju.clear();
         this.ffrgpgq4c34i = null;
         this.fbx67bnx0w41.clear();
         this.f87okhqyy2j4 = 0;
      }

      void selectOnly(LayoutIsContainerService var1) {
         this.f86kac7j5iju.clear();
         if (var1 != null) {
            this.f86kac7j5iju.add(var1);
         }

         this.ffrgpgq4c34i = var1;
      }

      void addToSelection(LayoutIsContainerService var1) {
         if (var1 != null) {
            this.f86kac7j5iju.add(var1);
            this.ffrgpgq4c34i = var1;
         }
      }

      void toggleSelection(LayoutIsContainerService var1) {
         this.mhawq7fw3tzd(var1);
      }

      private void mhawq7fw3tzd(LayoutIsContainerService var1) {
         if (var1 != null) {
            if (this.f86kac7j5iju.remove(var1)) {
               this.ffrgpgq4c34i = this.m9sj74wlst2d(this.f86kac7j5iju);
            } else {
               this.f86kac7j5iju.add(var1);
               this.ffrgpgq4c34i = var1;
            }
         }
      }

      void pruneSelection() {
         this.f86kac7j5iju.removeIf(var1 -> this.f6vwisjhq6u4 == null || !this.f6vwisjhq6u4.elements.contains(var1));
         if (this.ffrgpgq4c34i != null && !this.f86kac7j5iju.contains(this.ffrgpgq4c34i)) {
            this.ffrgpgq4c34i = this.m9sj74wlst2d(this.f86kac7j5iju);
         }
      }

      private LayoutIsContainerService m9sj74wlst2d(LinkedHashSet<LayoutIsContainerService> var1) {
         LayoutIsContainerService var2 = null;

         for (LayoutIsContainerService var4 : var1) {
            var2 = var4;
         }

         return var2;
      }

      boolean marqueeActive() {
         return this.f87okhqyy2j4 == 3 && this.isPressed();
      }

      float[] marqueeRect() {
         return !this.marqueeActive()
            ? null
            : new float[]{
               Math.min(this.fi6nm83jr75p, this.f2oiu6q5u1jn),
               Math.min(this.f95kvdxhy2zz, this.fbwrn0fnh61l),
               Math.abs(this.f2oiu6q5u1jn - this.fi6nm83jr75p),
               Math.abs(this.fbwrn0fnh61l - this.f95kvdxhy2zz)
            };
      }

      List<LayoutRenderer.Rect> selectionScreenRects() {
         ArrayList var1 = new ArrayList();
         if (this.fb0r5ksfgwey != null && this.f6vwisjhq6u4 != null) {
            float var2 = this.mixngze3x3dk();
            if (var2 <= 0.0F) {
               return var1;
            }

            float var3 = this.m74m2uey8p7e();
            float var4 = this.m8wbshutfnzw();

            for (LayoutIsContainerService var6 : this.f86kac7j5iju) {
               if (var6 != null && var6.visible && this.f6vwisjhq6u4.elements.contains(var6)) {
                  var1.add(this.fb0r5ksfgwey.screenRect(var6, this.fitX, this.fitY, var3, var4, var2, this.f2sa5kpgfxfw));
               }
            }

            return var1;
         } else {
            return var1;
         }
      }

      LayoutRenderer.Rect selectedScreenRect() {
         if (this.ffrgpgq4c34i != null && this.fb0r5ksfgwey != null) {
            if (this.ffrgpgq4c34i.visible && (this.f6vwisjhq6u4 == null || this.f6vwisjhq6u4.elements.contains(this.ffrgpgq4c34i))) {
               float var1 = this.mixngze3x3dk();
               return var1 <= 0.0F
                  ? null
                  : this.fb0r5ksfgwey.screenRect(this.ffrgpgq4c34i, this.fitX, this.fitY, this.m74m2uey8p7e(), this.m8wbshutfnzw(), var1, this.f2sa5kpgfxfw);
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      @Override
      protected void onPress(float var1, float var2) {
         boolean var3 = mg4h3xrtt73w();
         boolean var4 = m1jygps8gkxi();
         int[] var5 = this.m5bsrnhv68hx(var1, var2);
         if (var5 != null) {
            this.mdfd3on8w7tp(var5[0], var5[1], var1, var2);
         } else {
            LayoutIsContainerService var6 = this.m849a8cy3b69(var1, var2);
            if (var6 != null) {
               if (var3) {
                  this.mhawq7fw3tzd(var6);
                  this.f87okhqyy2j4 = 4;
                  return;
               }

               if (!this.isSelected(var6)) {
                  this.selectOnly(var6);
               } else {
                  this.ffrgpgq4c34i = var6;
               }

               this.md8e74zyryn8(var1, var2);
            } else if (var4) {
               this.f87okhqyy2j4 = 0;
               this.f9vqxr571xac = var1;
               this.fhmljivzxxri = var2;
            } else if (var3) {
               this.mgkki6smjw28(var1, var2, true);
            } else if (this.f2pvoqpy9wed > Float.intBitsToFloat(1065437102)) {
               this.mezq1kyjlad2();
               this.f87okhqyy2j4 = 0;
               this.f9vqxr571xac = var1;
               this.fhmljivzxxri = var2;
            } else {
               this.mgkki6smjw28(var1, var2, false);
            }
         }
      }

      private void md8e74zyryn8(float var1, float var2) {
         this.fbx67bnx0w41.clear();

         for (LayoutIsContainerService var4 : this.f86kac7j5iju) {
            if (!var4.locked) {
               this.fbx67bnx0w41.add(var4);
            }
         }

         this.f11x5ia98nwz = var1;
         this.fh5gdi83bvv2 = var2;
         this.fh2uz78r5nwt = false;
         if (this.fbx67bnx0w41.isEmpty()) {
            this.f87okhqyy2j4 = 0;
            this.f9vqxr571xac = var1;
            this.fhmljivzxxri = var2;
         } else {
            this.fifme2nv3uxy = new float[this.fbx67bnx0w41.size()];
            this.f4nm5dzw9x43 = new float[this.fbx67bnx0w41.size()];

            for (int var5 = 0; var5 < this.fbx67bnx0w41.size(); var5++) {
               this.fifme2nv3uxy[var5] = this.fbx67bnx0w41.get(var5).offsetX;
               this.f4nm5dzw9x43[var5] = this.fbx67bnx0w41.get(var5).offsetY;
            }

            this.f87okhqyy2j4 = 1;
         }
      }

      private void mgkki6smjw28(float var1, float var2, boolean var3) {
         this.f87okhqyy2j4 = 3;
         this.fi6nm83jr75p = this.f2oiu6q5u1jn = var1;
         this.f95kvdxhy2zz = this.fbwrn0fnh61l = var2;
         this.f5ti9xthufjw.clear();
         this.fhmbzyzryl6c.clear();
         this.f59fmxf7tn5k.clear();
         if (var3) {
            this.f5ti9xthufjw.addAll(this.f86kac7j5iju);
         } else {
            this.mezq1kyjlad2();
         }
      }

      private void mezq1kyjlad2() {
         this.f86kac7j5iju.clear();
         this.ffrgpgq4c34i = null;
      }

      @Override
      protected void updateWhilePressed(float var1, float var2) {
         if (this.f87okhqyy2j4 == 2 && this.ffrgpgq4c34i != null) {
            if (!this.fh2uz78r5nwt) {
               if (Math.abs(var1 - this.f11x5ia98nwz) < 2.0F && Math.abs(var2 - this.fh5gdi83bvv2) < 2.0F) {
                  return;
               }

               this.fh2uz78r5nwt = true;
            }

            this.m2fwo2rzjrgx(var1, var2);
         } else if (this.f87okhqyy2j4 == 1 && !this.fbx67bnx0w41.isEmpty()) {
            if (!this.fh2uz78r5nwt) {
               if (Math.abs(var1 - this.f11x5ia98nwz) < 2.0F && Math.abs(var2 - this.fh5gdi83bvv2) < 2.0F) {
                  return;
               }

               this.fh2uz78r5nwt = true;
            }

            float var8 = this.mixngze3x3dk();
            if (var8 <= 0.0F) {
               return;
            }

            float var9 = (var1 - this.f11x5ia98nwz) / var8;
            float var5 = (var2 - this.fh5gdi83bvv2) / var8;

            for (int var6 = 0; var6 < this.fbx67bnx0w41.size(); var6++) {
               LayoutIsContainerService var7 = this.fbx67bnx0w41.get(var6);
               var7.offsetX = this.fifme2nv3uxy[var6] + var9;
               var7.offsetY = this.f4nm5dzw9x43[var6] + var5;
            }

            if (this.fbx67bnx0w41.size() == 1) {
               this.mauibusk9uyw(this.fbx67bnx0w41.get(0));
            } else {
               this.fhmbzyzryl6c.clear();
               this.f59fmxf7tn5k.clear();
            }
         } else if (this.f87okhqyy2j4 == 3) {
            this.mel059mokvi2(var1, var2);
         } else if (this.f87okhqyy2j4 == 0) {
            float var3 = var1 - this.f9vqxr571xac;
            float var4 = var2 - this.fhmljivzxxri;
            this.f57f5jfo42ur += var3;
            this.fjczf73jxnln += var4;
            this.f9hzds9ynyos += var3;
            this.feps9zkj6f8f += var4;
            this.f9vqxr571xac = var1;
            this.fhmljivzxxri = var2;
         }
      }

      private void mel059mokvi2(float var1, float var2) {
         this.f2oiu6q5u1jn = var1;
         this.fbwrn0fnh61l = var2;
         float var3 = Math.min(this.fi6nm83jr75p, this.f2oiu6q5u1jn);
         float var4 = Math.min(this.f95kvdxhy2zz, this.fbwrn0fnh61l);
         float var5 = Math.max(this.fi6nm83jr75p, this.f2oiu6q5u1jn);
         float var6 = Math.max(this.f95kvdxhy2zz, this.fbwrn0fnh61l);
         this.f86kac7j5iju.clear();
         this.f86kac7j5iju.addAll(this.f5ti9xthufjw);
         float var7 = this.mixngze3x3dk();
         if (var7 > 0.0F && this.f6vwisjhq6u4 != null && this.fb0r5ksfgwey != null) {
            float var8 = this.m74m2uey8p7e();
            float var9 = this.m8wbshutfnzw();

            for (LayoutIsContainerService var11 : this.f6vwisjhq6u4.elements) {
               if (var11.visible) {
                  LayoutRenderer.Rect var12 = this.fb0r5ksfgwey.screenRect(var11, this.fitX, this.fitY, var8, var9, var7, this.f2sa5kpgfxfw);
                  if (var12.x() < var5 && var12.x() + var12.w() > var3 && var12.y() < var6 && var12.y() + var12.h() > var4) {
                     this.f86kac7j5iju.add(var11);
                  }
               }
            }
         }

         this.ffrgpgq4c34i = this.m9sj74wlst2d(this.f86kac7j5iju);
      }

      private static boolean mgj9ok9efy1p(int var0) {
         long var1 = CompatAdapterService.windowHandle(Minecraft.getInstance());
         return var1 != 0L && GLFW.glfwGetKey(var1, var0) == 1;
      }

      private static boolean mg4h3xrtt73w() {
         return mgj9ok9efy1p(340) || mgj9ok9efy1p(344);
      }

      private static boolean m1jygps8gkxi() {
         return mgj9ok9efy1p(32);
      }

      @Override
      protected boolean handleScroll(float var1) {
         float var2 = m4gzyszkiz3q(
            this.f1xomqxzdozf * (float)Math.pow(Double.longBitsToDouble(4607993066732944097L), var1), 1.0F, Float.intBitsToFloat(1090519040)
         );
         if (var2 != this.f1xomqxzdozf && this.cw > 0.0F && this.ch > 0.0F) {
            CompositorPushPresentationScaleService var3 = CoreIsInitializedHandler.get().compositor();
            float var4 = var3.pointerX();
            float var5 = var3.pointerY();
            float var6 = this.m2oe3sqexmt6();
            float var7 = this.cw;
            float var8 = this.cw / var6;
            if (var8 > this.ch) {
               var8 = this.ch;
               var7 = this.ch * var6;
            }

            float var9 = this.cx + this.cw * Float.intBitsToFloat(1056964608);
            float var10 = this.cy + this.ch * Float.intBitsToFloat(1056964608);
            float var11 = var7 * this.f2pvoqpy9wed;
            float var12 = var8 * this.f2pvoqpy9wed;
            float var13 = var9 - var11 * Float.intBitsToFloat(1056964608) + this.f9hzds9ynyos;
            float var14 = var10 - var12 * Float.intBitsToFloat(1056964608) + this.feps9zkj6f8f;
            float var15 = m4gzyszkiz3q((var4 - var13) / var11, 0.0F, 1.0F);
            float var16 = m4gzyszkiz3q((var5 - var14) / var12, 0.0F, 1.0F);
            float var17 = var7 * var2;
            float var18 = var8 * var2;
            this.f57f5jfo42ur = var4 - var9 + var17 * (Float.intBitsToFloat(1056964608) - var15);
            this.fjczf73jxnln = var5 - var10 + var18 * (Float.intBitsToFloat(1056964608) - var16);
         }

         this.f1xomqxzdozf = var2;
         return true;
      }

      private LayoutIsContainerService m849a8cy3b69(float var1, float var2) {
         if (this.f6vwisjhq6u4 != null && this.fb0r5ksfgwey != null) {
            float var3 = this.mixngze3x3dk();
            if (var3 <= 0.0F) {
               return null;
            }

            float var4 = this.m74m2uey8p7e();
            float var5 = this.m8wbshutfnzw();

            for (int var6 = this.f6vwisjhq6u4.elements.size() - 1; var6 >= 0; var6 += -1) {
               LayoutIsContainerService var7 = this.f6vwisjhq6u4.elements.get(var6);
               if (var7.visible) {
                  LayoutRenderer.Rect var8 = this.fb0r5ksfgwey.screenRect(var7, this.fitX, this.fitY, var4, var5, var3, this.f2sa5kpgfxfw);
                  if (var1 >= var8.x() - 2.0F && var1 <= var8.x() + var8.w() + 2.0F && var2 >= var8.y() - 2.0F && var2 <= var8.y() + var8.h() + 2.0F) {
                     return var7;
                  }
               }
            }

            return null;
         } else {
            return null;
         }
      }

      private void mauibusk9uyw(LayoutIsContainerService var1) {
         this.fhmbzyzryl6c.clear();
         this.f59fmxf7tn5k.clear();
         float var2 = this.mixngze3x3dk();
         float var3 = this.m74m2uey8p7e();
         float var4 = this.m8wbshutfnzw();
         if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            float var5 = Float.intBitsToFloat(1086324736) / var2;
            LayoutRenderer.Rect var6 = this.fb0r5ksfgwey.logicalRect(var1, var3, var4, this.f2sa5kpgfxfw);
            ArrayList var7 = new ArrayList<>(List.of(0.0F, var3 * Float.intBitsToFloat(1056964608), var3));
            ArrayList var8 = new ArrayList<>(List.of(0.0F, var4 * Float.intBitsToFloat(1056964608), var4));

            for (LayoutIsContainerService var10 : this.f6vwisjhq6u4.elements) {
               if (var10 != var1 && var10.visible) {
                  LayoutRenderer.Rect var11 = this.fb0r5ksfgwey.logicalRect(var10, var3, var4, this.f2sa5kpgfxfw);
                  var7.add(var11.x());
                  var7.add(var11.x() + var11.w() * Float.intBitsToFloat(1056964608));
                  var7.add(var11.x() + var11.w());
                  var8.add(var11.y());
                  var8.add(var11.y() + var11.h() * Float.intBitsToFloat(1056964608));
                  var8.add(var11.y() + var11.h());
               }
            }

            float[] var27 = new float[]{var6.x(), var6.x() + var6.w() * Float.intBitsToFloat(1056964608), var6.x() + var6.w()};
            float var28 = var5;
            float var29 = 0.0F;
            float var12 = 0.0F;
            byte var13 = 0;

            for (float var17 : var27) {
               for (float var19 : (Iterable<Float>) (Iterable<?>) (var7)) {
                  float var20 = Math.abs(var17 - var19);
                  if (var20 < var28) {
                     var28 = var20;
                     var29 = var19 - var17;
                     var12 = var19;
                     var13 = 1;
                  }
               }
            }

            if (var13 != 0) {
               var1.offsetX += var29;
               this.fhmbzyzryl6c.add(new HudEditorScreen.Guide(true, this.fitX + var12 * var2, this.fitY, this.fitY + this.fitH));
            }

            float[] var30 = new float[]{var6.y(), var6.y() + var6.h() * Float.intBitsToFloat(1056964608), var6.y() + var6.h()};
            float var31 = var5;
            float var32 = 0.0F;
            float var33 = 0.0F;
            byte var34 = 0;

            for (float var22 : var30) {
               for (float var24 : (Iterable<Float>) (Iterable<?>) (var8)) {
                  float var25 = Math.abs(var22 - var24);
                  if (var25 < var31) {
                     var31 = var25;
                     var32 = var24 - var22;
                     var33 = var24;
                     var34 = 1;
                  }
               }
            }

            if (var34 != 0) {
               var1.offsetY += var32;
               this.fhmbzyzryl6c.add(new HudEditorScreen.Guide(false, this.fitY + var33 * var2, this.fitX, this.fitX + this.fitW));
            }

            var6 = this.fb0r5ksfgwey.logicalRect(var1, var3, var4, this.f2sa5kpgfxfw);
            if (var13 == 0) {
               this.mdpxa064dfsl(var1, var6, var3, var4, var2, var5);
            }

            if (var34 == 0) {
               this.mdgbipef0o8j(var1, this.fb0r5ksfgwey.logicalRect(var1, var3, var4, this.f2sa5kpgfxfw), var3, var4, var2, var5);
            }
         }
      }

      private void mdpxa064dfsl(LayoutIsContainerService var1, LayoutRenderer.Rect var2, float var3, float var4, float var5, float var6) {
         LayoutRenderer.Rect var7 = null;
         LayoutRenderer.Rect var8 = null;

         for (LayoutIsContainerService var10 : this.f6vwisjhq6u4.elements) {
            if (var10 != var1 && var10.visible) {
               LayoutRenderer.Rect var11 = this.fb0r5ksfgwey.logicalRect(var10, var3, var4, this.f2sa5kpgfxfw);
               if (var2.y() < var11.y() + var11.h() && var2.y() + var2.h() > var11.y()) {
                  if (var11.x() + var11.w() <= var2.x() + Float.intBitsToFloat(1008981770)) {
                     if (var7 == null || var11.x() + var11.w() > var7.x() + var7.w()) {
                        var7 = var11;
                     }
                  } else if (var11.x() >= var2.x() + var2.w() - Float.intBitsToFloat(1008981770) && (var8 == null || var11.x() < var8.x())) {
                     var8 = var11;
                  }
               }
            }
         }

         if (var7 != null && var8 != null) {
            float var15 = var2.x() - (var7.x() + var7.w());
            float var16 = var8.x() - (var2.x() + var2.w());
            if (!(var15 < 0.0F) && !(var16 < 0.0F) && !(Math.abs(var16 - var15) > var6 * 2.0F)) {
               var1.offsetX = var1.offsetX + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var17 = var2.x() + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var12 = var2.x() + var2.w() + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var13 = this.fitY + (var2.y() + var2.h() * Float.intBitsToFloat(1056964608)) * var5;
               int var14 = Math.round((var15 + var16) * Float.intBitsToFloat(1056964608));
               this.f59fmxf7tn5k.add(new HudEditorScreen.SpacingBadge(true, this.fitX + (var7.x() + var7.w()) * var5, this.fitX + var17 * var5, var13, var14));
               this.f59fmxf7tn5k.add(new HudEditorScreen.SpacingBadge(true, this.fitX + var12 * var5, this.fitX + var8.x() * var5, var13, var14));
            }
         }
      }

      private void mdgbipef0o8j(LayoutIsContainerService var1, LayoutRenderer.Rect var2, float var3, float var4, float var5, float var6) {
         LayoutRenderer.Rect var7 = null;
         LayoutRenderer.Rect var8 = null;

         for (LayoutIsContainerService var10 : this.f6vwisjhq6u4.elements) {
            if (var10 != var1 && var10.visible) {
               LayoutRenderer.Rect var11 = this.fb0r5ksfgwey.logicalRect(var10, var3, var4, this.f2sa5kpgfxfw);
               if (var2.x() < var11.x() + var11.w() && var2.x() + var2.w() > var11.x()) {
                  if (var11.y() + var11.h() <= var2.y() + Float.intBitsToFloat(1008981770)) {
                     if (var7 == null || var11.y() + var11.h() > var7.y() + var7.h()) {
                        var7 = var11;
                     }
                  } else if (var11.y() >= var2.y() + var2.h() - Float.intBitsToFloat(1008981770) && (var8 == null || var11.y() < var8.y())) {
                     var8 = var11;
                  }
               }
            }
         }

         if (var7 != null && var8 != null) {
            float var15 = var2.y() - (var7.y() + var7.h());
            float var16 = var8.y() - (var2.y() + var2.h());
            if (!(var15 < 0.0F) && !(var16 < 0.0F) && !(Math.abs(var16 - var15) > var6 * 2.0F)) {
               var1.offsetY = var1.offsetY + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var17 = var2.y() + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var12 = var2.y() + var2.h() + (var16 - var15) * Float.intBitsToFloat(1056964608);
               float var13 = this.fitX + (var2.x() + var2.w() * Float.intBitsToFloat(1056964608)) * var5;
               int var14 = Math.round((var15 + var16) * Float.intBitsToFloat(1056964608));
               this.f59fmxf7tn5k.add(new HudEditorScreen.SpacingBadge(false, this.fitY + (var7.y() + var7.h()) * var5, this.fitY + var17 * var5, var13, var14));
               this.f59fmxf7tn5k.add(new HudEditorScreen.SpacingBadge(false, this.fitY + var12 * var5, this.fitY + var8.y() * var5, var13, var14));
            }
         }
      }

      private int[] m5bsrnhv68hx(float var1, float var2) {
         if (this.selectionSize() != 1) {
            return null;
         }

         if (this.ffrgpgq4c34i != null && this.ffrgpgq4c34i.locked) {
            return null;
         }

         LayoutRenderer.Rect var3 = this.selectedScreenRect();
         if (var3 == null) {
            return null;
         }

         if (!(var3.w() <= Float.intBitsToFloat(1101529088)) && !(var3.h() <= Float.intBitsToFloat(1101529088))) {
            if (var1 > var3.x() + Float.intBitsToFloat(1088421888)
               && var1 < var3.x() + var3.w() - Float.intBitsToFloat(1088421888)
               && var2 > var3.y() + Float.intBitsToFloat(1088421888)
               && var2 < var3.y() + var3.h() - Float.intBitsToFloat(1088421888)) {
               return null;
            }

            for (int[] var7 : HudEditorScreen.fdv1pwgda7wj) {
               float var8 = var3.x() + (var7[0] + 1) * Float.intBitsToFloat(1056964608) * var3.w();
               float var9 = var3.y() + (var7[1] + 1) * Float.intBitsToFloat(1056964608) * var3.h();
               if (Math.abs(var1 - var8) <= Float.intBitsToFloat(1088421888) && Math.abs(var2 - var9) <= Float.intBitsToFloat(1088421888)) {
                  return var7;
               }
            }

            return null;
         } else {
            return null;
         }
      }

      private void mdfd3on8w7tp(int var1, int var2, float var3, float var4) {
         LayoutIsContainerService var5 = this.ffrgpgq4c34i;
         if (var5 != null) {
            this.f87okhqyy2j4 = 2;
            this.f5ros71ee6j4 = var1;
            this.f8ybl7c49ize = var2;
            this.f11x5ia98nwz = var3;
            this.fh5gdi83bvv2 = var4;
            this.fh2uz78r5nwt = false;
            LayoutRenderer.Rect var6 = this.fb0r5ksfgwey.logicalRect(var5, this.m74m2uey8p7e(), this.m8wbshutfnzw(), this.f2sa5kpgfxfw);
            this.fdo7pt0uxf3 = var6.x();
            this.fc8uf86bf9a2 = var6.y();
            this.f5sv6cgpsx2d = var6.w();
            this.f4hkexf84zu9 = var6.h();
            this.f5yxn2ejht3t = m8kf964zbo8(var5.type);
            this.fczr8nr4xg4c = !(!"armorhud".equals(var5.type) && !"keystrokes".equals(var5.type));
            this.fhzvwpeozl9c = Math.max(Float.intBitsToFloat(1086324736), LayoutStringService.number(var5.props, "size", Float.intBitsToFloat(1098907648)));
            this.fb76wqc9913 = Math.max(Float.intBitsToFloat(1077936128), LayoutStringService.number(var5.props, "fontSize", Float.intBitsToFloat(1091567616)));
            this.f8slatb7zchf = LayoutStringService.number(var5.props, "shadowX", 0.0F);
            this.f26n3h05ov89 = LayoutStringService.number(var5.props, "shadowY", 0.0F);
            this.f9ulsggmm0kc = LayoutStringService.number(var5.props, "outlineWidth", 0.0F);
            this.f1ax835qkn9j = LayoutStringService.number(var5.props, "cornerRadius", "rect".equals(var5.type) ? Float.intBitsToFloat(1082130432) : 0.0F);
         }
      }

      private void m2fwo2rzjrgx(float var1, float var2) {
         LayoutIsContainerService var3 = this.ffrgpgq4c34i;
         if (var3 != null) {
            float var4 = this.mixngze3x3dk();
            float var5 = this.m74m2uey8p7e();
            float var6 = this.m8wbshutfnzw();
            if (!(var4 <= 0.0F) && !(var5 <= 0.0F) && !(var6 <= 0.0F)) {
               float var7 = (var1 - this.fitX) / var4;
               float var8 = (var2 - this.fitY) / var4;
               float var9 = this.f5sv6cgpsx2d;
               float var10 = this.f4hkexf84zu9;
               if (this.f5ros71ee6j4 == 1) {
                  var9 = var7 - this.fdo7pt0uxf3;
               } else if (this.f5ros71ee6j4 == -1) {
                  var9 = this.fdo7pt0uxf3 + this.f5sv6cgpsx2d - var7;
               }

               if (this.f8ybl7c49ize == 1) {
                  var10 = var8 - this.fc8uf86bf9a2;
               } else if (this.f8ybl7c49ize == -1) {
                  var10 = this.fc8uf86bf9a2 + this.f4hkexf84zu9 - var8;
               }

               var9 = Math.max(Float.intBitsToFloat(1082130432), var9);
               var10 = Math.max(Float.intBitsToFloat(1082130432), var10);
               float var11 = this.f5sv6cgpsx2d > Float.intBitsToFloat(981668463) ? var9 / this.f5sv6cgpsx2d : 1.0F;
               float var12 = this.f4hkexf84zu9 > Float.intBitsToFloat(981668463) ? var10 / this.f4hkexf84zu9 : 1.0F;
               m6dn8hejjqnu(var3);
               if (this.f5yxn2ejht3t) {
                  var3.width = Float.intBitsToFloat(-1082130432);
                  var3.height = Float.intBitsToFloat(-1082130432);
                  float var13 = this.f5ros71ee6j4 != 0 && this.f8ybl7c49ize != 0
                     ? (Math.abs(var11 - 1.0F) >= Math.abs(var12 - 1.0F) ? var11 : var12)
                     : (this.f5ros71ee6j4 != 0 ? var11 : var12);
                  float var14 = Math.max(Float.intBitsToFloat(1077936128), this.fb76wqc9913 * var13);
                  float var15 = var14 / this.fb76wqc9913;
                  var3.props.addProperty("fontSize", var14);
                  if (this.f8slatb7zchf != 0.0F) {
                     var3.props.addProperty("shadowX", this.f8slatb7zchf * var15);
                  }

                  if (this.f26n3h05ov89 != 0.0F) {
                     var3.props.addProperty("shadowY", this.f26n3h05ov89 * var15);
                  }

                  if (this.f9ulsggmm0kc > 0.0F) {
                     var3.props.addProperty("outlineWidth", this.f9ulsggmm0kc * var15);
                  }
               } else if (this.fczr8nr4xg4c) {
                  var3.width = Float.intBitsToFloat(-1082130432);
                  var3.height = Float.intBitsToFloat(-1082130432);
                  float var20 = this.f5ros71ee6j4 != 0 && this.f8ybl7c49ize != 0
                     ? (Math.abs(var11 - 1.0F) >= Math.abs(var12 - 1.0F) ? var11 : var12)
                     : (this.f5ros71ee6j4 != 0 ? var11 : var12);
                  var3.props.addProperty("size", Math.max(Float.intBitsToFloat(1090519040), this.fhzvwpeozl9c * var20));
               } else {
                  var3.width = var9;
                  var3.height = var10;
                  if ("rect".equals(var3.type) && this.f1ax835qkn9j > 0.0F) {
                     var3.props.addProperty("cornerRadius", this.f1ax835qkn9j * (float)Math.sqrt(Math.max(Float.intBitsToFloat(953267991), var11 * var12)));
                  }
               }

               LayoutRenderer.Rect var21 = this.fb0r5ksfgwey.logicalRect(var3, var5, var6, this.f2sa5kpgfxfw);
               float var22 = var21.w();
               float var23 = var21.h();
               float var16 = this.f5ros71ee6j4 == 1
                  ? this.fdo7pt0uxf3
                  : (
                     this.f5ros71ee6j4 == -1
                        ? this.fdo7pt0uxf3 + this.f5sv6cgpsx2d - var22
                        : this.fdo7pt0uxf3 + this.f5sv6cgpsx2d * Float.intBitsToFloat(1056964608) - var22 * Float.intBitsToFloat(1056964608)
                  );
               float var17 = this.f8ybl7c49ize == 1
                  ? this.fc8uf86bf9a2
                  : (
                     this.f8ybl7c49ize == -1
                        ? this.fc8uf86bf9a2 + this.f4hkexf84zu9 - var23
                        : this.fc8uf86bf9a2 + this.f4hkexf84zu9 * Float.intBitsToFloat(1056964608) - var23 * Float.intBitsToFloat(1056964608)
                  );
               var3.offsetX = var16 + var3.anchor.h.factor * (var22 - var5);
               var3.offsetY = var17 + var3.anchor.v.factor * (var23 - var6);
            }
         }
      }

      private static boolean m8kf964zbo8(String var0) {
         return "text".equals(var0) || "fps".equals(var0) || "coords".equals(var0);
      }

      private static void m6dn8hejjqnu(LayoutIsContainerService var0) {
         if (var0.props == null) {
            var0.props = new JsonObject();
         }
      }

      @Override
      protected void draw(CompositorPushPresentationScaleService var1) {
         if (!(this.cw <= 0.0F) && !(this.ch <= 0.0F)) {
            if (!this.isPressed()) {
               if (!this.fhmbzyzryl6c.isEmpty()) {
                  this.fhmbzyzryl6c.clear();
               }

               if (!this.f59fmxf7tn5k.isEmpty()) {
                  this.f59fmxf7tn5k.clear();
               }
            }

            float var2 = 1.0F - (float)Math.exp(-SceneDtService.dt() * Float.intBitsToFloat(1102053376));
            this.f2pvoqpy9wed = this.f2pvoqpy9wed + (this.f1xomqxzdozf - this.f2pvoqpy9wed) * var2;
            this.f9hzds9ynyos = this.f9hzds9ynyos + (this.f57f5jfo42ur - this.f9hzds9ynyos) * var2;
            this.feps9zkj6f8f = this.feps9zkj6f8f + (this.fjczf73jxnln - this.feps9zkj6f8f) * var2;
            float var3 = this.m2oe3sqexmt6();
            float var4 = this.cw;
            float var5 = this.cw / var3;
            if (var5 > this.ch) {
               var5 = this.ch;
               var4 = this.ch * var3;
            }

            float var6 = var4 * this.f2pvoqpy9wed;
            float var7 = var5 * this.f2pvoqpy9wed;
            float var8 = Math.max(0.0F, (var6 - this.cw) * Float.intBitsToFloat(1056964608));
            float var9 = Math.max(0.0F, (var7 - this.ch) * Float.intBitsToFloat(1056964608));
            float var10 = Math.max(0.0F, (var4 * this.f1xomqxzdozf - this.cw) * Float.intBitsToFloat(1056964608));
            float var11 = Math.max(0.0F, (var5 * this.f1xomqxzdozf - this.ch) * Float.intBitsToFloat(1056964608));
            this.f57f5jfo42ur = m4gzyszkiz3q(this.f57f5jfo42ur, -var10, var10);
            this.fjczf73jxnln = m4gzyszkiz3q(this.fjczf73jxnln, -var11, var11);
            this.f9hzds9ynyos = m4gzyszkiz3q(this.f9hzds9ynyos, -var8, var8);
            this.feps9zkj6f8f = m4gzyszkiz3q(this.feps9zkj6f8f, -var9, var9);
            float var12 = this.cx + this.cw * Float.intBitsToFloat(1056964608);
            float var13 = this.cy + this.ch * Float.intBitsToFloat(1056964608);
            this.fitW = var6;
            this.fitH = var7;
            this.fitX = var12 - var6 * Float.intBitsToFloat(1056964608) + this.f9hzds9ynyos;
            this.fitY = var13 - var7 * Float.intBitsToFloat(1056964608) + this.feps9zkj6f8f;
            var1.requestFramebufferSample();
            RhiBlendStateService.TextureHandle var14 = var1.framebufferSample();
            if (var14 != null && var14.valid()) {
               float var15 = Math.max(Float.intBitsToFloat(1060320051), this.effectiveEdgeSoftness);
               var1.drawTextureOpaque(
                  var14,
                  this.fitX,
                  this.fitY,
                  this.fitW,
                  this.fitH,
                  this.effectiveOpacity,
                  Float.intBitsToFloat(1090519040),
                  Float.intBitsToFloat(1090519040),
                  Float.intBitsToFloat(1090519040),
                  Float.intBitsToFloat(1090519040),
                  var15
               );
            }
         }
      }

      private float m74m2uey8p7e() {
         Window var1 = Minecraft.getInstance().getWindow();
         return var1 != null ? var1.getGuiScaledWidth() : 0.0F;
      }

      private float m8wbshutfnzw() {
         Window var1 = Minecraft.getInstance().getWindow();
         return var1 != null ? var1.getGuiScaledHeight() : 0.0F;
      }

      private float m2oe3sqexmt6() {
         float var1 = this.m74m2uey8p7e();
         float var2 = this.m8wbshutfnzw();
         return var2 > 0.0F ? var1 / var2 : Float.intBitsToFloat(1071877689);
      }

      private float mixngze3x3dk() {
         float var1 = this.m74m2uey8p7e();
         return var1 > 0.0F ? this.fitW / var1 : 0.0F;
      }

      private static float m4gzyszkiz3q(float var0, float var1, float var2) {
         return Math.max(var1, Math.min(var2, var0));
      }
   }

   record SpacingBadge(boolean horizontal, float a0, float a1, float cross, int dist) {
   }
}
