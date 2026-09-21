package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.util.Objects;

public final class SkyTextureRenderer implements AutoCloseable {
  private static final RhiBlendStateService.VertexLayout vertexLayout =
      RhiBlendStateService.VertexLayout.of(
          8,
          new RhiBlendStateService.VertexAttribute(
              0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
  private final RhiOperationHandler rhiOperationHandler = RhiDeviceService.device();
  private final ShaderRecipeService shaderRecipeService;
  private SkyTextureRenderer.Look renderer = SkyTextureRenderer.Look.goldenHour();
  private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
  private RhiBlendStateService.SamplerHandle samplerHandle =
      RhiBlendStateService.SamplerHandle.NONE;
  private RhiBlendStateService.SamplerHandle samplerHandle2 =
      RhiBlendStateService.SamplerHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.FramebufferHandle framebufferHandle =
      RhiBlendStateService.FramebufferHandle.NONE;
  private int count;
  private int count2;

  public SkyTextureRenderer(Render3dSceneService render3dScene) {
    Objects.requireNonNull(render3dScene, "engine");
    this.shaderRecipeService =
        new ShaderRecipeService(
            this.rhiOperationHandler,
            render3dScene.shaders(),
            new ShaderData(
                new ShaderDefinition(
                    "ellice:terrain-cinematic-grade",
                    "fullscreen.vert",
                    "terrain/cinematic_grade.frag"),
                vertexLayout,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
  }

  public void setLook(SkyTextureRenderer.Look look) {
    this.renderer = look == null ? SkyTextureRenderer.Look.goldenHour() : look;
  }

  public SkyTextureRenderer.Look look() {
    return this.renderer;
  }

  public RhiBlendStateService.TextureHandle texture() {
    return this.textureHandle;
  }

  public RhiBlendStateService.TextureHandle render(
      RhiBlendStateService.TextureHandle colorTexture,
      RhiBlendStateService.TextureHandle depthTexture,
      int width,
      int height,
      float nearPlane,
      float farPlane,
      float focusDistance,
      double time) {
    if (colorTexture != null && colorTexture.valid() && width > 0 && height > 0) {
      float renderTime = (float) (Double.isFinite(time) ? time : 0.0);
      boolean[] booleans = new boolean[] {false};
      this.updateState2(
          () -> {
            this.updateState(width, height);
            if (this.textureHandle.valid()
                && this.framebufferHandle.valid()
                && this.bufferHandle.valid()
                && this.samplerHandle.valid()
                && this.samplerHandle2.valid()) {
              RhiBlendStateService.PipelineHandle pipelineHandle =
                  this.shaderRecipeService.resolve();
              if (pipelineHandle.valid()) {
                RhiCommandBuffer rhiCommandBuffer = this.rhiOperationHandler.encoder();
                rhiCommandBuffer.clearScissor();
                rhiCommandBuffer.setColorWriteMask(true, true, true, true);
                rhiCommandBuffer.beginRenderPass(this.framebufferHandle);

                try {
                  rhiCommandBuffer.setViewport(0, 0, this.count, this.count2);
                  rhiCommandBuffer.bindPipeline(pipelineHandle);
                  rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
                  rhiCommandBuffer.bindTexture(colorTexture, this.samplerHandle, 0);
                  rhiCommandBuffer.pushInt("uColorTexture", 0);
                  rhiCommandBuffer.pushVec2("uResolution", this.count, this.count2);
                  rhiCommandBuffer.pushFloat("uTime", renderTime);
                  rhiCommandBuffer.pushFloat("uExposure", this.renderer.exposure());
                  rhiCommandBuffer.pushFloat("uContrast", this.renderer.contrast());
                  rhiCommandBuffer.pushFloat("uSaturation", this.renderer.saturation());
                  rhiCommandBuffer.pushFloat("uVignette", this.renderer.vignette());
                  rhiCommandBuffer.pushFloat("uChromatic", this.renderer.chromatic());
                  rhiCommandBuffer.pushFloat("uBloom", this.renderer.bloom());
                  rhiCommandBuffer.pushFloat("uBloomRadius", this.renderer.bloomRadius());
                  rhiCommandBuffer.pushFloat("uBloomThreshold", this.renderer.bloomThreshold());
                  rhiCommandBuffer.pushFloat("uHalation", this.renderer.halation());
                  rhiCommandBuffer.pushFloat("uGrain", this.renderer.grain());
                  rhiCommandBuffer.pushFloat("uWarmth", this.renderer.warmth());
                  rhiCommandBuffer.pushFloat("uLetterbox", this.renderer.letterbox());
                  rhiCommandBuffer.pushInt("uDepthTexture", 1);
                  int depthAvailable =
                      depthTexture != null
                              && depthTexture.valid()
                              && Float.isFinite(nearPlane)
                              && Float.isFinite(farPlane)
                              && nearPlane > 0.0F
                              && farPlane > nearPlane
                          ? 1
                          : 0;
                  rhiCommandBuffer.pushInt("uDepthAvailable", depthAvailable != 0 ? 1 : 0);
                  rhiCommandBuffer.pushFloat("uNear", nearPlane);
                  rhiCommandBuffer.pushFloat("uFar", farPlane);
                  rhiCommandBuffer.pushFloat("uSsao", this.renderer.ssao());
                  rhiCommandBuffer.pushFloat("uSsaoRadius", this.renderer.ssaoRadius());
                  rhiCommandBuffer.pushFloat("uDof", this.renderer.dof());
                  rhiCommandBuffer.pushFloat(
                      "uFocusDist",
                      Float.isFinite(focusDistance) && focusDistance > 0.0F
                          ? focusDistance
                          : 20.0F);
                  rhiCommandBuffer.pushFloat("uFxaa", this.renderer.fxaa());
                  if (depthAvailable != 0) {
                    rhiCommandBuffer.bindTexture(depthTexture, this.samplerHandle2, 1);
                  }

                  rhiCommandBuffer.draw(4, 1, 0);
                  booleans[0] = true;
                } finally {
                  rhiCommandBuffer.endRenderPass();
                }
              }
            }
          });
      return booleans[0] && this.textureHandle.valid() ? this.textureHandle : colorTexture;
    } else {
      return colorTexture;
    }
  }

  private void updateState(int value, int currentValue) {
    int nextValue = Math.clamp(value, 32, 4096);
    int previousValue = Math.clamp(currentValue, 32, 4096);
    if (!this.bufferHandle.valid()) {
      this.bufferHandle =
          this.rhiOperationHandler.createBuffer(
              RhiBlendStateService.BufferUsage.VERTEX.bit,
              RhiBlendStateService.BufferAccess.STATIC,
              new float[] {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F});
    }

    if (!this.samplerHandle.valid()) {
      this.samplerHandle =
          this.rhiOperationHandler.createSampler(
              new RhiBlendStateService.SamplerDescriptor(
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.AddressMode.CLAMP));
    }

    if (!this.samplerHandle2.valid()) {
      this.samplerHandle2 =
          this.rhiOperationHandler.createSampler(
              RhiBlendStateService.SamplerDescriptor.NEAREST_CLAMP);
    }

    if (!this.textureHandle.valid() || nextValue != this.count || previousValue != this.count2) {
      if (this.framebufferHandle.valid()) {
        this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
      }

      if (this.textureHandle.valid()) {
        this.rhiOperationHandler.destroyTexture(this.textureHandle);
      }

      this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
      this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
      this.count = this.count2 = 0;
      RhiBlendStateService.TextureHandle currentTextureHandle =
          this.rhiOperationHandler.createTexture(
              new RhiBlendStateService.TextureDescriptor(
                  nextValue,
                  previousValue,
                  RhiBlendStateService.TextureFormat.RGBA8,
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.FilterMode.LINEAR,
                  RhiBlendStateService.AddressMode.CLAMP));
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle =
          this.rhiOperationHandler.createFramebuffer(currentTextureHandle);
      this.textureHandle = currentTextureHandle;
      this.framebufferHandle = currentFramebufferHandle;
      this.count = nextValue;
      this.count2 = previousValue;
    }
  }

  private void updateState2(Runnable runnable) {
    if (this.rhiOperationHandler.encoder() instanceof GlRenderer glRenderer) {
      glRenderer.saveGLState();

      try {
        runnable.run();
      } finally {
        glRenderer.restoreGLState();
      }
    } else {
      runnable.run();
    }
  }

  @Override
  public void close() {
    Runnable runnable =
        () -> {
          if (this.framebufferHandle.valid()) {
            this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
          }

          if (this.textureHandle.valid()) {
            this.rhiOperationHandler.destroyTexture(this.textureHandle);
          }

          if (this.bufferHandle.valid()) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
          }

          if (this.samplerHandle.valid()) {
            this.rhiOperationHandler.destroySampler(this.samplerHandle);
          }

          if (this.samplerHandle2.valid()) {
            this.rhiOperationHandler.destroySampler(this.samplerHandle2);
          }

          this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
          this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
          this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
          this.samplerHandle = RhiBlendStateService.SamplerHandle.NONE;
          this.samplerHandle2 = RhiBlendStateService.SamplerHandle.NONE;
          this.count = this.count2 = 0;
          this.shaderRecipeService.close();
        };
    if (!this.framebufferHandle.valid()
        && !this.bufferHandle.valid()
        && !this.samplerHandle.valid()
        && !this.samplerHandle2.valid()) {
      runnable.run();
    } else {
      this.updateState2(runnable);
    }
  }

  public record Look(
      float exposure,
      float contrast,
      float saturation,
      float vignette,
      float chromatic,
      float bloom,
      float bloomRadius,
      float bloomThreshold,
      float halation,
      float grain,
      float warmth,
      float letterbox,
      float ssao,
      float ssaoRadius,
      float dof,
      float fxaa) {
    public Look(
        float exposure,
        float contrast,
        float saturation,
        float vignette,
        float chromatic,
        float bloom,
        float bloomRadius,
        float bloomThreshold,
        float halation,
        float grain,
        float warmth,
        float letterbox,
        float ssao,
        float ssaoRadius,
        float dof,
        float fxaa) {
      exposure = calculateValue(exposure, 0.06F, -3.0F, 3.0F);
      contrast = calculateValue(contrast, 1.06F, 0.0F, 3.0F);
      saturation = calculateValue(saturation, 1.12F, 0.0F, 3.0F);
      vignette = calculateValue(vignette, 0.32F, 0.0F, 1.0F);
      chromatic = calculateValue(chromatic, 0.0012F, 0.0F, 0.02F);
      bloom = calculateValue(bloom, 0.3F, 0.0F, 3.0F);
      bloomRadius = calculateValue(bloomRadius, 6.0F, 1.0F, 32.0F);
      bloomThreshold = calculateValue(bloomThreshold, 0.8F, 0.0F, 1.0F);
      halation = calculateValue(halation, 0.5F, 0.0F, 2.0F);
      grain = calculateValue(grain, 0.012F, 0.0F, 0.1F);
      warmth = calculateValue(warmth, 0.65F, 0.0F, 1.0F);
      letterbox = calculateValue(letterbox, 0.0F, 0.0F, 0.25F);
      ssao = calculateValue(ssao, 0.5F, 0.0F, 1.0F);
      ssaoRadius = calculateValue(ssaoRadius, 1.5F, 0.2F, 4.0F);
      dof = calculateValue(dof, 0.4F, 0.0F, 1.0F);
      fxaa = calculateValue(fxaa, 1.0F, 0.0F, 1.0F);
      this.exposure = exposure;
      this.contrast = contrast;
      this.saturation = saturation;
      this.vignette = vignette;
      this.chromatic = chromatic;
      this.bloom = bloom;
      this.bloomRadius = bloomRadius;
      this.bloomThreshold = bloomThreshold;
      this.halation = halation;
      this.grain = grain;
      this.warmth = warmth;
      this.letterbox = letterbox;
      this.ssao = ssao;
      this.ssaoRadius = ssaoRadius;
      this.dof = dof;
      this.fxaa = fxaa;
    }

    private static float calculateValue(
        float value, float currentValue, float nextValue, float previousValue) {
      return !Float.isFinite(value) ? currentValue : Math.clamp(value, nextValue, previousValue);
    }

    public static SkyTextureRenderer.Look goldenHour() {
      return new SkyTextureRenderer.Look(
          0.06F, 1.06F, 1.12F, 0.32F, 0.0012F, 0.3F, 6.0F, 0.8F, 0.5F, 0.012F, 0.65F, 0.0F, 0.5F,
          1.5F, 0.4F, 1.0F);
    }
  }
}
