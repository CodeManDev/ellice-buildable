package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import dev.felix.ellice.feature.travel.TravelPolicy;
import dev.felix.ellice.feature.travel.TravelStatusTracker;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class CraftingTask implements TaskStep {
  private static final String text = "minecraft:crafting_table";
  private static final String text2 = "minecraft:wooden_pickaxe";
  private static final Set<String> text3 =
      Set.of("minecraft:cobblestone", "minecraft:cobbled_deepslate");
  private static final Set<String> text4 =
      Set.of(
          "minecraft:wooden_pickaxe",
          "minecraft:stone_pickaxe",
          "minecraft:iron_pickaxe",
          "minecraft:diamond_pickaxe",
          "minecraft:netherite_pickaxe");
  private final CraftingTask.Config config2;
  private final List<TaskResolveService.Recipe> items;
  private TreeHarvestTask taskConfigTracker6;
  private TaskConfigTracker taskConfigTracker;
  private String text5 = "";
  private int count2;
  private long timestamp;
  private long timestamp2 = -1L;
  private TaskBlockPosition taskData2;
  private TravelStatusTracker travelStatusTracker;
  private TaskBlockPosition taskData22;
  private TaskAction taskOperationHandler2;
  private String text6 = "Starting";

  public CraftingTask(CraftingTask.Config config) {
    this(config, TaskResolveService.standard());
  }

  CraftingTask(CraftingTask.Config config, List<TaskResolveService.Recipe> currentItems) {
    this.config2 = Objects.requireNonNull(config);
    this.items = List.copyOf(currentItems);
  }

  @Override
  public String name() {
    return "Craft " + createText(this.config2.targetId());
  }

  @Override
  public String status() {
    return this.text6;
  }

  Map<String, Integer> shortages(TaskData taskData, boolean enabled) {
    LinkedHashMap linkedHashMap = new LinkedHashMap();
    Map entries =
        TaskResolveService.deficits(
            this.items, Map.of(this.config2.targetId(), this.config2.want()), taskData.inventory());
    int currentCount =
        calculateValue2(entries) > 0 && text4.stream().noneMatch(item -> taskData.count(item) > 0)
            ? 1
            : 0;
    int value =
        TaskResolveService.stationFor(this.items, this.config2.targetId())
                    != TaskResolveService.Station.TABLE
                && currentCount == 0
            ? 0
            : 1;
    if (value != 0 && !enabled) {
      linkedHashMap.put("minecraft:crafting_table", 1);
    }

    if (currentCount != 0) {
      linkedHashMap.put("minecraft:wooden_pickaxe", 1);
    }

    linkedHashMap.put(this.config2.targetId(), this.config2.want());
    return TaskResolveService.deficits(this.items, linkedHashMap, taskData.inventory());
  }

  private int calculateValue(TaskData taskData, boolean enabled) {
    Map<String, Integer> entries = this.shortages(taskData, enabled);
    return TreeHarvestTask.trunkIds().stream()
        .mapToInt(item -> entries.getOrDefault(item, 0))
        .sum();
  }

  private static int calculateValue2(Map<String, Integer> entries) {
    return text3.stream().mapToInt(item -> entries.getOrDefault(item, 0)).sum();
  }

  private boolean checkCondition(TaskOperationHandler taskOperation, TaskData taskData) {
    return this.createFind(taskOperation, taskData) != null;
  }

  private TaskNearestService.Find createFind(
      TaskOperationHandler taskOperation, TaskData taskData) {
    return TaskNearestService.nearest(
            taskOperation.blocks(),
            taskData.player().x,
            taskData.player().y,
            taskData.player().z,
            Set.of("minecraft:crafting_table"),
            24,
            8,
            1)
        .stream()
        .findFirst()
        .orElse(null);
  }

  @Override
  public TaskAction tick(long longValue, TaskOperationHandler taskOperation) {
    if (this.taskOperationHandler2 != null) {
      return this.taskOperationHandler2;
    }

    TaskData taskData = taskOperation.sense();
    TaskAction.Abort abort = DiedComponent.critical(taskData);
    if (abort != null) {
      return this.createTaskOperationHandler23(abort);
    }

    if (taskData.count(this.config2.targetId()) >= this.config2.want()) {
      return this.createTaskOperationHandler23(
          new TaskAction.Done(
              "Crafted "
                  + taskData.count(this.config2.targetId())
                  + "× "
                  + createText(this.config2.targetId())));
    }

    boolean enabled = this.checkCondition(taskOperation, taskData);
    Map entries = this.shortages(taskData, enabled);
    int value = this.calculateValue(taskData, enabled);
    if (value <= 0 && this.taskConfigTracker6 == null) {
      int currentCount =
          calculateValue2(entries) > 0 && text4.stream().noneMatch(item -> taskData.count(item) > 0)
              ? 1
              : 0;
      int currentValue =
          TaskResolveService.stationFor(this.items, this.config2.targetId())
                      != TaskResolveService.Station.TABLE
                  && currentCount == 0
              ? 0
              : 1;
      if (currentValue == 0 || enabled) {
        this.taskData2 = null;
        this.timestamp2 = -1L;
        if (currentCount != 0) {
          return this.createTaskOperationHandler2(
              longValue, taskOperation, taskData, "minecraft:wooden_pickaxe", 1);
        }

        int nextValue = calculateValue2(entries);
        if (nextValue > 0) {
          if (this.taskConfigTracker == null) {
            this.taskConfigTracker =
                new TaskConfigTracker(
                    TaskConfigTracker.Config.cobblestone(Math.min(64, nextValue)));
          }

          TaskAction taskAction = this.taskConfigTracker.tick(longValue, taskOperation);
          if (taskAction instanceof TaskAction.Abort currentAbort) {
            return this.createTaskOperationHandler23(currentAbort);
          } else if (taskAction instanceof TaskAction.Done) {
            this.taskConfigTracker = null;
            return new TaskAction.Wait("Rechecking stone requirement");
          } else {
            this.text6 = "Need " + nextValue + " stone · " + this.taskConfigTracker.status();
            return taskAction;
          }
        } else {
          this.taskConfigTracker = null;
          return this.createTaskOperationHandler2(
              longValue, taskOperation, taskData, this.config2.targetId(), this.config2.want());
        }
      } else {
        return this.taskData2 == null && taskData.count("minecraft:crafting_table") == 0
            ? this.createTaskOperationHandler2(
                longValue, taskOperation, taskData, "minecraft:crafting_table", 1)
            : this.createTaskOperationHandler22(longValue, taskOperation, taskData);
      }
    } else {
      if (this.taskConfigTracker6 == null) {
        this.taskConfigTracker6 =
            new TreeHarvestTask(
                TreeHarvestTask.Config.overworld(Math.min(256, value)),
                item -> this.calculateValue(item, this.checkCondition(taskOperation, item)));
      }

      TaskAction currentTaskAction = this.taskConfigTracker6.tick(longValue, taskOperation);
      if (currentTaskAction instanceof TaskAction.Abort nextAbort) {
        return this.createTaskOperationHandler23(nextAbort);
      } else if (currentTaskAction instanceof TaskAction.Done) {
        this.taskConfigTracker6 = null;
        return new TaskAction.Wait("Rechecking crafting materials");
      } else {
        this.text6 = "Need " + value + " logs · " + this.taskConfigTracker6.status();
        return currentTaskAction;
      }
    }
  }

  private TaskAction createTaskOperationHandler2(
      long size, TaskOperationHandler taskOperation, TaskData taskData, String text, int value) {
    TaskResolveService.Plan plan =
        TaskResolveService.resolve(this.items, text, value, taskData.inventory());
    if (!plan.missing().isEmpty()) {
      String currentText = plan.missing().getFirst();
      return this.createTaskOperationHandler23(
          new TaskAction.Abort(
              currentText.contains("iron")
                  ? "Smelting arrives with the mission phase"
                  : "Cannot obtain " + createText(currentText)));
    }

    if (plan.steps().isEmpty()) {
      return new TaskAction.Wait("Rechecking tools");
    }

    TaskResolveService.CraftStep craftStep = plan.steps().getFirst();
    if (craftStep.station() == TaskResolveService.Station.FURNACE) {
      return this.createTaskOperationHandler23(
          new TaskAction.Abort("Smelting arrives with the mission phase"));
    }

    if (craftStep.station() == TaskResolveService.Station.TABLE) {
      TaskNearestService.Find find = this.createFind(taskOperation, taskData);
      if (find != null && find.distSq() > 9.0) {
        this.text5 = "";
        TravelIsInGoalHandler.NearGoal currentX =
            new TravelIsInGoalHandler.NearGoal(find.x(), find.y(), find.z(), 1.0);
        TravelPolicy travelPolicy = TravelPolicy.legit(taskData.canSprint());
        if (this.travelStatusTracker == null || !find.pos().equals(this.taskData22)) {
          this.travelStatusTracker = new TravelStatusTracker(taskOperation.traversal());
          this.taskData22 = find.pos();
          this.travelStatusTracker.start(currentX, travelPolicy, taskData.player());
        }

        this.travelStatusTracker.tick(taskData.player());
        if (this.travelStatusTracker.status() != TravelStatusTracker.Status.UNAVAILABLE
            && this.travelStatusTracker.status() != TravelStatusTracker.Status.UNEXPLORED) {
          this.text6 = "Returning to workbench";
          return TaskAction.go(
              currentX, this.text6, this.travelStatusTracker.route(), false, travelPolicy);
        }

        return this.createTaskOperationHandler23(new TaskAction.Abort("Workbench unreachable"));
      }
    }

    int currentCount = taskData.count(craftStep.output());
    if (!craftStep.output().equals(this.text5) || currentCount != this.count2) {
      this.text5 = craftStep.output();
      this.count2 = currentCount;
      this.timestamp = size;
    }

    if (size - this.timestamp >= 10000L) {
      return this.createTaskOperationHandler23(
          new TaskAction.Abort(
              "Crafting stalled — craft " + createText(craftStep.output()) + " manually once"));
    }

    this.text6 = "Crafting " + createText(craftStep.output());
    return new TaskAction.Craft(craftStep.output());
  }

  private TaskAction createTaskOperationHandler22(
      long size, TaskOperationHandler taskOperation, TaskData taskData) {
    if (this.timestamp2 >= 0L && size - this.timestamp2 > 3000L) {
      return this.createTaskOperationHandler23(
          new TaskAction.Abort("Workbench placement was not confirmed"));
    }

    if (this.taskData2 == null) {
      int value = (int) Math.floor(taskData.player().x);
      int currentValue = (int) Math.floor(taskData.player().y);
      int nextValue = (int) Math.floor(taskData.player().z);
      double doubleValue = Double.POSITIVE_INFINITY;

      for (int index = value - 2; index <= value + 2; index++) {
        for (int currentIndex = nextValue - 2; currentIndex <= nextValue + 2; currentIndex++) {
          if (taskOperation.blocks().loaded(index, currentIndex)
              && (!(Math.abs(index + 0.5 - taskData.player().x) < 0.81)
                  || !(Math.abs(currentIndex + 0.5 - taskData.player().z) < 0.81))) {
            Double currentDoubleValue =
                taskOperation.traversal().floor(index, currentIndex, currentValue);
            if (currentDoubleValue != null
                && !(Math.abs(currentDoubleValue - Math.rint(currentDoubleValue)) > 0.01)) {
              int previousValue = (int) Math.rint(currentDoubleValue);
              String text = taskOperation.blocks().blockId(index, previousValue - 1, currentIndex);
              if (!text.equals("minecraft:air")
                  && !text.contains("water")
                  && !text.contains("lava")
                  && taskOperation
                      .blocks()
                      .blockId(index, previousValue, currentIndex)
                      .equals("minecraft:air")) {
                TaskBlockPosition taskBlockPosition =
                    new TaskBlockPosition(index, previousValue, currentIndex);
                double nextDoubleValue =
                    taskBlockPosition.distSq(
                        taskData.player().x, taskData.player().y, taskData.player().z);
                if (nextDoubleValue < doubleValue && nextDoubleValue < 9.0) {
                  doubleValue = nextDoubleValue;
                  this.taskData2 = taskBlockPosition;
                }
              }
            }
          }
        }
      }

      if (this.taskData2 == null) {
        return this.createTaskOperationHandler23(
            new TaskAction.Abort("No safe spot for a workbench"));
      }

      this.timestamp2 = size;
    }

    if (taskData.count("minecraft:crafting_table") == 0) {
      return new TaskAction.Wait("Confirming workbench placement");
    }

    this.text6 = "Placing workbench";
    return new TaskAction.Place("minecraft:crafting_table", this.taskData2, TaskAction.Face.UP);
  }

  private TaskAction createTaskOperationHandler23(TaskAction taskAction) {
    this.taskOperationHandler2 = taskAction;
    this.text6 =
        taskAction instanceof TaskAction.Abort abort
            ? abort.reason()
            : (taskAction instanceof TaskAction.Done done ? done.summary() : this.text6);
    return taskAction;
  }

  private static String createText(String text) {
    return text.replace("minecraft:", "");
  }

  public record Config(String targetId, int want) {
    public Config(String targetId, int want) {
      if (targetId != null && !targetId.isEmpty() && want >= 1 && want <= 8) {
        this.targetId = targetId;
        this.want = want;
      } else {
        throw new IllegalArgumentException("Invalid tool config");
      }
    }
  }
}
