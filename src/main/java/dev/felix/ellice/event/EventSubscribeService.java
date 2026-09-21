package dev.felix.ellice.event;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public final class EventSubscribeService {
  private final Map<EventTypeService<?>, List<EventSubscribeService.ListenerEntry<?>>> entries =
      new ConcurrentHashMap<>();

  public <T> EventCancelHandler subscribe(
      EventTypeService<T> eventType, EventIsAfterHandler<T> eventIsAfter) {
    return this.subscribe(eventType, EventIsAfterHandler.Priority.DEFAULT, eventIsAfter);
  }

  public <T> EventCancelHandler subscribe(
      EventTypeService<T> eventType,
      EventIsAfterHandler.Priority priority,
      EventIsAfterHandler<T> eventIsAfter) {
    return this.subscribe(eventType, priority, item -> true, eventIsAfter);
  }

  public <T> EventCancelHandler subscribe(
      EventTypeService<T> eventType, Predicate<T> predicate, EventIsAfterHandler<T> eventIsAfter) {
    return this.subscribe(eventType, EventIsAfterHandler.Priority.DEFAULT, predicate, eventIsAfter);
  }

  public <T> EventCancelHandler subscribe(
      EventTypeService<T> eventType,
      EventIsAfterHandler.Priority priority,
      Predicate<T> predicate,
      EventIsAfterHandler<T> eventIsAfter) {
    EventSubscribeService.ListenerEntry listenerEntry =
        new EventSubscribeService.ListenerEntry(priority, predicate, eventIsAfter);
    this.entries
        .computeIfAbsent(eventType, item -> new CopyOnWriteArrayList<>())
        .add(listenerEntry);
    this.updateState(eventType);
    return new EventCancelHandler.Active(
        () -> {
          List<EventSubscribeService.ListenerEntry<?>> items = this.entries.get(eventType);
          if (items != null) {
            items.remove(listenerEntry);
          }
        });
  }

  public <T> EventSubscribeService.SubscriptionBuilder<T> on(EventTypeService<T> eventType) {
    return new EventSubscribeService.SubscriptionBuilder<>(this, eventType);
  }

  public <T> T post(EventTypeService<T> eventType, T t) {
    List<EventSubscribeService.ListenerEntry<?>> items = this.entries.get(eventType);
    if (items != null && !items.isEmpty()) {
      for (EventSubscribeService.ListenerEntry listenerEntry :
          (Iterable<EventSubscribeService.ListenerEntry>) (Iterable<?>) (items)) {
        EventSubscribeService.ListenerEntry currentListenerEntry = listenerEntry;
        if (!(t instanceof EventIsCancelledHandler eventIsCancelled
                && eventIsCancelled.isCancelled()
                && currentListenerEntry.priority != EventIsAfterHandler.Priority.MONITOR)
            && currentListenerEntry.filter.test((T) t)) {
          try {
            currentListenerEntry.listener.handle((T) t);
          } catch (Exception exception) {
            System.err.println("[EventBus] Error in listener for " + eventType.name());
            exception.printStackTrace();
          }
        }
      }

      return (T) t;
    } else {
      return (T) t;
    }
  }

  public int listenerCount(EventTypeService<?> eventType) {
    List<EventSubscribeService.ListenerEntry<?>> items = this.entries.get(eventType);
    return items == null ? 0 : items.size();
  }

  public void clear() {
    this.entries.clear();
  }

  private void updateState(EventTypeService<?> eventType) {
    List<EventSubscribeService.ListenerEntry<?>> items = this.entries.get(eventType);
    if (items != null && items.size() > 1) {
      items.sort(Comparator.comparingInt(item -> item.priority.ordinal()));
    }
  }

  private record ListenerEntry<T>(
      EventIsAfterHandler.Priority priority,
      Predicate<T> filter,
      EventIsAfterHandler<T> listener) {}

  public static final class SubscriptionBuilder<T> {
    private final EventSubscribeService eventSubscribeService;
    private final EventTypeService<T> eventTypeService;
    private EventIsAfterHandler.Priority priority2 = EventIsAfterHandler.Priority.DEFAULT;
    private Predicate<T> predicate = item -> true;

    SubscriptionBuilder(EventSubscribeService eventSubscribe, EventTypeService<T> eventType) {
      this.eventSubscribeService = eventSubscribe;
      this.eventTypeService = eventType;
    }

    public EventSubscribeService.SubscriptionBuilder<T> priority(
        EventIsAfterHandler.Priority currentPriority) {
      this.priority2 = currentPriority;
      return this;
    }

    public EventSubscribeService.SubscriptionBuilder<T> filter(Predicate<T> currentPredicate) {
      this.predicate = currentPredicate;
      return this;
    }

    public EventCancelHandler run(EventIsAfterHandler<T> eventIsAfter) {
      return this.eventSubscribeService.subscribe(
          this.eventTypeService, this.priority2, this.predicate, eventIsAfter);
    }
  }
}
