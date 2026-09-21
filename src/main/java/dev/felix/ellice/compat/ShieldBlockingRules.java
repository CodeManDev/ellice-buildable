package dev.felix.ellice.compat;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.phys.Vec3;

final class ShieldBlockingRules {
   private ShieldBlockingRules() {
   }

   static boolean blocksAttackFrom(LivingEntity livingEntity, DamageSource damageSource) {
      ItemStack itemStack = livingEntity.getItemBlockingWith();
      BlocksAttacks blocksAttacks = itemStack == null ? null : (BlocksAttacks)itemStack.get(DataComponents.BLOCKS_ATTACKS);
      if (blocksAttacks != null && !blocksAttacks.bypassedBy().map(item -> item.contains(damageSource.typeHolder())).orElse(false)) {
         Vec3 vec3 = livingEntity.calculateViewVector(0.0F, livingEntity.getYHeadRot());
         Vec3 currentVec3 = damageSource.getSourcePosition().subtract(livingEntity.position());
         double doubleValue = Math.acos(new Vec3(currentVec3.x, 0.0, currentVec3.z).normalize().dot(vec3));
         return blocksAttacks.resolveBlockedDamage(damageSource, 1.0F, doubleValue) > 0.0F;
      } else {
         return false;
      }
   }

   static boolean canDisableWithHeldWeapon(LocalPlayer localPlayer, LivingEntity livingEntity) {
      ItemStack itemStack = livingEntity.getItemBlockingWith();
      BlocksAttacks blocksAttacks = itemStack == null ? null : (BlocksAttacks)itemStack.get(DataComponents.BLOCKS_ATTACKS);
      return blocksAttacks != null
         && Math.round(localPlayer.getSecondsToDisableBlocking() * blocksAttacks.disableCooldownScale() * 20.0F) > 0;
   }

   static int activationDelayTicks(ItemStack itemStack) {
      BlocksAttacks blocksAttacks = (BlocksAttacks)itemStack.get(DataComponents.BLOCKS_ATTACKS);
      return blocksAttacks == null ? 0 : blocksAttacks.blockDelayTicks();
   }
}
