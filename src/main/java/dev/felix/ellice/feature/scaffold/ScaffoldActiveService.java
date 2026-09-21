package dev.felix.ellice.feature.scaffold;

import java.util.function.Supplier;

public final class ScaffoldActiveService {
  private ScaffoldActiveService.Hand hand;
  private int count;
  private int count2;
  private int count3;
  private boolean enabled;
  private boolean enabled2;
  private static ScaffoldActiveService.Hand hand2;
  private static boolean enabled3;

  public boolean active() {
    return this.hand != null;
  }

  public boolean scoped() {
    return this.count3 > 0;
  }

  public int blockSlot() {
    return this.hand == null ? -1 : this.count2;
  }

  public int visibleSlot() {
    return this.hand == null ? -1 : this.count;
  }

  public boolean owned() {
    return this.hand != null
        && this.hand.valid()
        && this.hand.selected() == (this.enabled && !this.scoped() ? this.count : this.count2);
  }

  public void acquire(
      ScaffoldActiveService.Hand currentHand,
      int value,
      boolean currentEnabled,
      boolean nextEnabled) {
    if (value >= 0 && value <= 8) {
      if (this.hand != null && (!this.owned() || this.enabled != currentEnabled)) {
        this.release();
      }

      if (this.hand == null) {
        if (!currentHand.valid()) {
          return;
        }

        this.hand = currentHand;
        this.count = currentHand.selected();
        this.enabled = currentEnabled;
        hand2 = null;
        enabled3 = false;
      }

      this.enabled2 = nextEnabled;
      this.count2 = value;
      if (!this.enabled) {
        this.hand.select(this.count2);
      }
    } else {
      throw new IllegalArgumentException("Invalid hotbar slot");
    }
  }

  public <T> T use(Supplier<T> supplier) {
    try {
      return this.createValue(
          () -> {
            if (this.hand != null) {
              this.hand.sync();
            }

            return (T) supplier.get();
          });
    } catch (RuntimeException | Error runtimeExceptionError) {
      this.release();
      throw runtimeExceptionError;
    }
  }

  public void synchronize(Runnable runnable) {
    if (this.hand != null && !this.scoped() && !this.owned()) {
      this.release();
    }

    this.createValue(
        () -> {
          runnable.run();
          return null;
        });
  }

  public void release() {
    this.release(true);
  }

  public void release(boolean currentEnabled) {
    ScaffoldActiveService.Hand currentHand = this.hand;
    if (currentHand != null) {
      boolean nextEnabled = this.owned();
      this.hand = null;
      if (currentHand.valid()) {
        if (!this.enabled && this.enabled2 && nextEnabled) {
          currentHand.select(this.count);
        }

        if (currentEnabled && currentHand.valid()) {
          currentHand.sync();
        } else if (!currentEnabled) {
          hand2 = currentHand;
          enabled3 = true;
        }
      }
    }
  }

  public static void flushPendingSync() {
    if (enabled3) {
      enabled3 = false;
      ScaffoldActiveService.Hand hand = hand2;
      hand2 = null;
      if (hand != null && hand.valid()) {
        hand.sync();
      }
    }
  }

  private <T> T createValue(Supplier<T> supplier) {
    ScaffoldActiveService.Hand currentHand = this.hand;
    if (currentHand != null && currentHand.valid()) {
      int value = currentHand.selected();
      currentHand.select(this.count2);
      this.count3++;

      try {
        return (T) supplier.get();
      } finally {
        this.count3--;
        if (this.enabled && currentHand.valid() && currentHand.selected() == this.count2) {
          currentHand.select(value);
        }
      }
    } else {
      return (T) supplier.get();
    }
  }

  public interface Hand {
    boolean valid();

    int selected();

    void select(int value);

    void sync();
  }
}
