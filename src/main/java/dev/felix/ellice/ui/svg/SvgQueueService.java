package dev.felix.ellice.ui.svg;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.compositor.CompositorIntersectService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import java.util.ArrayList;
import java.util.List;

public final class SvgQueueService {
  private RhiOperationHandler rhiOperationHandler;
  private RhiBlendStateService.PipelineHandle pipelineHandle;
  private RhiBlendStateService.BufferHandle bufferHandle;
  private int count;
  private boolean enabled;
  private final List<SvgQueueService.VectorCmd> items = new ArrayList<>();

  public void init(RhiOperationHandler rhiOperation) {
    this.rhiOperationHandler = rhiOperation;

    try {
      RhiBlendStateService.ShaderHandle shaderHandle =
          rhiOperation.createShader(
              RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("vector"));
      RhiBlendStateService.ShaderHandle currentShaderHandle =
          rhiOperation.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("vector"));
      RhiBlendStateService.VertexLayout currentVertexLayout =
          RhiBlendStateService.VertexLayout.of(
              12,
              new RhiBlendStateService.VertexAttribute(
                  0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L),
              new RhiBlendStateService.VertexAttribute(
                  1, 1, RhiBlendStateService.VertexFormat.FLOAT, 8L));
      this.pipelineHandle =
          rhiOperation.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shaderHandle)
                  .fragment(currentShaderHandle)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.PREMULTIPLIED)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build());
      rhiOperation.destroyShader(shaderHandle);
      rhiOperation.destroyShader(currentShaderHandle);
      this.enabled = true;
      CoreIsInitializedHandler.LOGGER.info("Vector renderer initialized");
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Failed to init vector renderer", exception);
    }
  }

  public void queue(SvgQueueService.VectorCmd vectorCmd) {
    this.items.add(vectorCmd);
  }

  public int cmdCount() {
    return this.items.size();
  }

  public void flush(
      RhiCommandBuffer rhiCommandBuffer, int value, int currentValue, double doubleValue) {
    this.flushRange(rhiCommandBuffer, value, currentValue, doubleValue, 0, this.items.size());
    this.items.clear();
  }

  public void flushRange(
      RhiCommandBuffer rhiCommandBuffer,
      int value,
      int currentValue,
      double doubleValue,
      int nextValue,
      int previousValue) {
    if (this.enabled && nextValue < previousValue) {
      if (previousValue > this.items.size()) {
        previousValue = this.items.size();
      }

      if (nextValue < 0) {
        nextValue = 0;
      }

      int index = 0;

      for (int currentIndex = nextValue; currentIndex < previousValue; currentIndex++) {
        index += this.items.get(currentIndex).verts.length;
      }

      this.updateState(index);
      float[] floats = new float[index];
      int sourceValue = 0;

      for (int nextIndex = nextValue; nextIndex < previousValue; nextIndex++) {
        float[] currentFloats = this.items.get(nextIndex).verts;
        System.arraycopy(currentFloats, 0, floats, sourceValue, currentFloats.length);
        sourceValue += currentFloats.length;
      }

      this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, floats);
      rhiCommandBuffer.bindPipeline(this.pipelineHandle);
      rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
      float targetValue = (float) doubleValue;
      int inputValue = 0;

      for (int previousIndex = nextValue; previousIndex < previousValue; previousIndex++) {
        SvgQueueService.VectorCmd vectorCmd = this.items.get(previousIndex);
        rhiCommandBuffer.pushVec2("uResolution", value, currentValue);
        rhiCommandBuffer.pushVec4(
            "uTransform",
            vectorCmd.centerX * targetValue,
            vectorCmd.centerY * targetValue,
            vectorCmd.scaleX * targetValue,
            vectorCmd.scaleY * targetValue);
        rhiCommandBuffer.pushFloat("uRotation", vectorCmd.rotation);
        rhiCommandBuffer.pushVec2("uViewBoxCenter", vectorCmd.vbCenterX, vectorCmd.vbCenterY);
        rhiCommandBuffer.pushVec4("uColor", vectorCmd.r, vectorCmd.g, vectorCmd.b, vectorCmd.a);
        rhiCommandBuffer.pushInt("uEdgeCoverage", vectorCmd.edgeCoverage ? 1 : 0);
        rhiCommandBuffer.pushInt("uVertexAlpha", vectorCmd.vertexAlpha ? 1 : 0);
        rhiCommandBuffer.pushFloat("uDashLength", vectorCmd.dashLength * targetValue);
        rhiCommandBuffer.pushFloat("uDashOffset", vectorCmd.dashOffset * targetValue);
        rhiCommandBuffer.pushFloat("uDashGap", vectorCmd.dashGap);
        CompositorIntersectService.apply(
            rhiCommandBuffer, vectorCmd.clip, targetValue, currentValue);
        rhiCommandBuffer.draw(vectorCmd.vertexCount, 1, inputValue);
        inputValue += vectorCmd.vertexCount;
        CompositorIntersectService.clear(rhiCommandBuffer, vectorCmd.clip);
      }
    }
  }

  public void reset() {
    this.items.clear();
  }

  private void updateState(int value) {
    if (this.bufferHandle == null || value > this.count) {
      if (this.bufferHandle != null) {
        this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
      }

      this.count = Math.max(value, 4096);
      this.bufferHandle =
          this.rhiOperationHandler.createBuffer(
              this.count * 4L,
              RhiBlendStateService.BufferUsage.VERTEX.bit,
              RhiBlendStateService.BufferAccess.STREAM);
    }
  }

  public void shutdown() {
    if (this.pipelineHandle != null) {
      this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
    }

    if (this.bufferHandle != null) {
      this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
    }
  }

  public boolean isInitialized() {
    return this.enabled;
  }

  public record VectorCmd(
      float[] verts,
      int vertexCount,
      float centerX,
      float centerY,
      float scaleX,
      float scaleY,
      float rotation,
      float vbCenterX,
      float vbCenterY,
      float r,
      float g,
      float b,
      float a,
      float dashLength,
      float dashOffset,
      float dashGap,
      float[] clip,
      boolean edgeCoverage,
      boolean vertexAlpha) {
    public VectorCmd(
        float[] floats,
        int value,
        float currentValue,
        float nextValue,
        float previousValue,
        float sourceValue,
        float targetValue,
        float inputValue,
        float outputValue,
        float resultValue,
        float candidateValue,
        float selectedValue,
        float defaultValue,
        float initialValue,
        float resolvedValue,
        float computedValue,
        float[] currentFloats,
        boolean enabled) {
      this(
          floats,
          value,
          currentValue,
          nextValue,
          previousValue,
          sourceValue,
          targetValue,
          inputValue,
          outputValue,
          resultValue,
          candidateValue,
          selectedValue,
          defaultValue,
          initialValue,
          resolvedValue,
          computedValue,
          currentFloats,
          enabled,
          false);
    }

    public VectorCmd(
        float[] floats,
        int value,
        float currentValue,
        float nextValue,
        float previousValue,
        float sourceValue,
        float targetValue,
        float inputValue,
        float outputValue,
        float resultValue,
        float candidateValue,
        float selectedValue,
        float defaultValue,
        float initialValue,
        float resolvedValue,
        float computedValue,
        float[] currentFloats) {
      this(
          floats,
          value,
          currentValue,
          nextValue,
          previousValue,
          sourceValue,
          targetValue,
          inputValue,
          outputValue,
          resultValue,
          candidateValue,
          selectedValue,
          defaultValue,
          initialValue,
          resolvedValue,
          computedValue,
          currentFloats,
          false);
    }
  }
}
