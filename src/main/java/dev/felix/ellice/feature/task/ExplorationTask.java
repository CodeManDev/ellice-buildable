package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ExplorationTask implements TaskStep {
   private final ExplorationTask.Config config2;
   private final String text;
   private TravelStatusTracker travelStatusTracker;
   private TravelLoadedHandler travelLoadedHandler;
   private ExplorationTask.Vector3dAnchor vector3dAnchor;
   private final List<int[]> items = new ArrayList<>();
   private int count = -1;
   private long timestamp;
   private long timestamp2;
   private TaskAction taskOperationHandler2;
   private String text2 = "Starting";

   public ExplorationTask(ExplorationTask.Config currentConfig, String currentText) {
      this.config2 = Objects.requireNonNull(currentConfig, "config");
      this.text = currentText == null ? "ground" : currentText;
   }

   @Override
   public String name() {
      return "Explore for " + this.text;
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
         return this.createTaskOperationHandler2(abort);
      }

      if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
         this.travelLoadedHandler = taskOperation.traversal();
         this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
         this.vector3dAnchor = null;
         this.count = -1;
      }

      if (this.vector3dAnchor == null) {
         this.vector3dAnchor = new ExplorationTask.Vector3dAnchor(taskData.player().x, taskData.player().y, taskData.player().z);
         this.updateState();
      }

      int currentCount = this.count >= 0 && this.travelStatusTracker.status() != TravelStatusTracker.Status.ARRIVED && longValue < this.timestamp ? 0 : 1;
      if (currentCount != 0 || longValue >= this.timestamp2) {
         this.timestamp2 = longValue + 2000L;
         List currentItems = TaskNearestService.nearest(
            taskOperation.blocks(),
            taskData.player().x,
            taskData.player().y,
            taskData.player().z,
            this.config2.ids(),
            this.config2.scanRadius(),
            this.config2.yRadius(),
            this.config2.maxResults()
         );
         if (!currentItems.isEmpty()) {
            return this.createTaskOperationHandler2(new TaskAction.Done("Found " + currentItems.size() + " " + this.text + " — rescanning"));
         }
      }

      if (currentCount != 0) {
         this.count++;
         if (this.count >= this.items.size()) {
            return this.createTaskOperationHandler2(
               new TaskAction.Abort("Explored " + this.items.size() + " overlooks — no " + this.text)
            );
         }

         int[] ints = this.items.get(this.count);
         TravelIsInGoalHandler.XZGoal xZGoal = new TravelIsInGoalHandler.XZGoal(ints[0], ints[1]);
         this.travelStatusTracker.start(xZGoal, TravelPolicy.legit(taskData.canSprint()), taskData.player());
         this.timestamp = longValue + this.config2.pointMillis();
      }

      this.travelStatusTracker.tick(taskData.player());
      int[] currentInts = this.items.get(this.count);
      this.text2 = "Exploring for " + this.text + " (" + (this.count + 1) + "/" + this.items.size() + ")";
      TravelIsInGoalHandler.XZGoal currentXZGoal = new TravelIsInGoalHandler.XZGoal(currentInts[0], currentInts[1]);
      return TaskAction.go(
         currentXZGoal,
         this.text2,
         this.travelStatusTracker.route(),
         this.travelStatusTracker.status() == TravelStatusTracker.Status.ARRIVED,
         TravelPolicy.legit(taskData.canSprint())
      );
   }

   private void updateState() {
      this.items.clear();

      for (int index = 1; index <= this.config2.rounds(); index++) {
         int value = index * this.config2.scanRadius();
         int currentValue = index % 2 == 0 ? 45 : 0;

         for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
            double doubleValue = Math.toRadians(currentValue + currentIndex * 90);
            this.items
               .add(
                  new int[]{
                     (int)Math.floor(this.vector3dAnchor.x() + Math.cos(doubleValue) * value),
                     (int)Math.floor(this.vector3dAnchor.z() + Math.sin(doubleValue) * value)
                  }
               );
         }
      }
   }

   private TaskAction createTaskOperationHandler2(TaskAction taskAction) {
      this.taskOperationHandler2 = taskAction;
      this.text2 = taskAction instanceof TaskAction.Done done
         ? done.summary()
         : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text2);
      return taskAction;
   }

   public record Config(Set<String> ids, int scanRadius, int yRadius, int maxResults, int rounds, long pointMillis) {
      public Config(Set<String> ids, int scanRadius, int yRadius, int maxResults, int rounds, long pointMillis) {
         ids = Set.copyOf(Objects.requireNonNull(ids, "ids"));
         if (!ids.isEmpty()
            && scanRadius >= 4
            && scanRadius <= 32
            && yRadius >= 0
            && yRadius <= 16
            && maxResults >= 1
            && rounds >= 1
            && rounds <= 5
            && pointMillis >= 1000L) {
            this.ids = ids;
            this.scanRadius = scanRadius;
            this.yRadius = yRadius;
            this.maxResults = maxResults;
            this.rounds = rounds;
            this.pointMillis = pointMillis;
         } else {
            throw new IllegalArgumentException("Invalid explore config");
         }
      }

      public static ExplorationTask.Config standard(Set<String> values, int value, int currentValue) {
         return new ExplorationTask.Config(values, value, currentValue, 6, 3, 8000L);
      }
   }

   private record Vector3dAnchor(double x, double y, double z) {
   }
}
