package dev.felix.ellice.module;

import java.util.List;
import java.util.Locale;

public final class ModuleEntriesService extends ModuleSetting.Mode {
  private final List<ModuleEntriesService.Entry> items;

  public ModuleEntriesService(
      String text, List<ModuleEntriesService.Entry> currentItems, String currentText) {
    super(
        text,
        currentItems.stream().map(ModuleEntriesService.Entry::id).toArray(String[]::new),
        currentText);
    this.items = List.copyOf(currentItems);
  }

  public List<ModuleEntriesService.Entry> entries() {
    return this.items;
  }

  public ModuleEntriesService.Entry selected() {
    return this.items.stream()
        .filter(item -> item.id().equals(this.get()))
        .findFirst()
        .orElseThrow();
  }

  public List<String> editions() {
    return this.items.stream().map(ModuleEntriesService.Entry::edition).distinct().toList();
  }

  public List<String> groups(String text) {
    return this.items.stream()
        .filter(item -> text.isEmpty() || item.edition().equals(text))
        .map(ModuleEntriesService.Entry::group)
        .distinct()
        .sorted()
        .toList();
  }

  public List<ModuleEntriesService.Entry> search(String query, String edition, String group) {
    String[] searchTerms = query.strip().toLowerCase(Locale.ROOT).split("\\s+");
    return this.items.stream()
        .filter(item -> edition.isEmpty() || item.edition().equals(edition))
        .filter(item -> group.isEmpty() || item.group().equals(group))
        .filter(
            item -> {
              String searchableText =
                  (item.label()
                          + " "
                          + item.edition()
                          + " "
                          + item.group()
                          + " "
                          + item.description())
                      .toLowerCase(Locale.ROOT);

              for (String searchTerm : searchTerms) {
                if (!searchableText.contains(searchTerm)) {
                  return false;
                }
              }

              return true;
            })
        .toList();
  }

  public record Entry(
      String id,
      String label,
      String edition,
      String group,
      String description,
      boolean deprecated) {}
}
