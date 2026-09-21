package dev.felix.ellice.feature.cape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class CapePreviewRenderer {
  public static final int SCALE = 8;
  public static final int WIDTH = 512;
  public static final int HEIGHT = 256;

  private CapePreviewRenderer() {}

  public static BufferedImage render(BufferedImage bufferedImage, CapePreviewRenderer.Style style) {
    if (style.minecraftTexture()) {
      BufferedImage currentBufferedImage = new BufferedImage(512, 256, 2);
      Graphics2D graphics2D = currentBufferedImage.createGraphics();

      try {
        graphics2D.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(bufferedImage, 0, 0, 512, 256, null);
      } finally {
        graphics2D.dispose();
      }

      return currentBufferedImage;
    } else {
      BufferedImage nextBufferedImage = new BufferedImage(80, 128, 2);
      Graphics2D currentGraphics2D = nextBufferedImage.createGraphics();

      try {
        currentGraphics2D.setColor(new Color(style.background(), true));
        currentGraphics2D.fillRect(
            0, 0, nextBufferedImage.getWidth(), nextBufferedImage.getHeight());
        currentGraphics2D.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        double width = (double) nextBufferedImage.getWidth() / bufferedImage.getWidth();
        double height = (double) nextBufferedImage.getHeight() / bufferedImage.getHeight();
        double doubleValue =
            style.fit() == CapePreviewRenderer.Fit.FIT
                ? Math.min(width, height)
                : Math.max(width, height);
        int currentWidth =
            style.fit() == CapePreviewRenderer.Fit.STRETCH
                ? nextBufferedImage.getWidth()
                : (int) Math.round(bufferedImage.getWidth() * doubleValue);
        int currentHeight =
            style.fit() == CapePreviewRenderer.Fit.STRETCH
                ? nextBufferedImage.getHeight()
                : (int) Math.round(bufferedImage.getHeight() * doubleValue);
        int nextWidth = (nextBufferedImage.getWidth() - currentWidth) / 2;
        int nextHeight = (nextBufferedImage.getHeight() - currentHeight) / 2;
        currentGraphics2D.drawImage(
            bufferedImage,
            style.mirror() ? nextWidth + currentWidth : nextWidth,
            nextHeight,
            style.mirror() ? -currentWidth : currentWidth,
            currentHeight,
            null);
      } finally {
        currentGraphics2D.dispose();
      }

      BufferedImage previousBufferedImage = new BufferedImage(512, 256, 2);
      currentGraphics2D = previousBufferedImage.createGraphics();

      try {
        currentGraphics2D.setColor(new Color(style.background(), true));
        currentGraphics2D.fillRect(0, 0, 176, 136);
        currentGraphics2D.drawImage(nextBufferedImage, 8, 8, null);
        currentGraphics2D.drawImage(nextBufferedImage, 96, 8, null);
        currentGraphics2D.drawImage(nextBufferedImage, 0, 8, 8, 136, 0, 0, 1, 128, null);
        currentGraphics2D.drawImage(nextBufferedImage, 88, 8, 96, 136, 79, 0, 80, 128, null);
        currentGraphics2D.drawImage(nextBufferedImage, 8, 0, 88, 8, 0, 0, 80, 1, null);
        currentGraphics2D.drawImage(nextBufferedImage, 88, 0, 168, 8, 0, 127, 80, 128, null);
      } finally {
        currentGraphics2D.dispose();
      }

      return previousBufferedImage;
    }
  }

  public enum Fit {
    FILL,
    FIT,
    STRETCH;

    private static CapePreviewRenderer.Fit[] $values() {
      return new CapePreviewRenderer.Fit[] {FILL, FIT, STRETCH};
    }
  }

  public record Style(
      CapePreviewRenderer.Fit fit,
      boolean minecraftTexture,
      boolean mirror,
      int background,
      double speed) {
    public Style(
        CapePreviewRenderer.Fit fit,
        boolean minecraftTexture,
        boolean mirror,
        int background,
        double speed) {
      if (fit == null) {
        fit = CapePreviewRenderer.Fit.FILL;
      }

      speed = Double.isFinite(speed) ? Math.clamp(speed, 0.25, 3.0) : 1.0;
      this.fit = fit;
      this.minecraftTexture = minecraftTexture;
      this.mirror = mirror;
      this.background = background;
      this.speed = speed;
    }

    public static CapePreviewRenderer.Style defaults() {
      return new CapePreviewRenderer.Style(
          CapePreviewRenderer.Fit.FILL, false, false, -15264476, 1.0);
    }
  }
}
