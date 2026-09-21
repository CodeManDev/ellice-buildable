package dev.felix.ellice.render.rhi;

import java.nio.ByteBuffer;

public final class RhiBlendStateService {
  private RhiBlendStateService() {}

  public enum AddressMode {
    REPEAT,
    CLAMP,
    MIRROR;

    private static RhiBlendStateService.AddressMode[] $values() {
      return new RhiBlendStateService.AddressMode[] {REPEAT, CLAMP, MIRROR};
    }
  }

  public enum BlendFactor {
    ZERO,
    ONE,
    SRC_COLOR,
    ONE_MINUS_SRC_COLOR,
    DST_COLOR,
    ONE_MINUS_DST_COLOR,
    SRC_ALPHA,
    ONE_MINUS_SRC_ALPHA,
    DST_ALPHA,
    ONE_MINUS_DST_ALPHA;

    private static RhiBlendStateService.BlendFactor[] $values() {
      return new RhiBlendStateService.BlendFactor[] {
        ZERO,
        ONE,
        SRC_COLOR,
        ONE_MINUS_SRC_COLOR,
        DST_COLOR,
        ONE_MINUS_DST_COLOR,
        SRC_ALPHA,
        ONE_MINUS_SRC_ALPHA,
        DST_ALPHA,
        ONE_MINUS_DST_ALPHA
      };
    }
  }

  public enum BlendOp {
    ADD,
    SUBTRACT,
    REVERSE_SUBTRACT,
    MIN,
    MAX;

    private static RhiBlendStateService.BlendOp[] $values() {
      return new RhiBlendStateService.BlendOp[] {ADD, SUBTRACT, REVERSE_SUBTRACT, MIN, MAX};
    }
  }

  public record BlendState(
      boolean enabled,
      RhiBlendStateService.BlendFactor srcColor,
      RhiBlendStateService.BlendFactor dstColor,
      RhiBlendStateService.BlendOp colorOp,
      RhiBlendStateService.BlendFactor srcAlpha,
      RhiBlendStateService.BlendFactor dstAlpha,
      RhiBlendStateService.BlendOp alphaOp) {
    public static final RhiBlendStateService.BlendState DISABLED =
        new RhiBlendStateService.BlendState(
            false,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ZERO,
            RhiBlendStateService.BlendOp.ADD,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ZERO,
            RhiBlendStateService.BlendOp.ADD);
    public static final RhiBlendStateService.BlendState ALPHA =
        new RhiBlendStateService.BlendState(
            true,
            RhiBlendStateService.BlendFactor.SRC_ALPHA,
            RhiBlendStateService.BlendFactor.ONE_MINUS_SRC_ALPHA,
            RhiBlendStateService.BlendOp.ADD,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ONE_MINUS_SRC_ALPHA,
            RhiBlendStateService.BlendOp.ADD);
    public static final RhiBlendStateService.BlendState PREMULTIPLIED =
        new RhiBlendStateService.BlendState(
            true,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ONE_MINUS_SRC_ALPHA,
            RhiBlendStateService.BlendOp.ADD,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ONE_MINUS_SRC_ALPHA,
            RhiBlendStateService.BlendOp.ADD);
    public static final RhiBlendStateService.BlendState ADDITIVE =
        new RhiBlendStateService.BlendState(
            true,
            RhiBlendStateService.BlendFactor.SRC_ALPHA,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendOp.ADD,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendOp.ADD);
  }

  public enum BufferAccess {
    STATIC,
    DYNAMIC,
    STREAM;

    private static RhiBlendStateService.BufferAccess[] $values() {
      return new RhiBlendStateService.BufferAccess[] {STATIC, DYNAMIC, STREAM};
    }
  }

  public record BufferHandle(int id) {
    public static final RhiBlendStateService.BufferHandle NONE =
        new RhiBlendStateService.BufferHandle(0);

    public boolean valid() {
      return this.id != 0;
    }
  }

  public enum BufferUsage {
    VERTEX(1),
    INDEX(2),
    UNIFORM(4),
    STORAGE(8);

    public final int bit;

    BufferUsage(int value) {
      this.bit = value;
    }

    public static int flags(RhiBlendStateService.BufferUsage... bufferUsages) {
      int value = 0;

      for (RhiBlendStateService.BufferUsage bufferUsage : bufferUsages) {
        value |= bufferUsage.bit;
      }

      return value;
    }

    private static RhiBlendStateService.BufferUsage[] $values() {
      return new RhiBlendStateService.BufferUsage[] {VERTEX, INDEX, UNIFORM, STORAGE};
    }
  }

  public enum CompareOp {
    NEVER,
    LESS,
    EQUAL,
    LESS_OR_EQUAL,
    GREATER,
    NOT_EQUAL,
    GREATER_OR_EQUAL,
    ALWAYS;

    private static RhiBlendStateService.CompareOp[] $values() {
      return new RhiBlendStateService.CompareOp[] {
        NEVER, LESS, EQUAL, LESS_OR_EQUAL, GREATER, NOT_EQUAL, GREATER_OR_EQUAL, ALWAYS
      };
    }
  }

  public enum CullMode {
    NONE,
    FRONT,
    BACK;

    private static RhiBlendStateService.CullMode[] $values() {
      return new RhiBlendStateService.CullMode[] {NONE, FRONT, BACK};
    }
  }

  public record DepthCopyTarget(
      RhiBlendStateService.TextureHandle texture,
      RhiBlendStateService.FramebufferHandle framebuffer) {
    public static final RhiBlendStateService.DepthCopyTarget NONE =
        new RhiBlendStateService.DepthCopyTarget(
            RhiBlendStateService.TextureHandle.NONE, RhiBlendStateService.FramebufferHandle.NONE);

    public boolean valid() {
      return this.texture.valid() && this.framebuffer.valid();
    }
  }

  public record DepthStencilState(
      boolean depthTest,
      boolean depthWrite,
      RhiBlendStateService.CompareOp depthCompare,
      boolean stencilTest) {
    public static final RhiBlendStateService.DepthStencilState DISABLED =
        new RhiBlendStateService.DepthStencilState(
            false, false, RhiBlendStateService.CompareOp.ALWAYS, false);
    public static final RhiBlendStateService.DepthStencilState READ_WRITE =
        new RhiBlendStateService.DepthStencilState(
            true, true, RhiBlendStateService.CompareOp.LESS, false);
    public static final RhiBlendStateService.DepthStencilState READ_ONLY =
        new RhiBlendStateService.DepthStencilState(
            true, false, RhiBlendStateService.CompareOp.LESS, false);
  }

  public record FenceToken(long value) {
    public static final RhiBlendStateService.FenceToken NONE =
        new RhiBlendStateService.FenceToken(0L);

    public boolean valid() {
      return this.value != 0L;
    }
  }

  public enum FilterMode {
    NEAREST,
    LINEAR,
    NEAREST_MIPMAP_LINEAR,
    LINEAR_MIPMAP_LINEAR;

    private static RhiBlendStateService.FilterMode[] $values() {
      return new RhiBlendStateService.FilterMode[] {
        NEAREST, LINEAR, NEAREST_MIPMAP_LINEAR, LINEAR_MIPMAP_LINEAR
      };
    }
  }

  public enum FramebufferAspect {
    COLOR(1),
    DEPTH(2),
    STENCIL(4);

    public final int bit;

    FramebufferAspect(int value) {
      this.bit = value;
    }

    public static int flags(RhiBlendStateService.FramebufferAspect... framebufferAspects) {
      int value = 0;

      for (RhiBlendStateService.FramebufferAspect framebufferAspect : framebufferAspects) {
        value |= framebufferAspect.bit;
      }

      return value;
    }

    private static RhiBlendStateService.FramebufferAspect[] $values() {
      return new RhiBlendStateService.FramebufferAspect[] {COLOR, DEPTH, STENCIL};
    }
  }

  public record FramebufferHandle(int id) {
    public static final RhiBlendStateService.FramebufferHandle NONE =
        new RhiBlendStateService.FramebufferHandle(-1);
    public static final RhiBlendStateService.FramebufferHandle DEFAULT =
        new RhiBlendStateService.FramebufferHandle(0);

    public boolean valid() {
      return this.id >= 0;
    }
  }

  public enum FrontFace {
    CCW,
    CW;

    private static RhiBlendStateService.FrontFace[] $values() {
      return new RhiBlendStateService.FrontFace[] {CCW, CW};
    }
  }

  public enum IndexType {
    UINT16,
    UINT32;

    private static RhiBlendStateService.IndexType[] $values() {
      return new RhiBlendStateService.IndexType[] {UINT16, UINT32};
    }
  }

  public record PipelineHandle(int id) {
    public static final RhiBlendStateService.PipelineHandle NONE =
        new RhiBlendStateService.PipelineHandle(-1);

    public boolean valid() {
      return this.id >= 0;
    }
  }

  public enum PrimitiveTopology {
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN,
    LINES,
    LINE_STRIP,
    POINTS;

    private static RhiBlendStateService.PrimitiveTopology[] $values() {
      return new RhiBlendStateService.PrimitiveTopology[] {
        TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN, LINES, LINE_STRIP, POINTS
      };
    }
  }

  public record RasterizerState(
      RhiBlendStateService.CullMode cullMode,
      RhiBlendStateService.FrontFace frontFace,
      boolean wireframe) {
    public static final RhiBlendStateService.RasterizerState DEFAULT =
        new RhiBlendStateService.RasterizerState(
            RhiBlendStateService.CullMode.BACK, RhiBlendStateService.FrontFace.CCW, false);
    public static final RhiBlendStateService.RasterizerState NO_CULL =
        new RhiBlendStateService.RasterizerState(
            RhiBlendStateService.CullMode.NONE, RhiBlendStateService.FrontFace.CCW, false);
  }

  public record SamplerDescriptor(
      RhiBlendStateService.FilterMode minFilter,
      RhiBlendStateService.FilterMode magFilter,
      RhiBlendStateService.AddressMode addressMode) {
    public static final RhiBlendStateService.SamplerDescriptor LINEAR_CLAMP =
        new RhiBlendStateService.SamplerDescriptor(
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.FilterMode.LINEAR,
            RhiBlendStateService.AddressMode.CLAMP);
    public static final RhiBlendStateService.SamplerDescriptor NEAREST_CLAMP =
        new RhiBlendStateService.SamplerDescriptor(
            RhiBlendStateService.FilterMode.NEAREST,
            RhiBlendStateService.FilterMode.NEAREST,
            RhiBlendStateService.AddressMode.CLAMP);

    public SamplerDescriptor(
        RhiBlendStateService.FilterMode minFilter,
        RhiBlendStateService.FilterMode magFilter,
        RhiBlendStateService.AddressMode addressMode) {
      if (magFilter != RhiBlendStateService.FilterMode.NEAREST
          && magFilter != RhiBlendStateService.FilterMode.LINEAR) {
        throw new IllegalArgumentException("Magnification cannot use mipmap filtering");
      }

      this.minFilter = minFilter;
      this.magFilter = magFilter;
      this.addressMode = addressMode;
    }
  }

  public record SamplerHandle(int id) {
    public static final RhiBlendStateService.SamplerHandle NONE =
        new RhiBlendStateService.SamplerHandle(0);

    public boolean valid() {
      return this.id != 0;
    }
  }

  public record ShaderHandle(int id) {
    public static final RhiBlendStateService.ShaderHandle NONE =
        new RhiBlendStateService.ShaderHandle(0);

    public boolean valid() {
      return this.id != 0;
    }
  }

  public enum ShaderStage {
    VERTEX,
    FRAGMENT,
    COMPUTE;

    private static RhiBlendStateService.ShaderStage[] $values() {
      return new RhiBlendStateService.ShaderStage[] {VERTEX, FRAGMENT, COMPUTE};
    }
  }

  public record TextureDescriptor(
      int width,
      int height,
      RhiBlendStateService.TextureFormat format,
      RhiBlendStateService.FilterMode minFilter,
      RhiBlendStateService.FilterMode magFilter,
      RhiBlendStateService.AddressMode addressMode) {
    public static RhiBlendStateService.TextureDescriptor renderTarget(
        int x, int y, RhiBlendStateService.TextureFormat textureFormat) {
      return new RhiBlendStateService.TextureDescriptor(
          x,
          y,
          textureFormat,
          RhiBlendStateService.FilterMode.LINEAR,
          RhiBlendStateService.FilterMode.LINEAR,
          RhiBlendStateService.AddressMode.CLAMP);
    }

    public static RhiBlendStateService.TextureDescriptor sampled(
        int value, int currentValue, RhiBlendStateService.TextureFormat textureFormat) {
      return new RhiBlendStateService.TextureDescriptor(
          value,
          currentValue,
          textureFormat,
          RhiBlendStateService.FilterMode.LINEAR,
          RhiBlendStateService.FilterMode.LINEAR,
          RhiBlendStateService.AddressMode.REPEAT);
    }
  }

  public record TextureDownload(ByteBuffer pixels, int width, int height) {
    public boolean complete() {
      return this.pixels != null && this.width > 0 && this.height > 0;
    }
  }

  public enum TextureFormat {
    R8,
    RG8,
    RGBA8,
    RGBA16F,
    R11G11B10F,
    R16F,
    RG16F,
    DEPTH24,
    DEPTH24_STENCIL8,
    DEPTH32F;

    private static RhiBlendStateService.TextureFormat[] $values() {
      return new RhiBlendStateService.TextureFormat[] {
        R8, RG8, RGBA8, RGBA16F, R11G11B10F, R16F, RG16F, DEPTH24, DEPTH24_STENCIL8, DEPTH32F
      };
    }
  }

  public record TextureHandle(int id) {
    public static final RhiBlendStateService.TextureHandle NONE =
        new RhiBlendStateService.TextureHandle(0);

    public boolean valid() {
      return this.id != 0;
    }
  }

  public record VertexAttribute(
      int location, int components, RhiBlendStateService.VertexFormat format, long offset) {}

  public enum VertexFormat {
    FLOAT(4),
    INT(4),
    BYTE(1),
    UBYTE(1),
    SHORT(2),
    USHORT(2);

    public final int byteSize;

    VertexFormat(int value) {
      this.byteSize = value;
    }

    private static RhiBlendStateService.VertexFormat[] $values() {
      return new RhiBlendStateService.VertexFormat[] {FLOAT, INT, BYTE, UBYTE, SHORT, USHORT};
    }
  }

  public record VertexLayout(int stride, RhiBlendStateService.VertexAttribute[] attributes) {
    public static RhiBlendStateService.VertexLayout of(
        int value, RhiBlendStateService.VertexAttribute... vertexAttributes) {
      return new RhiBlendStateService.VertexLayout(value, vertexAttributes);
    }
  }
}
