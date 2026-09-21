package dev.felix.ellice.compat.adapter.mc26_1;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.felix.ellice.compat.CompatInstanceIdentityService;
import dev.felix.ellice.compat.CompatLoadedHandler;
import dev.felix.ellice.feature.terrain.TerrainData;
import dev.felix.ellice.feature.terrain.TerrainLayerData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.SectionCompiler.Results;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class Mc261SnapshotService extends CompatInstanceIdentityService {
   private SectionBufferBuilderPack sectionBufferBuilderPack;

   @Override
   public CompatLoadedHandler.BuildJob snapshot(TerrainData terrainData) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         return null;
      }

      SectionPos currentX = SectionPos.of(terrainData.x(), terrainData.y(), terrainData.z());
      RenderSectionRegion renderSectionRegion = new RenderRegionCache().createRegion(minecraft.level, currentX.asLong());
      if (renderSectionRegion == null) {
         return () -> new TerrainLayerData(terrainData, List.of());
      }

      ModelManager modelManager = minecraft.getModelManager();
      SectionCompiler sectionCompiler = new SectionCompiler(
         (Boolean)minecraft.options.ambientOcclusion().get(),
         (Boolean)minecraft.options.cutoutLeaves().get(),
         modelManager.getBlockStateModelSet(),
         modelManager.getFluidStateModelSet(),
         minecraft.getBlockColors(),
         minecraft.getBlockEntityRenderDispatcher()
      );
      return () -> {
         if (this.sectionBufferBuilderPack == null) {
            this.sectionBufferBuilderPack = new SectionBufferBuilderPack();
         }

         Results results = null;

         try {
            results = sectionCompiler.compile(currentX, renderSectionRegion, VertexSorting.byDistance(0.0F, 0.0F, 0.0F), this.sectionBufferBuilderPack);
            ArrayList arrayList = new ArrayList();

            for (Entry entry : results.renderedLayers.entrySet()) {
               TerrainLayerData.Pass pass = switch ((ChunkSectionLayer)entry.getKey()) {
                  case SOLID -> TerrainLayerData.Pass.SOLID;
                  case CUTOUT -> TerrainLayerData.Pass.CUTOUT_MIPPED;
                  case TRANSLUCENT -> TerrainLayerData.Pass.TRANSLUCENT;
                  default -> throw new MatchException(null, null);
               };
               MeshData meshData = (MeshData)entry.getValue();
               String text = ((ChunkSectionLayer)entry.getKey()).pipeline().getShaderDefines().values().getOrDefault("ALPHA_CUTOUT", "0");
               arrayList.add(
                  TerrainLayerData.copy(
                     pass,
                     meshData.vertexBuffer(),
                     meshData.drawState().format().getVertexSize(),
                     meshData.drawState().vertexCount(),
                     Float.parseFloat(text)
                  )
               );
            }

            return new TerrainLayerData(terrainData, arrayList);
         } finally {
            if (results != null) {
               results.release();
            }

            this.sectionBufferBuilderPack.discardAll();
         }
      };
   }

   @Override
   public CompatLoadedHandler.Textures textures() {
      Minecraft minecraft = Minecraft.getInstance();
      GpuTexture gpuTexture = minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).getTexture();
      GpuTexture currentGpuTexture = minecraft.gameRenderer.levelLightmap().texture();
      return gpuTexture instanceof GlTexture glTexture && currentGpuTexture instanceof GlTexture currentGlTexture
         ? new CompatLoadedHandler.Textures(glTexture.glId(), currentGlTexture.glId(), gpuTexture.getWidth(0), gpuTexture.getHeight(0), gpuTexture.getMipLevels(), true)
         : null;
   }

   @Override
   public void closeWorker() {
      if (this.sectionBufferBuilderPack != null) {
         this.sectionBufferBuilderPack.close();
         this.sectionBufferBuilderPack = null;
      }
   }

   @Override
   protected CompatInstanceIdentityService.SkinInfo skinInfo(Player player) {
      try {
         if (player instanceof AbstractClientPlayer abstractClientPlayer) {
            PlayerSkin playerSkin = abstractClientPlayer.getSkin();
            AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(playerSkin.body().texturePath());
            return abstractTexture != null && abstractTexture.getTexture() instanceof GlTexture glTexture
               ? new CompatInstanceIdentityService.SkinInfo(glTexture.glId(), playerSkin.model() == PlayerModelType.SLIM, glTexture.getHeight(0) < 64)
               : new CompatInstanceIdentityService.SkinInfo(-1, false, false);
         } else {
            return new CompatInstanceIdentityService.SkinInfo(-1, false, false);
         }
      } catch (Exception exception) {
         return new CompatInstanceIdentityService.SkinInfo(-1, false, false);
      }
   }
}
