package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationVanillaGcdService;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.function.Predicate;

public final class ScaffoldPlacementHeadingService {
  public static double placementHeading(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.WorldVector worldVector,
      double doubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue < 0.0) {
      doubleValue = 0.0;
    }

    double currentX = scaffoldPlayerSnapshot.velocity().x() + worldVector.x() * doubleValue;
    double currentDoubleValue =
        scaffoldPlayerSnapshot.velocity().z() + worldVector.z() * doubleValue;
    if (Math.hypot(currentX, currentDoubleValue) < 0.01) {
      currentX = worldVector.x();
      currentDoubleValue = worldVector.z();
    }

    return Math.toDegrees(Math.atan2(-currentX, currentDoubleValue));
  }

  private ScaffoldPlacementHeadingService() {}

  public static double airAcceleration(ScaffoldRemapService.Input input) {
    return 0.019999999552965164
        * Math.min(1.0, Math.hypot(input.forwardAxis(), input.leftAxis()) * 0.9800000190734863);
  }

  public static ScaffoldPlayerSnapshot nextFrame(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.WorldVector worldVector,
      double doubleValue,
      boolean enabled) {
    return nextFrame(
        scaffoldPlayerSnapshot, worldVector, doubleValue, enabled, 0.41999998688697815);
  }

  public static ScaffoldPlayerSnapshot nextFrame(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldRemapService.WorldVector worldVector,
      double doubleValue,
      boolean enabled,
      double currentDoubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue < 0.0) {
      throw new IllegalArgumentException("Acceleration must be finite and non-negative");
    } else if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue < 0.0)) {
      double currentY =
          scaffoldPlayerSnapshot.onGround()
              ? (enabled ? currentDoubleValue : 0.0)
              : scaffoldPlayerSnapshot.velocity().y();
      RotationVector currentX =
          scaffoldPlayerSnapshot
              .feetPosition()
              .add(
                  new RotationVector(
                      scaffoldPlayerSnapshot.velocity().x() + worldVector.x() * doubleValue,
                      currentY,
                      scaffoldPlayerSnapshot.velocity().z() + worldVector.z() * doubleValue));
      return new ScaffoldPlayerSnapshot(
          currentX.add(
              scaffoldPlayerSnapshot.eyePosition().subtract(scaffoldPlayerSnapshot.feetPosition())),
          currentX,
          scaffoldPlayerSnapshot.velocity(),
          scaffoldPlayerSnapshot.playerRotation(),
          scaffoldPlayerSnapshot.mouseSensitivity(),
          scaffoldPlayerSnapshot.onGround() && (!enabled || !(currentDoubleValue > 0.0)));
    } else {
      throw new IllegalArgumentException("Jump velocity must be finite and non-negative");
    }
  }

  public static ScaffoldPlayerSnapshot clipLanding(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldPlayerSnapshot currentScaffoldPlayerSnapshot,
      int width,
      Predicate<BlockCoordinates> predicate) {
    double doubleValue = width + 1.0;
    if (!scaffoldPlayerSnapshot.onGround()
        && !(scaffoldPlayerSnapshot.velocity().y() >= 0.0)
        && !(scaffoldPlayerSnapshot.feetPosition().y() < doubleValue)
        && !(currentScaffoldPlayerSnapshot.feetPosition().y() >= doubleValue)
        && ScaffoldHasContinuousSupportService.hasContinuousSupport(
            scaffoldPlayerSnapshot.feetPosition(),
            scaffoldPlayerSnapshot.feetPosition(),
            width,
            1.0E-6,
            predicate::test)) {
      RotationVector currentY =
          new RotationVector(
              0.0, doubleValue - currentScaffoldPlayerSnapshot.feetPosition().y(), 0.0);
      return new ScaffoldPlayerSnapshot(
          currentScaffoldPlayerSnapshot.eyePosition().add(currentY),
          currentScaffoldPlayerSnapshot.feetPosition().add(currentY),
          new RotationVector(
              currentScaffoldPlayerSnapshot.velocity().x(),
              0.0,
              currentScaffoldPlayerSnapshot.velocity().z()),
          currentScaffoldPlayerSnapshot.playerRotation(),
          currentScaffoldPlayerSnapshot.mouseSensitivity(),
          true);
    } else {
      return currentScaffoldPlayerSnapshot;
    }
  }

  public static RotationData turnToward(
      RotationData rotationData,
      RotationData currentRotationData,
      double doubleValue,
      double currentDoubleValue) {
    if (Double.isFinite(currentDoubleValue) && !(currentDoubleValue <= 0.0)) {
      double nextDoubleValue = new RotationVanillaGcdService().vanillaGcd(doubleValue);
      double previousDoubleValue =
          RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw());
      double sourceDoubleValue = currentRotationData.pitch() - rotationData.pitch();
      double targetDoubleValue = Math.hypot(previousDoubleValue, sourceDoubleValue);
      double inputDoubleValue =
          targetDoubleValue > currentDoubleValue ? currentDoubleValue / targetDoubleValue : 1.0;
      long longValue = Math.round(previousDoubleValue * inputDoubleValue / nextDoubleValue);
      long currentLongValue = Math.round(sourceDoubleValue * inputDoubleValue / nextDoubleValue);

      while (true) {
        RotationData nextRotationData =
            ScaffoldNextService.applyMouseCounts(
                rotationData, longValue, currentLongValue, doubleValue);
        if (RotationData.distance(rotationData, nextRotationData) <= currentDoubleValue + 1.0E-6) {
          return nextRotationData;
        }

        if (Math.abs(longValue) >= Math.abs(currentLongValue) && longValue != 0L) {
          longValue -= Long.signum(longValue);
        } else {
          if (currentLongValue == 0L) {
            return rotationData;
          }

          currentLongValue -= Long.signum(currentLongValue);
        }
      }
    } else {
      throw new IllegalArgumentException("Invalid turn limit");
    }
  }
}
