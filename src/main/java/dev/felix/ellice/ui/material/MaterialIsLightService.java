package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.scene.SceneEaseHandler;
import dev.felix.ellice.ui.text.TextData;

public final class MaterialIsLightService {
   public static int SURFACE = -15461864;
   public static int SURFACE_LOWEST = -15790829;
   public static int SURFACE_LOW = -14869728;
   public static int SURFACE_CONTAINER = -14606554;
   public static int SURFACE_HIGH = -13948624;
   public static int SURFACE_HIGHEST = -13224901;
   public static int PRIMARY = -3097345;
   public static int ON_PRIMARY = -13099406;
   public static int PRIMARY_CONTAINER = -11585653;
   public static int ON_PRIMARY_CONTAINER = -1384961;
   public static int SECONDARY = -3357988;
   public static int ON_SECONDARY = -13423295;
   public static int SECONDARY_CONTAINER = -11910056;
   public static int ON_SECONDARY_CONTAINER = -1515784;
   public static int TERTIARY = -1066808;
   public static int ON_SURFACE = -1646359;
   public static int ON_SURFACE_VARIANT = -3488560;
   public static int OUTLINE = -7106663;
   public static int OUTLINE_VARIANT = -11975345;
   public static int ERROR = -870219;
   public static int ERROR_CONTAINER = -7143414;
   public static int ON_ERROR_CONTAINER = -9514;
   public static int INVERSE_SURFACE = -1646359;
   public static int INVERSE_ON_SURFACE = -13488331;
   public static int SELECTION = 1716537957;
   public static final String FONT = "material-roboto";
   public static final String MEDIUM = "material-roboto-medium";
   public static final TextData BODY = TextData.NONE.withFontFamily("material-roboto");
   public static final TextData LABEL = TextData.NONE.withFontFamily("material-roboto-medium");
   public static final float TOUCH = 48.0F;
   public static final float BUTTON = 40.0F;
   public static final float SWITCH_WIDTH = 52.0F;
   public static final float SWITCH_HEIGHT = 32.0F;
   public static final float SLIDER_TRACK = 16.0F;
   public static final float SLIDER_HANDLE = 44.0F;
   public static final float SLIDER_GAP = 6.0F;
   public static final SceneEaseHandler.Spring SPATIAL = createSpring(0.8F, 380.0F);
   public static final SceneEaseHandler.Spring FAST_SPATIAL = createSpring(0.6F, 800.0F);
   public static final SceneEaseHandler.Spring EFFECTS = createSpring(1.0F, 1600.0F);
   public static final SceneEaseHandler.Spring FAST_EFFECTS = createSpring(1.0F, 3800.0F);

   private MaterialIsLightService() {
   }

   public static boolean isLight() {
      return MaterialData.contrast(SURFACE, -16777216) > MaterialData.contrast(SURFACE, -1);
   }

   public static void apply(MaterialData materialData) {
      SURFACE = materialData.color("SURFACE");
      SURFACE_LOWEST = materialData.color("SURFACE_LOWEST");
      SURFACE_LOW = materialData.color("SURFACE_LOW");
      SURFACE_CONTAINER = materialData.color("SURFACE_CONTAINER");
      SURFACE_HIGH = materialData.color("SURFACE_HIGH");
      SURFACE_HIGHEST = materialData.color("SURFACE_HIGHEST");
      PRIMARY = materialData.color("PRIMARY");
      ON_PRIMARY = materialData.color("ON_PRIMARY");
      PRIMARY_CONTAINER = materialData.color("PRIMARY_CONTAINER");
      ON_PRIMARY_CONTAINER = materialData.color("ON_PRIMARY_CONTAINER");
      SECONDARY = materialData.color("SECONDARY");
      ON_SECONDARY = materialData.color("ON_SECONDARY");
      SECONDARY_CONTAINER = materialData.color("SECONDARY_CONTAINER");
      ON_SECONDARY_CONTAINER = materialData.color("ON_SECONDARY_CONTAINER");
      TERTIARY = materialData.color("TERTIARY");
      ON_SURFACE = materialData.color("ON_SURFACE");
      ON_SURFACE_VARIANT = materialData.color("ON_SURFACE_VARIANT");
      OUTLINE = materialData.color("OUTLINE");
      OUTLINE_VARIANT = materialData.color("OUTLINE_VARIANT");
      ERROR = materialData.color("ERROR");
      ERROR_CONTAINER = materialData.color("ERROR_CONTAINER");
      ON_ERROR_CONTAINER = materialData.color("ON_ERROR_CONTAINER");
      INVERSE_SURFACE = materialData.color("INVERSE_SURFACE");
      INVERSE_ON_SURFACE = materialData.color("INVERSE_ON_SURFACE");
      SELECTION = 1426063360 | PRIMARY & 16777215;
   }

   private static SceneEaseHandler.Spring createSpring(float value, float currentValue) {
      return new SceneEaseHandler.Spring(2.0F * value * (float)Math.sqrt(currentValue), currentValue);
   }

   public static int layer(int value, int currentValue, float nextValue) {
      int previousValue = Math.round((value >>> 16 & 0xFF) * (1.0F - nextValue) + (currentValue >>> 16 & 0xFF) * nextValue);
      int sourceValue = Math.round((value >>> 8 & 0xFF) * (1.0F - nextValue) + (currentValue >>> 8 & 0xFF) * nextValue);
      int targetValue = Math.round((value & 0xFF) * (1.0F - nextValue) + (currentValue & 0xFF) * nextValue);
      return value & 0xFF000000 | previousValue << 16 | sourceValue << 8 | targetValue;
   }
}
