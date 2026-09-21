package dev.felix.ellice.feature.cape;

import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class CapeRenderer implements AutoCloseable {
  private RhiOperationHandler rhiOperationHandler;
  private GlRenderer renderer;
  private RhiBlendStateService.ShaderHandle fjepsae8jmjj = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.ShaderHandle fv70saumqw2 = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle2 =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle3 =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.TextureHandle textureHandle4 =
      RhiBlendStateService.TextureHandle.NONE;
  private RhiBlendStateService.FramebufferHandle framebufferHandle =
      RhiBlendStateService.FramebufferHandle.NONE;
  private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
  private BufferedImage bufferedImage;
  private BufferedImage bufferedImage2;
  private int count;
  private int count2;
  private float[] fzd4crawkjh;
  private boolean enabled;
  private boolean enabled2;

  public RhiBlendStateService.TextureHandle render(
      int n,
      int n2,
      BufferedImage bufferedImage,
      boolean bl,
      BufferedImage bufferedImage2,
      double d,
      double d2,
      double d3,
      double d4) {
    if (this.rhiOperationHandler == null) {
      this.rhiOperationHandler = RhiDeviceService.device();
      this.renderer = (GlRenderer) this.rhiOperationHandler.encoder();
    }
    this.renderer.saveGLState();
    try {
      boolean bl2;
      this.mpt5jbra3fr();
      this.updateState3(n, n2);
      if (this.bufferedImage != bufferedImage) {
        if (this.textureHandle3.valid()) {
          this.rhiOperationHandler.destroyTexture(this.textureHandle3);
        }
        this.textureHandle3 =
            this.createTextureHandle(RhiBlendStateService.TextureHandle.NONE, bufferedImage);
        this.bufferedImage = bufferedImage;
      }
      if (this.bufferedImage2 != bufferedImage2) {
        this.textureHandle4 = this.createTextureHandle(this.textureHandle4, bufferedImage2);
        this.bufferedImage2 = bufferedImage2;
      }
      this.renderer.beginRenderPass(this.framebufferHandle, 0.0f, 0.0f, 0.0f, 0.0f);
      this.renderer.clearScissor();
      this.renderer.setColorWriteMask(true, true, true, true);
      this.renderer.setViewport(0, 0, this.count, this.count2);
      this.renderer.clearDepth(1.0f);
      this.renderer.bindPipeline(this.pipelineHandle);
      Matrix4f matrix4f =
          new Matrix4f()
              .translate(0.0f, Float.intBitsToFloat(1098907648), 0.0f)
              .rotateY((float) Math.toRadians(d))
              .rotateX((float) Math.toRadians(d2))
              .translate(0.0f, Float.intBitsToFloat(-1048576000), 0.0f);
      float f = (float) (Double.longBitsToDouble(4626604192193052672L) / d3);
      float f2 = (float) this.count / (float) this.count2;
      Matrix4f matrix4f2 =
          new Matrix4f()
              .ortho(
                  -f * f2,
                  f * f2,
                  -f,
                  f,
                  Float.intBitsToFloat(0x3DCCCCCD),
                  Float.intBitsToFloat(1128792064))
              .lookAt(
                  0.0f,
                  Float.intBitsToFloat(1101004800),
                  Float.intBitsToFloat(1116471296),
                  0.0f,
                  Float.intBitsToFloat(1098907648),
                  0.0f,
                  0.0f,
                  1.0f,
                  0.0f)
              .mul((Matrix4fc) matrix4f);
      try (MemoryStack memoryStack = MemoryStack.stackPush(); ) {
        this.renderer.pushMatrix4("uMvp", matrix4f2.get(memoryStack.mallocFloat(16)));
        this.renderer.pushMatrix4("uModel", matrix4f.get(memoryStack.mallocFloat(16)));
      }
      this.renderer.pushInt("uTexture", 0);
      boolean bl3 = bl2 = bufferedImage.getHeight() == 32;
      if (this.fzd4crawkjh == null || this.enabled != bl || this.enabled2 != bl2) {
        this.fzd4crawkjh = CapePlayerService.player(bl, bl2);
        this.enabled = bl;
        this.enabled2 = bl2;
      }
      this.mjnrwjpgjb4z(this.fzd4crawkjh, this.textureHandle3);
      this.mjnrwjpgjb4z(CapePlayerService.cape(d4), this.textureHandle4);
      this.renderer.endRenderPass();
      RhiBlendStateService.TextureHandle textureHandle = this.textureHandle;
      return textureHandle;
    } finally {
      this.renderer.restoreGLState();
    }
  }

  private void mpt5jbra3fr() {
    if (this.pipelineHandle.valid()) {
      return;
    }
    this.fjepsae8jmjj =
        this.rhiOperationHandler.createShader(
            RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("cape_preview"));
    this.fv70saumqw2 =
        this.rhiOperationHandler.createShader(
            RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("cape_preview"));
    this.pipelineHandle =
        this.rhiOperationHandler.createPipeline(
            RhiBuilderData.graphics()
                .vertex(this.fjepsae8jmjj)
                .fragment(this.fv70saumqw2)
                .vertexLayout(
                    RhiBlendStateService.VertexLayout.of(
                        32,
                        new RhiBlendStateService.VertexAttribute(
                            0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
                        new RhiBlendStateService.VertexAttribute(
                            1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L),
                        new RhiBlendStateService.VertexAttribute(
                            2, 2, RhiBlendStateService.VertexFormat.FLOAT, 24L)))
                .blend(RhiBlendStateService.BlendState.ALPHA)
                .depthStencil(
                    new RhiBlendStateService.DepthStencilState(
                        true, true, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false))
                .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                .build());
    this.bufferHandle =
        this.rhiOperationHandler.createBuffer(
            18432L,
            RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX),
            RhiBlendStateService.BufferAccess.STREAM);
  }

  private void mjnrwjpgjb4z(float[] fArray, RhiBlendStateService.TextureHandle textureHandle) {
    if (!textureHandle.valid()) {
      return;
    }
    this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, fArray);
    this.renderer.bindVertexBuffer(this.bufferHandle, 0);
    this.renderer.bindTexture(textureHandle, 0);
    this.renderer.draw(fArray.length / 8, 1, 0);
  }

  private void updateState3(int n, int n2) {
    if (this.count == n && this.count2 == n2 && this.framebufferHandle.valid()) {
      return;
    }
    this.updateState4();
    this.count = n;
    this.count2 = n2;
    this.textureHandle =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                n, n2, RhiBlendStateService.TextureFormat.RGBA8));
    this.textureHandle2 =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                n, n2, RhiBlendStateService.TextureFormat.DEPTH32F));
    this.framebufferHandle =
        this.rhiOperationHandler.createFramebuffer(this.textureHandle, this.textureHandle2);
  }

  private RhiBlendStateService.TextureHandle createTextureHandle(
      RhiBlendStateService.TextureHandle textureHandle, BufferedImage bufferedImage) {
    if (bufferedImage == null) {
      return textureHandle;
    }
    ByteBuffer byteBuffer =
        MemoryUtil.memAlloc((int) (bufferedImage.getWidth() * bufferedImage.getHeight() * 4));
    try {
      for (int i = 0; i < bufferedImage.getHeight(); ++i) {
        for (int j = 0; j < bufferedImage.getWidth(); ++j) {
          int n = bufferedImage.getRGB(j, i);
          byteBuffer.put((byte) (n >> 16)).put((byte) (n >> 8)).put((byte) n).put((byte) (n >> 24));
        }
      }
      byteBuffer.flip();
      if (textureHandle.valid()) {
        this.rhiOperationHandler.updateTexture(
            textureHandle, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), byteBuffer);
        RhiBlendStateService.TextureHandle textureHandle2 = textureHandle;
        return textureHandle2;
      }
      RhiBlendStateService.TextureHandle textureHandle3 =
          this.rhiOperationHandler.createTexture(
              new RhiBlendStateService.TextureDescriptor(
                  bufferedImage.getWidth(),
                  bufferedImage.getHeight(),
                  RhiBlendStateService.TextureFormat.RGBA8,
                  RhiBlendStateService.FilterMode.NEAREST,
                  RhiBlendStateService.FilterMode.NEAREST,
                  RhiBlendStateService.AddressMode.CLAMP),
              byteBuffer);
      return textureHandle3;
    } finally {
      MemoryUtil.memFree((ByteBuffer) byteBuffer);
    }
  }

  private void updateState4() {
    if (this.framebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
    }
    if (this.textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle);
    }
    if (this.textureHandle2.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle2);
    }
    this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.textureHandle = this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
  }

  @Override
  public void close() {
    if (this.rhiOperationHandler == null) {
      return;
    }
    this.updateState4();
    if (this.textureHandle3.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle3);
    }
    if (this.textureHandle4.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle4);
    }
    if (this.bufferHandle.valid()) {
      this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
    }
    if (this.pipelineHandle.valid()) {
      this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
    }
    if (this.fjepsae8jmjj.valid()) {
      this.rhiOperationHandler.destroyShader(this.fjepsae8jmjj);
    }
    if (this.fv70saumqw2.valid()) {
      this.rhiOperationHandler.destroyShader(this.fv70saumqw2);
    }
    this.textureHandle3 = this.textureHandle4 = RhiBlendStateService.TextureHandle.NONE;
    this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    this.pipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
    this.fjepsae8jmjj = this.fv70saumqw2 = RhiBlendStateService.ShaderHandle.NONE;
    this.bufferedImage2 = null;
    this.bufferedImage = null;
    this.rhiOperationHandler = null;
  }
}
