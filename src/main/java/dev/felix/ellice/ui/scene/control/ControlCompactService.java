package dev.felix.ellice.ui.scene.control;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.material.MaterialCompactToggleService;
import dev.felix.ellice.ui.material.MaterialEnterService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;
import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.function.Consumer;

public class ControlCompactService extends ScenePctService<ControlCompactService> {
  protected boolean materialStyle;
  private boolean enabled;
  private boolean enabled2;
  private float fe1guwcmhnsj;
  private boolean enabled3;
  private static final MotionFiniteService f588u4dkp372;
  private boolean enabled4;
  private float value3;
  int trackColor;
  int activeColor;
  int knobColor;
  public Consumer<Boolean> onToggle;

  public ControlCompactService compact(final boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public ControlCompactService material(final boolean materialStyle) {
    this.materialStyle = materialStyle;
    this.invalidate();
    return this;
  }

  public void materialFocus(final boolean enabled2) {
    this.enabled2 = enabled2;
    this.invalidate();
  }

  private void updateState() {
    if (this.pressed != this.enabled3) {
      this.enabled3 = this.pressed;
      MotionAnimateService.animate(
          this,
          ControlCompactService.f588u4dkp372,
          this.pressed ? 1.0f : 0.0f,
          this.pressed ? MaterialEnterService.PRESS : MaterialEnterService.RELEASE);
    }
  }

  public ControlCompactService() {
    this.trackColor = Integer.MIN_VALUE;
    this.activeColor = Integer.MIN_VALUE;
    this.knobColor = Integer.MIN_VALUE;
    this.interactive = true;
  }

  public ControlCompactService value(final boolean enabled4) {
    this.enabled4 = enabled4;
    this.value3 = (enabled4 ? 1.0f : 0.0f);
    return this;
  }

  public ControlCompactService trackColor(final int trackColor) {
    this.trackColor = trackColor;
    return this;
  }

  public ControlCompactService activeColor(final int activeColor) {
    this.activeColor = activeColor;
    return this;
  }

  public ControlCompactService knobColor(final int knobColor) {
    this.knobColor = knobColor;
    return this;
  }

  public ControlCompactService onToggle(final Consumer<Boolean> onToggle) {
    this.onToggle = onToggle;
    return this;
  }

  public boolean value() {
    return this.enabled4;
  }

  public void toggle() {
    this.handleClick();
  }

  @Override
  protected void handleClick() {
    this.enabled4 = !this.enabled4;
    MotionAnimateService.animate(
        this,
        MotionFiniteService.finite(
            "knobPosition", scenePctService -> scenePctService instanceof ControlCompactService),
        this.enabled4 ? 1.0f : 0.0f,
        this.materialStyle ? MaterialIsLightService.FAST_SPATIAL : SceneEaseHandler.Spring.SNAPPY);
    if (this.onToggle != null) {
      this.onToggle.accept(this.enabled4);
    }
    if (this.onClick != null) {
      this.onClick.run();
    }
  }

  @Override
  public float getAnimProperty(final String s) {
    if ("materialPress".equals(s)) {
      return this.fe1guwcmhnsj;
    }
    if ("knobPosition".equals(s)) {
      return this.value3;
    }
    return super.getAnimProperty(s);
  }

  @Override
  public void setAnimProperty(final String s, final float n) {
    if ("materialPress".equals(s)) {
      this.fe1guwcmhnsj = n;
      return;
    }
    if ("knobPosition".equals(s)) {
      this.value3 = n;
      return;
    }
    super.setAnimProperty(s, n);
  }

  private ThemeIsSetService collectValues() {
    return CoreIsInitializedHandler.get().theme();
  }

  private int calculateValue(final int n, final String s) {
    return ThemeIsSetService.isSet(n) ? n : this.collectValues().color(s);
  }

  @Override
  public float intrinsicWidth(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1107296256);
  }

  @Override
  public float intrinsicHeight(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    return Float.intBitsToFloat(1099956224);
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    if (this.materialStyle) {
      this.updateState();
      if (this.enabled) {
        MaterialCompactToggleService.compactToggle(
            compositorPushPresentationScaleService,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.value3,
            this.fe1guwcmhnsj,
            this.hovered,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      } else {
        MaterialCompactToggleService.toggle(
            compositorPushPresentationScaleService,
            this.cx,
            this.cy,
            this.cw,
            this.ch,
            this.value3,
            this.fe1guwcmhnsj,
            this.hovered,
            this.interactive,
            this.enabled2,
            this.effectiveOpacity);
      }
      return;
    }
    final float effectiveOpacity = this.effectiveOpacity;
    final float effectiveEdgeSoftness = this.effectiveEdgeSoftness;
    final float max = Math.max(0.0f, Math.min(1.0f, this.value3));
    final int calculateValue = this.calculateValue(this.trackColor, "toggle.track");
    final int currentValue = this.calculateValue(this.activeColor, "toggle.active");
    final int nextValue = this.calculateValue(this.knobColor, "toggle.knob");
    final int lerpColor = lerpColor(calculateValue, currentValue, max);
    final float n = this.ch / 2.0f;
    compositorPushPresentationScaleService.roundedRect(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        n,
        n,
        n,
        n,
        ScenePctService.mulAlpha(lerpColor, effectiveOpacity),
        0.0f,
        0.0f,
        0,
        0.0f,
        0,
        1.0f,
        effectiveEdgeSoftness);
    final float n2 = 2.0f;
    final float n3 = this.ch - n2 * 2.0f;
    final float n4 = this.cx + n2 + max * (this.cw - n3 - n2 * 2.0f);
    final float n5 = n3 / 2.0f;
    compositorPushPresentationScaleService.roundedRect(
        n4,
        this.cy + n2,
        n3,
        n3,
        n5,
        n5,
        n5,
        n5,
        ScenePctService.mulAlpha(nextValue, effectiveOpacity),
        0.0f,
        (effectiveOpacity > Float.intBitsToFloat(1041865114)) ? 2.0f : 0.0f,
        805306368,
        0.0f,
        0,
        1.0f,
        effectiveEdgeSoftness);
  }

  static int lerpColor(final int n, final int n2, float max) {
    max = Math.max(0.0f, Math.min(1.0f, max));
    return calculateValue2(n >> 24 & 0xFF, n2 >> 24 & 0xFF, max) << 24
        | calculateValue2(n >> 16 & 0xFF, n2 >> 16 & 0xFF, max) << 16
        | calculateValue2(n >> 8 & 0xFF, n2 >> 8 & 0xFF, max) << 8
        | calculateValue2(n & 0xFF, n2 & 0xFF, max);
  }

  private static int calculateValue2(final int n, final int n2, final float n3) {
    return (int) (n + (n2 - n) * n3);
  }

  static {
    f588u4dkp372 =
        MotionFiniteService.of(
            "materialPress",
            scenePctService -> scenePctService instanceof ControlCompactService,
            Float::isFinite,
            "a finite value");
  }
}
