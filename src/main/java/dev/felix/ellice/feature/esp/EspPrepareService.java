package dev.felix.ellice.feature.esp;

import java.util.Set;
import java.util.UUID;

public final class EspPrepareService {
  private static Set<UUID> values = Set.of();

  private EspPrepareService() {}

  public static void prepare(Set<UUID> currentValues) {
    values = Set.copyOf(currentValues);
  }

  public static boolean replaces(UUID uUID) {
    return values.contains(uUID);
  }

  public static void clear() {
    values = Set.of();
  }
}
