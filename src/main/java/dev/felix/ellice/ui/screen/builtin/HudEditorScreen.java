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
  private static final int count = -871559915;
  private static final int count2 = -871757295;
  private static final int count3 = 788529151;
  private static final int count4 = 285212671;
  private static final int count5 = 1879048192;
  private static final int count6 = -218103809;
  private static final int fdps1helzlom = -553648129;
  private static final int count8 = -1627389953;
  private static final int count9 = 1795162111;
  private static final int count10 = 251658239;
  private static final int count11 = 452984831;
  private static final int count12 = 268435456;
  private static final int count13 = 385875967;
  private static final int count14 = 855638016;
  private static final int count15 = 822083583;
  private static final int count16 = 301989888;
  private static final int count17 = 419430399;
  private static final int count18 = 788529151;
  private static final int count19 = 318767103;
  private static final int count20 = 167772159;
  private static final Path path =
      FabricLoader.getInstance()
          .getModContainer("ellice")
          .flatMap(var0 -> var0.findPath("assets/ellice/components/icons/x.svg"))
          .orElse(Path.of("src/main/resources/assets/ellice/components/icons/x.svg"));
  private static final Path path2 =
      FabricLoader.getInstance()
          .getModContainer("ellice")
          .flatMap(var0 -> var0.findPath("assets/ellice/components/icons/trash.svg"))
          .orElse(Path.of("src/main/resources/assets/ellice/components/icons/trash.svg"));
  private static final int count21 = 384779332;
  private static final int count22 = 250561604;
  private static final int count23 = 753878084;
  private static final int count24 = 1190085700;
  private static final int count25 = 1307526212;
  private static final int count26 = -25958;
  private static final int count27 = 452984831;
  private static final int count28 = 352321535;
  private static final int count29 = 822083583;
  private static final int count30 = 301989888;
  private static final int count31 = -1;
  private static final int count32 = -1513234;
  private static final int count33 = -434628574;
  private static final float value2 = 2.0F;
  private static final float value3 = 0.0F;
  private static final float value4 = 46.0F;
  private static final float value5 = 168.0F;
  private static final float value6 = 220.0F;
  private static final int[][] int2 =
      new int[][] {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
  private static final String[][] text2 =
      new String[][] {
        {"fps", "FPS"},
        {"coords", "Coords"},
        {"rect", "Rect"},
        {"armorhud", "Armor HUD"},
        {"keystrokes", "Keystrokes"}
      };
  private final LayoutFindSelector f2prt6jl4bc;
  private final LayoutRenderer renderer;
  private final LayoutOperationHandler f7fr4h364rv;
  private int count34 = -10262799;
  private int f3dscnabklqo = -11581723;
  private LayoutContainerNode sceneComponent4;
  private SceneCornerRadiusService sceneCornerRadiusService;
  private SceneCornerRadiusService sceneCornerRadiusService2;
  private HudEditorScreen.ScreenSampleNode fetqhndloi5c;
  private SceneTextService sceneTextService;
  private String text3 = "Fit";
  private SceneTextService sceneTextService2;
  private LayoutContainerNode sceneComponent42;
  private SceneTextService sceneTextService3;
  private boolean fbkjdm8rmyhv;
  private String text4 = "";
  private LayoutContainerNode sceneComponent43;
  private LayoutContainerNode sceneComponent44;
  private TextFontDirectoryService flgozpodfet;
  private FONTComponent fONTComponent;
  private boolean enabled2;
  private SceneCornerRadiusService f1oxmbxecj6;
  private SceneCornerRadiusService sceneCornerRadiusService4;
  private SceneCornerRadiusService sceneCornerRadiusService5;
  private ColourComponent colourComponent;
  private LayoutIsContainerService layoutIsContainerService;
  private LayoutContainerNode fuy67uovp93;
  private final SceneCornerRadiusService[] sceneCornerRadiusService6 =
      new SceneCornerRadiusService[9];
  private ControlLetterSpacingService controlLetterSpacingService;
  private ControlLetterSpacingService controlLetterSpacingService2;
  private CompactSliderControl builtinComponent4;
  private SceneTextService sceneTextService4;
  private String text5 = "";
  private ControlCompactService controlCompactService;
  private ControlCompactService controlCompactService2;
  private LayoutIsContainerService layoutIsContainerService2;
  private String text6 = "";
  private String text7 = "";
  private LayoutContainerNode sceneComponent46;
  private LayoutIsContainerService layoutIsContainerService3;
  private boolean enabled3;
  private final List<Runnable> items = new ArrayList<>();
  private final List<ColourComponent> items2 = new ArrayList<>();
  private final List<Runnable> fds2fqndpgdh = new ArrayList<>();
  private final List<ColourComponent> items4 = new ArrayList<>();
  private List<Runnable> items5 = this.items;
  private List<ColourComponent> items6 = this.items2;
  private LayoutContainerNode sceneComponent47;
  private LayoutIsContainerService layoutIsContainerService4;
  private boolean enabled4;
  private SceneCornerRadiusService sceneCornerRadiusService7;
  private SceneCornerRadiusService sceneCornerRadiusService8;
  private final List<Runnable> items7 = new ArrayList<>();
  private final List<ColourComponent> fyh7bfyp88z = new ArrayList<>();
  private LayoutContainerNode sceneComponent48;
  private LayoutIsContainerService layoutIsContainerService5;
  private boolean enabled5;
  private FONTComponent feemtwsjsd4d;
  private ControlLetterSpacingService controlLetterSpacingService3;
  private ControlLetterSpacingService controlLetterSpacingService4;
  private String text8 = "";
  private String text9 = "";
  private CompactSliderControl builtinComponent42;
  private SceneTextService fcp6k70oa7q;
  private JsonObject ftxajmi19ob;
  private float value7 = 1.0F;
  private SceneTextService sceneTextService6;
  private long timestamp;
  private LayoutContainerNode fjfgtmdxhqe6;
  private LayoutContainerNode sceneComponent410;
  private String text10 = "";
  private LayoutIsContainerService layoutIsContainerService6;
  private boolean enabled6;
  private float value8;
  private static final float value9 = 26.0F;
  private static final float value10 = 3.0F;
  private static final float fg7yfexypqmk = 29.0F;
  private static final float value12 = 11.0F;
  private static final float value13 = 8.0F;
  private final List<LayoutIsContainerService> items9 = new ArrayList<>();
  private final Map<LayoutIsContainerService, SceneCornerRadiusService> entries = new HashMap<>();
  private final List<HudEditorScreen.PendingAnim> items10 = new ArrayList<>();

  public HudEditorScreen(
      LayoutFindSelector var1, LayoutRenderer var2, LayoutOperationHandler var3) {
    this.f2prt6jl4bc = var1;
    this.renderer = var2;
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
      this.count34 = var2;
    }

    int var3 = var1.theme().color("accent-press");
    if (var3 != 0) {
      this.f3dscnabklqo = var3;
    }

    this.sceneComponent4 =
        new LayoutContainerNode()
            .size(
                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                    Float.intBitsToFloat(1120403456)),
                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                    Float.intBitsToFloat(1120403456)))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH);
    this.flgozpodfet = new TextFontDirectoryService(FabricLoader.getInstance().getGameDir());
    this.sceneComponent4.addChild(
        new SceneCornerRadiusService()
            .absolute()
            .inset(dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(0.0F))
            .size(
                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(),
                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto())
            .backgroundColor(805306368)
            .cornerRadius(0.0F)
            .interactive(true)
            .layerBreak(true));
    this.sceneCornerRadiusService = this.createSceneCornerRadiusService(var1);
    this.sceneCornerRadiusService.size(
        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
            Float.intBitsToFloat(1120403456)),
        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
            Float.intBitsToFloat(1120403456)));
    this.sceneComponent4.addChild(this.sceneCornerRadiusService);
    this.sceneCornerRadiusService.scale(Float.intBitsToFloat(1064849900)).opacity(0.0F);
    this.sceneCornerRadiusService.animate(
        "opacity", 1.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1046562734)));
    this.sceneCornerRadiusService.animate("scale", 1.0F, SceneEaseHandler.Spring.SNAPPY);
    return this.sceneComponent4;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService(ScreenScreenIdService var1) {
    SceneCornerRadiusService var2 =
        new SceneCornerRadiusService()
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
    LayoutContainerNode var3 = this.createSceneComponent4(var1);
    var3.opacity(0.0F);
    var2.addChild(var3);
    this.updateState37(var3, Float.intBitsToFloat(1031127695));
    var2.addChild(
        new SceneCornerRadiusService()
            .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), 1.0F)
            .backgroundColor(318767103));
    LayoutContainerNode var4 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .flex(1.0F);
    var2.addChild(var4);
    LayoutContainerNode var5 = this.createSceneComponent42();
    var5.opacity(0.0F);
    var4.addChild(var5);
    this.updateState37(var5, Float.intBitsToFloat(1039516303));
    var4.addChild(
        new SceneCornerRadiusService()
            .size(
                Float.intBitsToFloat(1056964608),
                ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .backgroundColor(167772159));
    LayoutContainerNode var6 = this.createSceneComponent44();
    var6.opacity(0.0F);
    var4.addChild(var6);
    this.updateState37(var6, Float.intBitsToFloat(1043878380));
    var4.addChild(
        new SceneCornerRadiusService()
            .size(
                Float.intBitsToFloat(1056964608),
                ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .backgroundColor(167772159));
    LayoutContainerNode var7 = this.createSceneComponent45();
    var7.opacity(0.0F);
    var4.addChild(var7);
    this.updateState37(var7, Float.intBitsToFloat(1047904911));
    return var2;
  }

  private LayoutContainerNode createSceneComponent4(ScreenScreenIdService var1) {
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1110966272))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.SPACE_BETWEEN)
            .padding(0.0F, Float.intBitsToFloat(1098907648));
    LayoutContainerNode var3 =
        new LayoutContainerNode()
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1092616192));
    var3.addChild(
        createSceneTextService2(
            "HUD Editor", Float.intBitsToFloat(1097859072), -218103809, TextMode.BOLD));
    this.sceneTextService6 =
        createSceneTextService2("", Float.intBitsToFloat(1091043328), this.count34, TextMode.BOLD);
    this.sceneTextService6.opacity(0.0F);
    var3.addChild(this.sceneTextService6);
    var2.addChild(var3);
    LayoutContainerNode var4 =
        new LayoutContainerNode()
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.END)
            .gap(Float.intBitsToFloat(1090519040));
    var4.addChild(this.mbpayroggdes());
    var4.addChild(this.createSceneCornerRadiusService3());
    var4.addChild(this.createSceneCornerRadiusService4(var1));
    var2.addChild(var4);
    return var2;
  }

  private SceneCornerRadiusService mbpayroggdes() {
    SceneCornerRadiusService var1 =
        new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1102053376))
            .cornerRadius(Float.intBitsToFloat(1090519040))
            .backgroundColor(this.count34 & 16777215 | 855638016)
            .gradientEnd(this.f3dscnabklqo & 16777215 | 855638016)
            .hoverBackground(this.count34 & 16777215 | 1426063360)
            .border(Float.intBitsToFloat(1056964608), this.count34 & 16777215 | 1879048192)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER)
            .padding(0.0F, Float.intBitsToFloat(1093664768))
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(this::mcwatwgehpgo);
    var1.addChild(
        createSceneTextService2(
            "Save", Float.intBitsToFloat(1091043328), -218103809, TextMode.BOLD));
    return var1;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService3() {
    this.sceneTextService =
        createSceneTextService2(
            "Fit", Float.intBitsToFloat(1090519040), -1627389953, TextMode.BOLD);
    SceneCornerRadiusService var1 =
        new SceneCornerRadiusService()
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
            .onClick(
                () -> {
                  if (this.fetqhndloi5c != null) {
                    this.fetqhndloi5c.resetView();
                  }
                });
    var1.addChild(this.sceneTextService);
    return var1;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService4(ScreenScreenIdService var1) {
    SceneCornerRadiusService var2 =
        new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1104150528))
            .cornerRadius(Float.intBitsToFloat(1091567616))
            .backgroundColor(0)
            .hoverBackground(385875967)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER)
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(var1::close);
    SceneSrcService var3 =
        new SceneSrcService()
            .src(path)
            .size(Float.intBitsToFloat(1093664768), Float.intBitsToFloat(1093664768))
            .tintColor(-1627389953);
    var2.addChild(var3);
    var2.onHoverChange(
        var1x ->
            var3.animateColor(
                "color", var1x ? -218103809 : -1627389953, SceneEaseHandler.Spring.SNAPPY));
    return var2;
  }

  private LayoutContainerNode createSceneComponent42() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                Float.intBitsToFloat(1126694912),
                ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .padding(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1094713344))
            .gap(Float.intBitsToFloat(1090519040));
    var1.addChild(this.createSceneTextService("ADD"));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1086324736));

    for (String[] var6 : text2) {
      var2.addChild(this.createSceneCornerRadiusService6(var6[0], var6[1]));
    }

    var1.addChild(var2);
    var1.addChild(
        this.createSceneTextService("LAYERS").marginTop(Float.intBitsToFloat(1082130432)));
    this.fjfgtmdxhqe6 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .flex(1.0F)
            .scrollable(true)
            .clip(true)
            .scrollbarWidth(Float.intBitsToFloat(1077936128))
            .scrollbarColor(1157627903);
    this.sceneComponent410 =
        new LayoutContainerNode()
            .size(ScenePctService.pct(Float.intBitsToFloat(1120403456)), 0.0F)
            .direction(ScenePctService.Direction.NONE)
            .padding(
                Float.intBitsToFloat(1090519040),
                Float.intBitsToFloat(1093664768),
                Float.intBitsToFloat(1090519040),
                Float.intBitsToFloat(1093664768));
    this.fjfgtmdxhqe6.addChild(this.sceneComponent410);
    var1.addChild(this.fjfgtmdxhqe6);
    return var1;
  }

  private void updateState() {
    if (this.sceneComponent410 != null && this.f2prt6jl4bc != null && !this.enabled6) {
      this.sceneComponent410.clearChildren();
      if (this.fjfgtmdxhqe6 != null) {
        this.fjfgtmdxhqe6.scrollY(0.0F);
      }

      this.items9.clear();
      this.entries.clear();
      if (this.f2prt6jl4bc.elements.isEmpty()) {
        this.sceneComponent410.size(
            ScenePctService.pct(Float.intBitsToFloat(1120403456)),
            Float.intBitsToFloat(-1082130432));
        this.sceneComponent410.addChild(
            createSceneTextService2(
                    "No elements yet.",
                    Float.intBitsToFloat(1090519040),
                    1795162111,
                    TextMode.REGULAR)
                .marginTop(Float.intBitsToFloat(1082130432))
                .marginLeft(2.0F));
      } else {
        int var1 = this.f2prt6jl4bc.elements.size();

        for (int var2 = 0; var2 < var1; var2++) {
          LayoutIsContainerService var3 = this.f2prt6jl4bc.elements.get(var1 - 1 - var2);
          SceneCornerRadiusService var4 = this.createSceneCornerRadiusService5(var3);
          var4.position(0.0F, var2 * Float.intBitsToFloat(1105723392));
          this.sceneComponent410.addChild(var4);
          this.items9.add(var3);
          this.entries.put(var3, var4);
        }

        this.sceneComponent410.size(
            ScenePctService.pct(Float.intBitsToFloat(1120403456)),
            (var1 - 1) * Float.intBitsToFloat(1105723392)
                + Float.intBitsToFloat(1104150528)
                + Float.intBitsToFloat(1098907648));
      }
    }
  }

  private SceneCornerRadiusService createSceneCornerRadiusService5(LayoutIsContainerService var1) {
    int var2 = this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var1) ? 1 : 0;
    SceneCornerRadiusService var3 =
        new SceneCornerRadiusService()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1104150528))
            .cornerRadius(Float.intBitsToFloat(1088421888))
            .backgroundColor(var2 != 0 ? this.count34 & 16777215 | 771751936 : 0)
            .hoverBackground(419430399)
            .border(0.0F, this.count34 & 16777215 | -1610612736)
            .shadowColor(1711276032)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .padding(0.0F, Float.intBitsToFloat(1086324736))
            .gap(Float.intBitsToFloat(1082130432))
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(() -> this.updateState2(var1));
    HudEditorScreen.LayerGripNode var4 =
        new HudEditorScreen.LayerGripNode(
            1795162111,
            -553648129,
            var2x -> this.updateState3(var1, var2x),
            var1x -> this.updateState4(var1x));
    var4.size(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1101004800));
    var3.addChild(var4);
    SceneTextService var5 =
        createSceneTextService2(
            m6z84l3mpji(var1),
            Float.intBitsToFloat(1091043328),
            var1.visible ? (var2 != 0 ? -218103809 : -553648129) : 1795162111,
            TextMode.REGULAR);
    var5.overflow(SceneTextService.Overflow.ELLIPSIS);
    var5.flex(1.0F);
    var3.addChild(var5);
    var3.onHoverChange(
        var3x ->
            var5.animateColor(
                "color",
                var3x
                    ? -218103809
                    : (var1.visible ? (var2 != 0 ? -218103809 : -553648129) : 1795162111),
                SceneEaseHandler.Spring.SNAPPY));
    var3.addChild(
        this.createSceneComponent43(
            var1.visible ? "eye.svg" : "eye-off.svg",
            Float.intBitsToFloat(1093664768),
            var1.visible ? -1627389953 : 1795162111,
            -218103809,
            () -> var1.visible = !var1.visible));
    var3.addChild(
        this.createSceneComponent43(
            var1.locked ? "lock.svg" : "lock-open.svg",
            Float.intBitsToFloat(1093664768),
            var1.locked ? this.count34 : 1795162111,
            -218103809,
            () -> var1.locked = !var1.locked));
    return var3;
  }

  private LayoutContainerNode createSceneComponent43(
      String var1, float var2, int var3, int var4, Runnable var5) {
    SceneSrcService var6 =
        new SceneSrcService().src(mnciejz4hrw(var1)).size(var2, var2).tintColor(var3);
    LayoutContainerNode var7 =
        new LayoutContainerNode()
            .size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER)
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .stopPropagation(true)
            .onClick(var5)
            .onHoverChange(
                var3x ->
                    var6.animateColor(
                        "color", var3x ? var4 : var3, SceneEaseHandler.Spring.SNAPPY));
    var7.addChild(var6);
    return var7;
  }

  private void updateState2(LayoutIsContainerService var1) {
    if (this.fetqhndloi5c != null) {
      long var2 = CompatAdapterService.windowHandle(Minecraft.getInstance());
      int var4 =
          var2 == 0L || GLFW.glfwGetKey(var2, 340) != 1 && GLFW.glfwGetKey(var2, 344) != 1 ? 0 : 1;
      if (var4 != 0) {
        this.fetqhndloi5c.toggleSelection(var1);
      } else {
        this.fetqhndloi5c.selectOnly(var1);
      }
    }
  }

  private void updateState3(LayoutIsContainerService var1, float var2) {
    SceneCornerRadiusService var3 = this.entries.get(var1);
    if (this.f2prt6jl4bc != null && var3 != null) {
      this.layoutIsContainerService6 = var1;
      this.value8 = var2;
      this.enabled6 = true;
      if (this.sceneComponent410 != null) {
        this.sceneComponent410.bringChildToFront(var3);
      }

      var3.cancelAnimation("y");
      var3.animate("scale", Float.intBitsToFloat(1065688760), SceneEaseHandler.Spring.SNAPPY);
      var3.animate("shadow", Float.intBitsToFloat(1084227584), SceneEaseHandler.Spring.SNAPPY);
      var3.animate("borderWidth", 1.0F, SceneEaseHandler.Spring.SNAPPY);
      var3.animateColor(
          "backgroundColor", this.count34 & 16777215 | 1275068416, SceneEaseHandler.Spring.SNAPPY);
    }
  }

  private void updateState4(float var1) {
    if (this.enabled6 && this.layoutIsContainerService6 != null) {
      SceneCornerRadiusService var2 = this.entries.get(this.layoutIsContainerService6);
      if (var2 != null) {
        int var3 = this.items9.size();
        float var4 = Math.max(0.0F, (var3 - 1) * Float.intBitsToFloat(1105723392));
        var2.y = Math.max(0.0F, Math.min(var4, var2.y + (var1 - this.value8)));
        var2.invalidate();
        this.value8 = var1;
        int var5 = this.items9.indexOf(this.layoutIsContainerService6);
        int var6 =
            Math.max(0, Math.min(var3 - 1, Math.round(var2.y / Float.intBitsToFloat(1105723392))));
        if (var6 != var5 && var5 >= 0) {
          this.items9.remove(var5);
          this.items9.add(var6, this.layoutIsContainerService6);

          for (int var7 = 0; var7 < var3; var7++) {
            LayoutIsContainerService var8 = this.items9.get(var7);
            if (var8 != this.layoutIsContainerService6) {
              SceneCornerRadiusService var9 = this.entries.get(var8);
              if (var9 != null) {
                var9.animate(
                    "y", var7 * Float.intBitsToFloat(1105723392), SceneEaseHandler.Spring.SNAPPY);
              }
            }
          }
        }
      }
    }
  }

  private void updateState5() {
    if (this.enabled6) {
      this.enabled6 = false;
      LayoutIsContainerService var1 = this.layoutIsContainerService6;
      this.layoutIsContainerService6 = null;
      SceneCornerRadiusService var2 = var1 == null ? null : this.entries.get(var1);
      if (var2 != null) {
        int var3 = this.items9.indexOf(var1);
        if (var3 >= 0) {
          var2.animate(
              "y", var3 * Float.intBitsToFloat(1105723392), SceneEaseHandler.Spring.SNAPPY);
        }

        var2.animate("scale", 1.0F, SceneEaseHandler.Spring.SNAPPY);
        var2.animate("shadow", 0.0F, SceneEaseHandler.Spring.SNAPPY);
        var2.animate("borderWidth", 0.0F, SceneEaseHandler.Spring.SNAPPY);
        int var4 = this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var1) ? 1 : 0;
        var2.animateColor(
            "backgroundColor",
            var4 != 0 ? this.count34 & 16777215 | 771751936 : 0,
            SceneEaseHandler.Spring.SNAPPY);
      }

      if (this.f2prt6jl4bc != null && !this.items9.isEmpty()) {
        ArrayList var6 = new ArrayList();

        for (int var7 = this.items9.size() - 1; var7 >= 0; var7 += -1) {
          LayoutIsContainerService var5 = this.items9.get(var7);
          if (this.f2prt6jl4bc.elements.contains(var5)) {
            var6.add(var5);
          }
        }

        if (var6.size() == this.f2prt6jl4bc.elements.size()) {
          this.f2prt6jl4bc.elements.clear();
          this.f2prt6jl4bc.elements.addAll(var6);
          this.text10 = this.createText7();
        }
      } else {
        this.text10 = this.createText7();
      }
    }
  }

  private static Path mnciejz4hrw(String var0) {
    return FabricLoader.getInstance()
        .getModContainer("ellice")
        .flatMap(var1 -> var1.findPath("assets/ellice/components/icons/" + var0))
        .orElse(Path.of("src/main/resources/assets/ellice/components/icons/" + var0));
  }

  private SceneCornerRadiusService createSceneCornerRadiusService6(String var1, String var2) {
    SceneCornerRadiusService var3 =
        new SceneCornerRadiusService()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1107820544))
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
            .onClick(() -> this.updateState6(var1));
    var3.addChild(
        createSceneTextService2(var2, Float.intBitsToFloat(1092616192), -553648129, TextMode.BOLD));
    return var3;
  }

  private void updateState6(String var1) {
    if (this.f2prt6jl4bc != null) {
      LayoutIsContainerService var2 = createLayoutIsContainerService(var1);
      if (var2 != null) {
        int var3 = this.f2prt6jl4bc.elements.size();
        var2.offsetX = var2.offsetX + var3 % 6 * Float.intBitsToFloat(1094713344);
        var2.offsetY = var2.offsetY + var3 % 6 * Float.intBitsToFloat(1094713344);
        this.f2prt6jl4bc.elements.add(var2);
      }
    }
  }

  private static LayoutIsContainerService createLayoutIsContainerService(String var0) {
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

  private LayoutContainerNode createSceneComponent44() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .flex(1.0F)
            .padding(Float.intBitsToFloat(1098907648));
    this.sceneCornerRadiusService2 =
        new SceneCornerRadiusService()
            .flex(1.0F)
            .cornerRadius(Float.intBitsToFloat(1094713344))
            .backgroundColor(855638016)
            .border(Float.intBitsToFloat(1056964608), 822083583)
            .secondaryBorderColor(301989888)
            .clip(true)
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START);
    this.fetqhndloi5c =
        new HudEditorScreen.ScreenSampleNode(this.f2prt6jl4bc, this.renderer, this.f7fr4h364rv);
    this.fetqhndloi5c.flex(1.0F).interactive(true);
    this.sceneCornerRadiusService2.addChild(this.fetqhndloi5c);
    this.sceneCornerRadiusService2.addChild(
        new HudEditorScreen.HudPreviewNode(
            this.fetqhndloi5c, this.f2prt6jl4bc, this.renderer, this.f7fr4h364rv));
    this.sceneCornerRadiusService2.addChild(
        new HudEditorScreen.OverlayNode(this.fetqhndloi5c, this.count34));
    var1.addChild(this.sceneCornerRadiusService2);
    return var1;
  }

  private LayoutContainerNode createSceneComponent45() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                Float.intBitsToFloat(1130102784),
                ScenePctService.pct(Float.intBitsToFloat(1120403456)))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .padding(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1094713344))
            .gap(Float.intBitsToFloat(1090519040));
    var1.addChild(this.createSceneTextService("INSPECTOR"));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
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
    this.sceneTextService2 =
        createSceneTextService2(
            "No element selected", Float.intBitsToFloat(1091567616), -1627389953, TextMode.REGULAR);
    var2.addChild(this.sceneTextService2);
    this.sceneComponent42 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1094713344));
    this.sceneTextService3 =
        createSceneTextService2("", Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD);
    this.sceneComponent42.addChild(this.sceneTextService3);
    this.fuy67uovp93 = this.createSceneComponent427();
    this.sceneComponent42.addChild(this.fuy67uovp93);
    this.sceneComponent43 = this.createSceneComponent424();
    this.sceneComponent43.visible(false);
    this.sceneComponent42.addChild(this.sceneComponent43);
    this.sceneComponent46 = this.createSceneComponent47();
    this.sceneComponent46.visible(false);
    this.sceneComponent42.addChild(this.sceneComponent46);
    this.sceneComponent47 = this.createSceneComponent421();
    this.sceneComponent47.visible(false);
    this.sceneComponent42.addChild(this.sceneComponent47);
    this.sceneComponent48 = this.createSceneComponent423();
    this.sceneComponent48.visible(false);
    this.sceneComponent42.addChild(this.sceneComponent48);
    this.sceneComponent42.addChild(this.createSceneComponent435());
    this.sceneComponent42.addChild(this.createSceneCornerRadiusService10());
    this.sceneComponent42.visible(false);
    var2.addChild(this.sceneComponent42);
    var1.addChild(var2);
    return var1;
  }

  private LayoutContainerNode createSceneComponent46() {
    this.fONTComponent =
        new FONTComponent(
            this.sceneComponent4,
            this.flgozpodfet,
            CoreIsInitializedHandler.get().compositor(),
            this.count34,
            this::updateState7);
    return this.fONTComponent.node();
  }

  private void updateState7(String var1) {
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

  private static boolean checkCondition2(LayoutIsContainerService var0) {
    return "rect".equals(var0.type);
  }

  private LayoutContainerNode createSceneComponent47() {
    this.items5 = this.items;
    this.items6 = this.items2;
    this.items.clear();
    this.items2.clear();
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
    var1.addChild(this.createSceneComponent48());
    var1.addChild(this.createSceneComponent414("FILL", "color", -14671832));
    LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var2.addChild(this.createSceneComponent414("END COLOUR", "gradientEnd", -15724520));
    var1.addChild(this.createSceneComponent417("Gradient", "gradient", false, var2));
    var1.addChild(this.createSceneComponent49());
    var1.addChild(this.mjoqra3lvuyv());
    var1.addChild(this.createSceneComponent411());
    LayoutContainerNode var3 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var3.addChild(
        this.mb1btrwgujbo(
            "AMOUNT", "innerHighlight", 0.0F, 1.0F, Float.intBitsToFloat(1008981770), 0.0F, false));
    var3.addChild(
        this.mb1btrwgujbo(
            "SIZE",
            "innerHighlightSize",
            0.0F,
            Float.intBitsToFloat(1109393408),
            1.0F,
            0.0F,
            true));
    var1.addChild(this.createSceneComponent419("INNER HIGHLIGHT", var3));
    var1.addChild(
        this.mb1btrwgujbo(
            "FEATHER",
            "edgeSoftness",
            0.0F,
            Float.intBitsToFloat(1101004800),
            Float.intBitsToFloat(1056964608),
            0.0F,
            true));
    var1.addChild(
        this.mb1btrwgujbo(
            "BACKDROP BLUR", "blur", 0.0F, Float.intBitsToFloat(1109393408), 1.0F, 0.0F, true));
    LayoutContainerNode var4 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var4.addChild(
        this.mb1btrwgujbo(
            "SATURATION",
            "glassSaturation",
            0.0F,
            2.0F,
            Float.intBitsToFloat(1008981770),
            Float.intBitsToFloat(1066024305),
            false));
    var4.addChild(
        this.createSceneComponent413(
            "BRIGHTNESS",
            "glassBrightness",
            Float.intBitsToFloat(-1090519040),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1008981770),
            Float.intBitsToFloat(-1130113270),
            2));
    var4.addChild(
        this.createSceneComponent413(
            "CONTRAST",
            "glassContrast",
            Float.intBitsToFloat(1056964608),
            2.0F,
            Float.intBitsToFloat(1008981770),
            Float.intBitsToFloat(1065856532),
            2));
    var4.addChild(
        this.mb1btrwgujbo(
            "REFRACTION",
            "glassRefraction",
            0.0F,
            Float.intBitsToFloat(1101004800),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1090519040),
            true));
    var4.addChild(
        this.createSceneComponent413(
            "NOISE",
            "glassNoise",
            0.0F,
            Float.intBitsToFloat(1017370378),
            Float.intBitsToFloat(973279855),
            Float.intBitsToFloat(993493044),
            4));
    var4.addChild(
        this.mb1btrwgujbo(
            "CHROMATIC",
            "glassChromatic",
            0.0F,
            Float.intBitsToFloat(1077936128),
            Float.intBitsToFloat(1028443341),
            Float.intBitsToFloat(1056964608),
            false));
    var4.addChild(this.createSceneComponent414("HIGHLIGHT", "glassHighlightColor", 486539263));
    var1.addChild(
        this.createSceneComponent418(
            "Glass",
            "glass",
            false,
            var4,
            var1x -> {
              if (var1x) {
                LayoutIsContainerService var2x = this.createLayoutIsContainerService2();
                if (var2x != null
                    && LayoutStringService.number(var2x.props, "blur", 0.0F) <= 0.0F) {
                  this.updateState10("blur", Float.intBitsToFloat(1099956224));
                }
              }
            }));
    return var1;
  }

  private LayoutContainerNode createSceneComponent48() {
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2("SIZE", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1090519040));
    this.controlLetterSpacingService3 =
        this.createControlLetterSpacingService(var1x -> this.mimlouseeyin(true, var1x));
    this.controlLetterSpacingService4 =
        this.createControlLetterSpacingService(var1x -> this.mimlouseeyin(false, var1x));
    var2.addChild(this.createSceneComponent431("W", this.controlLetterSpacingService3));
    var2.addChild(this.createSceneComponent431("H", this.controlLetterSpacingService4));
    var1.addChild(var2);
    return var1;
  }

  private void mimlouseeyin(boolean var1, String var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
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

  private LayoutContainerNode createSceneComponent49() {
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var1.addChild(
        createSceneTextService2(
                "CORNERS", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    LayoutContainerNode var3 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.SPACE_BETWEEN);
    var3.addChild(
        createSceneTextService2(
                "Radius", Float.intBitsToFloat(1089470464), -1627389953, TextMode.REGULAR)
            .marginLeft(2.0F));
    this.fcp6k70oa7q =
        createSceneTextService2("4", Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
    var3.addChild(this.fcp6k70oa7q);
    var2.addChild(var3);
    this.builtinComponent42 =
        new CompactSliderControl(
            0.0F,
            Float.intBitsToFloat(1109393408),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1082130432),
            this.count34,
            this.f3dscnabklqo,
            var1x -> {
              this.updateState10("cornerRadius", var1x);
              this.fcp6k70oa7q.text(createText3(var1x, false));
            });
    var2.addChild(this.builtinComponent42);
    this.items5.add(
        () -> {
          float var1x = this.calculateValue("cornerRadius", Float.intBitsToFloat(1082130432));
          this.builtinComponent42.value(var1x);
          this.fcp6k70oa7q.text(createText3(var1x, false));
        });
    var1.addChild(var2);
    LayoutContainerNode var4 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    LayoutContainerNode var5 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .gap(Float.intBitsToFloat(1090519040));
    var5.addChild(this.createSceneComponent415("TL", "cornerTL"));
    var5.addChild(this.createSceneComponent415("TR", "cornerTR"));
    LayoutContainerNode var6 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .gap(Float.intBitsToFloat(1090519040));
    var6.addChild(this.createSceneComponent415("BL", "cornerBL"));
    var6.addChild(this.createSceneComponent415("BR", "cornerBR"));
    var4.addChild(var5);
    var4.addChild(var6);
    var1.addChild(this.createSceneComponent417("Per corner", "perCorner", false, var4));
    return var1;
  }

  private LayoutContainerNode mjoqra3lvuyv() {
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var1.addChild(
        this.mb1btrwgujbo(
            "BORDER",
            "borderWidth",
            0.0F,
            Float.intBitsToFloat(1086324736),
            Float.intBitsToFloat(1048576000),
            0.0F,
            false));
    var1.addChild(this.createSceneComponent414("BORDER COLOUR", "borderColor", -1));
    LayoutContainerNode var2 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var2.addChild(this.createSceneComponent414("BOTTOM COLOUR", "borderColor2", 1090519039));
    var1.addChild(this.createSceneComponent417("2-tone border", "border2", false, var2));
    return var1;
  }

  private LayoutContainerNode createSceneComponent411() {
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var1.addChild(
        this.mb1btrwgujbo(
            "SHADOW", "shadow", 0.0F, Float.intBitsToFloat(1109393408), 1.0F, 0.0F, true));
    var1.addChild(this.createSceneComponent414("SHADOW COLOUR", "shadowColor", 1610612736));
    var1.addChild(this.createSceneComponent416("Inset shadow", "insetShadow", false));
    return var1;
  }

  private LayoutContainerNode mb1btrwgujbo(
      String var1, String var2, float var3, float var4, float var5, float var6, boolean var7) {
    return this.createSceneComponent413(var1, var2, var3, var4, var5, var6, var7 ? -1 : 2);
  }

  private LayoutContainerNode createSceneComponent413(
      String var1, String var2, float var3, float var4, float var5, float var6, int var7) {
    LayoutContainerNode var8 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    LayoutContainerNode var9 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.SPACE_BETWEEN);
    var9.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    SceneTextService var10 =
        createSceneTextService2(
            createText4(var6, var7), Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
    var9.addChild(var10);
    var8.addChild(var9);
    CompactSliderControl var11 =
        new CompactSliderControl(
            var3,
            var4,
            var5,
            var6,
            this.count34,
            this.f3dscnabklqo,
            var4x -> {
              this.updateState10(var2, var4x);
              var10.text(createText4(var4x, var7));
            });
    var8.addChild(var11);
    this.items5.add(
        () -> {
          float var6x = this.calculateValue(var2, var6);
          var11.value(var6x);
          var10.text(createText4(var6x, var7));
        });
    return var8;
  }

  private LayoutContainerNode createSceneComponent414(String var1, String var2, int var3) {
    ColourComponent var4 =
        new ColourComponent(this.sceneComponent4, var2x -> this.updateState12(var2, var2x));
    this.items6.add(var4);
    this.items5.add(() -> var4.setColor(this.calculateValue2(var2, var3)));
    LayoutContainerNode var5 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    var5.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    var5.addChild(var4.node());
    return var5;
  }

  private LayoutContainerNode createSceneComponent415(String var1, String var2) {
    ControlLetterSpacingService var3 =
        this.createControlLetterSpacingService(var2x -> this.updateState13(var2, var2x));
    this.items5.add(
        () -> {
          if (!var3.focused()) {
            var3.text(
                createText5(
                    this.calculateValue(
                        var2,
                        this.calculateValue("cornerRadius", Float.intBitsToFloat(1082130432)))));
          }
        });
    return this.createSceneComponent431(var1, var3);
  }

  private LayoutContainerNode createSceneComponent416(String var1, String var2, boolean var3) {
    ControlCompactService var4 =
        new ControlCompactService()
            .value(var3)
            .activeColor(this.count34)
            .onToggle(var2x -> this.m8qdwolun1w(var2, var2x));
    this.items5.add(() -> var4.value(this.checkCondition3(var2, var3)));
    return this.createSceneComponent434(var1, var4);
  }

  private LayoutContainerNode createSceneComponent417(
      String var1, String var2, boolean var3, LayoutContainerNode var4) {
    return this.createSceneComponent418(var1, var2, var3, var4, null);
  }

  private LayoutContainerNode createSceneComponent418(
      String var1, String var2, boolean var3, LayoutContainerNode var4, Consumer<Boolean> var5) {
    ControlCompactService var6 =
        new ControlCompactService()
            .value(var3)
            .activeColor(this.count34)
            .onToggle(
                var4x -> {
                  this.m8qdwolun1w(var2, var4x);
                  var4.visible(var4x);
                  if (var5 != null) {
                    var5.accept(var4x);
                  }
                });
    this.items5.add(
        () -> {
          boolean var5x = this.checkCondition3(var2, var3);
          var6.value(var5x);
          var4.visible(var5x);
        });
    var4.visible(var3);
    LayoutContainerNode var7 = mftjrxqx4ezo(Float.intBitsToFloat(1090519040));
    var7.addChild(this.createSceneComponent434(var1, var6));
    var7.addChild(var4);
    return var7;
  }

  private LayoutContainerNode createSceneComponent419(String var1, LayoutContainerNode var2) {
    LayoutContainerNode var3 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    var3.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    var3.addChild(var2);
    return var3;
  }

  private static LayoutContainerNode mftjrxqx4ezo(float var0) {
    return new LayoutContainerNode()
        .size(
            ScenePctService.pct(Float.intBitsToFloat(1120403456)),
            Float.intBitsToFloat(-1082130432))
        .direction(ScenePctService.Direction.COLUMN)
        .align(ScenePctService.Align.STRETCH)
        .justify(ScenePctService.Justify.START)
        .gap(var0);
  }

  private void updateState9(LayoutIsContainerService var1) {
    if (var1.props == null) {
      var1.props = new JsonObject();
    }
  }

  private void updateState10(String var1, float var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    if (var3 != null) {
      this.updateState9(var3);
      var3.props.addProperty(var1, var2);
    }
  }

  private void m8qdwolun1w(String var1, boolean var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    if (var3 != null) {
      this.updateState9(var3);
      var3.props.addProperty(var1, var2);
    }
  }

  private void updateState12(String var1, int var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    if (var3 != null) {
      this.updateState9(var3);
      var3.props.addProperty(var1, String.format("#%08X", var2));
    }
  }

  private void updateState13(String var1, String var2) {
    try {
      float var3 = Float.parseFloat(var2.trim());
      if (Float.isFinite(var3)) {
        this.updateState10(var1, var3);
      }
    } catch (Exception var4) {
    }
  }

  private void updateState14(String var1, String var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    if (var3 != null) {
      this.updateState9(var3);
      var3.props.addProperty(var1, var2);
    }
  }

  private float calculateValue(String var1, float var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    return var3 != null ? LayoutStringService.number(var3.props, var1, var2) : var2;
  }

  private int calculateValue2(String var1, int var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    return var3 != null ? LayoutStringService.color(var3.props, var1, var2) : var2;
  }

  private boolean checkCondition3(String var1, boolean var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    return var3 != null ? LayoutStringService.bool(var3.props, var1, var2) : var2;
  }

  private String mezqlymtb7ba(String var1, String var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    return var3 != null ? LayoutStringService.string(var3.props, var1, var2) : var2;
  }

  private static String createText3(float var0, boolean var1) {
    return createText4(var0, var1 ? -1 : 2);
  }

  private static String createText4(float var0, int var1) {
    return var1 < 0
        ? String.valueOf(Math.round(var0))
        : String.format(Locale.ROOT, "%." + var1 + "f", var0);
  }

  private void mfiwpn4wknjg(LayoutIsContainerService var1) {
    for (Runnable var3 : this.items) {
      var3.run();
    }

    this.text8 = this.text9 = "";

    for (ColourComponent var5 : this.items2) {
      var5.close();
    }
  }

  private LayoutContainerNode createSceneComponent421() {
    this.items5 = this.fds2fqndpgdh;
    this.items6 = this.items4;
    this.fds2fqndpgdh.clear();
    this.items4.clear();
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
    var1.addChild(this.mi8augwbkvkf());
    var1.addChild(
        this.mb1btrwgujbo(
            "CHIP SIZE",
            "size",
            Float.intBitsToFloat(1094713344),
            Float.intBitsToFloat(1111490560),
            1.0F,
            Float.intBitsToFloat(1104150528),
            true));
    var1.addChild(
        this.mb1btrwgujbo(
            "GAP",
            "gap",
            0.0F,
            Float.intBitsToFloat(1098907648),
            1.0F,
            Float.intBitsToFloat(1084227584),
            true));
    var1.addChild(this.createSceneComponent414("ACCENT", "accent", -10262799));
    var1.addChild(this.createSceneComponent416("Show empty slots", "showEmpty", false));
    var1.addChild(this.createSceneComponent416("Durability bar", "durabilityBar", true));
    return var1;
  }

  private LayoutContainerNode mi8augwbkvkf() {
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2(
                "ORIENTATION", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1105199104))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1086324736));
    this.sceneCornerRadiusService7 =
        this.createSceneCornerRadiusService7("Horizontal", "horizontal");
    this.sceneCornerRadiusService8 = this.createSceneCornerRadiusService7("Vertical", "vertical");
    var2.addChild(this.sceneCornerRadiusService7);
    var2.addChild(this.sceneCornerRadiusService8);
    var1.addChild(var2);
    this.items5.add(this::updateState16);
    return var1;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService7(String var1, String var2) {
    SceneCornerRadiusService var3 =
        new SceneCornerRadiusService()
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
            .onClick(
                () -> {
                  this.updateState14("orientation", var2);
                  this.updateState16();
                });
    var3.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD));
    return var3;
  }

  private void updateState16() {
    if (this.sceneCornerRadiusService7 != null && this.sceneCornerRadiusService8 != null) {
      int var1 =
          !"vertical".equalsIgnoreCase(this.mezqlymtb7ba("orientation", "horizontal")) ? 1 : 0;
      this.sceneCornerRadiusService7.animateColor(
          "backgroundColor", var1 != 0 ? this.count34 : 452984831, SceneEaseHandler.Spring.SNAPPY);
      this.sceneCornerRadiusService8.animateColor(
          "backgroundColor", var1 != 0 ? 452984831 : this.count34, SceneEaseHandler.Spring.SNAPPY);
    }
  }

  private void updateState17(LayoutIsContainerService var1) {
    for (Runnable var3 : this.fds2fqndpgdh) {
      var3.run();
    }

    for (ColourComponent var5 : this.items4) {
      var5.close();
    }
  }

  private LayoutContainerNode createSceneComponent423() {
    this.items5 = this.items7;
    this.items6 = this.fyh7bfyp88z;
    this.items7.clear();
    this.fyh7bfyp88z.clear();
    LayoutContainerNode var1 = mftjrxqx4ezo(Float.intBitsToFloat(1094713344));
    var1.addChild(
        this.mb1btrwgujbo(
            "KEY SIZE",
            "size",
            Float.intBitsToFloat(1090519040),
            Float.intBitsToFloat(1107296256),
            1.0F,
            Float.intBitsToFloat(1098907648),
            true));
    var1.addChild(
        this.mb1btrwgujbo(
            "GAP",
            "gap",
            0.0F,
            Float.intBitsToFloat(1092616192),
            1.0F,
            Float.intBitsToFloat(1077936128),
            true));
    var1.addChild(
        this.mb1btrwgujbo(
            "RADIUS",
            "cornerRadius",
            0.0F,
            Float.intBitsToFloat(1098907648),
            Float.intBitsToFloat(1056964608),
            Float.intBitsToFloat(1082130432),
            false));
    var1.addChild(
        this.mb1btrwgujbo(
            "BLUR", "blur", 0.0F, Float.intBitsToFloat(1106247680), 1.0F, 0.0F, true));
    this.feemtwsjsd4d =
        new FONTComponent(
            this.sceneComponent4,
            this.flgozpodfet,
            CoreIsInitializedHandler.get().compositor(),
            this.count34,
            this::updateState7);
    var1.addChild(this.feemtwsjsd4d.node());
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1092616192));
    var2.addChild(this.createSceneComponent416("Mouse", "showMouse", true));
    var2.addChild(this.createSceneComponent416("Space", "showSpace", true));
    var1.addChild(this.createSceneComponent419("KEYS", var2));
    var1.addChild(this.createSceneComponent416("Liquid shader", "liquid", true));
    var1.addChild(this.createSceneComponent414("IDLE", "idleColor", 1713381424));
    var1.addChild(this.createSceneComponent414("PRESSED", "pressColor", -530882070));
    var1.addChild(this.createSceneComponent414("TEXT", "textColor", -218103809));
    var1.addChild(this.createSceneComponent414("ACCENT / GLOW", "accent", -10262799));
    return var1;
  }

  private void updateState18(LayoutIsContainerService var1) {
    for (Runnable var3 : this.items7) {
      var3.run();
    }

    for (ColourComponent var5 : this.fyh7bfyp88z) {
      var5.close();
    }
  }

  private void updateState19(LayoutIsContainerService var1) {
    if (this.controlLetterSpacingService3 != null && !this.controlLetterSpacingService3.focused()) {
      String var2 =
          var1.width == Float.intBitsToFloat(-1082130432) ? "auto" : createText5(var1.width);
      if (!var2.equals(this.text8)) {
        this.text8 = var2;
        this.controlLetterSpacingService3.text(var2);
      }
    }

    if (this.controlLetterSpacingService4 != null && !this.controlLetterSpacingService4.focused()) {
      String var3 =
          var1.height == Float.intBitsToFloat(-1082130432) ? "auto" : createText5(var1.height);
      if (!var3.equals(this.text9)) {
        this.text9 = var3;
        this.controlLetterSpacingService4.text(var3);
      }
    }

    if (this.builtinComponent42 != null && !this.builtinComponent42.isPressed()) {
      float var4 =
          LayoutStringService.number(var1.props, "cornerRadius", Float.intBitsToFloat(1082130432));
      if (Math.abs(var4 - this.builtinComponent42.value()) > Float.intBitsToFloat(1008981770)) {
        this.builtinComponent42.value(var4);
        if (this.fcp6k70oa7q != null) {
          this.fcp6k70oa7q.text(createText3(var4, false));
        }
      }
    }
  }

  private LayoutContainerNode createSceneComponent424() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1094713344));
    var1.addChild(this.createSceneComponent46());
    var1.addChild(this.miudbxrqxvwf());
    var1.addChild(this.createSceneComponent426());
    return var1;
  }

  private LayoutContainerNode miudbxrqxvwf() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .gap(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2(
                "STYLE", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1104150528))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1086324736));
    this.f1oxmbxecj6 =
        this.createSceneCornerRadiusService8(
            "B", TextMode.BOLD, () -> this.updateState20(true, false));
    this.sceneCornerRadiusService4 =
        this.createSceneCornerRadiusService8(
            "I", TextMode.ITALIC, () -> this.updateState20(false, true));
    this.sceneCornerRadiusService5 =
        this.createSceneCornerRadiusService8("U", TextMode.REGULAR, this::updateState21);
    var2.addChild(this.f1oxmbxecj6);
    var2.addChild(this.sceneCornerRadiusService4);
    var2.addChild(this.sceneCornerRadiusService5);
    var1.addChild(var2);
    return var1;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService8(
      String var1, TextMode var2, Runnable var3) {
    SceneCornerRadiusService var4 =
        new SceneCornerRadiusService()
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
    var4.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1092616192), -553648129, var2));
    return var4;
  }

  private LayoutContainerNode createSceneComponent426() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .gap(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2(
                "COLOR", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    this.colourComponent = new ColourComponent(this.sceneComponent4, this::updateState22);
    var1.addChild(this.colourComponent.node());
    return var1;
  }

  private void updateState20(boolean var1, boolean var2) {
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

      var3.props.addProperty(
          "variant", var5 && var6 ? "bolditalic" : (var5 ? "bold" : (var6 ? "italic" : "regular")));
      this.updateState23(var3);
    }
  }

  private void updateState21() {
    LayoutIsContainerService var1 = this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
    if (var1 != null) {
      if (var1.props == null) {
        var1.props = new JsonObject();
      }

      var1.props.addProperty(
          "underline", !LayoutStringService.bool(var1.props, "underline", false));
      this.updateState23(var1);
    }
  }

  private void updateState22(int var1) {
    LayoutIsContainerService var2 = this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
    if (var2 != null) {
      if (var2.props == null) {
        var2.props = new JsonObject();
      }

      var2.props.addProperty("color", String.format("#%08X", Integer.valueOf(var1)));
    }
  }

  private void updateState23(LayoutIsContainerService var1) {
    String var2 = LayoutStringService.string(var1.props, "variant", "regular").toLowerCase();
    this.updateState24(this.f1oxmbxecj6, var2.contains("bold"));
    this.updateState24(this.sceneCornerRadiusService4, var2.contains("italic"));
    this.updateState24(
        this.sceneCornerRadiusService5, LayoutStringService.bool(var1.props, "underline", false));
  }

  private void updateState24(SceneCornerRadiusService var1, boolean var2) {
    if (var1 != null) {
      int var3 = this.count34 & 16777215 | 1073741824;
      var1.animateColor("backgroundColor", var2 ? var3 : 452984831, SceneEaseHandler.Spring.SNAPPY);
      var1.animateColor("gradientEnd", var2 ? var3 : 352321535, SceneEaseHandler.Spring.SNAPPY);
      var1.animateColor(
          "borderColor", var2 ? this.count34 : 822083583, SceneEaseHandler.Spring.SNAPPY);
    }
  }

  private LayoutContainerNode createSceneComponent427() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1094713344));
    var1.addChild(this.mgtjt5upsc9());
    var1.addChild(this.createSceneComponent430());
    var1.addChild(this.createSceneComponent432());
    var1.addChild(this.createSceneComponent433());
    return var1;
  }

  private LayoutContainerNode mgtjt5upsc9() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.START)
            .gap(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2(
                "ANCHOR", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    SceneCornerRadiusService var2 =
        new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1115947008), Float.intBitsToFloat(1115947008))
            .cornerRadius(Float.intBitsToFloat(1090519040))
            .backgroundColor(855638016)
            .border(Float.intBitsToFloat(1056964608), 822083583)
            .secondaryBorderColor(301989888)
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .justify(ScenePctService.Justify.START);

    for (int var3 = 0; var3 < 3; var3++) {
      LayoutContainerNode var4 =
          new LayoutContainerNode()
              .size(
                  ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                  Float.intBitsToFloat(-1082130432))
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
    LayoutContainerNode var3 =
        new LayoutContainerNode()
            .flex(1.0F)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER)
            .cursorStyle(ScenePctService.CursorStyle.POINTER)
            .onClick(() -> this.updateState25(var1, var2));
    SceneCornerRadiusService var4 =
        new SceneCornerRadiusService()
            .size(Float.intBitsToFloat(1084227584), Float.intBitsToFloat(1084227584))
            .cornerRadius(Float.intBitsToFloat(1075838976))
            .backgroundColor(1090519039);
    this.sceneCornerRadiusService6[var2 * 3 + var1] = var4;
    var3.addChild(var4);
    return var3;
  }

  private void updateState25(int var1, int var2) {
    LayoutIsContainerService var3 = this.createLayoutIsContainerService2();
    if (var3 != null) {
      LayoutCodec.H var4 =
          var1 == 0 ? LayoutCodec.H.LEFT : (var1 == 1 ? LayoutCodec.H.CENTER : LayoutCodec.H.RIGHT);
      LayoutCodec.V var5 =
          var2 == 0 ? LayoutCodec.V.TOP : (var2 == 1 ? LayoutCodec.V.CENTER : LayoutCodec.V.BOTTOM);
      LayoutCodec var6 = new LayoutCodec(var4, var5);
      if (!var6.equals(var3.anchor)) {
        var3.anchor = var6;
        var3.offsetX = 0.0F;
        var3.offsetY = 0.0F;
        this.text6 = this.text7 = "";
      }

      this.updateState26(var3);
    }
  }

  private void updateState26(LayoutIsContainerService var1) {
    int var2 = var1.anchor.v.ordinal() * 3 + var1.anchor.h.ordinal();

    for (int var3 = 0; var3 < 9; var3++) {
      SceneCornerRadiusService var4 = this.sceneCornerRadiusService6[var3];
      if (var4 != null) {
        int var5 = var3 == var2 ? 1 : 0;
        var4.animateColor(
            "backgroundColor",
            var5 != 0 ? this.count34 : 1090519039,
            SceneEaseHandler.Spring.SNAPPY);
        var4.animate(
            "width",
            var5 != 0 ? Float.intBitsToFloat(1088421888) : Float.intBitsToFloat(1084227584),
            SceneEaseHandler.Spring.SNAPPY);
        var4.animate(
            "height",
            var5 != 0 ? Float.intBitsToFloat(1088421888) : Float.intBitsToFloat(1084227584),
            SceneEaseHandler.Spring.SNAPPY);
      }
    }
  }

  private LayoutContainerNode createSceneComponent430() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .gap(Float.intBitsToFloat(1084227584));
    var1.addChild(
        createSceneTextService2(
                "OFFSET", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1090519040));
    this.controlLetterSpacingService = this.createControlLetterSpacingService(this::updateState27);
    this.controlLetterSpacingService2 = this.createControlLetterSpacingService(this::updateState28);
    var2.addChild(this.createSceneComponent431("X", this.controlLetterSpacingService));
    var2.addChild(this.createSceneComponent431("Y", this.controlLetterSpacingService2));
    var1.addChild(var2);
    return var1;
  }

  private ControlLetterSpacingService createControlLetterSpacingService(Consumer<String> var1) {
    ControlLetterSpacingService var2 =
        new ControlLetterSpacingService()
            .fontSize(Float.intBitsToFloat(1091567616))
            .textColor(-553648129)
            .bgColor(452984831)
            .focusBorder(this.count34)
            .cornerRadius(Float.intBitsToFloat(1088421888))
            .maxLength(8)
            .onChanged(var1);
    var2.size(Float.intBitsToFloat(-1082130432), Float.intBitsToFloat(1103101952)).flex(1.0F);
    return var2;
  }

  private LayoutContainerNode createSceneComponent431(
      String var1, ControlLetterSpacingService var2) {
    LayoutContainerNode var3 =
        new LayoutContainerNode()
            .flex(1.0F)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1084227584));
    var3.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1090519040), 1795162111, TextMode.BOLD));
    var3.addChild(var2);
    return var3;
  }

  private void updateState27(String var1) {
    LayoutIsContainerService var2 = this.createLayoutIsContainerService2();
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

  private void updateState28(String var1) {
    LayoutIsContainerService var2 = this.createLayoutIsContainerService2();
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

  private void updateState29(LayoutIsContainerService var1) {
    if (this.controlLetterSpacingService != null && !this.controlLetterSpacingService.focused()) {
      String var2 = createText5(var1.offsetX);
      if (!var2.equals(this.text6)) {
        this.text6 = var2;
        this.controlLetterSpacingService.text(var2);
      }
    }

    if (this.controlLetterSpacingService2 != null && !this.controlLetterSpacingService2.focused()) {
      String var3 = createText5(var1.offsetY);
      if (!var3.equals(this.text7)) {
        this.text7 = var3;
        this.controlLetterSpacingService2.text(var3);
      }
    }
  }

  private static String createText5(float var0) {
    return var0 == Math.rint(var0)
        ? String.valueOf((int) var0)
        : String.format(Locale.ROOT, "%.1f", var0);
  }

  private LayoutContainerNode createSceneComponent432() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.COLUMN)
            .align(ScenePctService.Align.STRETCH)
            .gap(Float.intBitsToFloat(1084227584));
    LayoutContainerNode var2 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.SPACE_BETWEEN);
    var2.addChild(
        createSceneTextService2(
                "OPACITY", Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
            .marginLeft(2.0F));
    this.sceneTextService4 =
        createSceneTextService2(
            "1.00", Float.intBitsToFloat(1089470464), -1627389953, TextMode.BOLD);
    var2.addChild(this.sceneTextService4);
    var1.addChild(var2);
    this.builtinComponent4 =
        new CompactSliderControl(
            0.0F,
            1.0F,
            Float.intBitsToFloat(1008981770),
            1.0F,
            this.count34,
            this.f3dscnabklqo,
            var1x -> {
              LayoutIsContainerService var2x = this.createLayoutIsContainerService2();
              if (var2x != null) {
                var2x.opacity = var1x;
              }
            });
    var1.addChild(this.builtinComponent4);
    return var1;
  }

  private LayoutContainerNode createSceneComponent433() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1092616192));
    this.controlCompactService =
        new ControlCompactService()
            .value(true)
            .activeColor(this.count34)
            .onToggle(
                var1x -> {
                  LayoutIsContainerService var2 = this.createLayoutIsContainerService2();
                  if (var2 != null) {
                    var2.visible = var1x;
                  }
                });
    this.controlCompactService2 =
        new ControlCompactService()
            .value(false)
            .activeColor(this.count34)
            .onToggle(
                var1x -> {
                  LayoutIsContainerService var2 = this.createLayoutIsContainerService2();
                  if (var2 != null) {
                    var2.locked = var1x;
                  }
                });
    var1.addChild(this.createSceneComponent434("Visible", this.controlCompactService));
    var1.addChild(this.createSceneComponent434("Lock", this.controlCompactService2));
    return var1;
  }

  private LayoutContainerNode createSceneComponent434(String var1, ControlCompactService var2) {
    var2.size(Float.intBitsToFloat(1106247680), Float.intBitsToFloat(1099431936));
    LayoutContainerNode var3 =
        new LayoutContainerNode()
            .flex(1.0F)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.SPACE_BETWEEN)
            .gap(Float.intBitsToFloat(1086324736));
    var3.addChild(
        createSceneTextService2(
            var1, Float.intBitsToFloat(1091043328), -553648129, TextMode.REGULAR));
    var3.addChild(var2);
    return var3;
  }

  private void updateState30(LayoutIsContainerService var1) {
    this.updateState26(var1);
    if (this.builtinComponent4 != null) {
      this.builtinComponent4.value(var1.opacity);
    }

    if (this.controlCompactService != null) {
      this.controlCompactService.value(var1.visible);
    }

    if (this.controlCompactService2 != null) {
      this.controlCompactService2.value(var1.locked);
    }

    this.text6 = this.text7 = "";
  }

  private LayoutIsContainerService createLayoutIsContainerService2() {
    return this.fetqhndloi5c != null ? this.fetqhndloi5c.selected() : null;
  }

  private float calculateValue3() {
    Window var1 = Minecraft.getInstance().getWindow();
    return var1 != null ? var1.getGuiScaledWidth() : 0.0F;
  }

  private float calculateValue4() {
    Window var1 = Minecraft.getInstance().getWindow();
    return var1 != null ? var1.getGuiScaledHeight() : 0.0F;
  }

  private LayoutContainerNode createSceneComponent435() {
    LayoutContainerNode var1 =
        new LayoutContainerNode()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(-1082130432))
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.START)
            .gap(Float.intBitsToFloat(1090519040));
    var1.addChild(this.createSceneCornerRadiusService9("Copy style", this::updateState34));
    var1.addChild(this.createSceneCornerRadiusService9("Paste style", this::updateState35));
    return var1;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService9(String var1, Runnable var2) {
    SceneCornerRadiusService var3 =
        new SceneCornerRadiusService()
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
    var3.addChild(
        createSceneTextService2(var1, Float.intBitsToFloat(1091567616), -553648129, TextMode.BOLD));
    return var3;
  }

  private SceneCornerRadiusService createSceneCornerRadiusService10() {
    SceneCornerRadiusService var1 =
        new SceneCornerRadiusService()
            .size(
                ScenePctService.pct(Float.intBitsToFloat(1120403456)),
                Float.intBitsToFloat(1108344832))
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
            .onClick(this::updateState31);
    var1.addChild(
        new SceneSrcService()
            .src(path2)
            .size(Float.intBitsToFloat(1095761920), Float.intBitsToFloat(1095761920))
            .tintColor(-25958));
    var1.addChild(
        createSceneTextService2("Delete", Float.intBitsToFloat(1092091904), -25958, TextMode.BOLD));
    return var1;
  }

  private void updateState31() {
    if (this.fetqhndloi5c != null && this.f2prt6jl4bc != null) {
      List var1 = this.fetqhndloi5c.selection();
      if (!var1.isEmpty()) {
        this.f2prt6jl4bc.elements.removeAll(var1);
        this.fetqhndloi5c.deselect();
        this.fbkjdm8rmyhv = false;
        this.enabled2 = false;
        this.layoutIsContainerService = null;
        this.enabled3 = false;
        this.layoutIsContainerService3 = null;
        this.enabled4 = false;
        this.layoutIsContainerService4 = null;
        this.enabled5 = false;
        this.layoutIsContainerService5 = null;
        if (this.colourComponent != null) {
          this.colourComponent.close();
        }

        if (this.fONTComponent != null) {
          this.fONTComponent.close();
        }

        if (this.feemtwsjsd4d != null) {
          this.feemtwsjsd4d.close();
        }

        for (ColourComponent var3 : this.items2) {
          var3.close();
        }

        for (ColourComponent var6 : this.items4) {
          var6.close();
        }

        for (ColourComponent var7 : this.fyh7bfyp88z) {
          var7.close();
        }

        if (this.sceneComponent46 != null) {
          this.sceneComponent46.visible(false);
        }

        if (this.sceneComponent47 != null) {
          this.sceneComponent47.visible(false);
        }

        if (this.sceneComponent48 != null) {
          this.sceneComponent48.visible(false);
        }

        if (this.sceneTextService2 != null) {
          this.sceneTextService2.visible(true);
        }

        if (this.sceneComponent42 != null) {
          this.sceneComponent42.visible(false);
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
          this.updateState32();
          return true;
        }
        break;
      case 67:
        if (var4 != 0 && var5 != 0) {
          this.updateState34();
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
          this.updateState35();
          return true;
        }
        break;
      case 259:
      case 261:
        if (this.fetqhndloi5c != null && !this.fetqhndloi5c.selection().isEmpty()) {
          this.updateState31();
          return true;
        }
    }

    return false;
  }

  private void updateState32() {
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

      this.updateState36("Saved");
    }
  }

  private void updateState34() {
    LayoutIsContainerService var1 = this.createLayoutIsContainerService2();
    if (var1 != null) {
      JsonObject var2 = (var1.props != null ? var1.props : new JsonObject()).deepCopy();
      var2.remove("text");
      this.ftxajmi19ob = var2;
      this.value7 = var1.opacity;
      this.updateState36("Style copied");
    }
  }

  private void updateState35() {
    if (this.ftxajmi19ob != null && this.fetqhndloi5c != null) {
      List var1 = this.fetqhndloi5c.selection();
      if (!var1.isEmpty()) {
        for (LayoutIsContainerService var3 :
            (Iterable<LayoutIsContainerService>) (Iterable<?>) (var1)) {
          if (var3.props == null) {
            var3.props = new JsonObject();
          }

          for (Entry var5 : this.ftxajmi19ob.entrySet()) {
            var3.props.add((String) var5.getKey(), ((JsonElement) var5.getValue()).deepCopy());
          }

          var3.opacity = this.value7;
        }

        this.layoutIsContainerService2 = null;
        this.layoutIsContainerService = null;
        this.layoutIsContainerService3 = null;
        this.layoutIsContainerService4 = null;
        this.layoutIsContainerService5 = null;
        this.updateState36("Style pasted");
      }
    }
  }

  private void updateState36(String var1) {
    if (this.sceneTextService6 != null) {
      this.sceneTextService6.text(var1);
      this.sceneTextService6.opacity(1.0F);
      this.timestamp = System.currentTimeMillis() + 1400L;
    }
  }

  private static String m6z84l3mpji(LayoutIsContainerService var0) {
    if (var0.type != null) {
      for (String[] var4 : text2) {
        if (var4[0].equals(var0.type)) {
          return var4[1];
        }
      }
    }

    return var0.type != null ? var0.type : "Element";
  }

  private SceneTextService createSceneTextService(String var1) {
    return createSceneTextService2(
            var1, Float.intBitsToFloat(1088421888), 1795162111, TextMode.BOLD)
        .marginLeft(Float.intBitsToFloat(1086324736))
        .marginBottom(Float.intBitsToFloat(1082130432));
  }

  @Override
  public void tick(ScreenScreenIdService var1) {
    if (!this.items10.isEmpty()) {
      float var2 = var1.deltaTime();
      Iterator var3 = this.items10.iterator();

      while (var3.hasNext()) {
        HudEditorScreen.PendingAnim var4 = (HudEditorScreen.PendingAnim) var3.next();
        var4.delay -= var2;
        if (var4.delay <= 0.0F) {
          var4.node.animate(
              "opacity", 1.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1048911544)));
          var3.remove();
        }
      }
    }

    if (this.sceneTextService != null && this.fetqhndloi5c != null) {
      float var10 = this.fetqhndloi5c.zoom();
      String var14 =
          var10 <= Float.intBitsToFloat(1065437102)
              ? "Fit"
              : Math.round(var10 * Float.intBitsToFloat(1120403456)) + "%";
      if (!var14.equals(this.text3)) {
        this.text3 = var14;
        this.sceneTextService.text(var14);
      }
    }

    if (this.fetqhndloi5c != null) {
      LayoutIsContainerService var11 = this.fetqhndloi5c.selected();
      boolean var15 = var11 != null;
      if (var15 != this.fbkjdm8rmyhv) {
        this.fbkjdm8rmyhv = var15;
        if (this.sceneTextService2 != null) {
          this.sceneTextService2.visible(!var15);
        }

        if (this.sceneComponent42 != null) {
          this.sceneComponent42.visible(var15);
        }
      }

      if (var15 && this.sceneTextService3 != null) {
        String var16 = m6z84l3mpji(var11);
        if (!var16.equals(this.text4)) {
          this.text4 = var16;
          this.sceneTextService3.text(var16);
        }
      }

      if (var15) {
        if (var11 != this.layoutIsContainerService2) {
          this.layoutIsContainerService2 = var11;
          this.updateState30(var11);
        }

        this.updateState29(var11);
        if (this.sceneTextService4 != null && this.builtinComponent4 != null) {
          String var17 = String.format(Locale.ROOT, "%.2f", this.builtinComponent4.value());
          if (!var17.equals(this.text5)) {
            this.text5 = var17;
            this.sceneTextService4.text(var17);
          }
        }
      } else {
        this.layoutIsContainerService2 = null;
      }

      boolean var18 = var15 && malgeiokaae(var11);
      if (var18 != this.enabled2) {
        this.enabled2 = var18;
        if (this.sceneComponent43 != null) {
          this.sceneComponent43.visible(var18);
        }

        this.layoutIsContainerService = null;
        if (!var18) {
          if (this.colourComponent != null) {
            this.colourComponent.close();
          }

          if (this.fONTComponent != null) {
            this.fONTComponent.close();
          }
        }
      }

      if (var18) {
        if (var11 != this.layoutIsContainerService) {
          this.layoutIsContainerService = var11;
          if (this.colourComponent != null) {
            this.colourComponent.close();
            this.colourComponent.setColor(LayoutStringService.color(var11.props, "color", -1));
          }

          if (this.fONTComponent != null) {
            this.fONTComponent.setSelection(LayoutStringService.string(var11.props, "font", ""));
          }

          this.updateState23(var11);
        }
      } else {
        this.layoutIsContainerService = null;
      }

      boolean var5 = var15 && checkCondition2(var11);
      if (var5 != this.enabled3) {
        this.enabled3 = var5;
        if (this.sceneComponent46 != null) {
          this.sceneComponent46.visible(var5);
        }

        if (!var5) {
          for (ColourComponent var7 : this.items2) {
            var7.close();
          }
        }

        this.layoutIsContainerService3 = null;
      }

      if (var5) {
        if (var11 != this.layoutIsContainerService3) {
          this.layoutIsContainerService3 = var11;
          this.mfiwpn4wknjg(var11);
        }

        this.updateState19(var11);
      } else {
        this.layoutIsContainerService3 = null;
      }

      boolean var20 = var15 && "armorhud".equals(var11.type);
      if (var20 != this.enabled4) {
        this.enabled4 = var20;
        if (this.sceneComponent47 != null) {
          this.sceneComponent47.visible(var20);
        }

        if (!var20) {
          for (ColourComponent var8 : this.items4) {
            var8.close();
          }
        }

        this.layoutIsContainerService4 = null;
      }

      if (var20) {
        if (var11 != this.layoutIsContainerService4) {
          this.layoutIsContainerService4 = var11;
          this.updateState17(var11);
        }
      } else {
        this.layoutIsContainerService4 = null;
      }

      boolean var22 = var15 && "keystrokes".equals(var11.type);
      if (var22 != this.enabled5) {
        this.enabled5 = var22;
        if (this.sceneComponent48 != null) {
          this.sceneComponent48.visible(var22);
        }

        if (!var22) {
          for (ColourComponent var9 : this.fyh7bfyp88z) {
            var9.close();
          }

          if (this.feemtwsjsd4d != null) {
            this.feemtwsjsd4d.close();
          }
        }

        this.layoutIsContainerService5 = null;
      }

      if (var22) {
        if (var11 != this.layoutIsContainerService5) {
          this.layoutIsContainerService5 = var11;
          this.updateState18(var11);
          if (this.feemtwsjsd4d != null) {
            this.feemtwsjsd4d.setSelection(LayoutStringService.string(var11.props, "font", ""));
          }
        }
      } else {
        this.layoutIsContainerService5 = null;
      }
    }

    if (this.fONTComponent != null) {
      this.fONTComponent.tick();
    }

    if (this.feemtwsjsd4d != null) {
      this.feemtwsjsd4d.tick();
    }

    if (this.enabled6) {
      long var12 = CompatAdapterService.windowHandle(Minecraft.getInstance());
      int var19 = var12 != 0L && GLFW.glfwGetMouseButton(var12, 0) == 1 ? 1 : 0;
      if (var19 == 0) {
        this.updateState5();
      }
    }

    String var13 = this.createText7();
    if (!var13.equals(this.text10)) {
      this.text10 = var13;
      this.updateState();
    }

    if (this.sceneTextService6 != null
        && this.timestamp > 0L
        && System.currentTimeMillis() > this.timestamp) {
      this.timestamp = 0L;
      this.sceneTextService6.animate(
          "opacity", 0.0F, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1053609165)));
    }
  }

  private String createText7() {
    if (this.f2prt6jl4bc == null) {
      return "";
    }

    StringBuilder var1 = new StringBuilder();

    for (LayoutIsContainerService var3 : this.f2prt6jl4bc.elements) {
      var1.append(System.identityHashCode(var3))
          .append((char) (var3.visible ? '1' : '0'))
          .append((char) (var3.locked ? '1' : '0'))
          .append(
              (char) (this.fetqhndloi5c != null && this.fetqhndloi5c.isSelected(var3) ? 'S' : '.'))
          .append(';');
    }

    var1.append("|d")
        .append(this.enabled6 ? System.identityHashCode(this.layoutIsContainerService6) : 0);
    return var1.toString();
  }

  @Override
  public void onClose(ScreenScreenIdService var1) {
    this.items10.clear();
  }

  private static SceneTextService createSceneTextService2(
      String var0, float var1, int var2, TextMode var3) {
    SceneTextService var4 = new SceneTextService().text(var0).fontSize(var1).color(var2);
    if (var3 != null && var3 != TextMode.REGULAR) {
      var4.fontVariant(var3);
    }

    return var4;
  }

  private void updateState37(ScenePctService<?> var1, float var2) {
    this.items10.add(new HudEditorScreen.PendingAnim(var1, var2));
  }

  record Guide(boolean vertical, float pos, float a, float b) {}

  static final class HudPreviewNode extends ScenePctService<HudEditorScreen.HudPreviewNode> {
    private final HudEditorScreen.ScreenSampleNode ffsqhicehemw;
    private final LayoutFindSelector f5ya2y9vtox;
    private final LayoutRenderer renderer2;
    private final LayoutOperationHandler feyiy0zcotls;

    HudPreviewNode(
        HudEditorScreen.ScreenSampleNode var1,
        LayoutFindSelector var2,
        LayoutRenderer var3,
        LayoutOperationHandler var4) {
      this.ffsqhicehemw = var1;
      this.f5ya2y9vtox = var2;
      this.renderer2 = var3;
      this.feyiy0zcotls = var4;
      this.layerBreak(true);
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService var1) {
      if (this.f5ya2y9vtox != null && this.renderer2 != null) {
        float var2 = this.ffsqhicehemw.fitW;
        if (!(var2 <= 0.0F) && !(this.ffsqhicehemw.fitH <= 0.0F)) {
          Window var3 = Minecraft.getInstance().getWindow();
          if (var3 != null) {
            float var4 = var3.getGuiScaledWidth();
            float var5 = var3.getGuiScaledHeight();
            if (!(var4 <= 0.0F) && !(var5 <= 0.0F)) {
              float var6 = var2 / var4;
              this.renderer2.render(
                  var1,
                  this.f5ya2y9vtox,
                  this.ffsqhicehemw.fitX,
                  this.ffsqhicehemw.fitY,
                  var4,
                  var5,
                  var6,
                  this.feyiy0zcotls);
            }
          }
        }
      }
    }
  }

  static final class LayerGripNode extends ScenePctService<HudEditorScreen.LayerGripNode> {
    private final int count36;
    private final int count37;
    private final Consumer<Float> consumer;
    private final Consumer<Float> consumer2;

    LayerGripNode(int var1, int var2, Consumer<Float> var3, Consumer<Float> var4) {
      this.count36 = var1;
      this.count37 = var2;
      this.consumer = var3;
      this.consumer2 = var4;
      this.interactive(true);
      this.stopPropagation(true);
      this.cursorStyle(ScenePctService.CursorStyle.POINTER);
    }

    @Override
    protected void onPress(float var1, float var2) {
      if (this.consumer != null) {
        this.consumer.accept(var2);
      }
    }

    @Override
    protected void updateWhilePressed(float var1, float var2) {
      if (this.consumer2 != null) {
        this.consumer2.accept(var2);
      }
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService var1) {
      float var2 =
          Math.min(this.cw * Float.intBitsToFloat(1058642330), Float.intBitsToFloat(1088421888));
      float var3 = Float.intBitsToFloat(1068708659);
      float var4 = Float.intBitsToFloat(1076258406);
      float var5 = this.cx + (this.cw - var2) * Float.intBitsToFloat(1056964608);
      float var6 = var3 * Float.intBitsToFloat(1077936128) + var4 * 2.0F;
      float var7 = this.cy + (this.ch - var6) * Float.intBitsToFloat(1056964608);
      int var8 =
          calculateValue5(
              !this.isHovered() && !this.isPressed() ? this.count36 : this.count37,
              this.effectiveOpacity);

      for (int var9 = 0; var9 < 3; var9++) {
        var1.roundedRect(
            var5,
            var7 + var9 * (var3 + var4),
            var2,
            var3,
            var3 * Float.intBitsToFloat(1056964608),
            var8);
      }
    }

    private static int calculateValue5(int var0, float var1) {
      int var2 = Math.round((var0 >>> 24 & 0xFF) * Math.max(0.0F, Math.min(1.0F, var1)));
      return var2 << 24 | var0 & 16777215;
    }
  }

  static final class OverlayNode extends ScenePctService<HudEditorScreen.OverlayNode> {
    private static final int count38 = -1292135;
    private static final int count39 = -14494738;
    private final HudEditorScreen.ScreenSampleNode screenSampleNode3;
    private final int count40;

    OverlayNode(HudEditorScreen.ScreenSampleNode var1, int var2) {
      this.screenSampleNode3 = var1;
      this.count40 = var2;
      this.layerBreak(true);
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService var1) {
      for (HudEditorScreen.Guide var3 : this.screenSampleNode3.guides()) {
        if (var3.vertical()) {
          var1.roundedRect(
              var3.pos() - Float.intBitsToFloat(1056964608),
              var3.a(),
              1.0F,
              var3.b() - var3.a(),
              0.0F,
              -1292135);
        } else {
          var1.roundedRect(
              var3.a(),
              var3.pos() - Float.intBitsToFloat(1056964608),
              var3.b() - var3.a(),
              1.0F,
              0.0F,
              -1292135);
        }
      }

      for (HudEditorScreen.SpacingBadge var14 : this.screenSampleNode3.spacingBadges()) {
        this.updateState38(var1, var14);
      }

      float var13 = Float.intBitsToFloat(1069547520);

      for (LayoutRenderer.Rect var4 : this.screenSampleNode3.selectionScreenRects()) {
        var1.roundedRect(var4.x(), var4.y(), var4.w(), var13, 0.0F, this.count40);
        var1.roundedRect(
            var4.x(), var4.y() + var4.h() - var13, var4.w(), var13, 0.0F, this.count40);
        var1.roundedRect(var4.x(), var4.y(), var13, var4.h(), 0.0F, this.count40);
        var1.roundedRect(
            var4.x() + var4.w() - var13, var4.y(), var13, var4.h(), 0.0F, this.count40);
      }

      LayoutRenderer.Rect var16 =
          this.screenSampleNode3.selectionSize() == 1
              ? this.screenSampleNode3.selectedScreenRect()
              : null;
      if (var16 != null
          && var16.w() > Float.intBitsToFloat(1101529088)
          && var16.h() > Float.intBitsToFloat(1101529088)) {
        float var17 = Float.intBitsToFloat(1090519040);
        float var5 = Float.intBitsToFloat(1084227584);

        for (int[] var9 : HudEditorScreen.int2) {
          float var10 = var16.x() + (var9[0] + 1) * Float.intBitsToFloat(1056964608) * var16.w();
          float var11 = var16.y() + (var9[1] + 1) * Float.intBitsToFloat(1056964608) * var16.h();
          var1.roundedRect(
              var10 - var17 * Float.intBitsToFloat(1056964608),
              var11 - var17 * Float.intBitsToFloat(1056964608),
              var17,
              var17,
              2.0F,
              this.count40);
          var1.roundedRect(
              var10 - var5 * Float.intBitsToFloat(1056964608),
              var11 - var5 * Float.intBitsToFloat(1056964608),
              var5,
              var5,
              Float.intBitsToFloat(1069547520),
              -1);
        }
      }

      float[] var18 = this.screenSampleNode3.marqueeRect();
      if (var18 != null && var18[2] > 1.0F && var18[3] > 1.0F) {
        var1.roundedRect(
            var18[0], var18[1], var18[2], var18[3], 1.0F, this.count40 & 16777215 | 402653184);
        var1.roundedRect(var18[0], var18[1], var18[2], 1.0F, 0.0F, this.count40);
        var1.roundedRect(var18[0], var18[1] + var18[3] - 1.0F, var18[2], 1.0F, 0.0F, this.count40);
        var1.roundedRect(var18[0], var18[1], 1.0F, var18[3], 0.0F, this.count40);
        var1.roundedRect(var18[0] + var18[2] - 1.0F, var18[1], 1.0F, var18[3], 0.0F, this.count40);
      }
    }

    private void updateState38(
        CompositorPushPresentationScaleService var1, HudEditorScreen.SpacingBadge var2) {
      float var3 = Float.intBitsToFloat(1077936128);
      String var4 = String.valueOf(var2.dist());
      if (var2.horizontal()) {
        float var5 = var2.cross();
        float var6 = Math.min(var2.a0(), var2.a1());
        float var7 = Math.max(var2.a0(), var2.a1());
        var1.roundedRect(
            var6, var5 - Float.intBitsToFloat(1056964608), var7 - var6, 1.0F, 0.0F, -14494738);
        var1.roundedRect(var6, var5 - var3, 1.0F, var3 * 2.0F, 0.0F, -14494738);
        var1.roundedRect(var7 - 1.0F, var5 - var3, 1.0F, var3 * 2.0F, 0.0F, -14494738);
        var1.text(
            (var6 + var7) * Float.intBitsToFloat(1056964608)
                - var4.length() * Float.intBitsToFloat(1072902963),
            var5 - Float.intBitsToFloat(1092616192),
            var4,
            Float.intBitsToFloat(1087373312),
            -14494738);
      } else {
        float var8 = var2.cross();
        float var9 = Math.min(var2.a0(), var2.a1());
        float var10 = Math.max(var2.a0(), var2.a1());
        var1.roundedRect(
            var8 - Float.intBitsToFloat(1056964608), var9, 1.0F, var10 - var9, 0.0F, -14494738);
        var1.roundedRect(var8 - var3, var9, var3 * 2.0F, 1.0F, 0.0F, -14494738);
        var1.roundedRect(var8 - var3, var10 - 1.0F, var3 * 2.0F, 1.0F, 0.0F, -14494738);
        var1.text(
            var8 + Float.intBitsToFloat(1082130432),
            (var9 + var10) * Float.intBitsToFloat(1056964608) - Float.intBitsToFloat(1077936128),
            var4,
            Float.intBitsToFloat(1087373312),
            -14494738);
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
    private static final float value14 = 1.0F;
    private static final float value15 = 8.0F;
    private static final float value16 = 22.0F;
    private static final float value17 = 6.0F;
    private static final float value18 = 7.0F;
    static final float HANDLE_MIN_SIZE = 21.0F;
    private static final float value19 = 4.0F;
    private static final float fedxhdvjhf1 = 3.0F;
    private static final int count41 = 0;
    private static final int count42 = 1;
    private static final int count43 = 2;
    private static final int count44 = 3;
    private static final int f4rhivxcwtqw = 4;
    private final LayoutFindSelector layoutFindSelector3;
    private final LayoutRenderer renderer3;
    private final LayoutOperationHandler layoutOperationHandler3;
    private float value21 = 1.0F;
    private float f1xomqxzdozf = 1.0F;
    private float value23;
    private float value24;
    private float value25;
    private float value26;
    private float value27;
    private float fhmljivzxxri;
    private final LinkedHashSet<LayoutIsContainerService> values = new LinkedHashSet<>();
    private LayoutIsContainerService layoutIsContainerService7;
    private int count46 = 0;
    private final List<LayoutIsContainerService> items11 = new ArrayList<>();
    private float[] float2;
    private float[] float3;
    private float value29;
    private float value30;
    private boolean enabled7;
    private float value31;
    private float value32;
    private float value33;
    private float value34;
    private final LinkedHashSet<LayoutIsContainerService> values2 = new LinkedHashSet<>();
    private final List<HudEditorScreen.Guide> fhmbzyzryl6c = new ArrayList<>();
    private final List<HudEditorScreen.SpacingBadge> items13 = new ArrayList<>();
    private int count47;
    private int count48;
    private float fdo7pt0uxf3;
    private float value36;
    private float value37;
    private float value38;
    private float fb76wqc9913;
    private float value40;
    private float value41;
    private float value42;
    private float value43;
    private float fhzvwpeozl9c;
    private boolean enabled8;
    private boolean enabled9;

    ScreenSampleNode(LayoutFindSelector var1, LayoutRenderer var2, LayoutOperationHandler var3) {
      this.layoutFindSelector3 = var1;
      this.renderer3 = var2;
      this.layoutOperationHandler3 = var3;
    }

    float zoom() {
      return this.value21;
    }

    LayoutIsContainerService selected() {
      return this.layoutIsContainerService7;
    }

    List<LayoutIsContainerService> selection() {
      return new ArrayList<>(this.values);
    }

    int selectionSize() {
      return this.values.size();
    }

    boolean isSelected(LayoutIsContainerService var1) {
      return this.values.contains(var1);
    }

    List<HudEditorScreen.Guide> guides() {
      return this.fhmbzyzryl6c;
    }

    List<HudEditorScreen.SpacingBadge> spacingBadges() {
      return this.items13;
    }

    void resetView() {
      this.f1xomqxzdozf = 1.0F;
      this.value25 = 0.0F;
      this.value26 = 0.0F;
    }

    void deselect() {
      this.values.clear();
      this.layoutIsContainerService7 = null;
      this.items11.clear();
      this.count46 = 0;
    }

    void selectOnly(LayoutIsContainerService var1) {
      this.values.clear();
      if (var1 != null) {
        this.values.add(var1);
      }

      this.layoutIsContainerService7 = var1;
    }

    void addToSelection(LayoutIsContainerService var1) {
      if (var1 != null) {
        this.values.add(var1);
        this.layoutIsContainerService7 = var1;
      }
    }

    void toggleSelection(LayoutIsContainerService var1) {
      this.updateState39(var1);
    }

    private void updateState39(LayoutIsContainerService var1) {
      if (var1 != null) {
        if (this.values.remove(var1)) {
          this.layoutIsContainerService7 = this.createLayoutIsContainerService3(this.values);
        } else {
          this.values.add(var1);
          this.layoutIsContainerService7 = var1;
        }
      }
    }

    void pruneSelection() {
      this.values.removeIf(
          var1 ->
              this.layoutFindSelector3 == null
                  || !this.layoutFindSelector3.elements.contains(var1));
      if (this.layoutIsContainerService7 != null
          && !this.values.contains(this.layoutIsContainerService7)) {
        this.layoutIsContainerService7 = this.createLayoutIsContainerService3(this.values);
      }
    }

    private LayoutIsContainerService createLayoutIsContainerService3(
        LinkedHashSet<LayoutIsContainerService> var1) {
      LayoutIsContainerService var2 = null;

      for (LayoutIsContainerService var4 : var1) {
        var2 = var4;
      }

      return var2;
    }

    boolean marqueeActive() {
      return this.count46 == 3 && this.isPressed();
    }

    float[] marqueeRect() {
      return !this.marqueeActive()
          ? null
          : new float[] {
            Math.min(this.value31, this.value33),
            Math.min(this.value32, this.value34),
            Math.abs(this.value33 - this.value31),
            Math.abs(this.value34 - this.value32)
          };
    }

    List<LayoutRenderer.Rect> selectionScreenRects() {
      ArrayList var1 = new ArrayList();
      if (this.renderer3 != null && this.layoutFindSelector3 != null) {
        float var2 = this.calculateValue9();
        if (var2 <= 0.0F) {
          return var1;
        }

        float var3 = this.calculateValue6();
        float var4 = this.m8wbshutfnzw();

        for (LayoutIsContainerService var6 : this.values) {
          if (var6 != null && var6.visible && this.layoutFindSelector3.elements.contains(var6)) {
            var1.add(
                this.renderer3.screenRect(
                    var6, this.fitX, this.fitY, var3, var4, var2, this.layoutOperationHandler3));
          }
        }

        return var1;
      } else {
        return var1;
      }
    }

    LayoutRenderer.Rect selectedScreenRect() {
      if (this.layoutIsContainerService7 != null && this.renderer3 != null) {
        if (this.layoutIsContainerService7.visible
            && (this.layoutFindSelector3 == null
                || this.layoutFindSelector3.elements.contains(this.layoutIsContainerService7))) {
          float var1 = this.calculateValue9();
          return var1 <= 0.0F
              ? null
              : this.renderer3.screenRect(
                  this.layoutIsContainerService7,
                  this.fitX,
                  this.fitY,
                  this.calculateValue6(),
                  this.m8wbshutfnzw(),
                  var1,
                  this.layoutOperationHandler3);
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    @Override
    protected void onPress(float var1, float var2) {
      boolean var3 = checkCondition5();
      boolean var4 = checkCondition6();
      int[] var5 = this.createInt(var1, var2);
      if (var5 != null) {
        this.updateState47(var5[0], var5[1], var1, var2);
      } else {
        LayoutIsContainerService var6 = this.createLayoutIsContainerService4(var1, var2);
        if (var6 != null) {
          if (var3) {
            this.updateState39(var6);
            this.count46 = 4;
            return;
          }

          if (!this.isSelected(var6)) {
            this.selectOnly(var6);
          } else {
            this.layoutIsContainerService7 = var6;
          }

          this.updateState40(var1, var2);
        } else if (var4) {
          this.count46 = 0;
          this.value27 = var1;
          this.fhmljivzxxri = var2;
        } else if (var3) {
          this.updateState41(var1, var2, true);
        } else if (this.value21 > Float.intBitsToFloat(1065437102)) {
          this.updateState42();
          this.count46 = 0;
          this.value27 = var1;
          this.fhmljivzxxri = var2;
        } else {
          this.updateState41(var1, var2, false);
        }
      }
    }

    private void updateState40(float var1, float var2) {
      this.items11.clear();

      for (LayoutIsContainerService var4 : this.values) {
        if (!var4.locked) {
          this.items11.add(var4);
        }
      }

      this.value29 = var1;
      this.value30 = var2;
      this.enabled7 = false;
      if (this.items11.isEmpty()) {
        this.count46 = 0;
        this.value27 = var1;
        this.fhmljivzxxri = var2;
      } else {
        this.float2 = new float[this.items11.size()];
        this.float3 = new float[this.items11.size()];

        for (int var5 = 0; var5 < this.items11.size(); var5++) {
          this.float2[var5] = this.items11.get(var5).offsetX;
          this.float3[var5] = this.items11.get(var5).offsetY;
        }

        this.count46 = 1;
      }
    }

    private void updateState41(float var1, float var2, boolean var3) {
      this.count46 = 3;
      this.value31 = this.value33 = var1;
      this.value32 = this.value34 = var2;
      this.values2.clear();
      this.fhmbzyzryl6c.clear();
      this.items13.clear();
      if (var3) {
        this.values2.addAll(this.values);
      } else {
        this.updateState42();
      }
    }

    private void updateState42() {
      this.values.clear();
      this.layoutIsContainerService7 = null;
    }

    @Override
    protected void updateWhilePressed(float var1, float var2) {
      if (this.count46 == 2 && this.layoutIsContainerService7 != null) {
        if (!this.enabled7) {
          if (Math.abs(var1 - this.value29) < 2.0F && Math.abs(var2 - this.value30) < 2.0F) {
            return;
          }

          this.enabled7 = true;
        }

        this.updateState48(var1, var2);
      } else if (this.count46 == 1 && !this.items11.isEmpty()) {
        if (!this.enabled7) {
          if (Math.abs(var1 - this.value29) < 2.0F && Math.abs(var2 - this.value30) < 2.0F) {
            return;
          }

          this.enabled7 = true;
        }

        float var8 = this.calculateValue9();
        if (var8 <= 0.0F) {
          return;
        }

        float var9 = (var1 - this.value29) / var8;
        float var5 = (var2 - this.value30) / var8;

        for (int var6 = 0; var6 < this.items11.size(); var6++) {
          LayoutIsContainerService var7 = this.items11.get(var6);
          var7.offsetX = this.float2[var6] + var9;
          var7.offsetY = this.float3[var6] + var5;
        }

        if (this.items11.size() == 1) {
          this.mauibusk9uyw(this.items11.get(0));
        } else {
          this.fhmbzyzryl6c.clear();
          this.items13.clear();
        }
      } else if (this.count46 == 3) {
        this.updateState43(var1, var2);
      } else if (this.count46 == 0) {
        float var3 = var1 - this.value27;
        float var4 = var2 - this.fhmljivzxxri;
        this.value25 += var3;
        this.value26 += var4;
        this.value23 += var3;
        this.value24 += var4;
        this.value27 = var1;
        this.fhmljivzxxri = var2;
      }
    }

    private void updateState43(float var1, float var2) {
      this.value33 = var1;
      this.value34 = var2;
      float var3 = Math.min(this.value31, this.value33);
      float var4 = Math.min(this.value32, this.value34);
      float var5 = Math.max(this.value31, this.value33);
      float var6 = Math.max(this.value32, this.value34);
      this.values.clear();
      this.values.addAll(this.values2);
      float var7 = this.calculateValue9();
      if (var7 > 0.0F && this.layoutFindSelector3 != null && this.renderer3 != null) {
        float var8 = this.calculateValue6();
        float var9 = this.m8wbshutfnzw();

        for (LayoutIsContainerService var11 : this.layoutFindSelector3.elements) {
          if (var11.visible) {
            LayoutRenderer.Rect var12 =
                this.renderer3.screenRect(
                    var11, this.fitX, this.fitY, var8, var9, var7, this.layoutOperationHandler3);
            if (var12.x() < var5
                && var12.x() + var12.w() > var3
                && var12.y() < var6
                && var12.y() + var12.h() > var4) {
              this.values.add(var11);
            }
          }
        }
      }

      this.layoutIsContainerService7 = this.createLayoutIsContainerService3(this.values);
    }

    private static boolean checkCondition4(int var0) {
      long var1 = CompatAdapterService.windowHandle(Minecraft.getInstance());
      return var1 != 0L && GLFW.glfwGetKey(var1, var0) == 1;
    }

    private static boolean checkCondition5() {
      return checkCondition4(340) || checkCondition4(344);
    }

    private static boolean checkCondition6() {
      return checkCondition4(32);
    }

    @Override
    protected boolean handleScroll(float var1) {
      float var2 =
          calculateValue10(
              this.f1xomqxzdozf
                  * (float) Math.pow(Double.longBitsToDouble(4607993066732944097L), var1),
              1.0F,
              Float.intBitsToFloat(1090519040));
      if (var2 != this.f1xomqxzdozf && this.cw > 0.0F && this.ch > 0.0F) {
        CompositorPushPresentationScaleService var3 = CoreIsInitializedHandler.get().compositor();
        float var4 = var3.pointerX();
        float var5 = var3.pointerY();
        float var6 = this.calculateValue8();
        float var7 = this.cw;
        float var8 = this.cw / var6;
        if (var8 > this.ch) {
          var8 = this.ch;
          var7 = this.ch * var6;
        }

        float var9 = this.cx + this.cw * Float.intBitsToFloat(1056964608);
        float var10 = this.cy + this.ch * Float.intBitsToFloat(1056964608);
        float var11 = var7 * this.value21;
        float var12 = var8 * this.value21;
        float var13 = var9 - var11 * Float.intBitsToFloat(1056964608) + this.value23;
        float var14 = var10 - var12 * Float.intBitsToFloat(1056964608) + this.value24;
        float var15 = calculateValue10((var4 - var13) / var11, 0.0F, 1.0F);
        float var16 = calculateValue10((var5 - var14) / var12, 0.0F, 1.0F);
        float var17 = var7 * var2;
        float var18 = var8 * var2;
        this.value25 = var4 - var9 + var17 * (Float.intBitsToFloat(1056964608) - var15);
        this.value26 = var5 - var10 + var18 * (Float.intBitsToFloat(1056964608) - var16);
      }

      this.f1xomqxzdozf = var2;
      return true;
    }

    private LayoutIsContainerService createLayoutIsContainerService4(float var1, float var2) {
      if (this.layoutFindSelector3 != null && this.renderer3 != null) {
        float var3 = this.calculateValue9();
        if (var3 <= 0.0F) {
          return null;
        }

        float var4 = this.calculateValue6();
        float var5 = this.m8wbshutfnzw();

        for (int var6 = this.layoutFindSelector3.elements.size() - 1; var6 >= 0; var6 += -1) {
          LayoutIsContainerService var7 = this.layoutFindSelector3.elements.get(var6);
          if (var7.visible) {
            LayoutRenderer.Rect var8 =
                this.renderer3.screenRect(
                    var7, this.fitX, this.fitY, var4, var5, var3, this.layoutOperationHandler3);
            if (var1 >= var8.x() - 2.0F
                && var1 <= var8.x() + var8.w() + 2.0F
                && var2 >= var8.y() - 2.0F
                && var2 <= var8.y() + var8.h() + 2.0F) {
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
      this.items13.clear();
      float var2 = this.calculateValue9();
      float var3 = this.calculateValue6();
      float var4 = this.m8wbshutfnzw();
      if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
        float var5 = Float.intBitsToFloat(1086324736) / var2;
        LayoutRenderer.Rect var6 =
            this.renderer3.logicalRect(var1, var3, var4, this.layoutOperationHandler3);
        ArrayList var7 =
            new ArrayList<>(List.of(0.0F, var3 * Float.intBitsToFloat(1056964608), var3));
        ArrayList var8 =
            new ArrayList<>(List.of(0.0F, var4 * Float.intBitsToFloat(1056964608), var4));

        for (LayoutIsContainerService var10 : this.layoutFindSelector3.elements) {
          if (var10 != var1 && var10.visible) {
            LayoutRenderer.Rect var11 =
                this.renderer3.logicalRect(var10, var3, var4, this.layoutOperationHandler3);
            var7.add(var11.x());
            var7.add(var11.x() + var11.w() * Float.intBitsToFloat(1056964608));
            var7.add(var11.x() + var11.w());
            var8.add(var11.y());
            var8.add(var11.y() + var11.h() * Float.intBitsToFloat(1056964608));
            var8.add(var11.y() + var11.h());
          }
        }

        float[] var27 =
            new float[] {
              var6.x(), var6.x() + var6.w() * Float.intBitsToFloat(1056964608), var6.x() + var6.w()
            };
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
          this.fhmbzyzryl6c.add(
              new HudEditorScreen.Guide(
                  true, this.fitX + var12 * var2, this.fitY, this.fitY + this.fitH));
        }

        float[] var30 =
            new float[] {
              var6.y(), var6.y() + var6.h() * Float.intBitsToFloat(1056964608), var6.y() + var6.h()
            };
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
          this.fhmbzyzryl6c.add(
              new HudEditorScreen.Guide(
                  false, this.fitY + var33 * var2, this.fitX, this.fitX + this.fitW));
        }

        var6 = this.renderer3.logicalRect(var1, var3, var4, this.layoutOperationHandler3);
        if (var13 == 0) {
          this.updateState45(var1, var6, var3, var4, var2, var5);
        }

        if (var34 == 0) {
          this.updateState46(
              var1,
              this.renderer3.logicalRect(var1, var3, var4, this.layoutOperationHandler3),
              var3,
              var4,
              var2,
              var5);
        }
      }
    }

    private void updateState45(
        LayoutIsContainerService var1,
        LayoutRenderer.Rect var2,
        float var3,
        float var4,
        float var5,
        float var6) {
      LayoutRenderer.Rect var7 = null;
      LayoutRenderer.Rect var8 = null;

      for (LayoutIsContainerService var10 : this.layoutFindSelector3.elements) {
        if (var10 != var1 && var10.visible) {
          LayoutRenderer.Rect var11 =
              this.renderer3.logicalRect(var10, var3, var4, this.layoutOperationHandler3);
          if (var2.y() < var11.y() + var11.h() && var2.y() + var2.h() > var11.y()) {
            if (var11.x() + var11.w() <= var2.x() + Float.intBitsToFloat(1008981770)) {
              if (var7 == null || var11.x() + var11.w() > var7.x() + var7.w()) {
                var7 = var11;
              }
            } else if (var11.x() >= var2.x() + var2.w() - Float.intBitsToFloat(1008981770)
                && (var8 == null || var11.x() < var8.x())) {
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
          this.items13.add(
              new HudEditorScreen.SpacingBadge(
                  true,
                  this.fitX + (var7.x() + var7.w()) * var5,
                  this.fitX + var17 * var5,
                  var13,
                  var14));
          this.items13.add(
              new HudEditorScreen.SpacingBadge(
                  true, this.fitX + var12 * var5, this.fitX + var8.x() * var5, var13, var14));
        }
      }
    }

    private void updateState46(
        LayoutIsContainerService var1,
        LayoutRenderer.Rect var2,
        float var3,
        float var4,
        float var5,
        float var6) {
      LayoutRenderer.Rect var7 = null;
      LayoutRenderer.Rect var8 = null;

      for (LayoutIsContainerService var10 : this.layoutFindSelector3.elements) {
        if (var10 != var1 && var10.visible) {
          LayoutRenderer.Rect var11 =
              this.renderer3.logicalRect(var10, var3, var4, this.layoutOperationHandler3);
          if (var2.x() < var11.x() + var11.w() && var2.x() + var2.w() > var11.x()) {
            if (var11.y() + var11.h() <= var2.y() + Float.intBitsToFloat(1008981770)) {
              if (var7 == null || var11.y() + var11.h() > var7.y() + var7.h()) {
                var7 = var11;
              }
            } else if (var11.y() >= var2.y() + var2.h() - Float.intBitsToFloat(1008981770)
                && (var8 == null || var11.y() < var8.y())) {
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
          this.items13.add(
              new HudEditorScreen.SpacingBadge(
                  false,
                  this.fitY + (var7.y() + var7.h()) * var5,
                  this.fitY + var17 * var5,
                  var13,
                  var14));
          this.items13.add(
              new HudEditorScreen.SpacingBadge(
                  false, this.fitY + var12 * var5, this.fitY + var8.y() * var5, var13, var14));
        }
      }
    }

    private int[] createInt(float var1, float var2) {
      if (this.selectionSize() != 1) {
        return null;
      }

      if (this.layoutIsContainerService7 != null && this.layoutIsContainerService7.locked) {
        return null;
      }

      LayoutRenderer.Rect var3 = this.selectedScreenRect();
      if (var3 == null) {
        return null;
      }

      if (!(var3.w() <= Float.intBitsToFloat(1101529088))
          && !(var3.h() <= Float.intBitsToFloat(1101529088))) {
        if (var1 > var3.x() + Float.intBitsToFloat(1088421888)
            && var1 < var3.x() + var3.w() - Float.intBitsToFloat(1088421888)
            && var2 > var3.y() + Float.intBitsToFloat(1088421888)
            && var2 < var3.y() + var3.h() - Float.intBitsToFloat(1088421888)) {
          return null;
        }

        for (int[] var7 : HudEditorScreen.int2) {
          float var8 = var3.x() + (var7[0] + 1) * Float.intBitsToFloat(1056964608) * var3.w();
          float var9 = var3.y() + (var7[1] + 1) * Float.intBitsToFloat(1056964608) * var3.h();
          if (Math.abs(var1 - var8) <= Float.intBitsToFloat(1088421888)
              && Math.abs(var2 - var9) <= Float.intBitsToFloat(1088421888)) {
            return var7;
          }
        }

        return null;
      } else {
        return null;
      }
    }

    private void updateState47(int var1, int var2, float var3, float var4) {
      LayoutIsContainerService var5 = this.layoutIsContainerService7;
      if (var5 != null) {
        this.count46 = 2;
        this.count47 = var1;
        this.count48 = var2;
        this.value29 = var3;
        this.value30 = var4;
        this.enabled7 = false;
        LayoutRenderer.Rect var6 =
            this.renderer3.logicalRect(
                var5, this.calculateValue6(), this.m8wbshutfnzw(), this.layoutOperationHandler3);
        this.fdo7pt0uxf3 = var6.x();
        this.value36 = var6.y();
        this.value37 = var6.w();
        this.value38 = var6.h();
        this.enabled8 = m8kf964zbo8(var5.type);
        this.enabled9 = !(!"armorhud".equals(var5.type) && !"keystrokes".equals(var5.type));
        this.fhzvwpeozl9c =
            Math.max(
                Float.intBitsToFloat(1086324736),
                LayoutStringService.number(var5.props, "size", Float.intBitsToFloat(1098907648)));
        this.fb76wqc9913 =
            Math.max(
                Float.intBitsToFloat(1077936128),
                LayoutStringService.number(
                    var5.props, "fontSize", Float.intBitsToFloat(1091567616)));
        this.value40 = LayoutStringService.number(var5.props, "shadowX", 0.0F);
        this.value41 = LayoutStringService.number(var5.props, "shadowY", 0.0F);
        this.value42 = LayoutStringService.number(var5.props, "outlineWidth", 0.0F);
        this.value43 =
            LayoutStringService.number(
                var5.props,
                "cornerRadius",
                "rect".equals(var5.type) ? Float.intBitsToFloat(1082130432) : 0.0F);
      }
    }

    private void updateState48(float var1, float var2) {
      LayoutIsContainerService var3 = this.layoutIsContainerService7;
      if (var3 != null) {
        float var4 = this.calculateValue9();
        float var5 = this.calculateValue6();
        float var6 = this.m8wbshutfnzw();
        if (!(var4 <= 0.0F) && !(var5 <= 0.0F) && !(var6 <= 0.0F)) {
          float var7 = (var1 - this.fitX) / var4;
          float var8 = (var2 - this.fitY) / var4;
          float var9 = this.value37;
          float var10 = this.value38;
          if (this.count47 == 1) {
            var9 = var7 - this.fdo7pt0uxf3;
          } else if (this.count47 == -1) {
            var9 = this.fdo7pt0uxf3 + this.value37 - var7;
          }

          if (this.count48 == 1) {
            var10 = var8 - this.value36;
          } else if (this.count48 == -1) {
            var10 = this.value36 + this.value38 - var8;
          }

          var9 = Math.max(Float.intBitsToFloat(1082130432), var9);
          var10 = Math.max(Float.intBitsToFloat(1082130432), var10);
          float var11 = this.value37 > Float.intBitsToFloat(981668463) ? var9 / this.value37 : 1.0F;
          float var12 =
              this.value38 > Float.intBitsToFloat(981668463) ? var10 / this.value38 : 1.0F;
          updateState49(var3);
          if (this.enabled8) {
            var3.width = Float.intBitsToFloat(-1082130432);
            var3.height = Float.intBitsToFloat(-1082130432);
            float var13 =
                this.count47 != 0 && this.count48 != 0
                    ? (Math.abs(var11 - 1.0F) >= Math.abs(var12 - 1.0F) ? var11 : var12)
                    : (this.count47 != 0 ? var11 : var12);
            float var14 = Math.max(Float.intBitsToFloat(1077936128), this.fb76wqc9913 * var13);
            float var15 = var14 / this.fb76wqc9913;
            var3.props.addProperty("fontSize", var14);
            if (this.value40 != 0.0F) {
              var3.props.addProperty("shadowX", this.value40 * var15);
            }

            if (this.value41 != 0.0F) {
              var3.props.addProperty("shadowY", this.value41 * var15);
            }

            if (this.value42 > 0.0F) {
              var3.props.addProperty("outlineWidth", this.value42 * var15);
            }
          } else if (this.enabled9) {
            var3.width = Float.intBitsToFloat(-1082130432);
            var3.height = Float.intBitsToFloat(-1082130432);
            float var20 =
                this.count47 != 0 && this.count48 != 0
                    ? (Math.abs(var11 - 1.0F) >= Math.abs(var12 - 1.0F) ? var11 : var12)
                    : (this.count47 != 0 ? var11 : var12);
            var3.props.addProperty(
                "size", Math.max(Float.intBitsToFloat(1090519040), this.fhzvwpeozl9c * var20));
          } else {
            var3.width = var9;
            var3.height = var10;
            if ("rect".equals(var3.type) && this.value43 > 0.0F) {
              var3.props.addProperty(
                  "cornerRadius",
                  this.value43
                      * (float)
                          Math.sqrt(Math.max(Float.intBitsToFloat(953267991), var11 * var12)));
            }
          }

          LayoutRenderer.Rect var21 =
              this.renderer3.logicalRect(var3, var5, var6, this.layoutOperationHandler3);
          float var22 = var21.w();
          float var23 = var21.h();
          float var16 =
              this.count47 == 1
                  ? this.fdo7pt0uxf3
                  : (this.count47 == -1
                      ? this.fdo7pt0uxf3 + this.value37 - var22
                      : this.fdo7pt0uxf3
                          + this.value37 * Float.intBitsToFloat(1056964608)
                          - var22 * Float.intBitsToFloat(1056964608));
          float var17 =
              this.count48 == 1
                  ? this.value36
                  : (this.count48 == -1
                      ? this.value36 + this.value38 - var23
                      : this.value36
                          + this.value38 * Float.intBitsToFloat(1056964608)
                          - var23 * Float.intBitsToFloat(1056964608));
          var3.offsetX = var16 + var3.anchor.h.factor * (var22 - var5);
          var3.offsetY = var17 + var3.anchor.v.factor * (var23 - var6);
        }
      }
    }

    private static boolean m8kf964zbo8(String var0) {
      return "text".equals(var0) || "fps".equals(var0) || "coords".equals(var0);
    }

    private static void updateState49(LayoutIsContainerService var0) {
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

          if (!this.items13.isEmpty()) {
            this.items13.clear();
          }
        }

        float var2 =
            1.0F - (float) Math.exp(-SceneDtService.dt() * Float.intBitsToFloat(1102053376));
        this.value21 = this.value21 + (this.f1xomqxzdozf - this.value21) * var2;
        this.value23 = this.value23 + (this.value25 - this.value23) * var2;
        this.value24 = this.value24 + (this.value26 - this.value24) * var2;
        float var3 = this.calculateValue8();
        float var4 = this.cw;
        float var5 = this.cw / var3;
        if (var5 > this.ch) {
          var5 = this.ch;
          var4 = this.ch * var3;
        }

        float var6 = var4 * this.value21;
        float var7 = var5 * this.value21;
        float var8 = Math.max(0.0F, (var6 - this.cw) * Float.intBitsToFloat(1056964608));
        float var9 = Math.max(0.0F, (var7 - this.ch) * Float.intBitsToFloat(1056964608));
        float var10 =
            Math.max(0.0F, (var4 * this.f1xomqxzdozf - this.cw) * Float.intBitsToFloat(1056964608));
        float var11 =
            Math.max(0.0F, (var5 * this.f1xomqxzdozf - this.ch) * Float.intBitsToFloat(1056964608));
        this.value25 = calculateValue10(this.value25, -var10, var10);
        this.value26 = calculateValue10(this.value26, -var11, var11);
        this.value23 = calculateValue10(this.value23, -var8, var8);
        this.value24 = calculateValue10(this.value24, -var9, var9);
        float var12 = this.cx + this.cw * Float.intBitsToFloat(1056964608);
        float var13 = this.cy + this.ch * Float.intBitsToFloat(1056964608);
        this.fitW = var6;
        this.fitH = var7;
        this.fitX = var12 - var6 * Float.intBitsToFloat(1056964608) + this.value23;
        this.fitY = var13 - var7 * Float.intBitsToFloat(1056964608) + this.value24;
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
              var15);
        }
      }
    }

    private float calculateValue6() {
      Window var1 = Minecraft.getInstance().getWindow();
      return var1 != null ? var1.getGuiScaledWidth() : 0.0F;
    }

    private float m8wbshutfnzw() {
      Window var1 = Minecraft.getInstance().getWindow();
      return var1 != null ? var1.getGuiScaledHeight() : 0.0F;
    }

    private float calculateValue8() {
      float var1 = this.calculateValue6();
      float var2 = this.m8wbshutfnzw();
      return var2 > 0.0F ? var1 / var2 : Float.intBitsToFloat(1071877689);
    }

    private float calculateValue9() {
      float var1 = this.calculateValue6();
      return var1 > 0.0F ? this.fitW / var1 : 0.0F;
    }

    private static float calculateValue10(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
    }
  }

  record SpacingBadge(boolean horizontal, float a0, float a1, float cross, int dist) {}
}
