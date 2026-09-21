package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.geometry.GeometryCubeService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

final class PlayerShadowRenderer implements AutoCloseable {
  static final float BLOB_SIZE = 0.95f;
  static final float BLOB_LIFT = 0.045f;
  static final float BLOB_ALPHA = 0.38f;
  static final double FADE_START = 300.0;
  static final double FADE_END = 600.0;
  private final Render3dSceneService renderer;
  private final Render3dAddService renderer2;
  private final GpuMesh renderer3;
  private final ShaderDefinition shaderData2 =
      new ShaderDefinition(
          "ellice:map-player-shadow", "render3d/pbr.vert", "terrain/player_shadow.frag");
  private final Map<UUID, Entry> entries = new HashMap<UUID, Entry>();

  PlayerShadowRenderer(
      Render3dSceneService render3dSceneService, Render3dAddService render3dAddService) {
    this.renderer = render3dSceneService;
    this.renderer2 = render3dAddService;
    this.renderer3 = render3dSceneService.createMesh(GeometryCubeService.plane());
  }

  private Entry createEntry() {
    MaterialErFactory materialErFactory =
        MaterialErFactory.builder("ellice:map-player-shadow-blob", this.shaderData2)
            .queue(MaterialErFactory.Queue.TRANSPARENT)
            .build();
    return new Entry(
        this.renderer2.add(this.renderer3, materialErFactory, GeometryXService.IDENTITY),
        materialErFactory);
  }

  void update(
      List<CompatLoadedHandler.MapPlayer> list,
      Vector3f vector3f,
      TerrainViewportService.Frame frame,
      TerrainCloseService terrainCloseService,
      Map<UUID, Double> map) {
    float f = PlayerShadowRenderer.shadowStrength(vector3f, frame.distance());
    int n = f > Float.intBitsToFloat(981668463) ? 1 : 0;
    HashSet<UUID> hashSet = new HashSet<UUID>();
    for (CompatLoadedHandler.MapPlayer object : list) {
      hashSet.add(object.id());
      Entry entry = this.entries.get(object.id());
      if (entry == null) {
        entry = this.createEntry();
        this.entries.put(object.id(), entry);
      }
      Vector3d vector3d =
          terrainCloseService == null ? null : terrainCloseService.smoothed(object.id());
      double d = vector3d == null ? object.x() : vector3d.x;
      double d2 = vector3d == null ? object.y() : vector3d.y;
      double d3 = vector3d == null ? object.z() : vector3d.z;
      double d4 = map == null || map.get(object.id()) == null ? d2 : map.get(object.id());
      double d5 = Math.max(0.0, d2 - d4);
      float f2 = TerrainSunDirectionService.shadowHeightScale(d5);
      float f3 = f * TerrainSunDirectionService.shadowHeightFade(d5);
      entry.blobMaterial.color(
          "uColor", PlayerShadowRenderer.fadedBlack(Float.intBitsToFloat(1052938076), f3));
      boolean bl =
          n != 0
              && f3 > Float.intBitsToFloat(981668463)
              && frame.project(d, d4 + Double.longBitsToDouble(4606281698874543309L), d3) != null;
      float f4 = (object.crouching() ? Float.intBitsToFloat(1062836634) : 1.0f) * f2;
      entry.blob.transform(
          new GeometryXService(
              d,
              d4 + Double.longBitsToDouble(4586646004756905984L),
              d3,
              new Quaternionf(),
              new Vector3f(
                  Float.intBitsToFloat(0x3F733333) * f4,
                  1.0f,
                  Float.intBitsToFloat(0x3F733333) * f4)));
      entry.blob.visible(bl);
    }
    for (UUID uUID2 :
        this.entries.keySet().stream().filter(uUID -> !hashSet.contains(uUID)).toList()) {
      this.updateState(uUID2);
    }
  }

  void clear() {
    for (UUID uUID : this.entries.keySet().stream().toList()) {
      this.updateState(uUID);
    }
  }

  private void updateState(UUID uUID) {
    Entry entry = this.entries.remove(uUID);
    if (entry == null) {
      return;
    }
    this.renderer2.remove(entry.blob);
  }

  static float shadowStrength(Vector3f vector3f, double d) {
    float f;
    float f2 = Float.intBitsToFloat(1062333317);
    if (vector3f != null
        && vector3f.isFinite()
        && (f = vector3f.length()) > Float.intBitsToFloat(897988541)) {
      f2 = vector3f.y / f;
    }
    f = Math.clamp(f2, Float.intBitsToFloat(1034147594), 1.0f);
    return Math.clamp(f * Float.intBitsToFloat(1069547520), Float.intBitsToFloat(1050253722), 1.0f)
        * PlayerShadowRenderer.distanceFade(d);
  }

  static float distanceFade(double d) {
    if (!Double.isFinite(d)) {
      return 1.0f;
    }
    if (d <= Double.longBitsToDouble(4643985272004935680L)) {
      return 1.0f;
    }
    if (d >= Double.longBitsToDouble(4648488871632306176L)) {
      return 0.0f;
    }
    return (float)
        ((Double.longBitsToDouble(4648488871632306176L) - d)
            / Double.longBitsToDouble(4643985272004935680L));
  }

  static int fadedBlack(float f, float f2) {
    int n =
        Math.round(
            Math.clamp(f, 0.0f, 1.0f)
                * Math.clamp(f2, 0.0f, 1.0f)
                * Float.intBitsToFloat(1132396544));
    return Math.clamp((long) n, 0, 255) << 24;
  }

  @Override
  public void close() {
    this.clear();
    this.renderer.destroyMesh(this.renderer3);
  }

  private static final class Entry {
    final Render3dIdService blob;
    final MaterialErFactory blobMaterial;

    Entry(Render3dIdService render3dIdService, MaterialErFactory materialErFactory) {
      this.blob = render3dIdService;
      this.blobMaterial = materialErFactory;
    }
  }
}
