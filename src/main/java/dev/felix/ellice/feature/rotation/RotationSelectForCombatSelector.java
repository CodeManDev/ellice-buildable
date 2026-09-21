package dev.felix.ellice.feature.rotation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

public final class RotationSelectForCombatSelector {
  public Optional<RotationSelectForCombatSelector.Selection> selectForCombat(
      RotationFrameContext rotationFrameContext,
      Integer value,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue) {
    return Double.isFinite(currentDoubleValue) && !(currentDoubleValue <= 0.0)
        ? this.select(
                rotationFrameContext,
                value,
                Math.min(doubleValue, currentDoubleValue),
                nextDoubleValue)
            .or(() -> this.select(rotationFrameContext, value, doubleValue, nextDoubleValue))
        : this.select(rotationFrameContext, value, doubleValue, nextDoubleValue);
  }

  public Optional<RotationSelectForCombatSelector.Selection> select(
      RotationFrameContext rotationFrameContext,
      Integer value,
      double doubleValue,
      double currentDoubleValue) {
    updateState(doubleValue, currentDoubleValue);
    RotationVector rotationVector = rotationFrameContext.playerRotation().direction();
    double nextDoubleValue = currentDoubleValue * 0.5;
    return rotationFrameContext.entities().stream()
        .map(
            item ->
                findResult2(
                    rotationFrameContext, item, rotationVector, doubleValue, nextDoubleValue))
        .flatMap(Optional::stream)
        .min(
            Comparator.<RotationSelectForCombatSelector.Selection>comparingDouble(
                    item -> calculateValue4(item, value))
                .thenComparingInt(item -> item.entity().entityId()));
  }

  public Optional<RotationSelectForCombatSelector.Selection> selectForCombat(
      RotationFrameContext rotationFrameContext,
      UUID uUID,
      double doubleValue,
      double currentDoubleValue,
      double nextDoubleValue,
      RotationSelectForCombatSelector.Priority priority,
      boolean enabled) {
    updateState(doubleValue, nextDoubleValue);
    double previousDoubleValue =
        Double.isFinite(currentDoubleValue) && currentDoubleValue > 0.0
            ? Math.min(doubleValue, currentDoubleValue)
            : doubleValue;
    return this.findResult(
            rotationFrameContext, uUID, previousDoubleValue, nextDoubleValue, priority, enabled)
        .or(
            () ->
                this.findResult(
                    rotationFrameContext, uUID, doubleValue, nextDoubleValue, priority, false));
  }

  private static void updateState(double doubleValue, double currentDoubleValue) {
    if (!Double.isFinite(doubleValue) || doubleValue <= 0.0) {
      throw new IllegalArgumentException("maxRange must be finite and positive");
    } else if (!Double.isFinite(currentDoubleValue)
        || currentDoubleValue <= 0.0
        || currentDoubleValue > 360.0) {
      throw new IllegalArgumentException("FOV must be in (0, 360]");
    }
  }

  private Optional<RotationSelectForCombatSelector.Selection> findResult(
      RotationFrameContext rotationFrameContext,
      UUID uUID,
      double doubleValue,
      double currentDoubleValue,
      RotationSelectForCombatSelector.Priority priority,
      boolean enabled) {
    List<RotationSelectForCombatSelector.Selection> items =
        rotationFrameContext.entities().stream()
            .map(
                item ->
                    findResult2(
                        rotationFrameContext,
                        item,
                        rotationFrameContext.playerRotation().direction(),
                        doubleValue,
                        currentDoubleValue * 0.5))
            .flatMap(Optional::stream)
            .toList();
    if (enabled && uUID != null) {
      Optional<RotationSelectForCombatSelector.Selection> result =
          items.stream().filter(item -> item.entity().uuid().equals(uUID)).findFirst();
      if (result.isPresent()) {
        return result;
      }
    }

    return items.stream()
        .min(
            Comparator.<RotationSelectForCombatSelector.Selection>comparingDouble(
                    item -> calculateValue(item, priority, uUID))
                .thenComparingInt(item -> item.entity().entityId()));
  }

  private static double calculateValue(
      RotationSelectForCombatSelector.Selection selection,
      RotationSelectForCombatSelector.Priority priority,
      UUID uUID) {
    double doubleValue =
        switch (priority) {
          case SMART ->
              selection.hitDistance() * 8.0
                  + selection.angularDistance() * 0.25
                  + Math.min(40.0, selection.entity().effectiveHealth()) * 0.35;
          case NEAREST -> selection.hitDistance();
          case LOWEST_HEALTH -> selection.entity().effectiveHealth();
          case HIGHEST_HEALTH -> -selection.entity().effectiveHealth();
          case CROSSHAIR -> selection.angularDistance();
        };
    if (priority == RotationSelectForCombatSelector.Priority.SMART
        && selection.entity().uuid().equals(uUID)) {
      doubleValue *= 0.8;
    }

    return doubleValue;
  }

  private static Optional<RotationSelectForCombatSelector.Selection> findResult2(
      RotationFrameContext rotationFrameContext,
      RotationTarget rotationTarget,
      RotationVector rotationVector,
      double doubleValue,
      double currentDoubleValue) {
    RotationVector currentRotationVector = null;
    RotationVector nextRotationVector = null;
    double nextDoubleValue = Double.POSITIVE_INFINITY;
    double previousDoubleValue = Double.POSITIVE_INFINITY;

    for (RotationVector previousRotationVector : rotationTarget.visibleAimPoints()) {
      RotationVector sourceRotationVector =
          previousRotationVector.subtract(rotationFrameContext.eyePosition());
      double currentLength = sourceRotationVector.length();
      if (!(currentLength < 1.0E-9)) {
        RotationVector targetRotationVector = sourceRotationVector.multiply(1.0 / currentLength);
        if (!rotationTarget
            .hitbox()
            .rayIntersection(rotationFrameContext.eyePosition(), targetRotationVector, doubleValue)
            .isEmpty()) {
          double sourceDoubleValue =
              Math.max(-1.0, Math.min(1.0, rotationVector.dot(targetRotationVector)));
          double targetDoubleValue = Math.toDegrees(Math.acos(sourceDoubleValue));
          double inputDoubleValue =
              targetDoubleValue * 0.15
                  + calculateValue3(
                      rotationFrameContext.eyePosition(),
                      rotationTarget.hitbox(),
                      previousRotationVector,
                      rotationTarget.visibleAimPoints());
          if (targetDoubleValue <= currentDoubleValue + 1.0E-9
              && inputDoubleValue < previousDoubleValue) {
            previousDoubleValue = inputDoubleValue;
            nextDoubleValue = targetDoubleValue;
            currentRotationVector = previousRotationVector;
            nextRotationVector = targetRotationVector;
          }
        }
      }
    }

    if (currentRotationVector == null) {
      return Optional.empty();
    }

    OptionalDouble optionalDouble =
        rotationTarget
            .hitbox()
            .rayIntersection(rotationFrameContext.eyePosition(), nextRotationVector, doubleValue);
    if (optionalDouble.isEmpty()) {
      return Optional.empty();
    }

    RotationData rotationData =
        RotationData.lookAt(rotationFrameContext.eyePosition(), currentRotationVector);
    return Optional.of(
        new RotationSelectForCombatSelector.Selection(
            rotationTarget,
            currentRotationVector,
            rotationData,
            optionalDouble.getAsDouble(),
            nextDoubleValue));
  }

  public RotationVector chooseStableAimPoint(
      RotationVector rotationVector,
      RotationBoundingBox rotationBoundingBox,
      RotationVector currentRotationVector,
      List<RotationVector> items) {
    if (items.isEmpty()) {
      throw new IllegalArgumentException("At least one visible point is required");
    }

    RotationData rotationData = RotationData.lookAt(rotationVector, currentRotationVector);
    RotationVector nextRotationVector = (RotationVector) items.get(0);
    double doubleValue =
        calculateValue2(
            rotationVector, rotationBoundingBox, nextRotationVector, rotationData, items);

    for (int index = 1; index < items.size(); index++) {
      RotationVector previousRotationVector = (RotationVector) items.get(index);
      double currentDoubleValue =
          calculateValue2(
              rotationVector, rotationBoundingBox, previousRotationVector, rotationData, items);
      if (currentDoubleValue < doubleValue) {
        nextRotationVector = previousRotationVector;
        doubleValue = currentDoubleValue;
      }
    }

    return nextRotationVector;
  }

  private static double calculateValue2(
      RotationVector rotationVector,
      RotationBoundingBox rotationBoundingBox,
      RotationVector currentRotationVector,
      RotationData rotationData,
      List<RotationVector> items) {
    RotationData currentRotationData = RotationData.lookAt(rotationVector, currentRotationVector);
    return RotationData.distance(rotationData, currentRotationData) * 0.15
        + calculateValue3(rotationVector, rotationBoundingBox, currentRotationVector, items);
  }

  public static boolean hasHorizontalMargin(
      RotationBoundingBox rotationBoundingBox, RotationVector rotationVector) {
    double doubleValue = (rotationBoundingBox.maxX() - rotationBoundingBox.minX()) * 0.2;
    double currentDoubleValue = (rotationBoundingBox.maxZ() - rotationBoundingBox.minZ()) * 0.2;
    return rotationVector.x() >= rotationBoundingBox.minX() + doubleValue
        && rotationVector.x() <= rotationBoundingBox.maxX() - doubleValue
        && rotationVector.z() >= rotationBoundingBox.minZ() + currentDoubleValue
        && rotationVector.z() <= rotationBoundingBox.maxZ() - currentDoubleValue;
  }

  private static double calculateValue3(
      RotationVector rotationVector,
      RotationBoundingBox rotationBoundingBox,
      RotationVector currentRotationVector,
      List<RotationVector> items) {
    double doubleValue = Math.max(1.0E-9, rotationBoundingBox.maxX() - rotationBoundingBox.minX());
    double currentDoubleValue =
        Math.max(1.0E-9, rotationBoundingBox.maxY() - rotationBoundingBox.minY());
    double nextDoubleValue =
        Math.max(1.0E-9, rotationBoundingBox.maxZ() - rotationBoundingBox.minZ());
    int index = 0;

    for (RotationVector nextRotationVector : items) {
      double currentX = Math.abs(nextRotationVector.x() - currentRotationVector.x()) / doubleValue;
      double currentY =
          Math.abs(nextRotationVector.y() - currentRotationVector.y()) / currentDoubleValue;
      double previousDoubleValue =
          Math.abs(nextRotationVector.z() - currentRotationVector.z()) / nextDoubleValue;
      if (currentX <= 0.215 && currentY <= 0.155 && previousDoubleValue <= 0.215) {
        index++;
      }
    }

    RotationData rotationData =
        RotationData.lookAt(rotationVector, rotationBoundingBox.sample(0.5, 0.5, 0.5));
    RotationData currentRotationData = RotationData.lookAt(rotationVector, currentRotationVector);
    double sourceDoubleValue =
        Math.abs(RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw()));
    double targetDoubleValue = Math.abs(rotationData.pitch() - currentRotationData.pitch());
    return sourceDoubleValue + targetDoubleValue * 0.1 - Math.min(27, index) * 0.018;
  }

  private static double calculateValue4(
      RotationSelectForCombatSelector.Selection selection, Integer value) {
    double doubleValue = selection.angularDistance() + selection.hitDistance() * 0.075;
    if (value != null && selection.entity().entityId() == value) {
      doubleValue *= 0.65;
    }

    return doubleValue;
  }

  public enum Priority {
    SMART,
    NEAREST,
    LOWEST_HEALTH,
    HIGHEST_HEALTH,
    CROSSHAIR;

    private static RotationSelectForCombatSelector.Priority[] $values() {
      return new RotationSelectForCombatSelector.Priority[] {
        SMART, NEAREST, LOWEST_HEALTH, HIGHEST_HEALTH, CROSSHAIR
      };
    }
  }

  public record Selection(
      RotationTarget entity,
      RotationVector aimPoint,
      RotationData lookAt,
      double hitDistance,
      double angularDistance) {}
}
