package dev.felix.ellice.feature.terrain;

import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.render.render3d.GpuMesh;
import dev.felix.ellice.render.render3d.Render3dIdService;
import dev.felix.ellice.render.render3d.Render3dRenderer;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.render.render3d.Render3dViewService;
import dev.felix.ellice.render.render3d.shader.ShaderData;
import dev.felix.ellice.render.render3d.shader.ShaderDefinition;
import dev.felix.ellice.render.render3d.shader.ShaderRecipeService;
import dev.felix.ellice.render.rhi.RhiBlendStateService;
import dev.felix.ellice.render.rhi.RhiCommandBuffer;
import dev.felix.ellice.render.rhi.RhiDeviceService;
import dev.felix.ellice.render.rhi.RhiOperationHandler;
import dev.felix.ellice.render.rhi.gl.GlRenderer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class TerrainMapRenderer implements AutoCloseable {
   private static final RhiBlendStateService.VertexLayout f19xoo82qli1 = RhiBlendStateService.VertexLayout.of(
      28,
      new RhiBlendStateService.VertexAttribute(0, 3, RhiBlendStateService.VertexFormat.FLOAT, 0L),
      new RhiBlendStateService.VertexAttribute(1, 4, RhiBlendStateService.VertexFormat.UBYTE, 12L),
      new RhiBlendStateService.VertexAttribute(2, 2, RhiBlendStateService.VertexFormat.FLOAT, 16L),
      new RhiBlendStateService.VertexAttribute(3, 2, RhiBlendStateService.VertexFormat.USHORT, 24L)
   );
   private static final RhiBlendStateService.VertexLayout fdr39yfx42sz = RhiBlendStateService.VertexLayout.of(
      8, new RhiBlendStateService.VertexAttribute(0, 2, RhiBlendStateService.VertexFormat.FLOAT, 0L)
   );
   private final RhiOperationHandler fg0vqlu6cnr6 = RhiDeviceService.device();
   private final Map<Long, TerrainMapRenderer.Column> f82wlg43j14b = new HashMap<>();
   private final Set<Long> fbefuu78f3da = new LinkedHashSet<>();
   private final Render3dSceneService f3a2ta01lqlj;
   private final ShaderRecipeService f17adhgv95cp;
   private final ShaderRecipeService f3hgml1bl53u;
   private final ShaderRecipeService f2lp2f7cwucn;
   private final ShaderRecipeService fbdm2drnguev;
   private final ShaderRecipeService fa58xmj37wd9;
   private final long f9q12z6cc77o = System.nanoTime();
   private long fe2zg1yqci8r;
   private RhiBlendStateService.SamplerHandle fhxhnkfjay1t = RhiBlendStateService.SamplerHandle.NONE;
   private RhiBlendStateService.SamplerHandle fprq8p2g1pa = RhiBlendStateService.SamplerHandle.NONE;
   private RhiBlendStateService.SamplerHandle fahsk4oviegh = RhiBlendStateService.SamplerHandle.NONE;
   private RhiBlendStateService.BufferHandle f91nf5yayye3 = RhiBlendStateService.BufferHandle.NONE;
   private RhiBlendStateService.BufferHandle fg2z0o5ywm2y = RhiBlendStateService.BufferHandle.NONE;
   private RhiBlendStateService.TextureHandle fg9ercbz00bi = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.TextureHandle f7b3u1bgq5u2 = RhiBlendStateService.TextureHandle.NONE;
   private RhiBlendStateService.FramebufferHandle fbzg2d5f57dl = RhiBlendStateService.FramebufferHandle.NONE;
   private RhiBlendStateService.TextureHandle fj2ns364jmcp = RhiBlendStateService.TextureHandle.NONE;
   private Set<Long> f8oj63kb4ho9 = Set.of();
   private Set<Long> f77nq9owxrid = Set.of();
   private int fgmrola3srr2 = Integer.MIN_VALUE;
   private int fbs2oabg5dg3 = Integer.MIN_VALUE;
   private int f91vy7llmgva;
   private int fjnzarehzqjo = 2048;
   private long f3h00b7hj0xb;
   private int f1ydcb4iyt0z;
   private int f77knp8j4m0i;
   private double f864owsryhew;
   private double f73jxvy0ug2k;

   public TerrainMapRenderer(Render3dSceneService var1) {
      this.f3a2ta01lqlj = Objects.requireNonNull(var1);
      ShaderDefinition var2 = new ShaderDefinition("ellice:terrain-map", "terrain/map.vert", "terrain/map.frag");
      this.f17adhgv95cp = new ShaderRecipeService(
         this.fg0vqlu6cnr6,
         var1.shaders(),
         new ShaderData(
            var2,
            f19xoo82qli1,
            RhiBlendStateService.BlendState.DISABLED,
            RhiBlendStateService.DepthStencilState.READ_WRITE,
            RhiBlendStateService.RasterizerState.DEFAULT,
            RhiBlendStateService.PrimitiveTopology.TRIANGLES
         )
      );
      this.f3hgml1bl53u = new ShaderRecipeService(
         this.fg0vqlu6cnr6,
         var1.shaders(),
         new ShaderData(
            var2,
            f19xoo82qli1,
            RhiBlendStateService.BlendState.ALPHA,
            RhiBlendStateService.DepthStencilState.READ_ONLY,
            RhiBlendStateService.RasterizerState.DEFAULT,
            RhiBlendStateService.PrimitiveTopology.TRIANGLES
         )
      );
      ShaderDefinition var3 = new ShaderDefinition("ellice:terrain-sky", "terrain/sky.vert", "terrain/sky.frag");
      this.f2lp2f7cwucn = new ShaderRecipeService(
         this.fg0vqlu6cnr6,
         var1.shaders(),
         new ShaderData(
            var3,
            fdr39yfx42sz,
            RhiBlendStateService.BlendState.DISABLED,
            RhiBlendStateService.DepthStencilState.DISABLED,
            RhiBlendStateService.RasterizerState.NO_CULL,
            RhiBlendStateService.PrimitiveTopology.TRIANGLES
         )
      );
      ShaderDefinition var4 = new ShaderDefinition("ellice:terrain-depth", "terrain/depth.vert", "terrain/depth.frag");
      this.fbdm2drnguev = new ShaderRecipeService(
         this.fg0vqlu6cnr6,
         var1.shaders(),
         new ShaderData(
            var4,
            f19xoo82qli1,
            RhiBlendStateService.BlendState.DISABLED,
            RhiBlendStateService.DepthStencilState.READ_WRITE,
            RhiBlendStateService.RasterizerState.DEFAULT,
            RhiBlendStateService.PrimitiveTopology.TRIANGLES
         )
      );
      ShaderDefinition var5 = new ShaderDefinition("ellice:terrain-figure-depth", "terrain/depth_figure.vert", "terrain/depth_figure.frag");
      this.fa58xmj37wd9 = new ShaderRecipeService(
         this.fg0vqlu6cnr6,
         var1.shaders(),
         new ShaderData(
            var5,
            GpuMesh.VERTEX_LAYOUT,
            RhiBlendStateService.BlendState.DISABLED,
            RhiBlendStateService.DepthStencilState.READ_WRITE,
            RhiBlendStateService.RasterizerState.DEFAULT,
            RhiBlendStateService.PrimitiveTopology.TRIANGLES
         )
      );
   }

   public TerrainMapRenderer.Target createTarget() {
      return new TerrainMapRenderer.Target(this.f3a2ta01lqlj.createView());
   }

   public RhiBlendStateService.TextureHandle debugShadowDepth() {
      return this.f7b3u1bgq5u2;
   }

   public RhiBlendStateService.FramebufferHandle debugShadowFbo() {
      return this.fbzg2d5f57dl;
   }

   public void prepareTarget(TerrainMapRenderer.Target var1, int var2, int var3) {
      var1.f1rpyknhywsf.resize(var2, var3);
   }

   public void destroyTarget(TerrainMapRenderer.Target var1) {
      this.f3a2ta01lqlj.destroyView(var1.f1rpyknhywsf);
   }

   public void setShadowSize(int var1) {
      this.fjnzarehzqjo = TerrainFrameService.clampShadowSize(var1);
   }

   public int shadowSize() {
      return this.fjnzarehzqjo;
   }

   private int mggcjwujybh8() {
      return this.fjnzarehzqjo > 2048 ? 1 : 0;
   }

   public void update(TerrainStateController var1, List<TerrainData> var2) {
      this.f77nq9owxrid = readyColumns(this.f82wlg43j14b.keySet(), var2);
      this.fbefuu78f3da.addAll(var1.takeChangedColumns());
      if (!this.fbefuu78f3da.isEmpty()) {
         long var3 = System.nanoTime();
         this.mirv5tsxkgk6(() -> {
            this.m4il0c93w2kr();
            long var2x = 0L;
            int var4 = 0;

            for (Iterator var5 = this.fbefuu78f3da.iterator(); var5.hasNext() && var4 < 2 && var2x < 2097152L; var4++) {
               long var6 = (Long)var5.next();
               var5.remove();
               List var8 = var1.column(var6);
               var2x += this.m6pp9haukgpw(var6, var8);
            }
         });
         this.f864owsryhew = (System.nanoTime() - var3) / Double.longBitsToDouble(4696837146684686336L);
      }
   }

   static Set<Long> readyColumns(Set<Long> var0, List<TerrainData> var1) {
      if (var0 != null && !var0.isEmpty() && var1 != null && !var1.isEmpty()) {
         HashSet var2 = new HashSet();

         for (TerrainData var4 : var1) {
            if (var4 != null && var0.contains(var4.column())) {
               var2.add(var4.column());
            }
         }

         return Set.copyOf(var2);
      } else {
         return Set.of();
      }
   }

   public void recordedTerrain(Map<TerrainData, TerrainLayerData> var1, Map<TerrainData, TerrainLayerData> var2) {
      HashSet var3 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var2.get(var2x) != var3x) {
            var3.add(var2x.column());
         }
      });
      var2.forEach((var2x, var3x) -> {
         if (var1.get(var2x) != var3x) {
            var3.add(var2x.column());
         }
      });
      if (!var3.isEmpty()) {
         this.mirv5tsxkgk6(
            () -> {
               this.m4il0c93w2kr();

               for (long var4 : (Iterable<Long>) (Iterable<?>) (var3)) {
                  this.m6pp9haukgpw(
                     var4,
                     var2.values().stream().filter(var2xx -> var2xx.key().column() == var4).sorted(Comparator.comparingInt(var0 -> var0.key().y())).toList()
                  );
               }
            }
         );
         this.f77nq9owxrid = var2.keySet().stream().map(TerrainData::column).collect(Collectors.toSet());
      }
   }

   private long m6pp9haukgpw(long var1, List<TerrainLayerData> var3) {
      TerrainMapRenderer.Column var4 = this.f82wlg43j14b.remove(var1);
      if (var4 != null) {
         this.mfon5mbvwtww(var4);
      }

      if (var3.isEmpty()) {
         return 0L;
      }

      EnumMap var5 = new EnumMap<>(TerrainLayerData.Pass.class);
      long var6 = 0L;

      for (TerrainLayerData.Pass var11 : TerrainLayerData.Pass.values()) {
         int var12 = 0;
         float var13 = 0.0F;

         for (TerrainLayerData var15 : var3) {
            for (TerrainLayerData.Layer var17 : var15.layers()) {
               if (var17.pass() == var11) {
                  var12 = Math.addExact(var12, var17.vertexCount());
                  var13 = var17.alphaCutoff();
               }
            }
         }

         if (var12 != 0) {
            ByteBuffer var22 = MemoryUtil.memAlloc(Math.multiplyExact(var12, 28)).order(ByteOrder.nativeOrder());

            try {
               for (TerrainLayerData var25 : var3) {
                  for (TerrainLayerData.Layer var18 : var25.layers()) {
                     if (var18.pass() == var11) {
                        TerrainLayerData.appendColumn(var18, var25.key().y(), var22);
                     }
                  }
               }

               var22.flip();
               TerrainMapRenderer.Part var24 = new TerrainMapRenderer.Part();
               var24.vertexCount = var12;
               var24.cutoff = var13;
               var24.vertices = this.fg0vqlu6cnr6.createBuffer(RhiBlendStateService.BufferUsage.VERTEX.bit, RhiBlendStateService.BufferAccess.STATIC, var22);
               if (var11 == TerrainLayerData.Pass.TRANSLUCENT) {
                  var24.centers = m74mfdqbp83o(var22, var12);
                  var24.order = new long[var12 / 4];
                  var24.sortedIndices = this.fg0vqlu6cnr6
                     .createBuffer(var12 / 4L * 6L * 4L, RhiBlendStateService.BufferUsage.INDEX.bit, RhiBlendStateService.BufferAccess.DYNAMIC);
               }

               this.m21kt3r755fm(var12 / 4);
               var5.put(var11, var24);
               var6 += var12 * 28L + (var11 == TerrainLayerData.Pass.TRANSLUCENT ? var12 / 4L * 24L : 0L);
            } finally {
               MemoryUtil.memFree(var22);
            }
         }
      }

      if (!var5.isEmpty()) {
         this.f82wlg43j14b
            .put(
               var1,
               new TerrainMapRenderer.Column(
                  var1,
                  ((TerrainLayerData)var3.getFirst()).key().blockY(),
                  ((TerrainLayerData)var3.getLast()).key().blockY() + 16,
                  var5,
                  var3.stream().map(TerrainLayerData::key).collect(Collectors.toSet())
               )
            );
      }

      this.f3h00b7hj0xb += var6;
      return var6;
   }

   public void render(
      TerrainMapRenderer.Target var1,
      TerrainViewportService.Frame var2,
      CompatLoadedHandler.Textures var3,
      CompatLoadedHandler.SkyState var4,
      List<Render3dIdService> var5,
      boolean var6
   ) {
      this.render(var1, var2, var3, var4, var5, var6, (System.nanoTime() - this.f9q12z6cc77o) / Double.longBitsToDouble(4741671816366391296L));
   }

   public void render(
      TerrainMapRenderer.Target var1,
      TerrainViewportService.Frame var2,
      CompatLoadedHandler.Textures var3,
      CompatLoadedHandler.SkyState var4,
      List<Render3dIdService> var5,
      boolean var6,
      double var7
   ) {
      if (var1.f1rpyknhywsf.framebuffer().valid() && var3 != null && var3.atlas() > 0 && var3.lightmap() > 0) {
         long var9 = System.nanoTime();
         this.f1ydcb4iyt0z = this.f77knp8j4m0i = 0;
         Vector3d var11 = new Vector3d(var2.eye()).sub(var2.focus());
         Matrix4f var12 = new Matrix4f(var2.view()).translate((float)var11.x, (float)var11.y, (float)var11.z);
         Render3dViewService var13 = new Render3dViewService(
            var12, var2.projection(), null, var2.eye(), null, var1.width(), var1.height(), 0.0F, Float.intBitsToFloat(1015580809), var7, ++this.fe2zg1yqci8r
         );
         Vector3f var14 = var4 != null ? var4.sunDirection() : TerrainSunDirectionService.DEFAULT_SUN;
         float var15 = var4 != null ? var4.daylight() : 1.0F;
         Vector3f var16 = TerrainSunDirectionService.gradeTint(var15, var14.y);
         float var17 = (float)var7;
         TerrainFrameService.ShadowFrame var18 = TerrainFrameService.frame(
            TerrainSunDirectionService.mapSun(var14, var15).direction(),
            TerrainFrameService.radiusFor(var2.distance(), mad6zzaddqlq(var2)),
            var2.focus(),
            this.fjnzarehzqjo
         );
         this.mirv5tsxkgk6(() -> this.m5xupgajz92z(var2, var3, var18, var5));
         this.mirv5tsxkgk6(() -> this.m7xqr3pg5lhd(var2));
         Vector3f var19 = new Vector3f(
            (float)(this.fgmrola3srr2 * Double.longBitsToDouble(4625196817309499392L) - var2.focus().x),
            (float)(this.fbs2oabg5dg3 * Double.longBitsToDouble(4625196817309499392L) - var2.focus().z),
            Float.intBitsToFloat(1149370368)
         );
         float var20 = 2.0F * var18.radius() / this.fjnzarehzqjo / (var18.far() - var18.near()) * Float.intBitsToFloat(1053609165);
         Vector3f var21 = TerrainFogRangeService.fogRange(var2.distance(), var6);
         Vector3f var22 = TerrainFogRangeService.horizon(var15);
         Vector3f var23 = TerrainSunDirectionService.mapSun(var14, var15).direction();

         for (Render3dIdService var25 : var5) {
            var25.material()
               .texture("uShadowMap", this.f7b3u1bgq5u2, this.fhxhnkfjay1t, 2)
               .texture("uLightmap", new RhiBlendStateService.TextureHandle(var3.lightmap()), this.fahsk4oviegh, 1)
               .texture("uCoverage", this.fj2ns364jmcp, this.fahsk4oviegh, 3)
               .uniform("uCoverageOrigin", var19)
               .uniform("uEdgeFog", 0.0F)
               .uniform("uShadowMatrix", var18.viewProjection())
               .uniform("uShadowStrength", Float.intBitsToFloat(1057803469))
               .uniform("uShadowBias", var20 * Float.intBitsToFloat(1077936128))
               .uniform("uShadowSimple", 1.0F)
               .uniform("uShadowWide", this.mggcjwujybh8())
               .uniform("uGradeTint", var16)
               .uniform("uMapSunDirection", var23)
               .uniform("uCameraOffset", (float)var11.x, (float)var11.y, (float)var11.z)
               .uniform("uFogRange", var21)
               .uniform("uFogColor", var22);
         }

         var1.f1rpyknhywsf
            .render(
               var13,
               var16x -> {
                  this.m4il0c93w2kr();
                  this.mb5047hqczqz(var16x, var2, var11, var14, var15, var17, var1.width(), var1.height(), var6, var22);
                  FrustumIntersection var17x = new FrustumIntersection(var2.viewProjection());
                  ArrayList var18x = new ArrayList();

                  for (TerrainMapRenderer.Column var20x : this.f82wlg43j14b.values()) {
                     float var21x = (float)(TerrainData.columnX(var20x.key) * Double.longBitsToDouble(4625196817309499392L) - var2.focus().x);
                     float var22x = (float)(TerrainData.columnZ(var20x.key) * Double.longBitsToDouble(4625196817309499392L) - var2.focus().z);
                     if (var17x.testAab(
                        var21x,
                        (float)(var20x.minY - var2.focus().y),
                        var22x,
                        var21x + Float.intBitsToFloat(1098907648),
                        (float)(var20x.maxY - var2.focus().y),
                        var22x + Float.intBitsToFloat(1098907648)
                     )) {
                        var18x.add(var20x);
                     }
                  }

                  for (TerrainLayerData.Pass var37 : TerrainLayerData.Pass.values()) {
                     int var23x = var37 == TerrainLayerData.Pass.TRANSLUCENT ? 1 : 0;
                     var18x.sort(
                        Comparator.<TerrainMapRenderer.Column>comparingDouble(var1xx -> mh1r4c0g3nye(var1xx, var2.eye()))
                           .thenComparingLong(TerrainMapRenderer.Column::key)
                     );
                     if (var23x != 0) {
                        Collections.reverse(var18x);
                     }

                     RhiBlendStateService.PipelineHandle var24 = (var23x != 0 ? this.f3hgml1bl53u : this.f17adhgv95cp).resolve();
                     if (var24.valid()) {
                        var16x.bindPipeline(var24);
                        var16x.bindTexture(
                           new RhiBlendStateService.TextureHandle(var3.atlas()),
                           var3.mipLevels() > 1 && var37 != TerrainLayerData.Pass.CUTOUT ? this.fprq8p2g1pa : this.fhxhnkfjay1t,
                           0
                        );
                        var16x.bindTexture(new RhiBlendStateService.TextureHandle(var3.lightmap()), this.fahsk4oviegh, 1);
                        var16x.pushInt("uAtlas", 0);
                        var16x.pushInt("uLightmap", 1);
                        var16x.pushVec2("uAtlasSize", var3.width(), var3.height());
                        var16x.pushInt("uModernSampling", var3.modernSampling() ? 1 : 0);
                        var16x.pushVec3("uGradeTint", var16.x, var16.y, var16.z);
                        var16x.pushVec3("uCameraOffset", (float)var11.x, (float)var11.y, (float)var11.z);
                        var16x.pushVec3("uFogRange", var21.x, var21.y, var21.z);
                        var16x.pushVec3("uFogColor", var22.x, var22.y, var22.z);
                        var16x.bindTexture(this.fj2ns364jmcp, this.fahsk4oviegh, 3);
                        var16x.pushInt("uCoverage", 3);
                        var16x.pushVec3("uCoverageOrigin", var19.x, var19.y, var19.z);
                        var16x.pushFloat("uEdgeFog", var6 ? 1.0F : 0.0F);
                        var16x.pushFloat("uShadowBias", var20);
                        var16x.pushInt("uShadowWide", this.mggcjwujybh8());
                        var16x.pushFloat("uTime", var17);
                        var16x.pushFloat("uSparkle", var23x != 0 ? 1.0F : 0.0F);
                        var16x.pushVec3("uSunDirection", var23.x, var23.y, var23.z);
                        var16x.bindTexture(this.f7b3u1bgq5u2, this.fhxhnkfjay1t, 2);
                        var16x.pushInt("uShadowMap", 2);
                        MemoryStack var25x = MemoryStack.stackPush();

                        try {
                           var16x.pushMatrix4("uShadowMatrix", var18.viewProjection().get(var25x.mallocFloat(16)));
                        } catch (Throwable var33) {
                           if (var25x != null) {
                              try {
                                 var25x.close();
                              } catch (Throwable var31) {
                                 var33.addSuppressed(var31);
                              }
                           }

                           throw var33;
                        }

                        if (var25x != null) {
                           var25x.close();
                        }

                        var16x.pushFloat("uShadowStrength", var23x != 0 ? Float.intBitsToFloat(1058642330) : 1.0F);
                        var25x = MemoryStack.stackPush();

                        try {
                           var16x.pushMatrix4("uViewProjection", var2.viewProjection().get(var25x.mallocFloat(16)));
                        } catch (Throwable var32) {
                           if (var25x != null) {
                              try {
                                 var25x.close();
                              } catch (Throwable var30) {
                                 var32.addSuppressed(var30);
                              }
                           }

                           throw var32;
                        }

                        if (var25x != null) {
                           var25x.close();
                        }

                        for (TerrainMapRenderer.Column var26 : (Iterable<TerrainMapRenderer.Column>) (Iterable<?>) (var18x)) {
                           TerrainMapRenderer.Part var27 = var26.parts.get(var37);
                           if (var27 != null) {
                              float var28 = (float)(TerrainData.columnX(var26.key) * Double.longBitsToDouble(4625196817309499392L) - var2.focus().x);
                              float var29 = (float)(TerrainData.columnZ(var26.key) * Double.longBitsToDouble(4625196817309499392L) - var2.focus().z);
                              var16x.pushVec3("uOrigin", var28, (float)(-var2.focus().y), var29);
                              var16x.pushFloat("uAlphaCutoff", var27.cutoff);
                              var16x.bindVertexBuffer(var27.vertices, 0);
                              if (var23x != 0) {
                                 this.m7xmf5wt6wq7(
                                    var27,
                                    new Vector3d(var2.eye())
                                       .sub(
                                          TerrainData.columnX(var26.key) * Double.longBitsToDouble(4625196817309499392L),
                                          0.0,
                                          TerrainData.columnZ(var26.key) * Double.longBitsToDouble(4625196817309499392L)
                                       )
                                 );
                              }

                              var16x.bindIndexBuffer(var23x != 0 ? var27.sortedIndices : this.f91nf5yayye3, RhiBlendStateService.IndexType.UINT32);
                              var16x.drawIndexed(var27.vertexCount / 4 * 6, 1, 0);
                              this.f1ydcb4iyt0z++;
                              this.f77knp8j4m0i = this.f77knp8j4m0i + var27.vertexCount / 2;
                           }
                        }
                     }
                  }
               }
            );
         this.f73jxvy0ug2k = (System.nanoTime() - var9) / Double.longBitsToDouble(4696837146684686336L);
      }
   }

   private void m5xupgajz92z(
      TerrainViewportService.Frame var1, CompatLoadedHandler.Textures var2, TerrainFrameService.ShadowFrame var3, List<Render3dIdService> var4
   ) {
      this.m4il0c93w2kr();
      if (this.fbzg2d5f57dl.valid()) {
         RhiCommandBuffer var5 = this.fg0vqlu6cnr6.encoder();
         var5.clearScissor();
         var5.setColorWriteMask(true, true, true, true);
         var5.beginRenderPass(this.fbzg2d5f57dl);
         var5.setViewport(0, 0, this.fjnzarehzqjo, this.fjnzarehzqjo);
         var5.clearDepth(1.0F);
         RhiBlendStateService.PipelineHandle var6 = this.fbdm2drnguev.resolve();
         if (var6.valid()) {
            var5.bindPipeline(var6);
            MemoryStack var7 = MemoryStack.stackPush();

            try {
               var5.pushMatrix4("uSunVP", var3.viewProjection().get(var7.mallocFloat(16)));
            } catch (Throwable var23) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var20) {
                     var23.addSuppressed(var20);
                  }
               }

               throw var23;
            }

            if (var7 != null) {
               var7.close();
            }

            byte var24 = 0;

            for (TerrainLayerData.Pass var11 : TerrainLayerData.Pass.values()) {
               if (var11 != TerrainLayerData.Pass.TRANSLUCENT) {
                  int var12 = var11 != TerrainLayerData.Pass.SOLID ? 1 : 0;
                  var5.pushInt("uUseAtlas", var12 != 0 ? 1 : 0);
                  if (var12 != 0 && var24 == 0) {
                     var5.bindTexture(new RhiBlendStateService.TextureHandle(var2.atlas()), this.fhxhnkfjay1t, 0);
                     var5.pushInt("uAtlas", 0);
                     var24 = 1;
                  }

                  for (TerrainMapRenderer.Column var14 : this.f82wlg43j14b.values()) {
                     if (mdyyazwktd16(var14, var3, var1.focus())) {
                        TerrainMapRenderer.Part var15 = var14.parts.get(var11);
                        if (var15 != null) {
                           float var16 = (float)(TerrainData.columnX(var14.key) * Double.longBitsToDouble(4625196817309499392L) - var1.focus().x);
                           float var17 = (float)(TerrainData.columnZ(var14.key) * Double.longBitsToDouble(4625196817309499392L) - var1.focus().z);
                           var5.pushVec3("uOrigin", var16, (float)(-var1.focus().y), var17);
                           if (var12 != 0) {
                              var5.pushFloat("uAlphaCutoff", var15.cutoff);
                           }

                           var5.bindVertexBuffer(var15.vertices, 0);
                           var5.bindIndexBuffer(this.f91nf5yayye3, RhiBlendStateService.IndexType.UINT32);
                           var5.drawIndexed(var15.vertexCount / 4 * 6, 1, 0);
                           this.f1ydcb4iyt0z++;
                        }
                     }
                  }
               }
            }
         }

         if (var4 != null && !var4.isEmpty()) {
            RhiBlendStateService.PipelineHandle var25 = this.fa58xmj37wd9.resolve();
            if (var25.valid()) {
               var5.bindPipeline(var25);
               MemoryStack var26 = MemoryStack.stackPush();

               try {
                  var5.pushMatrix4("uSunVP", var3.viewProjection().get(var26.mallocFloat(16)));
               } catch (Throwable var22) {
                  if (var26 != null) {
                     try {
                        var26.close();
                     } catch (Throwable var19) {
                        var22.addSuppressed(var19);
                     }
                  }

                  throw var22;
               }

               if (var26 != null) {
                  var26.close();
               }

               Vector3d var27 = var1.focus();

               for (Render3dIdService var29 : var4) {
                  if (var29 != null && var29.visible() && !var29.mesh().closed()) {
                     var29.material().bindings().apply(var5);
                     Matrix4f var30 = var29.transform().modelMatrixRelativeTo(var27.x, var27.y, var27.z);
                     MemoryStack var31 = MemoryStack.stackPush();

                     try {
                        var5.pushMatrix4("uModel", var30.get(var31.mallocFloat(16)));
                     } catch (Throwable var21) {
                        if (var31 != null) {
                           try {
                              var31.close();
                           } catch (Throwable var18) {
                              var21.addSuppressed(var18);
                           }
                        }

                        throw var21;
                     }

                     if (var31 != null) {
                        var31.close();
                     }

                     var29.mesh().draw(var5);
                     this.f1ydcb4iyt0z++;
                  }
               }
            }
         }

         var5.endRenderPass();
      }
   }

   private static double mad6zzaddqlq(TerrainViewportService.Frame var0) {
      double var1 = var0.distance();
      if (Double.isFinite(var1) && !(var1 < Double.longBitsToDouble(4517329193108106637L))) {
         double var3 = (var0.eye().y - var0.focus().y) / var1;
         return !Double.isFinite(var3) ? 1.0 : Math.asin(Math.clamp(var3, Double.longBitsToDouble(-4616189618054758400L), 1.0));
      } else {
         return 1.0;
      }
   }

   private static boolean mdyyazwktd16(TerrainMapRenderer.Column var0, TerrainFrameService.ShadowFrame var1, Vector3d var2) {
      float var3 = (float)(
         TerrainData.columnX(var0.key) * Double.longBitsToDouble(4625196817309499392L) + Double.longBitsToDouble(4620693217682128896L) - var2.x
      );
      float var4 = (float)((var0.minY + var0.maxY) * Double.longBitsToDouble(4602678819172646912L) - var2.y);
      float var5 = (float)(
         TerrainData.columnZ(var0.key) * Double.longBitsToDouble(4625196817309499392L) + Double.longBitsToDouble(4620693217682128896L) - var2.z
      );
      Vector3f var6 = var1.view().transformPosition(var3, var4, var5, new Vector3f());
      float var7 = var1.radius() + Float.intBitsToFloat(1103101952);
      if (!(Math.abs(var6.x) > var7) && !(Math.abs(var6.y) > var7)) {
         float var8 = -var6.z;
         return var8 >= var1.near() - Float.intBitsToFloat(1109393408) && var8 <= var1.far() + Float.intBitsToFloat(1109393408);
      } else {
         return false;
      }
   }

   private void mb5047hqczqz(
      RhiCommandBuffer var1,
      TerrainViewportService.Frame var2,
      Vector3d var3,
      Vector3f var4,
      float var5,
      float var6,
      int var7,
      int var8,
      boolean var9,
      Vector3f var10
   ) {
      RhiBlendStateService.PipelineHandle var11 = this.f2lp2f7cwucn.resolve();
      if (var11.valid() && this.fg2z0o5ywm2y.valid()) {
         var1.bindPipeline(var11);
         var1.pushVec3("uSunDirection", var4.x, var4.y, var4.z);
         var1.pushFloat("uDaylight", var5);
         var1.pushFloat("uEdgeFog", var9 ? 1.0F : 0.0F);
         var1.pushVec3("uFogColor", var10.x, var10.y, var10.z);
         var1.pushFloat("uTime", var6);
         var1.pushVec2("uResolution", var7, var8);
         var1.pushVec3("uCameraOffset", (float)var3.x, (float)var3.y, (float)var3.z);
         MemoryStack var12 = MemoryStack.stackPush();

         try {
            var1.pushMatrix4("uInvViewProjection", var2.inverse().get(var12.mallocFloat(16)));
         } catch (Throwable var16) {
            if (var12 != null) {
               try {
                  var12.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }
            }

            throw var16;
         }

         if (var12 != null) {
            var12.close();
         }

         var1.bindVertexBuffer(this.fg2z0o5ywm2y, 0);
         var1.draw(3, 1, 0);
         this.f1ydcb4iyt0z++;
      }
   }

   private static double mh1r4c0g3nye(TerrainMapRenderer.Column var0, Vector3d var1) {
      double var2 = TerrainData.columnX(var0.key) * Double.longBitsToDouble(4625196817309499392L) + Double.longBitsToDouble(4620693217682128896L) - var1.x;
      double var4 = TerrainData.columnZ(var0.key) * Double.longBitsToDouble(4625196817309499392L) + Double.longBitsToDouble(4620693217682128896L) - var1.z;
      return var2 * var2 + var4 * var4;
   }

   private static float[] m74mfdqbp83o(ByteBuffer var0, int var1) {
      float[] var2 = new float[var1 / 4 * 3];

      for (int var3 = 0; var3 < var1 / 4; var3++) {
         for (int var4 = 0; var4 < 4; var4++) {
            int var5 = (var3 * 4 + var4) * 28;

            for (int var6 = 0; var6 < 3; var6++) {
               var2[var3 * 3 + var6] = var2[var3 * 3 + var6] + var0.getFloat(var5 + var6 * 4) * Float.intBitsToFloat(1048576000);
            }
         }
      }

      return var2;
   }

   private void m7xmf5wt6wq7(TerrainMapRenderer.Part var1, Vector3d var2) {
      if (var1.sortedEye == null || !(var2.distanceSquared(var1.sortedEye) < Double.longBitsToDouble(4589168020290535424L))) {
         int var3 = var1.vertexCount / 4;

         for (int var4 = 0; var4 < var3; var4++) {
            double var5 = var1.centers[var4 * 3] - var2.x;
            double var7 = var1.centers[var4 * 3 + 1] - var2.y;
            double var9 = var1.centers[var4 * 3 + 2] - var2.z;
            var1.order[var4] = (long)Float.floatToRawIntBits((float)(var5 * var5 + var7 * var7 + var9 * var9)) << 32 | var4 & 4294967295L;
         }

         Arrays.sort(var1.order);
         ByteBuffer var14 = MemoryUtil.memAlloc(var3 * 6 * 4).order(ByteOrder.nativeOrder());

         try {
            for (int var15 = var3 - 1; var15 >= 0; var15 += -1) {
               m5k0v095865k(var14, (int)var1.order[var15] * 4);
            }

            var14.flip();
            this.fg0vqlu6cnr6.orphanBuffer(var1.sortedIndices, var14.remaining());
            this.fg0vqlu6cnr6.updateBuffer(var1.sortedIndices, 0L, var14);
         } finally {
            MemoryUtil.memFree(var14);
         }

         var1.sortedEye = var2;
      }
   }

   private void m21kt3r755fm(int var1) {
      if (var1 > this.f91vy7llmgva) {
         if (this.f91nf5yayye3.valid()) {
            this.fg0vqlu6cnr6.destroyBuffer(this.f91nf5yayye3);
         }

         this.f91vy7llmgva = Integer.highestOneBit(Math.max(1024, var1 - 1)) << 1;
         ByteBuffer var2 = MemoryUtil.memAlloc(this.f91vy7llmgva * 6 * 4).order(ByteOrder.nativeOrder());

         try {
            for (int var3 = 0; var3 < this.f91vy7llmgva; var3++) {
               m5k0v095865k(var2, var3 * 4);
            }

            var2.flip();
            this.f91nf5yayye3 = this.fg0vqlu6cnr6.createBuffer(RhiBlendStateService.BufferUsage.INDEX.bit, RhiBlendStateService.BufferAccess.STATIC, var2);
         } finally {
            MemoryUtil.memFree(var2);
         }
      }
   }

   private static void m5k0v095865k(ByteBuffer var0, int var1) {
      var0.putInt(var1).putInt(var1 + 1).putInt(var1 + 2).putInt(var1 + 2).putInt(var1 + 3).putInt(var1);
   }

   private void m4il0c93w2kr() {
      if (!this.fhxhnkfjay1t.valid()) {
         this.fhxhnkfjay1t = this.fg0vqlu6cnr6.createSampler(RhiBlendStateService.SamplerDescriptor.NEAREST_CLAMP);
         this.fahsk4oviegh = this.fg0vqlu6cnr6.createSampler(RhiBlendStateService.SamplerDescriptor.LINEAR_CLAMP);
         this.fprq8p2g1pa = this.fg0vqlu6cnr6
            .createSampler(
               new RhiBlendStateService.SamplerDescriptor(
                  RhiBlendStateService.FilterMode.LINEAR_MIPMAP_LINEAR, RhiBlendStateService.FilterMode.NEAREST, RhiBlendStateService.AddressMode.CLAMP
               )
            );
         if (!this.fg2z0o5ywm2y.valid()) {
            this.fg2z0o5ywm2y = this.fg0vqlu6cnr6
               .createBuffer(
                  RhiBlendStateService.BufferUsage.VERTEX.bit,
                  RhiBlendStateService.BufferAccess.STATIC,
                  new float[]{
                     Float.intBitsToFloat(-1082130432),
                     Float.intBitsToFloat(-1082130432),
                     Float.intBitsToFloat(1077936128),
                     Float.intBitsToFloat(-1082130432),
                     Float.intBitsToFloat(-1082130432),
                     Float.intBitsToFloat(1077936128)
                  }
               );
         }

         if (!this.fbzg2d5f57dl.valid()) {
            int var1 = this.fjnzarehzqjo;
            this.fg9ercbz00bi = this.fg0vqlu6cnr6
               .createTexture(
                  new RhiBlendStateService.TextureDescriptor(
                     var1,
                     var1,
                     RhiBlendStateService.TextureFormat.R8,
                     RhiBlendStateService.FilterMode.NEAREST,
                     RhiBlendStateService.FilterMode.NEAREST,
                     RhiBlendStateService.AddressMode.CLAMP
                  )
               );
            this.f7b3u1bgq5u2 = this.fg0vqlu6cnr6
               .createTexture(
                  new RhiBlendStateService.TextureDescriptor(
                     var1,
                     var1,
                     RhiBlendStateService.TextureFormat.DEPTH24,
                     RhiBlendStateService.FilterMode.NEAREST,
                     RhiBlendStateService.FilterMode.NEAREST,
                     RhiBlendStateService.AddressMode.CLAMP
                  )
               );
            this.fbzg2d5f57dl = this.fg0vqlu6cnr6.createFramebuffer(this.fg9ercbz00bi, this.f7b3u1bgq5u2);
            this.f3h00b7hj0xb += (long)var1 * var1 * 4L;
         }
      }
   }

   private void m7xqr3pg5lhd(TerrainViewportService.Frame var1) {
      int var2 = Math.floorDiv((int)Math.floor(var1.focus().x), 16) - 32;
      int var3 = Math.floorDiv((int)Math.floor(var1.focus().z), 16) - 32;
      if (!this.fj2ns364jmcp.valid() || var2 != this.fgmrola3srr2 || var3 != this.fbs2oabg5dg3 || !this.f8oj63kb4ho9.equals(this.f77nq9owxrid)) {
         byte[] var4 = TerrainDistancesService.rgbaDistances(this.f77nq9owxrid, var2, var3);
         ByteBuffer var5 = MemoryUtil.memAlloc(var4.length);

         try {
            var5.put(var4);
            var5.flip();
            if (!this.fj2ns364jmcp.valid()) {
               this.fj2ns364jmcp = this.fg0vqlu6cnr6
                  .createTexture(
                     new RhiBlendStateService.TextureDescriptor(
                        65,
                        65,
                        RhiBlendStateService.TextureFormat.RGBA8,
                        RhiBlendStateService.FilterMode.LINEAR,
                        RhiBlendStateService.FilterMode.LINEAR,
                        RhiBlendStateService.AddressMode.CLAMP
                     ),
                     var5
                  );
               this.f3h00b7hj0xb += 16900L;
            } else {
               this.fg0vqlu6cnr6.updateTexture(this.fj2ns364jmcp, 0, 0, 65, 65, var5);
            }

            this.f8oj63kb4ho9 = this.f77nq9owxrid;
            this.fgmrola3srr2 = var2;
            this.fbs2oabg5dg3 = var3;
         } finally {
            MemoryUtil.memFree(var5);
         }
      }
   }

   private void mfon5mbvwtww(TerrainMapRenderer.Column var1) {
      for (TerrainMapRenderer.Part var3 : var1.parts.values()) {
         this.fg0vqlu6cnr6.destroyBuffer(var3.vertices);
         if (var3.sortedIndices.valid()) {
            this.fg0vqlu6cnr6.destroyBuffer(var3.sortedIndices);
         }

         this.f3h00b7hj0xb = this.f3h00b7hj0xb - (var3.vertexCount * 28L + (var3.sortedIndices.valid() ? var3.vertexCount / 4L * 24L : 0L));
      }
   }

   public void clear() {
      this.f77nq9owxrid = Set.of();
      this.f8oj63kb4ho9 = Set.of();
      this.fgmrola3srr2 = Integer.MIN_VALUE;
      if (this.f82wlg43j14b.isEmpty()) {
         this.fbefuu78f3da.clear();
      } else {
         this.mirv5tsxkgk6(() -> {
            for (TerrainMapRenderer.Column var2 : this.f82wlg43j14b.values()) {
               this.mfon5mbvwtww(var2);
            }

            this.f82wlg43j14b.clear();
            this.fbefuu78f3da.clear();
         });
      }
   }

   public long gpuBytes() {
      return this.f3h00b7hj0xb + this.f91vy7llmgva * 24L;
   }

   public int drawCalls() {
      return this.f1ydcb4iyt0z;
   }

   public int triangles() {
      return this.f77knp8j4m0i;
   }

   public double uploadMillis() {
      return this.f864owsryhew;
   }

   public double renderMillis() {
      return this.f73jxvy0ug2k;
   }

   private void mirv5tsxkgk6(Runnable var1) {
      if (this.fg0vqlu6cnr6.encoder() instanceof GlRenderer var2) {
         var2.saveGLState();

         try {
            var1.run();
         } finally {
            var2.restoreGLState();
         }
      } else {
         var1.run();
      }
   }

   @Override
   public void close() {
      this.clear();
      Runnable var1 = () -> {
         if (this.f91nf5yayye3.valid()) {
            this.fg0vqlu6cnr6.destroyBuffer(this.f91nf5yayye3);
         }

         if (this.fg2z0o5ywm2y.valid()) {
            this.fg0vqlu6cnr6.destroyBuffer(this.fg2z0o5ywm2y);
         }

         if (this.fbzg2d5f57dl.valid()) {
            this.fg0vqlu6cnr6.destroyFramebuffer(this.fbzg2d5f57dl);
         }

         if (this.f7b3u1bgq5u2.valid()) {
            this.fg0vqlu6cnr6.destroyTexture(this.f7b3u1bgq5u2);
         }

         if (this.fg9ercbz00bi.valid()) {
            this.fg0vqlu6cnr6.destroyTexture(this.fg9ercbz00bi);
         }

         if (this.fj2ns364jmcp.valid()) {
            this.fg0vqlu6cnr6.destroyTexture(this.fj2ns364jmcp);
         }

         this.fj2ns364jmcp = RhiBlendStateService.TextureHandle.NONE;
         this.f17adhgv95cp.close();
         this.f3hgml1bl53u.close();
         this.f2lp2f7cwucn.close();
         this.fbdm2drnguev.close();
         this.fa58xmj37wd9.close();
         if (this.fhxhnkfjay1t.valid()) {
            this.fg0vqlu6cnr6.destroySampler(this.fhxhnkfjay1t);
         }

         if (this.fprq8p2g1pa.valid()) {
            this.fg0vqlu6cnr6.destroySampler(this.fprq8p2g1pa);
         }

         if (this.fahsk4oviegh.valid()) {
            this.fg0vqlu6cnr6.destroySampler(this.fahsk4oviegh);
         }

         this.f91nf5yayye3 = RhiBlendStateService.BufferHandle.NONE;
         this.fg2z0o5ywm2y = RhiBlendStateService.BufferHandle.NONE;
         this.fbzg2d5f57dl = RhiBlendStateService.FramebufferHandle.NONE;
         this.fg9ercbz00bi = this.f7b3u1bgq5u2 = RhiBlendStateService.TextureHandle.NONE;
         this.fhxhnkfjay1t = this.fprq8p2g1pa = this.fahsk4oviegh = RhiBlendStateService.SamplerHandle.NONE;
         this.f91vy7llmgva = 0;
      };
      if (!this.f91nf5yayye3.valid()
         && !this.fg2z0o5ywm2y.valid()
         && !this.fbzg2d5f57dl.valid()
         && !this.fhxhnkfjay1t.valid()
         && !this.fprq8p2g1pa.valid()
         && !this.fahsk4oviegh.valid()) {
         var1.run();
      } else {
         this.mirv5tsxkgk6(var1);
      }
   }

   private record Column(long key, int minY, int maxY, Map<TerrainLayerData.Pass, TerrainMapRenderer.Part> parts, Set<TerrainData> sections) {
   }

   private static final class Part {
      RhiBlendStateService.BufferHandle vertices;
      RhiBlendStateService.BufferHandle sortedIndices = RhiBlendStateService.BufferHandle.NONE;
      int vertexCount;
      float cutoff;
      float[] centers;
      Vector3d sortedEye;
      long[] order;
   }

   public static final class Target {
      private final Render3dRenderer f1rpyknhywsf;

      private Target(Render3dRenderer var1) {
         this.f1rpyknhywsf = var1;
      }

      public RhiBlendStateService.TextureHandle texture() {
         return this.f1rpyknhywsf.texture();
      }

      public int width() {
         return this.f1rpyknhywsf.width();
      }

      public int height() {
         return this.f1rpyknhywsf.height();
      }

      public Render3dRenderer view() {
         return this.f1rpyknhywsf;
      }

      public boolean needsRender() {
         return !this.f1rpyknhywsf.hasRenderedFrame();
      }
   }
}
