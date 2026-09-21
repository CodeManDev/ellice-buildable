package dev.felix.ellice.feature.soup;

import java.util.Objects;

public record SoupKindData(SoupKindData.Kind kind, int count, int maxCount, Object components, boolean ingredient) {
   public static final SoupKindData EMPTY = new SoupKindData(SoupKindData.Kind.EMPTY, 0, 64, "", false);

   public SoupKindData(SoupKindData.Kind kind, int count, int maxCount, Object components, boolean ingredient) {
      Objects.requireNonNull(kind);
      Objects.requireNonNull(components);
      if (count >= 0 && maxCount >= 1) {
         this.kind = kind;
         this.count = count;
         this.maxCount = maxCount;
         this.components = components;
         this.ingredient = ingredient;
      } else {
         throw new IllegalArgumentException("Invalid stack size");
      }
   }

   public boolean empty() {
      return this.count == 0 || this.kind == SoupKindData.Kind.EMPTY;
   }

   public SoupKindData count(int value) {
      return value <= 0 ? EMPTY : new SoupKindData(this.kind, value, this.maxCount, this.components, this.ingredient);
   }

   public boolean merges(SoupKindData soupKindData) {
      return !this.empty() && this.kind == soupKindData.kind && this.components.equals(soupKindData.components) && this.maxCount == soupKindData.maxCount;
   }

   public enum Kind {
      EMPTY,
      SOUP,
      BOWL,
      RED_MUSHROOM,
      BROWN_MUSHROOM,
      OTHER;


      private static SoupKindData.Kind[] $values() {
         return new SoupKindData.Kind[]{EMPTY, SOUP, BOWL, RED_MUSHROOM, BROWN_MUSHROOM, OTHER};
      }
   }
}
