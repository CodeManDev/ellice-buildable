package dev.felix.ellice.feature.scaffold;

public final class ScaffoldBudgetService {
  private double value;

  public double budget(
      String text,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      double previousDoubleValue) {
    if (!"Smooth".equals(text)) {
      this.value = 0.0;
      return doubleValue;
    } else {
      double sourceDoubleValue =
          Math.max(previousDoubleValue, doubleValue * Math.pow(1.0 - currentDoubleValue, 2.0));
      double targetDoubleValue =
          Math.sqrt(2.0 * sourceDoubleValue * Math.max(0.0, nextDoubleValue));
      this.value =
          Math.min(doubleValue, Math.min(this.value + sourceDoubleValue, targetDoubleValue));
      return Math.min(doubleValue, Math.max(previousDoubleValue * 1.01, this.value));
    }
  }

  public void reset() {
    this.value = 0.0;
  }
}
