package dev.felix.ellice.feature.survival;

import java.util.List;

public record SurvivalItemData(
    Object session,
    List<SurvivalItemData.Item> hotbar,
    SurvivalItemData.Item offhand,
    List<SurvivalItemData.Item> storage,
    int selected,
    int food,
    float health,
    float maxHealth,
    float absorption,
    float saturation,
    int air,
    int maxAir,
    int fireTicks,
    double fallDistance,
    boolean onGround,
    boolean inWater,
    boolean eyeInFluid,
    boolean alive,
    boolean usingItem,
    boolean manualInput,
    boolean paused) {
  public SurvivalItemData(
      Object session,
      List<SurvivalItemData.Item> hotbar,
      SurvivalItemData.Item offhand,
      List<SurvivalItemData.Item> storage,
      int selected,
      int food,
      float health,
      float maxHealth,
      float absorption,
      float saturation,
      int air,
      int maxAir,
      int fireTicks,
      double fallDistance,
      boolean onGround,
      boolean inWater,
      boolean eyeInFluid,
      boolean alive,
      boolean usingItem,
      boolean manualInput,
      boolean paused) {
    hotbar = List.copyOf(hotbar);
    storage = List.copyOf(storage);
    if (hotbar.size() == 9
        && storage.size() == 27
        && selected >= 0
        && selected <= 8
        && food >= 0
        && food <= 20
        && offhand != null) {
      this.session = session;
      this.hotbar = hotbar;
      this.offhand = offhand;
      this.storage = storage;
      this.selected = selected;
      this.food = food;
      this.health = health;
      this.maxHealth = maxHealth;
      this.absorption = absorption;
      this.saturation = saturation;
      this.air = air;
      this.maxAir = maxAir;
      this.fireTicks = fireTicks;
      this.fallDistance = fallDistance;
      this.onGround = onGround;
      this.inWater = inWater;
      this.eyeInFluid = eyeInFluid;
      this.alive = alive;
      this.usingItem = usingItem;
      this.manualInput = manualInput;
      this.paused = paused;
    } else {
      throw new IllegalArgumentException("Expected the vanilla player inventory");
    }
  }

  public static int hotbarMenuSlot(int value) {
    return 36 + value;
  }

  public static int offhandMenuSlot() {
    return 45;
  }

  public static int storageMenuSlot(int value) {
    return 9 + value;
  }

  public SurvivalItemData.Item readySlot(int value) {
    if (value == 45) {
      return this.offhand;
    } else if (value >= 36 && value <= 44) {
      return this.hotbar.get(value - 36);
    } else {
      throw new IllegalArgumentException("Not a ready slot: " + value);
    }
  }

  public int readyCount() {
    int value = 0;

    for (SurvivalItemData.Item item : this.hotbar) {
      if (SurvivalAllService.isKnownFood(item.id())) {
        value += item.count();
      }
    }

    if (SurvivalAllService.isKnownFood(this.offhand.id())) {
      value += this.offhand.count();
    }

    return value;
  }

  public int storedCount() {
    int value = 0;

    for (SurvivalItemData.Item item : this.storage) {
      if (SurvivalAllService.isKnownFood(item.id())) {
        value += item.count();
      }
    }

    return value;
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
        this.alive);
  }

  public record Item(String id, int count) {
    public static final SurvivalItemData.Item EMPTY = new SurvivalItemData.Item("", 0);

    public Item(String id, int count) {
      if (id != null && count >= 0) {
        this.id = id;
        this.count = count;
      } else {
        throw new IllegalArgumentException("Invalid food item");
      }
    }

    public boolean empty() {
      return this.count <= 0 || this.id.isEmpty();
    }
  }
}
