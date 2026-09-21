package dev.felix.ellice.ui.screen.builtin.studio;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.util.function.BiConsumer;

public final class StudioActionsService extends SceneCornerRadiusService {
  private BiConsumer<Float, Float> biConsumer;
  private Runnable runnable;
  private BiConsumer<Float, Float> biConsumer2;
  private Runnable runnable2;
  private float value;
  private float value2;
  private float value3;
  private float value4;
  private boolean dragged;
  private boolean focused;

  public StudioActionsService() {
    this.biConsumer = ((p0, p1) -> {});
    this.runnable = (() -> {});
    this.biConsumer2 = ((p0, p1) -> {});
    this.runnable2 = (() -> {});
    this.interactive(true).stopPropagation(true).cursorStyle(CursorStyle.POINTER);
  }

  public StudioActionsService actions(
      final Runnable runnable, final BiConsumer<Float, Float> biConsumer) {
    this.runnable = runnable;
    this.biConsumer = biConsumer;
    return this;
  }

  public StudioActionsService preview(
      final BiConsumer<Float, Float> biConsumer2, final Runnable runnable2) {
    this.biConsumer2 = biConsumer2;
    this.runnable2 = runnable2;
    return this;
  }

  public void focus(final boolean enabled) {
    this.focused = enabled;
    this.invalidate();
  }

  public void activate() {
    this.runnable.run();
  }

  @Override
  protected void onScenePress(final float n, final float n2) {
    this.value3 = n;
    this.value = n;
    this.value4 = n2;
    this.value2 = n2;
    this.dragged = false;
    MotionAnimateService.animate(
        this,
        MotionColorsContainer.Floats.CORNER_RADIUS,
        Float.intBitsToFloat(1090519040),
        MaterialEnterService.PRESS);
  }

  @Override
  protected void updateWhileScenePressed(final float n, final float n2) {
    this.value3 = n;
    this.value4 = n2;
    this.dragged |=
        (Math.hypot(n - this.value, n2 - this.value2)
            > Double.longBitsToDouble(4617315517961601024L));
    if (this.dragged) {
      this.biConsumer2.accept(n, n2);
    }
  }

  @Override
  protected void onRelease(final boolean b) {
    MotionAnimateService.animate(
        this,
        MotionColorsContainer.Floats.CORNER_RADIUS,
        Float.intBitsToFloat(1094713344),
        MaterialEnterService.RELEASE);
    this.runnable2.run();
    if (this.dragged) {
      this.biConsumer.accept(this.value3, this.value4);
    } else if (b) {
      this.runnable.run();
    }
  }

  @Override
  protected void handleClick() {}

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    super.draw(compositorPushPresentationScaleService);
    if (this.focused) {
      compositorPushPresentationScaleService.roundedRect(
          this.cx + 1.0f,
          this.cy + 1.0f,
          this.cw - 2.0f,
          this.ch - 2.0f,
          Float.intBitsToFloat(1094713344),
          Float.intBitsToFloat(1094713344),
          Float.intBitsToFloat(1094713344),
          Float.intBitsToFloat(1094713344),
          0,
          0.0f,
          0.0f,
          0,
          Float.intBitsToFloat(1069547520),
          -2701569,
          this.effectiveOpacity,
          0.0f);
    }
  }
}
