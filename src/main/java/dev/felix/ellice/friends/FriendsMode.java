package dev.felix.ellice.friends;

import java.util.EnumSet;
import java.util.Set;

public enum FriendsMode {
   KILL_AURA("KillAura", "Never select or attack this player.", true),
   BOW_AIMBOT("Bow Aimbot", "Do not aim at this player.", true),
   AUTO_LAVA("AutoLava", "Do not place lava for this player.", true),
   BACKTRACK("Backtrack", "Do not delay this player's movement.", true),
   TICK_BASE("TickBase", "Do not plan extra ticks against this player.", true),
   ESP("ESP", "Hide this player's boxes and silhouette effects.", false),
   NAMETAGS("Nametags", "Keep this player's vanilla nameplate.", false),
   PROJECTILE_PREDICTOR(
      "Projectile Predictor", "Exclude from target intel and enemy warnings. Real projectile collisions stay visible.", false
   );

   public final String label;
   public final String hint;
   public final boolean combat;

   FriendsMode(String text, String currentText, boolean enabled) {
      this.label = text;
      this.hint = currentText;
      this.combat = enabled;
   }

   public static Set<FriendsMode> defaults() {
      EnumSet enumSet = EnumSet.noneOf(FriendsMode.class);

      for (FriendsMode friendsMode : values()) {
         if (friendsMode.combat) {
            enumSet.add(friendsMode);
         }
      }

      return Set.copyOf(enumSet);
   }


   private static FriendsMode[] $values() {
      return new FriendsMode[]{KILL_AURA, BOW_AIMBOT, AUTO_LAVA, BACKTRACK, TICK_BASE, ESP, NAMETAGS, PROJECTILE_PREDICTOR};
   }
}
