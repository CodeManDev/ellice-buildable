package dev.felix.ellice.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class CompatBlocksAttackFromService {
   private CompatBlocksAttackFromService() {
   }

   public static boolean blocksAttackFrom(LivingEntity livingEntity, Vec3 vec3) {
      if (livingEntity == null || vec3 == null || !livingEntity.isAlive() || !Double.isFinite(vec3.lengthSqr())) {
         return false;
      }

      if (livingEntity.getItemBlockingWith() == null) {
         return false;
      }

      Reference reference = livingEntity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK);
      return ShieldBlockingRules.blocksAttackFrom(livingEntity, new DamageSource(reference, vec3));
   }

   public static boolean canDisableWithHeldWeapon(Minecraft minecraft, LivingEntity livingEntity) {
      return minecraft != null && minecraft.player != null && livingEntity instanceof Player && livingEntity.isAlive() && livingEntity.getItemBlockingWith() != null
         ? ShieldBlockingRules.canDisableWithHeldWeapon(minecraft.player, livingEntity)
         : false;
   }

   public static int activationDelayTicks(Minecraft minecraft) {
      return minecraft != null && minecraft.player != null ? ShieldBlockingRules.activationDelayTicks(minecraft.player.getOffhandItem()) : 0;
   }
}
