package dev.felix.ellice.compat;

import dev.felix.ellice.security.ElliceKeep;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.SharedConstants;

@ElliceKeep
public final class MinecraftAdapters {
  private static final List<String> ADAPTER_CLASS_NAMES =
      List.of(
          "dev.felix.ellice.compat.adapter.mc26_1.Mc26_1Adapter",
          "dev.felix.ellice.compat.adapter.mc1_21_4.Mc1_21_4Adapter",
          "dev.felix.ellice.compat.adapter.mc1_21_10.Mc1_21_10Adapter",
          "dev.felix.ellice.compat.adapter.mc1_21_11.Mc1_21_11Adapter");
  private static final List<CompatibilityProvider> ADAPTERS = loadAdapters();
  private static final CompatibilityProvider ACTIVE = selectActive();

  private MinecraftAdapters() {}

  public static CompatibilityProvider active() {
    return ACTIVE;
  }

  public static List<CompatibilityProvider> all() {
    return ADAPTERS;
  }

  private static CompatibilityProvider selectActive() {
    if (ADAPTERS.isEmpty()) {
      throw new IllegalStateException("No Minecraft adapter was compiled into this ellice build");
    }

    String runtimeVersion = runtimeMinecraftVersion();

    for (CompatibilityProvider adapter : ADAPTERS) {
      if (adapter.metadata().supportsVersion(runtimeVersion)) {
        return adapter;
      }
    }

    return ADAPTERS.getFirst();
  }

  private static List<CompatibilityProvider> loadAdapters() {
    List<CompatibilityProvider> adapters = new ArrayList<>();
    ClassLoader loader = MinecraftAdapters.class.getClassLoader();

    for (String className : ADAPTER_CLASS_NAMES) {
      try {
        Class<?> type = Class.forName(className, true, loader);
        if (type.getDeclaredConstructor().newInstance() instanceof CompatibilityProvider adapter) {
          adapters.add(adapter);
        }
      } catch (ClassNotFoundException classNotFoundException) {
      } catch (InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e) {
        throw new IllegalStateException("Failed to load Minecraft adapter " + className, e);
      }
    }

    return List.copyOf(adapters);
  }

  private static String runtimeMinecraftVersion() {
    try {
      SharedConstants.tryDetectVersion();
      Object version = SharedConstants.getCurrentVersion();

      try {
        return String.valueOf(version.getClass().getMethod("name").invoke(version));
      } catch (NoSuchMethodException ignored) {
        return String.valueOf(version.getClass().getMethod("getName").invoke(version));
      }
    } catch (Throwable ignored) {
      return "";
    }
  }
}
