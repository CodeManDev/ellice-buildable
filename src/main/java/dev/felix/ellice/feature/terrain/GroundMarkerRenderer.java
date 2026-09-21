package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.geometry.GeometryCubeService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import java.util.List;
import java.util.UUID;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

final class GroundMarkerRenderer implements AutoCloseable {
   private final Render3dSceneService renderer;
   private final Render3dAddService renderer2;
   private final GpuMesh renderer3;
   private Render3dIdService renderer4;
   private final MaterialErFactory materialErFactory = MaterialErFactory.builder(
         "ellice:map-ground-locator", new ShaderDefinition("ellice:map-ground-locator", "render3d/pbr.vert", "terrain/marker.frag")
      )
      .queue(MaterialErFactory.Queue.TRANSPARENT)
      .build();

   GroundMarkerRenderer(Render3dSceneService render3dScene, Render3dAddService render3dAdd) {
      this.renderer = render3dScene;
      this.renderer2 = render3dAdd;
      this.renderer3 = render3dScene.createMesh(GeometryCubeService.plane());
   }

   void update(List<TerrainKindData> items, UUID uUID, TerrainViewportService.Frame frame) {
      TerrainKindData terrainKindData = items.stream().filter(item -> item.id().equals(uUID)).findFirst().orElse(null);
      if (terrainKindData != null && frame.project(terrainKindData.x(), terrainKindData.y(), terrainKindData.z()) != null) {
         if (this.renderer4 == null) {
            this.renderer4 = this.renderer2.add(this.renderer3, this.materialErFactory, GeometryXService.IDENTITY);
         }

         Vector4f currentX = frame.viewProjection()
            .transform(
               new Vector4f(
                  (float)(terrainKindData.x() - frame.focus().x), (float)(terrainKindData.y() - frame.focus().y), (float)(terrainKindData.z() - frame.focus().z), 1.0F
               )
            );
         float currentHeight = (float)Math.clamp(
            currentX.w
               * 2.0
               / frame.projection().m11()
               / frame.height()
               * 24.0,
            0.6,
            12.0
         );
         this.materialErFactory.color("uColor", MaterialIsLightService.layer(terrainKindData.color(), MaterialIsLightService.PRIMARY, 0.25F));
         this.renderer4
            .transform(
               new GeometryXService(
                  terrainKindData.x(),
                  terrainKindData.y() + 0.035,
                  terrainKindData.z(),
                  new Quaternionf(),
                  new Vector3f(currentHeight, 1.0F, currentHeight)
               )
            );
      } else {
         this.clear();
      }
   }

   void clear() {
      if (this.renderer4 != null) {
         this.renderer2.remove(this.renderer4);
      }

      this.renderer4 = null;
   }

   @Override
   public void close() {
      this.clear();
      this.renderer.destroyMesh(this.renderer3);
   }
}
