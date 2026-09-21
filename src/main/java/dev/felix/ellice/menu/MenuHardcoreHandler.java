package dev.felix.ellice.menu;

import java.util.List;

public interface MenuHardcoreHandler {
   MenuHardcoreHandler.State state();

   void name(String currentName);

   void seed(String text);

   void mode(MenuHardcoreHandler.Mode currentMode);

   void difficulty(MenuHardcoreHandler.Difficulty currentDifficulty);

   void worldType(int value);

   void commands(boolean enabled);

   void structures(boolean enabled);

   void bonusChest(boolean enabled);

   void create();

   void cancel();

   void dispose();

   enum Difficulty {
      PEACEFUL,
      EASY,
      NORMAL,
      HARD;


      private static MenuHardcoreHandler.Difficulty[] $values() {
         return new MenuHardcoreHandler.Difficulty[]{PEACEFUL, EASY, NORMAL, HARD};
      }
   }

   enum Mode {
      SURVIVAL,
      CREATIVE,
      HARDCORE;


      private static MenuHardcoreHandler.Mode[] $values() {
         return new MenuHardcoreHandler.Mode[]{SURVIVAL, CREATIVE, HARDCORE};
      }
   }

   record State(
      String name,
      String folder,
      String seed,
      MenuHardcoreHandler.Mode mode,
      MenuHardcoreHandler.Difficulty difficulty,
      List<String> worldTypes,
      int worldType,
      boolean commands,
      boolean structures,
      boolean bonusChest
   ) {
      public State(
         String name,
         String folder,
         String seed,
         MenuHardcoreHandler.Mode mode,
         MenuHardcoreHandler.Difficulty difficulty,
         List<String> worldTypes,
         int worldType,
         boolean commands,
         boolean structures,
         boolean bonusChest
      ) {
         worldTypes = List.copyOf(worldTypes);
         this.name = name;
         this.folder = folder;
         this.seed = seed;
         this.mode = mode;
         this.difficulty = difficulty;
         this.worldTypes = worldTypes;
         this.worldType = worldType;
         this.commands = commands;
         this.structures = structures;
         this.bonusChest = bonusChest;
      }

      public boolean hardcore() {
         return this.mode == MenuHardcoreHandler.Mode.HARDCORE;
      }
   }
}
