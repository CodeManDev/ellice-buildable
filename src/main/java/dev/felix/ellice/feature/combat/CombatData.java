package dev.felix.ellice.feature.combat;

public record CombatData(float attackStrength, float weaponCooldownTicks, int targetHurtTicks) {
  public CombatData(float attackStrength, float weaponCooldownTicks, int targetHurtTicks) {
    if (Float.isFinite(attackStrength)
        && !(attackStrength < 0.0F)
        && Float.isFinite(weaponCooldownTicks)
        && !(weaponCooldownTicks <= 0.0F)
        && targetHurtTicks >= 0) {
      this.attackStrength = attackStrength;
      this.weaponCooldownTicks = weaponCooldownTicks;
      this.targetHurtTicks = targetHurtTicks;
    } else {
      throw new IllegalArgumentException("Invalid attack timing state");
    }
  }
}
