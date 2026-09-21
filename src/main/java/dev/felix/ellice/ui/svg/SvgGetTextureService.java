package dev.felix.ellice.ui.svg;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.renderer.awt.NullPlatformSupport;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public final class SvgGetTextureService {
  public static final float ICON_SDF_PX_RANGE = 12.0F;
  private final Map<String, RhiBlendStateService.TextureHandle> text = new HashMap<>();
  private final SVGLoader sVGLoader = new SVGLoader();
  private final LoaderContext loaderContext = LoaderContext.builder().build();
  private RhiOperationHandler rhiOperationHandler;

  public void init(RhiOperationHandler rhiOperation) {
    this.rhiOperationHandler = rhiOperation;
  }

  public RhiBlendStateService.TextureHandle getTexture(
      Path path, int textureId, int currentTextureId) {
    String currentText = path.toAbsolutePath() + "@" + textureId + "x" + currentTextureId;
    RhiBlendStateService.TextureHandle textureHandle = this.text.get(currentText);
    if (textureHandle != null) {
      return textureHandle;
    }

    try (InputStream inputStream = Files.newInputStream(path)) {
      return this.createTextureHandle(
          inputStream, path.toUri(), currentText, textureId, currentTextureId);
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn("SVG load failed: {}", path.getFileName(), exception);
      this.text.put(currentText, RhiBlendStateService.TextureHandle.NONE);
      return RhiBlendStateService.TextureHandle.NONE;
    }
  }

  public RhiBlendStateService.TextureHandle getSdfTexture(Path path) {
    return this.getSdfTexture(path, 12.0F);
  }

  public RhiBlendStateService.TextureHandle getSdfTexture(Path path, float value) {
    if (Float.isFinite(value) && !(value <= 0.0F)) {
      String currentText = path.toAbsolutePath() + "@sdf-" + value;
      RhiBlendStateService.TextureHandle textureHandle = this.text.get(currentText);
      if (textureHandle != null) {
        return textureHandle;
      }

      try (InputStream inputStream = Files.newInputStream(path)) {
        RhiBlendStateService.TextureHandle currentTextureHandle =
            this.createTextureHandle2(inputStream, path.toUri(), currentText, value);
        this.text.put(currentText, currentTextureHandle);
        return currentTextureHandle;
      } catch (Exception exception) {
        CoreIsInitializedHandler.LOGGER.warn(
            "SVG SDF load failed: {}", path.getFileName(), exception);
        this.text.put(currentText, RhiBlendStateService.TextureHandle.NONE);
        return RhiBlendStateService.TextureHandle.NONE;
      }
    } else {
      throw new IllegalArgumentException("SDF range must be positive");
    }
  }

  public RhiBlendStateService.TextureHandle getClasspathTexture(
      String path, int textureId, int currentTextureId) {
    String currentPath = "cp:" + path + "@" + textureId + "x" + currentTextureId;
    RhiBlendStateService.TextureHandle textureHandle = this.text.get(currentPath);
    if (textureHandle != null) {
      return textureHandle;
    }

    try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
      if (inputStream == null) {
        this.text.put(currentPath, RhiBlendStateService.TextureHandle.NONE);
        return RhiBlendStateService.TextureHandle.NONE;
      } else {
        return this.createTextureHandle(
            inputStream, null, currentPath, textureId, currentTextureId);
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn("SVG classpath load failed: {}", path, exception);
      this.text.put(currentPath, RhiBlendStateService.TextureHandle.NONE);
      return RhiBlendStateService.TextureHandle.NONE;
    }
  }

  private RhiBlendStateService.TextureHandle createTextureHandle(
      InputStream inputStream, URI uRI, String currentText, int textureId, int currentTextureId) {
    try {
      SVGDocument sVGDocument = this.sVGLoader.load(inputStream, uRI, this.loaderContext);
      if (sVGDocument == null) {
        this.text.put(currentText, RhiBlendStateService.TextureHandle.NONE);
        return RhiBlendStateService.TextureHandle.NONE;
      } else {
        int nextTextureId = textureId * 2;
        int previousTextureId = currentTextureId * 2;
        BufferedImage bufferedImage = new BufferedImage(nextTextureId, previousTextureId, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(
            RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        sVGDocument.renderWithPlatform(
            NullPlatformSupport.INSTANCE,
            graphics2D,
            new ViewBox(0.0F, 0.0F, nextTextureId, previousTextureId));
        graphics2D.dispose();
        RhiBlendStateService.TextureHandle textureHandle = this.createTextureHandle3(bufferedImage);
        this.text.put(currentText, textureHandle);
        return textureHandle;
      }
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn("SVG rasterize failed: {}", currentText, exception);
      this.text.put(currentText, RhiBlendStateService.TextureHandle.NONE);
      return RhiBlendStateService.TextureHandle.NONE;
    }
  }

  private RhiBlendStateService.TextureHandle createTextureHandle2(
      InputStream inputStream, URI uRI, String text, float value) {
    try {
      SVGDocument sVGDocument = this.sVGLoader.load(inputStream, uRI, this.loaderContext);
      if (sVGDocument == null) {
        return RhiBlendStateService.TextureHandle.NONE;
      }

      short shortValue = 192;
      short currentShortValue = 192;
      BufferedImage bufferedImage = new BufferedImage(shortValue, currentShortValue, 2);
      Graphics2D graphics2D = bufferedImage.createGraphics();
      graphics2D.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics2D.setRenderingHint(
          RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      graphics2D.setRenderingHint(
          RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      sVGDocument.renderWithPlatform(
          NullPlatformSupport.INSTANCE,
          graphics2D,
          new ViewBox(0.0F, 0.0F, shortValue, currentShortValue));
      graphics2D.dispose();
      return this.createTextureHandle4(bufferedImage, shortValue, currentShortValue, value);
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.warn("SVG SDF rasterize failed: {}", text, exception);
      return RhiBlendStateService.TextureHandle.NONE;
    }
  }

  private RhiBlendStateService.TextureHandle createTextureHandle3(BufferedImage bufferedImage) {
    if (this.rhiOperationHandler == null) {
      return RhiBlendStateService.TextureHandle.NONE;
    }

    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);
    int[] ints = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

    for (int textureId : ints) {
      byteBuffer.put((byte) (textureId >> 16 & 0xFF));
      byteBuffer.put((byte) (textureId >> 8 & 0xFF));
      byteBuffer.put((byte) (textureId & 0xFF));
      byteBuffer.put((byte) (textureId >> 24 & 0xFF));
    }

    byteBuffer.flip();
    return this.rhiOperationHandler.createTexture(
        new RhiBlendStateService.TextureDescriptor(
            width,
            height,
            RhiBlendStateService.TextureFormat.RGBA8,
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.AddressMode.CLAMP),
        byteBuffer);
  }

  private RhiBlendStateService.TextureHandle createTextureHandle4(
      BufferedImage bufferedImage, int textureId, int currentTextureId, float value) {
    if (this.rhiOperationHandler == null) {
      return RhiBlendStateService.TextureHandle.NONE;
    }

    float[] floats = createFloat(bufferedImage, textureId, currentTextureId, value);
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(textureId * currentTextureId * 4);

    for (float currentValue : floats) {
      int nextTextureId = Math.max(0, Math.min(255, Math.round(currentValue * 255.0F)));
      byteBuffer.put((byte) nextTextureId);
      byteBuffer.put((byte) nextTextureId);
      byteBuffer.put((byte) nextTextureId);
      byteBuffer.put((byte) -1);
    }

    byteBuffer.flip();
    return this.rhiOperationHandler.createTexture(
        new RhiBlendStateService.TextureDescriptor(
            textureId,
            currentTextureId,
            RhiBlendStateService.TextureFormat.RGBA8,
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.AddressMode.CLAMP),
        byteBuffer);
  }

  private static float[] createFloat(
      BufferedImage bufferedImage, int value, int currentValue, float nextValue) {
    float previousValue = 1.0E10F;
    float[] floats = new float[value * currentValue];
    float[] currentFloats = new float[value * currentValue];

    for (int index = 0; index < currentValue; index++) {
      for (int currentIndex = 0; currentIndex < value; currentIndex++) {
        int sourceValue = bufferedImage.getRGB(currentIndex, index);
        float targetValue = (sourceValue >>> 24 & 0xFF) / 255.0F;
        int nextIndex = index * value + currentIndex;
        float inputValue = Math.max(0.0F, 0.5F - targetValue);
        float outputValue = Math.max(0.0F, targetValue - 0.5F);
        floats[nextIndex] = targetValue == 0.0F ? previousValue : inputValue * inputValue;
        currentFloats[nextIndex] = targetValue == 1.0F ? previousValue : outputValue * outputValue;
      }
    }

    updateState(floats, value, currentValue);
    updateState(currentFloats, value, currentValue);
    float[] nextFloats = new float[value * currentValue];

    for (int previousIndex = 0; previousIndex < nextFloats.length; previousIndex++) {
      float resultValue =
          (float) (Math.sqrt(floats[previousIndex]) - Math.sqrt(currentFloats[previousIndex]));
      nextFloats[previousIndex] = Math.max(0.0F, Math.min(1.0F, 0.5F - resultValue / nextValue));
    }

    return nextFloats;
  }

  private static void updateState(float[] floats, int value, int currentValue) {
    int index = Math.max(value, currentValue);
    float[] currentFloats = new float[index];
    float[] nextFloats = new float[index];
    int[] ints = new int[index];
    float[] previousFloats = new float[index + 1];

    for (int currentIndex = 0; currentIndex < value; currentIndex++) {
      for (int nextIndex = 0; nextIndex < currentValue; nextIndex++) {
        currentFloats[nextIndex] = floats[nextIndex * value + currentIndex];
      }

      updateState2(currentFloats, nextFloats, ints, previousFloats, currentValue);

      for (int previousIndex = 0; previousIndex < currentValue; previousIndex++) {
        floats[previousIndex * value + currentIndex] = nextFloats[previousIndex];
      }
    }

    for (int sourceIndex = 0; sourceIndex < currentValue; sourceIndex++) {
      for (int targetIndex = 0; targetIndex < value; targetIndex++) {
        currentFloats[targetIndex] = floats[sourceIndex * value + targetIndex];
      }

      updateState2(currentFloats, nextFloats, ints, previousFloats, value);

      for (int inputIndex = 0; inputIndex < value; inputIndex++) {
        floats[sourceIndex * value + inputIndex] = nextFloats[inputIndex];
      }
    }
  }

  private static void updateState2(
      float[] floats, float[] currentFloats, int[] ints, float[] nextFloats, int value) {
    int index = -1;

    for (int currentIndex = 0; currentIndex < value; currentIndex++) {
      if (!(floats[currentIndex] >= 1.0E9F)) {
        float currentValue;
        while (true) {
          if (index < 0) {
            currentValue = -3.4028235E38F;
            break;
          }

          int nextIndex = ints[index];
          currentValue =
              (floats[currentIndex]
                      + currentIndex * currentIndex
                      - (floats[nextIndex] + nextIndex * nextIndex))
                  / (2.0F * currentIndex - 2.0F * nextIndex);
          if (currentValue > nextFloats[index]) {
            break;
          }

          index += -1;
        }

        ints[++index] = currentIndex;
        nextFloats[index] = index == 0 ? -3.4028235E38F : currentValue;
        nextFloats[index + 1] = 3.4028235E38F;
      }
    }

    if (index < 0) {
      for (int previousIndex = 0; previousIndex < value; previousIndex++) {
        currentFloats[previousIndex] = 1.0E10F;
      }
    } else {
      index = 0;

      for (int sourceIndex = 0; sourceIndex < value; sourceIndex++) {
        while (nextFloats[index + 1] < sourceIndex) {
          index++;
        }

        float nextValue = sourceIndex - ints[index];
        currentFloats[sourceIndex] = nextValue * nextValue + floats[ints[index]];
      }
    }
  }

  public void clearCache() {
    if (this.rhiOperationHandler != null) {
      for (RhiBlendStateService.TextureHandle textureHandle : this.text.values()) {
        if (textureHandle.valid()) {
          this.rhiOperationHandler.destroyTexture(textureHandle);
        }
      }
    }

    this.text.clear();
  }

  public void shutdown() {
    this.clearCache();
  }

  public boolean isInitialized() {
    return this.rhiOperationHandler != null;
  }
}
