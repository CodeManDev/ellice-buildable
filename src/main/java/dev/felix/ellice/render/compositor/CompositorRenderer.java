



package dev.felix.ellice.render.compositor;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiRepository;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;

public class CompositorRenderer
{
    private final RhiOperationHandler fadxdm0pvp2s;
    private final RhiBlendStateService.BufferHandle f59ziqcrdevn;
    private boolean fgp4kxmstrft;
    private float f3qsv83h1tv6;
    private float f3wi8ko72vxm;
    private boolean f8j71bnbrrwq;
    private float fgbsa1q0km1p;
    private boolean filehlzcqm3x;
    private float f19rcorbed5l;
    private boolean f8601aks51xt;
    private float fhyvbuntjblk;
    private float f5zfg28s7io3;
    private int ffg5tjzfm2ys;
    private boolean fh8mdaz32mei;
    private RhiBlendStateService.PipelineHandle fe801insmxil;
    private RhiBlendStateService.PipelineHandle fedjagsxwyhj;
    private RhiBlendStateService.PipelineHandle f7mzscb565ar;
    private RhiBlendStateService.PipelineHandle f2umw7wnpr5w;
    private RhiBlendStateService.PipelineHandle f2po2ce39rx1;
    private RhiBlendStateService.PipelineHandle f18bodyt8bfw;
    private RhiBlendStateService.TextureHandle f9mfwpp6se22;
    private RhiBlendStateService.FramebufferHandle fdzibtop8rqz;
    private RhiBlendStateService.TextureHandle fd8chcu0uiuu;
    private RhiBlendStateService.FramebufferHandle fb81mngirlq3;
    private boolean f33s8vlekfax;
    private static final int f9dj02iunk5z = 4;
    private final RhiBlendStateService.TextureHandle[] fhzrxaes7s67;
    private final RhiBlendStateService.FramebufferHandle[] f1bv0si2h1l1;
    private final int[] f3pun78adj6m;
    private final int[] fwo3utbrzxa;
    private RhiBlendStateService.PipelineHandle fig9zjnt6w22;
    private RhiBlendStateService.PipelineHandle f3toy1jlf54s;
    private RhiBlendStateService.TextureHandle fb8aww6tw52r;
    private RhiBlendStateService.FramebufferHandle fj7v4up9rx2e;
    private int fcwwzoua67wk;
    private int fahhvskq07gm;
    private int fivx15jk8erh;
    private int fh0sj2ihnpa5;
    private boolean fceyh7ct2dip;
    
    public CompositorRenderer(final RhiOperationHandler fadxdm0pvp2s, final RhiBlendStateService.BufferHandle f59ziqcrdevn) {
        this.f3qsv83h1tv6 = Float.intBitsToFloat(1061158912);
        this.f3wi8ko72vxm = Float.intBitsToFloat(1048576000);
        this.fgbsa1q0km1p = Float.intBitsToFloat(994352038);
        this.f19rcorbed5l = Float.intBitsToFloat(1051931443);
        this.fhyvbuntjblk = Float.intBitsToFloat(1056964608);
        this.f5zfg28s7io3 = Float.intBitsToFloat(1060320051);
        this.fh8mdaz32mei = true;
        this.f9mfwpp6se22 = RhiBlendStateService.TextureHandle.NONE;
        this.fdzibtop8rqz = RhiBlendStateService.FramebufferHandle.NONE;
        this.fd8chcu0uiuu = RhiBlendStateService.TextureHandle.NONE;
        this.fb81mngirlq3 = RhiBlendStateService.FramebufferHandle.NONE;
        this.fhzrxaes7s67 = new RhiBlendStateService.TextureHandle[4];
        this.f1bv0si2h1l1 = new RhiBlendStateService.FramebufferHandle[4];
        this.f3pun78adj6m = new int[4];
        this.fwo3utbrzxa = new int[4];
        this.fb8aww6tw52r = RhiBlendStateService.TextureHandle.NONE;
        this.fj7v4up9rx2e = RhiBlendStateService.FramebufferHandle.NONE;
        this.fadxdm0pvp2s = fadxdm0pvp2s;
        this.f59ziqcrdevn = f59ziqcrdevn;
    }
    
    public void setBloom(final boolean fgp4kxmstrft, final float f3qsv83h1tv6, final float f3wi8ko72vxm) {
        this.fgp4kxmstrft = fgp4kxmstrft;
        this.f3qsv83h1tv6 = f3qsv83h1tv6;
        this.f3wi8ko72vxm = f3wi8ko72vxm;
    }
    
    public void setChromatic(final boolean f8j71bnbrrwq, final float fgbsa1q0km1p) {
        this.f8j71bnbrrwq = f8j71bnbrrwq;
        this.fgbsa1q0km1p = fgbsa1q0km1p;
    }
    
    public void setVignette(final boolean filehlzcqm3x, final float f19rcorbed5l) {
        this.filehlzcqm3x = filehlzcqm3x;
        this.f19rcorbed5l = f19rcorbed5l;
    }
    
    public void setMotionBlur(final boolean f8601aks51xt, final float fhyvbuntjblk, final float f5zfg28s7io3, final int ffg5tjzfm2ys) {
        if (this.f8601aks51xt != f8601aks51xt) {
            this.fh8mdaz32mei = true;
        }
        this.f8601aks51xt = f8601aks51xt;
        this.fhyvbuntjblk = fhyvbuntjblk;
        this.f5zfg28s7io3 = f5zfg28s7io3;
        this.ffg5tjzfm2ys = ffg5tjzfm2ys;
    }
    
    public boolean hasAnyEffect() {
        return this.fgp4kxmstrft || this.f8j71bnbrrwq || this.filehlzcqm3x || this.f8601aks51xt;
    }
    
    public void render(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2) {
        if (!this.hasAnyEffect()) {
            return;
        }
        if (!this.fceyh7ct2dip) {
            this.m3vmpupt2a5v();
        }
        if (!this.fceyh7ct2dip) {
            return;
        }
        this.m3xwprnugksd(n, n2);
        if (this.f8601aks51xt) {
            final int glGetInteger = GL11.glGetInteger(34016);
            final int[] array = new int[4];
            for (int i = 0; i < 4; ++i) {
                GL13.glActiveTexture(33984 + i);
                array[i] = GL11.glGetInteger(32873);
            }
            final RhiBlendStateService.TextureHandle textureHandle = this.f33s8vlekfax ? this.fd8chcu0uiuu : this.f9mfwpp6se22;
            final RhiBlendStateService.FramebufferHandle framebufferHandle2 = this.f33s8vlekfax ? this.fdzibtop8rqz : this.fb81mngirlq3;
            rhiCommandBuffer.blitFramebuffer(framebufferHandle, this.fj7v4up9rx2e, 0, 0, n, n2, 0, 0, n, n2, RhiBlendStateService.FilterMode.NEAREST);
            rhiCommandBuffer.beginRenderPass(framebufferHandle2);
            rhiCommandBuffer.setViewport(0, 0, n, n2);
            rhiCommandBuffer.bindPipeline(this.f2po2ce39rx1);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            rhiCommandBuffer.bindTexture(this.fb8aww6tw52r, 0);
            rhiCommandBuffer.bindTexture(textureHandle, 1);
            rhiCommandBuffer.pushInt("uCurrent", 0);
            rhiCommandBuffer.pushInt("uHistory", 1);
            rhiCommandBuffer.pushFloat("uStrength", this.fhyvbuntjblk);
            rhiCommandBuffer.pushFloat("uPersistence", this.f5zfg28s7io3);
            rhiCommandBuffer.pushInt("uMode", this.ffg5tjzfm2ys);
            rhiCommandBuffer.pushInt("uFirstFrame", this.fh8mdaz32mei ? 1 : 0);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
            GL11.glColorMask(true, true, true, this.fh8mdaz32mei = false);
            rhiCommandBuffer.blitFramebuffer(framebufferHandle2, framebufferHandle, 0, 0, n, n2, 0, 0, n, n2, RhiBlendStateService.FilterMode.NEAREST);
            GL11.glColorMask(true, true, true, true);
            this.f33s8vlekfax = !this.f33s8vlekfax;
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, n, n2);
            for (int j = 0; j < 4; ++j) {
                GL13.glActiveTexture(33984 + j);
                GL11.glBindTexture(3553, array[j]);
            }
            GL13.glActiveTexture(glGetInteger);
            rhiCommandBuffer.invalidateBindCache();
        }
        if (this.fgp4kxmstrft) {
            rhiCommandBuffer.blitFramebuffer(framebufferHandle, this.fj7v4up9rx2e, 0, 0, n, n2, 0, 0, n, n2, RhiBlendStateService.FilterMode.LINEAR);
            rhiCommandBuffer.bindPipeline(this.fedjagsxwyhj);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[0], 0.0f, 0.0f, 0.0f, 1.0f);
            rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[0], this.fwo3utbrzxa[0]);
            rhiCommandBuffer.bindTexture(this.fb8aww6tw52r, 0);
            rhiCommandBuffer.pushInt("uTexture", 0);
            rhiCommandBuffer.pushFloat("uThreshold", this.f3qsv83h1tv6);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
            rhiCommandBuffer.bindPipeline(this.fig9zjnt6w22);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            for (int k = 1; k < 4; ++k) {
                rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[k], 0.0f, 0.0f, 0.0f, 1.0f);
                rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[k], this.fwo3utbrzxa[k]);
                rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[k - 1], 0);
                rhiCommandBuffer.pushInt("uTexture", 0);
                rhiCommandBuffer.pushVec2("uHalfPixel", Float.intBitsToFloat(1056964608) / this.f3pun78adj6m[k - 1], Float.intBitsToFloat(1056964608) / this.fwo3utbrzxa[k - 1]);
                rhiCommandBuffer.draw(4, 1, 0);
                rhiCommandBuffer.endRenderPass();
            }
            rhiCommandBuffer.bindPipeline(this.f3toy1jlf54s);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            for (int l = 3; l > 0; --l) {
                rhiCommandBuffer.beginRenderPass(this.f1bv0si2h1l1[l - 1]);
                rhiCommandBuffer.setViewport(0, 0, this.f3pun78adj6m[l - 1], this.fwo3utbrzxa[l - 1]);
                rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[l], 0);
                rhiCommandBuffer.pushInt("uTexture", 0);
                rhiCommandBuffer.pushVec2("uHalfPixel", Float.intBitsToFloat(1056964608) / this.f3pun78adj6m[l], Float.intBitsToFloat(1056964608) / this.fwo3utbrzxa[l]);
                rhiCommandBuffer.draw(4, 1, 0);
                rhiCommandBuffer.endRenderPass();
            }
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, n, n2);
            rhiCommandBuffer.bindPipeline(this.f7mzscb565ar);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            rhiCommandBuffer.bindTexture(this.fhzrxaes7s67[0], 0);
            rhiCommandBuffer.pushInt("uBloom", 0);
            rhiCommandBuffer.pushFloat("uIntensity", this.f3wi8ko72vxm);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
        }
        if (this.f8j71bnbrrwq) {
            rhiCommandBuffer.blitFramebuffer(framebufferHandle, this.fj7v4up9rx2e, 0, 0, n, n2, 0, 0, n, n2, RhiBlendStateService.FilterMode.LINEAR);
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, n, n2);
            rhiCommandBuffer.bindPipeline(this.f2umw7wnpr5w);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            rhiCommandBuffer.bindTexture(this.fb8aww6tw52r, 0);
            rhiCommandBuffer.pushInt("uTexture", 0);
            rhiCommandBuffer.pushFloat("uIntensity", this.fgbsa1q0km1p);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
        }
        if (this.filehlzcqm3x) {
            rhiCommandBuffer.beginRenderPass(framebufferHandle);
            rhiCommandBuffer.setViewport(0, 0, n, n2);
            rhiCommandBuffer.bindPipeline(this.fe801insmxil);
            rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
            rhiCommandBuffer.pushFloat("uIntensity", this.f19rcorbed5l);
            rhiCommandBuffer.draw(4, 1, 0);
            rhiCommandBuffer.endRenderPass();
        }
    }
    
    public void renderBackgroundDesaturate(final RhiCommandBuffer rhiCommandBuffer, final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2, final float b) {
        if (b <= Float.intBitsToFloat(981668463)) {
            return;
        }
        if (!this.fceyh7ct2dip) {
            this.m3vmpupt2a5v();
        }
        if (!this.fceyh7ct2dip || this.f18bodyt8bfw == null) {
            return;
        }
        this.mh0ekecnghrg(n, n2);
        rhiCommandBuffer.blitFramebuffer(framebufferHandle, this.fj7v4up9rx2e, 0, 0, n, n2, 0, 0, n, n2, RhiBlendStateService.FilterMode.NEAREST);
        rhiCommandBuffer.beginRenderPass(framebufferHandle);
        rhiCommandBuffer.setViewport(0, 0, n, n2);
        rhiCommandBuffer.bindPipeline(this.f18bodyt8bfw);
        rhiCommandBuffer.bindVertexBuffer(this.f59ziqcrdevn, 0);
        rhiCommandBuffer.bindTexture(this.fb8aww6tw52r, 0);
        rhiCommandBuffer.pushInt("uTexture", 0);
        rhiCommandBuffer.pushFloat("uAmount", Math.max(0.0f, Math.min(1.0f, b)));
        GL11.glColorMask(true, true, true, false);
        rhiCommandBuffer.draw(4, 1, 0);
        GL11.glColorMask(true, true, true, true);
        rhiCommandBuffer.endRenderPass();
    }
    
    private void m3vmpupt2a5v() {
        try {
            final RhiBlendStateService.VertexLayout of = RhiBlendStateService.VertexLayout.of(8, new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
            final RhiBlendStateService.ShaderHandle shader = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("fullscreen"));
            final RhiBlendStateService.ShaderHandle shader2 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("vignette"));
            this.fe801insmxil = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader2).vertexLayout(of).blend(RhiBlendStateService.BlendState.PREMULTIPLIED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader2);
            final RhiBlendStateService.ShaderHandle shader3 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("bloom_threshold"));
            this.fedjagsxwyhj = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader3).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader3);
            final RhiBlendStateService.ShaderHandle shader4 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("bloom_composite"));
            this.f7mzscb565ar = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader4).vertexLayout(of).blend(RhiBlendStateService.BlendState.ADDITIVE).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader4);
            final RhiBlendStateService.ShaderHandle shader5 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_down"));
            final RhiBlendStateService.ShaderHandle shader6 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("kawase_up"));
            this.fig9zjnt6w22 = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader5).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.f3toy1jlf54s = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader6).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader5);
            this.fadxdm0pvp2s.destroyShader(shader6);
            final RhiBlendStateService.ShaderHandle shader7 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("chromatic"));
            this.f2umw7wnpr5w = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader7).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader7);
            final RhiBlendStateService.ShaderHandle shader8 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("motion_blur"));
            this.f2po2ce39rx1 = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader8).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader8);
            final RhiBlendStateService.ShaderHandle shader9 = this.fadxdm0pvp2s.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("desaturate"));
            this.f18bodyt8bfw = this.fadxdm0pvp2s.createPipeline(RhiBuilderData.graphics().vertex(shader).fragment(shader9).vertexLayout(of).blend(RhiBlendStateService.BlendState.DISABLED).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP).build());
            this.fadxdm0pvp2s.destroyShader(shader9);
            this.fadxdm0pvp2s.destroyShader(shader);
            this.fceyh7ct2dip = true;
            CoreIsInitializedHandler.LOGGER.info("PostFX stack initialized");
        }
        catch (final Exception ex) {
            CoreIsInitializedHandler.LOGGER.error("Failed to init PostFX stack", (Throwable)ex);
        }
    }
    
    private void m3xwprnugksd(int max, int max2) {
        max = Math.max(1, max);
        max2 = Math.max(1, max2);
        if (max == this.fivx15jk8erh && max2 == this.fh0sj2ihnpa5) {
            return;
        }
        this.mh0ekecnghrg(this.fivx15jk8erh = max, this.fh0sj2ihnpa5 = max2);
        if (this.f9mfwpp6se22.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.f9mfwpp6se22);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fdzibtop8rqz);
        }
        if (this.fd8chcu0uiuu.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.fd8chcu0uiuu);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fb81mngirlq3);
        }
        this.f9mfwpp6se22 = this.fadxdm0pvp2s.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(max, max2, RhiBlendStateService.TextureFormat.RGBA8));
        this.fdzibtop8rqz = this.fadxdm0pvp2s.createFramebuffer(this.f9mfwpp6se22);
        this.fd8chcu0uiuu = this.fadxdm0pvp2s.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(max, max2, RhiBlendStateService.TextureFormat.RGBA8));
        this.fb81mngirlq3 = this.fadxdm0pvp2s.createFramebuffer(this.fd8chcu0uiuu);
        this.f33s8vlekfax = false;
        this.fh8mdaz32mei = true;
        int max3 = max / 2;
        int max4 = max2 / 2;
        for (int i = 0; i < 4; ++i) {
            if (this.fhzrxaes7s67[i] != null && this.fhzrxaes7s67[i].valid()) {
                this.fadxdm0pvp2s.destroyFramebuffer(this.f1bv0si2h1l1[i]);
                this.fadxdm0pvp2s.destroyTexture(this.fhzrxaes7s67[i]);
            }
            this.f3pun78adj6m[i] = Math.max(1, max3);
            this.fwo3utbrzxa[i] = Math.max(1, max4);
            this.fhzrxaes7s67[i] = this.fadxdm0pvp2s.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(this.f3pun78adj6m[i], this.fwo3utbrzxa[i], RhiBlendStateService.TextureFormat.R11G11B10F));
            this.f1bv0si2h1l1[i] = this.fadxdm0pvp2s.createFramebuffer(this.fhzrxaes7s67[i]);
            max3 = Math.max(1, max3 / 2);
            max4 = Math.max(1, max4 / 2);
        }
    }
    
    private void mh0ekecnghrg(int max, int max2) {
        max = Math.max(1, max);
        max2 = Math.max(1, max2);
        if (this.fb8aww6tw52r.valid() && max == this.fcwwzoua67wk && max2 == this.fahhvskq07gm) {
            return;
        }
        if (this.fb8aww6tw52r.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.fb8aww6tw52r);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fj7v4up9rx2e);
        }
        this.fcwwzoua67wk = max;
        this.fahhvskq07gm = max2;
        this.fb8aww6tw52r = this.fadxdm0pvp2s.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(max, max2, RhiBlendStateService.TextureFormat.RGBA8));
        this.fj7v4up9rx2e = this.fadxdm0pvp2s.createFramebuffer(this.fb8aww6tw52r);
    }
    
    public void shutdown() {
        if (this.fb8aww6tw52r.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.fb8aww6tw52r);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fj7v4up9rx2e);
        }
        if (this.f9mfwpp6se22.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.f9mfwpp6se22);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fdzibtop8rqz);
        }
        if (this.fd8chcu0uiuu.valid()) {
            this.fadxdm0pvp2s.destroyTexture(this.fd8chcu0uiuu);
            this.fadxdm0pvp2s.destroyFramebuffer(this.fb81mngirlq3);
        }
        for (int i = 0; i < 4; ++i) {
            if (this.fhzrxaes7s67[i] != null && this.fhzrxaes7s67[i].valid()) {
                this.fadxdm0pvp2s.destroyFramebuffer(this.f1bv0si2h1l1[i]);
                this.fadxdm0pvp2s.destroyTexture(this.fhzrxaes7s67[i]);
            }
        }
        this.mj55puarzcxe(this.fe801insmxil);
        this.mj55puarzcxe(this.fedjagsxwyhj);
        this.mj55puarzcxe(this.f7mzscb565ar);
        this.mj55puarzcxe(this.f2umw7wnpr5w);
        this.mj55puarzcxe(this.f2po2ce39rx1);
        this.mj55puarzcxe(this.fig9zjnt6w22);
        this.mj55puarzcxe(this.f3toy1jlf54s);
        this.mj55puarzcxe(this.f18bodyt8bfw);
    }
    
    private void mj55puarzcxe(final RhiBlendStateService.PipelineHandle pipelineHandle) {
        if (pipelineHandle != null && pipelineHandle.valid()) {
            this.fadxdm0pvp2s.destroyPipeline(pipelineHandle);
        }
    }
}
