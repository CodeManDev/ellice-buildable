package dev.felix.ellice.render.rhi;

public record RhiBuilderData(
   RhiBlendStateService.ShaderHandle vertexShader,
   RhiBlendStateService.ShaderHandle fragmentShader,
   RhiBlendStateService.ShaderHandle computeShader,
   RhiBlendStateService.VertexLayout vertexLayout,
   RhiBlendStateService.BlendState blendState,
   RhiBlendStateService.DepthStencilState depthStencilState,
   RhiBlendStateService.RasterizerState rasterizerState,
   RhiBlendStateService.PrimitiveTopology topology
) {
   public static RhiBuilderData.Builder graphics() {
      return new RhiBuilderData.Builder();
   }

   public static RhiBuilderData.Builder compute(RhiBlendStateService.ShaderHandle shaderHandle) {
      return new RhiBuilderData.Builder().compute(shaderHandle);
   }

   public static class Builder {
      private RhiBlendStateService.ShaderHandle shaderHandle = RhiBlendStateService.ShaderHandle.NONE;
      private RhiBlendStateService.ShaderHandle shaderHandle2 = RhiBlendStateService.ShaderHandle.NONE;
      private RhiBlendStateService.ShaderHandle shaderHandle3 = RhiBlendStateService.ShaderHandle.NONE;
      private RhiBlendStateService.VertexLayout vertexLayout2;
      private RhiBlendStateService.BlendState blendState2 = RhiBlendStateService.BlendState.DISABLED;
      private RhiBlendStateService.DepthStencilState depthStencilState2 = RhiBlendStateService.DepthStencilState.DISABLED;
      private RhiBlendStateService.RasterizerState rasterizerState2 = RhiBlendStateService.RasterizerState.NO_CULL;
      private RhiBlendStateService.PrimitiveTopology primitiveTopology = RhiBlendStateService.PrimitiveTopology.TRIANGLES;

      public RhiBuilderData.Builder vertex(RhiBlendStateService.ShaderHandle currentShaderHandle) {
         this.shaderHandle = currentShaderHandle;
         return this;
      }

      public RhiBuilderData.Builder fragment(RhiBlendStateService.ShaderHandle shaderHandle) {
         this.shaderHandle2 = shaderHandle;
         return this;
      }

      public RhiBuilderData.Builder compute(RhiBlendStateService.ShaderHandle shaderHandle) {
         this.shaderHandle3 = shaderHandle;
         return this;
      }

      public RhiBuilderData.Builder vertexLayout(RhiBlendStateService.VertexLayout currentVertexLayout) {
         this.vertexLayout2 = currentVertexLayout;
         return this;
      }

      public RhiBuilderData.Builder blend(RhiBlendStateService.BlendState blendState) {
         this.blendState2 = blendState;
         return this;
      }

      public RhiBuilderData.Builder depthStencil(RhiBlendStateService.DepthStencilState depthStencilState) {
         this.depthStencilState2 = depthStencilState;
         return this;
      }

      public RhiBuilderData.Builder rasterizer(RhiBlendStateService.RasterizerState rasterizerState) {
         this.rasterizerState2 = rasterizerState;
         return this;
      }

      public RhiBuilderData.Builder topology(RhiBlendStateService.PrimitiveTopology currentPrimitiveTopology) {
         this.primitiveTopology = currentPrimitiveTopology;
         return this;
      }

      public RhiBuilderData build() {
         return new RhiBuilderData(
            this.shaderHandle,
            this.shaderHandle2,
            this.shaderHandle3,
            this.vertexLayout2,
            this.blendState2,
            this.depthStencilState2,
            this.rasterizerState2,
            this.primitiveTopology
         );
      }
   }
}
