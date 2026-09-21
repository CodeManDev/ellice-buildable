package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.studio.StudioListenerService;
import dev.felix.ellice.feature.studio.StudioMode;
import dev.felix.ellice.feature.studio.StudioOrbitService;
import dev.felix.ellice.feature.studio.StudioRenderer;
import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.feature.studio.StudioStringService;
import dev.felix.ellice.feature.studio.StudioValidateValidator;
import dev.felix.ellice.hud.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialColorService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialTabsService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.color.ColorFeatureType;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.HueTrianglePicker;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.control.SliderControl;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioActionsService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioComponent;
import dev.felix.ellice.ui.screen.builtin.studio.StudioKindService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioShowService;
import dev.felix.ellice.ui.screen.builtin.studio.StudioZoomService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;

public final class ModuleStudioScreen implements ScreenOperationHandler {
  private ColorFeatureType fplk09zljn3 = ColorFeatureType.GRADIENT;
  private static final int count = -14998990;
  private static final int count2 = -14077369;
  private static final int count3 = -12827301;
  private static final int count4 = -1447948;
  private static final int count5 = -5787963;
  private static final int count6 = -3096579;
  private static final int count7 = -6366515;
  private final StudioRenderer renderer;
  private final LayoutOperationHandler layoutOperationHandler;
  private StudioListenerService items2;
  private StudioZoomService studioZoomService;
  private StudioShowService studioShowService;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private ScreenScreenIdService screenScreenIdService;
  private float value2 = Float.intBitsToFloat(1150681088);
  private float value3 = Float.intBitsToFloat(0x44480000);
  private float value4;
  private long timestamp;
  private String faswzxueb2ko = "";
  private boolean enabled2;
  private boolean enabled3;
  private boolean enabled4;
  private boolean fo3nquhz6bp;
  private String text3 = "";
  private List<String> text4 = List.of();
  private Consumer<Integer> consumer;
  private IntConsumer fr70cu2sdes;
  private float value5;
  private float value6;
  private float value7;
  private float value8;
  private ScenePctService<?> scenePctService;
  private final StudioStringService text5 = new StudioStringService();
  private String text6 = "";

  public ModuleStudioScreen(StudioRenderer studioRenderer) {
    this(studioRenderer, studioRenderer.firstOrExample(), null);
  }

  public ModuleStudioScreen(
      StudioRenderer studioRenderer,
      StudioShapeService studioShapeService,
      LayoutOperationHandler layoutOperationHandler) {
    this.renderer = studioRenderer;
    this.layoutOperationHandler = layoutOperationHandler;
    this.items2 = new StudioListenerService(studioShapeService);
  }

  public StudioListenerService session() {
    return this.items2;
  }

  public StudioZoomService canvas() {
    return this.studioZoomService;
  }

  public void viewport(float f, float f2) {
    this.value2 = f;
    this.value3 = f2;
    this.updateState();
  }

  @Override
  public String id() {
    return "module-studio";
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.NONE;
  }

  @Override
  public float backgroundDesaturation() {
    return 0.0f;
  }

  @Override
  public boolean overlaysPreviousScreen() {
    return true;
  }

  @Override
  public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
    this.screenScreenIdService = screenScreenIdService;
    this.fo3nquhz6bp = false;
    this.timestamp = System.nanoTime();
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService) this.materialTerrainTooltipsService.id("studio.root"))
                .size(
                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                        Float.intBitsToFloat(1120403456)),
                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                        Float.intBitsToFloat(1120403456))))
        .backgroundColor(-15722715)
        .direction(ScenePctService.Direction.COLUMN);
    this.items2.listener(this::updateState);
    this.componentMountService =
        new ComponentMountService()
            .mount(
                this::createComponentKeyService,
                screenScreenIdService == null
                    ? new ThemeIsSetService()
                    : screenScreenIdService.theme());
    this.componentMountService.size(
        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
            Float.intBitsToFloat(1120403456)),
        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
            Float.intBitsToFloat(1120403456)));
    this.materialTerrainTooltipsService.addChild(this.componentMountService);
    return this.materialTerrainTooltipsService;
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    long l = System.nanoTime();
    if (this.items2.playing) {
      this.value4 +=
          Math.min(
              Float.intBitsToFloat(0x3DCCCCCD),
              (float) (l - this.timestamp) / Float.intBitsToFloat(1315859240));
    }
    this.timestamp = l;
    if (this.materialTerrainTooltipsService != null
        && (Math.abs(this.materialTerrainTooltipsService.computedW() - this.value2)
                > Float.intBitsToFloat(0x3F000000)
            || Math.abs(this.materialTerrainTooltipsService.computedH() - this.value3)
                > Float.intBitsToFloat(0x3F000000))) {
      this.value2 = this.materialTerrainTooltipsService.computedW();
      this.value3 = this.materialTerrainTooltipsService.computedH();
      this.updateState();
    }
  }

  public void previewTime(float f) {
    this.value4 = f;
  }

  private StudioValidateValidator.Frame createFrame() {
    return StudioValidateValidator.evaluate(
        this.items2.project, this.createLayoutOperationHandler(), this.value4);
  }

  private LayoutOperationHandler createLayoutOperationHandler() {
    if (this.layoutOperationHandler != null) {
      return this.layoutOperationHandler;
    }
    if (CoreIsInitializedHandler.isReady() && Minecraft.getInstance().level != null) {
      return CoreIsInitializedHandler.get().hudVariables();
    }
    return this.text5;
  }

  private void updateState() {
    if (!this.fo3nquhz6bp && this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (this.value2 >= Float.intBitsToFloat(0x44480000)) {
      arrayList.add(this.mexsbgng1sfx());
    }
    arrayList.add(this.createComponentKeyService3());
    if (this.value2 >= Float.intBitsToFloat(0x44480000)) {
      arrayList.add(this.createComponentKeyService8());
    }
    ArrayList arrayList2 = new ArrayList();
    arrayList2.add(
        ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.size(
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456)),
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .padding(
                                            this.value2 < Float.intBitsToFloat(1142292480)
                                                ? Float.intBitsToFloat(0x40C00000)
                                                : Float.intBitsToFloat(0x41400000)))
                                .gap(Float.intBitsToFloat(0x41000000)))
                        .pointerEvents(
                            (this.text3.isEmpty()
                                        && (!(this.value2 < Float.intBitsToFloat(0x44480000))
                                            || !this.enabled3 && !this.enabled4)
                                    ? 1
                                    : 0)
                                != 0),
                this.createComponentKeyService2(),
                ComponentBoxService.row(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.width(
                                                    dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.percent(
                                                        Float.intBitsToFloat(1120403456))))
                                            .flex(1.0f))
                                    .minHeight(0.0f))
                            .gap(Float.intBitsToFloat(0x41000000)),
                    (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)),
                this.maoobl7dtufz())
            .key("main"));
    if (this.value2 < Float.intBitsToFloat(0x44480000) && (this.enabled3 || this.enabled4)) {
      arrayList2.add(this.createComponentKeyService9());
    }
    if (!this.text3.isEmpty()) {
      arrayList2.add(this.createComponentKeyService10());
    }
    arrayList2.add(
        ComponentBoxService.node(
                "studio-drag-preview",
                StudioShowService::new,
                studioShowService -> {
                  this.studioShowService = studioShowService;
                  ((StudioShowService)
                          ((StudioShowService)
                                  ((StudioShowService) studioShowService.absolute())
                                      .inset(
                                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                              .px(0.0f)))
                              .size(
                                  dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(),
                                  dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto()))
                      .pointerEvents(false);
                },
                new ComponentKeyService[0])
            .key("drag-preview"));
    return ComponentBoxService.box(
            layoutContainerNode ->
                layoutContainerNode.size(
                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                        Float.intBitsToFloat(1120403456)),
                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                        Float.intBitsToFloat(1120403456))),
            (ComponentKeyService[]) arrayList2.toArray(ComponentKeyService[]::new))
        .key("studio:" + this.items2.project.id);
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(
        ComponentBoxService.node(
                "studio-mark",
                MaterialLabelService::new,
                materialLabelService ->
                    ((MaterialLabelService)
                            materialLabelService
                                .shape("cookie9")
                                .active(true)
                                .tint(-3096579)
                                .size(
                                    Float.intBitsToFloat(1106247680),
                                    Float.intBitsToFloat(1106247680)))
                        .flexShrink(0.0f),
                new ComponentKeyService[0])
            .key("mark"));
    arrayList.add(
        ComponentBoxService.column(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode) layoutContainerNode.flex(1.0f)).minWidth(0.0f))
                        .gap(0.0f),
                ModuleStudioScreen.createComponentKeyService19(
                        "Module Studio",
                        this.value2 < Float.intBitsToFloat(1142292480)
                            ? Float.intBitsToFloat(1098907648)
                            : Float.intBitsToFloat(1100480512),
                        -1447948)
                    .key("title"),
                ModuleStudioScreen.createComponentKeyService19(
                        this.items2.project.name
                            + (this.items2.dirty() ? " \u00b7 Unsaved" : " \u00b7 Draft"),
                        Float.intBitsToFloat(1093664768),
                        -5787963)
                    .props(
                        sceneTextService ->
                            ((SceneTextService) sceneTextService.id("studio.project-caption"))
                                .width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                    .key("name"))
            .key("title"));
    if (this.value2 >= Float.intBitsToFloat(1142292480)) {
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.projects", "Projects", () -> this.updateState14("Projects"), false));
    }
    arrayList.add(
        ModuleStudioScreen.createComponentKeyService20(
            "studio.new", "New", () -> this.updateState14("New project"), false));
    if (this.value2 >= Float.intBitsToFloat(1142292480)) {
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.save", "Save", this::checkCondition2, false));
    }
    arrayList.add(
        ModuleStudioScreen.createComponentKeyService20(
                "studio.publish",
                this.renderer.published(this.items2.project.id) ? "Update module" : "Create module",
                this::updateState16,
                true)
            .props(
                materialJoinedService ->
                    materialJoinedService.tooltip(
                        "Save this version as a module in ClickGUI \u2192 HUD")));
    arrayList.add(
        ModuleStudioScreen.createComponentKeyService20(
                "studio.close", "\u00d7", this::updateState17, false)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    materialJoinedService.size(
                                        Float.intBitsToFloat(0x42000000),
                                        Float.intBitsToFloat(1108344832)))
                                .padding(0.0f))
                        .tooltip("Back to ClickGUI")));
    return ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        layoutContainerNode.id("studio.header"))
                                                    .width(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                            .height(
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1112539136))))
                                    .gap(
                                        this.value2 < Float.intBitsToFloat(1142292480)
                                            ? Float.intBitsToFloat(0x40800000)
                                            : Float.intBitsToFloat(1092616192)))
                            .align(ScenePctService.Align.CENTER))
                    .flexShrink(0.0f),
            (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new))
        .key("header");
  }

  private ComponentKeyService<?> createComponentKeyService3() {
    ComponentKeyService<LayoutContainerNode> componentKeyService =
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode) layoutContainerNode.gap(2.0f))
                    .align(ScenePctService.Align.CENTER),
            ModuleStudioScreen.createComponentKeyService20(
                "studio.mode.design", "Design", () -> this.mxjeo5pbpyt(false), !this.items2.logic),
            ModuleStudioScreen.createComponentKeyService20(
                "studio.mode.logic", "Logic", () -> this.mxjeo5pbpyt(true), this.items2.logic));
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (this.value2 < Float.intBitsToFloat(0x44480000)) {
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.library",
              "Library",
              () -> {
                this.enabled3 = true;
                this.enabled4 = false;
                this.updateState();
              },
              false));
    }
    arrayList.add(componentKeyService.props(scenePctService -> scenePctService.flex(1.0f)));
    arrayList.add(
        ModuleStudioScreen.createComponentKeyService20(
            "studio.play",
            this.items2.playing ? "Pause" : "Play",
            () -> {
              this.items2.playing = !this.items2.playing;
              this.updateState();
            },
            false));
    if (this.value2 < Float.intBitsToFloat(0x44480000)) {
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.inspect",
              "Inspect",
              () -> {
                this.enabled4 = true;
                this.enabled3 = false;
                this.updateState();
              },
              false));
    }
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                layoutContainerNode.id("studio.workspace"))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .height(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40C00000)),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                                .height(
                                                    dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(1108344832))))
                                        .align(ScenePctService.Align.CENTER))
                                .gap(Float.intBitsToFloat(0x40C00000)))
                        .flexShrink(0.0f),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)),
            ComponentBoxService.node(
                    "studio-canvas",
                    () ->
                        new StudioZoomService(
                            this.items2, this::createFrame, () -> Float.valueOf(this.value4)),
                    studioZoomService -> {
                      this.studioZoomService = studioZoomService;
                      ((StudioZoomService)
                              ((StudioZoomService)
                                      ((StudioZoomService)
                                              studioZoomService
                                                  .beforeSelect(
                                                      () -> {
                                                        this.m8lski6g0f3w();
                                                        ModuleStudioScreen.updateState22(
                                                            this.materialTerrainTooltipsService);
                                                      })
                                                  .id("studio.canvas"))
                                          .width(
                                              dev.felix.ellice.ui.scene.layout
                                                  .LayoutOperationHandler.percent(
                                                  Float.intBitsToFloat(1120403456))))
                                  .flex(1.0f))
                          .minHeight(0.0f);
                    },
                    new ComponentKeyService[0])
                .key("canvas"),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    layoutContainerNode.width(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                                .height(
                                                    dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(0x42000000))))
                                        .gap(Float.intBitsToFloat(0x40800000)))
                                .align(ScenePctService.Align.CENTER))
                        .flexShrink(0.0f),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.undo", "Undo", () -> this.updateState15(this.items2::undo), false)
                    .props(
                        materialJoinedService ->
                            materialJoinedService.available(this.items2.canUndo())),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.redo", "Redo", () -> this.updateState15(this.items2::redo), false)
                    .props(
                        materialJoinedService ->
                            materialJoinedService.available(this.items2.canRedo())),
                ComponentBoxService.box(
                    layoutContainerNode -> layoutContainerNode.flex(1.0f),
                    new ComponentKeyService[0]),
                ModuleStudioScreen.createComponentKeyService20(
                    "studio.snap",
                    "Snap",
                    () -> {
                      this.items2.snap = !this.items2.snap;
                      this.updateState();
                    },
                    this.items2.snap),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.zoom-out",
                        "\u2212",
                        () -> this.studioZoomService.zoomBy(Float.intBitsToFloat(0x3F555555)),
                        false)
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    materialJoinedService.minWidth(
                                        Float.intBitsToFloat(1105199104)))
                                .padding(0.0f, Float.intBitsToFloat(0x40C00000))),
                ModuleStudioScreen.createComponentKeyService20(
                    "studio.fit", "Fit", () -> this.studioZoomService.fit(), false),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.zoom-in",
                        "+",
                        () -> this.studioZoomService.zoomBy(Float.intBitsToFloat(1067030938)),
                        false)
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    materialJoinedService.minWidth(
                                        Float.intBitsToFloat(1105199104)))
                                .padding(0.0f, Float.intBitsToFloat(0x40C00000)))))
        .key("workspace");
  }

  private ComponentKeyService<?> mexsbgng1sfx() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (this.enabled2) {
      for (StudioShapeService.Shape shape : this.items2.project.shapes.reversed()) {
        if (!this.checkCondition(shape.name)) continue;
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.layer." + shape.id,
                    shape.name,
                    () -> {
                      ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
                      this.items2.logic = false;
                      this.items2.select(shape.id);
                    },
                    shape.id.equals(this.items2.selected))
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        materialJoinedService.width(
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .percent(Float.intBitsToFloat(1120403456))))
                                    .justify(ScenePctService.Justify.START))
                            .tooltip(
                                String.valueOf((Object) shape.kind)
                                    + (!shape.visible ? " \u00b7 Hidden" : "")
                                    + (shape.locked ? " \u00b7 Locked" : ""))));
      }
      if (arrayList.isEmpty()) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService19(
                    "Your shapes appear here.", Float.intBitsToFloat(0x41400000), -5787963)
                .props(
                    sceneTextService ->
                        sceneTextService.wordWrap(true).padding(Float.intBitsToFloat(0x41000000))));
      }
    } else if (!this.items2.logic) {
      for (StudioShapeService.ShapeKind shapeKind : StudioShapeService.ShapeKind.values()) {
        if (!this.checkCondition(StudioListenerService.title(shapeKind))) continue;
        arrayList.add(this.createComponentKeyService5(shapeKind));
      }
    } else {
      String string = "";
      for (StudioMode studioMode : StudioMode.values()) {
        if (!this.checkCondition(studioMode.title + " " + studioMode.group)) continue;
        if (!string.equals(studioMode.group)) {
          arrayList.add(
              ModuleStudioScreen.createComponentKeyService19(
                      studioMode.group.toUpperCase(Locale.ROOT),
                      Float.intBitsToFloat(1092616192),
                      -5787963)
                  .props(
                      sceneTextService ->
                          sceneTextService.padding(
                              Float.intBitsToFloat(1092616192),
                              Float.intBitsToFloat(0x40800000),
                              Float.intBitsToFloat(0x40400000),
                              Float.intBitsToFloat(0x40800000)))
                  .key("group:" + studioMode.group));
          string = studioMode.group;
        }
        arrayList.add(this.createComponentKeyService6(studioMode));
      }
    }
    arrayList.replaceAll(
        componentKeyService ->
            componentKeyService.props(
                scenePctService ->
                    ((ScenePctService)
                            scenePctService.width(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456))))
                        .flexShrink(0.0f)));
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                sceneCornerRadiusService.id(
                                                                    "studio.library-panel"))
                                                            .width(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.px(
                                                                    this.value2
                                                                            >= Float.intBitsToFloat(
                                                                                1148846080)
                                                                        ? Float.intBitsToFloat(
                                                                            1127743488)
                                                                        : (this.value2
                                                                                < Float
                                                                                    .intBitsToFloat(
                                                                                        0x44480000)
                                                                            ? Float.intBitsToFloat(
                                                                                1130889216)
                                                                            : Float.intBitsToFloat(
                                                                                1125908480)))))
                                                    .height(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                            .flexShrink(0.0f))
                                    .backgroundColor(-14998990)
                                    .cornerRadius(Float.intBitsToFloat(1101004800))
                                    .border(Float.intBitsToFloat(1061158912), -12827301)
                                    .padding(Float.intBitsToFloat(1092616192)))
                            .gap(Float.intBitsToFloat(0x41000000)))
                    .direction(ScenePctService.Direction.COLUMN),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456))))
                        .gap(2.0f),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.library.blocks",
                        this.items2.logic ? "Blocks" : "Shapes",
                        () -> {
                          this.enabled2 = false;
                          this.updateState();
                        },
                        !this.enabled2)
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService) materialJoinedService.flex(1.0f))
                                        .minWidth(0.0f))
                                .padding(0.0f, Float.intBitsToFloat(0x40C00000))),
                ModuleStudioScreen.createComponentKeyService20(
                        "studio.library.layers",
                        "Layers",
                        () -> {
                          this.enabled2 = true;
                          this.updateState();
                        },
                        this.enabled2)
                    .props(
                        materialJoinedService ->
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService) materialJoinedService.flex(1.0f))
                                        .minWidth(0.0f))
                                .padding(0.0f, Float.intBitsToFloat(0x40C00000)))),
            ComponentBoxService.node(
                    "studio-library-search",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService -> {
                      ModuleStudioScreen.updateState21(controlLetterSpacingService);
                      if (!controlLetterSpacingService.focused()) {
                        controlLetterSpacingService.text(this.faswzxueb2ko);
                      }
                      ((ControlLetterSpacingService)
                              ((ControlLetterSpacingService)
                                      controlLetterSpacingService.id("studio.library-search"))
                                  .placeholder(
                                      this.enabled2
                                          ? "Find a layer"
                                          : (this.items2.logic ? "Find a block" : "Find a shape"))
                                  .height(
                                      dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                          Float.intBitsToFloat(0x42000000))))
                          .onChanged(
                              string -> {
                                this.faswzxueb2ko = string;
                                this.updateState();
                              });
                    },
                    new ComponentKeyService[0])
                .key("search"),
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
                                                                                            "studio.library-items"))
                                                                                .width(
                                                                                    dev.felix.ellice
                                                                                        .ui.scene
                                                                                        .layout
                                                                                        .LayoutOperationHandler
                                                                                        .percent(
                                                                                            Float
                                                                                                .intBitsToFloat(
                                                                                                    1120403456))))
                                                                        .flex(1.0f))
                                                                .minHeight(0.0f))
                                                        .gap(Float.intBitsToFloat(0x40A00000)))
                                                .scrollable(true))
                                        .scrollbarWidth(2.0f))
                                .scrollbarColor(-12827301))
                        .clip(true),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)),
            ModuleStudioScreen.createComponentKeyService19(
                    this.enabled2 ? "Front to back" : "Click or drag to the canvas",
                    Float.intBitsToFloat(1092616192),
                    -5787963)
                .props(sceneTextService -> sceneTextService.wordWrap(true)))
        .key("library");
  }

  private boolean checkCondition(String string) {
    return (this.faswzxueb2ko.isBlank()
                || string
                    .toLowerCase(Locale.ROOT)
                    .contains(this.faswzxueb2ko.toLowerCase(Locale.ROOT))
            ? 1
            : 0)
        != 0;
  }

  private ComponentKeyService<?> createComponentKeyService5(
      StudioShapeService.ShapeKind shapeKind) {
    return ComponentBoxService.node(
            "studio-library-tile",
            StudioActionsService::new,
            studioActionsService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        studioActionsService
                                                                            .actions(
                                                                                () ->
                                                                                    this
                                                                                        .updateState2(
                                                                                            shapeKind,
                                                                                            null,
                                                                                            null),
                                                                                (f, f2) ->
                                                                                    this
                                                                                        .updateState2(
                                                                                            shapeKind,
                                                                                            (Float)
                                                                                                f,
                                                                                            (Float)
                                                                                                f2))
                                                                            .preview(
                                                                                (f, f2) ->
                                                                                    this
                                                                                        .studioShowService
                                                                                        .show(
                                                                                            StudioListenerService
                                                                                                .title(
                                                                                                    shapeKind),
                                                                                            -3096579,
                                                                                            f
                                                                                                .floatValue(),
                                                                                            f2
                                                                                                .floatValue()),
                                                                                () ->
                                                                                    this
                                                                                        .studioShowService
                                                                                        .clear())
                                                                            .id(
                                                                                "studio.add.shape."
                                                                                    + shapeKind
                                                                                        .name()
                                                                                        .toLowerCase(
                                                                                            Locale
                                                                                                .ROOT)))
                                                                    .width(
                                                                        dev.felix.ellice.ui.scene
                                                                            .layout
                                                                            .LayoutOperationHandler
                                                                            .percent(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1120403456))))
                                                            .height(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.px(
                                                                    Float.intBitsToFloat(
                                                                        1110966272))))
                                                    .flexShrink(0.0f))
                                            .backgroundColor(-14077369)
                                            .hoverBackground(-13353900)
                                            .cornerRadius(Float.intBitsToFloat(0x41400000))
                                            .padding(
                                                Float.intBitsToFloat(0x40C00000),
                                                Float.intBitsToFloat(0x41000000)))
                                    .gap(Float.intBitsToFloat(0x41000000)))
                            .direction(ScenePctService.Direction.ROW))
                    .align(ScenePctService.Align.CENTER),
            ComponentBoxService.node(
                "studio-shape-icon",
                StudioKindService::new,
                studioKindService ->
                    ((StudioKindService)
                            studioKindService
                                .kind(shapeKind)
                                .size(
                                    Float.intBitsToFloat(1106247680),
                                    Float.intBitsToFloat(1106247680)))
                        .flexShrink(0.0f),
                new ComponentKeyService[0]),
            ModuleStudioScreen.createComponentKeyService19(
                    StudioListenerService.title(shapeKind),
                    Float.intBitsToFloat(0x41400000),
                    -1447948)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)))
        .key(shapeKind.name());
  }

  private ComponentKeyService<?> createComponentKeyService6(StudioMode studioMode) {
    return ComponentBoxService.node(
            "studio-library-tile",
            StudioActionsService::new,
            studioActionsService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        studioActionsService
                                                                            .actions(
                                                                                () ->
                                                                                    this
                                                                                        .updateState3(
                                                                                            studioMode,
                                                                                            null,
                                                                                            null),
                                                                                (f, f2) ->
                                                                                    this
                                                                                        .updateState3(
                                                                                            studioMode,
                                                                                            (Float)
                                                                                                f,
                                                                                            (Float)
                                                                                                f2))
                                                                            .preview(
                                                                                (f, f2) ->
                                                                                    this
                                                                                        .studioShowService
                                                                                        .show(
                                                                                            studioMode
                                                                                                .title,
                                                                                            studioMode
                                                                                                .color(),
                                                                                            f
                                                                                                .floatValue(),
                                                                                            f2
                                                                                                .floatValue()),
                                                                                () ->
                                                                                    this
                                                                                        .studioShowService
                                                                                        .clear())
                                                                            .id(
                                                                                "studio.add.block."
                                                                                    + studioMode
                                                                                        .name()
                                                                                        .toLowerCase(
                                                                                            Locale
                                                                                                .ROOT)))
                                                                    .width(
                                                                        dev.felix.ellice.ui.scene
                                                                            .layout
                                                                            .LayoutOperationHandler
                                                                            .percent(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1120403456))))
                                                            .height(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.px(
                                                                    Float.intBitsToFloat(
                                                                        1108344832))))
                                                    .flexShrink(0.0f))
                                            .backgroundColor(
                                                StudioValidateValidator.mix(
                                                    -14077369,
                                                    studioMode.color(),
                                                    Float.intBitsToFloat(1032134328)))
                                            .hoverBackground(-13353900)
                                            .cornerRadius(Float.intBitsToFloat(0x41400000))
                                            .padding(0.0f, Float.intBitsToFloat(0x41100000)))
                                    .gap(Float.intBitsToFloat(0x40E00000)))
                            .direction(ScenePctService.Direction.ROW))
                    .align(ScenePctService.Align.CENTER),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            sceneCornerRadiusService.size(
                                Float.intBitsToFloat(0x40A00000), Float.intBitsToFloat(1098907648)))
                        .cornerRadius(Float.intBitsToFloat(0x40400000))
                        .backgroundColor(studioMode.color())
                        .pointerEvents(false),
                new ComponentKeyService[0]),
            ModuleStudioScreen.createComponentKeyService19(
                    studioMode.title, Float.intBitsToFloat(1093664768), -1447948)
                .props(
                    sceneTextService ->
                        ((SceneTextService) sceneTextService.flex(1.0f)).minWidth(0.0f)))
        .key(studioMode.name());
  }

  private void updateState2(StudioShapeService.ShapeKind shapeKind, Float f, Float f2) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    if (f != null && !this.studioZoomService.containsScene(f.floatValue(), f2.floatValue())) {
      this.items2.message = "Drop onto the canvas to add a shape.";
      this.updateState();
      return;
    }
    float[] fArray = this.studioZoomService.insertion();
    this.items2.addShape(
        shapeKind,
        f == null ? fArray[0] : this.studioZoomService.worldX(f.floatValue()),
        f2 == null ? fArray[1] : this.studioZoomService.worldY(f2.floatValue()));
    this.enabled3 = false;
    this.updateState();
  }

  private void updateState3(StudioMode studioMode, Float f, Float f2) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    if (f != null && !this.studioZoomService.containsScene(f.floatValue(), f2.floatValue())) {
      this.items2.message = "Drop onto the canvas to add a block.";
      this.updateState();
      return;
    }
    float[] fArray = this.studioZoomService.insertion();
    this.items2.addBlock(
        studioMode,
        f == null ? fArray[0] : this.studioZoomService.worldX(f.floatValue()),
        f2 == null ? fArray[1] : this.studioZoomService.worldY(f2.floatValue()));
    this.enabled3 = false;
    this.updateState();
  }

  private void mxjeo5pbpyt(boolean bl) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    this.items2.logic = bl;
    this.enabled2 = false;
    this.faswzxueb2ko = "";
    if (bl && this.items2.block() == null) {
      String string =
          this.items2.selected =
              this.items2.project.blocks.isEmpty() ? "" : this.items2.project.blocks.getFirst().id;
    }
    if (!bl && this.items2.shape() == null) {
      this.items2.selected =
          this.items2.project.shapes.isEmpty() ? "" : this.items2.project.shapes.getFirst().id;
    }
    this.updateState();
  }

  private ComponentKeyService<?> maoobl7dtufz() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            layoutContainerNode.width(
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                        .height(
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .px(Float.intBitsToFloat(1103101952))))
                                .gap(Float.intBitsToFloat(0x41000000)))
                        .align(ScenePctService.Align.CENTER))
                .flexShrink(0.0f),
        ModuleStudioScreen.createComponentKeyService19(
                this.items2.message, Float.intBitsToFloat(1093664768), -5787963)
            .props(
                sceneTextService ->
                    ((SceneTextService)
                            ((SceneTextService) sceneTextService.id("studio.status")).flex(1.0f))
                        .minWidth(0.0f)),
        ModuleStudioScreen.createComponentKeyService19(
                this.items2.project.shapes.size()
                    + " shapes \u00b7 "
                    + this.items2.project.blocks.size()
                    + " blocks",
                Float.intBitsToFloat(1092616192),
                -5787963)
            .props(
                sceneTextService ->
                    sceneTextService.visible(this.value2 >= Float.intBitsToFloat(1142292480))));
  }

  private ComponentKeyService<?> createComponentKeyService8() {
    return this.meeqrbovpqe2();
  }

  private ComponentKeyService<?> createComponentKeyService9() {
    return this.mdrogl6fbkup();
  }

  private ComponentKeyService<?> createComponentKeyService10() {
    return this.createComponentKeyService17();
  }

  private ComponentKeyService<?> meeqrbovpqe2() {
    StudioShapeService.Block block;
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    StudioShapeService.Shape shape = this.items2.logic ? null : this.items2.shape();
    StudioShapeService.Block block2 = block = this.items2.logic ? this.items2.block() : null;
    if (shape != null) {
      this.updateState5(shape, arrayList);
    } else if (block != null) {
      this.updateState6(block, arrayList);
    } else {
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService19(
              "PROJECT", Float.intBitsToFloat(1092616192), -3096579));
      arrayList.add(
          this.createComponentKeyService13(
              "studio.project-name",
              "Name",
              this.items2.project.name,
              string -> {
                if (string.isBlank() || string.length() > 48) {
                  this.msihguhqsap("Use a name with 1\u201348 characters.");
                  return;
                }
                this.items2.edit(
                    studioShapeService -> {
                      studioShapeService.name = string.strip();
                    });
              }));
      arrayList.add(
          ComponentBoxService.row(
              layoutContainerNode ->
                  ((LayoutContainerNode)
                          layoutContainerNode.width(
                              dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                  Float.intBitsToFloat(1120403456))))
                      .gap(Float.intBitsToFloat(0x41000000)),
              this.createComponentKeyService14(
                  "studio.artboard.width",
                  "Width",
                  this.items2.project.width,
                  Float.intBitsToFloat(0x42000000),
                  Float.intBitsToFloat(1153957888),
                  f ->
                      this.items2.edit(
                          studioShapeService -> {
                            studioShapeService.width = f.floatValue();
                          })),
              this.createComponentKeyService14(
                  "studio.artboard.height",
                  "Height",
                  this.items2.project.height,
                  Float.intBitsToFloat(0x42000000),
                  Float.intBitsToFloat(1148846080),
                  f ->
                      this.items2.edit(
                          studioShapeService -> {
                            studioShapeService.height = f.floatValue();
                          }))));
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService19(
                  "Shapes create the look. Logic connects live data to their properties.",
                  Float.intBitsToFloat(0x41400000),
                  -5787963)
              .props(sceneTextService -> sceneTextService.wordWrap(true)));
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.export", "Export project", this::m7hzyslkgqmz, false)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
      if (this.renderer.published(this.items2.project.id)) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.unpublish", "Remove installed module", this::muyuxbyd7jv, false)
                .props(
                    materialJoinedService ->
                        materialJoinedService.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456)))));
      }
    }
    boolean bl =
        this.layoutOperationHandler != null
            || !CoreIsInitializedHandler.isReady()
            || Minecraft.getInstance().level == null;
    arrayList.replaceAll(
        componentKeyService ->
            componentKeyService.props(
                scenePctService ->
                    ((ScenePctService)
                            ((ScenePctService)
                                    scenePctService.width(
                                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                            .percent(Float.intBitsToFloat(1120403456))))
                                .flex(0.0f))
                        .flexShrink(0.0f)));
    return ComponentBoxService.panel(
            sceneCornerRadiusService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                sceneCornerRadiusService.id(
                                                                    "studio.inspector"))
                                                            .width(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.px(
                                                                    this.value2
                                                                            >= Float.intBitsToFloat(
                                                                                1148846080)
                                                                        ? Float.intBitsToFloat(
                                                                            1131937792)
                                                                        : (this.value2
                                                                                < Float
                                                                                    .intBitsToFloat(
                                                                                        0x44480000)
                                                                            ? Float.intBitsToFloat(
                                                                                1133248512)
                                                                            : Float.intBitsToFloat(
                                                                                1129840640)))))
                                                    .height(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456))))
                                            .flexShrink(0.0f))
                                    .backgroundColor(-14998990)
                                    .cornerRadius(Float.intBitsToFloat(1101004800))
                                    .border(Float.intBitsToFloat(1061158912), -12827301)
                                    .padding(Float.intBitsToFloat(0x41400000)))
                            .gap(Float.intBitsToFloat(0x41000000)))
                    .direction(ScenePctService.Direction.COLUMN),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            layoutContainerNode.width(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456))))
                        .align(ScenePctService.Align.CENTER),
                ModuleStudioScreen.createComponentKeyService19(
                        "LIVE PREVIEW", Float.intBitsToFloat(1092616192), -6366515)
                    .props(sceneTextService -> sceneTextService.flex(1.0f)),
                ModuleStudioScreen.createComponentKeyService19(
                    bl ? "Sample data" : "Game data", Float.intBitsToFloat(1092616192), -5787963)),
            ComponentBoxService.node(
                    "studio-preview",
                    () ->
                        new StudioComponent(
                            this.items2, this::createFrame, () -> Float.valueOf(this.value4)),
                    studioComponent ->
                        ((StudioComponent)
                                ((StudioComponent)
                                        ((StudioComponent) studioComponent.id("studio.preview"))
                                            .width(
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.percent(
                                                    Float.intBitsToFloat(1120403456))))
                                    .height(
                                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                            this.value3 < Float.intBitsToFloat(1140457472)
                                                ? Float.intBitsToFloat(1119354880)
                                                : Float.intBitsToFloat(1124990976))))
                            .flexShrink(0.0f),
                    new ComponentKeyService[0])
                .key("preview"),
            ComponentBoxService.row(
                layoutContainerNode ->
                    ((LayoutContainerNode)
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            layoutContainerNode.width(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                        .height(
                                                            dev.felix.ellice.ui.scene.layout
                                                                .LayoutOperationHandler.px(
                                                                Float.intBitsToFloat(1103101952))))
                                                .gap(Float.intBitsToFloat(0x41000000)))
                                        .align(ScenePctService.Align.CENTER))
                                .visible((bl && this.layoutOperationHandler == null ? 1 : 0) != 0))
                        .flexShrink(0.0f),
                ModuleStudioScreen.createComponentKeyService19(
                    "Health", Float.intBitsToFloat(1092616192), -5787963),
                ComponentBoxService.node(
                    "studio-sample-slider",
                    SliderControl::new,
                    sliderControl ->
                        ((SliderControl)
                                ((SliderControl)
                                        ((SliderControl)
                                                ((SliderControl)
                                                        sliderControl
                                                            .material(true)
                                                            .compact(true)
                                                            .range(
                                                                0.0f,
                                                                Float.intBitsToFloat(1101004800))
                                                            .step(1.0f)
                                                            .value(this.text5.health)
                                                            .id("studio.preview-health"))
                                                    .height(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.px(
                                                            Float.intBitsToFloat(1103101952))))
                                            .flex(1.0f))
                                    .minWidth(0.0f))
                            .onChange(
                                f -> {
                                  this.text5.health = f.floatValue();
                                }),
                    new ComponentKeyService[0])),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            sceneCornerRadiusService.size(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456)),
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(1.0f)))
                        .backgroundColor(-12827301)
                        .pointerEvents(false),
                new ComponentKeyService[0]),
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
                                                                                                    "studio.properties"))
                                                                                        .width(
                                                                                            dev
                                                                                                .felix
                                                                                                .ellice
                                                                                                .ui
                                                                                                .scene
                                                                                                .layout
                                                                                                .LayoutOperationHandler
                                                                                                .percent(
                                                                                                    Float
                                                                                                        .intBitsToFloat(
                                                                                                            1120403456))))
                                                                                .flex(1.0f))
                                                                        .minHeight(0.0f))
                                                                .padding(
                                                                    0.0f,
                                                                    Float.intBitsToFloat(
                                                                        0x40800000),
                                                                    Float.intBitsToFloat(
                                                                        0x40C00000),
                                                                    0.0f))
                                                        .gap(Float.intBitsToFloat(1092616192)))
                                                .scrollable(true))
                                        .scrollbarWidth(2.0f))
                                .scrollbarColor(-12827301))
                        .clip(true),
                (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)))
        .key("inspector");
  }

  private void updateState5(StudioShapeService.Shape shape, List<ComponentKeyService<?>> list) {
    StudioShapeService.Shape shape2;
    list.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                layoutContainerNode.width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .gap(Float.intBitsToFloat(0x40C00000)))
                    .align(ScenePctService.Align.CENTER),
            ModuleStudioScreen.createComponentKeyService19(
                    shape.kind.name(), Float.intBitsToFloat(1092616192), -3096579)
                .props(sceneTextService -> sceneTextService.flex(1.0f)),
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.select-project",
                    "Project",
                    () -> {
                      ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
                      this.items2.select("");
                    },
                    false)
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService.height(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                        Float.intBitsToFloat(1105199104))))
                            .padding(0.0f, Float.intBitsToFloat(0x41000000)))));
    list.add(
        this.createComponentKeyService13(
            "studio.shape.name",
            "Name",
            shape.name,
            string -> {
              if (string.isBlank() || string.length() > 48) {
                this.msihguhqsap("Use a shape name with 1\u201348 characters.");
                return;
              }
              this.items2.edit(
                  studioShapeService -> {
                    shape.name = string;
                  });
            }));
    list.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x41000000)),
            this.createComponentKeyService14(
                "studio.shape.x",
                "X",
                shape.x,
                Float.intBitsToFloat(-981860352),
                Float.intBitsToFloat(1165623296),
                f ->
                    this.items2.edit(
                        studioShapeService -> {
                          shape.x = f.floatValue();
                        })),
            this.createComponentKeyService14(
                "studio.shape.y",
                "Y",
                shape.y,
                Float.intBitsToFloat(-981860352),
                Float.intBitsToFloat(1165623296),
                f ->
                    this.items2.edit(
                        studioShapeService -> {
                          shape.y = f.floatValue();
                        }))));
    list.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x41000000)),
            this.createComponentKeyService14(
                "studio.shape.width",
                "Width",
                shape.width,
                Float.intBitsToFloat(0x40800000),
                Float.intBitsToFloat(1157234688),
                f ->
                    this.items2.edit(
                        studioShapeService -> {
                          shape.width = f.floatValue();
                        })),
            this.createComponentKeyService14(
                "studio.shape.height",
                "Height",
                shape.height,
                Float.intBitsToFloat(0x40800000),
                Float.intBitsToFloat(1157234688),
                f ->
                    this.items2.edit(
                        studioShapeService -> {
                          shape.height = f.floatValue();
                        }))));
    if (shape.kind == StudioShapeService.ShapeKind.TEXT) {
      list.add(
          this.createComponentKeyService13(
              "studio.shape.text",
              "Text \u00b7 {player.name}, {client.fps}",
              shape.text,
              string ->
                  this.items2.edit(
                      studioShapeService -> {
                        shape.text = string;
                      })));
      list.add(
          this.createComponentKeyService14(
              "studio.shape.font",
              "Font size",
              shape.fontSize,
              Float.intBitsToFloat(0x41000000),
              Float.intBitsToFloat(1126170624),
              f ->
                  this.items2.edit(
                      studioShapeService -> {
                        shape.fontSize = f.floatValue();
                      })));
      list.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.shape.alignment",
              "Align \u00b7 " + shape.alignment.name().toLowerCase(Locale.ROOT),
              () ->
                  this.updateState8(
                      "Text alignment",
                      List.of("Left", "Center", "Right"),
                      n ->
                          this.items2.edit(
                              studioShapeService -> {
                                shape.alignment = StudioShapeService.TextAlignment.values()[n];
                              })),
              false));
    }
    if (shape.kind != StudioShapeService.ShapeKind.GROUP) {
      list.add(
          this.createComponentKeyService15(
              "studio.shape.color",
              "Color",
              shape.color,
              n ->
                  this.items2.edit(
                      studioShapeService -> {
                        shape.color = n;
                      })));
      if (shape.kind != StudioShapeService.ShapeKind.TEXT) {
        list.add(
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.shape.finish",
                    "Finish \u00b7 " + shape.finish.name().toLowerCase(Locale.ROOT),
                    () ->
                        this.updateState8(
                            "Surface finish",
                            List.of("Solid", "Gradient", "Aurora"),
                            n ->
                                this.items2.edit(
                                    studioShapeService -> {
                                      shape.finish = StudioShapeService.Finish.values()[n];
                                    })),
                    false)
                .props(
                    materialJoinedService ->
                        materialJoinedService.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456)))));
        if (shape.finish != StudioShapeService.Finish.SOLID) {
          list.add(
              this.createComponentKeyService15(
                  "studio.shape.color2",
                  "Second color",
                  shape.color2,
                  n ->
                      this.items2.edit(
                          studioShapeService -> {
                            shape.color2 = n;
                          })));
        }
        list.add(
            this.createComponentKeyService14(
                "studio.shape.radius",
                shape.kind == StudioShapeService.ShapeKind.RING ? "Ring width" : "Corner radius",
                shape.kind == StudioShapeService.ShapeKind.RING ? shape.stroke : shape.radius,
                shape.kind == StudioShapeService.ShapeKind.RING ? 1.0f : 0.0f,
                shape.kind == StudioShapeService.ShapeKind.RING
                    ? Float.intBitsToFloat(1120403456)
                    : Float.intBitsToFloat(1140457472),
                f ->
                    this.items2.edit(
                        studioShapeService -> {
                          if (shape.kind == StudioShapeService.ShapeKind.RING) {
                            shape.stroke = f.floatValue();
                          } else {
                            shape.radius = f.floatValue();
                          }
                        })));
      }
      list.add(
          this.createComponentKeyService14(
              "studio.shape.opacity",
              "Opacity \u00b7 0\u20131",
              shape.opacity,
              0.0f,
              1.0f,
              f ->
                  this.items2.edit(
                      studioShapeService -> {
                        shape.opacity = f.floatValue();
                      })));
    }
    list.add(
        ModuleStudioScreen.createComponentKeyService20(
                "studio.shape.parent",
                "Group \u00b7 "
                    + ((shape2 = this.items2.project.shape(shape.parent)) == null
                        ? "Artboard"
                        : shape2.name),
                () -> {
                  List<StudioShapeService.Shape> availableGroups =
                      this.items2.project.shapes.stream()
                          .filter(
                              candidateShape ->
                                  candidateShape.kind == StudioShapeService.ShapeKind.GROUP
                                      && this.items2.parentError(candidateShape.id) == null)
                          .toList();
                  ArrayList<String> arrayList = new ArrayList<String>(List.of("Artboard"));
                  arrayList.addAll(
                      availableGroups.stream().map(candidateShape -> candidateShape.name).toList());
                  this.updateState8(
                      "Parent group",
                      arrayList,
                      n ->
                          this.items2.edit(
                              studioShapeService -> {
                                float[] fArray = this.createFloat(shape.parent);
                                String string =
                                    n == 0 ? "" : availableGroups.get(n.intValue() - 1).id;
                                float[] fArray2 = this.createFloat(string);
                                shape.x =
                                    StudioValidateValidator.clamp(
                                        shape.x + fArray[0] - fArray2[0],
                                        Float.intBitsToFloat(-981860352),
                                        Float.intBitsToFloat(1165623296));
                                shape.y =
                                    StudioValidateValidator.clamp(
                                        shape.y + fArray[1] - fArray2[1],
                                        Float.intBitsToFloat(-981860352),
                                        Float.intBitsToFloat(1165623296));
                                shape.parent = string;
                              }));
                },
                false)
            .props(
                materialJoinedService ->
                    materialJoinedService.width(
                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                            Float.intBitsToFloat(1120403456)))));
    list.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40800000)),
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.shape.visible",
                    shape.visible ? "Visible" : "Hidden",
                    () ->
                        this.items2.edit(
                            studioShapeService -> {
                              shape.visible = !shape.visible;
                            }),
                    shape.visible)
                .props(materialJoinedService -> materialJoinedService.flex(1.0f)),
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.shape.lock",
                    shape.locked ? "Unlock" : "Lock",
                    () ->
                        this.items2.edit(
                            studioShapeService -> {
                              shape.locked = !shape.locked;
                            }),
                    shape.locked)
                .props(materialJoinedService -> materialJoinedService.flex(1.0f))));
    list.add(
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40800000)),
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.shape.backward",
                    "Back",
                    () -> this.updateState15(() -> this.items2.reorder(-1)),
                    false)
                .props(materialJoinedService -> materialJoinedService.flex(1.0f)),
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.shape.forward",
                    "Front",
                    () -> this.updateState15(() -> this.items2.reorder(1)),
                    false)
                .props(materialJoinedService -> materialJoinedService.flex(1.0f))));
    list.add(this.createComponentKeyService12());
  }

  private float[] createFloat(String string) {
    float f = 0.0f;
    float f2 = 0.0f;
    StudioShapeService.Shape shape = this.items2.project.shape(string);
    while (shape != null) {
      f += shape.x;
      f2 += shape.y;
      shape = this.items2.project.shape(shape.parent);
    }
    return new float[] {f, f2};
  }

  private void updateState6(StudioShapeService.Block block, List<ComponentKeyService<?>> list) {
    list.add(
        ModuleStudioScreen.createComponentKeyService19(
            block.kind.group.toUpperCase(Locale.ROOT),
            Float.intBitsToFloat(1092616192),
            block.kind.color()));
    list.add(
        ModuleStudioScreen.createComponentKeyService19(
            block.kind.title, Float.intBitsToFloat(1099431936), -1447948));
    if (block.kind == StudioMode.NUMBER) {
      list.add(
          this.createComponentKeyService14(
              "studio.block.number",
              "Value",
              block.number,
              Float.intBitsToFloat(-943501312),
              Float.intBitsToFloat(1203982336),
              f ->
                  this.items2.edit(
                      studioShapeService -> {
                        block.number = f.floatValue();
                      })));
    }
    if (block.kind == StudioMode.TEXT
        || block.kind == StudioMode.FORMAT
        || block.kind == StudioMode.CAPTION) {
      list.add(
          this.createComponentKeyService13(
              "studio.block.text",
              block.kind == StudioMode.FORMAT ? "Template \u00b7 {value}" : "Text",
              block.text,
              string ->
                  this.items2.edit(
                      studioShapeService -> {
                        block.text = string;
                      })));
    }
    if (block.kind == StudioMode.COLOR
        || block.kind == StudioMode.TINT
        || block.kind == StudioMode.MIX_COLOR) {
      list.add(
          this.createComponentKeyService15(
              "studio.block.color",
              "Default color",
              block.color,
              n ->
                  this.items2.edit(
                      studioShapeService -> {
                        block.color = n;
                      })));
    }
    if (block.kind.shapeOutput()) {
      StudioShapeService.Shape shape = this.items2.project.shape(block.target);
      list.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.block.target",
                  shape == null ? "Choose a shape" : shape.name,
                  () -> {
                    List<StudioShapeService.Shape> targetShapes =
                        List.copyOf(this.items2.project.shapes);
                    this.updateState8(
                        "Target shape",
                        targetShapes.stream().map(targetShape -> targetShape.name).toList(),
                        n ->
                            this.items2.edit(
                                studioShapeService -> {
                                  block.target = targetShapes.get(n.intValue()).id;
                                }));
                  },
                  false)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
    } else if (block.kind == StudioMode.MODULE_GATE) {
      list.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.block.target",
                  block.target.isBlank() ? "Choose a module" : block.target,
                  () -> {
                    List<String> moduleNames = this.renderer.moduleNames();
                    this.updateState8(
                        "Target module",
                        moduleNames,
                        n ->
                            this.items2.edit(
                                studioShapeService -> {
                                  block.target = moduleNames.get(n);
                                }));
                  },
                  false)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
      list.add(
          ModuleStudioScreen.createComponentKeyService19(
                  "Runs while your module is enabled. The previous state is restored when it"
                      + " stops.",
                  Float.intBitsToFloat(1093664768),
                  -5787963)
              .props(sceneTextService -> sceneTextService.wordWrap(true)));
    }
    for (StudioMode.Port port : block.kind.ports) {
      StudioShapeService.Block block2 = this.items2.project.block(block.inputs.get(port.name()));
      list.add(
          ModuleStudioScreen.createComponentKeyService19(
              port.name() + " \u00b7 " + port.type().name().toLowerCase(Locale.ROOT),
              Float.intBitsToFloat(1092616192),
              port.type().ink));
      list.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.port." + port.name(),
                  (String) (block2 == null ? "Use a block\u2026" : "\u21b3 " + block2.kind.title),
                  () -> {
                    List<StudioShapeService.Block> compatibleBlocks =
                        this.items2.project.blocks.stream()
                            .filter(
                                candidateBlock ->
                                    candidateBlock.kind.output == port.type()
                                        && !candidateBlock.id.equals(block.id))
                            .toList();
                    ArrayList<String> arrayList =
                        new ArrayList<String>(List.of("Use default value"));
                    arrayList.addAll(
                        compatibleBlocks.stream()
                            .map(
                                candidateBlock ->
                                    candidateBlock.kind.title
                                        + (candidateBlock.kind == StudioMode.NUMBER
                                            ? " \u00b7 "
                                                + ModuleStudioScreen.createText(
                                                    candidateBlock.number)
                                            : ""))
                            .toList());
                    this.updateState8(
                        "Connect " + port.name(),
                        arrayList,
                        n -> {
                          if (n == 0) {
                            this.items2.disconnect(block.id, port.name());
                          } else {
                            this.items2.connect(
                                compatibleBlocks.get(n.intValue() - 1).id, block.id, port.name());
                          }
                        });
                  },
                  false)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
      if (block2 == null && port.type() == StudioMode.Type.NUMBER) {
        list.add(
            this.createComponentKeyService14(
                "studio.default." + port.name(),
                "Default",
                block
                    .defaults
                    .getOrDefault(port.name(), Float.valueOf(port.fallback()))
                    .floatValue(),
                Float.intBitsToFloat(-943501312),
                Float.intBitsToFloat(1203982336),
                f ->
                    this.items2.edit(
                        studioShapeService -> block.defaults.put(port.name(), (Float) f))));
      }
      if (block2 != null || port.type() != StudioMode.Type.BOOLEAN) continue;
      boolean bl =
          block.defaults.getOrDefault(port.name(), Float.valueOf(port.fallback())).floatValue()
              != 0.0f;
      list.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.default." + port.name(),
                  bl ? "True" : "False",
                  () ->
                      this.items2.edit(
                          studioShapeService ->
                              block.defaults.put(port.name(), Float.valueOf(bl ? 0.0f : 1.0f))),
                  bl)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
    }
    list.add(this.createComponentKeyService12());
  }

  private ComponentKeyService<?> createComponentKeyService12() {
    return ComponentBoxService.row(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    layoutContainerNode.width(
                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                            Float.intBitsToFloat(1120403456))))
                .gap(Float.intBitsToFloat(0x40800000)),
        ModuleStudioScreen.createComponentKeyService20(
                "studio.duplicate",
                "Duplicate",
                () -> this.updateState15(this.items2::duplicate),
                false)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService) materialJoinedService.flex(1.0f))
                                .minWidth(0.0f))
                        .padding(0.0f, Float.intBitsToFloat(0x40C00000))),
        ModuleStudioScreen.createComponentKeyService20(
                "studio.delete", "Delete", () -> this.updateState15(this.items2::delete), false)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService) materialJoinedService.flex(1.0f))
                                .minWidth(0.0f))
                        .padding(0.0f, Float.intBitsToFloat(0x40C00000))));
  }

  private ComponentKeyService<?> createComponentKeyService13(
      String string, String string2, String string3, Consumer<String> consumer) {
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        layoutContainerNode.width(
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .percent(Float.intBitsToFloat(1120403456))))
                                    .minWidth(0.0f))
                            .gap(Float.intBitsToFloat(0x40800000)))
                    .flexShrink(0.0f),
            ModuleStudioScreen.createComponentKeyService19(
                    string2, Float.intBitsToFloat(1092616192), -5787963)
                .props(
                    sceneTextService ->
                        sceneTextService.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456)))),
            ComponentBoxService.node(
                    "studio-input",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService -> {
                      ModuleStudioScreen.updateState21(controlLetterSpacingService);
                      ((ControlLetterSpacingService) controlLetterSpacingService.id(string))
                          .width(
                              dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                  Float.intBitsToFloat(1120403456)));
                      if (!controlLetterSpacingService.focused()) {
                        controlLetterSpacingService.text(string3);
                      }
                      controlLetterSpacingService
                          .onSubmit(consumer)
                          .onUnfocus(
                              () -> {
                                if (!this.fo3nquhz6bp) {
                                  consumer.accept(controlLetterSpacingService.text());
                                }
                              });
                    },
                    new ComponentKeyService[0])
                .key(string))
        .key("field:" + string);
  }

  private ComponentKeyService<?> createComponentKeyService14(
      String string,
      String string3,
      float f,
      float minimum,
      float maximum,
      Consumer<Float> consumer) {
    return this.createComponentKeyService13(
            string,
            string3,
            ModuleStudioScreen.createText(f),
            string2 -> {
              try {
                float parsedValue = Float.parseFloat(string2.strip().replace(',', '.'));
                if (!Float.isFinite(parsedValue)
                    || parsedValue < minimum
                    || parsedValue > maximum) {
                  throw new NumberFormatException();
                }
                consumer.accept(parsedValue);
              } catch (NumberFormatException numberFormatException) {
                this.msihguhqsap(
                    string3
                        + ": enter a value from "
                        + ModuleStudioScreen.createText(minimum)
                        + " to "
                        + ModuleStudioScreen.createText(maximum)
                        + ".");
              }
            })
        .props(
            scenePctService ->
                ((ScenePctService)
                        ((ScenePctService)
                                scenePctService.width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto()))
                            .flex(1.0f))
                    .minWidth(0.0f));
  }

  private ComponentKeyService<?> createComponentKeyService15(
      String string2, String string3, int n, IntConsumer intConsumer) {
    ArrayList<ComponentKeyService<MaterialJoinedService>> arrayList =
        new ArrayList<ComponentKeyService<MaterialJoinedService>>();
    arrayList.add(
        ModuleStudioScreen.createComponentKeyService20(
                string2 + ".picker",
                "Mix",
                () -> {
                  this.fr70cu2sdes = intConsumer;
                  this.mcxoirceyord(n);
                  this.updateState14("Color \u00b7 " + string3);
                },
                false)
            .props(
                materialJoinedService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    materialJoinedService.height(
                                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                            Float.intBitsToFloat(1106247680))))
                                .padding(0.0f, Float.intBitsToFloat(0x40E00000)))
                        .tooltip("Open the color mixer")));
    for (int n2 : new int[] {-4344587, -7086900, -1001069, -856583, -14208444}) {
      arrayList.add(
          ComponentBoxService.node(
              "studio-swatch",
              MaterialJoinedService::new,
              materialJoinedService ->
                  ((SceneCornerRadiusService)
                          ((SceneCornerRadiusService)
                                  ((SceneCornerRadiusService)
                                          ((SceneCornerRadiusService)
                                                  ((SceneCornerRadiusService)
                                                          materialJoinedService
                                                              .colors(0, -1447948)
                                                              .shape(
                                                                  Float.intBitsToFloat(0x41000000),
                                                                  Float.intBitsToFloat(1104150528))
                                                              .id(
                                                                  string2
                                                                      + ".swatch."
                                                                      + Integer.toHexString(n2)))
                                                      .height(
                                                          dev.felix.ellice.ui.scene.layout
                                                              .LayoutOperationHandler.px(
                                                              Float.intBitsToFloat(1106247680))))
                                              .flex(1.0f))
                                      .minWidth(0.0f))
                              .padding(Float.intBitsToFloat(0x40400000)))
                      .onClick(() -> intConsumer.accept(n2)),
              ComponentBoxService.node(
                  "studio-color",
                  MaterialColorService::new,
                  materialColorService ->
                      ((MaterialColorService)
                              materialColorService
                                  .color(n2)
                                  .radius(Float.intBitsToFloat(0x40C00000))
                                  .size(
                                      dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                          .percent(Float.intBitsToFloat(1120403456)),
                                      dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                          .percent(Float.intBitsToFloat(1120403456))))
                          .pointerEvents(false),
                  new ComponentKeyService[0])));
    }
    return ComponentBoxService.column(
        layoutContainerNode ->
            ((LayoutContainerNode)
                    layoutContainerNode.width(
                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                            Float.intBitsToFloat(1120403456))))
                .gap(Float.intBitsToFloat(0x40800000)),
        this.createComponentKeyService13(
            string2,
            string3,
            String.format(Locale.ROOT, "#%08X", n),
            string -> {
              String colorHex = string.strip().replace("#", "");
              if (!colorHex.matches("[0-9A-Fa-f]{6}|[0-9A-Fa-f]{8}")) {
                this.msihguhqsap("Use #RRGGBB or #AARRGGBB.");
                return;
              }
              long l = Long.parseLong(colorHex, 16);
              intConsumer.accept((int) (colorHex.length() == 6 ? 0xFF000000L | l : l));
            }),
        ComponentBoxService.row(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        layoutContainerNode.width(
                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                Float.intBitsToFloat(1120403456))))
                    .gap(Float.intBitsToFloat(0x40400000)),
            (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)));
  }

  private static String createText(float f) {
    return new BigDecimal(Float.toString(f)).stripTrailingZeros().toPlainString();
  }

  private void msihguhqsap(String string) {
    this.items2.message = string;
    this.updateState();
  }

  private void updateState8(String string, List<String> list, Consumer<Integer> consumer) {
    this.text4 = list;
    this.consumer = consumer;
    this.updateState14(string);
  }

  private ComponentKeyService<?> mdrogl6fbkup() {
    return ComponentBoxService.box(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode) layoutContainerNode.absolute())
                                    .inset(
                                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                            0.0f)))
                            .size(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto(),
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.auto()))
                    .layerBreak(true),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.absolute())
                                                .inset(
                                                    dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(0.0f)))
                                        .size(
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .auto(),
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .auto()))
                                .backgroundColor(-2063200998)
                                .interactive(true))
                        .onClick(
                            () -> {
                              this.enabled4 = false;
                              this.enabled3 = false;
                              this.updateState();
                            }),
                new ComponentKeyService[0]),
            ComponentBoxService.column(
                    layoutContainerNode ->
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode) layoutContainerNode.absolute())
                                            .inset(
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1115684864)),
                                                this.enabled4
                                                    ? dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(0x41000000))
                                                    : dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.auto(),
                                                dev.felix.ellice.ui.scene.layout
                                                    .LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1108344832)),
                                                this.enabled3
                                                    ? dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(
                                                        Float.intBitsToFloat(0x41000000))
                                                    : dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.auto()))
                                    .width(
                                        dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                            this.enabled4
                                                ? Math.min(
                                                    Float.intBitsToFloat(1133248512),
                                                    this.value2 - Float.intBitsToFloat(1098907648))
                                                : Float.intBitsToFloat(1130889216))))
                            .minHeight(0.0f),
                    this.enabled4 ? this.createComponentKeyService8() : this.mexsbgng1sfx())
                .key(this.enabled4 ? "inspect-drawer" : "library-drawer")
                .onMount(
                    layoutContainerNode -> {
                      layoutContainerNode.translateX(
                          this.enabled4
                              ? Float.intBitsToFloat(1099956224)
                              : Float.intBitsToFloat(-1047527424));
                      MotionAnimateService.animate(
                          layoutContainerNode,
                          MotionColorsContainer.Floats.TRANSLATE_X,
                          0.0f,
                          (SceneEaseHandler) MaterialEnterService.PAGE);
                    }))
        .key("drawer");
  }

  private ComponentKeyService<?> createComponentKeyService17() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (this.text3.startsWith("Color \u00b7 ")) {
      arrayList.add(
          MaterialTabsService.tabs(
              "studio.color-mode",
              this.fplk09zljn3,
              colorFeatureType -> {
                this.fplk09zljn3 = colorFeatureType;
                this.updateState();
              }));
      arrayList.add(
          ComponentBoxService.node(
              "studio-color-preview",
              MaterialColorService::new,
              materialColorService ->
                  ((MaterialColorService)
                          materialColorService
                              .color(this.calculateValue())
                              .radius(Float.intBitsToFloat(1096810496))
                              .size(
                                  dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                      Float.intBitsToFloat(1120403456)),
                                  dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                      Float.intBitsToFloat(1110441984))))
                      .pointerEvents(false),
              new ComponentKeyService[0]));
      if (this.fplk09zljn3 == ColorFeatureType.TRIANGLE) {
        arrayList.add(
            ComponentBoxService.node(
                    "studio-color-triangle",
                    HueTrianglePicker::new,
                    hueTrianglePicker ->
                        ((HueTrianglePicker)
                                hueTrianglePicker
                                    .hue(this.value5)
                                    .sat(this.value6)
                                    .bri(this.value7)
                                    .onHueChange(
                                        f -> {
                                          this.value5 = f.floatValue();
                                          this.updateState();
                                        })
                                    .onSBChange(
                                        (f, f2) -> {
                                          this.value6 = f.floatValue();
                                          this.value7 = f2.floatValue();
                                          this.updateState();
                                        })
                                    .id("studio.color-triangle"))
                            .size(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456)),
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                    Float.intBitsToFloat(1130102784))),
                    new ComponentKeyService[0])
                .key("triangle"));
      } else {
        arrayList.add(
            ComponentBoxService.node(
                    "studio-color-field",
                    SaturationBrightnessPicker::new,
                    saturationBrightnessPicker ->
                        ((SaturationBrightnessPicker)
                                saturationBrightnessPicker
                                    .material(true)
                                    .hue(this.value5)
                                    .sat(this.value6)
                                    .bri(this.value7)
                                    .cornerRadius(Float.intBitsToFloat(1098907648))
                                    .onChange(
                                        (f, f2) -> {
                                          this.value6 = f.floatValue();
                                          this.value7 = f2.floatValue();
                                          this.updateState();
                                        })
                                    .id("studio.color-field"))
                            .size(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456)),
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                    Float.intBitsToFloat(1125908480))),
                    new ComponentKeyService[0])
                .key("sv"));
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService19(
                "Hue", Float.intBitsToFloat(1092616192), -5787963));
        arrayList.add(
            ComponentBoxService.node(
                    "studio-hue",
                    ColorGetAnimPropertyService::new,
                    colorGetAnimPropertyService ->
                        ((ColorGetAnimPropertyService)
                                colorGetAnimPropertyService
                                    .material(true)
                                    .hue(this.value5)
                                    .cornerRadius(Float.intBitsToFloat(0x41400000))
                                    .onChange(
                                        f -> {
                                          this.value5 = f.floatValue();
                                          this.updateState();
                                        })
                                    .id("studio.color-hue"))
                            .size(
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                    Float.intBitsToFloat(1120403456)),
                                dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                    Float.intBitsToFloat(0x42000000))),
                    new ComponentKeyService[0])
                .key("hue"));
      }
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService19(
              "Alpha", Float.intBitsToFloat(1092616192), -5787963));
      arrayList.add(
          ComponentBoxService.node(
                  "studio-alpha",
                  AlphaSliderControl::new,
                  alphaSliderControl ->
                      ((AlphaSliderControl)
                              alphaSliderControl
                                  .material(true)
                                  .hue(this.value5)
                                  .sat(this.value6)
                                  .bri(this.value7)
                                  .alpha(this.value8)
                                  .cornerRadius(Float.intBitsToFloat(0x41400000))
                                  .onChange(
                                      f -> {
                                        this.value8 = f.floatValue();
                                        this.updateState();
                                      })
                                  .id("studio.color-alpha"))
                          .size(
                              dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                  Float.intBitsToFloat(1120403456)),
                              dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                                  Float.intBitsToFloat(0x42000000))),
                  new ComponentKeyService[0])
              .key("alpha"));
      arrayList.add(
          this.createComponentKeyService13(
              "studio.color-hex",
              "Hex",
              String.format(Locale.ROOT, "#%08X", this.calculateValue()),
              string -> {
                String string2 = string.strip().replace("#", "");
                if (!string2.matches("[0-9A-Fa-f]{6}|[0-9A-Fa-f]{8}")) {
                  this.msihguhqsap("Use #RRGGBB or #AARRGGBB.");
                  return;
                }
                this.mcxoirceyord(
                    (int)
                        (Long.parseLong(string2, 16) | (string2.length() == 6 ? 0xFF000000L : 0L)));
                this.updateState();
              }));
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
                  "studio.color.apply",
                  "Apply color",
                  () -> {
                    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
                    this.fr70cu2sdes.accept(this.calculateValue());
                    this.text3 = "";
                    this.updateState();
                  },
                  true)
              .props(
                  materialJoinedService ->
                      materialJoinedService.width(
                          dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                              Float.intBitsToFloat(1120403456)))));
    } else if (this.text3.equals("New project")) {
      arrayList.add(
          this.mgksk3el38x(
              "studio.template.orbit",
              "Vital orbit",
              "A health ring, live text and connected data.",
              StudioOrbitService::orbit));
      arrayList.add(
          this.mgksk3el38x(
              "studio.template.session",
              "Session glass",
              "An FPS card with a soft gradient surface.",
              StudioOrbitService::session));
      arrayList.add(
          this.mgksk3el38x(
              "studio.template.pulse",
              "Signal garden",
              "Time, a wave and a breathing shape.",
              StudioOrbitService::pulse));
      arrayList.add(
          this.mgksk3el38x(
              "studio.template.blank",
              "Blank canvas",
              "Start with your own shapes and ideas.",
              StudioOrbitService::blank));
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.show-projects",
              "Open saved projects",
              () -> {
                this.text3 = "Projects";
                this.updateState();
              },
              false));
    } else if (this.text3.equals("Projects")) {
      for (StudioShapeService studioShapeService : this.renderer.projects()) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.project." + studioShapeService.id,
                    studioShapeService.name
                        + (this.renderer.published(studioShapeService.id)
                            ? " \u00b7 Installed"
                            : " \u00b7 Draft"),
                    () -> this.updateState10(studioShapeService),
                    false)
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService.width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .justify(ScenePctService.Justify.START)));
      }
      if (this.renderer.projects().isEmpty()) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService19(
                    "Save a draft to add it to your project library.",
                    Float.intBitsToFloat(1095761920),
                    -5787963)
                .props(sceneTextService -> sceneTextService.wordWrap(true)));
      }
      arrayList.add(
          this.createComponentKeyService13(
              "studio.import-path",
              "Import a .ellice-module.json file",
              this.text6,
              string -> {
                this.text6 = string;
              }));
      arrayList.add(
          ModuleStudioScreen.createComponentKeyService20(
              "studio.import", "Import project", this::mbzubvm8vnej, false));
      if (!this.renderer.warnings().isEmpty()) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService19(
                    "Some saved files could not be loaded. Their originals were kept.",
                    Float.intBitsToFloat(1093664768),
                    -1001069)
                .props(sceneTextService -> sceneTextService.wordWrap(true)));
      }
    } else {
      for (int i = 0; i < this.text4.size(); ++i) {
        int n = i;
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService20(
                    "studio.choice." + i,
                    this.text4.get(i),
                    () -> {
                      this.text3 = "";
                      this.consumer.accept(n);
                      this.updateState();
                    },
                    false)
                .props(
                    materialJoinedService ->
                        ((SceneCornerRadiusService)
                                materialJoinedService.width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .justify(ScenePctService.Justify.START)));
      }
      if (this.text4.isEmpty()) {
        arrayList.add(
            ModuleStudioScreen.createComponentKeyService19(
                    "Add a compatible item to the project first.",
                    Float.intBitsToFloat(1095761920),
                    -5787963)
                .props(sceneTextService -> sceneTextService.wordWrap(true)));
      }
    }
    arrayList.replaceAll(
        componentKeyService ->
            componentKeyService.props(scenePctService -> scenePctService.flexShrink(0.0f)));
    float f =
        Math.min(
            Math.max(
                Float.intBitsToFloat(1120403456), this.value3 - Float.intBitsToFloat(0x42000000)),
            this.text3.equals("New project")
                ? Float.intBitsToFloat(1137836032)
                : (this.text3.startsWith("Color \u00b7 ")
                    ? Float.intBitsToFloat(0x440E0000)
                    : (float) Math.min(520, 112 + arrayList.size() * 64)));
    return ComponentBoxService.column(
            layoutContainerNode ->
                ((LayoutContainerNode)
                        ((LayoutContainerNode)
                                ((LayoutContainerNode)
                                        ((LayoutContainerNode)
                                                ((LayoutContainerNode)
                                                        ((LayoutContainerNode)
                                                                ((LayoutContainerNode)
                                                                        layoutContainerNode.id(
                                                                            "studio.modal"))
                                                                    .absolute())
                                                            .inset(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.px(
                                                                    0.0f)))
                                                    .size(
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.auto(),
                                                        dev.felix.ellice.ui.scene.layout
                                                            .LayoutOperationHandler.auto()))
                                            .padding(Float.intBitsToFloat(1098907648)))
                                    .layerBreak(true))
                            .align(ScenePctService.Align.CENTER))
                    .justify(ScenePctService.Justify.CENTER),
            ComponentBoxService.panel(
                sceneCornerRadiusService ->
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    ((SceneCornerRadiusService)
                                            ((SceneCornerRadiusService)
                                                    sceneCornerRadiusService.absolute())
                                                .inset(
                                                    dev.felix.ellice.ui.scene.layout
                                                        .LayoutOperationHandler.px(0.0f)))
                                        .size(
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .auto(),
                                            dev.felix.ellice.ui.scene.layout.LayoutOperationHandler
                                                .auto()))
                                .backgroundColor(-1442312936)
                                .interactive(true))
                        .onClick(
                            () -> {
                              this.text3 = "";
                              this.updateState();
                            }),
                new ComponentKeyService[0]),
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
                                                                                        ((SceneCornerRadiusService)
                                                                                                sceneCornerRadiusService
                                                                                                    .id(
                                                                                                        "studio.dialog"))
                                                                                            .width(
                                                                                                dev
                                                                                                    .felix
                                                                                                    .ellice
                                                                                                    .ui
                                                                                                    .scene
                                                                                                    .layout
                                                                                                    .LayoutOperationHandler
                                                                                                    .percent(
                                                                                                        Float
                                                                                                            .intBitsToFloat(
                                                                                                                1120403456))))
                                                                                    .maxWidth(
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1138491392)))
                                                                            .height(
                                                                                dev.felix.ellice.ui
                                                                                    .scene.layout
                                                                                    .LayoutOperationHandler
                                                                                    .px(f)))
                                                                    .minHeight(0.0f))
                                                            .backgroundColor(-14998990)
                                                            .border(1.0f, -12827301)
                                                            .cornerRadius(
                                                                Float.intBitsToFloat(1103101952))
                                                            .padding(
                                                                Float.intBitsToFloat(1101004800)))
                                                    .gap(Float.intBitsToFloat(0x41400000)))
                                            .direction(ScenePctService.Direction.COLUMN))
                                    .stopPropagation(true))
                            .clip(true),
                    ComponentBoxService.row(
                        layoutContainerNode ->
                            ((LayoutContainerNode)
                                    ((LayoutContainerNode)
                                            ((LayoutContainerNode)
                                                    ((LayoutContainerNode)
                                                            layoutContainerNode.width(
                                                                dev.felix.ellice.ui.scene.layout
                                                                    .LayoutOperationHandler.percent(
                                                                    Float.intBitsToFloat(
                                                                        1120403456))))
                                                        .height(
                                                            dev.felix.ellice.ui.scene.layout
                                                                .LayoutOperationHandler.px(
                                                                Float.intBitsToFloat(1108344832))))
                                                .flexShrink(0.0f))
                                        .gap(Float.intBitsToFloat(0x41000000)))
                                .align(ScenePctService.Align.CENTER),
                        ModuleStudioScreen.createComponentKeyService19(
                                this.text3.startsWith("Color \u00b7 ") ? "Color mixer" : this.text3,
                                Float.intBitsToFloat(1101004800),
                                -1447948)
                            .props(
                                sceneTextService ->
                                    ((SceneTextService) sceneTextService.flex(1.0f))
                                        .minWidth(0.0f)),
                        ModuleStudioScreen.createComponentKeyService20(
                            "studio.modal.close",
                            "\u00d7",
                            () -> {
                              this.text3 = "";
                              this.updateState();
                            },
                            false)),
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
                                                                                                    "studio.dialog-scroll"))
                                                                                        .width(
                                                                                            dev
                                                                                                .felix
                                                                                                .ellice
                                                                                                .ui
                                                                                                .scene
                                                                                                .layout
                                                                                                .LayoutOperationHandler
                                                                                                .percent(
                                                                                                    Float
                                                                                                        .intBitsToFloat(
                                                                                                            1120403456))))
                                                                                .flex(1.0f))
                                                                        .minHeight(0.0f))
                                                                .gap(
                                                                    Float.intBitsToFloat(
                                                                        0x41000000)))
                                                        .scrollable(true))
                                                .scrollbarWidth(2.0f))
                                        .scrollbarColor(-12827301))
                                .clip(true),
                        (ComponentKeyService[]) arrayList.toArray(ComponentKeyService[]::new)))
                .onMount(
                    sceneCornerRadiusService -> {
                      sceneCornerRadiusService.translateY(Float.intBitsToFloat(1096810496));
                      MotionAnimateService.animate(
                          sceneCornerRadiusService,
                          MotionColorsContainer.Floats.TRANSLATE_Y,
                          0.0f,
                          (SceneEaseHandler) MaterialEnterService.PAGE);
                    }))
        .key("modal");
  }

  private int calculateValue() {
    return Color.HSBtoRGB(this.value5, this.value6, this.value7) & 0xFFFFFF
        | Math.round(this.value8 * Float.intBitsToFloat(1132396544)) << 24;
  }

  private void mcxoirceyord(int n) {
    float[] fArray = Color.RGBtoHSB(n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, null);
    this.value5 = fArray[0];
    this.value6 = fArray[1];
    this.value7 = fArray[2];
    this.value8 = (float) (n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
  }

  private ComponentKeyService<?> mgksk3el38x(
      String string, String string2, String string3, Supplier<StudioShapeService> supplier) {
    return ComponentBoxService.node(
            "studio-template",
            MaterialJoinedService::new,
            materialJoinedService ->
                ((SceneCornerRadiusService)
                        ((SceneCornerRadiusService)
                                ((SceneCornerRadiusService)
                                        ((SceneCornerRadiusService)
                                                ((SceneCornerRadiusService)
                                                        ((SceneCornerRadiusService)
                                                                ((SceneCornerRadiusService)
                                                                        ((SceneCornerRadiusService)
                                                                                materialJoinedService
                                                                                    .colors(
                                                                                        -14077369,
                                                                                        -1447948)
                                                                                    .shape(
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1096810496),
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1115160576))
                                                                                    .id(string))
                                                                            .width(
                                                                                dev.felix.ellice.ui
                                                                                    .scene.layout
                                                                                    .LayoutOperationHandler
                                                                                    .percent(
                                                                                        Float
                                                                                            .intBitsToFloat(
                                                                                                1120403456))))
                                                                    .height(
                                                                        dev.felix.ellice.ui.scene
                                                                            .layout
                                                                            .LayoutOperationHandler
                                                                            .px(
                                                                                Float
                                                                                    .intBitsToFloat(
                                                                                        1115947008))))
                                                            .padding(
                                                                Float.intBitsToFloat(1092616192),
                                                                Float.intBitsToFloat(1096810496)))
                                                    .flexShrink(0.0f))
                                            .direction(ScenePctService.Direction.COLUMN))
                                    .gap(2.0f))
                            .justify(ScenePctService.Justify.CENTER))
                    .onClick(() -> this.m58dr25q30qk((Supplier) supplier)),
            ModuleStudioScreen.createComponentKeyService19(
                string2, Float.intBitsToFloat(1096810496), -1447948),
            ModuleStudioScreen.createComponentKeyService19(
                    string3, Float.intBitsToFloat(1093664768), -5787963)
                .props(
                    sceneTextService ->
                        ((SceneTextService)
                                sceneTextService.width(
                                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456))))
                            .wordWrap(true)))
        .key(string);
  }

  private void updateState10(StudioShapeService studioShapeService) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      this.renderer.save(this.items2.project);
    } catch (IOException iOException) {
      this.msihguhqsap(iOException.getMessage());
      return;
    }
    this.items2 = new StudioListenerService(studioShapeService);
    this.items2.listener(this::updateState);
    this.value4 = 0.0f;
    this.text3 = "";
    this.enabled2 = false;
    this.faswzxueb2ko = "";
    this.scenePctService = null;
    this.updateState();
  }

  private void m7hzyslkgqmz() {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      Path path = this.renderer.exportProject(this.items2.project);
      this.msihguhqsap("Exported to " + String.valueOf(path.toAbsolutePath()));
    } catch (IOException iOException) {
      this.msihguhqsap(iOException.getMessage());
    }
  }

  private void mbzubvm8vnej() {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      this.updateState10(this.renderer.importProject(Path.of(this.text6.strip(), new String[0])));
    } catch (Exception exception) {
      this.msihguhqsap("Import failed: " + exception.getMessage());
    }
  }

  private void muyuxbyd7jv() {
    try {
      this.renderer.unpublish(this.items2.project.id);
      if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
        CoreIsInitializedHandler.get().config().save();
      }
      this.msihguhqsap("Installed module removed. Your draft is kept.");
    } catch (IOException iOException) {
      this.msihguhqsap(iOException.getMessage());
    }
  }

  private void updateState14(String string) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    this.text3 = string;
    this.updateState();
  }

  private void updateState15(Runnable runnable) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    runnable.run();
  }

  private boolean checkCondition2() {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      this.renderer.save(this.items2.project);
      this.items2.saved();
      return true;
    } catch (IOException iOException) {
      this.items2.message = iOException.getMessage();
      this.updateState();
      return false;
    }
  }

  private void updateState16() {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      this.renderer.publish(this.items2.project);
      this.items2.saved();
      this.items2.message =
          "Module ready \u00b7 ClickGUI \u2192 HUD \u2192 " + this.items2.project.name;
      if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
        CoreIsInitializedHandler.get().config().save();
      }
      this.updateState();
    } catch (IOException iOException) {
      this.items2.message = iOException.getMessage();
      this.updateState();
    }
  }

  private void updateState17() {
    if (this.checkCondition2() && this.screenScreenIdService != null) {
      this.screenScreenIdService.open("clickgui");
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
    try {
      this.renderer.save(this.items2.project);
    } catch (IOException iOException) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Could not save Module Studio draft", (Throwable) iOException);
    }
    this.fo3nquhz6bp = true;
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int n, int n2) {
    int n3;
    int n4 = n3 = (n2 & 0xA) != 0 ? 1 : 0;
    if (n == 256) {
      this.m8lski6g0f3w();
      ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
      if (!this.text3.isEmpty()) {
        this.text3 = "";
        this.updateState();
      } else if (this.enabled3 || this.enabled4) {
        this.enabled4 = false;
        this.enabled3 = false;
        this.updateState();
      } else if (this.studioZoomService != null && this.studioZoomService.cancelInteraction()) {
        this.msihguhqsap("Gesture cancelled");
      } else {
        this.updateState17();
      }
      return true;
    }
    if (n3 != 0 && n == 83) {
      this.checkCondition2();
      return true;
    }
    if (n3 != 0 && n == 70 && this.text3.isEmpty()) {
      ScenePctService<?> scenePctService;
      this.m8lski6g0f3w();
      ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
      if (this.value2 < Float.intBitsToFloat(0x44480000)) {
        this.enabled3 = true;
        this.enabled4 = false;
        this.updateState();
      }
      if ((scenePctService = this.materialTerrainTooltipsService.findById("studio.library-search"))
          instanceof ControlLetterSpacingService) {
        ControlLetterSpacingService controlLetterSpacingService =
            (ControlLetterSpacingService) scenePctService;
        if (CoreIsInitializedHandler.isReady()) {
          CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacingService);
        } else {
          controlLetterSpacingService.focus();
        }
        controlLetterSpacingService.selectAll();
      }
      return true;
    }
    if (n == 258) {
      ArrayList arrayList = new ArrayList();
      ModuleStudioScreen.updateState19(this.materialTerrainTooltipsService, arrayList);
      if (!arrayList.isEmpty()) {
        ScenePctService scenePctService;
        ScenePctService scenePctService2 = this.scenePctService;
        for (ScenePctService scenePctService3 :
            (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
          if (!(scenePctService3 instanceof ControlLetterSpacingService)
              || !((ControlLetterSpacingService)
                      (scenePctService = (ControlLetterSpacingService) scenePctService3))
                  .focused()) continue;
          scenePctService2 = scenePctService3;
        }
        int n5 = arrayList.indexOf(scenePctService2);
        int n6 = (n2 & 1) != 0 ? 1 : 0;
        this.m8lski6g0f3w();
        ModuleStudioScreen.updateState22(this.materialTerrainTooltipsService);
        arrayList.clear();
        ModuleStudioScreen.updateState19(this.materialTerrainTooltipsService, arrayList);
        if (arrayList.isEmpty()) {
          return true;
        }
        this.scenePctService =
            (ScenePctService)
                arrayList.get(
                    n5 < 0
                        ? (n6 != 0 ? arrayList.size() - 1 : 0)
                        : Math.floorMod(n5 + (n6 != 0 ? -1 : 1), arrayList.size()));
        ScenePctService<?> scenePctService4 = this.scenePctService;
        if (scenePctService4 instanceof MaterialJoinedService) {
          scenePctService = (MaterialJoinedService) scenePctService4;
          ((MaterialJoinedService) scenePctService).keyboardFocused(true);
        }
        if ((scenePctService4 = this.scenePctService) instanceof StudioActionsService) {
          scenePctService = (StudioActionsService) scenePctService4;
          ((StudioActionsService) scenePctService).focus(true);
        }
        if ((scenePctService4 = this.scenePctService) instanceof ControlLetterSpacingService) {
          scenePctService = (ControlLetterSpacingService) scenePctService4;
          if (CoreIsInitializedHandler.isReady()) {
            CoreIsInitializedHandler.get()
                .scene()
                .setFocusedInput((ControlLetterSpacingService) scenePctService);
          } else {
            ((ControlLetterSpacingService) scenePctService).focus();
          }
        }
        ModuleStudioScreen.updateState20(this.scenePctService);
      }
      return true;
    }
    if (ModuleStudioScreen.checkCondition3(this.materialTerrainTooltipsService)) {
      return false;
    }
    if ((n == 257 || n == 32) && ModuleStudioScreen.mcoee0flezbr(this.scenePctService)) {
      SceneCornerRadiusService sceneCornerRadiusService;
      ScenePctService<?> scenePctService = this.scenePctService;
      if (scenePctService instanceof MaterialJoinedService
          && ((MaterialJoinedService)
                  (sceneCornerRadiusService = (MaterialJoinedService) scenePctService))
              .available()) {
        ((MaterialJoinedService) sceneCornerRadiusService).activate();
        return true;
      }
      scenePctService = this.scenePctService;
      if (scenePctService instanceof StudioActionsService) {
        sceneCornerRadiusService = (StudioActionsService) scenePctService;
        ((StudioActionsService) sceneCornerRadiusService).activate();
        return true;
      }
    }
    if (!this.text3.isEmpty() || this.enabled3 || this.enabled4) {
      return false;
    }
    if (n3 != 0 && n == 90) {
      if ((n2 & 1) != 0) {
        this.items2.redo();
      } else {
        this.items2.undo();
      }
      return true;
    }
    if (n3 != 0 && n == 89) {
      this.items2.redo();
      return true;
    }
    if (n3 != 0 && n == 68) {
      this.items2.duplicate();
      return true;
    }
    if (n == 261 || n == 259) {
      this.items2.delete();
      return true;
    }
    if (n == 32) {
      this.items2.playing = !this.items2.playing;
      this.updateState();
      return true;
    }
    return false;
  }

  private void m8lski6g0f3w() {
    SceneCornerRadiusService sceneCornerRadiusService;
    ScenePctService<?> scenePctService = this.scenePctService;
    if (scenePctService instanceof MaterialJoinedService) {
      sceneCornerRadiusService = (MaterialJoinedService) scenePctService;
      ((MaterialJoinedService) sceneCornerRadiusService).keyboardFocused(false);
    }
    if ((scenePctService = this.scenePctService) instanceof StudioActionsService) {
      sceneCornerRadiusService = (StudioActionsService) scenePctService;
      ((StudioActionsService) sceneCornerRadiusService).focus(false);
    }
    this.scenePctService = null;
  }

  private static boolean checkCondition3(ScenePctService<?> scenePctService) {
    Object object;
    if (scenePctService == null) {
      return false;
    }
    if (scenePctService instanceof ControlLetterSpacingService
        && ((ControlLetterSpacingService) (object = (ControlLetterSpacingService) scenePctService))
            .focused()) {
      return true;
    }
    for (ScenePctService scenePctService2 : scenePctService.children()) {
      if (!ModuleStudioScreen.checkCondition3(scenePctService2)) continue;
      return true;
    }
    return false;
  }

  private static boolean mcoee0flezbr(ScenePctService<?> scenePctService) {
    if (scenePctService == null) {
      return false;
    }
    for (ScenePctService<?> scenePctService2 = scenePctService;
        scenePctService2 != null;
        scenePctService2 = scenePctService2.parent()) {
      if (scenePctService2.visible && scenePctService2.pointerEvents()) continue;
      return false;
    }
    return true;
  }

  private static void updateState19(
      ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
    Object object;
    if (scenePctService == null || !scenePctService.visible || !scenePctService.pointerEvents()) {
      return;
    }
    if (scenePctService instanceof MaterialJoinedService
            && ((MaterialJoinedService) (object = (MaterialJoinedService) scenePctService))
                .available()
        || scenePctService instanceof ControlLetterSpacingService
        || scenePctService instanceof StudioActionsService) {
      list.add(scenePctService);
    }
    for (ScenePctService scenePctService2 : scenePctService.children()) {
      ModuleStudioScreen.updateState19(scenePctService2, list);
    }
  }

  private static void updateState20(ScenePctService<?> scenePctService) {
    for (ScenePctService<?> scenePctService2 = scenePctService.parent();
        scenePctService2 != null;
        scenePctService2 = scenePctService2.parent()) {
      if (!(scenePctService2.scrollMin() < 0.0f)) continue;
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
          (SceneEaseHandler) MaterialEnterService.PAGE);
    }
  }

  private static ComponentKeyService<SceneTextService> createComponentKeyService19(
      String string, float f, int n) {
    return MaterialTextService.text(string, f, n)
        .props(
            sceneTextService ->
                sceneTextService
                    .lineHeight(f + Float.intBitsToFloat(0x40A00000))
                    .letterSpacing(Float.intBitsToFloat(0x3DCCCCCD)));
  }

  private static ComponentKeyService<MaterialJoinedService> createComponentKeyService20(
      String string, String string2, Runnable runnable, boolean bl) {
    return ComponentBoxService.node(
            "studio-button",
            MaterialJoinedService::new,
            materialJoinedService -> {
              materialJoinedService
                  .colors(bl ? -3096579 : -14077369, bl ? -13622199 : -1447948)
                  .shape(Float.intBitsToFloat(0x41400000), Float.intBitsToFloat(1106247680));
              ((SceneCornerRadiusService)
                      ((SceneCornerRadiusService)
                              ((SceneCornerRadiusService)
                                      ((SceneCornerRadiusService)
                                              ((SceneCornerRadiusService)
                                                      ((SceneCornerRadiusService)
                                                              ((SceneCornerRadiusService)
                                                                      ((SceneCornerRadiusService)
                                                                              materialJoinedService
                                                                                  .id(string))
                                                                          .height(
                                                                              dev.felix.ellice.ui
                                                                                  .scene.layout
                                                                                  .LayoutOperationHandler
                                                                                  .px(
                                                                                      Float
                                                                                          .intBitsToFloat(
                                                                                              1108344832))))
                                                                  .minWidth(
                                                                      Float.intBitsToFloat(
                                                                          0x42000000)))
                                                          .padding(
                                                              0.0f,
                                                              Float.intBitsToFloat(1092616192)))
                                                  .flexShrink(0.0f))
                                          .direction(ScenePctService.Direction.ROW))
                                  .align(ScenePctService.Align.CENTER))
                          .justify(ScenePctService.Justify.CENTER))
                  .onClick(runnable);
            },
            ModuleStudioScreen.createComponentKeyService19(
                    string2, Float.intBitsToFloat(1093664768), bl ? -13622199 : -1447948)
                .props(sceneTextService -> sceneTextService.fontFamily("material-roboto-medium")))
        .key(string);
  }

  private static void updateState21(ControlLetterSpacingService controlLetterSpacingService) {
    MaterialTextService.input(controlLetterSpacingService);
    ((ControlLetterSpacingService)
            controlLetterSpacingService
                .fontSize(Float.intBitsToFloat(0x41400000))
                .letterSpacing(Float.intBitsToFloat(0x3DCCCCCD))
                .bgColor(-14077369)
                .focusBorder(-3096579)
                .cornerRadius(Float.intBitsToFloat(0x40C00000))
                .maxLength(256)
                .height(
                    dev.felix.ellice.ui.scene.layout.LayoutOperationHandler.px(
                        Float.intBitsToFloat(1107820544))))
        .minWidth(0.0f);
  }

  private static void updateState22(ScenePctService<?> scenePctService) {
    Object object;
    if (scenePctService == null) {
      return;
    }
    if (scenePctService instanceof ControlLetterSpacingService
        && ((ControlLetterSpacingService) (object = (ControlLetterSpacingService) scenePctService))
            .focused()) {
      ((ControlLetterSpacingService) object).unfocus();
    }
    for (ScenePctService scenePctService2 : List.copyOf(scenePctService.children())) {
      ModuleStudioScreen.updateState22(scenePctService2);
    }
  }

  private void m58dr25q30qk(Supplier supplier) {
    this.updateState10((StudioShapeService) supplier.get());
  }
}
