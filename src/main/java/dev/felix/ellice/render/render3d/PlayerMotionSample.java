package dev.felix.ellice.render.render3d;

import java.util.UUID;

public record PlayerMotionSample(UUID playerId, float velocityX, float velocityY, float velocityZ, float damageAmount) {
   public PlayerMotionSample(UUID playerId, float velocityX, float velocityY, float velocityZ, float damageAmount) {
      if (playerId == null) {
         throw new IllegalArgumentException("playerId is required");
      }

      updateState("velocityX", velocityX);
      updateState("velocityY", velocityY);
      updateState("velocityZ", velocityZ);
      updateState("damageAmount", damageAmount);
      if (!(damageAmount < 0.0F) && !(damageAmount > 1.0F)) {
         this.playerId = playerId;
         this.velocityX = velocityX;
         this.velocityY = velocityY;
         this.velocityZ = velocityZ;
         this.damageAmount = damageAmount;
      } else {
         throw new IllegalArgumentException("damageAmount must be in [0, 1]");
      }
   }

   public PlayerMotionSample(UUID uUID, float value, float currentValue, float nextValue) {
      this(uUID, value, currentValue, nextValue, 0.0F);
   }

   public PlayerMotionSample(UUID uUID) {
      this(uUID, 0.0F, 0.0F, 0.0F);
   }

   public PlayerMotionSample withDamage(float value) {
      return new PlayerMotionSample(this.playerId, this.velocityX, this.velocityY, this.velocityZ, value);
   }

   private static void updateState(String text, float value) {
      if (!Float.isFinite(value)) {
         throw new IllegalArgumentException(text + " must be finite");
      }
   }
}
