



package dev.felix.ellice.feature.backtrack;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.ArrayDeque;

public final class BacktrackDelayQueue<T>
{
    private final ArrayDeque<Entry<T>> f3j6n38kps5s;
    private final int fb9mc0rs0i9p;
    private int f93e98iqck82;
    private boolean f30v7m9r173k;
    
    public BacktrackDelayQueue(final int fb9mc0rs0i9p) {
        this.f3j6n38kps5s = new ArrayDeque<Entry<T>>();
        if (fb9mc0rs0i9p < 1) {
            throw new IllegalArgumentException("Positive capacity required");
        }
        this.fb9mc0rs0i9p = fb9mc0rs0i9p;
    }
    
    public boolean offer(final T obj, final long n, final int n2, final int n3) {
        Objects.requireNonNull(obj);
        if (n2 < 0 || n2 > 1000 || n3 < 1) {
            throw new IllegalArgumentException("Invalid packet delay or weight");
        }
        if (n3 > this.fb9mc0rs0i9p - this.f93e98iqck82) {
            return false;
        }
        this.f3j6n38kps5s.addLast(new Entry<T>(obj, n, n + n2 * 1000000L, n3));
        this.f93e98iqck82 += n3;
        return true;
    }
    
    public void releaseDue(final long n, final Consumer<T> consumer) {
        this.m8cttugazk0k(n, false, consumer);
    }
    
    public void releaseAll(final Consumer<T> consumer) {
        this.m8cttugazk0k(0L, true, consumer);
    }
    
    private void m8cttugazk0k(final long n, final boolean b, final Consumer<T> consumer) {
        if (this.f30v7m9r173k) {
            return;
        }
        this.f30v7m9r173k = true;
        try {
            while (!this.f3j6n38kps5s.isEmpty() && (b || n - this.f3j6n38kps5s.peekFirst().due() >= 0L)) {
                final Entry entry = this.f3j6n38kps5s.removeFirst();
                this.f93e98iqck82 -= entry.weight();
                consumer.accept((T)entry.value());
            }
        }
        finally {
            this.f30v7m9r173k = false;
        }
    }
    
    public void clear() {
        this.f3j6n38kps5s.clear();
        this.f93e98iqck82 = 0;
    }
    
    public int size() {
        return this.f93e98iqck82;
    }
    
    public boolean isEmpty() {
        return this.f3j6n38kps5s.isEmpty();
    }
    
    public long oldestAgeMillis(final long n) {
        return this.f3j6n38kps5s.isEmpty() ? 0L : Math.max(0L, (n - this.f3j6n38kps5s.peekFirst().arrived()) / 1000000L);
    }
    
    record Entry<T>(T value, long arrived, long due, int weight) {}
}
