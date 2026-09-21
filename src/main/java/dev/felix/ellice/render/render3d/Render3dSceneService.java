package dev.felix.ellice.render.render3d;

import dev.felix.ellice.compat.FramebufferInfo;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.diagnostics.DiagnosticsAddFrameListenerService;
import dev.felix.ellice.diagnostics.fatal.FatalCaptureService;
import dev.felix.ellice.diagnostics.fatal.FatalIsTrippedService;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.render.render3d.geometry.GeometryVertexCountService;
import dev.felix.ellice.render.render3d.material.MaterialErFactory;
import dev.felix.ellice.render.render3d.post.PostIdService;
import dev.felix.ellice.render.render3d.post.PostRenderer;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;

public final class Render3dSceneService implements AutoCloseable {
  public static final String WORLD_GRADE_EFFECT = "ellice:world-grade";
  public static final String WORLD_ATMOSPHERE_EFFECT = "ellice:world-atmosphere";
  public static final ShaderDefinition WORLD_ATMOSPHERE_SHADER =
      new ShaderDefinition(
          "ellice:world-atmosphere", "fullscreen.vert", "render3d/world_atmosphere.frag");
  public static final ShaderDefinition PBR_SHADER =
      new ShaderDefinition("ellice:pbr", "render3d/pbr.vert", "render3d/pbr.frag");
  public static final ShaderDefinition WORLD_GRADE_SHADER =
      new ShaderDefinition("ellice:world-grade", "fullscreen.vert", "render3d/world_grade.frag");
  private final RhiOperationHandler rhiOperationHandler = RhiDeviceService.device();
  private final ShaderOverrideRootService shaderOverrideRootService;
  private final Render3dAddService renderer = new Render3dAddService();
  private final SceneObjectRenderer renderer2;
  private final Render3dWantsCaptureService renderer3;
  private final PostRenderer renderer4;
  private final Set<GpuMesh> values = ConcurrentHashMap.newKeySet();
  private final Set<Render3dRenderer> values2 = ConcurrentHashMap.newKeySet();
  private final long timestamp = System.nanoTime();
  private Render3dViewService renderer5;
  private Matrix4f matrix4f;
  private Vector3d vector3d;
  private long timestamp2;
  private long timestamp3;
  private boolean enabled;

  public Render3dSceneService(Path path) {
    if (path == null) {
      throw new IllegalArgumentException("gameDirectory is required");
    }

    this.shaderOverrideRootService = new ShaderOverrideRootService(path.resolve("ellice-shaders"));
    this.renderer2 =
        new SceneObjectRenderer(this.rhiOperationHandler, this.shaderOverrideRootService);
    this.renderer3 =
        new Render3dWantsCaptureService(this.rhiOperationHandler, this.shaderOverrideRootService);
    this.renderer4 = new PostRenderer(this.rhiOperationHandler, this.shaderOverrideRootService);
    this.renderer4.register(
        new PostIdService("ellice:world-atmosphere", WORLD_ATMOSPHERE_SHADER, true)
            .stage(PostIdService.Stage.BEFORE_HAND));
    this.renderer4.register(
        new PostIdService("ellice:world-grade", WORLD_GRADE_SHADER, true)
            .uniform("uMode", 0)
            .uniform("uIntensity", 1.0F)
            .uniform("uExposure", 0.0F)
            .uniform("uContrast", 1.05F)
            .uniform("uSaturation", 1.08F)
            .uniform("uVignette", 0.22F)
            .uniform("uChromatic", 0.0015F)
            .uniform("uDepthFog", 0.0F)
            .uniform("uFogDistance", 112.0F)
            .uniform("uBloom", 0.0F)
            .uniform("uBloomRadius", 8.0F)
            .uniform("uBloomThreshold", 0.65F)
            .uniform("uHalation", 0.0F)
            .uniform("uGrain", 0.009F));
    CoreIsInitializedHandler.LOGGER.info(
        "ellice 3D engine ready (GLSL 330, live shaders: {})",
        this.shaderOverrideRootService.overrideRoot());
  }

  public Render3dAddService scene() {
    return this.renderer;
  }

  public RenderDiagnosticsSnapshot shaderEspDiagnostics() {
    return this.renderer3.diagnostics();
  }

  public Render3dRenderer createView() {
    this.updateState2();
    Render3dRenderer render3dRenderer =
        new Render3dRenderer(this.rhiOperationHandler, this.shaderOverrideRootService);
    this.values2.add(render3dRenderer);
    return render3dRenderer;
  }

  public void destroyView(Render3dRenderer render3dRenderer) {
    if (render3dRenderer != null && this.values2.remove(render3dRenderer)) {
      render3dRenderer.close();
    }
  }

  public ShaderOverrideRootService shaders() {
    return this.shaderOverrideRootService;
  }

  public PostRenderer worldPost() {
    return this.renderer4;
  }

  public PostIdService worldGradeEffect() {
    return this.renderer4.effect("ellice:world-grade").orElseThrow();
  }

  public PostIdService worldAtmosphereEffect() {
    return this.renderer4.effect("ellice:world-atmosphere").orElseThrow();
  }

  public void beginPlayerFireFrame() {
    if (!this.enabled) {
      this.renderer3.beginFrame();
    }
  }

  public void preparePlayerFireAuras(Set<UUID> values, FireEffectSettings fireEffectSettings) {
    if (!this.enabled) {
      this.renderer3.prepare(values, fireEffectSettings);
    }
  }

  public void preparePlayerFireAuras(
      List<PlayerMotionSample> items, FireEffectSettings fireEffectSettings) {
    if (!this.enabled) {
      this.renderer3.prepare(items, fireEffectSettings);
    }
  }

  public void clearPlayerFireAuras() {
    this.renderer3.clear();
  }

  public GpuMesh createMesh(GeometryVertexCountService geometryVertexCount) {
    this.updateState2();
    GpuMesh gpuMesh = new GpuMesh(this.rhiOperationHandler, geometryVertexCount);
    this.values.add(gpuMesh);
    return gpuMesh;
  }

  public MaterialErFactory createPbrMaterial(String text) {
    this.updateState2();
    return MaterialErFactory.builder(text, PBR_SHADER)
        .build()
        .color("uBaseColor", -1)
        .uniform("uMetallic", 0.0F)
        .uniform("uRoughness", 0.48F)
        .uniform("uEmissive", 0.0F, 0.0F, 0.0F)
        .uniform("uEmissiveStrength", 0.0F)
        .uniform("uNormalScale", 1.0F)
        .uniform("uAlphaCutoff", 0.5F)
        .uniform("uHasBaseColorMap", 0)
        .uniform("uHasNormalMap", 0);
  }

  public void renderGeometry(
      EventAttackInputService.WorldRender worldRender, FramebufferInfo framebufferInfo) {
    if (!this.enabled && worldRender != null && framebufferInfo != null) {
      long longValue = System.nanoTime();
      float currentHeight =
          this.timestamp2 == 0L ? 0.016666668F : (float) (longValue - this.timestamp2) / 1.0E9F;
      this.timestamp2 = longValue;
      this.timestamp3++;
      Vec3 vec3 = worldRender.cameraPos();
      Vector3d currentVector3d = new Vector3d(vec3.x, vec3.y, vec3.z);
      Render3dViewService currentWidth =
          new Render3dViewService(
              worldRender.viewMatrix(),
              worldRender.projectionMatrix(),
              this.matrix4f,
              currentVector3d,
              this.vector3d,
              framebufferInfo.width(),
              framebufferInfo.height(),
              worldRender.tickDelta(),
              currentHeight,
              (longValue - this.timestamp) / 1.0E9,
              this.timestamp3);
      this.matrix4f = currentWidth.viewProjection();
      this.vector3d = currentVector3d;
      this.renderer5 = currentWidth;
      this.updateState(
          () -> {
            RhiBlendStateService.FramebufferHandle framebufferHandle =
                new RhiBlendStateService.FramebufferHandle(framebufferInfo.framebufferId());
            this.renderer2.render(
                this.renderer, this.rhiOperationHandler.encoder(), framebufferHandle, currentWidth);
            DiagnosticsAddFrameListenerService diagnosticsAddFrameListener =
                DiagnosticsAddFrameListenerService.get();
            diagnosticsAddFrameListener.begin("shaderesp-depth");

            try {
              this.renderer3.captureSceneDepth(
                  this.rhiOperationHandler.encoder(), framebufferHandle, currentWidth);
            } finally {
              diagnosticsAddFrameListener.end();
            }

            this.renderer4.captureDepth(
                this.rhiOperationHandler.encoder(), framebufferHandle, currentWidth);
            this.renderer4.renderBeforeHand(
                this.rhiOperationHandler.encoder(), framebufferHandle, currentWidth);
          });
    }
  }

  public void renderPost(
      EventAttackInputService.WorldPostRender worldPostRender, FramebufferInfo framebufferInfo) {
    if (!this.enabled
        && worldPostRender != null
        && framebufferInfo != null
        && this.renderer5 != null) {
      Render3dViewService render3dView = this.renderer5;
      this.renderer5 = null;
      if (render3dView.width() == framebufferInfo.width()
          && render3dView.height() == framebufferInfo.height()) {
        this.updateState(
            () -> {
              RhiBlendStateService.FramebufferHandle framebufferHandle =
                  new RhiBlendStateService.FramebufferHandle(framebufferInfo.framebufferId());
              this.renderer4.render(
                  this.rhiOperationHandler.encoder(), framebufferHandle, render3dView);
              DiagnosticsAddFrameListenerService diagnosticsAddFrameListener =
                  DiagnosticsAddFrameListenerService.get();
              diagnosticsAddFrameListener.begin("shaderesp");

              try {
                this.renderer3.render(
                    this.rhiOperationHandler.encoder(), framebufferHandle, render3dView);
              } finally {
                diagnosticsAddFrameListener.end();
              }
            });
      }
    }
  }

  public void clearWorld() {
    this.renderer.clearObjects();
    this.renderer.clearLights();
    this.renderer2.clearWorldTargets();
    this.renderer3.clearWorldTargets();
    this.renderer5 = null;
    this.matrix4f = null;
    this.vector3d = null;
    this.timestamp2 = 0L;
  }

  public void destroyMesh(GpuMesh gpuMesh) {
    if (gpuMesh != null) {
      if (this.values.remove(gpuMesh)) {
        gpuMesh.close();
      }
    }
  }

  public void releaseMaterialPipeline(MaterialErFactory materialEr) {
    if (!this.enabled && materialEr != null) {
      this.renderer2.releasePipeline(materialEr);
    }
  }

  private void updateState(Runnable runnable) {
    if (this.rhiOperationHandler.encoder() instanceof GlRenderer glRenderer) {
      glRenderer.saveGLState();

      try {
        runnable.run();
      } catch (Throwable exception) {
        CoreIsInitializedHandler.LOGGER.error("ellice 3D frame failed", exception);
        FatalIsTrippedService.trip(exception, FatalCaptureService.Kind.RENDERER);
      } finally {
        glRenderer.restoreGLState();
      }
    } else {
      runnable.run();
    }
  }

  private void updateState2() {
    if (this.enabled) {
      throw new IllegalStateException("ellice 3D engine is closed");
    }
  }

  @Override
  public void close() {
    if (!this.enabled) {
      this.enabled = true;
      this.renderer4.close();
      this.renderer3.close();
      this.renderer2.close();

      for (Render3dRenderer render3dRenderer : this.values2) {
        render3dRenderer.close();
      }

      this.values2.clear();

      for (GpuMesh gpuMesh : this.values) {
        gpuMesh.close();
      }

      this.values.clear();
      this.renderer.clearObjects();
      this.renderer.clearLights();
    }
  }
}
