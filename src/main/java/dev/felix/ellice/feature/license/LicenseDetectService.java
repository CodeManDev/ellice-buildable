package dev.felix.ellice.feature.license;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class LicenseDetectService {
   private LicenseDetectService() {
   }

   public static List<String> detect() {
      try {
         return suspicious(ManagementFactory.getRuntimeMXBean().getInputArguments());
      } catch (Exception exception) {
         return List.of();
      }
   }

   static List<String> suspicious(List<String> items) {
      ArrayList arrayList = new ArrayList();
      if (items == null) {
         return arrayList;
      }

      for (String text : items) {
         if (text != null) {
            String currentText = text.toLowerCase(Locale.ROOT);
            if (currentText.startsWith("-javaagent:")) {
               String currentLength = text.substring("-javaagent:".length());
               String nextText = currentLength.toLowerCase(Locale.ROOT);
               if (!nextText.contains("fabric")
                  && !nextText.contains("loom")
                  && !nextText.contains("mixin")
                  && !nextText.contains("quilt")
                  && !nextText.contains("forge")
                  && !nextText.contains("neoforge")) {
                  arrayList.add(createText(currentLength));
               }
            } else if (currentText.contains("jdwp") || currentText.startsWith("-xdebug") || currentText.startsWith("-agentlib:jdwp")) {
               arrayList.add("jdwp-debug");
            }
         }
      }

      return arrayList;
   }

   private static String createText(String text) {
      int value = text.indexOf(61);
      String currentText = value >= 0 ? text.substring(0, value) : text;
      int currentValue = Math.max(currentText.lastIndexOf(47), currentText.lastIndexOf(92));
      currentText = currentValue >= 0 ? currentText.substring(currentValue + 1) : currentText;
      return currentText.length() > 80 ? currentText.substring(0, 80) : currentText;
   }
}
