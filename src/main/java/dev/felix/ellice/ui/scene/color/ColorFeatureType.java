package dev.felix.ellice.ui.scene.color;

public enum ColorFeatureType {
   GRADIENT("Gradient"),
   TRIANGLE("Triangle");

   private final String text;

   ColorFeatureType(String currentText) {
      this.text = currentText;
   }

   public String label() {
      return this.text;
   }


   private static ColorFeatureType[] $values() {
      return new ColorFeatureType[]{GRADIENT, TRIANGLE};
   }
}
