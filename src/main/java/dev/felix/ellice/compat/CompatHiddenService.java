package dev.felix.ellice.compat;

import dev.felix.ellice.feature.scaffold.PlacementOverrideBus;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;

public final class CompatHiddenService {
   private CompatHiddenService() {
   }

   public static boolean hidden(Entity entity) {
      return entity != null && entity == Minecraft.getInstance().player && entity.getPose() == Pose.CROUCHING && PlacementOverrideBus.hideSneakVisuals();
   }

   public static float eyeHeight(Entity entity) {
      return hidden(entity) ? entity.getEyeHeight(Pose.STANDING) : entity.getEyeHeight();
   }
}
