package dev.felix.ellice.render.render3d;

import java.util.Objects;

public record InterferenceEffectSettings(
   boolean enabled,
   int colorA,
   int colorB,
   int occludedColor,
   float widthPixels,
   float speed,
   float scale,
   float intensity,
   Render3dFeatureType visibility
) {
   public static final InterferenceEffectSettings NONE = new InterferenceEffectSettings(
      false, 0, 0, 0, 12.0F, 0.65F, 2.0F, 1.0F, Render3dFeatureType.BOTH
   );

   public InterferenceEffectSettings(
      boolean enabled,
      int colorA,
      int colorB,
      int occludedColor,
      float widthPixels,
      float speed,
      float scale,
      float intensity,
      Render3dFeatureType visibility
   ) {
      Objects.requireNonNull(visibility, "visibility");
      updateState(widthPixels, 3.0F, 32.0F);
      updateState(speed, 0.0F, 2.0F);
      updateState(scale, 0.5F, 5.0F);
      updateState(intensity, 0.0F, 3.0F);
      this.enabled = enabled;
      this.colorA = colorA;
      this.colorB = colorB;
      this.occludedColor = occludedColor;
      this.widthPixels = widthPixels;
      this.speed = speed;
      this.scale = scale;
      this.intensity = intensity;
      this.visibility = visibility;
   }

   public boolean hasVisible() {
      return this.enabled && this.intensity > 0.0F && this.visibility.includesVisible() && (this.colorA | this.colorB) >>> 24 != 0;
   }

   public boolean hasOccluded() {
      return this.enabled && this.intensity > 0.0F && this.visibility.includesOccluded() && this.occludedColor >>> 24 != 0;
   }

   public boolean renderable() {
      return this.hasVisible() || this.hasOccluded();
   }

   private static void updateState(float value, float currentValue, float nextValue) {
      if (!Float.isFinite(value) || value < currentValue || value > nextValue) {
         throw new IllegalArgumentException("Invalid interference control");
      }
   }
}
