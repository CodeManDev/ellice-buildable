package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelData;
import dev.felix.ellice.feature.travel.TravelIsInGoalHandler;
import dev.felix.ellice.feature.travel.TravelPolicy;
import java.util.List;
import java.util.Objects;

public sealed interface TaskAction
    permits TaskAction.Goto,
        TaskAction.Mine,
        TaskAction.Place,
        TaskAction.Craft,
        TaskAction.Strike,
        TaskAction.Cast,
        TaskAction.Reel,
        TaskAction.Loot,
        TaskAction.Wait,
        TaskAction.Done,
        TaskAction.Abort {
  static TaskAction.Goto go(
      TravelIsInGoalHandler travelIsInGoal,
      String text,
      List<TravelData> items,
      boolean enabled,
      TravelPolicy travelPolicy) {
    return new TaskAction.Goto(travelIsInGoal, text, items, enabled, travelPolicy.canSprint());
  }

  record Abort(String reason) implements TaskAction {
    public Abort(String reason) {
      reason = reason == null ? "" : reason;
      this.reason = reason;
    }
  }

  record Cast(TaskBlockPosition water) implements TaskAction {
    public Cast(TaskBlockPosition water) {
      Objects.requireNonNull(water, "water");
      this.water = water;
    }
  }

  record Craft(String outputId) implements TaskAction {
    public Craft(String outputId) {
      if (outputId != null && !outputId.isEmpty()) {
        this.outputId = outputId;
      } else {
        throw new IllegalArgumentException("Missing output id");
      }
    }
  }

  record Done(String summary) implements TaskAction {
    public Done(String summary) {
      summary = summary == null ? "" : summary;
      this.summary = summary;
    }
  }

  enum Face {
    UP,
    DOWN,
    NORTH,
    SOUTH,
    EAST,
    WEST;

    private static TaskAction.Face[] $values() {
      return new TaskAction.Face[] {UP, DOWN, NORTH, SOUTH, EAST, WEST};
    }
  }

  record Goto(
      TravelIsInGoalHandler goal,
      String label,
      List<TravelData> route,
      boolean arrived,
      boolean canSprint)
      implements TaskAction {
    public Goto(
        TravelIsInGoalHandler goal,
        String label,
        List<TravelData> route,
        boolean arrived,
        boolean canSprint) {
      Objects.requireNonNull(goal, "goal");
      route = List.copyOf(Objects.requireNonNull(route, "route"));
      label = label == null ? "" : label;
      this.goal = goal;
      this.label = label;
      this.route = route;
      this.arrived = arrived;
      this.canSprint = canSprint;
    }
  }

  record Loot(TaskBlockPosition pos, String id) implements TaskAction {
    public Loot(TaskBlockPosition pos, String id) {
      Objects.requireNonNull(pos, "pos");
      if (id != null && !id.isEmpty()) {
        this.pos = pos;
        this.id = id;
      } else {
        throw new IllegalArgumentException("Missing block id");
      }
    }
  }

  record Mine(TaskBlockPosition pos, String expectId) implements TaskAction {
    public Mine(TaskBlockPosition pos, String expectId) {
      Objects.requireNonNull(pos, "pos");
      if (expectId != null && !expectId.isEmpty()) {
        this.pos = pos;
        this.expectId = expectId;
      } else {
        throw new IllegalArgumentException("Missing block id");
      }
    }
  }

  record Place(String id, TaskBlockPosition pos, TaskAction.Face face) implements TaskAction {
    public Place(String id, TaskBlockPosition pos, TaskAction.Face face) {
      if (id != null && !id.isEmpty()) {
        Objects.requireNonNull(pos, "pos");
        Objects.requireNonNull(face, "face");
        this.id = id;
        this.pos = pos;
        this.face = face;
      } else {
        throw new IllegalArgumentException("Missing item id");
      }
    }
  }

  record Reel() implements TaskAction {}

  record Strike(int entityRef) implements TaskAction {}

  record Wait(String reason) implements TaskAction {
    public Wait(String reason) {
      reason = reason == null ? "" : reason;
      this.reason = reason;
    }
  }
}
