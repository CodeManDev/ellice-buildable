package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

final class ParentFolderComponent {
  private final LocalConfigRepository configRepository2;
  private final boolean enabled2;
  private final Path path2;
  private final Path path3;
  private final Runnable runnable;
  private final Runnable runnable2;
  private final Transfer transfer2;
  private Path path4;
  private Path path5;
  private List<Entry> items2 = List.of();
  private CompletableFuture<Page> completableFuture;
  private LocalConfigRepository.Profile profile;
  private String ft9hsbptrat = "";
  private String text3;
  private String text4 = "";
  private String text5 = "";

  ParentFolderComponent(
      LocalConfigRepository localConfigRepository,
      boolean bl,
      String string,
      Path path,
      Path path2,
      Path path3,
      Runnable runnable,
      Runnable runnable2,
      Transfer transfer) {
    this.configRepository2 = localConfigRepository;
    this.enabled2 = bl;
    this.text3 = string;
    this.path2 = path;
    this.path3 = path2;
    this.runnable = runnable;
    this.runnable2 = runnable2;
    this.transfer2 = transfer;
    this.updateState(path3);
  }

  private void updateState(Path path) {
    this.path5 = null;
    this.profile = null;
    this.items2 = List.of();
    this.ft9hsbptrat = "";
    this.text4 = "";
    this.text5 = "Opening folder\u2026";
    this.completableFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Path path2 = path.toRealPath(new LinkOption[0]);
                ArrayList<Entry> arrayList = new ArrayList<Entry>();
                int n = 0;
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path2); ) {
                  for (Path path3 : directoryStream) {
                    boolean bl;
                    if (path3.getFileName().toString().startsWith(".")
                        || !(bl = Files.isDirectory(path3, new LinkOption[0]))
                            && (this.enabled2
                                || !Files.isRegularFile(path3, new LinkOption[0])
                                || !path3
                                    .getFileName()
                                    .toString()
                                    .toLowerCase(Locale.ROOT)
                                    .endsWith(".json"))) continue;
                    arrayList.add(new Entry(path3, bl));
                    if (arrayList.size() != 1000) continue;
                    n = 1;
                    break;
                  }
                }
                arrayList.sort(
                    Comparator.comparing(Entry::folder)
                        .reversed()
                        .thenComparing(
                            entry -> entry.path().getFileName().toString(),
                            String.CASE_INSENSITIVE_ORDER));
                return new Page(path2, List.copyOf(arrayList), n != 0);
              } catch (Exception exception) {
                throw new CompletionException(exception);
              }
            });
    this.runnable.run();
  }

  void tick() {
    if (this.completableFuture == null || !this.completableFuture.isDone()) {
      return;
    }
    CompletableFuture<Page> completableFuture = this.completableFuture;
    this.completableFuture = null;
    try {
      Page page = completableFuture.join();
      this.path4 = page.directory();
      this.items2 = page.entries();
      this.text5 =
          page.truncated()
              ? "First 1,000 items shown. Narrow your search by opening a subfolder."
              : "";
    } catch (Exception exception) {
      this.path4 = null;
      this.text5 = "Folder unavailable. Choose Downloads, Desktop or Home.";
    }
    this.runnable.run();
  }

  private void mhtmezlxvg1c(Path path) {
    this.path5 = path;
    this.profile = null;
    this.text4 = "";
    try {
      this.profile = this.configRepository2.previewProfile(path);
      this.text3 = this.profile.name();
    } catch (Exception exception) {
      this.text4 = "This file cannot be imported. It may be damaged or from a newer version.";
    }
    this.runnable.run();
  }

  private void updateState3(boolean bl) {
    if (this.completableFuture != null
        || (this.enabled2 ? this.path4 == null : this.profile == null)) {
      return;
    }
    try {
      this.transfer2.run(this.enabled2 ? this.path4 : this.path5, this.text3, bl);
    } catch (Exception exception) {
      this.text4 =
          exception.getMessage() == null
              ? "Transfer failed. Please try again."
              : exception.getMessage();
      this.runnable.run();
    }
  }

  ComponentKeyService<?> render(float f, float f2) {
    boolean bl = f < Float.intBitsToFloat(1142292480);
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
    for (Entry entry : this.items2) {
      String fileName = entry.path().getFileName().toString();
      if (!fileName.toLowerCase(Locale.ROOT).contains(this.ft9hsbptrat.toLowerCase(Locale.ROOT)))
        continue;
      boolean bl2 = entry.path().equals(this.path5);
      arrayList.add(
          ComponentBoxService.node(
                  "config-file",
                  MaterialResponsiveService::new,
                  arg_0 -> this.mf0vthqc4svm(bl2, fileName, entry, arg_0),
                  MaterialTextService.icon(
                      entry.folder() ? "folder" : "content_copy",
                      entry.folder()
                          ? MaterialIsLightService.ON_SURFACE_VARIANT
                          : MaterialIsLightService.PRIMARY),
                  MaterialTextService.text(
                          fileName,
                          Float.intBitsToFloat(1096810496),
                          MaterialIsLightService.ON_SURFACE)
                      .props(
                          sceneTextService ->
                              ((SceneTextService)
                                      ((SceneTextService) sceneTextService.flex(1.0f))
                                          .minWidth(0.0f))
                                  .overflow(SceneTextService.Overflow.ELLIPSIS)),
                  MaterialTextService.icon(
                      entry.folder() ? "chevron_right" : (bl2 ? "check" : "chevron_right"),
                      MaterialIsLightService.ON_SURFACE_VARIANT))
              .key(entry.path().toString()));
    }
    if (arrayList.isEmpty()) {
      arrayList.add(
          MaterialTextService.text(
                  this.completableFuture != null
                      ? "Opening folder\u2026"
                      : (!this.ft9hsbptrat.isBlank()
                          ? "No matches."
                          : (this.enabled2
                              ? "No subfolders. You can export here."
                              : "No JSON files here. Try Downloads or another folder.")),
                  Float.intBitsToFloat(1096810496),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService ->
                      ((SceneTextService)
                              ((SceneTextService)
                                      sceneTextService.width(
                                          LayoutOperationHandler.percent(
                                              Float.intBitsToFloat(1120403456))))
                                  .padding(Float.intBitsToFloat(0x41400000)))
                          .wordWrap(true)));
    }
    ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
    arrayList2.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x41000000)))
                    .align(ScenePctService.Align.CENTER),
            MaterialTextService.iconButton(
                "configs.browser.up",
                "arrow_back",
                "Parent folder",
                () -> {
                  if (this.path4 != null && this.path4.getParent() != null) {
                    this.updateState(this.path4.getParent());
                  }
                }),
            MaterialTextService.text(
                    this.path4 == null
                        ? "Choose a folder"
                        : (this.path4.getFileName() == null
                            ? this.path4.toString()
                            : this.path4.getFileName().toString()),
                    Float.intBitsToFloat(1095761920),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f))
                            .overflow(SceneTextService.Overflow.ELLIPSIS)
                            .tooltip(this.path4 == null ? "" : this.path4.toString()))));
    arrayList2.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40C00000)),
            this.createComponentKeyService("downloads", "Downloads", this.path3),
            this.createComponentKeyService("desktop", "Desktop", this.path2.resolve("Desktop")),
            this.createComponentKeyService("home", "Home", this.path2)));
    arrayList2.add(
        MaterialTextService.search(
                "configs.browser.search",
                this.enabled2 ? "Find a folder\u2026" : "Find a file\u2026",
                this.ft9hsbptrat,
                string -> {
                  this.ft9hsbptrat = string;
                  this.runnable.run();
                },
                true)
            .props(
                interactiveSurfacePanel ->
                    interactiveSurfacePanel.width(
                        LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
    if (!this.text5.isEmpty()) {
      arrayList2.add(
          MaterialTextService.text(
                  this.text5,
                  Float.intBitsToFloat(1095761920),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService ->
                      ((SceneTextService)
                              sceneTextService.width(
                                  LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                          .wordWrap(true)));
    }
    arrayList2.add(
        ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.id("configs.browser.files"))
                                .width(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                        .gap(Float.intBitsToFloat(0x40C00000)),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))
            .key("files:" + String.valueOf(this.path4)));
    if (this.enabled2) {
      arrayList2.add(
          MaterialTextService.text(
                  "Export \u201c"
                      + this.text3
                      + "\u201d here. Existing files are kept; copies get a numbered filename.",
                  Float.intBitsToFloat(1096810496),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService ->
                      ((SceneTextService)
                              sceneTextService.width(
                                  LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                          .wordWrap(true)));
    }
    if (this.profile != null) {
      arrayList2.clear();
      arrayList2.add(
          ComponentBoxService.row(
              layoutContainerNode ->
                  ((LayoutContainerNode)
                          ((LayoutContainerNode)
                                  layoutContainerNode.width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456))))
                              .gap(Float.intBitsToFloat(0x41000000)))
                      .align(ScenePctService.Align.CENTER),
              MaterialTextService.iconButton(
                  "configs.browser.choose-another",
                  "arrow_back",
                  "Choose another file",
                  () -> {
                    this.profile = null;
                    this.path5 = null;
                    this.text4 = "";
                    this.runnable.run();
                  }),
              MaterialTextService.text(
                      this.path5.getFileName().toString(),
                      Float.intBitsToFloat(1096810496),
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(
                      sceneTextService ->
                          ((SceneTextService)
                                  ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f))
                              .overflow(SceneTextService.Overflow.ELLIPSIS))));
      arrayList2.add(
          ComponentBoxService.panel(
              sceneCornerRadiusService ->
                  ((SceneCornerRadiusService)
                          ((SceneCornerRadiusService)
                                  ((SceneCornerRadiusService)
                                          ((SceneCornerRadiusService)
                                                  sceneCornerRadiusService.id(
                                                      "configs.browser.preview"))
                                              .width(
                                                  LayoutOperationHandler.percent(
                                                      Float.intBitsToFloat(1120403456))))
                                      .padding(Float.intBitsToFloat(1098907648)))
                              .gap(Float.intBitsToFloat(0x41000000)))
                      .backgroundColor(MaterialIsLightService.SECONDARY_CONTAINER)
                      .cornerRadius(Float.intBitsToFloat(1098907648))
                      .direction(ScenePctService.Direction.COLUMN),
              MaterialTextService.text(
                  "CONFIG PREVIEW",
                  Float.intBitsToFloat(0x41400000),
                  MaterialIsLightService.PRIMARY),
              MaterialTextService.text(
                      this.profile.modules()
                          + " modules \u00b7 "
                          + this.profile.enabled()
                          + " enabled \u00b7 "
                          + this.profile.keybinds()
                          + " keybinds",
                      Float.intBitsToFloat(1096810496),
                      MaterialIsLightService.ON_SURFACE)
                  .props(
                      sceneTextService ->
                          ((SceneTextService)
                                  sceneTextService.width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456))))
                              .wordWrap(true)),
              ComponentBoxService.node(
                      "import-name",
                      ControlLetterSpacingService::new,
                      controlLetterSpacingService -> {
                        MaterialTextService.input(controlLetterSpacingService);
                        ((ControlLetterSpacingService)
                                ((ControlLetterSpacingService)
                                        ((ControlLetterSpacingService)
                                                ((ControlLetterSpacingService)
                                                        ((ControlLetterSpacingService)
                                                                controlLetterSpacingService.id(
                                                                    "configs.browser.name"))
                                                            .width(
                                                                LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                    .height(
                                                        LayoutOperationHandler.px(
                                                            Float.intBitsToFloat(0x42400000))))
                                            .flexShrink(0.0f))
                                    .maxLength(128)
                                    .placeholder("Config name")
                                    .tooltip("Name for the imported copy"))
                            .onChanged(
                                string -> {
                                  this.text3 = string;
                                  this.text4 = "";
                                  this.runnable.run();
                                });
                        if (!controlLetterSpacingService.focused()) {
                          controlLetterSpacingService.text(this.text3);
                        }
                      },
                      new ComponentKeyService[0])
                  .key("name"),
              MaterialTextService.text(
                      "Imported as a new config. Your existing configs stay intact.",
                      Float.intBitsToFloat(0x41400000),
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(
                      sceneTextService ->
                          ((SceneTextService)
                                  sceneTextService.width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456))))
                              .wordWrap(true))));
    }
    if (!this.text4.isEmpty()) {
      arrayList2.add(
          MaterialTextService.text(
                  this.text4, Float.intBitsToFloat(1096810496), MaterialIsLightService.ERROR)
              .props(
                  sceneTextService ->
                      ((SceneTextService)
                              ((SceneTextService) sceneTextService.id("configs.browser.error"))
                                  .width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456))))
                          .wordWrap(true)));
    }
    arrayList2.replaceAll(
        componentKeyService ->
            componentKeyService.props(scenePctService -> scenePctService.flexShrink(0.0f)));
    boolean bl3 =
        this.completableFuture == null
            && (this.enabled2 ? this.path4 != null : this.profile != null);
    ArrayList<ComponentKeyService<?>> actionButtons = new ArrayList<>();
    actionButtons.add(
        MaterialTextService.button(
                "configs.browser.confirm",
                this.enabled2 ? "Export here" : "Import",
                () -> this.updateState3(false),
                this.enabled2)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService) materialJoinedService.available(bl3).flex(1.0f))
                        .minWidth(0.0f)));
    if (!this.enabled2) {
      actionButtons.add(
          MaterialTextService.button(
                  "configs.browser.load", "Import & load", () -> this.updateState3(true), true)
              .props(
                  materialJoinedService ->
                      ((SceneCornerRadiusService) materialJoinedService.available(bl3).flex(1.0f))
                          .minWidth(0.0f)));
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
                                                                                sceneCornerRadiusService
                                                                                    .absolute())
                                                                            .inset(
                                                                                LayoutOperationHandler
                                                                                    .px(0.0f)))
                                                                    .size(
                                                                        LayoutOperationHandler
                                                                            .auto(),
                                                                        LayoutOperationHandler
                                                                            .auto()))
                                                            .padding(
                                                                bl
                                                                    ? Float.intBitsToFloat(
                                                                        0x41000000)
                                                                    : Float.intBitsToFloat(
                                                                        1103101952)))
                                                    .direction(ScenePctService.Direction.COLUMN))
                                            .align(ScenePctService.Align.CENTER))
                                    .justify(ScenePctService.Justify.CENTER))
                            .backgroundColor(-1728053248)
                            .interactive(true))
                    .layerBreak(true),
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
                                                                                            "configs.browser"))
                                                                                .width(
                                                                                    LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .maxWidth(
                                                                            Float.intBitsToFloat(
                                                                                1144913920)))
                                                                .height(
                                                                    LayoutOperationHandler.px(
                                                                        Math.min(
                                                                            Float.intBitsToFloat(
                                                                                0x44340000),
                                                                            Math.max(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1123024896),
                                                                                f2
                                                                                    - (float)
                                                                                        (bl
                                                                                            ? 16
                                                                                            : 48))))))
                                                        .padding(
                                                            bl
                                                                ? Float.intBitsToFloat(0x41400000)
                                                                : Float.intBitsToFloat(1103101952)))
                                                .gap(Float.intBitsToFloat(0x41400000)))
                                        .cornerRadius(Float.intBitsToFloat(1103101952))
                                        .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                                        .direction(ScenePctService.Direction.COLUMN))
                                .clip(true))
                        .stopPropagation(true),
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
                            this.enabled2 ? "Export location" : "Import config",
                            Float.intBitsToFloat(1102053376),
                            MaterialIsLightService.ON_SURFACE)
                        .props(
                            sceneTextService ->
                                ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)),
                    MaterialTextService.iconButton(
                        "configs.browser.cancel", "close", "Cancel", this.runnable2)),
                ComponentBoxService.column(
                        layoutContainerNode ->
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
                                                                                                    "configs.browser.body"))
                                                                                        .width(
                                                                                            LayoutOperationHandler
                                                                                                .percent(
                                                                                                    Float
                                                                                                        .intBitsToFloat(
                                                                                                            1120403456))))
                                                                                .flex(1.0f))
                                                                        .minHeight(0.0f))
                                                                .gap(
                                                                    Float.intBitsToFloat(
                                                                        0x41400000)))
                                                        .scrollable(true))
                                                .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                                        .scrollbarColor(MaterialIsLightService.OUTLINE))
                                .clip(true),
                        (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
                    .key(
                        "browser-body:"
                            + String.valueOf(this.path4)
                            + ":"
                            + String.valueOf(this.path5)),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456))))
                                    .gap(Float.intBitsToFloat(0x41000000)))
                            .flexShrink(0.0f),
                    actionButtons.toArray(ComponentKeyService[]::new))))
        .key("transfer-browser");
  }

  private ComponentKeyService<?> createComponentKeyService(
      String string, String string2, Path path) {
    return MaterialTextService.button(
            "configs.browser." + string, string2, () -> this.updateState(path), false)
        .props(
            materialJoinedService ->
                ((SceneCornerRadiusService) materialJoinedService.flex(1.0f)).minWidth(0.0f));
  }

  private void mf0vthqc4svm(
      boolean bl, String string, Entry entry, MaterialResponsiveService materialResponsiveService) {
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    ((SceneCornerRadiusService)
                                                            ((SceneCornerRadiusService)
                                                                    ((SceneCornerRadiusService)
                                                                            materialResponsiveService
                                                                                .surface(
                                                                                    bl
                                                                                        ? MaterialIsLightService
                                                                                            .SECONDARY_CONTAINER
                                                                                        : MaterialIsLightService
                                                                                            .SURFACE_CONTAINER)
                                                                                .selected(bl)
                                                                                .responsive(true)
                                                                                .id(
                                                                                    "configs.browser.entry."
                                                                                        + string))
                                                                        .width(
                                                                            LayoutOperationHandler
                                                                                .percent(
                                                                                    Float
                                                                                        .intBitsToFloat(
                                                                                            1120403456))))
                                                                .height(
                                                                    LayoutOperationHandler.px(
                                                                        Float.intBitsToFloat(
                                                                            1112539136))))
                                                        .flexShrink(0.0f))
                                                .padding(
                                                    Float.intBitsToFloat(1092616192),
                                                    Float.intBitsToFloat(0x41400000)))
                                        .gap(Float.intBitsToFloat(0x41400000)))
                                .cornerRadius(Float.intBitsToFloat(0x41400000))
                                .direction(ScenePctService.Direction.ROW))
                        .align(ScenePctService.Align.CENTER))
                .cursorStyle(ScenePctService.CursorStyle.POINTER))
        .onClick(
            () -> {
              if (entry.folder()) {
                this.updateState(entry.path());
              } else {
                this.mhtmezlxvg1c(entry.path());
              }
            });
  }

  @FunctionalInterface
  static interface Transfer {
    public void run(Path var1, String var2, boolean var3) throws Exception;
  }

  record Page(Path directory, List<Entry> entries, boolean truncated) {}

  record Entry(Path path, boolean folder) {}
}
