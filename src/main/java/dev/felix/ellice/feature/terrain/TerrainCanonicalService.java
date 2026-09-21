package dev.felix.ellice.feature.terrain;

public final class TerrainCanonicalService {
  public static final String OVERWORLD = "minecraft:overworld";
  public static final String NETHER = "minecraft:the_nether";
  public static final String END = "minecraft:the_end";

  private TerrainCanonicalService() {}

  public static String canonical(String text) {
    if (text == null) {
      return "";
    }

    int value = text.lastIndexOf(" / ");
    return value >= 0 && text.endsWith("]") ? text.substring(value + 3, text.length() - 1) : text;
  }

  public static String label(String text) {
    return switch (canonical(text)) {
      case "minecraft:overworld" -> "Overworld";
      case "minecraft:the_nether" -> "Nether";
      case "minecraft:the_end" -> "The End";
      default -> canonical(text);
    };
  }

  public static TerrainCanonicalService.PortalPlan portal(TerrainKindData terrainKindData) {
    return switch (canonical(terrainKindData.dimension())) {
      case "minecraft:overworld" ->
          new TerrainCanonicalService.PortalPlan(
              "minecraft:the_nether", terrainKindData.x() / 8.0, terrainKindData.z() / 8.0);
      case "minecraft:the_nether" ->
          new TerrainCanonicalService.PortalPlan(
              "minecraft:overworld", terrainKindData.x() * 8.0, terrainKindData.z() * 8.0);
      default -> null;
    };
  }

  public record PortalPlan(String dimension, double x, double z) {
    public boolean insideBorder() {
      return Math.abs(this.x) < 2.9999984E7 && Math.abs(this.z) < 2.9999984E7;
    }

    public String coordinates() {
      return "X " + (long) Math.floor(this.x) + " · Z " + (long) Math.floor(this.z);
    }
  }
}
