package dev.felix.ellice.feature.scaffold;

import java.util.Objects;
import java.util.Optional;

public final class ScaffoldPublishService {
   private static volatile ScaffoldRemapService.Input entries;
   private static volatile boolean enabled;

   private ScaffoldPublishService() {
   }

   public static boolean publish(ScaffoldRemapService.Input input) {
      return publish(input, false);
   }

   public static boolean publish(ScaffoldRemapService.Input input, boolean currentEnabled) {
      Objects.requireNonNull(input, "next");
      ScaffoldRemapService.Input currentInput = entries;
      entries = input;
      enabled = currentEnabled;
      return !input.equals(currentInput);
   }

   public static Optional<ScaffoldRemapService.Input> current() {
      return Optional.ofNullable(entries);
   }

   public static boolean suppressLocalSprint() {
      return enabled;
   }

   public static void clear() {
      entries = null;
      enabled = false;
   }
}

