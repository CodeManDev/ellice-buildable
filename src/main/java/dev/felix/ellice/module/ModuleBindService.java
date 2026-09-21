package dev.felix.ellice.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.lwjgl.glfw.GLFW;

public final class ModuleBindService {
  private final Map<Integer, String> text = new LinkedHashMap<>();
  private final ModuleRegisterService moduleRegisterService;

  public ModuleBindService() {
    this(new ModuleRegisterService());
  }

  public ModuleBindService(ModuleRegisterService moduleRegister) {
    this.moduleRegisterService = Objects.requireNonNull(moduleRegister, "modules");
  }

  public void bind(int value, String currentText) {
    this.text.values().removeIf(item -> item.equals(currentText));
    if (value > 0) {
      this.text.put(value, currentText);
    }
  }

  public void unbind(String currentText) {
    this.text.values().removeIf(item -> item.equals(currentText));
  }

  public int getKey(String currentText) {
    for (Entry entry : this.text.entrySet()) {
      if (((String) entry.getValue()).equals(currentText)) {
        return (Integer) entry.getKey();
      }
    }

    return -1;
  }

  public Map<Integer, String> all() {
    return Collections.unmodifiableMap(this.text);
  }

  public boolean onKey(int value, int currentValue, boolean enabled) {
    return enabled && currentValue == 1 && this.onKeyPress(value);
  }

  public boolean onKeyPress(int value) {
    String currentText = this.text.get(value);
    if (currentText == null) {
      return false;
    }

    Module module = this.moduleRegisterService.get(currentText).orElse(null);
    if (module == null) {
      return false;
    }

    module.toggle();
    return true;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();

    for (Entry entry : this.text.entrySet()) {
      jsonObject.addProperty(String.valueOf(entry.getKey()), (String) entry.getValue());
    }

    return jsonObject;
  }

  public void fromJson(JsonObject jsonObject) {
    this.text.clear();

    for (String currentText : jsonObject.keySet()) {
      try {
        String nextText = jsonObject.get(currentText).getAsString();
        if (nextText.equals("Rotation Lab")) {
          nextText = "KillAura";
        }

        if (nextText.equals("Scaffold Walk Lab")) {
          nextText = "ScaffoldWalk";
        }

        if (nextText.equals("2D ESP") || nextText.equals("Player Fire ESP")) {
          if (jsonObject.entrySet().stream()
                  .anyMatch(entry -> ((JsonElement) entry.getValue()).getAsString().equals("ESP"))
              || this.text.containsValue("ESP")) {
            continue;
          }

          nextText = "ESP";
        }

        this.text.put(Integer.parseInt(currentText), nextText);
      } catch (NumberFormatException numberFormatException) {
      }
    }
  }

  public static boolean isKeyboardKey(int value) {
    return value >= 65 && value <= 90
        || value >= 48 && value <= 57
        || value >= 290 && value <= 314
        || value >= 320 && value <= 336
        || value >= 340 && value <= 348
        || value >= 256 && value <= 269
        || value >= 280 && value <= 284
        || Set.of(32, 39, 44, 45, 46, 47, 59, 61, 91, 92, 93, 96, 161, 162).contains(value);
  }

  public static String keyName(int value) {
    if (value <= 0) {
      return "";
    }

    String name = GLFW.glfwGetKeyName(value, 0);
    if (name != null) {
      return name.toUpperCase();
    }

    return switch (value) {
      case 32 -> "SPACE";
      case 256 -> "ESC";
      case 257 -> "ENTER";
      case 258 -> "TAB";
      case 262 -> "RIGHT";
      case 263 -> "LEFT";
      case 264 -> "DOWN";
      case 265 -> "UP";
      case 340 -> "LSHIFT";
      case 341 -> "LCTRL";
      case 342 -> "LALT";
      case 344 -> "RSHIFT";
      case 345 -> "RCTRL";
      case 346 -> "RALT";
      default -> "K" + value;
    };
  }
}
