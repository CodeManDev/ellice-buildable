package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.ui.scene.SceneSrcService;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;

public final class BuiltinComponent {
   public static final int SURFACE = -99280092;
   static final int HEADER = -14998736;
   static final int CONTROL = -14537670;
   static final int HOVER = -13944759;
   static final int PRESS = -15195344;
   static final int BORDER = -13286826;
   public static final int BORDER_SUBTLE = -13945532;
   public static final int ACCENT = -8530948;
   static final int ACCENT_SURFACE = -14664624;
   static final int ACCENT_HOVER = -14005406;
   static final int ACCENT_BORDER = -12424317;
   public static final int TEXT = -985604;
   static final int SECONDARY = -5456944;
   static final int MUTED = -8154198;
   static final int DISABLED = -10127989;
   static final float PANEL_RADIUS = 12.0F;
   static final float CONTROL_RADIUS = 7.0F;
   private static final Map<String, Path> text = new HashMap<>();

   private BuiltinComponent() {
   }

   static Path iconPath(String path) {
      return text.computeIfAbsent(
         path,
         iconName -> {
            String resourcePath = "assets/ellice/components/icons/clickgui/" + iconName + ".svg";
            return FabricLoader.getInstance()
               .getModContainer("ellice")
               .flatMap(container -> container.findPath(resourcePath))
               .orElse(Path.of("src/main/resources", resourcePath));
         }
      );
   }

   static SceneSrcService icon(String text, float value, int currentValue) {
      return new SceneSrcService()
         .src(iconPath(text))
         .size(value, value)
         .flexShrink(0.0F)
         .tintColor(currentValue)
         .inheritEdgeSoftness(false)
         .pointerEvents(false);
   }

   static String categoryIcon(ModuleFeatureType moduleFeatureType) {
      return switch (moduleFeatureType) {
         case COMBAT -> "crosshair";
         case HUD -> "layout";
         case MOVEMENT -> "arrow";
         case PLAYER -> "layout";
         case VISUALS, CLIENT -> "sliders";
         case TOOLS, DEVELOPMENT -> "tool";
      };
   }
}
