package dev.felix.ellice.feature.scaffold;

public final class ScaffoldSneakService {
  private long timestamp = Long.MIN_VALUE;
  private int count;
  private int count2;

  public boolean sneak(
      long l, String string, boolean bl, boolean bl2, boolean bl3, boolean bl4, int n) {
    int n2;
    if ("Off".equals(string)) {
      this.timestamp = Long.MIN_VALUE;
      return false;
    }
    int n3 =
        n2 =
            "Always".equals(string)
                    || "Start".equals(string) && bl && bl2
                    || "Eagle".equals(string) && bl
                    || ("Edge".equals(string) || "Edge + Acquire".equals(string)) && bl
                    || ("Acquire".equals(string) || "Edge + Acquire".equals(string)) && bl2
                    || bl3
                    || bl4
                ? 1
                : 0;
    if (n2 != 0) {
      this.timestamp = l + (long) Math.max(0, n);
    }
    return (n2 != 0 || l <= this.timestamp ? 1 : 0) != 0;
  }

  public boolean jump(
      boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, int n) {
    boolean bl7;
    if (bl2 || !bl6 || n <= 0) {
      this.count = 0;
    }
    boolean bl8 = bl7 = bl || bl6 && n > 0 && this.count >= n && bl5 && bl4 && bl3 && !bl2;
    if (bl7 && bl3) {
      this.count = 0;
    }
    return bl7;
  }

  public void placed() {
    ++this.count;
    ++this.count2;
  }

  public boolean eagleDue(int n) {
    return this.count2 % (Math.max(0, n) + 1) == 0;
  }

  public void restart() {
    this.count = 0;
    this.count2 = 0;
  }

  public void reset() {
    this.restart();
    this.timestamp = Long.MIN_VALUE;
  }
}
