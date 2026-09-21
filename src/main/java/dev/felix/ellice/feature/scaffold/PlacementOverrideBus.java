package dev.felix.ellice.feature.scaffold;

import java.util.Objects;
import java.util.Optional;

public final class PlacementOverrideBus {
   private static volatile PlacementOverrideBus.Override override2;
   private static volatile ScaffoldRemapService.Input entries;

   private PlacementOverrideBus() {
   }

   public static void publish(PlacementOverrideBus.Override currentOverride) {
      PlacementOverrideBus.Override nextOverride = Objects.requireNonNull(currentOverride, "override");
      entries = null;
      override2 = nextOverride;
   }

   public static Optional<PlacementOverrideBus.Override> current() {
      return Optional.ofNullable(override2);
   }

   public static boolean captureApplied(ScaffoldRemapService.Input input) {
      Objects.requireNonNull(input, "movement");
      if (override2 != null && entries == null) {
         entries = input;
         return true;
      } else {
         return false;
      }
   }

   public static Optional<ScaffoldRemapService.Input> applied() {
      return Optional.ofNullable(entries);
   }

   public static ScaffoldRemapService.Input serverInput(ScaffoldRemapService.Input input) {
      return input;
   }

   public static boolean hideSneakVisuals() {
      PlacementOverrideBus.Override override = override2;
      return override != null && override.silentSneak() && !override.physicalSneak();
   }

   private static ScaffoldRemapService.Input createMap(ScaffoldRemapService.Input input, boolean enabled) {
      return new ScaffoldRemapService.Input(input.forward(), input.backward(), input.left(), input.right(), input.jump(), enabled, input.sprint());
   }

   public static void clear() {
      override2 = null;
      entries = null;
   }

   public record Override(
      boolean overrideDirections,
      ScaffoldRemapService.Input movement,
      boolean forceSneak,
      boolean suppressSprint,
      boolean overrideJump,
      boolean overrideSprint,
      boolean silentSneak,
      boolean physicalSneak
   ) {
      public Override(
         boolean overrideDirections,
         ScaffoldRemapService.Input movement,
         boolean forceSneak,
         boolean suppressSprint,
         boolean overrideJump,
         boolean overrideSprint,
         boolean silentSneak,
         boolean physicalSneak
      ) {
         Objects.requireNonNull(movement, "movement");
         this.overrideDirections = overrideDirections;
         this.movement = movement;
         this.forceSneak = forceSneak;
         this.suppressSprint = suppressSprint;
         this.overrideJump = overrideJump;
         this.overrideSprint = overrideSprint;
         this.silentSneak = silentSneak;
         this.physicalSneak = physicalSneak;
      }

      public Override(boolean enabled, ScaffoldRemapService.Input input, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled, boolean sourceEnabled) {
         this(enabled, input, currentEnabled, nextEnabled, previousEnabled, sourceEnabled, false, input.sneak());
      }

      public Override(boolean enabled, ScaffoldRemapService.Input input, boolean currentEnabled, boolean nextEnabled, boolean previousEnabled) {
         this(enabled, input, currentEnabled, nextEnabled, previousEnabled, false);
      }

      public Override(boolean enabled, ScaffoldRemapService.Input input, boolean currentEnabled, boolean nextEnabled) {
         this(enabled, input, currentEnabled, nextEnabled, false);
      }

      public PlacementOverrideBus.Override withSilentSneak(boolean enabled, boolean currentEnabled) {
         return new PlacementOverrideBus.Override(
            this.overrideDirections,
            this.movement,
            this.forceSneak,
            this.suppressSprint,
            this.overrideJump,
            this.overrideSprint,
            enabled,
            currentEnabled
         );
      }

      public boolean effectiveSneak() {
         return this.movement.sneak() || this.forceSneak || this.physicalSneak;
      }

      public ScaffoldRemapService.Input localInput() {
         return PlacementOverrideBus.createMap(this.movement, this.effectiveSneak());
      }
   }
}
