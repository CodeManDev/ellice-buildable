package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.CompatRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.Module;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.module.ModuleEntriesService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleIdService;
import dev.felix.ellice.module.ModuleLayoutService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleRegisterService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.render3d.geometry.GeometryCubeService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.component.ComponentUnmountService;
import dev.felix.ellice.ui.material.AnimatedSelectionIndicator;
import dev.felix.ellice.ui.material.ExpandableMaterialPanel;
import dev.felix.ellice.ui.material.InteractiveSurfacePanel;
import dev.felix.ellice.ui.material.MaterialCheckboxValidator;
import dev.felix.ellice.ui.material.MaterialColorService;
import dev.felix.ellice.ui.material.MaterialComponent;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialExpandedService;
import dev.felix.ellice.ui.material.MaterialFindByIdSelector;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialLabelService;
import dev.felix.ellice.ui.material.MaterialPaletteService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialSelectionSelector;
import dev.felix.ellice.ui.material.MaterialTabsService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.color.AlphaSliderControl;
import dev.felix.ellice.ui.scene.color.ColorFeatureType;
import dev.felix.ellice.ui.scene.color.ColorGetAnimPropertyService;
import dev.felix.ellice.ui.scene.color.HueTrianglePicker;
import dev.felix.ellice.ui.scene.color.SaturationBrightnessPicker;
import dev.felix.ellice.ui.scene.control.ControlCompactService;
import dev.felix.ellice.ui.scene.control.ControlGetAnimPropertyService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.control.ControlOnPreviewHandler;
import dev.felix.ellice.ui.scene.control.ControlSettingService;
import dev.felix.ellice.ui.scene.control.CurveGraphControl;
import dev.felix.ellice.ui.scene.control.NumberSettingControl;
import dev.felix.ellice.ui.scene.control.RangeSliderControl;
import dev.felix.ellice.ui.scene.control.SliderControl;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import dev.felix.ellice.ui.screen.builtin.inventory.InventoryRenderer;
import dev.felix.ellice.ui.screen.builtin.keybind.KeyCatalog;
import dev.felix.ellice.ui.screen.builtin.keybind.KeybindRenderer;
import dev.felix.ellice.ui.screen.builtin.keybind.KeybindTargetService;
import dev.felix.ellice.ui.theme.ThemeCornerData;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.lwjgl.glfw.GLFW;

public final class ClickGuiScreen implements ScreenOperationHandler {
  private ColorFeatureType fcsyblho83aq;
  private static final float fevbacf0qbqe = 40.0f;
  private static final float feam161i1wx3 = 15.0f;
  private static final float f5g3tb8bq2j8 = 72.0f;
  private final Function<ModuleFeatureType, List<Module>> items;
  private final BuiltinRepository builtinRepository;
  private final Map<String, Float> fjlbgzdyzws9;
  private final Map<ModuleFeatureType, List<Module>> fikxlao3mld;
  private MaterialTerrainTooltipsService materialTerrainTooltipsService;
  private ComponentMountService componentMountService;
  private ComponentMountService componentMountService2;
  private LayoutContainerNode sceneComponent4;
  private ScreenScreenIdService screenScreenIdService;
  private ThemeIsSetService values2;
  private ModuleFeatureType moduleFeatureType2;
  private Module moduleOperationHandler2;
  private String text4;
  private String text5;
  private String text6;
  private boolean enabled2;
  private final Set<String> text7;
  private final Map<String, String> text8;
  private final Set<String> fcijmdt1zfva;
  private boolean enabled3;
  private boolean enabled4;
  private boolean ftzcjcss2ew;
  private boolean enabled6;
  private int count;
  private float value5;
  private float value6;
  private ModuleSetting<?> moduleNameService2;
  private BuiltinOriginalService builtinOriginalService;
  private BuiltinRangeService fbknrfxp9co;
  private ModuleSetting.Number moduleNumber;
  private ScenePctService<?> scenePctService;
  private final ModuleBindService moduleBindService;
  private KeybindRenderer renderer;
  private InventoryRenderer renderer2;
  private BuiltinRenderer renderer3;
  private ComponentKeyService<?> fahprko4fthe;
  private long fgcmhfutlzbp;
  private int count2;
  private ComponentMountService figehearx1so;
  private LayoutContainerNode sceneComponent42;
  private static final ModuleFeatureType[] fdivljsc8kbu =
      (ModuleFeatureType[])
          ModuleFeatureType.navigation()
              .toArray(
                  i -> {
                    return new ModuleFeatureType[i];
                  });
  private static final MotionFiniteService motionFiniteService =
      MotionFiniteService.finite(
          "knobPosition",
          scenePctService -> {
            return scenePctService instanceof ControlCompactService;
          });
  private static final List<String> f89n839yrbtw =
      List.of(
          "clickgui.categories",
          "clickgui.module-list",
          "clickgui.settings",
          "clickgui.settings-groups",
          "clickgui.presets.choices");

  public ClickGuiScreen() {
    this.fcsyblho83aq = ColorFeatureType.GRADIENT;
    this.fjlbgzdyzws9 = new HashMap();
    this.fikxlao3mld = new EnumMap(ModuleFeatureType.class);
    this.moduleFeatureType2 = ModuleFeatureType.VISUALS;
    this.text4 = "";
    this.text5 = "";
    this.text6 = "";
    this.text7 = new HashSet();
    this.text8 = new HashMap();
    this.fcijmdt1zfva = new HashSet();
    this.value5 = Float.intBitsToFloat(1142292480);
    this.value6 = Float.intBitsToFloat(1150681088);
    this.moduleBindService = new ModuleBindService();
    this.items = null;
    this.builtinRepository = new BuiltinRepository();
  }

  public ClickGuiScreen(ModuleRegisterService moduleRegisterService) {
    this(
        (Function<ModuleFeatureType, List<Module>>)
            moduleFeatureType -> {
              return moduleRegisterService.byCategory(moduleFeatureType).toList();
            });
  }

  public ClickGuiScreen(
      ModuleRegisterService moduleRegisterService, BuiltinRepository builtinRepository) {
    this(
        (Function<ModuleFeatureType, List<Module>>)
            moduleFeatureType -> {
              return moduleRegisterService.byCategory(moduleFeatureType).toList();
            },
        builtinRepository);
  }

  public ClickGuiScreen(Function<ModuleFeatureType, List<Module>> function) {
    this(function, new BuiltinRepository());
  }

  public ClickGuiScreen(
      Function<ModuleFeatureType, List<Module>> function, BuiltinRepository builtinRepository) {
    this.fcsyblho83aq = ColorFeatureType.GRADIENT;
    this.fjlbgzdyzws9 = new HashMap();
    this.fikxlao3mld = new EnumMap(ModuleFeatureType.class);
    this.moduleFeatureType2 = ModuleFeatureType.VISUALS;
    this.text4 = "";
    this.text5 = "";
    this.text6 = "";
    this.text7 = new HashSet();
    this.text8 = new HashMap();
    this.fcijmdt1zfva = new HashSet();
    this.value5 = Float.intBitsToFloat(1142292480);
    this.value6 = Float.intBitsToFloat(1150681088);
    this.moduleBindService = new ModuleBindService();
    this.items = (Function) Objects.requireNonNull(function);
    this.builtinRepository = (BuiltinRepository) Objects.requireNonNull(builtinRepository);
  }

  @Override
  public String id() {
    return "clickgui";
  }

  @Override
  public SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.WORKSPACE;
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
    updateState22();
    this.screenScreenIdService = screenScreenIdService;
    this.values2 =
        screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme();
    this.fikxlao3mld.clear();
    ModuleFeatureType[] moduleFeatureTypeArrValues = ModuleFeatureType.values();
    int length = moduleFeatureTypeArrValues.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= length) {
        break;
      }
      ModuleFeatureType moduleFeatureType = moduleFeatureTypeArrValues[i2];
      this.fikxlao3mld.put(moduleFeatureType, collectValues(moduleFeatureType));
      i = i2 + 1;
    }
    BuiltinRepository.State state =
        ThemeCornerData.current().rememberClickGui()
            ? this.builtinRepository.state()
            : BuiltinRepository.State.initial();
    this.moduleFeatureType2 =
        ModuleFeatureType.find(state.category()).orElse(ModuleFeatureType.VISUALS);
    this.text4 = state.query();
    this.text5 = state.settingsQuery();
    this.text6 = state.settingsGroup();
    this.enabled3 = state.enabledOnly();
    this.enabled6 = state.detailVisible();
    this.enabled2 = state.showHelp();
    this.text7.clear();
    this.text7.addAll(state.collapsedSections());
    this.text8.clear();
    this.text8.putAll(state.presetChoices());
    this.fcijmdt1zfva.clear();
    this.fcijmdt1zfva.addAll(state.expandedPresets());
    this.moduleOperationHandler2 =
        (Module)
            this.fikxlao3mld.values().stream()
                .flatMap(
                    (v0) -> {
                      return v0.stream();
                    })
                .filter(
                    module -> {
                      return m1vcviafemyn(module).equals(state.selectedModule());
                    })
                .findFirst()
                .orElse(null);
    if (this.moduleOperationHandler2 != null) {
      this.moduleFeatureType2 = this.moduleOperationHandler2.category();
    } else if (this.fikxlao3mld.get(this.moduleFeatureType2).isEmpty()) {
      this.moduleFeatureType2 =
          (ModuleFeatureType)
              Arrays.stream(fdivljsc8kbu)
                  .filter(
                      moduleFeatureType2 -> {
                        return !this.fikxlao3mld.get(moduleFeatureType2).isEmpty();
                      })
                  .findFirst()
                  .orElse(
                      this.fikxlao3mld.get(ModuleFeatureType.CLIENT).isEmpty()
                          ? ModuleFeatureType.VISUALS
                          : ModuleFeatureType.CLIENT);
    }
    if (!collectValues2().contains(this.moduleOperationHandler2)) {
      this.moduleOperationHandler2 = null;
    }
    if (this.moduleOperationHandler2 == null) {
      this.moduleOperationHandler2 = collectValues2().stream().findFirst().orElse(null);
      this.text6 = "";
      this.text5 = "";
    }
    this.fjlbgzdyzws9.clear();
    this.fjlbgzdyzws9.putAll(state.scroll());
    if (ThemeCornerData.current().focusSearch()) {
      this.enabled6 = false;
    }
    this.enabled4 = this.value6 < Float.intBitsToFloat(1146224640);
    this.ftzcjcss2ew = this.value6 < Float.intBitsToFloat(1142292480);
    this.fahprko4fthe = null;
    this.fgcmhfutlzbp++;
    this.scenePctService = null;
    this.materialTerrainTooltipsService = new MaterialTerrainTooltipsService();
    this.materialTerrainTooltipsService
        .id("clickgui.panel-shell")
        .size(
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
            LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
        .direction(ScenePctService.Direction.NONE)
        .backgroundColor(1375731712)
        .interactive(true);
    this.materialTerrainTooltipsService.onLayout(this::updateState12);
    this.componentMountService =
        new ComponentMountService().mount(this::createComponentKeyService, this.values2);
    this.componentMountService
        .absolute()
        .inset(LayoutOperationHandler.px(0.0f))
        .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
    this.materialTerrainTooltipsService.addChild(this.componentMountService);
    this.count = calculateValue2();
    return this.materialTerrainTooltipsService;
  }

  private List<Module> collectValues(ModuleFeatureType moduleFeatureType) {
    List<Module> list;
    if (this.items != null) {
      list = this.items.apply(moduleFeatureType);
    } else {
      list =
          CoreIsInitializedHandler.isReady()
              ? CoreIsInitializedHandler.get().modules().byCategory(moduleFeatureType).toList()
              : List.of();
    }
    List<Module> list2 = list;
    return list2 == null
        ? List.of()
        : list2.stream()
            .sorted(
                Comparator.comparing(
                    (v0) -> {
                      return v0.name();
                    },
                    String.CASE_INSENSITIVE_ORDER))
            .toList();
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    if (this.materialTerrainTooltipsService != null) {
      this.materialTerrainTooltipsService.backgroundColor(
          ThemeCornerData.current().dimBackground() ? 1375731712 : 0);
    }
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    arrayList.add(mezavdvwfusb());
    if (this.fahprko4fthe != null) {
      long j = this.fgcmhfutlzbp;
      arrayList.add(
          MaterialFindByIdSelector.of(
              this.fahprko4fthe,
              this.count2,
              j,
              () -> {
                if (this.fgcmhfutlzbp == j) {
                  this.fahprko4fthe = null;
                  mgf9hldmchqz();
                }
              }));
    }
    return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  float fIntBitsToFloat;
                  LayoutContainerNode size =
                      layoutContainerNode.size(
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                  if (this.ftzcjcss2ew) {
                    fIntBitsToFloat = 0.0f;
                  } else {
                    fIntBitsToFloat =
                        this.value5 < Float.intBitsToFloat(1143603200)
                            ? Float.intBitsToFloat(1098907648)
                            : Float.intBitsToFloat(1103101952);
                  }
                  size.padding(fIntBitsToFloat)
                      .align(ScenePctService.Align.CENTER)
                      .justify(ScenePctService.Justify.CENTER);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  ComponentBoxService.panel(
                      sceneCornerRadiusService -> {
                        sceneCornerRadiusService
                            .id("clickgui.workspace")
                            .size(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .maxWidth(Float.intBitsToFloat(1151664128))
                            .maxHeight(Float.intBitsToFloat(1148190720))
                            .cornerRadius(
                                this.ftzcjcss2ew ? 0.0f : Float.intBitsToFloat(1105199104))
                            .backgroundColor(MaterialIsLightService.SURFACE)
                            .direction(ScenePctService.Direction.COLUMN)
                            .clip(true);
                      },
                      mq98d5e3j21(),
                      createComponentKeyService7(),
                      ComponentBoxService.stack(
                          (Consumer<LayoutContainerNode>)
                              layoutContainerNode2 -> {
                                layoutContainerNode2
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)))
                                    .flex(1.0f)
                                    .minHeight(0.0f)
                                    .clip(true);
                              },
                          (ComponentKeyService<?>[])
                              arrayList.toArray(
                                  i -> {
                                    return new ComponentKeyService[i];
                                  })))
                })
        .key("workspace");
  }

  private ComponentKeyService<?> mezavdvwfusb() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    if (!this.enabled4 || !this.enabled6) {
      arrayList.add(mg3ujj02y0vv());
    }
    if (!this.enabled4 || this.enabled6) {
      arrayList.add(createComponentKeyService10());
    }
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  float fIntBitsToFloat;
                  LayoutContainerNode layoutContainerNodeMinHeight =
                      layoutContainerNode
                          .id("clickgui.content")
                          .size(
                              LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                              LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .minHeight(0.0f);
                  if (this.ftzcjcss2ew) {
                    fIntBitsToFloat = this.value5 < Float.intBitsToFloat(1135869952) ? 4 : 12;
                  } else {
                    fIntBitsToFloat =
                        this.value5 < Float.intBitsToFloat(1143603200)
                            ? Float.intBitsToFloat(1098907648)
                            : Float.intBitsToFloat(1101004800);
                  }
                  layoutContainerNodeMinHeight
                      .padding(fIntBitsToFloat)
                      .gap(Float.intBitsToFloat(1101004800))
                      .align(ScenePctService.Align.STRETCH);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }))
        .key("current-page");
  }

  private ComponentKeyService<?> mq98d5e3j21() {
    boolean z = this.value6 < Float.intBitsToFloat(1144258560);
    boolean z2 = this.value5 < Float.intBitsToFloat(1138163712);
    if (this.enabled4 && this.enabled6) {
      Consumer<LayoutContainerNode> consumer =
          layoutContainerNode -> {
            layoutContainerNode
                .id("clickgui.toolbar")
                .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))
                .padding(Float.intBitsToFloat(1082130432))
                .gap(this.ftzcjcss2ew ? 2.0f : Float.intBitsToFloat(1090519040))
                .align(ScenePctService.Align.CENTER)
                .flexShrink(0.0f);
          };
      ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[8];
      componentKeyServiceArr[0] =
          createComponentKeyService28(
                  "clickgui.back", "arrow_back", "Back to modules", this::mkdxr04hjju)
              .props(
                  materialJoinedService -> {
                    materialJoinedService.size(
                        this.ftzcjcss2ew
                            ? Float.intBitsToFloat(1108344832)
                            : Float.intBitsToFloat(1111490560),
                        Float.intBitsToFloat(1109393408));
                  });
      componentKeyServiceArr[1] =
          MaterialTextService.text(
                  this.moduleOperationHandler2 == null
                      ? "Settings"
                      : this.moduleOperationHandler2.name(),
                  Float.intBitsToFloat(1099956224),
                  MaterialIsLightService.ON_SURFACE)
              .props(
                  sceneTextService -> {
                    sceneTextService.flex(1.0f).minWidth(0.0f);
                  });
      componentKeyServiceArr[2] =
          createComponentKeyService6()
              .props(
                  scenePctService -> {
                    scenePctService.size(
                        this.ftzcjcss2ew
                            ? Float.intBitsToFloat(1108344832)
                            : Float.intBitsToFloat(1111490560),
                        Float.intBitsToFloat(1109393408));
                  });
      componentKeyServiceArr[3] =
          createComponentKeyService5()
              .props(
                  scenePctService2 -> {
                    scenePctService2.size(
                        Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1109393408));
                  });
      componentKeyServiceArr[4] =
          createComponentKeyService4()
              .props(
                  scenePctService3 -> {
                    scenePctService3.size(
                        Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1109393408));
                  });
      componentKeyServiceArr[5] =
          createComponentKeyService28(
                  "clickgui.toolbar.keybinds",
                  "keyboard",
                  "Choose a module keybind",
                  this::updateState13)
              .props(
                  materialJoinedService2 -> {
                    materialJoinedService2.size(
                        this.ftzcjcss2ew
                            ? Float.intBitsToFloat(1108344832)
                            : Float.intBitsToFloat(1111490560),
                        Float.intBitsToFloat(1109393408));
                  });
      componentKeyServiceArr[6] =
          this.moduleOperationHandler2 != null
              ? createComponentKeyService12(
                  this.moduleOperationHandler2, "clickgui.toolbar.module-toggle")
              : ComponentBoxService.box(
                  layoutContainerNode2 -> {
                    layoutContainerNode2.visible(false);
                  },
                  new ComponentKeyService[0]);
      componentKeyServiceArr[7] =
          createComponentKeyService28(
                  "clickgui.toolbar.close", "close", "Close · Esc", this::updateState9)
              .props(
                  materialJoinedService3 -> {
                    materialJoinedService3.size(
                        this.ftzcjcss2ew
                            ? Float.intBitsToFloat(1108344832)
                            : Float.intBitsToFloat(1111490560),
                        Float.intBitsToFloat(1109393408));
                  });
      return ComponentBoxService.row(
              (Consumer<LayoutContainerNode>) consumer,
              (ComponentKeyService<?>[]) componentKeyServiceArr)
          .key("toolbar");
    }
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    ComponentKeyService<?> componentKeyServiceKey =
        ComponentBoxService.node(
                "material-search-bar",
                InteractiveSurfacePanel::new,
                interactiveSurfacePanel -> {
                  interactiveSurfacePanel
                      .id("clickgui.search-bar")
                      .height(LayoutOperationHandler.px(Float.intBitsToFloat(1112539136)))
                      .flex(1.0f)
                      .minWidth(0.0f)
                      .cornerRadius(Float.intBitsToFloat(1104150528))
                      .direction(ScenePctService.Direction.ROW)
                      .align(ScenePctService.Align.CENTER)
                      .padding(0.0f, Float.intBitsToFloat(1090519040));
                },
                MaterialTextService.icon("search", MaterialIsLightService.ON_SURFACE)
                    .props(
                        sceneSrcService -> {
                          sceneSrcService.margin(0.0f, Float.intBitsToFloat(1090519040));
                        }),
                ComponentBoxService.node(
                        "material-search",
                        ControlLetterSpacingService::new,
                        controlLetterSpacingService -> {
                          controlLetterSpacingService
                              .id("clickgui.toolbar.search")
                              .materialSearch(true)
                              .fontFamily(MaterialIsLightService.FONT)
                              .fontSize(Float.intBitsToFloat(1097859072))
                              .letterSpacing(Float.intBitsToFloat(1036831949))
                              .maxLength(100)
                              .placeholder(this.ftzcjcss2ew ? "Search" : "Search modules")
                              .bgColor(0)
                              .textColor(MaterialIsLightService.ON_SURFACE)
                              .placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT)
                              .focusBorder(0)
                              .selectionColor(MaterialIsLightService.SELECTION)
                              .cornerRadius(0.0f)
                              .height(
                                  LayoutOperationHandler.px(
                                      (z && z2)
                                          ? Float.intBitsToFloat(1105199104)
                                          : Float.intBitsToFloat(1109393408)))
                              .flex(1.0f)
                              .minWidth(0.0f)
                              .stopPropagation(true)
                              .onChanged(
                                  str -> {
                                    this.text4 = str.strip().toLowerCase(Locale.ROOT);
                                    this.enabled6 = false;
                                    updateState7();
                                    mgf9hldmchqz();
                                  });
                          if (controlLetterSpacingService.focused()) {
                            return;
                          }
                          controlLetterSpacingService.text(this.text4);
                        },
                        new ComponentKeyService[0])
                    .key("search-input"),
                createComponentKeyService28(
                        "clickgui.toolbar.clear",
                        "close",
                        "Clear search",
                        () -> {
                          this.text4 = "";
                          ControlLetterSpacingService controlLetterSpacingService2 =
                              (ControlLetterSpacingService)
                                  this.materialTerrainTooltipsService.findById(
                                      "clickgui.toolbar.search");
                          if (controlLetterSpacingService2 != null) {
                            controlLetterSpacingService2.text("");
                          }
                          updateState7();
                          mgf9hldmchqz();
                        })
                    .props(
                        materialJoinedService4 -> {
                          materialJoinedService4
                              .visible(!this.text4.isEmpty())
                              .size(
                                  (z && z2)
                                      ? Float.intBitsToFloat(1106247680)
                                      : Float.intBitsToFloat(1111490560),
                                  (z && z2)
                                      ? Float.intBitsToFloat(1106247680)
                                      : Float.intBitsToFloat(1111490560));
                        }))
            .key("search");
    if (!z) {
      arrayList.add(componentKeyServiceKey);
    }
    if (z) {
      arrayList.add(
          createComponentKeyService28(
                  "clickgui.toolbar.enabled-filter",
                  this.enabled3 ? "check" : "filter_list",
                  "Show enabled modules",
                  () -> {
                    this.enabled3 = !this.enabled3;
                    updateState7();
                    mgf9hldmchqz();
                  })
              .props(
                  materialJoinedService5 -> {
                    materialJoinedService5.colors(
                        this.enabled3 ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                        MaterialIsLightService.ON_SECONDARY_CONTAINER);
                  }));
    } else {
      Supplier supplier = MaterialJoinedService::new;
      Consumer<MaterialJoinedService> consumer2 =
          materialJoinedService6 -> {
            materialJoinedService6
                .colors(
                    this.enabled3 ? MaterialIsLightService.SECONDARY_CONTAINER : 0,
                    this.enabled3
                        ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                        : MaterialIsLightService.ON_SURFACE_VARIANT)
                .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1109393408))
                .outlined(!this.enabled3);
            materialJoinedService6
                .id("clickgui.toolbar.enabled-filter")
                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                .padding(0.0f, Float.intBitsToFloat(1098907648))
                .gap(Float.intBitsToFloat(1090519040))
                .flexShrink(0.0f)
                .direction(ScenePctService.Direction.ROW)
                .align(ScenePctService.Align.CENTER)
                .onClick(
                    () -> {
                      this.enabled3 = !this.enabled3;
                      updateState7();
                      mgf9hldmchqz();
                    });
          };
      ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
      componentKeyServiceArr2[0] =
          MaterialTextService.icon(
                  this.enabled3 ? "check" : "filter_list",
                  this.enabled3
                      ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                      : MaterialIsLightService.PRIMARY)
              .props(
                  sceneSrcService2 -> {
                    sceneSrcService2.size(
                        Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224));
                  });
      componentKeyServiceArr2[1] =
          MaterialTextService.label(
              "Enabled",
              this.enabled3
                  ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                  : MaterialIsLightService.ON_SURFACE_VARIANT);
      arrayList.add(
          ComponentBoxService.node("material-button", supplier, consumer2, componentKeyServiceArr2)
              .key("filter"));
    }
    arrayList.add(
        z
            ? createComponentKeyService28(
                "clickgui.toolbar.configs",
                "folder",
                "Manage configs",
                () -> {
                  if (this.screenScreenIdService != null) {
                    this.screenScreenIdService.open("configs");
                  }
                })
            : createComponentKeyService6());
    arrayList.add(createComponentKeyService5());
    arrayList.add(createComponentKeyService4());
    arrayList.add(
        createComponentKeyService28(
            "clickgui.toolbar.network",
            "swap_horiz",
            "FRITZ!Box · Reconnect",
            () -> {
              if (this.screenScreenIdService != null) {
                this.screenScreenIdService.open("fritz-box");
              }
            }));
    arrayList.add(
        createComponentKeyService28(
            "clickgui.toolbar.keybinds",
            "keyboard",
            "Choose a keybind for the selected module",
            this::updateState13));
    arrayList.add(
        createComponentKeyService28(
            "clickgui.toolbar.close", "close", "Close · Esc", this::updateState9));
    if (z) {
      return ComponentBoxService.column(
              (Consumer<LayoutContainerNode>)
                  layoutContainerNode3 -> {
                    layoutContainerNode3
                        .id("clickgui.toolbar")
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .height(
                            LayoutOperationHandler.px(
                                z2
                                    ? Float.intBitsToFloat(1116209152)
                                    : Float.intBitsToFloat(1119354880)))
                        .padding(Float.intBitsToFloat(1082130432))
                        .gap(z2 ? 0.0f : 2.0f)
                        .flexShrink(0.0f);
                  },
              (ComponentKeyService<?>[])
                  new ComponentKeyService[] {
                    componentKeyServiceKey.props(
                        interactiveSurfacePanel2 -> {
                          interactiveSurfacePanel2
                              .flex(0.0f)
                              .width(
                                  LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                              .height(
                                  LayoutOperationHandler.px(
                                      z2
                                          ? Float.intBitsToFloat(1106247680)
                                          : Float.intBitsToFloat(1110966272)));
                        }),
                    ComponentBoxService.row(
                        (Consumer<LayoutContainerNode>)
                            layoutContainerNode4 -> {
                              layoutContainerNode4
                                  .width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456)))
                                  .height(
                                      LayoutOperationHandler.px(
                                          z2
                                              ? Float.intBitsToFloat(1106247680)
                                              : Float.intBitsToFloat(1108344832)))
                                  .gap(0.0f)
                                  .justify(ScenePctService.Justify.SPACE_BETWEEN)
                                  .align(ScenePctService.Align.CENTER);
                            },
                        (ComponentKeyService<?>[])
                            arrayList.stream()
                                .map(
                                    componentKeyService -> {
                                      return componentKeyService.props(
                                          scenePctService4 -> {
                                            scenePctService4
                                                .size(
                                                    Float.intBitsToFloat(1108344832),
                                                    z2
                                                        ? Float.intBitsToFloat(1106247680)
                                                        : Float.intBitsToFloat(1108344832))
                                                .minWidth(0.0f);
                                          });
                                    })
                                .toArray(
                                    i -> {
                                      return new ComponentKeyService[i];
                                    }))
                  })
          .key("toolbar");
    }
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode5 -> {
                  layoutContainerNode5
                      .id("clickgui.toolbar")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .height(
                          LayoutOperationHandler.px(
                              this.value5 < Float.intBitsToFloat(1143603200)
                                  ? Float.intBitsToFloat(1116733440)
                                  : Float.intBitsToFloat(1117782016)))
                      .padding(
                          Float.intBitsToFloat(1094713344),
                          this.ftzcjcss2ew
                              ? Float.intBitsToFloat(1082130432)
                              : Float.intBitsToFloat(1103101952))
                      .gap(this.ftzcjcss2ew ? 0.0f : Float.intBitsToFloat(1094713344))
                      .align(ScenePctService.Align.CENTER)
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i2 -> {
                      return new ComponentKeyService[i2];
                    }))
        .key("toolbar");
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    return createComponentKeyService28(
            "clickgui.toolbar.friends",
            "friends",
            "Friends · individual module exclusions",
            () -> {
              if (this.screenScreenIdService != null) {
                this.screenScreenIdService.open("friends");
              }
            })
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1107296256))
                  .surfaceWidth(Float.intBitsToFloat(1107296256));
              materialJoinedService.size(
                  Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
            })
        .children(
            MaterialTextService.icon("friends", MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneSrcService -> {
                      sceneSrcService.size(
                          Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
                    }));
  }

  private ComponentKeyService<?> createComponentKeyService5() {
    return createComponentKeyService28(
            "clickgui.toolbar.anticheats",
            "security",
            "Minecraft Anticheat List",
            () -> {
              if (this.screenScreenIdService != null) {
                this.screenScreenIdService.open("anticheats");
              }
            })
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1107296256))
                  .surfaceWidth(Float.intBitsToFloat(1107296256));
              materialJoinedService.size(
                  Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
            })
        .children(
            MaterialTextService.icon("security", MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneSrcService -> {
                      sceneSrcService.size(
                          Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
                    }));
  }

  private ComponentKeyService<?> createComponentKeyService6() {
    Runnable runnable =
        () -> {
          if (this.screenScreenIdService != null) {
            this.screenScreenIdService.open("configs");
          }
        };
    return (this.ftzcjcss2ew || (this.enabled4 && this.enabled6))
        ? createComponentKeyService28(
            "clickgui.toolbar.configs", "folder", "Manage configs", runnable)
        : createComponentKeyService26("clickgui.toolbar.configs", "Configs", runnable, false)
            .props(
                materialJoinedService -> {
                  materialJoinedService
                      .gap(Float.intBitsToFloat(1090519040))
                      .tooltip("Save, load and manage your configs");
                })
            .children(
                MaterialTextService.icon("folder", MaterialIsLightService.ON_SECONDARY_CONTAINER)
                    .props(
                        sceneSrcService -> {
                          sceneSrcService.size(
                              Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
                        }),
                createComponentKeyService25(
                    "Configs", MaterialIsLightService.ON_SECONDARY_CONTAINER));
  }

  private ComponentKeyService<?> createComponentKeyService7() {
    ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
    ModuleFeatureType[] moduleFeatureTypeArr = fdivljsc8kbu;
    int length = moduleFeatureTypeArr.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= length) {
        return ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .flexShrink(0.0f)
                          .visible((this.enabled4 && this.enabled6) ? false : true);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      ComponentBoxService.row(
                          (Consumer<LayoutContainerNode>)
                              layoutContainerNode2 -> {
                                layoutContainerNode2
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)))
                                    .align(ScenePctService.Align.CENTER)
                                    .gap(Float.intBitsToFloat(1082130432))
                                    .padding(
                                        0.0f,
                                        this.ftzcjcss2ew
                                            ? Float.intBitsToFloat(1082130432)
                                            : Float.intBitsToFloat(1098907648));
                              },
                          (ComponentKeyService<?>[])
                              new ComponentKeyService[] {
                                ComponentBoxService.node(
                                    "material-tabs",
                                    AnimatedSelectionIndicator::new,
                                    animatedSelectionIndicator -> {
                                      animatedSelectionIndicator
                                          .selection(
                                              this.text4.isBlank()
                                                  ? Arrays.asList(fdivljsc8kbu)
                                                      .indexOf(this.moduleFeatureType2)
                                                  : -1)
                                          .id("clickgui.categories")
                                          .width(
                                              LayoutOperationHandler.percent(
                                                  Float.intBitsToFloat(1120403456)))
                                          .height(
                                              LayoutOperationHandler.px(
                                                  Float.intBitsToFloat(1111490560)))
                                          .flex(1.0f)
                                          .minWidth(0.0f)
                                          .direction(ScenePctService.Direction.ROW)
                                          .scrollable(true)
                                          .scrollbarWidth(0.0f)
                                          .clip(true);
                                    },
                                    (ComponentKeyService[])
                                        arrayList.toArray(
                                            i3 -> {
                                              return new ComponentKeyService[i3];
                                            })),
                                createComponentKeyService28(
                                        "clickgui.client-modules",
                                        "tune",
                                        "Client modules",
                                        () -> {
                                          updateState6(ModuleFeatureType.CLIENT);
                                        })
                                    .props(
                                        materialJoinedService -> {
                                          materialJoinedService.colors(
                                              (this.moduleFeatureType2 == ModuleFeatureType.CLIENT
                                                      || this.moduleFeatureType2
                                                          == ModuleFeatureType.DEVELOPMENT)
                                                  ? MaterialIsLightService.SECONDARY_CONTAINER
                                                  : 0,
                                              MaterialIsLightService.ON_SURFACE_VARIANT);
                                        }),
                                createComponentKeyService28(
                                    "clickgui.client-settings",
                                    "settings",
                                    "Client settings\nColors, presets and appearance",
                                    () -> {
                                      if (this.screenScreenIdService != null) {
                                        this.screenScreenIdService.open("client-settings");
                                      }
                                    })
                              }),
                      MaterialTextService.divider()
                    })
            .key("tabs");
      }
      ModuleFeatureType moduleFeatureType = moduleFeatureTypeArr[i2];
      boolean z = this.text4.isBlank() && moduleFeatureType == this.moduleFeatureType2;
      Supplier supplier = MaterialJoinedService::new;
      Consumer<MaterialJoinedService> consumer =
          materialJoinedService2 -> {
            materialJoinedService2
                .colors(
                    0,
                    z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT)
                .shape(0.0f, Float.intBitsToFloat(1111490560));
            materialJoinedService2
                .id("clickgui.category." + createText(moduleFeatureType.name()) + ".tab")
                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                .width(LayoutOperationHandler.auto())
                .minWidth(Float.intBitsToFloat(1120403456))
                .padding(0.0f, Float.intBitsToFloat(1101004800))
                .flexShrink(0.0f)
                .direction(ScenePctService.Direction.ROW)
                .align(ScenePctService.Align.CENTER)
                .justify(ScenePctService.Justify.CENTER)
                .onClick(
                    () -> {
                      updateState6(moduleFeatureType);
                    });
          };
      ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[1];
      componentKeyServiceArr[0] =
          MaterialTextService.label(
                  createText4(moduleFeatureType),
                  z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService -> {
                    sceneTextService
                        .fontSize(Float.intBitsToFloat(1097859072))
                        .lineHeight(Float.intBitsToFloat(1102053376))
                        .letterSpacing(Float.intBitsToFloat(1036831949))
                        .flexShrink(0.0f);
                  });
      arrayList.add(
          ComponentBoxService.node("material-tab", supplier, consumer, componentKeyServiceArr)
              .key(moduleFeatureType.name()));
      i = i2 + 1;
    }
  }

  private List<Module> collectValues2() {
    return (this.text4.isBlank()
            ? this.fikxlao3mld.get(this.moduleFeatureType2).stream()
            : this.fikxlao3mld.values().stream()
                .flatMap(
                    (v0) -> {
                      return v0.stream();
                    }))
        .filter(
            module -> {
              return module.category() != ModuleFeatureType.DEVELOPMENT
                  || this.moduleFeatureType2 == ModuleFeatureType.DEVELOPMENT;
            })
        .filter(
            module2 -> {
              return !this.enabled3 || module2.isEnabled();
            })
        .filter(
            module3 -> {
              return this.text4.isBlank()
                  || (module3.name()
                          + " "
                          + module3.description()
                          + " "
                          + createText4(module3.category()))
                      .toLowerCase(Locale.ROOT)
                      .contains(this.text4);
            })
        .sorted(
            Comparator.comparing(
                (v0) -> {
                  return v0.name();
                },
                String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  private ComponentKeyService<?> mg3ujj02y0vv() {
    ComponentKeyService<?> componentKeyServiceProps;
    boolean z = this.enabled4 && this.value5 < Float.intBitsToFloat(1135869952);
    List<Module> listM4k38isfutk5 = collectValues2();
    ArrayList arrayList = new ArrayList();
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= listM4k38isfutk5.size()) {
        break;
      }
      arrayList.add(
          createComponentKeyService9(
              listM4k38isfutk5.get(i2), i2 == 0, i2 == listM4k38isfutk5.size() - 1));
      i = i2 + 1;
    }
    if (arrayList.isEmpty()) {
      Consumer<LayoutContainerNode> consumer =
          layoutContainerNode -> {
            layoutContainerNode
                .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                .padding(Float.intBitsToFloat(1103101952))
                .gap(Float.intBitsToFloat(1090519040));
          };
      ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
      componentKeyServiceArr[0] =
          MaterialTextService.text(
              "No modules found",
              Float.intBitsToFloat(1099956224),
              MaterialIsLightService.ON_SURFACE);
      componentKeyServiceArr[1] =
          MaterialTextService.text(
                  this.enabled3 ? "Try showing all modules." : "Try a different search.",
                  Float.intBitsToFloat(1096810496),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService -> {
                    sceneTextService.wordWrap(true);
                  });
      componentKeyServiceArr[2] =
          createComponentKeyService26(
              "clickgui.clear-filters",
              "Clear filters",
              () -> {
                this.text4 = "";
                this.enabled3 = false;
                updateState7();
                mgf9hldmchqz();
              },
              false);
      arrayList.add(
          ComponentBoxService.column(
              (Consumer<LayoutContainerNode>) consumer,
              (ComponentKeyService<?>[]) componentKeyServiceArr));
    }
    Consumer<LayoutContainerNode> consumer2 =
        layoutContainerNode2 -> {
          LayoutOperationHandler layoutOperationHandlerPx;
          LayoutContainerNode layoutContainerNodeId = layoutContainerNode2.id("clickgui.modules");
          if (this.enabled4) {
            layoutOperationHandlerPx =
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456));
          } else {
            layoutOperationHandlerPx =
                LayoutOperationHandler.px(
                    this.value6 < Float.intBitsToFloat(1149861888)
                        ? Float.intBitsToFloat(1133117440)
                        : Float.intBitsToFloat(1134297088));
          }
          layoutContainerNodeId
              .width(layoutOperationHandlerPx)
              .flexShrink(0.0f)
              .height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .gap(z ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1094713344));
        };
    ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
    Consumer<LayoutContainerNode> consumer3 =
        layoutContainerNode3 -> {
          layoutContainerNode3
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .height(
                  LayoutOperationHandler.px(
                      z ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1110441984)))
              .padding(0.0f, Float.intBitsToFloat(1094713344))
              .gap(Float.intBitsToFloat(1090519040))
              .align(ScenePctService.Align.CENTER)
              .flexShrink(0.0f);
        };
    ComponentKeyService[] componentKeyServiceArr3 = new ComponentKeyService[2];
    componentKeyServiceArr3[0] =
        MaterialTextService.label(
                this.text4.isBlank() ? createText4(this.moduleFeatureType2) : "Search results",
                MaterialIsLightService.ON_SURFACE)
            .props(
                sceneTextService2 -> {
                  sceneTextService2.flex(1.0f).minWidth(0.0f);
                });
    if (this.moduleFeatureType2 == ModuleFeatureType.CLIENT) {
      componentKeyServiceProps =
          createComponentKeyService26(
                  "clickgui.developer-tools",
                  "Developer tools",
                  () -> {
                    updateState6(ModuleFeatureType.DEVELOPMENT);
                  },
                  false)
              .props(
                  materialJoinedService -> {
                    materialJoinedService.shape(
                        Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107296256));
                    materialJoinedService
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))
                        .padding(0.0f, Float.intBitsToFloat(1092616192))
                        .tooltip("Setting galleries and rendering experiments");
                  });
    } else {
      componentKeyServiceProps =
          this.moduleFeatureType2 == ModuleFeatureType.DEVELOPMENT
              ? createComponentKeyService26(
                      "clickgui.developer-back",
                      "Back",
                      () -> {
                        updateState6(ModuleFeatureType.CLIENT);
                      },
                      false)
                  .props(
                      materialJoinedService2 -> {
                        materialJoinedService2.shape(
                            Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107296256));
                        materialJoinedService2
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))
                            .padding(0.0f, Float.intBitsToFloat(1092616192))
                            .tooltip("Back to client settings");
                      })
              : MaterialTextService.text(
                  Integer.toString(listM4k38isfutk5.size()),
                  Float.intBitsToFloat(1096810496),
                  MaterialIsLightService.ON_SURFACE_VARIANT);
    }
    componentKeyServiceArr3[1] = componentKeyServiceProps;
    componentKeyServiceArr2[0] =
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>) consumer3,
            (ComponentKeyService<?>[]) componentKeyServiceArr3);
    componentKeyServiceArr2[1] =
        ComponentBoxService.node(
            "material-reflow-list",
            MaterialComponent::new,
            materialComponent -> {
              materialComponent
                  .id("clickgui.module-list")
                  .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .flex(1.0f)
                  .minHeight(0.0f)
                  .padding(
                      0.0f,
                      Float.intBitsToFloat(1096810496),
                      Float.intBitsToFloat(1082130432),
                      0.0f)
                  .gap(Float.intBitsToFloat(1090519040))
                  .scrollable(true)
                  .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                  .scrollbarWidth(Float.intBitsToFloat(1077936128))
                  .scrollbarTrackColor(338569528)
                  .scrollInset(Float.intBitsToFloat(1090519040))
                  .clip(true);
            },
            (ComponentKeyService[])
                arrayList.toArray(
                    i3 -> {
                      return new ComponentKeyService[i3];
                    }));
    return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>) consumer2,
            (ComponentKeyService<?>[]) componentKeyServiceArr2)
        .key("module-list");
  }

  private ComponentKeyService<?> createComponentKeyService9(Module module, boolean z, boolean z2) {
    String strM7sclicozxcc = m7sclicozxcc(module);
    boolean z3 = module == this.moduleOperationHandler2 && (!this.enabled4 || this.enabled6);
    int iLayer =
        z3
            ? MaterialIsLightService.layer(
                MaterialIsLightService.SURFACE_CONTAINER,
                MaterialIsLightService.PRIMARY,
                Float.intBitsToFloat(1040522936))
            : MaterialIsLightService.SURFACE_LOW;
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        MaterialTextService.text(
                module.name(), Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE)
            .props(
                sceneTextService -> {
                  sceneTextService
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .lineHeight(Float.intBitsToFloat(1102053376));
                })
            .key("name"));
    arrayList.add(
        MaterialTextService.text(
                this.text4.isBlank() ? module.description() : createText4(module.category()),
                Float.intBitsToFloat(1095761920),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService2 -> {
                  sceneTextService2.width(
                      LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                })
            .key("description"));
    return ComponentBoxService.node(
            "material-module-row",
            MaterialResponsiveService::new,
            materialResponsiveService -> {
              materialResponsiveService
                  .surface(iLayer)
                  .responsive(true)
                  .selected(z3)
                  .id(strM7sclicozxcc)
                  .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .minHeight(Float.intBitsToFloat(1117782016))
                  .flexShrink(0.0f)
                  .cornerRadius(Float.intBitsToFloat(1099956224))
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .padding(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1094713344))
                  .gap(Float.intBitsToFloat(1092616192))
                  .onRightClick(
                      () -> {
                        if (module instanceof ModuleOperationHandler) {
                          ((ModuleOperationHandler) module).openPanel();
                        } else {
                          updateState5(module);
                        }
                      });
            },
            ComponentBoxService.node(
                    "module-select",
                    MaterialJoinedService::new,
                    materialJoinedService -> {
                      materialJoinedService
                          .colors(0, MaterialIsLightService.ON_SURFACE)
                          .shape(Float.intBitsToFloat(1094713344), 0.0f);
                      materialJoinedService
                          .id(strM7sclicozxcc + ".open-settings")
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1114636288)))
                          .minWidth(0.0f)
                          .flex(1.0f)
                          .padding(0.0f, Float.intBitsToFloat(1082130432))
                          .direction(ScenePctService.Direction.COLUMN)
                          .gap(Float.intBitsToFloat(1084227584))
                          .justify(ScenePctService.Justify.CENTER)
                          .tooltip(module.description())
                          .onClick(
                              () -> {
                                updateState5(module);
                              });
                    },
                    (ComponentKeyService[])
                        arrayList.toArray(
                            i -> {
                              return new ComponentKeyService[i];
                            }))
                .key("select"),
            createComponentKeyService12(module, strM7sclicozxcc + ".toggle"))
        .key(strM7sclicozxcc);
  }

  private ComponentKeyService<?> createComponentKeyService10() {
    if (this.moduleOperationHandler2 == null) {
      return ComponentBoxService.panel(
              sceneCornerRadiusService -> {
                sceneCornerRadiusService
                    .flex(1.0f)
                    .minWidth(0.0f)
                    .height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                    .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                    .cornerRadius(Float.intBitsToFloat(1105199104))
                    .padding(Float.intBitsToFloat(1107296256))
                    .direction(ScenePctService.Direction.COLUMN)
                    .justify(ScenePctService.Justify.CENTER)
                    .gap(Float.intBitsToFloat(1094713344));
              },
              MaterialTextService.text(
                      "Your modules, your way",
                      Float.intBitsToFloat(1103101952),
                      MaterialIsLightService.ON_SURFACE)
                  .props(
                      sceneTextService -> {
                        sceneTextService.wordWrap(true);
                      }),
              MaterialTextService.text(
                      "Choose a module to adjust its settings.",
                      Float.intBitsToFloat(1098907648),
                      MaterialIsLightService.ON_SURFACE_VARIANT)
                  .props(
                      sceneTextService2 -> {
                        sceneTextService2.wordWrap(true);
                      }))
          .key("detail-empty");
    }
    Module module = this.moduleOperationHandler2;
    if (this.text5.isBlank()
        && !this.text6.isEmpty()
        && module.settings().stream()
            .noneMatch(
                moduleSetting -> {
                  return checkCondition(
                      moduleSetting,
                      (String)
                          moduleSetting
                              .category()
                              .map(
                                  (v0) -> {
                                    return v0.qualifiedName();
                                  })
                              .orElse("general"));
                })) {
      this.text6 = "";
    }
    ArrayList arrayList = new ArrayList();
    if (this.text5.isBlank() && !module.presets().isEmpty()) {
      arrayList.add(createComponentKeyService11(module));
    }
    if (module instanceof ModuleOperationHandler) {
      ModuleOperationHandler moduleOperationHandler = (ModuleOperationHandler) module;
      String str = "Open " + module.name();
      Objects.requireNonNull(moduleOperationHandler);
      arrayList.add(
          createComponentKeyService26(
              "clickgui.module.open-panel", str, moduleOperationHandler::openPanel, true));
    }
    updateState3(
        arrayList,
        "General",
        "",
        "general",
        module.settings().stream()
            .filter(
                moduleSetting2 -> {
                  return moduleSetting2.category().isEmpty();
                })
            .toList());
    Iterator<ModuleNameService> it = module.settingCategories().iterator();
    while (it.hasNext()) {
      updateState2(arrayList, it.next());
    }
    if (arrayList.isEmpty() && this.text5.isBlank()) {
      arrayList.add(
          MaterialTextService.text(
                  "This module has no additional settings.",
                  Float.intBitsToFloat(1095761920),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService3 -> {
                    sceneTextService3.padding(Float.intBitsToFloat(1098907648)).wordWrap(true);
                  }));
    }
    ComponentKeyService<?> objKey =
        ComponentBoxService.row(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .id("clickgui.detail.header")
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .height(
                              LayoutOperationHandler.px(
                                  this.value5 < Float.intBitsToFloat(1143603200)
                                      ? Float.intBitsToFloat(1116733440)
                                      : Float.intBitsToFloat(1117782016)))
                          .padding(
                              Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800))
                          .gap(Float.intBitsToFloat(1096810496))
                          .align(ScenePctService.Align.CENTER)
                          .flexShrink(0.0f)
                          .tooltip(module.description());
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      ComponentBoxService.node(
                              "material-emblem",
                              MaterialLabelService::new,
                              materialLabelService -> {
                                materialLabelService
                                    .shape("cookie9")
                                    .active(module.isEnabled())
                                    .label(
                                        module
                                            .name()
                                            .substring(0, module.name().offsetByCodePoints(0, 1))
                                            .toUpperCase(Locale.ROOT))
                                    .tint(
                                        module.isEnabled()
                                            ? MaterialIsLightService.PRIMARY
                                            : MaterialIsLightService.SECONDARY)
                                    .size(
                                        Float.intBitsToFloat(1109393408),
                                        Float.intBitsToFloat(1109393408))
                                    .flexShrink(0.0f);
                              },
                              new ComponentKeyService[0])
                          .key("emblem"),
                      MaterialTextService.text(
                              module.name(),
                              Float.intBitsToFloat(1103101952),
                              MaterialIsLightService.ON_SURFACE)
                          .props(
                              sceneTextService4 -> {
                                sceneTextService4
                                    .id("clickgui.detail.title")
                                    .flex(1.0f)
                                    .minWidth(0.0f);
                              }),
                      createComponentKeyService12(module, "clickgui.detail.toggle")
                    })
            .key("detail-header");
    if (arrayList.isEmpty() && !this.text5.isBlank()) {
      arrayList.add(
          MaterialTextService.text(
                  "No matching settings",
                  Float.intBitsToFloat(1095761920),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService5 -> {
                    sceneTextService5.padding(Float.intBitsToFloat(1094713344));
                  }));
    }
    ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
    if (!this.enabled4) {
      arrayList2.add(objKey);
    }
    arrayList2.add(mfstwwxrgcrh());
    if (!module.settingCategories().isEmpty()) {
      arrayList2.add(createComponentKeyService14());
    }
    arrayList2.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode2 -> {
                      layoutContainerNode2
                          .id("clickgui.settings")
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .flex(1.0f)
                          .minHeight(0.0f)
                          .padding(
                              Float.intBitsToFloat(1094713344),
                              Float.intBitsToFloat(1101004800),
                              Float.intBitsToFloat(1101004800),
                              Float.intBitsToFloat(1098907648))
                          .gap(Float.intBitsToFloat(1101004800))
                          .scrollable(true)
                          .scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT)
                          .scrollbarWidth(Float.intBitsToFloat(1077936128))
                          .clip(true);
                    },
                (ComponentKeyService<?>[])
                    arrayList.toArray(
                        i -> {
                          return new ComponentKeyService[i];
                        }))
            .key("settings-scroll"));
    return ComponentBoxService.panel(
            sceneCornerRadiusService2 -> {
              sceneCornerRadiusService2
                  .id("clickgui.detail")
                  .width(
                      this.enabled4
                          ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))
                          : LayoutOperationHandler.auto())
                  .flex(1.0f)
                  .minWidth(0.0f)
                  .height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .backgroundColor(MaterialIsLightService.SURFACE_LOW)
                  .cornerRadius(
                      this.ftzcjcss2ew
                          ? Float.intBitsToFloat(1101004800)
                          : Float.intBitsToFloat(1105199104))
                  .direction(ScenePctService.Direction.COLUMN)
                  .clip(true);
            },
            (ComponentKeyService[])
                arrayList2.toArray(
                    i2 -> {
                      return new ComponentKeyService[i2];
                    }))
        .key("detail:" + m7sclicozxcc(module))
        .onMount(
            sceneCornerRadiusService3 -> {
              MaterialEnterService.enter(
                  sceneCornerRadiusService3,
                  this.enabled4
                      ? Float.intBitsToFloat(1103101952)
                      : Float.intBitsToFloat(1098907648),
                  0.0f);
            });
  }

  private ComponentKeyService<?> createComponentKeyService11(Module module) {
    List<ModuleIdService> listPresets = module.presets();
    String orDefault =
        this.text8.getOrDefault(module.name(), ((ModuleIdService) listPresets.getFirst()).id());
    return PresetsComponent.render(
        module,
        orDefault,
        this.fcijmdt1zfva.contains(module.name()),
        this.ftzcjcss2ew || (!this.enabled4 && this.value6 < Float.intBitsToFloat(1148846080)),
        str -> {
          this.text8.put(module.name(), str);
          this.fcijmdt1zfva.remove(module.name());
          mgf9hldmchqz();
        },
        () -> {
          if (!this.fcijmdt1zfva.remove(module.name())) {
            this.fcijmdt1zfva.add(module.name());
          }
          mgf9hldmchqz();
        },
        () -> {
          ModuleIdService moduleIdService =
              (ModuleIdService)
                  listPresets.stream()
                      .filter(
                          moduleIdService2 -> {
                            return moduleIdService2.id().equals(orDefault);
                          })
                      .findFirst()
                      .orElse((ModuleIdService) listPresets.getFirst());
          updateState27(this.componentMountService);
          moduleIdService.apply(module);
          mfahyvg0apdz();
          mgf9hldmchqz();
        });
  }

  private ComponentKeyService<?> createComponentKeyService12(Module module, String str) {
    return ComponentBoxService.node(
            "module-switch",
            ControlCompactService::new,
            controlCompactService -> {
              controlCompactService
                  .material(true)
                  .compact(false)
                  .id(str)
                  .size(Float.intBitsToFloat(1113587712), Float.intBitsToFloat(1109393408))
                  .flexShrink(0.0f)
                  .stopPropagation(true)
                  .cursorStyle(ScenePctService.CursorStyle.POINTER)
                  .tooltip((module.isEnabled() ? "Disable " : "Enable ") + module.name());
              if (controlCompactService.value() != module.isEnabled()) {
                float f = motionFiniteService.get(controlCompactService);
                controlCompactService.value(module.isEnabled());
                motionFiniteService.set(controlCompactService, f);
                MotionAnimateService.animate(
                    controlCompactService,
                    motionFiniteService,
                    module.isEnabled() ? 1.0f : 0.0f,
                    MaterialIsLightService.FAST_SPATIAL);
              }
              controlCompactService.onToggle(
                  bool -> {
                    if (bool.booleanValue() != module.isEnabled()) {
                      module.toggle();
                    }
                    updateState7();
                    mgf9hldmchqz();
                  });
            },
            new ComponentKeyService[0])
        .key(str);
  }

  private ComponentKeyService<?> mfstwwxrgcrh() {
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .id("clickgui.settings-toolbar")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1098907648))
                      .gap(Float.intBitsToFloat(1094713344))
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  MaterialTextService.search(
                          "clickgui.settings-search",
                          "Find settings…",
                          this.text5,
                          str -> {
                            this.text5 = str.strip().toLowerCase(Locale.ROOT);
                            mgf9hldmchqz();
                            updateState();
                          })
                      .props(
                          interactiveSurfacePanel -> {
                            interactiveSurfacePanel
                                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                .flex(1.0f);
                          })
                      .children(
                          MaterialTextService.icon(
                                  "search", MaterialIsLightService.ON_SURFACE_VARIANT)
                              .props(
                                  sceneSrcService -> {
                                    sceneSrcService
                                        .size(
                                            Float.intBitsToFloat(1099956224),
                                            Float.intBitsToFloat(1099956224))
                                        .margin(0.0f, Float.intBitsToFloat(1082130432));
                                  }),
                          ComponentBoxService.node(
                                  "settings-search",
                                  ControlLetterSpacingService::new,
                                  controlLetterSpacingService -> {
                                    controlLetterSpacingService
                                        .id("clickgui.settings-search")
                                        .materialSearch(true)
                                        .fontFamily(MaterialIsLightService.FONT)
                                        .fontSize(Float.intBitsToFloat(1096810496))
                                        .letterSpacing(Float.intBitsToFloat(1036831949))
                                        .placeholder("Find settings…")
                                        .bgColor(0)
                                        .textColor(MaterialIsLightService.ON_SURFACE)
                                        .placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT)
                                        .focusBorder(0)
                                        .selectionColor(MaterialIsLightService.SELECTION)
                                        .cornerRadius(0.0f)
                                        .height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1109393408)))
                                        .flex(1.0f)
                                        .minWidth(0.0f)
                                        .onChanged(
                                            str2 -> {
                                              this.text5 = str2.strip().toLowerCase(Locale.ROOT);
                                              mgf9hldmchqz();
                                              updateState();
                                            });
                                    if (controlLetterSpacingService.focused()) {
                                      return;
                                    }
                                    controlLetterSpacingService.text(this.text5);
                                  },
                                  new ComponentKeyService[0])
                              .key("input"),
                          createComponentKeyService28(
                                  "clickgui.settings-search.clear",
                                  "close",
                                  "Clear settings search",
                                  () -> {
                                    this.text5 = "";
                                    ControlLetterSpacingService controlLetterSpacingService2 =
                                        (ControlLetterSpacingService)
                                            this.materialTerrainTooltipsService.findById(
                                                "clickgui.settings-search");
                                    if (controlLetterSpacingService2 != null) {
                                      controlLetterSpacingService2.text("");
                                    }
                                    mgf9hldmchqz();
                                    updateState();
                                  })
                              .props(
                                  materialJoinedService -> {
                                    materialJoinedService.visible(!this.text5.isEmpty());
                                  })
                              .key("clear")),
                  createComponentKeyService26(
                          "clickgui.settings-help",
                          "Help",
                          () -> {
                            this.enabled2 = !this.enabled2;
                            mgf9hldmchqz();
                          },
                          this.enabled2)
                      .props(
                          materialJoinedService2 -> {
                            materialJoinedService2.tooltip("Show setting descriptions");
                          })
                })
        .key("settings-toolbar");
  }

  private ComponentKeyService<?> createComponentKeyService14() {
    ArrayList arrayList = new ArrayList();
    arrayList.add(createComponentKeyService15("", "All"));
    if (this.moduleOperationHandler2.settings().stream()
        .anyMatch(
            moduleSetting -> {
              return moduleSetting.category().isEmpty() && moduleSetting.isVisible();
            })) {
      arrayList.add(createComponentKeyService15("general", "General"));
    }
    for (ModuleNameService moduleNameService : this.moduleOperationHandler2.settingCategories()) {
      if (this.moduleOperationHandler2.settings().stream()
          .anyMatch(
              moduleSetting2 -> {
                return moduleSetting2.isVisible()
                    && ((Boolean)
                            moduleSetting2
                                .category()
                                .map(
                                    moduleNameService2 -> {
                                      return Boolean.valueOf(
                                          ((String) moduleNameService2.path().getFirst())
                                              .equals(moduleNameService.name()));
                                    })
                                .orElse(false))
                        .booleanValue();
              })) {
        arrayList.add(
            createComponentKeyService15(moduleNameService.name(), moduleNameService.name()));
      }
    }
    String str = this.text5.isBlank() ? this.text6 : "";
    return ComponentBoxService.node(
            "material-choice-rail",
            MaterialSelectionSelector::new,
            materialSelectionSelector -> {
              materialSelectionSelector
                  .selection("clickgui.settings-group." + (str.isEmpty() ? "all" : createText(str)))
                  .id("clickgui.settings-groups")
                  .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1115684864)))
                  .padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1098907648))
                  .gap(Float.intBitsToFloat(1090519040))
                  .align(ScenePctService.Align.CENTER)
                  .scrollable(true)
                  .scrollbarWidth(0.0f)
                  .clip(true)
                  .flexShrink(0.0f);
            },
            (ComponentKeyService[])
                arrayList.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }))
        .key("settings-groups");
  }

  private ComponentKeyService<?> createComponentKeyService15(String str, String str2) {
    return createComponentKeyService27(
            "clickgui.settings-group." + (str.isEmpty() ? "all" : createText(str)),
            str2,
            () -> {
              if (this.text6.equals(str) && this.text5.isBlank()) {
                return;
              }
              String str3 = this.text5.isBlank() ? this.text6 : "";
              ScenePctService<?> scenePctServiceFindById =
                  this.materialTerrainTooltipsService.findById(
                      "clickgui.settings-group." + (str3.isEmpty() ? "all" : createText(str3)));
              ScenePctService<?> scenePctServiceFindById2 =
                  this.materialTerrainTooltipsService.findById(
                      "clickgui.settings-group." + (str.isEmpty() ? "all" : createText(str)));
              float fIntBitsToFloat =
                  (scenePctServiceFindById == null
                          || scenePctServiceFindById2 == null
                          || scenePctServiceFindById2.computedX()
                              >= scenePctServiceFindById.computedX())
                      ? Float.intBitsToFloat(1092616192)
                      : Float.intBitsToFloat(-1054867456);
              this.text6 = str;
              this.text5 = "";
              mfahyvg0apdz();
              updateState27(this.componentMountService);
              mgf9hldmchqz();
              updateState();
              MaterialEnterService.enter(
                  this.materialTerrainTooltipsService.findById("clickgui.settings"),
                  fIntBitsToFloat,
                  0.0f);
            },
            0,
            (this.text5.isBlank() ? this.text6 : "").equals(str)
                ? MaterialIsLightService.ON_SECONDARY_CONTAINER
                : MaterialIsLightService.ON_SURFACE_VARIANT,
            Float.intBitsToFloat(1098907648))
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                  .padding(0.0f, Float.intBitsToFloat(1099956224));
            })
        .key(str.isEmpty() ? "all" : str);
  }

  private void updateState() {
    ScenePctService<?> scenePctServiceFindById =
        this.materialTerrainTooltipsService.findById("clickgui.settings");
    if (scenePctServiceFindById != null) {
      MotionAnimateService.cancel(scenePctServiceFindById, MotionColorsContainer.Floats.SCROLL_Y);
      scenePctServiceFindById.scrollTarget(0.0f).scrollY(0.0f);
    }
  }

  private boolean checkCondition(ModuleSetting<?> moduleSetting, String str) {
    if (!moduleSetting.isVisible()) {
      return false;
    }
    if (this.text5.isBlank()) {
      return this.text6.isEmpty() || str.equals(this.text6) || str.startsWith(this.text6 + "/");
    }
    return (moduleSetting.name() + " " + moduleSetting.description() + " " + str)
        .toLowerCase(Locale.ROOT)
        .contains(this.text5);
  }

  private void updateState2(
      List<ComponentKeyService<?>> list, ModuleNameService moduleNameService) {
    updateState3(
        list,
        String.join(" / ", moduleNameService.path()),
        moduleNameService.description(),
        moduleNameService.qualifiedName(),
        moduleNameService.settings());
    Iterator<ModuleNameService> it = moduleNameService.children().iterator();
    while (it.hasNext()) {
      updateState2(list, it.next());
    }
  }

  private void updateState3(
      List<ComponentKeyService<?>> list,
      String str,
      String str2,
      String str3,
      List<ModuleSetting<?>> list2) {
    List<ModuleSetting<?>> list3 =
        list2.stream()
            .filter(
                moduleSetting -> {
                  return checkCondition(moduleSetting, str3);
                })
            .toList();
    if (list3.isEmpty()) {
      return;
    }
    boolean z =
        this.text5.isBlank()
            && this.text7.contains(m1vcviafemyn(this.moduleOperationHandler2) + ":" + str3);
    ArrayList arrayList = new ArrayList();
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= list3.size()) {
        list.add(
            ComponentBoxService.column(
                    (Consumer<LayoutContainerNode>)
                        layoutContainerNode -> {
                          layoutContainerNode
                              .id("clickgui.section." + createText(str3))
                              .width(
                                  LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                              .gap(Float.intBitsToFloat(1090519040))
                              .flexShrink(0.0f);
                        },
                    (ComponentKeyService<?>[])
                        new ComponentKeyService[] {
                          ComponentBoxService.node(
                                  "settings-section-heading",
                                  MaterialJoinedService::new,
                                  materialJoinedService -> {
                                    materialJoinedService
                                        .colors(0, MaterialIsLightService.PRIMARY)
                                        .shape(
                                            Float.intBitsToFloat(1096810496),
                                            Float.intBitsToFloat(1111490560))
                                        .id("clickgui.section." + createText(str3) + ".heading")
                                        .height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1111490560)))
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)))
                                        .padding(0.0f, Float.intBitsToFloat(1096810496))
                                        .gap(Float.intBitsToFloat(1092616192))
                                        .direction(ScenePctService.Direction.ROW)
                                        .align(ScenePctService.Align.CENTER)
                                        .tooltip(str2)
                                        .onClick(
                                            () -> {
                                              String str4 =
                                                  m1vcviafemyn(this.moduleOperationHandler2)
                                                      + ":"
                                                      + str3;
                                              if (!this.text7.remove(str4)) {
                                                this.text7.add(str4);
                                              }
                                              mfahyvg0apdz();
                                              updateState27(this.componentMountService);
                                              mgf9hldmchqz();
                                            });
                                  },
                                  ComponentBoxService.node(
                                          "material-chevron",
                                          MaterialExpandedService::new,
                                          materialExpandedService -> {
                                            materialExpandedService
                                                .expanded(!z)
                                                .tintColor(MaterialIsLightService.PRIMARY)
                                                .size(
                                                    Float.intBitsToFloat(1098907648),
                                                    Float.intBitsToFloat(1098907648))
                                                .flexShrink(0.0f);
                                          },
                                          new ComponentKeyService[0])
                                      .key("chevron"),
                                  MaterialTextService.label(str, MaterialIsLightService.PRIMARY)
                                      .props(
                                          sceneTextService -> {
                                            sceneTextService.flex(1.0f).minWidth(0.0f);
                                          }),
                                  createComponentKeyService25(
                                      Integer.toString(list3.size()),
                                      MaterialIsLightService.ON_SURFACE_VARIANT))
                              .key("heading"),
                          ComponentBoxService.node(
                                  "material-disclosure",
                                  ExpandableMaterialPanel::new,
                                  expandableMaterialPanel -> {
                                    expandableMaterialPanel
                                        .expanded(!z)
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)));
                                  },
                                  ComponentBoxService.column(
                                          (Consumer<LayoutContainerNode>)
                                              layoutContainerNode2 -> {
                                                layoutContainerNode2
                                                    .width(
                                                        LayoutOperationHandler.percent(
                                                            Float.intBitsToFloat(1120403456)))
                                                    .flexShrink(0.0f)
                                                    .gap(Float.intBitsToFloat(1086324736));
                                              },
                                          (ComponentKeyService<?>[])
                                              arrayList.toArray(
                                                  i3 -> {
                                                    return new ComponentKeyService[i3];
                                                  }))
                                      .key("content"))
                              .key("rows")
                        })
                .key("section:" + str3));
        return;
      } else {
        arrayList.add(
            createComponentKeyService16(list3.get(i2), str3, i2 == 0, i2 == list3.size() - 1));
        i = i2 + 1;
      }
    }
  }

  private ComponentKeyService<?> createComponentKeyService16(
      ModuleSetting<?> moduleSetting, String str, boolean z, boolean z2) throws MatchException {
    String strValueOf;
    ArrayList arrayList;
    int iMdnv1ub7rbpv;
    String str2;
    int iMdnv1ub7rbpv2;
    ComponentKeyService<?> componentKeyServiceKey;
    ModuleSetting.MultiSelect multiSelect;
    ModuleSetting.Mode mode;
    String str3 = "clickgui.setting." + createText(str) + "." + createText(moduleSetting.name());
    boolean zIsActive = moduleSetting.isActive();
    boolean z3 =
        this.enabled4
            && this.value6 < Float.intBitsToFloat(1142947840)
            && !(moduleSetting instanceof ModuleSetting.Bool);
    ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
    arrayList2.add(
        MaterialTextService.text(
                moduleSetting.displayName(),
                Float.intBitsToFloat(1097859072),
                zIsActive ? MaterialIsLightService.ON_SURFACE : calculateValue())
            .props(
                sceneTextService -> {
                  sceneTextService
                      .id(str3 + ".label")
                      .minWidth(0.0f)
                      .width(
                          z3
                              ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))
                              : LayoutOperationHandler.percent(Float.intBitsToFloat(1106247680)))
                      .flexShrink(0.0f)
                      .lineHeight(Float.intBitsToFloat(1102053376))
                      .letterSpacing(Float.intBitsToFloat(1036831949))
                      .wordWrap(true)
                      .maxLines(2);
                })
            .key("label"));
    if (moduleSetting instanceof ModuleSetting.Bool) {
      ModuleSetting.Bool bool = (ModuleSetting.Bool) moduleSetting;
      arrayList2.set(
          0,
          arrayList2
              .getFirst()
              .props(
                  scenePctService -> {
                    scenePctService.width(LayoutOperationHandler.auto()).flex(1.0f);
                  }));
      componentKeyServiceKey =
          ComponentBoxService.node(
                  "setting-switch",
                  () -> {
                    return new ControlSettingService(bool);
                  },
                  controlSettingService -> {
                    controlSettingService
                        .material(true)
                        .compact(false)
                        .size(Float.intBitsToFloat(1113587712), Float.intBitsToFloat(1109393408))
                        .flexShrink(0.0f)
                        .id(str3 + ".toggle");
                    controlSettingService
                        .disabledOpacity(1.0f)
                        .onToggle(
                            bool2 -> {
                              mgf9hldmchqz();
                            });
                    controlSettingService.refreshState();
                  },
                  new ComponentKeyService[0])
              .key("control");
    } else if (moduleSetting instanceof ModuleSetting.Number) {
      ModuleSetting.Number number = (ModuleSetting.Number) moduleSetting;
      if (number.usesExactInput()) {
        componentKeyServiceKey =
            createComponentKeyService18(
                str3,
                createText6(number.get().floatValue(), number.step(), number.min()),
                moduleSetting);
      } else {
        componentKeyServiceKey =
            checkCondition2(number)
                ? createComponentKeyService17(str3, number)
                : ComponentBoxService.row(
                        (Consumer<LayoutContainerNode>)
                            layoutContainerNode -> {
                              layoutContainerNode
                                  .gap(Float.intBitsToFloat(1090519040))
                                  .align(ScenePctService.Align.CENTER)
                                  .minWidth(0.0f);
                            },
                        (ComponentKeyService<?>[])
                            new ComponentKeyService[] {
                              ComponentBoxService.node(
                                      "setting-slider",
                                      () -> {
                                        return new NumberSettingControl(number);
                                      },
                                      numberSettingControl -> {
                                        numberSettingControl
                                            .material(true)
                                            .compact(false)
                                            .height(
                                                LayoutOperationHandler.px(
                                                    Float.intBitsToFloat(1109393408)))
                                            .flex(1.0f)
                                            .minWidth(Float.intBitsToFloat(1111490560))
                                            .id(str3 + ".slider");
                                        numberSettingControl
                                            .onPreview(
                                                f -> {
                                                  updateState4(
                                                      str3,
                                                      createText6(
                                                          f.floatValue(),
                                                          number.step(),
                                                          number.min()));
                                                })
                                            .onChange(
                                                f2 -> {
                                                  mgf9hldmchqz();
                                                });
                                        numberSettingControl.refreshState();
                                      },
                                      new ComponentKeyService[0])
                                  .key("slider"),
                              createComponentKeyService18(
                                  str3,
                                  createText6(
                                      number.get().floatValue(), number.step(), number.min()),
                                  number)
                            })
                    .key("control");
      }
    } else if (moduleSetting instanceof ModuleSetting.Range) {
      ModuleSetting.Range range = (ModuleSetting.Range) moduleSetting;
      componentKeyServiceKey =
          ComponentBoxService.row(
                  (Consumer<LayoutContainerNode>)
                      layoutContainerNode2 -> {
                        layoutContainerNode2
                            .gap(Float.intBitsToFloat(1090519040))
                            .align(ScenePctService.Align.CENTER)
                            .minWidth(0.0f);
                      },
                  (ComponentKeyService<?>[])
                      new ComponentKeyService[] {
                        ComponentBoxService.node(
                                "setting-range",
                                () -> {
                                  return new ControlOnPreviewHandler(range);
                                },
                                controlOnPreviewHandler -> {
                                  controlOnPreviewHandler
                                      .material(true)
                                      .compact(false)
                                      .height(
                                          LayoutOperationHandler.px(
                                              Float.intBitsToFloat(1109393408)))
                                      .flex(1.0f)
                                      .minWidth(Float.intBitsToFloat(1111490560))
                                      .id(str3 + ".range-slider");
                                  controlOnPreviewHandler
                                      .onPreview(
                                          (f, f2) -> {
                                            updateState4(
                                                str3,
                                                createText6(
                                                        f.floatValue(), range.step(), range.min())
                                                    + " – "
                                                    + createText6(
                                                        f2.floatValue(),
                                                        range.step(),
                                                        range.min()));
                                          })
                                      .onChange(
                                          (f3, f4) -> {
                                            mgf9hldmchqz();
                                          });
                                  controlOnPreviewHandler.refreshState();
                                },
                                new ComponentKeyService[0])
                            .key("slider"),
                        createComponentKeyService18(
                            str3,
                            createText6(range.low(), range.step(), range.min())
                                + " – "
                                + createText6(range.high(), range.step(), range.min()),
                            range)
                      })
              .key("control");
    } else if (moduleSetting instanceof ModuleSetting.Mode) {
      ModuleSetting.Mode mode2 = (ModuleSetting.Mode) moduleSetting;
      if (mode2.hasPreviews()) {
        componentKeyServiceKey = createComponentKeyService19(str3, mode2);
      } else if (moduleSetting instanceof ModuleSetting.Mode) {
        mode = (ModuleSetting.Mode) moduleSetting;
        if (checkCondition3(mode.options(), false)) {
          componentKeyServiceKey = createComponentKeyService20(str3, mode, mode.options(), false);
        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
          multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
          if (checkCondition3(multiSelect.options(), true)) {
            componentKeyServiceKey =
                createComponentKeyService20(str3, multiSelect, multiSelect.options(), true);
          } else if (moduleSetting instanceof ModuleLayoutService) {
            ModuleLayoutService moduleLayoutService = (ModuleLayoutService) moduleSetting;
            componentKeyServiceKey =
                InventoryRenderer.preview(
                    str3,
                    moduleLayoutService,
                    () -> {
                      updateState17(moduleLayoutService);
                    });
          } else if (moduleSetting instanceof ModuleSetting.Text) {
            ModuleSetting.Text text = (ModuleSetting.Text) moduleSetting;
            componentKeyServiceKey =
                ComponentBoxService.node(
                        "setting-text",
                        ControlLetterSpacingService::new,
                        controlLetterSpacingService -> {
                          MaterialTextService.input(controlLetterSpacingService);
                          controlLetterSpacingService
                              .fontSize(Float.intBitsToFloat(1097859072))
                              .letterSpacing(Float.intBitsToFloat(1036831949))
                              .id(str3 + ".text-input")
                              .maxLength(text.maxLength())
                              .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                              .minWidth(0.0f)
                              .interactive(zIsActive)
                              .pointerEvents(zIsActive);
                          if (!controlLetterSpacingService.focused()) {
                            controlLetterSpacingService.text(text.get());
                          }
                          Objects.requireNonNull(text);
                          controlLetterSpacingService
                              .onSubmit(text::set)
                              .onUnfocus(
                                  () -> {
                                    if (text.isActive()) {
                                      text.set(controlLetterSpacingService.text());
                                    }
                                  });
                        },
                        new ComponentKeyService[0])
                    .key("control");
          } else if (moduleSetting instanceof ModuleSetting.Keybind) {
            ModuleSetting.Keybind keybind = (ModuleSetting.Keybind) moduleSetting;
            componentKeyServiceKey =
                ComponentBoxService.node(
                        "setting-keybind",
                        ControlGetAnimPropertyService::new,
                        controlGetAnimPropertyService -> {
                          ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                              controlGetAnimPropertyService
                                  .material(true)
                                  .keyNameFn(
                                      (v0) -> {
                                        return createText8(v0);
                                      })
                                  .keyCode(keybind.get().intValue())
                                  .fontSize(Float.intBitsToFloat(1096810496))
                                  .cornerRadius(Float.intBitsToFloat(1094713344))
                                  .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                                  .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                  .height(
                                      LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                  .id(str3 + ".keybind")
                                  .interactive(zIsActive)
                                  .pointerEvents(zIsActive);
                          Objects.requireNonNull(keybind);
                          controlGetAnimPropertyServicePointerEvents
                              .onChange(
                                  (v1) -> {
                                    keybind.set(v1);
                                  })
                              .chooser(
                                  () -> {
                                    updateState14(keybind);
                                  })
                              .tooltip(
                                  "Choose a key on the rendered keyboard · Right-click to unbind");
                        },
                        new ComponentKeyService[0])
                    .key("control");
          } else {
            if (moduleSetting instanceof ModuleEntriesService) {
              ModuleEntriesService moduleEntriesService = (ModuleEntriesService) moduleSetting;
              strValueOf =
                  moduleEntriesService.selected().edition()
                      + " / "
                      + moduleEntriesService.selected().label();
            } else if (moduleSetting instanceof ModuleSetting.Mode) {
              strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
            } else if (moduleSetting instanceof ModuleSetting.Color) {
              strValueOf =
                  String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
            } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
              ModuleSetting.MultiSelect multiSelect2 = (ModuleSetting.MultiSelect) moduleSetting;
              strValueOf =
                  multiSelect2.get().size() + " of " + multiSelect2.options().length + " selected";
            } else if (moduleSetting instanceof ModuleSetting.Curve) {
              strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
            } else {
              strValueOf = String.valueOf(moduleSetting.get());
            }
            String str4 = strValueOf;
            arrayList = new ArrayList();
            if (moduleSetting instanceof ModuleSetting.Color) {
              ModuleSetting.Color color = (ModuleSetting.Color) moduleSetting;
              arrayList.add(
                  ComponentBoxService.node(
                      "material-color-swatch",
                      MaterialColorService::new,
                      materialColorService -> {
                        materialColorService
                            .color(color.get().intValue())
                            .radius(Float.intBitsToFloat(1086324736))
                            .size(
                                Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                            .flexShrink(0.0f)
                            .pointerEvents(false);
                      },
                      new ComponentKeyService[0]));
            }
            if (zIsActive) {
              iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
            } else {
              iMdnv1ub7rbpv = calculateValue();
            }
            arrayList.add(
                createComponentKeyService25(str4, iMdnv1ub7rbpv)
                    .props(
                        sceneTextService2 -> {
                          sceneTextService2.flex(1.0f).minWidth(0.0f);
                        }));
            if (moduleSetting instanceof ModuleSetting.Curve) {
              str2 = "tune";
            } else {
              str2 = "expand_more";
            }
            if (zIsActive) {
              iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
            } else {
              iMdnv1ub7rbpv2 = calculateValue();
            }
            arrayList.add(
                MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                    .props(
                        sceneSrcService -> {
                          sceneSrcService.size(
                              Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                        }));
            componentKeyServiceKey =
                ComponentBoxService.node(
                        "setting-choice",
                        MaterialJoinedService::new,
                        materialJoinedService -> {
                          materialJoinedService
                              .colors(
                                  MaterialIsLightService.SECONDARY_CONTAINER,
                                  MaterialIsLightService.ON_SECONDARY_CONTAINER)
                              .shape(
                                  Float.intBitsToFloat(1094713344),
                                  Float.intBitsToFloat(1108344832))
                              .available(zIsActive);
                          materialJoinedService
                              .id(str3 + ".edit")
                              .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                              .padding(0.0f, Float.intBitsToFloat(1094713344))
                              .gap(Float.intBitsToFloat(1090519040))
                              .direction(ScenePctService.Direction.ROW)
                              .align(ScenePctService.Align.CENTER)
                              .onClick(
                                  () -> {
                                    updateState17(moduleSetting);
                                  });
                        },
                        (ComponentKeyService[])
                            arrayList.toArray(
                                i -> {
                                  return new ComponentKeyService[i];
                                }))
                    .key("control");
          }
        } else if (moduleSetting instanceof ModuleLayoutService) {
          ModuleLayoutService moduleLayoutService2 = (ModuleLayoutService) moduleSetting;
          componentKeyServiceKey =
              InventoryRenderer.preview(
                  str3,
                  moduleLayoutService2,
                  () -> {
                    updateState17(moduleLayoutService2);
                  });
        } else if (moduleSetting instanceof ModuleSetting.Text) {
          ModuleSetting.Text text2 = (ModuleSetting.Text) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-text",
                      ControlLetterSpacingService::new,
                      controlLetterSpacingService2 -> {
                        MaterialTextService.input(controlLetterSpacingService2);
                        controlLetterSpacingService2
                            .fontSize(Float.intBitsToFloat(1097859072))
                            .letterSpacing(Float.intBitsToFloat(1036831949))
                            .id(str3 + ".text-input")
                            .maxLength(text2.maxLength())
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .minWidth(0.0f)
                            .interactive(zIsActive)
                            .pointerEvents(zIsActive);
                        if (!controlLetterSpacingService2.focused()) {
                          controlLetterSpacingService2.text(text2.get());
                        }
                        Objects.requireNonNull(text2);
                        controlLetterSpacingService2
                            .onSubmit(text2::set)
                            .onUnfocus(
                                () -> {
                                  if (text2.isActive()) {
                                    text2.set(controlLetterSpacingService2.text());
                                  }
                                });
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else if (moduleSetting instanceof ModuleSetting.Keybind) {
          ModuleSetting.Keybind keybind2 = (ModuleSetting.Keybind) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-keybind",
                      ControlGetAnimPropertyService::new,
                      controlGetAnimPropertyService2 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                            controlGetAnimPropertyService2
                                .material(true)
                                .keyNameFn(
                                    (v0) -> {
                                      return createText8(v0);
                                    })
                                .keyCode(keybind2.get().intValue())
                                .fontSize(Float.intBitsToFloat(1096810496))
                                .cornerRadius(Float.intBitsToFloat(1094713344))
                                .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                                .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                .id(str3 + ".keybind")
                                .interactive(zIsActive)
                                .pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind2);
                        controlGetAnimPropertyServicePointerEvents
                            .onChange(
                                (v1) -> {
                                  keybind2.set(v1);
                                })
                            .chooser(
                                () -> {
                                  updateState14(keybind2);
                                })
                            .tooltip(
                                "Choose a key on the rendered keyboard · Right-click to unbind");
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else {
          if (moduleSetting instanceof ModuleEntriesService) {
            ModuleEntriesService moduleEntriesService2 = (ModuleEntriesService) moduleSetting;
            strValueOf =
                moduleEntriesService2.selected().edition()
                    + " / "
                    + moduleEntriesService2.selected().label();
          } else if (moduleSetting instanceof ModuleSetting.Mode) {
            strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
          } else if (moduleSetting instanceof ModuleSetting.Color) {
            strValueOf =
                String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
          } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
            ModuleSetting.MultiSelect multiSelect3 = (ModuleSetting.MultiSelect) moduleSetting;
            strValueOf =
                multiSelect3.get().size() + " of " + multiSelect3.options().length + " selected";
          } else if (moduleSetting instanceof ModuleSetting.Curve) {
            strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
          } else {
            strValueOf = String.valueOf(moduleSetting.get());
          }
          String str5 = strValueOf;
          arrayList = new ArrayList();
          if (moduleSetting instanceof ModuleSetting.Color) {
            ModuleSetting.Color color2 = (ModuleSetting.Color) moduleSetting;
            arrayList.add(
                ComponentBoxService.node(
                    "material-color-swatch",
                    MaterialColorService::new,
                    materialColorService2 -> {
                      materialColorService2
                          .color(color2.get().intValue())
                          .radius(Float.intBitsToFloat(1086324736))
                          .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                          .flexShrink(0.0f)
                          .pointerEvents(false);
                    },
                    new ComponentKeyService[0]));
          }
          if (zIsActive) {
            iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv = calculateValue();
          }
          arrayList.add(
              createComponentKeyService25(str5, iMdnv1ub7rbpv)
                  .props(
                      sceneTextService3 -> {
                        sceneTextService3.flex(1.0f).minWidth(0.0f);
                      }));
          if (moduleSetting instanceof ModuleSetting.Curve) {
            str2 = "tune";
          } else {
            str2 = "expand_more";
          }
          if (zIsActive) {
            iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv2 = calculateValue();
          }
          arrayList.add(
              MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                  .props(
                      sceneSrcService2 -> {
                        sceneSrcService2.size(
                            Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                      }));
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-choice",
                      MaterialJoinedService::new,
                      materialJoinedService2 -> {
                        materialJoinedService2
                            .colors(
                                MaterialIsLightService.SECONDARY_CONTAINER,
                                MaterialIsLightService.ON_SECONDARY_CONTAINER)
                            .shape(
                                Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                            .available(zIsActive);
                        materialJoinedService2
                            .id(str3 + ".edit")
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .padding(0.0f, Float.intBitsToFloat(1094713344))
                            .gap(Float.intBitsToFloat(1090519040))
                            .direction(ScenePctService.Direction.ROW)
                            .align(ScenePctService.Align.CENTER)
                            .onClick(
                                () -> {
                                  updateState17(moduleSetting);
                                });
                      },
                      (ComponentKeyService[])
                          arrayList.toArray(
                              i2 -> {
                                return new ComponentKeyService[i2];
                              }))
                  .key("control");
        }
      } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
        multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
        if (checkCondition3(multiSelect.options(), true)) {
          componentKeyServiceKey =
              createComponentKeyService20(str3, multiSelect, multiSelect.options(), true);
        } else if (moduleSetting instanceof ModuleLayoutService) {
          ModuleLayoutService moduleLayoutService3 = (ModuleLayoutService) moduleSetting;
          componentKeyServiceKey =
              InventoryRenderer.preview(
                  str3,
                  moduleLayoutService3,
                  () -> {
                    updateState17(moduleLayoutService3);
                  });
        } else if (moduleSetting instanceof ModuleSetting.Text) {
          ModuleSetting.Text text3 = (ModuleSetting.Text) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-text",
                      ControlLetterSpacingService::new,
                      controlLetterSpacingService3 -> {
                        MaterialTextService.input(controlLetterSpacingService3);
                        controlLetterSpacingService3
                            .fontSize(Float.intBitsToFloat(1097859072))
                            .letterSpacing(Float.intBitsToFloat(1036831949))
                            .id(str3 + ".text-input")
                            .maxLength(text3.maxLength())
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .minWidth(0.0f)
                            .interactive(zIsActive)
                            .pointerEvents(zIsActive);
                        if (!controlLetterSpacingService3.focused()) {
                          controlLetterSpacingService3.text(text3.get());
                        }
                        Objects.requireNonNull(text3);
                        controlLetterSpacingService3
                            .onSubmit(text3::set)
                            .onUnfocus(
                                () -> {
                                  if (text3.isActive()) {
                                    text3.set(controlLetterSpacingService3.text());
                                  }
                                });
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else if (moduleSetting instanceof ModuleSetting.Keybind) {
          ModuleSetting.Keybind keybind3 = (ModuleSetting.Keybind) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-keybind",
                      ControlGetAnimPropertyService::new,
                      controlGetAnimPropertyService3 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                            controlGetAnimPropertyService3
                                .material(true)
                                .keyNameFn(
                                    (v0) -> {
                                      return createText8(v0);
                                    })
                                .keyCode(keybind3.get().intValue())
                                .fontSize(Float.intBitsToFloat(1096810496))
                                .cornerRadius(Float.intBitsToFloat(1094713344))
                                .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                                .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                .id(str3 + ".keybind")
                                .interactive(zIsActive)
                                .pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind3);
                        controlGetAnimPropertyServicePointerEvents
                            .onChange(
                                (v1) -> {
                                  keybind3.set(v1);
                                })
                            .chooser(
                                () -> {
                                  updateState14(keybind3);
                                })
                            .tooltip(
                                "Choose a key on the rendered keyboard · Right-click to unbind");
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else {
          if (moduleSetting instanceof ModuleEntriesService) {
            ModuleEntriesService moduleEntriesService3 = (ModuleEntriesService) moduleSetting;
            strValueOf =
                moduleEntriesService3.selected().edition()
                    + " / "
                    + moduleEntriesService3.selected().label();
          } else if (moduleSetting instanceof ModuleSetting.Mode) {
            strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
          } else if (moduleSetting instanceof ModuleSetting.Color) {
            strValueOf =
                String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
          } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
            ModuleSetting.MultiSelect multiSelect4 = (ModuleSetting.MultiSelect) moduleSetting;
            strValueOf =
                multiSelect4.get().size() + " of " + multiSelect4.options().length + " selected";
          } else if (moduleSetting instanceof ModuleSetting.Curve) {
            strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
          } else {
            strValueOf = String.valueOf(moduleSetting.get());
          }
          String str6 = strValueOf;
          arrayList = new ArrayList();
          if (moduleSetting instanceof ModuleSetting.Color) {
            ModuleSetting.Color color3 = (ModuleSetting.Color) moduleSetting;
            arrayList.add(
                ComponentBoxService.node(
                    "material-color-swatch",
                    MaterialColorService::new,
                    materialColorService3 -> {
                      materialColorService3
                          .color(color3.get().intValue())
                          .radius(Float.intBitsToFloat(1086324736))
                          .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                          .flexShrink(0.0f)
                          .pointerEvents(false);
                    },
                    new ComponentKeyService[0]));
          }
          if (zIsActive) {
            iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv = calculateValue();
          }
          arrayList.add(
              createComponentKeyService25(str6, iMdnv1ub7rbpv)
                  .props(
                      sceneTextService4 -> {
                        sceneTextService4.flex(1.0f).minWidth(0.0f);
                      }));
          if (moduleSetting instanceof ModuleSetting.Curve) {
            str2 = "tune";
          } else {
            str2 = "expand_more";
          }
          if (zIsActive) {
            iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv2 = calculateValue();
          }
          arrayList.add(
              MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                  .props(
                      sceneSrcService3 -> {
                        sceneSrcService3.size(
                            Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                      }));
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-choice",
                      MaterialJoinedService::new,
                      materialJoinedService3 -> {
                        materialJoinedService3
                            .colors(
                                MaterialIsLightService.SECONDARY_CONTAINER,
                                MaterialIsLightService.ON_SECONDARY_CONTAINER)
                            .shape(
                                Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                            .available(zIsActive);
                        materialJoinedService3
                            .id(str3 + ".edit")
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .padding(0.0f, Float.intBitsToFloat(1094713344))
                            .gap(Float.intBitsToFloat(1090519040))
                            .direction(ScenePctService.Direction.ROW)
                            .align(ScenePctService.Align.CENTER)
                            .onClick(
                                () -> {
                                  updateState17(moduleSetting);
                                });
                      },
                      (ComponentKeyService[])
                          arrayList.toArray(
                              i3 -> {
                                return new ComponentKeyService[i3];
                              }))
                  .key("control");
        }
      } else if (moduleSetting instanceof ModuleLayoutService) {
        ModuleLayoutService moduleLayoutService4 = (ModuleLayoutService) moduleSetting;
        componentKeyServiceKey =
            InventoryRenderer.preview(
                str3,
                moduleLayoutService4,
                () -> {
                  updateState17(moduleLayoutService4);
                });
      } else if (moduleSetting instanceof ModuleSetting.Text) {
        ModuleSetting.Text text4 = (ModuleSetting.Text) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-text",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService4 -> {
                      MaterialTextService.input(controlLetterSpacingService4);
                      controlLetterSpacingService4
                          .fontSize(Float.intBitsToFloat(1097859072))
                          .letterSpacing(Float.intBitsToFloat(1036831949))
                          .id(str3 + ".text-input")
                          .maxLength(text4.maxLength())
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .minWidth(0.0f)
                          .interactive(zIsActive)
                          .pointerEvents(zIsActive);
                      if (!controlLetterSpacingService4.focused()) {
                        controlLetterSpacingService4.text(text4.get());
                      }
                      Objects.requireNonNull(text4);
                      controlLetterSpacingService4
                          .onSubmit(text4::set)
                          .onUnfocus(
                              () -> {
                                if (text4.isActive()) {
                                  text4.set(controlLetterSpacingService4.text());
                                }
                              });
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else if (moduleSetting instanceof ModuleSetting.Keybind) {
        ModuleSetting.Keybind keybind4 = (ModuleSetting.Keybind) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-keybind",
                    ControlGetAnimPropertyService::new,
                    controlGetAnimPropertyService4 -> {
                      ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                          controlGetAnimPropertyService4
                              .material(true)
                              .keyNameFn(
                                  (v0) -> {
                                    return createText8(v0);
                                  })
                              .keyCode(keybind4.get().intValue())
                              .fontSize(Float.intBitsToFloat(1096810496))
                              .cornerRadius(Float.intBitsToFloat(1094713344))
                              .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                              .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                              .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                              .id(str3 + ".keybind")
                              .interactive(zIsActive)
                              .pointerEvents(zIsActive);
                      Objects.requireNonNull(keybind4);
                      controlGetAnimPropertyServicePointerEvents
                          .onChange(
                              (v1) -> {
                                keybind4.set(v1);
                              })
                          .chooser(
                              () -> {
                                updateState14(keybind4);
                              })
                          .tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else {
        if (moduleSetting instanceof ModuleEntriesService) {
          ModuleEntriesService moduleEntriesService4 = (ModuleEntriesService) moduleSetting;
          strValueOf =
              moduleEntriesService4.selected().edition()
                  + " / "
                  + moduleEntriesService4.selected().label();
        } else if (moduleSetting instanceof ModuleSetting.Mode) {
          strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
        } else if (moduleSetting instanceof ModuleSetting.Color) {
          strValueOf =
              String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
          ModuleSetting.MultiSelect multiSelect5 = (ModuleSetting.MultiSelect) moduleSetting;
          strValueOf =
              multiSelect5.get().size() + " of " + multiSelect5.options().length + " selected";
        } else if (moduleSetting instanceof ModuleSetting.Curve) {
          strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
        } else {
          strValueOf = String.valueOf(moduleSetting.get());
        }
        String str7 = strValueOf;
        arrayList = new ArrayList();
        if (moduleSetting instanceof ModuleSetting.Color) {
          ModuleSetting.Color color4 = (ModuleSetting.Color) moduleSetting;
          arrayList.add(
              ComponentBoxService.node(
                  "material-color-swatch",
                  MaterialColorService::new,
                  materialColorService4 -> {
                    materialColorService4
                        .color(color4.get().intValue())
                        .radius(Float.intBitsToFloat(1086324736))
                        .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                        .flexShrink(0.0f)
                        .pointerEvents(false);
                  },
                  new ComponentKeyService[0]));
        }
        if (zIsActive) {
          iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv = calculateValue();
        }
        arrayList.add(
            createComponentKeyService25(str7, iMdnv1ub7rbpv)
                .props(
                    sceneTextService5 -> {
                      sceneTextService5.flex(1.0f).minWidth(0.0f);
                    }));
        if (moduleSetting instanceof ModuleSetting.Curve) {
          str2 = "tune";
        } else {
          str2 = "expand_more";
        }
        if (zIsActive) {
          iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv2 = calculateValue();
        }
        arrayList.add(
            MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                .props(
                    sceneSrcService4 -> {
                      sceneSrcService4.size(
                          Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-choice",
                    MaterialJoinedService::new,
                    materialJoinedService4 -> {
                      materialJoinedService4
                          .colors(
                              MaterialIsLightService.SECONDARY_CONTAINER,
                              MaterialIsLightService.ON_SECONDARY_CONTAINER)
                          .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                          .available(zIsActive);
                      materialJoinedService4
                          .id(str3 + ".edit")
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .padding(0.0f, Float.intBitsToFloat(1094713344))
                          .gap(Float.intBitsToFloat(1090519040))
                          .direction(ScenePctService.Direction.ROW)
                          .align(ScenePctService.Align.CENTER)
                          .onClick(
                              () -> {
                                updateState17(moduleSetting);
                              });
                    },
                    (ComponentKeyService[])
                        arrayList.toArray(
                            i4 -> {
                              return new ComponentKeyService[i4];
                            }))
                .key("control");
      }
    } else if (moduleSetting instanceof ModuleSetting.Mode) {
      mode = (ModuleSetting.Mode) moduleSetting;
      if (checkCondition3(mode.options(), false)) {
        componentKeyServiceKey = createComponentKeyService20(str3, mode, mode.options(), false);
      } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
        multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
        if (checkCondition3(multiSelect.options(), true)) {
          componentKeyServiceKey =
              createComponentKeyService20(str3, multiSelect, multiSelect.options(), true);
        } else if (moduleSetting instanceof ModuleLayoutService) {
          ModuleLayoutService moduleLayoutService5 = (ModuleLayoutService) moduleSetting;
          componentKeyServiceKey =
              InventoryRenderer.preview(
                  str3,
                  moduleLayoutService5,
                  () -> {
                    updateState17(moduleLayoutService5);
                  });
        } else if (moduleSetting instanceof ModuleSetting.Text) {
          ModuleSetting.Text text5 = (ModuleSetting.Text) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-text",
                      ControlLetterSpacingService::new,
                      controlLetterSpacingService5 -> {
                        MaterialTextService.input(controlLetterSpacingService5);
                        controlLetterSpacingService5
                            .fontSize(Float.intBitsToFloat(1097859072))
                            .letterSpacing(Float.intBitsToFloat(1036831949))
                            .id(str3 + ".text-input")
                            .maxLength(text5.maxLength())
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .minWidth(0.0f)
                            .interactive(zIsActive)
                            .pointerEvents(zIsActive);
                        if (!controlLetterSpacingService5.focused()) {
                          controlLetterSpacingService5.text(text5.get());
                        }
                        Objects.requireNonNull(text5);
                        controlLetterSpacingService5
                            .onSubmit(text5::set)
                            .onUnfocus(
                                () -> {
                                  if (text5.isActive()) {
                                    text5.set(controlLetterSpacingService5.text());
                                  }
                                });
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else if (moduleSetting instanceof ModuleSetting.Keybind) {
          ModuleSetting.Keybind keybind5 = (ModuleSetting.Keybind) moduleSetting;
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-keybind",
                      ControlGetAnimPropertyService::new,
                      controlGetAnimPropertyService5 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                            controlGetAnimPropertyService5
                                .material(true)
                                .keyNameFn(
                                    (v0) -> {
                                      return createText8(v0);
                                    })
                                .keyCode(keybind5.get().intValue())
                                .fontSize(Float.intBitsToFloat(1096810496))
                                .cornerRadius(Float.intBitsToFloat(1094713344))
                                .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                                .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                                .id(str3 + ".keybind")
                                .interactive(zIsActive)
                                .pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind5);
                        controlGetAnimPropertyServicePointerEvents
                            .onChange(
                                (v1) -> {
                                  keybind5.set(v1);
                                })
                            .chooser(
                                () -> {
                                  updateState14(keybind5);
                                })
                            .tooltip(
                                "Choose a key on the rendered keyboard · Right-click to unbind");
                      },
                      new ComponentKeyService[0])
                  .key("control");
        } else {
          if (moduleSetting instanceof ModuleEntriesService) {
            ModuleEntriesService moduleEntriesService5 = (ModuleEntriesService) moduleSetting;
            strValueOf =
                moduleEntriesService5.selected().edition()
                    + " / "
                    + moduleEntriesService5.selected().label();
          } else if (moduleSetting instanceof ModuleSetting.Mode) {
            strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
          } else if (moduleSetting instanceof ModuleSetting.Color) {
            strValueOf =
                String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
          } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
            ModuleSetting.MultiSelect multiSelect6 = (ModuleSetting.MultiSelect) moduleSetting;
            strValueOf =
                multiSelect6.get().size() + " of " + multiSelect6.options().length + " selected";
          } else if (moduleSetting instanceof ModuleSetting.Curve) {
            strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
          } else {
            strValueOf = String.valueOf(moduleSetting.get());
          }
          String str8 = strValueOf;
          arrayList = new ArrayList();
          if (moduleSetting instanceof ModuleSetting.Color) {
            ModuleSetting.Color color5 = (ModuleSetting.Color) moduleSetting;
            arrayList.add(
                ComponentBoxService.node(
                    "material-color-swatch",
                    MaterialColorService::new,
                    materialColorService5 -> {
                      materialColorService5
                          .color(color5.get().intValue())
                          .radius(Float.intBitsToFloat(1086324736))
                          .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                          .flexShrink(0.0f)
                          .pointerEvents(false);
                    },
                    new ComponentKeyService[0]));
          }
          if (zIsActive) {
            iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv = calculateValue();
          }
          arrayList.add(
              createComponentKeyService25(str8, iMdnv1ub7rbpv)
                  .props(
                      sceneTextService6 -> {
                        sceneTextService6.flex(1.0f).minWidth(0.0f);
                      }));
          if (moduleSetting instanceof ModuleSetting.Curve) {
            str2 = "tune";
          } else {
            str2 = "expand_more";
          }
          if (zIsActive) {
            iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
          } else {
            iMdnv1ub7rbpv2 = calculateValue();
          }
          arrayList.add(
              MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                  .props(
                      sceneSrcService5 -> {
                        sceneSrcService5.size(
                            Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                      }));
          componentKeyServiceKey =
              ComponentBoxService.node(
                      "setting-choice",
                      MaterialJoinedService::new,
                      materialJoinedService5 -> {
                        materialJoinedService5
                            .colors(
                                MaterialIsLightService.SECONDARY_CONTAINER,
                                MaterialIsLightService.ON_SECONDARY_CONTAINER)
                            .shape(
                                Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                            .available(zIsActive);
                        materialJoinedService5
                            .id(str3 + ".edit")
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .padding(0.0f, Float.intBitsToFloat(1094713344))
                            .gap(Float.intBitsToFloat(1090519040))
                            .direction(ScenePctService.Direction.ROW)
                            .align(ScenePctService.Align.CENTER)
                            .onClick(
                                () -> {
                                  updateState17(moduleSetting);
                                });
                      },
                      (ComponentKeyService[])
                          arrayList.toArray(
                              i5 -> {
                                return new ComponentKeyService[i5];
                              }))
                  .key("control");
        }
      } else if (moduleSetting instanceof ModuleLayoutService) {
        ModuleLayoutService moduleLayoutService6 = (ModuleLayoutService) moduleSetting;
        componentKeyServiceKey =
            InventoryRenderer.preview(
                str3,
                moduleLayoutService6,
                () -> {
                  updateState17(moduleLayoutService6);
                });
      } else if (moduleSetting instanceof ModuleSetting.Text) {
        ModuleSetting.Text text6 = (ModuleSetting.Text) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-text",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService6 -> {
                      MaterialTextService.input(controlLetterSpacingService6);
                      controlLetterSpacingService6
                          .fontSize(Float.intBitsToFloat(1097859072))
                          .letterSpacing(Float.intBitsToFloat(1036831949))
                          .id(str3 + ".text-input")
                          .maxLength(text6.maxLength())
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .minWidth(0.0f)
                          .interactive(zIsActive)
                          .pointerEvents(zIsActive);
                      if (!controlLetterSpacingService6.focused()) {
                        controlLetterSpacingService6.text(text6.get());
                      }
                      Objects.requireNonNull(text6);
                      controlLetterSpacingService6
                          .onSubmit(text6::set)
                          .onUnfocus(
                              () -> {
                                if (text6.isActive()) {
                                  text6.set(controlLetterSpacingService6.text());
                                }
                              });
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else if (moduleSetting instanceof ModuleSetting.Keybind) {
        ModuleSetting.Keybind keybind6 = (ModuleSetting.Keybind) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-keybind",
                    ControlGetAnimPropertyService::new,
                    controlGetAnimPropertyService6 -> {
                      ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                          controlGetAnimPropertyService6
                              .material(true)
                              .keyNameFn(
                                  (v0) -> {
                                    return createText8(v0);
                                  })
                              .keyCode(keybind6.get().intValue())
                              .fontSize(Float.intBitsToFloat(1096810496))
                              .cornerRadius(Float.intBitsToFloat(1094713344))
                              .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                              .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                              .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                              .id(str3 + ".keybind")
                              .interactive(zIsActive)
                              .pointerEvents(zIsActive);
                      Objects.requireNonNull(keybind6);
                      controlGetAnimPropertyServicePointerEvents
                          .onChange(
                              (v1) -> {
                                keybind6.set(v1);
                              })
                          .chooser(
                              () -> {
                                updateState14(keybind6);
                              })
                          .tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else {
        if (moduleSetting instanceof ModuleEntriesService) {
          ModuleEntriesService moduleEntriesService6 = (ModuleEntriesService) moduleSetting;
          strValueOf =
              moduleEntriesService6.selected().edition()
                  + " / "
                  + moduleEntriesService6.selected().label();
        } else if (moduleSetting instanceof ModuleSetting.Mode) {
          strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
        } else if (moduleSetting instanceof ModuleSetting.Color) {
          strValueOf =
              String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
          ModuleSetting.MultiSelect multiSelect7 = (ModuleSetting.MultiSelect) moduleSetting;
          strValueOf =
              multiSelect7.get().size() + " of " + multiSelect7.options().length + " selected";
        } else if (moduleSetting instanceof ModuleSetting.Curve) {
          strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
        } else {
          strValueOf = String.valueOf(moduleSetting.get());
        }
        String str9 = strValueOf;
        arrayList = new ArrayList();
        if (moduleSetting instanceof ModuleSetting.Color) {
          ModuleSetting.Color color6 = (ModuleSetting.Color) moduleSetting;
          arrayList.add(
              ComponentBoxService.node(
                  "material-color-swatch",
                  MaterialColorService::new,
                  materialColorService6 -> {
                    materialColorService6
                        .color(color6.get().intValue())
                        .radius(Float.intBitsToFloat(1086324736))
                        .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                        .flexShrink(0.0f)
                        .pointerEvents(false);
                  },
                  new ComponentKeyService[0]));
        }
        if (zIsActive) {
          iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv = calculateValue();
        }
        arrayList.add(
            createComponentKeyService25(str9, iMdnv1ub7rbpv)
                .props(
                    sceneTextService7 -> {
                      sceneTextService7.flex(1.0f).minWidth(0.0f);
                    }));
        if (moduleSetting instanceof ModuleSetting.Curve) {
          str2 = "tune";
        } else {
          str2 = "expand_more";
        }
        if (zIsActive) {
          iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv2 = calculateValue();
        }
        arrayList.add(
            MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                .props(
                    sceneSrcService6 -> {
                      sceneSrcService6.size(
                          Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-choice",
                    MaterialJoinedService::new,
                    materialJoinedService6 -> {
                      materialJoinedService6
                          .colors(
                              MaterialIsLightService.SECONDARY_CONTAINER,
                              MaterialIsLightService.ON_SECONDARY_CONTAINER)
                          .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                          .available(zIsActive);
                      materialJoinedService6
                          .id(str3 + ".edit")
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .padding(0.0f, Float.intBitsToFloat(1094713344))
                          .gap(Float.intBitsToFloat(1090519040))
                          .direction(ScenePctService.Direction.ROW)
                          .align(ScenePctService.Align.CENTER)
                          .onClick(
                              () -> {
                                updateState17(moduleSetting);
                              });
                    },
                    (ComponentKeyService[])
                        arrayList.toArray(
                            i6 -> {
                              return new ComponentKeyService[i6];
                            }))
                .key("control");
      }
    } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
      multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
      if (checkCondition3(multiSelect.options(), true)) {
        componentKeyServiceKey =
            createComponentKeyService20(str3, multiSelect, multiSelect.options(), true);
      } else if (moduleSetting instanceof ModuleLayoutService) {
        ModuleLayoutService moduleLayoutService7 = (ModuleLayoutService) moduleSetting;
        componentKeyServiceKey =
            InventoryRenderer.preview(
                str3,
                moduleLayoutService7,
                () -> {
                  updateState17(moduleLayoutService7);
                });
      } else if (moduleSetting instanceof ModuleSetting.Text) {
        ModuleSetting.Text text7 = (ModuleSetting.Text) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-text",
                    ControlLetterSpacingService::new,
                    controlLetterSpacingService7 -> {
                      MaterialTextService.input(controlLetterSpacingService7);
                      controlLetterSpacingService7
                          .fontSize(Float.intBitsToFloat(1097859072))
                          .letterSpacing(Float.intBitsToFloat(1036831949))
                          .id(str3 + ".text-input")
                          .maxLength(text7.maxLength())
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .minWidth(0.0f)
                          .interactive(zIsActive)
                          .pointerEvents(zIsActive);
                      if (!controlLetterSpacingService7.focused()) {
                        controlLetterSpacingService7.text(text7.get());
                      }
                      Objects.requireNonNull(text7);
                      controlLetterSpacingService7
                          .onSubmit(text7::set)
                          .onUnfocus(
                              () -> {
                                if (text7.isActive()) {
                                  text7.set(controlLetterSpacingService7.text());
                                }
                              });
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else if (moduleSetting instanceof ModuleSetting.Keybind) {
        ModuleSetting.Keybind keybind7 = (ModuleSetting.Keybind) moduleSetting;
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-keybind",
                    ControlGetAnimPropertyService::new,
                    controlGetAnimPropertyService7 -> {
                      ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                          controlGetAnimPropertyService7
                              .material(true)
                              .keyNameFn(
                                  (v0) -> {
                                    return createText8(v0);
                                  })
                              .keyCode(keybind7.get().intValue())
                              .fontSize(Float.intBitsToFloat(1096810496))
                              .cornerRadius(Float.intBitsToFloat(1094713344))
                              .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                              .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                              .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                              .id(str3 + ".keybind")
                              .interactive(zIsActive)
                              .pointerEvents(zIsActive);
                      Objects.requireNonNull(keybind7);
                      controlGetAnimPropertyServicePointerEvents
                          .onChange(
                              (v1) -> {
                                keybind7.set(v1);
                              })
                          .chooser(
                              () -> {
                                updateState14(keybind7);
                              })
                          .tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    },
                    new ComponentKeyService[0])
                .key("control");
      } else {
        if (moduleSetting instanceof ModuleEntriesService) {
          ModuleEntriesService moduleEntriesService7 = (ModuleEntriesService) moduleSetting;
          strValueOf =
              moduleEntriesService7.selected().edition()
                  + " / "
                  + moduleEntriesService7.selected().label();
        } else if (moduleSetting instanceof ModuleSetting.Mode) {
          strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
        } else if (moduleSetting instanceof ModuleSetting.Color) {
          strValueOf =
              String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
          ModuleSetting.MultiSelect multiSelect8 = (ModuleSetting.MultiSelect) moduleSetting;
          strValueOf =
              multiSelect8.get().size() + " of " + multiSelect8.options().length + " selected";
        } else if (moduleSetting instanceof ModuleSetting.Curve) {
          strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
        } else {
          strValueOf = String.valueOf(moduleSetting.get());
        }
        String str10 = strValueOf;
        arrayList = new ArrayList();
        if (moduleSetting instanceof ModuleSetting.Color) {
          ModuleSetting.Color color7 = (ModuleSetting.Color) moduleSetting;
          arrayList.add(
              ComponentBoxService.node(
                  "material-color-swatch",
                  MaterialColorService::new,
                  materialColorService7 -> {
                    materialColorService7
                        .color(color7.get().intValue())
                        .radius(Float.intBitsToFloat(1086324736))
                        .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                        .flexShrink(0.0f)
                        .pointerEvents(false);
                  },
                  new ComponentKeyService[0]));
        }
        if (zIsActive) {
          iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv = calculateValue();
        }
        arrayList.add(
            createComponentKeyService25(str10, iMdnv1ub7rbpv)
                .props(
                    sceneTextService8 -> {
                      sceneTextService8.flex(1.0f).minWidth(0.0f);
                    }));
        if (moduleSetting instanceof ModuleSetting.Curve) {
          str2 = "tune";
        } else {
          str2 = "expand_more";
        }
        if (zIsActive) {
          iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
        } else {
          iMdnv1ub7rbpv2 = calculateValue();
        }
        arrayList.add(
            MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
                .props(
                    sceneSrcService7 -> {
                      sceneSrcService7.size(
                          Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
        componentKeyServiceKey =
            ComponentBoxService.node(
                    "setting-choice",
                    MaterialJoinedService::new,
                    materialJoinedService7 -> {
                      materialJoinedService7
                          .colors(
                              MaterialIsLightService.SECONDARY_CONTAINER,
                              MaterialIsLightService.ON_SECONDARY_CONTAINER)
                          .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                          .available(zIsActive);
                      materialJoinedService7
                          .id(str3 + ".edit")
                          .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                          .padding(0.0f, Float.intBitsToFloat(1094713344))
                          .gap(Float.intBitsToFloat(1090519040))
                          .direction(ScenePctService.Direction.ROW)
                          .align(ScenePctService.Align.CENTER)
                          .onClick(
                              () -> {
                                updateState17(moduleSetting);
                              });
                    },
                    (ComponentKeyService[])
                        arrayList.toArray(
                            i7 -> {
                              return new ComponentKeyService[i7];
                            }))
                .key("control");
      }
    } else if (moduleSetting instanceof ModuleLayoutService) {
      ModuleLayoutService moduleLayoutService8 = (ModuleLayoutService) moduleSetting;
      componentKeyServiceKey =
          InventoryRenderer.preview(
              str3,
              moduleLayoutService8,
              () -> {
                updateState17(moduleLayoutService8);
              });
    } else if (moduleSetting instanceof ModuleSetting.Text) {
      ModuleSetting.Text text8 = (ModuleSetting.Text) moduleSetting;
      componentKeyServiceKey =
          ComponentBoxService.node(
                  "setting-text",
                  ControlLetterSpacingService::new,
                  controlLetterSpacingService8 -> {
                    MaterialTextService.input(controlLetterSpacingService8);
                    controlLetterSpacingService8
                        .fontSize(Float.intBitsToFloat(1097859072))
                        .letterSpacing(Float.intBitsToFloat(1036831949))
                        .id(str3 + ".text-input")
                        .maxLength(text8.maxLength())
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                        .minWidth(0.0f)
                        .interactive(zIsActive)
                        .pointerEvents(zIsActive);
                    if (!controlLetterSpacingService8.focused()) {
                      controlLetterSpacingService8.text(text8.get());
                    }
                    Objects.requireNonNull(text8);
                    controlLetterSpacingService8
                        .onSubmit(text8::set)
                        .onUnfocus(
                            () -> {
                              if (text8.isActive()) {
                                text8.set(controlLetterSpacingService8.text());
                              }
                            });
                  },
                  new ComponentKeyService[0])
              .key("control");
    } else if (moduleSetting instanceof ModuleSetting.Keybind) {
      ModuleSetting.Keybind keybind8 = (ModuleSetting.Keybind) moduleSetting;
      componentKeyServiceKey =
          ComponentBoxService.node(
                  "setting-keybind",
                  ControlGetAnimPropertyService::new,
                  controlGetAnimPropertyService8 -> {
                    ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents =
                        controlGetAnimPropertyService8
                            .material(true)
                            .keyNameFn(
                                (v0) -> {
                                  return createText8(v0);
                                })
                            .keyCode(keybind8.get().intValue())
                            .fontSize(Float.intBitsToFloat(1096810496))
                            .cornerRadius(Float.intBitsToFloat(1094713344))
                            .bgColor(MaterialIsLightService.SECONDARY_CONTAINER)
                            .textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER)
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .id(str3 + ".keybind")
                            .interactive(zIsActive)
                            .pointerEvents(zIsActive);
                    Objects.requireNonNull(keybind8);
                    controlGetAnimPropertyServicePointerEvents
                        .onChange(
                            (v1) -> {
                              keybind8.set(v1);
                            })
                        .chooser(
                            () -> {
                              updateState14(keybind8);
                            })
                        .tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                  },
                  new ComponentKeyService[0])
              .key("control");
    } else {
      if (moduleSetting instanceof ModuleEntriesService) {
        ModuleEntriesService moduleEntriesService8 = (ModuleEntriesService) moduleSetting;
        strValueOf =
            moduleEntriesService8.selected().edition()
                + " / "
                + moduleEntriesService8.selected().label();
      } else if (moduleSetting instanceof ModuleSetting.Mode) {
        strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
      } else if (moduleSetting instanceof ModuleSetting.Color) {
        strValueOf =
            String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
      } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
        ModuleSetting.MultiSelect multiSelect9 = (ModuleSetting.MultiSelect) moduleSetting;
        strValueOf =
            multiSelect9.get().size() + " of " + multiSelect9.options().length + " selected";
      } else if (moduleSetting instanceof ModuleSetting.Curve) {
        strValueOf = createText7(((ModuleSetting.Curve) moduleSetting).get().type());
      } else {
        strValueOf = String.valueOf(moduleSetting.get());
      }
      String str11 = strValueOf;
      arrayList = new ArrayList();
      if (moduleSetting instanceof ModuleSetting.Color) {
        ModuleSetting.Color color8 = (ModuleSetting.Color) moduleSetting;
        arrayList.add(
            ComponentBoxService.node(
                "material-color-swatch",
                MaterialColorService::new,
                materialColorService8 -> {
                  materialColorService8
                      .color(color8.get().intValue())
                      .radius(Float.intBitsToFloat(1086324736))
                      .size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376))
                      .flexShrink(0.0f)
                      .pointerEvents(false);
                },
                new ComponentKeyService[0]));
      }
      if (zIsActive) {
        iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
      } else {
        iMdnv1ub7rbpv = calculateValue();
      }
      arrayList.add(
          createComponentKeyService25(str11, iMdnv1ub7rbpv)
              .props(
                  sceneTextService9 -> {
                    sceneTextService9.flex(1.0f).minWidth(0.0f);
                  }));
      if (moduleSetting instanceof ModuleSetting.Curve) {
        str2 = "tune";
      } else {
        str2 = "expand_more";
      }
      if (zIsActive) {
        iMdnv1ub7rbpv2 = MaterialIsLightService.ON_SECONDARY_CONTAINER;
      } else {
        iMdnv1ub7rbpv2 = calculateValue();
      }
      arrayList.add(
          MaterialTextService.icon(str2, iMdnv1ub7rbpv2)
              .props(
                  sceneSrcService8 -> {
                    sceneSrcService8.size(
                        Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                  }));
      componentKeyServiceKey =
          ComponentBoxService.node(
                  "setting-choice",
                  MaterialJoinedService::new,
                  materialJoinedService8 -> {
                    materialJoinedService8
                        .colors(
                            MaterialIsLightService.SECONDARY_CONTAINER,
                            MaterialIsLightService.ON_SECONDARY_CONTAINER)
                        .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
                        .available(zIsActive);
                    materialJoinedService8
                        .id(str3 + ".edit")
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                        .padding(0.0f, Float.intBitsToFloat(1094713344))
                        .gap(Float.intBitsToFloat(1090519040))
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .onClick(
                            () -> {
                              updateState17(moduleSetting);
                            });
                  },
                  (ComponentKeyService[])
                      arrayList.toArray(
                          i8 -> {
                            return new ComponentKeyService[i8];
                          }))
              .key("control");
    }
    arrayList2.add(
        componentKeyServiceKey.props(
            scenePctService2 -> {
              if (moduleSetting instanceof ModuleSetting.Bool) {
                return;
              }
              scenePctService2
                  .width(
                      z3
                          ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))
                          : LayoutOperationHandler.auto())
                  .flex(z3 ? 0.0f : 1.0f)
                  .minWidth(0.0f);
            }));
    ArrayList arrayList3 = new ArrayList();
    arrayList3.add(
        ComponentBoxService.box(
                layoutContainerNode3 -> {
                  layoutContainerNode3
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .direction(
                          z3 ? ScenePctService.Direction.COLUMN : ScenePctService.Direction.ROW)
                      .gap(z3 ? Float.intBitsToFloat(1094713344) : Float.intBitsToFloat(1101004800))
                      .align(z3 ? ScenePctService.Align.STRETCH : ScenePctService.Align.CENTER);
                },
                (ComponentKeyService[])
                    arrayList2.toArray(
                        i9 -> {
                          return new ComponentKeyService[i9];
                        }))
            .key("main"));
    if (this.enabled2 && !moduleSetting.description().isBlank()) {
      arrayList3.add(
          MaterialTextService.text(
                  moduleSetting.description(),
                  Float.intBitsToFloat(1095761920),
                  zIsActive ? MaterialIsLightService.ON_SURFACE_VARIANT : calculateValue())
              .props(
                  sceneTextService10 -> {
                    sceneTextService10
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .lineHeight(Float.intBitsToFloat(1101004800))
                        .wordWrap(true);
                  })
              .key("help"));
    }
    return ComponentBoxService.node(
            "material-setting-surface",
            MaterialResponsiveService::new,
            materialResponsiveService -> {
              Runnable runnable;
              SceneCornerRadiusService sceneCornerRadiusService =
                  materialResponsiveService
                      .surface(MaterialIsLightService.SURFACE_CONTAINER)
                      .responsive(zIsActive)
                      .id(str3)
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .minHeight(Float.intBitsToFloat(1116733440))
                      .flexShrink(0.0f)
                      .direction(ScenePctService.Direction.COLUMN)
                      .padding(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1099956224))
                      .gap(Float.intBitsToFloat(1092616192))
                      .cornerRadiusTL(
                          z ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040))
                      .cornerRadiusTR(
                          z ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040))
                      .cornerRadiusBR(
                          z2 ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040))
                      .cornerRadiusBL(
                          z2 ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040))
                      .tooltip(
                          moduleSetting.name()
                              + (moduleSetting.description().isBlank()
                                  ? ""
                                  : " · " + moduleSetting.description()));
              if (moduleSetting instanceof ModuleSetting.Bool) {
                ModuleSetting.Bool bool2 = (ModuleSetting.Bool) moduleSetting;
                if (zIsActive) {
                  runnable =
                      () -> {
                        bool2.toggle();
                        mgf9hldmchqz();
                      };
                } else {
                  runnable = null;
                }
              } else {
                runnable = null;
              }
              sceneCornerRadiusService
                  .onClick(runnable)
                  .interactive(zIsActive)
                  .cursorStyle(
                      ((moduleSetting instanceof ModuleSetting.Bool) && zIsActive)
                          ? ScenePctService.CursorStyle.POINTER
                          : ScenePctService.CursorStyle.DEFAULT);
            },
            (ComponentKeyService[])
                arrayList3.toArray(
                    i10 -> {
                      return new ComponentKeyService[i10];
                    }))
        .key(str3);
  }

  private static boolean checkCondition2(ModuleSetting.Number number) {
    return number.step() >= 1.0f
        && ((double) number.step()) == Math.rint((double) number.step())
        && ((double) number.min()) == Math.rint((double) number.min())
        && (number.max() - number.min()) / number.step() <= Float.intBitsToFloat(1094713344);
  }

  private ComponentKeyService<?> createComponentKeyService17(
      String str, ModuleSetting.Number number) {
    return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .id(str + ".counter")
                      .justify(ScenePctService.Justify.END)
                      .gap(2.0f)
                      .align(ScenePctService.Align.CENTER);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  createComponentKeyService26(
                          str + ".decrement",
                          "−",
                          () -> {
                            if (number.isActive()) {
                              number.set(Float.valueOf(number.get().floatValue() - number.step()));
                              mgf9hldmchqz();
                            }
                          },
                          false)
                      .props(
                          materialJoinedService -> {
                            materialJoinedService
                                .available(
                                    number.isActive() && number.get().floatValue() > number.min())
                                .minWidth(Float.intBitsToFloat(1107296256))
                                .padding(0.0f, Float.intBitsToFloat(1090519040))
                                .tooltip(
                                    "Decrease by "
                                        + createText6(number.step(), number.step(), 0.0f));
                          }),
                  createComponentKeyService18(
                      str,
                      createText6(number.get().floatValue(), number.step(), number.min()),
                      number),
                  createComponentKeyService26(
                          str + ".increment",
                          "+",
                          () -> {
                            if (number.isActive()) {
                              number.set(Float.valueOf(number.get().floatValue() + number.step()));
                              mgf9hldmchqz();
                            }
                          },
                          false)
                      .props(
                          materialJoinedService2 -> {
                            materialJoinedService2
                                .available(
                                    number.isActive() && number.get().floatValue() < number.max())
                                .minWidth(Float.intBitsToFloat(1107296256))
                                .padding(0.0f, Float.intBitsToFloat(1090519040))
                                .tooltip(
                                    "Increase by "
                                        + createText6(number.step(), number.step(), 0.0f));
                          })
                })
        .key("control");
  }

  private ComponentKeyService<?> createComponentKeyService18(
      String str, String str2, ModuleSetting<?> moduleSetting) {
    int length;
    if (moduleSetting instanceof ModuleSetting.Number) {
      ModuleSetting.Number number = (ModuleSetting.Number) moduleSetting;
      length = BuiltinRangeService.maxCharacters(number.min(), number.max(), number.step());
    } else if (moduleSetting instanceof ModuleSetting.Range) {
      ModuleSetting.Range range = (ModuleSetting.Range) moduleSetting;
      length = (2 * BuiltinRangeService.maxCharacters(range.min(), range.max(), range.step())) + 3;
    } else {
      length = str2.length();
    }
    float fMax = Math.max(56, (length * 8) + 24);
    Supplier supplier = MaterialJoinedService::new;
    Consumer<MaterialJoinedService> consumer =
        materialJoinedService -> {
          materialJoinedService
              .colors(MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.PRIMARY)
              .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832))
              .available(moduleSetting.isActive());
          materialJoinedService
              .id(str + ".exact")
              .size(
                  LayoutOperationHandler.px(fMax),
                  LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
              .minWidth(fMax)
              .padding(0.0f, Float.intBitsToFloat(1094713344))
              .flexShrink(0.0f)
              .direction(ScenePctService.Direction.ROW)
              .align(ScenePctService.Align.CENTER)
              .justify(ScenePctService.Justify.CENTER)
              .tooltip("Enter an exact value")
              .onClick(
                  () -> {
                    updateState17(moduleSetting);
                  });
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[1];
    componentKeyServiceArr[0] =
        createComponentKeyService25(
                str2, moduleSetting.isActive() ? MaterialIsLightService.PRIMARY : calculateValue())
            .props(
                sceneTextService -> {
                  sceneTextService.id(str + ".value");
                });
    return ComponentBoxService.node("material-value", supplier, consumer, componentKeyServiceArr)
        .key("value");
  }

  private ComponentKeyService<?> createComponentKeyService19(String str, ModuleSetting.Mode mode) {
    ArrayList arrayList = new ArrayList();
    String[] strArrOptions = mode.options();
    int length = strArrOptions.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= length) {
        break;
      }
      String str2 = strArrOptions[i2];
      boolean zEquals = mode.get().equals(str2);
      Supplier supplier = MaterialJoinedService::new;
      Consumer<MaterialJoinedService> consumer =
          materialJoinedService -> {
            materialJoinedService
                .colors(
                    zEquals
                        ? MaterialIsLightService.PRIMARY_CONTAINER
                        : MaterialIsLightService.SURFACE_HIGH,
                    MaterialIsLightService.ON_SURFACE)
                .shape(
                    zEquals ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1092616192),
                    Float.intBitsToFloat(1113587712))
                .available(mode.isActive());
            materialJoinedService
                .id(str + ".option." + createText(str2))
                .height(LayoutOperationHandler.px(Float.intBitsToFloat(1114636288)))
                .flex(1.0f)
                .minWidth(0.0f)
                .padding(Float.intBitsToFloat(1090519040))
                .gap(Float.intBitsToFloat(1086324736))
                .direction(ScenePctService.Direction.COLUMN)
                .justify(ScenePctService.Justify.CENTER)
                .tooltip(str2 + " palette")
                .onClick(
                    () -> {
                      if (mode.isActive()) {
                        mode.set(str2);
                        mgf9hldmchqz();
                      }
                    });
          };
      ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
      componentKeyServiceArr[0] =
          ComponentBoxService.node(
                  "material-palette-motif",
                  MaterialPaletteService::new,
                  materialPaletteService -> {
                    materialPaletteService
                        .palette(mode.previewColors(str2), zEquals)
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1098907648)))
                        .flexShrink(0.0f);
                  },
                  new ComponentKeyService[0])
              .key("motif");
      componentKeyServiceArr[1] =
          createComponentKeyService25(
                  str2,
                  zEquals
                      ? MaterialIsLightService.ON_PRIMARY_CONTAINER
                      : MaterialIsLightService.ON_SURFACE)
              .props(
                  sceneTextService -> {
                    sceneTextService
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .textAlign(SceneTextService.TextAlign.CENTER);
                  });
      arrayList.add(
          ComponentBoxService.node(
                  "material-palette-option", supplier, consumer, componentKeyServiceArr)
              .key(str2));
      i = i2 + 1;
    }
    ArrayList arrayList2 = new ArrayList();
    int i3 = (!this.enabled4 || this.value6 >= Float.intBitsToFloat(1139802112)) ? 4 : 2;
    int i4 = 0;
    while (true) {
      int i5 = i4;
      if (i5 >= arrayList.size()) {
        return ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .id(str + ".palettes")
                          .gap(Float.intBitsToFloat(1082130432))
                          .minWidth(0.0f);
                    },
                (ComponentKeyService<?>[])
                    arrayList2.toArray(
                        i6 -> {
                          return new ComponentKeyService[i6];
                        }))
            .key("control");
      }
      arrayList2.add(
          ComponentBoxService.row(
                  (Consumer<LayoutContainerNode>)
                      layoutContainerNode2 -> {
                        layoutContainerNode2
                            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .gap(Float.intBitsToFloat(1082130432));
                      },
                  (ComponentKeyService<?>[])
                      arrayList
                          .subList(i5, Math.min(i5 + i3, arrayList.size()))
                          .toArray(
                              i7 -> {
                                return new ComponentKeyService[i7];
                              }))
              .key("row:" + i5));
      i4 = i5 + i3;
    }
  }

  private static boolean checkCondition3(String[] strArr, boolean z) {
    if (strArr.length < 2 || strArr.length > 4) {
      return false;
    }
    int i = (z || strArr.length == 3) ? 8 : 12;
    return Arrays.stream(strArr)
        .allMatch(
            str -> {
              return str.length() <= i;
            });
  }

  private ComponentKeyService<?> createComponentKeyService20(
      String str, ModuleSetting<?> moduleSetting, String[] strArr, boolean z) {
    int iMdnv1ub7rbpv;
    ArrayList arrayList = new ArrayList();
    int length = strArr.length == 4 ? 2 : strArr.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= strArr.length) {
        return ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .id(str + ".options")
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(2.0f)
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    arrayList.toArray(
                        i3 -> {
                          return new ComponentKeyService[i3];
                        }))
            .key("control");
      }
      ArrayList arrayList2 = new ArrayList();
      int i4 = i2;
      while (true) {
        int i5 = i4;
        if (i5 < Math.min(i2 + length, strArr.length)) {
          String str2 = strArr[i5];
          boolean zEquals =
              moduleSetting instanceof ModuleSetting.Mode
                  ? ((ModuleSetting.Mode) moduleSetting).get().equals(str2)
                  : ((ModuleSetting.MultiSelect) moduleSetting).get().contains(str2);
          boolean z2 = i5 == i2;
          boolean z3 = i5 == Math.min(i2 + length, strArr.length) - 1;
          if (moduleSetting.isActive()) {
            iMdnv1ub7rbpv =
                zEquals ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE;
          } else {
            iMdnv1ub7rbpv = calculateValue();
          }
          int i6 = iMdnv1ub7rbpv;
          ArrayList arrayList3 = new ArrayList();
          if (z) {
            arrayList3.add(
                MaterialTextService.icon(zEquals ? "check" : "add", i6)
                    .props(
                        sceneSrcService -> {
                          sceneSrcService.size(
                              Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                        }));
          }
          arrayList3.add(
              createComponentKeyService25(str2, i6)
                  .props(
                      sceneTextService -> {
                        sceneTextService.minWidth(0.0f).flexShrink(1.0f);
                      }));
          arrayList2.add(
              ComponentBoxService.node(
                      "material-inline-option",
                      MaterialJoinedService::new,
                      materialJoinedService -> {
                        materialJoinedService
                            .colors(
                                zEquals
                                    ? MaterialIsLightService.PRIMARY
                                    : MaterialIsLightService.SURFACE_HIGH,
                                i6)
                            .shape(
                                Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832))
                            .joined(z2, z3, zEquals)
                            .available(moduleSetting.isActive());
                        materialJoinedService
                            .id(str + ".option." + createText(str2))
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                            .flex(1.0f)
                            .minWidth(0.0f)
                            .padding(0.0f, Float.intBitsToFloat(1092616192))
                            .gap(Float.intBitsToFloat(1086324736))
                            .direction(ScenePctService.Direction.ROW)
                            .align(ScenePctService.Align.CENTER)
                            .justify(ScenePctService.Justify.CENTER)
                            .tooltip(str2)
                            .onClick(
                                () -> {
                                  if (moduleSetting.isActive()) {
                                    if (moduleSetting instanceof ModuleSetting.Mode) {
                                      ((ModuleSetting.Mode) moduleSetting).set(str2);
                                    } else {
                                      ModuleSetting.MultiSelect multiSelect =
                                          (ModuleSetting.MultiSelect) moduleSetting;
                                      LinkedHashSet linkedHashSet =
                                          new LinkedHashSet(multiSelect.get());
                                      if (!linkedHashSet.remove(str2)) {
                                        linkedHashSet.add(str2);
                                      }
                                      multiSelect.set(linkedHashSet);
                                    }
                                    mgf9hldmchqz();
                                  }
                                });
                      },
                      (ComponentKeyService[])
                          arrayList3.toArray(
                              i7 -> {
                                return new ComponentKeyService[i7];
                              }))
                  .key(str2));
          i4 = i5 + 1;
        } else {
          break;
        }
      }
      arrayList.add(
          ComponentBoxService.row(
                  (Consumer<LayoutContainerNode>)
                      layoutContainerNode2 -> {
                        layoutContainerNode2
                            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .gap(2.0f)
                            .flexShrink(0.0f);
                      },
                  (ComponentKeyService<?>[])
                      arrayList2.toArray(
                          i8 -> {
                            return new ComponentKeyService[i8];
                          }))
              .key("row:" + i2));
      i = i2 + length;
    }
  }

  private void updateState4(String str, String str2) {
    if (this.materialTerrainTooltipsService != null) {
      ScenePctService<?> scenePctServiceFindById =
          this.materialTerrainTooltipsService.findById(str + ".value");
      if (scenePctServiceFindById instanceof SceneTextService) {
        ((SceneTextService) scenePctServiceFindById).text(str2);
      }
    }
  }

  private static int calculateValue() {
    return MaterialIsLightService.layer(
        MaterialIsLightService.SURFACE_CONTAINER,
        MaterialIsLightService.ON_SURFACE,
        Float.intBitsToFloat(1052938076));
  }

  private void updateState5(Module module) {
    updateState27(this.componentMountService);
    if (this.moduleOperationHandler2 != module) {
      this.text6 = "";
      this.text5 = "";
    }
    this.moduleOperationHandler2 = module;
    this.enabled6 = true;
    updateState22();
    mfahyvg0apdz();
    mgf9hldmchqz();
  }

  private void updateState6(ModuleFeatureType moduleFeatureType) {
    if (this.moduleFeatureType2 == moduleFeatureType && this.text4.isEmpty() && !this.enabled6) {
      return;
    }
    int iCompare =
        Integer.compare(
            Arrays.asList(fdivljsc8kbu).indexOf(moduleFeatureType),
            Arrays.asList(fdivljsc8kbu).indexOf(this.moduleFeatureType2));
    this.fahprko4fthe = mezavdvwfusb();
    this.count2 = iCompare;
    this.fgcmhfutlzbp++;
    this.moduleFeatureType2 = moduleFeatureType;
    this.text4 = "";
    this.text5 = "";
    this.text6 = "";
    this.enabled6 = false;
    updateState22();
    mfahyvg0apdz();
    ControlLetterSpacingService controlLetterSpacingService =
        (ControlLetterSpacingService)
            this.materialTerrainTooltipsService.findById("clickgui.toolbar.search");
    if (controlLetterSpacingService != null) {
      controlLetterSpacingService.text("");
    }
    this.moduleOperationHandler2 = collectValues2().stream().findFirst().orElse(null);
    mgf9hldmchqz();
    MaterialEnterService.reveal(
        this.materialTerrainTooltipsService.findById("clickgui.content"), iCompare * 24, 0.0f);
  }

  private void updateState7() {
    List<Module> listM4k38isfutk5 = collectValues2();
    if (listM4k38isfutk5.contains(this.moduleOperationHandler2)) {
      return;
    }
    this.text5 = "";
    this.text6 = "";
    this.moduleOperationHandler2 = listM4k38isfutk5.stream().findFirst().orElse(null);
    updateState22();
  }

  private void mgf9hldmchqz() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }

  private void updateState9() {
    if (this.screenScreenIdService != null) {
      this.screenScreenIdService.close();
    }
  }

  private void mkdxr04hjju() {
    updateState22();
    this.enabled6 = false;
    mfahyvg0apdz();
    mgf9hldmchqz();
    MaterialEnterService.enter(
        this.materialTerrainTooltipsService.findById("clickgui.modules"),
        Float.intBitsToFloat(-1047527424),
        0.0f);
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenIdService) {
    if (ThemeCornerData.current().focusSearch()) {
      ScenePctService<?> scenePctServiceFindById =
          this.materialTerrainTooltipsService.findById("clickgui.toolbar.search");
      if (scenePctServiceFindById instanceof ControlLetterSpacingService) {
        ControlLetterSpacingService controlLetterSpacingService =
            (ControlLetterSpacingService) scenePctServiceFindById;
        maqwjkcdabqp(controlLetterSpacingService);
        controlLetterSpacingService.selectAll();
      }
    }
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    updateState11();
    this.fahprko4fthe = null;
    this.fgcmhfutlzbp++;
    updateState22();
    mfahyvg0apdz();
    updateState27(this.componentMountService);
    mgf9hldmchqz();
  }

  @Override
  public void onSuspend(ScreenScreenIdService screenScreenIdService) {
    updateState11();
    mfahyvg0apdz();
    updateState27(this.componentMountService);
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    if (this.materialTerrainTooltipsService == null) {
      return;
    }
    viewport(
        this.materialTerrainTooltipsService.computedW(),
        this.materialTerrainTooltipsService.computedH());
  }

  private void updateState11() {
    if (ThemeCornerData.current().rememberClickGui()) {
      HashMap map = new HashMap(this.fjlbgzdyzws9);
      for (String str : f89n839yrbtw) {
        ScenePctService<?> scenePctServiceFindById =
            this.materialTerrainTooltipsService == null
                ? null
                : this.materialTerrainTooltipsService.findById(str);
        if (scenePctServiceFindById != null
            && scenePctServiceFindById.computedW() > 0.0f
            && scenePctServiceFindById.computedH() > 0.0f
            && !this.fjlbgzdyzws9.containsKey(str)) {
          map.put(
              str,
              Float.valueOf(
                  Math.max(
                      scenePctServiceFindById.scrollMin(),
                      Math.min(0.0f, scenePctServiceFindById.scrollY()))));
        }
      }
      this.builtinRepository.save(
          new BuiltinRepository.State(
              this.moduleFeatureType2.name(),
              this.moduleOperationHandler2 == null
                  ? ""
                  : m1vcviafemyn(this.moduleOperationHandler2),
              this.text4,
              this.text5,
              this.text6,
              this.enabled3,
              this.enabled6,
              this.enabled2,
              this.text7,
              this.text8,
              this.fcijmdt1zfva,
              map));
    }
  }

  private void updateState12() {
    if (Math.abs(this.value6 - this.materialTerrainTooltipsService.computedW())
            > Float.intBitsToFloat(1056964608)
        || Math.abs(this.value5 - this.materialTerrainTooltipsService.computedH())
            > Float.intBitsToFloat(1056964608)) {
      viewport(
          this.materialTerrainTooltipsService.computedW(),
          this.materialTerrainTooltipsService.computedH());
      return;
    }
    Iterator<Map.Entry<String, Float>> it = this.fjlbgzdyzws9.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Float> next = it.next();
      ScenePctService<?> scenePctServiceFindById =
          this.materialTerrainTooltipsService.findById(next.getKey());
      if (scenePctServiceFindById != null
          && scenePctServiceFindById.computedW() > 0.0f
          && scenePctServiceFindById.computedH() > 0.0f) {
        float fMax =
            Math.max(
                scenePctServiceFindById.scrollMin(), Math.min(0.0f, next.getValue().floatValue()));
        MotionAnimateService.cancel(scenePctServiceFindById, MotionColorsContainer.Floats.SCROLL_Y);
        scenePctServiceFindById.scrollY(fMax).scrollTarget(fMax);
        it.remove();
      }
    }
  }

  public void viewport(float f, float f2) {
    if (this.materialTerrainTooltipsService == null) {
      return;
    }
    boolean z = f > 0.0f && f < Float.intBitsToFloat(1146224640);
    boolean z2 = f > 0.0f && f < Float.intBitsToFloat(1142292480);
    boolean z3 =
        (f2 > 0.0f && Math.abs(this.value5 - f2) > Float.intBitsToFloat(1056964608))
            || (f > 0.0f && Math.abs(this.value6 - f) > Float.intBitsToFloat(1056964608));
    if (f2 > 0.0f) {
      this.value5 = f2;
    }
    if (f > 0.0f) {
      this.value6 = f;
    }
    boolean z4 = (z == this.enabled4 && z2 == this.ftzcjcss2ew) ? false : true;
    this.enabled4 = z;
    this.ftzcjcss2ew = z2;
    int iMj6gzbc3kwej = calculateValue2();
    if (this.renderer != null && iMj6gzbc3kwej != this.count) {
      this.renderer.refresh();
    }
    if (z4 || z3 || iMj6gzbc3kwej != this.count) {
      this.count = iMj6gzbc3kwej;
      updateState7();
      mgf9hldmchqz();
    }
    if (this.moduleNameService2 != null
        && (!this.moduleNameService2.isVisible() || !this.moduleNameService2.isActive())) {
      updateState22();
    }
    if (this.renderer != null) {
      if (this.renderer.selection().target().available().getAsBoolean()) {
        this.renderer.viewport(this.value6, this.value5);
        return;
      } else {
        manpywc7uulo();
        return;
      }
    }
    if (this.componentMountService2 == null || !z3) {
      return;
    }
    this.componentMountService2.invalidateComponent();
  }

  private int calculateValue2() {
    int iHashCode = createModuleBindService().all().hashCode();
    Iterator<List<Module>> it = this.fikxlao3mld.values().iterator();
    while (it.hasNext()) {
      for (Module module : it.next()) {
        iHashCode = (31 * iHashCode) + Boolean.hashCode(module.isEnabled());
        for (ModuleSetting<?> moduleSetting : module.settings()) {
          if (moduleSetting instanceof ModuleSetting.Keybind) {
            iHashCode = (31 * iHashCode) + Objects.hashCode(moduleSetting.get());
          }
        }
        if (module == this.moduleOperationHandler2) {
          for (ModuleSetting<?> moduleSetting2 : module.settings()) {
            iHashCode =
                (31
                        * ((31 * ((31 * iHashCode) + Objects.hashCode(moduleSetting2.get())))
                            + Boolean.hashCode(moduleSetting2.isVisible())))
                    + Boolean.hashCode(moduleSetting2.isActive());
          }
        }
      }
    }
    return iHashCode;
  }

  public boolean capturesKeybindInput() {
    return this.renderer != null;
  }

  private ModuleBindService createModuleBindService() {
    return CoreIsInitializedHandler.isReady()
        ? CoreIsInitializedHandler.get().keybinds()
        : this.moduleBindService;
  }

  private List<KeybindTargetService.Target> collectValues3() {
    ArrayList arrayList = new ArrayList();
    Iterator<List<Module>> it = this.fikxlao3mld.values().iterator();
    while (it.hasNext()) {
      for (Module module : it.next()) {
        if (module.category() != ModuleFeatureType.DEVELOPMENT
            || this.moduleFeatureType2 == ModuleFeatureType.DEVELOPMENT) {
          arrayList.add(createTarget(module));
          for (ModuleSetting<?> moduleSetting : module.settings()) {
            if (moduleSetting instanceof ModuleSetting.Keybind) {
              arrayList.add(createTarget2(module, (ModuleSetting.Keybind) moduleSetting));
            }
          }
        }
      }
    }
    return arrayList;
  }

  private KeybindTargetService.Target createTarget(Module module) {
    return new KeybindTargetService.Target(
        m7sclicozxcc(module) + "/toggle",
        "Toggle module",
        module.name(),
        () -> {
          return createModuleBindService().getKey(module.name());
        },
        i -> {
          if (i < 0) {
            createModuleBindService().unbind(module.name());
          } else {
            createModuleBindService().bind(i, module.name());
          }
          if (!CoreIsInitializedHandler.isReady()
              || CoreIsInitializedHandler.get().config() == null) {
            return;
          }
          CoreIsInitializedHandler.get().config().save();
        },
        () -> {
          return true;
        },
        true);
  }

  private KeybindTargetService.Target createTarget2(Module module, ModuleSetting.Keybind keybind) {
    String str =
        m7sclicozxcc(module)
            + "/"
            + ((String)
                keybind
                    .category()
                    .map(
                        (v0) -> {
                          return v0.qualifiedName();
                        })
                    .orElse("general"))
            + "/"
            + keybind.name();
    String strName = keybind.name();
    String strName2 = module.name();
    Objects.requireNonNull(keybind);
    IntSupplier intSupplier = keybind::get;
    Objects.requireNonNull(keybind);
    return new KeybindTargetService.Target(
        str,
        strName,
        strName2,
        intSupplier,
        (v1) -> {
          keybind.set(v1);
        },
        () -> {
          return keybind.isVisible() && keybind.isActive();
        },
        false);
  }

  private void updateState13() {
    if (this.moduleOperationHandler2 != null) {
      updateState15(createTarget(this.moduleOperationHandler2));
    }
  }

  private void updateState14(ModuleSetting.Keybind keybind) {
    if (this.moduleOperationHandler2 == null || !keybind.isActive()) {
      return;
    }
    updateState15(createTarget2(this.moduleOperationHandler2, keybind));
  }

  private void updateState15(KeybindTargetService.Target target) {
    updateState22();
    mfahyvg0apdz();
    updateState27(this.materialTerrainTooltipsService);
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
      CoreIsInitializedHandler.get().scene().clearFocus();
    }
    CompatRepository.Snapshot snapshot =
        CoreIsInitializedHandler.isReady()
            ? CompatRepository.read()
            : CompatRepository.Snapshot.EMPTY;
    this.renderer =
        new KeybindRenderer(
            new KeybindTargetService(target, collectValues3(), snapshot.uses(), snapshot.legends()),
            this::manpywc7uulo,
            () -> {
              manpywc7uulo();
              mgf9hldmchqz();
            });
    this.renderer.viewport(this.value6, this.value5);
    this.figehearx1so = new ComponentMountService().mount(this.renderer, this.values2);
    this.figehearx1so
        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
        .maxWidth(Float.intBitsToFloat(1150189568))
        .maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    this.componentMountService.pointerEvents(false);
    this.sceneComponent42 =
        ComponentUnmountService.mountCentered(
            this.materialTerrainTooltipsService,
            this.figehearx1so,
            this.ftzcjcss2ew ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1103101952),
            this::manpywc7uulo);
    this.sceneComponent42.id("clickgui.keybind-layer");
    ((SceneCornerRadiusService) this.sceneComponent42.children().getFirst())
        .backgroundColor(-1206973165);
    MaterialEnterService.enter(this.figehearx1so, 0.0f, Float.intBitsToFloat(1098907648));
  }

  private void manpywc7uulo() {
    if (this.figehearx1so != null) {
      this.figehearx1so.unmountComponent();
    }
    ComponentUnmountService.unmount(this.materialTerrainTooltipsService, this.sceneComponent42);
    this.sceneComponent42 = null;
    this.figehearx1so = null;
    this.renderer = null;
    if (this.componentMountService != null) {
      this.componentMountService.pointerEvents(true);
    }
  }

  private void updateState17(ModuleSetting<?> moduleSetting) {
    BuiltinRangeService builtinRangeService;
    float fIntBitsToFloat;
    if (moduleSetting.isActive()) {
      updateState22();
      mfahyvg0apdz();
      updateState27(this.materialTerrainTooltipsService);
      this.moduleNameService2 = moduleSetting;
      this.renderer3 =
          moduleSetting instanceof ModuleEntriesService
              ? new BuiltinRenderer(
                  (ModuleEntriesService) moduleSetting, this::updateState22, this::mgf9hldmchqz)
              : null;
      this.renderer2 =
          moduleSetting instanceof ModuleLayoutService
              ? new InventoryRenderer(((ModuleLayoutService) moduleSetting).layout())
              : null;
      this.builtinOriginalService =
          moduleSetting instanceof ModuleSetting.Color
              ? new BuiltinOriginalService(((ModuleSetting.Color) moduleSetting).get().intValue())
              : null;
      if (moduleSetting instanceof ModuleSetting.Number) {
        builtinRangeService = new BuiltinRangeService((ModuleSetting.Number) moduleSetting);
      } else {
        builtinRangeService =
            moduleSetting instanceof ModuleSetting.Range
                ? new BuiltinRangeService((ModuleSetting.Range) moduleSetting)
                : null;
      }
      this.fbknrfxp9co = builtinRangeService;
      if (moduleSetting instanceof ModuleSetting.Curve) {
        this.moduleNumber =
            new ModuleSetting.Number(
                "Steps",
                ((ModuleSetting.Curve) moduleSetting).get().steps(),
                1.0f,
                Float.intBitsToFloat(1120403456),
                1.0f);
      }
      this.componentMountService2 =
          new ComponentMountService().mount(this::createComponentKeyService21, this.values2);
      LayoutContainerNode layoutContainerNodeWidth =
          this.componentMountService2.width(
              LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
      if (this.renderer3 != null) {
        fIntBitsToFloat = Float.intBitsToFloat(1144913920);
      } else {
        fIntBitsToFloat =
            this.renderer2 != null
                ? Float.intBitsToFloat(1147207680)
                : Float.intBitsToFloat(1141637120);
      }
      layoutContainerNodeWidth
          .maxWidth(fIntBitsToFloat)
          .maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
      this.sceneComponent4 =
          ComponentUnmountService.mountCentered(
              this.materialTerrainTooltipsService,
              this.componentMountService2,
              this.ftzcjcss2ew
                  ? Float.intBitsToFloat(1090519040)
                  : Float.intBitsToFloat(1103101952),
              this::updateState22);
      this.sceneComponent4.id("clickgui.dialog-layer");
      ((SceneCornerRadiusService) this.sceneComponent4.children().getFirst())
          .backgroundColor(1375731712);
      MaterialEnterService.enter(
          this.componentMountService2.findById("clickgui.dialog"),
          0.0f,
          Float.intBitsToFloat(1099956224));
    }
  }

  private ComponentKeyService<?> createComponentKeyService21(
      ComponentThemeService componentThemeService) {
    boolean zIsEmpty;
    if (this.renderer3 != null) {
      this.renderer3.viewport(this.value6, this.value5);
      return this.renderer3.render(componentThemeService);
    }
    ModuleSetting<?> moduleSetting = this.moduleNameService2;
    ArrayList arrayList = new ArrayList();
    if (this.renderer2 != null) {
      this.renderer2.viewport(this.value6);
      arrayList.add(this.renderer2.render(componentThemeService));
    } else if (moduleSetting instanceof ModuleSetting.Mode) {
      ModuleSetting.Mode mode = (ModuleSetting.Mode) moduleSetting;
      String[] strArrOptions = mode.options();
      int length = strArrOptions.length;
      int i = 0;
      while (true) {
        int i2 = i;
        if (i2 >= length) {
          break;
        }
        String str = strArrOptions[i2];
        arrayList.add(
            createComponentKeyService22(
                "mode." + createText(str),
                str,
                mode.get().equals(str),
                false,
                () -> {
                  if (mode.isActive()) {
                    mode.set(str);
                  }
                  updateState22();
                  mgf9hldmchqz();
                }));
        i = i2 + 1;
      }
    } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
      ModuleSetting.MultiSelect multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
      String[] strArrOptions2 = multiSelect.options();
      int length2 = strArrOptions2.length;
      int i3 = 0;
      while (true) {
        int i4 = i3;
        if (i4 >= length2) {
          break;
        }
        String str2 = strArrOptions2[i4];
        arrayList.add(
            createComponentKeyService22(
                "multi." + createText(str2),
                str2,
                multiSelect.get().contains(str2),
                true,
                () -> {
                  if (multiSelect.isActive()) {
                    LinkedHashSet linkedHashSet = new LinkedHashSet(multiSelect.get());
                    if (!linkedHashSet.remove(str2)) {
                      linkedHashSet.add(str2);
                    }
                    multiSelect.set(linkedHashSet);
                    mgf9hldmchqz();
                    componentThemeService.invalidate();
                  }
                }));
        i3 = i4 + 1;
      }
    } else if (moduleSetting instanceof ModuleSetting.Color) {
      updateState18(arrayList, componentThemeService);
    } else if (moduleSetting instanceof ModuleSetting.Curve) {
      updateState21(arrayList, (ModuleSetting.Curve) moduleSetting, componentThemeService);
    } else if (this.fbknrfxp9co != null) {
      updateState19(arrayList, componentThemeService);
    }
    boolean z =
        (this.builtinOriginalService == null && this.fbknrfxp9co == null && this.renderer2 == null)
            ? false
            : true;
    if (this.builtinOriginalService != null) {
      zIsEmpty = this.builtinOriginalService.error().isEmpty();
    } else {
      zIsEmpty = this.fbknrfxp9co == null || this.fbknrfxp9co.error().isEmpty();
    }
    boolean z2 = zIsEmpty;
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService
              .id("clickgui.dialog")
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .maxHeight(
                  Math.max(
                      Float.intBitsToFloat(1123024896),
                      Math.min(
                          this.builtinOriginalService != null
                              ? Float.intBitsToFloat(1144258560)
                              : Float.intBitsToFloat(1142947840),
                          this.value5 - (this.ftzcjcss2ew ? 16 : 48))))
              .direction(ScenePctService.Direction.COLUMN)
              .padding(
                  this.value5 < Float.intBitsToFloat(1135869952)
                      ? Float.intBitsToFloat(1094713344)
                      : Float.intBitsToFloat(1101004800))
              .gap(
                  this.value5 < Float.intBitsToFloat(1135869952)
                      ? Float.intBitsToFloat(1090519040)
                      : Float.intBitsToFloat(1094713344))
              .cornerRadius(Float.intBitsToFloat(1103101952))
              .backgroundColor(MaterialIsLightService.SURFACE_HIGH)
              .clip(true);
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
    componentKeyServiceArr[0] =
        MaterialTextService.text(
                moduleSetting.displayName(),
                Float.intBitsToFloat(1101004800),
                MaterialIsLightService.ON_SURFACE)
            .props(
                sceneTextService -> {
                  sceneTextService.lineHeight(Float.intBitsToFloat(1103101952));
                })
            .props(
                sceneTextService2 -> {
                  sceneTextService2
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .flexShrink(0.0f);
                });
    componentKeyServiceArr[1] =
        ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .id("clickgui.dialog.body")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .minHeight(0.0f)
                      .flexShrink(1.0f)
                      .padding(0.0f, Float.intBitsToFloat(1086324736), 0.0f, 0.0f)
                      .gap(
                          ((moduleSetting instanceof ModuleSetting.Mode)
                                  || (moduleSetting instanceof ModuleSetting.MultiSelect))
                              ? 0.0f
                              : Float.intBitsToFloat(1092616192))
                      .scrollable(true)
                      .scrollbarColor(MaterialIsLightService.OUTLINE)
                      .scrollbarWidth(2.0f)
                      .clip(true);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i5 -> {
                      return new ComponentKeyService[i5];
                    }));
    Consumer<LayoutContainerNode> consumer2 =
        layoutContainerNode2 -> {
          layoutContainerNode2
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .justify(ScenePctService.Justify.END)
              .gap(Float.intBitsToFloat(1090519040))
              .flexShrink(0.0f);
        };
    ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
    componentKeyServiceArr2[0] =
        createComponentKeyService27(
            "clickgui.dialog.cancel",
            z ? "Cancel" : "Close",
            this::updateState22,
            0,
            MaterialIsLightService.PRIMARY,
            Float.intBitsToFloat(1101004800));
    componentKeyServiceArr2[1] =
        createComponentKeyService27(
                "clickgui.dialog.apply",
                z ? "Apply" : "Done",
                this::updateState20,
                MaterialIsLightService.PRIMARY,
                z2 ? MaterialIsLightService.ON_PRIMARY : calculateValue(),
                Float.intBitsToFloat(1101004800))
            .props(
                materialJoinedService -> {
                  materialJoinedService
                      .available(z2)
                      .visible(!(moduleSetting instanceof ModuleSetting.Mode));
                });
    componentKeyServiceArr[2] =
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>) consumer2,
            (ComponentKeyService<?>[]) componentKeyServiceArr2);
    return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("dialog");
  }

  private ComponentKeyService<?> createComponentKeyService22(
      String str, String str2, boolean z, boolean z2, Runnable runnable) {
    return ComponentBoxService.node(
            "material-choice",
            MaterialJoinedService::new,
            materialJoinedService -> {
              materialJoinedService
                  .colors(0, MaterialIsLightService.ON_SURFACE)
                  .shape(Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1111490560));
              materialJoinedService
                  .id("clickgui.dialog." + str)
                  .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                  .padding(0.0f, Float.intBitsToFloat(1094713344))
                  .gap(Float.intBitsToFloat(1098907648))
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .flexShrink(0.0f)
                  .onClick(runnable);
            },
            ComponentBoxService.node(
                    "material-selection",
                    MaterialCheckboxValidator::new,
                    materialCheckboxValidator -> {
                      materialCheckboxValidator.checkbox(z2).selected(z);
                    },
                    new ComponentKeyService[0])
                .key("selection"),
            MaterialTextService.text(
                    str2, Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE)
                .props(
                    sceneTextService -> {
                      sceneTextService.flex(1.0f).minWidth(0.0f);
                    }))
        .key(str);
  }

  private void updateState18(
      List<ComponentKeyService<?>> list, ComponentThemeService componentThemeService) {
    BuiltinOriginalService builtinOriginalService = this.builtinOriginalService;
    boolean z = this.fcsyblho83aq == ColorFeatureType.TRIANGLE;
    list.add(
        MaterialTabsService.tabs(
            "clickgui.dialog.color-mode",
            this.fcsyblho83aq,
            colorFeatureType -> {
              this.fcsyblho83aq = colorFeatureType;
              componentThemeService.invalidate();
            }));
    list.add(
        ComponentBoxService.row(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .id("clickgui.dialog.color-canvas")
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .height(
                              LayoutOperationHandler.px(
                                  z
                                      ? Float.intBitsToFloat(1131413504)
                                      : Float.intBitsToFloat(1127219200)))
                          .gap(Float.intBitsToFloat(1098907648))
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      z
                          ? ComponentBoxService.node(
                                  "color-triangle",
                                  HueTrianglePicker::new,
                                  hueTrianglePicker -> {
                                    hueTrianglePicker
                                        .id("clickgui.dialog.color-triangle")
                                        .hue(builtinOriginalService.hue())
                                        .sat(builtinOriginalService.saturation())
                                        .bri(builtinOriginalService.brightness())
                                        .height(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)))
                                        .flex(1.0f)
                                        .minWidth(0.0f)
                                        .onHueChange(
                                            f -> {
                                              builtinOriginalService.hue(f.floatValue());
                                              componentThemeService.invalidate();
                                            })
                                        .onSBChange(
                                            (f2, f3) -> {
                                              builtinOriginalService.sv(
                                                  f2.floatValue(), f3.floatValue());
                                              componentThemeService.invalidate();
                                            });
                                  },
                                  new ComponentKeyService[0])
                              .key("triangle")
                          : ComponentBoxService.node(
                                  "color-field",
                                  SaturationBrightnessPicker::new,
                                  saturationBrightnessPicker -> {
                                    saturationBrightnessPicker
                                        .material(true)
                                        .id("clickgui.dialog.color-field")
                                        .hue(builtinOriginalService.hue())
                                        .sat(builtinOriginalService.saturation())
                                        .bri(builtinOriginalService.brightness())
                                        .cornerRadius(Float.intBitsToFloat(1103101952))
                                        .height(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)))
                                        .flex(1.0f)
                                        .minWidth(0.0f)
                                        .onChange(
                                            (f, f2) -> {
                                              builtinOriginalService.sv(
                                                  f.floatValue(), f2.floatValue());
                                              componentThemeService.invalidate();
                                            });
                                  },
                                  new ComponentKeyService[0])
                              .key("field"),
                      ComponentBoxService.column(
                              (Consumer<LayoutContainerNode>)
                                  layoutContainerNode2 -> {
                                    layoutContainerNode2
                                        .id("clickgui.dialog.color-comparison")
                                        .width(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1117782016)))
                                        .height(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)))
                                        .gap(Float.intBitsToFloat(1094713344))
                                        .flexShrink(0.0f);
                                  },
                              (ComponentKeyService<?>[])
                                  new ComponentKeyService[] {
                                    m5uevlcbcehy(
                                        "current",
                                        "Current",
                                        builtinOriginalService.original(),
                                        -1),
                                    m5uevlcbcehy("preview", "New", builtinOriginalService.argb(), 1)
                                  })
                          .key("comparison")
                    })
            .key("canvas"));
    if (z) {
      list.add(
          MaterialTextService.text(
                  "Rotate the ring · Mix inside the triangle",
                  Float.intBitsToFloat(1094713344),
                  MaterialIsLightService.ON_SURFACE_VARIANT)
              .props(
                  sceneTextService -> {
                    sceneTextService
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .wordWrap(true)
                        .flexShrink(0.0f);
                  })
              .key("triangle-help"));
    }
    if (!z) {
      list.add(
          ComponentBoxService.column(
                  (Consumer<LayoutContainerNode>)
                      layoutContainerNode3 -> {
                        layoutContainerNode3
                            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .gap(0.0f)
                            .flexShrink(0.0f);
                      },
                  (ComponentKeyService<?>[])
                      new ComponentKeyService[] {
                        MaterialTextService.label("Hue", MaterialIsLightService.ON_SURFACE_VARIANT),
                        ComponentBoxService.node(
                                "color-hue",
                                ColorGetAnimPropertyService::new,
                                colorGetAnimPropertyService -> {
                                  colorGetAnimPropertyService
                                      .material(true)
                                      .id("clickgui.dialog.hue")
                                      .hue(builtinOriginalService.hue())
                                      .size(
                                          LayoutOperationHandler.percent(
                                              Float.intBitsToFloat(1120403456)),
                                          LayoutOperationHandler.px(
                                              Float.intBitsToFloat(1111490560)))
                                      .flexShrink(0.0f)
                                      .onChange(
                                          f -> {
                                            builtinOriginalService.hue(f.floatValue());
                                            componentThemeService.invalidate();
                                          });
                                },
                                new ComponentKeyService[0])
                            .key("hue")
                      })
              .key("hue-control"));
    }
    list.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode4 -> {
                      layoutContainerNode4
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(0.0f)
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      MaterialTextService.label(
                          "Opacity · "
                              + Math.round(
                                  builtinOriginalService.alpha() * Float.intBitsToFloat(1120403456))
                              + "%",
                          MaterialIsLightService.ON_SURFACE_VARIANT),
                      ComponentBoxService.node(
                              "color-alpha",
                              AlphaSliderControl::new,
                              alphaSliderControl -> {
                                alphaSliderControl
                                    .material(true)
                                    .id("clickgui.dialog.alpha")
                                    .hue(builtinOriginalService.hue())
                                    .sat(builtinOriginalService.saturation())
                                    .bri(builtinOriginalService.brightness())
                                    .alpha(builtinOriginalService.alpha())
                                    .size(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)),
                                        LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                                    .flexShrink(0.0f)
                                    .onChange(
                                        f -> {
                                          builtinOriginalService.alpha(f.floatValue());
                                          componentThemeService.invalidate();
                                        });
                              },
                              new ComponentKeyService[0])
                          .key("alpha")
                    })
            .key("alpha-control"));
    ArrayList arrayList = new ArrayList();
    float[][] fArr = {
      new float[] {Float.intBitsToFloat(1046562734), Float.intBitsToFloat(1065017672)},
      new float[] {Float.intBitsToFloat(1054280253), Float.intBitsToFloat(1064011039)},
      new float[] {Float.intBitsToFloat(1058977874), Float.intBitsToFloat(1060655596)},
      new float[] {Float.intBitsToFloat(1061662228), Float.intBitsToFloat(1056293519)}
    };
    String[] strArr = {"Light", "Soft", "Rich", "Deep"};
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= fArr.length) {
        break;
      }
      int iShade = builtinOriginalService.shade(fArr[i2][0], fArr[i2][1]);
      arrayList.add(
          ComponentBoxService.node(
                  "material-shade",
                  MaterialJoinedService::new,
                  materialJoinedService -> {
                    materialJoinedService
                        .colors(0, MaterialIsLightService.ON_SURFACE)
                        .shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1109393408))
                        .id("clickgui.dialog.shade." + i2)
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                        .flex(1.0f)
                        .minWidth(0.0f)
                        .padding(Float.intBitsToFloat(1090519040), 0.0f)
                        .tooltip(strArr[i2])
                        .onClick(
                            () -> {
                              builtinOriginalService.sv(fArr[i2][0], fArr[i2][1]);
                              componentThemeService.invalidate();
                            });
                  },
                  ComponentBoxService.node(
                      "material-color-swatch",
                      MaterialColorService::new,
                      materialColorService -> {
                        materialColorService
                            .color(iShade)
                            .radius(Float.intBitsToFloat(1094713344))
                            .size(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                            .pointerEvents(false);
                      },
                      new ComponentKeyService[0]))
              .key("shade:" + i2));
      i = i2 + 1;
    }
    list.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode5 -> {
                      layoutContainerNode5
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(0.0f)
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      MaterialTextService.label(
                          "Shades", MaterialIsLightService.ON_SURFACE_VARIANT),
                      ComponentBoxService.row(
                          (Consumer<LayoutContainerNode>)
                              layoutContainerNode6 -> {
                                layoutContainerNode6
                                    .id("clickgui.dialog.shades")
                                    .width(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)))
                                    .gap(Float.intBitsToFloat(1090519040));
                              },
                          (ComponentKeyService<?>[])
                              arrayList.toArray(
                                  i3 -> {
                                    return new ComponentKeyService[i3];
                                  }))
                    })
            .key("shades"));
    list.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode7 -> {
                      layoutContainerNode7
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(Float.intBitsToFloat(1090519040))
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    new ComponentKeyService[] {
                      MaterialTextService.label(
                          "Hex · AARRGGBB", MaterialIsLightService.ON_SURFACE_VARIANT),
                      ComponentBoxService.node(
                              "color-hex",
                              ControlLetterSpacingService::new,
                              controlLetterSpacingService -> {
                                MaterialTextService.input(controlLetterSpacingService);
                                controlLetterSpacingService
                                    .materialError(!builtinOriginalService.error().isEmpty())
                                    .id("clickgui.dialog.hex")
                                    .maxLength(9)
                                    .size(
                                        LayoutOperationHandler.percent(
                                            Float.intBitsToFloat(1120403456)),
                                        LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))
                                    .flexShrink(0.0f);
                                if (!controlLetterSpacingService.focused()) {
                                  controlLetterSpacingService.text(builtinOriginalService.hex());
                                }
                                controlLetterSpacingService.onChanged(
                                    str -> {
                                      builtinOriginalService.hex(str);
                                      componentThemeService.invalidate();
                                    });
                                controlLetterSpacingService.onSubmit(
                                    str2 -> {
                                      updateState20();
                                    });
                              },
                              new ComponentKeyService[0])
                          .key("hex")
                    })
            .key("hex-control"));
    if (builtinOriginalService.error().isEmpty()) {
      return;
    }
    list.add(
        MaterialTextService.text(
                builtinOriginalService.error(),
                Float.intBitsToFloat(1094713344),
                MaterialIsLightService.ERROR)
            .props(
                sceneTextService2 -> {
                  sceneTextService2.wordWrap(true).flexShrink(0.0f);
                })
            .key("error"));
  }

  private ComponentKeyService<?> m5uevlcbcehy(String str, String str2, int i, int i2) {
    return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .flex(1.0f)
                      .minWidth(0.0f)
                      .gap(Float.intBitsToFloat(1086324736))
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  MaterialTextService.label(str2, MaterialIsLightService.ON_SURFACE_VARIANT),
                  ComponentBoxService.node(
                      "material-color-swatch",
                      MaterialColorService::new,
                      materialColorService -> {
                        materialColorService
                            .id("clickgui.dialog.color-" + str)
                            .color(i)
                            .radius(Float.intBitsToFloat(1105199104))
                            .paired(i2)
                            .size(
                                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                                LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))
                            .pointerEvents(false);
                      },
                      new ComponentKeyService[0])
                })
        .key(str);
  }

  private void updateState19(
      List<ComponentKeyService<?>> list, ComponentThemeService componentThemeService) {
    BuiltinRangeService builtinRangeService = this.fbknrfxp9co;
    list.add(
        MaterialTextService.text(
                builtinRangeService.bounds() + " · " + builtinRangeService.stepLabel(),
                Float.intBitsToFloat(1096810496),
                MaterialIsLightService.ON_SURFACE_VARIANT)
            .props(
                sceneTextService -> {
                  sceneTextService
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .wordWrap(true)
                      .flexShrink(0.0f);
                })
            .key("bounds"));
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        createComponentKeyService24(
            "low",
            builtinRangeService.range() ? "Lower" : "Value",
            builtinRangeService.low(),
            componentThemeService));
    if (builtinRangeService.range()) {
      arrayList.add(
          createComponentKeyService24(
              "high", "Upper", builtinRangeService.high(), componentThemeService));
    }
    list.add(
        ComponentBoxService.column(
                (Consumer<LayoutContainerNode>)
                    layoutContainerNode -> {
                      layoutContainerNode
                          .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                          .gap(Float.intBitsToFloat(1094713344))
                          .flexShrink(0.0f);
                    },
                (ComponentKeyService<?>[])
                    arrayList.toArray(
                        i -> {
                          return new ComponentKeyService[i];
                        }))
            .key("fields"));
    if (builtinRangeService.error().isEmpty()) {
      return;
    }
    list.add(
        MaterialTextService.text(
                builtinRangeService.error(),
                Float.intBitsToFloat(1094713344),
                MaterialIsLightService.ERROR)
            .props(
                sceneTextService2 -> {
                  sceneTextService2.wordWrap(true).flexShrink(0.0f);
                })
            .key("error"));
  }

  private ComponentKeyService<?> createComponentKeyService24(
      String str, String str2, String str3, ComponentThemeService componentThemeService) {
    return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .gap(Float.intBitsToFloat(1090519040))
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                new ComponentKeyService[] {
                  MaterialTextService.label(str2, MaterialIsLightService.ON_SURFACE_VARIANT),
                  ComponentBoxService.node(
                          "material-exact-number",
                          ControlLetterSpacingService::new,
                          controlLetterSpacingService -> {
                            MaterialTextService.input(controlLetterSpacingService);
                            controlLetterSpacingService
                                .id("clickgui.dialog.number." + str)
                                .materialError(!this.fbknrfxp9co.error().isEmpty())
                                .size(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456)),
                                    LayoutOperationHandler.px(Float.intBitsToFloat(1113587712)))
                                .maxLength(32)
                                .flexShrink(0.0f);
                            if (!controlLetterSpacingService.focused()) {
                              controlLetterSpacingService.text(str3);
                            }
                            controlLetterSpacingService.onChanged(
                                str4 -> {
                                  if (str.equals("low")) {
                                    this.fbknrfxp9co.low(str4);
                                  } else {
                                    this.fbknrfxp9co.high(str4);
                                  }
                                  componentThemeService.invalidate();
                                });
                            controlLetterSpacingService.onSubmit(
                                str5 -> {
                                  updateState20();
                                });
                          },
                          new ComponentKeyService[0])
                      .key(str)
                })
        .key(str);
  }

  private void updateState20() {
    if (this.moduleNameService2 == null || !this.moduleNameService2.isActive()) {
      return;
    }
    if (this.renderer2 != null) {
      ((ModuleLayoutService) this.moduleNameService2).layout(this.renderer2.draft());
    } else if (this.builtinOriginalService != null) {
      if (!this.builtinOriginalService.error().isEmpty()) {
        return;
      } else {
        ((ModuleSetting.Color) this.moduleNameService2)
            .set(Integer.valueOf(this.builtinOriginalService.argb()));
      }
    } else if (this.fbknrfxp9co != null && !this.fbknrfxp9co.apply()) {
      return;
    }
    updateState22();
    mgf9hldmchqz();
  }

  private void updateState21(
      List<ComponentKeyService<?>> list,
      ModuleSetting.Curve curve,
      ComponentThemeService componentThemeService) {
    ArrayList arrayList = new ArrayList();
    ModuleSetting.CurveType[] curveTypeArrValues = ModuleSetting.CurveType.values();
    int length = curveTypeArrValues.length;
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= length) {
        break;
      }
      ModuleSetting.CurveType curveType = curveTypeArrValues[i2];
      arrayList.add(
          createComponentKeyService27(
                  "clickgui.dialog.curve." + curveType.name(),
                  createText7(curveType),
                  () -> {
                    ModuleSetting.CurveValue curveValueSteps;
                    if (curve.isActive()) {
                      switch (curveType) {
                        case LINEAR:
                          curveValueSteps = ModuleSetting.CurveValue.linear();
                          break;
                        case CUBIC_BEZIER:
                          curveValueSteps =
                              ModuleSetting.CurveValue.cubicBezier(
                                  Float.intBitsToFloat(1048576000),
                                  Float.intBitsToFloat(1036831949),
                                  Float.intBitsToFloat(1048576000),
                                  1.0f);
                          break;
                        case STEPS:
                          curveValueSteps =
                              ModuleSetting.CurveValue.steps(4, ModuleSetting.StepMode.JUMP_END);
                          break;
                        default:
                          throw new MatchException((String) null, (Throwable) null);
                      }
                      curve.set(curveValueSteps);
                      this.moduleNumber.set(Float.valueOf(curve.get().steps()));
                      componentThemeService.invalidate();
                      mgf9hldmchqz();
                    }
                  },
                  curve.get().type() == curveType
                      ? MaterialIsLightService.PRIMARY
                      : MaterialIsLightService.SECONDARY_CONTAINER,
                  curve.get().type() == curveType
                      ? MaterialIsLightService.ON_PRIMARY
                      : MaterialIsLightService.ON_SECONDARY_CONTAINER,
                  curve.get().type() == curveType
                      ? Float.intBitsToFloat(1101004800)
                      : Float.intBitsToFloat(1094713344))
              .props(
                  materialJoinedService -> {
                    materialJoinedService
                        .joined(
                            curveType == ModuleSetting.CurveType.LINEAR,
                            curveType == ModuleSetting.CurveType.STEPS,
                            curve.get().type() == curveType)
                        .flex(1.0f)
                        .minWidth(0.0f)
                        .padding(0.0f, Float.intBitsToFloat(1090519040));
                  }));
      i = i2 + 1;
    }
    list.add(
        ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .id("clickgui.dialog.curve-types")
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .gap(2.0f)
                      .flexShrink(0.0f);
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i3 -> {
                      return new ComponentKeyService[i3];
                    })));
    list.add(
        ComponentBoxService.node(
                "material-curve",
                CurveGraphControl::new,
                curveGraphControl -> {
                  curveGraphControl
                      .material(true)
                      .value(curve.get())
                      .id("clickgui.dialog.curve-graph")
                      .size(
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                          LayoutOperationHandler.px(Float.intBitsToFloat(1131413504)))
                      .flexShrink(0.0f)
                      .onCommit(
                          curveValue -> {
                            if (curve.isActive()) {
                              curve.set(curveValue);
                            }
                            mgf9hldmchqz();
                          });
                },
                new ComponentKeyService[0])
            .key("graph"));
    if (curve.get().type() != ModuleSetting.CurveType.STEPS) {
      if (curve.get().type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
        list.add(
            MaterialTextService.text(
                    "Drag either handle to shape the transition.",
                    Float.intBitsToFloat(1096810496),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService -> {
                      sceneTextService.wordWrap(true);
                    }));
        return;
      }
      return;
    }
    list.add(
        MaterialTextService.label(
            "Steps · " + curve.get().steps(), MaterialIsLightService.ON_SURFACE_VARIANT));
    list.add(
        ComponentBoxService.node(
                "curve-steps",
                () -> {
                  return new NumberSettingControl(this.moduleNumber);
                },
                numberSettingControl -> {
                  numberSettingControl
                      .material(true)
                      .size(
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                          LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
                      .flexShrink(0.0f);
                  numberSettingControl.onChange(
                      f -> {
                        curve.set(
                            ModuleSetting.CurveValue.steps(
                                Math.max(
                                    curve.get().stepMode() == ModuleSetting.StepMode.JUMP_NONE
                                        ? 2
                                        : 1,
                                    Math.round(f.floatValue())),
                                curve.get().stepMode()));
                        componentThemeService.invalidate();
                        mgf9hldmchqz();
                      });
                },
                new ComponentKeyService[0])
            .key("steps"));
    ModuleSetting.StepMode[] stepModeArrValues = ModuleSetting.StepMode.values();
    int length2 = stepModeArrValues.length;
    int i4 = 0;
    while (true) {
      int i5 = i4;
      if (i5 >= length2) {
        return;
      }
      ModuleSetting.StepMode stepMode = stepModeArrValues[i5];
      list.add(
          createComponentKeyService22(
              "step-mode." + stepMode.name(),
              createText5(stepMode.name()),
              curve.get().stepMode() == stepMode,
              false,
              () -> {
                curve.set(
                    ModuleSetting.CurveValue.steps(
                        Math.max(
                            stepMode == ModuleSetting.StepMode.JUMP_NONE ? 2 : 1,
                            curve.get().steps()),
                        stepMode));
                componentThemeService.invalidate();
                mgf9hldmchqz();
              }));
      i4 = i5 + 1;
    }
  }

  private void updateState22() {
    manpywc7uulo();
    if (this.componentMountService2 != null) {
      mfahyvg0apdz();
      updateState27(this.componentMountService2);
    }
    if (this.componentMountService2 != null) {
      this.componentMountService2.unmountComponent();
    }
    ComponentUnmountService.unmount(this.materialTerrainTooltipsService, this.sceneComponent4);
    this.sceneComponent4 = null;
    this.componentMountService2 = null;
    this.moduleNameService2 = null;
    this.builtinOriginalService = null;
    this.fbknrfxp9co = null;
    this.renderer2 = null;
    this.renderer3 = null;
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int i, int i2) {
    int iFloorMod;
    if (this.renderer != null) {
      return this.renderer.keyPressed(i, i2);
    }
    if (this.renderer2 != null && this.renderer2.keyPressed(i, i2)) {
      return true;
    }
    if (i == 256) {
      ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt =
          createControlGetAnimPropertyService(this.materialTerrainTooltipsService);
      if (controlGetAnimPropertyServiceMh0r9igdkzmt != null) {
        controlGetAnimPropertyServiceMh0r9igdkzmt.acceptKey(i);
        return true;
      }
      ControlLetterSpacingService controlLetterSpacingServiceMcudjgl33jla =
          createControlLetterSpacingService(this.materialTerrainTooltipsService);
      if (controlLetterSpacingServiceMcudjgl33jla != null) {
        controlLetterSpacingServiceMcudjgl33jla.unfocus();
        return true;
      }
      if (this.moduleNameService2 == null) {
        return false;
      }
      updateState22();
      return true;
    }
    ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt2 =
        createControlGetAnimPropertyService(this.materialTerrainTooltipsService);
    if (controlGetAnimPropertyServiceMh0r9igdkzmt2 != null) {
      controlGetAnimPropertyServiceMh0r9igdkzmt2.acceptKey(i);
      return true;
    }
    if ((i2 & 10) != 0 && i == 70 && this.moduleNameService2 == null) {
      ControlLetterSpacingService controlLetterSpacingService =
          (ControlLetterSpacingService)
              this.materialTerrainTooltipsService.findById(
                  (i2 & 1) != 0 ? "clickgui.settings-search" : "clickgui.toolbar.search");
      if (controlLetterSpacingService == null) {
        return true;
      }
      mfahyvg0apdz();
      maqwjkcdabqp(controlLetterSpacingService);
      controlLetterSpacingService.selectAll();
      return true;
    }
    if (i == 258) {
      ArrayList arrayList = new ArrayList();
      updateState28(
          this.componentMountService2 != null
              ? this.componentMountService2
              : this.componentMountService,
          arrayList);
      if (arrayList.isEmpty()) {
        return true;
      }
      boolean z = (i2 & 1) != 0;
      int iIndexOf =
          arrayList.indexOf(
              this.scenePctService != null
                  ? this.scenePctService
                  : createControlLetterSpacingService(this.materialTerrainTooltipsService));
      mfahyvg0apdz();
      updateState27(this.materialTerrainTooltipsService);
      if (iIndexOf < 0) {
        iFloorMod = z ? arrayList.size() - 1 : 0;
      } else {
        iFloorMod = Math.floorMod(iIndexOf + (z ? -1 : 1), arrayList.size());
      }
      this.scenePctService = (ScenePctService) arrayList.get(iFloorMod);
      updateState25(this.scenePctService, true);
      updateState24(this.scenePctService);
      return true;
    }
    if (createControlLetterSpacingService(this.materialTerrainTooltipsService) != null
        || createControlGetAnimPropertyService(this.materialTerrainTooltipsService) != null) {
      return false;
    }
    ScenePctService<?> scenePctService = this.scenePctService;
    if (scenePctService instanceof MaterialJoinedService) {
      MaterialJoinedService materialJoinedService = (MaterialJoinedService) scenePctService;
      if (i == 257 || i == 32) {
        materialJoinedService.activate();
        return true;
      }
    }
    ScenePctService<?> scenePctService2 = this.scenePctService;
    if (scenePctService2 instanceof ControlGetAnimPropertyService) {
      ControlGetAnimPropertyService controlGetAnimPropertyService =
          (ControlGetAnimPropertyService) scenePctService2;
      if (i == 257 || i == 32) {
        controlGetAnimPropertyService.activate();
        return true;
      }
    }
    ScenePctService<?> scenePctService3 = this.scenePctService;
    if (scenePctService3 instanceof ControlCompactService) {
      ControlCompactService controlCompactService = (ControlCompactService) scenePctService3;
      if (i == 257 || i == 32) {
        controlCompactService.toggle();
        return true;
      }
    }
    ScenePctService<?> scenePctService4 = this.scenePctService;
    if (scenePctService4 instanceof NumberSettingControl) {
      NumberSettingControl numberSettingControl = (NumberSettingControl) scenePctService4;
      if (i == 263 || i == 262) {
        ModuleSetting.Number number = numberSettingControl.setting();
        if (number.isActive()) {
          number.set(
              Float.valueOf(
                  number.get().floatValue()
                      + ((i == 262 ? 1 : -1)
                          * (number.step() > 0.0f
                              ? number.step()
                              : (number.max() - number.min())
                                  / Float.intBitsToFloat(1120403456)))));
        }
        mgf9hldmchqz();
        return true;
      }
    }
    boolean z2 = i == 263 || i == 262;
    float fIntBitsToFloat = (i == 263 || i == 264) ? Float.intBitsToFloat(-1082130432) : 1.0f;
    if (z2) {
      ScenePctService<?> scenePctService5 = this.scenePctService;
      if (scenePctService5 instanceof ControlOnPreviewHandler) {
        ControlOnPreviewHandler controlOnPreviewHandler =
            (ControlOnPreviewHandler) scenePctService5;
        ModuleSetting.Range range = controlOnPreviewHandler.setting();
        float fStep =
            (range.step() > 0.0f
                    ? range.step()
                    : (range.max() - range.min()) / Float.intBitsToFloat(1120403456))
                * fIntBitsToFloat;
        if ((i2 & 1) != 0) {
          controlOnPreviewHandler.trySetRange(
              range.low(), Math.max(range.low(), range.high() + fStep));
        } else {
          controlOnPreviewHandler.trySetRange(
              Math.min(range.high(), range.low() + fStep), range.high());
        }
        mgf9hldmchqz();
        return true;
      }
    }
    if (!(this.moduleNameService2 instanceof ModuleSetting.Color)) {
      return false;
    }
    if (!z2 && i != 265 && i != 264) {
      return false;
    }
    if (this.scenePctService instanceof ColorGetAnimPropertyService) {
      this.builtinOriginalService.hue(
          ((this.builtinOriginalService.hue()
                      + (fIntBitsToFloat / Float.intBitsToFloat(1135869952)))
                  + 1.0f)
              % 1.0f);
    } else if (this.scenePctService instanceof SaturationBrightnessPicker) {
      this.builtinOriginalService.sv(
          this.builtinOriginalService.saturation()
              + (z2 ? fIntBitsToFloat * Float.intBitsToFloat(1008981770) : 0.0f),
          this.builtinOriginalService.brightness()
              + (z2 ? 0.0f : fIntBitsToFloat * Float.intBitsToFloat(1008981770)));
    } else {
      if (!(this.scenePctService instanceof AlphaSliderControl)) {
        return false;
      }
      this.builtinOriginalService.alpha(
          this.builtinOriginalService.alpha()
              + (fIntBitsToFloat / Float.intBitsToFloat(1132396544)));
    }
    this.componentMountService2.invalidateComponent();
    return true;
  }

  private void mfahyvg0apdz() {
    if (this.scenePctService != null) {
      updateState25(this.scenePctService, false);
    }
    this.scenePctService = null;
  }

  private static void updateState24(ScenePctService<?> scenePctService) {
    float fComputedY;
    float fComputedX;
    ScenePctService<?> scenePctServiceParent = scenePctService.parent();
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctServiceParent;
      if (scenePctService2 == null) {
        return;
      }
      if (scenePctService2.scrollMin() < 0.0f) {
        if (scenePctService.computedY()
            < scenePctService2.computedY() + Float.intBitsToFloat(1086324736)) {
          fComputedY =
              (scenePctService2.computedY() + Float.intBitsToFloat(1086324736))
                  - scenePctService.computedY();
        } else {
          fComputedY =
              scenePctService.computedY() + scenePctService.computedH()
                      > (scenePctService2.computedY() + scenePctService2.computedH())
                          - Float.intBitsToFloat(1086324736)
                  ? (((scenePctService2.computedY() + scenePctService2.computedH())
                              - Float.intBitsToFloat(1086324736))
                          - scenePctService.computedY())
                      - scenePctService.computedH()
                  : 0.0f;
        }
        float f = fComputedY;
        if ((scenePctService2 instanceof AnimatedSelectionIndicator)
            || (scenePctService2 instanceof MaterialSelectionSelector)) {
          if (scenePctService.computedX() < scenePctService2.computedX()) {
            fComputedX = scenePctService2.computedX() - scenePctService.computedX();
          } else {
            fComputedX =
                scenePctService.computedX() + scenePctService.computedW()
                        > scenePctService2.computedX() + scenePctService2.computedW()
                    ? ((scenePctService2.computedX() + scenePctService2.computedW())
                            - scenePctService.computedX())
                        - scenePctService.computedW()
                    : 0.0f;
          }
          f = fComputedX;
        }
        if (f != 0.0f) {
          float fMax =
              Math.max(
                  scenePctService2.scrollMin(), Math.min(0.0f, scenePctService2.scrollY() + f));
          scenePctService2.scrollTarget(fMax);
          MotionAnimateService.animate(
              scenePctService2,
              MotionColorsContainer.Floats.SCROLL_Y,
              fMax,
              MaterialIsLightService.FAST_SPATIAL);
        }
      }
      scenePctServiceParent = scenePctService2.parent();
    }
  }

  private static void updateState25(ScenePctService<?> scenePctService, boolean z) {
    ScenePctService<?> scenePctServiceParent = scenePctService.parent();
    while (true) {
      ScenePctService<?> scenePctService2 = scenePctServiceParent;
      if (scenePctService2 == null) {
        break;
      }
      if (scenePctService2 instanceof MaterialResponsiveService) {
        ((MaterialResponsiveService) scenePctService2).keyboardActive(z);
      }
      scenePctServiceParent = scenePctService2.parent();
    }
    if (scenePctService instanceof MaterialJoinedService) {
      ((MaterialJoinedService) scenePctService).keyboardFocused(z);
      return;
    }
    if (scenePctService instanceof ControlCompactService) {
      ((ControlCompactService) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof SliderControl) {
      ((SliderControl) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof RangeSliderControl) {
      ((RangeSliderControl) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof ControlGetAnimPropertyService) {
      ((ControlGetAnimPropertyService) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof SaturationBrightnessPicker) {
      ((SaturationBrightnessPicker) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof ColorGetAnimPropertyService) {
      ((ColorGetAnimPropertyService) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof AlphaSliderControl) {
      ((AlphaSliderControl) scenePctService).materialFocus(z);
      return;
    }
    if (scenePctService instanceof ControlLetterSpacingService) {
      ControlLetterSpacingService controlLetterSpacingService =
          (ControlLetterSpacingService) scenePctService;
      if (z) {
        maqwjkcdabqp(controlLetterSpacingService);
      } else {
        controlLetterSpacingService.unfocus();
      }
    }
  }

  private static void maqwjkcdabqp(ControlLetterSpacingService controlLetterSpacingService) {
    if (!CoreIsInitializedHandler.isReady() || CoreIsInitializedHandler.get().scene() == null) {
      controlLetterSpacingService.focus();
    } else {
      CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacingService);
    }
  }

  private static void updateState27(ScenePctService<?> scenePctService) {
    if (scenePctService == null) {
      return;
    }
    if (scenePctService instanceof ControlLetterSpacingService) {
      ControlLetterSpacingService controlLetterSpacingService =
          (ControlLetterSpacingService) scenePctService;
      if (controlLetterSpacingService.focused()) {
        controlLetterSpacingService.unfocus();
      }
    }
    if (scenePctService instanceof ControlGetAnimPropertyService) {
      ControlGetAnimPropertyService controlGetAnimPropertyService =
          (ControlGetAnimPropertyService) scenePctService;
      if (controlGetAnimPropertyService.isListening()) {
        controlGetAnimPropertyService.acceptKey(256);
      }
    }
    Iterator it = List.copyOf(scenePctService.children()).iterator();
    while (it.hasNext()) {
      updateState27((ScenePctService) it.next());
    }
  }

  private static void updateState28(
      ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
    if (scenePctService != null && scenePctService.visible && scenePctService.pointerEvents()) {
      if (((scenePctService instanceof MaterialJoinedService)
              && ((MaterialJoinedService) scenePctService).available())
          || (scenePctService instanceof ControlCompactService)
          || (scenePctService instanceof SliderControl)
          || (scenePctService instanceof RangeSliderControl)
          || (scenePctService instanceof ControlLetterSpacingService)
          || (scenePctService instanceof ControlGetAnimPropertyService)
          || (scenePctService instanceof SaturationBrightnessPicker)
          || (scenePctService instanceof ColorGetAnimPropertyService)
          || (scenePctService instanceof AlphaSliderControl)) {
        list.add(scenePctService);
      }
      Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
      while (it.hasNext()) {
        updateState28(it.next(), list);
      }
    }
  }

  private static ControlLetterSpacingService createControlLetterSpacingService(
      ScenePctService<?> scenePctService) {
    if (scenePctService == null) {
      return null;
    }
    if (scenePctService instanceof ControlLetterSpacingService) {
      ControlLetterSpacingService controlLetterSpacingService =
          (ControlLetterSpacingService) scenePctService;
      if (controlLetterSpacingService.focused()) {
        return controlLetterSpacingService;
      }
    }
    Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
    while (it.hasNext()) {
      ControlLetterSpacingService controlLetterSpacingServiceMcudjgl33jla =
          createControlLetterSpacingService(it.next());
      if (controlLetterSpacingServiceMcudjgl33jla != null) {
        return controlLetterSpacingServiceMcudjgl33jla;
      }
    }
    return null;
  }

  private static ControlGetAnimPropertyService createControlGetAnimPropertyService(
      ScenePctService<?> scenePctService) {
    if (scenePctService == null) {
      return null;
    }
    if (scenePctService instanceof ControlGetAnimPropertyService) {
      ControlGetAnimPropertyService controlGetAnimPropertyService =
          (ControlGetAnimPropertyService) scenePctService;
      if (controlGetAnimPropertyService.isListening()) {
        return controlGetAnimPropertyService;
      }
    }
    Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
    while (it.hasNext()) {
      ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt =
          createControlGetAnimPropertyService(it.next());
      if (controlGetAnimPropertyServiceMh0r9igdkzmt != null) {
        return controlGetAnimPropertyServiceMh0r9igdkzmt;
      }
    }
    return null;
  }

  private static ComponentKeyService<SceneTextService> createComponentKeyService25(
      String str, int i) {
    return MaterialTextService.label(str, i)
        .props(
            sceneTextService -> {
              sceneTextService
                  .fontSize(Float.intBitsToFloat(1096810496))
                  .lineHeight(Float.intBitsToFloat(1101004800))
                  .letterSpacing(Float.intBitsToFloat(1036831949));
            });
  }

  private static ComponentKeyService<MaterialJoinedService> createComponentKeyService26(
      String str, String str2, Runnable runnable, boolean z) {
    return createComponentKeyService27(
        str,
        str2,
        runnable,
        z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER,
        z ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER,
        Float.intBitsToFloat(1094713344));
  }

  private static ComponentKeyService<MaterialJoinedService> createComponentKeyService27(
      String str, String str2, Runnable runnable, int i, int i2, float f) {
    return MaterialTextService.button(str, str2, runnable, i, i2, f)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .shape(
                      Math.min(Float.intBitsToFloat(1099956224), f),
                      Float.intBitsToFloat(1108344832))
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                  .minWidth(Float.intBitsToFloat(1109393408))
                  .padding(0.0f, Float.intBitsToFloat(1098907648));
            })
        .children(createComponentKeyService25(str2, i2));
  }

  private static ComponentKeyService<MaterialJoinedService> createComponentKeyService28(
      String str, String str2, String str3, Runnable runnable) {
    return MaterialTextService.iconButton(str, str2, str3, runnable)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .shape(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832))
                  .surfaceWidth(Float.intBitsToFloat(1108344832))
                  .size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
            })
        .children(
            MaterialTextService.icon(str2, MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneSrcService -> {
                      sceneSrcService.size(
                          Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376));
                    }));
  }

  private static String createText(String str) {
    return str.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
  }

  private static String m7sclicozxcc(Module module) {
    return "clickgui.category."
        + createText(module.category().name())
        + ".module."
        + createText(module.name());
  }

  private static String m1vcviafemyn(Module module) {
    return "module:" + createText(module.name());
  }

  private static String createText4(ModuleFeatureType moduleFeatureType) {
    return moduleFeatureType.displayName();
  }

  private static String createText5(String str) {
    return (String)
        Arrays.stream(str.toLowerCase(Locale.ROOT).split("_"))
            .map(
                str2 -> {
                  return Character.toUpperCase(str2.charAt(0)) + str2.substring(1);
                })
            .collect(Collectors.joining(" "));
  }

  private static String createText6(float f, float f2, float f3) {
    return BuiltinRangeService.format(f, f2, f3);
  }

  private static String createText7(ModuleSetting.CurveType curveType) throws MatchException {
    switch (curveType) {
      case LINEAR:
        return "Linear";
      case CUBIC_BEZIER:
        return "Bézier";
      case STEPS:
        return "Steps";
      default:
        throw new MatchException((String) null, (Throwable) null);
    }
  }

  private static String createText8(int i) {
    if (i < 0) {
      return "Unassigned";
    }
    if (i >= 320 || (i >= 290 && i <= 314)) {
      return KeyCatalog.name(i);
    }
    switch (i) {
      case GeometryCubeService.DEFAULT_SPHERE_SEGMENTS:
        return "Space";
      case 257:
        return "Enter";
      case 258:
        return "Tab";
      case 259:
        return "Backspace";
      case 340:
        return "Left Shift";
      case 344:
        return "Right Shift";
      default:
        int iGlfwGetKeyScancode = GLFW.glfwGetKeyScancode(i);
        return KeyCatalog.displayLegend(
            iGlfwGetKeyScancode < 0 ? null : GLFW.glfwGetKeyName(i, iGlfwGetKeyScancode), i);
    }
  }
}
