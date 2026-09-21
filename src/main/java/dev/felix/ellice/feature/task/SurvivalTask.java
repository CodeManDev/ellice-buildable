package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.survival.SurvivalAssessService;
import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.Objects;

public final class SurvivalTask implements TaskStep {
   private final SurvivalTask.Config config2;
   private final TaskStep taskOperationHandler3;
   private FoodGatheringTask taskConfigTracker7;
   private ShelterTask taskConfigTracker2;
   private SurvivalTask.Mode mode = SurvivalTask.Mode.MISSION;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private boolean enabled;
   private long timestamp = Long.MIN_VALUE;
   private long timestamp2;
   private TaskAction taskOperationHandler2;
   private String text = "Starting";

   public SurvivalTask(SurvivalTask.Config currentConfig, TaskStep taskStep) {
      this.config2 = Objects.requireNonNull(currentConfig, "config");
      this.taskOperationHandler3 = Objects.requireNonNull(taskStep, "mission");
   }

   @Override
   public String name() {
      return "Survive";
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
      if (taskData.dead()) {
         return this.createTaskOperationHandler26(new TaskAction.Abort("Died"));
      }

      SurvivalAssessService.Advice advice = SurvivalAssessService.assess(taskData.vitals(), this.config2.vitals());
      switch (advice.threat()) {
         case DROWNING:
            return this.createTaskOperationHandler22(taskOperation, taskData);
         case BURNING:
            return this.createTaskOperationHandler2("Burning — riding it out");
         case FALLING:
            return this.createTaskOperationHandler2("Falling — hands off");
         case CRITICAL_HEALTH:
            return this.createTaskOperationHandler2("Critical — eating to regen");
         default:
            this.enabled = false;
            if (FoodGatheringTask.edible(taskData) < this.config2.foodWant() && longValue >= this.timestamp2) {
               return this.createTaskOperationHandler23(longValue, taskOperation, taskData);
            } else {
               long currentLongValue = TaskDayNumberService.dayNumber(taskOperation.timeOfDay());
               return TaskDayNumberService.isNight(taskOperation.timeOfDay()) && taskOperation.skyAbove() && currentLongValue != this.timestamp
                  ? this.createTaskOperationHandler24(longValue, taskOperation, taskData, currentLongValue)
                  : this.createTaskOperationHandler25(longValue, taskOperation);
            }
      }
   }

   private TaskAction createTaskOperationHandler2(String currentText) {
      this.text = "Survive · " + currentText;
      return new TaskAction.Wait(currentText);
   }

   private TaskAction createTaskOperationHandler22(TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
         this.enabled = false;
      }

      int value = (int)Math.floor(taskData.player().x);
      int currentValue = (int)Math.floor(taskData.player().z);
      if (!this.enabled || this.travelStatusTracker.status() == TravelStatusTracker.Status.ARRIVED) {
         this.enabled = true;
         TravelIsInGoalHandler.NearGoal nearGoal = new TravelIsInGoalHandler.NearGoal(
            value, taskData.player().y + 5.0, currentValue, 1.2
         );
         this.travelStatusTracker.start(nearGoal, TravelPolicy.legit(true), taskData.player());
      }

      this.travelStatusTracker.tick(taskData.player());
      this.text = "Survive · surfacing";
      TravelIsInGoalHandler.NearGoal currentNearGoal = new TravelIsInGoalHandler.NearGoal(
         value, taskData.player().y + 5.0, currentValue, 1.2
      );
      return TaskAction.go(currentNearGoal, this.text, this.travelStatusTracker.route(), false, TravelPolicy.legit(true));
   }

   private TaskAction createTaskOperationHandler23(long size, TaskOperationHandler taskOperation, TaskData taskData) {
      if (this.mode != SurvivalTask.Mode.FOOD || this.taskConfigTracker7 == null) {
         this.mode = SurvivalTask.Mode.FOOD;
         this.taskConfigTracker7 = new FoodGatheringTask(FoodGatheringTask.Config.standard(this.config2.foodWant()));
      }

      TaskAction taskAction = this.taskConfigTracker7.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done) {
         this.taskConfigTracker7 = null;
         this.mode = SurvivalTask.Mode.MISSION;
         this.text = "Survive · fed";
         return this.createTaskOperationHandler25(size, taskOperation);
      } else if (taskAction instanceof TaskAction.Abort abort) {
         this.taskConfigTracker7 = null;
         this.mode = SurvivalTask.Mode.MISSION;
         this.timestamp2 = size + 10000L;
         this.text = "Survive · still hungry (" + abort.reason() + ")";
         return this.createTaskOperationHandler25(size, taskOperation);
      } else {
         this.text = "Survive · hungry · " + this.taskConfigTracker7.status();
         return taskAction;
      }
   }

   private TaskAction createTaskOperationHandler24(long size, TaskOperationHandler taskOperation, TaskData taskData, long currentSize) {
      if (this.mode != SurvivalTask.Mode.SHELTER || this.taskConfigTracker2 == null) {
         this.mode = SurvivalTask.Mode.SHELTER;
         this.taskConfigTracker2 = new ShelterTask(ShelterTask.Config.standard());
      }

      TaskAction taskAction = this.taskConfigTracker2.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done) {
         this.taskConfigTracker2 = null;
         this.mode = SurvivalTask.Mode.MISSION;
         this.timestamp = currentSize;
         this.text = "Survive · sheltered";
         return this.createTaskOperationHandler25(size, taskOperation);
      } else if (taskAction instanceof TaskAction.Abort abort) {
         this.taskConfigTracker2 = null;
         this.mode = SurvivalTask.Mode.MISSION;
         this.timestamp = currentSize;
         this.text = "Survive · exposed (" + abort.reason() + ")";
         return this.createTaskOperationHandler25(size, taskOperation);
      } else {
         this.text = "Survive · night · " + this.taskConfigTracker2.status();
         return taskAction;
      }
   }

   private TaskAction createTaskOperationHandler25(long size, TaskOperationHandler taskOperation) {
      this.mode = SurvivalTask.Mode.MISSION;
      TaskAction taskAction = this.taskOperationHandler3.tick(size, taskOperation);
      if (taskAction instanceof TaskAction.Done done) {
         return this.createTaskOperationHandler26(done);
      } else if (taskAction instanceof TaskAction.Abort abort) {
         return this.createTaskOperationHandler26(abort);
      } else {
         this.text = "Survive · " + this.taskOperationHandler3.status();
         return taskAction;
      }
   }

   private TaskAction createTaskOperationHandler26(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text);
      return taskAction;
   }

   public record Config(SurvivalAssessService.Policy vitals, int foodWant) {
      public Config(SurvivalAssessService.Policy vitals, int foodWant) {
         Objects.requireNonNull(vitals, "vitals");
         if (foodWant >= 1 && foodWant <= 64) {
            this.vitals = vitals;
            this.foodWant = foodWant;
         } else {
            throw new IllegalArgumentException("Invalid food want");
         }
      }

      public static SurvivalTask.Config standard() {
         return new SurvivalTask.Config(SurvivalAssessService.Policy.defaults(), 10);
      }
   }

   private enum Mode {
      MISSION,
      FOOD,
      SHELTER;


      private static SurvivalTask.Mode[] $values() {
         return new SurvivalTask.Mode[]{MISSION, FOOD, SHELTER};
      }
   }
}

