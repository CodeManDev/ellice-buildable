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
    private final Function<ModuleFeatureType, List<Module>> f5nnsx7uca78;
    private final BuiltinRepository fcsg0mvq82gd;
    private final Map<String, Float> fjlbgzdyzws9;
    private final Map<ModuleFeatureType, List<Module>> fikxlao3mld;
    private MaterialTerrainTooltipsService f2nu1diavlqn;
    private ComponentMountService fgnnly9bll12;
    private ComponentMountService f4fzgs7my5mf;
    private LayoutContainerNode fjnp49geqil8;
    private ScreenScreenIdService fcdshy34nlxj;
    private ThemeIsSetService fa0t12x6vytn;
    private ModuleFeatureType f4wkce74l1bz;
    private Module f48wg40ozief;
    private String f8fi7uhb1xh9;
    private String ffxnzn9t4ymi;
    private String fbgj78te3qzw;
    private boolean ffn57s6hi2n2;
    private final Set<String> f13slc68a70r;
    private final Map<String, String> f2kmodb23a21;
    private final Set<String> fcijmdt1zfva;
    private boolean f5pafgdpvh0n;
    private boolean fa9otqddo9xr;
    private boolean ftzcjcss2ew;
    private boolean fezzio1z6avy;
    private int fb78sspb85td;
    private float f8x77xf3839a;
    private float f5l40cw6jkd8;
    private ModuleSetting<?> f5sedrlfn27b;
    private BuiltinOriginalService fc6hxrkh2sl1;
    private BuiltinRangeService fbknrfxp9co;
    private ModuleSetting.Number faprap0mz9ce;
    private ScenePctService<?> fg2zbyyyq8m8;
    private final ModuleBindService f9zqzlfsqvx1;
    private KeybindRenderer f4j6eswx7fd3;
    private InventoryRenderer fduu1s9yuh4y;
    private BuiltinRenderer f8y3rqmwwn9e;
    private ComponentKeyService<?> fahprko4fthe;
    private long fgcmhfutlzbp;
    private int fhsro87cmgxb;
    private ComponentMountService figehearx1so;
    private LayoutContainerNode f54lv3h7aq07;
    private static final ModuleFeatureType[] fdivljsc8kbu = (ModuleFeatureType[]) ModuleFeatureType.navigation().toArray(i -> {
        return new ModuleFeatureType[i];
    });
    private static final MotionFiniteService f94dghijnnca = MotionFiniteService.finite("knobPosition", scenePctService -> {
        return scenePctService instanceof ControlCompactService;
    });
    private static final List<String> f89n839yrbtw = List.of("clickgui.categories", "clickgui.module-list", "clickgui.settings", "clickgui.settings-groups", "clickgui.presets.choices");

    public ClickGuiScreen() {
        this.fcsyblho83aq = ColorFeatureType.GRADIENT;
        this.fjlbgzdyzws9 = new HashMap();
        this.fikxlao3mld = new EnumMap(ModuleFeatureType.class);
        this.f4wkce74l1bz = ModuleFeatureType.VISUALS;
        this.f8fi7uhb1xh9 = "";
        this.ffxnzn9t4ymi = "";
        this.fbgj78te3qzw = "";
        this.f13slc68a70r = new HashSet();
        this.f2kmodb23a21 = new HashMap();
        this.fcijmdt1zfva = new HashSet();
        this.f8x77xf3839a = Float.intBitsToFloat(1142292480);
        this.f5l40cw6jkd8 = Float.intBitsToFloat(1150681088);
        this.f9zqzlfsqvx1 = new ModuleBindService();
        this.f5nnsx7uca78 = null;
        this.fcsg0mvq82gd = new BuiltinRepository();
    }

    public ClickGuiScreen(ModuleRegisterService moduleRegisterService) {
        this((Function<ModuleFeatureType, List<Module>>) moduleFeatureType -> {
            return moduleRegisterService.byCategory(moduleFeatureType).toList();
        });
    }

    public ClickGuiScreen(ModuleRegisterService moduleRegisterService, BuiltinRepository builtinRepository) {
        this((Function<ModuleFeatureType, List<Module>>) moduleFeatureType -> {
            return moduleRegisterService.byCategory(moduleFeatureType).toList();
        }, builtinRepository);
    }

    public ClickGuiScreen(Function<ModuleFeatureType, List<Module>> function) {
        this(function, new BuiltinRepository());
    }

    public ClickGuiScreen(Function<ModuleFeatureType, List<Module>> function, BuiltinRepository builtinRepository) {
        this.fcsyblho83aq = ColorFeatureType.GRADIENT;
        this.fjlbgzdyzws9 = new HashMap();
        this.fikxlao3mld = new EnumMap(ModuleFeatureType.class);
        this.f4wkce74l1bz = ModuleFeatureType.VISUALS;
        this.f8fi7uhb1xh9 = "";
        this.ffxnzn9t4ymi = "";
        this.fbgj78te3qzw = "";
        this.f13slc68a70r = new HashSet();
        this.f2kmodb23a21 = new HashMap();
        this.fcijmdt1zfva = new HashSet();
        this.f8x77xf3839a = Float.intBitsToFloat(1142292480);
        this.f5l40cw6jkd8 = Float.intBitsToFloat(1150681088);
        this.f9zqzlfsqvx1 = new ModuleBindService();
        this.f5nnsx7uca78 = (Function) Objects.requireNonNull(function);
        this.fcsg0mvq82gd = (BuiltinRepository) Objects.requireNonNull(builtinRepository);
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
        ma79ua8cb9mf();
        this.fcdshy34nlxj = screenScreenIdService;
        this.fa0t12x6vytn = screenScreenIdService == null ? new ThemeIsSetService() : screenScreenIdService.theme();
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
            this.fikxlao3mld.put(moduleFeatureType, m27at5o2asox(moduleFeatureType));
            i = i2 + 1;
        }
        BuiltinRepository.State state = ThemeCornerData.current().rememberClickGui() ? this.fcsg0mvq82gd.state() : BuiltinRepository.State.initial();
        this.f4wkce74l1bz = ModuleFeatureType.find(state.category()).orElse(ModuleFeatureType.VISUALS);
        this.f8fi7uhb1xh9 = state.query();
        this.ffxnzn9t4ymi = state.settingsQuery();
        this.fbgj78te3qzw = state.settingsGroup();
        this.f5pafgdpvh0n = state.enabledOnly();
        this.fezzio1z6avy = state.detailVisible();
        this.ffn57s6hi2n2 = state.showHelp();
        this.f13slc68a70r.clear();
        this.f13slc68a70r.addAll(state.collapsedSections());
        this.f2kmodb23a21.clear();
        this.f2kmodb23a21.putAll(state.presetChoices());
        this.fcijmdt1zfva.clear();
        this.fcijmdt1zfva.addAll(state.expandedPresets());
        this.f48wg40ozief = (Module) this.fikxlao3mld.values().stream().flatMap((v0) -> {
            return v0.stream();
        }).filter(module -> {
            return m1vcviafemyn(module).equals(state.selectedModule());
        }).findFirst().orElse(null);
        if (this.f48wg40ozief != null) {
            this.f4wkce74l1bz = this.f48wg40ozief.category();
        } else if (this.fikxlao3mld.get(this.f4wkce74l1bz).isEmpty()) {
            this.f4wkce74l1bz = (ModuleFeatureType) Arrays.stream(fdivljsc8kbu).filter(moduleFeatureType2 -> {
                return !this.fikxlao3mld.get(moduleFeatureType2).isEmpty();
            }).findFirst().orElse(this.fikxlao3mld.get(ModuleFeatureType.CLIENT).isEmpty() ? ModuleFeatureType.VISUALS : ModuleFeatureType.CLIENT);
        }
        if (!m4k38isfutk5().contains(this.f48wg40ozief)) {
            this.f48wg40ozief = null;
        }
        if (this.f48wg40ozief == null) {
            this.f48wg40ozief = m4k38isfutk5().stream().findFirst().orElse(null);
            this.fbgj78te3qzw = "";
            this.ffxnzn9t4ymi = "";
        }
        this.fjlbgzdyzws9.clear();
        this.fjlbgzdyzws9.putAll(state.scroll());
        if (ThemeCornerData.current().focusSearch()) {
            this.fezzio1z6avy = false;
        }
        this.fa9otqddo9xr = this.f5l40cw6jkd8 < Float.intBitsToFloat(1146224640);
        this.ftzcjcss2ew = this.f5l40cw6jkd8 < Float.intBitsToFloat(1142292480);
        this.fahprko4fthe = null;
        this.fgcmhfutlzbp++;
        this.fg2zbyyyq8m8 = null;
        this.f2nu1diavlqn = new MaterialTerrainTooltipsService();
        this.f2nu1diavlqn.id("clickgui.panel-shell").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).direction(ScenePctService.Direction.NONE).backgroundColor(1375731712).interactive(true);
        this.f2nu1diavlqn.onLayout(this::m5ysaxlmqi24);
        this.fgnnly9bll12 = new ComponentMountService().mount(this::m6lbrafa0l34, this.fa0t12x6vytn);
        this.fgnnly9bll12.absolute().inset(LayoutOperationHandler.px(0.0f)).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto());
        this.f2nu1diavlqn.addChild(this.fgnnly9bll12);
        this.fb78sspb85td = mj6gzbc3kwej();
        return this.f2nu1diavlqn;
    }

    private List<Module> m27at5o2asox(ModuleFeatureType moduleFeatureType) {
        List<Module> list;
        if (this.f5nnsx7uca78 != null) {
            list = this.f5nnsx7uca78.apply(moduleFeatureType);
        } else {
            list = CoreIsInitializedHandler.isReady() ? CoreIsInitializedHandler.get().modules().byCategory(moduleFeatureType).toList() : List.of();
        }
        List<Module> list2 = list;
        return list2 == null ? List.of() : list2.stream().sorted(Comparator.comparing((v0) -> {
            return v0.name();
        }, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    private ComponentKeyService<?> m6lbrafa0l34(ComponentThemeService componentThemeService) {
        if (this.f2nu1diavlqn != null) {
            this.f2nu1diavlqn.backgroundColor(ThemeCornerData.current().dimBackground() ? 1375731712 : 0);
        }
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        arrayList.add(mezavdvwfusb());
        if (this.fahprko4fthe != null) {
            long j = this.fgcmhfutlzbp;
            arrayList.add(MaterialFindByIdSelector.of(this.fahprko4fthe, this.fhsro87cmgxb, j, () -> {
                if (this.fgcmhfutlzbp == j) {
                    this.fahprko4fthe = null;
                    mgf9hldmchqz();
                }
            }));
        }
        return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            float fIntBitsToFloat;
            LayoutContainerNode size = layoutContainerNode.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            if (this.ftzcjcss2ew) {
                fIntBitsToFloat = 0.0f;
            } else {
                fIntBitsToFloat = this.f8x77xf3839a < Float.intBitsToFloat(1143603200) ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1103101952);
            }
            size.padding(fIntBitsToFloat).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.panel(sceneCornerRadiusService -> {
            sceneCornerRadiusService.id("clickgui.workspace").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxWidth(Float.intBitsToFloat(1151664128)).maxHeight(Float.intBitsToFloat(1148190720)).cornerRadius(this.ftzcjcss2ew ? 0.0f : Float.intBitsToFloat(1105199104)).backgroundColor(MaterialIsLightService.SURFACE).direction(ScenePctService.Direction.COLUMN).clip(true);
        }, mq98d5e3j21(), merb4w1cwozn(), ComponentBoxService.stack((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flex(1.0f).minHeight(0.0f).clip(true);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })))}).key("workspace");
    }

    private ComponentKeyService<?> mezavdvwfusb() {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        if (!this.fa9otqddo9xr || !this.fezzio1z6avy) {
            arrayList.add(mg3ujj02y0vv());
        }
        if (!this.fa9otqddo9xr || this.fezzio1z6avy) {
            arrayList.add(mfid3a0bf3zm());
        }
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            float fIntBitsToFloat;
            LayoutContainerNode layoutContainerNodeMinHeight = layoutContainerNode.id("clickgui.content").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(0.0f);
            if (this.ftzcjcss2ew) {
                fIntBitsToFloat = this.f8x77xf3839a < Float.intBitsToFloat(1135869952) ? 4 : 12;
            } else {
                fIntBitsToFloat = this.f8x77xf3839a < Float.intBitsToFloat(1143603200) ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1101004800);
            }
            layoutContainerNodeMinHeight.padding(fIntBitsToFloat).gap(Float.intBitsToFloat(1101004800)).align(ScenePctService.Align.STRETCH);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("current-page");
    }

    private ComponentKeyService<?> mq98d5e3j21() {
        boolean z = this.f5l40cw6jkd8 < Float.intBitsToFloat(1144258560);
        boolean z2 = this.f8x77xf3839a < Float.intBitsToFloat(1138163712);
        if (this.fa9otqddo9xr && this.fezzio1z6avy) {
            Consumer<LayoutContainerNode> consumer = layoutContainerNode -> {
                layoutContainerNode.id("clickgui.toolbar").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1113587712))).padding(Float.intBitsToFloat(1082130432)).gap(this.ftzcjcss2ew ? 2.0f : Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).flexShrink(0.0f);
            };
            ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[8];
            componentKeyServiceArr[0] = m9o0yydzlndu("clickgui.back", "arrow_back", "Back to modules", this::mkdxr04hjju).props(materialJoinedService -> {
                materialJoinedService.size(this.ftzcjcss2ew ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1111490560), Float.intBitsToFloat(1109393408));
            });
            componentKeyServiceArr[1] = MaterialTextService.text(this.f48wg40ozief == null ? "Settings" : this.f48wg40ozief.name(), Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
                sceneTextService.flex(1.0f).minWidth(0.0f);
            });
            componentKeyServiceArr[2] = ma0pfhge5x0c().props(scenePctService -> {
                scenePctService.size(this.ftzcjcss2ew ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1111490560), Float.intBitsToFloat(1109393408));
            });
            componentKeyServiceArr[3] = mfgmzaw8m0tr().props(scenePctService2 -> {
                scenePctService2.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1109393408));
            });
            componentKeyServiceArr[4] = m6x9fbkwh0d0().props(scenePctService3 -> {
                scenePctService3.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1109393408));
            });
            componentKeyServiceArr[5] = m9o0yydzlndu("clickgui.toolbar.keybinds", "keyboard", "Choose a module keybind", this::m18zjyo62x7g).props(materialJoinedService2 -> {
                materialJoinedService2.size(this.ftzcjcss2ew ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1111490560), Float.intBitsToFloat(1109393408));
            });
            componentKeyServiceArr[6] = this.f48wg40ozief != null ? mj3dgwwy2r06(this.f48wg40ozief, "clickgui.toolbar.module-toggle") : ComponentBoxService.box(layoutContainerNode2 -> {
                layoutContainerNode2.visible(false);
            }, new ComponentKeyService[0]);
            componentKeyServiceArr[7] = m9o0yydzlndu("clickgui.toolbar.close", "close", "Close · Esc", this::m121jzi3356z).props(materialJoinedService3 -> {
                materialJoinedService3.size(this.ftzcjcss2ew ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1111490560), Float.intBitsToFloat(1109393408));
            });
            return ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr).key("toolbar");
        }
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        ComponentKeyService<?> componentKeyServiceKey = ComponentBoxService.node("material-search-bar", InteractiveSurfacePanel::new, interactiveSurfacePanel -> {
            interactiveSurfacePanel.id("clickgui.search-bar").height(LayoutOperationHandler.px(Float.intBitsToFloat(1112539136))).flex(1.0f).minWidth(0.0f).cornerRadius(Float.intBitsToFloat(1104150528)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).padding(0.0f, Float.intBitsToFloat(1090519040));
        }, MaterialTextService.icon("search", MaterialIsLightService.ON_SURFACE).props(sceneSrcService -> {
            sceneSrcService.margin(0.0f, Float.intBitsToFloat(1090519040));
        }), ComponentBoxService.node("material-search", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            controlLetterSpacingService.id("clickgui.toolbar.search").materialSearch(true).fontFamily(MaterialIsLightService.FONT).fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).maxLength(100).placeholder(this.ftzcjcss2ew ? "Search" : "Search modules").bgColor(0).textColor(MaterialIsLightService.ON_SURFACE).placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT).focusBorder(0).selectionColor(MaterialIsLightService.SELECTION).cornerRadius(0.0f).height(LayoutOperationHandler.px((z && z2) ? Float.intBitsToFloat(1105199104) : Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(0.0f).stopPropagation(true).onChanged(str -> {
                this.f8fi7uhb1xh9 = str.strip().toLowerCase(Locale.ROOT);
                this.fezzio1z6avy = false;
                m519jxejgk6g();
                mgf9hldmchqz();
            });
            if (controlLetterSpacingService.focused()) {
                return;
            }
            controlLetterSpacingService.text(this.f8fi7uhb1xh9);
        }, new ComponentKeyService[0]).key("search-input"), m9o0yydzlndu("clickgui.toolbar.clear", "close", "Clear search", () -> {
            this.f8fi7uhb1xh9 = "";
            ControlLetterSpacingService controlLetterSpacingService2 = (ControlLetterSpacingService) this.f2nu1diavlqn.findById("clickgui.toolbar.search");
            if (controlLetterSpacingService2 != null) {
                controlLetterSpacingService2.text("");
            }
            m519jxejgk6g();
            mgf9hldmchqz();
        }).props(materialJoinedService4 -> {
            materialJoinedService4.visible(!this.f8fi7uhb1xh9.isEmpty()).size((z && z2) ? Float.intBitsToFloat(1106247680) : Float.intBitsToFloat(1111490560), (z && z2) ? Float.intBitsToFloat(1106247680) : Float.intBitsToFloat(1111490560));
        })).key("search");
        if (!z) {
            arrayList.add(componentKeyServiceKey);
        }
        if (z) {
            arrayList.add(m9o0yydzlndu("clickgui.toolbar.enabled-filter", this.f5pafgdpvh0n ? "check" : "filter_list", "Show enabled modules", () -> {
                this.f5pafgdpvh0n = !this.f5pafgdpvh0n;
                m519jxejgk6g();
                mgf9hldmchqz();
            }).props(materialJoinedService5 -> {
                materialJoinedService5.colors(this.f5pafgdpvh0n ? MaterialIsLightService.SECONDARY_CONTAINER : 0, MaterialIsLightService.ON_SECONDARY_CONTAINER);
            }));
        } else {
            Supplier supplier = MaterialJoinedService::new;
            Consumer<MaterialJoinedService> consumer2 = materialJoinedService6 -> {
                materialJoinedService6.colors(this.f5pafgdpvh0n ? MaterialIsLightService.SECONDARY_CONTAINER : 0, this.f5pafgdpvh0n ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1109393408)).outlined(!this.f5pafgdpvh0n);
                materialJoinedService6.id("clickgui.toolbar.enabled-filter").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                    this.f5pafgdpvh0n = !this.f5pafgdpvh0n;
                    m519jxejgk6g();
                    mgf9hldmchqz();
                });
            };
            ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
            componentKeyServiceArr2[0] = MaterialTextService.icon(this.f5pafgdpvh0n ? "check" : "filter_list", this.f5pafgdpvh0n ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.PRIMARY).props(sceneSrcService2 -> {
                sceneSrcService2.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224));
            });
            componentKeyServiceArr2[1] = MaterialTextService.label("Enabled", this.f5pafgdpvh0n ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT);
            arrayList.add(ComponentBoxService.node("material-button", supplier, consumer2, componentKeyServiceArr2).key("filter"));
        }
        arrayList.add(z ? m9o0yydzlndu("clickgui.toolbar.configs", "folder", "Manage configs", () -> {
            if (this.fcdshy34nlxj != null) {
                this.fcdshy34nlxj.open("configs");
            }
        }) : ma0pfhge5x0c());
        arrayList.add(mfgmzaw8m0tr());
        arrayList.add(m6x9fbkwh0d0());
        arrayList.add(m9o0yydzlndu("clickgui.toolbar.network", "swap_horiz", "FRITZ!Box · Reconnect", () -> {
            if (this.fcdshy34nlxj != null) {
                this.fcdshy34nlxj.open("fritz-box");
            }
        }));
        arrayList.add(m9o0yydzlndu("clickgui.toolbar.keybinds", "keyboard", "Choose a keybind for the selected module", this::m18zjyo62x7g));
        arrayList.add(m9o0yydzlndu("clickgui.toolbar.close", "close", "Close · Esc", this::m121jzi3356z));
        if (z) {
            return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode3 -> {
                layoutContainerNode3.id("clickgui.toolbar").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(z2 ? Float.intBitsToFloat(1116209152) : Float.intBitsToFloat(1119354880))).padding(Float.intBitsToFloat(1082130432)).gap(z2 ? 0.0f : 2.0f).flexShrink(0.0f);
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{componentKeyServiceKey.props(interactiveSurfacePanel2 -> {
                interactiveSurfacePanel2.flex(0.0f).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(z2 ? Float.intBitsToFloat(1106247680) : Float.intBitsToFloat(1110966272)));
            }), ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode4 -> {
                layoutContainerNode4.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(z2 ? Float.intBitsToFloat(1106247680) : Float.intBitsToFloat(1108344832))).gap(0.0f).justify(ScenePctService.Justify.SPACE_BETWEEN).align(ScenePctService.Align.CENTER);
            }, (ComponentKeyService<?>[]) arrayList.stream().map(componentKeyService -> {
                return componentKeyService.props(scenePctService4 -> {
                    scenePctService4.size(Float.intBitsToFloat(1108344832), z2 ? Float.intBitsToFloat(1106247680) : Float.intBitsToFloat(1108344832)).minWidth(0.0f);
                });
            }).toArray(i -> {
                return new ComponentKeyService[i];
            }))}).key("toolbar");
        }
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode5 -> {
            layoutContainerNode5.id("clickgui.toolbar").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(this.f8x77xf3839a < Float.intBitsToFloat(1143603200) ? Float.intBitsToFloat(1116733440) : Float.intBitsToFloat(1117782016))).padding(Float.intBitsToFloat(1094713344), this.ftzcjcss2ew ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1103101952)).gap(this.ftzcjcss2ew ? 0.0f : Float.intBitsToFloat(1094713344)).align(ScenePctService.Align.CENTER).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i2 -> {
            return new ComponentKeyService[i2];
        })).key("toolbar");
    }

    
    private ComponentKeyService<?> m6x9fbkwh0d0() {
        return m9o0yydzlndu("clickgui.toolbar.friends", "friends", "Friends · individual module exclusions", () -> {
            if (this.fcdshy34nlxj != null) {
                this.fcdshy34nlxj.open("friends");
            }
        }).props(materialJoinedService -> {
            materialJoinedService.shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1107296256)).surfaceWidth(Float.intBitsToFloat(1107296256));
            materialJoinedService.size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
        }).children(MaterialTextService.icon("friends", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
        }));
    }

    
    private ComponentKeyService<?> mfgmzaw8m0tr() {
        return m9o0yydzlndu("clickgui.toolbar.anticheats", "security", "Minecraft Anticheat List", () -> {
            if (this.fcdshy34nlxj != null) {
                this.fcdshy34nlxj.open("anticheats");
            }
        }).props(materialJoinedService -> {
            materialJoinedService.shape(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1107296256)).surfaceWidth(Float.intBitsToFloat(1107296256));
            materialJoinedService.size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
        }).children(MaterialTextService.icon("security", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
        }));
    }

    
    private ComponentKeyService<?> ma0pfhge5x0c() {
        Runnable runnable = () -> {
            if (this.fcdshy34nlxj != null) {
                this.fcdshy34nlxj.open("configs");
            }
        };
        return (this.ftzcjcss2ew || (this.fa9otqddo9xr && this.fezzio1z6avy)) ? m9o0yydzlndu("clickgui.toolbar.configs", "folder", "Manage configs", runnable) : mgtqng4c5k74("clickgui.toolbar.configs", "Configs", runnable, false).props(materialJoinedService -> {
            materialJoinedService.gap(Float.intBitsToFloat(1090519040)).tooltip("Save, load and manage your configs");
        }).children(MaterialTextService.icon("folder", MaterialIsLightService.ON_SECONDARY_CONTAINER).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
        }), m1z6anxxnaah("Configs", MaterialIsLightService.ON_SECONDARY_CONTAINER));
    }

    private ComponentKeyService<?> merb4w1cwozn() {
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<>();
        ModuleFeatureType[] moduleFeatureTypeArr = fdivljsc8kbu;
        int length = moduleFeatureTypeArr.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f).visible((this.fa9otqddo9xr && this.fezzio1z6avy) ? false : true);
                }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                    layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).align(ScenePctService.Align.CENTER).gap(Float.intBitsToFloat(1082130432)).padding(0.0f, this.ftzcjcss2ew ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1098907648));
                }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("material-tabs", AnimatedSelectionIndicator::new, animatedSelectionIndicator -> {
                    animatedSelectionIndicator.selection(this.f8fi7uhb1xh9.isBlank() ? Arrays.asList(fdivljsc8kbu).indexOf(this.f4wkce74l1bz) : -1).id("clickgui.categories").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).flex(1.0f).minWidth(0.0f).direction(ScenePctService.Direction.ROW).scrollable(true).scrollbarWidth(0.0f).clip(true);
                }, (ComponentKeyService[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                })), m9o0yydzlndu("clickgui.client-modules", "tune", "Client modules", () -> {
                    mbt1qklrr5y6(ModuleFeatureType.CLIENT);
                }).props(materialJoinedService -> {
                    materialJoinedService.colors((this.f4wkce74l1bz == ModuleFeatureType.CLIENT || this.f4wkce74l1bz == ModuleFeatureType.DEVELOPMENT) ? MaterialIsLightService.SECONDARY_CONTAINER : 0, MaterialIsLightService.ON_SURFACE_VARIANT);
                }), m9o0yydzlndu("clickgui.client-settings", "settings", "Client settings\nColors, presets and appearance", () -> {
                    if (this.fcdshy34nlxj != null) {
                        this.fcdshy34nlxj.open("client-settings");
                    }
                })}), MaterialTextService.divider()}).key("tabs");
            }
            ModuleFeatureType moduleFeatureType = moduleFeatureTypeArr[i2];
            boolean z = this.f8fi7uhb1xh9.isBlank() && moduleFeatureType == this.f4wkce74l1bz;
            Supplier supplier = MaterialJoinedService::new;
            Consumer<MaterialJoinedService> consumer = materialJoinedService2 -> {
                materialJoinedService2.colors(0, z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT).shape(0.0f, Float.intBitsToFloat(1111490560));
                materialJoinedService2.id("clickgui.category." + m1909encvz9z(moduleFeatureType.name()) + ".tab").height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).width(LayoutOperationHandler.auto()).minWidth(Float.intBitsToFloat(1120403456)).padding(0.0f, Float.intBitsToFloat(1101004800)).flexShrink(0.0f).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER).onClick(() -> {
                    mbt1qklrr5y6(moduleFeatureType);
                });
            };
            ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[1];
            componentKeyServiceArr[0] = MaterialTextService.label(mjkwd8iq0qx4(moduleFeatureType), z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
                sceneTextService.fontSize(Float.intBitsToFloat(1097859072)).lineHeight(Float.intBitsToFloat(1102053376)).letterSpacing(Float.intBitsToFloat(1036831949)).flexShrink(0.0f);
            });
            arrayList.add(ComponentBoxService.node("material-tab", supplier, consumer, componentKeyServiceArr).key(moduleFeatureType.name()));
            i = i2 + 1;
        }
    }

    private List<Module> m4k38isfutk5() {
        return (this.f8fi7uhb1xh9.isBlank() ? this.fikxlao3mld.get(this.f4wkce74l1bz).stream() : this.fikxlao3mld.values().stream().flatMap((v0) -> {
            return v0.stream();
        })).filter(module -> {
            return module.category() != ModuleFeatureType.DEVELOPMENT || this.f4wkce74l1bz == ModuleFeatureType.DEVELOPMENT;
        }).filter(module2 -> {
            return !this.f5pafgdpvh0n || module2.isEnabled();
        }).filter(module3 -> {
            return this.f8fi7uhb1xh9.isBlank() || (module3.name() + " " + module3.description() + " " + mjkwd8iq0qx4(module3.category())).toLowerCase(Locale.ROOT).contains(this.f8fi7uhb1xh9);
        }).sorted(Comparator.comparing((v0) -> {
            return v0.name();
        }, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    private ComponentKeyService<?> mg3ujj02y0vv() {
        ComponentKeyService<?> componentKeyServiceProps;
        boolean z = this.fa9otqddo9xr && this.f8x77xf3839a < Float.intBitsToFloat(1135869952);
        List<Module> listM4k38isfutk5 = m4k38isfutk5();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= listM4k38isfutk5.size()) {
                break;
            }
            arrayList.add(mcg9lshlmt6p(listM4k38isfutk5.get(i2), i2 == 0, i2 == listM4k38isfutk5.size() - 1));
            i = i2 + 1;
        }
        if (arrayList.isEmpty()) {
            Consumer<LayoutContainerNode> consumer = layoutContainerNode -> {
                layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(Float.intBitsToFloat(1103101952)).gap(Float.intBitsToFloat(1090519040));
            };
            ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
            componentKeyServiceArr[0] = MaterialTextService.text("No modules found", Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE);
            componentKeyServiceArr[1] = MaterialTextService.text(this.f5pafgdpvh0n ? "Try showing all modules." : "Try a different search.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
                sceneTextService.wordWrap(true);
            });
            componentKeyServiceArr[2] = mgtqng4c5k74("clickgui.clear-filters", "Clear filters", () -> {
                this.f8fi7uhb1xh9 = "";
                this.f5pafgdpvh0n = false;
                m519jxejgk6g();
                mgf9hldmchqz();
            }, false);
            arrayList.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr));
        }
        Consumer<LayoutContainerNode> consumer2 = layoutContainerNode2 -> {
            LayoutOperationHandler layoutOperationHandlerPx;
            LayoutContainerNode layoutContainerNodeId = layoutContainerNode2.id("clickgui.modules");
            if (this.fa9otqddo9xr) {
                layoutOperationHandlerPx = LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456));
            } else {
                layoutOperationHandlerPx = LayoutOperationHandler.px(this.f5l40cw6jkd8 < Float.intBitsToFloat(1149861888) ? Float.intBitsToFloat(1133117440) : Float.intBitsToFloat(1134297088));
            }
            layoutContainerNodeId.width(layoutOperationHandlerPx).flexShrink(0.0f).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(z ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1094713344));
        };
        ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
        Consumer<LayoutContainerNode> consumer3 = layoutContainerNode3 -> {
            layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(z ? Float.intBitsToFloat(1108344832) : Float.intBitsToFloat(1110441984))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).flexShrink(0.0f);
        };
        ComponentKeyService[] componentKeyServiceArr3 = new ComponentKeyService[2];
        componentKeyServiceArr3[0] = MaterialTextService.label(this.f8fi7uhb1xh9.isBlank() ? mjkwd8iq0qx4(this.f4wkce74l1bz) : "Search results", MaterialIsLightService.ON_SURFACE).props(sceneTextService2 -> {
            sceneTextService2.flex(1.0f).minWidth(0.0f);
        });
        if (this.f4wkce74l1bz == ModuleFeatureType.CLIENT) {
            componentKeyServiceProps = mgtqng4c5k74("clickgui.developer-tools", "Developer tools", () -> {
                mbt1qklrr5y6(ModuleFeatureType.DEVELOPMENT);
            }, false).props(materialJoinedService -> {
                materialJoinedService.shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107296256));
                materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))).padding(0.0f, Float.intBitsToFloat(1092616192)).tooltip("Setting galleries and rendering experiments");
            });
        } else {
            componentKeyServiceProps = this.f4wkce74l1bz == ModuleFeatureType.DEVELOPMENT ? mgtqng4c5k74("clickgui.developer-back", "Back", () -> {
                mbt1qklrr5y6(ModuleFeatureType.CLIENT);
            }, false).props(materialJoinedService2 -> {
                materialJoinedService2.shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107296256));
                materialJoinedService2.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))).padding(0.0f, Float.intBitsToFloat(1092616192)).tooltip("Back to client settings");
            }) : MaterialTextService.text(Integer.toString(listM4k38isfutk5.size()), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT);
        }
        componentKeyServiceArr3[1] = componentKeyServiceProps;
        componentKeyServiceArr2[0] = ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer3, (ComponentKeyService<?>[]) componentKeyServiceArr3);
        componentKeyServiceArr2[1] = ComponentBoxService.node("material-reflow-list", MaterialComponent::new, materialComponent -> {
            materialComponent.id("clickgui.module-list").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flex(1.0f).minHeight(0.0f).padding(0.0f, Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1082130432), 0.0f).gap(Float.intBitsToFloat(1090519040)).scrollable(true).scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT).scrollbarWidth(Float.intBitsToFloat(1077936128)).scrollbarTrackColor(338569528).scrollInset(Float.intBitsToFloat(1090519040)).clip(true);
        }, (ComponentKeyService[]) arrayList.toArray(i3 -> {
            return new ComponentKeyService[i3];
        }));
        return ComponentBoxService.column((Consumer<LayoutContainerNode>) consumer2, (ComponentKeyService<?>[]) componentKeyServiceArr2).key("module-list");
    }

    private ComponentKeyService<?> mcg9lshlmt6p(Module module, boolean z, boolean z2) {
        String strM7sclicozxcc = m7sclicozxcc(module);
        boolean z3 = module == this.f48wg40ozief && (!this.fa9otqddo9xr || this.fezzio1z6avy);
        int iLayer = z3 ? MaterialIsLightService.layer(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.PRIMARY, Float.intBitsToFloat(1040522936)) : MaterialIsLightService.SURFACE_LOW;
        ArrayList arrayList = new ArrayList();
        arrayList.add(MaterialTextService.text(module.name(), Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
            sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).lineHeight(Float.intBitsToFloat(1102053376));
        }).key("name"));
        arrayList.add(MaterialTextService.text(this.f8fi7uhb1xh9.isBlank() ? module.description() : mjkwd8iq0qx4(module.category()), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService2 -> {
            sceneTextService2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        }).key("description"));
        return ComponentBoxService.node("material-module-row", MaterialResponsiveService::new, materialResponsiveService -> {
            materialResponsiveService.surface(iLayer).responsive(true).selected(z3).id(strM7sclicozxcc).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(Float.intBitsToFloat(1117782016)).flexShrink(0.0f).cornerRadius(Float.intBitsToFloat(1099956224)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).padding(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1092616192)).onRightClick(() -> {
                if (module instanceof ModuleOperationHandler) {
                    ((ModuleOperationHandler) module).openPanel();
                } else {
                    m5zxnod9vmdl(module);
                }
            });
        }, ComponentBoxService.node("module-select", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1094713344), 0.0f);
            materialJoinedService.id(strM7sclicozxcc + ".open-settings").height(LayoutOperationHandler.px(Float.intBitsToFloat(1114636288))).minWidth(0.0f).flex(1.0f).padding(0.0f, Float.intBitsToFloat(1082130432)).direction(ScenePctService.Direction.COLUMN).gap(Float.intBitsToFloat(1084227584)).justify(ScenePctService.Justify.CENTER).tooltip(module.description()).onClick(() -> {
                m5zxnod9vmdl(module);
            });
        }, (ComponentKeyService[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("select"), mj3dgwwy2r06(module, strM7sclicozxcc + ".toggle")).key(strM7sclicozxcc);
    }

    private ComponentKeyService<?> mfid3a0bf3zm() {
        if (this.f48wg40ozief == null) {
            return ComponentBoxService.panel(sceneCornerRadiusService -> {
                sceneCornerRadiusService.flex(1.0f).minWidth(0.0f).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).backgroundColor(MaterialIsLightService.SURFACE_LOW).cornerRadius(Float.intBitsToFloat(1105199104)).padding(Float.intBitsToFloat(1107296256)).direction(ScenePctService.Direction.COLUMN).justify(ScenePctService.Justify.CENTER).gap(Float.intBitsToFloat(1094713344));
            }, MaterialTextService.text("Your modules, your way", Float.intBitsToFloat(1103101952), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
                sceneTextService.wordWrap(true);
            }), MaterialTextService.text("Choose a module to adjust its settings.", Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService2 -> {
                sceneTextService2.wordWrap(true);
            })).key("detail-empty");
        }
        Module module = this.f48wg40ozief;
        if (this.ffxnzn9t4ymi.isBlank() && !this.fbgj78te3qzw.isEmpty() && module.settings().stream().noneMatch(moduleSetting -> {
            return m5w7etjeu8ni(moduleSetting, (String) moduleSetting.category().map((v0) -> {
                return v0.qualifiedName();
            }).orElse("general"));
        })) {
            this.fbgj78te3qzw = "";
        }
        ArrayList arrayList = new ArrayList();
        if (this.ffxnzn9t4ymi.isBlank() && !module.presets().isEmpty()) {
            arrayList.add(m1115iweonkl(module));
        }
        if (module instanceof ModuleOperationHandler) {
            ModuleOperationHandler moduleOperationHandler = (ModuleOperationHandler) module;
            String str = "Open " + module.name();
            Objects.requireNonNull(moduleOperationHandler);
            arrayList.add(mgtqng4c5k74("clickgui.module.open-panel", str, moduleOperationHandler::openPanel, true));
        }
        m3zqjvi22tyt(arrayList, "General", "", "general", module.settings().stream().filter(moduleSetting2 -> {
            return moduleSetting2.category().isEmpty();
        }).toList());
        Iterator<ModuleNameService> it = module.settingCategories().iterator();
        while (it.hasNext()) {
            m5oe4on6eb1f(arrayList, it.next());
        }
        if (arrayList.isEmpty() && this.ffxnzn9t4ymi.isBlank()) {
            arrayList.add(MaterialTextService.text("This module has no additional settings.", Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService3 -> {
                sceneTextService3.padding(Float.intBitsToFloat(1098907648)).wordWrap(true);
            }));
        }
        ComponentKeyService<?> objKey = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id("clickgui.detail.header").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(this.f8x77xf3839a < Float.intBitsToFloat(1143603200) ? Float.intBitsToFloat(1116733440) : Float.intBitsToFloat(1117782016))).padding(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1101004800)).gap(Float.intBitsToFloat(1096810496)).align(ScenePctService.Align.CENTER).flexShrink(0.0f).tooltip(module.description());
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("material-emblem", MaterialLabelService::new, materialLabelService -> {
            materialLabelService.shape("cookie9").active(module.isEnabled()).label(module.name().substring(0, module.name().offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT)).tint(module.isEnabled() ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY).size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408)).flexShrink(0.0f);
        }, new ComponentKeyService[0]).key("emblem"), MaterialTextService.text(module.name(), Float.intBitsToFloat(1103101952), MaterialIsLightService.ON_SURFACE).props(sceneTextService4 -> {
            sceneTextService4.id("clickgui.detail.title").flex(1.0f).minWidth(0.0f);
        }), mj3dgwwy2r06(module, "clickgui.detail.toggle")}).key("detail-header");
        if (arrayList.isEmpty() && !this.ffxnzn9t4ymi.isBlank()) {
            arrayList.add(MaterialTextService.text("No matching settings", Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService5 -> {
                sceneTextService5.padding(Float.intBitsToFloat(1094713344));
            }));
        }
        ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
        if (!this.fa9otqddo9xr) {
            arrayList2.add(objKey);
        }
        arrayList2.add(mfstwwxrgcrh());
        if (!module.settingCategories().isEmpty()) {
            arrayList2.add(m2bf7ex0sca4());
        }
        arrayList2.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.id("clickgui.settings").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flex(1.0f).minHeight(0.0f).padding(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1101004800)).scrollable(true).scrollbarColor(MaterialIsLightService.OUTLINE_VARIANT).scrollbarWidth(Float.intBitsToFloat(1077936128)).clip(true);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("settings-scroll"));
        return ComponentBoxService.panel(sceneCornerRadiusService2 -> {
            sceneCornerRadiusService2.id("clickgui.detail").width(this.fa9otqddo9xr ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.auto()).flex(1.0f).minWidth(0.0f).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).backgroundColor(MaterialIsLightService.SURFACE_LOW).cornerRadius(this.ftzcjcss2ew ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1105199104)).direction(ScenePctService.Direction.COLUMN).clip(true);
        }, (ComponentKeyService[]) arrayList2.toArray(i2 -> {
            return new ComponentKeyService[i2];
        })).key("detail:" + m7sclicozxcc(module)).onMount(sceneCornerRadiusService3 -> {
            MaterialEnterService.enter(sceneCornerRadiusService3, this.fa9otqddo9xr ? Float.intBitsToFloat(1103101952) : Float.intBitsToFloat(1098907648), 0.0f);
        });
    }

    private ComponentKeyService<?> m1115iweonkl(Module module) {
        List<ModuleIdService> listPresets = module.presets();
        String orDefault = this.f2kmodb23a21.getOrDefault(module.name(), ((ModuleIdService) listPresets.getFirst()).id());
        return PresetsComponent.render(module, orDefault, this.fcijmdt1zfva.contains(module.name()), this.ftzcjcss2ew || (!this.fa9otqddo9xr && this.f5l40cw6jkd8 < Float.intBitsToFloat(1148846080)), str -> {
            this.f2kmodb23a21.put(module.name(), str);
            this.fcijmdt1zfva.remove(module.name());
            mgf9hldmchqz();
        }, () -> {
            if (!this.fcijmdt1zfva.remove(module.name())) {
                this.fcijmdt1zfva.add(module.name());
            }
            mgf9hldmchqz();
        }, () -> {
            ModuleIdService moduleIdService = (ModuleIdService) listPresets.stream().filter(moduleIdService2 -> {
                return moduleIdService2.id().equals(orDefault);
            }).findFirst().orElse((ModuleIdService) listPresets.getFirst());
            m4m81i8kd5x7(this.fgnnly9bll12);
            moduleIdService.apply(module);
            mfahyvg0apdz();
            mgf9hldmchqz();
        });
    }

    private ComponentKeyService<?> mj3dgwwy2r06(Module module, String str) {
        return ComponentBoxService.node("module-switch", ControlCompactService::new, controlCompactService -> {
            controlCompactService.material(true).compact(false).id(str).size(Float.intBitsToFloat(1113587712), Float.intBitsToFloat(1109393408)).flexShrink(0.0f).stopPropagation(true).cursorStyle(ScenePctService.CursorStyle.POINTER).tooltip((module.isEnabled() ? "Disable " : "Enable ") + module.name());
            if (controlCompactService.value() != module.isEnabled()) {
                float f = f94dghijnnca.get(controlCompactService);
                controlCompactService.value(module.isEnabled());
                f94dghijnnca.set(controlCompactService, f);
                MotionAnimateService.animate(controlCompactService, f94dghijnnca, module.isEnabled() ? 1.0f : 0.0f, MaterialIsLightService.FAST_SPATIAL);
            }
            controlCompactService.onToggle(bool -> {
                if (bool.booleanValue() != module.isEnabled()) {
                    module.toggle();
                }
                m519jxejgk6g();
                mgf9hldmchqz();
            });
        }, new ComponentKeyService[0]).key(str);
    }

    
    private ComponentKeyService<?> mfstwwxrgcrh() {
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id("clickgui.settings-toolbar").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.search("clickgui.settings-search", "Find settings…", this.ffxnzn9t4ymi, str -> {
            this.ffxnzn9t4ymi = str.strip().toLowerCase(Locale.ROOT);
            mgf9hldmchqz();
            mccw3ifj84f2();
        }).props(interactiveSurfacePanel -> {
            interactiveSurfacePanel.height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f);
        }).children(MaterialTextService.icon("search", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1099956224)).margin(0.0f, Float.intBitsToFloat(1082130432));
        }), ComponentBoxService.node("settings-search", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            controlLetterSpacingService.id("clickgui.settings-search").materialSearch(true).fontFamily(MaterialIsLightService.FONT).fontSize(Float.intBitsToFloat(1096810496)).letterSpacing(Float.intBitsToFloat(1036831949)).placeholder("Find settings…").bgColor(0).textColor(MaterialIsLightService.ON_SURFACE).placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT).focusBorder(0).selectionColor(MaterialIsLightService.SELECTION).cornerRadius(0.0f).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(0.0f).onChanged(str2 -> {
                this.ffxnzn9t4ymi = str2.strip().toLowerCase(Locale.ROOT);
                mgf9hldmchqz();
                mccw3ifj84f2();
            });
            if (controlLetterSpacingService.focused()) {
                return;
            }
            controlLetterSpacingService.text(this.ffxnzn9t4ymi);
        }, new ComponentKeyService[0]).key("input"), m9o0yydzlndu("clickgui.settings-search.clear", "close", "Clear settings search", () -> {
            this.ffxnzn9t4ymi = "";
            ControlLetterSpacingService controlLetterSpacingService2 = (ControlLetterSpacingService) this.f2nu1diavlqn.findById("clickgui.settings-search");
            if (controlLetterSpacingService2 != null) {
                controlLetterSpacingService2.text("");
            }
            mgf9hldmchqz();
            mccw3ifj84f2();
        }).props(materialJoinedService -> {
            materialJoinedService.visible(!this.ffxnzn9t4ymi.isEmpty());
        }).key("clear")), mgtqng4c5k74("clickgui.settings-help", "Help", () -> {
            this.ffn57s6hi2n2 = !this.ffn57s6hi2n2;
            mgf9hldmchqz();
        }, this.ffn57s6hi2n2).props(materialJoinedService2 -> {
            materialJoinedService2.tooltip("Show setting descriptions");
        })}).key("settings-toolbar");
    }

    private ComponentKeyService<?> m2bf7ex0sca4() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(m4cqizbztt9h("", "All"));
        if (this.f48wg40ozief.settings().stream().anyMatch(moduleSetting -> {
            return moduleSetting.category().isEmpty() && moduleSetting.isVisible();
        })) {
            arrayList.add(m4cqizbztt9h("general", "General"));
        }
        for (ModuleNameService moduleNameService : this.f48wg40ozief.settingCategories()) {
            if (this.f48wg40ozief.settings().stream().anyMatch(moduleSetting2 -> {
                return moduleSetting2.isVisible() && ((Boolean) moduleSetting2.category().map(moduleNameService2 -> {
                    return Boolean.valueOf(((String) moduleNameService2.path().getFirst()).equals(moduleNameService.name()));
                }).orElse(false)).booleanValue();
            })) {
                arrayList.add(m4cqizbztt9h(moduleNameService.name(), moduleNameService.name()));
            }
        }
        String str = this.ffxnzn9t4ymi.isBlank() ? this.fbgj78te3qzw : "";
        return ComponentBoxService.node("material-choice-rail", MaterialSelectionSelector::new, materialSelectionSelector -> {
            materialSelectionSelector.selection("clickgui.settings-group." + (str.isEmpty() ? "all" : m1909encvz9z(str))).id("clickgui.settings-groups").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1115684864))).padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1098907648)).gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).scrollable(true).scrollbarWidth(0.0f).clip(true).flexShrink(0.0f);
        }, (ComponentKeyService[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("settings-groups");
    }

    private ComponentKeyService<?> m4cqizbztt9h(String str, String str2) {
        return m32zlilhw8fn("clickgui.settings-group." + (str.isEmpty() ? "all" : m1909encvz9z(str)), str2, () -> {
            if (this.fbgj78te3qzw.equals(str) && this.ffxnzn9t4ymi.isBlank()) {
                return;
            }
            String str3 = this.ffxnzn9t4ymi.isBlank() ? this.fbgj78te3qzw : "";
            ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn.findById("clickgui.settings-group." + (str3.isEmpty() ? "all" : m1909encvz9z(str3)));
            ScenePctService<?> scenePctServiceFindById2 = this.f2nu1diavlqn.findById("clickgui.settings-group." + (str.isEmpty() ? "all" : m1909encvz9z(str)));
            float fIntBitsToFloat = (scenePctServiceFindById == null || scenePctServiceFindById2 == null || scenePctServiceFindById2.computedX() >= scenePctServiceFindById.computedX()) ? Float.intBitsToFloat(1092616192) : Float.intBitsToFloat(-1054867456);
            this.fbgj78te3qzw = str;
            this.ffxnzn9t4ymi = "";
            mfahyvg0apdz();
            m4m81i8kd5x7(this.fgnnly9bll12);
            mgf9hldmchqz();
            mccw3ifj84f2();
            MaterialEnterService.enter(this.f2nu1diavlqn.findById("clickgui.settings"), fIntBitsToFloat, 0.0f);
        }, 0, (this.ffxnzn9t4ymi.isBlank() ? this.fbgj78te3qzw : "").equals(str) ? MaterialIsLightService.ON_SECONDARY_CONTAINER : MaterialIsLightService.ON_SURFACE_VARIANT, Float.intBitsToFloat(1098907648)).props(materialJoinedService -> {
            materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).padding(0.0f, Float.intBitsToFloat(1099956224));
        }).key(str.isEmpty() ? "all" : str);
    }

    private void mccw3ifj84f2() {
        ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn.findById("clickgui.settings");
        if (scenePctServiceFindById != null) {
            MotionAnimateService.cancel(scenePctServiceFindById, MotionColorsContainer.Floats.SCROLL_Y);
            scenePctServiceFindById.scrollTarget(0.0f).scrollY(0.0f);
        }
    }

    private boolean m5w7etjeu8ni(ModuleSetting<?> moduleSetting, String str) {
        if (!moduleSetting.isVisible()) {
            return false;
        }
        if (this.ffxnzn9t4ymi.isBlank()) {
            return this.fbgj78te3qzw.isEmpty() || str.equals(this.fbgj78te3qzw) || str.startsWith(this.fbgj78te3qzw + "/");
        }
        return (moduleSetting.name() + " " + moduleSetting.description() + " " + str).toLowerCase(Locale.ROOT).contains(this.ffxnzn9t4ymi);
    }

    private void m5oe4on6eb1f(List<ComponentKeyService<?>> list, ModuleNameService moduleNameService) {
        m3zqjvi22tyt(list, String.join(" / ", moduleNameService.path()), moduleNameService.description(), moduleNameService.qualifiedName(), moduleNameService.settings());
        Iterator<ModuleNameService> it = moduleNameService.children().iterator();
        while (it.hasNext()) {
            m5oe4on6eb1f(list, it.next());
        }
    }

    private void m3zqjvi22tyt(List<ComponentKeyService<?>> list, String str, String str2, String str3, List<ModuleSetting<?>> list2) {
        List<ModuleSetting<?>> list3 = list2.stream().filter(moduleSetting -> {
            return m5w7etjeu8ni(moduleSetting, str3);
        }).toList();
        if (list3.isEmpty()) {
            return;
        }
        boolean z = this.ffxnzn9t4ymi.isBlank() && this.f13slc68a70r.contains(m1vcviafemyn(this.f48wg40ozief) + ":" + str3);
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list3.size()) {
                list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.id("clickgui.section." + m1909encvz9z(str3)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f);
                }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("settings-section-heading", MaterialJoinedService::new, materialJoinedService -> {
                    materialJoinedService.colors(0, MaterialIsLightService.PRIMARY).shape(Float.intBitsToFloat(1096810496), Float.intBitsToFloat(1111490560)).id("clickgui.section." + m1909encvz9z(str3) + ".heading").height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(0.0f, Float.intBitsToFloat(1096810496)).gap(Float.intBitsToFloat(1092616192)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).tooltip(str2).onClick(() -> {
                        String str4 = m1vcviafemyn(this.f48wg40ozief) + ":" + str3;
                        if (!this.f13slc68a70r.remove(str4)) {
                            this.f13slc68a70r.add(str4);
                        }
                        mfahyvg0apdz();
                        m4m81i8kd5x7(this.fgnnly9bll12);
                        mgf9hldmchqz();
                    });
                }, ComponentBoxService.node("material-chevron", MaterialExpandedService::new, materialExpandedService -> {
                    materialExpandedService.expanded(!z).tintColor(MaterialIsLightService.PRIMARY).size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648)).flexShrink(0.0f);
                }, new ComponentKeyService[0]).key("chevron"), MaterialTextService.label(str, MaterialIsLightService.PRIMARY).props(sceneTextService -> {
                    sceneTextService.flex(1.0f).minWidth(0.0f);
                }), m1z6anxxnaah(Integer.toString(list3.size()), MaterialIsLightService.ON_SURFACE_VARIANT)).key("heading"), ComponentBoxService.node("material-disclosure", ExpandableMaterialPanel::new, expandableMaterialPanel -> {
                    expandableMaterialPanel.expanded(!z).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                }, ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                    layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f).gap(Float.intBitsToFloat(1086324736));
                }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                })).key("content")).key("rows")}).key("section:" + str3));
                return;
            } else {
                arrayList.add(ma1e4wa0bv7g(list3.get(i2), str3, i2 == 0, i2 == list3.size() - 1));
                i = i2 + 1;
            }
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private ComponentKeyService<?> ma1e4wa0bv7g(ModuleSetting<?> moduleSetting, String str, boolean z, boolean z2) throws MatchException {
        String strValueOf;
        ArrayList arrayList;
        int iMdnv1ub7rbpv;
        String str2;
        int iMdnv1ub7rbpv2;
        ComponentKeyService<?> componentKeyServiceKey;
        ModuleSetting.MultiSelect multiSelect;
        ModuleSetting.Mode mode;
        String str3 = "clickgui.setting." + m1909encvz9z(str) + "." + m1909encvz9z(moduleSetting.name());
        boolean zIsActive = moduleSetting.isActive();
        boolean z3 = this.fa9otqddo9xr && this.f5l40cw6jkd8 < Float.intBitsToFloat(1142947840) && !(moduleSetting instanceof ModuleSetting.Bool);
        ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
        arrayList2.add(MaterialTextService.text(moduleSetting.displayName(), Float.intBitsToFloat(1097859072), zIsActive ? MaterialIsLightService.ON_SURFACE : mdnv1ub7rbpv()).props(sceneTextService -> {
            sceneTextService.id(str3 + ".label").minWidth(0.0f).width(z3 ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.percent(Float.intBitsToFloat(1106247680))).flexShrink(0.0f).lineHeight(Float.intBitsToFloat(1102053376)).letterSpacing(Float.intBitsToFloat(1036831949)).wordWrap(true).maxLines(2);
        }).key("label"));
        if (moduleSetting instanceof ModuleSetting.Bool) {
            ModuleSetting.Bool bool = (ModuleSetting.Bool) moduleSetting;
            arrayList2.set(0, arrayList2.getFirst().props(scenePctService -> {
                scenePctService.width(LayoutOperationHandler.auto()).flex(1.0f);
            }));
            componentKeyServiceKey = ComponentBoxService.node("setting-switch", () -> {
                return new ControlSettingService(bool);
            }, controlSettingService -> {
                controlSettingService.material(true).compact(false).size(Float.intBitsToFloat(1113587712), Float.intBitsToFloat(1109393408)).flexShrink(0.0f).id(str3 + ".toggle");
                controlSettingService.disabledOpacity(1.0f).onToggle(bool2 -> {
                    mgf9hldmchqz();
                });
                controlSettingService.refreshState();
            }, new ComponentKeyService[0]).key("control");
        } else if (moduleSetting instanceof ModuleSetting.Number) {
            ModuleSetting.Number number = (ModuleSetting.Number) moduleSetting;
            if (number.usesExactInput()) {
                componentKeyServiceKey = miomwjm0c76f(str3, mfouoi8d08ic(number.get().floatValue(), number.step(), number.min()), moduleSetting);
            } else {
                componentKeyServiceKey = m37cgn6ox99s(number) ? mcmu5zr4sfaf(str3, number) : ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).minWidth(0.0f);
                }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("setting-slider", () -> {
                    return new NumberSettingControl(number);
                }, numberSettingControl -> {
                    numberSettingControl.material(true).compact(false).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(Float.intBitsToFloat(1111490560)).id(str3 + ".slider");
                    numberSettingControl.onPreview(f -> {
                        mc8xohiu9lth(str3, mfouoi8d08ic(f.floatValue(), number.step(), number.min()));
                    }).onChange(f2 -> {
                        mgf9hldmchqz();
                    });
                    numberSettingControl.refreshState();
                }, new ComponentKeyService[0]).key("slider"), miomwjm0c76f(str3, mfouoi8d08ic(number.get().floatValue(), number.step(), number.min()), number)}).key("control");
            }
        } else if (moduleSetting instanceof ModuleSetting.Range) {
            ModuleSetting.Range range = (ModuleSetting.Range) moduleSetting;
            componentKeyServiceKey = ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER).minWidth(0.0f);
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("setting-range", () -> {
                return new ControlOnPreviewHandler(range);
            }, controlOnPreviewHandler -> {
                controlOnPreviewHandler.material(true).compact(false).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(Float.intBitsToFloat(1111490560)).id(str3 + ".range-slider");
                controlOnPreviewHandler.onPreview((f, f2) -> {
                    mc8xohiu9lth(str3, mfouoi8d08ic(f.floatValue(), range.step(), range.min()) + " – " + mfouoi8d08ic(f2.floatValue(), range.step(), range.min()));
                }).onChange((f3, f4) -> {
                    mgf9hldmchqz();
                });
                controlOnPreviewHandler.refreshState();
            }, new ComponentKeyService[0]).key("slider"), miomwjm0c76f(str3, mfouoi8d08ic(range.low(), range.step(), range.min()) + " – " + mfouoi8d08ic(range.high(), range.step(), range.min()), range)}).key("control");
        } else if (moduleSetting instanceof ModuleSetting.Mode) {
            ModuleSetting.Mode mode2 = (ModuleSetting.Mode) moduleSetting;
            if (mode2.hasPreviews()) {
                componentKeyServiceKey = maucq9txbp10(str3, mode2);
            } else if (moduleSetting instanceof ModuleSetting.Mode) {
                mode = (ModuleSetting.Mode) moduleSetting;
                if (mckb6ayhv749(mode.options(), false)) {
                    componentKeyServiceKey = mdvf7ejopn9c(str3, mode, mode.options(), false);
                } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                    multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
                    if (mckb6ayhv749(multiSelect.options(), true)) {
                        componentKeyServiceKey = mdvf7ejopn9c(str3, multiSelect, multiSelect.options(), true);
                    } else if (moduleSetting instanceof ModuleLayoutService) {
                        ModuleLayoutService moduleLayoutService = (ModuleLayoutService) moduleSetting;
                        componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService, () -> {
                            m2nqvpgnuas5(moduleLayoutService);
                        });
                    } else if (moduleSetting instanceof ModuleSetting.Text) {
                        ModuleSetting.Text text = (ModuleSetting.Text) moduleSetting;
                        componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService -> {
                            MaterialTextService.input(controlLetterSpacingService);
                            controlLetterSpacingService.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                            if (!controlLetterSpacingService.focused()) {
                                controlLetterSpacingService.text(text.get());
                            }
                            Objects.requireNonNull(text);
                            controlLetterSpacingService.onSubmit(text::set).onUnfocus(() -> {
                                if (text.isActive()) {
                                    text.set(controlLetterSpacingService.text());
                                }
                            });
                        }, new ComponentKeyService[0]).key("control");
                    } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                        ModuleSetting.Keybind keybind = (ModuleSetting.Keybind) moduleSetting;
                        componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService -> {
                            ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService.material(true).keyNameFn((v0) -> {
                                return m60hl91212ms(v0);
                            }).keyCode(keybind.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                            Objects.requireNonNull(keybind);
                            controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                                keybind.set(v1);
                            }).chooser(() -> {
                                m9545dt2cnyo(keybind);
                            }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                        }, new ComponentKeyService[0]).key("control");
                    } else {
                        if (moduleSetting instanceof ModuleEntriesService) {
                            ModuleEntriesService moduleEntriesService = (ModuleEntriesService) moduleSetting;
                            strValueOf = moduleEntriesService.selected().edition() + " / " + moduleEntriesService.selected().label();
                        } else if (moduleSetting instanceof ModuleSetting.Mode) {
                            strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                        } else if (moduleSetting instanceof ModuleSetting.Color) {
                            strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                            ModuleSetting.MultiSelect multiSelect2 = (ModuleSetting.MultiSelect) moduleSetting;
                            strValueOf = multiSelect2.get().size() + " of " + multiSelect2.options().length + " selected";
                        } else if (moduleSetting instanceof ModuleSetting.Curve) {
                            strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                        } else {
                            strValueOf = String.valueOf(moduleSetting.get());
                        }
                        String str4 = strValueOf;
                        arrayList = new ArrayList();
                        if (moduleSetting instanceof ModuleSetting.Color) {
                            ModuleSetting.Color color = (ModuleSetting.Color) moduleSetting;
                            arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService -> {
                                materialColorService.color(color.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                            }, new ComponentKeyService[0]));
                        }
                        if (zIsActive) {
                            iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                        } else {
                            iMdnv1ub7rbpv = mdnv1ub7rbpv();
                        }
                        arrayList.add(m1z6anxxnaah(str4, iMdnv1ub7rbpv).props(sceneTextService2 -> {
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
                            iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                        }
                        arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService -> {
                            sceneSrcService.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                        }));
                        componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService -> {
                            materialJoinedService.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                            materialJoinedService.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                                m2nqvpgnuas5(moduleSetting);
                            });
                        }, (ComponentKeyService[]) arrayList.toArray(i -> {
                            return new ComponentKeyService[i];
                        })).key("control");
                    }
                } else if (moduleSetting instanceof ModuleLayoutService) {
                    ModuleLayoutService moduleLayoutService2 = (ModuleLayoutService) moduleSetting;
                    componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService2, () -> {
                        m2nqvpgnuas5(moduleLayoutService2);
                    });
                } else if (moduleSetting instanceof ModuleSetting.Text) {
                    ModuleSetting.Text text2 = (ModuleSetting.Text) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService2 -> {
                        MaterialTextService.input(controlLetterSpacingService2);
                        controlLetterSpacingService2.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text2.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                        if (!controlLetterSpacingService2.focused()) {
                            controlLetterSpacingService2.text(text2.get());
                        }
                        Objects.requireNonNull(text2);
                        controlLetterSpacingService2.onSubmit(text2::set).onUnfocus(() -> {
                            if (text2.isActive()) {
                                text2.set(controlLetterSpacingService2.text());
                            }
                        });
                    }, new ComponentKeyService[0]).key("control");
                } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                    ModuleSetting.Keybind keybind2 = (ModuleSetting.Keybind) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService2 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService2.material(true).keyNameFn((v0) -> {
                            return m60hl91212ms(v0);
                        }).keyCode(keybind2.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind2);
                        controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                            keybind2.set(v1);
                        }).chooser(() -> {
                            m9545dt2cnyo(keybind2);
                        }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    }, new ComponentKeyService[0]).key("control");
                } else {
                    if (moduleSetting instanceof ModuleEntriesService) {
                        ModuleEntriesService moduleEntriesService2 = (ModuleEntriesService) moduleSetting;
                        strValueOf = moduleEntriesService2.selected().edition() + " / " + moduleEntriesService2.selected().label();
                    } else if (moduleSetting instanceof ModuleSetting.Mode) {
                        strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                    } else if (moduleSetting instanceof ModuleSetting.Color) {
                        strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                    } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                        ModuleSetting.MultiSelect multiSelect3 = (ModuleSetting.MultiSelect) moduleSetting;
                        strValueOf = multiSelect3.get().size() + " of " + multiSelect3.options().length + " selected";
                    } else if (moduleSetting instanceof ModuleSetting.Curve) {
                        strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                    } else {
                        strValueOf = String.valueOf(moduleSetting.get());
                    }
                    String str5 = strValueOf;
                    arrayList = new ArrayList();
                    if (moduleSetting instanceof ModuleSetting.Color) {
                        ModuleSetting.Color color2 = (ModuleSetting.Color) moduleSetting;
                        arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService2 -> {
                            materialColorService2.color(color2.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                        }, new ComponentKeyService[0]));
                    }
                    if (zIsActive) {
                        iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                    } else {
                        iMdnv1ub7rbpv = mdnv1ub7rbpv();
                    }
                    arrayList.add(m1z6anxxnaah(str5, iMdnv1ub7rbpv).props(sceneTextService3 -> {
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
                        iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                    }
                    arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService2 -> {
                        sceneSrcService2.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
                    componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService2 -> {
                        materialJoinedService2.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                        materialJoinedService2.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                            m2nqvpgnuas5(moduleSetting);
                        });
                    }, (ComponentKeyService[]) arrayList.toArray(i2 -> {
                        return new ComponentKeyService[i2];
                    })).key("control");
                }
            } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
                if (mckb6ayhv749(multiSelect.options(), true)) {
                    componentKeyServiceKey = mdvf7ejopn9c(str3, multiSelect, multiSelect.options(), true);
                } else if (moduleSetting instanceof ModuleLayoutService) {
                    ModuleLayoutService moduleLayoutService3 = (ModuleLayoutService) moduleSetting;
                    componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService3, () -> {
                        m2nqvpgnuas5(moduleLayoutService3);
                    });
                } else if (moduleSetting instanceof ModuleSetting.Text) {
                    ModuleSetting.Text text3 = (ModuleSetting.Text) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService3 -> {
                        MaterialTextService.input(controlLetterSpacingService3);
                        controlLetterSpacingService3.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text3.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                        if (!controlLetterSpacingService3.focused()) {
                            controlLetterSpacingService3.text(text3.get());
                        }
                        Objects.requireNonNull(text3);
                        controlLetterSpacingService3.onSubmit(text3::set).onUnfocus(() -> {
                            if (text3.isActive()) {
                                text3.set(controlLetterSpacingService3.text());
                            }
                        });
                    }, new ComponentKeyService[0]).key("control");
                } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                    ModuleSetting.Keybind keybind3 = (ModuleSetting.Keybind) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService3 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService3.material(true).keyNameFn((v0) -> {
                            return m60hl91212ms(v0);
                        }).keyCode(keybind3.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind3);
                        controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                            keybind3.set(v1);
                        }).chooser(() -> {
                            m9545dt2cnyo(keybind3);
                        }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    }, new ComponentKeyService[0]).key("control");
                } else {
                    if (moduleSetting instanceof ModuleEntriesService) {
                        ModuleEntriesService moduleEntriesService3 = (ModuleEntriesService) moduleSetting;
                        strValueOf = moduleEntriesService3.selected().edition() + " / " + moduleEntriesService3.selected().label();
                    } else if (moduleSetting instanceof ModuleSetting.Mode) {
                        strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                    } else if (moduleSetting instanceof ModuleSetting.Color) {
                        strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                    } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                        ModuleSetting.MultiSelect multiSelect4 = (ModuleSetting.MultiSelect) moduleSetting;
                        strValueOf = multiSelect4.get().size() + " of " + multiSelect4.options().length + " selected";
                    } else if (moduleSetting instanceof ModuleSetting.Curve) {
                        strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                    } else {
                        strValueOf = String.valueOf(moduleSetting.get());
                    }
                    String str6 = strValueOf;
                    arrayList = new ArrayList();
                    if (moduleSetting instanceof ModuleSetting.Color) {
                        ModuleSetting.Color color3 = (ModuleSetting.Color) moduleSetting;
                        arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService3 -> {
                            materialColorService3.color(color3.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                        }, new ComponentKeyService[0]));
                    }
                    if (zIsActive) {
                        iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                    } else {
                        iMdnv1ub7rbpv = mdnv1ub7rbpv();
                    }
                    arrayList.add(m1z6anxxnaah(str6, iMdnv1ub7rbpv).props(sceneTextService4 -> {
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
                        iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                    }
                    arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService3 -> {
                        sceneSrcService3.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
                    componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService3 -> {
                        materialJoinedService3.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                        materialJoinedService3.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                            m2nqvpgnuas5(moduleSetting);
                        });
                    }, (ComponentKeyService[]) arrayList.toArray(i3 -> {
                        return new ComponentKeyService[i3];
                    })).key("control");
                }
            } else if (moduleSetting instanceof ModuleLayoutService) {
                ModuleLayoutService moduleLayoutService4 = (ModuleLayoutService) moduleSetting;
                componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService4, () -> {
                    m2nqvpgnuas5(moduleLayoutService4);
                });
            } else if (moduleSetting instanceof ModuleSetting.Text) {
                ModuleSetting.Text text4 = (ModuleSetting.Text) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService4 -> {
                    MaterialTextService.input(controlLetterSpacingService4);
                    controlLetterSpacingService4.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text4.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                    if (!controlLetterSpacingService4.focused()) {
                        controlLetterSpacingService4.text(text4.get());
                    }
                    Objects.requireNonNull(text4);
                    controlLetterSpacingService4.onSubmit(text4::set).onUnfocus(() -> {
                        if (text4.isActive()) {
                            text4.set(controlLetterSpacingService4.text());
                        }
                    });
                }, new ComponentKeyService[0]).key("control");
            } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                ModuleSetting.Keybind keybind4 = (ModuleSetting.Keybind) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService4 -> {
                    ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService4.material(true).keyNameFn((v0) -> {
                        return m60hl91212ms(v0);
                    }).keyCode(keybind4.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                    Objects.requireNonNull(keybind4);
                    controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                        keybind4.set(v1);
                    }).chooser(() -> {
                        m9545dt2cnyo(keybind4);
                    }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                }, new ComponentKeyService[0]).key("control");
            } else {
                if (moduleSetting instanceof ModuleEntriesService) {
                    ModuleEntriesService moduleEntriesService4 = (ModuleEntriesService) moduleSetting;
                    strValueOf = moduleEntriesService4.selected().edition() + " / " + moduleEntriesService4.selected().label();
                } else if (moduleSetting instanceof ModuleSetting.Mode) {
                    strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                } else if (moduleSetting instanceof ModuleSetting.Color) {
                    strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                    ModuleSetting.MultiSelect multiSelect5 = (ModuleSetting.MultiSelect) moduleSetting;
                    strValueOf = multiSelect5.get().size() + " of " + multiSelect5.options().length + " selected";
                } else if (moduleSetting instanceof ModuleSetting.Curve) {
                    strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                } else {
                    strValueOf = String.valueOf(moduleSetting.get());
                }
                String str7 = strValueOf;
                arrayList = new ArrayList();
                if (moduleSetting instanceof ModuleSetting.Color) {
                    ModuleSetting.Color color4 = (ModuleSetting.Color) moduleSetting;
                    arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService4 -> {
                        materialColorService4.color(color4.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                    }, new ComponentKeyService[0]));
                }
                if (zIsActive) {
                    iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                } else {
                    iMdnv1ub7rbpv = mdnv1ub7rbpv();
                }
                arrayList.add(m1z6anxxnaah(str7, iMdnv1ub7rbpv).props(sceneTextService5 -> {
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
                    iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                }
                arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService4 -> {
                    sceneSrcService4.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                }));
                componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService4 -> {
                    materialJoinedService4.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                    materialJoinedService4.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                        m2nqvpgnuas5(moduleSetting);
                    });
                }, (ComponentKeyService[]) arrayList.toArray(i4 -> {
                    return new ComponentKeyService[i4];
                })).key("control");
            }
        } else if (moduleSetting instanceof ModuleSetting.Mode) {
            mode = (ModuleSetting.Mode) moduleSetting;
            if (mckb6ayhv749(mode.options(), false)) {
                componentKeyServiceKey = mdvf7ejopn9c(str3, mode, mode.options(), false);
            } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
                if (mckb6ayhv749(multiSelect.options(), true)) {
                    componentKeyServiceKey = mdvf7ejopn9c(str3, multiSelect, multiSelect.options(), true);
                } else if (moduleSetting instanceof ModuleLayoutService) {
                    ModuleLayoutService moduleLayoutService5 = (ModuleLayoutService) moduleSetting;
                    componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService5, () -> {
                        m2nqvpgnuas5(moduleLayoutService5);
                    });
                } else if (moduleSetting instanceof ModuleSetting.Text) {
                    ModuleSetting.Text text5 = (ModuleSetting.Text) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService5 -> {
                        MaterialTextService.input(controlLetterSpacingService5);
                        controlLetterSpacingService5.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text5.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                        if (!controlLetterSpacingService5.focused()) {
                            controlLetterSpacingService5.text(text5.get());
                        }
                        Objects.requireNonNull(text5);
                        controlLetterSpacingService5.onSubmit(text5::set).onUnfocus(() -> {
                            if (text5.isActive()) {
                                text5.set(controlLetterSpacingService5.text());
                            }
                        });
                    }, new ComponentKeyService[0]).key("control");
                } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                    ModuleSetting.Keybind keybind5 = (ModuleSetting.Keybind) moduleSetting;
                    componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService5 -> {
                        ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService5.material(true).keyNameFn((v0) -> {
                            return m60hl91212ms(v0);
                        }).keyCode(keybind5.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                        Objects.requireNonNull(keybind5);
                        controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                            keybind5.set(v1);
                        }).chooser(() -> {
                            m9545dt2cnyo(keybind5);
                        }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                    }, new ComponentKeyService[0]).key("control");
                } else {
                    if (moduleSetting instanceof ModuleEntriesService) {
                        ModuleEntriesService moduleEntriesService5 = (ModuleEntriesService) moduleSetting;
                        strValueOf = moduleEntriesService5.selected().edition() + " / " + moduleEntriesService5.selected().label();
                    } else if (moduleSetting instanceof ModuleSetting.Mode) {
                        strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                    } else if (moduleSetting instanceof ModuleSetting.Color) {
                        strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                    } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                        ModuleSetting.MultiSelect multiSelect6 = (ModuleSetting.MultiSelect) moduleSetting;
                        strValueOf = multiSelect6.get().size() + " of " + multiSelect6.options().length + " selected";
                    } else if (moduleSetting instanceof ModuleSetting.Curve) {
                        strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                    } else {
                        strValueOf = String.valueOf(moduleSetting.get());
                    }
                    String str8 = strValueOf;
                    arrayList = new ArrayList();
                    if (moduleSetting instanceof ModuleSetting.Color) {
                        ModuleSetting.Color color5 = (ModuleSetting.Color) moduleSetting;
                        arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService5 -> {
                            materialColorService5.color(color5.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                        }, new ComponentKeyService[0]));
                    }
                    if (zIsActive) {
                        iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                    } else {
                        iMdnv1ub7rbpv = mdnv1ub7rbpv();
                    }
                    arrayList.add(m1z6anxxnaah(str8, iMdnv1ub7rbpv).props(sceneTextService6 -> {
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
                        iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                    }
                    arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService5 -> {
                        sceneSrcService5.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                    }));
                    componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService5 -> {
                        materialJoinedService5.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                        materialJoinedService5.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                            m2nqvpgnuas5(moduleSetting);
                        });
                    }, (ComponentKeyService[]) arrayList.toArray(i5 -> {
                        return new ComponentKeyService[i5];
                    })).key("control");
                }
            } else if (moduleSetting instanceof ModuleLayoutService) {
                ModuleLayoutService moduleLayoutService6 = (ModuleLayoutService) moduleSetting;
                componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService6, () -> {
                    m2nqvpgnuas5(moduleLayoutService6);
                });
            } else if (moduleSetting instanceof ModuleSetting.Text) {
                ModuleSetting.Text text6 = (ModuleSetting.Text) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService6 -> {
                    MaterialTextService.input(controlLetterSpacingService6);
                    controlLetterSpacingService6.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text6.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                    if (!controlLetterSpacingService6.focused()) {
                        controlLetterSpacingService6.text(text6.get());
                    }
                    Objects.requireNonNull(text6);
                    controlLetterSpacingService6.onSubmit(text6::set).onUnfocus(() -> {
                        if (text6.isActive()) {
                            text6.set(controlLetterSpacingService6.text());
                        }
                    });
                }, new ComponentKeyService[0]).key("control");
            } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                ModuleSetting.Keybind keybind6 = (ModuleSetting.Keybind) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService6 -> {
                    ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService6.material(true).keyNameFn((v0) -> {
                        return m60hl91212ms(v0);
                    }).keyCode(keybind6.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                    Objects.requireNonNull(keybind6);
                    controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                        keybind6.set(v1);
                    }).chooser(() -> {
                        m9545dt2cnyo(keybind6);
                    }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                }, new ComponentKeyService[0]).key("control");
            } else {
                if (moduleSetting instanceof ModuleEntriesService) {
                    ModuleEntriesService moduleEntriesService6 = (ModuleEntriesService) moduleSetting;
                    strValueOf = moduleEntriesService6.selected().edition() + " / " + moduleEntriesService6.selected().label();
                } else if (moduleSetting instanceof ModuleSetting.Mode) {
                    strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                } else if (moduleSetting instanceof ModuleSetting.Color) {
                    strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                    ModuleSetting.MultiSelect multiSelect7 = (ModuleSetting.MultiSelect) moduleSetting;
                    strValueOf = multiSelect7.get().size() + " of " + multiSelect7.options().length + " selected";
                } else if (moduleSetting instanceof ModuleSetting.Curve) {
                    strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                } else {
                    strValueOf = String.valueOf(moduleSetting.get());
                }
                String str9 = strValueOf;
                arrayList = new ArrayList();
                if (moduleSetting instanceof ModuleSetting.Color) {
                    ModuleSetting.Color color6 = (ModuleSetting.Color) moduleSetting;
                    arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService6 -> {
                        materialColorService6.color(color6.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                    }, new ComponentKeyService[0]));
                }
                if (zIsActive) {
                    iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                } else {
                    iMdnv1ub7rbpv = mdnv1ub7rbpv();
                }
                arrayList.add(m1z6anxxnaah(str9, iMdnv1ub7rbpv).props(sceneTextService7 -> {
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
                    iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                }
                arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService6 -> {
                    sceneSrcService6.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                }));
                componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService6 -> {
                    materialJoinedService6.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                    materialJoinedService6.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                        m2nqvpgnuas5(moduleSetting);
                    });
                }, (ComponentKeyService[]) arrayList.toArray(i6 -> {
                    return new ComponentKeyService[i6];
                })).key("control");
            }
        } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
            multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
            if (mckb6ayhv749(multiSelect.options(), true)) {
                componentKeyServiceKey = mdvf7ejopn9c(str3, multiSelect, multiSelect.options(), true);
            } else if (moduleSetting instanceof ModuleLayoutService) {
                ModuleLayoutService moduleLayoutService7 = (ModuleLayoutService) moduleSetting;
                componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService7, () -> {
                    m2nqvpgnuas5(moduleLayoutService7);
                });
            } else if (moduleSetting instanceof ModuleSetting.Text) {
                ModuleSetting.Text text7 = (ModuleSetting.Text) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService7 -> {
                    MaterialTextService.input(controlLetterSpacingService7);
                    controlLetterSpacingService7.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text7.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                    if (!controlLetterSpacingService7.focused()) {
                        controlLetterSpacingService7.text(text7.get());
                    }
                    Objects.requireNonNull(text7);
                    controlLetterSpacingService7.onSubmit(text7::set).onUnfocus(() -> {
                        if (text7.isActive()) {
                            text7.set(controlLetterSpacingService7.text());
                        }
                    });
                }, new ComponentKeyService[0]).key("control");
            } else if (moduleSetting instanceof ModuleSetting.Keybind) {
                ModuleSetting.Keybind keybind7 = (ModuleSetting.Keybind) moduleSetting;
                componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService7 -> {
                    ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService7.material(true).keyNameFn((v0) -> {
                        return m60hl91212ms(v0);
                    }).keyCode(keybind7.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                    Objects.requireNonNull(keybind7);
                    controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                        keybind7.set(v1);
                    }).chooser(() -> {
                        m9545dt2cnyo(keybind7);
                    }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
                }, new ComponentKeyService[0]).key("control");
            } else {
                if (moduleSetting instanceof ModuleEntriesService) {
                    ModuleEntriesService moduleEntriesService7 = (ModuleEntriesService) moduleSetting;
                    strValueOf = moduleEntriesService7.selected().edition() + " / " + moduleEntriesService7.selected().label();
                } else if (moduleSetting instanceof ModuleSetting.Mode) {
                    strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
                } else if (moduleSetting instanceof ModuleSetting.Color) {
                    strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
                } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                    ModuleSetting.MultiSelect multiSelect8 = (ModuleSetting.MultiSelect) moduleSetting;
                    strValueOf = multiSelect8.get().size() + " of " + multiSelect8.options().length + " selected";
                } else if (moduleSetting instanceof ModuleSetting.Curve) {
                    strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
                } else {
                    strValueOf = String.valueOf(moduleSetting.get());
                }
                String str10 = strValueOf;
                arrayList = new ArrayList();
                if (moduleSetting instanceof ModuleSetting.Color) {
                    ModuleSetting.Color color7 = (ModuleSetting.Color) moduleSetting;
                    arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService7 -> {
                        materialColorService7.color(color7.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                    }, new ComponentKeyService[0]));
                }
                if (zIsActive) {
                    iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
                } else {
                    iMdnv1ub7rbpv = mdnv1ub7rbpv();
                }
                arrayList.add(m1z6anxxnaah(str10, iMdnv1ub7rbpv).props(sceneTextService8 -> {
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
                    iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
                }
                arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService7 -> {
                    sceneSrcService7.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                }));
                componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService7 -> {
                    materialJoinedService7.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                    materialJoinedService7.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                        m2nqvpgnuas5(moduleSetting);
                    });
                }, (ComponentKeyService[]) arrayList.toArray(i7 -> {
                    return new ComponentKeyService[i7];
                })).key("control");
            }
        } else if (moduleSetting instanceof ModuleLayoutService) {
            ModuleLayoutService moduleLayoutService8 = (ModuleLayoutService) moduleSetting;
            componentKeyServiceKey = InventoryRenderer.preview(str3, moduleLayoutService8, () -> {
                m2nqvpgnuas5(moduleLayoutService8);
            });
        } else if (moduleSetting instanceof ModuleSetting.Text) {
            ModuleSetting.Text text8 = (ModuleSetting.Text) moduleSetting;
            componentKeyServiceKey = ComponentBoxService.node("setting-text", ControlLetterSpacingService::new, controlLetterSpacingService8 -> {
                MaterialTextService.input(controlLetterSpacingService8);
                controlLetterSpacingService8.fontSize(Float.intBitsToFloat(1097859072)).letterSpacing(Float.intBitsToFloat(1036831949)).id(str3 + ".text-input").maxLength(text8.maxLength()).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(0.0f).interactive(zIsActive).pointerEvents(zIsActive);
                if (!controlLetterSpacingService8.focused()) {
                    controlLetterSpacingService8.text(text8.get());
                }
                Objects.requireNonNull(text8);
                controlLetterSpacingService8.onSubmit(text8::set).onUnfocus(() -> {
                    if (text8.isActive()) {
                        text8.set(controlLetterSpacingService8.text());
                    }
                });
            }, new ComponentKeyService[0]).key("control");
        } else if (moduleSetting instanceof ModuleSetting.Keybind) {
            ModuleSetting.Keybind keybind8 = (ModuleSetting.Keybind) moduleSetting;
            componentKeyServiceKey = ComponentBoxService.node("setting-keybind", ControlGetAnimPropertyService::new, controlGetAnimPropertyService8 -> {
                ControlGetAnimPropertyService controlGetAnimPropertyServicePointerEvents = controlGetAnimPropertyService8.material(true).keyNameFn((v0) -> {
                    return m60hl91212ms(v0);
                }).keyCode(keybind8.get().intValue()).fontSize(Float.intBitsToFloat(1096810496)).cornerRadius(Float.intBitsToFloat(1094713344)).bgColor(MaterialIsLightService.SECONDARY_CONTAINER).textColor(MaterialIsLightService.ON_SECONDARY_CONTAINER).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).id(str3 + ".keybind").interactive(zIsActive).pointerEvents(zIsActive);
                Objects.requireNonNull(keybind8);
                controlGetAnimPropertyServicePointerEvents.onChange((v1) -> {
                    keybind8.set(v1);
                }).chooser(() -> {
                    m9545dt2cnyo(keybind8);
                }).tooltip("Choose a key on the rendered keyboard · Right-click to unbind");
            }, new ComponentKeyService[0]).key("control");
        } else {
            if (moduleSetting instanceof ModuleEntriesService) {
                ModuleEntriesService moduleEntriesService8 = (ModuleEntriesService) moduleSetting;
                strValueOf = moduleEntriesService8.selected().edition() + " / " + moduleEntriesService8.selected().label();
            } else if (moduleSetting instanceof ModuleSetting.Mode) {
                strValueOf = ((ModuleSetting.Mode) moduleSetting).get();
            } else if (moduleSetting instanceof ModuleSetting.Color) {
                strValueOf = String.format(Locale.ROOT, "#%08X", ((ModuleSetting.Color) moduleSetting).get());
            } else if (moduleSetting instanceof ModuleSetting.MultiSelect) {
                ModuleSetting.MultiSelect multiSelect9 = (ModuleSetting.MultiSelect) moduleSetting;
                strValueOf = multiSelect9.get().size() + " of " + multiSelect9.options().length + " selected";
            } else if (moduleSetting instanceof ModuleSetting.Curve) {
                strValueOf = m9xb6xboaqgo(((ModuleSetting.Curve) moduleSetting).get().type());
            } else {
                strValueOf = String.valueOf(moduleSetting.get());
            }
            String str11 = strValueOf;
            arrayList = new ArrayList();
            if (moduleSetting instanceof ModuleSetting.Color) {
                ModuleSetting.Color color8 = (ModuleSetting.Color) moduleSetting;
                arrayList.add(ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService8 -> {
                    materialColorService8.color(color8.get().intValue()).radius(Float.intBitsToFloat(1086324736)).size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376)).flexShrink(0.0f).pointerEvents(false);
                }, new ComponentKeyService[0]));
            }
            if (zIsActive) {
                iMdnv1ub7rbpv = MaterialIsLightService.ON_SECONDARY_CONTAINER;
            } else {
                iMdnv1ub7rbpv = mdnv1ub7rbpv();
            }
            arrayList.add(m1z6anxxnaah(str11, iMdnv1ub7rbpv).props(sceneTextService9 -> {
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
                iMdnv1ub7rbpv2 = mdnv1ub7rbpv();
            }
            arrayList.add(MaterialTextService.icon(str2, iMdnv1ub7rbpv2).props(sceneSrcService8 -> {
                sceneSrcService8.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
            }));
            componentKeyServiceKey = ComponentBoxService.node("setting-choice", MaterialJoinedService::new, materialJoinedService8 -> {
                materialJoinedService8.colors(MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(zIsActive);
                materialJoinedService8.id(str3 + ".edit").height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1090519040)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).onClick(() -> {
                    m2nqvpgnuas5(moduleSetting);
                });
            }, (ComponentKeyService[]) arrayList.toArray(i8 -> {
                return new ComponentKeyService[i8];
            })).key("control");
        }
        arrayList2.add(componentKeyServiceKey.props(scenePctService2 -> {
            if (moduleSetting instanceof ModuleSetting.Bool) {
                return;
            }
            scenePctService2.width(z3 ? LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)) : LayoutOperationHandler.auto()).flex(z3 ? 0.0f : 1.0f).minWidth(0.0f);
        }));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(ComponentBoxService.box(layoutContainerNode3 -> {
            layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).direction(z3 ? ScenePctService.Direction.COLUMN : ScenePctService.Direction.ROW).gap(z3 ? Float.intBitsToFloat(1094713344) : Float.intBitsToFloat(1101004800)).align(z3 ? ScenePctService.Align.STRETCH : ScenePctService.Align.CENTER);
        }, (ComponentKeyService[]) arrayList2.toArray(i9 -> {
            return new ComponentKeyService[i9];
        })).key("main"));
        if (this.ffn57s6hi2n2 && !moduleSetting.description().isBlank()) {
            arrayList3.add(MaterialTextService.text(moduleSetting.description(), Float.intBitsToFloat(1095761920), zIsActive ? MaterialIsLightService.ON_SURFACE_VARIANT : mdnv1ub7rbpv()).props(sceneTextService10 -> {
                sceneTextService10.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).lineHeight(Float.intBitsToFloat(1101004800)).wordWrap(true);
            }).key("help"));
        }
        return ComponentBoxService.node("material-setting-surface", MaterialResponsiveService::new, materialResponsiveService -> {
            Runnable runnable;
            SceneCornerRadiusService sceneCornerRadiusService = materialResponsiveService.surface(MaterialIsLightService.SURFACE_CONTAINER).responsive(zIsActive).id(str3).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(Float.intBitsToFloat(1116733440)).flexShrink(0.0f).direction(ScenePctService.Direction.COLUMN).padding(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1099956224)).gap(Float.intBitsToFloat(1092616192)).cornerRadiusTL(z ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040)).cornerRadiusTR(z ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040)).cornerRadiusBR(z2 ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040)).cornerRadiusBL(z2 ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1090519040)).tooltip(moduleSetting.name() + (moduleSetting.description().isBlank() ? "" : " · " + moduleSetting.description()));
            if (moduleSetting instanceof ModuleSetting.Bool) {
                ModuleSetting.Bool bool2 = (ModuleSetting.Bool) moduleSetting;
                if (zIsActive) {
                    runnable = () -> {
                        bool2.toggle();
                        mgf9hldmchqz();
                    };
                } else {
                    runnable = null;
                }
            } else {
                runnable = null;
            }
            sceneCornerRadiusService.onClick(runnable).interactive(zIsActive).cursorStyle(((moduleSetting instanceof ModuleSetting.Bool) && zIsActive) ? ScenePctService.CursorStyle.POINTER : ScenePctService.CursorStyle.DEFAULT);
        }, (ComponentKeyService[]) arrayList3.toArray(i10 -> {
            return new ComponentKeyService[i10];
        })).key(str3);
    }

    private static boolean m37cgn6ox99s(ModuleSetting.Number number) {
        return number.step() >= 1.0f && ((double) number.step()) == Math.rint((double) number.step()) && ((double) number.min()) == Math.rint((double) number.min()) && (number.max() - number.min()) / number.step() <= Float.intBitsToFloat(1094713344);
    }

    private ComponentKeyService<?> mcmu5zr4sfaf(String str, ModuleSetting.Number number) {
        return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id(str + ".counter").justify(ScenePctService.Justify.END).gap(2.0f).align(ScenePctService.Align.CENTER);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{mgtqng4c5k74(str + ".decrement", "−", () -> {
            if (number.isActive()) {
                number.set(Float.valueOf(number.get().floatValue() - number.step()));
                mgf9hldmchqz();
            }
        }, false).props(materialJoinedService -> {
            materialJoinedService.available(number.isActive() && number.get().floatValue() > number.min()).minWidth(Float.intBitsToFloat(1107296256)).padding(0.0f, Float.intBitsToFloat(1090519040)).tooltip("Decrease by " + mfouoi8d08ic(number.step(), number.step(), 0.0f));
        }), miomwjm0c76f(str, mfouoi8d08ic(number.get().floatValue(), number.step(), number.min()), number), mgtqng4c5k74(str + ".increment", "+", () -> {
            if (number.isActive()) {
                number.set(Float.valueOf(number.get().floatValue() + number.step()));
                mgf9hldmchqz();
            }
        }, false).props(materialJoinedService2 -> {
            materialJoinedService2.available(number.isActive() && number.get().floatValue() < number.max()).minWidth(Float.intBitsToFloat(1107296256)).padding(0.0f, Float.intBitsToFloat(1090519040)).tooltip("Increase by " + mfouoi8d08ic(number.step(), number.step(), 0.0f));
        })}).key("control");
    }

    private ComponentKeyService<?> miomwjm0c76f(String str, String str2, ModuleSetting<?> moduleSetting) {
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
        Consumer<MaterialJoinedService> consumer = materialJoinedService -> {
            materialJoinedService.colors(MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.PRIMARY).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832)).available(moduleSetting.isActive());
            materialJoinedService.id(str + ".exact").size(LayoutOperationHandler.px(fMax), LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(fMax).padding(0.0f, Float.intBitsToFloat(1094713344)).flexShrink(0.0f).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER).tooltip("Enter an exact value").onClick(() -> {
                m2nqvpgnuas5(moduleSetting);
            });
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[1];
        componentKeyServiceArr[0] = m1z6anxxnaah(str2, moduleSetting.isActive() ? MaterialIsLightService.PRIMARY : mdnv1ub7rbpv()).props(sceneTextService -> {
            sceneTextService.id(str + ".value");
        });
        return ComponentBoxService.node("material-value", supplier, consumer, componentKeyServiceArr).key("value");
    }

    private ComponentKeyService<?> maucq9txbp10(String str, ModuleSetting.Mode mode) {
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
            Consumer<MaterialJoinedService> consumer = materialJoinedService -> {
                materialJoinedService.colors(zEquals ? MaterialIsLightService.PRIMARY_CONTAINER : MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.ON_SURFACE).shape(zEquals ? Float.intBitsToFloat(1098907648) : Float.intBitsToFloat(1092616192), Float.intBitsToFloat(1113587712)).available(mode.isActive());
                materialJoinedService.id(str + ".option." + m1909encvz9z(str2)).height(LayoutOperationHandler.px(Float.intBitsToFloat(1114636288))).flex(1.0f).minWidth(0.0f).padding(Float.intBitsToFloat(1090519040)).gap(Float.intBitsToFloat(1086324736)).direction(ScenePctService.Direction.COLUMN).justify(ScenePctService.Justify.CENTER).tooltip(str2 + " palette").onClick(() -> {
                    if (mode.isActive()) {
                        mode.set(str2);
                        mgf9hldmchqz();
                    }
                });
            };
            ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[2];
            componentKeyServiceArr[0] = ComponentBoxService.node("material-palette-motif", MaterialPaletteService::new, materialPaletteService -> {
                materialPaletteService.palette(mode.previewColors(str2), zEquals).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1098907648))).flexShrink(0.0f);
            }, new ComponentKeyService[0]).key("motif");
            componentKeyServiceArr[1] = m1z6anxxnaah(str2, zEquals ? MaterialIsLightService.ON_PRIMARY_CONTAINER : MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
                sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).textAlign(SceneTextService.TextAlign.CENTER);
            });
            arrayList.add(ComponentBoxService.node("material-palette-option", supplier, consumer, componentKeyServiceArr).key(str2));
            i = i2 + 1;
        }
        ArrayList arrayList2 = new ArrayList();
        int i3 = (!this.fa9otqddo9xr || this.f5l40cw6jkd8 >= Float.intBitsToFloat(1139802112)) ? 4 : 2;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= arrayList.size()) {
                return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.id(str + ".palettes").gap(Float.intBitsToFloat(1082130432)).minWidth(0.0f);
                }, (ComponentKeyService<?>[]) arrayList2.toArray(i6 -> {
                    return new ComponentKeyService[i6];
                })).key("control");
            }
            arrayList2.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1082130432));
            }, (ComponentKeyService<?>[]) arrayList.subList(i5, Math.min(i5 + i3, arrayList.size())).toArray(i7 -> {
                return new ComponentKeyService[i7];
            })).key("row:" + i5));
            i4 = i5 + i3;
        }
    }

    private static boolean mckb6ayhv749(String[] strArr, boolean z) {
        if (strArr.length < 2 || strArr.length > 4) {
            return false;
        }
        int i = (z || strArr.length == 3) ? 8 : 12;
        return Arrays.stream(strArr).allMatch(str -> {
            return str.length() <= i;
        });
    }

    private ComponentKeyService<?> mdvf7ejopn9c(String str, ModuleSetting<?> moduleSetting, String[] strArr, boolean z) {
        int iMdnv1ub7rbpv;
        ArrayList arrayList = new ArrayList();
        int length = strArr.length == 4 ? 2 : strArr.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= strArr.length) {
                return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.id(str + ".options").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(2.0f).flexShrink(0.0f);
                }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                })).key("control");
            }
            ArrayList arrayList2 = new ArrayList();
            int i4 = i2;
            while (true) {
                int i5 = i4;
                if (i5 < Math.min(i2 + length, strArr.length)) {
                    String str2 = strArr[i5];
                    boolean zEquals = moduleSetting instanceof ModuleSetting.Mode ? ((ModuleSetting.Mode) moduleSetting).get().equals(str2) : ((ModuleSetting.MultiSelect) moduleSetting).get().contains(str2);
                    boolean z2 = i5 == i2;
                    boolean z3 = i5 == Math.min(i2 + length, strArr.length) - 1;
                    if (moduleSetting.isActive()) {
                        iMdnv1ub7rbpv = zEquals ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE;
                    } else {
                        iMdnv1ub7rbpv = mdnv1ub7rbpv();
                    }
                    int i6 = iMdnv1ub7rbpv;
                    ArrayList arrayList3 = new ArrayList();
                    if (z) {
                        arrayList3.add(MaterialTextService.icon(zEquals ? "check" : "add", i6).props(sceneSrcService -> {
                            sceneSrcService.size(Float.intBitsToFloat(1098907648), Float.intBitsToFloat(1098907648));
                        }));
                    }
                    arrayList3.add(m1z6anxxnaah(str2, i6).props(sceneTextService -> {
                        sceneTextService.minWidth(0.0f).flexShrink(1.0f);
                    }));
                    arrayList2.add(ComponentBoxService.node("material-inline-option", MaterialJoinedService::new, materialJoinedService -> {
                        materialJoinedService.colors(zEquals ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SURFACE_HIGH, i6).shape(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832)).joined(z2, z3, zEquals).available(moduleSetting.isActive());
                        materialJoinedService.id(str + ".option." + m1909encvz9z(str2)).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(0.0f).padding(0.0f, Float.intBitsToFloat(1092616192)).gap(Float.intBitsToFloat(1086324736)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).justify(ScenePctService.Justify.CENTER).tooltip(str2).onClick(() -> {
                            if (moduleSetting.isActive()) {
                                if (moduleSetting instanceof ModuleSetting.Mode) {
                                    ((ModuleSetting.Mode) moduleSetting).set(str2);
                                } else {
                                    ModuleSetting.MultiSelect multiSelect = (ModuleSetting.MultiSelect) moduleSetting;
                                    LinkedHashSet linkedHashSet = new LinkedHashSet(multiSelect.get());
                                    if (!linkedHashSet.remove(str2)) {
                                        linkedHashSet.add(str2);
                                    }
                                    multiSelect.set(linkedHashSet);
                                }
                                mgf9hldmchqz();
                            }
                        });
                    }, (ComponentKeyService[]) arrayList3.toArray(i7 -> {
                        return new ComponentKeyService[i7];
                    })).key(str2));
                    i4 = i5 + 1;
                } else {
                    break;
                }
            }
            arrayList.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(2.0f).flexShrink(0.0f);
            }, (ComponentKeyService<?>[]) arrayList2.toArray(i8 -> {
                return new ComponentKeyService[i8];
            })).key("row:" + i2));
            i = i2 + length;
        }
    }

    private void mc8xohiu9lth(String str, String str2) {
        if (this.f2nu1diavlqn != null) {
            ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn.findById(str + ".value");
            if (scenePctServiceFindById instanceof SceneTextService) {
                ((SceneTextService) scenePctServiceFindById).text(str2);
            }
        }
    }

    private static int mdnv1ub7rbpv() {
        return MaterialIsLightService.layer(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1052938076));
    }

    private void m5zxnod9vmdl(Module module) {
        m4m81i8kd5x7(this.fgnnly9bll12);
        if (this.f48wg40ozief != module) {
            this.fbgj78te3qzw = "";
            this.ffxnzn9t4ymi = "";
        }
        this.f48wg40ozief = module;
        this.fezzio1z6avy = true;
        ma79ua8cb9mf();
        mfahyvg0apdz();
        mgf9hldmchqz();
    }

    private void mbt1qklrr5y6(ModuleFeatureType moduleFeatureType) {
        if (this.f4wkce74l1bz == moduleFeatureType && this.f8fi7uhb1xh9.isEmpty() && !this.fezzio1z6avy) {
            return;
        }
        int iCompare = Integer.compare(Arrays.asList(fdivljsc8kbu).indexOf(moduleFeatureType), Arrays.asList(fdivljsc8kbu).indexOf(this.f4wkce74l1bz));
        this.fahprko4fthe = mezavdvwfusb();
        this.fhsro87cmgxb = iCompare;
        this.fgcmhfutlzbp++;
        this.f4wkce74l1bz = moduleFeatureType;
        this.f8fi7uhb1xh9 = "";
        this.ffxnzn9t4ymi = "";
        this.fbgj78te3qzw = "";
        this.fezzio1z6avy = false;
        ma79ua8cb9mf();
        mfahyvg0apdz();
        ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) this.f2nu1diavlqn.findById("clickgui.toolbar.search");
        if (controlLetterSpacingService != null) {
            controlLetterSpacingService.text("");
        }
        this.f48wg40ozief = m4k38isfutk5().stream().findFirst().orElse(null);
        mgf9hldmchqz();
        MaterialEnterService.reveal(this.f2nu1diavlqn.findById("clickgui.content"), iCompare * 24, 0.0f);
    }

    private void m519jxejgk6g() {
        List<Module> listM4k38isfutk5 = m4k38isfutk5();
        if (listM4k38isfutk5.contains(this.f48wg40ozief)) {
            return;
        }
        this.ffxnzn9t4ymi = "";
        this.fbgj78te3qzw = "";
        this.f48wg40ozief = listM4k38isfutk5.stream().findFirst().orElse(null);
        ma79ua8cb9mf();
    }

    private void mgf9hldmchqz() {
        if (this.fgnnly9bll12 != null) {
            this.fgnnly9bll12.invalidateComponent();
        }
    }

    private void m121jzi3356z() {
        if (this.fcdshy34nlxj != null) {
            this.fcdshy34nlxj.close();
        }
    }

    private void mkdxr04hjju() {
        ma79ua8cb9mf();
        this.fezzio1z6avy = false;
        mfahyvg0apdz();
        mgf9hldmchqz();
        MaterialEnterService.enter(this.f2nu1diavlqn.findById("clickgui.modules"), Float.intBitsToFloat(-1047527424), 0.0f);
    }

    @Override 
    public void onOpen(ScreenScreenIdService screenScreenIdService) {
        if (ThemeCornerData.current().focusSearch()) {
            ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn.findById("clickgui.toolbar.search");
            if (scenePctServiceFindById instanceof ControlLetterSpacingService) {
                ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) scenePctServiceFindById;
                maqwjkcdabqp(controlLetterSpacingService);
                controlLetterSpacingService.selectAll();
            }
        }
    }

    @Override 
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        mf7rxfremc99();
        this.fahprko4fthe = null;
        this.fgcmhfutlzbp++;
        ma79ua8cb9mf();
        mfahyvg0apdz();
        m4m81i8kd5x7(this.fgnnly9bll12);
        mgf9hldmchqz();
    }

    @Override 
    public void onSuspend(ScreenScreenIdService screenScreenIdService) {
        mf7rxfremc99();
        mfahyvg0apdz();
        m4m81i8kd5x7(this.fgnnly9bll12);
    }

    @Override 
    public void tick(ScreenScreenIdService screenScreenIdService) {
        if (this.f2nu1diavlqn == null) {
            return;
        }
        viewport(this.f2nu1diavlqn.computedW(), this.f2nu1diavlqn.computedH());
    }

    private void mf7rxfremc99() {
        if (ThemeCornerData.current().rememberClickGui()) {
            HashMap map = new HashMap(this.fjlbgzdyzws9);
            for (String str : f89n839yrbtw) {
                ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn == null ? null : this.f2nu1diavlqn.findById(str);
                if (scenePctServiceFindById != null && scenePctServiceFindById.computedW() > 0.0f && scenePctServiceFindById.computedH() > 0.0f && !this.fjlbgzdyzws9.containsKey(str)) {
                    map.put(str, Float.valueOf(Math.max(scenePctServiceFindById.scrollMin(), Math.min(0.0f, scenePctServiceFindById.scrollY()))));
                }
            }
            this.fcsg0mvq82gd.save(new BuiltinRepository.State(this.f4wkce74l1bz.name(), this.f48wg40ozief == null ? "" : m1vcviafemyn(this.f48wg40ozief), this.f8fi7uhb1xh9, this.ffxnzn9t4ymi, this.fbgj78te3qzw, this.f5pafgdpvh0n, this.fezzio1z6avy, this.ffn57s6hi2n2, this.f13slc68a70r, this.f2kmodb23a21, this.fcijmdt1zfva, map));
        }
    }

    private void m5ysaxlmqi24() {
        if (Math.abs(this.f5l40cw6jkd8 - this.f2nu1diavlqn.computedW()) > Float.intBitsToFloat(1056964608) || Math.abs(this.f8x77xf3839a - this.f2nu1diavlqn.computedH()) > Float.intBitsToFloat(1056964608)) {
            viewport(this.f2nu1diavlqn.computedW(), this.f2nu1diavlqn.computedH());
            return;
        }
        Iterator<Map.Entry<String, Float>> it = this.fjlbgzdyzws9.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Float> next = it.next();
            ScenePctService<?> scenePctServiceFindById = this.f2nu1diavlqn.findById(next.getKey());
            if (scenePctServiceFindById != null && scenePctServiceFindById.computedW() > 0.0f && scenePctServiceFindById.computedH() > 0.0f) {
                float fMax = Math.max(scenePctServiceFindById.scrollMin(), Math.min(0.0f, next.getValue().floatValue()));
                MotionAnimateService.cancel(scenePctServiceFindById, MotionColorsContainer.Floats.SCROLL_Y);
                scenePctServiceFindById.scrollY(fMax).scrollTarget(fMax);
                it.remove();
            }
        }
    }

    public void viewport(float f, float f2) {
        if (this.f2nu1diavlqn == null) {
            return;
        }
        boolean z = f > 0.0f && f < Float.intBitsToFloat(1146224640);
        boolean z2 = f > 0.0f && f < Float.intBitsToFloat(1142292480);
        boolean z3 = (f2 > 0.0f && Math.abs(this.f8x77xf3839a - f2) > Float.intBitsToFloat(1056964608)) || (f > 0.0f && Math.abs(this.f5l40cw6jkd8 - f) > Float.intBitsToFloat(1056964608));
        if (f2 > 0.0f) {
            this.f8x77xf3839a = f2;
        }
        if (f > 0.0f) {
            this.f5l40cw6jkd8 = f;
        }
        boolean z4 = (z == this.fa9otqddo9xr && z2 == this.ftzcjcss2ew) ? false : true;
        this.fa9otqddo9xr = z;
        this.ftzcjcss2ew = z2;
        int iMj6gzbc3kwej = mj6gzbc3kwej();
        if (this.f4j6eswx7fd3 != null && iMj6gzbc3kwej != this.fb78sspb85td) {
            this.f4j6eswx7fd3.refresh();
        }
        if (z4 || z3 || iMj6gzbc3kwej != this.fb78sspb85td) {
            this.fb78sspb85td = iMj6gzbc3kwej;
            m519jxejgk6g();
            mgf9hldmchqz();
        }
        if (this.f5sedrlfn27b != null && (!this.f5sedrlfn27b.isVisible() || !this.f5sedrlfn27b.isActive())) {
            ma79ua8cb9mf();
        }
        if (this.f4j6eswx7fd3 != null) {
            if (this.f4j6eswx7fd3.selection().target().available().getAsBoolean()) {
                this.f4j6eswx7fd3.viewport(this.f5l40cw6jkd8, this.f8x77xf3839a);
                return;
            } else {
                manpywc7uulo();
                return;
            }
        }
        if (this.f4fzgs7my5mf == null || !z3) {
            return;
        }
        this.f4fzgs7my5mf.invalidateComponent();
    }

    private int mj6gzbc3kwej() {
        int iHashCode = m2tqr7c84oc5().all().hashCode();
        Iterator<List<Module>> it = this.fikxlao3mld.values().iterator();
        while (it.hasNext()) {
            for (Module module : it.next()) {
                iHashCode = (31 * iHashCode) + Boolean.hashCode(module.isEnabled());
                for (ModuleSetting<?> moduleSetting : module.settings()) {
                    if (moduleSetting instanceof ModuleSetting.Keybind) {
                        iHashCode = (31 * iHashCode) + Objects.hashCode(moduleSetting.get());
                    }
                }
                if (module == this.f48wg40ozief) {
                    for (ModuleSetting<?> moduleSetting2 : module.settings()) {
                        iHashCode = (31 * ((31 * ((31 * iHashCode) + Objects.hashCode(moduleSetting2.get()))) + Boolean.hashCode(moduleSetting2.isVisible()))) + Boolean.hashCode(moduleSetting2.isActive());
                    }
                }
            }
        }
        return iHashCode;
    }

    public boolean capturesKeybindInput() {
        return this.f4j6eswx7fd3 != null;
    }

    private ModuleBindService m2tqr7c84oc5() {
        return CoreIsInitializedHandler.isReady() ? CoreIsInitializedHandler.get().keybinds() : this.f9zqzlfsqvx1;
    }

    private List<KeybindTargetService.Target> mfvish4bq94d() {
        ArrayList arrayList = new ArrayList();
        Iterator<List<Module>> it = this.fikxlao3mld.values().iterator();
        while (it.hasNext()) {
            for (Module module : it.next()) {
                if (module.category() != ModuleFeatureType.DEVELOPMENT || this.f4wkce74l1bz == ModuleFeatureType.DEVELOPMENT) {
                    arrayList.add(mf4pd2ddegxz(module));
                    for (ModuleSetting<?> moduleSetting : module.settings()) {
                        if (moduleSetting instanceof ModuleSetting.Keybind) {
                            arrayList.add(m5beuhbhsl3v(module, (ModuleSetting.Keybind) moduleSetting));
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private KeybindTargetService.Target mf4pd2ddegxz(Module module) {
        return new KeybindTargetService.Target(m7sclicozxcc(module) + "/toggle", "Toggle module", module.name(), () -> {
            return m2tqr7c84oc5().getKey(module.name());
        }, i -> {
            if (i < 0) {
                m2tqr7c84oc5().unbind(module.name());
            } else {
                m2tqr7c84oc5().bind(i, module.name());
            }
            if (!CoreIsInitializedHandler.isReady() || CoreIsInitializedHandler.get().config() == null) {
                return;
            }
            CoreIsInitializedHandler.get().config().save();
        }, () -> {
            return true;
        }, true);
    }

    private KeybindTargetService.Target m5beuhbhsl3v(Module module, ModuleSetting.Keybind keybind) {
        String str = m7sclicozxcc(module) + "/" + ((String) keybind.category().map((v0) -> {
            return v0.qualifiedName();
        }).orElse("general")) + "/" + keybind.name();
        String strName = keybind.name();
        String strName2 = module.name();
        Objects.requireNonNull(keybind);
        IntSupplier intSupplier = keybind::get;
        Objects.requireNonNull(keybind);
        return new KeybindTargetService.Target(str, strName, strName2, intSupplier, (v1) -> {
            keybind.set(v1);
        }, () -> {
            return keybind.isVisible() && keybind.isActive();
        }, false);
    }

    private void m18zjyo62x7g() {
        if (this.f48wg40ozief != null) {
            mi4dv5y123mb(mf4pd2ddegxz(this.f48wg40ozief));
        }
    }

    private void m9545dt2cnyo(ModuleSetting.Keybind keybind) {
        if (this.f48wg40ozief == null || !keybind.isActive()) {
            return;
        }
        mi4dv5y123mb(m5beuhbhsl3v(this.f48wg40ozief, keybind));
    }

    private void mi4dv5y123mb(KeybindTargetService.Target target) {
        ma79ua8cb9mf();
        mfahyvg0apdz();
        m4m81i8kd5x7(this.f2nu1diavlqn);
        if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
            CoreIsInitializedHandler.get().scene().clearFocus();
        }
        CompatRepository.Snapshot snapshot = CoreIsInitializedHandler.isReady() ? CompatRepository.read() : CompatRepository.Snapshot.EMPTY;
        this.f4j6eswx7fd3 = new KeybindRenderer(new KeybindTargetService(target, mfvish4bq94d(), snapshot.uses(), snapshot.legends()), this::manpywc7uulo, () -> {
            manpywc7uulo();
            mgf9hldmchqz();
        });
        this.f4j6eswx7fd3.viewport(this.f5l40cw6jkd8, this.f8x77xf3839a);
        this.figehearx1so = new ComponentMountService().mount(this.f4j6eswx7fd3, this.fa0t12x6vytn);
        this.figehearx1so.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxWidth(Float.intBitsToFloat(1150189568)).maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        this.fgnnly9bll12.pointerEvents(false);
        this.f54lv3h7aq07 = ComponentUnmountService.mountCentered(this.f2nu1diavlqn, this.figehearx1so, this.ftzcjcss2ew ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1103101952), this::manpywc7uulo);
        this.f54lv3h7aq07.id("clickgui.keybind-layer");
        ((SceneCornerRadiusService) this.f54lv3h7aq07.children().getFirst()).backgroundColor(-1206973165);
        MaterialEnterService.enter(this.figehearx1so, 0.0f, Float.intBitsToFloat(1098907648));
    }

    private void manpywc7uulo() {
        if (this.figehearx1so != null) {
            this.figehearx1so.unmountComponent();
        }
        ComponentUnmountService.unmount(this.f2nu1diavlqn, this.f54lv3h7aq07);
        this.f54lv3h7aq07 = null;
        this.figehearx1so = null;
        this.f4j6eswx7fd3 = null;
        if (this.fgnnly9bll12 != null) {
            this.fgnnly9bll12.pointerEvents(true);
        }
    }

    private void m2nqvpgnuas5(ModuleSetting<?> moduleSetting) {
        BuiltinRangeService builtinRangeService;
        float fIntBitsToFloat;
        if (moduleSetting.isActive()) {
            ma79ua8cb9mf();
            mfahyvg0apdz();
            m4m81i8kd5x7(this.f2nu1diavlqn);
            this.f5sedrlfn27b = moduleSetting;
            this.f8y3rqmwwn9e = moduleSetting instanceof ModuleEntriesService ? new BuiltinRenderer((ModuleEntriesService) moduleSetting, this::ma79ua8cb9mf, this::mgf9hldmchqz) : null;
            this.fduu1s9yuh4y = moduleSetting instanceof ModuleLayoutService ? new InventoryRenderer(((ModuleLayoutService) moduleSetting).layout()) : null;
            this.fc6hxrkh2sl1 = moduleSetting instanceof ModuleSetting.Color ? new BuiltinOriginalService(((ModuleSetting.Color) moduleSetting).get().intValue()) : null;
            if (moduleSetting instanceof ModuleSetting.Number) {
                builtinRangeService = new BuiltinRangeService((ModuleSetting.Number) moduleSetting);
            } else {
                builtinRangeService = moduleSetting instanceof ModuleSetting.Range ? new BuiltinRangeService((ModuleSetting.Range) moduleSetting) : null;
            }
            this.fbknrfxp9co = builtinRangeService;
            if (moduleSetting instanceof ModuleSetting.Curve) {
                this.faprap0mz9ce = new ModuleSetting.Number("Steps", ((ModuleSetting.Curve) moduleSetting).get().steps(), 1.0f, Float.intBitsToFloat(1120403456), 1.0f);
            }
            this.f4fzgs7my5mf = new ComponentMountService().mount(this::m9617i3qf8w7, this.fa0t12x6vytn);
            LayoutContainerNode layoutContainerNodeWidth = this.f4fzgs7my5mf.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            if (this.f8y3rqmwwn9e != null) {
                fIntBitsToFloat = Float.intBitsToFloat(1144913920);
            } else {
                fIntBitsToFloat = this.fduu1s9yuh4y != null ? Float.intBitsToFloat(1147207680) : Float.intBitsToFloat(1141637120);
            }
            layoutContainerNodeWidth.maxWidth(fIntBitsToFloat).maxHeight(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            this.fjnp49geqil8 = ComponentUnmountService.mountCentered(this.f2nu1diavlqn, this.f4fzgs7my5mf, this.ftzcjcss2ew ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1103101952), this::ma79ua8cb9mf);
            this.fjnp49geqil8.id("clickgui.dialog-layer");
            ((SceneCornerRadiusService) this.fjnp49geqil8.children().getFirst()).backgroundColor(1375731712);
            MaterialEnterService.enter(this.f4fzgs7my5mf.findById("clickgui.dialog"), 0.0f, Float.intBitsToFloat(1099956224));
        }
    }

    private ComponentKeyService<?> m9617i3qf8w7(ComponentThemeService componentThemeService) {
        boolean zIsEmpty;
        if (this.f8y3rqmwwn9e != null) {
            this.f8y3rqmwwn9e.viewport(this.f5l40cw6jkd8, this.f8x77xf3839a);
            return this.f8y3rqmwwn9e.render(componentThemeService);
        }
        ModuleSetting<?> moduleSetting = this.f5sedrlfn27b;
        ArrayList arrayList = new ArrayList();
        if (this.fduu1s9yuh4y != null) {
            this.fduu1s9yuh4y.viewport(this.f5l40cw6jkd8);
            arrayList.add(this.fduu1s9yuh4y.render(componentThemeService));
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
                arrayList.add(maaxc7ln3jgp("mode." + m1909encvz9z(str), str, mode.get().equals(str), false, () -> {
                    if (mode.isActive()) {
                        mode.set(str);
                    }
                    ma79ua8cb9mf();
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
                arrayList.add(maaxc7ln3jgp("multi." + m1909encvz9z(str2), str2, multiSelect.get().contains(str2), true, () -> {
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
            m83t1nniqezy(arrayList, componentThemeService);
        } else if (moduleSetting instanceof ModuleSetting.Curve) {
            ma74d4b7i94u(arrayList, (ModuleSetting.Curve) moduleSetting, componentThemeService);
        } else if (this.fbknrfxp9co != null) {
            m5q769lx0uea(arrayList, componentThemeService);
        }
        boolean z = (this.fc6hxrkh2sl1 == null && this.fbknrfxp9co == null && this.fduu1s9yuh4y == null) ? false : true;
        if (this.fc6hxrkh2sl1 != null) {
            zIsEmpty = this.fc6hxrkh2sl1.error().isEmpty();
        } else {
            zIsEmpty = this.fbknrfxp9co == null || this.fbknrfxp9co.error().isEmpty();
        }
        boolean z2 = zIsEmpty;
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.id("clickgui.dialog").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).maxHeight(Math.max(Float.intBitsToFloat(1123024896), Math.min(this.fc6hxrkh2sl1 != null ? Float.intBitsToFloat(1144258560) : Float.intBitsToFloat(1142947840), this.f8x77xf3839a - (this.ftzcjcss2ew ? 16 : 48)))).direction(ScenePctService.Direction.COLUMN).padding(this.f8x77xf3839a < Float.intBitsToFloat(1135869952) ? Float.intBitsToFloat(1094713344) : Float.intBitsToFloat(1101004800)).gap(this.f8x77xf3839a < Float.intBitsToFloat(1135869952) ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1094713344)).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_HIGH).clip(true);
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
        componentKeyServiceArr[0] = MaterialTextService.text(moduleSetting.displayName(), Float.intBitsToFloat(1101004800), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
            sceneTextService.lineHeight(Float.intBitsToFloat(1103101952));
        }).props(sceneTextService2 -> {
            sceneTextService2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f);
        });
        componentKeyServiceArr[1] = ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id("clickgui.dialog.body").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).minHeight(0.0f).flexShrink(1.0f).padding(0.0f, Float.intBitsToFloat(1086324736), 0.0f, 0.0f).gap(((moduleSetting instanceof ModuleSetting.Mode) || (moduleSetting instanceof ModuleSetting.MultiSelect)) ? 0.0f : Float.intBitsToFloat(1092616192)).scrollable(true).scrollbarColor(MaterialIsLightService.OUTLINE).scrollbarWidth(2.0f).clip(true);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i5 -> {
            return new ComponentKeyService[i5];
        }));
        Consumer<LayoutContainerNode> consumer2 = layoutContainerNode2 -> {
            layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).justify(ScenePctService.Justify.END).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f);
        };
        ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
        componentKeyServiceArr2[0] = m32zlilhw8fn("clickgui.dialog.cancel", z ? "Cancel" : "Close", this::ma79ua8cb9mf, 0, MaterialIsLightService.PRIMARY, Float.intBitsToFloat(1101004800));
        componentKeyServiceArr2[1] = m32zlilhw8fn("clickgui.dialog.apply", z ? "Apply" : "Done", this::mafblp0uoc00, MaterialIsLightService.PRIMARY, z2 ? MaterialIsLightService.ON_PRIMARY : mdnv1ub7rbpv(), Float.intBitsToFloat(1101004800)).props(materialJoinedService -> {
            materialJoinedService.available(z2).visible(!(moduleSetting instanceof ModuleSetting.Mode));
        });
        componentKeyServiceArr[2] = ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer2, (ComponentKeyService<?>[]) componentKeyServiceArr2);
        return ComponentBoxService.panel(consumer, componentKeyServiceArr).key("dialog");
    }

    private ComponentKeyService<?> maaxc7ln3jgp(String str, String str2, boolean z, boolean z2, Runnable runnable) {
        return ComponentBoxService.node("material-choice", MaterialJoinedService::new, materialJoinedService -> {
            materialJoinedService.colors(0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1082130432), Float.intBitsToFloat(1111490560));
            materialJoinedService.id("clickgui.dialog." + str).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).padding(0.0f, Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1098907648)).direction(ScenePctService.Direction.ROW).align(ScenePctService.Align.CENTER).flexShrink(0.0f).onClick(runnable);
        }, ComponentBoxService.node("material-selection", MaterialCheckboxValidator::new, materialCheckboxValidator -> {
            materialCheckboxValidator.checkbox(z2).selected(z);
        }, new ComponentKeyService[0]).key("selection"), MaterialTextService.text(str2, Float.intBitsToFloat(1098907648), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
            sceneTextService.flex(1.0f).minWidth(0.0f);
        })).key(str);
    }

    private void m83t1nniqezy(List<ComponentKeyService<?>> list, ComponentThemeService componentThemeService) {
        BuiltinOriginalService builtinOriginalService = this.fc6hxrkh2sl1;
        boolean z = this.fcsyblho83aq == ColorFeatureType.TRIANGLE;
        list.add(MaterialTabsService.tabs("clickgui.dialog.color-mode", this.fcsyblho83aq, colorFeatureType -> {
            this.fcsyblho83aq = colorFeatureType;
            componentThemeService.invalidate();
        }));
        list.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id("clickgui.dialog.color-canvas").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(z ? Float.intBitsToFloat(1131413504) : Float.intBitsToFloat(1127219200))).gap(Float.intBitsToFloat(1098907648)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{z ? ComponentBoxService.node("color-triangle", HueTrianglePicker::new, hueTrianglePicker -> {
            hueTrianglePicker.id("clickgui.dialog.color-triangle").hue(builtinOriginalService.hue()).sat(builtinOriginalService.saturation()).bri(builtinOriginalService.brightness()).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flex(1.0f).minWidth(0.0f).onHueChange(f -> {
                builtinOriginalService.hue(f.floatValue());
                componentThemeService.invalidate();
            }).onSBChange((f2, f3) -> {
                builtinOriginalService.sv(f2.floatValue(), f3.floatValue());
                componentThemeService.invalidate();
            });
        }, new ComponentKeyService[0]).key("triangle") : ComponentBoxService.node("color-field", SaturationBrightnessPicker::new, saturationBrightnessPicker -> {
            saturationBrightnessPicker.material(true).id("clickgui.dialog.color-field").hue(builtinOriginalService.hue()).sat(builtinOriginalService.saturation()).bri(builtinOriginalService.brightness()).cornerRadius(Float.intBitsToFloat(1103101952)).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flex(1.0f).minWidth(0.0f).onChange((f, f2) -> {
                builtinOriginalService.sv(f.floatValue(), f2.floatValue());
                componentThemeService.invalidate();
            });
        }, new ComponentKeyService[0]).key("field"), ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
            layoutContainerNode2.id("clickgui.dialog.color-comparison").width(LayoutOperationHandler.px(Float.intBitsToFloat(1117782016))).height(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{m5uevlcbcehy("current", "Current", builtinOriginalService.original(), -1), m5uevlcbcehy("preview", "New", builtinOriginalService.argb(), 1)}).key("comparison")}).key("canvas"));
        if (z) {
            list.add(MaterialTextService.text("Rotate the ring · Mix inside the triangle", Float.intBitsToFloat(1094713344), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
                sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap(true).flexShrink(0.0f);
            }).key("triangle-help"));
        }
        if (!z) {
            list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode3 -> {
                layoutContainerNode3.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(0.0f).flexShrink(0.0f);
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label("Hue", MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("color-hue", ColorGetAnimPropertyService::new, colorGetAnimPropertyService -> {
                colorGetAnimPropertyService.material(true).id("clickgui.dialog.hue").hue(builtinOriginalService.hue()).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).flexShrink(0.0f).onChange(f -> {
                    builtinOriginalService.hue(f.floatValue());
                    componentThemeService.invalidate();
                });
            }, new ComponentKeyService[0]).key("hue")}).key("hue-control"));
        }
        list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode4 -> {
            layoutContainerNode4.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(0.0f).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label("Opacity · " + Math.round(builtinOriginalService.alpha() * Float.intBitsToFloat(1120403456)) + "%", MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("color-alpha", AlphaSliderControl::new, alphaSliderControl -> {
            alphaSliderControl.material(true).id("clickgui.dialog.alpha").hue(builtinOriginalService.hue()).sat(builtinOriginalService.saturation()).bri(builtinOriginalService.brightness()).alpha(builtinOriginalService.alpha()).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).flexShrink(0.0f).onChange(f -> {
                builtinOriginalService.alpha(f.floatValue());
                componentThemeService.invalidate();
            });
        }, new ComponentKeyService[0]).key("alpha")}).key("alpha-control"));
        ArrayList arrayList = new ArrayList();
        float[][] fArr = {new float[]{Float.intBitsToFloat(1046562734), Float.intBitsToFloat(1065017672)}, new float[]{Float.intBitsToFloat(1054280253), Float.intBitsToFloat(1064011039)}, new float[]{Float.intBitsToFloat(1058977874), Float.intBitsToFloat(1060655596)}, new float[]{Float.intBitsToFloat(1061662228), Float.intBitsToFloat(1056293519)}};
        String[] strArr = {"Light", "Soft", "Rich", "Deep"};
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= fArr.length) {
                break;
            }
            int iShade = builtinOriginalService.shade(fArr[i2][0], fArr[i2][1]);
            arrayList.add(ComponentBoxService.node("material-shade", MaterialJoinedService::new, materialJoinedService -> {
                materialJoinedService.colors(0, MaterialIsLightService.ON_SURFACE).shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1109393408)).id("clickgui.dialog.shade." + i2).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).flex(1.0f).minWidth(0.0f).padding(Float.intBitsToFloat(1090519040), 0.0f).tooltip(strArr[i2]).onClick(() -> {
                    builtinOriginalService.sv(fArr[i2][0], fArr[i2][1]);
                    componentThemeService.invalidate();
                });
            }, ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService -> {
                materialColorService.color(iShade).radius(Float.intBitsToFloat(1094713344)).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).pointerEvents(false);
            }, new ComponentKeyService[0])).key("shade:" + i2));
            i = i2 + 1;
        }
        list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode5 -> {
            layoutContainerNode5.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(0.0f).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label("Shades", MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode6 -> {
            layoutContainerNode6.id("clickgui.dialog.shades").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1090519040));
        }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
            return new ComponentKeyService[i3];
        }))}).key("shades"));
        list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode7 -> {
            layoutContainerNode7.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label("Hex · AARRGGBB", MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("color-hex", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            MaterialTextService.input(controlLetterSpacingService);
            controlLetterSpacingService.materialError(!builtinOriginalService.error().isEmpty()).id("clickgui.dialog.hex").maxLength(9).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1113587712))).flexShrink(0.0f);
            if (!controlLetterSpacingService.focused()) {
                controlLetterSpacingService.text(builtinOriginalService.hex());
            }
            controlLetterSpacingService.onChanged(str -> {
                builtinOriginalService.hex(str);
                componentThemeService.invalidate();
            });
            controlLetterSpacingService.onSubmit(str2 -> {
                mafblp0uoc00();
            });
        }, new ComponentKeyService[0]).key("hex")}).key("hex-control"));
        if (builtinOriginalService.error().isEmpty()) {
            return;
        }
        list.add(MaterialTextService.text(builtinOriginalService.error(), Float.intBitsToFloat(1094713344), MaterialIsLightService.ERROR).props(sceneTextService2 -> {
            sceneTextService2.wordWrap(true).flexShrink(0.0f);
        }).key("error"));
    }

    private ComponentKeyService<?> m5uevlcbcehy(String str, String str2, int i, int i2) {
        return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.flex(1.0f).minWidth(0.0f).gap(Float.intBitsToFloat(1086324736)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label(str2, MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("material-color-swatch", MaterialColorService::new, materialColorService -> {
            materialColorService.id("clickgui.dialog.color-" + str).color(i).radius(Float.intBitsToFloat(1105199104)).paired(i2).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1113587712))).pointerEvents(false);
        }, new ComponentKeyService[0])}).key(str);
    }

    private void m5q769lx0uea(List<ComponentKeyService<?>> list, ComponentThemeService componentThemeService) {
        BuiltinRangeService builtinRangeService = this.fbknrfxp9co;
        list.add(MaterialTextService.text(builtinRangeService.bounds() + " · " + builtinRangeService.stepLabel(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
            sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).wordWrap(true).flexShrink(0.0f);
        }).key("bounds"));
        ArrayList arrayList = new ArrayList();
        arrayList.add(m7bw3wf49bir("low", builtinRangeService.range() ? "Lower" : "Value", builtinRangeService.low(), componentThemeService));
        if (builtinRangeService.range()) {
            arrayList.add(m7bw3wf49bir("high", "Upper", builtinRangeService.high(), componentThemeService));
        }
        list.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1094713344)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        })).key("fields"));
        if (builtinRangeService.error().isEmpty()) {
            return;
        }
        list.add(MaterialTextService.text(builtinRangeService.error(), Float.intBitsToFloat(1094713344), MaterialIsLightService.ERROR).props(sceneTextService2 -> {
            sceneTextService2.wordWrap(true).flexShrink(0.0f);
        }).key("error"));
    }

    private ComponentKeyService<?> m7bw3wf49bir(String str, String str2, String str3, ComponentThemeService componentThemeService) {
        return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1090519040)).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.label(str2, MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("material-exact-number", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            MaterialTextService.input(controlLetterSpacingService);
            controlLetterSpacingService.id("clickgui.dialog.number." + str).materialError(!this.fbknrfxp9co.error().isEmpty()).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1113587712))).maxLength(32).flexShrink(0.0f);
            if (!controlLetterSpacingService.focused()) {
                controlLetterSpacingService.text(str3);
            }
            controlLetterSpacingService.onChanged(str4 -> {
                if (str.equals("low")) {
                    this.fbknrfxp9co.low(str4);
                } else {
                    this.fbknrfxp9co.high(str4);
                }
                componentThemeService.invalidate();
            });
            controlLetterSpacingService.onSubmit(str5 -> {
                mafblp0uoc00();
            });
        }, new ComponentKeyService[0]).key(str)}).key(str);
    }

    private void mafblp0uoc00() {
        if (this.f5sedrlfn27b == null || !this.f5sedrlfn27b.isActive()) {
            return;
        }
        if (this.fduu1s9yuh4y != null) {
            ((ModuleLayoutService) this.f5sedrlfn27b).layout(this.fduu1s9yuh4y.draft());
        } else if (this.fc6hxrkh2sl1 != null) {
            if (!this.fc6hxrkh2sl1.error().isEmpty()) {
                return;
            } else {
                ((ModuleSetting.Color) this.f5sedrlfn27b).set(Integer.valueOf(this.fc6hxrkh2sl1.argb()));
            }
        } else if (this.fbknrfxp9co != null && !this.fbknrfxp9co.apply()) {
            return;
        }
        ma79ua8cb9mf();
        mgf9hldmchqz();
    }

    private void ma74d4b7i94u(List<ComponentKeyService<?>> list, ModuleSetting.Curve curve, ComponentThemeService componentThemeService) {
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
            arrayList.add(m32zlilhw8fn("clickgui.dialog.curve." + curveType.name(), m9xb6xboaqgo(curveType), () -> {
                ModuleSetting.CurveValue curveValueSteps;
                if (curve.isActive()) {
                    switch (curveType) {
                        case LINEAR:
                            curveValueSteps = ModuleSetting.CurveValue.linear();
                            break;
                        case CUBIC_BEZIER:
                            curveValueSteps = ModuleSetting.CurveValue.cubicBezier(Float.intBitsToFloat(1048576000), Float.intBitsToFloat(1036831949), Float.intBitsToFloat(1048576000), 1.0f);
                            break;
                        case STEPS:
                            curveValueSteps = ModuleSetting.CurveValue.steps(4, ModuleSetting.StepMode.JUMP_END);
                            break;
                        default:
                            throw new MatchException((String) null, (Throwable) null);
                    }
                    curve.set(curveValueSteps);
                    this.faprap0mz9ce.set(Float.valueOf(curve.get().steps()));
                    componentThemeService.invalidate();
                    mgf9hldmchqz();
                }
            }, curve.get().type() == curveType ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER, curve.get().type() == curveType ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER, curve.get().type() == curveType ? Float.intBitsToFloat(1101004800) : Float.intBitsToFloat(1094713344)).props(materialJoinedService -> {
                materialJoinedService.joined(curveType == ModuleSetting.CurveType.LINEAR, curveType == ModuleSetting.CurveType.STEPS, curve.get().type() == curveType).flex(1.0f).minWidth(0.0f).padding(0.0f, Float.intBitsToFloat(1090519040));
            }));
            i = i2 + 1;
        }
        list.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
            layoutContainerNode.id("clickgui.dialog.curve-types").width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(2.0f).flexShrink(0.0f);
        }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
            return new ComponentKeyService[i3];
        })));
        list.add(ComponentBoxService.node("material-curve", CurveGraphControl::new, curveGraphControl -> {
            curveGraphControl.material(true).value(curve.get()).id("clickgui.dialog.curve-graph").size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1131413504))).flexShrink(0.0f).onCommit(curveValue -> {
                if (curve.isActive()) {
                    curve.set(curveValue);
                }
                mgf9hldmchqz();
            });
        }, new ComponentKeyService[0]).key("graph"));
        if (curve.get().type() != ModuleSetting.CurveType.STEPS) {
            if (curve.get().type() == ModuleSetting.CurveType.CUBIC_BEZIER) {
                list.add(MaterialTextService.text("Drag either handle to shape the transition.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
                    sceneTextService.wordWrap(true);
                }));
                return;
            }
            return;
        }
        list.add(MaterialTextService.label("Steps · " + curve.get().steps(), MaterialIsLightService.ON_SURFACE_VARIANT));
        list.add(ComponentBoxService.node("curve-steps", () -> {
            return new NumberSettingControl(this.faprap0mz9ce);
        }, numberSettingControl -> {
            numberSettingControl.material(true).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).flexShrink(0.0f);
            numberSettingControl.onChange(f -> {
                curve.set(ModuleSetting.CurveValue.steps(Math.max(curve.get().stepMode() == ModuleSetting.StepMode.JUMP_NONE ? 2 : 1, Math.round(f.floatValue())), curve.get().stepMode()));
                componentThemeService.invalidate();
                mgf9hldmchqz();
            });
        }, new ComponentKeyService[0]).key("steps"));
        ModuleSetting.StepMode[] stepModeArrValues = ModuleSetting.StepMode.values();
        int length2 = stepModeArrValues.length;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= length2) {
                return;
            }
            ModuleSetting.StepMode stepMode = stepModeArrValues[i5];
            list.add(maaxc7ln3jgp("step-mode." + stepMode.name(), mfur86tcqz73(stepMode.name()), curve.get().stepMode() == stepMode, false, () -> {
                curve.set(ModuleSetting.CurveValue.steps(Math.max(stepMode == ModuleSetting.StepMode.JUMP_NONE ? 2 : 1, curve.get().steps()), stepMode));
                componentThemeService.invalidate();
                mgf9hldmchqz();
            }));
            i4 = i5 + 1;
        }
    }

    private void ma79ua8cb9mf() {
        manpywc7uulo();
        if (this.f4fzgs7my5mf != null) {
            mfahyvg0apdz();
            m4m81i8kd5x7(this.f4fzgs7my5mf);
        }
        if (this.f4fzgs7my5mf != null) {
            this.f4fzgs7my5mf.unmountComponent();
        }
        ComponentUnmountService.unmount(this.f2nu1diavlqn, this.fjnp49geqil8);
        this.fjnp49geqil8 = null;
        this.f4fzgs7my5mf = null;
        this.f5sedrlfn27b = null;
        this.fc6hxrkh2sl1 = null;
        this.fbknrfxp9co = null;
        this.fduu1s9yuh4y = null;
        this.f8y3rqmwwn9e = null;
    }

    @Override 
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int i, int i2) {
        int iFloorMod;
        if (this.f4j6eswx7fd3 != null) {
            return this.f4j6eswx7fd3.keyPressed(i, i2);
        }
        if (this.fduu1s9yuh4y != null && this.fduu1s9yuh4y.keyPressed(i, i2)) {
            return true;
        }
        if (i == 256) {
            ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt = mh0r9igdkzmt(this.f2nu1diavlqn);
            if (controlGetAnimPropertyServiceMh0r9igdkzmt != null) {
                controlGetAnimPropertyServiceMh0r9igdkzmt.acceptKey(i);
                return true;
            }
            ControlLetterSpacingService controlLetterSpacingServiceMcudjgl33jla = mcudjgl33jla(this.f2nu1diavlqn);
            if (controlLetterSpacingServiceMcudjgl33jla != null) {
                controlLetterSpacingServiceMcudjgl33jla.unfocus();
                return true;
            }
            if (this.f5sedrlfn27b == null) {
                return false;
            }
            ma79ua8cb9mf();
            return true;
        }
        ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt2 = mh0r9igdkzmt(this.f2nu1diavlqn);
        if (controlGetAnimPropertyServiceMh0r9igdkzmt2 != null) {
            controlGetAnimPropertyServiceMh0r9igdkzmt2.acceptKey(i);
            return true;
        }
        if ((i2 & 10) != 0 && i == 70 && this.f5sedrlfn27b == null) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) this.f2nu1diavlqn.findById((i2 & 1) != 0 ? "clickgui.settings-search" : "clickgui.toolbar.search");
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
            mj2sjuy2kmae(this.f4fzgs7my5mf != null ? this.f4fzgs7my5mf : this.fgnnly9bll12, arrayList);
            if (arrayList.isEmpty()) {
                return true;
            }
            boolean z = (i2 & 1) != 0;
            int iIndexOf = arrayList.indexOf(this.fg2zbyyyq8m8 != null ? this.fg2zbyyyq8m8 : mcudjgl33jla(this.f2nu1diavlqn));
            mfahyvg0apdz();
            m4m81i8kd5x7(this.f2nu1diavlqn);
            if (iIndexOf < 0) {
                iFloorMod = z ? arrayList.size() - 1 : 0;
            } else {
                iFloorMod = Math.floorMod(iIndexOf + (z ? -1 : 1), arrayList.size());
            }
            this.fg2zbyyyq8m8 = (ScenePctService) arrayList.get(iFloorMod);
            mewzql46vnem(this.fg2zbyyyq8m8, true);
            mb7v2groceso(this.fg2zbyyyq8m8);
            return true;
        }
        if (mcudjgl33jla(this.f2nu1diavlqn) != null || mh0r9igdkzmt(this.f2nu1diavlqn) != null) {
            return false;
        }
        ScenePctService<?> scenePctService = this.fg2zbyyyq8m8;
        if (scenePctService instanceof MaterialJoinedService) {
            MaterialJoinedService materialJoinedService = (MaterialJoinedService) scenePctService;
            if (i == 257 || i == 32) {
                materialJoinedService.activate();
                return true;
            }
        }
        ScenePctService<?> scenePctService2 = this.fg2zbyyyq8m8;
        if (scenePctService2 instanceof ControlGetAnimPropertyService) {
            ControlGetAnimPropertyService controlGetAnimPropertyService = (ControlGetAnimPropertyService) scenePctService2;
            if (i == 257 || i == 32) {
                controlGetAnimPropertyService.activate();
                return true;
            }
        }
        ScenePctService<?> scenePctService3 = this.fg2zbyyyq8m8;
        if (scenePctService3 instanceof ControlCompactService) {
            ControlCompactService controlCompactService = (ControlCompactService) scenePctService3;
            if (i == 257 || i == 32) {
                controlCompactService.toggle();
                return true;
            }
        }
        ScenePctService<?> scenePctService4 = this.fg2zbyyyq8m8;
        if (scenePctService4 instanceof NumberSettingControl) {
            NumberSettingControl numberSettingControl = (NumberSettingControl) scenePctService4;
            if (i == 263 || i == 262) {
                ModuleSetting.Number number = numberSettingControl.setting();
                if (number.isActive()) {
                    number.set(Float.valueOf(number.get().floatValue() + ((i == 262 ? 1 : -1) * (number.step() > 0.0f ? number.step() : (number.max() - number.min()) / Float.intBitsToFloat(1120403456)))));
                }
                mgf9hldmchqz();
                return true;
            }
        }
        boolean z2 = i == 263 || i == 262;
        float fIntBitsToFloat = (i == 263 || i == 264) ? Float.intBitsToFloat(-1082130432) : 1.0f;
        if (z2) {
            ScenePctService<?> scenePctService5 = this.fg2zbyyyq8m8;
            if (scenePctService5 instanceof ControlOnPreviewHandler) {
                ControlOnPreviewHandler controlOnPreviewHandler = (ControlOnPreviewHandler) scenePctService5;
                ModuleSetting.Range range = controlOnPreviewHandler.setting();
                float fStep = (range.step() > 0.0f ? range.step() : (range.max() - range.min()) / Float.intBitsToFloat(1120403456)) * fIntBitsToFloat;
                if ((i2 & 1) != 0) {
                    controlOnPreviewHandler.trySetRange(range.low(), Math.max(range.low(), range.high() + fStep));
                } else {
                    controlOnPreviewHandler.trySetRange(Math.min(range.high(), range.low() + fStep), range.high());
                }
                mgf9hldmchqz();
                return true;
            }
        }
        if (!(this.f5sedrlfn27b instanceof ModuleSetting.Color)) {
            return false;
        }
        if (!z2 && i != 265 && i != 264) {
            return false;
        }
        if (this.fg2zbyyyq8m8 instanceof ColorGetAnimPropertyService) {
            this.fc6hxrkh2sl1.hue(((this.fc6hxrkh2sl1.hue() + (fIntBitsToFloat / Float.intBitsToFloat(1135869952))) + 1.0f) % 1.0f);
        } else if (this.fg2zbyyyq8m8 instanceof SaturationBrightnessPicker) {
            this.fc6hxrkh2sl1.sv(this.fc6hxrkh2sl1.saturation() + (z2 ? fIntBitsToFloat * Float.intBitsToFloat(1008981770) : 0.0f), this.fc6hxrkh2sl1.brightness() + (z2 ? 0.0f : fIntBitsToFloat * Float.intBitsToFloat(1008981770)));
        } else {
            if (!(this.fg2zbyyyq8m8 instanceof AlphaSliderControl)) {
                return false;
            }
            this.fc6hxrkh2sl1.alpha(this.fc6hxrkh2sl1.alpha() + (fIntBitsToFloat / Float.intBitsToFloat(1132396544)));
        }
        this.f4fzgs7my5mf.invalidateComponent();
        return true;
    }

    private void mfahyvg0apdz() {
        if (this.fg2zbyyyq8m8 != null) {
            mewzql46vnem(this.fg2zbyyyq8m8, false);
        }
        this.fg2zbyyyq8m8 = null;
    }

    private static void mb7v2groceso(ScenePctService<?> scenePctService) {
        float fComputedY;
        float fComputedX;
        ScenePctService<?> scenePctServiceParent = scenePctService.parent();
        while (true) {
            ScenePctService<?> scenePctService2 = scenePctServiceParent;
            if (scenePctService2 == null) {
                return;
            }
            if (scenePctService2.scrollMin() < 0.0f) {
                if (scenePctService.computedY() < scenePctService2.computedY() + Float.intBitsToFloat(1086324736)) {
                    fComputedY = (scenePctService2.computedY() + Float.intBitsToFloat(1086324736)) - scenePctService.computedY();
                } else {
                    fComputedY = scenePctService.computedY() + scenePctService.computedH() > (scenePctService2.computedY() + scenePctService2.computedH()) - Float.intBitsToFloat(1086324736) ? (((scenePctService2.computedY() + scenePctService2.computedH()) - Float.intBitsToFloat(1086324736)) - scenePctService.computedY()) - scenePctService.computedH() : 0.0f;
                }
                float f = fComputedY;
                if ((scenePctService2 instanceof AnimatedSelectionIndicator) || (scenePctService2 instanceof MaterialSelectionSelector)) {
                    if (scenePctService.computedX() < scenePctService2.computedX()) {
                        fComputedX = scenePctService2.computedX() - scenePctService.computedX();
                    } else {
                        fComputedX = scenePctService.computedX() + scenePctService.computedW() > scenePctService2.computedX() + scenePctService2.computedW() ? ((scenePctService2.computedX() + scenePctService2.computedW()) - scenePctService.computedX()) - scenePctService.computedW() : 0.0f;
                    }
                    f = fComputedX;
                }
                if (f != 0.0f) {
                    float fMax = Math.max(scenePctService2.scrollMin(), Math.min(0.0f, scenePctService2.scrollY() + f));
                    scenePctService2.scrollTarget(fMax);
                    MotionAnimateService.animate(scenePctService2, MotionColorsContainer.Floats.SCROLL_Y, fMax, MaterialIsLightService.FAST_SPATIAL);
                }
            }
            scenePctServiceParent = scenePctService2.parent();
        }
    }

    private static void mewzql46vnem(ScenePctService<?> scenePctService, boolean z) {
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
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) scenePctService;
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

    private static void m4m81i8kd5x7(ScenePctService<?> scenePctService) {
        if (scenePctService == null) {
            return;
        }
        if (scenePctService instanceof ControlLetterSpacingService) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) scenePctService;
            if (controlLetterSpacingService.focused()) {
                controlLetterSpacingService.unfocus();
            }
        }
        if (scenePctService instanceof ControlGetAnimPropertyService) {
            ControlGetAnimPropertyService controlGetAnimPropertyService = (ControlGetAnimPropertyService) scenePctService;
            if (controlGetAnimPropertyService.isListening()) {
                controlGetAnimPropertyService.acceptKey(256);
            }
        }
        Iterator it = List.copyOf(scenePctService.children()).iterator();
        while (it.hasNext()) {
            m4m81i8kd5x7((ScenePctService) it.next());
        }
    }

    private static void mj2sjuy2kmae(ScenePctService<?> scenePctService, List<ScenePctService<?>> list) {
        if (scenePctService != null && scenePctService.visible && scenePctService.pointerEvents()) {
            if (((scenePctService instanceof MaterialJoinedService) && ((MaterialJoinedService) scenePctService).available()) || (scenePctService instanceof ControlCompactService) || (scenePctService instanceof SliderControl) || (scenePctService instanceof RangeSliderControl) || (scenePctService instanceof ControlLetterSpacingService) || (scenePctService instanceof ControlGetAnimPropertyService) || (scenePctService instanceof SaturationBrightnessPicker) || (scenePctService instanceof ColorGetAnimPropertyService) || (scenePctService instanceof AlphaSliderControl)) {
                list.add(scenePctService);
            }
            Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
            while (it.hasNext()) {
                mj2sjuy2kmae(it.next(), list);
            }
        }
    }

    private static ControlLetterSpacingService mcudjgl33jla(ScenePctService<?> scenePctService) {
        if (scenePctService == null) {
            return null;
        }
        if (scenePctService instanceof ControlLetterSpacingService) {
            ControlLetterSpacingService controlLetterSpacingService = (ControlLetterSpacingService) scenePctService;
            if (controlLetterSpacingService.focused()) {
                return controlLetterSpacingService;
            }
        }
        Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
        while (it.hasNext()) {
            ControlLetterSpacingService controlLetterSpacingServiceMcudjgl33jla = mcudjgl33jla(it.next());
            if (controlLetterSpacingServiceMcudjgl33jla != null) {
                return controlLetterSpacingServiceMcudjgl33jla;
            }
        }
        return null;
    }

    private static ControlGetAnimPropertyService mh0r9igdkzmt(ScenePctService<?> scenePctService) {
        if (scenePctService == null) {
            return null;
        }
        if (scenePctService instanceof ControlGetAnimPropertyService) {
            ControlGetAnimPropertyService controlGetAnimPropertyService = (ControlGetAnimPropertyService) scenePctService;
            if (controlGetAnimPropertyService.isListening()) {
                return controlGetAnimPropertyService;
            }
        }
        Iterator<ScenePctService<?>> it = scenePctService.children().iterator();
        while (it.hasNext()) {
            ControlGetAnimPropertyService controlGetAnimPropertyServiceMh0r9igdkzmt = mh0r9igdkzmt(it.next());
            if (controlGetAnimPropertyServiceMh0r9igdkzmt != null) {
                return controlGetAnimPropertyServiceMh0r9igdkzmt;
            }
        }
        return null;
    }

    private static ComponentKeyService<SceneTextService> m1z6anxxnaah(String str, int i) {
        return MaterialTextService.label(str, i).props(sceneTextService -> {
            sceneTextService.fontSize(Float.intBitsToFloat(1096810496)).lineHeight(Float.intBitsToFloat(1101004800)).letterSpacing(Float.intBitsToFloat(1036831949));
        });
    }

    private static ComponentKeyService<MaterialJoinedService> mgtqng4c5k74(String str, String str2, Runnable runnable, boolean z) {
        return m32zlilhw8fn(str, str2, runnable, z ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER, z ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER, Float.intBitsToFloat(1094713344));
    }

    private static ComponentKeyService<MaterialJoinedService> m32zlilhw8fn(String str, String str2, Runnable runnable, int i, int i2, float f) {
        return MaterialTextService.button(str, str2, runnable, i, i2, f).props(materialJoinedService -> {
            materialJoinedService.shape(Math.min(Float.intBitsToFloat(1099956224), f), Float.intBitsToFloat(1108344832)).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).minWidth(Float.intBitsToFloat(1109393408)).padding(0.0f, Float.intBitsToFloat(1098907648));
        }).children(m1z6anxxnaah(str2, i2));
    }

    
    private static ComponentKeyService<MaterialJoinedService> m9o0yydzlndu(String str, String str2, String str3, Runnable runnable) {
        return MaterialTextService.iconButton(str, str2, str3, runnable).props(materialJoinedService -> {
            materialJoinedService.shape(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832)).surfaceWidth(Float.intBitsToFloat(1108344832)).size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
        }).children(MaterialTextService.icon(str2, MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1102053376), Float.intBitsToFloat(1102053376));
        }));
    }

    private static String m1909encvz9z(String str) {
        return str.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    private static String m7sclicozxcc(Module module) {
        return "clickgui.category." + m1909encvz9z(module.category().name()) + ".module." + m1909encvz9z(module.name());
    }

    private static String m1vcviafemyn(Module module) {
        return "module:" + m1909encvz9z(module.name());
    }

    private static String mjkwd8iq0qx4(ModuleFeatureType moduleFeatureType) {
        return moduleFeatureType.displayName();
    }

    private static String mfur86tcqz73(String str) {
        return (String) Arrays.stream(str.toLowerCase(Locale.ROOT).split("_")).map(str2 -> {
            return Character.toUpperCase(str2.charAt(0)) + str2.substring(1);
        }).collect(Collectors.joining(" "));
    }

    private static String mfouoi8d08ic(float f, float f2, float f3) {
        return BuiltinRangeService.format(f, f2, f3);
    }

    
    private static String m9xb6xboaqgo(ModuleSetting.CurveType curveType) throws MatchException {
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

    private static String m60hl91212ms(int i) {
        if (i < 0) {
            return "Unassigned";
        }
        if (i >= 320 || (i >= 290 && i <= 314)) {
            return KeyCatalog.name(i);
        }
        switch (i) {
            case GeometryCubeService.DEFAULT_SPHERE_SEGMENTS :
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
                return KeyCatalog.displayLegend(iGlfwGetKeyScancode < 0 ? null : GLFW.glfwGetKeyName(i, iGlfwGetKeyScancode), i);
        }
    }
}
