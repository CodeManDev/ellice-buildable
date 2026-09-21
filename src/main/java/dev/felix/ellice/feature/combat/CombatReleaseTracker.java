package dev.felix.ellice.feature.combat;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public final class CombatReleaseTracker {
  private static final double value = 1.0E9;
  private static final double value2 = 5.0E7;
  private final Random random;
  private UUID uUID;
  private long timestamp = Long.MIN_VALUE;
  private int count;
  private long timestamp2 = Long.MIN_VALUE;
  private long timestamp3;
  private boolean enabled;

  public CombatReleaseTracker(long longValue) {
    this.random = new Random(longValue);
  }

  public boolean cadenceReady(long longValue) {
    return !this.enabled || longValue - this.timestamp3 >= 0L;
  }

  public int stableTicks() {
    return this.count;
  }

  public boolean ready(
      long longValue,
      long currentLongValue,
      UUID currentUUID,
      CombatData combatData,
      CombatReleaseTracker.Profile currentProfile) {
    Objects.requireNonNull(currentUUID, "target");
    Objects.requireNonNull(combatData, "state");
    Objects.requireNonNull(currentProfile, "profile");
    if (!currentUUID.equals(this.uUID)
        || longValue > this.timestamp + 1L
        || longValue < this.timestamp) {
      this.uUID = currentUUID;
      this.count = 1;
    } else if (longValue != this.timestamp) {
      this.count = Math.min(currentProfile.requiredAimTicks(), this.count + 1);
    }

    this.timestamp = longValue;
    return longValue != this.timestamp2
        && this.cadenceReady(currentLongValue)
        && (!currentProfile.waitForCooldown() || combatData.attackStrength() >= 1.0F)
        && this.count >= currentProfile.requiredAimTicks()
        && (!currentProfile.waitForHurt() || combatData.targetHurtTicks() == 0);
  }

  public void attacked(
      long longValue,
      long currentLongValue,
      CombatData combatData,
      CombatReleaseTracker.Profile profile) {
    double doubleValue =
        profile.minCps() + this.random.nextDouble() * (profile.maxCps() - profile.minCps());
    double currentDoubleValue = 1.0E9 / doubleValue;
    if (!profile.comboTiming()) {
      currentDoubleValue = Math.max(currentDoubleValue, combatData.weaponCooldownTicks() * 5.0E7);
    }

    if (profile.intervalJitter() > 0.0) {
      currentDoubleValue *= 1.04 + this.random.nextDouble() * profile.intervalJitter();
    }

    if (profile.pauseChance() > 0.0 && this.random.nextDouble() < profile.pauseChance()) {
      currentDoubleValue += 5.0E7;
    }

    this.timestamp3 = currentLongValue + (long) Math.ceil(currentDoubleValue);
    this.timestamp2 = longValue;
    this.enabled = true;
  }

  public void loseTarget() {
    this.uUID = null;
    this.timestamp = Long.MIN_VALUE;
    this.count = 0;
  }

  public record Profile(
      double minCps,
      double maxCps,
      boolean smart,
      boolean comboTiming,
      boolean waitForCooldown,
      boolean waitForHurt,
      int extraAimTicks,
      double hitChance,
      double intervalJitter,
      double pauseChance) {
    public Profile(double doubleValue, double currentDoubleValue, boolean enabled) {
      this(doubleValue, currentDoubleValue, enabled, false);
    }

    public Profile(
        double doubleValue, double currentDoubleValue, boolean enabled, boolean currentEnabled) {
      this(
          doubleValue,
          currentDoubleValue,
          enabled,
          currentEnabled,
          true,
          enabled,
          0,
          1.0,
          enabled && !currentEnabled ? 0.12 : 0.0,
          enabled && !currentEnabled ? 0.15 : 0.0);
    }

    public Profile(
        double minCps,
        double maxCps,
        boolean smart,
        boolean comboTiming,
        boolean waitForCooldown,
        boolean waitForHurt,
        int extraAimTicks,
        double hitChance,
        double intervalJitter,
        double pauseChance) {
      if (!Double.isFinite(minCps)
          || !Double.isFinite(maxCps)
          || minCps <= 0.0
          || maxCps < minCps
          || maxCps > 20.0) {
        throw new IllegalArgumentException("CPS must be ordered and in (0, 20]");
      }

      if (extraAimTicks < 0 || extraAimTicks > 8) {
        throw new IllegalArgumentException("extraAimTicks must be in [0, 8]");
      }

      if (!Double.isFinite(hitChance) || hitChance < 0.0 || hitChance > 1.0) {
        throw new IllegalArgumentException("hitChance must be in [0, 1]");
      }

      if (!Double.isFinite(intervalJitter) || intervalJitter < 0.0 || intervalJitter > 1.0) {
        throw new IllegalArgumentException("intervalJitter must be in [0, 1]");
      }

      if (Double.isFinite(pauseChance) && !(pauseChance < 0.0) && !(pauseChance > 1.0)) {
        this.minCps = minCps;
        this.maxCps = maxCps;
        this.smart = smart;
        this.comboTiming = comboTiming;
        this.waitForCooldown = waitForCooldown;
        this.waitForHurt = waitForHurt;
        this.extraAimTicks = extraAimTicks;
        this.hitChance = hitChance;
        this.intervalJitter = intervalJitter;
        this.pauseChance = pauseChance;
      } else {
        throw new IllegalArgumentException("pauseChance must be in [0, 1]");
      }
    }

    public int requiredAimTicks() {
      return Math.max(
          1, (this.smart && !this.comboTiming ? 2 : 1) + Math.max(0, this.extraAimTicks));
    }
  }
}
