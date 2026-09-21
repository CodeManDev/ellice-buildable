package dev.felix.ellice.render.render3d.material;

import dev.felix.ellice.render.render3d.shader.ShaderSetColorService;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class MaterialErFactory {
   private final String text;
   private final ShaderDefinition shaderData2;
   private final MaterialErFactory.Queue queue2;
   private final RhiBlendStateService.BlendState blendState;
   private final RhiBlendStateService.DepthStencilState depthStencilState;
   private final RhiBlendStateService.RasterizerState rasterizerState;
   private final ShaderSetColorService values2 = new ShaderSetColorService();
   private volatile MaterialErFactory.Glow glow2 = MaterialErFactory.Glow.NONE;

   private MaterialErFactory(MaterialErFactory.Builder builder) {
      this.text = builder.text2;
      this.shaderData2 = builder.shaderData22;
      this.queue2 = builder.queue3;
      this.blendState = builder.blendState2 != null ? builder.blendState2 : createBlendState(builder.queue3);
      this.depthStencilState = builder.depthStencilState2 != null ? builder.depthStencilState2 : createDepthStencilState(builder.queue3);
      this.rasterizerState = builder.rasterizerState2;
   }

   public static MaterialErFactory.Builder builder(String text, ShaderDefinition shaderDefinition) {
      return new MaterialErFactory.Builder(text, shaderDefinition);
   }

   public String name() {
      return this.text;
   }

   public ShaderDefinition shader() {
      return this.shaderData2;
   }

   public MaterialErFactory.Queue queue() {
      return this.queue2;
   }

   public RhiBlendStateService.BlendState blend() {
      return this.blendState;
   }

   public RhiBlendStateService.DepthStencilState depthStencil() {
      return this.depthStencilState;
   }

   public RhiBlendStateService.RasterizerState rasterizer() {
      return this.rasterizerState;
   }

   public ShaderSetColorService bindings() {
      return this.values2;
   }

   public MaterialErFactory.Glow glow() {
      return this.glow2;
   }

   public MaterialErFactory uniform(String text, int value) {
      this.values2.set(text, value);
      return this;
   }

   public MaterialErFactory uniform(String text, float value) {
      this.values2.set(text, value);
      return this;
   }

   public MaterialErFactory uniform(String text, float value, float currentValue) {
      this.values2.set(text, value, currentValue);
      return this;
   }

   public MaterialErFactory uniform(String text, float value, float currentValue, float nextValue) {
      this.values2.set(text, value, currentValue, nextValue);
      return this;
   }

   public MaterialErFactory uniform(String text, float value, float currentValue, float nextValue, float previousValue) {
      this.values2.set(text, value, currentValue, nextValue, previousValue);
      return this;
   }

   public MaterialErFactory uniform(String text, Vector3f vector3f) {
      this.values2.set(text, vector3f);
      return this;
   }

   public MaterialErFactory uniform(String text, Matrix4f matrix4f) {
      this.values2.set(text, matrix4f);
      return this;
   }

   public MaterialErFactory color(String text, int currentColor) {
      this.values2.setColor(text, currentColor);
      return this;
   }

   public MaterialErFactory texture(String text, RhiBlendStateService.TextureHandle textureHandle, int textureId) {
      this.values2.texture(text, textureHandle, textureId);
      return this;
   }

   public MaterialErFactory texture(String text, RhiBlendStateService.TextureHandle textureHandle, RhiBlendStateService.SamplerHandle samplerHandle, int textureId) {
      this.values2.texture(text, textureHandle, samplerHandle, textureId);
      return this;
   }

   public MaterialErFactory glow(int value, float currentValue, float nextValue) {
      this.glow2 = new MaterialErFactory.Glow(value, currentValue, nextValue);
      return this;
   }

   public MaterialErFactory clearGlow() {
      this.glow2 = MaterialErFactory.Glow.NONE;
      return this;
   }

   public void bind(RhiCommandBuffer rhiCommandBuffer) {
      this.values2.apply(rhiCommandBuffer);
   }

   public MaterialErFactory.PipelineKey pipelineKey() {
      return new MaterialErFactory.PipelineKey(this.shaderData2, this.blendState, this.depthStencilState, this.rasterizerState);
   }

   private static RhiBlendStateService.BlendState createBlendState(MaterialErFactory.Queue queue) {
      return switch (queue) {
         case OPAQUE, CUTOUT -> RhiBlendStateService.BlendState.DISABLED;
         case TRANSPARENT -> RhiBlendStateService.BlendState.ALPHA;
         case ADDITIVE -> RhiBlendStateService.BlendState.ADDITIVE;
      };
   }

   private static RhiBlendStateService.DepthStencilState createDepthStencilState(MaterialErFactory.Queue queue) {
      return switch (queue) {
         case OPAQUE, CUTOUT -> RhiBlendStateService.DepthStencilState.READ_WRITE;
         case TRANSPARENT, ADDITIVE -> RhiBlendStateService.DepthStencilState.READ_ONLY;
      };
   }

   public static final class Builder {
      private final String text2;
      private final ShaderDefinition shaderData22;
      private MaterialErFactory.Queue queue3 = MaterialErFactory.Queue.OPAQUE;
      private RhiBlendStateService.BlendState blendState2;
      private RhiBlendStateService.DepthStencilState depthStencilState2;
      private RhiBlendStateService.RasterizerState rasterizerState2 = RhiBlendStateService.RasterizerState.DEFAULT;

      private Builder(String text, ShaderDefinition shaderDefinition) {
         if (text != null && !text.isBlank()) {
            this.text2 = text;
            this.shaderData22 = Objects.requireNonNull(shaderDefinition, "shader");
         } else {
            throw new IllegalArgumentException("Material name is required");
         }
      }

      public MaterialErFactory.Builder queue(MaterialErFactory.Queue currentQueue) {
         this.queue3 = Objects.requireNonNull(currentQueue, "queue");
         return this;
      }

      public MaterialErFactory.Builder blend(RhiBlendStateService.BlendState blendState) {
         this.blendState2 = Objects.requireNonNull(blendState, "blend");
         return this;
      }

      public MaterialErFactory.Builder depthStencil(RhiBlendStateService.DepthStencilState depthStencilState) {
         this.depthStencilState2 = Objects.requireNonNull(depthStencilState, "depthStencil");
         return this;
      }

      public MaterialErFactory.Builder rasterizer(RhiBlendStateService.RasterizerState rasterizerState) {
         this.rasterizerState2 = Objects.requireNonNull(rasterizerState, "rasterizer");
         return this;
      }

      public MaterialErFactory.Builder doubleSided() {
         this.rasterizerState2 = RhiBlendStateService.RasterizerState.NO_CULL;
         return this;
      }

      public MaterialErFactory build() {
         return new MaterialErFactory(this);
      }
   }

   public record Glow(int argb, float intensity, float radiusPixels) {
      public static final MaterialErFactory.Glow NONE = new MaterialErFactory.Glow(0, 0.0F, 0.0F);

      public Glow(int argb, float intensity, float radiusPixels) {
         if (!Float.isFinite(intensity) || intensity < 0.0F) {
            throw new IllegalArgumentException("Glow intensity must be finite and non-negative");
         }

         if (Float.isFinite(radiusPixels) && !(radiusPixels < 0.0F)) {
            this.argb = argb;
            this.intensity = intensity;
            this.radiusPixels = radiusPixels;
         } else {
            throw new IllegalArgumentException("Glow radius must be finite and non-negative");
         }
      }

      public boolean enabled() {
         return this.intensity > 0.0F && this.radiusPixels > 0.0F && (this.argb >>> 24 & 0xFF) != 0 && (this.argb & 16777215) != 0;
      }

      public float red() {
         return (this.argb >>> 16 & 0xFF) / 255.0F;
      }

      public float green() {
         return (this.argb >>> 8 & 0xFF) / 255.0F;
      }

      public float blue() {
         return (this.argb & 0xFF) / 255.0F;
      }

      public float alpha() {
         return (this.argb >>> 24 & 0xFF) / 255.0F;
      }
   }

   public record PipelineKey(
      ShaderDefinition shader,
      RhiBlendStateService.BlendState blend,
      RhiBlendStateService.DepthStencilState depthStencil,
      RhiBlendStateService.RasterizerState rasterizer
   ) {
   }

   public enum Queue {
      OPAQUE,
      CUTOUT,
      TRANSPARENT,
      ADDITIVE;


      private static MaterialErFactory.Queue[] $values() {
         return new MaterialErFactory.Queue[]{OPAQUE, CUTOUT, TRANSPARENT, ADDITIVE};
      }
   }
}
