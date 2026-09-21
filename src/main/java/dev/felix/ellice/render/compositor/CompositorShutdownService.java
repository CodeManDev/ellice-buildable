package dev.felix.ellice.render.compositor;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.ui.theme.ThemeBlurQualityData;

public class CompositorShutdownService {
   private static final int count = 5;
   private final RhiOperationHandler rhiOperationHandler;
   private final RhiBlendStateService.BufferHandle bufferHandle;
   private final RhiBlendStateService.TextureHandle[] textureHandle = new RhiBlendStateService.TextureHandle[5];
   private final RhiBlendStateService.FramebufferHandle[] framebufferHandle = new RhiBlendStateService.FramebufferHandle[5];
   private final int[] int2 = new int[5];
   private final int[] int3 = new int[5];
   private RhiBlendStateService.TextureHandle textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.FramebufferHandle framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
   private RhiBlendStateService.PipelineHandle pipelineHandle;
   private RhiBlendStateService.PipelineHandle pipelineHandle2;
   private int count2;
   private int count3;
   private ThemeBlurQualityData.BlurQuality blurQuality2 = ThemeBlurQualityData.BlurQuality.HIGH;
   private boolean enabled;

   public CompositorShutdownService(RhiOperationHandler rhiOperation, RhiBlendStateService.BufferHandle currentBufferHandle) {
      this.rhiOperationHandler = rhiOperation;
      this.bufferHandle = currentBufferHandle;
   }

   public void init() {
      RhiBlendStateService.ShaderHandle shaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("fullscreen"));
      RhiBlendStateService.ShaderHandle currentShaderHandle = this.rhiOperationHandler
         .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_down"));
      RhiBlendStateService.ShaderHandle nextShaderHandle = this.rhiOperationHandler
         .createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_up"));
      RhiBlendStateService.VertexLayout currentVertexLayout = RhiBlendStateService.VertexLayout.of(
         8, new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L)
      );
      this.pipelineHandle = this.rhiOperationHandler
         .createPipeline(
            RhiBuilderData.graphics()
               .vertex(shaderHandle)
               .fragment(currentShaderHandle)
               .vertexLayout(currentVertexLayout)
               .blend(RhiBlendStateService.BlendState.DISABLED)
               .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
               .build()
         );
      this.pipelineHandle2 = this.rhiOperationHandler
         .createPipeline(
            RhiBuilderData.graphics()
               .vertex(shaderHandle)
               .fragment(nextShaderHandle)
               .vertexLayout(currentVertexLayout)
               .blend(RhiBlendStateService.BlendState.DISABLED)
               .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)
               .build()
         );
      this.rhiOperationHandler.destroyShader(shaderHandle);
      this.rhiOperationHandler.destroyShader(currentShaderHandle);
      this.rhiOperationHandler.destroyShader(nextShaderHandle);
      this.enabled = true;
      CoreIsInitializedHandler.LOGGER.info("BlurPass initialized ({} levels)", 5);
   }

   public void shutdown() {
      if (this.enabled) {
         this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
         this.rhiOperationHandler.destroyPipeline(this.pipelineHandle2);
         this.updateState2();
         this.enabled = false;
      }
   }

   public void execute(RhiCommandBuffer rhiCommandBuffer, RhiBlendStateService.FramebufferHandle currentFramebufferHandle, int value, int currentValue) {
      if (this.enabled) {
         ThemeBlurQualityData themeBlurQualityData = ThemeBlurQualityData.current();
         if (!themeBlurQualityData.blurEnabled()) {
            this.releaseTargets();
         } else {
            if (value != this.count2 || currentValue != this.count3 || this.blurQuality2 != themeBlurQualityData.blurQuality()) {
               this.blurQuality2 = themeBlurQualityData.blurQuality();
               this.updateState(value, currentValue);
               this.count2 = value;
               this.count3 = currentValue;
            }

            rhiCommandBuffer.blitFramebuffer(
               currentFramebufferHandle, this.framebufferHandle2, 0, 0, value, currentValue, 0, 0, this.int2[0], this.int3[0], RhiBlendStateService.FilterMode.LINEAR
            );
            rhiCommandBuffer.blitFramebuffer(
               currentFramebufferHandle,
               this.framebufferHandle[0],
               0,
               0,
               value,
               currentValue,
               0,
               0,
               this.int2[0],
               this.int3[0],
               RhiBlendStateService.FilterMode.LINEAR
            );
            rhiCommandBuffer.bindPipeline(this.pipelineHandle);
            rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);

            for (int index = 1; index < this.blurQuality2.levels(); index++) {
               rhiCommandBuffer.beginRenderPass(this.framebufferHandle[index]);
               rhiCommandBuffer.setViewport(0, 0, this.int2[index], this.int3[index]);
               rhiCommandBuffer.bindTexture(this.textureHandle[index - 1], 0);
               rhiCommandBuffer.pushInt("uTexture", 0);
               rhiCommandBuffer.pushVec2(
                  "uHalfPixel",
                  0.5F * themeBlurQualityData.blurStrength() / this.int2[index - 1],
                  0.5F * themeBlurQualityData.blurStrength() / this.int3[index - 1]
               );
               rhiCommandBuffer.draw(4, 1, 0);
               rhiCommandBuffer.endRenderPass();
            }

            rhiCommandBuffer.bindPipeline(this.pipelineHandle2);
            rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);

            for (int currentIndex = this.blurQuality2.levels() - 1; currentIndex > 0; currentIndex += -1) {
               rhiCommandBuffer.beginRenderPass(this.framebufferHandle[currentIndex - 1]);
               rhiCommandBuffer.setViewport(0, 0, this.int2[currentIndex - 1], this.int3[currentIndex - 1]);
               rhiCommandBuffer.bindTexture(this.textureHandle[currentIndex], 0);
               rhiCommandBuffer.pushInt("uTexture", 0);
               rhiCommandBuffer.pushVec2(
                  "uHalfPixel",
                  0.5F * themeBlurQualityData.blurStrength() / this.int2[currentIndex],
                  0.5F * themeBlurQualityData.blurStrength() / this.int3[currentIndex]
               );
               rhiCommandBuffer.draw(4, 1, 0);
               rhiCommandBuffer.endRenderPass();
            }
         }
      }
   }

   public RhiBlendStateService.TextureHandle texture() {
      return this.textureHandle[0];
   }

   public RhiBlendStateService.TextureHandle sourceTexture() {
      return this.textureHandle2;
   }

   public RhiBlendStateService.TextureHandle texture(int textureId) {
      return this.textureHandle[Math.clamp(textureId, 0, this.blurQuality2.levels() - 1)];
   }

   private void updateState(int value, int currentValue) {
      this.updateState2();
      int nextValue = Math.max(1, value / this.blurQuality2.divisor());
      int previousValue = Math.max(1, currentValue / this.blurQuality2.divisor());
      this.textureHandle2 = this.rhiOperationHandler
         .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(Math.max(1, nextValue), Math.max(1, previousValue), RhiBlendStateService.TextureFormat.RGBA8));
      this.framebufferHandle2 = this.rhiOperationHandler.createFramebuffer(this.textureHandle2);

      for (int index = 0; index < this.blurQuality2.levels(); index++) {
         this.int2[index] = Math.max(1, nextValue);
         this.int3[index] = Math.max(1, previousValue);
         this.textureHandle[index] = this.rhiOperationHandler
            .createTexture(
               RhiBlendStateService.TextureDescriptor.renderTarget(
                  this.int2[index], this.int3[index], RhiBlendStateService.TextureFormat.RGBA8
               )
            );
         this.framebufferHandle[index] = this.rhiOperationHandler.createFramebuffer(this.textureHandle[index]);
         nextValue = Math.max(1, nextValue / 2);
         previousValue = Math.max(1, previousValue / 2);
      }
   }

   public void releaseTargets() {
      this.updateState2();
      this.count2 = this.count3 = 0;
   }

   private void updateState2() {
      if (this.textureHandle2 != null && this.textureHandle2.valid()) {
         this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
         this.rhiOperationHandler.destroyTexture(this.textureHandle2);
         this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
         this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
      }

      for (int index = 0; index < 5; index++) {
         if (this.textureHandle[index] != null && this.textureHandle[index].valid()) {
            this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle[index]);
            this.rhiOperationHandler.destroyTexture(this.textureHandle[index]);
            this.textureHandle[index] = RhiBlendStateService.TextureHandle.NONE;
            this.framebufferHandle[index] = RhiBlendStateService.FramebufferHandle.NONE;
         }
      }
   }
}

