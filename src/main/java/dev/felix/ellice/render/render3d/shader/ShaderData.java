package dev.felix.ellice.render.render3d.shader;

import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.util.Objects;

public record ShaderData(
   ShaderDefinition shader,
   RhiBlendStateService.VertexLayout vertexLayout,
   RhiBlendStateService.BlendState blend,
   RhiBlendStateService.DepthStencilState depthStencil,
   RhiBlendStateService.RasterizerState rasterizer,
   RhiBlendStateService.PrimitiveTopology topology
) {
   public ShaderData(
      ShaderDefinition shader,
      RhiBlendStateService.VertexLayout vertexLayout,
      RhiBlendStateService.BlendState blend,
      RhiBlendStateService.DepthStencilState depthStencil,
      RhiBlendStateService.RasterizerState rasterizer,
      RhiBlendStateService.PrimitiveTopology topology
   ) {
      Objects.requireNonNull(shader, "shader");
      Objects.requireNonNull(vertexLayout, "vertexLayout");
      Objects.requireNonNull(blend, "blend");
      Objects.requireNonNull(depthStencil, "depthStencil");
      Objects.requireNonNull(rasterizer, "rasterizer");
      Objects.requireNonNull(topology, "topology");
      this.shader = shader;
      this.vertexLayout = vertexLayout;
      this.blend = blend;
      this.depthStencil = depthStencil;
      this.rasterizer = rasterizer;
      this.topology = topology;
   }
}
