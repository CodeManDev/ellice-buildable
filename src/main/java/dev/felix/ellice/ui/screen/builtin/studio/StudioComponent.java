package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.feature.studio.StudioCanvasRenderer;
import dev.felix.ellice.feature.studio.StudioListenerService;
import dev.felix.ellice.feature.studio.StudioValidateValidator;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.function.Supplier;

public final class StudioComponent extends ScenePctService<StudioComponent> {
  private final StudioListenerService items;
  private final Supplier<StudioValidateValidator.Frame> supplier;
  private final Supplier<Float> supplier2;
  private final StudioCanvasRenderer renderer = new StudioCanvasRenderer();

  public StudioComponent(
      StudioListenerService studioListener,
      Supplier<StudioValidateValidator.Frame> currentSupplier,
      Supplier<Float> nextSupplier) {
    this.items = studioListener;
    this.supplier = currentSupplier;
    this.supplier2 = nextSupplier;
    this.pointerEvents(false);
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    compositorPushPresentationScale.pushClip(this.cx, this.cy, this.cw, this.ch, 16.0F);
    compositorPushPresentationScale.studioPrimitive(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        16.0F,
        -15261645,
        -14408392,
        this.effectiveOpacity,
        new CompositorPushPresentationScaleService.StudioPaint(
            0, this.supplier2.get(), 0.0F, 0.0F, 1.0F, 0.0F));
    compositorPushPresentationScale.nextLayer();
    float y =
        Math.min(
            (this.cw - 16.0F) / this.items.project.width,
            (this.ch - 16.0F) / this.items.project.height);
    this.renderer.draw(
        compositorPushPresentationScale,
        this.items.project,
        this.supplier.get(),
        this.cx + (this.cw - this.items.project.width * y) / 2.0F,
        this.cy + (this.ch - this.items.project.height * y) / 2.0F,
        y,
        this.effectiveOpacity,
        this.supplier2.get());
    compositorPushPresentationScale.popClip();
  }
}
