package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import java.util.Objects;
import java.util.function.Supplier;

public final class ScaffoldForActorService {
  private static final ThreadLocal<ScaffoldForActorService.Binding> threadLocal =
      new ThreadLocal<>();

  private ScaffoldForActorService() {}

  public static RotationData forActor(Object value) {
    ScaffoldForActorService.Binding binding = threadLocal.get();
    return binding != null && binding.actor() == value ? binding.rotation() : null;
  }

  public static <T> T withRotation(Object value, RotationData rotationData, Supplier<T> supplier) {
    Objects.requireNonNull(value, "actor");
    Objects.requireNonNull(rotationData, "rotation");
    Objects.requireNonNull(supplier, "interaction");
    ScaffoldForActorService.Binding binding = threadLocal.get();
    threadLocal.set(new ScaffoldForActorService.Binding(value, rotationData));

    try {
      return (T) supplier.get();
    } finally {
      if (binding == null) {
        threadLocal.remove();
      } else {
        threadLocal.set(binding);
      }
    }
  }

  private record Binding(Object actor, RotationData rotation) {}
}
