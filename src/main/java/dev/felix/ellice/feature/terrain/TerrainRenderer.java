package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.text.TextData;
import dev.felix.ellice.ui.text.TextMode;
import dev.felix.ellice.ui.text.TextTextureService;

public final class TerrainRenderer {
  public static final String FONT = "ellice-navigation";
  private static final TextData textData = TextData.NONE.withFontFamily("ellice-navigation");

  private TerrainRenderer() {}

  public static boolean ready(
      CompositorPushPresentationScaleService compositorPushPresentationScale) {
    compositorPushPresentationScale.requestFontFamily(
        "ellice-navigation",
        TextTextureService.FontSource.CLASSPATH,
        "/assets/ellice/fonts/material/ElliceNavigation.ttf");
    return compositorPushPresentationScale.isFontFamilyReady("ellice-navigation");
  }

  public static void draw(
      CompositorPushPresentationScaleService compositorPushPresentationScale,
      String currentText,
      float width,
      float height,
      float value,
      int currentValue) {
    if (ready(compositorPushPresentationScale)) {
      float nextValue =
          compositorPushPresentationScale.textWidth(
              currentText, value, TextMode.REGULAR, "ellice-navigation");
      float previousValue =
          compositorPushPresentationScale.textLineHeight(
              value, TextMode.REGULAR, "ellice-navigation");
      compositorPushPresentationScale.text(
          width + (value - nextValue) / 2.0F,
          height + (value - previousValue) / 2.0F,
          currentText,
          value,
          currentValue,
          textData);
    }
  }

  public static String symbol(String text) {
    return switch (text) {
      case "turn_left" -> "↰";
      case "turn_right" -> "↱";
      case "check" -> "✓";
      case "flag" -> "⚑";
      case "base" -> "⌂";
      case "stash" -> "▣";
      case "portal" -> "◎";
      case "danger" -> "⚠";
      case "death" -> "☠";
      case "→", "↘", "↓", "↙", "←", "↖", "↑", "↗" -> text;
      default -> "↑";
    };
  }

  public static final class GlyphNode extends ScenePctService<TerrainRenderer.GlyphNode> {
    private String text2 = "↑";
    private int count = -1;

    public TerrainRenderer.GlyphNode glyph(String text, int value) {
      this.text2 = TerrainRenderer.symbol(text);
      this.count = value;
      return this;
    }

    @Override
    protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
      float y = Math.min(this.cw, this.ch) * 0.64F;
      TerrainRenderer.draw(
          compositorPushPresentationScale,
          this.text2,
          this.cx + (this.cw - y) / 2.0F,
          this.cy + (this.ch - y) / 2.0F,
          y,
          mulAlpha(this.count, this.effectiveOpacity));
    }
  }
}
