



package dev.felix.ellice.render.render3d.post;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.lwjgl.system.MemoryStack;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import dev.felix.ellice.render.render3d.Render3dViewService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import java.util.Optional;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import java.util.LinkedHashMap;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import java.util.Map;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiBlendStateService;

public final class PostRenderer implements AutoCloseable
{
    private static final RhiBlendStateService.VertexLayout feu2hlubpv12;
    private final RhiOperationHandler f4tcn3mhegtx;
    private final ShaderOverrideRootService f39svw4c9znf;
    private final Map<String, PostIdService> f7yddbswc1ff;
    private final Map<String, ShaderRecipeService> f4zv8nsstfzr;
    private RhiBlendStateService.BufferHandle fjmhbji8l2d;
    private RhiBlendStateService.TextureHandle fjngzb9hc480;
    private RhiBlendStateService.TextureHandle fd53jw7rdqu0;
    private RhiBlendStateService.TextureHandle fdqi1s5ro0e8;
    private RhiBlendStateService.FramebufferHandle ffk48bb5dhj0;
    private RhiBlendStateService.FramebufferHandle fb97oarwc1yo;
    private RhiBlendStateService.FramebufferHandle f813pwyhfsd4;
    private int ffyjswu3hjpv;
    private int faumex4bpw6l;
    private int fbv7k2ughce3;
    private long f8r5kmu6af14;
    private long f1mc2pkgrtir;
    private long fj80nokfbvoh;
    private boolean f1tvi6j2jpqj;
    private boolean f582k6ibw4fe;
    
    public PostRenderer(final RhiOperationHandler f4tcn3mhegtx, final ShaderOverrideRootService f39svw4c9znf) {
        this.f7yddbswc1ff = new LinkedHashMap<String, PostIdService>();
        this.f4zv8nsstfzr = new LinkedHashMap<String, ShaderRecipeService>();
        this.fjmhbji8l2d = RhiBlendStateService.BufferHandle.NONE;
        this.fjngzb9hc480 = RhiBlendStateService.TextureHandle.NONE;
        this.fd53jw7rdqu0 = RhiBlendStateService.TextureHandle.NONE;
        this.fdqi1s5ro0e8 = RhiBlendStateService.TextureHandle.NONE;
        this.ffk48bb5dhj0 = RhiBlendStateService.FramebufferHandle.NONE;
        this.fb97oarwc1yo = RhiBlendStateService.FramebufferHandle.NONE;
        this.f813pwyhfsd4 = RhiBlendStateService.FramebufferHandle.NONE;
        this.fbv7k2ughce3 = Integer.MIN_VALUE;
        this.f8r5kmu6af14 = Long.MIN_VALUE;
        this.f4tcn3mhegtx = f4tcn3mhegtx;
        this.f39svw4c9znf = f39svw4c9znf;
    }
    
    public synchronized PostIdService register(final PostIdService postIdService) {
        if (this.f582k6ibw4fe) {
            throw new IllegalStateException("Post-process chain is closed");
        }
        if (this.f7yddbswc1ff.containsKey(postIdService.id())) {
            throw new IllegalArgumentException("World post effect already registered: " + postIdService.id());
        }
        this.f7yddbswc1ff.put(postIdService.id(), postIdService);
        this.f4zv8nsstfzr.put(postIdService.id(), new ShaderRecipeService(this.f4tcn3mhegtx, this.f39svw4c9znf, new ShaderData(postIdService.shader(), PostRenderer.feu2hlubpv12, RhiBlendStateService.BlendState.DISABLED, RhiBlendStateService.DepthStencilState.DISABLED, RhiBlendStateService.RasterizerState.NO_CULL, RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP)));
        return postIdService;
    }
    
    public synchronized Optional<PostIdService> effect(final String s) {
        return Optional.ofNullable(this.f7yddbswc1ff.get(s));
    }
    
    public synchronized boolean unregister(final String s) {
        final PostIdService postIdService = this.f7yddbswc1ff.remove(s);
        try (final ShaderRecipeService shaderRecipeService = this.f4zv8nsstfzr.remove(s)) {}
        return postIdService != null;
    }
    
    public synchronized boolean hasEnabledEffects() {
        return this.f7yddbswc1ff.values().stream().anyMatch(PostIdService::enabled);
    }
    
    public synchronized boolean needsDepthCapture() {
        return this.f7yddbswc1ff.values().stream().anyMatch(postIdService -> postIdService.enabled() && postIdService.requiresDepth());
    }
    
    public void captureDepth(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final Render3dViewService render3dViewService) {
        if (!this.needsDepthCapture()) {
            return;
        }
        this.f8r5kmu6af14 = Long.MIN_VALUE;
        this.mesdpdt9mdab(framebufferHandle, render3dViewService.width(), render3dViewService.height(), true);
        if (!this.f1tvi6j2jpqj) {
            return;
        }
        rhiCommandBuffer.clearScissor();
        if (this.f4tcn3mhegtx.blitFramebufferDepth(framebufferHandle, this.f813pwyhfsd4, 0, 0, render3dViewService.width(), render3dViewService.height(), 0, 0, render3dViewService.width(), render3dViewService.height())) {
            this.f8r5kmu6af14 = render3dViewService.frameIndex();
        }
        else {
            this.f1tvi6j2jpqj = false;
            this.f1mc2pkgrtir = 0L;
            this.mcxwsmr28a1g("Depth copy was rejected or failed");
        }
    }
    
    public void render(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final Render3dViewService render3dViewService) {
        this.mdkrq6eaoa0q(rhiCommandBuffer, framebufferHandle, render3dViewService, PostIdService.Stage.BEFORE_HUD);
    }
    
    public void renderBeforeHand(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final Render3dViewService render3dViewService) {
        this.mdkrq6eaoa0q(rhiCommandBuffer, framebufferHandle, render3dViewService, PostIdService.Stage.BEFORE_HAND);
    }
    
    private void mdkrq6eaoa0q(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final Render3dViewService render3dViewService, final PostIdService.Stage stage) {
        final List<PostIdService> mdkv0lywmw4b = this.mdkv0lywmw4b(stage);
        if (mdkv0lywmw4b.isEmpty()) {
            return;
        }
        this.mesdpdt9mdab(framebufferHandle, render3dViewService.width(), render3dViewService.height(), false);
        this.m49fi2hwo4ta();
        rhiCommandBuffer.clearScissor();
        rhiCommandBuffer.blitFramebuffer(framebufferHandle, this.ffk48bb5dhj0, 0, 0, render3dViewService.width(), render3dViewService.height(), 0, 0, render3dViewService.width(), render3dViewService.height(), RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.FramebufferAspect.COLOR.bit);
        if (this.needsDepthCapture() && this.f8r5kmu6af14 != render3dViewService.frameIndex()) {
            rhiCommandBuffer.beginRenderPass(this.f813pwyhfsd4);
            rhiCommandBuffer.clearDepth(1.0f);
            rhiCommandBuffer.endRenderPass();
        }
        final ArrayList<ResolvedPass> list = new ArrayList<>(mdkv0lywmw4b.size());
        for (final PostIdService postIdService : mdkv0lywmw4b) {
            final RhiBlendStateService.PipelineHandle resolve = this.mj7aeclo5mv8(postIdService.id()).resolve();
            if (resolve.valid()) {
                list.add(new ResolvedPass(postIdService, resolve));
            }
        }
        if (list.isEmpty()) {
            return;
        }
        RhiBlendStateService.TextureHandle fjngzb9hc480 = this.fjngzb9hc480;
        boolean b = true;
        for (int i = 0; i < list.size(); ++i) {
            final ResolvedPass resolvedPass = list.get(i);
            final PostIdService effect = resolvedPass.effect();
            final boolean b2 = i == list.size() - 1;
            rhiCommandBuffer.beginRenderPass(b2 ? framebufferHandle : (b ? this.fb97oarwc1yo : this.ffk48bb5dhj0));
            rhiCommandBuffer.setViewport(0, 0, render3dViewService.width(), render3dViewService.height());
            rhiCommandBuffer.clearScissor();
            rhiCommandBuffer.bindPipeline(resolvedPass.pipeline());
            rhiCommandBuffer.bindVertexBuffer(this.fjmhbji8l2d, 0);
            rhiCommandBuffer.bindTexture(fjngzb9hc480, 0);
            rhiCommandBuffer.bindTexture(this.fdqi1s5ro0e8, 1);
            rhiCommandBuffer.pushInt("uColorTexture", 0);
            rhiCommandBuffer.pushInt("uDepthTexture", 1);
            this.m9ts7douxgmk(rhiCommandBuffer, render3dViewService);
            effect.bindings().apply(rhiCommandBuffer);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
            if (!b2) {
                b = !b;
                fjngzb9hc480 = (b ? this.fjngzb9hc480 : this.fd53jw7rdqu0);
            }
        }
    }
    
    private synchronized List<PostIdService> mdkv0lywmw4b(final PostIdService.Stage stage) {
        final ArrayList list = new ArrayList();
        for (final PostIdService e : this.f7yddbswc1ff.values()) {
            if (e.enabled() && e.stage() == stage) {
                list.add(e);
            }
        }
        return list;
    }
    
    private synchronized ShaderRecipeService mj7aeclo5mv8(final String s) {
        return this.f4zv8nsstfzr.get(s);
    }
    
    private void m9ts7douxgmk(final RhiCommandBuffer rhiCommandBuffer, final Render3dViewService render3dViewService) {
        rhiCommandBuffer.pushVec2("uResolution", (float)render3dViewService.width(), (float)render3dViewService.height());
        rhiCommandBuffer.pushFloat("uTime", (float)render3dViewService.timeSeconds());
        rhiCommandBuffer.pushFloat("uDeltaTime", render3dViewService.deltaSeconds());
        rhiCommandBuffer.pushInt("uFrameIndex", (int)(render3dViewService.frameIndex() & 0x7FFFFFFFL));
        rhiCommandBuffer.pushInt("uDepthAvailable", (this.f8r5kmu6af14 == render3dViewService.frameIndex()) ? 1 : 0);
        final Vector3d cameraPosition = render3dViewService.cameraPosition();
        final Vector3d previousCameraPosition = render3dViewService.previousCameraPosition();
        rhiCommandBuffer.pushVec3("uCameraPosition", (float)cameraPosition.x, (float)cameraPosition.y, (float)cameraPosition.z);
        rhiCommandBuffer.pushVec3("uCameraWrapped", (float)(cameraPosition.x - Math.floor(cameraPosition.x / Double.longBitsToDouble(4661225614328463360L)) * Double.longBitsToDouble(4661225614328463360L)), (float)(cameraPosition.y - Math.floor(cameraPosition.y / Double.longBitsToDouble(4661225614328463360L)) * Double.longBitsToDouble(4661225614328463360L)), (float)(cameraPosition.z - Math.floor(cameraPosition.z / Double.longBitsToDouble(4661225614328463360L)) * Double.longBitsToDouble(4661225614328463360L)));
        rhiCommandBuffer.pushVec3("uPreviousCameraPosition", (float)previousCameraPosition.x, (float)previousCameraPosition.y, (float)previousCameraPosition.z);
        rhiCommandBuffer.pushVec3("uCameraDelta", (float)(cameraPosition.x - previousCameraPosition.x), (float)(cameraPosition.y - previousCameraPosition.y), (float)(cameraPosition.z - previousCameraPosition.z));
        try (final MemoryStack stackPush = MemoryStack.stackPush()) {
            mfacuihul41j(rhiCommandBuffer, "uView", render3dViewService.view(), stackPush.mallocFloat(16));
            mfacuihul41j(rhiCommandBuffer, "uProjection", render3dViewService.projection(), stackPush.mallocFloat(16));
            mfacuihul41j(rhiCommandBuffer, "uInvView", render3dViewService.inverseView(), stackPush.mallocFloat(16));
            mfacuihul41j(rhiCommandBuffer, "uInvProjection", render3dViewService.inverseProjection(), stackPush.mallocFloat(16));
            mfacuihul41j(rhiCommandBuffer, "uViewProjection", render3dViewService.viewProjection(), stackPush.mallocFloat(16));
            mfacuihul41j(rhiCommandBuffer, "uPreviousViewProjection", render3dViewService.previousViewProjection(), stackPush.mallocFloat(16));
        }
    }
    
    private static void mfacuihul41j(final RhiCommandBuffer rhiCommandBuffer, final String s, final Matrix4f matrix4f, final FloatBuffer floatBuffer) {
        matrix4f.get(floatBuffer);
        rhiCommandBuffer.pushMatrix4(s, floatBuffer);
    }
    
    private void m49fi2hwo4ta() {
        if (this.fjmhbji8l2d.valid()) {
            return;
        }
        this.fjmhbji8l2d = this.f4tcn3mhegtx.createBuffer(RhiBlendStateService.BufferUsage.VERTEX.bit, RhiBlendStateService.BufferAccess.STATIC, new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f });
    }
    
    private void mesdpdt9mdab(final RhiBlendStateService.FramebufferHandle framebufferHandle, final int b, final int b2, final boolean b3) {
        if (this.f582k6ibw4fe) {
            return;
        }
        final int max = Math.max(1, b);
        final int max2 = Math.max(1, b2);
        final boolean b4 = b3 && !this.f1tvi6j2jpqj && System.nanoTime() >= this.f1mc2pkgrtir;
        if (max == this.ffyjswu3hjpv && max2 == this.faumex4bpw6l && framebufferHandle.id() == this.fbv7k2ughce3 && this.fjngzb9hc480.valid() && this.fd53jw7rdqu0.valid() && this.ffk48bb5dhj0.valid() && this.fb97oarwc1yo.valid()) {
            if (b4 || !this.fdqi1s5ro0e8.valid() || !this.f813pwyhfsd4.valid()) {
                this.mdr92my2t03d(framebufferHandle, max, max2);
            }
            return;
        }
        this.m3zhbvf5ptce();
        final RhiBlendStateService.TextureDescriptor renderTarget = RhiBlendStateService.TextureDescriptor.renderTarget(max, max2, RhiBlendStateService.TextureFormat.RGBA8);
        RhiBlendStateService.TextureHandle fjngzb9hc480 = RhiBlendStateService.TextureHandle.NONE;
        RhiBlendStateService.TextureHandle fd53jw7rdqu0 = RhiBlendStateService.TextureHandle.NONE;
        RhiBlendStateService.FramebufferHandle ffk48bb5dhj0 = RhiBlendStateService.FramebufferHandle.NONE;
        RhiBlendStateService.FramebufferHandle fb97oarwc1yo = RhiBlendStateService.FramebufferHandle.NONE;
        try {
            fjngzb9hc480 = this.f4tcn3mhegtx.createTexture(renderTarget);
            fd53jw7rdqu0 = this.f4tcn3mhegtx.createTexture(renderTarget);
            ffk48bb5dhj0 = this.f4tcn3mhegtx.createFramebuffer(fjngzb9hc480);
            fb97oarwc1yo = this.f4tcn3mhegtx.createFramebuffer(fd53jw7rdqu0);
            this.fjngzb9hc480 = fjngzb9hc480;
            this.fd53jw7rdqu0 = fd53jw7rdqu0;
            this.ffk48bb5dhj0 = ffk48bb5dhj0;
            this.fb97oarwc1yo = fb97oarwc1yo;
            this.ffyjswu3hjpv = max;
            this.faumex4bpw6l = max2;
            this.fbv7k2ughce3 = framebufferHandle.id();
            this.mdr92my2t03d(framebufferHandle, max, max2);
            CoreIsInitializedHandler.LOGGER.debug("World post targets resized to {}x{}", (Object)this.ffyjswu3hjpv, (Object)this.faumex4bpw6l);
        }
        catch (final RuntimeException ex) {
            if (this.fjngzb9hc480.equals(fjngzb9hc480)) {
                this.m3zhbvf5ptce();
            }
            else {
                if (ffk48bb5dhj0.valid()) {
                    this.f4tcn3mhegtx.destroyFramebuffer(ffk48bb5dhj0);
                }
                if (fb97oarwc1yo.valid()) {
                    this.f4tcn3mhegtx.destroyFramebuffer(fb97oarwc1yo);
                }
                if (fjngzb9hc480.valid()) {
                    this.f4tcn3mhegtx.destroyTexture(fjngzb9hc480);
                }
                if (fd53jw7rdqu0.valid()) {
                    this.f4tcn3mhegtx.destroyTexture(fd53jw7rdqu0);
                }
            }
            throw ex;
        }
    }
    
    private void mdr92my2t03d(final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2) {
        RhiBlendStateService.TextureHandle fdqi1s5ro0e8 = RhiBlendStateService.TextureHandle.NONE;
        RhiBlendStateService.FramebufferHandle f813pwyhfsd4 = RhiBlendStateService.FramebufferHandle.NONE;
        boolean f1tvi6j2jpqj = false;
        try {
            final RhiBlendStateService.DepthCopyTarget compatibleDepthCopyTarget = this.f4tcn3mhegtx.createCompatibleDepthCopyTarget(framebufferHandle, n, n2);
            if (compatibleDepthCopyTarget.valid()) {
                fdqi1s5ro0e8 = compatibleDepthCopyTarget.texture();
                f813pwyhfsd4 = compatibleDepthCopyTarget.framebuffer();
                f1tvi6j2jpqj = true;
            }
            else {
                fdqi1s5ro0e8 = this.f4tcn3mhegtx.createTexture(new RhiBlendStateService.TextureDescriptor(n, n2, RhiBlendStateService.TextureFormat.DEPTH32F, RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.AddressMode.CLAMP));
                f813pwyhfsd4 = this.f4tcn3mhegtx.createFramebuffer(RhiBlendStateService.TextureHandle.NONE, fdqi1s5ro0e8);
                this.f1mc2pkgrtir = System.nanoTime() + 1000000000L;
                this.mcxwsmr28a1g("No compatible external depth target available");
            }
            this.m947wot99im1();
            this.fdqi1s5ro0e8 = fdqi1s5ro0e8;
            this.f813pwyhfsd4 = f813pwyhfsd4;
            this.f1tvi6j2jpqj = f1tvi6j2jpqj;
            if (f1tvi6j2jpqj) {
                this.f1mc2pkgrtir = 0L;
            }
            this.f8r5kmu6af14 = Long.MIN_VALUE;
        }
        catch (final RuntimeException ex) {
            if (f813pwyhfsd4.valid()) {
                this.f4tcn3mhegtx.destroyFramebuffer(f813pwyhfsd4);
            }
            if (fdqi1s5ro0e8.valid()) {
                this.f4tcn3mhegtx.destroyTexture(fdqi1s5ro0e8);
            }
            throw ex;
        }
    }
    
    private void m947wot99im1() {
        if (this.f813pwyhfsd4.valid()) {
            this.f4tcn3mhegtx.destroyFramebuffer(this.f813pwyhfsd4);
        }
        if (this.fdqi1s5ro0e8.valid()) {
            this.f4tcn3mhegtx.destroyTexture(this.fdqi1s5ro0e8);
        }
        this.f813pwyhfsd4 = RhiBlendStateService.FramebufferHandle.NONE;
        this.fdqi1s5ro0e8 = RhiBlendStateService.TextureHandle.NONE;
        this.f1tvi6j2jpqj = false;
        this.f8r5kmu6af14 = Long.MIN_VALUE;
    }
    
    private void m3zhbvf5ptce() {
        if (this.ffk48bb5dhj0.valid()) {
            this.f4tcn3mhegtx.destroyFramebuffer(this.ffk48bb5dhj0);
        }
        if (this.fb97oarwc1yo.valid()) {
            this.f4tcn3mhegtx.destroyFramebuffer(this.fb97oarwc1yo);
        }
        if (this.fjngzb9hc480.valid()) {
            this.f4tcn3mhegtx.destroyTexture(this.fjngzb9hc480);
        }
        if (this.fd53jw7rdqu0.valid()) {
            this.f4tcn3mhegtx.destroyTexture(this.fd53jw7rdqu0);
        }
        this.m947wot99im1();
        this.ffk48bb5dhj0 = RhiBlendStateService.FramebufferHandle.NONE;
        this.fb97oarwc1yo = RhiBlendStateService.FramebufferHandle.NONE;
        this.fjngzb9hc480 = RhiBlendStateService.TextureHandle.NONE;
        this.fd53jw7rdqu0 = RhiBlendStateService.TextureHandle.NONE;
        this.ffyjswu3hjpv = 0;
        this.faumex4bpw6l = 0;
        this.fbv7k2ughce3 = Integer.MIN_VALUE;
    }
    
    private void mcxwsmr28a1g(final String s) {
        final long nanoTime = System.nanoTime();
        if (this.fj80nokfbvoh != 0L && nanoTime - this.fj80nokfbvoh < 5000000000L) {
            return;
        }
        this.fj80nokfbvoh = nanoTime;
        CoreIsInitializedHandler.LOGGER.warn("{}; depth-aware world effects will use far depth", (Object)s);
    }
    
    @Override
    public synchronized void close() {
        if (this.f582k6ibw4fe) {
            return;
        }
        this.f582k6ibw4fe = true;
        final Iterator<ShaderRecipeService> iterator = this.f4zv8nsstfzr.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().close();
        }
        this.f4zv8nsstfzr.clear();
        this.f7yddbswc1ff.clear();
        this.m3zhbvf5ptce();
        if (this.fjmhbji8l2d.valid()) {
            this.f4tcn3mhegtx.destroyBuffer(this.fjmhbji8l2d);
        }
        this.fjmhbji8l2d = RhiBlendStateService.BufferHandle.NONE;
    }
    
    static {
        feu2hlubpv12 = RhiBlendStateService.VertexLayout.of(8, new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
    }
    
    record ResolvedPass(PostIdService effect, RhiBlendStateService.PipelineHandle pipeline) {}
}
