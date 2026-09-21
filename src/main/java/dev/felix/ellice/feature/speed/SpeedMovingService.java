package dev.felix.ellice.feature.speed;

public final class SpeedMovingService {
  public double x;
  public double y;
  public double z;
  public double mx;
  public double my;
  public double mz;
  public double lastDistance;
  public double fallDistance;
  public double flyDistance;
  public float yaw;
  public float pitch;
  public float forward;
  public float sideways;
  public int tick;
  public int airTicks;
  public int groundTicks;
  public int hurtTime;
  public int speedAmplifier = -1;
  public int slownessAmplifier = -1;
  public int jumpAmplifier = -1;
  public double strafeAcceleration = Double.NaN;
  public boolean ground;
  public boolean verticalCollision;
  public boolean horizontalCollision;
  public boolean sprinting;
  public boolean jumpKey;
  public boolean liquid;
  public boolean climbing;
  public boolean web;
  public boolean carpet;
  public boolean usingItem;
  public boolean consuming;
  public boolean leatherBoots;
  public boolean scaffold;
  public final SpeedMovingService.Actions actions;

  public SpeedMovingService(SpeedMovingService.Actions currentActions) {
    this.actions = currentActions;
  }

  public boolean moving() {
    return this.forward != 0.0F || this.sideways != 0.0F;
  }

  public boolean obstructed() {
    return this.liquid || this.climbing || this.web;
  }

  public double speed() {
    return Math.hypot(this.mx, this.mz);
  }

  public int speedLevel() {
    return Math.max(0, this.speedAmplifier);
  }

  public double jumpHeight(double doubleValue) {
    return doubleValue + Math.max(0, this.jumpAmplifier + 1) * 0.1;
  }

  public double direction() {
    double x = this.yaw;
    if (this.forward < 0.0F) {
      x += 180.0;
    }

    double y = this.forward < 0.0F ? -0.5 : (this.forward > 0.0F ? 0.5 : 1.0);
    if (this.sideways > 0.0F) {
      x -= 90.0 * y;
    }

    if (this.sideways < 0.0F) {
      x += 90.0 * y;
    }

    return Math.toRadians(x);
  }

  public void strafe() {
    this.strafe(this.speed());
  }

  public void strafe(double doubleValue) {
    this.strafe(doubleValue, 1.0);
  }

  public void strafe(double doubleValue, double currentDoubleValue) {
    this.strafeYaw(doubleValue, currentDoubleValue, this.direction());
  }

  public void strafeYaw(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    if (this.moving()) {
      this.mx =
          this.mx * (1.0 - currentDoubleValue)
              - Math.sin(nextDoubleValue) * doubleValue * currentDoubleValue;
      this.mz =
          this.mz * (1.0 - currentDoubleValue)
              + Math.cos(nextDoubleValue) * doubleValue * currentDoubleValue;
    }
  }

  public void multiply(double doubleValue) {
    this.mx *= doubleValue;
    this.mz *= doubleValue;
  }

  public void stop() {
    this.mx = this.mz = 0.0;
  }

  public void forwardBoost(double doubleValue) {
    double currentDoubleValue = Math.toRadians(this.yaw);
    this.mx = this.mx - Math.sin(currentDoubleValue) * doubleValue;
    this.mz = this.mz + Math.cos(currentDoubleValue) * doubleValue;
  }

  public void jump() {
    this.actions.jump(this);
  }

  public void timer(double doubleValue) {
    this.actions.timer(doubleValue);
  }

  public void airSpeed(double doubleValue) {
    this.actions.airSpeed(doubleValue);
  }

  public boolean collision(double doubleValue, double currentDoubleValue, double nextDoubleValue) {
    return this.actions.collision(doubleValue, currentDoubleValue, nextDoubleValue);
  }

  public void positionPacket(double x, boolean enabled) {
    this.actions.positionPacket(this, x, enabled);
  }

  public interface Actions {
    void jump(SpeedMovingService speedMoving);

    void timer(double doubleValue);

    void airSpeed(double doubleValue);

    boolean collision(double doubleValue, double currentDoubleValue, double nextDoubleValue);

    boolean blockAtFeet(double doubleValue, double currentDoubleValue, double nextDoubleValue);

    boolean hasLanding();

    int nearbyEntities(double doubleValue);

    long millis();

    double random();

    void positionPacket(SpeedMovingService speedMoving, double y, boolean enabled);

    void fullPacket(SpeedMovingService speedMoving, boolean enabled);

    void piercingAttack(
        int value,
        int currentValue,
        boolean enabled,
        boolean currentEnabled,
        boolean nextEnabled,
        int nextValue,
        String text);
  }
}
