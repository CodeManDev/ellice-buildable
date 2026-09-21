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
  private final TerrainMapController renderer;
  private final int count;
  private ComponentMountService componentMountService;
  private ScenePointerService scenePointerService;
  private ScreenScreenIdService screenScreenIdService;
  private boolean enabled;
  private boolean enabled2;
  private boolean fcjuakj5stnl;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;
  private Vector3d vector3d;
  private TerrainKindData terrainKindData;
  private long fbsx311vjijx;
  private float text2 = Float.intBitsToFloat(1148846080);
  private float f7nixdcioujo = Float.intBitsToFloat(1143930880);
  private TerrainKindData.Kind kind2 = TerrainKindData.Kind.PLACE;
  private final String[] fbyfwnffoqoo = {"0", "64", "0"};
  private int count2 = MaterialIsLightService.ERROR;
  private String text3 = "";
  private String text4 = "Waypoint";
  private String text5 = "";
  private long fcjixybt9mzk = -1;
  private long timestamp2 = -1;

  public TerrainMapScreen(TerrainMapController terrainMapController, int i) {
    this.renderer = terrainMapController;
    this.count = i;
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
    this.screenScreenIdService = screenScreenIdService;
    SceneCornerRadiusService sceneCornerRadiusServiceDirection =
        new MaterialTerrainTooltipsService()
            .backgroundColor(MaterialIsLightService.SURFACE_LOWEST)
            .direction(ScenePctService.Direction.NONE);
    this.componentMountService =
        new ComponentMountService()
            .mount(this::createComponentKeyService, screenScreenIdService.theme());
    this.componentMountService.size(
        LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
        LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
    sceneCornerRadiusServiceDirection.addChild(this.componentMountService);
    return sceneCornerRadiusServiceDirection;
  }

  @Override
  public void onOpen(ScreenScreenIdService screenScreenIdService) {
    this.renderer.expanded(true);
  }

  @Override
  public void onClose(ScreenScreenIdService screenScreenIdService) {
    this.renderer.expanded(false);
    this.renderer.camera(true).endDrag();
  }

  public void viewport(float f, float f2) {
    if (this.text2 == f && this.f7nixdcioujo == f2) {
      return;
    }
    this.text2 = f;
    this.f7nixdcioujo = f2;
    updateState9();
  }

  @Override
  public void tick(ScreenScreenIdService screenScreenIdService) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      screenScreenIdService.close();
      return;
    }
    viewport(
        CoreIsInitializedHandler.get().viewport().width(),
        CoreIsInitializedHandler.get().viewport().height());
    if (this.fcjixybt9mzk != this.renderer.revision()) {
      this.fcjixybt9mzk = this.renderer.revision();
      updateState9();
    }
    long jNanoTime = System.nanoTime();
    if (this.timestamp2 != this.renderer.navigation().revision()
        || (this.renderer.navigation().target() != null
            && jNanoTime - this.fbsx311vjijx > 500000000)) {
      this.timestamp2 = this.renderer.navigation().revision();
      this.fbsx311vjijx = jNanoTime;
      updateState9();
    }
    if (this.scenePointerService == null) {
      return;
    }
    long jWindowHandle = CompatAdapterService.windowHandle(minecraft);
    this.scenePointerService.pointer(
        new ScenePointerService.Pointer(
            CoreIsInitializedHandler.get().viewport().mouseX(minecraft.mouseHandler.xpos()),
            CoreIsInitializedHandler.get().viewport().mouseY(minecraft.mouseHandler.ypos()),
            GLFW.glfwGetMouseButton(jWindowHandle, 1) == 1
                || GLFW.glfwGetMouseButton(jWindowHandle, 2) == 1,
            miymh772hi1(jWindowHandle, 340) || miymh772hi1(jWindowHandle, 344),
            miymh772hi1(jWindowHandle, 341) || miymh772hi1(jWindowHandle, 345),
            (float) CoreIsInitializedHandler.get().viewport().renderScale()));
    if (CoreIsInitializedHandler.get().scene().hasFocusedInput()) {
      return;
    }
    int i =
        ((miymh772hi1(jWindowHandle, 263) || miymh772hi1(jWindowHandle, 65)) ? 1 : 0)
            - ((miymh772hi1(jWindowHandle, 262) || miymh772hi1(jWindowHandle, 68)) ? 1 : 0);
    int i2 =
        ((miymh772hi1(jWindowHandle, 265) || miymh772hi1(jWindowHandle, 87)) ? 1 : 0)
            - ((miymh772hi1(jWindowHandle, 264) || miymh772hi1(jWindowHandle, 83)) ? 1 : 0);
    if (i == 0 && i2 == 0) {
      return;
    }
    TerrainViewportService terrainViewportServiceCamera = this.renderer.camera(true);
    terrainViewportServiceCamera.beginDrag();
    terrainViewportServiceCamera.pan(
        this.text2 / 2.0f,
        this.f7nixdcioujo / 2.0f,
        (this.text2 / 2.0f)
            + (i * screenScreenIdService.deltaTime() * Float.intBitsToFloat(1137180672)),
        (this.f7nixdcioujo / 2.0f)
            + (i2 * screenScreenIdService.deltaTime() * Float.intBitsToFloat(1137180672)),
        0.0d);
    terrainViewportServiceCamera.endDrag();
  }

  private static boolean miymh772hi1(long j, int i) {
    return GLFW.glfwGetKey(j, i) == 1;
  }

  @Override
  public boolean keyPressed(ScreenScreenIdService screenScreenIdService, int i, int i2) {
    if (i == 256 && this.renderer.draggingWaypoint()) {
      this.renderer.cancelWaypointDrag();
      return true;
    }
    if (i == 256 && this.enabled) {
      this.fcjuakj5stnl = false;
      this.enabled = false;
      updateState9();
      return true;
    }
    if (CoreIsInitializedHandler.get().scene().hasFocusedInput()) {
      return false;
    }
    if (i == this.count) {
      screenScreenIdService.close();
      return true;
    }
    if (i == 70) {
      this.renderer.returnToPlayer();
      return true;
    }
    if (i == 78) {
      this.renderer.camera(true).northUp();
      return true;
    }
    if (i == 61 || i == 334) {
      updateState4(1.0d);
      return true;
    }
    if (i != 45 && i != 333) {
      return i >= 262 && i <= 265;
    }
    updateState4(Double.longBitsToDouble(-4616189618054758400L));
    return true;
  }

  private ComponentKeyService<?> createComponentKeyService(
      ComponentThemeService componentThemeService) {
    ArrayList arrayList = new ArrayList();
    arrayList.add(
        ComponentBoxService.node(
                "terrain-map-viewport",
                () -> {
                  return new ScenePointerService(this.renderer, true);
                },
                scenePointerService -> {
                  this.scenePointerService = scenePointerService;
                  ((ScenePointerService) scenePointerService.id("terrain-map.canvas"))
                      .size(
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                          LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
                  scenePointerService.onPlace(this::updateState6).onWaypoint(this::select);
                  ArrayList arrayList2 = new ArrayList();
                  arrayList2.add(
                      new TerrainReservedService.Box(
                          0.0f, 0.0f, this.text2, Float.intBitsToFloat(1115684864)));
                  boolean z =
                      this.text2 < Float.intBitsToFloat(1141637120)
                          || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
                  if (this.renderer.navigation().target() != null) {
                    arrayList2.add(
                        new TerrainReservedService.Box(
                            Float.intBitsToFloat(1094713344),
                            (this.f7nixdcioujo - (z ? 74 : 16)) - Float.intBitsToFloat(1125253120),
                            Math.min(
                                    Float.intBitsToFloat(1135214592),
                                    this.text2 - (z ? 32 : 82))
                                + Float.intBitsToFloat(1090519040),
                            Float.intBitsToFloat(1125515264)));
                  }
                  if (this.enabled) {
                    arrayList2.add(
                        new TerrainReservedService.Box(
                            Float.intBitsToFloat(1090519040),
                            (this.text2 < Float.intBitsToFloat(1142292480)
                                    || this.f7nixdcioujo < Float.intBitsToFloat(1135542272))
                                ? Float.intBitsToFloat(1082130432)
                                : Float.intBitsToFloat(1116471296),
                            Math.min(
                                    Float.intBitsToFloat(1133248512),
                                    this.text2 - Float.intBitsToFloat(1103101952))
                                + Float.intBitsToFloat(1090519040),
                            this.f7nixdcioujo - Float.intBitsToFloat(1117782016)));
                  }
                  scenePointerService.reserved(arrayList2);
                },
                new ComponentKeyService[0])
            .key("viewport"));
    ComponentStyleService componentStyleServiceBuild =
        ComponentStyleService.style()
            .absolute()
            .inset(
                LayoutOperationHandler.px(Float.intBitsToFloat(1092616192)),
                LayoutOperationHandler.px(Float.intBitsToFloat(1092616192)),
                LayoutOperationHandler.auto(),
                LayoutOperationHandler.px(Float.intBitsToFloat(1092616192)))
            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
            .gap(Float.intBitsToFloat(1086324736))
            .align(ScenePctService.Align.CENTER)
            .build();
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService.direction(ScenePctService.Direction.ROW).layerBreak(true);
        };
    ComponentStyleService componentStyleServiceBuild2 =
        ComponentStyleService.style()
            .grow(1.0f)
            .maxWidth(LayoutOperationHandler.px(Float.intBitsToFloat(1136525312)))
            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)))
            .padding(0.0f, Float.intBitsToFloat(1096810496))
            .gap(Float.intBitsToFloat(1092616192))
            .align(ScenePctService.Align.CENTER)
            .build();
    Consumer<SceneCornerRadiusService> consumer2 =
        sceneCornerRadiusService2 -> {
          sceneCornerRadiusService2
              .direction(ScenePctService.Direction.ROW)
              .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
              .cornerRadius(Float.intBitsToFloat(1103101952))
              .shadow(Float.intBitsToFloat(1084227584));
        };
    ComponentKeyService[] componentKeyServiceArr = {
      MaterialTextService.icon("search", MaterialIsLightService.ON_SURFACE_VARIANT)
          .props(
              sceneSrcService -> {
                sceneSrcService.size(
                    Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800));
              }),
      ComponentBoxService.node(
              "terrain-search",
              ControlLetterSpacingService::new,
              controlLetterSpacingService -> {
                controlLetterSpacingService
                    .id("terrain-map.search")
                    .fontFamily(MaterialIsLightService.FONT)
                    .fontSize(Float.intBitsToFloat(1095761920))
                    .placeholder(
                        this.text2 < Float.intBitsToFloat(1140981760)
                            ? "Search…"
                            : "Waypoint or X, Z coordinates")
                    .bgColor(0)
                    .textColor(MaterialIsLightService.ON_SURFACE)
                    .placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT)
                    .focusBorder(0)
                    .selectionColor(1716537957)
                    .height(LayoutOperationHandler.px(Float.intBitsToFloat(1109393408)))
                    .flex(1.0f)
                    .minWidth(0.0f)
                    .maxLength(80);
                controlLetterSpacingService
                    .onChanged(
                        str -> {
                          this.text3 = str;
                        })
                    .onSubmit(
                        str2 -> {
                          updateState8();
                        });
              },
              new ComponentKeyService[0])
          .onMount(
              controlLetterSpacingService2 -> {
                controlLetterSpacingService2.text(this.text3);
              })
          .key("search-input")
    };
    ScreenScreenIdService screenScreenIdService = this.screenScreenIdService;
    Objects.requireNonNull(screenScreenIdService);
    arrayList.add(
        ComponentBoxService.panel(
                componentStyleServiceBuild,
                consumer,
                createComponentKeyService6(
                    "terrain-map.waypoints",
                    "star",
                    "Waypoints",
                    () -> {
                      this.enabled = !this.enabled;
                      this.enabled5 = false;
                      this.fcjuakj5stnl = false;
                      updateState9();
                    }),
                ComponentBoxService.panel(
                    componentStyleServiceBuild2, consumer2, componentKeyServiceArr),
                createComponentKeyService6(
                    "terrain-map.add",
                    "add",
                    "Add waypoint here · Shift-click terrain",
                    this::updateState5),
                createComponentKeyService6(
                    "terrain-map.close", "close", "Close map", screenScreenIdService::close))
            .key("toolbar"));
    boolean z =
        this.text2 < Float.intBitsToFloat(1141637120)
            || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
    ComponentStyleService componentStyleServiceBuild3 =
        ComponentStyleService.style()
            .absolute()
            .inset(
                LayoutOperationHandler.auto(),
                LayoutOperationHandler.px(Float.intBitsToFloat(1094713344)),
                LayoutOperationHandler.px(Float.intBitsToFloat(1098907648)),
                LayoutOperationHandler.auto())
            .width(
                LayoutOperationHandler.px(
                    z ? Float.intBitsToFloat(1129709568) : Float.intBitsToFloat(1110966272)))
            .padding(Float.intBitsToFloat(1077936128))
            .gap(2.0f)
            .build();
    Consumer<SceneCornerRadiusService> consumer3 =
        sceneCornerRadiusService3 -> {
          sceneCornerRadiusService3
              .direction(z ? ScenePctService.Direction.ROW : ScenePctService.Direction.COLUMN)
              .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
              .cornerRadius(Float.intBitsToFloat(1101004800))
              .shadow(Float.intBitsToFloat(1086324736))
              .layerBreak(true);
        };
    ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[5];
    componentKeyServiceArr2[0] =
        mz8vzt5lbmt(
            "+",
            "Zoom in",
            () -> {
              updateState4(1.0d);
            });
    componentKeyServiceArr2[1] =
        mz8vzt5lbmt(
            "−",
            "Zoom out",
            () -> {
              updateState4(Double.longBitsToDouble(-4616189618054758400L));
            });
    componentKeyServiceArr2[2] =
        mz8vzt5lbmt(
            "N",
            "North up",
            () -> {
              this.renderer.camera(true).northUp();
            });
    componentKeyServiceArr2[3] =
        mz8vzt5lbmt(
            this.enabled2 ? "3D" : "2D",
            "Toggle overhead view",
            () -> {
              this.enabled2 = !this.enabled2;
              this.renderer.camera(true).topDown(this.enabled2);
              updateState9();
            });
    TerrainMapController terrainMapController = this.renderer;
    Objects.requireNonNull(terrainMapController);
    componentKeyServiceArr2[4] =
        createComponentKeyService6(
            "terrain-map.player",
            "person",
            "Return to player · F",
            terrainMapController::returnToPlayer);
    arrayList.add(
        ComponentBoxService.panel(componentStyleServiceBuild3, consumer3, componentKeyServiceArr2)
            .key("navigation"));
    if (this.renderer.navigation().target() != null) {
      arrayList.add(
          ComponentBoxService.column(
                  ComponentStyleService.style()
                      .absolute()
                      .inset(
                          LayoutOperationHandler.auto(),
                          LayoutOperationHandler.auto(),
                          LayoutOperationHandler.px(
                              z
                                  ? Float.intBitsToFloat(1116995584)
                                  : Float.intBitsToFloat(1098907648)),
                          LayoutOperationHandler.px(Float.intBitsToFloat(1098907648)))
                      .width(
                          LayoutOperationHandler.px(
                              Math.min(
                                  Float.intBitsToFloat(1135214592),
                                  this.text2 - (z ? 32 : 82))))
                      .build(),
                  (ComponentKeyService<?>[])
                      new ComponentKeyService[] {
                        new WaypointPanel(this.renderer).render(componentThemeService)
                      })
              .key("navigation-card-host"));
    } else if (this.text2 > Float.intBitsToFloat(1142620160)
        && this.f7nixdcioujo > Float.intBitsToFloat(1136525312)) {
      arrayList.add(
          ComponentBoxService.panel(
                  ComponentStyleService.style()
                      .absolute()
                      .inset(
                          LayoutOperationHandler.auto(),
                          LayoutOperationHandler.auto(),
                          LayoutOperationHandler.px(Float.intBitsToFloat(1099956224)),
                          LayoutOperationHandler.px(Float.intBitsToFloat(1099956224)))
                      .padding(Float.intBitsToFloat(1090519040), Float.intBitsToFloat(1094713344))
                      .build(),
                  sceneCornerRadiusService4 -> {
                    sceneCornerRadiusService4
                        .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                        .cornerRadius(Float.intBitsToFloat(1094713344))
                        .layerBreak(true);
                  },
                  MaterialTextService.text(
                      "Drag to pan · Scroll to zoom · Right-drag to orbit",
                      Float.intBitsToFloat(1093664768),
                      MaterialIsLightService.ON_SURFACE_VARIANT))
              .key("hint"));
    }
    if (this.enabled) {
      arrayList.add(createComponentKeyService2());
    }
    return ComponentBoxService.stack(
        ComponentStyleService.style()
            .size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .build(),
        (ComponentKeyService<?>[])
            arrayList.toArray(
                i -> {
                  return new ComponentKeyService[i];
                }));
  }

  private ComponentKeyService<?> createComponentKeyService2() {
    String str;
    String str2;
    boolean z =
        this.text2 < Float.intBitsToFloat(1142292480)
            || this.f7nixdcioujo < Float.intBitsToFloat(1135542272);
    ArrayList arrayList = new ArrayList();
    ComponentStyleService componentStyleServiceBuild =
        ComponentStyleService.style()
            .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
            .align(ScenePctService.Align.CENTER)
            .gap(Float.intBitsToFloat(1090519040))
            .build();
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[3];
    componentKeyServiceArr[0] =
        MaterialTextService.iconButton(
                "map.sheet.back",
                "arrow_back",
                "Back",
                () -> {
                  if (this.enabled6) {
                    this.enabled6 = false;
                  } else if (this.fcjuakj5stnl) {
                    this.fcjuakj5stnl = false;
                    this.enabled5 = this.terrainKindData != null;
                  } else {
                    this.enabled5 = false;
                  }
                  this.text5 = "";
                  updateState9();
                })
            .props(
                materialJoinedService -> {
                  materialJoinedService.visible(this.fcjuakj5stnl || this.enabled5);
                });
    if (this.fcjuakj5stnl) {
      str = this.terrainKindData == null ? "New waypoint" : "Edit waypoint";
    } else if (this.enabled6) {
      str = "Portal planner";
    } else {
      str = this.enabled5 ? "Place details" : "Waypoints";
    }
    componentKeyServiceArr[1] =
        MaterialTextService.label(str, MaterialIsLightService.ON_SURFACE)
            .props(
                sceneTextService -> {
                  sceneTextService.flex(1.0f).minWidth(0.0f);
                });
    componentKeyServiceArr[2] =
        MaterialTextService.iconButton(
            "terrain-map.sheet.close",
            "close",
            "Close waypoints",
            () -> {
              this.fcjuakj5stnl = false;
              this.enabled = false;
              updateState9();
            });
    arrayList.add(
        ComponentBoxService.row(
            componentStyleServiceBuild, (ComponentKeyService<?>[]) componentKeyServiceArr));
    ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
    if (this.fcjuakj5stnl) {
      arrayList2.add(
          ComponentBoxService.node(
                  "waypoint-name",
                  ControlLetterSpacingService::new,
                  controlLetterSpacingService -> {
                    MaterialTextService.input(controlLetterSpacingService);
                    controlLetterSpacingService
                        .id("terrain-map.waypoint.name")
                        .fontSize(Float.intBitsToFloat(1096810496))
                        .placeholder("Name")
                        .maxLength(64)
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .height(LayoutOperationHandler.px(Float.intBitsToFloat(1111490560)));
                    controlLetterSpacingService
                        .onChanged(
                            str3 -> {
                              this.text4 = str3;
                            })
                        .onSubmit(
                            str4 -> {
                              updateState7();
                            });
                  },
                  new ComponentKeyService[0])
              .onMount(
                  controlLetterSpacingService2 -> {
                    controlLetterSpacingService2.text(this.text4);
                  })
              .key("name"));
      arrayList2.add(
          MaterialTextService.text(
              "Marker type",
              Float.intBitsToFloat(1093664768),
              MaterialIsLightService.ON_SURFACE_VARIANT));
      arrayList2.add(createComponentKeyService3(true));
      arrayList2.add(createComponentKeyService4());
      arrayList2.add(
          MaterialTextService.button(
              "terrain-map.save", "Save waypoint", this::updateState7, true));
      arrayList2.add(
          MaterialTextService.button(
              "terrain-map.cancel",
              "Cancel",
              () -> {
                this.fcjuakj5stnl = false;
                updateState9();
              },
              false));
    } else if (!this.enabled5 || this.terrainKindData == null) {
      Consumer<LayoutContainerNode> consumer =
          layoutContainerNode -> {
            layoutContainerNode
                .gap(Float.intBitsToFloat(1086324736))
                .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)));
          };
      ComponentKeyService[] componentKeyServiceArr2 = new ComponentKeyService[2];
      componentKeyServiceArr2[0] =
          createComponentKeyService5(
              "map.filter.all",
              this.renderer.filter() == null ? "All types ✓" : "All types",
              () -> {
                this.renderer.filter(null);
                updateState9();
              });
      componentKeyServiceArr2[1] =
          createComponentKeyService5(
              "map.dimensions",
              this.enabled4 ? "All dimensions" : "This dimension",
              () -> {
                this.enabled4 = !this.enabled4;
                updateState9();
              });
      arrayList2.add(
          ComponentBoxService.row(
              (Consumer<LayoutContainerNode>) consumer,
              (ComponentKeyService<?>[]) componentKeyServiceArr2));
      arrayList2.add(createComponentKeyService3(false));
      TerrainKindData terrainKindDataLastDeath = this.renderer.lastDeath();
      if (terrainKindDataLastDeath != null) {
        arrayList2.add(
            MaterialTextService.button(
                "map.last-death",
                "Last death · "
                    + TerrainCanonicalService.label(terrainKindDataLastDeath.dimension()),
                () -> {
                  this.enabled4 = !this.renderer.inCurrentDimension(terrainKindDataLastDeath);
                  this.renderer.filter(null);
                  select(terrainKindDataLastDeath);
                  this.renderer.goTo(terrainKindDataLastDeath);
                },
                MaterialIsLightService.SECONDARY_CONTAINER,
                MaterialIsLightService.ON_SECONDARY_CONTAINER,
                Float.intBitsToFloat(1096810496)));
      }
      List<TerrainKindData> list =
          (this.enabled4 ? this.renderer.savedWaypoints() : this.renderer.waypoints())
              .stream()
                  .filter(
                      terrainKindData -> {
                        return this.renderer.filter() == null
                            || terrainKindData.kind() == this.renderer.filter();
                      })
                  .filter(
                      terrainKindData2 -> {
                        return this.text3.isBlank()
                            || terrainKindData2
                                .name()
                                .toLowerCase(Locale.ROOT)
                                .contains(this.text3.toLowerCase(Locale.ROOT));
                      })
                  .sorted(
                      Comparator.comparingInt(
                              (TerrainKindData terrainKindData3) -> {
                                return (this.terrainKindData == null
                                        || !terrainKindData3.id().equals(this.terrainKindData.id()))
                                    ? 1
                                    : 0;
                              })
                          .thenComparing(
                              Comparator.comparingLong(
                                      (TerrainKindData v0) -> {
                                        return v0.createdAt();
                                      })
                                  .reversed()))
                  .toList();
      if (list.isEmpty()) {
        arrayList2.add(
            MaterialTextService.text(
                    this.text3.isBlank()
                        ? "Your places, saved here. Shift-click the terrain to add one."
                        : "No matching waypoints.",
                    Float.intBitsToFloat(1096810496),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService2 -> {
                      sceneTextService2.wordWrap(true);
                    }));
      }
      for (TerrainKindData terrainKindData4 : list) {
        arrayList2.add(
            ComponentBoxService.panel(
                    ComponentStyleService.style()
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .padding(Float.intBitsToFloat(1092616192))
                        .gap(Float.intBitsToFloat(1077936128))
                        .build(),
                    sceneCornerRadiusService -> {
                      sceneCornerRadiusService
                          .direction(ScenePctService.Direction.COLUMN)
                          .backgroundColor(
                              (this.terrainKindData == null
                                      || !terrainKindData4.id().equals(this.terrainKindData.id()))
                                  ? MaterialIsLightService.SURFACE_HIGH
                                  : MaterialIsLightService.SECONDARY_CONTAINER)
                          .cornerRadius(Float.intBitsToFloat(1094713344))
                          .onClick(
                              () -> {
                                select(terrainKindData4);
                                this.renderer.goTo(terrainKindData4);
                              });
                    },
                    ComponentBoxService.row(
                        (Consumer<LayoutContainerNode>)
                            layoutContainerNode2 -> {
                              layoutContainerNode2
                                  .width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456)))
                                  .gap(Float.intBitsToFloat(1090519040))
                                  .align(ScenePctService.Align.CENTER);
                            },
                        (ComponentKeyService<?>[])
                            new ComponentKeyService[] {
                              ComponentBoxService.node(
                                  "place-kind",
                                  TerrainRenderer.GlyphNode::new,
                                  glyphNode -> {
                                    glyphNode
                                        .glyph(
                                            terrainKindData4.kind().icon(),
                                            terrainKindData4.color())
                                        .size(
                                            Float.intBitsToFloat(1103101952),
                                            Float.intBitsToFloat(1103101952))
                                        .flexShrink(0.0f);
                                  },
                                  new ComponentKeyService[0]),
                              MaterialTextService.label(
                                      terrainKindData4.name(), MaterialIsLightService.ON_SURFACE)
                                  .props(
                                      sceneTextService3 -> {
                                        sceneTextService3.flex(1.0f).minWidth(0.0f);
                                      })
                            }),
                    MaterialTextService.text(
                        terrainKindData4.kind().label()
                            + " · "
                            + TerrainCanonicalService.label(terrainKindData4.dimension()),
                        Float.intBitsToFloat(1092616192),
                        terrainKindData4.color()),
                    MaterialTextService.text(
                        createText2(
                            terrainKindData4.x(), terrainKindData4.y(), terrainKindData4.z()),
                        Float.intBitsToFloat(1092616192),
                        MaterialIsLightService.ON_SURFACE_VARIANT))
                .key(terrainKindData4.id().toString()));
      }
      arrayList2.add(
          MaterialTextService.button(
              "terrain-map.new", "Add waypoint here", this::updateState5, true));
    } else {
      arrayList2.add(
          MaterialTextService.text(
                  this.terrainKindData.name(),
                  Float.intBitsToFloat(1099956224),
                  MaterialIsLightService.ON_SURFACE)
              .props(
                  sceneTextService4 -> {
                    sceneTextService4.fontFamily(MaterialIsLightService.MEDIUM).wordWrap(true);
                  }));
      arrayList2.add(
          MaterialTextService.text(
              this.terrainKindData.kind().label()
                  + " · "
                  + TerrainCanonicalService.label(this.terrainKindData.dimension()),
              Float.intBitsToFloat(1093664768),
              this.terrainKindData.color()));
      arrayList2.add(
          MaterialTextService.text(
              createText2(
                  this.terrainKindData.x(), this.terrainKindData.y(), this.terrainKindData.z()),
              Float.intBitsToFloat(1093664768),
              MaterialIsLightService.ON_SURFACE_VARIANT));
      if (this.terrainKindData.kind() == TerrainKindData.Kind.DEATH
          && this.terrainKindData.createdAt() > 0) {
        arrayList2.add(
            MaterialTextService.text(
                DateTimeFormatter.ofPattern("dd MMM · HH:mm")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.ofEpochMilli(this.terrainKindData.createdAt())),
                Float.intBitsToFloat(1093664768),
                MaterialIsLightService.ON_SURFACE_VARIANT));
      }
      arrayList2.addAll(mhrvg6rszqbv());
    }
    if (!this.text5.isEmpty()) {
      arrayList2.add(
          MaterialTextService.text(this.text5, Float.intBitsToFloat(1094713344), this.count2)
              .props(
                  sceneTextService5 -> {
                    sceneTextService5.wordWrap(true);
                  }));
    }
    arrayList2.replaceAll(
        componentKeyService -> {
          return componentKeyService.props(
              scenePctService -> {
                scenePctService.flexShrink(0.0f);
              });
        });
    ComponentKeyService<LayoutContainerNode> componentKeyServiceColumn =
        ComponentBoxService.column(
            ComponentStyleService.style()
                .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                .grow(1.0f)
                .minHeight(LayoutOperationHandler.px(0.0f))
                .gap(Float.intBitsToFloat(1092616192))
                .scroll(true)
                .build(),
            (ComponentKeyService<?>[])
                arrayList2.toArray(
                    i -> {
                      return new ComponentKeyService[i];
                    }));
    if (this.fcjuakj5stnl) {
      str2 = "editor";
    } else if (this.enabled6) {
      str2 = "portal";
    } else {
      str2 = this.enabled5 ? "details" : "places";
    }
    arrayList.add(componentKeyServiceColumn.key(str2));
    return ComponentBoxService.panel(
            ComponentStyleService.style()
                .absolute()
                .inset(
                    LayoutOperationHandler.px(
                        z ? Float.intBitsToFloat(1090519040) : Float.intBitsToFloat(1116995584)),
                    LayoutOperationHandler.auto(),
                    LayoutOperationHandler.px(Float.intBitsToFloat(1096810496)),
                    LayoutOperationHandler.px(Float.intBitsToFloat(1094713344)))
                .width(
                    LayoutOperationHandler.px(
                        Math.min(
                            Float.intBitsToFloat(1133248512),
                            this.text2 - Float.intBitsToFloat(1103101952))))
                .padding(Float.intBitsToFloat(1096810496))
                .gap(Float.intBitsToFloat(1090519040))
                .build(),
            sceneCornerRadiusService2 -> {
              sceneCornerRadiusService2
                  .direction(ScenePctService.Direction.COLUMN)
                  .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                  .cornerRadius(Float.intBitsToFloat(1102053376))
                  .shadow(Float.intBitsToFloat(1092616192))
                  .clip(true)
                  .layerBreak(true);
            },
            (ComponentKeyService[])
                arrayList.toArray(
                    i2 -> {
                      return new ComponentKeyService[i2];
                    }))
        .key("waypoint-sheet")
        .onMount(
            sceneCornerRadiusService3 -> {
              sceneCornerRadiusService3.translateX(Float.intBitsToFloat(-1056964608));
              MotionAnimateService.animate(
                  sceneCornerRadiusService3,
                  MotionColorsContainer.Floats.TRANSLATE_X,
                  0.0f,
                  SceneEaseHandler.Tween.ease(Float.intBitsToFloat(1042536202)));
            });
  }

  private List<ComponentKeyService<?>> mhrvg6rszqbv() {
    ArrayList arrayList = new ArrayList();
    TerrainCanonicalService.PortalPlan portalPlanPortal =
        TerrainCanonicalService.portal(this.terrainKindData);
    if (portalPlanPortal == null || !this.enabled6) {
      if (this.renderer.inCurrentDimension(this.terrainKindData)) {
        arrayList.add(
            MaterialTextService.button(
                    "terrain-map.navigate",
                    "Directions",
                    () -> {
                      this.renderer.navigate(this.terrainKindData);
                      this.enabled = false;
                      updateState9();
                    },
                    true)
                .children(
                    MaterialTextService.icon("navigation", MaterialIsLightService.ON_PRIMARY),
                    MaterialTextService.label("Directions", MaterialIsLightService.ON_PRIMARY)));
      } else {
        arrayList.add(
            MaterialTextService.text(
                    "Enter "
                        + TerrainCanonicalService.label(this.terrainKindData.dimension())
                        + " to navigate to this place.",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.ON_SURFACE_VARIANT)
                .props(
                    sceneTextService -> {
                      sceneTextService.wordWrap(true);
                    }));
      }
      arrayList.add(
          MaterialTextService.button(
              "map.copy",
              "Copy coordinates",
              () -> {
                updateState3(createText(this.terrainKindData));
              },
              false));
      arrayList.add(
          ComponentBoxService.row(
              (Consumer<LayoutContainerNode>)
                  layoutContainerNode -> {
                    layoutContainerNode
                        .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                        .gap(Float.intBitsToFloat(1086324736));
                  },
              (ComponentKeyService<?>[])
                  new ComponentKeyService[] {
                    createComponentKeyService5(
                        "terrain-map.edit", "Edit marker", this::updateState),
                    createComponentKeyService5(
                        "terrain-map.remove",
                        "Remove",
                        () -> {
                          this.renderer.remove(this.terrainKindData.id());
                          this.terrainKindData = null;
                          this.enabled5 = false;
                          updateState9();
                        })
                  }));
      if (portalPlanPortal != null) {
        arrayList.add(
            MaterialTextService.button(
                "map.portal.toggle",
                TerrainCanonicalService.label(portalPlanPortal.dimension()) + " portal planner",
                () -> {
                  this.enabled6 = !this.enabled6;
                  updateState9();
                },
                MaterialIsLightService.SECONDARY_CONTAINER,
                MaterialIsLightService.ON_SECONDARY_CONTAINER,
                Float.intBitsToFloat(1096810496)));
      }
      return arrayList;
    }
    Consumer<SceneCornerRadiusService> consumer =
        sceneCornerRadiusService -> {
          sceneCornerRadiusService
              .direction(ScenePctService.Direction.COLUMN)
              .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
              .padding(Float.intBitsToFloat(1094713344))
              .gap(Float.intBitsToFloat(1086324736))
              .cornerRadius(Float.intBitsToFloat(1098907648))
              .backgroundColor(MaterialIsLightService.SURFACE_HIGH);
        };
    ComponentKeyService[] componentKeyServiceArr = new ComponentKeyService[5];
    componentKeyServiceArr[0] =
        MaterialTextService.text(
                "Portal planner · " + TerrainCanonicalService.label(portalPlanPortal.dimension()),
                Float.intBitsToFloat(1094713344),
                MaterialIsLightService.PRIMARY)
            .props(
                sceneTextService2 -> {
                  sceneTextService2.fontFamily(MaterialIsLightService.MEDIUM);
                });
    componentKeyServiceArr[1] =
        MaterialTextService.text(
            portalPlanPortal.coordinates(),
            Float.intBitsToFloat(1095761920),
            MaterialIsLightService.ON_SURFACE);
    componentKeyServiceArr[2] =
        MaterialTextService.text(
                portalPlanPortal.insideBorder()
                    ? "X/Z projection · choose the height on site"
                    : "Outside the world border",
                Float.intBitsToFloat(1092616192),
                portalPlanPortal.insideBorder()
                    ? MaterialIsLightService.ON_SURFACE_VARIANT
                    : MaterialIsLightService.ERROR)
            .props(
                sceneTextService3 -> {
                  sceneTextService3.wordWrap(true);
                });
    componentKeyServiceArr[3] =
        createComponentKeyService5(
                "map.portal.copy",
                "Copy projected X/Z",
                () -> {
                  long jFloor = (long) Math.floor(portalPlanPortal.x());
                  updateState3(jFloor + " " + this);
                })
            .props(
                materialJoinedService -> {
                  materialJoinedService.colors(
                      MaterialIsLightService.SURFACE_HIGHEST, MaterialIsLightService.ON_SURFACE);
                });
    componentKeyServiceArr[4] =
        createComponentKeyService5(
                "map.portal.save",
                "Save planned portal",
                () -> {
                  TerrainKindData terrainKindDataPlanPortal =
                      this.renderer.planPortal(this.terrainKindData);
                  if (terrainKindDataPlanPortal != null) {
                    this.enabled4 = true;
                    this.renderer.filter(null);
                    select(terrainKindDataPlanPortal);
                  } else {
                    this.text5 =
                        portalPlanPortal.insideBorder()
                            ? "Could not save the portal plan."
                            : "The projected position is outside the world border.";
                    this.count2 = MaterialIsLightService.ERROR;
                    updateState9();
                  }
                })
            .props(
                materialJoinedService2 -> {
                  materialJoinedService2.colors(
                      MaterialIsLightService.PRIMARY, MaterialIsLightService.ON_PRIMARY);
                })
            .children(
                MaterialTextService.text(
                    "Save planned portal",
                    Float.intBitsToFloat(1093664768),
                    MaterialIsLightService.ON_PRIMARY));
    arrayList.add(ComponentBoxService.panel(consumer, componentKeyServiceArr));
    return arrayList;
  }

  private ComponentKeyService<?> createComponentKeyService3(boolean z) {
    TerrainKindData.Kind[] kindArrValues = TerrainKindData.Kind.values();
    ArrayList arrayList = new ArrayList();
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= kindArrValues.length) {
        return ComponentBoxService.column(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .gap(Float.intBitsToFloat(1084227584));
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i3 -> {
                      return new ComponentKeyService[i3];
                    }));
      }
      ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
      int i4 = i2;
      while (true) {
        int i5 = i4;
        if (i5 < Math.min(i2 + 3, kindArrValues.length)) {
          TerrainKindData.Kind kind = kindArrValues[i5];
          boolean z2 = kind == (z ? this.kind2 : this.renderer.filter());
          int iColor = z2 ? MaterialIsLightService.ON_PRIMARY : kind.color();
          ComponentKeyService<?> componentKeyServiceProps =
              MaterialTextService.button(
                      "map.kind." + (z ? "edit." : "filter.") + String.valueOf(kind),
                      kind.label(),
                      () -> {
                        if (z) {
                          this.kind2 = kind;
                        } else {
                          this.renderer.filter(z2 ? null : kind);
                        }
                        updateState9();
                      },
                      z2 ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SURFACE_HIGH,
                      iColor,
                      Float.intBitsToFloat(1094713344))
                  .props(
                      materialJoinedService -> {
                        materialJoinedService.shape(
                            Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1108344832));
                        materialJoinedService
                            .height(LayoutOperationHandler.px(Float.intBitsToFloat(1108344832)))
                            .flex(1.0f)
                            .minWidth(0.0f)
                            .padding(Float.intBitsToFloat(1084227584))
                            .gap(Float.intBitsToFloat(1077936128));
                      });
          ComponentKeyService<?>[] componentKeyServiceArr = new ComponentKeyService[2];
          componentKeyServiceArr[0] =
              ComponentBoxService.node(
                  "kind-icon",
                  TerrainRenderer.GlyphNode::new,
                  glyphNode -> {
                    glyphNode
                        .glyph(kind.icon(), iColor)
                        .size(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1101004800))
                        .flexShrink(0.0f);
                  },
                  new ComponentKeyService[0]);
          componentKeyServiceArr[1] =
              MaterialTextService.text(
                  kind.label(),
                  Float.intBitsToFloat(1092616192),
                  z2 ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SURFACE);
          arrayList2.add(
              componentKeyServiceProps.children(componentKeyServiceArr).key(kind.name()));
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
                        .flexShrink(0.0f)
                        .gap(Float.intBitsToFloat(1084227584));
                  },
              (ComponentKeyService<?>[])
                  arrayList2.toArray(
                      i6 -> {
                        return new ComponentKeyService[i6];
                      })));
      i = i2 + 3;
    }
  }

  private ComponentKeyService<?> createComponentKeyService4() {
    ArrayList arrayList = new ArrayList();
    String[] strArr = {"X", "Y", "Z"};
    int i = 0;
    while (true) {
      int i2 = i;
      if (i2 >= 3) {
        return ComponentBoxService.row(
            (Consumer<LayoutContainerNode>)
                layoutContainerNode -> {
                  layoutContainerNode
                      .width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))
                      .gap(Float.intBitsToFloat(1088421888));
                },
            (ComponentKeyService<?>[])
                arrayList.toArray(
                    i3 -> {
                      return new ComponentKeyService[i3];
                    }));
      }
      arrayList.add(
          ComponentBoxService.column(
              (Consumer<LayoutContainerNode>)
                  layoutContainerNode2 -> {
                    layoutContainerNode2
                        .flex(1.0f)
                        .minWidth(0.0f)
                        .gap(Float.intBitsToFloat(1082130432));
                  },
              (ComponentKeyService<?>[])
                  new ComponentKeyService[] {
                    MaterialTextService.text(
                        strArr[i2],
                        Float.intBitsToFloat(1092616192),
                        MaterialIsLightService.ON_SURFACE_VARIANT),
                    ComponentBoxService.node(
                            "coordinate",
                            ControlLetterSpacingService::new,
                            controlLetterSpacingService -> {
                              MaterialTextService.input(controlLetterSpacingService);
                              controlLetterSpacingService
                                  .id("map.coordinate." + strArr[i2])
                                  .fontSize(Float.intBitsToFloat(1093664768))
                                  .width(
                                      LayoutOperationHandler.percent(
                                          Float.intBitsToFloat(1120403456)))
                                  .height(
                                      LayoutOperationHandler.px(Float.intBitsToFloat(1108869120)))
                                  .maxLength(16);
                              controlLetterSpacingService
                                  .onChanged(
                                      str -> {
                                        this.fbyfwnffoqoo[i2] = str;
                                      })
                                  .onSubmit(
                                      str2 -> {
                                        updateState7();
                                      });
                            },
                            new ComponentKeyService[0])
                        .key("coordinate." + strArr[i2])
                        .onMount(
                            controlLetterSpacingService2 -> {
                              controlLetterSpacingService2.text(this.fbyfwnffoqoo[i2]);
                            })
                  }));
      i = i2 + 1;
    }
  }

  private void updateState() {
    this.vector3d =
        new Vector3d(this.terrainKindData.x(), this.terrainKindData.y(), this.terrainKindData.z());
    maoi2jqbxibe(this.vector3d);
    this.text4 = this.terrainKindData.name();
    this.kind2 = this.terrainKindData.kind();
    this.fcjuakj5stnl = true;
    this.text5 = "";
    updateState9();
  }

  public void edit(TerrainKindData terrainKindData) {
    select(terrainKindData);
    updateState();
  }

  private void maoi2jqbxibe(Vector3d vector3d) {
    this.fbyfwnffoqoo[0] = String.valueOf(vector3d.x);
    this.fbyfwnffoqoo[1] = String.valueOf(vector3d.y);
    this.fbyfwnffoqoo[2] = String.valueOf(vector3d.z);
  }

  private ComponentKeyService<MaterialJoinedService> createComponentKeyService5(
      String str, String str2, Runnable runnable) {
    return MaterialTextService.button(
            str,
            str2,
            runnable,
            MaterialIsLightService.SURFACE_HIGH,
            MaterialIsLightService.ON_SURFACE,
            Float.intBitsToFloat(1094713344))
        .props(
            materialJoinedService -> {
              materialJoinedService.shape(
                  Float.intBitsToFloat(1094713344), Float.intBitsToFloat(1107820544));
              materialJoinedService
                  .height(LayoutOperationHandler.px(Float.intBitsToFloat(1107820544)))
                  .flexGrow(1.0f)
                  .flexShrink(0.0f)
                  .minWidth(0.0f)
                  .padding(Float.intBitsToFloat(1086324736));
            })
        .children(
            MaterialTextService.text(
                str2, Float.intBitsToFloat(1093664768), MaterialIsLightService.ON_SURFACE));
  }

  private static String createText(TerrainKindData terrainKindData) {
    long jFloor = (long) Math.floor(terrainKindData.x());
    long jFloor2 = (long) Math.floor(terrainKindData.y());
    return jFloor + " " + jFloor + " " + jFloor2;
  }

  private void updateState3(String str) {
    Minecraft.getInstance().keyboardHandler.setClipboard(str);
    this.text5 = "Coordinates copied";
    this.count2 = MaterialIsLightService.PRIMARY;
    updateState9();
  }

  private ComponentKeyService<?> createComponentKeyService6(
      String str, String str2, String str3, Runnable runnable) {
    return MaterialTextService.iconButton(str, str2, str3, runnable)
        .props(
            materialJoinedService -> {
              materialJoinedService
                  .surfaceWidth(Float.intBitsToFloat(1109393408))
                  .shape(Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1109393408))
                  .colors(
                      MaterialIsLightService.SURFACE_CONTAINER,
                      MaterialIsLightService.ON_SURFACE_VARIANT);
              materialJoinedService.size(
                  Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1109393408));
            });
  }

  private ComponentKeyService<?> mz8vzt5lbmt(String str, String str2, Runnable runnable) {
    return MaterialTextService.button(
            "terrain-map.control." + str,
            str,
            runnable,
            MaterialIsLightService.SURFACE_CONTAINER,
            MaterialIsLightService.ON_SURFACE,
            Float.intBitsToFloat(1099956224))
        .props(
            materialJoinedService -> {
              materialJoinedService.shape(
                  Float.intBitsToFloat(1099956224), Float.intBitsToFloat(1108344832));
              materialJoinedService
                  .size(Float.intBitsToFloat(1109393408), Float.intBitsToFloat(1108344832))
                  .minWidth(0.0f)
                  .padding(0.0f)
                  .tooltip(str2);
            });
  }

  private void updateState4(double d) {
    this.renderer.zoom(d, this.text2 / 2.0f, this.f7nixdcioujo / 2.0f);
  }

  private void updateState5() {
    Vector3d vector3dPick = this.renderer.pick(this.text2 / 2.0f, this.f7nixdcioujo / 2.0f);
    updateState6(vector3dPick != null ? vector3dPick : this.renderer.camera(true).frame().focus());
  }

  private void updateState6(Vector3d vector3d) {
    this.vector3d = new Vector3d(vector3d);
    this.terrainKindData = null;
    maoi2jqbxibe(this.vector3d);
    this.kind2 = TerrainKindData.Kind.PLACE;
    this.text4 = "Waypoint " + (this.renderer.waypoints().size() + 1);
    this.fcjuakj5stnl = true;
    this.enabled = true;
    this.enabled5 = false;
    this.text5 = "";
    updateState9();
  }

  public void select(TerrainKindData terrainKindData) {
    this.terrainKindData = terrainKindData;
    this.renderer.selected(terrainKindData.id());
    this.enabled5 = true;
    this.enabled = true;
    this.enabled6 = false;
    this.fcjuakj5stnl = false;
    this.text5 = "";
    updateState9();
  }

  public void showPortalPlanner() {
    this.enabled6 = true;
    updateState9();
  }

  private void updateState7() {
    boolean zSave;
    if (this.vector3d == null) {
      return;
    }
    try {
      this.vector3d =
          new Vector3d(
              Double.parseDouble(this.fbyfwnffoqoo[0]),
              Double.parseDouble(this.fbyfwnffoqoo[1]),
              Double.parseDouble(this.fbyfwnffoqoo[2]));
      if (!this.vector3d.isFinite()
          || Math.abs(this.vector3d.x) > Double.longBitsToDouble(4718818273909538816L)
          || Math.abs(this.vector3d.z) > Double.longBitsToDouble(4718818273909538816L)
          || Math.abs(this.vector3d.y) > Double.longBitsToDouble(4661225614328463360L)) {
        throw new IllegalArgumentException();
      }
      if (this.terrainKindData != null) {
        this.terrainKindData =
            this.terrainKindData
                .renamed(this.text4)
                .withKind(this.kind2)
                .movedTo(this.vector3d.x, this.vector3d.y, this.vector3d.z);
        zSave = this.renderer.save(this.terrainKindData);
      } else {
        this.terrainKindData = this.renderer.add(this.text4, this.vector3d, this.kind2);
        zSave = this.terrainKindData != null;
      }
      if (zSave) {
        this.fcjuakj5stnl = false;
        this.enabled5 = true;
        this.text5 = "";
        this.renderer.selected(this.terrainKindData.id());
      } else {
        this.text5 = "Could not save this waypoint.";
        this.count2 = MaterialIsLightService.ERROR;
      }
      updateState9();
    } catch (IllegalArgumentException e) {
      this.text5 = "Enter valid X, Y and Z coordinates within the world border.";
      this.count2 = MaterialIsLightService.ERROR;
      updateState9();
    }
  }

  private void updateState8() {
    String[] strArrSplit = this.text3.strip().split("[\\s,]+");
    try {
      if (strArrSplit.length == 2 || strArrSplit.length == 3) {
        double d = Double.parseDouble(strArrSplit[0]);
        double d2 = Double.parseDouble(strArrSplit[strArrSplit.length - 1]);
        double d3 =
            strArrSplit.length == 3
                ? Double.parseDouble(strArrSplit[1])
                : this.renderer.camera(true).frame().focus().y;
        if (Double.isFinite(d)
            && Double.isFinite(d3)
            && Double.isFinite(d2)
            && Math.abs(d) <= Double.longBitsToDouble(4718818273909538816L)
            && Math.abs(d2) <= Double.longBitsToDouble(4718818273909538816L)
            && Math.abs(d3) <= Double.longBitsToDouble(4661225614328463360L)) {
          this.renderer.camera(true).flyTo(d, d3, d2);
          CoreIsInitializedHandler.get().scene().clearFocus();
          return;
        }
        this.text5 = "Coordinates are outside the world border.";
        this.count2 = MaterialIsLightService.ERROR;
        this.enabled = true;
        this.fcjuakj5stnl = false;
        this.enabled5 = false;
        updateState9();
        return;
      }
    } catch (NumberFormatException e) {
    }
    this.enabled = true;
    this.enabled5 = false;
    this.fcjuakj5stnl = false;
    updateState9();
  }

  private static String createText2(double d, double d2, double d3) {
    long jFloor = (long) Math.floor(d);
    long jFloor2 = (long) Math.floor(d2);
    return "X " + jFloor + "  ·  Y " + jFloor + "  ·  Z " + jFloor2;
  }

  private void updateState9() {
    if (this.componentMountService != null) {
      this.componentMountService.invalidateComponent();
    }
  }
}
