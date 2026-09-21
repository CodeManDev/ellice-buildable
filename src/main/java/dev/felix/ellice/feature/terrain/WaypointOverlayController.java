package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;

public final class WaypointOverlayController extends ScenePctService<WaypointOverlayController> implements AutoCloseable {
   private final TerrainMapController renderer;
   private final GroundMarkerRenderer terrainCloseService3;
   private final RouteRenderer terrainCloseService4;
   private final TerrainReservedService terrainReservedService = new TerrainReservedService();
   private TerrainViewportService.Frame frame;
   private boolean enabled;
   private List<TerrainKindData> items = List.of();

   public WaypointOverlayController(TerrainMapController terrainMapController, Render3dSceneService render3dScene) {
      this.renderer = terrainMapController;
      this.terrainCloseService3 = new GroundMarkerRenderer(render3dScene, render3dScene.scene());
      this.terrainCloseService4 = new RouteRenderer(render3dScene, render3dScene.scene());
      this.absolute().pointerEvents(false).visible(false);
      this.addChild(this.terrainReservedService);
   }

   public void update(EventAttackInputService.WorldRender worldRender, float value, float currentValue, boolean currentEnabled, boolean nextEnabled) {
      if (!currentEnabled) {
         this.hide();
      } else {
         Vector3d vector3d = new Vector3d(worldRender.cameraPos().x, worldRender.cameraPos().y, worldRender.cameraPos().z);
         Matrix4f matrix4f = new Matrix4f(worldRender.projectionMatrix()).mul(worldRender.viewMatrix());
         this.frame = new TerrainViewportService.Frame(
            vector3d,
            vector3d,
            matrix4f,
            new Matrix4f(matrix4f).invert(),
            value,
            currentValue,
            0.0,
            new Matrix4f(worldRender.viewMatrix()),
            new Matrix4f(worldRender.projectionMatrix())
         );
         this.items = this.renderer
            .visibleWaypoints()
            .stream()
            .sorted(
               Comparator.<TerrainKindData>comparingInt(item -> item.id().equals(this.renderer.selected()) ? 0 : 1)
                  .thenComparingDouble(item -> vector3d.distanceSquared(item.x(), item.y(), item.z()))
            )
            .limit(12L)
            .toList();
         this.terrainCloseService3.update(this.items, this.renderer.selected(), this.frame);
         if (nextEnabled) {
            this.terrainCloseService4.update(this.renderer.navigation(), this.frame);
         } else {
            this.terrainCloseService4.clear();
         }

         this.size(value, currentValue).visible(true);
         this.enabled = true;
      }
   }

   public void hide() {
      this.visible(false);
      this.frame = null;
      this.items = List.of();
      this.terrainReservedService.present(List.of(), TerrainReservedService.View.WORLD, null, false);
      if (this.enabled) {
         this.terrainCloseService3.clear();
         this.terrainCloseService4.clear();
         this.enabled = false;
      }
   }

   public void reserved(List<TerrainReservedService.Box> items) {
      this.terrainReservedService.reserved(items);
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.frame != null) {
         ArrayList arrayList = new ArrayList();
         TerrainKindData terrainKindData = this.renderer.navigation().target();

         for (TerrainKindData currentTerrainKindData : this.items) {
            boolean enabled = currentTerrainKindData.id().equals(this.renderer.selected());
            Vector4f currentX = this.frame
               .viewProjection()
               .transform(
                  new Vector4f(
                     (float)(currentTerrainKindData.x() - this.frame.focus().x),
                     (float)(currentTerrainKindData.y() - this.frame.focus().y),
                     (float)(currentTerrainKindData.z() - this.frame.focus().z),
                     1.0F
                  )
               );
            if (currentX.isFinite()) {
               float value = currentX.x / Math.max(0.001F, Math.abs(currentX.w));
               float currentValue = -currentX.y / Math.max(0.001F, Math.abs(currentX.w));
               int nextValue = !(currentX.w <= 0.0F)
                     && !(Math.abs(value) > 0.91)
                     && !(Math.abs(currentValue) > 0.84)
                  ? 0
                  : 1;
               if (nextValue == 0 || enabled) {
                  if (currentX.w <= 0.0F) {
                     value = Math.copySign(Math.max(0.3F, Math.abs(value)), value);
                     currentValue = Math.max(0.7F, Math.abs(currentValue));
                  }

                  float previousValue = (value + 1.0F) * this.cw / 2.0F;
                  float sourceValue = (currentValue + 1.0F) * this.ch / 2.0F;
                  if (nextValue != 0) {
                     float targetValue = Math.max(
                        Math.abs(value) / 0.88F, Math.abs(currentValue) / 0.74F
                     );
                     previousValue = (value / targetValue + 1.0F) * this.cw / 2.0F;
                     sourceValue = (currentValue / targetValue + 1.0F) * this.ch / 2.0F;
                  }

                  previousValue = Math.clamp(
                     previousValue,
                     24.0F,
                     Math.max(24.0F, this.cw - 24.0F)
                  );
                  sourceValue = Math.clamp(
                     sourceValue,
                     68.0F,
                     Math.max(68.0F, this.ch - 34.0F)
                  );
                  arrayList.add(
                     new TerrainReservedService.Anchor(
                        currentTerrainKindData,
                        previousValue,
                        sourceValue,
                        this.frame.eye().distance(currentTerrainKindData.x(), currentTerrainKindData.y(), currentTerrainKindData.z()),
                        enabled,
                        terrainKindData != null && terrainKindData.id().equals(currentTerrainKindData.id()),
                        nextValue != 0 ? createText(value, currentValue) : ""
                     )
                  );
               }
            }
         }

         this.terrainReservedService.present(arrayList, TerrainReservedService.View.WORLD, null, false);
      }
   }

   private static String createText(float value, float currentValue) {
      int index = Math.floorMod((int)Math.round(Math.atan2(currentValue, value) / 0.7853981633974483), 8);
      return new String[]{"→", "↘", "↓", "↙", "←", "↖", "↑", "↗"}[index];
   }

   @Override
   public void close() {
      this.hide();
      this.terrainCloseService3.close();
      this.terrainCloseService4.close();
   }
}

