package dev.felix.ellice.feature.tool;

import java.util.function.Supplier;

public final class ToolActiveService {
  private ToolActiveService.Hand hand;
  private int count;
  private int count2;
  private int count3;
  private boolean enabled;
  private boolean enabled2;
  private boolean enabled3;

  public boolean active() {
    return this.hand != null;
  }

  public boolean scoped() {
    return this.count3 > 0;
  }

  public int tool() {
    return this.count;
  }

  public boolean owned() {
    return this.hand != null
        && this.hand.valid()
        && this.hand.selected() == (this.enabled && this.count3 == 0 ? this.count2 : this.count);
  }

  public void begin(
      ToolActiveService.Hand currentHand, int value, boolean currentEnabled, boolean nextEnabled) {
    if (value >= 0 && value < 9) {
      this.release(true);
      if (currentHand.valid()) {
        this.hand = currentHand;
        this.count2 = currentHand.selected();
        this.count = value;
        this.enabled = currentEnabled;
        this.enabled2 = nextEnabled;
        if (!currentEnabled) {
          currentHand.select(this.count);
        }
      }
    } else {
      throw new IllegalArgumentException("Not a hotbar slot: " + value);
    }
  }

  public boolean mine(Supplier<Boolean> supplier) {
    if (!this.active()) {
      return (Boolean) supplier.get();
    }

    try {
      boolean enabled =
          this.createValue(
              () -> {
                this.hand.sync();
                return (Boolean) supplier.get();
              });
      if (!enabled) {
        this.release(true);
      } else if (this.hand != null && (!this.hand.valid() || !this.hand.destroying())) {
        this.release(false);
      }

      return enabled;
    } catch (RuntimeException | Error runtimeExceptionError) {
      this.release(true);
      throw runtimeExceptionError;
    }
  }

  public void synchronize(Runnable runnable) {
    if (this.hand != null && !this.owned() && this.count3 == 0) {
      this.release(true);
    }

    this.createValue(
        () -> {
          runnable.run();
          return null;
        });
  }

  public void stop(Runnable runnable) {
    try {
      this.createValue(
          () -> {
            runnable.run();
            return null;
          });
    } finally {
      if (!this.enabled3) {
        this.release(false);
      }
    }
  }

  public void release(boolean currentEnabled) {
    ToolActiveService.Hand currentHand = this.hand;
    if (currentHand != null && !this.enabled3) {
      boolean nextEnabled = this.owned();
      this.enabled3 = true;

      try {
        if (currentHand.valid() && currentEnabled && currentHand.destroying()) {
          this.createValue(
              () -> {
                currentHand.abort();
                return null;
              });
        }
      } finally {
        this.hand = null;
        this.enabled3 = false;
        if (currentHand.valid()) {
          if (!this.enabled
              && this.enabled2
              && nextEnabled
              && currentHand.selected() == this.count) {
            currentHand.select(this.count2);
          }

          currentHand.sync();
        }
      }
    }
  }

  public void forget() {
    this.hand = null;
  }

  private <T> T createValue(Supplier<T> supplier) {
    ToolActiveService.Hand currentHand = this.hand;
    if (currentHand != null && currentHand.valid()) {
      int value = currentHand.selected();
      int currentCount = !this.enabled && value == this.count ? 0 : 1;
      if (currentCount != 0) {
        currentHand.select(this.count);
      }

      this.count3++;

      try {
        return (T) supplier.get();
      } finally {
        this.count3--;
        if (currentCount != 0 && currentHand.valid() && currentHand.selected() == this.count) {
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

    void abort();

    boolean destroying();
  }
}
