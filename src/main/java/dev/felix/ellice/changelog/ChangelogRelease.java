package dev.felix.ellice.changelog;

import java.util.List;

public record ChangelogRelease(String version, String date, String title, List<String> highlights, List<ChangelogData> entries) {
   public ChangelogRelease(String version, String date, String title, List<String> highlights, List<ChangelogData> entries) {
      version = version == null ? "" : version;
      date = date == null ? "" : date;
      title = title == null ? "" : title;
      highlights = highlights == null ? List.of() : List.copyOf(highlights);
      entries = entries == null ? List.of() : List.copyOf(entries);
      this.version = version;
      this.date = date;
      this.title = title;
      this.highlights = highlights;
      this.entries = entries;
   }
}
