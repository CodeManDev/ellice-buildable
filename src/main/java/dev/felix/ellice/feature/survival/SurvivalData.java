package dev.felix.ellice.feature.survival;

public record SurvivalData(
    float health,
    float maxHealth,
    float absorption,
    int food,
    float saturation,
    int air,
    int maxAir,
    int fireTicks,
    double fallDistance,
    boolean onGround,
    boolean inWater,
    boolean eyeInFluid,
    boolean alive) {
  public SurvivalData(
      float health,
      float maxHealth,
      float absorption,
      int food,
      float saturation,
      int air,
      int maxAir,
      int fireTicks,
      double fallDistance,
      boolean onGround,
      boolean inWater,
      boolean eyeInFluid,
      boolean alive) {
    if (Float.isFinite(health)
        && !(health < 0.0F)
        && Float.isFinite(maxHealth)
        && !(maxHealth <= 0.0F)
        && Float.isFinite(absorption)
        && !(absorption < 0.0F)
        && food >= 0
        && food <= 20
        && Float.isFinite(saturation)
        && !(saturation < 0.0F)
        && air >= 0
        && maxAir > 0
        && fireTicks >= 0
        && Double.isFinite(fallDistance)
        && !(fallDistance < 0.0)) {
      this.health = health;
      this.maxHealth = maxHealth;
      this.absorption = absorption;
      this.food = food;
      this.saturation = saturation;
      this.air = air;
      this.maxAir = maxAir;
      this.fireTicks = fireTicks;
      this.fallDistance = fallDistance;
      this.onGround = onGround;
      this.inWater = inWater;
      this.eyeInFluid = eyeInFluid;
      this.alive = alive;
    } else {
      throw new IllegalArgumentException("Invalid vitals snapshot");
    }
  }

  public float effectiveHealth() {
    return this.health + this.absorption;
  }
}
