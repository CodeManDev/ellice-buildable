package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.joml.Vector3d;

public final class MissionTask implements TaskStep {
   private final String text;
   private final Function<TaskData, List<TaskStep>> items;
   private final Deque<TaskStep> items2 = new ArrayDeque<>();
   private TaskStep taskOperationHandler3;
   private Vector3d vector3d;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private boolean enabled;
   private boolean enabled2;
   private String text2 = "";
   private TaskAction taskOperationHandler2;
   private String text3 = "Starting";

   public MissionTask(String currentText, Function<TaskData, List<TaskStep>> function) {
      this.text = Objects.requireNonNull(currentText, "mission");
      this.items = Objects.requireNonNull(function, "plan");
   }

   public static MissionTask stoneAge() {
      return new MissionTask(
         "Stone age",
         taskData -> {
            boolean enabled = Stream.of("stone", "iron", "diamond", "netherite")
               .anyMatch(material -> taskData.count("minecraft:" + material + "_pickaxe") > 0);
            return enabled ? List.of() : List.of(new CraftingTask(new CraftingTask.Config("minecraft:stone_pickaxe", 1)));
         }
      );
   }

   @Override
   public String name() {
      return this.text;
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
      if (taskData.dead()) {
         return this.createTaskOperationHandler23(new TaskAction.Abort("Died"));
      }

      if (this.vector3d == null) {
         this.vector3d = new Vector3d(taskData.player());
      }

      if (!this.enabled) {
         this.enabled = true;
         this.items2.addAll(this.items.apply(taskData));
      }

      if (this.enabled2) {
         return this.createTaskOperationHandler22(taskOperation, taskData);
      }

      if (taskData.threatened()) {
         return this.createTaskOperationHandler2(taskOperation, taskData, "Players nearby — standing down");
      }

      if (this.taskOperationHandler3 == null) {
         this.taskOperationHandler3 = this.items2.poll();
         if (this.taskOperationHandler3 == null) {
            return this.createTaskOperationHandler23(new TaskAction.Done("Mission complete: " + this.text));
         }

         this.text3 = this.text + " · " + this.taskOperationHandler3.status();
         return new TaskAction.Wait("Next: " + this.taskOperationHandler3.name());
      } else {
         TaskAction taskAction = this.taskOperationHandler3.tick(longValue, taskOperation);
         if (taskAction instanceof TaskAction.Done) {
            this.taskOperationHandler3 = null;
            this.text3 = this.text + " · step done";
            return new TaskAction.Wait("Next step");
         } else if (taskAction instanceof TaskAction.Abort abort) {
            this.taskOperationHandler3 = null;
            return this.createTaskOperationHandler2(taskOperation, taskData, abort.reason());
         } else {
            this.text3 = this.text + " · " + this.taskOperationHandler3.status();
            return taskAction;
         }
      }
   }

   private TaskAction createTaskOperationHandler2(TaskOperationHandler taskOperation, TaskData taskData, String currentText) {
      this.text2 = currentText;
      this.enabled2 = true;
      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
      }

      TravelIsInGoalHandler.NearGoal nearGoal = new TravelIsInGoalHandler.NearGoal(
         (int)Math.floor(this.vector3d.x),
         this.vector3d.y,
         (int)Math.floor(this.vector3d.z),
         2.0
      );
      this.travelStatusTracker.start(nearGoal, TravelPolicy.legit(taskData.canSprint()), taskData.player());
      this.text3 = this.text + " · returning to anchor";
      return TaskAction.go(nearGoal, this.text3, this.travelStatusTracker.route(), false, TravelPolicy.legit(taskData.canSprint()));
   }

   private TaskAction createTaskOperationHandler22(TaskOperationHandler taskOperation, TaskData taskData) {
      this.travelStatusTracker.tick(taskData.player());
      if (this.travelStatusTracker.status() == TravelStatusTracker.Status.ARRIVED) {
         return this.createTaskOperationHandler23(new TaskAction.Abort("Mission aborted (" + this.text2 + ") — back at anchor"));
      } else if (this.travelStatusTracker.status() != TravelStatusTracker.Status.UNAVAILABLE
         && this.travelStatusTracker.status() != TravelStatusTracker.Status.UNEXPLORED) {
         TravelIsInGoalHandler.NearGoal nearGoal = new TravelIsInGoalHandler.NearGoal(
            (int)Math.floor(this.vector3d.x),
            this.vector3d.y,
            (int)Math.floor(this.vector3d.z),
            2.0
         );
         this.text3 = this.text + " · returning to anchor";
         return TaskAction.go(nearGoal, this.text3, this.travelStatusTracker.route(), false, TravelPolicy.legit(taskData.canSprint()));
      } else {
         return this.createTaskOperationHandler23(new TaskAction.Abort("Mission aborted (" + this.text2 + ") — anchor unreachable"));
      }
   }

   private TaskAction createTaskOperationHandler23(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text3 = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text3);
      return taskAction;
   }
}

