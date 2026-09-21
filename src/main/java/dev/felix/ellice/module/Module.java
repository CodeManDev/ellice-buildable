package dev.felix.ellice.module;

import java.util.List;

public interface Module {
  ModuleBuilderData meta();

  ModuleMode lifecycle();

  void enable();

  void disable();

  default void toggle() {
    if (this.isEnabled()) {
      this.disable();
    } else {
      this.enable();
    }
  }

  default boolean isEnabled() {
    return this.lifecycle() == ModuleMode.ENABLED;
  }

  default List<ModuleSetting<?>> settings() {
    return List.of();
  }

  default List<ModuleIdService> presets() {
    return List.of();
  }

  default List<ModuleNameService> settingCategories() {
    return List.of();
  }

  default String name() {
    return this.meta().name();
  }

  default String description() {
    return this.meta().description();
  }

  default ModuleFeatureType category() {
    return this.meta().category();
  }
}
