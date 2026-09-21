package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import dev.felix.ellice.ui.scene.motion.MotionAnimateService;
import dev.felix.ellice.ui.scene.motion.MotionColorsContainer;

public final class MaterialFindByIdSelector extends LayoutContainerNode {
   @Override
   public ScenePctService<?> findById(String name) {
      return null;
   }

   public static ComponentKeyService<?> of(ComponentKeyService<?> componentKey, int value, long longValue, Runnable runnable) {
      return ComponentBoxService.<MaterialFindByIdSelector>node(
            "material-departure",
            MaterialFindByIdSelector::new,
            item -> item.absolute()
               .inset(LayoutOperationHandler.px(0.0F))
               .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.percent(100.0F))
               .pointerEvents(false)
               .clip(true),
            componentKey
         )
         .key("departing:" + longValue)
         .onMount(
            item -> {
               MotionAnimateService.animate(
                  item, MotionColorsContainer.Floats.TRANSLATE_X, -value * 16, SceneEaseHandler.Tween.ease(0.18F)
               );
               MotionAnimateService.animate(
                  item, MotionColorsContainer.Floats.OPACITY, 0.0F, SceneEaseHandler.Tween.ease(0.18F), 0.0F, runnable
               );
            }
         );
   }
}
