package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatAdapterService;
import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.terrain.TerrainMapController;
import dev.felix.ellice.feature.terrain.TerrainReservedService;
import dev.felix.ellice.feature.terrain.WaypointOverlayController;
import dev.felix.ellice.feature.terrain.WaypointPanel;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleOperationHandler;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.component.ComponentStyleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.ScenePointerService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.screen.builtin.TerrainMapScreen;
import java.util.ArrayList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class ImplControllerService extends ModuleSettingsService
    implements ModuleOperationHandler {
  private final ModuleSetting.Bool moduleBool =
      this.setting(new ModuleSetting.Bool("Minimap", true));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(new ModuleSetting.Bool("World Waypoints", true));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(new ModuleSetting.Bool("World Navigation Line", true));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Death Waypoints", true)
                  .description("Save the position and dimension once when you die."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Show Players", true)
                  .description("Render nearby players as 3D figures on the map and minimap."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          (ModuleSetting.Bool)
              new ModuleSetting.Bool("Player Names", true)
                  .description("Label nearby players on the expanded map."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number(
              "Minimap Size",
              Float.intBitsToFloat(1126694912),
              Float.intBitsToFloat(1121976320),
              Float.intBitsToFloat(1133248512),
              Float.intBitsToFloat(0x41000000)));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          (ModuleSetting.Number)
              new ModuleSetting.Number(
                      "Map Radius",
                      Float.intBitsToFloat(0x41400000),
                      Float.intBitsToFloat(0x40800000),
                      Float.intBitsToFloat(1103101952),
                      1.0f)
                  .description(
                      "Maximum radius in already-loaded chunks. Higher values use more memory."));
  private final ModuleSetting.Keybind moduleKeybind =
      this.setting(new ModuleSetting.Keybind("Open Map", 77));
  private TerrainMapController fa2qwwgthtnq;
  private SceneCornerRadiusService sceneCornerRadiusService;
  private ScenePointerService scenePointerService;
  private boolean enabled;
  private ComponentMountService componentMountService;
  private long timestamp;
  private long timestamp2 = -1L;
  private WaypointOverlayController terrainStateController2;

  public ImplControllerService() {
    super(
        ModuleBuilderData.builder("Terrain Map")
            .category(ModuleFeatureType.HUD)
            .description("Minecraft terrain in 3D. Pan, zoom, orbit and save your waypoints.")
            .build());
  }

  public TerrainMapController controller() {
    return this.fa2qwwgthtnq;
  }

  @Override
  protected void onEnable() {
    CompatLoadedHandler compatLoadedHandler =
        CompatAdapterService.terrainEnvironment()
            .orElseThrow(() -> new IllegalStateException("Missing terrain adapter"));
    this.fa2qwwgthtnq =
        new TerrainMapController(
            compatLoadedHandler,
            FabricLoader.getInstance().getGameDir(),
            CoreIsInitializedHandler.get().renderer3D());
    this.terrainStateController2 =
        new WaypointOverlayController(
            this.fa2qwwgthtnq, CoreIsInitializedHandler.get().renderer3D());
    CoreIsInitializedHandler.get().scene().root().addChild(this.terrainStateController2);
    this.sceneCornerRadiusService =
        (SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    ((SceneCornerRadiusService)
                            ((SceneCornerRadiusService)
                                    new MaterialTerrainTooltipsService()
                                        .direction(ScenePctService.Direction.NONE))
                                .pointerEvents(false))
                        .absolute())
                .visible(false);
    ComponentMountService componentMountService =
        new ComponentMountService()
            .mount(
                componentThemeService ->
                    ComponentBoxService.panel(
                            ComponentStyleService.style()
                                .size(
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456)),
                                    LayoutOperationHandler.percent(
                                        Float.intBitsToFloat(1120403456)))
                                .padding(Float.intBitsToFloat(0x40E00000))
                                .gap(Float.intBitsToFloat(0x40A00000))
                                .build(),
                            sceneCornerRadiusService ->
                                ((SceneCornerRadiusService)
                                        sceneCornerRadiusService.direction(
                                            ScenePctService.Direction.COLUMN))
                                    .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
                                    .cornerRadius(Float.intBitsToFloat(1097859072))
                                    .shadow(Float.intBitsToFloat(0x40E00000))
                                    .pointerEvents(false),
                            ComponentBoxService.node(
                                    "terrain-minimap",
                                    () -> new ScenePointerService(this.fa2qwwgthtnq, false),
                                    scenePointerService -> {
                                      this.scenePointerService = scenePointerService;
                                      ((ScenePointerService)
                                              ((ScenePointerService)
                                                      ((ScenePointerService)
                                                              scenePointerService
                                                                  .radius(
                                                                      Float.intBitsToFloat(
                                                                          0x41100000))
                                                                  .width(
                                                                      LayoutOperationHandler
                                                                          .percent(
                                                                              Float.intBitsToFloat(
                                                                                  1120403456))))
                                                          .height(LayoutOperationHandler.auto()))
                                                  .flex(1.0f))
                                          .minHeight(0.0f);
                                    },
                                    new ComponentKeyService[0])
                                .key("terrain"),
                            ComponentBoxService.row(
                                    ComponentStyleService.style()
                                        .width(
                                            LayoutOperationHandler.percent(
                                                Float.intBitsToFloat(1120403456)))
                                        .height(
                                            LayoutOperationHandler.px(
                                                Float.intBitsToFloat(1098907648)))
                                        .align(ScenePctService.Align.CENTER)
                                        .build(),
                                    ComponentBoxService.text(
                                        "Terrain",
                                        sceneTextService ->
                                            sceneTextService
                                                .fontFamily("material-roboto-medium")
                                                .fontSize(Float.intBitsToFloat(1092616192))
                                                .color(MaterialIsLightService.ON_SURFACE)
                                                .flex(1.0f)),
                                    ComponentBoxService.text(
                                        this.moduleKeybind.keyName(),
                                        sceneTextService ->
                                            sceneTextService
                                                .fontFamily("material-roboto")
                                                .fontSize(Float.intBitsToFloat(0x41100000))
                                                .color(MaterialIsLightService.ON_SURFACE_VARIANT)))
                                .key("caption"))
                        .key("minimap"),
                CoreIsInitializedHandler.get().theme());
    ((LayoutContainerNode)
            componentMountService.size(
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)),
                LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456))))
        .pointerEvents(false);
    this.sceneCornerRadiusService.addChild(componentMountService);
    CoreIsInitializedHandler.get().scene().root().addChild(this.sceneCornerRadiusService);
    this.componentMountService =
        new ComponentMountService()
            .mount(new WaypointPanel(this.fa2qwwgthtnq), CoreIsInitializedHandler.get().theme());
    ((LayoutContainerNode) this.componentMountService.absolute()).visible(false);
    CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
    this.on(EventAttackInputService.RENDER)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(render -> this.updateState());
    this.on(EventAttackInputService.RENDER)
        .priority(EventIsAfterHandler.Priority.LATE)
        .run(render -> this.fa2qwwgthtnq.render());
    this.on(EventAttackInputService.WORLD)
        .run(
            world -> {
              this.fa2qwwgthtnq.disconnect();
              this.terrainStateController2.hide();
            });
    this.on(EventAttackInputService.WORLD_RENDER)
        .priority(EventIsAfterHandler.Priority.DEFAULT)
        .run(
            worldRender -> {
              Minecraft minecraft = Minecraft.getInstance();
              this.terrainStateController2.update(
                  (EventAttackInputService.WorldRender) worldRender,
                  CoreIsInitializedHandler.get().viewport().width(),
                  CoreIsInitializedHandler.get().viewport().height(),
                  ((Boolean) this.moduleBool2.get() != false
                              && minecraft.level != null
                              && minecraft.screen == null
                              && !minecraft.options.hideGui
                              && !CoreIsInitializedHandler.get().screens().isActive()
                          ? 1
                          : 0)
                      != 0,
                  (Boolean) this.moduleBool3.get());
            });
    this.on(EventAttackInputService.TICK)
        .run(
            tick -> {
              int n;
              Minecraft minecraft = Minecraft.getInstance();
              int n2 =
                  n =
                      this.moduleKeybind.isBound()
                              && GLFW.glfwGetKey(
                                      (long) CompatAdapterService.windowHandle(minecraft),
                                      (int) ((Integer) this.moduleKeybind.get()))
                                  == 1
                          ? 1
                          : 0;
              if (n != 0 && !this.enabled && minecraft.level != null && minecraft.screen == null) {
                this.openPanel();
              }
              this.enabled = (n != 0);
            });
  }

  private void updateState() {
    Minecraft minecraft = Minecraft.getInstance();
    boolean bl =
        (Boolean) this.moduleBool.get() != false
            && minecraft.level != null
            && minecraft.screen == null
            && !minecraft.options.hideGui
            && !CoreIsInitializedHandler.get().screens().isActive();
    float f =
        Math.min(
            ((Float) this.moduleNumber.get()).floatValue(),
            (float) (CoreIsInitializedHandler.get().viewport().width() - 24));
    float f2 =
        Math.min(
            ((Float) this.moduleNumber.get()).floatValue() + Float.intBitsToFloat(1102577664),
            (float) (CoreIsInitializedHandler.get().viewport().height() - 24));
    ((SceneCornerRadiusService)
            ((SceneCornerRadiusService)
                    this.sceneCornerRadiusService.position(
                        (float) CoreIsInitializedHandler.get().viewport().width()
                            - f
                            - Float.intBitsToFloat(0x41400000),
                        Float.intBitsToFloat(0x41400000)))
                .size(f, f2))
        .visible(bl);
    if (this.scenePointerService != null) {
      this.scenePointerService.pointer(
          new ScenePointerService.Pointer(
              0.0f,
              0.0f,
              false,
              false,
              false,
              (float) CoreIsInitializedHandler.get().viewport().renderScale()));
    }
    this.fa2qwwgthtnq.miniature(bl);
    this.fa2qwwgthtnq.deathWaypoints((Boolean) this.moduleBool4.get());
    this.fa2qwwgthtnq.showPlayers((Boolean) this.moduleBool5.get());
    this.fa2qwwgthtnq.playerNames((Boolean) this.moduleBool6.get());
    this.fa2qwwgthtnq.radiusLimit(((Float) this.moduleNumber2.get()).intValue());
    this.fa2qwwgthtnq.beginFrame();
    boolean bl2 =
        minecraft.level != null
            && minecraft.screen == null
            && !minecraft.options.hideGui
            && !CoreIsInitializedHandler.get().screens().isActive()
            && this.fa2qwwgthtnq.navigation().target() != null;
    float f3 = Math.min(300, CoreIsInitializedHandler.get().viewport().width() - 24);
    ((LayoutContainerNode)
            ((LayoutContainerNode)
                    this.componentMountService.position(
                        Float.intBitsToFloat(0x41400000),
                        CoreIsInitializedHandler.get().viewport().height() - 156))
                .width(LayoutOperationHandler.px(f3)))
        .visible(bl2);
    ArrayList<TerrainReservedService.Box> arrayList = new ArrayList<TerrainReservedService.Box>();
    if (bl) {
      arrayList.add(
          new TerrainReservedService.Box(
              (float) CoreIsInitializedHandler.get().viewport().width()
                  - f
                  - Float.intBitsToFloat(1098907648),
              Float.intBitsToFloat(0x41000000),
              f + Float.intBitsToFloat(0x41000000),
              f2 + Float.intBitsToFloat(0x41000000)));
    }
    if (bl2) {
      arrayList.add(
          new TerrainReservedService.Box(
              Float.intBitsToFloat(0x41000000),
              CoreIsInitializedHandler.get().viewport().height() - 160,
              f3 + Float.intBitsToFloat(0x41000000),
              Float.intBitsToFloat(1125908480)));
    }
    this.terrainStateController2.reserved(arrayList);
    long l = System.nanoTime();
    if (this.timestamp2 != this.fa2qwwgthtnq.navigation().revision()
        || bl2 && l - this.timestamp > 500000000L) {
      this.timestamp2 = this.fa2qwwgthtnq.navigation().revision();
      this.timestamp = l;
      this.componentMountService.invalidateComponent();
    }
  }

  @Override
  public void openPanel() {
    if (!this.isEnabled()) {
      this.enable();
    }
    if (Minecraft.getInstance().level != null) {
      CoreIsInitializedHandler.get()
          .screens()
          .open(new TerrainMapScreen(this.fa2qwwgthtnq, (Integer) this.moduleKeybind.get()));
    }
  }

  @Override
  protected void onDisable() {
    if ("terrain-map".equals(CoreIsInitializedHandler.get().screens().currentId())) {
      CoreIsInitializedHandler.get().screens().closeNow();
    }
    if (this.sceneCornerRadiusService != null) {
      CoreIsInitializedHandler.get().scene().root().removeChild(this.sceneCornerRadiusService);
    }
    if (this.componentMountService != null) {
      CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
      this.componentMountService = null;
    }
    if (this.terrainStateController2 != null) {
      CoreIsInitializedHandler.get().scene().root().removeChild(this.terrainStateController2);
      this.terrainStateController2.close();
      this.terrainStateController2 = null;
    }
    if (this.fa2qwwgthtnq != null) {
      this.fa2qwwgthtnq.close();
    }
    this.fa2qwwgthtnq = null;
    this.sceneCornerRadiusService = null;
    this.scenePointerService = null;
    this.enabled = false;
  }
}
