package dev.felix.ellice.render.render3d.post;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.render3d.Render3dViewService;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.lwjgl.system.MemoryStack;

public final class PostRenderer implements AutoCloseable {
  private static final RhiBlendStateService.VertexLayout feu2hlubpv12;
  private final RhiOperationHandler rhiOperationHandler;
  private final ShaderOverrideRootService f39svw4c9znf;
  private final Map<String, PostIdService> text;
  private final Map<String, ShaderRecipeService> text2;
  private RhiBlendStateService.BufferHandle fjmhbji8l2d;
  private RhiBlendStateService.TextureHandle depthTextureHandle;
  private RhiBlendStateService.TextureHandle textureHandle2;
  private RhiBlendStateService.TextureHandle textureHandle;
  private RhiBlendStateService.FramebufferHandle framebufferHandle;
  private RhiBlendStateService.FramebufferHandle framebufferHandle2;
  private RhiBlendStateService.FramebufferHandle currentFramebufferHandle;
  private int ffyjswu3hjpv;
  private int count2;
  private int count3;
  private long timestamp;
  private long timestamp2;
  private long timestamp3;
  private boolean enabled2;
  private boolean enabled3;

  public PostRenderer(
      final RhiOperationHandler rhiOperationHandler, final ShaderOverrideRootService f39svw4c9znf) {
    this.text = new LinkedHashMap<String, PostIdService>();
    this.text2 = new LinkedHashMap<String, ShaderRecipeService>();
    this.fjmhbji8l2d = RhiBlendStateService.BufferHandle.NONE;
    this.depthTextureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
    this.currentFramebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.count3 = Integer.MIN_VALUE;
    this.timestamp = Long.MIN_VALUE;
    this.rhiOperationHandler = rhiOperationHandler;
    this.f39svw4c9znf = f39svw4c9znf;
  }

  public synchronized PostIdService register(final PostIdService postIdService) {
    if (this.enabled3) {
      throw new IllegalStateException("Post-process chain is closed");
    }
    if (this.text.containsKey(postIdService.id())) {
      throw new IllegalArgumentException(
          "World post effect already registered: " + postIdService.id());
    }
    this.text.put(postIdService.id(), postIdService);
    this.text2.put(
        postIdService.id(),
        new ShaderRecipeService(
            this.rhiOperationHandler,
            this.f39svw4c9znf,
            new ShaderData(
                postIdService.shader(),
                PostRenderer.feu2hlubpv12,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)));
    return postIdService;
  }

  public synchronized Optional<PostIdService> effect(final String s) {
    return Optional.ofNullable(this.text.get(s));
  }

  public synchronized boolean unregister(final String s) {
    final PostIdService postIdService = this.text.remove(s);
    try (final ShaderRecipeService shaderRecipeService = this.text2.remove(s)) {}
    return postIdService != null;
  }

  public synchronized boolean hasEnabledEffects() {
    return this.text.values().stream().anyMatch(PostIdService::enabled);
  }

  public synchronized boolean needsDepthCapture() {
    return this.text.values().stream()
        .anyMatch(postIdService -> postIdService.enabled() && postIdService.requiresDepth());
  }

  public void captureDepth(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService) {
    if (!this.needsDepthCapture()) {
      return;
    }
    this.timestamp = Long.MIN_VALUE;
    this.mesdpdt9mdab(
        framebufferHandle, render3dViewService.width(), render3dViewService.height(), true);
    if (!this.enabled2) {
      return;
    }
    rhiCommandBuffer.clearScissor();
    if (this.rhiOperationHandler.blitFramebufferDepth(
        framebufferHandle,
        this.currentFramebufferHandle,
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height(),
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height())) {
      this.timestamp = render3dViewService.frameIndex();
    } else {
      this.enabled2 = false;
      this.timestamp2 = 0L;
      this.updateState9("Depth copy was rejected or failed");
    }
  }

  public void render(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService) {
    this.mdkrq6eaoa0q(
        rhiCommandBuffer, framebufferHandle, render3dViewService, PostIdService.Stage.BEFORE_HUD);
  }

  public void renderBeforeHand(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService) {
    this.mdkrq6eaoa0q(
        rhiCommandBuffer, framebufferHandle, render3dViewService, PostIdService.Stage.BEFORE_HAND);
  }

  private void mdkrq6eaoa0q(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService,
      final PostIdService.Stage stage) {
    final List<PostIdService> mdkv0lywmw4b = this.mdkv0lywmw4b(stage);
    if (mdkv0lywmw4b.isEmpty()) {
      return;
    }
    this.mesdpdt9mdab(
        framebufferHandle, render3dViewService.width(), render3dViewService.height(), false);
    this.m49fi2hwo4ta();
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.blitFramebuffer(
        framebufferHandle,
        this.framebufferHandle,
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height(),
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height(),
        RhiBlendStateService.FilterMode.NEAREST,
        RhiBlendStateService.FramebufferAspect.COLOR.bit);
    if (this.needsDepthCapture() && this.timestamp != render3dViewService.frameIndex()) {
      rhiCommandBuffer.beginRenderPass(this.currentFramebufferHandle);
      rhiCommandBuffer.clearDepth(1.0f);
      rhiCommandBuffer.endRenderPass();
    }
    final ArrayList<ResolvedPass> list = new ArrayList<>(mdkv0lywmw4b.size());
    for (final PostIdService postIdService : mdkv0lywmw4b) {
      final RhiBlendStateService.PipelineHandle resolve =
          this.mj7aeclo5mv8(postIdService.id()).resolve();
      if (resolve.valid()) {
        list.add(new ResolvedPass(postIdService, resolve));
      }
    }
    if (list.isEmpty()) {
      return;
    }
    RhiBlendStateService.TextureHandle textureHandle = this.textureHandle;
    boolean b = true;
    for (int i = 0; i < list.size(); ++i) {
      final ResolvedPass resolvedPass = list.get(i);
      final PostIdService effect = resolvedPass.effect();
      final boolean b2 = i == list.size() - 1;
      rhiCommandBuffer.beginRenderPass(
          b2 ? framebufferHandle : (b ? this.framebufferHandle2 : this.framebufferHandle));
      rhiCommandBuffer.setViewport(0, 0, render3dViewService.width(), render3dViewService.height());
      rhiCommandBuffer.clearScissor();
      rhiCommandBuffer.bindPipeline(resolvedPass.pipeline());
      rhiCommandBuffer.bindVertexBuffer(this.fjmhbji8l2d, 0);
      rhiCommandBuffer.bindTexture(textureHandle, 0);
      rhiCommandBuffer.bindTexture(this.depthTextureHandle, 1);
      rhiCommandBuffer.pushInt("uColorTexture", 0);
      rhiCommandBuffer.pushInt("uDepthTexture", 1);
      this.m9ts7douxgmk(rhiCommandBuffer, render3dViewService);
      effect.bindings().apply(rhiCommandBuffer);
      rhiCommandBuffer.draw(4, 1, 0);
      rhiCommandBuffer.endRenderPass();
      if (!b2) {
        b = !b;
        textureHandle = (b ? this.textureHandle : this.textureHandle2);
      }
    }
  }

  private synchronized List<PostIdService> mdkv0lywmw4b(final PostIdService.Stage stage) {
    final ArrayList list = new ArrayList();
    for (final PostIdService e : this.text.values()) {
      if (e.enabled() && e.stage() == stage) {
        list.add(e);
      }
    }
    return list;
  }

  private synchronized ShaderRecipeService mj7aeclo5mv8(final String s) {
    return this.text2.get(s);
  }

  private void m9ts7douxgmk(
      final RhiCommandBuffer rhiCommandBuffer, final Render3dViewService render3dViewService) {
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) render3dViewService.width(), (float) render3dViewService.height());
    rhiCommandBuffer.pushFloat("uTime", (float) render3dViewService.timeSeconds());
    rhiCommandBuffer.pushFloat("uDeltaTime", render3dViewService.deltaSeconds());
    rhiCommandBuffer.pushInt("uFrameIndex", (int) (render3dViewService.frameIndex() & 0x7FFFFFFFL));
    rhiCommandBuffer.pushInt(
        "uDepthAvailable", (this.timestamp == render3dViewService.frameIndex()) ? 1 : 0);
    final Vector3d cameraPosition = render3dViewService.cameraPosition();
    final Vector3d previousCameraPosition = render3dViewService.previousCameraPosition();
    rhiCommandBuffer.pushVec3(
        "uCameraPosition",
        (float) cameraPosition.x,
        (float) cameraPosition.y,
        (float) cameraPosition.z);
    rhiCommandBuffer.pushVec3(
        "uCameraWrapped",
        (float)
            (cameraPosition.x
                - Math.floor(cameraPosition.x / Double.longBitsToDouble(4661225614328463360L))
                    * Double.longBitsToDouble(4661225614328463360L)),
        (float)
            (cameraPosition.y
                - Math.floor(cameraPosition.y / Double.longBitsToDouble(4661225614328463360L))
                    * Double.longBitsToDouble(4661225614328463360L)),
        (float)
            (cameraPosition.z
                - Math.floor(cameraPosition.z / Double.longBitsToDouble(4661225614328463360L))
                    * Double.longBitsToDouble(4661225614328463360L)));
    rhiCommandBuffer.pushVec3(
        "uPreviousCameraPosition",
        (float) previousCameraPosition.x,
        (float) previousCameraPosition.y,
        (float) previousCameraPosition.z);
    rhiCommandBuffer.pushVec3(
        "uCameraDelta",
        (float) (cameraPosition.x - previousCameraPosition.x),
        (float) (cameraPosition.y - previousCameraPosition.y),
        (float) (cameraPosition.z - previousCameraPosition.z));
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState3(
          rhiCommandBuffer, "uView", render3dViewService.view(), stackPush.mallocFloat(16));
      updateState3(
          rhiCommandBuffer,
          "uProjection",
          render3dViewService.projection(),
          stackPush.mallocFloat(16));
      updateState3(
          rhiCommandBuffer,
          "uInvView",
          render3dViewService.inverseView(),
          stackPush.mallocFloat(16));
      updateState3(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
      updateState3(
          rhiCommandBuffer,
          "uViewProjection",
          render3dViewService.viewProjection(),
          stackPush.mallocFloat(16));
      updateState3(
          rhiCommandBuffer,
          "uPreviousViewProjection",
          render3dViewService.previousViewProjection(),
          stackPush.mallocFloat(16));
    }
  }

  private static void updateState3(
      final RhiCommandBuffer rhiCommandBuffer,
      final String s,
      final Matrix4f matrix4f,
      final FloatBuffer floatBuffer) {
    matrix4f.get(floatBuffer);
    rhiCommandBuffer.pushMatrix4(s, floatBuffer);
  }

  private void m49fi2hwo4ta() {
    if (this.fjmhbji8l2d.valid()) {
      return;
    }
    this.fjmhbji8l2d =
        this.rhiOperationHandler.createBuffer(
            RhiBlendStateService.BufferUsage.VERTEX.bit,
            RhiBlendStateService.BufferAccess.STATIC,
            new float[] {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
  }

  private void mesdpdt9mdab(
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final int b,
      final int b2,
      final boolean b3) {
    if (this.enabled3) {
      return;
    }
    final int max = Math.max(1, b);
    final int max2 = Math.max(1, b2);
    final boolean b4 = b3 && !this.enabled2 && System.nanoTime() >= this.timestamp2;
    if (max == this.ffyjswu3hjpv
        && max2 == this.count2
        && framebufferHandle.id() == this.count3
        && this.textureHandle.valid()
        && this.textureHandle2.valid()
        && this.framebufferHandle.valid()
        && this.framebufferHandle2.valid()) {
      if (b4 || !this.textureHandle.valid() || !this.currentFramebufferHandle.valid()) {
        this.mdr92my2t03d(framebufferHandle, max, max2);
      }
      return;
    }
    this.updateState8();
    final RhiBlendStateService.TextureDescriptor renderTarget =
        RhiBlendStateService.TextureDescriptor.renderTarget(
            max, max2, RhiBlendStateService.TextureFormat.RGBA8);
    RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.TextureHandle textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.FramebufferHandle firstFramebufferHandle =
        RhiBlendStateService.FramebufferHandle.NONE;
    RhiBlendStateService.FramebufferHandle framebufferHandle2 =
        RhiBlendStateService.FramebufferHandle.NONE;
    try {
      textureHandle = this.rhiOperationHandler.createTexture(renderTarget);
      textureHandle2 = this.rhiOperationHandler.createTexture(renderTarget);
      firstFramebufferHandle = this.rhiOperationHandler.createFramebuffer(textureHandle);
      framebufferHandle2 = this.rhiOperationHandler.createFramebuffer(textureHandle2);
      this.textureHandle = textureHandle;
      this.textureHandle2 = textureHandle2;
      this.framebufferHandle = firstFramebufferHandle;
      this.framebufferHandle2 = framebufferHandle2;
      this.ffyjswu3hjpv = max;
      this.count2 = max2;
      this.count3 = framebufferHandle.id();
      this.mdr92my2t03d(framebufferHandle, max, max2);
      CoreIsInitializedHandler.LOGGER.debug(
          "World post targets resized to {}x{}", (Object) this.ffyjswu3hjpv, (Object) this.count2);
    } catch (final RuntimeException ex) {
      if (this.textureHandle.equals(textureHandle)) {
        this.updateState8();
      } else {
        if (firstFramebufferHandle.valid()) {
          this.rhiOperationHandler.destroyFramebuffer(firstFramebufferHandle);
        }
        if (framebufferHandle2.valid()) {
          this.rhiOperationHandler.destroyFramebuffer(framebufferHandle2);
        }
        if (textureHandle.valid()) {
          this.rhiOperationHandler.destroyTexture(textureHandle);
        }
        if (textureHandle2.valid()) {
          this.rhiOperationHandler.destroyTexture(textureHandle2);
        }
      }
      throw ex;
    }
  }

  private void mdr92my2t03d(
      final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2) {
    RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
    RhiBlendStateService.FramebufferHandle currentFramebufferHandle =
        RhiBlendStateService.FramebufferHandle.NONE;
    boolean enabled2 = false;
    try {
      final RhiBlendStateService.DepthCopyTarget compatibleDepthCopyTarget =
          this.rhiOperationHandler.createCompatibleDepthCopyTarget(framebufferHandle, n, n2);
      if (compatibleDepthCopyTarget.valid()) {
        textureHandle = compatibleDepthCopyTarget.texture();
        currentFramebufferHandle = compatibleDepthCopyTarget.framebuffer();
        enabled2 = true;
      } else {
        textureHandle =
            this.rhiOperationHandler.createTexture(
                new RhiBlendStateService.TextureDescriptor(
                    n,
                    n2,
                    RhiBlendStateService.TextureFormat.DEPTH32F,
                    RhiBlendStateService.FilterMode.NEAREST,
                    RhiBlendStateService.FilterMode.NEAREST,
                    RhiBlendStateService.AddressMode.CLAMP));
        currentFramebufferHandle =
            this.rhiOperationHandler.createFramebuffer(
                RhiBlendStateService.TextureHandle.NONE, textureHandle);
        this.timestamp2 = System.nanoTime() + 1000000000L;
        this.updateState9("No compatible external depth target available");
      }
      this.updateState7();
      this.depthTextureHandle = textureHandle;
      this.currentFramebufferHandle = currentFramebufferHandle;
      this.enabled2 = enabled2;
      if (enabled2) {
        this.timestamp2 = 0L;
      }
      this.timestamp = Long.MIN_VALUE;
    } catch (final RuntimeException ex) {
      if (currentFramebufferHandle.valid()) {
        this.rhiOperationHandler.destroyFramebuffer(currentFramebufferHandle);
      }
      if (textureHandle.valid()) {
        this.rhiOperationHandler.destroyTexture(textureHandle);
      }
      throw ex;
    }
  }

  private void updateState7() {
    if (this.currentFramebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(this.currentFramebufferHandle);
    }
    if (this.depthTextureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.depthTextureHandle);
    }
    this.currentFramebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.depthTextureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.enabled2 = false;
    this.timestamp = Long.MIN_VALUE;
  }

  private void updateState8() {
    if (this.framebufferHandle.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle);
    }
    if (this.framebufferHandle2.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(this.framebufferHandle2);
    }
    if (this.textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle);
    }
    if (this.textureHandle2.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle2);
    }
    this.updateState7();
    this.framebufferHandle = RhiBlendStateService.FramebufferHandle.NONE;
    this.framebufferHandle2 = RhiBlendStateService.FramebufferHandle.NONE;
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.textureHandle2 = RhiBlendStateService.TextureHandle.NONE;
    this.ffyjswu3hjpv = 0;
    this.count2 = 0;
    this.count3 = Integer.MIN_VALUE;
  }

  private void updateState9(final String s) {
    final long nanoTime = System.nanoTime();
    if (this.timestamp3 != 0L && nanoTime - this.timestamp3 < 5000000000L) {
      return;
    }
    this.timestamp3 = nanoTime;
    CoreIsInitializedHandler.LOGGER.warn(
        "{}; depth-aware world effects will use far depth", (Object) s);
  }

  @Override
  public synchronized void close() {
    if (this.enabled3) {
      return;
    }
    this.enabled3 = true;
    final Iterator<ShaderRecipeService> iterator = this.text2.values().iterator();
    while (iterator.hasNext()) {
      iterator.next().close();
    }
    this.text2.clear();
    this.text.clear();
    this.updateState8();
    if (this.fjmhbji8l2d.valid()) {
      this.rhiOperationHandler.destroyBuffer(this.fjmhbji8l2d);
    }
    this.fjmhbji8l2d = RhiBlendStateService.BufferHandle.NONE;
  }

  static {
    feu2hlubpv12 =
        RhiBlendStateService.VertexLayout.of(
            8,
            new RhiBlendStateService.VertexAttribute(
                0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
  }

  record ResolvedPass(PostIdService effect, RhiBlendStateService.PipelineHandle pipeline) {}
}
