package dev.felix.ellice.feature.backtrack;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.Consumer;

public final class BacktrackDelayQueue<T> {
  private final ArrayDeque<Entry<T>> items;
  private final int count;
  private int count2;
  private boolean enabled;

  public BacktrackDelayQueue(final int count) {
    this.items = new ArrayDeque<Entry<T>>();
    if (count < 1) {
      throw new IllegalArgumentException("Positive capacity required");
    }
    this.count = count;
  }

  public boolean offer(final T obj, final long n, final int n2, final int n3) {
    Objects.requireNonNull(obj);
    if (n2 < 0 || n2 > 1000 || n3 < 1) {
      throw new IllegalArgumentException("Invalid packet delay or weight");
    }
    if (n3 > this.count - this.count2) {
      return false;
    }
    this.items.addLast(new Entry<T>(obj, n, n + n2 * 1000000L, n3));
    this.count2 += n3;
    return true;
  }

  public void releaseDue(final long n, final Consumer<T> consumer) {
    this.updateState(n, false, consumer);
  }

  public void releaseAll(final Consumer<T> consumer) {
    this.updateState(0L, true, consumer);
  }

  private void updateState(final long n, final boolean b, final Consumer<T> consumer) {
    if (this.enabled) {
      return;
    }
    this.enabled = true;
    try {
      while (!this.items.isEmpty() && (b || n - this.items.peekFirst().due() >= 0L)) {
        final Entry entry = this.items.removeFirst();
        this.count2 -= entry.weight();
        consumer.accept((T) entry.value());
      }
    } finally {
      this.enabled = false;
    }
  }

  public void clear() {
    this.items.clear();
    this.count2 = 0;
  }

  public int size() {
    return this.count2;
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public long oldestAgeMillis(final long n) {
    return this.items.isEmpty()
        ? 0L
        : Math.max(0L, (n - this.items.peekFirst().arrived()) / 1000000L);
  }

  record Entry<T>(T value, long arrived, long due, int weight) {}
}
