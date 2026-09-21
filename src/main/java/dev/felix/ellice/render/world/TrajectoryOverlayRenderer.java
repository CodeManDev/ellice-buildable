package dev.felix.ellice.render.world;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class TrajectoryOverlayRenderer {
   private static final int count = 9;
   private static final int count2 = 36;
   private RhiOperationHandler rhiOperationHandler;
   private GlRenderer renderer;
   private RhiBlendStateService.ShaderHandle shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
   private RhiBlendStateService.ShaderHandle shaderHandle2 = RhiBlendStateService.ShaderHandle.NONE;
   private RhiBlendStateService.PipelineHandle pipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
   private RhiBlendStateService.PipelineHandle pipelineHandle2 = RhiBlendStateService.PipelineHandle.NONE;
   private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
   private int count3;

   public void render(TrajectoryOverlayRenderer.Trajectory trajectory, EventAttackInputService.WorldRender worldRender, int width, int height, double doubleValue, TrajectoryOverlayRenderer.Style style) {
      if (trajectory != null && trajectory.drawable()) {
         if (width > 0 && height > 0) {
            try {
               this.updateState();
               float[] floats = this.createFloat(trajectory, style.impactMarker());
               int currentLength = floats.length / 9;
               if (currentLength == 0) {
                  return;
               }

               this.updateState2(floats.length);
               this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, floats);
               Vec3 vec3 = worldRender.cameraPos();
               this.renderer.saveGLState();
               this.renderer.clearScissor();
               this.renderer.setViewport(0, 0, width, height);
               int value = calculateValue(0, Math.round(110.0F * calculateValue2(style.glow())));
               this.updateState3(
                  this.pipelineHandle,
                  worldRender,
                  vec3,
                  width,
                  height,
                  doubleValue,
                  currentLength,
                  style.widthPx() * 2.65F,
                  0.42F,
                  0.0F,
                  value,
                  value
               );
               this.updateState3(
                  this.pipelineHandle2,
                  worldRender,
                  vec3,
                  width,
                  height,
                  doubleValue,
                  currentLength,
                  style.widthPx(),
                  0.92F,
                  0.0F,
                  style.startColor(),
                  style.endColor()
               );
               this.renderer.restoreGLState();
            } catch (Exception exception) {
               CoreIsInitializedHandler.LOGGER.error("Trajectory render failed", exception);

               try {
                  if (this.renderer != null) {
                     this.renderer.restoreGLState();
                  }
               } catch (Exception currentException) {
               }
            }
         }
      }
   }

   public void shutdown() {
      if (this.rhiOperationHandler != null) {
         if (this.bufferHandle.valid()) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
         }

         if (this.pipelineHandle.valid()) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
         }

         if (this.pipelineHandle2.valid()) {
            this.rhiOperationHandler.destroyPipeline(this.pipelineHandle2);
         }

         if (this.shaderHandle.valid()) {
            this.rhiOperationHandler.destroyShader(this.shaderHandle);
         }

         if (this.shaderHandle2.valid()) {
            this.rhiOperationHandler.destroyShader(this.shaderHandle2);
         }

         this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
         this.pipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
         this.pipelineHandle2 = RhiBlendStateService.PipelineHandle.NONE;
         this.shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
         this.shaderHandle2 = RhiBlendStateService.ShaderHandle.NONE;
         this.count3 = 0;
         this.rhiOperationHandler = null;
         this.renderer = null;
      }
   }

   private void updateState() {
      if (this.rhiOperationHandler == null) {
         this.rhiOperationHandler = RhiDeviceService.device();
         this.renderer = (GlRenderer)this.rhiOperationHandler.encoder();
         this.shaderHandle = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("trajectory_ribbon"));
         this.shaderHandle2 = this.rhiOperationHandler.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("trajectory_ribbon"));
         RhiBlendStateService.VertexLayout currentVertexLayout = RhiBlendStateService.VertexLayout.of(
            36,
            new RhiBlendStateService.VertexAttribute(0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
            new RhiBlendStateService.VertexAttribute(1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L),
            new RhiBlendStateService.VertexAttribute(2, 1, RhiBlendStateService.VertexFormat.FLOAT, 24L),
            new RhiBlendStateService.VertexAttribute(3, 1, RhiBlendStateService.VertexFormat.FLOAT, 28L),
            new RhiBlendStateService.VertexAttribute(4, 1, RhiBlendStateService.VertexFormat.FLOAT, 32L)
         );
         this.pipelineHandle = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(this.shaderHandle)
                  .fragment(this.shaderHandle2)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.ALPHA)
                  .depthStencil(RhiBlendStateService.DepthStencilState.DISABLED)
                  .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build()
            );
         this.pipelineHandle2 = this.rhiOperationHandler
            .createPipeline(
               RhiBuilderData.graphics()
                  .vertex(this.shaderHandle)
                  .fragment(this.shaderHandle2)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.ALPHA)
                  .depthStencil(RhiBlendStateService.DepthStencilState.READ_ONLY)
                  .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build()
            );
      }
   }

   private void updateState2(int value) {
      if (!this.bufferHandle.valid() || this.count3 < value) {
         if (this.bufferHandle.valid()) {
            this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
         }

         this.count3 = Math.max(value, this.count3 * 2);
         if (this.count3 <= 0) {
            this.count3 = 2048;
         }

         this.bufferHandle = this.rhiOperationHandler
            .createBuffer(
               this.count3 * 4L, RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX), RhiBlendStateService.BufferAccess.STREAM
            );
      }
   }

   private void updateState3(
      RhiBlendStateService.PipelineHandle pipelineHandle,
      EventAttackInputService.WorldRender worldRender,
      Vec3 vec3,
      int value,
      int currentValue,
      double doubleValue,
      int nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      int inputValue,
      int outputValue
   ) {
      this.renderer.bindPipeline(pipelineHandle);
      this.renderer.bindVertexBuffer(this.bufferHandle, 0);
      this.renderer.pushVec3("uCameraPos", (float)vec3.x, (float)vec3.y, (float)vec3.z);
      this.renderer.pushVec2("uResolution", value, currentValue);
      this.renderer.pushFloat("uWidthPx", Math.max(0.5F, previousValue));
      this.renderer.pushFloat("uTime", (float)doubleValue);
      this.renderer.pushFloat("uAlpha", Math.max(0.0F, sourceValue));
      this.renderer.pushFloat("uGlow", Math.max(0.0F, targetValue));
      this.updateState4("uStartColor", inputValue);
      this.updateState4("uEndColor", outputValue);
      MemoryStack stack = MemoryStack.stackPush();

      try {
         FloatBuffer floatBuffer = stack.mallocFloat(16);
         FloatBuffer currentFloatBuffer = stack.mallocFloat(16);
         worldRender.viewMatrix().get(floatBuffer);
         worldRender.projectionMatrix().get(currentFloatBuffer);
         this.renderer.pushMatrix4("uView", floatBuffer);
         this.renderer.pushMatrix4("uProjection", currentFloatBuffer);
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

      this.renderer.draw(nextValue, 1, 0);
   }

   private void updateState4(String text, int value) {
      float currentValue = (value >>> 24 & 0xFF) / 255.0F;
      float nextValue = (value >>> 16 & 0xFF) / 255.0F;
      float previousValue = (value >>> 8 & 0xFF) / 255.0F;
      float sourceValue = (value & 0xFF) / 255.0F;
      this.renderer.pushVec4(text, nextValue, previousValue, sourceValue, currentValue);
   }

   private static int calculateValue(int value, int currentValue) {
      return (currentValue & 0xFF) << 24 | value & 16777215;
   }

   private static float calculateValue2(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }

   private float[] createFloat(TrajectoryOverlayRenderer.Trajectory trajectory, boolean enabled) {
      List items = trajectory.points();
      int currentSize = Math.max(0, items.size() - 1);
      int value = enabled && trajectory.collided() ? 2 : 0;
      float[] floats = new float[(currentSize + value) * 6 * 9];
      if (floats.length == 0) {
         return floats;
      }

      double[] doubles = this.createDouble(items);
      int index = 0;

      for (int currentIndex = 0; currentIndex < items.size() - 1; currentIndex++) {
         float currentValue = 0.58F + 0.42F * (float)doubles[currentIndex + 1];
         index = this.calculateValue4(
            floats, index, (Vec3)items.get(currentIndex), (Vec3)items.get(currentIndex + 1), (float)doubles[currentIndex], (float)doubles[currentIndex + 1], currentValue
         );
      }

      if (value > 0) {
         index = this.calculateValue3(floats, index, trajectory.impact(), trajectory.impactSide());
      }

      if (index == floats.length) {
         return floats;
      }

      float[] currentFloats = new float[index];
      System.arraycopy(floats, 0, currentFloats, 0, index);
      return currentFloats;
   }

   private double[] createDouble(List<Vec3> items) {
      double[] currentSize = new double[items.size()];
      double doubleValue = 0.0;

      for (int index = 1; index < items.size(); index++) {
         doubleValue += ((Vec3)items.get(index - 1)).distanceTo((Vec3)items.get(index));
         currentSize[index] = doubleValue;
      }

      if (doubleValue <= 1.0E-4) {
         return currentSize;
      }

      for (int currentIndex = 1; currentIndex < currentSize.length; currentIndex++) {
         currentSize[currentIndex] /= doubleValue;
      }

      return currentSize;
   }

   private int calculateValue3(float[] floats, int value, Vec3 vec3, Direction direction) {
      Vec3 currentVec3 = direction != null ? direction.getUnitVec3() : new Vec3(0.0, 1.0, 0.0);
      if (currentVec3.lengthSqr() < 0.001) {
         currentVec3 = new Vec3(0.0, 1.0, 0.0);
      }

      currentVec3 = currentVec3.normalize();
      Vec3 nextVec3 = vec3.add(currentVec3.scale(0.035));
      Vec3 previousVec3 = Math.abs(currentVec3.y) > 0.8
         ? new Vec3(1.0, 0.0, 0.0)
         : currentVec3.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
      Vec3 sourceVec3 = currentVec3.cross(previousVec3).normalize();
      value = this.calculateValue4(
         floats,
         value,
         nextVec3.subtract(previousVec3.scale(0.2)),
         nextVec3.add(previousVec3.scale(0.2)),
         0.96F,
         1.0F,
         0.7F
      );
      return this.calculateValue4(
         floats,
         value,
         nextVec3.subtract(sourceVec3.scale(0.2)),
         nextVec3.add(sourceVec3.scale(0.2)),
         0.96F,
         1.0F,
         0.7F
      );
   }

   private int calculateValue4(float[] floats, int value, Vec3 vec3, Vec3 currentVec3, float currentValue, float nextValue, float previousValue) {
      value = this.calculateValue5(floats, value, vec3, currentVec3, -1.0F, currentValue, previousValue);
      value = this.calculateValue5(floats, value, vec3, currentVec3, 1.0F, currentValue, previousValue);
      value = this.calculateValue5(floats, value, currentVec3, vec3, -1.0F, nextValue, previousValue);
      value = this.calculateValue5(floats, value, currentVec3, vec3, -1.0F, nextValue, previousValue);
      value = this.calculateValue5(floats, value, vec3, currentVec3, 1.0F, currentValue, previousValue);
      return this.calculateValue5(floats, value, currentVec3, vec3, 1.0F, nextValue, previousValue);
   }

   private int calculateValue5(float[] floats, int index, Vec3 vec3, Vec3 currentVec3, float value, float currentValue, float nextValue) {
      floats[index++] = (float)vec3.x;
      floats[index++] = (float)vec3.y;
      floats[index++] = (float)vec3.z;
      floats[index++] = (float)currentVec3.x;
      floats[index++] = (float)currentVec3.y;
      floats[index++] = (float)currentVec3.z;
      floats[index++] = value;
      floats[index++] = currentValue;
      floats[index++] = nextValue;
      return index;
   }

   public enum ImpactKind {
      MISS,
      BLOCK,
      ENTITY;


      private static TrajectoryOverlayRenderer.ImpactKind[] $values() {
         return new TrajectoryOverlayRenderer.ImpactKind[]{MISS, BLOCK, ENTITY};
      }
   }

   public record Style(int startColor, int endColor, float widthPx, float glow, boolean impactMarker) {
   }

   public record Trajectory(List<Vec3> points, Vec3 impact, Direction impactSide, TrajectoryOverlayRenderer.ImpactKind impactKind, int ticks) {
      public boolean drawable() {
         return this.points != null && this.points.size() >= 2;
      }

      public boolean collided() {
         return this.impactKind != TrajectoryOverlayRenderer.ImpactKind.MISS;
      }
   }
}
