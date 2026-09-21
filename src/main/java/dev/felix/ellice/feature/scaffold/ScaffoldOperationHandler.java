package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.compat.CombatCompatibility;
import dev.felix.ellice.compat.ScaffoldCompatibility;
import dev.felix.ellice.feature.rotation.RotationData;
import net.minecraft.client.Minecraft;

public interface ScaffoldOperationHandler {
  void prepare(
      Minecraft minecraft,
      ScaffoldCompatibility scaffoldCompatibility,
      CombatCompatibility combatCompatibility,
      ScaffoldRemapService.Input input,
      ScaffoldSettings scaffoldSettings,
      Runnable runnable);

  void observeOutgoing(ScaffoldCompatibility scaffoldCompatibility, Object value);

  void observeServerPacket(ScaffoldCompatibility scaffoldCompatibility, Object value);

  void reconcile(Minecraft minecraft);

  RotationData rotation();

  ScaffoldRemapService.Result movement();

  PlacementCandidate lastPlacement();

  boolean braking();

  String status();

  void suspend(Minecraft minecraft, double doubleValue, double currentDoubleValue);

  void release();

  void reset(Minecraft minecraft, ScaffoldCompatibility scaffoldCompatibility, boolean enabled);
}
