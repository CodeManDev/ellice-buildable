package dev.felix.ellice.feature.privacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PrivacyRevisionService {
   private static volatile PrivacyRevisionService.Policy policy = new PrivacyRevisionService.Policy("", "", 0);

   private PrivacyRevisionService() {
   }

   public static int revision() {
      return policy.revision;
   }

   public static boolean active() {
      return !policy.original.isEmpty();
   }

   public static synchronized boolean configure(String text, String currentText) {
      String nextText = text == null ? "" : text;
      String previousText = sanitize(currentText);
      if (previousText.isBlank()) {
         previousText = "Player";
      }

      PrivacyRevisionService.Policy currentPolicy = policy;
      if (currentPolicy.original.equals(nextText) && currentPolicy.alias.equals(previousText)) {
         return false;
      }

      policy = new PrivacyRevisionService.Policy(nextText, previousText, currentPolicy.revision + 1);
      return true;
   }

   public static void clear() {
      configure("", "Player");
   }

   public static String sanitize(String text) {
      if (text == null) {
         return "";
      }

      StringBuilder stringBuilder = new StringBuilder();
      byte byteValue = 0;

      for (int value : text.codePoints().toArray()) {
         if (byteValue != 0) {
            byteValue = 0;
         } else if (value == 167) {
            byteValue = 1;
         } else if (!Character.isISOControl(value) && Character.getType(value) != 16 && (value < 55296 || value > 57343)) {
            if (stringBuilder.codePointCount(0, stringBuilder.length()) == 32) {
               break;
            }

            stringBuilder.appendCodePoint(value);
         }
      }

      return stringBuilder.toString();
   }

   public static String replace(String currentText) {
      return active() ? map(currentText).text() : currentText;
   }

   public static PrivacyRevisionService.Mapping map(String text) {
      PrivacyRevisionService.Policy currentPolicy = policy;
      if (text != null && !currentPolicy.original.isEmpty() && text.length() >= currentPolicy.original.length()) {
         PrivacyRevisionService.Mapping mapping = currentPolicy.cache.get(text);
         if (mapping != null) {
            return mapping;
         }

         StringBuilder stringBuilder = new StringBuilder();
         ArrayList arrayList = new ArrayList();
         int value = 0;
         int index = 0;

         while (index <= text.length() - currentPolicy.original.length()) {
            if (!currentPolicy.alias.equalsIgnoreCase(currentPolicy.original) && text.startsWith(currentPolicy.alias, index)) {
               index += currentPolicy.alias.length();
            } else {
               int currentLength = index + currentPolicy.original.length();
               if (!text.regionMatches(true, index, currentPolicy.original, 0, currentPolicy.original.length())
                  || index != 0 && checkCondition(text.charAt(index - 1))
                  || currentLength != text.length() && checkCondition(text.charAt(currentLength))) {
                  index++;
               } else {
                  stringBuilder.append(text, value, index);
                  int nextLength = stringBuilder.length();
                  stringBuilder.append(currentPolicy.alias);
                  arrayList.add(new PrivacyRevisionService.Span(index, currentLength, nextLength, stringBuilder.length()));
                  value = currentLength;
                  index = currentLength;
               }
            }
         }

         PrivacyRevisionService.Mapping previousLength = arrayList.isEmpty()
            ? new PrivacyRevisionService.Mapping(text, text, List.of())
            : new PrivacyRevisionService.Mapping(text, stringBuilder.append(text, value, text.length()).toString(), List.copyOf(arrayList));
         if (currentPolicy.cache.size() >= 512) {
            currentPolicy.cache.clear();
         }

         if (text.length() <= 4096) {
            currentPolicy.cache.put(text, previousLength);
         }

         return previousLength;
      } else {
         return new PrivacyRevisionService.Mapping(text, text, List.of());
      }
   }

   private static boolean checkCondition(char character) {
      return character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '_';
   }

   public record Mapping(String source, String text, List<PrivacyRevisionService.Span> spans) {
      public boolean changed() {
         return !this.spans.isEmpty();
      }

      public int displayOffset(int value) {
         int currentLength = Math.max(0, Math.min(this.source == null ? 0 : this.source.length(), value));
         int currentValue = 0;

         for (PrivacyRevisionService.Span span : this.spans) {
            if (currentLength < span.start()) {
               break;
            }

            if (currentLength <= span.end()) {
               int nextValue = this.text.codePointCount(span.displayStart(), span.displayEnd());
               int previousValue = Math.round((float)(currentLength - span.start()) * nextValue / (span.end() - span.start()));
               return this.text.offsetByCodePoints(span.displayStart(), previousValue);
            }

            currentValue = span.displayEnd() - span.end();
         }

         return currentLength + currentValue;
      }

      public int sourceOffset(int value) {
         int currentValue = 0;

         for (PrivacyRevisionService.Span span : this.spans) {
            if (value < span.displayStart()) {
               break;
            }

            if (value < span.displayEnd()) {
               return span.start();
            }

            currentValue = span.displayEnd() - span.end();
         }

         return value - currentValue;
      }
   }

   private static final class Policy {
      final String original;
      final String alias;
      final int revision;
      final Map<String, PrivacyRevisionService.Mapping> cache = new ConcurrentHashMap<>();

      Policy(String text, String currentText, int value) {
         this.original = text;
         this.alias = currentText;
         this.revision = value;
      }
   }

   public record Span(int start, int end, int displayStart, int displayEnd) {
   }
}
