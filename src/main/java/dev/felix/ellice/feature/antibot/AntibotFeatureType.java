package dev.felix.ellice.feature.antibot;

import java.util.Arrays;

public enum AntibotFeatureType {
   KILL_AURA("KillAura", false),
   BOW_AIMBOT("BowAimbot", false),
   AUTO_ROD("AutoRod", false),
   AUTO_LAVA("AutoLava", false),
   BACKTRACK("Backtrack", false),
   TICK_BASE("TickBase", false),
   ESP("ESP", true),
   NAMETAGS("Nametags", true),
   PROJECTILE_INFO("Projectile Predictor", true);

   private final String text;
   private final boolean enabled;

   AntibotFeatureType(String currentText, boolean currentEnabled) {
      this.text = currentText;
      this.enabled = currentEnabled;
   }

   public String label() {
      return this.text;
   }

   public boolean visual() {
      return this.enabled;
   }

   public static String[] labels() {
      return Arrays.stream(values()).map(AntibotFeatureType::label).toArray(String[]::new);
   }

   public static String[] combatDefaults() {
      return Arrays.stream(values()).filter(item -> !item.enabled).map(AntibotFeatureType::label).toArray(String[]::new);
   }


   private static AntibotFeatureType[] $values() {
      return new AntibotFeatureType[]{KILL_AURA, BOW_AIMBOT, AUTO_ROD, AUTO_LAVA, BACKTRACK, TICK_BASE, ESP, NAMETAGS, PROJECTILE_INFO};
   }
}
