package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.joml.Vector3d;

public final class TerrainMapController implements AutoCloseable {
  private final CompatLoadedHandler compatLoadedHandler;
  private final TerrainStateController terrainStateController;
  private final TerrainMapRenderer renderer2;
  private final Path path;
  private final TerrainMapController.View renderer3;
  private final TerrainMapController.View renderer4;
  private final AutoCloseable autoCloseable;
  private CompatLoadedHandler.World world2;
  private Object object;
  private TerrainPointsService terrainPointsService;
  private List<TerrainData> items = List.of();
  private long timestamp;
  private long timestamp2;
  private String text = "";
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;
  private int count = 12;
  private long timestamp3;
  private Object object2;
  private Vector3d vector3d;
  private long timestamp4;
  private UUID uUID;
  private TerrainKindData terrainKindData;
  private final TerrainGroundTracker terrainGroundTracker;
  private final TerrainObserveService terrainObserveService = new TerrainObserveService();
  private boolean enabled4 = true;
  private boolean enabled5 = true;
  private boolean enabled6 = true;
  private List<CompatLoadedHandler.MapPlayer> entries = List.of();
  private Map<UUID, Double> entries2 = Map.of();
  private CompatLoadedHandler.SkyState skyState2;
  private TerrainKindData.Kind kind2;

  public TerrainMapController(
      final CompatLoadedHandler compatLoaded,
      Path currentPath,
      Render3dSceneService render3dScene) {
    this.renderer2 = new TerrainMapRenderer(render3dScene);
    this.renderer3 = new TerrainMapController.View(this.renderer2, render3dScene);
    this.renderer4 = new TerrainMapController.View(this.renderer2, render3dScene);
    this.compatLoadedHandler = Objects.requireNonNull(compatLoaded);
    this.terrainGroundTracker =
        new TerrainGroundTracker(
            new TerrainGroundTracker.Ground() {
              @Override
              public Double floor(int value, int currentValue, double doubleValue) {
                return compatLoaded.walkingFloor(value, currentValue, doubleValue);
              }

              @Override
              public boolean loaded(int value, int currentValue) {
                return compatLoaded.routeLoaded(value, currentValue);
              }
            });
    this.path = Objects.requireNonNull(currentPath);
    this.terrainStateController = new TerrainStateController(compatLoaded);
    this.renderer4.camera.distance(140.0);
    this.renderer4.camera.planView();
    this.autoCloseable = TerrainSubscribeService.subscribe(this.terrainStateController::invalidate);
  }

  public void expanded(boolean currentEnabled) {
    this.enabled = currentEnabled;
    this.timestamp = 0L;
  }

  public boolean expanded() {
    return this.enabled;
  }

  public void miniature(boolean enabled) {
    this.enabled2 = enabled;
  }

  public void radiusLimit(int value) {
    this.count = Math.clamp(value, 2, 24);
  }

  public void disconnect() {
    this.enabled3 = true;
    this.vector3d = null;
  }

  public TerrainViewportService camera(boolean enabled) {
    return (enabled ? this.renderer3 : this.renderer4).camera;
  }

  public RhiBlendStateService.TextureHandle texture(boolean enabled) {
    return (enabled ? this.renderer3 : this.renderer4).target.texture();
  }

  public CompatLoadedHandler.World world() {
    return this.world2;
  }

  public long revision() {
    return this.timestamp3;
  }

  public String error() {
    return this.text;
  }

  public int loadedSections() {
    return this.terrainStateController.size();
  }

  public TerrainStateController cache() {
    return this.terrainStateController;
  }

  public TerrainMapRenderer renderer() {
    return this.renderer2;
  }

  public void selected(UUID currentUUID) {
    this.uUID = currentUUID;
  }

  public UUID selected() {
    return this.uUID;
  }

  public void deathWaypoints(boolean enabled) {
    this.enabled4 = enabled;
  }

  public void showPlayers(boolean enabled) {
    this.enabled5 = enabled;
  }

  public void playerNames(boolean enabled) {
    this.enabled6 = enabled;
  }

  public boolean showPlayerNames() {
    return this.enabled6;
  }

  public List<CompatLoadedHandler.MapPlayer> players() {
    return this.entries;
  }

  public TerrainKindData.Kind filter() {
    return this.kind2;
  }

  public void filter(TerrainKindData.Kind kind) {
    this.kind2 = kind;
    this.timestamp3++;
  }

  public boolean inCurrentDimension(TerrainKindData terrainKindData) {
    return this.world2 != null && terrainKindData.dimension().equals(this.world2.dimension());
  }

  public TerrainGroundTracker navigation() {
    return this.terrainGroundTracker;
  }

  public void navigate(TerrainKindData terrainKindData) {
    if (this.inCurrentDimension(terrainKindData)) {
      this.uUID = terrainKindData.id();
      this.terrainGroundTracker.start(terrainKindData, this.world2.player());
      this.timestamp3++;
      Vector3d currentX =
          new Vector3d(this.world2.player())
              .add(terrainKindData.x(), terrainKindData.y(), terrainKindData.z())
              .mul(0.5);
      this.renderer3.camera.flyTo(currentX.x, currentX.y, currentX.z);
      this.renderer3.camera.zoomTo(
          Math.max(
              60.0,
              this.world2
                      .player()
                      .distance(terrainKindData.x(), terrainKindData.y(), terrainKindData.z())
                  * 1.7));
    }
  }

  public TerrainMapRenderer.Target target(boolean enabled) {
    return (enabled ? this.renderer3 : this.renderer4).target;
  }

  public Vector3d markerAnchor(TerrainKindData terrainKindData, boolean enabled) {
    return new Vector3d(terrainKindData.x(), terrainKindData.y(), terrainKindData.z());
  }

  public void beginWaypointDrag(TerrainKindData currentTerrainKindData) {
    this.terrainKindData = currentTerrainKindData;
    this.selected(currentTerrainKindData.id());
  }

  public void moveWaypointPreview(Vector3d vector3d) {
    if (this.terrainKindData != null && vector3d != null) {
      this.terrainKindData = this.terrainKindData.movedTo(vector3d.x, vector3d.y, vector3d.z);
    }
  }

  public boolean draggingWaypoint() {
    return this.terrainKindData != null;
  }

  public void cancelWaypointDrag() {
    this.terrainKindData = null;
  }

  public TerrainKindData finishWaypointDrag(boolean enabled) {
    TerrainKindData currentTerrainKindData = this.terrainKindData;
    this.terrainKindData = null;
    return currentTerrainKindData != null && enabled && !this.save(currentTerrainKindData)
        ? null
        : currentTerrainKindData;
  }

  public void viewport(boolean enabled, float y, float width, float height) {
    TerrainMapController.View view = enabled ? this.renderer3 : this.renderer4;
    view.camera.viewport(y, width);
    int[] ints = TerrainFogRangeService.targetSize(y, width, height, enabled);
    this.renderer2.prepareTarget(view.target, ints[0], ints[1]);
  }

  public void beginFrame() {
    long longValue = System.nanoTime();
    double doubleValue =
        this.timestamp2 == 0L
            ? 0.016666666666666666
            : Math.min(0.1, (longValue - this.timestamp2) / 1.0E9);
    this.timestamp2 = longValue;
    CompatLoadedHandler.World currentWorld = this.compatLoadedHandler.world();
    Object value = this.compatLoadedHandler.instanceIdentity();
    Object currentValue = this.compatLoadedHandler.resourceIdentity();
    int nextValue =
        currentWorld != null
                && this.world2 != null
                && currentWorld.identity().equals(this.world2.identity())
                && currentWorld.dimension().equals(this.world2.dimension())
            ? 1
            : 0;
    int previousValue =
        nextValue != 0
                && this.vector3d != null
                && currentWorld.player() != null
                && this.vector3d.distanceSquared(currentWorld.player()) > 65536.0
            ? 1
            : 0;
    int sourceValue =
        !this.enabled3
                && (currentWorld == null ? 1 : 0) == (this.world2 == null ? 1 : 0)
                && (currentWorld == null
                    || this.world2 == null
                    || currentWorld.identity().equals(this.world2.identity())
                        && currentWorld.dimension().equals(this.world2.dimension()))
                && Objects.equals(this.object2, value)
                && previousValue == 0
            ? 0
            : 1;
    int targetValue = !Objects.equals(this.object, currentValue) ? 1 : 0;
    if (sourceValue != 0 || targetValue != 0) {
      this.terrainStateController.reset();
      this.renderer2.clear();
      this.items = List.of();
      this.timestamp = 0L;
      this.renderer3.markers.clear();
      this.renderer4.markers.clear();
      this.uUID = null;
      this.renderer3.players.clear();
      this.renderer4.players.clear();
      this.entries = List.of();
      this.entries2 = Map.of();
      this.skyState2 = null;
      this.renderer3.shadows.clear();
      this.renderer4.shadows.clear();
      this.terrainKindData = null;
      this.terrainGroundTracker.stop();
      this.renderer3.route.clear();
      this.renderer4.route.clear();
      this.timestamp4 = System.currentTimeMillis();
      if (sourceValue != 0 && currentWorld != null) {
        this.renderer3.camera.center(
            currentWorld.player().x, currentWorld.player().y, currentWorld.player().z);

        try {
          this.terrainPointsService = new TerrainPointsService(this.path, currentWorld.identity());
          this.text = "";
        } catch (IOException iOException) {
          this.terrainPointsService = null;
          this.text = iOException.getMessage();
        }
      }

      this.object = currentValue;
      this.timestamp3++;
    }

    this.enabled3 = false;
    this.world2 = currentWorld;
    this.object2 = value;
    this.vector3d =
        this.world2 != null && this.world2.player() != null
            ? new Vector3d(this.world2.player())
            : null;
    TerrainKindData currentTerrainKindData =
        this.terrainObserveService.observe(
            this.world2,
            this.object2,
            this.compatLoadedHandler.playerDead(),
            this.enabled4,
            System.currentTimeMillis());
    if (currentTerrainKindData != null && this.save(currentTerrainKindData)) {
      this.uUID = currentTerrainKindData.id();
      this.terrainGroundTracker.stop();
    }

    if (this.world2 != null) {
      this.terrainGroundTracker.tick(this.world2.player());
    }

    if (this.world2 != null && (this.enabled || this.enabled2)) {
      this.entries = this.enabled5 ? this.compatLoadedHandler.mapPlayers() : List.of();
      int inputValue =
          this.world2 != null && "minecraft:overworld".equals(this.world2.dimension()) ? 1 : 0;
      this.skyState2 = inputValue != 0 ? this.compatLoadedHandler.skyState() : null;
      if (this.entries.isEmpty()) {
        this.entries2 = Map.of();
      } else {
        HashMap hashMap = new HashMap();

        for (CompatLoadedHandler.MapPlayer mapPlayer : this.entries) {
          Double currentX =
              this.compatLoadedHandler.shadowGround(mapPlayer.x(), mapPlayer.z(), mapPlayer.y());
          if (currentX != null) {
            hashMap.put(mapPlayer.id(), currentX);
          }
        }

        this.entries2 = Map.copyOf(hashMap);
      }

      this.renderer4.camera.center(
          this.world2.player().x, this.world2.player().y, this.world2.player().z);
      this.renderer3.camera.advance(doubleValue);
      this.renderer4.camera.advance(doubleValue);
      TerrainMapController.View view = this.enabled ? this.renderer3 : this.renderer4;
      Vector3d currentVector3d = view.camera.frame().focus();
      if (longValue - this.timestamp > 250000000L) {
        int currentCount =
            Math.clamp((int) Math.ceil(view.camera.frame().distance() / 24.0), 2, this.count);
        this.items =
            this.compatLoadedHandler.sections(currentVector3d.x, currentVector3d.z, currentCount);
        this.timestamp = longValue;
      }

      this.terrainStateController.update(this.items, currentVector3d.x, currentVector3d.z);
    }
  }

  public void render() {
    if (this.world2 != null && (this.enabled || this.enabled2)) {
      this.renderer2.update(this.terrainStateController, this.items);
      CompatLoadedHandler.Textures currentTextures = this.compatLoadedHandler.textures();
      TerrainMapController.View currentView = this.enabled ? this.renderer3 : this.renderer4;
      long longValue = System.nanoTime();
      updateState(currentView, this.skyState2);
      currentView.players.update(this.entries, currentView.camera.frame(), longValue);
      currentView.shadows.update(
          this.entries,
          currentView.target.view().scene().sun().direction(),
          currentView.camera.frame(),
          currentView.players,
          this.entries2);
      long currentLongValue =
          renderInterval(this.enabled, currentView.camera.moving(), !this.entries.isEmpty());
      if (currentView.target.needsRender()
          || longValue - currentView.lastRender >= currentLongValue) {
        currentView.markers.update(this.visibleWaypoints(), this.uUID, currentView.camera.frame());
        currentView.route.update(this.terrainGroundTracker, currentView.camera.frame());
        this.renderer2.render(
            currentView.target,
            currentView.camera.frame(),
            currentTextures,
            this.skyState2,
            currentView.players.casters(),
            this.enabled
                && "minecraft:overworld".equals(this.world2.dimension())
                && !currentView.camera.topDown());
        currentView.lastRender = longValue;
      }
    }
  }

  static long renderInterval(boolean enabled, boolean currentEnabled, boolean nextEnabled) {
    if (!enabled || !currentEnabled && !nextEnabled) {
      if (nextEnabled) {
        return 16000000L;
      } else {
        return enabled ? 33000000L : 50000000L;
      }
    } else {
      return 0L;
    }
  }

  private static void updateState(
      TerrainMapController.View currentView, CompatLoadedHandler.SkyState skyState) {
    if (skyState != null) {
      currentView
          .target
          .view()
          .scene()
          .sun(TerrainSunDirectionService.mapSun(skyState.sunDirection(), skyState.daylight()));
      currentView
          .target
          .view()
          .scene()
          .ambient(
              TerrainSunDirectionService.mapAmbientColor(skyState.daylight()),
              TerrainSunDirectionService.mapAmbientIntensity(skyState.daylight()));
    }
  }

  public Vector3d pick(float value, float currentValue) {
    TerrainViewportService.Frame currentFrame = this.renderer3.camera.frame();
    return TerrainPickService.pick(
        this.terrainStateController.meshes(),
        currentFrame.ray(value, currentValue),
        currentFrame.distance() * 6.0 + 2048.0);
  }

  public void zoom(double doubleValue, float value, float currentValue) {
    this.renderer3.camera.zoom(doubleValue, value, currentValue, this.pick(value, currentValue));
  }

  public void returnToPlayer() {
    if (this.world2 != null) {
      this.renderer3.camera.flyTo(
          this.world2.player().x, this.world2.player().y, this.world2.player().z);
    }
  }

  public List<TerrainKindData> waypoints() {
    if (this.world2 != null && this.terrainPointsService != null) {
      List<TerrainKindData> items =
          this.terrainPointsService.points(this.world2.dimension()).stream()
              .filter(
                  item ->
                      item.kind() != TerrainKindData.Kind.DEATH
                          || this.timestamp4 <= 0L
                          || item.createdAt() >= this.timestamp4)
              .toList();
      return this.terrainKindData == null
          ? items
          : items.stream()
              .map(
                  item -> item.id().equals(this.terrainKindData.id()) ? this.terrainKindData : item)
              .toList();
    } else {
      return List.of();
    }
  }

  public List<TerrainKindData> savedWaypoints() {
    return this.terrainPointsService == null ? List.of() : this.terrainPointsService.all();
  }

  public List<TerrainKindData> visibleWaypoints() {
    TerrainKindData terrainKindData = this.terrainGroundTracker.target();
    return this.waypoints().stream()
        .filter(
            item ->
                this.kind2 == null
                    || item.kind() == this.kind2
                    || item.id().equals(this.uUID)
                    || terrainKindData != null && item.id().equals(terrainKindData.id()))
        .toList();
  }

  public TerrainKindData lastDeath() {
    return this.waypoints().stream()
        .filter(item -> item.kind() == TerrainKindData.Kind.DEATH)
        .max(Comparator.comparingLong(TerrainKindData::createdAt))
        .orElse(null);
  }

  public TerrainKindData add(String text, Vector3d vector3d, int value) {
    if (this.world2 != null && this.terrainPointsService != null && vector3d != null) {
      TerrainKindData terrainKindData =
          new TerrainKindData(
              UUID.randomUUID(),
              text,
              this.world2.dimension(),
              vector3d.x,
              vector3d.y,
              vector3d.z,
              value);
      return this.save(terrainKindData) ? terrainKindData : null;
    } else {
      return null;
    }
  }

  public TerrainKindData add(String text, Vector3d vector3d, TerrainKindData.Kind kind) {
    if (this.world2 != null && vector3d != null) {
      TerrainKindData terrainKindData =
          new TerrainKindData(
              UUID.randomUUID(),
              text,
              this.world2.dimension(),
              vector3d.x,
              vector3d.y,
              vector3d.z,
              kind.color(),
              kind,
              System.currentTimeMillis());
      return this.save(terrainKindData) ? terrainKindData : null;
    } else {
      return null;
    }
  }

  public TerrainKindData planPortal(TerrainKindData terrainKindData) {
    TerrainCanonicalService.PortalPlan portalPlan = TerrainCanonicalService.portal(terrainKindData);
    if (portalPlan != null && portalPlan.insideBorder()) {
      for (TerrainKindData currentTerrainKindData : this.savedWaypoints()) {
        if (currentTerrainKindData.kind() == TerrainKindData.Kind.PORTAL
            && currentTerrainKindData.dimension().equals(portalPlan.dimension())
            && Math.floor(currentTerrainKindData.x()) == Math.floor(portalPlan.x())
            && Math.floor(currentTerrainKindData.z()) == Math.floor(portalPlan.z())) {
          return currentTerrainKindData;
        }
      }

      TerrainKindData currentX =
          new TerrainKindData(
              UUID.randomUUID(),
              "Planned portal · " + terrainKindData.name(),
              portalPlan.dimension(),
              Math.floor(portalPlan.x()),
              64.0,
              Math.floor(portalPlan.z()),
              TerrainKindData.Kind.PORTAL.color(),
              TerrainKindData.Kind.PORTAL,
              System.currentTimeMillis());
      return this.save(currentX) ? currentX : null;
    } else {
      return null;
    }
  }

  public boolean save(TerrainKindData terrainKindData) {
    if (this.terrainPointsService == null) {
      return false;
    }

    try {
      this.terrainPointsService.put(terrainKindData);
      this.timestamp3++;
      this.text = "";
      if (this.world2 != null) {
        this.terrainGroundTracker.moved(terrainKindData, this.world2.player());
      }

      return true;
    } catch (IOException iOException) {
      this.text = iOException.getMessage();
      this.timestamp3++;
      return false;
    }
  }

  public void remove(UUID uUID) {
    if (this.terrainPointsService != null) {
      try {
        this.terrainPointsService.remove(uUID);
        this.timestamp3++;
        this.text = "";
        if (this.terrainGroundTracker.target() != null
            && this.terrainGroundTracker.target().id().equals(uUID)) {
          this.terrainGroundTracker.stop();
        }
      } catch (IOException iOException) {
        this.text = iOException.getMessage();
        this.timestamp3++;
      }
    }
  }

  public void goTo(TerrainKindData terrainKindData) {
    if (this.inCurrentDimension(terrainKindData)) {
      this.renderer3.camera.flyTo(terrainKindData.x(), terrainKindData.y(), terrainKindData.z());
    }
  }

  @Override
  public void close() {
    try {
      this.autoCloseable.close();
    } catch (Exception exception) {
    }

    this.terrainStateController.close();
    this.renderer3.markers.close();
    this.renderer4.markers.close();
    this.renderer3.players.close();
    this.renderer4.players.close();
    this.renderer3.shadows.close();
    this.renderer4.shadows.close();
    this.renderer3.route.close();
    this.renderer4.route.close();
    this.renderer2.destroyTarget(this.renderer3.target);
    this.renderer2.destroyTarget(this.renderer4.target);
    this.renderer2.close();
  }

  private static final class View {
    final TerrainViewportService camera = new TerrainViewportService();
    final TerrainMapRenderer.Target target;
    final GroundMarkerRenderer markers;
    final RouteRenderer route;
    final TerrainCloseService players;
    final PlayerShadowRenderer shadows;
    long lastRender;

    View(TerrainMapRenderer terrainMapRenderer, Render3dSceneService render3dScene) {
      this.target = terrainMapRenderer.createTarget();
      this.target
          .view()
          .scene()
          .sun(TerrainSunDirectionService.mapSun(TerrainSunDirectionService.DEFAULT_SUN, 1.0F));
      this.target
          .view()
          .scene()
          .ambient(
              TerrainSunDirectionService.mapAmbientColor(1.0F),
              TerrainSunDirectionService.mapAmbientIntensity(1.0F));
      this.markers = new GroundMarkerRenderer(render3dScene, this.target.view().scene());
      this.route = new RouteRenderer(render3dScene, this.target.view().scene());
      this.players = new TerrainCloseService(render3dScene, this.target.view().scene());
      this.shadows = new PlayerShadowRenderer(render3dScene, this.target.view().scene());
    }
  }
}
