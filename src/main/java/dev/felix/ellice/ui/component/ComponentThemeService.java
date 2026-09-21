package dev.felix.ellice.ui.component;

import dev.felix.ellice.ui.theme.ThemeIsSetService;
import java.util.Objects;

public final class ComponentThemeService {
   private final ThemeIsSetService values;
   private final Runnable runnable;

   ComponentThemeService(ThemeIsSetService themeIsSet, Runnable currentRunnable) {
      this.values = Objects.requireNonNull(themeIsSet, "theme");
      this.runnable = Objects.requireNonNull(currentRunnable, "invalidate");
   }

   public ThemeIsSetService theme() {
      return this.values;
   }

   public void invalidate() {
      this.runnable.run();
   }
}
