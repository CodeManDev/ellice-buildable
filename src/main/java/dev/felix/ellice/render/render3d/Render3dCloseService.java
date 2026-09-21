package dev.felix.ellice.render.render3d;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

final class Render3dCloseService implements AutoCloseable {
  private static final long timestamp = 1000000000L;
  private static final long timestamp2 = 5000000000L;
  private static final long timestamp3 = 5000000000L;
  private static final float value = 96.0F;
  private static final float value2 = 64.0F;
  private static final RhiBlendStateService.VertexLayout vertexLayout =
      RhiBlendStateService.VertexLayout.of(
          8,
          new RhiBlendStateService.VertexAttribute(
              0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
  private static final RhiBlendStateService.DepthStencilState depthStencilState =
      new RhiBlendStateService.DepthStencilState(
          true, false, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false);
  private static final RhiBlendStateService.BlendState blendState =
      new RhiBlendStateService.BlendState(
          true,
          RhiBlendStateService.BlendFactor.ONE,
          RhiBlendStateService.BlendFactor.ONE,
          RhiBlendStateService.BlendOp.ADD,
          RhiBlendStateService.BlendFactor.ZERO,
          RhiBlendStateService.BlendFactor.ONE,
          RhiBlendStateService.BlendOp.ADD);
  private static final ShaderDefinition shaderData2 =
      new ShaderDefinition(
          "ellice:selective-glow-mask", "render3d/glow_mask.vert", "render3d/glow_mask.frag");
  private static final ShaderDefinition shaderData22 =
      new ShaderDefinition(
          "ellice:selective-glow-blur", "fullscreen.vert", "render3d/glow_blur.frag");
  private static final ShaderDefinition shaderData23 =
      new ShaderDefinition(
          "ellice:selective-glow-composite", "fullscreen.vert", "render3d/glow_composite.frag");
  private final RhiOperationHandler rhiOperationHandler;
  private final ShaderRecipeService shaderRecipeService;
  private final ShaderRecipeService shaderRecipeService2;
  private final ShaderRecipeService shaderRecipeService3;
  private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle2 =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle3 =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.FramebufferHandle framebufferHandle =
      RhiBlendStateService.FramebufferHandle.NONE;
  private RhiBlendStateService.FramebufferHandle framebufferHandle2 =
      RhiBlendStateService.FramebufferHandle.NONE;
  private RhiBlendStateService.FramebufferHandle framebufferHandle3 =
      RhiBlendStateService.FramebufferHandle.NONE;
  private RhiBlendStateService.DepthCopyTarget depthCopyTarget =
      RhiBlendStateService.DepthCopyTarget.NONE;
  private int count;
  private int count2;
  private int count3;
  private int count4;
  private int count5 = Integer.MIN_VALUE;
  private long timestamp4;
  private long timestamp5;
  private long timestamp6;
  private boolean enabled;

  Render3dCloseService(
      RhiOperationHandler rhiOperation, ShaderOverrideRootService shaderOverrideRoot) {
    this.rhiOperationHandler = rhiOperation;
    this.shaderRecipeService =
        new ShaderRecipeService(
            rhiOperation,
            shaderOverrideRoot,
            new ShaderData(
                shaderData2,
                GpuMesh.VERTEX_LAYOUT,
                RhiBlendStateService.BlendState.DISABLED,
                depthStencilState,
                RhiBlendStateService.RasterizerState.DEFAULT,
                RhiBlendStateService.PrimitiveTopology.TRIANGLES));
    this.shaderRecipeService2 =
        new ShaderRecipeService(
            rhiOperation,
            shaderOverrideRoot,
            new ShaderData(
                shaderData22,
                vertexLayout,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService3 =
        new ShaderRecipeService(
            rhiOperation,
            shaderOverrideRoot,
            new ShaderData(
                shaderData23,
                vertexLayout,
                blendState,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
  }

  void render(
      List<Render3dCloseService.Draw> items,
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      Render3dViewService render3dView) {
    if (!this.enabled) {
      if (items.isEmpty()) {
        this.idle();
      } else {
        this.timestamp6 = System.nanoTime();
        RhiBlendStateService.PipelineHandle pipelineHandle = this.shaderRecipeService.resolve();
        RhiBlendStateService.PipelineHandle currentPipelineHandle =
            this.shaderRecipeService2.resolve();
        RhiBlendStateService.PipelineHandle nextPipelineHandle =
            this.shaderRecipeService3.resolve();
        if (pipelineHandle.valid() && currentPipelineHandle.valid() && nextPipelineHandle.valid()) {
          if (this.checkCondition(framebufferHandle, render3dView.width(), render3dView.height())) {
            this.updateState4();
            rhiCommandBuffer.setColorWriteMask(true, true, true, true);
            rhiCommandBuffer.clearScissor();
            boolean currentWidth =
                this.rhiOperationHandler.blitFramebufferDepth(
                    framebufferHandle,
                    this.depthCopyTarget.framebuffer(),
                    0,
                    0,
                    render3dView.width(),
                    render3dView.height(),
                    0,
                    0,
                    render3dView.width(),
                    render3dView.height());
            if (!currentWidth) {
              this.updateState5("Selective glow depth copy failed");
            } else {
              this.updateState(items, rhiCommandBuffer, pipelineHandle, render3dView);
              float value = 0.0F;

              for (Render3dCloseService.Draw draw : items) {
                value = Math.max(value, draw.glow().radiusPixels());
              }

              value = Math.min(value, 96.0F);
              this.updateState2(
                  rhiCommandBuffer,
                  currentPipelineHandle,
                  this.textureHandle,
                  this.framebufferHandle2,
                  this.count,
                  this.count2,
                  this.count3,
                  this.count4,
                  0.0F,
                  0.0F,
                  0.0F);
              this.updateState2(
                  rhiCommandBuffer,
                  currentPipelineHandle,
                  this.textureHandle2,
                  this.framebufferHandle3,
                  this.count3,
                  this.count4,
                  this.count3,
                  this.count4,
                  value * 0.5F,
                  1.0F,
                  0.0F);
              this.updateState2(
                  rhiCommandBuffer,
                  currentPipelineHandle,
                  this.textureHandle3,
                  this.framebufferHandle2,
                  this.count3,
                  this.count4,
                  this.count3,
                  this.count4,
                  value * 0.5F,
                  0.0F,
                  1.0F);
              this.updateState3(
                  rhiCommandBuffer,
                  nextPipelineHandle,
                  this.textureHandle2,
                  framebufferHandle,
                  render3dView);
            }
          }
        }
      }
    }
  }

  void idle() {
    if (this.depthCopyTarget.valid() && this.timestamp6 != 0L) {
      if (System.nanoTime() - this.timestamp6 >= 5000000000L) {
        this.updateState7();
        this.timestamp6 = 0L;
      }
    }
  }

  void clearWorldTargets() {
    this.updateState7();
    this.timestamp6 = 0L;
  }

  private void updateState(
      List<Render3dCloseService.Draw> items,
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.PipelineHandle pipelineHandle,
      Render3dViewService render3dView) {
    rhiCommandBuffer.beginRenderPass(this.framebufferHandle, 0.0F, 0.0F, 0.0F, 0.0F);
    rhiCommandBuffer.setViewport(0, 0, this.count, this.count2);
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    Matrix4f matrix4f = render3dView.view();
    Matrix4f currentMatrix4f = render3dView.projection();

    for (Render3dCloseService.Draw currentDraw : items) {
      MaterialErFactory.Glow currentGlow = currentDraw.glow();
      MemoryStack stack = MemoryStack.stackPush();

      try {
        updateState9(rhiCommandBuffer, "uModel", currentDraw.model(), stack.mallocFloat(16));
        updateState9(rhiCommandBuffer, "uView", matrix4f, stack.mallocFloat(16));
        updateState9(rhiCommandBuffer, "uProjection", currentMatrix4f, stack.mallocFloat(16));
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

      rhiCommandBuffer.pushVec4(
          "uGlowColor",
          currentGlow.red(),
          currentGlow.green(),
          currentGlow.blue(),
          currentGlow.alpha());
      rhiCommandBuffer.pushFloat("uGlowStrength", Math.min(currentGlow.intensity(), 64.0F));
      currentDraw.mesh().draw(rhiCommandBuffer);
    }

    rhiCommandBuffer.endRenderPass();
  }

  private void updateState2(
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.PipelineHandle pipelineHandle,
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      int value,
      int currentValue,
      int nextValue,
      int previousValue,
      float sourceValue,
      float targetValue,
      float inputValue) {
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, nextValue, previousValue);
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.pushInt("uSourceTexture", 0);
    rhiCommandBuffer.pushVec2("uSourceResolution", value, currentValue);
    rhiCommandBuffer.pushVec2("uDirection", targetValue, inputValue);
    rhiCommandBuffer.pushFloat("uRadius", Math.max(0.0F, sourceValue));
    rhiCommandBuffer.draw(4, 1, 0);
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState3(
      RhiCommandBuffer rhiCommandBuffer,
      RhiBlendStateService.PipelineHandle pipelineHandle,
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      Render3dViewService render3dView) {
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, render3dView.width(), render3dView.height());
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.depthCopyTarget.texture(), 1);
    rhiCommandBuffer.pushInt("uGlowTexture", 0);
    rhiCommandBuffer.pushInt("uDepthTexture", 1);
    rhiCommandBuffer.pushFloat("uOcclusionBias", 0.075F);
    MemoryStack stack = MemoryStack.stackPush();

    try {
      updateState9(
          rhiCommandBuffer,
          "uInvProjection",
          render3dView.inverseProjection(),
          stack.mallocFloat(16));
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

    rhiCommandBuffer.draw(4, 1, 0);
    rhiCommandBuffer.endRenderPass();
  }

  private boolean checkCondition(
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle,
      int value,
      int currentValue) {
    int nextValue = Math.max(1, value);
    int previousValue = Math.max(1, currentValue);
    int sourceValue = Math.max(1, (nextValue + 1) / 2);
    int targetValue = Math.max(1, (previousValue + 1) / 2);
    if (currentFramebufferHandle.id() == this.count5
        && nextValue == this.count
        && previousValue == this.count2
        && this.textureHandle.valid()
        && this.textureHandle2.valid()
        && this.textureHandle3.valid()
        && this.framebufferHandle.valid()
        && this.framebufferHandle2.valid()
        && this.framebufferHandle3.valid()
        && this.depthCopyTarget.valid()) {
      return true;
    }

    long longValue = System.nanoTime();
    if (longValue < this.timestamp4) {
      return false;
    }

    RhiBlendStateService.TextureHandle currentTextureHandle =
        RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.TextureHandle nextTextureHandle = RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.TextureHandle previousTextureHandle =
        RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.FramebufferHandle nextFramebufferHandle =
        RhiBlendStateService.FramebufferHandle.NONE;
    RhiBlendStateService.FramebufferHandle previousFramebufferHandle =
        RhiBlendStateService.FramebufferHandle.NONE;
    RhiBlendStateService.FramebufferHandle sourceFramebufferHandle =
        RhiBlendStateService.FramebufferHandle.NONE;
    RhiBlendStateService.DepthCopyTarget currentDepthCopyTarget =
        RhiBlendStateService.DepthCopyTarget.NONE;

    try {
      currentDepthCopyTarget =
          this.rhiOperationHandler.createCompatibleDepthCopyTarget(
              currentFramebufferHandle, nextValue, previousValue);
      if (!currentDepthCopyTarget.valid()) {
        throw new IllegalStateException("No compatible world-depth target");
      }

      currentTextureHandle =
          this.rhiOperationHandler.createTexture(
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  nextValue, previousValue, RhiBlendStateService.TextureFormat.RGBA16F));
      nextTextureHandle =
          this.rhiOperationHandler.createTexture(
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  sourceValue, targetValue, RhiBlendStateService.TextureFormat.RGBA16F));
      previousTextureHandle =
          this.rhiOperationHandler.createTexture(
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  sourceValue, targetValue, RhiBlendStateService.TextureFormat.RGBA16F));
      nextFramebufferHandle =
          this.rhiOperationHandler.createFramebuffer(
              currentTextureHandle, currentDepthCopyTarget.texture());
      previousFramebufferHandle = this.rhiOperationHandler.createFramebuffer(nextTextureHandle);
      sourceFramebufferHandle = this.rhiOperationHandler.createFramebuffer(previousTextureHandle);
      this.updateState7();
      this.textureHandle = currentTextureHandle;
      this.textureHandle2 = nextTextureHandle;
      this.textureHandle3 = previousTextureHandle;
      this.framebufferHandle = nextFramebufferHandle;
      this.framebufferHandle2 = previousFramebufferHandle;
      this.framebufferHandle3 = sourceFramebufferHandle;
      this.depthCopyTarget = currentDepthCopyTarget;
      this.count = nextValue;
      this.count2 = previousValue;
      this.count3 = sourceValue;
      this.count4 = targetValue;
      this.count5 = currentFramebufferHandle.id();
      this.timestamp4 = 0L;
      CoreIsInitializedHandler.LOGGER.debug(
          "Selective glow targets ready at {}x{} (blur {}x{})",
          new Object[] {this.count, this.count2, this.count3, this.count4});
      return true;
    } catch (RuntimeException exception) {
      this.updateState8(
          nextFramebufferHandle,
          previousFramebufferHandle,
          sourceFramebufferHandle,
          currentDepthCopyTarget,
          currentTextureHandle,
          nextTextureHandle,
          previousTextureHandle);
      this.timestamp4 = longValue + 1000000000L;
      this.updateState6("Selective glow target creation failed: " + exception.getMessage());
      return false;
    }
  }

  private void updateState4() {
    if (!this.bufferHandle.valid()) {
      this.bufferHandle =
          this.rhiOperationHandler.createBuffer(
              RhiBlendStateService.BufferUsage.VERTEX.bit,
              RhiBlendStateService.BufferAccess.STATIC,
              new float[] {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F});
    }
  }

  private void updateState5(String text) {
    this.updateState7();
    this.timestamp4 = System.nanoTime() + 1000000000L;
    this.updateState6(text);
  }

  private void updateState6(String text) {
    long offset = System.nanoTime();
    if (this.timestamp5 == 0L || offset - this.timestamp5 >= 5000000000L) {
      this.timestamp5 = offset;
      CoreIsInitializedHandler.LOGGER.warn(
          "{}; base 3D material remains visible without glow", text);
    }
  }

  private void updateState7() {
    this.updateState8(
        this.framebufferHandle,
        this.framebufferHandle2,
        this.framebufferHandle3,
        this.depthCopyTarget,
        this.textureHandle,
        this.textureHandle2,
        this.textureHandle3);
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
    this.textureHandle3 = RhiBlendStateService.TextureHandle.NONE;
    this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
    this.framebufferHandle3 = RhiBlendStateService.FramebufferHandle.NONE;
    this.depthCopyTarget = RhiBlendStateService.DepthCopyTarget.NONE;
    this.count = 0;
    this.count2 = 0;
    this.count3 = 0;
    this.count4 = 0;
    this.count5 = Integer.MIN_VALUE;
  }

  private void updateState8(
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle,
      RhiBlendStateService.FramebufferHandle nextFramebufferHandle,
      RhiBlendStateService.DepthCopyTarget depthCopyTarget,
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.TextureHandle currentTextureHandle,
      RhiBlendStateService.TextureHandle nextTextureHandle) {
    if (framebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(framebufferHandle);
    }

    if (currentFramebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(currentFramebufferHandle);
    }

    if (nextFramebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(nextFramebufferHandle);
    }

    if (depthCopyTarget.framebuffer().valid()) {
      this.rhiOperationHandler.destroyFramebuffer(depthCopyTarget.framebuffer());
    }

    if (textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(textureHandle);
    }

    if (currentTextureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(currentTextureHandle);
    }

    if (nextTextureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(nextTextureHandle);
    }

    if (depthCopyTarget.texture().valid()) {
      this.rhiOperationHandler.destroyTexture(depthCopyTarget.texture());
    }
  }

  private static void updateState9(
      RhiCommandBuffer rhiCommandBuffer, String text, Matrix4f matrix4f, FloatBuffer floatBuffer) {
    matrix4f.get(floatBuffer);
    rhiCommandBuffer.pushMatrix4(text, floatBuffer);
  }

  @Override
  public void close() {
    if (!this.enabled) {
      this.enabled = true;
      this.shaderRecipeService.close();
      this.shaderRecipeService2.close();
      this.shaderRecipeService3.close();
      this.updateState7();
      if (this.bufferHandle.valid()) {
        this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
      }

      this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    }
  }

  record Draw(GpuMesh mesh, Matrix4f model, MaterialErFactory.Glow glow) {
    Draw(GpuMesh mesh, Matrix4f model, MaterialErFactory.Glow glow) {
      if (mesh != null && model != null && glow != null) {
        model = new Matrix4f(model);
        this.mesh = mesh;
        this.model = model;
        this.glow = glow;
      } else {
        throw new IllegalArgumentException("Glow draw requires mesh, model, and style");
      }
    }
  }
}
