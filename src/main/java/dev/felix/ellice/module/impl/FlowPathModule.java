package dev.felix.ellice.module.impl;

import dev.felix.ellice.compat.CompatStateController;
import dev.felix.ellice.compat.LootScreenController;
import dev.felix.ellice.compat.CompatCurrentService;
import dev.felix.ellice.compat.CompatExecuteService;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventAttackInputService;
import dev.felix.ellice.feature.task.TaskConfigTracker;
import dev.felix.ellice.feature.task.TaskAction;
import dev.felix.ellice.feature.task.TaskResolveService;
import dev.felix.ellice.feature.task.SurvivalTask;
import dev.felix.ellice.feature.task.TaskStep;
import dev.felix.ellice.feature.task.TaskData;
import dev.felix.ellice.feature.task.TreeHarvestTask;
import dev.felix.ellice.feature.task.MissionTask;
import dev.felix.ellice.hud.FlowPathStatusHud;
import dev.felix.ellice.module.ModuleSettingsService;
import dev.felix.ellice.module.ModuleNameService;
import dev.felix.ellice.module.ModuleFeatureType;
import dev.felix.ellice.module.ModuleBuilderData;
import dev.felix.ellice.module.ModuleSetting;
import dev.felix.ellice.ui.component.ComponentMountService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;

public final class FlowPathModule extends ModuleSettingsService {
   private final ModuleNameService moduleNameService = this.settingCategory("Autonomy")
      .description("One task at a time. Stone age gathers the missing materials and builds a stone pickaxe.");
   private final ModuleSetting.Mode moduleMode = this.setting(
      this.moduleNameService,
      new ModuleSetting.Mode("Task", new String[]{"Off", "Mine diamonds", "Chop trees", "Stone age", "Survive"}, "Off")
         .description(
            "Mine diamonds needs an iron pickaxe or better. Chop trees replants saplings on soil. Survive runs the stone age under the survival director."
         )
   );
   private final ModuleSetting.Number moduleNumber = this.setting(
      this.moduleNameService,
      new ModuleSetting.Number("Target count", 2.0F, 1.0F, 64.0F, 1.0F)
         .description(
            "Diamonds or logs to gather before the task reports done. Stone age derives material needs from recipes and current inventory."
         )
   );
   private final ModuleSetting.Bool moduleBool = this.setting(
      this.moduleNameService,
      new ModuleSetting.Bool("Status display", true).description("Shows the current task, stage and abort reason below the crosshair.")
   );
   private final List<TaskResolveService.Recipe> items = TaskResolveService.standard();
   private TaskStep taskOperationHandler3;
   private String text = "";
   private CompatCurrentService compatCurrentService;
   private final CompatExecuteService compatExecuteService = new CompatExecuteService();
   private final CompatStateController compatStateController = new CompatStateController();
   private final LootScreenController compatStateController2 = new LootScreenController();
   private String text2 = "";
   private String text3 = "";
   private long timestamp;
   private String text4 = "";
   private String text5 = "";
   private boolean enabled2;
   private FlowPathStatusHud renderer;
   private ComponentMountService componentMountService;

   public FlowPathModule() {
      super(
         ModuleBuilderData.builder("FlowPath")
            .category(ModuleFeatureType.PLAYER)
            .description(
               "Autonomous mining, woodcutting and tool crafting: scans loaded ground, walks planned routes, mines and crafts vanilla blocks."
            )
            .build()
      );
      this.moduleNumber.visibleWhen(this.moduleMode, item -> !item.equals("Off") && !item.equals("Stone age") && !item.equals("Survive"));
   }

   @Override
   protected void onEnable() {
      this.timestamp = 0L;
      this.text2 = "";
      this.text3 = "";
      CoreIsInitializedHandler.LOGGER.info("[FlowPath] enabled, task={}", this.moduleMode.get());
      this.renderer = new FlowPathStatusHud();
      this.componentMountService = new ComponentMountService().mount(this.renderer, CoreIsInitializedHandler.get().theme());
      this.componentMountService
         .id("flowpath.status")
         .absolute()
         .size(LayoutOperationHandler.percent(100.0F), LayoutOperationHandler.px(16.0F))
         .pointerEvents(false)
         .visible(false);
      CoreIsInitializedHandler.get().scene().root().addChild(this.componentMountService);
      this.on(EventAttackInputService.TICK).run(item -> {
         try {
            this.updateState2();
         } catch (Exception exception) {
            this.updateState4();
            this.text2 = "Task failed: " + exception.getClass().getSimpleName();
            this.timestamp = System.currentTimeMillis() + 10000L;
            String text = String.valueOf(exception);
            if (!text.equals(this.text4)) {
               this.text4 = text;
               CoreIsInitializedHandler.LOGGER.warn("[FlowPath] tick failed", exception);
            }
         }
      });
      this.on(EventAttackInputService.WORLD).run(item -> {
         this.updateState4();
         this.timestamp = 0L;
         this.text3 = "";
         this.text2 = "";
      });
      this.on(EventAttackInputService.RENDER).run(item -> this.updateState());
   }

   @Override
   protected void onDisable() {
      this.updateState4();
      if (this.componentMountService != null) {
         CoreIsInitializedHandler.get().scene().root().removeChild(this.componentMountService);
      }

      this.componentMountService = null;
      this.renderer = null;
   }

   public String status() {
      if (this.taskOperationHandler3 == null) {
         if (!this.text2.isEmpty() && !this.text2.equals("Idle")) {
            return this.text2;
         } else {
            return this.text3.isEmpty() ? "Idle" : this.text3;
         }
      } else {
         return this.text2.isEmpty() ? this.taskOperationHandler3.status() : this.taskOperationHandler3.status() + " · " + this.text2;
      }
   }

   private void updateState() {
      ComponentMountService componentMount = this.componentMountService;
      FlowPathStatusHud flowPathStatusHud = this.renderer;
      if (componentMount != null && flowPathStatusHud != null && this.isEnabled()) {
         CoreIsInitializedHandler coreIsInitialized = CoreIsInitializedHandler.get();
         Minecraft minecraft = CoreIsInitializedHandler.mc();
         int value = this.moduleBool.get()
               && minecraft.player != null
               && minecraft.level != null
               && minecraft.screen == null
               && !minecraft.options.hideGui
               && !coreIsInitialized.screens().isActive()
            ? 1
            : 0;
         componentMount.visible((value != 0));
         if (value != 0) {
            componentMount.position(0.0F, coreIsInitialized.viewport().height() / 2 + 46);
            flowPathStatusHud.update("FlowPath · " + this.status());
         }
      }
   }

   private void updateState2() {
      String currentText = (String)this.moduleMode.get() + "/" + ((Float)this.moduleNumber.get()).intValue();
      if (!currentText.equals(this.text)) {
         this.updateState4();
         this.text = currentText;
         this.timestamp = 0L;
         this.text3 = "";
      }

      if (((String)this.moduleMode.get()).equals("Off")) {
         this.updateState4();
         this.text2 = "";
         this.text3 = "";
      } else if (!CompatCurrentService.senseAvailable()) {
         this.updateState4();
         this.text2 = "Waiting for gameplay";
         this.updateState3("sense-blocked");
      } else if (CompatCurrentService.handsBusy()) {
         this.compatExecuteService.release();
         this.compatStateController.stop();
         this.compatStateController2.stop();
         this.text2 = "Paused for hand owner";
      } else {
         Optional result = CompatCurrentService.current();
         if (result.isEmpty()) {
            this.updateState4();
            this.text2 = "Missing terrain adapter";
            this.updateState3("adapter-missing");
         } else {
            if (this.taskOperationHandler3 == null || !this.text.equals(currentText)) {
               if (this.taskOperationHandler3 == null && System.currentTimeMillis() < this.timestamp) {
                  return;
               }

               this.updateState4();
               this.compatCurrentService = (CompatCurrentService)result.get();

               this.taskOperationHandler3 = switch ((String)this.moduleMode.get()) {
                  case "Mine diamonds" -> new TaskConfigTracker(TaskConfigTracker.Config.diamonds(((Float)this.moduleNumber.get()).intValue()));
                  case "Chop trees" -> new TreeHarvestTask(TreeHarvestTask.Config.overworld(((Float)this.moduleNumber.get()).intValue()));
                  case "Stone age" -> MissionTask.stoneAge();
                  case "Survive" -> new SurvivalTask(SurvivalTask.Config.standard(), MissionTask.stoneAge());
                  default -> null;
               };
               this.text = currentText;
               if (this.taskOperationHandler3 == null) {
                  this.text2 = "";
                  return;
               }

               this.enabled2 = false;
               this.text5 = "";
               this.text4 = "";
               CoreIsInitializedHandler.LOGGER.info("[FlowPath] starting {}", this.taskOperationHandler3.name());
            }

            TaskData taskData = this.compatCurrentService.sense();
            TaskAction taskAction = this.compatStateController
               .pendingCommand()
               .map(item -> (TaskAction)item)
               .orElseGet(() -> this.taskOperationHandler3.tick(System.currentTimeMillis(), this.compatCurrentService));
            if (!this.enabled2) {
               this.enabled2 = true;
               CoreIsInitializedHandler.LOGGER.info("[FlowPath] first command: {}", createText2(taskAction));
            }

            if (taskAction instanceof TaskAction.Craft craft) {
               this.compatExecuteService.release();
               this.compatStateController2.closeIfOwned();
               this.text2 = this.createText(craft, taskData);
            } else if (taskAction instanceof TaskAction.Loot loot) {
               this.compatExecuteService.release();
               this.compatStateController.closeIfOwned();
               this.text2 = this.compatStateController2.tick(loot);
            } else {
               this.compatStateController.closeIfOwned();
               this.compatStateController2.closeIfOwned();
               this.text2 = this.compatExecuteService.execute(taskAction, taskData, this.compatCurrentService.traversal());
            }

            if (taskAction instanceof TaskAction.Done done) {
               CoreIsInitializedHandler.LOGGER.info("[FlowPath] {}", done.summary());
               this.text3 = done.summary();
               this.timestamp = System.currentTimeMillis() + 5000L;
               this.taskOperationHandler3 = null;
               this.compatCurrentService = null;
            } else if (taskAction instanceof TaskAction.Abort abort) {
               CoreIsInitializedHandler.LOGGER.warn("[FlowPath] aborted: {}", abort.reason());
               this.text3 = abort.reason();
               this.timestamp = System.currentTimeMillis() + 10000L;
               this.taskOperationHandler3 = null;
               this.compatCurrentService = null;
            }
         }
      }
   }

   private String createText(TaskAction.Craft craft, TaskData taskData) {
      TaskResolveService.Station station;
      try {
         station = TaskResolveService.stationFor(this.items, craft.outputId());
      } catch (IllegalArgumentException illegalArgumentException) {
         return "No recipe for " + craft.outputId();
      }

      return this.compatStateController.tick(craft, station, taskData);
   }

   private void updateState3(String text) {
      if (!text.equals(this.text5)) {
         this.text5 = text;
         CoreIsInitializedHandler.LOGGER
            .warn(
               "[FlowPath] waiting: {}",
               text.equals("sense-blocked") ? "no gameplay sense (world or player state)" : "no terrain adapter for this version"
            );
      }
   }

   private static String createText2(TaskAction taskAction) {
      if (taskAction instanceof TaskAction.Goto gotoAction) {
         return "Goto " + gotoAction.label() + " waypoints=" + gotoAction.route().size() + " arrived=" + gotoAction.arrived();
      } else if (taskAction instanceof TaskAction.Mine mine) {
         return "Mine " + mine.pos() + " " + mine.expectId();
      } else if (taskAction instanceof TaskAction.Place place) {
         return "Place " + place.id() + " " + place.pos();
      } else if (taskAction instanceof TaskAction.Craft craft) {
         return "Craft " + craft.outputId();
      } else if (taskAction instanceof TaskAction.Strike strike) {
         return "Strike entity " + strike.entityRef();
      } else if (taskAction instanceof TaskAction.Cast cast) {
         return "Cast " + cast.water();
      } else if (taskAction instanceof TaskAction.Reel) {
         return "Reel";
      } else if (taskAction instanceof TaskAction.Loot loot) {
         return "Loot " + loot.pos();
      } else if (taskAction instanceof TaskAction.Wait wait) {
         return "Wait " + wait.reason();
      } else if (taskAction instanceof TaskAction.Done done) {
         return "Done " + done.summary();
      } else {
         return taskAction instanceof TaskAction.Abort abort ? "Abort " + abort.reason() : String.valueOf(taskAction);
      }
   }

   private void updateState4() {
      this.compatExecuteService.release();
      this.compatStateController.stop();
      this.compatStateController2.stop();
      if (this.taskOperationHandler3 != null) {
         this.taskOperationHandler3.stop();
      }

      this.taskOperationHandler3 = null;
      this.compatCurrentService = null;
   }
}

