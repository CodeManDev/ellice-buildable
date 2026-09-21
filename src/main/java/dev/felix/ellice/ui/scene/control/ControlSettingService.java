package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import java.util.Objects;
import java.util.function.Consumer;

public final class ControlSettingService extends ControlCompactService {
  public static final float DEFAULT_WIDTH = 36.0f;
  public static final float DEFAULT_HEIGHT = 20.0f;
  public static final MotionFiniteService TOGGLE_PROGRESS;
  private static final int count = 1802731408;
  private static final int count2 = -8530948;
  private static final int count3 = -1;
  private static final SceneEaseHandler f8mtzekmsj6k;
  private static final SceneEaseHandler fq9klyrad2e;
  private final ModuleSetting.Bool moduleBool;
  private final AutoCloseable autoCloseable;
  private float fhpxltearaqs;
  private boolean currentVisible;
  private boolean currentEnabled;
  private boolean enabled4;
  private boolean enabled5;
  private boolean enabled6;
  private boolean enabled7;
  private boolean enabled8;
  private float fazuf6jr90s4;

  public ControlSettingService(final ModuleSetting.Bool obj) {
    this.fhpxltearaqs = Float.intBitsToFloat(1054951342);
    this.currentVisible = true;
    this.currentEnabled = true;
    this.fazuf6jr90s4 = 1.0f;
    this.moduleBool = Objects.requireNonNull(obj, "setting");
    this.enabled6 = Boolean.TRUE.equals(obj.get());
    this.size(Float.intBitsToFloat(1108344832), Float.intBitsToFloat(1101004800));
    this.trackColor(1802731408);
    this.activeColor(-8530948);
    this.knobColor(-1);
    this.cursorStyle = CursorStyle.POINTER;
    super.value(this.enabled6);
    this.updateState(false);
    this.enabled7 = true;
    this.autoCloseable = obj.onStateChanged(this::refreshState);
  }

  public ModuleSetting.Bool setting() {
    return this.moduleBool;
  }

  @Override
  public boolean value() {
    return Boolean.TRUE.equals(this.moduleBool.get());
  }

  @Override
  public ControlSettingService value(final boolean b) {
    if (Boolean.TRUE.equals(this.moduleBool.get()) == b) {
      return this;
    }
    this.moduleBool.set(b);
    this.updateState2(b, this.enabled7);
    return this;
  }

  public float progress() {
    return ControlSettingService.TOGGLE_PROGRESS.get(this);
  }

  public boolean dependencyVisible() {
    return this.enabled4;
  }

  public boolean dependencyActive() {
    return this.enabled5;
  }

  @Override
  public ControlSettingService visible(final boolean currentVisible) {
    this.currentVisible = currentVisible;
    this.refreshState();
    return this;
  }

  public ControlSettingService enabled(final boolean currentEnabled) {
    this.currentEnabled = currentEnabled;
    this.refreshState();
    return this;
  }

  public ControlSettingService disabledOpacity(final float n) {
    if (!Float.isFinite(n) || n < 0.0f || n > 1.0f) {
      throw new IllegalArgumentException("disabled opacity must be finite and in [0, 1]");
    }
    this.fhpxltearaqs = n;
    this.updateState3(this.enabled7);
    return this;
  }

  @Override
  public ControlSettingService activeColor(final int n) {
    super.activeColor(n);
    this.invalidate();
    return this;
  }

  @Override
  public ControlSettingService trackColor(final int n) {
    super.trackColor(n);
    this.invalidate();
    return this;
  }

  @Override
  public ControlSettingService knobColor(final int n) {
    super.knobColor(n);
    this.invalidate();
    return this;
  }

  @Override
  public ControlSettingService onToggle(final Consumer<Boolean> consumer) {
    super.onToggle(consumer);
    return this;
  }

  public void refreshState() {
    this.updateState(this.enabled7);
  }

  public boolean tryToggle() {
    this.refreshState();
    if (!this.enabled4 || !this.enabled5 || this.enabled8) {
      return false;
    }
    final boolean b = !Boolean.TRUE.equals(this.moduleBool.get());
    this.moduleBool.set(b);
    this.updateState2(b, true);
    if (this.onToggle != null) {
      this.onToggle.accept(b);
    }
    return true;
  }

  @Override
  public void toggle() {
    this.handleClick();
  }

  @Override
  protected void handleClick() {
    if (this.tryToggle() && this.onClick != null) {
      this.onClick.run();
    }
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1108344832);
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1101004800);
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    if (this.materialStyle) {
      super.draw(compositorPushPresentationScaleService);
      return;
    }
    final float n =
        this.enabled5
            ? (this.pressed
                ? Float.intBitsToFloat(1064766013)
                : (this.hovered ? Float.intBitsToFloat(1065504211) : 1.0f))
            : 1.0f;
    if (Float.compare(this.fazuf6jr90s4, n) != 0) {
      this.fazuf6jr90s4 = n;
      MotionAnimateService.animate(
          this, MotionColorsContainer.Floats.SCALE, n, ControlSettingService.fq9klyrad2e);
    }
    super.draw(compositorPushPresentationScaleService);
  }

  @Override
  protected void onDetached() {
    this.dispose();
  }

  public void dispose() {
    if (this.enabled8) {
      return;
    }
    this.enabled8 = true;
    try {
      this.autoCloseable.close();
    } catch (final Exception ex) {
    }
  }

  private void updateState(final boolean b) {
    if (this.enabled8) {
      return;
    }
    final boolean b2 = this.currentVisible && this.moduleBool.isVisible();
    final boolean enabled5 = this.currentEnabled && this.moduleBool.isActive();
    final boolean b3 = this.enabled4 != b2;
    final boolean b4 = this.enabled5 != enabled5;
    this.enabled4 = b2;
    this.enabled5 = enabled5;
    if (this.visible != b2) {
      this.visible = b2;
      this.invalidateLayout();
    }
    final boolean interactive = b2 && enabled5;
    if (this.interactive != interactive) {
      this.interactive = interactive;
      this.invalidate();
    }
    final CursorStyle cursorStyle = interactive ? CursorStyle.POINTER : CursorStyle.DEFAULT;
    if (this.cursorStyle != cursorStyle) {
      this.cursorStyle = cursorStyle;
      this.invalidate();
    }
    if (b4 || b3 || !this.enabled7) {
      this.updateState3(b);
    }
    this.updateState2(Boolean.TRUE.equals(this.moduleBool.get()), b);
  }

  private void updateState2(final boolean enabled6, final boolean b) {
    if (this.enabled7 && this.enabled6 == enabled6) {
      return;
    }
    final float progress = this.progress();
    final float n = (this.enabled6 = enabled6) ? 1.0f : 0.0f;
    super.value(enabled6);
    if (b && Math.abs(progress - n) > Float.intBitsToFloat(953267991)) {
      ControlSettingService.TOGGLE_PROGRESS.set(this, progress);
      MotionAnimateService.animate(
          this,
          ControlSettingService.TOGGLE_PROGRESS,
          n,
          this.materialStyle
              ? MaterialIsLightService.FAST_SPATIAL
              : ControlSettingService.f8mtzekmsj6k);
    } else {
      MotionAnimateService.cancel(this, ControlSettingService.TOGGLE_PROGRESS);
      ControlSettingService.TOGGLE_PROGRESS.set(this, n);
    }
  }

  private void updateState3(final boolean b) {
    final float n = this.enabled5 ? 1.0f : this.fhpxltearaqs;
    if (b) {
      MotionAnimateService.animate(
          this, MotionColorsContainer.Floats.OPACITY, n, ControlSettingService.f8mtzekmsj6k);
    } else {
      MotionAnimateService.cancel(this, MotionColorsContainer.Floats.OPACITY);
      MotionColorsContainer.Floats.OPACITY.set(this, n);
    }
  }

  static {
    TOGGLE_PROGRESS =
        MotionFiniteService.of(
            "knobPosition",
            scenePctService -> scenePctService instanceof ControlSettingService,
            f -> Float.isFinite(f) && f >= 0.0f && f <= 1.0f,
            "a finite value in [0, 1]");
    f8mtzekmsj6k =
        new SceneEaseHandler.Tween(
            Float.intBitsToFloat(1045220557), SceneEaseHandler.Easing.EASE_IN_OUT);
    fq9klyrad2e =
        new SceneEaseHandler.Spring(
            Float.intBitsToFloat(1103101952), Float.intBitsToFloat(1139146752));
  }
}
