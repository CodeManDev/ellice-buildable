package dev.felix.ellice.compat;

public enum CompatMode {
   SUPPORTED,
   EXPERIMENTAL,
   BROKEN,
   DEPRECATED;


   private static CompatMode[] $values() {
      return new CompatMode[]{SUPPORTED, EXPERIMENTAL, BROKEN, DEPRECATED};
   }
}
