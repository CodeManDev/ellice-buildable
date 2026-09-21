package dev.felix.ellice.feature.task;

import dev.felix.ellice.feature.survival.SurvivalData;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.joml.Vector3d;

public record TaskData(
    Vector3d player,
    boolean onGround,
    boolean inWater,
    int food,
    Map<String, Integer> inventory,
    boolean inventoryFull,
    boolean dead,
    boolean threatened,
    float health,
    float maxHealth,
    float absorption,
    float saturation,
    int air,
    int maxAir,
    int fireTicks,
    double fallDistance,
    boolean eyeInFluid) {
  public TaskData(
      Vector3d player,
      boolean onGround,
      boolean inWater,
      int food,
      Map<String, Integer> inventory,
      boolean inventoryFull,
      boolean dead,
      boolean threatened,
      float health,
      float maxHealth,
      float absorption,
      float saturation,
      int air,
      int maxAir,
      int fireTicks,
      double fallDistance,
      boolean eyeInFluid) {
    player = new Vector3d(Objects.requireNonNull(player, "player"));
    inventory = Map.copyOf(Objects.requireNonNull(inventory, "inventory"));
    if (food < 0 || food > 20) {
      throw new IllegalArgumentException("Invalid food level");
    }

    if (Float.isFinite(health)
        && !(health < 0.0F)
        && Float.isFinite(maxHealth)
        && !(maxHealth <= 0.0F)
        && Float.isFinite(absorption)
        && !(absorption < 0.0F)
        && Float.isFinite(saturation)
        && !(saturation < 0.0F)
        && air >= 0
        && maxAir > 0
        && fireTicks >= 0
        && Double.isFinite(fallDistance)
        && !(fallDistance < 0.0)) {
      for (Entry entry : inventory.entrySet()) {
        if (entry.getKey() == null || entry.getValue() == null || (Integer) entry.getValue() < 0) {
          throw new IllegalArgumentException("Invalid inventory count");
        }
      }

      this.player = player;
      this.onGround = onGround;
      this.inWater = inWater;
      this.food = food;
      this.inventory = inventory;
      this.inventoryFull = inventoryFull;
      this.dead = dead;
      this.threatened = threatened;
      this.health = health;
      this.maxHealth = maxHealth;
      this.absorption = absorption;
      this.saturation = saturation;
      this.air = air;
      this.maxAir = maxAir;
      this.fireTicks = fireTicks;
      this.fallDistance = fallDistance;
      this.eyeInFluid = eyeInFluid;
    } else {
      throw new IllegalArgumentException("Invalid vitals");
    }
  }

  public static TaskData simple(Vector3d vector3d, int value, Map<String, Integer> entries) {
    return new TaskData(
        vector3d, true, false, value, entries, false, false, false, 20.0F, 20.0F, 0.0F, 5.0F, 300,
        300, 0, 0.0, false);
  }

  public int count(String text) {
    return this.inventory.getOrDefault(text, 0);
  }

  public boolean canSprint() {
    return this.food > 6;
  }

  public SurvivalData vitals() {
    return new SurvivalData(
        this.health,
        this.maxHealth,
        this.absorption,
        this.food,
        this.saturation,
        this.air,
        this.maxAir,
        this.fireTicks,
        this.fallDistance,
        this.onGround,
        this.inWater,
        this.eyeInFluid,
        !this.dead);
  }
}
