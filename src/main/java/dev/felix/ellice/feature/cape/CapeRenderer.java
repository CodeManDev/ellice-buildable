








package dev.felix.ellice.feature.cape;

import dev.felix.ellice.feature.cape.CapePlayerService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class CapeRenderer
implements AutoCloseable {
    private RhiOperationHandler f43tdql6am6m;
    private GlRenderer fer2y5srl5ly;
    private RhiBlendStateService.ShaderHandle fjepsae8jmjj = RhiBlendStateService.ShaderHandle.NONE;
    private RhiBlendStateService.ShaderHandle fv70saumqw2 = RhiBlendStateService.ShaderHandle.NONE;
    private RhiBlendStateService.PipelineHandle f4epxtpzgty2 = RhiBlendStateService.PipelineHandle.NONE;
    private RhiBlendStateService.TextureHandle f14t7sidb4lq = RhiBlendStateService.TextureHandle.NONE;
    private RhiBlendStateService.TextureHandle f3m5hh98zbkp = RhiBlendStateService.TextureHandle.NONE;
    private RhiBlendStateService.TextureHandle f2qdeu1l4gum = RhiBlendStateService.TextureHandle.NONE;
    private RhiBlendStateService.TextureHandle f3ipby4ak0nk = RhiBlendStateService.TextureHandle.NONE;
    private RhiBlendStateService.FramebufferHandle fj9plig0g6st = RhiBlendStateService.FramebufferHandle.NONE;
    private RhiBlendStateService.BufferHandle fh0syswn4dtv = RhiBlendStateService.BufferHandle.NONE;
    private BufferedImage fgilbr5r1wjs;
    private BufferedImage fjaxzfhz25nq;
    private int fd9xvsrmil49;
    private int f696ct7oo2rm;
    private float[] fzd4crawkjh;
    private boolean f4rpwu0ta3xp;
    private boolean fj16anr1hr1e;

    


    public RhiBlendStateService.TextureHandle render(int n, int n2, BufferedImage bufferedImage, boolean bl, BufferedImage bufferedImage2, double d, double d2, double d3, double d4) {
        if (this.f43tdql6am6m == null) {
            this.f43tdql6am6m = RhiDeviceService.device();
            this.fer2y5srl5ly = (GlRenderer)this.f43tdql6am6m.encoder();
        }
        this.fer2y5srl5ly.saveGLState();
        try {
            boolean bl2;
            this.mpt5jbra3fr();
            this.mi98hnwuvzoe(n, n2);
            if (this.fgilbr5r1wjs != bufferedImage) {
                if (this.f2qdeu1l4gum.valid()) {
                    this.f43tdql6am6m.destroyTexture(this.f2qdeu1l4gum);
                }
                this.f2qdeu1l4gum = this.mfwcn076ye07(RhiBlendStateService.TextureHandle.NONE, bufferedImage);
                this.fgilbr5r1wjs = bufferedImage;
            }
            if (this.fjaxzfhz25nq != bufferedImage2) {
                this.f3ipby4ak0nk = this.mfwcn076ye07(this.f3ipby4ak0nk, bufferedImage2);
                this.fjaxzfhz25nq = bufferedImage2;
            }
            this.fer2y5srl5ly.beginRenderPass(this.fj9plig0g6st, 0.0f, 0.0f, 0.0f, 0.0f);
            this.fer2y5srl5ly.clearScissor();
            this.fer2y5srl5ly.setColorWriteMask(true, true, true, true);
            this.fer2y5srl5ly.setViewport(0, 0, this.fd9xvsrmil49, this.f696ct7oo2rm);
            this.fer2y5srl5ly.clearDepth(1.0f);
            this.fer2y5srl5ly.bindPipeline(this.f4epxtpzgty2);
            Matrix4f matrix4f = new Matrix4f().translate(0.0f, Float.intBitsToFloat(1098907648), 0.0f).rotateY((float)Math.toRadians(d)).rotateX((float)Math.toRadians(d2)).translate(0.0f, Float.intBitsToFloat(-1048576000), 0.0f);
            float f = (float)(Double.longBitsToDouble(4626604192193052672L) / d3);
            float f2 = (float)this.fd9xvsrmil49 / (float)this.f696ct7oo2rm;
            Matrix4f matrix4f2 = new Matrix4f().ortho(-f * f2, f * f2, -f, f, Float.intBitsToFloat(0x3DCCCCCD), Float.intBitsToFloat(1128792064)).lookAt(0.0f, Float.intBitsToFloat(1101004800), Float.intBitsToFloat(1116471296), 0.0f, Float.intBitsToFloat(1098907648), 0.0f, 0.0f, 1.0f, 0.0f).mul((Matrix4fc)matrix4f);
            try (MemoryStack memoryStack = MemoryStack.stackPush();){
                this.fer2y5srl5ly.pushMatrix4("uMvp", matrix4f2.get(memoryStack.mallocFloat(16)));
                this.fer2y5srl5ly.pushMatrix4("uModel", matrix4f.get(memoryStack.mallocFloat(16)));
            }
            this.fer2y5srl5ly.pushInt("uTexture", 0);
            boolean bl3 = bl2 = bufferedImage.getHeight() == 32;
            if (this.fzd4crawkjh == null || this.f4rpwu0ta3xp != bl || this.fj16anr1hr1e != bl2) {
                this.fzd4crawkjh = CapePlayerService.player(bl, bl2);
                this.f4rpwu0ta3xp = bl;
                this.fj16anr1hr1e = bl2;
            }
            this.mjnrwjpgjb4z(this.fzd4crawkjh, this.f2qdeu1l4gum);
            this.mjnrwjpgjb4z(CapePlayerService.cape(d4), this.f3ipby4ak0nk);
            this.fer2y5srl5ly.endRenderPass();
            RhiBlendStateService.TextureHandle textureHandle = this.f14t7sidb4lq;
            return textureHandle;
        }
        finally {
            this.fer2y5srl5ly.restoreGLState();
        }
    }

    private void mpt5jbra3fr() {
        if (this.f4epxtpzgty2.valid()) {
            return;
        }
        this.fjepsae8jmjj = this.f43tdql6am6m.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("cape_preview"));
        this.fv70saumqw2 = this.f43tdql6am6m.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("cape_preview"));
        this.f4epxtpzgty2 = this.f43tdql6am6m.createPipeline(RhiBuilderData.graphics().vertex(this.fjepsae8jmjj).fragment(this.fv70saumqw2).vertexLayout(RhiBlendStateService.VertexLayout.of(32, new RhiBlendStateService.VertexAttribute(0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L), new RhiBlendStateService.VertexAttribute(1, 3, RhiBlendStateService.VertexFormat.FLOAT, 12L), new RhiBlendStateService.VertexAttribute(2, 2, RhiBlendStateService.VertexFormat.FLOAT, 24L))).blend(RhiBlendStateService.BlendState.ALPHA).depthStencil(new RhiBlendStateService.DepthStencilState(true, true, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false)).rasterizer(RhiBlendStateService.RasterizerState.NO_CULL).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES).build());
        this.fh0syswn4dtv = this.f43tdql6am6m.createBuffer(18432L, RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX), RhiBlendStateService.BufferAccess.STREAM);
    }

    private void mjnrwjpgjb4z(float[] fArray, RhiBlendStateService.TextureHandle textureHandle) {
        if (!textureHandle.valid()) {
            return;
        }
        this.f43tdql6am6m.updateBuffer(this.fh0syswn4dtv, 0L, fArray);
        this.fer2y5srl5ly.bindVertexBuffer(this.fh0syswn4dtv, 0);
        this.fer2y5srl5ly.bindTexture(textureHandle, 0);
        this.fer2y5srl5ly.draw(fArray.length / 8, 1, 0);
    }

    private void mi98hnwuvzoe(int n, int n2) {
        if (this.fd9xvsrmil49 == n && this.f696ct7oo2rm == n2 && this.fj9plig0g6st.valid()) {
            return;
        }
        this.mcpx97aeyl2s();
        this.fd9xvsrmil49 = n;
        this.f696ct7oo2rm = n2;
        this.f14t7sidb4lq = this.f43tdql6am6m.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(n, n2, RhiBlendStateService.TextureFormat.RGBA8));
        this.f3m5hh98zbkp = this.f43tdql6am6m.createTexture(RhiBlendStateService.TextureDescriptor.renderTarget(n, n2, RhiBlendStateService.TextureFormat.DEPTH32F));
        this.fj9plig0g6st = this.f43tdql6am6m.createFramebuffer(this.f14t7sidb4lq, this.f3m5hh98zbkp);
    }

    


    private RhiBlendStateService.TextureHandle mfwcn076ye07(RhiBlendStateService.TextureHandle textureHandle, BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return textureHandle;
        }
        ByteBuffer byteBuffer = MemoryUtil.memAlloc((int)(bufferedImage.getWidth() * bufferedImage.getHeight() * 4));
        try {
            for (int i = 0; i < bufferedImage.getHeight(); ++i) {
                for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                    int n = bufferedImage.getRGB(j, i);
                    byteBuffer.put((byte)(n >> 16)).put((byte)(n >> 8)).put((byte)n).put((byte)(n >> 24));
                }
            }
            byteBuffer.flip();
            if (textureHandle.valid()) {
                this.f43tdql6am6m.updateTexture(textureHandle, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), byteBuffer);
                RhiBlendStateService.TextureHandle textureHandle2 = textureHandle;
                return textureHandle2;
            }
            RhiBlendStateService.TextureHandle textureHandle3 = this.f43tdql6am6m.createTexture(new RhiBlendStateService.TextureDescriptor(bufferedImage.getWidth(), bufferedImage.getHeight(), RhiBlendStateService.TextureFormat.RGBA8, RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.AddressMode.CLAMP), byteBuffer);
            return textureHandle3;
        }
        finally {
            MemoryUtil.memFree((ByteBuffer)byteBuffer);
        }
    }

    private void mcpx97aeyl2s() {
        if (this.fj9plig0g6st.valid()) {
            this.f43tdql6am6m.destroyFramebuffer(this.fj9plig0g6st);
        }
        if (this.f14t7sidb4lq.valid()) {
            this.f43tdql6am6m.destroyTexture(this.f14t7sidb4lq);
        }
        if (this.f3m5hh98zbkp.valid()) {
            this.f43tdql6am6m.destroyTexture(this.f3m5hh98zbkp);
        }
        this.fj9plig0g6st = RhiBlendStateService.FramebufferHandle.NONE;
        this.f14t7sidb4lq = this.f3m5hh98zbkp = RhiBlendStateService.TextureHandle.NONE;
    }

    @Override
    public void close() {
        if (this.f43tdql6am6m == null) {
            return;
        }
        this.mcpx97aeyl2s();
        if (this.f2qdeu1l4gum.valid()) {
            this.f43tdql6am6m.destroyTexture(this.f2qdeu1l4gum);
        }
        if (this.f3ipby4ak0nk.valid()) {
            this.f43tdql6am6m.destroyTexture(this.f3ipby4ak0nk);
        }
        if (this.fh0syswn4dtv.valid()) {
            this.f43tdql6am6m.destroyBuffer(this.fh0syswn4dtv);
        }
        if (this.f4epxtpzgty2.valid()) {
            this.f43tdql6am6m.destroyPipeline(this.f4epxtpzgty2);
        }
        if (this.fjepsae8jmjj.valid()) {
            this.f43tdql6am6m.destroyShader(this.fjepsae8jmjj);
        }
        if (this.fv70saumqw2.valid()) {
            this.f43tdql6am6m.destroyShader(this.fv70saumqw2);
        }
        this.f2qdeu1l4gum = this.f3ipby4ak0nk = RhiBlendStateService.TextureHandle.NONE;
        this.fh0syswn4dtv = RhiBlendStateService.BufferHandle.NONE;
        this.f4epxtpzgty2 = RhiBlendStateService.PipelineHandle.NONE;
        this.fjepsae8jmjj = this.fv70saumqw2 = RhiBlendStateService.ShaderHandle.NONE;
        this.fjaxzfhz25nq = null;
        this.fgilbr5r1wjs = null;
        this.f43tdql6am6m = null;
    }
}

