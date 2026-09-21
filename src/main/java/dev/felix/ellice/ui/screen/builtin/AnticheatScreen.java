package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.anticheat.AnticheatCodec;
import dev.felix.ellice.anticheat.AnticheatRepository;
import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.net.URI;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;

public final class AnticheatScreen implements ScreenOperationHandler {
  private final AnticheatRepository anticheatRepository;
  private final Consumer<URI> consumer;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private AnticheatRepository.View view2;
  private ScreenScreenIdService fasnmfavrpfs;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private float f6ehsgfyeiz = Float.intBitsToFloat(1150025728);
  private float fgvjnn3sg1bz = Float.intBitsToFloat(0x44340000);
  private String text2 = "";
  private String text3 = "";
  private String text4 = "";
  private boolean fczhtab2khzf;
  private AnticheatCodec.Status status2;
  private AnticheatCodec.Sort sort2 = AnticheatCodec.Sort.NAME;
  private boolean enabled2;
  private Menu menu2 = Menu.NONE;

  public AnticheatScreen(AnticheatRepository anticheatRepository) {
    this(anticheatRepository, CompatOpenUriService::openUri);
  }

  public AnticheatScreen(AnticheatRepository anticheatRepository, Consumer<URI> consumer) {
    this.anticheatRepository = Objects.requireNonNull(anticheatRepository);
    this.consumer = Objects.requireNonNull(consumer);
    this.view2 = anticheatRepository.view();
  }

  @Override
  public String id() {
    return "anticheats";
  }

  @Override
  public boolean overlaysPreviousScreen() {
    return true;
  }

  @Override
  public boolean opensAsWindow() {
    return true;
  }

  @Override
  public boolean closesOnEscape() {
    return false;
  }

  @Override
  public float backgroundDesaturation() {
    return 0.0f;
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.NONE;
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
    this.fasnmfavrpfs = screenScreenIdService;
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService) this.materialTerrainTooltipsService.id("anticheats.root"))
                .size(
                    LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                    LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
        .backgroundColor(0x52000000);
    this.componentMountService =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService,
                screenScreenIdService == null
                    ? new ThemeIsSetService()
                    : screenScreenIdService.theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode) this.componentMountService.absolute())
                .inset(LayoutOperationHandler.px(0.0f)))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.materialTerrainTooltipsService.addChild(this.componentMountService);
    return this.materialTerrainTooltipsService;
  }

  public void viewport(float f, float f2) {
    if (this.f6ehsgfyeiz == f && this.fgvjnn3sg1bz == f2) {
      return;
    }
    this.f6ehsgfyeiz = f;
    this.fgvjnn3sg1bz = f2;
    this.updateState();
  }

  private boolean mfjzyzgupop1() {
    return this.f6ehsgfyeiz < Float.intBitsToFloat(1142292480);
  }

  private boolean checkCondition2() {
    return this.fgvjnn3sg1bz < Float.intBitsToFloat(1137180672);
  }

  private boolean checkCondition3() {
    return this.f6ehsgfyeiz >= Float.intBitsToFloat(1147207680);
  }

  private void updateState() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState2() {
    this.text4 = "";
    this.updateState();
    ScenePctService<?> scenePctService =
        this.materialTerrainTooltipsService.findById("anticheats.list");
    if (scenePctService != null) {
      ((ScenePctService) scenePctService.scrollY(0.0f)).scrollTarget(0.0f);
    }
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    ComponentKeyService[] componentKeyServiceArray;
    List<AnticheatCodec.Entry> list =
        this.view2.entries().stream()
            .filter(entry -> entry.matches(this.text2, this.text3, this.fczhtab2khzf, this.status2))
            .sorted(this.sort2.comparator(this.enabled2))
            .toList();
    Consumer<LayoutContainerNode> consumer =
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.size(
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456)),
                                                LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .padding(
                                            this.mfjzyzgupop1()
                                                ? 0.0f
                                                : Float.intBitsToFloat(1098907648)))
                                .align(ScenePctService.Align.CENTER))
                        .justify(ScenePctService.Justify.CENTER))
                .pointerEvents(this.menu2 == Menu.NONE);
    ComponentKeyService[] componentKeyServiceArray2 = new ComponentKeyService[1];
    Consumer<SceneCornerRadiusService> consumer2 =
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id(
                                                        "anticheats.workspace"))
                                                .size(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456)),
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                        .maxWidth(Float.intBitsToFloat(1150353408)))
                                .maxHeight(Float.intBitsToFloat(1146552320)))
                        .cornerRadius(this.mfjzyzgupop1() ? 0.0f : Float.intBitsToFloat(1105199104))
                        .backgroundColor(MaterialIsLightService.SURFACE)
                        .direction(ScenePctService.Direction.COLUMN))
                .clip(true);
    ComponentKeyService[] componentKeyServiceArray3 = new ComponentKeyService[7];
    componentKeyServiceArray3[0] = this.createComponentKeyService2();
    componentKeyServiceArray3[1] =
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.padding(
                                            0.0f,
                                            this.mfjzyzgupop1()
                                                ? Float.intBitsToFloat(0x41400000)
                                                : Float.intBitsToFloat(1103101952)))
                                    .gap(Float.intBitsToFloat(0x41000000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.search(
                    "anticheats.search",
                    this.mfjzyzgupop1()
                        ? "Find an anticheat\u2026"
                        : "Search name, version or status\u2026",
                    this.text2,
                    string -> {
                      this.text2 = string;
                      this.updateState2();
                    },
                    true)
                .props(
                    interactiveSurfacePanel ->
                        ((SceneCornerRadiusService) interactiveSurfacePanel.flex(1.0f))
                            .minWidth(0.0f)),
            MaterialTextService.button(
                    "anticheats.free",
                    "Free",
                    () -> {
                      this.fczhtab2khzf = !this.fczhtab2khzf;
                      this.updateState2();
                    },
                    this.fczhtab2khzf)
                .props(
                    materialJoinedService -> {
                      materialJoinedService
                          .colors(
                              this.fczhtab2khzf ? MaterialIsLightService.PRIMARY : 0,
                              this.fczhtab2khzf
                                  ? MaterialIsLightService.ON_PRIMARY
                                  : MaterialIsLightService.ON_SURFACE_VARIANT)
                          .outlined(!this.fczhtab2khzf);
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      materialJoinedService.height(
                                          LayoutOperationHandler.px(
                                              this.checkCondition2()
                                                  ? Float.intBitsToFloat(0x42200000)
                                                  : Float.intBitsToFloat(0x42400000))))
                                  .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                          .tooltip("Only free anticheats");
                    }));
    componentKeyServiceArray3[2] = this.m58q3w3q73ks();
    ComponentKeyService[] componentKeyServiceArray4 = new ComponentKeyService[2];
    componentKeyServiceArray4[0] =
        MaterialTextService.text(
                list.size()
                    + (String)
                        (this.text2.isBlank()
                                && this.text3.isEmpty()
                                && !this.fczhtab2khzf
                                && this.status2 == null
                            ? " anticheats"
                            : " of " + this.view2.entries().size() + " anticheats"),
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService) sceneTextService.id("anticheats.count")).flex(1.0f))
                        .minWidth(0.0f));
    componentKeyServiceArray4[1] =
        MaterialTextService.text(
            this.sort2 == AnticheatCodec.Sort.NAME
                ? (this.enabled2 ? "Z\u2013A" : "A\u2013Z")
                : this.sort2.label,
            Float.intBitsToFloat(0x41400000),
            MaterialIsLightService.OUTLINE);
    componentKeyServiceArray3[3] =
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.padding(
                                            Float.intBitsToFloat(0x40800000),
                                            this.mfjzyzgupop1()
                                                ? Float.intBitsToFloat(1098907648)
                                                : Float.intBitsToFloat(1105199104)))
                                    .align(ScenePctService.Align.CENTER))
                            .flexShrink(0.0f))
                    .visible(!this.checkCondition2()),
            componentKeyServiceArray4);
    componentKeyServiceArray3[4] =
        this.checkCondition3() && !list.isEmpty()
            ? this.createComponentKeyService7()
            : ComponentBoxService.box(
                layoutContainerNode -> layoutContainerNode.visible(false),
                new ComponentKeyService[0]);
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
                                                                                            "anticheats.list"))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .flex(1.0f))
                                                                .minHeight(0.0f))
                                                        .padding(
                                                            Float.intBitsToFloat(0x40800000),
                                                            this.mfjzyzgupop1()
                                                                ? Float.intBitsToFloat(0x41400000)
                                                                : Float.intBitsToFloat(1103101952),
                                                            this.checkCondition2()
                                                                ? Float.intBitsToFloat(0x40800000)
                                                                : Float.intBitsToFloat(0x41400000),
                                                            this.mfjzyzgupop1()
                                                                ? Float.intBitsToFloat(0x41400000)
                                                                : Float.intBitsToFloat(1103101952)))
                                                .gap(Float.intBitsToFloat(0x40800000)))
                                        .scrollable(true))
                                .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                        .scrollbarColor(MaterialIsLightService.OUTLINE))
                .clip(true);
    if (list.isEmpty()) {
      ComponentKeyService[] componentKeyServiceArray5 = new ComponentKeyService[1];
      componentKeyServiceArray = componentKeyServiceArray5;
      componentKeyServiceArray5[0] = this.mdb68zh3tz2i();
    } else {
      componentKeyServiceArray =
          (ComponentKeyService[])
              list.stream()
                  .map(this::createComponentKeyService8)
                  .toArray(ComponentKeyService[]::new);
    }
    componentKeyServiceArray3[5] = ComponentBoxService.column(consumer3, componentKeyServiceArray);
    componentKeyServiceArray3[6] = this.createComponentKeyService12();
    componentKeyServiceArray2[0] = ComponentBoxService.panel(consumer2, componentKeyServiceArray3);
    ComponentKeyService<LayoutContainerNode> componentKeyService =
        ComponentBoxService.column(consumer, componentKeyServiceArray2).key("anticheats");
    return ComponentBoxService.stack(
        layoutContainerNode ->
            layoutContainerNode.size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))),
        componentKeyService,
        this.menu2 == Menu.NONE
            ? ComponentBoxService.box(
                    layoutContainerNode -> layoutContainerNode.visible(false),
                    new ComponentKeyService[0])
                .key("menu")
            : this.createComponentKeyService5());
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.height(
                                                LayoutOperationHandler.px(
                                                    this.checkCondition2()
                                                        ? Float.intBitsToFloat(0x42400000)
                                                        : Float.intBitsToFloat(1116733440))))
                                        .padding(
                                            Float.intBitsToFloat(0x40800000),
                                            this.mfjzyzgupop1()
                                                ? Float.intBitsToFloat(0x41000000)
                                                : Float.intBitsToFloat(1098907648)))
                                .gap(Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        MaterialTextService.iconButton(
                "anticheats.back", "arrow_back", "Back to ClickGUI", this::updateState5)
            .props(
                materialJoinedService ->
                    materialJoinedService.size(
                        this.checkCondition2()
                            ? Float.intBitsToFloat(0x42200000)
                            : Float.intBitsToFloat(0x42400000),
                        this.checkCondition2()
                            ? Float.intBitsToFloat(0x42200000)
                            : Float.intBitsToFloat(0x42400000))),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                    .gap(0.0f),
            MaterialTextService.text(
                "Anticheats",
                this.mfjzyzgupop1()
                    ? Float.intBitsToFloat(1102053376)
                    : Float.intBitsToFloat(1104150528),
                MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                    "Minecraft Anticheat List",
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        sceneTextService.visible(
                            (this.fgvjnn3sg1bz >= Float.intBitsToFloat(1137180672)
                                        && !this.mfjzyzgupop1()
                                    ? 1
                                    : 0)
                                != 0))),
        MaterialTextService.iconButton(
                "anticheats.refresh",
                "refresh",
                this.view2.loading() ? "Updating the list\u2026" : "Refresh list",
                () -> {
                  this.anticheatRepository.load(true);
                  this.updateState6();
                })
            .props(
                materialJoinedService ->
                    materialJoinedService
                        .available(!this.view2.loading())
                        .size(
                            this.checkCondition2()
                                ? Float.intBitsToFloat(0x42200000)
                                : Float.intBitsToFloat(0x42400000),
                            this.checkCondition2()
                                ? Float.intBitsToFloat(0x42200000)
                                : Float.intBitsToFloat(0x42400000))));
  }

  private ComponentKeyService<?> m58q3w3q73ks() {
    ComponentKeyService[] componentKeyServiceArray = new ComponentKeyService[5];
    componentKeyServiceArray[0] =
        this.createComponentKeyService4(
            "platform",
            this.text3.isEmpty()
                ? (this.mfjzyzgupop1() ? "Platform" : "All platforms")
                : this.text3,
            Menu.PLATFORM);
    componentKeyServiceArray[1] =
        this.createComponentKeyService4(
            "status",
            this.status2 == null
                ? (this.mfjzyzgupop1() ? "Status" : "All statuses")
                : this.status2.label,
            Menu.STATUS);
    componentKeyServiceArray[2] =
        this.createComponentKeyService4("sort", this.sort2.label, Menu.SORT);
    componentKeyServiceArray[3] =
        MaterialTextService.iconButton(
                "anticheats.direction",
                "sort",
                this.enabled2
                    ? "Descending \u00b7 click for ascending"
                    : "Ascending \u00b7 click for descending",
                () -> {
                  this.enabled2 = !this.enabled2;
                  this.updateState2();
                })
            .props(
                materialJoinedService ->
                    materialJoinedService.size(
                        Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1108344832)))
            .children(
                MaterialTextService.text(
                    this.enabled2 ? "\u2193" : "\u2191",
                    Float.intBitsToFloat(1101004800),
                    MaterialIsLightService.PRIMARY));
    componentKeyServiceArray[4] =
        ComponentBoxService.box(
            layoutContainerNode ->
                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                    .visible(!this.mfjzyzgupop1()),
            new ComponentKeyService[0]);
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            layoutContainerNode.id(
                                                                "anticheats.filters"))
                                                        .width(
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .height(
                                                    LayoutOperationHandler.px(
                                                        this.checkCondition2()
                                                            ? Float.intBitsToFloat(0x42200000)
                                                            : Float.intBitsToFloat(1110441984))))
                                        .padding(
                                            2.0f,
                                            this.mfjzyzgupop1()
                                                ? Float.intBitsToFloat(0x41400000)
                                                : Float.intBitsToFloat(1103101952)))
                                .gap(Float.intBitsToFloat(0x40C00000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        componentKeyServiceArray);
  }

  private ComponentKeyService<?> createComponentKeyService4(
      String string, String string2, Menu menu) {
    return MaterialTextService.button(
            "anticheats." + string,
            string2,
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.menu2 = menu;
              this.updateState();
            },
            false)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .colors(
                      MaterialIsLightService.SURFACE_HIGH,
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .shape(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x42000000));
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      ((SceneCornerRadiusService)
                                                              materialJoinedService.height(
                                                                  LayoutOperationHandler.px(
                                                                      Float.intBitsToFloat(
                                                                          1108344832))))
                                                          .padding(
                                                              0.0f,
                                                              this.mfjzyzgupop1()
                                                                  ? Float.intBitsToFloat(0x40C00000)
                                                                  : Float.intBitsToFloat(
                                                                      0x41400000)))
                                                  .minWidth(0.0f))
                                          .gap(
                                              this.mfjzyzgupop1()
                                                  ? Float.intBitsToFloat(0x40800000)
                                                  : Float.intBitsToFloat(0x40C00000)))
                                  .flex(this.mfjzyzgupop1() ? 1.0f : 0.0f))
                          .flexShrink(1.0f))
                  .tooltip(
                      menu == Menu.SORT ? "Sort by " + this.sort2.label : "Filter by " + string);
            })
        .children(
            MaterialTextService.text(
                    string2,
                    this.mfjzyzgupop1()
                        ? Float.intBitsToFloat(1093664768)
                        : Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.minWidth(0.0f)).flexShrink(1.0f)),
            MaterialTextService.icon("expand_more", MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648))));
  }

  private ComponentKeyService<?> createComponentKeyService5() {
    ArrayList arrayList = new ArrayList();
    String menuTitle =
        switch (this.menu2) {
          case PLATFORM -> "Platform";
          case STATUS -> "Status";
          default -> "Sort by";
        };
    if (this.menu2 == Menu.PLATFORM) {
      arrayList.add(
          this.createComponentKeyService6(
              "platform.all",
              "All platforms",
              this.text3.isEmpty(),
              () -> {
                this.text3 = "";
              }));
      TreeSet treeSet = new TreeSet();
      this.view2.entries().forEach(entry -> treeSet.addAll(entry.platforms()));
      for (String string3 : (Iterable<String>) (Iterable<?>) (treeSet)) {
        arrayList.add(
            this.createComponentKeyService6(
                "platform." + string3,
                string3,
                this.text3.equals(string3),
                () -> {
                  this.text3 = string3;
                }));
      }
    } else if (this.menu2 == Menu.STATUS) {
      arrayList.add(
          this.createComponentKeyService6(
              "status.all",
              "All statuses",
              this.status2 == null,
              () -> {
                this.status2 = null;
              }));
      for (AnticheatCodec.Status status : AnticheatCodec.Status.values()) {
        if (status == AnticheatCodec.Status.CHECKING) continue;
        arrayList.add(
            this.createComponentKeyService6(
                "status." + status.name(),
                status.label,
                this.status2 == status,
                () -> {
                  this.status2 = status;
                }));
      }
    } else {
      for (AnticheatCodec.Sort sort : AnticheatCodec.Sort.values()) {
        arrayList.add(
            this.createComponentKeyService6(
                "sort." + sort.name(),
                sort.label,
                this.sort2 == sort,
                () -> {
                  this.sort2 = sort;
                }));
      }
    }
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        ((SceneCornerRadiusService)
                                                                                ((SceneCornerRadiusService)
                                                                                        sceneCornerRadiusService
                                                                                            .id(
                                                                                                "anticheats.menu-scrim"))
                                                                                    .size(
                                                                                        LayoutOperationHandler
                                                                                            .percent(
                                                                                                Float
                                                                                                    .intBitsToFloat(
                                                                                                        1120403456)),
                                                                                        LayoutOperationHandler
                                                                                            .percent(
                                                                                                Float
                                                                                                    .intBitsToFloat(
                                                                                                        1120403456))))
                                                                            .padding(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1098907648)))
                                                                    .layerBreak(true))
                                                            .backgroundColor(-1728053248)
                                                            .direction(
                                                                ScenePctService.Direction.COLUMN))
                                                    .align(ScenePctService.Align.CENTER))
                                            .justify(ScenePctService.Justify.CENTER))
                                    .interactive(true))
                            .onClick(this::updateState3))
                    .stopPropagation(true),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            ((SceneCornerRadiusService)
                                                                    ((SceneCornerRadiusService)
                                                                            ((SceneCornerRadiusService)
                                                                                    sceneCornerRadiusService
                                                                                        .id(
                                                                                            "anticheats.menu"))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .maxWidth(
                                                                            Float.intBitsToFloat(
                                                                                1135869952)))
                                                                .height(
                                                                    LayoutOperationHandler.px(
                                                                        Math.min(
                                                                            this.fgvjnn3sg1bz
                                                                                - Float
                                                                                    .intBitsToFloat(
                                                                                        0x42000000),
                                                                            (float)
                                                                                (arrayList.size()
                                                                                        * 44
                                                                                    + 76)))))
                                                        .cornerRadius(
                                                            Float.intBitsToFloat(1103101952))
                                                        .backgroundColor(
                                                            MaterialIsLightService.SURFACE_HIGH)
                                                        .padding(Float.intBitsToFloat(0x41400000)))
                                                .direction(ScenePctService.Direction.COLUMN))
                                        .gap(Float.intBitsToFloat(0x40800000)))
                                .interactive(true))
                        .stopPropagation(true),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.height(
                                                    LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(1110441984))))
                                            .gap(Float.intBitsToFloat(0x41000000)))
                                    .align(ScenePctService.Align.CENTER))
                            .flexShrink(0.0f),
                    MaterialTextService.text(
                            menuTitle,
                            Float.intBitsToFloat(1101004800),
                            MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService ->
                                ((SceneTextService)
                                        ((SceneTextService) sceneTextService.flex(1.0f))
                                            .minWidth(0.0f))
                                    .padding(0.0f, Float.intBitsToFloat(0x41000000))),
                    MaterialTextService.iconButton(
                            "anticheats.menu.close", "close", "Close", this::updateState3)
                        .props(
                            materialJoinedService ->
                                materialJoinedService.size(
                                    Float.intBitsToFloat(0x42200000),
                                    Float.intBitsToFloat(0x42200000)))),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        ((LayoutContainerNode)
                                                                                layoutContainerNode
                                                                                    .id(
                                                                                        "anticheats.menu.options"))
                                                                            .width(
                                                                                LayoutOperationHandler
                                                                                    .percent(
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1120403456))))
                                                                    .flex(1.0f))
                                                            .minHeight(0.0f))
                                                    .scrollable(true))
                                            .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                                    .scrollbarColor(MaterialIsLightService.OUTLINE))
                            .gap(2.0f),
                    (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))))
        .key("menu");
  }

  private ComponentKeyService<?> createComponentKeyService6(
      String string, String string2, boolean bl, Runnable runnable) {
    return MaterialTextService.button(
            "anticheats.option." + string,
            string2,
            () -> {
              runnable.run();
              this.menu2 = Menu.NONE;
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              this.updateState2();
            },
            false)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .colors(
                      bl ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                      MaterialIsLightService.ON_SURFACE)
                  .shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(0x42200000));
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              materialJoinedService.width(
                                                  LayoutOperationHandler.percent(
                                                      Float.intBitsToFloat(1120403456))))
                                          .height(
                                              LayoutOperationHandler.px(
                                                  Float.intBitsToFloat(1110441984))))
                                  .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                          .justify(ScenePctService.Justify.START))
                  .gap(Float.intBitsToFloat(0x41400000));
            })
        .children(
            MaterialTextService.icon(
                    bl ? "check" : "radio_button_unchecked",
                    bl ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE)
                .props(
                    sceneSrcService ->
                        sceneSrcService.size(
                            Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800))),
            MaterialTextService.text(
                    string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)));
  }

  private void updateState3() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.menu2 = Menu.NONE;
    this.updateState();
  }

  private ComponentKeyService<?> createComponentKeyService7() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.padding(
                                        Float.intBitsToFloat(0x40C00000),
                                        Float.intBitsToFloat(0x42200000)))
                                .gap(Float.intBitsToFloat(1098907648)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        this.createComponentKeyService10(
            "ANTICHEAT",
            Float.intBitsToFloat(1075000115),
            Float.intBitsToFloat(1093664768),
            MaterialIsLightService.OUTLINE),
        this.createComponentKeyService10(
            "PLATFORM",
            Float.intBitsToFloat(1068289229),
            Float.intBitsToFloat(1093664768),
            MaterialIsLightService.OUTLINE),
        this.createComponentKeyService10(
            "STATUS",
            Float.intBitsToFloat(1068289229),
            Float.intBitsToFloat(1093664768),
            MaterialIsLightService.OUTLINE),
        this.createComponentKeyService10(
            "VERSIONS",
            Float.intBitsToFloat(0x3FD33333),
            Float.intBitsToFloat(1093664768),
            MaterialIsLightService.OUTLINE),
        this.createComponentKeyService10(
            "PRICE",
            Float.intBitsToFloat(1066192077),
            Float.intBitsToFloat(1093664768),
            MaterialIsLightService.OUTLINE),
        ComponentBoxService.box(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.size(Float.intBitsToFloat(1101004800), 1.0f))
                    .flexShrink(0.0f),
            new ComponentKeyService[0]));
  }

  private ComponentKeyService<?> createComponentKeyService8(AnticheatCodec.Entry entry) {
    boolean bl = entry.name().equals(this.text4);
    String string = "anticheats.entry." + entry.name();
    ComponentKeyService<MaterialJoinedService> componentKeyService =
        ComponentBoxService.node(
                "anticheat-row",
                MaterialJoinedService::new,
                materialJoinedService -> {
                  materialJoinedService
                      .colors(
                          bl
                              ? MaterialIsLightService.SECONDARY_CONTAINER
                              : MaterialIsLightService.SURFACE_CONTAINER,
                          MaterialIsLightService.ON_SURFACE)
                      .shape(Float.intBitsToFloat(1098907648), 0.0f);
                  ((SceneCornerRadiusService)
                          ((SceneCornerRadiusService)
                                  ((SceneCornerRadiusService)
                                          ((SceneCornerRadiusService)
                                                  ((SceneCornerRadiusService)
                                                          ((SceneCornerRadiusService)
                                                                  ((SceneCornerRadiusService)
                                                                          ((SceneCornerRadiusService)
                                                                                  ((SceneCornerRadiusService)
                                                                                          materialJoinedService
                                                                                              .id(
                                                                                                  string))
                                                                                      .width(
                                                                                          LayoutOperationHandler
                                                                                              .percent(
                                                                                                  Float
                                                                                                      .intBitsToFloat(
                                                                                                          1120403456))))
                                                                              .minHeight(
                                                                                  this
                                                                                          .checkCondition3()
                                                                                      ? Float
                                                                                          .intBitsToFloat(
                                                                                              1115160576)
                                                                                      : (this
                                                                                              .checkCondition2()
                                                                                          ? Float
                                                                                              .intBitsToFloat(
                                                                                                  1116209152)
                                                                                          : Float
                                                                                              .intBitsToFloat(
                                                                                                  1119354880))))
                                                                      .padding(
                                                                          this.checkCondition2()
                                                                              ? Float
                                                                                  .intBitsToFloat(
                                                                                      0x40C00000)
                                                                              : Float
                                                                                  .intBitsToFloat(
                                                                                      0x41400000),
                                                                          Float.intBitsToFloat(
                                                                              1098907648)))
                                                              .gap(
                                                                  this.checkCondition2()
                                                                      ? Float.intBitsToFloat(
                                                                          0x41000000)
                                                                      : Float.intBitsToFloat(
                                                                          1098907648)))
                                                      .direction(ScenePctService.Direction.ROW))
                                              .align(ScenePctService.Align.CENTER))
                                      .flexShrink(0.0f))
                              .onClick(
                                  () -> {
                                    this.materialKeyService.clear(
                                        this.materialTerrainTooltipsService);
                                    this.text4 = bl ? "" : entry.name();
                                    this.updateState();
                                  }))
                      .tooltip(bl ? "Hide project links" : "Show project links");
                },
                new ComponentKeyService[0])
            .key(string);
    componentKeyService =
        this.checkCondition3()
            ? componentKeyService.children(
                this.createComponentKeyService10(
                    entry.name(),
                    Float.intBitsToFloat(1075000115),
                    Float.intBitsToFloat(1097859072),
                    MaterialIsLightService.ON_SURFACE),
                this.createComponentKeyService10(
                    String.join((CharSequence) " \u00b7 ", entry.platforms()),
                    Float.intBitsToFloat(1068289229),
                    Float.intBitsToFloat(1095761920),
                    MaterialIsLightService.ON_SURFACE_VARIANT),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                layoutContainerNode.flex(Float.intBitsToFloat(1068289229)))
                            .minWidth(0.0f),
                    this.createComponentKeyService9(entry.status())),
                this.createComponentKeyService10(
                    entry.versions(),
                    Float.intBitsToFloat(0x3FD33333),
                    Float.intBitsToFloat(1095761920),
                    MaterialIsLightService.ON_SURFACE_VARIANT),
                this.createComponentKeyService10(
                    entry.price(),
                    Float.intBitsToFloat(1066192077),
                    Float.intBitsToFloat(1095761920),
                    entry.price().equalsIgnoreCase("Free")
                        ? MaterialIsLightService.PRIMARY
                        : MaterialIsLightService.ON_SURFACE),
                MaterialTextService.icon(
                        bl ? "expand_more" : "chevron_right",
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1101004800),
                                Float.intBitsToFloat(1101004800))))
            : componentKeyService.children(
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(this.checkCondition2() ? 2.0f : Float.intBitsToFloat(0x40C00000)),
                    ComponentBoxService.row(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    layoutContainerNode.gap(Float.intBitsToFloat(0x41000000)))
                                .align(ScenePctService.Align.CENTER),
                        MaterialTextService.text(
                                entry.name(),
                                this.checkCondition2()
                                    ? Float.intBitsToFloat(1096810496)
                                    : Float.intBitsToFloat(1098907648),
                                MaterialIsLightService.ON_SURFACE)
                            .props(
                                sceneTextService ->
                                    ((SceneTextService)
                                            ((SceneTextService) sceneTextService.flex(1.0f))
                                                .minWidth(0.0f))
                                        .wordWrap(true)),
                        this.createComponentKeyService9(entry.status())),
                    MaterialTextService.text(
                            String.join((CharSequence) " \u00b7 ", entry.platforms())
                                + "  \u00b7  "
                                + entry.versions(),
                            Float.intBitsToFloat(0x41400000),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService -> sceneTextService.wordWrap(true)),
                    MaterialTextService.text(
                            entry.price(),
                            Float.intBitsToFloat(0x41400000),
                            entry.price().equalsIgnoreCase("Free")
                                ? MaterialIsLightService.PRIMARY
                                : MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService -> sceneTextService.wordWrap(true))),
                MaterialTextService.icon(
                        bl ? "expand_more" : "chevron_right",
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        sceneSrcService ->
                            sceneSrcService.size(
                                Float.intBitsToFloat(1101004800),
                                Float.intBitsToFloat(1101004800))));
    if (!bl) {
      return componentKeyService;
    }
    ComponentKeyService[] componentKeyServiceArray =
        (ComponentKeyService[])
            entry.links().stream()
                .map(
                    link ->
                        MaterialTextService.button(
                                string + ".link." + String.valueOf(link.uri()),
                                link.name(),
                                () -> this.mghhxamtwwqa(link.uri()),
                                false)
                            .props(
                                materialJoinedService ->
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            materialJoinedService.width(
                                                                this.mfjzyzgupop1()
                                                                    ? LayoutOperationHandler
                                                                        .percent(
                                                                            Float.intBitsToFloat(
                                                                                1120403456))
                                                                    : LayoutOperationHandler
                                                                        .auto()))
                                                        .maxWidth(
                                                            this.mfjzyzgupop1()
                                                                ? Float.intBitsToFloat(1176256512)
                                                                : Float.intBitsToFloat(1133903872)))
                                                .tooltip(link.uri().toString()))
                                        .gap(Float.intBitsToFloat(0x41000000)))
                            .children(
                                MaterialTextService.icon(
                                        "open_in_new",
                                        MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                    .props(
                                        sceneSrcService ->
                                            sceneSrcService.size(
                                                Float.intBitsToFloat(1099956224),
                                                Float.intBitsToFloat(1099956224))),
                                MaterialTextService.label(
                                        link.name(), MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                    .props(
                                        sceneTextService ->
                                            ((SceneTextService) sceneTextService.minWidth(0.0f))
                                                .flexShrink(1.0f))))
                .toArray(ComponentKeyService[]::new);
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(0.0f))
                    .flexShrink(0.0f),
            componentKeyService,
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            sceneCornerRadiusService.id(
                                                                string + ".details"))
                                                        .width(
                                                            LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                                .padding(
                                                    Float.intBitsToFloat(0x41400000),
                                                    Float.intBitsToFloat(1098907648)))
                                        .cornerRadius(Float.intBitsToFloat(1098907648))
                                        .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                                        .direction(ScenePctService.Direction.COLUMN))
                                .gap(Float.intBitsToFloat(0x41000000)))
                        .flexShrink(0.0f),
                MaterialTextService.text(
                    "PROJECT LINKS",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.PRIMARY),
                entry.links().isEmpty()
                    ? MaterialTextService.text(
                        "No links listed by the source.",
                        Float.intBitsToFloat(1095761920),
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    : ComponentBoxService.column(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    layoutContainerNode.gap(Float.intBitsToFloat(0x40C00000)))
                                .align(ScenePctService.Align.START),
                        componentKeyServiceArray)))
        .key(string + ".expanded");
  }

  private ComponentKeyService<?> createComponentKeyService9(AnticheatCodec.Status status) {
    int n =
        switch (status) {
          case AnticheatCodec.Status.ACTIVE -> -5450065;
          case AnticheatCodec.Status.DISCONTINUED, AnticheatCodec.Status.UNAVAILABLE ->
              MaterialIsLightService.ERROR;
          case AnticheatCodec.Status.OLD, AnticheatCodec.Status.INACTIVE ->
              MaterialIsLightService.TERTIARY;
          default -> MaterialIsLightService.ON_SURFACE_VARIANT;
        };
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            sceneCornerRadiusService
                                .backgroundColor(
                                    MaterialIsLightService.layer(
                                        MaterialIsLightService.SURFACE_CONTAINER,
                                        n,
                                        Float.intBitsToFloat(0x3DCCCCCD)))
                                .cornerRadius(Float.intBitsToFloat(0x41000000))
                                .padding(
                                    Float.intBitsToFloat(0x40400000),
                                    Float.intBitsToFloat(0x40E00000)))
                        .flexShrink(0.0f))
                .pointerEvents(false),
        MaterialTextService.text(status.label, Float.intBitsToFloat(1093664768), n));
  }

  private ComponentKeyService<?> createComponentKeyService10(
      String string, float f, float f2, int n) {
    return MaterialTextService.text(string, f2, n)
        .props(
            sceneTextService ->
                ((SceneTextService) ((SceneTextService) sceneTextService.flex(f)).minWidth(0.0f))
                    .wordWrap(true)
                    .maxLines(2)
                    .tooltip(string));
  }

  private ComponentKeyService<?> mdb68zh3tz2i() {
    ComponentKeyService[] componentKeyServiceArray = new ComponentKeyService[3];
    componentKeyServiceArray[0] =
        MaterialTextService.icon(
                this.view2.loading() ? "refresh" : "search", MaterialIsLightService.PRIMARY)
            .props(
                sceneSrcService ->
                    sceneSrcService.size(
                        Float.intBitsToFloat(1105199104), Float.intBitsToFloat(1105199104)));
    componentKeyServiceArray[1] =
        MaterialTextService.text(
            this.view2.loading() && this.view2.entries().isEmpty()
                ? "Loading the list\u2026"
                : (this.view2.entries().isEmpty() ? "The list is unavailable" : "No matches"),
            Float.intBitsToFloat(1099956224),
            MaterialIsLightService.ON_SURFACE);
    componentKeyServiceArray[2] =
        MaterialTextService.text(
                this.view2.entries().isEmpty()
                    ? (this.view2.notice().isEmpty()
                        ? "Fetching the community catalog."
                        : this.view2.notice())
                    : "Try a different name, version or platform.",
                Float.intBitsToFloat(1095761920),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(sceneTextService -> sceneTextService.wordWrap(true));
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                                .padding(
                                    this.fgvjnn3sg1bz < Float.intBitsToFloat(1137180672)
                                        ? Float.intBitsToFloat(0x41400000)
                                        : Float.intBitsToFloat(1105199104)))
                        .gap(Float.intBitsToFloat(0x41000000)))
                .align(ScenePctService.Align.CENTER),
        componentKeyServiceArray);
  }

  private ComponentKeyService<?> createComponentKeyService12() {
    String string =
        this.view2.updated() == null
            ? "Community directory"
            : "Updated "
                + DateTimeFormatter.ofPattern("dd MMM \u00b7 HH:mm", Locale.ENGLISH)
                    .withZone(ZoneId.systemDefault())
                    .format(this.view2.updated());
    long l =
        this.view2.entries().stream()
            .filter(entry -> entry.matches(this.text2, this.text3, this.fczhtab2khzf, this.status2))
            .count();
    String string2 =
        this.view2.loading()
            ? "Updating\u2026"
            : (this.view2.notice().isEmpty() ? string : this.view2.notice());
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    layoutContainerNode.padding(
                                        this.checkCondition2()
                                            ? 2.0f
                                            : Float.intBitsToFloat(0x40800000),
                                        this.mfjzyzgupop1()
                                            ? Float.intBitsToFloat(0x41400000)
                                            : Float.intBitsToFloat(1103101952)))
                                .gap(Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f),
            MaterialTextService.text(
                    (String) (this.checkCondition2() ? l + " anticheats \u00b7 " : "") + string2,
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.id("anticheats.notice"))
                            .wordWrap(!this.checkCondition2())
                            .tooltip(string2)),
            MaterialTextService.text(
                    "by ManInMyVan \u00b7 Community data",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.OUTLINE)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                sceneTextService.visible(
                                    this.fgvjnn3sg1bz >= Float.intBitsToFloat(1137180672)))
                            .wordWrap(true))),
        MaterialTextService.iconButton(
                "anticheats.source",
                "open_in_new",
                "Open the original Minecraft Anticheat List",
                () ->
                    this.mghhxamtwwqa(
                        URI.create("https://maninmyvan.github.io/Minecraft-Anticheat-List/")))
            .props(
                materialJoinedService ->
                    materialJoinedService.size(
                        this.checkCondition2()
                            ? Float.intBitsToFloat(0x42000000)
                            : Float.intBitsToFloat(0x42200000),
                        this.checkCondition2()
                            ? Float.intBitsToFloat(0x42000000)
                            : Float.intBitsToFloat(0x42200000))));
  }

  private void mghhxamtwwqa(URI uRI) {
    if (AnticheatCodec.isWebLink(uRI)) {
      this.consumer.accept(uRI);
    }
  }

  private void updateState5() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    if (this.fasnmfavrpfs != null) {
      this.fasnmfavrpfs.close();
    }
  }

  private void updateState6() {
    this.view2 = this.anticheatRepository.view();
    this.updateState();
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenIdService) {
    this.anticheatRepository.load(false);
    this.updateState6();
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    if (this.materialTerrainTooltipsService != null
        && this.materialTerrainTooltipsService.computedW() > 0.0f) {
      this.viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
    }
    if (this.view2 != this.anticheatRepository.view()) {
      this.updateState6();
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
    if (n == 256) {
      if (this.menu2 != Menu.NONE) {
        this.updateState3();
      } else if (!this.text4.isEmpty()) {
        this.text4 = "";
        this.updateState();
      } else {
        this.updateState5();
      }
      return true;
    }
    if (n == 70 && (n2 & 0xA) != 0) {
      ScenePctService<?> scenePctService =
          this.materialTerrainTooltipsService.findById("anticheats.search");
      if (scenePctService instanceof ControlLetterSpacingService) {
        ControlLetterSpacingService controlLetterSpacingService =
            (ControlLetterSpacingService) scenePctService;
        this.materialKeyService.focus(controlLetterSpacingService);
      }
      return true;
    }
    return this.materialKeyService.key(
        this.menu2 == Menu.NONE
            ? this.materialTerrainTooltipsService
            : this.materialTerrainTooltipsService.findById("anticheats.menu"),
        n,
        n2);
  }

  private static enum Menu {
    NONE,
    PLATFORM,
    STATUS,
    SORT;
  }
}
