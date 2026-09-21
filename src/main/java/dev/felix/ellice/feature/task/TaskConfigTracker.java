package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class TaskConfigTracker implements TaskStep {
   private static final double value = 4.2;
   private static final double value2 = 5.2;
   private static final int count2 = 200;
   private static final int count3 = 6;
   private static final int count4 = 3;
   private static final long timestamp = 3000L;
   private final TaskConfigTracker.Config config2;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private TaskConfigTracker.Stage stage = TaskConfigTracker.Stage.SEARCH;
   private List<TaskNearestService.Find> items = List.of();
   private int count5;
   private TaskNearestService.Find find;
   private TaskBlockPosition taskData2;
   private int count6;
   private TaskBlockPosition taskData22;
   private int count7;
   private int count8;
   private ExplorationTask taskConfigTracker3;
   private boolean enabled;
   private int count9;
   private int count10;
   private int count11;
   private TaskStateController taskStateController;
   private TaskAction taskOperationHandler2;
   private String text = "Starting";

   public TaskConfigTracker(TaskConfigTracker.Config currentConfig) {
      this.config2 = Objects.requireNonNull(currentConfig, "config");
   }

   @Override
   public String name() {
      return "Mine " + createText(this.config2.primaryDrop());
   }

   @Override
   public String status() {
      return this.text;
   }

   @Override
   public TaskAction tick(long longValue, TaskOperationHandler taskOperation) {
      if (this.taskOperationHandler2 != null) {
         return this.taskOperationHandler2;
      }

      Objects.requireNonNull(taskOperation, "env");
      TaskData taskData = taskOperation.sense();
      TaskAction taskAction = this.createTaskOperationHandler2(taskData);
      if (taskAction != null) {
         return this.createTaskOperationHandler29(taskAction);
      }

      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
         this.stage = TaskConfigTracker.Stage.SEARCH;
      }
      return switch (this.stage) {
         case SEARCH -> this.createTaskOperationHandler22(longValue, taskOperation, taskData);
         case EXPLORE -> this.createTaskOperationHandler23(longValue, taskOperation, taskData);
         case GOTO -> this.createTaskOperationHandler25(longValue, taskOperation, taskData);
         case MINE -> this.createTaskOperationHandler27(longValue, taskOperation, taskData);
         case COLLECT -> this.createTaskOperationHandler28(longValue, taskOperation, taskData);
      };
   }

   private TaskAction createTaskOperationHandler2(TaskData taskData) {
      TaskAction.Abort abort = DiedComponent.critical(taskData);
      if (abort != null) {
         return abort;
      } else {
         return !this.checkCondition(taskData) ? new TaskAction.Abort("Need " + this.config2.toolName()) : null;
      }
   }

   private boolean checkCondition(TaskData taskData) {
      if (this.config2.toolIds().isEmpty()) {
         return true;
      }

      for (String text : this.config2.toolIds()) {
         if (taskData.count(text) > 0) {
            return true;
         }
      }

      return false;
   }

   private TravelPolicy createTravelData2(TaskData taskData) {
      return TravelPolicy.legit(taskData.canSprint());
   }

   private TaskAction createTaskOperationHandler22(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      this.items = TaskNearestService.nearest(
         taskOperation.blocks(),
         taskData.player().x,
         taskData.player().y,
         taskData.player().z,
         this.config2.oreIds(),
         this.config2.scanRadius(),
         this.config2.yRadius(),
         6
      );
      this.count5 = 0;
      if (this.items.isEmpty()) {
         if (!this.enabled) {
            this.stage = TaskConfigTracker.Stage.EXPLORE;
            return this.createTaskOperationHandler23(size, taskOperation, taskData);
         } else {
            return this.createTaskOperationHandler29(
               new TaskAction.Abort("No " + createText(this.config2.primaryDrop()) + " ore anywhere — ground is empty")
            );
         }
      } else {
         return this.createTaskOperationHandler24(taskOperation, taskData);
      }
   }

   private TaskAction createTaskOperationHandler23(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.taskConfigTracker3 == null) {
         this.taskConfigTracker3 = new ExplorationTask(
            ExplorationTask.Config.standard(this.config2.oreIds(), this.config2.scanRadius(), this.config2.yRadius()),
            createText(this.config2.primaryDrop()) + " ore"
         );
      }

      TaskAction taskAction = this.taskConfigTracker3.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done) {
         this.taskConfigTracker3 = null;
         this.enabled = true;
         this.stage = TaskConfigTracker.Stage.SEARCH;
         return this.createTaskOperationHandler22(size, taskOperation, taskData);
      } else if (taskAction instanceof TaskAction.Abort abort) {
         return this.createTaskOperationHandler29(
            new TaskAction.Abort("No " + createText(this.config2.primaryDrop()) + " ore anywhere: " + abort.reason())
         );
      } else {
         this.text = "Exploring · " + this.taskConfigTracker3.status();
         return taskAction;
      }
   }

   private TaskAction createTaskOperationHandler24(TaskOperationHandler taskOperation, TaskData taskData) {
      this.find = this.items.get(Math.min(this.count5, this.items.size() - 1));
      if (this.find.pos().equals(this.taskData22) && this.count11 == this.count7) {
         if (++this.count8 >= 2) {
            return this.createTaskOperationHandler29(new TaskAction.Abort("Ore will not break — moving on"));
         }
      } else {
         this.count8 = 0;
         this.taskData22 = this.find.pos();
         this.count7 = this.count11;
      }

      ArrayList arrayList = new ArrayList();

      for (TaskNearestService.Find currentFind : this.items) {
         arrayList.add(new TravelIsInGoalHandler.NearGoal(currentFind.x(), currentFind.y(), currentFind.z(), 2.5));
      }

      TravelIsInGoalHandler.CompositeGoal compositeGoal = new TravelIsInGoalHandler.CompositeGoal(arrayList);
      this.travelStatusTracker.start(compositeGoal, this.createTravelData2(taskData), taskData.player());
      this.stage = TaskConfigTracker.Stage.GOTO;
      this.text = "Getting to "
         + createText(this.find.id())
         + " ("
         + this.count11
         + "/"
         + this.config2.targetCount()
         + ")";
      return TaskAction.go(compositeGoal, this.text, this.travelStatusTracker.route(), false, this.createTravelData2(taskData));
   }

   private TaskAction createTaskOperationHandler25(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      this.travelStatusTracker.tick(taskData.player());
      if (this.travelStatusTracker.status() == TravelStatusTracker.Status.ARRIVED
         || checkCondition2(taskData, this.find, 4.2)) {
         this.stage = TaskConfigTracker.Stage.MINE;
         this.count10 = this.calculateValue(taskData);
         this.text = "Mining " + createText(this.find.id());
         return new TaskAction.Mine(this.find.pos(), this.find.id());
      }

      if (this.travelStatusTracker.status() == TravelStatusTracker.Status.UNAVAILABLE) {
         return this.createTaskOperationHandler26(taskOperation, taskData);
      }

      if (this.travelStatusTracker.status() == TravelStatusTracker.Status.UNEXPLORED) {
         return this.createTaskOperationHandler29(new TaskAction.Abort("Ground not loaded"));
      }

      this.text = "Getting to "
         + createText(this.find.id())
         + " ("
         + this.count11
         + "/"
         + this.config2.targetCount()
         + ")";
      return TaskAction.go(this.createTravelIsInGoalHandler(), this.text, this.travelStatusTracker.route(), false, this.createTravelData2(taskData));
   }

   private TaskAction createTaskOperationHandler26(TaskOperationHandler taskOperation, TaskData taskData) {
      this.taskData2 = null;
      this.count6 = 0;
      return ++this.count5 < Math.min(3, this.items.size())
         ? this.createTaskOperationHandler24(taskOperation, taskData)
         : this.createTaskOperationHandler29(new TaskAction.Abort("Ore unreachable"));
   }

   private TravelIsInGoalHandler createTravelIsInGoalHandler() {
      ArrayList arrayList = new ArrayList();

      for (TaskNearestService.Find find : this.items) {
         arrayList.add(new TravelIsInGoalHandler.NearGoal(find.x(), find.y(), find.z(), 2.5));
      }

      return new TravelIsInGoalHandler.CompositeGoal(arrayList);
   }

   private TaskAction createTaskOperationHandler27(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      String currentX = taskOperation.blocks().blockId(this.find.x(), this.find.y(), this.find.z());
      if (!this.config2.oreIds().contains(currentX)) {
         this.stage = TaskConfigTracker.Stage.COLLECT;
         this.taskStateController = new TaskStateController(this.config2.dropIds(), this.find.pos());
         this.taskData2 = null;
         this.count6 = 0;
         this.count9 = Math.min(this.count10, this.calculateValue(taskData));
         this.text = "Collecting drops";
         return this.createTaskOperationHandler28(size, taskOperation, taskData);
      }

      if (!checkCondition2(taskData, this.find, 5.2)) {
         this.stage = TaskConfigTracker.Stage.GOTO;
         TravelIsInGoalHandler.NearGoal nextX = new TravelIsInGoalHandler.NearGoal(this.find.x(), this.find.y(), this.find.z(), 1.0);
         this.travelStatusTracker.start(nextX, this.createTravelData2(taskData), taskData.player());
         this.text = "Getting closer";
         return this.createTaskOperationHandler25(size, taskOperation, taskData);
      }

      if (!this.find.pos().equals(this.taskData2)) {
         this.taskData2 = this.find.pos();
         this.count6 = 0;
      } else if (++this.count6 > 200) {
         this.text = "Skipping stuck rock";
         return this.createTaskOperationHandler26(taskOperation, taskData);
      }

      return new TaskAction.Mine(this.find.pos(), currentX);
   }

   private TaskAction createTaskOperationHandler28(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      int value = this.calculateValue(taskData);
      if (value > this.count9) {
         this.count11 = this.count11 + (value - this.count9);
         this.count9 = value;
      }

      if (this.count11 >= this.config2.targetCount()) {
         return this.createTaskOperationHandler29(new TaskAction.Done("Mined " + this.count11 + " " + createText(this.config2.primaryDrop())));
      } else {
         TaskAction taskAction = this.taskStateController.tick(size, taskOperation);
         if (taskAction instanceof TaskAction.Abort abort) {
            return this.createTaskOperationHandler29(abort);
         } else if (taskAction instanceof TaskAction.Done) {
            this.stage = TaskConfigTracker.Stage.SEARCH;
            return this.createTaskOperationHandler22(size, taskOperation, taskData);
         } else {
            this.text = "Collecting drops (" + this.count11 + "/" + this.config2.targetCount() + ")";
            return taskAction;
         }
      }
   }

   private int calculateValue(TaskData taskData) {
      int value = 0;

      for (String text : this.config2.dropIds()) {
         value += taskData.count(text);
      }

      return value;
   }

   private static boolean checkCondition2(TaskData taskData, TaskNearestService.Find find, double doubleValue) {
      return find.pos().distSq(taskData.player().x, taskData.player().y, taskData.player().z) <= doubleValue * doubleValue;
   }

   private TaskAction createTaskOperationHandler29(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text);
      return taskAction;
   }

   private static String createText(String text) {
      return text.startsWith("minecraft:") ? text.substring("minecraft:".length()) : text;
   }

   public record Config(
      Set<String> oreIds, Set<String> dropIds, Set<String> toolIds, String toolName, int targetCount, int scanRadius, int yRadius
   ) {
      public Config(
         Set<String> oreIds, Set<String> dropIds, Set<String> toolIds, String toolName, int targetCount, int scanRadius, int yRadius
      ) {
         oreIds = Set.copyOf(Objects.requireNonNull(oreIds, "oreIds"));
         dropIds = Set.copyOf(Objects.requireNonNull(dropIds, "dropIds"));
         toolIds = Set.copyOf(Objects.requireNonNull(toolIds, "toolIds"));
         if (!oreIds.isEmpty()
            && !dropIds.isEmpty()
            && targetCount >= 1
            && targetCount <= 64
            && scanRadius >= 4
            && scanRadius <= 64
            && yRadius >= 0
            && yRadius <= 64) {
            toolName = toolName == null ? "" : toolName;
            this.oreIds = oreIds;
            this.dropIds = dropIds;
            this.toolIds = toolIds;
            this.toolName = toolName;
            this.targetCount = targetCount;
            this.scanRadius = scanRadius;
            this.yRadius = yRadius;
         } else {
            throw new IllegalArgumentException("Invalid mine config");
         }
      }

      public String primaryDrop() {
         return this.dropIds.stream().sorted().findFirst().orElse("");
      }

      public static TaskConfigTracker.Config diamonds(int value) {
         return new TaskConfigTracker.Config(
            Set.of("minecraft:diamond_ore", "minecraft:deepslate_diamond_ore"),
            Set.of("minecraft:diamond"),
            Set.of("minecraft:iron_pickaxe", "minecraft:diamond_pickaxe", "minecraft:netherite_pickaxe"),
            "an iron pickaxe or better",
            value,
            24,
            12
         );
      }

      public static TaskConfigTracker.Config gather(Set<String> values, Set<String> currentValues, int value, int currentValue, int nextValue) {
         return new TaskConfigTracker.Config(
            values,
            currentValues,
            Set.of(
               "minecraft:wooden_pickaxe",
               "minecraft:stone_pickaxe",
               "minecraft:iron_pickaxe",
               "minecraft:diamond_pickaxe",
               "minecraft:netherite_pickaxe"
            ),
            "a pickaxe",
            value,
            currentValue,
            nextValue
         );
      }

      public static TaskConfigTracker.Config cobblestone(int value) {
         return gather(
            Set.of("minecraft:stone", "minecraft:cobblestone", "minecraft:deepslate", "minecraft:cobbled_deepslate"),
            Set.of("minecraft:cobblestone", "minecraft:cobbled_deepslate"),
            value,
            16,
            8
         );
      }
   }

   private enum Stage {
      SEARCH,
      EXPLORE,
      GOTO,
      MINE,
      COLLECT;


      private static TaskConfigTracker.Stage[] $values() {
         return new TaskConfigTracker.Stage[]{SEARCH, EXPLORE, GOTO, MINE, COLLECT};
      }
   }
}

