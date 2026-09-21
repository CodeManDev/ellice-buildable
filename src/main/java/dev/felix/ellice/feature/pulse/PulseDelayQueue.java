package dev.felix.ellice.feature.pulse;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PulseDelayQueue<T> {
  private final ArrayDeque<T> items = new ArrayDeque<>();
  private final int count2;
  private long timestamp;
  private long timestamp2;

  public PulseDelayQueue(int value) {
    if (value < 1) {
      throw new IllegalArgumentException("Positive capacity required");
    }

    this.count2 = value;
  }

  public boolean offer(T t, long longValue, int value) {
    Objects.requireNonNull(t);
    if (value >= 1 && value <= 150) {
      if (this.items.size() != this.count2 && !this.due(longValue)) {
        if (this.items.isEmpty()) {
          this.timestamp = longValue;
          this.timestamp2 = longValue + value * 1000000L;
        }

        this.items.addLast((T) t);
        return true;
      } else {
        return false;
      }
    } else {
      throw new IllegalArgumentException("Window must be 1–150 ms");
    }
  }

  public boolean due(long longValue) {
    return !this.items.isEmpty() && longValue - this.timestamp2 >= 0L;
  }

  public int size() {
    return this.items.size();
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public int count(Predicate<? super T> predicate) {
    return (int) this.items.stream().filter(predicate).count();
  }

  public long ageMillis(long longValue) {
    return this.isEmpty() ? 0L : Math.max(0L, (longValue - this.timestamp) / 1000000L);
  }

  public void releaseAll(Consumer<T> consumer) {
    while (!this.items.isEmpty()) {
      consumer.accept(this.items.removeFirst());
    }
  }

  public void clear() {
    this.items.clear();
  }
}
