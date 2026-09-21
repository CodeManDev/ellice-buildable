package dev.felix.ellice.render.render3d;

public enum Render3dFeatureType {
   VISIBLE("Visible", true, false),
   THROUGH_WALLS("Through walls", false, true),
   BOTH("Both", true, true);

   private final String text;
   private final boolean enabled;
   private final boolean enabled2;

   Render3dFeatureType(String currentText, boolean currentEnabled, boolean nextEnabled) {
      this.text = currentText;
      this.enabled = currentEnabled;
      this.enabled2 = nextEnabled;
   }

   public String displayName() {
      return this.text;
   }

   public boolean includesVisible() {
      return this.enabled;
   }

   public boolean includesOccluded() {
      return this.enabled2;
   }


   private static Render3dFeatureType[] $values() {
      return new Render3dFeatureType[]{VISIBLE, THROUGH_WALLS, BOTH};
   }
}
