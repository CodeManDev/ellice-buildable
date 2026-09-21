package dev.felix.ellice.render.render3d;

import java.util.Objects;

public record Render3dData(
   boolean enabled, int visibleColor, int occludedColor, float radiusPixels, float intensity, float highlights, Render3dFeatureType visibility
) {
   public static final Render3dData NONE = new Render3dData(
      false, 0, 0, 6.0F, 1.35F, 0.65F, Render3dFeatureType.BOTH
   );

   public Render3dData(
      boolean enabled, int visibleColor, int occludedColor, float radiusPixels, float intensity, float highlights, Render3dFeatureType visibility
   ) {
      Objects.requireNonNull(visibility, "visibility");
      updateState(radiusPixels, 3.0F, 16.0F);
      updateState(intensity, 0.0F, 3.0F);
      updateState(highlights, 0.0F, 1.0F);
      this.enabled = enabled;
      this.visibleColor = visibleColor;
      this.occludedColor = occludedColor;
      this.radiusPixels = radiusPixels;
      this.intensity = intensity;
      this.highlights = highlights;
      this.visibility = visibility;
   }

   public boolean hasVisible() {
      return this.enabled && this.intensity > 0.0F && this.visibility.includesVisible() && this.visibleColor >>> 24 != 0;
   }

   public boolean hasOccluded() {
      return this.enabled && this.intensity > 0.0F && this.visibility.includesOccluded() && this.occludedColor >>> 24 != 0;
   }

   public boolean renderable() {
      return this.hasVisible() || this.hasOccluded();
   }

   private static void updateState(float value, float currentValue, float nextValue) {
      if (!Float.isFinite(value) || value < currentValue || value > nextValue) {
         throw new IllegalArgumentException("Invalid glow control");
      }
   }
}
