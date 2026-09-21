package dev.felix.ellice.ui.scene.motion;

import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;

public final class MotionAnimateService {
  private static final float value = 1.0E-6F;

  private MotionAnimateService() {}

  public static <N extends ScenePctService<?>> N animate(
      N n, MotionFiniteService motionFinite, float value, SceneEaseHandler sceneEase) {
    return animate((N) n, motionFinite, value, sceneEase, 0.0F);
  }

  public static <N extends ScenePctService<?>> N animate(
      N n,
      MotionFiniteService motionFinite,
      float value,
      SceneEaseHandler sceneEase,
      float currentValue) {
    return animate((N) n, motionFinite, value, sceneEase, currentValue, null);
  }

  public static <N extends ScenePctService<?>> N animate(
      N n,
      MotionFiniteService motionFinite,
      float value,
      SceneEaseHandler sceneEase,
      float currentValue,
      Runnable runnable) {
    updateState(n, motionFinite, value, sceneEase, currentValue);
    n.animate(motionFinite.compatibilityName(), value, sceneEase, currentValue, 0, false, runnable);
    return (N) n;
  }

  public static <N extends ScenePctService<?>> N animate(
      N n,
      MotionCompatibilityNameService motionCompatibilityName,
      int value,
      SceneEaseHandler sceneEase) {
    return animate((N) n, motionCompatibilityName, value, sceneEase, 0.0F);
  }

  public static <N extends ScenePctService<?>> N animate(
      N n,
      MotionCompatibilityNameService motionCompatibilityName,
      int value,
      SceneEaseHandler sceneEase,
      float currentValue) {
    Objects.requireNonNull(motionCompatibilityName, "property").requireSupported(n);
    validate(sceneEase);
    updateState2(currentValue);
    n.animateColor(
        motionCompatibilityName.compatibilityName(), value, sceneEase, currentValue, null);
    return (N) n;
  }

  public static <N extends ScenePctService<?>> N loop(
      N n,
      MotionFiniteService motionFinite,
      float value,
      float currentValue,
      SceneEaseHandler sceneEase,
      boolean enabled) {
    Objects.requireNonNull(motionFinite, "property").requireSupported(n);
    motionFinite.validate(value);
    motionFinite.validate(currentValue);
    validate(sceneEase);
    if (Float.compare(value, currentValue) == 0) {
      cancel((N) n, motionFinite);
      motionFinite.set(n, value);
      return (N) n;
    } else {
      n.loop(motionFinite.compatibilityName(), value, currentValue, sceneEase, enabled);
      return (N) n;
    }
  }

  public static <N extends ScenePctService<?>> N cancel(N n, MotionFiniteService motionFinite) {
    Objects.requireNonNull(motionFinite, "property").requireSupported(n);
    n.cancelAnimation(motionFinite.compatibilityName());
    return (N) n;
  }

  public static <N extends ScenePctService<?>> N cancel(
      N n, MotionCompatibilityNameService motionCompatibilityName) {
    Objects.requireNonNull(motionCompatibilityName, "property").requireSupported(n);
    int value = motionCompatibilityName.get(n);
    n.animateColor(
        motionCompatibilityName.compatibilityName(),
        value,
        SceneEaseHandler.Tween.linear(1.0E-6F),
        0.0F,
        null);
    return (N) n;
  }

  public static void validate(SceneEaseHandler sceneEase) {
    Objects.requireNonNull(sceneEase, "animation");
    if (sceneEase instanceof SceneEaseHandler.Tween currentTween) {
      if (!Float.isFinite(currentTween.duration()) || currentTween.duration() <= 0.0F) {
        throw new IllegalArgumentException("Tween duration must be finite and greater than zero");
      }

      Objects.requireNonNull(currentTween.easing(), "tween.easing");
    } else if (sceneEase instanceof SceneEaseHandler.Spring spring) {
      if (!Float.isFinite(spring.damping()) || spring.damping() <= 0.0F) {
        throw new IllegalArgumentException("Spring damping must be finite and greater than zero");
      }

      if (!Float.isFinite(spring.stiffness()) || spring.stiffness() <= 0.0F) {
        throw new IllegalArgumentException("Spring stiffness must be finite and greater than zero");
      }
    }
  }

  private static void updateState(
      ScenePctService<?> scenePct,
      MotionFiniteService motionFinite,
      float value,
      SceneEaseHandler sceneEase,
      float currentValue) {
    Objects.requireNonNull(motionFinite, "property").requireSupported(scenePct);
    motionFinite.validate(value);
    validate(sceneEase);
    updateState2(currentValue);
  }

  private static void updateState2(float value) {
    if (!Float.isFinite(value) || value < 0.0F) {
      throw new IllegalArgumentException(
          "Animation delay must be finite and greater than or equal to zero");
    }
  }
}
