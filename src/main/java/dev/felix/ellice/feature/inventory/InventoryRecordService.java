package dev.felix.ellice.feature.inventory;

public final class InventoryRecordService {
  private InventoryRecordService.Opening opening;

  public void reset() {
    this.opening = null;
  }

  public void record(
      Object value, Object currentValue, InventoryRecordService.Kind kind, long longValue) {
    this.opening =
        value != null && kind != null
            ? new InventoryRecordService.Opening(value, currentValue, kind, longValue)
            : null;
  }

  public boolean consume(
      Object value, Object currentValue, InventoryRecordService.Kind currentKind, long longValue) {
    InventoryRecordService.Opening currentOpening = this.opening;
    if (currentOpening == null) {
      return false;
    }

    long currentLongValue = longValue - currentOpening.at();
    if (currentOpening.level() == value && currentLongValue >= 0L && currentLongValue <= 5000L) {
      if (currentOpening.previousMenu() == currentValue) {
        return false;
      }

      this.reset();
      return currentOpening.kind() == currentKind;
    } else {
      this.reset();
      return false;
    }
  }

  public enum Kind {
    CHEST,
    SHULKER;

    private static InventoryRecordService.Kind[] $values() {
      return new InventoryRecordService.Kind[] {CHEST, SHULKER};
    }
  }

  private record Opening(
      Object level, Object previousMenu, InventoryRecordService.Kind kind, long at) {}
}
