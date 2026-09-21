package dev.felix.ellice.ui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class ComponentStateController<T> {
  private T t;
  private final List<Consumer<T>> items = new ArrayList<>();

  public ComponentStateController(T currentT) {
    this.t = (T) currentT;
  }

  public T get() {
    return this.t;
  }

  public boolean set(T currentT) {
    if (Objects.equals(this.t, currentT)) {
      return false;
    }

    this.t = (T) currentT;

    for (Consumer consumer : List.copyOf(this.items)) {
      consumer.accept(currentT);
    }

    return true;
  }

  public boolean update(UnaryOperator<T> unaryOperator) {
    return this.set((T) Objects.requireNonNull(unaryOperator, "update").apply(this.t));
  }

  public AutoCloseable subscribe(Consumer<T> consumer) {
    Objects.requireNonNull(consumer, "listener");
    this.items.add(consumer);
    return () -> this.items.remove(consumer);
  }
}
