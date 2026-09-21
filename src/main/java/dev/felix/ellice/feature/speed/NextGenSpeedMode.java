package dev.felix.ellice.feature.speed;

final class NextGenSpeedMode implements SpeedStrafeHandler {
   private final String text;
   private final SpeedOperationHandler speedOperationHandler;
   private int count;
   private int count2 = -1000;
   private int count3;
   private int count4;
   private int count5;
   private int count6;
   private int count7 = -1;
   private int count8 = -1;
   private int count9;
   private int count10;
   private boolean enabled;
   private boolean enabled2;
   private boolean enabled3;
   private boolean enabled4;
   private long timestamp;

   NextGenSpeedMode(String currentText, SpeedOperationHandler speedOperation) {
      this.text = currentText;
      this.speedOperationHandler = speedOperation;
   }

   private boolean checkCondition(String text) {
      return this.speedOperationHandler.bool(text);
   }

   private double calculateValue(String text) {
      return this.speedOperationHandler.number(text);
   }

   @Override
   public void update(SpeedMovingService speedMoving) {
      this.count++;
      int currentCount = this.count - this.count2;
      switch (this.text) {
         case "LegitHop":
         case "Spartan-4.0.4.3":
         case "Spartan-4.0.4.3-FastFall":
            break;
         case "Custom":
            if (speedMoving.moving()) {
               if (this.checkCondition("Horizontal modification")) {
                  speedMoving.multiply(1.0 + this.calculateValue("Horizontal acceleration"));
               }

               if (this.checkCondition("Vertical modification")) {
                  speedMoving.my = speedMoving.my - this.calculateValue(speedMoving.my <= 0.0 ? "Fall pull down" : "Pull down");
               }

               if (this.checkCondition("Strafe")) {
                  if (this.count6 > 0) {
                     this.count6--;
                  } else {
                     speedMoving.strafe(
                        this.checkCondition("Custom speed") ? this.calculateValue("Speed") : speedMoving.speed(), this.calculateValue("Strafe strength")
                     );
                  }
               }

               speedMoving.timer(this.calculateValue("Timer speed"));
            } else if (this.count6 > 0) {
               this.count6--;
            }

            if (this.count8 == this.count) {
               speedMoving.multiply(1.0 + this.calculateValue("Jump off multiplier"));
               this.count8 = -1;
            }

            if (this.count7 == this.count) {
               speedMoving.strafe(this.enabled4 ? Math.max(speedMoving.speed(), 0.2857671997172534) : speedMoving.speed());
               this.count7 = -1;
            }
            break;
         case "YPort":
            if (!speedMoving.ground && speedMoving.moving()) {
               speedMoving.my = -1.0;
            }
            break;
         case "PiercingAttack":
            speedMoving.actions
               .piercingAttack(
                  this.speedOperationHandler.integer("Hold minimum"),
                  this.speedOperationHandler.integer("Hold maximum"),
                  this.checkCondition("On ground"),
                  this.checkCondition("Ignore hunger"),
                  this.checkCondition("Wait for cooldown"),
                  this.speedOperationHandler.integer("Minimum durability"),
                  this.speedOperationHandler.choice("Swing")
               );
            break;
         case "VerusB3882":
            if ((this.count - 1) % 101 == 0) {
               speedMoving.timer(2.0);
            }
            break;
         case "HypixelBHop":
            if (speedMoving.ground) {
               speedMoving.strafe();
            } else {
               speedMoving.multiply(
                  1.0
                     + (
                        this.checkCondition("Horizontal acceleration")
                           ? 4.0E-4
                              + 7.0E-4 * speedMoving.speedLevel()
                           : 0.0
                     )
               );
               if (this.checkCondition("Vertical acceleration") && speedMoving.my < 0.0 && speedMoving.fallDistance < 1.0) {
                  speedMoving.my = speedMoving.my * 1.0004;
               }
            }

            if (this.count7 == this.count) {
               speedMoving.strafe(
                  this.enabled4
                     ? Math.max(speedMoving.speed(), 0.2857671997172534 * speedMoving.speedLevel())
                     : speedMoving.speed()
               );
               this.count7 = -1;
            }
            break;
         case "HypixelLowHop":
            if (speedMoving.ground) {
               speedMoving.strafe();
            } else {
               switch (speedMoving.airTicks) {
                  case 1:
                     speedMoving.strafe();
                     speedMoving.my = speedMoving.my + 0.0568;
                  case 2:
                  case 5:
                  case 6:
                  default:
                     break;
                  case 3:
                     speedMoving.multiply(0.95);
                     speedMoving.my = speedMoving.my - 0.13;
                     break;
                  case 4:
                     speedMoving.my = speedMoving.my - 0.2;
                     break;
                  case 7:
                     if (this.checkCondition("Glide") && checkCondition2(speedMoving)) {
                        speedMoving.my = 0.0;
                     }
               }

               if (checkCondition2(speedMoving)) {
                  speedMoving.strafe();
               }

               if (speedMoving.hurtTime == 9) {
                  speedMoving.strafe(Math.max(speedMoving.speed(), 0.281));
               }

               if (speedMoving.speedLevel() == 2
                  && (speedMoving.airTicks == 1 || speedMoving.airTicks == 2 || speedMoving.airTicks == 5 || speedMoving.airTicks == 6 || speedMoving.airTicks == 8)) {
                  speedMoving.multiply(1.2);
               }
            }
            break;
         case "SentinelDamage":
            if (speedMoving.moving() && this.count >= this.count9 + this.count10) {
               this.count10 = 0;
               this.enabled2 = false;
               this.timestamp = speedMoving.actions.millis();
               speedMoving.positionPacket(0.0, false);
               speedMoving.positionPacket(3.25, false);
               speedMoving.positionPacket(0.0, false);
               speedMoving.positionPacket(0.0, true);
               this.count9 = this.count + this.speedOperationHandler.integer("Reboost ticks");
            }
            break;
         case "Vulcan286":
            if (currentCount == 1) {
               speedMoving.strafe(
                  speedMoving.sideways != 0.0F
                     ? 0.3345
                     : 0.3355
                        * (1.0 + this.count3 * 0.3819)
               );
            }

            if (currentCount == 2 && speedMoving.sprinting) {
               speedMoving.strafe(
                  speedMoving.sideways != 0.0F
                     ? 0.3235
                     : 0.3284
                        * (1.0 + this.count3 * 0.355)
               );
            }

            if (currentCount == 4) {
               speedMoving.my = -0.376;
            }

            if (currentCount == 6 && speedMoving.flyDistance > 0.298) {
               speedMoving.strafe(0.298);
            }
            break;
         case "Vulcan288":
            int value = this.count3 != 0 ? 1 : 0;
            switch (currentCount) {
               case 1:
                  speedMoving.strafe(value != 0 ? 0.605 : 0.31);
                  break;
               case 2:
                  speedMoving.strafe(value != 0 ? 0.57 : 0.29);
                  speedMoving.my = value != 0 ? -0.5 : -0.37;
                  break;
               case 3:
                  speedMoving.strafe(value != 0 ? 0.595 : 0.27);
                  break;
               case 4:
                  speedMoving.strafe(value != 0 ? 0.595 : 0.28);
            }

            if (!speedMoving.ground && Math.abs(speedMoving.fallDistance) > 0.0 && speedMoving.speedLevel() != 0) {
               speedMoving.multiply(1.055);
            }
            break;
         case "VulcanGround286":
            if (speedMoving.moving() && speedMoving.collision(0.0, -0.005, 0.0) && !speedMoving.jumpKey) {
               speedMoving.strafe(
                  speedMoving.speedLevel() > 0
                     ? 0.59
                     : (
                        speedMoving.sideways != 0.0F
                           ? 0.41
                           : 0.42
                     )
               );
               speedMoving.my = 0.005;
            }
            break;
         case "GrimCollide":
            if (speedMoving.moving()) {
               speedMoving.forwardBoost(this.calculateValue("Boost speed") * speedMoving.actions.nearbyEntities(this.calculateValue("Shrink box")));
            }
            break;
         case "NCP":
            if (this.checkCondition("Pull down") && !speedMoving.ground) {
               if (speedMoving.airTicks == this.speedOperationHandler.integer("On tick")) {
                  speedMoving.strafe();
                  speedMoving.my = speedMoving.my - 0.1523351824467155 * this.calculateValue("Motion multiplier");
               }

               if (this.checkCondition("On hurt") && speedMoving.hurtTime >= 5 && speedMoving.my >= 0.0) {
                  speedMoving.my = speedMoving.my - 0.1;
               }
            }

            if (this.checkCondition("Boost") && speedMoving.moving()) {
               speedMoving.multiply(1.0 + 0.00718 * this.calculateValue("Initial boost multiplier"));
            }

            if (speedMoving.moving()) {
               if (speedMoving.ground) {
                  speedMoving.strafe(
                     Math.max(
                        speedMoving.speed(),
                        0.281 + 0.199999999 * speedMoving.speedLevel()
                     )
                  );
               } else if (this.checkCondition("Air strafe")) {
                  speedMoving.strafe(
                     Math.max(
                        speedMoving.speed(),
                        0.2 + 0.199999999 * speedMoving.speedLevel()
                     ),
                     0.7
                  );
               }
            }

            if (this.checkCondition("Timer")) {
               speedMoving.timer(1.08);
            }

            if (this.checkCondition("Damage boost") && speedMoving.hurtTime >= 1) {
               speedMoving.strafe(Math.max(speedMoving.speed(), 0.5));
            }
            break;
         case "Intave14":
            if (this.checkCondition("Strafe") && speedMoving.sprinting && (speedMoving.ground || speedMoving.airTicks == 11)) {
               speedMoving.strafe(speedMoving.speed(), this.calculateValue("Strafe strength"));
            }

            if (this.checkCondition("Air boost") && speedMoving.my > 0.003 && speedMoving.sprinting) {
               speedMoving.multiply(1.00075);
            }
            break;
         case "Intave14Fast":
            if (speedMoving.airTicks == 1) {
               speedMoving.multiply(1.0399999618530273);
            } else if (speedMoving.airTicks >= 2 && speedMoving.airTicks <= 4) {
               speedMoving.multiply(1.0199999809265137);
            }

            if (this.checkCondition("Timer")) {
               speedMoving.timer(1.002);
            }
            break;
         case "HylexLowHop":
            if (speedMoving.ground) {
               if (speedMoving.moving() && speedMoving.speed() < 0.32) {
                  speedMoving.multiply(1.1);
               }
            } else {
               if (speedMoving.airTicks == 9 && speedMoving.speed() < 0.29) {
                  speedMoving.multiply(1.007);
               }

               if (speedMoving.airTicks == 1 && speedMoving.speed() < 0.2) {
                  speedMoving.multiply(1.01);
               }

               if (speedMoving.my > 0.0 && speedMoving.airTicks <= 2 && speedMoving.speed() < 0.2) {
                  speedMoving.multiply(1.02);
               }
            }
            break;
         case "HylexGround":
            if (speedMoving.ground && speedMoving.groundTicks > 5 && speedMoving.hurtTime < 1 && speedMoving.speedLevel() < 1) {
               speedMoving.multiply(
                  speedMoving.sideways == 0.0F ? 1.2174 : 1.214
               );
            }
            break;
         case "BlocksMC":
            if (speedMoving.ground) {
               this.count5 = speedMoving.scaffold ? 2 : 1;
            }

            double doubleValue = 0.06
               + (speedMoving.ground ? 0.12 : 0.21)
               + speedMoving.my / 20.0;
            if (speedMoving.speedLevel() == 1) {
               doubleValue += 0.1;
            }

            if (this.count4 > 0) {
               this.count4--;
               doubleValue -= 0.007 * this.count4;
            }

            if (this.count5 == 1 && speedMoving.airTicks == 4) {
               speedMoving.my = -0.09800000190734863;
            }

            if (this.count5 == 2) {
               switch (speedMoving.airTicks) {
                  case 1:
                     speedMoving.my = speedMoving.my + 0.0568;
                  case 2:
                  default:
                     break;
                  case 3:
                     speedMoving.my = speedMoving.my - 0.13;
                     break;
                  case 4:
                     speedMoving.my = speedMoving.my - 0.2;
               }
            }

            double currentDoubleValue = speedMoving.direction();
            if (this.checkCondition("Round strafe yaw")) {
               currentDoubleValue = Math.toRadians(
                  Math.rint(Math.toDegrees(currentDoubleValue) / 45.0)
                     * 45.0
               );
            }

            if (!speedMoving.ground && this.count5 != 0) {
               speedMoving.strafeYaw(doubleValue, 1.0, currentDoubleValue);
            }
            break;
         case "Matrix7":
            if (speedMoving.moving()) {
               if (speedMoving.ground) {
                  speedMoving.my = 0.419652;
                  speedMoving.strafe();
               } else if (speedMoving.mx * speedMoving.mx + speedMoving.mz * speedMoving.mz < 0.04) {
                  speedMoving.strafe();
               }
            }
            break;
         default:
            throw new IllegalStateException("Unimplemented Nextgen mode: " + this.text);
      }
   }

   @Override
   public boolean autoJump(SpeedMovingService speedMoving) {
      if (speedMoving.ground && speedMoving.moving()) {
         return (boolean)(switch (this.text) {
            case "GrimCollide", "HylexGround", "Spartan-4.0.4.3", "Spartan-4.0.4.3-FastFall" -> 0;
            case "SentinelDamage" -> this.enabled2;
            case "BlocksMC" -> this.count5 != 0 ? 1 : 0;
            default -> 1;
         });
      } else {
         return false;
      }
   }

   @Override
   public boolean cancelJump(SpeedMovingService speedMoving) {
      return this.text.equals("VulcanGround286") && !speedMoving.jumpKey;
   }

   @Override
   public float jumpPower(SpeedMovingService speedMoving, float value) {
      return switch (this.text) {
         case "Custom" -> this.checkCondition("Vertical modification")
               && Math.abs(this.calculateValue("Jump height") - 0.42)
                  > 1.0E-6
            ? (float)this.calculateValue("Jump height")
            : value;
         case "HylexLowHop" -> 0.33F;
         case "NCP" -> this.checkCondition("Low hop") ? 0.4F : value;
         default -> value;
      };
   }

   @Override
   public void beforeJump(SpeedMovingService speedMoving) {
      if (this.text.equals("HypixelBHop")) {
         speedMoving.strafe(
            Math.max(
               speedMoving.speed(),
               this.enabled
                  ? 0.0
                  : 0.281 + 0.008003278196411223 * speedMoving.speedLevel()
            )
         );
      }

      if (this.text.equals("HypixelLowHop")) {
         speedMoving.strafe(
            Math.max(
               speedMoving.speed(),
               0.247 + 0.15 * speedMoving.speedLevel()
            )
         );
      }
   }

   @Override
   public void afterJump(SpeedMovingService speedMoving) {
      this.count2 = this.count;
      this.count3 = speedMoving.speedLevel();
      switch (this.text) {
         case "YPort":
            speedMoving.strafe(this.calculateValue("Speed"));
            break;
         case "VerusB3882":
            speedMoving.multiply(1.100000023841858);
            break;
         case "Vulcan288":
            speedMoving.strafe(
               this.count3 != 0 ? 0.771 : 0.5
            );
            break;
         case "Custom":
            if (this.checkCondition("Horizontal modification") && this.calculateValue("Jump off multiplier") != 0.0) {
               int value = this.speedOperationHandler.integer("Boost delay");
               if (value == 0) {
                  speedMoving.multiply(1.0 + this.calculateValue("Jump off multiplier"));
               } else {
                  this.count8 = this.count + value;
               }
            }
      }
   }

   @Override
   public void move(SpeedMovingService speedMoving, SpeedStrafeHandler.Move currentMove) {
      switch (this.text) {
         case "VerusB3882":
            if (speedMoving.moving()) {
               currentMove.strafe(speedMoving, Math.hypot(currentMove.x, currentMove.z));
            }
            break;
         case "Spartan-4.0.4.3":
         case "Spartan-4.0.4.3-FastFall":
            if (speedMoving.forward <= 0.0F) {
               return;
            }

            boolean enabled = this.text.endsWith("FastFall");
            if (speedMoving.ground) {
               double doubleValue = enabled
                  ? (speedMoving.leatherBoots ? 1.2 : 1.05)
                  : (speedMoving.leatherBoots ? 1.8 : 1.3);
               currentMove.x = speedMoving.mx * doubleValue;
               currentMove.z = speedMoving.mz * doubleValue;
               int value = enabled ? (speedMoving.leatherBoots ? 7 : 3) : 4;

               for (int index = 0; index < value; index++) {
                  speedMoving.jump();
               }

               currentMove.y = enabled ? 0.42 : speedMoving.my;
            } else if (enabled && speedMoving.airTicks == 1) {
               speedMoving.timer(0.5);
               speedMoving.actions.fullPacket(speedMoving, true);
               currentMove.y = -0.0784;
            }
            break;
         case "SentinelDamage":
            if (speedMoving.hurtTime > 0 && !this.enabled2) {
               this.enabled2 = true;
               this.enabled3 = true;
            } else if (speedMoving.hurtTime == 10) {
               this.count10 = (int)Math.ceil(
                  (speedMoving.actions.millis() - this.timestamp) / 50.0
               );
            }

            if ((this.enabled2 || this.enabled3) && speedMoving.moving()) {
               currentMove.strafe(speedMoving, this.calculateValue("Speed"));
            }
      }
   }

   @Override
   public void correction(SpeedMovingService speedMoving) {
      this.enabled = true;
      this.count4 = 20;
   }

   @Override
   public void velocity(SpeedMovingService speedMoving, boolean enabled) {
      if (this.text.equals("HypixelBHop") || this.text.equals("Custom") && this.checkCondition("Strafe knockback")) {
         this.count7 = this.count + 1;
         this.enabled4 = enabled;
      }

      if (this.text.equals("Custom")) {
         this.count6 = this.speedOperationHandler.integer("Velocity timeout");
      }
   }

   @Override
   public void packet(SpeedMovingService speedMoving, SpeedStrafeHandler.Packet currentPacket) {
      if (this.text.equals("Vulcan288") && speedMoving.my < 0.0) {
         currentPacket.ground = true;
      }

      if (this.text.equals("VulcanGround286")
         && !speedMoving.jumpKey
         && speedMoving.collision(0.0, -0.005, 0.0)
         && currentPacket.hasPosition) {
         currentPacket.y = currentPacket.y + 0.005;
      }
   }

   @Override
   public void disable(SpeedMovingService speedMoving) {
      if (this.text.equals("BlocksMC")
         || this.text.equals("SentinelDamage")
         || this.text.equals("Spartan-4.0.4.3-FastFall")) {
         speedMoving.stop();
      }
   }

   private static boolean checkCondition2(SpeedMovingService speedMoving) {
      return speedMoving.my < 0.0 && speedMoving.collision(0.0, -0.66, 0.0);
   }
}

