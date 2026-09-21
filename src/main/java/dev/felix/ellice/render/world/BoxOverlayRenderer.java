package dev.felix.ellice.render.world;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class BoxOverlayRenderer {
  private static final int count = 3;
  private static final int flc1c1j7mr3 = 12;
  private RhiOperationHandler rhiOperationHandler;
  private GlRenderer renderer;
  private RhiBlendStateService.ShaderHandle shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.ShaderHandle fz58j51n7t0 = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle2 =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
  private int fbnfzfijo0fe;

  public void render(
      List<AABB> list,
      EventAttackInputService.WorldRender worldRender,
      int n,
      int n2,
      Style style) {
    if (list == null || list.isEmpty()) {
      return;
    }
    if (n <= 0 || n2 <= 0) {
      return;
    }
    if (!style.fill() && !style.outline()) {
      return;
    }
    try {
      this.updateState();
      float[] fArray = style.fill() ? this.createFloat(list) : new float[] {};
      float[] fArray2 =
          style.outline()
              ? this.createFloat2(
                  list, Math.max(Float.intBitsToFloat(973279855), style.edgeWidth()))
              : new float[] {};
      int n3 = fArray.length / 3;
      int n4 = fArray2.length / 3;
      int n5 = fArray.length + fArray2.length;
      if (n5 == 0) {
        return;
      }
      this.updateState2(n5);
      if (fArray.length > 0) {
        this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, fArray);
      }
      if (fArray2.length > 0) {
        this.rhiOperationHandler.updateBuffer(
            this.bufferHandle, (long) fArray.length * 4L, fArray2);
      }
      Vec3 vec3 = worldRender.cameraPos();
      this.renderer.saveGLState();
      this.renderer.clearScissor();
      this.renderer.setViewport(0, 0, n, n2);
      this.renderer.bindPipeline(style.seeThrough() ? this.pipelineHandle2 : this.pipelineHandle);
      this.renderer.bindVertexBuffer(this.bufferHandle, 0);
      this.updateState3(worldRender, vec3);
      if (n3 > 0) {
        this.mg6nkfkdpkwm(style.fillColor());
        this.renderer.draw(n3, 1, 0);
      }
      if (n4 > 0) {
        this.mg6nkfkdpkwm(style.outlineColor());
        this.renderer.draw(n4, 1, n3);
      }
      this.renderer.restoreGLState();
    } catch (Exception exception) {
      CoreIsInitializedHandler.LOGGER.error("Block overlay render failed", (Throwable) exception);
      try {
        if (this.renderer != null) {
          this.renderer.restoreGLState();
        }
      } catch (Exception exception2) {

      }
    }
  }

  public void shutdown() {
    if (this.rhiOperationHandler == null) {
      return;
    }
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
    if (this.fz58j51n7t0.valid()) {
      this.rhiOperationHandler.destroyShader(this.fz58j51n7t0);
    }
    this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    this.pipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
    this.pipelineHandle2 = RhiBlendStateService.PipelineHandle.NONE;
    this.shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
    this.fz58j51n7t0 = RhiBlendStateService.ShaderHandle.NONE;
    this.fbnfzfijo0fe = 0;
    this.rhiOperationHandler = null;
    this.renderer = null;
  }

  private void updateState() {
    if (this.rhiOperationHandler != null) {
      return;
    }
    this.rhiOperationHandler = RhiDeviceService.device();
    this.renderer = (GlRenderer) this.rhiOperationHandler.encoder();
    this.shaderHandle =
        this.rhiOperationHandler.createShader(
            RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("block_overlay"));
    this.fz58j51n7t0 =
        this.rhiOperationHandler.createShader(
            RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("block_overlay"));
    RhiBlendStateService.VertexLayout vertexLayout =
        RhiBlendStateService.VertexLayout.of(
            12,
            new RhiBlendStateService.VertexAttribute(
                0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L));
    RhiBlendStateService.DepthStencilState depthStencilState =
        new RhiBlendStateService.DepthStencilState(
            true, false, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false);
    this.pipelineHandle =
        this.rhiOperationHandler.createPipeline(
            RhiBuilderData.graphics()
                .vertex(this.shaderHandle)
                .fragment(this.fz58j51n7t0)
                .vertexLayout(vertexLayout)
                .blend(RhiBlendStateService.BlendState.ALPHA)
                .depthStencil(depthStencilState)
                .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                .build());
    this.pipelineHandle2 =
        this.rhiOperationHandler.createPipeline(
            RhiBuilderData.graphics()
                .vertex(this.shaderHandle)
                .fragment(this.fz58j51n7t0)
                .vertexLayout(vertexLayout)
                .blend(RhiBlendStateService.BlendState.ALPHA)
                .depthStencil(RhiBlendStateService.DepthStencilState.DISABLED)
                .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                .build());
  }

  private void updateState2(int n) {
    if (this.bufferHandle.valid() && this.fbnfzfijo0fe >= n) {
      return;
    }
    if (this.bufferHandle.valid()) {
      this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
    }
    this.fbnfzfijo0fe = Math.max(n, this.fbnfzfijo0fe * 2);
    if (this.fbnfzfijo0fe <= 0) {
      this.fbnfzfijo0fe = 4096;
    }
    this.bufferHandle =
        this.rhiOperationHandler.createBuffer(
            (long) this.fbnfzfijo0fe * 4L,
            RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX),
            RhiBlendStateService.BufferAccess.STREAM);
  }

  private void updateState3(EventAttackInputService.WorldRender worldRender, Vec3 vec3) {
    this.renderer.pushVec3("uCameraPos", (float) vec3.x, (float) vec3.y, (float) vec3.z);
    try (MemoryStack memoryStack = MemoryStack.stackPush(); ) {
      FloatBuffer floatBuffer = memoryStack.mallocFloat(16);
      FloatBuffer floatBuffer2 = memoryStack.mallocFloat(16);
      worldRender.viewMatrix().get(floatBuffer);
      worldRender.projectionMatrix().get(floatBuffer2);
      this.renderer.pushMatrix4("uView", floatBuffer);
      this.renderer.pushMatrix4("uProjection", floatBuffer2);
    }
  }

  private void mg6nkfkdpkwm(int n) {
    this.updateState5("uColor", n);
  }

  private void updateState5(String string, int n) {
    float f = (float) (n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
    float f2 = (float) (n >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544);
    float f3 = (float) (n >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544);
    float f4 = (float) (n & 0xFF) / Float.intBitsToFloat(1132396544);
    this.renderer.pushVec4(string, f2, f3, f4, f);
  }

  private float[] createFloat(List<AABB> list) {
    float[] fArray = new float[list.size() * 36 * 3];
    int n = 0;
    for (AABB aABB : list) {
      n = this.calculateValue(fArray, n, aABB);
    }
    return BoxOverlayRenderer.createFloat3(fArray, n);
  }

  private float[] createFloat2(List<AABB> list, float f) {
    ArrayList<AABB> arrayList = new ArrayList<AABB>(list.size() * 12);
    for (AABB aABB : list) {
      this.updateState6(arrayList, aABB, f);
    }
    Object object = new float[arrayList.size() * 36 * 3];
    int n = 0;
    for (AABB aABB : arrayList) {
      n = this.calculateValue((float[]) object, n, aABB);
    }
    return BoxOverlayRenderer.createFloat3((float[]) object, n);
  }

  private void updateState6(List<AABB> list, AABB aABB, float f) {
    double d = (double) f * Double.longBitsToDouble(4602678819172646912L);
    double d2 = aABB.minX;
    double d3 = aABB.minY;
    double d4 = aABB.minZ;
    double d5 = aABB.maxX;
    double d6 = aABB.maxY;
    double d7 = aABB.maxZ;
    for (double d8 : new double[] {d3, d6}) {
      for (double d9 : new double[] {d4, d7}) {
        list.add(new AABB(d2, d8 - d, d9 - d, d5, d8 + d, d9 + d));
      }
    }
    for (double d8 : new double[] {d2, d5}) {
      for (double d9 : new double[] {d4, d7}) {
        list.add(new AABB(d8 - d, d3, d9 - d, d8 + d, d6, d9 + d));
      }
    }
    for (double d8 : new double[] {d2, d5}) {
      for (double d9 : new double[] {d3, d6}) {
        list.add(new AABB(d8 - d, d9 - d, d4, d8 + d, d9 + d, d7));
      }
    }
  }

  private int calculateValue(float[] fArray, int n, AABB aABB) {
    float f = (float) aABB.minX;
    float f2 = (float) aABB.minY;
    float f3 = (float) aABB.minZ;
    float f4 = (float) aABB.maxX;
    float f5 = (float) aABB.maxY;
    float f6 = (float) aABB.maxZ;
    n = this.calculateValue2(fArray, n, f, f2, f6, f4, f2, f6, f4, f5, f6, f, f5, f6);
    n = this.calculateValue2(fArray, n, f4, f2, f3, f, f2, f3, f, f5, f3, f4, f5, f3);
    n = this.calculateValue2(fArray, n, f, f2, f3, f, f2, f6, f, f5, f6, f, f5, f3);
    n = this.calculateValue2(fArray, n, f4, f2, f6, f4, f2, f3, f4, f5, f3, f4, f5, f6);
    n = this.calculateValue2(fArray, n, f, f5, f6, f4, f5, f6, f4, f5, f3, f, f5, f3);
    return this.calculateValue2(fArray, n, f, f2, f3, f4, f2, f3, f4, f2, f6, f, f2, f6);
  }

  private int calculateValue2(
      float[] fArray,
      int n,
      float f,
      float f2,
      float f3,
      float f4,
      float f5,
      float f6,
      float f7,
      float f8,
      float f9,
      float f10,
      float f11,
      float f12) {
    n = this.calculateValue3(fArray, n, f, f2, f3);
    n = this.calculateValue3(fArray, n, f4, f5, f6);
    n = this.calculateValue3(fArray, n, f7, f8, f9);
    n = this.calculateValue3(fArray, n, f, f2, f3);
    n = this.calculateValue3(fArray, n, f7, f8, f9);
    return this.calculateValue3(fArray, n, f10, f11, f12);
  }

  private int calculateValue3(float[] fArray, int n, float f, float f2, float f3) {
    fArray[n++] = f;
    fArray[n++] = f2;
    fArray[n++] = f3;
    return n;
  }

  private static float[] createFloat3(float[] fArray, int n) {
    if (n == fArray.length) {
      return fArray;
    }
    float[] fArray2 = new float[n];
    System.arraycopy(fArray, 0, fArray2, 0, n);
    return fArray2;
  }

  public record Style(
      int fillColor,
      int outlineColor,
      float edgeWidth,
      boolean fill,
      boolean outline,
      boolean seeThrough) {}
}
