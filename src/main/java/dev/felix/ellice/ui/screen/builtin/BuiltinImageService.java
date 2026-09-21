package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public final class BuiltinImageService extends ScenePctService<BuiltinImageService>
    implements AutoCloseable {
  private Supplier<BufferedImage> supplier = () -> null;
  private BufferedImage bufferedImage;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private CompositorPushPresentationScaleService compositorPushPresentationScaleService;
  private float value = 8.0F;

  public BuiltinImageService image(Supplier<BufferedImage> currentSupplier) {
    this.supplier = currentSupplier;
    return this;
  }

  public BuiltinImageService rounding(float currentValue) {
    this.value = currentValue;
    return this;
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    this.compositorPushPresentationScaleService = compositorPushPresentationScale;
    BufferedImage currentBufferedImage = this.supplier.get();
    if (currentBufferedImage != null) {
      if (currentBufferedImage != this.bufferedImage) {
        if (this.bufferedImage != null
            && (this.bufferedImage.getWidth() != currentBufferedImage.getWidth()
                || this.bufferedImage.getHeight() != currentBufferedImage.getHeight())) {
          compositorPushPresentationScale.destroyTexture(this.textureHandle);
          this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
        }

        this.textureHandle =
            compositorPushPresentationScale.uploadImageTexture(
                this.textureHandle, currentBufferedImage);
        if (this.textureHandle.valid()) {
          this.bufferedImage = currentBufferedImage;
        }
      }

      float width =
          Math.min(
              this.cw / currentBufferedImage.getWidth(),
              this.ch / currentBufferedImage.getHeight());
      float currentWidth = currentBufferedImage.getWidth() * width;
      float height = currentBufferedImage.getHeight() * width;
      compositorPushPresentationScale.drawTexture(
          this.textureHandle,
          this.cx + (this.cw - currentWidth) / 2.0F,
          this.cy + (this.ch - height) / 2.0F,
          currentWidth,
          height,
          this.effectiveOpacity,
          this.value);
    }
  }

  @Override
  public void close() {
    if (this.compositorPushPresentationScaleService != null) {
      this.compositorPushPresentationScaleService.destroyTexture(this.textureHandle);
    }

    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.bufferedImage = null;
  }
}
