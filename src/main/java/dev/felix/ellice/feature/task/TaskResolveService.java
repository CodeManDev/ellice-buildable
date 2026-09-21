package dev.felix.ellice.feature.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class TaskResolveService {
  private static final Set<String> text =
      Set.of(
          "minecraft:oak_planks",
          "minecraft:spruce_planks",
          "minecraft:birch_planks",
          "minecraft:jungle_planks",
          "minecraft:acacia_planks",
          "minecraft:dark_oak_planks",
          "minecraft:mangrove_planks",
          "minecraft:cherry_planks",
          "minecraft:pale_oak_planks");
  private static final List<String> text2 =
      List.of(
          "oak",
          "spruce",
          "birch",
          "jungle",
          "acacia",
          "dark_oak",
          "mangrove",
          "cherry",
          "pale_oak");

  private TaskResolveService() {}

  static String chooseVariant(
      TaskResolveService.Ingredient ingredient, Map<String, Integer> entries) {
    String text = null;
    int value = -1;

    for (String currentText : ingredient.ids()) {
      int currentValue = entries.getOrDefault(currentText, 0);
      if (currentValue > value) {
        value = currentValue;
        text = currentText;
      }
    }

    return text;
  }

  public static TaskResolveService.Plan resolve(
      List<TaskResolveService.Recipe> items, String text, int value, Map<String, Integer> entries) {
    Objects.requireNonNull(items, "recipes");
    Objects.requireNonNull(entries, "inventory");
    if (text != null && !text.isEmpty() && value >= 1) {
      LinkedHashMap<String, List<TaskResolveService.Recipe>> linkedHashMap = new LinkedHashMap<>();

      for (TaskResolveService.Recipe recipe : items) {
        linkedHashMap.computeIfAbsent(recipe.output(), item -> new ArrayList<>()).add(recipe);
      }

      LinkedHashMap currentLinkedHashMap = new LinkedHashMap(entries);
      ArrayList arrayList = new ArrayList();
      ArrayList currentArrayList = new ArrayList();
      HashSet hashSet = new HashSet();
      checkCondition(
          linkedHashMap, text, value, currentLinkedHashMap, arrayList, currentArrayList, hashSet);
      return new TaskResolveService.Plan(List.copyOf(arrayList), List.copyOf(currentArrayList));
    } else {
      throw new IllegalArgumentException("Invalid target");
    }
  }

  private static boolean checkCondition(
      Map<String, List<TaskResolveService.Recipe>> entries,
      String text,
      int value,
      Map<String, Integer> currentEntries,
      List<TaskResolveService.CraftStep> items,
      List<String> currentItems,
      Set<String> values) {
    int currentValue = currentEntries.getOrDefault(text, 0);
    if (currentValue >= value) {
      return true;
    }

    List nextItems = entries.getOrDefault(text, List.of());
    if (nextItems.isEmpty()) {
      if (!currentItems.contains(text)) {
        currentItems.add(text);
      }

      return false;
    } else {
      if (!values.add(text)) {
        throw new IllegalArgumentException("Recipe cycle at " + text);
      }

      try {
        TaskResolveService.Recipe recipe = (TaskResolveService.Recipe) nextItems.getFirst();
        int nextValue = (value - currentValue + recipe.outputCount() - 1) / recipe.outputCount();

        for (TaskResolveService.Ingredient ingredient : recipe.ingredients()) {
          if (!checkCondition2(
              ingredient, nextValue, entries, currentEntries, items, currentItems, values)) {
            return false;
          }
        }

        items.add(new TaskResolveService.CraftStep(text, nextValue, recipe.station()));
        currentEntries.merge(text, nextValue * recipe.outputCount(), Integer::sum);
        return true;
      } finally {
        values.remove(text);
      }
    }
  }

  private static boolean checkCondition2(
      TaskResolveService.Ingredient ingredient,
      int value,
      Map<String, List<TaskResolveService.Recipe>> entries,
      Map<String, Integer> currentEntries,
      List<TaskResolveService.CraftStep> items,
      List<String> currentItems,
      Set<String> values) {
    ArrayList arrayList = new ArrayList<>(ingredient.ids());
    arrayList.sort(
        Comparator.<String>comparingInt(item -> -currentEntries.getOrDefault(item, 0))
            .thenComparing(Comparator.naturalOrder()));
    int currentCount = ingredient.count() * value;

    for (String text : (Iterable<String>) (Iterable<?>) (arrayList)) {
      int currentValue = Math.min(currentCount, currentEntries.getOrDefault(text, 0));
      currentEntries.merge(text, -currentValue, Integer::sum);
      currentCount -= currentValue;
    }

    if (currentCount == 0) {
      return true;
    }

    LinkedHashMap linkedHashMap = new LinkedHashMap(currentEntries);
    int currentSize = items.size();
    int nextSize = currentItems.size();

    for (String currentText : (Iterable<String>) (Iterable<?>) (arrayList)) {
      currentEntries.clear();
      currentEntries.putAll(linkedHashMap);

      while (items.size() > currentSize) {
        items.removeLast();
      }

      while (currentItems.size() > nextSize) {
        currentItems.removeLast();
      }

      if (checkCondition(
          entries, currentText, currentCount, currentEntries, items, currentItems, values)) {
        currentEntries.merge(currentText, -currentCount, Integer::sum);
        return true;
      }
    }

    currentEntries.clear();
    currentEntries.putAll(linkedHashMap);

    while (items.size() > currentSize) {
      items.removeLast();
    }

    while (currentItems.size() > nextSize) {
      currentItems.removeLast();
    }

    checkCondition(
        entries,
        (String) arrayList.getFirst(),
        currentCount,
        currentEntries,
        items,
        currentItems,
        values);
    return false;
  }

  public static Map<String, Integer> deficits(
      List<TaskResolveService.Recipe> items,
      Map<String, Integer> entries,
      Map<String, Integer> currentEntries) {
    LinkedHashMap<String, List<TaskResolveService.Recipe>> linkedHashMap = new LinkedHashMap<>();

    for (TaskResolveService.Recipe recipe : items) {
      linkedHashMap.computeIfAbsent(recipe.output(), item -> new ArrayList<>()).add(recipe);
    }

    LinkedHashMap currentLinkedHashMap = new LinkedHashMap(currentEntries);
    LinkedHashMap nextLinkedHashMap = new LinkedHashMap();

    for (Entry entry : entries.entrySet()) {
      if ((Integer) entry.getValue() < 0) {
        throw new IllegalArgumentException("Negative target");
      }

      updateState(
          (String) entry.getKey(),
          (Integer) entry.getValue(),
          linkedHashMap,
          currentLinkedHashMap,
          nextLinkedHashMap,
          new HashSet<>());
    }

    return Map.copyOf(nextLinkedHashMap);
  }

  private static void updateState(
      String text,
      int value,
      Map<String, List<TaskResolveService.Recipe>> entries,
      Map<String, Integer> currentEntries,
      Map<String, Integer> nextEntries,
      Set<String> currentValues) {
    int currentValue = Math.min(value, currentEntries.getOrDefault(text, 0));
    currentEntries.merge(text, -currentValue, Integer::sum);
    value -= currentValue;
    if (value != 0) {
      List<TaskResolveService.Recipe> items = entries.getOrDefault(text, List.of());
      if (items.isEmpty()) {
        nextEntries.merge(text, value, Integer::sum);
      } else {
        if (!currentValues.add(text)) {
          throw new IllegalArgumentException("Recipe cycle at " + text);
        }

        try {
          TaskResolveService.Recipe recipe = (TaskResolveService.Recipe) items.getFirst();
          int nextValue = (value + recipe.outputCount() - 1) / recipe.outputCount();

          for (TaskResolveService.Ingredient ingredient : recipe.ingredients()) {
            int currentCount = ingredient.count() * nextValue;
            List<String> currentItems = ingredient.ids().stream().sorted().toList();

            for (String currentText : (Iterable<String>) (Iterable<?>) (currentItems)) {
              int previousValue =
                  Math.min(currentCount, currentEntries.getOrDefault(currentText, 0));
              currentEntries.merge(currentText, -previousValue, Integer::sum);
              currentCount -= previousValue;
            }

            if (currentCount != 0) {
              LinkedHashMap<String, Integer> linkedHashMap = null;
              LinkedHashMap<String, Integer> currentLinkedHashMap = null;
              int sourceValue = Integer.MAX_VALUE;

              for (String nextText : (Iterable<String>) (Iterable<?>) (currentItems)) {
                LinkedHashMap<String, Integer> nextLinkedHashMap =
                    new LinkedHashMap<>(currentEntries);
                LinkedHashMap<String, Integer> previousLinkedHashMap =
                    new LinkedHashMap<>(nextEntries);
                updateState(
                    nextText,
                    currentCount,
                    entries,
                    nextLinkedHashMap,
                    previousLinkedHashMap,
                    currentValues);
                int targetValue =
                    previousLinkedHashMap.values().stream().mapToInt(Integer::intValue).sum();
                if (targetValue < sourceValue) {
                  sourceValue = targetValue;
                  linkedHashMap = nextLinkedHashMap;
                  currentLinkedHashMap = previousLinkedHashMap;
                }
              }

              currentEntries.clear();
              currentEntries.putAll(linkedHashMap);
              nextEntries.clear();
              nextEntries.putAll(currentLinkedHashMap);
            }
          }

          currentEntries.merge(text, nextValue * recipe.outputCount() - value, Integer::sum);
        } finally {
          currentValues.remove(text);
        }
      }
    }
  }

  public static Map<String, Integer> rawDemand(
      List<TaskResolveService.Recipe> items, String text, int value) {
    LinkedHashMap<String, List<TaskResolveService.Recipe>> linkedHashMap = new LinkedHashMap<>();

    for (TaskResolveService.Recipe recipe : items) {
      linkedHashMap.computeIfAbsent(recipe.output(), item -> new ArrayList<>()).add(recipe);
    }

    LinkedHashMap currentLinkedHashMap = new LinkedHashMap();
    updateState2(text, value, linkedHashMap, currentLinkedHashMap, new HashSet<>(), Map.of());
    return Map.copyOf(currentLinkedHashMap);
  }

  private static void updateState2(
      String text,
      int value,
      Map<String, List<TaskResolveService.Recipe>> entries,
      Map<String, Integer> currentEntries,
      Set<String> values,
      Map<String, Integer> nextEntries) {
    List items = entries.getOrDefault(text, List.of());
    if (items.isEmpty()) {
      currentEntries.merge(text, value, Integer::sum);
    } else {
      if (!values.add(text)) {
        throw new IllegalArgumentException("Recipe cycle at " + text);
      }

      try {
        TaskResolveService.Recipe recipe = (TaskResolveService.Recipe) items.getFirst();
        int currentValue = (value + recipe.outputCount() - 1) / recipe.outputCount();

        for (TaskResolveService.Ingredient ingredient : recipe.ingredients()) {
          updateState2(
              chooseVariant(ingredient, nextEntries),
              ingredient.count() * currentValue,
              entries,
              currentEntries,
              values,
              nextEntries);
        }
      } finally {
        values.remove(text);
      }
    }
  }

  public static TaskResolveService.Station stationFor(
      List<TaskResolveService.Recipe> items, String text) {
    Objects.requireNonNull(items, "recipes");

    for (TaskResolveService.Recipe currentRecipe : items) {
      if (currentRecipe.output().equals(text)) {
        return currentRecipe.station();
      }
    }

    throw new IllegalArgumentException("No recipe for " + text);
  }

  public static List<TaskResolveService.Recipe> standard() {
    ArrayList arrayList = new ArrayList();

    for (String currentText : text2) {
      arrayList.add(
          new TaskResolveService.Recipe(
              "minecraft:" + currentText + "_planks",
              4,
              List.of(TaskResolveService.Ingredient.of("minecraft:" + currentText + "_log", 1)),
              TaskResolveService.Station.INVENTORY));
    }

    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:stick",
            4,
            List.of(TaskResolveService.Ingredient.any(text, 2)),
            TaskResolveService.Station.INVENTORY));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:crafting_table",
            1,
            List.of(TaskResolveService.Ingredient.any(text, 4)),
            TaskResolveService.Station.INVENTORY));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:wooden_pickaxe",
            1,
            List.of(
                TaskResolveService.Ingredient.any(text, 3),
                TaskResolveService.Ingredient.of("minecraft:stick", 2)),
            TaskResolveService.Station.TABLE));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:furnace",
            1,
            List.of(
                TaskResolveService.Ingredient.any(
                    Set.of("minecraft:cobblestone", "minecraft:cobbled_deepslate"), 8)),
            TaskResolveService.Station.TABLE));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:stone_pickaxe",
            1,
            List.of(
                TaskResolveService.Ingredient.any(
                    Set.of("minecraft:cobblestone", "minecraft:cobbled_deepslate"), 3),
                TaskResolveService.Ingredient.of("minecraft:stick", 2)),
            TaskResolveService.Station.TABLE));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:iron_ingot",
            1,
            List.of(
                TaskResolveService.Ingredient.any(
                    Set.of(
                        "minecraft:iron_ore", "minecraft:deepslate_iron_ore", "minecraft:raw_iron"),
                    1)),
            TaskResolveService.Station.FURNACE));
    arrayList.add(
        new TaskResolveService.Recipe(
            "minecraft:iron_pickaxe",
            1,
            List.of(
                TaskResolveService.Ingredient.of("minecraft:iron_ingot", 3),
                TaskResolveService.Ingredient.of("minecraft:stick", 2)),
            TaskResolveService.Station.TABLE));
    return List.copyOf(arrayList);
  }

  public record CraftStep(String output, int batches, TaskResolveService.Station station) {}

  public record Ingredient(Set<String> ids, int count) {
    public Ingredient(Set<String> ids, int count) {
      ids = Set.copyOf(Objects.requireNonNull(ids, "ids"));
      if (!ids.isEmpty() && count >= 1) {
        this.ids = ids;
        this.count = count;
      } else {
        throw new IllegalArgumentException("Invalid ingredient");
      }
    }

    public static TaskResolveService.Ingredient of(String text, int value) {
      return new TaskResolveService.Ingredient(Set.of(text), value);
    }

    public static TaskResolveService.Ingredient any(Set<String> values, int value) {
      return new TaskResolveService.Ingredient(values, value);
    }
  }

  public record Plan(List<TaskResolveService.CraftStep> steps, List<String> missing) {}

  public record Recipe(
      String output,
      int outputCount,
      List<TaskResolveService.Ingredient> ingredients,
      TaskResolveService.Station station) {
    public Recipe(
        String output,
        int outputCount,
        List<TaskResolveService.Ingredient> ingredients,
        TaskResolveService.Station station) {
      if (output != null
          && !output.isEmpty()
          && outputCount >= 1
          && ingredients != null
          && !ingredients.isEmpty()) {
        ingredients = List.copyOf(ingredients);
        Objects.requireNonNull(station, "station");
        this.output = output;
        this.outputCount = outputCount;
        this.ingredients = ingredients;
        this.station = station;
      } else {
        throw new IllegalArgumentException("Invalid recipe");
      }
    }
  }

  public enum Station {
    INVENTORY,
    TABLE,
    FURNACE;

    private static TaskResolveService.Station[] $values() {
      return new TaskResolveService.Station[] {INVENTORY, TABLE, FURNACE};
    }
  }
}
