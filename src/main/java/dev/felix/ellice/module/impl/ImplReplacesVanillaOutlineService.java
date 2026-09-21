package dev.felix.ellice.module.impl;

import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.world.WorldData;
import dev.felix.ellice.render.world.WorldRenderer;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.scene.SceneSelectSelector;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ImplReplacesVanillaOutlineService extends ModuleSettingsService {
  private final ModuleSetting.Mode moduleMode =
      this.setting(
          new ModuleSetting.Mode("Material", new String[] {"Clean", "Glass", "ellice"}, "Glass")
              .description(
                  "Clean keeps the highlight quiet. Glass adds soft face lighting; ellice adds a"
                      + " slow two-tone shimmer."));
  private final ModuleSetting.Bool moduleBool =
      this.setting(
          new ModuleSetting.Bool("Theme Colors", true)
              .description(
                  "Uses your client theme for the highlight and shimmer while keeping the"
                      + " configured opacity."));
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(
          new ModuleSetting.Bool("Animate Selection", true)
              .description(
                  "Softly crossfades between exact block shapes without moving the highlight"
                      + " through terrain."));
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(
          new ModuleSetting.Bool("Fill", true)
              .description(
                  "Adds a subtle translucent material to the exposed faces of the selected"
                      + " shape."));
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(
          new ModuleSetting.Bool("Outline", true)
              .description(
                  "Draws smooth, consistent-width outer edges, including stairs, fences and"
                      + " slabs."));
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          new ModuleSetting.Bool("See Through", false)
              .description(
                  "Disables depth testing so the selected-block overlay remains visible behind"
                      + " terrain."));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          new ModuleSetting.Number("Padding", 0.002F, 0.0F, 0.02F, 0.001F)
              .description(
                  "Expands every shape face by this fraction of a block to prevent surface"
                      + " flicker."));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          new ModuleSetting.Number("Line Width", 1.5F, 0.5F, 4.0F, 0.1F)
              .description(
                  "Outline width in screen pixels; remains readable as the camera moves."));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          new ModuleSetting.Number("Edge Glow", 0.35F, 0.0F, 1.0F, 0.05F)
              .description(
                  "Adds a restrained soft halo around the contour. Zero keeps the edge crisp."));
  private final ModuleSetting.Color moduleColor =
      this.setting(
          new ModuleSetting.Color("Fill Color", 679334908)
              .description(
                  "Sets the tint and alpha of selected-block faces when Fill is enabled."));
  private final ModuleSetting.Color moduleColor2 =
      this.setting(
          new ModuleSetting.Color("Outline Color", -427961348)
              .description(
                  "Sets the tint and alpha of selected-block edges when Outline is enabled."));
  private final ModuleSetting.Color moduleColor3 =
      this.setting(
          new ModuleSetting.Color("Shimmer Color", -1657606)
              .description("Second color of the ellice material when Theme Colors is disabled."));
  private final WorldRenderer renderer = new WorldRenderer();
  private final SceneSelectSelector<ImplReplacesVanillaOutlineService.Target> sceneSelectSelector =
      new SceneSelectSelector<>();
  private List<AABB> items = List.of();
  private WorldData worldData;
  private ClientLevel client2;
  private long timestamp;
  private long timestamp2;
  private boolean enabled2;

  public ImplReplacesVanillaOutlineService() {
    super(
        ModuleBuilderData.builder("Block Overlay")
            .description(
                "A polished block selection with smooth contours, glass lighting and theme-aware"
                    + " shader materials.")
            .category(ModuleFeatureType.VISUALS)
            .build());
  }

  @Override
  protected void onEnable() {
    this.enabled2 = false;
    this.timestamp = 0L;
    this.timestamp2 = System.nanoTime();
    this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState);
  }

  @Override
  protected void onDisable() {
    this.renderer.close();
    this.sceneSelectSelector.clear();
    this.items = List.of();
    this.worldData = null;
    this.client2 = null;
    this.timestamp = 0L;
  }

  public boolean replacesVanillaOutline() {
    return this.isEnabled()
        && !this.enabled2
        && ((Boolean) this.moduleBool3.get() || (Boolean) this.moduleBool4.get());
  }

  private void updateState(EventAttackInputService.WorldRender worldRender) {
    Minecraft minecraft = CoreIsInitializedHandler.mc();
    ClientLevel clientLevel = minecraft.level;
    if (clientLevel != this.client2) {
      this.sceneSelectSelector.clear();
      this.client2 = clientLevel;
      this.worldData = null;
      this.items = List.of();
    }

    if (clientLevel != null && !this.enabled2) {
      ImplReplacesVanillaOutlineService.Target currentTarget = null;
      if (!minecraft.options.hideGui
          && minecraft.hitResult instanceof BlockHitResult blockHitResult
          && blockHitResult.getType() == Type.BLOCK) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = clientLevel.getBlockState(blockPos);
        if (!blockState.isAir()) {
          VoxelShape voxelShape = blockState.getShape(clientLevel, blockPos);
          if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
          }

          List currentItems = voxelShape.toAabbs();
          if (this.worldData == null || !this.items.equals(currentItems)) {
            this.items = currentItems;
            this.worldData = WorldData.of(voxelShape);
          }

          currentTarget =
              new ImplReplacesVanillaOutlineService.Target(blockPos.immutable(), this.worldData);
        }
      }

      long offset = System.nanoTime();
      float value =
          this.timestamp == 0L
              ? 0.016666668F
              : Math.clamp((float) (offset - this.timestamp) / 1.0E9F, 0.0F, 1.0F);
      this.timestamp = offset;
      this.sceneSelectSelector.select(currentTarget, (Boolean) this.moduleBool2.get());
      List nextItems =
          this.sceneSelectSelector.advance(value).stream()
              .map(
                  item ->
                      new WorldRenderer.Selection(
                          item.target().mesh(),
                          Vec3.atLowerCornerOf(item.target().position()),
                          item.opacity()))
              .toList();
      int currentValue =
          this.moduleBool.get()
              ? (Integer) this.moduleColor.get() & 0xFF000000
                  | MaterialIsLightService.PRIMARY & 16777215
              : (Integer) this.moduleColor.get();
      int nextValue =
          this.moduleBool.get()
              ? (Integer) this.moduleColor2.get() & 0xFF000000
                  | MaterialIsLightService.PRIMARY & 16777215
              : (Integer) this.moduleColor2.get();
      WorldRenderer.Style style =
          new WorldRenderer.Style(
              currentValue,
              nextValue,
              this.moduleBool.get()
                  ? MaterialIsLightService.TERTIARY
                  : (Integer) this.moduleColor3.get(),
              (Float) this.moduleNumber2.get(),
              "Clean".equals(this.moduleMode.get()) ? 0.0F : (Float) this.moduleNumber3.get(),
              (Float) this.moduleNumber.get(),
              (Boolean) this.moduleBool3.get(),
              (Boolean) this.moduleBool4.get(),
              (Boolean) this.moduleBool5.get(),
              "ellice".equals(this.moduleMode.get())
                  ? 2
                  : ("Glass".equals(this.moduleMode.get()) ? 1 : 0));

      try {
        this.renderer.render(
            nextItems,
            worldRender,
            minecraft.getWindow().getWidth(),
            minecraft.getWindow().getHeight(),
            style,
            (float) (offset - this.timestamp2) / 1.0E9F);
      } catch (RuntimeException exception) {
        this.enabled2 = true;
        this.renderer.close();
        CoreIsInitializedHandler.LOGGER.error(
            "Block selection shader failed; restoring Vanilla outline", exception);
      }
    }
  }

  private record Target(BlockPos position, WorldData mesh) {}
}
