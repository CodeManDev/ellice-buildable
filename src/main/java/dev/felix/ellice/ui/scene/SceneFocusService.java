package dev.felix.ellice.ui.scene;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionFiniteService;

public final class SceneFocusService extends ScenePctService<SceneFocusService> {
  public static final MotionFiniteService SERVERS =
      MotionFiniteService.finite("menuServers", item -> item instanceof SceneFocusService);
  public static final MotionFiniteService WORLDS =
      MotionFiniteService.finite("menuWorlds", item -> item instanceof SceneFocusService);
  public static final MotionFiniteService FOCUS =
      MotionFiniteService.finite("menuFocus", item -> item instanceof SceneFocusService);
  private final long timestamp = System.nanoTime();
  private float value;
  private boolean enabled;
  private float value2;
  private float value3;
  private float value4;
  private int count;

  public SceneFocusService focus(boolean enabled) {
    MotionAnimateService.animate(
        this, FOCUS, enabled ? 1.0F : 0.0F, new SceneEaseHandler.Spring(24.0F, 140.0F));
    return this;
  }

  public SceneFocusService page(int value) {
    if (value < 0 || value > 2) {
      throw new IllegalArgumentException("Unknown menu page: " + value);
    }

    if (this.count == value) {
      return this;
    }

    this.count = value;
    MotionAnimateService.animate(
        this, SERVERS, value == 1 ? 1.0F : 0.0F, new SceneEaseHandler.Spring(16.0F, 64.0F));
    MotionAnimateService.animate(
        this, WORLDS, value == 2 ? 1.0F : 0.0F, new SceneEaseHandler.Spring(18.0F, 74.0F));
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "menuServers" -> this.value2;
      case "menuWorlds" -> this.value3;
      case "menuFocus" -> this.value4;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float value) {
    switch (text) {
      case "menuServers":
        this.value2 = value;
        break;
      case "menuWorlds":
        this.value3 = value;
        break;
      case "menuFocus":
        this.value4 = value;
        break;
      default:
        super.setAnimProperty(text, value);
    }
  }

  public SceneFocusService radius(float currentValue) {
    this.value = currentValue;
    return this;
  }

  public SceneFocusService backdrop(boolean currentEnabled) {
    this.enabled = currentEnabled;
    return this;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    compositorPushPresentationScale.menuBackground(
        this.cx,
        this.cy,
        this.cw,
        this.ch,
        this.value,
        (float) ((System.nanoTime() - this.timestamp) / 1.0E9),
        this.effectiveOpacity,
        this.value2,
        this.value3,
        this.value4);
    if (this.enabled) {
      compositorPushPresentationScale.nextLayer();
    }
  }
}
