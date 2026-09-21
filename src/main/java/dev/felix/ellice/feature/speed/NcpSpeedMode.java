package dev.felix.ellice.feature.speed;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class NcpSpeedMode implements SpeedStrafeHandler {
  private final String text;
  private final SpeedOperationHandler speedOperationHandler;
  private int count = 1;
  private int count2;
  private int count3;
  private int count4;
  private int count5;
  private int count6;
  private int count7;
  private int count8;
  private double value = 0.2873;
  private double value2;
  private double value3;
  private float value4;
  private boolean enabled;

  NcpSpeedMode(String currentText, SpeedOperationHandler speedOperation) {
    this.text = currentText;
    this.speedOperationHandler = speedOperation;
  }

  private boolean checkCondition(String text) {
    return this.speedOperationHandler.bool(text);
  }

  @Override
  public void enable(SpeedMovingService speedMoving) {
    switch (this.text) {
      case "NCPBHop":
        this.count =
            !speedMoving.collision(0.0, speedMoving.my, 0.0) && !speedMoving.verticalCollision
                ? 4
                : 1;
        break;
      case "SNCPBHop":
        this.value = 0.0;
        this.count = 4;
        break;
      case "NCPFHop":
        speedMoving.timer(1.0865999460220337);
        break;
      case "NCPHop":
        speedMoving.timer(1.0865000486373901);
    }
  }

  @Override
  public void update(SpeedMovingService speedMoving) {
    switch (this.text) {
      case "NCPFHop":
      case "NCPHop":
        if (!speedMoving.moving()) {
          speedMoving.stop();
          return;
        }

        if (speedMoving.ground) {
          speedMoving.jump();
          if (this.text.equals("NCPFHop")) {
            speedMoving.multiply(1.01);
          }

          speedMoving.airSpeed(0.022299999371170998);
        }

        if (this.text.equals("NCPFHop")) {
          speedMoving.my = speedMoving.my - 9.9999E-4;
        }

        speedMoving.strafe();
        break;
      case "UNCPHop":
        if (speedMoving.obstructed()) {
          return;
        }

        if (!speedMoving.moving()) {
          speedMoving.timer(1.0);
          return;
        }

        if (speedMoving.ground) {
          this.value3 = speedMoving.speedLevel() >= 1 ? 0.4562999904155731 : 0.3384999930858612;
          speedMoving.jump();
        } else {
          this.value3 = this.value3 * 0.9800000190734863;
        }

        if (!speedMoving.ground && speedMoving.fallDistance > 2.0) {
          speedMoving.timer(1.0);
          return;
        }

        speedMoving.strafe(this.value3);
        if (!speedMoving.ground && ++this.count3 % 3 == 0) {
          speedMoving.timer(1.0815000534057617);
          this.count3 = 0;
        } else {
          speedMoving.timer(0.9598000049591064);
        }
        break;
      case "UNCPHopNew":
        if (speedMoving.fallDistance > 2.0) {
          speedMoving.timer(1.0);
          return;
        }

        if (!speedMoving.moving() || speedMoving.obstructed()) {
          return;
        }

        if (speedMoving.ground) {
          if (this.checkCondition("Low hop")) {
            speedMoving.my = 0.4;
          } else {
            speedMoving.jump();
          }

          this.count4 = 0;
          return;
        }

        if (speedMoving.hurtTime <= 1
            && ++this.count4 == this.speedOperationHandler.integer("On tick")
            && this.checkCondition("Pull down")) {
          speedMoving.strafe();
          speedMoving.my = -0.1523351824467155;
        }

        if (this.checkCondition("On hurt")
            && speedMoving.hurtTime >= 2
            && speedMoving.hurtTime <= 4
            && speedMoving.my >= 0.0) {
          speedMoving.my = speedMoving.my - 0.1;
        }

        if (this.checkCondition("Air strafe")) {
          speedMoving.strafe(
              Math.max(speedMoving.speed(), 0.2 + 0.199999999 * speedMoving.speedLevel()), 0.7);
        }

        if (this.checkCondition("Timer")) {
          if (speedMoving.hurtTime <= 1) {
            switch (speedMoving.tick % 5) {
              case 0:
                speedMoving.timer(1.024999976158142);
              case 1:
              case 3:
              default:
                break;
              case 2:
                speedMoving.timer(1.0800000429153442);
                break;
              case 4:
                speedMoving.timer(1.0);
            }
          } else {
            speedMoving.timer(1.0);
          }
        }

        if (this.checkCondition("Boost")) {
          speedMoving.multiply(1.00718);
        }

        if (this.checkCondition("Damage boost") && speedMoving.hurtTime >= 1) {
          speedMoving.strafe(Math.max(speedMoving.speed(), 0.5));
        }
      case "NCPBHop":
      case "SNCPBHop":
      case "NCPYPort":
      case "Boost":
      case "Frame":
      case "MiJump":
      case "OnGround":
        break;
      default:
        throw new IllegalStateException("Unimplemented Legacy NCP mode: " + this.text);
    }
  }

  @Override
  public void motion(SpeedMovingService speedMoving) {
    switch (this.text) {
      case "NCPBHop":
      case "SNCPBHop":
        this.value2 = speedMoving.lastDistance;
        break;
      case "NCPYPort":
        if (speedMoving.obstructed() || !speedMoving.moving()) {
          return;
        }

        if (this.count5 >= 4 && speedMoving.ground) {
          this.count5 = 0;
        }

        if (speedMoving.ground) {
          speedMoving.my = this.count5 <= 1 ? 0.42 : 0.4;
          speedMoving.forwardBoost(0.20000000298023224);
          this.count5++;
        } else if (this.count5 <= 1) {
          speedMoving.my = -5.0;
        }

        speedMoving.strafe();
        break;
      case "Boost":
        double doubleValue = 3.1981;
        double currentDoubleValue = 4.69;
        int value =
            !speedMoving.collision(
                    speedMoving.mx / currentDoubleValue, 0.0, speedMoving.mz / currentDoubleValue)
                ? 1
                : 0;
        if (speedMoving.ground && this.value4 < 1.0F) {
          this.value4 = this.value4 + 0.2F;
        }

        if (!speedMoving.ground) {
          this.value4 = 0.0F;
        }

        if (this.value4 == 1.0F && !speedMoving.obstructed() && speedMoving.moving()) {
          if (!speedMoving.sprinting) {
            currentDoubleValue += 0.8;
          }

          if (speedMoving.sideways != 0.0F) {
            doubleValue -= 0.1;
            currentDoubleValue += 0.5;
          }

          switch (++this.count6) {
            case 1:
              speedMoving.multiply(doubleValue);
              return;
            case 2:
              speedMoving.multiply(0.6858710562414266);
              return;
            case 3:
            default:
              return;
            case 4:
              if (value != 0) {
                speedMoving.x = speedMoving.x + speedMoving.mx / currentDoubleValue;
                speedMoving.z = speedMoving.z + speedMoving.mz / currentDoubleValue;
              }

              this.count6 = 0;
          }
        }
        break;
      case "Frame":
        if (!speedMoving.moving()) {
          return;
        }

        if (speedMoving.ground) {
          speedMoving.jump();
          if (this.count7 == 1) {
            this.count8 = 0;
            if (this.enabled) {
              speedMoving.stop();
              this.enabled = false;
            }

            this.count7 = 0;
          } else {
            this.count7 = 1;
          }
        } else if (!this.enabled && this.count7 == 1 && this.count8 >= 5) {
          speedMoving.multiply(4.25);
          this.enabled = true;
        }

        if (!speedMoving.ground) {
          speedMoving.strafe();
        }

        this.count8++;
        break;
      case "MiJump":
        if (!speedMoving.moving()) {
          return;
        }

        if (speedMoving.ground && !speedMoving.jumpKey) {
          speedMoving.my = speedMoving.my + 0.1;
          speedMoving.multiply(1.8);
          if (speedMoving.speed() > 0.66) {
            speedMoving.multiply(0.66 / speedMoving.speed());
          }
        }

        speedMoving.strafe();
        break;
      case "OnGround":
        if (!speedMoving.moving()
            || speedMoving.fallDistance > 3.994
            || speedMoving.liquid
            || speedMoving.climbing
            || speedMoving.horizontalCollision) {
          return;
        }

        speedMoving.y = speedMoving.y - 0.3993000090122223;
        speedMoving.my = -1000.0;
        speedMoving.timer(1.0);
        if (speedMoving.ground) {
          speedMoving.y = speedMoving.y + 0.3993000090122223;
          speedMoving.my = 0.3993000090122223;
          speedMoving.multiply(1.590000033378601);
          speedMoving.timer(1.1990000009536743);
        }
    }
  }

  @Override
  public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
    if (this.text.equals("NCPBHop") || this.text.equals("SNCPBHop")) {
      boolean enabled = this.text.equals("SNCPBHop");
      this.count2 = (this.count2 + 1) % 5;
      if (this.count2 != 0) {
        speedMoving.timer(1.0);
      } else if (speedMoving.moving()) {
        speedMoving.timer(1.2999999523162842);
        speedMoving.multiply(1.0199999809265137);
      }

      if (speedMoving.ground && speedMoving.moving()) {
        this.count = 2;
      }

      if (calculateValue2(speedMoving.y - (int) speedMoving.y) == calculateValue2(0.138)) {
        speedMoving.my = speedMoving.my - 0.08;
        currentMove.y = currentMove.y - 0.09316090325960147;
        speedMoving.y = speedMoving.y - 0.09316090325960147;
      }

      double doubleValue = calculateValue(speedMoving, enabled);
      if (this.count == 1 && speedMoving.moving()) {
        this.count = 2;
        this.value = 1.35 * doubleValue - 0.01;
      } else if (this.count == 2) {
        this.count = 3;
        speedMoving.my = currentMove.y = 0.399399995803833;
        this.value = this.value * 2.149;
      } else if (this.count == 3) {
        this.count = 4;
        this.value = this.value2 - 0.66 * (this.value2 - doubleValue);
      } else if (enabled && this.count == 88) {
        this.value = doubleValue;
        this.value2 = 0.0;
        this.count = 89;
      } else {
        if (enabled && this.count == 89) {
          if (speedMoving.collision(0.0, speedMoving.my, 0.0) || speedMoving.verticalCollision) {
            this.count = 1;
          }

          this.value2 = 0.0;
          this.value = doubleValue;
          return;
        }

        if (speedMoving.collision(0.0, speedMoving.my, 0.0) || speedMoving.verticalCollision) {
          if (enabled) {
            this.value = doubleValue;
            this.value2 = 0.0;
            this.count = 88;
            return;
          }

          this.count = 1;
        }

        this.value = this.value2 - this.value2 / 159.0;
      }

      this.value = Math.max(this.value, doubleValue);
      currentMove.strafe(speedMoving, this.value);
    }
  }

  private static double calculateValue(SpeedMovingService speedMoving, boolean enabled) {
    return speedMoving.speedAmplifier < 0
        ? 0.2873
        : 0.2873
            * (enabled
                ? 1.0 + 0.2 * (speedMoving.speedAmplifier + 1)
                : 2.0 + 0.2 * speedMoving.speedAmplifier);
  }

  private static double calculateValue2(double currentDoubleValue) {
    return new BigDecimal(currentDoubleValue).setScale(3, RoundingMode.HALF_UP).doubleValue();
  }
}
