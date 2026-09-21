package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.EntityRenderCapture;
import dev.felix.ellice.compat.EspTargetCapture;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.feature.esp.EspData;
import dev.felix.ellice.feature.esp.EspLabelData;
import dev.felix.ellice.feature.esp.EspPlaceService;
import dev.felix.ellice.feature.esp.EspPrepareService;
import dev.felix.ellice.feature.esp.EspProjectService;
import dev.felix.ellice.feature.esp.EspRenderer;
import dev.felix.ellice.feature.nametags.NametagsPrepareService;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.render.render3d.ElliceEffectSettings;
import dev.felix.ellice.render.render3d.FireEffectSettings;
import dev.felix.ellice.render.render3d.InterferenceEffectSettings;
import dev.felix.ellice.render.render3d.KawaseBloomSettings;
import dev.felix.ellice.render.render3d.OutlineEffectSettings;
import dev.felix.ellice.render.render3d.PlayerMotionSample;
import dev.felix.ellice.render.render3d.Render3dData;
import dev.felix.ellice.render.render3d.Render3dFeatureType;
import dev.felix.ellice.render.render3d.Render3dSceneService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.ScenePctService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class EffectsComponent extends ModuleSettingsService {
  private static final double value = 48.0;
  private static final double value2 = 24.0;
  private static final double value3 = 15.0;
  private final ModuleNameService moduleNameService =
      this.settingCategory("Effects")
          .description("Combine projected boxes and silhouette effects.");
  private final ModuleNameService moduleNameService2 =
      this.settingCategory("Damage")
          .description(
              "Temporarily tints every shader on a hurt opponent; fades with the actual hurt"
                  + " animation.");
  private final ModuleSetting.Bool moduleBool =
      this.setting(this.moduleNameService2, new ModuleSetting.Bool("Damage Tint", true));
  private final ModuleSetting.Color moduleColor =
      this.setting(
          this.moduleNameService2,
          new ModuleSetting.Color("Damage Color", -40324)
              .description(
                  "Hit color for living opponents. Color alpha controls the blend strength."));
  private final ModuleNameService moduleNameService3 =
      this.settingCategory("Targets").description("Shared target selection for every effect.");
  private final ModuleNameService moduleNameService4 = this.settingCategory("2D Boxes");
  private final ModuleNameService moduleNameService5 =
      this.settingCategory(this.moduleNameService4, "Details");
  private final ModuleNameService moduleNameService6 =
      this.settingCategory(this.moduleNameService4, "Visibility");
  private final ModuleSetting.Bool moduleBool2 =
      this.setting(this.moduleNameService, new ModuleSetting.Bool("2D Boxes", false));
  private final ModuleNameService moduleNameService7 =
      this.settingCategory("Glow")
          .description(
              "Compact layered light: a pearl rim, saturated edge, soft halo and fine outer"
                  + " falloff.");
  private final ModuleSetting.Bool moduleBool3 =
      this.setting(this.moduleNameService, new ModuleSetting.Bool("Glow", false));
  private final ModuleSetting.Color moduleColor2 =
      this.setting(this.moduleNameService7, new ModuleSetting.Color("Glow Color", -10308097));
  private final ModuleSetting.Color moduleColor3 =
      this.setting(
          this.moduleNameService7, new ModuleSetting.Color("Glow Occluded Color", -225600513));
  private final ModuleSetting.Number moduleNumber =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Number("Glow Radius", 6.0F, 3.0F, 16.0F, 0.5F));
  private final ModuleSetting.Number moduleNumber2 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Number("Glow Intensity", 1.35F, 0.0F, 3.0F, 0.05F));
  private final ModuleSetting.Number moduleNumber3 =
      this.setting(
          this.moduleNameService7,
          new ModuleSetting.Number("Glow Highlights", 0.65F, 0.0F, 1.0F, 0.05F)
              .description(
                  "Pearlescent highlights along the inner edge, above the colored glow layers."));
  private final ModuleSetting.Mode moduleMode =
      this.setting(this.moduleNameService7, createMode("Glow Visibility Mode"));
  private final ModuleNameService moduleNameService8 =
      this.settingCategory("ellice")
          .description("Faceted light and thin-film iridescence along the model contour.");
  private final ModuleSetting.Bool moduleBool4 =
      this.setting(this.moduleNameService, new ModuleSetting.Bool("ellice", false));
  private final ModuleSetting.Color moduleColor4 =
      this.setting(this.moduleNameService8, new ModuleSetting.Color("ellice Color A", -8785921));
  private final ModuleSetting.Color moduleColor5 =
      this.setting(this.moduleNameService8, new ModuleSetting.Color("ellice Color B", -4090369));
  private final ModuleSetting.Color moduleColor6 =
      this.setting(
          this.moduleNameService8, new ModuleSetting.Color("ellice Occluded Color", -273772289));
  private final ModuleSetting.Number moduleNumber4 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("ellice Width", 9.0F, 3.0F, 32.0F, 0.5F));
  private final ModuleSetting.Number moduleNumber5 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("ellice Speed", 0.35F, 0.0F, 2.0F, 0.05F));
  private final ModuleSetting.Number moduleNumber6 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("ellice Facets", 3.5F, 0.5F, 5.0F, 0.1F));
  private final ModuleSetting.Number moduleNumber7 =
      this.setting(
          this.moduleNameService8,
          new ModuleSetting.Number("ellice Intensity", 1.2F, 0.0F, 3.0F, 0.05F));
  private final ModuleSetting.Mode moduleMode2 =
      this.setting(this.moduleNameService8, createMode("ellice Visibility Mode"));
  private final ModuleNameService moduleNameService9 =
      this.settingCategory("Fire")
          .description("Groups flame appearance, glow, heat distortion, and visibility controls.");
  private final ModuleNameService moduleNameService10 =
      this.settingCategory(this.moduleNameService9, "Shape")
          .description("Controls flame height, width, density, and brightness.");
  private final ModuleNameService moduleNameService11 =
      this.settingCategory(this.moduleNameService9, "Colors")
          .description("Controls flame colors and optional per-player palette variation.");
  private final ModuleNameService moduleNameService12 =
      this.settingCategory(this.moduleNameService9, "Bloom")
          .description("Controls the brightness and screen-space spread of the fire glow.");
  private final ModuleNameService moduleNameService13 =
      this.settingCategory(this.moduleNameService9, "Heat")
          .description("Controls backdrop blur and heat-warp distortion behind flames.");
  private final ModuleNameService moduleNameService14 =
      this.settingCategory(this.moduleNameService9, "Visibility")
          .description("Controls flame opacity for visible and occluded targets.");
  private final ModuleNameService moduleNameService15 =
      this.settingCategory("Outline")
          .description("Groups the silhouette appearance and visibility controls.");
  private final ModuleNameService moduleNameService16 =
      this.settingCategory(this.moduleNameService15, "Appearance")
          .description("Controls outline thickness, softness, and visible or occluded colors.");
  private final ModuleNameService moduleNameService17 =
      this.settingCategory(this.moduleNameService15, "Visibility")
          .description(
              "Chooses whether outlines render for visible targets, occluded targets, or both.");
  private final ModuleNameService moduleNameService18 =
      this.settingCategory("Kawase Bloom")
          .description("Groups the dedicated Kawase glow, blur, and visibility controls.");
  private final ModuleNameService moduleNameService19 =
      this.settingCategory(this.moduleNameService18, "Appearance")
          .description("Controls Kawase strength, core opacity, and visible or occluded colors.");
  private final ModuleNameService moduleNameService20 =
      this.settingCategory(this.moduleNameService18, "Blur")
          .description("Controls Kawase blur radius and pyramid depth.");
  private final ModuleNameService moduleNameService21 =
      this.settingCategory(this.moduleNameService18, "Visibility")
          .description(
              "Chooses whether Kawase glow renders for visible targets, occluded targets, or"
                  + " both.");
  private final ModuleNameService moduleNameService22 =
      this.settingCategory("Interference")
          .description(
              "Complex polynomial vortices weave moving light filaments around the silhouette.");
  private final ModuleSetting.Bool moduleBool5 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Fire", true)
              .description("Renders animated flames around each selected target."));
  private final ModuleSetting.Bool moduleBool6 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Outline", false)
              .description("Draws a crisp silhouette around each selected target."));
  private final ModuleSetting.Bool moduleBool7 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Kawase Bloom", false)
              .description(
                  "Adds a soft glow outside the model contour with an optional narrow rim."));
  private final ModuleSetting.Bool moduleBool8 =
      this.setting(
          this.moduleNameService,
          new ModuleSetting.Bool("Interference", false)
              .description(
                  "Animates splitting, twisting light filaments along the target's exterior"
                      + " contour."));
  private final ModuleSetting.Color moduleColor7 =
      this.setting(
          this.moduleNameService22, new ModuleSetting.Color("Interference Color A", -429068289));
  private final ModuleSetting.Color moduleColor8 =
      this.setting(
          this.moduleNameService22, new ModuleSetting.Color("Interference Color B", -423523585));
  private final ModuleSetting.Color moduleColor9 =
      this.setting(
          this.moduleNameService22,
          new ModuleSetting.Color("Interference Occluded Color", -1276080708));
  private final ModuleSetting.Number moduleNumber8 =
      this.setting(
          this.moduleNameService22,
          new ModuleSetting.Number("Interference Width", 12.0F, 3.0F, 32.0F, 0.5F));
  private final ModuleSetting.Number moduleNumber9 =
      this.setting(
          this.moduleNameService22,
          new ModuleSetting.Number("Interference Speed", 0.65F, 0.0F, 2.0F, 0.05F));
  private final ModuleSetting.Number moduleNumber10 =
      this.setting(
          this.moduleNameService22,
          new ModuleSetting.Number("Interference Complexity", 2.0F, 0.5F, 5.0F, 0.1F));
  private final ModuleSetting.Number moduleNumber11 =
      this.setting(
          this.moduleNameService22,
          new ModuleSetting.Number("Interference Intensity", 1.25F, 0.0F, 3.0F, 0.05F));
  private final ModuleSetting.Mode moduleMode3 =
      this.setting(this.moduleNameService22, createMode("Interference Visibility Mode"));
  private final ModuleSetting.Color moduleColor10 =
      this.setting(
          this.moduleNameService11,
          new ModuleSetting.Color("Core Color", -11414)
              .description("Sets the hottest inner flame color and opacity."));
  private final ModuleSetting.Color moduleColor11 =
      this.setting(
          this.moduleNameService11,
          new ModuleSetting.Color("Flame Color", -49902)
              .description("Sets the outer flame color and opacity."));
  private final ModuleSetting.Bool moduleBool9 =
      this.setting(
          this.moduleNameService11,
          new ModuleSetting.Bool("Per Player Colors", true)
              .description("Derives a stable color palette from each player’s identity."));
  private final ModuleSetting.Number moduleNumber12 =
      this.setting(
          this.moduleNameService10,
          new ModuleSetting.Number("Intensity", 1.65F, 0.0F, 4.0F, 0.05F)
              .description("Controls overall flame density and brightness."));
  private final ModuleSetting.Number moduleNumber13 =
      this.setting(
          this.moduleNameService10,
          new ModuleSetting.Number("Flame Height", 1.15F, 0.2F, 3.0F, 0.05F)
              .description("Sets how far flames rise above the model silhouette."));
  private final ModuleSetting.Number moduleNumber14 =
      this.setting(
          this.moduleNameService10,
          new ModuleSetting.Number("Flame Width", 0.38F, 0.1F, 1.25F, 0.01F)
              .description("Sets how far flames spread beside the model silhouette."));
  private final ModuleSetting.Number moduleNumber15 =
      this.setting(
          this.moduleNameService12,
          new ModuleSetting.Number("Bloom Strength", 1.35F, 0.0F, 4.0F, 0.05F)
              .description("Controls the brightness of the fire glow."));
  private final ModuleSetting.Number moduleNumber16 =
      this.setting(
          this.moduleNameService12,
          new ModuleSetting.Number("Blur Radius", 24.0F, 0.0F, 96.0F, 1.0F)
              .description("Sets the fire-glow spread in screen pixels."));
  private final ModuleSetting.Number moduleNumber17 =
      this.setting(
          this.moduleNameService13,
          new ModuleSetting.Number("Backdrop Blur", 0.72F, 0.0F, 1.0F, 0.01F)
              .description("Controls how strongly the scene behind the fire is blurred."));
  private final ModuleSetting.Number moduleNumber18 =
      this.setting(
          this.moduleNameService13,
          new ModuleSetting.Number("Heat Distortion", 5.0F, 0.0F, 18.0F, 0.25F)
              .description("Sets heat-warp displacement in screen pixels."));
  private final ModuleSetting.Number moduleNumber19 =
      this.setting(
          this.moduleNameService14,
          new ModuleSetting.Number("Visible Opacity", 0.68F, 0.0F, 1.0F, 0.01F)
              .description("Sets flame opacity for unobstructed players."));
  private final ModuleSetting.Number moduleNumber20 =
      this.setting(
          this.moduleNameService14,
          new ModuleSetting.Number("Through Walls Opacity", 0.92F, 0.0F, 1.0F, 0.01F)
              .description("Sets flame opacity where players are hidden by terrain."));
  private final ModuleSetting.Number moduleNumber21 =
      this.setting(
          this.moduleNameService16,
          new ModuleSetting.Number("Outline Thickness", 1.5F, 0.5F, 16.0F, 0.25F)
              .description("Sets the silhouette width in screen pixels."));
  private final ModuleSetting.Number moduleNumber22 =
      this.setting(
          this.moduleNameService16,
          new ModuleSetting.Number("Outline Softness", 0.85F, 0.0F, 3.0F, 0.05F)
              .description("Feathers the silhouette edge in screen pixels."));
  private final ModuleSetting.Mode moduleMode4 =
      this.setting(
          this.moduleNameService17,
          createMode("Outline Visibility Mode")
              .description("Shows the outline when visible, occluded, or in both cases."));
  private final ModuleSetting.Color moduleColor12 =
      this.setting(
          this.moduleNameService16,
          new ModuleSetting.Color("Outline Visible Color", -427043329)
              .description("Sets outline color and opacity for visible targets."));
  private final ModuleSetting.Color moduleColor13 =
      this.setting(
          this.moduleNameService16,
          new ModuleSetting.Color("Outline Occluded Color", -1275112070)
              .description("Sets outline color and opacity through walls."));
  private final ModuleSetting.Number moduleNumber23 =
      this.setting(
          this.moduleNameService19,
          new ModuleSetting.Number("Kawase Strength", 1.35F, 0.0F, 4.0F, 0.05F)
              .description("Controls the brightness of the Kawase glow."));
  private final ModuleSetting.Number moduleNumber24 =
      this.setting(
          this.moduleNameService19,
          new ModuleSetting.Number("Kawase Core Opacity", 0.72F, 0.0F, 1.0F, 0.01F)
              .description(
                  "Sets opacity of the narrow exterior rim; the model interior stays unchanged."));
  private final ModuleSetting.Number moduleNumber25 =
      this.setting(
          this.moduleNameService20,
          new ModuleSetting.Number("Kawase Radius", 24.0F, 0.0F, 96.0F, 1.0F)
              .description("Sets the Kawase glow spread in screen pixels."));
  private final ModuleSetting.Number moduleNumber26 =
      this.setting(
          this.moduleNameService20,
          new ModuleSetting.Number("Kawase Levels", 4.0F, 2.0F, 5.0F, 1.0F)
              .description("Sets blur pyramid depth; higher values spread glow and cost more."));
  private final ModuleSetting.Mode moduleMode5 =
      this.setting(
          this.moduleNameService21,
          createMode("Kawase Visibility Mode")
              .description("Shows Kawase output when visible, occluded, or both."));
  private final ModuleSetting.Color moduleColor14 =
      this.setting(
          this.moduleNameService19,
          new ModuleSetting.Color("Kawase Visible Color", -645147137)
              .description("Sets Kawase color and opacity for visible targets."));
  private final ModuleSetting.Color moduleColor15 =
      this.setting(
          this.moduleNameService19,
          new ModuleSetting.Color("Kawase Occluded Color", -1275112070)
              .description("Sets Kawase color and opacity through walls."));
  private final ModuleSetting.Number moduleNumber27 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Range", 96.0F, 8.0F, 256.0F, 1.0F)
              .description("Limits all effects to this distance from the camera, in blocks."));
  private final ModuleSetting.Number moduleNumber28 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Number("Max Targets", 32.0F, 1.0F, 64.0F, 1.0F)
              .description("Limits the shared selection to control rendering cost."));
  private final ModuleSetting.Mode moduleMode6 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Mode("Box Style", new String[] {"Full", "Corners"}, "Full")
              .description("A continuous fine frame or eight compact corner strokes."));
  private final ModuleSetting.Mode moduleMode7 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Mode("Color Mode", new String[] {"Custom", "Health", "Team"}, "Custom")
              .description("Use a custom tint, health colors, or the server's team colors."));
  private final ModuleSetting.Color moduleColor16 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Color("Box Color", -3809547)
              .description("Frame tint and opacity. Also used when a target has no team."));
  private final ModuleSetting.Number moduleNumber29 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Line Width", 0.8F, 0.5F, 2.0F, 0.1F)
              .description("Fine anti-aliased strokes with a dark inner and outer contrast edge."));
  private final ModuleSetting.Number moduleNumber30 =
      this.setting(
          this.moduleNameService4,
          new ModuleSetting.Number("Fill Opacity", 0.025F, 0.0F, 0.2F, 0.005F)
              .description(
                  "A subtle tint inside the box; set to zero for an entirely clear centre."));
  private final ModuleSetting.Bool moduleBool10 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Health Bar", true)
              .description(
                  "Left-side health, a brief damage trail, and a separate gold absorption rail."));
  private final ModuleSetting.Bool moduleBool11 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Armor Bar", false)
              .description("A slim blue track below the box, showing armor points out of twenty."));
  private final ModuleSetting.Bool moduleBool12 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Names", true)
              .description(
                  "Compact names above the box. Automatically defers to the Nametags module."));
  private final ModuleSetting.Bool moduleBool13 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Distance", true)
              .description("Distance in metres below the box."));
  private final ModuleSetting.Bool moduleBool14 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Avoid Label Overlap", true)
              .description(
                  "Prioritizes nearby players' captions while keeping every selected box"
                      + " visible."));
  private final ModuleSetting.Number moduleNumber31 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Number("Label Scale", 1.0F, 0.75F, 1.5F, 0.05F)
              .description("Changes caption size without changing projected box geometry."));
  private final ModuleSetting.Bool moduleBool15 =
      this.setting(
          this.moduleNameService5,
          new ModuleSetting.Bool("Animations", true)
              .description(
                  "Short entry fades and smooth vitals. Boxes follow world movement immediately."));
  private final ModuleSetting.Bool moduleBool16 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Players", true));
  private final ModuleSetting.Mode moduleMode8 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Mode("Mobs", new String[] {"Off", "Hostile", "All"}, "Off"));
  private final ModuleSetting.Bool moduleBool17 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Include Self", false));
  private final ModuleSetting.Bool moduleBool18 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Invisible Entities", false)
              .description("Includes invisible entities that the server has sent to your client."));
  private final ModuleSetting.Bool moduleBool19 =
      this.setting(
          this.moduleNameService6,
          new ModuleSetting.Bool("Boxes Through Walls", true)
              .description(
                  "Shows loaded targets through terrain. Disable to require a visible body or"
                      + " head."));
  private final ModuleSetting.Bool moduleBool20 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Dropped Items", true));
  private final ModuleSetting.Bool moduleBool21 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Chests", true));
  private final ModuleSetting.Bool moduleBool22 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Ender Chests", true));
  private final ModuleSetting.Bool moduleBool23 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Barrels", true));
  private final ModuleSetting.Bool moduleBool24 =
      this.setting(this.moduleNameService3, new ModuleSetting.Bool("Shulker Boxes", true));
  private final ModuleSetting.Bool moduleBool25 =
      this.setting(
          this.moduleNameService3,
          new ModuleSetting.Bool("Other Storage", false)
              .description(
                  "Furnaces, hoppers, dispensers, droppers and other loaded inventory blocks."));
  private final Map<UUID, EffectsComponent.MotionState> entries = new HashMap<>();
  private long timestamp;
  private final LayoutContainerNode sceneComponent4 =
      new LayoutContainerNode()
          .direction(ScenePctService.Direction.NONE)
          .id("esp.overlay")
          .absolute()
          .clip(true)
          .pointerEvents(false);
  private final Map<UUID, EffectsComponent.Tag> entries2 = new HashMap<>();
  private List<EspTargetCapture.Target> items2 = List.of();
  private boolean enabled;

  public EffectsComponent() {
    super(
        ModuleBuilderData.builder("ESP")
            .description(
                "Boxes and silhouette effects for players, mobs, dropped items and storage.")
            .category(ModuleFeatureType.VISUALS)
            .build());
    this.moduleNameService8.settings().forEach(item -> item.visibleWhen(this.moduleBool4));
    this.moduleColor4.visibleWhen(this.moduleMode2, EffectsComponent::checkCondition2);
    this.moduleColor5.visibleWhen(this.moduleMode2, EffectsComponent::checkCondition2);
    this.moduleColor6.visibleWhen(this.moduleMode2, EffectsComponent::checkCondition3);
    this.moduleColor.visibleWhen(this.moduleBool);
    this.moduleNameService7.settings().forEach(item -> item.visibleWhen(this.moduleBool3));
    this.moduleColor2.visibleWhen(this.moduleMode, EffectsComponent::checkCondition2);
    this.moduleColor3.visibleWhen(this.moduleMode, EffectsComponent::checkCondition3);
    this.collectValues().forEach(item -> item.visibleWhen(this.moduleBool2));
    this.collectValues2().forEach(item -> item.visibleWhen(this.moduleBool5));
    this.collectValues3().forEach(item -> item.visibleWhen(this.moduleBool6));
    this.collectValues4().forEach(item -> item.visibleWhen(this.moduleBool7));
    this.moduleNameService22.settings().forEach(item -> item.visibleWhen(this.moduleBool8));
    this.moduleColor7.visibleWhen(this.moduleMode3, EffectsComponent::checkCondition2);
    this.moduleColor8.visibleWhen(this.moduleMode3, EffectsComponent::checkCondition2);
    this.moduleColor9.visibleWhen(this.moduleMode3, EffectsComponent::checkCondition3);
    this.moduleColor12.visibleWhen(this.moduleMode4, EffectsComponent::checkCondition2);
    this.moduleColor13.visibleWhen(this.moduleMode4, EffectsComponent::checkCondition3);
    this.moduleColor14.visibleWhen(this.moduleMode5, EffectsComponent::checkCondition2);
    this.moduleColor15.visibleWhen(this.moduleMode5, EffectsComponent::checkCondition3);
  }

  @Override
  protected void onEnable() {
    this.sceneComponent4.visible(false);
    CoreIsInitializedHandler.get().scene().root().addChild(this.sceneComponent4);
    this.on(EventAttackInputService.WORLD_RENDER_PREPARE).run(this::updateState);
    this.on(EventAttackInputService.WORLD_RENDER)
        .run(
            item -> {
              if (this.style().renderable()) {
                for (EspTargetCapture.Target target : this.items2) {
                  if (!target.player() && target.alive()) {
                    EntityRenderCapture.capture(target, item);
                  }
                }
              }

              if ((Boolean) this.moduleBool2.get()) {
                this.updateState2(item);
              }
            });
    this.on(EventAttackInputService.RENDER)
        .priority(EventIsAfterHandler.Priority.FIRST)
        .run(
            item -> {
              if (!this.enabled || !this.checkCondition()) {
                this.updateState3();
              }

              this.enabled = false;
              this.items2 = List.of();
            });
    this.on(EventAttackInputService.WORLD)
        .run(
            item -> {
              this.updateState3();
              this.updateState5();
            });
  }

  @Override
  protected void onDisable() {
    this.updateState3();
    this.updateState5();
    CoreIsInitializedHandler.get().scene().root().removeChild(this.sceneComponent4);
  }

  private boolean checkCondition() {
    Minecraft minecraft = Minecraft.getInstance();
    return minecraft.level != null
        && minecraft.player != null
        && minecraft.screen == null
        && !minecraft.options.hideGui
        && !CoreIsInitializedHandler.get().screens().isActive();
  }

  private void updateState(EventAttackInputService.WorldRenderPrepare worldRenderPrepare) {
    this.enabled = false;
    this.sceneComponent4.visible(false);
    EspPrepareService.clear();
    if (!this.checkCondition()) {
      this.updateState3();
      this.updateState5();
    } else {
      this.items2 =
          EspTargetCapture.capture(
                  new EspTargetCapture.Options(
                      (Boolean) this.moduleBool16.get(),
                      (String) this.moduleMode8.get(),
                      (Boolean) this.moduleBool17.get(),
                      (Boolean) this.moduleBool18.get(),
                      ((Float) this.moduleNumber27.get()).floatValue(),
                      (Boolean) this.moduleBool20.get(),
                      (Boolean) this.moduleBool21.get(),
                      (Boolean) this.moduleBool22.get(),
                      (Boolean) this.moduleBool23.get(),
                      (Boolean) this.moduleBool24.get(),
                      (Boolean) this.moduleBool25.get()),
                  worldRenderPrepare.tickDelta())
              .stream()
              .limit(Math.round((Float) this.moduleNumber28.get()))
              .toList();
      if ((Boolean) this.moduleBool2.get() && (Boolean) this.moduleBool12.get()) {
        HashSet hashSet = new HashSet();

        for (EspTargetCapture.Target target : this.items2) {
          if (target.entity() != null) {
            hashSet.add(target.id());
          }
        }

        EspPrepareService.prepare(hashSet);
      }

      long offset = System.nanoTime();
      double doubleValue =
          this.timestamp == 0L ? 0.016666666666666666 : (offset - this.timestamp) / 1.0E9;
      this.timestamp = offset;
      if (!Double.isFinite(doubleValue) || doubleValue <= 0.0 || doubleValue > 0.25) {
        doubleValue = 0.016666666666666666;
        this.entries.clear();
      }

      ArrayList arrayList = new ArrayList();
      HashSet currentHashSet = new HashSet();

      for (EspTargetCapture.Target currentTarget : this.items2) {
        currentHashSet.add(currentTarget.id());
        arrayList.add(
            currentTarget.entity() == null
                ? new PlayerMotionSample(currentTarget.id())
                : this.createRender3dData5(
                        currentTarget.entity(),
                        currentTarget.entity().getPosition(worldRenderPrepare.tickDelta()),
                        doubleValue)
                    .withDamage(
                        currentTarget.entity() instanceof LivingEntity livingEntity
                                && currentTarget.entity() != Minecraft.getInstance().player
                            ? calculateValue(
                                (livingEntity.hurtTime - worldRenderPrepare.tickDelta()) / 10.0F)
                            : 0.0F));
      }

      this.entries.keySet().retainAll(currentHashSet);
      this.updateState4(arrayList, this.style());
    }
  }

  FireEffectSettings style() {
    return new FireEffectSettings(
        (Integer) this.moduleColor10.get(),
        (Integer) this.moduleColor11.get(),
        (Float) this.moduleNumber12.get(),
        (Float) this.moduleNumber13.get(),
        (Float) this.moduleNumber14.get(),
        (Float) this.moduleNumber15.get(),
        (Float) this.moduleNumber16.get(),
        (Float) this.moduleNumber17.get(),
        (Float) this.moduleNumber18.get(),
        (Float) this.moduleNumber19.get(),
        (Float) this.moduleNumber20.get(),
        (Boolean) this.moduleBool9.get(),
        (Boolean) this.moduleBool5.get(),
        new OutlineEffectSettings(
            (Boolean) this.moduleBool6.get(),
            (Integer) this.moduleColor12.get(),
            (Integer) this.moduleColor13.get(),
            (Float) this.moduleNumber21.get(),
            (Float) this.moduleNumber22.get(),
            createRender3dFeatureType((String) this.moduleMode4.get())),
        new KawaseBloomSettings(
            (Boolean) this.moduleBool7.get(),
            (Integer) this.moduleColor14.get(),
            (Integer) this.moduleColor15.get(),
            (Float) this.moduleNumber23.get(),
            (Float) this.moduleNumber25.get(),
            Math.round((Float) this.moduleNumber26.get()),
            (Float) this.moduleNumber24.get(),
            createRender3dFeatureType((String) this.moduleMode5.get())),
        new InterferenceEffectSettings(
            (Boolean) this.moduleBool8.get(),
            (Integer) this.moduleColor7.get(),
            (Integer) this.moduleColor8.get(),
            (Integer) this.moduleColor9.get(),
            (Float) this.moduleNumber8.get(),
            (Float) this.moduleNumber9.get(),
            (Float) this.moduleNumber10.get(),
            (Float) this.moduleNumber11.get(),
            createRender3dFeatureType((String) this.moduleMode3.get())),
        new Render3dData(
            (Boolean) this.moduleBool3.get(),
            (Integer) this.moduleColor2.get(),
            (Integer) this.moduleColor3.get(),
            (Float) this.moduleNumber.get(),
            (Float) this.moduleNumber2.get(),
            (Float) this.moduleNumber3.get(),
            createRender3dFeatureType((String) this.moduleMode.get())),
        this.moduleBool.get() ? (Integer) this.moduleColor.get() : 0,
        new ElliceEffectSettings(
            (Boolean) this.moduleBool4.get(),
            (Integer) this.moduleColor4.get(),
            (Integer) this.moduleColor5.get(),
            (Integer) this.moduleColor6.get(),
            (Float) this.moduleNumber4.get(),
            (Float) this.moduleNumber5.get(),
            (Float) this.moduleNumber6.get(),
            (Float) this.moduleNumber7.get(),
            createRender3dFeatureType((String) this.moduleMode2.get())));
  }

  private void updateState2(EventAttackInputService.WorldRender worldRender) {
    if (!this.checkCondition()) {
      this.updateState3();
    } else {
      CoreIsInitializedHandler coreIsInitialized = CoreIsInitializedHandler.get();
      float currentWidth = coreIsInitialized.viewport().width();
      float currentHeight = coreIsInitialized.viewport().height();
      this.sceneComponent4.size(currentWidth, currentHeight).visible(true);
      EspProjectService espProject =
          new EspProjectService(
              worldRender.viewMatrix(),
              worldRender.projectionMatrix(),
              worldRender.cameraPos().x,
              worldRender.cameraPos().y,
              worldRender.cameraPos().z,
              currentWidth,
              currentHeight);
      EspData espData =
          new EspData(
              ((String) this.moduleMode6.get()).equals("Corners"),
              (String) this.moduleMode7.get(),
              (Integer) this.moduleColor16.get(),
              (Boolean) this.moduleBool10.get(),
              (Boolean) this.moduleBool11.get(),
              (Float) this.moduleNumber30.get(),
              (Float) this.moduleNumber29.get(),
              (Float) this.moduleNumber31.get(),
              (Boolean) this.moduleBool15.get());
      HashSet hashSet = new HashSet();
      ArrayList arrayList = new ArrayList();
      ArrayList currentArrayList = new ArrayList();
      CompositorPushPresentationScaleService compositorPushPresentationScale =
          coreIsInitialized.compositor();

      for (EspTargetCapture.Target currentTarget : this.items2) {
        if (hashSet.size() >= ((Float) this.moduleNumber28.get()).intValue()) {
          break;
        }

        if (currentTarget.alive()) {
          EspProjectService.Bounds bounds = espProject.project(currentTarget.box());
          if (bounds != null) {
            float value =
                calculateValue(
                    (float)
                        ((((Float) this.moduleNumber27.get()).floatValue()
                                - currentTarget.distance())
                            / Math.min(
                                12.0, ((Float) this.moduleNumber27.get()).floatValue() * 0.15)));
            if (!(value < 0.01F)
                && ((Boolean) this.moduleBool19.get()
                    || EspTargetCapture.visible(currentTarget, worldRender.cameraPos()))) {
              UUID uUID = currentTarget.id();
              EspLabelData espLabelData = EspTargetCapture.describe(currentTarget);
              EspData currentEspData =
                  currentTarget.entity() instanceof LivingEntity
                      ? espData
                      : new EspData(
                          espData.corners(),
                          "Custom",
                          espData.color(),
                          false,
                          false,
                          espData.fillOpacity(),
                          espData.lineWidth(),
                          espData.labelScale(),
                          espData.animations());
              float currentValue = currentEspData.labelScale();
              EspRenderer.Captions captions =
                  EspPlaceService.place(
                      bounds,
                      currentEspData,
                      espLabelData,
                      (Boolean) this.moduleBool12.get() && !NametagsPrepareService.replaces(uUID),
                      (Boolean) this.moduleBool13.get(),
                      (Boolean) this.moduleBool14.get(),
                      compositorPushPresentationScale.textWidth(
                          espLabelData.name(), 9.5F * currentValue),
                      compositorPushPresentationScale.textWidth(
                          espLabelData.metres() + " m", 8.5F * currentValue),
                      currentWidth,
                      currentHeight,
                      currentArrayList);
              EffectsComponent.Tag tag = this.entries2.get(uUID);
              if (tag == null) {
                EspRenderer espRenderer =
                    new EspRenderer(espLabelData, currentEspData, bounds.clipped(), captions);
                ComponentMountService componentMount =
                    new ComponentMountService().mount(espRenderer, coreIsInitialized.theme());
                componentMount
                    .id("esp.target." + uUID)
                    .key(uUID.toString())
                    .absolute()
                    .pointerEvents(false);
                tag = new EffectsComponent.Tag(espRenderer, componentMount);
                this.entries2.put(uUID, tag);
                this.sceneComponent4.addChild(componentMount);
              } else if (tag.component.update(
                  espLabelData, currentEspData, bounds.clipped(), captions)) {
                tag.host.invalidateComponent();
              }

              tag.host
                  .position(bounds.x(), bounds.y())
                  .size(bounds.width(), bounds.height())
                  .opacity(value);
              hashSet.add(uUID);
              arrayList.add(tag.host);
            }
          }
        }
      }

      this.entries2
          .entrySet()
          .removeIf(
              entry -> {
                if (hashSet.contains(entry.getKey())) {
                  return false;
                }

                this.sceneComponent4.removeChild(entry.getValue().host);
                return true;
              });
      Collections.reverse(arrayList);
      this.sceneComponent4.reconcileChildren(arrayList);
      this.enabled = true;
    }
  }

  private void updateState3() {
    this.sceneComponent4.visible(false).clearChildren();
    this.entries2.clear();
    this.items2 = List.of();
    EspPrepareService.clear();
    this.enabled = false;
  }

  private static float calculateValue(float value) {
    float currentValue = Math.clamp(value, 0.0F, 1.0F);
    return currentValue * currentValue * (3.0F - 2.0F * currentValue);
  }

  private List<ModuleSetting<?>> collectValues() {
    return List.of(
        this.moduleMode6,
        this.moduleMode7,
        this.moduleColor16,
        this.moduleNumber29,
        this.moduleNumber30,
        this.moduleBool10,
        this.moduleBool11,
        this.moduleBool12,
        this.moduleBool13,
        this.moduleBool14,
        this.moduleNumber31,
        this.moduleBool15,
        this.moduleBool19);
  }

  private List<ModuleSetting<?>> collectValues2() {
    return List.of(
        this.moduleColor10,
        this.moduleColor11,
        this.moduleBool9,
        this.moduleNumber12,
        this.moduleNumber13,
        this.moduleNumber14,
        this.moduleNumber15,
        this.moduleNumber16,
        this.moduleNumber17,
        this.moduleNumber18,
        this.moduleNumber19,
        this.moduleNumber20);
  }

  private List<ModuleSetting<?>> collectValues3() {
    return List.of(
        this.moduleNumber21,
        this.moduleNumber22,
        this.moduleMode4,
        this.moduleColor12,
        this.moduleColor13);
  }

  private List<ModuleSetting<?>> collectValues4() {
    return List.of(
        this.moduleNumber23,
        this.moduleNumber24,
        this.moduleNumber25,
        this.moduleNumber26,
        this.moduleMode5,
        this.moduleColor14,
        this.moduleColor15);
  }

  private static ModuleSetting.Mode createMode(String text) {
    return new ModuleSetting.Mode(
        text,
        new String[] {
          Render3dFeatureType.VISIBLE.displayName(),
          Render3dFeatureType.THROUGH_WALLS.displayName(),
          Render3dFeatureType.BOTH.displayName()
        },
        Render3dFeatureType.BOTH.displayName());
  }

  private static Render3dFeatureType createRender3dFeatureType(String text) {
    for (Render3dFeatureType render3dFeatureType : Render3dFeatureType.values()) {
      if (render3dFeatureType.displayName().equals(text)) {
        return render3dFeatureType;
      }
    }

    return Render3dFeatureType.BOTH;
  }

  private static boolean checkCondition2(String text) {
    return createRender3dFeatureType(text).includesVisible();
  }

  private static boolean checkCondition3(String text) {
    return createRender3dFeatureType(text).includesOccluded();
  }

  private PlayerMotionSample createRender3dData5(Entity entity, Vec3 vec3, double width) {
    UUID uUID = entity.getUUID();
    Vec3 currentVec3 = createVec3(entity.getDeltaMovement().scale(20.0), 24.0);
    EffectsComponent.MotionState motionState = this.entries.get(uUID);
    if (motionState == null) {
      motionState = new EffectsComponent.MotionState(vec3, currentVec3);
      this.entries.put(uUID, motionState);
    } else {
      Vec3 nextVec3 = vec3.subtract(motionState.position);
      if (nextVec3.lengthSqr() > 16.0) {
        motionState.position = vec3;
        motionState.velocity = currentVec3;
        return new PlayerMotionSample(
            uUID,
            (float) motionState.velocity.x,
            (float) motionState.velocity.y,
            (float) motionState.velocity.z);
      }

      Vec3 previousVec3 = nextVec3.scale(1.0 / Math.max(width, 1.0E-4));
      double currentLength = previousVec3.length();
      Vec3 sourceVec3 =
          Double.isFinite(currentLength) && currentLength <= 48.0
              ? previousVec3.scale(0.82).add(currentVec3.scale(0.18))
              : currentVec3;
      sourceVec3 = createVec3(sourceVec3, 24.0);
      double doubleValue = 1.0 - Math.exp(-15.0 * width);
      motionState.velocity =
          motionState.velocity.scale(1.0 - doubleValue).add(sourceVec3.scale(doubleValue));
      motionState.position = vec3;
    }

    return new PlayerMotionSample(
        uUID,
        (float) motionState.velocity.x,
        (float) motionState.velocity.y,
        (float) motionState.velocity.z);
  }

  private static Vec3 createVec3(Vec3 vec3, double doubleValue) {
    if (vec3 != null
        && Double.isFinite(vec3.x)
        && Double.isFinite(vec3.y)
        && Double.isFinite(vec3.z)) {
      double currentLength = vec3.length();
      if (Double.isFinite(currentLength) && !(currentLength <= 0.0)) {
        return currentLength <= doubleValue ? vec3 : vec3.scale(doubleValue / currentLength);
      } else {
        return Vec3.ZERO;
      }
    } else {
      return Vec3.ZERO;
    }
  }

  private void updateState4(List<PlayerMotionSample> items, FireEffectSettings fireEffectSettings) {
    Render3dSceneService render3dScene = CoreIsInitializedHandler.get().renderer3D();
    if (render3dScene != null) {
      render3dScene.preparePlayerFireAuras(items, fireEffectSettings);
    }
  }

  private void updateState5() {
    this.entries.clear();
    this.timestamp = 0L;
    Render3dSceneService render3dScene = CoreIsInitializedHandler.get().renderer3D();
    if (render3dScene != null) {
      render3dScene.clearPlayerFireAuras();
    }
  }

  static Set<UUID> nearestTargetIds(ArrayList<EffectsComponent.Candidate> arrayList, int value) {
    arrayList.sort(
        Comparator.comparingDouble(EffectsComponent.Candidate::distanceSquared)
            .thenComparing(EffectsComponent.Candidate::playerId));
    int currentSize = Math.min(Math.max(value, 0), arrayList.size());
    LinkedHashSet linkedHashSet = new LinkedHashSet(currentSize);

    for (int index = 0; index < currentSize; index++) {
      linkedHashSet.add(((EffectsComponent.Candidate) arrayList.get(index)).playerId());
    }

    return Collections.unmodifiableSet(linkedHashSet);
  }

  static List<PlayerMotionSample> nearestTargets(
      ArrayList<EffectsComponent.Candidate> arrayList, int value) {
    arrayList.sort(
        Comparator.comparingDouble(EffectsComponent.Candidate::distanceSquared)
            .thenComparing(EffectsComponent.Candidate::playerId));
    int currentSize = Math.min(Math.max(value, 0), arrayList.size());
    ArrayList currentArrayList = new ArrayList(currentSize);

    for (int index = 0; index < currentSize; index++) {
      currentArrayList.add(((EffectsComponent.Candidate) arrayList.get(index)).target());
    }

    return List.copyOf(currentArrayList);
  }

  record Candidate(PlayerMotionSample target, double distanceSquared) {
    Candidate(PlayerMotionSample target, double distanceSquared) {
      if (target == null) {
        throw new IllegalArgumentException("target is required");
      }

      if (Double.isFinite(distanceSquared) && !(distanceSquared < 0.0)) {
        this.target = target;
        this.distanceSquared = distanceSquared;
      } else {
        throw new IllegalArgumentException("distanceSquared must be finite and non-negative");
      }
    }

    Candidate(UUID uUID, double doubleValue) {
      this(new PlayerMotionSample(uUID), doubleValue);
    }

    UUID playerId() {
      return this.target.playerId();
    }
  }

  private static final class MotionState {
    Vec3 position;
    Vec3 velocity;

    MotionState(Vec3 vec3, Vec3 currentVec3) {
      this.position = vec3;
      this.velocity = currentVec3;
    }
  }

  private record Tag(EspRenderer component, ComponentMountService host) {}
}
