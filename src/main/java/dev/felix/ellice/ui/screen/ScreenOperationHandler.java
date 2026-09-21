package dev.felix.ellice.ui.screen;

import dev.felix.ellice.ui.scene.SceneCodec;
import dev.felix.ellice.ui.scene.ScenePctService;

public interface ScreenOperationHandler {
  String id();

  ScenePctService<?> build(ScreenScreenIdService screenScreenId);

  default SceneCodec.Preset transitionPreset() {
    return SceneCodec.Preset.MODAL;
  }

  default SceneCodec.Options transitionOptions() {
    return new SceneCodec.Options();
  }

  default boolean closesOnEscape() {
    return true;
  }

  default float backgroundDesaturation() {
    return 0.42F;
  }

  default boolean overlaysPreviousScreen() {
    return false;
  }

  default boolean opensAsWindow() {
    return false;
  }

  default void onSuspend(ScreenScreenIdService screenScreenId) {}

  default void onOpen(ScreenScreenIdService screenScreenId) {}

  default void onClose(ScreenScreenIdService screenScreenId) {}

  default void tick(ScreenScreenIdService screenScreenId) {}

  default boolean keyPressed(ScreenScreenIdService screenScreenId, int value, int currentValue) {
    return false;
  }
}
