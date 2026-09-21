package dev.felix.ellice.feature.tickbase;

public final class TickbaseBeginService {
  private boolean fdlz85jnqah9;
  private Skip f3u4lx5if0m8;

  public TickbaseBeginService() {
    this.f3u4lx5if0m8 = Skip.NONE;
  }

  public void begin() {
    this.fdlz85jnqah9 = false;
    this.f3u4lx5if0m8 = Skip.NONE;
  }

  public boolean canSkip() {
    return !this.fdlz85jnqah9 && this.f3u4lx5if0m8 == Skip.NONE;
  }

  public void skipped(final boolean b) {
    this.f3u4lx5if0m8 = (b ? Skip.REPAY : Skip.CHARGE);
  }

  public Skip activity() {
    this.fdlz85jnqah9 = true;
    final Skip f3u4lx5if0m8 = this.f3u4lx5if0m8;
    this.f3u4lx5if0m8 = Skip.NONE;
    return f3u4lx5if0m8;
  }

  public boolean suppressEnd() {
    final boolean b = this.f3u4lx5if0m8 != Skip.NONE;
    this.f3u4lx5if0m8 = Skip.NONE;
    return b;
  }

  public enum Skip {
    NONE,
    CHARGE,
    REPAY;
  }
}
