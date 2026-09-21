package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.survival.SurvivalAllService;
import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class FoodGatheringTask implements TaskStep {
   public static final List<FoodGatheringTask.CropKind> CROPS = List.of(
      new FoodGatheringTask.CropKind(Set.of("minecraft:wheat"), 7, "minecraft:wheat_seeds"),
      new FoodGatheringTask.CropKind(Set.of("minecraft:carrots"), 7, "minecraft:carrot"),
      new FoodGatheringTask.CropKind(Set.of("minecraft:potatoes"), 7, "minecraft:potato"),
      new FoodGatheringTask.CropKind(Set.of("minecraft:beetroots"), 3, "minecraft:beetroot_seeds"),
      new FoodGatheringTask.CropKind(Set.of("minecraft:melon"), -1, ""),
      new FoodGatheringTask.CropKind(Set.of("minecraft:pumpkin"), -1, "")
   );
   public static final Set<String> SOIL = Set.of("minecraft:farmland");
   public static final Set<String> PREY = Set.of(
      "minecraft:cow", "minecraft:pig", "minecraft:sheep", "minecraft:chicken", "minecraft:rabbit", "minecraft:mooshroom"
   );
   public static final Set<String> LOOTABLE = Set.of("minecraft:chest", "minecraft:barrel");
   public static final String ROD = "minecraft:fishing_rod";
   public static final String WATER = "minecraft:water";
   public static final String HOOK = "minecraft:fishing_bobber";
   private static final double value = 4.2;
   private static final double value2 = 5.2;
   private static final double value3 = 3.0;
   private static final double value4 = 3.5;
   private static final int count2 = 200;
   private final FoodGatheringTask.Config config2;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private FoodGatheringTask.Stage stage = FoodGatheringTask.Stage.HARVEST;
   private int count3;
   private int count4;
   private boolean enabled;
   private int count5;
   private int count6;
   private int count7;
   private int count8;
   private long timestamp;
   private long timestamp2;
   private long timestamp3;
   private TaskBlockPosition taskData2;
   private TaskBlockPosition taskData22;
   private String text = "";
   private int count9 = -1;
   private int count10;
   private int count11 = -1;
   private int count12;
   private int count13;
   private int count14;
   private TaskBlockPosition taskData23;
   private int count15;
   private final Set<TaskBlockPosition> values2 = new HashSet<>();
   private ExplorationTask taskConfigTracker3;
   private boolean enabled2;
   private double value5;
   private double value6;
   private double value7;
   private final Set<Integer> values3 = new HashSet<>();
   private long timestamp4;
   private TaskStateController taskStateController;
   private TaskAction taskOperationHandler2;
   private String text2 = "Starting";

   public FoodGatheringTask(FoodGatheringTask.Config currentConfig) {
      this.config2 = Objects.requireNonNull(currentConfig, "config");
   }

   @Override
   public String name() {
      return "Gather food";
   }

   @Override
   public String status() {
      return this.text2;
   }

   @Override
   public TaskAction tick(long longValue, TaskOperationHandler taskOperation) {
      if (this.taskOperationHandler2 != null) {
         return this.taskOperationHandler2;
      }

      Objects.requireNonNull(taskOperation, "env");
      TaskData taskData = taskOperation.sense();
      TaskAction.Abort abort = DiedComponent.critical(taskData);
      if (abort != null) {
         return this.createTaskOperationHandler28(abort);
      }

      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
         this.taskData22 = null;
      }

      if (edible(taskData) >= this.config2.wantItems()) {
         return this.createTaskOperationHandler28(new TaskAction.Done("Gathered " + edible(taskData) + " food"));
      }

      return switch (this.stage) {
         case HARVEST -> this.createTaskOperationHandler23(longValue, taskOperation, taskData);
         case HUNT -> this.createTaskOperationHandler25(longValue, taskOperation, taskData);
         case FISH -> this.createTaskOperationHandler26(longValue, taskOperation, taskData);
         case LOOT -> this.createTaskOperationHandler27(longValue, taskOperation, taskData);
         case EXPLORE -> this.createTaskOperationHandler24(longValue, taskOperation, taskData);
      };
   }

   static int edible(TaskData taskData) {
      int value = 0;

      for (String text : SurvivalAllService.all().keySet()) {
         value += taskData.count(text);
      }

      return value;
   }

   private TravelPolicy createTravelData2(TaskData taskData) {
      return TravelPolicy.legit(taskData.canSprint());
   }

   private TaskAction createTaskOperationHandler2(TaskOperationHandler taskOperation, TaskData taskData, TaskBlockPosition taskBlockPosition, double doubleValue, String text) {
      if (!taskBlockPosition.equals(this.taskData22)) {
         this.taskData22 = taskBlockPosition;
         TravelIsInGoalHandler.NearGoal currentX = new TravelIsInGoalHandler.NearGoal(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z(), doubleValue);
         this.travelStatusTracker.start(currentX, this.createTravelData2(taskData), taskData.player());
      }

      this.travelStatusTracker.tick(taskData.player());
      this.text2 = text;
      TravelIsInGoalHandler.NearGoal nextX = new TravelIsInGoalHandler.NearGoal(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z(), doubleValue);
      return TaskAction.go(nextX, this.text2, this.travelStatusTracker.route(), false, this.createTravelData2(taskData));
   }

   private void updateState(FoodGatheringTask.Stage currentStage, long offset, long currentOffset) {
      this.stage = currentStage;
      this.enabled = false;
      this.timestamp = offset + currentOffset;
      this.taskData2 = null;
      this.taskData22 = null;
      this.text = "";
      this.taskData23 = null;
      this.count15 = 0;
      this.values2.clear();
      this.count9 = -1;
      this.count11 = -1;
      this.count12 = this.count13 = 0;
      this.count8 = 0;
      this.count14 = -1;
   }

   private TaskAction createTaskOperationHandler22(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.count3 > 0) {
         return this.createTaskOperationHandler28(
            new TaskAction.Done("Gathered " + this.count3 + "/" + this.config2.wantItems() + " food — wilds are empty")
         );
      } else if (!this.enabled2) {
         this.stage = FoodGatheringTask.Stage.EXPLORE;
         return this.createTaskOperationHandler24(size, taskOperation, taskData);
      } else {
         return this.createTaskOperationHandler28(new TaskAction.Abort("No food found — wilds are empty"));
      }
   }

   private static FoodGatheringTask.CropKind createCropKind(String text) {
      for (FoodGatheringTask.CropKind cropKind : CROPS) {
         if (cropKind.ids().contains(text)) {
            return cropKind;
         }
      }

      return null;
   }

   private static boolean checkCondition(TaskWorldView taskWorldView, int value, int currentValue, int nextValue, FoodGatheringTask.CropKind cropKind) {
      if (cropKind.ripeAge() < 0) {
         return true;
      }

      String text = taskWorldView.blockProps(value, currentValue, nextValue).get("age");
      if (text == null) {
         return false;
      }

      try {
         return Integer.parseInt(text) >= cropKind.ripeAge();
      } catch (NumberFormatException numberFormatException) {
         return false;
      }
   }

   private TaskAction createTaskOperationHandler23(long currentSize, TaskOperationHandler taskOperation, TaskData taskData) {
      if (!this.enabled) {
         this.enabled = true;
         this.count4 = edible(taskData);
      }

      int value = edible(taskData) - this.count4;
      if (value > 0) {
         this.count3 += value;
      }

      this.count4 = edible(taskData);
      if (this.taskData2 != null) {
         if (this.taskData2.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 27.040000000000003) {
            this.taskData2 = null;
            this.text = "";
            this.taskData23 = null;
            this.count15 = 0;
         } else {
            String currentX = taskOperation.blocks().blockId(this.taskData2.x(), this.taskData2.y(), this.taskData2.z());
            if (currentX.equals(this.text)) {
               if (!this.taskData2.equals(this.taskData23)) {
                  this.taskData23 = this.taskData2;
                  this.count15 = 0;
               } else {
                  if (++this.count15 <= 200) {
                     this.text2 = "Harvesting " + createText(currentX);
                     return new TaskAction.Mine(this.taskData2, currentX);
                  }

                  this.values2.add(this.taskData2);
                  if (this.values2.size() > 32) {
                     this.values2.clear();
                  }

                  this.taskData2 = null;
                  this.text = "";
                  this.taskData23 = null;
                  this.count15 = 0;
                  this.text2 = "Skipping stuck crop";
               }
            } else {
               this.count5++;
               TaskBlockPosition taskBlockPosition = this.taskData2;
               this.taskData2 = null;
               String currentText = this.text;
               this.text = "";
               this.taskData23 = null;
               this.count15 = 0;
               FoodGatheringTask.CropKind cropKind = createCropKind(currentText);
               String nextText = cropKind == null ? "" : cropKind.seedId();
               if (!nextText.isEmpty()
                  && SOIL.contains(taskOperation.blocks().blockId(taskBlockPosition.x(), taskBlockPosition.y() - 1, taskBlockPosition.z()))
                  && taskOperation.blocks().blockId(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z()).equals("minecraft:air")
                  && taskData.count(nextText) > 0) {
                  this.text2 = "Replanting";
                  return new TaskAction.Place(nextText, taskBlockPosition, TaskAction.Face.UP);
               }
            }
         }
      }

      if (this.count5 >= this.config2.maxCrops()) {
         this.updateState(FoodGatheringTask.Stage.HUNT, currentSize, 0L);
         return this.createTaskOperationHandler25(currentSize, taskOperation, taskData);
      }

      HashSet<String> hashSet = new HashSet<>();

      for (FoodGatheringTask.CropKind currentCropKind : CROPS) {
         hashSet.addAll(currentCropKind.ids());
      }

      for (TaskNearestService.Find find : TaskNearestService.nearest(
         taskOperation.blocks(),
         taskData.player().x,
         taskData.player().y,
         taskData.player().z,
         hashSet,
         this.config2.scanRadius(),
         this.config2.yRadius(),
         8
      )) {
         FoodGatheringTask.CropKind nextCropKind = createCropKind(find.id());
         if (nextCropKind != null && checkCondition(taskOperation.blocks(), find.x(), find.y(), find.z(), nextCropKind)) {
            TaskBlockPosition currentTaskBlockPosition = find.pos();
            if (!this.values2.contains(currentTaskBlockPosition)) {
               if (currentTaskBlockPosition.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 17.64) {
                  return this.createTaskOperationHandler2(
                     taskOperation,
                     taskData,
                     currentTaskBlockPosition,
                     2.5,
                     "Getting to crops (" + this.count3 + "/" + this.config2.wantItems() + ")"
                  );
               }

               this.taskData2 = currentTaskBlockPosition;
               this.text = find.id();
               this.taskData23 = null;
               this.count15 = 0;
               this.text2 = "Harvesting " + createText(find.id());
               return new TaskAction.Mine(currentTaskBlockPosition, find.id());
            }
         }
      }

      this.updateState(FoodGatheringTask.Stage.HUNT, currentSize, 0L);
      return this.createTaskOperationHandler25(currentSize, taskOperation, taskData);
   }

   private TaskAction createTaskOperationHandler24(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.taskConfigTracker3 == null) {
         HashSet hashSet = new HashSet();

         for (FoodGatheringTask.CropKind cropKind : CROPS) {
            hashSet.addAll(cropKind.ids());
         }

         hashSet.add("minecraft:water");
         hashSet.addAll(LOOTABLE);
         this.taskConfigTracker3 = new ExplorationTask(
            ExplorationTask.Config.standard(hashSet, this.config2.scanRadius(), this.config2.yRadius()), "food"
         );
      }

      TaskAction taskAction = this.taskConfigTracker3.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done) {
         this.taskConfigTracker3 = null;
         this.enabled2 = true;
         this.stage = FoodGatheringTask.Stage.HARVEST;
         this.enabled = false;
         return this.createTaskOperationHandler23(size, taskOperation, taskData);
      } else if (taskAction instanceof TaskAction.Abort) {
         this.enabled2 = true;
         return this.createTaskOperationHandler22(size, taskOperation, taskData);
      } else {
         this.text2 = "Exploring · " + this.taskConfigTracker3.status();
         return taskAction;
      }
   }

   private static TaskOperationHandler.SeenEntity createSeenEntity(TaskOperationHandler taskOperation, TaskData taskData) {
      TaskOperationHandler.SeenEntity seenEntity = null;
      double doubleValue = Double.POSITIVE_INFINITY;

      for (TaskOperationHandler.SeenEntity currentSeenEntity : taskOperation.entities()) {
         if (PREY.contains(currentSeenEntity.type())) {
            double currentDoubleValue = calculateValue(currentSeenEntity, taskData);
            if (currentDoubleValue < doubleValue) {
               doubleValue = currentDoubleValue;
               seenEntity = currentSeenEntity;
            }
         }
      }

      return seenEntity;
   }

   private TaskAction createTaskOperationHandler25(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (!this.enabled) {
         this.enabled = true;
         this.count4 = edible(taskData);
      }

      int value = edible(taskData) - this.count4;
      if (value > 0) {
         this.count3 += value;
      }

      this.count4 = edible(taskData);
      if (this.taskStateController != null) {
         TaskAction taskAction = this.taskStateController.tick(size, taskOperation);
         this.text2 = "Collecting hunted food";
         if (!(taskAction instanceof TaskAction.Done) && !(taskAction instanceof TaskAction.Abort)) {
            return taskAction;
         }

         this.taskStateController = null;
         this.taskData22 = null;
      }

      TaskOperationHandler.SeenEntity seenEntity = taskOperation.entities().stream().filter(item -> item.ref() == this.count9).findFirst().orElse(null);
      if (this.count9 >= 0 && seenEntity == null) {
         this.count6++;
         this.count9 = -1;
         this.taskStateController = new TaskStateController(
            SurvivalAllService.all().keySet(),
            new TaskBlockPosition((int)Math.floor(this.value5), (int)Math.floor(this.value6), (int)Math.floor(this.value7))
         );
         return this.createTaskOperationHandler25(size, taskOperation, taskData);
      }

      if (seenEntity == null) {
         if (this.count6 >= this.config2.maxKills()) {
            this.updateState(FoodGatheringTask.Stage.FISH, size, this.config2.fishMillis());
            return this.createTaskOperationHandler26(size, taskOperation, taskData);
         }

         seenEntity = taskOperation.entities()
            .stream()
            .filter(item -> PREY.contains(item.type()) && !this.values3.contains(item.ref()))
            .min(Comparator.comparingDouble(item -> calculateValue(item, taskData)))
            .orElse(null);
         if (seenEntity != null) {
            this.count9 = seenEntity.ref();
            this.timestamp4 = size;
            this.count10 = 0;
            this.taskData22 = null;
         }
      }

      if (seenEntity == null) {
         this.updateState(FoodGatheringTask.Stage.FISH, size, this.config2.fishMillis());
         return this.createTaskOperationHandler26(size, taskOperation, taskData);
      }

      this.value5 = seenEntity.x();
      this.value6 = seenEntity.y();
      this.value7 = seenEntity.z();
      if (size - this.timestamp4 > 20000L) {
         this.values3.add(this.count9);
         this.count9 = -1;
         return new TaskAction.Wait("Trying another food source");
      }

      if (calculateValue(seenEntity, taskData) > 9.0) {
         TaskBlockPosition currentX = new TaskBlockPosition((int)Math.floor(seenEntity.x()), (int)Math.floor(seenEntity.y()), (int)Math.floor(seenEntity.z()));
         if (this.taskData22 != null
            && this.taskData22.distSq(seenEntity.x(), seenEntity.y(), seenEntity.z()) < 2.25
            && this.travelStatusTracker.status() != TravelStatusTracker.Status.ARRIVED) {
            currentX = this.taskData22;
         }

         return this.createTaskOperationHandler2(
            taskOperation,
            taskData,
            currentX,
            0.5,
            "Hunting " + createText(seenEntity.type()) + " (" + this.count3 + "/" + this.config2.wantItems() + ")"
         );
      } else {
         this.text2 = "Hunting " + createText(seenEntity.type());
         return new TaskAction.Strike(seenEntity.ref());
      }
   }

   private static double calculateValue(TaskOperationHandler.SeenEntity seenEntity, TaskData taskData) {
      double currentX = seenEntity.x() - taskData.player().x;
      double currentY = seenEntity.y() - taskData.player().y;
      double doubleValue = seenEntity.z() - taskData.player().z;
      return currentX * currentX + currentY * currentY + doubleValue * doubleValue;
   }

   private TaskAction createTaskOperationHandler26(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (!this.enabled) {
         this.enabled = true;
         this.count4 = edible(taskData);
         this.timestamp2 = size;
      }

      int value = edible(taskData) - this.count4;
      if (value > 0) {
         this.count3 += value;
      }

      this.count4 = edible(taskData);
      if (size - this.timestamp2 >= this.config2.fishMillis()) {
         this.updateState(FoodGatheringTask.Stage.LOOT, size, 0L);
         return this.createTaskOperationHandler27(size, taskOperation, taskData);
      }

      if (taskData.count("minecraft:fishing_rod") == 0) {
         this.text2 = "No rod — skipping the lake";
         this.updateState(FoodGatheringTask.Stage.LOOT, size, 0L);
         return this.createTaskOperationHandler27(size, taskOperation, taskData);
      }

      List items = TaskNearestService.nearest(
         taskOperation.blocks(), taskData.player().x, taskData.player().y, taskData.player().z, Set.of("minecraft:water"), 10, 3, 4
      );
      if (items.isEmpty()) {
         this.updateState(FoodGatheringTask.Stage.LOOT, size, 0L);
         return this.createTaskOperationHandler27(size, taskOperation, taskData);
      }

      TaskBlockPosition taskBlockPosition = ((TaskNearestService.Find)items.getFirst()).pos();
      if (taskBlockPosition.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 17.64) {
         return this.createTaskOperationHandler2(taskOperation, taskData, taskBlockPosition, 2.5, "Getting to water");
      }

      TaskOperationHandler.SeenEntity seenEntity = createSeenEntity2(taskOperation, taskData);
      if (seenEntity == null) {
         if (this.count11 >= 0) {
            this.count11 = -1;
            this.count12 = this.count13 = 0;
            if (++this.count8 > 4) {
               this.updateState(FoodGatheringTask.Stage.LOOT, size, 0L);
               return this.createTaskOperationHandler27(size, taskOperation, taskData);
            }
         }

         this.text2 = "Casting";
         this.count11 = -2;
         return new TaskAction.Cast(taskBlockPosition);
      } else {
         this.count11 = seenEntity.ref();
         if (this.count12++ < 20) {
            this.text2 = "Waiting for a bite";
            return new TaskAction.Wait("Settling the bobber");
         }

         if (seenEntity.vy() < -0.12) {
            if (++this.count13 >= 2) {
               this.count13 = 0;
               this.count12 = 0;
               this.text2 = "Reeling in";
               return new TaskAction.Reel();
            }
         } else {
            this.count13 = 0;
         }

         this.text2 = "Waiting for a bite";
         return new TaskAction.Wait("Watching the bobber");
      }
   }

   private static TaskOperationHandler.SeenEntity createSeenEntity2(TaskOperationHandler taskOperation, TaskData taskData) {
      TaskOperationHandler.SeenEntity seenEntity = null;
      double doubleValue = 256.0;

      for (TaskOperationHandler.SeenEntity currentSeenEntity : taskOperation.entities()) {
         if ("minecraft:fishing_bobber".equals(currentSeenEntity.type())) {
            double currentDoubleValue = calculateValue(currentSeenEntity, taskData);
            if (currentDoubleValue < doubleValue) {
               doubleValue = currentDoubleValue;
               seenEntity = currentSeenEntity;
            }
         }
      }

      return seenEntity;
   }

   private TaskAction createTaskOperationHandler27(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (!this.enabled) {
         this.enabled = true;
         this.count4 = edible(taskData);
         this.timestamp3 = size;
         this.count14 = this.count4;
      }

      int value = edible(taskData);
      if (value != this.count14) {
         this.count14 = value;
         this.timestamp3 = size;
      }

      if (value > this.count4) {
         this.count3 = this.count3 + (value - this.count4);
      }

      this.count4 = value;
      if (this.count7 < this.config2.maxChests() && (this.taskData2 == null || size - this.timestamp3 <= 6000L)) {
         if (this.taskData2 == null) {
            for (TaskNearestService.Find find : TaskNearestService.nearest(
               taskOperation.blocks(),
               taskData.player().x,
               taskData.player().y,
               taskData.player().z,
               LOOTABLE,
               this.config2.scanRadius(),
               this.config2.yRadius(),
               6
            )) {
               if (!find.id().equals("minecraft:chest") || !checkCondition2(taskOperation, find)) {
                  this.taskData2 = find.pos();
                  this.text = find.id();
                  this.count7++;
                  break;
               }
            }

            if (this.taskData2 == null) {
               return this.createTaskOperationHandler22(size, taskOperation, taskData);
            }
         }

         if (this.taskData2.distSq(taskData.player().x, taskData.player().y, taskData.player().z) > 17.64) {
            return this.createTaskOperationHandler2(
               taskOperation,
               taskData,
               this.taskData2,
               2.5,
               "Getting to loot (" + this.count3 + "/" + this.config2.wantItems() + ")"
            );
         }

         this.text2 = "Looting food";
         return new TaskAction.Loot(this.taskData2, this.text);
      } else {
         this.taskData2 = null;
         return this.createTaskOperationHandler22(size, taskOperation, taskData);
      }
   }

   private static boolean checkCondition2(TaskOperationHandler taskOperation, TaskNearestService.Find find) {
      int[][] ints = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int[] currentInts : ints) {
         if (taskOperation.blocks().blockId(find.x() + currentInts[0], find.y(), find.z() + currentInts[1]).equals(find.id())) {
            return true;
         }
      }

      return false;
   }

   private TaskAction createTaskOperationHandler28(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text2 = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text2);
      return taskAction;
   }

   private static String createText(String text) {
      return text.startsWith("minecraft:") ? text.substring("minecraft:".length()) : text;
   }

   public record Config(int wantItems, int scanRadius, int yRadius, int maxCrops, int maxKills, long fishMillis, int maxChests) {
      public Config(int wantItems, int scanRadius, int yRadius, int maxCrops, int maxKills, long fishMillis, int maxChests) {
         if (wantItems >= 1
            && wantItems <= 64
            && scanRadius >= 4
            && scanRadius <= 32
            && yRadius >= 0
            && yRadius <= 16
            && maxCrops >= 0
            && maxKills >= 0
            && fishMillis >= 0L
            && maxChests >= 0) {
            this.wantItems = wantItems;
            this.scanRadius = scanRadius;
            this.yRadius = yRadius;
            this.maxCrops = maxCrops;
            this.maxKills = maxKills;
            this.fishMillis = fishMillis;
            this.maxChests = maxChests;
         } else {
            throw new IllegalArgumentException("Invalid food config");
         }
      }

      public static FoodGatheringTask.Config standard(int value) {
         return new FoodGatheringTask.Config(value, 20, 8, 8, 6, 60000L, 4);
      }
   }

   public record CropKind(Set<String> ids, int ripeAge, String seedId) {
   }

   private enum Stage {
      HARVEST,
      HUNT,
      FISH,
      LOOT,
      EXPLORE;


      private static FoodGatheringTask.Stage[] $values() {
         return new FoodGatheringTask.Stage[]{HARVEST, HUNT, FISH, LOOT, EXPLORE};
      }
   }
}

