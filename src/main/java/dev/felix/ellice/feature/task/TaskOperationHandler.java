package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.travel.TravelLoadedHandler;
import java.util.List;

public interface TaskOperationHandler {
  TravelLoadedHandler traversal();

  TaskWorldView blocks();

  TaskData sense();

  default List<TaskOperationHandler.SeenEntity> entities() {
    return List.of();
  }

  default List<TaskOperationHandler.DroppedItem> droppedItems() {
    return List.of();
  }

  default TaskBlockPosition foliageInWay(
      double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return null;
  }

  default long timeOfDay() {
    return 6000L;
  }

  default boolean skyAbove() {
    return true;
  }

  record DroppedItem(String id, int count, int ref, double x, double y, double z) {}

  record SeenEntity(String type, int ref, double x, double y, double z, double vy) {}
}
