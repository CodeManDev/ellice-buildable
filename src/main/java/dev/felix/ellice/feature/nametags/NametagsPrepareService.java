package dev.felix.ellice.feature.nametags;

import java.util.Set;
import java.util.UUID;

public final class NametagsPrepareService {
   private static Set<UUID> values = Set.of();
   private static boolean enabled;

   private NametagsPrepareService() {
   }

   public static void prepare(Set<UUID> currentValues) {
      values = Set.copyOf(currentValues);
      enabled = true;
   }

   public static boolean replaces(UUID uUID) {
      return values.contains(uUID);
   }

   public static boolean suppressesVanilla() {
      return enabled;
   }

   public static void clear() {
      values = Set.of();
      enabled = false;
   }
}

