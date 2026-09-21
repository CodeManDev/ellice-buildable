package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.changelog.ChangelogRepository;
import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.menu.MenuLoaderTracker;
import dev.felix.ellice.server.ServerKeyService;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.component.ComponentUnmountService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.SceneFocusService;
import dev.felix.ellice.ui.scene.SceneImageService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneSelectionSelector;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BuiltinActionsTracker implements ScreenOperationHandler {
  private final ServerViewTracker serverViewTracker;
  private final MenuLoaderTracker menuLoaderTracker;
  private final Supplier<Session> supplier;
  private final Actions fe5fm4nwbka;
  private final String text2;
  private final ServerListRenderer renderer;
  private final WorldListRenderer renderer2;
  private ComponentMountService fru87gio83d;
  private ScreenScreenIdService screenScreenIdService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private SceneFocusService sceneFocusService;
  private Session session;
  private Page fukeosqhtbl = Page.HOME;
  private float value = Float.intBitsToFloat(1150025728);
  private float value2 = Float.intBitsToFloat(0x44340000);
  private long timestamp = -1L;
  private long timestamp2 = -1L;
  private long timestamp3;
  private ScenePctService<?> scenePctService;
  private float value3;
  private int count = 1;
  private final EnumSet<Page> fchakgedxeah = EnumSet.of(Page.HOME);
  private boolean enabled;
  private ComponentMountService componentMountService2;
  private LayoutContainerNode sceneComponent4;
  private BuiltinStateController builtinStateController;

  public BuiltinActionsTracker(
      ServerViewTracker serverViewTracker,
      MenuLoaderTracker menuLoaderTracker,
      Supplier<Session> supplier,
      Actions actions,
      String string) {
    this.serverViewTracker = Objects.requireNonNull(serverViewTracker);
    this.menuLoaderTracker = Objects.requireNonNull(menuLoaderTracker);
    this.supplier = Objects.requireNonNull(supplier);
    this.fe5fm4nwbka = Objects.requireNonNull(actions);
    this.text2 = string;
    this.renderer = new ServerListRenderer(serverViewTracker, actions.connect());
    this.renderer.expanded(true);
    this.renderer.preview(actions.preview());
    this.renderer2 =
        new WorldListRenderer(
            menuLoaderTracker, actions.playWorld(), actions.editWorld(), actions.createWorld());
  }

  @Override
  public String id() {
    return "main-menu";
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

  public Page page() {
    return this.fukeosqhtbl;
  }

  public void page(Page page) {
    Objects.requireNonNull(page);
    if (this.fukeosqhtbl == page) {
      return;
    }
    if (this.materialTerrainTooltipsService != null
        && this.fukeosqhtbl == Page.HOME
        && this.materialTerrainTooltipsService.findById("menu.home.scroll") != null) {
      this.value3 = this.materialTerrainTooltipsService.findById("menu.home.scroll").scrollY();
    }
    boolean bl = this.fchakgedxeah.contains((Object) page);
    this.count = Integer.compare(page.ordinal(), this.fukeosqhtbl.ordinal());
    this.updateState6();
    this.fukeosqhtbl = page;
    this.fchakgedxeah.add(page);
    if (this.sceneFocusService != null) {
      this.sceneFocusService.page(page.ordinal()).focus(false);
    }
    this.updateState();
    if (this.materialTerrainTooltipsService == null) {
      return;
    }
    for (Page page2 : EnumSet.copyOf(this.fchakgedxeah)) {
      int n;
      ScenePctService<?> scenePctService =
          this.materialTerrainTooltipsService.findById(
              "menu.view." + String.valueOf((Object) page2));
      if (scenePctService == null) continue;
      int n2 = n = page2 == this.fukeosqhtbl ? 1 : 0;
      if (n != 0 && !bl) {
        ((ScenePctService)
                ((ScenePctService) scenePctService.opacity(0.0f))
                    .translateY(Float.intBitsToFloat(1099956224)))
            .translateX(this.count * 8);
      }
      MotionAnimateService.animate(
          scenePctService,
          MotionColorsContainer.Floats.OPACITY,
          n != 0 ? 1.0f : 0.0f,
          SceneEaseHandler.Tween.ease(
              n != 0 ? Float.intBitsToFloat(1050924810) : Float.intBitsToFloat(1047904911)),
          0.0f,
          () -> {
            if (page2 != this.fukeosqhtbl) {
              this.fchakgedxeah.remove((Object) page2);
              this.updateState();
            }
          });
      MotionAnimateService.animate(
          scenePctService,
          MotionColorsContainer.Floats.TRANSLATE_Y,
          n != 0 ? 0.0f : Float.intBitsToFloat(-1054867456),
          (SceneEaseHandler)
              new SceneEaseHandler.Spring(
                  Float.intBitsToFloat(1105723392), Float.intBitsToFloat(1130758144)));
      MotionAnimateService.animate(
          scenePctService,
          MotionColorsContainer.Floats.TRANSLATE_X,
          n != 0 ? 0.0f : (float) (-this.count * 8),
          (SceneEaseHandler)
              new SceneEaseHandler.Spring(
                  Float.intBitsToFloat(1107820544), Float.intBitsToFloat(1133248512)));
    }
  }

  public void viewport(float f, float f2) {
    if (this.value == f && this.value2 == f2) {
      return;
    }
    this.value = f;
    this.value2 = f2;
    this.updateState6();
    this.updateState();
    if (this.componentMountService2 != null) {
      this.componentMountService2.invalidateComponent();
    }
    if (this.builtinStateController != null) {
      this.builtinStateController.viewport(f, f2);
    }
  }

  private boolean checkCondition() {
    return this.value < Float.intBitsToFloat(1142620160);
  }

  private boolean checkCondition2() {
    return this.value >= Float.intBitsToFloat(1148518400);
  }

  private boolean magoo8oxviwc() {
    return this.value2 < Float.intBitsToFloat(1137836032);
  }

  private float calculateValue() {
    return this.checkCondition() || this.magoo8oxviwc()
        ? Float.intBitsToFloat(0x41400000)
        : (this.value >= Float.intBitsToFloat(1149861888)
            ? Float.intBitsToFloat(0x42200000)
            : Float.intBitsToFloat(1103101952));
  }

  private float calculateValue2() {
    return Math.min(Float.intBitsToFloat(0x44B40000), this.value - this.calculateValue() * 2.0f);
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
    this.fchakgedxeah.clear();
    this.fchakgedxeah.add(this.fukeosqhtbl);
    this.screenScreenIdService = screenScreenIdService;
    this.session = this.supplier.get();
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            this.materialTerrainTooltipsService.id("main-menu.canvas"))
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .direction(ScenePctService.Direction.NONE))
        .backgroundColor(MaterialIsLightService.SURFACE_LOWEST)
        .interactive(true);
    this.sceneFocusService =
        new SceneFocusService().backdrop(true).page(this.fukeosqhtbl.ordinal());
    ((SceneFocusService)
            ((SceneFocusService)
                    ((SceneFocusService)
                            ((SceneFocusService) this.sceneFocusService.id("main-menu.material"))
                                .absolute())
                        .inset(LayoutOperationHandler.px(0.0f)))
                .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto()))
        .pointerEvents(false);
    this.materialTerrainTooltipsService.addChild(this.sceneFocusService);
    this.fru87gio83d =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService,
                screenScreenIdService == null
                    ? new ThemeIsSetService()
                    : screenScreenIdService.theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode) this.fru87gio83d.absolute())
                .inset(LayoutOperationHandler.px(0.0f)))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.materialTerrainTooltipsService.addChild(this.fru87gio83d);
    this.builtinStateController =
        new BuiltinStateController(
            ChangelogRepository.load(),
            this.fe5fm4nwbka.changelog(),
            screenScreenIdService == null
                ? new ThemeIsSetService()
                : screenScreenIdService.theme());
    this.builtinStateController.viewport(this.value, this.value2);
    this.materialTerrainTooltipsService.addChild(this.builtinStateController.node());
    return this.materialTerrainTooltipsService;
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenIdService) {
    if (screenScreenIdService != null) {
      this.serverViewTracker.reloadSaved();
    }
    this.menuLoaderTracker.reload();
    this.updateState();
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    this.updateState3();
    this.updateState6();
    if (this.builtinStateController != null) {
      this.builtinStateController.close();
    }
  }

  @Override
  public void onSuspend(ScreenScreenIdService screenScreenIdService) {
    this.updateState3();
    this.updateState6();
    if (this.builtinStateController != null) {
      this.builtinStateController.close();
    }
  }

  private void updateState() {
    if (this.fru87gio83d != null) {
      this.fru87gio83d.invalidateComponent();
    }
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    this.materialTerrainTooltipsService.backgroundColor(MaterialIsLightService.SURFACE_LOWEST);
    this.renderer.account(this.session.uuid(), this.session.name(), true);
    this.renderer.shortHeight(this.magoo8oxviwc());
    this.renderer2.shortHeight(this.magoo8oxviwc());
    this.renderer.width(this.calculateValue2() - this.calculateValue() * 2.0f);
    this.renderer2.width(this.calculateValue2() - this.calculateValue() * 2.0f);
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.id("menu.layout"))
                                    .size(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)),
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .padding(this.calculateValue()))
                    .justify(ScenePctService.Justify.CENTER),
            ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                layoutContainerNode.id(
                                                                    "menu.content"))
                                                            .flex(1.0f))
                                                    .minWidth(0.0f))
                                            .maxWidth(Float.intBitsToFloat(0x44B40000)))
                                    .height(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456))))
                            .gap(
                                this.magoo8oxviwc()
                                    ? Float.intBitsToFloat(0x41000000)
                                    : Float.intBitsToFloat(1103101952)),
                    this.createComponentKeyService3(),
                    ComponentBoxService.stack(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            layoutContainerNode.id("menu.pages"))
                                                        .flex(1.0f))
                                                .minHeight(0.0f))
                                        .minWidth(0.0f))
                                .width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))),
                        this.createComponentKeyService2(componentThemeService, Page.HOME),
                        this.createComponentKeyService2(componentThemeService, Page.SERVERS),
                        this.createComponentKeyService2(componentThemeService, Page.WORLDS)),
                    this.createComponentKeyService5(false)
                        .props(scenePctService -> scenePctService.visible(!this.checkCondition2())),
                    this.mbkdhqxmkbvk()
                        .props(
                            scenePctService ->
                                scenePctService.visible(
                                    (this.checkCondition2() && !this.magoo8oxviwc() ? 1 : 0) != 0)))
                .key("content"))
        .key("menu-layout");
  }

  private ComponentKeyService<?> createComponentKeyService2(
      ComponentThemeService componentThemeService, Page page) {
    ComponentKeyService<?> componentKeyService =
        switch (page.ordinal()) {
          default -> throw new MatchException(null, null);
          case 0 -> this.createComponentKeyService11();
          case 1 ->
              this.createComponentKeyService10(this.renderer.render(componentThemeService))
                  .props(scenePctService -> scenePctService.id("menu.page.servers"));
          case 2 ->
              this.createComponentKeyService10(this.renderer2.render(componentThemeService))
                  .props(scenePctService -> scenePctService.id("menu.page.worlds"));
        };
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                layoutContainerNode.id(
                                                                    "menu.view."
                                                                        + String.valueOf(
                                                                            (Object) page)))
                                                            .absolute())
                                                    .inset(LayoutOperationHandler.px(0.0f)))
                                            .size(
                                                LayoutOperationHandler.auto(),
                                                LayoutOperationHandler.auto()))
                                    .pointerEvents(this.fukeosqhtbl == page))
                            .visible(this.fchakgedxeah.contains((Object) page)))
                    .layerBreak(true),
            componentKeyService.props(
                scenePctService ->
                    ((ScenePctService)
                            ((ScenePctService)
                                    ((ScenePctService) scenePctService.flex(1.0f)).minHeight(0.0f))
                                .minWidth(0.0f))
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))))
        .key("page:" + String.valueOf((Object) page))
        .onMount(
            layoutContainerNode -> {
              layoutContainerNode.opacity(0.0f);
              if (this.fukeosqhtbl == page) {
                MotionAnimateService.animate(
                    layoutContainerNode,
                    MotionColorsContainer.Floats.OPACITY,
                    1.0f,
                    (SceneEaseHandler)
                        SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1050253722)));
              }
            });
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.id("menu.toolbar"))
                                            .height(
                                                LayoutOperationHandler.px(
                                                    this.magoo8oxviwc()
                                                        ? Float.intBitsToFloat(0x42400000)
                                                        : Float.intBitsToFloat(1113587712))))
                                    .gap(
                                        this.checkCondition()
                                            ? Float.intBitsToFloat(0x40800000)
                                            : Float.intBitsToFloat(0x41400000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            ComponentBoxService.node(
                "ellice-emblem",
                MaterialLabelService::new,
                materialLabelService ->
                    ((MaterialLabelService)
                            materialLabelService
                                .shape("clover4")
                                .active(true)
                                .tint(MaterialIsLightService.PRIMARY)
                                .size(
                                    Float.intBitsToFloat(1106247680),
                                    Float.intBitsToFloat(1106247680)))
                        .pointerEvents(false),
                new ComponentKeyService[0]),
            MaterialTextService.text(
                    "ellice", Float.intBitsToFloat(1104150528), MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        sceneTextService
                            .fontFamily("material-roboto-medium")
                            .letterSpacing(Float.intBitsToFloat(-1082130432))
                            .visible(this.value >= Float.intBitsToFloat(1136525312))),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f),
                new ComponentKeyService[0]),
            this.createComponentKeyService5(true)
                .props(scenePctService -> scenePctService.visible(this.checkCondition2())),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .visible(this.checkCondition2()),
                new ComponentKeyService[0]),
            MaterialTextService.button(
                    "menu.accounts",
                    this.session.name(),
                    this.fe5fm4nwbka.accounts(),
                    ScenePctService.mulAlpha(
                        MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(0x3F666666)),
                    MaterialIsLightService.ON_SURFACE,
                    Float.intBitsToFloat(1103101952))
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                materialJoinedService.maxWidth(
                                                    this.checkCondition()
                                                        ? Float.intBitsToFloat(0x42400000)
                                                        : Float.intBitsToFloat(1129447424)))
                                            .minWidth(Float.intBitsToFloat(0x42400000)))
                                    .padding(
                                        0.0f,
                                        this.checkCondition()
                                            ? Float.intBitsToFloat(1092616192)
                                            : Float.intBitsToFloat(0x41400000)))
                            .blur(Float.intBitsToFloat(1099956224))
                            .tooltip("Manage accounts & favourites"))
                .children(
                    this.mgorlin1xta(),
                    MaterialTextService.label(
                            this.session.name(), MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService ->
                                ((SceneTextService)
                                        ((SceneTextService) sceneTextService.minWidth(0.0f))
                                            .flex(1.0f))
                                    .visible(!this.checkCondition())),
                    MaterialTextService.icon(
                            "expand_more", MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(
                            sceneSrcService ->
                                ((SceneSrcService)
                                        sceneSrcService.size(
                                            Float.intBitsToFloat(1099956224),
                                            Float.intBitsToFloat(1099956224)))
                                    .visible(!this.checkCondition()))),
            MaterialTextService.iconButton(
                "menu.appearance", "palette", "Appearance & performance", this::updateState4),
            MaterialTextService.iconButton(
                "menu.tools", "tune", "Client tools & settings", this::updateState2))
        .key("toolbar");
  }

  private ComponentKeyService<?> mgorlin1xta() {
    String string = "mc.head." + String.valueOf(this.session.uuid());
    boolean bl =
        CoreIsInitializedHandler.isReady()
            && CoreIsInitializedHandler.get().compositor().getNamedTexture(string) != null
            && CoreIsInitializedHandler.get().compositor().getNamedTexture(string).valid();
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        sceneCornerRadiusService.size(
                                            Float.intBitsToFloat(1105199104),
                                            Float.intBitsToFloat(1105199104)))
                                    .flexShrink(0.0f))
                            .cornerRadius(Float.intBitsToFloat(1096810496))
                            .backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
                            .direction(ScenePctService.Direction.NONE))
                    .pointerEvents(false),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.size(
                                                Float.intBitsToFloat(1105199104),
                                                Float.intBitsToFloat(1105199104)))
                                        .align(ScenePctService.Align.CENTER))
                                .justify(ScenePctService.Justify.CENTER))
                        .visible(!bl),
                MaterialTextService.text(
                        this.session.name(),
                        Float.intBitsToFloat(1096810496),
                        MaterialIsLightService.ON_PRIMARY_CONTAINER)
                    .props(sceneTextService -> sceneTextService.initial(true))),
            ComponentBoxService.node(
                "menu-avatar",
                SceneTextureService::new,
                sceneTextureService ->
                    ((SceneTextureService)
                            sceneTextureService
                                .textureName(string)
                                .cornerRadius(Float.intBitsToFloat(1096810496))
                                .size(
                                    Float.intBitsToFloat(1105199104),
                                    Float.intBitsToFloat(1105199104)))
                        .visible(bl),
                new ComponentKeyService[0]))
        .key("avatar:" + String.valueOf(this.session.uuid()));
  }

  private ComponentKeyService<?> createComponentKeyService5(boolean bl) {
    return ComponentBoxService.node(
            "menu-navigation-surface",
            SceneSelectionSelector::new,
            sceneSelectionSelector ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        sceneSelectionSelector
                                                                            .selection(
                                                                                this.fukeosqhtbl
                                                                                    .ordinal())
                                                                            .id(
                                                                                bl
                                                                                    ? "menu.navigation"
                                                                                    : "menu.bottom-nav"))
                                                                    .direction(
                                                                        ScenePctService.Direction
                                                                            .ROW))
                                                            .height(
                                                                LayoutOperationHandler.px(
                                                                    Float.intBitsToFloat(
                                                                        1112539136))))
                                                    .width(
                                                        bl
                                                            ? LayoutOperationHandler.px(
                                                                Float.intBitsToFloat(1135869952))
                                                            : LayoutOperationHandler.percent(
                                                                Float.intBitsToFloat(1120403456))))
                                            .flexShrink(0.0f))
                                    .padding(Float.intBitsToFloat(0x40800000)))
                            .gap(2.0f))
                    .align(ScenePctService.Align.CENTER),
            this.m13bb1k09wtz(Page.HOME, "Home", bl),
            this.m13bb1k09wtz(Page.SERVERS, "Servers", bl),
            this.m13bb1k09wtz(Page.WORLDS, "Worlds", bl))
        .key(bl ? "top-navigation" : "bottom-navigation");
  }

  private ComponentKeyService<?> m13bb1k09wtz(Page page, String string, boolean bl) {
    int n =
        this.fukeosqhtbl == page
            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
            : MaterialIsLightService.ON_SURFACE_VARIANT;
    return MaterialTextService.button(
            "menu.nav." + (bl ? "top." : "bottom.") + page.name().toLowerCase(Locale.ROOT),
            string,
            () -> this.page(page),
            0,
            n,
            Float.intBitsToFloat(1103101952))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        materialJoinedService.width(
                                                            LayoutOperationHandler.auto()))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            Float.intBitsToFloat(1110441984))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .flexShrink(1.0f))
                    .padding(0.0f, Float.intBitsToFloat(0x41000000)))
        .children(MaterialTextService.label(string, n));
  }

  private ComponentKeyService<?> mbkdhqxmkbvk() {
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.id("menu.footer"))
                                            .height(
                                                LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1108344832))))
                                    .gap(Float.intBitsToFloat(0x41000000)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.button(
                    "menu.changelog",
                    "ellice " + CoreIsInitializedHandler.VERSION,
                    this.fe5fm4nwbka.changelog(),
                    0,
                    MaterialIsLightService.ON_SURFACE_VARIANT,
                    Float.intBitsToFloat(1099956224))
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService.height(
                                    LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))))
                            .padding(0.0f, Float.intBitsToFloat(0x40800000)))
                .children(
                    MaterialTextService.text(
                        "ellice " + CoreIsInitializedHandler.VERSION,
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT)),
            MaterialTextService.text(
                "/  Minecraft " + this.text2,
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.ON_SURFACE_VARIANT),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f),
                new ComponentKeyService[0]),
            MaterialTextService.button(
                    "menu.modules",
                    "Modules",
                    this.fe5fm4nwbka.modules(),
                    0,
                    MaterialIsLightService.ON_SURFACE_VARIANT,
                    Float.intBitsToFloat(1099956224))
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService.padding(
                                    0.0f, Float.intBitsToFloat(0x41400000)))
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))),
            MaterialTextService.iconButton(
                    "menu.network", "swap_horiz", "FRITZ!Box \u00b7 Reconnect", this::m6jvoseevjdh)
                .props(
                    materialJoinedService ->
                        materialJoinedService.height(
                            LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))),
            MaterialTextService.iconButton(
                    "menu.options", "settings", "Minecraft settings", this.fe5fm4nwbka.options())
                .props(
                    materialJoinedService ->
                        materialJoinedService.height(
                            LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))),
            MaterialTextService.iconButton(
                    "menu.quit", "power_settings_new", "Quit Minecraft", this.fe5fm4nwbka.quit())
                .props(
                    materialJoinedService ->
                        materialJoinedService.height(
                            LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))))
        .key("footer");
  }

  private void updateState2() {
    if (this.sceneComponent4 != null) {
      return;
    }
    this.updateState6();
    this.fru87gio83d.pointerEvents(false);
    this.componentMountService2 =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService8,
                this.screenScreenIdService == null
                    ? new ThemeIsSetService()
                    : this.screenScreenIdService.theme());
    ((LayoutContainerNode)
            ((LayoutContainerNode)
                    this.componentMountService2.width(
                        LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .maxWidth(Float.intBitsToFloat(1136525312)))
        .maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.sceneComponent4 =
        ComponentUnmountService.mountCentered(
            this.materialTerrainTooltipsService,
            this.componentMountService2,
            Float.intBitsToFloat(1098907648),
            this::updateState3);
    this.sceneComponent4.id("menu.tools.layer");
    ((SceneCornerRadiusService) this.sceneComponent4.children().getFirst())
        .backgroundColor(
            ScenePctService.mulAlpha(
                MaterialIsLightService.SURFACE_LOWEST, Float.intBitsToFloat(1047904911)))
        .blur(Float.intBitsToFloat(0x41400000));
    MaterialEnterService.enter(this.componentMountService2, 0.0f, Float.intBitsToFloat(1098907648));
  }

  private ComponentKeyService<?> createComponentKeyService8(
      ComponentThemeService componentThemeService) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.id("menu.tools.sheet"))
                                                .size(
                                                    LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456)),
                                                    LayoutOperationHandler.px(
                                                        Math.min(
                                                            Float.intBitsToFloat(1136525312),
                                                            this.value2
                                                                - Float.intBitsToFloat(
                                                                    0x42000000)))))
                                        .direction(ScenePctService.Direction.COLUMN))
                                .padding(Float.intBitsToFloat(0x41400000)))
                        .gap(Float.intBitsToFloat(0x41000000)))
                .cornerRadius(Float.intBitsToFloat(1105199104))
                .backgroundColor(MaterialIsLightService.SURFACE),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.height(
                                    LayoutOperationHandler.px(Float.intBitsToFloat(0x42400000))))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            MaterialTextService.text(
                    "Make it yours",
                    Float.intBitsToFloat(1102053376),
                    MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
            MaterialTextService.iconButton(
                "menu.tools.close", "close", "Close \u00b7 Esc", this::updateState3)),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                            .minHeight(0.0f))
                                    .scrollable(true))
                            .clip(true))
                    .gap(Float.intBitsToFloat(0x40800000)),
            this.createComponentKeyService9(
                "appearance", "palette", "Appearance & performance", this::updateState4),
            this.createComponentKeyService9(
                "modules", "tune", "ellice modules", this.fe5fm4nwbka.modules()),
            this.createComponentKeyService9(
                "network", "swap_horiz", "Network tools", this::m6jvoseevjdh),
            this.createComponentKeyService9(
                "options", "settings", "Minecraft settings", this.fe5fm4nwbka.options()),
            this.createComponentKeyService9(
                "quit", "power_settings_new", "Quit Minecraft", this.fe5fm4nwbka.quit())));
  }

  private ComponentKeyService<?> createComponentKeyService9(
      String string, String string2, String string3, Runnable runnable) {
    return MaterialTextService.button(
            "menu.tools." + string,
            string3,
            () -> {
              this.updateState3();
              runnable.run();
            },
            MaterialIsLightService.SURFACE_LOW,
            MaterialIsLightService.ON_SURFACE,
            Float.intBitsToFloat(1099956224))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        materialJoinedService.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .height(
                                        LayoutOperationHandler.px(
                                            Float.intBitsToFloat(0x42400000))))
                            .padding(0.0f, Float.intBitsToFloat(0x41400000)))
                    .justify(ScenePctService.Justify.START))
        .children(
            MaterialTextService.icon(string2, MaterialIsLightService.ON_SURFACE_VARIANT),
            MaterialTextService.label(string3, MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)));
  }

  private void updateState3() {
    if (this.sceneComponent4 == null) {
      return;
    }
    this.updateState6();
    ComponentUnmountService.unmount(this.materialTerrainTooltipsService, this.sceneComponent4);
    this.sceneComponent4 = null;
    this.componentMountService2 = null;
    if (this.fru87gio83d != null) {
      this.fru87gio83d.pointerEvents(true);
    }
  }

  private void updateState4() {
    if (this.screenScreenIdService != null) {
      this.screenScreenIdService.open("client-settings");
    }
  }

  private void m6jvoseevjdh() {
    if (this.screenScreenIdService != null) {
      this.screenScreenIdService.open("fritz-box");
    }
  }

  private ComponentKeyService<?> createComponentKeyService10(
      ComponentKeyService<?> componentKeyService) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService) sceneCornerRadiusService.id("menu.page"))
                                .direction(ScenePctService.Direction.COLUMN))
                        .backgroundColor(
                            ScenePctService.mulAlpha(
                                MaterialIsLightService.SURFACE_LOW,
                                Float.intBitsToFloat(1064682127)))
                        .blur(Float.intBitsToFloat(1103101952))
                        .cornerRadius(Float.intBitsToFloat(1105199104))
                        .padding(this.calculateValue()))
                .clip(true),
        componentKeyService.props(
            scenePctService -> ((ScenePctService) scenePctService.flex(1.0f)).minHeight(0.0f)));
  }

  private ComponentKeyService<?> createComponentKeyService11() {
    int n;
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    int n2 = n = this.calculateValue2() >= Float.intBitsToFloat(1149861888) ? 3 : 2;
    if (this.enabled) {
      for (MenuLoaderTracker.World world :
          this.menuLoaderTracker.worlds().stream().limit(n).toList()) {
        arrayList.add(this.mfrvvrsa9yib(world));
      }
      if (arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService17(
                "Room for a new world",
                this.menuLoaderTracker.loading()
                    ? "Finding your saves\u2026"
                    : "Build a place of your own.",
                "Create world",
                this.fe5fm4nwbka.createWorld(),
                "landscape"));
      }
    } else {
      List<Server> list =
          this.m2u7kjcok1b().stream().filter(server -> server.usage().favorite()).toList();
      for (Server server2 :
          (list.isEmpty() ? this.m2u7kjcok1b() : list).stream().limit(n).toList()) {
        arrayList.add(this.mc3hoou073r(server2));
      }
      if (arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService17(
                "Keep good company",
                "Your favourite servers will appear here.",
                "Browse servers",
                () -> this.page(Page.SERVERS),
                "dns"));
      }
    }
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                layoutContainerNode.id(
                                                                    "menu.home.scroll"))
                                                            .gap(
                                                                this.magoo8oxviwc()
                                                                    ? Float.intBitsToFloat(
                                                                        0x41400000)
                                                                    : Float.intBitsToFloat(
                                                                        1105199104)))
                                                    .scrollable(true))
                                            .clip(true))
                                    .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                            .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT))
                    .padding(
                        0.0f,
                        Float.intBitsToFloat(0x40800000),
                        Float.intBitsToFloat(0x41400000),
                        0.0f),
            this.mfq3dcysh44y().props(scenePctService -> scenePctService.flexShrink(0.0f)),
            ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.id("menu.home.libraries"))
                                .gap(Float.intBitsToFloat(0x41400000)))
                        .flexShrink(0.0f),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                layoutContainerNode.gap(Float.intBitsToFloat(0x40800000)))
                            .align(ScenePctService.Align.CENTER),
                    this.createComponentKeyService12("Saved servers", false),
                    this.createComponentKeyService12("Your worlds", true),
                    ComponentBoxService.column(
                        layoutContainerNode ->
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f),
                        new ComponentKeyService[0]),
                    MaterialTextService.iconButton(
                        "menu.shelf.all",
                        "chevron_right",
                        this.enabled ? "All worlds" : "All servers",
                        () -> this.page(this.enabled ? Page.WORLDS : Page.SERVERS))),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                layoutContainerNode.direction(
                                    this.calculateValue2() >= Float.intBitsToFloat(1144913920)
                                        ? ScenePctService.Direction.ROW
                                        : ScenePctService.Direction.COLUMN))
                            .gap(Float.intBitsToFloat(0x41400000)),
                    (ComponentKeyService[])
                        arrayList.stream()
                            .map(
                                componentKeyService ->
                                    componentKeyService.props(
                                        scenePctService ->
                                            ((ScenePctService)
                                                    scenePctService.flex(
                                                        this.calculateValue2()
                                                                >= Float.intBitsToFloat(1144913920)
                                                            ? 1.0f
                                                            : 0.0f))
                                                .minWidth(0.0f)))
                            .toArray(ComponentKeyService[]::new))))
        .onMount(
            layoutContainerNode ->
                ((LayoutContainerNode) layoutContainerNode.scrollY(this.value3))
                    .scrollTarget(this.value3));
  }

  private ComponentKeyService<?> createComponentKeyService12(String string, boolean bl) {
    int n = this.enabled == bl ? 1 : 0;
    return MaterialTextService.button(
            "menu.shelf." + (bl ? "worlds" : "servers"),
            string,
            () -> {
              this.enabled = bl;
              this.updateState();
            },
            n != 0
                ? ScenePctService.mulAlpha(
                    MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(1064346583))
                : 0,
            n != 0 ? MaterialIsLightService.ON_SURFACE : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1101004800))
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                materialJoinedService.padding(
                                    0.0f,
                                    this.checkCondition()
                                        ? Float.intBitsToFloat(1092616192)
                                        : Float.intBitsToFloat(1098907648)))
                            .minWidth(0.0f))
                    .flexShrink(1.0f));
  }

  private ComponentKeyService<?> mfq3dcysh44y() {
    int n;
    boolean bl;
    MenuLoaderTracker.World world =
        this.menuLoaderTracker.worlds().stream()
            .filter(MenuLoaderTracker.World::canPlay)
            .max(Comparator.comparingLong(MenuLoaderTracker.World::lastPlayed))
            .orElse(null);
    Server server2 =
        this.m2u7kjcok1b().stream()
            .filter(server -> server.usage().lastJoined() > 0L)
            .max(Comparator.comparingLong(server -> server.usage().lastJoined()))
            .orElse(null);
    int n2 =
        server2 != null && (world == null || server2.usage().lastJoined() > world.lastPlayed())
            ? 1
            : 0;
    boolean bl2 = bl = world != null || server2 != null;
    String selectedWorldName =
        bl ? (n2 != 0 ? server2.name() : world.name()) : "Create your first world";
    Runnable runnable =
        bl
            ? (n2 != 0
                ? () -> this.fe5fm4nwbka.connect().accept(server2.address(), server2.name())
                : () -> this.fe5fm4nwbka.playWorld().accept(world))
            : this.fe5fm4nwbka.createWorld();
    int n3 = n = this.value2 < Float.intBitsToFloat(1134559232) ? 1 : 0;
    if (n != 0) {
      return this.createComponentKeyService14(selectedWorldName, runnable, bl, true)
          .key("stage-tiny");
    }
    float f =
        this.magoo8oxviwc()
            ? Float.intBitsToFloat(1126957056)
            : (this.value2 >= Float.intBitsToFloat(1143930880)
                ? Float.intBitsToFloat(1136525312)
                : Float.intBitsToFloat(1134559232));
    boolean bl3 = this.calculateValue2() >= Float.intBitsToFloat(1144913920);
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.id("menu.stage"))
                                    .height(LayoutOperationHandler.px(f)))
                            .gap(Float.intBitsToFloat(1103101952)))
                    .align(ScenePctService.Align.CENTER),
            ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        layoutContainerNode.width(
                                                            bl3
                                                                ? LayoutOperationHandler.px(
                                                                    Math.min(
                                                                        Float.intBitsToFloat(
                                                                            0x440C0000),
                                                                        this.calculateValue2()
                                                                            * Float.intBitsToFloat(
                                                                                1057300152)))
                                                                : LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                    .minWidth(0.0f))
                                            .gap(
                                                this.magoo8oxviwc()
                                                    ? Float.intBitsToFloat(0x41400000)
                                                    : Float.intBitsToFloat(1101004800)))
                                    .padding(
                                        0.0f,
                                        this.checkCondition()
                                            ? Float.intBitsToFloat(0x40800000)
                                            : Float.intBitsToFloat(0x41400000)))
                            .flexShrink(0.0f),
                    MaterialTextService.text(
                            "YOUR TIME. YOUR WORLD.",
                            Float.intBitsToFloat(1093664768),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(
                            sceneTextService ->
                                sceneTextService
                                    .letterSpacing(Float.intBitsToFloat(1072064102))
                                    .visible(!this.magoo8oxviwc())),
                    MaterialTextService.text(
                            this.magoo8oxviwc() ? "Ready to play?" : "Make time\nfor play.",
                            this.magoo8oxviwc()
                                ? Float.intBitsToFloat(1106247680)
                                : (bl3
                                    ? Float.intBitsToFloat(1115684864)
                                    : Float.intBitsToFloat(1109917696)),
                            MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService ->
                                ((SceneTextService)
                                        ((SceneTextService)
                                                sceneTextService
                                                    .fontFamily("material-roboto-medium")
                                                    .lineHeight(
                                                        bl3
                                                            ? Float.intBitsToFloat(1116209152)
                                                            : Float.intBitsToFloat(0x42400000))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            bl3
                                                                ? Float.intBitsToFloat(1124597760)
                                                                : Float.intBitsToFloat(
                                                                    1119879168))))
                                            .flexShrink(0.0f))
                                    .letterSpacing(Float.intBitsToFloat(-1073741824))
                                    .wordWrap(true)
                                    .maxLines(2)
                                    .visible(this.value2 >= Float.intBitsToFloat(1137836032))),
                    this.createComponentKeyService14(selectedWorldName, runnable, bl, false),
                    ComponentBoxService.row(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.gap(
                                                Float.intBitsToFloat(0x41000000)))
                                        .align(ScenePctService.Align.CENTER))
                                .visible(!this.magoo8oxviwc()),
                        MaterialTextService.button(
                                "menu.launch.create-world",
                                "New world",
                                this.fe5fm4nwbka.createWorld(),
                                0,
                                MaterialIsLightService.PRIMARY,
                                Float.intBitsToFloat(1101004800))
                            .props(
                                materialJoinedService ->
                                    materialJoinedService.padding(
                                        0.0f, Float.intBitsToFloat(0x41000000)))
                            .children(
                                MaterialTextService.icon("add", MaterialIsLightService.PRIMARY)
                                    .props(
                                        sceneSrcService ->
                                            sceneSrcService.size(
                                                Float.intBitsToFloat(1099956224),
                                                Float.intBitsToFloat(1099956224))),
                                MaterialTextService.label(
                                    "New world", MaterialIsLightService.PRIMARY)),
                        MaterialTextService.button(
                                "menu.hero.servers",
                                "Explore servers",
                                () -> this.page(Page.SERVERS),
                                0,
                                MaterialIsLightService.ON_SURFACE_VARIANT,
                                Float.intBitsToFloat(1101004800))
                            .props(
                                materialJoinedService ->
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    materialJoinedService.padding(
                                                        0.0f, Float.intBitsToFloat(0x41000000)))
                                                .minWidth(0.0f))
                                        .flexShrink(1.0f))))
                .key("stage-copy")
                .onMount(
                    layoutContainerNode -> {
                      layoutContainerNode.translateY(Float.intBitsToFloat(1096810496));
                      MotionAnimateService.animate(
                          layoutContainerNode,
                          MotionColorsContainer.Floats.TRANSLATE_Y,
                          0.0f,
                          (SceneEaseHandler)
                              new SceneEaseHandler.Spring(
                                  Float.intBitsToFloat(1106771968),
                                  Float.intBitsToFloat(1128792064)),
                          Float.intBitsToFloat(1027101164));
                    }),
            ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .visible(bl3),
                    new ComponentKeyService[0])
                .key("sculpture-space"))
        .key("stage");
  }

  private ComponentKeyService<?> createComponentKeyService14(
      String string, Runnable runnable, boolean bl, boolean bl2) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            sceneCornerRadiusService.id(
                                                                "menu.resume"))
                                                        .direction(ScenePctService.Direction.ROW))
                                                .align(ScenePctService.Align.CENTER))
                                        .height(
                                            LayoutOperationHandler.px(
                                                bl2
                                                    ? Float.intBitsToFloat(1118306304)
                                                    : Float.intBitsToFloat(1118568448))))
                                .padding(
                                    Float.intBitsToFloat(0x41400000),
                                    Float.intBitsToFloat(0x41400000),
                                    Float.intBitsToFloat(0x41400000),
                                    Float.intBitsToFloat(1099956224)))
                        .gap(Float.intBitsToFloat(0x41400000)))
                .cornerRadius(Float.intBitsToFloat(1104150528))
                .backgroundColor(
                    ScenePctService.mulAlpha(
                        MaterialIsLightService.SURFACE_CONTAINER, Float.intBitsToFloat(0x3F666666)))
                .blur(Float.intBitsToFloat(1101004800)),
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                    .gap(Float.intBitsToFloat(0x40800000)),
            MaterialTextService.text(
                    bl ? "CONTINUE PLAYING" : "A FRESH START",
                    Float.intBitsToFloat(1092616192),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(sceneTextService -> sceneTextService.letterSpacing(1.0f)),
            MaterialTextService.text(
                    string,
                    bl2 ? Float.intBitsToFloat(1099956224) : Float.intBitsToFloat(1101004800),
                    MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.id("menu.resume.title"))
                            .fontFamily("material-roboto-medium"))),
        MaterialTextService.button(
                "menu.resume.play",
                bl ? "Play" : "Create",
                runnable,
                MaterialIsLightService.PRIMARY,
                MaterialIsLightService.ON_PRIMARY,
                Float.intBitsToFloat(1103101952))
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            materialJoinedService.padding(
                                                0.0f,
                                                bl2
                                                    ? Float.intBitsToFloat(0x41400000)
                                                    : Float.intBitsToFloat(1098907648)))
                                        .minWidth(0.0f))
                                .tooltip(
                                    (bl ? "Continue playing \u00b7 " : "Create \u00b7 ") + string))
                        .onHoverChange(
                            hovered -> {
                              if (this.sceneFocusService != null) {
                                this.sceneFocusService.focus(hovered);
                              }
                            }))
            .children(
                MaterialTextService.icon("play_arrow", MaterialIsLightService.ON_PRIMARY)
                    .props(
                        sceneSrcService ->
                            ((SceneSrcService)
                                    sceneSrcService.size(
                                        Float.intBitsToFloat(1101004800),
                                        Float.intBitsToFloat(1101004800)))
                                .visible(!bl2)),
                MaterialTextService.label(
                    bl ? "Play" : "Create", MaterialIsLightService.ON_PRIMARY)));
  }

  private ComponentKeyService<?> mc3hoou073r(Server server) {
    int n;
    ServerViewTracker.View view = this.serverViewTracker.view(server.address());
    int n2 = n = view.reachable() && view.status() != null ? 1 : 0;
    String string =
        view.checkedAt() == 0L
            ? "Checking status\u2026"
            : (n != 0
                ? (view.status().players() < 0
                        ? "Online"
                        : String.format(Locale.ROOT, "%,d online", view.status().players()))
                    + (String)
                        (view.status().latencyMs() < 0L
                            ? ""
                            : " \u00b7 " + view.status().latencyMs() + " ms")
                : "Status unavailable");
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        sceneCornerRadiusService.id(
                                                            "menu.server." + server.address()))
                                                    .direction(ScenePctService.Direction.ROW))
                                            .align(ScenePctService.Align.CENTER))
                                    .gap(Float.intBitsToFloat(0x40800000)))
                            .backgroundColor(
                                ScenePctService.mulAlpha(
                                    MaterialIsLightService.SURFACE_CONTAINER,
                                    Float.intBitsToFloat(1064011039)))
                            .blur(Float.intBitsToFloat(1099956224))
                            .cornerRadius(Float.intBitsToFloat(1102053376))
                            .padding(Float.intBitsToFloat(0x40800000)))
                    .height(LayoutOperationHandler.px(Float.intBitsToFloat(1117257728))),
            ComponentBoxService.node(
                "home-server-select",
                MaterialJoinedService::new,
                materialJoinedService -> {
                  materialJoinedService
                      .colors(0, MaterialIsLightService.ON_SURFACE)
                      .shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1116209152));
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
                                                                                          "menu.server.details."
                                                                                              + server
                                                                                                  .address()))
                                                                              .height(
                                                                                  LayoutOperationHandler
                                                                                      .px(
                                                                                          Float
                                                                                              .intBitsToFloat(
                                                                                                  1116209152))))
                                                                      .flex(1.0f))
                                                              .minWidth(0.0f))
                                                      .padding(Float.intBitsToFloat(0x41000000)))
                                              .gap(Float.intBitsToFloat(0x41400000)))
                                      .direction(ScenePctService.Direction.ROW))
                              .align(ScenePctService.Align.CENTER))
                      .onClick(
                          () -> this.fe5fm4nwbka.preview().accept(server.address(), server.name()));
                },
                ComponentBoxService.node(
                    "home-server-icon",
                    SceneImageService::new,
                    sceneImageService ->
                        ((SceneImageService)
                                ((SceneImageService)
                                        sceneImageService
                                            .identity(server.address(), server.name())
                                            .image(this.serverViewTracker.icon(server.address()))
                                            .material(true)
                                            .size(
                                                Float.intBitsToFloat(0x42400000),
                                                Float.intBitsToFloat(0x42400000)))
                                    .flexShrink(0.0f))
                            .pointerEvents(false),
                    new ComponentKeyService[0]),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x40400000)),
                    MaterialTextService.text(
                        server.name(),
                        Float.intBitsToFloat(1098907648),
                        MaterialIsLightService.ON_SURFACE),
                    MaterialTextService.text(
                        string,
                        Float.intBitsToFloat(0x41400000),
                        n != 0
                            ? MaterialIsLightService.TERTIARY
                            : MaterialIsLightService.ON_SURFACE_VARIANT))),
            MaterialTextService.iconButton(
                    "menu.server.join." + server.address(),
                    "play_arrow",
                    "Join " + server.name(),
                    () -> this.fe5fm4nwbka.connect().accept(server.address(), server.name()))
                .props(
                    materialJoinedService ->
                        materialJoinedService.marginRight(Float.intBitsToFloat(0x40800000))))
        .key("home-server:" + server.address());
  }

  private ComponentKeyService<?> mfrvvrsa9yib(MenuLoaderTracker.World world) {
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                sceneCornerRadiusService.id(
                                                    "menu.world." + world.id()))
                                            .direction(ScenePctService.Direction.ROW))
                                    .align(ScenePctService.Align.CENTER))
                            .backgroundColor(
                                ScenePctService.mulAlpha(
                                    MaterialIsLightService.SURFACE_CONTAINER,
                                    Float.intBitsToFloat(1064011039)))
                            .blur(Float.intBitsToFloat(1099956224))
                            .cornerRadius(Float.intBitsToFloat(1102053376))
                            .padding(Float.intBitsToFloat(0x40800000)))
                    .height(LayoutOperationHandler.px(Float.intBitsToFloat(1117257728))),
            ComponentBoxService.node(
                "home-world-select",
                MaterialJoinedService::new,
                materialJoinedService -> {
                  materialJoinedService
                      .colors(0, MaterialIsLightService.ON_SURFACE)
                      .shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1116209152));
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
                                                                                          "menu.world.details."
                                                                                              + world
                                                                                                  .id()))
                                                                              .height(
                                                                                  LayoutOperationHandler
                                                                                      .px(
                                                                                          Float
                                                                                              .intBitsToFloat(
                                                                                                  1116209152))))
                                                                      .flex(1.0f))
                                                              .minWidth(0.0f))
                                                      .padding(Float.intBitsToFloat(0x41000000)))
                                              .gap(Float.intBitsToFloat(0x41400000)))
                                      .direction(ScenePctService.Direction.ROW))
                              .align(ScenePctService.Align.CENTER))
                      .onClick(
                          () -> {
                            this.renderer2.select(world.id());
                            this.page(Page.WORLDS);
                          });
                },
                WorldListRenderer.thumbnail(world, Float.intBitsToFloat(0x42400000)),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x40400000)),
                    MaterialTextService.text(
                        world.name(),
                        Float.intBitsToFloat(1098907648),
                        MaterialIsLightService.ON_SURFACE),
                    MaterialTextService.text(
                        (world.hardcore() ? "Hardcore" : world.mode())
                            + " \u00b7 "
                            + WorldListRenderer.lastPlayed(world.lastPlayed()),
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT))),
            MaterialTextService.iconButton(
                    "menu.world.play." + world.id(),
                    "play_arrow",
                    world.playLabel(),
                    () -> {
                      if (world.canPlay()) {
                        this.fe5fm4nwbka.playWorld().accept(world);
                      }
                    })
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService
                                    .available(world.canPlay())
                                    .marginRight(Float.intBitsToFloat(0x40800000)))
                            .tooltip(world.canPlay() ? world.playLabel() : world.info())))
        .key("home-world:" + world.id());
  }

  private ComponentKeyService<?> createComponentKeyService17(
      String string, String string2, String string3, Runnable runnable, String string4) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN))
                        .backgroundColor(
                            ScenePctService.mulAlpha(
                                MaterialIsLightService.SURFACE_CONTAINER,
                                Float.intBitsToFloat(1064346583)))
                        .blur(Float.intBitsToFloat(1099956224))
                        .cornerRadius(Float.intBitsToFloat(1103101952))
                        .padding(Float.intBitsToFloat(1101004800)))
                .gap(Float.intBitsToFloat(1092616192)),
        MaterialTextService.icon(string4, MaterialIsLightService.PRIMARY),
        MaterialTextService.text(
                string, Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE)
            .props(sceneTextService -> sceneTextService.wordWrap(true)),
        MaterialTextService.text(
                string2,
                Float.intBitsToFloat(1096810496),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(sceneTextService -> sceneTextService.wordWrap(true)),
        MaterialTextService.button("menu.empty." + string4, string3, runnable, false));
  }

  private List<Server> m2u7kjcok1b() {
    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
    for (CompatSavedService.Saved object : this.serverViewTracker.saved()) {
      linkedHashMap.put(object.address(), object.name());
    }
    for (String string : this.serverViewTracker.library().history(this.session.uuid()).keySet()) {
      linkedHashMap.putIfAbsent(string, this.serverViewTracker.library().name(string));
    }
    return linkedHashMap.entrySet().stream()
        .map(
            entry ->
                new Server(
                    entry.getKey(),
                    entry.getValue(),
                    this.serverViewTracker.library().usage(this.session.uuid(), entry.getKey())))
        .sorted(
            Comparator.<Server, Boolean>comparing(server -> !server.usage().favorite())
                .thenComparing(
                    Comparator.comparingLong((Server server) -> server.usage().lastJoined())
                        .reversed())
                .thenComparing(Server::name))
        .toList();
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    if (this.builtinStateController != null) {
      this.builtinStateController.tick();
    }
    if (screenScreenIdService != null) {
      DisplayMetrics displayMetrics = CoreIsInitializedHandler.get().viewport();
      this.viewport(displayMetrics.width(), displayMetrics.height());
    }
    Session session = this.supplier.get();
    if (!session.equals(this.session)) {
      this.session = session;
      this.updateState();
    }
    this.menuLoaderTracker.tick();
    if (screenScreenIdService != null) {
      this.renderer.tick(this.fukeosqhtbl == Page.SERVERS);
      long l = System.currentTimeMillis();
      if (this.fukeosqhtbl == Page.HOME && l >= this.timestamp3) {
        this.timestamp3 = l + 250L;
        for (Server server : this.m2u7kjcok1b().stream().limit(3L).toList()) {
          this.serverViewTracker.seedIcon(server.address(), null);
          this.serverViewTracker.request(server.address(), false);
        }
      }
    }
    if (this.timestamp != this.serverViewTracker.revision()
        || this.timestamp2 != this.menuLoaderTracker.revision()) {
      this.timestamp = this.serverViewTracker.revision();
      this.timestamp2 = this.menuLoaderTracker.revision();
      this.updateState();
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
    MaterialJoinedService materialJoinedService;
    Object object;
    if (n == 256) {
      if (this.builtinStateController != null && this.builtinStateController.isOpen()) {
        this.builtinStateController.close();
        return true;
      }
      if (this.sceneComponent4 != null) {
        this.updateState3();
      } else {
        this.page(Page.HOME);
      }
      return true;
    }
    if (this.sceneComponent4 == null && n == 70 && (n2 & 0xA) != 0) {
      ScenePctService<?> scenePctService2;
      if (this.fukeosqhtbl == Page.HOME) {
        this.page(Page.SERVERS);
      }
      if ((scenePctService2 =
              this.materialTerrainTooltipsService.findById(
                  this.fukeosqhtbl == Page.SERVERS ? "servers.search" : "worlds.search"))
          instanceof ControlLetterSpacingService) {
        ControlLetterSpacingService controlLetterSpacingService =
            (ControlLetterSpacingService) scenePctService2;
        this.updateState6();
        BuiltinActionsTracker.updateState7(controlLetterSpacingService);
        controlLetterSpacingService.selectAll();
        BuiltinActionsTracker.updateState9(controlLetterSpacingService);
      }
      return true;
    }
    if (n == 258) {
      ArrayList<ScenePctService<?>> arrayList = new ArrayList<>();
      BuiltinActionsTracker.updateState8(
          this.sceneComponent4 == null ? this.materialTerrainTooltipsService : this.sceneComponent4,
          arrayList);
      if (!arrayList.isEmpty()) {
        ScenePctService scenePctService3;
        ScenePctService<?> scenePctService4 = this.scenePctService;
        for (ScenePctService<?> scenePctService5 :
            (Iterable<ScenePctService<?>>) (Iterable<?>) (arrayList)) {
          if (!(scenePctService5 instanceof ControlLetterSpacingService)
              || !((ControlLetterSpacingService)
                      (scenePctService3 = (ControlLetterSpacingService) scenePctService5))
                  .focused()) continue;
          scenePctService4 = scenePctService5;
        }
        int n3 = arrayList.indexOf(scenePctService4);
        int n4 = (n2 & 1) != 0 ? 1 : 0;
        this.updateState6();
        ScenePctService<?> scenePctService6 =
            this.scenePctService =
                arrayList.get(
                    n3 < 0
                        ? (n4 != 0 ? arrayList.size() - 1 : 0)
                        : Math.floorMod(n3 + (n4 != 0 ? -1 : 1), arrayList.size()));
        if (scenePctService6 instanceof MaterialJoinedService) {
          scenePctService3 = (MaterialJoinedService) scenePctService6;
          ((MaterialJoinedService) scenePctService3).keyboardFocused(true);
        }
        if ((scenePctService6 = this.scenePctService) instanceof ControlLetterSpacingService) {
          scenePctService3 = (ControlLetterSpacingService) scenePctService6;
          BuiltinActionsTracker.updateState7((ControlLetterSpacingService) scenePctService3);
        }
        BuiltinActionsTracker.updateState9(this.scenePctService);
      }
      return true;
    }
    if ((n == 257 || n == 32)
        && (object = this.scenePctService) instanceof MaterialJoinedService
        && (materialJoinedService = (MaterialJoinedService) object).available()) {
      ArrayList<ScenePctService<?>> focusableNodes = new ArrayList<>();
      BuiltinActionsTracker.updateState8(
          this.sceneComponent4 == null ? this.materialTerrainTooltipsService : this.sceneComponent4,
          focusableNodes);
      if (focusableNodes.stream()
          .anyMatch(
              scenePctService -> {
                ControlLetterSpacingService controlLetterSpacingService;
                return (scenePctService instanceof ControlLetterSpacingService
                            && (controlLetterSpacingService =
                                    (ControlLetterSpacingService) scenePctService)
                                .focused()
                        ? 1
                        : 0)
                    != 0;
              })) {
        return false;
      }
      if (focusableNodes.contains(materialJoinedService)) {
        materialJoinedService.activate();
        return true;
      }
    }
    return false;
  }

  private void updateState6() {
    Object object;
    ScenePctService<?> scenePctService = this.scenePctService;
    if (scenePctService instanceof MaterialJoinedService) {
      object = (MaterialJoinedService) scenePctService;
      ((MaterialJoinedService) object).keyboardFocused(false);
    }
    this.scenePctService = null;
    ArrayList<ScenePctService<?>> focusableNodes = new ArrayList<>();
    BuiltinActionsTracker.updateState8(
        this.sceneComponent4 == null ? this.materialTerrainTooltipsService : this.sceneComponent4,
        focusableNodes);
    for (ScenePctService<?> scenePctService2 : focusableNodes) {
      if (!(scenePctService2 instanceof ControlLetterSpacingService)) continue;
      ControlLetterSpacingService controlLetterSpacingService =
          (ControlLetterSpacingService) scenePctService2;
      controlLetterSpacingService.unfocus();
    }
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
      CoreIsInitializedHandler.get().scene().clearFocus();
    }
  }

  private static void updateState7(ControlLetterSpacingService controlLetterSpacingService) {
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
      CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacingService);
    } else {
      controlLetterSpacingService.focus();
    }
  }

  private static void updateState8(
      ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
    Object object;
    if (scenePctService == null || !scenePctService.visible || !scenePctService.pointerEvents()) {
      return;
    }
    if (scenePctService instanceof MaterialJoinedService
            && ((MaterialJoinedService) (object = (MaterialJoinedService) scenePctService))
                .available()
        || scenePctService instanceof ControlLetterSpacingService) {
      list.add(scenePctService);
    }
    for (ScenePctService scenePctService2 : scenePctService.children()) {
      BuiltinActionsTracker.updateState8(scenePctService2, list);
    }
  }

  private static void updateState9(ScenePctService<?> scenePctService) {
    for (ScenePctService<?> scenePctService2 = scenePctService.parent();
        scenePctService2 != null;
        scenePctService2 = scenePctService2.parent()) {
      if (scenePctService2.scrollMin() >= 0.0f) continue;
      float scrollDelta =
          scenePctService.computedY()
                  < scenePctService2.computedY() + Float.intBitsToFloat(0x40C00000)
              ? scenePctService2.computedY()
                  + Float.intBitsToFloat(0x40C00000)
                  - scenePctService.computedY()
              : (scenePctService.computedY() + scenePctService.computedH()
                      > scenePctService2.computedY()
                          + scenePctService2.computedH()
                          - Float.intBitsToFloat(0x40C00000)
                  ? scenePctService2.computedY()
                      + scenePctService2.computedH()
                      - Float.intBitsToFloat(0x40C00000)
                      - scenePctService.computedY()
                      - scenePctService.computedH()
                  : 0.0f);
      if (scrollDelta == 0.0f) continue;
      float f3 =
          Math.max(
              scenePctService2.scrollMin(),
              Math.min(0.0f, scenePctService2.scrollY() + scrollDelta));
      scenePctService2.scrollTarget(f3);
      MotionAnimateService.animate(
          scenePctService2,
          MotionColorsContainer.Floats.SCROLL_Y,
          f3,
          (SceneEaseHandler) MaterialIsLightService.FAST_SPATIAL);
    }
  }

  public static enum Page {
    HOME,
    SERVERS,
    WORLDS;
  }

  public record Actions(
      BiConsumer<String, String> connect,
      BiConsumer<String, String> preview,
      Consumer<MenuLoaderTracker.World> playWorld,
      Consumer<MenuLoaderTracker.World> editWorld,
      Runnable createWorld,
      Runnable accounts,
      Runnable options,
      Runnable modules,
      Runnable changelog,
      Runnable quit) {}

  public record Session(UUID uuid, String name) {}

  private record Server(String address, String name, ServerKeyService.Usage usage) {}
}
