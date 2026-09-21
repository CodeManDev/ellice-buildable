package dev.felix.ellice.render.render3d;

import dev.felix.ellice.compat.CompatListenService;
import dev.felix.ellice.compat.CompatPlayerIdService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderOverrideRootService;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

final class Render3dWantsCaptureService implements AutoCloseable {
  private static final int count = 64;
  private static final int count2 = 393216;
  private static final int fbiwhhvqmv0w = 6;
  private static final int count4 = 24;
  private static final float f7lt9l1o0c2r = 320.0f;
  private static final float fetnefhhb006 = 0.05f;
  private static final long timestamp = 1000000000L;
  private static final long timestamp2 = 5000000000L;
  private static final long timestamp3 = 5000000000L;
  private static final int f5ddn9kvltw = 76;
  private static final int count6 = 64;
  private static final int count7 = 64;
  private static final float fgzwra1zggxg = 2.0f;
  private static final int count8 = 1;
  private static final float f88ixasgz6bu = 0.85f;
  private static final RhiBlendStateService.VertexLayout fbgfzjue6ab2;
  private static final RhiBlendStateService.VertexLayout fd307shiadea;
  private static final ShaderDefinition f6lmhmpgfrid;
  private static final ShaderDefinition fgn1deqguonh;
  private static final ShaderDefinition fd9ak93ihox7;
  private static final ShaderDefinition fazjzig6lfbs;
  private static final ShaderDefinition fd11qv0f6ps5;
  private static final ShaderDefinition f66x1d2fdfu9;
  private static final ShaderDefinition f97aawwsahlz;
  private static final ShaderDefinition f4y5lq9q86ll;
  private static final ShaderDefinition f6r0xd3fjnrf;
  private static final ShaderDefinition f7qlterueum5;
  static final RhiBlendStateService.BlendState OUTLINE_BLEND;
  private final RhiOperationHandler rhiOperationHandler;
  private final ShaderRecipeService shaderRecipeService;
  private final ShaderRecipeService shaderRecipeService2;
  private final ShaderRecipeService shaderRecipeService3;
  private final ShaderRecipeService shaderRecipeService4;
  private final ShaderRecipeService fgxnv4ehrefh;
  private final ShaderRecipeService shaderRecipeService6;
  private final ShaderRecipeService shaderRecipeService7;
  private final KawasePostProcessor fty10xp4v57;
  private RhiBlendStateService.TextureHandle textureHandle;
  private int count9;
  private boolean enabled;
  private boolean enabled2;
  private long feked3cyturc;
  private final float[] float2;
  private final ShaderRecipeService shaderRecipeService8;
  private final ShaderRecipeService shaderRecipeService9;
  private final ShaderRecipeService shaderRecipeService10;
  private final CompatListenService.Registration items;
  private final Map<UUID, CompatPlayerIdService> entries;
  private Set<UUID> values2;
  private Map<UUID, PlayerMotionSample> fgbmqjbafu1i;
  private FireEffectSettings fireEffectSettings;
  private float[] float3;
  private float[] float4;
  private final Vector4f vector4f;
  private final Vector4f fb0u2jb2mfzz;
  private RhiBlendStateService.BufferHandle bufferHandle;
  private RhiBlendStateService.BufferHandle bufferHandle2;
  private Targets renderer3;
  private long fjutdofkna6;
  private long fmeiiz7sxor;
  private long timestamp7;
  private boolean enabled3;
  private RenderDiagnosticsSnapshot faza4zkcbkii;

  static EffectBranchPlan effectBranchPlan(final FireEffectSettings fireEffectSettings) {
    if (fireEffectSettings == null) {
      return new EffectBranchPlan(false, false, false, false, false, false);
    }
    final boolean hasFire = fireEffectSettings.hasFire();
    return new EffectBranchPlan(
        hasFire,
        fireEffectSettings.hasOutline(),
        fireEffectSettings.hasKawase(),
        fireEffectSettings.hasInterference(),
        hasFire,
        hasFire
            && (fireEffectSettings.bloomStrength() > 0.0f
                || fireEffectSettings.backdropBlur() > 0.0f),
        fireEffectSettings.hasGlow(),
        fireEffectSettings.hasEllice());
  }

  static FireFieldWork estimateFireFieldWork(final int n) {
    return estimateFireFieldWork(n, 64);
  }

  static FireFieldWork estimateFireFieldWork(final int n, final int n2) {
    if (n < 0) {
      throw new IllegalArgumentException("regionCount must not be negative");
    }
    final int mfvl3y0nf9ed = mfvl3y0nf9ed(n2);
    if (n == 0) {
      return new FireFieldWork(0, 0, 0);
    }
    final int n3 = 1 + mfvl3y0nf9ed + 1;
    return new FireFieldWork(n, n3, n3 * n);
  }

  static int outlineJumpFloodInitialStep(final float b, final float b2) {
    int max;
    int a;
    for (max = Math.max(1, (int) Math.ceil(Math.max(0.0f, b) + Math.max(0.0f, b2) + 2.0f)), a = 1;
        a < max && a < 64;
        a <<= 1) {}
    return Math.min(a, 64);
  }

  static boolean nearestFieldEndsInSeedTexture(final int n) {
    return (mfvl3y0nf9ed(n) & 0x1) != 0x0;
  }

  private static int mfvl3y0nf9ed(final int i) {
    if (i < 1 || i > 64 || (i & i - 1) != 0x0) {
      throw new IllegalArgumentException("initialStep must be a power of two in [1, 64]");
    }
    return Integer.numberOfTrailingZeros(i) + 1;
  }

  RenderDiagnosticsSnapshot diagnostics() {
    return this.faza4zkcbkii;
  }

  private void updateState(final String s) {
    this.faza4zkcbkii =
        new RenderDiagnosticsSnapshot(
            s,
            this.faza4zkcbkii.effects(),
            this.faza4zkcbkii.selected(),
            this.faza4zkcbkii.captured(),
            this.faza4zkcbkii.vertices(),
            this.faza4zkcbkii.regions(),
            this.faza4zkcbkii.width(),
            this.faza4zkcbkii.height(),
            this.faza4zkcbkii.terrainDepth(),
            this.faza4zkcbkii.handProtection());
  }

  private static String mirel1efe7sv(final EffectBranchPlan effectBranchPlan) {
    final ArrayList elements = new ArrayList();
    if (effectBranchPlan.fire()) {
      elements.add("Fire");
    }
    if (effectBranchPlan.outline()) {
      elements.add("Outline");
    }
    if (effectBranchPlan.kawase()) {
      elements.add("Kawase");
    }
    if (effectBranchPlan.interference()) {
      elements.add("Interference");
    }
    if (effectBranchPlan.glow()) {
      elements.add("Glow");
    }
    if (effectBranchPlan.ellice()) {
      elements.add("ellice");
    }
    return elements.isEmpty() ? "None" : String.join(" / ", elements);
  }

  Render3dWantsCaptureService(
      final RhiOperationHandler rhiOperationHandler,
      final ShaderOverrideRootService shaderOverrideRootService) {
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    this.feked3cyturc = Long.MIN_VALUE;
    this.float2 = new float[1024];
    this.entries = new LinkedHashMap<UUID, CompatPlayerIdService>();
    this.values2 = Set.of();
    this.fgbmqjbafu1i = Map.of();
    this.float3 = new float[16384];
    this.float4 = new float[8192];
    this.vector4f = new Vector4f();
    this.fb0u2jb2mfzz = new Vector4f();
    this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    this.bufferHandle2 = RhiBlendStateService.BufferHandle.NONE;
    this.faza4zkcbkii = RenderDiagnosticsSnapshot.IDLE;
    this.rhiOperationHandler = rhiOperationHandler;
    this.fty10xp4v57 = new KawasePostProcessor(rhiOperationHandler, shaderOverrideRootService);
    this.shaderRecipeService10 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f4y5lq9q86ll,
                Render3dWantsCaptureService.fd307shiadea,
                Render3dWantsCaptureService.OUTLINE_BLEND,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService9 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f6r0xd3fjnrf,
                Render3dWantsCaptureService.fd307shiadea,
                Render3dWantsCaptureService.OUTLINE_BLEND,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService8 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f7qlterueum5,
                Render3dWantsCaptureService.fd307shiadea,
                Render3dWantsCaptureService.OUTLINE_BLEND,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f6lmhmpgfrid,
                Render3dWantsCaptureService.fbgfzjue6ab2,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.READ_WRITE,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLES));
    this.shaderRecipeService2 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.fd9ak93ihox7,
                Render3dWantsCaptureService.fd307shiadea,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService3 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.fazjzig6lfbs,
                Render3dWantsCaptureService.fd307shiadea,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService4 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.fgn1deqguonh,
                Render3dWantsCaptureService.fd307shiadea,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.fgxnv4ehrefh =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.fd11qv0f6ps5,
                Render3dWantsCaptureService.fd307shiadea,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService6 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f66x1d2fdfu9,
                Render3dWantsCaptureService.fd307shiadea,
                RhiBlendStateService.BlendState.DISABLED,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.shaderRecipeService7 =
        new ShaderRecipeService(
            rhiOperationHandler,
            shaderOverrideRootService,
            new ShaderData(
                Render3dWantsCaptureService.f97aawwsahlz,
                Render3dWantsCaptureService.fd307shiadea,
                Render3dWantsCaptureService.OUTLINE_BLEND,
                RhiBlendStateService.DepthStencilState.DISABLED,
                RhiBlendStateService.RasterizerState.NO_CULL,
                RhiBlendStateService.PrimitiveTopology.TRIANGLE_STRIP));
    this.items =
        CompatListenService.listen(
            new CompatListenService.Listener() {
              @Override
              public boolean wantsCapture(final UUID uuid, final int n) {
                return !Render3dWantsCaptureService.this.enabled3
                    && Render3dWantsCaptureService.this.values2.contains(uuid)
                    && Render3dWantsCaptureService.this.entries.size() < 64;
              }

              @Override
              public void onCapture(final CompatPlayerIdService compatPlayerIdService) {
                if (!Render3dWantsCaptureService.this.enabled3
                    && Render3dWantsCaptureService.this.values2.contains(
                        compatPlayerIdService.playerId())
                    && compatPlayerIdService.baseTriangleCount() > 0) {
                  Render3dWantsCaptureService.this.entries.put(
                      compatPlayerIdService.playerId(), compatPlayerIdService);
                }
              }
            });
  }

  void beginFrame() {
    this.faza4zkcbkii = RenderDiagnosticsSnapshot.IDLE;
    this.feked3cyturc = Long.MIN_VALUE;
    this.enabled2 = false;
    if (this.enabled3) {
      return;
    }
    this.values2 = Set.of();
    this.fgbmqjbafu1i = Map.of();
    this.fireEffectSettings = null;
    this.entries.clear();
  }

  void prepare(final Set<UUID> set, final FireEffectSettings fireEffectSettings) {
    if (set == null) {
      this.prepare((List<PlayerMotionSample>) null, fireEffectSettings);
      return;
    }
    final ArrayList list = new ArrayList(set.size());
    for (final UUID uuid : set) {
      if (uuid != null) {
        list.add(new PlayerMotionSample(uuid));
      }
    }
    this.prepare(list, fireEffectSettings);
  }

  void prepare(final List<PlayerMotionSample> list, final FireEffectSettings fireEffectSettings) {
    if (this.enabled3
        || list == null
        || fireEffectSettings == null
        || !fireEffectSettings.renderable()) {
      this.values2 = Set.of();
      this.fgbmqjbafu1i = Map.of();
      this.fireEffectSettings = null;
      return;
    }
    final LinkedHashMap map = new LinkedHashMap();
    for (final PlayerMotionSample value : list) {
      if (value == null) {
        continue;
      }
      map.putIfAbsent(value.playerId(), value);
      if (map.size() == 64) {
        break;
      }
    }
    this.fgbmqjbafu1i = (Map<UUID, PlayerMotionSample>) Map.copyOf((Map<?, ?>) map);
    this.values2 = (Set<UUID>) Set.copyOf((Collection<?>) map.keySet());
    this.fireEffectSettings = (this.values2.isEmpty() ? null : fireEffectSettings);
  }

  void clear() {
    this.faza4zkcbkii = RenderDiagnosticsSnapshot.IDLE;
    this.values2 = Set.of();
    this.fgbmqjbafu1i = Map.of();
    this.fireEffectSettings = null;
    this.entries.clear();
  }

  void captureSceneDepth(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService) {
    this.feked3cyturc = Long.MIN_VALUE;
    if (this.fireEffectSettings == null
        || this.entries.isEmpty()
        || !this.checkCondition(
            framebufferHandle, render3dViewService.width(), render3dViewService.height())) {
      return;
    }
    rhiCommandBuffer.clearScissor();
    if (this.rhiOperationHandler.blitFramebufferDepth(
        framebufferHandle,
        this.renderer3.sceneDepth.framebuffer(),
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height(),
        0,
        0,
        render3dViewService.width(),
        render3dViewService.height())) {
      this.feked3cyturc = render3dViewService.frameIndex();
    }
  }

  void render(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final Render3dViewService render3dViewService) {
    if (this.enabled3) {
      return;
    }
    final FireEffectSettings fireEffectSettings = this.fireEffectSettings;
    final EffectBranchPlan effectBranchPlan = effectBranchPlan(fireEffectSettings);
    final Map<UUID, PlayerMotionSample> fgbmqjbafu1i = this.fgbmqjbafu1i;
    this.fireEffectSettings = null;
    this.fgbmqjbafu1i = Map.of();
    this.values2 = Set.of();
    final List<CompatPlayerIdService> copy = List.copyOf(this.entries.values());
    this.entries.clear();
    this.faza4zkcbkii =
        new RenderDiagnosticsSnapshot(
            (fireEffectSettings == null)
                ? "Idle"
                : (copy.isEmpty() ? "No model captures" : "Preparing"),
            mirel1efe7sv(effectBranchPlan),
            fgbmqjbafu1i.size(),
            copy.size(),
            0,
            0,
            render3dViewService.width(),
            render3dViewService.height(),
            this.feked3cyturc == render3dViewService.frameIndex(),
            false);
    if (fireEffectSettings == null || copy.isEmpty()) {
      this.updateState19();
      return;
    }
    final int b = effectBranchPlan.outline() ? mc1hff22z4z2(fireEffectSettings.outline()) : 0;
    int n = effectBranchPlan.fire() ? 76 : Math.max(1, b);
    if (effectBranchPlan.kawase()) {
      n = Math.max(n, (int) Math.ceil(fireEffectSettings.kawase().radiusPixels()) + 6);
    }
    if (effectBranchPlan.ellice()) {
      n = Math.max(n, (int) Math.ceil(fireEffectSettings.ellice().widthPixels()) + 4);
    }
    if (effectBranchPlan.glow()) {
      n = Math.max(n, (int) Math.ceil(fireEffectSettings.glow().radiusPixels()) + 3);
    }
    if (effectBranchPlan.interference()) {
      n = Math.max(n, (int) Math.ceil(fireEffectSettings.interference().widthPixels()) + 4);
    }
    final UploadResult m8gh05z92ci = this.m8gh05z92ci(copy, fgbmqjbafu1i, render3dViewService, n);
    this.faza4zkcbkii =
        new RenderDiagnosticsSnapshot(
            "Preparing",
            this.faza4zkcbkii.effects(),
            this.faza4zkcbkii.selected(),
            copy.size(),
            m8gh05z92ci.vertexCount(),
            m8gh05z92ci.fireRegions().size(),
            render3dViewService.width(),
            render3dViewService.height(),
            this.feked3cyturc == render3dViewService.frameIndex(),
            false);
    if (m8gh05z92ci.vertexCount() == 0 || m8gh05z92ci.fireRegions().isEmpty()) {
      this.updateState("No visible mask geometry");
      this.updateState19();
      return;
    }
    final RhiBlendStateService.PipelineHandle resolve = this.shaderRecipeService.resolve();
    this.count9 = fireEffectSettings.damageColor();
    this.enabled &= (this.count9 >>> 24 != 0);
    final boolean b2 =
        effectBranchPlan.fire()
            || effectBranchPlan.outline()
            || effectBranchPlan.interference()
            || effectBranchPlan.glow()
            || effectBranchPlan.ellice()
            || (effectBranchPlan.kawase() && this.enabled);
    final RhiBlendStateService.PipelineHandle pipelineHandle =
        b2 ? this.shaderRecipeService2.resolve() : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle2 =
        b2 ? this.shaderRecipeService3.resolve() : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle3 =
        effectBranchPlan.fire()
            ? this.shaderRecipeService4.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle4 =
        effectBranchPlan.fireBlur()
            ? this.fgxnv4ehrefh.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle5 =
        effectBranchPlan.fire()
            ? this.shaderRecipeService6.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle6 =
        effectBranchPlan.outline()
            ? this.shaderRecipeService7.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle7 =
        effectBranchPlan.ellice()
            ? this.shaderRecipeService10.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle8 =
        effectBranchPlan.glow()
            ? this.shaderRecipeService9.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    final RhiBlendStateService.PipelineHandle pipelineHandle9 =
        effectBranchPlan.interference()
            ? this.shaderRecipeService8.resolve()
            : RhiBlendStateService.PipelineHandle.NONE;
    if (!resolve.valid()
        || (b2 && (!pipelineHandle.valid() || !pipelineHandle2.valid()))
        || (effectBranchPlan.fire() && (!pipelineHandle3.valid() || !pipelineHandle5.valid()))
        || (effectBranchPlan.fireBlur() && !pipelineHandle4.valid())
        || (effectBranchPlan.outline() && !pipelineHandle6.valid())
        || (effectBranchPlan.interference() && !pipelineHandle9.valid())
        || (effectBranchPlan.glow() && !pipelineHandle8.valid())
        || (effectBranchPlan.ellice() && !pipelineHandle7.valid())) {
      this.updateState("Shader pipeline unavailable");
      this.updateState19();
      return;
    }
    if (!this.checkCondition(
        framebufferHandle, render3dViewService.width(), render3dViewService.height())) {
      this.updateState("Depth targets unavailable");
      this.updateState19();
      return;
    }
    this.updateState18();
    rhiCommandBuffer.setColorWriteMask(true, true, true, true);
    rhiCommandBuffer.clearScissor();
    this.enabled2 = (this.feked3cyturc == render3dViewService.frameIndex());
    if (!this.rhiOperationHandler.blitFramebufferDepth(
        framebufferHandle,
        (this.enabled2 ? this.renderer3.foregroundDepth : this.renderer3.sceneDepth).framebuffer(),
        0,
        0,
        this.renderer3.width,
        this.renderer3.height,
        0,
        0,
        this.renderer3.width,
        this.renderer3.height)) {
      this.updateState("Depth copy failed");
      this.updateState20("ESP depth copy failed");
      return;
    }
    if (effectBranchPlan.sceneColorCopy()) {
      rhiCommandBuffer.blitFramebuffer(
          framebufferHandle,
          this.renderer3.sceneColorFbo,
          0,
          0,
          this.renderer3.width,
          this.renderer3.height,
          0,
          0,
          this.renderer3.width,
          this.renderer3.height,
          RhiBlendStateService.FilterMode.NEAREST,
          RhiBlendStateService.FramebufferAspect.COLOR.bit);
    }
    this.timestamp7 = System.nanoTime();
    this.updateState6(rhiCommandBuffer, resolve, m8gh05z92ci.vertexCount(), render3dViewService);
    RhiBlendStateService.TextureHandle textureHandle = RhiBlendStateService.TextureHandle.NONE;
    if (b2) {
      int n2 =
          effectBranchPlan.fire()
              ? 64
              : outlineJumpFloodInitialStep(
                  fireEffectSettings.outline().thicknessPixels(),
                  fireEffectSettings.outline().softnessPixels());
      if (effectBranchPlan.interference()) {
        n2 =
            Math.max(
                n2,
                outlineJumpFloodInitialStep(fireEffectSettings.interference().widthPixels(), 2.0f));
      }
      if (effectBranchPlan.glow()) {
        n2 =
            Math.max(
                n2, outlineJumpFloodInitialStep(fireEffectSettings.glow().radiusPixels(), 2.0f));
      }
      if (effectBranchPlan.kawase() && this.enabled) {
        n2 =
            Math.max(
                n2, outlineJumpFloodInitialStep(fireEffectSettings.kawase().radiusPixels(), 2.0f));
      }
      if (effectBranchPlan.ellice()) {
        n2 =
            Math.max(
                n2, outlineJumpFloodInitialStep(fireEffectSettings.ellice().widthPixels(), 2.0f));
      }
      textureHandle =
          this.createTextureHandle(
              rhiCommandBuffer, pipelineHandle, pipelineHandle2, m8gh05z92ci.fireRegions(), n2);
    }
    if (effectBranchPlan.fire()) {
      this.mbktclesjumd(
          rhiCommandBuffer,
          pipelineHandle3,
          pipelineHandle4,
          pipelineHandle5,
          framebufferHandle,
          textureHandle,
          fireEffectSettings,
          render3dViewService,
          m8gh05z92ci.fireRegions());
    }
    if (effectBranchPlan.kawase()) {
      this.fty10xp4v57.render(
          rhiCommandBuffer,
          framebufferHandle,
          this.bufferHandle2,
          this.renderer3.modelMask,
          this.renderer3.modelDepth,
          this.renderer3.sceneDepth.texture(),
          render3dViewService,
          fireEffectSettings.kawase(),
          m8gh05z92ci.fireRegions(),
          this.textureHandle,
          textureHandle,
          this.enabled ? this.count9 : 0,
          this.renderer3.foregroundDepth.texture(),
          this.enabled2);
    } else {
      this.fty10xp4v57.clearTargets();
    }
    if (effectBranchPlan.interference()) {
      this.updateState13(
          rhiCommandBuffer,
          pipelineHandle9,
          framebufferHandle,
          textureHandle,
          fireEffectSettings.interference(),
          render3dViewService,
          m8gh05z92ci.fireRegions());
    }
    if (effectBranchPlan.glow()) {
      this.updateState15(
          rhiCommandBuffer,
          pipelineHandle8,
          framebufferHandle,
          textureHandle,
          fireEffectSettings.glow(),
          render3dViewService,
          m8gh05z92ci.fireRegions());
    }
    if (effectBranchPlan.ellice()) {
      this.updateState14(
          rhiCommandBuffer,
          pipelineHandle7,
          framebufferHandle,
          textureHandle,
          fireEffectSettings.ellice(),
          render3dViewService,
          m8gh05z92ci.fireRegions());
    }
    if (effectBranchPlan.outline()) {
      this.updateState11(
          rhiCommandBuffer,
          pipelineHandle6,
          framebufferHandle,
          textureHandle,
          fireEffectSettings.outline(),
          render3dViewService,
          m8gh05z92ci.fireRegions());
    }
    this.faza4zkcbkii =
        new RenderDiagnosticsSnapshot(
            "Rendered",
            this.faza4zkcbkii.effects(),
            this.faza4zkcbkii.selected(),
            copy.size(),
            m8gh05z92ci.vertexCount(),
            m8gh05z92ci.fireRegions().size(),
            this.renderer3.width,
            this.renderer3.height,
            true,
            this.enabled2);
  }

  private void mbktclesjumd(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.PipelineHandle pipelineHandle2,
      final RhiBlendStateService.PipelineHandle pipelineHandle3,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final FireEffectSettings fireEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    this.updateState8(
        rhiCommandBuffer,
        pipelineHandle,
        textureHandle,
        fireEffectSettings,
        render3dViewService,
        list);
    final float n = this.renderer3.fieldWidth / (float) this.renderer3.width;
    final boolean b =
        fireEffectSettings.bloomStrength() > 0.0f || fireEffectSettings.backdropBlur() > 0.0f;
    final float n2 =
        b
            ? Math.min(
                Float.intBitsToFloat(1111490560),
                Math.max(0.0f, fireEffectSettings.blurRadiusPixels() * n))
            : 0.0f;
    final List<ScreenRect> mbp2ge3llxkh =
        mbp2ge3llxkh(
            list,
            this.renderer3.width,
            this.renderer3.height,
            this.renderer3.fieldWidth,
            this.renderer3.fieldHeight,
            1,
            1);
    final int n3 = (int) Math.ceil(n2) + 2;
    final List<ScreenRect> mgzyeskm9i71 =
        mgzyeskm9i71(mbp2ge3llxkh, this.renderer3.fieldWidth, this.renderer3.fieldHeight, n3, 1);
    final List<ScreenRect> mgzyeskm9i72 =
        mgzyeskm9i71(mgzyeskm9i71, this.renderer3.fieldWidth, this.renderer3.fieldHeight, 1, n3);
    if (b) {
      this.updateState9(
          rhiCommandBuffer,
          pipelineHandle2,
          this.renderer3.fireField,
          this.renderer3.fireBlurFboA,
          this.renderer3.width,
          this.renderer3.height,
          0.0f,
          0.0f,
          0.0f,
          mbp2ge3llxkh);
      if (n2 >= Float.intBitsToFloat(1056964608)) {
        this.updateState9(
            rhiCommandBuffer,
            pipelineHandle2,
            this.renderer3.fireBlurA,
            this.renderer3.fireBlurFboB,
            this.renderer3.fieldWidth,
            this.renderer3.fieldHeight,
            n2,
            1.0f,
            0.0f,
            mgzyeskm9i71);
        this.updateState9(
            rhiCommandBuffer,
            pipelineHandle2,
            this.renderer3.fireBlurB,
            this.renderer3.fireBlurFboA,
            this.renderer3.fieldWidth,
            this.renderer3.fieldHeight,
            n2,
            0.0f,
            1.0f,
            mgzyeskm9i72);
      }
    }
    final boolean b2 = fireEffectSettings.backdropBlur() > 0.0f;
    final float n4 =
        b2
            ? Math.min(
                Float.intBitsToFloat(1099956224),
                Math.max(
                    2.0f,
                    fireEffectSettings.blurRadiusPixels() * Float.intBitsToFloat(1054951342) * n))
            : 0.0f;
    final int n5 = (int) Math.ceil(fireEffectSettings.heatDistortionPixels() * n) + 2;
    final List<ScreenRect> mgzyeskm9i73 =
        mgzyeskm9i71(mgzyeskm9i72, this.renderer3.fieldWidth, this.renderer3.fieldHeight, n5, n5);
    final int n6 = (int) Math.ceil(n4) + 2;
    final List<ScreenRect> list2 =
        b2
            ? mgzyeskm9i71(
                mgzyeskm9i73, this.renderer3.fieldWidth, this.renderer3.fieldHeight, n6, n6)
            : List.of();
    if (b2) {
      this.updateState9(
          rhiCommandBuffer,
          pipelineHandle2,
          this.renderer3.sceneColor,
          this.renderer3.sceneBlurFboA,
          this.renderer3.width,
          this.renderer3.height,
          0.0f,
          0.0f,
          0.0f,
          list2);
      this.updateState9(
          rhiCommandBuffer,
          pipelineHandle2,
          this.renderer3.sceneBlurA,
          this.renderer3.sceneBlurFboB,
          this.renderer3.fieldWidth,
          this.renderer3.fieldHeight,
          n4,
          1.0f,
          0.0f,
          list2);
      this.updateState9(
          rhiCommandBuffer,
          pipelineHandle2,
          this.renderer3.sceneBlurB,
          this.renderer3.sceneBlurFboA,
          this.renderer3.fieldWidth,
          this.renderer3.fieldHeight,
          n4,
          0.0f,
          1.0f,
          mgzyeskm9i73);
    }
    this.mfsc5wzebjgg(
        rhiCommandBuffer,
        pipelineHandle3,
        framebufferHandle,
        textureHandle,
        fireEffectSettings,
        render3dViewService,
        mbp2ge3llxkh(
            mgzyeskm9i73,
            this.renderer3.fieldWidth,
            this.renderer3.fieldHeight,
            this.renderer3.width,
            this.renderer3.height,
            2,
            2));
  }

  private UploadResult m8gh05z92ci(
      final List<CompatPlayerIdService> list,
      final Map<UUID, PlayerMotionSample> map,
      final Render3dViewService render3dViewService,
      final int n) {
    int n2 = 0;
    int n3 = 0;
    int n4 = 0;
    Arrays.fill(this.float2, 0.0f);
    this.enabled = false;
    final Matrix4f viewProjection = render3dViewService.viewProjection();
    final ArrayList list2 = new ArrayList();
    final long max =
        Math.max(
            1L,
            (long)
                (render3dViewService.width()
                    * (double) render3dViewService.height()
                    * Double.longBitsToDouble(4605831339126554624L)));
    for (final CompatPlayerIdService compatPlayerIdService : list) {
      final float[] copyEffectModelPositions = copyEffectModelPositions(compatPlayerIdService);
      if (copyEffectModelPositions.length < 9) {
        continue;
      }
      this.mbzb090i1j20(copyEffectModelPositions.length / 3 * 2);
      final PlayerMotionSample playerMotionSample = map.get(compatPlayerIdService.playerId());
      final ScreenRect nextWidth =
          nextWidth(
              copyEffectModelPositions,
              playerMotionSample,
              viewProjection,
              render3dViewService.width(),
              render3dViewService.height(),
              this.float4,
              this.vector4f,
              this.fb0u2jb2mfzz,
              n);
      if (nextWidth == null) {
        continue;
      }
      final ArrayList c = new ArrayList<ScreenRect>(list2);
      mgd4hblcklmm((List<ScreenRect>) c, nextWidth);
      if (mdr21f0tz0e1((List<ScreenRect>) c) > max) {
        continue;
      }
      final int min = Math.min(copyEffectModelPositions.length / 3, 393216 - n3);
      final int n5 = min - min % 3;
      if (n5 <= 0) {
        break;
      }
      this.m8674cc0ijpb(n2 + n5 * 6);
      final int n6;
      n4 = (n6 = n4 + 1);
      final float n7 = n6 / Float.intBitsToFloat(1132396544);
      this.float2[n6 * 4] = mam6i3u1ymnm(compatPlayerIdService.playerId());
      this.float2[n6 * 4 + 1] =
          ((playerMotionSample == null) ? 0.0f : playerMotionSample.damageAmount());
      this.enabled |= (this.float2[n6 * 4 + 1] > 0.0f);
      for (int i = 0; i < n5; ++i) {
        final int n8 = i * 3;
        final float[] float3 = this.float3;
        final int n9 = n2;
        final int n10 = n2 + 1;
        float3[n9] = copyEffectModelPositions[n8];
        final float[] f9p1e5te5nf7 = this.float3;
        final int n11 = n10;
        final int n12 = n10 + 1;
        f9p1e5te5nf7[n11] = copyEffectModelPositions[n8 + 1];
        final float[] f9p1e5te5nf8 = this.float3;
        final int n13 = n12;
        final int n14 = n12 + 1;
        f9p1e5te5nf8[n13] = copyEffectModelPositions[n8 + 2];
        final float[] f9p1e5te5nf9 = this.float3;
        final int n15 = n14;
        final int n16 = n14 + 1;
        f9p1e5te5nf9[n15] = n7;
        final float[] f9p1e5te5nf10 = this.float3;
        final int n17 = n16;
        final int n18 = n16 + 1;
        f9p1e5te5nf10[n17] = this.float4[i * 2];
        final float[] f9p1e5te5nf11 = this.float3;
        final int n19 = n18;
        n2 = n18 + 1;
        f9p1e5te5nf11[n19] = this.float4[i * 2 + 1];
      }
      n3 += n5;
      list2.clear();
      list2.addAll(c);
      if (n3 == 393216) {
        break;
      }
    }
    if (n3 == 0) {
      return new UploadResult(0, List.of());
    }
    if (!this.textureHandle.valid()) {
      this.textureHandle =
          this.rhiOperationHandler.createTexture(
              new RhiBlendStateService.TextureDescriptor(
                  256,
                  1,
                  RhiBlendStateService.TextureFormat.RGBA8,
                  RhiBlendStateService.FilterMode.NEAREST,
                  RhiBlendStateService.FilterMode.NEAREST,
                  RhiBlendStateService.AddressMode.CLAMP));
    }
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      final ByteBuffer malloc = stackPush.malloc(this.float2.length);
      final float[] float2 = this.float2;
      for (int length = float2.length, j = 0; j < length; ++j) {
        malloc.put(
            (byte)
                Math.round(Math.clamp(float2[j], 0.0f, 1.0f) * Float.intBitsToFloat(1132396544)));
      }
      malloc.flip();
      this.rhiOperationHandler.updateTexture(this.textureHandle, 0, 0, 256, 1, malloc);
    }
    final long n20 = n2 * 4L;
    if (!this.bufferHandle.valid()) {
      this.bufferHandle =
          this.rhiOperationHandler.createBuffer(
              n20,
              RhiBlendStateService.BufferUsage.VERTEX.bit,
              RhiBlendStateService.BufferAccess.STREAM);
    } else {
      this.rhiOperationHandler.orphanBuffer(this.bufferHandle, n20);
    }
    this.rhiOperationHandler.updateBuffer(this.bufferHandle, 0L, this.float3, n2);
    return new UploadResult(n3, (List<ScreenRect>) List.copyOf((Collection<?>) list2));
  }

  static float[] projectedScreenMotion(
      final float[] array,
      final PlayerMotionSample playerMotionSample,
      final Matrix4f matrix4f,
      final int n,
      final int n2) {
    final int n3 = (array == null) ? 0 : (array.length / 3);
    final float[] array2 = new float[n3 * 2];
    if (n3 == 0 || playerMotionSample == null || matrix4f == null || n <= 0 || n2 <= 0) {
      return array2;
    }
    nextWidth(
        array, playerMotionSample, matrix4f, n, n2, array2, new Vector4f(), new Vector4f(), 76);
    return array2;
  }

  static float[] copyEffectModelPositions(final CompatPlayerIdService compatPlayerIdService) {
    if (compatPlayerIdService == null) {
      return new float[0];
    }
    return compatPlayerIdService.copyBaseTrianglePositions();
  }

  private static int mc1hff22z4z2(final OutlineEffectSettings outlineEffectSettings) {
    return Math.max(
        1,
        (int)
            Math.ceil(
                outlineEffectSettings.thicknessPixels()
                    + outlineEffectSettings.softnessPixels()
                    + 2.0f));
  }

  private static ScreenRect nextWidth(
      final float[] array,
      final PlayerMotionSample playerMotionSample,
      final Matrix4f matrix4f,
      final int a,
      final int a2,
      final float[] array2,
      final Vector4f vector4f,
      final Vector4f vector4f2,
      final int n) {
    float n2 = Float.intBitsToFloat(2139095040);
    float a3 = Float.intBitsToFloat(2139095040);
    float a4 = Float.intBitsToFloat(-8388608);
    float a5 = Float.intBitsToFloat(-8388608);
    for (int n3 = array.length / 3, i = 0; i < n3; ++i) {
      final int n4 = i * 3;
      final int n5 = i * 2;
      array2[n5 + 1] = (array2[n5] = 0.0f);
      final float n6 = array[n4];
      final float n7 = array[n4 + 1];
      final float n8 = array[n4 + 2];
      vector4f.set(n6, n7, n8, 1.0f);
      matrix4f.transform(vector4f);
      if (vector4f.w > Float.intBitsToFloat(953267991)) {
        final float n9 = 1.0f / vector4f.w;
        final float n10 =
            (vector4f.x * n9 * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608))
                * a;
        final float n11 =
            (vector4f.y * n9 * Float.intBitsToFloat(1056964608) + Float.intBitsToFloat(1056964608))
                * a2;
        n2 = Math.min(n2, n10);
        a3 = Math.min(a3, n11);
        a4 = Math.max(a4, n10);
        a5 = Math.max(a5, n11);
      }
      if (playerMotionSample != null) {
        vector4f2.set(
            n6 + playerMotionSample.velocityX() * Float.intBitsToFloat(1028443341),
            n7 + playerMotionSample.velocityY() * Float.intBitsToFloat(1028443341),
            n8 + playerMotionSample.velocityZ() * Float.intBitsToFloat(1028443341),
            1.0f);
        matrix4f.transform(vector4f2);
        if (vector4f.w > Float.intBitsToFloat(953267991)) {
          if (vector4f2.w > Float.intBitsToFloat(953267991)) {
            float f =
                (vector4f2.x / vector4f2.w - vector4f.x / vector4f.w)
                    * Float.intBitsToFloat(1056964608)
                    * a
                    / Float.intBitsToFloat(1028443341);
            float f2 =
                (vector4f2.y / vector4f2.w - vector4f.y / vector4f.w)
                    * Float.intBitsToFloat(1056964608)
                    * a2
                    / Float.intBitsToFloat(1028443341);
            if (Float.isFinite(f)) {
              if (Float.isFinite(f2)) {
                final float n12 = (float) Math.hypot(f, f2);
                if (n12 > Float.intBitsToFloat(1134559232)) {
                  final float n13 = Float.intBitsToFloat(1134559232) / n12;
                  f *= n13;
                  f2 *= n13;
                }
                array2[n5] = f;
                array2[n5 + 1] = f2;
              }
            }
          }
        }
      }
    }
    if (!Float.isFinite(n2)) {
      return null;
    }
    final int max = Math.max(0, (int) Math.floor(n2) - n);
    final int max2 = Math.max(0, (int) Math.floor(a3) - n);
    final int min = Math.min(a, (int) Math.ceil(a4) + n);
    final int min2 = Math.min(a2, (int) Math.ceil(a5) + n);
    if (min <= max || min2 <= max2) {
      return null;
    }
    return new ScreenRect(max, max2, min - max, min2 - max2);
  }

  private static long mdr21f0tz0e1(final List<ScreenRect> list) {
    long n = 0L;
    for (final ScreenRect screenRect : list) {
      n += screenRect.width() * (long) screenRect.height();
    }
    return n;
  }

  private static void mgd4hblcklmm(final List<ScreenRect> list, final ScreenRect screenRect) {
    int a = screenRect.x();
    int a2 = screenRect.y();
    int a3 = screenRect.right();
    int a4 = screenRect.top();
    int i = 0;
    while (i < list.size()) {
      final ScreenRect screenRect2 = list.get(i);
      if (a3 < screenRect2.x()
          || screenRect2.right() < a
          || a4 < screenRect2.y()
          || screenRect2.top() < a2) {
        ++i;
      } else {
        a = Math.min(a, screenRect2.x());
        a2 = Math.min(a2, screenRect2.y());
        a3 = Math.max(a3, screenRect2.right());
        a4 = Math.max(a4, screenRect2.top());
        list.remove(i);
      }
    }
    list.add(new ScreenRect(a, a2, a3 - a, a4 - a2));
  }

  private static List<ScreenRect> mgzyeskm9i71(
      final List<ScreenRect> list, final int n, final int n2, final int b, final int b2) {
    return mbp2ge3llxkh(list, n, n2, n, n2, Math.max(0, b), Math.max(0, b2));
  }

  private static List<ScreenRect> mbp2ge3llxkh(
      final List<ScreenRect> list,
      final int b,
      final int b2,
      final int a,
      final int a2,
      final int n,
      final int n2) {
    if (list.isEmpty()) {
      return List.of();
    }
    final ArrayList coll = new ArrayList(list.size());
    final double n3 = a / (double) Math.max(1, b);
    final double n4 = a2 / (double) Math.max(1, b2);
    for (final ScreenRect screenRect : list) {
      final int max = Math.max(0, (int) Math.floor(screenRect.x() * n3) - n);
      final int max2 = Math.max(0, (int) Math.floor(screenRect.y() * n4) - n2);
      final int min = Math.min(a, (int) Math.ceil(screenRect.right() * n3) + n);
      final int min2 = Math.min(a2, (int) Math.ceil(screenRect.top() * n4) + n2);
      if (min > max && min2 > max2) {
        mgd4hblcklmm(coll, new ScreenRect(max, max2, min - max, min2 - max2));
      }
    }
    return (List<ScreenRect>) List.copyOf((Collection<?>) coll);
  }

  private void m8674cc0ijpb(final int n) {
    if (n <= this.float3.length) {
      return;
    }
    int a;
    int n2;
    for (a = 2359296, n2 = this.float3.length;
        n2 < n && n2 < a;
        n2 = Math.min(a, n2 + Math.max(1024, n2 / 2))) {}
    final float[] float3 = new float[n2];
    System.arraycopy(this.float3, 0, float3, 0, this.float3.length);
    this.float3 = float3;
  }

  private void mbzb090i1j20(final int n) {
    if (n <= this.float4.length) {
      return;
    }
    int i;
    for (i = this.float4.length; i < n; i += Math.max(1024, i / 2)) {}
    this.float4 = new float[i];
  }

  private void updateState6(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final int n,
      final Render3dViewService render3dViewService) {
    rhiCommandBuffer.clearScissor();
    final float intBitsToFloat = Float.intBitsToFloat(1056997505);
    rhiCommandBuffer.beginRenderPass(
        this.renderer3.modelMaskFbo, 0.0f, 0.0f, intBitsToFloat, intBitsToFloat);
    rhiCommandBuffer.clearDepth(1.0f);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle, 0);
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer, "uView", render3dViewService.view(), stackPush.mallocFloat(16));
      updateState23(
          rhiCommandBuffer,
          "uProjection",
          render3dViewService.projection(),
          stackPush.mallocFloat(16));
    }
    rhiCommandBuffer.draw(n, 1, 0);
    rhiCommandBuffer.endRenderPass();
  }

  private RhiBlendStateService.TextureHandle createTextureHandle(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.PipelineHandle pipelineHandle2,
      final List<ScreenRect> list,
      final int n) {
    mfvl3y0nf9ed(n);
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(this.renderer3.fireSeedFbo);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelDepth, 1);
    rhiCommandBuffer.pushInt("uPlayerMask", 0);
    rhiCommandBuffer.pushInt("uPlayerDepth", 1);
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
    RhiBlendStateService.TextureHandle fireSeed = this.renderer3.fireSeed;
    for (int i = n; i >= 1; i >>= 1) {
      final boolean b = fireSeed.id() == this.renderer3.fireSeed.id();
      this.updateState7(
          rhiCommandBuffer,
          pipelineHandle2,
          fireSeed,
          b ? this.renderer3.fireFieldFbo : this.renderer3.fireSeedFbo,
          i,
          list);
      fireSeed = (b ? this.renderer3.fireField : this.renderer3.fireSeed);
    }
    final boolean b2 = fireSeed.id() == this.renderer3.fireSeed.id();
    final RhiBlendStateService.FramebufferHandle framebufferHandle =
        b2 ? this.renderer3.fireFieldFbo : this.renderer3.fireSeedFbo;
    final RhiBlendStateService.TextureHandle textureHandle =
        b2 ? this.renderer3.fireField : this.renderer3.fireSeed;
    this.updateState7(rhiCommandBuffer, pipelineHandle2, fireSeed, framebufferHandle, 1, list);
    return textureHandle;
  }

  private void updateState7(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final int n,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.pushInt("uSeedField", 0);
    rhiCommandBuffer.pushInt("uJumpStep", n);
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState8(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final FireEffectSettings fireEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(this.renderer3.fireFieldFbo, 0.0f, 0.0f, 0.0f, 0.0f);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 2);
    rhiCommandBuffer.pushInt("uNearestSeed", 0);
    rhiCommandBuffer.pushInt("uPlayerMask", 1);
    rhiCommandBuffer.pushInt("uSceneDepth", 2);
    rhiCommandBuffer.pushVec2(
        "uFieldResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushVec2(
        "uModelResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushFloat("uProjectionScaleY", render3dViewService.projection().m11());
    rhiCommandBuffer.pushFloat("uTime", (float) render3dViewService.timeSeconds());
    rhiCommandBuffer.pushFloat("uIntensity", fireEffectSettings.intensity());
    rhiCommandBuffer.pushFloat("uFlameHeight", fireEffectSettings.flameHeight());
    rhiCommandBuffer.pushFloat("uFlameWidth", fireEffectSettings.flameWidth());
    rhiCommandBuffer.pushFloat("uVisibleOpacity", fireEffectSettings.visibleOpacity());
    rhiCommandBuffer.pushFloat("uThroughOpacity", fireEffectSettings.throughWallsOpacity());
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState9(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final int n,
      final int n2,
      final float n3,
      final float n4,
      final float n5,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle, 0.0f, 0.0f, 0.0f, 0.0f);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.fieldWidth, this.renderer3.fieldHeight);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.pushInt("uSourceTexture", 0);
    rhiCommandBuffer.pushVec2("uSourceResolution", (float) n, (float) n2);
    rhiCommandBuffer.pushVec2("uDirection", n4, n5);
    rhiCommandBuffer.pushFloat("uRadius", n3);
    rhiCommandBuffer.pushFloat("uGaussianBase", gaussianBase(n3));
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  static float gaussianBase(final float b) {
    final float n =
        (float)
            Math.floor(
                Math.max(0.0f, Math.min(Float.intBitsToFloat(1111490560), b))
                    + Float.intBitsToFloat(1056964608));
    if (n < Float.intBitsToFloat(1056964608)) {
      return 1.0f;
    }
    final float max =
        Math.max(n / Float.intBitsToFloat(1077936128), Float.intBitsToFloat(1056964608));
    return (float) Math.exp(Float.intBitsToFloat(-1090519040) / (max * max));
  }

  private void mfsc5wzebjgg(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final FireEffectSettings fireEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneColor, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneBlurA, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.fireField, 2);
    rhiCommandBuffer.bindTexture(this.renderer3.fireBlurA, 3);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 4);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 5);
    rhiCommandBuffer.bindTexture(textureHandle, 6);
    rhiCommandBuffer.pushInt("uSceneColor", 0);
    rhiCommandBuffer.pushInt("uSceneBlur", 1);
    rhiCommandBuffer.pushInt("uSharpFire", 2);
    rhiCommandBuffer.pushInt("uBlurFire", 3);
    rhiCommandBuffer.pushInt("uSceneDepth", 4);
    rhiCommandBuffer.pushInt("uPlayerMask", 5);
    rhiCommandBuffer.pushInt("uNearestSeed", 6);
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushFloat("uTime", (float) render3dViewService.timeSeconds());
    rhiCommandBuffer.pushFloat("uBloomStrength", fireEffectSettings.bloomStrength());
    rhiCommandBuffer.pushFloat("uCoreStrength", 1.0f);
    rhiCommandBuffer.pushFloat("uHeatPixels", fireEffectSettings.heatDistortionPixels());
    rhiCommandBuffer.pushFloat("uBackdropBlur", fireEffectSettings.backdropBlur());
    rhiCommandBuffer.pushFloat(
        "uPerPlayerColor", fireEffectSettings.perPlayerColors() ? 1.0f : 0.0f);
    rhiCommandBuffer.pushFloat("uOcclusionBias", Float.intBitsToFloat(1036831949));
    m7fyz847oim(rhiCommandBuffer, fireEffectSettings);
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState11(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final OutlineEffectSettings outlineEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 2);
    rhiCommandBuffer.pushInt("uNearestSeed", 0);
    rhiCommandBuffer.pushInt("uPlayerMask", 1);
    rhiCommandBuffer.pushInt("uSceneDepth", 2);
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushFloat("uThickness", outlineEffectSettings.thicknessPixels());
    rhiCommandBuffer.pushFloat("uSoftness", outlineEffectSettings.softnessPixels());
    rhiCommandBuffer.pushVec2(
        "uVisibility",
        outlineEffectSettings.hasVisible() ? 1.0f : 0.0f,
        outlineEffectSettings.hasOccluded() ? 1.0f : 0.0f);
    updateState12(rhiCommandBuffer, "uVisibleColor", outlineEffectSettings.visibleColor());
    updateState12(rhiCommandBuffer, "uOccludedColor", outlineEffectSettings.occludedColor());
    rhiCommandBuffer.pushFloat("uOcclusionBias", Float.intBitsToFloat(1036831949));
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private static void updateState12(
      final RhiCommandBuffer rhiCommandBuffer, final String s, final int n) {
    rhiCommandBuffer.pushVec4(
        s,
        (n >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544),
        (n >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544),
        (n & 0xFF) / Float.intBitsToFloat(1132396544),
        (n >>> 24 & 0xFF) / Float.intBitsToFloat(1132396544));
  }

  private void updateState13(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final InterferenceEffectSettings interferenceEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 2);
    rhiCommandBuffer.pushInt("uNearestSeed", 0);
    rhiCommandBuffer.pushInt("uPlayerMask", 1);
    rhiCommandBuffer.pushInt("uSceneDepth", 2);
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushVec2(
        "uVisibility",
        interferenceEffectSettings.hasVisible() ? 1.0f : 0.0f,
        interferenceEffectSettings.hasOccluded() ? 1.0f : 0.0f);
    rhiCommandBuffer.pushFloat(
        "uTime", (float) render3dViewService.timeSeconds() * interferenceEffectSettings.speed());
    rhiCommandBuffer.pushFloat("uWidth", interferenceEffectSettings.widthPixels());
    rhiCommandBuffer.pushFloat("uComplexity", interferenceEffectSettings.scale());
    rhiCommandBuffer.pushFloat("uIntensity", interferenceEffectSettings.intensity());
    updateState12(rhiCommandBuffer, "uColorA", interferenceEffectSettings.colorA());
    updateState12(rhiCommandBuffer, "uColorB", interferenceEffectSettings.colorB());
    updateState12(rhiCommandBuffer, "uOccludedColor", interferenceEffectSettings.occludedColor());
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState14(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final ElliceEffectSettings elliceEffectSettings,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 2);
    rhiCommandBuffer.pushInt("uNearestSeed", 0);
    rhiCommandBuffer.pushInt("uPlayerMask", 1);
    rhiCommandBuffer.pushInt("uSceneDepth", 2);
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushVec2(
        "uVisibility",
        elliceEffectSettings.hasVisible() ? 1.0f : 0.0f,
        elliceEffectSettings.hasOccluded() ? 1.0f : 0.0f);
    rhiCommandBuffer.pushFloat(
        "uTime", (float) render3dViewService.timeSeconds() * elliceEffectSettings.speed());
    rhiCommandBuffer.pushFloat("uWidth", elliceEffectSettings.widthPixels());
    rhiCommandBuffer.pushFloat("uComplexity", elliceEffectSettings.scale());
    rhiCommandBuffer.pushFloat("uIntensity", elliceEffectSettings.intensity());
    updateState12(rhiCommandBuffer, "uColorA", elliceEffectSettings.colorA());
    updateState12(rhiCommandBuffer, "uColorB", elliceEffectSettings.colorB());
    updateState12(rhiCommandBuffer, "uOccludedColor", elliceEffectSettings.occludedColor());
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState15(
      final RhiCommandBuffer rhiCommandBuffer,
      final RhiBlendStateService.PipelineHandle pipelineHandle,
      final RhiBlendStateService.FramebufferHandle framebufferHandle,
      final RhiBlendStateService.TextureHandle textureHandle,
      final Render3dData render3dData,
      final Render3dViewService render3dViewService,
      final List<ScreenRect> list) {
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.beginRenderPass(framebufferHandle);
    rhiCommandBuffer.setViewport(0, 0, this.renderer3.width, this.renderer3.height);
    rhiCommandBuffer.bindPipeline(pipelineHandle);
    this.updateState16(rhiCommandBuffer);
    rhiCommandBuffer.bindVertexBuffer(this.bufferHandle2, 0);
    rhiCommandBuffer.bindTexture(textureHandle, 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelMask, 1);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 2);
    rhiCommandBuffer.pushInt("uNearestSeed", 0);
    rhiCommandBuffer.pushInt("uPlayerMask", 1);
    rhiCommandBuffer.pushInt("uSceneDepth", 2);
    rhiCommandBuffer.pushVec2(
        "uResolution", (float) this.renderer3.width, (float) this.renderer3.height);
    rhiCommandBuffer.pushVec2(
        "uVisibility",
        render3dData.hasVisible() ? 1.0f : 0.0f,
        render3dData.hasOccluded() ? 1.0f : 0.0f);
    rhiCommandBuffer.pushFloat("uRadius", render3dData.radiusPixels());
    rhiCommandBuffer.pushFloat("uHighlights", render3dData.highlights());
    rhiCommandBuffer.pushFloat("uIntensity", render3dData.intensity());
    updateState12(rhiCommandBuffer, "uVisibleColor", render3dData.visibleColor());
    updateState12(rhiCommandBuffer, "uOccludedColor", render3dData.occludedColor());
    try (final MemoryStack stackPush = MemoryStack.stackPush()) {
      updateState23(
          rhiCommandBuffer,
          "uInvProjection",
          render3dViewService.inverseProjection(),
          stackPush.mallocFloat(16));
    }
    for (final ScreenRect screenRect : list) {
      rhiCommandBuffer.pushVec2("uRegionMin", (float) screenRect.x(), (float) screenRect.y());
      rhiCommandBuffer.pushVec2(
          "uRegionMax", (float) (screenRect.right() - 1), (float) (screenRect.top() - 1));
      rhiCommandBuffer.setScissor(
          screenRect.x(), screenRect.y(), screenRect.width(), screenRect.height());
      rhiCommandBuffer.draw(4, 1, 0);
    }
    rhiCommandBuffer.clearScissor();
    rhiCommandBuffer.endRenderPass();
  }

  private void updateState16(final RhiCommandBuffer rhiCommandBuffer) {
    rhiCommandBuffer.bindTexture(this.textureHandle, 7);
    rhiCommandBuffer.pushInt("uTargetData", 7);
    rhiCommandBuffer.bindTexture(this.renderer3.foregroundDepth.texture(), 8);
    rhiCommandBuffer.pushInt("uForegroundDepth", 8);
    rhiCommandBuffer.pushInt("uForegroundActive", this.enabled2 ? 1 : 0);
    rhiCommandBuffer.bindTexture(this.renderer3.modelDepth, 9);
    rhiCommandBuffer.pushInt("uEspModelDepth", 9);
    rhiCommandBuffer.bindTexture(this.renderer3.sceneDepth.texture(), 10);
    rhiCommandBuffer.pushInt("uEspSceneDepth", 10);
    updateState12(rhiCommandBuffer, "uDamageColor", this.count9);
  }

  private static void m7fyz847oim(
      final RhiCommandBuffer rhiCommandBuffer, final FireEffectSettings fireEffectSettings) {
    final int edgeColor = fireEffectSettings.edgeColor();
    final float n = (edgeColor >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544);
    final float n2 = (edgeColor >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544);
    final float n3 = (edgeColor & 0xFF) / Float.intBitsToFloat(1132396544);
    final int coreColor = fireEffectSettings.coreColor();
    final float a = (coreColor >>> 16 & 0xFF) / Float.intBitsToFloat(1132396544);
    final float a2 = (coreColor >>> 8 & 0xFF) / Float.intBitsToFloat(1132396544);
    final float b = (coreColor & 0xFF) / Float.intBitsToFloat(1132396544);
    final float max = Math.max(a, Math.max(a2, b));
    final float n4 =
        (max > Float.intBitsToFloat(953267991))
            ? Math.min(1.0f / max, Float.intBitsToFloat(1082130432))
            : 1.0f;
    rhiCommandBuffer.pushVec3(
        "uLowColor",
        n * Float.intBitsToFloat(1050924810),
        n2 * Float.intBitsToFloat(1046562734),
        n3 * Float.intBitsToFloat(1042536202));
    rhiCommandBuffer.pushVec3("uMidColor", n, n2, n3);
    rhiCommandBuffer.pushVec3("uHotColor", a * n4, a2 * n4, b * n4);
    rhiCommandBuffer.pushVec3(
        "uOccludedColor",
        n * Float.intBitsToFloat(1060655596),
        n2 * Float.intBitsToFloat(1049582633),
        n3 * Float.intBitsToFloat(1043878380));
  }

  private void updateState18() {
    if (!this.bufferHandle2.valid()) {
      this.bufferHandle2 =
          this.rhiOperationHandler.createBuffer(
              RhiBlendStateService.BufferUsage.VERTEX.bit,
              RhiBlendStateService.BufferAccess.STATIC,
              new float[] {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
    }
  }

  private boolean checkCondition(
      final RhiBlendStateService.FramebufferHandle framebufferHandle, final int b, final int b2) {
    final int max = Math.max(1, b);
    final int max2 = Math.max(1, b2);
    if (this.renderer3 != null && this.renderer3.validFor(framebufferHandle, max, max2)) {
      return true;
    }
    final long nanoTime = System.nanoTime();
    if (nanoTime < this.fjutdofkna6) {
      return false;
    }
    Targets currentTargets = null;
    try {
      currentTargets = this.currentTargets(framebufferHandle, max, max2);
      final Targets renderer3 = this.renderer3;
      this.renderer3 = currentTargets;
      currentTargets = null;
      this.updateState22(renderer3);
      this.fjutdofkna6 = 0L;
      this.timestamp7 = System.nanoTime();
      CoreIsInitializedHandler.LOGGER.debug(
          "Exact PlayerModel fire targets ready at {}x{} (field {}x{})",
          new Object[] {
            this.renderer3.width,
            this.renderer3.height,
            this.renderer3.fieldWidth,
            this.renderer3.fieldHeight
          });
      return true;
    } catch (final RuntimeException ex) {
      this.updateState22(currentTargets);
      this.fjutdofkna6 = nanoTime + 1000000000L;
      this.updateState21("Player fire target creation failed: " + ex.getMessage());
      return false;
    }
  }

  private Targets currentTargets(
      final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2) {
    final int n3 = 2;
    final int max = Math.max(1, (n + n3 - 1) / n3);
    final int max2 = Math.max(1, (n2 + n3 - 1) / n3);
    final ArrayList list = new ArrayList();
    final ArrayList list2 = new ArrayList();
    RhiBlendStateService.DepthCopyTarget depthCopyTarget =
        RhiBlendStateService.DepthCopyTarget.NONE;
    RhiBlendStateService.DepthCopyTarget depthCopyTarget2 =
        RhiBlendStateService.DepthCopyTarget.NONE;
    boolean b = false;
    try {
      depthCopyTarget =
          this.rhiOperationHandler.createCompatibleDepthCopyTarget(framebufferHandle, n, n2);
      if (!depthCopyTarget.valid()) {
        throw new IllegalStateException("No compatible world-depth target");
      }
      depthCopyTarget2 =
          this.rhiOperationHandler.createCompatibleDepthCopyTarget(framebufferHandle, n, n2);
      if (!depthCopyTarget2.valid()) {
        throw new IllegalStateException("No compatible foreground-depth target");
      }
      final RhiBlendStateService.TextureHandle textureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  n, n2, RhiBlendStateService.TextureFormat.RGBA8));
      final RhiBlendStateService.TextureHandle currentTextureHandle =
          this.textureHandle(list, mg19hw19j7fg(n, n2, RhiBlendStateService.TextureFormat.DEPTH24));
      final RhiBlendStateService.TextureHandle nextTextureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  n, n2, RhiBlendStateService.TextureFormat.RGBA8));
      final RhiBlendStateService.TextureHandle previousTextureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  max, max2, RhiBlendStateService.TextureFormat.RGBA8));
      final RhiBlendStateService.TextureHandle sourceTextureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  max, max2, RhiBlendStateService.TextureFormat.RGBA8));
      final RhiBlendStateService.TextureHandle m50musjrxq0k6 =
          this.textureHandle(list, mg19hw19j7fg(n, n2, RhiBlendStateService.TextureFormat.RGBA16F));
      final RhiBlendStateService.TextureHandle outputTextureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  n, n2, RhiBlendStateService.TextureFormat.RGBA16F));
      final RhiBlendStateService.TextureHandle resultTextureHandle =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  max, max2, RhiBlendStateService.TextureFormat.RGBA16F));
      final RhiBlendStateService.TextureHandle m50musjrxq0k9 =
          this.textureHandle(
              list,
              RhiBlendStateService.TextureDescriptor.renderTarget(
                  max, max2, RhiBlendStateService.TextureFormat.RGBA16F));
      final Targets targets =
          new Targets(
              framebufferHandle.id(),
              n,
              n2,
              max,
              max2,
              depthCopyTarget,
              depthCopyTarget2,
              textureHandle,
              currentTextureHandle,
              nextTextureHandle,
              previousTextureHandle,
              sourceTextureHandle,
              m50musjrxq0k6,
              outputTextureHandle,
              resultTextureHandle,
              m50musjrxq0k9,
              this.micoba2nk5yt(list2, textureHandle, currentTextureHandle),
              this.m724fg223a0r(list2, nextTextureHandle),
              this.m724fg223a0r(list2, previousTextureHandle),
              this.m724fg223a0r(list2, sourceTextureHandle),
              this.m724fg223a0r(list2, m50musjrxq0k6),
              this.m724fg223a0r(list2, outputTextureHandle),
              this.m724fg223a0r(list2, resultTextureHandle),
              this.m724fg223a0r(list2, m50musjrxq0k9));
      b = true;
      return targets;
    } finally {
      if (!b) {
        for (final RhiBlendStateService.FramebufferHandle framebufferHandle2 :
            (Iterable<RhiBlendStateService.FramebufferHandle>) (Iterable<?>) (list2)) {
          if (framebufferHandle2.valid()) {
            this.rhiOperationHandler.destroyFramebuffer(framebufferHandle2);
          }
        }
        if (depthCopyTarget2.framebuffer().valid()) {
          this.rhiOperationHandler.destroyFramebuffer(depthCopyTarget2.framebuffer());
        }
        if (depthCopyTarget2.texture().valid()) {
          this.rhiOperationHandler.destroyTexture(depthCopyTarget2.texture());
        }
        if (depthCopyTarget.framebuffer().valid()) {
          this.rhiOperationHandler.destroyFramebuffer(depthCopyTarget.framebuffer());
        }
        for (final RhiBlendStateService.TextureHandle textureHandle :
            (Iterable<RhiBlendStateService.TextureHandle>) (Iterable<?>) (list)) {
          if (textureHandle.valid()) {
            this.rhiOperationHandler.destroyTexture(textureHandle);
          }
        }
        if (depthCopyTarget.texture().valid()) {
          this.rhiOperationHandler.destroyTexture(depthCopyTarget.texture());
        }
      }
    }
  }

  private static RhiBlendStateService.TextureDescriptor mg19hw19j7fg(
      final int n, final int n2, final RhiBlendStateService.TextureFormat textureFormat) {
    return new RhiBlendStateService.TextureDescriptor(
        n,
        n2,
        textureFormat,
        RhiBlendStateService.FilterMode.NEAREST,
        RhiBlendStateService.FilterMode.NEAREST,
        RhiBlendStateService.AddressMode.CLAMP);
  }

  private RhiBlendStateService.TextureHandle textureHandle(
      final List<RhiBlendStateService.TextureHandle> list,
      final RhiBlendStateService.TextureDescriptor textureDescriptor) {
    final RhiBlendStateService.TextureHandle texture =
        this.rhiOperationHandler.createTexture(textureDescriptor);
    list.add(texture);
    return texture;
  }

  private RhiBlendStateService.FramebufferHandle m724fg223a0r(
      final List<RhiBlendStateService.FramebufferHandle> list,
      final RhiBlendStateService.TextureHandle... array) {
    final RhiBlendStateService.FramebufferHandle framebuffer =
        this.rhiOperationHandler.createFramebuffer(array);
    list.add(framebuffer);
    return framebuffer;
  }

  private RhiBlendStateService.FramebufferHandle micoba2nk5yt(
      final List<RhiBlendStateService.FramebufferHandle> list,
      final RhiBlendStateService.TextureHandle textureHandle,
      final RhiBlendStateService.TextureHandle textureHandle2) {
    final RhiBlendStateService.FramebufferHandle framebuffer =
        this.rhiOperationHandler.createFramebuffer(textureHandle, textureHandle2);
    list.add(framebuffer);
    return framebuffer;
  }

  private void updateState19() {
    if (this.renderer3 == null || this.timestamp7 == 0L) {
      return;
    }
    if (System.nanoTime() - this.timestamp7 >= 5000000000L) {
      this.updateState22(this.renderer3);
      this.renderer3 = null;
      this.timestamp7 = 0L;
    }
  }

  void clearWorldTargets() {
    this.clear();
    this.updateState22(this.renderer3);
    this.renderer3 = null;
    this.timestamp7 = 0L;
    this.fjutdofkna6 = 0L;
  }

  private void updateState20(final String s) {
    this.updateState22(this.renderer3);
    this.renderer3 = null;
    this.fjutdofkna6 = System.nanoTime() + 1000000000L;
    this.updateState21(s);
  }

  private void updateState21(final String s) {
    final long nanoTime = System.nanoTime();
    if (this.fmeiiz7sxor != 0L && nanoTime - this.fmeiiz7sxor < 5000000000L) {
      return;
    }
    this.fmeiiz7sxor = nanoTime;
    CoreIsInitializedHandler.LOGGER.warn("{}; Player Fire ESP skipped this frame", (Object) s);
  }

  private void updateState22(final Targets targets) {
    this.fty10xp4v57.clearTargets();
    if (targets == null) {
      return;
    }
    if (targets.modelMaskFbo.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.modelMaskFbo);
    }
    if (targets.sceneColorFbo.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.sceneColorFbo);
    }
    if (targets.sceneBlurFboA.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.sceneBlurFboA);
    }
    if (targets.sceneBlurFboB.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.sceneBlurFboB);
    }
    if (targets.fireSeedFbo.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.fireSeedFbo);
    }
    if (targets.fireFieldFbo.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.fireFieldFbo);
    }
    if (targets.fireBlurFboA.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.fireBlurFboA);
    }
    if (targets.fireBlurFboB.valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.fireBlurFboB);
    }
    if (targets.sceneDepth.framebuffer().valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.sceneDepth.framebuffer());
    }
    if (targets.foregroundDepth.framebuffer().valid()) {
      this.rhiOperationHandler.destroyFramebuffer(targets.foregroundDepth.framebuffer());
    }
    if (targets.foregroundDepth.texture().valid()) {
      this.rhiOperationHandler.destroyTexture(targets.foregroundDepth.texture());
    }
    if (targets.modelMask.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.modelMask);
    }
    if (targets.modelDepth.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.modelDepth);
    }
    if (targets.sceneColor.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.sceneColor);
    }
    if (targets.sceneBlurA.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.sceneBlurA);
    }
    if (targets.sceneBlurB.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.sceneBlurB);
    }
    if (targets.fireSeed.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.fireSeed);
    }
    if (targets.fireField.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.fireField);
    }
    if (targets.fireBlurA.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.fireBlurA);
    }
    if (targets.fireBlurB.valid()) {
      this.rhiOperationHandler.destroyTexture(targets.fireBlurB);
    }
    if (targets.sceneDepth.texture().valid()) {
      this.rhiOperationHandler.destroyTexture(targets.sceneDepth.texture());
    }
  }

  private static float mam6i3u1ymnm(final UUID uuid) {
    final long n =
        uuid.getMostSignificantBits() ^ Long.rotateLeft(uuid.getLeastSignificantBits(), 23);
    final long n2 = (n ^ n >>> 33) * -49064778989728563L;
    return ((n2 ^ n2 >>> 33) >>> 40 & 0xFFFFFFL) / Float.intBitsToFloat(1266679807);
  }

  private static void updateState23(
      final RhiCommandBuffer rhiCommandBuffer,
      final String s,
      final Matrix4f matrix4f,
      final FloatBuffer floatBuffer) {
    matrix4f.get(floatBuffer);
    rhiCommandBuffer.pushMatrix4(s, floatBuffer);
  }

  @Override
  public void close() {
    if (this.enabled3) {
      return;
    }
    this.enabled3 = true;
    this.clear();
    this.items.close();
    this.shaderRecipeService.close();
    this.shaderRecipeService2.close();
    this.shaderRecipeService3.close();
    this.shaderRecipeService4.close();
    this.fgxnv4ehrefh.close();
    this.shaderRecipeService6.close();
    this.shaderRecipeService7.close();
    this.fty10xp4v57.close();
    this.shaderRecipeService8.close();
    this.shaderRecipeService9.close();
    this.shaderRecipeService10.close();
    this.updateState22(this.renderer3);
    this.renderer3 = null;
    if (this.textureHandle.valid()) {
      this.rhiOperationHandler.destroyTexture(this.textureHandle);
    }
    this.textureHandle = RhiBlendStateService.TextureHandle.NONE;
    if (this.bufferHandle.valid()) {
      this.rhiOperationHandler.destroyBuffer(this.bufferHandle);
    }
    if (this.bufferHandle2.valid()) {
      this.rhiOperationHandler.destroyBuffer(this.bufferHandle2);
    }
    this.bufferHandle = RhiBlendStateService.BufferHandle.NONE;
    this.bufferHandle2 = RhiBlendStateService.BufferHandle.NONE;
  }

  static {
    fbgfzjue6ab2 =
        RhiBlendStateService.VertexLayout.of(
            24,
            new RhiBlendStateService.VertexAttribute(
                0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
            new RhiBlendStateService.VertexAttribute(
                1, 1, RhiBlendStateService.VertexFormat.FLOAT, 12L),
            new RhiBlendStateService.VertexAttribute(
                2, 2, RhiBlendStateService.VertexFormat.FLOAT, 16L));
    fd307shiadea =
        RhiBlendStateService.VertexLayout.of(
            8,
            new RhiBlendStateService.VertexAttribute(
                0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L));
    f6lmhmpgfrid =
        new ShaderDefinition(
            "ellice:player-fire-model-mask",
            "render3d/player_fire.vert",
            "render3d/player_fire.frag");
    fgn1deqguonh =
        new ShaderDefinition(
            "ellice:player-fire-generate", "fullscreen.vert", "render3d/player_fire_generate.frag");
    fd9ak93ihox7 =
        new ShaderDefinition(
            "ellice:player-fire-seed", "fullscreen.vert", "render3d/player_fire_seed.frag");
    fazjzig6lfbs =
        new ShaderDefinition(
            "ellice:player-fire-jump-flood",
            "fullscreen.vert",
            "render3d/player_fire_jump_flood.frag");
    fd11qv0f6ps5 =
        new ShaderDefinition(
            "ellice:player-fire-blur", "fullscreen.vert", "render3d/player_fire_blur.frag");
    f66x1d2fdfu9 =
        new ShaderDefinition(
            "ellice:player-fire-composite",
            "fullscreen.vert",
            "render3d/player_fire_composite.frag");
    f97aawwsahlz =
        new ShaderDefinition(
            "ellice:player-outline-composite",
            "fullscreen.vert",
            "render3d/player_outline_composite.frag");
    f4y5lq9q86ll =
        new ShaderDefinition(
            "ellice:esp-ellice", "fullscreen.vert", "render3d/esp_ellice_composite.frag");
    f6r0xd3fjnrf =
        new ShaderDefinition(
            "ellice:esp-glow", "fullscreen.vert", "render3d/esp_glow_composite.frag");
    f7qlterueum5 =
        new ShaderDefinition(
            "ellice:player-interference",
            "fullscreen.vert",
            "render3d/player_interference_composite.frag");
    OUTLINE_BLEND =
        new RhiBlendStateService.BlendState(
            true,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendFactor.ONE_MINUS_SRC_ALPHA,
            RhiBlendStateService.BlendOp.ADD,
            RhiBlendStateService.BlendFactor.ZERO,
            RhiBlendStateService.BlendFactor.ONE,
            RhiBlendStateService.BlendOp.ADD);
  }

  record EffectBranchPlan(
      boolean fire,
      boolean outline,
      boolean kawase,
      boolean interference,
      boolean sceneColorCopy,
      boolean fireBlur,
      boolean glow,
      boolean ellice) {
    EffectBranchPlan(
        final boolean b,
        final boolean b2,
        final boolean b3,
        final boolean b4,
        final boolean b5,
        final boolean b6) {
      this(b, b2, b3, b4, b5, b6, false, false);
    }
  }

  record FireFieldWork(int regions, int renderPasses, int draws) {}

  private static final class Targets {
    final int sourceFramebuffer;
    final int width;
    final int height;
    final int fieldWidth;
    final int fieldHeight;
    final RhiBlendStateService.DepthCopyTarget sceneDepth;
    final RhiBlendStateService.DepthCopyTarget foregroundDepth;
    final RhiBlendStateService.TextureHandle modelMask;
    final RhiBlendStateService.TextureHandle modelDepth;
    final RhiBlendStateService.TextureHandle sceneColor;
    final RhiBlendStateService.TextureHandle sceneBlurA;
    final RhiBlendStateService.TextureHandle sceneBlurB;
    final RhiBlendStateService.TextureHandle fireSeed;
    final RhiBlendStateService.TextureHandle fireField;
    final RhiBlendStateService.TextureHandle fireBlurA;
    final RhiBlendStateService.TextureHandle fireBlurB;
    final RhiBlendStateService.FramebufferHandle modelMaskFbo;
    final RhiBlendStateService.FramebufferHandle sceneColorFbo;
    final RhiBlendStateService.FramebufferHandle sceneBlurFboA;
    final RhiBlendStateService.FramebufferHandle sceneBlurFboB;
    final RhiBlendStateService.FramebufferHandle fireSeedFbo;
    final RhiBlendStateService.FramebufferHandle fireFieldFbo;
    final RhiBlendStateService.FramebufferHandle fireBlurFboA;
    final RhiBlendStateService.FramebufferHandle fireBlurFboB;

    Targets(
        final int sourceFramebuffer,
        final int width,
        final int height,
        final int fieldWidth,
        final int fieldHeight,
        final RhiBlendStateService.DepthCopyTarget sceneDepth,
        final RhiBlendStateService.DepthCopyTarget foregroundDepth,
        final RhiBlendStateService.TextureHandle modelMask,
        final RhiBlendStateService.TextureHandle modelDepth,
        final RhiBlendStateService.TextureHandle sceneColor,
        final RhiBlendStateService.TextureHandle sceneBlurA,
        final RhiBlendStateService.TextureHandle sceneBlurB,
        final RhiBlendStateService.TextureHandle fireSeed,
        final RhiBlendStateService.TextureHandle fireField,
        final RhiBlendStateService.TextureHandle fireBlurA,
        final RhiBlendStateService.TextureHandle fireBlurB,
        final RhiBlendStateService.FramebufferHandle modelMaskFbo,
        final RhiBlendStateService.FramebufferHandle sceneColorFbo,
        final RhiBlendStateService.FramebufferHandle sceneBlurFboA,
        final RhiBlendStateService.FramebufferHandle sceneBlurFboB,
        final RhiBlendStateService.FramebufferHandle fireSeedFbo,
        final RhiBlendStateService.FramebufferHandle fireFieldFbo,
        final RhiBlendStateService.FramebufferHandle fireBlurFboA,
        final RhiBlendStateService.FramebufferHandle fireBlurFboB) {
      this.sourceFramebuffer = sourceFramebuffer;
      this.width = width;
      this.height = height;
      this.fieldWidth = fieldWidth;
      this.fieldHeight = fieldHeight;
      this.sceneDepth = sceneDepth;
      this.foregroundDepth = foregroundDepth;
      this.modelMask = modelMask;
      this.modelDepth = modelDepth;
      this.sceneColor = sceneColor;
      this.sceneBlurA = sceneBlurA;
      this.sceneBlurB = sceneBlurB;
      this.fireSeed = fireSeed;
      this.fireField = fireField;
      this.fireBlurA = fireBlurA;
      this.fireBlurB = fireBlurB;
      this.modelMaskFbo = modelMaskFbo;
      this.sceneColorFbo = sceneColorFbo;
      this.sceneBlurFboA = sceneBlurFboA;
      this.sceneBlurFboB = sceneBlurFboB;
      this.fireSeedFbo = fireSeedFbo;
      this.fireFieldFbo = fireFieldFbo;
      this.fireBlurFboA = fireBlurFboA;
      this.fireBlurFboB = fireBlurFboB;
    }

    boolean validFor(
        final RhiBlendStateService.FramebufferHandle framebufferHandle, final int n, final int n2) {
      return framebufferHandle.id() == this.sourceFramebuffer
          && n == this.width
          && n2 == this.height
          && this.sceneDepth.valid()
          && this.modelMask.valid()
          && this.modelDepth.valid()
          && this.sceneColor.valid()
          && this.sceneBlurA.valid()
          && this.sceneBlurB.valid()
          && this.fireSeed.valid()
          && this.fireField.valid()
          && this.fireBlurA.valid()
          && this.fireBlurB.valid()
          && this.modelMaskFbo.valid()
          && this.sceneColorFbo.valid()
          && this.sceneBlurFboA.valid()
          && this.sceneBlurFboB.valid()
          && this.fireSeedFbo.valid()
          && this.fireFieldFbo.valid()
          && this.fireBlurFboA.valid()
          && this.fireBlurFboB.valid();
    }
  }

  record UploadResult(int vertexCount, List<ScreenRect> fireRegions) {}

  record ScreenRect(int x, int y, int width, int height) {
    int right() {
      return this.x + this.width;
    }

    int top() {
      return this.y + this.height;
    }
  }
}
