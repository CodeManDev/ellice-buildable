package dev.felix.ellice.render.rhi;

import java.nio.ByteBuffer;

public interface RhiOperationHandler {
  RhiBlendStateService.ShaderHandle createShader(
      RhiBlendStateService.ShaderStage shaderStage, String shaderSource);

  void destroyShader(RhiBlendStateService.ShaderHandle shaderHandle);

  RhiBlendStateService.BufferHandle createBuffer(long size, int offset);

  RhiBlendStateService.BufferHandle createBuffer(int bufferId, ByteBuffer byteBuffer);

  RhiBlendStateService.BufferHandle createBuffer(int bufferId, float[] floats);

  RhiBlendStateService.BufferHandle createBuffer(
      long size, int offset, RhiBlendStateService.BufferAccess bufferAccess);

  RhiBlendStateService.BufferHandle createBuffer(
      int bufferId, RhiBlendStateService.BufferAccess bufferAccess, ByteBuffer byteBuffer);

  RhiBlendStateService.BufferHandle createBuffer(
      int bufferId, RhiBlendStateService.BufferAccess bufferAccess, float[] floats);

  void updateBuffer(
      RhiBlendStateService.BufferHandle bufferHandle, long offset, ByteBuffer byteBuffer);

  void updateBuffer(RhiBlendStateService.BufferHandle bufferHandle, long offset, float[] floats);

  void updateBuffer(
      RhiBlendStateService.BufferHandle bufferHandle,
      long offset,
      float[] floats,
      int currentOffset);

  void orphanBuffer(RhiBlendStateService.BufferHandle bufferHandle, long size);

  void destroyBuffer(RhiBlendStateService.BufferHandle bufferHandle);

  RhiBlendStateService.TextureHandle createTexture(
      RhiBlendStateService.TextureDescriptor textureDescriptor);

  RhiBlendStateService.TextureHandle createTexture(
      RhiBlendStateService.TextureDescriptor textureDescriptor, ByteBuffer byteBuffer);

  void updateTexture(
      RhiBlendStateService.TextureHandle textureHandle,
      int textureId,
      int currentTextureId,
      int nextTextureId,
      int previousTextureId,
      ByteBuffer byteBuffer);

  void destroyTexture(RhiBlendStateService.TextureHandle textureHandle);

  default RhiBlendStateService.TextureDownload downloadTexture(
      RhiBlendStateService.TextureHandle textureHandle) {
    return null;
  }

  RhiBlendStateService.SamplerHandle createSampler(
      RhiBlendStateService.SamplerDescriptor samplerDescriptor);

  void destroySampler(RhiBlendStateService.SamplerHandle samplerHandle);

  RhiBlendStateService.FramebufferHandle createFramebuffer(
      RhiBlendStateService.TextureHandle... textureHandles);

  RhiBlendStateService.FramebufferHandle createFramebuffer(
      RhiBlendStateService.TextureHandle textureHandle,
      RhiBlendStateService.TextureHandle currentTextureHandle);

  void destroyFramebuffer(RhiBlendStateService.FramebufferHandle framebufferHandle);

  default RhiBlendStateService.DepthCopyTarget createCompatibleDepthCopyTarget(
      RhiBlendStateService.FramebufferHandle framebufferHandle, int value, int currentValue) {
    return RhiBlendStateService.DepthCopyTarget.NONE;
  }

  default boolean blitFramebufferDepth(
      RhiBlendStateService.FramebufferHandle framebufferHandle,
      RhiBlendStateService.FramebufferHandle currentFramebufferHandle,
      int framebufferId,
      int currentFramebufferId,
      int nextFramebufferId,
      int previousFramebufferId,
      int sourceFramebufferId,
      int targetFramebufferId,
      int inputFramebufferId,
      int outputFramebufferId) {
    return false;
  }

  RhiBlendStateService.PipelineHandle createPipeline(RhiBuilderData rhiBuilderData);

  void destroyPipeline(RhiBlendStateService.PipelineHandle pipelineHandle);

  RhiCommandBuffer encoder();

  default RhiOperationHandler.StateScope preserveState() {
    throw new UnsupportedOperationException("Render state scopes are unavailable");
  }

  @FunctionalInterface
  interface StateScope extends AutoCloseable {
    @Override
    void close();
  }
}
