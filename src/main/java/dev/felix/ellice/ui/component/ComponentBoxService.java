package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ComponentBoxService {
   private ComponentBoxService() {
   }

   public static ComponentKeyService<LayoutContainerNode> box(Consumer<LayoutContainerNode> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of("box", LayoutContainerNode::new, consumer, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> box(Consumer<LayoutContainerNode> consumer, Object[] components) {
      return box(consumer, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> row(Consumer<LayoutContainerNode> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of("row", LayoutContainerNode::new, item -> {
         item.direction(ScenePctService.Direction.ROW);
         if (consumer != null) {
            consumer.accept(item);
         }
      }, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> row(ComponentStyleService componentStyle, ComponentKeyService<?>... componentKeies) {
      return row(componentStyle::apply, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> row(Consumer<LayoutContainerNode> consumer, Object[] components) {
      return row(consumer, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> row(ComponentStyleService componentStyle, Object[] components) {
      return row(componentStyle, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> column(Consumer<LayoutContainerNode> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of("column", LayoutContainerNode::new, item -> {
         item.direction(ScenePctService.Direction.COLUMN);
         if (consumer != null) {
            consumer.accept(item);
         }
      }, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> column(ComponentStyleService componentStyle, ComponentKeyService<?>... componentKeies) {
      return column(componentStyle::apply, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> column(Consumer<LayoutContainerNode> consumer, Object[] components) {
      return column(consumer, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> column(ComponentStyleService componentStyle, Object[] components) {
      return column(componentStyle, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> stack(Consumer<LayoutContainerNode> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of("stack", LayoutContainerNode::new, item -> {
         item.direction(ScenePctService.Direction.NONE);
         if (consumer != null) {
            consumer.accept(item);
         }
      }, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> stack(ComponentStyleService componentStyle, ComponentKeyService<?>... componentKeies) {
      return stack(componentStyle::apply, componentKeies);
   }

   public static ComponentKeyService<LayoutContainerNode> stack(Consumer<LayoutContainerNode> consumer, Object[] components) {
      return stack(consumer, componentKeys(components));
   }

   public static ComponentKeyService<LayoutContainerNode> stack(ComponentStyleService componentStyle, Object[] components) {
      return stack(componentStyle, componentKeys(components));
   }

   public static ComponentKeyService<SceneCornerRadiusService> panel(Consumer<SceneCornerRadiusService> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of("panel", SceneCornerRadiusService::new, consumer, componentKeies);
   }

   public static ComponentKeyService<SceneCornerRadiusService> panel(ComponentStyleService componentStyle, Consumer<SceneCornerRadiusService> consumer, ComponentKeyService<?>... componentKeies) {
      return panel(item -> {
         componentStyle.apply(item);
         if (consumer != null) {
            consumer.accept(item);
         }
      }, componentKeies);
   }

   public static ComponentKeyService<SceneCornerRadiusService> panel(Consumer<SceneCornerRadiusService> consumer, Object[] components) {
      return panel(consumer, componentKeys(components));
   }

   public static ComponentKeyService<SceneCornerRadiusService> panel(ComponentStyleService componentStyle, Consumer<SceneCornerRadiusService> consumer, Object[] components) {
      return panel(componentStyle, consumer, componentKeys(components));
   }

   public static ComponentKeyService<SceneTextService> text(String currentText, Consumer<SceneTextService> consumer) {
      return ComponentKeyService.of("text", SceneTextService::new, item -> {
         item.text(currentText != null ? currentText : "");
         if (consumer != null) {
            consumer.accept(item);
         }
      });
   }

   public static ComponentKeyService<SceneSrcService> svg(Path path, Consumer<SceneSrcService> consumer) {
      return ComponentKeyService.of("svg", SceneSrcService::new, item -> {
         item.src(path);
         if (consumer != null) {
            consumer.accept(item);
         }
      });
   }

   public static ComponentKeyService<LayoutContainerNode> spacer() {
      return box(item -> item.flex(1.0F));
   }

   public static <N extends ScenePctService<?>> ComponentKeyService<N> node(String text, Supplier<N> supplier, Consumer<N> consumer, ComponentKeyService<?>... componentKeies) {
      return ComponentKeyService.of(text, supplier, consumer, componentKeies);
   }

   public static <N extends ScenePctService<?>> ComponentKeyService<N> node(String text, Supplier<N> supplier, Consumer<N> consumer, Object[] components) {
      return node(text, supplier, consumer, componentKeys(components));
   }

   private static ComponentKeyService<?>[] componentKeys(Object[] components) {
      ComponentKeyService<?>[] keys = new ComponentKeyService<?>[components.length];
      for (int index = 0; index < components.length; index++) {
         keys[index] = (ComponentKeyService<?>) components[index];
      }
      return keys;
   }
}
