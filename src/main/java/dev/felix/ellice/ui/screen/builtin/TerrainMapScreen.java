package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.feature.terrain.TerrainCanonicalService;
import dev.felix.ellice.feature.terrain.TerrainKindData;
import dev.felix.ellice.feature.terrain.TerrainMapController;
import dev.felix.ellice.feature.terrain.TerrainRenderer;
import dev.felix.ellice.feature.terrain.TerrainReservedService;
import dev.felix.ellice.feature.terrain.TerrainViewportService;
import dev.felix.ellice.feature.terrain.WaypointPanel;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialJoinedService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.ScenePointerService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.screen.ScreenOperationHandler;
import dev.felix.ellice.ui.screen.ScreenScreenIdService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;


public final class TerrainMapScreen implements ScreenOperationHandler {
    private final TerrainMapController f8b2aec2mdn4;
    private final int f10w46izxkn9;
    private ComponentMountService f5ua4o089agu;
    private ScenePointerService f2jchrjdgh2o;
    private ScreenScreenIdService f86z781lv8g1;
    private boolean f5ubx8tlea6w;
    private boolean f4qjxl2lsmez;
    private boolean fcjuakj5stnl;
    private boolean f7tn36vsulzi;
    private boolean ff4izes3vvbh;
    private boolean f1cio3takyuw;
    private Vector3d f8lt1xfu9umw;
    private TerrainKindData fihdk7eps5ri;
    private long fbsx311vjijx;
    private float f6bjd6ohjy2x = Float.intBitsToFloat(1148846080);
    private float f7nixdcioujo = Float.intBitsToFloat(1143930880);
    private TerrainKindData.Kind ffdwz57zzott = TerrainKindData.Kind.PLACE;
    private final String[] fbyfwnffoqoo = {"0", "64", "0"};
    private int f3obsvk7an4g = MaterialIsLightService.ERROR;
    private String ff65ootfuf00 = "";
    private String fid22j5sbpix = "Waypoint";
    private String fhza61y9iwz5 = "";
    private long fcjixybt9mzk = -1;
    private long f3gbybnq8rz7 = -1;

    public TerrainMapScreen(TerrainMapController terrainMapController, int i) {
        this.f8b2aec2mdn4 = terrainMapController;
        this.f10w46izxkn9 = i;
    }

    @Override 
    public String id() {
        return "terrain-map";
    }

    @Override 
    public float backgroundDesaturation() {
        return 0.0f;
    }

    @Override 
    public ScenePctService<?> build(ScreenScreenIdService screenScreenIdService) {
        this.f86z781lv8g1 = screenScreenIdService;
        SceneCornerRadiusService sceneCornerRadiusServiceDirection = new MaterialTerrainTooltipsService().backgroundColor(MaterialIsLightService.SURFACE_LOWEST).direction(ScenePctService.Direction.NONE);
        this.f5ua4o089agu = new ComponentMountService().mount(this::mamnujfu5v0j, screenScreenIdService.theme());
        this.f5ua4o089agu.size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
        sceneCornerRadiusServiceDirection.addChild(this.f5ua4o089agu);
        return sceneCornerRadiusServiceDirection;
    }

    @Override 
    public void onOpen(ScreenScreenIdService screenScreenIdService) {
        this.f8b2aec2mdn4.expanded(true);
    }

    @Override 
    public void onClose(ScreenScreenIdService screenScreenIdService) {
        this.f8b2aec2mdn4.expanded(false);
        this.f8b2aec2mdn4.camera(true).endDrag();
    }

    public void viewport(float f, float f2) {
        if (this.f6bjd6ohjy2x == f && this.f7nixdcioujo == f2) {
            return;
        }
        this.f6bjd6ohjy2x = f;
        this.f7nixdcioujo = f2;
        mbnhko45bvwk();
    }

    @Override 
    public void tick(ScreenScreenIdService screenScreenIdService) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            screenScreenIdService.close();
            return;
        }
        viewport(CoreIsInitializedHandler.get().viewport().width(), CoreIsInitializedHandler.get().viewport().height());
        if (this.fcjixybt9mzk != this.f8b2aec2mdn4.revision()) {
            this.fcjixybt9mzk = this.f8b2aec2mdn4.revision();
            mbnhko45bvwk();
        }
        long jNanoTime = System.nanoTime();
        if (this.f3gbybnq8rz7 != this.f8b2aec2mdn4.navigation().revision() || (this.f8b2aec2mdn4.navigation().target() != null && jNanoTime - this.fbsx311vjijx > 500000000)) {
            this.f3gbybnq8rz7 = this.f8b2aec2mdn4.navigation().revision();
            this.fbsx311vjijx = jNanoTime;
            mbnhko45bvwk();
        }
        if (this.f2jchrjdgh2o == null) {
            return;
        }
        long jWindowHandle = CompatAdapterService.windowHandle(minecraft);
        this.f2jchrjdgh2o.pointer(new ScenePointerService.Pointer(CoreIsInitializedHandler.get().viewport().mouseX(minecraft.mouseHandler.xpos()), CoreIsInitializedHandler.get().viewport().mouseY(minecraft.mouseHandler.ypos()), GLFW.glfwGetMouseButton(jWindowHandle, 1) == 1 || GLFW.glfwGetMouseButton(jWindowHandle, 2) == 1, miymh772hi1(jWindowHandle, 340) || miymh772hi1(jWindowHandle, 344), miymh772hi1(jWindowHandle, 341) || miymh772hi1(jWindowHandle, 345), (float) CoreIsInitializedHandler.get().viewport().renderScale()));
        if (CoreIsInitializedHandler.get().scene().hasFocusedInput()) {
            return;
        }
        int i = ((miymh772hi1(jWindowHandle, 263) || miymh772hi1(jWindowHandle, 65)) ? 1 : 0) - ((miymh772hi1(jWindowHandle, 262) || miymh772hi1(jWindowHandle, 68)) ? 1 : 0);
        int i2 = ((miymh772hi1(jWindowHandle, 265) || miymh772hi1(jWindowHandle, 87)) ? 1 : 0) - ((miymh772hi1(jWindowHandle, 264) || miymh772hi1(jWindowHandle, 83)) ? 1 : 0);
        if (i == 0 && i2 == 0) {
            return;
        }
        TerrainViewportService terrainViewportServiceCamera = this.f8b2aec2mdn4.camera(true);
        terrainViewportServiceCamera.beginDrag();
        terrainViewportServiceCamera.pan(this.f6bjd6ohjy2x / 2.0f, this.f7nixdcioujo / 2.0f, (this.f6bjd6ohjy2x / 2.0f) + (i * screenScreenIdService.deltaTime() * Float.intBitsToFloat(1137180672)), (this.f7nixdcioujo / 2.0f) + (i2 * screenScreenIdService.deltaTime() * Float.intBitsToFloat(1137180672)), 0.0d);
        terrainViewportServiceCamera.endDrag();
    }

    private static boolean miymh772hi1(long j, int i) {
        return GLFW.glfwGetKey(j, i) == 1;
    }

    @Override 
    public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int i, int i2) {
        if (i == 256 && this.f8b2aec2mdn4.draggingWaypoint()) {
            this.f8b2aec2mdn4.cancelWaypointDrag();
            return true;
        }
        if (i == 256 && this.f5ubx8tlea6w) {
            this.fcjuakj5stnl = false;
            this.f5ubx8tlea6w = false;
            mbnhko45bvwk();
            return true;
        }
        if (CoreIsInitializedHandler.get().scene().hasFocusedInput()) {
            return false;
        }
        if (i == this.f10w46izxkn9) {
            screenScreenIdService.close();
            return true;
        }
        if (i == 70) {
            this.f8b2aec2mdn4.returnToPlayer();
            return true;
        }
        if (i == 78) {
            this.f8b2aec2mdn4.camera(true).northUp();
            return true;
        }
        if (i == 61 || i == 334) {
            mje92gq708ta(1.0d);
            return true;
        }
        if (i != 45 && i != 333) {
            return i >= 262 && i <= 265;
        }
        mje92gq708ta(Double.longBitsToDouble(-4616189618054758400L));
        return true;
    }

    private ComponentKeyService<?> mamnujfu5v0j(ComponentThemeService componentThemeService) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ComponentBoxService.node("terrain-map-viewport", () -> {
            return new ScenePointerService(this.f8b2aec2mdn4, true);
        }, scenePointerService -> {
            this.f2jchrjdgh2o = scenePointerService;
            ((ScenePointerService) scenePointerService.id("terrain-map.canvas")).size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            scenePointerService.onPlace(this::mbo7sqtt0gkc).onWaypoint(this::select);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new TerrainReservedService.Box(0.0f, 0.0f, this.f6bjd6ohjy2x, Float.intBitsToFloat(1115684864)));
            boolean z = this.f6bjd6ohjy2x < Float.intBitsToFloat(1141637120) || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
            if (this.f8b2aec2mdn4.navigation().target() != null) {
                arrayList2.add(new TerrainReservedService.Box(Float.intBitsToFloat(1094713344), (this.f7nixdcioujo - (z ? 74 : 16)) - Float.intBitsToFloat(1125253120), Math.min(Float.intBitsToFloat(1135214592), this.f6bjd6ohjy2x - (z ? 32 : 82)) + Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1125515264)));
            }
            if (this.f5ubx8tlea6w) {
                arrayList2.add(new TerrainReservedService.Box(Float.intBitsToFloat(1090519040), (this.f6bjd6ohjy2x < Float.intBitsToFloat(1142292480) || this.f7nixdcioujo < Float.intBitsToFloat(1135542272)) ? Float.intBitsToFloat(1082130432) : Float.intBitsToFloat(1116471296), Math.min(Float.intBitsToFloat(1133248512), this.f6bjd6ohjy2x - Float.intBitsToFloat(1103101952)) + Float.intBitsToFloat(1090519040), this.f7nixdcioujo - Float.intBitsToFloat(1117782016)));
            }
            scenePointerService.reserved(arrayList2);
        }, new ComponentKeyService[0]).key("viewport"));
        ComponentStyleService componentStyleServiceBuild = ComponentStyleService.style().absolute().inset(LayoutOperationHandler.px(Float.intBitsToFloat(1092616192)), LayoutOperationHandler.px(Float.intBitsToFloat(1092616192)), LayoutOperationHandler.auto(), LayoutOperationHandler.px(Float.intBitsToFloat(1092616192))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).gap(Float.intBitsToFloat(1086324736)).align(ScenePctService.Align.CENTER).build();
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.direction(ScenePctService.Direction.ROW).layerBreak(true);
        };
        ComponentStyleService componentStyleServiceBuild2 = ComponentStyleService.style().grow(1.0f).maxWidth(LayoutOperationHandler.px(Float.intBitsToFloat(1136525312))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560))).padding(0.0f, Float.intBitsToFloat(1096810496)).gap(Float.intBitsToFloat(1092616192)).align(ScenePctService.Align.CENTER).build();
        Consumer<SceneCornerRadiusService> consumer2 = sceneCornerRadiusService2 -> {
            sceneCornerRadiusService2.direction(ScenePctService.Direction.ROW).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1103101952)).shadow(Float.intBitsToFloat(1084227584));
        };
        ComponentKeyService[] componentKeyServiceArr = {MaterialTextService.icon("search", MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneSrcService -> {
            sceneSrcService.size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
        }), ComponentBoxService.node("terrain-search", ControlLetterSpacingService::new, controlLetterSpacingService -> {
            controlLetterSpacingService.id("terrain-map.search").fontFamily(MaterialIsLightService.FONT).fontSize(Float.intBitsToFloat(1095761920)).placeholder(this.f6bjd6ohjy2x < Float.intBitsToFloat(1140981760) ? "Search…" : "Waypoint or X, Z coordinates").bgColor(0).textColor(MaterialIsLightService.ON_SURFACE).placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT).focusBorder(0).selectionColor(1716537957).height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408))).flex(1.0f).minWidth(0.0f).maxLength(80);
            controlLetterSpacingService.onChanged(str -> {
                this.ff65ootfuf00 = str;
            }).onSubmit(str2 -> {
                m9gyxvp3bdlr();
            });
        }, new ComponentKeyService[0]).onMount(controlLetterSpacingService2 -> {
            controlLetterSpacingService2.text(this.ff65ootfuf00);
        }).key("search-input")};
        ScreenScreenIdService screenScreenIdService = this.f86z781lv8g1;
        Objects.requireNonNull(screenScreenIdService);
        arrayList.add(ComponentBoxService.panel(componentStyleServiceBuild, consumer, m9b6ckf4291n("terrain-map.waypoints", "star", "Waypoints", () -> {
            this.f5ubx8tlea6w = !this.f5ubx8tlea6w;
            this.ff4izes3vvbh = false;
            this.fcjuakj5stnl = false;
            mbnhko45bvwk();
        }), ComponentBoxService.panel(componentStyleServiceBuild2, consumer2, componentKeyServiceArr), m9b6ckf4291n("terrain-map.add", "add", "Add waypoint here · Shift-click terrain", this::mij4g3vq13lf), m9b6ckf4291n("terrain-map.close", "close", "Close map", screenScreenIdService::close)).key("toolbar"));
        boolean z = this.f6bjd6ohjy2x < Float.intBitsToFloat(1141637120) || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
        ComponentStyleService componentStyleServiceBuild3 = ComponentStyleService.style().absolute().inset(LayoutOperationHandler.auto(), LayoutOperationHandler.px(Float.intBitsToFloat(1094713344)), LayoutOperationHandler.px(Float.intBitsToFloat(1098907648)), LayoutOperationHandler.auto()).width(LayoutOperationHandler.px(z ? Float.intBitsToFloat(1129709568) : Float.intBitsToFloat(1110966272))).padding(Float.intBitsToFloat(1077936128)).gap(2.0f).build();
        Consumer<SceneCornerRadiusService> consumer3 = sceneCornerRadiusService3 -> {
            sceneCornerRadiusService3.direction(z ? ScenePctService.Direction.ROW : ScenePctService.Direction.COLUMN).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1101004800)).shadow(Float.intBitsToFloat(1086324736)).layerBreak(true);
        };
        ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[5];
        componentKeyServiceArr2[0] = mz8vzt5lbmt("+", "Zoom in", () -> {
            mje92gq708ta(1.0d);
        });
        componentKeyServiceArr2[1] = mz8vzt5lbmt("−", "Zoom out", () -> {
            mje92gq708ta(Double.longBitsToDouble(-4616189618054758400L));
        });
        componentKeyServiceArr2[2] = mz8vzt5lbmt("N", "North up", () -> {
            this.f8b2aec2mdn4.camera(true).northUp();
        });
        componentKeyServiceArr2[3] = mz8vzt5lbmt(this.f4qjxl2lsmez ? "3D" : "2D", "Toggle overhead view", () -> {
            this.f4qjxl2lsmez = !this.f4qjxl2lsmez;
            this.f8b2aec2mdn4.camera(true).topDown(this.f4qjxl2lsmez);
            mbnhko45bvwk();
        });
        TerrainMapController terrainMapController = this.f8b2aec2mdn4;
        Objects.requireNonNull(terrainMapController);
        componentKeyServiceArr2[4] = m9b6ckf4291n("terrain-map.player", "person", "Return to player · F", terrainMapController::returnToPlayer);
        arrayList.add(ComponentBoxService.panel(componentStyleServiceBuild3, consumer3, componentKeyServiceArr2).key("navigation"));
        if (this.f8b2aec2mdn4.navigation().target() != null) {
            arrayList.add(ComponentBoxService.column(ComponentStyleService.style().absolute().inset(LayoutOperationHandler.auto(), LayoutOperationHandler.auto(), LayoutOperationHandler.px(z ? Float.intBitsToFloat(1116995584) : Float.intBitsToFloat(1098907648)), LayoutOperationHandler.px(Float.intBitsToFloat(1098907648))).width(LayoutOperationHandler.px(Math.min(Float.intBitsToFloat(1135214592), this.f6bjd6ohjy2x - (z ? 32 : 82)))).build(), (ComponentKeyService<?>[]) new ComponentKeyService[]{new WaypointPanel(this.f8b2aec2mdn4).render(componentThemeService)}).key("navigation-card-host"));
        } else if (this.f6bjd6ohjy2x > Float.intBitsToFloat(1142620160) && this.f7nixdcioujo > Float.intBitsToFloat(1136525312)) {
            arrayList.add(ComponentBoxService.panel(ComponentStyleService.style().absolute().inset(LayoutOperationHandler.auto(), LayoutOperationHandler.auto(), LayoutOperationHandler.px(Float.intBitsToFloat(1099956224)), LayoutOperationHandler.px(Float.intBitsToFloat(1099956224))).padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1094713344)).build(), sceneCornerRadiusService4 -> {
                sceneCornerRadiusService4.backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1094713344)).layerBreak(true);
            }, MaterialTextService.text("Drag to pan · Scroll to zoom · Right-drag to orbit", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT)).key("hint"));
        }
        if (this.f5ubx8tlea6w) {
            arrayList.add(mc4xaoci2n29());
        }
        return ComponentBoxService.stack(ComponentStyleService.style().size(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)), LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).build(), (ComponentKeyService<?>[]) arrayList.toArray(i -> {
            return new ComponentKeyService[i];
        }));
    }

    private ComponentKeyService<?> mc4xaoci2n29() {
        String str;
        String str2;
        boolean z = this.f6bjd6ohjy2x < Float.intBitsToFloat(1142292480) || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
        ArrayList arrayList = new ArrayList();
        ComponentStyleService componentStyleServiceBuild = ComponentStyleService.style().width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).align(ScenePctService.Align.CENTER).gap(Float.intBitsToFloat(1090519040)).build();
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
        componentKeyServiceArr[0] = MaterialTextService.iconButton("map.sheet.back", "arrow_back", "Back", () -> {
            if (this.f1cio3takyuw) {
                this.f1cio3takyuw = false;
            } else if (this.fcjuakj5stnl) {
                this.fcjuakj5stnl = false;
                this.ff4izes3vvbh = this.fihdk7eps5ri != null;
            } else {
                this.ff4izes3vvbh = false;
            }
            this.fhza61y9iwz5 = "";
            mbnhko45bvwk();
        }).props(materialJoinedService -> {
            materialJoinedService.visible(this.fcjuakj5stnl || this.ff4izes3vvbh);
        });
        if (this.fcjuakj5stnl) {
            str = this.fihdk7eps5ri == null ? "New waypoint" : "Edit waypoint";
        } else if (this.f1cio3takyuw) {
            str = "Portal planner";
        } else {
            str = this.ff4izes3vvbh ? "Place details" : "Waypoints";
        }
        componentKeyServiceArr[1] = MaterialTextService.label(str, MaterialIsLightService.ON_SURFACE).props(sceneTextService -> {
            sceneTextService.flex(1.0f).minWidth(0.0f);
        });
        componentKeyServiceArr[2] = MaterialTextService.iconButton("terrain-map.sheet.close", "close", "Close waypoints", () -> {
            this.fcjuakj5stnl = false;
            this.f5ubx8tlea6w = false;
            mbnhko45bvwk();
        });
        arrayList.add(ComponentBoxService.row(componentStyleServiceBuild, (ComponentKeyService<?>[]) componentKeyServiceArr));
        ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
        if (this.fcjuakj5stnl) {
            arrayList2.add(ComponentBoxService.node("waypoint-name", ControlLetterSpacingService::new, controlLetterSpacingService -> {
                MaterialTextService.input(controlLetterSpacingService);
                controlLetterSpacingService.id("terrain-map.waypoint.name").fontSize(Float.intBitsToFloat(1096810496)).placeholder("Name").maxLength(64).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)));
                controlLetterSpacingService.onChanged(str3 -> {
                    this.fid22j5sbpix = str3;
                }).onSubmit(str4 -> {
                    m2f5t2pa9utb();
                });
            }, new ComponentKeyService[0]).onMount(controlLetterSpacingService2 -> {
                controlLetterSpacingService2.text(this.fid22j5sbpix);
            }).key("name"));
            arrayList2.add(MaterialTextService.text("Marker type", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT));
            arrayList2.add(mj61sis27po1(true));
            arrayList2.add(m1h0nm6ynhk9());
            arrayList2.add(MaterialTextService.button("terrain-map.save", "Save waypoint", this::m2f5t2pa9utb, true));
            arrayList2.add(MaterialTextService.button("terrain-map.cancel", "Cancel", () -> {
                this.fcjuakj5stnl = false;
                mbnhko45bvwk();
            }, false));
        } else if (!this.ff4izes3vvbh || this.fihdk7eps5ri == null) {
            Consumer<LayoutContainerNode> consumer = layoutContainerNode -> {
                layoutContainerNode.gap(Float.intBitsToFloat(1086324736)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
            };
            ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
            componentKeyServiceArr2[0] = mharlko7tp59("map.filter.all", this.f8b2aec2mdn4.filter() == null ? "All types ✓" : "All types", () -> {
                this.f8b2aec2mdn4.filter(null);
                mbnhko45bvwk();
            });
            componentKeyServiceArr2[1] = mharlko7tp59("map.dimensions", this.f7tn36vsulzi ? "All dimensions" : "This dimension", () -> {
                this.f7tn36vsulzi = !this.f7tn36vsulzi;
                mbnhko45bvwk();
            });
            arrayList2.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) consumer, (ComponentKeyService<?>[]) componentKeyServiceArr2));
            arrayList2.add(mj61sis27po1(false));
            TerrainKindData terrainKindDataLastDeath = this.f8b2aec2mdn4.lastDeath();
            if (terrainKindDataLastDeath != null) {
                arrayList2.add(MaterialTextService.button("map.last-death", "Last death · " + TerrainCanonicalService.label(terrainKindDataLastDeath.dimension()), () -> {
                    this.f7tn36vsulzi = !this.f8b2aec2mdn4.inCurrentDimension(terrainKindDataLastDeath);
                    this.f8b2aec2mdn4.filter(null);
                    select(terrainKindDataLastDeath);
                    this.f8b2aec2mdn4.goTo(terrainKindDataLastDeath);
                }, MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER, Float.intBitsToFloat(1096810496)));
            }
            List<TerrainKindData> list = (this.f7tn36vsulzi ? this.f8b2aec2mdn4.savedWaypoints() : this.f8b2aec2mdn4.waypoints()).stream().filter(terrainKindData -> {
                return this.f8b2aec2mdn4.filter() == null || terrainKindData.kind() == this.f8b2aec2mdn4.filter();
            }).filter(terrainKindData2 -> {
                return this.ff65ootfuf00.isBlank() || terrainKindData2.name().toLowerCase(Locale.ROOT).contains(this.ff65ootfuf00.toLowerCase(Locale.ROOT));
            }).sorted(Comparator.comparingInt((TerrainKindData terrainKindData3) -> {
                return (this.fihdk7eps5ri == null || !terrainKindData3.id().equals(this.fihdk7eps5ri.id())) ? 1 : 0;
            }).thenComparing(Comparator.comparingLong((TerrainKindData v0) -> {
                return v0.createdAt();
            }).reversed())).toList();
            if (list.isEmpty()) {
                arrayList2.add(MaterialTextService.text(this.ff65ootfuf00.isBlank() ? "Your places, saved here. Shift-click the terrain to add one." : "No matching waypoints.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService2 -> {
                    sceneTextService2.wordWrap(true);
                }));
            }
            for (TerrainKindData terrainKindData4 : list) {
                arrayList2.add(ComponentBoxService.panel(ComponentStyleService.style().width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(Float.intBitsToFloat(1092616192)).gap(Float.intBitsToFloat(1077936128)).build(), sceneCornerRadiusService -> {
                    sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN).backgroundColor((this.fihdk7eps5ri == null || !terrainKindData4.id().equals(this.fihdk7eps5ri.id())) ? MaterialIsLightService.SURFACE_HIGH : MaterialIsLightService.SECONDARY_CONTAINER).cornerRadius(Float.intBitsToFloat(1094713344)).onClick(() -> {
                        select(terrainKindData4);
                        this.f8b2aec2mdn4.goTo(terrainKindData4);
                    });
                }, ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                    layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1090519040)).align(ScenePctService.Align.CENTER);
                }, (ComponentKeyService<?>[]) new ComponentKeyService[]{ComponentBoxService.node("place-kind", TerrainRenderer.GlyphNode::new, glyphNode -> {
                    glyphNode.glyph(terrainKindData4.kind().icon(), terrainKindData4.color()).size(Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1103101952)).flexShrink(0.0f);
                }, new ComponentKeyService[0]), MaterialTextService.label(terrainKindData4.name(), MaterialIsLightService.ON_SURFACE).props(sceneTextService3 -> {
                    sceneTextService3.flex(1.0f).minWidth(0.0f);
                })}), MaterialTextService.text(terrainKindData4.kind().label() + " · " + TerrainCanonicalService.label(terrainKindData4.dimension()), Float.intBitsToFloat(1092616192), terrainKindData4.color()), MaterialTextService.text(mbp4bzt5dv3z(terrainKindData4.x(), terrainKindData4.y(), terrainKindData4.z()), Float.intBitsToFloat(1092616192), MaterialIsLightService.ON_SURFACE_VARIANT)).key(terrainKindData4.id().toString()));
            }
            arrayList2.add(MaterialTextService.button("terrain-map.new", "Add waypoint here", this::mij4g3vq13lf, true));
        } else {
            arrayList2.add(MaterialTextService.text(this.fihdk7eps5ri.name(), Float.intBitsToFloat(1099956224), MaterialIsLightService.ON_SURFACE).props(sceneTextService4 -> {
                sceneTextService4.fontFamily(MaterialIsLightService.MEDIUM).wordWrap(true);
            }));
            arrayList2.add(MaterialTextService.text(this.fihdk7eps5ri.kind().label() + " · " + TerrainCanonicalService.label(this.fihdk7eps5ri.dimension()), Float.intBitsToFloat(1093664768), this.fihdk7eps5ri.color()));
            arrayList2.add(MaterialTextService.text(mbp4bzt5dv3z(this.fihdk7eps5ri.x(), this.fihdk7eps5ri.y(), this.fihdk7eps5ri.z()), Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT));
            if (this.fihdk7eps5ri.kind() == TerrainKindData.Kind.DEATH && this.fihdk7eps5ri.createdAt() > 0) {
                arrayList2.add(MaterialTextService.text(DateTimeFormatter.ofPattern("dd MMM · HH:mm").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(this.fihdk7eps5ri.createdAt())), Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT));
            }
            arrayList2.addAll(mhrvg6rszqbv());
        }
        if (!this.fhza61y9iwz5.isEmpty()) {
            arrayList2.add(MaterialTextService.text(this.fhza61y9iwz5, Float.intBitsToFloat(1094713344), this.f3obsvk7an4g).props(sceneTextService5 -> {
                sceneTextService5.wordWrap(true);
            }));
        }
        arrayList2.replaceAll(componentKeyService -> {
            return componentKeyService.props(scenePctService -> {
                scenePctService.flexShrink(0.0f);
            });
        });
        ComponentKeyService<LayoutContainerNode> componentKeyServiceColumn = ComponentBoxService.column(ComponentStyleService.style().width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).grow(1.0f).minHeight(LayoutOperationHandler.px(0.0f)).gap(Float.intBitsToFloat(1092616192)).scroll(true).build(), (ComponentKeyService<?>[]) arrayList2.toArray(i -> {
            return new ComponentKeyService[i];
        }));
        if (this.fcjuakj5stnl) {
            str2 = "editor";
        } else if (this.f1cio3takyuw) {
            str2 = "portal";
        } else {
            str2 = this.ff4izes3vvbh ? "details" : "places";
        }
        arrayList.add(componentKeyServiceColumn.key(str2));
        return ComponentBoxService.panel(ComponentStyleService.style().absolute().inset(LayoutOperationHandler.px(z ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1116995584)), LayoutOperationHandler.auto(), LayoutOperationHandler.px(Float.intBitsToFloat(1096810496)), LayoutOperationHandler.px(Float.intBitsToFloat(1094713344))).width(LayoutOperationHandler.px(Math.min(Float.intBitsToFloat(1133248512), this.f6bjd6ohjy2x - Float.intBitsToFloat(1103101952)))).padding(Float.intBitsToFloat(1096810496)).gap(Float.intBitsToFloat(1090519040)).build(), sceneCornerRadiusService2 -> {
            sceneCornerRadiusService2.direction(ScenePctService.Direction.COLUMN).backgroundColor(MaterialIsLightService.SURFACE_CONTAINER).cornerRadius(Float.intBitsToFloat(1102053376)).shadow(Float.intBitsToFloat(1092616192)).clip(true).layerBreak(true);
        }, (ComponentKeyService[]) arrayList.toArray(i2 -> {
            return new ComponentKeyService[i2];
        })).key("waypoint-sheet").onMount(sceneCornerRadiusService3 -> {
            sceneCornerRadiusService3.translateX(Float.intBitsToFloat(-1056964608));
            MotionAnimateService.animate(sceneCornerRadiusService3, MotionColorsContainer.Floats.TRANSLATE_X, 0.0f, SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)));
        });
    }

    private List<ComponentKeyService<?>> mhrvg6rszqbv() {
        ArrayList arrayList = new ArrayList();
        TerrainCanonicalService.PortalPlan portalPlanPortal = TerrainCanonicalService.portal(this.fihdk7eps5ri);
        if (portalPlanPortal == null || !this.f1cio3takyuw) {
            if (this.f8b2aec2mdn4.inCurrentDimension(this.fihdk7eps5ri)) {
                arrayList.add(MaterialTextService.button("terrain-map.navigate", "Directions", () -> {
                    this.f8b2aec2mdn4.navigate(this.fihdk7eps5ri);
                    this.f5ubx8tlea6w = false;
                    mbnhko45bvwk();
                }, true).children(MaterialTextService.icon("navigation", MaterialIsLightService.ON_PRIMARY), MaterialTextService.label("Directions", MaterialIsLightService.ON_PRIMARY)));
            } else {
                arrayList.add(MaterialTextService.text("Enter " + TerrainCanonicalService.label(this.fihdk7eps5ri.dimension()) + " to navigate to this place.", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> {
                    sceneTextService.wordWrap(true);
                }));
            }
            arrayList.add(MaterialTextService.button("map.copy", "Copy coordinates", () -> {
                ma4zgy6tk1i3(mh7f5o5bgmn7(this.fihdk7eps5ri));
            }, false));
            arrayList.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1086324736));
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{mharlko7tp59("terrain-map.edit", "Edit marker", this::meswxw1l9635), mharlko7tp59("terrain-map.remove", "Remove", () -> {
                this.f8b2aec2mdn4.remove(this.fihdk7eps5ri.id());
                this.fihdk7eps5ri = null;
                this.ff4izes3vvbh = false;
                mbnhko45bvwk();
            })}));
            if (portalPlanPortal != null) {
                arrayList.add(MaterialTextService.button("map.portal.toggle", TerrainCanonicalService.label(portalPlanPortal.dimension()) + " portal planner", () -> {
                    this.f1cio3takyuw = !this.f1cio3takyuw;
                    mbnhko45bvwk();
                }, MaterialIsLightService.SECONDARY_CONTAINER, MaterialIsLightService.ON_SECONDARY_CONTAINER, Float.intBitsToFloat(1096810496)));
            }
            return arrayList;
        }
        Consumer<SceneCornerRadiusService> consumer = sceneCornerRadiusService -> {
            sceneCornerRadiusService.direction(ScenePctService.Direction.COLUMN).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).padding(Float.intBitsToFloat(1094713344)).gap(Float.intBitsToFloat(1086324736)).cornerRadius(Float.intBitsToFloat(1098907648)).backgroundColor(MaterialIsLightService.SURFACE_HIGH);
        };
        ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[5];
        componentKeyServiceArr[0] = MaterialTextService.text("Portal planner · " + TerrainCanonicalService.label(portalPlanPortal.dimension()), Float.intBitsToFloat(1094713344), MaterialIsLightService.PRIMARY).props(sceneTextService2 -> {
            sceneTextService2.fontFamily(MaterialIsLightService.MEDIUM);
        });
        componentKeyServiceArr[1] = MaterialTextService.text(portalPlanPortal.coordinates(), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE);
        componentKeyServiceArr[2] = MaterialTextService.text(portalPlanPortal.insideBorder() ? "X/Z projection · choose the height on site" : "Outside the world border", Float.intBitsToFloat(1092616192), portalPlanPortal.insideBorder() ? MaterialIsLightService.ON_SURFACE_VARIANT : MaterialIsLightService.ERROR).props(sceneTextService3 -> {
            sceneTextService3.wordWrap(true);
        });
        componentKeyServiceArr[3] = mharlko7tp59("map.portal.copy", "Copy projected X/Z", () -> {
            long jFloor = (long) Math.floor(portalPlanPortal.x());
            ma4zgy6tk1i3(jFloor + " " + this);
        }).props(materialJoinedService -> {
            materialJoinedService.colors(MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.ON_SURFACE);
        });
        componentKeyServiceArr[4] = mharlko7tp59("map.portal.save", "Save planned portal", () -> {
            TerrainKindData terrainKindDataPlanPortal = this.f8b2aec2mdn4.planPortal(this.fihdk7eps5ri);
            if (terrainKindDataPlanPortal != null) {
                this.f7tn36vsulzi = true;
                this.f8b2aec2mdn4.filter(null);
                select(terrainKindDataPlanPortal);
            } else {
                this.fhza61y9iwz5 = portalPlanPortal.insideBorder() ? "Could not save the portal plan." : "The projected position is outside the world border.";
                this.f3obsvk7an4g = MaterialIsLightService.ERROR;
                mbnhko45bvwk();
            }
        }).props(materialJoinedService2 -> {
            materialJoinedService2.colors(MaterialIsLightService.PRIMARY, MaterialIsLightService.ON_PRIMARY);
        }).children(MaterialTextService.text("Save planned portal", Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_PRIMARY));
        arrayList.add(ComponentBoxService.panel(consumer, componentKeyServiceArr));
        return arrayList;
    }

    private ComponentKeyService<?> mj61sis27po1(boolean z) {
        TerrainKindData.Kind[] kindArrValues = TerrainKindData.Kind.values();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= kindArrValues.length) {
                return ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1084227584));
                }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                }));
            }
            ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
            int i4 = i2;
            while (true) {
                int i5 = i4;
                if (i5 < Math.min(i2 + 3, kindArrValues.length)) {
                    TerrainKindData.Kind kind = kindArrValues[i5];
                    boolean z2 = kind == (z ? this.ffdwz57zzott : this.f8b2aec2mdn4.filter());
                    int iColor = z2 ? MaterialIsLightService.ON_PRIMARY : kind.color();
                    ComponentKeyService<?> componentKeyServiceProps = MaterialTextService.button("map.kind." + (z ? "edit." : "filter.") + String.valueOf(kind), kind.label(), () -> {
                        if (z) {
                            this.ffdwz57zzott = kind;
                        } else {
                            this.f8b2aec2mdn4.filter(z2 ? null : kind);
                        }
                        mbnhko45bvwk();
                    }, z2 ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SURFACE_HIGH, iColor, Float.intBitsToFloat(1094713344)).props(materialJoinedService -> {
                        materialJoinedService.shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832));
                        materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832))).flex(1.0f).minWidth(0.0f).padding(Float.intBitsToFloat(1084227584)).gap(Float.intBitsToFloat(1077936128));
                    });
                    ComponentKeyService<?>[] componentKeyServiceArr = new ComponentKeyService[2];
                    componentKeyServiceArr[0] = ComponentBoxService.node("kind-icon", TerrainRenderer.GlyphNode::new, glyphNode -> {
                        glyphNode.glyph(kind.icon(), iColor).size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800)).flexShrink(0.0f);
                    }, new ComponentKeyService[0]);
                    componentKeyServiceArr[1] = MaterialTextService.text(kind.label(), Float.intBitsToFloat(1092616192), z2 ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE);
                    arrayList2.add(componentKeyServiceProps.children(componentKeyServiceArr).key(kind.name()));
                    i4 = i5 + 1;
                } else {
                    break;
                }
            }
            arrayList.add(ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).flexShrink(0.0f).gap(Float.intBitsToFloat(1084227584));
            }, (ComponentKeyService<?>[]) arrayList2.toArray(i6 -> {
                return new ComponentKeyService[i6];
            })));
            i = i2 + 3;
        }
    }

    private ComponentKeyService<?> m1h0nm6ynhk9() {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"X", "Y", "Z"};
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 3) {
                return ComponentBoxService.row((Consumer<LayoutContainerNode>) layoutContainerNode -> {
                    layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).gap(Float.intBitsToFloat(1088421888));
                }, (ComponentKeyService<?>[]) arrayList.toArray(i3 -> {
                    return new ComponentKeyService[i3];
                }));
            }
            arrayList.add(ComponentBoxService.column((Consumer<LayoutContainerNode>) layoutContainerNode2 -> {
                layoutContainerNode2.flex(1.0f).minWidth(0.0f).gap(Float.intBitsToFloat(1082130432));
            }, (ComponentKeyService<?>[]) new ComponentKeyService[]{MaterialTextService.text(strArr[i2], Float.intBitsToFloat(1092616192), MaterialIsLightService.ON_SURFACE_VARIANT), ComponentBoxService.node("coordinate", ControlLetterSpacingService::new, controlLetterSpacingService -> {
                MaterialTextService.input(controlLetterSpacingService);
                controlLetterSpacingService.id("map.coordinate." + strArr[i2]).fontSize(Float.intBitsToFloat(1093664768)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1108869120))).maxLength(16);
                controlLetterSpacingService.onChanged(str -> {
                    this.fbyfwnffoqoo[i2] = str;
                }).onSubmit(str2 -> {
                    m2f5t2pa9utb();
                });
            }, new ComponentKeyService[0]).key("coordinate." + strArr[i2]).onMount(controlLetterSpacingService2 -> {
                controlLetterSpacingService2.text(this.fbyfwnffoqoo[i2]);
            })}));
            i = i2 + 1;
        }
    }

    private void meswxw1l9635() {
        this.f8lt1xfu9umw = new Vector3d(this.fihdk7eps5ri.x(), this.fihdk7eps5ri.y(), this.fihdk7eps5ri.z());
        maoi2jqbxibe(this.f8lt1xfu9umw);
        this.fid22j5sbpix = this.fihdk7eps5ri.name();
        this.ffdwz57zzott = this.fihdk7eps5ri.kind();
        this.fcjuakj5stnl = true;
        this.fhza61y9iwz5 = "";
        mbnhko45bvwk();
    }

    public void edit(TerrainKindData terrainKindData) {
        select(terrainKindData);
        meswxw1l9635();
    }

    private void maoi2jqbxibe(Vector3d vector3d) {
        this.fbyfwnffoqoo[0] = String.valueOf(vector3d.x);
        this.fbyfwnffoqoo[1] = String.valueOf(vector3d.y);
        this.fbyfwnffoqoo[2] = String.valueOf(vector3d.z);
    }

    private ComponentKeyService<MaterialJoinedService> mharlko7tp59(String str, String str2, Runnable runnable) {
        return MaterialTextService.button(str, str2, runnable, MaterialIsLightService.SURFACE_HIGH, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1094713344)).props(materialJoinedService -> {
            materialJoinedService.shape(Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107820544));
            materialJoinedService.height(LayoutOperationHandler.px(Float.intBitsToFloat(1107820544))).flexGrow(1.0f).flexShrink(0.0f).minWidth(0.0f).padding(Float.intBitsToFloat(1086324736));
        }).children(MaterialTextService.text(str2, Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE));
    }

    private static String mh7f5o5bgmn7(TerrainKindData terrainKindData) {
        long jFloor = (long) Math.floor(terrainKindData.x());
        long jFloor2 = (long) Math.floor(terrainKindData.y());
        return jFloor + " " + jFloor + " " + jFloor2;
    }

    private void ma4zgy6tk1i3(String str) {
        Minecraft.getInstance().keyboardHandler.setClipboard(str);
        this.fhza61y9iwz5 = "Coordinates copied";
        this.f3obsvk7an4g = MaterialIsLightService.PRIMARY;
        mbnhko45bvwk();
    }

    private ComponentKeyService<?> m9b6ckf4291n(String str, String str2, String str3, Runnable runnable) {
        return MaterialTextService.iconButton(str, str2, str3, runnable).props(materialJoinedService -> {
            materialJoinedService.surfaceWidth(Float.intBitsToFloat(1109393408)).shape(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1109393408)).colors(MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE_VARIANT);
            materialJoinedService.size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
        });
    }

    private ComponentKeyService<?> mz8vzt5lbmt(String str, String str2, Runnable runnable) {
        return MaterialTextService.button("terrain-map.control." + str, str, runnable, MaterialIsLightService.SURFACE_CONTAINER, MaterialIsLightService.ON_SURFACE, Float.intBitsToFloat(1099956224)).props(materialJoinedService -> {
            materialJoinedService.shape(Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832));
            materialJoinedService.size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1108344832)).minWidth(0.0f).padding(0.0f).tooltip(str2);
        });
    }

    private void mje92gq708ta(double d) {
        this.f8b2aec2mdn4.zoom(d, this.f6bjd6ohjy2x / 2.0f, this.f7nixdcioujo / 2.0f);
    }

    private void mij4g3vq13lf() {
        Vector3d vector3dPick = this.f8b2aec2mdn4.pick(this.f6bjd6ohjy2x / 2.0f, this.f7nixdcioujo / 2.0f);
        mbo7sqtt0gkc(vector3dPick != null ? vector3dPick : this.f8b2aec2mdn4.camera(true).frame().focus());
    }

    private void mbo7sqtt0gkc(Vector3d vector3d) {
        this.f8lt1xfu9umw = new Vector3d(vector3d);
        this.fihdk7eps5ri = null;
        maoi2jqbxibe(this.f8lt1xfu9umw);
        this.ffdwz57zzott = TerrainKindData.Kind.PLACE;
        this.fid22j5sbpix = "Waypoint " + (this.f8b2aec2mdn4.waypoints().size() + 1);
        this.fcjuakj5stnl = true;
        this.f5ubx8tlea6w = true;
        this.ff4izes3vvbh = false;
        this.fhza61y9iwz5 = "";
        mbnhko45bvwk();
    }

    public void select(TerrainKindData terrainKindData) {
        this.fihdk7eps5ri = terrainKindData;
        this.f8b2aec2mdn4.selected(terrainKindData.id());
        this.ff4izes3vvbh = true;
        this.f5ubx8tlea6w = true;
        this.f1cio3takyuw = false;
        this.fcjuakj5stnl = false;
        this.fhza61y9iwz5 = "";
        mbnhko45bvwk();
    }

    public void showPortalPlanner() {
        this.f1cio3takyuw = true;
        mbnhko45bvwk();
    }

    private void m2f5t2pa9utb() {
        boolean zSave;
        if (this.f8lt1xfu9umw == null) {
            return;
        }
        try {
            this.f8lt1xfu9umw = new Vector3d(Double.parseDouble(this.fbyfwnffoqoo[0]), Double.parseDouble(this.fbyfwnffoqoo[1]), Double.parseDouble(this.fbyfwnffoqoo[2]));
            if (!this.f8lt1xfu9umw.isFinite() || Math.abs(this.f8lt1xfu9umw.x) > Double.longBitsToDouble(4718818273909538816L) || Math.abs(this.f8lt1xfu9umw.z) > Double.longBitsToDouble(4718818273909538816L) || Math.abs(this.f8lt1xfu9umw.y) > Double.longBitsToDouble(4661225614328463360L)) {
                throw new IllegalArgumentException();
            }
            if (this.fihdk7eps5ri != null) {
                this.fihdk7eps5ri = this.fihdk7eps5ri.renamed(this.fid22j5sbpix).withKind(this.ffdwz57zzott).movedTo(this.f8lt1xfu9umw.x, this.f8lt1xfu9umw.y, this.f8lt1xfu9umw.z);
                zSave = this.f8b2aec2mdn4.save(this.fihdk7eps5ri);
            } else {
                this.fihdk7eps5ri = this.f8b2aec2mdn4.add(this.fid22j5sbpix, this.f8lt1xfu9umw, this.ffdwz57zzott);
                zSave = this.fihdk7eps5ri != null;
            }
            if (zSave) {
                this.fcjuakj5stnl = false;
                this.ff4izes3vvbh = true;
                this.fhza61y9iwz5 = "";
                this.f8b2aec2mdn4.selected(this.fihdk7eps5ri.id());
            } else {
                this.fhza61y9iwz5 = "Could not save this waypoint.";
                this.f3obsvk7an4g = MaterialIsLightService.ERROR;
            }
            mbnhko45bvwk();
        } catch (IllegalArgumentException e) {
            this.fhza61y9iwz5 = "Enter valid X, Y and Z coordinates within the world border.";
            this.f3obsvk7an4g = MaterialIsLightService.ERROR;
            mbnhko45bvwk();
        }
    }

    private void m9gyxvp3bdlr() {
        String[] strArrSplit = this.ff65ootfuf00.strip().split("[\\s,]+");
        try {
            if (strArrSplit.length == 2 || strArrSplit.length == 3) {
                double d = Double.parseDouble(strArrSplit[0]);
                double d2 = Double.parseDouble(strArrSplit[strArrSplit.length - 1]);
                double d3 = strArrSplit.length == 3 ? Double.parseDouble(strArrSplit[1]) : this.f8b2aec2mdn4.camera(true).frame().focus().y;
                if (Double.isFinite(d) && Double.isFinite(d3) && Double.isFinite(d2) && Math.abs(d) <= Double.longBitsToDouble(4718818273909538816L) && Math.abs(d2) <= Double.longBitsToDouble(4718818273909538816L) && Math.abs(d3) <= Double.longBitsToDouble(4661225614328463360L)) {
                    this.f8b2aec2mdn4.camera(true).flyTo(d, d3, d2);
                    CoreIsInitializedHandler.get().scene().clearFocus();
                    return;
                }
                this.fhza61y9iwz5 = "Coordinates are outside the world border.";
                this.f3obsvk7an4g = MaterialIsLightService.ERROR;
                this.f5ubx8tlea6w = true;
                this.fcjuakj5stnl = false;
                this.ff4izes3vvbh = false;
                mbnhko45bvwk();
                return;
            }
        } catch (NumberFormatException e) {
        }
        this.f5ubx8tlea6w = true;
        this.ff4izes3vvbh = false;
        this.fcjuakj5stnl = false;
        mbnhko45bvwk();
    }

    private static String mbp4bzt5dv3z(double d, double d2, double d3) {
        long jFloor = (long) Math.floor(d);
        long jFloor2 = (long) Math.floor(d2);
        return "X " + jFloor + "  ·  Y " + jFloor + "  ·  Z " + jFloor2;
    }

    private void mbnhko45bvwk() {
        if (this.f5ua4o089agu != null) {
            this.f5ua4o089agu.invalidateComponent();
        }
    }
}
