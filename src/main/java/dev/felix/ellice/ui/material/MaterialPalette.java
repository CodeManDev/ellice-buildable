package dev.felix.ellice.ui.material;

public record MaterialPalette(
    int base, int first, int second, int third, int fourth, boolean light) {
  public static MaterialPalette current() {
    return new MaterialPalette(
        MaterialIsLightService.SURFACE,
        MaterialIsLightService.PRIMARY_CONTAINER,
        MaterialIsLightService.PRIMARY,
        MaterialIsLightService.TERTIARY,
        MaterialIsLightService.SURFACE_HIGH,
        MaterialIsLightService.isLight());
  }
}
