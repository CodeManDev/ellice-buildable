package dev.felix.ellice.ui.scene;

import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public final class SceneSelectSelector<T> {
   private final LinkedHashMap<T, LayoutContainerNode> entries = new LinkedHashMap<>();
   private T t;

   public void select(T currentT, boolean enabled) {
      if (!enabled) {
         this.entries.clear();
         this.t = (T)currentT;
         if (currentT != null) {
            this.entries.put((T)currentT, new LayoutContainerNode());
         }
      } else if (!Objects.equals(this.t, currentT)) {
         this.t = (T)currentT;
         if (currentT != null) {
            this.entries.computeIfAbsent((T)currentT, item -> new LayoutContainerNode().opacity(0.0F));
         }

         this.entries
            .forEach(
               (item, currentItem) -> MotionAnimateService.animate(
                  currentItem,
                  MotionColorsContainer.Floats.OPACITY,
                  Objects.equals(item, currentT) ? 1.0F : 0.0F,
                  SceneEaseHandler.Tween.ease(Objects.equals(item, currentT) ? 0.16F : 0.12F)
               )
            );

         while (this.entries.size() > 3) {
            this.entries.remove(this.entries.keySet().iterator().next());
         }
      }
   }

   public List<SceneSelectSelector.Layer<T>> advance(float value) {
      this.entries.values().forEach(item -> item.tickAnimations(value));
      this.entries
         .entrySet()
         .removeIf(
            entry -> !Objects.equals(entry.getKey(), this.t)
               && MotionColorsContainer.Floats.OPACITY.get(entry.getValue()) < 0.002F
         );
      return this.entries
         .entrySet()
         .stream()
         .map(entry -> new SceneSelectSelector.Layer<>(entry.getKey(), MotionColorsContainer.Floats.OPACITY.get(entry.getValue())))
         .toList();
   }

   public void clear() {
      this.entries.clear();
      this.t = null;
   }

   public record Layer<T>(T target, float opacity) {
   }
}
