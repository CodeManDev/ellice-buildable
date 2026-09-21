package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.DisplayMetrics;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.menu.MenuHardcoreHandler;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialKeyService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneFocusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.control.ControlCompactService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class WorldCreationScreen implements ScreenOperationHandler {
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite("knobPosition", item -> item instanceof ControlCompactService);
  private final MenuHardcoreHandler menuHardcoreHandler;
  private final MaterialKeyService materialKeyService = new MaterialKeyService();
  private ComponentMountService componentMountService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private float value2 = 1120.0F;
  private float value3 = 720.0F;
  private boolean enabled;
  private String text2 = "";

  public WorldCreationScreen(MenuHardcoreHandler menuHardcore) {
    this.menuHardcoreHandler = Objects.requireNonNull(menuHardcore);
  }

  @Override
  public String id() {
    return "world-creation";
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

  public void viewport(float x, float y) {
    if (this.value2 != x || this.value3 != y) {
      this.value2 = x;
      this.value3 = y;
      this.materialKeyService.clear(this.materialTerrainTooltipsService);
      this.updateState2();
    }
  }

  private boolean checkCondition() {
    return this.value2 < 600.0F;
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenId) {
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    this.materialTerrainTooltipsService
        .id("world-creation.canvas")
        .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
        .direction(ScenePctService.Direction.NONE)
        .backgroundColor(MaterialIsLightService.SURFACE_LOWEST)
        .interactive(true);
    this.materialTerrainTooltipsService.addChild(
        new SceneFocusService()
            .backdrop(true)
            .absolute()
            .inset(LayoutOperationHandler.px(0.0F))
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
            .pointerEvents(false));
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

  private ComponentKeyService<?> createComponentKeyService(ComponentThemeService componentTheme) {
    this.materialTerrainTooltipsService.backgroundColor(MaterialIsLightService.SURFACE_LOWEST);
    MenuHardcoreHandler.State currentState = this.menuHardcoreHandler.state();
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(
        ComponentBoxService.column(
                item -> item.gap(4.0F).visible(this.value3 >= 420.0F),
                MaterialTextService.text(
                        "Something only you could build.", 28.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.lineHeight(36.0F).wordWrap(true)),
                MaterialTextService.text(
                    "Choose how your next adventure begins.",
                    14.0F,
                    MaterialIsLightService.ON_SURFACE_VARIANT))
            .key("intro"));
    arrayList.add(
        this.createComponentKeyService3(
            "creation.name",
            "World name",
            "My world",
            currentState.name(),
            this.menuHardcoreHandler::name,
            80,
            "The name shown in your worlds library."));
    arrayList.add(
        ComponentBoxService.column(
                item -> item.gap(4.0F),
                MaterialTextService.text(
                    "Game mode", 14.0F, MaterialIsLightService.ON_SURFACE_VARIANT),
                ComponentBoxService.row(
                    item -> item.gap(4.0F),
                    this.createComponentKeyService4(
                        MenuHardcoreHandler.Mode.SURVIVAL, currentState),
                    this.createComponentKeyService4(
                        MenuHardcoreHandler.Mode.CREATIVE, currentState),
                    this.createComponentKeyService4(
                        MenuHardcoreHandler.Mode.HARDCORE, currentState)))
            .key("modes"));
    arrayList.add(
        ComponentBoxService.row(
            item -> item.gap(8.0F),
            this.createComponentKeyService5(
                    "Difficulty",
                    createText2(currentState.difficulty().name()),
                    "Click to cycle the difficulty for mobs, hunger and damage.",
                    () ->
                        this.updateState(
                            () ->
                                this.menuHardcoreHandler.difficulty(
                                    MenuHardcoreHandler.Difficulty.values()[
                                        (currentState.difficulty().ordinal() + 1)
                                            % MenuHardcoreHandler.Difficulty.values().length])),
                    !currentState.hardcore())
                .key("difficulty"),
            this.createComponentKeyService5(
                    "World type",
                    createText(currentState),
                    "Click to change the landscape generator. Amplified worlds need more processing"
                        + " power.",
                    () ->
                        this.updateState(
                            () ->
                                this.menuHardcoreHandler.worldType(
                                    Math.floorMod(
                                        currentState.worldType() + 1,
                                        currentState.worldTypes().size()))),
                    !currentState.worldTypes().isEmpty())
                .key("type")));
    arrayList.add(
        this.createComponentKeyService3(
            "creation.seed",
            "World seed",
            "Random seed",
            currentState.seed(),
            this.menuHardcoreHandler::seed,
            128,
            "Leave empty for a random world. Numbers and text are both supported."));
    arrayList.add(
        MaterialTextService.button(
                "creation.random",
                "Roll a new seed",
                () ->
                    this.updateState(
                        () ->
                            this.menuHardcoreHandler.seed(
                                Long.toString(ThreadLocalRandom.current().nextLong()))),
                0,
                MaterialIsLightService.PRIMARY,
                20.0F)
            .children(
                MaterialTextService.icon("refresh", MaterialIsLightService.PRIMARY),
                MaterialTextService.label("Roll a new seed", MaterialIsLightService.PRIMARY))
            .props(item -> item.tooltip("Generate a new seed without creating a world.")));
    arrayList.add(
        ComponentBoxService.column(
            item -> item.gap(4.0F),
            MaterialTextService.text("Make it yours", 18.0F, MaterialIsLightService.ON_SURFACE),
            this.createComponentKeyService6(
                "creation.structures",
                "Generate structures",
                "Villages, temples and other generated structures.",
                currentState.structures(),
                true,
                this.menuHardcoreHandler::structures),
            this.createComponentKeyService6(
                "creation.commands",
                "Allow commands",
                currentState.hardcore()
                    ? "Unavailable in Hardcore."
                    : "Enable commands such as /gamemode and /tp.",
                currentState.commands(),
                !currentState.hardcore(),
                this.menuHardcoreHandler::commands),
            this.createComponentKeyService6(
                "creation.bonus",
                "Bonus chest",
                currentState.hardcore()
                    ? "Unavailable in Hardcore."
                    : "Start with a chest of supplies near spawn.",
                currentState.bonusChest(),
                !currentState.hardcore(),
                this.menuHardcoreHandler::bonusChest)));
    if (!this.text2.isEmpty()) {
      arrayList.add(
          MaterialTextService.text(this.text2, 14.0F, MaterialIsLightService.ERROR)
              .props(item -> item.wordWrap(true))
              .key("error"));
    }

    ComponentKeyService<?> currentHeight =
        ComponentBoxService.column(
                item ->
                    item.id("creation.scroll")
                        .flex(1.0F)
                        .minWidth(0.0F)
                        .minHeight(0.0F)
                        .height(LayoutOperationHandler.percent(100.0F))
                        .gap(16.0F)
                        .scrollable(true)
                        .clip(true)
                        .scrollbarWidth(3.0F)
                        .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                        .padding(4.0F, 8.0F, 16.0F, 0.0F),
                arrayList.stream()
                    .map(
                        componentKey -> componentKey.props(component -> component.flexShrink(0.0F)))
                    .toArray(ComponentKeyService[]::new))
            .key("form");
    return ComponentBoxService.column(
        item ->
            item.size(
                    LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
                .padding(this.checkCondition() ? 0.0F : 24.0F)
                .align(ScenePctService.Align.CENTER)
                .justify(ScenePctService.Justify.CENTER),
        ComponentBoxService.panel(
            item ->
                item.id("creation.sheet")
                    .size(
                        LayoutOperationHandler.percent(100.0F),
                        LayoutOperationHandler.percent(100.0F))
                    .maxWidth(1080.0F)
                    .maxHeight(800.0F)
                    .backgroundColor(
                        ScenePctService.mulAlpha(MaterialIsLightService.SURFACE, 0.97F))
                    .blur(24.0F)
                    .cornerRadius(this.checkCondition() ? 0.0F : 28.0F)
                    .direction(ScenePctService.Direction.COLUMN)
                    .clip(true),
            ComponentBoxService.row(
                item ->
                    item.id("creation.toolbar")
                        .height(LayoutOperationHandler.px(this.value3 < 360.0F ? 52.0F : 72.0F))
                        .padding(4.0F, 8.0F)
                        .gap(4.0F)
                        .align(ScenePctService.Align.CENTER)
                        .flexShrink(0.0F),
                MaterialTextService.iconButton(
                    "creation.back", "arrow_back", "Back to your worlds · Esc", this::updateState4),
                MaterialTextService.text(
                        "Create your world", 22.0F, MaterialIsLightService.ON_SURFACE)
                    .props(item -> item.flex(1.0F).minWidth(0.0F))),
            ComponentBoxService.row(
                item ->
                    item.flex(1.0F)
                        .minHeight(0.0F)
                        .gap(20.0F)
                        .padding(0.0F, this.checkCondition() ? 12.0F : 24.0F),
                currentHeight,
                this.createComponentKeyService2(currentState)
                    .props(item -> item.visible(this.value2 >= 900.0F))),
            ComponentBoxService.row(
                item ->
                    item.id("creation.actions")
                        .height(LayoutOperationHandler.px(72.0F))
                        .padding(8.0F, this.checkCondition() ? 12.0F : 24.0F)
                        .gap(8.0F)
                        .align(ScenePctService.Align.CENTER)
                        .flexShrink(0.0F),
                MaterialTextService.text(
                        currentState.hardcore()
                            ? "One life. Make it count."
                            : "Ready when you are.",
                        14.0F,
                        currentState.hardcore()
                            ? MaterialIsLightService.TERTIARY
                            : MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.flex(1.0F).minWidth(0.0F).visible(!this.checkCondition())),
                MaterialTextService.button(
                        "creation.cancel",
                        "Cancel",
                        this::updateState4,
                        0,
                        MaterialIsLightService.PRIMARY,
                        20.0F)
                    .props(item -> item.visible(this.checkCondition())),
                MaterialTextService.button(
                        "creation.create", "Create world", this::updateState3, true)
                    .props(
                        item ->
                            item.available(!currentState.name().isBlank() && !this.enabled)
                                .flex(this.checkCondition() ? 1.0F : 0.0F))
                    .children(
                        MaterialTextService.icon("play_arrow", MaterialIsLightService.ON_PRIMARY),
                        MaterialTextService.label(
                            "Create world", MaterialIsLightService.ON_PRIMARY)))));
  }

  private ComponentKeyService<?> createComponentKeyService2(MenuHardcoreHandler.State state) {
    return ComponentBoxService.column(
        item ->
            item.width(LayoutOperationHandler.px(260.0F))
                .height(LayoutOperationHandler.percent(100.0F))
                .flexShrink(0.0F)
                .gap(16.0F)
                .scrollable(true)
                .clip(true),
        ComponentBoxService.panel(
            item ->
                item.backgroundColor(MaterialIsLightService.PRIMARY_CONTAINER)
                    .cornerRadius(24.0F)
                    .padding(24.0F)
                    .direction(ScenePctService.Direction.COLUMN)
                    .gap(16.0F),
            ComponentBoxService.node(
                "creation-shape",
                MaterialLabelService::new,
                item ->
                    item.shape(state.hardcore() ? "cookie9" : "clover4")
                        .active(true)
                        .tint(
                            state.hardcore()
                                ? MaterialIsLightService.TERTIARY
                                : MaterialIsLightService.PRIMARY)
                        .size(96.0F, 96.0F)),
            MaterialTextService.text(
                    createText(state), 24.0F, MaterialIsLightService.ON_PRIMARY_CONTAINER)
                .props(item -> item.wordWrap(true)),
            MaterialTextService.text(
                "Your world, your rules.", 14.0F, MaterialIsLightService.ON_PRIMARY_CONTAINER)),
        MaterialTextService.text(
                state.name().isBlank() ? "Give your world a name" : state.name(),
                22.0F,
                MaterialIsLightService.ON_SURFACE)
            .props(item -> item.wordWrap(true)),
        MaterialTextService.text(
            createText2(state.mode().name()) + " · " + createText2(state.difficulty().name()),
            14.0F,
            MaterialIsLightService.ON_SURFACE_VARIANT),
        MaterialTextService.text(
                state.seed().isBlank() ? "A fresh random seed" : "Seed · " + state.seed(),
                12.0F,
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.wordWrap(true)),
        MaterialTextService.text("Save folder", 12.0F, MaterialIsLightService.ON_SURFACE_VARIANT),
        MaterialTextService.text(state.folder(), 14.0F, MaterialIsLightService.ON_SURFACE)
            .props(item -> item.wordWrap(true)),
        MaterialTextService.text(
                state.hardcore()
                    ? "Hardcore uses hard difficulty and gives you one life. Commands and the bonus"
                          + " chest are disabled."
                    : "Your world appears in the library after creation.",
                14.0F,
                state.hardcore()
                    ? MaterialIsLightService.TERTIARY
                    : MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(item -> item.wordWrap(true)));
  }

  private ComponentKeyService<?> createComponentKeyService3(
      String currentText,
      String nextText,
      String previousText,
      String sourceText,
      Consumer<String> consumer,
      int value,
      String targetText) {
    return ComponentBoxService.column(
            item -> item.gap(6.0F),
            MaterialTextService.text(nextText, 14.0F, MaterialIsLightService.ON_SURFACE_VARIANT),
            ComponentBoxService.<ControlLetterSpacingService>node(
                    "creation-input",
                    ControlLetterSpacingService::new,
                    item -> {
                      MaterialTextService.input(item);
                      item.id(currentText)
                          .height(LayoutOperationHandler.px(52.0F))
                          .placeholder(previousText)
                          .maxLength(value)
                          .tooltip(targetText)
                          .onChanged(text -> this.updateState(() -> consumer.accept(text)));
                      if (!Objects.equals(item.text(), sourceText)) {
                        item.text(sourceText);
                      }
                    })
                .key(currentText))
        .key(currentText + ".field");
  }

  private ComponentKeyService<?> createComponentKeyService4(
      MenuHardcoreHandler.Mode currentMode, MenuHardcoreHandler.State state) {
    return MaterialTextService.button(
            "creation.mode." + currentMode,
            createText2(currentMode.name()),
            () -> this.updateState(() -> this.menuHardcoreHandler.mode(currentMode)),
            state.mode() == currentMode)
        .props(
            item -> {
              item.flex(1.0F)
                  .flexShrink(1.0F)
                  .minWidth(0.0F)
                  .padding(0.0F, 8.0F)
                  .tooltip(
                      switch (currentMode) {
                        case SURVIVAL -> "Gather resources, craft and survive.";
                        case CREATIVE -> "Unlimited blocks and flight for building.";
                        case HARDCORE ->
                            "Hard difficulty. One life. Commands and bonus chest are disabled.";
                      });
            });
  }

  private ComponentKeyService<?> createComponentKeyService5(
      String currentText,
      String nextText,
      String previousText,
      Runnable runnable,
      boolean enabled) {
    return ComponentBoxService.column(
        item -> item.flex(1.0F).minWidth(0.0F).gap(4.0F),
        MaterialTextService.text(currentText, 14.0F, MaterialIsLightService.ON_SURFACE_VARIANT),
        MaterialTextService.button("creation." + currentText, nextText, runnable, false)
            .props(item -> item.available(enabled).padding(0.0F, 12.0F).tooltip(previousText))
            .children(
                MaterialTextService.label(
                        nextText,
                        enabled
                            ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                            : MaterialIsLightService.ON_SURFACE_VARIANT)
                    .props(item -> item.flex(1.0F).minWidth(0.0F)),
                MaterialTextService.icon("swap_horiz", MaterialIsLightService.ON_SURFACE_VARIANT)));
  }

  private ComponentKeyService<?> createComponentKeyService6(
      String currentText,
      String nextText,
      String previousText,
      boolean enabled,
      boolean currentEnabled,
      Consumer<Boolean> consumer) {
    return MaterialTextService.button(
            currentText,
            nextText,
            () -> this.updateState(() -> consumer.accept(!enabled)),
            0,
            MaterialIsLightService.ON_SURFACE,
            16.0F)
        .props(
            item ->
                item.available(currentEnabled)
                    .height(LayoutOperationHandler.px(56.0F))
                    .tooltip(previousText)
                    .padding(0.0F, 8.0F))
        .children(
            MaterialTextService.text(
                    nextText,
                    14.0F,
                    currentEnabled
                        ? MaterialIsLightService.ON_SURFACE
                        : MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(item -> item.flex(1.0F).minWidth(0.0F)),
            ComponentBoxService.node(
                "creation-switch",
                ControlCompactService::new,
                item -> {
                  item.material(true)
                      .size(52.0F, 32.0F)
                      .flexShrink(0.0F)
                      .pointerEvents(false)
                      .interactive(currentEnabled);
                  if (item.value() != enabled) {
                    float currentValue = motionFiniteService.get(item);
                    item.value(enabled);
                    motionFiniteService.set(item, currentValue);
                    MotionAnimateService.animate(
                        item,
                        motionFiniteService,
                        enabled ? 1.0F : 0.0F,
                        MaterialIsLightService.FAST_SPATIAL);
                  }
                }));
  }

  private static String createText(MenuHardcoreHandler.State state) {
    return state.worldType() >= 0 && state.worldType() < state.worldTypes().size()
        ? state.worldTypes().get(state.worldType())
        : "Custom";
  }

  private static String createText2(String text) {
    return text.substring(0, 1) + text.substring(1).toLowerCase(Locale.ROOT);
  }

  private void updateState(Runnable runnable) {
    if (!this.enabled) {
      try {
        runnable.run();
        this.text2 = "";
      } catch (RuntimeException exception) {
        this.text2 = "That option could not be applied. Please try again.";
      }

      this.updateState2();
    }
  }

  private void updateState2() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState3() {
    if (!this.enabled && !this.menuHardcoreHandler.state().name().isBlank()) {
      this.enabled = true;

      try {
        this.menuHardcoreHandler.create();
      } catch (RuntimeException exception) {
        this.enabled = false;
        this.text2 = "Could not create this world. Please return and try again.";
        this.updateState2();
      }
    }
  }

  private void updateState4() {
    if (!this.enabled) {
      this.enabled = true;
      this.menuHardcoreHandler.cancel();
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenId) {
    this.materialKeyService.clear(this.materialTerrainTooltipsService);
    if (!this.enabled) {
      this.menuHardcoreHandler.dispose();
    }
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenId) {
    if (screenScreenId != null) {
      DisplayMetrics displayMetrics = CoreIsInitializedHandler.get().viewport();
      this.viewport(displayMetrics.width(), displayMetrics.height());
    }
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
    if (value == 256) {
      this.updateState4();
      return true;
    }

    if (value == 70 && (currentValue & 10) != 0) {
      if (this.materialTerrainTooltipsService.findById("creation.name")
          instanceof ControlLetterSpacingService controlLetterSpacing) {
        this.materialKeyService.clear(this.materialTerrainTooltipsService);
        this.materialKeyService.focus(controlLetterSpacing);
        controlLetterSpacing.selectAll();
      }

      return true;
    } else {
      return this.materialKeyService.key(this.materialTerrainTooltipsService, value, currentValue);
    }
  }
}
