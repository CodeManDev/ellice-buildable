package dev.felix.ellice.ui.theme;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ThemeIsSetService {
  public static final int UNSET_COLOR = Integer.MIN_VALUE;
  public static final float UNSET_NUMBER = Float.NaN;
  private final Map<String, String> text2 = new LinkedHashMap<>();
  private final Set<Runnable> values = new LinkedHashSet<>();

  public static boolean isSet(int value) {
    return value != Integer.MIN_VALUE;
  }

  public static boolean isSet(float value) {
    return !Float.isNaN(value);
  }

  public void updateColors(Map<String, Integer> entries) {
    entries.forEach(this::setColor);

    for (Runnable runnable : List.copyOf(this.values)) {
      runnable.run();
    }
  }

  public Runnable onAppearanceChanged(Runnable runnable) {
    this.values.add(runnable);
    return () -> this.values.remove(runnable);
  }

  public ThemeIsSetService() {
    this.updateState("accent", "#FF6366f1");
    this.updateState("accent-hover", "#FF818cf8");
    this.updateState("accent-press", "#FF4f46e5");
    this.updateState("accent-glow", "#306366f1");
    this.updateState("surface", "#500D0D18");
    this.updateState("surface-light", "#18FFFFFF");
    this.updateState("surface-solid", "#FF1a1a24");
    this.updateState("surface-hover", "#10FFFFFF");
    this.updateState("surface-active", "#18FFFFFF");
    this.updateState("text", "#E6FFFFFF");
    this.updateState("text-dim", "#66FFFFFF");
    this.updateState("text-muted", "#44FFFFFF");
    this.updateState("success", "#FF22c55e");
    this.updateState("danger", "#FFef4444");
    this.updateState("warning", "#FFf59e0b");
    this.updateState("dot-off", "#33FFFFFF");
    this.updateState("divider", "#15FFFFFF");
    this.updateState("shadow-color", "#40000000");
    this.updateState("header", "#300D0D1C");
    this.updateState("card", "#08FFFFFF");
    this.updateState("card-hover", "#12FFFFFF");
    this.updateState("radius", "10");
    this.updateState("radius-sm", "6");
    this.updateState("radius-xs", "4");
    this.updateState("blur", "16");
    this.updateState("shadow", "6");
    this.updateState("shadow-sm", "3");
    this.updateState("font", "10");
    this.updateState("font-sm", "8");
    this.updateState("font-lg", "14");
    this.updateState("font-xl", "16");
    this.updateState("padding", "8");
    this.updateState("padding-sm", "6");
    this.updateState("gap", "4");
    this.updateState("gap-sm", "1");
    this.updateState("sp0", "0");
    this.updateState("sp1", "2");
    this.updateState("sp2", "4");
    this.updateState("sp3", "6");
    this.updateState("sp4", "8");
    this.updateState("sp5", "12");
    this.updateState("sp6", "16");
    this.updateState("sp7", "20");
    this.updateState("sp8", "24");
    this.updateState("sp10", "32");
    this.updateState("sp12", "40");
    this.updateState("sp16", "56");
    this.updateState("slider.track", "#25FFFFFF");
    this.updateState("slider.fill", "#FF6366f1");
    this.updateState("slider.thumb", "#FFFFFFFF");
    this.updateState("slider.thumb-shadow", "#30000000");
    this.updateState("slider.text", "#AAFFFFFF");
    this.updateState("slider.track-height", "4");
    this.updateState("slider.thumb-radius", "3");
    this.updateState("slider.thumb-shadow-size", "2");
    this.updateState("slider.font-size", "8");
    this.updateState("toggle.track", "#40FFFFFF");
    this.updateState("toggle.active", "#FF6366f1");
    this.updateState("toggle.knob", "#FFFFFFFF");
    this.updateState("textinput.bg", "#20FFFFFF");
    this.updateState("textinput.border-focus", "#FF6366f1");
    this.updateState("textinput.text", "#FFFFFFFF");
    this.updateState("textinput.selection", "#406366f1");
    this.updateState("textinput.radius", "3");
    this.updateState("textinput.font-size", "8");
    this.updateState("combo.bg", "#10FFFFFF");
    this.updateState("combo.hover", "#1CFFFFFF");
    this.updateState("combo.text", "#FFFFFFFF");
    this.updateState("combo.radius", "4");
    this.updateState("keybind.bg", "#15FFFFFF");
    this.updateState("keybind.active", "#FF6366f1");
    this.updateState("keybind.text", "#AAFFFFFF");
    this.updateState("keybind.radius", "4");
  }

  private void updateState(String text, String currentText) {
    this.text2.put(text, currentText);
  }

  public void set(String text, String currentText) {
    this.text2.put(text, currentText);
  }

  public void setColor(String text, int color) {
    this.text2.put(text, String.format(Locale.ROOT, "#%08X", Integer.valueOf(color)));
  }

  public void loadOverrides(Path path) {
    if (Files.exists(path)) {
      try {
        String id = Files.readString(path);
        Map entries =
            (Map) new Gson().fromJson(id, (new TypeToken<Map<String, String>>() {}).getType());
        if (entries != null) {
          this.text2.putAll(entries);
        }

        CoreIsInitializedHandler.LOGGER.info("Theme overrides loaded from {}", path.getFileName());
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.error("Failed to load theme overrides", exception);
      }
    }
  }

  public String get(String text) {
    return this.text2.getOrDefault(text, "");
  }

  public boolean has(String text) {
    return this.text2.containsKey(text);
  }

  public Set<String> keys() {
    return Collections.unmodifiableSet(this.text2.keySet());
  }

  public int color(String text) {
    String currentText = this.get(text);
    return currentText.isEmpty() ? 0 : parseColor(currentText);
  }

  public float number(String text) {
    String currentText = this.get(text);
    if (currentText.isEmpty()) {
      return 0.0F;
    }

    try {
      return Float.parseFloat(currentText);
    } catch (NumberFormatException numberFormatException) {
      return 0.0F;
    }
  }

  public String resolve(String text) {
    if (text != null && text.startsWith("$")) {
      String currentText = this.get(text.substring(1));
      return currentText.isEmpty() ? text : currentText;
    } else {
      return text;
    }
  }

  public static int parseColor(String text) {
    if (text.startsWith("#")) {
      text = text.substring(1);
    }

    try {
      long longValue = Long.parseLong(text, 16);
      return text.length() <= 6 ? (int) (4278190080L | longValue) : (int) longValue;
    } catch (NumberFormatException numberFormatException) {
      return 0;
    }
  }
}
