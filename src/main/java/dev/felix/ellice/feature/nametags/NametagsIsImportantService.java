package dev.felix.ellice.feature.nametags;

import java.util.Set;

public final class NametagsIsImportantService {
  private static final Set<String> text =
      Set.of(
          "diamond",
          "diamond_block",
          "emerald",
          "emerald_block",
          "ancient_debris",
          "netherite_scrap",
          "netherite_ingot",
          "netherite_block",
          "nether_star",
          "totem_of_undying",
          "elytra",
          "trident",
          "mace",
          "heavy_core",
          "golden_apple",
          "enchanted_golden_apple",
          "ender_pearl",
          "ender_eye",
          "end_crystal",
          "experience_bottle",
          "enchanted_book",
          "beacon",
          "dragon_egg",
          "heart_of_the_sea",
          "breeze_rod",
          "wind_charge",
          "netherite_upgrade_smithing_template");

  private NametagsIsImportantService() {}

  public static boolean isImportant(
      String currentText, boolean enabled, boolean currentEnabled, boolean nextEnabled) {
    if (enabled || currentEnabled || nextEnabled) {
      return true;
    } else if (currentText != null && currentText.startsWith("minecraft:")) {
      String currentLength = currentText.substring("minecraft:".length());
      return text.contains(currentLength)
          || currentLength.startsWith("diamond_")
          || currentLength.startsWith("netherite_")
          || currentLength.equals("shulker_box")
          || currentLength.endsWith("_shulker_box");
    } else {
      return false;
    }
  }
}
