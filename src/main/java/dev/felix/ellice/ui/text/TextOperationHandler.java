package dev.felix.ellice.ui.text;

import dev.felix.ellice.render.rhi.RhiBlendStateService;

interface TextOperationHandler {
  RhiBlendStateService.TextureHandle texture();

  float pixelRange();

  float lineHeight();

  TextTextureService.GlyphInfo glyph(int value);

  boolean isReady();

  default boolean colorGlyph(TextTextureService.GlyphInfo glyphInfo) {
    return false;
  }
}
