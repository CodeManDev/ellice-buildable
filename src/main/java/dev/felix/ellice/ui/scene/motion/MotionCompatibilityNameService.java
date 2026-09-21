package dev.felix.ellice.ui.scene.motion;

import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.Objects;
import java.util.function.Predicate;

public final class MotionCompatibilityNameService {
  private final String text;
  private final Predicate<ScenePctService<?>> predicate;

  private MotionCompatibilityNameService(
      String currentText, Predicate<ScenePctService<?>> currentPredicate) {
    if (currentText != null && !currentText.isBlank()) {
      this.text = currentText;
      this.predicate = Objects.requireNonNull(currentPredicate, "supportedBy");
    } else {
      throw new IllegalArgumentException("Property name must not be blank");
    }
  }

  public static MotionCompatibilityNameService of(
      String text, Predicate<ScenePctService<?>> predicate) {
    return new MotionCompatibilityNameService(text, predicate);
  }

  public String compatibilityName() {
    return this.text;
  }

  public boolean supports(ScenePctService<?> scenePct) {
    return scenePct != null && this.predicate.test(scenePct);
  }

  public int get(ScenePctService<?> scenePct) {
    this.requireSupported(scenePct);
    return scenePct.getColorProperty(this.text);
  }

  public void set(ScenePctService<?> scenePct, int value) {
    this.requireSupported(scenePct);
    scenePct.setColorProperty(this.text, value);
    scenePct.invalidate();
  }

  void requireSupported(ScenePctService<?> scenePct) {
    Objects.requireNonNull(scenePct, "node");
    if (!this.predicate.test(scenePct)) {
      throw new IllegalArgumentException(
          "Color property '"
              + this.text
              + "' is not supported by "
              + scenePct.getClass().getSimpleName());
    }
  }

  @Override
  public String toString() {
    return "ColorProperty[" + this.text + "]";
  }
}
