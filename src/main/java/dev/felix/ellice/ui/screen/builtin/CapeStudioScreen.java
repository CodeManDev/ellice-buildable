package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.feature.cape.CapeHttpsService;
import dev.felix.ellice.feature.cape.CapePreviewRenderer;
import dev.felix.ellice.feature.cape.CapeRecoveredFactory;
import dev.felix.ellice.feature.cape.CapeRevisionService;
import dev.felix.ellice.feature.cape.CapeSearchSelector;
import dev.felix.ellice.feature.cape.CapeSizeService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.imageio.ImageIO;

public final class CapeStudioScreen implements ScreenOperationHandler {
  private final CapeRevisionService capeRevisionService;
  private final Runnable runnable;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private ScreenScreenIdService screenScreenIdService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private float value = 1120.0F;
  private float value2 = 720.0F;
  private long timestamp = -1L;
  private int count;
  private int count2;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private boolean enabled5;
  private CapeSearchSelector.Provider provider = CapeSearchSelector.Provider.COMMONS;
  private String text2 = "";
  private String text3 = "";
  private String text4 = "";
  private String text5 = "";
  private String text6 = "";
  private Path path2;
  private List<Path> path3 = List.of();
  private Set<Path> path4 = Set.of();
  private int count3;

  public CapeStudioScreen(CapeRevisionService capeRevision, Runnable currentRunnable) {
    this.capeRevisionService = capeRevision;
    this.runnable = currentRunnable;
  }

  @Override
  public String id() {
    return "cape-studio";
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
  public boolean closesOnEscape() {
    return false;
  }

  @Override
  public float backgroundDesaturation() {
    return 0.0F;
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.NONE;
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
    this.screenScreenIdService = screenScreenId;
    this.enabled5 = false;
    this.materialTerrainTooltipsService =
        new MaterialTerrainTooltipsService().terrainTooltips(true);
    this.materialTerrainTooltipsService
        .id("cape.root")
        .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
        .backgroundColor(1879048192);
    this.componentMountService =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService,
                screenScreenId == null ? new ThemeIsSetService() : screenScreenId.theme());
    this.componentMountService
        .absolute()
        .inset(LayoutOperationHandler.px(0.0F))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.materialTerrainTooltipsService.addChild(this.componentMountService);
    return this.materialTerrainTooltipsService;
  }

  public void viewport(float x, float y) {
    if (this.value != x || this.value2 != y) {
      this.value = x;
      this.value2 = y;
      this.updateState();
    }
  }

  private boolean checkCondition() {
    return this.value < 780.0F;
  }

  private boolean checkCondition2() {
    return this.value2 < 480.0F;
  }

  private void updateState() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState2(Runnable runnable) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    runnable.run();
    this.updateState();
  }

  private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
    ArrayList arrayList = new ArrayList();
    if (!this.checkCondition() || !this.enabled) {
      arrayList.add(this.createComponentKeyService3());
    }

    if (!this.checkCondition() || this.enabled) {
      arrayList.add(this.createComponentKeyService7());
    }

    ComponentKeyService<?> currentSize =
        ComponentBoxService.column(
            item ->
                item.size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.percent(100.0F))
                    .padding(this.value < 600.0F ? 0.0F : 16.0F)
                    .align(ScenePctService.Align.CENTER)
                    .justify(ScenePctService.Justify.CENTER)
                    .pointerEvents(!this.enabled3 && !this.enabled4),
            ComponentBoxService.panel(
                    item ->
                        item.id("cape.workspace")
                            .size(
                                LayoutOperationHandler.percent(100.0F),
                                LayoutOperationHandler.percent(100.0F))
                            .maxWidth(1180.0F)
                            .maxHeight(860.0F)
                            .backgroundColor(MaterialIsLightService.SURFACE)
                            .cornerRadius(this.value < 600.0F ? 0.0F : 28.0F)
                            .direction(ScenePctService.Direction.COLUMN)
                            .clip(true),
                    this.createComponentKeyService2(),
                    ComponentBoxService.row(
                        item ->
                            item.height(LayoutOperationHandler.px(44.0F))
                                .padding(0.0F, 12.0F)
                                .gap(8.0F)
                                .flexShrink(0.0F)
                                .visible(this.checkCondition()),
                        this.createComponentKeyService17(
                            "browse", "Browse", !this.enabled, () -> this.enabled = false),
                        this.createComponentKeyService17(
                            "preview", "3D preview", this.enabled, () -> this.enabled = true)),
                    ComponentBoxService.row(
                        item ->
                            item.id("cape.panes")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .flex(1.0F)
                                .minHeight(0.0F)
                                .padding(0.0F, this.checkCondition() ? 12.0F : 20.0F)
                                .gap(16.0F)
                                .align(ScenePctService.Align.STRETCH),
                        arrayList.toArray(ComponentKeyService[]::new)),
                    this.createComponentKeyService14())
                .key("workspace")
                .onMount(item -> MaterialEnterService.enter(item, 0.0F, 16.0F)));
    return ComponentBoxService.stack(
        item ->
            item.size(
                LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)),
        currentSize,
        this.enabled3
            ? this.createComponentKeyService21()
            : ComponentBoxService.box(item -> item.visible(false)).key("connections"),
        this.enabled4
            ? this.createComponentKeyService23()
            : ComponentBoxService.box(item -> item.visible(false)).key("browser"));
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    return ComponentBoxService.row(
        item ->
            item.height(LayoutOperationHandler.px(this.checkCondition2() ? 52.0F : 76.0F))
                .padding(0.0F, this.checkCondition() ? 8.0F : 12.0F)
                .gap(8.0F)
                .align(ScenePctService.Align.CENTER)
                .flexShrink(0.0F),
        MaterialTextService.iconButton(
            "cape.back", "arrow_back", "Back to ClickGUI", this::updateState8),
        ComponentBoxService.column(
            item -> item.flex(1.0F).minWidth(0.0F),
            MaterialTextService.text(
                "Cape Studio",
                this.checkCondition() ? 24.0F : 28.0F,
                MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                    "A little more you.", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.visible(!this.checkCondition2()))),
        MaterialTextService.text("LOCAL CAPE", 10.0F, MaterialIsLightService.PRIMARY)
            .props(item -> item.margin(0.0F, 8.0F).visible(this.value > 850.0F)),
        MaterialTextService.iconButton(
            "cape.connections",
            "settings",
            "GIF service connections",
            () ->
                this.updateState2(
                    () -> {
                      this.enabled3 = true;
                      this.text4 = this.capeRevisionService.key();
                    })));
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    return ComponentBoxService.column(
            item ->
                item.id("cape.library")
                    .width(
                        this.checkCondition()
                            ? LayoutOperationHandler.percent(100.0F)
                            : LayoutOperationHandler.px(this.value < 1000.0F ? 304.0F : 370.0F))
                    .flex(this.checkCondition() ? 1.0F : 0.0F)
                    .minWidth(0.0F)
                    .minHeight(0.0F)
                    .gap(12.0F)
                    .flexShrink(0.0F),
            ComponentBoxService.row(
                item -> item.gap(4.0F).flexShrink(0.0F),
                this.createComponentKeyService17(
                    "collection", "Collection", this.count == 0, () -> this.count = 0),
                this.createComponentKeyService17(
                    "gifs",
                    "GIFs",
                    this.count == 1,
                    () -> {
                      this.count = 1;
                      if (this.capeRevisionService.results().isEmpty()
                          && !this.capeRevisionService.searching()) {
                        this.updateState6(0);
                      }
                    }),
                this.createComponentKeyService17(
                    "import", "Import", this.count == 2, () -> this.count = 2)),
            this.count == 0
                ? this.createComponentKeyService4()
                : (this.count == 1
                    ? this.createComponentKeyService5()
                    : this.createComponentKeyService6()))
        .key("library");
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    ArrayList arrayList = new ArrayList();

    for (String currentText : CapeRecoveredFactory.NAMES) {
      CapeSizeService capeSize = this.capeRevisionService.preset(currentText);
      arrayList.add(
          this.createComponentKeyService15(
              "preset." + currentText,
              currentText,
              "ellice collection",
              () -> capeSize.frame(capeSize.frameAt(System.nanoTime() / 1000000.0)),
              this.capeRevisionService.selected().source().kind().equals("preset")
                  && this.capeRevisionService.selected().source().reference().equals(currentText),
              () -> {
                this.capeRevisionService.choosePreset(currentText);
                this.enabled = true;
              }));
    }

    return ComponentBoxService.column(
            item ->
                item.flex(1.0F)
                    .minHeight(0.0F)
                    .gap(12.0F)
                    .scrollable(true)
                    .scrollbarWidth(3.0F)
                    .scrollbarColor(MaterialIsLightService.OUTLINE)
                    .clip(true)
                    .padding(0.0F, 4.0F, 8.0F, 0.0F),
            ComponentBoxService.column(
                item -> item.gap(3.0F).flexShrink(0.0F),
                MaterialTextService.text("Made to move", 20.0F, MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                        "Original animated capes. Always available.",
                        12.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.wordWrap(true))),
            this.createComponentKeyService16(arrayList, 2),
            MaterialTextService.button(
                    "cape.original",
                    "Use original Minecraft cape",
                    this.capeRevisionService::original,
                    false)
                .props(
                    item -> {
                      item.colors(
                          MaterialIsLightService.SURFACE_HIGH,
                          MaterialIsLightService.ON_SURFACE_VARIANT);
                      item.width(LayoutOperationHandler.percent(100.0F));
                    }))
        .key("collection");
  }

  private ComponentKeyService<?> createComponentKeyService5() {
    ArrayList arrayList = new ArrayList();

    for (CapeSearchSelector.Entry entry : this.capeRevisionService.results()) {
      arrayList.add(
          this.createComponentKeyService15(
              "gif." + entry.id(),
              entry.title(),
              entry.credit(),
              () -> this.capeRevisionService.thumbnail(entry.id()),
              this.capeRevisionService.selected().source().title().equals(entry.title()),
              () -> {
                this.capeRevisionService.choose(entry);
                this.enabled = true;
              }));
    }

    ArrayList currentArrayList = new ArrayList();
    if (this.capeRevisionService.searching()) {
      currentArrayList.add(
          this.createComponentKeyService19("Searching GIFs…", "Your results will appear here."));
    } else if (arrayList.isEmpty()) {
      currentArrayList.add(
          this.createComponentKeyService19(
              this.provider == CapeSearchSelector.Provider.GIPHY
                      && this.capeRevisionService.key().isBlank()
                  ? "Connect GIPHY"
                  : "Find your next cape",
              this.provider == CapeSearchSelector.Provider.GIPHY
                      && this.capeRevisionService.key().isBlank()
                  ? "Add your API key with the settings button, or choose Commons."
                  : "Search a mood, a color, a character or an animation."));
    } else {
      currentArrayList.add(this.createComponentKeyService16(arrayList, 2));
    }

    if (this.capeRevisionService.nextOffset() >= 0) {
      currentArrayList.add(
          MaterialTextService.button(
              "cape.more",
              "Next page",
              () -> this.updateState6(this.capeRevisionService.nextOffset()),
              false));
    }

    if (this.provider == CapeSearchSelector.Provider.COMMONS) {
      currentArrayList.add(
          MaterialTextService.text(
                  "Wikimedia Commons · creator credits on each item",
                  11.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.wordWrap(true).flexShrink(0.0F)));
    }

    return ComponentBoxService.column(
            item -> item.flex(1.0F).minHeight(0.0F).gap(10.0F),
            ComponentBoxService.row(
                item -> item.gap(4.0F).flexShrink(0.0F),
                this.createComponentKeyService17(
                    "commons",
                    "Commons",
                    this.provider == CapeSearchSelector.Provider.COMMONS,
                    () -> {
                      this.provider = CapeSearchSelector.Provider.COMMONS;
                      this.updateState6(0);
                    }),
                this.createComponentKeyService17(
                    "giphy",
                    "GIPHY",
                    this.provider == CapeSearchSelector.Provider.GIPHY,
                    () -> {
                      this.provider = CapeSearchSelector.Provider.GIPHY;
                      this.updateState6(0);
                    })),
            ComponentBoxService.row(
                item -> item.gap(4.0F).flexShrink(0.0F).align(ScenePctService.Align.CENTER),
                MaterialTextService.search(
                        "cape.search",
                        "Find a GIF…",
                        this.text2,
                        item -> {
                          this.text2 = item;
                          this.updateState();
                        },
                        true)
                    .props(item -> item.flex(1.0F).minWidth(0.0F)),
                MaterialTextService.iconButton(
                        "cape.search.go", "search", "Search GIFs", () -> this.updateState6(0))
                    .props(item -> item.size(40.0F, 40.0F))),
            this.provider == CapeSearchSelector.Provider.GIPHY
                ? ComponentBoxService.<BuiltinImageService>node(
                        "giphy-attribution",
                        BuiltinImageService::new,
                        item ->
                            item.size(160.0F, 22.0F)
                                .flexShrink(0.0F)
                                .rounding(0.0F)
                                .image(() -> CapeStudioScreen.Attribution.IMAGE))
                    .onUnmount(BuiltinImageService::close)
                : ComponentBoxService.box(item -> item.visible(false)),
            ComponentBoxService.column(
                item ->
                    item.flex(1.0F)
                        .minHeight(0.0F)
                        .gap(12.0F)
                        .scrollable(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE)
                        .clip(true)
                        .padding(0.0F, 4.0F, 8.0F, 0.0F),
                currentArrayList.toArray(ComponentKeyService[]::new)))
        .key("discovery");
  }

  private ComponentKeyService<?> createComponentKeyService6() {
    return ComponentBoxService.column(
            item ->
                item.flex(1.0F)
                    .minHeight(0.0F)
                    .gap(14.0F)
                    .scrollable(true)
                    .clip(true)
                    .padding(0.0F, 4.0F, 12.0F, 0.0F),
            MaterialTextService.text("Bring your own", 20.0F, MaterialIsLightService.ON_SURFACE)
                .props(item -> item.flexShrink(0.0F)),
            MaterialTextService.text(
                    "Animated GIF, PNG or JPG. Minecraft cape textures work too.",
                    13.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.wordWrap(true).flexShrink(0.0F)),
            MaterialTextService.button(
                    "cape.files",
                    "Choose an image file",
                    () -> this.updateState5(Path.of(System.getProperty("user.home"), "Downloads")),
                    true)
                .children(
                    MaterialTextService.icon("folder", MaterialIsLightService.ON_PRIMARY),
                    MaterialTextService.label(
                        "Choose an image file", MaterialIsLightService.ON_PRIMARY))
                .props(item -> item.width(LayoutOperationHandler.percent(100.0F)).flexShrink(0.0F)),
            MaterialTextService.divider(),
            MaterialTextService.label("Or paste an image link", MaterialIsLightService.ON_SURFACE),
            this.createComponentKeyService20(
                "cape.url",
                "https://…",
                this.text3,
                2048,
                item -> {
                  this.text3 = item;
                  this.updateState();
                },
                false),
            MaterialTextService.button(
                    "cape.url.load",
                    "Preview link",
                    () -> {
                      this.capeRevisionService.importLink(this.text3);
                      this.enabled = true;
                      this.updateState();
                    },
                    false)
                .props(
                    item ->
                        item.available(!this.text3.isBlank() && !this.capeRevisionService.loading())
                            .flexShrink(0.0F)),
            MaterialTextService.text(
                    "Up to 16 MB · 240 frames. GIFs loop at their original frame timing.",
                    12.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.wordWrap(true).flexShrink(0.0F)),
            ComponentBoxService.panel(
                item ->
                    item.backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                        .cornerRadius(18.0F)
                        .padding(14.0F)
                        .gap(6.0F)
                        .direction(ScenePctService.Direction.COLUMN)
                        .flexShrink(0.0F),
                MaterialTextService.label(
                    "Your cape, on your client", MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                        "The cape is visible locally. Your Minecraft account and other players’"
                            + " capes stay as they are.",
                        12.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.wordWrap(true))))
        .key("imports");
  }

  private ComponentKeyService<?> createComponentKeyService7() {
    int currentValue = this.value2 < 600.0F && this.value >= 600.0F ? 1 : 0;
    return currentValue != 0
        ? ComponentBoxService.panel(
                item ->
                    item.id("cape.preview.pane")
                        .flex(1.0F)
                        .minWidth(0.0F)
                        .minHeight(0.0F)
                        .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                        .cornerRadius(24.0F)
                        .direction(ScenePctService.Direction.ROW)
                        .padding(12.0F)
                        .gap(12.0F)
                        .clip(true),
                ComponentBoxService.column(
                    item -> item.flex(1.0F).minWidth(0.0F).minHeight(0.0F).gap(4.0F),
                    MaterialTextService.text(
                            this.capeRevisionService.selected().source().title(),
                            18.0F,
                            MaterialIsLightService.ON_SURFACE)
                        .props(item -> item.flexShrink(0.0F)),
                    this.createComponentKeyService8()),
                ComponentBoxService.column(
                    item ->
                        item.width(LayoutOperationHandler.px(204.0F))
                            .flexShrink(0.0F)
                            .minHeight(0.0F)
                            .gap(6.0F)
                            .scrollable(true)
                            .scrollbarWidth(3.0F)
                            .scrollbarColor(MaterialIsLightService.OUTLINE)
                            .clip(true),
                    this.createComponentKeyService10(true),
                    MaterialTextService.divider(),
                    this.createComponentKeyService11(),
                    this.createComponentKeyService13(),
                    MaterialTextService.button(
                            "cape.source",
                            "View source",
                            () -> updateState7(this.capeRevisionService.selected().source().page()),
                            false)
                        .props(
                            item ->
                                item.flexShrink(0.0F)
                                    .visible(
                                        !this.capeRevisionService
                                            .selected()
                                            .source()
                                            .page()
                                            .isBlank()))))
            .key("preview-pane")
        : ComponentBoxService.panel(
                item ->
                    item.id("cape.preview.pane")
                        .flex(1.0F)
                        .minWidth(0.0F)
                        .minHeight(0.0F)
                        .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                        .cornerRadius(24.0F)
                        .direction(ScenePctService.Direction.COLUMN)
                        .padding(this.checkCondition() ? 12.0F : 18.0F)
                        .gap(8.0F)
                        .clip(true),
                this.createComponentKeyService9(),
                this.createComponentKeyService8(),
                this.createComponentKeyService10(false),
                MaterialTextService.text(
                        "Drag to rotate · scroll to zoom", 11.0F, MaterialIsLightService.OUTLINE)
                    .props(item -> item.flexShrink(0.0F)),
                MaterialTextService.divider(),
                this.createComponentKeyService11(),
                this.createComponentKeyService13())
            .key("preview-pane");
  }

  private ComponentKeyService<?> createComponentKeyService8() {
    return ComponentBoxService.<BuiltinControllerService>node(
            "cape-model",
            BuiltinControllerService::new,
            item ->
                item.id("cape.model")
                    .width(LayoutOperationHandler.percent(100.0F))
                    .flex(1.0F)
                    .minHeight(40.0F)
                    .controller(this.capeRevisionService)
                    .view(this.count2, this.enabled2))
        .key("model")
        .onUnmount(BuiltinControllerService::close);
  }

  private ComponentKeyService<?> createComponentKeyService9() {
    CapeRevisionService.Loaded loaded = this.capeRevisionService.selected();
    return ComponentBoxService.row(
        item -> item.gap(12.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
        ComponentBoxService.<BuiltinImageService>node(
                "cape-live-image",
                BuiltinImageService::new,
                item ->
                    item.id("cape.live.gif")
                        .size(44.0F, 56.0F)
                        .flexShrink(0.0F)
                        .image(
                            () -> {
                              CapeSizeService capeSize =
                                  this.capeRevisionService.selected().animation();
                              return capeSize.frame(
                                  capeSize.frameAt(
                                      this.capeRevisionService
                                          .playback()
                                          .position(System.nanoTime())));
                            }))
            .onUnmount(BuiltinImageService::close),
        ComponentBoxService.column(
            item -> item.flex(1.0F).minWidth(0.0F),
            MaterialTextService.text(
                loaded.source().title(), 20.0F, MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                loaded.animation().size() > 1
                    ? String.format(
                        Locale.ROOT,
                        "Animated · %.1f s loop",
                        loaded.animation().duration() / 1000.0)
                    : "Still image",
                12.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)),
        MaterialTextService.iconButton(
                "cape.source",
                "open_in_new",
                "View source and creator credits",
                () -> updateState7(loaded.source().page()))
            .props(
                item -> {
                  item.size(36.0F, 36.0F);
                  item.visible(!loaded.source().page().isBlank());
                }));
  }

  private ComponentKeyService<?> createComponentKeyService10(boolean enabled) {
    CapePreviewRenderer.Style currentStyle = this.capeRevisionService.style();
    ComponentKeyService<?> currentSize =
        MaterialTextService.iconButton(
                "cape.play",
                this.capeRevisionService.playback().paused() ? "play_arrow" : "pause",
                this.capeRevisionService.playback().paused() ? "Play animation" : "Pause preview",
                () -> {
                  this.capeRevisionService.togglePlayback();
                  this.updateState();
                })
            .props(item -> item.size(36.0F, 36.0F));
    ComponentKeyService<?> componentKey =
        this.createComponentKeyService18(
            "speed",
            String.format(Locale.ROOT, "%.2fx", currentStyle.speed()).replace(".00", ""),
            false,
            () -> {
              double doubleValue =
                  currentStyle.speed() < 0.75
                      ? 1.0
                      : (currentStyle.speed() < 1.25
                          ? 1.5
                          : (currentStyle.speed() < 1.75 ? 2.0 : 0.5));
              this.updateState3(
                  currentStyle.fit(),
                  currentStyle.minecraftTexture(),
                  currentStyle.mirror(),
                  doubleValue);
            });
    ComponentKeyService<?> currentComponentKey =
        this.createComponentKeyService18("view", "Front / back", false, () -> this.count2++);
    ComponentKeyService<?> nextComponentKey =
        this.createComponentKeyService18(
            "spin",
            this.enabled2 ? "Stop turn" : "Auto turn",
            this.enabled2,
            () -> this.enabled2 = !this.enabled2);
    return enabled
        ? ComponentBoxService.column(
            item -> item.gap(4.0F).flexShrink(0.0F),
            ComponentBoxService.row(
                item -> item.gap(4.0F).align(ScenePctService.Align.CENTER),
                currentSize,
                componentKey,
                ComponentBoxService.box(item -> item.flex(1.0F)),
                ComponentBoxService.<BuiltinImageService>node(
                        "cape-live-image",
                        BuiltinImageService::new,
                        item ->
                            item.id("cape.live.gif")
                                .size(28.0F, 36.0F)
                                .image(
                                    () -> {
                                      CapeSizeService capeSize =
                                          this.capeRevisionService.selected().animation();
                                      return capeSize.frame(
                                          capeSize.frameAt(
                                              this.capeRevisionService
                                                  .playback()
                                                  .position(System.nanoTime())));
                                    }))
                    .onUnmount(BuiltinImageService::close)),
            ComponentBoxService.row(
                item -> item.gap(4.0F),
                currentComponentKey.props(item -> item.flex(1.0F)),
                nextComponentKey.props(item -> item.flex(1.0F))))
        : ComponentBoxService.row(
            item -> item.gap(4.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
            currentSize,
            componentKey,
            ComponentBoxService.box(item -> item.flex(1.0F)),
            currentComponentKey,
            nextComponentKey);
  }

  private ComponentKeyService<?> createComponentKeyService11() {
    CapePreviewRenderer.Style currentStyle = this.capeRevisionService.style();
    int currentValue = this.value2 < 600.0F && this.value >= 600.0F ? 1 : 0;
    return ComponentBoxService.column(
        item -> item.gap(6.0F).flexShrink(0.0F),
        ComponentBoxService.row(
            item -> item.gap(4.0F).flexShrink(0.0F),
            this.createComponentKeyService17(
                "artwork",
                "GIF / artwork",
                !currentStyle.minecraftTexture(),
                () ->
                    this.updateState3(
                        currentStyle.fit(), false, currentStyle.mirror(), currentStyle.speed())),
            this.createComponentKeyService17(
                "texture",
                currentValue != 0 ? "Cape texture" : "Minecraft texture",
                currentStyle.minecraftTexture(),
                () ->
                    this.updateState3(
                        currentStyle.fit(), true, currentStyle.mirror(), currentStyle.speed()))),
        ComponentBoxService.row(
            item -> item.gap(4.0F).flexShrink(0.0F).visible(!currentStyle.minecraftTexture()),
            this.createComponentKeyService17(
                "fill",
                "Fill",
                currentStyle.fit() == CapePreviewRenderer.Fit.FILL,
                () ->
                    this.updateState3(
                        CapePreviewRenderer.Fit.FILL,
                        false,
                        currentStyle.mirror(),
                        currentStyle.speed())),
            this.createComponentKeyService17(
                "fit",
                "Fit",
                currentStyle.fit() == CapePreviewRenderer.Fit.FIT,
                () ->
                    this.updateState3(
                        CapePreviewRenderer.Fit.FIT,
                        false,
                        currentStyle.mirror(),
                        currentStyle.speed())),
            this.createComponentKeyService17(
                "stretch",
                "Stretch",
                currentStyle.fit() == CapePreviewRenderer.Fit.STRETCH,
                () ->
                    this.updateState3(
                        CapePreviewRenderer.Fit.STRETCH,
                        false,
                        currentStyle.mirror(),
                        currentStyle.speed())),
            currentValue != 0
                ? ComponentBoxService.box(item -> item.visible(false))
                : this.createComponentKeyService12()),
        currentValue != 0
            ? this.createComponentKeyService12()
                .props(item -> item.visible(!currentStyle.minecraftTexture()))
            : ComponentBoxService.box(item -> item.visible(false)));
  }

  private ComponentKeyService<?> createComponentKeyService12() {
    CapePreviewRenderer.Style currentStyle = this.capeRevisionService.style();
    return this.createComponentKeyService18(
        "mirror",
        "Mirror",
        currentStyle.mirror(),
        () ->
            this.updateState3(
                currentStyle.fit(), false, !currentStyle.mirror(), currentStyle.speed()));
  }

  private ComponentKeyService<?> createComponentKeyService13() {
    return MaterialTextService.text(
            this.capeRevisionService.style().minecraftTexture()
                ? "Uses the full 64 × 32 Minecraft cape layout."
                : this.capeRevisionService.selected().source().credit(),
            11.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT)
        .props(
            item ->
                item.flexShrink(0.0F)
                    .wordWrap(true)
                    .maxHeight(32.0F)
                    .tooltip(this.capeRevisionService.selected().source().credit()));
  }

  private void updateState3(
      CapePreviewRenderer.Fit fit, boolean enabled, boolean currentEnabled, double doubleValue) {
    this.capeRevisionService.style(
        new CapePreviewRenderer.Style(
            fit,
            enabled,
            currentEnabled,
            this.capeRevisionService.style().background(),
            doubleValue));
    this.updateState();
  }

  private ComponentKeyService<?> createComponentKeyService14() {
    return ComponentBoxService.row(
        item ->
            item.height(LayoutOperationHandler.px(this.checkCondition2() ? 62.0F : 76.0F))
                .padding(8.0F, this.checkCondition() ? 12.0F : 24.0F)
                .gap(12.0F)
                .align(ScenePctService.Align.CENTER)
                .flexShrink(0.0F),
        MaterialTextService.text(
                this.capeRevisionService.message(),
                12.0F,
                this.capeRevisionService.error()
                    ? MaterialIsLightService.ERROR
                    : MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                item ->
                    item.id("cape.status")
                        .flex(1.0F)
                        .minWidth(0.0F)
                        .maxHeight(42.0F)
                        .wordWrap(true)
                        .tooltip(this.capeRevisionService.message())),
        MaterialTextService.button(
                "cape.apply",
                this.capeRevisionService.saving() ? "Saving…" : "Wear cape",
                () -> this.capeRevisionService.apply(this.runnable),
                true)
            .props(
                item -> {
                  item.available(
                      !this.capeRevisionService.loading() && !this.capeRevisionService.saving());
                  item.height(LayoutOperationHandler.px(48.0F));
                })
            .children(
                MaterialTextService.icon("check", MaterialIsLightService.ON_PRIMARY)
                    .props(item -> item.visible(!this.checkCondition())),
                MaterialTextService.label(
                    this.capeRevisionService.saving() ? "Saving…" : "Wear cape",
                    MaterialIsLightService.ON_PRIMARY)));
  }

  private ComponentKeyService<?> createComponentKeyService15(
      String currentText,
      String nextText,
      String previousText,
      Supplier<BufferedImage> supplier,
      boolean enabled,
      Runnable runnable) {
    return MaterialTextService.button(
            "cape." + currentText, nextText, () -> this.updateState2(runnable), false)
        .props(
            item -> {
              item.colors(
                      enabled
                          ? MaterialIsLightService.SECONDARY_CONTAINER
                          : MaterialIsLightService.SURFACE_CONTAINER,
                      MaterialIsLightService.ON_SURFACE)
                  .shape(20.0F, 0.0F);
              item.flex(1.0F)
                  .minWidth(0.0F)
                  .height(LayoutOperationHandler.px(168.0F))
                  .padding(8.0F)
                  .gap(5.0F)
                  .direction(ScenePctService.Direction.COLUMN)
                  .justify(ScenePctService.Justify.START);
            })
        .children(
            ComponentBoxService.panel(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(108.0F))
                        .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                        .cornerRadius(14.0F)
                        .clip(true)
                        .pointerEvents(false),
                ComponentBoxService.<BuiltinImageService>node(
                        "cape-thumbnail",
                        BuiltinImageService::new,
                        item ->
                            item.image(supplier)
                                .size(
                                    LayoutOperationHandler.percent(100.0F),
                                    LayoutOperationHandler.percent(100.0F))
                                .pointerEvents(false))
                    .onUnmount(BuiltinImageService::close)),
            ComponentBoxService.row(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .gap(4.0F)
                        .align(ScenePctService.Align.CENTER),
                MaterialTextService.text(nextText, 12.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.flex(1.0F).minWidth(0.0F)),
                MaterialTextService.icon(
                        enabled ? "check" : "play_arrow",
                        enabled
                            ? MaterialIsLightService.PRIMARY
                            : MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.size(14.0F, 14.0F))),
            MaterialTextService.text(previousText, 10.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    item ->
                        item.width(LayoutOperationHandler.percent(100.0F)).tooltip(previousText)));
  }

  private ComponentKeyService<?> createComponentKeyService16(
      List<ComponentKeyService<?>> items, int value) {
    ArrayList arrayList = new ArrayList();
    int index = 0;

    while (index < items.size()) {
      ArrayList currentArrayList = new ArrayList();

      for (int currentIndex = index; currentIndex < index + value; currentIndex++) {
        currentArrayList.add(
            currentIndex < items.size()
                ? (ComponentKeyService) items.get(currentIndex)
                : ComponentBoxService.box(item -> item.flex(1.0F).minWidth(0.0F)));
      }

      arrayList.add(
          ComponentBoxService.row(
                  item ->
                      item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F).flexShrink(0.0F),
                  currentArrayList.toArray(ComponentKeyService[]::new))
              .key("row-" + index));
      index += value;
    }

    return ComponentBoxService.column(
        item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F).flexShrink(0.0F),
        arrayList.toArray(ComponentKeyService[]::new));
  }

  private ComponentKeyService<?> createComponentKeyService17(
      String text, String currentText, boolean enabled, Runnable runnable) {
    return this.createComponentKeyService18(text, currentText, enabled, runnable)
        .props(item -> item.flex(1.0F).minWidth(0.0F));
  }

  private ComponentKeyService<MaterialJoinedService> createComponentKeyService18(
      String currentText, String nextText, boolean enabled, Runnable runnable) {
    return MaterialTextService.button(
            "cape." + currentText, nextText, () -> this.updateState2(runnable), false)
        .props(
            item -> {
              item.colors(
                      enabled ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                      enabled
                          ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                          : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .shape(14.0F, 32.0F);
              item.height(LayoutOperationHandler.px(36.0F)).padding(0.0F, 8.0F).gap(4.0F);
            })
        .children(
            MaterialTextService.text(
                nextText,
                12.0F,
                enabled
                    ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                    : MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private ComponentKeyService<?> createComponentKeyService19(String currentText, String nextText) {
    return ComponentBoxService.column(
        item -> item.padding(18.0F, 8.0F).gap(8.0F).flexShrink(0.0F),
        MaterialTextService.text(currentText, 20.0F, MaterialIsLightService.ON_SURFACE),
        MaterialTextService.text(nextText, 13.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.wordWrap(true)));
  }

  private ComponentKeyService<?> createComponentKeyService20(
      String currentText,
      String nextText,
      String previousText,
      int value,
      Consumer<String> consumer,
      boolean enabled) {
    return ComponentBoxService.<ControlLetterSpacingService>node(
            "cape-field",
            ControlLetterSpacingService::new,
            item -> {
              MaterialTextService.input(item);
              item.id(currentText)
                  .width(LayoutOperationHandler.percent(100.0F))
                  .height(LayoutOperationHandler.px(48.0F))
                  .minWidth(0.0F)
                  .flexShrink(0.0F)
                  .placeholder(nextText)
                  .maxLength(value)
                  .password(enabled)
                  .onChanged(consumer);
              if (!item.focused()) {
                item.text(previousText);
              }
            })
        .key(currentText);
  }

  private ComponentKeyService<?> createComponentKeyService21() {
    return this.createComponentKeyService22(
        "connections",
        460,
        ComponentBoxService.column(
            item -> item.gap(14.0F),
            MaterialTextService.text("GIF connections", 24.0F, MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                    "Commons works without an account. For GIPHY search, add your own API key.",
                    14.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.wordWrap(true)),
            this.createComponentKeyService20(
                "cape.key", "GIPHY API key", this.text4, 256, item -> this.text4 = item, true),
            MaterialTextService.text(
                    "Stored only on this device, outside shared client configs.",
                    12.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.wordWrap(true)),
            MaterialTextService.button(
                "cape.key.get",
                "Get a GIPHY API key",
                () -> updateState7("https://developers.giphy.com/dashboard/"),
                false),
            ComponentBoxService.row(
                item -> item.gap(8.0F).justify(ScenePctService.Justify.END),
                MaterialTextService.button(
                    "cape.key.cancel",
                    "Cancel",
                    () -> this.updateState2(() -> this.enabled3 = false),
                    false),
                MaterialTextService.button(
                    "cape.key.save",
                    "Save",
                    () -> {
                      this.capeRevisionService.saveKey(this.text4);
                      this.updateState2(() -> this.enabled3 = false);
                    },
                    true))));
  }

  private ComponentKeyService<?> createComponentKeyService22(
      String text, int value, ComponentKeyService<?> componentKey) {
    return ComponentBoxService.panel(
            item ->
                item.size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.percent(100.0F))
                    .layerBreak(true)
                    .backgroundColor(-1610612736)
                    .padding(16.0F)
                    .direction(ScenePctService.Direction.COLUMN)
                    .align(ScenePctService.Align.CENTER)
                    .justify(ScenePctService.Justify.CENTER)
                    .interactive(true)
                    .stopPropagation(true),
            ComponentBoxService.panel(
                item ->
                    item.id("cape." + text + ".dialog")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .maxWidth(value)
                        .maxHeight(Math.max(150.0F, this.value2 - 32.0F))
                        .direction(ScenePctService.Direction.COLUMN)
                        .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                        .cornerRadius(28.0F)
                        .padding(this.checkCondition2() ? 14.0F : 24.0F)
                        .gap(12.0F)
                        .scrollable(true)
                        .clip(true)
                        .stopPropagation(true)
                        .interactive(true),
                componentKey))
        .key(text)
        .onMount(item -> MaterialEnterService.enter(item, 0.0F, 12.0F));
  }

  private ComponentKeyService<?> createComponentKeyService23() {
    ArrayList arrayList = new ArrayList();

    for (Path currentPath : this.path3) {
      boolean currentEnabled = this.path4.contains(currentPath);
      arrayList.add(
          MaterialTextService.button(
                  "cape.file." + currentPath.getFileName(),
                  currentPath.getFileName().toString(),
                  () -> {
                    if (currentEnabled) {
                      this.updateState5(currentPath);
                    } else {
                      this.updateState2(
                          () -> {
                            this.enabled4 = false;
                            this.enabled = true;
                          });
                      this.capeRevisionService.importFile(currentPath);
                    }
                  },
                  false)
              .props(
                  item -> {
                    item.colors(
                        MaterialIsLightService.SURFACE_CONTAINER,
                        MaterialIsLightService.ON_SURFACE);
                    item.height(LayoutOperationHandler.px(42.0F))
                        .width(LayoutOperationHandler.percent(100.0F))
                        .padding(0.0F, 10.0F)
                        .justify(ScenePctService.Justify.START);
                  })
              .children(
                  MaterialTextService.icon(
                          currentEnabled ? "folder" : "landscape", MaterialIsLightService.PRIMARY)
                      .props(item -> item.size(18.0F, 18.0F)),
                  MaterialTextService.text(
                          currentPath.getFileName().toString(),
                          13.0F,
                          MaterialIsLightService.ON_SURFACE)
                      .props(item -> item.flex(1.0F).minWidth(0.0F))));
    }

    return this.createComponentKeyService22(
        "browser",
        640,
        ComponentBoxService.column(
            item -> item.gap(10.0F).maxHeight(Math.max(130.0F, this.value2 - 90.0F)),
            ComponentBoxService.row(
                item -> item.align(ScenePctService.Align.CENTER).gap(8.0F).flexShrink(0.0F),
                MaterialTextService.text(
                        "Choose a cape image", 22.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.flex(1.0F)),
                MaterialTextService.iconButton(
                        "cape.file.close",
                        "close",
                        "Close file browser",
                        () ->
                            this.updateState2(
                                () -> {
                                  this.enabled4 = false;
                                  this.count3++;
                                }))
                    .props(item -> item.size(36.0F, 36.0F))),
            this.createComponentKeyService20(
                "cape.file.path",
                "Folder path",
                this.text5,
                2048,
                item -> this.text5 = item,
                false),
            ComponentBoxService.row(
                item -> item.gap(4.0F).flexShrink(0.0F),
                this.createComponentKeyService18(
                    "file.up",
                    "Up",
                    false,
                    () -> {
                      if (this.path2 != null && this.path2.getParent() != null) {
                        this.updateState5(this.path2.getParent());
                      }
                    }),
                this.createComponentKeyService18(
                    "file.home",
                    "Home",
                    false,
                    () -> this.updateState5(Path.of(System.getProperty("user.home")))),
                this.createComponentKeyService18(
                    "file.downloads",
                    "Downloads",
                    false,
                    () -> this.updateState5(Path.of(System.getProperty("user.home"), "Downloads"))),
                this.createComponentKeyService18(
                    "file.go", "Open path", false, this::updateState4)),
            MaterialTextService.text(this.text6, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.wordWrap(true).flexShrink(0.0F)),
            ComponentBoxService.column(
                item ->
                    item.flex(1.0F)
                        .minHeight(60.0F)
                        .maxHeight(Math.max(100.0F, this.value2 - 300.0F))
                        .gap(4.0F)
                        .scrollable(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE)
                        .clip(true),
                arrayList.toArray(ComponentKeyService[]::new))));
  }

  private void updateState4() {
    try {
      String text = this.text5.strip();
      if (text.startsWith("~")) {
        text = System.getProperty("user.home") + text.substring(1);
      }

      this.updateState5(Path.of(text));
    } catch (RuntimeException exception) {
      this.text6 = "That path is invalid.";
      this.updateState();
    }
  }

  private void updateState5(Path currentPath) {
    this.updateState2(
        () -> {
          this.enabled4 = true;
          this.text6 = "Opening folder…";
          this.path3 = List.of();
        });
    int value = ++this.count3;
    CompletableFuture.<CapeStudioScreen.Directory>supplyAsync(
            () -> {
              try {
                Path path = currentPath.toRealPath();
                if (Files.isRegularFile(path)) {
                  return new CapeStudioScreen.Directory(path, List.of(), Set.of(), true);
                }

                ArrayList arrayList = new ArrayList();
                HashSet hashSet = new HashSet();

                try (DirectoryStream directoryStream = Files.newDirectoryStream(path)) {
                  for (Path entryPath : (Iterable<Path>) (Iterable<?>) (directoryStream)) {
                    String text = entryPath.getFileName().toString();
                    if (!text.startsWith(".")) {
                      boolean directory = Files.isDirectory(entryPath);
                      if (directory) {
                        hashSet.add(entryPath);
                        arrayList.add(entryPath);
                      } else if (text.toLowerCase(Locale.ROOT).matches(".*\\.(gif|png|jpg|jpeg)")) {
                        arrayList.add(entryPath);
                      }

                      if (arrayList.size() >= 300) {
                        break;
                      }
                    }
                  }
                }

                arrayList.sort(
                    Comparator.<Path, Boolean>comparing(item -> !hashSet.contains(item))
                        .thenComparing(
                            item -> item.getFileName().toString(), String.CASE_INSENSITIVE_ORDER));
                return new CapeStudioScreen.Directory(
                    path, List.copyOf(arrayList), Set.copyOf(hashSet), false);
              } catch (IOException iOException) {
                throw new UncheckedIOException(iOException);
              }
            })
        .whenComplete(
            (item, currentItem) ->
                this.capeRevisionService.dispatch(
                    () -> {
                      if (!this.enabled5 && value == this.count3) {
                        if (currentItem != null) {
                          this.text6 = "Folder unavailable. Choose Home or enter another path.";
                        } else if (item.image()) {
                          this.enabled4 = false;
                          this.enabled = true;
                          this.capeRevisionService.importFile(item.path());
                        } else {
                          this.path2 = item.path();
                          this.text5 = this.path2.toString();
                          this.path3 = item.files();
                          this.path4 = item.folders();
                          this.text6 = this.path3.size() + " folders and images";
                        }

                        this.updateState();
                      }
                    }));
  }

  private void updateState6(int value) {
    this.capeRevisionService.search(this.provider, this.text2, value);
    this.updateState();
  }

  private static void updateState7(String text) {
    try {
      if (!text.isBlank()) {
        CompatOpenUriService.openUri(CapeHttpsService.https(text));
      }
    } catch (Exception exception) {
    }
  }

  private void updateState8() {
    this.updateState2(
        () -> {
          if (this.enabled3) {
            this.enabled3 = false;
          } else if (this.enabled4) {
            this.enabled4 = false;
            this.count3++;
          } else if (this.checkCondition() && this.enabled) {
            this.enabled = false;
          } else if (this.screenScreenIdService != null) {
            this.screenScreenIdService.close();
          }
        });
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenId) {
    if (this.materialTerrainTooltipsService != null
        && this.materialTerrainTooltipsService.computedW() > 0.0F) {
      this.viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
    }

    if (this.timestamp != this.capeRevisionService.revision()) {
      this.timestamp = this.capeRevisionService.revision();
      this.updateState();
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenId) {
    this.enabled5 = true;
    this.count3++;
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
    if (value == 256) {
      this.updateState8();
      return true;
    }

    if (value == 257) {
      if (this.enabled4
          && this.materialTerrainTooltipsService.findById("cape.file.path")
              instanceof ControlLetterSpacingService controlLetterSpacing
          && controlLetterSpacing.focused()) {
        this.updateState4();
        return true;
      }

      if (this.count == 1
          && this.materialTerrainTooltipsService.findById("cape.search")
              instanceof ControlLetterSpacingService currentControlLetterSpacing
          && currentControlLetterSpacing.focused()) {
        this.updateState6(0);
        return true;
      }

      if (this.count == 2
          && this.materialTerrainTooltipsService.findById("cape.url")
              instanceof ControlLetterSpacingService nextControlLetterSpacing
          && nextControlLetterSpacing.focused()) {
        this.capeRevisionService.importLink(this.text3);
        this.enabled = true;
        this.updateState();
        return true;
      }
    }

    return this.materialKeyService.key(
        this.enabled3
            ? this.materialTerrainTooltipsService.findById("cape.connections.dialog")
            : (this.enabled4
                ? this.materialTerrainTooltipsService.findById("cape.browser.dialog")
                : this.materialTerrainTooltipsService),
        value,
        currentValue);
  }

  private static final class Attribution {
    static final BufferedImage IMAGE = createBufferedImage();

    private static BufferedImage createBufferedImage() {
      try (InputStream inputStream =
          CapeStudioScreen.class.getResourceAsStream("/assets/ellice/cape/powered-by-giphy.png")) {
        return ImageIO.read(inputStream);
      } catch (IOException iOException) {
        throw new UncheckedIOException(iOException);
      }
    }
  }

  private record Directory(Path path, List<Path> files, Set<Path> folders, boolean image) {}
}
