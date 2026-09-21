package dev.felix.ellice.render.compositor;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class CompositorRenderer {
  private final RhiOperationHandler rhiOperationHandler;
  private final RhiBlendStateService.BufferHandle bufferHandle;
  private boolean fgp4kxmstrft;
  private float f3qsv83h1tv6;
  private float f3wi8ko72vxm;
  private boolean enabled2;
  private float fgbsa1q0km1p;
  private boolean filehlzcqm3x;
  private float f19rcorbed5l;
  private boolean enabled4;
  private float fhyvbuntjblk;
  private float f5zfg28s7io3;
  private int count;
  private boolean enabled5;
  private RhiBlendStateService.PipelineHandle pipelineHandle;
  private RhiBlendStateService.PipelineHandle fedjagsxwyhj;
  private RhiBlendStateService.PipelineHandle pipelineHandle3;
  private RhiBlendStateService.PipelineHandle pipelineHandle4;
  private RhiBlendStateService.PipelineHandle pipelineHandle5;
  private RhiBlendStateService.PipelineHandle pipelineHandle6;
  private RhiBlendStateService.TextureHandle textureHandle;
  private RhiBlendStateService.FramebufferHandle fdzibtop8rqz;
  private RhiBlendStateService.TextureHandle textureHandle2;
  private RhiBlendStateService.FramebufferHandle framebufferHandle2;
  private boolean enabled6;
  private static final int count2 = 4;
  private final RhiBlendStateService.TextureHandle[] fhzrxaes7s67;
  private final RhiBlendStateService.FramebufferHandle[] f1bv0si2h1l1;
  private final int[] f3pun78adj6m;
  private final int[] fwo3utbrzxa;
  private RhiBlendStateService.PipelineHandle pipelineHandle7;
  private RhiBlendStateService.PipelineHandle pipelineHandle8;
  private RhiBlendStateService.TextureHandle textureHandle4;
  private RhiBlendStateService.FramebufferHandle framebufferHandle4;
  private int count3;
  private int count4;
  private int count5;
  private int count6;
  private boolean enabled7;

  public CompositorRenderer(
      final RhiOperationHandler rhiOperationHandler,
      final RhiBlendStateService.BufferHandle bufferHandle) {
    this.f3qsv83h1tv6 = Float.intBitsToFloat(1061158912);
    this.f3wi8ko72vxm = Float.intBitsToFloat(1048576000);
    this.fgbsa1q0km1p = Float.intBitsToFloat(994352038);
    this.f19rcorbed5l = Float.intBitsToFloat(1051931443);
    this.fhyvbuntjblk = Float.intBitsToFloat(1056964608);
    this.f5zfg28s7io3 = Float.intBitsToFloat(1060320051);
    this.enabled5 = true;
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.fdzibtop8rqz = RhiBlendStateService.FramebufferHandle.NONE;
    this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
    this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
    this.fhzrxaes7s67 = new RhiBlendStateService.TextureHandle[4];
    this.f1bv0si2h1l1 = new RhiBlendStateService.FramebufferHandle[4];
    this.f3pun78adj6m = new int[4];
    this.fwo3utbrzxa = new int[4];
    this.textureHandle4 = RhiBlendStateService.TextureHandle.NONE;
    this.framebufferHandle4 = RhiBlendStateService.FramebufferHandle.NONE;
    this.rhiOperationHandler = rhiOperationHandler;
    this.bufferHandle = bufferHandle;
  }

  public void setBloom(
      final boolean fgp4kxmstrft, final float f3qsv83h1tv6, final float f3wi8ko72vxm) {
    this.fgp4kxmstrft = fgp4kxmstrft;
    this.f3qsv83h1tv6 = f3qsv83h1tv6;
    this.f3wi8ko72vxm = f3wi8ko72vxm;
  }

  public void setChromatic(final boolean enabled2, final float fgbsa1q0km1p) {
    this.enabled2 = enabled2;
    this.fgbsa1q0km1p = fgbsa1q0km1p;
  }

  public void setVignette(final boolean filehlzcqm3x, final float f19rcorbed5l) {
    this.filehlzcqm3x = filehlzcqm3x;
    this.f19rcorbed5l = f19rcorbed5l;
  }

  public void setMotionBlur(
      final boolean enabled4,
      final float fhyvbuntjblk,
      final float f5zfg28s7io3,
      final int count) {
    if (this.enabled4 != enabled4) {
      this.enabled5 = true;
    }
    this.enabled4 = enabled4;
    this.fhyvbuntjblk = fhyvbuntjblk;
    this.f5zfg28s7io3 = f5zfg28s7io3;
    this.count = count;
  }

  public boolean hasAnyEffect() {
    return this.fgp4kxmstrft || this.enabled2 || this.filehlzcqm3x || this.enabled4;
  }

  public void render(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final int n,
      final int n2) {
    if (!this.hasAnyEffect()) {
      return;
    }
    if (!this.enabled7) {
      this.updateState();
    }
    if (!this.enabled7) {
      return;
    }
    this.m3xwprnugksd(n, n2);
    if (this.enabled4) {
      final int glGetInteger = GL11.glGetInteger(34016);
      final int[] array = new int[4];
      for (int i = 0; i < 4; ++i) {
        GL13.glActiveTexture(33984 + i);
        array[i] = GL11.glGetInteger(32873);
      }
      final RhiBlendStateService.TextureHandle textureHandle =
          this.enabled6 ? this.textureHandle2 : this.textureHandle;
      final RhiBlendStateService.FramebufferHandle framebufferHandle2 =
          this.enabled6 ? this.fdzibtop8rqz : this.framebufferHandle2;
      rhiCommandBuffer.blitFramebuffer(
          framebufferHandle,
          this.framebufferHandle4,
          0,
          0,
          n,
          n2,
          0,
          0,
          n,
          n2,
          RhiBlendStateService.FilterMode.NEAREST);
      rhiCommandBuffer.beginRenderPass(framebufferHandle2);
      rhiCommandBuffer.setViewport(0, 0, n, n2);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle5);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindTexture(this.textureHandle4, 0);
      rhiCommandBuffer.bindTexture(textureHandle, 1);
      rhiCommandBuffer.pushInt("uCurrent", 0);
      rhiCommandBuffer.pushInt("uHistory", 1);
      rhiCommandBuffer.pushFloat("uStrength", this.fhyvbuntjblk);
      rhiCommandBuffer.pushFloat("uPersistence", this.f5zfg28s7io3);
      rhiCommandBuffer.pushInt("uMode", this.count);
      rhiCommandBuffer.pushInt("uFirstFrame", this.enabled5 ? 1 : 0);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
      GL11.glColorMask(true, true, true, this.enabled5 = false);
      rhiCommandBuffer.blitFramebuffer(
          framebufferHandle2,
          framebufferHandle,
          0,
          0,
          n,
          n2,
          0,
          0,
          n,
          n2,
          RhiBlendStateService.FilterMode.NEAREST);
      GL11.glColorMask(true, true, true, true);
      this.enabled6 = !this.enabled6;
      rhiCommandBuffer.beginRenderPass(framebufferHandle);
      rhiCommandBuffer.setViewport(0, 0, n, n2);
      for (int j = 0; j < 4; ++j) {
        GL13.glActiveTexture(33984 + j);
        GL11.glBindTexture(3553, array[j]);
      }
      GL13.glActiveTexture(glGetInteger);
      rhiCommandBuffer.invalidateBindCache();
    }
    if (this.fgp4kxmstrft) {
      rhiCommandBuffer.blitFramebuffer(
          framebufferHandle,
          this.framebufferHandle4,
          0,
          0,
          n,
          n2,
          0,
          0,
          n,
          n2,
          RhiBlendStateService.FilterMode.LINEAR);
      rhiCommandBuffer.bindPipeline(this.fedjagsxwyhj);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[0], 0.0f, 0.0f, 0.0f, 1.0f);
      rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[0], this.fwo3utbrzxa[0]);
      rhiCommandBuffer.bindTexture(this.textureHandle4, 0);
      rhiCommandBuffer.pushInt("uTexture", 0);
      rhiCommandBuffer.pushFloat("uThreshold", this.f3qsv83h1tv6);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
      rhiCommandBuffer.bindPipeline(this.pipelineHandle7);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      for (int k = 1; k < 4; ++k) {
        rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[k], 0.0f, 0.0f, 0.0f, 1.0f);
        rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[k], this.fwo3utbrzxa[k]);
        rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[k - 1], 0);
        rhiCommandBuffer.pushInt("uTexture", 0);
        rhiCommandBuffer.pushVec2(
            "uHalfPixel",
            Float.intBitsToFloat(1056964608) / this.f3pun78adj6m[k - 1],
            Float.intBitsToFloat(1056964608) / this.fwo3utbrzxa[k - 1]);
        rhiCommandBuffer.draw(4, 1, 0);
        rhiCommandBuffer.endRenderPass();
      }
      rhiCommandBuffer.bindPipeline(this.pipelineHandle8);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      for (int l = 3; l > 0; --l) {
        rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[l - 1]);
        rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[l - 1], this.fwo3utbrzxa[l - 1]);
        rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[l], 0);
        rhiCommandBuffer.pushInt("uTexture", 0);
        rhiCommandBuffer.pushVec2(
            "uHalfPixel",
            Float.intBitsToFloat(1056964608) / this.f3pun78adj6m[l],
            Float.intBitsToFloat(1056964608) / this.fwo3utbrzxa[l]);
        rhiCommandBuffer.draw(4, 1, 0);
        rhiCommandBuffer.endRenderPass();
      }
      rhiCommandBuffer.beginRenderPass(framebufferHandle);
      rhiCommandBuffer.setViewport(0, 0, n, n2);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle3);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[0], 0);
      rhiCommandBuffer.pushInt("uBloom", 0);
      rhiCommandBuffer.pushFloat("uIntensity", this.f3wi8ko72vxm);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
    }
    if (this.enabled2) {
      rhiCommandBuffer.blitFramebuffer(
          framebufferHandle,
          this.framebufferHandle4,
          0,
          0,
          n,
          n2,
          0,
          0,
          n,
          n2,
          RhiBlendStateService.FilterMode.LINEAR);
      rhiCommandBuffer.beginRenderPass(framebufferHandle);
      rhiCommandBuffer.setViewport(0, 0, n, n2);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle4);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindTexture(this.textureHandle4, 0);
      rhiCommandBuffer.pushInt("uTexture", 0);
      rhiCommandBuffer.pushFloat("uIntensity", this.fgbsa1q0km1p);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
    }
    if (this.filehlzcqm3x) {
      rhiCommandBuffer.beginRenderPass(framebufferHandle);
      rhiCommandBuffer.setViewport(0, 0, n, n2);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.pushFloat("uIntensity", this.f19rcorbed5l);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
    }
  }

  public void renderBackgroundDesaturate(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final int n,
      final int n2,
      final float b) {
    if (b <= Float.intBitsToFloat(981668463)) {
      return;
    }
    if (!this.enabled7) {
      this.updateState();
    }
    if (!this.enabled7 || this.pipelineHandle6 == null) {
      return;
    }
    this.mh0ekecnghrg(n, n2);
    rhiCommandBuffer.blitFramebuffer(
        framebufferHandle,
        this.framebufferHandle4,
        0,
        0,
        n,
        n2,
        0,
        0,
        n,
        n2,
        RhiBlendStateService.FilterMode.NEAREST);
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, n, n2);
    rhiCommandBuffer.bindPipeline(this.pipelineHandle6);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
    rhiCommandBuffer.bindTexture(this.textureHandle4, 0);
    rhiCommandBuffer.pushInt("uTexture", 0);
    rhiCommandBuffer.pushFloat("uAmount", Math.max(0.0f, Math.min(1.0f, b)));
    GL11.glColorMask(true, true, true, false);
    rhiCommandBuffer.draw(4, 1, 0);
    GL11.glColorMask(true, true, true, true);
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState() {
    try {
      final RhiBlendStateService.VertexLayout of =
          RhiBlendStateService.VertexLayout.of(
              8,
              new RhiBlendStateService.VertexAttribute(
                  0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
      final RhiBlendStateService.ShaderHandle shader =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("fullscreen"));
      final RhiBlendStateService.ShaderHandle shader2 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("vignette"));
      this.pipelineHandle =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader2)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader2);
      final RhiBlendStateService.ShaderHandle shader3 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("bloom_threshold"));
      this.fedjagsxwyhj =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader3)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader3);
      final RhiBlendStateService.ShaderHandle shader4 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("bloom_composite"));
      this.pipelineHandle3 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader4)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.ADDITIVE)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader4);
      final RhiBlendStateService.ShaderHandle shader5 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_down"));
      final RhiBlendStateService.ShaderHandle shader6 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_up"));
      this.pipelineHandle7 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader5)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.pipelineHandle8 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader6)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader5);
      this.rhiOperationHandler.destroyShader(shader6);
      final RhiBlendStateService.ShaderHandle shader7 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("chromatic"));
      this.pipelineHandle4 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader7)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader7);
      final RhiBlendStateService.ShaderHandle shader8 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("motion_blur"));
      this.pipelineHandle5 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader8)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader8);
      final RhiBlendStateService.ShaderHandle shader9 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("desaturate"));
      this.pipelineHandle6 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shader)
                  .fragment(shader9)
                  .vertexLayout(of)
                  .blend(RhiBlendStateService.BlendState.DISABLED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
                  .build());
      this.rhiOperationHandler.destroyShader(shader9);
      this.rhiOperationHandler.destroyShader(shader);
      this.enabled7 = true;
      CoreIsInitializedHandler.LOGGER.info("PostFX stack initialized");
    } catch (final Exception ex) {
      CoreIsInitializedHandler.LOGGER.error("Failed to init PostFX stack", (Throwable) ex);
    }
  }

  private void m3xwprnugksd(int max, int max2) {
    max = Math.max(1, max);
    max2 = Math.max(1, max2);
    if (max == this.count5 && max2 == this.count6) {
      return;
    }
    this.mh0ekecnghrg(this.count5 = max, this.count6 = max2);
    if (this.textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle);
      this.rhiOperationHandler.destroyFramebuffer(this.fdzibtop8rqz);
    }
    if (this.textureHandle2.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle2);
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
    }
    this.textureHandle =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                max, max2, RhiBlendStateService.TextureFormat.RGBA8));
    this.fdzibtop8rqz = this.rhiOperationHandler.createFramebuffer(this.textureHandle);
    this.textureHandle2 =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                max, max2, RhiBlendStateService.TextureFormat.RGBA8));
    this.framebufferHandle2 = this.rhiOperationHandler.createFramebuffer(this.textureHandle2);
    this.enabled6 = false;
    this.enabled5 = true;
    int max3 = max / 2;
    int max4 = max2 / 2;
    for (int i = 0; i < 4; ++i) {
      if (this.fhzrxaes7s67[i] != null && this.fhzrxaes7s67[i].valid()) {
        this.rhiOperationHandler.destroyFramebuffer(this.f1bv0si2h1l1[i]);
        this.rhiOperationHandler.destroyTexture(this.fhzrxaes7s67[i]);
      }
      this.f3pun78adj6m[i] = Math.max(1, max3);
      this.fwo3utbrzxa[i] = Math.max(1, max4);
      this.fhzrxaes7s67[i] =
          this.rhiOperationHandler.createTexture(
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  this.f3pun78adj6m[i],
                  this.fwo3utbrzxa[i],
                  RhiBlendStateService.TextureFormat.R11G11B10F));
      this.f1bv0si2h1l1[i] = this.rhiOperationHandler.createFramebuffer(this.fhzrxaes7s67[i]);
      max3 = Math.max(1, max3 / 2);
      max4 = Math.max(1, max4 / 2);
    }
  }

  private void mh0ekecnghrg(int max, int max2) {
    max = Math.max(1, max);
    max2 = Math.max(1, max2);
    if (this.textureHandle4.valid() && max == this.count3 && max2 == this.count4) {
      return;
    }
    if (this.textureHandle4.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle4);
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle4);
    }
    this.count3 = max;
    this.count4 = max2;
    this.textureHandle4 =
        this.rhiOperationHandler.createTexture(
            RhiBlendStateService.TextureDescriptor.renderTarget(
                max, max2, RhiBlendStateService.TextureFormat.RGBA8));
    this.framebufferHandle4 = this.rhiOperationHandler.createFramebuffer(this.textureHandle4);
  }

  public void shutdown() {
    if (this.textureHandle4.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle4);
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle4);
    }
    if (this.textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle);
      this.rhiOperationHandler.destroyFramebuffer(this.fdzibtop8rqz);
    }
    if (this.textureHandle2.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle2);
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
    }
    for (int i = 0; i < 4; ++i) {
      if (this.fhzrxaes7s67[i] != null && this.fhzrxaes7s67[i].valid()) {
        this.rhiOperationHandler.destroyFramebuffer(this.f1bv0si2h1l1[i]);
        this.rhiOperationHandler.destroyTexture(this.fhzrxaes7s67[i]);
      }
    }
    this.updateState4(this.pipelineHandle);
    this.updateState4(this.fedjagsxwyhj);
    this.updateState4(this.pipelineHandle3);
    this.updateState4(this.pipelineHandle4);
    this.updateState4(this.pipelineHandle5);
    this.updateState4(this.pipelineHandle7);
    this.updateState4(this.pipelineHandle8);
    this.updateState4(this.pipelineHandle6);
  }

  private void updateState4(final RhiBlendStateService.PipelineHandle pipelineHandle) {
    if (pipelineHandle != null && pipelineHandle.valid()) {
      this.rhiOperationHandler.destroyPipeline(pipelineHandle);
    }
  }
}
