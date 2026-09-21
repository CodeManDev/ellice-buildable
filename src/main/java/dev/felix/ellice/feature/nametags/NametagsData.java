package dev.felix.ellice.feature.nametags;

import dev.felix.ellice.compat.TextureUvRegion;

public record NametagsData(String name, String detail, int accent, float health, float absorption, TextureUvRegion icon, boolean item) {
   public NametagsData(String name, String detail, int accent, float health, float absorption, TextureUvRegion icon, boolean item) {
      name = cleanName(name);
      detail = detail == null ? "" : detail;
      health = Float.isFinite(health) && health >= 0.0F ? Math.min(1.0F, health) : -1.0F;
      absorption = Float.isFinite(absorption) ? Math.clamp(absorption, 0.0F, 1.0F) : 0.0F;
      this.name = name;
      this.detail = detail;
      this.accent = accent;
      this.health = health;
      this.absorption = absorption;
      this.icon = icon;
      this.item = item;
   }

   public static String cleanName(String name) {
      if (name == null) {
         return "?";
      }

      StringBuilder stringBuilder = new StringBuilder();
      int index = 0;
      int currentIndex = 0;

      while (currentIndex < name.length() && index < 64) {
         int value = name.codePointAt(currentIndex);
         currentIndex += Character.charCount(value);
         if (value == 167 && currentIndex < name.length()) {
            int currentValue = name.codePointAt(currentIndex);
            if (!Character.isISOControl(currentValue) && Character.getType(currentValue) != 16) {
               stringBuilder.appendCodePoint(value).appendCodePoint(currentValue);
               currentIndex += Character.charCount(currentValue);
            } else {
               stringBuilder.appendCodePoint(value);
            }
         } else if (!Character.isISOControl(value) && Character.getType(value) != 16) {
            stringBuilder.appendCodePoint(value);
            index++;
         }
      }

      String currentName = stringBuilder.toString().strip();
      return currentName.isEmpty() ? "?" : currentName;
   }
}
