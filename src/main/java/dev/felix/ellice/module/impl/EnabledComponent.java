package dev.felix.ellice.module.impl;

import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;

public final class EnabledComponent extends ModuleSettingsService {
   private final ModuleSetting.Bool moduleBool = this.setting(
      new ModuleSetting.Bool("Enabled", true).description("Tests the boolean toggle control.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      new ModuleSetting.Number("Strength", 0.5F, 0.0F, 1.0F, 0.05F)
         .description("Tests a continuous 0–1 slider.")
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      new ModuleSetting.Number("Steps", 4.0F, 0.0F, 10.0F, 1.0F)
         .description("Tests a whole-number stepped slider.")
   );
   private final ModuleSetting.Mode moduleMode = this.setting(
      new ModuleSetting.Mode("Mode", new String[]{"Static", "Smooth", "Snap"}, "Smooth").description("Tests a three-option mode selector.")
   );
   private final ModuleSetting.Color moduleColor = this.setting(
      new ModuleSetting.Color("Tint", -1292135).description("Tests the ARGB color picker.")
   );
   private final ModuleSetting.Curve moduleCurve = this.setting(
      new ModuleSetting.Curve(
            "Timing curve",
            ModuleSetting.CurveValue.cubicBezier(
               0.25F, 0.1F, 0.25F, 1.0F
            )
         )
         .description("Tests linear, Bézier, and stepped curve editing.")
   );
   private final ModuleSetting.Keybind moduleKeybind = this.setting(
      new ModuleSetting.Keybind("Hotkey", 84).description("Tests key capture and unbinding.")
   );
   private final ModuleSetting.Text moduleText = this.setting(
      new ModuleSetting.Text("Label", "ellice", 32).description("Tests a text field with a 32-character limit.")
   );
   private final ModuleSetting.Range moduleRange = this.setting(
      new ModuleSetting.Range(
            "Range", 20.0F, 80.0F, 0.0F, 100.0F, 1.0F
         )
         .description("Tests a two-handle 0–100 range slider.")
   );
   private final ModuleSetting.MultiSelect moduleMultiSelect = this.setting(
      new ModuleSetting.MultiSelect("Tags", new String[]{"Glow", "Outline", "Tracer", "Health"}, "Glow", "Health")
         .description("Tests selecting multiple values.")
   );

   public EnabledComponent() {
      super(
         ModuleBuilderData.builder("Test Module")
            .description("Gallery for previewing and validating every supported setting control.")
            .category(ModuleFeatureType.DEVELOPMENT)
            .build()
      );
   }
}
