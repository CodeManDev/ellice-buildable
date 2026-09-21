package dev.felix.ellice.module;

public enum ModuleMode {
   CREATED,
   ENABLED,
   DISABLED,
   DESTROYED;

   public boolean canEnable() {
      return this == CREATED || this == DISABLED;
   }

   public boolean canDisable() {
      return this == ENABLED;
   }

   public boolean isTerminal() {
      return this == DESTROYED;
   }


   private static ModuleMode[] $values() {
      return new ModuleMode[]{CREATED, ENABLED, DISABLED, DESTROYED};
   }
}
