package dev.felix.ellice.render.rhi.gl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

public class GlShaderFactory implements RhiOperationHandler {
  private final Map<Integer, PipelineState> pipelines;
  private int nextPipelineId;
  private final Map<Integer, Integer> textureInternalFormats;
  private final Map<Integer, Integer> framebufferColorAttachmentCounts;
  private final GlRenderer fhlhugm5lep;
  private long lastErrorLogTimeNanos;
  private static final int f5wsvrenqt37 = 34046;
  private static final int f66tqt6xi6su = 34047;
  private static final int f1gn8qtpmkdh = 34049;
  private static final float fdsnw0991as1 = -0.25f;
  private static final float faoj597q6vg4 = 8.0f;
  private static volatile float f7gqy4aroaro;

  public GlShaderFactory() {
    this.pipelines = new HashMap<Integer, PipelineState>();
    this.textureInternalFormats = new HashMap<Integer, Integer>();
    this.framebufferColorAttachmentCounts = new HashMap<Integer, Integer>();
    this.fhlhugm5lep = new GlRenderer(this);
  }

  private static PixelUnpackState m288gwllrocx() {
    final PixelUnpackState pixelUnpackState =
        new PixelUnpackState(
            GL11.glGetInteger(35055),
            GL11.glGetInteger(3317),
            GL11.glGetInteger(3314),
            GL11.glGetInteger(3315),
            GL11.glGetInteger(3316));
    GL15.glBindBuffer(35052, 0);
    GL11.glPixelStorei(3314, 0);
    GL11.glPixelStorei(3315, 0);
    GL11.glPixelStorei(3316, 0);
    GL11.glPixelStorei(3317, 1);
    return pixelUnpackState;
  }

  private static void micx73bomiw7(final PixelUnpackState pixelUnpackState) {
    GL11.glPixelStorei(3317, pixelUnpackState.alignment());
    GL11.glPixelStorei(3314, pixelUnpackState.rowLength());
    GL11.glPixelStorei(3315, pixelUnpackState.skipRows());
    GL11.glPixelStorei(3316, pixelUnpackState.skipPixels());
    GL15.glBindBuffer(35052, pixelUnpackState.buffer());
  }

  @Override
  public RhiBlendStateService.ShaderHandle createShader(
      final RhiBlendStateService.ShaderStage obj, final String s) {
    final int glCreateShader =
        GL20.glCreateShader(
            switch (obj) {
              default -> throw new MatchException(null, null);
              case VERTEX -> 35633;
              case FRAGMENT -> 35632;
              case COMPUTE -> 37305;
            });
    GL20.glShaderSource(glCreateShader, (CharSequence) s);
    GL20.glCompileShader(glCreateShader);
    if (GL20.glGetShaderi(glCreateShader, 35713) == 0) {
      final String glGetShaderInfoLog = GL20.glGetShaderInfoLog(glCreateShader);
      GL20.glDeleteShader(glCreateShader);
      throw new RuntimeException(
          "Shader compilation failed (" + String.valueOf(obj) + "):\n" + glGetShaderInfoLog);
    }
    return new RhiBlendStateService.ShaderHandle(glCreateShader);
  }

  @Override
  public void destroyShader(final RhiBlendStateService.ShaderHandle shaderHandle) {
    if (shaderHandle.valid()) {
      GL20.glDeleteShader(shaderHandle.id());
    }
  }

  private static int calculateValue(final RhiBlendStateService.BufferAccess bufferAccess) {
    return switch (bufferAccess) {
      default -> throw new MatchException(null, null);
      case STATIC -> 35044;
      case STREAM -> 35040;
      case DYNAMIC -> 35048;
    };
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(final long n, final int n2) {
    return this.createBuffer(n, n2, RhiBlendStateService.BufferAccess.DYNAMIC);
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(
      final long n, final int n2, final RhiBlendStateService.BufferAccess bufferAccess) {
    final int glGenBuffers = GL15.glGenBuffers();
    GL15.glBindBuffer(34962, glGenBuffers);
    GL15.glBufferData(34962, n, calculateValue(bufferAccess));
    GL15.glBindBuffer(34962, 0);
    return new RhiBlendStateService.BufferHandle(glGenBuffers);
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(final int n, final ByteBuffer byteBuffer) {
    return this.createBuffer(n, RhiBlendStateService.BufferAccess.DYNAMIC, byteBuffer);
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(
      final int n,
      final RhiBlendStateService.BufferAccess bufferAccess,
      final ByteBuffer byteBuffer) {
    final int glGenBuffers = GL15.glGenBuffers();
    GL15.glBindBuffer(34962, glGenBuffers);
    GL15.glBufferData(34962, byteBuffer, calculateValue(bufferAccess));
    GL15.glBindBuffer(34962, 0);
    return new RhiBlendStateService.BufferHandle(glGenBuffers);
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(final int n, final float[] array) {
    return this.createBuffer(n, RhiBlendStateService.BufferAccess.DYNAMIC, array);
  }

  @Override
  public RhiBlendStateService.BufferHandle createBuffer(
      final int n, final RhiBlendStateService.BufferAccess bufferAccess, final float[] src) {
    final FloatBuffer memAllocFloat = MemoryUtil.memAllocFloat(src.length);
    try {
      memAllocFloat.put(src).flip();
      final int glGenBuffers = GL15.glGenBuffers();
      GL15.glBindBuffer(34962, glGenBuffers);
      GL15.glBufferData(34962, memAllocFloat, calculateValue(bufferAccess));
      GL15.glBindBuffer(34962, 0);
      return new RhiBlendStateService.BufferHandle(glGenBuffers);
    } finally {
      MemoryUtil.memFree(memAllocFloat);
    }
  }

  @Override
  public void updateBuffer(
      final RhiBlendStateService.BufferHandle bufferHandle,
      final long n,
      final ByteBuffer byteBuffer) {
    GL15.glBindBuffer(34962, bufferHandle.id());
    GL15.glBufferSubData(34962, n, byteBuffer);
    GL15.glBindBuffer(34962, 0);
  }

  @Override
  public void updateBuffer(
      final RhiBlendStateService.BufferHandle bufferHandle, final long n, final float[] array) {
    this.updateBuffer(bufferHandle, n, array, array.length);
  }

  @Override
  public void updateBuffer(
      final RhiBlendStateService.BufferHandle bufferHandle,
      final long n,
      final float[] src,
      final int length) {
    if (length <= 0) {
      return;
    }
    final FloatBuffer memAllocFloat = MemoryUtil.memAllocFloat(length);
    try {
      memAllocFloat.put(src, 0, length).flip();
      GL15.glBindBuffer(34962, bufferHandle.id());
      GL15.glBufferSubData(34962, n, memAllocFloat);
      GL15.glBindBuffer(34962, 0);
    } finally {
      MemoryUtil.memFree(memAllocFloat);
    }
  }

  @Override
  public void orphanBuffer(final RhiBlendStateService.BufferHandle bufferHandle, final long n) {
    GL15.glBindBuffer(34962, bufferHandle.id());
    GL15.glBufferData(34962, n, 35040);
    GL15.glBindBuffer(34962, 0);
  }

  @Override
  public void destroyBuffer(final RhiBlendStateService.BufferHandle bufferHandle) {
    if (bufferHandle.valid()) {
      this.fhlhugm5lep.invalidateBufferCache(bufferHandle.id());
      GL15.glDeleteBuffers(bufferHandle.id());
    }
  }

  @Override
  public RhiBlendStateService.TextureHandle createTexture(
      final RhiBlendStateService.TextureDescriptor textureDescriptor) {
    final int glGetInteger = GL11.glGetInteger(32873);
    final int glGenTextures = GL11.glGenTextures();
    final PixelUnpackState m288gwllrocx = m288gwllrocx();
    boolean b = false;
    try {
      this.m5lulpwgwxe("texture creation");
      GL11.glBindTexture(3553, glGenTextures);
      GL11.glTexImage2D(
          3553,
          0,
          glInternalFormat(textureDescriptor.format()),
          textureDescriptor.width(),
          textureDescriptor.height(),
          0,
          glPixelFormat(textureDescriptor.format()),
          glPixelType(textureDescriptor.format()),
          (ByteBuffer) null);
      GL11.glTexParameteri(3553, 10241, glFilter(textureDescriptor.minFilter()));
      GL11.glTexParameteri(3553, 10240, glFilter(textureDescriptor.magFilter()));
      GL11.glTexParameteri(3553, 10242, glWrap(textureDescriptor.addressMode()));
      GL11.glTexParameteri(3553, 10243, glWrap(textureDescriptor.addressMode()));
      meyzdbkiogv8(textureDescriptor.minFilter());
      final int drainGlErrors = drainGlErrors();
      if (drainGlErrors != 0) {
        throw new IllegalStateException(
            "Texture creation failed with GL error 0x" + Integer.toHexString(drainGlErrors));
      }
      b = true;
    } finally {
      GL11.glBindTexture(3553, glGetInteger);
      micx73bomiw7(m288gwllrocx);
      if (!b && glGenTextures != 0) {
        GL11.glDeleteTextures(glGenTextures);
      }
    }
    this.textureInternalFormats.put(glGenTextures, glInternalFormat(textureDescriptor.format()));
    return new RhiBlendStateService.TextureHandle(glGenTextures);
  }

  @Override
  public RhiBlendStateService.TextureHandle createTexture(
      final RhiBlendStateService.TextureDescriptor textureDescriptor, final ByteBuffer byteBuffer) {
    final int glGetInteger = GL11.glGetInteger(32873);
    final int glGenTextures = GL11.glGenTextures();
    final PixelUnpackState m288gwllrocx = m288gwllrocx();
    boolean b = false;
    try {
      this.m5lulpwgwxe("texture upload");
      GL11.glBindTexture(3553, glGenTextures);
      GL11.glTexImage2D(
          3553,
          0,
          glInternalFormat(textureDescriptor.format()),
          textureDescriptor.width(),
          textureDescriptor.height(),
          0,
          glPixelFormat(textureDescriptor.format()),
          glPixelType(textureDescriptor.format()),
          byteBuffer);
      GL11.glTexParameteri(3553, 10241, glFilter(textureDescriptor.minFilter()));
      GL11.glTexParameteri(3553, 10240, glFilter(textureDescriptor.magFilter()));
      GL11.glTexParameteri(3553, 10242, glWrap(textureDescriptor.addressMode()));
      GL11.glTexParameteri(3553, 10243, glWrap(textureDescriptor.addressMode()));
      meyzdbkiogv8(textureDescriptor.minFilter());
      if (textureDescriptor.minFilter() == RhiBlendStateService.FilterMode.NEAREST_MIPMAP_LINEAR
          || textureDescriptor.minFilter()
              == RhiBlendStateService.FilterMode.LINEAR_MIPMAP_LINEAR) {
        GL30.glGenerateMipmap(3553);
      }
      final int drainGlErrors = drainGlErrors();
      if (drainGlErrors != 0) {
        throw new IllegalStateException(
            "Texture upload failed with GL error 0x" + Integer.toHexString(drainGlErrors));
      }
      b = true;
    } finally {
      GL11.glBindTexture(3553, glGetInteger);
      micx73bomiw7(m288gwllrocx);
      if (!b && glGenTextures != 0) {
        GL11.glDeleteTextures(glGenTextures);
      }
    }
    this.textureInternalFormats.put(glGenTextures, glInternalFormat(textureDescriptor.format()));
    return new RhiBlendStateService.TextureHandle(glGenTextures);
  }

  @Override
  public void updateTexture(
      final RhiBlendStateService.TextureHandle textureHandle,
      final int n,
      final int n2,
      final int n3,
      final int n4,
      final ByteBuffer byteBuffer) {
    final int glGetInteger = GL11.glGetInteger(32873);
    final PixelUnpackState m288gwllrocx = m288gwllrocx();
    try {
      GL11.glBindTexture(3553, textureHandle.id());
      final RhiBlendStateService.TextureFormat mh1t24qx5ubq = this.mh1t24qx5ubq(textureHandle.id());
      GL11.glTexSubImage2D(
          3553,
          0,
          n,
          n2,
          n3,
          n4,
          glPixelFormat(mh1t24qx5ubq),
          glPixelType(mh1t24qx5ubq),
          byteBuffer);
    } finally {
      GL11.glBindTexture(3553, glGetInteger);
      micx73bomiw7(m288gwllrocx);
    }
  }

  @Override
  public void destroyTexture(final RhiBlendStateService.TextureHandle textureHandle) {
    if (textureHandle.valid()) {
      this.textureInternalFormats.remove(textureHandle.id());
      this.fhlhugm5lep.invalidateTextureCache(textureHandle.id());
      GL11.glDeleteTextures(textureHandle.id());
    }
  }

  @Override
  public RhiBlendStateService.TextureDownload downloadTexture(
      final RhiBlendStateService.TextureHandle textureHandle) {
    if (textureHandle == null || !textureHandle.valid()) {
      return null;
    }
    final int glGetInteger = GL11.glGetInteger(32873);
    final int glGetInteger2 = GL11.glGetInteger(3333);
    final int glGetInteger3 = GL11.glGetInteger(35053);
    final int glGetInteger4 = GL11.glGetInteger(3330);
    final int glGetInteger5 = GL11.glGetInteger(3331);
    final int glGetInteger6 = GL11.glGetInteger(3332);
    GL15.glBindBuffer(35051, 0);
    GL11.glPixelStorei(3330, 0);
    GL11.glPixelStorei(3331, 0);
    GL11.glPixelStorei(3332, 0);
    GL11.glBindTexture(3553, textureHandle.id());
    try {
      this.m5lulpwgwxe("texture download");
      final int glGetTexLevelParameteri = GL11.glGetTexLevelParameteri(3553, 0, 4096);
      final int glGetTexLevelParameteri2 = GL11.glGetTexLevelParameteri(3553, 0, 4097);
      if (glGetTexLevelParameteri <= 0
          || glGetTexLevelParameteri2 <= 0
          || glGetTexLevelParameteri > 4096
          || glGetTexLevelParameteri2 > 4096) {
        return null;
      }
      GL11.glPixelStorei(3333, 1);
      ByteBuffer memAlloc =
          MemoryUtil.memAlloc(glGetTexLevelParameteri * glGetTexLevelParameteri2 * 4);
      RhiBlendStateService.TextureDownload textureDownload = null;
      try {
        GL11.glGetTexImage(3553, 0, 6408, 5121, memAlloc);
        if (drainGlErrors() == 0) {
          textureDownload =
              new RhiBlendStateService.TextureDownload(
                  memAlloc, glGetTexLevelParameteri, glGetTexLevelParameteri2);
          memAlloc = null;
        }
      } finally {
        if (memAlloc != null) {
          MemoryUtil.memFree(memAlloc);
        }
      }
      return textureDownload;
    } finally {
      GL11.glBindTexture(3553, glGetInteger);
      GL11.glPixelStorei(3333, glGetInteger2);
      GL11.glPixelStorei(3330, glGetInteger4);
      GL11.glPixelStorei(3331, glGetInteger5);
      GL11.glPixelStorei(3332, glGetInteger6);
      GL15.glBindBuffer(35051, glGetInteger3);
      this.fhlhugm5lep.invalidateTextureCache(textureHandle.id());
    }
  }

  @Override
  public RhiBlendStateService.SamplerHandle createSampler(
      final RhiBlendStateService.SamplerDescriptor samplerDescriptor) {
    final int glGenSamplers = GL33.glGenSamplers();
    GL33.glSamplerParameteri(glGenSamplers, 10241, glFilter(samplerDescriptor.minFilter()));
    GL33.glSamplerParameteri(glGenSamplers, 10240, glFilter(samplerDescriptor.magFilter()));
    GL33.glSamplerParameteri(glGenSamplers, 10242, glWrap(samplerDescriptor.addressMode()));
    GL33.glSamplerParameteri(glGenSamplers, 10243, glWrap(samplerDescriptor.addressMode()));
    m3hb630cydvh(glGenSamplers, samplerDescriptor.minFilter());
    return new RhiBlendStateService.SamplerHandle(glGenSamplers);
  }

  @Override
  public void destroySampler(final RhiBlendStateService.SamplerHandle samplerHandle) {
    if (samplerHandle != null && samplerHandle.valid()) {
      this.fhlhugm5lep.invalidateSamplerCache(samplerHandle.id());
      GL33.glDeleteSamplers(samplerHandle.id());
    }
  }

  private static boolean mi6csq50rk48(final RhiBlendStateService.FilterMode filterMode) {
    return filterMode == RhiBlendStateService.FilterMode.NEAREST_MIPMAP_LINEAR
        || filterMode == RhiBlendStateService.FilterMode.LINEAR_MIPMAP_LINEAR;
  }

  private static float m98jkve5b3cl() {
    final float f7gqy4aroaro = GlShaderFactory.f7gqy4aroaro;
    if (f7gqy4aroaro >= 0.0f) {
      return f7gqy4aroaro;
    }
    float min = 0.0f;
    try {
      final GLCapabilities capabilities = GL.getCapabilities();
      if (capabilities != null && capabilities.GL_EXT_texture_filter_anisotropic) {
        min = Math.min(Float.intBitsToFloat(1090519040), GL11.glGetFloat(34047));
      }
    } catch (final RuntimeException ex) {
      min = 0.0f;
    }
    return GlShaderFactory.f7gqy4aroaro = min;
  }

  private static void m3hb630cydvh(final int n, final RhiBlendStateService.FilterMode filterMode) {
    if (!mi6csq50rk48(filterMode)) {
      return;
    }
    try {
      GL33.glSamplerParameterf(n, 34049, Float.intBitsToFloat(-1098907648));
      final float m98jkve5b3cl = m98jkve5b3cl();
      if (m98jkve5b3cl >= 1.0f) {
        GL33.glSamplerParameterf(n, 34046, m98jkve5b3cl);
      }
    } catch (final RuntimeException ex) {
    }
  }

  private static void meyzdbkiogv8(final RhiBlendStateService.FilterMode filterMode) {
    if (!mi6csq50rk48(filterMode)) {
      return;
    }
    try {
      GL11.glTexParameterf(3553, 34049, Float.intBitsToFloat(-1098907648));
      final float m98jkve5b3cl = m98jkve5b3cl();
      if (m98jkve5b3cl >= 1.0f) {
        GL11.glTexParameterf(3553, 34046, m98jkve5b3cl);
      }
    } catch (final RuntimeException ex) {
    }
  }

  @Override
  public RhiBlendStateService.FramebufferHandle createFramebuffer(
      final RhiBlendStateService.TextureHandle... array) {
    final int glGetInteger = GL11.glGetInteger(36010);
    final int glGetInteger2 = GL11.glGetInteger(36006);
    final int glGenFramebuffers = GL30.glGenFramebuffers();
    boolean b = false;
    try {
      GL30.glBindFramebuffer(36160, glGenFramebuffers);
      for (int i = 0; i < array.length; ++i) {
        GL30.glFramebufferTexture2D(36160, 36064 + i, 3553, array[i].id(), 0);
      }
      if (array.length == 0) {
        GL11.glDrawBuffer(0);
        GL11.glReadBuffer(0);
      } else if (array.length > 1) {
        final int[] array2 = new int[array.length];
        for (int j = 0; j < array2.length; ++j) {
          array2[j] = 36064 + j;
        }
        GL20.glDrawBuffers(array2);
      }
      this.validateBoundFramebuffer();
      this.framebufferColorAttachmentCounts.put(glGenFramebuffers, array.length);
      b = true;
      return new RhiBlendStateService.FramebufferHandle(glGenFramebuffers);
    } finally {
      GL30.glBindFramebuffer(36008, glGetInteger);
      GL30.glBindFramebuffer(36009, glGetInteger2);
      if (!b && glGenFramebuffers != 0) {
        GL30.glDeleteFramebuffers(glGenFramebuffers);
      }
    }
  }

  @Override
  public RhiBlendStateService.FramebufferHandle createFramebuffer(
      final RhiBlendStateService.TextureHandle textureHandle,
      final RhiBlendStateService.TextureHandle textureHandle2) {
    final int glGetInteger = GL11.glGetInteger(36010);
    final int glGetInteger2 = GL11.glGetInteger(36006);
    final int glGenFramebuffers = GL30.glGenFramebuffers();
    boolean b = false;
    try {
      GL30.glBindFramebuffer(36160, glGenFramebuffers);
      if (textureHandle.valid()) {
        GL30.glFramebufferTexture2D(36160, 36064, 3553, textureHandle.id(), 0);
      }
      if (textureHandle2.valid()) {
        GL30.glFramebufferTexture2D(
            36160,
            manpk65hg463(this.textureInternalFormats.getOrDefault(textureHandle2.id(), 0))
                ? 33306
                : 36096,
            3553,
            textureHandle2.id(),
            0);
      }
      final int valid = textureHandle.valid() ? 1 : 0;
      if (valid == 0) {
        GL11.glDrawBuffer(0);
        GL11.glReadBuffer(0);
      }
      this.validateBoundFramebuffer();
      this.framebufferColorAttachmentCounts.put(glGenFramebuffers, valid);
      b = true;
      return new RhiBlendStateService.FramebufferHandle(glGenFramebuffers);
    } finally {
      GL30.glBindFramebuffer(36008, glGetInteger);
      GL30.glBindFramebuffer(36009, glGetInteger2);
      if (!b && glGenFramebuffers != 0) {
        GL30.glDeleteFramebuffers(glGenFramebuffers);
      }
    }
  }

  @Override
  public void destroyFramebuffer(final RhiBlendStateService.FramebufferHandle framebufferHandle) {
    if (framebufferHandle.valid() && framebufferHandle.id() != 0) {
      this.framebufferColorAttachmentCounts.remove(framebufferHandle.id());
      GL30.glDeleteFramebuffers(framebufferHandle.id());
    }
  }

  private void validateBoundFramebuffer() {
    final int glCheckFramebufferStatus = GL30.glCheckFramebufferStatus(36160);
    if (glCheckFramebufferStatus != 36053) {
      throw new IllegalStateException(
          "Framebuffer incomplete: 0x" + Integer.toHexString(glCheckFramebufferStatus));
    }
  }

  @Override
  public RhiBlendStateService.DepthCopyTarget createCompatibleDepthCopyTarget(
      final RhiBlendStateService.FramebufferHandle framebufferHandle, final int i, final int j) {
    if (!framebufferHandle.valid() || i <= 0 || j <= 0) {
      return RhiBlendStateService.DepthCopyTarget.NONE;
    }
    final DepthAttachmentFormat depthAttachmentFormat =
        this.depthAttachmentFormat(framebufferHandle);
    if (depthAttachmentFormat == null) {
      CoreIsInitializedHandler.LOGGER.debug(
          "No supported depth attachment on framebuffer {}", (Object) framebufferHandle.id());
      return RhiBlendStateService.DepthCopyTarget.NONE;
    }
    final int glGetInteger = GL11.glGetInteger(32873);
    final int glGetInteger2 = GL11.glGetInteger(36010);
    final int glGetInteger3 = GL11.glGetInteger(36006);
    final PixelUnpackState m288gwllrocx = m288gwllrocx();
    int glGenTextures = 0;
    int glGenFramebuffers = 0;
    boolean b = false;
    try {
      this.m5lulpwgwxe("compatible depth target creation");
      glGenTextures = GL11.glGenTextures();
      GL11.glBindTexture(3553, glGenTextures);
      GL11.glTexImage2D(
          3553,
          0,
          depthAttachmentFormat.internalFormat(),
          i,
          j,
          0,
          depthAttachmentFormat.pixelFormat(),
          depthAttachmentFormat.pixelType(),
          (ByteBuffer) null);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GL11.glTexParameteri(3553, 34892, 0);
      glGenFramebuffers = GL30.glGenFramebuffers();
      GL30.glBindFramebuffer(36160, glGenFramebuffers);
      GL30.glFramebufferTexture2D(
          36160, depthAttachmentFormat.attachmentPoint(), 3553, glGenTextures, 0);
      GL11.glDrawBuffer(0);
      GL11.glReadBuffer(0);
      final int glCheckFramebufferStatus = GL30.glCheckFramebufferStatus(36160);
      final int drainGlErrors = drainGlErrors();
      if (glCheckFramebufferStatus != 36053 || drainGlErrors != 0) {
        CoreIsInitializedHandler.LOGGER.warn(
            "Could not create compatible depth-copy target (source={}, format=0x{}, status=0x{},"
                + " error=0x{})",
            new Object[] {
              framebufferHandle.id(),
              Integer.toHexString(depthAttachmentFormat.internalFormat()),
              Integer.toHexString(glCheckFramebufferStatus),
              Integer.toHexString(drainGlErrors)
            });
        return RhiBlendStateService.DepthCopyTarget.NONE;
      }
      this.textureInternalFormats.put(glGenTextures, depthAttachmentFormat.internalFormat());
      this.framebufferColorAttachmentCounts.put(glGenFramebuffers, 0);
      CoreIsInitializedHandler.LOGGER.debug(
          "Created depth-copy target {}x{} with GL format 0x{}",
          new Object[] {i, j, Integer.toHexString(depthAttachmentFormat.internalFormat())});
      b = true;
      return new RhiBlendStateService.DepthCopyTarget(
          new RhiBlendStateService.TextureHandle(glGenTextures),
          new RhiBlendStateService.FramebufferHandle(glGenFramebuffers));
    } finally {
      GL11.glBindTexture(3553, glGetInteger);
      micx73bomiw7(m288gwllrocx);
      GL30.glBindFramebuffer(36008, glGetInteger2);
      GL30.glBindFramebuffer(36009, glGetInteger3);
      if (!b) {
        if (glGenFramebuffers != 0) {
          GL30.glDeleteFramebuffers(glGenFramebuffers);
        }
        if (glGenTextures != 0) {
          GL11.glDeleteTextures(glGenTextures);
        }
      }
    }
  }

  @Override
  public boolean blitFramebufferDepth(
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle2,
      final int n,
      final int n2,
      final int n3,
      final int n4,
      final int n5,
      final int n6,
      final int n7,
      final int n8) {
    if (!framebufferHandle.valid() || !framebufferHandle2.valid()) {
      return false;
    }
    final DepthAttachmentFormat depthAttachmentFormat =
        this.depthAttachmentFormat(framebufferHandle);
    final DepthAttachmentFormat m8h19nnnwebu2 = this.depthAttachmentFormat(framebufferHandle2);
    if (depthAttachmentFormat == null
        || m8h19nnnwebu2 == null
        || depthAttachmentFormat.internalFormat() != m8h19nnnwebu2.internalFormat()) {
      return false;
    }
    final int glGetInteger = GL11.glGetInteger(36010);
    final int glGetInteger2 = GL11.glGetInteger(36006);
    try {
      this.m5lulpwgwxe("depth framebuffer blit");
      GL30.glBindFramebuffer(36008, framebufferHandle.id());
      GL30.glBindFramebuffer(36009, framebufferHandle2.id());
      if (GL30.glCheckFramebufferStatus(36008) != 36053
          || GL30.glCheckFramebufferStatus(36009) != 36053) {
        drainGlErrors();
        return false;
      }
      GL30.glBlitFramebuffer(n, n2, n3, n4, n5, n6, n7, n8, 256, 9728);
      return drainGlErrors() == 0;
    } finally {
      GL30.glBindFramebuffer(36008, glGetInteger);
      GL30.glBindFramebuffer(36009, glGetInteger2);
    }
  }

  private DepthAttachmentFormat depthAttachmentFormat(
      final RhiBlendStateService.FramebufferHandle framebufferHandle) {
    final int glGetInteger = GL11.glGetInteger(36010);
    try {
      this.m5lulpwgwxe("depth attachment query");
      GL30.glBindFramebuffer(36008, framebufferHandle.id());
      if (GL30.glCheckFramebufferStatus(36008) != 36053) {
        drainGlErrors();
        return null;
      }
      final int n = (framebufferHandle.id() == 0) ? 6145 : 36096;
      final AttachmentIdentity mdyg5oigw3ek = mdyg5oigw3ek(n);
      if (!mdyg5oigw3ek.present()) {
        drainGlErrors();
        return null;
      }
      final int glGetFramebufferAttachmentParameteri =
          GL30.glGetFramebufferAttachmentParameteri(36008, n, 33302);
      final int glGetFramebufferAttachmentParameteri2 =
          GL30.glGetFramebufferAttachmentParameteri(36008, n, 33297);
      final int n2 = (framebufferHandle.id() == 0) ? 6146 : 36128;
      final AttachmentIdentity depthIdentity = mdyg5oigw3ek(n2);
      int glGetFramebufferAttachmentParameteri3 = 0;
      if (depthIdentity.present() && depthIdentity.equals(mdyg5oigw3ek)) {
        glGetFramebufferAttachmentParameteri3 =
            GL30.glGetFramebufferAttachmentParameteri(36008, n2, 33303);
      }
      final DepthAttachmentFormat mgwulfudeph8 =
          mgwulfudeph8(
              glGetFramebufferAttachmentParameteri,
              glGetFramebufferAttachmentParameteri3,
              glGetFramebufferAttachmentParameteri2);
      return (drainGlErrors() == 0) ? mgwulfudeph8 : null;
    } finally {
      GL30.glBindFramebuffer(36008, glGetInteger);
    }
  }

  private static AttachmentIdentity mdyg5oigw3ek(final int n) {
    final int glGetFramebufferAttachmentParameteri =
        GL30.glGetFramebufferAttachmentParameteri(36008, n, 36048);
    if (glGetFramebufferAttachmentParameteri == 0) {
      return new AttachmentIdentity(glGetFramebufferAttachmentParameteri, 0);
    }
    return new AttachmentIdentity(
        glGetFramebufferAttachmentParameteri,
        GL30.glGetFramebufferAttachmentParameteri(36008, n, 36049));
  }

  private static DepthAttachmentFormat mgwulfudeph8(final int n, final int n2, final int n3) {
    if (n2 == 8) {
      if (n == 24) {
        return new DepthAttachmentFormat(35056, 34041, 34042, 33306);
      }
      if (n == 32 && n3 == 5126) {
        return new DepthAttachmentFormat(36013, 34041, 36269, 33306);
      }
      return null;
    } else {
      final int n5 =
          switch (n) {
            case 16 -> 33189;
            case 24 -> 33190;
            case 32 -> (n3 == 5126) ? 36012 : 33191;
            default -> 0;
          };
      if (n5 == 0) {
        return null;
      }
      return new DepthAttachmentFormat(n5, 6402, 5126, 36096);
    }
  }

  private static boolean manpk65hg463(final int n) {
    return n == 35056 || n == 36013;
  }

  private void m5lulpwgwxe(final String s) {
    final int drainGlErrors = drainGlErrors();
    if (drainGlErrors == 0) {
      return;
    }
    final long nanoTime = System.nanoTime();
    if (this.lastErrorLogTimeNanos == 0L || nanoTime - this.lastErrorLogTimeNanos >= 5000000000L) {
      this.lastErrorLogTimeNanos = nanoTime;
      CoreIsInitializedHandler.LOGGER.warn(
          "Cleared pre-existing GL error 0x{} before {}; it predates the current ellice depth"
              + " operation",
          (Object) Integer.toHexString(drainGlErrors),
          (Object) s);
    }
  }

  private static int drainGlErrors() {
    int n = 0;
    int glGetError;
    while ((glGetError = GL11.glGetError()) != 0) {
      if (n == 0) {
        n = glGetError;
      }
    }
    return n;
  }

  @Override
  public RhiBlendStateService.PipelineHandle createPipeline(final RhiBuilderData rhiBuilderData) {
    final int glCreateProgram = GL20.glCreateProgram();
    if (rhiBuilderData.computeShader().valid()) {
      GL20.glAttachShader(glCreateProgram, rhiBuilderData.computeShader().id());
    } else {
      if (rhiBuilderData.vertexShader().valid()) {
        GL20.glAttachShader(glCreateProgram, rhiBuilderData.vertexShader().id());
      }
      if (rhiBuilderData.fragmentShader().valid()) {
        GL20.glAttachShader(glCreateProgram, rhiBuilderData.fragmentShader().id());
      }
    }
    GL20.glLinkProgram(glCreateProgram);
    if (GL20.glGetProgrami(glCreateProgram, 35714) == 0) {
      final String glGetProgramInfoLog = GL20.glGetProgramInfoLog(glCreateProgram);
      GL20.glDeleteProgram(glCreateProgram);
      throw new RuntimeException("Shader link failed:\n" + glGetProgramInfoLog);
    }
    int glGenVertexArrays = 0;
    if (rhiBuilderData.vertexLayout() != null) {
      glGenVertexArrays = GL30.glGenVertexArrays();
      GL30.glBindVertexArray(glGenVertexArrays);
      final RhiBlendStateService.VertexAttribute[] attributes =
          rhiBuilderData.vertexLayout().attributes();
      for (int length = attributes.length, i = 0; i < length; ++i) {
        GL20.glEnableVertexAttribArray(attributes[i].location());
      }
      GL30.glBindVertexArray(0);
    }
    final int j = this.nextPipelineId++;
    if (j < 0) {
      GL20.glDeleteProgram(glCreateProgram);
      if (glGenVertexArrays != 0) {
        GL30.glDeleteVertexArrays(glGenVertexArrays);
      }
      throw new IllegalStateException("Pipeline handle space exhausted");
    }
    this.pipelines.put(
        j,
        new PipelineState(
            glCreateProgram,
            glGenVertexArrays,
            rhiBuilderData.vertexLayout(),
            rhiBuilderData.blendState(),
            rhiBuilderData.depthStencilState(),
            rhiBuilderData.rasterizerState(),
            rhiBuilderData.topology()));
    return new RhiBlendStateService.PipelineHandle(j);
  }

  @Override
  public void destroyPipeline(final RhiBlendStateService.PipelineHandle pipelineHandle) {
    if (!pipelineHandle.valid()) {
      return;
    }
    final PipelineState pipelineState = this.pipelines.remove(pipelineHandle.id());
    if (pipelineState == null) {
      return;
    }
    this.fhlhugm5lep.invalidateUniformCache(pipelineState.program());
    GL20.glDeleteProgram(pipelineState.program());
    if (pipelineState.vao() != 0) {
      this.fhlhugm5lep.invalidateVaoCache(pipelineState.vao());
      GL30.glDeleteVertexArrays(pipelineState.vao());
    }
  }

  @Override
  public RhiCommandBuffer encoder() {
    return this.fhlhugm5lep;
  }

  @Override
  public StateScope preserveState() {
    this.fhlhugm5lep.saveGLState();
    final GlRenderer fhlhugm5lep = this.fhlhugm5lep;
    Objects.requireNonNull(fhlhugm5lep);
    return fhlhugm5lep::restoreGLState;
  }

  PipelineState getPipeline(final int i) {
    final PipelineState pipelineState = this.pipelines.get(i);
    if (pipelineState == null) {
      throw new IllegalStateException("Invalid pipeline handle: " + i);
    }
    return pipelineState;
  }

  int getFramebufferColorAttachmentCount(final int i) {
    return this.framebufferColorAttachmentCounts.getOrDefault(i, -1);
  }

  private RhiBlendStateService.TextureFormat mh1t24qx5ubq(final int i) {
    final int intValue = this.textureInternalFormats.getOrDefault(i, 0);
    if (intValue == 33321) {
      return RhiBlendStateService.TextureFormat.R8;
    }
    if (intValue == 33323) {
      return RhiBlendStateService.TextureFormat.RG8;
    }
    if (intValue == 34842) {
      return RhiBlendStateService.TextureFormat.RGBA16F;
    }
    if (intValue == 35898) {
      return RhiBlendStateService.TextureFormat.R11G11B10F;
    }
    if (intValue == 33325) {
      return RhiBlendStateService.TextureFormat.R16F;
    }
    if (intValue == 33327) {
      return RhiBlendStateService.TextureFormat.RG16F;
    }
    if (intValue == 33190) {
      return RhiBlendStateService.TextureFormat.DEPTH24;
    }
    if (intValue == 35056) {
      return RhiBlendStateService.TextureFormat.DEPTH24_STENCIL8;
    }
    if (intValue == 36012) {
      return RhiBlendStateService.TextureFormat.DEPTH32F;
    }
    return RhiBlendStateService.TextureFormat.RGBA8;
  }

  static int glInternalFormat(final RhiBlendStateService.TextureFormat textureFormat) {
    return switch (textureFormat) {
      default -> throw new MatchException(null, null);
      case R8 -> 33321;
      case RG8 -> 33323;
      case RGBA8 -> 32856;
      case RGBA16F -> 34842;
      case R11G11B10F -> 35898;
      case R16F -> 33325;
      case RG16F -> 33327;
      case DEPTH24 -> 33190;
      case DEPTH24_STENCIL8 -> 35056;
      case DEPTH32F -> 36012;
    };
  }

  static int glPixelFormat(final RhiBlendStateService.TextureFormat textureFormat) {
    return switch (textureFormat) {
      default -> throw new MatchException(null, null);
      case R8, R16F -> 6403;
      case RG8, RG16F -> 33319;
      case RGBA8, RGBA16F -> 6408;
      case R11G11B10F -> 6407;
      case DEPTH24, DEPTH32F -> 6402;
      case DEPTH24_STENCIL8 -> 34041;
    };
  }

  static int glPixelType(final RhiBlendStateService.TextureFormat textureFormat) {
    return switch (textureFormat) {
      default -> throw new MatchException(null, null);
      case R8, RG8, RGBA8 -> 5121;
      case RGBA16F, R16F, RG16F, DEPTH32F -> 5126;
      case R11G11B10F -> 35899;
      case DEPTH24 -> 5125;
      case DEPTH24_STENCIL8 -> 34042;
    };
  }

  static int glFilter(final RhiBlendStateService.FilterMode filterMode) {
    return switch (filterMode) {
      default -> throw new MatchException(null, null);
      case NEAREST -> 9728;
      case LINEAR -> 9729;
      case NEAREST_MIPMAP_LINEAR -> 9986;
      case LINEAR_MIPMAP_LINEAR -> 9987;
    };
  }

  static int glWrap(final RhiBlendStateService.AddressMode addressMode) {
    return switch (addressMode) {
      default -> throw new MatchException(null, null);
      case REPEAT -> 10497;
      case CLAMP -> 33071;
      case MIRROR -> 33648;
    };
  }

  static int glVertexAttribType(final RhiBlendStateService.VertexFormat vertexFormat) {
    return switch (vertexFormat) {
      default -> throw new MatchException(null, null);
      case FLOAT -> 5126;
      case INT -> 5124;
      case BYTE -> 5120;
      case UBYTE -> 5121;
      case SHORT -> 5122;
      case USHORT -> 5123;
    };
  }

  static int glBlendFactor(final RhiBlendStateService.BlendFactor blendFactor) {
    return switch (blendFactor) {
      default -> throw new MatchException(null, null);
      case ZERO -> 0;
      case ONE -> 1;
      case SRC_COLOR -> 768;
      case ONE_MINUS_SRC_COLOR -> 769;
      case DST_COLOR -> 774;
      case ONE_MINUS_DST_COLOR -> 775;
      case SRC_ALPHA -> 770;
      case ONE_MINUS_SRC_ALPHA -> 771;
      case DST_ALPHA -> 772;
      case ONE_MINUS_DST_ALPHA -> 773;
    };
  }

  static int glBlendOp(final RhiBlendStateService.BlendOp blendOp) {
    return switch (blendOp) {
      default -> throw new MatchException(null, null);
      case ADD -> 32774;
      case SUBTRACT -> 32778;
      case REVERSE_SUBTRACT -> 32779;
      case MIN -> 32775;
      case MAX -> 32776;
    };
  }

  static int glCompareOp(final RhiBlendStateService.CompareOp compareOp) {
    return switch (compareOp) {
      default -> throw new MatchException(null, null);
      case NEVER -> 512;
      case LESS -> 513;
      case EQUAL -> 514;
      case LESS_OR_EQUAL -> 515;
      case GREATER -> 516;
      case NOT_EQUAL -> 517;
      case GREATER_OR_EQUAL -> 518;
      case ALWAYS -> 519;
    };
  }

  static int glTopology(final RhiBlendStateService.PrimitiveTopology primitiveTopology) {
    return switch (primitiveTopology) {
      default -> throw new MatchException(null, null);
      case TRIANGLES -> 4;
      case TRIANGLE_STRIP -> 5;
      case TRIANGLE_FAN -> 6;
      case LINES -> 1;
      case LINE_STRIP -> 3;
      case POINTS -> 0;
    };
  }

  static {
    GlShaderFactory.f7gqy4aroaro = Float.intBitsToFloat(-1082130432);
  }

  record PixelUnpackState(int buffer, int alignment, int rowLength, int skipRows, int skipPixels) {}

  record DepthAttachmentFormat(
      int internalFormat, int pixelFormat, int pixelType, int attachmentPoint) {}

  record AttachmentIdentity(int type, int name) {
    boolean present() {
      return this.type != 0;
    }
  }

  record PipelineState(
      int program,
      int vao,
      RhiBlendStateService.VertexLayout layout,
      RhiBlendStateService.BlendState blend,
      RhiBlendStateService.DepthStencilState depthStencil,
      RhiBlendStateService.RasterizerState rasterizer,
      RhiBlendStateService.PrimitiveTopology topology) {}
}
