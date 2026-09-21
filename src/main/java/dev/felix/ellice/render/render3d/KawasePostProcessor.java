package dev.felix.ellice.render.render3d;

import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.util.List;
import org.lwjgl.system.MemoryStack;

final class KawasePostProcessor implements AutoCloseable {
  private static final RhiBlendStateService.VertexLayout vertexLayout =
      RhiBlendStateService.VertexLayout.of(
          8,
          new RhiBlendStateService.VertexAttribute(
              0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
  private final RhiOperationHandler rhiOperationHandler;
  private final ShaderRecipeService fmvr01y3d7;
  private final ShaderRecipeService shaderRecipeService;
  private final ShaderRecipeService shaderRecipeService2;
  private final ShaderRecipeService shaderRecipeService3;
  private KawasePostProcessor.Surface[] renderer = new KawasePostProcessor.Surface[0];
  private KawasePostProcessor.Surface[] renderer2 = new KawasePostProcessor.Surface[0];
  private int count;
  private int count2;

  KawasePostProcessor(
      RhiOperationHandler rhiOperation, ShaderOverrideRootService shaderOverrideRoot) {
    this.rhiOperationHandler = rhiOperation;
    this.fmvr01y3d7 =
        this.createShaderRecipeService(
            shaderOverrideRoot, "extract", RhiBlendStateService.BlendState.DISABLED);
    this.shaderRecipeService =
        this.createShaderRecipeService(
            shaderOverrideRoot, "down", RhiBlendStateService.BlendState.DISABLED);
    this.shaderRecipeService2 =
        this.createShaderRecipeService(
            shaderOverrideRoot, "up", RhiBlendStateService.BlendState.DISABLED);
    this.shaderRecipeService3 =
        this.createShaderRecipeService(
            shaderOverrideRoot, "composite", Render3dWantsCaptureService.OUTLINE_BLEND);
  }

  private ShaderRecipeService createShaderRecipeService(
      ShaderOverrideRootService shaderOverrideRoot,
      String shaderSource,
      RhiBlendStateService.BlendState blendState) {
    return new ShaderRecipeService(
        this.rhiOperationHandler,
        shaderOverrideRoot,
        new ShaderData(
            new ShaderDefinition(
                "ellice:player-kawase-" + shaderSource,
                "fullscreen.vert",
                "render3d/player_kawase_" + shaderSource + ".frag"),
            vertexLayout,
            blendState,
            RhiBlendStateService.DepthStencilState.DISABLED,
            RhiBlendStateService.RasterizerState.NO_CULL,
            RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
  }

  void render(
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      RhiBlendStateService.BufferHandle bufferHandle,
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.TextureHandle currentTextureHandle,
      RhiBlendStateService.TextureHandle nextTextureHandle,
      Render3dViewService render3dView,
      KawaseBloomSettings kawaseBloomSettings,
      List<Render3dWantsCaptureService.ScreenRect> items,
      RhiBlendStateService.TextureHandle previousTextureHandle,
      RhiBlendStateService.TextureHandle sourceTextureHandle,
      int value,
      RhiBlendStateService.TextureHandle targetTextureHandle,
      boolean enabled) {
    if (kawaseBloomSettings.renderable()) {
      RhiBlendStateService.PipelineHandle pipelineHandle = this.fmvr01y3d7.resolve();
      RhiBlendStateService.PipelineHandle currentPipelineHandle =
          this.shaderRecipeService3.resolve();
      RhiBlendStateService.PipelineHandle nextPipelineHandle =
          kawaseBloomSettings.bloomRenderable()
              ? this.shaderRecipeService.resolve()
              : RhiBlendStateService.PipelineHandle.NONE;
      RhiBlendStateService.PipelineHandle previousPipelineHandle =
          kawaseBloomSettings.bloomRenderable()
              ? this.shaderRecipeService2.resolve()
              : RhiBlendStateService.PipelineHandle.NONE;
      if (pipelineHandle.valid()
          && currentPipelineHandle.valid()
          && (!kawaseBloomSettings.bloomRenderable()
              || nextPipelineHandle.valid() && previousPipelineHandle.valid())) {
        Render3dSourceWidthService currentWidth =
            Render3dSourceWidthService.create(
                render3dView.width(),
                render3dView.height(),
                kawaseBloomSettings.radiusPixels(),
                kawaseBloomSettings.levels());
        this.updateState4(
            currentWidth, kawaseBloomSettings.bloomRenderable() ? currentWidth.levels() : 1);
        updateState3(rhiCommandBuffer, this.renderer[0], pipelineHandle, bufferHandle);
        rhiCommandBuffer.bindTexture(textureHandle, 0);
        rhiCommandBuffer.bindTexture(currentTextureHandle, 1);
        rhiCommandBuffer.bindTexture(nextTextureHandle, 2);
        rhiCommandBuffer.pushInt("uModelMask", 0);
        rhiCommandBuffer.pushInt("uModelDepth", 1);
        rhiCommandBuffer.pushInt("uSceneDepth", 2);
        updateState(rhiCommandBuffer, render3dView, kawaseBloomSettings);
        rhiCommandBuffer.draw(4, 1, 0);
        rhiCommandBuffer.endRenderPass();
        KawasePostProcessor.Surface surface = this.renderer[0];
        if (kawaseBloomSettings.bloomRenderable()) {
          for (int index = 1; index < this.renderer.length; index++) {
            KawasePostProcessor.Surface currentSurface = this.renderer[index - 1];
            updateState3(rhiCommandBuffer, this.renderer[index], nextPipelineHandle, bufferHandle);
            rhiCommandBuffer.bindTexture(currentSurface.texture(), 0);
            rhiCommandBuffer.pushInt("uSourceTexture", 0);
            rhiCommandBuffer.pushVec2(
                "uSourceTexel", 1.0F / currentSurface.width(), 1.0F / currentSurface.height());
            rhiCommandBuffer.pushFloat("uOffset", currentWidth.offsetScale());
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
          }

          surface = this.renderer[this.renderer.length - 1];

          for (int currentIndex = this.renderer.length - 2; currentIndex >= 0; currentIndex += -1) {
            updateState3(
                rhiCommandBuffer,
                this.renderer2[currentIndex],
                previousPipelineHandle,
                bufferHandle);
            rhiCommandBuffer.bindTexture(this.renderer[currentIndex].texture(), 0);
            rhiCommandBuffer.bindTexture(surface.texture(), 1);
            rhiCommandBuffer.pushInt("uHighTexture", 0);
            rhiCommandBuffer.pushInt("uLowTexture", 1);
            rhiCommandBuffer.pushVec2("uLowTexel", 1.0F / surface.width(), 1.0F / surface.height());
            rhiCommandBuffer.pushFloat("uOffset", currentWidth.offsetScale());
            rhiCommandBuffer.pushFloat("uScatter", 0.85F);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
            surface = this.renderer2[currentIndex];
          }
        }

        rhiCommandBuffer.clearScissor();
        rhiCommandBuffer.beginRenderPass(framebufferHandle);
        rhiCommandBuffer.setViewport(0, 0, render3dView.width(), render3dView.height());
        rhiCommandBuffer.bindPipeline(currentPipelineHandle);
        rhiCommandBuffer.bindTexture(previousTextureHandle, 7);
        rhiCommandBuffer.pushInt("uTargetData", 7);
        rhiCommandBuffer.bindTexture(
            sourceTextureHandle.valid() ? sourceTextureHandle : textureHandle, 4);
        rhiCommandBuffer.pushInt("uNearestSeed", 4);
        updateState2(rhiCommandBuffer, "uDamageColor", value);
        rhiCommandBuffer.bindTexture(targetTextureHandle, 8);
        rhiCommandBuffer.pushInt("uForegroundDepth", 8);
        rhiCommandBuffer.pushInt("uForegroundActive", enabled ? 1 : 0);
        rhiCommandBuffer.bindTexture(currentTextureHandle, 9);
        rhiCommandBuffer.pushInt("uEspModelDepth", 9);
        rhiCommandBuffer.bindTexture(nextTextureHandle, 10);
        rhiCommandBuffer.pushInt("uEspSceneDepth", 10);
        rhiCommandBuffer.bindVertexBuffer(bufferHandle, 0);
        rhiCommandBuffer.bindTexture(surface.texture(), 0);
        rhiCommandBuffer.bindTexture(textureHandle, 1);
        rhiCommandBuffer.bindTexture(currentTextureHandle, 2);
        rhiCommandBuffer.bindTexture(nextTextureHandle, 3);
        rhiCommandBuffer.pushInt("uBloomTexture", 0);
        rhiCommandBuffer.pushInt("uModelMask", 1);
        rhiCommandBuffer.pushInt("uModelDepth", 2);
        rhiCommandBuffer.pushInt("uSceneDepth", 3);
        updateState(rhiCommandBuffer, render3dView, kawaseBloomSettings);
        updateState2(rhiCommandBuffer, "uVisibleColor", kawaseBloomSettings.visibleColor());
        updateState2(rhiCommandBuffer, "uOccludedColor", kawaseBloomSettings.occludedColor());
        rhiCommandBuffer.pushFloat(
            "uStrength",
            kawaseBloomSettings.bloomRenderable() ? kawaseBloomSettings.strength() : 0.0F);
        rhiCommandBuffer.pushFloat("uCoreOpacity", kawaseBloomSettings.coreOpacity());

        for (Render3dWantsCaptureService.ScreenRect screenRect : items) {
          rhiCommandBuffer.setScissor(
              screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
          rhiCommandBuffer.draw(4, 1, 0);
        }

        rhiCommandBuffer.clearScissor();
        rhiCommandBuffer.endRenderPass();
      }
    }
  }

  private static void updateState(
      RhiCommandBuffer rhiCommandBuffer,
      Render3dViewService render3dView,
      KawaseBloomSettings kawaseBloomSettings) {
    rhiCommandBuffer.pushFloat("uOcclusionBias", 0.1F);
    rhiCommandBuffer.pushFloat("uVisibleEnabled", kawaseBloomSettings.hasVisible() ? 1.0F : 0.0F);
    rhiCommandBuffer.pushFloat("uOccludedEnabled", kawaseBloomSettings.hasOccluded() ? 1.0F : 0.0F);
    MemoryStack stack = MemoryStack.stackPush();

    try {
      rhiCommandBuffer.pushMatrix4(
          "uInvProjection", render3dView.inverseProjection().get(stack.mallocFloat(16)));
    } catch (Throwable exception) {
      if (stack != null) {
        try {
          stack.close();
        } catch (Throwable currentException) {
          exception.addSuppressed(currentException);
        }
      }

      throw exception;
    }

    if (stack != null) {
      stack.close();
    }
  }

  private static void updateState2(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
    rhiCommandBuffer.pushVec4(
        text,
        (value >>> 16 & 0xFF) / 255.0F,
        (value >>> 8 & 0xFF) / 255.0F,
        (value & 0xFF) / 255.0F,
        (value >>> 24) / 255.0F);
  }

  private static void updateState3(
      RhiCommandBuffer rhiCommandBuffer,
      KawasePostProcessor.Surface surface,
      RhiBlendStateService.PipelineHandle pipelineHandle,
      RhiBlendStateService.BufferHandle bufferHandle) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(surface.framebuffer(), 0.0F, 0.0F, 0.0F, 0.0F);
    rhiCommandBuffer.setViewport(0, 0, surface.width(), surface.height());
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(bufferHandle, 0);
  }

  private void updateState4(Render3dSourceWidthService render3dSourceWidth, int index) {
    if (this.count != render3dSourceWidth.sourceWidth()
        || this.count2 != render3dSourceWidth.sourceHeight()
        || this.renderer.length != index) {
      this.clearTargets();
      this.renderer = new KawasePostProcessor.Surface[index];
      this.renderer2 = new KawasePostProcessor.Surface[index - 1];

      try {
        for (int currentIndex = 0; currentIndex < index; currentIndex++) {
          Render3dSourceWidthService.MipLevel mipLevel = render3dSourceWidth.mip(currentIndex);
          this.renderer[currentIndex] = this.createSurface(mipLevel.width(), mipLevel.height());
          if (currentIndex < index - 1) {
            this.renderer2[currentIndex] = this.createSurface(mipLevel.width(), mipLevel.height());
          }
        }

        this.count = render3dSourceWidth.sourceWidth();
        this.count2 = render3dSourceWidth.sourceHeight();
      } catch (RuntimeException exception) {
        this.clearTargets();
        throw exception;
      }
    }
  }

  private KawasePostProcessor.Surface createSurface(int value, int currentValue) {
    RhiBlendStateService.TextureHandle textureHandle =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                value, currentValue, RhiBlendStateService.TextureFormat.RGBA16F));

    try {
      RhiBlendStateService.FramebufferHandle framebufferHandle =
          this.rhiOperationHandler.createFramebuffer(textureHandle);
      if (textureHandle.valid() && framebufferHandle.valid()) {
        return new KawasePostProcessor.Surface(
            textureHandle, framebufferHandle, value, currentValue);
      } else {
        throw new IllegalStateException("Invalid Kawase target");
      }
    } catch (RuntimeException exception) {
      if (textureHandle.valid()) {
        this.rhiOperationHandler.destroyTexture(textureHandle);
      }

      throw exception;
    }
  }

  void clearTargets() {
    for (KawasePostProcessor.Surface surface : this.renderer) {
      this.updateState5(surface);
    }

    for (KawasePostProcessor.Surface currentSurface : this.renderer2) {
      this.updateState5(currentSurface);
    }

    this.renderer = this.renderer2 = new KawasePostProcessor.Surface[0];
    this.count = this.count2 = 0;
  }

  private void updateState5(KawasePostProcessor.Surface surface) {
    if (surface != null) {
      this.rhiOperationHandler.destroyFramebuffer(surface.framebuffer());
      this.rhiOperationHandler.destroyTexture(surface.texture());
    }
  }

  @Override
  public void close() {
    this.clearTargets();
    this.fmvr01y3d7.close();
    this.shaderRecipeService.close();
    this.shaderRecipeService2.close();
    this.shaderRecipeService3.close();
  }

  private record Surface(
      RhiBlendStateService.TextureHandle texture,
      RhiBlendStateService.FramebufferHandle framebuffer,
      int width,
      int height) {}
}
