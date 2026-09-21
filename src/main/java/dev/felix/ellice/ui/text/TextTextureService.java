package dev.felix.ellice.ui.text;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.system.MemoryUtil;

public class TextTextureService implements TextOperationHandler {
  private static final String text2 = "/assets/ellice/fonts/inter";
  private static final int count = 1024;
  static final int RENDER_SIZE = 96;
  private static final float value = 6.0F;
  private static final int count2 = (int) Math.ceil(6.0) + 1;
  private final TextMode textMode;
  private final TextTextureService.FontSource fontSource;
  private final String text3;
  private final String text4;
  private final String text5;
  private Font font2;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private final Map<Integer, TextTextureService.GlyphInfo> entries = new HashMap<>();
  private float value2 = 6.0F;
  private float value3;
  private float value4;
  private byte[] byte2;
  private int count3;
  private int count4;
  private volatile boolean enabled;

  public TextTextureService() {
    this(TextMode.REGULAR);
  }

  public TextTextureService(TextMode textMode) {
    this(
        textMode,
        "/assets/ellice/fonts/inter",
        "/assets/ellice/fonts/inter" + textMode.msdfSuffix() + ".ttf");
  }

  public TextTextureService(TextMode currentTextMode, String text, String currentText) {
    this.textMode = currentTextMode;
    this.fontSource = TextTextureService.FontSource.CLASSPATH;
    this.text3 = text;
    this.text4 = currentText;
    this.text5 = null;
  }

  public TextTextureService(
      TextMode currentTextMode, TextTextureService.FontSource currentFontSource, String text) {
    this.textMode = currentTextMode;
    this.fontSource = currentFontSource;
    this.text3 = null;
    this.text4 = null;
    this.text5 = text;
  }

  @Override
  public RhiBlendStateService.TextureHandle texture() {
    return this.textureHandle;
  }

  @Override
  public float pixelRange() {
    return this.value2;
  }

  @Override
  public float lineHeight() {
    return this.value3;
  }

  public float ascent() {
    return this.value4;
  }

  public TextTextureService.GlyphInfo glyph(char character) {
    return this.entries.get(Integer.valueOf(character));
  }

  @Override
  public TextTextureService.GlyphInfo glyph(int value) {
    return this.entries.get(value);
  }

  @Override
  public boolean isReady() {
    return this.textureHandle.valid();
  }

  public TextMode variant() {
    return this.textMode;
  }

  public void generate(RhiOperationHandler rhiOperation) {
    if (this.text3 != null) {
      try {
        if (this.checkCondition(rhiOperation)) {
          CoreIsInitializedHandler.LOGGER.info(
              "MSDF atlas loaded ({}): {} glyphs", this.textMode, this.entries.size());
          return;
        }
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.warn(
            "MSDF atlas load failed for {}, falling back to runtime SDF", this.textMode, exception);
      }
    }

    try {
      this.prepare();
      this.uploadStaged(rhiOperation);
      CoreIsInitializedHandler.LOGGER.info(
          "Runtime SDF atlas generated ({}): {} glyphs", this.createText(), this.entries.size());
    } catch (Exception currentException) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to generate font atlas for {}", this.createText(), currentException);
    }
  }

  public void prepare() {
    try {
      this.updateState();
      this.enabled = true;
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error(
          "Failed to rasterize font atlas for {}", this.createText(), exception);
      this.enabled = false;
    }
  }

  public void uploadStaged(RhiOperationHandler rhiOperation) {
    if (this.enabled && this.byte2 != null) {
      ByteBuffer currentLength = MemoryUtil.memAlloc(this.byte2.length);
      currentLength.put(this.byte2).flip();
      RhiBlendStateService.TextureHandle currentTextureHandle =
          rhiOperation.createTexture(
              new RhiBlendStateService.TextureDescriptor(
                  this.count3,
                  this.count4,
                  RhiBlendStateService.TextureFormat.RGBA8,
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.AddressMode.CLAMP),
              currentLength);
      MemoryUtil.memFree(currentLength);
      if (this.textureHandle.valid()) {
        rhiOperation.destroyTexture(this.textureHandle);
      }

      this.textureHandle = currentTextureHandle;
      this.byte2 = null;
    }
  }

  private String createText() {
    return switch (this.fontSource) {
      case CLASSPATH -> this.text4 + " (" + this.textMode + ")";
      case FILESYSTEM -> this.text5 + " (" + this.textMode + ")";
      case SYSTEM_NAME -> "system:" + this.text5 + " (" + this.textMode + ")";
    };
  }

  private boolean checkCondition(RhiOperationHandler rhiOperation) throws Exception {
    String text = this.text3 + this.textMode.msdfSuffix() + "-msdf.png";
    String currentText = this.text3 + this.textMode.msdfSuffix() + "-msdf.json";
    InputStream inputStream = TextTextureService.class.getResourceAsStream(text);
    InputStream currentInputStream = TextTextureService.class.getResourceAsStream(currentText);
    if (inputStream != null && currentInputStream != null) {
      JsonObject jsonObject;
      try (InputStreamReader inputStreamReader =
          new InputStreamReader(currentInputStream, StandardCharsets.UTF_8)) {
        jsonObject = JsonParser.parseReader(inputStreamReader).getAsJsonObject();
      }

      JsonObject currentJsonObject = jsonObject.getAsJsonObject("atlas");
      int value = currentJsonObject.get("width").getAsInt();
      int currentValue = currentJsonObject.get("height").getAsInt();
      this.value2 = currentJsonObject.get("distanceRange").getAsFloat();
      boolean enabled =
          "bottom"
              .equals(
                  currentJsonObject.has("yOrigin")
                      ? currentJsonObject.get("yOrigin").getAsString()
                      : "top");
      JsonObject nextJsonObject = jsonObject.getAsJsonObject("metrics");
      this.value3 = nextJsonObject.get("lineHeight").getAsFloat();
      this.value4 = nextJsonObject.get("ascender").getAsFloat();

      for (JsonElement jsonElement : jsonObject.getAsJsonArray("glyphs")) {
        JsonObject previousJsonObject = jsonElement.getAsJsonObject();
        int nextValue = previousJsonObject.get("unicode").getAsInt();
        float previousValue = previousJsonObject.get("advance").getAsFloat();
        if (previousJsonObject.has("atlasBounds") && previousJsonObject.has("planeBounds")) {
          JsonObject sourceJsonObject = previousJsonObject.getAsJsonObject("atlasBounds");
          JsonObject targetJsonObject = previousJsonObject.getAsJsonObject("planeBounds");
          float sourceValue = sourceJsonObject.get("left").getAsFloat();
          float targetValue = sourceJsonObject.get("bottom").getAsFloat();
          float inputValue = sourceJsonObject.get("right").getAsFloat();
          float outputValue = sourceJsonObject.get("top").getAsFloat();
          float resultValue = sourceValue / value;
          float candidateValue = inputValue / value;
          float selectedValue;
          float defaultValue;
          if (enabled) {
            selectedValue = 1.0F - outputValue / currentValue;
            defaultValue = 1.0F - targetValue / currentValue;
          } else {
            selectedValue = outputValue / currentValue;
            defaultValue = targetValue / currentValue;
          }

          float initialValue = targetJsonObject.get("left").getAsFloat();
          float resolvedValue = targetJsonObject.get("bottom").getAsFloat();
          float computedValue = targetJsonObject.get("right").getAsFloat();
          float cachedValue = targetJsonObject.get("top").getAsFloat();
          this.entries.put(
              nextValue,
              new TextTextureService.GlyphInfo(
                  resultValue,
                  selectedValue,
                  candidateValue,
                  defaultValue,
                  computedValue - initialValue,
                  cachedValue - resolvedValue,
                  initialValue,
                  this.value4 - cachedValue,
                  previousValue));
        } else {
          this.entries.put(
              nextValue,
              new TextTextureService.GlyphInfo(
                  0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, previousValue));
        }
      }

      BufferedImage bufferedImage;
      try (inputStream) {
        bufferedImage = ImageIO.read(inputStream);
      }

      if (bufferedImage == null) {
        return false;
      }

      if (value > 0 && currentValue > 0) {
        long longValue = (long) value * currentValue;
        if (longValue > 67108864L) {
          return false;
        }

        ByteBuffer byteBuffer = MemoryUtil.memAlloc(value * currentValue * 4);

        for (int index = 0; index < currentValue; index++) {
          for (int currentIndex = 0; currentIndex < value; currentIndex++) {
            int pendingValue = bufferedImage.getRGB(currentIndex, index);
            byteBuffer.put((byte) (pendingValue >> 16 & 0xFF));
            byteBuffer.put((byte) (pendingValue >> 8 & 0xFF));
            byteBuffer.put((byte) (pendingValue & 0xFF));
            byteBuffer.put((byte) -1);
          }
        }

        byteBuffer.flip();
        RhiBlendStateService.TextureHandle currentTextureHandle =
            rhiOperation.createTexture(
                new RhiBlendStateService.TextureDescriptor(
                    value,
                    currentValue,
                    RhiBlendStateService.TextureFormat.RGBA8,
                    RhiBlendStateService.FilterMode.LINEAR,
                    RhiBlendStateService.FilterMode.LINEAR,
                    RhiBlendStateService.AddressMode.CLAMP),
                byteBuffer);
        MemoryUtil.memFree(byteBuffer);
        if (this.textureHandle.valid()) {
          rhiOperation.destroyTexture(this.textureHandle);
        }

        this.textureHandle = currentTextureHandle;
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void updateState() {
    Font font = this.createFont2();
    this.value2 = 12.0F;
    this.entries.clear();
    FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
    LineMetrics lineMetrics = font.getLineMetrics("Hg", fontRenderContext);
    float value = lineMetrics.getAscent();
    this.value4 = value / 96.0F;
    this.value3 = lineMetrics.getHeight() / 96.0F;
    int currentValue = 0;
    int nextValue = 0;
    int previousValue = 0;

    for (char index = '!'; index < 127; index++) {
      Rectangle rectangle =
          font.createGlyphVector(fontRenderContext, new char[] {index})
              .getPixelBounds(fontRenderContext, 0.0F, 0.0F);
      int sourceValue = Math.max(1, rectangle.width) + count2 * 2;
      int targetValue = Math.max(1, rectangle.height) + count2 * 2;
      if (currentValue + sourceValue > 1024) {
        currentValue = 0;
        nextValue += previousValue + 1;
        previousValue = 0;
      }

      currentValue += sourceValue + 1;
      previousValue = Math.max(previousValue, targetValue);
    }

    short shortValue = 1024;

    while (shortValue < nextValue + previousValue) {
      shortValue *= 2;
    }

    byte[] bytes = new byte[1024 * shortValue * 4];
    int inputValue = 0;
    int outputValue = 0;
    int resultValue = 0;
    float candidateValue = 0.010416667F;

    for (char currentIndex = ' '; currentIndex < 127; currentIndex++) {
      float currentWidth =
          (float) font.getStringBounds(String.valueOf(currentIndex), fontRenderContext).getWidth()
              * candidateValue;
      if (currentIndex == ' ') {
        this.entries.put(
            Integer.valueOf(currentIndex),
            new TextTextureService.GlyphInfo(
                0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, currentWidth));
      } else {
        GlyphVector glyphVector =
            font.createGlyphVector(fontRenderContext, new char[] {currentIndex});
        Rectangle currentRectangle = glyphVector.getPixelBounds(fontRenderContext, 0.0F, 0.0F);
        int selectedValue = Math.max(1, currentRectangle.width) + count2 * 2;
        int defaultValue = Math.max(1, currentRectangle.height) + count2 * 2;
        if (selectedValue <= 1024) {
          if (inputValue + selectedValue > 1024) {
            inputValue = 0;
            outputValue += resultValue + 1;
            resultValue = 0;
          }

          if (outputValue + defaultValue > shortValue) {
            throw new IllegalStateException("Font shelf pack exceeded its measured height");
          }

          BufferedImage bufferedImage = new BufferedImage(selectedValue, defaultValue, 10);
          Graphics2D graphics2D = bufferedImage.createGraphics();
          graphics2D.setRenderingHint(
              RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          graphics2D.setRenderingHint(
              RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
          graphics2D.setRenderingHint(
              RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
          graphics2D.setFont(font);
          graphics2D.setColor(Color.WHITE);
          graphics2D.drawGlyphVector(
              glyphVector, count2 - currentRectangle.x, count2 - currentRectangle.y);
          graphics2D.dispose();
          float[] floats = this.createFloat(bufferedImage, selectedValue, defaultValue);

          for (int nextIndex = 0; nextIndex < defaultValue; nextIndex++) {
            for (int previousIndex = 0; previousIndex < selectedValue; previousIndex++) {
              int sourceIndex = ((outputValue + nextIndex) * 1024 + inputValue + previousIndex) * 4;
              if (sourceIndex + 3 < bytes.length) {
                byte byteValue =
                    (byte) Math.round(floats[nextIndex * selectedValue + previousIndex] * 255.0F);
                bytes[sourceIndex] = byteValue;
                bytes[sourceIndex + 1] = byteValue;
                bytes[sourceIndex + 2] = byteValue;
                bytes[sourceIndex + 3] = -1;
              }
            }
          }

          this.entries.put(
              Integer.valueOf(currentIndex),
              new TextTextureService.GlyphInfo(
                  inputValue / 1024.0F,
                  (float) outputValue / shortValue,
                  (inputValue + selectedValue) / 1024.0F,
                  (float) (outputValue + defaultValue) / shortValue,
                  selectedValue * candidateValue,
                  defaultValue * candidateValue,
                  (currentRectangle.x - count2) * candidateValue,
                  (value + currentRectangle.y - count2) * candidateValue,
                  currentWidth));
          inputValue += selectedValue + 1;
          resultValue = Math.max(resultValue, defaultValue);
        }
      }
    }

    this.byte2 = bytes;
    this.count3 = 1024;
    this.count4 = shortValue;
  }

  Font font(float value, TextMode currentTextMode) {
    if ("/assets/ellice/fonts/inter".equals(this.text3) && currentTextMode != this.textMode) {
      return new TextTextureService(currentTextMode).font(value, currentTextMode);
    }

    Font currentFont = this.createFont3();
    return "/assets/ellice/fonts/inter".equals(this.text3)
        ? currentFont.deriveFont(value)
        : currentFont.deriveFont(currentFont.getStyle() | currentTextMode.awtStyle(), value);
  }

  private Font createFont2() {
    return this.font(96.0F, this.textMode);
  }

  private synchronized Font createFont3() {
    if (this.font2 != null) {
      return this.font2;
    }

    try {
      Font font =
          switch (this.fontSource) {
            case CLASSPATH -> {
              Font currentFont;
              try (InputStream inputStream =
                  TextTextureService.class.getResourceAsStream(this.text4)) {
                currentFont = inputStream != null ? Font.createFont(0, inputStream) : null;
              }

              yield currentFont;
            }
            case FILESYSTEM -> {
              Font nextFont;
              try (InputStream currentInputStream = Files.newInputStream(Path.of(this.text5))) {
                nextFont = Font.createFont(0, currentInputStream);
              }

              yield nextFont;
            }
            case SYSTEM_NAME -> {
              Font previousFont = new Font(this.text5, 0, 96);
              yield previousFont;
            }
          };
      if (font != null) {
        this.font2 = font;
        return font;
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn(
          "Font load failed (source={}, ref={}), using SansSerif",
          new Object[] {
            this.fontSource,
            this.fontSource == TextTextureService.FontSource.CLASSPATH ? this.text4 : this.text5,
            exception
          });
    }

    this.font2 = new Font("SansSerif", this.textMode.awtStyle(), 96);
    return this.font2;
  }

  private float[] createFloat(BufferedImage bufferedImage, int value, int currentValue) {
    float nextValue = 1.0E10F;
    float[] floats = new float[value * currentValue];
    float[] currentFloats = new float[value * currentValue];

    for (int index = 0; index < currentValue; index++) {
      for (int currentIndex = 0; currentIndex < value; currentIndex++) {
        float previousValue = bufferedImage.getRaster().getSample(currentIndex, index, 0) / 255.0F;
        int nextIndex = index * value + currentIndex;
        if (previousValue >= 1.0F) {
          floats[nextIndex] = 0.0F;
          currentFloats[nextIndex] = nextValue;
        } else if (previousValue <= 0.0F) {
          floats[nextIndex] = nextValue;
          currentFloats[nextIndex] = 0.0F;
        } else {
          float sourceValue = Math.max(0.0F, 0.5F - previousValue);
          floats[nextIndex] = sourceValue * sourceValue;
          float targetValue = Math.max(0.0F, previousValue - 0.5F);
          currentFloats[nextIndex] = targetValue * targetValue;
        }
      }
    }

    updateState2(floats, value, currentValue);
    updateState2(currentFloats, value, currentValue);
    float[] nextFloats = new float[value * currentValue];

    for (int previousIndex = 0; previousIndex < value * currentValue; previousIndex++) {
      float inputValue =
          (float) (Math.sqrt(floats[previousIndex]) - Math.sqrt(currentFloats[previousIndex]));
      nextFloats[previousIndex] = Math.max(0.0F, Math.min(1.0F, 0.5F - inputValue / 12.0F));
    }

    return nextFloats;
  }

  private static void updateState2(float[] floats, int value, int currentValue) {
    int index = Math.max(value, currentValue);
    float[] currentFloats = new float[index];
    float[] nextFloats = new float[index];
    int[] ints = new int[index];
    float[] previousFloats = new float[index + 1];

    for (int currentIndex = 0; currentIndex < value; currentIndex++) {
      for (int nextIndex = 0; nextIndex < currentValue; nextIndex++) {
        currentFloats[nextIndex] = floats[nextIndex * value + currentIndex];
      }

      updateState3(currentFloats, nextFloats, ints, previousFloats, currentValue);

      for (int previousIndex = 0; previousIndex < currentValue; previousIndex++) {
        floats[previousIndex * value + currentIndex] = nextFloats[previousIndex];
      }
    }

    for (int sourceIndex = 0; sourceIndex < currentValue; sourceIndex++) {
      System.arraycopy(floats, sourceIndex * value, currentFloats, 0, value);
      updateState3(currentFloats, nextFloats, ints, previousFloats, value);
      System.arraycopy(nextFloats, 0, floats, sourceIndex * value, value);
    }
  }

  private static void updateState3(
      float[] floats, float[] currentFloats, int[] ints, float[] nextFloats, int value) {
    ints[0] = 0;
    nextFloats[0] = Float.NEGATIVE_INFINITY;
    nextFloats[1] = Float.POSITIVE_INFINITY;
    int index = 0;

    for (int currentIndex = 1; currentIndex < value; currentIndex++) {
      float currentValue;
      while (true) {
        int nextIndex = ints[index];
        currentValue =
            (floats[currentIndex]
                    + (float) currentIndex * currentIndex
                    - (floats[nextIndex] + (float) nextIndex * nextIndex))
                / (2.0F * currentIndex - 2.0F * nextIndex);
        if (currentValue > nextFloats[index]) {
          break;
        }

        index += -1;
        if (index < 0) {
          index = 0;
          break;
        }
      }

      ints[++index] = currentIndex;
      nextFloats[index] = currentValue;
      nextFloats[index + 1] = Float.POSITIVE_INFINITY;
    }

    index = 0;

    for (int previousIndex = 0; previousIndex < value; previousIndex++) {
      while (nextFloats[index + 1] < previousIndex) {
        index++;
      }

      float nextValue = previousIndex - ints[index];
      currentFloats[previousIndex] = nextValue * nextValue + floats[ints[index]];
    }
  }

  public void shutdown(RhiOperationHandler rhiOperation) {
    if (this.textureHandle.valid()) {
      rhiOperation.destroyTexture(this.textureHandle);
    }
  }

  public enum FontSource {
    CLASSPATH,
    FILESYSTEM,
    SYSTEM_NAME;

    private static TextTextureService.FontSource[] $values() {
      return new TextTextureService.FontSource[] {CLASSPATH, FILESYSTEM, SYSTEM_NAME};
    }
  }

  public record GlyphInfo(
      float u0,
      float v0,
      float u1,
      float v1,
      float width,
      float height,
      float bearingX,
      float bearingY,
      float advance) {}
}
