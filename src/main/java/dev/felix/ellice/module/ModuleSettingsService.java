package dev.felix.ellice.module;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.core.CoreIsInitializedHandler;
import dev.felix.ellice.event.EventCancelHandler;
import dev.felix.ellice.event.EventIsAfterHandler;
import dev.felix.ellice.event.EventSubscribeService;
import dev.felix.ellice.event.EventTypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class ModuleSettingsService implements Module {
  private final ModuleBuilderData moduleBuilderData;
  private ModuleMode moduleMode = ModuleMode.CREATED;
  private final List<EventCancelHandler> items = new ArrayList<>();
  private final List<ModuleSetting<?>> items2 = new ArrayList<>();
  private final List<ModuleNameService> items3 = new ArrayList<>();
  private final Map<String, Object> text = new LinkedHashMap<>();

  protected ModuleSettingsService(ModuleBuilderData currentModuleBuilderData) {
    this.moduleBuilderData = currentModuleBuilderData;
  }

  protected <S extends ModuleSetting<?>> S setting(S s) {
    return this.createValue(null, (S) s);
  }

  protected <S extends ModuleSetting<?>> S setting(ModuleNameService moduleName, S s) {
    return this.createValue(Objects.requireNonNull(moduleName, "category"), (S) s);
  }

  protected final ModuleNameService settingCategory(String text) {
    ModuleNameService moduleName = new ModuleNameService(this, text, null);

    for (ModuleNameService currentModuleName : this.items3) {
      if (currentModuleName.name().equalsIgnoreCase(moduleName.name())) {
        throw new IllegalArgumentException("Setting category already exists: " + moduleName.name());
      }
    }

    this.items3.add(moduleName);
    return moduleName;
  }

  protected final ModuleNameService settingCategory(ModuleNameService moduleName, String text) {
    Objects.requireNonNull(moduleName, "parent");
    this.updateState(moduleName);
    return moduleName.addChild(text);
  }

  private <S extends ModuleSetting<?>> S createValue(ModuleNameService moduleName, S s) {
    Objects.requireNonNull(s, "setting");
    if (this.items2.contains(s)) {
      throw new IllegalArgumentException("Setting is already registered: " + s.name());
    }

    for (ModuleSetting moduleSetting : this.items2) {
      if (moduleSetting.name().equalsIgnoreCase(s.name())) {
        throw new IllegalArgumentException(
            "Setting name must be unique inside a module: " + s.name());
      }
    }

    if (moduleName != null) {
      this.updateState(moduleName);
      s.assignCategory(moduleName);
      moduleName.attach(s);
    }

    this.items2.add(s);
    if (ModuleIdService.isTunable(s)) {
      this.text.put(s.name(), ModuleIdService.snapshot(s.get()));
    }

    return (S) s;
  }

  private void updateState(ModuleNameService moduleName) {
    if (!moduleName.belongsTo(this)) {
      throw new IllegalArgumentException(
          "Setting category belongs to a different module: " + moduleName);
    }
  }

  @Override
  public List<ModuleSetting<?>> settings() {
    return Collections.unmodifiableList(this.items2);
  }

  @Override
  public List<ModuleIdService> presets() {
    return ContinuousComponent.forModule(this, this.text);
  }

  @Override
  public List<ModuleNameService> settingCategories() {
    return Collections.unmodifiableList(this.items3);
  }

  @Override
  public final ModuleBuilderData meta() {
    return this.moduleBuilderData;
  }

  @Override
  public final ModuleMode lifecycle() {
    return this.moduleMode;
  }

  @Override
  public final void enable() {
    if (!this.moduleMode.canEnable()) {
      throw new IllegalStateException("Cannot enable module in state: " + this.moduleMode);
    }

    this.moduleMode = ModuleMode.ENABLED;
    this.onEnable();
    this.updateState2();
    CoreIsInitializedHandler.LOGGER.debug("Enabled module: {}", this.name());
  }

  @Override
  public final void disable() {
    if (!this.moduleMode.canDisable()) {
      throw new IllegalStateException("Cannot disable module in state: " + this.moduleMode);
    }

    this.items.forEach(EventCancelHandler::cancel);
    this.items.clear();
    this.moduleMode = ModuleMode.DISABLED;
    this.onDisable();
    this.updateState2();
    CoreIsInitializedHandler.LOGGER.debug("Disabled module: {}", this.name());
  }

  protected void onEnable() {}

  protected void onDisable() {}

  private void updateState2() {
    if (!LocalConfigRepository.isSavesSuppressed()) {
      if (CoreIsInitializedHandler.isReady() && CoreIsInitializedHandler.get().config() != null) {
        CoreIsInitializedHandler.get().config().save();
      }
    }
  }

  protected final <T> ModuleSettingsService.ManagedBuilder<T> on(EventTypeService<T> eventType) {
    return new ModuleSettingsService.ManagedBuilder<>(eventType);
  }

  protected final EventSubscribeService bus() {
    return CoreIsInitializedHandler.get().bus();
  }

  protected final List<EventCancelHandler> subscriptions() {
    return Collections.unmodifiableList(this.items);
  }

  protected final class ManagedBuilder<T> {
    private final EventTypeService<T> eventTypeService;
    private EventIsAfterHandler.Priority priority2 = EventIsAfterHandler.Priority.DEFAULT;
    private Predicate<T> predicate = item -> true;

    ManagedBuilder(EventTypeService<T> eventType) {
      this.eventTypeService = eventType;
    }

    public ModuleSettingsService.ManagedBuilder<T> priority(
        EventIsAfterHandler.Priority currentPriority) {
      this.priority2 = currentPriority;
      return this;
    }

    public ModuleSettingsService.ManagedBuilder<T> filter(Predicate<T> currentPredicate) {
      this.predicate = currentPredicate;
      return this;
    }

    public EventCancelHandler run(EventIsAfterHandler<T> eventIsAfter) {
      EventCancelHandler eventCancel =
          ModuleSettingsService.this
              .bus()
              .subscribe(this.eventTypeService, this.priority2, this.predicate, eventIsAfter);
      ModuleSettingsService.this.items.add(eventCancel);
      return eventCancel;
    }
  }
}
