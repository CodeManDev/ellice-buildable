package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Objects;
import java.util.Optional;

public final class ScaffoldShouldActivateService {
  private static final double value = 1.0E-6;

  public static boolean shouldActivate(
      String text,
      boolean enabled,
      ScaffoldRemapService.Input currentInput,
      double doubleValue,
      double currentDoubleValue) {
    Objects.requireNonNull(text, "mode");
    Objects.requireNonNull(currentInput, "input");
    if (Double.isFinite(doubleValue)
        && !(doubleValue < 0.0)
        && Double.isFinite(currentDoubleValue)
        && !(currentDoubleValue < 0.0)) {
      return enabled
          && "Hold Jump".equals(text)
          && currentInput.jump()
          && !currentInput.hasHorizontalIntent()
          && doubleValue <= currentDoubleValue;
    } else {
      throw new IllegalArgumentException("Invalid tower motion");
    }
  }

  public Optional<PlacementCandidate> plan(
      ScaffoldPlayerSnapshot scaffoldPlayerSnapshot,
      ScaffoldShouldActivateService.Query query,
      double doubleValue,
      double currentDoubleValue,
      int value) {
    Objects.requireNonNull(scaffoldPlayerSnapshot, "frame");
    Objects.requireNonNull(query, "world");
    if (Double.isFinite(doubleValue)
        && !(doubleValue <= 0.0)
        && Double.isFinite(currentDoubleValue)
        && !(currentDoubleValue < 0.0)
        && !(currentDoubleValue > 0.45)
        && value >= 1
        && value <= 4) {
      int currentX = calculateValue(scaffoldPlayerSnapshot.feetPosition().x());
      int currentValue = calculateValue(scaffoldPlayerSnapshot.feetPosition().z());
      double nextX = scaffoldPlayerSnapshot.feetPosition().x() - currentX;
      double nextDoubleValue = scaffoldPlayerSnapshot.feetPosition().z() - currentValue;
      double previousDoubleValue =
          Math.min(Math.min(nextX, 1.0 - nextX), Math.min(nextDoubleValue, 1.0 - nextDoubleValue));
      if (previousDoubleValue + 1.0E-6 < currentDoubleValue) {
        return Optional.empty();
      }

      int currentY = calculateValue(scaffoldPlayerSnapshot.feetPosition().y() + 1.0E-6);

      for (int index = 0; index < value; index++) {
        BlockCoordinates blockCoordinates =
            new BlockCoordinates(currentX, currentY - index, currentValue);
        BlockCoordinates currentBlockCoordinates = blockCoordinates.offset(ScaffoldMode.DOWN);
        if (query.isReplaceable(blockCoordinates)
            && !query.isReplaceable(currentBlockCoordinates)
            && query.isFaceSturdy(currentBlockCoordinates, ScaffoldMode.UP)) {
          RotationVector rotationVector =
              currentBlockCoordinates.facePoint(ScaffoldMode.UP, nextX, nextDoubleValue);
          if (!(rotationVector.subtract(scaffoldPlayerSnapshot.eyePosition()).length()
              > doubleValue)) {
            return Optional.of(
                new PlacementCandidate(
                    blockCoordinates, currentBlockCoordinates, ScaffoldMode.UP, rotationVector));
          }
        }
      }

      return Optional.empty();
    } else {
      throw new IllegalArgumentException("Invalid tower plan bounds");
    }
  }

  private static int calculateValue(double doubleValue) {
    return (int) Math.floor(doubleValue);
  }

  public interface Query {
    boolean isReplaceable(BlockCoordinates blockCoordinates);

    boolean isFaceSturdy(BlockCoordinates blockCoordinates, ScaffoldMode scaffoldMode);
  }
}
