package dev.felix.ellice.feature.combat;

import java.util.UUID;

public final class CombatAttackedService {
  private UUID uUID;
  private long timestamp;
  private long timestamp2;

  public void attacked(long longValue, UUID uUID, boolean enabled) {
    this.attacked(longValue, uUID, enabled, 1);
  }

  public void attacked(long longValue, UUID currentUUID, boolean enabled, int value) {
    this.clear();
    int currentValue = Math.max(0, value);
    if (enabled && currentValue > 0) {
      this.uUID = currentUUID;
      this.timestamp = longValue + 1L;
      this.timestamp2 = longValue + currentValue;
    }
  }

  public boolean suppress(long longValue, UUID currentUUID, boolean enabled) {
    if (this.uUID == null) {
      return false;
    }

    if (this.uUID.equals(currentUUID) && enabled && longValue <= this.timestamp2) {
      return longValue >= this.timestamp;
    }

    this.clear();
    return false;
  }

  public void clear() {
    this.uUID = null;
  }
}
