package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import java.util.List;
import org.joml.Vector3d;

final class RouteRenderer implements AutoCloseable {
  private final Render3dSceneService renderer;
  private final Render3dAddService renderer2;
  private final MaterialErFactory materialErFactory =
      MaterialErFactory.builder(
              "ellice:map-route",
              new ShaderDefinition("ellice:map-route", "terrain/route.vert", "terrain/route.frag"))
          .queue(MaterialErFactory.Queue.TRANSPARENT)
          .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
          .build();
  private List<Vector3d> items = List.of();
  private Render3dIdService renderer3;
  private GpuMesh renderer4;
  private long timestamp;

  RouteRenderer(Render3dSceneService render3dScene, Render3dAddService render3dAdd) {
    this.renderer = render3dScene;
    this.renderer2 = render3dAdd;
  }

  void update(TerrainGroundTracker terrainGroundTracker, TerrainViewportService.Frame frame) {
    List currentItems = terrainGroundTracker.route();
    if (currentItems != this.items) {
      this.clear();
      this.items = currentItems;
      if (currentItems.size() >= 2) {
        this.renderer4 = this.renderer.createMesh(TerrainRecoveredFactory.create(currentItems));
        Vector3d vector3d = (Vector3d) currentItems.getFirst();
        this.renderer3 =
            this.renderer2.add(
                this.renderer4,
                this.materialErFactory,
                new GeometryXService(vector3d.x, vector3d.y, vector3d.z));
        this.timestamp = System.nanoTime();
      }
    }

    if (this.renderer3 != null) {
      float currentHeight =
          (float) Math.clamp(frame.distance() * 0.828427 / frame.height() * 4.0, 0.18, 0.45);
      this.materialErFactory
          .uniform("uWidth", currentHeight)
          .uniform("uProgress", (float) terrainGroundTracker.progress())
          .color("uRouteColor", MaterialIsLightService.PRIMARY)
          .color("uRouteEdge", MaterialIsLightService.ON_PRIMARY)
          .uniform("uReveal", (float) Math.min(1.0, (System.nanoTime() - this.timestamp) / 4.5E8));
    }
  }

  void clear() {
    if (this.renderer3 != null) {
      this.renderer2.remove(this.renderer3);
    }

    if (this.renderer4 != null) {
      this.renderer.destroyMesh(this.renderer4);
    }

    this.renderer3 = null;
    this.renderer4 = null;
    this.items = List.of();
  }

  @Override
  public void close() {
    this.clear();
  }
}
