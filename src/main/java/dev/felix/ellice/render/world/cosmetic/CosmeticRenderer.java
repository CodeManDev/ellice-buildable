package dev.felix.ellice.render.world.cosmetic;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class CosmeticRenderer implements CosmeticOperationHandler<CosmeticRenderer.Style> {
  private static final int count = 10;
  private static final int count2 = 18;
  private static final int count3 = 10;
  private static final int count4 = 40;
  private RhiOperationHandler rhiOperationHandler;
  private GlRenderer renderer;
  private RhiBlendStateService.ShaderHandle shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.ShaderHandle shaderHandle2 = RhiBlendStateService.ShaderHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.PipelineHandle pipelineHandle2 =
      RhiBlendStateService.PipelineHandle.NONE;
  private RhiBlendStateService.BufferHandle bufferHandle = RhiBlendStateService.BufferHandle.NONE;
  private int count5;
  private final Map<UUID, CosmeticRenderer.CapeState> entries = new HashMap<>();

  public void render(
      Collection<? extends Player> items, CosmeticData cosmeticData, CosmeticRenderer.Style style) {
    if (items != null && !items.isEmpty()) {
      if (cosmeticData.framebufferWidth() > 0 && cosmeticData.framebufferHeight() > 0) {
        if (!(style.opacity() <= 0.01F)
            && !(style.length() <= 0.05F)
            && !(style.width() <= 0.05F)) {
          byte byteValue = 0;

          try {
            this.updateState();
            CosmeticRenderer.Mesh mesh = this.createMesh(items, cosmeticData, style);
            if (mesh.vertexCount() != 0) {
              this.updateState2(mesh.floatCount());
              this.rhiOperationHandler.updateBuffer(
                  this.bufferHandle, 0L, mesh.vertices(), mesh.floatCount());
              this.renderer.saveGLState();
              byteValue = 1;
              this.renderer.clearScissor();
              this.renderer.setViewport(
                  0, 0, cosmeticData.framebufferWidth(), cosmeticData.framebufferHeight());
              this.renderer.bindPipeline(
                  style.seeThrough() ? this.pipelineHandle2 : this.pipelineHandle);
              this.renderer.bindVertexBuffer(this.bufferHandle, 0);
              this.updateState3(cosmeticData, style);
              this.renderer.draw(mesh.vertexCount(), 1, 0);
              return;
            }
          } catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.error("Cosmetic cape render failed", exception);
            return;
          } finally {
            if (byteValue != 0 && this.renderer != null) {
              try {
                this.renderer.restoreGLState();
              } catch (Exception currentException) {
              }
            }
          }
        }
      }
    }
  }

  public void clearStates() {
    this.entries.clear();
  }

  @Override
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
      this.count5 = 0;
      this.entries.clear();
      this.rhiOperationHandler = null;
      this.renderer = null;
    }
  }

  private void updateState() {
    if (this.rhiOperationHandler == null) {
      this.rhiOperationHandler = RhiDeviceService.device();
      this.renderer = (GlRenderer) this.rhiOperationHandler.encoder();
      this.shaderHandle =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("cosmetic_cape"));
      this.shaderHandle2 =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("cosmetic_cape"));
      RhiBlendStateService.VertexLayout currentVertexLayout =
          RhiBlendStateService.VertexLayout.of(
              40,
              new RhiBlendStateService.VertexAttribute(
                  0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
              new RhiBlendStateService.VertexAttribute(
                  1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L),
              new RhiBlendStateService.VertexAttribute(
                  2, 2, RhiBlendStateService.VertexFormat.FLOAT, 24L),
              new RhiBlendStateService.VertexAttribute(
                  3, 2, RhiBlendStateService.VertexFormat.FLOAT, 32L));
      RhiBlendStateService.DepthStencilState depthStencilState =
          new RhiBlendStateService.DepthStencilState(
              true, false, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false);
      this.pipelineHandle =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(this.shaderHandle)
                  .fragment(this.shaderHandle2)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.ALPHA)
                  .depthStencil(depthStencilState)
                  .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build());
      this.pipelineHandle2 =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(this.shaderHandle)
                  .fragment(this.shaderHandle2)
                  .vertexLayout(currentVertexLayout)
                  .blend(RhiBlendStateService.BlendState.ALPHA)
                  .depthStencil(RhiBlendStateService.DepthStencilState.DISABLED)
                  .rasterizer(RhiBlendStateService.RasterizerState.NO_CULL)
                  .topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES)
                  .build());
    }
  }

  private void updateState2(int value) {
    if (!this.bufferHandle.valid() || this.count5 < value) {
      if (this.bufferHandle.valid()) {
        this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
      }

      this.count5 = Math.max(value, this.count5 * 2);
      if (this.count5 <= 0) {
        this.count5 = 8192;
      }

      this.bufferHandle =
          this.rhiOperationHandler.createBuffer(
              this.count5 * 4L,
              RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX),
              RhiBlendStateService.BufferAccess.STREAM);
    }
  }

  private CosmeticRenderer.Mesh createMesh(
      Collection<? extends Player> items, CosmeticData cosmeticData, CosmeticRenderer.Style style) {
    int index = items.size() * 17 * 9 * 6 * 10;
    float[] floats = new float[index];
    int value = 0;
    HashSet currentSize = new HashSet(items.size());

    for (Player player : items) {
      if (this.checkCondition(player)) {
        UUID uUID = player.getUUID();
        currentSize.add(uUID);
        CosmeticRenderer.CapeState capeState =
            this.entries.computeIfAbsent(uUID, item -> new CosmeticRenderer.CapeState());
        CosmeticRenderer.CapeFrame capeFrame =
            capeState.update(
                player, cosmeticData.event().tickDelta(), cosmeticData.timeSeconds(), style);
        value = this.calculateValue(floats, value, capeState, capeFrame, style);
      }
    }

    this.entries.keySet().removeIf(item -> !currentSize.contains(item));
    return new CosmeticRenderer.Mesh(floats, value);
  }

  private boolean checkCondition(Player player) {
    return player != null && !player.isSpectator() && !player.isInvisible();
  }

  private int calculateValue(
      float[] floats,
      int value,
      CosmeticRenderer.CapeState capeState,
      CosmeticRenderer.CapeFrame capeFrame,
      CosmeticRenderer.Style style) {
    Vec3[] vec3s = new Vec3[180];
    float currentWidth = Mth.clamp(style.width(), 0.3F, 1.4F);
    float currentLength = Mth.clamp(style.length(), 0.35F, 2.2F);
    float currentValue = Mth.clamp(style.wind(), 0.0F, 2.0F);
    double doubleValue = capeFrame.phase();
    double currentDoubleValue = capeFrame.timeSeconds();

    for (int index = 0; index < 18; index++) {
      float nextValue = index / 17.0F;
      double nextDoubleValue = calculateValue4(0.2, 1.0, nextValue);
      double previousDoubleValue = nextDoubleValue * nextDoubleValue;
      double sourceDoubleValue = 1.0 - calculateValue4(0.0, 0.26, nextValue);
      double targetDoubleValue = currentWidth * (0.82 + 0.12 * nextDoubleValue);
      double inputDoubleValue =
          Math.sin(currentDoubleValue * (1.2 + currentValue * 0.45) + doubleValue + nextValue * 5.5)
              * 0.012
              * currentValue
              * previousDoubleValue;
      double outputDoubleValue =
          Math.sin(currentDoubleValue * 1.9 + doubleValue * 0.7 + nextValue * 8.0)
              * 0.007
              * currentValue
              * previousDoubleValue;

      for (int currentIndex = 0; currentIndex < 10; currentIndex++) {
        float previousValue = currentIndex / 9.0F;
        double resultDoubleValue = Math.abs(previousValue - 0.5) * 2.0;
        double candidateDoubleValue =
            0.07 * Math.pow(resultDoubleValue, 1.55) * (0.75 + 0.25 * sourceDoubleValue);
        double selectedDoubleValue = (previousValue - 0.5) * targetDoubleValue;
        double defaultDoubleValue = sourceDoubleValue * Math.pow(resultDoubleValue, 1.35);
        double initialDoubleValue = 0.155 + 0.115 * nextDoubleValue - 0.06 * defaultDoubleValue;
        double resolvedDoubleValue = Math.pow(resultDoubleValue, 1.9) * 0.026 * nextDoubleValue;
        double computedDoubleValue =
            -currentLength * nextValue - candidateDoubleValue * (1.0 - 0.45 * nextDoubleValue);
        Vec3 vec3 =
            capeFrame
                .anchor()
                .add(capeFrame.right().scale(selectedDoubleValue + inputDoubleValue))
                .add(capeFrame.back().scale(initialDoubleValue + resolvedDoubleValue))
                .add(capeFrame.motion().scale(previousDoubleValue))
                .add(0.0, computedDoubleValue + outputDoubleValue, 0.0);
        vec3s[index * 10 + currentIndex] = vec3;
      }
    }

    Vec3[] currentVec3s = capeState.simulate(vec3s, capeFrame.dt(), style.motion());

    for (int nextIndex = 0; nextIndex < 17; nextIndex++) {
      for (int previousIndex = 0; previousIndex < 9; previousIndex++) {
        float sourceValue = previousIndex / 9.0F;
        float targetValue = (previousIndex + 1) / 9.0F;
        float inputValue = nextIndex / 17.0F;
        float outputValue = (nextIndex + 1) / 17.0F;
        Vec3 currentVec3 = currentVec3s[nextIndex * 10 + previousIndex];
        Vec3 nextVec3 = currentVec3s[nextIndex * 10 + previousIndex + 1];
        Vec3 previousVec3 = currentVec3s[(nextIndex + 1) * 10 + previousIndex];
        Vec3 sourceVec3 = currentVec3s[(nextIndex + 1) * 10 + previousIndex + 1];
        value =
            this.calculateValue2(
                floats,
                value,
                currentVec3,
                previousVec3,
                sourceVec3,
                sourceValue,
                inputValue,
                sourceValue,
                outputValue,
                targetValue,
                outputValue);
        value =
            this.calculateValue2(
                floats,
                value,
                currentVec3,
                sourceVec3,
                nextVec3,
                sourceValue,
                inputValue,
                targetValue,
                outputValue,
                targetValue,
                inputValue);
      }
    }

    return value;
  }

  private int calculateValue2(
      float[] floats,
      int value,
      Vec3 vec3,
      Vec3 currentVec3,
      Vec3 nextVec3,
      float currentValue,
      float nextValue,
      float previousValue,
      float sourceValue,
      float targetValue,
      float inputValue) {
    Vec3 previousVec3 = currentVec3.subtract(vec3).cross(nextVec3.subtract(vec3));
    if (previousVec3.lengthSqr() < 1.0E-6) {
      previousVec3 = new Vec3(0.0, 0.0, 1.0);
    } else {
      previousVec3 = previousVec3.normalize();
    }

    value = this.calculateValue3(floats, value, vec3, previousVec3, currentValue, nextValue);
    value =
        this.calculateValue3(floats, value, currentVec3, previousVec3, previousValue, sourceValue);
    return this.calculateValue3(floats, value, nextVec3, previousVec3, targetValue, inputValue);
  }

  private int calculateValue3(
      float[] floats, int index, Vec3 vec3, Vec3 currentVec3, float value, float currentValue) {
    float nextValue = Math.min(1.0F, Math.abs(value - 0.5F) * 2.0F);
    float previousValue = 1.0F - currentValue;
    floats[index++] = (float) vec3.x;
    floats[index++] = (float) vec3.y;
    floats[index++] = (float) vec3.z;
    floats[index++] = (float) currentVec3.x;
    floats[index++] = (float) currentVec3.y;
    floats[index++] = (float) currentVec3.z;
    floats[index++] = value;
    floats[index++] = currentValue;
    floats[index++] = nextValue;
    floats[index++] = previousValue;
    return index;
  }

  private void updateState3(CosmeticData cosmeticData, CosmeticRenderer.Style style) {
    Vec3 vec3 = cosmeticData.event().cameraPos();
    this.renderer.pushVec3("uCameraPos", (float) vec3.x, (float) vec3.y, (float) vec3.z);
    this.renderer.pushFloat("uTime", (float) cosmeticData.timeSeconds());
    this.renderer.pushFloat("uOpacity", Mth.clamp(style.opacity(), 0.0F, 1.0F));
    this.renderer.pushFloat("uEnergy", Mth.clamp(style.wind(), 0.0F, 2.0F));
    this.renderer.pushInt("uMaterial", style.material());
    this.updateState4("uTopColor", style.topColor());
    this.updateState4("uBottomColor", style.bottomColor());
    this.updateState4("uEdgeColor", style.edgeColor());
    MemoryStack stack = MemoryStack.stackPush();

    try {
      FloatBuffer floatBuffer = stack.mallocFloat(16);
      FloatBuffer currentFloatBuffer = stack.mallocFloat(16);
      cosmeticData.event().viewMatrix().get(floatBuffer);
      cosmeticData.event().projectionMatrix().get(currentFloatBuffer);
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
  }

  private void updateState4(String text, int value) {
    float currentValue = (value >>> 24 & 0xFF) / 255.0F;
    float nextValue = (value >>> 16 & 0xFF) / 255.0F;
    float previousValue = (value >>> 8 & 0xFF) / 255.0F;
    float sourceValue = (value & 0xFF) / 255.0F;
    this.renderer.pushVec4(text, nextValue, previousValue, sourceValue, currentValue);
  }

  private static double calculateValue4(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    double previousDoubleValue =
        Mth.clamp((nextDoubleValue - doubleValue) / (currentDoubleValue - doubleValue), 0.0, 1.0);
    return previousDoubleValue * previousDoubleValue * (3.0 - 2.0 * previousDoubleValue);
  }

  private record CapeFrame(
      Vec3 anchor,
      Vec3 right,
      Vec3 back,
      Vec3 motion,
      double phase,
      double timeSeconds,
      double dt) {}

  private static final class CapeState {
    private static final Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
    private Vec3 vec32 = vec3;
    private Vec3[] vec33;
    private Vec3[] vec34;
    private double value = Double.NaN;
    private double value2 = Double.NaN;

    CosmeticRenderer.CapeFrame update(
        Player player, float currentValue, double doubleValue, CosmeticRenderer.Style style) {
      if (Double.isNaN(this.value2)) {
        this.value2 =
            (player.getUUID().getLeastSignificantBits() & 65535L)
                / 65535.0
                * 3.141592653589793
                * 2.0;
      }

      double currentDoubleValue =
          Double.isNaN(this.value)
              ? 0.016666666666666666
              : Mth.clamp(doubleValue - this.value, 0.0, 0.1);
      this.value = doubleValue;
      float nextValue = this.calculateValue5(player, currentValue);
      double nextDoubleValue = Math.toRadians(nextValue);
      Vec3 vec3 = new Vec3(-Math.sin(nextDoubleValue), 0.0, Math.cos(nextDoubleValue)).normalize();
      Vec3 currentVec3 = vec3.scale(-1.0);
      Vec3 nextVec3 =
          new Vec3(Math.cos(nextDoubleValue), 0.0, Math.sin(nextDoubleValue)).normalize();
      Vec3 previousVec3 = player.getDeltaMovement();
      Vec3 sourceVec3 = new Vec3(previousVec3.x, 0.0, previousVec3.z);
      double previousDoubleValue = sourceVec3.dot(vec3);
      double sourceDoubleValue = sourceVec3.dot(nextVec3);
      float previousValue = Mth.clamp(style.motion(), 0.0F, 2.0F);
      double targetDoubleValue =
          Mth.clamp(previousDoubleValue * 0.24 * previousValue, -0.045, 0.24);
      double inputDoubleValue = Mth.clamp(-sourceDoubleValue * 0.3 * previousValue, -0.2, 0.2);
      double outputDoubleValue = Mth.clamp(-previousVec3.y * 0.1 * previousValue, -0.1, 0.1);
      Vec3 targetVec3 =
          currentVec3
              .scale(targetDoubleValue)
              .add(nextVec3.scale(inputDoubleValue))
              .add(0.0, outputDoubleValue, 0.0);
      if (player.isSprinting()) {
        targetVec3 = targetVec3.add(currentVec3.scale(0.045 * previousValue));
      }

      double resultDoubleValue = 1.0 - Math.exp(-currentDoubleValue * 11.5);
      this.vec32 = this.vec32.add(targetVec3.subtract(this.vec32).scale(resultDoubleValue));
      Vec3 inputVec3 = player.getPosition(currentValue);
      double candidateDoubleValue = player.getBbHeight() * (player.isCrouching() ? 0.8 : 0.82);
      Vec3 outputVec3 = inputVec3.add(0.0, candidateDoubleValue, 0.0);
      return new CosmeticRenderer.CapeFrame(
          outputVec3,
          nextVec3,
          currentVec3,
          this.vec32,
          this.value2,
          doubleValue,
          currentDoubleValue);
    }

    Vec3[] simulate(Vec3[] vec3s, double doubleValue, float value) {
      if (this.vec33 != null && this.vec33.length == vec3s.length) {
        double currentDoubleValue =
            Mth.clamp(doubleValue, 0.008333333333333333, 0.03333333333333333);
        double nextDoubleValue = Mth.clamp(value, 0.0F, 2.0F);

        for (int index = 0; index < 18; index++) {
          double previousDoubleValue = index / 17.0;
          double sourceDoubleValue =
              CosmeticRenderer.calculateValue4(0.18, 1.0, previousDoubleValue);
          double targetDoubleValue =
              1.0 - CosmeticRenderer.calculateValue4(0.02, 0.24, previousDoubleValue);
          double inputDoubleValue = 34.0 + 34.0 * targetDoubleValue;
          double outputDoubleValue = 0.64 - 0.1 * Math.min(1.0, nextDoubleValue);
          double resultDoubleValue =
              0.015 + 0.145 * sourceDoubleValue * Math.max(0.45, nextDoubleValue);

          for (int currentIndex = 0; currentIndex < 10; currentIndex++) {
            int nextIndex = index * 10 + currentIndex;
            if (index <= 1) {
              this.vec33[nextIndex] = vec3s[nextIndex];
              this.vec34[nextIndex] = vec3;
            } else {
              Vec3 currentVec3 = vec3s[nextIndex].subtract(this.vec33[nextIndex]);
              double candidateDoubleValue = 1.0 - Math.exp(-currentDoubleValue * inputDoubleValue);
              Vec3 nextVec3 =
                  this.vec34[nextIndex]
                      .scale(outputDoubleValue)
                      .add(currentVec3.scale(candidateDoubleValue));
              Vec3 previousVec3 = this.vec33[nextIndex].add(nextVec3);
              Vec3 sourceVec3 = previousVec3.subtract(vec3s[nextIndex]);
              double selectedDoubleValue = resultDoubleValue * resultDoubleValue;
              if (sourceVec3.lengthSqr() > selectedDoubleValue) {
                sourceVec3 = sourceVec3.normalize().scale(resultDoubleValue);
                previousVec3 = vec3s[nextIndex].add(sourceVec3);
                nextVec3 = nextVec3.scale(0.35);
              }

              this.vec33[nextIndex] = previousVec3;
              this.vec34[nextIndex] = nextVec3;
            }
          }
        }

        for (int previousIndex = 0; previousIndex < 2; previousIndex++) {
          for (int sourceIndex = 0; sourceIndex < 10; sourceIndex++) {
            int targetIndex = previousIndex * 10 + sourceIndex;
            this.vec33[targetIndex] = vec3s[targetIndex];
            this.vec34[targetIndex] = vec3;
          }
        }

        return this.vec33;
      } else {
        this.vec33 = new Vec3[vec3s.length];
        this.vec34 = new Vec3[vec3s.length];

        for (int inputIndex = 0; inputIndex < vec3s.length; inputIndex++) {
          this.vec33[inputIndex] = vec3s[inputIndex];
          this.vec34[inputIndex] = vec3;
        }

        return this.vec33;
      }
    }

    private float calculateValue5(Player player, float value) {
      return this.calculateValue6(value, player.yBodyRotO, player.yBodyRot);
    }

    private float calculateValue6(float value, float currentValue, float nextValue) {
      return currentValue + value * Mth.wrapDegrees(nextValue - currentValue);
    }
  }

  private record Mesh(float[] vertices, int floatCount) {
    int vertexCount() {
      return this.floatCount / 10;
    }
  }

  public record Style(
      float width,
      float length,
      float motion,
      float wind,
      float opacity,
      int topColor,
      int bottomColor,
      int edgeColor,
      int material,
      boolean seeThrough) {}
}
