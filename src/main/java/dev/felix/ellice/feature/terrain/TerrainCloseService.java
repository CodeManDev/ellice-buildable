package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joml.Vector3d;
import org.lwjgl.system.MemoryUtil;

final class TerrainCloseService implements AutoCloseable {
  private static final int count = -7636797;
  private static final float value = 2.1F;
  private static final float value2 = 0.65F;
  private static final float value3 = 1.1F;
  private static final float value4 = 4.3F;
  private static final float value5 = 0.45F;
  private static final float value6 = 0.22F;
  private static final float value7 = 0.85F;
  private static final float value8 = 24.0F;
  private static final float value9 = 32.0F;
  private static final float value10 = 10.0F;
  private static final float value11 = 10.0F;
  private static final float value12 = 6.0F;
  private static final float value13 = 0.8F;
  private static final TerrainPoseLimbService.Limb[] limb = TerrainPoseLimbService.Limb.values();
  private final Render3dSceneService renderer;
  private final RhiOperationHandler rhiOperationHandler = RhiDeviceService.device();
  private final Render3dAddService renderer2;
  private RhiBlendStateService.SamplerHandle samplerHandle =
      RhiBlendStateService.SamplerHandle.NONE;
  private final GpuMesh[][] renderer3 = new GpuMesh[4][limb.length];
  private final Map<UUID, TerrainCloseService.Entry> entries = new HashMap<>();

  private RhiBlendStateService.SamplerHandle createSamplerHandle() {
    if (!this.samplerHandle.valid()) {
      this.samplerHandle =
          this.rhiOperationHandler.createSampler(
              RhiBlendStateService.SamplerDescriptor.NEAREST_CLAMP);
    }

    return this.samplerHandle;
  }

  TerrainCloseService(Render3dSceneService render3dScene, Render3dAddService render3dAdd) {
    this.renderer = render3dScene;
    this.renderer2 = render3dAdd;

    for (boolean enabled : new boolean[] {false, true}) {
      for (boolean currentEnabled : new boolean[] {false, true}) {
        int index = calculateValue(enabled, currentEnabled);

        for (TerrainPoseLimbService.Limb currentLimb : limb) {
          TerrainPoseLimbService.LimbMesh currentLimbMesh =
              TerrainPoseLimbService.limbMesh(enabled, currentEnabled, currentLimb);
          this.renderer3[index][currentLimb.ordinal()] =
              render3dScene.createMesh(currentLimbMesh.mesh());
        }
      }
    }
  }

  void update(
      List<CompatLoadedHandler.MapPlayer> items, TerrainViewportService.Frame frame, long offset) {
    this.updateState(items, frame, offset, false);
  }

  void updateRecorded(
      List<CompatLoadedHandler.MapPlayer> items, TerrainViewportService.Frame frame, long offset) {
    this.updateState(items, frame, offset, true);
  }

  private void updateState(
      List<CompatLoadedHandler.MapPlayer> items,
      TerrainViewportService.Frame frame,
      long offset,
      boolean enabled) {
    HashSet hashSet = new HashSet();

    for (CompatLoadedHandler.MapPlayer mapPlayer : items) {
      hashSet.add(mapPlayer.id());
      TerrainCloseService.Entry entry = this.entries.get(mapPlayer.id());
      if (entry != null
          && (entry.skinGlId != mapPlayer.skinGlId()
              || entry.slim != mapPlayer.slim()
              || entry.legacy != mapPlayer.legacySkin())) {
        this.updateState4(mapPlayer.id());
        entry = null;
      }

      if (entry == null) {
        entry = this.createEntry(mapPlayer, offset);
        this.entries.put(mapPlayer.id(), entry);
      }

      if (enabled) {
        entry.smoothX = mapPlayer.x();
        entry.smoothY = mapPlayer.y();
        entry.smoothZ = mapPlayer.z();
        entry.smoothYaw = mapPlayer.yaw();
      }

      this.updateState2(entry, mapPlayer, frame, offset);
    }

    for (UUID uUID :
        this.entries.keySet().stream().filter(item -> !hashSet.contains(item)).toList()) {
      this.updateState4(uUID);
    }
  }

  void clear() {
    for (UUID uUID : this.entries.keySet().stream().toList()) {
      this.updateState4(uUID);
    }
  }

  List<Render3dIdService> casters() {
    ArrayList currentSize = new ArrayList(this.entries.size() * limb.length);

    for (TerrainCloseService.Entry entry : this.entries.values()) {
      for (Render3dIdService render3dId : entry.objects) {
        if (render3dId != null && render3dId.visible()) {
          currentSize.add(render3dId);
        }
      }

      for (Render3dIdService currentRender3dId : entry.nativeObjects) {
        if (currentRender3dId.visible()) {
          currentSize.add(currentRender3dId);
        }
      }
    }

    return currentSize;
  }

  private MaterialErFactory createMaterialErFactory(String text) {
    return MaterialErFactory.builder(
            text,
            new ShaderDefinition(
                "ellice:map-player-surface", "render3d/pbr.vert", "terrain/player.frag"))
        .build()
        .color("uBaseColor", -1)
        .uniform("uAlphaCutoff", 0.5F)
        .uniform("uHasBaseColorMap", 0);
  }

  private TerrainCloseService.Entry createEntry(
      CompatLoadedHandler.MapPlayer mapPlayer, long size) {
    RhiBlendStateService.TextureHandle textureHandle =
        this.createTextureHandle(mapPlayer.skinGlId());
    MaterialErFactory materialEr;
    if (textureHandle.valid()) {
      materialEr =
          this.createMaterialErFactory("ellice:map-player")
              .texture("uBaseColorMap", textureHandle, 0)
              .uniform("uHasBaseColorMap", 1)
              .uniform("uRoughness", 0.85F);
    } else if (mapPlayer.skinGlId() > 0) {
      materialEr =
          this.createMaterialErFactory("ellice:map-player")
              .texture(
                  "uBaseColorMap",
                  new RhiBlendStateService.TextureHandle(mapPlayer.skinGlId()),
                  this.createSamplerHandle(),
                  0)
              .uniform("uHasBaseColorMap", 1)
              .uniform("uRoughness", 0.85F);
    } else {
      materialEr =
          this.createMaterialErFactory("ellice:map-player-fallback")
              .color("uBaseColor", -7636797)
              .uniform("uRoughness", 0.85F);
    }

    TerrainCloseService.Entry entry = new TerrainCloseService.Entry(materialEr);
    entry.skinGlId = mapPlayer.skinGlId();
    entry.slim = mapPlayer.slim();
    entry.legacy = mapPlayer.legacySkin();
    entry.skinCopy = textureHandle;
    entry.lastX = mapPlayer.x();
    entry.lastZ = mapPlayer.z();
    entry.lastNanos = size;
    entry.crouchBlend = mapPlayer.crouching() ? 1.0F : 0.0F;
    entry.lastHealth = mapPlayer.health();
    entry.hurtFlash = 0.0F;
    entry.smoothX = mapPlayer.x();
    entry.smoothY = mapPlayer.y();
    entry.smoothZ = mapPlayer.z();
    entry.smoothYaw = mapPlayer.yaw();
    entry.smoothLook = 0.0F;
    entry.smoothSpeed = 0.0F;
    int index = calculateValue(mapPlayer.slim(), mapPlayer.legacySkin());

    for (TerrainPoseLimbService.Limb currentLimb : limb) {
      entry.objects[currentLimb.ordinal()] =
          this.renderer2.add(
              this.renderer3[index][currentLimb.ordinal()],
              materialEr,
              TerrainPoseLimbService.poseLimb(
                  mapPlayer.x(),
                  mapPlayer.y(),
                  mapPlayer.z(),
                  mapPlayer.yaw(),
                  0.0F,
                  0.0F,
                  currentLimb));
    }

    return entry;
  }

  private void updateState2(
      TerrainCloseService.Entry entry,
      CompatLoadedHandler.MapPlayer mapPlayer,
      TerrainViewportService.Frame frame,
      long offset) {
    double doubleValue =
        entry.lastNanos == 0L ? 0.0 : Math.clamp((offset - entry.lastNanos) / 1.0E9, 0.0, 0.25);
    double currentX = Math.hypot(mapPlayer.x() - entry.lastX, mapPlayer.z() - entry.lastZ);
    double currentDoubleValue = doubleValue > 1.0E-4 ? currentX / doubleValue : 0.0;
    entry.lastX = mapPlayer.x();
    entry.lastZ = mapPlayer.z();
    entry.lastNanos = offset;
    entry.smoothSpeed = damp(entry.smoothSpeed, (float) currentDoubleValue, 6.0F, doubleValue);
    entry.smoothX = dampPosition(entry.smoothX, mapPlayer.x(), 24.0F, doubleValue);
    entry.smoothY = dampPosition(entry.smoothY, mapPlayer.y(), 32.0F, doubleValue);
    entry.smoothZ = dampPosition(entry.smoothZ, mapPlayer.z(), 24.0F, doubleValue);
    entry.smoothYaw = dampAngle(entry.smoothYaw, mapPlayer.yaw(), 10.0F, doubleValue);
    float value = Math.clamp(-mapPlayer.pitch(), -1.1F, 1.1F);
    entry.smoothLook = damp(entry.smoothLook, value, 10.0F, doubleValue);
    entry.phase =
        entry.phase
            + (float)
                (entry.smoothSpeed * doubleValue * 3.141592653589793 * 2.0 / 2.0999999046325684);
    float currentValue = Math.clamp(entry.smoothSpeed / 4.3F, 0.0F, 1.0F) * 0.65F;
    float nextValue = mapPlayer.crouching() ? 1.0F : 0.0F;
    entry.crouchBlend =
        (float)
            (entry.crouchBlend
                + (nextValue - entry.crouchBlend) * Math.clamp(doubleValue * 8.0, 0.0, 1.0));
    float previousValue = entry.crouchBlend * 0.45F;
    float sourceValue = entry.crouchBlend * 0.22F;
    float targetValue = (float) Math.sin(entry.phase) * gaitFactor(entry.smoothSpeed);
    float inputValue = entry.smoothLook;
    double nextDoubleValue = entry.smoothX;
    double previousDoubleValue = entry.smoothY;
    double sourceDoubleValue = entry.smoothZ;
    float outputValue = entry.smoothYaw;
    int resultValue =
        frame.project(nextDoubleValue, previousDoubleValue + 0.9, sourceDoubleValue) != null
            ? 1
            : 0;
    this.updateState3(
        entry,
        mapPlayer,
        new GeometryXService(nextDoubleValue, previousDoubleValue, sourceDoubleValue),
        (resultValue != 0));
    entry.material.uniform("uPlayerLight", mapPlayer.blockLight(), mapPlayer.skyLight());

    for (Render3dIdService render3dId : entry.nativeObjects) {
      render3dId.material().uniform("uPlayerLight", mapPlayer.blockLight(), mapPlayer.skyLight());
    }

    if (mapPlayer.health() < entry.lastHealth - 0.001F) {
      entry.hurtFlash = 1.0F;
    }

    entry.lastHealth = mapPlayer.health();
    entry.hurtFlash = decayHurtFlash(entry.hurtFlash, doubleValue);
    entry.material.uniform("uHurtFlash", entry.hurtFlash);

    for (Render3dIdService currentRender3dId : entry.nativeObjects) {
      currentRender3dId.material().uniform("uHurtFlash", entry.hurtFlash);
    }

    for (TerrainPoseLimbService.Limb currentLimb : limb) {
      GeometryXService geometryX =
          switch (currentLimb) {
            case LEG_RIGHT, ARM_LEFT, LEG_LEFT, ARM_RIGHT -> {
              float candidateValue =
                  switch (currentLimb) {
                    case LEG_RIGHT, ARM_LEFT -> targetValue * currentValue;
                    default -> -targetValue * currentValue;
                  };
              if (currentLimb != TerrainPoseLimbService.Limb.ARM_RIGHT
                  && currentLimb != TerrainPoseLimbService.Limb.ARM_LEFT) {
                yield TerrainPoseLimbService.poseLimb(
                    nextDoubleValue,
                    previousDoubleValue,
                    sourceDoubleValue,
                    outputValue,
                    candidateValue,
                    sourceValue,
                    currentLimb);
              } else {
                float selectedValue = candidateValue * 0.9F;
                yield TerrainPoseLimbService.poseTorsoChild(
                    nextDoubleValue,
                    previousDoubleValue,
                    sourceDoubleValue,
                    outputValue,
                    previousValue,
                    selectedValue,
                    sourceValue,
                    currentLimb);
              }
            }
            case TORSO ->
                TerrainPoseLimbService.poseLimb(
                    nextDoubleValue,
                    previousDoubleValue,
                    sourceDoubleValue,
                    outputValue,
                    previousValue,
                    sourceValue,
                    currentLimb);
            case HEAD ->
                TerrainPoseLimbService.poseTorsoChild(
                    nextDoubleValue,
                    previousDoubleValue,
                    sourceDoubleValue,
                    outputValue,
                    previousValue,
                    inputValue - previousValue * 0.6F,
                    sourceValue,
                    currentLimb);
          };
      entry.objects[currentLimb.ordinal()].transform(geometryX);
      entry.objects[currentLimb.ordinal()].visible(resultValue != 0 && mapPlayer.model().isEmpty());
    }
  }

  private void updateState3(
      TerrainCloseService.Entry entry,
      CompatLoadedHandler.MapPlayer mapPlayer,
      GeometryXService geometryX,
      boolean enabled) {
    List items = mapPlayer.model();
    if (entry.nativeSample != items) {
      for (int index = 0; index < items.size(); index++) {
        CompatLoadedHandler.PlayerPart playerPart =
            (CompatLoadedHandler.PlayerPart) items.get(index);
        Render3dIdService render3dId;
        if (index < entry.nativeObjects.size()) {
          render3dId = entry.nativeObjects.get(index);
          render3dId.mesh().update(playerPart.mesh());
        } else {
          render3dId =
              this.renderer2.add(
                  this.renderer.createMesh(playerPart.mesh()),
                  this.createMaterialErFactory("ellice:map-player-native")
                      .uniform("uRoughness", 0.85F),
                  geometryX);
          entry.nativeObjects.add(render3dId);
        }

        render3dId
            .material()
            .texture(
                "uBaseColorMap",
                new RhiBlendStateService.TextureHandle(playerPart.texture()),
                this.createSamplerHandle(),
                0)
            .uniform("uHasBaseColorMap", playerPart.texture() > 0 ? 1 : 0)
            .color("uBaseColor", playerPart.color());
      }

      while (entry.nativeObjects.size() > items.size()) {
        Render3dIdService currentRender3dId = entry.nativeObjects.removeLast();
        this.renderer2.remove(currentRender3dId);
        this.renderer.destroyMesh(currentRender3dId.mesh());
      }

      entry.nativeSample = items;
    }

    for (Render3dIdService nextRender3dId : entry.nativeObjects) {
      nextRender3dId.transform(geometryX).visible(enabled);
    }
  }

  static double dampPosition(double x, double y, float width, double height) {
    if (!Double.isFinite(height) || height <= 0.0) {
      return x;
    } else if (!Double.isFinite(x)) {
      return y;
    } else if (Double.isFinite(y) && Float.isFinite(width) && !(width <= 0.0F)) {
      return Math.abs(y - x) > 16.0 ? y : x + (y - x) * -Math.expm1(-width * height);
    } else {
      return x;
    }
  }

  Vector3d smoothed(UUID uUID) {
    TerrainCloseService.Entry entry = this.entries.get(uUID);
    return entry == null ? null : new Vector3d(entry.smoothX, entry.smoothY, entry.smoothZ);
  }

  static float decayHurtFlash(float value, double doubleValue) {
    if (!Float.isFinite(value) || value <= 0.0F) {
      return 0.0F;
    } else {
      return Double.isFinite(doubleValue) && !(doubleValue <= 0.0)
          ? Math.max(0.0F, value - (float) (doubleValue / 0.5))
          : value;
    }
  }

  static float gaitFactor(float value) {
    return Float.isFinite(value) && !(value <= 0.0F) ? Math.clamp(value / 0.8F, 0.0F, 1.0F) : 0.0F;
  }

  static float damp(float value, float currentValue, float nextValue, double doubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue <= 0.0) {
      return value;
    } else if (!Float.isFinite(value)) {
      return currentValue;
    } else if (Float.isFinite(currentValue) && Float.isFinite(nextValue) && !(nextValue <= 0.0F)) {
      float previousValue = 1.0F - (float) Math.exp(-nextValue * doubleValue);
      return value + (currentValue - value) * previousValue;
    } else {
      return value;
    }
  }

  static float dampAngle(float value, float currentValue, float nextValue, double doubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue <= 0.0) {
      return value;
    } else if (!Float.isFinite(value)) {
      return currentValue;
    } else {
      return !Float.isFinite(currentValue)
          ? value
          : value
              + wrapDegrees(currentValue - value)
                  * (1.0F - (float) Math.exp(-Math.max(0.0F, nextValue) * doubleValue));
    }
  }

  static float wrapDegrees(float value) {
    float currentValue = value % 360.0F;
    if (currentValue > 180.0F) {
      currentValue -= 360.0F;
    } else if (currentValue < -180.0F) {
      currentValue += 360.0F;
    }

    return currentValue;
  }

  private void updateState4(UUID uUID) {
    TerrainCloseService.Entry entry = this.entries.remove(uUID);
    if (entry != null) {
      for (Render3dIdService render3dId : entry.objects) {
        if (render3dId != null) {
          this.renderer2.remove(render3dId);
        }
      }

      for (Render3dIdService currentRender3dId : entry.nativeObjects) {
        this.renderer2.remove(currentRender3dId);
        this.renderer.destroyMesh(currentRender3dId.mesh());
      }

      if (entry.skinCopy.valid()) {
        this.rhiOperationHandler.destroyTexture(entry.skinCopy);
      }
    }
  }

  private RhiBlendStateService.TextureHandle createTextureHandle(int textureId) {
    if (textureId <= 0) {
      return RhiBlendStateService.TextureHandle.NONE;
    }

    RhiBlendStateService.TextureDownload textureDownload;
    try {
      textureDownload =
          this.rhiOperationHandler.downloadTexture(
              new RhiBlendStateService.TextureHandle(textureId));
    } catch (RuntimeException exception) {
      return RhiBlendStateService.TextureHandle.NONE;
    }

    if (textureDownload != null && textureDownload.complete()) {
      try {
        RhiBlendStateService.TextureDescriptor currentWidth =
            new RhiBlendStateService.TextureDescriptor(
                textureDownload.width(),
                textureDownload.height(),
                RhiBlendStateService.TextureFormat.RGBA8,
                RhiBlendStateService.FilterMode.LINEAR_MIPMAP_LINEAR,
                RhiBlendStateService.FilterMode.NEAREST,
                RhiBlendStateService.AddressMode.CLAMP);
        return this.rhiOperationHandler.createTexture(currentWidth, textureDownload.pixels());
      } catch (RuntimeException currentException) {
        return RhiBlendStateService.TextureHandle.NONE;
      } finally {
        MemoryUtil.memFree(textureDownload.pixels());
      }
    } else {
      return RhiBlendStateService.TextureHandle.NONE;
    }
  }

  private static int calculateValue(boolean enabled, boolean currentEnabled) {
    return (enabled ? 2 : 0) | (currentEnabled ? 1 : 0);
  }

  @Override
  public void close() {
    this.clear();
    if (this.samplerHandle.valid()) {
      this.rhiOperationHandler.destroySampler(this.samplerHandle);
      this.samplerHandle = RhiBlendStateService.SamplerHandle.NONE;
    }

    for (GpuMesh[] gpuMeshs : this.renderer3) {
      for (GpuMesh gpuMesh : gpuMeshs) {
        this.renderer.destroyMesh(gpuMesh);
      }
    }
  }

  private static final class Entry {
    final Render3dIdService[] objects = new Render3dIdService[TerrainCloseService.limb.length];
    final MaterialErFactory material;
    final List<Render3dIdService> nativeObjects = new ArrayList<>();
    List<CompatLoadedHandler.PlayerPart> nativeSample = List.of();
    int skinGlId;
    boolean slim;
    boolean legacy;
    RhiBlendStateService.TextureHandle skinCopy = RhiBlendStateService.TextureHandle.NONE;
    double lastX;
    double lastZ;
    long lastNanos;
    float phase;
    float crouchBlend;
    float lastHealth = 20.0F;
    float hurtFlash;
    double smoothX;
    double smoothY;
    double smoothZ;
    float smoothYaw;
    float smoothLook;
    float smoothSpeed;

    Entry(MaterialErFactory materialEr) {
      this.material = materialEr;
    }
  }
}
