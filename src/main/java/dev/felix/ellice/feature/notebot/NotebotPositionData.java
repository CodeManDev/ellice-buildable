package dev.felix.ellice.feature.notebot;

import java.util.Locale;

public record NotebotPositionData(NotebotPositionData.Position position, String instrument, int pitch) {
   public NotebotPositionData(NotebotPositionData.Position position, String instrument, int pitch) {
      if (position != null && instrument != null && pitch >= 0 && pitch <= 24) {
         instrument = instrument.toLowerCase(Locale.ROOT);
         this.position = position;
         this.instrument = instrument;
         this.pitch = pitch;
      } else {
         throw new IllegalArgumentException("Invalid note block");
      }
   }

   public int baseMidi() {
      return switch (this.instrument) {
         case "bass", "didgeridoo" -> 30;
         case "guitar" -> 42;
         case "harp", "iron_xylophone", "bit", "banjo", "pling", "trumpet", "trumpet_exposed", "trumpet_weathered", "trumpet_oxidized" -> 54;
         case "flute", "cow_bell" -> 66;
         case "bell", "chime", "xylophone" -> 78;
         default -> -1;
      };
   }

   public boolean melodic() {
      return this.baseMidi() >= 0;
   }

   public int midi() {
      return this.baseMidi() + this.pitch;
   }

   public int clicksTo(int value) {
      return Math.floorMod(value - this.pitch, 25);
   }

   public record Position(int x, int y, int z) {
   }
}
