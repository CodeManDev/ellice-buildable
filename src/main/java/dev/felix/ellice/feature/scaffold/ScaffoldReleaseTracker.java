package dev.felix.ellice.feature.scaffold;

public final class ScaffoldReleaseTracker {
  private long timestamp;
  private String text;
  private int count;

  public boolean attempt(long longValue, String currentText, int value) {
    int currentValue = "Held Use".equals(currentText) ? 4 : Math.max(1, value);
    if (!currentText.equals(this.text) || this.count != currentValue) {
      this.text = currentText;
      this.count = currentValue;
      this.timestamp = longValue;
    }

    if ("On Target".equals(currentText)) {
      return true;
    }

    if (longValue < this.timestamp) {
      return false;
    }

    this.timestamp = longValue + currentValue;
    return true;
  }

  public void reset() {
    this.text = null;
    this.timestamp = 0L;
  }

  public boolean ready(long longValue) {
    return this.text == null || "On Target".equals(this.text) || longValue >= this.timestamp;
  }
}
