package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.menu.MenuLoaderTracker;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialComponent;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneImageService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

final class WorldListRenderer implements ComponentOperationHandler {
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("d MMM yyyy \u00b7 HH:mm").withZone(ZoneId.systemDefault());
  private final MenuLoaderTracker menuLoaderTracker;
  private final Consumer<MenuLoaderTracker.World> consumer;
  private final Consumer<MenuLoaderTracker.World> consumer2;
  private final Runnable runnable;
  private ComponentThemeService componentThemeService;
  private String text2 = "";
  private String text3;
  private boolean enabled;
  private float value;
  private boolean enabled2;

  void shortHeight(boolean bl) {
    this.enabled2 = bl;
  }

  WorldListRenderer(
      MenuLoaderTracker menuLoaderTracker,
      Consumer<MenuLoaderTracker.World> consumer,
      Consumer<MenuLoaderTracker.World> consumer2,
      Runnable runnable) {
    this.menuLoaderTracker = menuLoaderTracker;
    this.consumer = consumer;
    this.consumer2 = consumer2;
    this.runnable = runnable;
  }

  void width(float f) {
    this.value = f;
  }

  void select(String string) {
    this.text3 = string;
    this.text2 = "";
    this.man7mvmhlqdo();
  }

  private void man7mvmhlqdo() {
    if (this.componentThemeService != null) {
      this.componentThemeService.invalidate();
    }
  }

  private List<MenuLoaderTracker.World> collectValues() {
    Stream<MenuLoaderTracker.World> stream =
        this.menuLoaderTracker.worlds().stream()
            .filter(
                world ->
                    (world.name() + " " + world.id() + " " + world.mode() + " " + world.version())
                        .toLowerCase(Locale.ROOT)
                        .contains(this.text2.strip().toLowerCase(Locale.ROOT)));
    return (this.enabled
            ? stream.sorted(
                Comparator.comparing(MenuLoaderTracker.World::name, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(MenuLoaderTracker.World::id))
            : stream)
        .toList();
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentThemeService) {
    this.componentThemeService = componentThemeService;
    List<MenuLoaderTracker.World> list = this.collectValues();
    MenuLoaderTracker.World world2 =
        list.stream()
            .filter(world -> world.id().equals(this.text3))
            .findFirst()
            .orElse(list.isEmpty() ? null : list.getFirst());
    int n = this.value >= Float.intBitsToFloat(1144913920) ? 1 : 0;
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(
        ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            layoutContainerNode.gap(Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x40800000)),
                    MaterialTextService.text(
                        "Your worlds",
                        this.enabled2
                            ? Float.intBitsToFloat(1101004800)
                            : Float.intBitsToFloat(1103101952),
                        MaterialIsLightService.ON_SURFACE),
                    MaterialTextService.text(
                            (String)
                                (this.menuLoaderTracker.loading()
                                    ? "Reading saved worlds\u2026"
                                    : this.menuLoaderTracker.worlds().size()
                                        + " saved worlds \u00b7 ready for your next idea"),
                            Float.intBitsToFloat(1096810496),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService -> sceneTextService.visible(!this.enabled2))),
                MaterialTextService.iconButton(
                        "worlds.create", "add", "Create a new world", this.runnable)
                    .props(
                        materialJoinedService -> {
                          if (this.enabled2) {
                            materialJoinedService
                                .shape(
                                    Float.intBitsToFloat(1099956224),
                                    Float.intBitsToFloat(0x42000000))
                                .surfaceWidth(Float.intBitsToFloat(0x42000000))
                                .size(
                                    Float.intBitsToFloat(1108344832),
                                    Float.intBitsToFloat(1108344832));
                          }
                        }))
            .key("header"));
    arrayList.add(
        MaterialTextService.search(
            "worlds.search",
            "Search name, mode or version",
            this.text2,
            string -> {
              this.text2 = string;
              this.man7mvmhlqdo();
            },
            this.enabled2));
    arrayList.add(
        ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            layoutContainerNode.gap(Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER),
                MaterialTextService.button(
                        "worlds.sort",
                        this.enabled ? "Name A\u2013Z" : "Recently played",
                        () -> {
                          this.enabled = !this.enabled;
                          this.man7mvmhlqdo();
                        },
                        0,
                        MaterialIsLightService.PRIMARY,
                        Float.intBitsToFloat(1101004800))
                    .children(
                        MaterialTextService.icon("sort", MaterialIsLightService.PRIMARY),
                        MaterialTextService.label(
                            this.enabled ? "Name A\u2013Z" : "Recently played",
                            MaterialIsLightService.PRIMARY))
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            materialJoinedService
                                                .shape(
                                                    Float.intBitsToFloat(1099956224),
                                                    this.enabled2
                                                        ? Float.intBitsToFloat(0x42000000)
                                                        : Float.intBitsToFloat(0x42200000))
                                                .height(
                                                    LayoutOperationHandler.px(
                                                        this.enabled2
                                                            ? Float.intBitsToFloat(1108344832)
                                                            : Float.intBitsToFloat(0x42400000))))
                                        .flex(1.0f))
                                .minWidth(0.0f)),
                MaterialTextService.iconButton(
                        "worlds.refresh",
                        "refresh",
                        "Refresh saved worlds",
                        () -> {
                          this.menuLoaderTracker.reload();
                          this.man7mvmhlqdo();
                        })
                    .props(
                        materialJoinedService -> {
                          materialJoinedService.available(!this.menuLoaderTracker.loading());
                          if (this.enabled2) {
                            materialJoinedService
                                .shape(
                                    Float.intBitsToFloat(1099956224),
                                    Float.intBitsToFloat(0x42000000))
                                .surfaceWidth(Float.intBitsToFloat(0x42000000))
                                .size(
                                    Float.intBitsToFloat(1108344832),
                                    Float.intBitsToFloat(1108344832));
                          }
                        }))
            .key("sort"));
    if (!this.menuLoaderTracker.error().isBlank()) {
      arrayList.add(
          MaterialTextService.text(
                  this.menuLoaderTracker.error(),
                  Float.intBitsToFloat(1096810496),
                  MaterialIsLightService.ERROR)
              .props(sceneTextService -> sceneTextService.wordWrap(true))
              .key("error"));
    }
    ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
    for (MenuLoaderTracker.World world3 : list) {
      arrayList2.add(this.createComponentKeyService(world3, world2));
    }
    if (list.isEmpty()) {
      arrayList2.add(
          ComponentBoxService.column(
                  layoutContainerNode ->
                      ((LayoutContainerNode)
                              ((LayoutContainerNode) layoutContainerNode.id("worlds.empty"))
                                  .padding(
                                      Float.intBitsToFloat(1103101952),
                                      Float.intBitsToFloat(0x41400000)))
                          .gap(Float.intBitsToFloat(0x41400000)),
                  MaterialTextService.icon("landscape", MaterialIsLightService.PRIMARY)
                      .props(
                          sceneSrcService ->
                              sceneSrcService.size(
                                  Float.intBitsToFloat(0x42200000),
                                  Float.intBitsToFloat(0x42200000))),
                  MaterialTextService.text(
                          this.menuLoaderTracker.loading()
                              ? "Finding your worlds"
                              : (this.text2.isBlank()
                                  ? "A blank canvas. Endless possibilities."
                                  : "No matching worlds"),
                          Float.intBitsToFloat(1102053376),
                          MaterialIsLightService.ON_SURFACE)
                      .props(sceneTextService -> sceneTextService.wordWrap(true)),
                  MaterialTextService.text(
                          this.menuLoaderTracker.loading()
                              ? "Your saves are loading in the background."
                              : (this.text2.isBlank()
                                  ? "Build something that is yours. Your Minecraft saves will"
                                        + " appear here automatically."
                                  : "Try another name, game mode or Minecraft version."),
                          Float.intBitsToFloat(1096810496),
                          MaterialIsLightService.ON_SURFACE_VARIANT)
                      .props(sceneTextService -> sceneTextService.wordWrap(true)),
                  MaterialTextService.button(
                          "worlds.empty.create", "Create a world", this.runnable, true)
                      .props(
                          materialJoinedService ->
                              materialJoinedService.visible(
                                  (this.text2.isBlank() && !this.menuLoaderTracker.loading()
                                          ? 1
                                          : 0)
                                      != 0)))
              .key("empty"));
    }
    arrayList.add(
        ComponentBoxService.node(
                "world-reflow-list",
                MaterialComponent::new,
                materialComponent ->
                    ((LayoutContainerNode) materialComponent.id("worlds.library"))
                        .gap(Float.intBitsToFloat(0x40C00000)),
                (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
            .key("library"));
    if (n == 0 && world2 != null) {
      arrayList.add(this.mhguretkcirj(world2).key("detail:" + world2.id()));
    }
    ComponentKeyService<LayoutContainerNode> componentKeyService2 =
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
                                                                                    ((LayoutContainerNode)
                                                                                            layoutContainerNode
                                                                                                .id(
                                                                                                    "worlds.scroll"))
                                                                                        .height(
                                                                                            LayoutOperationHandler
                                                                                                .percent(
                                                                                                    Float
                                                                                                        .intBitsToFloat(
                                                                                                            1120403456))))
                                                                                .flex(1.0f))
                                                                        .minWidth(0.0f))
                                                                .gap(
                                                                    this.enabled2
                                                                        ? Float.intBitsToFloat(
                                                                            0x41000000)
                                                                        : Float.intBitsToFloat(
                                                                            1098907648)))
                                                        .scrollable(true))
                                                .clip(true))
                                        .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                                .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT))
                        .padding(
                            Float.intBitsToFloat(0x40800000),
                            Float.intBitsToFloat(0x41000000),
                            Float.intBitsToFloat(1098907648),
                            0.0f),
                (ComponentKeyService[])
                    arrayList.stream()
                        .map(
                            componentKeyService ->
                                componentKeyService.props(
                                    scenePctService -> scenePctService.flexShrink(0.0f)))
                        .toArray(ComponentKeyService[]::new))
            .key("scroll");
    if (n == 0) {
      return componentKeyService2;
    }
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode) layoutContainerNode.id("worlds.pane"))
                        .size(
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
                .gap(Float.intBitsToFloat(1098907648)),
        componentKeyService2,
        ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        layoutContainerNode.id(
                                                                            "worlds.inspector"))
                                                                    .width(
                                                                        LayoutOperationHandler.px(
                                                                            Float.intBitsToFloat(
                                                                                1134559232))))
                                                            .height(
                                                                LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                    .flexShrink(0.0f))
                                            .scrollable(true))
                                    .clip(true))
                            .scrollbarWidth(Float.intBitsToFloat(0x40400000)))
                    .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT),
            world2 == null
                ? ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                layoutContainerNode.padding(Float.intBitsToFloat(1103101952)))
                            .gap(Float.intBitsToFloat(0x41400000)),
                    MaterialTextService.icon("landscape", MaterialIsLightService.PRIMARY),
                    MaterialTextService.text(
                        "Room to explore",
                        Float.intBitsToFloat(1102053376),
                        MaterialIsLightService.ON_SURFACE),
                    MaterialTextService.text(
                            "Select a world to see its details, or start something new.",
                            Float.intBitsToFloat(1096810496),
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(sceneTextService -> sceneTextService.wordWrap(true)))
                : this.mhguretkcirj(world2).key("detail:" + world2.id())));
  }

  private ComponentKeyService<?> createComponentKeyService(
      MenuLoaderTracker.World world, MenuLoaderTracker.World world2) {
    boolean bl = world == world2;
    int n = bl ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE;
    return ComponentBoxService.node(
            "world-row",
            MaterialResponsiveService::new,
            materialResponsiveService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        materialResponsiveService
                                            .surface(
                                                bl
                                                    ? MaterialIsLightService.SECONDARY_CONTAINER
                                                    : MaterialIsLightService.SURFACE_CONTAINER)
                                            .id("worlds.row." + world.id()))
                                    .height(
                                        LayoutOperationHandler.px(
                                            this.enabled2
                                                ? Float.intBitsToFloat(1116733440)
                                                : Float.intBitsToFloat(1118830592))))
                            .cornerRadius(Float.intBitsToFloat(1101004800))
                            .direction(ScenePctService.Direction.ROW))
                    .align(ScenePctService.Align.CENTER),
            ComponentBoxService.node(
                "world-select",
                MaterialJoinedService::new,
                materialJoinedService -> {
                  materialJoinedService
                      .colors(0, n)
                      .shape(
                          Float.intBitsToFloat(1101004800),
                          this.enabled2
                              ? Float.intBitsToFloat(1116733440)
                              : Float.intBitsToFloat(1118830592));
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
                                                                                                  "worlds.select."
                                                                                                      + world
                                                                                                          .id()))
                                                                                      .height(
                                                                                          LayoutOperationHandler
                                                                                              .px(
                                                                                                  this
                                                                                                          .enabled2
                                                                                                      ? Float
                                                                                                          .intBitsToFloat(
                                                                                                              1116733440)
                                                                                                      : Float
                                                                                                          .intBitsToFloat(
                                                                                                              1118830592))))
                                                                              .flex(1.0f))
                                                                      .minWidth(0.0f))
                                                              .padding(
                                                                  Float.intBitsToFloat(0x41400000)))
                                                      .gap(Float.intBitsToFloat(0x41400000)))
                                              .direction(ScenePctService.Direction.ROW))
                                      .align(ScenePctService.Align.CENTER))
                              .tooltip(world.name() + " \u00b7 " + world.info()))
                      .onClick(
                          () -> {
                            this.text3 = world.id();
                            this.man7mvmhlqdo();
                          });
                },
                WorldListRenderer.thumbnail(
                    world,
                    this.enabled2
                        ? Float.intBitsToFloat(1110441984)
                        : Float.intBitsToFloat(1113587712)),
                ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.flex(1.0f))
                                    .minWidth(0.0f))
                            .gap(1.0f),
                    MaterialTextService.text(world.name(), Float.intBitsToFloat(1098907648), n),
                    MaterialTextService.text(
                        (world.hardcore() ? "Hardcore" : world.mode())
                            + " \u00b7 "
                            + world.version(),
                        Float.intBitsToFloat(0x41400000),
                        MaterialIsLightService.ON_SURFACE_VARIANT),
                    MaterialTextService.text(
                            world.canPlay()
                                ? WorldListRenderer.lastPlayed(world.lastPlayed())
                                : world.info(),
                            Float.intBitsToFloat(0x41400000),
                            world.canPlay()
                                ? MaterialIsLightService.ON_SURFACE_VARIANT
                                : MaterialIsLightService.ERROR)
                        .props(sceneTextService -> sceneTextService.visible(!this.enabled2)))),
            MaterialTextService.iconButton(
                    "worlds.play." + world.id(),
                    "play_arrow",
                    world.canPlay() ? world.playLabel() : world.info(),
                    () -> {
                      if (world.canPlay()) {
                        this.consumer.accept(world);
                      }
                    })
                .props(
                    materialJoinedService ->
                        materialJoinedService
                            .available(world.canPlay())
                            .marginRight(Float.intBitsToFloat(0x40800000))))
        .key("world:" + world.id());
  }

  private ComponentKeyService<?> mhguretkcirj(MenuLoaderTracker.World world) {
    return ComponentBoxService.panel(
        sceneCornerRadiusService ->
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    sceneCornerRadiusService.id("worlds.detail"))
                                .direction(ScenePctService.Direction.COLUMN))
                        .cornerRadius(Float.intBitsToFloat(1103101952))
                        .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                        .padding(Float.intBitsToFloat(1101004800)))
                .gap(Float.intBitsToFloat(1098907648)),
        ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        sceneCornerRadiusService
                                            .backgroundColor(
                                                MaterialIsLightService.PRIMARY_CONTAINER)
                                            .cornerRadius(Float.intBitsToFloat(1101004800))
                                            .padding(Float.intBitsToFloat(1101004800)))
                                    .direction(ScenePctService.Direction.ROW))
                            .align(ScenePctService.Align.CENTER))
                    .justify(ScenePctService.Justify.CENTER),
            WorldListRenderer.thumbnail(world, Float.intBitsToFloat(1121976320))),
        ComponentBoxService.column(
            layoutContainerNode -> layoutContainerNode.gap(Float.intBitsToFloat(0x40800000)),
            MaterialTextService.text(
                    world.name(),
                    Float.intBitsToFloat(1103101952),
                    MaterialIsLightService.ON_SURFACE)
                .props(sceneTextService -> sceneTextService.wordWrap(true)),
            MaterialTextService.text(
                    world.id(),
                    Float.intBitsToFloat(0x41400000),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(sceneTextService -> sceneTextService.tooltip(world.id()))),
        MaterialTextService.button(
                "worlds.detail.play",
                world.playLabel(),
                () -> {
                  if (world.canPlay()) {
                    this.consumer.accept(world);
                  }
                },
                true)
            .props(materialJoinedService -> materialJoinedService.available(world.canPlay())),
        this.createComponentKeyService3("Game mode", world.hardcore() ? "Hardcore" : world.mode()),
        this.createComponentKeyService3("Minecraft", world.version()),
        this.createComponentKeyService3(
            "Last played",
            world.lastPlayed() <= 0L
                ? "Not available"
                : dateTimeFormatter.format(Instant.ofEpochMilli(world.lastPlayed()))),
        this.createComponentKeyService3("Commands", world.commands() ? "Enabled" : "Disabled"),
        MaterialTextService.text(
                world.info(),
                Float.intBitsToFloat(1096810496),
                world.canPlay()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR)
            .props(sceneTextService -> sceneTextService.wordWrap(true)),
        MaterialTextService.text(
                "Experimental features",
                Float.intBitsToFloat(0x41400000),
                MaterialIsLightService.TERTIARY)
            .props(sceneTextService -> sceneTextService.visible(world.experimental())),
        MaterialTextService.button(
                "worlds.edit",
                "Edit & backups",
                () -> {
                  if (world.canEdit()) {
                    this.consumer2.accept(world);
                  }
                },
                false)
            .children(
                MaterialTextService.icon("edit", MaterialIsLightService.ON_SECONDARY_CONTAINER),
                MaterialTextService.label(
                    "Edit & backups", MaterialIsLightService.ON_SECONDARY_CONTAINER))
            .props(materialJoinedService -> materialJoinedService.available(world.canEdit())));
  }

  private ComponentKeyService<?> createComponentKeyService3(String string, String string2) {
    return ComponentBoxService.column(
        layoutContainerNode -> layoutContainerNode.gap(2.0f),
        MaterialTextService.text(
            string, Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT),
        MaterialTextService.text(
                string2, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE)
            .props(sceneTextService -> sceneTextService.wordWrap(true)));
  }

  static ComponentKeyService<?> thumbnail(MenuLoaderTracker.World world, float f) {
    return ComponentBoxService.node(
            "world-thumbnail",
            SceneImageService::new,
            sceneImageService ->
                ((SceneImageService)
                        ((SceneImageService)
                                sceneImageService
                                    .identity(world.id(), world.name())
                                    .image(world.icon())
                                    .material(true)
                                    .size(f, f))
                            .flexShrink(0.0f))
                    .pointerEvents(false),
            new ComponentKeyService[0])
        .key("thumbnail");
  }

  static String lastPlayed(long l) {
    if (l <= 0L) {
      return "Not played yet";
    }
    long l2 = Math.max(0L, (System.currentTimeMillis() - l) / 86400000L);
    return l2 == 0L
        ? "Played today"
        : (l2 == 1L ? "Played yesterday" : "Played " + l2 + " days ago");
  }
}
