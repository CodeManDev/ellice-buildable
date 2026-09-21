package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;

public final class MaterialTextService {
   private static final Map<String, Path> text2 = new HashMap<>();

   private MaterialTextService() {
   }

   public static ComponentKeyService<SceneTextService> text(String currentText, float value, int currentValue) {
      return ComponentBoxService.text(
         currentText,
         item -> item.fontFamily("material-roboto")
            .fontSize(value)
            .lineHeight(
               value <= 12.0F
                  ? 16.0F
                  : (
                     value <= 14.0F
                        ? 20.0F
                        : (
                           value <= 16.0F
                              ? 24.0F
                              : (
                                 value <= 22.0F
                                    ? 28.0F
                                    : 32.0F
                              )
                        )
                  )
            )
            .letterSpacing(
               value <= 12.0F
                  ? 0.4F
                  : (
                     value <= 14.0F
                        ? 0.2F
                        : (value <= 16.0F ? 0.5F : 0.0F)
                  )
            )
            .color(currentValue)
            .overflow(SceneTextService.Overflow.ELLIPSIS)
            .inheritEdgeSoftness(false)
            .pointerEvents(false)
      );
   }

   public static ComponentKeyService<SceneTextService> label(String currentText, int value) {
      return text(currentText, 14.0F, value)
         .props(item -> item.fontFamily("material-roboto-medium").letterSpacing(0.1F));
   }

   public static ComponentKeyService<SceneSrcService> icon(String text, int value) {
      return ComponentBoxService.svg(
         iconPath(text),
         item -> item.size(24.0F, 24.0F)
            .flexShrink(0.0F)
            .tintColor(value)
            .inheritEdgeSoftness(false)
            .pointerEvents(false)
      );
   }

   public static Path iconPath(String path) {
      return text2.computeIfAbsent(
         path,
         iconName -> {
            String resourcePath = "assets/ellice/components/icons/material/" + iconName + ".svg";
            return FabricLoader.getInstance()
               .getModContainer("ellice")
               .flatMap(container -> container.findPath(resourcePath))
               .orElse(Path.of("src/main/resources", resourcePath));
         }
      );
   }

   public static ComponentKeyService<MaterialJoinedService> button(String text, String currentText, Runnable runnable, boolean enabled) {
      return button(
         text,
         currentText,
         runnable,
         enabled ? MaterialIsLightService.PRIMARY : MaterialIsLightService.SECONDARY_CONTAINER,
         enabled ? MaterialIsLightService.ON_PRIMARY : MaterialIsLightService.ON_SECONDARY_CONTAINER,
         20.0F
      );
   }

   public static ComponentKeyService<MaterialJoinedService> button(String text, String currentText, Runnable runnable, int value, int currentValue, float nextValue) {
      return ComponentBoxService.<MaterialJoinedService>node(
            "material-button",
            MaterialJoinedService::new,
            item -> {
               item.colors(value, currentValue).shape(nextValue, 40.0F).surfaceWidth(0.0F);
               item.id(text)
                  .width(LayoutOperationHandler.auto())
                  .height(LayoutOperationHandler.px(48.0F))
                  .minWidth(48.0F)
                  .padding(0.0F, 16.0F)
                  .gap(8.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .flexShrink(0.0F)
                  .onClick(runnable);
            },
            label(currentText, currentValue)
         )
         .key(text);
   }

   public static ComponentKeyService<MaterialJoinedService> iconButton(String text, String currentText, String nextText, Runnable runnable) {
      return ComponentBoxService.<MaterialJoinedService>node(
            "material-button",
            MaterialJoinedService::new,
            item -> {
               item.colors(0, MaterialIsLightService.ON_SURFACE_VARIANT)
                  .shape(20.0F, 40.0F)
                  .surfaceWidth(40.0F);
               item.id(text)
                  .size(48.0F, 48.0F)
                  .padding(0.0F)
                  .gap(0.0F)
                  .flexShrink(0.0F)
                  .direction(ScenePctService.Direction.ROW)
                  .align(ScenePctService.Align.CENTER)
                  .justify(ScenePctService.Justify.CENTER)
                  .tooltip(nextText)
                  .onClick(runnable);
            },
            icon(currentText, MaterialIsLightService.ON_SURFACE_VARIANT)
         )
         .key(text);
   }

   public static ComponentKeyService<SceneCornerRadiusService> divider() {
      return ComponentBoxService.panel(
         item -> item.size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(1.0F))
            .backgroundColor(MaterialIsLightService.OUTLINE_VARIANT)
            .flexShrink(0.0F)
            .pointerEvents(false)
      );
   }

   public static void input(ControlLetterSpacingService controlLetterSpacing) {
      controlLetterSpacing.fontFamily("material-roboto")
         .fontSize(16.0F)
         .letterSpacing(0.5F)
         .cornerRadius(4.0F)
         .bgColor(MaterialIsLightService.SURFACE_HIGHEST)
         .textColor(MaterialIsLightService.ON_SURFACE)
         .placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT)
         .focusBorder(MaterialIsLightService.PRIMARY)
         .selectionColor(MaterialIsLightService.SELECTION)
         .material(true);
   }

   public static ComponentKeyService<InteractiveSurfacePanel> search(String text, String currentText, String nextText, Consumer<String> consumer) {
      return search(text, currentText, nextText, consumer, false);
   }

   public static ComponentKeyService<InteractiveSurfacePanel> search(String currentText, String nextText, String previousText, Consumer<String> consumer, boolean enabled) {
      return ComponentBoxService.<InteractiveSurfacePanel>node(
            "material-search-bar",
            InteractiveSurfacePanel::new,
            item -> item.height(LayoutOperationHandler.px(enabled ? 40.0F : 56.0F))
               .minWidth(0.0F)
               .cornerRadius(28.0F)
               .flexShrink(0.0F)
               .direction(ScenePctService.Direction.ROW)
               .align(ScenePctService.Align.CENTER)
               .padding(0.0F, 8.0F),
            icon("search", MaterialIsLightService.ON_SURFACE).props(item -> item.margin(0.0F, 8.0F)),
            ComponentBoxService.<ControlLetterSpacingService>node(
                  "material-search",
                  ControlLetterSpacingService::new,
                  item -> {
                     item.id(currentText)
                        .materialSearch(true)
                        .fontFamily("material-roboto")
                        .fontSize(enabled ? 14.0F : 16.0F)
                        .letterSpacing(0.5F)
                        .placeholder(nextText)
                        .maxLength(100)
                        .bgColor(0)
                        .textColor(MaterialIsLightService.ON_SURFACE)
                        .placeholderColor(MaterialIsLightService.ON_SURFACE_VARIANT)
                        .focusBorder(0)
                        .selectionColor(MaterialIsLightService.SELECTION)
                        .cornerRadius(0.0F)
                        .height(LayoutOperationHandler.px(enabled ? 32.0F : 48.0F))
                        .flex(1.0F)
                        .minWidth(0.0F)
                        .stopPropagation(true)
                        .onChanged(consumer);
                     if (!item.focused()) {
                        item.text(previousText);
                     }
                  }
               )
               .key(currentText),
            ComponentBoxService.<MaterialJoinedService>node(
                  "material-search-clear",
                  MaterialJoinedService::new,
                  item -> {
                     item.colors(0, MaterialIsLightService.ON_SURFACE_VARIANT)
                        .shape(20.0F, 40.0F)
                        .surfaceWidth(40.0F);
                     item.id(currentText + ".clear")
                        .size(
                           enabled ? 32.0F : 48.0F,
                           enabled ? 32.0F : 48.0F
                        )
                        .flexShrink(0.0F)
                        .direction(ScenePctService.Direction.ROW)
                        .align(ScenePctService.Align.CENTER)
                        .justify(ScenePctService.Justify.CENTER)
                        .visible(!previousText.isEmpty())
                        .tooltip("Clear search")
                        .onClick(() -> {
                           if (item.parent() != null && item.parent().findById(currentText) instanceof ControlLetterSpacingService controlLetterSpacing) {
                              controlLetterSpacing.text("");
                           }

                           consumer.accept("");
                        });
                  },
                  icon("close", MaterialIsLightService.ON_SURFACE_VARIANT)
               )
               .key(currentText + ".clear")
         )
         .key(currentText + ".bar");
   }
}
