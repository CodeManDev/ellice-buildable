package dev.felix.ellice.ui.material;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.text.TextTextureService;

public final class MaterialTerrainTooltipsService extends SceneCornerRadiusService {
  private boolean enabled;
  private boolean enabled2;

  public MaterialTerrainTooltipsService terrainTooltips(final boolean enabled2) {
    this.enabled2 = enabled2;
    return this;
  }

  public boolean terrainTooltips() {
    return this.enabled2;
  }

  @Override
  protected void draw(
      final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
    compositorPushPresentationScaleService.requestFontFamily(
        "material-roboto",
        TextTextureService.FontSource.CLASSPATH,
        "/assets/ellice/fonts/material/Roboto-Regular.ttf");
    compositorPushPresentationScaleService.requestFontFamily(
        "material-roboto-medium",
        TextTextureService.FontSource.CLASSPATH,
        "/assets/ellice/fonts/material/Roboto-Medium.ttf");
    final boolean enabled =
        compositorPushPresentationScaleService.isFontFamilyReady("material-roboto")
            && compositorPushPresentationScaleService.isFontFamilyReady("material-roboto-medium");
    if (enabled != this.enabled) {
      this.enabled = enabled;
      this.invalidateLayout();
    }
    super.draw(compositorPushPresentationScaleService);
  }
}
