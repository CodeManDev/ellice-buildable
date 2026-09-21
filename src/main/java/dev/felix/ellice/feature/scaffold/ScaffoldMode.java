package dev.felix.ellice.feature.scaffold;

public enum ScaffoldMode {
   DOWN(0, -1, 0),
   UP(0, 1, 0),
   NORTH(0, 0, -1),
   SOUTH(0, 0, 1),
   WEST(-1, 0, 0),
   EAST(1, 0, 0);

   private final int count;
   private final int count2;
   private final int count3;

   ScaffoldMode(int value, int currentValue, int nextValue) {
      this.count = value;
      this.count2 = currentValue;
      this.count3 = nextValue;
   }

   public int x() {
      return this.count;
   }

   public int y() {
      return this.count2;
   }

   public int z() {
      return this.count3;
   }

   public ScaffoldMode opposite() {
      return switch (this) {
         case DOWN -> UP;
         case UP -> DOWN;
         case NORTH -> SOUTH;
         case SOUTH -> NORTH;
         case WEST -> EAST;
         case EAST -> WEST;
      };
   }


   private static ScaffoldMode[] $values() {
      return new ScaffoldMode[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   }
}
