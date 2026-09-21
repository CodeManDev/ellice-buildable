package dev.felix.ellice.render.rhi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RhiRepository {
   private static final String text = "/assets/ellice/shaders/ellice/";
   private static final Pattern pattern = Pattern.compile("^\\s*#include\\s*([<\"])([^>\"]+)[>\"]\\s*$");

   private RhiRepository() {
   }

   public static String load(String text) {
      return createText(text, new ArrayDeque<>());
   }

   private static String createText(String text, Deque<String> deque) {
      String currentText = createText4(text);
      if (deque.contains(currentText)) {
         throw new IllegalArgumentException("Cyclic shader include: " + String.join(" -> ", deque) + " -> " + currentText);
      }

      deque.addLast(currentText);
      String nextText = createText2(currentText);
      StringBuilder currentLength = new StringBuilder(nextText.length() + 256);

      for (String previousText : nextText.split("\\R", -1)) {
         Matcher currentMatcher = pattern.matcher(previousText);
         if (currentMatcher.matches()) {
            boolean enabled = "<".equals(currentMatcher.group(1));
            currentLength.append(createText(createText3(currentText, currentMatcher.group(2), enabled), deque));
         } else {
            currentLength.append(previousText).append('\n');
         }
      }

      deque.removeLast();
      return currentLength.toString();
   }

   private static String createText2(String text) {
      String currentText = "/assets/ellice/shaders/ellice/" + text;

      try (InputStream inputStream = RhiRepository.class.getResourceAsStream(currentText)) {
         if (inputStream == null) {
            throw new FileNotFoundException("Shader not found on classpath: " + currentText);
         } else {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
         }
      } catch (IOException iOException) {
         throw new RuntimeException("Failed to load shader: " + currentText, iOException);
      }
   }

   private static String createText3(String text, String currentText, boolean enabled) {
      String nextText = createText4(currentText);
      int value = text.lastIndexOf(47);
      return !enabled && !currentText.startsWith("/") && value >= 0 ? createText4(text.substring(0, value + 1) + nextText) : nextText;
   }

   private static String createText4(String text) {
      String currentText = text.replace('\\', '/');

      while (currentText.startsWith("/")) {
         currentText = currentText.substring(1);
      }

      ArrayDeque arrayDeque = new ArrayDeque();

      for (String nextText : currentText.split("/")) {
         if (!nextText.isEmpty() && !".".equals(nextText)) {
            if ("..".equals(nextText)) {
               if (arrayDeque.isEmpty()) {
                  throw new IllegalArgumentException("Shader path escapes root: " + text);
               }

               arrayDeque.removeLast();
            } else {
               arrayDeque.addLast(nextText);
            }
         }
      }

      return String.join("/", arrayDeque);
   }

   public static String vertex(String text) {
      return load(text + ".vert");
   }

   public static String fragment(String text) {
      return load(text + ".frag");
   }

   public static String compute(String text) {
      return load(text + ".comp");
   }
}
