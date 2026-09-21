package dev.felix.ellice.feature.speed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public final class SpeedStateController {
   private static LocalPlayer localPlayer;
   private static int count = -1;
   private static int count2 = -1;
   private static int count3;
   private static int count4;

   private SpeedStateController() {
   }

   public static int entityId(ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket) {
      return clientboundSetEntityMotionPacket.id();
   }

   public static boolean fallDamage(ClientboundSetEntityMotionPacket clientboundSetEntityMotionPacket) {
      Vec3 vec3 = clientboundSetEntityMotionPacket.movement();
      return vec3.x == 0.0 && vec3.z == 0.0 && Double.doubleToRawLongBits(vec3.y) == -4633060179779189496L;
   }

   public static boolean consuming(ItemStack itemStack) {
      return itemStack.has(DataComponents.CONSUMABLE);
   }

   public static void captureEffects(LocalPlayer localPlayer, SpeedMovingService speedMoving) {
      MobEffectInstance mobEffectInstance = localPlayer.getEffect(MobEffects.SPEED);
      speedMoving.speedAmplifier = mobEffectInstance == null ? -1 : mobEffectInstance.getAmplifier();
      MobEffectInstance currentMobEffectInstance = localPlayer.getEffect(MobEffects.SLOWNESS);
      speedMoving.slownessAmplifier = currentMobEffectInstance == null ? -1 : currentMobEffectInstance.getAmplifier();
      MobEffectInstance nextMobEffectInstance = localPlayer.getEffect(MobEffects.JUMP_BOOST);
      speedMoving.jumpAmplifier = nextMobEffectInstance == null ? -1 : nextMobEffectInstance.getAmplifier();
   }

   public static void tick() {
      LocalPlayer currentLocalPlayer = Minecraft.getInstance().player;
      if (localPlayer != null && (localPlayer != currentLocalPlayer || currentLocalPlayer.tickCount >= count3)) {
         updateState();
      }
   }

   public static void reset() {
      updateState();
      count4 = 0;
   }

   private static void updateState() {
      LocalPlayer currentLocalPlayer = Minecraft.getInstance().player;
      if (localPlayer != null && currentLocalPlayer == localPlayer && count >= 0 && currentLocalPlayer.getInventory().getSelectedSlot() == count2) {
         currentLocalPlayer.getInventory().setSelectedSlot(count);
         currentLocalPlayer.connection.send(new ServerboundSetCarriedItemPacket(count));
      }

      localPlayer = null;
      count2 = -1;
      count = -1;
   }

   public static void piercingAttack(int value, int currentValue, boolean enabled, boolean currentEnabled, boolean nextEnabled, int nextValue, String text) {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer currentLocalPlayer = minecraft.player;
      if (currentLocalPlayer != null
         && minecraft.gameMode != null
         && minecraft.level != null
         && !minecraft.gameMode.isSpectator()
         && (enabled || !currentLocalPlayer.onGround())
         && (currentEnabled || currentLocalPlayer.getFoodData().getFoodLevel() >= 6)
         && (!nextEnabled || currentLocalPlayer.tickCount >= count4)) {
         Reference reference = minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LUNGE);
         int previousValue = -1;
         int sourceValue = 0;
         double doubleValue = 0.0;

         for (int index = 0; index < 9; index++) {
            ItemStack itemStack = currentLocalPlayer.getInventory().getItem(index);
            if (itemStack.has(DataComponents.PIERCING_WEAPON)
               && itemStack.isItemEnabled(minecraft.level.enabledFeatures())
               && !currentLocalPlayer.cannotAttackWithItem(itemStack, 0)
               && itemStack.getMaxDamage() - itemStack.getDamageValue() >= nextValue) {
               int targetValue = EnchantmentHelper.getItemEnchantmentLevel(reference, itemStack);
               if (targetValue > 0) {
                  ItemAttributeModifiers itemAttributeModifiers = (ItemAttributeModifiers)itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
                  double currentDoubleValue = itemAttributeModifiers == null
                     ? 4.0
                     : itemAttributeModifiers.compute(Attributes.ATTACK_SPEED, currentLocalPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED), EquipmentSlot.MAINHAND);
                  if (targetValue > sourceValue || targetValue == sourceValue && currentDoubleValue > doubleValue) {
                     previousValue = index;
                     sourceValue = targetValue;
                     doubleValue = currentDoubleValue;
                  }
               }
            }
         }

         if (previousValue >= 0) {
            if (localPlayer != currentLocalPlayer || count2 != previousValue) {
               updateState();
               localPlayer = currentLocalPlayer;
               count = currentLocalPlayer.getInventory().getSelectedSlot();
               count2 = previousValue;
               currentLocalPlayer.getInventory().setSelectedSlot(previousValue);
               currentLocalPlayer.connection.send(new ServerboundSetCarriedItemPacket(previousValue));
            }

            int inputValue = Math.min(value, currentValue);
            int outputValue = Math.max(value, currentValue);
            count3 = currentLocalPlayer.tickCount + inputValue + currentLocalPlayer.getRandom().nextInt(outputValue - inputValue + 1);
            minecraft.gameMode.piercingAttack((PiercingWeapon)currentLocalPlayer.getInventory().getItem(previousValue).get(DataComponents.PIERCING_WEAPON));
            if (text.equals("Visible")) {
               currentLocalPlayer.swing(InteractionHand.MAIN_HAND);
            } else if (text.equals("Packet")) {
               currentLocalPlayer.connection.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }

            count4 = currentLocalPlayer.tickCount
               + Math.max(
                  1,
                  (int)Math.round(
                     20.0 / Math.max(0.01, doubleValue)
                  )
               );
         }
      }
   }
}
