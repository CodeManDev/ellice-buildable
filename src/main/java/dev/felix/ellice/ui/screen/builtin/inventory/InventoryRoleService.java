package dev.felix.ellice.ui.screen.builtin.inventory;

import dev.felix.ellice.compat.CompatSpriteService;
import dev.felix.ellice.compat.TextureUvRegion;
import dev.felix.ellice.feature.inventory.InventoryRoleData;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.text.TextMode;

public final class InventoryRoleService extends ScenePctService<InventoryRoleService> {
  private InventoryRoleData.Role role2 = InventoryRoleData.Role.LOCKED;

  public InventoryRoleService role(InventoryRoleData.Role currentRole) {
    this.role2 = currentRole;
    return this;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    TextureUvRegion textureUvRegion = CompatSpriteService.sprite(this.role2);
    float width = Math.min(this.cw, this.ch);
    float height = this.cx + (this.cw - width) / 2.0F;
    float value = this.cy + (this.ch - width) / 2.0F;
    if (textureUvRegion != null && textureUvRegion.textureId() != 0) {
      compositorPushPresentationScale.drawTextureRegion(
          new RhiBlendStateService.TextureHandle(textureUvRegion.textureId()),
          height,
          value,
          width,
          width,
          this.effectiveOpacity,
          0.0F,
          0.0F,
          0.0F,
          0.0F,
          0,
          0.0F,
          0.0F,
          textureUvRegion.u0(),
          textureUvRegion.v0(),
          textureUvRegion.u1(),
          textureUvRegion.v1(),
          true);
    } else {
      compositorPushPresentationScale.roundedRect(
          height,
          value,
          width,
          width,
          width * 0.22F,
          mulAlpha(MaterialIsLightService.SECONDARY_CONTAINER, this.effectiveOpacity));
      String currentText =
          this.role2 == InventoryRoleData.Role.LOCKED ? "–" : this.role2.title().substring(0, 2);
      float currentValue = width * 0.4F;
      compositorPushPresentationScale.text(
          height
              + (width
                      - compositorPushPresentationScale.textWidth(
                          currentText, currentValue, TextMode.REGULAR, "material-roboto-medium"))
                  / 2.0F,
          value
              + (width
                      - compositorPushPresentationScale.textLineHeight(
                          currentValue, TextMode.REGULAR, "material-roboto-medium"))
                  / 2.0F,
          currentText,
          currentValue,
          mulAlpha(MaterialIsLightService.ON_SECONDARY_CONTAINER, this.effectiveOpacity),
          MaterialIsLightService.LABEL);
    }
  }
}
