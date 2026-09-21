package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.survival.SurvivalAllService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TaskCheckValidator {
  private static final Set<String> text =
      Set.of(
          "minecraft:wooden_pickaxe",
          "minecraft:stone_pickaxe",
          "minecraft:iron_pickaxe",
          "minecraft:diamond_pickaxe",
          "minecraft:netherite_pickaxe");

  private TaskCheckValidator() {}

  public static List<TaskCheckValidator.Deficit> check(Map<String, Integer> entries) {
    ArrayList arrayList = new ArrayList();
    int value = 0;

    for (String currentText : TreeHarvestTask.trunkIds()) {
      value += entries.getOrDefault(currentText, 0);
    }

    if (value < 16) {
      arrayList.add(
          new TaskCheckValidator.Deficit(TreeHarvestTask.trunkIds(), 16 - value, "Chop trees"));
    }

    int currentValue = 0;

    for (String nextText : SurvivalAllService.all().keySet()) {
      currentValue += entries.getOrDefault(nextText, 0);
    }

    if (currentValue < 10) {
      arrayList.add(
          new TaskCheckValidator.Deficit(
              SurvivalAllService.all().keySet(), 10 - currentValue, "Gather food (food phase)"));
    }

    int nextValue = 0;

    for (String previousText : text) {
      nextValue += entries.getOrDefault(previousText, 0);
    }

    if (nextValue < 2) {
      arrayList.add(new TaskCheckValidator.Deficit(text, 2 - nextValue, "Craft a spare pickaxe"));
    }

    return List.copyOf(arrayList);
  }

  public static boolean blocksMission(List<TaskCheckValidator.Deficit> items) {
    for (TaskCheckValidator.Deficit deficit : items) {
      if (!deficit.advice().startsWith("Gather food")) {
        return true;
      }
    }

    return false;
  }

  public record Deficit(Set<String> ids, int missing, String advice) {}
}
