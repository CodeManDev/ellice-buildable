package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;

public final class TreeHarvestTask implements TaskStep {
   private static final Set<String> text = Set.of(
      "minecraft:oak_log",
      "minecraft:spruce_log",
      "minecraft:birch_log",
      "minecraft:jungle_log",
      "minecraft:acacia_log",
      "minecraft:dark_oak_log",
      "minecraft:mangrove_log",
      "minecraft:cherry_log",
      "minecraft:pale_oak_log"
   );
   private static final double value = 4.2;
   private static final double value2 = 5.2;
   private static final int count2 = 200;
   private static final int count3 = 12;
   private final TreeHarvestTask.Config config2;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private TreeHarvestTask.Stage stage = TreeHarvestTask.Stage.SEARCH;
   private TaskNearestService.Find find;
   private int count4;
   private int count5;
   private String text2 = "";
   private TaskBlockPosition taskData2;
   private int fcpgshtkp0;
   private final Deque<TaskBlockPosition> items = new ArrayDeque<>();
   private int count6;
   private int count7 = -1;
   private final ToIntFunction<TaskData> toIntFunction;
   private TaskStateController taskStateController;
   private boolean enabled;
   private int count8;
   private int count9;
   private boolean enabled2;
   private ExplorationTask taskConfigTracker3;
   private boolean enabled3;
   private TaskBlockPosition taskData22;
   private int count10;
   private int count11;
   private TaskAction taskOperationHandler2;
   private String text3 = "Starting";

   public static Set<String> trunkIds() {
      return text;
   }

   public TreeHarvestTask(TreeHarvestTask.Config config) {
      this(config, null);
   }

   public TreeHarvestTask(TreeHarvestTask.Config currentConfig, ToIntFunction<TaskData> currentToIntFunction) {
      this.config2 = Objects.requireNonNull(currentConfig, "config");
      this.toIntFunction = currentToIntFunction;
   }

   private int calculateValue(TaskData taskData) {
      return this.toIntFunction == null
         ? Math.max(0, this.config2.targetLogs() - this.count6)
         : Math.max(0, this.toIntFunction.applyAsInt(taskData));
   }

   private String createText(TaskData taskData) {
      return this.count6 + " collected, " + this.calculateValue(taskData) + " needed";
   }

   @Override
   public String name() {
      return "Chop trees";
   }

   @Override
   public String status() {
      return this.text3;
   }

   @Override
   public TaskAction tick(long longValue, TaskOperationHandler taskOperation) {
      if (this.taskOperationHandler2 != null) {
         return this.taskOperationHandler2;
      }

      Objects.requireNonNull(taskOperation, "env");
      TaskData taskData = taskOperation.sense();
      int currentCount = this.config2.logIds().stream().mapToInt(taskData::count).sum();
      if (this.count7 < 0) {
         this.count7 = currentCount;
      }

      this.count6 = Math.max(0, currentCount - this.count7);
      TaskAction.Abort abort = DiedComponent.critical(taskData);
      if (abort != null) {
         return this.createTaskOperationHandler210(abort);
      }

      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
         this.stage = TreeHarvestTask.Stage.SEARCH;
      }
      return switch (this.stage) {
         case SEARCH -> this.createTaskOperationHandler2(longValue, taskOperation, taskData);
         case EXPLORE -> this.createTaskOperationHandler28(longValue, taskOperation, taskData);
         case GOTO -> this.createTaskOperationHandler22(longValue, taskOperation, taskData);
         case CHOP -> this.createTaskOperationHandler23(longValue, taskOperation, taskData);
         case LEAVES -> this.createTaskOperationHandler24(longValue, taskOperation, taskData);
         case COLLECT -> this.createTaskOperationHandler26(longValue, taskOperation, taskData);
         case REPLANT -> this.createTaskOperationHandler27(longValue, taskOperation, taskData);
      };
   }

   private TaskAction createTaskOperationHandler2(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.calculateValue(taskData) == 0) {
         return this.createTaskOperationHandler210(new TaskAction.Done("Collected " + this.count6 + " logs"));
      }

      List currentItems = TaskNearestService.nearest(
         taskOperation.blocks(),
         taskData.player().x,
         taskData.player().y,
         taskData.player().z,
         this.config2.logIds(),
         this.config2.scanRadius(),
         this.config2.yRadius(),
         4
      );
      if (currentItems.isEmpty()) {
         if (!this.enabled3) {
            this.stage = TreeHarvestTask.Stage.EXPLORE;
            return this.createTaskOperationHandler28(size, taskOperation, taskData);
         } else {
            return this.createTaskOperationHandler210(new TaskAction.Abort("No trees anywhere — ground is empty"));
         }
      } else {
         this.find = this.createFind(taskOperation, (TaskNearestService.Find)currentItems.getFirst());
         TaskBlockPosition currentX = new TaskBlockPosition(this.find.x(), this.find.y(), this.find.z());
         if (currentX.equals(this.taskData22) && this.count6 == this.count10) {
            if (++this.count11 >= 2) {
               return this.createTaskOperationHandler210(new TaskAction.Abort("Trees will not break — moving on"));
            }
         } else {
            this.count11 = 0;
            this.taskData22 = currentX;
            this.count10 = this.count6;
         }

         this.count8 = this.count6;
         this.count5 = this.find.y();

         while (
            this.count5 - this.find.y() < 12
               && this.config2
                  .logIds()
                  .contains(taskOperation.blocks().blockId(this.find.x(), this.count5 + 1, this.find.z()))
         ) {
            this.count5++;
         }

         this.count4 = this.find.y();
         this.text2 = taskOperation.blocks().blockId(this.find.x(), this.count4, this.find.z());
         this.items.clear();
         this.enabled2 = false;
         TravelIsInGoalHandler.NearGoal nextX = new TravelIsInGoalHandler.NearGoal(
            this.find.x(), this.find.y(), this.find.z(), 2.5
         );
         this.travelStatusTracker.start(nextX, this.createTravelData2(taskData), taskData.player());
         this.stage = TreeHarvestTask.Stage.GOTO;
         this.text3 = "Getting to a tree (" + this.createText(taskData) + ")";
         return TaskAction.go(nextX, this.text3, this.travelStatusTracker.route(), false, this.createTravelData2(taskData));
      }
   }

   private TaskNearestService.Find createFind(TaskOperationHandler taskOperation, TaskNearestService.Find find) {
      int currentY = find.y();

      while (currentY - 1 >= taskOperation.blocks().minY() && this.config2.logIds().contains(taskOperation.blocks().blockId(find.x(), currentY - 1, find.z()))) {
         currentY += -1;
      }

      return new TaskNearestService.Find(find.x(), currentY, find.z(), taskOperation.blocks().blockId(find.x(), currentY, find.z()), find.distSq());
   }

   private TravelPolicy createTravelData2(TaskData taskData) {
      return TravelPolicy.legit(taskData.canSprint());
   }

   private TaskAction createTaskOperationHandler22(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      this.travelStatusTracker.tick(taskData.player());
      TravelIsInGoalHandler.NearGoal currentX = new TravelIsInGoalHandler.NearGoal(
         this.find.x(), this.find.y(), this.find.z(), 2.5
      );
      if (this.travelStatusTracker.status() == TravelStatusTracker.Status.ARRIVED
         || new TaskBlockPosition(this.find.x(), this.count4, this.find.z())
               .distSq(taskData.player().x, taskData.player().y, taskData.player().z)
            <= 17.64) {
         this.stage = TreeHarvestTask.Stage.CHOP;
         return this.createTaskOperationHandler23(size, taskOperation, taskData);
      } else if (this.travelStatusTracker.status() != TravelStatusTracker.Status.UNAVAILABLE
         && this.travelStatusTracker.status() != TravelStatusTracker.Status.UNEXPLORED) {
         this.text3 = "Getting to a tree (" + this.createText(taskData) + ")";
         return TaskAction.go(currentX, this.text3, this.travelStatusTracker.route(), false, this.createTravelData2(taskData));
      } else {
         return this.createTaskOperationHandler210(new TaskAction.Abort("Tree unreachable"));
      }
   }

   private TaskAction createTaskOperationHandler23(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.count4 > this.count5) {
         return this.createTaskOperationHandler25(size, taskOperation, taskData, false);
      }

      String currentX = taskOperation.blocks().blockId(this.find.x(), this.count4, this.find.z());
      if (!this.config2.logIds().contains(currentX)) {
         this.text2 = currentX;
         this.count4++;
         return this.createTaskOperationHandler23(size, taskOperation, taskData);
      }

      this.text2 = currentX;
      TaskBlockPosition nextX = new TaskBlockPosition(this.find.x(), this.count4, this.find.z());
      if (nextX.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 27.040000000000003) {
         this.stage = TreeHarvestTask.Stage.GOTO;
         TravelIsInGoalHandler.NearGoal previousX = new TravelIsInGoalHandler.NearGoal(this.find.x(), this.count4, this.find.z(), 1.0);
         this.travelStatusTracker.start(previousX, this.createTravelData2(taskData), taskData.player());
         this.text3 = "Getting closer";
         return this.createTaskOperationHandler22(size, taskOperation, taskData);
      }

      if (!nextX.equals(this.taskData2)) {
         this.taskData2 = nextX;
         this.fcpgshtkp0 = 0;
      } else if (++this.fcpgshtkp0 > 200) {
         this.taskData2 = null;
         this.fcpgshtkp0 = 0;
         this.text2 = "";
         this.count4++;
         this.text3 = "Skipping stuck log";
         return this.createTaskOperationHandler23(size, taskOperation, taskData);
      }

      this.text3 = "Chopping (" + this.createText(taskData) + ")";
      return new TaskAction.Mine(nextX, currentX);
   }

   private void updateState(TaskOperationHandler taskOperation, TaskData taskData) {
      List<TaskNearestService.Find> currentX = TaskNearestService.nearest(
         taskOperation.blocks(),
         this.find.x() + 0.5,
         this.find.y() + 2,
         this.find.z() + 0.5,
         this.config2.leafIds(),
         4,
         6,
         this.config2.maxLeaves() + 4
      );
      ArrayList<TaskNearestService.Find> arrayList = new ArrayList<>(currentX);
      arrayList.sort((item, currentItem) -> {
         double doubleValue = item.pos().distSq(taskData.player().x, taskData.player().y, taskData.player().z);
         double currentDoubleValue = currentItem.pos().distSq(taskData.player().x, taskData.player().y, taskData.player().z);
         return Double.compare(doubleValue, currentDoubleValue);
      });

      for (int index = 0; index < Math.min(this.config2.maxLeaves(), arrayList.size()); index++) {
         this.items.addLast(((TaskNearestService.Find)arrayList.get(index)).pos());
      }
   }

   private TaskAction createTaskOperationHandler24(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      while (
         !this.items.isEmpty()
            && !this.config2
               .leafIds()
               .contains(
                  taskOperation.blocks()
                     .blockId(this.items.getFirst().x(), this.items.getFirst().y(), this.items.getFirst().z())
               )
      ) {
         this.items.removeFirst();
      }

      if (this.items.isEmpty()) {
         return this.createTaskOperationHandler25(size, taskOperation, taskData, true);
      }

      TaskBlockPosition taskBlockPosition = this.items.getFirst();
      if (taskBlockPosition.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 20.25) {
         this.items.clear();
         return this.createTaskOperationHandler25(size, taskOperation, taskData, true);
      }

      if (!taskBlockPosition.equals(this.taskData2)) {
         this.taskData2 = taskBlockPosition;
         this.fcpgshtkp0 = 0;
      } else if (++this.fcpgshtkp0 > 200) {
         this.items.removeFirst();
         this.taskData2 = null;
         this.fcpgshtkp0 = 0;
         return this.createTaskOperationHandler24(size, taskOperation, taskData);
      }

      this.text3 = "Clearing leaves";
      return new TaskAction.Mine(taskBlockPosition, taskOperation.blocks().blockId(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z()));
   }

   private TaskAction createTaskOperationHandler25(long size, TaskOperationHandler taskOperation, TaskData taskData, boolean currentEnabled) {
      this.enabled = currentEnabled;
      HashSet hashSet = new HashSet<>(this.config2.logIds());
      hashSet.addAll(this.config2.saplingFor().values());
      hashSet.add("minecraft:apple");
      this.taskStateController = new TaskStateController(hashSet, new TaskBlockPosition(this.find.x(), this.find.y(), this.find.z()));
      this.stage = TreeHarvestTask.Stage.COLLECT;
      return this.createTaskOperationHandler26(size, taskOperation, taskData);
   }

   private TaskAction createTaskOperationHandler26(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      TaskAction taskAction = this.taskStateController.tick(size, taskOperation);
      this.text3 = "Collecting wood (" + this.createText(taskData) + ") · " + this.taskStateController.status();
      if (taskAction instanceof TaskAction.Abort abort) {
         return this.createTaskOperationHandler210(abort);
      } else {
         if (!(taskAction instanceof TaskAction.Done)) {
            return taskAction;
         }

         this.taskStateController = null;
         if (this.enabled) {
            this.stage = TreeHarvestTask.Stage.REPLANT;
            return this.createTaskOperationHandler27(size, taskOperation, taskData);
         }

         String text = this.config2.saplingFor().getOrDefault(this.find.id(), "");
         if (this.calculateValue(taskData) != 0 || !text.isEmpty() && taskData.count(text) <= 0) {
            if (this.count6 == this.count8 && this.calculateValue(taskData) > 0) {
               if (++this.count9 >= 2) {
                  return this.createTaskOperationHandler210(new TaskAction.Abort("No wood reached the inventory"));
               }
            } else {
               this.count9 = 0;
            }

            this.updateState(taskOperation, taskData);
            this.stage = TreeHarvestTask.Stage.LEAVES;
            return this.createTaskOperationHandler24(size, taskOperation, taskData);
         } else {
            this.stage = TreeHarvestTask.Stage.REPLANT;
            return this.createTaskOperationHandler27(size, taskOperation, taskData);
         }
      }
   }

   private TaskAction createTaskOperationHandler27(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      TaskBlockPosition currentX = new TaskBlockPosition(this.find.x(), this.find.y(), this.find.z());
      String text = this.config2.saplingFor().getOrDefault(this.find.id(), "");
      if (this.enabled2) {
         this.text3 = taskOperation.blocks().blockId(currentX.x(), currentX.y(), currentX.z()).equals(text) ? "Replanted" : "Replant failed";
         return this.createTaskOperationHandler29(size, taskOperation, taskData);
      } else {
         String nextX = taskOperation.blocks().blockId(this.find.x(), this.find.y() - 1, this.find.z());
         boolean previousX = taskOperation.blocks().blockId(currentX.x(), currentX.y(), currentX.z()).equals("minecraft:air");
         if (!text.isEmpty() && this.config2.soilIds().contains(nextX) && previousX && taskData.count(text) > 0) {
            this.enabled2 = true;
            this.text3 = "Replanting";
            return new TaskAction.Place(text, currentX, TaskAction.Face.UP);
         } else {
            this.text3 = "Skipping replant (no sapling or soil)";
            return this.createTaskOperationHandler29(size, taskOperation, taskData);
         }
      }
   }

   private TaskAction createTaskOperationHandler28(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.taskConfigTracker3 == null) {
         this.taskConfigTracker3 = new ExplorationTask(
            ExplorationTask.Config.standard(this.config2.logIds(), this.config2.scanRadius(), this.config2.yRadius()), "trees"
         );
      }

      TaskAction taskAction = this.taskConfigTracker3.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done) {
         this.taskConfigTracker3 = null;
         this.enabled3 = true;
         this.stage = TreeHarvestTask.Stage.SEARCH;
         return this.createTaskOperationHandler2(size, taskOperation, taskData);
      } else if (taskAction instanceof TaskAction.Abort abort) {
         return this.createTaskOperationHandler210(new TaskAction.Abort("No trees anywhere: " + abort.reason()));
      } else {
         this.text3 = "Exploring · " + this.taskConfigTracker3.status();
         return taskAction;
      }
   }

   private TaskAction createTaskOperationHandler29(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.calculateValue(taskData) == 0) {
         return this.createTaskOperationHandler210(new TaskAction.Done("Collected " + this.count6 + " logs"));
      }

      this.stage = TreeHarvestTask.Stage.SEARCH;
      return this.createTaskOperationHandler2(size, taskOperation, taskData);
   }

   private TaskAction createTaskOperationHandler210(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text3 = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text3);
      return taskAction;
   }

   public record Config(
      Set<String> logIds,
      Map<String, String> saplingFor,
      Set<String> leafIds,
      Set<String> soilIds,
      int targetLogs,
      int scanRadius,
      int yRadius,
      int maxLeaves
   ) {
      public Config(
         Set<String> logIds,
         Map<String, String> saplingFor,
         Set<String> leafIds,
         Set<String> soilIds,
         int targetLogs,
         int scanRadius,
         int yRadius,
         int maxLeaves
      ) {
         logIds = Set.copyOf(Objects.requireNonNull(logIds, "logIds"));
         saplingFor = Map.copyOf(Objects.requireNonNull(saplingFor, "saplingFor"));
         leafIds = Set.copyOf(Objects.requireNonNull(leafIds, "leafIds"));
         soilIds = Set.copyOf(Objects.requireNonNull(soilIds, "soilIds"));
         if (!logIds.isEmpty()
            && targetLogs >= 1
            && targetLogs <= 256
            && scanRadius >= 4
            && scanRadius <= 64
            && yRadius >= 0
            && yRadius <= 32
            && maxLeaves >= 0) {
            this.logIds = logIds;
            this.saplingFor = saplingFor;
            this.leafIds = leafIds;
            this.soilIds = soilIds;
            this.targetLogs = targetLogs;
            this.scanRadius = scanRadius;
            this.yRadius = yRadius;
            this.maxLeaves = maxLeaves;
         } else {
            throw new IllegalArgumentException("Invalid wood config");
         }
      }

      public static TreeHarvestTask.Config overworld(int value) {
         Set values = TreeHarvestTask.trunkIds();
         Map entries = Map.of(
            "minecraft:oak_log",
            "minecraft:oak_sapling",
            "minecraft:spruce_log",
            "minecraft:spruce_sapling",
            "minecraft:birch_log",
            "minecraft:birch_sapling",
            "minecraft:jungle_log",
            "minecraft:jungle_sapling",
            "minecraft:acacia_log",
            "minecraft:acacia_sapling",
            "minecraft:dark_oak_log",
            "minecraft:dark_oak_sapling",
            "minecraft:mangrove_log",
            "minecraft:mangrove_propagule",
            "minecraft:cherry_log",
            "minecraft:cherry_sapling",
            "minecraft:pale_oak_log",
            "minecraft:pale_oak_sapling"
         );
         Set currentValues = Set.of(
            "minecraft:oak_leaves",
            "minecraft:spruce_leaves",
            "minecraft:birch_leaves",
            "minecraft:jungle_leaves",
            "minecraft:acacia_leaves",
            "minecraft:dark_oak_leaves",
            "minecraft:mangrove_leaves",
            "minecraft:cherry_leaves",
            "minecraft:pale_oak_leaves",
            "minecraft:azalea_leaves",
            "minecraft:flowering_azalea_leaves"
         );
         Set nextValues = Set.of(
            "minecraft:dirt",
            "minecraft:grass_block",
            "minecraft:podzol",
            "minecraft:coarse_dirt",
            "minecraft:rooted_dirt",
            "minecraft:mud",
            "minecraft:muddy_mangrove_roots"
         );
         return new TreeHarvestTask.Config(values, entries, currentValues, nextValues, value, 24, 10, 12);
      }
   }

   private enum Stage {
      SEARCH,
      EXPLORE,
      GOTO,
      CHOP,
      LEAVES,
      COLLECT,
      REPLANT;


      private static TreeHarvestTask.Stage[] $values() {
         return new TreeHarvestTask.Stage[]{SEARCH, EXPLORE, GOTO, CHOP, LEAVES, COLLECT, REPLANT};
      }
   }
}

