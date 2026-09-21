package dev.felix.ellice.compat;

import com.mojang.blaze3d.platform.InputConstants;
import dev.felix.ellice.module.ModuleBindService;
import dev.felix.ellice.ui.screen.builtin.keybind.KeyCatalog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class CompatRepository {
  private CompatRepository() {}

  public static Snapshot read() {
    final Minecraft instance = Minecraft.getInstance();
    if (instance == null || instance.options == null) {
      return Snapshot.EMPTY;
    }
    final HashMap map = new HashMap();
    final HashMap map2 = new HashMap();
    final KeyMapping[] keyMappings = instance.options.keyMappings;
    for (int length = keyMappings.length, i = 0; i < length; ++i) {
      final KeyMapping keyMapping = keyMappings[i];
      final InputConstants.Key key = InputConstants.getKey(keyMapping.saveString());
      if (key.getType() == InputConstants.Type.KEYSYM) {
        if (key.getValue() >= 0) {
          ((List<String>) map.computeIfAbsent(key.getValue(), p0 -> new ArrayList()))
              .add(Component.translatable(keyMapping.getName()).getString());
        }
      }
    }
    for (int j = 32; j <= 162; ++j) {
      if (ModuleBindService.isKeyboardKey(j)) {
        final int glfwGetKeyScancode = GLFW.glfwGetKeyScancode(j);
        if (glfwGetKeyScancode >= 0) {
          map2.put(j, KeyCatalog.displayLegend(GLFW.glfwGetKeyName(j, glfwGetKeyScancode), j));
        }
      }
    }
    map.replaceAll((p0, coll) -> List.copyOf((Collection<?>) coll));
    return new Snapshot(
        Map.copyOf((Map<? extends Integer, ? extends List<String>>) map),
        (Map<Integer, String>) Map.copyOf((Map<?, ?>) map2));
  }

  public record Snapshot(Map<Integer, List<String>> uses, Map<Integer, String> legends) {
    public static final Snapshot EMPTY;

    static {
      EMPTY = new Snapshot(Map.of(), Map.of());
    }
  }
}
