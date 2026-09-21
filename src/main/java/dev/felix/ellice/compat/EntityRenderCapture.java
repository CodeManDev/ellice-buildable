package dev.felix.ellice.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import dev.felix.ellice.event.EventAttackInputService;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector.CustomGeometryRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector.ParticleGroupRenderer;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.LeashState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.ShadowPiece;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.item.ItemStackRenderState.FoilType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3fc;

public final class EntityRenderCapture {
   private EntityRenderCapture() {
   }

   public static void capture(EspTargetCapture.Target target, EventAttackInputService.WorldRender worldRender) {
      CompatListenService.Ticket ticket = CompatListenService.request(target.id(), target.entity() == null ? -1 : target.entity().getId());
      if (ticket != null) {
         Minecraft minecraft = Minecraft.getInstance();
         EntityRenderCapture.Mesh mesh = new EntityRenderCapture.Mesh();
         EntityRenderCapture.Collector collector = new EntityRenderCapture.Collector(mesh);
         CameraRenderState cameraRenderState = new CameraRenderState();
         cameraRenderState.pos = worldRender.cameraPos();
         cameraRenderState.orientation = new Quaternionf();
         if (target.entity() != null) {
            Entity currentEntity = target.entity();
            EntityRenderer entityRenderer = minecraft.getEntityRenderDispatcher().getRenderer(currentEntity);
            EntityRenderState entityRenderState = entityRenderer.createRenderState(currentEntity, worldRender.tickDelta());
            entityRenderState.nameTag = null;
            Vec3 vec3 = entityRenderer.getRenderOffset(entityRenderState);
            PoseStack poseStack = new PoseStack();
            poseStack.translate(
               entityRenderState.x - worldRender.cameraPos().x + vec3.x, entityRenderState.y - worldRender.cameraPos().y + vec3.y, entityRenderState.z - worldRender.cameraPos().z + vec3.z
            );
            entityRenderer.submit(entityRenderState, poseStack, collector, cameraRenderState);
         } else {
            for (BlockEntity blockEntity : target.blocks()) {
               BlockPos blockPos = blockEntity.getBlockPos();
               PoseStack currentPoseStack = new PoseStack();
               currentPoseStack.translate(blockPos.getX() - worldRender.cameraPos().x, blockPos.getY() - worldRender.cameraPos().y, blockPos.getZ() - worldRender.cameraPos().z);
               BlockEntityRenderer blockEntityRenderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
               if (blockEntityRenderer != null) {
                  BlockEntityRenderState blockEntityRenderState = blockEntityRenderer.createRenderState();
                  blockEntityRenderer.extractRenderState(blockEntity, blockEntityRenderState, worldRender.tickDelta(), worldRender.cameraPos(), null);
                  blockEntityRenderer.submit(blockEntityRenderState, currentPoseStack, collector, cameraRenderState);
               } else {
                  BlockStateModel blockStateModel = minecraft.getModelManager().getBlockStateModelSet().get(blockEntity.getBlockState());
                  ArrayList arrayList = new ArrayList();
                  blockStateModel.collectParts(RandomSource.create(blockEntity.getBlockState().getSeed(blockPos)), arrayList);

                  for (BlockStateModelPart blockStateModelPart : (Iterable<BlockStateModelPart>) (Iterable<?>) (arrayList)) {
                     for (Direction direction : Direction.values()) {
                        for (BakedQuad bakedQuad : blockStateModelPart.getQuads(direction)) {
                           mesh.quad(currentPoseStack.last(), bakedQuad);
                        }
                     }

                     for (BakedQuad currentBakedQuad : blockStateModelPart.getQuads(null)) {
                        mesh.quad(currentPoseStack.last(), currentBakedQuad);
                     }
                  }
               }
            }
         }

         ticket.publish(mesh.triangles(), new float[0]);
      }
   }

   private static final class Collector implements SubmitNodeCollector {
      private final EntityRenderCapture.Mesh mesh;

      Collector(EntityRenderCapture.Mesh currentMesh) {
         this.mesh = currentMesh;
      }

      public OrderedSubmitNodeCollector order(int value) {
         return this;
      }

      public void submitShadow(PoseStack poseStack, float value, List<ShadowPiece> items) {
      }

      public void submitNameTag(
         PoseStack poseStack, Vec3 vec3, int value, Component component, boolean enabled, int currentValue, double doubleValue, CameraRenderState cameraRenderState
      ) {
      }

      public void submitText(
         PoseStack poseStack,
         float value,
         float currentValue,
         FormattedCharSequence formattedCharSequence,
         boolean enabled,
         DisplayMode displayMode,
         int nextValue,
         int previousValue,
         int sourceValue,
         int targetValue
      ) {
      }

      public void submitFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
      }

      public void submitLeash(PoseStack poseStack, LeashState leashState) {
      }

      public <S> void submitModel(
         Model<? super S> model,
         S s,
         PoseStack poseStack,
         RenderType renderType,
         int value,
         int currentValue,
         int nextValue,
         TextureAtlasSprite textureAtlasSprite,
         int previousValue,
         CrumblingOverlay crumblingOverlay
      ) {
         model.setupAnim(s);
         model.renderToBuffer(poseStack, this.mesh, value, currentValue, nextValue);
      }

      public void submitModelPart(
         ModelPart modelPart,
         PoseStack poseStack,
         RenderType renderType,
         int value,
         int currentValue,
         TextureAtlasSprite textureAtlasSprite,
         boolean enabled,
         boolean currentEnabled,
         int nextValue,
         CrumblingOverlay crumblingOverlay,
         int previousValue
      ) {
         modelPart.render(poseStack, this.mesh, value, currentValue);
      }

      public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
      }

      public void submitBlockModel(
         PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> items, int[] ints, int value, int currentValue, int nextValue
      ) {
         for (BlockStateModelPart blockStateModelPart : items) {
            for (Direction direction : Direction.values()) {
               for (BakedQuad bakedQuad : blockStateModelPart.getQuads(direction)) {
                  this.mesh.quad(poseStack.last(), bakedQuad);
               }
            }

            for (BakedQuad currentBakedQuad : blockStateModelPart.getQuads(null)) {
               this.mesh.quad(poseStack.last(), currentBakedQuad);
            }
         }
      }

      public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel blockStateModel, long longValue, int value) {
      }

      public void submitItem(
         PoseStack poseStack, ItemDisplayContext itemDisplayContext, int value, int currentValue, int nextValue, int[] ints, List<BakedQuad> items, FoilType foilType
      ) {
         for (BakedQuad bakedQuad : items) {
            this.mesh.quad(poseStack.last(), bakedQuad);
         }
      }

      public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
      }

      public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
      }
   }

   private static final class Mesh extends TriangleVertexCollector {
      void quad(Pose currentPose, BakedQuad bakedQuad) {
         float[] floats = new float[12];
         float[] currentFloats = new float[8];

         for (int index = 0; index < 4; index++) {
            Vector3fc vector3fc = bakedQuad.position(index);
            floats[index * 3] = vector3fc.x();
            floats[index * 3 + 1] = vector3fc.y();
            floats[index * 3 + 2] = vector3fc.z();
            currentFloats[index * 2] = UVPair.unpackU(bakedQuad.packedUV(index));
            currentFloats[index * 2 + 1] = UVPair.unpackV(bakedQuad.packedUV(index));
         }

         this.transformed(
            CompatTrianglesService.triangles(
               bakedQuad, floats, currentFloats, bakedQuad.materialInfo().sprite(), bakedQuad.materialInfo().sprite().contents().getUniqueFrames().getInt(0)
            ),
            currentPose.pose()
         );
      }
   }
}
