package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.changelog.ChangelogFeatureType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

final class ChangelogEntryFormatter {
   private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH);

   private ChangelogEntryFormatter() {
   }

   static String date(String text) {
      try {
         return dateTimeFormatter.format(LocalDate.parse(text));
      } catch (DateTimeParseException dateTimeParseException) {
         return text;
      }
   }

   static String symbol(ChangelogFeatureType changelogFeatureType) {
      return switch (changelogFeatureType) {
         case ADDED -> "add";
         case REMOVED -> "close";
         case FIXED -> "check";
         case IMPROVED -> "tune";
         case CHANGED -> "edit";
      };
   }
}
