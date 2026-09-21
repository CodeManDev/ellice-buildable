package dev.felix.ellice.event;

@FunctionalInterface
public interface EventIsAfterHandler<T> {
  void handle(T t);

  enum Priority {
    FIRST,
    EARLY,
    DEFAULT,
    LATE,
    LAST,
    MONITOR;

    public boolean isAfter(EventIsAfterHandler.Priority priority) {
      return this.ordinal() > priority.ordinal();
    }

    private static EventIsAfterHandler.Priority[] $values() {
      return new EventIsAfterHandler.Priority[] {FIRST, EARLY, DEFAULT, LATE, LAST, MONITOR};
    }
  }
}
