package dev.felix.ellice.feature.notebot;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class NotebotTitleService {
   private final String text;
   private final int count;
   private final List<NotebotTitleService.Note> items;
   private final float[] float2;

   public NotebotTitleService(String currentText, int value, List<NotebotTitleService.Note> currentItems, float[] floats) {
      this.text = Objects.requireNonNull(currentText);
      if (value <= 0) {
         throw new IllegalArgumentException("Empty audio");
      }

      this.count = value;
      this.items = currentItems.stream()
         .filter(item -> item.tick() < value)
         .sorted(
            Comparator.comparingInt(NotebotTitleService.Note::tick)
               .thenComparing(Comparator.comparingDouble(NotebotTitleService.Note::strength).reversed())
         )
         .toList();
      this.float2 = (float[])floats.clone();
   }

   public String title() {
      return this.text;
   }

   public int durationTicks() {
      return this.count;
   }

   public List<NotebotTitleService.Note> notes() {
      return this.items;
   }

   public int waveformSize() {
      return this.float2.length;
   }

   public float amplitude(int index) {
      return this.float2[index];
   }

   public double seconds() {
      return this.count / 20.0;
   }

   public static String time(double doubleValue) {
      int value = Math.max(0, (int)doubleValue);
      return "%d:%02d".formatted(value / 60, value % 60);
   }

   public static String pitchName(int value) {
      String[] strings = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
      return strings[Math.floorMod(value, 12)] + (value / 12 - 1);
   }

   public record Note(int tick, int midi, float strength) {
      public Note(int tick, int midi, float strength) {
         if (tick >= 0 && midi >= 0 && midi <= 127 && Float.isFinite(strength) && !(strength <= 0.0F)) {
            this.tick = tick;
            this.midi = midi;
            this.strength = strength;
         } else {
            throw new IllegalArgumentException("Invalid note");
         }
      }
   }
}
