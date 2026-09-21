package dev.felix.ellice.feature.task;

public interface TaskStep {
   String name();

   TaskAction tick(long longValue, TaskOperationHandler taskOperation);

   default String status() {
      return this.name();
   }

   default void stop() {
   }
}
