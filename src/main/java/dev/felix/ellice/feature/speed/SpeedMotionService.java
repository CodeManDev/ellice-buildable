package dev.felix.ellice.feature.speed;

final class SpeedMotionService extends SpeedEnableService {
  private final SpeedMotionService.Kind kind;
  private double value;
  private double value2;
  private double value3;
  private int count;

  SpeedMotionService(String text, SpeedOperationHandler speedOperation) {
    super(speedOperation);
    this.kind = SpeedMotionService.Kind.valueOf(text);
  }

  @Override
  protected void reset(SpeedMovingService speedMoving) {
    this.value = base(speedMoving);
    this.value2 = this.value3 = 0.0;
    this.count = 2;
  }

  @Override
  public void motion(SpeedMovingService speedMoving) {
    this.value3 = 0.0;
    if (this.kind == SpeedMotionService.Kind.OnGround) {
      speedMoving.timer(1.0850000381469727);
      if (speedMoving.moving()
          && !speedMoving.jumpKey
          && !speedMoving.obstructed()
          && !speedMoving.horizontalCollision
          && speedMoving.tick % 2 != 0) {
        this.value3 = 0.4;
      }

      if (speedMoving.moving()) {
        speedMoving.strafe(
            0.15 * Math.max(speedMoving.tick % 2 == 0 ? 2.1 : 1.3, base(speedMoving)));
      } else {
        speedMoving.stop();
      }
    } else {
      if (this.kind == SpeedMotionService.Kind.YPort && this.count == 3) {
        this.value3 = speedMoving.jumpAmplifier < 0 ? 0.40453293 : speedMoving.jumpHeight(0.0);
      }

      this.value2 = speedMoving.lastDistance;
    }
  }

  @Override
  public void packet(SpeedMovingService speedMoving, SpeedStrafeHandler.Packet currentPacket) {
    if (currentPacket.hasPosition) {
      currentPacket.y = currentPacket.y + this.value3;
    }
  }

  @Override
  public void afterMotion(SpeedMovingService speedMoving) {
    this.value3 = 0.0;
  }

  @Override
  public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
    if (this.kind != SpeedMotionService.Kind.OnGround) {
      double doubleValue = base(speedMoving);
      if (this.kind == SpeedMotionService.Kind.YPort) {
        if (this.count < 1) {
          this.count++;
          this.value2 = 0.0;
        } else if (speedMoving.ground || this.count == 3) {
          if ((speedMoving.horizontalCollision || speedMoving.forward == 0.0F)
              && speedMoving.sideways == 0.0F) {
            speedMoving.timer(1.0);
          } else if (this.count == 2) {
            this.value = this.value * 2.149;
            this.count = 3;
          } else if (this.count == 3) {
            this.count = 2;
            this.value = this.value2 + 0.66 * (doubleValue - this.value2);
          } else if (speedMoving.collision(0.0, speedMoving.my, 0.0)
              || speedMoving.verticalCollision) {
            this.count = 1;
          }

          this.value = Math.max(doubleValue, this.value);
          currentMove.strafe(speedMoving, this.value);
        }
      } else {
        if (!speedMoving.moving()) {
          this.value = doubleValue;
        }

        int currentValue = this.kind == SpeedMotionService.Kind.Hop ? 1 : 0;
        int currentCount =
            this.count == 1 && speedMoving.verticalCollision && speedMoving.moving() ? 1 : 0;
        if (currentCount != 0) {
          this.value = doubleValue + (currentValue != 0 ? 1.35 : 0.25) - 0.01;
        }

        int nextCount =
            this.count != 2
                    || !speedMoving.verticalCollision
                    || !speedMoving.moving()
                    || currentValue != 0 && !speedMoving.ground
                ? 0
                : 1;
        if (nextCount != 0) {
          currentMove.y = speedMoving.my = currentValue != 0 ? 0.41999998688697815 : 0.4;
          this.value =
              this.value
                  * (currentValue != 0
                      ? 1.533
                      : (this.kind == SpeedMotionService.Kind.OldSlow ? 1.749 : 2.149));
        } else if (this.count == 3) {
          this.value = this.value2 + 0.66 * (doubleValue - this.value2);
        } else if (currentValue != 0 || currentCount == 0) {
          if ((speedMoving.collision(0.0, speedMoving.my, 0.0) || speedMoving.verticalCollision)
              && this.count > 0) {
            this.count =
                (currentValue != 0 || !(1.35 * doubleValue - 0.01 > this.value))
                        && speedMoving.moving()
                    ? 1
                    : 0;
          }

          this.value = this.value2 * 0.9937106918238994;
        }

        this.value = Math.max(doubleValue, this.value);
        if (this.count > 0) {
          currentMove.strafe(speedMoving, this.value);
        }

        if (speedMoving.moving()) {
          this.count++;
        }
      }
    }
  }

  enum Kind {
    Hop,
    OldHop,
    OldSlow,
    OnGround,
    YPort;

    private static SpeedMotionService.Kind[] $values() {
      return new SpeedMotionService.Kind[] {Hop, OldHop, OldSlow, OnGround, YPort};
    }
  }
}
