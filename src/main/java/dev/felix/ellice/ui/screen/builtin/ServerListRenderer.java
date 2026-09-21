package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.CompatSavedService;
import dev.felix.ellice.render.compositor.CompositorEmptyService;
import dev.felix.ellice.server.ParsedServerEndpoint;
import dev.felix.ellice.server.ServerKeyService;
import dev.felix.ellice.server.ServerQueryService;
import dev.felix.ellice.server.ServerViewTracker;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialComponent;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialSelectedSelector;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneDataService;
import dev.felix.ellice.ui.scene.SceneImageService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

final class ServerListRenderer implements ComponentOperationHandler {
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
  private final ServerViewTracker serverViewTracker;
  private final BiConsumer<String, String> text2;
  private BiConsumer<String, String> text3;
  private ComponentThemeService componentThemeService;
  private UUID uUID;
  private String text4 = "your account";
  private String text5;
  private String text6 = "";
  private String text7 = "";
  private String text8 = "";
  private ServerListRenderer.Filter renderer = ServerListRenderer.Filter.ALL;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3 = true;
  private long timestamp2 = -1L;
  private long timestamp3;
  private long timestamp4;
  private ScenePctService<?> scenePctService;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;
  private float value;
  private boolean enabled7;

  void preview(BiConsumer<String, String> biConsumer) {
    this.text3 = biConsumer;
  }

  ComponentKeyService<?> previewDetails(
      ComponentThemeService componentTheme, String text, String currentText) {
    this.componentThemeService = componentTheme;
    return this.createComponentKeyService(
        new ServerListRenderer.Server(
            text, currentText, this.serverViewTracker.library().usage(this.uUID, text)));
  }

  void shortHeight(boolean enabled) {
    this.enabled6 = enabled;
  }

  void expanded(boolean enabled) {
    this.enabled5 = enabled;
  }

  void select(String text) {
    this.text5 = text;
    this.text6 = "";
    this.renderer = ServerListRenderer.Filter.ALL;
    this.updateState();
  }

  void width(float currentValue) {
    this.value = currentValue;
    this.enabled4 = currentValue < 460.0F;
  }

  ServerListRenderer(
      ServerViewTracker currentServerViewTracker, BiConsumer<String, String> biConsumer) {
    this.serverViewTracker = currentServerViewTracker;
    this.text2 = biConsumer;
  }

  void account(UUID currentUUID, String text, boolean enabled) {
    if (!Objects.equals(currentUUID, this.uUID)
        || !Objects.equals(text, this.text4)
        || this.enabled3 != enabled) {
      if (!Objects.equals(currentUUID, this.uUID)) {
        this.text5 = null;
      }

      this.uUID = currentUUID;
      this.text4 = text == null ? "your account" : text;
      this.enabled3 = enabled;
      this.updateState();
    }
  }

  void tick(boolean enabled) {
    if (enabled) {
      if (this.enabled7 && this.scenePctService != null) {
        ScenePctService scenePct = this.scenePctService.findById("servers.detail");
        if (scenePct != null) {
          float value =
              Math.max(
                  this.scenePctService.scrollMin(),
                  Math.min(
                      0.0F,
                      this.scenePctService.scrollY()
                          + this.scenePctService.computedY()
                          - scenePct.computedY()));
          this.scenePctService.scrollTarget(value);
          MotionAnimateService.animate(
              this.scenePctService,
              MotionColorsContainer.Floats.SCROLL_Y,
              value,
              MaterialIsLightService.SPATIAL);
          this.enabled7 = false;
        }
      }

      long longValue = System.currentTimeMillis();
      if (longValue >= this.timestamp4) {
        this.timestamp4 = longValue + 200L;
        List items = this.collectValues2();
        ServerListRenderer.Server server = this.createServer(items);
        if (server != null) {
          this.serverViewTracker.request(server.address(), false);
        }

        for (ServerListRenderer.Server currentServer :
            (Iterable<ServerListRenderer.Server>)
                (Iterable<?>) (items.stream().limit(64L).toList())) {
          this.serverViewTracker.seedIcon(currentServer.address(), null);
          this.serverViewTracker.request(currentServer.address(), false);
        }

        if (this.timestamp2 != this.serverViewTracker.revision()
            || longValue - this.timestamp3 > 5000L) {
          this.timestamp2 = this.serverViewTracker.revision();
          this.timestamp3 = longValue;
          this.updateState();
        }
      }
    }
  }

  private void updateState() {
    if (this.componentThemeService != null) {
      this.componentThemeService.invalidate();
    }
  }

  private void updateState2() {
    if (this.scenePctService != null) {
      this.scenePctService.scrollTarget(0.0F);
      MotionAnimateService.animate(
          this.scenePctService,
          MotionColorsContainer.Floats.SCROLL_Y,
          0.0F,
          MaterialIsLightService.SPATIAL);
    }
  }

  @Override
  public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
    this.componentThemeService = componentTheme;
    List items = this.collectValues2();
    long longValue = this.collectValues().stream().filter(item -> item.usage().favorite()).count();
    ServerListRenderer.Server currentServer = this.createServer(items);
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(
        ComponentBoxService.row(
                item -> item.gap(8.0F).align(ScenePctService.Align.CENTER),
                ComponentBoxService.column(
                    item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
                    MaterialTextService.text(
                        "Your servers",
                        this.enabled6 ? 20.0F : 24.0F,
                        MaterialIsLightService.ON_SURFACE),
                    MaterialTextService.text(
                            this.text4
                                + " · "
                                + longValue
                                + (longValue == 1L ? " favourite" : " favourites"),
                            14.0F,
                            MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(item -> item.visible(!this.enabled6))),
                MaterialTextService.iconButton(
                        "servers.add",
                        "add",
                        "Add server",
                        () -> {
                          this.enabled = !this.enabled;
                          if (this.enabled) {
                            this.text7 = "";
                          }

                          this.text8 = "";
                          this.updateState();
                          this.updateState2();
                        })
                    .props(
                        item -> {
                          if (this.enabled6) {
                            item.shape(18.0F, 32.0F).surfaceWidth(32.0F).size(36.0F, 36.0F);
                          }
                        }))
            .key("header"));
    arrayList.add(
        MaterialTextService.search(
            "servers.search",
            "Search servers",
            this.text6,
            item -> {
              this.text6 = item;
              this.updateState();
            },
            this.enabled6));
    arrayList.add(
        ComponentBoxService.row(
                item -> item.gap(2.0F),
                this.createComponentKeyService6("All", ServerListRenderer.Filter.ALL),
                this.createComponentKeyService6("Favourites", ServerListRenderer.Filter.FAVORITES),
                this.createComponentKeyService6("Recent", ServerListRenderer.Filter.RECENT))
            .key("filters"));
    if (this.enabled) {
      arrayList.add(this.createComponentKeyService7().key("add-form"));
    }

    ArrayList<ComponentKeyService<?>> currentArrayList = new ArrayList<>();

    for (ServerListRenderer.Server nextServer :
        (Iterable<ServerListRenderer.Server>) (Iterable<?>) (items)) {
      currentArrayList.add(this.createComponentKeyService2(nextServer, currentServer));
    }

    if (items.isEmpty()) {
      String currentText =
          !this.text6.isBlank()
              ? "No matching servers"
              : (this.renderer == ServerListRenderer.Filter.FAVORITES
                  ? "Keep your favourites close"
                  : (this.renderer == ServerListRenderer.Filter.RECENT
                      ? "Your next session starts here"
                      : "Find your next place to play"));
      String nextText =
          !this.text6.isBlank()
              ? "Try a different name or address."
              : (this.renderer == ServerListRenderer.Filter.FAVORITES
                  ? "Star a server in All to pin it to this account."
                  : (this.renderer == ServerListRenderer.Filter.RECENT
                      ? "Servers you play on with this account will appear here."
                      : "Your Minecraft server list appears here. Add a server to get started."));
      currentArrayList.add(
          ComponentBoxService.column(
                  item -> item.id("servers.empty").padding(24.0F, 12.0F).gap(8.0F),
                  MaterialTextService.text(currentText, 20.0F, MaterialIsLightService.ON_SURFACE)
                      .props(item -> item.wordWrap(true)),
                  createComponentKeyService8(nextText, MaterialIsLightService.ON_SURFACE_VARIANT))
              .key("empty"));
    }

    arrayList.add(
        ComponentBoxService.<MaterialComponent>node(
                "material-reflow-list",
                MaterialComponent::new,
                item -> item.id("servers.library").gap(4.0F),
                currentArrayList.toArray(ComponentKeyService[]::new))
            .key("library"));
    int currentValue = this.enabled5 && this.value >= 760.0F ? 1 : 0;
    if (currentServer != null && currentValue == 0) {
      arrayList.add(this.createComponentKeyService(currentServer).key("detail"));
    }

    if (!this.serverViewTracker.saveError().isBlank()) {
      arrayList.add(
          createComponentKeyService8(
                  this.serverViewTracker.saveError(), MaterialIsLightService.ERROR)
              .key("save-error"));
    }

    return currentValue != 0
        ? ComponentBoxService.row(
            item ->
                item.id("servers.pane")
                    .size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.percent(100.0F))
                    .gap(16.0F),
            ComponentBoxService.column(
                    item ->
                        item.id("server-dashboard-scroll")
                            .height(LayoutOperationHandler.percent(100.0F))
                            .flex(1.0F)
                            .minWidth(0.0F)
                            .gap(16.0F)
                            .scrollable(true)
                            .clip(true)
                            .scrollbarWidth(3.0F)
                            .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                            .padding(4.0F, 8.0F, 16.0F, 0.0F),
                    arrayList.stream()
                        .map(
                            componentKey ->
                                componentKey.props(component -> component.flexShrink(0.0F)))
                        .toArray(ComponentKeyService[]::new))
                .key("scroll")
                .onMount(item -> this.scenePctService = item)
                .onUnmount(
                    item -> {
                      if (this.scenePctService == item) {
                        this.scenePctService = null;
                      }
                    }),
            ComponentBoxService.column(
                item ->
                    item.id("servers.inspector")
                        .width(LayoutOperationHandler.px(320.0F))
                        .height(LayoutOperationHandler.percent(100.0F))
                        .flexShrink(0.0F)
                        .scrollable(true)
                        .clip(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT),
                currentServer == null
                    ? ComponentBoxService.column(
                        item -> item.padding(24.0F).gap(12.0F),
                        MaterialTextService.icon("star", MaterialIsLightService.PRIMARY),
                        MaterialTextService.text(
                                "Make yourself at home", 22.0F, MaterialIsLightService.ON_SURFACE)
                            .props(item -> item.wordWrap(true)),
                        createComponentKeyService8(
                            "Choose a server to see its players, latency and your play history.",
                            MaterialIsLightService.ON_SURFACE_VARIANT))
                    : this.createComponentKeyService(currentServer).key("detail")))
        : ComponentBoxService.panel(
            item ->
                item.id("servers.pane")
                    .direction(ScenePctService.Direction.COLUMN)
                    .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                    .cornerRadius(28.0F)
                    .padding(this.enabled5 ? 0.0F : 16.0F)
                    .clip(true)
                    .backgroundColor(this.enabled5 ? 0 : MaterialIsLightService.SURFACE_LOW),
            ComponentBoxService.column(
                    item ->
                        item.id("server-dashboard-scroll")
                            .flex(1.0F)
                            .minHeight(0.0F)
                            .gap(this.enabled6 ? 8.0F : 20.0F)
                            .scrollable(true)
                            .clip(true)
                            .scrollbarWidth(3.0F)
                            .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                            .scrollInset(1.0F)
                            .padding(0.0F, 4.0F, 8.0F, 0.0F),
                    arrayList.stream()
                        .map(
                            componentKey ->
                                componentKey.props(component -> component.flexShrink(0.0F)))
                        .toArray(ComponentKeyService[]::new))
                .key("scroll")
                .onMount(item -> this.scenePctService = item)
                .onUnmount(
                    item -> {
                      if (this.scenePctService == item) {
                        this.scenePctService = null;
                      }
                    }));
  }

  private ComponentKeyService<?> createComponentKeyService(
      ServerListRenderer.Server currentServer) {
    ServerViewTracker.View currentView = this.serverViewTracker.view(currentServer.address());
    ServerQueryService.Status currentStatus = currentView.status();
    int value = currentView.reachable() && currentStatus != null ? 1 : 0;
    String currentText =
        currentView.checkedAt() == 0L
            ? "Checking server…"
            : (currentView.loading()
                ? "Refreshing…"
                : (value != 0
                    ? "Online · checked " + createText3(currentView.checkedAt())
                    : "Status unavailable · " + createText3(currentView.checkedAt())));
    String nextText =
        value != 0 && currentStatus.players() >= 0 ? createText(currentStatus.players()) : "—";
    String previousText =
        value != 0 && currentStatus.capacity() >= 0
            ? "of " + createText(currentStatus.capacity()) + " players"
            : "Players online";
    String sourceText =
        value != 0 && currentStatus.latencyMs() >= 0L ? currentStatus.latencyMs() + " ms" : "—";
    String targetText =
        value != 0 && !currentStatus.version().isBlank()
            ? currentStatus.version()
            : "Not available";
    CompositorEmptyService compositorEmpty =
        this.enabled2 ? currentView.ping() : currentView.players();
    String inputText = this.enabled2 ? "Latency" : "Players online";
    String outputText =
        compositorEmpty.validCount() == 0L
            ? "Waiting for a measurement"
            : (compositorEmpty.validCount() == 1L
                ? "First sample · collecting every 30s"
                : "Live samples · every 30s");
    ServerKeyService.Usage currentUsage = currentServer.usage();
    String resultText =
        currentUsage.joins() == 0
            ? "Playtime will appear after your first visit."
            : createText2(currentUsage.playedMillis())
                + " played · "
                + currentUsage.joins()
                + (currentUsage.joins() == 1 ? " visit" : " visits")
                + " · last played "
                + createText3(currentUsage.lastJoined());
    return ComponentBoxService.panel(
        item ->
            item.id("servers.detail")
                .direction(ScenePctService.Direction.COLUMN)
                .cornerRadius(20.0F)
                .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                .padding(16.0F)
                .gap(16.0F)
                .flexShrink(0.0F),
        ComponentBoxService.row(
            item -> item.gap(12.0F).align(ScenePctService.Align.CENTER),
            this.createComponentKeyService4(currentServer, 48.0F),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
                MaterialTextService.text(
                    currentServer.name(), 20.0F, MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                        currentServer.address(), 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.tooltip(currentServer.address()))),
            MaterialTextService.iconButton(
                "servers.copy",
                "content_copy",
                "Copy server address",
                () -> CompatSavedService.copyAddress(currentServer.address()))),
        createComponentKeyService8(
            currentText,
            value != 0
                ? MaterialIsLightService.TERTIARY
                : MaterialIsLightService.ON_SURFACE_VARIANT),
        MaterialTextService.button(
                "servers.preview",
                "Server preview",
                () -> {
                  if (this.text3 != null) {
                    this.text3.accept(currentServer.address(), currentServer.name());
                  }
                },
                false)
            .props(
                item ->
                    item.visible(this.text3 != null)
                        .tooltip(
                            "Inspect live status, graphs and the server's public player sample"
                                + " without joining.")),
        ComponentBoxService.row(
            item -> item.gap(8.0F),
            MaterialTextService.button(
                    "servers.join",
                    this.enabled3
                        ? (currentUsage.joins() > 0 ? "Play again" : "Join server")
                        : "In game",
                    () -> {
                      if (this.enabled3) {
                        this.text2.accept(currentServer.address(), currentServer.name());
                      }
                    },
                    this.enabled3
                        ? MaterialIsLightService.PRIMARY
                        : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.12F),
                    this.enabled3
                        ? MaterialIsLightService.ON_PRIMARY
                        : ScenePctService.mulAlpha(MaterialIsLightService.ON_SURFACE, 0.38F),
                    20.0F)
                .props(item -> item.available(this.enabled3).flex(1.0F)),
            MaterialTextService.iconButton(
                    "servers.refresh",
                    "refresh",
                    "Refresh server status",
                    () -> {
                      this.serverViewTracker.request(currentServer.address(), true);
                      this.updateState();
                    })
                .props(item -> item.available(!currentView.loading()))),
        ComponentBoxService.row(
            item -> item.gap(8.0F),
            this.createComponentKeyService5(nextText, previousText),
            this.createComponentKeyService5(sourceText, "Round-trip ping")),
        MaterialTextService.text(
                "Minecraft · " + targetText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.tooltip(targetText)),
        createComponentKeyService8(
                currentStatus == null
                    ? "Server details will appear here."
                    : (value == 0
                        ? "The server may be offline or have status disabled. You can still try"
                              + " joining."
                        : (currentStatus.motd().isBlank()
                            ? "This server has no description."
                            : currentStatus.motd())),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.maxLines(3)),
        ComponentBoxService.row(
            item -> item.gap(8.0F).align(ScenePctService.Align.CENTER),
            MaterialTextService.text(inputText, 14.0F, MaterialIsLightService.ON_SURFACE)
                .props(item -> item.flex(1.0F).minWidth(0.0F)),
            MaterialTextService.iconButton(
                "servers.graph.toggle",
                "swap_horiz",
                this.enabled2 ? "Show players" : "Show latency",
                () -> {
                  this.enabled2 = !this.enabled2;
                  this.updateState();
                })),
        ComponentBoxService.row(
            item -> item.gap(8.0F),
            ComponentBoxService.column(
                item ->
                    item.width(LayoutOperationHandler.px(36.0F))
                        .justify(ScenePctService.Justify.SPACE_BETWEEN),
                MaterialTextService.text(
                    createText((long) compositorEmpty.ceiling()),
                    12.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT),
                MaterialTextService.text("0", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)),
            ComponentBoxService.<SceneDataService>node(
                    "time-series",
                    SceneDataService::new,
                    item ->
                        item.data(compositorEmpty)
                            .color(
                                this.enabled2
                                    ? MaterialIsLightService.TERTIARY
                                    : MaterialIsLightService.PRIMARY)
                            .height(LayoutOperationHandler.px(80.0F))
                            .flex(1.0F)
                            .minWidth(0.0F))
                .key("graph")),
        ComponentBoxService.row(
            item -> item.gap(8.0F).align(ScenePctService.Align.CENTER),
            MaterialTextService.text(
                compositorEmpty.size() > 0
                    ? dateTimeFormatter.format(
                        Instant.ofEpochMilli(compositorEmpty.samples().getFirst().timestamp()))
                    : "",
                12.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT),
            MaterialTextService.text(outputText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    item ->
                        item.flex(1.0F)
                            .minWidth(0.0F)
                            .wordWrap(true)
                            .textAlign(SceneTextService.TextAlign.CENTER)),
            MaterialTextService.text(
                compositorEmpty.size() > 1
                    ? dateTimeFormatter.format(
                        Instant.ofEpochMilli(compositorEmpty.samples().getLast().timestamp()))
                    : "",
                12.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)),
        createComponentKeyService8(resultText, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                item ->
                    item.tooltip(
                        "Playtime and visits recorded locally by ellice for this account.")));
  }

  private ComponentKeyService<?> createComponentKeyService2(
      ServerListRenderer.Server currentServer, ServerListRenderer.Server nextServer) {
    ServerViewTracker.View currentView = this.serverViewTracker.view(currentServer.address());
    int currentValue =
        nextServer != null && nextServer.address().equals(currentServer.address()) ? 1 : 0;
    String currentText =
        currentView.checkedAt() == 0L
            ? "Checking…"
            : (currentView.reachable() && currentView.status() != null
                ? (currentView.status().players() < 0
                    ? "Online"
                    : createText(currentView.status().players()) + " online")
                : "Unavailable");
    int nextValue =
        currentValue != 0
            ? MaterialIsLightService.SECONDARY_CONTAINER
            : MaterialIsLightService.SURFACE_CONTAINER;
    int previousValue =
        currentValue != 0
            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
            : MaterialIsLightService.ON_SURFACE;
    return ComponentBoxService.<MaterialResponsiveService>node(
            "server-row",
            MaterialResponsiveService::new,
            item ->
                item.surface(nextValue)
                    .id("servers.row." + currentServer.address())
                    .height(LayoutOperationHandler.px(72.0F))
                    .cornerRadius(20.0F)
                    .direction(ScenePctService.Direction.ROW)
                    .align(ScenePctService.Align.CENTER)
                    .flexShrink(0.0F),
            ComponentBoxService.node(
                "server-select",
                MaterialJoinedService::new,
                item -> {
                  item.colors(0, previousValue).shape(16.0F, 72.0F);
                  item.id("servers.select." + currentServer.address())
                      .height(LayoutOperationHandler.px(72.0F))
                      .flex(1.0F)
                      .minWidth(0.0F)
                      .padding(12.0F)
                      .gap(12.0F)
                      .direction(ScenePctService.Direction.ROW)
                      .align(ScenePctService.Align.CENTER)
                      .tooltip(currentServer.name() + " · " + currentServer.address())
                      .onClick(
                          () -> {
                            this.text5 = currentServer.address();
                            this.updateState();
                            this.enabled7 = !(this.enabled5 && this.value >= 760.0F);
                          });
                },
                this.createComponentKeyService4(currentServer, 40.0F),
                ComponentBoxService.column(
                    item -> item.flex(1.0F).minWidth(0.0F),
                    MaterialTextService.text(currentServer.name(), 16.0F, previousValue),
                    MaterialTextService.text(
                            this.enabled4
                                ? currentText
                                : currentServer.address() + " · " + currentText,
                            12.0F,
                            currentValue != 0
                                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                                : MaterialIsLightService.ON_SURFACE_VARIANT)
                        .props(item -> item.tooltip(currentServer.address()))),
                ComponentBoxService.node(
                    "server-sparkline",
                    SceneDataService::new,
                    item ->
                        item.data(currentView.players())
                            .color(MaterialIsLightService.PRIMARY)
                            .size(72.0F, 28.0F)
                            .flexShrink(0.0F)
                            .pointerEvents(false)
                            .visible(
                                this.enabled5
                                    && this.value > 980.0F
                                    && currentView.players().validCount() > 1L))),
            this.createComponentKeyService3(currentServer))
        .key("server:" + currentServer.address());
  }

  private ComponentKeyService<?> createComponentKeyService3(ServerListRenderer.Server server) {
    boolean enabled = server.usage().favorite();
    return MaterialTextService.iconButton(
            "servers.favorite." + server.address(),
            enabled ? "star_filled" : "star",
            enabled ? "Remove from this account's favourites" : "Favourite for this account",
            () -> {
              this.serverViewTracker.toggleFavorite(this.uUID, server.address());
              this.updateState();
            })
        .children(
            ComponentBoxService.<MaterialSelectedSelector>node(
                    "material-selection-symbol",
                    MaterialSelectedSelector::new,
                    item ->
                        item.selected(enabled)
                            .src(MaterialTextService.iconPath(enabled ? "star_filled" : "star"))
                            .tintColor(
                                enabled
                                    ? MaterialIsLightService.PRIMARY
                                    : MaterialIsLightService.ON_SURFACE_VARIANT)
                            .size(24.0F, 24.0F)
                            .flexShrink(0.0F)
                            .pointerEvents(false)
                            .inheritEdgeSoftness(false))
                .key("symbol"))
        .props(item -> item.stopPropagation(true).marginRight(4.0F));
  }

  private ComponentKeyService<?> createComponentKeyService4(
      ServerListRenderer.Server currentServer, float value) {
    return ComponentBoxService.<SceneImageService>node(
            "server-icon",
            SceneImageService::new,
            item ->
                item.identity(currentServer.address(), currentServer.name())
                    .material(true)
                    .image(this.serverViewTracker.icon(currentServer.address()))
                    .size(value, value)
                    .flexShrink(0.0F)
                    .pointerEvents(false))
        .key("icon");
  }

  private ComponentKeyService<?> createComponentKeyService5(String currentText, String nextText) {
    return ComponentBoxService.column(
        item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
        MaterialTextService.text(currentText, 24.0F, MaterialIsLightService.ON_SURFACE),
        MaterialTextService.text(nextText, 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private ComponentKeyService<?> createComponentKeyService6(
      String text, ServerListRenderer.Filter currentFilter) {
    int value = this.renderer == currentFilter ? 1 : 0;
    int currentValue =
        value != 0
            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
            : MaterialIsLightService.ON_SURFACE_VARIANT;
    return ComponentBoxService.<MaterialJoinedService>node(
            "server-filter",
            MaterialJoinedService::new,
            item -> {
              item.colors(
                      value != 0
                          ? MaterialIsLightService.SECONDARY_CONTAINER
                          : MaterialIsLightService.SURFACE_CONTAINER,
                      currentValue)
                  .shape(20.0F, 40.0F)
                  .joined(
                      currentFilter == ServerListRenderer.Filter.ALL,
                      currentFilter == ServerListRenderer.Filter.RECENT,
                      value != 0);
              item.id("servers.filter." + currentFilter.name().toLowerCase(Locale.ROOT))
                  .height(LayoutOperationHandler.px(48.0F))
                  .flex(1.0F)
                  .minWidth(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .onClick(
                      () -> {
                        this.renderer = currentFilter;
                        this.updateState();
                        this.updateState2();
                      });
            },
            MaterialTextService.label(text, currentValue))
        .key(currentFilter.name());
  }

  private ComponentKeyService<?> createComponentKeyService7() {
    return ComponentBoxService.panel(
            item ->
                item.id("servers.add-form")
                    .direction(ScenePctService.Direction.COLUMN)
                    .cornerRadius(20.0F)
                    .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                    .padding(16.0F)
                    .gap(16.0F)
                    .flexShrink(0.0F),
            MaterialTextService.text(
                "Add to your servers", 20.0F, MaterialIsLightService.ON_SURFACE),
            ComponentBoxService.<ControlLetterSpacingService>node(
                    "server-address",
                    ControlLetterSpacingService::new,
                    item -> {
                      MaterialTextService.input(item);
                      item.materialError(!this.text8.isBlank());
                      item.id("servers.address")
                          .height(LayoutOperationHandler.px(56.0F))
                          .placeholder("play.example.com:25565")
                          .maxLength(255)
                          .onChanged(text -> this.text7 = text)
                          .onSubmit(text -> this.updateState3());
                      if (!item.focused()) {
                        item.text(this.text7);
                      }
                    })
                .key("address"),
            createComponentKeyService8(
                this.text8.isBlank()
                    ? "Saved to Minecraft and favourited for " + this.text4 + "."
                    : this.text8,
                this.text8.isBlank()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR),
            ComponentBoxService.row(
                item -> item.gap(8.0F),
                MaterialTextService.button("servers.save", "Save server", this::updateState3, true),
                MaterialTextService.button(
                    "servers.cancel",
                    "Cancel",
                    () -> {
                      this.enabled = false;
                      this.updateState();
                    },
                    0,
                    MaterialIsLightService.PRIMARY,
                    20.0F)))
        .onMount(item -> MaterialEnterService.enter(item, 0.0F, 12.0F));
  }

  private static ComponentKeyService<SceneTextService> createComponentKeyService8(
      String currentText, int value) {
    return MaterialTextService.text(currentText, 14.0F, value).props(item -> item.wordWrap(true));
  }

  private void updateState3() {
    try {
      String text = ParsedServerEndpoint.parse(this.text7).address();
      CompatSavedService.add(text);
      this.serverViewTracker.reloadSaved();
      if (!this.serverViewTracker.library().usage(this.uUID, text).favorite()) {
        this.serverViewTracker.toggleFavorite(this.uUID, text);
      }

      this.text5 = text;
      this.text6 = "";
      this.renderer = ServerListRenderer.Filter.ALL;
      this.enabled = false;
      this.text7 = "";
      this.text8 = "";
    } catch (IllegalArgumentException illegalArgumentException) {
      this.text8 = illegalArgumentException.getMessage();
    } catch (RuntimeException exception) {
      this.text8 = "Couldn't save this server. Please try again.";
    }

    this.updateState();
  }

  private List<ServerListRenderer.Server> collectValues() {
    LinkedHashMap linkedHashMap = new LinkedHashMap();

    for (CompatSavedService.Saved currentSaved : this.serverViewTracker.saved()) {
      linkedHashMap.put(
          currentSaved.address(),
          new ServerListRenderer.Server(
              currentSaved.address(),
              currentSaved.name(),
              this.serverViewTracker.library().usage(this.uUID, currentSaved.address())));
    }

    this.serverViewTracker
        .library()
        .history(this.uUID)
        .forEach(
            (item, currentItem) ->
                linkedHashMap.putIfAbsent(
                    item,
                    new ServerListRenderer.Server(
                        item, this.serverViewTracker.library().name(item), currentItem)));
    return linkedHashMap.values().stream()
        .sorted(
            Comparator.<ServerListRenderer.Server, Boolean>comparing(
                    item -> !item.usage().favorite())
                .thenComparing(
                    Comparator.<ServerListRenderer.Server>comparingLong(
                            item -> item.usage().lastJoined())
                        .reversed())
                .thenComparing(ServerListRenderer.Server::name, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  private List<ServerListRenderer.Server> collectValues2() {
    String text = this.text6.strip().toLowerCase(Locale.ROOT);
    Stream currentStream =
        this.collectValues().stream()
            .filter(
                item ->
                    this.renderer != ServerListRenderer.Filter.FAVORITES || item.usage().favorite())
            .filter(
                item ->
                    this.renderer != ServerListRenderer.Filter.RECENT || item.usage().joins() > 0)
            .filter(
                item ->
                    item.name().toLowerCase(Locale.ROOT).contains(text)
                        || item.address().toLowerCase(Locale.ROOT).contains(text));
    if (this.renderer == ServerListRenderer.Filter.RECENT) {
      currentStream =
          currentStream.sorted(
              Comparator.<ServerListRenderer.Server>comparingLong(item -> item.usage().lastJoined())
                  .reversed());
    }

    return currentStream.toList();
  }

  private ServerListRenderer.Server createServer(List<ServerListRenderer.Server> items) {
    return items.stream()
        .filter(item -> item.address().equals(this.text5))
        .findFirst()
        .orElse(items.isEmpty() ? null : (ServerListRenderer.Server) items.getFirst());
  }

  private static String createText(long size) {
    return NumberFormat.getIntegerInstance(Locale.US).format(size);
  }

  private static String createText2(long size) {
    long currentSize = size / 60000L;
    return currentSize < 1L
        ? "<1m"
        : (currentSize < 60L
            ? currentSize + "m"
            : currentSize / 60L + "h " + currentSize % 60L + "m");
  }

  private static String createText3(long size) {
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

  private enum Filter {
    ALL,
    FAVORITES,
    RECENT;

    private static ServerListRenderer.Filter[] $values() {
      return new ServerListRenderer.Filter[] {ALL, FAVORITES, RECENT};
    }
  }

  private record Server(String address, String name, ServerKeyService.Usage usage) {}
}
