package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.NametagTargetCapture;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.nametags.NametagsPrepareService;
import dev.felix.ellice.feature.nametags.NametagsData;
import dev.felix.ellice.feature.nametags.NametagsProjectService;
import dev.felix.ellice.feature.nametags.NametagsRenderer;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.material.MaterialTerrainTooltipsService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class TargetsComponent extends ModuleSettingsService {
   private final ModuleNameService moduleNameService = this.settingCategory("Targets");
   private final ModuleNameService moduleNameService2 = this.settingCategory("Appearance");
   private final ModuleNameService moduleNameService3 = this.settingCategory("Visibility");
   private final ModuleSetting.Bool moduleBool = this.setting(this.moduleNameService, new ModuleSetting.Bool("Players", true));
   private final ModuleSetting.Bool moduleBool2 = this.setting(this.moduleNameService, new ModuleSetting.Bool("Dropped Items", true));
   private final ModuleSetting.Bool moduleBool3 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Important Items Only", true)
         .description("Rare, enchanted, renamed, diamond/netherite, combat and endgame items.")
   );
   private final ModuleSetting.Bool moduleBool4 = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Named Entities", true).description("Includes named pets and other entities with a custom name.")
   );
   private final ModuleSetting.Mode moduleMode = this.setting(
      this.moduleNameService, new ModuleSetting.Mode("Mobs", new String[]{"Off", "Hostile", "All"}, "Off")
   );
   private final ModuleSetting.Bool moduleBool5 = this.setting(this.moduleNameService, new ModuleSetting.Bool("Self in Third Person", false));
   private final ModuleSetting.Bool moduleBool6 = this.setting(this.moduleNameService2, new ModuleSetting.Bool("Health Bar", false));
   private final ModuleSetting.Bool moduleBool7 = this.setting(this.moduleNameService2, new ModuleSetting.Bool("Distance", false));
   private final ModuleSetting.Bool moduleBool8 = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Bool("Animations", true).description("A short fade-in and smooth health changes; no looping or bouncing.")
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService2,
      new ModuleSetting.Number(
         "Scale", 1.0F, 0.65F, 1.6F, 0.05F
      )
   );
   private final ModuleSetting.Number moduleNumber2 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
         "Range",
         64.0F,
         8.0F,
         160.0F,
         4.0F
      )
   );
   private final ModuleSetting.Number moduleNumber3 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Number(
         "Max Tags", 24.0F, 4.0F, 64.0F, 1.0F
      )
   );
   private final ModuleSetting.Bool moduleBool9 = this.setting(
      this.moduleNameService3,
      new ModuleSetting.Bool("Avoid Overlap", true).description("Prioritizes players, then items and named entities when labels overlap.")
   );
   private final ModuleSetting.Bool moduleBool10 = this.setting(this.moduleNameService3, new ModuleSetting.Bool("Through Walls", false));
   private final SceneCornerRadiusService sceneCornerRadiusService = new MaterialTerrainTooltipsService().direction(ScenePctService.Direction.NONE).pointerEvents(false).absolute();
   private final Map<UUID, TargetsComponent.Tag> entries = new HashMap<>();
   private List<NametagTargetCapture.Target> items2 = List.of();
   private boolean enabled;

   public TargetsComponent() {
      super(
         ModuleBuilderData.builder("Nametags")
            .category(ModuleFeatureType.VISUALS)
            .description("Simple animated names for players, important items and named entities.")
            .build()
      );
   }

   @Override
   protected void onEnable() {
      this.sceneCornerRadiusService.visible(false);
      CoreIsInitializedHandler.get().scene().root().addChild(this.sceneCornerRadiusService);
      this.on(EventAttackInputService.WORLD_RENDER_PREPARE).run(this::updateState);
      this.on(EventAttackInputService.WORLD_RENDER).run(this::updateState2);
      this.on(EventAttackInputService.RENDER).priority(EventIsAfterHandler.Priority.FIRST).run(item -> {
         if (!this.enabled || !this.checkCondition()) {
            this.updateState3();
         }

         this.enabled = false;
         this.items2 = List.of();
      });
      this.on(EventAttackInputService.WORLD).run(item -> this.updateState3());
   }

   @Override
   protected void onDisable() {
      this.updateState3();
      CoreIsInitializedHandler.get().scene().root().removeChild(this.sceneCornerRadiusService);
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
      this.sceneCornerRadiusService.visible(false);
      NametagsPrepareService.clear();
      if (!this.checkCondition()) {
         this.updateState3();
      } else {
         this.items2 = NametagTargetCapture.capture(
            new NametagTargetCapture.Options(
               (Boolean)this.moduleBool.get(),
               (Boolean)this.moduleBool2.get(),
               (Boolean)this.moduleBool3.get(),
               (Boolean)this.moduleBool4.get(),
               (String)this.moduleMode.get(),
               (Boolean)this.moduleBool5.get(),
               ((Float)this.moduleNumber2.get()).floatValue()
            ),
            worldRenderPrepare.tickDelta()
         );
         HashSet hashSet = new HashSet();

         for (NametagTargetCapture.Target target : this.items2) {
            hashSet.add(target.entity().getUUID());
         }

         NametagsPrepareService.prepare(hashSet);
      }
   }

   private void updateState2(EventAttackInputService.WorldRender worldRender) {
      if (!this.checkCondition()) {
         this.updateState3();
      } else {
         Minecraft minecraft = Minecraft.getInstance();
         float currentWidth = CoreIsInitializedHandler.get().viewport().width();
         float currentHeight = CoreIsInitializedHandler.get().viewport().height();
         this.sceneCornerRadiusService.size(currentWidth, currentHeight).visible(true);
         ArrayList<NametagsProjectService.Bounds> arrayList = new ArrayList<>();
         HashSet hashSet = new HashSet();
         CompositorPushPresentationScaleService compositorPushPresentationScale = CoreIsInitializedHandler.get().compositor();
         float value = (Float)this.moduleNumber.get();

         for (NametagTargetCapture.Target target : this.items2) {
            if (hashSet.size() >= ((Float)this.moduleNumber3.get()).intValue()) {
               break;
            }

            Vec3 vec3 = target.anchor().subtract(worldRender.cameraPos());
            NametagsProjectService.Point point = NametagsProjectService.project(
               vec3.x, vec3.y, vec3.z, worldRender.viewMatrix(), worldRender.projectionMatrix(), currentWidth, currentHeight
            );
            if (point != null && ((Boolean)this.moduleBool10.get() || NametagTargetCapture.visible(target, worldRender.cameraPos()))) {
               NametagsData nametagsData = NametagTargetCapture.describe(target, (Boolean)this.moduleBool6.get(), (Boolean)this.moduleBool7.get());
               float nextWidth = NametagsRenderer.width(compositorPushPresentationScale, nametagsData, value, currentWidth - 8.0F);
               NametagsProjectService.Bounds nextHeight = NametagsProjectService.bounds(point, nextWidth, NametagsRenderer.height(nametagsData, value), currentWidth, currentHeight);
               if (nextHeight != null
                  && (
                     !(Boolean)this.moduleBool9.get()
                        || !arrayList.stream().anyMatch(item -> item.overlaps(nextHeight, 3.0F * value))
                  )) {
                  UUID uUID = target.entity().getUUID();
                  TargetsComponent.Tag tag = this.entries.get(uUID);
                  if (tag == null) {
                     NametagsRenderer nametagsRenderer = new NametagsRenderer(nametagsData, value, (Boolean)this.moduleBool8.get());
                     ComponentMountService componentMount = new ComponentMountService().mount(nametagsRenderer, CoreIsInitializedHandler.get().theme());
                     componentMount.absolute().pointerEvents(false);
                     tag = new TargetsComponent.Tag(nametagsRenderer, componentMount);
                     this.entries.put(uUID, tag);
                     this.sceneCornerRadiusService.addChild(componentMount);
                  } else if (tag.component.update(nametagsData, value, (Boolean)this.moduleBool8.get())) {
                     tag.host.invalidateComponent();
                  }

                  tag.host.position(nextHeight.x(), nextHeight.y()).size(nextHeight.width(), nextHeight.height());
                  arrayList.add(nextHeight);
                  hashSet.add(uUID);
               }
            }
         }

         this.entries.entrySet().removeIf(entry -> {
            if (hashSet.contains(entry.getKey())) {
               return false;
            }

            this.sceneCornerRadiusService.removeChild(entry.getValue().host);
            return true;
         });
         this.enabled = true;
      }
   }

   private void updateState3() {
      this.sceneCornerRadiusService.visible(false).clearChildren();
      this.entries.clear();
      this.items2 = List.of();
      NametagsPrepareService.clear();
      this.enabled = false;
   }

   private record Tag(NametagsRenderer component, ComponentMountService host) {
   }
}

