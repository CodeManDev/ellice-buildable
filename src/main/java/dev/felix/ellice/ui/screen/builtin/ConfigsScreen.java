package dev.felix.ellice.ui.screen.builtin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.felix.ellice.compat.CompatOpenUriService;
import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.community.CommunityForGameDirService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.AnimatedSelectionIndicator;
import dev.felix.ellice.ui.material.MaterialComponent;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ConfigsScreen implements ScreenOperationHandler {
  private static final Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm", Locale.ENGLISH)
          .withZone(ZoneId.systemDefault());
  private final LocalConfigRepository configRepository2;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private List<LocalConfigRepository.Profile> items2 = List.of();
  private ScreenScreenIdService screenScreenIdService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private String text2 = "";
  private String text3 = "";
  private String text4 = "";
  private String text5 = "";
  private String text6 = "";
  private String text7 = "";
  private String text8 = "";
  private String text9 = "";
  private String text10 = "";
  private final Path path;
  private final Path path2;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4 = true;
  private String text11 = "";
  private String text12 = "top";
  private String text13 = "";
  private String text14 = "";
  private long timestamp;
  private long timestamp2;
  private List<CommunityForGameDirService.Entry> items3 = List.of();
  private CommunityForGameDirService communityForGameDirService;
  private Path path3;
  private Path path4;
  private ParentFolderComponent parentFolderComponent;
  private ConfigsScreen.Dialog dialog2 = ConfigsScreen.Dialog.NONE;
  private float value2 = 1200.0F;
  private float value3 = 800.0F;
  private boolean enabled5;
  private boolean enabled6;

  public ConfigsScreen(LocalConfigRepository localConfigRepository) {
    this(localConfigRepository, Path.of(System.getProperty("user.home")));
  }

  public ConfigsScreen(LocalConfigRepository localConfigRepository, Path currentPath) {
    this.configRepository2 = Objects.requireNonNull(localConfigRepository);
    this.path = currentPath;
    this.path2 = currentPath.resolve("Downloads");
    this.path3 = this.path2;
  }

  @Override
  public String id() {
    return "configs";
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
  public boolean overlaysPreviousScreen() {
    return true;
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.NONE;
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
    this.screenScreenIdService = screenScreenId;
    this.text2 = this.configRepository2.activeConfig();
    this.updateState2();
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    this.materialTerrainTooltipsService
        .id("configs.root")
        .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F));
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
    if (this.value2 != x || this.value3 != y) {
      this.value2 = x;
      this.value3 = y;
      this.updateState();
    }
  }

  private boolean checkCondition() {
    return this.value2 < 820.0F;
  }

  private boolean checkCondition2() {
    return this.value2 < 600.0F;
  }

  private void updateState() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState2() {
    this.items2 = this.configRepository2.profiles();
    if (this.items2.stream().noneMatch(item -> item.name().equals(this.text2))) {
      this.text2 =
          this.items2.stream().findFirst().map(LocalConfigRepository.Profile::name).orElse("");
    }

    this.updateState();
  }

  private LocalConfigRepository.Profile createProfile2() {
    return this.items2.stream()
        .filter(item -> item.name().equals(this.text2))
        .findFirst()
        .orElse(null);
  }

  private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (!this.checkCondition() || !this.enabled5) {
      arrayList.add(this.createComponentKeyService3());
    }

    if (!this.checkCondition() || this.enabled5) {
      arrayList.add(this.createComponentKeyService9());
    }

    ArrayList currentArrayList = new ArrayList();
    currentArrayList.add(
        ComponentBoxService.column(
                item ->
                    item.size(
                            LayoutOperationHandler.percent(100.0F),
                            LayoutOperationHandler.percent(100.0F))
                        .padding(this.checkCondition2() ? 0.0F : 16.0F)
                        .align(ScenePctService.Align.CENTER)
                        .justify(ScenePctService.Justify.CENTER)
                        .pointerEvents(this.dialog2 == ConfigsScreen.Dialog.NONE),
                ComponentBoxService.panel(
                    item ->
                        item.id("configs.workspace")
                            .size(
                                LayoutOperationHandler.percent(100.0F),
                                LayoutOperationHandler.percent(100.0F))
                            .maxWidth(1120.0F)
                            .maxHeight(840.0F)
                            .backgroundColor(MaterialIsLightService.SURFACE)
                            .cornerRadius(this.checkCondition2() ? 0.0F : 28.0F)
                            .direction(ScenePctService.Direction.COLUMN)
                            .clip(true),
                    this.createComponentKeyService2(),
                    ComponentBoxService.row(
                        item ->
                            item.width(LayoutOperationHandler.percent(100.0F))
                                .flex(1.0F)
                                .minHeight(0.0F)
                                .padding(12.0F)
                                .gap(12.0F)
                                .align(ScenePctService.Align.STRETCH),
                        arrayList.toArray(ComponentKeyService[]::new)),
                    ComponentBoxService.row(
                        item ->
                            item.width(LayoutOperationHandler.percent(100.0F))
                                .padding(0.0F, 20.0F, 12.0F, 20.0F)
                                .gap(8.0F)
                                .align(ScenePctService.Align.CENTER)
                                .visible(!this.text5.isEmpty()),
                        MaterialTextService.text(
                                this.text5,
                                13.0F,
                                this.enabled6
                                    ? MaterialIsLightService.ERROR
                                    : MaterialIsLightService.ON_SURFACE_VARIANT)
                            .props(
                                item ->
                                    item.id("configs.notice")
                                        .flex(1.0F)
                                        .minWidth(0.0F)
                                        .wordWrap(true)),
                        MaterialTextService.button(
                                "configs.export.open",
                                "Open folder",
                                () -> CompatOpenUriService.openPath(this.path4.getParent()),
                                false)
                            .props(item -> item.visible(this.path4 != null && !this.enabled6)))))
            .key("workspace"));
    if (this.parentFolderComponent != null) {
      currentArrayList.add(this.parentFolderComponent.render(this.value2, this.value3));
    } else if (this.dialog2 != ConfigsScreen.Dialog.NONE) {
      currentArrayList.add(this.createComponentKeyService14());
    }

    return ComponentBoxService.stack(
        item ->
            item.size(
                LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)),
        currentArrayList.toArray(ComponentKeyService[]::new));
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    if (this.enabled2) {
      boolean empty = this.text14.isEmpty();
      return ComponentBoxService.row(
          item ->
              item.height(LayoutOperationHandler.px(64.0F))
                  .width(LayoutOperationHandler.percent(100.0F))
                  .padding(8.0F, 12.0F)
                  .gap(12.0F)
                  .align(ScenePctService.Align.CENTER)
                  .flexShrink(0.0F),
          MaterialTextService.iconButton(
              "configs.back",
              "arrow_back",
              this.checkCondition() && this.enabled5 ? "Back to configs" : "Back to ClickGUI",
              this::updateState16),
          MaterialTextService.text("Configs", 24.0F, MaterialIsLightService.ON_SURFACE)
              .props(item -> item.flex(1.0F).minWidth(0.0F)),
          this.checkCondition2()
              ? MaterialTextService.iconButton(
                      "configs.share",
                      "add",
                      "Share your active setup with the community",
                      () -> this.updateState11(ConfigsScreen.Dialog.SHARE))
                  .props(item -> item.available(empty).opacity(empty ? 1.0F : 0.38F))
              : MaterialTextService.button(
                      "configs.share",
                      "Share",
                      () -> this.updateState11(ConfigsScreen.Dialog.SHARE),
                      true)
                  .props(
                      item ->
                          item.available(empty)
                              .opacity(empty ? 1.0F : 0.38F)
                              .tooltip(
                                  empty
                                      ? "Share your active setup with the community"
                                      : "Community unavailable"))
                  .children(
                      MaterialTextService.icon("add", MaterialIsLightService.ON_PRIMARY),
                      MaterialTextService.label("Share", MaterialIsLightService.ON_PRIMARY)));
    } else {
      return ComponentBoxService.row(
          item ->
              item.height(LayoutOperationHandler.px(64.0F))
                  .width(LayoutOperationHandler.percent(100.0F))
                  .padding(8.0F, 12.0F)
                  .gap(12.0F)
                  .align(ScenePctService.Align.CENTER)
                  .flexShrink(0.0F),
          MaterialTextService.iconButton(
              "configs.back",
              "arrow_back",
              this.checkCondition() && this.enabled5 ? "Back to configs" : "Back to ClickGUI",
              this::updateState16),
          MaterialTextService.text("Configs", 24.0F, MaterialIsLightService.ON_SURFACE)
              .props(item -> item.flex(1.0F).minWidth(0.0F)),
          this.checkCondition2()
              ? MaterialTextService.iconButton(
                  "configs.import",
                  "folder",
                  "Import config from JSON file",
                  () -> this.updateState11(ConfigsScreen.Dialog.IMPORT))
              : MaterialTextService.button(
                  "configs.import",
                  "Import",
                  () -> this.updateState11(ConfigsScreen.Dialog.IMPORT),
                  false),
          this.checkCondition2()
              ? MaterialTextService.iconButton(
                  "configs.new",
                  "add",
                  "Save current settings as a new config",
                  () -> this.updateState11(ConfigsScreen.Dialog.CREATE))
              : MaterialTextService.button(
                      "configs.new",
                      "New config",
                      () -> this.updateState11(ConfigsScreen.Dialog.CREATE),
                      true)
                  .children(
                      MaterialTextService.icon("add", MaterialIsLightService.ON_PRIMARY),
                      MaterialTextService.label("New config", MaterialIsLightService.ON_PRIMARY)));
    }
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    ArrayList arrayList = new ArrayList();
    if (this.enabled2) {
      int index = 0;

      for (CommunityForGameDirService.Entry entry : this.items3) {
        arrayList.add(this.createComponentKeyService6(entry, index++));
      }

      if (this.enabled3 && arrayList.isEmpty()) {
        arrayList.add(
            ComponentBoxService.column(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .padding(24.0F)
                        .gap(10.0F)
                        .align(ScenePctService.Align.CENTER),
                MaterialTextService.text(
                        "Loading community configs…",
                        15.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.wordWrap(true))));
      } else if (!this.text14.isEmpty() && arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService7("Community unavailable", this.text14, "warning", true));
      } else if (arrayList.isEmpty() && this.text11.isBlank()) {
        arrayList.add(
            this.createComponentKeyService7(
                "No shared configs yet", "Share your setup to start.", "open_in_new", false));
      } else if (arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService7(
                "No configs match your search", "Try another search.", "search", false));
      }
    } else {
      int currentIndex = 0;

      for (LocalConfigRepository.Profile currentProfile : this.items2) {
        if (currentProfile
            .name()
            .toLowerCase(Locale.ROOT)
            .contains(this.text3.toLowerCase(Locale.ROOT))) {
          boolean currentEnabled =
              currentProfile.name().equals(this.configRepository2.activeConfig());
          boolean nextEnabled = currentProfile.name().equals(this.text2);
          int value = currentIndex++;
          arrayList.add(
              ComponentBoxService.<MaterialResponsiveService>node(
                      "config-profile",
                      MaterialResponsiveService::new,
                      item ->
                          item.surface(
                                  nextEnabled
                                      ? MaterialIsLightService.SECONDARY_CONTAINER
                                      : MaterialIsLightService.SURFACE_CONTAINER)
                              .selected(nextEnabled)
                              .responsive(true)
                              .id("configs.profile." + currentProfile.name())
                              .width(LayoutOperationHandler.percent(100.0F))
                              .minHeight(76.0F)
                              .padding(12.0F)
                              .gap(10.0F)
                              .cornerRadius(16.0F)
                              .direction(ScenePctService.Direction.ROW)
                              .align(ScenePctService.Align.CENTER)
                              .cursorStyle(ScenePctService.CursorStyle.POINTER)
                              .onClick(
                                  () -> {
                                    this.materialKeyService.clear(
                                        this.materialTerrainTooltipsService);
                                    this.text2 = currentProfile.name();
                                    this.enabled5 = true;
                                    this.updateState();
                                  }),
                      MaterialTextService.icon(
                          currentEnabled ? "check" : "folder",
                          currentEnabled
                              ? MaterialIsLightService.PRIMARY
                              : MaterialIsLightService.ON_SURFACE_VARIANT),
                      ComponentBoxService.column(
                          item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
                          MaterialTextService.text(
                                  currentProfile.name(), 16.0F, MaterialIsLightService.ON_SURFACE)
                              .props(
                                  item ->
                                      item.width(LayoutOperationHandler.percent(100.0F))
                                          .wordWrap(true)
                                          .maxLines(2)),
                          MaterialTextService.text(
                              !currentProfile.valid()
                                  ? "Unreadable file"
                                  : (currentEnabled
                                      ? "Active · autosave on"
                                      : currentProfile.enabled() + " modules enabled"),
                              12.0F,
                              currentProfile.valid()
                                  ? MaterialIsLightService.ON_SURFACE_VARIANT
                                  : MaterialIsLightService.ERROR)))
                  .key(currentProfile.name())
                  .onMount(
                      item -> MaterialEnterService.enter(item, 8.0F, 0.0F, calculateValue(value))));
        }
      }

      if (arrayList.isEmpty()) {
        arrayList.add(
            MaterialTextService.text(
                    this.text3.isBlank()
                        ? "No saved configs yet. Save your current settings to get started."
                        : "No configs match your search.",
                    15.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.padding(16.0F).wordWrap(true)));
      }
    }

    ArrayList currentArrayList = new ArrayList();
    currentArrayList.add(
        ComponentBoxService.column(
                item -> item.width(LayoutOperationHandler.percent(100.0F)).flexShrink(0.0F),
                ComponentBoxService.<AnimatedSelectionIndicator>node(
                        "material-tabs",
                        AnimatedSelectionIndicator::new,
                        item ->
                            item.selection(this.enabled2 ? 1 : 0)
                                .id("configs.tabs")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .height(LayoutOperationHandler.px(48.0F))
                                .direction(ScenePctService.Direction.ROW),
                        this.createComponentKeyService4(
                            "mine",
                            "Mine · " + this.items2.size(),
                            !this.enabled2,
                            () -> {
                              this.enabled2 = false;
                              this.updateState();
                            }),
                        this.createComponentKeyService4(
                            "community",
                            this.timestamp > 0L ? "Community · " + this.timestamp : "Community",
                            this.enabled2,
                            this::updateState3))
                    .key("navigation"),
                MaterialTextService.divider())
            .key("tabs"));
    currentArrayList.add(
        ComponentBoxService.row(
            item ->
                item.width(LayoutOperationHandler.percent(100.0F))
                    .gap(4.0F)
                    .align(ScenePctService.Align.CENTER)
                    .flexShrink(0.0F),
            MaterialTextService.search(
                    "configs.search",
                    this.enabled2 ? "Search community configs" : "Find a config…",
                    this.enabled2 ? this.text11 : this.text3,
                    item -> {
                      if (this.enabled2) {
                        this.text11 = item;
                        this.enabled4 = true;
                      } else {
                        this.text3 = item;
                      }

                      this.updateState();
                    },
                    true)
                .props(item -> item.flex(1.0F).minWidth(0.0F)),
            MaterialTextService.iconButton(
                    "configs.refresh",
                    "refresh",
                    this.enabled2 ? "Refresh community configs" : "Refresh configs",
                    () -> {
                      if (this.enabled2) {
                        this.updateState5();
                      } else {
                        this.updateState2();
                      }
                    })
                .props(item -> item.size(40.0F, 40.0F))));
    if (this.enabled2) {
      ArrayList nextArrayList = new ArrayList();
      nextArrayList.add(
          this.createComponentKeyService5(
              "top", "Top", this.text12.equals("top"), () -> this.updateState4("top")));
      nextArrayList.add(
          this.createComponentKeyService5(
              "new", "New", this.text12.equals("new"), () -> this.updateState4("new")));
      nextArrayList.add(
          this.createComponentKeyService5(
              "downloads",
              "Downloads",
              this.text12.equals("downloads"),
              () -> this.updateState4("downloads")));
      if (this.enabled3 && !this.items3.isEmpty()) {
        nextArrayList.add(ComponentBoxService.spacer().key("sort-spacer"));
        nextArrayList.add(
            MaterialTextService.text("Updating…", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .key("sort-updating"));
      }

      currentArrayList.add(
          ComponentBoxService.row(
              item ->
                  item.width(LayoutOperationHandler.percent(100.0F))
                      .gap(8.0F)
                      .align(ScenePctService.Align.CENTER)
                      .flexShrink(0.0F),
              nextArrayList.toArray(ComponentKeyService[]::new)));
    }

    currentArrayList.add(
        ComponentBoxService.<MaterialComponent>node(
                "material-reflow-list",
                MaterialComponent::new,
                item ->
                    item.id("configs.list")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .flex(1.0F)
                        .minHeight(0.0F)
                        .gap(6.0F)
                        .scrollable(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE)
                        .clip(true),
                arrayList.toArray(ComponentKeyService[]::new))
            .key("configs-list:" + (this.enabled2 ? "community" : "mine"))
            .onMount(item -> MaterialEnterService.enter(item, 0.0F, 10.0F)));
    return ComponentBoxService.column(
        item ->
            item.id("configs.list-pane")
                .width(
                    this.checkCondition()
                        ? LayoutOperationHandler.percent(100.0F)
                        : LayoutOperationHandler.px(300.0F))
                .height(LayoutOperationHandler.percent(100.0F))
                .gap(12.0F)
                .flexShrink(0.0F),
        currentArrayList.toArray(ComponentKeyService[]::new));
  }

  private ComponentKeyService<?> createComponentKeyService4(
      String text, String currentText, boolean enabled, Runnable runnable) {
    int value =
        enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT;
    return ComponentBoxService.<MaterialJoinedService>node(
            "material-tab",
            MaterialJoinedService::new,
            item -> {
              item.colors(0, value).shape(0.0F, 48.0F);
              item.id("configs.tab." + text)
                  .height(LayoutOperationHandler.px(48.0F))
                  .flex(1.0F)
                  .minWidth(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .onClick(
                      () -> {
                        this.materialKeyService.clear(this.materialTerrainTooltipsService);
                        runnable.run();
                        this.updateState();
                      });
            },
            MaterialTextService.label(currentText, value)
                .props(
                    item ->
                        item.fontSize(15.0F)
                            .lineHeight(22.0F)
                            .letterSpacing(0.1F)
                            .flexShrink(0.0F)))
        .key(text);
  }

  private ComponentKeyService<?> createComponentKeyService5(
      String text, String currentText, boolean enabled, Runnable runnable) {
    int value =
        enabled
            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
            : MaterialIsLightService.ON_SURFACE_VARIANT;
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (enabled) {
      arrayList.add(
          MaterialTextService.icon("check", value).props(item -> item.size(18.0F, 18.0F)));
    }

    arrayList.add(MaterialTextService.label(currentText, value));
    return MaterialTextService.button(
            "configs.filter." + text,
            currentText,
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              runnable.run();
              this.updateState();
            },
            false)
        .props(
            item -> {
              item.colors(enabled ? MaterialIsLightService.SECONDARY_CONTAINER : 0, value)
                  .shape(8.0F, 32.0F)
                  .outlined(!enabled);
              item.height(LayoutOperationHandler.px(32.0F))
                  .padding(0.0F, 12.0F)
                  .gap(6.0F)
                  .flexShrink(0.0F);
            })
        .children(arrayList.toArray(ComponentKeyService[]::new));
  }

  private void updateState3() {
    this.enabled2 = true;
    this.text13 = "";
    this.enabled5 = false;
    this.updateState5();
  }

  private void updateState4(String text) {
    this.text12 = text;
    this.updateState5();
  }

  private void updateState5() {
    if (this.enabled2) {
      this.enabled4 = false;
      this.timestamp2 = System.currentTimeMillis();

      CommunityForGameDirService communityForGameDir;
      try {
        communityForGameDir = this.createCommunityForGameDirService();
      } catch (Exception exception) {
        this.enabled3 = false;
        this.text14 = "Community unavailable.";
        this.updateState();
        return;
      }

      this.enabled3 = true;
      this.text14 = "";
      this.updateState();
      communityForGameDir.list(
          this.text12,
          this.text11,
          0,
          item -> {
            this.enabled3 = false;
            if (item.ok()) {
              this.items3 = item.value().items();
              this.timestamp = item.value().total();
            } else {
              this.text14 = item.error();
            }

            this.updateState();
          });
    }
  }

  private CommunityForGameDirService createCommunityForGameDirService() {
    if (this.communityForGameDirService == null) {
      this.communityForGameDirService = CommunityForGameDirService.forGameDir();
    }

    return this.communityForGameDirService;
  }

  private ComponentKeyService<?> createComponentKeyService6(
      CommunityForGameDirService.Entry entry, int value) {
    boolean enabled = entry.id().equals(this.text13);
    int currentValue =
        this.communityForGameDirService != null
                && this.communityForGameDirService.deleteToken(entry.id()) != null
            ? 1
            : 0;
    boolean currentEnabled = checkCondition3(entry);
    String currentText = currentEnabled ? "HOT" : (checkCondition4(entry) ? "FRESH" : "");
    String nextText = createText(entry.createdAtMs());
    String previousText =
        "by "
            + entry.author()
            + (nextText.isEmpty() ? "" : " · " + nextText)
            + " · "
            + entry.downloads()
            + " downloads";
    ArrayList arrayList = new ArrayList();
    if (!currentText.isEmpty()) {
      arrayList.add(
          ComponentBoxService.row(
              item ->
                  item.width(LayoutOperationHandler.percent(100.0F))
                      .gap(6.0F)
                      .align(ScenePctService.Align.CENTER)
                      .flexShrink(0.0F),
              MaterialTextService.icon(
                      currentEnabled ? "star_filled" : "history",
                      currentEnabled
                          ? MaterialIsLightService.PRIMARY
                          : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(item -> item.size(14.0F, 14.0F)),
              MaterialTextService.text(
                  currentText,
                  10.0F,
                  currentEnabled
                      ? MaterialIsLightService.PRIMARY
                      : MaterialIsLightService.ON_SURFACE_VARIANT)));
    }

    arrayList.add(
        MaterialTextService.text(entry.title(), 16.0F, MaterialIsLightService.ON_SURFACE)
            .props(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F)).wordWrap(true).maxLines(2)));
    arrayList.add(
        MaterialTextService.text(previousText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.width(LayoutOperationHandler.percent(100.0F)).wordWrap(true)));
    return ComponentBoxService.<MaterialResponsiveService>node(
            "config-community",
            MaterialResponsiveService::new,
            item ->
                item.surface(
                        enabled
                            ? MaterialIsLightService.SECONDARY_CONTAINER
                            : MaterialIsLightService.SURFACE_CONTAINER)
                    .selected(enabled)
                    .responsive(true)
                    .id("configs.community." + entry.id())
                    .width(LayoutOperationHandler.percent(100.0F))
                    .minHeight(76.0F)
                    .padding(12.0F)
                    .gap(10.0F)
                    .cornerRadius(16.0F)
                    .direction(ScenePctService.Direction.ROW)
                    .align(ScenePctService.Align.CENTER)
                    .cursorStyle(ScenePctService.CursorStyle.POINTER)
                    .onClick(
                        () -> {
                          this.materialKeyService.clear(this.materialTerrainTooltipsService);
                          this.text13 = entry.id();
                          this.enabled5 = true;
                          this.updateState();
                        }),
            this.createComponentKeyService8(entry.author(), (currentValue != 0), 40),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
                arrayList.toArray(ComponentKeyService[]::new)),
            ComponentBoxService.column(
                item -> item.align(ScenePctService.Align.CENTER).gap(2.0F).flexShrink(0.0F),
                MaterialTextService.icon(
                        entry.myVote() == 1
                            ? "thumb_up"
                            : (entry.myVote() == -1 ? "thumb_down" : "chevron_right"),
                        entry.myVote() == 0
                            ? MaterialIsLightService.OUTLINE
                            : MaterialIsLightService.PRIMARY)
                    .props(item -> item.size(18.0F, 18.0F)),
                MaterialTextService.text(
                        createText3(entry.score()),
                        11.0F,
                        entry.myVote() == 0
                            ? MaterialIsLightService.ON_SURFACE_VARIANT
                            : MaterialIsLightService.PRIMARY)
                    .props(item -> item.textAlign(SceneTextService.TextAlign.CENTER))))
        .key(entry.id())
        .onMount(item -> MaterialEnterService.enter(item, 8.0F, 0.0F, calculateValue(value)));
  }

  private ComponentKeyService<?> createComponentKeyService7(
      String currentText, String nextText, String previousText, boolean enabled) {
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        MaterialTextService.icon(previousText, MaterialIsLightService.PRIMARY)
            .props(item -> item.size(36.0F, 36.0F)));
    arrayList.add(
        MaterialTextService.text(currentText, 18.0F, MaterialIsLightService.ON_SURFACE)
            .props(item -> item.wordWrap(true)));
    arrayList.add(
        MaterialTextService.text(nextText, 13.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.wordWrap(true)));
    if (enabled) {
      arrayList.add(
          MaterialTextService.button("configs.retry", "Retry", this::updateState5, false));
    }

    return ComponentBoxService.column(
        item ->
            item.width(LayoutOperationHandler.percent(100.0F))
                .padding(24.0F)
                .gap(10.0F)
                .align(ScenePctService.Align.CENTER),
        arrayList.toArray(ComponentKeyService[]::new));
  }

  private static float calculateValue(int value) {
    return Math.min(value * 0.025F, 0.2F);
  }

  private static String createText(long size) {
    if (size <= 0L) {
      return "";
    }

    long currentSize = Math.max(0L, (System.currentTimeMillis() - size) / 1000L);
    return currentSize < 10L
        ? "just now"
        : (currentSize < 60L
            ? currentSize + "s ago"
            : (currentSize < 3600L
                ? currentSize / 60L + "m ago"
                : (currentSize < 86400L
                    ? currentSize / 3600L + "h ago"
                    : currentSize / 86400L + "d ago")));
  }

  private static String createText2(String text) {
    return text != null && !text.isEmpty()
        ? text.substring(0, text.offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT)
        : "?";
  }

  private static boolean checkCondition3(CommunityForGameDirService.Entry entry) {
    return entry.score() >= 10;
  }

  private static boolean checkCondition4(CommunityForGameDirService.Entry entry) {
    return entry.createdAtMs() > 0L && System.currentTimeMillis() - entry.createdAtMs() < 86400000L;
  }

  private ComponentKeyService<?> createComponentKeyService8(
      String currentText, boolean enabled, int value) {
    return ComponentBoxService.panel(
        item ->
            item.size(value, value)
                .flexShrink(0.0F)
                .cornerRadius(value * 0.35F)
                .backgroundColor(
                    enabled
                        ? MaterialIsLightService.PRIMARY_CONTAINER
                        : MaterialIsLightService.SURFACE_HIGHEST)
                .direction(ScenePctService.Direction.COLUMN)
                .align(ScenePctService.Align.CENTER)
                .justify(ScenePctService.Justify.CENTER)
                .pointerEvents(false),
        MaterialTextService.text(
            createText2(currentText),
            value >= 52 ? 26.0F : 18.0F,
            enabled
                ? MaterialIsLightService.ON_PRIMARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private static String createText3(int value) {
    return value > 0 ? "+" + value : Integer.toString(value);
  }

  private CommunityForGameDirService.Entry createEntry() {
    return this.items3.stream()
        .filter(item -> item.id().equals(this.text13))
        .findFirst()
        .orElse(null);
  }

  private ComponentKeyService<?> createComponentKeyService9() {
    if (this.enabled2) {
      return this.createComponentKeyService10(this.createEntry());
    }

    LocalConfigRepository.Profile profile = this.createProfile2();
    if (profile == null) {
      return ComponentBoxService.column(
          item -> item.id("configs.details").flex(1.0F).minWidth(0.0F).padding(24.0F).gap(12.0F),
          MaterialTextService.text(
                  "Your settings, ready to switch", 24.0F, MaterialIsLightService.ON_SURFACE)
              .props(item -> item.wordWrap(true)),
          MaterialTextService.text(
                  "Save a config for each server, play style or setup.",
                  16.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.wordWrap(true)));
    }

    boolean currentEnabled = profile.name().equals(this.configRepository2.activeConfig());
    boolean nextEnabled = profile.name().equalsIgnoreCase("default");
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        MaterialTextService.text(
            currentEnabled ? "ACTIVE CONFIG" : "SAVED CONFIG",
            12.0F,
            MaterialIsLightService.PRIMARY));
    arrayList.add(
        MaterialTextService.text(profile.name(), 26.0F, MaterialIsLightService.ON_SURFACE)
            .props(
                item ->
                    item.id("configs.detail.name")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .wordWrap(true)));
    arrayList.add(
        MaterialTextService.text(
            profile.modified() > 0L
                ? "Saved " + dateTimeFormatter.format(Instant.ofEpochMilli(profile.modified()))
                : "Not saved yet",
            13.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT));
    if (profile.valid()) {
      arrayList.add(
          ComponentBoxService.row(
              item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F).marginTop(8.0F),
              this.createComponentKeyService13(Integer.toString(profile.enabled()), "Enabled"),
              this.createComponentKeyService13(Integer.toString(profile.settings()), "Settings"),
              this.createComponentKeyService13(Integer.toString(profile.keybinds()), "Keybinds")));
      arrayList.add(
          MaterialTextService.text(
                  profile.modules()
                      + " modules stored · "
                      + profile.hudElements()
                      + " HUD elements",
                  14.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.wordWrap(true)));
      arrayList.add(
          MaterialTextService.text(
                  currentEnabled
                      ? "Changes you make in the ClickGUI are saved to this config automatically."
                      : "Loading restores this config’s modules, settings, keybinds and saved HUD"
                            + " layout.",
                  15.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  item ->
                      item.width(LayoutOperationHandler.percent(100.0F))
                          .wordWrap(true)
                          .margin(4.0F, 0.0F)));
    } else {
      arrayList.add(
          MaterialTextService.text(profile.problem(), 15.0F, MaterialIsLightService.ERROR)
              .props(item -> item.wordWrap(true)));
    }

    arrayList.add(
        MaterialTextService.button(
                "configs.load",
                currentEnabled ? "Save now" : "Load config",
                () ->
                    this.updateState15(
                        () -> {
                          if (currentEnabled) {
                            this.configRepository2.saveProfile(profile.name());
                          } else {
                            this.configRepository2.loadProfile(profile.name());
                          }
                        },
                        currentEnabled ? "Config saved." : "Loaded “" + profile.name() + "”."),
                true)
            .props(
                item ->
                    item.available(profile.valid()).width(LayoutOperationHandler.percent(100.0F))));
    arrayList.add(
        MaterialTextService.button(
                "configs.export", "Export config", () -> this.updateState14(this.path3), false)
            .props(
                item ->
                    item.available(profile.valid()).width(LayoutOperationHandler.percent(100.0F))));
    arrayList.add(
        MaterialTextService.button(
                "configs.export.location",
                "Change export location…",
                () -> this.updateState11(ConfigsScreen.Dialog.EXPORT),
                false)
            .props(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .tooltip(this.path3.toString())));
    if (!currentEnabled) {
      arrayList.add(
          MaterialTextService.button(
                  "configs.replace",
                  "Replace with current settings",
                  () -> this.updateState11(ConfigsScreen.Dialog.REPLACE),
                  false)
              .props(item -> item.width(LayoutOperationHandler.percent(100.0F))));
    }

    arrayList.add(MaterialTextService.divider().props(item -> item.margin(8.0F, 0.0F)));
    arrayList.add(
        ComponentBoxService.row(
            item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F),
            MaterialTextService.button(
                    "configs.rename",
                    "Rename",
                    () -> this.updateState11(ConfigsScreen.Dialog.RENAME),
                    false)
                .props(
                    item ->
                        item.available(!nextEnabled)
                            .opacity(nextEnabled ? 0.38F : 1.0F)
                            .flex(1.0F)
                            .minWidth(0.0F)
                            .tooltip(
                                nextEnabled
                                    ? "The default config keeps its name"
                                    : "Rename this config")),
            MaterialTextService.button(
                    "configs.delete",
                    "Delete",
                    () -> this.updateState11(ConfigsScreen.Dialog.DELETE),
                    MaterialIsLightService.SURFACE_HIGH,
                    MaterialIsLightService.ERROR,
                    20.0F)
                .props(
                    item ->
                        item.available(!nextEnabled && !currentEnabled)
                            .opacity(!nextEnabled && !currentEnabled ? 1.0F : 0.38F)
                            .flex(1.0F)
                            .minWidth(0.0F)
                            .tooltip(
                                currentEnabled
                                    ? "Load another config before deleting this one"
                                    : (nextEnabled
                                        ? "The default config is kept"
                                        : "Delete this saved config")))));
    return this.createComponentKeyService12("detail:" + profile.name(), arrayList);
  }

  private ComponentKeyService<?> createComponentKeyService10(
      CommunityForGameDirService.Entry entry) {
    if (entry == null) {
      return ComponentBoxService.column(
          item -> item.id("configs.details").flex(1.0F).minWidth(0.0F).padding(24.0F).gap(12.0F),
          MaterialTextService.text("Community configs", 24.0F, MaterialIsLightService.ON_SURFACE)
              .props(item -> item.wordWrap(true)),
          MaterialTextService.text(
                  this.timestamp > 0L
                      ? this.timestamp + " shared setups. Pick one to preview, install or vote."
                      : "Shared setups from other players appear here.",
                  16.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.wordWrap(true)));
    }

    ArrayList arrayList = new ArrayList();
    ArrayList currentArrayList = new ArrayList();
    currentArrayList.add(
        MaterialTextService.text("COMMUNITY CONFIG", 12.0F, MaterialIsLightService.PRIMARY));
    if (entry.verified()) {
      currentArrayList.add(
          MaterialTextService.icon("check", MaterialIsLightService.PRIMARY)
              .props(item -> item.size(14.0F, 14.0F)));
      currentArrayList.add(
          MaterialTextService.text("VERIFIED", 10.0F, MaterialIsLightService.PRIMARY));
    }

    arrayList.add(
        ComponentBoxService.row(
            item ->
                item.width(LayoutOperationHandler.percent(100.0F))
                    .gap(14.0F)
                    .align(ScenePctService.Align.CENTER)
                    .flexShrink(0.0F),
            ComponentBoxService.<MaterialLabelService>node(
                    "material-emblem",
                    MaterialLabelService::new,
                    item ->
                        item.shape("cookie9")
                            .active(true)
                            .label(createText2(entry.title()))
                            .tint(MaterialIsLightService.PRIMARY)
                            .size(40.0F, 40.0F)
                            .flexShrink(0.0F))
                .key("emblem"),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
                ComponentBoxService.row(
                    item ->
                        item.width(LayoutOperationHandler.percent(100.0F))
                            .gap(6.0F)
                            .align(ScenePctService.Align.CENTER)
                            .flexShrink(0.0F),
                    currentArrayList.toArray(ComponentKeyService[]::new)),
                MaterialTextService.text(entry.title(), 26.0F, MaterialIsLightService.ON_SURFACE)
                    .props(
                        item ->
                            item.id("configs.detail.name")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .wordWrap(true)))));
    String currentText = createText(entry.createdAtMs());
    arrayList.add(
        MaterialTextService.text(
                "by " + entry.author() + (currentText.isEmpty() ? "" : " · " + currentText),
                13.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .wordWrap(true)
                        .tooltip(
                            entry.createdAtMs() > 0L
                                ? dateTimeFormatter.format(
                                    Instant.ofEpochMilli(entry.createdAtMs()))
                                : "")));
    if (!entry.description().isEmpty()) {
      arrayList.add(
          MaterialTextService.text(
                  entry.description(), 15.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.width(LayoutOperationHandler.percent(100.0F)).wordWrap(true)));
    }

    ArrayList nextArrayList = new ArrayList();
    nextArrayList.add(this.createComponentKeyService13(Integer.toString(entry.score()), "Points"));
    nextArrayList.add(
        this.createComponentKeyService13(Integer.toString(entry.downloads()), "Downloads"));
    if (entry.moduleCount() > 0) {
      nextArrayList.add(
          this.createComponentKeyService13(
              entry.enabledCount() + "/" + entry.moduleCount(), "Enabled"));
    }

    nextArrayList.add(
        this.createComponentKeyService13(
            entry.elliceVersion().isEmpty() ? "–" : entry.elliceVersion(), "Version"));
    arrayList.add(
        ComponentBoxService.row(
            item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(8.0F).marginTop(8.0F),
            nextArrayList.toArray(ComponentKeyService[]::new)));
    arrayList.add(
        MaterialTextService.text(
                "Installing replaces your current setup. Your keybinds are never touched.",
                15.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                item ->
                    item.width(LayoutOperationHandler.percent(100.0F))
                        .wordWrap(true)
                        .margin(4.0F, 0.0F)));
    if (!entry.elliceVersion().isEmpty()
        && !entry.elliceVersion().equals(CoreIsInitializedHandler.VERSION)) {
      arrayList.add(
          MaterialTextService.text(
                  "Shared for ellice " + entry.elliceVersion() + " — some settings may differ.",
                  12.0F,
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(item -> item.width(LayoutOperationHandler.percent(100.0F)).wordWrap(true)));
    }

    arrayList.add(
        MaterialTextService.button(
                "configs.install." + entry.id(),
                "Install config",
                () -> this.updateState7(entry),
                true)
            .props(item -> item.width(LayoutOperationHandler.percent(100.0F))));
    ArrayList previousArrayList = new ArrayList();
    previousArrayList.add(this.createComponentKeyService11(entry, 1));
    previousArrayList.add(
        MaterialTextService.text(
                createText3(entry.score()), 14.0F, MaterialIsLightService.ON_SURFACE)
            .props(
                item ->
                    item.width(LayoutOperationHandler.px(48.0F))
                        .textAlign(SceneTextService.TextAlign.CENTER)
                        .flexShrink(0.0F)));
    previousArrayList.add(this.createComponentKeyService11(entry, -1));
    previousArrayList.add(ComponentBoxService.spacer().key("vote-spacer"));
    previousArrayList.add(
        MaterialTextService.iconButton(
            "configs.report." + entry.id(),
            "warning",
            "Report this config",
            () -> this.updateState11(ConfigsScreen.Dialog.REPORT)));
    if (this.communityForGameDirService != null
        && this.communityForGameDirService.deleteToken(entry.id()) != null) {
      previousArrayList.add(
          MaterialTextService.iconButton(
              "configs.unshare." + entry.id(),
              "delete",
              "Remove your shared config",
              () -> this.updateState8(entry)));
    }

    arrayList.add(
        ComponentBoxService.row(
            item ->
                item.width(LayoutOperationHandler.percent(100.0F))
                    .gap(8.0F)
                    .align(ScenePctService.Align.CENTER)
                    .flexShrink(0.0F),
            previousArrayList.toArray(ComponentKeyService[]::new)));
    return this.createComponentKeyService12("community:" + entry.id(), arrayList);
  }

  private ComponentKeyService<?> createComponentKeyService11(
      CommunityForGameDirService.Entry entry, int value) {
    int currentValue = value > 0 ? 1 : 0;
    int nextValue = entry.myVote() == value ? 1 : 0;
    int previousValue =
        nextValue == 0
            ? 0
            : (currentValue != 0
                ? MaterialIsLightService.SECONDARY_CONTAINER
                : MaterialIsLightService.ERROR_CONTAINER);
    int sourceValue =
        nextValue == 0
            ? MaterialIsLightService.ON_SURFACE_VARIANT
            : (currentValue != 0
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_ERROR_CONTAINER);
    return ComponentBoxService.<MaterialJoinedService>node(
            "material-button",
            MaterialJoinedService::new,
            item -> {
              item.colors(previousValue, sourceValue).shape(20.0F, 40.0F).surfaceWidth(40.0F);
              item.id("configs.vote." + (currentValue != 0 ? "up." : "down.") + entry.id())
                  .size(48.0F, 48.0F)
                  .padding(0.0F)
                  .gap(0.0F)
                  .flexShrink(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .tooltip(currentValue != 0 ? "Upvote this config" : "Downvote this config")
                  .onClick(() -> this.updateState6(entry, value));
            },
            MaterialTextService.icon(currentValue != 0 ? "thumb_up" : "thumb_down", sourceValue)
                .props(item -> item.size(20.0F, 20.0F))
                .key("vote-icon:" + entry.myVote())
                .onMount(
                    item -> {
                      item.scale(1.35F);
                      MotionAnimateService.animate(
                          item,
                          MotionColorsContainer.Floats.SCALE,
                          1.0F,
                          MaterialEnterService.PRESS);
                    }))
        .key("configs.vote." + (currentValue != 0 ? "up." : "down.") + entry.id());
  }

  private void updateState6(CommunityForGameDirService.Entry entry, int currentValue) {
    int nextValue = entry.myVote() == currentValue ? 0 : currentValue;

    try {
      this.createCommunityForGameDirService()
          .vote(
              entry.id(),
              nextValue,
              item -> {
                if (!item.ok()) {
                  this.enabled6 = true;
                  this.text5 = item.error();
                  this.updateState();
                } else {
                  CommunityForGameDirService.VoteResult voteResult = item.value();
                  this.items3 =
                      this.items3.stream()
                          .map(
                              configEntry ->
                                  configEntry.id().equals(entry.id())
                                      ? new CommunityForGameDirService.Entry(
                                          configEntry.id(),
                                          configEntry.title(),
                                          configEntry.description(),
                                          configEntry.author(),
                                          configEntry.elliceVersion(),
                                          voteResult.upvotes(),
                                          voteResult.downvotes(),
                                          voteResult.score(),
                                          configEntry.downloads(),
                                          configEntry.createdAtMs(),
                                          voteResult.myVote(),
                                          configEntry.moduleCount(),
                                          configEntry.enabledCount(),
                                          configEntry.verified())
                                      : configEntry)
                          .toList();
                  this.updateState();
                }
              });
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = "Community unavailable.";
      this.updateState();
    }
  }

  private void updateState7(CommunityForGameDirService.Entry entry) {
    this.text5 = "Downloading “" + entry.title() + "”…";
    this.enabled6 = false;
    this.updateState();

    try {
      this.createCommunityForGameDirService()
          .download(
              entry.id(),
              item -> {
                if (!item.ok()) {
                  this.enabled6 = true;
                  this.text5 = item.error();
                  this.updateState();
                } else {
                  Path path = null;

                  try {
                    path = Files.createTempFile("ellice-community-", ".json");
                    JsonObject jsonObject = item.value().config();
                    jsonObject.remove("keybinds");
                    Files.writeString(path, gson2.toJson(jsonObject), StandardCharsets.UTF_8);
                    String text = this.configRepository2.importProfile(path, entry.title(), true);
                    this.enabled2 = false;
                    this.text3 = "";
                    this.text2 = text;
                    this.enabled6 = false;
                    this.text5 = "Installed “" + text + "”.";
                    this.updateState2();
                    this.enabled5 = true;
                    this.updateState();
                  } catch (Exception exception) {
                    this.enabled6 = true;
                    this.text5 = createText4(exception);
                    this.updateState();
                  } finally {
                    try {
                      if (path != null) {
                        Files.deleteIfExists(path);
                      }
                    } catch (Exception currentException) {
                    }
                  }
                }
              });
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = "Community unavailable.";
      this.updateState();
    }
  }

  private void updateState8(CommunityForGameDirService.Entry entry) {
    try {
      this.createCommunityForGameDirService()
          .delete(
              entry.id(),
              item -> {
                if (!item.ok()) {
                  this.enabled6 = true;
                  this.text5 = item.error();
                  this.updateState();
                } else {
                  this.enabled6 = false;
                  this.text5 = "Removed your shared config.";
                  this.text13 = "";
                  this.updateState5();
                }
              });
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = "Community unavailable.";
      this.updateState();
    }
  }

  private void updateState9() {
    String text = this.text4.strip();
    if (text.length() >= 3 && text.length() <= 64) {
      JsonObject jsonObject = this.configRepository2.exportActiveConfig();
      String currentText = this.text8.strip();
      String nextText = this.text9.strip();
      int currentSize =
          jsonObject.has("modules") && jsonObject.get("modules").isJsonObject()
              ? jsonObject.getAsJsonObject("modules").size()
              : 0;
      this.updateState12();
      this.text5 = "Sharing “" + text + "”…";
      this.enabled6 = false;
      this.updateState();

      try {
        this.createCommunityForGameDirService()
            .upload(
                text,
                currentText,
                nextText.isEmpty() ? "Anonymous" : nextText,
                jsonObject,
                CoreIsInitializedHandler.VERSION,
                item -> {
                  if (item.ok()) {
                    this.enabled6 = false;
                    this.text5 =
                        "Shared “"
                            + text
                            + "”"
                            + (currentSize > 0 ? " · " + currentSize + " modules." : ". Thanks!");
                    this.updateState5();
                  } else {
                    this.enabled6 = true;
                    this.text5 = item.error();
                    this.updateState();
                  }
                });
      } catch (Exception exception) {
        this.enabled6 = true;
        this.text5 = "Community unavailable.";
        this.updateState();
      }
    } else {
      this.text6 = "Use 3–64 characters for the title.";
      this.updateState();
    }
  }

  private void updateState10() {
    String text = this.text7;
    String currentText = this.text10.strip();
    this.updateState12();

    try {
      this.createCommunityForGameDirService()
          .report(
              text,
              currentText,
              item -> {
                this.enabled6 = !item.ok();
                this.text5 = item.ok() ? "Thanks, we'll take a look." : item.error();
                this.updateState();
              });
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = "Community unavailable.";
      this.updateState();
    }
  }

  private ComponentKeyService<?> createComponentKeyService12(
      String text, List<ComponentKeyService<?>> items) {
    items.replaceAll(componentKey -> componentKey.props(component -> component.flexShrink(0.0F)));
    return ComponentBoxService.panel(
            item ->
                item.id("configs.details")
                    .height(LayoutOperationHandler.percent(100.0F))
                    .flex(1.0F)
                    .minWidth(0.0F)
                    .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                    .cornerRadius(24.0F)
                    .padding(this.checkCondition2() ? 16.0F : 24.0F)
                    .gap(12.0F)
                    .direction(ScenePctService.Direction.COLUMN)
                    .scrollable(true)
                    .scrollbarWidth(3.0F)
                    .scrollbarColor(MaterialIsLightService.OUTLINE)
                    .clip(true),
            items.toArray(ComponentKeyService[]::new))
        .key(text)
        .onMount(item -> MaterialEnterService.enter(item, 16.0F, 0.0F));
  }

  private ComponentKeyService<?> createComponentKeyService13(String currentText, String nextText) {
    return ComponentBoxService.panel(
        item ->
            item.flex(1.0F)
                .minWidth(0.0F)
                .padding(12.0F)
                .gap(4.0F)
                .cornerRadius(16.0F)
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .direction(ScenePctService.Direction.COLUMN),
        MaterialTextService.text(currentText, 22.0F, MaterialIsLightService.ON_SURFACE),
        MaterialTextService.text(nextText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private void updateState11(ConfigsScreen.Dialog dialog) {
    if (dialog != ConfigsScreen.Dialog.IMPORT && dialog != ConfigsScreen.Dialog.EXPORT) {
      this.materialKeyService.clear(this.materialTerrainTooltipsService);
      this.text7 = dialog == ConfigsScreen.Dialog.REPORT ? this.text13 : this.text2;
      this.dialog2 = dialog;
      this.text4 =
          dialog == ConfigsScreen.Dialog.RENAME
              ? this.text2
              : (dialog == ConfigsScreen.Dialog.SHARE ? this.configRepository2.activeConfig() : "");
      if (dialog == ConfigsScreen.Dialog.SHARE) {
        this.text8 = "";
        this.text9 = "";
      }

      if (dialog == ConfigsScreen.Dialog.REPORT) {
        this.text10 = "";
      }

      this.text6 = "";
      this.updateState();
    } else {
      this.materialKeyService.clear(this.materialTerrainTooltipsService);
      this.dialog2 = dialog;
      this.text7 = this.text2;
      this.parentFolderComponent =
          new ParentFolderComponent(
              this.configRepository2,
              dialog == ConfigsScreen.Dialog.EXPORT,
              this.text2,
              this.path,
              this.path2,
              dialog == ConfigsScreen.Dialog.EXPORT ? this.path3 : this.path2,
              this::updateState,
              this::updateState12,
              (item, currentItem, nextItem) -> {
                if (dialog == ConfigsScreen.Dialog.EXPORT) {
                  this.path4 = this.configRepository2.exportToDirectory(this.text7, item);
                  this.path3 = item;
                  this.text5 = "Exported “" + this.path4.getFileName() + "”.";
                } else {
                  this.text2 = this.configRepository2.importProfile(item, currentItem, nextItem);
                  this.text3 = "";
                  this.path4 = null;
                  this.text5 =
                      nextItem
                          ? "Imported and loaded “" + this.text2 + "”."
                          : "Imported “" + this.text2 + "”.";
                }

                this.enabled6 = false;
                this.updateState12();
                this.updateState2();
                this.enabled5 = true;
                this.updateState();
              });
      this.updateState();
    }
  }

  private void updateState12() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.dialog2 = ConfigsScreen.Dialog.NONE;
    this.parentFolderComponent = null;
    this.text6 = "";
    this.updateState();
  }

  private ComponentKeyService<?> createComponentKeyService14() {
    int value =
        this.dialog2 != ConfigsScreen.Dialog.CREATE && this.dialog2 != ConfigsScreen.Dialog.RENAME
            ? 0
            : 1;

    String currentText =
        switch (this.dialog2) {
          case CREATE -> "New config";
          case RENAME -> "Rename config";
          case REPLACE -> "Replace saved config?";
          case DELETE -> "Delete config?";
          case IMPORT -> "Import config";
          case EXPORT -> "Export config";
          case SHARE -> "Share config";
          case REPORT -> "Report config?";
          default -> "";
        };

    String nextText =
        switch (this.dialog2) {
          case CREATE ->
              this.value3 < 360.0F
                  ? "Save as a new active config."
                  : "Save your current modules, settings, keybinds and HUD as a new active config.";
          case RENAME -> "Choose a new name for “" + this.text7 + "”.";
          case REPLACE ->
              "Replace “"
                  + this.text7
                  + "” with your current settings? Its previous contents will be overwritten.";
          case DELETE -> "Delete “" + this.text7 + "” from your saved configs?";
          case IMPORT ->
              "Choose a name for the imported config. Load it afterwards to apply its settings.";
          case EXPORT -> "Save “" + this.text7 + "” as a JSON file.";
          case SHARE ->
              "Share “"
                  + this.configRepository2.activeConfig()
                  + "” with the community. Your keybinds are never uploaded.";
          case REPORT -> "What's wrong with this config? Reports are anonymous.";
          default -> "";
        };
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        MaterialTextService.text(nextText, 15.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                item ->
                    item.id("configs.dialog.description")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .wordWrap(true)
                        .flexShrink(0.0F)));
    if (value != 0) {
      arrayList.add(
          ComponentBoxService.<ControlLetterSpacingService>node(
                  "config-name",
                  ControlLetterSpacingService::new,
                  item -> {
                    MaterialTextService.input(item);
                    item.id("configs.dialog.name")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(48.0F))
                        .flexShrink(0.0F)
                        .maxLength(128)
                        .placeholder("My config")
                        .onChanged(
                            text -> {
                              this.text4 = text;
                              this.text6 = "";
                              this.updateState();
                            })
                        .onSubmit(
                            text -> {
                              this.text4 = text;
                              this.updateState13();
                            });
                    if (!item.focused()) {
                      item.text(this.text4);
                    }
                  })
              .key("name")
              .onMount(this.materialKeyService::focus));
    }

    if (this.dialog2 == ConfigsScreen.Dialog.SHARE) {
      arrayList.add(
          ComponentBoxService.<ControlLetterSpacingService>node(
                  "config-name",
                  ControlLetterSpacingService::new,
                  item -> {
                    MaterialTextService.input(item);
                    item.id("configs.dialog.name")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(48.0F))
                        .flexShrink(0.0F)
                        .maxLength(64)
                        .placeholder("Config title")
                        .onChanged(
                            text -> {
                              this.text4 = text;
                              this.text6 = "";
                              this.updateState();
                            })
                        .onSubmit(
                            text -> {
                              this.text4 = text;
                              this.updateState13();
                            });
                    if (!item.focused()) {
                      item.text(this.text4);
                    }
                  })
              .key("name")
              .onMount(this.materialKeyService::focus));
      arrayList.add(
          ComponentBoxService.<ControlLetterSpacingService>node(
                  "config-description",
                  ControlLetterSpacingService::new,
                  item -> {
                    MaterialTextService.input(item);
                    item.id("configs.dialog.share.description")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(48.0F))
                        .flexShrink(0.0F)
                        .maxLength(500)
                        .placeholder("What is this setup for? (optional)")
                        .onChanged(
                            text -> {
                              this.text8 = text;
                              this.updateState();
                            });
                    if (!item.focused()) {
                      item.text(this.text8);
                    }
                  })
              .key("share-description"));
      arrayList.add(
          ComponentBoxService.row(
              item ->
                  item.width(LayoutOperationHandler.percent(100.0F))
                      .justify(ScenePctService.Justify.END)
                      .flexShrink(0.0F),
              MaterialTextService.text(
                      this.text8.length() + " / 500",
                      12.0F,
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .key("share-counter")));
      arrayList.add(
          ComponentBoxService.<ControlLetterSpacingService>node(
                  "config-author",
                  ControlLetterSpacingService::new,
                  item -> {
                    MaterialTextService.input(item);
                    item.id("configs.dialog.share.author")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(48.0F))
                        .flexShrink(0.0F)
                        .maxLength(32)
                        .placeholder("Your name (optional)")
                        .onChanged(
                            text -> {
                              this.text9 = text;
                              this.updateState();
                            });
                    if (!item.focused()) {
                      item.text(this.text9);
                    }
                  })
              .key("share-author"));
    }

    if (this.dialog2 == ConfigsScreen.Dialog.REPORT) {
      arrayList.add(
          ComponentBoxService.<ControlLetterSpacingService>node(
                  "config-reason",
                  ControlLetterSpacingService::new,
                  item -> {
                    MaterialTextService.input(item);
                    item.id("configs.dialog.report.reason")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .height(LayoutOperationHandler.px(48.0F))
                        .flexShrink(0.0F)
                        .maxLength(200)
                        .placeholder("Reason (optional)")
                        .onChanged(
                            text -> {
                              this.text10 = text;
                              this.updateState();
                            })
                        .onSubmit(
                            text -> {
                              this.text10 = text;
                              this.updateState13();
                            });
                    if (!item.focused()) {
                      item.text(this.text10);
                    }
                  })
              .key("reason")
              .onMount(this.materialKeyService::focus));
    }

    arrayList.add(
        MaterialTextService.text(this.text6, 14.0F, MaterialIsLightService.ERROR)
            .props(
                item ->
                    item.id("configs.dialog.error")
                        .visible(!this.text6.isEmpty())
                        .wordWrap(true)
                        .flexShrink(0.0F)));
    return ComponentBoxService.panel(
            item ->
                item.absolute()
                    .inset(LayoutOperationHandler.px(0.0F))
                    .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
                    .padding(this.checkCondition2() ? 12.0F : 24.0F)
                    .direction(ScenePctService.Direction.COLUMN)
                    .align(ScenePctService.Align.CENTER)
                    .justify(ScenePctService.Justify.CENTER)
                    .backgroundColor(-1728053248)
                    .interactive(true)
                    .layerBreak(true),
            ComponentBoxService.panel(
                item ->
                    item.id("configs.dialog")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .maxWidth(480.0F)
                        .maxHeight(
                            Math.max(120.0F, this.value3 - (this.checkCondition2() ? 24 : 48)))
                        .padding(this.value3 < 360.0F ? 12.0F : 24.0F)
                        .gap(12.0F)
                        .cornerRadius(28.0F)
                        .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                        .direction(ScenePctService.Direction.COLUMN)
                        .clip(true)
                        .stopPropagation(true),
                MaterialTextService.text(currentText, 22.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.wordWrap(true).flexShrink(0.0F)),
                ComponentBoxService.column(
                    item ->
                        item.width(LayoutOperationHandler.percent(100.0F))
                            .minHeight(0.0F)
                            .flexShrink(1.0F)
                            .gap(16.0F)
                            .scrollable(true)
                            .scrollbarWidth(3.0F)
                            .clip(true),
                    arrayList.toArray(ComponentKeyService[]::new)),
                ComponentBoxService.row(
                    item ->
                        item.width(LayoutOperationHandler.percent(100.0F))
                            .gap(8.0F)
                            .justify(ScenePctService.Justify.END)
                            .flexShrink(0.0F),
                    MaterialTextService.button(
                        "configs.dialog.cancel", "Cancel", this::updateState12, false),
                    MaterialTextService.button(
                        "configs.dialog.confirm",
                        this.dialog2 == ConfigsScreen.Dialog.IMPORT
                            ? "Import"
                            : (this.dialog2 == ConfigsScreen.Dialog.EXPORT
                                ? "Export"
                                : (this.dialog2 == ConfigsScreen.Dialog.DELETE
                                    ? "Delete"
                                    : (this.dialog2 == ConfigsScreen.Dialog.REPLACE
                                        ? "Replace"
                                        : (this.dialog2 == ConfigsScreen.Dialog.SHARE
                                            ? "Share"
                                            : (this.dialog2 == ConfigsScreen.Dialog.REPORT
                                                ? "Report"
                                                : "Save"))))),
                        this::updateState13,
                        this.dialog2 == ConfigsScreen.Dialog.DELETE
                            ? MaterialIsLightService.ERROR_CONTAINER
                            : MaterialIsLightService.PRIMARY,
                        this.dialog2 == ConfigsScreen.Dialog.DELETE
                            ? MaterialIsLightService.ON_ERROR_CONTAINER
                            : MaterialIsLightService.ON_PRIMARY,
                        20.0F))))
        .key("dialog:" + this.dialog2)
        .onMount(item -> MaterialEnterService.enter(item.findById("configs.dialog"), 0.0F, 14.0F));
  }

  private void updateState13() {
    try {
      switch (this.dialog2) {
        case CREATE:
          this.configRepository2.createProfile(this.text4);
          this.text2 = this.configRepository2.activeConfig();
          break;
        case RENAME:
          this.configRepository2.renameProfile(this.text7, this.text4);
          this.text2 = LocalConfigRepository.normalizeConfigName(this.text4);
          break;
        case REPLACE:
          this.configRepository2.saveProfile(this.text7);
          break;
        case DELETE:
          this.configRepository2.removeProfile(this.text7);
          break;
        case IMPORT:
        case EXPORT:
        default:
          return;
        case SHARE:
          this.updateState9();
          return;
        case REPORT:
          this.updateState10();
          return;
      }
      this.text5 =
          switch (this.dialog2) {
            case CREATE -> "Created “" + this.text2 + "”. Changes now save to this config.";
            case RENAME -> "Config renamed.";
            case REPLACE -> "Saved current settings.";
            case DELETE -> "Config deleted.";
            default -> "";
          };
      this.enabled6 = false;
      this.updateState12();
      this.updateState2();
      this.enabled5 = true;
      this.updateState();
    } catch (Exception exception) {
      this.text6 = createText4(exception);
      this.updateState();
    }
  }

  private void updateState14(Path path) {
    try {
      this.path4 = this.configRepository2.exportToDirectory(this.text2, path);
      this.enabled6 = false;
      this.text5 = "Exported “" + this.path4.getFileName() + "”.";
      this.updateState();
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = createText4(exception);
      this.updateState();
    }
  }

  private void updateState15(ConfigsScreen.Operation operation, String text) {
    try {
      operation.run();
      this.enabled6 = false;
      this.text5 = text;
      this.updateState2();
    } catch (Exception exception) {
      this.enabled6 = true;
      this.text5 = createText4(exception);
      this.updateState();
    }
  }

  private static String createText4(Exception exception) {
    return exception.getMessage() == null
        ? "This action could not be completed."
        : exception.getMessage();
  }

  private void updateState16() {
    if (this.dialog2 != ConfigsScreen.Dialog.NONE) {
      this.updateState12();
    } else if (this.checkCondition() && this.enabled5) {
      this.materialKeyService.clear(this.materialTerrainTooltipsService);
      this.enabled5 = false;
      this.updateState();
    } else {
      if (this.screenScreenIdService != null) {
        this.screenScreenIdService.open("clickgui");
      }
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
    if (value == 256) {
      this.updateState16();
      return true;
    }

    if (value == 70 && (currentValue & 10) != 0 && this.dialog2 == ConfigsScreen.Dialog.NONE) {
      this.enabled5 = false;
      this.updateState();
      if (this.materialTerrainTooltipsService.findById("configs.search")
          instanceof ControlLetterSpacingService controlLetterSpacing) {
        this.materialKeyService.focus(controlLetterSpacing);
      }

      return true;
    } else {
      return this.materialKeyService.key(this.materialTerrainTooltipsService, value, currentValue);
    }
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenId) {
    if (this.parentFolderComponent != null) {
      this.parentFolderComponent.tick();
    }

    if (this.enabled2 && this.enabled4 && System.currentTimeMillis() - this.timestamp2 > 500L) {
      this.updateState5();
    }

    if (this.materialTerrainTooltipsService != null
        && this.materialTerrainTooltipsService.computedW() > 0.0F) {
      this.viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
    }
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenId) {
    MaterialEnterService.enter(
        this.materialTerrainTooltipsService.findById("configs.workspace"), 0.0F, 12.0F);
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenId) {
    this.parentFolderComponent = null;
    this.dialog2 = ConfigsScreen.Dialog.NONE;
    this.enabled2 = false;
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
  }

  private enum Dialog {
    NONE,
    CREATE,
    RENAME,
    REPLACE,
    DELETE,
    IMPORT,
    EXPORT,
    SHARE,
    REPORT;

    private static ConfigsScreen.Dialog[] $values() {
      return new ConfigsScreen.Dialog[] {
        NONE, CREATE, RENAME, REPLACE, DELETE, IMPORT, EXPORT, SHARE, REPORT
      };
    }
  }

  @FunctionalInterface
  private interface Operation {
    void run() throws Exception;
  }
}
