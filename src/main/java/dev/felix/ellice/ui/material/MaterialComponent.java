package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class MaterialComponent extends LayoutContainerNode {
  private final Map<ScenePctService<?>, Float> entries = new IdentityHashMap<>();
  private List<ScenePctService<?>> items = List.of();
  private float value = -1.0F;

  public MaterialComponent() {
    this.direction(ScenePctService.Direction.COLUMN);
    this.onLayout(this::updateState);
  }

  private void updateState() {
    List currentItems = this.children().stream().filter(item -> item.visible).toList();
    int currentValue = Math.abs(this.computedW() - this.value) > 0.5F ? 1 : 0;
    List nextItems = this.items.stream().filter(currentItems::contains).toList();
    List previousItems = currentItems.stream().filter(this.entries::containsKey).toList();
    boolean enabled = nextItems.equals(previousItems);

    for (ScenePctService scenePct : (Iterable<ScenePctService>) (Iterable<?>) (currentItems)) {
      float nextValue = scenePct.computedY() - this.computedY() - this.scrollY();
      Float previousValue = this.entries.get(scenePct);
      if (currentValue != 0 || !enabled) {
        MotionAnimateService.cancel(scenePct, MotionColorsContainer.Floats.TRANSLATE_Y);
        scenePct.translateY(0.0F);
      } else if (previousValue != null && Math.abs(previousValue - nextValue) > 0.5F) {
        float sourceValue =
            MotionColorsContainer.Floats.TRANSLATE_Y.get(scenePct) + previousValue - nextValue;
        MotionColorsContainer.Floats.TRANSLATE_Y.set(
            scenePct, Math.max(-160.0F, Math.min(160.0F, sourceValue)));
        MotionAnimateService.animate(
            scenePct, MotionColorsContainer.Floats.TRANSLATE_Y, 0.0F, MaterialEnterService.PAGE);
      }
    }

    this.entries.clear();

    for (ScenePctService currentScenePct :
        (Iterable<ScenePctService>) (Iterable<?>) (currentItems)) {
      this.entries.put(
          currentScenePct, currentScenePct.computedY() - this.computedY() - this.scrollY());
    }

    this.items = currentItems;
    this.value = this.computedW();
  }

  @Override
  protected void onDetached() {
    this.entries.clear();
    this.items = List.of();
    this.value = -1.0F;
  }
}
