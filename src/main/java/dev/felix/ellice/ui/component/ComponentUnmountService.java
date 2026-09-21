package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.Objects;

public final class ComponentUnmountService {
   private ComponentUnmountService() {
   }

   public static LayoutContainerNode mountCentered(ScenePctService<?> scenePct, ScenePctService<?> currentScenePct, float value, Runnable runnable) {
      Objects.requireNonNull(scenePct, "root");
      Objects.requireNonNull(currentScenePct, "content");
      if (Float.isFinite(value) && !(value < 0.0F)) {
         LayoutContainerNode currentSize = new LayoutContainerNode()
            .absolute()
            .inset(LayoutOperationHandler.px(0.0F))
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
            .direction(ScenePctService.Direction.NONE)
            .layerBreak(true);
         SceneCornerRadiusService nextSize = new SceneCornerRadiusService()
            .absolute()
            .inset(LayoutOperationHandler.px(0.0F))
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
            .backgroundColor(0);
         if (runnable != null) {
            nextSize.interactive(true).onClick(runnable);
         }

         currentSize.addChild(nextSize);
         LayoutContainerNode previousSize = new LayoutContainerNode()
            .absolute()
            .inset(LayoutOperationHandler.px(0.0F))
            .size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())
            .padding(value)
            .direction(ScenePctService.Direction.ROW)
            .align(ScenePctService.Align.CENTER)
            .justify(ScenePctService.Justify.CENTER);
         previousSize.addChild(currentScenePct);
         currentSize.addChild(previousSize);
         scenePct.addChild(currentSize);
         return currentSize;
      } else {
         throw new IllegalArgumentException("viewportPadding must be finite and >= 0: " + value);
      }
   }

   public static void unmount(ScenePctService<?> scenePct, LayoutContainerNode layoutContainerNode) {
      if (scenePct != null && layoutContainerNode != null) {
         scenePct.removeChild(layoutContainerNode);
      }
   }
}
