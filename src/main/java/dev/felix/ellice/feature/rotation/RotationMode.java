package dev.felix.ellice.feature.rotation;

public enum RotationMode {
   PLAYER,
   MOB;


   private static RotationMode[] $values() {
      return new RotationMode[]{PLAYER, MOB};
   }
}
