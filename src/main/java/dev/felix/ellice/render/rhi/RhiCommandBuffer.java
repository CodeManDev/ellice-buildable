package dev.felix.ellice.render.rhi;

import java.nio.FloatBuffer;

public interface RhiCommandBuffer {
  void beginRenderPass(
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      float red,
      float green,
      float blue,
      float alpha);

  void beginRenderPass(RhiBlendStateService.FramebufferHandle framebufferHandle);

  void clearDepth(float value);

  void endRenderPass();

  void bindPipeline(RhiBlendStateService.PipelineHandle pipelineHandle);

  void bindVertexBuffer(RhiBlendStateService.BufferHandle bufferHandle, int offset);

  void bindIndexBuffer(
      RhiBlendStateService.BufferHandle bufferHandle, RhiBlendStateService.IndexType indexType);

  void bindUniformBuffer(RhiBlendStateService.BufferHandle bufferHandle, int offset);

  void bindStorageBuffer(RhiBlendStateService.BufferHandle bufferHandle, int offset);

  void bindTexture(RhiBlendStateService.TextureHandle textureHandle, int textureId);

  void bindTexture(
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.SamplerHandle samplerHandle,
      int textureId);

  void setViewport(int x, int y, int width, int height);

  void setScissor(int x, int y, int width, int height);

  void setColorWriteMask(
      boolean enabled, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled);

  void clearScissor();

  void pushInt(String text, int value);

  void pushFloat(String text, float value);

  void pushVec2(String text, float value, float currentValue);

  void pushVec3(String text, float value, float currentValue, float nextValue);

  void pushVec4(String text, float value, float currentValue, float nextValue, float previousValue);

  void pushMatrix4(String text, FloatBuffer floatBuffer);

  void draw(int x, int y, int width);

  void drawIndexed(int x, int y, int width);

  void dispatch(int value, int currentValue, int nextValue);

  default void blitFramebuffer(
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle,
      int framebufferId,
      int currentFramebufferId,
      int nextFramebufferId,
      int previousFramebufferId,
      int sourceFramebufferId,
      int targetFramebufferId,
      int inputFramebufferId,
      int outputFramebufferId,
      RhiBlendStateService.FilterMode filterMode) {
    this.blitFramebuffer(
        framebufferHandle,
        currentFramebufferHandle,
        framebufferId,
        currentFramebufferId,
        nextFramebufferId,
        previousFramebufferId,
        sourceFramebufferId,
        targetFramebufferId,
        inputFramebufferId,
        outputFramebufferId,
        filterMode,
        RhiBlendStateService.FramebufferAspect.COLOR.bit);
  }

  void blitFramebuffer(
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle,
      int framebufferId,
      int currentFramebufferId,
      int nextFramebufferId,
      int previousFramebufferId,
      int sourceFramebufferId,
      int targetFramebufferId,
      int inputFramebufferId,
      int outputFramebufferId,
      RhiBlendStateService.FilterMode filterMode,
      int resultFramebufferId);

  default void invalidateBindCache() {}
}
