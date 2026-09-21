package dev.felix.ellice.render.render3d.shader;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.util.Objects;

public final class ShaderRecipeService implements AutoCloseable {
  private final RhiOperationHandler rhiOperationHandler;
  private final ShaderOverrideRootService shaderOverrideRootService;
  private final ShaderData shaderData;
  private RhiBlendStateService.PipelineHandle pipelineHandle =
      RhiBlendStateService.PipelineHandle.NONE;
  private long timestamp = Long.MIN_VALUE;
  private String text;
  private boolean enabled;

  public ShaderRecipeService(
      RhiOperationHandler rhiOperation,
      ShaderOverrideRootService shaderOverrideRoot,
      ShaderData currentShaderData) {
    this.rhiOperationHandler = Objects.requireNonNull(rhiOperation, "device");
    this.shaderOverrideRootService = Objects.requireNonNull(shaderOverrideRoot, "sources");
    this.shaderData = Objects.requireNonNull(currentShaderData, "recipe");
  }

  public ShaderData recipe() {
    return this.shaderData;
  }

  public RhiBlendStateService.PipelineHandle resolve() {
    if (this.enabled) {
      return RhiBlendStateService.PipelineHandle.NONE;
    }

    this.shaderOverrideRootService.pollChanges();
    long longValue = this.shaderOverrideRootService.revision();
    if (this.timestamp == longValue) {
      return this.pipelineHandle;
    }

    this.timestamp = longValue;
    RhiBlendStateService.ShaderHandle shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
    RhiBlendStateService.ShaderHandle currentShaderHandle = RhiBlendStateService.ShaderHandle.NONE;
    RhiBlendStateService.PipelineHandle currentPipelineHandle =
        RhiBlendStateService.PipelineHandle.NONE;

    try {
      ShaderDefinition shaderDefinition = this.shaderData.shader();
      String currentText =
          this.shaderOverrideRootService.resolve(shaderDefinition.vertexAsset()).source();
      String nextText =
          this.shaderOverrideRootService.resolve(shaderDefinition.fragmentAsset()).source();
      shaderHandle =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.VERTEX, currentText);
      currentShaderHandle =
          this.rhiOperationHandler.createShader(
              RhiBlendStateService.ShaderStage.FRAGMENT, nextText);
      currentPipelineHandle =
          this.rhiOperationHandler.createPipeline(
              RhiBuilderData.graphics()
                  .vertex(shaderHandle)
                  .fragment(currentShaderHandle)
                  .vertexLayout(this.shaderData.vertexLayout())
                  .blend(this.shaderData.blend())
                  .depthStencil(this.shaderData.depthStencil())
                  .rasterizer(this.shaderData.rasterizer())
                  .topology(this.shaderData.topology())
                  .build());
      RhiBlendStateService.PipelineHandle nextPipelineHandle = this.pipelineHandle;
      this.pipelineHandle = currentPipelineHandle;
      currentPipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
      this.text = null;
      if (nextPipelineHandle.valid()) {
        this.rhiOperationHandler.destroyPipeline(nextPipelineHandle);
      }

      CoreIsInitializedHandler.LOGGER.info(
          "Shader pipeline '{}' active at revision {}", shaderDefinition.id(), longValue);
    } catch (RuntimeException exception) {
      this.text =
          exception.getMessage() == null
              ? exception.getClass().getSimpleName()
              : exception.getMessage();
      CoreIsInitializedHandler.LOGGER.error(
          "Shader reload failed for '{}'; keeping last good pipeline: {}",
          this.shaderData.shader().id(),
          this.text);
    } finally {
      if (currentPipelineHandle.valid()) {
        this.rhiOperationHandler.destroyPipeline(currentPipelineHandle);
      }

      if (shaderHandle.valid()) {
        this.rhiOperationHandler.destroyShader(shaderHandle);
      }

      if (currentShaderHandle.valid()) {
        this.rhiOperationHandler.destroyShader(currentShaderHandle);
      }
    }

    return this.pipelineHandle;
  }

  public boolean valid() {
    return this.pipelineHandle.valid();
  }

  public String lastError() {
    return this.text;
  }

  @Override
  public void close() {
    if (!this.enabled) {
      this.enabled = true;
      if (this.pipelineHandle.valid()) {
        this.rhiOperationHandler.destroyPipeline(this.pipelineHandle);
      }

      this.pipelineHandle = RhiBlendStateService.PipelineHandle.NONE;
    }
  }
}
