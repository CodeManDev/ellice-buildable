package dev.felix.ellice.feature.esp;

import dev.felix.ellice.feature.nametags.NametagsData;

public record EspLabelData(String name, int metres, float health, float absorption, float armor, int teamColor) {
   public EspLabelData(String name, int metres, float health, float absorption, float armor, int teamColor) {
      name = NametagsData.cleanName(name);
      metres = Math.max(0, metres);
      health = calculateValue(health);
      absorption = calculateValue(absorption);
      armor = calculateValue(armor);
      this.name = name;
      this.metres = metres;
      this.health = health;
      this.absorption = absorption;
      this.armor = armor;
      this.teamColor = teamColor;
   }

   private static float calculateValue(float value) {
      return Float.isFinite(value) ? Math.clamp(value, 0.0F, 1.0F) : 0.0F;
   }
}
