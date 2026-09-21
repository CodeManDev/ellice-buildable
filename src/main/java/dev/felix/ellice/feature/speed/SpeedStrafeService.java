package dev.felix.ellice.feature.speed;

final class SpeedStrafeService extends SpeedEnableService {
  private final SpeedStrafeService.Kind kind;
  private double value;
  private int count;

  SpeedStrafeService(String text, SpeedOperationHandler speedOperation) {
    super(speedOperation);
    this.kind = SpeedStrafeService.Kind.valueOf(text);
  }

  @Override
  protected void reset(SpeedMovingService speedMoving) {
    this.value = 0.0;
    this.count = 0;
  }

  private double calculateValue(SpeedMovingService speedMoving) {
    if (speedMoving.slownessAmplifier >= 0) {
      return 0.29;
    }

    int value =
        !(Math.abs(speedMoving.forward) >= 0.8) && !(Math.abs(speedMoving.sideways) >= 0.8) ? 0 : 1;
    return 0.221 * (value != 0 ? 1.3F : 1.0F) * (1.0 + 0.2 * potion(speedMoving));
  }

  @Override
  public void strafe(SpeedMovingService speedMoving) {
    if (this.kind == SpeedStrafeService.Kind.NCP) {
      double doubleValue = this.calculateValue(speedMoving);
      if (!speedMoving.moving()) {
        this.value = doubleValue;
        speedMoving.timer(1.0);
      } else {
        if (speedMoving.airTicks == 0) {
          double currentDoubleValue =
              !speedMoving.horizontalCollision && (float) this.values.number("Jump motion") == 0.4F
                  ? 0.4000000059604645
                  : 0.41999998688697815;
          speedMoving.my = speedMoving.jumpHeight(currentDoubleValue);
          this.value = doubleValue * this.values.number("Ground multiplier");
        } else if (speedMoving.airTicks == 1) {
          this.value = this.value + (doubleValue - this.value) * this.values.number("Bunny slope");
        } else {
          this.value = this.value * 0.9937461018562317;
        }

        speedMoving.timer(this.values.number("Timer"));
      }

      if (speedMoving.horizontalCollision) {
        this.value = doubleValue;
      }

      speedMoving.strafeAcceleration =
          Math.max(doubleValue, this.value)
              * (speedMoving.forward != 0.0F && speedMoving.sideways != 0.0F ? 0.98F : 1.0F);
      speedMoving.multiply(speedMoving.actions.random() / 2000.0);
    } else if (this.kind == SpeedStrafeService.Kind.VulcanBHop && speedMoving.moving()) {
      switch (speedMoving.airTicks) {
        case 0:
          speedMoving.jump();
          speedMoving.strafe(speedMoving.speedAmplifier >= 0 ? 0.6 : 0.485);
          break;
        case 1:
        case 2:
          speedMoving.strafe();
        case 3:
        case 4:
        case 6:
        case 7:
        case 8:
        default:
          break;
        case 5:
          speedMoving.my = predict(speedMoving.my, 2);
          break;
        case 9:
          if (speedMoving.actions.blockAtFeet(0.0, speedMoving.my, 0.0)) {
            speedMoving.strafe();
          }
      }
    }
  }

  @Override
  public void motion(SpeedMovingService speedMoving) {
    if (this.kind == SpeedStrafeService.Kind.KoksCraft) {
      if (speedMoving.ground) {
        if (speedMoving.hurtTime == 0) {
          speedMoving.strafe(this.calculateValue(speedMoving) * 0.99);
        }

        speedMoving.jump();
        this.count++;
      }

      if (speedMoving.airTicks == 1 && speedMoving.hurtTime == 0) {
        speedMoving.my = predict(speedMoving.my, this.count % 2 == 0 ? 2 : 4);
      }
    } else if (this.kind == SpeedStrafeService.Kind.VerusHop && speedMoving.moving()) {
      if (speedMoving.ground) {
        speedMoving.jump();
        speedMoving.strafe(0.550000011920929 + 0.09 * potion(speedMoving));
      } else {
        speedMoving.strafe(0.33000001311302185 + 0.084 * potion(speedMoving));
      }

      if (speedMoving.forward <= 0.0F) {
        speedMoving.strafe(0.3 + 0.065 * potion(speedMoving));
      }
    }
  }

  @Override
  public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
    if (this.kind == SpeedStrafeService.Kind.VerusYPort && speedMoving.moving()) {
      if (speedMoving.ground) {
        currentMove.y = 0.41999998688697815;
        speedMoving.strafe(0.6899999976158142 + 0.1 * potion(speedMoving));
        speedMoving.my = 0.0;
      } else {
        speedMoving.strafe(0.4099999964237213 + 0.055 * potion(speedMoving));
      }

      speedMoving.sprinting = true;
    }
  }

  enum Kind {
    NCP,
    KoksCraft,
    VulcanBHop,
    VerusHop,
    VerusYPort;

    private static SpeedStrafeService.Kind[] $values() {
      return new SpeedStrafeService.Kind[] {NCP, KoksCraft, VulcanBHop, VerusHop, VerusYPort};
    }
  }
}
