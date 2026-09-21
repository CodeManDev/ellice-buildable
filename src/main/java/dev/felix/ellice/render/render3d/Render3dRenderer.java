package dev.felix.ellice.render.render3d;

import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.util.function.Consumer;
import org.slf4j.LoggerFactory;

public final class Render3dRenderer implements AutoCloseable {
   private final RhiOperationHandler rhiOperationHandler;
   private final Render3dAddService renderer = new Render3dAddService();
   private final SceneObjectRenderer renderer2;
   private RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.TextureHandle textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.FramebufferHandle framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
   private int count;
   private int count2;
   private boolean enabled;
   private boolean enabled2;

   Render3dRenderer(RhiOperationHandler rhiOperation, ShaderOverrideRootService shaderOverrideRoot) {
      this.rhiOperationHandler = rhiOperation;
      this.renderer2 = new SceneObjectRenderer(rhiOperation, shaderOverrideRoot);
   }

   public Render3dAddService scene() {
      return this.renderer;
   }

   public RhiBlendStateService.TextureHandle texture() {
      return this.textureHandle;
   }

   public RhiBlendStateService.TextureHandle depth() {
      return this.textureHandle2;
   }

   public RhiBlendStateService.FramebufferHandle framebuffer() {
      return this.framebufferHandle;
   }

   public int width() {
      return this.count;
   }

   public int height() {
      return this.count2;
   }

   public boolean hasRenderedFrame() {
      return this.enabled2;
   }

   public long targetBytes() {
      return (long)this.count * this.count2 * 8L;
   }

   public void resize(int value, int currentValue) {
      this.updateState(value, currentValue, 2048);
   }

   public void resizeForExport(int value, int currentValue) {
      this.updateState(value, currentValue, 4096);
   }

   private void updateState(int value, int currentValue, int nextValue) {
      if (this.enabled) {
         throw new IllegalStateException("View is closed");
      }

      int previousValue = Math.clamp(value, 32, nextValue);
      int sourceValue = Math.clamp(currentValue, 32, nextValue);
      if (this.count != previousValue || this.count2 != sourceValue) {
         this.updateState2(
            () -> {
               RhiBlendStateService.TextureHandle currentTextureHandle = RhiBlendStateService.TextureHandle.NONE;
               RhiBlendStateService.TextureHandle nextTextureHandle = RhiBlendStateService.TextureHandle.NONE;
               RhiBlendStateService.FramebufferHandle currentFramebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;

               try {
                  currentTextureHandle = this.rhiOperationHandler
                     .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(previousValue, sourceValue, RhiBlendStateService.TextureFormat.RGBA8));
                  nextTextureHandle = this.rhiOperationHandler
                     .createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(previousValue, sourceValue, RhiBlendStateService.TextureFormat.DEPTH24));
                  currentFramebufferHandle = this.rhiOperationHandler.createFramebuffer(currentTextureHandle, nextTextureHandle);
                  RhiCommandBuffer rhiCommandBuffer = this.rhiOperationHandler.encoder();
                  rhiCommandBuffer.clearScissor();
                  rhiCommandBuffer.setColorWriteMask(true, true, true, true);
                  rhiCommandBuffer.beginRenderPass(
                     currentFramebufferHandle, 0.08F, 0.1F, 0.13F, 1.0F
                  );

                  try {
                     rhiCommandBuffer.clearDepth(1.0F);
                  } finally {
                     rhiCommandBuffer.endRenderPass();
                  }
               } catch (RuntimeException exception) {
                  if (currentFramebufferHandle.valid()) {
                     this.rhiOperationHandler.destroyFramebuffer(currentFramebufferHandle);
                  }

                  if (nextTextureHandle.valid()) {
                     this.rhiOperationHandler.destroyTexture(nextTextureHandle);
                  }

                  if (currentTextureHandle.valid()) {
                     this.rhiOperationHandler.destroyTexture(currentTextureHandle);
                  }

                  throw exception;
               }

               this.updateState3();
               this.textureHandle = currentTextureHandle;
               this.textureHandle2 = nextTextureHandle;
               this.framebufferHandle = currentFramebufferHandle;
               this.count = previousValue;
               this.count2 = sourceValue;
               this.enabled2 = false;
            }
         );
      }
   }

   public void render(Render3dViewService render3dView, Consumer<RhiCommandBuffer> consumer) {
      if (!this.enabled && this.framebufferHandle.valid()) {
         if (render3dView.width() == this.count && render3dView.height() == this.count2) {
            this.updateState2(
               () -> {
                  RhiCommandBuffer rhiCommandBuffer = this.rhiOperationHandler.encoder();
                  rhiCommandBuffer.clearScissor();
                  rhiCommandBuffer.setColorWriteMask(true, true, true, true);
                  rhiCommandBuffer.beginRenderPass(
                     this.framebufferHandle,
                     0.08F,
                     0.1F,
                     0.13F,
                     1.0F
                  );
                  rhiCommandBuffer.setViewport(0, 0, this.count, this.count2);
                  rhiCommandBuffer.clearDepth(1.0F);

                  try {
                     if (consumer != null) {
                        consumer.accept(rhiCommandBuffer);
                     }
                  } finally {
                     rhiCommandBuffer.endRenderPass();
                  }

                  this.renderer2.render(this.renderer, rhiCommandBuffer, this.framebufferHandle, render3dView);
               }
            );
            this.enabled2 = true;
         } else {
            throw new IllegalArgumentException("Camera viewport must match the offscreen target");
         }
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

   private void updateState3() {
      if (this.framebufferHandle.valid()) {
         this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
      }

      if (this.textureHandle2.valid()) {
         this.rhiOperationHandler.destroyTexture(this.textureHandle2);
      }

      if (this.textureHandle.valid()) {
         this.rhiOperationHandler.destroyTexture(this.textureHandle);
      }

      this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
      this.textureHandle = this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
      this.count = this.count2 = 0;
      this.enabled2 = false;
   }

   @Override
   public void close() {
      if (!this.enabled) {
         this.enabled = true;

         try {
            Runnable runnable = () -> {
               this.renderer2.close();
               this.updateState3();
            };
            if (this.framebufferHandle.valid()) {
               this.updateState2(runnable);
            } else {
               runnable.run();
            }
         } catch (RuntimeException exception) {
            LoggerFactory.getLogger(Render3dRenderer.class).debug("Offscreen view GL release skipped without context", exception);
            this.renderer.clearObjects();
            this.renderer.clearLights();
            this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
            this.textureHandle = this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
            this.count = this.count2 = 0;
            this.enabled2 = false;
         } finally {
            if (this.framebufferHandle.valid() || this.textureHandle.valid() || this.textureHandle2.valid()) {
               this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
               this.textureHandle = this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
               this.count = this.count2 = 0;
               this.enabled2 = false;
            }

            this.renderer.clearObjects();
            this.renderer.clearLights();
         }
      }
   }
}

