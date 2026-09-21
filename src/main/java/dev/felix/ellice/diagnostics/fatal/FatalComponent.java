package dev.felix.ellice.diagnostics.fatal;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

final class FatalComponent {
  private static final Map<Integer, FatalComponent.Glyph[]> entries = createMap();

  static boolean available(int value) {
    return entries.containsKey(value);
  }

  static int width(String id, int currentWidth) {
    FatalComponent.Glyph[] glyphs = entries.get(currentWidth);
    int nextWidth = 0;

    for (int index = 0; index < id.length(); index++) {
      nextWidth += createGlyph(glyphs, id.charAt(index)).advance;
    }

    return nextWidth;
  }

  static void draw(
      FatalScreenCanvas fatalScreenCanvas,
      int y,
      int currentWidth,
      String text,
      int value,
      int currentValue) {
    FatalComponent.Glyph[] glyphs = entries.get(value);

    for (int index = 0; index < text.length(); index++) {
      FatalComponent.Glyph glyph = createGlyph(glyphs, text.charAt(index));

      for (int currentIndex = 0; currentIndex < glyph.height; currentIndex++) {
        for (int nextIndex = 0; nextIndex < glyph.width; nextIndex++) {
          int nextValue = y + nextIndex - glyph.inset;
          int previousValue = currentWidth + currentIndex;
          int sourceValue = glyph.alpha[currentIndex * glyph.width + nextIndex] & 255;
          if (sourceValue != 0
              && nextValue >= 0
              && previousValue >= 0
              && nextValue < fatalScreenCanvas.w
              && previousValue < fatalScreenCanvas.h) {
            int previousIndex = previousValue * fatalScreenCanvas.w + nextValue;
            fatalScreenCanvas.px[previousIndex] =
                blend(fatalScreenCanvas.px[previousIndex], currentValue, sourceValue);
          }
        }
      }

      y += glyph.advance;
    }
  }

  static int blend(int value, int currentValue, int nextValue) {
    int previousValue = 255 - nextValue;
    int sourceValue =
        ((value >> 16 & 0xFF) * previousValue + (currentValue >> 16 & 0xFF) * nextValue) / 255;
    int targetValue =
        ((value >> 8 & 0xFF) * previousValue + (currentValue >> 8 & 0xFF) * nextValue) / 255;
    int inputValue = ((value & 0xFF) * previousValue + (currentValue & 0xFF) * nextValue) / 255;
    return 0xFF000000 | sourceValue << 16 | targetValue << 8 | inputValue;
  }

  private static FatalComponent.Glyph createGlyph(FatalComponent.Glyph[] glyphs, char character) {
    return glyphs[(character >= ' ' && character < 256 ? character : '?') - ' '];
  }

  private static Map<Integer, FatalComponent.Glyph[]> createMap() {
    try (InputStream inputStream =
        FatalComponent.class.getResourceAsStream("/assets/ellice/fonts/crash-ui.bin.gz")) {
      if (inputStream == null) {
        return Map.of();
      }

      try (DataInputStream dataInputStream =
          new DataInputStream(new GZIPInputStream(inputStream))) {
        if (dataInputStream.readInt() != 1162625862) {
          return Map.of();
        }

        HashMap hashMap = new HashMap();
        int value = dataInputStream.readInt();
        if (value != 12) {
          return Map.of();
        }

        for (int index = 0; index < value; index++) {
          int currentValue = dataInputStream.readInt();
          int currentIndex = dataInputStream.readInt();
          if (currentValue < 121 || currentValue > 284 || currentIndex != 224) {
            return Map.of();
          }

          FatalComponent.Glyph[] glyphs = new FatalComponent.Glyph[currentIndex];

          for (int nextIndex = 0; nextIndex < currentIndex; nextIndex++) {
            int nextValue = dataInputStream.readInt();
            int previousValue = dataInputStream.readInt();
            int sourceValue = dataInputStream.readInt();
            int targetValue = dataInputStream.readInt();
            if (previousValue < 1 || sourceValue < 1 || previousValue > 224 || sourceValue > 224) {
              return Map.of();
            }

            byte[] bytes = dataInputStream.readNBytes(previousValue * sourceValue);
            if (bytes.length != previousValue * sourceValue) {
              return Map.of();
            }

            glyphs[nextIndex] =
                new FatalComponent.Glyph(nextValue, previousValue, sourceValue, targetValue, bytes);
          }

          hashMap.put(currentValue, glyphs);
        }

        return Map.copyOf(hashMap);
      }
    } catch (Throwable exception) {
      return Map.of();
    }
  }

  private record Glyph(int advance, int width, int height, int inset, byte[] alpha) {}
}
