















package dev.felix.ellice.render.rhi.gl;

import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.gl.GlShaderFactory;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryStack;

public class GlRenderer
implements RhiCommandBuffer {
    private final GlShaderFactory fbn6szuvhz4i;
    private RhiBlendStateService.PipelineHandle fgwppd7bvwdo = RhiBlendStateService.PipelineHandle.NONE;
    private int f415hublcv70;
    private int f43g87viav74;
    private RhiBlendStateService.IndexType f25dux3jku1 = RhiBlendStateService.IndexType.UINT32;
    private final Map<Integer, Map<String, Uniform>> f4dnufo1wxq8 = new HashMap<Integer, Map<String, Uniform>>();
    private Map<String, Uniform> f3811babw0cm;
    private final Map<Integer, Integer> fdngku0p22mg = new HashMap<Integer, Integer>();
    private static final int fcyy67i0r2vj = 16;
    private static final int fjbclt1d5i18 = RhiBlendStateService.FramebufferAspect.COLOR.bit | RhiBlendStateService.FramebufferAspect.DEPTH.bit | RhiBlendStateService.FramebufferAspect.STENCIL.bit;
    private static final int f4abcs4u6vff = RhiBlendStateService.FramebufferAspect.DEPTH.bit | RhiBlendStateService.FramebufferAspect.STENCIL.bit;
    private final int[] fi29mhhcmx7g = new int[16];
    private final int[] f6m8ahjcwfrq = new int[16];
    private final Map<Integer, Integer> ffke0xqh5rhk = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> fdvxvihqybac = new HashMap<Integer, Integer>();
    private final ArrayList<GLState> fhngyse51ony = new ArrayList();

    public void invalidateUniformCache(int n) {
        this.f4dnufo1wxq8.remove(n);
        if (this.f415hublcv70 == n) {
            this.f415hublcv70 = 0;
            this.f3811babw0cm = null;
        }
    }

    public void invalidateVaoCache(int n) {
        this.fdngku0p22mg.remove(n);
        if (this.f43g87viav74 == n) {
            this.f43g87viav74 = 0;
        }
    }

    public void invalidateBufferCache(int n) {
        this.fdngku0p22mg.entrySet().removeIf(entry -> (Integer)entry.getValue() == n);
        this.ffke0xqh5rhk.entrySet().removeIf(entry -> (Integer)entry.getValue() == n);
        this.fdvxvihqybac.entrySet().removeIf(entry -> (Integer)entry.getValue() == n);
    }

    public void invalidateTextureCache(int n) {
        for (int i = 0; i < this.fi29mhhcmx7g.length; ++i) {
            if (this.fi29mhhcmx7g[i] != n) continue;
            this.fi29mhhcmx7g[i] = -1;
        }
    }

    public void invalidateSamplerCache(int n) {
        for (int i = 0; i < this.f6m8ahjcwfrq.length; ++i) {
            if (this.f6m8ahjcwfrq[i] != n) continue;
            this.f6m8ahjcwfrq[i] = -1;
        }
    }

    private void m2dq4f0n9krl() {
        Arrays.fill(this.fi29mhhcmx7g, -1);
        Arrays.fill(this.f6m8ahjcwfrq, -1);
        this.ffke0xqh5rhk.clear();
        this.fdvxvihqybac.clear();
    }

    @Override
    public void invalidateBindCache() {
        this.m2dq4f0n9krl();
    }

    GlRenderer(GlShaderFactory glShaderFactory) {
        this.fbn6szuvhz4i = glShaderFactory;
        this.m2dq4f0n9krl();
    }

    public void saveGLState() {
        boolean bl;
        boolean bl2;
        boolean bl3;
        boolean bl4;
        int n;
        int n2 = GL11.glGetInteger((int)34016);
        int n3 = Math.min(16, GL11.glGetInteger((int)35661));
        int[] nArray = new int[n3];
        int[] nArray2 = new int[n3];
        for (int i = 0; i < n3; ++i) {
            GL13.glActiveTexture((int)(33984 + i));
            nArray[i] = GL11.glGetInteger((int)32873);
            nArray2[i] = GL11.glGetInteger((int)35097);
        }
        GL13.glActiveTexture((int)n2);
        int[] nArray3 = new int[4];
        GL11.glGetIntegerv((int)2978, (int[])nArray3);
        int[] nArray4 = new int[4];
        GL11.glGetIntegerv((int)3088, (int[])nArray4);
        int[] nArray5 = new int[2];
        GL11.glGetIntegerv((int)2880, (int[])nArray5);
        int n4 = GL11.glGetInteger((int)34852);
        int[] nArray6 = new int[Math.max(1, n4)];
        for (n = 0; n < nArray6.length; ++n) {
            nArray6[n] = GL11.glGetInteger((int)(34853 + n));
        }
        for (n = nArray6.length; n > 1 && nArray6[n - 1] == 0; n += -1) {
        }
        if (n != nArray6.length) {
            nArray6 = Arrays.copyOf(nArray6, n);
        }
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            ByteBuffer byteBuffer = memoryStack.malloc(4);
            GL11.glGetBooleanv((int)3107, (ByteBuffer)byteBuffer);
            bl4 = byteBuffer.get(0) != 0;
            bl3 = byteBuffer.get(1) != 0;
            bl2 = byteBuffer.get(2) != 0;
            bl = byteBuffer.get(3) != 0;
        }
        this.fhngyse51ony.add(new GLState(GL11.glGetInteger((int)35725), GL11.glGetInteger((int)34229), GL11.glGetInteger((int)34964), GL11.glGetInteger((int)35055), GL11.glGetInteger((int)36010), GL11.glGetInteger((int)36006), GL11.glGetInteger((int)3074), nArray6, n2, nArray, nArray2, GL11.glGetInteger((int)3317), GL11.glGetInteger((int)3314), GL11.glGetInteger((int)3315), GL11.glGetInteger((int)3316), GL11.glGetInteger((int)3041), GL11.glGetInteger((int)3040), GL11.glGetInteger((int)32971), GL11.glGetInteger((int)32970), GL11.glGetInteger((int)32777), GL11.glGetInteger((int)34877), GL11.glIsEnabled((int)3042), GL11.glIsEnabled((int)2929), GL11.glIsEnabled((int)2884), GL11.glIsEnabled((int)2960), GL11.glIsEnabled((int)3089), GL11.glGetBoolean((int)2930), bl4, bl3, bl2, bl, GL11.glGetInteger((int)2932), GL11.glGetInteger((int)2885), GL11.glGetInteger((int)2886), nArray3, nArray4, nArray5));
        this.fgwppd7bvwdo = RhiBlendStateService.PipelineHandle.NONE;
        this.f415hublcv70 = 0;
        this.f3811babw0cm = null;
        this.f43g87viav74 = 0;
        this.m2dq4f0n9krl();
    }

    public void restoreGLState() {
        if (this.fhngyse51ony.isEmpty()) {
            return;
        }
        GLState gLState = this.fhngyse51ony.remove(this.fhngyse51ony.size() - 1);
        GL20.glUseProgram((int)gLState.program);
        GL30.glBindVertexArray((int)gLState.vao);
        GL15.glBindBuffer((int)34962, (int)gLState.arrayBuffer);
        GL15.glBindBuffer((int)35052, (int)gLState.pixelUnpackBuffer);
        GL30.glBindFramebuffer((int)36008, (int)gLState.readFbo);
        GL30.glBindFramebuffer((int)36009, (int)gLState.drawFbo);
        GL11.glReadBuffer((int)gLState.readBuffer);
        if (gLState.drawFbo == 0) {
            GL11.glDrawBuffer((int)gLState.drawBuffers[0]);
        } else {
            GL20.glDrawBuffers((int[])gLState.drawBuffers);
        }
        GL11.glViewport((int)gLState.viewport[0], (int)gLState.viewport[1], (int)gLState.viewport[2], (int)gLState.viewport[3]);
        GL11.glScissor((int)gLState.scissorBox[0], (int)gLState.scissorBox[1], (int)gLState.scissorBox[2], (int)gLState.scissorBox[3]);
        for (int i = 0; i < gLState.textureBindings.length; ++i) {
            GL13.glActiveTexture((int)(33984 + i));
            GL11.glBindTexture((int)3553, (int)gLState.textureBindings[i]);
            GL33.glBindSampler((int)i, (int)gLState.samplerBindings[i]);
        }
        GL13.glActiveTexture((int)gLState.activeTexture);
        GL11.glPixelStorei((int)3317, (int)gLState.unpackAlignment);
        GL11.glPixelStorei((int)3314, (int)gLState.unpackRowLength);
        GL11.glPixelStorei((int)3315, (int)gLState.unpackSkipRows);
        GL11.glPixelStorei((int)3316, (int)gLState.unpackSkipPixels);
        GlRenderer.m98ovos69nb4(3042, gLState.blend);
        GL14.glBlendFuncSeparate((int)gLState.blendSrc, (int)gLState.blendDst, (int)gLState.blendSrcA, (int)gLState.blendDstA);
        GL20.glBlendEquationSeparate((int)gLState.blendEqRGB, (int)gLState.blendEqAlpha);
        GlRenderer.m98ovos69nb4(2929, gLState.depth);
        GL11.glDepthFunc((int)gLState.depthFunc);
        GL11.glDepthMask((boolean)gLState.depthMask);
        GL11.glColorMask((boolean)gLState.colorMaskR, (boolean)gLState.colorMaskG, (boolean)gLState.colorMaskB, (boolean)gLState.colorMaskA);
        GlRenderer.m98ovos69nb4(2884, gLState.cull);
        GL11.glCullFace((int)gLState.cullFace);
        GL11.glFrontFace((int)gLState.frontFace);
        GL11.glPolygonMode((int)1032, (int)gLState.polygonMode[0]);
        GlRenderer.m98ovos69nb4(2960, gLState.stencil);
        GlRenderer.m98ovos69nb4(3089, gLState.scissor);
        this.fgwppd7bvwdo = RhiBlendStateService.PipelineHandle.NONE;
        this.f415hublcv70 = 0;
        this.f3811babw0cm = null;
        this.f43g87viav74 = 0;
        this.m2dq4f0n9krl();
    }

    @Override
    public void beginRenderPass(RhiBlendStateService.FramebufferHandle framebufferHandle, float f, float f2, float f3, float f4) {
        GL30.glBindFramebuffer((int)36160, (int)framebufferHandle.id());
        this.mb4a1skr5lko(framebufferHandle);
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            FloatBuffer floatBuffer = memoryStack.floats(f, f2, f3, f4);
            GL30.glClearBufferfv((int)6144, (int)0, (FloatBuffer)floatBuffer);
        }
    }

    @Override
    public void beginRenderPass(RhiBlendStateService.FramebufferHandle framebufferHandle) {
        GL30.glBindFramebuffer((int)36160, (int)framebufferHandle.id());
        this.mb4a1skr5lko(framebufferHandle);
    }

    


    @Override
    public void clearDepth(float f) {
        boolean bl = GL11.glGetBoolean((int)2930);
        if (!bl) {
            GL11.glDepthMask((boolean)true);
        }
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            GL30.glClearBufferfv((int)6145, (int)0, (FloatBuffer)memoryStack.floats(f));
        }
        finally {
            if (!bl) {
                GL11.glDepthMask((boolean)false);
            }
        }
    }

    @Override
    public void endRenderPass() {
    }

    @Override
    public void bindPipeline(RhiBlendStateService.PipelineHandle pipelineHandle) {
        if (pipelineHandle.equals(this.fgwppd7bvwdo)) {
            return;
        }
        this.fgwppd7bvwdo = pipelineHandle;
        GlShaderFactory.PipelineState pipelineState = this.fbn6szuvhz4i.getPipeline(pipelineHandle.id());
        if (pipelineState.program() != this.f415hublcv70) {
            GL20.glUseProgram((int)pipelineState.program());
            this.f415hublcv70 = pipelineState.program();
            this.f3811babw0cm = null;
        }
        if (pipelineState.vao() != 0 && pipelineState.vao() != this.f43g87viav74) {
            GL30.glBindVertexArray((int)pipelineState.vao());
            this.f43g87viav74 = pipelineState.vao();
        }
        this.mnbe52lygeq(pipelineState.blend());
        this.m4uq1drixgja(pipelineState.depthStencil());
        this.m18nqoa0c5wj(pipelineState.rasterizer());
    }

    @Override
    public void bindVertexBuffer(RhiBlendStateService.BufferHandle bufferHandle, int n) {
        Integer n2;
        GlShaderFactory.PipelineState pipelineState = this.fbn6szuvhz4i.getPipeline(this.fgwppd7bvwdo.id());
        int n3 = pipelineState.vao();
        if (n3 != 0 && n3 != this.f43g87viav74) {
            GL30.glBindVertexArray((int)n3);
            this.f43g87viav74 = n3;
        }
        Integer n4 = n2 = n3 != 0 ? this.fdngku0p22mg.get(n3) : null;
        if (n2 != null && n2.intValue() == bufferHandle.id()) {
            return;
        }
        GL15.glBindBuffer((int)34962, (int)bufferHandle.id());
        if (pipelineState.layout() != null) {
            for (RhiBlendStateService.VertexAttribute vertexAttribute : pipelineState.layout().attributes()) {
                if (vertexAttribute.format() == RhiBlendStateService.VertexFormat.INT) {
                    GL30.glVertexAttribIPointer((int)vertexAttribute.location(), (int)vertexAttribute.components(), (int)GlShaderFactory.glVertexAttribType(vertexAttribute.format()), (int)pipelineState.layout().stride(), (long)vertexAttribute.offset());
                    continue;
                }
                GL20.glVertexAttribPointer((int)vertexAttribute.location(), (int)vertexAttribute.components(), (int)GlShaderFactory.glVertexAttribType(vertexAttribute.format()), (boolean)false, (int)pipelineState.layout().stride(), (long)vertexAttribute.offset());
            }
            if (n3 != 0) {
                this.fdngku0p22mg.put(n3, bufferHandle.id());
            }
        }
    }

    @Override
    public void bindIndexBuffer(RhiBlendStateService.BufferHandle bufferHandle, RhiBlendStateService.IndexType indexType) {
        this.f25dux3jku1 = indexType;
        GL15.glBindBuffer((int)34963, (int)bufferHandle.id());
    }

    @Override
    public void bindUniformBuffer(RhiBlendStateService.BufferHandle bufferHandle, int n) {
        int n2 = bufferHandle.id();
        Integer n3 = this.ffke0xqh5rhk.get(n);
        if (n3 != null && n3 == n2) {
            return;
        }
        GL30.glBindBufferBase((int)35345, (int)n, (int)n2);
        this.ffke0xqh5rhk.put(n, n2);
    }

    @Override
    public void bindStorageBuffer(RhiBlendStateService.BufferHandle bufferHandle, int n) {
        int n2 = bufferHandle.id();
        Integer n3 = this.fdvxvihqybac.get(n);
        if (n3 != null && n3 == n2) {
            return;
        }
        GL30.glBindBufferBase((int)37074, (int)n, (int)n2);
        this.fdvxvihqybac.put(n, n2);
    }

    @Override
    public void bindTexture(RhiBlendStateService.TextureHandle textureHandle, int n) {
        this.mhh1c2rh91nk(textureHandle != null ? textureHandle.id() : 0, 0, n);
    }

    @Override
    public void bindTexture(RhiBlendStateService.TextureHandle textureHandle, RhiBlendStateService.SamplerHandle samplerHandle, int n) {
        this.mhh1c2rh91nk(textureHandle != null ? textureHandle.id() : 0, samplerHandle != null ? samplerHandle.id() : 0, n);
    }

    private void mhh1c2rh91nk(int n, int n2, int n3) {
        int n4;
        int n5 = n4 = n3 >= 0 && n3 < this.fi29mhhcmx7g.length ? 1 : 0;
        if (n4 != 0 && this.fi29mhhcmx7g[n3] == n && this.f6m8ahjcwfrq[n3] == n2) {
            return;
        }
        GL13.glActiveTexture((int)(33984 + n3));
        GL33.glBindSampler((int)n3, (int)n2);
        GL11.glBindTexture((int)3553, (int)n);
        if (n4 != 0) {
            this.fi29mhhcmx7g[n3] = n;
            this.f6m8ahjcwfrq[n3] = n2;
        }
    }

    @Override
    public void setViewport(int n, int n2, int n3, int n4) {
        GL11.glViewport((int)n, (int)n2, (int)n3, (int)n4);
    }

    @Override
    public void setScissor(int n, int n2, int n3, int n4) {
        GL11.glEnable((int)3089);
        GL11.glScissor((int)n, (int)n2, (int)n3, (int)n4);
    }

    @Override
    public void setColorWriteMask(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        GL11.glColorMask((boolean)bl, (boolean)bl2, (boolean)bl3, (boolean)bl4);
    }

    @Override
    public void clearScissor() {
        GL11.glDisable((int)3089);
    }

    private Uniform me5jze8beq16(String string) {
        Uniform uniform;
        Map<String, Uniform> map = this.f3811babw0cm;
        if (map == null) {
            this.f3811babw0cm = map = this.f4dnufo1wxq8.computeIfAbsent(this.f415hublcv70, n -> new HashMap());
        }
        if ((uniform = map.get(string)) == null) {
            uniform = new Uniform(GL20.glGetUniformLocation((int)this.f415hublcv70, (CharSequence)string));
            map.put(string, uniform);
        }
        return uniform;
    }

    @Override
    public void pushInt(String string, int n) {
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        if (uniform.count == 1 && uniform.bits[0] == n) {
            return;
        }
        uniform.bits[0] = n;
        uniform.count = 1;
        GL20.glUniform1i((int)uniform.location, (int)n);
    }

    @Override
    public void pushFloat(String string, float f) {
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        int n = Float.floatToRawIntBits(f);
        if (uniform.count == 1 && uniform.bits[0] == n) {
            return;
        }
        uniform.bits[0] = n;
        uniform.count = 1;
        GL20.glUniform1f((int)uniform.location, (float)f);
    }

    @Override
    public void pushVec2(String string, float f, float f2) {
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        int n = Float.floatToRawIntBits(f);
        int n2 = Float.floatToRawIntBits(f2);
        if (uniform.count == 2 && uniform.bits[0] == n && uniform.bits[1] == n2) {
            return;
        }
        uniform.bits[0] = n;
        uniform.bits[1] = n2;
        uniform.count = 2;
        GL20.glUniform2f((int)uniform.location, (float)f, (float)f2);
    }

    @Override
    public void pushVec3(String string, float f, float f2, float f3) {
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        int n = Float.floatToRawIntBits(f);
        int n2 = Float.floatToRawIntBits(f2);
        int n3 = Float.floatToRawIntBits(f3);
        if (uniform.count == 3 && uniform.bits[0] == n && uniform.bits[1] == n2 && uniform.bits[2] == n3) {
            return;
        }
        uniform.bits[0] = n;
        uniform.bits[1] = n2;
        uniform.bits[2] = n3;
        uniform.count = 3;
        GL20.glUniform3f((int)uniform.location, (float)f, (float)f2, (float)f3);
    }

    @Override
    public void pushVec4(String string, float f, float f2, float f3, float f4) {
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        int n = Float.floatToRawIntBits(f);
        int n2 = Float.floatToRawIntBits(f2);
        int n3 = Float.floatToRawIntBits(f3);
        int n4 = Float.floatToRawIntBits(f4);
        if (uniform.count == 4 && uniform.bits[0] == n && uniform.bits[1] == n2 && uniform.bits[2] == n3 && uniform.bits[3] == n4) {
            return;
        }
        uniform.bits[0] = n;
        uniform.bits[1] = n2;
        uniform.bits[2] = n3;
        uniform.bits[3] = n4;
        uniform.count = 4;
        GL20.glUniform4f((int)uniform.location, (float)f, (float)f2, (float)f3, (float)f4);
    }

    @Override
    public void pushMatrix4(String string, FloatBuffer floatBuffer) {
        int n;
        int n2;
        Uniform uniform = this.me5jze8beq16(string);
        if (uniform.location < 0) {
            return;
        }
        int n3 = floatBuffer.position();
        int n4 = n2 = uniform.count == 16 ? 1 : 0;
        if (n2 != 0) {
            for (n = 0; n < 16; ++n) {
                if (uniform.bits[n] == Float.floatToRawIntBits(floatBuffer.get(n3 + n))) continue;
                n2 = 0;
                break;
            }
        }
        if (n2 != 0) {
            return;
        }
        for (n = 0; n < 16; ++n) {
            uniform.bits[n] = Float.floatToRawIntBits(floatBuffer.get(n3 + n));
        }
        uniform.count = 16;
        GL20.glUniformMatrix4fv((int)uniform.location, (boolean)false, (FloatBuffer)floatBuffer);
    }

    @Override
    public void draw(int n, int n2, int n3) {
        int n4 = GlShaderFactory.glTopology(this.fbn6szuvhz4i.getPipeline(this.fgwppd7bvwdo.id()).topology());
        if (n2 <= 1) {
            GL11.glDrawArrays((int)n4, (int)n3, (int)n);
        } else {
            GL31.glDrawArraysInstanced((int)n4, (int)n3, (int)n, (int)n2);
        }
    }

    @Override
    public void drawIndexed(int n, int n2, int n3) {
        int n4 = GlShaderFactory.glTopology(this.fbn6szuvhz4i.getPipeline(this.fgwppd7bvwdo.id()).topology());
        int n5 = this.f25dux3jku1 == RhiBlendStateService.IndexType.UINT16 ? 5123 : 5125;
        long l = (long)n3 * (long)(this.f25dux3jku1 == RhiBlendStateService.IndexType.UINT16 ? 2 : 4);
        if (n2 <= 1) {
            GL11.glDrawElements((int)n4, (int)n, (int)n5, (long)l);
        } else {
            GL31.glDrawElementsInstanced((int)n4, (int)n, (int)n5, (long)l, (int)n2);
        }
    }

    @Override
    public void dispatch(int n, int n2, int n3) {
        GL43.glDispatchCompute((int)n, (int)n2, (int)n3);
        GL42.glMemoryBarrier((int)-1);
    }

    @Override
    public void blitFramebuffer(RhiBlendStateService.FramebufferHandle framebufferHandle, RhiBlendStateService.FramebufferHandle framebufferHandle2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, RhiBlendStateService.FilterMode filterMode, int n9) {
        if (n9 == 0 || (n9 & ~fjbclt1d5i18) != 0) {
            throw new IllegalArgumentException("Invalid framebuffer aspect flags: " + n9);
        }
        int n10 = 0;
        if ((n9 & RhiBlendStateService.FramebufferAspect.COLOR.bit) != 0) {
            n10 |= 0x4000;
        }
        if ((n9 & RhiBlendStateService.FramebufferAspect.DEPTH.bit) != 0) {
            n10 |= 0x100;
        }
        if ((n9 & RhiBlendStateService.FramebufferAspect.STENCIL.bit) != 0) {
            n10 |= 0x400;
        }
        GL30.glBindFramebuffer((int)36008, (int)framebufferHandle.id());
        GL30.glBindFramebuffer((int)36009, (int)framebufferHandle2.id());
        if ((n9 & RhiBlendStateService.FramebufferAspect.COLOR.bit) != 0) {
            this.mdq76gt23q1m(framebufferHandle);
            this.m4p7f8enbi10(framebufferHandle2);
        }
        int n11 = (n9 & f4abcs4u6vff) != 0 ? 1 : 0;
        GL30.glBlitFramebuffer((int)n, (int)n2, (int)n3, (int)n4, (int)n5, (int)n6, (int)n7, (int)n8, (int)n10, (int)(n11 == 0 && filterMode == RhiBlendStateService.FilterMode.LINEAR ? 9729 : 9728));
    }

    private void mb4a1skr5lko(RhiBlendStateService.FramebufferHandle framebufferHandle) {
        if (framebufferHandle.id() == 0) {
            GL11.glDrawBuffer((int)1029);
            return;
        }
        int n = this.fbn6szuvhz4i.getFramebufferColorAttachmentCount(framebufferHandle.id());
        if (n < 0) {
            GL11.glDrawBuffer((int)36064);
        } else if (n == 0) {
            GL11.glDrawBuffer((int)0);
        } else if (n == 1) {
            GL11.glDrawBuffer((int)36064);
        } else {
            try (MemoryStack memoryStack = MemoryStack.stackPush();){
                IntBuffer intBuffer = memoryStack.mallocInt(n);
                for (int i = 0; i < n; ++i) {
                    intBuffer.put(36064 + i);
                }
                intBuffer.flip();
                GL20.glDrawBuffers((IntBuffer)intBuffer);
            }
        }
    }

    private void mdq76gt23q1m(RhiBlendStateService.FramebufferHandle framebufferHandle) {
        int n = this.fbn6szuvhz4i.getFramebufferColorAttachmentCount(framebufferHandle.id());
        GL11.glReadBuffer((int)(framebufferHandle.id() == 0 ? 1029 : (n == 0 ? 0 : 36064)));
    }

    private void m4p7f8enbi10(RhiBlendStateService.FramebufferHandle framebufferHandle) {
        int n = this.fbn6szuvhz4i.getFramebufferColorAttachmentCount(framebufferHandle.id());
        GL11.glDrawBuffer((int)(framebufferHandle.id() == 0 ? 1029 : (n == 0 ? 0 : 36064)));
    }

    private void mnbe52lygeq(RhiBlendStateService.BlendState blendState) {
        if (!blendState.enabled()) {
            GL11.glDisable((int)3042);
            return;
        }
        GL11.glEnable((int)3042);
        GL14.glBlendFuncSeparate((int)GlShaderFactory.glBlendFactor(blendState.srcColor()), (int)GlShaderFactory.glBlendFactor(blendState.dstColor()), (int)GlShaderFactory.glBlendFactor(blendState.srcAlpha()), (int)GlShaderFactory.glBlendFactor(blendState.dstAlpha()));
        GL20.glBlendEquationSeparate((int)GlShaderFactory.glBlendOp(blendState.colorOp()), (int)GlShaderFactory.glBlendOp(blendState.alphaOp()));
    }

    private void m4uq1drixgja(RhiBlendStateService.DepthStencilState depthStencilState) {
        if (depthStencilState.depthTest()) {
            GL11.glEnable((int)2929);
            GL11.glDepthFunc((int)GlShaderFactory.glCompareOp(depthStencilState.depthCompare()));
        } else {
            GL11.glDisable((int)2929);
        }
        GL11.glDepthMask((boolean)depthStencilState.depthWrite());
        GlRenderer.m98ovos69nb4(2960, depthStencilState.stencilTest());
    }

    private void m18nqoa0c5wj(RhiBlendStateService.RasterizerState rasterizerState) {
        if (rasterizerState.cullMode() == RhiBlendStateService.CullMode.NONE) {
            GL11.glDisable((int)2884);
        } else {
            GL11.glEnable((int)2884);
            GL11.glCullFace((int)(rasterizerState.cullMode() == RhiBlendStateService.CullMode.FRONT ? 1028 : 1029));
        }
        GL11.glFrontFace((int)(rasterizerState.frontFace() == RhiBlendStateService.FrontFace.CCW ? 2305 : 2304));
        GL11.glPolygonMode((int)1032, (int)(rasterizerState.wireframe() ? 6913 : 6914));
    }

    private static void m98ovos69nb4(int n, boolean bl) {
        if (bl) {
            GL11.glEnable((int)n);
        } else {
            GL11.glDisable((int)n);
        }
    }

    private record GLState(int program, int vao, int arrayBuffer, int pixelUnpackBuffer, int readFbo, int drawFbo, int readBuffer, int[] drawBuffers, int activeTexture, int[] textureBindings, int[] samplerBindings, int unpackAlignment, int unpackRowLength, int unpackSkipRows, int unpackSkipPixels, int blendSrc, int blendDst, int blendSrcA, int blendDstA, int blendEqRGB, int blendEqAlpha, boolean blend, boolean depth, boolean cull, boolean stencil, boolean scissor, boolean depthMask, boolean colorMaskR, boolean colorMaskG, boolean colorMaskB, boolean colorMaskA, int depthFunc, int cullFace, int frontFace, int[] viewport, int[] scissorBox, int[] polygonMode) {
    }

    private static final class Uniform {
        final int location;
        final int[] bits = new int[16];
        int count = 0;

        Uniform(int n) {
            this.location = n;
        }
    }
}
