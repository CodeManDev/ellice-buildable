package dev.felix.ellice.compat;

import dev.felix.ellice.feature.combat.CombatData;
import dev.felix.ellice.feature.combat.CombatOwnsService;
import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationTargetFilter;
import dev.felix.ellice.feature.rotation.RotationFrameContext;
import dev.felix.ellice.feature.rotation.RotationTarget;
import dev.felix.ellice.feature.rotation.RotationVector;
import java.util.Optional;
import net.minecraft.client.Minecraft;

public interface CombatCompatibility {
   Optional<RotationFrameContext> capture(Minecraft minecraft, RotationTargetFilter rotationTargetFilter);

   void applyServerRotation(Minecraft minecraft, RotationData rotationData);

   default void applyServerRotation(Minecraft minecraft, RotationData rotationData, CombatOwnsService.Owner owner) {
      if (CombatOwnsService.owns(owner)) {
         this.applyServerRotation(minecraft, rotationData);
      }
   }

   void clearServerRotation(Minecraft minecraft);

   default void clearServerRotation(Minecraft minecraft, CombatOwnsService.Owner owner) {
      CombatOwnsService.clearFrame(owner);
   }

   boolean hasBlockLineOfSight(Minecraft minecraft, RotationVector rotationVector, RotationVector currentRotationVector);

   Optional<CombatData> captureAttack(Minecraft minecraft, RotationTarget rotationTarget, double doubleValue);

   boolean attack(Minecraft minecraft, RotationTarget rotationTarget, double doubleValue);

   default boolean attack(Minecraft minecraft, RotationTarget rotationTarget, double doubleValue, float value) {
      return this.attack(minecraft, rotationTarget, doubleValue);
   }
}
