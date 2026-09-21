package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.feature.studio.StudioShapeService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;

public final class StudioKindService extends ScenePctService<StudioKindService> {
   private StudioShapeService.ShapeKind shapeKind = StudioShapeService.ShapeKind.PANEL;

   public StudioKindService kind(StudioShapeService.ShapeKind currentShapeKind) {
      this.shapeKind = currentShapeKind;
      this.pointerEvents(false);
      return this;
   }

   @Override
   protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      if (this.shapeKind == StudioShapeService.ShapeKind.TEXT) {
         compositorPushPresentationScale.text(
            this.cx + 5.0F,
            this.cy + 3.0F,
            "T",
            19.0F,
            -1715982,
            MaterialIsLightService.LABEL
         );
      } else if (this.shapeKind == StudioShapeService.ShapeKind.GROUP) {
         compositorPushPresentationScale.roundedRect(
            this.cx + 2.0F,
            this.cy + 2.0F,
            this.cw - 4.0F,
            this.ch - 4.0F,
            6.0F,
            6.0F,
            6.0F,
            6.0F,
            0,
            0.0F,
            0.0F,
            0,
            1.0F,
            -6830636,
            this.effectiveOpacity,
            0.0F
         );
         compositorPushPresentationScale.roundedRect(
            this.cx + 7.0F,
            this.cy + 7.0F,
            8.0F,
            8.0F,
            3.0F,
            -4344587
         );
         compositorPushPresentationScale.roundedRect(
            this.cx + 15.0F,
            this.cy + 15.0F,
            6.0F,
            6.0F,
            3.0F,
            -7416383
         );
      } else {
         byte byteValue = switch (this.shapeKind) {
            case ELLIPSE -> 2;
            case RING -> 3;
            case DIAMOND -> 4;
            case BAR -> 6;
            default -> 1;
         };
         compositorPushPresentationScale.studioPrimitive(
            this.cx + 3.0F,
            this.cy + (this.shapeKind == StudioShapeService.ShapeKind.BAR ? 10 : 3),
            this.cw - 6.0F,
            this.shapeKind == StudioShapeService.ShapeKind.BAR ? 9.0F : this.ch - 6.0F,
            6.0F,
            -3754254,
            -7680051,
            this.effectiveOpacity,
            new CompositorPushPresentationScaleService.StudioPaint(byteValue, 0.0F, 0.7F, 3.0F, 1.0F, 0.0F)
         );
      }
   }
}
