package dev.felix.ellice.feature.combat;

import dev.felix.ellice.feature.rotation.RotationData;
import dev.felix.ellice.feature.rotation.RotationPublishService;
import dev.felix.ellice.feature.scaffold.LocalPhysicsFrameBus;
import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import dev.felix.ellice.feature.scaffold.ScaffoldPublishService;
import java.util.Optional;

public final class CombatOwnsService {
  private static CombatOwnsService.Owner owner;

  private CombatOwnsService() {}

  public static boolean owns(CombatOwnsService.Owner currentOwner) {
    return owner == currentOwner;
  }

  public static Optional<CombatOwnsService.Owner> currentOwner() {
    return Optional.ofNullable(owner);
  }

  public static boolean unclaimed() {
    return owner == null;
  }

  public static void reset() {
    owner = null;
    updateState();
    RotationPublishService.clear();
  }

  public static Optional<RotationData> claim(CombatOwnsService.Owner currentOwner) {
    if (owner != currentOwner) {
      updateState();
      owner = currentOwner;
    }

    return RotationPublishService.current();
  }

  public static void transfer(CombatOwnsService.Owner owner, CombatOwnsService.Owner currentOwner) {
    if (owns(owner)) {
      claim(currentOwner);
    }
  }

  public static void clearFrame(CombatOwnsService.Owner owner) {
    if (owns(owner)) {
      updateState();
      RotationPublishService.clear();
    }
  }

  public static void release(CombatOwnsService.Owner currentOwner) {
    if (owns(currentOwner)) {
      clearFrame(currentOwner);
      owner = null;
    }
  }

  private static void updateState() {
    PlacementOverrideBus.clear();
    ScaffoldPublishService.clear();
    LocalPhysicsFrameBus.clear();
  }

  public enum Owner {
    KILL_AURA,
    BOW,
    RETURN,
    BUCKET,
    ROD,
    SCAFFOLD,
    PEARL,
    TRAVEL;

    private static CombatOwnsService.Owner[] $values() {
      return new CombatOwnsService.Owner[] {
        KILL_AURA, BOW, RETURN, BUCKET, ROD, SCAFFOLD, PEARL, TRAVEL
      };
    }
  }
}
