package dev.felix.ellice.feature.rotation;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record RotationTarget(
   int entityId, UUID uuid, RotationMode type, RotationBoundingBox hitbox, List<RotationVector> visibleAimPoints, double effectiveHealth
) {
   public RotationTarget(
      int entityId, UUID uuid, RotationMode type, RotationBoundingBox hitbox, List<RotationVector> visibleAimPoints, double effectiveHealth
   ) {
      Objects.requireNonNull(uuid, "uuid");
      Objects.requireNonNull(type, "type");
      Objects.requireNonNull(hitbox, "hitbox");
      if (Double.isFinite(effectiveHealth) && !(effectiveHealth < 0.0)) {
         visibleAimPoints = List.copyOf(visibleAimPoints);
         if (visibleAimPoints.stream().anyMatch(aimPoint -> !hitbox.contains(aimPoint))) {
            throw new IllegalArgumentException("Visible aim point lies outside hitbox");
         }

         this.entityId = entityId;
         this.uuid = uuid;
         this.type = type;
         this.hitbox = hitbox;
         this.visibleAimPoints = visibleAimPoints;
         this.effectiveHealth = effectiveHealth;
      } else {
         throw new IllegalArgumentException("Health must be finite and nonnegative");
      }
   }

   public RotationTarget(int value, UUID uUID, RotationMode rotationMode, RotationBoundingBox rotationBoundingBox, List<RotationVector> items) {
      this(value, uUID, rotationMode, rotationBoundingBox, items, 20.0);
   }

   public RotationTarget(int value, UUID uUID, RotationMode rotationMode, RotationBoundingBox rotationBoundingBox) {
      this(value, uUID, rotationMode, rotationBoundingBox, rotationBoundingBox.aimSamples());
   }
}
