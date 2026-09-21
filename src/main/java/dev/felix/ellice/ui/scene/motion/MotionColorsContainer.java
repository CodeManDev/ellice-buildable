package dev.felix.ellice.ui.scene.motion;

import dev.felix.ellice.ui.scene.SceneTextureService;
import dev.felix.ellice.ui.scene.SceneSrcService;
import dev.felix.ellice.ui.scene.SceneColorService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.MaterialSurfaceNode;
import dev.felix.ellice.ui.scene.TooltipBubbleNode;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.SceneSourceService;
import dev.felix.ellice.ui.scene.SelectionIndicatorNode;
import java.util.function.Predicate;

public final class MotionColorsContainer {
   private static final Predicate<ScenePctService<?>> predicate = item -> true;
   private static final Predicate<ScenePctService<?>> predicate2 = item -> item instanceof SceneCornerRadiusService;
   private static final Predicate<ScenePctService<?>> predicate3 = item -> item instanceof TooltipBubbleNode;
   private static final Predicate<ScenePctService<?>> predicate4 = item -> item instanceof SceneCornerRadiusService
      || item instanceof MaterialSurfaceNode
      || item instanceof SceneTextureService
      || item instanceof SceneSourceService
      || item instanceof SceneColorService
      || item instanceof SelectionIndicatorNode
      || item instanceof TooltipBubbleNode;
   private static final Predicate<ScenePctService<?>> predicate5 = item -> item instanceof SceneCornerRadiusService
      || item instanceof MaterialSurfaceNode
      || item instanceof SceneTextureService
      || item instanceof SceneSourceService
      || item instanceof TooltipBubbleNode;

   private MotionColorsContainer() {
   }

   private static MotionFiniteService createMotionFiniteService(String text, Predicate<ScenePctService<?>> predicate) {
      return MotionFiniteService.finite(text, predicate);
   }

   private static MotionFiniteService createMotionFiniteService2(String text, Predicate<ScenePctService<?>> predicate) {
      return MotionFiniteService.of(text, predicate, item -> Float.isFinite(item) && item >= 0.0F, "a finite value greater than or equal to zero");
   }

   private static MotionFiniteService createMotionFiniteService3(String text, Predicate<ScenePctService<?>> predicate, float currentValue, float nextValue) {
      return MotionFiniteService.of(
         text, predicate, item -> Float.isFinite(item) && item >= currentValue && item <= nextValue, "a finite value in [" + currentValue + ", " + nextValue + "]"
      );
   }

   private static MotionCompatibilityNameService createMotionCompatibilityNameService(String name) {
      return MotionCompatibilityNameService.of(name, item -> item instanceof SceneColorService);
   }

   public static final class Colors {
      public static final MotionCompatibilityNameService BACKGROUND = MotionCompatibilityNameService.of("backgroundColor", MotionColorsContainer.predicate2);
      public static final MotionCompatibilityNameService BORDER = MotionCompatibilityNameService.of(
         "borderColor", item -> MotionColorsContainer.predicate2.test(item) || MotionColorsContainer.predicate3.test(item)
      );
      public static final MotionCompatibilityNameService BORDER_SECONDARY = MotionCompatibilityNameService.of("secondaryBorderColor", MotionColorsContainer.predicate2);
      public static final MotionCompatibilityNameService SHADOW = MotionCompatibilityNameService.of(
         "shadowColor", item -> MotionColorsContainer.predicate2.test(item) || MotionColorsContainer.predicate3.test(item)
      );
      public static final MotionCompatibilityNameService HOVER_BACKGROUND = MotionCompatibilityNameService.of("hoverBackground", MotionColorsContainer.predicate2);
      public static final MotionCompatibilityNameService GRADIENT_END = MotionCompatibilityNameService.of(
         "gradientEnd", item -> MotionColorsContainer.predicate2.test(item) || MotionColorsContainer.predicate3.test(item)
      );
      public static final MotionCompatibilityNameService GLASS_HIGHLIGHT = MotionCompatibilityNameService.of("glassHighlightColor", MotionColorsContainer.predicate2);
      public static final MotionCompatibilityNameService FOREGROUND = MotionCompatibilityNameService.of(
         "color",
         item -> item instanceof SceneTextService
            || item instanceof SceneSrcService
            || item instanceof MaterialSurfaceNode
            || item instanceof TooltipBubbleNode
            || item instanceof SelectionIndicatorNode
      );
      public static final MotionCompatibilityNameService TINT = MotionCompatibilityNameService.of(
         "tintColor", item -> item instanceof SceneSrcService || item instanceof MaterialSurfaceNode
      );
      public static final MotionCompatibilityNameService ACCENT = MotionCompatibilityNameService.of("accentColor", item -> item instanceof SelectionIndicatorNode);
      public static final MotionCompatibilityNameService MESH_0 = MotionColorsContainer.createMotionCompatibilityNameService("color0");
      public static final MotionCompatibilityNameService MESH_1 = MotionColorsContainer.createMotionCompatibilityNameService("color1");
      public static final MotionCompatibilityNameService MESH_2 = MotionColorsContainer.createMotionCompatibilityNameService("color2");
      public static final MotionCompatibilityNameService MESH_3 = MotionColorsContainer.createMotionCompatibilityNameService("color3");
      public static final MotionCompatibilityNameService MESH_4 = MotionColorsContainer.createMotionCompatibilityNameService("color4");

      private Colors() {
      }
   }

   public static final class Floats {
      public static final MotionFiniteService X = MotionColorsContainer.createMotionFiniteService("x", MotionColorsContainer.predicate);
      public static final MotionFiniteService Y = MotionColorsContainer.createMotionFiniteService("y", MotionColorsContainer.predicate);
      public static final MotionFiniteService TRANSLATE_X = MotionColorsContainer.createMotionFiniteService("translateX", MotionColorsContainer.predicate);
      public static final MotionFiniteService TRANSLATE_Y = MotionColorsContainer.createMotionFiniteService("translateY", MotionColorsContainer.predicate);
      public static final MotionFiniteService WIDTH = MotionColorsContainer.createMotionFiniteService2("width", MotionColorsContainer.predicate);
      public static final MotionFiniteService HEIGHT = MotionColorsContainer.createMotionFiniteService2("height", MotionColorsContainer.predicate);
      public static final MotionFiniteService OPACITY = MotionColorsContainer.createMotionFiniteService3("opacity", MotionColorsContainer.predicate, 0.0F, 1.0F);
      public static final MotionFiniteService SCALE = MotionColorsContainer.createMotionFiniteService2("scale", MotionColorsContainer.predicate);
      public static final MotionFiniteService SCROLL_Y = MotionColorsContainer.createMotionFiniteService("scrollY", MotionColorsContainer.predicate);
      public static final MotionFiniteService PADDING = MotionColorsContainer.createMotionFiniteService2("padding", MotionColorsContainer.predicate);
      public static final MotionFiniteService PADDING_TOP = MotionColorsContainer.createMotionFiniteService2("paddingTop", MotionColorsContainer.predicate);
      public static final MotionFiniteService PADDING_RIGHT = MotionColorsContainer.createMotionFiniteService2("paddingRight", MotionColorsContainer.predicate);
      public static final MotionFiniteService PADDING_BOTTOM = MotionColorsContainer.createMotionFiniteService2("paddingBottom", MotionColorsContainer.predicate);
      public static final MotionFiniteService PADDING_LEFT = MotionColorsContainer.createMotionFiniteService2("paddingLeft", MotionColorsContainer.predicate);
      public static final MotionFiniteService MARGIN = MotionColorsContainer.createMotionFiniteService("margin", MotionColorsContainer.predicate);
      public static final MotionFiniteService MARGIN_TOP = MotionColorsContainer.createMotionFiniteService("marginTop", MotionColorsContainer.predicate);
      public static final MotionFiniteService MARGIN_RIGHT = MotionColorsContainer.createMotionFiniteService("marginRight", MotionColorsContainer.predicate);
      public static final MotionFiniteService MARGIN_BOTTOM = MotionColorsContainer.createMotionFiniteService("marginBottom", MotionColorsContainer.predicate);
      public static final MotionFiniteService MARGIN_LEFT = MotionColorsContainer.createMotionFiniteService("marginLeft", MotionColorsContainer.predicate);
      public static final MotionFiniteService GAP = MotionColorsContainer.createMotionFiniteService2("gap", MotionColorsContainer.predicate);
      public static final MotionFiniteService HOVER_PROGRESS = MotionColorsContainer.createMotionFiniteService3("hoverProgress", MotionColorsContainer.predicate, 0.0F, 1.0F);
      public static final MotionFiniteService PRESS_PROGRESS = MotionColorsContainer.createMotionFiniteService3("pressProgress", MotionColorsContainer.predicate, 0.0F, 1.0F);
      public static final MotionFiniteService CORNER_RADIUS = MotionColorsContainer.createMotionFiniteService2("cornerRadius", MotionColorsContainer.predicate4);
      public static final MotionFiniteService CORNER_RADIUS_TL = MotionColorsContainer.createMotionFiniteService2("cornerRadiusTL", MotionColorsContainer.predicate4);
      public static final MotionFiniteService CORNER_RADIUS_TR = MotionColorsContainer.createMotionFiniteService2("cornerRadiusTR", MotionColorsContainer.predicate4);
      public static final MotionFiniteService CORNER_RADIUS_BR = MotionColorsContainer.createMotionFiniteService2("cornerRadiusBR", MotionColorsContainer.predicate4);
      public static final MotionFiniteService CORNER_RADIUS_BL = MotionColorsContainer.createMotionFiniteService2("cornerRadiusBL", MotionColorsContainer.predicate4);
      public static final MotionFiniteService EDGE_SOFTNESS = MotionColorsContainer.createMotionFiniteService2("edgeSoftness", MotionColorsContainer.predicate5);
      public static final MotionFiniteService BLUR = MotionColorsContainer.createMotionFiniteService2(
         "blur", item -> item instanceof SceneCornerRadiusService || item instanceof MaterialSurfaceNode
      );
      public static final MotionFiniteService SHADOW = MotionColorsContainer.createMotionFiniteService2(
         "shadow", item -> MotionColorsContainer.predicate2.test(item) || MotionColorsContainer.predicate3.test(item)
      );
      public static final MotionFiniteService BORDER_WIDTH = MotionColorsContainer.createMotionFiniteService2(
         "borderWidth", item -> MotionColorsContainer.predicate2.test(item) || MotionColorsContainer.predicate3.test(item)
      );
      public static final MotionFiniteService ROTATION = MotionColorsContainer.createMotionFiniteService("rotation", item -> item instanceof SceneSrcService);

      private Floats() {
      }
   }
}
