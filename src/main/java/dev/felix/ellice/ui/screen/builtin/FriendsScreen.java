package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.friends.FriendProfile;
import dev.felix.ellice.friends.FriendsData;
import dev.felix.ellice.friends.FriendsMode;
import dev.felix.ellice.friends.FriendsStateController;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FriendsScreen implements ScreenOperationHandler {
  private final FriendsStateController friendsStateController;
  private final Supplier<List<FriendsData>> items;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private final Map<UUID, String> text2 = new HashMap<>();
  private ScreenScreenIdService screenScreenIdService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private List<FriendsData> items2 = List.of();
  private UUID uUID;
  private FriendProfile friendsData2;
  private long timestamp = -1L;
  private float value = 1120.0F;
  private float value2 = 720.0F;
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private boolean enabled5;
  private String text3 = "";
  private String text4 = "";
  private String text5 = "";
  private String text6 = "";
  private boolean enabled6;

  public FriendsScreen(
      FriendsStateController currentFriendsStateController, Supplier<List<FriendsData>> supplier) {
    this.friendsStateController = Objects.requireNonNull(currentFriendsStateController);
    this.items = Objects.requireNonNull(supplier);
  }

  @Override
  public String id() {
    return "friends";
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
    this.items2 = List.copyOf(this.items.get());
    this.friendsStateController.resolve(this.items2);
    this.timestamp = this.friendsStateController.revision();
    if (this.uUID == null && !this.friendsStateController.entries().isEmpty()) {
      this.uUID = this.friendsStateController.entries().getFirst().id();
    }

    this.materialTerrainTooltipsService =
        new MaterialTerrainTooltipsService().terrainTooltips(true);
    this.materialTerrainTooltipsService
        .id("friends.root")
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
    return this.value < 760.0F;
  }

  private boolean checkCondition2() {
    return this.value2 < 430.0F;
  }

  private void updateState() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private boolean checkCondition3(FriendProfile friendProfile) {
    return this.items2.stream().anyMatch(item -> friendProfile.matches(item.uuid(), item.name()));
  }

  private void updateState2(FriendProfile friendProfile) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.uUID = friendProfile.id();
    this.enabled2 = true;
    this.updateState();
  }

  private List<FriendProfile> collectValues() {
    String text = this.text3.strip().toLowerCase(Locale.ROOT);
    return this.friendsStateController.entries().stream()
        .filter(item -> !this.enabled4 || item.favorite())
        .filter(
            item ->
                (item.displayName()
                        + " "
                        + item.note()
                        + " "
                        + (item.uuid() == null ? "" : item.uuid()))
                    .toLowerCase(Locale.ROOT)
                    .contains(text))
        .sorted(
            Comparator.<FriendProfile, Boolean>comparing(item -> item.favorite())
                .reversed()
                .thenComparing(item -> !this.checkCondition3(item))
                .thenComparing(FriendProfile::displayName, String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
    FriendProfile friendProfile =
        this.uUID == null ? null : this.friendsStateController.get(this.uUID);
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (!this.checkCondition() || !this.enabled2 || friendProfile == null) {
      arrayList.add(this.createComponentKeyService3());
    }

    if (!this.checkCondition() || this.enabled2 && friendProfile != null) {
      arrayList.add(
          friendProfile == null
              ? this.createComponentKeyService8()
              : this.createComponentKeyService9(friendProfile));
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
                    .pointerEvents(!this.enabled5),
            ComponentBoxService.panel(
                    item ->
                        item.id("friends.workspace")
                            .size(
                                LayoutOperationHandler.percent(100.0F),
                                LayoutOperationHandler.percent(100.0F))
                            .maxWidth(1120.0F)
                            .maxHeight(860.0F)
                            .backgroundColor(MaterialIsLightService.SURFACE)
                            .cornerRadius(this.value < 600.0F ? 0.0F : 28.0F)
                            .direction(ScenePctService.Direction.COLUMN)
                            .clip(true),
                    this.createComponentKeyService2(),
                    ComponentBoxService.row(
                        item ->
                            item.id("friends.panes")
                                .width(LayoutOperationHandler.percent(100.0F))
                                .flex(1.0F)
                                .minHeight(0.0F)
                                .gap(16.0F)
                                .padding(0.0F, this.checkCondition() ? 12.0F : 24.0F)
                                .align(ScenePctService.Align.STRETCH),
                        arrayList.toArray(ComponentKeyService[]::new)),
                    this.createComponentKeyService13())
                .key("workspace")
                .onMount(item -> MaterialEnterService.enter(item, 0.0F, 18.0F)));
    return ComponentBoxService.stack(
        item ->
            item.size(
                LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F)),
        currentSize,
        this.enabled5
            ? this.createComponentKeyService14()
            : ComponentBoxService.box(item -> item.visible(false)).key("add-dialog"));
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    return ComponentBoxService.row(
        item ->
            item.height(LayoutOperationHandler.px(this.checkCondition2() ? 52.0F : 80.0F))
                .padding(4.0F, this.checkCondition() ? 8.0F : 16.0F)
                .gap(8.0F)
                .align(ScenePctService.Align.CENTER)
                .flexShrink(0.0F),
        MaterialTextService.iconButton(
            "friends.back",
            "arrow_back",
            this.checkCondition() && this.enabled2 ? "Back to friends" : "Back to ClickGUI",
            this::updateState8),
        ComponentBoxService.column(
            item -> item.flex(1.0F).minWidth(0.0F),
            MaterialTextService.text(
                "Friends",
                this.checkCondition() ? 24.0F : 28.0F,
                MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                    "Your people. Your rules.", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.visible(!this.checkCondition2() && !this.checkCondition()))),
        MaterialTextService.iconButton(
                "friends.add", "person_add", "Add a friend by name or UUID", this::updateState3)
            .props(
                item ->
                    item.colors(
                        MaterialIsLightService.PRIMARY_CONTAINER,
                        MaterialIsLightService.ON_PRIMARY_CONTAINER)));
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    ArrayList arrayList = new ArrayList();
    if (this.enabled3) {
      for (FriendsData friendsData :
          this.items2.stream()
              .filter(
                  item ->
                      item.name()
                          .toLowerCase(Locale.ROOT)
                          .contains(this.text3.strip().toLowerCase(Locale.ROOT)))
              .toList()) {
        arrayList.add(this.createComponentKeyService7(friendsData));
      }

      if (arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService11(
                "No players here",
                this.items2.isEmpty()
                    ? "Join a server to add players from its player list."
                    : "Try another player name.",
                "person"));
      }
    } else {
      for (FriendProfile friendProfile : this.collectValues()) {
        arrayList.add(this.createComponentKeyService6(friendProfile));
      }

      if (arrayList.isEmpty()) {
        arrayList.add(
            this.createComponentKeyService11(
                this.friendsStateController.entries().isEmpty()
                    ? "Keep your friends close"
                    : "No matching friends",
                this.friendsStateController.entries().isEmpty()
                    ? "Add a name, or find someone on your current server."
                    : "Try another search or turn off the star filter.",
                "friends"));
      }
    }

    return ComponentBoxService.column(
            item ->
                item.id("friends.directory")
                    .width(
                        this.checkCondition()
                            ? LayoutOperationHandler.percent(100.0F)
                            : LayoutOperationHandler.px(320.0F))
                    .flex(this.checkCondition() ? 1.0F : 0.0F)
                    .minWidth(0.0F)
                    .minHeight(0.0F)
                    .gap(8.0F)
                    .flexShrink(0.0F),
            ComponentBoxService.row(
                item -> item.gap(4.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
                this.createComponentKeyService4(
                    "saved",
                    "Saved · " + this.friendsStateController.entries().size(),
                    !this.enabled3,
                    () -> this.enabled3 = false),
                this.createComponentKeyService4(
                    "server",
                    "On server · " + this.items2.size(),
                    this.enabled3,
                    () -> this.enabled3 = true)),
            ComponentBoxService.row(
                item -> item.gap(4.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
                MaterialTextService.search(
                        "friends.search",
                        "Search people or notes",
                        this.text3,
                        item -> {
                          this.text3 = item;
                          this.updateState();
                        },
                        true)
                    .props(item -> item.flex(1.0F).minWidth(0.0F)),
                MaterialTextService.iconButton(
                        "friends.favorites",
                        this.enabled4 ? "star_filled" : "star",
                        "Show favorite friends",
                        () -> {
                          this.enabled4 = !this.enabled4;
                          this.updateState();
                        })
                    .props(
                        item -> {
                          item.visible(!this.enabled3).size(40.0F, 40.0F);
                          item.colors(
                              this.enabled4 ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                              MaterialIsLightService.PRIMARY);
                        })),
            ComponentBoxService.column(
                item ->
                    item.id("friends.list")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .flex(1.0F)
                        .minHeight(0.0F)
                        .scrollable(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE)
                        .clip(true)
                        .gap(6.0F)
                        .padding(2.0F, 4.0F, 8.0F, 0.0F),
                arrayList.toArray(ComponentKeyService[]::new)))
        .key("directory");
  }

  private ComponentKeyService<?> createComponentKeyService4(
      String currentText, String nextText, boolean enabled, Runnable runnable) {
    return MaterialTextService.button(
            "friends.tab." + currentText,
            nextText,
            () -> {
              this.materialKeyService.clear(this.materialTerrainTooltipsService);
              runnable.run();
              this.updateState();
            },
            false)
        .props(
            item -> {
              item.colors(
                      enabled ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                      enabled
                          ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                          : MaterialIsLightService.ON_SURFACE_VARIANT)
                  .shape(16.0F, 36.0F);
              item.flex(1.0F)
                  .minWidth(0.0F)
                  .height(LayoutOperationHandler.px(40.0F))
                  .padding(0.0F, 8.0F);
            })
        .children(
            MaterialTextService.text(
                nextText,
                12.0F,
                enabled
                    ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                    : MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private ComponentKeyService<?> createComponentKeyService5(
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
            currentText.isEmpty() ? "?" : currentText.substring(0, 1).toUpperCase(Locale.ROOT),
            value >= 52 ? 26.0F : 18.0F,
            enabled
                ? MaterialIsLightService.ON_PRIMARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT));
  }

  private ComponentKeyService<?> createComponentKeyService6(FriendProfile friendProfile) {
    boolean enabled = friendProfile.id().equals(this.uUID);
    boolean currentEnabled = this.checkCondition3(friendProfile);
    return MaterialTextService.button(
            "friends.row." + friendProfile.id(),
            friendProfile.displayName(),
            () -> this.updateState2(friendProfile),
            false)
        .props(
            item -> {
              item.colors(
                      enabled
                          ? MaterialIsLightService.SECONDARY_CONTAINER
                          : MaterialIsLightService.SURFACE_CONTAINER,
                      MaterialIsLightService.ON_SURFACE)
                  .shape(18.0F, 0.0F);
              item.width(LayoutOperationHandler.percent(100.0F))
                  .height(LayoutOperationHandler.px(72.0F))
                  .padding(10.0F, 12.0F)
                  .gap(12.0F)
                  .justify(ScenePctService.Justify.START);
              item.tooltip(
                  friendProfile.note().isEmpty()
                      ? "Edit module exclusions for " + friendProfile.displayName()
                      : friendProfile.note());
            })
        .children(
            this.createComponentKeyService5(friendProfile.displayName(), currentEnabled, 40),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
                MaterialTextService.label(
                    friendProfile.displayName(), MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                    currentEnabled
                        ? "On this server"
                        : (friendProfile.uuid() == null
                            ? "Name saved · UUID pending"
                            : "Saved friend"),
                    11.0F,
                    currentEnabled
                        ? MaterialIsLightService.PRIMARY
                        : MaterialIsLightService.ON_SURFACE_VARIANT)),
            MaterialTextService.icon(
                    friendProfile.favorite() ? "star_filled" : "chevron_right",
                    friendProfile.favorite()
                        ? MaterialIsLightService.PRIMARY
                        : MaterialIsLightService.OUTLINE)
                .props(item -> item.size(18.0F, 18.0F)))
        .onMount(item -> MaterialEnterService.enter(item, 8.0F, 0.0F));
  }

  private ComponentKeyService<?> createComponentKeyService7(FriendsData friendsData) {
    FriendProfile friendProfile =
        this.friendsStateController.find(friendsData.uuid(), friendsData.name());
    return MaterialTextService.button(
            "friends.player." + friendsData.uuid(),
            friendsData.name(),
            () -> {
              if (friendProfile != null) {
                this.updateState2(friendProfile);
              } else {
                this.updateState6(
                    () -> {
                      FriendProfile addedFriend =
                          this.friendsStateController.add(friendsData.uuid(), friendsData.name());
                      this.updateState2(addedFriend);
                    },
                    "Friend added · combat protection enabled");
              }
            },
            false)
        .props(
            item -> {
              item.colors(
                      MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE)
                  .shape(18.0F, 0.0F);
              item.width(LayoutOperationHandler.percent(100.0F))
                  .height(LayoutOperationHandler.px(72.0F))
                  .padding(10.0F, 12.0F)
                  .gap(12.0F)
                  .justify(ScenePctService.Justify.START);
            })
        .children(
            this.createComponentKeyService5(friendsData.name(), true, 40),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
                MaterialTextService.label(friendsData.name(), MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                    friendProfile == null ? "Add with combat protection" : "Already a friend",
                    11.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT)),
            MaterialTextService.icon(
                    friendProfile == null ? "person_add" : "check", MaterialIsLightService.PRIMARY)
                .props(item -> item.size(20.0F, 20.0F)));
  }

  private ComponentKeyService<?> createComponentKeyService8() {
    return ComponentBoxService.panel(
        item ->
            item.id("friends.welcome")
                .flex(1.0F)
                .minWidth(0.0F)
                .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                .cornerRadius(24.0F)
                .direction(ScenePctService.Direction.COLUMN)
                .align(ScenePctService.Align.CENTER)
                .justify(ScenePctService.Justify.CENTER)
                .padding(24.0F)
                .gap(12.0F),
        MaterialTextService.icon("friends", MaterialIsLightService.PRIMARY)
            .props(item -> item.size(64.0F, 64.0F)),
        MaterialTextService.text(
                "A place for your people", 24.0F, MaterialIsLightService.ON_SURFACE)
            .props(item -> item.wordWrap(true)),
        MaterialTextService.text(
                "Choose a friend to adjust their module exclusions, pin them as a favorite or leave"
                    + " a private note.",
                14.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.maxWidth(340.0F).wordWrap(true)),
        MaterialTextService.button(
            "friends.welcome.add", "Add your first friend", this::updateState3, true));
  }

  private ComponentKeyService<?> createComponentKeyService9(FriendProfile friendProfile) {
    boolean enabled = this.checkCondition3(friendProfile);
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(
        ComponentBoxService.row(
            item -> item.gap(12.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
            this.createComponentKeyService5(friendProfile.displayName(), enabled, 56),
            ComponentBoxService.column(
                item -> item.flex(1.0F).minWidth(0.0F),
                MaterialTextService.text(
                    friendProfile.displayName(), 22.0F, MaterialIsLightService.ON_SURFACE),
                MaterialTextService.text(
                    enabled ? "On this server" : "Saved locally",
                    12.0F,
                    enabled
                        ? MaterialIsLightService.PRIMARY
                        : MaterialIsLightService.ON_SURFACE_VARIANT)),
            MaterialTextService.iconButton(
                    "friends.favorite",
                    friendProfile.favorite() ? "star_filled" : "star",
                    friendProfile.favorite() ? "Unpin this friend" : "Pin this friend to the top",
                    () ->
                        this.updateState7(
                            friendProfile.favorite(!friendProfile.favorite()), "Favorite updated"))
                .props(item -> item.size(40.0F, 40.0F))));
    arrayList.add(
        MaterialTextService.text(
                friendProfile.uuid() == null
                    ? "UUID resolves when this player is on your server."
                    : friendProfile.uuid().toString(),
                11.0F,
                MaterialIsLightService.OUTLINE)
            .props(
                item ->
                    item.id("friends.identity")
                        .flexShrink(0.0F)
                        .wordWrap(true)
                        .tooltip(
                            friendProfile.uuid() == null
                                ? "This entry currently matches the player name."
                                : "Matched by UUID, even if this player changes their name.")));
    arrayList.add(MaterialTextService.divider());
    arrayList.add(
        ComponentBoxService.column(
            item -> item.gap(2.0F).flexShrink(0.0F),
            MaterialTextService.text("Module exclusions", 20.0F, MaterialIsLightService.ON_SURFACE),
            MaterialTextService.text(
                "Checked modules skip this friend.",
                13.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)));
    arrayList.add(
        ComponentBoxService.row(
            item -> item.gap(6.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
            MaterialTextService.button(
                    "friends.defaults",
                    "Combat defaults",
                    () ->
                        this.updateState7(
                            friendProfile.excluded(FriendsMode.defaults()),
                            "Combat protection restored"),
                    false)
                .props(
                    item -> {
                      item.shape(14.0F, 32.0F);
                      item.height(LayoutOperationHandler.px(36.0F))
                          .padding(0.0F, 12.0F)
                          .tooltip(
                              "Exclude from all five combat modules; keep visual modules visible.");
                    })
                .children(
                    MaterialTextService.text(
                        "Combat defaults", 12.0F, MaterialIsLightService.ON_SECONDARY_CONTAINER)),
            MaterialTextService.button(
                    "friends.clear",
                    "Clear",
                    () ->
                        this.updateState7(
                            friendProfile.excluded(Set.of()), "All exclusions cleared"),
                    false)
                .props(
                    item -> {
                      item.colors(0, MaterialIsLightService.ON_SURFACE_VARIANT).shape(14.0F, 32.0F);
                      item.height(LayoutOperationHandler.px(36.0F)).padding(0.0F, 10.0F);
                    })
                .children(
                    MaterialTextService.text(
                        "Clear", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT))));

    for (boolean currentEnabled : new boolean[] {true, false}) {
      arrayList.add(
          MaterialTextService.text(
                  currentEnabled ? "COMBAT" : "VISUALS & INTEL",
                  11.0F,
                  MaterialIsLightService.OUTLINE)
              .props(item -> item.margin(8.0F, 4.0F, 0.0F, 4.0F).flexShrink(0.0F)));
      ArrayList<ComponentKeyService<?>> currentArrayList = new ArrayList<>();

      for (FriendsMode friendsMode : FriendsMode.values()) {
        if (friendsMode.combat == currentEnabled) {
          currentArrayList.add(this.createComponentKeyService10(friendProfile, friendsMode));
        }
      }

      arrayList.add(
          ComponentBoxService.column(
              item -> item.width(LayoutOperationHandler.percent(100.0F)).gap(2.0F).flexShrink(0.0F),
              currentArrayList.toArray(ComponentKeyService[]::new)));
    }

    arrayList.add(MaterialTextService.divider());
    arrayList.add(
        MaterialTextService.label("Private note", MaterialIsLightService.ON_SURFACE)
            .props(item -> item.flexShrink(0.0F)));
    String currentText = this.text2.getOrDefault(friendProfile.id(), friendProfile.note());
    arrayList.add(
        this.createComponentKeyService12(
            "friends.note",
            "How do you know them?",
            currentText,
            240,
            item -> {
              this.text2.put(friendProfile.id(), item);
              this.updateState();
            }));
    arrayList.add(
        ComponentBoxService.row(
            item -> item.gap(8.0F).align(ScenePctService.Align.CENTER).flexShrink(0.0F),
            MaterialTextService.button(
                    "friends.note.save",
                    "Save note",
                    () ->
                        this.updateState6(
                            () -> {
                              this.friendsStateController.update(
                                  this.friendsStateController
                                      .get(friendProfile.id())
                                      .note(
                                          this.text2.getOrDefault(
                                              friendProfile.id(), friendProfile.note())));
                              this.text2.remove(friendProfile.id());
                            },
                            "Note saved"),
                    false)
                .props(item -> item.available(!currentText.equals(friendProfile.note()))),
            ComponentBoxService.box(item -> item.flex(1.0F)),
            MaterialTextService.iconButton(
                "friends.remove",
                "delete",
                "Remove friend · can be undone",
                () ->
                    this.updateState6(
                        () -> {
                          this.friendsStateController.remove(friendProfile.id());
                          this.friendsData2 = friendProfile;
                          this.uUID = null;
                          this.enabled2 = false;
                        },
                        "Removed " + friendProfile.displayName()))));
    return ComponentBoxService.panel(
            item ->
                item.direction(ScenePctService.Direction.COLUMN)
                    .id("friends.editor")
                    .flex(1.0F)
                    .minWidth(0.0F)
                    .minHeight(0.0F)
                    .padding(this.checkCondition() ? 14.0F : 20.0F)
                    .gap(12.0F)
                    .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                    .cornerRadius(24.0F)
                    .scrollable(true)
                    .scrollbarWidth(3.0F)
                    .scrollbarColor(MaterialIsLightService.OUTLINE)
                    .clip(true),
            arrayList.stream()
                .map(componentKey -> componentKey.props(component -> component.flexShrink(0.0F)))
                .toArray(ComponentKeyService[]::new))
        .key("editor:" + friendProfile.id())
        .onMount(
            item -> MaterialEnterService.enter(item, this.checkCondition() ? 18.0F : 10.0F, 0.0F));
  }

  private ComponentKeyService<?> createComponentKeyService10(
      FriendProfile friendProfile, FriendsMode friendsMode) {
    boolean enabled = friendProfile.excluded().contains(friendsMode);
    return MaterialTextService.button(
            "friends.exclude." + friendsMode.name(),
            friendsMode.label,
            () -> {
              FriendProfile currentProfile = this.friendsStateController.get(friendProfile.id());
              EnumSet enumSet = EnumSet.noneOf(FriendsMode.class);
              enumSet.addAll(currentProfile.excluded());
              if (!enumSet.remove(friendsMode)) {
                enumSet.add(friendsMode);
              }

              this.updateState7(
                  currentProfile.excluded(enumSet),
                  friendsMode.label
                      + (enumSet.contains(friendsMode) ? " will skip " : " will include ")
                      + currentProfile.displayName());
            },
            false)
        .props(
            item -> {
              item.colors(
                      enabled
                          ? MaterialIsLightService.SECONDARY_CONTAINER
                          : MaterialIsLightService.SURFACE_CONTAINER,
                      MaterialIsLightService.ON_SURFACE)
                  .shape(12.0F, 40.0F);
              item.width(LayoutOperationHandler.percent(100.0F))
                  .height(LayoutOperationHandler.px(44.0F))
                  .padding(0.0F, 12.0F)
                  .gap(12.0F)
                  .justify(ScenePctService.Justify.START)
                  .tooltip(friendsMode.hint);
            })
        .children(
            MaterialTextService.icon(
                    enabled ? "check_box" : "check_box_outline_blank",
                    enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE)
                .props(item -> item.size(22.0F, 22.0F)),
            MaterialTextService.text(friendsMode.label, 14.0F, MaterialIsLightService.ON_SURFACE)
                .props(item -> item.flex(1.0F).minWidth(0.0F)),
            MaterialTextService.text(
                enabled ? "Excluded" : "Included",
                11.0F,
                enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.OUTLINE));
  }

  private ComponentKeyService<?> createComponentKeyService11(
      String currentText, String nextText, String previousText) {
    return ComponentBoxService.column(
        item ->
            item.width(LayoutOperationHandler.percent(100.0F))
                .padding(this.checkCondition2() ? 12.0F : 24.0F)
                .gap(10.0F)
                .align(ScenePctService.Align.CENTER),
        MaterialTextService.icon(previousText, MaterialIsLightService.PRIMARY)
            .props(item -> item.size(36.0F, 36.0F)),
        MaterialTextService.text(currentText, 18.0F, MaterialIsLightService.ON_SURFACE)
            .props(item -> item.wordWrap(true)),
        MaterialTextService.text(nextText, 13.0F, MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.wordWrap(true)));
  }

  private ComponentKeyService<?> createComponentKeyService12(
      String currentText,
      String nextText,
      String previousText,
      int value,
      Consumer<String> consumer) {
    return ComponentBoxService.<ControlLetterSpacingService>node(
            "friend-input",
            ControlLetterSpacingService::new,
            item -> {
              MaterialTextService.input(item);
              item.id(currentText)
                  .width(LayoutOperationHandler.percent(100.0F))
                  .height(LayoutOperationHandler.px(48.0F))
                  .flexShrink(0.0F)
                  .minWidth(0.0F)
                  .placeholder(nextText)
                  .maxLength(value)
                  .stopPropagation(true)
                  .onChanged(consumer);
              if (!item.focused()) {
                item.text(previousText);
              }
            })
        .key(currentText);
  }

  private ComponentKeyService<?> createComponentKeyService13() {
    String currentText =
        !this.friendsStateController.notice().isEmpty()
            ? this.friendsStateController.notice()
            : (this.text5.isEmpty() ? "Private to this client · saved across configs" : this.text5);
    return ComponentBoxService.row(
        item ->
            item.padding(4.0F, this.checkCondition() ? 12.0F : 24.0F)
                .gap(8.0F)
                .minHeight(this.checkCondition2() ? 36.0F : 48.0F)
                .align(ScenePctService.Align.CENTER)
                .flexShrink(0.0F),
        MaterialTextService.text(
                currentText,
                11.0F,
                !this.enabled6 && this.friendsStateController.notice().isEmpty()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR)
            .props(
                item -> item.id("friends.notice").flex(1.0F).minWidth(0.0F).tooltip(currentText)),
        MaterialTextService.button(
                "friends.undo",
                "Undo",
                () ->
                    this.updateState6(
                        () -> {
                          this.friendsStateController.restore(this.friendsData2);
                          this.updateState2(this.friendsData2);
                          this.friendsData2 = null;
                        },
                        "Friend restored"),
                false)
            .props(
                item -> {
                  item.visible(this.friendsData2 != null).height(LayoutOperationHandler.px(36.0F));
                  item.shape(14.0F, 32.0F);
                }));
  }

  private void updateState3() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.enabled5 = true;
    this.text4 = "";
    this.text6 = "";
    this.updateState();
  }

  private void updateState4() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    this.enabled5 = false;
    this.updateState();
  }

  private void updateState5() {
    try {
      FriendProfile friendProfile = this.friendsStateController.add(this.text4, this.items2);
      this.updateState4();
      this.updateState2(friendProfile);
      this.text5 = "Friend saved · choose their module exclusions below";
      this.enabled6 = false;
    } catch (IOException | IllegalArgumentException iOExceptionIllegalArgumentException) {
      this.text6 =
          iOExceptionIllegalArgumentException instanceof IOException
              ? "Could not save friends. " + this.friendsStateController.notice()
              : iOExceptionIllegalArgumentException.getMessage();
    }

    this.updateState();
  }

  private ComponentKeyService<?> createComponentKeyService14() {
    return ComponentBoxService.panel(
            item ->
                item.id("friends.add.scrim")
                    .size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.percent(100.0F))
                    .layerBreak(true)
                    .padding(16.0F)
                    .backgroundColor(-1610612736)
                    .direction(ScenePctService.Direction.COLUMN)
                    .align(ScenePctService.Align.CENTER)
                    .justify(ScenePctService.Justify.CENTER)
                    .interactive(true)
                    .onClick(this::updateState4)
                    .stopPropagation(true),
            ComponentBoxService.panel(
                item ->
                    item.id("friends.add.dialog")
                        .width(LayoutOperationHandler.percent(100.0F))
                        .maxWidth(460.0F)
                        .maxHeight(Math.max(120.0F, this.value2 - 32.0F))
                        .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
                        .cornerRadius(28.0F)
                        .padding(this.checkCondition2() ? 12.0F : 24.0F)
                        .gap(this.checkCondition2() ? 6.0F : 12.0F)
                        .direction(ScenePctService.Direction.COLUMN)
                        .scrollable(true)
                        .scrollbarWidth(3.0F)
                        .clip(true)
                        .interactive(true)
                        .stopPropagation(true),
                MaterialTextService.text("Add a friend", 24.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.flexShrink(0.0F)),
                MaterialTextService.text(
                        "Enter their player name or UUID. Players on this server are identified"
                            + " immediately.",
                        14.0F,
                        MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(
                        item ->
                            item.wordWrap(true).flexShrink(0.0F).visible(!this.checkCondition2())),
                this.createComponentKeyService12(
                    "friends.add.input",
                    "Player name or UUID",
                    this.text4,
                    36,
                    item -> {
                      this.text4 = item;
                      this.text6 = "";
                      this.updateState();
                    }),
                MaterialTextService.text(
                        this.text6.isEmpty()
                            ? (this.checkCondition2()
                                ? "Combat protection starts enabled."
                                : "New friends start with combat protection enabled.")
                            : this.text6,
                        12.0F,
                        this.text6.isEmpty()
                            ? MaterialIsLightService.ON_SURFACE_VARIANT
                            : MaterialIsLightService.ERROR)
                    .props(item -> item.id("friends.add.notice").wordWrap(true).flexShrink(0.0F)),
                ComponentBoxService.row(
                    item -> item.gap(8.0F).justify(ScenePctService.Justify.END).flexShrink(0.0F),
                    MaterialTextService.button(
                        "friends.add.cancel", "Cancel", this::updateState4, false),
                    MaterialTextService.button(
                            "friends.add.confirm", "Add friend", this::updateState5, true)
                        .props(item -> item.available(!this.text4.isBlank())))))
        .key("add-dialog")
        .onMount(item -> MaterialEnterService.enter(item, 0.0F, 12.0F));
  }

  private void updateState6(FriendsScreen.Edit edit, String text) {
    try {
      edit.run();
      this.text5 = text;
      this.enabled6 = false;
    } catch (IOException | IllegalArgumentException iOExceptionIllegalArgumentException) {
      this.text5 =
          iOExceptionIllegalArgumentException instanceof IOException
              ? "Could not save friends. Check that ellice/friends.json is writable."
              : iOExceptionIllegalArgumentException.getMessage();
      this.enabled6 = true;
    }

    this.timestamp = this.friendsStateController.revision();
    this.updateState();
  }

  private void updateState7(FriendProfile friendProfile, String text) {
    this.updateState6(() -> this.friendsStateController.update(friendProfile), text);
  }

  private void updateState8() {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    if (this.enabled5) {
      this.updateState4();
    } else if (this.checkCondition() && this.enabled2) {
      this.enabled2 = false;
      this.updateState();
    } else if (this.screenScreenIdService != null) {
      this.screenScreenIdService.close();
    }
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenId) {
    if (this.materialTerrainTooltipsService != null
        && this.materialTerrainTooltipsService.computedW() > 0.0F) {
      this.viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
    }

    List currentItems = List.copyOf(this.items.get());
    if (!currentItems.equals(this.items2)) {
      this.items2 = currentItems;
      this.friendsStateController.resolve(this.items2);
      this.updateState();
    }

    if (this.timestamp != this.friendsStateController.revision()) {
      this.timestamp = this.friendsStateController.revision();
      this.updateState();
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenId) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
    if (value == 256) {
      this.updateState8();
      return true;
    }

    if (value == 257
        && this.enabled5
        && this.materialTerrainTooltipsService.findById("friends.add.input")
            instanceof ControlLetterSpacingService controlLetterSpacing
        && controlLetterSpacing.focused()) {
      this.updateState5();
      return true;
    }

    if (value == 70 && (currentValue & 10) != 0) {
      if (!this.enabled5) {
        this.enabled2 = false;
        this.updateState();
        if (this.materialTerrainTooltipsService.findById("friends.search")
            instanceof ControlLetterSpacingService currentControlLetterSpacing) {
          this.materialKeyService.focus(currentControlLetterSpacing);
        }
      }

      return true;
    } else {
      return this.materialKeyService.key(
          this.enabled5
              ? this.materialTerrainTooltipsService.findById("friends.add.dialog")
              : this.materialTerrainTooltipsService,
          value,
          currentValue);
    }
  }

  @FunctionalInterface
  private interface Edit {
    void run() throws IOException;
  }
}
