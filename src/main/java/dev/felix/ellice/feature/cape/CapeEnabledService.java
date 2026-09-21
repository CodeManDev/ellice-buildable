package dev.felix.ellice.feature.cape;

import java.awt.image.BufferedImage;

public final class CapeEnabledService {
  private static boolean enabled2;
  private static CapeSizeService capeSizeService;
  private static CapePreviewRenderer.Style renderer = CapePreviewRenderer.Style.defaults();
  private static final CapePositionService capePositionService = new CapePositionService();
  private static BufferedImage bufferedImage;
  private static int count = -1;

  private CapeEnabledService() {}

  public static void enabled(boolean currentEnabled) {
    enabled2 = currentEnabled;
  }

  public static boolean active() {
    return enabled2 && capeSizeService != null;
  }

  public static void apply(CapeSizeService capeSize, CapePreviewRenderer.Style style) {
    capeSizeService = capeSize;
    renderer = style;
    count = -1;
    bufferedImage = null;
    capePositionService.seek(0.0, System.nanoTime());
    capePositionService.speed(renderer.speed(), System.nanoTime());
  }

  public static BufferedImage frame() {
    if (!active()) {
      return null;
    }

    int value = capeSizeService.frameAt(capePositionService.position(System.nanoTime()));
    if (value != count || bufferedImage == null) {
      bufferedImage = CapePreviewRenderer.render(capeSizeService.frame(value), renderer);
      count = value;
    }

    return bufferedImage;
  }
}
