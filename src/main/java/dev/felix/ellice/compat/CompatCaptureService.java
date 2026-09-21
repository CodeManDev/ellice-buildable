package dev.felix.ellice.compat;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felix.ellice.mixin.MapRenderTypeAccessor;
import java.util.List;
import java.util.Locale;
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
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.LeashState;
import net.minecraft.client.renderer.entity.state.EntityRenderState.ShadowPiece;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.item.ItemStackRenderState.FoilType;
import net.minecraft.client.renderer.rendertype.RenderSetup.TextureAndSampler;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CompatCaptureService {
  private static final Logger logger = LoggerFactory.getLogger(CompatCaptureService.class);

  private CompatCaptureService() {}

  public static List<CompatLoadedHandler.PlayerPart> capture(Player currentPlayer, float value) {
    try {
      Minecraft minecraft = Minecraft.getInstance();
      EntityRenderer entityRenderer =
          minecraft.getEntityRenderDispatcher().getRenderer(currentPlayer);
      EntityRenderState entityRenderState = entityRenderer.createRenderState(currentPlayer, value);
      entityRenderState.nameTag = null;
      Vec3 vec3 = entityRenderer.getRenderOffset(entityRenderState);
      PoseStack poseStack = new PoseStack();
      poseStack.translate(vec3.x, vec3.y, vec3.z);
      CompatSinkService compatSink = new CompatSinkService();
      CameraRenderState cameraRenderState = new CameraRenderState();
      cameraRenderState.pos = currentPlayer.position();
      cameraRenderState.orientation = new Quaternionf();
      entityRenderer.submit(
          entityRenderState,
          poseStack,
          new CompatCaptureService.Collector(compatSink),
          cameraRenderState);
      return compatSink.finish();
    } catch (RuntimeException exception) {
      logger.debug("Could not capture map player model", exception);
      return List.of();
    }
  }

  private static int calculateValue(RenderType renderType) {
    if (renderType.toString().toLowerCase(Locale.ROOT).contains("glint")) {
      return 0;
    }

    TextureAndSampler textureAndSampler =
        (TextureAndSampler)
            ((MapRenderTypeAccessor) renderType).ellice$mapState().getTextures().get("Sampler0");
    return textureAndSampler != null
            && textureAndSampler.textureView().texture() instanceof GlTexture glTexture
        ? glTexture.glId()
        : 0;
  }

  private static final class Collector implements SubmitNodeCollector {
    private final CompatSinkService compatSinkService;

    Collector(CompatSinkService compatSink) {
      this.compatSinkService = compatSink;
    }

    private VertexConsumer createVertexConsumer(
        RenderType renderType, TextureAtlasSprite textureAtlasSprite) {
      VertexConsumer vertexConsumer =
          this.compatSinkService.sink(CompatCaptureService.calculateValue(renderType));
      return textureAtlasSprite == null ? vertexConsumer : textureAtlasSprite.wrap(vertexConsumer);
    }

    private void updateState(Pose currentPose, BakedQuad bakedQuad, int[] ints) {
      int index = bakedQuad.materialInfo().tintIndex();
      int currentLength = index >= 0 && index < ints.length ? ints[index] : -1;
      VertexConsumer vertexConsumer =
          this.compatSinkService.sink(
              CompatCaptureService.calculateValue(bakedQuad.materialInfo().itemRenderType()));
      Vector3f vector3f =
          new Vector3f(
              bakedQuad.direction().getStepX(),
              bakedQuad.direction().getStepY(),
              bakedQuad.direction().getStepZ());
      currentPose.normal().transform(vector3f).normalize();

      for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
        Vector3f currentVector3f =
            currentPose.pose().transformPosition(new Vector3f(bakedQuad.position(currentIndex)));
        vertexConsumer.addVertex(
            currentVector3f.x,
            currentVector3f.y,
            currentVector3f.z,
            currentLength,
            UVPair.unpackU(bakedQuad.packedUV(currentIndex)),
            UVPair.unpackV(bakedQuad.packedUV(currentIndex)),
            0,
            15728880,
            vector3f.x,
            vector3f.y,
            vector3f.z);
      }
    }

    public OrderedSubmitNodeCollector order(int value) {
      return this;
    }

    public void submitShadow(PoseStack poseStack, float value, List<ShadowPiece> items) {}

    public void submitNameTag(
        PoseStack poseStack,
        Vec3 vec3,
        int value,
        Component component,
        boolean enabled,
        int currentValue,
        double doubleValue,
        CameraRenderState cameraRenderState) {}

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
        int targetValue) {}

    public void submitFlame(
        PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {}

    public void submitLeash(PoseStack poseStack, LeashState leashState) {}

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
        CrumblingOverlay crumblingOverlay) {
      model.setupAnim(s);
      model.renderToBuffer(
          poseStack,
          this.createVertexConsumer(renderType, textureAtlasSprite),
          value,
          currentValue,
          nextValue);
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
        int previousValue) {
      modelPart.render(
          poseStack,
          this.createVertexConsumer(renderType, textureAtlasSprite),
          value,
          currentValue,
          nextValue);
    }

    public void submitMovingBlock(
        PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {}

    public void submitBlockModel(
        PoseStack poseStack,
        RenderType renderType,
        List<BlockStateModelPart> items,
        int[] ints,
        int value,
        int currentValue,
        int nextValue) {}

    public void submitBreakingBlockModel(
        PoseStack poseStack, BlockStateModel blockStateModel, long longValue, int value) {}

    public void submitItem(
        PoseStack poseStack,
        ItemDisplayContext itemDisplayContext,
        int value,
        int currentValue,
        int nextValue,
        int[] ints,
        List<BakedQuad> items,
        FoilType foilType) {
      for (BakedQuad bakedQuad : items) {
        this.updateState(poseStack.last(), bakedQuad, ints);
      }
    }

    public void submitCustomGeometry(
        PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
      customGeometryRenderer.render(
          poseStack.last(),
          this.compatSinkService.sink(CompatCaptureService.calculateValue(renderType)));
    }

    public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {}
  }
}
