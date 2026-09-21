













package dev.felix.ellice.render.render3d;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dAddService;
import dev.felix.ellice.render.render3d.Render3dCloseService;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.Render3dViewService;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import dev.felix.ellice.render.render3d.geometry.GeometryXService;
import dev.felix.ellice.render.render3d.lighting.DirectionalLight;
import dev.felix.ellice.render.render3d.lighting.LightingData;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.joml.FrustumIntersection;
import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

final class SceneObjectRenderer
implements AutoCloseable {
    private static final int f460a4s6msrl = 8;
    private final RhiOperationHandler fes5babjny5r;
    private final ShaderOverrideRootService fcli1o4rrj88;
    private final Map<MaterialErFactory.PipelineKey, ShaderRecipeService> f3med4n1x8oe = new HashMap<MaterialErFactory.PipelineKey, ShaderRecipeService>();
    private final Render3dCloseService f8t6q8eht3ha;

    SceneObjectRenderer(RhiOperationHandler rhiOperationHandler, ShaderOverrideRootService shaderOverrideRootService) {
        this.fes5babjny5r = rhiOperationHandler;
        this.fcli1o4rrj88 = shaderOverrideRootService;
        this.f8t6q8eht3ha = new Render3dCloseService(rhiOperationHandler, shaderOverrideRootService);
    }

    void render(Render3dAddService render3dAddService, RhiCommandBuffer rhiCommandBuffer, RhiBlendStateService.FramebufferHandle framebufferHandle, Render3dViewService render3dViewService) {
        List<RenderItem> list = this.m8zmcv08rgdt(render3dAddService, render3dViewService);
        if (list.isEmpty()) {
            this.f8t6q8eht3ha.idle();
            return;
        }
        ArrayList<RenderItem> arrayList = new ArrayList<RenderItem>();
        ArrayList<RenderItem> arrayList2 = new ArrayList<RenderItem>();
        for (RenderItem object2 : list) {
            switch (object2.object.material().queue()) {
                case OPAQUE: 
                case CUTOUT: {
                    arrayList.add(object2);
                    break;
                }
                case TRANSPARENT: 
                case ADDITIVE: {
                    arrayList2.add(object2);
                }
            }
        }
        arrayList.sort(Comparator.comparing((RenderItem renderItem) -> renderItem.object.material().name()).thenComparingDouble(RenderItem::distanceSquared));
        arrayList2.sort(Comparator.comparingDouble(RenderItem::distanceSquared).reversed());
        ArrayList<RenderItem> arrayList3 = new ArrayList<>(list.size());
        rhiCommandBuffer.setColorWriteMask(true, true, true, true);
        rhiCommandBuffer.beginRenderPass(framebufferHandle);
        rhiCommandBuffer.setViewport(0, 0, render3dViewService.width(), render3dViewService.height());
        rhiCommandBuffer.clearScissor();
        this.m83zznnq92p(arrayList, render3dAddService, rhiCommandBuffer, render3dViewService, arrayList3);
        this.m83zznnq92p(arrayList2, render3dAddService, rhiCommandBuffer, render3dViewService, arrayList3);
        rhiCommandBuffer.endRenderPass();
        ArrayList<Render3dCloseService.Draw> arrayList4 = new ArrayList<Render3dCloseService.Draw>();
        Iterator iterator = arrayList3.iterator();
        while (iterator.hasNext()) {
            RenderItem renderItem2 = (RenderItem)iterator.next();
            MaterialErFactory materialErFactory = renderItem2.object.material();
            MaterialErFactory.Glow glow = materialErFactory.glow();
            if (!glow.enabled() || !SceneObjectRenderer.mie319m9yyuc(materialErFactory)) continue;
            arrayList4.add(new Render3dCloseService.Draw(renderItem2.object.mesh(), renderItem2.model, glow));
        }
        this.f8t6q8eht3ha.render(arrayList4, rhiCommandBuffer, framebufferHandle, render3dViewService);
    }

    private static boolean mie319m9yyuc(MaterialErFactory materialErFactory) {
        return (materialErFactory.queue() == MaterialErFactory.Queue.OPAQUE && materialErFactory.shader().equals(Render3dSceneService.PBR_SHADER) && materialErFactory.rasterizer().equals(RhiBlendStateService.RasterizerState.DEFAULT) && materialErFactory.blend().equals(RhiBlendStateService.BlendState.DISABLED) && materialErFactory.depthStencil().equals(RhiBlendStateService.DepthStencilState.READ_WRITE) ? 1 : 0) != 0;
    }

    private List<RenderItem> m8zmcv08rgdt(Render3dAddService render3dAddService, Render3dViewService render3dViewService) {
        Matrix4f matrix4f = render3dViewService.viewProjection();
        FrustumIntersection frustumIntersection = new FrustumIntersection((Matrix4fc)matrix4f);
        Vector3d vector3d = render3dViewService.cameraPosition();
        ArrayList<RenderItem> arrayList = new ArrayList<RenderItem>();
        for (Render3dIdService render3dIdService : render3dAddService.snapshotObjects()) {
            if (!render3dIdService.visible() || render3dIdService.mesh().closed()) continue;
            GeometryXService geometryXService = render3dIdService.transform();
            GeometryVertexCountService.BoundingSphere boundingSphere = render3dIdService.mesh().bounds();
            Matrix4f matrix4f2 = geometryXService.modelMatrixRelativeTo(vector3d.x, vector3d.y, vector3d.z);
            Vector3f vector3f = matrix4f2.transformPosition(boundingSphere.center());
            Vector3f vector3f2 = geometryXService.scale();
            float f = Math.max(Math.abs(vector3f2.x), Math.max(Math.abs(vector3f2.y), Math.abs(vector3f2.z)));
            float f2 = boundingSphere.radius() * f;
            if (!frustumIntersection.testSphere(vector3f.x, vector3f.y, vector3f.z, f2)) continue;
            double d = (double)vector3f.x * (double)vector3f.x + (double)vector3f.y * (double)vector3f.y + (double)vector3f.z * (double)vector3f.z;
            arrayList.add(new RenderItem(render3dIdService, matrix4f2, new Vector3f((Vector3fc)vector3f), d));
        }
        return arrayList;
    }

    private void m83zznnq92p(List<RenderItem> list, Render3dAddService render3dAddService, RhiCommandBuffer rhiCommandBuffer, Render3dViewService render3dViewService, List<RenderItem> list2) {
        List<LightingData> list3 = render3dAddService.snapshotLights();
        for (RenderItem renderItem : list) {
            try {
                MaterialErFactory materialErFactory = renderItem.object.material();
                ShaderRecipeService shaderRecipeService = this.f3med4n1x8oe.computeIfAbsent(materialErFactory.pipelineKey(), pipelineKey -> new ShaderRecipeService(this.fes5babjny5r, this.fcli1o4rrj88, new ShaderData(pipelineKey.shader(), GpuMesh.VERTEX_LAYOUT, pipelineKey.blend(), pipelineKey.depthStencil(), pipelineKey.rasterizer(), RhiBlendStateService.PrimitiveTopology.TRIANGLES)));
                RhiBlendStateService.PipelineHandle pipelineHandle = shaderRecipeService.resolve();
                if (!pipelineHandle.valid()) continue;
                rhiCommandBuffer.bindPipeline(pipelineHandle);
                this.m4j76et9u22a(rhiCommandBuffer, render3dViewService, renderItem.model);
                this.m221zh75toc3(rhiCommandBuffer, render3dAddService, list3, render3dViewService.cameraPosition(), renderItem.center);
                materialErFactory.bind(rhiCommandBuffer);
                renderItem.object.mesh().draw(rhiCommandBuffer);
                list2.add(renderItem);
            }
            catch (RuntimeException runtimeException) {
                CoreIsInitializedHandler.LOGGER.error("Failed to draw ellice 3D object {}", (Object)renderItem.object.id(), (Object)runtimeException);
            }
        }
    }

    private void m4j76et9u22a(RhiCommandBuffer rhiCommandBuffer, Render3dViewService render3dViewService, Matrix4f matrix4f) {
        Matrix3f matrix3f = matrix4f.normal(new Matrix3f());
        Matrix4f matrix4f2 = new Matrix4f().set3x3((Matrix3fc)matrix3f);
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            SceneObjectRenderer.mbgi3e7ycyhs(rhiCommandBuffer, "uView", render3dViewService.view(), memoryStack.mallocFloat(16));
            SceneObjectRenderer.mbgi3e7ycyhs(rhiCommandBuffer, "uProjection", render3dViewService.projection(), memoryStack.mallocFloat(16));
            SceneObjectRenderer.mbgi3e7ycyhs(rhiCommandBuffer, "uModel", matrix4f, memoryStack.mallocFloat(16));
            SceneObjectRenderer.mbgi3e7ycyhs(rhiCommandBuffer, "uNormalMatrix", matrix4f2, memoryStack.mallocFloat(16));
        }
        rhiCommandBuffer.pushFloat("uTime", (float)render3dViewService.timeSeconds());
        rhiCommandBuffer.pushFloat("uDeltaTime", render3dViewService.deltaSeconds());
    }

    private void m221zh75toc3(RhiCommandBuffer rhiCommandBuffer, Render3dAddService render3dAddService, List<LightingData> list, Vector3d vector3d, Vector3f vector3f) {
        DirectionalLight directionalLight = render3dAddService.sun();
        Vector3f vector3f2 = directionalLight.direction();
        Vector3f vector3f3 = directionalLight.color();
        Vector3f vector3f4 = render3dAddService.ambientColor();
        rhiCommandBuffer.pushVec3("uSunDirection", vector3f2.x, vector3f2.y, vector3f2.z);
        rhiCommandBuffer.pushVec3("uSunColor", vector3f3.x, vector3f3.y, vector3f3.z);
        rhiCommandBuffer.pushFloat("uSunIntensity", directionalLight.intensity());
        rhiCommandBuffer.pushVec3("uAmbientColor", vector3f4.x, vector3f4.y, vector3f4.z);
        rhiCommandBuffer.pushFloat("uAmbientIntensity", render3dAddService.ambientIntensity());
        List<LightingData> list2 = list;
        if (list.size() > 8) {
            ArrayList<LightingData> arrayList = new ArrayList<LightingData>(list);
            arrayList.sort(Comparator.comparingDouble(lightingData -> SceneObjectRenderer.mi2jsbmeg6kb(lightingData, vector3d, vector3f)));
            list2 = arrayList;
        }
        int n = Math.min(8, list2.size());
        rhiCommandBuffer.pushInt("uPointLightCount", n);
        for (int i = 0; i < n; ++i) {
            LightingData lightingData2 = list2.get(i);
            Vector3d vector3d2 = lightingData2.position();
            Vector3f vector3f5 = lightingData2.color();
            String string = "uPointLights[" + i + "]";
            rhiCommandBuffer.pushVec3(string + ".position", (float)(vector3d2.x - vector3d.x), (float)(vector3d2.y - vector3d.y), (float)(vector3d2.z - vector3d.z));
            rhiCommandBuffer.pushVec3(string + ".color", vector3f5.x, vector3f5.y, vector3f5.z);
            rhiCommandBuffer.pushFloat(string + ".intensity", lightingData2.intensity());
            rhiCommandBuffer.pushFloat(string + ".radius", lightingData2.radius());
        }
    }

    private static double mi2jsbmeg6kb(LightingData lightingData, Vector3d vector3d, Vector3f vector3f) {
        if (lightingData.intensity() <= 0.0f) {
            return Double.longBitsToDouble(0x7FF0000000000000L);
        }
        Vector3d vector3d2 = lightingData.position();
        double d = vector3d2.x - vector3d.x - (double)vector3f.x;
        double d2 = vector3d2.y - vector3d.y - (double)vector3f.y;
        double d3 = vector3d2.z - vector3d.z - (double)vector3f.z;
        double d4 = (double)lightingData.radius() * (double)lightingData.radius();
        double d5 = Math.max(Double.longBitsToDouble(4547007122018943789L), (double)lightingData.intensity());
        return (d * d + d2 * d2 + d3 * d3) / (d4 * d5);
    }

    void releasePipeline(MaterialErFactory materialErFactory) {
        if (materialErFactory == null) {
            return;
        }
        ShaderRecipeService shaderRecipeService = this.f3med4n1x8oe.remove(materialErFactory.pipelineKey());
        if (shaderRecipeService != null) {
            shaderRecipeService.close();
        }
    }

    void clearWorldTargets() {
        this.f8t6q8eht3ha.clearWorldTargets();
    }

    private static void mbgi3e7ycyhs(RhiCommandBuffer rhiCommandBuffer, String string, Matrix4f matrix4f, FloatBuffer floatBuffer) {
        matrix4f.get(floatBuffer);
        rhiCommandBuffer.pushMatrix4(string, floatBuffer);
    }

    @Override
    public void close() {
        this.f8t6q8eht3ha.close();
        for (ShaderRecipeService shaderRecipeService : this.f3med4n1x8oe.values()) {
            shaderRecipeService.close();
        }
        this.f3med4n1x8oe.clear();
    }

    private record RenderItem(Render3dIdService object, Matrix4f model, Vector3f center, double distanceSquared) {
    }
}
