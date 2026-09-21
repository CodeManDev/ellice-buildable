package dev.felix.ellice.feature.survival;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SurvivalAllService {
  private static final Map<String, SurvivalAllService.Nutrition> text = createText();

  private static Map<String, SurvivalAllService.Nutrition> createText() {
    LinkedHashMap linkedHashMap = new LinkedHashMap();
    linkedHashMap.put("minecraft:golden_carrot", new SurvivalAllService.Nutrition(6, 14.4));
    linkedHashMap.put("minecraft:cooked_beef", new SurvivalAllService.Nutrition(8, 12.8));
    linkedHashMap.put("minecraft:cooked_porkchop", new SurvivalAllService.Nutrition(8, 12.8));
    linkedHashMap.put("minecraft:rabbit_stew", new SurvivalAllService.Nutrition(10, 12.0));
    linkedHashMap.put("minecraft:golden_apple", new SurvivalAllService.Nutrition(4, 9.6));
    linkedHashMap.put("minecraft:enchanted_golden_apple", new SurvivalAllService.Nutrition(4, 9.6));
    linkedHashMap.put("minecraft:cooked_mutton", new SurvivalAllService.Nutrition(6, 9.6));
    linkedHashMap.put("minecraft:cooked_salmon", new SurvivalAllService.Nutrition(6, 9.6));
    linkedHashMap.put("minecraft:mushroom_stew", new SurvivalAllService.Nutrition(6, 7.2));
    linkedHashMap.put("minecraft:cooked_chicken", new SurvivalAllService.Nutrition(6, 7.2));
    linkedHashMap.put("minecraft:beetroot_soup", new SurvivalAllService.Nutrition(6, 7.2));
    linkedHashMap.put("minecraft:baked_potato", new SurvivalAllService.Nutrition(5, 6.0));
    linkedHashMap.put("minecraft:bread", new SurvivalAllService.Nutrition(5, 6.0));
    linkedHashMap.put("minecraft:cooked_cod", new SurvivalAllService.Nutrition(5, 6.0));
    linkedHashMap.put("minecraft:cooked_rabbit", new SurvivalAllService.Nutrition(5, 6.0));
    linkedHashMap.put("minecraft:pumpkin_pie", new SurvivalAllService.Nutrition(8, 4.8));
    linkedHashMap.put("minecraft:carrot", new SurvivalAllService.Nutrition(3, 3.6));
    linkedHashMap.put("minecraft:apple", new SurvivalAllService.Nutrition(4, 2.4));
    linkedHashMap.put("minecraft:beef", new SurvivalAllService.Nutrition(3, 1.8));
    linkedHashMap.put("minecraft:porkchop", new SurvivalAllService.Nutrition(3, 1.8));
    linkedHashMap.put("minecraft:rabbit", new SurvivalAllService.Nutrition(3, 1.8));
    linkedHashMap.put("minecraft:melon_slice", new SurvivalAllService.Nutrition(2, 1.2));
    linkedHashMap.put("minecraft:sweet_berries", new SurvivalAllService.Nutrition(2, 1.2));
    linkedHashMap.put("minecraft:mutton", new SurvivalAllService.Nutrition(2, 1.2));
    linkedHashMap.put("minecraft:beetroot", new SurvivalAllService.Nutrition(1, 1.2));
    linkedHashMap.put("minecraft:honey_bottle", new SurvivalAllService.Nutrition(6, 1.2));
    linkedHashMap.put("minecraft:potato", new SurvivalAllService.Nutrition(1, 0.6));
    linkedHashMap.put("minecraft:dried_kelp", new SurvivalAllService.Nutrition(1, 0.6));
    linkedHashMap.put("minecraft:cod", new SurvivalAllService.Nutrition(2, 0.4));
    linkedHashMap.put("minecraft:salmon", new SurvivalAllService.Nutrition(2, 0.4));
    linkedHashMap.put("minecraft:cookie", new SurvivalAllService.Nutrition(2, 0.4));
    linkedHashMap.put("minecraft:glow_berries", new SurvivalAllService.Nutrition(2, 0.4));
    linkedHashMap.put("minecraft:tropical_fish", new SurvivalAllService.Nutrition(1, 0.2));
    return Collections.unmodifiableMap(linkedHashMap);
  }

  private SurvivalAllService() {}

  public static Map<String, SurvivalAllService.Nutrition> all() {
    return text;
  }

  public static boolean isKnownFood(String currentText) {
    return currentText != null && text.containsKey(currentText);
  }

  public static SurvivalAllService.Nutrition nutrition(String currentText) {
    return text.get(currentText);
  }

  public record Nutrition(int food, double saturation) {
    public Nutrition(int food, double saturation) {
      if (food >= 1
          && food <= 10
          && Double.isFinite(saturation)
          && !(saturation < 0.0)
          && !(saturation > 15.0)) {
        this.food = food;
        this.saturation = saturation;
      } else {
        throw new IllegalArgumentException("Invalid nutrition");
      }
    }
  }
}
