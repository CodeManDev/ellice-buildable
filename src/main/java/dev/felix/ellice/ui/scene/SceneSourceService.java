package dev.felix.ellice.ui.scene;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.media.MediaSourceService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Objects;

public class SceneSourceService extends ScenePctService<SceneSourceService> {
  private final MediaSourceService mediaSourceService = new MediaSourceService();
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private int count;
  private int count2;
  private long timestamp;
  private Path path;
  private float value;
  private float value2 = -1.0F;
  private float value3 = -1.0F;
  private float value4 = -1.0F;
  private float value5 = -1.0F;
  private float value6;
  private int count3;
  private SceneSourceService.Fit fit2 = SceneSourceService.Fit.STRETCH;
  private boolean enabled = true;
  private boolean enabled2;
  private boolean enabled3;
  private float value7 = 60.0F;

  public SceneSourceService source(Path sourcePath) {
    if (!Objects.equals(this.path, sourcePath)) {
      this.path = sourcePath;
      this.timestamp = 0L;
      this.enabled3 = true;
    }

    this.mediaSourceService.source(sourcePath);
    return this;
  }

  public SceneSourceService cornerRadius(float currentValue) {
    this.value = currentValue;
    return this;
  }

  public SceneSourceService cornerRadiusTL(float value) {
    this.value2 = value;
    return this;
  }

  public SceneSourceService cornerRadiusTR(float value) {
    this.value3 = value;
    return this;
  }

  public SceneSourceService cornerRadiusBR(float value) {
    this.value4 = value;
    return this;
  }

  public SceneSourceService cornerRadiusBL(float value) {
    this.value5 = value;
    return this;
  }

  public SceneSourceService edgeSoftness(float value) {
    this.value6 = value;
    return this;
  }

  public SceneSourceService tintColor(int color) {
    this.count3 = color;
    return this;
  }

  public SceneSourceService fit(SceneSourceService.Fit currentFit) {
    this.fit2 = currentFit != null ? currentFit : SceneSourceService.Fit.STRETCH;
    return this;
  }

  public SceneSourceService loop(boolean enabled) {
    this.mediaSourceService.loop(enabled);
    return this;
  }

  public SceneSourceService autoplay(boolean currentEnabled) {
    this.enabled = currentEnabled;
    this.mediaSourceService.autoplay(currentEnabled);
    return this;
  }

  public SceneSourceService playing(boolean enabled) {
    this.enabled2 = enabled;
    this.mediaSourceService.playing(enabled);
    return this;
  }

  public SceneSourceService playbackRate(float value) {
    this.mediaSourceService.playbackRate(value);
    return this;
  }

  public SceneSourceService frameRate(float value) {
    this.mediaSourceService.frameRate(value);
    return this;
  }

  public SceneSourceService uploadFrameRate(float value) {
    this.value7 = Math.max(0.0F, value);
    this.mediaSourceService.outputFrameRate(this.value7);
    return this;
  }

  public SceneSourceService restart() {
    this.enabled2 = true;
    this.mediaSourceService.restart();
    return this;
  }

  @Override
  public float getAnimProperty(String text) {
    return switch (text) {
      case "cornerRadius" -> this.value;
      case "cornerRadiusTL", "cornerRadiusTopLeft" ->
          this.value2 >= 0.0F ? this.value2 : this.value;
      case "cornerRadiusTR", "cornerRadiusTopRight" ->
          this.value3 >= 0.0F ? this.value3 : this.value;
      case "cornerRadiusBR", "cornerRadiusBottomRight" ->
          this.value4 >= 0.0F ? this.value4 : this.value;
      case "cornerRadiusBL", "cornerRadiusBottomLeft" ->
          this.value5 >= 0.0F ? this.value5 : this.value;
      case "edgeSoftness" -> this.value6;
      default -> super.getAnimProperty(text);
    };
  }

  @Override
  public void setAnimProperty(String text, float currentValue) {
    switch (text) {
      case "cornerRadius":
        this.value = currentValue;
        break;
      case "cornerRadiusTL":
      case "cornerRadiusTopLeft":
        this.value2 = currentValue;
        break;
      case "cornerRadiusTR":
      case "cornerRadiusTopRight":
        this.value3 = currentValue;
        break;
      case "cornerRadiusBR":
      case "cornerRadiusBottomRight":
        this.value4 = currentValue;
        break;
      case "cornerRadiusBL":
      case "cornerRadiusBottomLeft":
        this.value5 = currentValue;
        break;
      case "edgeSoftness":
        this.value6 = currentValue;
        break;
      default:
        super.setAnimProperty(text, currentValue);
    }
  }

  @Override
  protected void draw(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    if (this.enabled3) {
      if (this.textureHandle.valid()) {
        compositorPushPresentationScale.destroyTexture(this.textureHandle);
      }

      this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
      this.count = this.count2 = 0;
      this.enabled3 = false;
    }

    if (!this.mediaSourceService.hasSource() && this.path != null) {
      this.mediaSourceService.source(this.path);
    }

    if (!this.enabled && this.enabled2) {
      this.mediaSourceService.playing(true);
    }

    if (this.cw > 0.0F && this.ch > 0.0F) {
      double y = CoreIsInitializedHandler.get().viewport().renderScale();
      this.mediaSourceService.outputSize(
          (int) Math.ceil(this.cw * y), (int) Math.ceil(this.ch * y), this.createScaleMode());
    }

    this.mediaSourceService.outputFrameRate(this.value7);
    this.updateState(compositorPushPresentationScale);
    if (this.textureHandle.valid()) {
      float[] floats = this.createFloat();
      float height = this.value2 >= 0.0F ? this.value2 : this.value;
      float currentValue = this.value3 >= 0.0F ? this.value3 : this.value;
      float nextValue = this.value4 >= 0.0F ? this.value4 : this.value;
      float previousValue = this.value5 >= 0.0F ? this.value5 : this.value;
      float sourceValue = Math.max(this.value6, this.effectiveEdgeSoftness);
      float[] currentFloats = this.createFloat2();
      compositorPushPresentationScale.drawTextureRegion(
          this.textureHandle,
          floats[0],
          floats[1],
          floats[2],
          floats[3],
          this.effectiveOpacity,
          height,
          currentValue,
          nextValue,
          previousValue,
          this.count3,
          0.0F,
          sourceValue,
          currentFloats[0],
          currentFloats[1],
          currentFloats[2],
          currentFloats[3]);
    }
  }

  private void updateState(CompositorPushPresentationScaleService compositorPushPresentationScale) {
    MediaSourceService.DecodedFrame decodedFrame = this.mediaSourceService.latestFrame();
    if (decodedFrame != null && decodedFrame.version() != this.timestamp) {
      int currentWidth = decodedFrame.width();
      int currentHeight = decodedFrame.height();
      if (currentWidth > 0 && currentHeight > 0) {
        ByteBuffer byteBuffer = decodedFrame.rgba();
        if (byteBuffer != null) {
          if (this.textureHandle.valid()
              && (this.count != currentWidth || this.count2 != currentHeight)) {
            compositorPushPresentationScale.destroyTexture(this.textureHandle);
            this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
            this.count = this.count2 = 0;
          }

          RhiBlendStateService.TextureHandle currentTextureHandle =
              compositorPushPresentationScale.uploadRgbaTexture(
                  this.textureHandle, currentWidth, currentHeight, byteBuffer);
          if (currentTextureHandle.valid()) {
            this.textureHandle = currentTextureHandle;
            this.count = currentWidth;
            this.count2 = currentHeight;
            this.timestamp = decodedFrame.version();
          }
        }
      }
    }
  }

  private float[] createFloat() {
    if (this.fit2 == SceneSourceService.Fit.STRETCH
        || this.count <= 0
        || this.count2 <= 0
        || this.cw <= 0.0F
        || this.ch <= 0.0F) {
      return new float[] {this.cx, this.cy, this.cw, this.ch};
    }

    if (this.fit2 == SceneSourceService.Fit.COVER) {
      return new float[] {this.cx, this.cy, this.cw, this.ch};
    }

    float value = this.cw / this.count;
    float currentValue = this.ch / this.count2;
    float nextValue = Math.min(value, currentValue);
    float previousValue = this.count * nextValue;
    float sourceValue = this.count2 * nextValue;
    return new float[] {
      this.cx + (this.cw - previousValue) * 0.5F,
      this.cy + (this.ch - sourceValue) * 0.5F,
      previousValue,
      sourceValue
    };
  }

  private float[] createFloat2() {
    if (this.fit2 == SceneSourceService.Fit.COVER
        && this.count > 0
        && this.count2 > 0
        && !(this.cw <= 0.0F)
        && !(this.ch <= 0.0F)) {
      float value = (float) this.count / this.count2;
      float currentValue = this.cw / this.ch;
      if (value > currentValue) {
        float nextValue = currentValue / value;
        float previousValue = (1.0F - nextValue) * 0.5F;
        return new float[] {previousValue, 0.0F, 1.0F - previousValue, 1.0F};
      } else {
        float sourceValue = value / currentValue;
        float targetValue = (1.0F - sourceValue) * 0.5F;
        return new float[] {0.0F, targetValue, 1.0F, 1.0F - targetValue};
      }
    } else {
      return new float[] {0.0F, 0.0F, 1.0F, 1.0F};
    }
  }

  private MediaSourceService.ScaleMode createScaleMode() {
    return switch (this.fit2) {
      case STRETCH -> MediaSourceService.ScaleMode.STRETCH;
      case CONTAIN -> MediaSourceService.ScaleMode.CONTAIN;
      case COVER -> MediaSourceService.ScaleMode.COVER;
    };
  }

  @Override
  protected void onDetached() {
    this.mediaSourceService.close();
    if (this.textureHandle.valid() && CoreIsInitializedHandler.isReady()) {
      CoreIsInitializedHandler.get().compositor().destroyTexture(this.textureHandle);
      this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    }
  }

  public Path source() {
    return this.path;
  }

  public enum Fit {
    STRETCH,
    CONTAIN,
    COVER;

    private static SceneSourceService.Fit[] $values() {
      return new SceneSourceService.Fit[] {STRETCH, CONTAIN, COVER};
    }
  }
}
