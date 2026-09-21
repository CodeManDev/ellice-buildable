package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.config.ConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialData;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialTabsService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneRenderer;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.ColorFeatureType;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.HueTrianglePicker;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.control.ControlCompactService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeBlurQualityData;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ClientSettingsScreen implements ScreenOperationHandler {
  private ColorFeatureType colorFeatureType = ColorFeatureType.GRADIENT;
  private static final int count = 6;
  private PaletteFilter paletteFilter = PaletteFilter.ALL;
  private int count2;
  private final ConfigRepository farotg0tgsyf;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private ConfigRepository.Appearance appearance2;
  private ThemeBlurQualityData themeBlurQualityData;
  private ThemeCornerData fdrtsyprgmd3;
  private BuiltinOriginalService builtinOriginalService;
  private boolean enabled;
  private String frilltdzt6a;
  private String text3 = "";
  private ComponentMountService componentMountService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ScreenScreenIdService screenScreenIdService;
  private float fcmfoxswwf0j = Float.intBitsToFloat(1150025728);
  private float f993lbxroi2g = Float.intBitsToFloat(0x44340000);
  private Page f5u6yamoganm = Page.APPEARANCE;

  public ClientSettingsScreen(ConfigRepository configRepository) {
    this.farotg0tgsyf = Objects.requireNonNull(configRepository);
    this.appearance2 = configRepository.appearance();
    this.themeBlurQualityData = configRepository.effects();
    this.fdrtsyprgmd3 = configRepository.preferences();
    this.m1wybiblgxns();
  }

  @Override
  public String id() {
    return "client-settings";
  }

  @Override
  public boolean opensAsWindow() {
    return true;
  }

  @Override
  public boolean overlaysPreviousScreen() {
    return true;
  }

  @Override
  public float backgroundDesaturation() {
    return 0.0f;
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.WORKSPACE;
  }

  public void viewport(float f, float f2) {
    if (this.fcmfoxswwf0j == f && this.f993lbxroi2g == f2) {
      return;
    }
    this.fcmfoxswwf0j = f;
    this.f993lbxroi2g = f2;
    this.updateState13();
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
    this.screenScreenIdService = screenScreenIdService;
    this.appearance2 = this.farotg0tgsyf.appearance();
    this.themeBlurQualityData = this.farotg0tgsyf.effects();
    this.fdrtsyprgmd3 = this.farotg0tgsyf.preferences();
    this.m1wybiblgxns();
    this.updateState();
    this.materialTerrainTooltipsService =
        new MaterialTerrainTooltipsService().terrainTooltips(true);
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            this.materialTerrainTooltipsService.id("client-settings.canvas"))
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .direction(ScenePctService.Direction.NONE))
        .backgroundColor(0)
        .interactive(true);
    this.componentMountService =
        new ComponentMountService()
            .mount(this::createComponentKeyService, this.farotg0tgsyf.theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode) this.componentMountService.absolute())
                .inset(LayoutOperationHandler.px(0.0f)))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.materialTerrainTooltipsService.addChild(this.componentMountService);
    return this.materialTerrainTooltipsService;
  }

  private boolean mtwo0sq7wfg() {
    return this.fcmfoxswwf0j < Float.intBitsToFloat(1142292480);
  }

  private boolean m11p8kx068m() {
    return this.f993lbxroi2g < Float.intBitsToFloat(1137180672);
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    Consumer<LayoutContainerNode> consumer =
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.size(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)),
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .padding(
                                    this.mtwo0sq7wfg() || this.m11p8kx068m()
                                        ? Float.intBitsToFloat(0x41000000)
                                        : Float.intBitsToFloat(1103101952)))
                        .align(ScenePctService.Align.CENTER))
                .justify(ScenePctService.Justify.CENTER);
    ComponentKeyService[] componentKeyServiceArray = new ComponentKeyService[1];
    Consumer<SceneCornerRadiusService> consumer2 =
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id(
                                                        "client-settings.sheet"))
                                                .size(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456)),
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .maxWidth(Float.intBitsToFloat(0x44700000)))
                                .maxHeight(Float.intBitsToFloat(1143930880)))
                        .backgroundColor(MaterialIsLightService.SURFACE)
                        .cornerRadius(
                            this.mtwo0sq7wfg() || this.m11p8kx068m()
                                ? Float.intBitsToFloat(1101004800)
                                : Float.intBitsToFloat(1105199104))
                        .shadow(Float.intBitsToFloat(1102053376))
                        .shadowColor(0x70000000)
                        .direction(ScenePctService.Direction.COLUMN))
                .clip(true);
    ComponentKeyService[] componentKeyServiceArray2 = new ComponentKeyService[5];
    componentKeyServiceArray2[0] =
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.height(
                                                    LayoutOperationHandler.px(
                                                        this.m11p8kx068m()
                                                            ? Float.intBitsToFloat(1112539136)
                                                            : Float.intBitsToFloat(1118306304))))
                                            .padding(
                                                Float.intBitsToFloat(0x40800000),
                                                this.mtwo0sq7wfg()
                                                    ? Float.intBitsToFloat(0x41400000)
                                                    : Float.intBitsToFloat(1103101952)))
                                    .gap(Float.intBitsToFloat(0x41400000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.icon("palette", MaterialIsLightService.PRIMARY)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1104150528), Float.intBitsToFloat(1104150528))),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .gap(2.0f),
                MaterialTextService.text(
                    "Client settings",
                    this.m11p8kx068m()
                        ? Float.intBitsToFloat(1101004800)
                        : Float.intBitsToFloat(1104150528),
                    MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                        "Make ellice feel like yours",
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(sceneTextService -> sceneTextService.visible(!this.m11p8kx068m()))),
            MaterialTextService.iconButton(
                    "client-settings.close", "close", "Close \u00b7 Esc", this::updateState14)
                .props(
                    materialJoinedService ->
                        materialJoinedService.size(
                            Float.intBitsToFloat(0x42200000), Float.intBitsToFloat(0x42200000))));
    componentKeyServiceArray2[1] = this.createComponentKeyService2();
    Consumer<LayoutContainerNode> consumer3 =
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            ((LayoutContainerNode)
                                                                    ((LayoutContainerNode)
                                                                            ((LayoutContainerNode)
                                                                                    layoutContainerNode
                                                                                        .id(
                                                                                            "client-settings.scroll"))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .flex(1.0f))
                                                                .minHeight(0.0f))
                                                        .padding(
                                                            0.0f,
                                                            this.mtwo0sq7wfg()
                                                                ? Float.intBitsToFloat(0x41400000)
                                                                : Float.intBitsToFloat(1103101952),
                                                            Float.intBitsToFloat(1101004800),
                                                            this.mtwo0sq7wfg()
                                                                ? Float.intBitsToFloat(0x41400000)
                                                                : Float.intBitsToFloat(1103101952)))
                                                .gap(Float.intBitsToFloat(1101004800)))
                                        .scrollable(true))
                                .clip(true))
                        .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT);
    ComponentKeyService[] componentKeyServiceArray3 = new ComponentKeyService[1];
    componentKeyServiceArray3[0] =
        switch (this.f5u6yamoganm.ordinal()) {
          default -> throw new MatchException(null, null);
          case 0 -> this.createComponentKeyService4();
          case 1 -> this.createComponentKeyService14();
          case 2 -> this.createComponentKeyService18();
        };
    componentKeyServiceArray2[2] = ComponentBoxService.column(consumer3, componentKeyServiceArray3);
    componentKeyServiceArray2[3] = MaterialTextService.divider();
    componentKeyServiceArray2[4] = this.createComponentKeyService13();
    componentKeyServiceArray[0] = ComponentBoxService.panel(consumer2, componentKeyServiceArray2);
    return ComponentBoxService.column(consumer, componentKeyServiceArray)
        .key("client-settings-window");
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.id("client-settings.pages"))
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .padding(
                                    0.0f,
                                    this.mtwo0sq7wfg()
                                        ? Float.intBitsToFloat(0x41400000)
                                        : Float.intBitsToFloat(1103101952),
                                    this.m11p8kx068m()
                                        ? Float.intBitsToFloat(0x40C00000)
                                        : Float.intBitsToFloat(0x41400000),
                                    this.mtwo0sq7wfg()
                                        ? Float.intBitsToFloat(0x41400000)
                                        : Float.intBitsToFloat(1103101952)))
                        .gap(
                            this.mtwo0sq7wfg()
                                ? Float.intBitsToFloat(0x40800000)
                                : Float.intBitsToFloat(0x41000000)))
                .flexShrink(0.0f),
        this.m67fhlrahl3p(Page.APPEARANCE, "Appearance"),
        this.m67fhlrahl3p(Page.EFFECTS, this.mtwo0sq7wfg() ? "Effects" : "Effects & performance"),
        this.m67fhlrahl3p(Page.GENERAL, "General"));
  }

  private ComponentKeyService<?> m67fhlrahl3p(Page page, String string) {
    return MaterialTextService.button(
            "client-settings.page." + page.name().toLowerCase(Locale.ROOT),
            string,
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.f5u6yamoganm = page;
              this.updateState13();
              ScenePctService<?> scenePctService =
                  this.materialTerrainTooltipsService.findById("client-settings.scroll");
              if (scenePctService != null) {
                scenePctService.scrollTarget(0.0f);
                scenePctService.scrollY(0.0f);
              }
            },
            this.f5u6yamoganm == page ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
            this.f5u6yamoganm == page
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1098907648))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        materialJoinedService.height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1108344832))))
                                    .padding(
                                        0.0f,
                                        this.mtwo0sq7wfg()
                                            ? Float.intBitsToFloat(1092616192)
                                            : Float.intBitsToFloat(1098907648)))
                            .minWidth(0.0f))
                    .flex(this.mtwo0sq7wfg() ? 1.0f : 0.0f))
        .children(
            MaterialTextService.text(
                string,
                this.mtwo0sq7wfg()
                    ? Float.intBitsToFloat(0x41400000)
                    : Float.intBitsToFloat(1096810496),
                this.f5u6yamoganm == page
                    ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                    : MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    boolean bl = this.fcmfoxswwf0j >= Float.intBitsToFloat(0x44430000);
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .gap(Float.intBitsToFloat(1099956224)))
                .flexShrink(0.0f),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .gap(Float.intBitsToFloat(0x41000000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.text(
                    "Color mode",
                    Float.intBitsToFloat(1099956224),
                    MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
            this.mazgfneis1wy(false),
            this.mazgfneis1wy(true)),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                            .direction(
                                                bl
                                                    ? ScenePctService.Direction.ROW
                                                    : ScenePctService.Direction.COLUMN))
                                    .align(ScenePctService.Align.STRETCH))
                            .gap(Float.intBitsToFloat(1101004800)))
                    .flexShrink(0.0f),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        bl
                                                            ? LayoutOperationHandler.auto()
                                                            : LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .flex(bl ? 1.0f : 0.0f))
                                        .minWidth(0.0f))
                                .gap(Float.intBitsToFloat(0x41400000)))
                        .flexShrink(0.0f),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .align(ScenePctService.Align.CENTER),
                    MaterialTextService.text(
                            "Color presets",
                            Float.intBitsToFloat(1096810496),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService -> sceneTextService.flex(1.0f)),
                    MaterialTextService.text(
                            this.farotg0tgsyf.presetName(),
                            Float.intBitsToFloat(0x41400000),
                            MaterialIsLightService.PRIMARY)
                        .props(sceneTextService -> sceneTextService.id("client-settings.current"))),
                this.createComponentKeyService6(),
                MaterialTextService.text(
                        "Start with a palette, then fine-tune its colors.",
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneTextService ->
                            ((SceneTextService)
                                    sceneTextService.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .wordWrap(true)
                                .flexShrink(0.0f))),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        bl
                                            ? LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1135214592))
                                            : LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .gap(Float.intBitsToFloat(1096810496)))
                        .flexShrink(0.0f),
                this.createComponentKeyService10(),
                this.createComponentKeyService11())));
  }

  private ComponentKeyService<?> mazgfneis1wy(boolean bl) {
    int n = this.farotg0tgsyf.appearance().light() == bl ? 1 : 0;
    return MaterialTextService.button(
            "client-settings.mode." + (bl ? "light" : "dark"),
            bl ? "Light" : "Dark",
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              ConfigRepository.Appearance appearance = this.farotg0tgsyf.appearance();
              this.mt0jmcjb08g(
                  new ConfigRepository.Appearance(
                      appearance.accent(), appearance.surfaceTint(), bl));
            },
            n != 0
                ? MaterialIsLightService.SECONDARY_CONTAINER
                : MaterialIsLightService.SURFACE_CONTAINER,
            n != 0
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1098907648))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                materialJoinedService.height(
                                    LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))))
                            .padding(
                                0.0f,
                                this.mtwo0sq7wfg()
                                    ? Float.intBitsToFloat(0x41400000)
                                    : Float.intBitsToFloat(1098907648)))
                    .tooltip(
                        bl
                            ? "Light surfaces\nKeep your color choices in a bright workspace."
                            : "Dark surfaces\nKeep your color choices in a dim workspace."));
  }

  private ComponentKeyService<?> createComponentKeyService6() {
    List<ConfigRepository.Preset> list = this.collectValues();
    int n = this.count2 * 6;
    int n2 = Math.min(list.size(), n + 6);
    ArrayList<ComponentKeyService<LayoutContainerNode>> arrayList =
        new ArrayList<ComponentKeyService<LayoutContainerNode>>();
    for (int i = n; i < n2; i += 2) {
      ConfigRepository.Preset preset = list.get(i);
      arrayList.add(
          ComponentBoxService.row(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              ((LayoutContainerNode)
                                      layoutContainerNode.width(
                                          LayoutOperationHandler.percent(
                                              Float.intBitsToFloat(1120403456))))
                                  .gap(Float.intBitsToFloat(1092616192)))
                          .flexShrink(0.0f),
                  this.mghyhl1rxggm(preset),
                  i + 1 < n2
                      ? this.mghyhl1rxggm(list.get(i + 1))
                      : ComponentBoxService.box(
                              layoutContainerNode ->
                                  ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                      .minWidth(0.0f),
                              new ComponentKeyService[0])
                          .key("empty"))
              .key("preset-row-" + preset.id()));
    }
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .gap(Float.intBitsToFloat(1092616192)))
                .flexShrink(0.0f),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .flexShrink(0.0f),
            (ComponentKeyService[])
                Arrays.stream(PaletteFilter.values())
                    .map(this::createComponentKeyService7)
                    .toArray(ComponentKeyService[]::new)),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.id(
                                                    "client-settings.palette-grid"))
                                            .width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                    .minHeight(Float.intBitsToFloat(1133182976)))
                            .gap(Float.intBitsToFloat(1092616192)))
                    .flexShrink(0.0f),
            (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .gap(Float.intBitsToFloat(0x40C00000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.text(
                    n + 1 + "\u2013" + n2 + " of " + list.size() + " themes",
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService)
                                        sceneTextService.id("client-settings.palette-range"))
                                    .flex(1.0f))
                            .minWidth(0.0f)),
            MaterialTextService.iconButton(
                    "client-settings.palettes.previous",
                    "arrow_back",
                    "Previous themes",
                    () -> this.updateState2(-1))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(this.count2 > 0)
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832))),
            MaterialTextService.iconButton(
                    "client-settings.palettes.next",
                    "chevron_right",
                    "More themes",
                    () -> this.updateState2(1))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(n2 < list.size())
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832)))));
  }

  private List<ConfigRepository.Preset> collectValues() {
    return ConfigRepository.PRESETS.stream().filter(this.paletteFilter::includes).toList();
  }

  private void updateState() {
    List<ConfigRepository.Preset> list = this.collectValues();
    this.count2 = 0;
    for (int i = 0; i < list.size(); ++i) {
      if (!list.get(i).appearance().equals(this.farotg0tgsyf.appearance())) continue;
      this.count2 = i / 6;
      break;
    }
  }

  private void updateState2(int n) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.count2 = Math.clamp((long) (this.count2 + n), 0, (this.collectValues().size() - 1) / 6);
    this.updateState13();
  }

  private ComponentKeyService<?> createComponentKeyService7(PaletteFilter paletteFilter) {
    int n = this.paletteFilter == paletteFilter ? 1 : 0;
    return MaterialTextService.button(
            "client-settings.palettes.filter." + paletteFilter.name().toLowerCase(Locale.ROOT),
            paletteFilter.label,
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.paletteFilter = paletteFilter;
              this.updateState();
              this.updateState13();
            },
            n != 0
                ? MaterialIsLightService.SECONDARY_CONTAINER
                : MaterialIsLightService.SURFACE_CONTAINER,
            n != 0
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(0x41400000))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                materialJoinedService.height(
                                                    LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(0x42000000))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .padding(0.0f, Float.intBitsToFloat(1092616192)))
                    .tooltip(
                        "Browse "
                            + paletteFilter.label.toLowerCase(Locale.ROOT)
                            + " themes\n"
                            + "Filtering keeps your current colors until you choose a theme."));
  }

  private ComponentKeyService<?> mghyhl1rxggm(ConfigRepository.Preset preset) {
    boolean bl = preset.appearance().equals(this.farotg0tgsyf.appearance());
    MaterialData materialData = preset.appearance().scheme();
    return MaterialTextService.button(
            "client-settings.preset." + preset.id(),
            preset.name(),
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.mt0jmcjb08g(preset.appearance());
            },
            bl
                ? MaterialIsLightService.SECONDARY_CONTAINER
                : MaterialIsLightService.SURFACE_CONTAINER,
            bl ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE,
            Float.intBitsToFloat(1098907648))
        .props(
            materialJoinedService -> {
              materialJoinedService.outlined(bl).shape(Float.intBitsToFloat(1098907648), 0.0f);
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      ((SceneCornerRadiusService)
                                                              ((SceneCornerRadiusService)
                                                                      ((SceneCornerRadiusService)
                                                                              materialJoinedService
                                                                                  .width(
                                                                                      LayoutOperationHandler
                                                                                          .auto()))
                                                                          .flex(1.0f))
                                                                  .minWidth(0.0f))
                                                          .height(
                                                              LayoutOperationHandler.px(
                                                                  Float.intBitsToFloat(
                                                                      1118568448))))
                                                  .padding(
                                                      Float.intBitsToFloat(1092616192),
                                                      Float.intBitsToFloat(0x41400000)))
                                          .direction(ScenePctService.Direction.COLUMN))
                                  .align(ScenePctService.Align.STRETCH))
                          .gap(Float.intBitsToFloat(0x41000000)))
                  .tooltip(preset.name() + "\n" + preset.description());
            })
        .children(
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            ((SceneCornerRadiusService)
                                                                    sceneCornerRadiusService.width(
                                                                        LayoutOperationHandler
                                                                            .percent(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1120403456))))
                                                                .height(
                                                                    LayoutOperationHandler.px(
                                                                        Float.intBitsToFloat(
                                                                            1105199104))))
                                                        .backgroundColor(
                                                            materialData.color("SURFACE"))
                                                        .cornerRadius(
                                                            Float.intBitsToFloat(0x41000000))
                                                        .direction(ScenePctService.Direction.ROW))
                                                .align(ScenePctService.Align.CENTER))
                                        .padding(Float.intBitsToFloat(0x40A00000)))
                                .gap(Float.intBitsToFloat(0x40A00000)))
                        .pointerEvents(false),
                ClientSettingsScreen.meylb30c11ed(
                    materialData.color("PRIMARY"), Float.intBitsToFloat(1099956224)),
                ClientSettingsScreen.meylb30c11ed(
                    materialData.color("SECONDARY_CONTAINER"), Float.intBitsToFloat(1099956224)),
                ComponentBoxService.box(
                    layoutContainerNode -> layoutContainerNode.flex(1.0f),
                    new ComponentKeyService[0]),
                ClientSettingsScreen.meylb30c11ed(
                    materialData.color("ON_SURFACE"), Float.intBitsToFloat(0x41000000))),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .gap(Float.intBitsToFloat(0x40800000)))
                                .align(ScenePctService.Align.CENTER))
                        .pointerEvents(false),
                MaterialTextService.text(
                        preset.name(),
                        Float.intBitsToFloat(1096810496),
                        bl
                            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                            : MaterialIsLightService.ON_SURFACE)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                MaterialTextService.icon("check", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                    .props(
                        sceneSrcService ->
                            ((SceneSrcService)
                                    sceneSrcService.size(
                                        Float.intBitsToFloat(1098907648),
                                        Float.intBitsToFloat(1098907648)))
                                .visible(bl))));
  }

  private static ComponentKeyService<?> meylb30c11ed(int n, float f) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService) sceneCornerRadiusService.size(f, f))
                        .cornerRadius(f / 2.0f)
                        .backgroundColor(n)
                        .flexShrink(0.0f))
                .pointerEvents(false),
        new ComponentKeyService[0]);
  }

  private ComponentKeyService<?> createComponentKeyService10() {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            sceneCornerRadiusService.id(
                                                                "client-settings.preview"))
                                                        .width(
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .direction(ScenePctService.Direction.COLUMN))
                                        .padding(Float.intBitsToFloat(1096810496)))
                                .gap(Float.intBitsToFloat(1092616192)))
                        .cornerRadius(Float.intBitsToFloat(1101004800))
                        .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                        .flexShrink(0.0f))
                .pointerEvents(false),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.text(
                    "LIVE PREVIEW",
                    Float.intBitsToFloat(1092616192),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(sceneTextService -> sceneTextService.flex(1.0f)),
            ClientSettingsScreen.meylb30c11ed(
                MaterialIsLightService.PRIMARY, Float.intBitsToFloat(0x41000000))),
        ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                sceneCornerRadiusService.width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                            .direction(ScenePctService.Direction.COLUMN))
                                    .padding(Float.intBitsToFloat(0x41400000)))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .backgroundColor(MaterialIsLightService.SECONDARY_CONTAINER)
                    .cornerRadius(Float.intBitsToFloat(0x41400000)),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .align(ScenePctService.Align.CENTER),
                MaterialTextService.text(
                        "Selected module",
                        Float.intBitsToFloat(1096810496),
                        MaterialIsLightService.ON_SECONDARY_CONTAINER)
                    .props(sceneTextService -> sceneTextService.flex(1.0f)),
                MaterialTextService.icon("check", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1098907648),
                                Float.intBitsToFloat(1098907648)))),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    sceneCornerRadiusService.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .height(
                                    LayoutOperationHandler.px(Float.intBitsToFloat(0x40800000))))
                        .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                        .cornerRadius(2.0f),
                ComponentBoxService.panel(
                    sceneCornerRadiusService ->
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        sceneCornerRadiusService.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1115815936))))
                                    .height(
                                        LayoutOperationHandler.px(
                                            Float.intBitsToFloat(0x40800000))))
                            .backgroundColor(MaterialIsLightService.PRIMARY)
                            .cornerRadius(2.0f),
                    new ComponentKeyService[0]))),
        MaterialTextService.text(
                "Surfaces, text and controls adapt together.",
                Float.intBitsToFloat(1093664768),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)));
  }

  private ComponentKeyService<?> createComponentKeyService11() {
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .gap(Float.intBitsToFloat(1092616192)))
                .flexShrink(0.0f),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x41000000)),
            this.mji0yw2s38v(false),
            this.mji0yw2s38v(true)),
        MaterialTabsService.tabs(
            "client-settings.color-mode",
            this.colorFeatureType,
            colorFeatureType -> {
              this.colorFeatureType = colorFeatureType;
              this.updateState13();
            }),
        this.colorFeatureType == ColorFeatureType.TRIANGLE
            ? ComponentBoxService.node(
                    "appearance-color-triangle",
                    HueTrianglePicker::new,
                    hueTrianglePicker ->
                        ((HueTrianglePicker)
                                ((HueTrianglePicker)
                                        ((HueTrianglePicker)
                                                hueTrianglePicker
                                                    .hue(this.builtinOriginalService.hue())
                                                    .sat(this.builtinOriginalService.saturation())
                                                    .bri(this.builtinOriginalService.brightness())
                                                    .onHueChange(
                                                        f -> {
                                                          this.builtinOriginalService.hue(
                                                              f.floatValue());
                                                          this.updateState9();
                                                        })
                                                    .onSBChange(
                                                        (f, f2) -> {
                                                          this.builtinOriginalService.sv(
                                                              f.floatValue(), f2.floatValue());
                                                          this.updateState9();
                                                        })
                                                    .onCommit(this::updateState12)
                                                    .id("client-settings.color-triangle"))
                                            .size(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456)),
                                                LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(0x43300000))))
                                    .flexShrink(0.0f))
                            .stopPropagation(true),
                    new ComponentKeyService[0])
                .key("color-triangle")
            : ComponentBoxService.node(
                    "appearance-color-field",
                    SaturationBrightnessPicker::new,
                    saturationBrightnessPicker ->
                        ((SaturationBrightnessPicker)
                                ((SaturationBrightnessPicker)
                                        ((SaturationBrightnessPicker)
                                                saturationBrightnessPicker
                                                    .material(true)
                                                    .hue(this.builtinOriginalService.hue())
                                                    .sat(this.builtinOriginalService.saturation())
                                                    .bri(this.builtinOriginalService.brightness())
                                                    .cornerRadius(Float.intBitsToFloat(1096810496))
                                                    .onChange(
                                                        (f, f2) -> {
                                                          this.builtinOriginalService.sv(
                                                              f.floatValue(), f2.floatValue());
                                                          this.updateState9();
                                                        })
                                                    .onCommit((f, f2) -> this.updateState12())
                                                    .id("client-settings.color-field"))
                                            .size(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456)),
                                                LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1121976320))))
                                    .flexShrink(0.0f))
                            .stopPropagation(true),
                    new ComponentKeyService[0])
                .key("color-field"),
        ComponentBoxService.node(
                "appearance-hue",
                ColorGetAnimPropertyService::new,
                colorGetAnimPropertyService ->
                    ((ColorGetAnimPropertyService)
                            ((ColorGetAnimPropertyService)
                                    ((ColorGetAnimPropertyService)
                                            ((ColorGetAnimPropertyService)
                                                    colorGetAnimPropertyService
                                                        .material(true)
                                                        .hue(this.builtinOriginalService.hue())
                                                        .cornerRadius(
                                                            Float.intBitsToFloat(1096810496))
                                                        .onChange(
                                                            f -> {
                                                              this.builtinOriginalService.hue(
                                                                  f.floatValue());
                                                              this.updateState9();
                                                            })
                                                        .onCommit(f -> this.updateState12())
                                                        .id("client-settings.hue"))
                                                .size(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456)),
                                                    LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(1106247680))))
                                        .flexShrink(0.0f))
                                .stopPropagation(true))
                        .visible(this.colorFeatureType == ColorFeatureType.GRADIENT),
                new ComponentKeyService[0])
            .key("hue"),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x41000000)))
                    .align(ScenePctService.Align.CENTER),
            ClientSettingsScreen.meylb30c11ed(
                this.builtinOriginalService.argb(), Float.intBitsToFloat(1103101952)),
            ComponentBoxService.node(
                    "appearance-hex",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService -> {
                      MaterialTextService.input(controlLetterSpacingService);
                      ((ControlLetterSpacingService)
                              ((ControlLetterSpacingService)
                                      ((ControlLetterSpacingService)
                                              ((ControlLetterSpacingService)
                                                      ((ControlLetterSpacingService)
                                                              ((ControlLetterSpacingService)
                                                                      controlLetterSpacingService
                                                                          .id(
                                                                              "client-settings.hex"))
                                                                  .size(
                                                                      LayoutOperationHandler.auto(),
                                                                      LayoutOperationHandler.px(
                                                                          Float.intBitsToFloat(
                                                                              1109917696))))
                                                          .flex(1.0f))
                                                  .minWidth(0.0f))
                                          .maxLength(7)
                                          .placeholder("#RRGGBB")
                                          .materialError(!this.text3.isEmpty())
                                          .stopPropagation(true))
                                  .tooltip(
                                      "Custom color\n"
                                          + "Enter six hexadecimal digits. ellice adjusts tones for"
                                          + " readable contrast."))
                          .onChanged(this::updateState10)
                          .onSubmit(string -> this.updateState11())
                          .onUnfocus(this::updateState11);
                      if (!controlLetterSpacingService.focused()) {
                        controlLetterSpacingService.text(this.frilltdzt6a);
                      }
                    },
                    new ComponentKeyService[0])
                .key("hex")),
        MaterialTextService.text(
                this.text3.isEmpty() ? "Contrast is balanced automatically." : this.text3,
                Float.intBitsToFloat(1093664768),
                this.text3.isEmpty()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService) sceneTextService.id("client-settings.color-help"))
                                .width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                        .wordWrap(true)
                        .flexShrink(0.0f)));
  }

  private ComponentKeyService<?> mji0yw2s38v(boolean bl) {
    int n = this.enabled == bl ? 1 : 0;
    return MaterialTextService.button(
            "client-settings.color." + (bl ? "surface" : "accent"),
            bl ? "Surface tint" : "Accent",
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.enabled = bl;
              this.m1wybiblgxns();
              this.updateState13();
            },
            n != 0
                ? MaterialIsLightService.SECONDARY_CONTAINER
                : MaterialIsLightService.SURFACE_CONTAINER,
            n != 0
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1096810496))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                materialJoinedService.height(
                                                    LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(0x42200000))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .padding(0.0f, Float.intBitsToFloat(1092616192)))
                    .tooltip(
                        bl
                            ? "Surface tint\n"
                                  + "Choose the hue for panels and cards. Brightness follows Light"
                                  + " or Dark mode."
                            : "Accent color\n"
                                  + "Choose the hue for active modules, sliders and highlights."));
  }

  private ComponentKeyService<?> createComponentKeyService13() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.id(
                                                        "client-settings.footer"))
                                                .width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .padding(
                                            this.m11p8kx068m()
                                                ? Float.intBitsToFloat(0x41000000)
                                                : Float.intBitsToFloat(0x41400000),
                                            this.mtwo0sq7wfg()
                                                ? Float.intBitsToFloat(0x41400000)
                                                : Float.intBitsToFloat(1103101952)))
                                .gap(
                                    this.mtwo0sq7wfg()
                                        ? Float.intBitsToFloat(0x40C00000)
                                        : Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        MaterialTextService.iconButton(
                "client-settings.reset",
                "refresh",
                "Reset this page\nRestore this page's default settings.",
                () -> {
                  this.materialKeyService.clear(this.materialTerrainTooltipsService);
                  switch (this.f5u6yamoganm.ordinal()) {
                    case 0:
                      {
                        this.mt0jmcjb08g(ConfigRepository.Appearance.defaults());
                        break;
                      }
                    case 1:
                      {
                        this.updateState4(ThemeBlurQualityData.defaults());
                        break;
                      }
                    case 2:
                      {
                        this.farotg0tgsyf.preferences(ThemeCornerData.defaults());
                        this.updateState12();
                      }
                  }
                })
            .props(
                materialJoinedService ->
                    materialJoinedService
                        .available(
                            switch (this.f5u6yamoganm.ordinal()) {
                              default -> throw new MatchException(null, null);
                              case 0 -> {
                                if (!this.farotg0tgsyf
                                    .appearance()
                                    .equals(ConfigRepository.Appearance.defaults())) {
                                  yield true;
                                }
                                yield false;
                              }
                              case 1 -> {
                                if (!this.farotg0tgsyf
                                    .effects()
                                    .equals(ThemeBlurQualityData.defaults())) {
                                  yield true;
                                }
                                yield false;
                              }
                              case 2 ->
                                  !this.farotg0tgsyf
                                      .preferences()
                                      .equals(ThemeCornerData.defaults());
                            })
                        .size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(0x42200000))),
        MaterialTextService.text(
                this.farotg0tgsyf.error().isEmpty()
                    ? "Saved automatically"
                    : (this.mtwo0sq7wfg() ? "Save failed" : this.farotg0tgsyf.error()),
                Float.intBitsToFloat(1093664768),
                this.farotg0tgsyf.error().isEmpty()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService)
                                    ((SceneTextService)
                                            sceneTextService.id("client-settings.save-status"))
                                        .flex(1.0f))
                                .minWidth(0.0f))
                        .wordWrap(true)
                        .visible(
                            (!this.mtwo0sq7wfg() || !this.farotg0tgsyf.error().isEmpty() ? 1 : 0)
                                != 0)),
        ComponentBoxService.box(
            layoutContainerNode ->
                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                    .visible(
                        (this.mtwo0sq7wfg() && this.farotg0tgsyf.error().isEmpty() ? 1 : 0) != 0),
            new ComponentKeyService[0]),
        MaterialTextService.button(
                "client-settings.revert",
                "Revert",
                () -> {
                  this.materialKeyService.clear(this.materialTerrainTooltipsService);
                  this.farotg0tgsyf.effects(this.themeBlurQualityData);
                  this.farotg0tgsyf.preferences(this.fdrtsyprgmd3);
                  this.mt0jmcjb08g(this.appearance2);
                },
                false)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    materialJoinedService
                                        .available(
                                            (!this.farotg0tgsyf
                                                            .appearance()
                                                            .equals(this.appearance2)
                                                        || !this.farotg0tgsyf
                                                            .effects()
                                                            .equals(this.themeBlurQualityData)
                                                        || !this.farotg0tgsyf
                                                            .preferences()
                                                            .equals(this.fdrtsyprgmd3)
                                                    ? 1
                                                    : 0)
                                                != 0)
                                        .height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(0x42200000))))
                                .padding(
                                    0.0f,
                                    this.mtwo0sq7wfg()
                                        ? Float.intBitsToFloat(0x41400000)
                                        : Float.intBitsToFloat(1098907648)))
                        .tooltip(
                            "Revert this session\n"
                                + "Return to the client settings you had when this window"
                                + " opened.")),
        MaterialTextService.button("client-settings.done", "Done", this::updateState14, true)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            materialJoinedService.height(
                                LayoutOperationHandler.px(Float.intBitsToFloat(0x42200000))))
                        .padding(
                            0.0f,
                            this.mtwo0sq7wfg()
                                ? Float.intBitsToFloat(0x41400000)
                                : Float.intBitsToFloat(1101004800))));
  }

  private ComponentKeyService<?> createComponentKeyService14() {
    boolean bl = this.fcmfoxswwf0j >= Float.intBitsToFloat(0x44430000);
    ComponentKeyService[] componentKeyServiceArray =
        (ComponentKeyService[])
            ConfigRepository.EFFECT_PRESETS.stream()
                .map(
                    effectsPreset -> {
                      boolean bl2 = effectsPreset.effects().equals(this.farotg0tgsyf.effects());
                      return MaterialTextService.button(
                              "client-settings.effects-preset." + effectsPreset.id(),
                              effectsPreset.name(),
                              () -> {
                                this.materialKeyService.clear(this.materialTerrainTooltipsService);
                                this.updateState4(effectsPreset.effects());
                              },
                              bl2
                                  ? MaterialIsLightService.SECONDARY_CONTAINER
                                  : MaterialIsLightService.SURFACE_CONTAINER,
                              bl2
                                  ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                  : MaterialIsLightService.ON_SURFACE,
                              Float.intBitsToFloat(1098907648))
                          .props(
                              materialJoinedService -> {
                                materialJoinedService
                                    .shape(Float.intBitsToFloat(1098907648), 0.0f)
                                    .outlined(bl2);
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        ((SceneCornerRadiusService)
                                                                                ((SceneCornerRadiusService)
                                                                                        ((SceneCornerRadiusService)
                                                                                                materialJoinedService
                                                                                                    .width(
                                                                                                        bl
                                                                                                            ? LayoutOperationHandler
                                                                                                                .auto()
                                                                                                            : LayoutOperationHandler
                                                                                                                .percent(
                                                                                                                    Float
                                                                                                                        .intBitsToFloat(
                                                                                                                            1120403456))))
                                                                                            .flex(
                                                                                                bl
                                                                                                    ? 1.0f
                                                                                                    : 0.0f))
                                                                                    .minWidth(0.0f))
                                                                            .height(
                                                                                LayoutOperationHandler
                                                                                    .px(
                                                                                        bl
                                                                                            ? Float
                                                                                                .intBitsToFloat(
                                                                                                    1120927744)
                                                                                            : Float
                                                                                                .intBitsToFloat(
                                                                                                    1117519872))))
                                                                    .direction(
                                                                        ScenePctService.Direction
                                                                            .COLUMN))
                                                            .align(ScenePctService.Align.STRETCH))
                                                    .padding(Float.intBitsToFloat(0x41400000)))
                                            .gap(Float.intBitsToFloat(0x40C00000)))
                                    .tooltip(
                                        effectsPreset.name() + "\n" + effectsPreset.description());
                              })
                          .children(
                              ComponentBoxService.row(
                                  layoutContainerNode ->
                                      ((LayoutContainerNode)
                                              layoutContainerNode.width(
                                                  LayoutOperationHandler.percent(
                                                      Float.intBitsToFloat(1120403456))))
                                          .align(ScenePctService.Align.CENTER),
                                  MaterialTextService.text(
                                          effectsPreset.name(),
                                          Float.intBitsToFloat(1098907648),
                                          bl2
                                              ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                              : MaterialIsLightService.ON_SURFACE)
                                      .props(sceneTextService -> sceneTextService.flex(1.0f)),
                                  MaterialTextService.icon(
                                          "check", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                      .props(
                                          sceneSrcService ->
                                              ((SceneSrcService)
                                                      sceneSrcService.size(
                                                          Float.intBitsToFloat(1099956224),
                                                          Float.intBitsToFloat(1099956224)))
                                                  .visible(bl2))),
                              MaterialTextService.text(
                                      effectsPreset.description(),
                                      Float.intBitsToFloat(0x41400000),
                                      bl2
                                          ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                          : MaterialIsLightService.ON_SURFACE_VARIANT)
                                  .props(
                                      sceneTextService ->
                                          sceneTextService.wordWrap(true).flexShrink(0.0f)));
                    })
                .toArray(ComponentKeyService[]::new);
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(1096810496)))
                    .flexShrink(0.0f),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .align(ScenePctService.Align.CENTER))
                        .gap(Float.intBitsToFloat(0x41000000)),
                MaterialTextService.text(
                        "UI rendering",
                        Float.intBitsToFloat(1099956224),
                        MaterialIsLightService.ON_SURFACE)
                    .props(sceneTextService -> sceneTextService.flex(1.0f)),
                MaterialTextService.text(
                        this.farotg0tgsyf.effectsPresetName(),
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.PRIMARY)
                    .props(
                        sceneTextService ->
                            sceneTextService.id("client-settings.effects-current"))),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .direction(
                                            bl
                                                ? ScenePctService.Direction.ROW
                                                : ScenePctService.Direction.COLUMN))
                                .gap(Float.intBitsToFloat(1092616192)))
                        .flexShrink(0.0f),
                componentKeyServiceArray),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                                .direction(
                                                    bl
                                                        ? ScenePctService.Direction.ROW
                                                        : ScenePctService.Direction.COLUMN))
                                        .gap(Float.intBitsToFloat(1096810496)))
                                .align(ScenePctService.Align.STRETCH))
                        .flexShrink(0.0f),
                ComponentBoxService.panel(
                    sceneCornerRadiusService ->
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        sceneCornerRadiusService
                                                                            .width(
                                                                                bl
                                                                                    ? LayoutOperationHandler
                                                                                        .auto()
                                                                                    : LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                    .flex(bl ? 1.0f : 0.0f))
                                                            .minWidth(0.0f))
                                                    .padding(Float.intBitsToFloat(1096810496)))
                                            .gap(Float.intBitsToFloat(0x41000000)))
                                    .cornerRadius(Float.intBitsToFloat(1101004800))
                                    .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                                    .direction(ScenePctService.Direction.COLUMN))
                            .flexShrink(0.0f),
                    this.m9rsn4owr6z6(
                        "blur",
                        "Blur strength",
                        this.farotg0tgsyf.effects().blurStrength(),
                        Float.intBitsToFloat(1069547520)),
                    this.m5rrtxuqaanm(),
                    this.m9rsn4owr6z6(
                        "shadow",
                        "Shadow strength",
                        this.farotg0tgsyf.effects().shadowStrength(),
                        1.0f)),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        layoutContainerNode.width(
                                                            bl
                                                                ? LayoutOperationHandler.auto()
                                                                : LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                    .flex(bl ? 1.0f : 0.0f))
                                            .minWidth(0.0f))
                                    .gap(Float.intBitsToFloat(1092616192)))
                            .flexShrink(0.0f),
                    ComponentBoxService.node(
                            "effects-preview",
                            LiveEffectsPreviewComponent::new,
                            liveEffectsPreviewComponent ->
                                ((LiveEffectsPreviewComponent)
                                        ((LiveEffectsPreviewComponent)
                                                ((LiveEffectsPreviewComponent)
                                                        ((LiveEffectsPreviewComponent)
                                                                liveEffectsPreviewComponent.id(
                                                                    "client-settings.effects-preview"))
                                                            .width(
                                                                LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            Float.intBitsToFloat(1121976320))))
                                            .flexShrink(0.0f))
                                    .pointerEvents(false),
                            new ComponentKeyService[0])
                        .key("effects-preview"),
                    this.createComponentKeyService17(
                        "glass",
                        "Glass refraction",
                        "Decorative reflections and refraction",
                        this.farotg0tgsyf.effects().glassEffects(),
                        () -> {
                          ThemeBlurQualityData themeBlurQualityData = this.farotg0tgsyf.effects();
                          this.updateState4(
                              new ThemeBlurQualityData(
                                  themeBlurQualityData.blurStrength(),
                                  themeBlurQualityData.blurQuality(),
                                  themeBlurQualityData.shadowStrength(),
                                  !themeBlurQualityData.glassEffects(),
                                  themeBlurQualityData.animatedBackground()));
                        }),
                    this.createComponentKeyService17(
                        "background",
                        "Animated menu",
                        "Use the animated menu shader",
                        this.farotg0tgsyf.effects().animatedBackground(),
                        () -> {
                          ThemeBlurQualityData themeBlurQualityData = this.farotg0tgsyf.effects();
                          this.updateState4(
                              new ThemeBlurQualityData(
                                  themeBlurQualityData.blurStrength(),
                                  themeBlurQualityData.blurQuality(),
                                  themeBlurQualityData.shadowStrength(),
                                  themeBlurQualityData.glassEffects(),
                                  !themeBlurQualityData.animatedBackground()));
                        }))),
            MaterialTextService.text(
                    "Applies to ellice menus, HUD surfaces and notifications. Blur Off skips"
                        + " backdrop passes; Low and Medium render blur at a lower resolution.",
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                sceneTextService.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .wordWrap(true)
                            .flexShrink(0.0f)))
        .key("effects-page");
  }

  private ComponentKeyService<?> m5rrtxuqaanm() {
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .gap(Float.intBitsToFloat(0x40C00000)))
                        .padding(Float.intBitsToFloat(0x40800000), 0.0f))
                .flexShrink(0.0f),
        MaterialTextService.text(
            "Blur quality",
            Float.intBitsToFloat(0x41400000),
            MaterialIsLightService.ON_SURFACE_VARIANT),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40C00000)),
            (ComponentKeyService[])
                Arrays.stream(ThemeBlurQualityData.BlurQuality.values())
                    .map(
                        blurQuality ->
                            MaterialTextService.button(
                                    "client-settings.blur-quality."
                                        + blurQuality.name().toLowerCase(Locale.ROOT),
                                    blurQuality.label(),
                                    () -> {
                                      ThemeBlurQualityData themeBlurQualityData =
                                          this.farotg0tgsyf.effects();
                                      this.updateState4(
                                          new ThemeBlurQualityData(
                                              themeBlurQualityData.blurStrength(),
                                              (ThemeBlurQualityData.BlurQuality)
                                                  ((Object) blurQuality),
                                              themeBlurQualityData.shadowStrength(),
                                              themeBlurQualityData.glassEffects(),
                                              themeBlurQualityData.animatedBackground()));
                                    },
                                    this.farotg0tgsyf.effects().blurQuality() == blurQuality
                                        ? MaterialIsLightService.SECONDARY_CONTAINER
                                        : MaterialIsLightService.SURFACE_HIGH,
                                    this.farotg0tgsyf.effects().blurQuality() == blurQuality
                                        ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                        : MaterialIsLightService.ON_SURFACE_VARIANT,
                                    Float.intBitsToFloat(0x41400000))
                                .props(
                                    materialJoinedService ->
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        materialJoinedService
                                                                            .height(
                                                                                LayoutOperationHandler
                                                                                    .px(
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1108344832))))
                                                                    .padding(
                                                                        0.0f,
                                                                        Float.intBitsToFloat(
                                                                            0x41000000)))
                                                            .flex(1.0f))
                                                    .minWidth(0.0f))
                                            .tooltip(
                                                blurQuality.label()
                                                    + " blur\n"
                                                    + (String)
                                                        (blurQuality.divisor() == 1
                                                            ? "Full-resolution backdrop."
                                                            : "Backdrop width and height reduced to"
                                                                  + " 1/"
                                                                + blurQuality.divisor()
                                                                + " for lower GPU cost."))))
                    .toArray(ComponentKeyService[]::new)));
  }

  private ComponentKeyService<?> m9rsn4owr6z6(String string, String string2, float f, float f2) {
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                        .gap(2.0f))
                .flexShrink(0.0f),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.text(
                    string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE)
                .props(sceneTextService -> sceneTextService.flex(1.0f)),
            MaterialTextService.text(
                    (String)
                        (f == 0.0f
                            ? "Off"
                            : Math.round(f * Float.intBitsToFloat(1120403456)) + "%"),
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.PRIMARY)
                .props(
                    sceneTextService ->
                        sceneTextService.id("client-settings." + string + "-value"))),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.iconButton(
                    "client-settings." + string + "-less",
                    "remove",
                    "Reduce " + string2.toLowerCase(Locale.ROOT),
                    () -> {
                      this.miz1p3fdtp9m(string, f - Float.intBitsToFloat(1028443341));
                      this.updateState12();
                    })
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(f > 0.0f)
                            .size(
                                Float.intBitsToFloat(0x42000000),
                                Float.intBitsToFloat(1108344832))),
            ComponentBoxService.node(
                    "effects-slider",
                    SliderControl::new,
                    sliderControl ->
                        ((SliderControl)
                                ((SliderControl)
                                        ((SliderControl)
                                                ((SliderControl)
                                                        sliderControl
                                                            .material(true)
                                                            .compact(true)
                                                            .range(0.0f, f2)
                                                            .step(Float.intBitsToFloat(1028443341))
                                                            .value(f)
                                                            .onChange(
                                                                sliderValue ->
                                                                    this.miz1p3fdtp9m(
                                                                        string,
                                                                        sliderValue.floatValue()))
                                                            .onCommit(
                                                                sliderValue -> this.updateState12())
                                                            .id(
                                                                "client-settings."
                                                                    + string
                                                                    + "-slider"))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            Float.intBitsToFloat(1108344832))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .stopPropagation(true),
                    new ComponentKeyService[0])
                .key(string + "-slider"),
            MaterialTextService.iconButton(
                    "client-settings." + string + "-more",
                    "add",
                    "Increase " + string2.toLowerCase(Locale.ROOT),
                    () -> {
                      this.miz1p3fdtp9m(string, f + Float.intBitsToFloat(1028443341));
                      this.updateState12();
                    })
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(f < f2)
                            .size(
                                Float.intBitsToFloat(0x42000000),
                                Float.intBitsToFloat(1108344832)))));
  }

  private void miz1p3fdtp9m(String string, float f) {
    ThemeBlurQualityData themeBlurQualityData = this.farotg0tgsyf.effects();
    this.farotg0tgsyf.effects(
        new ThemeBlurQualityData(
            string.equals("blur") ? f : themeBlurQualityData.blurStrength(),
            themeBlurQualityData.blurQuality(),
            string.equals("shadow") ? f : themeBlurQualityData.shadowStrength(),
            themeBlurQualityData.glassEffects(),
            themeBlurQualityData.animatedBackground()));
  }

  private ComponentKeyService<?> createComponentKeyService17(
      String string, String string2, String string3, boolean bl, Runnable runnable) {
    return MaterialTextService.button(
            "client-settings.effect." + string,
            string2,
            runnable,
            MaterialIsLightService.SURFACE_CONTAINER,
            MaterialIsLightService.ON_SURFACE,
            Float.intBitsToFloat(1098907648))
        .props(
            materialJoinedService -> {
              materialJoinedService.shape(Float.intBitsToFloat(1098907648), 0.0f);
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      materialJoinedService.width(
                                                          LayoutOperationHandler.percent(
                                                              Float.intBitsToFloat(1120403456))))
                                                  .height(
                                                      LayoutOperationHandler.px(
                                                          Float.intBitsToFloat(1115684864))))
                                          .padding(
                                              Float.intBitsToFloat(1092616192),
                                              Float.intBitsToFloat(1096810496)))
                                  .gap(Float.intBitsToFloat(1092616192)))
                          .justify(ScenePctService.Justify.START))
                  .tooltip(
                      string2
                          + "\n"
                          + string3
                          + (string.equals("glass")
                              ? ". Requires Blur strength above zero."
                              : "."));
            })
        .children(
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .gap(2.0f),
                MaterialTextService.text(
                    string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                    string3,
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.ON_SURFACE_VARIANT)),
            ComponentBoxService.node(
                    "effect-toggle",
                    ControlCompactService::new,
                    controlCompactService ->
                        ((ControlCompactService)
                                ((ControlCompactService)
                                        controlCompactService
                                            .material(true)
                                            .compact(true)
                                            .value(bl)
                                            .size(
                                                Float.intBitsToFloat(1108344832),
                                                Float.intBitsToFloat(0x42000000)))
                                    .flexShrink(0.0f))
                            .pointerEvents(false),
                    new ComponentKeyService[0])
                .key(string + "-toggle"));
  }

  private void updateState4(ThemeBlurQualityData themeBlurQualityData) {
    this.farotg0tgsyf.effects(themeBlurQualityData);
    this.updateState12();
  }

  private ComponentKeyService<?> createComponentKeyService18() {
    ThemeCornerData themeCornerData = this.farotg0tgsyf.preferences();
    boolean bl = this.fcmfoxswwf0j >= Float.intBitsToFloat(0x44430000);
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.width(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                            .direction(
                                                bl
                                                    ? ScenePctService.Direction.ROW
                                                    : ScenePctService.Direction.COLUMN))
                                    .gap(Float.intBitsToFloat(1101004800)))
                            .align(ScenePctService.Align.STRETCH))
                    .flexShrink(0.0f),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        bl
                                                            ? LayoutOperationHandler.auto()
                                                            : LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .flex(bl ? 1.0f : 0.0f))
                                        .minWidth(0.0f))
                                .gap(Float.intBitsToFloat(0x41400000)))
                        .flexShrink(0.0f),
                MaterialTextService.text(
                    "Notifications",
                    Float.intBitsToFloat(1099956224),
                    MaterialIsLightService.ON_SURFACE),
                this.createValue(
                    "notifications",
                    "Show notifications",
                    themeCornerData.notifications(),
                    ThemeCornerData.NotificationMode.values(),
                    ThemeCornerData.NotificationMode::label,
                    3),
                MaterialTextService.text(
                        "Important shows warnings and errors only.",
                        Float.intBitsToFloat(1093664768),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(sceneTextService -> sceneTextService.wordWrap(true).flexShrink(0.0f)),
                this.createValue(
                    "duration",
                    "Display duration",
                    themeCornerData.notificationDuration(),
                    ThemeCornerData.NotificationDuration.values(),
                    ThemeCornerData.NotificationDuration::label,
                    3),
                this.createValue(
                    "corner",
                    "Screen corner",
                    themeCornerData.notificationCorner(),
                    ThemeCornerData.Corner.values(),
                    ThemeCornerData.Corner::label,
                    2),
                this.mbxc5grbvnkh(
                    "limit",
                    "Maximum visible",
                    themeCornerData.notificationLimit(),
                    1.0f,
                    Float.intBitsToFloat(0x40A00000),
                    1.0f,
                    Integer.toString(themeCornerData.notificationLimit())),
                MaterialTextService.button(
                        "client-settings.test-notification",
                        "Test notification",
                        this::updateState6,
                        false)
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    materialJoinedService
                                        .available(
                                            themeCornerData.notifications()
                                                != ThemeCornerData.NotificationMode.OFF)
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                .tooltip(
                                    "Notification preview\n"
                                        + "Send a local sample using the selected position and"
                                        + " duration."))),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        bl
                                                            ? LayoutOperationHandler.auto()
                                                            : LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .flex(bl ? 1.0f : 0.0f))
                                        .minWidth(0.0f))
                                .gap(Float.intBitsToFloat(0x41400000)))
                        .flexShrink(0.0f),
                MaterialTextService.text(
                    "Interaction",
                    Float.intBitsToFloat(1099956224),
                    MaterialIsLightService.ON_SURFACE),
                this.createComponentKeyService17(
                    "tooltips",
                    "Show tooltips",
                    "Helpful descriptions on hover",
                    themeCornerData.tooltips(),
                    () -> this.mcofttruqzis("tooltips", !themeCornerData.tooltips())),
                this.mbxc5grbvnkh(
                    "tooltip-delay",
                    "Tooltip delay",
                    themeCornerData.tooltipDelay(),
                    0.0f,
                    Float.intBitsToFloat(1069547520),
                    Float.intBitsToFloat(0x3DCCCCCD),
                    Math.round(themeCornerData.tooltipDelay() * Float.intBitsToFloat(1148846080))
                        + " ms"),
                this.mbxc5grbvnkh(
                    "scroll-speed",
                    "Scroll speed",
                    themeCornerData.scrollSpeed(),
                    Float.intBitsToFloat(0x3F000000),
                    2.0f,
                    Float.intBitsToFloat(1048576000),
                    Math.round(themeCornerData.scrollSpeed() * Float.intBitsToFloat(1120403456))
                        + "%"),
                this.createComponentKeyService17(
                    "discord-presence",
                    "Discord Rich Presence",
                    "Show live ellice activity on your Discord profile",
                    themeCornerData.discordRichPresence(),
                    () ->
                        this.mcofttruqzis(
                            "discord-presence", !themeCornerData.discordRichPresence())),
                this.createComponentKeyService17(
                    "remember",
                    "Remember ClickGUI",
                    "Restore module, search and scroll position",
                    themeCornerData.rememberClickGui(),
                    () -> this.mcofttruqzis("remember", !themeCornerData.rememberClickGui())),
                this.createComponentKeyService17(
                    "focus-search",
                    "Focus search on open",
                    "Start typing to find a module",
                    themeCornerData.focusSearch(),
                    () -> this.mcofttruqzis("focus-search", !themeCornerData.focusSearch())),
                this.createComponentKeyService17(
                    "dim",
                    "Dim behind ClickGUI",
                    "Darken the scene behind the window",
                    themeCornerData.dimBackground(),
                    () -> this.mcofttruqzis("dim", !themeCornerData.dimBackground()))))
        .key("general-page");
  }

  private <E> ComponentKeyService<?> createValue(
      String string, String string2, E e, E[] EArray, Function<E, String> function, int n) {
    ArrayList<ComponentKeyService<LayoutContainerNode>> arrayList =
        new ArrayList<ComponentKeyService<LayoutContainerNode>>();
    for (int i = 0; i < EArray.length; i += n) {
      ArrayList<ComponentKeyService<MaterialJoinedService>> arrayList2 =
          new ArrayList<ComponentKeyService<MaterialJoinedService>>();
      for (int j = i; j < Math.min(EArray.length, i + n); ++j) {
        E e2 = EArray[j];
        boolean bl = e.equals(e2);
        arrayList2.add(
            MaterialTextService.button(
                    "client-settings.preference."
                        + string
                        + "."
                        + e2.toString().toLowerCase(Locale.ROOT),
                    function.apply(e2),
                    () -> this.mcofttruqzis(string, e2),
                    bl
                        ? MaterialIsLightService.SECONDARY_CONTAINER
                        : MaterialIsLightService.SURFACE_HIGH,
                    bl
                        ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                        : MaterialIsLightService.ON_SURFACE_VARIANT,
                    Float.intBitsToFloat(0x41400000))
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                materialJoinedService.height(
                                                    LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(1108344832))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .padding(0.0f, Float.intBitsToFloat(0x41000000))));
      }
      arrayList.add(
          ComponentBoxService.row(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              layoutContainerNode.width(
                                  LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                          .gap(Float.intBitsToFloat(0x40C00000)),
                  (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
              .key(string + "-row-" + i));
    }
    ArrayList<ComponentKeyService<?>> arrayList3 = new ArrayList<ComponentKeyService<?>>();
    arrayList3.add(
        MaterialTextService.text(
            string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE));
    arrayList3.addAll(arrayList);
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            sceneCornerRadiusService.width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(Float.intBitsToFloat(0x41400000)))
                        .gap(Float.intBitsToFloat(0x41000000)))
                .cornerRadius(Float.intBitsToFloat(1098907648))
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .flexShrink(0.0f),
        (ComponentKeyService[]) arrayList3.toArray(ComponentKeyService[]::new));
  }

  private ComponentKeyService<?> mbxc5grbvnkh(
      String string, String string2, float f, float f2, float f3, float f4, String string3) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            sceneCornerRadiusService.width(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(
                                    Float.intBitsToFloat(1092616192),
                                    Float.intBitsToFloat(0x41400000)))
                        .gap(Float.intBitsToFloat(0x40800000)))
                .cornerRadius(Float.intBitsToFloat(1098907648))
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .flexShrink(0.0f),
        MaterialTextService.text(
            string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.iconButton(
                    "client-settings.preference." + string + ".less",
                    "remove",
                    "Reduce " + string2.toLowerCase(Locale.ROOT),
                    () -> this.mcofttruqzis(string, Float.valueOf(Math.max(f2, f - f4))))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(f > f2 + Float.intBitsToFloat(981668463))
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832))),
            MaterialTextService.text(
                    string3, Float.intBitsToFloat(1096810496), MaterialIsLightService.PRIMARY)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService)
                                        sceneTextService.id(
                                            "client-settings.preference." + string + ".value"))
                                    .flex(1.0f))
                            .minWidth(0.0f)),
            MaterialTextService.iconButton(
                    "client-settings.preference." + string + ".more",
                    "add",
                    "Increase " + string2.toLowerCase(Locale.ROOT),
                    () -> this.mcofttruqzis(string, Float.valueOf(Math.min(f3, f + f4))))
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(f < f3 - Float.intBitsToFloat(981668463))
                            .size(
                                Float.intBitsToFloat(1108344832),
                                Float.intBitsToFloat(1108344832)))));
  }

  private void mcofttruqzis(String string, Object object) {
    ThemeCornerData themeCornerData = this.farotg0tgsyf.preferences();
    this.farotg0tgsyf.preferences(
        new ThemeCornerData(
            string.equals("notifications")
                ? (ThemeCornerData.NotificationMode) ((Object) object)
                : themeCornerData.notifications(),
            string.equals("duration")
                ? (ThemeCornerData.NotificationDuration) ((Object) object)
                : themeCornerData.notificationDuration(),
            string.equals("corner")
                ? (ThemeCornerData.Corner) ((Object) object)
                : themeCornerData.notificationCorner(),
            string.equals("limit")
                ? Math.round(((Number) object).floatValue())
                : themeCornerData.notificationLimit(),
            string.equals("tooltips")
                ? ((Boolean) object).booleanValue()
                : themeCornerData.tooltips(),
            string.equals("tooltip-delay")
                ? ((Number) object).floatValue()
                : themeCornerData.tooltipDelay(),
            string.equals("scroll-speed")
                ? ((Number) object).floatValue()
                : themeCornerData.scrollSpeed(),
            string.equals("remember")
                ? ((Boolean) object).booleanValue()
                : themeCornerData.rememberClickGui(),
            string.equals("focus-search")
                ? ((Boolean) object).booleanValue()
                : themeCornerData.focusSearch(),
            string.equals("dim")
                ? ((Boolean) object).booleanValue()
                : themeCornerData.dimBackground(),
            string.equals("discord-presence")
                ? ((Boolean) object).booleanValue()
                : themeCornerData.discordRichPresence()));
    this.updateState12();
  }

  private void updateState6() {
    if (CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get()
          .scene()
          .toasts()
          .show(
              "Notification preview",
              "Your position and duration settings are active.",
              Float.intBitsToFloat(0x40400000),
              this.farotg0tgsyf.preferences().notifications()
                      == ThemeCornerData.NotificationMode.IMPORTANT
                  ? SceneRenderer.Type.WARNING
                  : SceneRenderer.Type.INFO);
    }
  }

  private void mt0jmcjb08g(ConfigRepository.Appearance appearance) {
    this.farotg0tgsyf.appearance(appearance);
    this.m1wybiblgxns();
    this.updateState12();
  }

  private void m1wybiblgxns() {
    this.builtinOriginalService =
        new BuiltinOriginalService(
            this.enabled
                ? this.farotg0tgsyf.appearance().surfaceTint()
                : this.farotg0tgsyf.appearance().accent());
    this.frilltdzt6a =
        String.format(Locale.ROOT, "#%06X", this.builtinOriginalService.argb() & 0xFFFFFF);
    this.text3 = "";
  }

  private void updateState9() {
    this.frilltdzt6a =
        String.format(Locale.ROOT, "#%06X", this.builtinOriginalService.argb() & 0xFFFFFF);
    this.text3 = "";
    ConfigRepository.Appearance appearance = this.farotg0tgsyf.appearance();
    this.farotg0tgsyf.appearance(
        new ConfigRepository.Appearance(
            this.enabled ? appearance.accent() : this.builtinOriginalService.argb(),
            this.enabled ? this.builtinOriginalService.argb() : appearance.surfaceTint(),
            appearance.light()));
    this.updateState13();
  }

  private void updateState10(String string) {
    this.frilltdzt6a = string;
    this.text3 = "";
    if (string.matches("#?[a-fA-F0-9]{6}")) {
      this.builtinOriginalService.hex(string);
      this.updateState9();
    } else {
      this.updateState13();
    }
  }

  private void updateState11() {
    if (!this.frilltdzt6a.matches("#?[a-fA-F0-9]{6}")) {
      this.text3 = "Enter six hex digits, e.g. #72C9F6.";
      this.updateState13();
      return;
    }
    this.updateState12();
  }

  private void updateState12() {
    this.farotg0tgsyf.save();
    this.updateState13();
  }

  private void updateState13() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState14() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.updateState12();
    if (this.screenScreenIdService != null) {
      this.screenScreenIdService.close();
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.farotg0tgsyf.save();
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    if (this.materialTerrainTooltipsService != null
        && this.materialTerrainTooltipsService.computedW() > 0.0f) {
      this.viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
    return this.materialKeyService.key(this.materialTerrainTooltipsService, n, n2);
  }

  private static enum PaletteFilter {
    ALL("All"),
    DARK("Dark"),
    LIGHT("Light");

    final String label;

    private PaletteFilter(String string2) {
      this.label = string2;
    }

    boolean includes(ConfigRepository.Preset preset) {
      return this == ALL || preset.appearance().light() == (this == LIGHT);
    }
  }

  private static enum Page {
    APPEARANCE,
    EFFECTS,
    GENERAL;
  }
}
