package dev.felix.ellice.changelog;

import java.util.Locale;

public enum ChangelogFeatureType {
   ADDED("Added", -14498466),
   IMPROVED("Improved", -13058568),
   CHANGED("Changed", -5796870),
   FIXED("Fixed", -680437),
   REMOVED("Removed", -298619);

   private final String text;
   private final int count;

   ChangelogFeatureType(String currentText, int value) {
      this.text = currentText;
      this.count = value;
   }

   public String label() {
      return this.text;
   }

   public int color() {
      return this.count;
   }

   public static ChangelogFeatureType from(String text) {
      if (text == null) {
         return CHANGED;
      }

      try {
         return valueOf(text.strip().toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException illegalArgumentException) {
         return CHANGED;
      }
   }


   private static ChangelogFeatureType[] $values() {
      return new ChangelogFeatureType[]{ADDED, IMPROVED, CHANGED, FIXED, REMOVED};
   }
}
