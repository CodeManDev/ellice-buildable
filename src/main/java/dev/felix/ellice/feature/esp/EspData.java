package dev.felix.ellice.feature.esp;

public record EspData(
    boolean corners,
    String colorMode,
    int color,
    boolean healthBar,
    boolean armorBar,
    float fillOpacity,
    float lineWidth,
    float labelScale,
    boolean animations) {
  public static EspData defaults() {
    return new EspData(false, "Custom", -3809547, true, false, 0.025F, 0.8F, 1.0F, true);
  }
}
