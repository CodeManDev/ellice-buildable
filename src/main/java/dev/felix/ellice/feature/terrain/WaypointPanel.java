package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.ui.component.ComponentOperationHandler;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentThemeService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class WaypointPanel implements ComponentOperationHandler {
   private final TerrainGroundTracker terrainGroundTracker;
   private final Runnable runnable;

   public WaypointPanel(TerrainMapController terrainMapController) {
      this(terrainMapController.navigation(), terrainMapController::returnToPlayer);
   }

   public WaypointPanel(TerrainGroundTracker currentTerrainGroundTracker, Runnable currentRunnable) {
      this.terrainGroundTracker = currentTerrainGroundTracker;
      this.runnable = currentRunnable;
   }

   @Override
   public ComponentKeyService<?> render(ComponentThemeService componentTheme) {
      TerrainKindData terrainKindData = this.terrainGroundTracker.target();
      if (terrainKindData == null) {
         return ComponentBoxService.column(item -> {});
      }

      TerrainGroundTracker.Instruction currentInstruction = this.terrainGroundTracker.instruction();
      int height = this.terrainGroundTracker.status() == TerrainGroundTracker.Status.ARRIVED ? 1 : 0;
      String currentText = TerrainReservedService.distance(this.terrainGroundTracker.remaining());
      String nextText = this.terrainGroundTracker.status() == TerrainGroundTracker.Status.READY
         ? (
            this.terrainGroundTracker.remaining() / 4.3 < 60.0
               ? "<1 min walk"
               : (int)Math.ceil(
                     this.terrainGroundTracker.remaining()
                        / 4.3
                        / 60.0
                  )
                  + " min walk"
         )
         : "";
      return ComponentBoxService.panel(
            item -> item.direction(ScenePctService.Direction.COLUMN)
               .width(LayoutOperationHandler.percent(100.0F))
               .padding(14.0F)
               .gap(12.0F)
               .cornerRadius(22.0F)
               .backgroundColor(MaterialIsLightService.SURFACE_CONTAINER)
               .shadow(9.0F)
               .layerBreak(true),
            ComponentBoxService.row(
               item -> item.gap(12.0F).align(ScenePctService.Align.CENTER),
               ComponentBoxService.panel(
                  item -> item.size(58.0F, 58.0F)
                     .flexShrink(0.0F)
                     .cornerRadius(20.0F)
                     .backgroundColor(MaterialIsLightService.PRIMARY)
                     .direction(ScenePctService.Direction.COLUMN)
                     .align(ScenePctService.Align.CENTER)
                     .justify(ScenePctService.Justify.CENTER),
                  ComponentBoxService.<TerrainRenderer.GlyphNode>node(
                        "map-direction-glyph",
                        TerrainRenderer.GlyphNode::new,
                        item -> item.glyph(currentInstruction.icon(), MaterialIsLightService.ON_PRIMARY)
                           .size(58.0F, 58.0F)
                     )
                     .key("turn:" + currentInstruction.icon())
                     .onMount(item -> {
                        item.opacity(0.0F).translateY(3.0F);
                        MotionAnimateService.animate(
                           item, MotionColorsContainer.Floats.OPACITY, 1.0F, SceneEaseHandler.Tween.ease(0.16F)
                        );
                        MotionAnimateService.animate(item, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0F, SceneEaseHandler.Spring.SNAPPY);
                     })
               ),
               ComponentBoxService.column(
                  item -> item.flex(1.0F).minWidth(0.0F).gap(3.0F),
                  MaterialTextService.text(currentInstruction.text(), 14.0F, MaterialIsLightService.ON_SURFACE)
                     .props(item -> item.fontFamily("material-roboto-medium").wordWrap(true)),
                  MaterialTextService.text(
                     height != 0
                        ? terrainKindData.name()
                        : (
                           this.terrainGroundTracker.status() == TerrainGroundTracker.Status.READY
                              ? "In " + TerrainReservedService.distance(currentInstruction.distance())
                              : "Walking directions"
                        ),
                     11.0F,
                     MaterialIsLightService.ON_SURFACE_VARIANT
                  )
               ),
               MaterialTextService.iconButton("map.navigation.stop", "close", "End navigation", () -> {
                  this.terrainGroundTracker.stop();
                  componentTheme.invalidate();
               })
            ),
            ComponentBoxService.row(
               item -> item.align(ScenePctService.Align.CENTER).gap(8.0F),
               ComponentBoxService.column(
                  item -> item.flex(1.0F).minWidth(0.0F).gap(2.0F),
                  MaterialTextService.text(terrainKindData.name(), 12.0F, MaterialIsLightService.ON_SURFACE)
                     .props(item -> item.fontFamily("material-roboto-medium")),
                  MaterialTextService.text(
                     height != 0 ? "Destination reached" : currentText + (nextText.isBlank() ? "" : " · " + nextText),
                     11.0F,
                     MaterialIsLightService.ON_SURFACE_VARIANT
                  )
               ),
               MaterialTextService.iconButton("map.navigation.recenter", "person", "Recenter on player", this.runnable)
            )
         )
         .key("navigation-card")
         .onMount(item -> {
            item.opacity(0.0F).translateY(12.0F);
            MotionAnimateService.animate(item, MotionColorsContainer.Floats.OPACITY, 1.0F, SceneEaseHandler.Tween.ease(0.2F));
            MotionAnimateService.animate(item, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0F, SceneEaseHandler.Spring.GENTLE);
         });
   }

   public static String symbol(String text) {
      return TerrainRenderer.symbol(text);
   }
}
