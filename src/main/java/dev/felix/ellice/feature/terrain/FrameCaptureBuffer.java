package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.system.MemoryUtil;

final class FrameCaptureBuffer implements AutoCloseable {
  private final Map<Integer, RhiBlendStateService.TextureHandle> entries = new HashMap<>();
  private long timestamp;
  private static final long timestamp2 = 268435456L;

  private int calculateValue(int value, boolean enabled) {
    if (value <= 0) {
      return value;
    }

    RhiBlendStateService.TextureHandle textureHandle = this.entries.get(value);
    if (textureHandle != null) {
      return textureHandle.id();
    }

    RhiOperationHandler rhiOperation = RhiDeviceService.device();
    RhiBlendStateService.TextureDownload textureDownload =
        rhiOperation.downloadTexture(new RhiBlendStateService.TextureHandle(value));
    if (textureDownload != null && textureDownload.complete()) {
      try {
        long currentWidth =
            (long) textureDownload.width() * textureDownload.height() * (enabled ? 6 : 4);
        if (this.timestamp + currentWidth > 268435456L) {
          throw new IllegalStateException("Recording texture limit reached (256 MiB)");
        }

        RhiBlendStateService.TextureHandle nextWidth =
            rhiOperation.createTexture(
                new RhiBlendStateService.TextureDescriptor(
                    textureDownload.width(),
                    textureDownload.height(),
                    RhiBlendStateService.TextureFormat.RGBA8,
                    enabled
                        ? RhiBlendStateService.FilterMode.NEAREST_MIPMAP_LINEAR
                        : RhiBlendStateService.FilterMode.NEAREST,
                    RhiBlendStateService.FilterMode.NEAREST,
                    RhiBlendStateService.AddressMode.CLAMP),
                textureDownload.pixels());
        this.entries.put(value, nextWidth);
        this.timestamp += currentWidth;
        return nextWidth.id();
      } finally {
        MemoryUtil.memFree(textureDownload.pixels());
      }
    } else {
      throw new IllegalStateException("Unable to capture scene texture");
    }
  }

  CompatLoadedHandler.Textures capture(CompatLoadedHandler.Textures currentTextures) {
    if (currentTextures != null && currentTextures.atlas() > 0 && currentTextures.lightmap() > 0) {
      return new CompatLoadedHandler.Textures(
          this.calculateValue(currentTextures.atlas(), true),
          this.calculateValue(currentTextures.lightmap(), false),
          currentTextures.width(),
          currentTextures.height(),
          currentTextures.mipLevels(),
          currentTextures.modernSampling());
    } else {
      throw new IllegalStateException("Terrain textures are not ready");
    }
  }

  List<CompatLoadedHandler.MapPlayer> capture(List<CompatLoadedHandler.MapPlayer> items) {
    ArrayList arrayList = new ArrayList();

    for (CompatLoadedHandler.MapPlayer mapPlayer : items) {
      ArrayList currentArrayList = new ArrayList();

      for (CompatLoadedHandler.PlayerPart playerPart : mapPlayer.model()) {
        currentArrayList.add(
            new CompatLoadedHandler.PlayerPart(
                playerPart.mesh(),
                this.calculateValue(playerPart.texture(), false),
                playerPart.color()));
      }

      arrayList.add(
          new CompatLoadedHandler.MapPlayer(
              mapPlayer.id(),
              mapPlayer.name(),
              mapPlayer.x(),
              mapPlayer.y(),
              mapPlayer.z(),
              mapPlayer.yaw(),
              mapPlayer.pitch(),
              mapPlayer.slim(),
              this.calculateValue(mapPlayer.skinGlId(), false),
              mapPlayer.legacySkin(),
              mapPlayer.health(),
              mapPlayer.maxHealth(),
              mapPlayer.crouching(),
              currentArrayList,
              mapPlayer.blockLight(),
              mapPlayer.skyLight()));
    }

    return arrayList;
  }

  @Override
  public void close() {
    for (RhiBlendStateService.TextureHandle textureHandle : this.entries.values()) {
      RhiDeviceService.device().destroyTexture(textureHandle);
    }

    this.entries.clear();
    this.timestamp = 0L;
  }
}
