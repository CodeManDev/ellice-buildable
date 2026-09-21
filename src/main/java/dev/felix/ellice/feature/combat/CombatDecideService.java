package dev.felix.ellice.feature.combat;

import java.util.UUID;

public final class CombatDecideService {
  private UUID uUID;
  private UUID uUID2;
  private long timestamp = Long.MIN_VALUE;
  private long timestamp2 = Long.MIN_VALUE;
  private long timestamp3 = Long.MIN_VALUE;
  private long timestamp4 = Long.MIN_VALUE;

  public CombatDecideService.Action decide(long longValue, CombatDecideService.Frame frame) {
    if (longValue < this.timestamp4) {
      this.clear();
    }

    this.timestamp4 = longValue;
    int value = frame.enabled() && frame.threat() && frame.target() != null ? 1 : 0;
    if (value == 0) {
      this.uUID = null;
      this.timestamp = Long.MIN_VALUE;
    } else if (!frame.target().equals(this.uUID)) {
      this.uUID = frame.target();
      this.timestamp = longValue;
    }

    if (this.timestamp3 == longValue) {
      return frame.holding() ? CombatDecideService.Action.HOLD : CombatDecideService.Action.WAIT;
    }

    if (frame.holding()) {
      if (value != 0 && frame.target().equals(this.uUID2) && this.timestamp2 != Long.MIN_VALUE) {
        long currentLongValue =
            Math.max(Math.max(1, frame.minimumHoldTicks()), frame.activationTicks() + 1L);
        return longValue - this.timestamp2 >= currentLongValue && calculateValue(frame) <= 1.0
            ? CombatDecideService.Action.RELEASE
            : CombatDecideService.Action.HOLD;
      } else {
        return CombatDecideService.Action.RELEASE;
      }
    } else if (this.timestamp2 != Long.MIN_VALUE) {
      this.interrupted(longValue);
      return CombatDecideService.Action.WAIT;
    } else {
      long nextLongValue =
          Math.max(Math.max(1, frame.minimumHoldTicks()), frame.activationTicks() + 1L) + 1L;
      return value != 0
              && frame.available()
              && longValue - this.timestamp >= Math.max(0, frame.reactionTicks())
              && calculateValue(frame) >= nextLongValue
          ? CombatDecideService.Action.RAISE
          : CombatDecideService.Action.PASS;
    }
  }

  public void raised(long longValue, UUID uUID) {
    this.uUID2 = uUID;
    this.timestamp2 = longValue;
    this.timestamp3 = longValue;
  }

  public void interrupted(long longValue) {
    this.uUID2 = null;
    this.timestamp2 = Long.MIN_VALUE;
    this.timestamp3 = longValue;
  }

  public void clear() {
    this.uUID = null;
    this.uUID2 = null;
    this.timestamp = Long.MIN_VALUE;
    this.timestamp2 = Long.MIN_VALUE;
    this.timestamp3 = Long.MIN_VALUE;
    this.timestamp4 = Long.MIN_VALUE;
  }

  private static double calculateValue(CombatDecideService.Frame frame) {
    return Float.isFinite(frame.attackStrength())
            && Float.isFinite(frame.weaponCooldownTicks())
            && !(frame.weaponCooldownTicks() <= 0.0F)
        ? (1.0 - Math.max(0.0F, Math.min(1.0F, frame.attackStrength())))
            * frame.weaponCooldownTicks()
        : 0.0;
  }

  public enum Action {
    PASS,
    RAISE,
    HOLD,
    RELEASE,
    WAIT;

    private static CombatDecideService.Action[] $values() {
      return new CombatDecideService.Action[] {PASS, RAISE, HOLD, RELEASE, WAIT};
    }
  }

  public record Frame(
      UUID target,
      boolean threat,
      boolean enabled,
      boolean available,
      boolean holding,
      float attackStrength,
      float weaponCooldownTicks,
      int activationTicks,
      int minimumHoldTicks,
      int reactionTicks) {}
}
