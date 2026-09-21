package dev.felix.ellice.feature.travel;

import dev.felix.ellice.feature.terrain.TerrainGroundTracker;
import java.util.Objects;

public interface TravelLoadedHandler {
   boolean loaded(int value, int currentValue);

   Double floor(int value, int currentValue, double doubleValue);

   default boolean headroom(int value, double doubleValue, int currentValue) {
      return true;
   }

   default boolean jumpHeadroom(int value, double doubleValue, int currentValue) {
      return this.headroom(value, doubleValue, currentValue);
   }

   default TravelLoadedHandler.Hazard hazard(int value, double doubleValue, int currentValue) {
      return TravelLoadedHandler.Hazard.NONE;
   }

   default boolean water(int value, double doubleValue, int currentValue) {
      return false;
   }

   default boolean climbable(int value, double doubleValue, int currentValue) {
      return false;
   }

   default boolean passableDoor(int value, double doubleValue, int currentValue) {
      return false;
   }

   default boolean placeable(int value, int currentValue, int nextValue) {
      return false;
   }

   default boolean placeAllowed(int value, int currentValue) {
      return true;
   }

   static TravelLoadedHandler fromGround(final TerrainGroundTracker.Ground currentGround) {
      Objects.requireNonNull(currentGround, "ground");
      return new TravelLoadedHandler() {
         @Override
         public boolean loaded(int value, int currentValue) {
            return currentGround.loaded(value, currentValue);
         }

         @Override
         public Double floor(int value, int currentValue, double doubleValue) {
            return currentGround.floor(value, currentValue, doubleValue);
         }
      };
   }

   enum Hazard {
      NONE,
      WATER,
      DANGER,
      VOID;


      private static TravelLoadedHandler.Hazard[] $values() {
         return new TravelLoadedHandler.Hazard[]{NONE, WATER, DANGER, VOID};
      }
   }
}
