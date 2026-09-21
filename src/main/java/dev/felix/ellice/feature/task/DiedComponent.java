package dev.felix.ellice.feature.task;

final class DiedComponent {
  private DiedComponent() {}

  static TaskAction.Abort critical(TaskData taskData) {
    if (taskData.dead()) {
      return new TaskAction.Abort("Died");
    } else if (taskData.threatened()) {
      return new TaskAction.Abort("Players nearby — standing down");
    } else {
      return taskData.inventoryFull() ? new TaskAction.Abort("Inventory full") : null;
    }
  }
}
