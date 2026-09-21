package dev.felix.ellice.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ModuleNameService {
  private final Object object;
  private final String text;
  private final ModuleNameService moduleNameService;
  private String text2 = "";
  private final List<ModuleNameService> items = new ArrayList<>();
  private final List<ModuleSetting<?>> items2 = new ArrayList<>();

  ModuleNameService(Object value, String currentText, ModuleNameService moduleName) {
    this.object = Objects.requireNonNull(value, "owner");
    this.text = createText(currentText);
    this.moduleNameService = moduleName;
  }

  public String name() {
    return this.text;
  }

  public String description() {
    return this.text2;
  }

  public ModuleNameService description(String text) {
    Objects.requireNonNull(text, "description");
    String currentText = text.trim();
    if (currentText.isEmpty()) {
      throw new IllegalArgumentException("Setting category description must not be blank");
    }

    this.text2 = currentText;
    return this;
  }

  public Optional<ModuleNameService> parent() {
    return Optional.ofNullable(this.moduleNameService);
  }

  public List<ModuleNameService> children() {
    return Collections.unmodifiableList(this.items);
  }

  public List<ModuleSetting<?>> settings() {
    return Collections.unmodifiableList(this.items2);
  }

  public int depth() {
    int index = 0;

    for (ModuleNameService moduleName = this.moduleNameService;
        moduleName != null;
        moduleName = moduleName.moduleNameService) {
      index++;
    }

    return index;
  }

  public List<String> path() {
    ArrayList arrayList = new ArrayList(this.depth() + 1);

    for (ModuleNameService moduleName = this;
        moduleName != null;
        moduleName = moduleName.moduleNameService) {
      arrayList.add(moduleName.text);
    }

    Collections.reverse(arrayList);
    return List.copyOf(arrayList);
  }

  public String qualifiedName() {
    return String.join("/", this.path());
  }

  boolean belongsTo(Object value) {
    return this.object == value;
  }

  ModuleNameService addChild(String currentText) {
    String nextText = createText(currentText);

    for (ModuleNameService moduleName : this.items) {
      if (moduleName.text.equalsIgnoreCase(nextText)) {
        throw new IllegalArgumentException(
            "Setting category already exists below '" + this.qualifiedName() + "': " + nextText);
      }
    }

    ModuleNameService currentModuleName = new ModuleNameService(this.object, nextText, this);
    this.items.add(currentModuleName);
    return currentModuleName;
  }

  void attach(ModuleSetting<?> moduleSetting) {
    Objects.requireNonNull(moduleSetting, "setting");
    if (!this.items2.contains(moduleSetting)) {
      this.items2.add(moduleSetting);
    }
  }

  private static String createText(String text) {
    Objects.requireNonNull(text, "name");
    String currentText = text.trim();
    if (currentText.isEmpty()) {
      throw new IllegalArgumentException("Setting category name must not be blank");
    } else {
      return currentText;
    }
  }

  @Override
  public String toString() {
    return this.qualifiedName();
  }
}
