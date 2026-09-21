package dev.felix.ellice.module;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public enum ModuleFeatureType {
   COMBAT("Combat", "Targeting, attacks, and combat assistance"),
   MOVEMENT("Movement", "Sprinting, traversal, and building while moving"),
   PLAYER("Player", "Inventory, equipment, healing, and player actions"),
   VISUALS("Visuals", "World rendering, entity visuals, and camera effects"),
   HUD("HUD", "Information anchored to the screen"),
   TOOLS("Tools", "Music, module creation, lookups, and utilities"),
   CLIENT("Client settings", "Shared client appearance and preferences"),
   DEVELOPMENT("Developer tools", "Setting galleries and rendering experiments");

   private static final List<ModuleFeatureType> items = List.of(COMBAT, MOVEMENT, PLAYER, VISUALS, HUD, TOOLS);
   private static final Set<ModuleFeatureType> values2 = Set.of(COMBAT, MOVEMENT, PLAYER);
   private final String text;
   private final String text2;

   public static List<ModuleFeatureType> navigation() {
      return items;
   }

   public boolean isNecessary() {
      return values2.contains(this);
   }

   public static Optional<ModuleFeatureType> find(String name) {
      if (name == null) {
         return Optional.empty();
      }

      String currentName = name.strip().toUpperCase(Locale.ROOT).replace(' ', '_');

      return switch (currentName) {
         case "INTERFACE" -> {
            Optional result = Optional.of(VISUALS);
            yield result;
         }
         case "UTILITY", "SOCIAL", "INTEGRATIONS" -> {
            Optional currentResult = Optional.of(TOOLS);
            yield currentResult;
         }
         case "CLIENT_SETTINGS" -> {
            Optional nextResult = Optional.of(CLIENT);
            yield nextResult;
         }
         case "DEVELOPER_TOOLS" -> {
            Optional previousResult = Optional.of(DEVELOPMENT);
            yield previousResult;
         }
         default -> {
            try {
               yield Optional.of(valueOf(currentName));
            } catch (IllegalArgumentException illegalArgumentException) {
               yield Optional.empty();
            }
         }
      };
   }

   ModuleFeatureType(String currentText, String nextText) {
      this.text = currentText;
      this.text2 = nextText;
   }

   public String displayName() {
      return this.text;
   }

   public String description() {
      return this.text2;
   }


   private static ModuleFeatureType[] $values() {
      return new ModuleFeatureType[]{COMBAT, MOVEMENT, PLAYER, VISUALS, HUD, TOOLS, CLIENT, DEVELOPMENT};
   }
}
