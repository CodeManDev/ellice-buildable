package dev.felix.ellice.ui.material;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.util.ArrayList;
import java.util.List;

public final class MaterialKeyService {
  private ScenePctService<?> scenePctService;

  public boolean key(ScenePctService<?> scenePct, int value, int currentValue) {
    if (value == 258) {
      ArrayList arrayList = new ArrayList();
      updateState(scenePct, arrayList);
      if (arrayList.isEmpty()) {
        return true;
      }

      ScenePctService currentScenePct = this.scenePctService;

      for (ScenePctService nextScenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
        if (nextScenePct instanceof ControlLetterSpacingService controlLetterSpacing
            && controlLetterSpacing.focused()) {
          currentScenePct = nextScenePct;
        }
      }

      int index = arrayList.indexOf(currentScenePct);
      int currentIndex = (currentValue & 1) != 0 ? -1 : 1;
      this.clear(scenePct);
      this.scenePctService =
          (ScenePctService<?>)
              arrayList.get(
                  index < 0
                      ? (currentIndex < 0 ? arrayList.size() - 1 : 0)
                      : Math.floorMod(index + currentIndex, arrayList.size()));
      if (this.scenePctService instanceof MaterialJoinedService materialJoined) {
        materialJoined.keyboardFocused(true);
      }

      if (this.scenePctService instanceof ControlLetterSpacingService currentControlLetterSpacing) {
        this.focus(currentControlLetterSpacing);
      }

      updateState2(this.scenePctService);
      return true;
    } else {
      if ((value == 257 || value == 32)
          && this.scenePctService instanceof MaterialJoinedService currentMaterialJoined
          && currentMaterialJoined.available()) {
        ArrayList currentArrayList = new ArrayList();
        updateState(scenePct, currentArrayList);
        if (currentArrayList.stream()
            .anyMatch(
                item ->
                    item instanceof ControlLetterSpacingService controlLetterSpacing
                        && controlLetterSpacing.focused())) {
          return false;
        }

        if (currentArrayList.contains(currentMaterialJoined)) {
          currentMaterialJoined.activate();
          return true;
        }
      }

      return false;
    }
  }

  public void clear(ScenePctService<?> scenePct) {
    if (this.scenePctService instanceof MaterialJoinedService materialJoined) {
      materialJoined.keyboardFocused(false);
    }

    this.scenePctService = null;
    ArrayList arrayList = new ArrayList();
    updateState(scenePct, arrayList);

    for (ScenePctService currentScenePct : (Iterable<ScenePctService>) (Iterable<?>) (arrayList)) {
      if (currentScenePct instanceof ControlLetterSpacingService controlLetterSpacing) {
        controlLetterSpacing.unfocus();
      }
    }

    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
      CoreIsInitializedHandler.get().scene().clearFocus();
    }
  }

  public void focus(ControlLetterSpacingService controlLetterSpacing) {
    if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().scene() != null) {
      CoreIsInitializedHandler.get().scene().setFocusedInput(controlLetterSpacing);
    } else {
      controlLetterSpacing.focus();
    }

    this.scenePctService = controlLetterSpacing;
    updateState2(controlLetterSpacing);
  }

  private static void updateState(ScenePctService<?> scenePct, List<ScenePctService<?>> items) {
    if (scenePct != null && scenePct.visible && scenePct.pointerEvents()) {
      if (scenePct instanceof MaterialJoinedService materialJoined && materialJoined.available()
          || scenePct instanceof ControlLetterSpacingService) {
        items.add(scenePct);
      }

      for (ScenePctService currentScenePct : scenePct.children()) {
        updateState(currentScenePct, items);
      }
    }
  }

  private static void updateState2(ScenePctService<?> scenePct) {
    for (ScenePctService currentScenePct = scenePct.parent();
        currentScenePct != null;
        currentScenePct = currentScenePct.parent()) {
      if (!(currentScenePct.scrollMin() >= 0.0F)) {
        float value =
            scenePct.computedY() < currentScenePct.computedY() + 6.0F
                ? currentScenePct.computedY() + 6.0F - scenePct.computedY()
                : Math.min(
                    0.0F,
                    currentScenePct.computedY()
                        + currentScenePct.computedH()
                        - 6.0F
                        - scenePct.computedY()
                        - scenePct.computedH());
        if (value != 0.0F) {
          float currentValue =
              Math.max(
                  currentScenePct.scrollMin(), Math.min(0.0F, currentScenePct.scrollY() + value));
          currentScenePct.scrollTarget(currentValue);
          MotionAnimateService.animate(
              currentScenePct,
              MotionColorsContainer.Floats.SCROLL_Y,
              currentValue,
              MaterialIsLightService.FAST_SPATIAL);
        }
      }
    }
  }
}
