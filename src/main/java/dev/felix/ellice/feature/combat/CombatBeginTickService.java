package dev.felix.ellice.feature.combat;

public final class CombatBeginTickService {
  private boolean enabled;
  private boolean enabled2 = true;
  private boolean enabled3;
  private boolean enabled4;

  public synchronized void beginTick() {
    this.enabled = false;
    this.enabled4 = false;
  }

  public synchronized void beginInput() {
    this.enabled = true;
  }

  public synchronized void endInput() {
    this.enabled = false;
  }

  public synchronized void movement() {
    this.enabled2 = true;
  }

  public synchronized void tickEnd() {
    this.enabled2 = false;
    this.enabled3 = false;
  }

  public synchronized void action() {
    this.enabled3 = true;
  }

  public synchronized void reserve() {
    this.enabled4 = true;
  }

  public synchronized boolean reserved() {
    return this.enabled4;
  }

  public synchronized boolean canAct() {
    return this.enabled && !this.enabled2 && !this.enabled3 && !this.enabled4;
  }

  public synchronized void reset() {
    this.enabled = this.enabled3 = this.enabled4 = false;
    this.enabled2 = true;
  }
}
