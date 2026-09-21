package dev.felix.ellice.render.world;

import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class WorldRenderer implements AutoCloseable {
  private RhiOperationHandler rhiOperationHandler;
  private final List<RhiBlendStateService.ShaderHandle> items = new ArrayList<>();
  private final Map<WorldData, WorldRenderer.GpuShape> entries = new IdentityHashMap<>();
  private RhiBlendStateService.PipelineHandle pipelineHandle =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle2 =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle3 =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle4 =
      RhiBlendStateService.PipelineHandle.NONE;

  public WorldRenderer() {}

  public WorldRenderer(RhiOperationHandler rhiOperation) {
    this.rhiOperationHandler = rhiOperation;
  }

  public void render(
      List<WorldRenderer.Selection> items,
      EventAttackInputService.WorldRender worldRender,
      int width,
      int height,
      WorldRenderer.Style style,
      float value) {
    if (width > 0 && height > 0 && !items.isEmpty() && (style.fill() || style.outline())) {
      if (this.rhiOperationHandler == null) {
        this.rhiOperationHandler = RhiDeviceService.device();
      }

      try (RhiOperationHandler.StateScope stateScope = this.rhiOperationHandler.preserveState()) {
        this.updateState2();
        RhiCommandBuffer rhiCommandBuffer = this.rhiOperationHandler.encoder();
        rhiCommandBuffer.clearScissor();
        rhiCommandBuffer.setViewport(0, 0, width, height);
        rhiCommandBuffer.setColorWriteMask(true, true, true, false);
        Set values = Collections.newSetFromMap(new IdentityHashMap());

        for (WorldRenderer.Selection selection : items) {
          values.add(selection.mesh());
          WorldRenderer.GpuShape gpuShape =
              this.entries.computeIfAbsent(selection.mesh(), this::createGpuShape);
          if (!(selection.opacity() <= 0.001F)) {
            if (style.fill() && gpuShape.faceCount() > 0) {
              rhiCommandBuffer.bindPipeline(
                  style.seeThrough() ? this.pipelineHandle2 : this.pipelineHandle);
              this.updateState(
                  rhiCommandBuffer, worldRender, selection, style, width, height, value);
              rhiCommandBuffer.bindVertexBuffer(gpuShape.faces(), 0);
              rhiCommandBuffer.draw(gpuShape.faceCount(), 1, 0);
            }

            if (style.outline() && gpuShape.edgeCount() > 0) {
              rhiCommandBuffer.bindPipeline(
                  style.seeThrough() ? this.pipelineHandle4 : this.pipelineHandle3);
              this.updateState(
                  rhiCommandBuffer, worldRender, selection, style, width, height, value);
              rhiCommandBuffer.bindVertexBuffer(gpuShape.edges(), 0);
              rhiCommandBuffer.draw(gpuShape.edgeCount(), 1, 0);
            }
          }
        }

        this.entries
            .entrySet()
            .removeIf(
                entry -> {
                  if (values.contains(entry.getKey())) {
                    return false;
                  }

                  this.updateState4(entry.getValue());
                  return true;
                });
      }
    }
  }

  private void updateState(
      RhiCommandBuffer rhiCommandBuffer,
      EventAttackInputService.WorldRender worldRender,
      WorldRenderer.Selection selection,
      WorldRenderer.Style style,
      int value,
      int currentValue,
      float nextValue) {
    Vec3 vec3 = selection.origin().subtract(worldRender.cameraPos());
    rhiCommandBuffer.pushVec3("uOrigin", (float) vec3.x, (float) vec3.y, (float) vec3.z);
    rhiCommandBuffer.pushVec2("uViewport", value, currentValue);
    rhiCommandBuffer.pushFloat("uPadding", style.padding());
    rhiCommandBuffer.pushFloat("uLineWidth", style.lineWidth());
    rhiCommandBuffer.pushFloat("uGlow", style.glow());
    rhiCommandBuffer.pushFloat("uOpacity", selection.opacity());
    rhiCommandBuffer.pushFloat("uTime", nextValue);
    rhiCommandBuffer.pushInt("uMaterial", style.material());
    updateState3(rhiCommandBuffer, "uFill", style.fillColor());
    updateState3(rhiCommandBuffer, "uEdge", style.edgeColor());
    updateState3(rhiCommandBuffer, "uSecondary", style.secondaryColor());
    MemoryStack stack = MemoryStack.stackPush();

    try {
      rhiCommandBuffer.pushMatrix4("uView", worldRender.viewMatrix().get(stack.mallocFloat(16)));
      rhiCommandBuffer.pushMatrix4(
          "uProjection", worldRender.projectionMatrix().get(stack.mallocFloat(16)));
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
  }

  private void updateState2() {
    if (!this.pipelineHandle.valid()) {
      RhiBlendStateService.ShaderHandle shaderHandle =
          this.createShaderHandle("block_selection", RhiBlendStateService.ShaderStage.VERTEX);
      RhiBlendStateService.ShaderHandle currentShaderHandle =
          this.createShaderHandle("block_selection", RhiBlendStateService.ShaderStage.FRAGMENT);
      RhiBlendStateService.ShaderHandle nextShaderHandle =
          this.createShaderHandle("block_selection_edge", RhiBlendStateService.ShaderStage.VERTEX);
      RhiBlendStateService.ShaderHandle previousShaderHandle =
          this.createShaderHandle(
              "block_selection_edge", RhiBlendStateService.ShaderStage.FRAGMENT);
      RhiBlendStateService.VertexLayout vertexLayout =
          RhiBlendStateService.VertexLayout.of(
              24,
              new RhiBlendStateService.VertexAttribute(
                  0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
              new RhiBlendStateService.VertexAttribute(
                  1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L));
      RhiBlendStateService.VertexLayout currentVertexLayout =
          RhiBlendStateService.VertexLayout.of(
              32,
              new RhiBlendStateService.VertexAttribute(
                  0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
              new RhiBlendStateService.VertexAttribute(
                  1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L),
              new RhiBlendStateService.VertexAttribute(
                  2, 2, RhiBlendStateService.VertexFormat.FLOAT, 24L));
      this.pipelineHandle =
          this.createPipelineHandle(shaderHandle, currentShaderHandle, vertexLayout, true);
      this.pipelineHandle2 =
          this.createPipelineHandle(shaderHandle, currentShaderHandle, vertexLayout, false);
      this.pipelineHandle3 =
          this.createPipelineHandle(
              nextShaderHandle, previousShaderHandle, currentVertexLayout, true);
      this.pipelineHandle4 =
          this.createPipelineHandle(
              nextShaderHandle, previousShaderHandle, currentVertexLayout, false);
    }
  }

  private RhiBlendStateService.ShaderHandle createShaderHandle(
      String shaderName, RhiBlendStateService.ShaderStage shaderStage) {
    RhiBlendStateService.ShaderHandle shaderHandle =
        this.rhiOperationHandler.createShader(
            shaderStage,
            shaderStage == RhiBlendStateService.ShaderStage.VERTEX
                ? RhiRepository.vertex(shaderName)
                : RhiRepository.fragment(shaderName));
    this.items.add(shaderHandle);
    return shaderHandle;
  }

  private RhiBlendStateService.PipelineHandle createPipelineHandle(
      RhiBlendStateService.ShaderHandle shaderHandle,
      RhiBlendStateService.ShaderHandle currentShaderHandle,
      RhiBlendStateService.VertexLayout currentVertexLayout,
      boolean enabled) {
    return this.rhiOperationHandler.createPipeline(
        RhiBuilderData.graphics()
            .vertex(shaderHandle)
            .fragment(currentShaderHandle)
            .vertexLayout(currentVertexLayout)
            .blend(RhiBlendStateService.BlendState.ALPHA)
            .depthStencil(
                new RhiBlendStateService.DepthStencilState(
                    enabled, false, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false))
            .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
            .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
            .build());
  }

  private WorldRenderer.GpuShape createGpuShape(WorldData worldData) {
    RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    RhiBlendStateService.BufferHandle currentBufferHandle = RhiBlendStateService.BufferHandle.NONE;

    try {
      if (worldData.faces().length > 0) {
        bufferHandle =
            this.rhiOperationHandler.createBuffer(
                RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX),
                RhiBlendStateService.BufferAccess.STATIC,
                worldData.faces());
      }

      if (worldData.edges().length > 0) {
        currentBufferHandle =
            this.rhiOperationHandler.createBuffer(
                RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX),
                RhiBlendStateService.BufferAccess.STATIC,
                worldData.edges());
      }

      return new WorldRenderer.GpuShape(
          bufferHandle,
          currentBufferHandle,
          worldData.faces().length / 6,
          worldData.edges().length / 8);
    } catch (RuntimeException exception) {
      this.rhiOperationHandler.destroyBuffer(bufferHandle);
      this.rhiOperationHandler.destroyBuffer(currentBufferHandle);
      throw exception;
    }
  }

  private static void updateState3(RhiCommandBuffer rhiCommandBuffer, String text, int value) {
    rhiCommandBuffer.pushVec4(
        text,
        (value >> 16 & 0xFF) / 255.0F,
        (value >> 8 & 0xFF) / 255.0F,
        (value & 0xFF) / 255.0F,
        (value >>> 24) / 255.0F);
  }

  private void updateState4(WorldRenderer.GpuShape gpuShape) {
    this.rhiOperationHandler.destroyBuffer(gpuShape.faces());
    this.rhiOperationHandler.destroyBuffer(gpuShape.edges());
  }

  @Override
  public void close() {
    if (this.rhiOperationHandler != null) {
      this.entries.values().forEach(this::updateState4);
      this.entries.clear();

      for (RhiBlendStateService.PipelineHandle currentPipelineHandle :
          List.of(
              this.pipelineHandle,
              this.pipelineHandle2,
              this.pipelineHandle3,
              this.pipelineHandle4)) {
        this.rhiOperationHandler.destroyPipeline(currentPipelineHandle);
      }

      this.items.forEach(this.rhiOperationHandler::destroyShader);
      this.items.clear();
      this.pipelineHandle =
          this.pipelineHandle2 =
              this.pipelineHandle3 =
                  this.pipelineHandle4 = RhiBlendStateService.PipelineHandle.NONE;
    }
  }

  private record GpuShape(
      RhiBlendStateService.BufferHandle faces,
      RhiBlendStateService.BufferHandle edges,
      int faceCount,
      int edgeCount) {}

  public record Selection(WorldData mesh, Vec3 origin, float opacity) {}

  public record Style(
      int fillColor,
      int edgeColor,
      int secondaryColor,
      float lineWidth,
      float glow,
      float padding,
      boolean fill,
      boolean outline,
      boolean seeThrough,
      int material) {}
}
