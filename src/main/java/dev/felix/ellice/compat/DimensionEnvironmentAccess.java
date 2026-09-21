package dev.felix.ellice.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;

final class DimensionEnvironmentAccess {
  private DimensionEnvironmentAccess() {}

  static boolean waterEvaporates(Minecraft minecraft, BlockPos blockPos) {
    return (Boolean)
        minecraft
            .level
            .environmentAttributes()
            .getValue(EnvironmentAttributes.WATER_EVAPORATES, blockPos);
  }
}
