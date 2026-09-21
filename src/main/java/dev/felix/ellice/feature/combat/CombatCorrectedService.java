package dev.felix.ellice.feature.combat;

public final class CombatCorrectedService {
  private boolean enabled;
  private long timestamp;
  private int fgbw8oqehihm;

  public void corrected(final long timestamp, final int fgbw8oqehihm) {
    if (fgbw8oqehihm < 0 || fgbw8oqehihm > 20) {
      throw new IllegalArgumentException("Recovery must be 0–20 ticks");
    }
    this.timestamp = timestamp;
    this.fgbw8oqehihm = fgbw8oqehihm;
    this.enabled = (fgbw8oqehihm != 0);
  }

  public boolean blocks(final long n) {
    return this.enabled
        && (n < this.timestamp
            || n - this.timestamp <= this.fgbw8oqehihm
            || (this.enabled = false));
  }

  public void clear() {
    this.enabled = false;
  }
}
