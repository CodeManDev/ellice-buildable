package dev.felix.ellice.module;

public record ModuleBuilderData(String name, String description, ModuleFeatureType category) {
   public static ModuleBuilderData.Builder builder(String text) {
      return new ModuleBuilderData.Builder(text);
   }

   public static final class Builder {
      private final String text;
      private String text2 = "";
      private ModuleFeatureType moduleFeatureType = ModuleFeatureType.TOOLS;

      private Builder(String currentText) {
         this.text = currentText;
      }

      public ModuleBuilderData.Builder description(String text) {
         this.text2 = text;
         return this;
      }

      public ModuleBuilderData.Builder category(ModuleFeatureType currentModuleFeatureType) {
         this.moduleFeatureType = currentModuleFeatureType;
         return this;
      }

      public ModuleBuilderData build() {
         return new ModuleBuilderData(this.text, this.text2, this.moduleFeatureType);
      }
   }
}
