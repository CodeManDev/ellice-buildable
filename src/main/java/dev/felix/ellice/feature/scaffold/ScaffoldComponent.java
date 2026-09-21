package dev.felix.ellice.feature.scaffold;

import dev.felix.ellice.compat.CompatProfileTracker;
import dev.felix.ellice.mixin.ScaffoldKeyMappingAccess;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;

public final class ScaffoldComponent {
  private ScaffoldComponent() {}

  public static boolean manualItemUse(Minecraft minecraft) {
    if (minecraft.player.isUsingItem()) {
      return true;
    }

    if (minecraft.player.getMainHandItem().getItem() instanceof BlockItem) {
      return false;
    }

    if (CompatProfileTracker.depletedVisibleHand(minecraft)) {
      return false;
    }

    KeyMapping keyMapping = minecraft.options.keyUse;
    return keyMapping.isDown()
        || ((ScaffoldKeyMappingAccess) keyMapping).ellice$pendingClicks() > 0;
  }

  public static boolean manualAttack(Minecraft minecraft) {
    KeyMapping keyMapping = minecraft.options.keyAttack;
    return keyMapping.isDown()
        || ((ScaffoldKeyMappingAccess) keyMapping).ellice$pendingClicks() > 0;
  }
}
