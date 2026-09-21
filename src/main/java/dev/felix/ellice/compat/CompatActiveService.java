package dev.felix.ellice.compat;

import dev.felix.ellice.feature.rotation.RotationData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class CompatActiveService {
   private static boolean enabled;
   private static RotationData rotationData;
   private static float value;
   private static float value2;
   private static float value3;
   private static float value4 = 700.0F;
   private static long timestamp;

   private CompatActiveService() {
   }

   public static boolean active() {
      return enabled;
   }

   public static boolean blocksMouse(Entity entity) {
      if (enabled && entity != null) {
         Minecraft minecraft = Minecraft.getInstance();
         return minecraft != null && entity == minecraft.player;
      } else {
         return false;
      }
   }

   public static void follow(Minecraft minecraft, RotationData currentRotationData, float currentValue, float nextValue) {
      if (minecraft != null && minecraft.player != null && currentRotationData != null) {
         rotationData = currentRotationData.withPitchClamped();
         value3 = calculateValue(currentValue, 0.0F, 1.0F);
         value4 = Math.max(20.0F, nextValue) * 20.0F;
         if (!enabled) {
            value = minecraft.player.getYRot();
            value2 = minecraft.player.getXRot();
            timestamp = 0L;
            enabled = true;
         }
      } else {
         stop();
      }
   }

   public static float[] sample() {
      if (enabled && rotationData != null) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null && minecraft.player != null) {
            long longValue = System.nanoTime();
            float currentValue = timestamp == 0L
               ? 0.016666668F
               : (float)(longValue - timestamp) / 1.0E9F;
            timestamp = longValue;
            RotationData currentRotationData = step(new RotationData(value, value2), rotationData, currentValue, value3, value4);
            value = (float)currentRotationData.yaw();
            value2 = (float)currentRotationData.pitch();
            updateState(minecraft.player);
            return new float[]{value, value2};
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void preserveInterpolation(Entity entity) {
      if (enabled && entity != null) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null && minecraft.player != null && entity == minecraft.player) {
            entity.yRotO = value;
            entity.xRotO = value2;
            if (entity instanceof LivingEntity livingEntity) {
               livingEntity.yHeadRotO = value;
            }
         }
      }
   }

   public static void stop() {
      enabled = false;
      rotationData = null;
      timestamp = 0L;
   }

   public static RotationData step(RotationData rotationData, RotationData currentRotationData, float value, float currentValue, float nextValue) {
      if (rotationData != null && currentRotationData != null) {
         float previousValue = !(value <= 0.0F) && Float.isFinite(value) ? Math.min(value, 0.05F) : 0.0F;
         if (previousValue == 0.0F) {
            return rotationData.withPitchClamped();
         }

         float sourceValue = 0.016F
            + calculateValue(currentValue, 0.0F, 1.0F) * calculateValue(currentValue, 0.0F, 1.0F) * 0.28F;
         float targetValue = 1.0F - (float)Math.exp(-previousValue / Math.max(1.0E-4F, sourceValue));
         double doubleValue = RotationData.yawDelta(rotationData.yaw(), currentRotationData.yaw());
         double currentDoubleValue = currentRotationData.pitch() - rotationData.pitch();
         double nextDoubleValue = doubleValue * targetValue;
         double previousDoubleValue = currentDoubleValue * targetValue;
         double sourceDoubleValue = Math.hypot(nextDoubleValue, previousDoubleValue);
         double targetDoubleValue = Math.max(20.0, nextValue) * previousValue;
         if (sourceDoubleValue > targetDoubleValue && sourceDoubleValue > 1.0E-6) {
            double inputDoubleValue = targetDoubleValue / sourceDoubleValue;
            nextDoubleValue *= inputDoubleValue;
            previousDoubleValue *= inputDoubleValue;
         }

         return new RotationData(rotationData.yaw() + nextDoubleValue, RotationData.clampPitch(rotationData.pitch() + previousDoubleValue));
      } else {
         throw new IllegalArgumentException("view/target");
      }
   }

   private static void updateState(Player player) {
      player.setYRot(value);
      player.setXRot(value2);
      player.setYHeadRot(value);
      player.yRotO = value;
      player.xRotO = value2;
      if (player instanceof LivingEntity) {
         Player currentPlayer = player;
         currentPlayer.yHeadRotO = value;
      }
   }

   private static float calculateValue(float value, float currentValue, float nextValue) {
      return Math.max(currentValue, Math.min(nextValue, value));
   }
}

