package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ShelterTask implements TaskStep {
  private static final double value = 4.2;
  private final ShelterTask.Config config2;
  private TravelStatusTracker travelStatusTracker;
  private TravelLoadedHandler travelLoadedHandler;
  private ShelterTask.Stage stage = ShelterTask.Stage.DIG;
  private TaskConfigTracker taskConfigTracker;
  private ShelterTask.Vector3dAnchor vector3dAnchor;
  private TaskBlockPosition taskData2;
  private final List<TaskBlockPosition> items = new ArrayList<>();
  private int count2;
  private String text = "";
  private TaskBlockPosition taskData22;
  private TaskAction taskOperationHandler2;
  private String text2 = "Starting";

  public ShelterTask(ShelterTask.Config currentConfig) {
    this.config2 = Objects.requireNonNull(currentConfig, "config");
  }

  @Override
  public String name() {
    return "Shelter";
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
      return this.createTaskOperationHandler26(abort);
    }

    if (this.travelLoadedHandler != taskOperation.traversal() || this.travelStatusTracker == null) {
      this.travelLoadedHandler = taskOperation.traversal();
      this.travelStatusTracker = new TravelStatusTracker(this.travelLoadedHandler);
      this.taskData2 = null;
    }

    if (TaskDayNumberService.isNight(taskOperation.timeOfDay())
        || this.stage != ShelterTask.Stage.DIG && this.stage != ShelterTask.Stage.BUILD) {
      if (!taskOperation.skyAbove()) {
        return this.createTaskOperationHandler26(new TaskAction.Done("Already roofed"));
      }

      return switch (this.stage) {
        case DIG -> this.createTaskOperationHandler2(longValue, taskOperation, taskData);
        case BUILD -> this.createTaskOperationHandler23(longValue, taskOperation, taskData);
        case WAIT_NIGHT -> this.createTaskOperationHandler24(taskOperation, taskData);
        case EXIT -> this.createTaskOperationHandler25(taskOperation, taskData);
      };
    } else {
      return this.createTaskOperationHandler26(new TaskAction.Done("Daylight — no shelter needed"));
    }
  }

  private String createText(TaskData taskData) {
    String text = "";
    int value = 0;

    for (String currentText : this.config2.placeIds()) {
      int currentCount = taskData.count(currentText);
      if (currentCount > value) {
        value = currentCount;
        text = currentText;
      }
    }

    return text;
  }

  private TaskAction createTaskOperationHandler2(
      long size, TaskOperationHandler taskOperation, TaskData taskData) {
    int value = 0;

    for (String text : this.config2.placeIds()) {
      value += taskData.count(text);
    }

    if (value >= calculateValue()) {
      this.stage = ShelterTask.Stage.BUILD;
      this.vector3dAnchor = null;
      return this.createTaskOperationHandler23(size, taskOperation, taskData);
    }

    if (this.taskConfigTracker == null) {
      this.taskConfigTracker =
          new TaskConfigTracker(
              new TaskConfigTracker.Config(
                  this.config2.dirtIds(),
                  this.config2.placeIds(),
                  Set.of(),
                  "",
                  calculateValue() - value,
                  this.config2.scanRadius(),
                  this.config2.yRadius()));
    }

    TaskAction taskAction = this.taskConfigTracker.tick(size, taskOperation);
    if (!(taskAction instanceof TaskAction.Done)) {
      if (taskAction instanceof TaskAction.Abort abort) {
        return this.createTaskOperationHandler26(
            new TaskAction.Abort("No dirt for shelter: " + abort.reason()));
      } else {
        this.text2 = "Stocking dirt · " + this.taskConfigTracker.status();
        return taskAction;
      }
    } else {
      this.taskConfigTracker = null;
      int currentValue = 0;

      for (String currentText : this.config2.placeIds()) {
        currentValue += taskData.count(currentText);
      }

      if (currentValue >= calculateValue()) {
        this.stage = ShelterTask.Stage.BUILD;
        this.vector3dAnchor = null;
        this.text2 = "Dirt stocked";
        return this.createTaskOperationHandler23(size, taskOperation, taskData);
      } else {
        return this.createTaskOperationHandler26(
            new TaskAction.Abort(
                "Only " + currentValue + " dirt within reach — need " + calculateValue()));
      }
    }
  }

  private static int calculateValue() {
    return 17;
  }

  private void updateState(ShelterTask.Vector3dAnchor vector3dAnchor) {
    this.items.clear();
    int[][] ints = new int[][] {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};

    for (int[] currentInts : ints) {
      this.items.add(
          new TaskBlockPosition(
              vector3dAnchor.x() + currentInts[0],
              vector3dAnchor.y(),
              vector3dAnchor.z() + currentInts[2]));
      this.items.add(
          new TaskBlockPosition(
              vector3dAnchor.x() + currentInts[0],
              vector3dAnchor.y() + 1,
              vector3dAnchor.z() + currentInts[2]));
    }

    for (int index = -1; index <= 1; index++) {
      for (int currentIndex = -1; currentIndex <= 1; currentIndex++) {
        this.items.add(
            new TaskBlockPosition(
                vector3dAnchor.x() + index,
                vector3dAnchor.y() + 2,
                vector3dAnchor.z() + currentIndex));
      }
    }
  }

  private TaskAction createTaskOperationHandler22(
      TaskOperationHandler taskOperation,
      TaskData taskData,
      TaskBlockPosition taskBlockPosition,
      double doubleValue,
      String text) {
    if (!taskBlockPosition.equals(this.taskData2)) {
      this.taskData2 = taskBlockPosition;
      TravelIsInGoalHandler.NearGoal currentX =
          new TravelIsInGoalHandler.NearGoal(
              taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z(), doubleValue);
      this.travelStatusTracker.start(currentX, this.createTravelData2(taskData), taskData.player());
    }

    this.travelStatusTracker.tick(taskData.player());
    this.text2 = text;
    TravelIsInGoalHandler.NearGoal nextX =
        new TravelIsInGoalHandler.NearGoal(
            taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z(), doubleValue);
    return TaskAction.go(
        nextX,
        this.text2,
        this.travelStatusTracker.route(),
        false,
        this.createTravelData2(taskData));
  }

  private TaskAction createTaskOperationHandler23(
      long currentSize, TaskOperationHandler taskOperation, TaskData taskData) {
    if (this.vector3dAnchor == null) {
      this.vector3dAnchor =
          new ShelterTask.Vector3dAnchor(
              (int) Math.floor(taskData.player().x),
              (int) Math.floor(taskData.player().y),
              (int) Math.floor(taskData.player().z));
      this.updateState(this.vector3dAnchor);
      this.count2 = 0;
      this.taskData2 = null;
    }

    this.text = this.createText(taskData);
    if (this.text.isEmpty()) {
      this.stage = ShelterTask.Stage.DIG;
      return this.createTaskOperationHandler2(currentSize, taskOperation, taskData);
    }

    while (this.count2 < this.items.size()) {
      TaskBlockPosition taskBlockPosition = this.items.get(this.count2);
      if (taskOperation
          .blocks()
          .blockId(taskBlockPosition.x(), taskBlockPosition.y(), taskBlockPosition.z())
          .equals("minecraft:air")) {
        if (taskBlockPosition.distSq(taskData.player().x, taskData.player().y, taskData.player().z)
            > 17.64) {
          return this.createTaskOperationHandler22(
              taskOperation, taskData, taskBlockPosition, 2.0, "Getting to build spot");
        }

        this.text2 = "Building shelter (" + this.count2 + "/" + this.items.size() + ")";
        return new TaskAction.Place(this.text, taskBlockPosition, TaskAction.Face.UP);
      }

      this.count2++;
    }

    this.taskData22 = null;

    for (TaskBlockPosition currentTaskBlockPosition : this.items) {
      if (currentTaskBlockPosition.y() == this.vector3dAnchor.y()
          && taskOperation
              .blocks()
              .blockId(
                  currentTaskBlockPosition.x(),
                  currentTaskBlockPosition.y(),
                  currentTaskBlockPosition.z())
              .equals(this.text)) {
        this.taskData22 = currentTaskBlockPosition;
        break;
      }
    }

    this.stage = ShelterTask.Stage.WAIT_NIGHT;
    this.text2 = "Waiting for dawn";
    return new TaskAction.Wait("Sheltered");
  }

  private TaskAction createTaskOperationHandler24(
      TaskOperationHandler taskOperation, TaskData taskData) {
    if (!TaskDayNumberService.isNight(taskOperation.timeOfDay())) {
      this.stage = ShelterTask.Stage.EXIT;
      return this.createTaskOperationHandler25(taskOperation, taskData);
    } else {
      this.text2 = "Waiting for dawn";
      return new TaskAction.Wait("Sheltered");
    }
  }

  private TaskAction createTaskOperationHandler25(
      TaskOperationHandler taskOperation, TaskData taskData) {
    if (this.taskData22 == null
        || !taskOperation
            .blocks()
            .blockId(this.taskData22.x(), this.taskData22.y(), this.taskData22.z())
            .equals(this.text)) {
      return this.createTaskOperationHandler26(new TaskAction.Done("Survived the night"));
    }

    if (this.taskData22.distSq(taskData.player().x, taskData.player().y, taskData.player().z)
        > 17.64) {
      return this.createTaskOperationHandler22(
          taskOperation, taskData, this.taskData22, 2.0, "Getting to the exit");
    }

    this.text2 = "Breaking out";
    return new TaskAction.Mine(this.taskData22, this.text);
  }

  private TravelPolicy createTravelData2(TaskData taskData) {
    return TravelPolicy.legit(taskData.canSprint());
  }

  private TaskAction createTaskOperationHandler26(TaskAction taskAction) {
    this.taskOperationHandler2 = taskAction;
    this.text2 =
        taskAction instanceof TaskAction.Done done
            ? done.summary()
            : (taskAction instanceof TaskAction.Abort abort ? abort.reason() : this.text2);
    return taskAction;
  }

  public record Config(Set<String> dirtIds, Set<String> placeIds, int scanRadius, int yRadius) {
    public Config(Set<String> dirtIds, Set<String> placeIds, int scanRadius, int yRadius) {
      dirtIds = Set.copyOf(Objects.requireNonNull(dirtIds, "dirtIds"));
      placeIds = Set.copyOf(Objects.requireNonNull(placeIds, "placeIds"));
      if (!dirtIds.isEmpty()
          && !placeIds.isEmpty()
          && scanRadius >= 4
          && scanRadius <= 32
          && yRadius >= 0
          && yRadius <= 16) {
        this.dirtIds = dirtIds;
        this.placeIds = placeIds;
        this.scanRadius = scanRadius;
        this.yRadius = yRadius;
      } else {
        throw new IllegalArgumentException("Invalid shelter config");
      }
    }

    public static ShelterTask.Config standard() {
      return new ShelterTask.Config(
          Set.of(
              "minecraft:dirt",
              "minecraft:coarse_dirt",
              "minecraft:grass_block",
              "minecraft:rooted_dirt"),
          Set.of("minecraft:dirt", "minecraft:coarse_dirt"),
          12,
          4);
    }
  }

  private enum Stage {
    DIG,
    BUILD,
    WAIT_NIGHT,
    EXIT;

    private static ShelterTask.Stage[] $values() {
      return new ShelterTask.Stage[] {DIG, BUILD, WAIT_NIGHT, EXIT};
    }
  }

  private record Vector3dAnchor(int x, int y, int z) {}
}
