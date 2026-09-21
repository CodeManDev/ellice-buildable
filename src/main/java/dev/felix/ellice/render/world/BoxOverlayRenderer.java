







package dev.felix.ellice.render.world;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiBuilderData;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.RhiRepository;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

public final class BoxOverlayRenderer {
    private static final int fiw3ezz8xw90 = 3;
    private static final int flc1c1j7mr3 = 12;
    private RhiOperationHandler f95v3muymmyr;
    private GlRenderer f4p8akcapzfc;
    private RhiBlendStateService.ShaderHandle f4q5osub5ls1 = RhiBlendStateService.ShaderHandle.NONE;
    private RhiBlendStateService.ShaderHandle fz58j51n7t0 = RhiBlendStateService.ShaderHandle.NONE;
    private RhiBlendStateService.PipelineHandle f7s23vxs07iw = RhiBlendStateService.PipelineHandle.NONE;
    private RhiBlendStateService.PipelineHandle f9473ma2iudo = RhiBlendStateService.PipelineHandle.NONE;
    private RhiBlendStateService.BufferHandle f9phum6qo66s = RhiBlendStateService.BufferHandle.NONE;
    private int fbnfzfijo0fe;

    public void render(List<AABB> list, EventAttackInputService.WorldRender worldRender, int n, int n2, Style style) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (n <= 0 || n2 <= 0) {
            return;
        }
        if (!style.fill() && !style.outline()) {
            return;
        }
        try {
            this.m1eudsb2rg3w();
            float[] fArray = style.fill() ? this.m7w48ia2o6ns(list) : new float[]{};
            float[] fArray2 = style.outline() ? this.m5siyjlf54om(list, Math.max(Float.intBitsToFloat(973279855), style.edgeWidth())) : new float[]{};
            int n3 = fArray.length / 3;
            int n4 = fArray2.length / 3;
            int n5 = fArray.length + fArray2.length;
            if (n5 == 0) {
                return;
            }
            this.mid1qozhrnv6(n5);
            if (fArray.length > 0) {
                this.f95v3muymmyr.updateBuffer(this.f9phum6qo66s, 0L, fArray);
            }
            if (fArray2.length > 0) {
                this.f95v3muymmyr.updateBuffer(this.f9phum6qo66s, (long)fArray.length * 4L, fArray2);
            }
            Vec3 vec3 = worldRender.cameraPos();
            this.f4p8akcapzfc.saveGLState();
            this.f4p8akcapzfc.clearScissor();
            this.f4p8akcapzfc.setViewport(0, 0, n, n2);
            this.f4p8akcapzfc.bindPipeline(style.seeThrough() ? this.f9473ma2iudo : this.f7s23vxs07iw);
            this.f4p8akcapzfc.bindVertexBuffer(this.f9phum6qo66s, 0);
            this.mfa4o903o1g6(worldRender, vec3);
            if (n3 > 0) {
                this.mg6nkfkdpkwm(style.fillColor());
                this.f4p8akcapzfc.draw(n3, 1, 0);
            }
            if (n4 > 0) {
                this.mg6nkfkdpkwm(style.outlineColor());
                this.f4p8akcapzfc.draw(n4, 1, n3);
            }
            this.f4p8akcapzfc.restoreGLState();
        }
        catch (Exception exception) {
            CoreIsInitializedHandler.LOGGER.error("Block overlay render failed", (Throwable)exception);
            try {
                if (this.f4p8akcapzfc != null) {
                    this.f4p8akcapzfc.restoreGLState();
                }
            }
            catch (Exception exception2) {
                
            }
        }
    }

    public void shutdown() {
        if (this.f95v3muymmyr == null) {
            return;
        }
        if (this.f9phum6qo66s.valid()) {
            this.f95v3muymmyr.destroyBuffer(this.f9phum6qo66s);
        }
        if (this.f7s23vxs07iw.valid()) {
            this.f95v3muymmyr.destroyPipeline(this.f7s23vxs07iw);
        }
        if (this.f9473ma2iudo.valid()) {
            this.f95v3muymmyr.destroyPipeline(this.f9473ma2iudo);
        }
        if (this.f4q5osub5ls1.valid()) {
            this.f95v3muymmyr.destroyShader(this.f4q5osub5ls1);
        }
        if (this.fz58j51n7t0.valid()) {
            this.f95v3muymmyr.destroyShader(this.fz58j51n7t0);
        }
        this.f9phum6qo66s = RhiBlendStateService.BufferHandle.NONE;
        this.f7s23vxs07iw = RhiBlendStateService.PipelineHandle.NONE;
        this.f9473ma2iudo = RhiBlendStateService.PipelineHandle.NONE;
        this.f4q5osub5ls1 = RhiBlendStateService.ShaderHandle.NONE;
        this.fz58j51n7t0 = RhiBlendStateService.ShaderHandle.NONE;
        this.fbnfzfijo0fe = 0;
        this.f95v3muymmyr = null;
        this.f4p8akcapzfc = null;
    }

    private void m1eudsb2rg3w() {
        if (this.f95v3muymmyr != null) {
            return;
        }
        this.f95v3muymmyr = RhiDeviceService.device();
        this.f4p8akcapzfc = (GlRenderer)this.f95v3muymmyr.encoder();
        this.f4q5osub5ls1 = this.f95v3muymmyr.createShader(RhiBlendStateService.ShaderStage.VERTEX, RhiRepository.vertex("block_overlay"));
        this.fz58j51n7t0 = this.f95v3muymmyr.createShader(RhiBlendStateService.ShaderStage.FRAGMENT, RhiRepository.fragment("block_overlay"));
        RhiBlendStateService.VertexLayout vertexLayout = RhiBlendStateService.VertexLayout.of(12, new RhiBlendStateService.VertexAttribute(0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L));
        RhiBlendStateService.DepthStencilState depthStencilState = new RhiBlendStateService.DepthStencilState(true, false, RhiBlendStateService.CompareOp.LESS_OR_EQUAL, false);
        this.f7s23vxs07iw = this.f95v3muymmyr.createPipeline(RhiBuilderData.graphics().vertex(this.f4q5osub5ls1).fragment(this.fz58j51n7t0).vertexLayout(vertexLayout).blend(RhiBlendStateService.BlendState.ALPHA).depthStencil(depthStencilState).rasterizer(RhiBlendStateService.RasterizerState.NO_CULL).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES).build());
        this.f9473ma2iudo = this.f95v3muymmyr.createPipeline(RhiBuilderData.graphics().vertex(this.f4q5osub5ls1).fragment(this.fz58j51n7t0).vertexLayout(vertexLayout).blend(RhiBlendStateService.BlendState.ALPHA).depthStencil(RhiBlendStateService.DepthStencilState.DISABLED).rasterizer(RhiBlendStateService.RasterizerState.NO_CULL).topology(RhiBlendStateService.PrimitiveTopology.TRIANGLES).build());
    }

    private void mid1qozhrnv6(int n) {
        if (this.f9phum6qo66s.valid() && this.fbnfzfijo0fe >= n) {
            return;
        }
        if (this.f9phum6qo66s.valid()) {
            this.f95v3muymmyr.destroyBuffer(this.f9phum6qo66s);
        }
        this.fbnfzfijo0fe = Math.max(n, this.fbnfzfijo0fe * 2);
        if (this.fbnfzfijo0fe <= 0) {
            this.fbnfzfijo0fe = 4096;
        }
        this.f9phum6qo66s = this.f95v3muymmyr.createBuffer((long)this.fbnfzfijo0fe * 4L, RhiBlendStateService.BufferUsage.flags(RhiBlendStateService.BufferUsage.VERTEX), RhiBlendStateService.BufferAccess.STREAM);
    }

    private void mfa4o903o1g6(EventAttackInputService.WorldRender worldRender, Vec3 vec3) {
        this.f4p8akcapzfc.pushVec3("uCameraPos", (float)vec3.x, (float)vec3.y, (float)vec3.z);
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            FloatBuffer floatBuffer = memoryStack.mallocFloat(16);
            FloatBuffer floatBuffer2 = memoryStack.mallocFloat(16);
            worldRender.viewMatrix().get(floatBuffer);
            worldRender.projectionMatrix().get(floatBuffer2);
            this.f4p8akcapzfc.pushMatrix4("uView", floatBuffer);
            this.f4p8akcapzfc.pushMatrix4("uProjection", floatBuffer2);
        }
    }

    private void mg6nkfkdpkwm(int n) {
        this.m8jbzx2qd3mb("uColor", n);
    }

    private void m8jbzx2qd3mb(String string, int n) {
        float f = (float)(n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544);
        float f2 = (float)(n >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544);
        float f3 = (float)(n >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544);
        float f4 = (float)(n & 0xFF) / Float.intBitsToFloat(1132396544);
        this.f4p8akcapzfc.pushVec4(string, f2, f3, f4, f);
    }

    private float[] m7w48ia2o6ns(List<AABB> list) {
        float[] fArray = new float[list.size() * 36 * 3];
        int n = 0;
        for (AABB aABB : list) {
            n = this.m7mmetbo92ga(fArray, n, aABB);
        }
        return BoxOverlayRenderer.m97zeo8f2x3f(fArray, n);
    }

    private float[] m5siyjlf54om(List<AABB> list, float f) {
        ArrayList<AABB> arrayList = new ArrayList<AABB>(list.size() * 12);
        for (AABB aABB : list) {
            this.m9us5axmlodk(arrayList, aABB, f);
        }
        Object object = new float[arrayList.size() * 36 * 3];
        int n = 0;
        for (AABB aABB : arrayList) {
            n = this.m7mmetbo92ga((float[])object, n, aABB);
        }
        return BoxOverlayRenderer.m97zeo8f2x3f((float[])object, n);
    }

    private void m9us5axmlodk(List<AABB> list, AABB aABB, float f) {
        double d = (double)f * Double.longBitsToDouble(4602678819172646912L);
        double d2 = aABB.minX;
        double d3 = aABB.minY;
        double d4 = aABB.minZ;
        double d5 = aABB.maxX;
        double d6 = aABB.maxY;
        double d7 = aABB.maxZ;
        for (double d8 : new double[]{d3, d6}) {
            for (double d9 : new double[]{d4, d7}) {
                list.add(new AABB(d2, d8 - d, d9 - d, d5, d8 + d, d9 + d));
            }
        }
        for (double d8 : new double[]{d2, d5}) {
            for (double d9 : new double[]{d4, d7}) {
                list.add(new AABB(d8 - d, d3, d9 - d, d8 + d, d6, d9 + d));
            }
        }
        for (double d8 : new double[]{d2, d5}) {
            for (double d9 : new double[]{d3, d6}) {
                list.add(new AABB(d8 - d, d9 - d, d4, d8 + d, d9 + d, d7));
            }
        }
    }

    private int m7mmetbo92ga(float[] fArray, int n, AABB aABB) {
        float f = (float)aABB.minX;
        float f2 = (float)aABB.minY;
        float f3 = (float)aABB.minZ;
        float f4 = (float)aABB.maxX;
        float f5 = (float)aABB.maxY;
        float f6 = (float)aABB.maxZ;
        n = this.mb9p8m9bsll5(fArray, n, f, f2, f6, f4, f2, f6, f4, f5, f6, f, f5, f6);
        n = this.mb9p8m9bsll5(fArray, n, f4, f2, f3, f, f2, f3, f, f5, f3, f4, f5, f3);
        n = this.mb9p8m9bsll5(fArray, n, f, f2, f3, f, f2, f6, f, f5, f6, f, f5, f3);
        n = this.mb9p8m9bsll5(fArray, n, f4, f2, f6, f4, f2, f3, f4, f5, f3, f4, f5, f6);
        n = this.mb9p8m9bsll5(fArray, n, f, f5, f6, f4, f5, f6, f4, f5, f3, f, f5, f3);
        return this.mb9p8m9bsll5(fArray, n, f, f2, f3, f4, f2, f3, f4, f2, f6, f, f2, f6);
    }

    private int mb9p8m9bsll5(float[] fArray, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        n = this.mj18bpo1q1fo(fArray, n, f, f2, f3);
        n = this.mj18bpo1q1fo(fArray, n, f4, f5, f6);
        n = this.mj18bpo1q1fo(fArray, n, f7, f8, f9);
        n = this.mj18bpo1q1fo(fArray, n, f, f2, f3);
        n = this.mj18bpo1q1fo(fArray, n, f7, f8, f9);
        return this.mj18bpo1q1fo(fArray, n, f10, f11, f12);
    }

    private int mj18bpo1q1fo(float[] fArray, int n, float f, float f2, float f3) {
        fArray[n++] = f;
        fArray[n++] = f2;
        fArray[n++] = f3;
        return n;
    }

    private static float[] m97zeo8f2x3f(float[] fArray, int n) {
        if (n == fArray.length) {
            return fArray;
        }
        float[] fArray2 = new float[n];
        System.arraycopy(fArray, 0, fArray2, 0, n);
        return fArray2;
    }

    public record Style(int fillColor, int outlineColor, float edgeWidth, boolean fill, boolean outline, boolean seeThrough) {
    }
}

