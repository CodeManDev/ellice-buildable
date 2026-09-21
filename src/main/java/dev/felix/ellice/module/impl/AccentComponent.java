package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.security.ElliceFuse;
import dev.felix.ellice.ui.theme.ThemeIsSetService;

public final class AccentComponent extends ModuleSettingsService {
   private static final int DEFAULT_ACCENT_COLOR = -10262799;
   private static final int DEFAULT_SURFACE_COLOR = 1343032600;
   private final ModuleSetting.Color accentColor = this.setting(
      new ModuleSetting.Color("Accent", DEFAULT_ACCENT_COLOR)
         .description("Sets the client-wide interactive accent; hover and pressed shades are derived automatically.")
   );
   private final ModuleSetting.Color surfaceColor = this.setting(
      new ModuleSetting.Color("Surface", DEFAULT_SURFACE_COLOR)
         .description("Sets the shared UI surface tint and alpha used by themed panels.")
   );
   private final ModuleSetting.Number blurRadius = this.setting(
      new ModuleSetting.Number("Blur", 16.0F, 0.0F, 32.0F, 1.0F)
         .description("Sets the shared backdrop-blur radius for themed UI surfaces; 0 keeps them sharp.")
   );

   public AccentComponent() {
      super(
         ModuleBuilderData.builder("Overlay Theme")
            .description("Controls the accent, surface tint, and blur used by ellice overlays.")
            .category(ModuleFeatureType.CLIENT)
            .build()
      );
      this.accentColor.onChange(ignored -> this.applyThemeWhenReady());
      this.surfaceColor.onChange(ignored -> this.applyThemeWhenReady());
      this.blurRadius.onChange(ignored -> this.applyThemeWhenReady());
   }

   @Override
   protected void onEnable() {
      this.applyTheme();
   }

   @Override
   protected void onDisable() {
      ThemeIsSetService theme = CoreIsInitializedHandler.get().theme();
      theme.setColor("accent", DEFAULT_ACCENT_COLOR);
      theme.setColor("accent-hover", -8286984);
      theme.setColor("accent-press", -11581723);
      theme.setColor("slider.fill", DEFAULT_ACCENT_COLOR);
      theme.setColor("toggle.active", DEFAULT_ACCENT_COLOR);
      theme.setColor("textinput.border-focus", DEFAULT_ACCENT_COLOR);
      theme.setColor("keybind.active", DEFAULT_ACCENT_COLOR);
      theme.setColor("surface", DEFAULT_SURFACE_COLOR);
      theme.set("blur", "16");
      this.notifyThemeChanged();
   }

   private void applyTheme() {
      ThemeIsSetService theme = CoreIsInitializedHandler.get().theme();
      int accent = this.accentColor.get();
      theme.setColor("accent", accent);
      theme.setColor("accent-hover", adjustRgb(accent, 28));
      theme.setColor("accent-press", adjustRgb(accent, -18));
      theme.setColor("slider.fill", accent);
      theme.setColor("toggle.active", accent);
      theme.setColor("textinput.border-focus", accent);
      theme.setColor("keybind.active", accent);
      theme.setColor("surface", this.surfaceColor.get());
      theme.set("blur", String.valueOf(this.blurRadius.get()));
      this.notifyThemeChanged();
   }

   private void applyThemeWhenReady() {
      if (CoreIsInitializedHandler.isReady()) {
         this.applyTheme();
      }
   }

   private void notifyThemeChanged() {
      if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().pluginLoader() != null) {
         CoreIsInitializedHandler.get().pluginLoader().fireEvent("theme.changed");
      }
   }

   @ElliceFuse
   private static int adjustRgb(int color, int offset) {
      int alpha = color >>> 24 & 255;
      int red = clampChannel((color >>> 16 & 255) + offset);
      int green = clampChannel((color >>> 8 & 255) + offset);
      int blue = clampChannel((color & 255) + offset);
      return alpha << 24 | red << 16 | green << 8 | blue;
   }

   @ElliceFuse
   private static int clampChannel(int value) {
      return Math.max(0, Math.min(255, value));
   }
}
