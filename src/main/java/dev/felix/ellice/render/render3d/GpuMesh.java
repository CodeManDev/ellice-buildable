package dev.felix.ellice.render.render3d;

import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GpuMesh implements AutoCloseable {
   private static final Logger logger = LoggerFactory.getLogger(GpuMesh.class);
   public static final RhiBlendStateService.VertexLayout VERTEX_LAYOUT = RhiBlendStateService.VertexLayout.of(
      48,
      new RhiBlendStateService.VertexAttribute(0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
      new RhiBlendStateService.VertexAttribute(1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L),
      new RhiBlendStateService.VertexAttribute(2, 2, RhiBlendStateService.VertexFormat.FLOAT, 24L),
      new RhiBlendStateService.VertexAttribute(3, 4, RhiBlendStateService.VertexFormat.FLOAT, 32L)
   );
   private final RhiOperationHandler rhiOperationHandler;
   private GeometryVertexCountService geometryVertexCountService;
   private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
   private RhiBlendStateService.BufferHandle bufferHandle2 = RhiBlendStateService.BufferHandle.NONE;
   private boolean enabled;
   private boolean enabled2;

   GpuMesh(RhiOperationHandler rhiOperation, GeometryVertexCountService geometryVertexCount) {
      this.rhiOperationHandler = Objects.requireNonNull(rhiOperation, "device");
      this.geometryVertexCountService = Objects.requireNonNull(geometryVertexCount, "data");
   }

   public GeometryVertexCountService data() {
      return this.geometryVertexCountService;
   }

   public GeometryVertexCountService.BoundingSphere bounds() {
      return this.geometryVertexCountService.boundingSphere();
   }

   public int vertexCount() {
      return this.geometryVertexCountService.vertexCount();
   }

   public int indexCount() {
      return this.geometryVertexCountService.indexCount();
   }

   public boolean uploaded() {
      return this.enabled;
   }

   public boolean closed() {
      return this.enabled2;
   }

   public void update(GeometryVertexCountService geometryVertexCount) {
      Objects.requireNonNull(geometryVertexCount, "next");
      if (this.enabled2) {
         throw new IllegalStateException("Mesh is closed");
      }

      if (this.enabled
         && geometryVertexCount.vertexCount() == this.geometryVertexCountService.vertexCount()
         && Arrays.equals(geometryVertexCount.indices(), this.geometryVertexCountService.indices())) {
         this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, geometryVertexCount.vertexData());
      } else if (this.enabled) {
         this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
         this.rhiOperationHandler.destroyBuffer(this.bufferHandle2);
         this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
         this.bufferHandle2 = RhiBlendStateService.BufferHandle.NONE;
         this.enabled = false;
      }

      this.geometryVertexCountService = geometryVertexCount;
   }

   void ensureUploaded() {
      if (!this.enabled) {
         if (this.enabled2) {
            throw new IllegalStateException("Mesh is closed");
         }

         RhiBlendStateService.BufferHandle currentBufferHandle = RhiBlendStateService.BufferHandle.NONE;
         RhiBlendStateService.BufferHandle nextBufferHandle = RhiBlendStateService.BufferHandle.NONE;
         ByteBuffer byteBuffer = null;

         try {
            currentBufferHandle = this.rhiOperationHandler
               .createBuffer(RhiBlendStateService.BufferUsage.VERTEX.bit, RhiBlendStateService.BufferAccess.STATIC, this.geometryVertexCountService.vertexData());
            int[] ints = this.geometryVertexCountService.indexData();
            byteBuffer = MemoryUtil.memAlloc(ints.length * 4);
            byteBuffer.asIntBuffer().put(ints);
            nextBufferHandle = this.rhiOperationHandler.createBuffer(RhiBlendStateService.BufferUsage.INDEX.bit, RhiBlendStateService.BufferAccess.STATIC, byteBuffer);
            this.bufferHandle = currentBufferHandle;
            this.bufferHandle2 = nextBufferHandle;
            this.enabled = true;
         } catch (RuntimeException exception) {
            if (currentBufferHandle.valid()) {
               this.rhiOperationHandler.destroyBuffer(currentBufferHandle);
            }

            if (nextBufferHandle.valid()) {
               this.rhiOperationHandler.destroyBuffer(nextBufferHandle);
            }

            throw exception;
         } finally {
            if (byteBuffer != null) {
               MemoryUtil.memFree(byteBuffer);
            }
         }
      }
   }

   public void draw(RhiCommandBuffer rhiCommandBuffer) {
      this.ensureUploaded();
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      rhiCommandBuffer.bindIndexBuffer(this.bufferHandle2, RhiBlendStateService.IndexType.UINT32);
      rhiCommandBuffer.drawIndexed(this.geometryVertexCountService.indexCount(), 1, 0);
   }

   @Override
   public void close() {
      if (!this.enabled2) {
         this.enabled2 = true;

         try {
            if (this.bufferHandle.valid()) {
               this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
            }
         } catch (RuntimeException exception) {
            logger.debug("Skipping vertex buffer delete without GL context", exception);
         }

         try {
            if (this.bufferHandle2.valid()) {
               this.rhiOperationHandler.destroyBuffer(this.bufferHandle2);
            }
         } catch (RuntimeException currentException) {
            logger.debug("Skipping index buffer delete without GL context", currentException);
         }

         this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
         this.bufferHandle2 = RhiBlendStateService.BufferHandle.NONE;
         this.enabled = false;
      }
   }
}

